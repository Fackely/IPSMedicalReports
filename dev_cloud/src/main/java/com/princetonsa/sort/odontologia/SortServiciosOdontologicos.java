package com.princetonsa.sort.odontologia;

import java.util.Comparator;

import com.princetonsa.dto.odontologia.DtoServicioOdontologico;

import util.ConstantesIntegridadDominio;


public class SortServiciosOdontologicos implements Comparator<DtoServicioOdontologico> 
{
	/**
	 * 
	 */
	private String patronOrdenar; 
	
	
	@Override
	public int compare(DtoServicioOdontologico one, DtoServicioOdontologico two) 
	{
		if(this.patronOrdenar.equals("pieza")){
			return new String(one.getDespPiezaDental()).compareTo(new String(two.getDespPiezaDental()));
		}else if(this.patronOrdenar.equals("despservicio")){
			return new String(one.getDescripcionServicio()).compareTo(new String(two.getDescripcionServicio()));
		}
		
		if(this.patronOrdenar.equals("pieza_descendente")){
			return new String(two.getDespPiezaDental()).compareTo(new String(one.getDespPiezaDental()));
		}else if(this.patronOrdenar.equals("despservicio_descendente")){
			return new String(two.getDescripcionServicio()).compareTo(new String(one.getDescripcionServicio()));
		}
		
		
		else{
			return new String(one.getDespPiezaDental()).compareTo(new String(two.getDespPiezaDental()));
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