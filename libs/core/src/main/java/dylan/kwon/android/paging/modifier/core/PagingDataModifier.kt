package dylan.kwon.android.paging.modifier.core

import androidx.paging.TerminalSeparatorType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PagingDataModifier<T : Any> {

    private val modifications = MutableStateFlow<List<PagingDataModification<T>>>(mutableListOf())
    val flow = modifications.asStateFlow()

    fun insert(
        data: T,
        type: TerminalSeparatorType = TerminalSeparatorType.FULLY_COMPLETE,
        condition: (prev: T?, next: T?) -> Boolean
    ) {
        modifications.updateList {
            this += PagingDataModification.Insert(data, type, condition)
        }
    }

    fun insertHeader(
        data: T,
        type: TerminalSeparatorType = TerminalSeparatorType.FULLY_COMPLETE
    ) {
        modifications.updateList {
            this += PagingDataModification.InsertHeader(data, type)
        }
    }

    fun insertFooter(
        data: T,
        type: TerminalSeparatorType = TerminalSeparatorType.FULLY_COMPLETE
    ) {
        modifications.updateList {
            this += PagingDataModification.InsertFooter(data, type)
        }
    }

    fun update(data: T) {
        modifications.updateList {
            this += PagingDataModification.Update(data)
        }
    }

    fun delete(data: T) {
        modifications.updateList {
            this += PagingDataModification.Delete(data)
        }
    }

}

private fun <T : Any> MutableStateFlow<List<PagingDataModification<T>>>.updateList(
    block: MutableList<PagingDataModification<T>>.() -> Unit
) =
    update {
        it.toMutableList().apply {
            block(this)
        }
    }