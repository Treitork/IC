

import id3.AlgoID3;
import id3.DecisionNode;
import id3.DecisionTree;
import Controller.Controlador;


import java.io.IOException;


/**
 * Example of how to use ID3 from the source code.
 * @author Philippe Fournier-Viger (Copyright 2011)
 */
public class MainTestID3 {

	public static void main(String [] arg) throws IOException{
		// Read input file and run algorithm to create a decision tree

		// There is three parameters:
		// - a file path
		// - the "target attribute that should be used to create the decision tree
		// - the separator that was used in the file to separate values (by default it is a space)
                AlgoID3 algo = new AlgoID3();
                Controlador ctrl = new Controlador (algo);
		DecisionTree tree = algo.runAlgorithm();
                FramePrincipal frame = new FramePrincipal(tree);
		frame.setVisible(true);
		//algo.printStatistics();
		
		// print the decision tree:
		//tree.print();
		tree.printArea(frame.getjTextArea1());
		// Use the decision tree to make predictions
		// For example, we want to predict the class of an instance:
		/*String [] instance = {"nublado", "frio", "normal", "verdad",null};
		String prediction = tree.predictTargetAttributeValue(instance);
		System.out.println("The class that is predicted is: " + prediction);*/
	}

	
}
