package com.yenaly.yenaly_libs.base.settings

import android.os.Bundle
import android.util.Log
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.XmlRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.yenaly.yenaly_libs.utils.unsafeLazy
import java.lang.reflect.ParameterizedType

/**
 * @ProjectName : YenalyModule
 * @Author : Yenaly Liew
 * @Time : 2022/04/17 017 19:26
 * @Description : Description...
 */
abstract class YenalySettingsFragment(@XmlRes private val xmlRes: Int) :
    PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(xmlRes, rootKey)
        initPreferencesVariable()
        onPreferencesCreated(savedInstanceState)
        bindDataObservers()
    }

    /**
     * 用于绑定数据观察器 (optional)
     */
    open fun bindDataObservers() {
    }

    /**
     * 在此处使用[findPreference]初始化设置中的变量
     */
    open fun initPreferencesVariable() = Unit

    /**
     * 界面与xml设置列表绑定后从此处进行view操作
     */
    abstract fun onPreferencesCreated(savedInstanceState: Bundle?)

    @Suppress("unchecked_cast")
    private fun <VM : ViewModel> createViewModel(
        fragment: Fragment,
        viewModelStoreOwner: ViewModelStoreOwner,
        factory: ViewModelProvider.Factory? = null,
    ): VM {
        val vmClass =
            (fragment.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>
        Log.d("vmClass", vmClass.toString())
        return if (factory != null) {
            ViewModelProvider(viewModelStoreOwner, factory)[vmClass]
        } else {
            ViewModelProvider(viewModelStoreOwner)[vmClass]
        }
    }

    /**
     * 快速獲得隸屬於某[key]的Preference，可以爲null
     */
    fun <T : Preference> preference(key: String) = unsafeLazy { findPreference<T>(key) }

    /**
     * 快速獲得隸屬於某[key]的Preference，不可以爲null
     */
    fun <T : Preference> safePreference(key: String) = unsafeLazy {
        checkNotNull(findPreference<T>(key)) {
            "The preference belonged to the key \"$key\" is null."
        }
    }

    override fun setDivider(divider: Drawable?) {
        super.setDivider(ColorDrawable(Color.TRANSPARENT))
    }

    override fun setDividerHeight(height: Int) {
        super.setDividerHeight(0)
    }
}
