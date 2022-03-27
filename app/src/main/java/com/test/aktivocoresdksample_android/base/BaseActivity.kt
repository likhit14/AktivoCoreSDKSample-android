package com.test.aktivocoresdksample_android.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aktivolabs.aktivocore.data.repositories.LocalRepository
import com.aktivolabs.aktivocore.managers.AktivoManager
import com.test.aktivocoresdksample_android.utils.MappingUtils
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity : AppCompatActivity() {

    val mappingUtils by lazy { MappingUtils() }
    val aktivoManager by lazy { AktivoManager(this) }
    val localRepository by lazy { LocalRepository(this) }
    val compositeDisposable by lazy { CompositeDisposable() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    abstract fun showText(text:String)

}