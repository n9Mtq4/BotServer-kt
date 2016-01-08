package com.n9mtq4.botserver.world.objects

import com.n9mtq4.botserver.world.World
import com.n9mtq4.botserver.world.objects.interfaces.WorldObject

/**
 * Created by will on 12/8/15 at 4:58 PM.
 * 
 * This is a [WorldObject] that represents a blank
 * space on the map / nothing is there.
 * 
 * Every [WorldNothing] on the map is identical.
 * 
 * The code inside this class is a bit weird
 * as in this WorldObject is a singleton and
 * can not have multiple instances, but it
 * saves on ram when working on large maps.
 * 
 * @author Will "n9Mtq4" Bresnahan
 */
object WorldNothing : WorldObject {
	
	override val world: World
		get() = throw UnsupportedOperationException()
	override var x = -1
		get() {
//			System.err.println("[WARNING]: getting x on WorldNothing")
			return -1
		}
	override var y = -1
		get() {
//			System.err.println("[WARNING]: getting y on WorldNothing")
			return -1
		}
	override var isSolid = false
	override val id = ID_NOTHING
	override val uuid = -1
	
}
