/*
 * @(#)ConsultaReferenciaContrareferenciaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */
package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.mundo.historiaClinica.ConsultaReferenciaContrareferencia;

/**
 * 
 * @author Wilson Rios
 * wrios@princetonsa.com
 */
public interface ConsultaReferenciaContrareferenciaDao 
{
	/**
	 * 
	 * @param con
	 * @param criteriosBusquedaMap
	 * @param esBusquedaXpaciente
	 * @return
	 */
	public HashMap busquedaReferenciaContrareferencia(Connection con, ConsultaReferenciaContrareferencia mundo);
}
