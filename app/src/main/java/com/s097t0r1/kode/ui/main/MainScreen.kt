package com.s097t0r1.kode.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.s097t0r1.data.mock.mockUsers
import com.s097t0r1.domain.models.Department
import com.s097t0r1.domain.models.User
import com.s097t0r1.kode.R
import com.s097t0r1.kode.ui.details.DETAILS_ARGUMENT_USER_KEY
import com.s097t0r1.kode.ui.details.DETAILS_SCREEN
import com.s097t0r1.kode.ui.main.components.*
import kotlinx.coroutines.launch
import org.koin.androidx.compose.viewModel

const val MAIN_SCREEN = "main_screen"

@Composable
fun MainScreen(navController: NavController) {
    val viewModel: MainViewModel by viewModel()

    val viewState by viewModel.viewState.collectAsState()
    val viewEffect by viewModel.viewEffect.collectAsState(MainViewEffect.Empty)

    MainScreen(
        viewState = viewState,
        viewEffect = viewEffect,
        onRetryClick = viewModel::getUsers,
        onSortingTypeSelect = viewModel::setSortingType,
        onTabClick = viewModel::setDepartment,
        onSearch = viewModel::setSearchQuery,
        onSwipeRefresh = viewModel::refreshUsers,
        onItemClick = {
            navController.previousBackStackEntry?.arguments?.putString(
                DETAILS_ARGUMENT_USER_KEY,
                it.id
            )
            navController.navigate("$DETAILS_SCREEN/" + it.id)
        }
    )

}

@Composable
private fun MainScreen(
    viewState: MainViewState,
    viewEffect: MainViewEffect,
    onRetryClick: () -> Unit,
    onSortingTypeSelect: (SortingType) -> Unit,
    onTabClick: (Department?) -> Unit,
    onSearch: (String) -> Unit,
    onSwipeRefresh: () -> Unit,
    onItemClick: (User) -> Unit
) {
    when {
        viewState.criticalError -> CriticalError(onRetryClick = onRetryClick)
        else -> MainContent(
            viewState = viewState,
            viewEffect = viewEffect,
            onSortTypeSelect = onSortingTypeSelect,
            onTabClick = onTabClick,
            onSearch = onSearch,
            onRefresh = onSwipeRefresh,
            onItemClick = onItemClick
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    viewState: MainViewState,
    viewEffect: MainViewEffect,
    onSortTypeSelect: (SortingType) -> Unit,
    onTabClick: (Department?) -> Unit,
    onSearch: (String) -> Unit,
    onRefresh: () -> Unit,
    onItemClick: (User) -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetContent = {
            SortingBottomSheet(
                currentSortingType = viewState.currentSortingType,
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
                text = viewState.currentSearchQuery,
                onTextChange = {
                    onSearch(it)
                },
                onFilterClick = {
                    coroutineScope.launch { bottomSheetState.show() }
                }
            )
            DepartmentTabs(viewState.currentDepartmentTab, onTabClick)
            when {
                viewState.isInitialLoading -> PlaceholderUsersList()
                viewState.isEmptySearchResult -> EmptySearch(Modifier.fillMaxSize())
                else -> {
                    RefreshableContentScreen(
                        viewState = viewState,
                        viewEffect= viewEffect,
                        onRefresh = onRefresh,
                        onItemClick = onItemClick
                    )
                }
            }
        }
    }
}

@Composable
private fun CriticalError(
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
private fun RefreshableContentScreen(
    viewState: MainViewState,
    viewEffect: MainViewEffect,
    onRefresh: () -> Unit,
    onItemClick: (User) -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(viewEffect is MainViewEffect.Refreshing),
        onRefresh = onRefresh
    ) {
        when (viewState.currentSortingType) {
            SortingType.ALPHABETICALLY -> AlphabetUsersList(
                users = viewState.alphabetUsers,
                onItemClick = onItemClick
            )
            SortingType.BIRTHDAY -> BirthdayUsersList(
                beforeNewYear = viewState.birthdayTuple.first,
                afterNewYear = viewState.birthdayTuple.second,
                onItemClick = onItemClick
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun MainErrorScreenPreview() {
    MainScreen(
        viewState = MainViewState(isInitialLoading = false, criticalError = true),
        viewEffect = MainViewEffect.Empty,
        onSortingTypeSelect = {},
        onTabClick = {},
        onRetryClick = {},
        onSearch = {},
        onSwipeRefresh = {},
        onItemClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun MainEmptySearch() {
    MainScreen(
        viewState = MainViewState(
            isInitialLoading = false,
            criticalError = false,
            isEmptySearchResult = true
        ),
        viewEffect = MainViewEffect.Empty,
        onSortingTypeSelect = {},
        onTabClick = {},
        onSearch = {},
        onRetryClick = {},
        onSwipeRefresh = {},
        onItemClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun MainInitialLoadingPreview() {
    MainScreen(
        viewState = MainViewState(isInitialLoading = true),
        viewEffect = MainViewEffect.Empty,
        onSortingTypeSelect = {},
        onTabClick = {},
        onSearch = {},
        onRetryClick = {},
        onSwipeRefresh = {},
        onItemClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun MainDisplayUsersByAlphabetically() {
    MainScreen(
        viewState = MainViewState(
            isInitialLoading = false,
            currentSortingType = SortingType.ALPHABETICALLY,
            alphabetUsers = mockUsers
        ),
        viewEffect = MainViewEffect.Empty,
        onSortingTypeSelect = {},
        onTabClick = {},
        onSearch = {},
        onRetryClick = {},
        onSwipeRefresh = {},
        onItemClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun MainDisplayUsersByBirthday() {
    MainScreen(
        viewState = MainViewState(
            isInitialLoading = false,
            currentSortingType = SortingType.BIRTHDAY,
            birthdayTuple = Pair(mockUsers, mockUsers)
        ),
        viewEffect = MainViewEffect.Empty,
        onSortingTypeSelect = {},
        onTabClick = {},
        onSearch = {},
        onRetryClick = {},
        onSwipeRefresh = {},
        onItemClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun MainRefreshUsers() {
    MainScreen(
        viewState = MainViewState(
            isInitialLoading = false,
            alphabetUsers = mockUsers,
        ) ,
        viewEffect = MainViewEffect.Refreshing,
        onSortingTypeSelect = {},
        onTabClick = {},
        onSearch = {},
        onRetryClick = {},
        onSwipeRefresh = {},
        onItemClick = {}
    )
}
