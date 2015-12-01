/*
 * Oct 4, 2005
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.struts.action.ActionErrors;

/**
 * @author Sebasti�n G�mez R
 *
 * Interface utilizada para gestionar los m�todos DAO de la funcionalidad
 * Materiales Qx
 */
public interface MaterialesQxDao 
{

	
	/**
	 * M�todo usado para consultar las ordenes de cirugia de una cuenta 
	 * que tengan estado de facturacion 'Pendiente' y estado de historia cl�nica 'Interpretada'
	 * @param con
	 * @param cuenta
	 * @param funcionalidad
	 * @return
	 */
	public HashMap cargarOrdenesCirugia(Connection con,int cuenta,String funcionalidad);
	
	/**
	 * M�todo usado para consultar los datos generales de un
	 * registro de materiales Qx.
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap cargarDatosGenerales(Connection con,int numeroSolicitud);
	
	/**
	 * M�todo para consultar las cirug�as de un acto quir�gico que har�n
	 * parte del registro de los materiales Qx.
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap cargarCirugiasPorActo(Connection con,int numeroSolicitud);
	
	/**
	 * M�todo implementado para cargar los art�culos para Materiales Qx. buscando as�:
	 * 1) Se buscan primero si existen articulos ya ingresados en Materiales Qx.
	 * 2) Si no hay, se buscan art�culos en el pedido de la orden.
	 * 3) Si no hay, se buscan art�culos en la hoja de gastos
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap cargarArticulosMaterialesQx(Connection con,int numeroSolicitud);
	
	/**
	 * M�todo que inserta la informaci�n del encabezado del ingreso de consumo
	 * de materiales Qx.
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param usuario
	 * @param fecha
	 * @param hora
	 * @param esActo
	 * @param finalizado
	 * @param entidadSubcontratada 
	 * @return
	 */
	public int insertarEncabezadoMaterialesQx(Connection con,
			int numeroSolicitud,
			int centroCosto,
			String usuario,
			String fecha,
			String hora,
			boolean esActo,
			String finalizado,
			String estado,
			int farmacia, String entidadSubcontratada);
	
	
	
	/**
	 * M�todo que modifica la informaci�n del encabezado de materiales Qx.
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param usuario
	 * @param fecha
	 * @param hora
	 * @param esActo
	 * @param finalizado
	 * @param estado
	 * @param entidadSubcontratada 
	 * @return
	 */
	public int modificarEncabezadoMaterialesQx(Connection con,
			int numeroSolicitud,
			int centroCosto,
			String usuario,
			String fecha,
			String hora,
			boolean esActo,
			String finalizado,
			String estado,
			int farmacia, String entidadSubcontratada);
	
	/**
	 * M�todo que carga los art�culos por cirugia de un registro de materiales Qx.
	 * @param con
	 * @param numeroSolicitud
	 * @param numeroCirugias
	 * @return
	 */
	public HashMap cargarArticulosPorCirugia(Connection con,int numeroSolicitud,int numeroCirugias);
	
	/**
	 * M�todo que realiza uan busqueda avanzada de ordenes de cirugia
	 * que ya est�n cargadas
	 * @param con
	 * @param ordenInicial
	 * @param ordenFinal
	 * @param fechaOrdenInicial
	 * @param fechaOrdenFinal
	 * @param fechaCxInicial
	 * @param fechaCxFinal
	 * @param medico
	 * @param centroAtencion
	 * @return
	 */
	public HashMap cargarOrdenesCirugia(Connection con,
			String ordenInicial,String ordenFinal,String fechaOrdenInicial,
			String fechaOrdenFinal,String fechaCxInicial,String fechaCxFinal,int medico,int centroAtencion);
	
	/**
	 * M�todo que consulta la cantidad de los articulos despachados de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap consultarCantidadesArticulosDespacho(Connection con,String numeroSolicitud);
	
	/**
	 * M�todo para consultar el pedido pendiente de una peticion
	 */
	public HashMap consultarPedidoPendientePeticion(Connection con,HashMap campos);
	
	/**
	 * M�todo que consulta los art�culos de pedidos anteriores de la peticion
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarArticulosPedidosAnterioresPeticion(Connection con,HashMap campos);
	
	/**
	 * M�todo que consulta los centros de costo de los pedidos existentes de la peticion
	 * @param con
	 * @param numeroPeticion
	 * @return
	 */
	public HashMap consultarCentrosCostoPedidosPeticion(Connection con,int numeroPeticion);
	
	/**
	 * M�todo que registra el consumo de los articulos
	 * @param con
	 * @param campos
	 * @return
	 */
	public ActionErrors guardarArticulosConsumo(Connection con,HashMap campos);
	
	/**
	 * M�todo que verifica si existe hoja de anestesia finalizada
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean existeHojaAnestesiaSinFinalizar(Connection con,String numeroSolicitud);
	
	/**
	 * M�todo que realiza la reversi�n de la finalizaci�n del consumo
	 * @param con
	 * @param campos
	 * @return
	 */
	public int reversionFinalizacionConsumo(Connection con,HashMap campos);
	
	/**
	 * M�todo que verifica si existe consumo de materiales finalizado
	 */
	public boolean existeConsumoMateriales(Connection con,String numeroSolicitud,boolean finalizado);
	
	/**
	 * M�todo para insertar el consumo de un art�culo de modo autom�tico
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarConsumoArticuloAutomatico(Connection con,HashMap campos);
	
	/**
	 * M�todo para consultar la cantidad del consumo del articulo de una peticion
	 * Si retorna -1 quiere decir que el art�culo no tiene consumo
	 */
	public int consultarCantidadConsumoArticuloPeticion(Connection con,HashMap campos);
	
	/**
	 * M�todo para obtener las farmacias de los pedidos de la peticion
	 * @param con
	 * @param codigoPeticion
	 * @return
	 */
	public String obtenerFarmaciasPedidosPeticion(Connection con,String codigoPeticion);
}
