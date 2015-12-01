package com.princetonsa.sort.odontologia;

import java.util.Comparator;

import com.princetonsa.dto.odontologia.DtoDetalleIndicePlaca;

/**
 * @author Víctor Hugo Gómez L.
 */

public class SortDetalleIndicePlaca implements Comparator<DtoDetalleIndicePlaca> 
{
	/**
	 * 
	 */
	private String patronOrdenar; 
	
	
	@Override
	public int compare(DtoDetalleIndicePlaca one, DtoDetalleIndicePlaca two) 
	{
		
		if(this.patronOrdenar.equals("eliminar")){
			return new String(one.getEliminar()).compareTo(new String(two.getEliminar()));
		}else if(this.patronOrdenar.equals("nuevo")){
			return new String(one.getNuevo()).compareTo(new String(two.getNuevo()));
		}else if(this.patronOrdenar.equals("modificar")){
			return new String(one.getModificar()).compareTo(new String(two.getModificar()));
		}
		
		
		if(this.patronOrdenar.equals("eliminar_descendente")){
			return new String(two.getEliminar()).compareTo(new String(one.getEliminar()));
		}else if(this.patronOrdenar.equals("nuevo_descendente")){
			return new String(two.getNuevo()).compareTo(new String(one.getNuevo()));
		}else if(this.patronOrdenar.equals("modificar_descendente")){
			return new String(two.getModificar()).compareTo(new String(one.getModificar()));
		} 
		
		else{
			return new String(one.getEliminar()).compareTo(new String(two.getEliminar()));
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

