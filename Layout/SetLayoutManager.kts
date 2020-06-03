/*
    레이아웃 걍 호출 해서 매번 하느니 이렇게 쓰는게 제일 빠르더라
    SetLayoutManager.verticalLinearLayout(applicationContext, gitHubRecyclerView)
 */

object SetLayoutManager {

    fun verticalLinearLayout(context: Context, viewRecyclerView:  RecyclerView) {
        val placeLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewRecyclerView.layoutManager = placeLayoutManager
    }

    fun horizontalLinearLayout(context: Context, viewRecyclerView: RecyclerView) {
        val placeLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        viewRecyclerView.layoutManager = placeLayoutManager
    }

    //todo 알아서 추가 중 귀찬아서 더 추가 안하는거 아님 카드뷰 엔드리스 등 기타 잡다 한거 그냥 사용하는 것 다 넣으면 됨 


}
