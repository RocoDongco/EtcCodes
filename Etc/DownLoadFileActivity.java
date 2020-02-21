import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;


/**
 * Created by dev on 2018. 3. 19..
 * 다운로드 액티비티 최초 한번 실행 됨 업데이트 마다 페이지가 나오게 제작될 수도 있음
 */

public class DownLoadFileActivity extends AppCompatActivity {

    static final int PERMISSION_REQUEST_CODE = 1;
    String[] PERMISSIONS = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;

    
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    
    private String PREF_NAME = "이름들";
    private int PRIVATE_MODE = 0;

    @SuppressLint("WrongConstant")
    private boolean hasPermissions(String[] permissions) {
        int res = 0;
        //스트링 배열에 있는 퍼미션들의 허가 상태 여부 확인
        for (String perms : permissions) {
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)) {
                //퍼미션 허가 안된 경우
                return false;
            }

        }
        //퍼미션이 허가된 경우
        return true;
    }

    private void requestNecessaryPermissions(String[] permissions) {
        //마시멜로( API 23 )이상에서 런타임 퍼미션(Runtime Permission) 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load_file);

        pref = getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = null;
        if (powerManager != null) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    "DownloadLock");
        }
        if (wakeLock != null) {
            wakeLock.acquire(10 * 60 * 1000L /*10분 정도 시간만 cpu 점유*/);
        }

        //화면 꺼짐 방지[cpu 점유 하기 위한 내용인대 백그라운드에서도 작업 가능하게 만들면 되는 문제]
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (!hasPermissions(PERMISSIONS)) { //퍼미션 허가를 했었는지 여부를 확인
            requestNecessaryPermissions(PERMISSIONS);//퍼미션 허가안되어 있다면 사용자에게 요청
        } else {
            downloadAndUnzipContent();
        }

    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {
        switch (permsRequestCode) {

            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean readAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if (!readAccepted || !writeAccepted) {
                            showDialogPermission("앱 리소스를 다운로드 하기 위해서는 퍼미션을 허용해주셔야 합니다");
                            return;
                        } else {
                            downloadAndUnzipContent();
                        }
                    }
                }
                break;
        }
    }

    private void downloadAndUnzipContent() {
        //sd카드 다운로드 경로 : Environment.getExternalStorageDirectory().getPath()
        //??? Environment.getExternalStorageDirectory().getAbsolutePath()
        ///data/data/com.androidhuman.app/files/
        ///data/user/0/앱주소/files/content.zip
        //getFilesDir().getAbsolutePath().replace("files", "");
        String url = getResources().getString(R.string.app_download_url);
        DownloadFileAsync download = new DownloadFileAsync(this.getFilesDir().getAbsolutePath() + "/content.zip", this, new DownloadFileAsync.PostDownload() {
            @Override
            public void downloadDone(File file) {
                Log.d("test", "file download completed");

                Decompress unzip = new Decompress(DownLoadFileActivity.this, file);
                unzip.unzip();
                Log.d("test", "file unzip completed");

                editor.putBoolean("downLoadData", true);
                editor.commit();
                Log.d("test", "file download update completed");

                wakeLock.release();

                Intent intent = new Intent(DownLoadFileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
        download.execute(url);
    }

    private void showDialogPermission(String msg) {

        final AlertDialog.Builder rePermissionDialog = new AlertDialog.Builder(DownLoadFileActivity.this);
        rePermissionDialog.setTitle("알림");
        rePermissionDialog.setMessage(msg);
        rePermissionDialog.setCancelable(false);
        rePermissionDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(PERMISSIONS, PERMISSION_REQUEST_CODE);
                }

            }
        });
        rePermissionDialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        rePermissionDialog.show();
    }


}
