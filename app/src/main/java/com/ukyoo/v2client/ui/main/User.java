package com.ukyoo.v2client.ui.main;

import com.ukyoo.v2client.api.ApiService;
import com.ukyoo.v2client.entity.TopicEntity;
import io.reactivex.functions.Function;

import java.util.List;

public class User {

    public String name;
    public String age;
    public String phoneNum;

    public User(String name, String age, String phoneNum) {
        this.name = name;
        this.age = age;
        this.phoneNum = phoneNum;
    }
}
