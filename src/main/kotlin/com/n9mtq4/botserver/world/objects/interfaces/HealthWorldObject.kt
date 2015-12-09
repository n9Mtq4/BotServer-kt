package com.n9mtq4.botserver.world.objects.interfaces

/**
 * Created by will on 12/8/15 at 5:55 PM.
 * 
 * Implement this if your [WorldObject] has health
 * 
 * @author Will "n9Mtq4" Bresnahan
 */
interface HealthWorldObject : WorldObject {
	
	/**
	 * The health of the [WorldObject]
	 * */
	var health: Int
	/**
	 * If this [WorldObject] should take damage right now
	 * */
	var invinsible: Boolean
	
	/**
	 * Deals [amount] of damage to this
	 * [WorldObject]. It then checks if this
	 * [WorldObject] is dead. If it is, this
	 * removes it from the world
	 * 
	 * @param amount the amount of damage
	 * */
	fun dealDamage(amount: Int) {
		
		if (invinsible) return // stop if it can't be destroyed
		health -= amount // subtract from the health
		
		if (isDead()) world.remove(this) // remove this if it is dead
		
	}
	
	/**
	 * Calculates if this [WorldObject]
	 * is dead.
	 * 
	 * @return true if dead, false if alive
	 * */
	fun isDead() = health <= 0
	
}
