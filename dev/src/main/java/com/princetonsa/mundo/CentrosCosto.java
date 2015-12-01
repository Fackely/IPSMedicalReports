/*
 * @(#)CentrosCosto.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.mundo;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadValidacion;

import com.princetonsa.dao.CentrosCostoDao;
import com.princetonsa.dao.DaoFactory;

/**
 * Objeto que maneja la interacción entre la capa
 * de control y el acceso a la información de 
 * Centros de Costo
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 10 /May/ 2006
 */
public class CentrosCosto
{
	
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(CentrosCosto.class);
	
	
    /**
     * Constructor del objeto
     * (Solo inicializa el acceso a la 
     * fuente de datos)
     */
    public CentrosCosto()
    {
        this.init(System.getProperty("TIPOBD"));
    }
    
    /**
	 * El DAO usado por el objeto <code>CentrosCostoDao</code> 
	 * para acceder a la fuente de datos. 
	 */
	private CentrosCostoDao centrosCostoDao ;

	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD) 
	{

		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

		if (myFactory != null)
		{
			centrosCostoDao = myFactory.getCentrosCostoDao();
			wasInited = (centrosCostoDao != null);
		}

		return wasInited;
	}
	
	/**
	 * Método para consultar los centros de costo asociados a un centro de atencion y una
	 * institucion en especifico
	 * @param con
	 * @param centroatencion
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarCentrosCosto(Connection con , int centroatencion, int institucion) throws SQLException
	{		
		return centrosCostoDao.consultarCentrosCosto(con, centroatencion, institucion);
	}
	
	
	/**
	 * Método para consultar los centros de costo asociados a un centro de atencion y una
	 * institucion en especifico
	 * @param con
	 * @param centroatencion
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarCentrosCostoEsUsado(Connection con , int centroatencion, int institucion) throws SQLException
	{		
		HashMap respuesta;
		int numRegistros = 0;		
		
		respuesta = centrosCostoDao.consultarCentrosCosto(con, centroatencion, institucion);
		logger.info("FIN REALIZACIÓN CONSULTA A");
		numRegistros = Integer.parseInt(respuesta.get("numRegistros").toString()); 
		for(int i=0;i<numRegistros;i++)
		{ 
			logger.info("validacion usado CONSULTA A");
			if(!UtilidadValidacion.esCentroCostoUsado(con,respuesta.get("codigo_"+i).toString(),institucion))
				respuesta.put("esUsado_"+i,ConstantesBD.acronimoNo);
			else
				respuesta.put("esUsado_"+i,ConstantesBD.acronimoSi);
			//respuesta.put("esUsado_"+i,ConstantesBD.acronimoNo);
			logger.info("fin validacion usado CONSULTA A");
		}		
				
		return respuesta;
	}
	
	
	
	/**
	 * Método para modificar un centro de costo dado su codigo
	 * @param con
	 * @param identificador
	 * @param descripcion
	 * @param codigoTipoArea
	 * @param manejoCamas
	 * @param unidadFuncional
	 * @param activo
	 * @param codigo
	 * @return
	 */
	public int modificarCentroCosto(Connection con, String identificador, String descripcion, int codigoTipoArea,String reg_resp, boolean manejoCamas, int unidadFuncional, String codigo_interfaz, boolean activo, int codigo, String tipoEntidad) throws SQLException 
	{
		return centrosCostoDao.modificarCentroCosto(con, identificador, descripcion, codigoTipoArea,reg_resp, manejoCamas, unidadFuncional, codigo_interfaz, activo, codigo, tipoEntidad);
	}
	
	/**
	 * Método para la insercion de un nuevo Centro de Costo con todos sus atributos
	 * @param con
	 * @param codigocentrocosto
	 * @param nombre
	 * @param codigoTipoArea
	 * @param institucion
	 * @param activo
	 * @param identificador
	 * @param manejoCamas
	 * @param unidadFuncional
	 * @param centroAtencion
	 * @param tipoEntidad 
	 * @param insertarCentroCostoStr -> Postgres - Oracle
	 * @return
	 */
	public int insertarCentrosCosto(Connection con, int codigoCentroCosto, String nombre, int codigoTipoArea,String reg_resp, int institucion, boolean activo, String identificador, boolean manejoCamas, String unidadFuncional, String codigo_interfaz, int centroAtencion, String tipoEntidad) throws SQLException
	{
		
		return centrosCostoDao.insertarCentrosCosto(con, codigoCentroCosto, nombre, codigoTipoArea,reg_resp, institucion, activo, identificador, manejoCamas, unidadFuncional, codigo_interfaz, centroAtencion,tipoEntidad);
	}
	
	/**
	 * Método para eliminar un centro de costo dado su codigo
	 * @param con
	 * @param centroCosto
	 * @return
	 */
	public int eliminarCentroCosto(Connection con, int centroCosto) throws SQLException
	{
		return centrosCostoDao.eliminarCentroCosto(con, centroCosto);
	}
	
	/**
	 * 
	 * @param con
	 * @param almacen
	 * @return
	 * @throws SQLException
	 */
	public int eliminarAlmacen(Connection con, int almacen) throws SQLException
	{
		return centrosCostoDao.eliminarAlmacen(con, almacen);
		
	}
	
	/**
	 * 
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap consultarCentrosGrupoServicios(int centroAtencion) 
	{
		Connection con= UtilidadBD.abrirConexion();
		HashMap mapa=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCentrosCostoDao().consultarCentrosGrupoServicios(con, centroAtencion);
		UtilidadBD.closeConnection(con);
		return mapa;
	}
}
