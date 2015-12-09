package com.n9mtq4.botserver.world.generation

import com.n9mtq4.botserver.world.World
import com.n9mtq4.botserver.world.objects.Block
import com.n9mtq4.botserver.world.objects.Wall
import com.n9mtq4.botserver.world.objects.WorldNothing
import com.n9mtq4.botserver.world.objects.interfaces.WorldObject

/**
 * Created by will on 12/8/15 at 5:47 PM.
 * 
 * Contains some default implementations for
 * [WorldGeneratorBehavior]s.
 * 
 * @see WorldGeneratorBehavior
 * @see [World]
 * @author Will "n9Mtq4" Bresnahan
 */
sealed class WorldGenerators {
	
	/**
	 * Generates an empty map, except for walls along the outside.
	 * */
	object Empty : WorldGeneratorBehavior {
		override fun getObjectAt(x: Int, y: Int, world: World): WorldObject {
			if (x == 0 || y == 0 || x == world.width - 1 || y == world.height - 1) return Wall // edges
			return WorldNothing // everything else
		}
	}
	
	/**
	 * Generates a map with randomly places blocks.
	 * */
	object Random : WorldGeneratorBehavior {
		private val RANDOM = java.util.Random()
		val BLOCK_PERCENTAGE = 0.005F // percentage of blocks - 0.5%
		override fun getObjectAt(x: Int, y: Int, world: World): WorldObject {
			if (Empty.getObjectAt(x, y, world) is Wall) return Wall // use the empty generator as a template
			if (RANDOM.nextFloat() <= BLOCK_PERCENTAGE) return Block(world, x, y) // random chance of a block
			return WorldNothing // nothing else to do, so an empty space
		}
	}
	
}
