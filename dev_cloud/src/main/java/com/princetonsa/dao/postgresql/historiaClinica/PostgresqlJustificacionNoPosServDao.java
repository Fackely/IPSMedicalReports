package com.princetonsa.dao.postgresql.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import util.ConstantesBD;
import util.UtilidadFecha;

import com.princetonsa.dao.historiaClinica.JustificacionNoPosServDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseJustificacionNoPosServDao;

/**
 * 
 * @author Giovanny Arias
 * Métodos de oracle para el acceso a la fuente de datos para las utilidades
 * de la funcionalidad justificacion de servicios no pos
 */
public class PostgresqlJustificacionNoPosServDao implements JustificacionNoPosServDao
{
	/**
	 * Método que consulta las justificaciones de servicios No POS que se encuentran pendiente por determinada cuenta
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public HashMap consultarSolicitudesJustificaciones(Connection con,int institucion)
	{
		return SqlBaseJustificacionNoPosServDao.consultarSolicitudesJustificaciones(con,institucion);
	}
	
	/**
	 * Método que consulta las justificaciones de servicios No POS que se encuentran pendiente por determinada cuenta
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public HashMap consultarSolicitudesJustificacionesDiligenciadas(Connection con, int cuenta)
	{
		return SqlBaseJustificacionNoPosServDao.consultarSolicitudesJustificacionesDiligenciadas(con, cuenta);
	}
	
	/**
	 * 
	 * @param con
	 * @param filtros
	 * @param codigosServicios
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public HashMap cargarInfoIngresoRango(Connection con, HashMap filtros, String codigosServicios, String fechaInicial, String fechaFinal)
	{
		
		String where = "c.estado_cuenta <> "+ConstantesBD.codigoEstadoCuentaCerrada+" and sc.nro_prioridad=1 "+
		"and s.estado_historia_clinica <> "+ConstantesBD.codigoEstadoHCAnulada+" " +
		"and s.fecha_solicitud BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' and '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"' " +
		
		"and i.centro_atencion="+filtros.get("centroAtencion")+" " ;
		
		return SqlBaseJustificacionNoPosServDao.cargarInfoIngresoRango(con, filtros, codigosServicios, fechaInicial, fechaFinal,where);
	}
	
	/**
	 * 
	 * @param con
	 * @param filtros
	 * @param codigosServicios
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public HashMap cargarInfoIngresoConsultarModificarRango(Connection con, HashMap filtros, String codigosServicios, String fechaInicial, String fechaFinal)
	{
		String where =" where " +
		" c.estado_cuenta!="+ConstantesBD.codigoEstadoCuentaCerrada+" and sc.nro_prioridad=1"+
		" and s.estado_historia_clinica!="+ConstantesBD.codigoEstadoHCAnulada+" " +
		" and jss.fecha_modifica BETWEEN TO_DATE ('"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"','YYYY-MM-DD') and TO_DATE ('"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"','YYYY-MM-DD') " +
		" and i.centro_atencion="+filtros.get("centroAtencion")+" " ;
		
		return SqlBaseJustificacionNoPosServDao.cargarInfoIngresoConsultarModificarRango(con, filtros, codigosServicios, fechaInicial, fechaFinal,where);
	}
	
	/**
	 * 
	 * @param con
	 * @param filtros
	 * @param codigosServicios
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public HashMap cargarInfoIngresoConsultarModificarRangoCon(Connection con, HashMap filtros, String codigosServicios, String fechaInicial, String fechaFinal)
	{
		
		String where=" where " +
		" c.estado_cuenta <> "+ConstantesBD.codigoEstadoCuentaCerrada+" and sc.nro_prioridad=1"+
		" and s.estado_historia_clinica <> "+ConstantesBD.codigoEstadoHCAnulada+" " +
		" and jss.fecha_modifica BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' and '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"' " +
		" and i.centro_atencion="+filtros.get("centroAtencion")+" " ;
	
		
		
		return SqlBaseJustificacionNoPosServDao.cargarInfoIngresoConsultarModificarRangoCon(con, filtros, codigosServicios, fechaInicial, fechaFinal,where);
	}
}	