package Main;

import Generators.*;
import Main.InputParser;
import TypeDefinitions.GarglModule;
import Utilities.JCommanderParser;

import com.beust.jcommander.JCommander;

public class Gargl {
	public static void main(String[] args) {

		// Parse command line arguments

		JCommanderParser jct = new JCommanderParser();
		JCommander jcmdr = new JCommander(jct, args);
		
		if(args == null || args.length == 0 || jct.help){
			jcmdr .usage();
			System.exit(0);
		}

		if (jct.inputFilename == null) {
			System.out.println("ERROR: Need to specify input filename with -i");
			System.exit(0);
		}
		
		if(jct.language == null){
			System.out.println("ERROR: Need to specify output language with -l");
			System.exit(0);
		}
		
		if(jct.outputDirectory == null){
			// default to current working directory
			jct.outputDirectory = "";
		}
		else if(jct.outputDirectory.charAt(jct.outputDirectory.length() - 1) != '\\') {
			jct.outputDirectory = jct.outputDirectory + "\\";
		}

		// Read in file and convert to Module containing function name and Requests
		InputParser parser = new InputParser(jct.inputFilename);
		GarglModule mod = parser.parseAndConvert();

		System.out.println("LOG: Parsed requests " + jct.inputFilename);

		// Create the necessary generator based on language selected and initialize it with the Module created from file
		Generator generator = null;
		if(jct.language.equalsIgnoreCase("java")) generator = new JavaClassGenerator(mod);
		else if (jct.language.equalsIgnoreCase("javascript-win8")) generator = new Windows8JavascriptModuleGenerator(mod); 
		else System.out.println("ERROR: Language '" + jct.language + "' has no associated generator.");
			
		generator.generateClass(jct.outputDirectory);
	}
}
