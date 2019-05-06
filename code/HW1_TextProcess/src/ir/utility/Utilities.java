package ir.utility;

/* 
 * Homework: 1
 * author:   Andres Segura
 * date:     June 2018
 * address:  Bogota, Colombia
 * 
 * comments: Class that contains generic or utility functions for any program.
*/

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class Utilities {
	
	// Class objects
	private static ArrayList<String> stopWords = getStopWordList();
	
	/*
	 * Returns True if the directory path exists. Else return False.
	 */
	public static boolean filepathExists(String filepath) {
		File f = new File(filepath);
		
		if (f.isDirectory())
			return true;
		
		return false;
	}
	
	/*
	 * Returns True if the file path exists. Else return False.
	 */
	public static boolean fileExists(String filepath) {
		File f = new File(filepath);
		
		if (f.exists() && !f.isDirectory())
			return true;
		
		return false;
	}
	
	/*
	 * Split a raw text by tokens. Tokenize on whitespace.
	 */
	public static ArrayList<String> splitTextByTokens(String rawText, boolean advanced) {
		ArrayList<String> tokenList = new ArrayList<String>();
		
		// Temporal variables
		Porter stemmer = new Porter();
		StringTokenizer st = new StringTokenizer(rawText);
		String currToken = "";
		
		while (st.hasMoreElements()) {
			currToken = st.nextToken();
			
			// Integration with the Porter stemmer and a stopword eliminator
			if (advanced) {
				
				// With double check of no stop-words
				if (!stopWords.contains(currToken)) {
					currToken = stemmer.stripAffixes(currToken);
					if (!stopWords.contains(currToken) && currToken != "")
						tokenList.add(currToken);
				}
			}
			else {
				// Just add the token
				if (currToken != "")
					tokenList.add(currToken);
			}
		}
		
		return tokenList;
	}
	
	/*
	 * Sort a Hash table by numeric values.
	 */
	@SuppressWarnings("rawtypes")
	public static List<Map.Entry> sortHashtableByValue(Hashtable<String, Integer> wordCount) {
		List<Map.Entry> sortedList = new ArrayList<Map.Entry>(wordCount.entrySet());
		
        Collections.sort(sortedList, new Comparator<Map.Entry>() {
        	public int compare(Map.Entry e1, Map.Entry e2) {
        		Integer i1 = (Integer) e1.getValue();
        		Integer i2 = (Integer) e2.getValue();
        		return i2.compareTo(i1);
            }
        });
        
        return sortedList;
	}
	
	/*
	 * Return a list of stop words.
	 */
	public static ArrayList<String> getStopWordList() {
		String filepath = "resources/stopwords.txt";
		Reader myReader = new Reader();
		ArrayList<String> stopWords = myReader.readFile(filepath);
		return stopWords;
	}
	
}