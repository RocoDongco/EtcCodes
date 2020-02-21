import android.util.Log

import okhttp3.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

/** 토큰 재발급
 * @param type  통신에서 사용하는 헤더 조건에 대한 값
 *              주로 http 코드값 401과 토큰 만료시에만 호출 되어 이후 재시도에서 새로 받은 값 사용됨
 * @author DevSCV
 */


class TokenAuthenticator(var type: String?) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {

        if (response.code == 401) {

            Log.d("test", "intest code 401 : ")

            val insetData = HashMap<String, String>()
            insetData["refresh"] = refreshToken

            val httpClient = OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)

            httpClient.addInterceptor { chain ->

                val original = chain.request()

                val requestBuilder = original.newBuilder()

                val request = requestBuilder.build()
                chain.proceed(request)
            }

            val client = httpClient.build()

            val retrofit = Retrofit.Builder()
                    .baseUrl(베이직 주소)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()


            val obtainAccessTokenRequest: APIInterface? = retrofit.create(공용 주소 클래스명)
            val call: Call<ResponseAccessToken>? = obtainAccessTokenRequest?.refreshTokenRequest(insetData)

            val tokenResponse: retrofit2.Response<ResponseAccessToken>? = call?.execute()

            val newTokenData: ResponseAccessToken? = tokenResponse?.body()
            newTokenData?.date?.access.let {
            접근 토큰 //암호화 처리는 알아서 = it }
            //Log.d("test", "intest accse : " + newTokenData?.date?.access.toString())

            newTokenData?.date?.expired_at.let {
            만료시간 받는 것 = it }
            when {
                type.equals("조건 방식 확인") -> {
                    return response.request.newBuilder().addHeader("헤더명", "헤더값등").addHeader("Content-Type", "application/json").build()
                }
                
                else -> {
                    //디폴트값 리턴 등.....
                }
            }

        } else {
            return null
        }

    }
}
