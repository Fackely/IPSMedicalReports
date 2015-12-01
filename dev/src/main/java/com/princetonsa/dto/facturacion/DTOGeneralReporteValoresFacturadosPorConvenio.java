package com.princetonsa.dto.facturacion;

import java.io.Serializable;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Esta clase se encarga de almacenar la informaci&oacute;n general del reporte
 * de valores facturados por convenio.
 * 
 * @author Luis Fernando Hincapi&eacute; Ospina
 * @since 06/12/2010
 */
public class DTOGeneralReporteValoresFacturadosPorConvenio implements
		Serializable {

	private static final long serialVersionUID = 1L;

	private String usuarioProceso;
	private String logoDerecha;
	private String razonSocial;
	private String nit;
	private String actividadEconomica;
	private String fechaInicialFactura;
	private String fechaFinalFactura;
	private String nombreInstitucion;
	private String nombreCentroAtencion;
	private String nitEmpresa;
	private String nombreEmpresa;
	private JRBeanCollectionDataSource dsListaValoresFacturadosConvenios;

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo usuarioProceso.
	 * 
	 * @return usuarioProceso
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getUsuarioProceso() {
		return usuarioProceso;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * usuarioProceso.
	 * 
	 * @param usuarioProceso
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setUsuarioProceso(String usuarioProceso) {
		this.usuarioProceso = usuarioProceso;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo logoDerecha.
	 * 
	 * @return logoDerecha
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getLogoDerecha() {
		return logoDerecha;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * logoDerecha.
	 * 
	 * @param logoDerecha
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setLogoDerecha(String logoDerecha) {
		this.logoDerecha = logoDerecha;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo razonSocial.
	 * 
	 * @return razonSocial
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getRazonSocial() {
		return razonSocial;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo razonSocial.
	 * 
	 * @param razonSocial
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo nit.
	 * 
	 * @return nit
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNit() {
		return nit;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo nit.
	 * 
	 * @param nit
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNit(String nit) {
		this.nit = nit;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * fechaInicialFactura.
	 * 
	 * @return fechaInicialFactura
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getFechaInicialFactura() {
		return fechaInicialFactura;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * fechaInicialFactura.
	 * 
	 * @param fechaInicialFactura
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setFechaInicialFactura(String fechaInicialFactura) {
		this.fechaInicialFactura = fechaInicialFactura;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * fechaFinalFactura.
	 * 
	 * @return fechaFinalFactura
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getFechaFinalFactura() {
		return fechaFinalFactura;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * fechaFinalFactura.
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
	 * M&eacute;todo encargado de obtener el valor del atributo nitEmpresa.
	 * 
	 * @return nitEmpresa
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNitEmpresa() {
		return nitEmpresa;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo nitEmpresa.
	 * 
	 * @param nitEmpresa
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNitEmpresa(String nitEmpresa) {
		this.nitEmpresa = nitEmpresa;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo nombreEmpresa.
	 * 
	 * @return nombreEmpresa
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNombreEmpresa() {
		return nombreEmpresa;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * nombreEmpresa.
	 * 
	 * @param nombreEmpresa
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * dsListaValoresFacturadosConvenios.
	 * 
	 * @return dsListaValoresFacturadosConvenios
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public JRBeanCollectionDataSource getDsListaValoresFacturadosConvenios() {
		return dsListaValoresFacturadosConvenios;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * dsListaValoresFacturadosConvenios.
	 * 
	 * @param dsListaValoresFacturadosConvenios
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setDsListaValoresFacturadosConvenios(
			JRBeanCollectionDataSource dsListaValoresFacturadosConvenios) {
		this.dsListaValoresFacturadosConvenios = dsListaValoresFacturadosConvenios;
	}

	public void setActividadEconomica(String actividadEconomica) {
		this.actividadEconomica = actividadEconomica;
	}

	public String getActividadEconomica() {
		return actividadEconomica;
	}

}
