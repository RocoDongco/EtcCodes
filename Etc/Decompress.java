package kr.co.sbooks.phoca_project_off_line.common;

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by dev on 2018. 3. 19..
 * 압축 파일 풀기 위해서 사용됨
 *
 */

public class Decompress {

    private File _zipFile;
    private InputStream _zipFileStream;
    private Context context;
    //원본 저장 구문 [용량 차이 때문에 원본 저장 기능은 사용하지 않음]
    // [Environment.getExternalStorageDirectory().getPath()]
    //this.getFilesDir().getAbsolutePath()
    //private static final String ROOT_LOCATION = Context.getFilesDir().getAbsolutePath();
    private static final String TAG = "UNZIPUTIL";

    Decompress(Context context, File zipFile) {
        _zipFile = zipFile;
        this.context = context;

        _dirChecker("");
    }

    public Decompress(Context context, InputStream zipFile) {
        _zipFileStream = zipFile;
        this.context = context;

        _dirChecker("");
    }

    public void unzip() {
        try  {
            Log.i(TAG, "Starting to unzip");
            InputStream fin = _zipFileStream;
            if(fin == null) {
                fin = new FileInputStream(_zipFile);
            }
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                Log.v(TAG, "Unzipping " + ze.getName());

                if(ze.isDirectory()) {
                    _dirChecker(context.getFilesDir().getAbsolutePath() + "/" + ze.getName());
                } else {
                    FileOutputStream fout = new FileOutputStream(new File(context.getFilesDir().getAbsolutePath(), ze.getName()));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int count;

                    // reading and writing
                    while((count = zin.read(buffer)) != -1)
                    {
                        baos.write(buffer, 0, count);
                        byte[] bytes = baos.toByteArray();
                        fout.write(bytes);
                        baos.reset();
                    }

                    fout.close();
                    zin.closeEntry();
                }

            }
            zin.close();
            Log.i(TAG, "Finished unzip");

            boolean deleted = _zipFile.delete();
            Log.i(TAG, "delete unzip");

        } catch(Exception e) {
            Log.e(TAG, "Unzip Error", e);
        }

    }

    private void _dirChecker(String dir) {
        File f = new File(dir);
        Log.i(TAG, "creating dir " + dir);

        if(dir.length() >= 0 && !f.isDirectory() ) {
            f.mkdirs();
        }
    }

}
