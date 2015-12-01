/*
 * @(#)ListadoCitasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004 Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import util.ResultadoCollectionDB;

/**
 * Interfaz para acceder a la fuente de datos los listados definidos de citas 
 *
 * @version 1.0, Marzo 25 / 2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */

public interface ListadoCitasDao
{
	/**
	 * 
	 * @param con
	 * @param codMedico
	 * @param fecha
	 * @param centroAtencion
	 * @param institucion
	 * @return
	 */
	public HashMap listarCitasPorAtenderMedico(	Connection con,HashMap campos);
																									
	/**
	 * 
	 * @param con
	 * @param codPaciente
	 * @param codMedico
	 * @param fechaInico
	 * @param fechaFin
	 * @param horaInicio
	 * @param horaFin
	 * @param unidadConsulta
	 * @param estadoLiquidacion
	 * @param consultorio
	 * @param estadosCita
	 * @param centroAtencion
	 * @return
	 * @throws SQLException
	 */
	public ResultadoCollectionDB listarCitasPorPaciente(Connection con,int codPaciente,int codMedico,String fechaInico, String fechaFin,String horaInicio,String horaFin,int unidadConsulta, String estadoLiquidacion,int consultorio,String[] estadosCita, String centroAtencion,String postO,String tipoOrdenamiento) throws SQLException;
	
	/**
	 * 
	 * @param con
	 * @param codPaciente
	 * @param idCuenta
	 * @return
	 * @throws SQLException
	 */
	public ResultadoCollectionDB listarCitas(Connection con,int codPaciente, int idCuenta) throws SQLException ;
	
	
	/**
	 * Consulta los servicios asociados a la cita 
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap serviciosCita(Connection con, HashMap parametros);
	
	/**
	 * Método implementado para obtener le fecha de la primera cita del paciente
	 * en estado asignada o reservada
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public String obtenerFechaPrimeraCitaPaciente(Connection con,int codigoPaciente);
	
}