package com.n9mtq4.botserver

import com.n9mtq4.botserver.bot.DEFAULT_MANA
import com.n9mtq4.botserver.bot.DELTA_MANA
import com.n9mtq4.botserver.bot.MAX_MANA
import com.n9mtq4.botserver.connection.InputHandler
import com.n9mtq4.botserver.connection.OutputHandler
import com.n9mtq4.botserver.world.World
import com.n9mtq4.botserver.world.objects.interfaces.Tickable

/**
 * Created by will on 12/9/15 at 3:06 PM.
 * 
 * @param teamNumber the team number (1 or 2)
 * @param clientConnection the [ClientConnection] used to talk to the client
 * @author Will "n9Mtq4" Bresnahan
 */
data class Team(val teamNumber: Int, val outputHandler: OutputHandler, val inputHandler: InputHandler) : Tickable {
	
	/**
	 * Stays the same over multiple turns. (w/ gradual increasing)
	 * Used for placing blocks and spawning more bots.
	 * It is global for the team, unlike action points
	 * which every bot has 10.
	 *
	 * @see DEFAULT_MANA
	 * @see MAX_MANA
	 * @see DELTA_MANA
	 * @see tick
	 * */
	var mana = DEFAULT_MANA
	
	/**
	 * Called by [processTurn].
	 * Recharges mana.
	 * 
	 * @see processTurn
	 * @see mana
	 * */
	override fun tick() {
		mana += DELTA_MANA
		if (mana > MAX_MANA) mana = MAX_MANA // mana cap
	}
	
	/**
	 * A method that processes the turn for the team.
	 * Sends the start turn to the client.
	 * Sends the bot's info to the client.
	 * Sends the end turn to the client.
	 * 
	 * @param world the game world
	 * */
	internal fun processTurn(world: World) {
		
//		TODO: the start and end are inside the forEach for jake's ruby api support.
//		go through all bots
		world.getAllBotsByTeam(teamNumber).forEach { bot ->
			
//			write to the client
			outputHandler.send(bot, world, this)
			
//			read from the client
			inputHandler.receive(bot, world, this)
			
		}
		tick() // team tick
		
	}
	
}
