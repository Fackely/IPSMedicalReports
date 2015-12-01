package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import org.axioma.util.log.Log4JManager;


import util.UtilidadFecha;
import util.UtilidadTexto;


public class DtoInfoFechaUsuario  implements Serializable , Cloneable {

	private String horaModifica;
	private String fechaModifica;
	private String usuarioModifica;
	private String nombreUsuarioModifica;
	
	/**
	 * 
	 */
	public DtoInfoFechaUsuario(){
	    this.reset();	
	}
	
	/**
	 * 
	 * @param horaModifica
	 * @param fechaModifica
	 * @param usuarioModifica
	 */
	public DtoInfoFechaUsuario(String horaModifica, String fechaModifica,
			String usuarioModifica)
	{
		super();
		this.horaModifica = horaModifica;
		this.fechaModifica = fechaModifica;
		this.usuarioModifica = usuarioModifica;
		this.nombreUsuarioModifica = "";
	}

	public void reset(){
		this.horaModifica = "";
		this.fechaModifica = "";
		this.usuarioModifica = "";
		this.nombreUsuarioModifica = "";
	}
	
	public DtoInfoFechaUsuario(String loginUsuario){
		
		this.setHoraModifica(UtilidadFecha.getHoraActual());
		this.setFechaModifica(UtilidadFecha.getFechaActual());
		this.setUsuarioModifica(loginUsuario);
		
	}

	/**
	 * @return the horaModifica
	 */
	public String getHoraModifica() {
		return horaModifica;
	}

	/**
	 * @param horaModifica the horaModifica to set
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	/**
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
		return fechaModifica;
	}

	/**
	 * @param fechaModifica the fechaModifica to set
	 */
	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}
	
	
	/**
	 * @return the fechaModifica
	 */
	public String getFechaModificaFromatoBD() 
	{
		if(UtilidadFecha.validarFecha(this.fechaModifica))
		{	
			return  UtilidadFecha.conversionFormatoFechaABD(this.fechaModifica);
		}
		else if(!UtilidadTexto.isEmpty(this.getFechaModifica()))
		{
			return  UtilidadFecha.conversionFormatoFechaABD(this.fechaModifica); 
		}
		return "";
	}
	
	
	/***
	 * 
	 *  
	 */
	public Object clone(){
	        Object obj=null;
	        try{
	            obj=super.clone();
	        }catch(CloneNotSupportedException ex){
	        	Log4JManager.error("no se puede duplicar");
	        }
	        return obj;
	    }

	/**
	 * @return the nombreUsuarioModifica
	 */
	public String getNombreUsuarioModifica() {
		return nombreUsuarioModifica;
	}

	/**
	 * @param nombreUsuarioModifica the nombreUsuarioModifica to set
	 */
	public void setNombreUsuarioModifica(String nombreUsuarioModifica) {
		this.nombreUsuarioModifica = nombreUsuarioModifica;
	}

	
}
