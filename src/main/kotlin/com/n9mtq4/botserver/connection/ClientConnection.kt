package com.n9mtq4.botserver.connection

import com.n9mtq4.botserver.Team
import com.n9mtq4.botserver.bot.Bot
import com.n9mtq4.botserver.connection.decode.SocketDecoder
import com.n9mtq4.botserver.connection.encode.SocketEncoder
import com.n9mtq4.botserver.world.World
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
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
class ClientConnection(val serverSocket: ServerSocket, val encoder: SocketEncoder, val decoder: SocketDecoder) {
	
	internal val client: Socket
	internal val input: BufferedReader
	internal val output: PrintWriter
	
	/**
	 * Waits for a client to connect
	 * and creates the input and output buffered steams.
	 * Then sends the game start command
	 * */
	init {
		
		print("Waiting for team to connect...")
		this.client = serverSocket.accept()
		println("connection established")
		this.input = BufferedReader(InputStreamReader(client.inputStream))
		this.output = PrintWriter(client.outputStream, true)
		
		write(encoder.encodeStartGame())
		
	}
	
	/**
	 * Sends the start turn command
	 * @see SocketEncoder.encodeStartTurn
	 * */
	internal fun startTurnSend() = write(encoder.encodeStartTurn())
	
	/**
	 * Sends the turn end command.
	 * 
	 * IMPORTANT: doesn't do anything at all right now
	 * 
	 * @see SocketEncoder.encodeEndTurn
	 * */
	internal fun endTurnSend() {/*write(encoder.encodeEndTurn())*/}
	
	/**
	 * Writes the [bot] to the client.
	 * 
	 * @param bot the bot to write
	 * @param world the world
	 * @param team the team
	 * @see SocketEncoder.encodeBotData
	 * */
	internal fun writeBot(bot: Bot, world: World, team: Team) = write(encoder.encodeBotData(bot, world, team))
	
	/**
	 * Reads data from the socket / client.
	 * 
	 * @return the line read by the socket
	 * */
	internal fun read() = input.readLine()
	
	/**
	 * Writes data to the socket.
	 * 
	 * @param msg the string to write
	 * */
	internal fun write(msg: String) {
		output.print(msg)
		output.flush() // make sure to flush!
	}
	
	/**
	 * Writes data to the socket followed by a new line.
	 * 
	 * @param msg the string to write
	 * @see write
	 * */
	internal fun writeln(msg: String) = write(msg + "\n")
	
}
