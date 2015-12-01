package com.princetonsa.dto.capitacion;

import util.ConstantesBD;

import com.servinte.axioma.orm.NaturalezaArticuloId;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public class DTOBusquedaCierreTemporalArticulo {
	
	private int codigoContrato ;
	private long codigoNivelAtencion;
	private NaturalezaArticuloId naturalezaID;
	private int codigoClaseInventario ;
	
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public DTOBusquedaCierreTemporalArticulo(){
		this.codigoContrato = ConstantesBD.codigoNuncaValido;
		this.codigoNivelAtencion = ConstantesBD.codigoNuncaValidoLong;
		this.naturalezaID = null;
		this.codigoClaseInventario = ConstantesBD.codigoNuncaValido;
	}
	
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoContrato
	
	 * @return retorna la variable codigoContrato 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoContrato() {
		return codigoContrato;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoContrato
	
	 * @param valor para el atributo codigoContrato 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoContrato(int codigoContrato) {
		this.codigoContrato = codigoContrato;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoNivelAtencion
	
	 * @return retorna la variable codigoNivelAtencion 
	 * @author Angela Maria Aguirre 
	 */
	public long getCodigoNivelAtencion() {
		return codigoNivelAtencion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoNivelAtencion
	
	 * @param valor para el atributo codigoNivelAtencion 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoNivelAtencion(long codigoNivelAtencion) {
		this.codigoNivelAtencion = codigoNivelAtencion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo naturalezaID
	
	 * @return retorna la variable naturalezaID 
	 * @author Angela Maria Aguirre 
	 */
	public NaturalezaArticuloId getNaturalezaID() {
		return naturalezaID;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo naturalezaID
	
	 * @param valor para el atributo naturalezaID 
	 * @author Angela Maria Aguirre 
	 */
	public void setNaturalezaID(NaturalezaArticuloId naturalezaID) {
		this.naturalezaID = naturalezaID;
	}


	/**
	 * @return the codigoClaseInventario
	 */
	public int getCodigoClaseInventario() {
		return codigoClaseInventario;
	}


	/**
	 * @param codigoClaseInventario the codigoClaseInventario to set
	 */
	public void setCodigoClaseInventario(int codigoClaseInventario) {
		this.codigoClaseInventario = codigoClaseInventario;
	}
	
	 
	

}
