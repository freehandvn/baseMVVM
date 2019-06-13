package com.freehand.logger

fun logd(tag:String,message:String){
    Logger.log().d(tag,message)
}
fun loge(tag:String,error:Throwable){
    Logger.log().e(tag,error)
}

fun trace(tag:String){
    Logger.log().trace(tag)
}