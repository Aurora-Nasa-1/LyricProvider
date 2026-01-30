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

1. **获取插件**：[前往 Releases 页面](https://github.com/proify/LyricProvider/releases) 下载 `manager.apk`。
2. **启动 API**：部署并启动 [网易云 API](./api)。
3. **配置与登录**：打开 `Lyricon Manager` App，配置 API 地址并完成扫码登录。
4. **激活模块**：在 **LSPosed** 管理器中勾选 `Lyricon Manager` 并启用。
5. **设置作用域**：作用域应包含你想获取歌词的所有音乐 App。
6. **即刻生效**：重启对应的音乐 App 即可。

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