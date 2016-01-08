package com.n9mtq4.botserver.world.objects

import com.n9mtq4.botserver.world.World
import com.n9mtq4.botserver.world.objects.interfaces.WorldObject

/**
 * Created by will on 12/8/15 at 5:54 PM.
 * 
 * This is a [WorldObject] that is indestructible
 * and is used for walling of the edges of the map.
 * 
 * Every [Wall] on the map is identical.
 *
 * The code inside this class is a bit weird
 * as in this WorldObject is a singleton and
 * can not have multiple instances, but it
 * saves on ram when working on large maps.
 * 
 * @author Will "n9Mtq4" Bresnahan
 */
class Wall(override val world: World, override var x: Int, override var y: Int) : WorldObject {
	
	override var isSolid = true
	override val id = ID_WALL
	override val uuid = world.getNextUUID()
	
}
