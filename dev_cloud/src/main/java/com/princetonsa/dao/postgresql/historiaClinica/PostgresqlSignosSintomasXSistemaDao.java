/*
 * @(#)PostgresqlSignosSintomasXSistemaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.postgresql.historiaClinica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.SignosSintomasXSistemaDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseSignosSintomasXSistemaDao;

/**
 * Implementación postgres de las funciones de acceso a la fuente de datos
 * para signos sintomas x sistema
 *
 * @version 1.0, Jun 1 / 2006
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class PostgresqlSignosSintomasXSistemaDao implements SignosSintomasXSistemaDao
{
	/**
	 * sentencia para insertar
	 */
	private static String insertarSql="insert into signos_sintomas_x_sistema (codigo, cod_mot_consulta_urg, consecutivo_signos_sintomas, consecutivo_categoria_triage, institucion) values(nextval('seq_signos_sintomas_sist'), ?, ?, ?, ?) ";
	
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
						 )
	{
		return SqlBaseSignosSintomasXSistemaDao.listado(con, codigoMotivoConsulta, codigoInstitucion);
	}

	/**
	 * Elimina
	 * @param con
	 * @param estado
	 * @return
	 * @throws SQLException
	 */
	public int eliminar (Connection con, String codigoPK)
	{
		return SqlBaseSignosSintomasXSistemaDao.eliminar(con, codigoPK);
	}
	
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
	public boolean existeRegistro(Connection con,
										 String codigoConsultaMotivo,
										 String consecutivoSignoSintoma,
										 String consecutivoCalificacionTriage,
										 int codigoInstitucion,
										 String codigoPK)
	{
		return SqlBaseSignosSintomasXSistemaDao.existeRegistro(con, codigoConsultaMotivo, consecutivoSignoSintoma, consecutivoCalificacionTriage, codigoInstitucion, codigoPK);
	}
	
	/**
	 * Modifica
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
									String codigoPK)
	{
		return SqlBaseSignosSintomasXSistemaDao.modificar(con, codigoConsultaMotivo, consecutivoSignoSintoma, consecutivoCalificacionTriage, codigoInstitucion, codigoPK);
	}
	
	/**
	 * inserta
	 * @param con
	 * @param codigoConsultaMotivo
	 * @param consecutivoSignoSintoma
	 * @param consecutivoCalificacionTriage
	 * @param codigoInstitucion
	 * @param insertarSql
	 * @return
	 */
	public boolean insertar (	Connection con, 
										String codigoConsultaMotivo,
										String consecutivoSignoSintoma,
										String consecutivoCalificacionTriage,
										int codigoInstitucion)
	{
		return SqlBaseSignosSintomasXSistemaDao.insertar(con, codigoConsultaMotivo, consecutivoSignoSintoma, consecutivoCalificacionTriage, codigoInstitucion, insertarSql);
	}
}
