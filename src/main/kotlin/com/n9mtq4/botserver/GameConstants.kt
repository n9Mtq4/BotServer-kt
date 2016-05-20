package com.n9mtq4.botserver

/**
 * Created by will on 12/7/15 at 10:58 AM.
 * 
 * @property SERVER_VERSION the version of the server that the client must have compliance with
 * @property SOCKET_PORT the default port to run the socket server
 * @property API_LEVEL the api level, increment it if you change the encode/decode and make it incompatible
 * @property MAX_TURNS the max turns a game can last, will stop when the turn count hits this
 * 
 * @author Will "n9Mtq4" Bresnahan
 */

val SERVER_VERSION = "v1.0.5-beta" // The version of the server that the client must have compliance with
val SOCKET_PORT = 2000
val API_LEVEL = 1
val MAX_TURNS = 1000
val CLIENT_TURN_TIMER = 500L
