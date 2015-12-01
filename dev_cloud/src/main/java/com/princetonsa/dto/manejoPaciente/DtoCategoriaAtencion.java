package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;


import org.apache.log4j.Logger;

import com.princetonsa.dao.sqlbase.parametrizacion.SqlBaseCentrosAtencionDao;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadLog;


public class DtoCategoriaAtencion implements Serializable {

	/**
	 *  
	 */
   private static Logger logger = Logger.getLogger(DtoCategoriaAtencion.class);
	/**
	 * 	
	 */
	private double Codigo;
	private String descripcion;
	private int institucion;
	private String fechaModifica;
	private String horaModifica;
	private String usuarioModifica;
	
	public DtoCategoriaAtencion(){
		this.reset();
	}

	/**
	 * 
	 * 
	 */
	public void reset(){
		
		this.Codigo= ConstantesBD.codigoNuncaValido;
		this.descripcion="";
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.horaModifica = "";
		this.fechaModifica = "";
		this.usuarioModifica = "";
	}
	/**
	 * @return the codigo
	 */
	public Double getCodigo() {
		return Codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(Double codigo) {
		Codigo = codigo;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
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
	public String getFechaModificaFromatoBD() {
		return UtilidadFecha.validarFecha(this.fechaModifica) ? UtilidadFecha
				.conversionFormatoFechaABD(this.fechaModifica) : "";
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
	
	
	public void Logger(){
		
		logger.info(UtilidadLog.obtenerString(this, true));
	}
	
	
	
	
	
}
