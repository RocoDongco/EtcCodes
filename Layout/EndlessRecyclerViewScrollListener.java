import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/** Created by devScv
 * EndlessRecyclerViewScrollListener
 * 1차 : 일차 기능 업데이트
 * 2차 수정 : Toolbar 변경 / fab 버튼 등의 상태 업데이트 	
 */

public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private int visibleThreshold = 5;
    // 로드 한 데이터의 현재 오프셋 인덱스
    private int currentPage = 0;
    // 마지막로드 후 데이터 세트의 총 항목 수
    private int previousTotalItemCount = 0;
    // 데이터 로드 확인용
    private boolean loading = true;
    //페이지 시작 인덱스
    private int startingPageIndex = 0;

    RecyclerView.LayoutManager mLayoutManager;

    private Toolbar toolbar;

    private RelativeLayout fab;

    private ConstraintLayout constraintLayout;

    public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager, Toolbar toolbar, RelativeLayout fab) {
        this.fab = fab;
        this.toolbar = toolbar;
        this.mLayoutManager = layoutManager;
    }

    public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager, Toolbar toolbar) {
        this.toolbar = toolbar;
        this.mLayoutManager = layoutManager;
    }

    public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager, ConstraintLayout constraintLayout) {
        this.mLayoutManager = layoutManager;
        this.constraintLayout = constraintLayout;
    }

    public EndlessRecyclerViewScrollListener(GridLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    public EndlessRecyclerViewScrollListener(StaggeredGridLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    //스크롤시 일어나야 하는 이벤트 관련 메소드
    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        int lastVisibleItemPosition = 0;
        int totalItemCount = mLayoutManager.getItemCount();

        //툴바의 상단 Elevation 값 변경
        if (toolbar != null) {
            if (!view.canScrollVertically(-1)) {
                toolbar.setElevation(0);
            } else {
                toolbar.setElevation(4);
            }
        }
        //fab 비활성화 또는 활성화
        if (fab != null) {
            if (!view.canScrollVertically(-1)) {
                fab.setVisibility(View.GONE);
            } else {
                fab.setVisibility(View.VISIBLE);
            }
        }

        //tab + toolbar
        if (constraintLayout != null) {
            if (!view.canScrollVertically(-1)) {
                constraintLayout.setElevation(0);
            } else {
                constraintLayout.setElevation(4);
            }
        }


        if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
            // get maximum element within the list
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        } else if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        }

        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
            currentPage++;
            onLoadMore(currentPage, totalItemCount, view);
            loading = true;
        }
    }

    public void resetState() {
        this.currentPage = this.startingPageIndex;
        this.previousTotalItemCount = 0;
        this.loading = true;
    }

    //새로운 값 받아서 로드
    public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);

}