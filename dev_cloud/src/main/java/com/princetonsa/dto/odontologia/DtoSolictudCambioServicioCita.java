/**
 * 
 */
package com.princetonsa.dto.odontologia;

import java.util.ArrayList;

/**
 * @author armando
 *
 */
public class DtoSolictudCambioServicioCita 
{

	/**
	 * 
	 */
	private int codigoPk;
	

	/**
	 * 
	 */
	private String estado;

	/**
	 * 
	 */
	private DtoCitaOdontologica cita;
	
	/**
	 * 
	 */
	private int codigoPaciente;
	

	/**
	 * 
	 */
	private String observacionesGenerales;
	

	/**
	 * 
	 */
	private int institucion;

	/**
	 * 
	 */
	private String usuarioSolicita;
	

	/**
	 * 
	 */
	private String fechaSolicitud;

	/**
	 * 
	 */
	private String horaSolicitud;
	

	/**
	 * 
	 */
	private String usuarioConfirma;

	/**
	 * 
	 */
	private String fechaConfirma;

	/**
	 * 
	 */
	private String horaConfirma;

	/**
	 * 
	 */
	private String confirmacionAutomatica;
	
	/**
	 * 
	 */
	private String usuarioAnulacion;
	
	/**
	 * 
	 */
	private String fechaAnulacion;

	/**
	 * 
	 */
	private String horaAnulacion;
	
	/**
	 * 
	 */
	private int motivoAnulacion;
	
	/**
	 * 
	 */
	private String estadoCita;
	
	/**
	 * 
	 */
	private ArrayList<DtoProgramasServiciosAnterioresCita> progServAnteriores;
	
	/**
	 * 
	 */
	private ArrayList<DtoProgramasServiciosNuevosCita> progServNuevos;

	private double totalTrifaProgServAnteriores;
	private double totalTrifaProgServNuevos;
	
	public int getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}


	public String getObservacionesGenerales() {
		return observacionesGenerales;
	}

	public void setObservacionesGenerales(String observacionesGenerales) {
		this.observacionesGenerales = observacionesGenerales;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public String getUsuarioSolicita() {
		return usuarioSolicita;
	}

	public void setUsuarioSolicita(String usuarioSolicita) {
		this.usuarioSolicita = usuarioSolicita;
	}

	public String getFechaSolicitud() {
		return fechaSolicitud;
	}

	public void setFechaSolicitud(String fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	public String getHoraSolicitud() {
		return horaSolicitud;
	}

	public void setHoraSolicitud(String horaSolicitud) {
		this.horaSolicitud = horaSolicitud;
	}

	public String getUsuarioConfirma() {
		return usuarioConfirma;
	}

	public void setUsuarioConfirma(String usuarioConfirma) {
		this.usuarioConfirma = usuarioConfirma;
	}

	public String getFechaConfirma() {
		return fechaConfirma;
	}

	public void setFechaConfirma(String fechaConfirma) {
		this.fechaConfirma = fechaConfirma;
	}

	public String getHoraConfirma() {
		return horaConfirma;
	}

	public void setHoraConfirma(String horaConfirma) {
		this.horaConfirma = horaConfirma;
	}

	public String getConfirmacionAutomatica() {
		return confirmacionAutomatica;
	}

	public void setConfirmacionAutomatica(String confirmacionAutomatica) {
		this.confirmacionAutomatica = confirmacionAutomatica;
	}

	public ArrayList<DtoProgramasServiciosAnterioresCita> getProgServAnteriores() {
		return progServAnteriores;
	}

	public void setProgServAnteriores(
			ArrayList<DtoProgramasServiciosAnterioresCita> progServAnteriores) {
		this.progServAnteriores = progServAnteriores;
	}

	public ArrayList<DtoProgramasServiciosNuevosCita> getProgServNuevos() {
		return progServNuevos;
	}

	public void setProgServNuevos(
			ArrayList<DtoProgramasServiciosNuevosCita> progServNuevos) {
		this.progServNuevos = progServNuevos;
	}

	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	public DtoCitaOdontologica getCita() {
		return cita;
	}

	public void setCita(DtoCitaOdontologica cita) {
		this.cita = cita;
	}

	public String getUsuarioAnulacion() {
		return usuarioAnulacion;
	}

	public void setUsuarioAnulacion(String usuarioAnulacion) {
		this.usuarioAnulacion = usuarioAnulacion;
	}

	public String getFechaAnulacion() {
		return fechaAnulacion;
	}

	public void setFechaAnulacion(String fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;
	}

	public String getHoraAnulacion() {
		return horaAnulacion;
	}

	public void setHoraAnulacion(String horaAnulacion) {
		this.horaAnulacion = horaAnulacion;
	}

	public int getMotivoAnulacion() {
		return motivoAnulacion;
	}

	public void setMotivoAnulacion(int motivoAnulacion) {
		this.motivoAnulacion = motivoAnulacion;
	}

	public String getEstadoCita() {
		return estadoCita;
	}

	public void setEstadoCita(String estadoCita) {
		this.estadoCita = estadoCita;
	}

	/**
	 * @return Retorna atributo totalTrifaProgServAnteriores
	 */
	public double getTotalTrifaProgServAnteriores()
	{
		return totalTrifaProgServAnteriores;
	}

	/**
	 * @param totalTrifaProgServAnteriores Asigna atributo totalTrifaProgServAnteriores
	 */
	public void setTotalTrifaProgServAnteriores(double totalTrifaProgServAnteriores)
	{
		this.totalTrifaProgServAnteriores = totalTrifaProgServAnteriores;
	}

	/**
	 * @return Retorna atributo totalTrifaProgServNuevos
	 */
	public double getTotalTrifaProgServNuevos()
	{
		return totalTrifaProgServNuevos;
	}

	/**
	 * @param totalTrifaProgServNuevos Asigna atributo totalTrifaProgServNuevos
	 */
	public void setTotalTrifaProgServNuevos(double totalTrifaProgServNuevos)
	{
		this.totalTrifaProgServNuevos = totalTrifaProgServNuevos;
	}
}
