package com.n9mtq4.botserver.world.generation

import com.n9mtq4.botserver.world.World
import com.n9mtq4.botserver.world.objects.interfaces.WorldObject

/**
 * Created by will on 12/8/15 at 5:46 PM.
 * 
 * An interface that classes can implement
 * for generating worlds.
 * 
 * @author Will "n9Mtq4" Bresnahan
 */
interface WorldGeneratorBehavior {
	
	/**
	 * When generating a [World] give us what [WorldObject]
	 * should be at ([x], [y]).
	 * 
	 * @param x the x pos
	 * @param y the y pos
	 * @param world the current world being generated. (Note: may not be initializes)
	 * @return the desired [WorldObject] at ([x], [y])
	 * */
	fun getObjectAt(x: Int, y: Int, world: World): WorldObject
	
}
