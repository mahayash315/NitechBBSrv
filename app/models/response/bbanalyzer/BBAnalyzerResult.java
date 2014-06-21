package models.response.bbanalyzer;

import java.util.List;

public class BBAnalyzerResult {
	
	private List<String> surfaces;
	private List<String> features;
	
	
	
	public List<String> getSurfaces() {
		return surfaces;
	}
	public void setSurfaces(List<String> surfaces) {
		this.surfaces = surfaces;
	}
	public List<String> getFeatures() {
		return features;
	}
	public void setFeatures(List<String> features) {
		this.features = features;
	}
}
