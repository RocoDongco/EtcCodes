import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dev on 2018. 3. 19..
 * 리소스 다운로드 및 압축 해제 -> 압축파일 삭제
 */

public class DownloadFileAsync extends AsyncTask<String, String, String> {

    private static final String TAG = "DOWNLOADFILE";

    private PostDownload callback;
    @SuppressLint("StaticFieldLeak")
    public Context context;
    private FileDescriptor fd;
    private File file;
    private String downloadLocation;
    private HttpURLConnection connection;

    private int TIMEOUT_VALUE = 1000;   // 타임 연결 타임 아웃 시간1초

    private ProgressDialog downloadProgress; //프로그래스바

    DownloadFileAsync(String downloadLocation, Context context, PostDownload callback) {
        this.context = context;
        this.callback = callback;
        this.downloadLocation = downloadLocation;
        this.downloadProgress = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        downloadProgress.setMessage("파일 리소스를 다운로드 및 설치 중입니다");
        downloadProgress.setIndeterminate(false);
        downloadProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        downloadProgress.setCancelable(false);
        downloadProgress.show();
    }

    @Override
    protected String doInBackground(String... aurl) {
        int count;

        try {
            URL url = new URL(aurl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(TIMEOUT_VALUE);
            connection.setReadTimeout(TIMEOUT_VALUE);
            connection.connect();

            int lengthOfFile = connection.getContentLength();
            Log.d(TAG, "Length of the file: " + lengthOfFile);

            InputStream input = new BufferedInputStream(url.openStream());
            file = new File(downloadLocation);
            FileOutputStream output = new FileOutputStream(file);
            //context.openFileOutput("content.zip", Context.MODE_PRIVATE);
            Log.d(TAG, "file saved at " + file.getAbsolutePath());
            fd = output.getFD();

            byte data[] = new byte[1024];
            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                publishProgress("" + (int) ((total * 100) / lengthOfFile));
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }


        return null;

    }

    protected void onProgressUpdate(String... progress) {
        super.onProgressUpdate(progress);
        //Log.d(TAG,progress[0]);
    }

    @Override
    protected void onPostExecute(String unused) {
        if (callback != null) {
            callback.downloadDone(file);
            downloadProgress.dismiss();
            connection.disconnect();
        }


    }

    public interface PostDownload {
        void downloadDone(File fd);
    }

}
