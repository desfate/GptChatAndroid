package com.github.desfate.chatgptandroid.ui.compose.business

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.github.desfate.chatgptandroid.R
import com.github.desfate.chatgptandroid.ui.compose.business.conversation.ConversationContent
import com.github.desfate.chatgptandroid.ui.compose.theme.MainTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * compose实现ui
 */
@AndroidEntryPoint
class ChatFragment : Fragment() {

    private val chatViewModel: ChatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(inflater.context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        setContent {
            MainTheme{  // 自定义主题
                ConversationContent(
                    uiState = chatViewModel.exampleUiState,
                    navigateToProfile = { user ->
                        // Click callback
//                        val bundle = bundleOf("userId" to user)
//                        findNavController().navigate(
//                            R.id.nav_view,
//                            bundle
//                        )
                    },
                    onNavIconPressed = {
                        chatViewModel.openDrawer()
                    }
                )
            }
        }
    }

}