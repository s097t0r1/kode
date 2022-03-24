package com.s097t0r1.kode.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.s097t0r1.data.mock.mockUsers
import com.s097t0r1.domain.entities.Department
import com.s097t0r1.kode.R
import com.s097t0r1.kode.ui.main.components.*
import com.s097t0r1.kode.ui.main.managers.UsersManager
import com.s097t0r1.kode.ui.theme.KodeTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KodeTheme {
                Scaffold {
                    val viewState by viewModel.viewState.collectAsState(MainViewState.InitialLoadingUsers)
                    val viewEffect by viewModel.viewEffect.collectAsState(MainViewEffect.Empty)

                    MainScreen(
                        viewState = viewState,
                        viewEffect = viewEffect,
                        onSortTypeSelect = viewModel::setSortingType,
                        onTabClick = viewModel::setDepartment,
                        onRetryClick = viewModel::getUsers,
                        onSearch = viewModel::setSearchQuery,
                        onRefresh = viewModel::refreshUsers
                    )
                }
            }
        }
    }
}

@Composable
private fun MainScreen(
    viewState: MainViewState,
    viewEffect: MainViewEffect,
    onSortTypeSelect: (SortingType) -> Unit,
    onTabClick: (Department?) -> Unit,
    onRetryClick: () -> Unit,
    onSearch: (String) -> Unit,
    onRefresh: () -> Unit
) {

    when (viewState) {
        MainViewState.CriticalError -> MainErrorScreen(onRetryClick = onRetryClick)
        else -> MainContentScreen(
            viewState = viewState,
            viewEffect = viewEffect,
            onSortTypeSelect = onSortTypeSelect,
            onTabClick = onTabClick,
            onSearch = onSearch,
            onRefresh = onRefresh
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MainContentScreen(
    modifier: Modifier = Modifier,
    viewState: MainViewState,
    viewEffect: MainViewEffect,
    onSortTypeSelect: (SortingType) -> Unit,
    onTabClick: (Department?) -> Unit,
    onSearch: (String) -> Unit,
    onRefresh: () -> Unit
) {
    val (searchText, setSearchText) = rememberSaveable { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            SortingBottomSheet(
                onSortingTypeSelect = {
                    onSortTypeSelect(it)
                    coroutineScope.launch { bottomSheetState.hide() }
                }
            )
        }
    ) {
        Column(modifier = modifier) {
            SearchField(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 22.dp),
                text = searchText,
                onTextChange = {
                    setSearchText(it)
                    onSearch(it)
                },
                onFilterClick = {
                    coroutineScope.launch { bottomSheetState.show() }
                }
            )
            DepartmentTabs(onTabClick)
            when (viewState) {
                is MainViewState.InitialLoadingUsers -> PlaceholderUsersList()
                is MainViewState.EmptySearchResult -> EmptySearch(Modifier.fillMaxSize())
                else -> {
                    MainRefreshableContentScreen(
                        viewState = viewState,
                        viewEffect = viewEffect,
                        onRefresh = onRefresh
                    )
                }
            }
        }
    }
}

@Composable
private fun MainErrorScreen(
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

@Composable
fun MainRefreshableContentScreen(
    viewState: MainViewState,
    viewEffect: MainViewEffect,
    onRefresh: () -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(viewEffect is MainViewEffect.OnSwipeRefresh),
        onRefresh = onRefresh
    ) {
        when (viewState) {
            is MainViewState.DisplayUsersByAlphabetically -> AlphabetUsersList(users = viewState.users)
            is MainViewState.DisplayUsersByBirthday -> BirthdayUsersList(
                beforeNewYear = viewState.birthdayTuple.currentYear,
                afterNewYear = viewState.birthdayTuple.nextYear
            )
            else -> throw IllegalStateException("Illegal state for viewState" + viewState::class)
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun MainErrorScreenPreview() {
    MainScreen(
        viewState = MainViewState.CriticalError,
        viewEffect = MainViewEffect.Empty,
        onSortTypeSelect = {},
        onTabClick = {},
        onRetryClick = {},
        onSearch = {},
        onRefresh = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun MainEmptySearch() {
    MainScreen(
        viewState = MainViewState.EmptySearchResult,
        viewEffect = MainViewEffect.Empty,
        onSortTypeSelect = { /*TODO*/ },
        onTabClick = {},
        onSearch = {},
        onRetryClick = {},
        onRefresh = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun MainInitialLoadingPreview() {
    MainScreen(
        viewState = MainViewState.InitialLoadingUsers,
        viewEffect = MainViewEffect.Empty,
        onSortTypeSelect = {},
        onTabClick = {},
        onSearch = {},
        onRetryClick = {},
        onRefresh = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun MainDisplayUsersByAlphabetically() {
    MainScreen(
        viewState = MainViewState.DisplayUsersByAlphabetically(mockUsers),
        viewEffect = MainViewEffect.Empty,
        onSortTypeSelect = { /*TODO*/ },
        onTabClick = {},
        onSearch = {},
        onRetryClick = {},
        onRefresh = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun MainDisplayUsersByBirthday() {
    MainScreen(
        viewState = MainViewState.DisplayUsersByBirthday(
            UsersManager.UsersBirthdayTuple(
                mockUsers,
                mockUsers
            )
        ),
        viewEffect = MainViewEffect.Empty,
        onSortTypeSelect = {},
        onTabClick = {},
        onSearch = {},
        onRetryClick = {},
        onRefresh = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun MainRefreshUsers() {
    MainScreen(
        viewState = MainViewState.DisplayUsersByAlphabetically(mockUsers),
        viewEffect = MainViewEffect.OnSwipeRefresh,
        onSortTypeSelect = { /*TODO*/ },
        onTabClick = {},
        onSearch = {},
        onRetryClick = {},
        onRefresh = {}
    )
}
