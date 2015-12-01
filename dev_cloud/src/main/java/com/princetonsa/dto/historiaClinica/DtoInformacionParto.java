/*
 * Enero 16, 2008
 */
package com.princetonsa.dto.historiaClinica;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;
import util.InfoDatos;
import util.InfoDatosInt;

/**
 * 
 * Data Transfer Object: de la información de parto
 * @author Sebastián Gómez R.
 *
 */
public class DtoInformacionParto implements Serializable
{
	/**
	 * Consecutivo de la información del parto
	 */
	private String consecutivo;
	
	/**
	 * Paciente
	 */
	private InfoDatosInt paciente;
	
	/**
	 * Codigo del inrgeso
	 */
	private String codigoIngreso;
	
	/**
	 * Número de solicitud
	 */
	private String numeroSolicitud;
	
	/**
	 * Fecha del proceso
	 */
	private String fechaProceso;
	
	/**
	 * Hora proceso
	 */
	private String horaProceso;
	
	/**
	 * Usuario
	 */
	private InfoDatos usuario;
	
	/**
	 * Código institución
	 */
	private int institucion;
	
	/**
	 * Edad gestacional
	 */
	private int semanasGestacional;
	
	/**
	 * Control prenatal
	 */
	private boolean controlPrenatal;
	
	/**
	 * Cantidad de hijos vivos
	 */
	private int cantidadHijosVivos;
	
	/**
	 * Cantidad de hijos muertos
	 */
	private int cantidadHijosMuertos;
	
	/**
	 * Para saber si fue parto o aborto
	 */
	private boolean parto;
	
	/**
	 * Finalizado
	 */
	private boolean finalizado;
	
	/**
	 * Inica si el registro ya existe en la base de datos
	 */
	private boolean existeBaseDatos;
	
	/**
	 * Arreglo donde se almacenan los hijos del parto
	 */
	private ArrayList<DtoInformacionRecienNacido> recienNacidos = new ArrayList<DtoInformacionRecienNacido>();
	
	/**
	 * Resetea el objeto
	 *
	 */
	 public void clean()
	 {
		 this.consecutivo = "";
		 this.paciente = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		 this.codigoIngreso = "";
		 this.numeroSolicitud = "";
		 this.fechaProceso = "";
		 this.horaProceso = "";
		 this.usuario = new InfoDatos("","");
		 this.institucion = ConstantesBD.codigoNuncaValido;
		 this.semanasGestacional = ConstantesBD.codigoNuncaValido;
		 this.controlPrenatal = false;
		 this.cantidadHijosVivos  = ConstantesBD.codigoNuncaValido;
		 this.cantidadHijosMuertos = ConstantesBD.codigoNuncaValido;
		 this.parto = false;
		 this.finalizado = false;
		 this.recienNacidos = new ArrayList<DtoInformacionRecienNacido>();
		 this.existeBaseDatos = false;
	 }
	
	
	 /**
	  * Constructor
	  *
	  */
	public DtoInformacionParto()
	{
		this.clean();
	}


	/**
	 * @return the cantidadHijosMuertos
	 */
	public int getCantidadHijosMuertos() {
		return cantidadHijosMuertos;
	}


	/**
	 * @param cantidadHijosMuertos the cantidadHijosMuertos to set
	 */
	public void setCantidadHijosMuertos(int cantidadHijosMuertos) {
		this.cantidadHijosMuertos = cantidadHijosMuertos;
	}


	/**
	 * @return the cantidadHijosVivos
	 */
	public int getCantidadHijosVivos() {
		return cantidadHijosVivos;
	}


	/**
	 * @param cantidadHijosVivos the cantidadHijosVivos to set
	 */
	public void setCantidadHijosVivos(int cantidadHijosVivos) {
		this.cantidadHijosVivos = cantidadHijosVivos;
	}


	/**
	 * @return the codigoIngreso
	 */
	public String getCodigoIngreso() {
		return codigoIngreso;
	}


	/**
	 * @param codigoIngreso the codigoIngreso to set
	 */
	public void setCodigoIngreso(String codigoIngreso) {
		this.codigoIngreso = codigoIngreso;
	}


	/**
	 * @return the consecutivo
	 */
	public String getConsecutivo() {
		return consecutivo;
	}


	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}


	/**
	 * @return the controlPrenatal
	 */
	public boolean isControlPrenatal() {
		return controlPrenatal;
	}


	/**
	 * @param controlPrenatal the controlPrenatal to set
	 */
	public void setControlPrenatal(boolean controlPrenatal) {
		this.controlPrenatal = controlPrenatal;
	}


	/**
	 * @return the fechaProceso
	 */
	public String getFechaProceso() {
		return fechaProceso;
	}


	/**
	 * @param fechaProceso the fechaProceso to set
	 */
	public void setFechaProceso(String fechaProceso) {
		this.fechaProceso = fechaProceso;
	}


	/**
	 * @return the finalizado
	 */
	public boolean isFinalizado() {
		return finalizado;
	}


	/**
	 * @param finalizado the finalizado to set
	 */
	public void setFinalizado(boolean finalizado) {
		this.finalizado = finalizado;
	}


	/**
	 * @return the horaProceso
	 */
	public String getHoraProceso() {
		return horaProceso;
	}


	/**
	 * @param horaProceso the horaProceso to set
	 */
	public void setHoraProceso(String horaProceso) {
		this.horaProceso = horaProceso;
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
	 * @return the paciente
	 */
	public InfoDatosInt getPaciente() {
		return paciente;
	}


	/**
	 * @param paciente the paciente to set
	 */
	public void setPaciente(InfoDatosInt paciente) {
		this.paciente = paciente;
	}
	
	/**
	 * @return the paciente
	 */
	public int getCodigoPaciente() {
		return paciente.getCodigo();
	}


	/**
	 * @param paciente the paciente to set
	 */
	public void setCodigoPaciente(int codigo) {
		this.paciente.setCodigo(codigo);
	}
	
	/**
	 * @return the paciente
	 */
	public String getNombrePaciente() {
		return paciente.getNombre();
	}


	/**
	 * @param paciente the paciente to set
	 */
	public void setNombrePaciente(String nombre) {
		this.paciente.setNombre(nombre);
	}


	/**
	 * @return the parto
	 */
	public boolean isParto() {
		return parto;
	}


	/**
	 * @param parto the parto to set
	 */
	public void setParto(boolean parto) {
		this.parto = parto;
	}


	/**
	 * @return the semanasGestacional
	 */
	public int getSemanasGestacional() {
		return semanasGestacional;
	}


	/**
	 * @param semanasGestacional the semanasGestacional to set
	 */
	public void setSemanasGestacional(int semanasGestacional) {
		this.semanasGestacional = semanasGestacional;
	}


	/**
	 * @return the usuario
	 */
	public InfoDatos getUsuario() {
		return usuario;
	}


	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(InfoDatos usuario) {
		this.usuario = usuario;
	}
	
	/**
	 * @return the usuario
	 */
	public String getLoginUsuario() {
		return usuario.getId();
	}


	/**
	 * @param usuario the usuario to set
	 */
	public void setLoginUsuario(String loginUsuario) {
		this.usuario.setId(loginUsuario);
	}
	
	/**
	 * @return the usuario
	 */
	public String getNombreUsuario() {
		return usuario.getValue();
	}


	/**
	 * @param usuario the usuario to set
	 */
	public void setNombreUsuario(String nombreUsuario) {
		this.usuario.setValue(nombreUsuario);
	}


	/**
	 * @return the numeroSolicitud
	 */
	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}


	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}


	/**
	 * @return the recienNacidos
	 */
	public ArrayList<DtoInformacionRecienNacido> getRecienNacidos() {
		return recienNacidos;
	}


	/**
	 * @param recienNacidos the recienNacidos to set
	 */
	public void setRecienNacidos(ArrayList<DtoInformacionRecienNacido> recienNacidos) {
		this.recienNacidos = recienNacidos;
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
}
