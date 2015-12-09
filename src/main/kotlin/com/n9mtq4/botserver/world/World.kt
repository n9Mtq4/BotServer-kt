package com.n9mtq4.botserver.world

import com.n9mtq4.botserver.bot.Bot
import com.n9mtq4.botserver.bot.SeenWorldObject
import com.n9mtq4.botserver.toRadians
import com.n9mtq4.botserver.world.generation.WorldGeneratorBehavior
import com.n9mtq4.botserver.world.objects.Block
import com.n9mtq4.botserver.world.objects.WorldNothing
import com.n9mtq4.botserver.world.objects.interfaces.Tickable
import com.n9mtq4.botserver.world.objects.interfaces.WorldObject
import java.util.ArrayList
import java.util.Arrays
import kotlin.test.assertTrue

/**
 * Created by will on 12/7/15 at 10:58 AM.
 * 
 * A class that contains all the data of the world.
 * 
 * @param width the world width
 * @param height the world height
 * @param generator the [WorldGeneratorBehavior] that should be used
 * @see WorldGeneratorBehavior
 * @see com.n9mtq4.botserver.world.generation.WorldGenerators
 * @author Will "n9Mtq4" Bresnahan
 */
public class World(val width: Int, val height: Int, generator: WorldGeneratorBehavior) {
	
	internal val mapData: Array<WorldObject>
	internal val turnLog: ArrayList<String>
	internal var win: Int
	
	init {
//		TODO: isSolid = false wont work properly with the movement system
//		so long as the only isSolid false thing is WorldNothing
		this.mapData = Array(width * height, { index -> generator.getObjectAt(index % width, index / width, this)})
		this.turnLog = ArrayList<String>()
		this.win = -1
	}
	
	/**
	 * Goes through everything in the world and calls its tick method.
	 * The [WorldObject] must implement [Tickable].
	 * This method should be called from the [com.n9mtq4.botserver.Game] class
	 * 
	 * @see Tickable
	 * @see Tickable.tick
	 * */
	fun tick() {
		mapData.filter 	{ it is Tickable }.
				forEach { (it as Tickable).tick() }
	}
	
	/**
	 * Gets all bots that are owned by a specified team number.
	 * 
	 * @param teamNumber the team number (1 or 2)
	 * */
	fun getAllBotsByTeam(teamNumber: Int): List<Bot> {
//		make sure the team number is 1 or 2
		assertTrue(teamNumber == 1 || teamNumber == 2, "$teamNumber is not a valid team number")
		return mapData.	filter 	{ it is Bot }. // only bots
						filter 	{ it.id == teamNumber }. // only with the correct team number
						map 	{ it as Bot } // turn the [WorldObjects] to [Bots]
	}
	
	/**
	 * Does a ray cast from ([x], [y]) using [angle] and returns
	 * the first [WorldObject] it encounters.
	 *
	 * @param x the x pos (casts to a [Double])
	 * @param y the y pos (casts to a [Double])
	 * @param angle the angle (degrees)
	 * @return the first [WorldObject] along that line that isn't [WorldNothing]
	 * */
	fun findObjectUsingRayCasting(x: Int, y: Int, angle: Int) = findObjectUsingRayCasting(x, y, angle.toDouble())
	
	/**
	 * Does a ray cast from [x] and [y] using [angle] and returns
	 * the first [WorldObject] it encounters.
	 * 
	 * @param x the x pos
	 * @param y the y pos
	 * @param angle the angle (degrees)
	 * @return the first [WorldObject] along that line that isn't [WorldNothing]. Returns as [SeenWorldObject]
	 * */
	fun findObjectUsingRayCasting(x: Int, y: Int, angle: Double): SeenWorldObject {
		
//		t'is always good
		assertWorldBounds(x, y)
		
//		calculate slope of the angle
		val dx = Math.cos(angle.toRadians())
		val dy = -Math.sin(angle.toRadians()) // remember 0 y is top and 1000 y is bottom, so negate it
		var cx = x.toDouble() // the currently scanning x pos
		var cy = y.toDouble() // the currently scanning y pos
		
		while (isInBounds(cx, cy)) {
//			if the WorldObject at (cx, cy) isn't nothing, return it with the pos
			if (get(cx, cy) !is WorldNothing) return SeenWorldObject(get(cx, cy), cx.toInt(), cy.toInt())
//			update the ray casting location
			cx += dx
			cy += dy
		}
		
//		we should always return something, so this is an error
		throw RuntimeException("There are no walls on the map!")
		
	}
	
	/**
	 * Moves the [WorldObject] [obj] a relative
	 * amount by [dx] and [dy].
	 *
	 * @param obj the [WorldObject] to move
	 * @param dx the delta x
	 * @param dy the delta y
	 * */
	fun moveRel(obj: WorldObject, dx: Int, dy: Int) = moveRel(obj.x, obj.y, dx, dy)
	
	/**
	 * Moves the [WorldObject] at pos ([x], [y])
	 * a relative amount by [dx] and [dy].
	 * 
	 * @param x the old x pos
	 * @param y the old y pos
	 * @param dx the delta x
	 * @param dy the delta y
	 * */
	fun moveRel(x: Int, y: Int, dx: Int, dy: Int) = moveAbs(x, y, x + dx, y + dy)
	
	/**
	 * Moves the [WorldObject] [obj] to ([nx], [ny]).
	 * 
	 * @param obj the [WorldObject] to move
	 * @param nx the new x pos
	 * @param ny the new y pos
	 * */
	fun moveAbs(obj: WorldObject, nx: Int, ny: Int) = moveAbs(obj.x, obj.y, nx, ny)
	
	/**
	 * Moves the [WorldObject] at pos ([x], [y]) to 
	 * pos ([nx], [ny]).
	 * 
	 * @param x the old x pos
	 * @param y the old y pos
	 * @param nx the new x pos
	 * @param ny the new y pos
	 * */
	fun moveAbs(x: Int, y: Int, nx: Int, ny: Int) {
		
//		make sure we wont crash anything first
		assertWorldBounds(x, y)
		assertWorldBounds(nx, ny)
		
//		check for collision
		if (get(nx, ny).isSolid) return
		
		val obj = get(x, y)
		
//		move the world objects pos
		obj.x = x
		obj.y = y
		
//		change the location in the map array of the world object
		set(nx, ny, obj)
		set(x, y, WorldNothing)
		
	}
	
	/**
	 * Places a new block at ([x], [y])
	 * if there isn't anything there.
	 * 
	 * @param x the x pos
	 * @param y the y pos
	 * */
	fun placeBlock(x: Int, y: Int) {
		
//		make sure it's a valid coord
		assertWorldBounds(x, y)
		
//		make sure there isn't something there
		if (get(x, y) !is WorldNothing) return
		
//		place a block
		forcePlaceBlock(x, y)
		
	}
	
	/**
	 * Places a new block ([x], [y]).
	 * 
	 * @param x the x pos
	 * @param y the y pos
	 * */
	fun forcePlaceBlock(x: Int, y: Int) {
		
//		make sure it's a valid coord
		assertWorldBounds(x, y)
		
//		place a new block
		set(x, y, Block(this, x, y))
		
	}
	
	/**
	 * Removes the [WorldObject] [obj]
	 * from the world.
	 *
	 * @param obj the [WorldObject] to remove
	 * */
	fun remove(obj: WorldObject) = remove(obj.x, obj.y)
	
	/**
	 * Removes the [WorldObject] at ([x], [y])
	 * from the world.
	 * 
	 * @param x the x pos
	 * @param y the y pos
	 * */
	fun remove(x: Int, y: Int) {
		
//		make sure it's a valid coord
		assertWorldBounds(x, y)
		
//		remove it from the world
		set(x, y, WorldNothing)
		
	}
	
	/**
	 * Gets the [WorldObject] at ([x], [y]).
	 *
	 * @param x the x pos (casts to an [Int])
	 * @param y the y pos (casts to an [Int])
	 * @return The [WorldObject] at that position
	 * @see set
	 * */
	fun get(x: Double, y: Double) = get(x.toInt(), y.toInt())
	
	/**
	 * Gets the [WorldObject] at ([x], [y]).
	 *
	 * @param x the x pos
	 * @param y the y pos
	 * @return The [WorldObject] at that position
	 * @see set
	 * */
	fun get(x: Int, y: Int): WorldObject {
		assertWorldBounds(x, y)
		return mapData[x + y * width] // convert (x, y) to index
	}
	
	/**
	 * Sets the world object at ([x], [y]) to the new
	 * [WorldObject] passed as [obj].
	 *
	 * @param x the x pos (casts to an [Int])
	 * @param y the y pos (casts to an [Int])
	 * @param obj the new [WorldObject] to put in that position
	 * @see get
	 * */
	fun set(x: Double, y: Double, obj: WorldObject) = set(x.toInt(), y.toInt(), obj)
	
	/**
	 * Sets the world object at ([x], [y]) to the new
	 * [WorldObject] passed as [obj].
	 *
	 * @param x the x pos
	 * @param y the y pos
	 * @param obj the new [WorldObject] to put in that position
	 * @see get
	 * */
	fun set(x: Int, y: Int, obj: WorldObject) {
		assertWorldBounds(x, y)
		mapData[x + y * width] = obj // convert (x, y) to index
	}
	
//	TODO: add documentation
	fun isInBounds(x: Double, y: Double) = isInBounds(x.toInt(), y.toInt())
	fun isInBounds(x: Int, y: Int) = x >= 0 && y >= 0 && x < width && y < height
	
	fun assertWorldBounds(x: Double, y: Double) = assertWorldBounds(x.toInt(), y.toInt())
	fun assertWorldBounds(x: Int, y: Int) = assertTrue(isInBounds(x, y), "position ($x, $y) are not in world bounds")
	
	internal fun toGraphicsString(): String {
		var data = ""
		mapData.forEach { data += it.id } // add the [WorldObject] id to the string
		return data
	}
	
	internal fun turnLogToGraphicsString(): String {
		var data = ""
		turnLog.forEach { data += it + " " }
		return data.trim()
	}
	
	override fun toString(): String {
		return Arrays.toString(mapData)
	}
	
	override fun equals(other: Any?): Boolean {
		if (other == null) return false
		if (other !is World) return false
		return 	mapData.equals(other.mapData) && 
				turnLog.equals(other.turnLog) && 
				win == other.win
	}
	
	override fun hashCode(): Int {
		return super.hashCode()
	}
	
}
