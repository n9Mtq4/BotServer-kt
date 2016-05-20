@file:JvmName("BotServer")

package com.n9mtq4.botserver

import com.n9mtq4.botserver.graphics.GraphicsDisplay

/**
 * Created by will on 12/7/15 at 10:55 AM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
fun main(args: Array<String>) {
	
	val game = if (args.contains("live")) Game(GraphicsDisplay()) else Game()
	
	println("Server started")
	
	println("Starting Game")
	game.run()
	
}
