package com.n9mtq4.botserver.world.generation

import com.n9mtq4.botserver.world.objects.interfaces.WorldObject
import com.n9mtq4.botserver.world.World

/**
 * Created by will on 12/8/15 at 5:46 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
interface WorldGeneratorBehavior {
	
	fun getObjectAt(x: Int, y: Int, world: World): WorldObject
	
}
