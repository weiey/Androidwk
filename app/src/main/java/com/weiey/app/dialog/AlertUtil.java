package com.weiey.app.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import com.weiey.app.R;
import com.weiey.app.adapter.CommonAdapter;
import com.weiey.app.adapter.ViewHolder;
import com.weiey.app.bean.SortToken;
import com.weiey.app.utils.Callback;

import java.util.*;

public class AlertUtil {
    static int checked = 0;//皓元S2684120010808657
//    ACHG3020022726657
    static Map<Integer,Integer> map = new LinkedHashMap<>();
    public static  void  showSingleChoice(Context mContext, String[] item,int checkedItem, final Callback<Integer> callback){



        List<String> data = new ArrayList<String>(Arrays.asList(item));
        List<SortToken> allData = new ArrayList<>();
        int pos = 0;
        for (int i = 0; i < data.size(); i++) {
            map.put(pos++,i);
            allData.add(new SortToken(i,data.get(i)));
        }
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_common_list,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(v);
        AlertDialog dialog = builder.create();

        ListView lv = v.findViewById(R.id.lv_list);
        EditText et = v.findViewById(R.id.et_key);
        checked = 0;
        CommonAdapter<String> commonAdapter = new CommonAdapter<String>(mContext, R.layout.layout_type_tv) {

            @Override
            public void convert(ViewHolder holder, String s) {
                ImageView iv_check = holder.getView(R.id.iv_check);
                TextView tv = holder.getView(R.id.tv_value);
                tv.setText(s);
                iv_check.setSelected(holder.getPosition() == checked);
            }
        };
        lv.setAdapter(commonAdapter);
        commonAdapter.setData(data);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checked = position;
//                commonAdapter.notifyDataSetChanged();
                callback.callback(map.get(position));
                dialog.dismiss();
            }
        });
        et.setImeOptions(EditorInfo.IME_ACTION_DONE);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable sss) {
                String str = et.getText().toString();
                List<String> list = new ArrayList<>();
                map.clear();
                int pos = 0;
                str = str.toUpperCase();
                for (int i = 0; i < allData.size(); i++) {
                    SortToken item = allData.get(i);
                    if(item.getTag().contains("请选择") ||item.getTag().contains(str) ||item.getSimpleSpell().contains(str) || item.getWholeSpell().contains(str)){
                        list.add(item.getTag());
                        map.put(pos++,i);
                        if( checked < i){
                            checked = 0;
                        }
                    }
                }
                commonAdapter.setData(list);
            }
        });


        Window window =  dialog.getWindow();
        if (window != null && mContext instanceof Activity) {
//            window.getDecorView().setPadding(0, 0, 0, 0);

//从Android 4.1开始向上兼容，对下不兼容
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                window.getDecorView().setBackground(mContext.getResources().getDrawable(R.drawable.dialog_top_bg));
            }

            WindowManager manager = ((Activity)mContext).getWindowManager();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(displayMetrics);
            int h =  displayMetrics.heightPixels * 4/5;
            int w = displayMetrics.widthPixels * 4/5;
            WindowManager.LayoutParams lp = dialog.getWindow()
                    .getAttributes();
            lp.gravity = Gravity.CENTER;
            lp.width = w;
            lp.height = h;
            window.setAttributes(lp);
        }
        dialog.show();
    }

//    public static int checked = 0;
//    public static void showSingleChoice(Context mContext, String[] item,int checkedItem, final Callback<Integer> callback){
//      AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//
//        builder.setCancelable(true);
//        builder.setSingleChoiceItems(item, checkedItem, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        callback.callback(which);
//                        dialog.dismiss();
//                    }
//                })
//                .create().show();
//    }
    public static AlertDialog showAlert(Context mContext, String title, String msg, final DialogInterface.OnClickListener negativeListener){
//        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(mContext, R.style.Theme_MaterialComponents_Light_BottomSheetDialog);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title)
                .setMessage(msg)
                .setNegativeButton("确定", negativeListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;

    }
    public static AlertDialog showAlert(Context mContext, String title, String msg, final DialogInterface.OnClickListener negativeListener, final DialogInterface.OnClickListener positiveListener){
//        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(mContext, R.style.Theme_MaterialComponents_Light_BottomSheetDialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);


        builder.setTitle(title)
                .setMessage(msg)
                .setNegativeButton("确定", negativeListener)
                .setPositiveButton("取消", positiveListener);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }
    public static AlertDialog showAlert(Context mContext, String title, String msg, String[] btns, final DialogInterface.OnClickListener neutralListener, final DialogInterface.OnClickListener negativeListener, final DialogInterface.OnClickListener positiveListener){
//        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(mContext, R.style.Theme_MaterialComponents_Light_BottomSheetDialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        if(btns == null){
            btns = new String[]{"是","否","取消"};
        }

        builder.setTitle(title)
                .setMessage(msg)
                .setNeutralButton(btns[0],neutralListener)
                .setNegativeButton(btns[1], negativeListener)

                .setPositiveButton(btns[2], positiveListener);


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

    public static AlertDialog showAlertWindow(Activity activity, View view, final DialogInterface.OnClickListener negativeListener, final DialogInterface.OnClickListener positiveListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.base_dialog);
        builder.setView(view)
                .setNegativeButton("确定", negativeListener)
                .setPositiveButton("取消", positiveListener);
        AlertDialog alertDialog = builder.create();
        Window window = alertDialog.getWindow();
        if (window != null) {
            window.getDecorView().setPadding(0, 0, 0, 0);

//从Android 4.1开始向上兼容，对下不兼容
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                window.getDecorView().setBackground(activity.getResources().getDrawable(R.drawable.dialog_top_bg));
            }
            WindowManager manager = activity.getWindowManager();
            Display display = manager.getDefaultDisplay();
            WindowManager.LayoutParams lp = alertDialog.getWindow()
                    .getAttributes();
            lp.gravity = Gravity.CENTER;
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
        }
        alertDialog.show();
        return alertDialog;
    }
}
