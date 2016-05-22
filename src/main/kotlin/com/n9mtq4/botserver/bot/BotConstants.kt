package com.n9mtq4.botserver.bot

/**
 * Created by will on 12/8/15 at 4:45 PM.
 * 
 * @property BOT_HEALTH the max health of the bot
 * @property DELTA_BOT_HEALTH how much the bot heals per turn
 * @property FOV the angle of the bot's vision
 * 
 * @property SHOOT_DAMAGE how much damage a shot deals
 * 
 * @property DEFAULT_ACTION_POINTS how many action points a bot gets
 * @property DEFAULT_MANA how much mana a team starts with
 * @property MAX_MANA how much mana a team can possibly have
 * @property DELTA_MANA how much mana is added per turn
 * 
 * @property MOVE_COST how much it costs to move the bot (action points)
 * @property TURN_COST how much it costs to turn (degree / [TURN_COST]) (action points)
 * @property SHOOT_COST how much it costs to shoot (action points)
 * @property PLACE_COST how much it costs to place a block (mana)
 * @property SPAWN_COST how much it costs to spawn a bot (mana)
 * @property SPAWN_MANA_COST The amount of mana that it costs to spawn a new bot
 * @property PLACE_MANA_COST The amount of mana that it costs to place a new block
 * 
 * @author Will "n9Mtq4" Bresnahan
 */
val FOV = 90 // Unused
val VIEW_DISTANCE = 50

val SHOOT_DAMAGE = 50

val BOT_HEALTH = 100
val DELTA_BOT_HEALTH = 1
val MAX_BOT_HEALTH = 150

val DEFAULT_ACTION_POINTS = 10
val DEFAULT_MANA = 100
val MAX_MANA = 100
val DELTA_MANA = 5

val MOVE_COST = 1
val TURN_COST = 20 // higher is lower - totalCost = angle / TURN_COST
val SHOOT_COST = 1
val PLACE_COST = 10
val SPAWN_COST = 10
val SPAWN_MANA_COST = 50
val PLACE_MANA_COST = 20
