package com.n9mtq4.botserver

import com.n9mtq4.botserver.connection.ClientConnection
import com.n9mtq4.botserver.graphics.GameDisplayer
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
open class Game(val renderer: GameDisplayer? = null) : Runnable {
	
	val serverSocket: ServerSocket
	lateinit var world: World // TODO: bad find a way to make it a val
	lateinit var gameWriter: GameWriter // TODO: bad find a way to make it a val
	var turnNumber: Int
	
	lateinit var team1: Team
	lateinit var team2: Team
	
	/**
	 * Initializes the fields
	 * */
	init {
		
		this.serverSocket = ServerSocket(SOCKET_PORT)
		this.turnNumber = 0
		
	}
	
	fun getTeamByNumber(teamNumber: Int): Team {
		return when (teamNumber) {
			1 -> team1
			2 -> team2
			else -> throw IllegalArgumentException("No team with number: $teamNumber")
		}
	}
	
	/**
	 * Initializes the teams and their connections
	 * */
	open fun initConnections() {
		
		val team1Connection = ClientConnection(1, serverSocket)
		val team2Connection = ClientConnection(2, serverSocket)
		
		team1 = Team(1, team1Connection, team1Connection)
		team2 = Team(2, team2Connection, team2Connection)
		
	}
	
	/**
	 * A loop that runs through the game,
	 * stops when [MAX_TURNS] have been reached.
	 * 
	 * Process the teams and the world.
	 * */
	override fun run() {
		
		initConnections()
		
//		generate world
		this.world = World(this, WORLD_WIDTH, WORLD_HEIGHT, WorldGenerators.StrategicRandom)
		this.gameWriter = GameWriter(world, File("game.txt"))
		
		while (turnNumber < MAX_TURNS) {
			
			println("Processing turn: $turnNumber")
			
//			process the teams turn
			team1.processTurn(world)
			team2.processTurn(world)
			
			renderer?.render(world, world.turnLog)
			gameWriter.tick() // write this turn's data
			world.tick() // tick the world
			
//			check if a team has won
			if (world.win > 0) {
//				a team has won!
				if (world.win == 1) {
					team1.outputHandler.endGame("WIN")
					team2.outputHandler.endGame("LOOSE")
				}else {
					team1.outputHandler.endGame("LOOSE")
					team2.outputHandler.endGame("WIN")
				}
				gameWriter.printWriter.println("WIN ${world.win}") // record their winning
				break; // end the game
			}
			
			turnNumber++ // next turn
			
		}
		
		renderer?.gameOver(world.win) // FIXME: game over doesn't yet show the end of the game!
		
		if (world.win > 0) {
			team1.outputHandler.endGame("DRAW")
			team2.outputHandler.endGame("DRAW")
			gameWriter.printWriter.println("DRAW") // no one wins :(
		}
		
	}
	
}
