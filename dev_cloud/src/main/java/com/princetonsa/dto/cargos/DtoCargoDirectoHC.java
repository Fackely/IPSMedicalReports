/*
 * Enero 15, 2008
 */
package com.princetonsa.dto.cargos;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;
import util.InfoDatos;
import util.InfoDatosInt;

/**
 * 
 * Data Transfer Object: Clase donde se manejan los datos de historia clinica de un 
 * cargo directo
 * @author Sebastián Gómez R.
 *
 */
public class DtoCargoDirectoHC implements Serializable
{
	/**
	 * Consecutivo del registro
	 */
	private String codigo;
	/**
	 * FEcha de solicitud
	 */
	private String fechaSolicitud;
	/**
	 * Hora de solicitud
	 */
	private String horaSolicitud;
	/**
	 * Información del servicio
	 */
	private InfoDatosInt servicio;
	/**
	 * Información del tipo de servicio
	 */
	private InfoDatos tipoServicio;
	/**
	 * Información de la causa externa
	 */
	private InfoDatosInt causaExterna;
	/**
	 * Campo para saber si se maneja rips
	 */
	private boolean manejaRips;
	/**
	 * Finalidad de la consulta
	 */
	private InfoDatos finalidadConsulta;
	/**
	 * Finalidad del procedimiento
	 */
	private InfoDatosInt finalidadProcedimiento;
	/**
	 * Observaciones
	 */
	private String observaciones;
 	/**
 	 * 
 	 */
    private String especialidadProfesional;	
	/**
	 * Codigo de la institucion
	 */
	private int codigoInstitucion;
	/**
	 * Usuario modifica
	 */
	private InfoDatos usuarioModifica;
	
	/**
	 * Fecha modifica
	 */
	private String fechaModifica;
	/**
	 * Hora Modifica
	 */
	private String horaModifica;
	/**
	 * Tipo de cargo directo
	 */
	private String tipo;
	/**
	 * Código del personal atiende que viene de la lectura planos
	 */
	private int personalAtiende;
	/**
	 * Código de la forma de realización que viene de la lectura de planos
	 */
	private int formaRealizacion;
	
	/**
	 * Diagnósticos de los cargos directos de historia clínica
	 */
	private ArrayList<DtoDiagnosticosCargoDirectoHC> diagnosticos = new ArrayList<DtoDiagnosticosCargoDirectoHC>();
	
	/**
	 * Indica si el DTO fue cargado
	 */
	private boolean cargado;
	
	/**
	 * Indica si el registro ya existe en la base de datos
	 */
	private boolean existeBaseDatos;
	/**
	 * Constructor
	 *
	 */
	public DtoCargoDirectoHC()
	{
		this.clean();
	}

	/**
	 * Inicialización de los datos
	 *
	 */
	private void clean() 
	{
		this.codigo = "";
		this.fechaSolicitud = "";
		this.horaSolicitud = "";
		this.servicio = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.tipoServicio = new InfoDatos("","");
		this.causaExterna = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.manejaRips = false;
		this.finalidadConsulta = new InfoDatos("","");
		this.finalidadProcedimiento = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.observaciones = "";
		this.codigoInstitucion = ConstantesBD.codigoNuncaValido;
		this.usuarioModifica = new InfoDatos("","");
		this.fechaModifica = "";
		this.horaModifica = "";
		this.tipo = "";
		this.personalAtiende = ConstantesBD.codigoNuncaValido;
		this.formaRealizacion = ConstantesBD.codigoNuncaValido;
		this.diagnosticos = new ArrayList<DtoDiagnosticosCargoDirectoHC>();
		this.cargado = false;
		this.existeBaseDatos = false;
		
	}

	/**
	 * @return the causaExterna
	 */
	public InfoDatosInt getCausaExterna() {
		return causaExterna;
	}

	/**
	 * @param causaExterna the causaExterna to set
	 */
	public void setCausaExterna(InfoDatosInt causaExterna) {
		this.causaExterna = causaExterna;
	}
	
	/**
	 * @return the causaExterna
	 */
	public int getCodigoCausaExterna() {
		return causaExterna.getCodigo();
	}

	/**
	 * @param causaExterna the causaExterna to set
	 */
	public void setCodigoCausaExterna(int causaExterna) {
		this.causaExterna.setCodigo(causaExterna);
	}
	
	/**
	 * @return the causaExterna
	 */
	public String getNombreCausaExterna() {
		return causaExterna.getNombre();
	}

	/**
	 * @param causaExterna the causaExterna to set
	 */
	public void setNombreCausaExterna(String causaExterna) {
		this.causaExterna.setNombre(causaExterna);
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
	 * @return the codigoInstitucion
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * @param codigoInstitucion the codigoInstitucion to set
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
		return fechaModifica;
	}

	/**
	 * @param fechaModifica the fechaModifica to set
	 */
	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	/**
	 * @return the fechaSolicitud
	 */
	public String getFechaSolicitud() {
		return fechaSolicitud;
	}

	/**
	 * @param fechaSolicitud the fechaSolicitud to set
	 */
	public void setFechaSolicitud(String fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	/**
	 * @return the finalidadConsulta
	 */
	public InfoDatos getFinalidadConsulta() {
		return finalidadConsulta;
	}

	/**
	 * @param finalidadConsulta the finalidadConsulta to set
	 */
	public void setFinalidadConsulta(InfoDatos finalidadConsulta) {
		this.finalidadConsulta = finalidadConsulta;
	}
	
	/**
	 * @return the finalidadConsulta
	 */
	public String getCodigoFinalidadConsulta() {
		return finalidadConsulta.getId();
	}

	/**
	 * @param finalidadConsulta the finalidadConsulta to set
	 */
	public void setCodigoFinalidadConsulta(String finalidadConsulta) {
		this.finalidadConsulta.setId(finalidadConsulta);
	}
	
	/**
	 * @return the finalidadConsulta
	 */
	public String getNombreFinalidadConsulta() {
		return finalidadConsulta.getValue();
	}

	/**
	 * @param finalidadConsulta the finalidadConsulta to set
	 */
	public void setNombreFinalidadConsulta(String finalidadConsulta) {
		this.finalidadConsulta.setValue(finalidadConsulta);
	}

	/**
	 * @return the finalidadProcedimiento
	 */
	public InfoDatosInt getFinalidadProcedimiento() {
		return finalidadProcedimiento;
	}

	/**
	 * @param finalidadProcedimiento the finalidadProcedimiento to set
	 */
	public void setFinalidadProcedimiento(InfoDatosInt finalidadProcedimiento) {
		this.finalidadProcedimiento = finalidadProcedimiento;
	}
	
	/**
	 * @return the finalidadProcedimiento
	 */
	public int getCodigoFinalidadProcedimiento() {
		return finalidadProcedimiento.getCodigo();
	}

	/**
	 * @param finalidadProcedimiento the finalidadProcedimiento to set
	 */
	public void setCodigoFinalidadProcedimiento(int finalidadProcedimiento) {
		this.finalidadProcedimiento.setCodigo(finalidadProcedimiento);
	}
	
	/**
	 * @return the finalidadProcedimiento
	 */
	public String getNombreFinalidadProcedimiento() {
		return finalidadProcedimiento.getNombre();
	}

	/**
	 * @param finalidadProcedimiento the finalidadProcedimiento to set
	 */
	public void setNombreFinalidadProcedimiento(String finalidadProcedimiento) {
		this.finalidadProcedimiento.setNombre(finalidadProcedimiento);
	}

	/**
	 * @return the formaRealizacion
	 */
	public int getFormaRealizacion() {
		return formaRealizacion;
	}

	/**
	 * @param formaRealizacion the formaRealizacion to set
	 */
	public void setFormaRealizacion(int formaRealizacion) {
		this.formaRealizacion = formaRealizacion;
	}

	/**
	 * @return the horaModifica
	 */
	public String getHoraModifica() {
		return horaModifica;
	}

	/**
	 * @param horaModifica the horaModifica to set
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	/**
	 * @return the horaSolicitud
	 */
	public String getHoraSolicitud() {
		return horaSolicitud;
	}

	/**
	 * @param horaSolicitud the horaSolicitud to set
	 */
	public void setHoraSolicitud(String horaSolicitud) {
		this.horaSolicitud = horaSolicitud;
	}

	/**
	 * @return the manejaRips
	 */
	public boolean isManejaRips() {
		return manejaRips;
	}

	/**
	 * @param manejaRips the manejaRips to set
	 */
	public void setManejaRips(boolean manejaRips) {
		this.manejaRips = manejaRips;
	}

	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return the personalAtiende
	 */
	public int getPersonalAtiende() {
		return personalAtiende;
	}

	/**
	 * @param personalAtiende the personalAtiende to set
	 */
	public void setPersonalAtiende(int personalAtiende) {
		this.personalAtiende = personalAtiende;
	}

	/**
	 * @return the servicio
	 */
	public InfoDatosInt getServicio() {
		return servicio;
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(InfoDatosInt servicio) {
		this.servicio = servicio;
	}
	
	/**
	 * @return the servicio
	 */
	public int getCodigoServicio() {
		return servicio.getCodigo();
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setCodigoServicio(int servicio) {
		this.servicio.setCodigo(servicio);
	}
	
	/**
	 * @return the servicio
	 */
	public String getNombreServicio() {
		return servicio.getNombre();
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setNombreServicio(String servicio) {
		this.servicio.setNombre(servicio);
	}

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the tipoServicio
	 */
	public InfoDatos getTipoServicio() {
		return tipoServicio;
	}

	/**
	 * @param tipoServicio the tipoServicio to set
	 */
	public void setTipoServicio(InfoDatos tipoServicio) {
		this.tipoServicio = tipoServicio;
	}
	
	/**
	 * @return the tipoServicio
	 */
	public String getCodigoTipoServicio() {
		return tipoServicio.getId();
	}

	/**
	 * @param tipoServicio the tipoServicio to set
	 */
	public void setCodigoTipoServicio(String tipoServicio) {
		this.tipoServicio.setId(tipoServicio);
	}
	
	/**
	 * @return the tipoServicio
	 */
	public String getNombreTipoServicio() {
		return tipoServicio.getValue();
	}

	/**
	 * @param tipoServicio the tipoServicio to set
	 */
	public void setNombreTipoServicio(String tipoServicio) {
		this.tipoServicio.setValue(tipoServicio);
	}

	/**
	 * @return the usuarioModifica
	 */
	public InfoDatos getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(InfoDatos usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}
	
	/**
	 * @return the usuarioModifica
	 */
	public String getLoginUsuarioModifica() {
		return usuarioModifica.getId();
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setLoginUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica.setId(usuarioModifica);
	}
	
	/**
	 * @return the usuarioModifica
	 */
	public String getNombreUsuarioModifica() {
		return usuarioModifica.getValue();
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setNombreUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica.setValue(usuarioModifica);
	}

	/**
	 * @return the diagnosticos
	 */
	public ArrayList<DtoDiagnosticosCargoDirectoHC> getDiagnosticos() {
		return diagnosticos;
	}

	/**
	 * @param diagnosticos the diagnosticos to set
	 */
	public void setDiagnosticos(
			ArrayList<DtoDiagnosticosCargoDirectoHC> diagnosticos) {
		this.diagnosticos = diagnosticos;
	}

	/**
	 * @return the cargado
	 */
	public boolean isCargado() {
		return cargado;
	}

	/**
	 * @param cargado the cargado to set
	 */
	public void setCargado(boolean cargado) {
		this.cargado = cargado;
	}

	/**
	 * @return the existeBaseDatos
	 */
	public boolean isExisteBaseDatos() {
		return existeBaseDatos;
	}

	/**
	 * @param existeBaseDatos the existeBaseDatos to set
	 */
	public void setExisteBaseDatos(boolean existeBaseDatos) {
		this.existeBaseDatos = existeBaseDatos;
	}

	/**
	 * @return the especialidadProfesional
	 */
	public String getEspecialidadProfesional() {
		return especialidadProfesional;
	}

	/**
	 * @param especialidadProfesional the especialidadProfesional to set
	 */
	public void setEspecialidadProfesional(String especialidadProfesional) {
		this.especialidadProfesional = especialidadProfesional;
	}
}
