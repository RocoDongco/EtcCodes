import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.opengl.GLES10;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.solver.widgets.Helper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;


/**
 * Created by KIMTAEHO on 2018-07-16.
 * 공용적으로 사용 되는 주소, 메소드, 관련 변수 값 모음
 */

public class DataHouse {

    
    /**
     * ArrayList 의 Null Pointer 체크이다.
     *
     * @param arrList 체크하고자하는 ArrayList
     * @return null 일경우 true, 아닐 경우 false
     * @author DevSCV
     */
    public static Boolean ContainsAllNulls(ArrayList arrList) {
        if (arrList != null) {
            for (Object a : arrList)
                if (a != null) return false;
        }

        return true;
    }


    /**
     * String 의 Null Pointer 체크이다.
     *
     * @param str 체크하고자하는 문자열
     * @return null 일경우 ""리턴, 아닐경우 문자열 리턴
     * @author DevSCV
     */
    public static String nullCheck(String str) {
        if (str == null || str.trim().equals("")) {
            return "";
        } else {
            return str;
        }
    }


    /**
     * String 의 Null Pointer 체크이다.
     *
     * @param str 체크하고자하는 문자열
     * @return null 일경우 true, 아닐 경우 false
     */
    public static Boolean checkTokenNull(String str) {
        return str == null || str.trim().equals("");
    }


  
    /**
     * convertDateFormat 현재 날짜 받아오기
     *
     * @param date      날자
     * @param curFormat 상태값
     * @param desFormat 정렬
     * @return 현재 날짜
     * @author DevSCV
     */
    public static String convertDateFormat(String date, String curFormat, String desFormat) {
        String newFormat = null;
        Date frmtDate = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(curFormat);

            frmtDate = sdf.parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat(desFormat);
            newFormat = formatter.format(frmtDate);

        } catch (Exception e) {
            newFormat = date;
        }
        return newFormat;
    }

    /**
     * 디바이스의 텍스처 최대 크기
     *
     * @return maxSize 디바이스 텍스처 최대 크기값
     * @author DevSCV
     */
    public static int getGLESTextureLimitEqualAboveLollipop() {
        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay dpy = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        int[] vers = new int[2];
        egl.eglInitialize(dpy, vers);
        int[] configAttr = {
                EGL10.EGL_COLOR_BUFFER_TYPE, EGL10.EGL_RGB_BUFFER,
                EGL10.EGL_LEVEL, 0,
                EGL10.EGL_SURFACE_TYPE, EGL10.EGL_PBUFFER_BIT,
                EGL10.EGL_NONE
        };
        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfig = new int[1];
        egl.eglChooseConfig(dpy, configAttr, configs, 1, numConfig);
        if (numConfig[0] == 0) {
        }
        EGLConfig config = configs[0];
        int[] surfAttr = {
                EGL10.EGL_WIDTH, 64,
                EGL10.EGL_HEIGHT, 64,
                EGL10.EGL_NONE
        };
        EGLSurface surf = egl.eglCreatePbufferSurface(dpy, config, surfAttr);
        final int EGL_CONTEXT_CLIENT_VERSION = 0x3098;
        int[] ctxAttrib = {
                EGL_CONTEXT_CLIENT_VERSION, 1,
                EGL10.EGL_NONE
        };
        EGLContext ctx = egl.eglCreateContext(dpy, config, EGL10.EGL_NO_CONTEXT, ctxAttrib);
        egl.eglMakeCurrent(dpy, surf, surf, ctx);
        int[] maxSize = new int[1];
        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize, 0);
        egl.eglMakeCurrent(dpy, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE,
                EGL10.EGL_NO_CONTEXT);
        egl.eglDestroySurface(dpy, surf);
        egl.eglDestroyContext(dpy, ctx);
        egl.eglTerminate(dpy);

        return maxSize[0];
    }

    public static String getStringReplace(String textInput) {

        String restunString = "";

        if (textInput.length() > 0) {

            restunString = textInput.replace(",", "");
        }

        return restunString;
    }

   
    /**
     * dp -> pixel 단위로 변경
     *
     * @return pixel size
     * @author DevSCV
     */
    public static int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension
                (TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

  
}
