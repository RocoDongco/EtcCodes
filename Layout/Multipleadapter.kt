/**
 *  Multipleadapter 
 *  어뎁터 클래스 [다중 리사이클러뷰 : 이미지 + 배너 + 다른 매뉴 같은 다중 어뎁터에서 사용]
 *   @param act 
 *   @param presenter
 */
class Multipleadapter(var act: BaseActivity, var presenter: Presenter) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //전체 화면 구성을 위한 데이터
    var datas = ArrayList<Any>() <-- 리사이클러 뷰 사이즈를 맞추기 위한 사이즈용 변수 아무값이나 등록하여 사용 

    val VIEW_TYPE_BANNER = 10001 
    val VIEW_TYPE_SALE = 10005 
    val VIEW_TYPE_RECOMMEND_TITLE = 10006 
    val VIEW_TYPE_RECOMMEND = 10007 
    val VIEW_TYPE_FOOTER = 10008 //최하단 매뉴
    val VIEW_TYPE_ERROR = 10009 //에러
    val VIEW_TYPE_EMPTY = 10010 //공백

    var mLayoutInflater: LayoutInflater = LayoutInflater.from(presenter.mContext)

    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            VIEW_TYPE_BANNER -> return BannerViewHolder(act, mLayoutInflater.inflate(R.layout.cell_banner_item, parent, false), presenter)
            VIEW_TYPE_SALE -> return SaleViewHolder(act, mLayoutInflater.inflate(R.layout.cell_sale_layout_item, parent, false), presenter)
            VIEW_TYPE_RECOMMEND_TITLE -> return RecommeedTitleViewHolder(act, mLayoutInflater.inflate(R.layout.cell_recommendation_title, parent, false), presenter)
            VIEW_TYPE_RECOMMEND -> return RecommeedViewHolder(act, mLayoutInflater.inflate(R.layout.cell_recommendation_item, parent, false), presenter)
            VIEW_TYPE_EMPTY -> return EmptyHolder(mLayoutInflater.inflate(R.layout.cell_enurimart_list_goods_empty, parent, false))
            VIEW_TYPE_ERROR -> return ErrorHolder(act, mLayoutInflater.inflate(R.layout.cell_enurimart_network_error, parent, false), presenter)
            else -> return FooterHolder(act, mLayoutInflater.inflate(R.layout.cell_enuri_footer, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
 
        return when (datas[position]) {  <---- 데이터 확인 하여 각각에 홀더 분류
            is BannerVoTest -> {
                VIEW_TYPE_BANNER
            }
            is ListGoodsVo -> {
                VIEW_TYPE_RECOMMEND
            }
            is HomeRecomTitleVo -> {
                VIEW_TYPE_RECOMMEND_TITLE
            }
            is ListEmptyVo -> {
                VIEW_TYPE_EMPTY
            }
            is ListErrorVo -> {
                VIEW_TYPE_ERROR
            }
            else -> {
                VIEW_TYPE_FOOTER
            }
        }

    }

    fun setData(mdata: ArrayList<Any>) {
        datas = mdata
        presenter.act.runOnUiThread(Runnable {
            notifyDataSetChanged()
        })
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = datas[position]
        when (holder) {

            is BannerViewHolder -> {

                holder.onBind(data as BannerVoTest)
            }
            is RecommeedViewHolder -> {
                holder.onBind(position, data as ListGoodsVo)
            }
            is RecommeedTitleViewHolder -> {
                holder.onBind()
            }
            is FooterHolder -> {
                holder.onBind(act, mLayoutInflater)
            }

            is SaleViewHolder -> {
                holder.onBind()
            }

            is ListErrorHolder -> {
                holder.onBind()
            }

            else -> {
                val h = holder as ListEmptyHolder
                h.onBind(data as ListEmptyVo)
            }


        }
    }

    /**
     *  BannerViewHolder
     *  홈에서 배너 아이콘을 가지는 화면
     *   @param act
     *   @param itemView
     *   @param presenter
     */
    internal class BannerViewHolder(act: BaseActivity, itemView: View, val presenter: EnuriMartHomePresenter) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val bannerData: BannerVoTest? = null
        fun onBind(vo: BannerVoTest) {
            ImageLoader.getInstance().displayImage(vo.imageUri, itemView.iv_enurimart_homebanner)
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            bannerData?.url?.let {
                if (!it.isEmpty())
                    presenter.goURL(it)
            }

        }

        init {

        }
    }

    /**
     *  CateViewHolder
     *  홈에서 배너 아이콘을 가지는 홀더 클래스
     *   @param act
     *   @param itemView
     *   @param presenter
     */
    internal class CateViewHolder(act: BaseActivity, itemView: View, presenter: EnuriMartHomePresenter) : RecyclerView.ViewHolder(itemView) {
        var cate_top_layout: RecyclerView
        fun onBind() {
        }

        init {
            val sGridSpacesitemDecoration = SGridSpacesitemDecoration(act.resources.getDimensionPixelSize(R.dimen.list_menu_spaces))
            cate_top_layout = itemView.findViewById(R.id.cate_top_layout)
            val categoryAdapter = MartCategoryAdapter(act, "mart_home")
            cate_top_layout.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL)
            cate_top_layout.adapter = categoryAdapter
            cate_top_layout.addItemDecoration(sGridSpacesitemDecoration)
            categoryAdapter.setData(presenter.cateList)
        }
    }


/**
 *  SaleViewHolder
 * 
 *   @param act
 *   @param itemView
 *   @param presenter
 */
internal class SaleViewHolder(act: BaseActivity, itemView: View, presenter: EnuriMartHomePresenter) : RecyclerView.ViewHolder(itemView) {
    var sale_menu_reycler_view: RecyclerView? = null
    var sale_item_reycler_view: RecyclerView? = null
    fun onBind() {

    }

    init {
        sale_menu_reycler_view = itemView.findViewById(R.id.sale_menu_reycler_view)
        sale_item_reycler_view = itemView.findViewById(R.id.sale_item_reycler_view)

        val itemDecoration = SpacesItemDecoration(act.resources.getDimensionPixelSize(R.dimen.list_first_spaces), act.resources.getDimensionPixelSize(R.dimen.list_item_spaces))
        val martSaleListTopAdapter = MartSaleListTopAdapter(act, presenter.discountTopDataList)

        sale_menu_reycler_view?.layoutManager = LinearLayoutManager(act, RecyclerView.HORIZONTAL, false)
        sale_menu_reycler_view?.adapter = martSaleListTopAdapter
        sale_menu_reycler_view?.addItemDecoration(itemDecoration)

        val martSaleListSubAdapter = MartSaleListSubAdapter(act, presenter.discountSubData, presenter, TYPE_MART_SALE_SUB_LIST)
        sale_item_reycler_view?.layoutManager = LinearLayoutManager(act, RecyclerView.HORIZONTAL, false)
        sale_item_reycler_view?.adapter = martSaleListSubAdapter
        sale_item_reycler_view?.addItemDecoration(itemDecoration)

        martSaleListTopAdapter?.setItemClickListener(object : MartSaleListTopAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int) {
                if (presenter.discountTopDataList != null) {
                    for (a in presenter.discountTopDataList!!.indices) {
                        presenter.discountTopDataList!![a].isClick = false
                    }
                }
                presenter.discountTopDataList?.get(position)?.isClick = true
                martSaleListTopAdapter.notifyDataSetChanged()
                presenter.discountSubData.clear()
                presenter.discountTopDataList?.get(position)?.modelList?.let {
                    presenter.discountSubData.addAll(it)
                    martSaleListSubAdapter.notifyDataSetChanged()
                    sale_item_reycler_view!!.scrollToPosition(0)
                }
            }
        })

    }
}

/**
 *  RecommeedTitleViewHolder
 *   @param act
 *   @param itemView
 *   @param presenter
 */
internal class RecommeedTitleViewHolder(act: BaseActivity, itemView: View, presenter: EnuriMartHomePresenter) : RecyclerView.ViewHolder(itemView) {
    fun onBind() {

    }
}

................................................................... 중략 ....................................

/**
 *  EnurimartListErrorHolder 
 *   @param act
 *   @param itemView
 *   @param presenter
 */
internal class EnurimartListErrorHolder(act: BaseActivity, itemView: View, presenter: EnuriMartHomePresenter?) : RecyclerView.ViewHolder(itemView) {

    var frame_networkerror_go_enurihome: RelativeLayout
    var frame_networkerror_refresh: RelativeLayout
    fun onBind() {

    }

    init {
        frame_networkerror_go_enurihome = itemView.findViewById(R.id.frame_networkerror_go_enurihome)
        frame_networkerror_refresh = itemView.findViewById(R.id.frame_networkerror_refresh)

        frame_networkerror_go_enurihome.setOnClickListener {
            act.finish()
        }

        frame_networkerror_refresh.setOnClickListener {
            act.recreate()
        }
    }
}