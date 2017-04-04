package Controller;

import java.io.IOException;

import javax.swing.JTextArea;

import id3.AlgoID3;

public class Controlador {
	
	
	private  AlgoID3 algoritmo;
	
	
	
	
	
	public Controlador(AlgoID3 algoritmo){
		
		this.algoritmo = algoritmo;
		
		
	}

	
	
	public void setFileAtributos (String file){
		
		algoritmo.setFileAtributos(file);
		
	}
	
	public void setFileJuego(String file){
		
		algoritmo.setFileJuego(file);
	}
	
	public void setFileTest(String file){
		
		algoritmo.setFileTest(file);
		
	}
	
	
	
	
	public void run(JTextArea textArea){
		
		
		try {
			algoritmo.runAlgorithm();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}



	public void predecir(JTextArea textArea, String[] t) {
		algoritmo.predictTargetAttributeValue(textArea,t);
	}



	public void predecirArchivo(JTextArea textArea) {
		try {
			algoritmo.predictTargetAttributeValue(textArea);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
}
