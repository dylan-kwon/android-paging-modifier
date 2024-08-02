@file:OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)

package dylan.kwon.android.paging.modifier.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import dylan.kwon.android.paging.modifier.sample.ui.theme.AndroidpagingmodifierTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidpagingmodifierTheme {
                val viewModel by viewModels<MainViewModel>()
                val pagingItems = viewModel.pagingData.collectAsLazyPagingItems()

                Screen(
                    pagingItems = pagingItems,
                    onInsertClick = viewModel::insert,
                    onInsertToTopClick = viewModel::insertToTop,
                    onInsertToBottomClick = viewModel::insertToBottom,
                    onUpdateClick = viewModel::update,
                    onDeleteClick = viewModel::delete
                )
            }
        }
    }
}

@Composable
fun Screen(
    modifier: Modifier = Modifier,
    pagingItems: LazyPagingItems<MyData>,
    onInsertClick: () -> Unit,
    onInsertToTopClick: () -> Unit,
    onInsertToBottomClick: () -> Unit,
    onDeleteClick: (MyData) -> Unit,
    onUpdateClick: (MyData) -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            val listState = rememberLazyListState()
            val coroutineScope = rememberCoroutineScope()
            List(
                listState = listState,
                pagingItems = pagingItems,
                onUpdateClick = onUpdateClick,
                onDeleteClick = onDeleteClick
            )
            Actions(
                modifier = Modifier.align(Alignment.BottomCenter),
                onInsertClick = onInsertClick,
                onInsertToTopClick = {
                    onInsertToTopClick()
                    coroutineScope.launch {
                        delay(50)
                        listState.animateScrollToItem(0)
                    }
                },
                onInsertToBottomClick = {
                    onInsertToBottomClick()
                    coroutineScope.launch {
                        delay(50)
                        listState.animateScrollToItem(pagingItems.itemCount - 1)
                    }
                },
            )
        }
    }
}

@Composable
private fun List(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    pagingItems: LazyPagingItems<MyData>,
    onDeleteClick: (MyData) -> Unit,
    onUpdateClick: (MyData) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        state = listState,
    ) {
        items(
            count = pagingItems.itemCount,
            key = pagingItems.itemKey {
                it.id
            }
        ) {
            val data = pagingItems[it]
            if (data != null) ListItem(
                modifier = Modifier.animateItemPlacement(),
                overlineContent = {
                    Text(text = "id: ${data.id}")
                },
                headlineContent = {
                    Text(text = data.title)
                },
                supportingContent = {
                    Text(text = "createdAt: ${data.createdAt}")
                },
                trailingContent = {
                    Row {
                        UpdateButton {
                            onUpdateClick(data)
                        }
                        DeleteButton {
                            onDeleteClick(data)
                        }
                    }
                }
            )
        }
    }
    if (pagingItems.loadState.prepend is LoadState.NotLoading &&
        pagingItems.loadState.prepend.endOfPaginationReached
    ) {
        LaunchedEffect(Unit) {
            listState.animateScrollToItem(0)
        }
    }
}

@Composable
private fun Actions(
    modifier: Modifier = Modifier,
    onInsertClick: () -> Unit,
    onInsertToTopClick: () -> Unit,
    onInsertToBottomClick: () -> Unit,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ActionButton(
            text = stringResource(id = R.string.insert),
            onClick = onInsertClick
        )
        ActionButton(
            text = stringResource(id = R.string.insert_to_top),
            onClick = onInsertToTopClick
        )
        ActionButton(
            text = stringResource(id = R.string.insert_to_bottom),
            onClick = onInsertToBottomClick
        )
    }
}

@Composable
private fun ActionButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    ElevatedButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Text(text = text)
    }
}

@Composable
fun UpdateButton(
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = stringResource(id = R.string.update)
        )
    }
}

@Composable
fun DeleteButton(
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(id = R.string.delete)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun Preview() {
    AndroidpagingmodifierTheme {
        Screen(
            pagingItems = flowOf(PagingData.empty<MyData>()).collectAsLazyPagingItems(),
            onInsertClick = {},
            onInsertToTopClick = {},
            onInsertToBottomClick = {},
            onDeleteClick = {},
            onUpdateClick = {}
        )
    }
}