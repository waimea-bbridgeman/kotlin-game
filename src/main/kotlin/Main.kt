import com.formdev.flatlaf.themes.FlatMacDarkLaf
import java.awt.Color
import java.awt.Font
import javax.swing.*
import javax.swing.Timer

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
    val requiredItem: String? = null

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

    val locations = mutableListOf<Location>()
    val inventory = mutableSetOf<String>()

    var timeRemaining = 3
    val fireTimer = Timer(1000, null) // countdown for the end of the game

    fun reset() {
        timeRemaining = 3
        inventory.clear()


        fireTimer.restart()

    }

    init {

        fireTimer.start()

        //setup locations to be added to the list

        val thorofareBasin = Location("Thoroughfare Basin", "A broad, forested " +
                "valley that links many trails and regions. It feels calm at first but gradually becomes more tense " +
                "as events unfold.", "To the southwest is Wapiti Meadow, and to the east is Beartooth Point.")

        val wapitiMeadow = Location("Wapiti Meadow","A bright, open field surrounded" +
                " by trees, made unsettling by the presence of a secret fenced research area. The contrast makes " +
                "it one of the eeriest spots.", "To the southeast is Mule Point, and to the northeast is Thorofare Basin." )

        val beartoothPoint = Location("Beartooth Point", "A high, rocky lookout with " +
                "wide views over the wilderness. It highlights the scale of the forest and Henry’s isolation.",
            "To the south is Two Forks Lookout, to the southwest is Wapiti Meadow, and to the west is Thorofare Basin.")

        val mulePoint = Location("Mule Point", " A quieter overlook with softer " +
                "terrain and peaceful scenery. It offers a break from the tension found in other areas.",
            "To the south is Thunder Canyon, and to the northwest is Wapiti Meadow.")

        val thunderCanyon = Location("Thunder Canyon", "A dramatic canyon with steep " +
                "walls and a rushing river below. The echoing water creates a powerful but slightly claustrophobic " +
                "atmosphere.", "To the southeast is Two Forks Lookout, to the west is Jonesy Lake, " +
                "and to the north is Mule Point.", "rope")

        val jonesyLake = Location("Jonesy Lake", " A calm, reflective lake tucked away " +
                "in the forest. It feels peaceful, though the stillness can seem a bit eerie.",
            "To the southeast is Five Mile Creek, and to the east is Thunder Canyon.")

        val fiveMileCreek = Location("Five Mile Creek", "A lively creek running through " +
                "wooded areas, adding sound and movement. It enhances the natural immersion of the environment.",
            "To the east is Cottonwood Creek, and to the northwest is Jonesy Lake.")


        val cottonWoodCreek = Location("CottonWood Creek",  "A more remote and quiet creek" +
                " in less-traveled terrain. It feels deeper in the wilderness and more isolated.",
            "To the northeast is Ruby River, and to the west is Five Mile Creek.")

        val rubyRiver = Location("Ruby River", "A large, fast-moving river that shapes " +
                "the landscape. It acts as a natural boundary and adds to the rugged feel of the area.",
            "To the north is Two Forks Lookout, and to the southwest is Cottonwood Creek.",
            "Raft",)

        val twoForksLookout = Location("Two-Forks Lookout", "A tall wooden lookout " +
                "tower with wide views over the surrounding forest.","To the north is Beartooth Point, " +
                "to the northwest is Thunder Canyon, and to the south is Ruby River.")


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

        // Two Forks Lookout
        twoForksLookout.north = beartoothPoint
        twoForksLookout.northWest = thunderCanyon
        twoForksLookout.south = rubyRiver

        // Beartooth Point
        beartoothPoint.south = twoForksLookout
        beartoothPoint.southWest = wapitiMeadow
        beartoothPoint.west = thorofareBasin

        // Thoroughfare Basin
        thorofareBasin.southWest = wapitiMeadow
        thorofareBasin.east = beartoothPoint

        // Wapiti Meadow
        wapitiMeadow.southEast = mulePoint
        wapitiMeadow.northEast = thorofareBasin

        // Mule Point
        mulePoint.south = thunderCanyon
        mulePoint.northWest = wapitiMeadow

        // Thunder Canyon
        thunderCanyon.southEast = twoForksLookout
        thunderCanyon.west = jonesyLake
        thunderCanyon.north = mulePoint

        // Jonesy Lake
        jonesyLake.southEast = fiveMileCreek
        jonesyLake.east = thunderCanyon

        // Five Mile Creek
        fiveMileCreek.east = cottonWoodCreek
        fiveMileCreek.northWest = jonesyLake

        // Cottonwood Creek
        cottonWoodCreek.northEast = rubyRiver
        cottonWoodCreek.west = fiveMileCreek

        // Ruby River
        rubyRiver.north = twoForksLookout
        rubyRiver.southWest = cottonWoodCreek


    }

}

/**
 * Main UI window, handles user clicks, etc.
 *
 * @param gameWorld the app state object
 */
class MainWindow(val gameWorld: GameWorld) {



    var currentLocation: Location = gameWorld.locations[9]

    val frame = JFrame("Firewatch Game")
    private val panel = JPanel().apply { layout = null }

    private val locationName = JLabel()

    private val descriptionText = JLabel()

    private val directionalInfo = JLabel()


    private val northButton = JButton("North")
    private val northEastButton = JButton("NE")
    private val eastButton = JButton("East")
    private val southEastButton = JButton("SE")
    private val southButton = JButton("South")
    private val southWestButton = JButton("SW")
    private val westButton = JButton("West")
    private val northWestButton = JButton("NW")



    init {
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

        if (gameWorld.timeRemaining == 0) {
            val option = JOptionPane.showConfirmDialog(
                frame,
                "You shouldn't become a firefighter. You let the forest burn down! \n \n Start a new game?"
            )

            if (option == JOptionPane.YES_OPTION) {
                gameWorld.reset()

                currentLocation = gameWorld.locations[9]
                updateUI()
            }
           else {
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
        if (destination != null) {
            currentLocation = destination
            updateUI()
        }
    }


    fun updateUI() {
        descriptionText.text = currentLocation.description

        directionalInfo.text = currentLocation.directionalInfo

        locationName.text = currentLocation.name

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


