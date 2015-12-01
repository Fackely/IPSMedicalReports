package com.princetonsa.dao.postgresql.historiaClinica;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.EvolucionesDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseEvolucionesDao;
import com.princetonsa.dto.historiaClinica.DtoEvolucion;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.servinte.axioma.dto.manejoPaciente.InformacionCentroCostoValoracionDto;

public class PostgresqlEvolucionesDao implements EvolucionesDao 
{

	
	/**
	 * 
	 */
	public DtoEvolucion cargarEvolucion(Connection con, String evolucion) 
	{
		return SqlBaseEvolucionesDao.cargarEvolucion(con, evolucion);
	}
	
	/**
	 * 
	 */
	public int insertarEvolucionBase(Connection con, HashMap mapa) 
	{
		return SqlBaseEvolucionesDao.insertarEvolucionBase(con, mapa);
	}
	
	/**
	 * 
	 */
	public boolean insertarEvolucionesHospitalarias(Connection con, HashMap mapa)
	{
		return SqlBaseEvolucionesDao.insertarEvolucionesHospitalarias(con, mapa);
	}

	/**
	 * 
	 */
	public boolean insertarEvolSignosVitales(Connection con, HashMap mapa) 
	{
		return SqlBaseEvolucionesDao.insertarEvolSignosVitales(con, mapa);
	}
	
	/**
	 * 
	 */
	public boolean insertarDiagnosticosEvolucion(Connection con, HashMap mapa) 
	{
		return SqlBaseEvolucionesDao.insertarDiagnosticosEvolucion(con, mapa);
	}
	
	/**
	 * 
	 */
	public boolean insertarBalanceLiquidosEvol(Connection con, HashMap mapa)
	{
		return SqlBaseEvolucionesDao.insertarBalanceLiquidosEvol(con, mapa);
	}
	
	/**
	 * 
	 */
	public boolean insertarIngresoCuidadoEspecial(Connection con, HashMap mapa) 
	{
		return SqlBaseEvolucionesDao.insertarIngresoCuidadoEspecial(con,mapa);
	}
	
	/**
	 * 
	 */
	public int consultarCausaExternaValoracion(Connection con, int codigoCuenta, int codigoTipoEvolucion) 
	{
		return SqlBaseEvolucionesDao.consultarCausaExternaValoracion(con, codigoCuenta, codigoTipoEvolucion);
	}
	
	/**
	 * 
	 */
	public int obtenerCodigoUltimaEvolucion(Connection con, int codigoCuenta) 
	{
		return SqlBaseEvolucionesDao.obtenerCodigoUltimaEvolucion(con, codigoCuenta);
	}
	
	/**
	 * 
	 */
	public boolean actualizarIngresoCuidadoEspecial(Connection con, HashMap mapa) 
	{
		return SqlBaseEvolucionesDao.actualizarIngresoCuidadoEspecial(con, mapa);
	}
	
	/**
	 * 
	 */
	public String[] obtenerDiagnosticoComplicacion(Connection con, int codigoEvolucion) 
	{
		return SqlBaseEvolucionesDao.obtenerDiagnosticoComplicacion(con, codigoEvolucion);
	}
	
	/**
	 * 
	 */
	public String[] obtenerDiagnosticoPrincipal(Connection con, int codigoEvolucion) 
	{
		return SqlBaseEvolucionesDao.obtenerDiagnosticoPrincipal(con, codigoEvolucion);
	}
	
	/**
	 * 
	 */
	public String[] consultarTipoMonitoreo(Connection con, int codigoIngreso) 
	{
		return SqlBaseEvolucionesDao.consultarTipoMonitoreo(con, codigoIngreso);
	}

	/**
	 * 
	 */
	public boolean actualizarAreaCuenta(Connection con, HashMap mapa) 
	{
		return SqlBaseEvolucionesDao.actualizarAreaCuenta(con, mapa);
	}
	
	/**
	 * 
	 */
	public ArrayList<Diagnostico> consultaDiagnosticosRelacionados(Connection con, int codigoEvolucion) 
	{
		return SqlBaseEvolucionesDao.consultaDiagnosticosRelacionados(con, codigoEvolucion);
	}
	
	/**
	 * 
	 */
	public String[] consultarInfoFallecimiento(Connection con, int codigoCuenta) 
	{
		return SqlBaseEvolucionesDao.consultarInfoFallecimiento(con, codigoCuenta);
	}
	
	/**
	 * 
	 */
	public boolean enviadoEpicrisis(Connection con, String codigoEvolucion) 
	{
		return SqlBaseEvolucionesDao.enviadoEpicrisis(con, codigoEvolucion);
	}

	/**
	 * 
	 */
	public boolean insertarComentariosEvolucion(Connection con, HashMap mapa) 
	{
		return SqlBaseEvolucionesDao.insertarComentariosEvolucion(con, mapa);
	}

	/**
	 * 
	 */
	public String[] consultarOrdenEgreso(Connection con, int codigoEvolucion) 
	{
		return SqlBaseEvolucionesDao.consultarOrdenEgreso(con, codigoEvolucion);
	}

	/**
	 * 
	 */
	public int obtenerUltimaConducta(Connection con, int codigoEvolucion) 
	{
		return SqlBaseEvolucionesDao.obtenerUltimaConducta(con, codigoEvolucion);
	}
	
	/**
	 * @see com.princetonsa.dao.historiaClinica.EvolucionesDao#informacionCentroCostoDeIngresoActivoCuidadoEspecialXCuenta(Connection, int)
	 */
	@Override
	public InformacionCentroCostoValoracionDto informacionCentroCostoDeIngresoActivoCuidadoEspecialXCuenta(Connection con, int idCuenta) {
		return SqlBaseEvolucionesDao.informacionCentroCostoDeIngresoActivoCuidadoEspecialXCuenta(con, idCuenta);
	}	
	
}
