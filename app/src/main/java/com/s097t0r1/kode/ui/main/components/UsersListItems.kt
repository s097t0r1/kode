package com.s097t0r1.kode.ui.main.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.placeholder.material.placeholder
import com.s097t0r1.data.mock.mockUser
import com.s097t0r1.domain.models.User
import com.s097t0r1.kode.R
import com.s097t0r1.kode.ui.theme.KodeTypography
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ItemUser(modifier: Modifier = Modifier, user: User, showPlaceholder: Boolean = false) {
    Row(modifier = modifier) {
        AsyncImage(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .size(72.dp)
                .clip(CircleShape)
                .placeholder(
                    visible = showPlaceholder,
                    shape = CircleShape
                ),
            model = user.avatarUrl,
            placeholder = painterResource(R.drawable.ic_item_user_avatar_placeholder),
            error = painterResource(R.drawable.ic_item_user_avatar_placeholder),
            contentDescription = null
        )
        Column(
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Row(
                modifier = Modifier.placeholder(
                    visible = showPlaceholder,
                    shape = RoundedCornerShape(8.dp)
                )
            ) {
                Text(
                    text = "${user.firstName} ${user.lastName}",
                    style = MaterialTheme.typography.subtitle2
                )
                Text(
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .align(Alignment.Bottom),
                    text = user.userTag,
                    style = KodeTypography.Meta,
                )
            }
            Text(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .placeholder(
                        visible = showPlaceholder,
                        shape = RoundedCornerShape(8.dp)
                    ),
                text = user.position,
                style = KodeTypography.Subtitle
            )
        }
    }
}

@Composable
fun BirthdayItemUser(modifier: Modifier = Modifier, user: User) {
    Row(modifier) {
        ItemUser(
            modifier = Modifier.weight(1f),
            user = user
        )
        BirthdayCaption(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = 3.dp),
            birthday = user.birthday
        )
    }
}

@Composable
fun BirthdayCaption(modifier: Modifier = Modifier, birthday: Date) {
    val formattedBirthday = SimpleDateFormat("d MMM", Locale.getDefault())
        .format(birthday).lowercase()
    Text(
        modifier = modifier,
        text = formattedBirthday,
        style = KodeTypography.Detail
    )
}

@Composable
fun BirthdayDivider(modifier: Modifier = Modifier, dividerText: String) {
    Row(modifier = modifier) {
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

@Preview
@Composable
private fun ItemUserPreview() {
    ItemUser(
        modifier = Modifier.fillMaxWidth(),
        user = mockUser
    )
}

@Preview
@Composable
private fun PlaceholderItemUserPreview() {
    ItemUser(modifier = Modifier.fillMaxWidth(), user = mockUser, showPlaceholder = true)
}

@Preview
@Composable
private fun BirthdayItemUserPreview() {
    BirthdayItemUser(
        modifier = Modifier.fillMaxWidth(),
        user = mockUser
    )
}

@Preview
@Composable
private fun BirthdayDividerPreview() {
    BirthdayDivider(dividerText = "2023")
}
