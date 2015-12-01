package com.princetonsa.mundo.odontologia;

import java.sql.Connection;
import java.util.ArrayList;

import util.ResultadoBoolean;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.odontologia.AperturaCuentaPacienteOdontologicoDao;
import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.manejoPaciente.DtoCuentas;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

public class AperturaCuentaPacienteOdontologico
{
	/**
	 * Método para retornar la aprtura de cuenta paciente DAO
	 * @return
	 */
	public static AperturaCuentaPacienteOdontologicoDao aperturaCuentaPacienteOdoDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAperturaCuentaPacienteOdontologicoDao();
	}
	
	public static ArrayList<DtoConvenio> consultaConvenios()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAperturaCuentaPacienteOdontologicoDao().consultaConvenios();
	}
	public static ArrayList<DtoConvenio> consultaConveniosTarjetaPaciente(Connection con, DtoPaciente codigoPaciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAperturaCuentaPacienteOdontologicoDao().consultaConveniosTarjetaPaciente(con, codigoPaciente);
	}
	public static boolean guardarCuentas(Connection con, ArrayList<DtoSubCuentas> listaCuentas, UsuarioBasico usuario, PersonaBasica paciente, int codigoArea)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAperturaCuentaPacienteOdontologicoDao().guardarCuentas(con, listaCuentas, usuario, paciente, codigoArea);
	}
	
	/**
	 * Método para cargar datos de la cuenta del ingreso abierto del paciente
	 * @param con
	 * @param paciente
	 * @return
	 */
	public static DtoCuentas cargarCuentaIngresoAbiertoPaciente(Connection con,DtoPaciente paciente)
	{
		return aperturaCuentaPacienteOdoDao().cargarCuentaIngresoAbiertoPaciente(con, paciente); 
	}
	
	/**
	 * Método usado para cargar las subcuentas existentes del ingreso del paciente
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static ArrayList<DtoSubCuentas> cargarSubcuentas(Connection con,DtoCuentas cuenta)
	{
		return aperturaCuentaPacienteOdoDao().cargarSubcuentas(con, cuenta);
	}
	
	/**
	 * Método implementado para realizar la modificación de la cuenta
	 * @param con
	 * @param listaCuentas
	 * @param usuario
	 * @param paciente
	 * @param dtoCuenta
	 * @return
	 */
	public static ResultadoBoolean modificarCuenta(Connection con,ArrayList<DtoSubCuentas> listaCuentas, UsuarioBasico usuario, PersonaBasica paciente, DtoCuentas dtoCuenta)
	{
		return aperturaCuentaPacienteOdoDao().modificarCuenta(con, listaCuentas, usuario, paciente, dtoCuenta);
	}
}