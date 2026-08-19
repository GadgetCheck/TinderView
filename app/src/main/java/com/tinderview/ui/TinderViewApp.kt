package com.tinderview.ui

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
        when (destinations[selected]) {
            Dest.Connect -> ConnectScreen(
                modifier = Modifier.padding(padding),
                onLiked = { profile ->
                    if (liked.none { it.id == profile.id }) liked.add(0, profile)
                },
            )
            Dest.Discover -> DiscoverScreen(modifier = Modifier.padding(padding))
            Dest.Chat -> ChatScreen(
                modifier = Modifier.padding(padding),
                liked = liked,
            )
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
