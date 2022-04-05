/*----------------------------------------------------------------*
|   Copyright 2020 Networking and Simulation Laboratory           |
|         George Mason University, Fairfax, Virginia              |
|                                                                 |
| Permission to use, copy, modify, and distribute this            |
| software and its documentation for all purposes is hereby       |
| granted without fee, provided that the above copyright notice   |
| and this permission appear in all copies and in supporting      |
| documentation, and that the name of George Mason University     |
| not be used in advertising or publicity pertaining to           |
| distribution of the software without specific, written prior    |
| permission. GMU makes no representations about the suitability  |
| of this software for any purposes.  It is provided "AS IS"      |
| without express or implied warranties.  All risk associated     |
| with use of this software is expressly assumed by the user.     |
*----------------------------------------------------------------*/

/**
 * Post-processor to revise an XML Schema (XSD) file by reordering
 * as necessary such that all complex types using "sequence" and 
 * "choice" have all of their elements sequenced in alphanumeric order,
 * with "group" instances followed by "element" instances. This is
 * required for consistency across versions of the C2SIM ontologies
 * and schema, as specified in the SISO C2SIM Standard. Compatible with
 * version 1.0.0 of the C2SIM Core+SMX+LOX schema.
 */
package c2simalphabetize;

import java.util.TreeMap;
import java.util.Iterator;
import java.io.BufferedWriter;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.FileReader;
import java.io.BufferedReader;

/**
 * input: schema.xsd file
 * output: revised schema.xsd with complex types reordered but not other change
 * command line parameters:
 *  0. input filename in the invoking directory or path-filename
 *  1. input filename in the invoking directory or path-filename
 *  2. optionally, namespace prefix; defaults to xs
 * 
 * We presume the XSD has passed validation and that the following 
 * patterns do not appear in data, XML comments or XSD annotations 
 * (patterns illustrated with xs as prefix):
 *   <xs:sequence>
 *   <xs:choice>
 *   <xs:element ref=
 *   <xs:group ref=
 *   </xs:sequence>
 *   </xs:choice>
 * 
 * @author JMPullen
 */
public class C2SIMAlphabetize {
    
    String inputFilename, outputFilename, prefix = "xs";
    String inputXML;
    String outputXML = "";
    
    // our one and only function
    public void alphabetize(String[] args){
        
        // copy and print the arguments
        System.out.println("Running C2SIMAlphabetize");
        inputFilename = args[0];
        System.out.println("Input file name:" + inputFilename);
        outputFilename = args[1];
        System.out.println("Output file name:" + outputFilename);
        if(args.length > 2){
            prefix = args[2];
            System.out.println("using XML namespace prefix:" + prefix);
        }
        
        // patterns we will need to search for
        String sequenceStarts = "<"+prefix+":sequence>";
        String choiceStarts = "<"+prefix+":choice>";
        String elementStarts = "<"+prefix+":element ref=";
        String groupStarts = "<"+prefix+":group ref=";
        String sequenceEnds = "</"+prefix+":sequence>";
        String choiceEnds = "</"+prefix+":choice>";
        
        // maps where we will store Strings being alphabetized
        TreeMap<String,String> elements = new TreeMap<String,String>();
        TreeMap<String,String> groups = new TreeMap<String,String>();
        Iterator iterator;
        
        // open input and output files;
        // read in the input to one long String
        FileReader inputFile;
        FileWriter outputFile;
        inputXML = "";
        try{
            inputFile=new FileReader(new File(inputFilename));
            int charBuf; 
            while((charBuf=inputFile.read())>0) {
              inputXML += (char)charBuf;
            }
        }
        catch(Exception e) {
            System.err.println(
                "Exception in reading XML file " + inputFilename + ":"+e);
            return;
        }
        
        // loop through the whole input String copying to output string
        for(;;){
        
            // search for instances of keywords sequence, choice
            // until one of these is found in form <pf:sequence
            // or <pf:choice; copy all input to output while searching;
            // take care not to look for the pattern in quoted data
            //
            // in the description here whitespace includes XML 
            // comments <!--...--> and XSD annotations 
            // <xs:annotation>...</xs:annotation>
            // 
            // also copy the sequence or choice text to beginning
            // of line starting pattern "(whitespace)<pf:element ref="
            // or "(whitespace)<pf:group ref=" where pf is the prefix
            // and element statement ends with > that is not quoted;
            int sequenceIndex = inputXML.indexOf(sequenceStarts);
            int choiceIndex = inputXML.indexOf(choiceStarts);
            if(sequenceIndex < 0 && choiceIndex < 0)break;
            int searchIndex = sequenceIndex;
            if(searchIndex < 0)searchIndex = choiceIndex;
            else if(choiceIndex < searchIndex && choiceIndex >= 0)
                searchIndex = choiceIndex;
            
            // searchIndex now points to sequence or choice
            // scan forward to closing ">" outside a quote
            boolean insideQuote = false;
            for(; searchIndex < inputXML.length(); searchIndex++){
                char testChar = inputXML.charAt(searchIndex);
                if(testChar == '"')insideQuote = !insideQuote;
                if(!insideQuote && testChar == '>')break;
            }
            if(insideQuote){
                System.err.println("XML ends inside a quote!");
                break;
            }
            searchIndex++;
            
            // move inputXML up to this point to outputXML
            outputXML += inputXML.substring(0, searchIndex);
            inputXML = inputXML.substring(searchIndex);
    
            // store but do not copy all the group and element statements, 
            // down to end of sequence or choice, in a TreeMap by group
            // or element name, which is the quoted string immediately 
            // following "ref=" and unlike the other input will not contain 
            // quotes; copy groups into a separate TreeMap
            //
            // include the whitespace at start of each element
            for(;;){
                
                // find first group or element
                int groupIndex = inputXML.indexOf(groupStarts);
                int elementIndex = inputXML.indexOf(elementStarts);
                boolean foundElement = false;
                if(groupIndex < 0 && elementIndex < 0)break;
                searchIndex = groupIndex;
                if(searchIndex < 0){
                    searchIndex = elementIndex;
                    foundElement = true;
                }
                else if(elementIndex < searchIndex && elementIndex >= 0){
                    searchIndex = elementIndex;
                    foundElement = true;
                }
                
                // if there is a sequence or choice  before the
                // next group or element, the current sequence or 
                // choice has all been parsed
                sequenceIndex = inputXML.indexOf(sequenceStarts);
                choiceIndex = inputXML.indexOf(choiceStarts);
                if((sequenceIndex < searchIndex && sequenceIndex > 0) || 
                   (choiceIndex < searchIndex && choiceIndex > 0))
                    break;
                
                // find ending ">" outside of quotes in input
                for(; searchIndex < inputXML.length(); ++searchIndex){
                    char testChar = inputXML.charAt(searchIndex);
                    if(testChar == '"')insideQuote = !insideQuote;
                    if(!insideQuote && testChar == '>')break;
                }
                if(insideQuote){
                    System.err.println("XML ends inside a quote!");
                    break;
                }
                searchIndex++;
                
                // find first "ref=" inside the remaining part 
                // of inputXML up to searchIndex and outside quote
                int refIndex = 0;
                for(; refIndex < searchIndex; ++refIndex){
                    char testChar = inputXML.charAt(refIndex);
                    if(testChar == '"')insideQuote = !insideQuote;
                    int refTest = inputXML.indexOf("ref=", refIndex);
                    if(!insideQuote && refTest >= 0 && refTest == refIndex)
                        break;
                }
                if(insideQuote){
                    System.err.println("XML ends inside a quote!");
                    break;
                }

                // to collect the name following "ref="
                // find the next blank after refIndex
                // (also can be "/" at end of element)
                int blankIndex = refIndex;
                for(; blankIndex < searchIndex; ++blankIndex){
                    if(inputXML.charAt(blankIndex) == ' ')break;
                    if(inputXML.charAt(blankIndex) == '/')break;
                }
                if(blankIndex >= searchIndex){
                    System.err.println("malformed XML = incomplete 'ref='");
                    break;
                }
                String name = inputXML.substring(refIndex+4, blankIndex);
               
                // peel off this element or group statement from inputXML
                String insertToMap = inputXML.substring(0, searchIndex);
                inputXML = inputXML.substring(searchIndex);

                // copy statements to element or group Map
                if(foundElement)elements.put(name, insertToMap);
                else groups.put(name, insertToMap);

            }// end for(;;) capturing groups and elements

            // emit to output String contents of first group TreeMap, 
            // then element TreeMap in alphanumeric order using Iterator
            iterator = groups.keySet().iterator();
            while(iterator.hasNext()){
                String key   = (String)iterator.next();
                outputXML += groups.get(key);
            }
            iterator = elements.keySet().iterator();
            while(iterator.hasNext()){
                String key   = (String)iterator.next();
                outputXML += elements.get(key);
            }
            
            // clear out both Maps and go back for another
            // sequence or choice
            elements.clear();
            groups.clear();

        }// end for(;;) scanning entire inputXML String
    
        // copy remaining inputXML to outputXML
        outputXML += inputXML;
        
        // write the output String to output file and close the file
        try{
            outputFile = new FileWriter(outputFilename);
            outputFile.write(outputXML);
            outputFile.close();
        }
        catch(Exception e) {
            System.err.println(
                "Exception in writing XML file " + 
                    outputFilename + ":" + e.getMessage());
            return;
        }
        System.out.println("C2SIM alphabetizing completed");
        
    }// end alphabetize()

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        (new C2SIMAlphabetize()).alphabetize(args);
        
    }// end main()
    
}// end XMLAlphabetize
