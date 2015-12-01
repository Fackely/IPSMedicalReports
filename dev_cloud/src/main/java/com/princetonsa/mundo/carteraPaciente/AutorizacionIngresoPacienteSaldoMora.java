package com.princetonsa.mundo.carteraPaciente;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.carteraPaciente.DtoAutorizacionSaldoMora;
import com.princetonsa.dto.carteraPaciente.DtoCuotasDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoDocsAutorizacionSaldoMora;
import com.princetonsa.dto.carteraPaciente.DtoDocumentosGarantia;
import com.princetonsa.dto.carteraPaciente.DtoMotivosAutSaldoMora;
import com.princetonsa.mundo.PersonaBasica;

public class AutorizacionIngresoPacienteSaldoMora
{
	static Logger logger = Logger.getLogger(AutorizacionIngresoPacienteSaldoMora.class);
	
	public static ArrayList<DtoDocumentosGarantia> consultaDocsPacienteDeudor (int codigoPaciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAutorizacionIngresoPacienteSaldoMoraDao().consultaDocsPacienteDeudor(codigoPaciente);
	}
	
	public static ArrayList<DtoCuotasDatosFinanciacion> consultaCuotas(DtoDocumentosGarantia dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAutorizacionIngresoPacienteSaldoMoraDao().consultaCuotas(dto);
	}
	
	public static ArrayList<DtoMotivosAutSaldoMora> consultaMotivosAutSaldoMora()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAutorizacionIngresoPacienteSaldoMoraDao().consultaMotivosAutSaldoMora();
	}
	
	public static double insertarAutorizacionIngreso(DtoAutorizacionSaldoMora dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAutorizacionIngresoPacienteSaldoMoraDao().insertarAutorizacionIngreso(dto);
	}
	
	public static ArrayList<DtoAutorizacionSaldoMora> existeAutorizacionConViaIngreso(DtoAutorizacionSaldoMora dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAutorizacionIngresoPacienteSaldoMoraDao().existeAutorizacionConViaIngreso(dto);
	}
	
	public static ArrayList<DtoAutorizacionSaldoMora> consultaAutorizaciones(DtoAutorizacionSaldoMora dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAutorizacionIngresoPacienteSaldoMoraDao().consultaAutorizaciones(dto);
	}
	
	public static boolean insertarDocsAutorizacionSaldoM(DtoDocsAutorizacionSaldoMora dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAutorizacionIngresoPacienteSaldoMoraDao().insertarDocsAutorizacionSaldoM(dto);
	}
	
	public static boolean actualizarDatosAutoricacion(DtoAutorizacionSaldoMora dto, PersonaBasica paciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAutorizacionIngresoPacienteSaldoMoraDao().actualizarDatosAutoricacion(dto, paciente);
	}
}