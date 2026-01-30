package io.github.proify.lyricon.manager

import com.highcapable.yukihookapi.YukiHookAPI
import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit

@InjectYukiHookWithXposed(modulePackageName = "io.github.proify.lyricon.manager")
open class HookEntry : IYukiHookXposedInit {

    override fun onHook() {
        YukiHookAPI.encase {
            // Hook all apps to catch MediaSession metadata
            loadApp {
                GlobalHook.onHook(this)
            }
        }
    }

    override fun onInit() {
        super.onInit()
        YukiHookAPI.configs {
            debugLog {
                tag = "LyriconManager"
            }
        }
    }
}
