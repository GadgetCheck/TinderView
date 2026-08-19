package com.tinderview.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.tinderview.data.Profile
import com.tinderview.ui.chat.ChatScreen
import com.tinderview.ui.connect.ConnectScreen
import com.tinderview.ui.discover.DiscoverScreen
import com.tinderview.ui.theme.CardsIcon
import com.tinderview.ui.theme.ChatIcon
import com.tinderview.ui.theme.Ink
import com.tinderview.ui.theme.SearchIcon

private enum class Dest(val label: String) {
    Connect("Connect"),
    Discover("Explore"),
    Chat("Chat"),
}

@Composable
fun TinderViewApp() {
    var selected by remember { mutableIntStateOf(0) }
    val liked = remember { mutableStateListOf<Profile>() }
    val destinations = Dest.entries

    Scaffold(
        containerColor = Ink.Night,
        bottomBar = {
            AppNavigationBar(
                selected = selected,
                destinations = destinations,
                onSelect = { selected = it },
            )
        },
    ) { padding ->
        val dest = destinations[selected]
        Box(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Ink.Night),
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .zIndex(if (dest == Dest.Connect) 1f else 0f)
                    .graphicsLayer { alpha = if (dest == Dest.Connect) 1f else 0f }
                    .then(if (dest != Dest.Connect) Modifier.clearAndSetSemantics { } else Modifier),
            ) {
                ConnectScreen(
                    onLiked = { profile ->
                        if (liked.none { it.id == profile.id }) liked.add(0, profile)
                    },
                )
            }
            if (dest == Dest.Discover) {
                DiscoverScreen(modifier = Modifier.fillMaxSize())
            }
            if (dest == Dest.Chat) {
                ChatScreen(modifier = Modifier.fillMaxSize(), liked = liked)
            }
        }
    }
}

@Composable
private fun AppNavigationBar(
    selected: Int,
    destinations: List<Dest>,
    onSelect: (Int) -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Ink.Ink)
            .navigationBarsPadding(),
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Ink.Hairline),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            destinations.forEachIndexed { index, dest ->
                val active = selected == index
                val tint = if (active) Ink.Coral else Ink.CreamMuted
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = remember(index) { MutableInteractionSource() },
                            indication = null,
                            onClick = { onSelect(index) },
                        )
                        .semantics { this.selected = active }
                        .padding(vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    when (dest) {
                        Dest.Connect -> CardsIcon(tint, size = 20.dp)
                        Dest.Discover -> SearchIcon(tint, size = 20.dp)
                        Dest.Chat -> ChatIcon(tint, size = 20.dp)
                    }
                    Text(
                        text = dest.label,
                        color = tint,
                        style = androidx.compose.material3.MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }
        }
    }
}
