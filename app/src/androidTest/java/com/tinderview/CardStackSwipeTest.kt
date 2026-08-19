package com.tinderview

import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tinderview.cardstack.CardStack
import com.tinderview.cardstack.CardStackState
import com.tinderview.cardstack.SwipeDirection
import com.tinderview.cardstack.rememberCardStackState
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CardStackSwipeTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun swipeRightAdvancesCurrentIndex() {
        val holder = arrayOfNulls<CardStackState>(1)
        composeRule.setContent {
            val items = listOf("a", "b", "c")
            val state = rememberCardStackState { items.size }
            holder[0] = state
            CardStack(
                items = items,
                key = { it },
                state = state,
            ) { label ->
                Text(label)
            }
        }
        composeRule.onNodeWithContentDescription("Top card").assertExists()
        composeRule.runOnIdle {
            runBlocking { holder[0]!!.swipe(SwipeDirection.Right) }
        }
        composeRule.waitForIdle()
        assertEquals(1, holder[0]!!.currentIndex)
    }
}
