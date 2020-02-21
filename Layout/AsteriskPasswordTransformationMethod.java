
import android.text.method.PasswordTransformationMethod;
import android.view.View;


/**
 * Created by devScv on 2019-04-19.
 * 커스텀 폰트가 이벤트 입력기의 텍스트값이 존재하지 않아 공백이 표시됨 따라서 입력값을 문자 *로 변경 시키는 클래스
 * 
 */

public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new PasswordCharSequence(source);
    }

    private class PasswordCharSequence implements CharSequence {
        private CharSequence mSource;
        public PasswordCharSequence(CharSequence source) {
            mSource = source; 
        }
        public char charAt(int index) {
            return '*'; //모든 입력된 텍스트를 *표기로 변경
        }
        public int length() {
            return mSource.length(); // Return default
        }
        public CharSequence subSequence(int start, int end) {
            return mSource.subSequence(start, end); 
        }
    }
}
