package com.lokile.applibraries.base

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.viewbinding.ViewBinding
import com.lokile.applibraries.managers.RxBus
import com.lokile.applibraries.managers.RxBusListener
import com.lokile.applibraries.utils.checkAppPermission
import com.lokile.applibraries.utils.handleException

abstract class AppBaseActivity : AppCompatActivity(), IView, RxBusListener {
    private val permissionRequestCallBack =
        HashMap<Int, (result: Boolean, isPermanentDenied: Boolean) -> Unit>()
    override val uuid: Int by lazy { RxBus.INSTANCE.newUUID() }
    internal var viewBindingProvider: ((LayoutInflater) -> ViewBinding)? = null
    private var permissionRequestCode = 1

    fun requestPermission(
        permissionList: List<String>,
        callback: (result: Boolean, isPermanentDenied: Boolean) -> Unit
    ) {
        if (!checkAppPermission(permissionList) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val requestCode = permissionRequestCode++
            ActivityCompat.requestPermissions(
                this, permissionList.toTypedArray(), requestCode
            )
            permissionRequestCallBack[requestCode] = callback
        } else {
            callback.invoke(true, false)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionRequestCallBack[requestCode]?.invoke(
            grantResults.all { it == PackageManager.PERMISSION_GRANTED },
            Build.VERSION.SDK_INT < Build.VERSION_CODES.M || permissions
                .any { shouldShowRequestPermissionRationale(it) }
        )
        permissionRequestCallBack.remove(requestCode)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = viewBindingProvider?.invoke(layoutInflater)?.root
        if (view != null) {
            setContentView(view)
        }
        try {
            setupView(savedInstanceState)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    fun hideStatusBar() {
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            controller.hide(WindowInsetsCompat.Type.statusBars())
        }
    }

    fun showStatusBar() {
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            controller.show(WindowInsetsCompat.Type.statusBars())
        }
    }

    fun showNavigationBar() {
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            controller.show(WindowInsetsCompat.Type.navigationBars())
        }
    }

    fun hideNavigationBar() {
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            controller.hide(WindowInsetsCompat.Type.navigationBars())
        }
    }

    inline fun <reified T> registerEventListener(crossinline callBack: (T) -> Unit) {
        RxBus.INSTANCE.register<T>(this, callBack)
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.INSTANCE.unregister(this)
    }
}