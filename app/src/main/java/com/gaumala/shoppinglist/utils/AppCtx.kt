package com.gaumala.shoppinglist.utils

import android.content.Context

interface AppCtx {
    fun getLocalizedString(resId: Int): String

    class Default(ctx: Context): AppCtx {
        private val appCtx = ctx.applicationContext

        override fun getLocalizedString(resId: Int): String {
            return appCtx.getString(resId)
        }

    }
}