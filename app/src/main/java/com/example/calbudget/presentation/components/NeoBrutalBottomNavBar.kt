package com.example.calbudget.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.calbudget.core.navigation.BottomNavItem
import com.example.calbudget.core.theme.NeoBrutal
import com.example.calbudget.core.theme.NeoBrutalBlack
import com.example.calbudget.core.theme.NeoBrutalYellow

@Composable
fun NeoBrutalBottomNavBar(
    items: List<BottomNavItem>,
    currentDestination: NavDestination?,
    onItemClick: (BottomNavItem) -> Unit
) {
    // Bottom nav pakai border atas tebal — Neo Brutalism
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = NeoBrutal.BorderWidth,
                color = NeoBrutalBlack,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any {
                it.route == item.route
            } == true

            NeoBrutalNavItem(
                item = item,
                isSelected = isSelected,
                onClick = { onItemClick(item) }
            )
        }
    }
}

@Composable
private fun NeoBrutalNavItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) NeoBrutalYellow
    else MaterialTheme.colorScheme.surface

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(NeoBrutal.RadiusMedium))
            .background(backgroundColor)
            .then(
                if (isSelected) Modifier.border(
                    width = 2.dp,
                    color = NeoBrutalBlack,
                    shape = RoundedCornerShape(NeoBrutal.RadiusMedium)
                ) else Modifier
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
            contentDescription = item.title,
            tint = NeoBrutalBlack,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = item.title,
            style = MaterialTheme.typography.labelSmall,
            color = NeoBrutalBlack
        )
    }
}