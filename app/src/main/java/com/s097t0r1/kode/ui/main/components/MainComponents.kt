package com.s097t0r1.kode.ui.main.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.s097t0r1.data.mock.mockUsers
import com.s097t0r1.domain.entities.Department
import com.s097t0r1.domain.entities.User
import com.s097t0r1.kode.R
import java.util.*

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    isFocused: Boolean,
    onFocus: (Boolean) -> Unit,
    onFilterClick: () -> Unit,
    onCrossClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF7F7F8),
        modifier = modifier
    ) {
        val searchIconAlpha = if (isFocused) 1f else 0.3f

        CompositionLocalProvider(LocalContentAlpha provides 0.3f) {
            Row() {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 14.dp),
                    tint = LocalContentColor.current.copy(alpha = searchIconAlpha)
                )
                TextField(
                    value = text,
                    onValueChange = onTextChange,
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged { onFocus(it.isFocused) },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = LocalContentColor.current.copy(alpha = 1f),
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    placeholder = {
                        Text(
                            text = if (isFocused) "" else stringResource(R.string.search_bar_placeholder),
                            color = LocalContentColor.current.copy(0.3f),
                        )
                    },
                )
                if (text.isEmpty()) {
                    Icon(
                        painter = painterResource(R.drawable.ic_filter),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(end = 14.dp)
                            .clickable { onFilterClick() }
                    )
                } else {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(end = 14.dp)
                            .clickable { onCrossClick() },
                    )
                }
            }
        }
    }
}

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    onFilterClick: () -> Unit,
) {
    val (isFocused, setIsFocused) = remember { mutableStateOf(false) }
    val localFocusManager = LocalFocusManager.current

    Row(
        modifier = modifier
    ) {
        SearchBar(
            modifier = Modifier.weight(1f),
            text = text,
            onTextChange = onTextChange,
            isFocused = isFocused,
            onFocus = setIsFocused,
            onFilterClick = onFilterClick,
            onCrossClick = { onTextChange("") }
        )
         AnimatedVisibility(isFocused) {
            TextButton(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 12.dp),
                onClick = { localFocusManager.clearFocus() }
            ) {
                Text("Отмена")
            }
        }

    }
}

@Composable
fun DepartmentTabs(onTabClick: (Department?) -> Unit) {

    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        backgroundColor = Color.Transparent,
        indicator = {
            TabRowDefaults.Indicator(
                color = MaterialTheme.colors.primary,
                modifier = Modifier.tabIndicatorOffset(it[selectedTabIndex])
            )
        }
    ) {
        DepartmentTabs.values().forEach { tab ->
            Tab(
                selected = selectedTabIndex == tab.ordinal,
                onClick = {
                    selectedTabIndex = tab.ordinal
                    onTabClick(tab.department)
                },
                text = { Text(text = stringResource(tab.title)) },
            )
        }
    }
}

@Composable
fun AlphabetUsersList(
    modifier: Modifier = Modifier,
    users: List<User>
) {
    LazyColumn(modifier) {
        items(users) { user ->
            ItemUser(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                user = user
            )
        }
    }
}

@Composable
fun BirthdayUsersList(
    modifier: Modifier = Modifier,
    beforeNewYear: List<User>,
    afterNewYear: List<User>
) {
    LazyColumn(modifier) {
        items(beforeNewYear) { user ->
            BirthdayItemUser(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                user = user
            )
        }
        item {
            BirthdayDivider(
                modifier = Modifier.padding(24.dp),
                dividerText = (Calendar.getInstance().get(Calendar.YEAR) + 1).toString()
            )
        }
        items(afterNewYear) { user ->
            BirthdayItemUser(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                user = user
            )
        }
    }
}

@Composable
fun PlaceholderUsersList(modifier: Modifier = Modifier) {
    LazyColumn(modifier) {
        items(mockUsers) { user ->
            ItemUser(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                user = user,
                showPlaceholder = true
            )
        }
    }
}

@Composable
fun EmptySearch(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .padding(8.dp)
                .size(56.dp),
            painter = painterResource(R.drawable.ic_empty_search_result),
            contentDescription = null
        )
        Text(
            text = stringResource(R.string.empty_search_title),
            style = MaterialTheme.typography.subtitle2
        )
        Text(
            modifier = Modifier.padding(12.dp),
            text = stringResource(R.string.empty_search_subtitle),
            style = MaterialTheme.typography.caption
        )
    }
}

@Composable
fun SortingBottomSheet(modifier: Modifier = Modifier, onSortingTypeSelect: (SortingType) -> Unit) {

    val (currentSortingType, setCurrentSortingType) = remember { mutableStateOf(SortingType.ALPHABETICALLY) }

    Column(
        modifier = modifier
    ) {
        Divider(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .clip(RoundedCornerShape(8.dp))
                .width(56.dp)
                .align(Alignment.CenterHorizontally),

            thickness = 5.dp
        )
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(R.string.sorting_title),
            style = MaterialTheme.typography.h5,
        )
        Spacer(modifier = Modifier.height(34.dp))
        Column(
            modifier = Modifier
                .padding(horizontal = 18.dp)
                .selectableGroup()
        ) {
            SortingType.values().forEach() {
                SortItem(
                    modifier = Modifier.fillMaxWidth(),
                    sortingType = it,
                    isChecked = it == currentSortingType,
                    onSelect = { type ->
                        onSortingTypeSelect(type)
                        setCurrentSortingType(type)
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Divider(
            modifier = Modifier
                .padding(vertical = 9.dp)
                .clip(RoundedCornerShape(8.dp))
                .width(134.dp)
                .align(Alignment.CenterHorizontally),
            thickness = 5.dp
        )
    }
}

@Composable
fun SortItem(
    modifier: Modifier = Modifier,
    sortingType: SortingType,
    isChecked: Boolean,
    onSelect: (SortingType) -> Unit
) {
    Row(modifier) {
        RadioButton(
            selected = isChecked,
            onClick = { onSelect(sortingType) },
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colors.primary,
                unselectedColor = MaterialTheme.colors.primary,
            )
        )
        Spacer(Modifier.width(14.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = stringResource(sortingType.value),
            style = MaterialTheme.typography.body2,
        )
    }
}

@Composable
@Preview
private fun SearchFieldPreview() {
    val (text, setText) = remember { mutableStateOf("") }
    SearchField(text = text, onTextChange = setText, onFilterClick = {})
}

@Composable
@Preview
private fun TabsPreview() {
    DepartmentTabs(onTabClick = {})
}

@Composable
@Preview(showBackground = true)
private fun EmptySearchPreview() {
    EmptySearch()
}

@Composable
@Preview(showBackground = true)
private fun PlaceholderUsersListPreview() {
    PlaceholderUsersList()
}

@Composable
@Preview(showBackground = true)
private fun AlphabetUsersListPreview() {
    AlphabetUsersList(users = mockUsers)
}

@Composable
@Preview(showBackground = true)
private fun BirthdayUsersListPreview() {
    BirthdayUsersList(beforeNewYear = mockUsers, afterNewYear = mockUsers)
}

@Composable
@Preview(showBackground = true)
private fun SortBottomSheetPreview() {
    SortingBottomSheet(onSortingTypeSelect = {})
}

@Composable
@Preview
private fun SortingItemPreview() {
    SortItem(sortingType = SortingType.ALPHABETICALLY, isChecked = true, onSelect = {})
}