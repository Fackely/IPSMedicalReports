package com.princetonsa.mundo.facturacion;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.IngresarModificarContratosEntidadesSubcontratadasDao;


public class IngresarModificarContratosEntidadesSubcontratadas
{
	/**
	 * Para manejo de Logs
	 */
	private static Logger logger = Logger.getLogger(IngresarModificarContratosEntidadesSubcontratadas.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static IngresarModificarContratosEntidadesSubcontratadasDao ingresarModificarContratosEntidadesSubcontratadasDao;
	
	/**
	 * Codigo de la institucion
	 */
	private int institucion;
	
	private void reset() 
	{
		this.institucion = ConstantesBD.codigoNuncaValido;
	}
	
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		if (myFactory != null)
		{
			ingresarModificarContratosEntidadesSubcontratadasDao = myFactory.getIngresarModificarContratosEntidadesSubcontratadasDao();
			wasInited = (ingresarModificarContratosEntidadesSubcontratadasDao != null);
		}
		return wasInited;
	}
	
	/**
	 * 
	 * @return
	 */
	public static IngresarModificarContratosEntidadesSubcontratadasDao getIngresarModificarContratosEntidadesSubcontratadasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getIngresarModificarContratosEntidadesSubcontratadasDao();
	}
	
	/**
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerEntidades(Connection con)
	{
		return getIngresarModificarContratosEntidadesSubcontratadasDao().obtenerEntidades(con);
	}

	/**
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerClaseInventarios(Connection con)
	{
		return getIngresarModificarContratosEntidadesSubcontratadasDao().obtenerClaseInventarios(con);
	}
	
	/**
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerEsquemas(Connection con)
	{
		return getIngresarModificarContratosEntidadesSubcontratadasDao().obtenerEsquemas(con);
	}
	
	/**
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerGruposServicio(Connection con, String activos)
	{
		return getIngresarModificarContratosEntidadesSubcontratadasDao().obtenerGruposServicio(con, activos);
	}
	
	/**
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerEsquemasProcedimientos(Connection con)
	{
		return getIngresarModificarContratosEntidadesSubcontratadasDao().obtenerEsquemasProcedimientos(con);
	}
	
	/**
	 * @param con
	 * @param entidad
	 * @return
	 */
	public static HashMap obtenerInfoXEntidadInv(Connection con, String entidad)
	{
		return getIngresarModificarContratosEntidadesSubcontratadasDao().obtenerInfoXEntidadInv(con, entidad);
	}
	
	/**
	 * @param con
	 * @param entidad
	 * @return
	 */
	public static HashMap obtenerInfoXEntidadServ(Connection con, String entidad)
	{
		return getIngresarModificarContratosEntidadesSubcontratadasDao().obtenerInfoXEntidadServ(con, entidad);
	}
	
	/**
	 * @param con
	 * @param entidad
	 * @return
	 */
	public static HashMap consultaInfoXEntidadEncabezado(Connection con, String entidad)
	{
		return getIngresarModificarContratosEntidadesSubcontratadasDao().consultaInfoXEntidadEncabezado(con, entidad);
	}
	/**
	 * @param con
	 * @param encabezadoEntidad
	 * @return
	 */
	public static long guardarEncabezado(Connection con, HashMap entidad)
	{
		return getIngresarModificarContratosEntidadesSubcontratadasDao().guardarEncabezado(con, entidad);
	}
	
	/**
	 * @param con
	 * @param esquemasInventarios
	 * @return
	 */
	public static boolean guardarEsquemasInventarios(Connection con, HashMap esquemasInventarios)
	{
		return getIngresarModificarContratosEntidadesSubcontratadasDao().guardarEsquemasInventarios(con, esquemasInventarios);
	}
	
	/**
	 * @param con
	 * @param esquemasServcios
	 * @return
	 */
	public static boolean guardarEsquemasServicios(Connection con, HashMap esquemasServicios)
	{
		return getIngresarModificarContratosEntidadesSubcontratadasDao().guardarEsquemasServicios(con, esquemasServicios);
	}
	
	public static HashMap consultaContratos(Connection con, HashMap encabezado, HashMap inventarios, HashMap servicios)
	{
		return getIngresarModificarContratosEntidadesSubcontratadasDao().consultaContratos(con,encabezado,inventarios,servicios);
	}
	
	public static HashMap consultarEsquemasInventarios(Connection con)
	{
		return getIngresarModificarContratosEntidadesSubcontratadasDao().consultaEsquemasInventarios(con);
	}
	
	public static HashMap consultarEsquemasServicios(Connection con)
	{
		return getIngresarModificarContratosEntidadesSubcontratadasDao().consultaEsquemasServicios(con);
	}
	
	public static boolean actualizarEncabezado(Connection con, HashMap encabezado)
	{
		return getIngresarModificarContratosEntidadesSubcontratadasDao().actualizarEncabezado(con,encabezado);
	}
	
	public static boolean eliminarEsquemasInventarios(Connection con, HashMap datos)
	{
		return getIngresarModificarContratosEntidadesSubcontratadasDao().eliminarEsquemasInventarios(con,datos);
	}
	
	public static boolean eliminarEsquemasServicios(Connection con, HashMap datos)
	{
		return getIngresarModificarContratosEntidadesSubcontratadasDao().eliminarEsquemasServicios(con,datos);
	}
	
	/**
	 * @param con
	 * @param encabezadoEntidad
	 * @return
	 */
	public static boolean verificarTraslapeContratos(Connection con, HashMap entidad)
	{
		return getIngresarModificarContratosEntidadesSubcontratadasDao().verificarTraslapeContratos(con, entidad);
	}
	
	public static boolean comprobarExistenciaInventario(Connection con, HashMap inventarios, int indice)
	{
		return getIngresarModificarContratosEntidadesSubcontratadasDao().comprobarExistenciaInventario(con, inventarios, indice);
	}
}