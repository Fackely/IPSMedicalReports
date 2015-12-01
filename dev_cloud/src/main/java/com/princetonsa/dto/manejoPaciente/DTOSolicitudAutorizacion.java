package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.ArrayList;

import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.servinte.axioma.orm.CentrosCosto;
import com.servinte.axioma.orm.Convenios;

/**
 * Esta clase se encarga de contener los datos de solicitud
 * necesarios para generar la autorización de capitación y entidad subcontratada
 * @author Angela Maria Aguirre
 * @since 2/12/2010
 */
public class DTOSolicitudAutorizacion implements Serializable{
	
	
	/** * Serial */
	private static final long serialVersionUID = 1L;
	
	/** * Número de la solicitud */
	private int numeroSolicitud;
	
	/** * Cento de Costo solicitante */
	private CentrosCosto centrosCostoSolicitante;
	
	/** * Servicios solicitados, si hay mas de un servicio, 
	 * se genera una autorización por cada uno  */
	private ArrayList<DtoServiciosAutorizaciones> listaServicios;
	
	/** * lista de artículos a autorizar */
	private ArrayList<DtoArticulosAutorizaciones> listaArticulos;
	
	/** * convenio responsable */
	private Convenios convenioResponsable;
	
	/** * Datos para diferenciar tipos de Ordenes */
	private String tipoOrden;
	
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo numeroSolicitud
	
	 * @return retorna la variable numeroSolicitud 
	 * @author Angela Maria Aguirre 
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo numeroSolicitud
	
	 * @param valor para el atributo numeroSolicitud 
	 * @author Angela Maria Aguirre 
	 */
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo centrosCostoSolicitante
	
	 * @return retorna la variable centrosCostoSolicitante 
	 * @author Angela Maria Aguirre 
	 */
	public CentrosCosto getCentrosCostoSolicitante() {
		return centrosCostoSolicitante;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo centrosCostoSolicitante
	
	 * @param valor para el atributo centrosCostoSolicitante 
	 * @author Angela Maria Aguirre 
	 */
	public void setCentrosCostoSolicitante(CentrosCosto centrosCostoSolicitante) {
		this.centrosCostoSolicitante = centrosCostoSolicitante;
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaArticulos
	
	 * @return retorna la variable listaArticulos 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DtoArticulosAutorizaciones> getListaArticulos() {
		return listaArticulos;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaArticulos
	
	 * @param valor para el atributo listaArticulos 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaArticulos(
			ArrayList<DtoArticulosAutorizaciones> listaArticulos) {
		this.listaArticulos = listaArticulos;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaServicios
	
	 * @return retorna la variable listaServicios 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DtoServiciosAutorizaciones> getListaServicios() {
		return listaServicios;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaServicios
	
	 * @param valor para el atributo listaServicios 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaServicios(
			ArrayList<DtoServiciosAutorizaciones> listaServicios) {
		this.listaServicios = listaServicios;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo convenioResponsable
	
	 * @return retorna la variable convenioResponsable 
	 * @author Angela Maria Aguirre 
	 */
	public Convenios getConvenioResponsable() {
		return convenioResponsable;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo convenioResponsable
	
	 * @param valor para el atributo convenioResponsable 
	 * @author Angela Maria Aguirre 
	 */
	public void setConvenioResponsable(Convenios convenioResponsable) {
		this.convenioResponsable = convenioResponsable;
	}
	/**
	 * @return valor de tipoOrden
	 */
	public String getTipoOrden() {
		return tipoOrden;
	}
	/**
	 * @param tipoOrden el tipoOrden para asignar
	 */
	public void setTipoOrden(String tipoOrden) {
		this.tipoOrden = tipoOrden;
	}
	
}
