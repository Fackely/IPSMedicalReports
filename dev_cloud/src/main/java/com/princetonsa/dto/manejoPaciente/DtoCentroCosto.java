/*
 * Ago 2, 2009
 */
package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.ArrayList;

import util.InfoDatosString;

import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;

/**
 * DTO : que representa un centro de costo
 * @author Sebastián Gómez R.
 *
 */
public class DtoCentroCosto implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String codigo;
	private String identificador;
	private String codigoInterfaz;
	private String nombre;
	private InfoDatosString unidadFuncional;
	private int codigoCentroCosto;
	private Integer codigoCentroAtencion;
	private Integer codigoUnidadConsulta;
	private String descripcionUnidadConsulta;
	private ArrayList<DtoEntidadSubcontratada> listaEntidadesSubcontratadas;
	private String nombreCentroAtencion;
	private String tipoEntidadEjecuta;
	private Integer codServicio;
	private Integer codCentroAtencion;
	
	public void clean()
	{
		this.codigo ="";
		this.codigoInterfaz = "";
		this.identificador = "";
		this.nombre = "";
		this.unidadFuncional = new InfoDatosString("","");
		this.codigoCentroCosto=0; 
		this.codigoCentroAtencion=new Integer(0);
		this.codigoUnidadConsulta=new Integer(0);
		this.descripcionUnidadConsulta="";
		this.listaEntidadesSubcontratadas = new ArrayList<DtoEntidadSubcontratada>();
		this.nombreCentroAtencion="";
		this.tipoEntidadEjecuta	="";
	}
	
	/**
	 * Constructor
	 */
	public DtoCentroCosto()
	{
		this.clean();
	}

	/**
	 * Constructor utilizado para la consulta 
	 * catalogoFacturacion.obtenerCentrosCostoEntidadesSubXTipoArea
	 * @param codigo
	 * @param identificador
	 * @param codigoInterfaz
	 * @param nombre
	 * @param tipoEntidadEjecuta
	 */
	public DtoCentroCosto(int codigoCentroCosto, String identificador,
			String codigoInterfaz, String nombre,
			String tipoEntidadEjecuta) {
		this.codigoCentroCosto = codigoCentroCosto;
		this.identificador = identificador;
		this.codigoInterfaz = codigoInterfaz;
		this.nombre = nombre;
		this.tipoEntidadEjecuta = tipoEntidadEjecuta;
	}

	public DtoCentroCosto( int codigo, String identificador, String nombre,
			Integer codigoCentroAtencion, String nombreCentroAtencion, String codigoInterfaz) {
		
		this.codigoCentroCosto =codigo;
		this.identificador = identificador;
		this.nombre = nombre;
		this.codigoCentroAtencion= codigoCentroAtencion;
		this.nombreCentroAtencion=nombreCentroAtencion;
		this.codigoInterfaz = codigoInterfaz;
	}
	
	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the identificador
	 */
	public String getIdentificador() {
		return identificador;
	}

	/**
	 * @param identificador the identificador to set
	 */
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the unidadFuncional
	 */
	public InfoDatosString getUnidadFuncional() {
		return unidadFuncional;
	}

	/**
	 * @param unidadFuncional the unidadFuncional to set
	 */
	public void setUnidadFuncional(InfoDatosString unidadFuncional) {
		this.unidadFuncional = unidadFuncional;
	}
	
	/**
	 * @return the unidadFuncional
	 */
	public String getAcronimoUnidadFuncional() {
		return unidadFuncional.getAcronimo();
	}

	/**
	 * @param unidadFuncional the unidadFuncional to set
	 */
	public void setAcronimoUnidadFuncional(String unidadFuncional) {
		this.unidadFuncional.setAcronimo(unidadFuncional);
	}
	
	/**
	 * @return the unidadFuncional
	 */
	public String getInstitucionUnidadFuncional() {
		return unidadFuncional.getValue();
	}

	/**
	 * @param unidadFuncional the unidadFuncional to set
	 */
	public void setInstitucionUnidadFuncional(String institucion) {
		this.unidadFuncional.setValue(institucion);
	}

	/**
	 * @return the codigoInterfaz
	 */
	public String getCodigoInterfaz() {
		return codigoInterfaz;
	}

	/**
	 * @param codigoInterfaz the codigoInterfaz to set
	 */
	public void setCodigoInterfaz(String codigoInterfaz) {
		this.codigoInterfaz = codigoInterfaz;
	}

	public void setCodigoCentroCosto(int codigoCentroCosto) {
		this.codigoCentroCosto = codigoCentroCosto;
	}

	public int getCodigoCentroCosto() {
		return codigoCentroCosto;
	}

	public void setCodigoCentroAtencion(Integer codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}

	public Integer getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	public void setCodigoUnidadConsulta(Integer codigoUnidadConsulta) {
		this.codigoUnidadConsulta = codigoUnidadConsulta;
	}

	public Integer getCodigoUnidadConsulta() {
		return codigoUnidadConsulta;
	}

	public void setDescripcionUnidadConsulta(String descripcionUnidadConsulta) {
		this.descripcionUnidadConsulta = descripcionUnidadConsulta;
	}

	public String getDescripcionUnidadConsulta() {
		return descripcionUnidadConsulta;
	}

	/**
	 * @return valor de listaEntidadesSubcontratadas
	 */
	public ArrayList<DtoEntidadSubcontratada> getListaEntidadesSubcontratadas() {
		return listaEntidadesSubcontratadas;
	}

	/**
	 * @param listaEntidadesSubcontratadas el listaEntidadesSubcontratadas para asignar
	 */
	public void setListaEntidadesSubcontratadas(
			ArrayList<DtoEntidadSubcontratada> listaEntidadesSubcontratadas) {
		this.listaEntidadesSubcontratadas = listaEntidadesSubcontratadas;
	}
	

	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	/**
	 * 
	 * @param tipoEntidadEjecuta
	 */
	public void setTipoEntidadEjecuta(String tipoEntidadEjecuta) {
		this.tipoEntidadEjecuta = tipoEntidadEjecuta;
	}

	/**
	 * 
	 * @return TipoEntidadEjecuta
	 */
	public String getTipoEntidadEjecuta() {
		return tipoEntidadEjecuta;
	}

	public Integer getCodServicio() {
		return codServicio;
	}

	public void setCodServicio(Integer codServicio) {
		this.codServicio = codServicio;
	}

	public Integer getCodCentroAtencion() {
		return codCentroAtencion;
	}

	public void setCodCentroAtencion(Integer codCentroAtencion) {
		this.codCentroAtencion = codCentroAtencion;
	}
}
