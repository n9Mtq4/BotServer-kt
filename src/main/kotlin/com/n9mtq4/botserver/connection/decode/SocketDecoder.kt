package com.n9mtq4.botserver.connection.decode

import com.n9mtq4.botserver.bot.Bot
import com.n9mtq4.botserver.world.World

/**
 * Created by will on 12/9/15 at 11:16 AM.
 * 
 * An interface that can be implemented to add support for
 * decoding the responses from a client.
 * 
 * @author Will "n9Mtq4" Bresnahan
 */
interface SocketDecoder {
	
	/**
	 * Receives a string and is supposed to take that input and
	 * parse it and apply the actions to the [bot] or [world].
	 * 
	 * @param line the line that the client has sent
	 * @param bot the current bot being processed
	 * @param world the [World]
	 * */
	fun decodeTurnLine(line: String, bot: Bot, world: World)
	
	/**
	 * Receives a string and is supposed to return if the
	 * end command has been received from the client yet.
	 * 
	 * @param line the line that the client has sent
	 * @param bot the current bot being processed.
	 * @param world the [World]
	 * @return if the parsing of the client should continue
	 * */
	fun shouldContinue(line: String, bot: Bot, world: World): Boolean
	
}
