package com.princetonsa.dto.facturacion;

import java.io.Serializable;

import util.ConstantesBD;
import util.InfoDatosStr;
import util.UtilidadFecha;

/**
 * 
 * @author axioma
 *
 */
public class DtoMediosAutorizacion implements Serializable
{
	
	private int codigo;
	private int convenio;
	private InfoDatosStr tipo;
	private String fechaModifica;
	private String horaModifica;
	private String usuarioModifica;
	private boolean activo;
	 
	/**
	 * 
	 */
	 public DtoMediosAutorizacion()
	 {
		 reset();
	 }
	 
	 /**
	  * 
	  */
	 void reset(){
		
		 this.codigo=ConstantesBD.codigoNuncaValido;
		 this.convenio=ConstantesBD.codigoNuncaValido;
		 this.tipo=new InfoDatosStr();
		 this.fechaModifica="";
		 this.horaModifica="";
		 this.usuarioModifica="";
		 this.activo=false;
		 		 
	 }
	 
	 /**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	/**
	 * @return the convenio
	 */
	public int getConvenio() {
		return convenio;
	}
	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(int convenio) {
		this.convenio = convenio;
	}
	/**
	 * @return the tipo
	 */
	
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
	
	
	public String getFechaModificaFromatoBD() {
		return UtilidadFecha.validarFecha(this.fechaModifica) ? UtilidadFecha
				.conversionFormatoFechaABD(this.fechaModifica) : "";
	}

	/**
	 * @return the tipo
	 */
	public InfoDatosStr getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(InfoDatosStr tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the activo
	 */
	public boolean isActivo() {
		return activo;
	}
	/**
	 * @return the activo
	 */
	public boolean getActivo() {
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	

}
