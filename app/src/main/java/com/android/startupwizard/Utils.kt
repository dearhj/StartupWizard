package com.android.startupwizard

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.ScanResult
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.widget.Toast
import java.io.BufferedReader
import java.io.InputStreamReader


fun getDisplayName(localName: String): String {
    return when (localName) {
        "ca-ES" -> "Català (Espanya)"
        "cs-CZ" -> "Čeština (Česko)"
        "da-DK" -> "Dansk (Danmark)"
        "de-DE" -> "Deutsch (Deutschland)"
        "de-AT" -> "Deutsch (Österreich)"
        "et-EE" -> "Eesti (Eesti)"
        "en-GB" -> "English (United Kingdom)"
        "en-US" -> "English (United States)"
        "es-ES" -> "Español (España)"
        "es-US" -> "Español (Estados Unidos)"
        "fi_PH" -> "Filipino (Pilipinas)"
        "fr-FR" -> "Français (France)"
        "hr-HR" -> "Hrvatski (Hrvatska)"
        "id-ID" -> "Indonesia (Indonesia)"
        "it-IT" -> "Italiano (Italia)"
        "lv-LV" -> "Latviešu (Latvija)"
        "lt-LT" -> "Lietuvių (Lietuva)"
        "hu-HU" -> "Magyar (Magyarország)"
        "ms-MY" -> "Melayu (Malaysia)"
        "nl-NL" -> "Nederlands (Nederland)"
        "nb-NO" -> "Norsk bokmål (Norge)"
        "pl-PL" -> "Polski (Polska)"
        "pt-BR" -> "Português (Brasil)"
        "pt-PT" -> "Português (Portugal)"
        "ro-RO" -> "Română (România)"
        "sk-SK" -> "Slovenčina (Slovensko)"
        "sl-SI" -> "Slovenščina (Slovenija)"
        "fi-FI" -> "Suomi (Suomi)"
        "sv-SE" -> "Svenska (Sverige)"
        "vi-VN" -> "Tiếng Việt (Việt Nam)"
        "tr-TR" -> "Türkçe (Türkiye)"
        "el-GR" -> "Ελληνικά (Ελλάδα)"
        "bg-BG" -> "Български (България)"
        "ru-RU" -> "Pусский (Россия)"
        "sr-RS" -> "Cрпски (Србија)"
        "uk-UA" -> "Українська (Україна)"
        "hy-AM" -> "հայերեն (Հայաստան)"
        "he-IL" -> "עברית (ישראל)"
        "ar-EG" -> "العربية (مصر)"
        "fa-IR" -> "فارسی (ایران)"
        "hi-IN" -> "हिन्दी (भारत)"
        "th-TH" -> "ไทย (ไทย)"
        "my-MM" -> "ြန်မာ (မြန်မာ)"
        "km-KH" -> "ខ្មែរ (កម្ពុជា)"
        "ko-KR" -> "한국어 (대한민국)"
        "zh-CN" -> "中文 (简体)"
        "zh-TW" -> "中文 (繁體)"
        "zh-HK" -> "中文 (香港)"
        "ja-JP" -> "日本語 (日本)"
        else -> "中文 (简体)"
    }
}

fun startScanWifi(manager: WifiManager?) {
    manager?.startScan()
}


/**
 * 获取wifi列表
 */
@SuppressLint("MissingPermission")
fun getWifiList(mWifiManager: WifiManager): List<ScanResult> {
    return mWifiManager.scanResults
}

/**
 * 获取当前wifi名字
 * @return
 */
fun getWiFiName(manager: WifiManager): String {
    val wifiInfo = manager.connectionInfo
    return wifiInfo.ssid
}


/**
 * 是否开启wifi，没有的话打开wifi
 */
fun openWifi(mWifiManager: WifiManager?): Boolean {
    var bRet = true
    if (mWifiManager?.isWifiEnabled == false) {
        bRet = mWifiManager.setWifiEnabled(true)
    }
    return bRet
}

var toast: Toast? = null
fun toast(context: Context, msg: String) {
    if (toast != null) toast?.cancel()
    toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
    toast?.show()
}


@SuppressLint("WifiManagerLeak", "MissingPermission", "NewApi")
fun connectWifi(wifiManager: WifiManager?, wifiName: String, password: String, type: String?) {
    //断开之前连接
    forgetNetwork(wifiManager)
    // 1、注意热点和密码均包含引号，此处需要需要转义引号
    val ssid = "\"" + wifiName + "\""
    val psd = "\"" + password + "\""

    //2、配置wifi信息
    val conf = WifiConfiguration()
    conf.SSID = ssid
    when (type) {
        "WEP" -> {
            conf.wepKeys[0] = psd
            conf.wepTxKeyIndex = 0
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40)
        }

        "WPA" -> conf.preSharedKey = psd
        else -> conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)
    }
    //3、链接wifi
    wifiManager?.addNetwork(conf)
    val list = wifiManager?.configuredNetworks
    if (list != null) {
        for (i in list) {
            if (i.SSID != null && i.SSID == ssid) {
                wifiManager.enableNetwork(i.networkId, true)
                wifiManager.reconnect()
                break
            }
        }
    }
}

@SuppressLint("MissingPermission")
fun forgetNetwork(wifiManager: WifiManager?) {
    val configs = wifiManager?.configuredNetworks
    if (configs != null) {
        for (config in configs) {
            wifiManager.removeNetwork(config.networkId)
            wifiManager.saveConfiguration()
        }
    }
}

fun canPing(): Boolean {
    return try {
        val command = arrayOf("ping", "-c", "1", "-W", "1", "www.baidu.com")
        val process = Runtime.getRuntime().exec(command)
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val output = StringBuilder()
        var read: Int
        val buffer = CharArray(4096)
        while (reader.read(buffer).also { read = it } > 0) {
            output.appendRange(buffer, 0, read)
        }
        val exitVal = process.waitFor()
        exitVal == 0
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}