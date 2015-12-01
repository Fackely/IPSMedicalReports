/*
 * @(#)SqlBaseEvolucionHospitalariaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import util.ConstantesBD;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a EvolucionHospitalaria
 *
 *	@version 1.0, Apr 2, 2004
 */
public class SqlBaseEvolucionHospitalariaDao  
{
	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar una entrada en la tabla de evolu_hospitalarias.
	 */
	private static final String insertarEvolucionHospitalariaStr = "INSERT INTO evolu_hospitalarias (evolucion, info_dada_pac_epicrisis, signos_vitales_epicrisis, hallazgos_epicrisis, complicaciones_epicrisis, diag_presuntivos_epicrisis, diag_definitivos_epicrisis, tratamiento_epicrisis, resultados_trat_epicrisis, cambios_manejo_epicrisis, proced_quirurgicos_obst_ep, resultado_examenes_diag_ep, pronostico_epicrisis, observaciones_epicrisis, va_a_epicrisis) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + ValoresPorDefecto.getValorTrueParaConsultas() + ")";

	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar una entrada desde la tabla de evolu_hospitalarias.
	 */
	private static final String cargarEvolucionHospitalariaStr = "SELECT * FROM evolu_hospitalarias WHERE evolucion = ?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para modificar una entrada en la tabla de evolu_hospitalarias.
	 */
	private static final String modificarEvolucionHospitalariaStr = "UPDATE evolu_hospitalarias SET info_dada_pac_epicrisis = ?, signos_vitales_epicrisis = ?, hallazgos_epicrisis = ?, complicaciones_epicrisis = ?, diag_presuntivos_epicrisis = ?, diag_definitivos_epicrisis = ?, tratamiento_epicrisis = ?, resultados_trat_epicrisis = ?, cambios_manejo_epicrisis = ?, proced_quirurgicos_obst_ep = ?, resultado_examenes_diag_ep = ?, pronostico_epicrisis = ?, observaciones_epicrisis = ? WHERE evolucion = ?";

	/**
	 * Implementación del método que inserta una evolución hospitalaria
	 * en una BD Genérica 
	 * 
	 * @see com.princetonsa.dao.EvolucionHospitalariaDao#insertar(Connection , int , boolean , boolean , boolean , boolean , boolean , boolean , boolean , boolean , boolean , boolean , boolean , boolean , boolean ) 
	 */
	public static int insertar(Connection con, int codigoEvolucion, boolean cambiosManejoEpicrisis, boolean complicacionesEpicrisis, boolean diagnosticosDefinitivosEpicrisis, boolean diagnosticosPresuntivosEpicrisis, boolean fechaYResultadoExamenesDiagnosticoEpicrisis, boolean hallazgosEpicrisis, boolean infoDadaPacienteEpicrisis, boolean observacionesEpicrisis, boolean procedimientosQuirurgicosObstetricosEpicrisis, boolean pronosticoEpicrisis, boolean resultadosTratamientoEpicrisis, boolean signosVitalesEpicrisis, boolean tratamientoEpicrisis) throws SQLException
	{

		int resp = 0;
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		resp=insertarTransaccional (con, codigoEvolucion, ConstantesBD.inicioTransaccion, cambiosManejoEpicrisis, complicacionesEpicrisis, diagnosticosDefinitivosEpicrisis, diagnosticosPresuntivosEpicrisis, fechaYResultadoExamenesDiagnosticoEpicrisis, hallazgosEpicrisis, infoDadaPacienteEpicrisis, observacionesEpicrisis, procedimientosQuirurgicosObstetricosEpicrisis, pronosticoEpicrisis, resultadosTratamientoEpicrisis, signosVitalesEpicrisis, tratamientoEpicrisis);
		if (resp >0)
		{
		    myFactory.endTransaction(con);
		    return resp; 
		}
		else
		{
		    myFactory.abortTransaction(con);
		    return resp;
		}
	}

	/**
	 * Implementación del método que inserta una evolución hospitalaria, 
	 * soportando la definición de transaccionalidad en una BD Genérica 
	 * 
	 * @see com.princetonsa.dao.EvolucionHospitalariaDao#insertarTransaccional (Connection , int , String , boolean , boolean , boolean , boolean , boolean , boolean , boolean , boolean , boolean , boolean , boolean , boolean , boolean ) throws SQLException 
	 */
	public static int insertarTransaccional (Connection con, int codigoEvolucion, String estado, boolean cambiosManejoEpicrisis, boolean complicacionesEpicrisis, boolean diagnosticosDefinitivosEpicrisis, boolean diagnosticosPresuntivosEpicrisis, boolean fechaYResultadoExamenesDiagnosticoEpicrisis, boolean hallazgosEpicrisis, boolean infoDadaPacienteEpicrisis, boolean observacionesEpicrisis, boolean procedimientosQuirurgicosObstetricosEpicrisis, boolean pronosticoEpicrisis, boolean resultadosTratamientoEpicrisis, boolean signosVitalesEpicrisis, boolean tratamientoEpicrisis) throws SQLException 
	{
		int resp = 0;
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		boolean resp0 = false, resp1 = false;  // Estos valores son true si todo salió bien

		/* Iniciamos la transacción */
		if(estado.equals("empezar")) {
			resp0 = myFactory.beginTransaction(con);
		}
		else {
			resp0 = true;
		}

		/* Inserción de datos en evolu_hospitalarias */
		PreparedStatementDecorator insertarEvolucionHospitalaria =  new PreparedStatementDecorator(con.prepareStatement(insertarEvolucionHospitalariaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		insertarEvolucionHospitalaria.setInt(1, codigoEvolucion);
		insertarEvolucionHospitalaria.setBoolean(2, infoDadaPacienteEpicrisis);
		insertarEvolucionHospitalaria.setBoolean(3, signosVitalesEpicrisis);
		insertarEvolucionHospitalaria.setBoolean(4, hallazgosEpicrisis);
		insertarEvolucionHospitalaria.setBoolean(5, complicacionesEpicrisis);
		insertarEvolucionHospitalaria.setBoolean(6, diagnosticosPresuntivosEpicrisis);
		insertarEvolucionHospitalaria.setBoolean(7, diagnosticosDefinitivosEpicrisis);
		insertarEvolucionHospitalaria.setBoolean(8, tratamientoEpicrisis);
		insertarEvolucionHospitalaria.setBoolean(9, resultadosTratamientoEpicrisis);
		insertarEvolucionHospitalaria.setBoolean(10, cambiosManejoEpicrisis);
		insertarEvolucionHospitalaria.setBoolean(11, procedimientosQuirurgicosObstetricosEpicrisis);
		insertarEvolucionHospitalaria.setBoolean(12, fechaYResultadoExamenesDiagnosticoEpicrisis);
		insertarEvolucionHospitalaria.setBoolean(13, pronosticoEpicrisis);
		insertarEvolucionHospitalaria.setBoolean(14, observacionesEpicrisis);

		try {
			resp1 = (insertarEvolucionHospitalaria.executeUpdate() > 0);
		}	catch (SQLException sqle) {
				myFactory.abortTransaction(con);
				throw sqle;
		}

		/* Terminamos la transacción, bien sea con un commit o un rollback */
		if (resp0 && resp1 && codigoEvolucion > 0) {
			if (estado.equals("finalizar")) {
				myFactory.endTransaction(con);
			}
			resp = codigoEvolucion;
		}

		else {
			if (estado.equals("finalizar")) {
				myFactory.abortTransaction(con);
			}
			resp = 0;
		}

		/* Retornamos el número de la solicitud de evolución, o 0 si se hizo rollback */
		return resp;
	}

	/**
	 * Implementación del método que carga una evolución hospitalaria
	 * en una BD Genérica 
	 * 
	 * @see com.princetonsa.dao.EvolucionHospitalariaDao#cargar(Connection, int)
	 */
	public static ResultSetDecorator cargar(Connection con, int numeroSolicitud) throws SQLException 
	{
		PreparedStatementDecorator cargar =  new PreparedStatementDecorator(con.prepareStatement(cargarEvolucionHospitalariaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargar.setInt(1, numeroSolicitud);
		return new ResultSetDecorator(cargar.executeQuery());
	}

	/**
	 * Implementación del método que modifica una evolución hospitalaria
	 * en una BD Genérica 
	 * 
	 * @see com.princetonsa.dao.EvolucionHospitalariaDao#modificar(Connection, int, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean)
	 */
	public static boolean modificar(Connection con, int numeroSolicitud, boolean cambiosManejoEpicrisis, boolean complicacionesEpicrisis, boolean diagnosticosDefinitivosEpicrisis, boolean diagnosticosPresuntivosEpicrisis, boolean fechaYResultadoExamenesDiagnosticoEpicrisis, boolean hallazgosEpicrisis, boolean infoDadaPacienteEpicrisis, boolean observacionesEpicrisis, boolean procedimientosQuirurgicosObstetricosEpicrisis, boolean pronosticoEpicrisis, boolean resultadosTratamientoEpicrisis, boolean signosVitalesEpicrisis, boolean tratamientoEpicrisis) throws SQLException 
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		boolean wasModified = false, resp0 = false, resp1 = false;  // Estos valores son true si todo salió bien

		/* Iniciamos la transacción */
		resp0 = myFactory.beginTransaction(con);

		/* Modificación de datos en evolu_hospitalarias */
		PreparedStatementDecorator modificarEvolucionHospitalaria =  new PreparedStatementDecorator(con.prepareStatement(modificarEvolucionHospitalariaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		modificarEvolucionHospitalaria.setBoolean(1, infoDadaPacienteEpicrisis);
		modificarEvolucionHospitalaria.setBoolean(2, signosVitalesEpicrisis);
		modificarEvolucionHospitalaria.setBoolean(3, hallazgosEpicrisis);
		modificarEvolucionHospitalaria.setBoolean(4, complicacionesEpicrisis);
		modificarEvolucionHospitalaria.setBoolean(5, diagnosticosPresuntivosEpicrisis);
		modificarEvolucionHospitalaria.setBoolean(6, diagnosticosDefinitivosEpicrisis);
		modificarEvolucionHospitalaria.setBoolean(7, tratamientoEpicrisis);
		modificarEvolucionHospitalaria.setBoolean(8, resultadosTratamientoEpicrisis);
		modificarEvolucionHospitalaria.setBoolean(9, cambiosManejoEpicrisis);
		modificarEvolucionHospitalaria.setBoolean(10, procedimientosQuirurgicosObstetricosEpicrisis);
		modificarEvolucionHospitalaria.setBoolean(11, fechaYResultadoExamenesDiagnosticoEpicrisis);
		modificarEvolucionHospitalaria.setBoolean(12, pronosticoEpicrisis);
		modificarEvolucionHospitalaria.setBoolean(13, observacionesEpicrisis);
		modificarEvolucionHospitalaria.setInt(14, numeroSolicitud);

		try {
			resp1 = (modificarEvolucionHospitalaria.executeUpdate() > 0);
		}	catch (SQLException sqle) {
				myFactory.abortTransaction(con);
				throw sqle;
		}

		/* Terminamos la transacción, bien sea con un commit o un rollback */
		if (resp0 && resp1) {
			myFactory.endTransaction(con);
			wasModified = true;
		}

		else {
			myFactory.abortTransaction(con);
			wasModified = false;
		}				

		/* Retornamos un boolean indicando si se pudo o no modificar */
		return wasModified;

	}

	/**
	 * Implementación del método que modifica una evolución hospitalaria
	 * en una BD Genérica 
	 * 
	 * @see com.princetonsa.dao.EvolucionHospitalariaDao#modificarTransaccional(Connection, int, String estado, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean)
	 */
	public static boolean modificarTransaccional(Connection con, int numeroSolicitud, String estado, boolean cambiosManejoEpicrisis, boolean complicacionesEpicrisis, boolean diagnosticosDefinitivosEpicrisis, boolean diagnosticosPresuntivosEpicrisis, boolean fechaYResultadoExamenesDiagnosticoEpicrisis, boolean hallazgosEpicrisis, boolean infoDadaPacienteEpicrisis, boolean observacionesEpicrisis, boolean procedimientosQuirurgicosObstetricosEpicrisis, boolean pronosticoEpicrisis, boolean resultadosTratamientoEpicrisis, boolean signosVitalesEpicrisis, boolean tratamientoEpicrisis) throws SQLException 
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		boolean wasModified = false, resp0 = false, resp1 = false;  // Estos valores son true si todo salió bien

		/* Iniciamos la transacción */
		if(estado.equals("empezar")) {
			resp0 = myFactory.beginTransaction(con);
		}
		else {
			resp0 = true;
		}

		/* Modificación de datos en evolu_hospitalarias */
		PreparedStatementDecorator modificarEvolucionHospitalaria =  new PreparedStatementDecorator(con.prepareStatement(modificarEvolucionHospitalariaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		modificarEvolucionHospitalaria.setBoolean(1, infoDadaPacienteEpicrisis);
		modificarEvolucionHospitalaria.setBoolean(2, signosVitalesEpicrisis);
		modificarEvolucionHospitalaria.setBoolean(3, hallazgosEpicrisis);
		modificarEvolucionHospitalaria.setBoolean(4, complicacionesEpicrisis);
		modificarEvolucionHospitalaria.setBoolean(5, diagnosticosPresuntivosEpicrisis);
		modificarEvolucionHospitalaria.setBoolean(6, diagnosticosDefinitivosEpicrisis);
		modificarEvolucionHospitalaria.setBoolean(7, tratamientoEpicrisis);
		modificarEvolucionHospitalaria.setBoolean(8, resultadosTratamientoEpicrisis);
		modificarEvolucionHospitalaria.setBoolean(9, cambiosManejoEpicrisis);
		modificarEvolucionHospitalaria.setBoolean(10, procedimientosQuirurgicosObstetricosEpicrisis);
		modificarEvolucionHospitalaria.setBoolean(11, fechaYResultadoExamenesDiagnosticoEpicrisis);
		modificarEvolucionHospitalaria.setBoolean(12, pronosticoEpicrisis);
		modificarEvolucionHospitalaria.setBoolean(13, observacionesEpicrisis);
		modificarEvolucionHospitalaria.setInt(14, numeroSolicitud);

		try {
			resp1 = (modificarEvolucionHospitalaria.executeUpdate() > 0);
		}	catch (SQLException sqle) {
				myFactory.abortTransaction(con);
				throw sqle;
		}

		/* Terminamos la transacción, bien sea con un commit o un rollback */
		if (resp0 && resp1) {
			if (estado.equals("finalizar")) {
				myFactory.endTransaction(con);
			}
			wasModified = true;
		}

		else {
			if (estado.equals("finalizar")) {
				myFactory.abortTransaction(con);
			}
			wasModified = false;
		}

		/* Retornamos un boolean indicando si se pudo o no modificar */
		return wasModified;
	}
}
