package com.example.luas.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.luas.R

sealed class MenuAction(
    @StringRes val label: Int,
    @DrawableRes val icon: Int
) {
    object Refresh : MenuAction(R.string.refresh, R.drawable.refresh)
}