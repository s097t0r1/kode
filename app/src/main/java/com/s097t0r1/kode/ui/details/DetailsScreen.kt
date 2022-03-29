package com.s097t0r1.kode.ui.details

import android.telephony.PhoneNumberUtils
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.s097t0r1.data.mock.mockUser
import com.s097t0r1.data.model.DataUser
import com.s097t0r1.data.model.toDomainModel
import com.s097t0r1.domain.entities.User
import com.s097t0r1.kode.R
import com.s097t0r1.kode.ui.theme.KodeTypography
import org.koin.androidx.compose.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

const val DETAILS_SCREEN = "details_screen"

@Composable
fun DetailsScreen(user: DataUser?) {

    val viewModel: DetailsViewModel by viewModel()

    DetailsScreen(user!!.toDomainModel())
}

@Composable
private fun DetailsScreen(user: User) {
    Column {
        DetailsHeader(
            modifier = Modifier.fillMaxWidth(),
            user = user
        )
        DetailsBody(
            modifier = Modifier.padding(horizontal = 20.dp),
            user = user
        )
    }
}

@Composable
private fun DetailsHeader(modifier: Modifier = Modifier, user: User) {
    Surface(
        modifier = modifier,
        color = Color(0xFFF7F7F8)
    ) {
        Box {
            IconButton(
                modifier = Modifier.align(Alignment.TopStart),
                onClick = {}
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null
                )
            }
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(104.dp)
                        .clip(CircleShape),
                    model = user.avatarUrl,
                    error = painterResource(id = R.drawable.ic_item_user_avatar_placeholder),
                    placeholder = painterResource(id = R.drawable.ic_item_user_avatar_placeholder),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row {
                    Text(
                        text = "${user.firstName} ${user.lastName}",
                        fontSize = 24.sp,
                        style = MaterialTheme.typography.subtitle2
                    )
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 4.dp),
                        text = user.userTag,
                        style = KodeTypography.Meta,
                        fontSize = 17.sp
                    )
                }
                Text(
                    modifier = Modifier.padding(12.dp),
                    text = user.position,
                    style = KodeTypography.Subtitle
                )
            }
        }
    }
}

@Composable
fun DetailsBody(modifier: Modifier = Modifier, user: User) {
    Column(
        modifier = modifier
    ) {
        val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        DetailsItem(
            modifier = Modifier.padding(vertical = 26.dp),
            painter = painterResource(id = R.drawable.ic_star),
            text = simpleDateFormat.format(user.birthday)
        ) {
            Text(
                text = LocalContext.current.resources.getQuantityString(
                    R.plurals.ages,
                    user.age,
                    user.age
                ),
                style = KodeTypography.Detail
            )
        }
        Divider(thickness = 0.5.dp)
        DetailsItem(
            modifier = Modifier.padding(vertical = 26.dp),
            painter = painterResource(id = R.drawable.ic_phone),
            text = PhoneNumberUtils.formatNumber(user.phone, Locale.getDefault().country)
        )
    }
}

private val User.age
    get() = ((Date().time - this.birthday.time) * 3.17E-10).roundToInt()

@Composable
fun DetailsItem(
    modifier: Modifier = Modifier,
    painter: Painter,
    text: String,
    slot: @Composable () -> Unit = {},
) {
    Row(modifier) {
        Icon(
            painter = painter,
            contentDescription = null
        )
        Spacer(Modifier.width(14.dp))
        Text(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            text = text
        )
        slot()
    }
}

@Preview
@Composable
private fun DetailsScreenPreview() {
    DetailsScreen(mockUser)
}
