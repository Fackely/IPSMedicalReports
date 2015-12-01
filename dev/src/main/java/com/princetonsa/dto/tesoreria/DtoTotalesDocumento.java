package com.princetonsa.dto.tesoreria;

import java.io.Serializable;

/**
 * Esta clase reune toda la informaci&oacute;n necesaria para mostrar los totales de los registros agrupados por estado de cada uno de los consolidados parciales.
 * (Recibos de Caja, Devoluciones, Traslados, Entregas a Transportadora o a Caja mayor)
 * 
 * @author Jorge Armando Agudelo Quintero
 * @anexo 226
 */
public class DtoTotalesDocumento  implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	/** 
	 * Indica el tipo de informaci&oacute;n a la que esta asociado el valor total por documento
	 *
	 * tipo = 0 ---> Recibos de Caja
	 * tipo = 1 ---> Devoluciones de Recibos de Caja
	 * tipo = 2 ---> Traslados a Cajas de Recaudo
	 * tipo = 3 ---> Entregas a Transportadora de valores
	 * tipo = 4 ---> Entregas a Caja Mayor Principal
	 * 
	 */
	private int tipo;
	
	/**
	 * Descripci&oacute;n del tipo de informaci&oacute;n
	 */
	private String descripcion;
	
	/**
	 * Valor total (n&uacute;mero de documentos seg&uacute;n el tipo)
	 */
	private int total;
	
	
	/**
	 * Constructor vac&iacute;o, requerido para hacer la proyecci&oacute;n del ORM
	 */
	public DtoTotalesDocumento() {
		
	}


	/**
	 * @return the tipo
	 */
	public int getTipo() {
		return tipo;
	}


	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
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
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}


	/**
	 * @param total the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	
}

