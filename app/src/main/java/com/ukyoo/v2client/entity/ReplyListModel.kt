package com.ukyoo.v2client.entity

import org.jsoup.Jsoup
import org.jsoup.select.Elements

/**
 * 用户的回复
 */
class ReplyListModel {

    var replyTo: String = ""
    var replyTime: String = ""
    var topicTitle: String = ""
    var topicId: String = ""

    var nodeName: String = ""
    var nodeId: String = ""

    fun prase(responseBody: String) {
        val doc = Jsoup.parse(responseBody)
        val body = doc.body()

        val titleElements = body.getElementsByAttributeValue("class", "dock_area")

        val contentElements = body.getElementsByAttributeValue("class", "reply_content")

        for (element in titleElements) {
            val tdNodes = element.getElementsByTag("td")
            for (tdNode in tdNodes) {
                tdNode.getElementsByTag("")



            }
        }

        for (element in contentElements) {

        }
    }
}