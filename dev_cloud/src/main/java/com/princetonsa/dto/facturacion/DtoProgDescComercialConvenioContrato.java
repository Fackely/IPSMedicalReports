package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.math.BigDecimal;

import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.odontologia.DtoPrograma;

import util.ConstantesBD;
import util.InfoDatosDouble;
import util.UtilidadFecha;

/**
 * 
 * @author axioma
 *
 */
public class DtoProgDescComercialConvenioContrato implements Serializable{

	
	private BigDecimal codigoPk;
	private DtoPrograma dtoPrograma;
	private Double porcentaje;
	private DtoInfoFechaUsuario usuarioModifica;
	private String fechaVigencia;
	private BigDecimal codigoDescuento;
	
	private String vieneBaseDatos;
	 private String borrar;
	
	
	
	/**
	 * @return the borrar
	 */
	public String getBorrar() {
		return borrar;
	}

	/**
	 * @param borrar the borrar to set
	 */
	public void setBorrar(String borrar) {
		this.borrar = borrar;
	}

	/**
	 * @return the dtoPrograma
	 */
	public DtoPrograma getDtoPrograma() {
		return dtoPrograma;
	}

	/**
	 * @param dtoPrograma the dtoPrograma to set
	 */
	public void setDtoPrograma(DtoPrograma dtoPrograma) {
		this.dtoPrograma = dtoPrograma;
	}

	/**
	 * @return the codigoPk
	 */
	public BigDecimal getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(BigDecimal codigoPk) {
		this.codigoPk = codigoPk;
	}

	

	/**
	 * @return the porcentaje
	 */
	public Double getPorcentaje() {
		return porcentaje;
	}

	/**
	 * @param porcentaje the porcentaje to set
	 */
	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
	}

	/**
	 * @return the usuarioModifica
	 */
	public DtoInfoFechaUsuario getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(DtoInfoFechaUsuario usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * @return the fechaVigencia
	 */
	public String getFechaVigencia() {
		return fechaVigencia;
	}

	/**
	 * 
	 * @return
	 */
	public String getFechaVigenciaBd(){
		return UtilidadFecha.conversionFormatoFechaABD(this.fechaVigencia);
	}
	/**
	 * @param fechaVigencia the fechaVigencia to set
	 */
	public void setFechaVigencia(String fechaVigencia) {
		this.fechaVigencia = fechaVigencia;
	}

	/**
	 * @return the codigoDescuento
	 */
	public BigDecimal getCodigoDescuento() {
		return codigoDescuento;
	}

	/**
	 * @param codigoDescuento the codigoDescuento to set
	 */
	public void setCodigoDescuento(BigDecimal codigoDescuento) {
		this.codigoDescuento = codigoDescuento;
	}

	public 	DtoProgDescComercialConvenioContrato (){		
		
		this.reset();
	}
	
	/**
	 * 
	 */
	public void reset(){
		 this.codigoPk = new BigDecimal(0);
		 this.porcentaje = new Double(0.0);
		 this.usuarioModifica = new DtoInfoFechaUsuario();
		 this.fechaVigencia = "";
		 this.codigoDescuento = new BigDecimal(0);
		 this.dtoPrograma = new DtoPrograma();
		 this.vieneBaseDatos=ConstantesBD.acronimoNo;
		 this.borrar=ConstantesBD.acronimoNo;
	}

	public void setVieneBaseDatos(String vieneBaseDatos) {
		this.vieneBaseDatos = vieneBaseDatos;
	}

	public String getVieneBaseDatos() {
		return vieneBaseDatos;
	}
	
	
	
	
	
	
	
	
	
	
	
}
