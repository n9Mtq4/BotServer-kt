package com.n9mtq4.botserver.connection

import com.n9mtq4.botserver.SERVER_VERSION
import com.n9mtq4.botserver.Team
import com.n9mtq4.botserver.bot.Bot
import com.n9mtq4.botserver.world.World
import com.n9mtq4.botserver.world.objects.interfaces.Entity
import com.n9mtq4.botserver.world.objects.interfaces.HealthWorldObject
import com.n9mtq4.kotlin.extlib.io.errPrintln
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.ServerSocket
import java.net.Socket

/**
 * Created by will on 12/7/15 at 11:05 AM.
 * 
 * A class that handles all connections to the client.
 * Proxies all requests through the encoders or decoders.
 * 
 * @property client the socket connected to the client
 * @property input the buffered input
 * @property output the buffered output
 * 
 * @see SocketEncoder
 * @see SocketDecoder
 * @author Will "n9Mtq4" Bresnahan
 */
class ClientConnection(val teamNumber: Int, val serverSocket: ServerSocket) : InputHandler, OutputHandler {
	
	companion object {
		private val MOVE_REGEX = "/^MOVE -?[01] -?[01]$/ig".toRegex()
		private val TURN_REGEX = """/^TURN -?(\d|[1-9]\d|[1-2]\d{2}|3[0-5]\d|360)$/ig""".toRegex() // https://github.com/dimka665/range-regex
		private val SHOOT_REGEX = "/^SHOOT$/ig".toRegex()
		private val PLACE_REGEX = "/^PLACE -?[01] -?[01]$/ig".toRegex()
		private val SPAWN_REGEX = "/^SPAWN -?[01] -?[01]$/ig".toRegex()
	}
	
	internal val client: Socket
	internal val input: BufferedReader
	internal val output: BufferedWriter
	
	/**
	 * Waits for a client to connect
	 * and creates the input and output buffered steams.
	 * Then sends the game start command
	 * */
	init {
		
		print("Waiting for team to connect...")
		this.client = serverSocket.accept()
		
		this.input = BufferedReader(InputStreamReader(client.inputStream))
		this.output = BufferedWriter(OutputStreamWriter(client.outputStream))
		
		val clientCompliance = input.readLine()
		if (clientCompliance != SERVER_VERSION) {
			write("Incorrect server compliance. Please update your api.")
			throw RuntimeException("A client has the wrong server compliance")
		}
		
//		start up
		write("START")
		write(teamNumber.toString())
		
		println("connection established")
		
	}
	
	override fun receive(bot: Bot, world: World, team: Team) {
		val input = readInputTurnData()
		input.split("\n").forEach { line ->
			
			try {
				
				line.run { when {
					
					matches(MOVE_REGEX) -> line.split(" ").drop(0).map { it.toInt() }.let { bot.move(it[0], it[1]) }
					matches(TURN_REGEX) -> line.split(" ").drop(0).map { it.toInt() }.let { bot.turn(it[0]) }
					matches(SHOOT_REGEX) -> bot.shoot()
					matches(PLACE_REGEX) -> line.split(" ").drop(0).map { it.toInt() }.let { bot.place(it[0], it[1]) }
					matches(SPAWN_REGEX) -> line.split(" ").drop(0).map { it.toInt() }.let { bot.spawnBot(it[0], it[1]) }
					
				} }
				
			}catch (e: Exception) {
//				This should really never happen. The regex should sanitize every request
				errPrintln("WARNING: regex failed to sanitize input from client: '$line'")
			}
			
		}
	}
	
	override fun send(bot: Bot, world: World, team: Team) {
		
		val json = JSONObject() // the json text
		
//		bot's data
		json.put("x", bot.x)
		json.put("y", bot.y)
		json.put("angle", bot.angle)
		json.put("health", bot.health)
		json.put("ap", bot.actionPoints)
		json.put("mana", team.mana)
		json.put("uid", bot.uid)
		
		val vision = JSONArray() // array of things we can see
		
//		go through every thing we can see
		bot.generateVision().forEach {
			
//			make an object for it
			val visionObj = JSONObject()
			
//			SeenWorldObject data
			visionObj.put("type", it.javaClass.simpleName.toUpperCase()) // type
			if (it is Bot) visionObj.put("team", it.id) // team
			visionObj.put("x", it.x) // x
			visionObj.put("y", it.y) // y
			if (it is Entity) visionObj.put("angle", it.angle) // angle
			if (it is HealthWorldObject) visionObj.put("health", it.health) // health
			
			vision.add(visionObj) // add it to the things we can see
			
		}
		
//		add the vision
		json.put("vision", vision)
		
//		turn it into a string and write to client
		write(json.toJSONString())
		
	}
	
	/**
	 * Reads all the clients actions for this turn.
	 * */
	internal fun readInputTurnData(): String {
		var text = ""
		var line: String
		do {
			line = input.readLine()
			text += line + "\n"
		}while (shouldContinue(line))
		return text
	}
	
	/**
	 * Writes data to the socket.
	 * 
	 * @param msg the string to write
	 * */
	@JvmName("write")
	internal fun write(msg: String) {
//		output.print(msg)
		output.write(msg + "\n")
		output.flush() // make sure to flush!
	}
	
	fun shouldContinue(line: String): Boolean {
		return !line.contains("END")
	}
	
}
