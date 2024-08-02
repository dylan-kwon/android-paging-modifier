package dylan.kwon.android.paging.modifier.core

import androidx.paging.TerminalSeparatorType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PagingDataModifier<T : Any> {

    /**
     * A list of operations that modify PagingData.
     */
    private val modifications = MutableStateFlow<List<PagingDataModification<T>>>(mutableListOf())
    val flow = modifications.asStateFlow()

    /**
     * Inserts a new item into the list.
     * For more details, please refer to [PagingDataModification.Insert].
     */
    fun insert(
        data: T,
        type: TerminalSeparatorType = TerminalSeparatorType.FULLY_COMPLETE,
        condition: (prev: T?, next: T?) -> Boolean
    ) {
        modifications.updateList {
            this += PagingDataModification.Insert(data, type, condition)
        }
    }

    /**
     * Inserts a new item into the top of list.
     * For more details, please refer to [PagingDataModification.InsertHeader].
     */
    fun insertHeader(
        data: T,
        type: TerminalSeparatorType = TerminalSeparatorType.FULLY_COMPLETE
    ) {
        modifications.updateList {
            this += PagingDataModification.InsertHeader(data, type)
        }
    }

    /**
     * Inserts a new item into the bottom of list.
     * For more details, please refer to [PagingDataModification.InsertFooter].
     */
    fun insertFooter(
        data: T,
        type: TerminalSeparatorType = TerminalSeparatorType.FULLY_COMPLETE
    ) {
        modifications.updateList {
            this += PagingDataModification.InsertFooter(data, type)
        }
    }

    /**
     * Updates an existing item in the list.
     * For more details, please refer to [PagingDataModification.Update].
     */
    fun update(data: T) {
        modifications.updateList {
            this += PagingDataModification.Update(data)
        }
    }

    /**
     * Deletes an existing item in the list.
     * For more details, please refer to [PagingDataModification.Delete].
     */
    fun delete(data: T) {
        modifications.updateList {
            this += PagingDataModification.Delete(data)
        }
    }
}

/**
 * The existing list is shallow copied, [block] is executed,
 * and the result is reflected in the new state.
 */
private fun <T : Any> MutableStateFlow<List<PagingDataModification<T>>>.updateList(
    block: MutableList<PagingDataModification<T>>.() -> Unit
) = update {
    it.toMutableList().apply {
        block(this)
    }
}