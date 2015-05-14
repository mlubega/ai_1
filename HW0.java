/*Name: Maimuna Lubega 
 * CS ID: maimuna
 * NetID: lubega
 * Class: CS570 Fall 2014
 * Assignment: HW 0
 * Instructor: Shavlik
 * * */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.*;

public class HW0 {
    public static void main(String[] args) {
	// Make sure an input file was specified on the command line.
	// If this fails and you are using Eclipse, see the comments 
    // in the header above.
    if (args.length != 1) {
        System.err.println("Please supply a filename on the " + "command line: java ScannerSample" + " <filename>");
        System.exit(1);
    }

    //Create Scanner
    Scanner fileScanner = null;
    try{
        fileScanner = new Scanner (new File(args[0]));
    } catch(FileNotFoundException e){
        System.err.println("Could not find file '" + args[0] +"'.");
        System.exit(1);
    }

    //variables
    int numFeatures = 0;
    int numExamples = 0;

    //data storage
    
    //labels are the key (survived, died) and values are num for each feature
    Map<String, HashMap<String, Integer>> dataPerLabel = new HashMap<String, HashMap<String, Integer>>();
    //keys are feature type and value is list of features, ex: Type-passenger, crew 
    Map<String, String []> features = new HashMap<String, String []>();
    //key is label(survived, died) and value is number of that label
    HashMap<String, Integer> occurenceOfLabel = new HashMap<String, Integer>();


    //Iterate through each line
    int lineCount = 1;
    while(fileScanner.hasNext()){
        String line = fileScanner.nextLine().trim();

        //Skip empty lines and comments
        if(line.length() == 0 || (line.length() > 2 && line.substring(0,2).equals("//"))) {
            continue;
	}else{
//	System.out.println(line);
	

        String [] tokens = line.split("\\s+");
        
	
        if(tokens.length == 1){
	       // integer values
		if(isInteger(tokens[0])){
		
        	    if(numFeatures == 0 ){
        	        numFeatures = Integer.parseInt(tokens[0]);
        	    } else if (numExamples == 0){
        	        numExamples = Integer.parseInt(tokens[0]);
        	    } else{
        	        System.err.println("Error parsing integers.");
                	System.exit(1);
            	   }
        	}
		//labels
		else{
			dataPerLabel.put(tokens[0], new HashMap<String, Integer>());
		}
	}
	//Parse features
	else if(tokens.length > 1 && tokens[1].equals("-")){
		features.put(tokens[0], Arrays.copyOfRange(tokens, 2, tokens.length));
	}
	//actual data points
	else{

	
		//count occurence of label
		if(!occurenceOfLabel.containsKey(tokens[1])){
			occurenceOfLabel.put(tokens[1], 1);
		}else{
			occurenceOfLabel.put(tokens[1],occurenceOfLabel.get(tokens[1])+1 );	
		}

		//Count occurence of feature
		HashMap<String, Integer> temp = dataPerLabel.get(tokens[1]);
		for( int i = 2; i < tokens.length; i++){
			if(!temp.containsKey(tokens[i])){
				temp.put(tokens[i], 1);
			}else{
				temp.put(tokens[i], temp.get(tokens[i]) + 1 );
			}

		}		
	
	}





	}
    }
		
    System.out.println("There are "+ numFeatures + " in the dataset.");
    System.out.println("There are "+ numExamples + " examples.");

    	for (String label : occurenceOfLabel.keySet() ){
		System.out.println(occurenceOfLabel.get(label) + " have output label '"+ label + "' .");
	}
    	for (String featureLabel : features.keySet() ){
		System.out.println(" Feature '"+featureLabel+ "':");  //EX: Accomodations
		String [] featuresList = features.get(featureLabel); // EX: [ regular, luxury]
		for( String label: dataPerLabel.keySet()){  //EX: [ survived, died ]
			for( String feat : featuresList ){ //EX: [ regular, luxury ]
				//EX: survived => [ regular => 30, luxury=>20, crew=>15, passenger=>6, etc]
				HashMap<String, Integer> featureCounts = dataPerLabel.get(label);
				if(featureCounts.containsKey(feat)){
					double percent = ((double) featureCounts.get(feat)/ (double) occurenceOfLabel.get(label)) * 100;
					System.out.println("In the examples with the label '" + label+ "', "+ String.format("%.2f", percent)+"% have value '"+feat+"'." );
				}else{
				 	System.out.println("Error with finding feature.");
				}
			}

		}
	}
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s); 
        } catch(NumberFormatException e) { 
            return false; 
        }
        // only got here if we didn't return false
        return true;
    }
 }
