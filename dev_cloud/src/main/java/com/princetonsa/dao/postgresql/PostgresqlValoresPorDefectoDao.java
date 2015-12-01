/*
 * Creado en 18/08/2004
 *
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import util.ConstantesBD;
import util.ResultadoBoolean;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ValoresPorDefectoDao;
import com.princetonsa.dao.sqlbase.SqlBaseValoresPorDefectoDao;
import com.princetonsa.dto.administracion.DtoAreaAperturaCuentaAutoPYP;
import com.princetonsa.dto.administracion.DtoFirmasValoresPorDefecto;

/**
 * @author Juan David Ramírez López
 *
 * Princeton S.A.
 */
public class PostgresqlValoresPorDefectoDao implements ValoresPorDefectoDao
{

	/**
	 * Método para modificar un parametro de valores por defecto
	 * @param con
	 * @param parametro
	 * @param nombre
	 * @param valor
	 * @return true si se modifico correctamente, false de lo contrario
	 */
	public boolean modificar(Connection con, String parametro, String nombre, String valor, int institucion)
	{
		return SqlBaseValoresPorDefectoDao.modificar(con, parametro, nombre, valor, institucion);
	}
	
	/**
	 * Método para cargar todos los valores por defecto
	 * @param con Conexión con la fuente de datos 
	 * @return Collection con todos los valores por defecto
	 */
	public Collection cargar(Connection con)
	{
		return SqlBaseValoresPorDefectoDao.cargar(con);
	}

	/**
	 * Implementación del método que que carga el código 
	 * cie válido para una BD Postgresql
	 *
	 * @see com.princetonsa.dao.ValoresPorDefectoDao#cargarCodigoTipoCieActual (Connection )
	 */
	public int cargarCodigoTipoCieActual (Connection con)
	{
	    return SqlBaseValoresPorDefectoDao.cargarCodigoTipoCieActual (con);
	}
	/**
	 * Metodo para cargar las etiquetas de
	 * parametros generales
	 * @param con Connection
	 * @param modulo int
	 * @return HashMap
	 * @author jarloc
	 */
	public HashMap cargarEtiquetas(Connection con,int modulo)
	{
	    return SqlBaseValoresPorDefectoDao.cargarEtiquetas(con, modulo);
	}
	/**
	 * metodo para generar el resumen de parametros
	 * @param con Connection
	 * @param modulo int
	 * @param institucion int 
	 * @return HashMap
	 * @author jarloc
	 */
	public HashMap cargarResumenValores(Connection con,int modulo,int institucion)
	{
	    return SqlBaseValoresPorDefectoDao.cargarResumenValores(con, modulo, institucion);
	}
	

	/**
	 * @param con
	 * @return
	 */
	public HashMap cargarIntegridadDominio(Connection con)
	{
		return SqlBaseValoresPorDefectoDao.cargarIntegridadDominio(con);
	}
	
	/**
	 * @param con
	 * @return
	 */
	public HashMap consultarCentrosCostoTerceros(Connection con)
	{
		return SqlBaseValoresPorDefectoDao.consultarCentrosCostoTerceros(con);
	}
	
	/**
	 * @param con
	 * @param mapa
	 * @return
	 */
	public boolean insertarCentrosCostoTercero(Connection con, HashMap mapa)
	{
		return SqlBaseValoresPorDefectoDao.insertarCentrosCostoTercero(con, mapa);
	}
	
	/**
	 * @param con
	 * @return
	 */
	public HashMap consultarHorasReproceso(Connection con)
	{
		return SqlBaseValoresPorDefectoDao.consultarHorasReproceso(con);
	}
	
	/**
	 * @param con
	 * @param mapa
	 * @return
	 */
	public boolean insertarHorasReproceso(Connection con, HashMap mapa)
	{
		return SqlBaseValoresPorDefectoDao.insertarHorasReproceso(con, mapa);
	}
	
	/**
	 * Método encargado de ejecutar al consulta consultarSiExisteFacturaVaria
	 * y retorna el value object si hay registros en la tabla: facturas_varias
	 * @author Felipe Pérez Granda
	 * @param con
	 * @return HashMap Cargar Value Object
	 */
	public HashMap consultarFacturasVarias(Connection con)
	{
		return SqlBaseValoresPorDefectoDao.consultarFacturasVarias(con,DaoFactory.POSTGRESQL);
	}
	
	/**
	 * @param con
	 * @return
	 */
	public HashMap consultarClasesInventariosPaqMatQx(Connection con)
	{
		return SqlBaseValoresPorDefectoDao.consultarClasesInventariosPaqMatQx(con);
	}
	
	/**
	 * @param con
	 * @param mapa 
	 * @return
	 */
	public boolean insertarClasesInventariosPaqMatQx(Connection con, HashMap mapa)
	{
		return SqlBaseValoresPorDefectoDao.insertarClasesInventariosPaqMatQx(con, mapa);
	}
	
	/**
	 * Método encargado de consultar si existe el datos seHanUtilizadoMedicamentosPosEnElTratamientoDelPaciente
	 * @author Felipe Pérez Granda
	 * @param con
	 * @param numSolicitudOrden
	 * @param codArticulo
	 * @param esOrdenAmbulatoria
	 * @return HashMap
	 */
	public HashMap<String,Object> utilizaMedicamentosTratamientoPaciente(Connection con, String numSolicitudOrden, String codArticulo, boolean esOrdenAmbulatoria)
	{
		return SqlBaseValoresPorDefectoDao.utilizaMedicamentosTratamientoPaciente(con, numSolicitudOrden, codArticulo, esOrdenAmbulatoria);
	}
	
	//Anexo 922
	/**
	 * 
	 */
	public ResultadoBoolean insertarFirmasValoresPorDefecto(DtoFirmasValoresPorDefecto dto)
	{
		return SqlBaseValoresPorDefectoDao.insertarFirmasValoresPorDefecto(dto);
	}
	
	/**
	 * 
	 */
	public ArrayList<DtoFirmasValoresPorDefecto> consultarFirmasValoresPorDefecto(DtoFirmasValoresPorDefecto dto)
	{
		return SqlBaseValoresPorDefectoDao.consultarFirmasValoresPorDefecto(dto);
	}
	
	/**
	 * 
	 */
	public ResultadoBoolean eliminarFirma(DtoFirmasValoresPorDefecto dto)
	{
		return SqlBaseValoresPorDefectoDao.eliminarFirma(dto);
	}
	
	/**
	 * 
	 */
	public boolean existeAgendaOdon()
	{
		return SqlBaseValoresPorDefectoDao.existeAgendaOdon();
	}
	
	/**
	 * 
	 */
	public boolean existePresupesto()
	{
		return SqlBaseValoresPorDefectoDao.existePresupesto();
	}

	/**
	 * 
	 */
	public boolean guardarAreaAperturaCuentaAutoPYP(DtoAreaAperturaCuentaAutoPYP dto)
	{
		return SqlBaseValoresPorDefectoDao.guardarAreaAperturaCuentaAutoPYP(dto);
	}
	
	/**
	 * 
	 */
	public boolean eliminarAreaAperturaCuentaAutoPYP(DtoAreaAperturaCuentaAutoPYP dto)
	{
		return SqlBaseValoresPorDefectoDao.eliminarAreaAperturaCuentaAutoPYP(dto);
	}
	
	/**
	 * 
	 */
	public ArrayList<DtoAreaAperturaCuentaAutoPYP> consultarAreasAperturaCuentaAutoPYP(int institucion, int centroAtencion)
	{
		return SqlBaseValoresPorDefectoDao.consultarAreasAperturaCuentaAutoPYP(institucion, centroAtencion);
	}
	
	@Override
	public ArrayList<Integer> consultarServiciosManejoTransPrimario(int institucion, Connection con) 
	{
		return SqlBaseValoresPorDefectoDao.consultarServiciosManejoTransPrimario(institucion, con);
	}

	@Override
	public ArrayList<Integer> consultarServiciosManejoTransSecundario(int institucion, Connection con) 
	{
		return SqlBaseValoresPorDefectoDao.consultarServiciosManejoTransSecundario(institucion, con);
	}


	@Override
	public boolean insertarServiciosManejoTransPrimario(Connection con,ArrayList<Integer> servicios,int institucion) 
	{
		return SqlBaseValoresPorDefectoDao.insertarServiciosManejoTransPrimario(con,servicios,institucion);
	}

	@Override
	public boolean insertarServiciosManejoTransSecundario(Connection con,ArrayList<Integer> servicios,int institucion) 
	{
		return SqlBaseValoresPorDefectoDao.insertarServiciosManejoTransSecundario(con,servicios,institucion);
	}
	
}