package com.princetonsa.dao.carterapaciente;

import java.util.ArrayList;

import com.princetonsa.dto.carteraPaciente.DtoAutorizacionSaldoMora;
import com.princetonsa.dto.carteraPaciente.DtoCuotasDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoDocsAutorizacionSaldoMora;
import com.princetonsa.dto.carteraPaciente.DtoDocumentosGarantia;
import com.princetonsa.dto.carteraPaciente.DtoMotivosAutSaldoMora;
import com.princetonsa.mundo.PersonaBasica;

public interface AutorizacionIngresoPacienteSaldoMoraDao
{
	public ArrayList<DtoDocumentosGarantia> consultaDocsPacienteDeudor(int codigoPaciente);
	
	public ArrayList<DtoCuotasDatosFinanciacion> consultaCuotas(DtoDocumentosGarantia dto);
	
	public ArrayList<DtoMotivosAutSaldoMora> consultaMotivosAutSaldoMora();
	
	public double insertarAutorizacionIngreso(DtoAutorizacionSaldoMora dto);
	
	public ArrayList<DtoAutorizacionSaldoMora> existeAutorizacionConViaIngreso(DtoAutorizacionSaldoMora dto);
	
	public ArrayList<DtoAutorizacionSaldoMora> consultaAutorizaciones(DtoAutorizacionSaldoMora dto);
	
	public boolean insertarDocsAutorizacionSaldoM(DtoDocsAutorizacionSaldoMora dto);

	public boolean actualizarDatosAutoricacion(DtoAutorizacionSaldoMora dto, PersonaBasica paciente);
}