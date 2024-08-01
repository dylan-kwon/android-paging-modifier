package dylan.kwon.android.paging.modifier.core

import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.insertFooterItem
import androidx.paging.insertHeaderItem
import androidx.paging.insertSeparators
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine


/**
 * Combines PagingData with a [PagingDataModification] List to return a new PagingData.
 */
fun <T : Any, K : Any> Flow<PagingData<T>>.modifier(
    modifier: PagingDataModifier<T>,
    key: (T) -> K
) = combine(modifier.flow) { pagingData, modifications ->
    modifications.fold(pagingData) { acc, mod ->
        acc.applyModify(mod, key)
    }
}

/**
 * Applies individual modifiers to PagingData to return a new PagingData.
 */
internal fun <T : Any, K : Any> PagingData<T>.applyModify(
    modification: PagingDataModification<T>,
    key: (T) -> K
): PagingData<T> = when (modification) {

    // Insert Data.
    is PagingDataModification.Insert<T> -> insertSeparators { before: T?, after: T? ->
        when (modification.condition(before, after)) {
            true -> modification.data
            else -> null
        }
    }

    // Insert Header.
    is PagingDataModification.InsertHeader<T> -> insertHeaderItem(
        item = modification.data
    )

    // Insert Footer.
    is PagingDataModification.InsertFooter<T> -> insertFooterItem(
        item = modification.data,
        terminalSeparatorType = modification.type
    )

    // Update Data.
    is PagingDataModification.Update<T> -> map { data ->
        when (isEquals(data, modification.data, key)) {
            true -> modification.data
            else -> data
        }
    }

    // Delete Data.
    is PagingDataModification.Delete<T> -> filter { data ->
        !isEquals(data, modification.data, key)
    }
}

/**
 * Compares the keys of two objects for equality.
 */
private fun <T : Any, K : Any> isEquals(
    a: T,
    b: T,
    key: (T) -> K
): Boolean = key(a) == key(b)