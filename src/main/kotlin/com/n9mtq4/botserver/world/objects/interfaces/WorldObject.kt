package com.n9mtq4.botserver.world.objects.interfaces

import com.n9mtq4.botserver.world.World

/**
 * Created by will on 12/7/15 at 11:01 AM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
interface WorldObject {
	
	val world: World
	var x: Int
	var y: Int
	@Deprecated("not fully supported with movement")
	var isSolid: Boolean
	val id: Int
	
}
