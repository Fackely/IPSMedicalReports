package com.princetonsa.sort.odontologia;

import java.util.Comparator;
import util.odontologia.InfoDetallePlanTramiento;
import util.odontologia.InfoHallazgoSuperficie;

/**
 * @author Víctor Hugo Gómez L.
 */

public class SortPiezasDentales implements Comparator<InfoDetallePlanTramiento> 
{
	/**
	 * 
	 */
	private String patronOrdenar; 
	
	
	@Override
	public int compare(InfoDetallePlanTramiento one, InfoDetallePlanTramiento two) 
	{
		if(this.patronOrdenar.equals("pieza")){
			return new Integer(one.getPieza().getCodigo()).compareTo(new Integer(two.getPieza().getCodigo()));
		}
		
		if(this.patronOrdenar.equals("pieza_descendente")){
			return new Integer(two.getPieza().getCodigo()).compareTo(new Integer(one.getPieza().getCodigo()));
		}
		
		else{
			return new Integer(one.getPieza().getCodigo()).compareTo(new Integer(two.getPieza().getCodigo()));
		}
	}


	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}


	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}
}
