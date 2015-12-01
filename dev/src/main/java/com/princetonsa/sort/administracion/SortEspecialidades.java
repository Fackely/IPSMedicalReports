package com.princetonsa.sort.administracion;

import java.util.Comparator;

import com.princetonsa.dto.administracion.DtoEspecialidades;

public class SortEspecialidades implements Comparator<DtoEspecialidades> 
{
	/**
	 * 
	 */
	private String patronOrdenar; 
	
	
	@Override
	public int compare(DtoEspecialidades one, DtoEspecialidades two) 
	{
		if(this.patronOrdenar.equals("codigo")){
			return new String(one.getConsecutivo()).compareTo(new String(two.getConsecutivo()));
		}else if(this.patronOrdenar.equals("codigocentrocostohonorario")){
			return new String(one.getNombreCentroCosto()).compareTo(new String(two.getNombreCentroCosto()));
		}else if(this.patronOrdenar.equals("descripcion")){
			return new String(one.getDescripcion()).compareTo(new String(two.getDescripcion()));
		}else if(this.patronOrdenar.equals("tipoespecialidad")){
			return new String(one.getTipoEspecialidad()).compareTo(new String(two.getTipoEspecialidad()));
		}  
		else if(this.patronOrdenar.equals("codigo_descendente")){
			return new String(two.getConsecutivo()).compareTo(new String(one.getConsecutivo()));
		}else if(this.patronOrdenar.equals("codigocentrocostohonorario_descendente")){
			return new String(two.getNombreCentroCosto()).compareTo(new String(one.getNombreCentroCosto()));
		}else if(this.patronOrdenar.equals("descripcion_descendente")){
			return new String(two.getDescripcion()).compareTo(new String(one.getDescripcion()));
		}else if(this.patronOrdenar.equals("tipoespecialidad_descendente")){
			return new String(two.getTipoEspecialidad()).compareTo(new String(one.getTipoEspecialidad()));
		}
		else
		{
			return new Integer(one.getCodigo()).compareTo(new Integer(two.getCodigo()));
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
