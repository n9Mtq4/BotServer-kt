package com.n9mtq4.botserver.bot

import com.n9mtq4.botserver.safeAssert
import com.n9mtq4.botserver.world.World
import com.n9mtq4.botserver.world.objects.interfaces.Entity
import com.n9mtq4.botserver.world.objects.interfaces.HealthWorldObject
import java.util.ArrayList

/**
 * Created by will on 12/7/15 at 11:00 AM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class Bot(override val world: World, val team: Int, override var x: Int, override var y: Int) : Entity {
	
	override var angle: Int = if (team == 1) 90 else 270
	override var health: Int = BOT_HEALTH
	override var isDestroyable: Boolean = true
	override var isSolid: Boolean = true
	override val id = team // bot id is the same as the team number
	
	var actionPoints = DEFAULT_ACTION_POINTS
	var mana = DEFAULT_MANA
	
	fun generateVision(): ArrayList<SeenWorldObject> {
		
		val vision = ArrayList<SeenWorldObject>() // store vision things here
		var castingAngle = angle - (FOV / 2) // the current angle we are ray casting
		
		while (castingAngle < angle + (FOV / 2)) { // go through all angles in FOV
			
			val obj = world.findObjectUsingRayCasting(x, y, castingAngle) // ray cast
			
			if (!vision.contains(obj)) vision.add(obj) // if we haven't seen it yet, add it
			
			castingAngle++ // next angle
			
		}
		
		return vision
		
	}
	
	fun move(dx: Int, dy: Int) {
		
//		server side movement checking
		if (safeAssert(dx in -1..1, "movement out of bounds: $dx")) return
		if (safeAssert(dy in -1..1, "movement out of bounds: $dy")) return
		
//		move us
		world.moveRel(this, dx, dy)
		
	}
	
	fun turn(angle: Int) {
		
//		server side turn checking
		if (safeAssert(angle in -360..360, "angle out of bounds: $angle")) return
		
//		turn us
		this.angle += angle
		this.angle %= 360 // keep the angle in bounds
		
	}
	
	fun shoot() {
		
		val target = world.findObjectUsingRayCasting(x, y, angle)
		if (target.obj is HealthWorldObject) target.obj.dealDamage(SHOOT_DAMAGE)
		
	}
	
}
