package com.resumemaker.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resumemaker.app.data.models.CV
import com.resumemaker.app.data.models.Experience
import com.resumemaker.app.data.models.Formation
import com.resumemaker.app.ui.viewmodel.CVViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditCVScreen(
    viewModel: CVViewModel,
    cvId: String? = null,
    onNavigateBack: () -> Unit
) {
    var loadedCV by remember { mutableStateOf<CV?>(null) }
    
    LaunchedEffect(cvId) {
        if (cvId != null) {
            loadedCV = viewModel.getCVById(cvId)
        }
    }
    
    val cv = loadedCV ?: CV()
    
    var nom by remember(cvId) { mutableStateOf(cv.nom) }
    var prenom by remember(cvId) { mutableStateOf(cv.prenom) }
    var email by remember(cvId) { mutableStateOf(cv.email) }
    var telephone by remember(cvId) { mutableStateOf(cv.telephone) }
    var adresse by remember(cvId) { mutableStateOf(cv.adresse) }
    var competences by remember(cvId) { mutableStateOf(cv.competences) }
    var experiences by remember(cvId) { mutableStateOf(cv.experiences.toMutableList()) }
    var formations by remember(cvId) { mutableStateOf(cv.formations.toMutableList()) }
    
    LaunchedEffect(loadedCV) {
        loadedCV?.let {
            nom = it.nom
            prenom = it.prenom
            email = it.email
            telephone = it.telephone
            adresse = it.adresse
            competences = it.competences
            experiences = it.experiences.toMutableList()
            formations = it.formations.toMutableList()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (cvId != null) "Edit Resume" else "Create Resume",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            val updatedCV = if (cvId != null) {
                                cv.copy(
                                    nom = nom,
                                    prenom = prenom,
                                    email = email,
                                    telephone = telephone,
                                    adresse = adresse,
                                    competences = competences,
                                    experiences = experiences,
                                    formations = formations,
                                    updatedAt = System.currentTimeMillis()
                                )
                            } else {
                                CV(
                                    nom = nom,
                                    prenom = prenom,
                                    email = email,
                                    telephone = telephone,
                                    adresse = adresse,
                                    competences = competences,
                                    experiences = experiences,
                                    formations = formations,
                                    updatedAt = System.currentTimeMillis()
                                )
                            }
                            if (cvId != null) {
                                viewModel.updateCV(updatedCV)
                            } else {
                                viewModel.addCV(updatedCV)
                            }
                            onNavigateBack()
                        }
                    ) {
                        Text("Save", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Personal Information Section
            SectionCard(title = "Personal Information") {
                OutlinedTextField(
                    value = nom,
                    onValueChange = { nom = it },
                    label = { Text("Last Name") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Person, null) }
                )
                OutlinedTextField(
                    value = prenom,
                    onValueChange = { prenom = it },
                    label = { Text("First Name") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Person, null) }
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Email, null) },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = androidx.compose.foundation.text.KeyboardType.Email
                    )
                )
                OutlinedTextField(
                    value = telephone,
                    onValueChange = { telephone = it },
                    label = { Text("Phone") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Phone, null) },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = androidx.compose.foundation.text.KeyboardType.Phone
                    )
                )
                OutlinedTextField(
                    value = adresse,
                    onValueChange = { adresse = it },
                    label = { Text("Address") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.LocationOn, null) }
                )
            }

            // Experiences Section
            SectionCard(title = "Work Experience") {
                experiences.forEachIndexed { index, exp ->
                    ExperienceCard(
                        experience = exp,
                        onUpdate = { updated ->
                            experiences[index] = updated
                            experiences = experiences.toMutableList()
                        },
                        onDelete = {
                            experiences = experiences.filterIndexed { i, _ -> i != index }.toMutableList()
                        }
                    )
                    if (index < experiences.size - 1) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
                Button(
                    onClick = {
                        experiences = (experiences + Experience()).toMutableList()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Icon(Icons.Default.Add, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Experience")
                }
            }

            // Education Section
            SectionCard(title = "Education") {
                formations.forEachIndexed { index, formation ->
                    FormationCard(
                        formation = formation,
                        onUpdate = { updated ->
                            formations[index] = updated
                            formations = formations.toMutableList()
                        },
                        onDelete = {
                            formations = formations.filterIndexed { i, _ -> i != index }.toMutableList()
                        }
                    )
                    if (index < formations.size - 1) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
                Button(
                    onClick = {
                        formations = (formations + Formation()).toMutableList()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Icon(Icons.Default.Add, null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Education")
                }
            }

            // Skills Section
            SectionCard(title = "Skills") {
                OutlinedTextField(
                    value = competences,
                    onValueChange = { competences = it },
                    label = { Text("Enter your skills (comma separated)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    leadingIcon = { Icon(Icons.Default.Star, null) }
                )
            }
        }
    }
}

@Composable
fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            content()
        }
    }
}

@Composable
fun ExperienceCard(
    experience: Experience,
    onUpdate: (Experience) -> Unit,
    onDelete: () -> Unit
) {
    var poste by remember { mutableStateOf(experience.poste) }
    var entreprise by remember { mutableStateOf(experience.entreprise) }
    var dateDebut by remember { mutableStateOf(experience.dateDebut) }
    var dateFin by remember { mutableStateOf(experience.dateFin) }
    var description by remember { mutableStateOf(experience.description) }

    LaunchedEffect(poste, entreprise, dateDebut, dateFin, description) {
        onUpdate(
            experience.copy(
                poste = poste,
                entreprise = entreprise,
                dateDebut = dateDebut,
                dateFin = dateFin,
                description = description
            )
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Experience",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                Icon(
                    Icons.Default.Delete,
                    "Delete",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        OutlinedTextField(
            value = poste,
            onValueChange = { poste = it },
            label = { Text("Job Title") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = entreprise,
            onValueChange = { entreprise = it },
            label = { Text("Company") },
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = dateDebut,
                onValueChange = { dateDebut = it },
                label = { Text("Start Date") },
                modifier = Modifier.weight(1f),
                placeholder = { Text("MM/YYYY") }
            )
            OutlinedTextField(
                value = dateFin,
                onValueChange = { dateFin = it },
                label = { Text("End Date") },
                modifier = Modifier.weight(1f),
                placeholder = { Text("MM/YYYY or Present") }
            )
        }
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )
    }
}

@Composable
fun FormationCard(
    formation: Formation,
    onUpdate: (Formation) -> Unit,
    onDelete: () -> Unit
) {
    var diplome by remember { mutableStateOf(formation.diplome) }
    var etablissement by remember { mutableStateOf(formation.etablissement) }
    var annee by remember { mutableStateOf(formation.annee) }

    LaunchedEffect(diplome, etablissement, annee) {
        onUpdate(
            formation.copy(
                diplome = diplome,
                etablissement = etablissement,
                annee = annee
            )
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Education",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                Icon(
                    Icons.Default.Delete,
                    "Delete",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        OutlinedTextField(
            value = diplome,
            onValueChange = { diplome = it },
            label = { Text("Degree/Certificate") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = etablissement,
            onValueChange = { etablissement = it },
            label = { Text("Institution") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = annee,
            onValueChange = { annee = it },
            label = { Text("Year") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

