package com.princetonsa.sort.odontologia.recomendaciones;

import java.util.Comparator;

import com.servinte.axioma.orm.RecomendacionesContOdonto;


/**
 * 
 * @author Edgar Carvajal Ruiz
 *
 */
public class SortRecomendaciones implements Comparator<RecomendacionesContOdonto> {

	
	
	private String patronOrdenar;

	/**
	 * 
	 * @param patronOrden
	 */
	public SortRecomendaciones (String patronOrdenar)
	{
		this.setPatronOrdenar(patronOrdenar);
	}
	
	
	
	
	
	@Override
	public int compare(RecomendacionesContOdonto o1,RecomendacionesContOdonto o2) 
	{
		if(this.getPatronOrdenar().equals("descripcion"))
		{
			return o1.getDescripcion().compareTo(o2.getDescripcion());
		}
		else if (this.getPatronOrdenar().equals("codigo"))
		{
			return o1.getCodigo().compareTo(o2.getCodigo());
		}
		
		else if(this.getPatronOrdenar().equals("descripcion_des"))
		{
			return o2.getDescripcion().compareTo(o1.getDescripcion());
		}
		
		else if(this.getPatronOrdenar().equals("activo") )
		{
			return o1.getActivo().compareTo(o2.getActivo());
		} 
		
		
		else if(this.getPatronOrdenar().equals("activo_des") )
		{
			return o2.getActivo().compareTo(o1.getActivo());
		} 
		
		else if(this.getPatronOrdenar().equals("codigo_des"))
		{
			return o2.getCodigo().compareTo(o1.getCodigo());
		}
		
		return o1.getCodigo().compareTo(o2.getCodigo());
	}



	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public String getPatronOrdenar() {
		return patronOrdenar;
	}

}
