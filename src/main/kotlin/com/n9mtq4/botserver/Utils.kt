package com.n9mtq4.botserver

import com.n9mtq4.kotlin.extlib.math.toDegrees
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
 * An assert that will print a warning message, but wont
 * crash the program.
 * 
 * @param bool the boolean expression to evaluate
 * @param msg the message to print if [bool] fails
 * @return if the calling method should stop
 * */
fun safeAssert(bool: Boolean, msg: String): Boolean {
	if (!bool) println(msg)
	return !bool
}

/**
 * Puts the angle to a -179 to 180 range.
 * http://stackoverflow.com/a/2321136/5196460
 * 
 * @param angle any angle in degrees
 * @return the angle in -179 to 180 range
 * */
fun normalizeAngle(angle: Double): Double {
	var newAngle = angle
	while (newAngle <= -180) newAngle += 360
	while (newAngle > 180) newAngle -= 360
	return newAngle
}

fun calculateAnge(x: Int, y: Int, x1: Int, y1: Int) = calculateAnge(x.toDouble(), y.toDouble(), x1.toDouble(), y1.toDouble())
fun calculateAnge(x: Double, y: Double, x1: Double, y1: Double) = Math.atan2((y1 - y), (x1 - x)).toDegrees()
