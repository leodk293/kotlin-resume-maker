package cvbuilder

data class CV(
    var nom: String = "",
    var prenom: String = "",
    var email: String = "",
    var telephone: String = "",
    var adresse: String = "",
    var experiences: MutableList<Experience> = mutableListOf(),
    var formations: MutableList<Formation> = mutableListOf(),
    var competences: String = ""
)

data class Experience(
    var poste: String = "",
    var entreprise: String = "",
    var dateDebut: String = "",
    var dateFin: String = "",
    var description: String = ""
)

data class Formation(
    var diplome: String = "",
    var etablissement: String = "",
    var annee: String = ""
)