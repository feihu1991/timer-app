# 计时器（timer-app）

一个「简单工具类 App」流水线示例：用 **Kotlin + Jetpack Compose** 实现的极简计时器，配齐了从编写到 CI 自动测试的完整链路。

## 项目结构

```
timer-app/
├── app/
│   ├── src/main/java/com/example/timer/
│   │   ├── TimerState.kt      # 纯 Kotlin 状态机（可单元测试）
│   │   └── MainActivity.kt    # Compose UI 入口
│   ├── src/test/              # 单元测试（JUnit4）
│   ├── src/androidTest/       # Compose UI 测试（instrumented）
│   └── build.gradle.kts
├── .maestro/                  # Maestro 端到端测试 YAML
├── .github/workflows/ci.yml   # GitHub Actions CI 流水线
└── 验收清单.md
```

## CI 流水线做什么

GitHub Actions（`ci.yml`）在云端自动完成：

1. **build_and_test**：构建 → 单元测试 → Lint → 打包 debug APK
2. **instrumented_test**：模拟器上跑 Compose UI 测试
3. **maestro_test**：模拟器上跑 Maestro 端到端流程

运行产出的 **debug APK**、测试报告、截图都会作为 artifact 上传，可直接下载安装到真机做最后人工验收。

## 本地运行（可选）

需要 JDK 17 + Android SDK：

```bash
./gradlew assembleDebug         # 构建 APK
./gradlew testDebugUnitTest     # 跑单元测试
```

APK 输出在 `app/build/outputs/apk/debug/app-debug.apk`。

## 人工验收

构建全绿后，按根目录的 `验收清单.md` 在真机上点一遍。其中「动画/手感」这类主观项只能人判断，是流水线里唯一必须人工的部分。
