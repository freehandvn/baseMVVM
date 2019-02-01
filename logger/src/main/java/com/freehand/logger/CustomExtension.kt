package com.freehand.logger

fun log(tag:String,message:String){
    Logger.log().d(tag,message)
}

fun trace(tag:String){
    Logger.log().trace(tag)
}