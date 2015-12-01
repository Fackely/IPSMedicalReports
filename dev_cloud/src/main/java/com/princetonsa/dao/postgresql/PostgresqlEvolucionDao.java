/*
 * @(#)PostgresqlEvolucionDao.java
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
import java.util.HashMap;
import java.util.Vector;

import com.princetonsa.dao.EvolucionDao;
import com.princetonsa.dao.sqlbase.SqlBaseEvolucionDao;

/**
 * Esta clase implementa el contrato estipulado en <code>EvolucionDao</code>, proporcionando los servicios
 * de acceso a una base de datos PostgreSQL requeridos por la clase <code>Evolucion</code>
 *
 * @version May 26, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public class PostgresqlEvolucionDao implements EvolucionDao 
{


	/**
	 * Nombre de la secuencia que maneja la evolución
	 */
	private static final String nombreSecuencia="seq_evolucion";
	
	/**
	 * @see com.princetonsa.dao.EvolucionDao#int insertar(Connection , int , int , boolean , String , int , String , String , String , String , String , String , String , String , String , String , String , String , String , String , boolean , Collection , Collection , Collection , int , int ) throws SQLException
	 */
	public int insertar(Connection con, boolean cobrable, String diagnosticoComplicacion, int diagnosticoComplicacionCIE, String fechaEvolucion, String horaEvolucion, String informacionDadaPaciente, String descripcionComplicacion, String tratamiento, String resultadosTratamiento, String cambiosManejo, String hallazgosImportantes, String procedimientosQuirurgicosObstetricos, String fechaYResultadoExamenesDiagnostico, String pronostico, String observaciones, int codigoMedico, boolean ordenSalida, Collection diagnosticosPresuntivos, Collection diagnosticosDefinitivos, Collection signosVitales, int tipoEvolucion, int codigoValoracion, int recargo, int tipoDiagnosticoPrincipal, String datosMedico, int centroCostoAreaPaciente, int codigoConductaASeguir, String acronimoTipoReferencia) throws SQLException 
	{
		return SqlBaseEvolucionDao.insertar(con, cobrable, diagnosticoComplicacion, diagnosticoComplicacionCIE, fechaEvolucion, horaEvolucion, informacionDadaPaciente, descripcionComplicacion, tratamiento, resultadosTratamiento, cambiosManejo, hallazgosImportantes, procedimientosQuirurgicosObstetricos, fechaYResultadoExamenesDiagnostico, pronostico, observaciones, codigoMedico, ordenSalida, diagnosticosPresuntivos, diagnosticosDefinitivos, signosVitales, tipoEvolucion, codigoValoracion, recargo, tipoDiagnosticoPrincipal, datosMedico, centroCostoAreaPaciente, codigoConductaASeguir, acronimoTipoReferencia) ;
	}

	/**
	 * @see com.princetonsa.dao.EvolucionDao#insertarTransaccional(Connection , String , int , int , boolean , String , int , String , String , String , String , String , String , String , String , String , String , String , String , String , String , boolean , Collection , Collection , Collection , int ) throws SQLException 
	 */
	public int insertarTransaccional(Connection con, String estado, boolean cobrable, String diagnosticoComplicacion, int diagnosticoComplicacionCIE, String fechaEvolucion, String horaEvolucion, String informacionDadaPaciente, String descripcionComplicacion, String tratamiento, String resultadosTratamiento, String cambiosManejo, String hallazgosImportantes, String procedimientosQuirurgicosObstetricos, String fechaYResultadoExamenesDiagnostico, String pronostico, String observaciones, int codigoMedico, boolean ordenSalida, Collection diagnosticosPresuntivos, Collection diagnosticosDefinitivos, Collection signosVitales, int tipoEvolucion, int codigoValoracion, int recargo, int tipoDiagnosticoPrincipal, String datosMedico, Vector balanceLiquidos, int centroCostoAreaPaciente, int codigoConductaASeguir, String acronimoTipoReferencia) throws SQLException 
	{
		return SqlBaseEvolucionDao.insertarTransaccional(con, estado, cobrable, diagnosticoComplicacion, diagnosticoComplicacionCIE, fechaEvolucion, horaEvolucion, informacionDadaPaciente, descripcionComplicacion, tratamiento, resultadosTratamiento, cambiosManejo, hallazgosImportantes, procedimientosQuirurgicosObstetricos, fechaYResultadoExamenesDiagnostico, pronostico, observaciones, codigoMedico, ordenSalida, diagnosticosPresuntivos, diagnosticosDefinitivos, signosVitales, tipoEvolucion, codigoValoracion, recargo, nombreSecuencia, tipoDiagnosticoPrincipal, datosMedico, balanceLiquidos, centroCostoAreaPaciente, codigoConductaASeguir, acronimoTipoReferencia) ; 
	}

	/**
	 * @see com.princetonsa.dao.EvolucionDao#cargarEvolucionBase(Connection, int)
	 */
	public ResultSetDecorator cargarEvolucionBase(Connection con, int codigo) throws SQLException {
		return SqlBaseEvolucionDao.cargarEvolucionBase(con, codigo) ;
	}

	/**
	 * @see com.princetonsa.dao.EvolucionDao#cargarEvolucionDiagnosticos(Connection, int)
	 */
	public ResultSetDecorator cargarEvolucionDiagnosticos(Connection con, int codigo) throws SQLException {
		return SqlBaseEvolucionDao.cargarEvolucionDiagnosticos(con, codigo) ;
	}

	/**
	 * @see com.princetonsa.dao.EvolucionDao#cargarEvolucionSignosVitales(Connection, int)
	 */
	public ResultSetDecorator cargarEvolucionSignosVitales(Connection con, int codigo) throws SQLException {
		return SqlBaseEvolucionDao.cargarEvolucionSignosVitales(con, codigo) ;
	}

	/**
	 * @see com.princetonsa.dao.EvolucionDao#consultarFechaEvolucionSalida(Connection, int)
	 */	
	public ResultSetDecorator consultarFechaEvolucionSalida(Connection con, int numeroCuenta) throws SQLException {
		return SqlBaseEvolucionDao.consultarFechaEvolucionSalida(con, numeroCuenta) ;
	}

	/**
	 * Implementación de la consulta de datos basicos de las evoluciones que estan asociadas al mismo numero de cuenta
	 * @see com.princetonsa.dao.EvolucionDao#getBasicoEvolucionesRespondidas (con, int ) throws SQLException
	 * @version Sept. 11 de 2003
	 */
	 
	public ResultSetDecorator getBasicoEvolucionesRespondidas(Connection con, int numeroCuenta) throws SQLException
	{		                                             
		return SqlBaseEvolucionDao.getBasicoEvolucionesRespondidas(con, numeroCuenta) ;
	}
	
	/**
	 * Implementación de la consulta del numero de cuenta asociado a la evolucion con numero de solicitud dado.
	 * @see com.princetonsa.dao.EvolucionDao#getIdCuenta (con, int ) throws SQLException
	 * @version Sept. 24 de 2003
	 */	
	public int getIdCuenta(Connection con, int codigo) throws SQLException
	{
		return SqlBaseEvolucionDao.getIdCuenta(con, codigo) ;
	}	
	
	/**
	 * Implementación del método que revisa si esta es la 
	 * primera evolución que llenó el usuario después que
	 * se cancelo una solicitud de cambio de tratante para
	 * una BD Postgresql
	 *
	 * @see com.princetonsa.dao.EvolucionDao#deboMostrarCancelacionTratante (Connection , int ) throws SQLException
	 */
	public boolean deboMostrarCancelacionTratante (Connection con, int idEvolucion) throws SQLException
	{
	    return SqlBaseEvolucionDao.deboMostrarCancelacionTratante (con, idEvolucion) ;
	}
	
	/**
	 * Método para cargar el balance de liquidos de la evolución
	 * @param con
	 * @param codigo
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarEvolucionBalanceLiquidos(Connection con, int codigo) throws SQLException
	{
		return SqlBaseEvolucionDao.cargarEvolucionBalanceLiquidos(con, codigo);
	}

	/**
	 * mapa que carga la informacion de conducta a seguir de la utltinma evolucion insertada,
	 * en caso de no encontrar informacion retorna null
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public HashMap conductaASeguirUltimaInsertada(Connection con, int idCuenta)
	{
		return SqlBaseEvolucionDao.conductaASeguirUltimaInsertada(con, idCuenta);
	}
	
	/**
	 * mapa que contiene las conductas a seguir
	 * @param con
	 * @return
	 */
	public HashMap conductasASeguirMap(Connection con)
	{
		return SqlBaseEvolucionDao.conductasASeguirMap(con);
	}

	/**
	 * 
	 */
	public HashMap conductaASeguirDadaEvol(Connection con, int codigoEvolucion) 
	{
		return SqlBaseEvolucionDao.conductaASeguirDadaEvol(con, codigoEvolucion);
	}
	
	/**
	 * Metodo para Consultar el Parametro General de Controla Interpretacion
	 */
	public String consultarParametroControlaInterpretacion(Connection con) 
	{
		return SqlBaseEvolucionDao.consultarParametroInterpretacion(con);
	}

	/**
	 * Metodo para Consultar las Especialidades de la Cuenta
	 */
	public HashMap consultarEspecialidadesCuenta(Connection con, int codigoCuenta) 
	{
		return SqlBaseEvolucionDao.consultarEspecialidadesCuenta(con, codigoCuenta);
	}
	
	
	
	/**
	 * Metodo para validar las especialidades del medico que solicita al que evoluciona
	 * @param con
	 * @param codigoMedicoEvo
	 * @param codigoMedicoSol
	 * @return
	 */
	public HashMap validarEspecialidadesMedico(Connection con, int codigoMedicoEvo, int codigoMedicoSol) { 
	
		return SqlBaseEvolucionDao.validarEspecialidadesMedico(con, codigoMedicoEvo, codigoMedicoSol);
	}
	
	
}