package com.s097t0r1.kode.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.s097t0r1.domain.entities.Department
import com.s097t0r1.kode.R

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
        modifier = modifier.padding(horizontal = 16.dp, vertical = 4.dp)
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
fun MainTabs(onTabClick: (Department?) -> Unit) {

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
@Preview
fun SearchFieldPreview() {
    SearchField(text = "", onTextChange = {}, onFilterClick = {})
}

@Composable
@Preview
fun SearchFieldFocusedPreview() {
    SearchField(Modifier, "sdfasdf", {}, {})
}

@Composable
@Preview
fun TabsPreview() {
    MainTabs(onTabClick = {})
}
