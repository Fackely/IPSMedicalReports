/*
 * @(#)PostgresqlEvolucionHospitalariaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;

import com.princetonsa.dao.EvolucionHospitalariaDao;
import com.princetonsa.dao.sqlbase.SqlBaseEvolucionHospitalariaDao;

/**
 * Esta clase implementa el contrato estipulado en <code>EvolucionHospitalariaDao</code>, proporcionando los servicios
 * de acceso a una base de datos PostgreSQL requeridos por la clase <code>EvolucionHospitalaria</code>
 *
 * @version May 29, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public class PostgresqlEvolucionHospitalariaDao extends PostgresqlEvolucionDao implements EvolucionHospitalariaDao 
{

	/**
	 * Implementación del método que inserta una evolución hospitalaria, 
	 * soportando la definición de transaccionalidad en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.EvolucionHospitalariaDao#insertarTransaccional (Connection , int , String , boolean , boolean , boolean , boolean , boolean , boolean , boolean , boolean , boolean , boolean , boolean , boolean , boolean ) throws SQLException 
	 */
    public int insertar(Connection con, int codigoEvolucion, boolean cambiosManejoEpicrisis, boolean complicacionesEpicrisis, boolean diagnosticosDefinitivosEpicrisis, boolean diagnosticosPresuntivosEpicrisis, boolean fechaYResultadoExamenesDiagnosticoEpicrisis, boolean hallazgosEpicrisis, boolean infoDadaPacienteEpicrisis, boolean observacionesEpicrisis, boolean procedimientosQuirurgicosObstetricosEpicrisis, boolean pronosticoEpicrisis, boolean resultadosTratamientoEpicrisis, boolean signosVitalesEpicrisis, boolean tratamientoEpicrisis) throws SQLException
	{
		return SqlBaseEvolucionHospitalariaDao.insertar(con, codigoEvolucion, cambiosManejoEpicrisis, complicacionesEpicrisis, diagnosticosDefinitivosEpicrisis, diagnosticosPresuntivosEpicrisis, fechaYResultadoExamenesDiagnosticoEpicrisis, hallazgosEpicrisis, infoDadaPacienteEpicrisis, observacionesEpicrisis, procedimientosQuirurgicosObstetricosEpicrisis, pronosticoEpicrisis, resultadosTratamientoEpicrisis, signosVitalesEpicrisis, tratamientoEpicrisis) ;
	}

	/**
	 * Implementación del método que inserta una evolución hospitalaria, 
	 * soportando la definición de transaccionalidad en una BD Postgresql 
	 * 
	 * @see com.princetonsa.dao.EvolucionHospitalariaDao#insertarTransaccional(Connection , String , int , int , boolean , String , int , String , String , String , String , String , String , String , String , String , String , String , String , String , String , boolean , Collection , Collection , Collection , boolean , boolean , boolean , boolean , boolean , boolean , boolean , boolean , boolean , boolean , boolean , boolean , boolean , int , int ) throws SQLException 
	 */
    public int insertarTransaccional (Connection con, int codigoEvolucion, String estado, boolean cambiosManejoEpicrisis, boolean complicacionesEpicrisis, boolean diagnosticosDefinitivosEpicrisis, boolean diagnosticosPresuntivosEpicrisis, boolean fechaYResultadoExamenesDiagnosticoEpicrisis, boolean hallazgosEpicrisis, boolean infoDadaPacienteEpicrisis, boolean observacionesEpicrisis, boolean procedimientosQuirurgicosObstetricosEpicrisis, boolean pronosticoEpicrisis, boolean resultadosTratamientoEpicrisis, boolean signosVitalesEpicrisis, boolean tratamientoEpicrisis) throws SQLException 
	{
		return SqlBaseEvolucionHospitalariaDao.insertarTransaccional ( con,  codigoEvolucion,  estado, cambiosManejoEpicrisis,  complicacionesEpicrisis,  diagnosticosDefinitivosEpicrisis,  diagnosticosPresuntivosEpicrisis,  fechaYResultadoExamenesDiagnosticoEpicrisis,  hallazgosEpicrisis,  infoDadaPacienteEpicrisis,  observacionesEpicrisis,  procedimientosQuirurgicosObstetricosEpicrisis,  pronosticoEpicrisis, resultadosTratamientoEpicrisis,  signosVitalesEpicrisis,  tratamientoEpicrisis) ;
	}

	/**
	 * Implementación del método que carga una evolución hospitalaria
	 * en una BD Postgresql 
	 * 
	 * @see com.princetonsa.dao.EvolucionHospitalariaDao#cargar(Connection, int)
	 */
	public ResultSetDecorator cargar(Connection con, int numeroSolicitud) throws SQLException 
	{
		return SqlBaseEvolucionHospitalariaDao.cargar(con, numeroSolicitud) ;
	}

	/**
	 * Implementación del método que modifica una evolución hospitalaria
	 * en una BD Postgresql 
	 * 
	 * @see com.princetonsa.dao.EvolucionHospitalariaDao#modificar(Connection, int, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean)
	 */
	public boolean modificar(Connection con, int numeroSolicitud, boolean cambiosManejoEpicrisis, boolean complicacionesEpicrisis, boolean diagnosticosDefinitivosEpicrisis, boolean diagnosticosPresuntivosEpicrisis, boolean fechaYResultadoExamenesDiagnosticoEpicrisis, boolean hallazgosEpicrisis, boolean infoDadaPacienteEpicrisis, boolean observacionesEpicrisis, boolean procedimientosQuirurgicosObstetricosEpicrisis, boolean pronosticoEpicrisis, boolean resultadosTratamientoEpicrisis, boolean signosVitalesEpicrisis, boolean tratamientoEpicrisis) throws SQLException 
	{
		return SqlBaseEvolucionHospitalariaDao.modificar(con, numeroSolicitud, cambiosManejoEpicrisis, complicacionesEpicrisis, diagnosticosDefinitivosEpicrisis, diagnosticosPresuntivosEpicrisis, fechaYResultadoExamenesDiagnosticoEpicrisis, hallazgosEpicrisis, infoDadaPacienteEpicrisis, observacionesEpicrisis, procedimientosQuirurgicosObstetricosEpicrisis, pronosticoEpicrisis, resultadosTratamientoEpicrisis, signosVitalesEpicrisis, tratamientoEpicrisis) ;
	}

	/**
	 * Implementación del método que modifica una evolución hospitalaria
	 * en una BD Postgresql 
	 * 
	 * @see com.princetonsa.dao.EvolucionHospitalariaDao#modificarTransaccional(Connection, int, String estado, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean)
	 */
	public boolean modificarTransaccional(Connection con, int numeroSolicitud, String estado, boolean cambiosManejoEpicrisis, boolean complicacionesEpicrisis, boolean diagnosticosDefinitivosEpicrisis, boolean diagnosticosPresuntivosEpicrisis, boolean fechaYResultadoExamenesDiagnosticoEpicrisis, boolean hallazgosEpicrisis, boolean infoDadaPacienteEpicrisis, boolean observacionesEpicrisis, boolean procedimientosQuirurgicosObstetricosEpicrisis, boolean pronosticoEpicrisis, boolean resultadosTratamientoEpicrisis, boolean signosVitalesEpicrisis, boolean tratamientoEpicrisis) throws SQLException 
	{
		return SqlBaseEvolucionHospitalariaDao.modificarTransaccional(con, numeroSolicitud, estado, cambiosManejoEpicrisis, complicacionesEpicrisis, diagnosticosDefinitivosEpicrisis, diagnosticosPresuntivosEpicrisis, fechaYResultadoExamenesDiagnosticoEpicrisis, hallazgosEpicrisis, infoDadaPacienteEpicrisis, observacionesEpicrisis, procedimientosQuirurgicosObstetricosEpicrisis, pronosticoEpicrisis, resultadosTratamientoEpicrisis, signosVitalesEpicrisis, tratamientoEpicrisis) ;
	}

}