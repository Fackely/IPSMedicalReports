/**
 * 
 */
package com.princetonsa.dto.capitacion;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;

import net.sf.jasperreports.engine.JRDataSource;


/**
 * @author Cristhian Murillo
 */
public class DtoImpresionReporteTotalServiciosArticulosValorizadosPorConvenio implements Serializable
{

	/** * */
	private static final long serialVersionUID = 1L;
	
	/** * nombreHospital */
	private String nombreHospital;
	
	/** * nitHospital */
	private String nitHospital;
	
	/** * Fecha en formato del reporte (AAAA NombreMesInicio - NombreMesFin) */
	private String fecha;
	
	/** * rutaLogo */
	private String rutaLogo;
	
	/** * ubicacionLogo */
	private String ubicacionLogo;
	
	/** * nombreLogo */
	private String nombreLogo;
	
	/** * logoDerecha */
	private String logoDerecha;
	
	/** * logoIzquierda */
	private String logoIzquierda;
	
	/** Lista meses con los valoresy cantidades respectivas para servicios */
	private  ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> listaNivelesAtencionServicios;
	
	/** Lista meses con los valoresy cantidades respectivas para Articulos*/
	private  ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> listaNivelesAtencionArticulos;
	
	/** * Id Formato reporte para Servicios */
	private int idFormatoServicios;
	
	/** * Id Tipo Reporte Articulos */
	private int idFormatoArticulos;
	
	/** * DataSource de la lista de Niveles de Atención */
	private JRDataSource ds_listaNivelesAtencion;
	
	/** * Usuario que genera el proceso */
	private String usuarioProceso;
	
	/** * Fecha en la que se genera el proceso */
	private String fechaProceso;
	
	/** * Nombre de la agrupación del subreporte */
	private String nombreAgrupacion;
	
	/** * Convenio seleccionado por el usuario para generar el reporte */
	private String nombreConvenio;
	
	/** * Contrato seleccionado por el usuario para generar el reporte*/
	private String nombreContrato;
	
	/** * Define si el reporte lleva encabezado o no */
	private boolean formatoSinEncabezado;
	
	/** * Cantidad de meses seleccionados para el reporte */
	private Integer cantidadMesesMostrar;
	
	/** * Constructor de la clase */
	public DtoImpresionReporteTotalServiciosArticulosValorizadosPorConvenio() {
		this.reset();
	}
	
	/** * Reset */
	private void reset() 
	{ 
		this.listaNivelesAtencionServicios	= new ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>();
		this.listaNivelesAtencionArticulos	= new ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>();
		this.ds_listaNivelesAtencion	= null;
		this.nombreHospital				= null;
		this.nitHospital				= null;
		this.fecha						= null;
		this.rutaLogo					= null;
		this.ubicacionLogo				= null;
		this.nombreLogo					= null;
		this.logoDerecha				= null;
		this.logoIzquierda				= null;
		this.usuarioProceso				= null;
		this.fechaProceso				= null;
		this.nombreConvenio				= null;
		this.nombreContrato				= null;
		this.idFormatoArticulos			= ConstantesBD.codigoNuncaValido;
		this.idFormatoServicios			= ConstantesBD.codigoNuncaValido;
		this.nombreAgrupacion			= null;
		this.formatoSinEncabezado		= false;
		this.cantidadMesesMostrar		= null;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo ds_listaNivelesAtencion
	 * @return retorna la variable ds_listaNivelesAtencion 
	 * @author Cristhian Murillo
	 */
	public JRDataSource getDs_listaNivelesAtencion() {
		return ds_listaNivelesAtencion;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo ds_listaNivelesAtencion
	 * @param valor para el atributo ds_listaNivelesAtencion 
	 * @author Cristhian Murillo
	 */
	public void setDs_listaNivelesAtencion(JRDataSource dsListaNivelesAtencion) {
		ds_listaNivelesAtencion = dsListaNivelesAtencion;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo fecha
	 * @return retorna la variable fecha 
	 * @author Cristhian Murillo
	 */
	public String getFecha() {
		return fecha;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo fecha
	 * @param valor para el atributo fecha 
	 * @author Cristhian Murillo
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo nombreHospital
	 * @return retorna la variable nombreHospital 
	 * @author Cristhian Murillo
	 */
	public String getNombreHospital() {
		return nombreHospital;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo nombreHospital
	 * @param valor para el atributo nombreHospital 
	 * @author Cristhian Murillo
	 */
	public void setNombreHospital(String nombreHospital) {
		this.nombreHospital = nombreHospital;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo nitHospital
	 * @return retorna la variable nitHospital 
	 * @author Cristhian Murillo
	 */
	public String getNitHospital() {
		return nitHospital;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo nitHospital
	 * @param valor para el atributo nitHospital 
	 * @author Cristhian Murillo
	 */
	public void setNitHospital(String nitHospital) {
		this.nitHospital = nitHospital;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo rutaLogo
	 * @return retorna la variable rutaLogo 
	 * @author Cristhian Murillo
	 */
	public String getRutaLogo() {
		return rutaLogo;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo rutaLogo
	 * @param valor para el atributo rutaLogo 
	 * @author Cristhian Murillo
	 */
	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo ubicacionLogo
	 * @return retorna la variable ubicacionLogo 
	 * @author Cristhian Murillo
	 */
	public String getUbicacionLogo() {
		return ubicacionLogo;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo ubicacionLogo
	 * @param valor para el atributo ubicacionLogo 
	 * @author Cristhian Murillo
	 */
	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo nombreLogo
	 * @return retorna la variable nombreLogo 
	 * @author Cristhian Murillo
	 */
	public String getNombreLogo() {
		return nombreLogo;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo nombreLogo
	 * @param valor para el atributo nombreLogo 
	 * @author Cristhian Murillo
	 */
	public void setNombreLogo(String nombreLogo) {
		this.nombreLogo = nombreLogo;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo logoDerecha
	 * @return retorna la variable logoDerecha 
	 * @author Cristhian Murillo
	 */
	public String getLogoDerecha() {
		return logoDerecha;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo logoDerecha
	 * @param valor para el atributo logoDerecha 
	 * @author Cristhian Murillo
	 */
	public void setLogoDerecha(String logoDerecha) {
		this.logoDerecha = logoDerecha;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo logoIzquierda
	 * @return retorna la variable logoIzquierda 
	 * @author Cristhian Murillo
	 */
	public String getLogoIzquierda() {
		return logoIzquierda;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo logoIzquierda
	 * @param valor para el atributo logoIzquierda 
	 * @author Cristhian Murillo
	 */
	public void setLogoIzquierda(String logoIzquierda) {
		this.logoIzquierda = logoIzquierda;
	}

	/**
	 * Este Método se encarga de obtener el valor del atributo usuarioProceso
	 * @return retorna la variable usuarioProceso 
	 * @author Cristhian Murillo
	 */
	public String getUsuarioProceso() {
		return usuarioProceso;
	}

	/**
	 * Este Método se encarga de establecer el valor del atributo usuarioProceso
	 * @param valor para el atributo usuarioProceso 
	 * @author Cristhian Murillo
	 */
	public void setUsuarioProceso(String usuarioProceso) {
		this.usuarioProceso = usuarioProceso;
	}

	/**
	 * Este Método se encarga de obtener el valor del atributo fechaProceso
	 * @return retorna la variable fechaProceso 
	 * @author Cristhian Murillo
	 */
	public String getFechaProceso() {
		return fechaProceso;
	}

	/**
	 * Este Método se encarga de establecer el valor del atributo fechaProceso
	 * @param valor para el atributo fechaProceso 
	 * @author Cristhian Murillo
	 */
	public void setFechaProceso(String fechaProceso) {
		this.fechaProceso = fechaProceso;
	}

	/**
	 * @return the listaNivelesAtencionArticulos
	 */
	public ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> getListaNivelesAtencionArticulos() {
		return listaNivelesAtencionArticulos;
	}

	/**
	 * @param listaNivelesAtencionArticulos the listaNivelesAtencionArticulos to set
	 */
	public void setListaNivelesAtencionArticulos(
			ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> listaNivelesAtencionArticulos) {
		this.listaNivelesAtencionArticulos = listaNivelesAtencionArticulos;
	}

	/**
	 * @return the idFormatoServicios
	 */
	public int getIdFormatoServicios() {
		return idFormatoServicios;
	}

	/**
	 * @param idFormatoServicios the idFormatoServicios to set
	 */
	public void setIdFormatoServicios(int idFormatoServicios) {
		this.idFormatoServicios = idFormatoServicios;
	}

	/**
	 * @return the idFormatoArticulos
	 */
	public int getIdFormatoArticulos() {
		return idFormatoArticulos;
	}

	/**
	 * @param idFormatoArticulos the idFormatoArticulos to set
	 */
	public void setIdFormatoArticulos(int idFormatoArticulos) {
		this.idFormatoArticulos = idFormatoArticulos;
	}

	/**
	 * @return valor de listaNivelesAtencionServicios
	 */
	public ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> getListaNivelesAtencionServicios() {
		return listaNivelesAtencionServicios;
	}

	/**
	 * @param listaNivelesAtencionServicios el listaNivelesAtencionServicios para asignar
	 */
	public void setListaNivelesAtencionServicios(
			ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> listaNivelesAtencionServicios) {
		this.listaNivelesAtencionServicios = listaNivelesAtencionServicios;
	}

	/**
	 * @return valor de nombreAgrupacion
	 */
	public String getNombreAgrupacion() {
		return nombreAgrupacion;
	}

	/**
	 * @param nombreAgrupacion el nombreAgrupacion para asignar
	 */
	public void setNombreAgrupacion(String nombreAgrupacion) {
		this.nombreAgrupacion = nombreAgrupacion;
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

	/**
	 * @return valor de nombreContrato
	 */
	public String getNombreContrato() {
		return nombreContrato;
	}

	/**
	 * @param nombreContrato el nombreContrato para asignar
	 */
	public void setNombreContrato(String nombreContrato) {
		this.nombreContrato = nombreContrato;
	}

	/**
	 * @return valor de formatoSinEncabezado
	 */
	public boolean isFormatoSinEncabezado() {
		return formatoSinEncabezado;
	}

	/**
	 * @param formatoSinEncabezado el formatoSinEncabezado para asignar
	 */
	public void setFormatoSinEncabezado(boolean formatoSinEncabezado) {
		this.formatoSinEncabezado = formatoSinEncabezado;
	}

	/**
	 * @return valor de cantidadMesesMostrar
	 */
	public Integer getCantidadMesesMostrar() {
		return cantidadMesesMostrar;
	}

	/**
	 * @param cantidadMesesMostrar el cantidadMesesMostrar para asignar
	 */
	public void setCantidadMesesMostrar(Integer cantidadMesesMostrar) {
		this.cantidadMesesMostrar = cantidadMesesMostrar;
	}


	
}
