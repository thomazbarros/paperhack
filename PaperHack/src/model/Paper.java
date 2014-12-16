package model;

public class Paper {
	
	private String title;
	private String pAbstract;
	private String link;	
	
	public Paper() {
	}
	
	public Paper(String title, String pAbstract, String link) {
		super();
		this.title = title;
		this.pAbstract = pAbstract;
		this.link = link;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAbstract() {
		return pAbstract;
	}
	public void setAbstract(String pAbstract) {
		this.pAbstract = pAbstract;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
}
