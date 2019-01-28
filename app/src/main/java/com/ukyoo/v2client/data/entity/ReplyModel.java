package com.ukyoo.v2client.data.entity;

import android.os.Parcel;
import android.os.Parcelable;
import com.ukyoo.v2client.util.ContentUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class ReplyModel extends V2EXModel implements Parcelable {
    private static final long serialVersionUID = 2015050104L;

    public int id;
    public int thanks;
    public String content;
    public String contentRendered;
    public MemberModel member;
    public String created;
    public long lastModified;

    public void parse(JSONObject jsonObject) throws JSONException {
        id = jsonObject.getInt("id");
        thanks = jsonObject.getInt("thanks");
        content = jsonObject.getString("content");
        contentRendered = ContentUtils.formatContent(jsonObject.getString("content_rendered"));
        member = new MemberModel();
        member.parse(jsonObject.getJSONObject("member"));
        created = jsonObject.getString("created");
        lastModified = jsonObject.getLong("last_modified");
    }

    public ReplyModel(){}

    private ReplyModel(Parcel in){
        int[] ints= new int[2];
        in.readIntArray(ints);
        id = ints[0];
        thanks = ints[1];
        String[] strings = new String[2];
        in.readStringArray(strings);
        content = strings[0];
        contentRendered = strings[1];
        created = strings[2];
        long[] longs = new long[1];
        in.readLongArray(longs);
        lastModified = longs[1];
        member = (MemberModel) in.readValue(MemberModel.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(new int[]{
                id,
                thanks
        });
        dest.writeStringArray(new String[]{
                content,
                contentRendered,
                created
        });
        dest.writeLongArray(new long[]{
                lastModified,
        });
        dest.writeValue(member);
    }

    public static final Creator<ReplyModel> CREATOR = new Creator<ReplyModel>() {
        @Override
        public ReplyModel createFromParcel(Parcel source) {
            return new ReplyModel(source);
        }

        @Override
        public ReplyModel[] newArray(int size) {
            return new ReplyModel[size];
        }
    };

}
