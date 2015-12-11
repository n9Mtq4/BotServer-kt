package com.n9mtq4.botserver.bot

import com.n9mtq4.botserver.Team
import com.n9mtq4.botserver.safeAssert
import com.n9mtq4.botserver.world.World
import com.n9mtq4.botserver.world.objects.interfaces.Entity
import com.n9mtq4.botserver.world.objects.interfaces.HealthWorldObject
import com.n9mtq4.botserver.world.objects.interfaces.Tickable
import java.util.ArrayList

/**
 * Created by will on 12/7/15 at 11:00 AM.
 * 
 * Code that implements a bot that a client can control.
 * 
 * @param world The world the bot is in
 * @param team the team number of the bot
 * @param x the x pos of the bot in the world
 * @param y the y pos of the bot in the world
 * @author Will "n9Mtq4" Bresnahan
 */
class Bot(override val world: World, val team: Team, override var x: Int, override var y: Int) : Entity, Tickable {
	
	override var angle: Int = if (team.teamNumber == 1) 270 else 90
	override var health: Int = BOT_HEALTH
	override var invincible: Boolean = false
	override var isSolid: Boolean = true
	override val id = team.teamNumber // bot id is the same as the team number
	
	/**
	 * Number of movements the bot can do in one turn.
	 * Resets back to [DEFAULT_ACTION_POINTS] every turn.
	 * 
	 * @see DEFAULT_ACTION_POINTS
	 * */
	var actionPoints = DEFAULT_ACTION_POINTS
	
	/**
	 * Calculates an [ArrayList] of [SeenWorldObject]s that
	 * are in the bot's field of view.
	 * 
	 * @return an [ArrayList] of [SeenWorldObject]s in the bot's fov
	 * @see FOV
	 * */
	internal fun generateVision(): ArrayList<SeenWorldObject> {
		
		val vision = ArrayList<SeenWorldObject>() // store vision things here
		var castingAngle = angle - (FOV / 2) // the current angle we are ray casting
		
		while (castingAngle < angle + (FOV / 2)) { // go through all angles in FOV
			
			val obj = world.findObjectUsingRayCasting(x, y, castingAngle) // ray cast
			
			if (!vision.contains(obj)) vision.add(obj) // if we haven't seen it yet, add it
			
			castingAngle++ // next angle
			
		}
		
		return vision
		
	}
	
	/**
	 * This method is called on all bots every turn.
	 * Used for healing and regenerating mana.
	 * 
	 * @see Tickable.tick
	 * */
	override fun tick() {
		health += DELTA_BOT_HEALTH
		if (health > MAX_BOT_HEALTH) health = MAX_BOT_HEALTH // cap the health
		actionPoints = DEFAULT_ACTION_POINTS // reset the action points
	}
	
	/**
	 * Calculates how much moving by [dx] and [dy]
	 * will cost in action points.
	 * 
	 * @param dx the delta x (-1 to 1, theoretically could be more)
	 * @param dy the delta y (-1 to 1, theoretically could be more)
	 * @return the cost in action points
	 * @see move
	 * @see MOVE_COST
	 * */
	internal fun calcMove(dx: Int, dy: Int) = MOVE_COST
	
	/**
	 * Moves the [Bot] by [dx] in the x direction and
	 * moves the [Bot] by [dy] in the y direction.
	 * Uses up action points.
	 * 
	 * @param dx the delta x (-1 to 1)
	 * @param dy the delta y (-1 to 1)
	 * @see calcMove
	 * @see MOVE_COST
	 * */
	fun move(dx: Int, dy: Int) {
		
//		server side movement checking
		if (safeAssert(dx in -1..1, "movement out of bounds: $dx")) return
		if (safeAssert(dy in -1..1, "movement out of bounds: $dy")) return
		
//		action point handling
		if (assertPerformAction(calcMove(dx, dy))) return
		
//		move us
		world.moveRel(this, dx, dy)
		
	}
	
	/**
	 * Calculates how much turning by [angle] will
	 * cost in action points.
	 * 
	 * @param angle the turning angle in degrees
	 * @return the cost in action points
	 * @see turn
	 * @see TURN_COST
	 * */
	internal fun calcTurn(angle: Int) = angle / TURN_COST
	
	/**
	 * Turns the [Bot] by [angle] degrees.
	 * Uses up action points.
	 * 
	 * @param angle the angle to turn by in degrees
	 * @see calcTurn
	 * @see TURN_COST
	 * */
	fun turn(angle: Int) {
		
//		server side turn checking
		if (safeAssert(angle in -360..360, "angle out of bounds: $angle")) return
		
//		action point handling
		if (assertPerformAction(calcTurn(angle))) return
		
//		turn us
		this.angle += angle
		this.angle %= 360 // keep the angle in bounds
		
	}
	
	/**
	 * Calculates how much shooting will cost
	 * in action points.
	 * 
	 * @return the cost in action points
	 * @see shoot
	 * @see SHOOT_COST
	 * */
	internal fun calcShoot() = SHOOT_COST
	
	/**
	 * Shoots based off where the bot is facing.
	 * The shot will deal [SHOOT_DAMAGE] in damage
	 * to the first [HealthWorldObject] that it encounters.
	 * Uses up action points.
	 * 
	 * @see angle
	 * @see calcShoot
	 * @see SHOOT_COST
	 * */
	fun shoot() {
		
//		action point handling
		if (assertPerformAction(calcShoot())) return
		
		val target = world.findObjectUsingRayCasting(x, y, angle) // find the first target
		if (target.obj is HealthWorldObject) target.obj.dealDamage(SHOOT_DAMAGE) // if we can shoot it, then shoot it
//		TODO: add this to the turn file, so we can see the shot
		world.turnLog.add("$x, $y, $angle")
		
	}
	
	/**
	 * Calculates how much placing a block will cost
	 * in mana. Places it at ([x] + [dx], [y] + [dy]).
	 * 
	 * @return the cost in mana
	 * @param dx the delta x of the block pos (-1 to 1)
	 * @param dy the delta y of the block pos (-1 to 1)
	 * @see place
	 * @see PLACE_COST
	 * */
	internal fun calcPlace(dx: Int, dy: Int) = PLACE_COST
	
	/**
	 * Places a block at ([x] + [dx], [y] + [dy]), if
	 * there are not any blocks there first.
	 * Uses up mana points.
	 * 
	 * @param dx the delta x pos
	 * @param dy the delta y pos
	 * */
	fun place(dx: Int, dy: Int) {
		
//		mana handling
		if (assertPerformMana(calcPlace(dx, dy))) return
		
//		place a block - world.placeBlock does checking for a clear space
		world.placeBlock(x + dx, y + dy)
		
	}
	
	/**
	 * A method that checks to see if you can perform an action.
	 * If you can, it deducts action points from your total.
	 * 
	 * @param need How many action points needed
	 * @return true if there aren't enough action points, false otherwise
	 * */
	private fun assertPerformAction(need: Int): Boolean {
		if (actionPoints - need < 0) return true // stop if we can't do it, failed
		actionPoints -= need // deduct
		return false // say it was successful
	}
	
	/**
	 * A method that checks to see if you can perform an action.
	 * If you can, it deducts mana from your total.
	 *
	 * @param need How much mana is needed
	 * @return true if there aren't enough mana, false otherwise
	 * */
	private fun assertPerformMana(need: Int): Boolean {
		if (team.mana - need < 0) return true
		team.mana -= need
		return false
	}
	
}
