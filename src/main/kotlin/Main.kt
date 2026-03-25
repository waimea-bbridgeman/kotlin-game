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
    var south: Location? = null
    var east: Location? = null
    var west: Location? = null
}


/**
 * Manage app state
 *
 */
class GameWorld {
    val locations = mutableListOf<Location>()


    init {

        //setup locations to be added to the list

        val thoroughfareBasin = Location("Thoroughfare Basin", "Meadow", "")

        val wapitiMeadow = Location("Wapiti Meadow", "Meadow", "" )

        val beartoothPoint = Location("Beartooth Point", "Meadow", "")

        val mulePoint = Location("Mule Point", "Dead forest", "")

        val thunderCanyon = Location("Thunder Canyon", "Canyon", "")

        val jonesyLake = Location("Jonesy Lake", "Lake", "")

        val fiveMileCreek = Location("Five Mile Creek", "Creek", "")

        val cottonWoodCreek = Location("CottonWood Creek", "Creek", "")

        val rubyRiver = Location("Ruby River", "River", "")

        val TwoForksLookout = Location("TwoForks Lookout", "Lookout", "")

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
        locations.add(TwoForksLookout)




    }

}


class GamePlay {
    fun currentLocation() {

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
//        app.scorePoints(1000)       // Update the app state
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