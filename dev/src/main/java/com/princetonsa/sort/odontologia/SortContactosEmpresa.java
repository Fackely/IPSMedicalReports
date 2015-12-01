package com.princetonsa.sort.odontologia;

import java.util.Comparator;

import com.princetonsa.dto.facturacion.DtoContactoEmpresa;
import com.princetonsa.dto.odontologia.DtoPrograma;

public class SortContactosEmpresa implements Comparator<DtoContactoEmpresa> 
{	
	private String patronOrdenar;
	
	public int compare(DtoContactoEmpresa dtoUno, DtoContactoEmpresa dtoDos) 
	{
		if(this.patronOrdenar.equals("nombre"))
		{
			return new String(dtoUno.getNombre()).compareTo(new String(dtoDos.getNombre()));
		}
		if(this.patronOrdenar.equals("direccion")){
			return new String(dtoUno.getDireccion()).compareTo(new String(dtoDos.getDireccion()));
		}
		if(this.patronOrdenar.equals("telefono")){
			return new String(dtoUno.getTelefono()).compareTo(new String(dtoDos.getTelefono()));
		}
		if(this.patronOrdenar.equals("email")){
			return new String(dtoUno.getEmail()).compareTo(new String(dtoDos.getEmail()));
		}
		if(this.patronOrdenar.equals("cargo")){
			return new String(dtoUno.getCargo()).compareTo(new String(dtoDos.getCargo()));
		}
		if(this.patronOrdenar.equals("nombre_descendente"))
		{
			return new String(dtoDos.getNombre()).compareTo(new String(dtoUno.getNombre()));
		}
		if(this.patronOrdenar.equals("direccion_descendente")){
			return new String(dtoDos.getDireccion()).compareTo(new String(dtoUno.getDireccion()));
		}
		if(this.patronOrdenar.equals("telefono_descendente")){
			return new String(dtoDos.getTelefono()).compareTo(new String(dtoUno.getTelefono()));
		}
		if(this.patronOrdenar.equals("email_descendente")){
			return new String(dtoDos.getEmail()).compareTo(new String(dtoUno.getEmail()));
		}
		if(this.patronOrdenar.equals("cargo_descendente")){
			return new String(dtoDos.getCargo()).compareTo(new String(dtoUno.getCargo()));
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