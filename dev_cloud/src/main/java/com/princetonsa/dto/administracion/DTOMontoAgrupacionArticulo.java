/**
 * 
 */
package com.princetonsa.dto.administracion;

import java.io.Serializable;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
public class DTOMontoAgrupacionArticulo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int codigo;
	private int detalleCodigo;
	private int subgrupoInventario;
	private String acronimoNaturaleza;
	private int cantidadArticulo;
	private int cantidadMonto;
	private int valorMonto;
	private int codigoInstitucion;
	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo codigo
	 *
	 * @author Angela Aguirre 
	 */
	public int getCodigo() {
		return codigo;
	}
	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo codigo
	 *
	 * @author Angela Aguirre 
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo detalleCodigo
	 *
	 * @author Angela Aguirre 
	 */
	public int getDetalleCodigo() {
		return detalleCodigo;
	}
	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo detalleCodigo
	 *
	 * @author Angela Aguirre 
	 */
	public void setDetalleCodigo(int detalleCodigo) {
		this.detalleCodigo = detalleCodigo;
	}
	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo subgrupoInventario
	 *
	 * @author Angela Aguirre 
	 */
	public int getSubgrupoInventario() {
		return subgrupoInventario;
	}
	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo subgrupoInventario
	 *
	 * @author Angela Aguirre 
	 */
	public void setSubgrupoInventario(int subgrupoInventario) {
		this.subgrupoInventario = subgrupoInventario;
	}
	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo acronimoNaturaleza
	 *
	 * @author Angela Aguirre 
	 */
	public String getAcronimoNaturaleza() {
		return acronimoNaturaleza;
	}
	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo acronimoNaturaleza
	 *
	 * @author Angela Aguirre 
	 */
	public void setAcronimoNaturaleza(String acronimoNaturaleza) {
		this.acronimoNaturaleza = acronimoNaturaleza;
	}
	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo cantidadArticulo
	 *
	 * @author Angela Aguirre 
	 */
	public int getCantidadArticulo() {
		return cantidadArticulo;
	}
	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo cantidadArticulo
	 *
	 * @author Angela Aguirre 
	 */
	public void setCantidadArticulo(int cantidadArticulo) {
		this.cantidadArticulo = cantidadArticulo;
	}
	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo cantidadMonto
	 *
	 * @author Angela Aguirre 
	 */
	public int getCantidadMonto() {
		return cantidadMonto;
	}
	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo cantidadMonto
	 *
	 * @author Angela Aguirre 
	 */
	public void setCantidadMonto(int cantidadMonto) {
		this.cantidadMonto = cantidadMonto;
	}
	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo valorMonto
	 *
	 * @author Angela Aguirre 
	 */
	public int getValorMonto() {
		return valorMonto;
	}
	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo valorMonto
	 *
	 * @author Angela Aguirre 
	 */
	public void setValorMonto(int valorMonto) {
		this.valorMonto = valorMonto;
	}
	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo codigoInstitucion
	 *
	 * @author Angela Aguirre 
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}
	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo codigoInstitucion
	 *
	 * @author Angela Aguirre 
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}	
}
