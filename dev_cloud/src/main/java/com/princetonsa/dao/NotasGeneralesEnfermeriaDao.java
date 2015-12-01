/*
 * @(#)NotasGeneralesEnfermeriaDao.java
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
import java.util.List;

import com.princetonsa.decorator.ResultSetDecorator;
import com.servinte.axioma.dto.salascirugia.NotaEnfermeriaDto;
import com.servinte.axioma.fwk.exception.BDException;

/**
 *  Interfaz para el acceder a la fuente de datos de las notas generales
 *  de enfermeria
 *
 * @version 1.0, Nov 03 / 2005
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
public interface NotasGeneralesEnfermeriaDao 
{
	/**
	 * Método para cargar todas las notas asociadas a un solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarNotasPaciente(Connection con, int numeroSolicitud)  throws SQLException;
	/**
	 * Carga una lista de Notas enfermeria a partir de un numero de Solicitud dado
	 * @param con
	 * @param numeroSolicitud
	 * @return listaNotasEnfermeria
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 03/07/2013
	 */
	public List<NotaEnfermeriaDto> listaNotasEnfermeria(Connection con,Integer numeroSolicitud, boolean esAscendente) throws BDException;
	
	/**
	 * Permite persistir la informacion de una nota enfermeria
	 * @param con
	 * @param dto NotaEnfermeriaDto
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 03/07/2013
	 */
	void guardarNotaEnfermeria(Connection con, NotaEnfermeriaDto dto)throws BDException;
	
	/**
	 * Método para insertar una nota
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
	public int  insertarNota(Connection con, int numeroSolicitud, String fechaNota, String horaNota, String nota, int codigoEnfermera, int institucion, String fechaGrabacion, String horaGrabacion);
	
	
	
}
