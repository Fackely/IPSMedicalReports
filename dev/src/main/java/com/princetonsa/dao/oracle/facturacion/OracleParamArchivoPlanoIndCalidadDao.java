package com.princetonsa.dao.oracle.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import util.ValoresPorDefecto;

import com.princetonsa.dao.facturacion.ParamArchivoPlanoIndCalidadDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseParamArchivoPlanoIndCalidadDao;
import com.princetonsa.mundo.facturacion.ParamArchivoPlanoIndCalidad;

public class OracleParamArchivoPlanoIndCalidadDao implements ParamArchivoPlanoIndCalidadDao
{
	private static String insertarES="INSERT INTO ind_calidad_especialidad VALUES " +
									"(facturacion.seq_ind_calidad_especialidad.nextval," +
									"?," +
									"?," +
									"?," +
									"?," +
									"CURRENT_DATE," +
									""+ValoresPorDefecto.getSentenciaHoraActualBD()+")";

	private static String insertarCC="INSERT INTO ind_calidad_cc VALUES " +
										"(facturacion.seq_ind_calidad_cc.nextval," +
										"?," +
										"?," +
										"?," +
										"?," +
										"CURRENT_DATE," +
										""+ValoresPorDefecto.getSentenciaHoraActualBD()+")";
	
	private static String insertarDX=	"INSERT INTO ind_calidad_dx VALUES " +
										"(facturacion.seq_ind_calidad_dx.nextval," +
										"?," +
										"?," +
										"?," +
										"?," +
										"?," +
										"CURRENT_DATE," +
										""+ValoresPorDefecto.getSentenciaHoraActualBD()+")";
	
	
	/**
	 * 
	 */
	public HashMap<String, Object> consultaES (Connection con)
	{
		return SqlBaseParamArchivoPlanoIndCalidadDao.consultaES(con);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> consultaCC (Connection con)
	{
		return SqlBaseParamArchivoPlanoIndCalidadDao.consultaCC(con);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> consultaDX (Connection con)
	{
		return SqlBaseParamArchivoPlanoIndCalidadDao.consultaDX(con);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> consultaG (Connection con)
	{
		return SqlBaseParamArchivoPlanoIndCalidadDao.consultaG(con);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> consultaCOD (Connection con, String tipo)
	{
		return SqlBaseParamArchivoPlanoIndCalidadDao.consultaCOD(con, tipo);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> consultaEspe (Connection con)
	{
		return SqlBaseParamArchivoPlanoIndCalidadDao.consultaEspe(con);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> consultaCentros (Connection con, int centroAtencion)
	{
		return SqlBaseParamArchivoPlanoIndCalidadDao.consultaCentros(con, centroAtencion);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> consultaSignos (Connection con)
	{
		return SqlBaseParamArchivoPlanoIndCalidadDao.consultaSignos(con);
	}
	
	/**
	 * 
	 */
	public boolean insertarEspecialidad(Connection con, ParamArchivoPlanoIndCalidad mundo)
	{
		return SqlBaseParamArchivoPlanoIndCalidadDao.insertarEspecialidad(con, mundo, insertarES);
	}
	
	/**
	 * 
	 */
	public boolean insertarCentro(Connection con, ParamArchivoPlanoIndCalidad mundo)
	{
		return SqlBaseParamArchivoPlanoIndCalidadDao.insertarCentro(con, mundo, insertarCC);
	}
	
	/**
	 * 
	 */
	public boolean insertarDiagnostico(Connection con, ParamArchivoPlanoIndCalidad mundo)
	{
		return SqlBaseParamArchivoPlanoIndCalidadDao.insertarDiagnostico(con, mundo, insertarDX);
	}
	
	/**
	 * 
	 */
	public boolean eliminarEspecialidad(Connection con, String codigo)
	{
		return SqlBaseParamArchivoPlanoIndCalidadDao.eliminar(con, codigo);
	}
	
	/**
	 * 
	 */
	public boolean eliminarCentro(Connection con, String codigo)
	{
		return SqlBaseParamArchivoPlanoIndCalidadDao.eliminarCC(con, codigo);
	}
	
	/**
	 * 
	 */
	public boolean eliminarDiagnostico(Connection con, String codigo)
	{
		return SqlBaseParamArchivoPlanoIndCalidadDao.eliminarDX(con, codigo);
	}
	
	/**
	 * 
	 */
	public boolean modificarES(Connection con, ParamArchivoPlanoIndCalidad mundo, String codigoM)
	{
		return SqlBaseParamArchivoPlanoIndCalidadDao.modificarES(con, mundo, codigoM);
	}
	
	/**
	 * 
	 */
	public boolean modificarCC(Connection con, ParamArchivoPlanoIndCalidad mundo, String codigoM)
	{
		return SqlBaseParamArchivoPlanoIndCalidadDao.modificarCC(con, mundo, codigoM);
	}
	
	/**
	 * 
	 */
	public boolean modificarDX(Connection con, ParamArchivoPlanoIndCalidad mundo, String codigoM)
	{
		return SqlBaseParamArchivoPlanoIndCalidadDao.modificarDX(con, mundo, codigoM);
	}
	
	/**
	 * 
	 */
	public boolean modificarS(Connection con, ParamArchivoPlanoIndCalidad mundo, String codigoM, String esNuevo)
	{
		return SqlBaseParamArchivoPlanoIndCalidadDao.modificarS(con, mundo, codigoM, esNuevo);
	}
}