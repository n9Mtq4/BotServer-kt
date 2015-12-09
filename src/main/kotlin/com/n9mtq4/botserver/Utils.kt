package com.n9mtq4.botserver

/**
 * Created by will on 12/8/15 at 4:42 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
fun Int.toRadians() = Math.toRadians(this.toDouble())
fun Double.toRadians() = Math.toRadians(this)
fun Double.toDegrees() = Math.toDegrees(this)

/**
 * An assert that will print a warning message, but wont
 * crash the program.
 * 
 * @param bool the boolean expression to evaluate
 * @param msg the message to print if [bool] fails
 * @return if the calling method should stop
 * */
fun safeAssert(bool: Boolean, msg: String): Boolean {
	println(msg)
	return bool
}
