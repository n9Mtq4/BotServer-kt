package com.n9mtq4.botserver.world.objects

import com.n9mtq4.botserver.world.World
import com.n9mtq4.botserver.world.objects.interfaces.HealthWorldObject

/**
 * Created by will on 12/8/15 at 6:02 PM.
 * 
 * A block.
 * Something that is in the world and destroyable.
 * Can also be placed by [com.n9mtq4.botserver.bot.Bot]s.
 * 
 * @see HealthWorldObject
 * @see com.n9mtq4.botserver.world.objects.interfaces.WorldObject
 * @author Will "n9Mtq4" Bresnahan
 */
class Block(override val world: World, override var x: Int, override var y: Int) : HealthWorldObject {
	
	/**@see HealthWorldObject.health*/
	override var health = BLOCK_HEALTH
	/**@see HealthWorldObject.invincible*/
	override var invincible = false
	/**@see com.n9mtq4.botserver.world.objects.interfaces.WorldObject.isSolid*/
	override var isSolid = true
	/**@see com.n9mtq4.botserver.world.objects.interfaces.WorldObject.id]*/
	override val id = ID_BLOCK
	/**@see com.n9mtq4.botserver.world.object.interfaces.WorldObject.uuid*/
	override val uuid = world.getNextUUID()
	
}
