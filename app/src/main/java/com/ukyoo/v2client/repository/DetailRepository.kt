package com.ukyoo.v2client.repository

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ukyoo.v2client.data.api.HtmlService
import com.ukyoo.v2client.data.api.JsonService
import com.ukyoo.v2client.data.entity.*
import com.ukyoo.v2client.util.ContentUtils
import com.ukyoo.v2client.util.async
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
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
    var htmlService: HtmlService, var jsonService: JsonService
) {

    var replyList = ObservableArrayList<ReplyModel>()

    /**
     *  回复列表
     */
    fun getRepliesByTopicId(topicId: Int, isRefresh: Boolean): LiveData<List<ReplyModel>> {
        val list: LiveData<List<ReplyModel>> = MutableLiveData()

        jsonService.getRepliesByTopicId(topicId)
            .async()
            .map {
                if (isRefresh) {
                    replyList.clear()
                }
                replyList.addAll(it)
                return@map it
            }.doOnSubscribe {

            }.doFinally {

            }.subscribe({

            }, {

            })

        return list
    }


    /**
     *  查看主题信息
     */
    fun getTopicInfo(topicId: Int) {

        jsonService.getTopicByTopicId(topicId)
            .async()
            .doOnSubscribe {

            }.doFinally {

            }.subscribe({
                //                mTopic = it[0]

                getRepliesByTopicId(topicId, true)
            }, {

            })
    }

    /**
     *  查看主题信息和回复
     */
    fun getTopicInfoAndRepliesByTopicId(topicId: Int, isRefresh: Boolean) :  {
        htmlService.getTopicAndRepliesByTopicId(topicId, getPage(isRefresh))
            .async()
            .map { response ->
                return@map parse(response, true, topicId)
            }
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


    lateinit var topic: TopicModel
    lateinit var replies: ArrayList<ReplyModel>
    var totalPage: Int = 0
    var currentPage: Int = 0


    fun parse(responseBody: String, parseTopic: Boolean, id: Int){
        val doc = Jsoup.parse(responseBody)
        val body = doc.body()

        topic = TopicModel()
        topic.id = id

        if (parseTopic) {
            try {
                parseTopicModel(doc, body)
            } catch (e: Exception) {
                android.util.Log.w("parse_topic", e.toString())
            }

        }

        replies = ArrayList<ReplyModel>()
        val elements = body.getElementsByAttributeValueMatching("id", Pattern.compile("r_(.*)"))
        for (el in elements) {
            try {
                val reply = parseReplyModel(el)
                replies.add(reply)
            } catch (e: Exception) {
//                android.util.Log.w("parse_reply", e.toString())
            }
        }

        val pages = ContentUtils.parsePage(body)
        currentPage = pages[0]
        totalPage = pages[1]
    }

    private fun parseReplyModel(element: Element): ReplyModel {
        val reply = ReplyModel()
        reply.member = MemberModel()

        val tdNodes = element.getElementsByTag("td")
        for (tdNode in tdNodes) {
            val avatars = tdNode.getElementsByClass("avatar")
            if (avatars.size > 0) {
                val avatarNode = tdNode.getElementsByTag("img")
                if (avatarNode != null) {
                    var avatarString = avatarNode.attr("src")
                    if (avatarString.startsWith("//")) {
                        avatarString = "http:$avatarString"
                    }
                    reply.member.avatar = avatarString
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
                    reply.member.username = aNode.attr("href").replace("/member/", "")
                    break
                }
            }
        }
        return reply
    }

    @Throws(Exception::class)
    internal fun parseTopicModel(doc: Document, body: Element) {
        var title = doc.title()
        if (title.endsWith("- V2EX"))
            title = title.substring(0, title.length - 6).trim { it <= ' ' }
        topic.title = title

        val header = body.getElementsByClass("header")
        if (header.size == 0) throw Exception("fail to parse topic")

        topic.member = MemberModel()
        topic.node = NodeModel()
        val aNodes = header[0].getElementsByTag("a")
        for (aNode in aNodes) {
            val content = aNode.toString()
            if (content.contains("/member/")) {
                var member = aNode.attr("href")
                member = member.replace("/member/", "")
                topic.member.username = member

                val avatarNode = aNode.getElementsByTag("img")
                if (avatarNode != null && (topic.member.avatar == null || topic.member.avatar.isEmpty())) {
                    var avatar = avatarNode.attr("src")
                    if (avatar.startsWith("//"))
                        avatar = "http:$avatar"
                    topic.member.avatar = avatar
                }
            } else if (content.contains("/go/")) {
                val node = aNode.attr("href")
                topic.node.name = node.replace("/go/", "")
                topic.node.title = aNode.text()
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
    }
}

