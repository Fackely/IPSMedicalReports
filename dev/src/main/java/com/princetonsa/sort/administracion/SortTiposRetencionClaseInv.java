package com.princetonsa.sort.administracion;

import java.util.Comparator;

import com.princetonsa.dto.administracion.DtoDetTiposRetencionClaseInv;
import com.princetonsa.dto.administracion.DtoDetTiposRetencionGrupoSer;

public class SortTiposRetencionClaseInv implements Comparator<DtoDetTiposRetencionClaseInv> {

	/**
	 * Patron de Ordenamiento
	 */
	private String patronOrdenar;
	
	@Override
	public int compare(DtoDetTiposRetencionClaseInv one,DtoDetTiposRetencionClaseInv two) {
		
		if(this.patronOrdenar.equals("descripcion")){
			return new String(one.getNombreClaseInv()).compareTo(new String(two.getNombreClaseInv()));
		} 
		
		else if(this.patronOrdenar.equals("descripcion_descendente")){
			return new String(two.getNombreClaseInv()).compareTo(new String(one.getNombreClaseInv()));
		}
		
		else
		{
			return new String(one.getNombreClaseInv()).compareTo(new String(two.getNombreClaseInv()));
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
