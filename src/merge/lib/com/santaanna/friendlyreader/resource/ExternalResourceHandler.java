package merge.lib.com.santaanna.friendlyreader.resource;

public class ExternalResourceHandler {

	private String taggerPath;
	private String synonymsPath;
	
	public String getTaggerPath() {
		return taggerPath;
	}
	
	public void setTaggerPath(String taggerPath) {
		if (this.taggerPath == null) {
			this.taggerPath = taggerPath;
		} else {
			System.out.println("ERROR: Cannot set taggerPath more than once!");
		}
	}
	
	public String getSynonymsPath() {
		return synonymsPath;
	}
	
	public void setSynonymsPath(String synonymsPath) {
		if (this.synonymsPath == null) {
			this.synonymsPath = synonymsPath;
		} else {
			System.out.println("ERROR: Cannot set synonymsPath more than once!");
		}
	}
}
