package com.toantmt.rxjavaexample.auth.databinding.request

class LoginRequest: BaseRestRequest() {

    var username: String = "toantmt1990@gmail.com"
        set(value) {
            field = value; notifyRequireValidation()
        }

    var password: String = "8883"
        set(value) {
            field = value; notifyRequireValidation()
        }

    override fun validates(): Boolean {
        return username.isNotEmpty() && password.isNotEmpty()
    }
}