package com.princetonsa.sort.odontologia;

import java.util.ArrayList;
import java.util.Comparator;

public class SortListaDetEmisionBonos implements Comparator<ArrayList<ArrayList<String>>>  {

	
	
	private String patronOrdenar;

	/**
	 * 
	 * @param patronOrden
	 */
	
	public SortListaDetEmisionBonos (String patronOrdenar)
	{
		this.setPatronOrdenar(patronOrdenar);
	}
	
	
	


	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}


	public String getPatronOrdenar() {
		return patronOrdenar;
	}


	@Override
	public int compare(ArrayList<ArrayList<String>> o1,	ArrayList<ArrayList<String>> o2) {
		
		
		return 0;
	}

	
	
	
}
