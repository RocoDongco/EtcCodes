import android.os.SystemClock;
import android.view.View;

/**
 * Created by devScv
 * 중복 클릭 이벤트 막는 리스너
 */

public abstract class OnSingleClickEventListener implements View.OnClickListener {

    // 중복 클릭 방지 시간 설정
    private final long MIN_CLICK_INTERVAL = 1000;

    private long mLastClickTime;

    public abstract void onSingleClick(View v);

    @Override
    public void onClick(View v) {


        //클릭 이후 1초 전이면 이벤트 리턴
        if (SystemClock.elapsedRealtime() - mLastClickTime < MIN_CLICK_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();


        onSingleClick(v);
    }
}
