package com.n9mtq4.botserver

import com.n9mtq4.botserver.world.World
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter

/**
 * Created by will on 12/8/15 at 10:00 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class GameWriter(val world: World, val file: File) {
	
	val printWriter: PrintWriter
	val bufferedWriter: BufferedWriter
	
	init {
		
		this.bufferedWriter = BufferedWriter(FileWriter(file, true))
		this.printWriter = PrintWriter(bufferedWriter)
		
	}
	
	fun tick() {
		
		printWriter.println(world.toGraphicsString()) // append the data to the buffer
//		TODO: append shooting information
		printWriter.flush() // write the data
		
	}
	
}
