package com.princetonsa.dao.odontologia;

import java.sql.Connection;
import java.util.ArrayList;

import util.ResultadoBoolean;

import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.manejoPaciente.DtoCuentas;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

public interface AperturaCuentaPacienteOdontologicoDao
{
	public ArrayList<DtoConvenio> consultaConvenios();

	public ArrayList<DtoConvenio> consultaConveniosTarjetaPaciente(Connection con,	DtoPaciente paciente);

	public boolean guardarCuentas(Connection con, ArrayList<DtoSubCuentas> listaCuentas, UsuarioBasico usuario, PersonaBasica paciente, int codigoArea);
	
	/**
	 * M�todo para cargar datos de la cuenta del ingreso abierto del paciente
	 * @param con
	 * @param paciente
	 * @return
	 */
	public DtoCuentas cargarCuentaIngresoAbiertoPaciente(Connection con,DtoPaciente paciente);
	
	/**
	 * M�todo usado para cargar las subcuentas existentes del ingreso del paciente
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public ArrayList<DtoSubCuentas> cargarSubcuentas(Connection con,DtoCuentas cuenta);
	
	/**
	 * M�todo implementado para realizar la modificaci�n de la cuenta
	 * @param con
	 * @param listaCuentas
	 * @param usuario
	 * @param paciente
	 * @param dtoCuenta
	 * @return
	 */
	public ResultadoBoolean modificarCuenta(Connection con,ArrayList<DtoSubCuentas> listaCuentas, UsuarioBasico usuario, PersonaBasica paciente, DtoCuentas dtoCuenta);
}