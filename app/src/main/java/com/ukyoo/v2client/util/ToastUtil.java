package com.ukyoo.v2client.util;

import android.widget.Toast;
import com.ukyoo.v2client.App;

/**
 * Created by hewei26 on 2017/5/3.
 */

public class ToastUtil {

    private static Toast toast = null;

    public static void longShow(String msg) {
        if(toast == null){
            toast = Toast.makeText(App.Companion.instance(), msg, Toast.LENGTH_LONG);
        }else{
            toast.setText(msg);
        }
        toast.show();
    }

    public static void shortShow(String msg) {
        if(toast == null){
            toast = Toast.makeText(App.Companion.instance(), msg, Toast.LENGTH_SHORT);
        }else{
            toast.setText(msg);
        }
        toast.show();
    }

//    /**
//     * 在view的上层顶部显示类似SnackBar的Toast
//     *
//     * @param view
//     * @param msg
//     */
//    public static void showTop(Activity activity, View view, String msg) {
//
//        Style style = new Style.Builder()
//                .setTextColor(R.color.white)
//                .setBackgroundColor(R.color.half_transcluent)
//                .setConfiguration(new Configuration.Builder().setDuration(1000).build())
//                .build();
//
//        Crouton.makeText(activity, msg, style, (ViewGroup) view).show();
//
//    }
}
