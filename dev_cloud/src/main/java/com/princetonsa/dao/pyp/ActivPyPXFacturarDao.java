/*
 * @(#)ActivPyPXFacturarDao
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.pyp;

import java.sql.Connection;
import java.util.HashMap;

/**
 *  Interfaz para el acceder a la fuente de datos 
 *
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public interface ActivPyPXFacturarDao
{
	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @param codigoCentroAtencion
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap listado(	Connection con,
									int codigoConvenio,
									int codigoCentroAtencion,
	        						int codigoInstitucion
								 );
}
