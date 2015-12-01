package com.princetonsa.dto.facturacion;

import java.io.Serializable;

import util.ConstantesBD;
import util.InfoDatosStr;

import com.servinte.axioma.orm.Programas;

public class DtoReporteTarifasPorEsquemaTarifario implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Informaci&oacute;n de c&oacute;digo y nombre de un esquema tarifario.
	 */
	private int codigoEsquemaTarifario;
	private String nombreEsquemaTarifario;

	/**
	 * Informaci&oacute;n de c&oacute;digo y nombre de una especialidad.
	 */
	private int codigoEspecialidad;
	private String nombreEspecialidad;

	/**
	 * Informaci&oacute;n de c&oacute;digo y nombre de un programa.
	 */
	private long codigoPrograma;
	private String codPrograma;
	private String nombrePrograma;

	/**
	 * Informaci&oacute;n de c&oacute;digo, nombre y tarifa de un servicio.
	 */
	private String codigoServicio;
	private String descripcionServicio;
	private String tarifaServicio;

	/**
	 * Atributo que permite obtener el servicio por medio de
	 *  la b&uacute;squeda gen&eacute;rica. 
	 */
	private InfoDatosStr servicio;

	private Programas programas;

	/**
	 * Atributo que almacena el código de la empresa-institucion.
	 */
	private long codigoEmpresaInstitucion;

	private boolean utilizaProgramasOdontologicos;
	private String usuarioProceso;
	private String razonSocial;
	private String rutaLogo;
	private String ubicacionLogo;
	private boolean multiempresa;
	private String logoDerecha;
	private String logoIzquierda;
	private String valor;

	/**
	 * M&eacute;todo constructor de la clase
	 */
	public DtoReporteTarifasPorEsquemaTarifario() {
		this.codigoEsquemaTarifario = ConstantesBD.codigoNuncaValido;
		this.nombreEsquemaTarifario = "";
		this.codigoEspecialidad = ConstantesBD.codigoNuncaValido;
		this.nombreEspecialidad = "";
		this.codigoPrograma = ConstantesBD.codigoNuncaValido;
		this.codPrograma = null;
		this.nombrePrograma = "";
		this.codigoServicio = String.valueOf(ConstantesBD.codigoNuncaValido);
		this.descripcionServicio = "";
		this.usuarioProceso = "";
		this.razonSocial = "";
		this.rutaLogo = "";
		this.ubicacionLogo = "";
		this.logoDerecha = "";
		this.logoIzquierda = "";
		this.setServicio(new InfoDatosStr());
		this.setProgramas(new Programas());
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * codigoEsquemaTarifario.
	 * 
	 * @return codigoEsquemaTarifario
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public int getCodigoEsquemaTarifario() {
		return codigoEsquemaTarifario;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * codigoEsquemaTarifario.
	 * 
	 * @param codigoEsquemaTarifario
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setCodigoEsquemaTarifario(int codigoEsquemaTarifario) {
		this.codigoEsquemaTarifario = codigoEsquemaTarifario;
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
	 * codigoEspecialidad.
	 * 
	 * @return codigoEspecialidad
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public int getCodigoEspecialidad() {
		return codigoEspecialidad;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * codigoEspecialidad.
	 * 
	 * @param codigoEspecialidad
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setCodigoEspecialidad(int codigoEspecialidad) {
		this.codigoEspecialidad = codigoEspecialidad;
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
	 * M&eacute;todo encargado de obtener el valor del atributo codigoPrograma.
	 * 
	 * @return codigoPrograma
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public long getCodigoPrograma() {
		return codigoPrograma;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * codigoPrograma.
	 * 
	 * @param codigoPrograma
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setCodigoPrograma(long codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo codPrograma.
	 * 
	 * @return codPrograma
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getCodPrograma() {
		return codPrograma;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo codPrograma.
	 * 
	 * @param codPrograma
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setCodPrograma(String codPrograma) {
		this.codPrograma = codPrograma;
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
	 * M&eacute;todo encargado de obtener el valor del atributo codigoServicio.
	 * 
	 * @return codigoServicio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getCodigoServicio() {
		return codigoServicio;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo codigoServicio.
	 * 
	 * @param codigoServicio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setCodigoServicio(String codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo descripcionServicio.
	 * 
	 * @return descripcionServicio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getDescripcionServicio() {
		return descripcionServicio;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * descripcionServicio.
	 * 
	 * @param descripcionServicio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setDescripcionServicio(String descripcionServicio) {
		this.descripcionServicio = descripcionServicio;
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
	 * M&eacute;todo encargado de obtener el valor del atributo servicio.
	 * 
	 * @return servicio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public InfoDatosStr getServicio() {
		return servicio;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo servicio.
	 * 
	 * @param servicio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setServicio(InfoDatosStr servicio) {
		this.servicio = servicio;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo programas.
	 * 
	 * @return programas
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public Programas getProgramas() {
		return programas;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo programas.
	 * 
	 * @param programas
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setProgramas(Programas programas) {
		this.programas = programas;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * codigoEmpresaInstitucion.
	 * 
	 * @return codigoEmpresaInstitucion
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public long getCodigoEmpresaInstitucion() {
		return codigoEmpresaInstitucion;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * codigoEmpresaInstitucion.
	 * 
	 * @param codigoEmpresaInstitucion
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setCodigoEmpresaInstitucion(long codigoEmpresaInstitucion) {
		this.codigoEmpresaInstitucion = codigoEmpresaInstitucion;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * utilizaProgramasOdontologicos.
	 * 
	 * @return utilizaProgramasOdontologicos
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public boolean isUtilizaProgramasOdontologicos() {
		return utilizaProgramasOdontologicos;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * utilizaProgramasOdontologicos.
	 * 
	 * @param utilizaProgramasOdontologicos
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setUtilizaProgramasOdontologicos(
			boolean utilizaProgramasOdontologicos) {
		this.utilizaProgramasOdontologicos = utilizaProgramasOdontologicos;
	}

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
	 * M&eacute;todo encargado de obtener el valor del atributo rutaLogo.
	 * 
	 * @return rutaLogo
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getRutaLogo() {
		return rutaLogo;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo rutaLogo.
	 * 
	 * @param rutaLogo
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo ubicacionLogo.
	 * 
	 * @return ubicacionLogo
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getUbicacionLogo() {
		return ubicacionLogo;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * ubicacionLogo.
	 * 
	 * @param ubicacionLogo
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo multiempresa.
	 * 
	 * @return multiempresa
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public boolean isMultiempresa() {
		return multiempresa;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo multiempresa.
	 * 
	 * @param multiempresa
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setMultiempresa(boolean esMultiempresa) {
		this.multiempresa = esMultiempresa;
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
	 * M&eacute;todo encargado de obtener el valor del atributo tarifaServicio.
	 * 
	 * @return tarifaServicio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getTarifaServicio() {
		return tarifaServicio;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * tarifaServicio.
	 * 
	 * @param tarifaServicio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setTarifaServicio(String tarifaServicio) {
		this.tarifaServicio = tarifaServicio;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo valor.
	 * 
	 * @return valor
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo valor.
	 * 
	 * @param valor
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}
	

}
