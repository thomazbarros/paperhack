package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IeeeSearch {

	private static String urlRoot = "http://ieeexplore.ieee.org/gateway/ipsSearch.jsp?querytext=";

	public static void doSearch(String input) {

		System.out.println("Input doSearch: " + input);
		File file = new File("/Users/thomaz/testando.txt");
		List<Paper> dados = new ArrayList<Paper>();
		try {
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			DocumentBuilder b = f.newDocumentBuilder();
			Document doc = b.parse(urlRoot + input);

			doc.getDocumentElement().normalize();
			System.out.println("Root element: "
					+ doc.getDocumentElement().getNodeName());

			// loop through each item
			NodeList items = doc.getElementsByTagName("document");
			for (int i = 0; i < items.getLength(); i++) {
				Node n = items.item(i);
				if (n.getNodeType() != Node.ELEMENT_NODE)
					continue;
				Element e = (Element) n;

				// get the "title elem" in this item (only one)
				NodeList titleList = e.getElementsByTagName("title");
				Element titleElem = (Element) titleList.item(0);

				NodeList abstractList = e.getElementsByTagName("abstract");
				Element abstractElem = (Element) abstractList.item(0);

				NodeList pdfList = e.getElementsByTagName("pdf");
				Element pdfElem = (Element) pdfList.item(0);

				// get the "text node" in the title (only one)
				Node titleNode = titleElem.getChildNodes().item(0);
				Node abstractNode = abstractElem.getChildNodes().item(0);
				Node pdfNode = pdfElem.getChildNodes().item(0);

				// artigo.setTitulo(titleNode.getNodeValue());
				// artigo.setResumo(abstractNode.getNodeValue());

				dados.add(new Paper(titleNode.getNodeValue(), abstractNode
						.getNodeValue(), pdfNode.getNodeValue()));
				// artigo = null;

				// System.out.println(titleNode.getNodeValue());
				// escreveArquivo(dados, file);

			}
			writeFile(dados, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String doSearch2(String input) {

		input = input.trim();
		System.out.println("Input doSearch: " + input);
		List<Paper> data = new ArrayList<Paper>();
		try {
			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			DocumentBuilder b = f.newDocumentBuilder();
			String temp = urlRoot + input;
			temp = temp.trim();
			System.out.println("URL MONTADA: " + temp);
			Document doc = b.parse(temp);

			doc.getDocumentElement().normalize();
			System.out.println("Root element: "
					+ doc.getDocumentElement().getNodeName());

			// loop through each item
			NodeList items = doc.getElementsByTagName("document");
			for (int i = 0; i < items.getLength(); i++) {
				Node n = items.item(i);
				if (n.getNodeType() != Node.ELEMENT_NODE)
					continue;
				Element e = (Element) n;

				// get the "title elem" in this item (only one)
				NodeList titleList = e.getElementsByTagName("title");
				Element titleElem = (Element) titleList.item(0);

				NodeList abstractList = e.getElementsByTagName("abstract");
				Element abstractElem = (Element) abstractList.item(0);

				NodeList pdfList = e.getElementsByTagName("pdf");
				Element pdfElem = (Element) pdfList.item(0);

				// get the "text node" in the title (only one)
				Node titleNode = titleElem.getChildNodes().item(0);
				Node abstractNode = abstractElem.getChildNodes().item(0);
				Node pdfNode = pdfElem.getChildNodes().item(0);

				data.add(new Paper(titleNode.getNodeValue(), abstractNode
						.getNodeValue(), pdfNode.getNodeValue()));
			}
			return prepareString(data);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String prepareString(List<Paper> dados2) {
		String data = "";

		List<Paper> dados = new ArrayList<Paper>();
		dados = dados2;

		for (Paper dado : dados) {
			data += "Título: \n";
			data += dado.getTitle() + "\n";

			data += "Abstract: \n";
			data += dado.getAbstract() + "\n";

			data += "Link: \n";
			data += dado.getLink() + "\n\n";

		}
		System.out.println("Done");
		return data;
	}

	public static void writeFile(List<Paper> dados2, File file) {
		try {

			List<Paper> dados = new ArrayList<Paper>();
			dados = dados2;
			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);

			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			for (Paper dado : dados) {
				bw.write("Título: ");
				bw.write(dado.getTitle());
				bw.newLine();
				bw.write("Abstract: ");
				bw.write(dado.getAbstract());
				bw.newLine();
				bw.write("Link: ");
				bw.write(dado.getLink());
				bw.newLine();
				bw.newLine();
			}
			bw.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
