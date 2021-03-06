package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

/**
 * LogDetalleProgramas generated by hbm2java
 */
public class LogDetalleProgramas implements java.io.Serializable {

	private long codigoPk;
	private Servicios servicios;
	private Usuarios usuarios;
	private long detallePrograma;
	private long programa;
	private int orden;
	private String activo;
	private String eliminado;
	private String fecha;
	private String hora;

	public LogDetalleProgramas() {
	}

	public LogDetalleProgramas(long codigoPk, Servicios servicios,
			Usuarios usuarios, long detallePrograma, long programa, int orden,
			String activo, String eliminado, String fecha, String hora) {
		this.codigoPk = codigoPk;
		this.servicios = servicios;
		this.usuarios = usuarios;
		this.detallePrograma = detallePrograma;
		this.programa = programa;
		this.orden = orden;
		this.activo = activo;
		this.eliminado = eliminado;
		this.fecha = fecha;
		this.hora = hora;
	}

	public long getCodigoPk() {
		return this.codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public Servicios getServicios() {
		return this.servicios;
	}

	public void setServicios(Servicios servicios) {
		this.servicios = servicios;
	}

	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public long getDetallePrograma() {
		return this.detallePrograma;
	}

	public void setDetallePrograma(long detallePrograma) {
		this.detallePrograma = detallePrograma;
	}

	public long getPrograma() {
		return this.programa;
	}

	public void setPrograma(long programa) {
		this.programa = programa;
	}

	public int getOrden() {
		return this.orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public String getActivo() {
		return this.activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public String getEliminado() {
		return this.eliminado;
	}

	public void setEliminado(String eliminado) {
		this.eliminado = eliminado;
	}

	public String getFecha() {
		return this.fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return this.hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

}
