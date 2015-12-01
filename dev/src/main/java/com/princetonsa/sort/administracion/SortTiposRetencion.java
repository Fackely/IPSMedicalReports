package com.princetonsa.sort.administracion;

import java.util.Comparator;

import com.princetonsa.dto.administracion.DtoTiposRetencion;

public class SortTiposRetencion implements Comparator<DtoTiposRetencion> {
	
	/**
	 * Patron de Ordenamiento
	 */
	private String patronOrdenar;

	@Override
	public int compare(DtoTiposRetencion one, DtoTiposRetencion two) {
		
		if(this.patronOrdenar.equals("codigo")){
			return new String(one.getCodigo()).compareTo(new String(two.getCodigo()));
		}else if(this.patronOrdenar.equals("descripcion")){
			return new String(one.getDescripcion()).compareTo(new String(two.getDescripcion()));
		}else if(this.patronOrdenar.equals("sigla")){
			return new String(one.getSigla()).compareTo(new String(two.getSigla()));
		}else if(this.patronOrdenar.equals("codigointerfaz")){
			return new String(one.getCodigoInterfaz()).compareTo(new String(two.getCodigoInterfaz()));
		} 
		
		else if(this.patronOrdenar.equals("codigo_descendente")){
			return new String(two.getCodigo()).compareTo(new String(one.getCodigo()));
		}else if(this.patronOrdenar.equals("descripcion_descendente")){
			return new String(two.getDescripcion()).compareTo(new String(one.getDescripcion()));
		}else if(this.patronOrdenar.equals("sigla_descendente")){
			return new String(two.getSigla()).compareTo(new String(one.getSigla()));
		}else if(this.patronOrdenar.equals("codigointerfaz_descendente")){
			return new String(two.getCodigoInterfaz()).compareTo(new String(one.getCodigoInterfaz()));
		}
		
		else
		{
			return new String(one.getCodigo()).compareTo(new String(two.getCodigo()));
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
