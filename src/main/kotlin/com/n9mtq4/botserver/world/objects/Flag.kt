package com.n9mtq4.botserver.world.objects

import com.n9mtq4.botserver.bot.Bot
import com.n9mtq4.botserver.world.BOTS_TO_WIN
import com.n9mtq4.botserver.world.World
import com.n9mtq4.botserver.world.objects.interfaces.Tickable
import com.n9mtq4.botserver.world.objects.interfaces.WorldObject

/**
 * Created by will on 12/9/15 at 6:28 PM.
 * 
 * The flag [WorldObject].
 * This class also does win condition checking.
 * 
 * @param world the [World]
 * @param x the x pos
 * @param y the y pos
 * @param loyalty what team this flag belongs to
 * @author Will "n9Mtq4" Bresnahan
 */
class Flag(override val world: World, override var x: Int, override var y: Int, val loyalty: Int) : WorldObject, Tickable {
	
	override var isSolid = true
	override val id = ID_FLAG
	
	/**
	 * The flag's tick method.
	 * Checks for bots around it and if they
	 * are over the win threshold make the
	 * other team win.
	 * 
	 * TODO: win condition
	 * 
	 * @see BOTS_TO_WIN
	 * */
	override fun tick() {
		
		val surrounding = arrayOf(	world.get(-1 + x, -1 + y), 	world.get(x + 0, -1 + y), 	world.get(1 + x, -1 + y), 
									world.get(-1 + x, 0 + y), 	/*u r here*/				world.get(1 + x, 0 + y), 
									world.get(-1 + x, 1 + y), 	world.get(0 + x, 1 + y), 	world.get(1 + x, 1 + y))
		
		val surroundingEnemyBots = surrounding.	filter 	{ it is Bot }. // filter by bot
												map 	{ it.id }. // get the bot team
												filter 	{ it != loyalty }. // if it isn't our team
												size // count them all
		
		if (surroundingEnemyBots >= BOTS_TO_WIN) world.win = if (loyalty == 1) 2 else 1 // TODO: bad, not very extendable
		
	}
	
}
