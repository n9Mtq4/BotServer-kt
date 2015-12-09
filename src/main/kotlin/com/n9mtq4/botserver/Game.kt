package com.n9mtq4.botserver

import com.n9mtq4.botserver.connection.ClientConnection
import com.n9mtq4.botserver.connection.decode.SocketDecoders
import com.n9mtq4.botserver.connection.encode.SocketEncoders
import com.n9mtq4.botserver.world.WORLD_HEIGHT
import com.n9mtq4.botserver.world.WORLD_WIDTH
import com.n9mtq4.botserver.world.World
import com.n9mtq4.botserver.world.generation.WorldGenerators
import java.io.File
import java.net.ServerSocket

/**
 * Created by will on 12/7/15 at 10:56 AM.
 * 
 * Implements Runnable just in case we want to
 * multi-thread it later on.
 * 
 * @property serverSocket the socket server receiving connections
 * @property world the game world where the bots are playing
 * @property gameWriter handles writing the save file
 * @property turnNumber the current number of turns that have passed (starts w/ 0)
 * @property team1 the [Team] for team #1. (as soon as [run] is called, this will have a value)
 * @property team2 the [Team] for team #2. (as soon as [run] is called, this will have a value)
 * @author Will "n9Mtq4" Bresnahan
 */
class Game : Runnable {
	
	val serverSocket: ServerSocket
	val world: World
	val gameWriter: GameWriter
	var turnNumber: Int
	
	var team1: Team? = null
	var team2: Team? = null
	
	/**
	 * Initializes the fields
	 * */
	init {
		
		this.serverSocket = ServerSocket(SOCKET_PORT)
		this.world = World(WORLD_WIDTH, WORLD_HEIGHT, WorldGenerators.Random)
		this.gameWriter = GameWriter(world, File("game.txt")) // TODO: get the file from cla
		this.turnNumber = 0
		
	}
	
	/**
	 * Initializes the teams and their connections
	 * */
	fun initConnections() {
		
//		TODO: change to KotlinClient when there are unique features
		val team1Connection = ClientConnection(serverSocket, SocketEncoders.RubyClient, SocketDecoders.RubyClient)
		val team2Connection = ClientConnection(serverSocket, SocketEncoders.RubyClient, SocketDecoders.RubyClient)
		
		team1 = Team(1, team1Connection)
		team2 = Team(2, team2Connection)
		
	}
	
	/**
	 * A loop that runs through the game,
	 * stops when [MAX_TURNS] have been reached.
	 * 
	 * Process the teams and the world.
	 * */
	override fun run() {
		
		initConnections()
		while (turnNumber < MAX_TURNS) {
			
//			TODO: ruby server does each teams bot back and forth
//			process the teams turn, if initConnections didn't work, we want the NPE
			team1!!.processTurn(world)
			team2!!.processTurn(world)
			
			gameWriter.tick() // write this turn's data
			world.tick() // tick the world
//			check if a team has won
			if (world.win > 0) {
//				a team has!
				gameWriter.printWriter.println("WIN ${world.win}") // record their winning
				break; // end the game
			}
			turnNumber++ // next turn
			
		}
		
		gameWriter.printWriter.println("DRAW") // no one wins :(
		
	}
	
}
