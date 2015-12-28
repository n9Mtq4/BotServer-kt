package com.n9mtq4.botserver.world.generation

import com.n9mtq4.botserver.RANDOM
import com.n9mtq4.botserver.bot.Bot
import com.n9mtq4.botserver.world.World
import com.n9mtq4.botserver.world.objects.Block
import com.n9mtq4.botserver.world.objects.Flag
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
	 * Also places a flag and a bot for each team.
	 * */
	object Empty : WorldGeneratorBehavior {
		override fun getObjectAt(x: Int, y: Int, world: World): WorldObject {
			if (x == 0 || y == 0 || x == world.width - 1 || y == world.height - 1) return Wall // edges
//			things in the middle of the map, like bots or flags
			if (x == world.width / 2) {
				when (y) {
					1 -> return Flag(world, x, y, 1) // team 1's flag
					2 -> return Bot(world, world.game.getTeamByNumber(1), x, y) // team 1's starting bot
					world.height - 2 -> return Flag(world, x, y, 2) // team 2's flag
					world.height - 3 -> return Bot(world, world.game.getTeamByNumber(2), x, y) // team 2's bot
				}
			}
			return WorldNothing // everything else
		}
	}
	
	/**
	 * Generates a map with randomly places blocks.
	 * */
	object UniformRandom : WorldGeneratorBehavior {
		val BLOCK_PERCENTAGE = 0.005F // percentage of blocks - 0.5%
		override fun getObjectAt(x: Int, y: Int, world: World): WorldObject {
//			use the empty world as a template
			val emptysValue = Empty.getObjectAt(x, y, world)
			if (emptysValue !is WorldNothing) return emptysValue
			if (RANDOM.nextFloat() <= BLOCK_PERCENTAGE) return Block(world, x, y) // random chance of a block
			return WorldNothing // nothing else to do, so an empty space
		}
	}
	
	/**
	 * Generates a map with randomly placed blocks in the middle 1/3rd of the map.
	 * */
	object StrategicRandom : WorldGeneratorBehavior {
		val BLOCK_PERCENTAGE = 0.10F // percentage of blocks - 10%
		override fun getObjectAt(x: Int, y: Int, world: World): WorldObject {
//			use the empty world as a template
			val emptysValue = Empty.getObjectAt(x, y, world)
			if (emptysValue !is WorldNothing) return emptysValue
//			only generate random blocks in the middle 1/3rd of the map
			if (y > (world.height / 3) * 1 && y < (world.height / 3) * 2) {
				if (RANDOM.nextFloat() <= BLOCK_PERCENTAGE) return Block(world, x, y) // random chance of a block
			}
			return WorldNothing // nothing else to do, so an empty space
		}
	}
	
}
