package com.ukyoo.v2client.data.entity;

import android.text.TextUtils;
import android.util.Log;
import com.ukyoo.v2client.util.ContentUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * 话题列表类,网页解析得到
 * Created by yw on 2015/5/19.
 */
public class TopicListModel extends ArrayList<TopicModel> {

    private static final long serialVersionUID = 2015050107L;

    public int mCurrentPage = 1;
    public int mTotalPage = 1;

    public ArrayList<TopicModel> parse(String responseBody) throws Exception {
        Document doc = Jsoup.parse(responseBody);
        Element body = doc.body();
        Elements elements = body.getElementsByAttributeValue("class", "cell item");
        for (Element el : elements) {
            try {
                TopicModel model = parseTopicModel(el, true, null);
                add(model);
            } catch (Exception e) {
            }
        }

        int[] pages = ContentUtils.parsePage(body);
        mCurrentPage = pages[0];
        mTotalPage = pages[1];
        return this;
    }

    public ArrayList<TopicModel> parseFromNodeEntry(String responseBody, String nodeName) throws Exception {
        Document doc = Jsoup.parse(responseBody);
        String title = doc.title();
        title = title.replace("V2EX ›", "").trim();
        title = title.split(" ")[0];
        NodeModel node = new NodeModel();
        node.name = nodeName;
        node.title = title;

        Element body = doc.body();
        //Elements elements = body.getElementsByAttributeValue("id", "TopicsNode");
        //if(elements.size() != 1) return;
        Elements elements = body.getElementsByAttributeValueMatching("class", Pattern.compile("cell from_(.*)"));
        for (Element el : elements) {
            try {
                TopicModel topic = parseTopicModel(el, false, node);
                add(topic);
            } catch (Exception e) {
                Log.e("err", e.toString());
            }
        }

        int[] pages = ContentUtils.parsePage(body);
        mCurrentPage = pages[0];
        mTotalPage = pages[1];
        return this;
    }

    private TopicModel parseTopicModel(Element el, boolean parseNode, NodeModel node) throws Exception {
        Elements tdNodes = el.getElementsByTag("td");
        TopicModel topic = new TopicModel();
        MemberModel member = new MemberModel();
        if (parseNode) node = new NodeModel();
        for (Element tdNode : tdNodes) {
            String content = tdNode.toString();
            if (content.contains("class=\"avatar\"") ) { //用户信息
                Elements userIdNode = tdNode.getElementsByTag("a");
                if (userIdNode != null) {
                    String idUrlString = userIdNode.attr("href");
                    member.username = idUrlString.replace("/member/", "");
                }

                Elements avatarNode = tdNode.getElementsByTag("img");
                if (avatarNode != null) {
                    String avatarString = avatarNode.attr("src");
                    if (avatarString.startsWith("//")) {
                        avatarString = "http:" + avatarString;
                    }
                    member.avatar = avatarString;
                }
            } else if (content.contains("class=\"item_title\"") ) { //主题条目
                Elements aNodes = tdNode.getElementsByTag("a");
                for (Element aNode : aNodes) {
                    if (parseNode && aNode.attr("class").equals("node")) {
                        String nodeUrlString = aNode.attr("href");
                        node.name = nodeUrlString.replace("/go/", "");
                        node.title = aNode.text();
                    } else {
                        if (aNode.toString().contains("reply") ) {
                            topic.title = aNode.text();
                            String topicIdString = aNode.attr("href");
                            String[] subArray = topicIdString.split("#");
                            topic.id = Integer.parseInt(subArray[0].replace("/t/", ""));
                            topic.replies = Integer.parseInt(subArray[1].replace("reply", ""));
                        }
                    }
                }

                Elements spanNodes = tdNode.getElementsByTag("span");
                for (Element spanNode : spanNodes) {
                    String contentString = spanNode.text();
                    if (contentString.contains("最后回复")
                            || contentString.contains("前")
                            || contentString.contains(" • ") ) {
                        String[] components = contentString.split(" • ");
                        if (parseNode && components.length <= 2) continue;
                        else if (!parseNode && components.length <= 1) continue;
                        String dateString = parseNode ? components[2] : components[1];
                        if (!TextUtils.isEmpty(dateString)) {
                            topic.created = dateString;
                        } else {
                            topic.created = "刚刚";
                        }
                    } else {
                        topic.created = "刚刚";
                    }
                }
            }
        }

        topic.member = member;
        topic.node = node;

        return topic;
    }
}
