package o1.adventure

import scala.collection.mutable.Map


/** A `Player` object represents a player character controlled by the real-life user of the program.
  *
  * A player object's state is mutable: the player's location and possessions can change, for instance.
  *
  * @param startingArea  the initial location of the player */
class Player(startingArea: Area) {

  private var currentLocation = startingArea        // gatherer: changes in relation to the previous location
  private var quitCommandGiven = false              // one-way flag
  private var items = Map[String, Item]()
  private var dead = false
  private var grownUp = 0

  /** Determines if the player has indicated a desire to quit the game. */
  def hasQuit = this.quitCommandGiven

  def hasDied = this.dead

  def hasGrownUp = this.grownUp >= 3

  /** Returns the current location of the player. */
  def location = this.currentLocation

  /** Attempts to move the player in the given direction. This is successful if there
    * is an exit from the player's current location towards the direction name. Returns
    * a description of the result: "You go DIRECTION." or "You can't go DIRECTION." */
  def go(direction: String) = {
    val destination = this.location.neighbor(direction)
    this.currentLocation = destination.getOrElse(this.currentLocation)
    if (destination.isDefined) "You go " + direction + "." else "You can't go " + direction + "."
  }


  /** Causes the player to rest for a short while (this has no substantial effect in game terms).
    * Returns a description of what happened. */
  def dance() = {
    "You have awesome moves! You should be a professional dancer."
  }


  /** Signals that the player wants to quit the game. Returns a description of what happened within
    * the game as a result (which is the empty string, in this case). */
  def quit() = {
    this.quitCommandGiven = true
    ""
  }

  def drop(itemName: String) = {
    if (this.has(itemName)) {
      this.currentLocation.addItem(this.items.remove(itemName).get)
      "You drop the " + itemName + "."
    } else
       "You don't have that!"
  }

  def examine(itemName: String) = {
    if (this.has(itemName))
      "You look closely at the " + itemName + ".\n" + this.items(itemName).description
    else
      "If you want to examine something, you need to pick it up first."
  }

  def get(itemName: String) = {
    if (this.currentLocation.contains(itemName)) {
      this.items += itemName -> this.currentLocation.removeItem(itemName).get
      "You pick up the " + itemName + "."
    } else
      "There is no " + itemName + " here to pick up."
  }

  def has(itemName: String) = this.items.contains(itemName)

  def inventory = {
    if (this.items.nonEmpty)
      "Wow, what a strong creature! You are carrying: \n" + this.items.keys.mkString("\n")
    else
      "You have nothing with you."
  }

  def use(itemName: String) = {
    if (this.items.contains(itemName)) {
    if (itemName.contains("bottle")) {
      this.items.remove(itemName)
      var poison = ""
      if (itemName.contains("red")) {
        this.grownUp += 1
        poison = "It was growing liquid, good job."
      }
      else if (itemName.contains("green")) {
        this.dead = true
        poison = "It was poison, poor you. You die now."
      }
      val emptyNumber = this.items.count(_._1.contains("empty bottle")) + 1 //MIETI TÄN TOTEUTUS, pitäskö alkuperäsetki pullot numeroida?
      this.items += "empty bottle " + emptyNumber -> new Item("empty bottle " + emptyNumber, "You can clearly see through this bottle. There's nothing inside.")
      "You drank the whole bottle! " + poison
    } else
      "You can't use this."
    }
    else
      "You don't have that one."

  }

  def detectWolf(direction: String) = {
    val destination = this.location.neighbor(direction)
    if (destination.isDefined) {
     if (destination.get.hasWolf)
      "There is a wolf in " + direction + "."
     else
       "There is no wolf in " + direction + "."
    } else if (direction == "north" || direction == "east" || direction == "south" || direction == "west")
      "There's nothing in " + direction + "."
    else
      "Give a direction. That wasn't a direction."
  }



  def help() = {
      "Here are some instructions how to play this game. \n" +
      "To win you have to collect 3 non-poisonous bottles, drink all 3 of them and get home. \n" +
      "Here are some commands that you can give: \n" +
      "go 'exit'       go to this direction, eg. go north\n" +
      "dance           just dance\n" +
      "quit            quit the game\n" +
      "get 'item'      pick up an item in the current location, eg. get bottle\n" +
      "drop 'item'     drop the item to this area. eq. drop bottle\n" +
      "examine 'item'  get an description about the item. eg. examine bottle\n" +
      "inventory       get a list of items that you're carrying. \n" +
      "use 'item'      drink the liquid inside the bottle, watch out, because you might die!\n" +
      "detect 'exit'   see if the wolf is in some direction. IF the wolf isn't in the given direction, it's safe to move there.\n" +
      "help            get this list."
  }

  /** Returns a brief description of the player's state, for debugging purposes. */
  override def toString = "Now at: " + this.location.name


}


