package o1.adventure


/** The class `Adventure` represents text adventure games. An adventure consists of a player and
  * a number of areas that make up the game world. It provides methods for playing the game one
  * turn at a time and for checking the state of the game.
  *
  * N.B. This version of the class has a lot of "hard-coded" information which pertain to a very
  * specific adventure game that involves a small trip through a twisted forest. All newly created
  * instances of class `Adventure` are identical to each other. To create other kinds of adventure
  * games, you will need to modify or replace the source code of this class. */
class Adventure {

  /** The title of the adventure game. */
  val title = "An Ameba Adventure"

  private val middle      = new Area("Lake", "You are swimming in a lake. You can only see water around you and you hear nothing, because you're an AMEBA!")
  private val northGrass= new Area("Grass", "You lay down on a grass and can see beautiful sky. Or can you?")
  private val southGrass = new Area("Grass", "Grass, grass, grass, but where is home? Nobody knows, good luck!")
  private val lake    = new Area("Lake", "Oh yes, you found a lake! Good to be surrounded by water.")
  private val porch     = new Area("Porch", "What is all this wood. Now you see it's a porch. You must be near home.")
  private val home        = new Area("Home", "Finally home! Now you just need the growing poison, go find it or drink all 3 of them!")
  private val livingRoom = new Area("Living Room", "Oh you found a room with a tv. It says that Biden wins!")
  private val mountain = new Area("Mountain", "So high, you got yourself up on a mountain. Quite a view! Watch out for the wolf.")
  private val valley = new Area("Valley", "Humans are scared of the mighty wolf here. You better not to stay here for too long.")
  private val destination = home

      middle.setNeighbors(Vector("north" -> northGrass, "east" -> porch, "south" -> southGrass, "west" -> lake   ))
  northGrass.setNeighbors(Vector("north" -> mountain,   "east" -> porch,                        "west" -> lake   ))
  southGrass.setNeighbors(Vector(                       "east" -> porch, "south" -> southGrass, "west" -> lake   ))
        lake.setNeighbors(Vector("north" -> northGrass,                  "south" -> southGrass, "west" -> southGrass))
       porch.setNeighbors(Vector("north" -> northGrass, "east" -> home,  "south" -> southGrass, "west" -> lake   ))
         home.setNeighbors(Vector("north" -> livingRoom,                                        "west" -> porch  ))
   livingRoom.setNeighbors(Vector(                                       "south" -> home                         ))
     mountain.setNeighbors(Vector("north"-> southGrass, "east" -> valley,"south" -> northGrass                   ))
       valley.setNeighbors(Vector(                                       "south" -> northGrass, "west" -> mountain))

  lake.addItem(new Item("little red bottle", "It's a bottle with some weird liquid inside. It might be dangerous."))
  porch.addItem(new Item("large red bottle", "It's a bottle with some weird liquid inside. It might be dangerous."))
  southGrass.addItem(new Item("red bottle", "It's a bottle with some weird liquid inside. It might be dangerous."))
  livingRoom.addItem(new Item("green bottle", "It's a bottle with some weird liquid inside. It might be dangerous."))

   /** The character that the player controls in the game. */
  val player = new Player(middle)
  val wolf = new Wolf(mountain)

  /** The number of turns that have passed since the start of the game. */
  var turnCount = 0
  /** The maximum number of turns that this adventure game allows before time runs out. */
  val timeLimit = 50


  /** Determines if the adventure is complete, that is, if the player has won. */
  def isComplete = this.player.location == this.destination && this.player.hasGrownUp

  /** Determines whether the player has won, lost, or quit, thereby ending the game. */
  def isOver = this.isComplete || this.player.hasQuit || this.turnCount == this.timeLimit || this.player.hasDied || this.player.location == this.wolf.location

  /** Returns a message that is to be displayed to the player at the beginning of the game. */
  def welcomeMessage = "You're an ameba and want to be a human\n" +
    "Welcome to find your way to humanity and home. \n" +
    "Grow up by drinking 3 correct bottles (Not the wrong ones. They'll kill you.) \n" +
    "and find home, but you better hurry, because the time is limited!\n" +
    "Also watch out for the wolf, because it will surely kill you. It has been starving for many months now.\n" +
    "To see possible commands type 'help'"


  /** Returns a message that is to be displayed to the player at the end of the game. The message
    * will be different depending on whether or not the player has completed their quest. */
  def goodbyeMessage = {
    if (this.isComplete)
      "You found home! I'm so glad to see you like that in here."
    else if (this.turnCount == this.timeLimit)
      "Time's up. :(( What a loser with no orienteering skills. \nGame over!"
    else if (this.player.hasQuit)
      "Oh, I wouldn't quit!\nGame over!"
    else if (this.player.hasDied)
      "Damn, it was the wrong poison.\nGame over!"
    else
      "You hit the wolf. \nGame over!"
  }


  /** Plays a turn by executing the given in-game command, such as "go west". Returns a textual
    * report of what happened, or an error message if the command was unknown. In the latter
    * case, no turns elapse. */
  def playTurn(command: String) = {
    val action = new Action(command)
    val outcomeReport = action.execute(this.player)
    if (outcomeReport.isDefined) {
      this.turnCount += 1
      this.wolf.turn()
      outcomeReport.get
    } else
      "Unknown command: \"" + command + "\"."
  }

}

