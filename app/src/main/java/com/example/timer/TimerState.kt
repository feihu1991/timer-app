package com.example.timer

/**
 * 计时器状态机（纯 Kotlin，无 Android 依赖，便于单元测试）。
 *
 * 状态：
 *  - READY  初始/已重置，未开始计时
 *  - RUNNING 正在计时
 *  - PAUSED  已暂停
 */
class TimerState {

    enum class Phase { READY, RUNNING, PAUSED }

    var phase: Phase = Phase.READY
        private set

    /** 已累计的毫秒数（暂停时冻结，恢复后继续累加）。 */
    var elapsedMillis: Long = 0L
        private set

    /** 内部：本次 RUNNING 段开始时的基准时间戳。 */
    private var runningStart: Long = 0L

    /** 开始计时。仅当处于 READY 或 PAUSED 状态时有效。 */
    fun start(nowMillis: Long) {
        if (phase == Phase.RUNNING) return
        runningStart = nowMillis
        phase = Phase.RUNNING
    }

    /** 暂停计时，冻结当前累计值。 */
    fun pause(nowMillis: Long) {
        if (phase != Phase.RUNNING) return
        elapsedMillis += nowMillis - runningStart
        phase = Phase.PAUSED
    }

    /** 重置计时器。 */
    fun reset() {
        phase = Phase.READY
        elapsedMillis = 0L
        runningStart = 0L
    }

    /** 返回当前显示用的总毫秒数（RUNNING 时包含本段未冻结的增量）。 */
    fun currentMillis(nowMillis: Long): Long {
        return if (phase == Phase.RUNNING) {
            elapsedMillis + (nowMillis - runningStart)
        } else {
            elapsedMillis
        }
    }

    companion object {
        /** 将毫秒格式化为 "MM:SS" 或 "H:MM:SS"。 */
        fun formatMillis(millis: Long): String {
            val totalSeconds = millis / 1000
            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60
            val seconds = totalSeconds % 60
            return if (hours > 0) {
                String.format("%d:%02d:%02d", hours, minutes, seconds)
            } else {
                String.format("%02d:%02d", minutes, seconds)
            }
        }
    }
}
