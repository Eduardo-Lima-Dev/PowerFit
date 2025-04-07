package com.example.powerfit.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

;

@Composable
fun CustomNavigationButton(
    text: String,
    navRoute: String,
    navController: NavController,
    paddingTop: Dp,
    icon: Int,
    iconSize: Dp,
    iconPosition: IconPosition = IconPosition.Start
) {
    Button(
        onClick = { navController.navigate(navRoute) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = paddingTop)
    ) {
        if (iconPosition == IconPosition.Start) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(iconSize)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(text = text)
        if (iconPosition == IconPosition.End) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}

enum class IconPosition {
    Start, End
}