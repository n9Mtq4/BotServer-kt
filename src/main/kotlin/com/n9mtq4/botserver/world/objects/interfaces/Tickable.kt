package com.n9mtq4.botserver.world.objects.interfaces

/**
 * Created by will on 12/9/15 at 10:00 AM.
 * 
 * Interface that adds support for ticking in the world.
 * 
 * @author Will "n9Mtq4" Bresnahan
 */
interface Tickable {
	
	/**
	 * A method that is called every turn.
	 * */
	fun tick()
	
}
