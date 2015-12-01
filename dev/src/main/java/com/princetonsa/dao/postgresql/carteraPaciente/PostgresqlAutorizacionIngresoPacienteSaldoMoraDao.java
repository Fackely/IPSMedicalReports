package com.princetonsa.dao.postgresql.carteraPaciente;

import java.util.ArrayList;

import com.princetonsa.dao.carterapaciente.AutorizacionIngresoPacienteSaldoMoraDao;
import com.princetonsa.dao.sqlbase.carteraPaciente.SqlBaseAutorizacionIngresoPacienteSaldoMoraDao;
import com.princetonsa.dto.carteraPaciente.DtoAutorizacionSaldoMora;
import com.princetonsa.dto.carteraPaciente.DtoCuotasDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoDocsAutorizacionSaldoMora;
import com.princetonsa.dto.carteraPaciente.DtoDocumentosGarantia;
import com.princetonsa.dto.carteraPaciente.DtoMotivosAutSaldoMora;
import com.princetonsa.mundo.PersonaBasica;

public class PostgresqlAutorizacionIngresoPacienteSaldoMoraDao implements AutorizacionIngresoPacienteSaldoMoraDao
{
	public ArrayList<DtoDocumentosGarantia> consultaDocsPacienteDeudor(int codigoPaciente)
	{
		return SqlBaseAutorizacionIngresoPacienteSaldoMoraDao.consultaDocsPacienteDeudor(codigoPaciente);
	}
	
	public ArrayList<DtoCuotasDatosFinanciacion> consultaCuotas(DtoDocumentosGarantia dto)
	{
		return SqlBaseAutorizacionIngresoPacienteSaldoMoraDao.consultaCuotas(dto);
	}
	
	public ArrayList<DtoMotivosAutSaldoMora> consultaMotivosAutSaldoMora()
	{
		return SqlBaseAutorizacionIngresoPacienteSaldoMoraDao.consultaMotivosAutSaldoMora();
	}
	
	public double insertarAutorizacionIngreso(DtoAutorizacionSaldoMora dto)
	{
		return SqlBaseAutorizacionIngresoPacienteSaldoMoraDao.insertarAutorizacionIngreso(dto);
	}
	
	public ArrayList<DtoAutorizacionSaldoMora> existeAutorizacionConViaIngreso(DtoAutorizacionSaldoMora dto)
	{
		return SqlBaseAutorizacionIngresoPacienteSaldoMoraDao.existeAutorizacionConViaIngreso(dto);
	}
	
	public ArrayList<DtoAutorizacionSaldoMora> consultaAutorizaciones(DtoAutorizacionSaldoMora dto)
	{
		return SqlBaseAutorizacionIngresoPacienteSaldoMoraDao.consultaAutorizaciones(dto);
	}
	
	public boolean insertarDocsAutorizacionSaldoM(DtoDocsAutorizacionSaldoMora dto)
	{
		return SqlBaseAutorizacionIngresoPacienteSaldoMoraDao.insertarDocsAutorizacionSaldoM(dto);
	}

	@Override
	public boolean actualizarDatosAutoricacion(DtoAutorizacionSaldoMora dto, PersonaBasica paciente) {
		return SqlBaseAutorizacionIngresoPacienteSaldoMoraDao.actualizarDatosAutoricacion(dto, paciente);
	}

}