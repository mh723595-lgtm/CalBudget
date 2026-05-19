package com.example.calbudget.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.calbudget.core.theme.*

@Composable
fun NeoBrutalTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    errorMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLines: Int = 1,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val borderColor = if (errorMessage != null) NeoBrutalRed else NeoBrutalBlack

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Label
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = NeoBrutalBlack
        )

        // TextField dengan Neo Brutal style
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = NeoBrutal.BorderWidth,
                    color = borderColor,
                    shape = RoundedCornerShape(NeoBrutal.RadiusSmall)
                ),
            placeholder = {
                if (placeholder.isNotEmpty()) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyMedium,
                        color = NeoBrutalBlack.copy(alpha = 0.4f)
                    )
                }
            },
            trailingIcon = trailingIcon,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            maxLines = maxLines,
            isError = errorMessage != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,  // border sudah kita handle sendiri
                unfocusedBorderColor = Color.Transparent,
                errorBorderColor = Color.Transparent,
                focusedContainerColor = NeoBrutalWhite,
                unfocusedContainerColor = NeoBrutalWhite,
                focusedTextColor = NeoBrutalBlack,
                unfocusedTextColor = NeoBrutalBlack
            ),
            shape = RoundedCornerShape(NeoBrutal.RadiusSmall)
        )

        // Error message
        if (errorMessage != null) {
            Text(
                text = "⚠ $errorMessage",
                style = MaterialTheme.typography.bodySmall,
                color = NeoBrutalRed
            )
        }
    }
}