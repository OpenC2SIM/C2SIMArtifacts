package de.fraunhofer.fkie.xsd;

public class Facet {
	
	private String facetType;
	private String facetValue;
	
	Facet(String Type, String Value){
		this.facetType = Type;
		this.facetValue = Value;
	}
	
	public String getFacetType() {
		return facetType;
	}
	public void setFacetType(String facetType) {
		this.facetType = facetType;
	}
	public String getFacetValue() {
		return facetValue;
	}
	public void setFacetValue(String facetValue) {
		this.facetValue = facetValue;
	}
}
