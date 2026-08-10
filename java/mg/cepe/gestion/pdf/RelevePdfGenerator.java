package mg.cepe.gestion.pdf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import mg.cepe.gestion.model.Eleve;
import mg.cepe.gestion.model.LigneReleve;

public class RelevePdfGenerator {
    public static void generer(String cheminSortie, String anneeScolaire, Eleve eleve, String nomEcole,
            List<LigneReleve> lignes, double totalPondere, double totalCoef, double moyenne) throws IOException {
        Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter.getInstance(doc, new FileOutputStream(cheminSortie));
        doc.open();
        Font tf = new Font(Font.HELVETICA, 18, Font.BOLD);
        Paragraph titre = new Paragraph("RELEVE DE NOTES - SESSION CEPE", tf);
        titre.setAlignment(Element.ALIGN_CENTER);
        titre.setSpacingAfter(20);
        doc.add(titre);
        Font vf = new Font(Font.HELVETICA, 11, Font.NORMAL);
        doc.add(new Paragraph("Année scolaire : " + anneeScolaire, vf));
        doc.add(new Paragraph("Nom : " + eleve.getNom(), vf));
        doc.add(new Paragraph("Prénoms : " + eleve.getPrenom(), vf));
        doc.add(new Paragraph(
                "Date de naissance : " + eleve.getDateNaissance().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                vf));
        doc.add(new Paragraph("Ecole : " + nomEcole, vf));
        doc.add(Chunk.NEWLINE);
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[] { 3f, 1f, 1f, 1.5f });
        Font hf = new Font(Font.HELVETICA, 11, Font.BOLD);
        String[] headers = { "Matière", "Coefficient", "Note", "Note pondérée" };
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, hf));
            cell.setBackgroundColor(new java.awt.Color(200, 220, 240));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
        for (LigneReleve l : lignes) {
            table.addCell(new Phrase(l.getDesignMat(), vf));
            table.addCell(new Phrase(String.valueOf(l.getCoef()), vf));
            table.addCell(new Phrase(String.format("%.0f", l.getNote()), vf));
            table.addCell(new Phrase(String.format("%.0f", l.getNotePonderee()), vf));
        }
        for (int i = 0; i < 4; i++)
            table.addCell("");
        table.addCell(new Phrase("TOTAL", hf));
        table.addCell(new Phrase(String.valueOf(totalCoef), hf));
        table.addCell("");
        table.addCell(new Phrase(String.format("%.0f", totalPondere), hf));
        doc.add(table);
        doc.add(Chunk.NEWLINE);
        Paragraph moy = new Paragraph("Moyenne : " + String.format("%.2f", moyenne),
                new Font(Font.HELVETICA, 14, Font.BOLD));
        moy.setAlignment(Element.ALIGN_RIGHT);
        doc.add(moy);
        doc.close();
    }
}
