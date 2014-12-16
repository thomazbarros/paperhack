package model;

import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.store.LockObtainFailedException;


public class TestIndex {

	public static void main(String[] args) throws CorruptIndexException, LockObtainFailedException, IOException
	{
		Indexer indexer = new Indexer("files", "indexOut");
		Indexer indexer2 = new Indexer("/Users/thomaz/Downloads/Archive/files", "/Users/thomaz/Downloads/Archive");
		indexer.index();
		
		TermProcessor processor = new TermProcessor();
		processor.processDocument("indexOut");
	}
}
