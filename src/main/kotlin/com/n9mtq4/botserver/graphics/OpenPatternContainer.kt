package com.n9mtq4.botserver.graphics

import com.n9mtq4.patternimage.PatternImage
import com.n9mtq4.patternimage.ui.PatternImageContainer

/**
 * Created by will on 5/20/16 at 3:47 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class OpenPatternContainer(patternImage: PatternImage, debug: Boolean = false, ticksPerSecond: Double = PatternImageContainer.TICKS_DEFAULT, scale: Int = 1) : 
		PatternImageContainer(patternImage, debug, ticksPerSecond, scale) {
	
	public override fun render() {
		super.render()
	}
	
	public override fun tick() {
		super.tick()
	}
	
}
