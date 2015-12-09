package com.n9mtq4.botserver.world.objects.interfaces

/**
 * Created by will on 12/8/15 at 5:55 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
interface HealthWorldObject : WorldObject {
	
	var health: Int
	var isDestroyable: Boolean
	
	fun dealDamage(amount: Int) {
		
		if (!isDestroyable) return // stop if it can't be destroyed
		health -= amount // subtract from the health
		
		if (isDead()) world.remove(this) // remove this if it is dead
		
	}
	
	fun isDead() = health <= 0
	
}
