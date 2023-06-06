package com.khaled.weather.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchText(searchState: MutableState<TextFieldValue>, searchFn: (String) -> Unit) {
    TextField(
        value = searchState.value,
        label = { Text("Enter City") },
        onValueChange = { value ->
            searchState.value = value
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(1.dp, Color.Black, CircleShape),
        textStyle = TextStyle(color = Color.Black, fontSize = 18.sp, background = Color.White),

        leadingIcon = {
            if (searchState.value != TextFieldValue("")) {
                Column {
                    IconButton(
                        onClick = {
                            searchState.value =
                                TextFieldValue("")
                        },
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(15.dp)
                                .size(24.dp),
                        )
                    }
                }
            }
        },

        trailingIcon = {
            if (searchState.value != TextFieldValue("")) {
                Column {
                    IconButton(
                        onClick = {
                            searchFn(searchState.value.text)
                        },
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "",
                            tint = Color.Black,
                            modifier = Modifier
                                .padding(15.dp)
                                .size(24.dp),

                        )
                    }
                }
            }
        },
        singleLine = true,
        shape = CircleShape,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            cursorColor = Color.Black,
            leadingIconColor = Color.Black,
            trailingIconColor = Color.Black,
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
    )
}
