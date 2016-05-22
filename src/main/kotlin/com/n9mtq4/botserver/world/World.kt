package com.n9mtq4.botserver.world

import com.n9mtq4.botserver.Game
import com.n9mtq4.botserver.bot.Bot
import com.n9mtq4.botserver.world.generation.WorldGeneratorBehavior
import com.n9mtq4.botserver.world.objects.Block
import com.n9mtq4.botserver.world.objects.WorldNothing
import com.n9mtq4.botserver.world.objects.interfaces.Tickable
import com.n9mtq4.botserver.world.objects.interfaces.WorldObject
import com.n9mtq4.kotlin.extlib.assertTrue
import com.n9mtq4.kotlin.extlib.math.pow
import com.n9mtq4.kotlin.extlib.math.sqrt
import com.n9mtq4.kotlin.extlib.math.toRadians
import java.util.ArrayList
import java.util.Arrays

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
class World(val game: Game, val width: Int, val height: Int, generator: WorldGeneratorBehavior) {
	
	internal val mapData: Array<WorldObject>
	internal val turnLog: ArrayList<String>
	internal var win: Int
	private var uidManager: Int = 0
	
	init {
//		TODO: isSolid = false wont work properly with the movement system
//		so long as the only isSolid false thing is WorldNothing
		this.mapData = Array(width * height, { index -> generator.getObjectAt(index % width, index / width, this)})
		this.turnLog = ArrayList<String>()
		this.win = 0 // win is 0 if there is nothing yet
	}
	
	fun createNewBot(x: Int, y: Int, team: Int) = Bot(this, game.getTeamByNumber(team), x, y)
	
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
//		clear the turn log
		turnLog.clear()
	}
	
	/**
	 * Spawns a bot at pos ([x], [y]) for team
	 * [teamNumber].
	 * 
	 * @param x the x pos
	 * @param y the y pos
	 * @param teamNumber the team number of the bot
	 * */
	fun spawnBotAt(x: Int, y: Int, teamNumber: Int) {
		this[x, y] = createNewBot(x, y, teamNumber)
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
	fun findObjectUsingRayCasting(x: Int, y: Int, angle: Double): WorldObject {
		
//		t'is always good
		assertWorldBounds(x, y)
		
//		calculate slope of the angle
		val dx = Math.cos(angle.toRadians())
		val dy = -Math.sin(angle.toRadians()) // remember 0 y is top and 1000 y is bottom, so negate it
		var cx = x.toDouble() // the currently scanning x pos
		var cy = y.toDouble() // the currently scanning y pos
		
		while (isInBounds(cx, cy)) {
//			if the WorldObject at (cx, cy) isn't nothing, return it with the pos
			if (cx.toInt() == x && cy.toInt() == y) {
//				TODO: don't repeat the update location
//				update the ray casting location
				cx += dx
				cy += dy
//				don't include this block
				continue
			}
			if (get(cx, cy) !is WorldNothing) return this[cx, cy]
//			update the ray casting location
			cx += dx
			cy += dy
		}
		
//		we should always return something, so this is an error
		throw RuntimeException("There are no walls on the map!")
		
	}
	
	/**
	 * Calculates the angle between two world objects
	 * 
	 * @param wo the first world object
	 * @param wo1 the second world object
	 * @return the angle between the two objects
	 * */
	fun getAngleBetween(wo: WorldObject, wo1: WorldObject): Double { //TODO: should this be an int?
		val x1 = wo.x
		val y1 = wo.y
		val x2 = wo1.x
		val y2 = wo1.y
		return Math.atan2((x1 - x2).toDouble(), (y1 - y2).toDouble())
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
		
		val obj = this[x, y]
		
//		move the world objects pos
		obj.x = nx
		obj.y = ny
		
//		change the location in the map array of the world object
		this[nx, ny] = obj
		this[x, y] = WorldNothing(this, x, y)
		
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
	
	operator fun minusAssign(obj: WorldObject) = remove(obj)
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
		this[x, y] = WorldNothing(this, x, y)
		
	}
	
	/**
	 * Gets the [WorldObject] at ([x], [y]).
	 *
	 * @param x the x pos (casts to an [Int])
	 * @param y the y pos (casts to an [Int])
	 * @return The [WorldObject] at that position
	 * @see set
	 * */
	operator fun get(x: Double, y: Double) = get(x.toInt(), y.toInt())
	
	/**
	 * Gets the [WorldObject] at ([x], [y]).
	 *
	 * @param x the x pos
	 * @param y the y pos
	 * @return The [WorldObject] at that position
	 * @see set
	 * */
	operator fun get(x: Int, y: Int): WorldObject {
		assertWorldBounds(x, y)
		return this[x + y * width] // convert (x, y) to index
	}
	
	/**
	 * Gets the [WorldObject] at [index].
	 *
	 * @param index the index in the world
	 * @return The [WorldObject] at that position
	 * @see set
	 * */
	operator fun get(index: Int): WorldObject {
		assertTrue(index in 0..mapData.lastIndex, "index out of bounds: $index")
		return mapData[index]
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
	operator fun set(x: Double, y: Double, obj: WorldObject) = set(x.toInt(), y.toInt(), obj)
	
	/**
	 * Sets the world object at ([x], [y]) to the new
	 * [WorldObject] passed as [obj].
	 *
	 * @param x the x pos
	 * @param y the y pos
	 * @param obj the new [WorldObject] to put in that position
	 * @see get
	 * */
	operator fun set(x: Int, y: Int, obj: WorldObject) {
		assertWorldBounds(x, y)
		this[x + y * width] = obj // convert (x, y) to index
	}
	
	/**
	 * Sets the world object at [index] to the new
	 * [WorldObject] passed as [obj].
	 *
	 * @param index the index in the world
	 * @param obj the new [WorldObject] to put in that position
	 * @see get
	 * */
	operator fun set(index: Int, obj: WorldObject) {
		assertTrue(index in 0..mapData.lastIndex, "index out of bounds: $index")
		mapData[index] = obj
	}
	
	/**
	 * Gets the next UID for a [WorldObject].
	 * 
	 * @return the next available UID
	 * */
	@JvmName("getNextUID")
	internal fun getNextUID(): Int {
		return uidManager++
	}
	
//	TODO: add documentation
	fun isInBounds(x: Double, y: Double) = isInBounds(x.toInt(), y.toInt())
	fun isInBounds(x: Int, y: Int) = x >= 0 && y >= 0 && x < width && y < height
	
	fun assertWorldBounds(x: Double, y: Double) = assertWorldBounds(x.toInt(), y.toInt())
	fun assertWorldBounds(x: Int, y: Int) = assertTrue(isInBounds(x, y), "position ($x, $y) are not in world bounds")
	
	fun isBorder(x: Int, y: Int) = x == 0 || y == 0 || x == width - 1 || y == height - 1
	
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

/** distance between two points */
fun getDistanceBetween(x1: Int, y1: Int, x2: Int, y2: Int) = (((x1 - x2) pow 2) + ((y1 - y2) pow 2)).sqrt()
fun getDistanceBetween(w1: WorldObject, w2: WorldObject) = getDistanceBetween(w1.x, w2.y, w2.x, w2.y)
fun getDistanceBetween(x1: Int, y1: Int, w: WorldObject) = getDistanceBetween(x1, y1, w.x, w.y)
