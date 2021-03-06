package com.n9mtq4.botserver.connection

import com.n9mtq4.botserver.Team
import com.n9mtq4.botserver.bot.Bot
import com.n9mtq4.botserver.world.World

/**
 * Created by will on 5/19/16 at 10:09 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
interface InputHandler {
	
	fun receive(bot: Bot, world: World, team: Team)
	
}
