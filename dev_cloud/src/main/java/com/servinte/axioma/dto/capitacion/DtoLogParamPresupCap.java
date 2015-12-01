package com.servinte.axioma.dto.capitacion;

import java.io.Serializable;
import java.util.Date;

import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.MotivosModifiPresupuesto;
import com.servinte.axioma.orm.Usuarios;

public class DtoLogParamPresupCap implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long codigoLog;

	private Usuarios usuarios;
	
	private Convenios convenios;
	
	private Contratos contratos;
	
	private MotivosModifiPresupuesto motivosModificacionPresupuesto;
	
	private String anioVigencia;
	
	private Date fechaModificacion;
	
	private String horaModificacion;
	
	private String nombreUsuario;

	private String numeroContrato;
	
	private String nombreConvenio;
	
	
	public DtoLogParamPresupCap() {}

	public DtoLogParamPresupCap(long codigoLog, Usuarios usuarios,
			Convenios convenios, Contratos contratos,
			MotivosModifiPresupuesto motivosModificacionPresupuesto,
			String anioVigencia, Date fechaModificacion,
			String horaModificacion, String nombreUsuario,
			String numeroContrato, String nombreConvenio) {
		super();
		this.codigoLog = codigoLog;
		this.usuarios = usuarios;
		this.convenios = convenios;
		this.contratos = contratos;
		this.motivosModificacionPresupuesto = motivosModificacionPresupuesto;
		this.anioVigencia = anioVigencia;
		this.fechaModificacion = fechaModificacion;
		this.horaModificacion = horaModificacion;
		this.nombreUsuario = nombreUsuario;
		this.numeroContrato = numeroContrato;
		this.nombreConvenio = nombreConvenio;
	}

	public long getCodigoLog() {
		return codigoLog;
	}

	public void setCodigoLog(long codigoLog) {
		this.codigoLog = codigoLog;
	}

	public Usuarios getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public Convenios getConvenios() {
		return convenios;
	}

	public void setConvenios(Convenios convenios) {
		this.convenios = convenios;
	}

	public Contratos getContratos() {
		return contratos;
	}

	public void setContratos(Contratos contratos) {
		this.contratos = contratos;
	}

	public MotivosModifiPresupuesto getMotivosModificacionPresupuesto() {
		return motivosModificacionPresupuesto;
	}

	public void setMotivosModificacionPresupuesto(
			MotivosModifiPresupuesto motivosModificacionPresupuesto) {
		this.motivosModificacionPresupuesto = motivosModificacionPresupuesto;
	}

	public String getAnioVigencia() {
		return anioVigencia;
	}

	public void setAnioVigencia(String anioVigencia) {
		this.anioVigencia = anioVigencia;
	}

	public Date getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(Date fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getHoraModificacion() {
		return horaModificacion;
	}

	public void setHoraModificacion(String horaModificacion) {
		this.horaModificacion = horaModificacion;
	}

	public String getNombreUsuario() {
		if (usuarios != null) {
			nombreUsuario = usuarios.getPersonas().getPrimerNombre() + 
							usuarios.getPersonas().getSegundoNombre()+
							usuarios.getPersonas().getPrimerApellido()+
							usuarios.getPersonas().getSegundoApellido();
		} else {
			nombreUsuario = "";
		}
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getNumeroContrato() {
		if (contratos != null) {
			numeroContrato = contratos.getNumeroContrato();
		} else {
			numeroContrato = "";
		}
		return numeroContrato;
	}

	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}

	public String getNombreConvenio() {
		if (convenios != null) {
			nombreConvenio = convenios.getNombre();
		} else {
			nombreConvenio = "";
		}
		return nombreConvenio;
	}

	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}
	
	
}
