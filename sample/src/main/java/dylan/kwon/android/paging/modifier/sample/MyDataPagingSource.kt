package dylan.kwon.android.paging.modifier.sample

import androidx.paging.PagingSource
import androidx.paging.PagingState

class MyDataPagingSource(
    private val getDataUseCase: GetDataUseCase = GetDataUseCase()
) : PagingSource<Int, MyData>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MyData> {
        try {
            val id = params.key ?: 1
            val data = getDataUseCase(id) ?: listOf()
            return LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = data.lastOrNull()?.id?.inc()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MyData>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        val firstId = anchorPage.data.firstOrNull()?.id
        val lastId = anchorPage.data.lastOrNull()?.id?.inc()
        return firstId ?: lastId
    }
}