package model;
import java.io.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

public class Indexer {
	private File sourceDirectory;
	private File indexOutputDirectory;
	private static final String FIELD_NAME = "contents";

	public Indexer(String directory, String outDirectory) {
		this.sourceDirectory = new File(directory);
		this.indexOutputDirectory = new File(outDirectory);
	}

	public void index() throws CorruptIndexException,
			LockObtainFailedException, IOException {
		Directory dir = FSDirectory.open(indexOutputDirectory);
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_31);
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_31,
				analyzer);

		IndexWriter writer = new IndexWriter(dir, iwc);

		for (String f : sourceDirectory.list()) {
			Document doc = new Document();
			Field contentField = new Field("contents", new BufferedReader(
					new InputStreamReader(new FileInputStream(f), "UTF-8")),
					Field.TermVector.YES);
			doc.add(contentField);
			writer.addDocument(doc);
		}
		writer.close();
	}

}
