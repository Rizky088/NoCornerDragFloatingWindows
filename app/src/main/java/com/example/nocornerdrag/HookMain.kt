package com.example.nocornerdrag

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.lang.reflect.Method

class HookMain : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != "com.android.systemui") return

        val targetClass = XposedHelpers.findClassIfExists(
            "com.android.wm.shell.multitasking.miuiinfinitymode.MiuiInfinityModeThermalArea",
            lpparam.classLoader
        ) ?: return

        XposedBridge.hookAllMethods(
            targetClass,
            "triggerFullscreenGesture",
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val returnType = (param.method as? Method)?.returnType
                    param.result = if (returnType == Boolean::class.javaPrimitiveType) false else null
                }
            }
        )
    }
}
