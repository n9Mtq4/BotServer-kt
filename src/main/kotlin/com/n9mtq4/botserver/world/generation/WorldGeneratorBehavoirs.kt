package com.n9mtq4.botserver.world.generation

import com.n9mtq4.botserver.world.World
import com.n9mtq4.botserver.world.objects.Block
import com.n9mtq4.botserver.world.objects.Wall
import com.n9mtq4.botserver.world.objects.WorldNothing
import com.n9mtq4.botserver.world.objects.interfaces.WorldObject

/**
 * Created by will on 12/8/15 at 5:47 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
sealed class WorldGenerators {
	
	class Empty() : WorldGeneratorBehavior {
		override fun getObjectAt(x: Int, y: Int, world: World): WorldObject {
			if (x == 0 || y == 0 || x == world.width - 1 || y == world.height - 1) return Wall
			return WorldNothing
		}
	}
	
	class Random() : WorldGeneratorBehavior {
		companion object {
			private val RANDOM = java.util.Random()
			val BLOCK_PERCENTAGE = 0.5F
		}
		override fun getObjectAt(x: Int, y: Int, world: World): WorldObject {
			if (x == 0 || y == 0 || x == world.width - 1 || y == world.height - 1) return Wall
			if (RANDOM.nextFloat() <= BLOCK_PERCENTAGE) return Block(world, x, y)
			return WorldNothing
		}
	}
	
}
