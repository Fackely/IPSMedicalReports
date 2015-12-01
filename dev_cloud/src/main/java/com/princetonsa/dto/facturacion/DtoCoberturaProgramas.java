package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.ibm.icu.math.BigDecimal;

import util.ConstantesBD;

/**
 * 
 * @author julio
 *
 */
public class DtoCoberturaProgramas implements Serializable
{
	/**	* Objeto para manejar los logs de esta clase	*/
	private static Logger logger = Logger.getLogger(DtoCoberturaProgramas.class);
	
	private double codigo;
	
	private double codigoDetalleCob;
	
	private double programa;
	
	private int cantidad;
	
	private String usuarioModifica;
	
	private String fechaModifica;
	
	private String horaModifica;
	
	private String nombrePrograma;
	
	private String especialidadPrograma;
	
	private String codigoPrograma;
	
	public DtoCoberturaProgramas()
	{
		this.codigo=ConstantesBD.codigoNuncaValidoDouble;
		this.codigoDetalleCob=ConstantesBD.codigoNuncaValidoDouble;
		this.programa=ConstantesBD.codigoNuncaValidoDouble;
		this.codigo=ConstantesBD.codigoNuncaValido;
		this.usuarioModifica="";
		this.usuarioModifica="";
		this.horaModifica="";
		this.nombrePrograma="";
		this.especialidadPrograma="";
		this.codigoPrograma="";
	}

	public double getCodigo() {
		return codigo;
	}

	public void setCodigo(double codigo) {
		this.codigo = codigo;
	}

	public double getCodigoDetalleCob() {
		return codigoDetalleCob;
	}

	public void setCodigoDetalleCob(double codigoDetalleCob) {
		this.codigoDetalleCob = codigoDetalleCob;
	}

	public double getPrograma() {
		return programa;
	}

	public void setPrograma(double programa) {
		this.programa = programa;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	public String getFechaModifica() {
		return fechaModifica;
	}

	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public String getHoraModifica() {
		return horaModifica;
	}

	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	public String getNombrePrograma() {
		return nombrePrograma;
	}

	public void setNombrePrograma(String nombrePrograma) {
		this.nombrePrograma = nombrePrograma;
	}

	public String getEspecialidadPrograma() {
		return especialidadPrograma;
	}

	public void setEspecialidadPrograma(String especialidadPrograma) {
		this.especialidadPrograma = especialidadPrograma;
	}

	public String getCodigoPrograma() {
		return codigoPrograma;
	}

	public void setCodigoPrograma(String codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}
	
	
}