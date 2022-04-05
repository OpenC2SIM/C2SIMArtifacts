package de.fraunhofer.fkie.xsd;

import java.util.List;
import java.util.ArrayList;

public class DataType extends OntologyEntity{
	private List<Facet> Typedefinitions = new ArrayList<Facet>();

	public Facet getTypedefinition(int i) {
		return Typedefinitions.get(i);
	}

	public void setTypedefinition(Facet typedefinition) {
		Typedefinitions.add(typedefinition); 
	}
	
	public String printTypedefinitions() {
		String s = "";
		for (int i = 0; i < Typedefinitions.size(); i++) {
			s += Typedefinitions.get(i).getFacetType() + Typedefinitions.get(i).getFacetValue() +  ", ";
		}
		return s;
	}
	
	public int getTypedefinitionAmount() {
		return Typedefinitions.size();
	}
	
	@Override
	public String toString() {
		return "Datatype [Typename=" + this.getName() + ", Typedefinitions=" + printTypedefinitions() + "]";
	}

	public static void main(String[] args) {
		

	}
}