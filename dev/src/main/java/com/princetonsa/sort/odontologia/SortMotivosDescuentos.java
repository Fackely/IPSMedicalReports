package com.princetonsa.sort.odontologia;

import java.util.Comparator;

import com.princetonsa.dto.odontologia.DtoMotivoDescuento;


/**
 * 
 * @author axioma
 *
 */

public class SortMotivosDescuentos  implements Comparator<DtoMotivoDescuento>   {
	
	private String patronOrdenar;

	/**
	 * 
	 * @param patronOrden
	 */
	
	public SortMotivosDescuentos (String patronOrdenar)
	{
		this.patronOrdenar=patronOrdenar;
	}
	
	/**
	 * 
	 */
	
	public int compare(DtoMotivoDescuento one, DtoMotivoDescuento two) 
	{
		if(this.getPatronOrdenar().equals("codigo"))
		{	
			return  (one.getCodigoMotivo().toLowerCase()).compareTo(two.getCodigoMotivo().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("descripcion"))
		{
			return  (one.getDescripcion().toLowerCase()).compareTo(two.getDescripcion().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("tipo"))
		{
			return  (one.getTipo().toLowerCase()).compareTo(two.getTipo().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("activo"))
		{
			return  (one.getActivo().toLowerCase()).compareTo(two.getActivo().toLowerCase());
		}
		else if(getPatronOrdenar().equals("indicativo"))
			{
			 return (one.getIndicativo().toLowerCase()).compareTo(two.getIndicativo().toLowerCase());
			}
		else if(getPatronOrdenar().equals("indicativo_descendente"))
		{
			return (two.getIndicativo().toLowerCase()).compareTo(one.getIndicativo().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("codigo_descendente"))
		{
			return  (two.getCodigoMotivo().toLowerCase()).compareTo(one.getCodigoMotivo().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("descripcion_descendente"))
		{
			return  (two.getDescripcion().toLowerCase()).compareTo(one.getDescripcion().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("tipo_descendente"))
		{
			return  (two.getTipo().toLowerCase()).compareTo(one.getTipo().toLowerCase());
		}
		else if(this.getPatronOrdenar().equals("activo_descendente"))
		{
			return  (two.getActivo().toLowerCase()).compareTo(one.getActivo().toLowerCase());
		}
		else
		{
			return  (one.getCodigoMotivo().toLowerCase()).compareTo(two.getCodigoMotivo().toLowerCase());
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
