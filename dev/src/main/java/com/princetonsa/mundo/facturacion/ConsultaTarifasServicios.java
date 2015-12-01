/*
 * @(#)ConsultaTarifasServicios.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.mundo.facturacion;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.ConsultaTarifasServiciosDao;
import com.princetonsa.dao.DaoFactory;

/**
 * Objeto que maneja la interacción entre la capa
 * de control y el acceso a la información de la 
 * Consulta de Tarifas de Servicios
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 27 /Mar/ 2006
 */
public class ConsultaTarifasServicios
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ConsultaTarifasServicios.class);
	
	/**
     * Constructor del objeto
     * (Solo inicializa el acceso a la 
     * fuente de datos)
     */
    public ConsultaTarifasServicios()
    {
        this.init(System.getProperty("TIPOBD"));
    }
    
    /**
	 * El DAO usado por el objeto <code>consultaTarifasServiciosDao</code> 
	 * para acceder a la fuente de datos. 
	 */
	private ConsultaTarifasServiciosDao consultaTarifasServiciosDao ;
	
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
			consultaTarifasServiciosDao = myFactory.getConsultaTarifasServiciosDao();
			wasInited = (consultaTarifasServiciosDao != null);
		}

		return wasInited;
	}
	
	/**
	 * Metodo para realizar la busqueda avanzada de los servicios por los campos
	 * que se especifiquen
	 * @param con
	 * @param codigoInterno
	 * @param codigoCups
	 * @param descripcionCups
	 * @param codigoIss
	 * @param descripcionIss
	 * @param codigoSoat
	 * @param descripcionSoat
	 * @param codigoEspecialidad
	 * @param acronimoTipoServicio
	 * @param acronimoNaturaleza
	 * @param codigoGrupoServicio
	 * @return
	 */
	public HashMap busquedaServicios (Connection con, String codigoInterno, String tipoTarifario, String codigoServicio, String descripcionServicio, int codigoEspecialidad, String acronimoTipoServicio, String acronimoNaturaleza,int codigoGrupoServicio) throws SQLException
	{
		return consultaTarifasServiciosDao.busquedaServicios(con, codigoInterno, tipoTarifario, codigoServicio, descripcionServicio, codigoEspecialidad, acronimoTipoServicio, acronimoNaturaleza, codigoGrupoServicio);
	}
	
	/**
	 * Método para consultar el encabzado del detalle de una tarifa de servicios dado el codigo de servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultaEncabezadoDetalle(Connection con, int codigoServicio)  throws SQLException
	{
		return consultaTarifasServiciosDao.consultaEncabezadoDetalle(con, codigoServicio);
	}
	/**
	 * Método para consultar el cuerpo del detalle de una tarifa de servicios dado el codigo interno del servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultaCuerpoDetalle(Connection con, int codigoServicio) throws SQLException
	{
		return consultaTarifasServiciosDao.consultaCuerpoDetalle(con, codigoServicio);
	}
	
}