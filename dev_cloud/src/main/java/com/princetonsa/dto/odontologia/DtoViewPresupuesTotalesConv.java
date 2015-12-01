package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;


/**
 * Dto para el manejo de la vista ViewPresupuesTotalesConv, m&oacute;dulo
 * de odontolog&iacute;a.
 *
 * @author Yennifer Guerrero
 * @since  07/09/2010
 *
 */
public class DtoViewPresupuesTotalesConv implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
	/**	
	 * Atributo que almacena el c&odigo del presupuesto.
	 */
	private long codigoPresupuesto;
	/**	
	 * Falta convenio y contrato. por definir ???
	 * TODO mirar carvajal
	 */
	
	/**
	 * M&eacute;todo constructor de la clase.
	 */
	public DtoViewPresupuesTotalesConv(){
		this.setCodigoPresupuesto(ConstantesBD.codigoNuncaValidoLong);
	}

	
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo codigoPresupuesto
	 * 
	 * @param  valor para el atributo codigoPresupuesto
	 */
	public void setCodigoPresupuesto(long codigoPresupuesto) {
		this.codigoPresupuesto = codigoPresupuesto;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo codigoPresupuesto
	 * 
	 * @return  Retorna la variable codigoPresupuesto
	 */
	public long getCodigoPresupuesto() {
		return codigoPresupuesto;
	}
	
}
