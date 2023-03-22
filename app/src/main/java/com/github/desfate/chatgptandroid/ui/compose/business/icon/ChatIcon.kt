package com.github.desfate.chatgptandroid.ui.compose.business.icon

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.github.desfate.chatgptandroid.R
import com.github.desfate.chatgptandroid.ui.compose.theme.MainTheme

@Composable
fun ChatIcon(
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    val semantics = if (contentDescription != null) {
        Modifier.semantics {
            this.contentDescription = contentDescription
            this.role = Role.Image
        }
    } else {
        Modifier
    }
    Box(modifier = modifier.then(semantics)) {
        Icon(
            painter = painterResource(id = R.drawable.ic_chat_icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primaryContainer
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun chatAppBarPreview() {
    MainTheme {
        ChatIcon("Preview!")
    }
}
