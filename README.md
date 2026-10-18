# 日期小工具 (1x1 Android Widget)

桌面 1x1 小工具,只顯示今天的「日」,字體自動放到最大,跨日由系統自動更新。

## 產生 APK

### 方法 A:Android Studio
1. File > Open,選擇這個資料夾,等 Gradle 同步完成。
2. Build > Build APK(s)。
3. APK 位置:`app/build/outputs/apk/debug/app-debug.apk`

### 方法 B:GitHub Actions(不用裝任何東西)
1. 把整個資料夾推到一個 GitHub repo。
2. 到 Actions 分頁,等 "Build APK" 跑完。
3. 從 Artifacts 下載 `DateWidget-apk`。

### 方法 C:命令列
已安裝 Android SDK 與 Gradle 的話:`gradle assembleDebug`

## 安裝與使用
1. 手機安裝 APK(需允許安裝未知來源應用程式)。
2. 在桌面長按空白處 > 小工具 > 找到「日期小工具」> 拖到桌面。

## 自訂
- 字體大小上限:`res/layout/widget_date.xml` 的 `autoSizeMaxTextSize`
- 背景/文字顏色:`res/drawable/widget_bg.xml` 與同一個 layout 的 `textColor`
- 想顯示月/日,把 `format24Hour` 與 `format12Hour` 改成 `M/d`
