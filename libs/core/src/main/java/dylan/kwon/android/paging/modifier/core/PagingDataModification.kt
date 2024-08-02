package dylan.kwon.android.paging.modifier.core

import androidx.paging.TerminalSeparatorType

sealed class PagingDataModification<T : Any> {

    /**
     * the insertion position is determined using prev and next.
     * if prev and next are at the beginning or end of the list, they are null.
     * if the return value of [condition] is false, the insertion is not performed.
     * For a description of the type, please check [TerminalSeparatorType].
     */
    data class Insert<T : Any>(
        val data: T,
        val type: TerminalSeparatorType = TerminalSeparatorType.FULLY_COMPLETE,
        val condition: (before: T?, after: T?) -> Boolean
    ) : PagingDataModification<T>()

    /**
     * Adds [data] to the top of the list.
     * For a description of the type, please check [TerminalSeparatorType].
     */
    data class InsertToTop<T : Any>(
        val data: T,
        val type: TerminalSeparatorType = TerminalSeparatorType.FULLY_COMPLETE
    ) : PagingDataModification<T>()

    /**
     * Adds [data] to the bottom of the list.
     * For a description of the type, please check [TerminalSeparatorType].
     */
    data class InsertToBottom<T : Any>(
        val data: T,
        val type: TerminalSeparatorType = TerminalSeparatorType.FULLY_COMPLETE
    ) : PagingDataModification<T>()

    /**
     * Updates the existing [data] with new data.
     */
    data class Update<T : Any>(val data: T) : PagingDataModification<T>()

    /**
     * Deletes the [data].
     */
    data class Delete<T : Any>(val data: T) : PagingDataModification<T>()
}