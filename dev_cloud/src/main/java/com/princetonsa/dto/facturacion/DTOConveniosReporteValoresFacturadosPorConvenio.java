package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.util.ArrayList;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Esta clase se encarga de determinar los valores facturados por convenio.
 * 
 * @author Luis Fernando Hincapi&eacute; Ospina
 * @since 06/12/2010
 */
public class DTOConveniosReporteValoresFacturadosPorConvenio implements
		Serializable {

	private static final long serialVersionUID = 1L;
	private String fechaInicialFactura;
	private String fechaFinalFactura;
	private String nombreInstitucion;
	private String nombreCentroAtencion;
	private int codigoConvenio;
	private String nombreConvenio;
	private int codigoEmpresa;
	ArrayList<DTOFacturasConvenios> listaValoresFacturadosConvenios;
	private JRBeanCollectionDataSource dsListaValoresFacturados;

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo fechaInicialFactura.
	 * 
	 * @return fechaInicialFactura
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getFechaInicialFactura() {
		return fechaInicialFactura;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo fechaInicialFactura.
	 * 
	 * @param fechaInicialFactura
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setFechaInicialFactura(String fechaInicialFactura) {
		this.fechaInicialFactura = fechaInicialFactura;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo fechaFinalFactura.
	 * 
	 * @return fechaFinalFactura
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getFechaFinalFactura() {
		return fechaFinalFactura;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo fechaFinalFactura.
	 * 
	 * @param fechaFinalFactura
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setFechaFinalFactura(String fechaFinalFactura) {
		this.fechaFinalFactura = fechaFinalFactura;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * nombreInstitucion.
	 * 
	 * @return nombreInstitucion
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNombreInstitucion() {
		return nombreInstitucion;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * nombreInstitucion.
	 * 
	 * @param nombreInstitucion
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNombreInstitucion(String nombreInstitucion) {
		this.nombreInstitucion = nombreInstitucion;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * nombreCentroAtencion.
	 * 
	 * @return nombreCentroAtencion
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * nombreCentroAtencion.
	 * 
	 * @param nombreCentroAtencion
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo codigoConvenio.
	 * 
	 * @return codigoConvenio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public int getCodigoConvenio() {
		return codigoConvenio;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * codigoConvenio.
	 * 
	 * @param codigoConvenio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo nombreConvenio.
	 * 
	 * @return nombreConvenio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNombreConvenio() {
		return nombreConvenio;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * nombreConvenio.
	 * 
	 * @param nombreConvenio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo codigoEmpresa.
	 * 
	 * @return codigoEmpresa
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public int getCodigoEmpresa() {
		return codigoEmpresa;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * codigoEmpresa.
	 * 
	 * @param codigoEmpresa
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setCodigoEmpresa(int codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * listaValoresFacturadosConvenios.
	 * 
	 * @return listaValoresFacturadosConvenios
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<DTOFacturasConvenios> getListaValoresFacturadosConvenios() {
		return listaValoresFacturadosConvenios;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * listaValoresFacturadosConvenios.
	 * 
	 * @param listaValoresFacturadosConvenios
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setListaValoresFacturadosConvenios(
			ArrayList<DTOFacturasConvenios> listaValoresFacturadosConvenios) {
		this.listaValoresFacturadosConvenios = listaValoresFacturadosConvenios;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * dsListaValoresFacturados.
	 * 
	 * @return dsListaValoresFacturados
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public JRBeanCollectionDataSource getDsListaValoresFacturados() {
		return dsListaValoresFacturados;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * dsListaValoresFacturados.
	 * 
	 * @param dsListaValoresFacturados
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setDsListaValoresFacturados(
			JRBeanCollectionDataSource dsListaValoresFacturados) {
		this.dsListaValoresFacturados = dsListaValoresFacturados;
	}

}
