/*
 * @(#)HistoricoEvolucionesDao.java
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
import java.util.Collection;

/**
 * Esta clase define el contrato de operaciones que debe implementar
 * un objeto que preste servicio de acceso de BD a <code>HistoricoEvoluciones</code>.
 *
 * @version Jun 10, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public interface HistoricoAdmisionesDao 
{
	
	/**
	 * Carga un conjunto de evoluciones (dado su codigo de tipo, hospitalizacion
	 * urgencias ... definidos en la clase Evolucion)desde la fuente de datos,
	 * dado el paciente al que pertenecen.
	 * 
	 * @param con conexión abierta con la fuente de datos
	 * @param codigoTipoIdentificacionPaciente Código del tipo de identificación
	 * del paciente al que se quieren sacar las evoluciones
	 * @param numeroIdentificacionPaciente Número de identificación
	 * del paciente al que se quieren sacar las evoluciones 
	 * @param codigoTipoEvolucion Código del tipo de evolución 
	 * @return
	 * @throws SQLException
	 */
	public Collection cargarAdmisionesHospitalarias(Connection con, String numeroIdPaciente, String tipoIdPaciente, int codigoInstitucion) throws SQLException;
	public Collection cargarAdmisionesUrgencias(Connection con, String numeroIdPaciente, String tipoIdPaciente, int codigoInstitucion) throws SQLException;
}