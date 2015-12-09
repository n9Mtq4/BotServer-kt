package com.n9mtq4.botserver.bot

import com.n9mtq4.botserver.world.objects.interfaces.WorldObject

/**
 * Created by will on 12/8/15 at 6:46 PM.
 * 
 * A [WorldObject] that has been seen.
 * Contains a [WorldObject] along with
 * the x and y positions.
 * 
 * This is a work around for [WorldNothing] and [Wall]
 * been singletons, and therefore no distinguishing factors.
 * 
 * @author Will "n9Mtq4" Bresnahan
 */
data class SeenWorldObject(val obj: WorldObject, val x: Int, val y: Int)
