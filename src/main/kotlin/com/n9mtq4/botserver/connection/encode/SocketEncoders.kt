package com.n9mtq4.botserver.connection.encode

import com.n9mtq4.botserver.API_LEVEL
import com.n9mtq4.botserver.Team
import com.n9mtq4.botserver.bot.Bot
import com.n9mtq4.botserver.world.World
import com.n9mtq4.botserver.world.objects.interfaces.Entity
import com.n9mtq4.botserver.world.objects.interfaces.HealthWorldObject
import org.json.simple.JSONArray
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
			
			val json = JSONObject() // the json text
			
//			data for the bot
			json.put("x", bot.x)
			json.put("y", bot.y)
			json.put("angle", bot.angle)
			json.put("health", bot.health)
			json.put("ap", bot.actionPoints)
			json.put("mana", team.mana)
			
			val vision = JSONArray() // array of things we can see
			
//			go through every thing we can see
			bot.generateVision().forEach { 
				
//				make an object for it
				val visionObj = JSONObject()
				
//				SeenWorldObject data
				visionObj.put("type", it.obj.javaClass.simpleName.toUpperCase()) // type
				if (it.obj is Bot) visionObj.put("team", it.obj.id) // team
				visionObj.put("x", it.x) // x
				visionObj.put("y", it.y) // y
				if (it.obj is Entity) visionObj.put("angle", it.obj.angle) // angle
				if (it.obj is HealthWorldObject) visionObj.put("health", it.obj.health) // health
				
				vision.add(visionObj) // add it to the things we can see
				
			}
			
//			add the vision
			json.put("vision", vision)
			
//			turn it into a string
			return json.toJSONString()
			
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
