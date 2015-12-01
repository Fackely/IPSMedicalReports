package com.princetonsa.sort.carteraPaciente;

import java.util.Comparator;

import com.princetonsa.dto.administracion.DtoEspecialidades;
import com.princetonsa.dto.carteraPaciente.DtoDeudor;

public class SortDeudores implements Comparator<DtoDeudor>
{
	/**
	 * 
	 */
	private String patronOrdenar; 
	
	
	@Override
	public int compare(DtoDeudor one, DtoDeudor two) 
	{
		if(this.patronOrdenar.equals("tipoidentificacion")){
			return new String(one.getTipoIdentificacionDeu()).compareTo(new String(two.getTipoIdentificacionDeu()));
		}else if(this.patronOrdenar.equals("numidentificacion")){
			return new String(one.getNumeroIdentificacionDeu()).compareTo(new String(two.getNumeroIdentificacionDeu()));
		}else if(this.patronOrdenar.equals("papellido")){
			return new String(one.getPrimerApellidoDeu()).compareTo(new String(two.getPrimerApellidoDeu()));
		}else if(this.patronOrdenar.equals("pnombre")){
			return new String(one.getPrimerNombreDeu()).compareTo(new String(two.getPrimerNombreDeu()));
		} 
		
		else if(this.patronOrdenar.equals("tipoidentificacion_descendente")){
			return new String(two.getTipoIdentificacionDeu()).compareTo(new String(one.getTipoIdentificacionDeu()));
		}else if(this.patronOrdenar.equals("numidentificacion_descendente")){
			return new String(two.getNumeroIdentificacionDeu()).compareTo(new String(one.getNumeroIdentificacionDeu()));
		}else if(this.patronOrdenar.equals("papellido_descendente")){
			return new String(two.getPrimerApellidoDeu()).compareTo(new String(one.getPrimerApellidoDeu()));
		}else if(this.patronOrdenar.equals("pnombre_descendente")){
			return new String(two.getPrimerNombreDeu()).compareTo(new String(one.getPrimerNombreDeu()));
		}
		
		else
		{
			return new Integer(one.getPrimerApellidoDeu()).compareTo(new Integer(two.getPrimerApellidoDeu()));
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
