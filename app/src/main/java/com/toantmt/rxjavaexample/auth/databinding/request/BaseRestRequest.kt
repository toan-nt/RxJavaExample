package com.toantmt.rxjavaexample.auth.databinding.request
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

import com.toantmt.rxjavaexample.BR

abstract class BaseRestRequest : BaseObservable() {

    val isRequestValid: Boolean
        @Bindable get() {
            return validates()
        }

    protected abstract fun validates(): Boolean

    protected fun notifyRequireValidation() {
        this.notifyPropertyChanged(BR.requestValid)
    }
}