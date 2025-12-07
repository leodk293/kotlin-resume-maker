package cvbuilder

import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Text
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.layout.properties.TextAlignment

object PDFExporter {

    fun exporter(cv: CV, cheminFichier: String) {
        val writer = PdfWriter(cheminFichier)
        val pdfDoc = PdfDocument(writer)
        val document = Document(pdfDoc)

        // Titre
        val titre = Paragraph("${cv.prenom} ${cv.nom}")
            .setFontSize(24f)
            .setBold()
            .setTextAlignment(TextAlignment.CENTER)
        document.add(titre)

        // Informations de contact
        document.add(Paragraph("${cv.email} | ${cv.telephone}").setTextAlignment(TextAlignment.CENTER))
        document.add(Paragraph(cv.adresse).setTextAlignment(TextAlignment.CENTER))
        document.add(Paragraph("\n"))

        // Expériences professionnelles
        if (cv.experiences.isNotEmpty()) {
            val titreExp = Paragraph("EXPÉRIENCES PROFESSIONNELLES")
                .setFontSize(16f)
                .setBold()
                .setFontColor(ColorConstants.BLUE)
            document.add(titreExp)

            cv.experiences.forEach { exp ->
                document.add(Paragraph(exp.poste).setBold())
                document.add(Paragraph("${exp.entreprise} | ${exp.dateDebut} - ${exp.dateFin}").setItalic())
                document.add(Paragraph(exp.description))
                document.add(Paragraph("\n"))
            }
        }

        // Formations
        if (cv.formations.isNotEmpty()) {
            val titreForm = Paragraph("FORMATIONS")
                .setFontSize(16f)
                .setBold()
                .setFontColor(ColorConstants.BLUE)
            document.add(titreForm)

            cv.formations.forEach { formation ->
                document.add(Paragraph(formation.diplome).setBold())
                document.add(Paragraph("${formation.etablissement} | ${formation.annee}"))
                document.add(Paragraph("\n"))
            }
        }

        // Compétences
        if (cv.competences.isNotBlank()) {
            val titreComp = Paragraph("COMPÉTENCES")
                .setFontSize(16f)
                .setBold()
                .setFontColor(ColorConstants.BLUE)
            document.add(titreComp)
            document.add(Paragraph(cv.competences))
        }

        document.close()
    }
}