package com.tinderview.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tinderview.data.Profile
import com.tinderview.ui.theme.Ink

@Composable
fun ChatScreen(
    liked: List<Profile>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .fillMaxSize()
            .background(Ink.Night),
    ) {
        Text(
            text = "Messages",
            style = MaterialTheme.typography.headlineMedium,
            color = Ink.Cream,
            modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 18.dp),
        )
        Text(
            text = if (liked.isEmpty()) "Likes become conversations" else "${liked.size} open threads",
            color = Ink.CreamMuted,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 4.dp, bottom = 10.dp),
        )
        if (liked.isEmpty()) {
            Text(
                text = "Like someone on Connect to start a conversation.",
                color = Ink.CreamMuted,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 22.dp, vertical = 12.dp),
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(liked, key = { it.id }) { profile ->
                    val brush = remember(profile.id) { Brush.linearGradient(profile.gradient) }
                    val shape = RoundedCornerShape(20.dp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape)
                            .background(Ink.Raised)
                            .border(1.dp, Ink.Hairline, shape)
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(brush)
                                .border(
                                    width = if (profile.isMatch) 1.5.dp else 0.dp,
                                    color = if (profile.isMatch) Ink.Coral else Color.Transparent,
                                    shape = CircleShape,
                                ),
                        )
                        Column(Modifier.padding(start = 14.dp).weight(1f)) {
                            Text(
                                text = profile.name,
                                style = MaterialTheme.typography.titleMedium,
                                color = Ink.Cream,
                            )
                            Text(
                                text = if (profile.isMatch) {
                                    "You matched · say hello"
                                } else {
                                    "You liked ${profile.name}"
                                },
                                color = Ink.CreamMuted,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(top = 2.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}
