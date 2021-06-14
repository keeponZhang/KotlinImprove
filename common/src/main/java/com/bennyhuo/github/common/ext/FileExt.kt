package com.bennyhuo.github.common.ext

import android.util.Log
import java.io.File

private const val TAG = "FileExt"

/**
 * Created by benny on 6/20/17.
 */
fun File.ensureDir(): Boolean {
    try {
        //这个也比较简单，不是文件夹的话，如果是文件的话就删除，否则就创建。
        isDirectory.no {
            isFile.yes {
                delete()
            }
            return mkdirs()
        }
    } catch (e: Exception) {
        Log.w(TAG, e.message)
    }
    return false
}