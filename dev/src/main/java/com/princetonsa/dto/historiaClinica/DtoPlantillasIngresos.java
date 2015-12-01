package com.princetonsa.dto.historiaClinica;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;
import util.UtilidadFecha;

/**
 * @author axioma
 * Agosto 21 de 2009
 *
 */
public class DtoPlantillasIngresos implements Serializable 
{
	private double codigoPK;
	
	private int ingreso;
	
	private int numeroSolicitud;
	
	private int codigoPaciente;
	
	private double plantilla;
	
	private String fecha;
	
	private String hora;
	
	private String fechaModifica;
	
	private String horaModifica;
	
	private String usuarioModifica;
	
	private double valoracionOdonto;
	
	public void clean()
	{
		this.codigoPK=ConstantesBD.codigoNuncaValidoDouble;
		this.ingreso=ConstantesBD.codigoNuncaValido;
		this.numeroSolicitud=ConstantesBD.codigoNuncaValido;;
		this.codigoPaciente=ConstantesBD.codigoNuncaValido;
		this.plantilla=ConstantesBD.codigoNuncaValidoDouble;
		this.fecha="";
		this.hora="";
		this.fechaModifica="";
		this.horaModifica="";
		this.usuarioModifica="";
		this.valoracionOdonto=ConstantesBD.codigoNuncaValidoDouble;
	}
	
	public void DtoPlantillasIngreso()
	{
		clean();
	}

	public double getCodigoPK() {
		return codigoPK;
	}

	public void setCodigoPK(double codigoPK) {
		this.codigoPK = codigoPK;
	}

	public int getIngreso() {
		return ingreso;
	}

	public void setIngreso(int ingreso) {
		this.ingreso = ingreso;
	}

	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	public double getPlantilla() {
		return plantilla;
	}

	public void setPlantilla(double plantilla) {
		this.plantilla = plantilla;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getFechaModifica() {
		return fechaModifica;
	}

	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public String getHoraModifica() {
		return horaModifica;
	}

	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	public double getValoracionOdonto() {
		return valoracionOdonto;
	}

	public void setValoracionOdonto(double valoracionOdonto) {
		this.valoracionOdonto = valoracionOdonto;
	}
	
	
}