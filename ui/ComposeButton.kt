package compose.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import compose.utils.OnSingleClickListener

enum class TypeButtonState {
    ENABLED, DISABLED
}

@Composable
fun ButtonDefault(
    modifier: Modifier = Modifier,
    buttonPadding: PaddingValues = ButtonDefaults.ContentPadding,
    arrangementTextAndIcon: Arrangement.Horizontal = Arrangement.Center,
    text: String? = null,
    textStyle: TextStyle,
    textOverflow: TextOverflow = TextOverflow.Clip,
    textMaxLines: Int = Int.MAX_VALUE,
    textColor: Color = Color.White,
    iconStart: Int? = null,
    iconEnd: Int? = null,
    iconPadding: Dp = 0.dp,
    state: TypeButtonState = TypeButtonState.ENABLED,
    colorBackground: Color = Color.Black,
    clickInterval: Int = 600,
    onClick: () -> Unit
) {
    val singleClickListener by remember {
        mutableStateOf(
            OnSingleClickListener(
                interval = clickInterval,
                onSingleClick = onClick
            )
        )
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(color = colorBackground)
            .height(IntrinsicSize.Min)
            .width(IntrinsicSize.Max)
            .clickable(role = Role.Button,
                onClick = {
                    if (state == TypeButtonState.ENABLED) {
                        singleClickListener()
                    }
                })
            .padding(buttonPadding),
        horizontalArrangement = arrangementTextAndIcon,
    ) {
        iconStart?.also {
            Icon(
                painter = painterResource(id = it),
                contentDescription = null,
                tint = textColor,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            )
            Spacer(modifier = Modifier.width(iconPadding))
        }
        text?.also {
            Text(
                text = it,
                color = textColor,
                style = textStyle,
                overflow = textOverflow,
                maxLines = textMaxLines,
                modifier = if (textOverflow != TextOverflow.Clip) Modifier.weight(1f) else Modifier.wrapContentWidth()
            )
        }
        iconEnd?.also {
            Spacer(modifier = Modifier.width(iconPadding))
            Icon(
                painter = painterResource(id = it),
                contentDescription = null,
                tint = textColor,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            )
        }
    }
}

@Composable
fun ButtonSpinner(
    modifier: Modifier = Modifier,
    buttonPadding: PaddingValues = PaddingValues(0.dp),
    selectedValue: Pair<String, String>? = null,
    textStyle: TextStyle = TypeTypography.p2.style,
    list: List<Pair<String, String>>,
    state: TypeButtonState = TypeButtonState.ENABLED,
    colorText: Color = Color.Black,
    colorBackground: Color = Color.White,
    onClick: (Pair<String, String>) -> Unit
) {
    if (list.isEmpty()) return
    var dropDownWidth by remember { mutableStateOf(0) }
    var expanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        ButtonDefault(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged {
                    dropDownWidth = it.width
                },
            buttonPadding = buttonPadding,
            arrangementTextAndIcon = Arrangement.SpaceBetween,
            text = selectedValue?.second,
            textStyle = textStyle,
            textOverflow = TextOverflow.Ellipsis,
            textMaxLines = 1,
            iconPadding = 8.dp,
            iconEnd = R.drawable.ic_dropdown,
            state = state,
            color = color,
        ) {
            expanded = !expanded
        }
        DropdownMenu(
            modifier = Modifier
                .width(with(LocalDensity.current) { dropDownWidth.toDp() })
                .background(color = colorBackground),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            list.forEach {
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onClick.invoke(it)
                    }
                ) {
                    Text(text = it.second, color = colorText, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}