package com.princetonsa.dao.postgresql.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.ContrarreferenciaDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseContrarreferenciaDao;

public class PostgresqlContrarreferenciaDao implements ContrarreferenciaDao
{
		
	public  HashMap busquedaInstitucionesSirc(Connection con,HashMap vo)
	{
		return SqlBaseContrarreferenciaDao.busquedaInstitucionesSirc(con, vo);
	}

	/**
	 * 
	 */
	public HashMap cargarConductasSeguirContrareferencia(Connection con, int numeroReferenciaContra) 
	{
		return SqlBaseContrarreferenciaDao.cargarConductasSeguirContrareferencia(con,numeroReferenciaContra);
	}
	
	
	
	public HashMap consultarReferenciaPaciente(Connection con, HashMap vo) 
	{
		return SqlBaseContrarreferenciaDao.consultarReferenciaPaciente(con,vo);
	}
	
	
	
	public HashMap consultarReferenciaArea(Connection con, HashMap vo)
	{
		return SqlBaseContrarreferenciaDao.consultarReferenciaArea(con,vo);				
	}
	
	
	
	public HashMap consultarContrarreferencia(Connection con, HashMap vo)
	{
		return SqlBaseContrarreferenciaDao.consultarContrarreferencia(con,vo);				
	}
	
	
	public boolean modificar(Connection con, HashMap vo)
	{
		return SqlBaseContrarreferenciaDao.modificar(con, vo);
	}
	
	
	public boolean insertar(Connection con, HashMap vo)
	{
		return SqlBaseContrarreferenciaDao.insertar(con, vo);
	}
	
	
	public int actualizarEstadoContrarreferencia(Connection con,HashMap vo)
	{
		return SqlBaseContrarreferenciaDao.actualizarEstadoContrarreferencia(con, vo);
		
	}
	
	
	
	public HashMap consultarResultadosProcedimiento(Connection con, int numeroReferencia) 
	{
		return SqlBaseContrarreferenciaDao.consultarResultadosProcedimiento(con,numeroReferencia);
	}
	
	
	
	public boolean insertarProcedimientos(Connection con, HashMap vo)
	{
		return SqlBaseContrarreferenciaDao.insertarProcedimientos(con, vo);
	}
	
	
	public boolean insertarDiagnosticos(Connection con, HashMap vo)
	{
		return SqlBaseContrarreferenciaDao.insertarDiagnosticos(con, vo);
	}

	/**
	 * 
	 */
	public boolean eliminarProcedimientos(Connection con, String numeroContrarreferencia, String numeroSolicitud) 
	{
		return SqlBaseContrarreferenciaDao.eliminarProcedimientos(con,numeroContrarreferencia,numeroSolicitud);
	}

	/**
	 * 
	 */
	public boolean eliminarDiagnosticos(Connection con, int numeroReferenciaContra) 
	{
		return SqlBaseContrarreferenciaDao.eliminarDiagnosticos(con,numeroReferenciaContra);
	}
	
	/**
	 * 
	 */
	public HashMap consultarDiagnosticos(Connection con, int numeroReferenciaContra) 
	{
		return SqlBaseContrarreferenciaDao.consultarDiagnosticos(con,numeroReferenciaContra);
	}

	/**
	 * 
	 */
	public boolean eliminarConductaSeguir(Connection con, int numeroReferenciaContra, String codigoConducta) 
	{
		return SqlBaseContrarreferenciaDao.eliminarConductaSeguir(con,numeroReferenciaContra,codigoConducta);
	}

	/**
	 * 
	 */
	public boolean insertarConductaSeguir(Connection con, int numeroReferenciaContra, String codigoConducta, String valor) 
	{
		return SqlBaseContrarreferenciaDao.insertarConductaSeguir(con,numeroReferenciaContra,codigoConducta,valor);
	}

	/**
	 * 
	 */
	public HashMap getUltimosDiagnosticosIngreso(Connection con, int ingreso) 
	{
		return SqlBaseContrarreferenciaDao.getUltimosDiagnosticosIngreso(con,ingreso);
	}

}
