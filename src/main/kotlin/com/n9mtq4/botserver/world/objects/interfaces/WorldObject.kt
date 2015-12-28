package com.n9mtq4.botserver.world.objects.interfaces

import com.n9mtq4.botserver.world.World

/**
 * Created by will on 12/7/15 at 11:01 AM.
 * 
 * The parent interface for all things that can
 * be added to the world / map.
 * 
 * Contains variables for the world,
 * position, solid, and id.
 * 
 * @author Will "n9Mtq4" Bresnahan
 */
interface WorldObject {
	
	/**
	 * The parent [World] that this is added to.
	 * */
	val world: World
	/**
	 * The x pos of this object.
	 * */
	var x: Int
	/**
	 * The y pos of this object.
	 * */
	var y: Int
	/**
	 * If this object is solid and can't be walked
	 * through.
	 * */
	@Deprecated("not fully supported with movement")
	var isSolid: Boolean
	/**
	 * The id number for this [WorldObject].
	 * This is used when exporting to the clients.
	 * The is no support for 2 digit numbers yet
	 * */
	val id: Int
	/**
	 * The Unique id of the entity in the world.
	 * Initialize with ```override val uuid = world.getNextUUID()```
	 * This should be -1 for singletons
	 * */
	val uuid: Int
	
}
