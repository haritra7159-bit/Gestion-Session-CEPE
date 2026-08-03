package mg.cepe.gestion.pdf;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import mg.cepe.gestion.model.Eleve;
import mg.cepe.gestion.model.LigneReleve;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RelevePdfGenerator {

    public static void generer(String cheminSortie, String anneeScolaire, Eleve eleve,
                               String nomEcole, List<LigneReleve> lignes,
                               double totalPondere, int totalCoef, double moyenne) throws IOException {

        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter.getInstance(document, new FileOutputStream(cheminSortie));
        document.open();

        Font titreFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        Paragraph titre = new Paragraph("RELEVE DE NOTES - SESSION CEPE", titreFont);
        titre.setAlignment(Element.ALIGN_CENTER);
        titre.setSpacingAfter(20);
        document.add(titre);

        Font labelFont = new Font(Font.HELVETICA, 11, Font.BOLD);
        Font valueFont = new Font(Font.HELVETICA, 11, Font.NORMAL);

        document.add(new Paragraph("Année scolaire : " + anneeScolaire, valueFont));
        document.add(new Paragraph("Nom : " + eleve.getNom(), valueFont));
        document.add(new Paragraph("Prénoms : " + eleve.getPrenom(), valueFont));
        document.add(new Paragraph("Date de naissance : " + eleve.getDateNaissance().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), valueFont));
        document.add(new Paragraph("Ecole : " + nomEcole, valueFont));
        document.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3f, 1f, 1f, 1.5f});

        String[] headers = {"Matière", "Coefficient", "Note", "Note pondérée"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, labelFont));
            cell.setBackgroundColor(new java.awt.Color(200, 220, 240));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        for (LigneReleve l : lignes) {
            table.addCell(new Phrase(l.getDesignMat(), valueFont));
            table.addCell(new Phrase(String.valueOf(l.getCoef()), valueFont));
            table.addCell(new Phrase(String.format("%.0f", l.getNote()), valueFont));
            table.addCell(new Phrase(String.format("%.0f", l.getNotePonderee()), valueFont));
        }

        for (int i = 0; i < 4; i++) table.addCell("");
        table.addCell(new Phrase("TOTAL", labelFont));
        table.addCell(new Phrase(String.valueOf(totalCoef), labelFont));
        table.addCell("");
        table.addCell(new Phrase(String.format("%.0f", totalPondere), labelFont));

        document.add(table);
        document.add(Chunk.NEWLINE);

        Paragraph moy = new Paragraph("Moyenne : " + String.format("%.2f", moyenne), new Font(Font.HELVETICA, 14, Font.BOLD));
        moy.setAlignment(Element.ALIGN_RIGHT);
        document.add(moy);

        document.close();
    }
}
