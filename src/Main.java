import java.io.IOException;

import com.itextpdf.kernel.pdf.PageLabelNumberingStyle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

public class Main {
    
	private static final String SRC = "original.pdf";
	private static final String DEST = "output.pdf";
	
    public static void main(String args[]) throws IOException {
    	PdfDocument pdf = new PdfDocument(new PdfReader(SRC), new PdfWriter(DEST));
    	    	
    	pdf.getPage(1).setPageLabel(null, "Cover");
    	pdf.getPage(2).setPageLabel(PageLabelNumberingStyle.LOWERCASE_ROMAN_NUMERALS, "");
    	pdf.getPage(24).setPageLabel(PageLabelNumberingStyle.DECIMAL_ARABIC_NUMERALS, "");
    	
    	pdf.initializeOutlines();
    	PdfOutline root = pdf.getOutlines(false);
    	
    	// TODO add outline objects
    	
    	pdf.close();
    	
    	System.out.println("Done.");
    }
}