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
public class DtoCoberturaServicios implements Serializable
{
	/**	* Objeto para manejar los logs de esta clase	*/
	private static Logger logger = Logger.getLogger(DtoCoberturaServicios.class);
	
	private double codigo;
	
	private double codigoDetalleCob;
	
	private int codigoServicio;
	
	private String requiereAutorizacion;
	
	private String nombreServicio;
	
	private String codigoCUPSServicio;
	
	private String usuarioModifica;
	
	private String fechaModifica;
	
	private String horaModifica;
	
	public DtoCoberturaServicios()
	{
		this.codigo=ConstantesBD.codigoNuncaValidoDouble;
		this.codigoDetalleCob=ConstantesBD.codigoNuncaValidoDouble;
		this.codigoServicio=ConstantesBD.codigoNuncaValido;
		this.requiereAutorizacion="";
		this.nombreServicio="";
		this.codigoCUPSServicio="";
		this.usuarioModifica="";
		this.fechaModifica="";
		this.horaModifica="";
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

	public int getCodigoServicio() {
		return codigoServicio;
	}

	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	public String getRequiereAutorizacion() {
		return requiereAutorizacion;
	}

	public void setRequiereAutorizacion(String requiereAutorizacion) {
		this.requiereAutorizacion = requiereAutorizacion;
	}

	public String getNombreServicio() {
		return nombreServicio;
	}

	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}

	public String getCodigoCUPSServicio() {
		return codigoCUPSServicio;
	}

	public void setCodigoCUPSServicio(String codigoCUPSServicio) {
		this.codigoCUPSServicio = codigoCUPSServicio;
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
}