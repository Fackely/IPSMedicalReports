package com.princetonsa.sort.Interfaz;

import java.util.Comparator;

import com.princetonsa.dto.interfaz.DtoTiposInterfazDocumentosParam1E;

public class SortTiposInterfazDocumentosParam1E implements Comparator<DtoTiposInterfazDocumentosParam1E> {

	private String patronOrdenar; 
	
	@Override
	public int compare(DtoTiposInterfazDocumentosParam1E dtoUno, DtoTiposInterfazDocumentosParam1E dtoDos) {
		
		
		if(this.patronOrdenar.equals("tipoDoc")){
			return new String(dtoUno.getNombreDocumento()).compareTo(new String(dtoDos.getNombreDocumento()));
		}else if(this.patronOrdenar.equals("indTipoDoc")){
			return new String(dtoUno.getIndTipoDocumento()).compareTo(new String(dtoDos.getIndTipoDocumento()));
		}else if(this.patronOrdenar.equals("consecutReportar")){
			return new String(dtoUno.getNombreConsecutivo()).compareTo(new String(dtoDos.getNombreConsecutivo()));
		}else if(this.patronOrdenar.equals("documCruce")){
			return new String(dtoUno.getTipoDocumentoCruce()).compareTo(new String(dtoDos.getTipoDocumentoCruce()));
		}else if(this.patronOrdenar.equals("unidFuncEstandar")){
			return new String(dtoUno.getNombreUnidadFuncional()).compareTo(new String(dtoDos.getNombreUnidadFuncional()));
		}else if(this.patronOrdenar.equals("obsEncabezado")){
			return new String(dtoUno.getObservacionesEncabezado()).compareTo(new String(dtoDos.getObservacionesEncabezado()));
		}
		else if(this.patronOrdenar.equals("tipoDoc_descendente")){
			return new String(dtoDos.getNombreDocumento()).compareTo(new String(dtoUno.getNombreDocumento()));
		}else if(this.patronOrdenar.equals("indTipoDoc_descendente")){
			return new String(dtoDos.getIndTipoDocumento()).compareTo(new String(dtoUno.getIndTipoDocumento()));
		}else if(this.patronOrdenar.equals("consecutReportar_descendente")){
			return new String(dtoDos.getNombreConsecutivo()).compareTo(new String(dtoUno.getNombreConsecutivo()));
		}else if(this.patronOrdenar.equals("documCruce_descendente")){
			return new String(dtoDos.getTipoDocumentoCruce()).compareTo(new String(dtoUno.getTipoDocumentoCruce()));
		}else if(this.patronOrdenar.equals("unidFuncEstandar_descendente")){
			return new String(dtoDos.getNombreUnidadFuncional()).compareTo(new String(dtoUno.getNombreUnidadFuncional()));
		}else if(this.patronOrdenar.equals("obsEncabezado_descendente")){
			return new String(dtoDos.getObservacionesEncabezado()).compareTo(new String(dtoUno.getObservacionesEncabezado()));
		}	
		else
		{
			return new Integer(dtoUno.getConsecutivoPk()).compareTo(new Integer(dtoDos.getConsecutivoPk()));
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
