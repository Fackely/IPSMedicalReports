/**
 * 
 */
package com.princetonsa.dto.capitacion;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * @author Cristhian Murillo
 */
public class DtoMesesTotalServiciosArticulosValorizadosPorConvenio implements Serializable
{

	/** * */
	private static final long serialVersionUID = 1L;
	
	
	/** Número del mes */
	private Integer numeroMes;
	
	/** Nombre del mes */
	private String nombreMes;
	
	/** Cantidad de servicios */
	private Integer cantidadServicios;
	
	/** Cantidad de medicamentos / insumos */
	private Integer cantidadArticulos;
	
	/** Valor de Servicio */
	private BigDecimal valorServicios;
	
	/** Valor de Articulo */
	private BigDecimal valorArticulos;

	/** Grupo de servicio */
	private String grupoServicio;
	
	/** Clase Inventario */
	private String claseInventario;
	
	/** Grupo de servicio */
	private String servicio;
	
	/** Clase Inventario */
	private String articulo;
	
	/** Nombre del convenio */
	private String nombreConvenio;
	
	/**
	 * Constructor de la clase
	 */
	public DtoMesesTotalServiciosArticulosValorizadosPorConvenio() {
		this.reset();
	}
	
	
	/** * */
	private void reset() 
	{ 
		this.numeroMes			= null;
		this.nombreMes			= null;
		this.cantidadServicios	= null;
		this.cantidadArticulos	= null;
		this.nombreConvenio		= null;
		this.valorServicios		= null;
		this.valorArticulos		= null;
		this.grupoServicio		= null;
		this.claseInventario	= null;
		this.servicio			= null;
		this.articulo			= null;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo numeroMes
	 * @return retorna la variable numeroMes 
	 * @author Cristhian Murillo
	 */
	public Integer getNumeroMes() {
		return numeroMes;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo numeroMes
	 * @param valor para el atributo numeroMes 
	 * @author Cristhian Murillo
	 */
	public void setNumeroMes(Integer numeroMes) {
		this.numeroMes = numeroMes;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo nombreMes
	 * @return retorna la variable nombreMes 
	 * @author Cristhian Murillo
	 */
	public String getNombreMes() {
		return nombreMes;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo nombreMes
	 * @param valor para el atributo nombreMes 
	 * @author Cristhian Murillo
	 */
	public void setNombreMes(String nombreMes) {
		this.nombreMes = nombreMes;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo cantidadServicios
	 * @return retorna la variable cantidadServicios 
	 */
	public Integer getCantidadServicios() {
		return cantidadServicios;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo cantidadServicios
	 * @param valor para el atributo cantidadServicios 
	 */
	public void setCantidadServicios(Integer cantidadServicios) {
		this.cantidadServicios = cantidadServicios;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo cantidadArticulos
	 * @return retorna la variable cantidadArticulos 
	 */
	public Integer getCantidadArticulos() {
		return cantidadArticulos;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo cantidadArticulos
	 * @param valor para el atributo cantidadArticulos 
	 */
	public void setCantidadArticulos(Integer cantidadArticulos) {
		this.cantidadArticulos = cantidadArticulos;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo valorServicios
	 * @return retorna la variable valorServicios 
	 */
	public BigDecimal getValorServicios() {
		return valorServicios;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo valorServicios
	 * @param valor para el atributo valorServicios 
	 */
	public void setValorServicios(BigDecimal valorServicios) {
		this.valorServicios = valorServicios;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo valorArticulos
	 * @return retorna la variable valorArticulos 
	 */
	public BigDecimal getValorArticulos() {
		return valorArticulos;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo valorArticulos
	 * @param valor para el atributo valorArticulos 
	 */
	public void setValorArticulos(BigDecimal valorArticulos) {
		this.valorArticulos = valorArticulos;
	}


	/**
	 * @return valor de grupoServicio
	 */
	public String getGrupoServicio() {
		return grupoServicio;
	}


	/**
	 * @param grupoServicio el grupoServicio para asignar
	 */
	public void setGrupoServicio(String grupoServicio) {
		this.grupoServicio = grupoServicio;
	}


	/**
	 * @return valor de claseInventario
	 */
	public String getClaseInventario() {
		return claseInventario;
	}


	/**
	 * @param claseInventario el claseInventario para asignar
	 */
	public void setClaseInventario(String claseInventario) {
		this.claseInventario = claseInventario;
	}


	/**
	 * @return valor de servicio
	 */
	public String getServicio() {
		return servicio;
	}


	/**
	 * @param servicio el servicio para asignar
	 */
	public void setServicio(String servicio) {
		this.servicio = servicio;
	}


	/**
	 * @return valor de articulo
	 */
	public String getArticulo() {
		return articulo;
	}


	/**
	 * @param articulo el articulo para asignar
	 */
	public void setArticulo(String articulo) {
		this.articulo = articulo;
	}


	/**
	 * @return valor de nombreConvenio
	 */
	public String getNombreConvenio() {
		return nombreConvenio;
	}


	/**
	 * @param nombreConvenio el nombreConvenio para asignar
	 */
	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}




	
}
