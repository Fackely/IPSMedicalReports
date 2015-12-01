package com.princetonsa.dto.interfaz;

public class DtoLogInterfaz1E {

	String consecutivo;
	String usuarioProcesa;
	String fechaGeneracion;
	String horaGeneracion;
	String tipoMovimiento;
	String tipoDocumento;
	String path;
	String nombreArchivo;
	String fechaProceso;
	String tipoArchivo;
	String institucion;
	String tipoProceso;
	String fechaInicioDesmarcacion;
	String fechaFinalDesmaracion;
	String motivoDesmarcacion;
	
	
	public DtoLogInterfaz1E()
	{
		this.reset();
		
	}
	
	public void reset()
	{
		this.consecutivo=new String("");
		this.usuarioProcesa=new String("");
		this.fechaGeneracion=new String("");
		this.horaGeneracion=new String("");
		this.tipoMovimiento=new String("");
		this.tipoDocumento=new String("");
		this.path=new String("");
		this.nombreArchivo=new String("");
		this.fechaProceso=new String("");
		this. tipoArchivo=new String("");
		this.institucion=new String("");
		this.tipoProceso=new String("");
		this.fechaInicioDesmarcacion=new String("");
		this.fechaFinalDesmaracion=new String("");
		this.motivoDesmarcacion=new String("");
		
	}

	public String getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getUsuarioProcesa() {
		return usuarioProcesa;
	}

	public void setUsuarioProcesa(String usuarioProcesa) {
		this.usuarioProcesa = usuarioProcesa;
	}

	public String getFechaGeneracion() {
		return fechaGeneracion;
	}

	public void setFechaGeneracion(String fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}

	public String getHoraGeneracion() {
		return horaGeneracion;
	}

	public void setHoraGeneracion(String horaGeneracion) {
		this.horaGeneracion = horaGeneracion;
	}

	public String getTipoMovimiento() {
		return tipoMovimiento;
	}

	public void setTipoMovimiento(String tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public String getFechaProceso() {
		return fechaProceso;
	}

	public void setFechaProceso(String fechaProceso) {
		this.fechaProceso = fechaProceso;
	}

	public String getTipoArchivo() {
		return tipoArchivo;
	}

	public void setTipoArchivo(String tipoArchivo) {
		this.tipoArchivo = tipoArchivo;
	}

	public String getInstitucion() {
		return institucion;
	}

	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}

	public String getTipoProceso() {
		return tipoProceso;
	}

	public void setTipoProceso(String tipoProceso) {
		this.tipoProceso = tipoProceso;
	}

	public String getFechaInicioDesmarcacion() {
		return fechaInicioDesmarcacion;
	}

	public void setFechaInicioDesmarcacion(String fechaInicioDesmarcacion) {
		this.fechaInicioDesmarcacion = fechaInicioDesmarcacion;
	}

	public String getFechaFinalDesmaracion() {
		return fechaFinalDesmaracion;
	}

	public void setFechaFinalDesmaracion(String fechaFinalDesmaracion) {
		this.fechaFinalDesmaracion = fechaFinalDesmaracion;
	}

	public String getMotivoDesmarcacion() {
		return motivoDesmarcacion;
	}

	public void setMotivoDesmarcacion(String motivoDesmarcacion) {
		this.motivoDesmarcacion = motivoDesmarcacion;
	}
	
	
	
	
	
	
}
