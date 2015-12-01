/*
 * @(#)ReportesEstadosCarteraDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */
package com.princetonsa.dao.cartera;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.mundo.cartera.ReportesEstadosCartera;

/**
 * 
 * @author wilson
 *
 */
public interface ReportesEstadosCarteraDao 
{	
	/**
	 * 
	 * @param mundo
	 * @return
	 */
	public String armarConsultaEstadoCarteraEvento(ReportesEstadosCartera mundo, String oldQuery);
	
	/**
	 * 
	 * @param mundo
	 * @param oldQuery
	 * @return
	 */
	public String armarConsultaTOTALESestadoCarteraEvento(ReportesEstadosCartera mundo, String oldQuery);

	/**
	 * 
	 * @param con
	 * @param query
	 * @return
	 */
	public HashMap ejecutarConsultaEstadoCarteraEvento(Connection con, String query);

}
