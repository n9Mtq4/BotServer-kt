package com.n9mtq4.botserver

/**
 * Created by will on 12/8/15 at 4:42 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
fun Int.toRadians() = Math.toRadians(this.toDouble())
fun Double.toRadians() = Math.toRadians(this)
fun Double.toDegrees() = Math.toDegrees(this)
fun safeAssert(bool: Boolean, msg: String): Boolean {
	println(msg)
	return bool
}
