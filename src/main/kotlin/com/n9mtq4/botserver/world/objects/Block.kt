package com.n9mtq4.botserver.world.objects

import com.n9mtq4.botserver.world.World
import com.n9mtq4.botserver.world.objects.interfaces.HealthWorldObject

/**
 * Created by will on 12/8/15 at 6:02 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class Block(override val world: World, override var x: Int, override var y: Int) : HealthWorldObject {
	
	override var health = BLOCK_HEALTH
	override var isDestroyable = true;
	override var isSolid = true
	override val id = ID_BLOCK
	
}
