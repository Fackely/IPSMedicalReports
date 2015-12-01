package com.princetonsa.dto.interfaz;

import java.io.Serializable;

public class DtoTiposInterfazDocumentosParam1E implements Serializable
{
	private String consecutivoPk;
	
	private String paramGenerales;
	
	private String tipoDocumento;
	
	private String tipoConsecutivo;
	
	private boolean huboInconsistenciaCodigoTipoConsecutivo;
	
	private String indTipoDocumento;
	
	private boolean huboInconsistenciaIndTipoDocumento;
	
	private String tipoDocumentoCruce;
	
	private boolean huboInconsistenciaTipoDocCruce;
	
	private String unidadFuncionalEstandar;
	
	private String unidadFuncionalEstandarParamGen;
	
	private boolean huboInconsistenciaUnidadFuncionalEstandar;
	
	private String institucion;
	
	private String observacionesEncabezado;
	
	private String fecha;
	
	private String hora;
	
	private String usuario;
	
	private String activo;
	
	private String fechaInactivacion;
	
	private String horaInactivacion;
	
	private String usuarioInactivacion;
	
	private DtoInterfazParamContaS1E dtoParamInterfaz1E;
	
	private String nombreDocumento;
	
	private String nombreConsecutivo;
	
	private String nombreUnidadFuncional;
	
	private String nombreUnidadFuncionalParamGen;
	
	private String tipoMovimiento;
	
	public DtoTiposInterfazDocumentosParam1E()
	{
		reset();
	}
	
	public void reset()
	{
		this.consecutivoPk="";
		this.paramGenerales="";
		this.tipoDocumento="";
		this.tipoConsecutivo="";
		this.huboInconsistenciaCodigoTipoConsecutivo = false;
		this.indTipoDocumento="";
		this.huboInconsistenciaIndTipoDocumento = false;
		this.tipoDocumentoCruce="";
		this.huboInconsistenciaTipoDocCruce = false;
		this.unidadFuncionalEstandar="";
		this.unidadFuncionalEstandarParamGen="";
		this.huboInconsistenciaUnidadFuncionalEstandar = false;
		this.institucion="";
		this.observacionesEncabezado="";
		this.fecha="";
		this.hora="";
		this.usuario="";
		this.activo="";
		this.fechaInactivacion="";
		this.horaInactivacion="";
		this.usuarioInactivacion="";
		this.dtoParamInterfaz1E=new DtoInterfazParamContaS1E();
		this.nombreConsecutivo="";
		this.nombreDocumento="";
		this.nombreUnidadFuncional="";
		this.nombreUnidadFuncionalParamGen="";
		this.tipoMovimiento="";
	}

	/**
	 * @return the huboInconsistenciaIndTipoDocumento
	 */
	public boolean isHuboInconsistenciaIndTipoDocumento() {
		return huboInconsistenciaIndTipoDocumento;
	}

	/**
	 * @param huboInconsistenciaIndTipoDocumento the huboInconsistenciaIndTipoDocumento to set
	 */
	public void setHuboInconsistenciaIndTipoDocumento(
			boolean huboInconsistenciaIndTipoDocumento) {
		this.huboInconsistenciaIndTipoDocumento = huboInconsistenciaIndTipoDocumento;
	}

	public String getConsecutivoPk() {
		return consecutivoPk;
	}

	public void setConsecutivoPk(String consecutivoPk) {
		this.consecutivoPk = consecutivoPk;
	}

	public String getParamGenerales() {
		return paramGenerales;
	}

	public void setParamGenerales(String paramGenerales) {
		this.paramGenerales = paramGenerales;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getTipoConsecutivo() {
		return tipoConsecutivo;
	}

	public void setTipoConsecutivo(String tipoConsecutivo) {
		this.tipoConsecutivo = tipoConsecutivo;
	}

	public String getIndTipoDocumento() {
		return indTipoDocumento;
	}

	public void setIndTipoDocumento(String indTipoDocumento) {
		this.indTipoDocumento = indTipoDocumento;
	}

	public String getTipoDocumentoCruce() {
		return tipoDocumentoCruce;
	}

	public void setTipoDocumentoCruce(String tipoDocumentoCruce) {
		this.tipoDocumentoCruce = tipoDocumentoCruce;
	}

	public String getUnidadFuncionalEstandar() {
		return unidadFuncionalEstandar;
	}

	public void setUnidadFuncionalEstandar(String unidadFuncionalEstandar) {
		this.unidadFuncionalEstandar = unidadFuncionalEstandar;
	}

	public String getInstitucion() {
		return institucion;
	}

	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}

	public String getObservacionesEncabezado() {
		return observacionesEncabezado;
	}

	public void setObservacionesEncabezado(String observacionesEncabezado) {
		this.observacionesEncabezado = observacionesEncabezado;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public String getFechaInactivacion() {
		return fechaInactivacion;
	}

	public void setFechaInactivacion(String fechaInactivacion) {
		this.fechaInactivacion = fechaInactivacion;
	}

	public String getHoraInactivacion() {
		return horaInactivacion;
	}

	public void setHoraInactivacion(String horaInactivacion) {
		this.horaInactivacion = horaInactivacion;
	}

	public String getUsuarioInactivacion() {
		return usuarioInactivacion;
	}

	public void setUsuarioInactivacion(String usuarioInactivacion) {
		this.usuarioInactivacion = usuarioInactivacion;
	}

	public DtoInterfazParamContaS1E getDtoParamInterfaz1E() {
		return dtoParamInterfaz1E;
	}

	public void setDtoParamInterfaz1E(DtoInterfazParamContaS1E dtoParamInterfaz1E) {
		this.dtoParamInterfaz1E = dtoParamInterfaz1E;
	}

	/**
	 * @return the huboInconsistenciaCodigoTipoConsecutivo
	 */
	public boolean isHuboInconsistenciaCodigoTipoConsecutivo() {
		return huboInconsistenciaCodigoTipoConsecutivo;
	}

	/**
	 * @param huboInconsistenciaCodigoTipoConsecutivo the huboInconsistenciaCodigoTipoConsecutivo to set
	 */
	public void setHuboInconsistenciaCodigoTipoConsecutivo(
			boolean huboInconsistenciaCodigoTipoConsecutivo) {
		this.huboInconsistenciaCodigoTipoConsecutivo = huboInconsistenciaCodigoTipoConsecutivo;
	}

	public String getNombreDocumento() {
		return nombreDocumento;
	}

	public void setNombreDocumento(String nombreDocumento) {
		this.nombreDocumento = nombreDocumento;
	}

	public String getNombreConsecutivo() {
		return nombreConsecutivo;
	}

	public void setNombreConsecutivo(String nombreConsecutivo) {
		this.nombreConsecutivo = nombreConsecutivo;
	}

	public String getNombreUnidadFuncional() {
		return nombreUnidadFuncional;
	}

	public void setNombreUnidadFuncional(String nombreUnidadFuncional) {
		this.nombreUnidadFuncional = nombreUnidadFuncional;
	}

	public boolean isHuboInconsistenciaUnidadFuncionalEstandar() {
		return huboInconsistenciaUnidadFuncionalEstandar;
	}

	public void setHuboInconsistenciaUnidadFuncionalEstandar(
			boolean huboInconsistenciaUnidadFuncionalEstandar) {
		this.huboInconsistenciaUnidadFuncionalEstandar = huboInconsistenciaUnidadFuncionalEstandar;
	}

	/**
	 * @return the huboInconsistenciaTipoDocCruce
	 */
	public boolean isHuboInconsistenciaTipoDocCruce() {
		return huboInconsistenciaTipoDocCruce;
	}

	/**
	 * @param huboInconsistenciaTipoDocCruce the huboInconsistenciaTipoDocCruce to set
	 */
	public void setHuboInconsistenciaTipoDocCruce(
			boolean huboInconsistenciaTipoDocCruce) {
		this.huboInconsistenciaTipoDocCruce = huboInconsistenciaTipoDocCruce;
	}

	public String getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(String tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	public String getUnidadFuncionalEstandarParamGen() {
		return unidadFuncionalEstandarParamGen;
	}

	public void setUnidadFuncionalEstandarParamGen(
			String unidadFuncionalEstandarParamGen) {
		this.unidadFuncionalEstandarParamGen = unidadFuncionalEstandarParamGen;
	}

	public String getNombreUnidadFuncionalParamGen() {
		return nombreUnidadFuncionalParamGen;
	}

	public void setNombreUnidadFuncionalParamGen(
			String nombreUnidadFuncionalParamGen) {
		this.nombreUnidadFuncionalParamGen = nombreUnidadFuncionalParamGen;
	}
}