package com.princetonsa.sort.odontologia;

import java.util.Comparator;

import com.princetonsa.dto.odontologia.DtoEmisionBonosDesc;


public class SortEmisionBonosDesc implements Comparator<DtoEmisionBonosDesc> {
	private String patronOrdenar;

	/**
	 * 
	 * @param patronOrden
	 */
	public SortEmisionBonosDesc (String patronOrdenar)
	{
		this.patronOrdenar=patronOrdenar;
	}
	
	/**
	 * 
	 */
	public int compare(DtoEmisionBonosDesc one, DtoEmisionBonosDesc two) 
	{
		if(this.getPatronOrdenar().equals("id"))
		{	
			return  (one.getId().toLowerCase()).compareTo(two.getId().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("serialInicial"))
		{
			return new Double(one.getSerialInicial().doubleValue()).compareTo(new Double(two.getSerialInicial().doubleValue()));
		}
		else if(this.getPatronOrdenar().equals("serialFinal"))
		{
			return new Double(one.getSerialFinal().doubleValue()).compareTo(new Double(two.getSerialFinal().doubleValue()));
		}
		else if(this.getPatronOrdenar().equals("fechaVigenciaInicial"))
		{
			return  (one.getFechaVigenciaInicial().toLowerCase()).compareTo(two.getFechaVigenciaInicial().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("fechaVigenciaFinal"))
		{
			return  (one.getFechaVigenciaFinal().toLowerCase()).compareTo(two.getFechaVigenciaFinal().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("programa"))
		{
			return  (one.getPrograma().getNombre().toLowerCase()).compareTo(two.getPrograma().getNombre().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("valor"))
		{
			return new Double(one.getValorDescuento()).compareTo(new Double(two.getValorDescuento()));
		}
		else if(this.getPatronOrdenar().equals("porcentaje"))
		{
			return new Double(one.getPorcentajeDescuento()).compareTo(new Double(two.getPorcentajeDescuento()));
		}
		
		else if(this.getPatronOrdenar().equals("servicio"))
		{
			return  (one.getServicio().getNombre().toLowerCase()).compareTo(two.getServicio().getNombre().toLowerCase());
		}
	
		else if(this.getPatronOrdenar().equals("servicio_descendente"))
		{
			return  (two.getServicio().getNombre().toLowerCase()).compareTo(one.getServicio().getNombre().toLowerCase());
		}
		
		if(this.getPatronOrdenar().equals("id_descendente"))
		{	
			return  (two.getId().toLowerCase()).compareTo(one.getId().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("serialInicial_descendente"))
		{
			return new Double(two.getSerialInicial().doubleValue()).compareTo(new Double(one.getSerialInicial().doubleValue()));
		}
		else if(this.getPatronOrdenar().equals("serialFinal_descendente"))
		{
			return new Double(two.getSerialFinal().doubleValue()).compareTo(new Double(one.getSerialFinal().doubleValue()));
			
		}
		else if(this.getPatronOrdenar().equals("fechaVigenciaInicial_descendente"))
		{
			return  (two.getFechaVigenciaInicial().toLowerCase()).compareTo(one.getFechaVigenciaInicial().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("fechaVigenciaFinal_descendente"))
		{
			return  (two.getFechaVigenciaFinal().toLowerCase()).compareTo(one.getFechaVigenciaFinal().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("programa_descendente"))
		{
			return  (two.getPrograma().getNombre().toLowerCase()).compareTo(one.getPrograma().getNombre().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("valor_descendente"))
		{
			return new Double(two.getValorDescuento()).compareTo(new Double(one.getValorDescuento()));
		}
		else if(this.getPatronOrdenar().equals("porcentaje_descendente"))
		{
			return new Double(two.getPorcentajeDescuento()).compareTo(new Double(one.getPorcentajeDescuento()));
		}
		
		else
		{
			return  (one.getId().toLowerCase()).compareTo(two.getId().toLowerCase());
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
