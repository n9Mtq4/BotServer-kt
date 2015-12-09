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
	 * A [SocketDecoder] that can decode infomation from the kotlin client.
	 * 
	 * @see SocketDecoder
	 * */
	object KotlinClient : SocketDecoder {
		/**@see [SocketDecoder.decodeTurnLine]*/
		override fun decodeTurnLine(line: String, bot: Bot, world: World) {
			throw UnsupportedOperationException()
		}
		
		/**@see [SocketDecoder.shouldContinue]*/
		override fun shouldContinue(line: String, bot: Bot, world: World): Boolean {
			return !line.contains("END")
		}
	}
	
}
