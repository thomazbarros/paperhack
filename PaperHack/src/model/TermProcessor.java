package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


public class TermProcessor {
	
	public void processDocument(String indexDirName) throws CorruptIndexException, IOException
	{
		IndexReader reader = IndexReader.open(FSDirectory.open(new File(indexDirName)));
		TermEnum termEnum = reader.terms();
		ArrayList<String> termList = new ArrayList<String>();
		
		while (termEnum.next())
		{
			String term = termEnum.term().text();
			termList.add(term);
		}
		System.out.printf("%-20s %-4s\t %-4s\t %-4s\t %-4s\n","Term","D1","D2","D3","D4");
		System.out.println("------------------------------------------------------------");
		for (int i = 0; i < termList.size(); i++)
		{
			System.out.printf("%-20s",termList.get(i));
			String term = termList.get(i);
			StringBuffer sb = new StringBuffer();
			for (int j = 0; j < reader.numDocs(); j++)
			{
				System.out.printf("%4.2f\t",getTFIDF(reader, term, "contents", j));
			}
			System.out.println();
		}
	}
	public double getTF(IndexReader reader, String term, String field, int docID) throws IOException
	{
		TermFreqVector termVector = reader.getTermFreqVector(docID, field);
		int idx = termVector.indexOf(term);
		if (idx == -1)
		{
			return 0;
		}
		else
		{
			int[] freqs = termVector.getTermFrequencies();
			return freqs[idx];
		}
	}
	
	public double getIDF(IndexReader reader, String field, String termName) throws IOException
	{
		return Math.log(reader.numDocs()/ ((double)reader.docFreq(new Term(field, termName))));
	}
	
	public double getTFIDF(IndexReader reader, String termName, String field, int docID) throws IOException
	{
		return getTF(reader, termName, field, docID) * getIDF(reader, field, termName);
	}
}
