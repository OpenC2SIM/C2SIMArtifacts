package de.fraunhofer.fkie.xsd;

public class DataRestriction {
	

	private String restrictionName;
	private String datatype;
	private String value;
	private String restrictionType;
	
	public String getRestrictionType() {
		return restrictionType;
	}

	public void setRestrictionType(String restrictionType) {
		this.restrictionType = restrictionType;
	}
	
	public String getRestrictionName() {
		return restrictionName;
	}
	
	public void setPropertyName(String propertyName) {
		this.restrictionName = propertyName;
	}
	
	public String getDatatype() {
		return datatype;
	}
	
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public void print() {
		System.out.println("Restriction Name: " + this.getRestrictionName());
		System.out.println("Datatype: " + this.getDatatype());
		System.out.println("Value: " + this.getValue());
		System.out.println("Restriction Type: " + this.getRestrictionType());
	}
	
	@Override
	public String toString() {
		return restrictionName + ", ";
	}
}