package com.n9mtq4.botserver.world.objects.interfaces

/**
 * Created by will on 12/8/15 at 10:55 AM.
 * 
 * The interface of entities.
 * Contains an angle in degrees of
 * where the entity is facing.
 * 
 * @author Will "n9Mtq4" Bresnahan
 */
interface Entity : HealthWorldObject {
	
	/**
	 * The angle in degrees of where the
	 * entity is facing -360 to 360.
	 * */
	var angle: Int
	
}
