package com.princetonsa.sort.odontologia;

import java.util.Comparator;
import com.princetonsa.dto.odontologia.DtoPrograma;

public class SortProgramasOdontologicos implements Comparator<DtoPrograma> {
	
	private String patronOrdenar; 
	
	public int compare(DtoPrograma dtoUno, DtoPrograma dtoDos) 
	{
		if(this.patronOrdenar.equals("codigoPrograma")){
			return new String(dtoUno.getCodigoPrograma()).compareTo(new String(dtoDos.getCodigoPrograma()));
		}
		if(this.patronOrdenar.equals("nombrePrograma")){
			return new String(dtoUno.getNombre()).compareTo(new String(dtoDos.getNombre()));
		}
		if(this.patronOrdenar.equals("activo")){
			return new String(dtoUno.getActivo()).compareTo(new String(dtoDos.getActivo()));
		}
		if(this.patronOrdenar.equals("especialidad")){
			return new Integer(dtoUno.getEspecialidad()).compareTo(new Integer(dtoDos.getEspecialidad()));
		}
		if(this.patronOrdenar.equals("codigoPrograma_descendente")){
			return new String(dtoDos.getCodigoPrograma()).compareTo(new String(dtoUno.getCodigoPrograma()));
		}
		if(this.patronOrdenar.equals("nombrePrograma_descendente")){
			return new String(dtoDos.getNombre()).compareTo(new String(dtoUno.getNombre()));
		}
		if(this.patronOrdenar.equals("activo_descendente")){
			return new String(dtoDos.getActivo()).compareTo(new String(dtoUno.getActivo()));
		}
		if(this.patronOrdenar.equals("especialidad_descendente")){
			return new Integer(dtoDos.getEspecialidad()).compareTo(new Integer(dtoUno.getEspecialidad()));
		}
		else 
		{
			return new Double(dtoUno.getCodigo()).compareTo(new Double(dtoDos.getCodigo()));
		}
	}

	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}
	
	
}