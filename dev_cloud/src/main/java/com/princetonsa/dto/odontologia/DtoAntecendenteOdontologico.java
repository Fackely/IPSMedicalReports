package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;

/**
 * @author Víctor Hugo Gómez L
 */
public class DtoAntecendenteOdontologico implements Serializable
{
	/**
	 * Versión serial
	 */
	private static final long serialVersionUID = 1L;
	private int codigoPk;
	private int codigoPaciente;
	private int ingreso;
	private int centroAtencion;
	private int institucion;
	private int codigoMedico;
	private String nombresMedico;
	private int especialidad;
	private String porConfirmar;
	private String observaciones;
	private String mostrarPor;
	private int valoracion;
	private int evolucion;
	private String fechaModifica;
	private String horaModifica;
	private String usuarioModifica;
	private ArrayList<DtoTratamientoExterno> tratamientosExternos;
	private ArrayList<DtoTratamientoInterno> tratamientosInternos;
	
	public DtoAntecendenteOdontologico()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.codigoPk = ConstantesBD.codigoNuncaValido;
		this.codigoPaciente = ConstantesBD.codigoNuncaValido;
		this.ingreso = ConstantesBD.codigoNuncaValido;
		this.centroAtencion = ConstantesBD.codigoNuncaValido;
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.codigoMedico = ConstantesBD.codigoNuncaValido;
		this.nombresMedico="";
		this.especialidad = ConstantesBD.codigoNuncaValido;
		this.porConfirmar = "";
		this.observaciones = "";
		this.mostrarPor = "";
		this.valoracion = ConstantesBD.codigoNuncaValido;
		this.evolucion = ConstantesBD.codigoNuncaValido;
		this.fechaModifica = "";
		this.horaModifica = "";
		this.usuarioModifica = "";
		this.tratamientosExternos = new ArrayList<DtoTratamientoExterno>();
		this.tratamientosInternos= new ArrayList<DtoTratamientoInterno>();
	}

	/**
	 * @return the codigoPk
	 */
	public int getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * @return the codigoPaciente
	 */
	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	/**
	 * @return the ingreso
	 */
	public int getIngreso() {
		return ingreso;
	}

	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngreso(int ingreso) {
		this.ingreso = ingreso;
	}

	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}



	/**
	 * @return the especialidad
	 */
	public int getEspecialidad() {
		return especialidad;
	}

	/**
	 * @param especialidad the especialidad to set
	 */
	public void setEspecialidad(int especialidad) {
		this.especialidad = especialidad;
	}

	/**
	 * @return the porConfirmar
	 */
	public String getPorConfirmar() {
		return porConfirmar;
	}

	/**
	 * @param porConfirmar the porConfirmar to set
	 */
	public void setPorConfirmar(String porConfirmar) {
		this.porConfirmar = porConfirmar;
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
	 * @return the mostrarPor
	 */
	public String getMostrarPor() {
		return mostrarPor;
	}

	/**
	 * @param mostrarPor the mostrarPor to set
	 */
	public void setMostrarPor(String mostrarPor) {
		this.mostrarPor = mostrarPor;
	}

	/**
	 * @return the valoracion
	 */
	public int getValoracion() {
		return valoracion;
	}

	/**
	 * @param valoracion the valoracion to set
	 */
	public void setValoracion(int valoracion) {
		this.valoracion = valoracion;
	}

	/**
	 * @return the evolucion
	 */
	public int getEvolucion() {
		return evolucion;
	}

	/**
	 * @param evolucion the evolucion to set
	 */
	public void setEvolucion(int evolucion) {
		this.evolucion = evolucion;
	}

	/**
	 * @return the tratamientosExternos
	 */
	public ArrayList<DtoTratamientoExterno> getTratamientosExternos() {
		return tratamientosExternos;
	}

	/**
	 * @param tratamientosExternos the tratamientosExternos to set
	 */
	public void setTratamientosExternos(
			ArrayList<DtoTratamientoExterno> tratamientosExternos) {
		this.tratamientosExternos = tratamientosExternos;
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
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * @return the centroAtencion
	 */
	public int getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the codigoMedico
	 */
	public int getCodigoMedico() {
		return codigoMedico;
	}

	/**
	 * @param codigoMedico the codigoMedico to set
	 */
	public void setCodigoMedico(int codigoMedico) {
		this.codigoMedico = codigoMedico;
	}

	/**
	 * @return the tratamientosInernos
	 */
	public ArrayList<DtoTratamientoInterno> getTratamientosInternos() {
		return tratamientosInternos;
	}

	/**
	 * @param tratamientosInernos the tratamientosInernos to set
	 */
	public void setTratamientosInternos(
			ArrayList<DtoTratamientoInterno> tratamientosInternos) {
		this.tratamientosInternos = tratamientosInternos;
	}

	/**
	 * @return the nombresMedico
	 */
	public String getNombresMedico() {
		return nombresMedico;
	}

	/**
	 * @param nombresMedico the nombresMedico to set
	 */
	public void setNombresMedico(String nombresMedico) {
		this.nombresMedico = nombresMedico;
	}
	
	
}
