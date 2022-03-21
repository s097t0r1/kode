package com.s097t0r1.kode.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.s097t0r1.domain.entities.Department
import com.s097t0r1.kode.R
import com.s097t0r1.kode.ui.theme.KodeTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KodeTheme {
                Scaffold {
                    val viewState by viewModel.viewState.collectAsState(MainViewState.InitialLoadingUsers())
                    MainScreen(
                        viewState = viewState,
                        onFilterClick = {},
                        onTabClick = viewModel::setDepartment,
                        onRetryClick = viewModel::getUsers,
                        onSearch = viewModel::setSearchQuery
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    viewState: MainViewState,
    onFilterClick: () -> Unit,
    onTabClick: (Department?) -> Unit,
    onRetryClick: () -> Unit,
    onSearch: (String) -> Unit
) {

    when (viewState) {
        MainViewState.CriticalError -> MainErrorScreen(onRetryClick = onRetryClick)
        else -> MainContentScreen(
            viewState = viewState,
            onFilterClick = onFilterClick,
            onTabClick = onTabClick,
            onSearch = onSearch
        )
    }


}

@Composable
fun MainContentScreen(
    modifier: Modifier = Modifier,
    viewState: MainViewState,
    onFilterClick: () -> Unit,
    onTabClick: (Department?) -> Unit,
    onSearch: (String) -> Unit
) {
    val (searchText, setSearchText) = remember { mutableStateOf("") }
    Column(modifier = modifier) {
        SearchField(
            text = searchText,
            onTextChange = {
                setSearchText(it)
                onSearch(it)
            },
            onFilterClick = onFilterClick,
        )
        DepartmentTabs(onTabClick)
        when (viewState) {
            is MainViewState.InitialLoadingUsers -> UsersList(
                viewState = viewState,
                users = viewState.users
            )
            is MainViewState.DisplayUsersByAlphabetically -> UsersList(
                viewState = viewState,
                users = viewState.users
            )
            is MainViewState.EmptySearchResult -> EmptySearchScreen(Modifier.fillMaxSize())
            else -> throw IllegalStateException("Illegal view state in MainContentScreen: " + viewState::class)
        }
    }
}


@Composable
fun MainErrorScreen(
    modifier: Modifier = Modifier,
    onRetryClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(56.dp),
            painter = painterResource(R.drawable.ic_flying_saucer),
            contentDescription = null
        )
        Text(
            text = stringResource(R.string.critical_error_title),
            style = MaterialTheme.typography.subtitle1
        )
        Text(
            text = stringResource(R.string.critical_error_subtitle),
            style = MaterialTheme.typography.caption
        )
        TextButton(onClick = onRetryClick) {
            Text(text = stringResource(R.string.critical_error_retry))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainErrorScreenPreview() {
    MainErrorScreen(onRetryClick = {})
}

@Preview(showBackground = true)
@Composable
fun MainEmptySearch() {
    MainContentScreen(
        viewState = MainViewState.EmptySearchResult,
        onFilterClick = { /*TODO*/ },
        onTabClick = {},
        onSearch = {}
    )
}

@Preview(showBackground = true)
@Composable
fun MainInitialLoadingPreview() {
    MainContentScreen(
        viewState = MainViewState.InitialLoadingUsers(),
        onFilterClick = { /*TODO*/ },
        onTabClick = {},
        onSearch = {}
    )
}

@Preview(showBackground = true)
@Composable
fun MainDisplayUsersByAlphabetically() {
    MainContentScreen(
        viewState = MainViewState.DisplayUsersByAlphabetically(mockUsers),
        onFilterClick = { /*TODO*/ },
        onTabClick = {},
        onSearch = {}
    )
}