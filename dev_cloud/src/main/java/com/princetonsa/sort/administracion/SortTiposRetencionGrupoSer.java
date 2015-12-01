package com.princetonsa.sort.administracion;

import java.util.Comparator;

import com.princetonsa.dto.administracion.DtoDetTiposRetencionGrupoSer;

public class SortTiposRetencionGrupoSer implements Comparator<DtoDetTiposRetencionGrupoSer> {

	/**
	 * Patron de Ordenamiento
	 */
	private String patronOrdenar;
	
	@Override
	public int compare(DtoDetTiposRetencionGrupoSer one,DtoDetTiposRetencionGrupoSer two) {
		
		if(this.patronOrdenar.equals("descripcion")){
			return new String(one.getDescripcionGrupoSer()).compareTo(new String(two.getDescripcionGrupoSer()));
		} 
		
		else if(this.patronOrdenar.equals("descripcion_descendente")){
			return new String(two.getDescripcionGrupoSer()).compareTo(new String(one.getDescripcionGrupoSer()));
		}
		
		else
		{
			return new String(one.getDescripcionGrupoSer()).compareTo(new String(two.getDescripcionGrupoSer()));
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
