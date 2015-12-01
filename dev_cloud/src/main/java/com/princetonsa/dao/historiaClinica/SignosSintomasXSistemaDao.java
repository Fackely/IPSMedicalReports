/*
 * @(#)SignosSintomasXSistemaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 *  Interfaz para el acceder a la fuente de datos de signos sintomas x sistema
 *
 * @version 1.0, Jun 1 / 2006
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public interface SignosSintomasXSistemaDao 
{
	/**
	 * Consulta la info de los asocios x uvr 
	 * @param con
	 * @param codigoMotivoConsulta
	 * @param codigoInstitucion
	 * @param
	 * @return
	 */
	public HashMap listado(	Connection con, 
	       					String codigoMotivoConsulta,
	       					int codigoInstitucion
						  );
	
	/**
	 * Elimina
	 * @param con
	 * @param estado
	 * @return
	 * @throws SQLException
	 */
	public int eliminar (Connection con, String codigoPK); 
	
	/**
	 * evalua si existe un registro unique o no
	 * @param con
	 * @param codigoConsultaMotivo
	 * @param consecutivoSignoSintoma
	 * @param consecutivoCalificacionTriage
	 * @param codigoInstitucion
	 * @param codigoPK
	 * @return
	 */
	public boolean existeRegistro(	Connection con,
								 	String codigoConsultaMotivo,
									String consecutivoSignoSintoma,
									String consecutivoCalificacionTriage,
									int codigoInstitucion,
									String codigoPK);
	
	/**
	 * Elimina
	 * @param con
	 * @param estado
	 * @return
	 * @throws SQLException
	 */
	public int modificar (	Connection con, 
									String codigoConsultaMotivo,
									String consecutivoSignoSintoma,
									String consecutivoCalificacionTriage,
									int codigoInstitucion,
									String codigoPK);
	
	/**
	 * inserta
	 * @param con
	 * @param codigoConsultaMotivo
	 * @param consecutivoSignoSintoma
	 * @param consecutivoCalificacionTriage
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean insertar (	Connection con, 
								String codigoConsultaMotivo,
								String consecutivoSignoSintoma,
								String consecutivoCalificacionTriage,
								int codigoInstitucion
								);
	
}