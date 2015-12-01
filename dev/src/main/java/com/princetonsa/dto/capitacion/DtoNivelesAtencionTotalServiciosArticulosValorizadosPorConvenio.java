/**
 * 
 */
package com.princetonsa.dto.capitacion;

import java.io.Serializable;
import java.util.ArrayList;

import net.sf.jasperreports.engine.JRDataSource;


/**
 * @author Cristhian Murillo
 */
public class DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio implements Serializable
{

	/** * */
	private static final long serialVersionUID = 1L;
	
	
	/** Nivel de atención */
	String nivelAtencion;

	/** Lista meses con los valoresy cantidades respectivas para medicamentos/insumos y servicios */
	private  ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> listaMesesTotales;
	
	/** DataSource de meses totales */
	private JRDataSource ds_listaMesesTotales;
	
	/** DataSource de meses totales */
	private JRDataSource ds_listaMesesTotalesDetalle;
	
	
	/**
	 * Constructor de la clase
	 */
	public DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio() {
		this.reset();
	}
	
	
	/** * */
	private void reset() 
	{ 
		this.nivelAtencion					= null;
		this.listaMesesTotales				= new ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio>();
		
		this.ds_listaMesesTotales			= null;
		this.ds_listaMesesTotalesDetalle	= null;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo nivelAtencion
	 * @return retorna la variable nivelAtencion 
	 * @author Cristhian Murillo
	 */
	public String getNivelAtencion() {
		return nivelAtencion;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo nivelAtencion
	 * @param valor para el atributo nivelAtencion 
	 * @author Cristhian Murillo
	 */
	public void setNivelAtencion(String nivelAtencion) {
		this.nivelAtencion = nivelAtencion;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo listaMesesTotales
	 * @return retorna la variable listaMesesTotales 
	 * @author Cristhian Murillo
	 */
	public ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> getListaMesesTotales() {
		return listaMesesTotales;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo listaMesesTotales
	 * @param valor para el atributo listaMesesTotales 
	 * @author Cristhian Murillo
	 */
	public void setListaMesesTotales(
			ArrayList<DtoMesesTotalServiciosArticulosValorizadosPorConvenio> listaMesesTotales) {
		this.listaMesesTotales = listaMesesTotales;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo ds_listaMesesTotales
	 * @return retorna la variable ds_listaMesesTotales 
	 * @author Cristhian Murillo
	 */
	public JRDataSource getDs_listaMesesTotales() {
		return this.ds_listaMesesTotales;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo ds_listaMesesTotales
	 * @param valor para el atributo ds_listaMesesTotales 
	 * @author Cristhian Murillo
	 */
	public void setDs_listaMesesTotales(JRDataSource dsListaMesesTotales) {
		this.ds_listaMesesTotales = dsListaMesesTotales;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo ds_listaMesesTotalesDetalle
	 * @return retorna la variable ds_listaMesesTotalesDetalle 
	 * @author Cristhian Murillo
	 */
	public JRDataSource getDs_listaMesesTotalesDetalle() {
		return this.ds_listaMesesTotalesDetalle;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo ds_listaMesesTotalesDetalle
	 * @param valor para el atributo ds_listaMesesTotalesDetalle 
	 * @author Cristhian Murillo
	 */
	public void setDs_listaMesesTotalesDetalle(
			JRDataSource dsListaMesesTotalesDetalle) {
		this.ds_listaMesesTotalesDetalle = dsListaMesesTotalesDetalle;
	}

	
}
