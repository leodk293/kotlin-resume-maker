package com.resumemaker.app.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CV(
    var id: String = System.currentTimeMillis().toString(),
    var nom: String = "",
    var prenom: String = "",
    var email: String = "",
    var telephone: String = "",
    var adresse: String = "",
    var experiences: MutableList<Experience> = mutableListOf(),
    var formations: MutableList<Formation> = mutableListOf(),
    var competences: String = "",
    var createdAt: Long = System.currentTimeMillis(),
    var updatedAt: Long = System.currentTimeMillis()
) : Parcelable

@Parcelize
data class Experience(
    var id: String = System.currentTimeMillis().toString(),
    var poste: String = "",
    var entreprise: String = "",
    var dateDebut: String = "",
    var dateFin: String = "",
    var description: String = ""
) : Parcelable

@Parcelize
data class Formation(
    var id: String = System.currentTimeMillis().toString(),
    var diplome: String = "",
    var etablissement: String = "",
    var annee: String = ""
) : Parcelable

