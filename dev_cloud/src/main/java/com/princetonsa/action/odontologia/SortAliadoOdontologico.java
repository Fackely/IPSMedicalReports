package com.princetonsa.action.odontologia;

import java.util.Comparator;

import com.princetonsa.dto.odontologia.DtoAliadoOdontologico;


/**
 * 
 * @author Juan Diego Garcia
 *
 */

public class SortAliadoOdontologico  implements Comparator<DtoAliadoOdontologico> {
	
	private String patronOrdenar;
	
	/**
	 * @param patronOrden
	 */
	
	public SortAliadoOdontologico (String patronOrdenar)
	{
		this.patronOrdenar=patronOrdenar;
	}
	
	/**
	 * 
	 */
	
	public int compare(DtoAliadoOdontologico one, DtoAliadoOdontologico two){
		
		if(this.getPatronOrdenar().equals("codigo"))
		{	
			return  (one.getCodigo().toLowerCase()).compareTo(two.getCodigo().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("descripcion"))
		{
			return  (one.getDescripcion().toLowerCase()).compareTo(two.getDescripcion().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("direccion"))
		{
			return  (one.getDireccion().toLowerCase()).compareTo(two.getDireccion().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("telefono"))
		{
			return  (one.getTelefono().toLowerCase()).compareTo(two.getTelefono().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("observaciones"))
		{
			return  (one.getObservaciones().toLowerCase()).compareTo(two.getObservaciones().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("estado"))
		{
			return  (one.getEstado().toLowerCase()).compareTo(two.getEstado().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("codigo_descendente"))
		{
			return  (two.getCodigo().toLowerCase()).compareTo(one.getCodigo().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("descripcion_descendente"))
		{
			return  (two.getDescripcion().toLowerCase()).compareTo(one.getDescripcion().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("direccion_descendente"))
		{
			return  (two.getDireccion().toLowerCase()).compareTo(one.getDireccion().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("telefono_descendente"))
		{
			return  (two.getTelefono().toLowerCase()).compareTo(one.getTelefono().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("observaciones_descendente"))
		{
			return  (two.getObservaciones().toLowerCase()).compareTo(one.getObservaciones().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("estado_descendente"))
		{
			return  (two.getEstado().toLowerCase()).compareTo(one.getEstado().toLowerCase());
		}
		else
		{
			return  (one.getCodigo().toLowerCase()).compareTo(two.getCodigo().toLowerCase());
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
