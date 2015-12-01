package com.princetonsa.sort.administracion;

import java.util.Comparator;

import com.princetonsa.dto.administracion.DtoDetTiposRetencionClaseInv;
import com.princetonsa.dto.administracion.DtoDetTiposRetencionConceptoFV;

public class SortTiposRetencionConceptoFraVaria implements Comparator<DtoDetTiposRetencionConceptoFV>{
	
	/**
	 * Patron de Ordenamiento
	 */
	private String patronOrdenar;
	
	@Override
	public int compare(DtoDetTiposRetencionConceptoFV one,DtoDetTiposRetencionConceptoFV two) {
		
		if(this.patronOrdenar.equals("descripcion")){
			return new String(one.getDescripcionConcFV()).compareTo(new String(two.getDescripcionConcFV()));
		} 
		
		else if(this.patronOrdenar.equals("descripcion_descendente")){
			return new String(two.getDescripcionConcFV()).compareTo(new String(one.getDescripcionConcFV()));
		}
		
		else
		{
			return new String(one.getDescripcionConcFV()).compareTo(new String(two.getDescripcionConcFV()));
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
