/*
 * @(#)GruposDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *  Interfaz para el acceder a la fuente de datos de grupos
 *
 * @version 1.0, Sep 06 / 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public interface GruposDao 
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
     * @param fechaFinal 
     * @param fechaInicial 
     * @param usuario 
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
														String fechaInicial, 
														String fechaFinal, 
														String usuario,
														int codigoGrupo,
														boolean esquemaGeneral,
														String liquidarPor
													);
	
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
													);
	
	/**
	 * Metodo para eliminar un grupo
	 * @param con
	 * @param codigoPKGrupo
	 * @return
	 */
	public  boolean eliminar(	Connection con, 
												int codigoPKGrupo
												);
	
	/**
	 * Consulta la info de la participación del pool X tarifa dado el cod del pool 
	 * @param con
	 * @param codigoPool
	 * @return
	 */
	public HashMap listadoGrupos(Connection con, int codigoInstitucion, int codigoEsquemaTarifario,int codigoGrupo, boolean esEsquemaGeneral);
	
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
																				);
	
	/**
	 * Metodo que lista los tipos de servicios
	 * @param con
	 * @return
	 */
	public ArrayList listarTiposServicio(Connection con);
	
	/**
	 * Metodo para listar los tarifarios oficiales para que no solo se pida el codigo cups y soat
	 * @param con
	 * @return
	 */
	public ArrayList listarTarifariosOficiales(Connection con);



	/**
	 * 
	 * @param con
	 * @param vo
	 */
	public boolean actualizarDetCodigosGrupos(Connection con, HashMap<String, Object> vo);

	/**
	 * 
	 * @param con
	 * @param tempoCodPKGrupo
	 * @return
	 */
	public HashMap listadoGruposLLave(Connection con, String tempoCodPKGrupo);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean eliminarCodigosGruposTotal(Connection con,HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param convenio
	 * @return
	 */
	public HashMap listarVigenciasConvenio(Connection con,int convenio);

	/**
	 * 
	 * @param con
	 * @param codigoEsquemaTarifario
	 * @param convenio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoInstitucion
	 * @param usuario
	 * @return
	 */
	public int insertarVigenciaGrupo(Connection con, int codigoEsquemaTarifario, String convenio, String fechaInicial, String fechaFinal, int codigoInstitucion, String usuario, boolean esquemaGeneral);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 */
	public boolean modificarVigenciasConvenio(Connection con,HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param convenio
	 * @param codigo
	 */
	public boolean eliminarGrupoMaestro(Connection con,int convenio,int codigo);
	
}
