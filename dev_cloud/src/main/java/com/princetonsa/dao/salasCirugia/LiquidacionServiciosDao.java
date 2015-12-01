/*
 * Feb 06, 2008
 */
package com.princetonsa.dao.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;

import util.InfoDatosInt;

/**
 * @author Sebasti�n G�mez 
 *
 * Interface utilizada para gestionar los m�todos DAO de la funcionalidad
 * Liquidacion de Servicios
 */
public interface LiquidacionServiciosDao 
{
	
	
	/**
	 * M�todo que realiza la consulta las ordenes de cirugia para liquidar
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> consultarListadoOrdenes(Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para cargar los datos del acto quirurgico
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoInstitucion 
	 * @return
	 */
	public HashMap<String, Object> cargarDatosActoQuirurgico(Connection con,String numeroSolicitud, int codigoInstitucion);
	
	/**
	 * M�todo que realiza la consulta de otros profesionales del acto quir�rgico
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap<String, Object> consultarOtrosProfesionales(Connection con,String numeroSolicitud);
	
	/**
	 * M�todo que consulta los profesionales de la cirug�a
	 * @param con
	 * @param codigoCirugia
	 * @return
	 */
	public HashMap<String, Object> consultarProfesionalesCirugia(Connection con,String codigoCirugia);
	
	/**
	 * M�todo para obtener la parametrizaci�n de los valores de los asocios x grupos
	 */
	public HashMap<String, Object> obtenerValorAsociosXGrupo(Connection con,HashMap campos);
	
	/**
	 * M�todo que realiza la consulta de la excepcion Qx de un asocio
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> obtenerExcepcionQxAsocio(Connection con,HashMap campos);
	
	/**
	 * M�todo que retorna el servicio de un asocio
	 * @param con
	 * @param campos
	 * @return
	 */
	public int obtenerServicioAsocio(Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para consultar la excepcion tarifa quirurgica de un asocio
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> obtenerExcepcionTarifaQxAsocio(Connection con,HashMap campos);
	
	/**
	 * M�todo para realizar la consulta una excepcion tipo sala del asocio
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> obtenerExcepcionXTipoSala(Connection con,HashMap campos);
	
	/**
	 * M�todo para consultar los porcentajes de cx multi de un asocio
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> obtenerPorcentajeCirugiaMultiple(Connection con,HashMap campos);
	
	/**
	 * M�todo que consulta los valores de los asocios por uvr
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> obtenerValorAsociosXUvr(Connection con,HashMap campos);
	
	/**
	 * M�todo que realiza la consulta de los asocios por rango de tiempo
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> obtenerValorAsociosXRangoTiempo(Connection con,HashMap campos);
	
	/**
	 * M�todo que realiza la consulta de lso asocios del servicio x tarifa
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> obtenerAsociosServicioXTarifa(Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para obtener el listado de articulos del consumo de materiales
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap<String, Object> obtenerListaArticulosConsumoMateriales(Connection con,String numeroSolicitud);
	
	/**
	 * M�todo que realiza la actualizaci�n de incluido y valor unitario de un articulo del consumo de materiales
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @param incluido
	 * @param valorUnitario
	 * @return
	 */
	public int actualizarInclusionYTarifaArticulo(Connection con,String numeroSolicitud,int codigoArticulo,String incluido,double valorUnitario);
	
	/**
	 * M�todo para obtener el total del consumo de materiales
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoServicio
	 * @param esTarifa
	 * @return
	 */
	public double obtenerTotalConsumoMateriales(Connection con,String numeroSolicitud,int codigoServicio,boolean esTarifa);
	
	/**
	 * M�todo implementado para verificar que existe un consumo de materiales
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoServicio
	 * @return
	 */
	public boolean existeConsumoMateriales(Connection con,String numeroSolicitud,int codigoServicio);
	
	/**
	 * M�todo implementado para obtener el codigo propietario de un asocio
	 * @param con
	 * @param codigoParametrizacion
	 * @param tarifarioOficial
	 * @param modoLiquidacion
	 * @return
	 */
	public String obtenerCodigoPropietarioAsocio(Connection con,String codigoParametrizacion,int tarifarioOficial,int modoLiquidacion);
	
	/**
	 * M�todo implementado para insertar un asocio
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarAsocio(Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para consultar los materiales especiales
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap<String, Object> consultarMaterialesEspeciales(Connection con,String numeroSolicitud);
	
	/**
	 * M�todo implementado
	 * @param con
	 * @return
	 */
	public InfoDatosInt obtenerAsocioProcedimientos(Connection con,int institucion);
	
	/**
	 * M�todo implementado para insertar el log bd de la liquidaci�n de servicios
	 * @param con
	 * @param numeroSolicitud
	 * @param loginUsuario
	 * @return
	 */
	public int insertarLiquidacionServicios(Connection con,String numeroSolicitud,String loginUsuario);
	
	/**
	 * M�todo para consultar los asocios liquidados de una orden
	 * @param con
	 * @param consecutivoServicio
	 * @return
	 */
	public HashMap<String, Object> consultarAsociosLiquidados(Connection con,String consecutivoServicio);
	
	/**
	 * M�todo para actualizar el indicativo de consumo de materiales en la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param indicador
	 * @return
	 */
	public int actualizarIndicadorConsumoMaterialesSolicitud(Connection con,String numeroSolicitud,boolean indicador);
}
