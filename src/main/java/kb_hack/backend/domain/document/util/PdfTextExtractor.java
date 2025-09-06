package kb_hack.backend.domain.document.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class PdfTextExtractor {
	public static String extract(File pdfFile) throws IOException {
		try (PDDocument doc = PDDocument.load(pdfFile)) {
			PDFTextStripper stripper = new PDFTextStripper();
			stripper.setSortByPosition(true);
			return stripper.getText(doc);
		}
	}
}
