package com.bennyhuo.dsl.layout.v1

import android.content.Context
import android.view.View
import android.widget.LinearLayout

const val MATCH_PARENT = -1
const val WRAP_CONTENT = -2

 class _LinearLayout(context: Context): LinearLayout(context), DslViewParent<LinearLayout.LayoutParams> {
    //这个不是_LinearLayout的属性，只是拓展属性
    var <T: View> T.layoutWeight: Float
        set(value){
            lparams.weight = value
        }
       get(){
            return lparams.weight
        }



   public var <T: View> T.layoutGravity: Int
        set(value){
            lparams.gravity = value
        }
        get(){
            return lparams.gravity
        }
}