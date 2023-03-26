package com.github.desfate.chatgptandroid.ui.compose.business.conversation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.blankj.utilcode.util.TimeUtils
import com.github.desfate.chatgptandroid.R
import com.github.desfate.chatgptandroid.ui.compose.business.ChatViewModel
import com.github.desfate.chatgptandroid.ui.compose.business.FunctionalityNotAvailablePopup
import com.github.desfate.chatgptandroid.ui.compose.business.appbar.ChatAppBar
import com.github.desfate.chatgptandroid.ui.compose.theme.MainTheme
import kotlinx.coroutines.launch
import java.util.*

/**
 * 用户聊天区域
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationContent(
    uiState: ConversationUiState,  //           关联的数据状态
    navigateToProfile: (String) -> Unit,  //    点击行为导致的navigate跳转
    modifier: Modifier = Modifier, //           装饰器
    onNavIconPressed: () -> Unit = { }, //      按钮点击事件
    type: String                        //      聊天类型
) {
    val authorName = stringResource(id = R.string.me_default)
    val currentTime = TimeUtils.getSafeDateFormat("HH:mm:ss").format(Date(System.currentTimeMillis()))

    val scrollState = rememberLazyListState()
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)

    val model: ChatViewModel = viewModel()
    val scope = rememberCoroutineScope()

    Scaffold(  // 使用脚手架
        topBar = {
            ChannelNameBar(
                channelName = uiState.channelName,
                channelMembers = uiState.channelMembers,
                onNavIconPressed = onNavIconPressed,
                scrollBehavior = scrollBehavior,
            )
        },
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.ime),
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
            Messages(
                messages = uiState.messages,
                navigateToProfile = navigateToProfile,
                modifier = Modifier.weight(1f),
                scrollState = scrollState
            )
            UserInput(
                onMessageSent = { content ->
                    model.requestChat(content, type)
                    uiState.addMessage(
                        Message(authorName, content, currentTime)
                    )
                },
                resetScroll = {
                    scope.launch {
                        scrollState.scrollToItem(0)
                    }
                },
                // let this element handle the padding so that the elevation is shown behind the
                // navigation bar
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding()
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelNameBar(
    channelName: String,
    channelMembers: Int,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavIconPressed: () -> Unit = { }
) {
    var functionalityNotAvailablePopupShown by remember { mutableStateOf(false) }
    if (functionalityNotAvailablePopupShown) {
        FunctionalityNotAvailablePopup { functionalityNotAvailablePopupShown = false }
    }
    ChatAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        onNavIconPressed = onNavIconPressed,
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Channel name
                Text(
                    text = channelName,
                    style = MaterialTheme.typography.titleMedium
                )
                // Number of members
                Text(
                    text = stringResource(R.string.me_default, channelMembers),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        actions = {
            // Search icon
            Icon(
                imageVector = Icons.Outlined.Search,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .clickable(onClick = { functionalityNotAvailablePopupShown = true })
                    .padding(horizontal = 12.dp, vertical = 16.dp)
                    .height(24.dp),
                contentDescription = stringResource(id = R.string.me_default)
            )
            // Info icon
            Icon(
                imageVector = Icons.Outlined.Info,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .clickable(onClick = { functionalityNotAvailablePopupShown = true })
                    .padding(horizontal = 12.dp, vertical = 16.dp)
                    .height(24.dp),
                contentDescription = stringResource(id = R.string.me_default)
            )
        }
    )
}

const val ConversationTestTag = "ConversationTestTag"
private val JumpToBottomThreshold = 56.dp

@Composable
fun Messages(
    messages: List<Message>,
    navigateToProfile: (String) -> Unit,
    scrollState: LazyListState,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    Box(modifier = modifier) {

        val authorMe = stringResource(id = R.string.me_default)
        LazyColumn(
            reverseLayout = true,
            state = scrollState,
            modifier = Modifier
                .testTag(ConversationTestTag)
                .fillMaxSize()
        ) {
            for (index in messages.indices) {
                val prevAuthor = messages.getOrNull(index - 1)?.author
                val nextAuthor = messages.getOrNull(index + 1)?.author
                val content = messages[index]
                val isFirstMessageByAuthor = prevAuthor != content.author
                val isLastMessageByAuthor = nextAuthor != content.author

                // Hardcode day dividers for simplicity
                if (index == messages.size - 1) {
                    item {
                        DayHeader("20 Aug")
                    }
                } else if (index == 2) {
                    item {
                        DayHeader("Today")
                    }
                }

                item {
                    Message(
                        onAuthorClick = { name -> navigateToProfile(name) },
                        msg = content,
                        isUserMe = content.author == authorMe,
                        isFirstMessageByAuthor = isFirstMessageByAuthor,
                        isLastMessageByAuthor = isLastMessageByAuthor
                    )
                }
            }
        }
        // Jump to bottom button shows up when user scrolls past a threshold.
        // Convert to pixels:
        val jumpThreshold = with(LocalDensity.current) {
            JumpToBottomThreshold.toPx()
        }

        // Show the button if the first visible item is not the first one or if the offset is
        // greater than the threshold.
        val jumpToBottomButtonEnabled by remember {
            derivedStateOf {
                scrollState.firstVisibleItemIndex != 0 ||
                        scrollState.firstVisibleItemScrollOffset > jumpThreshold
            }
        }

        JumpToBottom(
            // Only show if the scroller is not at the bottom
            enabled = jumpToBottomButtonEnabled,
            onClicked = {
                scope.launch {
                    scrollState.animateScrollToItem(0)
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun Message(
    onAuthorClick: (String) -> Unit,
    msg: Message,
    isUserMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean
) {
    val borderColor = if (isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.tertiary
    }

    val spaceBetweenAuthors = if (isLastMessageByAuthor) Modifier.padding(top = 8.dp) else Modifier
    Row(modifier = spaceBetweenAuthors) {
        if (isLastMessageByAuthor) {
            // Avatar
            Image(
                modifier = Modifier
                    .clickable(onClick = { onAuthorClick(msg.author) })
                    .padding(horizontal = 16.dp)
                    .size(42.dp)
                    .border(1.5.dp, borderColor, CircleShape)
                    .border(3.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    .clip(CircleShape)
                    .align(Alignment.Top),
                painter = painterResource(id = msg.authorImage),
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
        } else {
            // Space under avatar
            Spacer(modifier = Modifier.width(74.dp))
        }
        AuthorAndTextMessage(
            msg = msg,
            isUserMe = isUserMe,
            isFirstMessageByAuthor = isFirstMessageByAuthor,
            isLastMessageByAuthor = isLastMessageByAuthor,
            authorClicked = onAuthorClick,
            modifier = Modifier
                .padding(end = 16.dp)
                .weight(1f)
        )
    }
}

@Composable
fun DayHeader(dayString: String) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .height(16.dp)
    ) {
        DayHeaderLine()
        Text(
            text = dayString,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        DayHeaderLine()
    }
}

@Composable
private fun RowScope.DayHeaderLine() {
    Divider(
        modifier = Modifier
            .weight(1f)
            .align(Alignment.CenterVertically),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    )
}

@Composable
fun AuthorAndTextMessage(
    msg: Message,
    isUserMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
    authorClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        if (isLastMessageByAuthor) {
            AuthorNameTimestamp(msg)
        }
        ChatItemBubble(msg, isUserMe, authorClicked = authorClicked)
        if (isFirstMessageByAuthor) {
            // Last bubble before next author
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            // Between bubbles
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun AuthorNameTimestamp(msg: Message) {
    // Combine author and timestamp for a11y.
    Row(modifier = Modifier.semantics(mergeDescendants = true) {}) {
        Text(
            text = msg.author,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .alignBy(LastBaseline)
                .paddingFrom(LastBaseline, after = 8.dp) // Space to 1st bubble
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = msg.timestamp,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.alignBy(LastBaseline),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private val ChatBubbleShape = RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)

@Composable
fun ChatItemBubble(
    message: Message,
    isUserMe: Boolean,
    authorClicked: (String) -> Unit
) {

    val backgroundBubbleColor = if (isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Column {
        Surface(
            color = backgroundBubbleColor,
            shape = ChatBubbleShape
        ) {
            ClickableMessage(
                message = message,
                isUserMe = isUserMe,
                authorClicked = authorClicked
            )
        }

        if (message.imageSrc.isNotEmpty()){
            Spacer(modifier = Modifier.height(4.dp))
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(message.imageSrc)
                    .placeholder(R.mipmap.placeholder)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(160.dp).clip(RoundedCornerShape(15.dp)),
            )
        }

        message.image?.let {
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                color = backgroundBubbleColor,
                shape = ChatBubbleShape
            ) {
                Image(
                    painter = painterResource(it),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(160.dp),
                    contentDescription = stringResource(id = R.string.attached_image)
                )
            }
        }
    }
}

@Composable
fun ClickableMessage(
    message: Message,
    isUserMe: Boolean,
    authorClicked: (String) -> Unit
) {
    val uriHandler = LocalUriHandler.current

    val styledMessage = messageFormatter(
        text = message.content,
        primary = isUserMe
    )

    ClickableText(
        text = styledMessage,
        style = MaterialTheme.typography.bodyLarge.copy(color = LocalContentColor.current),
        modifier = Modifier.padding(16.dp),
        onClick = {
            styledMessage
                .getStringAnnotations(start = it, end = it)
                .firstOrNull()
                ?.let { annotation ->
                    when (annotation.tag) {
                        SymbolAnnotationType.LINK.name -> uriHandler.openUri(annotation.item)
                        SymbolAnnotationType.PERSON.name -> authorClicked(annotation.item)
                        else -> Unit
                    }
                }
        }
    )
}

private val initialMessages = listOf(
    Message(
        "me",
        "Check it out!",
        "8:07 PM"
    ),
    Message(
        "me",
        "Thank you!",
        "8:06 PM"
    ),
    Message(
        "Taylor Brooks",
        "You can use all the same stuff",
        "8:05 PM"
    ),
    Message(
        "Taylor Brooks",
        "@aliconors Take a look at the `Flow.collectAsStateWithLifecycle()` APIs",
        "8:05 PM"
    ),
    Message(
        "John Glenn",
        "Compose newbie as well, have you looked at the JetNews sample? Most blog posts end up " +
                "out of date pretty fast but this sample is always up to date and deals with async " +
                "data loading (it's faked but the same idea applies) \uD83D\uDC49" +
                "https://github.com/android/compose-samples/tree/master/JetNews",
        "8:04 PM"
    ),
    Message(
        "me",
        "Compose newbie: I’ve scourged the internet for tutorials about async data loading " +
                "but haven’t found any good ones. What’s the recommended way to load async " +
                "data and emit composable widgets?",
        "8:03 PM"
    )
)
val exampleUiState = ConversationUiState(
    initialMessages = initialMessages,
    channelName = "#composers",
    channelMembers = 42
)

@Preview
@Composable
fun ConversationPreview() {
    MainTheme() {
        ConversationContent(
            uiState = exampleUiState,
            navigateToProfile = { },
            type = "text"
        )
    }
}

//@Preview
//@Composable
//fun ChannelBarPrev() {
//    MainTheme {
//        ChannelNameBar(channelName = "composers", channelMembers = 52)
//    }
//}

@Preview
@Composable
fun DayHeaderPrev() {
    DayHeader("Aug 6")
}
