package com.jkantech.jadonotes.ui.utils

import com.jkantech.jadonotes.R

class NoteColorPicker {
    companion object {
        fun NoteColor(id: Int): Int {

            var color = R.id._1
            when (id) {
                R.id._1 -> {
                    color = R.color.one
                }
                R.id._2 -> {
                    color = R.color.two
                }
                R.id._3 -> {
                    color = R.color.three
                }
                R.id._4 -> {
                    color = R.color.four
                }

            R.id._5 -> {
                    color = R.color.five
                }
            }
            return color
        }
    }
}