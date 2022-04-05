package de.fraunhofer.fkie.xsd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.Namespace;


public class XsdCreator {
	//1.Attributes
    //I'm not sure how important those URIs are.
    //They are just copied from the original schema.
    private final static Namespace VERSIONING = Namespace.getNamespace("vc", "http://www.w3.org/2007/XMLSchema-versioning");
    private final static Namespace C2SIM = Namespace.getNamespace("http://www.sisostds.org/schemas/C2SIM/1.1");
	private final static Namespace SCHEMA = Namespace.getNamespace("xs", "http://www.w3.org/2001/XMLSchema");
    private final static Namespace XSD = Namespace.getNamespace("xsd", "http://www.w3.org/2001/XMLSchema#");
    private ArrayList<File> ofiles = new ArrayList<File>();
    private Document doc;
    private File owlOutput;
	private boolean doReasoning;
    private C2SIMAlphabetize alpha = new C2SIMAlphabetize();
    
    //2.Constructors
    public XsdCreator(String path, File owlOutput, boolean doReasoning){
		this.owlOutput = owlOutput;
		this.doReasoning = doReasoning;
		File file = new File(path);
    	ofiles.add(file);
    }
    
    public XsdCreator(List<String> paths, File owlOutput, boolean doReasoning) {
		this.owlOutput = owlOutput;
		this.doReasoning = doReasoning;
		for(String path: paths) {
    		ofiles.add(new File(path));
    	}
    }
    
    
    //3.1. Auxiliary Methods
    //Cuts down strings to schema convention.
    String cutPropertyName(String name) {
    	if(name.startsWith("is") && !name.endsWith("is")) {
    		name = name.replaceFirst("is", "");
    	}
    	
    	if(name.startsWith("has") && !name.endsWith("has")) {
    		name = name.replaceFirst("has", "");
    	}
    	return name;
    }
    
    Element createRootElement(String elementFormDefault, String attributeFormDefault, String minVersion) {
    	Element schemaRoot = new Element("schema", SCHEMA);
        schemaRoot.addNamespaceDeclaration(VERSIONING);
        schemaRoot.addNamespaceDeclaration(C2SIM);
        schemaRoot.addNamespaceDeclaration(XSD);
        schemaRoot.setAttribute("targetNamespace", C2SIM.getURI().toString());
        schemaRoot.setAttribute("elementFormDefault", elementFormDefault);
        schemaRoot.setAttribute("attributeFormDefault", attributeFormDefault);
        schemaRoot.setAttribute("minVersion", minVersion, VERSIONING);
        return schemaRoot;
    }
        
    void addDocumentation(OntologyEntity oe, Element element) {
    	Element anno = new Element("annotation", SCHEMA);
    	
    	if(oe.getUri() != null) {
    		Element doc = new Element("documentation", SCHEMA);
    		doc.setText(oe.getUri());
    		anno.addContent(doc);
    	}
    	
    	if(oe.getDocumentation() != null) {
    		Element doc = new Element("documentation", SCHEMA);
    		doc.setText(oe.getDocumentation());
    		anno.addContent(doc);
    	}
    	
    	element.addContent(anno);
    }
    
    void addDataRestrictionElement(OntologyClass oc, Element sequence) {
        for(int i = 0; i < oc.getDataRestrictionAmount(); i++) {
            
         	Element restr = new Element("element", SCHEMA);
   
         	restr.setAttribute("ref", cutPropertyName(oc.getDataRestriction(i).getRestrictionName()));
         	
         	if (oc.getDataRestriction(i).getRestrictionType().equals("DataMaxCardinality")) { 
         		restr.setAttribute("minOccurs", "0");
         		restr.setAttribute("maxOccurs", oc.getDataRestriction(i).getValue());         	
         	}
         	
         	if (oc.getDataRestriction(i).getRestrictionType().equals("DataMinCardinality")) { 
         		restr.setAttribute("minOccurs", oc.getDataRestriction(i).getValue());
         		restr.setAttribute("maxOccurs", "unbounded");
         	}
         	
         	if (oc.getDataRestriction(i).getRestrictionType().equals("DataExactCardinality")) {		   
         		restr.setAttribute("minOccurs", oc.getDataRestriction(i).getValue());
         		restr.setAttribute("maxOccurs", oc.getDataRestriction(i).getValue());
         	}
         	
         	if (oc.getDataRestriction(i).getRestrictionType().equals("DataSomeValuesFrom")) {		   
         		restr.setAttribute("minOccurs", "1");
         		restr.setAttribute("maxOccurs", "unbounded");
         	}
        	
         	sequence.addContent(restr);
        }
    }
    
    void addObjectRestrictionElement(OntologyClass oc, Element sequence) {
    	 for(int i = 0; i < oc.getObjectRestrictionAmount(); i++) {
             
          	Element restr = new Element("element", SCHEMA);
         
          	restr.setAttribute("ref", oc.getObjectRestriction(i).getObject().toString());
          	
          	if (oc.getObjectRestriction(i).getRestrictionType().equals("ObjectMaxCardinality")) {
          		restr.setAttribute("minOccurs", "0");
          		restr.setAttribute("maxOccurs", oc.getObjectRestriction(i).getValue());
          		
          	}
          	
          	if (oc.getObjectRestriction(i).getRestrictionType().equals("ObjectMinCardinality")) { 
          		restr.setAttribute("minOccurs", oc.getObjectRestriction(i).getValue());
          		restr.setAttribute("maxOccurs", "unbounded");
          	}           	
          	
          	if(oc.getObjectRestriction(i).getRestrictionType().equals("ObjectExactCardinality")) {
          		restr.setAttribute("minOccurs", oc.getObjectRestriction(i).getValue());
          		restr.setAttribute("maxOccurs", oc.getObjectRestriction(i).getValue());
          	}
          	
          	if(oc.getObjectRestriction(i).getRestrictionType().equals("ObjectSomeValuesFrom")) {
          		restr.setAttribute("minOccurs", "1");
          		restr.setAttribute("maxOccurs", "unbounded");
          	}
          	         	
          	sequence.addContent(restr);	
          
          }
    }
    
    void addChoiceElement(OntologyClass oc, Element sequence) {
    	Element choice = new Element("choice", SCHEMA);
    	
    	for(int i = 0; i < oc.getSubclassAmount(); i++) {
    		Element element = new Element("element", SCHEMA);
    		element.setAttribute("ref", oc.getSubclass(i).getName());
    		choice.addContent(element);
    	}
    	
    	sequence.addContent(choice);
    }
    
    //Create schema elements from data types.
    void createDataTypeNode(DataType dt, Element schemaRoot) {
    	Element simpType = new Element("simpleType", SCHEMA);
    	simpType.setAttribute("name", dt.getName()+"Type");
    	
    	addDocumentation(dt, simpType);
    	
    	
    	Element rest = new Element("restriction", SCHEMA);
    	
    	//Element Attach range as xs:Restriction to the element. 	
    	for (int i = 0; i < dt.getTypedefinitionAmount(); i++) { 
    		
    		
    		//To-Do: Perhaps this can be done more compact?
    		if(dt.getTypedefinition(i).getFacetType() != null) {
    			
    		 
    			if(dt.getTypedefinition(i).getFacetValue().endsWith("xsd:double")) {
    				if(dt.getTypedefinition(i).getFacetType().equals("maxInclusive")){
    					rest.setAttribute("base", "xs:double");
    					Element maxInc = new Element("maxInclusive", SCHEMA);
    					maxInc.setAttribute("value", dt.getTypedefinition(i).getFacetValue().replace("^^xsd:double", ""));
    					rest.addContent(maxInc);   				
    				}
    		
    				if(dt.getTypedefinition(i).getFacetType().equals("minInclusive")){    				
    					rest.setAttribute("base", "xs:double");
    					Element minInc = new Element("minInclusive", SCHEMA);
    					minInc.setAttribute("value", dt.getTypedefinition(i).getFacetValue().replace("^^xsd:double", ""));
    					rest.addContent(minInc);   				
    				}
    			}
    			
    			if(dt.getTypedefinition(i).getFacetValue().endsWith("xsd:nonNegativeInteger")) {
    				if(dt.getTypedefinition(i).getFacetType().equals("maxInclusive")){
    					rest.setAttribute("base", "xs:nonNegativeInteger");
    					Element maxInc = new Element("maxInclusive", SCHEMA);
    					maxInc.setAttribute("value", dt.getTypedefinition(i).getFacetValue().replace("^^xsd:nonNegativeInteger", ""));
    					rest.addContent(maxInc);   				
    				}
    		
    				if(dt.getTypedefinition(i).getFacetType().equals("minInclusive")){    				
    					rest.setAttribute("base", "xs:nonNegativeInteger");
    					Element minInc = new Element("minInclusive", SCHEMA);
    					minInc.setAttribute("value", dt.getTypedefinition(i).getFacetValue().replace("^^xsd:double", ""));
    					rest.addContent(minInc);   				
    				}
    			}
    				
    			if(dt.getTypedefinition(i).getFacetType().equals("pattern")){
        				rest.setAttribute("base", "xs:string");
        				Element minInc = new Element("pattern", SCHEMA);
        				minInc.setAttribute("value", dt.getTypedefinition(i).getFacetValue().replace("^^xsd:string", ""));
        				rest.addContent(minInc);
    				}
    			}
    		
    		else {
    			System.out.println(dt.getName() + " has no definition.");
    		}
    		
    	}	
    	
    	//The restriction will only be attached, when it has content.
    	if (rest.getChildren().size() != 0) {
    		simpType.addContent(rest);
    		schemaRoot.addContent(simpType);
    		
            Element dtElement = new Element("element", SCHEMA);
            dtElement.setAttribute("name", dt.getName());
            dtElement.setAttribute("type", dt.getName()+"Type");
            schemaRoot.addContent(dtElement);
    		}
    	

    	else {
    		System.out.println(dt.getName() + " has no definition.");
    	}

    }
    
    //Create a schema type from the Data- or Object Property, which will be attached to root,
    //and an element, which will be directly attached to root.
    void createDataPropertyNode(DataProperty dp, Element schemaRoot) {
    	Element simpType = new Element("simpleType", SCHEMA);
    	simpType.setAttribute("name", cutPropertyName(dp.getName())+"Type");
    	
    	addDocumentation(dp, simpType);
    	
    	//Attache range as xs:Restriction to the property element
    	Element rest = new Element("restriction", SCHEMA);    	
    	if(dp.getRange()!= null) {   	
    		if(dp.getRange().startsWith("xs:")) {
    			rest.setAttribute("base", dp.getRange());
    		}
    		else {
    			rest.setAttribute("base", dp.getRange()+"Type");
    		}
    		
    	}   
    	
    	//Create type and restrictions only, if they exist.
    	if(rest.getAttributes().size() != 0) {
    		simpType.addContent(rest);
        	schemaRoot.addContent(simpType);
      
        	
            //Create the corresponding element.
            Element propElement = new Element("element", SCHEMA);
            propElement.setAttribute("name", cutPropertyName(dp.getName()));
            propElement.setAttribute("type", cutPropertyName(dp.getName())+"Type");
            schemaRoot.addContent(propElement);
    	}
    	
    	else {
    		System.out.println(dp.getName()+ " is not defined!");
    	}

    }
    
    //The Object Properties are transferred to xs:elements and nothing more.
    void createObjectPropertyNode(ObjectProperty op, Element schemaRoot) {
    	Element element = new Element("element", SCHEMA);
    	element.setAttribute("name", cutPropertyName(op.getPropertyName()));
    	element.setAttribute("type", op.getRange()+"Type");
    	schemaRoot.addContent(element);
    }
    
    //Create a schema type from the class, which is attached to root,
    //and an element, which is directly attached.
    void createClassNode(OntologyClass oc, Element schemaRoot) {
    	Element compClass = new Element("complexType", SCHEMA);
    	compClass.setAttribute("name", oc.getName()+"Type");
    	schemaRoot.addContent(compClass);
    	
    	addDocumentation(oc, compClass);	
    	
        
        
        if(oc.getSubclassAmount() > 0) {
        	addChoiceElement(oc, compClass);
        }
        
        if(oc.getSubclassAmount() == 0) {
        	Element sequence = new Element("sequence", SCHEMA);
            addDataRestrictionElement(oc, sequence); 
            addObjectRestrictionElement(oc, sequence); 
            compClass.addContent(sequence);
        }
        
        //Create the corresponding element.
        Element classElement = new Element("element", SCHEMA);
        classElement.setAttribute("name", oc.getName());
        classElement.setAttribute("type", oc.getName()+"Type");
        schemaRoot.addContent(classElement);
    }
    
    void createSimpleClassNode(OntologyClass oc, Element schemaRoot) {
    	Element simpType = new Element("simpleType", SCHEMA);
    	simpType.setAttribute("name", oc.getName()+"Type");
    	
    	addDocumentation(oc, simpType);
    	
    	Element rest = new Element("restriction", SCHEMA);
    	rest.setAttribute("base", "xs:string");
    	
    	for (int i = 0; i < oc.getInstanceAmount(); i++) {
    		Element enumer = new Element("enumeration", SCHEMA);
    		enumer.setAttribute("value", oc.getInstance(i).getName());
    		rest.addContent(enumer);
    	}
    	
    	simpType.addContent(rest);
    	schemaRoot.addContent(simpType);
    	
    	Element classElement = new Element("element", SCHEMA);
    	classElement.setAttribute("name", oc.getName());
    	classElement.setAttribute("type", oc.getName()+"Type");
    	schemaRoot.addContent(classElement);
    }
    
    public void writeFile(Document doc, String path) throws IOException {
        File file = new File(path);
 		FileOutputStream out = new FileOutputStream(file);
 		
 		XMLOutputter xmlOutputter = new XMLOutputter();
 		xmlOutputter.setFormat(Format.getPrettyFormat());
 	    xmlOutputter.output(doc, out);
 	    System.out.println("File written to " +path);
    }
    
    //3.2. Methods 
    public void createSchema(String outputPath) {
    	try {
    		Document doc = new Document();
    		OwlToJavaTransformer trans = new OwlToJavaTransformer(ofiles, owlOutput, doReasoning);
    		List<DataType> dtList = trans.createDataTypeList();
    		List<DataProperty> dpList = trans.createDataPropertyList();
    		List<ObjectProperty> opList = trans.createObjectPropertyList();
    		HashMap<String, OntologyClass> ocMap = trans.createClassMap();
            
            
            //Create root with the name spaces
            Element schemaRoot = createRootElement("qualified", "unqualified", "1.1");
            doc.setRootElement(schemaRoot);
            
            //Transformation of data types.          
           schemaRoot.addContent(new Comment("*****SIMPLE TYPES DERIVED FROM ONTOLOGY DATA TYPES*****"));
           
           for (int i = 0; i < dtList.size(); i++) {
        	   createDataTypeNode(dtList.get(i), schemaRoot);
           }           
           
            //Transformation of data type properties.
           schemaRoot.addContent(new Comment("*****SIMPLE TYPES DERIVED FROM ONTOLOGY DATA PROPERTIES*****")); 
           for (int i = 0; i < dpList.size(); i++) {
        	   createDataPropertyNode(dpList.get(i), schemaRoot);
           }  
      
           
           //Transformation of classes.
           //Zunächst muss owl:thing gelöscht werden.
           ocMap.remove("owl:Thing");
     
           
           schemaRoot.addContent(new Comment("*****TYPES DERIVED FROM ONTOLOGY CLASSES*****")); 
           for(Map.Entry<String, OntologyClass> entry : ocMap.entrySet()) {
        	   
        	   //TO-DO: Class with instances are created as simple classes. this is not a good logic.
        	   //This problem concerns the ontology also.
        	   if(entry.getValue().getInstanceAmount() > 0) {
        		   createSimpleClassNode(entry.getValue(), schemaRoot);
        	   }
        	   else {
            	createClassNode(entry.getValue(), schemaRoot);
        	   }
            }
           
           //Transformation of object properties; not good, cause some might be created and then deleted.
           schemaRoot.addContent(new Comment("*****ELEMENTS DERIVED FROM ONTOLOGY OBJECT PROPERTIES"
           		+ " THAT ARE NOT ALREADY DERIVED FROM CLASSES*****")); 
           
          
          
           for (int i = 0; i < opList.size(); i++) {
        	   if(opList.get(i).getRange() != null) {
        		   createObjectPropertyNode(opList.get(i), schemaRoot);
        	   }       	  
           }  
           
           
           doc = alpha.alphabetize(doc);
           
           writeFile(doc, outputPath);
            
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		
    	}
    }
    
    //This is not used and not finished.
//    public void createSchemaForSpecificClass(String classname) {
//   	 try {
//            doc = new Document();
//            OwlToJavaTransformer cc = new OwlToJavaTransformer(ofiles);
//            OntologyClass oc = cc.createClass(classname);
//            
//            Element schemaRoot = createRootElement("qualified", "unqualified", "1.1");
//            doc.setRootElement(schemaRoot);       
//
//            createClassNode(oc, schemaRoot);                 
//
//            writeFile(doc, "src\\main\\resources\\Test12.xsd");
//
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }    	
//   }


    public static void main(String[] args) {
       XsdCreator xcreator = new XsdCreator("src\\main\\resources\\ontologies\\C2SIM_v1.0.0.rdf",
					   						new File("src\\main\\resources\\ontologies\\Merge.owl"),
					   						true);
       xcreator.createSchema("src\\main\\resources\\schemas\\AnimalSchelterSchema.xsd");
    }
}