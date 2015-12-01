/*
 * @(#)PostgresqlMotivoAnulacionFacturasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.princetonsa.dao.NotasRecuperacionDao;
import com.princetonsa.dao.sqlbase.SqlBaseNotasRecuperacionDao;
import com.servinte.axioma.dto.salascirugia.NotaRecuperacionDto;
import com.servinte.axioma.fwk.exception.BDException;


/**
 * Implementación postgresql de las funciones de acceso a la fuente de datos
 * para las notas recuperacion
 *
 * @version 1.0, Nov 23 / 2005
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
public class PostgresqlNotasRecuperacionDao implements NotasRecuperacionDao
{
	/**
	 *  Sentencia SQL para insertar el detalle de las notas de recuperacion
	 */
	private final static String insertarDetalleNotasRecuperacionStr =" INSERT INTO det_notas_recuperacion "+
																	 " (codigo, " +
																	 " numero_solicitud, " +
																	 " fecha_recuperacion, " +
																	 " hora_recuperacion, " +
																	 " nota_recup," +
																	 " valor, " +
																	 " fecha_grabacion, " +
																	 " hora_grabacion, " +
																	 " codigo_enfermera, " +
																	 " institucion )"+
																	 " VALUES (nextval('seq_not_recuperacion'),?,?,?,?,?,?,?,?,?)";
	
	
	/**
	 * Metodo para insertar el detalle de una nota de recuperacion
	 * @param con
	 * @param numeroSolicitud
	 * @param fechaRecuperacion
	 * @param horaRecuperacion
	 * @param noraRecuperacion
	 * @param valorNota
	 * @param fechaGrabacion
	 * @param horaGrabacion
	 * @param codigoEnfermera
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public int  insertarDetalleNotaRecuperacion(Connection con, int numeroSolicitud, String fechaRecuperacion, String horaRecuperacion, int notaRecuperacion, String valorNota, String fechaGrabacion, String horaGrabacion, int codigoEnfermera, int institucion) throws SQLException
	{
	    return SqlBaseNotasRecuperacionDao.insertarDetalleNotaRecuperacion(con, numeroSolicitud, fechaRecuperacion, horaRecuperacion, notaRecuperacion, valorNota, fechaGrabacion, horaGrabacion, codigoEnfermera, institucion, insertarDetalleNotasRecuperacionStr);
	}
	
	/***
	 * Método para insertar una nota de recuperacion
	 * @param con
	 * @param numeroSolicitud
	 * @param medicamentos
	 * @param observacionesGrales
	 * @return
	 */
	public int insertarNotaRecuperacion(Connection con, int numeroSolicitud, String medicamentos, String observacionesGrales) throws SQLException
	{
		return SqlBaseNotasRecuperacionDao.insertarNotaRecuperacion(con, numeroSolicitud, medicamentos, observacionesGrales);
	}
	
	
	/**
	 * Método para consultar los tipos de notas de recuperacion parametrizados a la institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public  HashMap consultarTiposNotasRecuperacion (Connection con, int institucion,  int numeroSolicitud) throws SQLException
	{
		return SqlBaseNotasRecuperacionDao.consultarTiposNotasRecuperacion(con, institucion, numeroSolicitud );
	}
	
	/**
	 * Método para consultar las obervaciones de una nota de recuperacion segun el
	 * número de solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * @throws SQLException
	 */
	public String consultarObservacionesGenerales( Connection con, int numeroSolicitud) throws SQLException
	{
		return SqlBaseNotasRecuperacionDao.consultarObservacionesGenerales(con, numeroSolicitud);
	}
	
	/**
	 * Método para consultar los medicamentos ingresados en una nota
	 * segun el número de solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * @throws SQLException
	 */
	public String consultarMedicamentosGenerales( Connection con, int numeroSolicitud) throws SQLException
	{
		return SqlBaseNotasRecuperacionDao.consultarMedicamentosGenerales(con, numeroSolicitud); 
	}
	
	/**
	 * Métodod para consultar la fecha - hora de todas las notas de recuperacion
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarFechaRecuperacion (Connection con, int numeroSolicitud) throws SQLException
	{
		return SqlBaseNotasRecuperacionDao.consultarFechaRecuperacion(con, numeroSolicitud);
	}

	/**
	 * Metodo que carga estructura e historico de notas recuperacion dado un numero de Solicitud
	 * @param con
	 * @param codigoInstitucion
	 * @param numeroSolicitud
	 * @param soloEstructuraParametrizada
	 * @return listaNotasRecuperacion
	 * @throws BDException
	 */
	public List<NotaRecuperacionDto> listaNotasRecuperacion(Connection con, int codigoInstitucion, int numeroSolicitud, boolean soloEstructuraParametrizada, boolean esAscendente)throws BDException {
		return SqlBaseNotasRecuperacionDao.listaNotasRecuperacion(con, codigoInstitucion, numeroSolicitud, soloEstructuraParametrizada, esAscendente);
	}

	/**
	 * Metodo que Guarda nota de recuperacion general, especifica para fecha y detalle de la nota
	 * @param con
	 * @param dto NotaRecuperacionDto
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 03/07/2013
	 */
	public void guardarNotaRecuperacion(Connection con, NotaRecuperacionDto dto) throws BDException {
		SqlBaseNotasRecuperacionDao.guardarNotaRecuperacion(con, dto);	
	}
}
