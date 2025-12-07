package cvbuilder

import java.awt.*
import javax.swing.*

class MainWindow : JFrame("Créateur de CV") {
    private val cv = CV()

    // Champs de saisie
    private val nomField = JTextField(20)
    private val prenomField = JTextField(20)
    private val emailField = JTextField(20)
    private val telephoneField = JTextField(20)
    private val adresseField = JTextField(20)
    private val competencesArea = JTextArea(5, 20)

    // Listes pour expériences et formations
    private val experiencesListModel = DefaultListModel<String>()
    private val formationsListModel = DefaultListModel<String>()

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(800, 600)
        setLocationRelativeTo(null)

        layout = BorderLayout()

        // Panel principal avec scroll
        val mainPanel = JPanel()
        mainPanel.layout = BoxLayout(mainPanel, BoxLayout.Y_AXIS)

        // Section Informations personnelles
        mainPanel.add(createSection("Informations personnelles", createInfoPanel()))

        // Section Expériences
        mainPanel.add(createSection("Expériences professionnelles", createExperiencePanel()))

        // Section Formations
        mainPanel.add(createSection("Formations", createFormationPanel()))

        // Section Compétences
        mainPanel.add(createSection("Compétences", createCompetencesPanel()))

        val scrollPane = JScrollPane(mainPanel)
        add(scrollPane, BorderLayout.CENTER)

        // Boutons en bas
        add(createButtonPanel(), BorderLayout.SOUTH)
    }

    private fun createSection(titre: String, panel: JPanel): JPanel {
        val section = JPanel()
        section.layout = BorderLayout()
        section.border = BorderFactory.createTitledBorder(titre)
        section.add(panel, BorderLayout.CENTER)
        return section
    }

    private fun createInfoPanel(): JPanel {
        val panel = JPanel(GridLayout(5, 2, 5, 5))

        panel.add(JLabel("Nom:"))
        panel.add(nomField)
        panel.add(JLabel("Prénom:"))
        panel.add(prenomField)
        panel.add(JLabel("Email:"))
        panel.add(emailField)
        panel.add(JLabel("Téléphone:"))
        panel.add(telephoneField)
        panel.add(JLabel("Adresse:"))
        panel.add(adresseField)

        return panel
    }

    private fun createExperiencePanel(): JPanel {
        val panel = JPanel(BorderLayout())

        val list = JList(experiencesListModel)
        panel.add(JScrollPane(list), BorderLayout.CENTER)

        val btnPanel = JPanel()
        val btnAdd = JButton("Ajouter")
        btnAdd.addActionListener { ajouterExperience() }
        btnPanel.add(btnAdd)

        panel.add(btnPanel, BorderLayout.SOUTH)

        return panel
    }

    private fun createFormationPanel(): JPanel {
        val panel = JPanel(BorderLayout())

        val list = JList(formationsListModel)
        panel.add(JScrollPane(list), BorderLayout.CENTER)

        val btnPanel = JPanel()
        val btnAdd = JButton("Ajouter")
        btnAdd.addActionListener { ajouterFormation() }
        btnPanel.add(btnAdd)

        panel.add(btnPanel, BorderLayout.SOUTH)

        return panel
    }

    private fun createCompetencesPanel(): JPanel {
        val panel = JPanel(BorderLayout())
        competencesArea.lineWrap = true
        competencesArea.wrapStyleWord = true
        panel.add(JScrollPane(competencesArea), BorderLayout.CENTER)
        return panel
    }

    private fun createButtonPanel(): JPanel {
        val panel = JPanel()

        val btnGenerer = JButton("Générer PDF")
        btnGenerer.addActionListener { genererPDF() }

        panel.add(btnGenerer)

        return panel
    }

    private fun ajouterExperience() {
        val dialog = JDialog(this, "Ajouter une expérience", true)
        dialog.layout = GridLayout(6, 2, 5, 5)

        val posteField = JTextField()
        val entrepriseField = JTextField()
        val dateDebutField = JTextField()
        val dateFinField = JTextField()
        val descriptionArea = JTextArea(3, 20)

        dialog.add(JLabel("Poste:"))
        dialog.add(posteField)
        dialog.add(JLabel("Entreprise:"))
        dialog.add(entrepriseField)
        dialog.add(JLabel("Date début:"))
        dialog.add(dateDebutField)
        dialog.add(JLabel("Date fin:"))
        dialog.add(dateFinField)
        dialog.add(JLabel("Description:"))
        dialog.add(JScrollPane(descriptionArea))

        val btnOk = JButton("OK")
        btnOk.addActionListener {
            val exp = Experience(
                posteField.text,
                entrepriseField.text,
                dateDebutField.text,
                dateFinField.text,
                descriptionArea.text
            )
            cv.experiences.add(exp)
            experiencesListModel.addElement("${exp.poste} - ${exp.entreprise}")
            dialog.dispose()
        }

        val btnCancel = JButton("Annuler")
        btnCancel.addActionListener { dialog.dispose() }

        dialog.add(btnOk)
        dialog.add(btnCancel)

        dialog.setSize(400, 300)
        dialog.setLocationRelativeTo(this)
        dialog.isVisible = true
    }

    private fun ajouterFormation() {
        val dialog = JDialog(this, "Ajouter une formation", true)
        dialog.layout = GridLayout(4, 2, 5, 5)

        val diplomeField = JTextField()
        val etablissementField = JTextField()
        val anneeField = JTextField()

        dialog.add(JLabel("Diplôme:"))
        dialog.add(diplomeField)
        dialog.add(JLabel("Établissement:"))
        dialog.add(etablissementField)
        dialog.add(JLabel("Année:"))
        dialog.add(anneeField)

        val btnOk = JButton("OK")
        btnOk.addActionListener {
            val formation = Formation(
                diplomeField.text,
                etablissementField.text,
                anneeField.text
            )
            cv.formations.add(formation)
            formationsListModel.addElement("${formation.diplome} - ${formation.annee}")
            dialog.dispose()
        }

        val btnCancel = JButton("Annuler")
        btnCancel.addActionListener { dialog.dispose() }

        dialog.add(btnOk)
        dialog.add(btnCancel)

        dialog.setSize(400, 200)
        dialog.setLocationRelativeTo(this)
        dialog.isVisible = true
    }

    private fun genererPDF() {
        // Mettre à jour le CV avec les infos saisies
        cv.nom = nomField.text
        cv.prenom = prenomField.text
        cv.email = emailField.text
        cv.telephone = telephoneField.text
        cv.adresse = adresseField.text
        cv.competences = competencesArea.text

        // Choisir l'emplacement du fichier
        val fileChooser = JFileChooser()
        fileChooser.selectedFile = java.io.File("CV_${cv.nom}_${cv.prenom}.pdf")

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            val file = fileChooser.selectedFile
            try {
                PDFExporter.exporter(cv, file.absolutePath)
                JOptionPane.showMessageDialog(
                    this,
                    "CV généré avec succès !",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE
                )
            } catch (e: Exception) {
                JOptionPane.showMessageDialog(
                    this,
                    "Erreur lors de la génération : ${e.message}",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
                )
            }
        }
    }
}