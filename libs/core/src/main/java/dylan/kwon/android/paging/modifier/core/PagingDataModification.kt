package dylan.kwon.android.paging.modifier.core

import androidx.paging.TerminalSeparatorType

sealed class PagingDataModification<T : Any> {

    /**
     * The insertion position is determined using prev and next.
     * If prev and next are at the beginning or end of the list, they are null.
     * If the return value is null, the insertion is not performed.
     */
    data class Insert<T : Any>(
        val data: T,
        val type: TerminalSeparatorType = TerminalSeparatorType.FULLY_COMPLETE,
        val condition: (before: T?, after: T?) -> Boolean
    ) : PagingDataModification<T>()

    /**
     * Adds [data] to the top of the list.
     */
    data class InsertHeader<T : Any>(
        val data: T,
        val type: TerminalSeparatorType = TerminalSeparatorType.FULLY_COMPLETE
    ) : PagingDataModification<T>()

    /**
     * Adds [data] to the bottom of the list.
     */
    data class InsertFooter<T : Any>(
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