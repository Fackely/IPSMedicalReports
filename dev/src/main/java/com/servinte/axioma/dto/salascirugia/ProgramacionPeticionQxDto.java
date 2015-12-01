/**
 * 
 */
package com.servinte.axioma.dto.salascirugia;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author Oscar Pulido
 * @created 23/07/2013
 *
 */
public class ProgramacionPeticionQxDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5563760888303970799L;

	private Date fechaCirugia;
	
	private Date fechaProgramacion;
	
	private int codigoPeticion;
	
	private String horaPrgramacion;
	
	private String horaInicio;
	
	private String horaFin;
	
	private int sala;
	
	private int estadoSala;

	public ProgramacionPeticionQxDto() {

	}

	public ProgramacionPeticionQxDto(Date fechaCirugia, Date fechaProgramacion,
			int codigoPeticion, String horaPrgramacion, String horaInicio,
			String horaFin, int sala, int estadoSala) {
		this.fechaCirugia = fechaCirugia;
		this.fechaProgramacion = fechaProgramacion;
		this.codigoPeticion = codigoPeticion;
		this.horaPrgramacion = horaPrgramacion;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.sala = sala;
		this.estadoSala = estadoSala;
	}

	public Date getFechaCirugia() {
		return fechaCirugia;
	}

	public void setFechaCirugia(Date fechaCirugia) {
		this.fechaCirugia = fechaCirugia;
	}

	public Date getFechaProgramacion() {
		return fechaProgramacion;
	}

	public void setFechaProgramacion(Date fechaProgramacion) {
		this.fechaProgramacion = fechaProgramacion;
	}

	public int getCodigoPeticion() {
		return codigoPeticion;
	}

	public void setCodigoPeticion(int codigoPeticion) {
		this.codigoPeticion = codigoPeticion;
	}

	public String getHoraPrgramacion() {
		return horaPrgramacion;
	}

	public void setHoraPrgramacion(String horaPrgramacion) {
		this.horaPrgramacion = horaPrgramacion;
	}

	public String getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	public String getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(String horaFin) {
		this.horaFin = horaFin;
	}

	public int getSala() {
		return sala;
	}

	public void setSala(int sala) {
		this.sala = sala;
	}

	public int getEstadoSala() {
		return estadoSala;
	}

	public void setEstadoSala(int estadoSala) {
		this.estadoSala = estadoSala;
	}
}
