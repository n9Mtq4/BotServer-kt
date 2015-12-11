package com.n9mtq4.botserver.connection.decode

import com.n9mtq4.botserver.bot.Bot
import com.n9mtq4.botserver.world.World

/**
 * Created by will on 12/9/15 at 11:18 AM.
 * 
 * Contains some default implementations of [SocketDecoder]
 * 
 * @see SocketDecoder
 * @author Will "n9Mtq4" Bresnahan
 */
sealed class SocketDecoders {
	
	/**
	 * A [SocketDecoder] that can decode information from the ruby client.
	 * 
	 * @see SocketDecoder
	 * */
	object RubyClient : SocketDecoder {
		/**@see [SocketDecoder.decodeTurnLine]*/
		override fun decodeTurnLine(line: String, bot: Bot, world: World) {
			
			try {
				
				if (line.toLowerCase().contains("move")) {
//					moving
//					parse
					val tokens = line.split(" ")
					val dx = tokens[1].toInt()
					val dy = tokens[2].toInt()
//					move
					bot.move(dx, dy)
				}else if (line.toLowerCase().contains("turn")) {
//					turning
//					parse
					val tokens = line.split(" ")
					val angle = tokens[1].toInt()
//					turn
					bot.turn(angle)
				}else if (line.equals("shoot", true)) {
//					shooting
					bot.shoot()
				}else if (line.toLowerCase().contains("place")) {
//					placing a block
//					parse
					val tokens = line.split(" ")
					val dx = tokens[1].toInt()
					val dy = tokens[2].toInt()
//					place
					bot.place(dx, dy)
				}else if (line.equals("spawn", true)) {
//					TODO: spawn a new bot
				}
				
			}catch (e: Exception) {
//				we don't want a bad request to crash anything do we now?
				println("WARNING: malformed turn action: '$line'")
			}
			
		}
		
		/**@see [SocketDecoder.shouldContinue]*/
		override fun shouldContinue(line: String): Boolean {
			return !line.contains("END")
		}
	}
	
}
