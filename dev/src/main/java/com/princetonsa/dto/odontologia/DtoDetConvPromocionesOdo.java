package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadFecha;

public class DtoDetConvPromocionesOdo implements Serializable {

	
	/**
	 * 
	 */
	
	private int codigoPk; 
	private int detPromocionOdo; 
	private InfoDatosInt convenio; 
	private String activo;
	private String fechaInactivacion ;
	private String horaInactivacion;
	private String usuarioInactivacion;
	private String fechaModifica; 
	private String horaModifica;
	private String usuarioModifica;
	private boolean estadoDb;
	private boolean visible;
  
	
		/**
		 * 
		 */
		 public DtoDetConvPromocionesOdo(){
			 this.clean();
			 
		 }
	
	/**
	 * 
	 */
	void clean(){
		codigoPk=0; 
		detPromocionOdo=0;
		this.setConvenio(new InfoDatosInt()); 
		activo=ConstantesBD.acronimoSi;
		fechaInactivacion="";
		horaInactivacion="";
		usuarioInactivacion="";
		fechaModifica="";
		horaModifica="";
		usuarioModifica="";
		estadoDb=false;
		this.visible= true;
	}


	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}


	public int getCodigoPk() {
		return codigoPk;
	}


	public void setConvenio(InfoDatosInt convenio) {
		this.convenio = convenio;
	}


	public InfoDatosInt getConvenio() {
		return convenio;
	}


	/**
	 * @return the detPromocionOdo
	 */
	public int getDetPromocionOdo() {
		return detPromocionOdo;
	}


	/**
	 * @param detPromocionOdo the detPromocionOdo to set
	 */
	public void setDetPromocionOdo(int detPromocionOdo) {
		this.detPromocionOdo = detPromocionOdo;
	}


	/**
	 * @return the activo
	 */
	public String getActivo() {
		return activo;
	}


	/**
	 * @param activo the activo to set
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}


	/**
	 * @return the fechaInactivacion
	 */
	public String getFechaInactivacion() {
		return fechaInactivacion;
	}
	
	/**
	 * 
	 *
	 * @return
	 */
   public String getFechaInactivacionDB()
	{
	   return  UtilidadFecha.conversionFormatoFechaABD(this.fechaInactivacion);
	}


	/**
	 * @param fechaInactivacion the fechaInactivacion to set
	 */
	public void setFechaInactivacion(String fechaInactivacion) {
		this.fechaInactivacion = fechaInactivacion;
	}


	/**
	 * @return the horaInactivacion
	 */
	public String getHoraInactivacion() {
		return horaInactivacion;
	}


	/**
	 * @param horaInactivacion the horaInactivacion to set
	 */
	public void setHoraInactivacion(String horaInactivacion) {
		this.horaInactivacion = horaInactivacion;
	}


	/**
	 * @return the usuarioInactivacion
	 */
	public String getUsuarioInactivacion() {
		return usuarioInactivacion;
	}


	/**
	 * @param usuarioInactivacion the usuarioInactivacion to set
	 */
	public void setUsuarioInactivacion(String usuarioInactivacion) {
		this.usuarioInactivacion = usuarioInactivacion;
	}


	/**
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
		return fechaModifica;
	}
	
	/**
	 * 
	 */
	
	public String getFechaModificaBD()
	
	{
		return  UtilidadFecha.conversionFormatoFechaABD(this.fechaModifica);
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

	public void setEstadoDb(boolean estadoDb) {
		this.estadoDb = estadoDb;
	}

	public boolean isEstadoDb() {
		return estadoDb;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return visible;
	}

}
