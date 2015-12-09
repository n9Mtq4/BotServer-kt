package com.n9mtq4.botserver

/**
 * Created by will on 12/7/15 at 10:55 AM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
fun main(args: Array<String>) {
	
	val game = Game()
	println("Server started")
	
	game.start()
	println("Match started")
	
	println("Running Game")
	game.run()
	
}
