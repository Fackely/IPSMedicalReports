/**
 * 
 */
package com.princetonsa.dto.carteraPaciente;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import util.ConstantesBD;

/**
 * @author armando
 *
 */
public class DtoCierreCarteraPaciente implements Serializable
{
	
	
	
	/**
	 * codigo del cierre.
	 */
	private BigDecimal codigo;
	
	/**
	 * institucion del cierre.
	 */
	private int institucion;
	
	/**
	 * anio del cierre.
	 */
	private String anioCierre;
	
	/**
	 * mes del cierre.
	 */
	private String mesCierre;
	
	/**
	 * indicativo  de si es cierre de saldo inicial.
	 */
	private String saldoIncial;
	
	/**
	 * fecha Generacion
	 */
	private String fechaGeneracion;
	
	/**
	 * Hora generacion.
	 */
	private String horaGeneracion;
	
	/**
	 * Usuario Generacion.
	 */
	private String usuarioGenracion;
	
	/**
	 * 
	 */
	private String observaciones;
	
	
	ArrayList<DtoDetCierreSaldoInicialCartera> detalleCierreSaldoIncial;
	
	

	public DtoCierreCarteraPaciente() 
	{
		this.codigo=new BigDecimal(ConstantesBD.codigoNuncaValido);
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.anioCierre="";
		this.mesCierre="";
		this.observaciones="";
		this.saldoIncial=ConstantesBD.acronimoSi;
		this.fechaGeneracion="";
		this.horaGeneracion="";
		this.usuarioGenracion="";
		this.detalleCierreSaldoIncial=new ArrayList<DtoDetCierreSaldoInicialCartera>();
		this.observaciones="";
	}



	

	/**
	 * @return the codigo
	 */
	public BigDecimal getCodigo() {
		return codigo;
	}



	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(BigDecimal codigo) {
		this.codigo = codigo;
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
	 * @return the anioCierre
	 */
	public String getAnioCierre() {
		return anioCierre;
	}



	/**
	 * @param anioCierre the anioCierre to set
	 */
	public void setAnioCierre(String anioCierre) {
		this.anioCierre = anioCierre;
	}



	/**
	 * @return the mesCierre
	 */
	public String getMesCierre() {
		return mesCierre;
	}



	/**
	 * @param mesCierre the mesCierre to set
	 */
	public void setMesCierre(String mesCierre) {
		this.mesCierre = mesCierre;
	}



	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}



	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}



	/**
	 * @return the saldoIncial
	 */
	public String getSaldoIncial() {
		return saldoIncial;
	}



	/**
	 * @param saldoIncial the saldoIncial to set
	 */
	public void setSaldoIncial(String saldoIncial) {
		this.saldoIncial = saldoIncial;
	}



	/**
	 * @return the fechaGeneracion
	 */
	public String getFechaGeneracion() {
		return fechaGeneracion;
	}



	/**
	 * @param fechaGeneracion the fechaGeneracion to set
	 */
	public void setFechaGeneracion(String fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}



	/**
	 * @return the horaGeneracion
	 */
	public String getHoraGeneracion() {
		return horaGeneracion;
	}



	/**
	 * @param horaGeneracion the horaGeneracion to set
	 */
	public void setHoraGeneracion(String horaGeneracion) {
		this.horaGeneracion = horaGeneracion;
	}



	/**
	 * @return the usuarioGenracion
	 */
	public String getUsuarioGenracion() {
		return usuarioGenracion;
	}



	/**
	 * @param usuarioGenracion the usuarioGenracion to set
	 */
	public void setUsuarioGenracion(String usuarioGenracion) {
		this.usuarioGenracion = usuarioGenracion;
	}



	/**
	 * @return the detalleCierreSaldoIncial
	 */
	public ArrayList<DtoDetCierreSaldoInicialCartera> getDetalleCierreSaldoIncial() {
		return detalleCierreSaldoIncial;
	}



	/**
	 * @param detalleCierreSaldoIncial the detalleCierreSaldoIncial to set
	 */
	public void setDetalleCierreSaldoIncial(
			ArrayList<DtoDetCierreSaldoInicialCartera> detalleCierreSaldoIncial) {
		this.detalleCierreSaldoIncial = detalleCierreSaldoIncial;
	}

}
