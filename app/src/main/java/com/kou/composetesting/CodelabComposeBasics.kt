package com.kou.composetesting

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kou.composetesting.ui.theme.ComposeTestingTheme

@Composable
fun MainUIComposeBasics() {
    ComposeTestingTheme {
        var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }

        if (shouldShowOnboarding)
            OnboardingScreen (onContinueClicked = { shouldShowOnboarding = false } )
        else
            Conversation(SampleData.conversationSample)
    }
}

@Composable
fun OnboardingScreen(
    onContinueClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to Compose testing app!")
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = onContinueClicked
        ) {
            Text("Continue")
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 320,
    heightDp = 320
)
@Composable
fun OnboardingPreview() {
    ComposeTestingTheme {
        OnboardingScreen( onContinueClicked = {} )
    }
}

data class Message(
    val author: String,
    val body: String
)

@Composable
fun MessageCard(msg: Message) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(R.drawable.kou_avatar),
            contentDescription = "Kou avatar picture",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        var isExpanded by remember { mutableStateOf(false) }
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
            animationSpec = repeatable(
                iterations = 2,
                animation = tween()
            )
        )

        Column(
            modifier = Modifier.clickable { isExpanded = !isExpanded }
        ) {
            Text(
                text = msg.author,
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.subtitle2
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                elevation = 1.dp,
                color = surfaceColor,
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }

}

@Composable
fun Conversation(messages: List<Message>) {
    LazyColumn {
        messages.map { item { MessageCard(it) } }
    }
}

/**
 * for the correct color of the dark theme background,
 * use DayNight theme in themes.xml
 */
@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun MessageCardPreview() {
    ComposeTestingTheme {
        MessageCard(Message(
            "Android",
            "Jetpack composable"
        ))
    }
}

@Preview
@Composable
fun ConversationPreview() {
    ComposeTestingTheme {
        Conversation(SampleData.conversationSample)
    }
}