/*
 * @(#)NotasRecuperacionDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao;
  
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.servinte.axioma.dto.salascirugia.NotaRecuperacionDto;
import com.servinte.axioma.fwk.exception.BDException;

/**
 *  Interfaz para el acceder a la fuente de datos de las notas de recuperacion
 *  
 *
 * @version 1.0, Nov 23 / 2005
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
public interface NotasRecuperacionDao 
{
	
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
	public int  insertarDetalleNotaRecuperacion(Connection con, int numeroSolicitud, String fechaRecuperacion, String horaRecuperacion, int notaRecuperacion, String valorNota, String fechaGrabacion, String horaGrabacion, int codigoEnfermera, int institucion) throws SQLException;
	
	
	/***
	 * Método para insertar una nota de recuperacion
	 * @param con
	 * @param numeroSolicitud
	 * @param medicamentos
	 * @param observacionesGrales
	 * @return
	 */
	public int insertarNotaRecuperacion(Connection con, int numeroSolicitud, String medicamentos, String observacionesGrales)   throws SQLException;
	
	
	/**
	 * Método para consultar los tipos de notas de recuperacion parametrizados a la institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap consultarTiposNotasRecuperacion (Connection con, int institucion, int numeroSolicitud) throws SQLException;
	
	/**
	 * Método para consultar las obervaciones de una nota de recuperacion segun el
	 * número de solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * @throws SQLException
	 */
	public String consultarObservacionesGenerales( Connection con, int numeroSolicitud) throws SQLException;
	
	/**
	 * Método para consultar los medicamentos ingresados en una nota
	 * segun el número de solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * @throws SQLException
	 */
	public  String consultarMedicamentosGenerales( Connection con, int numeroSolicitud) throws SQLException;
	
	/**
	 * Métodod para consultar la fecha - hora de todas las notas de recuperacion
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarFechaRecuperacion (Connection con, int numeroSolicitud) throws SQLException;
	

	/**
	 * Metodo que carga estructura e historico de notas recuperacion dado un numero de Solicitud
	 * @param con
	 * @param codigoInstitucion
	 * @param numeroSolicitud
	 * @param soloEstructuraParametrizada
	 * @return listaNotasRecuperacion
	 * @throws BDException
	 */
	public List<NotaRecuperacionDto> listaNotasRecuperacion(Connection con, int codigoInstitucion, int numeroSolicitud, boolean soloEstructuraParametrizada, boolean esAscendente) throws BDException;
	
	/**
	 * Metodo que Guarda nota de recuperacion general, especifica para fecha y detalle de la nota
	 * @param con
	 * @param dto NotaRecuperacionDto
	 * @throws BDException
	 */
	void guardarNotaRecuperacion(Connection con, NotaRecuperacionDto dto)throws BDException;
}
