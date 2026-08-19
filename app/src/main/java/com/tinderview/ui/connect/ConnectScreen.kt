package com.tinderview.ui.connect

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tinderview.cardstack.CardStack
import com.tinderview.cardstack.SwipeDirection
import com.tinderview.cardstack.rememberCardStackState
import com.tinderview.data.Profile
import com.tinderview.data.SampleProfiles
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectScreen(
    onLiked: (Profile) -> Unit,
    modifier: Modifier = Modifier,
) {
    val profiles = remember { SampleProfiles }
    val state = rememberCardStackState { profiles.size }
    val scope = rememberCoroutineScope()
    var ready by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf<Profile?>(null) }
    var match by remember { mutableStateOf<Profile?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(Unit) {
        delay(1200)
        ready = true
    }

    Box(modifier.fillMaxSize()) {
        if (!ready) {
            PulseLoader()
        }

        AnimatedVisibility(
            visible = ready,
            enter = fadeIn() + scaleIn(initialScale = 0.96f),
            exit = fadeOut(),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .widthIn(max = 480.dp)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                ) {
                    if (state.currentIndex >= profiles.size) {
                        EmptyDeck(
                            canRewind = state.canRewind,
                            onRewind = { scope.launch { state.rewind() } },
                        )
                    } else {
                        CardStack(
                            items = profiles,
                            key = { it.id },
                            state = state,
                            modifier = Modifier.fillMaxSize(),
                            onSwiped = { profile, direction ->
                                if (direction != SwipeDirection.Left) {
                                    onLiked(profile)
                                    if (profile.isMatch && direction == SwipeDirection.Right) {
                                        match = profile
                                    }
                                }
                            },
                            onTopCardClick = { selected = it },
                        ) { profile ->
                            ProfileCard(profile)
                        }
                    }
                }
                GlassActionBar(
                    canRewind = state.canRewind,
                    canSwipe = state.canSwipe,
                    onRewind = { scope.launch { state.rewind() } },
                    onPass = { scope.launch { state.swipe(SwipeDirection.Left) } },
                    onSuperLike = { scope.launch { state.swipe(SwipeDirection.Up) } },
                    onLike = { scope.launch { state.swipe(SwipeDirection.Right) } },
                    modifier = Modifier
                        .padding(bottom = 20.dp, top = 4.dp)
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                )
            }
        }

        match?.let { profile ->
            MatchBurst(profile = profile, onDismiss = { match = null })
        }
    }

    selected?.let { profile ->
        ModalBottomSheet(
            onDismissRequest = { selected = null },
            sheetState = sheetState,
        ) {
            Column(Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
                ProfileCard(
                    profile = profile,
                    modifier = Modifier.height(360.dp),
                )
                Spacer(Modifier.height(16.dp))
                Text(profile.bio, style = MaterialTheme.typography.bodyLarge)
                TextButton(
                    onClick = { selected = null },
                    modifier = Modifier.align(Alignment.End),
                ) { Text("Close") }
            }
        }
    }
}

@Composable
private fun EmptyDeck(
    canRewind: Boolean,
    onRewind: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.weight(1f))
        Text("You're all caught up", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
        Text(
            "Rewind the last card or check Discover.",
            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
        )
        if (canRewind) {
            TextButton(onClick = onRewind) { Text("Rewind last swipe") }
        }
        Spacer(Modifier.weight(1f))
    }
}
