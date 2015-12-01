package com.princetonsa.dao.postgresql.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.GeneracionArchivoPlanoIndicadoresCalidadDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseGeneracionArchivoPlanoIndicadoresCalidadDao;

/**
 * @author lgchavez@princetonsa.com
 */
public class PostgresqlGeneracionArchivoPlanoIndicadoresCalidadDao implements GeneracionArchivoPlanoIndicadoresCalidadDao {

	/**
	 *
	 */
	public HashMap consultarOportunidadAsignacionCitasConsulta(Connection con, HashMap mapa) {
		return SqlBaseGeneracionArchivoPlanoIndicadoresCalidadDao.consultarOportunidadAsignacionCitasConsulta(con, mapa);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public HashMap consultaProporcionCancelacionCirugiaProgramada(Connection con, HashMap mapa)
	{
		return SqlBaseGeneracionArchivoPlanoIndicadoresCalidadDao.consultaProporcionCancelacionCirugiaProgramada(con, mapa);
	}
	

	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public HashMap consultaOportunidadAtencionServiciosImagenologia(Connection con, HashMap mapa)
	{
		return SqlBaseGeneracionArchivoPlanoIndicadoresCalidadDao.consultaOportunidadAtencionServiciosImagenologia(con, mapa);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public HashMap consultaCirugiaProgramada(Connection con, HashMap mapa)
	{
		return SqlBaseGeneracionArchivoPlanoIndicadoresCalidadDao.consultaCirugiaProgramada(con, mapa);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public HashMap consultaTasaReingresoPacientesHospitalizados(Connection con, HashMap mapa)
	{
		return SqlBaseGeneracionArchivoPlanoIndicadoresCalidadDao.consultaTasaReingresoPacientesHospitalizados(con, mapa);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public HashMap consultaProporcionPacientesHipertencionArterialControlada(Connection con, HashMap mapa)
	{
		return SqlBaseGeneracionArchivoPlanoIndicadoresCalidadDao.consultaProporcionPacientesHipertencionArterialControlada(con, mapa);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public HashMap consultaTasaMortalidadIntrahospitalariaDespuesDosDias(Connection con, HashMap mapa)
	{
		return SqlBaseGeneracionArchivoPlanoIndicadoresCalidadDao.consultaTasaMortalidadIntrahospitalariaDespuesDosDias(con, mapa);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public HashMap consultaTasaInfeccionIntrahospitalaria(Connection con, HashMap mapa)
	{
		return SqlBaseGeneracionArchivoPlanoIndicadoresCalidadDao.consultaTasaInfeccionIntrahospitalaria(con, mapa);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public HashMap consultaProporcionVigilanciaEventosAdversos(Connection con, HashMap mapa)
	{
		return SqlBaseGeneracionArchivoPlanoIndicadoresCalidadDao.consultaProporcionVigilanciaEventosAdversos(con, mapa);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public HashMap consultaTasaSatisfaccionGlobal(Connection con, HashMap mapa)
	{
		return SqlBaseGeneracionArchivoPlanoIndicadoresCalidadDao.consultaTasaSatisfaccionGlobal(con, mapa);
	}


	public HashMap consultaOportunidadAtencionConsultaUrgencias(Connection con, HashMap mapa) {
		return SqlBaseGeneracionArchivoPlanoIndicadoresCalidadDao.consultaOportunidadAtencionConsultaUrgencias(con, mapa);
	}
	
	

	public HashMap consultarDatosInstitucion(Connection con, HashMap mapa){
		return SqlBaseGeneracionArchivoPlanoIndicadoresCalidadDao.consultarDatosInstitucion(con, mapa);
	}
	
	public HashMap consultarDatosEmpresaInstitucion(Connection con, HashMap mapa){
		return SqlBaseGeneracionArchivoPlanoIndicadoresCalidadDao.consultarDatosEmpresaInstitucion(con, mapa);
	}
	
	public int guardarLog(Connection con, HashMap mapa)
	{
		return SqlBaseGeneracionArchivoPlanoIndicadoresCalidadDao.guardarLog(con, mapa);
	}
	
	public String consultarUltimoPath(Connection con)
	{
		return SqlBaseGeneracionArchivoPlanoIndicadoresCalidadDao.consultarUltimoPath(con);
	}
	

}