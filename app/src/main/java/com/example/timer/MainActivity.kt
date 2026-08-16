package com.example.timer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                TimerScreen()
            }
        }
    }
}

@Composable
fun TimerScreen() {
    val timer = remember { TimerState() }
    // 用 Compose state 驱动 phase，确保状态变化触发重组、按钮 enabled 更新。
    var phase by remember { mutableStateOf(TimerState.Phase.READY) }
    var displayMillis by remember { mutableLongStateOf(0L) }

    // 恢复状态后，若处于 RUNNING 则持续刷新显示。
    LaunchedEffect(phase) {
        while (phase == TimerState.Phase.RUNNING) {
            delay(200L)
            displayMillis = timer.currentMillis(System.currentTimeMillis())
        }
        if (phase != TimerState.Phase.RUNNING) {
            displayMillis = timer.currentMillis(System.currentTimeMillis())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "计时器",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = TimerState.formatMillis(displayMillis),
            fontSize = 64.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "已计时",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(32.dp))
        Row {
            Button(
                onClick = {
                    timer.start(System.currentTimeMillis())
                    phase = timer.phase
                    displayMillis = timer.currentMillis(System.currentTimeMillis())
                },
                enabled = phase != TimerState.Phase.RUNNING
            ) {
                Text("开始")
            }
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedButton(
                onClick = {
                    timer.pause(System.currentTimeMillis())
                    phase = timer.phase
                    displayMillis = timer.currentMillis(System.currentTimeMillis())
                },
                enabled = phase == TimerState.Phase.RUNNING
            ) {
                Text("暂停")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = {
                timer.reset()
                phase = timer.phase
                displayMillis = 0L
            }
        ) {
            Text("重置")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimerScreenPreview() {
    MaterialTheme {
        TimerScreen()
    }
}
