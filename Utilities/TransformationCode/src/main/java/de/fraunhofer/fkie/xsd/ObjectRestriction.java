package de.fraunhofer.fkie.xsd;

public class ObjectRestriction {

	private String restrictionName;
	private String object;
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
	
	public void setRestrictionName(String restrictionName) {
		this.restrictionName = restrictionName;
	}
	
	public String getObject() {
		return object;
	}
	
	public void setObject(String object) {
		this.object = object;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return restrictionName + ", ";
	}
	
	public void print() {
		System.out.println("Property Name: " + this.getRestrictionName());
		System.out.println("Object: " + this.getObject());
		System.out.println("Value: " + this.getValue());
		System.out.println("Restriction Type: " + this.getRestrictionType());
	}
}