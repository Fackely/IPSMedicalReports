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
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public class PostgresqlPresupuestoPacienteDao implements PresupuestoPacienteDao{
	

	/**
	 * Método que consulta los convenios que están activos y tiene un contrato vigente a la fecha actual
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
	 * Mètodo que inserta la informaciòn básica del presupuesto del paciente
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
	 * Método para cargar la información básica del presupuesto
	 * @param con
	 * @param consecutivoPresupuesto
	 * @return Collection con la informaciòn del presupuesto
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
	 * Método para insertar los servicios de intervención del presupuesto
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
	 * Método que consulta los presupuesto de un paciente para unos convenios
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
	 * Método que actualiza el ingreso de un prespuesto
	 * @param con
	 * @param campos
	 * @return
	 */
	public int actualizarIngreso(Connection con,HashMap campos)
	{
		return SqlBasePresupuestoPacienteDao.actualizarIngreso(con, campos);
	}
	
	/**
	 * Método implementado para obtener el presupuesto de un ingreso
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
