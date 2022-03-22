package com.s097t0r1.kode.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder
import com.s097t0r1.domain.entities.Department
import com.s097t0r1.domain.entities.User
import com.s097t0r1.kode.R
import com.s097t0r1.kode.ui.main.components.DepartmentTabs
import com.s097t0r1.kode.ui.main.managers.UsersManager
import com.s097t0r1.kode.ui.theme.KodeTypography
import java.text.SimpleDateFormat
import java.time.LocalDate
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
        if (isFocused) {
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

    var selectedTabIndex by remember { mutableStateOf(0) }

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
fun UsersListByAlphabet(
    modifier: Modifier = Modifier,
    viewState: MainViewState,
    users: List<User>
) {
    LazyColumn() {
        items(users) { user ->
            ItemUser(
                viewState = viewState,
                user = user
            )
        }
    }
}

@Composable
fun UsersListByBirthday(
    modifier: Modifier = Modifier,
    viewState: MainViewState,
    usersTuple: UsersManager.UsersBirthdayTuple
) {
    LazyColumn() {
        items(usersTuple.currentYear) { user ->
            ItemUser(viewState = viewState, user = user)
        }
        item {
            UsersListDivider(
                modifier = Modifier.padding(24.dp),
                dividerText = (Calendar.getInstance().get(Calendar.YEAR) + 1).toString()
            )
        }
        items(usersTuple.nextYear) { user ->
            ItemUser(viewState = viewState, user = user)
        }
    }
}

@Composable
fun EmptySearchScreen(modifier: Modifier = Modifier) {
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
fun ItemUser(
    modifier: Modifier = Modifier,
    viewState: MainViewState,
    user: User
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        AsyncImage(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .size(72.dp)
                .clip(CircleShape)
                .placeholder(
                    visible = viewState is MainViewState.InitialLoadingUsers,
                    highlight = PlaceholderHighlight.fade(),
                    shape = CircleShape,
                ),
            model = user.avatarUrl,
            contentDescription = null,
            placeholder = painterResource(R.drawable.ic_item_user_avatar_placeholder),
            error = painterResource(R.drawable.ic_item_user_avatar_placeholder)
        )
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
        ) {
            Row {
                Text(
                    modifier = Modifier.placeholder(
                        visible = viewState is MainViewState.InitialLoadingUsers,
                        highlight = PlaceholderHighlight.fade(),
                        shape = RoundedCornerShape(8.dp)
                    ),
                    text = "${user.firstName} ${user.lastName}",
                    style = MaterialTheme.typography.subtitle2
                )
                Text(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .align(Alignment.Bottom)
                        .placeholder(
                            visible = viewState is MainViewState.InitialLoadingUsers,
                            highlight = PlaceholderHighlight.fade(),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    text = user.userTag,
                    style = KodeTypography.Meta,
                )
            }
            Text(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .placeholder(
                        visible = viewState is MainViewState.InitialLoadingUsers,
                        highlight = PlaceholderHighlight.fade(),
                        shape = RoundedCornerShape(8.dp)
                    ),
                text = user.position,
                style = KodeTypography.Subtitle
            )
        }
        if (viewState is MainViewState.DisplayUsersByBirthday) {
            BirthdayCaption(
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.CenterVertically),
                birthDay = user.birthday
            )
        }
    }
}

@Composable
fun UsersListDivider(modifier: Modifier, dividerText: String) {
    Row(
        modifier = modifier
    ) {
        Divider(
            modifier = Modifier
                .weight(0.2f)
                .align(Alignment.CenterVertically)
        )
        Text(
            modifier = Modifier
                .weight(0.6f)
                .alpha(0.4f),
            textAlign = TextAlign.Center,
            text = dividerText
        )
        Divider(
            modifier = Modifier
                .weight(0.2f)
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun BirthdayCaption(modifier: Modifier, birthDay: Date) {
    val birthday = SimpleDateFormat("d MMM", Locale.getDefault())
        .format(birthDay).lowercase()
    Text(
        modifier = modifier,
        text = birthday,
        style = KodeTypography.Detail
    )
}

@Composable
@Preview
fun SearchFieldPreview() {
    val (text, setText) = remember { mutableStateOf("") }
    SearchField(text = text, onTextChange = setText, onFilterClick = {})
}

@Composable
@Preview
fun TabsPreview() {
    DepartmentTabs(onTabClick = {})
}

@Composable
@Preview(showBackground = true)
fun ItemUserPlaceholderPreview() {
    ItemUser(Modifier, MainViewState.InitialLoadingUsers(), mockUser)
}

@Composable
@Preview(showBackground = true)
fun ItemUserPreview() {
    ItemUser(Modifier, MainViewState.CriticalError, mockUser)
}

@Composable
@Preview(showBackground = true)
fun ItemUserBirthdayPreview() {
    ItemUser(
        Modifier,
        MainViewState.DisplayUsersByBirthday(
            UsersManager.UsersBirthdayTuple(
                emptyList(),
                emptyList()
            )
        ),
        mockUser
    )
}

@Composable
@Preview(showBackground = true)
fun EmptySearchPreview() {
    EmptySearchScreen()
}

@Composable
@Preview
fun UsersListDividerPreview() {
    UsersListDivider(
        modifier = Modifier.padding(24.dp),
        dividerText = LocalDate.now().year.toString()
    )
}
