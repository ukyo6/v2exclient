package com.ukyoo.v2client.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.ukyoo.v2client.R;

/**
 * Created by hewei26 on 2017/5/12.
 */

public class CustomDialog extends Dialog {
    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private View contentView;
        private boolean isCancelable = false;
        private boolean isBottomDialog = false;
        private boolean isNotitle;
        private int width;
        private int height;

        public Builder(Context context) {
            this.context = context;
        }

        public int getWidth() {
            return width;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public int getHeight() {
            return height;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public String getTitle() {
            return title;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public String getMessage() {
            return message;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public View getContentView() {
            return contentView;
        }

        public Builder setContentView(View contentView) {
            this.contentView = contentView;
            return this;
        }

        public boolean isCancelable() {
            return isCancelable;
        }

        public Builder setCancelable(boolean cancelable) {
            isCancelable = cancelable;
            return this;
        }

        public boolean isBottomDialog() {
            return isBottomDialog;
        }

        public Builder setBottomDialog(boolean bottomDialog) {
            isBottomDialog = bottomDialog;
            return this;
        }

        public boolean isNotitle() {
            return isNotitle;
        }

        public Builder setNotitle(boolean notitle) {
            isNotitle = notitle;
            return this;
        }

        public CustomDialog create() {
            CustomDialog dialog;
            if(isBottomDialog){
                dialog = new CustomDialog(context, R.style.BottomDialog);
            }else{
                dialog = new CustomDialog(context, R.style.DefaultDialog);
            }
            //点击消失
            dialog.setCanceledOnTouchOutside(isCancelable);
            //是否设置标题
            if (isNotitle) {
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            }
            if(title != null){
                dialog.setTitle(title);
            }
            //内容
            Window win = dialog.getWindow();
            if (isBottomDialog) {
                win.setGravity(Gravity.BOTTOM);
            }
            dialog.setContentView(contentView);
            //宽高
            WindowManager.LayoutParams attrs = win.getAttributes();
            attrs.width = width == 0 ? android.view.ViewGroup.LayoutParams.WRAP_CONTENT : getWidth();
            attrs.height = height == 0 ? android.view.ViewGroup.LayoutParams.WRAP_CONTENT : getHeight();

            return dialog;
        }
    }
}
