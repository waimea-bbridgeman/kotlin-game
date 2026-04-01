import com.formdev.flatlaf.themes.FlatMacDarkLaf
import java.awt.Color
import java.awt.Font
import javax.swing.*

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
    val type: String, // forest, lookout, cave, lake, etc.
    val description: String

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


    init {

        //setup locations to be added to the list

        val thorofareBasin = Location("Thoroughfare Basin", "Meadow", "A broad, forested " +
                "valley that links many trails and regions. It feels calm at first but gradually becomes more tense " +
                "as events unfold.")

        val wapitiMeadow = Location("Wapiti Meadow", "Meadow", "A bright, open field surrounded" +
                " by trees, made unsettling by the presence of a secret fenced research area. The contrast makes " +
                "it one of the eeriest spots." )

        val beartoothPoint = Location("Beartooth Point", "Meadow", "A high, rocky lookout with " +
                "wide views over the wilderness. It highlights the scale of the forest and Henry’s isolation.")

        val mulePoint = Location("Mule Point", "Dead forest", " A quieter overlook with softer " +
                "terrain and peaceful scenery. It offers a break from the tension found in other areas.")

        val thunderCanyon = Location("Thunder Canyon", "Canyon", "A dramatic canyon with steep " +
                "walls and a rushing river below. The echoing water creates a powerful but slightly claustrophobic " +
                "atmosphere.")

        val jonesyLake = Location("Jonesy Lake", "Lake", " A calm, reflective lake tucked away " +
                "in the forest. It feels peaceful, though the stillness can seem a bit eerie.")

        val fiveMileCreek = Location("Five Mile Creek", "Creek", "A lively creek running through " +
                "wooded areas, adding sound and movement. It enhances the natural immersion of the environment.")

        val cottonWoodCreek = Location("CottonWood Creek", "Creek", "A more remote and quiet creek" +
                " in less-traveled terrain. It feels deeper in the wilderness and more isolated. ")

        val rubyRiver = Location("Ruby River", "River", "A large, fast-moving river that shapes " +
                "the landscape. It acts as a natural boundary and adds to the rugged feel of the area.")

        val twoForksLookout = Location("Two-Forks Lookout", "Lookout", "Henry’s fire tower and main" +
                " base of operations. It serves as both a safe space and a symbol of solitude throughout the story.")

      //add them

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


}
/**
 * Main UI window, handles user clicks, etc.
 *
 * @param gameWorld the app state object
 */
class MainWindow(val gameWorld: GameWorld) {

    fun currentLocation(currentLocation: Location) {
        currentLocation = gameWorld.locations[9]
    }


        fun description() {
            (currentLocation.description)
        }



    val frame = JFrame("Firewatch Game")
    private val panel = JPanel().apply { layout = null }

    private val titleLabel = JLabel("Firewatch")

    private val userInput = JLabel()

    private val descriptionText = JLabel()



    init {
        setupLayout()
        setupStyles()
        setupActions()
        setupWindow()
        updateUI()
    }

    private fun setupLayout() {
        panel.preferredSize = java.awt.Dimension(1500, 800)

        titleLabel.setBounds(30, 30, 340, 30)
        userInput.setBounds(30, 90, 340, 30)
        descriptionText.setBounds(60, 50, 340, 30)

        panel.add(titleLabel)
        panel.add(userInput)
        panel.add(descriptionText)

    }


    private fun setupStyles() {
        titleLabel.font = Font(Font.SANS_SERIF, Font.BOLD, 32)
        userInput.font = Font(Font.SANS_SERIF, Font.PLAIN, 20)


    }

    private fun setupWindow() {
        frame.isResizable = false                           // Can't resize
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE  // Exit upon window close
        frame.contentPane = panel                           // Define the main content
        frame.pack()
        frame.setLocationRelativeTo(null)                   // Centre on the screen
    }

    private fun setupActions() {

    }

    private fun handleMainClick() {

        updateUI()                  // Update this window UI to reflect this
    }


    fun updateUI() {

    }

    fun show() {
        frame.isVisible = true
    }
}


