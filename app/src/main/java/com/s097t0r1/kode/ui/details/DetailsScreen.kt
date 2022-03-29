package com.s097t0r1.kode.ui.details

import android.content.Intent
import android.net.Uri
import android.telephony.PhoneNumberUtils
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.accompanist.placeholder.material.placeholder
import com.s097t0r1.domain.entities.User
import com.s097t0r1.kode.R
import com.s097t0r1.kode.ui.theme.KodeTypography
import org.koin.androidx.compose.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

const val DETAILS_ARGUMENT_USER_KEY = "details_user"
const val DETAILS_SCREEN = "details_screen"

@Composable
fun DetailsScreen(navController: NavHostController, id: String) {

    val viewModel: DetailsViewModel by viewModel()
    val viewState by viewModel.viewState.collectAsState()

    viewModel.getUser(id)

    DetailsScreen(
        viewState = viewState,
        onBackButton = { navController.popBackStack() }
    )
}

@Composable
private fun DetailsScreen(viewState: DetailsViewState, onBackButton: () -> Unit) {
    Column {
        DetailsHeader(
            modifier = Modifier.fillMaxWidth(),
            viewState = viewState,
            onBackButton = onBackButton
        )
        DetailsBody(
            modifier = Modifier.padding(horizontal = 20.dp),
            viewState = viewState
        )
    }
}

@Composable
private fun DetailsHeader(
    modifier: Modifier = Modifier,
    viewState: DetailsViewState,
    onBackButton: () -> Unit
) {
    Surface(
        modifier = modifier,
        color = Color(0xFFF7F7F8)
    ) {
        Box {
            IconButton(
                modifier = Modifier.align(Alignment.TopStart),
                onClick = { onBackButton() }
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
                        .clip(CircleShape)
                        .placeholder(visible = viewState.isLoading),
                    model = viewState.user.avatarUrl,
                    error = painterResource(id = R.drawable.ic_item_user_avatar_placeholder),
                    placeholder = painterResource(id = R.drawable.ic_item_user_avatar_placeholder),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.placeholder(visible = viewState.isLoading)
                ) {
                    Text(
                        text = "${viewState.user.firstName} ${viewState.user.lastName}",
                        fontSize = 24.sp,
                        style = MaterialTheme.typography.subtitle2
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .align(Alignment.CenterVertically),
                        text = viewState.user.userTag,
                        style = KodeTypography.Meta,
                        fontSize = 17.sp
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(12.dp)
                        .placeholder(visible = viewState.isLoading),
                    text = viewState.user.position,
                    style = KodeTypography.Subtitle
                )
            }
        }
    }
}

@Composable
fun DetailsBody(modifier: Modifier = Modifier, viewState: DetailsViewState) {
    Column(
        modifier = modifier
    ) {
        val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        DetailsItem(
            modifier = Modifier
                .padding(vertical = 26.dp)
                .placeholder(visible = viewState.isLoading),
            painter = painterResource(id = R.drawable.ic_star),
            text = simpleDateFormat.format(viewState.user.birthday)
        ) {
            Text(
                text = LocalContext.current.resources.getQuantityString(
                    R.plurals.ages,
                    viewState.user.age,
                    viewState.user.age
                ),
                style = KodeTypography.Detail
            )
        }
        Divider(thickness = 0.5.dp)
        val context = LocalContext.current
        DetailsItem(
            modifier = Modifier
                .clickable {
                    if (!viewState.isLoading) {
                        context.startActivity(
                            Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:${viewState.user.phone}"))
                        )
                    }
                }
                .padding(vertical = 26.dp)
                .placeholder(visible = viewState.isLoading),

            painter = painterResource(id = R.drawable.ic_phone),
            text = PhoneNumberUtils.formatNumber(viewState.user.phone, Locale.getDefault().country)
        )
    }
}

private val User.age
    get() = ((Date().time - this.birthday.time) * 3.17E-11).roundToInt()

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
private fun DetailsScreenLoadingPreview() {
    DetailsScreen(DetailsViewState(), {})
}

@Preview
@Composable
private fun DetailsScreen() {
    DetailsScreen(DetailsViewState(isLoading = false), {})
}
