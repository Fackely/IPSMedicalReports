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
public class DtoProExeCobConvXCont implements Serializable
{
	private static Logger logger = Logger.getLogger(DtoProExeCobConvXCont.class);
	
	private double codigo;
	
	private double programa;
	
	private String incluido;
	
	private int cantidad;
	
	private String fechaModifica;
	
	private String usuarioModifica;
	
	private String horaModifica;
	
	private double codigoExcepcion;
	
	private String nombrePrograma;
	
	private String especialidadPrograma;
	
	private String codigoPrograma;
	
	public DtoProExeCobConvXCont()
	{
		this.codigo=ConstantesBD.codigoNuncaValidoDouble;
		this.programa=ConstantesBD.codigoNuncaValidoDouble;
		this.incluido="";
		this.cantidad=0;
		this.fechaModifica="";
		this.usuarioModifica="";
		this.horaModifica="";
		this.codigoExcepcion=ConstantesBD.codigoNuncaValidoDouble;
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

	public double getPrograma() {
		return programa;
	}

	public void setPrograma(double programa) {
		this.programa = programa;
	}

	public String getIncluido() {
		return incluido;
	}

	public void setIncluido(String incluido) {
		this.incluido = incluido;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getFechaModifica() {
		return fechaModifica;
	}

	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	public String getHoraModifica() {
		return horaModifica;
	}

	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	public double getCodigoExcepcion() {
		return codigoExcepcion;
	}

	public void setCodigoExcepcion(double codigoExcepcion) {
		this.codigoExcepcion = codigoExcepcion;
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