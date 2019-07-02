package com.ukyoo.v2client.data.entity

import com.ukyoo.v2client.util.ContentUtils
import org.jsoup.Jsoup

/**
 * 用户的回复
 */
class ReplyListModel {

    var replyItems: ArrayList<ReplyItemModel> = ArrayList()

    class ReplyItemModel {
        var replyTo: String = ""
        var replyTime: String = ""
        var topicTitle: String = ""
        var topicId: Int = 0

        var nodeName: String = ""
        var nodeId: String = ""
        var replies: Int = 0
        var replyContent = ""
    }

    fun parse(responseBody: String): ArrayList<ReplyItemModel>{
        val doc = Jsoup.parse(responseBody)
        val body = doc.body()

        val titleElements = body.getElementsByAttributeValue("class", "dock_area")

        val contentElements = body.getElementsByAttributeValue("class", "reply_content")

        for (element in titleElements) {

            val item = ReplyItemModel()

            val tdNodes = element.getElementsByTag("td")
            for (tdNode in tdNodes) {
                val spanNodes = tdNode.getElementsByTag("span")
                for (spanNode in spanNodes) {
                    if (spanNode.attr("class").equals("fade")) {
                        item.replyTime = spanNode.text() //回复时间
                    } else if (spanNode.attr("class").equals("gray")) {
                        val aNodes = spanNode.getElementsByTag("a")
                        for (aNode in aNodes) {
                            val href = aNode.attr("href")
                            when {
                                href.contains("/member") -> item.replyTo = aNode.text()
                                href.contains("/go") -> {
                                    item.nodeName = aNode.text()
                                    item.nodeId = href.replace("/go/", "")
                                }
                                href.contains("reply") -> {
                                    item.topicTitle = aNode.text()

                                    val topicIdString = aNode.attr("href")
                                    val subArray = topicIdString.split("#".toRegex())

                                    item.topicId = Integer.parseInt(subArray[0].replace("/t/", ""))
                                    item.replies = Integer.parseInt(subArray[1].replace("reply", ""))
                                }
                            }
                        }
                    }
                }
            }
            val replyElement = contentElements[titleElements.indexOf(element)]
            item.replyContent = ContentUtils.formatContent(replyElement.html())

            replyItems.add(item)
        }

        return replyItems
    }
}