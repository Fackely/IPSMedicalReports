package com.princetonsa.dto.facturacion;

import java.io.Serializable;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Esta clase se encarga de almacenar la informaci&oacute;n general del reporte
 * de tarifas por esquemas tarifarios.
 * 
 * @author Luis Fernando Hincapi&eacute; Ospina
 * @since 23/11/2010
 */
public class DTOGeneralReporteTarifasPorEsquemaTarifario implements
		Serializable {

	private static final long serialVersionUID = 1L;

	private String usuarioProceso;
	private String logoDerecha;
	private String logoIzquierda;
	private String razonSocial;
	private String nombreEsquemaTarifario;
	private String nombreEspecialidad;
	private String nombrePrograma;
	private String nombreServicio;
	private JRBeanCollectionDataSource dsListaTarifasServicios;

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
	 * M&eacute;todo encargado de establecer el valor del atributo logoDerecha.
	 * 
	 * @param logoDerecha
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setLogoDerecha(String logoDerecha) {
		this.logoDerecha = logoDerecha;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo logoIzquierda.
	 * 
	 * @return logoIzquierda
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getLogoIzquierda() {
		return logoIzquierda;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * logoIzquierda.
	 * 
	 * @param logoIzquierda
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setLogoIzquierda(String logoIzquierda) {
		this.logoIzquierda = logoIzquierda;
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
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * nombreEsquemaTarifario.
	 * 
	 * @return nombreEsquemaTarifario
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNombreEsquemaTarifario() {
		return nombreEsquemaTarifario;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * nombreEsquemaTarifario.
	 * 
	 * @param nombreEsquemaTarifario
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNombreEsquemaTarifario(String nombreEsquemaTarifario) {
		this.nombreEsquemaTarifario = nombreEsquemaTarifario;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * nombreEspecialidad.
	 * 
	 * @return nombreEspecialidad
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNombreEspecialidad() {
		return nombreEspecialidad;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * nombreEspecialidad.
	 * 
	 * @param nombreEspecialidad
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNombreEspecialidad(String nombreEspecialidad) {
		this.nombreEspecialidad = nombreEspecialidad;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo nombrePrograma.
	 * 
	 * @return nombrePrograma
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNombrePrograma() {
		return nombrePrograma;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * nombrePrograma.
	 * 
	 * @param nombrePrograma
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNombrePrograma(String nombrePrograma) {
		this.nombrePrograma = nombrePrograma;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo nombreServicio.
	 * 
	 * @return nombreServicio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNombreServicio() {
		return nombreServicio;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * nombreServicio.
	 * 
	 * @param nombreServicio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * dsListaTarifasServicios.
	 * 
	 * @return dsListaTarifasServicios
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public JRBeanCollectionDataSource getDsListaTarifasServicios() {
		return dsListaTarifasServicios;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * dsListaTarifasServicios.
	 * 
	 * @param dsListaTarifasServicios
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setDsListaTarifasServicios(
			JRBeanCollectionDataSource dsListaTarifasServicios) {
		this.dsListaTarifasServicios = dsListaTarifasServicios;
	}

}
