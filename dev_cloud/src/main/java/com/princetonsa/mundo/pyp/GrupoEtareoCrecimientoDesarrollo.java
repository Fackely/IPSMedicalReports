/*
 * @(#)GrupoEtareoCrecimientoDesarrollo.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.mundo.pyp;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.pyp.GrupoEtareoCrecimientoDesarrolloDao;

/**
 * Objeto que maneja la interacción entre la capa
 * de control y el acceso a la información de 
 * Grupo Etareo de Crecimiento y Desarrollo
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 28 /Jul/ 2006
 */
public class GrupoEtareoCrecimientoDesarrollo
{
	
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(GrupoEtareoCrecimientoDesarrollo.class);
	
	
    /**
     * Constructor del objeto
     * (Solo inicializa el acceso a la 
     * fuente de datos)
     */
    public GrupoEtareoCrecimientoDesarrollo()
    {
        this.init(System.getProperty("TIPOBD"));
    }
    
    /**
	 * El DAO usado por el objeto <code>GruposEtareos</code> 
	 * para acceder a la fuente de datos. 
	 */
	private GrupoEtareoCrecimientoDesarrolloDao grupoEtareoCrecimientoDesarrolloDao ;

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
			grupoEtareoCrecimientoDesarrolloDao = myFactory.getGrupoEtareoCrecimientoDesarrolloDao();
			wasInited = (grupoEtareoCrecimientoDesarrolloDao != null);
		}

		return wasInited;
	}
	
	/**
	 * Método para consultar los grupos etareos en la base de datos
	 * segun la insitucion del usuario cargado en session
	 * @param con
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarGruposEtareos (Connection con, int institucion) throws SQLException
	{
		return grupoEtareoCrecimientoDesarrolloDao.consultarGruposEtareos(con, institucion);
	}
	
	
	/**
	 * eliminarGrupoEtareo
	 * @param con
	 * @param codigoInterno
	 * @return
	 * @throws SQLException
	 */
	public boolean eliminarGrupoEtareoTransaccional (Connection con,  String estado, int codigoInterno) throws SQLException
	{
	    boolean eliminado = false;
	    DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    if (estado.equals(ConstantesBD.inicioTransaccion))
	    {
	        if (!myFactory.beginTransaction(con))
	        {
	            myFactory.abortTransaction(con);
	        }
	    }
	    try
	    {
	        eliminado = grupoEtareoCrecimientoDesarrolloDao.eliminarGrupoEtareo(con, codigoInterno);
	        if (!eliminado)
	        {
	            myFactory.abortTransaction(con);
	        }
	    }
	    catch (SQLException e)
	    {
	        myFactory.abortTransaction(con);
	        throw e;
	    }
	    
	    if (estado.equals(ConstantesBD.finTransaccion))
	    {
	        myFactory.endTransaction(con);
	    }
	    return eliminado;
	}
	
	/**
	 * modifica un honorario en una transaccion
	 * @param con
	 * @param estado
	 * @return
	 * @throws SQLException
	 */
	public boolean modificarGrupoEtareoTransaccional (Connection con, String estado, int codigoInterno, int codigo, String descripcion, int unidadMedida, int rangoInicial, int rangoFinal, int codigoSexo, String activo) throws SQLException
	{
	    boolean insertados = false;
	    DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    if (estado.equals(ConstantesBD.inicioTransaccion))
	    {
	        if (!myFactory.beginTransaction(con))
	        {
	            myFactory.abortTransaction(con);
	        }
	    }
	    try
	    {
	        insertados = grupoEtareoCrecimientoDesarrolloDao.modificarGrupoEtareo(con, codigoInterno, codigo, descripcion, unidadMedida, rangoInicial, rangoFinal, codigoSexo, activo);
	        if (!insertados)
		    {
	            myFactory.abortTransaction(con);
		    }
	    }
	    catch (SQLException e)
	    {
	        myFactory.abortTransaction(con);
	        throw e;
	    }
	    
	    if (estado.equals(ConstantesBD.finTransaccion))
	    {
	        myFactory.endTransaction(con);
	    }
	    return insertados;
	}
	
	
	/**
	 * Metodo que inserta los Grupos Etareos de Crecimiento y desarrollo
	 * @param con
	 * @param estado
	 * @param codigo
	 * @param descripcion
	 * @param unidadMedida
	 * @param rangoInicial
	 * @param rangoFinal
	 * @param codigoSexo
	 * @param activo
	 * @return
	 * @throws SQLException
	 */
	public int insertarGrupoEtareoTransaccional (Connection con, String estado, int codigo, String descripcion, int unidadMedida, int rangoInicial, int rangoFinal, int codigoSexo, String activo, int institucion) throws SQLException
	{
	    int codigoPK = -1;
	    DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    if (estado.equals(ConstantesBD.inicioTransaccion))
	    {
	        if (!myFactory.beginTransaction(con))
	        {
	            myFactory.abortTransaction(con);
	        }
	    }
	    try
	    {
	        codigoPK = grupoEtareoCrecimientoDesarrolloDao.insertarGrupoEtareo(con, codigo, descripcion, unidadMedida, rangoInicial, rangoFinal, codigoSexo, activo, institucion);
	        if (codigoPK <= 0)
	        {
	            myFactory.abortTransaction(con);
	        }
	    }
	    catch (SQLException e)
	    {
	        myFactory.abortTransaction(con);
	        throw e;
	    }
      
	    if (estado.equals(ConstantesBD.finTransaccion))
	    {
	        myFactory.endTransaction(con);
	    }
	    return codigoPK;
	}

	/**
	 * Método para eliminar un grupo etareo dado su codigo
	 * @param con
	 * @param codigo
	 * @return
	 * @throws SQLException
	 */
	public boolean eliminarGrupoEtareo(Connection con, int codigo) throws SQLException
	{
		return grupoEtareoCrecimientoDesarrolloDao.eliminarGrupoEtareo(con, codigo);
	}
}