package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.function.type4.Parser;
import org.apache.pdfbox.util.PDFTextStripper;

import view.Configs;
import controller.PaperHack;

/**
 * @author thomaz
 *
 */
public class Lucene {

	private IndexWriter writer;
	private ArrayList<File> queue = new ArrayList<File>();
	private Directory indexLocation;
	/*
	 * private String[] STOPWORDS = new String[] { "é", "a", "à", "ao", "aos",
	 * "as", "até", "através", "com", "como", "da", "daquele", "daqueles",
	 * "das", "de", "dela", "delas", "dele", "deles", "dessa", "dessas",
	 * "desse", "desses", "desta", "destas", "deste", "deste", "destes",
	 * "disso", "disto", "do", "dos", "e", "é", "e'", "em", "entre", "era",
	 * "essa", "essas", "esse", "esses", "esta", "estas", "este", "estes",
	 * "estou", "eu", "foi", "for", "há", "isso", "isto", "já", "lá", "lhe",
	 * "lhes", "lo", "mas", "me", "muita", "muitas", "muito", "muitos", "na",
	 * "não", "nas", "nem", "nenhum", "nessa", "nessas", "nesta", "nestas",
	 * "no", "nos", "nós", "num", "numa", "nunca", "o", "os", "ou", "outra",
	 * "outras", "outro", "outros", "para", "pela", "pelas", "pelo", "pelos",
	 * "per", "perante", "pois", "por", "porque", "quais", "qual", "quando",
	 * "que", "quem", "se", "sem", "seu", "seus", "si", "sido", "só", "sob",
	 * "sobre", "sua", "suas", "talvez", "também", "tampouco", "te", "tem",
	 * "tendo", "tenha", "ter", "teu", "teus", "ti", "toda", "todas", "todo",
	 * "todos", "tu", "tua", "tuas", "um", "uma", "umas", "uns", "vos", "vós",
	 * "x", "y", "z", "s", "n" };
	 */

	ArrayList<String> array = new ArrayList<String>(Arrays.asList("a", "about",
			"above", "after", "again", "against", "all", "am", "an", "and",
			"any", "are", "aren't", "as", "at", "be", "because", "been",
			"before", "being", "below", "between", "both", "but", "by",
			"can't", "cannot", "could", "couldn't", "did", "didn't", "do",
			"does", "doesn't", "doing", "don't", "down", "during", "each",
			"few", "for", "from", "further", "had", "hadn't", "has", "hasn't",
			"have", "haven't", "having", "he", "he'd", "he'll", "he's", "her",
			"here", "here's", "hers", "herself", "him", "himself", "his",
			"how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in",
			"into", "is", "isn't", "it", "it's", "its", "itself", "let's",
			"me", "more", "most", "mustn't", "my", "myself", "no", "nor",
			"not", "of", "off", "on", "once", "only", "or", "other", "ought",
			"our", "ours", "ourselves", "out", "over", "own", "same", "shan't",
			"she", "she'd", "she'll", "she's", "should", "shouldn't", "so",
			"some", "such", "than", "that", "that's", "the", "their", "theirs",
			"them", "themselves", "then", "there", "there's", "these", "they",
			"they'd", "they'll", "they're", "they've", "this", "those",
			"through", "to", "too", "under", "until", "up", "very", "was",
			"wasn't", "we", "we'd", "we'll", "we're", "we've", "were",
			"weren't", "what", "what's", "when", "when's", "where", "where's",
			"which", "while", "who", "who's", "whom", "why", "why's", "with",
			"won't", "would", "wouldn't", "you", "you'd", "you'll", "you're",
			"you've", "your", "yours", "yourself", "yourselves", "able",
			"about", "above", "abroad", "according", "accordingly", "across",
			"actually", "adj", "after", "afterwards", "again", "against",
			"ago", "ahead", "ain't", "all", "allow", "allows", "almost",
			"alone", "along", "alongside", "already", "also", "although",
			"always", "am", "amid", "amidst", "among", "amongst", "an", "and",
			"another", "any", "anybody", "anyhow", "anyone", "anything",
			"anyway", "anyways", "anywhere", "apart", "appear", "appreciate",
			"appropriate", "are", "aren't", "around", "as", "a's", "aside",
			"ask", "asking", "associated", "at", "available", "away",
			"awfully", "back", "backward", "backwards", "be", "became",
			"because", "become", "becomes", "becoming", "been", "before",
			"beforehand", "begin", "behind", "being", "believe", "below",
			"beside", "besides", "best", "better", "between", "beyond", "both",
			"brief", "but", "by", "came", "can", "cannot", "cant", "can't",
			"caption", "cause", "causes", "certain", "certainly", "changes",
			"clearly", "c'mon", "co", "co.", "com", "come", "comes",
			"concerning", "consequently", "consider", "considering", "contain",
			"containing", "contains", "corresponding", "could", "couldn't",
			"course", "c's", "currently", "dare", "daren't", "definitely",
			"described", "despite", "did", "didn't", "different", "directly",
			"do", "does", "doesn't", "doing", "done", "don't", "down",
			"downwards", "during", "each", "edu", "eg", "eight", "eighty",
			"either", "else", "elsewhere", "end", "ending", "enough",
			"entirely", "especially", "et", "etc", "even", "ever", "evermore",
			"every", "everybody", "everyone", "everything", "everywhere", "ex",
			"exactly", "example", "except", "fairly", "far", "farther", "few",
			"fewer", "fifth", "first", "five", "followed", "following",
			"follows", "for", "forever", "former", "formerly", "forth",
			"forward", "found", "four", "from", "further", "furthermore",
			"get", "gets", "getting", "given", "gives", "go", "goes", "going",
			"gone", "got", "gotten", "greetings", "had", "hadn't", "half",
			"happens", "hardly", "has", "hasn't", "have", "haven't", "having",
			"he", "he'd", "he'll", "hello", "help", "hence", "her", "here",
			"hereafter", "hereby", "herein", "here's", "hereupon", "hers",
			"herself", "he's", "hi", "him", "himself", "his", "hither",
			"hopefully", "how", "howbeit", "however", "hundred", "i'd", "ie",
			"if", "ignored", "i'll", "i'm", "immediate", "in", "inasmuch",
			"inc", "inc.", "indeed", "indicate", "indicated", "indicates",
			"inner", "inside", "insofar", "instead", "into", "inward", "is",
			"isn't", "it", "it'd", "it'll", "its", "it's", "itself", "i've",
			"just", "k", "keep", "keeps", "kept", "know", "known", "knows",
			"last", "lately", "later", "latter", "latterly", "least", "less",
			"lest", "let", "let's", "like", "liked", "likely", "likewise",
			"little", "look", "looking", "looks", "low", "lower", "ltd",
			"made", "mainly", "make", "makes", "many", "may", "maybe",
			"mayn't", "me", "mean", "meantime", "meanwhile", "merely", "might",
			"mightn't", "mine", "minus", "miss", "more", "moreover", "most",
			"mostly", "mr", "mrs", "much", "must", "mustn't", "my", "myself",
			"name", "namely", "nd", "near", "nearly", "necessary", "need",
			"needn't", "needs", "neither", "never", "neverf", "neverless",
			"nevertheless", "new", "next", "nine", "ninety", "no", "nobody",
			"non", "none", "nonetheless", "noone", "no-one", "nor", "normally",
			"not", "nothing", "notwithstanding", "novel", "now", "nowhere",
			"obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on",
			"once", "one", "ones", "one's", "only", "onto", "opposite", "or",
			"other", "others", "otherwise", "ought", "oughtn't", "our", "ours",
			"ourselves", "out", "outside", "over", "overall", "own",
			"particular", "particularly", "past", "per", "perhaps", "placed",
			"please", "plus", "possible", "presumably", "probably", "provided",
			"provides", "que", "quite", "qv", "rather", "rd", "re", "really",
			"reasonably", "recent", "recently", "regarding", "regardless",
			"regards", "relatively", "respectively", "right", "round", "said",
			"same", "saw", "say", "saying", "says", "second", "secondly",
			"see", "seeing", "seem", "seemed", "seeming", "seems", "seen",
			"self", "selves", "sensible", "sent", "serious", "seriously",
			"seven", "several", "shall", "shan't", "she", "she'd", "she'll",
			"she's", "should", "shouldn't", "since", "six", "so", "some",
			"somebody", "someday", "somehow", "someone", "something",
			"sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry",
			"specified", "specify", "specifying", "still", "sub", "such",
			"sup", "sure", "take", "taken", "taking", "tell", "tends", "th",
			"than", "thank", "thanks", "thanx", "that", "that'll", "thats",
			"that's", "that've", "the", "their", "theirs", "them",
			"themselves", "then", "thence", "there", "thereafter", "thereby",
			"there'd", "therefore", "therein", "there'll", "there're",
			"theres", "there's", "thereupon", "there've", "these", "they",
			"they'd", "they'll", "they're", "they've", "thing", "things",
			"think", "third", "thirty", "this", "thorough", "thoroughly",
			"those", "though", "three", "through", "throughout", "thru",
			"thus", "till", "to", "together", "too", "took", "toward",
			"towards", "tried", "tries", "truly", "try", "trying", "t's",
			"twice", "two", "un", "under", "underneath", "undoing",
			"unfortunately", "unless", "unlike", "unlikely", "until", "unto",
			"up", "upon", "upwards", "us", "use", "used", "useful", "uses",
			"using", "usually", "v", "value", "various", "versus", "very",
			"via", "viz", "vs", "want", "wants", "was", "wasn't", "way", "we",
			"we'd", "welcome", "well", "we'll", "went", "were", "we're",
			"weren't", "we've", "what", "whatever", "what'll", "what's",
			"what've", "when", "whence", "whenever", "where", "whereafter",
			"whereas", "whereby", "wherein", "where's", "whereupon",
			"wherever", "whether", "which", "whichever", "while", "whilst",
			"whither", "who", "who'd", "whoever", "whole", "who'll", "whom",
			"whomever", "who's", "whose", "why", "will", "willing", "wish",
			"with", "within", "without", "wonder", "won't", "would",
			"wouldn't", "yes", "yet", "you", "you'd", "you'll", "your",
			"you're", "yours", "yourself", "yourselves", "you've", "zero", "a",
			"a's", "able", "about", "above", "according", "accordingly",
			"across", "actually", "after", "afterwards", "again", "against",
			"ain't", "all", "allow", "allows", "almost", "alone", "along",
			"already", "also", "although", "always", "am", "among", "amongst",
			"an", "and", "another", "any", "anybody", "anyhow", "anyone",
			"anything", "anyway", "anyways", "anywhere", "apart", "appear",
			"appreciate", "appropriate", "are", "aren't", "around", "as",
			"aside", "ask", "asking", "associated", "at", "available", "away",
			"awfully", "b", "be", "became", "because", "become", "becomes",
			"becoming", "been", "before", "beforehand", "behind", "being",
			"believe", "below", "beside", "besides", "best", "better",
			"between", "beyond", "both", "brief", "but", "by", "c", "c'mon",
			"c's", "came", "can", "can't", "cannot", "cant", "cause", "causes",
			"certain", "certainly", "changes", "clearly", "co", "com", "come",
			"comes", "concerning", "consequently", "consider", "considering",
			"contain", "containing", "contains", "corresponding", "could",
			"couldn't", "course", "currently", "d", "definitely", "described",
			"despite", "did", "didn't", "different", "do", "does", "doesn't",
			"doing", "don't", "done", "down", "downwards", "during", "e",
			"each", "edu", "eg", "eight", "either", "else", "elsewhere",
			"enough", "entirely", "especially", "et", "etc", "even", "ever",
			"every", "everybody", "everyone", "everything", "everywhere", "ex",
			"exactly", "example", "except", "f", "far", "few", "fifth",
			"first", "five", "followed", "following", "follows", "for",
			"former", "formerly", "forth", "four", "from", "further",
			"furthermore", "g", "get", "gets", "getting", "given", "gives",
			"go", "goes", "going", "gone", "got", "gotten", "greetings", "h",
			"had", "hadn't", "happens", "hardly", "has", "hasn't", "have",
			"haven't", "having", "he", "he's", "hello", "help", "hence", "her",
			"here", "here's", "hereafter", "hereby", "herein", "hereupon",
			"hers", "herself", "hi", "him", "himself", "his", "hither",
			"hopefully", "how", "howbeit", "however", "i", "i'd", "i'll",
			"i'm", "i've", "ie", "if", "ignored", "immediate", "in",
			"inasmuch", "inc", "indeed", "indicate", "indicated", "indicates",
			"inner", "insofar", "instead", "into", "inward", "is", "isn't",
			"it", "it'd", "it'll", "it's", "its", "itself", "j", "just", "k",
			"keep", "keeps", "kept", "know", "knows", "known", "l", "last",
			"lately", "later", "latter", "latterly", "least", "less", "lest",
			"let", "let's", "like", "liked", "likely", "little", "look",
			"looking", "looks", "ltd", "m", "mainly", "many", "may", "maybe",
			"me", "mean", "meanwhile", "merely", "might", "more", "moreover",
			"most", "mostly", "much", "must", "my", "myself", "n", "name",
			"namely", "nd", "near", "nearly", "necessary", "need", "needs",
			"neither", "never", "nevertheless", "new", "next", "nine", "no",
			"nobody", "non", "none", "noone", "nor", "normally", "not",
			"nothing", "novel", "now", "nowhere", "o", "obviously", "of",
			"off", "often", "oh", "ok", "okay", "old", "on", "once", "one",
			"ones", "only", "onto", "or", "other", "others", "otherwise",
			"ought", "our", "ours", "ourselves", "out", "outside", "over",
			"overall", "own", "p", "particular", "particularly", "per",
			"perhaps", "placed", "please", "plus", "possible", "presumably",
			"probably", "provides", "q", "que", "quite", "qv", "r", "rather",
			"rd", "re", "really", "reasonably", "regarding", "regardless",
			"regards", "relatively", "respectively", "right", "s", "said",
			"same", "saw", "say", "saying", "says", "second", "secondly",
			"see", "seeing", "seem", "seemed", "seeming", "seems", "seen",
			"self", "selves", "sensible", "sent", "serious", "seriously",
			"seven", "several", "shall", "she", "should", "shouldn't", "since",
			"six", "so", "some", "somebody", "somehow", "someone", "something",
			"sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry",
			"specified", "specify", "specifying", "still", "sub", "such",
			"sup", "sure", "t", "t's", "take", "taken", "tell", "tends", "th",
			"than", "thank", "thanks", "thanx", "that", "that's", "thats",
			"the", "their", "theirs", "them", "themselves", "then", "thence",
			"there", "there's", "thereafter", "thereby", "therefore",
			"therein", "theres", "thereupon", "these", "they", "they'd",
			"they'll", "they're", "they've", "think", "third", "this",
			"thorough", "thoroughly", "those", "though", "three", "through",
			"throughout", "thru", "thus", "to", "together", "too", "took",
			"toward", "towards", "tried", "tries", "truly", "try", "trying",
			"twice", "two", "u", "un", "under", "unfortunately", "unless",
			"unlikely", "until", "unto", "up", "upon", "us", "use", "used",
			"useful", "uses", "using", "usually", "uucp", "v", "value",
			"various", "very", "via", "viz", "vs", "w", "want", "wants", "was",
			"wasn't", "way", "we", "we'd", "we'll", "we're", "we've",
			"welcome", "well", "went", "were", "weren't", "what", "what's",
			"whatever", "when", "whence", "whenever", "where", "where's",
			"whereafter", "whereas", "whereby", "wherein", "whereupon",
			"wherever", "whether", "which", "while", "whither", "who", "who's",
			"whoever", "whole", "whom", "whose", "why", "will", "willing",
			"wish", "with", "within", "without", "won't", "wonder", "would",
			"would", "wouldn't", "x", "y", "yes", "yet", "you", "you'd",
			"you'll", "you're", "you've", "your", "yours", "yourself",
			"yourselves", "z", "zero", "I", "a", "about", "an", "are", "as",
			"at", "be", "by", "com", "for", "from", "how", "in", "is", "it",
			"of", "on", "or", "that", "the", "this", "to", "was", "what",
			"when", "where", "who", "will", "with", "the", "www", "a", "about",
			"above", "across", "after", "afterwards", "again", "against",
			"all", "almost", "alone", "along", "already", "also", "although",
			"always", "am", "among", "amongst", "amoungst", "amount", "an",
			"and", "another", "any", "anyhow", "anyone", "anything", "anyway",
			"anywhere", "are", "around", "as", "at", "back", "be", "became",
			"because", "become", "becomes", "becoming", "been", "before",
			"beforehand", "behind", "being", "below", "beside", "besides",
			"between", "beyond", "bill", "both", "bottom", "but", "by", "call",
			"can", "cannot", "cant", "co", "computer", "con", "could",
			"couldnt", "cry", "de", "describe", "detail", "do", "done", "down",
			"due", "during", "each", "eg", "eight", "either", "eleven", "else",
			"elsewhere", "empty", "enough", "etc", "even", "ever", "every",
			"everyone", "everything", "everywhere", "except", "few", "fifteen",
			"fify", "fill", "find", "fire", "first", "five", "for", "former",
			"formerly", "forty", "found", "four", "from", "front", "full",
			"further", "get", "give", "go", "had", "has", "hasnt", "have",
			"he", "hence", "her", "here", "hereafter", "hereby", "herein",
			"hereupon", "hers", "herse”", "him", "himse”", "his", "how",
			"however", "hundred", "i", "ie", "if", "in", "inc", "indeed",
			"interest", "into", "is", "it", "its", "itse”", "keep", "last",
			"latter", "latterly", "least", "less", "ltd", "made", "many",
			"may", "me", "meanwhile", "might", "mill", "mine", "more",
			"moreover", "most", "mostly", "move", "much", "must", "my",
			"myse”", "name", "namely", "neither", "never", "nevertheless",
			"next", "nine", "no", "nobody", "none", "noone", "nor", "not",
			"nothing", "now", "nowhere", "of", "off", "often", "on", "once",
			"one", "only", "onto", "or", "other", "others", "otherwise", "our",
			"ours", "ourselves", "out", "over", "own", "part", "per",
			"perhaps", "please", "put", "rather", "re", "same", "see", "seem",
			"seemed", "seeming", "seems", "serious", "several", "she",
			"should", "show", "side", "since", "sincere", "six", "sixty", "so",
			"some", "somehow", "someone", "something", "sometime", "sometimes",
			"somewhere", "still", "such", "system", "take", "ten", "than",
			"that", "the", "their", "them", "themselves", "then", "thence",
			"there", "thereafter", "thereby", "therefore", "therein",
			"thereupon", "these", "they", "thick", "thin", "third", "this",
			"those", "though", "three", "through", "throughout", "thru",
			"thus", "to", "together", "too", "top", "toward", "towards",
			"twelve", "twenty", "two", "un", "under", "until", "up", "upon",
			"us", "very", "via", "was", "we", "well", "were", "what",
			"whatever", "when", "whence", "whenever", "where", "whereafter",
			"whereas", "whereby", "wherein", "whereupon", "wherever",
			"whether", "which", "while", "whither", "who", "whoever", "whole",
			"whom", "whose", "why", "will", "with", "within", "without",
			"would", "yet", "you", "your", "yours", "yourself", "yourselves",
			"able", "about", "above", "abst", "accordance", "according",
			"accordingly", "across", "act", "actually", "added", "adj",
			"adopted", "affected", "affecting", "affects", "after",
			"afterwards", "again", "against", "ah", "all", "almost", "alone",
			"along", "already", "also", "although", "always", "am", "among",
			"amongst", "an", "and", "announce", "another", "any", "anybody",
			"anyhow", "anymore", "anyone", "anything", "anyway", "anyways",
			"anywhere", "apparently", "approximately", "are", "aren", "arent",
			"arise", "around", "as", "aside", "ask", "asking", "at", "auth",
			"available", "away", "awfully", "b", "back", "be", "became",
			"because", "become", "becomes", "becoming", "been", "before",
			"beforehand", "begin", "beginning", "beginnings", "begins",
			"behind", "being", "believe", "below", "beside", "besides",
			"between", "beyond", "biol", "both", "brief", "briefly", "but",
			"by", "c", "ca", "came", "can", "cannot", "can't", "cause",
			"causes", "certain", "certainly", "co", "com", "come", "comes",
			"contain", "containing", "contains", "could", "couldnt", "d",
			"date", "did", "didn't", "different", "do", "does", "doesn't",
			"doing", "done", "don't", "down", "downwards", "due", "during",
			"e", "each", "ed", "edu", "effect", "eg", "eight", "eighty",
			"either", "else", "elsewhere", "end", "ending", "enough",
			"especially", "et", "et-", "etc", "even", "ever", "every",
			"everybody", "everyone", "everything", "everywhere", "ex",
			"except", "f", "far", "few", "ff", "fifth", "first", "five", "fix",
			"followed", "following", "follows", "for", "former", "formerly",
			"forth", "found", "four", "from", "further", "furthermore", "g",
			"gave", "get", "gets", "getting", "give", "given", "gives",
			"giving", "go", "goes", "gone", "got", "gotten", "h", "had",
			"happens", "hardly", "has", "hasn't", "have", "haven't", "having",
			"he", "hed", "hence", "her", "here", "hereafter", "hereby",
			"herein", "heres", "hereupon", "hers", "herself", "hes", "hi",
			"hid", "him", "himself", "his", "hither", "home", "how", "howbeit",
			"however", "hundred", "i", "id", "ie", "if", "i'll", "im",
			"immediate", "immediately", "importance", "important", "in", "inc",
			"indeed", "index", "information", "instead", "into", "invention",
			"inward", "is", "isn't", "it", "itd", "it'll", "its", "itself",
			"i've", "j", "just", "k", "keep", "keeps", "kept", "keys", "kg",
			"km", "know", "known", "knows", "l", "largely", "last", "lately",
			"later", "latter", "latterly", "least", "less", "lest", "let",
			"lets", "like", "liked", "likely", "line", "little", "'ll", "look",
			"looking", "looks", "ltd", "m", "made", "mainly", "make", "makes",
			"many", "may", "maybe", "me", "mean", "means", "meantime",
			"meanwhile", "merely", "mg", "might", "million", "miss", "ml",
			"more", "moreover", "most", "mostly", "mr", "mrs", "much", "mug",
			"must", "my", "myself", "n", "na", "name", "namely", "nay", "nd",
			"near", "nearly", "necessarily", "necessary", "need", "needs",
			"neither", "never", "nevertheless", "new", "next", "nine",
			"ninety", "no", "nobody", "non", "none", "nonetheless", "noone",
			"nor", "normally", "nos", "not", "noted", "nothing", "now",
			"nowhere", "o", "obtain", "obtained", "obviously", "of", "off",
			"often", "oh", "ok", "okay", "old", "omitted", "on", "once", "one",
			"ones", "only", "onto", "or", "ord", "other", "others",
			"otherwise", "ought", "our", "ours", "ourselves", "out", "outside",
			"over", "overall", "owing", "own", "p", "page", "pages", "part",
			"particular", "particularly", "past", "per", "perhaps", "placed",
			"please", "plus", "poorly", "possible", "possibly", "potentially",
			"pp", "predominantly", "present", "previously", "primarily",
			"probably", "promptly", "proud", "provides", "put", "q", "que",
			"quickly", "quite", "qv", "r", "ran", "rather", "rd", "re",
			"readily", "really", "recent", "recently", "ref", "refs",
			"regarding", "regardless", "regards", "related", "relatively",
			"research", "respectively", "resulted", "resulting", "results",
			"right", "run", "s", "said", "same", "saw", "say", "saying",
			"says", "sec", "section", "see", "seeing", "seem", "seemed",
			"seeming", "seems", "seen", "self", "selves", "sent", "seven",
			"several", "shall", "she", "shed", "she'll", "shes", "should",
			"shouldn't", "show", "showed", "shown", "showns", "shows",
			"significant", "significantly", "similar", "similarly", "since",
			"six", "slightly", "so", "some", "somebody", "somehow", "someone",
			"somethan", "something", "sometime", "sometimes", "somewhat",
			"somewhere", "soon", "sorry", "specifically", "specified",
			"specify", "specifying", "state", "states", "still", "stop",
			"strongly", "sub", "substantially", "successfully", "such",
			"sufficiently", "suggest", "sup", "sure", "t", "take", "taken",
			"taking", "tell", "tends", "th", "than", "thank", "thanks",
			"thanx", "that", "that'll", "thats", "that've", "the", "their",
			"theirs", "them", "themselves", "then", "thence", "there",
			"thereafter", "thereby", "thered", "therefore", "therein",
			"there'll", "thereof", "therere", "theres", "thereto", "thereupon",
			"there've", "these", "they", "theyd", "they'll", "theyre",
			"they've", "think", "this", "those", "thou", "though", "thoughh",
			"thousand", "throug", "through", "throughout", "thru", "thus",
			"til", "tip", "to", "together", "too", "took", "toward", "towards",
			"tried", "tries", "truly", "try", "trying", "ts", "twice", "two",
			"u", "un", "under", "unfortunately", "unless", "unlike",
			"unlikely", "until", "unto", "up", "upon", "ups", "us", "use",
			"used", "useful", "usefully", "usefulness", "uses", "using",
			"usually", "v", "value", "various", "'ve", "very", "via", "viz",
			"vol", "vols", "vs", "w", "want", "wants", "was", "wasn't", "way",
			"we", "wed", "welcome", "we'll", "went", "were", "weren't",
			"we've", "what", "whatever", "what'll", "whats", "when", "whence",
			"whenever", "where", "whereafter", "whereas", "whereby", "wherein",
			"wheres", "whereupon", "wherever", "whether", "which", "while",
			"whim", "whither", "who", "whod", "whoever", "whole", "who'll",
			"whom", "whomever", "whos", "whose", "why", "widely", "willing",
			"wish", "with", "within", "without", "won't", "words", "world",
			"would", "wouldn't", "www", "x", "y", "yes", "yet", "you", "youd",
			"you'll", "your", "youre", "yours", "yourself", "yourselves",
			"you've", "z", "zero"));
	Set<String> mySet = new HashSet<String>(array);

	/*
	 * private EnglishAnalyzer english_analyzer = new EnglishAnalyzer(
	 * Version.LUCENE_36);
	 */
	// private StandardAnalyzer english_analyzer = new StandardAnalyzer(
	// Version.LUCENE_36, mySet);
	private StopAnalyzer english_analyzer = new StopAnalyzer(Version.LUCENE_36,
			mySet);
	// private SnowballAnalyzer english_analyzer = new
	// SnowballAnalyzer(Version.LUCENE_36, "English");
	@SuppressWarnings("deprecation")
	/*
	 * private BrazilianAnalyzer brazilian_analyzer = new BrazilianAnalyzer(
	 * Version.LUCENE_36, STOPWORDS);
	 */
	private DocVector[] docs = new DocVector[2];
	private String similarity_text = "";
	private String globalFilePath;
	private static String fileData;

	// @Override
	// public Lucene(int a){
	// english_analyzer.
	// }

	public String getSimilarityText() {

		return this.similarity_text;
	}

	public Lucene() {
		// Creates the index writer and location in memory
		IndexWriterConfig config = null;
		indexLocation = new RAMDirectory();

		// Selects the analyzer based on radio button
		try {
			if (PaperHack.language == 1) {
				config = new IndexWriterConfig(Version.LUCENE_36,
						english_analyzer);
			} /*
			 * else { config = new IndexWriterConfig(Version.LUCENE_36,
			 * brazilian_analyzer); }
			 */
			writer = new IndexWriter(indexLocation, config);
		} catch (Exception e) {
			System.out.println("Error creating Lucene object: "
					+ e.getMessage());
		}
	}

	public static Lucene createIndex() {
		// Indexing variable
		Lucene indexer = null;

		// Creates the Lucene's indexer
		try {
			indexer = new Lucene();
		} catch (Exception e) {
			System.out.println("Can't create index:" + e.getMessage());
			System.exit(-1);
		}

		return indexer;
	}

	public Lucene add_file_to_index(String file_path, int file_number) {
		// Try to add file into the index
		try {
			globalFilePath = file_path;
			this.indexFileOrDirectory(file_path, file_number);
		} catch (Exception e) {
			System.out.println("Error indexing file: " + e.getMessage());
		}

		return this;
	}

	public void closeIndex() {
		// After adding, we always have to call the closeIndex, otherwise the
		// index is not created

		IndexReader reader = null;
		Map<String, Integer> terms = null;
		try {
			// Really closes the index
			writer.close();

			// Open a cursor reader
			reader = IndexReader.open(indexLocation);

			// Find all terms in the index
			terms = new HashMap<String, Integer>();
			// Get values indexed as contents
			TermEnum termEnum = reader.terms(new Term("contents"));

			int pos = 0;
			while (termEnum.next()) {
				Term term = termEnum.term();

				// Skips if not contents field
				if (!"contents".equals(term.field())) {
					break;
				}

				terms.put(term.text(), pos++);
			}

			// Vector of two documents, 0 e 1
			int[] docIds = new int[] { 0, 1 };
			// Vector of documents

			int i = 0;
			for (int docId : docIds) {
				// Get the vector of terms in docId position
				TermFreqVector[] tf_vectors = reader.getTermFreqVectors(docId);
				// Append the vectors of terms to the vector docs
				docs[i] = new DocVector(terms);

				// For each term in vector
				for (TermFreqVector tf_vector : tf_vectors) {
					String[] termTexts = tf_vector.getTerms();

					// Term frequency is the number of times term appears in
					// document
					int[] termFreqs = tf_vector.getTermFrequencies();

					for (int j = 0; j < termTexts.length; j++) {
						docs[i].setEntry(termTexts[j], termFreqs[j]);
					}
				}

				// Apply normalization
				docs[i].normalize();
				i++;
			}

			reader.close();

		} catch (IOException e) {
			System.out.println("Error closing index: " + e.getMessage());
		}

		return;

	}

	public void closeIndex2() {
		// After adding, we always have to call the closeIndex, otherwise the
		// index is not created

		IndexReader reader = null;
		// Map<String, Integer> terms = null;
		try {
			// Really closes the index
			writer.close();

			// Open a cursor reader
			reader = IndexReader.open(indexLocation);

			// Find all terms in the index
			// Get values indexed as contents
			reader.close();

		} catch (IOException e) {
			System.out.println("Error closing index: " + e.getMessage());
		}

		return;

	}

	/* I'm getting the hits here */
	public String getTopHits(int position) {
		// Get values indexed as contents
		IndexReader reader = null;
		int[] max_freq = new int[6];
		String[] max_term = new String[6];
		int term_vect_length = 0;
		try {
			// Open a cursor reader
			reader = IndexReader.open(this.indexLocation);

			// Get the vector of terms in docId position
			TermFreqVector[] tf_vectors = reader.getTermFreqVectors(position);

			// For each term in vector
			for (TermFreqVector tf_vector : tf_vectors) {
				String[] termTexts = tf_vector.getTerms();
				term_vect_length = termTexts.length;

				// Term frequency is the number of times term appears in
				// document
				int[] termFreqs = tf_vector.getTermFrequencies();

				// Order the top 10 terms
				for (int i = 0; i < termTexts.length; i++) {
					if (termFreqs[i] >= max_freq[0]) {
						max_freq[0] = termFreqs[i];
						max_term[0] = termTexts[i];
					} else if (termFreqs[i] < max_freq[0]
							&& termFreqs[i] >= max_freq[1]) {
						max_freq[1] = termFreqs[i];
						max_term[1] = termTexts[i];
					} else if (termFreqs[i] < max_freq[1]
							&& termFreqs[i] >= max_freq[2]) {
						max_freq[2] = termFreqs[i];
						max_term[2] = termTexts[i];
					} else if (termFreqs[i] < max_freq[2]
							&& termFreqs[i] >= max_freq[3]) {
						max_freq[3] = termFreqs[i];
						max_term[3] = termTexts[i];
					} else if (termFreqs[i] < max_freq[3]
							&& termFreqs[i] >= max_freq[4]) {
						max_freq[4] = termFreqs[i];
						max_term[4] = termTexts[i];
					} else if (termFreqs[i] < max_freq[4]
							&& termFreqs[i] >= max_freq[5]) {
						max_freq[5] = termFreqs[i];
						max_term[5] = termTexts[i];
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Error closing index: " + e.getMessage());
		}

		String result = term_vect_length + " " + Configs.TERM + "<br/><br/>";
		String temporary = "";
		for (int i = 0; i < max_freq.length; i++) {
			ArrayList<String> temp = null;
			if (max_term[i] != null) {
				System.out.println("RADICAIS: " + max_term[i]);
				// ////////////////////////////////////////////////////////////////////////////////////
				// stringMatching(fileData, max_term[i]);
				temp = stringMatching(fileData.trim(), max_term[i]);
				String temporary2 = "";
				for (int j = 0; j < temp.size(); j++) {

					if (j == 0 && temp.size() >= 2)
						temporary2 = temp.get(j) + " OR ";
					else if (j == (temp.size() - 1))
						temporary2 = temporary2 + temp.get(j);
					else
						temporary2 = temporary2 + temp.get(j) + " OR ";
				}
				if (i == 0 && max_freq.length >= 2)
					temporary = "(" + temporary2 + ") AND ";
				else if (i == (max_freq.length - 1))
					temporary = temporary + "(" + temporary2 + ")";
				else if (i != (max_freq.length - 1) && i >= 1)
					temporary = temporary + "(" + temporary2 + ") AND ";



				for (String word : temp)
					System.out.println("Palavras: " + word);

				// result += max_term[i] + " " + max_freq[i] + "<br/>";
				result += max_term[i] + "<br/>";
			}
		}
		fileData="";
		String[] parts = temporary.split(" ");
		String lastWord = parts[parts.length - 1];
		if(lastWord.equals("AND"))
			for(int i=0;i<(parts.length-1); i++)
				fileData=fileData+" "+parts[i];
		else
			for(int i=0;i<(parts.length); i++)
				fileData=fileData+" "+parts[i];
		System.out.println(lastWord); //
		//fileData = temporary;
		System.out.println(">>>>>>>" + fileData);
		return result;
	}

	private void indexFileOrDirectory(String fileName, int fileNumber)
			throws IOException {
		// Gets the list of files in a folder (if submitted the name of a
		// folder)
		// or gets a single file name (submitted only the file name)
		add_files(new File(fileName));
		String results = "";

		// For each file in queue
		for (File file : queue) {
			FileReader fileContent = null;

			try {
				// Creates the document file
				Document documentFile = new Document();

				// Special treatment for PDF file
				if (file.getPath().toLowerCase().endsWith(".pdf")) {
					// Parse a PDF file
					FileInputStream fi = new FileInputStream(new File(
							file.getPath()));
					PDFParser parser = new PDFParser(fi);
					parser.parse();
					COSDocument cd = parser.getDocument();
					PDFTextStripper stripper = new PDFTextStripper();
					// String text = stripper.getText(new PDDocument(cd));
					fileData = stripper.getText(new PDDocument(cd));
					cd.close();

					// Create temporary file.
					File tempFile = File.createTempFile("temp_file", ".tmp");

					// Write parsed file text to temp_file
					BufferedWriter out = new BufferedWriter(new FileWriter(
							tempFile));
					// out.write(text);
					out.write(fileData);
					out.close();

					// Add contents attribute of file to documents
					fileContent = new FileReader(tempFile);

					// Delete temporary file
					tempFile.delete();
				} else {
					// Add contents attribute of file to documents
					fileData = "";
					fileContent = new FileReader(file);
					BufferedReader in = new BufferedReader(fileContent);
					String line;
					while ((line = in.readLine()) != null) {
						fileData += " " + line.trim();
					}
					in.close();
					fileContent = new FileReader(file);
				}
				System.out.println("===BEGINNING OF THE PDF FILE OUTPUT:===\n"
						+ fileData + "===END OF THE PDF FILE OUTPUT===\n");

				// Documents have the original path
				// doc.add(new Field("path", file.getPath(), Field.Store.YES,
				// Field.Index.ANALYZED, TermVector.YES));
				// File contents
				documentFile.add(new Field("contents", fileContent,
						TermVector.YES));
				// document_file.add(new Field("content", value,
				// Field.Store.YES, Field.Index.ANALYZED));
				// original filename
				// doc.add(new Field("filename", file.getName(),
				// Field.Store.YES, Field.Index.ANALYZED, TermVector.YES));

				// Adds document to index and commit changes
				writer.addDocument(documentFile);
				writer.commit();

				// Get file info
				String[] splited_path = file.toString().split("/");
				results = splited_path[splited_path.length - 1];
				results += "<br/><br/>" + (file.length() / 1024) + " KB";
				results += "<br/><br/>" + getTopHits(fileNumber - 1);
			} catch (Exception e) {
				System.out.println("Could not add: " + file + " "
						+ e.getMessage());
			} finally {
				fileContent.close();
			}
		}

		// Cleans-up the file queue
		queue.clear();

		// Answers
		PaperHack.update_results(results, fileNumber);
	}

	private void add_files(File file) {
		// Check if file selected exists
		if (!file.exists()) {
			System.out.println(file + " does not exist.");
		}

		// Each file on selected directory
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				add_files(f);
			}
			// Simple file
		} else {
			String filename = file.getName().toLowerCase();
			// Only supported text files
			if (filename.endsWith(".htm") || filename.endsWith(".html")
					|| filename.endsWith(".xml") || filename.endsWith(".txt")
					|| filename.endsWith(".pdf")) {
				System.out.println("!!!!!!!!!!!" + file);
				queue.add(file);
			} else {
				System.out.println("File not suported. Skipped: " + filename);
			}
		}

	}

	public ArrayList<String> stringMatching(String text, String root) {
		ArrayList<String> results = new ArrayList<String>();

		String sPattern = "(?i)\\b\\s+" + Pattern.quote(root) + "\\w*\\b";
		// String sPattern = "(?i)\\b*" + Pattern.quote(searchTerm) + "\\w*\\b";

		Pattern pattern = Pattern.compile(sPattern);
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			String t = matcher.group().trim();
			t = t.toLowerCase();
			if (!results.contains(t))
				results.add(t);
		}

		System.out.println("RAIZ: " + root);
		for (String temp : results)
			System.out.println(temp);

		System.out.println("######");
		return results;
	}

	public void stringMatching2(String input) {
		File file = new File(globalFilePath);
		//System.out.println("GGGGGGGGG");
		int maxHits = 6;
		try {
			Analyzer stdAn = new StandardAnalyzer(Version.LUCENE_36);
			Directory fsDir = new RAMDirectory();
			IndexWriterConfig myconfig = new IndexWriterConfig(
					Version.LUCENE_36, stdAn);
			IndexWriter mywriter = new IndexWriter(fsDir, myconfig);

			// Leitura Arquivo
			Document documentFile = new Document();
			FileReader fileContent = new FileReader(file);
			documentFile
					.add(new Field("contents", fileContent, TermVector.YES));
			// document_file.add(new Field("content", value,
			// Field.Store.YES, Field.Index.ANALYZED));
			// original filename
			// doc.add(new Field("filename", file.getName(),
			// Field.Store.YES, Field.Index.ANALYZED, TermVector.YES));

			// Adds document to index and commit changes
			mywriter.addDocument(documentFile);
			mywriter.commit();
			mywriter.close();

			IndexReader reader = IndexReader.open(fsDir);
			IndexSearcher searcher = new IndexSearcher(reader);

			String dField = "contents";

			QueryParser parser = new QueryParser(Version.LUCENE_36, dField,
					stdAn);
			Query q = parser.parse("+thomaz*");
			TopDocs hits = searcher.search(q, maxHits);
			ScoreDoc[] scoreDocs = hits.scoreDocs;
			for (int n = 0; n < scoreDocs.length; ++n) {
				ScoreDoc sd = scoreDocs[n];
				float score = sd.score;
				int docId = sd.doc;
				Document d = searcher.doc(docId);
				System.out.println(">>>>>" + d.get("contents"));
			}
			searcher.close();
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ArrayList<String> stringMatching(String input) {

		ArrayList<String> list = new ArrayList<String>();

		try {

			System.out.println("Entrei!!!");
			System.out.println("INPUT: " + input);
			@SuppressWarnings("deprecation")
			// Query query = new QueryParser(Version.LUCENE_36, "contents",
			// english_analyzer).parse(input);
			QueryParser qParser = new QueryParser(Version.LUCENE_36,
					"contents", english_analyzer);

			Query query = qParser.parse(input + "*");

			IndexReader reader = IndexReader.open(indexLocation);
			// IndexReader reader =
			IndexSearcher searcher = new IndexSearcher(reader);

			TopScoreDocCollector collector = TopScoreDocCollector.create(10,
					true);

			searcher.search(query, collector);

			ScoreDoc[] hits = collector.topDocs().scoreDocs;

			System.out.println("Found " + hits.length + " for the input "
					+ input + ".");
			for (int i = 0; i < hits.length; i++) {
				int docId = hits[i].doc;
				System.out.println("INTEIRO " + docId);
				Document d = searcher.doc(docId);
				System.out.println("Match " + i + ":" + d.get("contents"));
				list.add(d.get("contents"));
			}
			reader.close();
			return list;

		} catch (IOException e) {
			System.out.println("Could not open index: " + " " + e.getMessage());
			return null;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public static String getFileData() {
		return fileData;
	}
	
	


	
}