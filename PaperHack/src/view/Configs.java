package view;

public interface Configs {
	//	Window properties
	int WIDTH_SIZE = 600;
	int HEIGHT = 600;
	String PROJECT_NAME = "CopyPaste";
	
	String NAME = "PaperHack";
	
	
	// File buttons Properties
	String OPEN_FILE_1_TEXT = "File 1";
	String OPEN_FILE_2_TEXT = "File 2";
	String OPEN_FILE_TIP = "Click here to open a file";
	String SIMILARITY_BUTTON_TEXT = "Retrieve";
	String SIMILARITY_BUTTON_TIP = "Click here to look for new papers";
	String RESULT_LABEL = "Report";
	
	// Messages
	String TERM = "Term(s)";
	
	// Phrase matches
/*	int MINIMUM_LENTGH_FOR_PHRASE = 10;
	String EXACT_MATCH = "Exact match for phrase: '";
	String CONTAINS_MATCH = "Contains text: '";
	String MATCH_FILE_1_TO_2 = "' of file 1 in file 2.\n";
	String MATCH_FILE_2_TO_1 = "' of file 2 in file 1.\n";
	String LOW_SIMILARITY = "Low chances of copy. More chances of quotation.";
	String SOME_SIMILARITY = "Some chances of copy. Maybe quotations and citations.";
	String HIGH_SIMILARITY = "High chances of copy. Multiple quotations or equal sentences.";
	String COPY_SIMILARITY = "Very high chances of copy. Many equal sentences.";*/
	
	// Cosine comparison
/*	String NONE_COSINE_SIMILARITY = "Files do not seam equal. Simililarity of ";
	String LOW_COSINE_SIMILARITY = "Files have low similarity of ";
	String SOME_COSINE_SIMILARITY = "Files have some similarity of ";
	String HIGH_COSINE_SIMILARITY = "Files are very similar by ";
	String EQUAL_COSINE_SIMILARITY = "Files are extremely similar. Similarity of ";*/
	
	// Languages
	//not portuguese for now
	String LANGUAGE_PORTUGUESE_TEXT = "Portuguese";
	String LANGUAGE_ENGLISH_TEXT = "English";
	
	// Document icon path
	String DOCUMENT_ICON_PATH = "document_icon.png";
	
	// References for Cosine Similarity
	// http://darakpanand.wordpress.com/2013/06/01/document-comparison-by-cosine-methodology-using-lucene/#more-53
	// http://stackoverflow.com/search?q=lucene+similarity+documents
	// http://cephas.net/blog/2008/03/30/how-morelikethis-works-in-lucene/
	// http://www.lucenetutorial.com/lucene-query-syntax.html
	// http://stackoverflow.com/questions/10173202/how-to-calculate-cosine-similarity-with-tf-idf-using-lucene-and-java
	// http://www.avajava.com/tutorials/lessons/how-do-i-use-lucene-to-index-and-search-text-files.html?page=1
	// http://dev.fernandobrito.com/2012/10/building-your-own-lucene-scorer/
	// http://lucene.apache.org/core/old_versioned_docs/versions/2_9_0/api/all/org/apache/lucene/search/Similarity.html
	// http://lucene.apache.org/core/old_versioned_docs/versions/2_9_3/api/contrib-memory/org/apache/lucene/index/memory/AnalyzerUtil.html
	// http://sujitpal.blogspot.com.br/2011/10/computing-document-similarity-using.html
	
	// References for Synonym Dictionary
	// http://grokbase.com/t/lucene/java-user/125ww86g91/how-synonymfilter-works
	// http://www.java2v.com/Open-Source/Java-Document/Search-Engine/apache-solr-1.2.0/org/apache/solr/analysis/SynonymMap.java.htm
	
	// References for Paragraph Comparison
	
}
