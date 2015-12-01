/**
 * 
 */
package com.princetonsa.dto.manejoPaciente;

import java.util.Date;

import util.ConstantesBD;
import util.UtilidadFecha;

/**
 * @author axioma
 *
 */
public class DtoIngresos 
{
	/**
	 * 
	 */
	private int ingreso;
	
	/**
	 * 
	 */
	private String consecutivoIngreso;
	
	/**
	 * 
	 */
	private String fechaIngreso;
	
	/**
	 * 
	 */
	private String horaIngreso;
	
	/**
	 * 
	 */
	private int viaIngreso;
	
	/**
	 * 
	 */
	private String nombreViaIngreso;
	
	/**
	 * 
	 */
	private String tipoPaciente;
	
	/**
	 * 
	 */
	private String nombreTipoPaciente;
	
	/**
	 * 
	 */
	private String estadoIngreso;
	
	/**
	 * 
	 */
	private int codigoAreaIngreso;
	
	/**
	 * 
	 */
	private int codigoCentroAtencion;
	
	/**
	 * 
	 */
	private String descCentroAtencion;
	
	/**
	 * 
	 */
	private String tipoEvento;
	
	/**
	 * 
	 */
	private String descTipoEvento;
	

	/**
	 * 
	 */
	private String codigoEvento;
	
	
	/**
	 * 
	 */
	private String estadoEvento;
	
	private boolean tieneRegistroIncapacidad;
	
	private int idCuenta;
	
	
	
	public DtoIngresos() 
	{
		this.ingreso = ConstantesBD.codigoNuncaValido;
		this.consecutivoIngreso = "";
		this.fechaIngreso = "";
		this.horaIngreso = "";
		this.viaIngreso = ConstantesBD.codigoNuncaValido;
		this.nombreViaIngreso = "";
		this.tipoPaciente = "";
		this.nombreTipoPaciente = "";
		this.estadoIngreso = "";
		this.codigoAreaIngreso = ConstantesBD.codigoNuncaValido;
		this.nombreAreaIngreso = "";
		this.codigoCentroAtencion=ConstantesBD.codigoNuncaValido;
		this.descCentroAtencion="";
		this.tipoEvento="";
		this.codigoEvento="";
		this.descTipoEvento="";
		this.estadoEvento="";
		this.tieneRegistroIncapacidad=false;
		this.idCuenta = ConstantesBD.codigoNuncaValido;
	}
	
	
	public DtoIngresos(int codIngreso, String consecutivoIngreso, Date fechaIngreso, String horaIngreso,
							int codViaIngreso, String nombreViaIngreso, int codCentroAtencion, 
							String nombreCentroAtencion, int codCuenta){
		this.ingreso=codIngreso;
		this.consecutivoIngreso=consecutivoIngreso;
		this.fechaIngreso=UtilidadFecha.conversionFormatoFechaAAp(fechaIngreso);
		this.horaIngreso=horaIngreso;
		this.viaIngreso=codViaIngreso;
		this.nombreViaIngreso=nombreViaIngreso;
		this.codigoCentroAtencion=codCentroAtencion;
		this.descCentroAtencion=nombreCentroAtencion;
		this.idCuenta=codCuenta;
	}

	/**
	 * 
	 */
	private String nombreAreaIngreso;

	public int getIngreso() {
		return ingreso;
	}

	public void setIngreso(int ingreso) {
		this.ingreso = ingreso;
	}

	public String getConsecutivoIngreso() {
		return consecutivoIngreso;
	}

	public void setConsecutivoIngreso(String consecutivoIngreso) {
		this.consecutivoIngreso = consecutivoIngreso;
	}

	public String getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public String getHoraIngreso() {
		return horaIngreso;
	}

	public void setHoraIngreso(String horaIngreso) {
		this.horaIngreso = horaIngreso;
	}

	public int getViaIngreso() {
		return viaIngreso;
	}

	public void setViaIngreso(int viaIngreso) {
		this.viaIngreso = viaIngreso;
	}

	public String getNombreViaIngreso() {
		return nombreViaIngreso;
	}

	public void setNombreViaIngreso(String nombreViaIngreso) {
		this.nombreViaIngreso = nombreViaIngreso;
	}

	public String getTipoPaciente() {
		return tipoPaciente;
	}

	public void setTipoPaciente(String tipoPaciente) {
		this.tipoPaciente = tipoPaciente;
	}

	public String getNombreTipoPaciente() {
		return nombreTipoPaciente;
	}

	public void setNombreTipoPaciente(String nombreTipoPaciente) {
		this.nombreTipoPaciente = nombreTipoPaciente;
	}

	public String getEstadoIngreso() {
		return estadoIngreso;
	}

	public void setEstadoIngreso(String estadoIngreso) {
		this.estadoIngreso = estadoIngreso;
	}

	public int getCodigoAreaIngreso() {
		return codigoAreaIngreso;
	}

	public void setCodigoAreaIngreso(int codigoAreaIngreso) {
		this.codigoAreaIngreso = codigoAreaIngreso;
	}

	public String getNombreAreaIngreso() {
		return nombreAreaIngreso;
	}

	public void setNombreAreaIngreso(String nombreAreaIngreso) {
		this.nombreAreaIngreso = nombreAreaIngreso;
	}

	public int getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	public void setCodigoCentroAtencion(int codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}

	public String getDescCentroAtencion() {
		return descCentroAtencion;
	}

	public void setDescCentroAtencion(String descCentroAtencion) {
		this.descCentroAtencion = descCentroAtencion;
	}

	public String getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(String tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public String getCodigoEvento() {
		return codigoEvento;
	}

	public void setCodigoEvento(String codigoEvento) {
		this.codigoEvento = codigoEvento;
	}

	public String getDescTipoEvento() {
		return descTipoEvento;
	}

	public void setDescTipoEvento(String descTipoEvento) {
		this.descTipoEvento = descTipoEvento;
	}
	
	public String getViaIngresoAbreviatura()
	{
		String retorno="";
		if(this.viaIngreso==ConstantesBD.codigoViaIngresoConsultaExterna)
		{
			retorno="CE";
		}
		else if(this.viaIngreso==ConstantesBD.codigoViaIngresoAmbulatorios)
		{
			retorno="AMB";	
		}
		else if(this.viaIngreso==ConstantesBD.codigoViaIngresoUrgencias)
		{
			retorno="URG";	
		}
		else if(this.viaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion)
		{
			if(this.tipoPaciente.equals(ConstantesBD.tipoPacienteHospitalizado))
			{
				retorno="HOSP-HOS";	
			}
			else if(this.tipoPaciente.equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria))
			{
				retorno="HOS-Cx AMB";	 	
			}
		}
		return retorno;
	}

	public String getEstadoEvento() {
		return estadoEvento;
	}

	public void setEstadoEvento(String estadoEvento) {
		this.estadoEvento = estadoEvento;
	}

	public boolean isTieneRegistroIncapacidad() {
		return tieneRegistroIncapacidad;
	}

	public void setTieneRegistroIncapacidad(boolean tieneRegistroIncapacidad) {
		this.tieneRegistroIncapacidad = tieneRegistroIncapacidad;
	}


	public int getIdCuenta() {
		return idCuenta;
	}


	public void setIdCuenta(int idCuenta) {
		this.idCuenta = idCuenta;
	}

}
