import com.formdev.flatlaf.themes.FlatMacDarkLaf
import java.awt.Color
import java.awt.Font
import javax.swing.*
import javax.swing.Timer
import java.awt.KeyboardFocusManager
import java.awt.event.KeyEvent

fun ImageIcon.scaled(width: Int, height: Int): ImageIcon =
    ImageIcon(image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH))

/**
 * Application entry point
 */
fun main() {
    FlatMacDarkLaf.setup()          // Initialise the LAF

    val app = GameWorld()                 // Get an app state object
    val window = MainWindow(app)    // Spawn the UI, passing in the app state

    SwingUtilities.invokeLater { window.show() }
}


class Location(
    val name: String,
    val description: String,
    val directionalInfo: String,
    var requiredItem: String? = null,
    var itemHere: String? = null

) {
    var north: Location? = null
    var northEast: Location? = null
    var east: Location? = null
    var southEast: Location? = null
    var south: Location? = null
    var southWest: Location? = null
    var west: Location? = null
    var northWest: Location? = null
}


/**
 * Manage app state
 *
 */
class GameWorld {
    //Sets locations, starts the timer for the fire, places required items into a random location

    val locations = mutableListOf<Location>()
    val inventory = mutableSetOf<String>()
    val items = mutableListOf<String>()

    var fireLocation: Location = Location("", "", "")

    var timeRemaining = 300
    val fireTimer = Timer(1000, null) // countdown for the end of the game

    var bucketFull = false



    fun itemsRandom() {
        items.add("Rope")
        items.add("Bucket")
        items.add("Raft")

        //List of locations a required item can be at without putting them somewhere that needs a required item to enter
        val eligible = mutableListOf<Location>()
        for (location in locations) {
            if (location.requiredItem == null) {
                eligible.add(location)
            }
        }

        // Place each item at a random eligible location and removing it so that you don't have two items in the same place
        for (item in items) {
            val spot = eligible.random()
            spot.itemHere = item
            eligible.remove(spot)
        }
    }

    //Save the last picked up item so that when i remove it from the location (so the message doesn't repeat everytime)
    //it can be used by MainWindow to say "you picked up ..."
    var lastPickedUp = ""

    fun tryMove(destination: Location): String {
        val required = destination.requiredItem
        if (required != null && !inventory.contains(required)) {
            return "BLOCKED"
        }

        val found = destination.itemHere
        if (found != null) {
            inventory.add(found)
            destination.itemHere = null
            lastPickedUp = found
            return "PICKED UP"
        }

        if (destination.name == "Jonesy Lake" && inventory.contains("bucket")) {
            bucketFull = true
            return "BUCKET FILLED"
        }

        if (destination == fireLocation && !bucketFull) {
            return "FIRE"
        }

        if (destination == fireLocation && bucketFull) {
            return "WIN"
        }

        return "MOVED"
    }

    fun reset() {
        //Re-pick the fire location
        val fireEligible = mutableListOf<Location>()
        for (location in locations) {
            if (location.requiredItem == null) {
                fireEligible.add(location)
            }
        }
        fireLocation = fireEligible.random()

        //Clear where the items are
        for (location in locations) {
            location.itemHere = null
        }

        inventory.clear()
        bucketFull = false
        itemsRandom()
        timeRemaining = 300
        fireTimer.restart()

    }

        init {

            fireTimer.start()


            //setup locations to be added to the list

            val thorofareBasin = Location(
                "Thoroughfare Basin", "A broad, forested " +
                        "valley that links many trails and regions. It feels calm at first but gradually becomes more tense " +
                        "as events unfold.", "To the southwest is Wapiti Meadow, and to the east is Beartooth Point."
            )

            val wapitiMeadow = Location(
                "Wapiti Meadow",
                "A bright, open field surrounded" +
                        " by trees, made unsettling by the presence of a secret fenced research area. The contrast makes " +
                        "it one of the eeriest spots.",
                "To the southeast is Mule Point, and to the northeast is Thorofare Basin."
            )

            val beartoothPoint = Location(
                "Beartooth Point", "A high, rocky lookout with " +
                        "wide views over the wilderness. It highlights the scale of the forest and Henry’s isolation.",
                "To the south is Two Forks Lookout, to the southwest is Wapiti Meadow, and to the west is Thorofare Basin."
            )

            val mulePoint = Location(
                "Mule Point", " A quieter overlook with softer " +
                        "terrain and peaceful scenery. It offers a break from the tension found in other areas.",
                "To the south is Thunder Canyon, and to the northwest is Wapiti Meadow."
            )

            val thunderCanyon = Location(
                "Thunder Canyon", "A dramatic canyon with steep " +
                        "walls and a rushing river below. The echoing water creates a powerful but slightly claustrophobic " +
                        "atmosphere.", "To the southeast is Two Forks Lookout, to the west is Jonesy Lake, " +
                        "and to the north is Mule Point.", "rope"
            )

            val jonesyLake = Location(
                "Jonesy Lake", " A calm, reflective lake tucked away " +
                        "in the forest. It feels peaceful, though the stillness can seem a bit eerie.",
                "To the southeast is Five Mile Creek, and to the east is Thunder Canyon."
            )

            val fiveMileCreek = Location(
                "Five Mile Creek", "A lively creek running through " +
                        "wooded areas, adding sound and movement. It enhances the natural immersion of the environment.",
                "To the east is Cottonwood Creek, and to the northwest is Jonesy Lake."
            )


            val cottonWoodCreek = Location(
                "CottonWood Creek", "A more remote and quiet creek" +
                        " in less-traveled terrain. It feels deeper in the wilderness and more isolated.",
                "To the northeast is Ruby River, and to the west is Five Mile Creek."
            )

            val rubyRiver = Location(
                "Ruby River",
                "A large, fast-moving river that shapes " +
                        "the landscape. It acts as a natural boundary and adds to the rugged feel of the area.",
                "To the north is Two Forks Lookout, and to the southwest is Cottonwood Creek.",
                "Raft",
            )

            val twoForksLookout = Location(
                "Two-Forks Lookout", "A tall wooden lookout " +
                        "tower with wide views over the surrounding forest.", "To the north is Beartooth Point, " +
                        "to the northwest is Thunder Canyon, and to the south is Ruby River."
            )


            locations.add(thorofareBasin)
            locations.add(wapitiMeadow)
            locations.add(beartoothPoint)
            locations.add(mulePoint)
            locations.add(thunderCanyon)
            locations.add(jonesyLake)
            locations.add(fiveMileCreek)
            locations.add(cottonWoodCreek)
            locations.add(rubyRiver)
            locations.add(twoForksLookout)

            //Connect all the locations together
            twoForksLookout.north = beartoothPoint
            twoForksLookout.northWest = thunderCanyon
            twoForksLookout.south = rubyRiver

            beartoothPoint.south = twoForksLookout
            beartoothPoint.southWest = wapitiMeadow
            beartoothPoint.west = thorofareBasin

            thorofareBasin.southWest = wapitiMeadow
            thorofareBasin.east = beartoothPoint

            wapitiMeadow.southEast = mulePoint
            wapitiMeadow.northEast = thorofareBasin

            mulePoint.south = thunderCanyon
            mulePoint.northWest = wapitiMeadow

            thunderCanyon.southEast = twoForksLookout
            thunderCanyon.west = jonesyLake
            thunderCanyon.north = mulePoint

            jonesyLake.southEast = fiveMileCreek
            jonesyLake.east = thunderCanyon

            fiveMileCreek.east = cottonWoodCreek
            fiveMileCreek.northWest = jonesyLake

            cottonWoodCreek.northEast = rubyRiver
            cottonWoodCreek.west = fiveMileCreek

            rubyRiver.north = twoForksLookout
            rubyRiver.southWest = cottonWoodCreek

            itemsRandom()

            //Pick a place for the fire to be, making sure that if an item is required the fire can't spawn there
            val fireEligible = mutableListOf<Location>()
            for (location in locations) {
                if (location.requiredItem == null) {
                    fireEligible.add(location)
                }
            }
            fireLocation = fireEligible.random()
        }
}
    /**
     * Main UI window, handles user clicks, etc.
     *
     * @param gameWorld the app state object
     */
    class MainWindow(val gameWorld: GameWorld) {

        //if the 'i' key is press instructions appear for the user
        private fun keyPress() {
            KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher { i ->
                    if (i.id == KeyEvent.KEY_PRESSED) {
                        instructions(i.keyCode)
                    }
                    false
                }
        }

        //The message that appears after 'i' is pressed
        private fun instructions(keyCode: Int) {
            when (keyCode) {
                KeyEvent.VK_I -> JOptionPane.showMessageDialog(
                    frame,
                    "A fire has started at a random location on the map! Your goal is to find a bucket and fill it " +
                            "up at the lake and reach the fire before the forest burns down. " +
                            "The bucket can be found at a random location across the map."
                )

            }
        }



        var currentLocation: Location = gameWorld.locations[9]

        val mapIcon = ImageIcon(ClassLoader.getSystemResource("images/firewatchMap.jpg")).scaled(400,400)


        val frame = JFrame("Firewatch Game")
        private val panel = JPanel().apply { layout = null }

        private val locationName = JLabel()
        private val descriptionText = JLabel()
        private val directionalInfo = JLabel()
        private val mapLabel = JLabel(mapIcon)

        private val instructionLabel = JLabel("I")

        private val northButton = JButton("North")
        private val northEastButton = JButton("NE")
        private val eastButton = JButton("East")
        private val southEastButton = JButton("SE")
        private val southButton = JButton("South")
        private val southWestButton = JButton("SW")
        private val westButton = JButton("West")
        private val northWestButton = JButton("NW")


        init {
            keyPress()
            setupLayout()
            setupStyles()
            setupActions()
            setupWindow()
            updateUI()
        }

        private fun setupLayout() {
            panel.preferredSize = java.awt.Dimension(1500, 800)

            locationName.setBounds(150, 30, 1000, 120)
            descriptionText.setBounds(150, 120, 1000, 120)
            directionalInfo.setBounds(150, 160, 1000, 120)
            instructionLabel.setBounds(150, 700, 400, 40)
            mapLabel.setBounds(1050, 30, 400, 300)

            northWestButton.setBounds(600, 450, 90, 40)
            northButton.setBounds(700, 450, 90, 40)
            northEastButton.setBounds(800, 450, 90, 40)
            westButton.setBounds(600, 510, 90, 40)
            southButton.setBounds(700, 570, 90, 40)
            eastButton.setBounds(800, 510, 90, 40)
            southWestButton.setBounds(600, 570, 90, 40)
            southEastButton.setBounds(800, 570, 90, 40)


            panel.add(locationName)
            panel.add(descriptionText)
            panel.add(directionalInfo)
            panel.add(instructionLabel)
            panel.add(mapLabel)


            panel.add(northButton)
            panel.add(northEastButton)
            panel.add(eastButton)
            panel.add(southEastButton)
            panel.add(southButton)
            panel.add(southWestButton)
            panel.add(westButton)
            panel.add(northWestButton)

        }


        private fun setupStyles() {
            locationName.font = Font(Font.SANS_SERIF, Font.BOLD, 32)
            descriptionText.font = Font(Font.SANS_SERIF, Font.PLAIN, 20)
            directionalInfo.font = Font(Font.SANS_SERIF, Font.PLAIN, 20)
            instructionLabel.font = Font(Font.SANS_SERIF, Font.PLAIN, 20)

        }

        private fun setupWindow() {
            frame.isResizable = false                           // Can't resize
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE  // Exit upon window close
            frame.contentPane = panel                           // Define the main content
            frame.pack()
            frame.setLocationRelativeTo(null)                   // Centre on the screen
        }


        private fun handleClockTick() {
            gameWorld.timeRemaining--

            updateUI()

            //if you run out of time it lets the user play again or exit
            if (gameWorld.timeRemaining == 0) {
                val option = JOptionPane.showConfirmDialog(
                    frame,
                    "You shouldn't become a firefighter. You let the forest burn down! \n \n Start a new game?"
                )

                if (option == JOptionPane.YES_OPTION) {
                    gameWorld.reset()

                    currentLocation = gameWorld.locations[9]
                    updateUI()
                } else {
                    frame.dispose()
                }
            }
        }

        private fun setupActions() {
            gameWorld.fireTimer.addActionListener { handleClockTick() }
            northButton.addActionListener { handleButtonClick(currentLocation.north) }
            northEastButton.addActionListener { handleButtonClick(currentLocation.northEast) }
            eastButton.addActionListener { handleButtonClick(currentLocation.east) }
            southEastButton.addActionListener { handleButtonClick(currentLocation.southEast) }
            southButton.addActionListener { handleButtonClick(currentLocation.south) }
            southWestButton.addActionListener { handleButtonClick(currentLocation.southWest) }
            westButton.addActionListener { handleButtonClick(currentLocation.west) }
            northWestButton.addActionListener { handleButtonClick(currentLocation.northWest) }

        }

        private fun handleButtonClick(destination: Location?) {
            if (destination == null) return

            val result = gameWorld.tryMove(destination)

            val required = destination.requiredItem

            if (result == "BLOCKED") {
                JOptionPane.showMessageDialog(frame, "You need a $required to go there.")
                return
            }

            currentLocation = destination

            if (result == "PICKED UP") {
                JOptionPane.showMessageDialog(frame, "You picked up: ${gameWorld.lastPickedUp}")
            }

            if (result == "BUCKET FILLED") {
                JOptionPane.showMessageDialog(frame, "You filled the bucket with water!")
            }

            if (result == "WIN") {
                gameWorld.fireTimer.stop()
                val option = JOptionPane.showConfirmDialog(frame, "You saved the forest! Play again?")
                if (option == JOptionPane.YES_OPTION) {
                    gameWorld.reset()
                    currentLocation = gameWorld.locations[9]
                } else {
                    frame.dispose()
                }
            }
            if (result == "FIRE") {
                JOptionPane.showMessageDialog(frame, "You found the fire! You need a full bucket of water to put it out.")
            }

            updateUI()
        }


        fun updateUI() {
            descriptionText.text = currentLocation.description

            directionalInfo.text = currentLocation.directionalInfo

            locationName.text = currentLocation.name

            instructionLabel.text = "Press i for instructions"

            northButton.isEnabled = currentLocation.north != null
            northEastButton.isEnabled = currentLocation.northEast != null
            eastButton.isEnabled = currentLocation.east != null
            southEastButton.isEnabled = currentLocation.southEast != null
            southButton.isEnabled = currentLocation.south != null
            southWestButton.isEnabled = currentLocation.southWest != null
            westButton.isEnabled = currentLocation.west != null
            northWestButton.isEnabled = currentLocation.northWest != null

        }

        fun show() {
            frame.isVisible = true
        }
    }



