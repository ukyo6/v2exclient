package com.ukyoo.v2client.repository

import android.text.TextUtils
import com.orhanobut.logger.Logger
import com.ukyoo.v2client.data.api.HtmlService
import com.ukyoo.v2client.data.api.JsonService
import com.ukyoo.v2client.data.entity.ReplyListModel
import com.ukyoo.v2client.entity.MemberModel
import com.ukyoo.v2client.entity.ReplyItem
import com.ukyoo.v2client.entity.TopicModel
import com.ukyoo.v2client.util.ContentUtils
import com.ukyoo.v2client.util.ErrorHanding
import com.ukyoo.v2client.util.ToastUtil
import com.ukyoo.v2client.util.async
import io.reactivex.Flowable
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.util.ArrayList
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * 用户信息(详细)
 * 发表的主题列表
 * 回复列表
 */
@Singleton
class UserInfoRepository @Inject constructor(
    @Named("non_cached")
    private val htmlService: HtmlService,
    private val jsonService: JsonService
) {


    /**
     * 获取用户详细信息
     */
    fun getUserInfo(username: String): Flowable<MemberModel> {
        return jsonService.getUserInfo(username)
            .async()

    }

    /**
     * 获取创建的主题
     */
    fun getUserTopics(userName: String, page: Int): Flowable<ArrayList<TopicModel>> {
        return htmlService.getUserTopics(userName, page)
            .map { responseStr ->
                parse(responseStr)
            }
            .async()
    }

    /**
     * 获取用户回复
     */
    fun getUserReplies(userName: String, page: Int) {
        htmlService.getUserTopics(userName, page)
            .map { responseStr ->
                ReplyListModel().parse(responseStr)
            }
            .async()
    }

    private var mCurrentPage = 1
    private var mTotalPage = 1

    fun parse(responseBody: String): ArrayList<TopicModel> {
        val topics = ArrayList<TopicModel>()

        val doc = Jsoup.parse(responseBody)
        val body = doc.body()
        val elements = body.getElementsByAttributeValue("class", "cell item")
        for (el in elements) {
            try {
                val model = parseTopicModel(el, true)
                topics.add(model)
            } catch (e: Exception) {
                Logger.d("解析节点下列表失败")
            }
        }

        //页码
        val pages = ContentUtils.parsePage(body)
        mCurrentPage = pages[0]
        mTotalPage = pages[1]

        return topics
    }

    private fun parseTopicModel(el: Element, parseNode: Boolean): TopicModel {
        val tdNodes = el.getElementsByTag("td")

        val topic = TopicModel()

        val member = TopicModel.UserInfo() //用户信息
        val node = TopicModel.NodeInfo()  //节点信息

        for (tdNode in tdNodes) {
            val content = tdNode.toString()
            if (content.contains("class=\"avatar\"")) {  //用户信息
                //用户名
                val userIdNode = tdNode.getElementsByTag("a")
                if (userIdNode != null) {
                    val idUrlString = userIdNode.attr("href")
                    member.username = idUrlString.replace("/member/", "")
                }

                //头像
                val avatarNode = tdNode.getElementsByTag("img")
                if (avatarNode != null) {
                    var avatarString = avatarNode.attr("src")
                    if (avatarString.startsWith("//")) {
                        avatarString = "https:$avatarString"
                    }
                    member.avatar = avatarString
                }
            } else if (content.contains("class=\"item_title\"")) {  //节点信息

                val aNodes = tdNode.getElementsByTag("a")
                for (aNode in aNodes) {
                    if (aNode.attr("class") == "node") {
                        val nodeUrlString = aNode.attr("href")
                        node.name = nodeUrlString.replace("/go/", "")
                        node.title = aNode.text()
                    } else {
                        if (aNode.toString().contains("reply")) {

                            topic.title = aNode.text()
                            val topicIdString = aNode.attr("href")
                            val subArray =
                                topicIdString.split("#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                            topic.id = Integer.parseInt(subArray[0].replace("/t/", ""))
                            topic.replies = Integer.parseInt(subArray[1].replace("reply", ""))
                        }
                    }
                }

                val spanNodes = tdNode.getElementsByTag("span")
                for (spanNode in spanNodes) {
                    val contentString = spanNode.text()
                    if (contentString.contains("最后回复")
                        || contentString.contains("前")
                        || contentString.contains(" • ")
                    ) {
                        val components =
                            contentString.split(" • ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        if (parseNode && components.size <= 2) {
                            continue
                        } else if (!parseNode && components.size <= 1) {
                            continue
                        }

                        val dateString = if (parseNode) components[2] else components[1]
                        if (!TextUtils.isEmpty(dateString)) {
                            topic.created = dateString
                        } else {
                            topic.created = "刚刚"
                        }
                    } else {
                        topic.created = "刚刚"
                    }
                }
            }
        }
        topic.member = member
        topic.node = node
        return topic
    }
}