/*
 * Creado en Dec 15, 2005
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.dao.PresupuestoPacienteDao;
import com.princetonsa.dao.sqlbase.SqlBasePresupuestoPacienteDao;

/**
 * @author Andr�s Mauricio Ruiz V�lez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public class PostgresqlPresupuestoPacienteDao implements PresupuestoPacienteDao{
	

	/**
	 * M�todo que consulta los convenios que est�n activos y tiene un contrato vigente a la fecha actual
	 * @param con
	 * @return Collection con los convenios vigentes
	 */
	public Collection consultarConveniosVigentes (Connection con, boolean cargarContrato)
	{
		return SqlBasePresupuestoPacienteDao.consultarConveniosVigentes(con, cargarContrato);
	}


	/**
	 * @param con
	 * @param consecutivoCotizacion
	 * @param codigoArticulo
	 * @param cantidad
	 * @param valor
	 * @return
	 */
	public int insertarMateriales(Connection con, int consecutivoCotizacion, int codigoArticulo, int cantidad, float valor,int esquemaTarifario)
	{
		return SqlBasePresupuestoPacienteDao.insertarMateriales(con, consecutivoCotizacion, codigoArticulo, cantidad, valor,esquemaTarifario);
	}
	
	/**
	 * M�todo que inserta la informaci�n b�sica del presupuesto del paciente
	 * @param con
	 * @param consecutivoPresupuesto
	 * @param codigoPersona
	 * @param convenio
	 * @param medicoTratante
	 * @param diagnosticoIntervencion
	 * @param cieDiagnostico
	 * @param loginUsuario
	 * @param centroAtencion
	 * @return consecutivoPresupuesto
	 */	
	public int insertarPresupuesto (Connection con, int consecutivoPresupuesto, int codigoPersona, int convenio,  
																int medicoTratante, String diagnosticoIntervencion, int cieDiagnostico, String loginUsuario, int centroAtencion, int contrato, String paquete)
	{
		return SqlBasePresupuestoPacienteDao.insertarPresupuesto (con, consecutivoPresupuesto, codigoPersona, convenio,  
																											medicoTratante, diagnosticoIntervencion, cieDiagnostico, loginUsuario, centroAtencion, contrato, paquete);
	}
	
	/**
	 * M�todo para cargar la informaci�n b�sica del presupuesto
	 * @param con
	 * @param consecutivoPresupuesto
	 * @return Collection con la informaci�n del presupuesto
	 */
	public Collection cargarInfoPresupuesto (Connection con, int consecutivoPresupuesto)
	{
		return SqlBasePresupuestoPacienteDao.cargarInfoPresupuesto (con, consecutivoPresupuesto);
	}

	
	/**
	 * Metodo para insertar los materiales del presupuesto 
	 * @param con
	 * @param consecutivoCotizacion
	 * @param codigoArticulo
	 * @param cantidad
	 * @param valor
	 * @return
	 */
	public int insertarServicios(Connection con, int consecutivoPresupuesto, int codigoServicio, int cantidad, float valor,int esquemaTarifario)
	{
		return SqlBasePresupuestoPacienteDao.insertarServicios(con, consecutivoPresupuesto, codigoServicio, cantidad, valor,esquemaTarifario);
	}
	
	/**
	 * M�todo para insertar los servicios de intervenci�n del presupuesto
	 * @param con
	 * @param consecutivoCotizacion
	 * @param codigoServicio
	 * @return
	 */
	public int insertarServiciosIntervencion(Connection con, int consecutivoCotizacion, int codigoServicio)
	{
		return SqlBasePresupuestoPacienteDao.insertarServiciosIntervencion (con, consecutivoCotizacion, codigoServicio);
	}

	/**
	 * 
	 */
	public HashMap cargarServiciosPaquetes(Connection con, String codigoPaquete, int convenio, int contrato) 
	{
		return SqlBasePresupuestoPacienteDao.cargarServiciosPaquetes(con,codigoPaquete,convenio,contrato);
	}

	/**
	 * 
	 */
	public HashMap cargarArticulosPaquetes(Connection con, String codigoPaquete) 
	{
		return SqlBasePresupuestoPacienteDao.cargarArticulosPaquetes(con,codigoPaquete);
	}


	/**
	 * 
	 */
	public HashMap obtenerPaquetesValidos(Connection con,int convenio, String servicios) 
	{
		return SqlBasePresupuestoPacienteDao.obtenerPaquetesValidos(con,convenio,servicios);
	}
	
	
	/**
	 * M�todo que consulta los presupuesto de un paciente para unos convenios
	 * determinados cuando no tengan ingreso definido
	 * @param con
	 * @param campos
	 * @return
	 */
	public  HashMap obtenerPrespuestosSinIngreso(Connection con,HashMap campos)
	{
		return SqlBasePresupuestoPacienteDao.obtenerPrespuestosSinIngreso(con, campos);
	}
	
	/**
	 * M�todo que actualiza el ingreso de un prespuesto
	 * @param con
	 * @param campos
	 * @return
	 */
	public int actualizarIngreso(Connection con,HashMap campos)
	{
		return SqlBasePresupuestoPacienteDao.actualizarIngreso(con, campos);
	}
	
	/**
	 * M�todo implementado para obtener el presupuesto de un ingreso
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap obtenerPresupuestoXIngreso(Connection con,HashMap campos)
	{
		return SqlBasePresupuestoPacienteDao.obtenerPresupuestoXIngreso(con, campos);
	}
	
	/**
	 * Metodo para la consulta de los contratos por convenio
	 */
	public HashMap consultarContratos(Connection con,int convenio)
	{
		return SqlBasePresupuestoPacienteDao.consultarContratos(con, convenio);
	}
	
}
