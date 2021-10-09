import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitRemoteGoToDestination;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class Main {
	
	private static final String CONFIG_JSON = "config.json";
	private static final String INPUT = "original.pdf";
	private static final String OUTPUT = "output.pdf";
	
    public static void main(String[] args) throws IOException, ParseException {
    	PdfDocument pdf = new PdfDocument(new PdfReader(INPUT), new PdfWriter(OUTPUT));
    	JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(CONFIG_JSON));   	   	
    	
    	setPageLabels(pdf, (JSONArray) json.get("labelRanges"));
    	
    	pdf.initializeOutlines();
    	PdfOutline root = pdf.getOutlines(false);
    	
    	setBookmarks(root, (JSONArray) json.get("bookmarks"));
    	
    	pdf.close();
    	System.out.println("Done");
    }
    
    private static void setPageLabels(PdfDocument pdf, JSONArray labelRanges) {
    	for (Object obj : labelRanges) {
    		JSONObject labelRange = (JSONObject) obj;
    		
    		int startPage = ((Long) labelRange.get("start")).intValue();
    		String prefix = (String) labelRange.get("prefix");
    		PageLabelNumberingStyle style = switch ((String) labelRange.get("style")) {
				case "Lowercase Roman Numerals" -> PageLabelNumberingStyle.LOWERCASE_ROMAN_NUMERALS;
				case "None" -> null;
				default -> PageLabelNumberingStyle.DECIMAL_ARABIC_NUMERALS;
			};

			pdf.getPage(startPage).setPageLabel(style, prefix);
    	}
    }
    
    private static void setBookmarks(PdfOutline parent, JSONArray bookmarks) {
    	for (Object obj : bookmarks) {
    	    JSONObject bookmark = (JSONObject) obj;
    	    
    	    String name = (String) bookmark.get("name");
    	    int page = ((Long) bookmark.get("page")).intValue();
    	    
    	    PdfOutline current = parent.addOutline(name);
    	    current.addDestination(PdfExplicitRemoteGoToDestination.createFit(page));
    	    setBookmarks(current, (JSONArray) bookmark.get("children"));
    	}
    }
}