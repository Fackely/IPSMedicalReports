package com.mercury.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;

public class DtoDienteCartaDental implements Serializable
{
	private int numeroDiente;
	
	private int numeroDienteAnterior;
	
	private ArrayList<DtoDiagCartaDental> arrayDtoDiagCartaDental;
	
	private ArrayList<DtoTratamientoCartaDental> arrayDtoTratamientoCartaDental;
	
	private boolean permanente;
	
	private boolean permanenteAnterior;
	
	private boolean tieneInformacion;	
	
	public void clean ()
	{
		this.numeroDiente = ConstantesBD.codigoNuncaValido;
		this.numeroDienteAnterior = ConstantesBD.codigoNuncaValido;
		this.tieneInformacion = false;
		this.permanente = false;
		this.permanenteAnterior = false;
		this.arrayDtoDiagCartaDental = new ArrayList<DtoDiagCartaDental>();
		this.arrayDtoTratamientoCartaDental = new ArrayList<DtoTratamientoCartaDental>();
	}	
	
	public DtoDienteCartaDental()
	{
		this.clean();
	}
	
	public DtoDienteCartaDental(int numeroDiente,boolean permanente)
	{
		this.clean();
		this.numeroDiente = numeroDiente;
		this.permanente = permanente;
		this.numeroDienteAnterior = numeroDiente;
		this.permanenteAnterior = permanente;
	}

	public int getNumeroDiente() {
		return numeroDiente;
	}

	public void setNumeroDiente(int numeroDiente) {
		this.numeroDiente = numeroDiente;
	}

	public ArrayList<DtoDiagCartaDental> getArrayDtoDiagCartaDental() {
		return arrayDtoDiagCartaDental;
	}

	public void setArrayDtoDiagCartaDental(
			ArrayList<DtoDiagCartaDental> arrayDtoDiagCartaDental) {
		this.arrayDtoDiagCartaDental = arrayDtoDiagCartaDental;
	}
	
	public DtoDiagCartaDental getArrayDtoDiagCartaDentalPos(int pos) {
		return arrayDtoDiagCartaDental.get(pos);
	}

	public void setArrayDtoDiagCartaDental(
			DtoDiagCartaDental dto,int pos) {
		this.arrayDtoDiagCartaDental.set(pos,dto);
	}

	public ArrayList<DtoTratamientoCartaDental> getArrayDtoTratamientoCartaDental() {
		return arrayDtoTratamientoCartaDental;
	}

	public void setArrayDtoTratamientoCartaDental(
			ArrayList<DtoTratamientoCartaDental> arrayDtoTratamientoCartaDental) {
		this.arrayDtoTratamientoCartaDental = arrayDtoTratamientoCartaDental;
	}
	
	public DtoTratamientoCartaDental getArrayDtoTratamientoCartaDentalPos(int pos) {
		return arrayDtoTratamientoCartaDental.get(pos);
	}

	public void setArrayDtoTratamientoCartaDental(
			DtoTratamientoCartaDental dto, int pos) {
		this.arrayDtoTratamientoCartaDental.set(pos,dto);
	}

	public boolean isTieneInformacion() {
		return tieneInformacion;
	}

	public void setTieneInformacion(boolean tieneInformacion) {
		this.tieneInformacion = tieneInformacion;
	}

	public boolean isPermanente() {
		return permanente;
	}

	public void setPermanente(boolean permanente) {
		this.permanente = permanente;
	}

	public int getNumeroDienteAnterior() {
		return numeroDienteAnterior;
	}

	public void setNumeroDienteAnterior(int numeroDienteAnterior) {
		this.numeroDienteAnterior = numeroDienteAnterior;
	}

	public boolean isPermanenteAnterior() {
		return permanenteAnterior;
	}

	public void setPermanenteAnterior(boolean permanenteAnterior) {
		this.permanenteAnterior = permanenteAnterior;
	}
}