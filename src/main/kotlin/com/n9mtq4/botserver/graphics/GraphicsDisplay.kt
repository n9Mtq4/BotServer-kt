package com.n9mtq4.botserver.graphics

import com.n9mtq4.botserver.graphics.constants.BACKGROUND
import com.n9mtq4.botserver.graphics.constants.BOX
import com.n9mtq4.botserver.graphics.constants.DISPLAY_HEIGHT
import com.n9mtq4.botserver.graphics.constants.DISPLAY_WIDTH
import com.n9mtq4.botserver.graphics.constants.FLAG_COLOR
import com.n9mtq4.botserver.graphics.constants.SCALE
import com.n9mtq4.botserver.graphics.constants.TEAM_COLOR
import com.n9mtq4.botserver.graphics.layers.GridBackground
import com.n9mtq4.botserver.graphics.layers.PixelRender
import com.n9mtq4.botserver.world.World
import com.n9mtq4.botserver.world.objects.WorldNothing
import com.n9mtq4.patternimage.Pattern
import com.n9mtq4.patternimage.PatternImage
import com.n9mtq4.patternimage.colorgetter.StaticColor
import java.awt.Rectangle
import javax.swing.JFrame

/**
 * Created by will on 11/17/15 at 2:45 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class GraphicsDisplay : PatternImage(DISPLAY_WIDTH * SCALE, DISPLAY_HEIGHT * SCALE), GameDisplayer {
	
	val frame: JFrame
	val patternContainer: OpenPatternContainer
	
	val pixelRender: PixelRender
	
	var gameEnded: Boolean
	var tick: Int
	
	/*
	* Initializes the frame
	* */
	init {
		
		this.tick = 0
		this.gameEnded = false
		
		this.frame = JFrame("Bot Game")
//		TODO: change back to true
		this.patternContainer = OpenPatternContainer(this, true, 0.0)
		this.patternContainer.isCapFps = true
		
		frame.add(patternContainer)
		
		frame.pack()
		frame.isVisible = true
		frame.setLocationRelativeTo(null)
		
	}
	
	/*
	* Adds the rendering components
	* */
	init {
		
		setBackground(when {
			SCALE > 1 -> GridBackground()
			else -> StaticColor(BACKGROUND)
		})
		
		this.pixelRender = PixelRender(this)
		addPattern(Pattern(pixelRender, Rectangle(0, 0, width, height)))
		
	}
	
	/**
	 * Starts rendering the game
	 * */
	fun start() {
		patternContainer.start()
	}
	
	override fun render(world: World, turnLog: List<String>) {
		
		pixelRender.clear()
		
//		map
		world.mapData.forEachIndexed { i, it -> 
			
			if (it is WorldNothing) return@forEachIndexed
			
			when (it.id) {
				1 -> pixelRender.setPixel(i, TEAM_COLOR[1]) // team 1
				2 -> pixelRender.setPixel(i, TEAM_COLOR[2]) // team 2
				3 -> pixelRender.setPixel(i, BOX) // box
				4 -> pixelRender.setPixel(i, BOX) // wall
				5 -> pixelRender.setPixel(i, FLAG_COLOR) // flag
				else -> println("ERROR: unknown value in map: '$it'") // other!
			}
			
		}
		
//		shooting data
		turnLog.forEach { 
			it.split(",").map { it.map { it.toInt() } }. // parses them all as ints
					forEach { // then goes through all the array of 3 ints
//						an array that looks like {x, y, deg}
						pixelRender.drawLine(it[0], it[1], it[2])
					}
		}
		
		patternContainer.render()
		patternContainer.tick()
		
	}
	
	/**
	 * Called when the game has ended
	 * */
	override fun gameOver(win: Int) {
		
		this.gameEnded = true // make sure everyone knows the game is over - stops the tick method
		println("Game is over! Data: $win") // print out info
		
		addPattern(Pattern(StaticColor(TEAM_COLOR[win]), Rectangle(0, 0, width, height))) // render the color of that team
		
		patternContainer.render()
		patternContainer.tick()
		
	}
	
}
