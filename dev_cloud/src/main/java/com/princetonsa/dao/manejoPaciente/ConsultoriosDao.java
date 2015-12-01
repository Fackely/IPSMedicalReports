/*
 * @(#)ConsultoriosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */
package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.consultaExterna.DtoConsultorios;
import com.princetonsa.dto.odontologia.DtoAgendaOdontologica;
import com.princetonsa.mundo.manejoPaciente.Consultorios;

/**
 * 
 * @author Wilson Rios
 * wrios@princetonsa.com
 */
public interface ConsultoriosDao 
{
	/**
	 * Consulta los n consultorios x centro atencion 
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public HashMap consultoriosXCentroAtencion(Connection con, int centroAtencion);

	/**
	 * 
	 * @param con
	 * @param string
	 * @return
	 */
	public boolean eliminarRegistro(Connection con, int codigo);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public HashMap consultarConsultorio(Connection con, int codigo);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public boolean insertar(Connection con, Consultorios consultorio);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public boolean modificar(Connection con, Consultorios consultorio);

	/**
	 * Lista los consultorios asociados a los horarios de atención que
	 * concuerden con los parámetros de búsqueda enviados
	 * @param dtoAgenda Dto con los datos de la agenda
	 * @return Lista de consultorios que cumplan las condiciones
	 */
	public ArrayList<DtoConsultorios> listarConsultoriosParaGenerarAgenda(DtoAgendaOdontologica dtoAgenda);

	/**
	 * Lista los consultorios que no se encuentren asociados a los horarios de atención
	 * que concuerden con los parámetros de búsqueda enviados
	 * Los consultorios se asignan en el atributo listConsultoriosXGenerar
	 * @param dtoAgenda DTO con los datos de la agenda
	 */
	public void listarConsultoriosParaGenerarAgendaSinConsultorio(DtoAgendaOdontologica dtoAgenda);
	
}
