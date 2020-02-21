import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import android.os.Build



/**
 * Created by Dev.SCV
 * Glide에서 이미지 로드 시 OOM을 넘어가는 경우 웹뷰로 돌려서 큰 이미지 로드 시키는 꼼수형 소스
 * 일반적으로 사용하진 않음 이미지 크기를 줄이던 혹은 리사이징 등을 통해 처리 하지 못한 경우에 사용
 * 어지간 하면 리사이징 또는 WebP로 변경 해서 로드 하길 바람
 */

class ContentImageMore : AppCompatActivity() {

    var contentImages: ArrayList<ContentDetailImage>? = null

    var layoutManager: LinearLayoutManager? = null

    var mGlideRequestManager: RequestManager? = null

    var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_image_more)

        toolbar = findViewById(R.id.tool_bar) ?: return

        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        supportActionBar!!.setDisplayShowTitleEnabled(false)

        toolbar_title.text = "이미지 상세 로드"

        mGlideRequestManager = Glide.with(this)

        if (intent.hasExtra("contentImages")) {
            contentImages = intent.getSerializableExtra("contentImages") as ArrayList<ContentDetailImage>

            layoutManager = LinearLayoutManager(applicationContext)
            layoutManager?.orientation = RecyclerView.VERTICAL
            image_more_recycler_view.layoutManager = layoutManager
            image_more_recycler_view.itemAnimator = DefaultItemAnimator()

            val fristTopMargins = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0f,
                    resources.displayMetrics).toInt()

            val fristBottomMargins = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f,
                    resources.displayMetrics).toInt()

            val top = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0f,
                    resources.displayMetrics).toInt()

            val bottom = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f,
                    resources.displayMetrics).toInt()

            val start = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0f,
                    resources.displayMetrics).toInt()

            val end = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0f,
                    resources.displayMetrics).toInt()

                    //꼼수 코드 시작 웹뷰로 4k급 이미지 등을 로드 하는 경우에도 OOM이 일어나지 않음
                    val style = "<style>img{height: auto;max-width: 100%;}</style>"
                    val tag = "<img src=\"${contentImages?.get(0)?.image}\"/>"

                    val textTag =  contentImages?.get(0)?.text?.replace("\n", "<br>")
                    val replaceTag = "<div>"+ textTag+ "</div>"

                    val body = "<body style='margin:0;padding:0; overflow:scroll;text-align: center;'>$tag$replaceTag</body>"

                    val settings = webview.settings
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true

                    settings.javaScriptCanOpenWindowsAutomatically = true
                    settings.loadWithOverviewMode = true

                    webview.loadData("$style$body", "text/html", "utf-8")

                    fab_page_up_button.setOnClickListener {
                        webview.scrollTo(0, 0)
                    }

                }

            }

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }

    override fun onBackPressed() {
        finish()
    }

}
