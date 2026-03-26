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

}


/**
 * Manage app state
 *
 */
class GameWorld {
    val locations = mutableListOf<Location>()


    init {

        //setup locations to be added to the list

        val thoroughfareBasin = Location("Thoroughfare Basin", "Meadow", "A broad, forested " +
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

        locations.add(thoroughfareBasin)
        locations.add(wapitiMeadow)
        locations.add(beartoothPoint)
        locations.add(mulePoint)
        locations.add(thunderCanyon)
        locations.add(jonesyLake)
        locations.add(fiveMileCreek)
        locations.add(cottonWoodCreek)
        locations.add(rubyRiver)
        locations.add(twoForksLookout)




    }


}


class GamePlay(val gameWorld: GameWorld, var currentLocation: Location){
    fun currentLocation() {
        currentLocation = gameWorld.locations[9]
    }

}
/**
 * Main UI window, handles user clicks, etc.
 *
 * @param GameWorld the app state object
 */
class MainWindow(val GameWorld: GameWorld) {
    val frame = JFrame("Firewatch Game")
    private val panel = JPanel().apply { layout = null }

    private val titleLabel = JLabel("Firewatch")

    private val infoLabel = JLabel()

    private val infoWindow = InfoWindow(this, GameWorld)      // Pass app state to dialog too

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
        infoLabel.setBounds(30, 90, 340, 30)

        panel.add(titleLabel)
        panel.add(infoLabel)

    }

    private fun setupStyles() {
        titleLabel.font = Font(Font.SANS_SERIF, Font.BOLD, 32)
        infoLabel.font = Font(Font.SANS_SERIF, Font.PLAIN, 20)


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

    private fun handleInfoClick() {
        infoWindow.show()
    }

    fun updateUI() {
//        infoLabel.text = "User ${app.name} has ${app.score} points"

//        if (app.maxScoreReached()) {
//            clickButton.text = "No More!"
//            clickButton.isEnabled = false
//        } else {
//            clickButton.text = "Click Me!"
//            clickButton.isEnabled = true
//        }

        infoWindow.updateUI()       // Keep child dialog window UI up-to-date too
    }

    fun show() {
        frame.isVisible = true
    }
}



/**
 * Info UI window is a child dialog and shows how the
 * app state can be shown / updated from multiple places
 *
 * @param owner the parent frame, used to position and layer the dialog correctly
 * @param app the app state object
 */
class InfoWindow(val owner: MainWindow, val app: GameWorld) {
    private val dialog = JDialog(owner.frame, "DIALOG TITLE", false)
    private val panel = JPanel().apply { layout = null }

    private val infoLabel = JLabel()
    private val resetButton = JButton("Reset")

    init {
        setupLayout()
        setupStyles()
        setupActions()
        setupWindow()
        updateUI()
    }

    private fun setupLayout() {
        panel.preferredSize = java.awt.Dimension(240, 180)

        infoLabel.setBounds(30, 30, 180, 60)
        resetButton.setBounds(30, 120, 180, 30)

        panel.add(infoLabel)
        panel.add(resetButton)
    }

    private fun setupStyles() {
        infoLabel.font = Font(Font.SANS_SERIF, Font.PLAIN, 16)
        resetButton.font = Font(Font.SANS_SERIF, Font.PLAIN, 16)
    }

    private fun setupWindow() {
        dialog.isResizable = false                              // Can't resize
        dialog.defaultCloseOperation = JDialog.HIDE_ON_CLOSE    // Hide upon window close
        dialog.contentPane = panel                              // Main content panel
        dialog.pack()
    }

    private fun setupActions() {
        resetButton.addActionListener { handleResetClick() }
    }

    private fun handleResetClick() {
//        app.resetScore()    // Update the app state
        owner.updateUI()    // Update the UI to reflect this, via the main window
    }

    fun updateUI() {
        // Use app properties to display state
//        infoLabel.text = "<html>User: ${app.name}<br>Score: ${app.score} points"

//        resetButton.isEnabled = app.score > 0
    }

    fun show() {
        val ownerBounds = owner.frame.bounds          // get location of the main window
        dialog.setLocation(                           // Position next to main window
            ownerBounds.x + ownerBounds.width + 10,
            ownerBounds.y
        )

        dialog.isVisible = true
    }
}