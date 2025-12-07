package com.resumemaker.app.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.resumemaker.app.data.models.CV
import com.resumemaker.app.utils.PDFExporter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CVDetailScreen(
    cv: CV,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Resume Preview",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToEdit) {
                        Icon(Icons.Default.Edit, "Edit")
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onNavigateToEdit,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Edit, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Edit")
                    }
                    Button(
                        onClick = {
                            PDFExporter.exportCV(context, cv)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Download, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Download PDF")
                    }
                }
            }
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
            // Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${cv.prenom} ${cv.nom}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (cv.email.isNotEmpty()) {
                        Text(
                            text = cv.email,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    if (cv.telephone.isNotEmpty()) {
                        Text(
                            text = cv.telephone,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    if (cv.adresse.isNotEmpty()) {
                        Text(
                            text = cv.adresse,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            // Experiences
            if (cv.experiences.isNotEmpty()) {
                SectionCard(title = "Work Experience") {
                    cv.experiences.forEach { exp ->
                        ExperienceItem(exp)
                        if (exp != cv.experiences.last()) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                        }
                    }
                }
            }

            // Education
            if (cv.formations.isNotEmpty()) {
                SectionCard(title = "Education") {
                    cv.formations.forEach { formation ->
                        FormationItem(formation)
                        if (formation != cv.formations.last()) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                        }
                    }
                }
            }

            // Skills
            if (cv.competences.isNotEmpty()) {
                SectionCard(title = "Skills") {
                    Text(
                        text = cv.competences,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun ExperienceItem(exp: Experience) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = exp.poste,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = exp.entreprise,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "${exp.dateDebut} - ${exp.dateFin}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (exp.description.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = exp.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun FormationItem(formation: Formation) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = formation.diplome,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = formation.etablissement,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = formation.annee,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

