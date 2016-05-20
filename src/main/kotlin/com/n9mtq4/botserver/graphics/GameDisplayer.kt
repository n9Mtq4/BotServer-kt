package com.n9mtq4.botserver.graphics

import com.n9mtq4.botserver.world.World

/**
 * Created by will on 5/20/16 at 3:31 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
interface GameDisplayer {
	
	fun render(world: World, turnLog: List<String>)
	fun gameOver(win: Int)
	
}
