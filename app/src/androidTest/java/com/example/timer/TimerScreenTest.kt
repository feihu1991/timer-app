package com.example.timer

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class TimerScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `初始时开始按钮可用_暂停按钮禁用`() {
        composeTestRule.setContent {
            TimerScreen()
        }
        composeTestRule.onNodeWithText("开始").assertIsDisplayed().assertIsEnabled()
        composeTestRule.onNodeWithText("暂停").assertIsDisplayed().assertIsNotEnabled()
        composeTestRule.onNodeWithText("重置").assertIsDisplayed()
    }

    @Test
    fun `点击开始后暂停按钮可用_开始按钮禁用`() {
        composeTestRule.setContent {
            TimerScreen()
        }
        composeTestRule.onNodeWithText("开始").performClick()
        composeTestRule.onNodeWithText("暂停").assertIsEnabled()
        composeTestRule.onNodeWithText("开始").assertIsNotEnabled()
    }
}
