
package de.fraunhofer.fkie.xsd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLAxiomVisitor;
import org.semanticweb.owlapi.model.OWLCardinalityRestriction;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitor;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDataRangeVisitor;
import org.semanticweb.owlapi.model.OWLDataRestriction;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLDatatypeRestriction;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNaryDataRange;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectCardinalityRestriction;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLPropertyExpressionVisitor;
import org.semanticweb.owlapi.model.OWLQuantifiedDataRestriction;
import org.semanticweb.owlapi.model.OWLQuantifiedObjectRestriction;
import org.semanticweb.owlapi.model.OWLRestriction;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.util.InferredEquivalentClassAxiomGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;

import com.google.common.collect.Multimap;

import org.semanticweb.owlapi.util.AxiomSubjectProviderEx;

import gnu.trove.impl.sync.TSynchronizedObjectShortMap;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.Reasoner;

public class OwlToJavaTransformer {
	
	//1.Attribute
	private OWLOntology o;
	private OWLReasoner owl_r;
	private org.semanticweb.HermiT.Reasoner hermit_r;
	private List<OWLClass> owl_supercList;
	private List<OWLClass> owl_subcList;
	private List<OWLObjectPropertyExpression> owl_sub_opList;
	private List<OWLDataPropertyExpression> owl_sub_dpList;
	private List<OWLAxiom> aList;
	private List<OWLAxiom> dpaList;
	private IRI  iri = IRI.create("MergeForXsd");
	
	//2.Constructor: Creates Ontology and Reasoner form the files array given.
	public OwlToJavaTransformer(ArrayList<File> ofiles, File owlOutput, boolean doReasoning) throws OWLOntologyCreationException, OWLOntologyStorageException, FileNotFoundException{
		OWLOntologyManager oman = OWLManager.createOWLOntologyManager();
		this.o = oman.createOntology(iri);
			
		for (File file : ofiles) {
				OWLOntology o2 = oman.loadOntologyFromOntologyDocument(file);
				o2.axioms().forEach(o::add);				
		}

			System.out.println("Ontology loaded: Axiom-Count:" + o.getAxiomCount());

			oman.saveOntology(o, new FileOutputStream(owlOutput));
			System.out.println("Ontology merged and written to: "+ owlOutput.getAbsolutePath());
			
	        Configuration c = new Configuration();
	        c.reasonerProgressMonitor = new ConsoleProgressMonitor();
			
			OWLReasonerFactory rf = new StructuralReasonerFactory();
			owl_r = rf.createReasoner(o);
			hermit_r = new Reasoner(c, o);
			
			
			if(doReasoning) {
				InferredSubClassAxiomGenerator sub_generator = new  InferredSubClassAxiomGenerator();
				Set<OWLSubClassOfAxiom> sub_axioms = sub_generator.createAxioms(oman.getOWLDataFactory(), hermit_r);
				sub_axioms.parallelStream().forEach(x->o.add(x));
			}		
	}
	
	
	//3.1 Auxiliary Methods	
	void addTypeDefinition(DataType dt, OWLDatatype owl_dt) {
		
		//To-Do: Isn't there a more readable way to do this?
		o.datatypeDefinitions(owl_dt).
		map(x->x.getDataRange()).
		map(x->(OWLDatatypeRestriction) x).
		findFirst().
		ifPresent(x->x.facetRestrictions().
				forEach(y-> dt.setTypedefinition
						(new Facet(y.getFacet().toString(), 
									y.getFacetValue().toString().
										replaceAll("\"", "").
										replaceAll("\\\\\\\\", "\\\\") //Not sure, why there are two backlashes in ontology
									))));
		

	}
		//The visitor pattern forces us to write this one big method
		//consisting of two sub methods.
	void addClassDefinition(OntologyClass oc, OWLAxiom ax) {
		
		ax.accept(new OWLObjectVisitor() {
			
			//Process defined classes (equivalences)
			public void visit(OWLEquivalentClassesAxiom eqAx) {
				
//				eqAx.namedClasses().
//					//filter(x->!x.toString().equals(AxiomSubjectProviderEx.getSubject(eqAx).toString())).
//					forEach(x->System.out.println(eqAx.toString()+": "+x.toString()));
				

				
//				eqAx.signature().
//					//filter(x->x.getClassExpressionType().equals("OjectInter))
//					forEach(x->System.out.println("Something: "+x.toString()));
				
//				eqAx.namedClasses().
//					filter(x->!x.toString().equals(AxiomSubjectProviderEx.getSubject(eqAx).toString())).
//					forEach(x->oc.addDirSuperclass(x.toString().replaceAll("<http:\\S+#", "").replaceAll(">", "")));
				
				
				
				List <OWLCardinalityRestriction> owl_orList = new ArrayList<OWLCardinalityRestriction>();
				List <OWLCardinalityRestriction> owl_drList = new ArrayList<OWLCardinalityRestriction>();

				
				
				eqAx.nestedClassExpressions().
					filter(x->x.getClassExpressionType().toString().startsWith("Object")).
					filter(x->x.getClassExpressionType().toString().endsWith("Cardinality")).
					forEach(x->owl_orList.add((OWLCardinalityRestriction) x));
				
				eqAx.nestedClassExpressions().
					filter(x->x.getClassExpressionType().toString().startsWith("Data")).
					filter(x->x.getClassExpressionType().toString().endsWith("Cardinality")).
						forEach(x->owl_drList.add((OWLCardinalityRestriction) x));

				
				//Add Object Restrictions to Java Ontology Class.
				for (int i = 0; i < owl_orList.size(); i++) {
						
						ObjectRestriction or = new ObjectRestriction();
						
						or.setValue(String.valueOf(owl_orList.get(i).getCardinality()));
			
						owl_orList.get(i).objectPropertiesInSignature().
							findAny().
							ifPresent(x->or.setRestrictionName
									(x.toString().replaceAll("<http:\\S+#", "").replaceAll(">", "")));
						

						owl_orList.get(i).classesInSignature().
						findAny().
							ifPresent(x->or.setObject
									(x.toString().replaceAll("<http:\\S+#", "").replaceAll(">", "")));
						
						or.setRestrictionType(owl_orList.get(i).getClassExpressionType().toString());
						
						System.out.println("Restriction Name: "+ or.getRestrictionName());
						oc.addObjectRestriction(or);
					}

				
				//Add Data Restrictions to Java Ontology Class.
				for (int i = 0; i < owl_drList.size(); i++) {
					
					DataRestriction dr = new DataRestriction();
					
					dr.setValue(String.valueOf(owl_drList.get(i).getCardinality()));
					

					owl_drList.get(i).dataPropertiesInSignature().
						findAny().
						ifPresent(x->dr.setPropertyName
								(x.toString().replaceAll("<http:\\S+#", "").replaceAll(">", "")));

					

					owl_drList.get(i).datatypesInSignature().
					findAny().
					ifPresent(x->dr.setDatatype
								(x.toString().replaceAll("<http:\\S+#", "").replaceAll(">", "")));
					
					dr.setRestrictionType(owl_drList.get(i).getClassExpressionType().toString());
					
					
					oc.addDataRestriction(dr);
					}
				}
				
				
			
			//Process primitve classes.
			public void visit(OWLSubClassOfAxiom subClassAxiom) {	
				
	
				if (subClassAxiom.getSuperClass().getClassExpressionType().toString().equals("Class")){
					//Verbessern: Kann ich den String nicht besser zurechtschneiden?
//					oc.addDirSuperclass
//						(subClassAxiom.getSuperClass().
//								toString().replaceAll("<http:\\S+#", "").replaceAll(">", ""));
				}
		
										
				if (subClassAxiom.getSuperClass().getClassExpressionType().toString().startsWith("DataExactCardinality")
					||	
					subClassAxiom.getSuperClass().getClassExpressionType().toString().equals("DataMaxCardinality")
					||	
					subClassAxiom.getSuperClass().getClassExpressionType().toString().equals("DataMinCardinality")
					) {
					DataRestriction dr = new DataRestriction();
					
					OWLCardinalityRestriction cr = (OWLCardinalityRestriction) subClassAxiom.getSuperClass();
					dr.setValue(String.valueOf(cr.getCardinality()));
					

					cr.dataPropertiesInSignature().
						findAny().
						ifPresent(x->dr.setPropertyName
								(x.toString().replaceAll("<http:\\S+#", "").replaceAll(">", "")));

					

					cr.datatypesInSignature().
					findAny().
					ifPresent(x->dr.setDatatype
								(x.toString().replaceAll("<http:\\S+#", "").replaceAll(">", "")));
					
					dr.setRestrictionType(subClassAxiom.getSuperClass().getClassExpressionType().toString());
					
					
					oc.addDataRestriction(dr);
					}
				
				if (subClassAxiom.getSuperClass().getClassExpressionType().toString().equals("DataSomeValuesFrom")){
					DataRestriction dr = new DataRestriction();
					
					OWLQuantifiedDataRestriction qr = (OWLQuantifiedDataRestriction) subClassAxiom.getSuperClass();
					
					dr.setValue("some");

					qr.dataPropertiesInSignature().
						findAny().
						ifPresent(x->dr.setPropertyName
								(x.toString().replaceAll("<http:\\S+#", "").replaceAll(">", "")));

					

					qr.datatypesInSignature().
					findAny().
					ifPresent(x->dr.setDatatype
								(x.toString().replaceAll("<http:\\S+#", "").replaceAll(">", "")));
					
					dr.setRestrictionType(subClassAxiom.getSuperClass().getClassExpressionType().toString());
					
					
					oc.addDataRestriction(dr);
				}
				
				
				
				if (subClassAxiom.getSuperClass().getClassExpressionType().toString().equals("ObjectExactCardinality")
						||	
					subClassAxiom.getSuperClass().getClassExpressionType().toString().equals("ObjectMaxCardinality")
						||	
					subClassAxiom.getSuperClass().getClassExpressionType().toString().equals("ObjectMinCardinality")
						) {
					ObjectRestriction or = new ObjectRestriction();
					
					OWLCardinalityRestriction cr = (OWLCardinalityRestriction) subClassAxiom.getSuperClass();
					
					or.setValue(String.valueOf(cr.getCardinality()));
					

					
					cr.objectPropertiesInSignature().
						findAny().
						ifPresent(x->or.setRestrictionName
								(x.toString().replaceAll("<http:\\S+#", "").replaceAll(">", "")));
					

					cr.classesInSignature().
					findAny().
						ifPresent(x->or.setObject
								(x.toString().replaceAll("<http:\\S+#", "").replaceAll(">", "")));
					
					or.setRestrictionType(subClassAxiom.getSuperClass().getClassExpressionType().toString());

					oc.addObjectRestriction(or);
				}
				
				if (subClassAxiom.getSuperClass().getClassExpressionType().toString().equals("ObjectSomeValuesFrom")){
					ObjectRestriction or = new ObjectRestriction();
					
					OWLQuantifiedObjectRestriction qr = (OWLQuantifiedObjectRestriction) subClassAxiom.getSuperClass();
					
					or.setValue("some");				

					
					qr.objectPropertiesInSignature().
						findAny().
						ifPresent(x->or.setRestrictionName
								(x.toString().replaceAll("<http:\\S+#", "").replaceAll(">", "")));
					

					qr.classesInSignature().
					findAny().
						ifPresent(x->or.setObject
								(x.toString().replaceAll("<http:\\S+#", "").replaceAll(">", "")));
					
					or.setRestrictionType(subClassAxiom.getSuperClass().getClassExpressionType().toString());

					oc.addObjectRestriction(or);
				}
		
				}
			});
	}	
	
	
	//Iterates backwards over all classes already built
	//to extract properties of superclasses.
	void addSuperClassObjectRestrictions(OntologyClass oc, List<OntologyClass> ocList){
		int class_count = 0;
		for(int i = ocList.size()-1; i >= 0; i--) {
			for(int j = 0; j < oc.getSuperclassAmount(); j++) {
				if(oc.getSuperclass(j).equals(ocList.get(i).getName())) {
					oc.copyObjectRestrictions(ocList.get(i));
					class_count += 1;
				}
			}
			if(class_count == oc.getSuperclassAmount()) {
				break;
			}
		}
	}
	
	//Iterates backwards over all classes already built
	//to extract properties of superclasses.
		void addSuperClassDataRestrictions(OntologyClass oc, List<OntologyClass> ocList){
			int class_count = 0;
			for(int i = ocList.size()-1; i >= 0; i--) {
				for(int j = 0; j < oc.getSuperclassAmount(); j++) {
					if(oc.getSuperclass(j).equals(ocList.get(i).getName())) {
						oc.copyDataRestrictions(ocList.get(i));
						class_count += 1;
					}
				}
				if(class_count == oc.getSuperclassAmount()) {
					break;
				}
			}
		}
	
	//3.2 Methods	
	public List<DataType> createDataTypeList(){
		List<DataType> dtList = new ArrayList<DataType>();
		List<OWLDatatype> owl_dtList = o.datatypesInSignature().
			collect(Collectors.toList());
		

		
		for(int i = 0; i < owl_dtList.size(); i++) {
			DataType dt = new DataType();
			dt.setName(owl_dtList.get(i).toString().
					replaceAll("<http:\\S+#", "").replaceAll(">", ""));
			
			dt.setUri(owl_dtList.get(i).toString().
					replaceAll("<", "").replaceAll(">", ""));
			
			EntitySearcher.getAnnotationAssertionAxioms(owl_dtList.get(i), o).		
				forEach(x->dt.setDocumentation(x.getValue().toString().
					replaceAll("\\^\\^xsd:string", "")));
			
			addTypeDefinition(dt, owl_dtList.get(i));
		
			
			dtList.add(dt);
		}	
		
		return dtList;
	}
	
	public List<DataProperty> createDataPropertyList(){
		DataProperty top_dp = new DataProperty();
		List<DataProperty> dpList = new ArrayList<DataProperty>();
		
		
		
		OWLDataProperty owl_dp = owl_r.getTopDataPropertyNode().entities().findFirst().get();
		
		owl_sub_dpList = owl_r.getSubDataProperties(owl_dp).entities().
				collect(Collectors.toList());
		
		
		for (int i = 0; i < owl_sub_dpList.size(); i++) {
			DataProperty sub_dp = new DataProperty();
			sub_dp.setName(owl_sub_dpList.get(i).toString().
					replaceAll("<http:\\S+#", "").replaceAll(">", ""));
			
			sub_dp.setUri(owl_sub_dpList.get(i).toString().
					replaceAll("<", "").replaceAll(">", ""));
			
			EntitySearcher.getAnnotationAssertionAxioms((OWLEntity) owl_sub_dpList.get(i), o).		
				forEach(x->sub_dp.setDocumentation(x.getValue().toString().
						replaceAll("\\^\\^xsd:string", "")));
			
			dpaList = o.dataPropertyRangeAxioms((OWLDataProperty) owl_sub_dpList.get(i)).collect(Collectors.toList());
			
			if(dpaList.size() == 1) {
				sub_dp.setRange(dpaList.get(0).datatypesInSignature().findFirst().get().toString()
						.replaceAll("<http:\\S+#", "").replaceAll("xsd:", "xs:").replaceAll(">", ""));
			}
			
			else if(dpaList.size() == 0) {
				System.out.println("No range at: "+sub_dp.getName());
			}
			
			else{
				System.out.println(sub_dp.getName()+ " has more than one range.");
			}
			
			dpList.add(sub_dp);
				
		}
		System.out.println(dpList.size()+ " Data Properties have been built.");
		return dpList;
	}
	
	public List<ObjectProperty> createObjectPropertyList(){
		List<ObjectProperty> opList = new ArrayList<ObjectProperty>();
		
		//Get the OWL Top ObjectProperty
		OWLObjectPropertyExpression owl_op_ex = owl_r.getTopObjectPropertyNode().
				entities().
				findFirst().
				get();
		
		//The Reasoner does, for some reason, create the inverse property of every property;
		//those need to be filtered out.
		owl_sub_opList = owl_r.getSubObjectProperties(owl_op_ex).
				entities().
				filter(x->!x.toString().contains("ObjectInverseOf")).
				filter(x->!x.toString().startsWith("owl:bottomObjectProperty")).
				collect(Collectors.toList());
		
		System.out.println("Number of OWL Object Expressions: "+owl_sub_opList.size());
		
		for (int i = 0; i < owl_sub_opList.size(); i++) {
			ObjectProperty op = new ObjectProperty();
			op.setPropertyName(owl_sub_opList.get(i).toString().replaceAll("<http:\\S+#", "").replaceAll(">", ""));
			
			owl_r.getObjectPropertyRanges(owl_sub_opList.get(i), true).
				entities().
				forEach(x->op.setRange(x.toString().replaceAll("<http:\\S+#", "").replaceAll(">", "")));	
			
			//Only add those, whose cut-name differs from its range
			//otherwise there will be duplicates in the schema
			//Not a clean way to do it!
			if(!op.getPropertyName().replaceFirst("has", "").equals(op.getRange())) {
				opList.add(op);
			}
			
		}
				
		System.out.println(opList.size()+" Object Properties have been built.");
		return opList;
	}
	
	public HashMap<String, OntologyClass> createClassMap() throws OWLOntologyCreationException {
		HashMap<String, OntologyClass> ocMap = new HashMap<String, OntologyClass>();
		
		//Create first class "Thing".
		OWLClass owl_thing = owl_r.getTopClassNode().entities().findFirst().get();
		OntologyClass oc_thing = new OntologyClass(owl_thing, o);
		ocMap.put(owl_thing.toString(), oc_thing);
		
		
		owl_r.getSubClasses(owl_thing).
				entities().
				filter(x->!x.toString().equals("owl:Nothing")).
				forEach(x->ocMap.put(x.toString().replaceAll("<", "").replaceAll(">", ""), new OntologyClass(x, o)));
		
		
		for(Map.Entry<String, OntologyClass> entry : ocMap.entrySet()) {
			
			EntitySearcher.getSuperClasses(entry.getValue().getOwlClass(), entry.getValue().getOntology()).
				filter(x->!x.toString().startsWith("Object")).
				filter(x->!x.toString().startsWith("Data")).
				forEach(x->entry.getValue().addSuperclass(ocMap.get(x.toString().replaceAll("<", "").replaceAll(">", ""))));
			
			
			EntitySearcher.getSubClasses(entry.getValue().getOwlClass(), entry.getValue().getOntology()).
				forEach(x->entry.getValue().addDirSubclass(ocMap.get(x.toString().replaceAll("<", "").replaceAll(">", ""))));
			
			//Add simple Equivalent-Classes (basically synonyms)
			EntitySearcher.getEquivalentClasses(entry.getValue().getOwlClass(), entry.getValue().getOntology()).
				filter(x->!x.toString().startsWith("ObjectIntersectionOf")).
				filter(x->!entry.getValue().getOwlClass().toString().equals(x.toString())).
				forEach(x->entry.getValue().addEqClass(ocMap.get(x.toString().replaceAll("<", "").replaceAll(">", ""))));
			
			
			aList = o.axioms(entry.getValue().getOwlClass()).collect(Collectors.toList());
			
			for(int j = 0; j < aList.size(); j++) {
				addClassDefinition(entry.getValue(), aList.get(j));			
			}
			
			//Copy restrictions of simple equivalence class.
			for(int i = 0; i<entry.getValue().getEqClassAmount(); i++) {
				entry.getValue().copyObjectRestrictions(entry.getValue().getEqClasses(i));
				System.out.println(entry.getValue().getName()+" kopiert die Restrictions von "+entry.getValue().getEqClasses(i).getName());
			}
			
		}
		
		
		ocMap.get("owl:Thing").bequeathRestrictions();
		


		System.out.println(+ocMap.size()+" Classes haven been built.");
		return ocMap;
	}
	
	
	
	//Method is not complete and used
	public OntologyClass getClassByLabel(){//In Progress
		return null;
		
//		for(OWLClass c: o.getClassesInSignature()) {
//				for(OWLAnnotationAssertionAxiom a : o.getAnnotationAssertionAxioms(c.getIRI())) {
//					if(a.getProperty().isLabel()) {
//							if(a.getValue() instanceof OWLLiteral) {
//								OWLLiteral val = (OWLLiteral) a.getValue();
//								//oc.setName(val.getLiteral());
//								System.out.println(val);
//						}
//					
//		        }
//		    }
//		}
		
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		System.out.println("I'm the best class there is.");
	}

}