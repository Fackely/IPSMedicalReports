/**
 * 
 */
package com.servinte.axioma.dto.salascirugia;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jeilones
 * @created 17/06/2013
 * 
 */
public class IngresoSalidaPacienteDto implements Serializable {

	/**
	 * Serializable
	 */
	private static final long serialVersionUID = -2305703678480810230L;
	/**
	 * Numero de solicitud relacionado con la hqx de la seccion "Ingreso Salida Paciente"
	 */
	private int numeroSolicitud;
	/**
	 * Tipo de sala y sala de la hqx
	 */
	private TipoSalaDto tipoSala;
	private SalaCirugiaDto salaCirugia;
	/**
	 * Fechas relacionadas en la seccion "Ingreso Salida Paciente" de la hqx
	 */
	private Date fechaIngresoSala;
	private String horaIngresoSala;
	private Date fechaSalidaSala;
	private String horaSalidaSala;
	private Date fechaInicioAnestesiaSala;
	private String horaInicioAnestesiaSala;
	private Date fechaFinAnestesiaSala;
	private String horaFinAnestesiaSala;
	private Date fechaInicioActoQxSala;
	private String horaInicioActoQxSala;
	private Date fechaFinActoQxSala;
	private String horaFinActoQxSala;
	/**
	 * Duracion final de la cx en la hqx
	 */
	private String duracionFinalCirugia;
	/**
	 * Destino del paciente registrado en la hqx
	 */
	private DestinoPacienteDto destinoPaciente;
	/**
	 * Indicativo que muestra si esta o no finalizada la hqx
	 */
	private boolean finalizaHojaQuirurgica;
	/**
	 * 
	 */
	private boolean haSidoReversada;

	
	/**********************
	 * Mostrar filas seccion
	 * **************************/
	private Boolean mostrarFila1=false;
	private Boolean mostrarFila2=false;
	private Boolean mostrarFila3=false;
	private Boolean mostrarFila4=false;

	public Date getFechaIngresoSala() {
		return fechaIngresoSala;
	}

	public void setFechaIngresoSala(Date fechaIngresoSala) {
		this.fechaIngresoSala = fechaIngresoSala;
	}

	public String getHoraIngresoSala() {
		return horaIngresoSala;
	}

	public void setHoraIngresoSala(String horaIngresoSala) {
		this.horaIngresoSala = horaIngresoSala;
	}

	public Date getFechaSalidaSala() {
		return fechaSalidaSala;
	}

	public void setFechaSalidaSala(Date fechaSalidaSala) {
		this.fechaSalidaSala = fechaSalidaSala;
	}

	public String getHoraSalidaSala() {
		return horaSalidaSala;
	}

	public void setHoraSalidaSala(String horaSalidaSala) {
		this.horaSalidaSala = horaSalidaSala;
	}

	public Date getFechaInicioAnestesiaSala() {
		return fechaInicioAnestesiaSala;
	}

	public void setFechaInicioAnestesiaSala(Date fechaInicioAnestesiaSala) {
		this.fechaInicioAnestesiaSala = fechaInicioAnestesiaSala;
	}

	public String getHoraInicioAnestesiaSala() {
		return horaInicioAnestesiaSala;
	}

	public void setHoraInicioAnestesiaSala(String horaInicioAnestesiaSala) {
		this.horaInicioAnestesiaSala = horaInicioAnestesiaSala;
	}

	public Date getFechaFinAnestesiaSala() {
		return fechaFinAnestesiaSala;
	}

	public void setFechaFinAnestesiaSala(Date fechaFinAnestesiaSala) {
		this.fechaFinAnestesiaSala = fechaFinAnestesiaSala;
	}

	public String getHoraFinAnestesiaSala() {
		return horaFinAnestesiaSala;
	}

	public void setHoraFinAnestesiaSala(String horaFinAnestesiaSala) {
		this.horaFinAnestesiaSala = horaFinAnestesiaSala;
	}

	public Date getFechaInicioActoQxSala() {
		return fechaInicioActoQxSala;
	}

	public void setFechaInicioActoQxSala(Date fechaInicioActoQxSala) {
		this.fechaInicioActoQxSala = fechaInicioActoQxSala;
	}

	public String getHoraInicioActoQxSala() {
		return horaInicioActoQxSala;
	}

	public void setHoraInicioActoQxSala(String horaInicioActoQxSala) {
		this.horaInicioActoQxSala = horaInicioActoQxSala;
	}

	public Date getFechaFinActoQxSala() {
		return fechaFinActoQxSala;
	}

	public void setFechaFinActoQxSala(Date fechaFinActoQxSala) {
		this.fechaFinActoQxSala = fechaFinActoQxSala;
	}

	public String getHoraFinActoQxSala() {
		return horaFinActoQxSala;
	}

	public void setHoraFinActoQxSala(String horaFinActoQxSala) {
		this.horaFinActoQxSala = horaFinActoQxSala;
	}

	public String getDuracionFinalCirugia() {
		return duracionFinalCirugia;
	}

	public void setDuracionFinalCirugia(String duracionFinalCirugia) {
		this.duracionFinalCirugia = duracionFinalCirugia;
	}

	public DestinoPacienteDto getDestinoPaciente() {
		return destinoPaciente;
	}

	public void setDestinoPaciente(DestinoPacienteDto destinoPaciente) {
		this.destinoPaciente = destinoPaciente;
	}

	public boolean isFinalizaHojaQuirurgica() {
		return finalizaHojaQuirurgica;
	}

	public void setFinalizaHojaQuirurgica(boolean finalizaHojaQuirurgica) {
		this.finalizaHojaQuirurgica = finalizaHojaQuirurgica;
	}

	public TipoSalaDto getTipoSala() {
		return tipoSala;
	}

	public void setTipoSala(TipoSalaDto tipoSala) {
		this.tipoSala = tipoSala;
	}

	public SalaCirugiaDto getSalaCirugia() {
		return salaCirugia;
	}

	public void setSalaCirugia(SalaCirugiaDto salaCirgugia) {
		this.salaCirugia = salaCirgugia;
	}

	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	} 

	/**
	 * @return the mostrarFila1
	 */
	public Boolean getMostrarFila1() {
		return mostrarFila1;
	}

	/**
	 * @param mostrarFila1 the mostrarFila1 to set
	 */
	public void setMostrarFila1(Boolean mostrarFila1) {
		this.mostrarFila1 = mostrarFila1;
	}

	/**
	 * @return the mostrarFila2
	 */
	public Boolean getMostrarFila2() {
		return mostrarFila2;
	}

	/**
	 * @param mostrarFila2 the mostrarFila2 to set
	 */
	public void setMostrarFila2(Boolean mostrarFila2) {
		this.mostrarFila2 = mostrarFila2;
	}

	/**
	 * @return the mostrarFila3
	 */
	public Boolean getMostrarFila3() {
		return mostrarFila3;
	}

	/**
	 * @param mostrarFila3 the mostrarFila3 to set
	 */
	public void setMostrarFila3(Boolean mostrarFila3) {
		this.mostrarFila3 = mostrarFila3;
	}

	/**
	 * @return the mostrarFila4
	 */
	public Boolean getMostrarFila4() {
		return mostrarFila4;
	}

	/**
	 * @param mostrarFila4 the mostrarFila4 to set
	 */
	public void setMostrarFila4(Boolean mostrarFila4) {
		this.mostrarFila4 = mostrarFila4;
	}
	
	public boolean getMostrarSeccionIngresoSalidaPaciente(){
		return this.mostrarFila1||this.mostrarFila2||this.mostrarFila3||this.mostrarFila4;
	}

	public boolean isHaSidoReversada() {
		return haSidoReversada;
	}

	public void setHaSidoReversada(boolean haSidoReversada) {
		this.haSidoReversada = haSidoReversada;
	}
}
