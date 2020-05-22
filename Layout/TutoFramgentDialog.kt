
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

/*
    스타일

<style name="FullScreenDialogStyle" parent="Theme.AppCompat.Dialog">
        <item name="android:windowNoTitle">true</item>
        <item name="android:windowFullscreen">true</item>
        <item name="android:windowIsFloating">false</item>
        <item name="android:windowBackground">@null</item>
</style>

호출 방식
TutoFramgentDialog(2).show(this.supportFragmentManager, "화면1")
*/


//단순 화면 전용 튜토리얼 페이지에서 사용 
class TutoFramgentDialog : DialogFragment{

    var viewType : Int = 0

    constructor(){

    }

    constructor(viewType : Int) {
        this.viewType = viewType
    }

    val resLayout  = intArrayOf(R.layout.cell_0, R.layout.cell_1,
            R.layout.cell_2, R.layout.cell_3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
        //각페이지별로 열린 상태값 확인 후 다시는 열리지 않기 위해서 추가
        SharedPrefManager.getInstance(context).setValue(SharedPrefManager + viewType, true) 
    }

    //화면 클릭시 화면 닫기
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(resLayout[viewType], container, false)
        view.setOnTouchListener{ _: View, event:MotionEvent ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dismiss()
                }
            }
            true
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        //풀화면 세팅
        if(dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog!!.window?.setLayout(width, height)
        }
    }

    override fun dismiss() {
        super.dismiss()
    }

  
}