/*
 * @(#)OracleNotasGeneralesEnfermeriaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.princetonsa.dao.NotasGeneralesEnfermeriaDao;
import com.princetonsa.dao.sqlbase.SqlBaseNotasGeneralesEnfermeriaDao;
import com.princetonsa.decorator.ResultSetDecorator;
import com.servinte.axioma.dto.salascirugia.NotaEnfermeriaDto;
import com.servinte.axioma.fwk.exception.BDException;


/**
 * Implementación Oracle de las funciones de acceso a la fuente de datos
 * para las notas generales de enfermeria
 *
 * @version 1.0, Nov 03 / 2005
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
public class OracleNotasGeneralesEnfermeriaDao implements NotasGeneralesEnfermeriaDao
{
	
	
	/**
	 *  Insertar ununa general de enfermeria
	 */
	private final static String insertarNotaStr = " INSERT INTO notas_enfermeria " +
												  " (codigo, numero_solicitud, fecha_nota, hora_nota, nota, codigo_enfermera, institucion, fecha_grabacion, hora_grabacion) " +
												  " VALUES (seq_not_enfermeria.nextval, ?, ?, ?, ?, ?, ?, ?, ? )" ;
	
	/**
	 * Método para inserta una nota
	 * @param con
	 * @param numeroSolicitud
	 * @param fechaNota
	 * @param horaNota
	 * @param nota
	 * @param codigoEnfermera
	 * @param institucion
	 * @param fechaGrabacion
	 * @param horaGrabacion
	 * @return
	 */
	public int  insertarNota(Connection con, int numeroSolicitud, String fechaNota, String horaNota, String nota, int codigoEnfermera, int institucion, String fechaGrabacion, String horaGrabacion)
	{
	    return SqlBaseNotasGeneralesEnfermeriaDao.insertarNota(con, numeroSolicitud, fechaNota, horaNota, nota, codigoEnfermera, institucion, fechaGrabacion, horaGrabacion, insertarNotaStr);
	}
	
	/**
	 * Método para cargar todas las notas asociadas a un solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarNotasPaciente(Connection con, int numeroSolicitud)  throws SQLException
	{
		return SqlBaseNotasGeneralesEnfermeriaDao.cargarNotasPaciente(con, numeroSolicitud);
	}
	
	/**
	 * Metodo que carga una lista de Notas enfermeria a partir de un numero de Solicitud dado
	 * @param con
	 * @param numeroSolicitud
	 * @return listaNotasEnfermeria
	 * @throws SQLException
	 */
	public List<NotaEnfermeriaDto> listaNotasEnfermeria(Connection con, Integer numeroSolicitud, boolean esAscendente) throws BDException {
		return SqlBaseNotasGeneralesEnfermeriaDao.listaNotasEnfermeria(con, numeroSolicitud, esAscendente);
	}

	/**
	 * Permite persistir la informacion de una nota enfermeria
	 * @param con
	 * @param dto NotaEnfermeriaDto
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 03/07/2013
	 */
	public void guardarNotaEnfermeria(Connection con, NotaEnfermeriaDto dto) throws BDException {
		SqlBaseNotasGeneralesEnfermeriaDao.guardarNotaEnfermeria(con, dto);
	}
	
}
