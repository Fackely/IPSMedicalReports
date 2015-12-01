package com.princetonsa.sort.odontologia;

import java.util.Comparator;

import com.princetonsa.dto.odontologia.DtoServicioOdontologico;

import util.odontologia.InfoHallazgoSuperficie;

/**
 * @author Víctor Hugo Gómez L.
 */

public class SortSuperficiesDentales implements Comparator<InfoHallazgoSuperficie>
{
	/**
	 * 
	 */
	private String patronOrdenar; 
	
	
	@Override
	public int compare(InfoHallazgoSuperficie one, InfoHallazgoSuperficie two) 
	{
		if(this.patronOrdenar.equals("superficie")){
			return new Integer(one.getSuperficieOPCIONAL().getCodigo()).compareTo(new Integer(two.getSuperficieOPCIONAL().getCodigo()));
		}else if(this.patronOrdenar.equals("hallazgo")){
			return new Integer(one.getHallazgoREQUERIDO().getCodigo()).compareTo(new Integer(two.getHallazgoREQUERIDO().getCodigo()));
		}
		
		if(this.patronOrdenar.equals("superficie_descendente")){
			return new Integer(two.getSuperficieOPCIONAL().getCodigo()).compareTo(new Integer(one.getSuperficieOPCIONAL().getCodigo()));
		}else if(this.patronOrdenar.equals("hallazgo_descendente")){
			return new Integer(two.getHallazgoREQUERIDO().getCodigo()).compareTo(new Integer(one.getHallazgoREQUERIDO().getCodigo()));
		}
		
		else{
			return new Integer(one.getSuperficieOPCIONAL().getCodigo()).compareTo(new Integer(two.getSuperficieOPCIONAL().getCodigo()));
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
