package com.resumemaker.app.utils

import android.content.Context
import android.os.Build
import androidx.core.content.FileProvider
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.properties.TextAlignment
import com.resumemaker.app.data.models.CV
import java.io.File
import java.io.FileOutputStream

object PDFExporter {
    
    fun exportCV(context: Context, cv: CV) {
        try {
            val fileName = "CV_${cv.nom}_${cv.prenom}_${System.currentTimeMillis()}.pdf"
            val file = File(context.getExternalFilesDir(null), fileName)
            
            val writer = PdfWriter(FileOutputStream(file))
            val pdfDoc = PdfDocument(writer)
            val document = Document(pdfDoc)

            // Title
            val titre = Paragraph("${cv.prenom} ${cv.nom}")
                .setFontSize(24f)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
            document.add(titre)

            // Contact Information
            val contactInfo = buildString {
                if (cv.email.isNotEmpty()) append(cv.email)
                if (cv.telephone.isNotEmpty()) {
                    if (isNotEmpty()) append(" | ")
                    append(cv.telephone)
                }
            }
            if (contactInfo.isNotEmpty()) {
                document.add(Paragraph(contactInfo).setTextAlignment(TextAlignment.CENTER))
            }
            if (cv.adresse.isNotEmpty()) {
                document.add(Paragraph(cv.adresse).setTextAlignment(TextAlignment.CENTER))
            }
            document.add(Paragraph("\n"))

            // Work Experience
            if (cv.experiences.isNotEmpty()) {
                val titreExp = Paragraph("WORK EXPERIENCE")
                    .setFontSize(16f)
                    .setBold()
                    .setFontColor(ColorConstants.BLUE)
                document.add(titreExp)

                cv.experiences.forEach { exp ->
                    document.add(Paragraph(exp.poste).setBold())
                    document.add(
                        Paragraph("${exp.entreprise} | ${exp.dateDebut} - ${exp.dateFin}")
                            .setItalic()
                    )
                    if (exp.description.isNotEmpty()) {
                        document.add(Paragraph(exp.description))
                    }
                    document.add(Paragraph("\n"))
                }
            }

            // Education
            if (cv.formations.isNotEmpty()) {
                val titreForm = Paragraph("EDUCATION")
                    .setFontSize(16f)
                    .setBold()
                    .setFontColor(ColorConstants.BLUE)
                document.add(titreForm)

                cv.formations.forEach { formation ->
                    document.add(Paragraph(formation.diplome).setBold())
                    document.add(
                        Paragraph("${formation.etablissement} | ${formation.annee}")
                    )
                    document.add(Paragraph("\n"))
                }
            }

            // Skills
            if (cv.competences.isNotEmpty()) {
                val titreComp = Paragraph("SKILLS")
                    .setFontSize(16f)
                    .setBold()
                    .setFontColor(ColorConstants.BLUE)
                document.add(titreComp)
                document.add(Paragraph(cv.competences))
            }

            document.close()

            // Share the file
            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )
            } else {
                android.net.Uri.fromFile(file)
            }

            val shareIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(android.content.Intent.EXTRA_STREAM, uri)
                addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(
                android.content.Intent.createChooser(
                    shareIntent,
                    "Share PDF"
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

