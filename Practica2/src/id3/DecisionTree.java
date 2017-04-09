package id3;

import javax.swing.JTextArea;


/* This file is copyright (c) 2008-2012 Philippe Fournier-Viger
* 
* This file is part of the SPMF DATA MINING SOFTWARE
* (http://www.philippe-fournier-viger.com/spmf).
* 
* SPMF is free software: you can redistribute it and/or modify it under the
* terms of the GNU General Public License as published by the Free Software
* Foundation, either version 3 of the License, or (at your option) any later
* version.
* 
* SPMF is distributed in the hope that it will be useful, but WITHOUT ANY
* WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
* A PARTICULAR PURPOSE. See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with
* SPMF. If not, see <http://www.gnu.org/licenses/>.
*/

/**
* This class represents a decision tree created by the ID3 algorithm.
 *
 * @see AlgoID3
 * @see Node
 * @see DecisionNode
 * @see ClassNode
 * @author jorge 
 */
public class DecisionTree {
	// the list of attributes that was used to create the decision tree
	String []allAttributes;
	
	// a reference to the root node of the tree
	Node root = null;

	/**
	 * Print the tree to System.out.
	 * @param textArea 
	 */
	public void print() {
		System.out.println("DECISION TREE"+"\n");
		System.out.println(" "+"\n");
		String indent = "   ";
		print(root, indent, "");
	}
        public void printArea(JTextArea area){
          	String indent = " ";
                printArea(root, indent, indent, area);
        }
	/**
	 * Print a sub-tree to System.out
	 * @param nodeToPrint the root note
	 * @param indent the current indentation
	 * @param value  a string that should be used to increase the indentation
	 * @param textArea 
	 */
	private void print(Node nodeToPrint, String indent, String value) {
		String newIndent = indent + "      ";
		// if it is a class node
		if(nodeToPrint instanceof ClassNode){
			// cast to a class node and print it
			ClassNode node = (ClassNode) nodeToPrint;
			System.out.println(  "="+ node.className);
			System.out.println(" "+"\n");
		}else{
			// if it is a decision node, cast it to a decision node
			// and print it.
			DecisionNode node = (DecisionNode) nodeToPrint;
			
			/*if ( node.attribute !=0){
			textArea.append(node.attributeValues[node.attribute]);}*/
			System.out.println(indent + allAttributes[node.attribute] + "->"+"\n");
			System.out.println(" "+"\n");
			// then recursively call the method for subtrees
			for(int i=0; i< node.nodes.length; i++){
				System.out.println(indent+indent +node.attributeValues[i]);
				print(node.nodes[i], newIndent, node.attributeValues[i]);
			}
		}
		
	}
        
        private void printArea(Node nodeToPrint, String indent, String value, JTextArea textArea) {
		String newIndent = indent + "      ";
                String prefijo = "\t ║";
		// if it is a class node
		if(nodeToPrint instanceof ClassNode){
			// cast to a class node and print it
			ClassNode node = (ClassNode) nodeToPrint;
			textArea.append("   ¿Se sale? "+ (node.className.equals("si") ? "✔" : "✖") +"\n");
			//textArea.append(" "+"\n");
		}else{
			// if it is a decision node, cast it to a decision node
			// and print it.
			DecisionNode node = (DecisionNode) nodeToPrint;
			
			if ( node.attribute ==0){
                            textArea.append("\n          ╔════════╗\n");
                            textArea.append("          ║  " + allAttributes[node.attribute] +"  ║\n");
                            textArea.append("          ╚════╦═══╝\n");
                        }else
                            //estan son las segundas
                            textArea.append(prefijo + indent + "╚═ " + allAttributes[node.attribute] +"\n");
			//textArea.append(" "+"\n");
                        
			// then recursively call the method for subtrees
			for(int i=0; i< node.nodes.length; i++){
                            
                                if(node.nodes.length == 2){
                                    if(node.attributeValues.length - 1 != i)
                                        textArea.append(prefijo + newIndent +"╠═" +node.attributeValues[i]);
                                    else
                                        textArea.append(prefijo + newIndent + "╚═" +node.attributeValues[i]);
                                }else{
                                    if(i==2){
                                        textArea.append(indent +"\t ╚═" +node.attributeValues[i]);
                                    }
                                    else
                                        textArea.append(indent +"\t ╠═" +node.attributeValues[i] + "\n");
                                }
				
				printArea(node.nodes[i], newIndent, node.attributeValues[i],textArea);
			}
		}
		
	}
	/**
	 * This method predict the class of an instance.
	 * @param newInstance  an instance for which to perform the prediction.
	 * @return Return the class name or null if the tree cannot predict the class, for example because
	 *  some value does not appear in the tree.
	 */
	public String predictTargetAttributeValue(String[] newInstance) {
		return predict(root, newInstance);
	}
        public String predict2(String[] newInstance) {
            String prediction = null;
		if(newInstance[0].equals("soleado")){
                    if(newInstance[3].equals("normal"))
                        prediction = "Afirmativo";
                    else if(newInstance[3].equals("alta"))
                        prediction = "Negativo";
                }else if(newInstance[0].equals("lluvioso")){
                    if(newInstance[2].equals("verdad"))
                        prediction = "Negativo";
                    else if (newInstance[2].equals("falso"))
                        prediction = "Afirmativo";
                }else if(newInstance[0].equals("nublado")){
                    prediction = "Afirmativo";
                }
                return prediction;
	}

	/**
	 * Helper method to perform a prediction.
	 * @param currentNode the current node from the decision tree that is considered
	 * @param newInstance an instance for which to perform the prediction.
	 * @return Return the class name or null if the tree cannot predict the class, for example because
	 *  some value does not appear in the tree.
	 */
	private String predict(Node currentNode, String[] newInstance) {
		// If this node is a class node, then return the class name
		if(currentNode instanceof ClassNode){
			ClassNode node = (ClassNode) currentNode;
			return node.className;
		}else{
			// otherwise, check which subtree we should follow
			// by comparing the attribute of the instance
			// with the one in the tree
			DecisionNode node = (DecisionNode) currentNode;
			String value = newInstance[node.attribute];
			for(int i=0; i< node.attributeValues.length; i++){
				if(node.attributeValues[i].equals(value)){
					return predict(node.nodes[i], newInstance);
				}
			}
		}
		return null; // null if no subtree correspond to the attribute value for the instance
	}
	
	

}
