/**
 * 
 */
package com.servinte.axioma.dto.salascirugia;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author jeilones
 * @created 17/06/2013
 *
 */
public class NotaRecuperacionDto implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2994393148502440555L;

	private int institucion;	
	private int id_nota;
	private int solicitud;
	private int enfermero;
	private String nombreEnfermero;
	private Date fechaNota;
	private String horaNota;
	private Date fechaGrabacion;
	private String horaGrabacion;
	private String observaciones;
	private String especialidadesMedico;
	private String datosMedico;
	
	private List<TipoCampoNotaRecuperacionDto> tiposCampoNotaRecuperacion;

	public List<TipoCampoNotaRecuperacionDto> getTiposCampoNotaRecuperacion() {
		return tiposCampoNotaRecuperacion;
	}

	public void setTiposCampoNotaRecuperacion(
			List<TipoCampoNotaRecuperacionDto> tiposCampoNotaRecuperacion) {
		this.tiposCampoNotaRecuperacion = tiposCampoNotaRecuperacion;
	}

	public Date getFechaNota() {
		return fechaNota;
	}

	public void setFechaNota(Date fechaNota) {
		this.fechaNota = fechaNota;
	}

	public String getHoraNota() {
		return horaNota;
	}

	public void setHoraNota(String horaNota) {
		this.horaNota = horaNota;
	}

	public int getId_nota() {
		return id_nota;
	}

	public void setId_nota(int id_nota) {
		this.id_nota = id_nota;
	}

	public int getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(int solicitud) {
		this.solicitud = solicitud;
	}
	
	public int getEnfermero() {
		return enfermero;
	}

	public void setEnfermero(int enfermero) {
		this.enfermero = enfermero;
	}

	public String getNombreEnfermero() {
		return nombreEnfermero;
	}

	public void setNombreEnfermero(String nombreEnfermero) {
		this.nombreEnfermero = nombreEnfermero;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Date getFechaGrabacion() {
		return fechaGrabacion;
	}

	public void setFechaGrabacion(Date fechaGrabacion) {
		this.fechaGrabacion = fechaGrabacion;
	}

	public String getHoraGrabacion() {
		return horaGrabacion;
	}

	public void setHoraGrabacion(String horaGrabacion) {
		this.horaGrabacion = horaGrabacion;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public String getEspecialidadesMedico() {
		return especialidadesMedico;
	}

	public void setEspecialidadesMedico(String especialidadesMedico) {
		this.especialidadesMedico = especialidadesMedico;
	}

	public String getDatosMedico() {
		return datosMedico;
	}

	public void setDatosMedico(String datosMedico) {
		this.datosMedico = datosMedico;
	}

	@Override
	public NotaRecuperacionDto clone(){
		try {
			return (NotaRecuperacionDto)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
