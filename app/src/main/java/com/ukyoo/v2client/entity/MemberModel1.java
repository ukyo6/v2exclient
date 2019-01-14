package com.ukyoo.v2client.entity;

public class MemberModel1 {


    /**
     * username : askfermi
     * website :
     * github :
     * psn :
     * avatar_normal : //cdn.v2ex.com/gravatar/44dd63808cafcfb86f7acc7e17c31383?s=24&d=retro
     * bio :
     * url : https://www.v2ex.com/u/askfermi
     * tagline :
     * twitter :
     * created : 1405954433
     * status : found
     * avatar_large : //cdn.v2ex.com/gravatar/44dd63808cafcfb86f7acc7e17c31383?s=24&d=retro
     * avatar_mini : //cdn.v2ex.com/gravatar/44dd63808cafcfb86f7acc7e17c31383?s=24&d=retro
     * location :
     * btc :
     * id : 68592
     */

    private String username;
    private String website;
    private String github;
    private String psn;
    private String avatar_normal;
    private String bio;
    private String url;
    private String tagline;
    private String twitter;
    private int created;
    private String status;
    private String avatar_large;
    private String avatar_mini;
    private String location;
    private String btc;
    private int id;

    public void transfer(){
        if (avatar_large.startsWith("//")) {
            avatar_large = "http:" + avatar_large;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getPsn() {
        return psn;
    }

    public void setPsn(String psn) {
        this.psn = psn;
    }

    public String getAvatar_normal() {
        return avatar_normal;
    }

    public void setAvatar_normal(String avatar_normal) {
        this.avatar_normal = avatar_normal;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAvatar_large() {
        return avatar_large;
    }

    public void setAvatar_large(String avatar_large) {
        this.avatar_large = avatar_large;
    }

    public String getAvatar_mini() {
        return avatar_mini;
    }

    public void setAvatar_mini(String avatar_mini) {
        this.avatar_mini = avatar_mini;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBtc() {
        return btc;
    }

    public void setBtc(String btc) {
        this.btc = btc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
