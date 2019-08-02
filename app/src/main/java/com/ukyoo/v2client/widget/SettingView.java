package com.ukyoo.v2client.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.ukyoo.v2client.R;
import com.ukyoo.v2client.util.SizeUtils;


/**
 * 个人中心的设置条目
 * <p>
 * Created by hewei26 on 2017/5/8.
 */

public class SettingView extends RelativeLayout {

    private ImageView mIvLeft;
    private TextView mTvCenter;
    private ImageView mIvRight;

    public SettingView(Context context) {
        super(context);
    }

    public SettingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public SettingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingViews);

        String str = mTypedArray.getString(R.styleable.SettingViews_tv_desc);
        int iv_left = mTypedArray.getResourceId(R.styleable.SettingViews_img_left, R.drawable.user);
        int iv_right = mTypedArray.getResourceId(R.styleable.SettingViews_img_right, R.drawable.more);
        boolean rightVisible = mTypedArray.getBoolean(R.styleable.SettingViews_right_visible, true);
        boolean leftVisible = mTypedArray.getBoolean(R.styleable.SettingViews_left_visible, true);
        int textColor = mTypedArray.getColor(R.styleable.SettingViews_text_color1, ContextCompat.getColor(context, R.color.black_87));
        float textSize = mTypedArray.getDimension(R.styleable.SettingViews_text_size1, SizeUtils.sp2px(context, 15));

        initView(context);

        mTvCenter.setText(str.trim());
        mTvCenter.setTextColor(textColor);
        mTvCenter.setTextSize(SizeUtils.px2sp(context, textSize));
        mIvLeft.setImageResource(iv_left);

        if (leftVisible) {
            mIvLeft.setImageResource(iv_left);
        } else {
            mIvLeft.setVisibility(View.GONE);
        }

        if (rightVisible) {
            mIvRight.setImageResource(iv_right);
        } else {
            mIvRight.setVisibility(View.GONE);
        }

        mTypedArray.recycle();
    }

    public void setText(String str) {
        mTvCenter.setText(str);
    }


    private void initView(Context context) {
//        View.inflate(context, R.layout.view_setting_view, null);

        View view = LayoutInflater.from(context).inflate(R.layout.view_setting_view, this);
        mIvLeft = view.findViewById(R.id.iv_setting_left);
        mTvCenter = view.findViewById(R.id.tv_setting_center);
        mIvRight = view.findViewById(R.id.iv_setting_right);
    }
}
