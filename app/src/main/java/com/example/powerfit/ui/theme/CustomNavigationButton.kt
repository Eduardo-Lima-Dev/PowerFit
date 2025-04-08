package com.example.powerfit.ui.theme

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

;

@Composable
fun CustomNavigationButton(
    text: String,
    navRoute: String,
    navController: NavController,
    paddingTop: Dp = 0.dp,
    icon: Int? = null,
    iconSize: Dp = 24.dp,
    clickable: Boolean = true
) {
    Button(

        onClick = { if (clickable) {navController.navigate(navRoute)} else null },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = paddingTop)
    ) {
        if (icon != null) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(iconSize)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(text = text)
    }
}

enum class IconPosition {
    Start, End
}