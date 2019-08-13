package com.ukyoo.v2client.repository

import android.text.TextUtils
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import com.uber.autodispose.autoDisposable
import com.ukyoo.v2client.data.Resource
import com.ukyoo.v2client.data.api.HtmlService
import com.ukyoo.v2client.data.api.JsonService
import com.ukyoo.v2client.data.entity.*
import com.ukyoo.v2client.entity.*
import com.ukyoo.v2client.util.ContentUtils
import com.ukyoo.v2client.util.ErrorHanding
import com.ukyoo.v2client.util.ToastUtil
import com.ukyoo.v2client.util.async
import io.reactivex.Flowable
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import retrofit2.HttpException
import java.util.ArrayList
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * 主题详情Repository
 */
@Singleton
class DetailRepository @Inject constructor(
    @Named("non_cached")
    private val htmlService: HtmlService,
    @Named("cached")
    private val htmlService2: HtmlService,
    private val jsonService: JsonService
) {

    var replyList = ObservableArrayList<ReplyModel>()


    /**
     *  查看主题信息和回复
     */
    fun getTopicInfoAndRepliesByTopicId(topicId: Int, isRefresh: Boolean): LiveData<Resource<DetailModel>> {
        val result = MutableLiveData<Resource<DetailModel>>()

        htmlService2.getTopicAndRepliesByTopicId(topicId, getPage(isRefresh))
            .map { response ->
                return@map parse(response, true, topicId)
            }
            .async()
            .doOnSubscribe {
                result.value = Resource.loading()
            }
            .subscribe({ data ->
                result.value = Resource.success(data)
            }, {
                result.value = Resource.error(ErrorHanding.handleError(it), null)
            })

        return result

//            .subscribe({
        //更新主题和回复列表
//                loadMore.set(it.currentPage < it.totalPage)
//
//                if (isRefresh) {
//                    empty.set(it.replies.isEmpty())
//                    replyList.clear()
//                    multiDataList.clear()
//                    multiDataList.add(it.topic) //主题内容
//                }
//
//                replyList.addAll(it.replies)
//                multiDataList.addAll(it.replies) //回复列表
//            }, {
//
//            })
    }


    private fun getPage(isRefresh: Boolean) = if (isRefresh) 1 else (replyList.size / 100) + 1

//    private lateinit var topic: TopicInfo

    /**
     * 解析html
     */
    fun parse(responseBody: String, parseTopic: Boolean, topicId: Int): DetailModel {
        val doc = Jsoup.parse(responseBody)
        val body = doc.body()

        //解析主题信息
//        if (parseTopic) {
        val topic = parseTopicModel(doc, body)
        topic.id = topicId
//        }

        //解析回复信息
        val replyList = ArrayList<ReplyItem>()
        val elements = body.getElementsByAttributeValueMatching("id", Pattern.compile("r_(.*)"))
        for (el in elements) {
            val reply = parseReplyModel(el)
            replyList.add(reply)
        }

        //解析页码
        val pages = ContentUtils.parsePage(body)

        return DetailModel()
            .apply {
                replies = replyList
                topicInfo = topic
                currentPage = pages[0]
                totalPage = pages[1]
            }
    }

    private fun parseReplyModel(element: Element): ReplyItem {
        val reply = ReplyItem()
        val memberInfo = ReplyItem.MemberInfo()

        val tdNodes = element.getElementsByTag("td")
        for (tdNode in tdNodes) {
            val avatars = tdNode.getElementsByClass("avatar")
            if (avatars.size > 0) {
                val avatarNode = tdNode.getElementsByTag("img")
                if (avatarNode != null) {
                    var avatarString = avatarNode.attr("src")
                    if (avatarString.startsWith("//")) {
                        avatarString = "https:$avatarString"
                    }
                    memberInfo.avatar = avatarString
                }
            }

            val replyElements = tdNode.getElementsByClass("reply_content")
            if (replyElements.size > 0) {
                reply.content = replyElements.text()
                reply.contentRendered = ContentUtils.formatContent(replyElements.html())
            }

            val agos = tdNode.getElementsByClass("ago")
            if (agos.size > 0) {
                reply.created = agos.text()
            }

            val aNodes = tdNode.getElementsByTag("a")
            for (aNode in aNodes) {
                if (aNode.toString().contains("/member/")) {
                    memberInfo.username = aNode.attr("href").replace("/member/", "")
                    break
                }
            }
        }

        reply.member = memberInfo
        return reply
    }

    private fun parseTopicModel(doc: Document, body: Element): TopicInfo {
        val topic = TopicInfo()
        val userInfo = TopicInfo.UserInfo()
        val nodeInfo = TopicInfo.NodeInfo()

        var title = doc.title()
        if (title.endsWith("- V2EX"))
            title = title.substring(0, title.length - 6).trim { it <= ' ' }
        topic.title = title

        val header = body.getElementsByClass("header")
        if (header.size == 0) throw Exception("fail to parse topic")

        val aNodes = header[0].getElementsByTag("a")
        for (aNode in aNodes) {
            val content = aNode.toString()
            if (content.contains("/member/")) {
                var member = aNode.attr("href")
                member = member.replace("/member/", "")
                userInfo.username = member

                val avatarNode = aNode.getElementsByTag("img")
                if (avatarNode != null && (userInfo.avatar.isEmpty())) {
                    var avatar = avatarNode.attr("src")
                    if (avatar.startsWith("//"))
                        avatar = "https:$avatar"
                    userInfo.avatar = avatar
                }
            } else if (content.contains("/go/")) {
                val node = aNode.attr("href")
                nodeInfo.name = node.replace("/go/", "")
                nodeInfo.title = aNode.text()
            }
        }

        var dateString = header[0].getElementsByClass("gray").text()
        val components = dateString.split("·".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (components.size >= 2) {
            dateString = components[1].trim { it <= ' ' }
            topic.created = dateString
        }

        val hNodes = header[0].getElementsByTag("h1")
        if (hNodes != null) {
            topic.title = hNodes.text()
        }

        val contentNodes = body.getElementsByClass("topic_content")
        if (contentNodes != null && contentNodes.size > 0) {
            topic.content = contentNodes[0].text()
            topic.contentRendered = ContentUtils.formatContent(contentNodes[0].html())
        } else {
            topic.contentRendered = ""
            topic.content = topic.contentRendered
        }

        val boxNodes = body.getElementsByClass("box")
        var got = false
        for (boxNode in boxNodes) {
            if (got) break
            val spanNodes = boxNode.getElementsByTag("span")
            if (spanNodes != null) {
                for (spanNode in spanNodes) {
                    val spanString = spanNode.text()
                    if (!spanString.contains("回复"))
                        continue
                    val components2 = spanString.split(" \\| ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    if (components2.size < 2) {
                        topic.replies = 0
                    } else {
                        var replyCount = components2[0].replace("回复", "")
                        replyCount = replyCount.trim { it <= ' ' }
                        topic.replies = Integer.parseInt(replyCount)
                    }
                    got = true
                    break
                }
            }
        }

        topic.member = userInfo
        topic.node = nodeInfo
        return topic
    }


    /**
     * 获取回复需要的ONCE
     */
    fun makeReply(topicId: Int, replyContent: String) : Flowable<String>{
        return htmlService2.getReplyOnce(topicId)
            .flatMap { content ->
                val pattern = Pattern.compile("<input type=\"hidden\" value=\"([0-9]+)\" name=\"once\" />")
                val matcher = pattern.matcher(content)
                if (matcher.find()) {
                    val once = matcher.group(1)

                    if (TextUtils.isEmpty(replyContent)) {
                        Flowable.error(ErrorHanding.CustomException("回复内容不得为空"))
                    } else {
                        //回复
                        val url = "https://www.v2ex.com/t/$topicId"
                        htmlService2.reply(url, topicId, replyContent, once)
                    }
                } else {
                    Flowable.error(ErrorHanding.CustomException("请检查登录"))
                }
            }
    }


//    .subscribe({ response ->
//        ErrorHanding.getProblemFromHtmlResponse(response).apply {
//            ToastUtil.shortShow(this)
//            Logger.d(this)
//        }
//    }, { throwable ->
//        if (throwable is HttpException && throwable.code() == 302) {
//            Logger.d("回复成功了")
//        } else {
//            ToastUtil.shortShow(ErrorHanding.handleError(throwable))
//        }
//    })
}

