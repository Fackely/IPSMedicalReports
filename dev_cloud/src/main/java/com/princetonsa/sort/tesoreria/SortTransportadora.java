package com.princetonsa.sort.tesoreria;

import java.util.Comparator;

import com.servinte.axioma.orm.TransportadoraValores;


/**
 *
 * Clase para Realizar el ordenamiento
 * @author Edgar Carvajal
 *
 */
public class SortTransportadora implements Comparator<TransportadoraValores> {

	
	
	/**
	 *Patron
	 *Hace reprentacion a los atributos del objeto Transportadora 
	 */
	private String patronOrdenamiento;
	
	
	
	
	/**
	 *Construtor
	 * @param patronOrdenamiento
	 */
	public SortTransportadora(String patronOrdenamiento){
		this.patronOrdenamiento=patronOrdenamiento;
	}
	
	
	
	@Override
	public int compare(TransportadoraValores arg0, TransportadoraValores arg1) 
	{
		
			if(this.getPatronOrdenamiento().equals("codigo"))
			{
				return arg0.getCodigo().compareTo(arg1.getCodigo());
			}
			
			else if( this.getPatronOrdenamiento().equals("codigo_des") ) 
			{
				return arg1.getCodigo().compareTo(arg0.getCodigo());
			}
			
			else if(this.getPatronOrdenamiento().equals("razonSocial"))
			{
				return arg1.getTerceros().getDescripcion().compareTo(arg0.getTerceros().getDescripcion());
			}
			
			else if(this.getPatronOrdenamiento().equals("razonSocial_des"))
			{
				return arg0.getTerceros().getDescripcion().compareTo(arg1.getTerceros().getDescripcion());
			}
			
			else if(this.getPatronOrdenamiento().equals("activo")){
				return  new String(arg1.getActivo()+"").compareTo( new String(arg0.getActivo()+"") );
			}
			
			else if (this.getPatronOrdenamiento().equals("activo_des"))
			{
				return  new String(arg0.getActivo()+"").compareTo( new String(arg1.getActivo()+"") );
			}
			
		
		return 0;
	}

	
	
	
	
	



	public void setPatronOrdenamiento(String patronOrdenamiento) {
		this.patronOrdenamiento = patronOrdenamiento;
	}




	public String getPatronOrdenamiento() {
		return patronOrdenamiento;
	}

	
	
	
}
