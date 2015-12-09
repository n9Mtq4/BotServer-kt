package com.n9mtq4.botserver.connection.encode

import com.n9mtq4.botserver.Team
import com.n9mtq4.botserver.bot.Bot
import com.n9mtq4.botserver.world.World

/**
 * Created by will on 12/9/15 at 9:49 AM.
 * 
 * An interface that can be implemented for
 * adding encoding support to a client.
 * 
 * @author Will "n9Mtq4" Bresnahan
 */
interface SocketEncoder {
	
	/**
	 * @return The command to send when the game has started
	 * */
	fun encodeStartGame(): String
	
	/**
	 * @return The command to send when the turn has started
	 * */
	fun encodeStartTurn(): String
	
	/**
	 * @return The command to send when the has ended.
	 * */
	@Deprecated("Not yet implemented with clients. Being held back by the ruby api.")
	fun encodeEndTurn(): String
	
	/**
	 * Encodes the [bot] into a string.
	 * Also given the [World] and the current [Team].
	 * 
	 * @param bot the bot to encode
	 * @param world the [World]
	 * @param team the [Team]
	 * @return the [bot] into a client passable string
	 * */
	fun encodeBotData(bot: Bot, world: World, team: Team): String
	
}
