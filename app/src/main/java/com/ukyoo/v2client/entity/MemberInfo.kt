package com.ukyoo.v2client.entity

class MemberInfo {


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

    var username: String? = null
    var website: String? = null
    var github: String? = null
    var psn: String? = null
    var avatar_normal: String? = null
    var bio: String? = null
    var url: String? = null
    var tagline: String? = null
    var twitter: String? = null
    var created: Int = 0
    var status: String? = null
    var avatar_large: String? = null
    var avatar_mini: String? = null
    var location: String? = null
    var btc: String? = null
    var id: Int = 0

    fun transfer() {
        if (avatar_large!!.startsWith("//")) {
            avatar_large = "http:" + avatar_large!!
        }
    }
}
