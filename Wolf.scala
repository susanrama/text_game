package o1.adventure


/* This wolf goes between mountain and walley,
you will die if you come in the same area with it */
class Wolf(startingArea: Area) {

  private var currentLocation = startingArea
  private val round = Vector("east", "west")
  private var number = 0

  def location = this.currentLocation

  def turn(): Unit = {
    this.currentLocation.removeWolf()
    this.currentLocation = this.location.neighbor(round(number)).get
    this.currentLocation.addWolf()
    if (number == 0) number = 1 else number = 0
  }
}

