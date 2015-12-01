package com.mercury.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;
import util.UtilidadFecha;

public class DtoCartaDental implements Serializable
{
	private int codigoPk;
	
	private int codPkTratamientoOdo;
	
	private int codMedicoRegistra;
	
	private String nombreMedicoRegistra;
	
	private String fechaRegistra;	
	
	private String horaRegistra;
	
	private String usuarioModica;
	
	private String otrosHallazgos;
	
	private ArrayList<DtoDienteCartaDental> arrayDtoDienteCartaDental;
	
	public DtoCartaDental()
	{
		this.clean();
		this.inicializarDientesPermanentes();
	}
	
	public void clean()
	{
		this.codigoPk = ConstantesBD.codigoNuncaValido;
		this.codPkTratamientoOdo = ConstantesBD.codigoNuncaValido;
		this.codMedicoRegistra = ConstantesBD.codigoNuncaValido;
		this.nombreMedicoRegistra = "";
		this.fechaRegistra = UtilidadFecha.getFechaActual();
		this.horaRegistra = UtilidadFecha.getHoraActual();
		this.usuarioModica = "";		
		this.otrosHallazgos = "";	
		this.arrayDtoDienteCartaDental = new ArrayList<DtoDienteCartaDental>();
	}
	
	/**
	 * Inicializa los dientes de la carta dental 
	 * */
	public void inicializarDientesPermanentes()
	{
		this.arrayDtoDienteCartaDental = new ArrayList<DtoDienteCartaDental>();
		
		//Primer cuadrante permanentes
		for(int i = 11; i < 19; i++)
			this.arrayDtoDienteCartaDental.add(new DtoDienteCartaDental(i,true));
		
		//Segundo cuadrante permanentes
		for(int i = 21; i < 29; i++)
			this.arrayDtoDienteCartaDental.add(new DtoDienteCartaDental(i,true));
		
		//tercer cuadrante permanentes
		for(int i = 31; i < 39; i++)
			this.arrayDtoDienteCartaDental.add(new DtoDienteCartaDental(i,true));
		
		//cuarto cuadrante permanente
		for(int i = 41; i < 49; i++)
			this.arrayDtoDienteCartaDental.add(new DtoDienteCartaDental(i,true));
	}
	
	public int getPosDiente(int diente)
	{
		for(int i=0; i<this.arrayDtoDienteCartaDental.size(); i++)
		{
			if(this.arrayDtoDienteCartaDental.get(i).getNumeroDiente() == diente)
				return i;
		}
		
		return ConstantesBD.codigoNuncaValido;		
	}
	
	public int getPosDiente(int dientePermanente,int dienteTemporal)
	{		
		for(int i=0; i<this.arrayDtoDienteCartaDental.size(); i++)
		{
			if(this.arrayDtoDienteCartaDental.get(i).getNumeroDiente() == dientePermanente 
					|| this.arrayDtoDienteCartaDental.get(i).getNumeroDiente() == dienteTemporal)
				return i;
		}
		
		return ConstantesBD.codigoNuncaValido;		
	}

	public int getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public int getCodPkTratamientoOdo() {
		return codPkTratamientoOdo;
	}

	public void setCodPkTratamientoOdo(int codPkTratamientoOdo) {
		this.codPkTratamientoOdo = codPkTratamientoOdo;
	}

	public int getCodMedicoRegistra() {
		return codMedicoRegistra;
	}

	public void setCodMedicoRegistra(int codMedicoRegistra) {
		this.codMedicoRegistra = codMedicoRegistra;
	}

	public String getNombreMedicoRegistra() {
		return nombreMedicoRegistra;
	}

	public void setNombreMedicoRegistra(String nombreMedicoRegistra) {
		this.nombreMedicoRegistra = nombreMedicoRegistra;
	}

	public String getFechaRegistra() {
		return fechaRegistra;
	}

	public void setFechaRegistra(String fechaRegistra) {
		this.fechaRegistra = fechaRegistra;
	}

	public String getHoraRegistra() {
		return horaRegistra;
	}

	public void setHoraRegistra(String horaRegistra) {
		this.horaRegistra = horaRegistra;
	}

	public String getUsuarioModica() {
		return usuarioModica;
	}

	public void setUsuarioModica(String usuarioModica) {
		this.usuarioModica = usuarioModica;
	}

	public String getOtrosHallazgos() {
		return otrosHallazgos;
	}

	public void setOtrosHallazgos(String otrosHallazgos) {
		this.otrosHallazgos = otrosHallazgos;
	}

	public ArrayList<DtoDienteCartaDental> getArrayDtoDienteCartaDental() {
		return arrayDtoDienteCartaDental;
	}
	
	public DtoDienteCartaDental getArrayDtoDienteCartaDental(int pos) {
		return arrayDtoDienteCartaDental.get(pos);
	}
	
	public void setArrayDtoDienteCartaDental(
			DtoDienteCartaDental dto,int pos) {
		this.arrayDtoDienteCartaDental.set(pos,dto);
	}

	public void setArrayDtoDienteCartaDental(
			ArrayList<DtoDienteCartaDental> arrayDtoDienteCartaDental) {
		this.arrayDtoDienteCartaDental = arrayDtoDienteCartaDental;
	}	
}