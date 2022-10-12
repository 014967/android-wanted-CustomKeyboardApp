package com.preonboarding.customkeyboard.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.preonboarding.customkeyboard.presentation.theme.CustomKeyBoardTheme

/**
 * @Created by 김현국 2022/10/12
 */
@OptIn(ExperimentalTextApi::class)
@Composable
fun Tag(
    tagText: String
) {
    Row(
        modifier = Modifier
            .background(
                color = CustomKeyBoardTheme.color.allBtnGray,
                shape = RoundedCornerShape(18.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 10.3.dp, vertical = 4.dp),
            text = tagText,
            style = CustomKeyBoardTheme.typography.allBodyMid.copy(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            ),
            color = CustomKeyBoardTheme.color.allBodyGray
        )
    }
}

@Preview
@Composable
fun PreviewTag() {
    CustomKeyBoardTheme() {
        Tag("하이하이")
    }
}
