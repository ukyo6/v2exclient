package com.ukyoo.v2client.repository

import com.ukyoo.v2client.data.api.HtmlService
import com.ukyoo.v2client.data.AppDataBase
import com.ukyoo.v2client.data.entity.ProfileModel
import io.reactivex.Flowable
import org.jsoup.Jsoup
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ProfilerRepository @Inject constructor(@Named("cached") var htmlService: HtmlService) {


    /**
     * 获取用户基本信息
     */
     fun getUserProfiler() : Flowable<ProfileModel> {
        return htmlService.getProfiler()
            .map {
                val profileModel = parseProfilerModel(it)
                //存到db  子线程
                AppDataBase.getDatabase().profileModelDao().saveUserProfile(profileModel)
                return@map profileModel
            }
    }

    /**
     * 解析个人信息
     */
    private fun parseProfilerModel(responseBody: String) : ProfileModel{
        val model = ProfileModel()

        val doc = Jsoup.parse(responseBody)
        val body = doc.body()
        val elements = body.getElementsByAttributeValue("id", "Rightbar")
        if(elements[0].getElementsByAttributeValue("href", "/signin").size >0){  //未登录
            return model
        }

        val found = intArrayOf(0, 0, 0, 0)
        for (el in elements) {
            if (found[0] == 1 && found[1] == 1 && found[2] == 1 && found[3] == 1)
                break
            val tdNodes = el.getElementsByTag("td")
            for (tdNode in tdNodes) {
                val content = tdNode.toString()
                if (found[0] == 0 && content.contains("a href=\"/member/")) {
                    val aNode = tdNode.getElementsByTag("a")
                    model.username = aNode.attr("href").replace("/member/", "")
                    val avatarNode = tdNode.getElementsByTag("img")
                    if (avatarNode != null) {
                        var avatarString = avatarNode.attr("src")
                        if (avatarString.startsWith("//")) {
                            avatarString = "https:$avatarString"
                        }
                        model.avatar = avatarString
                        found[0] = 1
                    }
                } else if (found[1] == 0 && content.contains("a href=\"/my/nodes\"")) {
                    //text = 20 节点收藏
                    var text = tdNode.text()
                    text = text.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                    try {
                        model.nodes = Integer.parseInt(text)
                        found[1] = 1
                    } catch (e: Exception) {
                        model.nodes = 0
                    }

                } else if (found[2] == 0 && content.contains("a href=\"/my/topics\"")) {
                    //text = 20 主题收藏
                    var text = tdNode.text()
                    text = text.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                    try {
                        model.topics = Integer.parseInt(text)
                        found[2] = 1
                    } catch (e: Exception) {
                        model.topics = 0
                    }

                } else if (found[3] == 0 && content.contains("a href=\"/my/following\"")) {
                    //text = 20 特别关注
                    var text = tdNode.text()
                    text = text.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                    try {
                        model.followings = Integer.parseInt(text)
                        found[3] = 1
                    } catch (e: Exception) {
                        model.followings = 0
                    }

                }
            }
        }

        val pattern = Pattern.compile("<a href=\"/notifications\"([^>]*)>([0-9]+) 条未读提醒</a>")
        val matcher = pattern.matcher(responseBody)
        if (matcher.find()) {
            model.notifications = Integer.parseInt(matcher.group(2))
        }

        return model
    }
}