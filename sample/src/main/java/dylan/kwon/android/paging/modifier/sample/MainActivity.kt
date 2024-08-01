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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import dylan.kwon.android.paging.modifier.sample.ui.theme.AndroidpagingmodifierTheme
import kotlinx.coroutines.flow.flowOf

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
                    onInsertToTopClick = viewModel::insertHeader,
                    onInsertToBottomClick = viewModel::insertFooter,
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
) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            List(
                pagingItems = pagingItems
            )
            Actions(
                modifier = Modifier.align(Alignment.BottomCenter),
                onInsertClick = onInsertClick,
                onInsertToTopClick = onInsertToTopClick,
                onInsertToBottomClick = onInsertToBottomClick,
            )
        }
    }
}

@Composable
private fun List(
    modifier: Modifier = Modifier,
    pagingItems: LazyPagingItems<MyData>,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 100.dp)
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
                headlineContent = {
                    Text(text = data.title)
                }
            )
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
            text = stringResource(id = R.string.insert_header),
            onClick = onInsertToTopClick
        )
        ActionButton(
            text = stringResource(id = R.string.insert_footer),
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
@Preview(showBackground = true)
fun Preview() {
    AndroidpagingmodifierTheme {
        Screen(
            pagingItems = flowOf(PagingData.empty<MyData>()).collectAsLazyPagingItems(),
            onInsertClick = {},
            onInsertToTopClick = {},
            onInsertToBottomClick = {},
        )
    }
}