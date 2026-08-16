package com.example.timer

import org.junit.Assert.assertEquals
import org.junit.Test

class TimerStateTest {

    @Test
    fun `初始状态为 READY 且计时为零`() {
        val timer = TimerState()
        assertEquals(TimerState.Phase.READY, timer.phase)
        assertEquals(0L, timer.elapsedMillis)
    }

    @Test
    fun `开始后进入 RUNNING 状态`() {
        val timer = TimerState()
        timer.start(0L)
        assertEquals(TimerState.Phase.RUNNING, timer.phase)
    }

    @Test
    fun `暂停会冻结累计时长`() {
        val timer = TimerState()
        timer.start(0L)
        timer.pause(5_000L)
        assertEquals(TimerState.Phase.PAUSED, timer.phase)
        assertEquals(5_000L, timer.elapsedMillis)
        assertEquals(5_000L, timer.currentMillis(10_000L))
    }

    @Test
    fun `暂停后恢复继续累加`() {
        val timer = TimerState()
        timer.start(0L)
        timer.pause(5_000L)
        timer.start(5_000L)
        val now = timer.currentMillis(8_000L)
        assertEquals(8_000L, now)
    }

    @Test
    fun `重置后回到 READY 且清零`() {
        val timer = TimerState()
        timer.start(0L)
        timer.pause(5_000L)
        timer.reset()
        assertEquals(TimerState.Phase.READY, timer.phase)
        assertEquals(0L, timer.elapsedMillis)
        assertEquals(0L, timer.currentMillis(10_000L))
    }

    @Test
    fun `READY 时直接暂停无效`() {
        val timer = TimerState()
        timer.pause(5_000L)
        assertEquals(TimerState.Phase.READY, timer.phase)
        assertEquals(0L, timer.elapsedMillis)
    }

    @Test
    fun `格式化毫秒为分秒`() {
        assertEquals("00:00", TimerState.formatMillis(0L))
        assertEquals("00:05", TimerState.formatMillis(5_000L))
        assertEquals("01:05", TimerState.formatMillis(65_000L))
        assertEquals("1:00:00", TimerState.formatMillis(3_600_000L))
    }
}
