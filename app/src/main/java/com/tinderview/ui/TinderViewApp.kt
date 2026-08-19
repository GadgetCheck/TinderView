package com.tinderview.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.zIndex
import com.tinderview.data.Profile
import com.tinderview.ui.chat.ChatScreen
import com.tinderview.ui.connect.ConnectScreen
import com.tinderview.ui.discover.DiscoverScreen

private enum class Dest(val label: String) {
    Connect("Connect"),
    Discover("Discover"),
    Chat("Chat"),
}

@Composable
fun TinderViewApp() {
    var selected by remember { mutableIntStateOf(0) }
    val liked = remember { mutableStateListOf<Profile>() }
    val destinations = Dest.entries

    Scaffold(
        bottomBar = {
            NavigationBar {
                destinations.forEachIndexed { index, dest ->
                    NavigationBarItem(
                        selected = selected == index,
                        onClick = { selected = index },
                        icon = { DestinationGlyph(dest) },
                        label = { Text(dest.label) },
                    )
                }
            }
        },
    ) { padding ->
        val dest = destinations[selected]
        Box(
            Modifier
                .padding(padding)
                .fillMaxSize(),
        ) {
            // Keep Connect composed across tabs so the deck, rewind
            // history, and pulse intro survive a Discover/Chat peek.
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
private fun DestinationGlyph(dest: Dest) {
    Text(
        text = when (dest) {
            Dest.Connect -> "✦"
            Dest.Discover -> "▦"
            Dest.Chat -> "✉"
        },
    )
}
