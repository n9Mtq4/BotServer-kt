package com.n9mtq4.botserver

import com.n9mtq4.botserver.world.WORLD_HEIGHT
import com.n9mtq4.botserver.world.WORLD_WIDTH
import com.n9mtq4.botserver.world.World
import com.n9mtq4.botserver.world.generation.WorldGenerators
import java.net.ServerSocket
import java.util.ArrayList

/**
 * Created by will on 12/7/15 at 10:56 AM.
 * 
 * Implements Runnable just in case we want to
 * multi-thread it later on.
 * 
 * @author Will "n9Mtq4" Bresnahan
 */
class Game : Runnable {
	
	val turnLog: ArrayList<String>
	val serverSocket: ServerSocket
	val world: World
	
	init {
		
		this.turnLog = ArrayList<String>()
		this.serverSocket = ServerSocket(SOCKET_PORT)
		this.world = World(WORLD_WIDTH, WORLD_HEIGHT, WorldGenerators.Random())
		
	}
	
	fun start() {
		
		
		
	}
	
	override fun run() {
		throw UnsupportedOperationException()
	}
	
	internal fun addToTurnLog(msg: String) {
		turnLog.add(msg)
	}
	
}
