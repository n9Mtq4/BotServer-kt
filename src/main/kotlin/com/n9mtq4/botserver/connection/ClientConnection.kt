package com.n9mtq4.botserver.connection

import com.n9mtq4.botserver.API_LEVEL
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

/**
 * Created by will on 12/7/15 at 11:05 AM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class ClientConnection(val serverSocket: ServerSocket) {
	
	internal val client: Socket
	internal val input: BufferedReader
	internal val output: PrintWriter
	
	init {
		
		this.client = serverSocket.accept()
		this.input = BufferedReader(InputStreamReader(client.inputStream))
		this.output = PrintWriter(client.outputStream, true)
		
		write("START API $API_LEVEL")
		
	}
	
	internal fun read(): String {
		return input.readLine()
	}
	
	internal fun write(msg: String) {
		output.print(msg)
	}
	
	internal fun writeln(msg: String) {
		output.println(msg)
	}
	
}
