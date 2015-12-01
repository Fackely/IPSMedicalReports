package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.sqlbase.facturacion.SqlBaseGeneracionArchivoPlanoIndicadoresCalidadDao;

/**
 * Interfaz de Generacion Archivo Plano Indicadores Calidad 
 * @author lgchavez@princetonsa.com
 */
public interface GeneracionArchivoPlanoIndicadoresCalidadDao {
	
	public  HashMap consultarOportunidadAsignacionCitasConsulta(Connection con, HashMap mapa);
	public  HashMap consultaProporcionCancelacionCirugiaProgramada(Connection con, HashMap mapa);
	public  HashMap consultaOportunidadAtencionServiciosImagenologia(Connection con, HashMap mapa);
	public  HashMap consultaCirugiaProgramada(Connection con, HashMap mapa);
	public  HashMap consultaTasaReingresoPacientesHospitalizados(Connection con, HashMap mapa);
	public  HashMap consultaProporcionPacientesHipertencionArterialControlada(Connection con, HashMap mapa);
	public  HashMap consultaTasaMortalidadIntrahospitalariaDespuesDosDias(Connection con, HashMap mapa);
	public  HashMap consultaTasaInfeccionIntrahospitalaria(Connection con, HashMap mapa);
	public  HashMap consultaProporcionVigilanciaEventosAdversos(Connection con, HashMap mapa);
	public  HashMap consultaTasaSatisfaccionGlobal(Connection con, HashMap mapa);	
	public  HashMap consultaOportunidadAtencionConsultaUrgencias(Connection con, HashMap mapa);
	public  HashMap consultarDatosInstitucion(Connection con, HashMap mapa);
	public	HashMap consultarDatosEmpresaInstitucion(Connection con, HashMap mapa);
	public int guardarLog(Connection con, HashMap mapa);
	public String consultarUltimoPath(Connection con);
	
}