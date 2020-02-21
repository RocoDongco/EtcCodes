import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;



/**
 * Created by devScv
 * 다이얼로그 호출 시 최근 적용된 매뉴바 숨김 또는 표기 시 위치가 맞지 않는 경우에 사용되는 소스
 */

public class PushNoticeEndDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private boolean active;
    private int index;

    private int height;

    public PushNoticeEndDialog(@NonNull Context context, boolean isActive, int noticeIndex) {
        super(context);
        mContext = context;
        active = isActive;
        index = noticeIndex;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_notice_agree);

        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setCancelable(true);
        Objects.requireNonNull(getWindow()).clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        if (isNavigationBar()) {
            height = hasNavBar();
        } else {
            height = metrics.heightPixels;
        }

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = width;
        params.height = height;
        getWindow().setAttributes(params);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        TextView notice_agree_title = findViewById(R.id.notice_agree_title);

        TextView notice_agree = findViewById(R.id.notice_agree);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");

        Date time = new Date();

        String text = simpleDateFormat.format(time);
        notice_agree_title.setText("최상단 제목 텍스트");


        if (active) {

            switch (index) {

                case 0:
                    notice_agree.setText(R.string.text_notice_email_active);
                    break;
                case 1:
                    notice_agree.setText(R.string.text_notice_sms_active);
                    break;
                case 2:
                    notice_agree.setText(R.string.text_notice_push_active);
                    FirebaseMessaging.getInstance().subscribeToTopic("event")
                            .addOnCompleteListener(task -> {
                                Log.d("test", "subscribeToTopic");
                            });
                    break;
            }


        } else {
            switch (index) {

                case 0:
                    notice_agree.setText(R.string.text_notice_email_deactivate);
                    break;
                case 1:
                    notice_agree.setText(R.string.text_notice_sms_deactivate);
                    break;
                case 2:
                    notice_agree.setText(R.string.text_notice_push_deactivate);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("event")
                            .addOnCompleteListener(task -> {
                                Log.d("test", "unsubscribeFromTopic");
                            });
                    break;

            }
        }


        RelativeLayout dialog_layout = findViewById(R.id.dialog_layout);
        dialog_layout.setOnClickListener(v -> dismiss());

        timerDelayRemoveDialog(1500, this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    //타이머 설정
    private void timerDelayRemoveDialog(long time, final Dialog d) {
        new Handler().postDelayed(d::dismiss, time);
    }


    private boolean isNavigationBar() {
        int id = mContext.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && mContext.getResources().getBoolean(id);
    }

    private int hasNavBar() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //실제 화면에서 표현 되는 것
        int height = metrics.heightPixels;

        Display d = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);
        //전체 화면
        int realHeight = realDisplayMetrics.heightPixels;

        if (height != realHeight) {
            //navigation_bar_height 높이를 제외한
            Resources resources = mContext.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            int navbarSize = resources.getDimensionPixelSize(resourceId);
            return height - navbarSize;
        } else {
            return realHeight;
        }
    }

}
