/*
 * @(#)OracleGruposDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.GruposDao;
import com.princetonsa.dao.sqlbase.SqlBaseGruposDao;

/**
 * Implementación postgresql de las funciones de acceso a la fuente de datos
 * para grupos
 *
 * @version 1.0, Septiembre  06 / 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */

public class OracleGruposDao implements GruposDao
{
  /**
	 * Método para la insercion de un grupo
	 * @param con
	 * @param grupo
	 * @param codigoAsocio
	 * @param codigoEsquemaTarifario
	 * @param codigoCups
	 * @param codigoSoat
	 * @param codigoTipoLiquidacion
	 * @param unidades
	 * @param valor
	 * @param activo
	 * @param codigoInstitucion
	 * @return
	 */
	public int insertarXesquemaTarifario(		Connection con,
														int grupo,
														int codigoAsocio,
														int codigoEsquemaTarifario,
														int codigoTipoLiquidacion,
														String unidades,
														double valor,
														int codigoInstitucion,
														String convenio,
														String tipoServicio,
														String fechaIncial,
														String fechaFinal, 
														String usuario,
														int codigoGrupo,
														boolean esquemaGeneral,
														String liquidarPor
													)
	{
	    return SqlBaseGruposDao.insertarXesquemaTarifario(con, grupo, codigoAsocio, codigoEsquemaTarifario, codigoTipoLiquidacion, unidades, valor, codigoInstitucion,convenio,tipoServicio,fechaIncial,fechaFinal,usuario, codigoGrupo, esquemaGeneral,liquidarPor);
	}

	/**
	 * Método para la modioficacion de un grupo
	 * @param con
	 * @param grupo
	 * @param codigoAsocio
	 * @param codigoEsquemaTarifario
	 * @param codigoCups
	 * @param codigoSoat
	 * @param codigoTipoLiquidacion
	 * @param unidades
	 * @param valor
	 * @param activo
	 * @param codigoPKGrupo
	 * @return
	 */
	public  boolean modificar(		Connection con,
														int grupo,
														int codigoAsocio,
														int codigoTipoLiquidacion,
														String unidades,
														double valor,
														int codigoPKGrupo,
														String tipoServicio,
														String liquidarPor
													) 
	{
	    return SqlBaseGruposDao.modificar(con, grupo, codigoAsocio, codigoTipoLiquidacion, unidades, valor, codigoPKGrupo,tipoServicio,liquidarPor);
	}

	/**
	 * Metodo para eliminar un grupo
	 * @param con
	 * @param codigoPKGrupo
	 * @return
	 */
	public  boolean eliminar(	Connection con, 
												int codigoPKGrupo
												) 
	{
	    return SqlBaseGruposDao.eliminar(con, codigoPKGrupo);
	}
	
	/**
	 * Consulta la info de la participación del pool X tarifa dado el cod del pool 
	 * @param con
	 * @param codigoPool
	 * @return
	 */
	public  HashMap listadoGrupos(Connection con, int codigoInstitucion, int codigoEsquemaTarifario,int codigoGrupo,boolean esEsquemaGeneral)
	{
	    return SqlBaseGruposDao.listadoGrupos(con, codigoInstitucion, codigoEsquemaTarifario,codigoGrupo, esEsquemaGeneral);
	}
	
	/**
	 * metodo para la busqueda avanzada de los grupos 
	 * @param con
	 * @param grupo
	 * @param codigoAsocio
	 * @param codigoEsquemaTarifario
	 * @param codigoCups
	 * @param codigoSoat
	 * @param codigoTipoLiquidacion
	 * @param unidades
	 * @param valor
	 * @param activo
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap busquedaAvanzadaGrupos(			Connection con, 
	        																	int grupo,
																				int codigoAsocio,
																				int codigoTipoLiquidacion,
																				int codigoInstitucion,
																				String tipoServicio,
																				int codigoEsquemaTarifario,
																				String convenio,
																				String fechainicial,
																				String fechafinal,
																				String liquidarPor,
																				String cups
																				)
	{
	    return SqlBaseGruposDao.busquedaAvanzadaGrupos(con, grupo, codigoAsocio, codigoTipoLiquidacion, codigoInstitucion,tipoServicio,codigoEsquemaTarifario,convenio,fechainicial,fechafinal,liquidarPor,cups);
	}
	
	/**
	 * Metodo que lista los tipos de servicios
	 * @param con
	 * @return
	 */
	public ArrayList listarTiposServicio(Connection con)
	{
		return SqlBaseGruposDao.listarTiposServicio(con);
	}
	
	/**
	 * 
	 */
	public ArrayList listarTarifariosOficiales(Connection con)
	{
		return SqlBaseGruposDao.listarTarifariosOficiales(con);
	}
	

	/**
	 * 
	 */
	public boolean actualizarDetCodigosGrupos(Connection con, HashMap<String, Object> vo) 
	{
		return SqlBaseGruposDao.actualizarDetCodigosGrupos(con,vo);
	}

	public HashMap listadoGruposLLave(Connection con, String tempoCodPKGrupo) 
	{
		return SqlBaseGruposDao.listadoGruposLLave(con,tempoCodPKGrupo);
	}
	
	public boolean eliminarCodigosGruposTotal(Connection con,HashMap vo)
	{
		return SqlBaseGruposDao.eliminarCodigosGruposTotal(con, vo);
	}
	
	public HashMap listarVigenciasConvenio(Connection con,int convenio)
	{
		return SqlBaseGruposDao.listarVigenciasConvenio(con, convenio);
	}
	
	public int insertarVigenciaGrupo(Connection con, int codigoEsquemaTarifario, String convenio, String fechaInicial, String fechaFinal, int codigoInstitucion, String usuario,boolean esquemaGeneral)
	{
		return SqlBaseGruposDao.insertarVigenciaGrupo(con, codigoEsquemaTarifario, convenio, fechaInicial, fechaFinal, codigoInstitucion, usuario, esquemaGeneral);
	}
	
	public boolean modificarVigenciasConvenio(Connection con,HashMap vo)
	{
		return SqlBaseGruposDao.modificarVigenciasConvenio(con, vo);
	}
	
	public boolean eliminarGrupoMaestro(Connection con,int convenio,int codigo)
	{
		return SqlBaseGruposDao.eliminarGrupoMaestro(con, convenio, codigo);
	}
	
}