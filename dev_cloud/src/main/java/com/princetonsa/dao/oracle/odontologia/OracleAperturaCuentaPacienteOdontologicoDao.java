package com.princetonsa.dao.oracle.odontologia;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;

import util.ResultadoBoolean;

import com.princetonsa.dao.odontologia.AperturaCuentaPacienteOdontologicoDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseAperturaCuentaPacienteOdontologicoDao;
import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.manejoPaciente.DtoCuentas;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

public class OracleAperturaCuentaPacienteOdontologicoDao implements AperturaCuentaPacienteOdontologicoDao, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ArrayList<DtoConvenio> consultaConvenios()
	{
		return SqlBaseAperturaCuentaPacienteOdontologicoDao.consultaConvenios();
	}
	
	@Override
	public ArrayList<DtoConvenio> consultaConveniosTarjetaPaciente(Connection con, DtoPaciente paciente) {
		return SqlBaseAperturaCuentaPacienteOdontologicoDao.consultaConveniosTarjetaPaciente(con, paciente);
	}

	@Override
	public boolean guardarCuentas(Connection con, ArrayList<DtoSubCuentas> listaCuentas, UsuarioBasico usuario, PersonaBasica paciente, int codigoArea) {
		return SqlBaseAperturaCuentaPacienteOdontologicoDao.guardarCuentas(con, listaCuentas, usuario, paciente, codigoArea);
	}
	
	/**
	 * Método para cargar datos de la cuenta del ingreso abierto del paciente
	 * @param con
	 * @param paciente
	 * @return
	 */
	@Override
	public DtoCuentas cargarCuentaIngresoAbiertoPaciente(Connection con,DtoPaciente paciente)
	{
		return SqlBaseAperturaCuentaPacienteOdontologicoDao.cargarCuentaIngresoAbiertoPaciente(con, paciente);
	}
	
	/**
	 * Método usado para cargar las subcuentas existentes del ingreso del paciente
	 * @param con
	 * @param cuenta
	 * @return
	 */
	@Override
	public ArrayList<DtoSubCuentas> cargarSubcuentas(Connection con,DtoCuentas cuenta)
	{
		return SqlBaseAperturaCuentaPacienteOdontologicoDao.cargarSubcuentas(con, cuenta);
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
	@Override
	public ResultadoBoolean modificarCuenta(Connection con,ArrayList<DtoSubCuentas> listaCuentas, UsuarioBasico usuario, PersonaBasica paciente, DtoCuentas dtoCuenta)
	{
		return SqlBaseAperturaCuentaPacienteOdontologicoDao.modificarCuenta(con, listaCuentas, usuario, paciente, dtoCuenta);
	}

}