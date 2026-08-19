package com.tinderview.ui.connect

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tinderview.cardstack.CardStack
import com.tinderview.cardstack.SwipeDirection
import com.tinderview.cardstack.rememberCardStackState
import com.tinderview.data.Profile
import com.tinderview.data.SampleProfiles
import com.tinderview.ui.theme.Ink
import com.tinderview.ui.theme.InstrumentSerif
import com.tinderview.ui.theme.PlusJakarta
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
        delay(1100)
        ready = true
    }

    Box(modifier.fillMaxSize().background(Ink.Night)) {
        if (!ready) {
            PulseLoader()
        }

        AnimatedVisibility(
            visible = ready,
            enter = fadeIn() + scaleIn(initialScale = 0.975f),
            exit = fadeOut(),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Wordmark()
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .widthIn(max = 480.dp)
                        .padding(horizontal = 16.dp, vertical = 6.dp),
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
                        .padding(horizontal = 28.dp)
                        .padding(bottom = 16.dp, top = 8.dp),
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
            containerColor = Ink.Raised,
            contentColor = Ink.Cream,
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp, vertical = 8.dp),
            ) {
                ProfileCard(
                    profile = profile,
                    showBio = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                )
                Text(
                    text = profile.name,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(top = 20.dp),
                )
                Text(
                    text = "${profile.age}  ·  ${profile.city}",
                    color = Ink.CreamMuted,
                    fontFamily = PlusJakarta,
                    modifier = Modifier.padding(top = 4.dp),
                )
                Row(Modifier.padding(top = 14.dp)) {
                    profile.interests.forEach { interest ->
                        InfoChip(interest)
                        Spacer(Modifier.width(8.dp))
                    }
                }
                Text(
                    text = profile.bio,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Ink.Cream.copy(alpha = 0.9f),
                    modifier = Modifier.padding(top = 16.dp),
                )
                TextButton(
                    onClick = { selected = null },
                    modifier = Modifier.align(Alignment.End),
                ) {
                    Text("Close", color = Ink.Coral, fontFamily = PlusJakarta, fontWeight = FontWeight.SemiBold)
                }
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun Wordmark() {
    Row(
        modifier = Modifier.padding(top = 6.dp, bottom = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .padding(end = 8.dp)
                .size(7.dp)
                .clip(CircleShape)
                .background(Ink.Coral),
        )
        Text(
            text = "tinderview",
            color = Ink.Cream.copy(alpha = 0.72f),
            fontFamily = PlusJakarta,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            letterSpacing = 3.2.sp,
        )
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
        Text(
            text = "That's everyone",
            fontFamily = InstrumentSerif,
            fontStyle = FontStyle.Italic,
            fontSize = 36.sp,
            color = Ink.Cream,
        )
        Text(
            text = "Rewind the last card, or see who's nearby.",
            modifier = Modifier.padding(top = 10.dp, bottom = 18.dp),
            color = Ink.CreamMuted,
            fontFamily = PlusJakarta,
        )
        if (canRewind) {
            TextButton(onClick = onRewind) {
                Text("Rewind last swipe", color = Ink.Coral, fontFamily = PlusJakarta, fontWeight = FontWeight.SemiBold)
            }
        }
        Spacer(Modifier.weight(1f))
    }
}
