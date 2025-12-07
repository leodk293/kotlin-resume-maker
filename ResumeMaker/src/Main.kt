package cvbuilder

import javax.swing.SwingUtilities

fun main() {
    SwingUtilities.invokeLater {
        MainWindow().isVisible = true
    }
}