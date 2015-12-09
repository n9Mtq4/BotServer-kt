package com.n9mtq4.botserver.world.objects.interfaces

/**
 * Created by will on 12/8/15 at 10:55 AM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
interface Entity : HealthWorldObject {
	
	override var x: Int
	override var y: Int
	var angle: Int
	
}
