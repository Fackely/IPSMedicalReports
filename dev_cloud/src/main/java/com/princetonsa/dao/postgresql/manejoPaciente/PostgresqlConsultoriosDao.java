/*
 * @(#)PostgresqlConsultoriosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */
package com.princetonsa.dao.postgresql.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.ConsultoriosDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseConsultoriosDao;
import com.princetonsa.dto.consultaExterna.DtoConsultorios;
import com.princetonsa.dto.odontologia.DtoAgendaOdontologica;
import com.princetonsa.mundo.manejoPaciente.Consultorios;

/**
 * 
 * @author Wilson Rios
 * wrios@princetonsa.com
 */
public class PostgresqlConsultoriosDao implements ConsultoriosDao 
{
	/**
	 * Consulta los n consultorios x centro atencion 
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public HashMap consultoriosXCentroAtencion(Connection con, int centroAtencion)
	{
		return SqlBaseConsultoriosDao.consultoriosXCentroAtencion(con, centroAtencion); 
	}
	
	/**
	 * 
	 * @param con
	 * @param string
	 * @return
	 */
	public boolean eliminarRegistro(Connection con, int codigo)
	{
		return SqlBaseConsultoriosDao.eliminarRegistro(con, codigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public HashMap consultarConsultorio(Connection con, int codigo)
	{
		return SqlBaseConsultoriosDao.consultarConsultorio(con, codigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public boolean insertar(Connection con, Consultorios consultorio)
	{
		return SqlBaseConsultoriosDao.insertar(con, consultorio);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public boolean modificar(Connection con, Consultorios consultorio)
	{
		return SqlBaseConsultoriosDao.modificar(con, consultorio);
	}

	@Override
	public ArrayList<DtoConsultorios> listarConsultoriosParaGenerarAgenda(
			DtoAgendaOdontologica dtoAgenda)
	{
		return SqlBaseConsultoriosDao.listarConsultoriosParaGenerarAgenda(dtoAgenda);
	}

	@Override
	public void listarConsultoriosParaGenerarAgendaSinConsultorio(DtoAgendaOdontologica dtoAgenda)
	{
		SqlBaseConsultoriosDao.listarConsultoriosParaGenerarAgendaSinConsultorio(dtoAgenda);
	}
}
