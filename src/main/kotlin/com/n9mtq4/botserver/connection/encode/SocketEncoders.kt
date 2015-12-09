package com.n9mtq4.botserver.connection.encode

import com.n9mtq4.botserver.API_LEVEL
import com.n9mtq4.botserver.Team
import com.n9mtq4.botserver.bot.Bot
import com.n9mtq4.botserver.world.World
import org.json.simple.JSONObject

/**
 * Created by will on 12/9/15 at 9:50 AM.
 * 
 * Includes some default implementations of [SocketEncoder].
 * Has support for the Ruby client and the Kotlin client.
 * 
 * @author Will "n9Mtq4" Bresnahan
 */
sealed class SocketEncoders {
	
	/**
	 * The BaseClient for the socket encoding.
	 * Has the basic support and is extended by
	 * both [RubyClient] and [KotlinClient].
	 * 
	 * @see RubyClient
	 * @see KotlinClient
	 * */
	open class BaseClient : SocketEncoder {
		/**
		 * @return "START"
		 * @see SocketEncoder.encodeStartGame
		 * */
		override fun encodeStartGame() = "START"
		/**
		 * @return "START_TURN"
		 * @see SocketEncoder.encodeStartTurn
		 * */
		override fun encodeStartTurn() = "START_TURN"
		/**
		 * @return ""
		 * @see SocketEncoder.encodeEndTurn
		 * */
		override fun encodeEndTurn() = ""
		/**
		 * @return a json representation of the bot and what it can see.
		 * @see SocketEncoder.encodeBotData
		 * */
		override fun encodeBotData(bot: Bot, world: World, team: Team): String {
			
//			TODO: encode into JSON
			val json = JSONObject()
			
			throw UnsupportedOperationException("NYI")
			
		}
		
	}
	
	/**
	 * An implementation of [BaseClient] that
	 * supports the ruby api.
	 * 
	 * @see BaseClient
	 * */
	object RubyClient : BaseClient() {
		
	}
	
	/**
	 * An implementation of [BaseClient] that
	 * supports the kotlin api.
	 * 
	 * @see BaseClient
	 * */
	object KotlinClient : BaseClient() {
		/**
		 * @return "START API $API_LEVEL"
		 * @see BaseClient.encodeStartGame
		 * @see SocketEncoder.encodeStartGame
		 * */
		override fun encodeStartGame() = "START API $API_LEVEL"
	}
	
}
