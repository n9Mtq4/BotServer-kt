package com.n9mtq4.botserver

import java.util.Random

/**
 * Created by will on 12/8/15 at 4:42 PM.
 * 
 * Contains basic / misc utils. eg:
 * radian and degree conversions
 * and an assert that wont crash anything
 * 
 * @author Will "n9Mtq4" Bresnahan
 */

/**
 * A global random variable.
 * Generated numbers are more random
 * if you use the same java.util.Random instance.
 * */
val RANDOM = Random()

/**
 * Converts degrees into radians.
 * 
 * @return the radians
 * */
fun Int.toRadians() = Math.toRadians(this.toDouble())

/**
 * Converts degrees into radians.
 * 
 * @return the radians
 * */
fun Double.toRadians() = Math.toRadians(this)

/**
 * Converts radians to degrees.
 * 
 * @return the degrees
 * */
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
