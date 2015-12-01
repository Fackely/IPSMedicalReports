package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.GeneracionArchivoPlanoIndicadoresCalidadDao;

public class GeneracionArchivoPlanoIndicadoresCalidad extends ValidatorForm {
	
	/**
	 * Intefaz para acceder y comunicarse con la fuente de datos
	 */
	private GeneracionArchivoPlanoIndicadoresCalidadDao objetoDao;
	
	
	/**
	 *  Constructor de la clase
	 */
	public GeneracionArchivoPlanoIndicadoresCalidad() 
	{
		init(System.getProperty("TIPOBD"));
		this.reset();
	}
	
	/**
	 * Inicializa el acceso a la base de datos de este objeto, obteniendo su respectivo DAO.
	 * param tipoBD el tipo de bases de datos que va a usar este objeto.
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores validos para tipoBD.
	 * son los nombres y constantes definidos en <code>DaoFactory</code>
	 * @return <b>true</b> si la inicializacion fue exitosa, <code>false</code> si no.
	 */
	
	private boolean init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(tipoBD);
			objetoDao=myFactory.getGeneracionArchivoPlanoIndicadoresCalidadDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
			
		
	}
	
	/**
	 * resetea los atributos del objeto.
	 *
	 */
	private void reset() 
	{
		
   	}
	
	public HashMap consultarDatosEmpresaInstitucion(Connection con, HashMap mapa)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGeneracionArchivoPlanoIndicadoresCalidadDao().consultarDatosEmpresaInstitucion(con, mapa);
	}
	
	public HashMap consultarDatosInstitucion(Connection con, HashMap mapa)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGeneracionArchivoPlanoIndicadoresCalidadDao().consultarDatosInstitucion(con, mapa);
	}
	
	
	public HashMap consultaOportunidadAtencionConsultaUrgencias(Connection con, HashMap mapa) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGeneracionArchivoPlanoIndicadoresCalidadDao().consultaOportunidadAtencionConsultaUrgencias(con, mapa);
			
	}
	
	public HashMap consultarOportunidadAsignacionCitasConsulta(Connection con, HashMap mapa) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGeneracionArchivoPlanoIndicadoresCalidadDao().consultarOportunidadAsignacionCitasConsulta(con, mapa);
			
	}
	
	public int guardarLog(Connection con, HashMap mapa)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGeneracionArchivoPlanoIndicadoresCalidadDao().guardarLog(con, mapa);
	}
	
	
	public String consultarUltimoPath(Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGeneracionArchivoPlanoIndicadoresCalidadDao().consultarUltimoPath(con);
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public HashMap consultaProporcionCancelacionCirugiaProgramada(Connection con, HashMap mapa)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGeneracionArchivoPlanoIndicadoresCalidadDao().consultaProporcionCancelacionCirugiaProgramada(con, mapa);
	}
	

	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public HashMap consultaOportunidadAtencionServiciosImagenologia(Connection con, HashMap mapa)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGeneracionArchivoPlanoIndicadoresCalidadDao().consultaOportunidadAtencionServiciosImagenologia(con, mapa);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public HashMap consultaCirugiaProgramada(Connection con, HashMap mapa)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGeneracionArchivoPlanoIndicadoresCalidadDao().consultaCirugiaProgramada(con, mapa);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public HashMap consultaTasaReingresoPacientesHospitalizados(Connection con, HashMap mapa)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGeneracionArchivoPlanoIndicadoresCalidadDao().consultaTasaReingresoPacientesHospitalizados(con, mapa);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public HashMap consultaProporcionPacientesHipertencionArterialControlada(Connection con, HashMap mapa)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGeneracionArchivoPlanoIndicadoresCalidadDao().consultaProporcionPacientesHipertencionArterialControlada(con, mapa);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public HashMap consultaTasaMortalidadIntrahospitalariaDespuesDosDias(Connection con, HashMap mapa)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGeneracionArchivoPlanoIndicadoresCalidadDao().consultaTasaMortalidadIntrahospitalariaDespuesDosDias(con, mapa);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public HashMap consultaTasaInfeccionIntrahospitalaria(Connection con, HashMap mapa)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGeneracionArchivoPlanoIndicadoresCalidadDao().consultaTasaInfeccionIntrahospitalaria(con, mapa);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public HashMap consultaProporcionVigilanciaEventosAdversos(Connection con, HashMap mapa)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGeneracionArchivoPlanoIndicadoresCalidadDao().consultaProporcionVigilanciaEventosAdversos(con, mapa);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public HashMap consultaTasaSatisfaccionGlobal(Connection con, HashMap mapa)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGeneracionArchivoPlanoIndicadoresCalidadDao().consultaTasaSatisfaccionGlobal(con, mapa);
	}

}
