package ir.main;

/* 
 * Homework: 1
 * author:   Andres Segura
 * date:     June 2018
 * address:  Bogota, Colombia
 * 
 * comments: Class that contains the main functions to pre-process the text and build the inverted index.
*/

import ir.utility.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextPreprocessing {
	
	// Class constants
	private static final Pattern PUNCT_SYMBOLS = Pattern.compile("\\p{Punct}");
	private static final Pattern DIGIT_SYMBOLS = Pattern.compile("\\p{Digit}");
	private static final boolean TO_LOWER_CASE = true;
	
	/*
	 * Function that a collection of documents from a file list.
	 */
	public Hashtable<String, ArrayList<String>> getCollection(Hashtable<String, String> files, boolean advanced) {
		Hashtable<String, ArrayList<String>> collection = new Hashtable<String, ArrayList<String>>();
		
		// Temporal variables
		Enumeration<String> e = files.keys();
		String filename;
		String content;
		ArrayList<String> fileTokens;
		
		// Pre processing file data
		while(e.hasMoreElements()) {
			filename = e.nextElement();
			content = files.get(filename);
			content = cleanText(content);
			fileTokens = Utilities.splitTextByTokens(content, advanced);
			
			// Save file token list
			collection.put(filename, fileTokens);
			// System.out.println(">> Filename: " + filename + ", Size: " + fileTokens.size());
		}
		
		// Return the collection of documents
		return collection;
	}
	
	/*
	 * Function that create a inverted index from a collection of documents.
	 */
	public Hashtable<String, Hashtable<String, Integer>> createInverseIndex(Hashtable<String, ArrayList<String>> collection) {
		Hashtable<String, Hashtable<String, Integer>> invIndex = new Hashtable<String, Hashtable<String, Integer>>();
		
		// Temporal variables
		Enumeration<String> e = collection.keys();
		ArrayList<String> doct;
		Hashtable<String, Integer> occurrence;
		String filename = "";
		String currWord = "";
		
		// Create Inverse Index
		while(e.hasMoreElements()) {
			filename = e.nextElement();			
			doct = collection.get(filename);
			
			for(int ix = 0; ix < doct.size(); ix++) {
				currWord = doct.get(ix);
				occurrence = null;
				
				if (!invIndex.containsKey(currWord)) {
					occurrence = new Hashtable<String, Integer>();
					occurrence.put(filename, 1);
				}
				else {
					occurrence = invIndex.get(currWord);
					
					if (!occurrence.containsKey(filename)) {
						occurrence.put(filename, 1);
					}
					else {
						occurrence.put(filename, (1 + occurrence.get(filename)));
					}
				}
				
				// Save word
				if (occurrence != null) {
					invIndex.put(currWord, occurrence);
				}
				
			}
		}
		
		return invIndex;
	}
	
	/*
	 * Calculate the total frequency of words in the document collection.
	 */
	@SuppressWarnings("rawtypes")
	public List<Map.Entry> getWordsFrequency(Hashtable<String, Hashtable<String, Integer>> invIndex) {
		Hashtable<String, Integer> wordCount = new Hashtable<String, Integer>();
		
		// Temporal variables
		Enumeration<String> e = invIndex.keys();
		Hashtable<String, Integer> occurrence;
		String currWord = "";
		String filename = "";
		int wordFreq = 0;
		
		while(e.hasMoreElements()) {
			currWord = e.nextElement();			
			occurrence = invIndex.get(currWord);
			wordFreq = 0;
			
			Enumeration<String> k = occurrence.keys();
			while(k.hasMoreElements()) {
				filename = k.nextElement();
				wordFreq += occurrence.get(filename);
			}
			
			wordCount.put(currWord, wordFreq);
		}
		
		// Create the sorted list with the frequency of the words
		List<Map.Entry> sortedList = Utilities.sortHashtableByValue(wordCount);
		
        // Return the words total frequency on collection
		return sortedList;
	}
	
	/*
	 * Function that create the sparse TF-IDF representation of collection of documents. 
	 */
	public Hashtable<String, ArrayList<String>> createSparseTFIDF(Hashtable<String, Hashtable<String, Integer>> invIndex) {
		Hashtable<String, ArrayList<String>> sparseIndex = new Hashtable<String, ArrayList<String>>(); 
		
		// Get sorted word list
		List<String> wordList = getOrdedWordList(invIndex);
		
		// Temporal variables
		ArrayList<String> doct;
		Hashtable<String, Integer> occurrence;
		Enumeration<String> e;
		String wordName;
		String filename;
		int wordFreq;
		
		for (int wordIx=0; wordIx < wordList.size(); wordIx++) {
			wordName = wordList.get(wordIx);
			occurrence = invIndex.get(wordName);
			
			// Create document encoding
			e = occurrence.keys();
			while(e.hasMoreElements()) {
				filename = e.nextElement();
				wordFreq = occurrence.get(filename);
				
				if (!sparseIndex.containsKey(filename)) {
					doct = new ArrayList<String>();
				}
				else {
					doct = sparseIndex.get(filename);
				}
				String data = (wordIx + 1) + ":" + wordFreq;
				doct.add(data);
				
				// Update document
				sparseIndex.put(filename, doct);
			}
		}
		
		// Return the sparse TF-IDF representation
		return sparseIndex;
	}
	
	/*
	 * Sorting words alphabetically.
	 */
	public ArrayList<String> getOrdedWordList(Hashtable<String, Hashtable<String, Integer>> invIndex) {
		
		// Sorting words alphabetically
		ArrayList<String> sl = Collections.list(invIndex.keys());
		Collections.sort(sl);
		
		return sl;
	}
	
	/*
	 * Summarize the total number of words in the collection (file by file).
	 */
	public int getWordCount(Hashtable<String, ArrayList<String>> collection) {
		int wCount = 0;
		
		Enumeration<ArrayList<String>> e = collection.elements();
		ArrayList<String> words;
		
		while(e.hasMoreElements()) {
			words = e.nextElement();
			wCount += words.size();
		}
		
		// Return the total number of words
		return wCount;
	}
	
	/*
	 * Clean raw text. Remove punctuation. Data Quality function.
	 *  Step 1: Transform to lower case
	 *  Step 2: Remove punctuation symbols
	 *  Step 3: Remove digits
	 */
	public String cleanText(String rawText) {
		String newText = (TO_LOWER_CASE ? rawText.toLowerCase() : rawText);
		Matcher unwantedMatcher;
		
		// Remove punctuation symbols
		unwantedMatcher = PUNCT_SYMBOLS.matcher(newText);
		newText = unwantedMatcher.replaceAll("");

		// Remove numbers
		unwantedMatcher = DIGIT_SYMBOLS.matcher(newText);
		newText = unwantedMatcher.replaceAll("");
		
		// Return cleaned text
		return newText;
	}
	
}