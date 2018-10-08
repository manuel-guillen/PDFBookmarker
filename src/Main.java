import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.itextpdf.kernel.pdf.PageLabelNumberingStyle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

public class Main {
	
	private static final String CONFIG_JSON = "config.json";
	private static final String INPUT = "original.pdf";
	private static final String OUTPUT = "output.pdf";
	
    public static void main(String args[]) throws FileNotFoundException, IOException, ParseException {
    	PdfDocument pdf = new PdfDocument(new PdfReader(INPUT), new PdfWriter(OUTPUT));
    	JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(CONFIG_JSON));   	   	
    	
    	setPageLabels(pdf, (JSONArray) json.get("labelRanges"));
    	setBookmarks(pdf, (JSONArray) json.get("bookmarks"));
    	
    	pdf.close();
    	System.out.println("Done");
    }
    
    private static void setPageLabels(PdfDocument pdf, JSONArray labelRanges) {
    	for (Object obj : labelRanges) {
    		JSONObject labelRange = (JSONObject) obj;
    		
    		int startPage = ((Long) labelRange.get("start")).intValue();
    		String prefix = (String) labelRange.get("prefix");
    		PageLabelNumberingStyle style;
    		
    		switch((String) labelRange.get("style")) {
    		case "Lowercase Roman Numerals":  style = PageLabelNumberingStyle.LOWERCASE_ROMAN_NUMERALS; break;
    		case "None":                      style = null;                                             break;
    		default:                          style = PageLabelNumberingStyle.DECIMAL_ARABIC_NUMERALS;
    		}
    		
    		pdf.getPage(startPage).setPageLabel(style, prefix);
    	}
    }
    
    private static void setBookmarks(PdfDocument pdf, JSONArray bookmarks) {
    	// TODO Implement
    }
}