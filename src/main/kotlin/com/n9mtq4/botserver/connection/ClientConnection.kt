package com.n9mtq4.botserver.connection

import com.n9mtq4.botserver.API_LEVEL
import java.net.ServerSocket
import java.net.Socket

/**
 * Created by will on 12/7/15 at 11:05 AM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class ClientConnection(val serverSocket: ServerSocket, val team: Team) {
	
	val inSocket: Socket
	
	init {
		this.inSocket = serverSocket.accept()
		write("START API ${API_LEVEL}")
	}
	
	private fun write(msg: String) {
		inSocket
	}
	
}
