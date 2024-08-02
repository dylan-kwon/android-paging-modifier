package dylan.kwon.android.paging.modifier.sample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dylan.kwon.android.paging.modifier.core.PagingDataModifier
import dylan.kwon.android.paging.modifier.core.modifier

class MainViewModel : ViewModel() {

    private val pager = Pager(
        config = PagingConfig(30)
    ) {
        MyDataPagingSource()
    }

    private var insertCount = 0

    private val modifier = PagingDataModifier<MyData>()

    val pagingData = pager.flow
        .cachedIn(viewModelScope)
        .modifier(modifier) {
            it.id
        }
        .cachedIn(viewModelScope)

    fun insert() {
        modifier.insert(
            data = createNewData("Insert"),
            condition = { _, after ->
                after?.id == 2
            }
        )
    }

    fun insertHeader() {
        modifier.insertHeader(
            createNewData("Insert Header")
        )
    }

    fun insertFooter() {
        modifier.insertFooter(
            createNewData("Insert Footer")
        )
    }

    fun update(myData: MyData) {
        modifier.update(
            myData.copy(
                createdAt = System.currentTimeMillis()
            )
        )
    }

    fun delete(myData: MyData) {
        modifier.delete(myData)
    }

    private fun createNewData(titlePrefix: String): MyData {
        val id = -(++insertCount)
        return MyData(
            id = id,
            title = titlePrefix,
            createdAt = System.currentTimeMillis()
        )
    }

}