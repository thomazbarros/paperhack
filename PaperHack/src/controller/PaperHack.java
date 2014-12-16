package controller;


import model.IeeeSearch;
import model.Lucene;
import view.Gui;



public class PaperHack{
	
	public static String NAME = "PaperHack";
	public static Lucene luceneIndexer = null;
	public static int language = 1;
	public static String fileData;

	public static void main(String[] args) {
		// Set the default application name on Mac
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", NAME);
     		
 		// Start GUI
		new Gui();
	}
	
	public static void addFile (String filePath, int counter){
		luceneIndexer = Lucene.createIndex();
		
		// Adds the first file to index
		luceneIndexer.add_file_to_index(filePath, counter);
		
	}
	
	public static void closeIndex(){
		luceneIndexer.closeIndex();
	}
	
	public static void addFile(String filePath, boolean one){
	
		luceneIndexer = Lucene.createIndex();
		
		luceneIndexer.add_file_to_index(filePath, 1);
		if(one)
			luceneIndexer.closeIndex2();
	}
	
	public static void addFirstFile(String filePath){
		//Instantiates a new Lucene.
		// Creates the index for all files in this round
		luceneIndexer = Lucene.createIndex();
		
		// Adds the first file to index
		luceneIndexer.add_file_to_index(filePath, 1);
	}

	public static void addSecondFile(String filePath) {
		// Adds the second file to index
		luceneIndexer.add_file_to_index(filePath, 2);
		
		// Close the index
		luceneIndexer.closeIndex2();
	}
	
	/*public static void get_similarity_of_files(){
		// Check for phrases that are equal //check for hits that are equal (intersection)
		//phrase_indexer.calculates_phrase_similarity();
		
		// Get evaluation of sentences
		//String phrase_text = phrase_indexer.get_similarity_text();
		//String phrase_evaluation = phrase_indexer.get_evaluation();
		
		// If there's something to say about the sentences
		if (! phrase_text.equals("") || ! phrase_evaluation.equals("")) {
			Gui.update_results(phrase_text);
			Gui.update_results(phrase_evaluation);
		}
		
		// Calculates the similarity between files
		lucene_indexer.calculates_cosine_similarity();
		Gui.update_results(lucene_indexer.getSimilarityText());
	}*/

	public static void set_language_action(int i) {
		language = i;
	}

	public static void update_results(String results, int file_number) {
		Gui.updateFileInfo(results, file_number);
		System.out.println(results);
	}

	public static void doSearch(){
		fileData = Lucene.getFileData();
		System.out.println("<<<<<<"+fileData);
		fileData = IeeeSearch.doSearch2(fileData);
		Gui.updateResults(fileData);
		System.out.println(fileData);
	}
}