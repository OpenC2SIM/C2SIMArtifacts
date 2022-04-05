package de.fraunhofer.fkie.xsd;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.search.EntitySearcher;

public class OntologyClass extends OntologyEntity{

	OWLClass owlClass;
	OWLOntology ontology;
	
	private List <OntologyClass> Superclasses= new ArrayList<OntologyClass>();
	private List <OntologyClass> Subclasses= new ArrayList<OntologyClass>();
	private List <OntologyClass> eqClasses= new ArrayList<OntologyClass>();
//	private List <String> superclassList = new ArrayList<String>();
//	private List <String> subclassList = new ArrayList<String>();
	private List <DataRestriction> drList = new ArrayList<DataRestriction>();
	private List <ObjectRestriction> orList = new ArrayList<ObjectRestriction>();
	private List <Instance> inList = new ArrayList<Instance>();
	
	//Constructor
	public OntologyClass(OWLClass owlClass, OWLOntology ontology) {
		this.owlClass = owlClass;
		this.ontology = ontology;
		
		this.setUri(owlClass.toString().replaceAll("<", "").replaceAll(">", ""));
		
		this.setName(this.getUri().replaceAll("http:\\S+#", ""));
		
		EntitySearcher.getAnnotationAssertionAxioms(owlClass, ontology).
			forEach(x->this.setDocumentation(x.getValue().toString().
				replaceAll("\\^\\^xsd:string", "")));
		
		
		
		EntitySearcher.getInstances(owlClass, ontology).
			forEach(x->this.addInstance
					(new Instance(x.toString().replaceAll("<http:\\S+#", "").replaceAll(">", ""))));


	}
	
	
	public OWLClass getOwlClass() {
		return owlClass;
	}

	public OWLOntology getOntology() {
		return ontology;
	}


	public void addSuperclass(OntologyClass oc) {
		this.Superclasses.add(oc);
	}
	
	public OntologyClass getSuperclass(int i) {
		return Superclasses.get(i);
	}
	
	public int getSuperclassAmount() {
		return Superclasses.size();
	}
	
	public void addDirSubclass(OntologyClass dirSubclass) {
		this.Subclasses.add(dirSubclass);
	}
	
	public OntologyClass getSubclass(int i) {
		return Subclasses.get(i);
	}
	
	public Stream subclasses() {
		return this.Subclasses.stream();
	}
	
	public int getSubclassAmount() {
		return Subclasses.size();
	}
	
	public void addEqClass(OntologyClass eqClass) {
		this.eqClasses.add(eqClass);
	}
	
	public OntologyClass getEqClasses(int i) {
		return eqClasses.get(i);
	}
	
	public int getEqClassAmount() {
		return eqClasses.size();
	}
	
	public void addDataRestriction(DataRestriction dr) {
		drList.add(dr);
	}
	
	public DataRestriction getDataRestriction(int i) {
		return drList.get(i);
	}
	
	public int getDataRestrictionAmount() {
		return drList.size();
	}
	
	public void addObjectRestriction (ObjectRestriction or) {
		orList.add(or);
	}
	
	public ObjectRestriction getObjectRestriction(int i) {
		return orList.get(i);
	}
	
	public int getObjectRestrictionAmount() {
		return orList.size();
	}
	
	public void addInstance (Instance in) {
		inList.add(in);
	}
	
	public Instance getInstance(int i) {
		return inList.get(i);
	}
	
	public int getInstanceAmount() {
		return inList.size();
	}
	
	//Kopiert die ObjectRestrictions einer anderen Klasse.
	public void copyObjectRestrictions(OntologyClass oc_toCopy) {
		for (int i = 0; i < oc_toCopy.getObjectRestrictionAmount(); i++) {
			this.addObjectRestriction(oc_toCopy.getObjectRestriction(i));
		}
		
	}
	
	//Kopiert die DataRestrictions einer anderen Klasse.
	public void copyDataRestrictions(OntologyClass oc_toCopy) {
		for (int i = 0; i < oc_toCopy.getDataRestrictionAmount(); i++) {
			this.addDataRestriction(oc_toCopy.getDataRestriction(i));
		}
		
	}
	
	public void bequeathRestrictions() {
		if (this.getSubclassAmount()>0) {
			for (int i = 0; i < this.getSubclassAmount(); i++) {
				this.getSubclass(i).copyObjectRestrictions(this);
				this.getSubclass(i).copyDataRestrictions(this);
				
				this.getSubclass(i).bequeathRestrictions();
			}
		}
		else {
			System.out.println(this.getName()+" has no subclasses.");
		}
	}
	
	public String printPList(List pList) {
		String s = "";
		for (int i = 0; i<pList.size(); i++) {
			s = s + pList.get(i).toString();
		}
		return s;
	}
	
	public String printDirSuperclassesList() {
		String s = "";
		for (int i = 0; i<this.Superclasses.size(); i++) {
			s = s + this.Superclasses.get(i).toString();
		}
		return s;
	}
	
//	@Override
//	public String toString() {
//		return "OntologyClass [name=" + this.getName() + ", dirSuperclass=" + printDirSuperclassesList() + ", superclassList=" + superclassList
//				+ ", drList=" + printPList(drList) + ", orList=" + printPList(orList) + "]";
//	}
}