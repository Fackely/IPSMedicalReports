/*
 * @(#)CentrosCostoViaIngreso.java
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

import util.UtilidadBD;

import com.princetonsa.dao.CentrosCostoViaIngresoDao;
import com.princetonsa.dao.DaoFactory;

/**
 * Objeto que maneja la interacción entre la capa
 * de control y el acceso a la información de 
 * Centros de Costo X Via de Ingreso
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 15 /May/ 2006
 */
public class CentrosCostoViaIngreso
{
	
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(CentrosCostoViaIngreso.class);
	
	
    /**
     * Constructor del objeto
     * (Solo inicializa el acceso a la 
     * fuente de datos)
     */
    public CentrosCostoViaIngreso()
    {
        this.init(System.getProperty("TIPOBD"));
    }
    
    /**
	 * El DAO usado por el objeto <code>CentrosCostoViaIngreso</code> 
	 * para acceder a la fuente de datos. 
	 */
	private CentrosCostoViaIngresoDao centrosCostoViaIngresoDao ;

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
			centrosCostoViaIngresoDao = myFactory.getCentrosCostoViaIngresoDao();
			wasInited = (centrosCostoViaIngresoDao != null);
		}

		return wasInited;
	}
	
	/**
	 * Método para consultar los centros de cotso por via de ingreso
	 * segun la insitucion de los centros de costo
	 * @param con
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarCentrosCostoViaIngreso(int institucion, int centroAtencion) 
	{
		Connection con= UtilidadBD.abrirConexion();
		HashMap mapa=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCentrosCostoViaIngresoDao().consultarCentrosCostoViaIngreso(con, institucion, centroAtencion);
		UtilidadBD.closeConnection(con);
		return mapa;
	}
	
	/**
	 * Método para consultar los centros de cotso por via de ingreso
	 * segun la insitucion de los centros de costo
	 * @param con
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarCentrosCostoViaIngreso(Connection con, int institucion, int centroAtencion) throws SQLException
	{
		return centrosCostoViaIngresoDao.consultarCentrosCostoViaIngreso(con, institucion, centroAtencion);
	}
	
	
	/**
	 * Método para eliminar un cento ed costo x via de ingreso
	 * @param con
	 * @param codigo
	 * @return
	 * @throws SQLException
	 */
	public int eliminarCentroCostoViaIngreso(Connection con, int codigo) throws SQLException
	{
		return centrosCostoViaIngresoDao.eliminarCentroCostoViaIngreso(con, codigo);
	}
	
	/**
	 * Método para modificar un centro de costo x via de ingreso
	 * @param con
	 * @param codigoCentroCosto
	 * @param codigoViaIngreso
	 * @param codigo
	 * @return
	 * @throws SQLException
	 */
	public int modificarCentroCostoViaIngreso(Connection con, int codigoCentroCosto, int codigoViaIngreso, int codigo, int institucion, String tipopaciente) throws SQLException 
	{
		return centrosCostoViaIngresoDao.modificarCentroCostoViaIngreso(con, codigoCentroCosto, codigoViaIngreso, codigo, institucion, tipopaciente);
	}
	
	/**
	 * Método para insertar un nuevo centro de costo.
	 * Si existe lo que hace es modificarlo de lo contrario lo Inserta
	 * @param con
	 * @param codigo
	 * @param codigoCentroCosto
	 * @param codigoViaIngreso
	 * @return
	 * @throws SQLException
	 */
	public int insertarCentrosCostoViaIngreso(Connection con, int codigo, int codigoCentroCosto, int codigoViaIngreso, int institucion, String tipopaciente) throws SQLException
	{
		return centrosCostoViaIngresoDao.insertarCentrosCostoViaIngreso(con, codigo, codigoCentroCosto, codigoViaIngreso, institucion, tipopaciente);
	}
	
}