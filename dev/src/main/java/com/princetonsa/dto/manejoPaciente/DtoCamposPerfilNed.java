package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.math.BigDecimal;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;

import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;

public class DtoCamposPerfilNed implements Serializable , Cloneable {

	private double codigoPk;
	private double codigoPerfilNed;
	private double escalaCampoSeccion;
	private BigDecimal valor;
	private String observaciones;
	private DtoInfoFechaUsuario datosfechaUsuarioModifica= new DtoInfoFechaUsuario();
	
	public DtoCamposPerfilNed(){
		this.reset();
	}
	
	public void reset(){
		this.codigoPk = ConstantesBD.codigoNuncaValido;
		this.codigoPerfilNed = ConstantesBD.codigoNuncaValido;
		this.escalaCampoSeccion = ConstantesBD.codigoNuncaValido;
		this.valor = new BigDecimal(0);
		this.datosfechaUsuarioModifica = new DtoInfoFechaUsuario();
		this.observaciones = "";
	}

	/**
	 * @return the codigoPk
	 */
	public double getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(double codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * @return the codigoPerfilNed
	 */
	public double getCodigoPerfilNed() {
		return codigoPerfilNed;
	}

	/**
	 * @param codigoPerfilNed the codigoPerfilNed to set
	 */
	public void setCodigoPerfilNed(double codigoPerfilNed) {
		this.codigoPerfilNed = codigoPerfilNed;
	}

	/**
	 * @return the escalaCampoSeccion
	 */
	public double getEscalaCampoSeccion() {
		return escalaCampoSeccion;
	}

	/**
	 * @param escalaCampoSeccion the escalaCampoSeccion to set
	 */
	public void setEscalaCampoSeccion(double escalaCampoSeccion) {
		this.escalaCampoSeccion = escalaCampoSeccion;
	}

	

	/**
	 * @return the datosfechaUsuarioModifica
	 */
	public DtoInfoFechaUsuario getDatosfechaUsuarioModifica() {
		return datosfechaUsuarioModifica;
	}

	/**
	 * @param datosfechaUsuarioModifica the datosfechaUsuarioModifica to set
	 */
	public void setDatosfechaUsuarioModifica(
			DtoInfoFechaUsuario datosfechaUsuarioModifica) {
		this.datosfechaUsuarioModifica = datosfechaUsuarioModifica;
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
	 * @return the valor
	 */
	public BigDecimal getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
}
