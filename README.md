<!--suppress ALL -->
<h1 align="center">LyricProvider - 歌词提供器</h1>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-brightgreen?style=flat-square&logo=android&logoColor=white" alt="Android">
  <img src="https://img.shields.io/github/v/release/proify/LyricProvider?style=flat-square&color=blue" alt="release">
  <img src="https://img.shields.io/github/downloads/proify/LyricProvider/total?style=flat-square&color=orange" alt="downloads">
  <img src="https://img.shields.io/github/license/proify/LyricProvider?style=flat-square" alt="license">
  <img src="https://img.shields.io/github/last-commit/proify/lyricon?style=flat-square" alt="Last Commit">
</p>

<p align="center">
  <b>针对 <a href="https://github.com/proify/lyricon">词幕 (lyricon)</a> 开发的标准化歌词获取插件库</b>
</p>

---

## 🎵 支持平台

目前已适配以下音乐客户端的歌词抓取：

| 平台                                            | 状态      | 说明               |
|:----------------------------------------------|:--------|:-----------------|
| 🍎 **Apple Music**                            | 🟢 稳定   | 支持动态歌词/翻译        |
| ☁️ **网易云音乐 (Global)**                          | 🟢 稳定   | 全局支持，通过 API 获取     |
| **🎵[光锥音乐](https://coneplayer.trantor.ink/)** | 🟢稳定    | 由开发者适配           |
| 🎸 **更多平台**                                   | 🛠️ 开发中 | 欢迎提交 PR 适配更多 App |

---

## 📥 安装指南

> [!IMPORTANT]
> 本插件必须配合 **[词幕](https://github.com/proify/lyricon)** 主程序使用。

1.  **获取插件**：[前往 Releases 页面](https://github.com/proify/LyricProvider/releases) 下载 `manager.apk`。
2.  **启动 API 服务端**：
    *   本项目内置了网易云 API 子模块 (`api` 文件夹)。
    *   您可以在本地电脑、服务器或 Android 设备（如 Termux）上运行它。
    *   **启动步骤**：
        1. 进入 `api` 目录：`cd api`
        2. 安装依赖：`pnpm install` (或 `npm install`)
        3. 启动服务：`node app.js`
    *   默认服务地址通常为 `http://您的IP:3000`。
3.  **配置与登录**：
    *   打开 `Lyricon Manager` App。
    *   在 "Netease API Base URL" 中输入您的 API 地址，点击 "Test" 检查连接。
    *   点击 "Login via QR Code" 进行扫码登录（建议登录以获得更高质量的歌词和更好的稳定性）。
    *   根据需要选择歌词显示模式（原词/翻译/双语）。
4.  **激活 Xposed 模块**：
    *   在 **LSPosed** 管理器中找到 `Lyricon Manager` 并勾选**启用**。
    *   **设置作用域**：勾选您希望自动获取歌词的所有音乐播放器 App（如网易云、QQ音乐、Spotify、Youtube Music 等）。
5.  **即刻生效**：重启对应的音乐 App 即可。

### 💡 说明

*   **管理器与插件的关系**：`manager.apk` 既是配置界面（管理器），也是 Xposed 插件本身。它集成了全局歌词获取逻辑。
*   **无需后台运行**：完成配置和登录后，`Lyricon Manager` 应用程序**不需要**在后台运行。歌词抓取逻辑会由系统自动注入到您设置的作用域 App 中执行。
*   **全平台支持**：通过监听系统 `MediaSession`，该插件理论上支持所有能够向系统发送媒体信息的播放器。

---

## 🛠️ 开发者指南

如果你想为自己喜欢的音乐 App 开发插件，可以参考以下资源：

- **开发文档**
  ：参考 [开发文档](https://github.com/proify/lyricon/blob/master/lyric/bridge/provider/README.md)
- **示例代码**：参考项目中实现代码

---

## 🤝 贡献与反馈

<p align="left">
  <a href="https://github.com/proify/LyricProvider/issues">
    <img src="https://img.shields.io/github/issues/proify/LyricProvider?style=flat-square&logo=github" alt="Issues">
  </a>
</p>

### 贡献者

[![Contributors](https://contrib.rocks/image?repo=proify/LyricProvider)](https://github.com/proify/LyricProvider/graphs/contributors)

---

## 📊 统计与历史

### 访问统计

![Visitors](https://count.getloli.com/get/@proify_LyricProvider?theme=minecraft)

### Star 趋势

[![Star History Chart](https://api.star-history.com/svg?repos=proify/LyricProvider&type=Date)](https://star-history.com/#proify/LyricProvider&Date)