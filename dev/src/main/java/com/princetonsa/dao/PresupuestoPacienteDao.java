/*
 * Creado en Dec 15, 2005
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public interface PresupuestoPacienteDao 
{

	/**
	 * Método que consulta los convenios que están activos y tiene un contrato vigente a la fecha actual
	 * @param con
	 * @return Collection con los convenios vigentes
	 */
	public Collection consultarConveniosVigentes (Connection con, boolean cargarContrato);

	

	/**
	 * @param con
	 * @param consecutivoCotizacion
	 * @param codigoArticulo
	 * @param cantidad
	 * @param valor
	 * @param esquemaTarifario 
	 * @return
	 */
	int insertarMateriales(Connection con, int consecutivoCotizacion, int codigoArticulo, int cantidad, float valor, int esquemaTarifario);
	
	/**
	 * Mètodo que inserta la informaciòn básica del presupuesto del paciente
	 * @param con
	 * @param consecutivoPresupuesto
	 * @param codigoPersona
	 * @param convenio
	 * @param esquemaTarifarioServicio
	 * @param esquemaTarifarioInventario
	 * @param medicoTratante
	 * @param diagnosticoIntervencion
	 * @param cieDiagnostico
	 * @param loginUsuario
	 * @param centroAtencion
	 * @return consecutivoPresupuesto
	 */	
	public int insertarPresupuesto (Connection con, int consecutivoPresupuesto, int codigoPersona, int convenio,  
																int medicoTratante, String diagnosticoIntervencion, int cieDiagnostico, String loginUsuario, int centroAtencion, int contrato, String paquete);
	
	/**
	 * Método para cargar la información básica del presupuesto
	 * @param con
	 * @param consecutivoPresupuesto
	 * @return Collection con la informaciòn del presupuesto
	 */
	public Collection cargarInfoPresupuesto (Connection con, int consecutivoPresupuesto);



	/**
	 * Método para insertar los servicios del presupuesto
	 * @param con
	 * @param consecutivoCotizacion
	 * @param codigoServicio
	 * @param cantidad
	 * @param valor
	 * @param esquemaTarifario 
	 * @return
	 */
	public int insertarServicios(Connection con, int consecutivoCotizacion, int codigoServicio, int cantidad, float valor, int esquemaTarifario);
	
	/**
	 * Método para insertar los servicios de intervención del presupuesto
	 * @param con
	 * @param consecutivoCotizacion
	 * @param codigoServicio
	 * @return
	 */
	public int insertarServiciosIntervencion(Connection con, int consecutivoCotizacion, int codigoServicio);
	
	/**
	 * 
	 * @param con
	 * @param codigoPaquete
	 * @param contrato 
	 * @param convenio 
	 * @return
	 */
	public HashMap cargarServiciosPaquetes(Connection con, String codigoPaquete, int convenio, int contrato);


	/**
	 * 
	 * @param con
	 * @param codigoPaquete
	 * @return
	 */
	public HashMap cargarArticulosPaquetes(Connection con, String codigoPaquete);



	/**
	 * 
	 * @param con
	 * @param convenio 
	 * @param servicios
	 * @return
	 */
	public HashMap obtenerPaquetesValidos(Connection con, int convenio, String servicios);
	
	/**
	 * Método que consulta los presupuesto de un paciente para unos convenios
	 * determinados cuando no tengan ingreso definido
	 * @param con
	 * @param campos
	 * @return
	 */
	public  HashMap obtenerPrespuestosSinIngreso(Connection con,HashMap campos);
	
	/**
	 * Método que actualiza el ingreso de un prespuesto
	 * @param con
	 * @param campos
	 * @return
	 */
	public int actualizarIngreso(Connection con,HashMap campos);
	
	/**
	 * Método implementado para obtener el presupuesto de un ingreso
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap obtenerPresupuestoXIngreso(Connection con,HashMap campos);
	
	/**
	 * Metodo para la consulta de los contratos por convenio
	 * @param con
	 * @param convenio
	 * @return
	 */
	public HashMap consultarContratos(Connection con,int convenio);

}
