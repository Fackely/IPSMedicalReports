/*
 * Oct 4, 2005
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;

import com.princetonsa.dao.MaterialesQxDao;
import com.princetonsa.dao.sqlbase.SqlBaseMaterialesQxDao;

/**
 * @author Sebastián Gómez
 *
 * Clase que maneja los métodos propìos de Oracle para el acceso a la fuente
 * de datos en la funcionalidad Materiales Qx
 */
public class OracleMaterialesQxDao implements MaterialesQxDao 
{
	
	 /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(OracleMaterialesQxDao.class);
	        
	
	
	/**
	 * Método usado para consultar las ordenes de cirugia de una cuenta 
	 * que tengan estado de facturacion 'Pendiente' y estado de historia clínica 'Interpretada'
	 * @param con
	 * @param cuenta
	 * @param funcionalidad
	 * @return
	 */
	public HashMap cargarOrdenesCirugia(Connection con,int cuenta,String funcionalidad)
	{
		return SqlBaseMaterialesQxDao.cargarOrdenesCirugia(con,cuenta,funcionalidad);        	
	}
	
	/**
	 * Método usado para consultar los datos generales de un
	 * registro de materiales Qx.
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap cargarDatosGenerales(Connection con,int numeroSolicitud)
	{
		return SqlBaseMaterialesQxDao.cargarDatosGenerales(con,numeroSolicitud);
	}
	
	/**
	 * Método para consultar las cirugías de un acto quirúgico que harán
	 * parte del registro de los materiales Qx.
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap cargarCirugiasPorActo(Connection con,int numeroSolicitud)
	{
		return SqlBaseMaterialesQxDao.cargarCirugiasPorActo(con,numeroSolicitud);
	}
	
	/**
	 * Método implementado para cargar los artículos para Materiales Qx. buscando así:
	 * 1) Se buscan primero si existen articulos ya ingresados en Materiales Qx.
	 * 2) Si no hay, se buscan artículos en el pedido de la orden.
	 * 3) Si no hay, se buscan artículos en la hoja de gastos
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap cargarArticulosMaterialesQx(Connection con,int numeroSolicitud)
	{
		return SqlBaseMaterialesQxDao.cargarArticulosMaterialesQx(con,numeroSolicitud);
	}
	
	/**
	 * Método que inserta la información del encabezado del ingreso de consumo
	 * de materiales Qx.
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param usuario
	 * @param fecha
	 * @param hora
	 * @param esActo
	 * @param finalizado
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
			int farmacia,String entidadSubcontratada)
	{
		return SqlBaseMaterialesQxDao.insertarEncabezadoMaterialesQx(con,numeroSolicitud,centroCosto,usuario,fecha,hora,esActo,finalizado,estado,farmacia,entidadSubcontratada);
	}
	
	
	
	/**
	 * Método que modifica la información del encabezado de materiales Qx.
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param usuario
	 * @param fecha
	 * @param hora
	 * @param esActo
	 * @param finalizado
	 * @param estado
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
			int farmacia, String entidadSubcontratada)
	{
		return SqlBaseMaterialesQxDao.modificarEncabezadoMaterialesQx(con,numeroSolicitud,centroCosto,usuario,fecha,hora,esActo,finalizado,estado,farmacia,entidadSubcontratada);
	}
	
	
	/**
	 * Método que carga los artículos por cirugia de un registro de materiales Qx.
	 * @param con
	 * @param numeroSolicitud
	 * @param numeroCirugias
	 * @return
	 */
	public HashMap cargarArticulosPorCirugia(Connection con,int numeroSolicitud,int numeroCirugias)
	{
		return SqlBaseMaterialesQxDao.cargarArticulosPorCirugia(con,numeroSolicitud,numeroCirugias);
	}
	
	/**
	 * Método que realiza uan busqueda avanzada de ordenes de cirugia
	 * que ya estén cargadas
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
			String fechaOrdenFinal,String fechaCxInicial,String fechaCxFinal,int medico,int centroAtencion)
	{
		return SqlBaseMaterialesQxDao.cargarOrdenesCirugia(con,ordenInicial,ordenFinal,fechaOrdenInicial,fechaOrdenFinal,fechaCxInicial,fechaCxFinal,medico,centroAtencion);
	}
	
	/**
	 * Método que consulta la cantidad de los articulos despachados de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap consultarCantidadesArticulosDespacho(Connection con,String numeroSolicitud)
	{
		return SqlBaseMaterialesQxDao.consultarCantidadesArticulosDespacho(con, numeroSolicitud);
	}
	
	/**
	 * Método para consultar el pedido pendiente de una peticion
	 */
	public HashMap consultarPedidoPendientePeticion(Connection con,HashMap campos)
	{
		return SqlBaseMaterialesQxDao.consultarPedidoPendientePeticion(con, campos);
	}
	
	/**
	 * Método que consulta los artículos de pedidos anteriores de la peticion
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarArticulosPedidosAnterioresPeticion(Connection con,HashMap campos)
	{
		return SqlBaseMaterialesQxDao.consultarArticulosPedidosAnterioresPeticion(con, campos);
	}
	
	/**
	 * Método que consulta los centros de costo de los pedidos existentes de la peticion
	 * @param con
	 * @param numeroPeticion
	 * @return
	 */
	public HashMap consultarCentrosCostoPedidosPeticion(Connection con,int numeroPeticion)
	{
		return SqlBaseMaterialesQxDao.consultarCentrosCostoPedidosPeticion(con, numeroPeticion);
	}
	
	
	/**
	 * Método que registra el consumo de los articulos
	 * @param con
	 * @param campos
	 * @return
	 */
	public ActionErrors guardarArticulosConsumo(Connection con,HashMap campos)
	{
		campos.put("secuenciaFinalizacion","seq_det_fin_mat_qx.nextval");
		return SqlBaseMaterialesQxDao.guardarArticulosConsumo(con, campos);
	}
	
	/**
	 * Método que verifica si existe hoja de anestesia finalizada
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean existeHojaAnestesiaSinFinalizar(Connection con,String numeroSolicitud)
	{
		return SqlBaseMaterialesQxDao.existeHojaAnestesiaSinFinalizar(con, numeroSolicitud);
	}
	
	/**
	 * Método que realiza la reversión de la finalización del consumo
	 * @param con
	 * @param campos
	 * @return
	 */
	public int reversionFinalizacionConsumo(Connection con,HashMap campos)
	{
		return SqlBaseMaterialesQxDao.reversionFinalizacionConsumo(con, campos);
	}
	
	/**
	 * Método que verifica si existe consumo de materiales finalizado
	 */
	public boolean existeConsumoMateriales(Connection con,String numeroSolicitud,boolean finalizado)
	{
		return SqlBaseMaterialesQxDao.existeConsumoMateriales(con, numeroSolicitud, finalizado);
		
	}
	
	/**
	 * Método para insertar el consumo de un artículo de modo automático
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarConsumoArticuloAutomatico(Connection con,HashMap campos)
	{
		return SqlBaseMaterialesQxDao.insertarConsumoArticuloAutomatico(con, campos);
	}
	
	/**
	 * Método para consultar la cantidad del consumo del articulo de una peticion
	 * Si retorna -1 quiere decir que el artículo no tiene consumo
	 */
	public int consultarCantidadConsumoArticuloPeticion(Connection con,HashMap campos)
	{
		return SqlBaseMaterialesQxDao.consultarCantidadConsumoArticuloPeticion(con, campos);
	}
	
	/**
	 * Método para obtener las farmacias de los pedidos de la peticion
	 * @param con
	 * @param codigoPeticion
	 * @return
	 */
	public String obtenerFarmaciasPedidosPeticion(Connection con,String codigoPeticion)
	{
		return SqlBaseMaterialesQxDao.obtenerFarmaciasPedidosPeticion(con, codigoPeticion);
	}
}
