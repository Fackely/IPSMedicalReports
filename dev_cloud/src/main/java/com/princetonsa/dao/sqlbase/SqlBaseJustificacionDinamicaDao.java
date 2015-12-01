/*
 * Created on May 27, 2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import util.ConstantesBD;

/**
 * @author sebacho
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SqlBaseJustificacionDinamicaDao {
	
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseJustificacionDinamicaDao.class);
	
	/**
	 * Cadena para insertar el atributo de la justificacion de un medicamento
	 */
	private final static String insertarAtributoJustificacionMedicamentoStr="INSERT INTO desc_atributos_solicitud (numero_solicitud,articulo,atributo,descripcion) VALUES(?,?,?,?)";
	
	/**
	 * Cadena para insertar el atributo de la justificación de un servicio
	 */
	private final static String insertarAtributoJustificacionServicioStr="INSERT INTO desc_atr_sol_int_proc (numero_solicitud,servicio,atributo,descripcion) VALUES(?,?,?,?)";
	
	/**
	 * Cadena para modificar el atributo de la justificacion de un medicamento
	 */
	private final static String modificarAtributoJustificacionServicioStr=" UPDATE desc_atr_sol_int_proc SET descripcion = ? WHERE numero_solicitud=? AND servicio= ? AND atributo=?";
	
	/**
	 * Cadena para modificar el atributo de la justificacion de un servicio
	 */
	private final static String modificarAtributoJustificacionMedicamentoStr="UPDATE desc_atributos_solicitud SET descripcion = ? WHERE numero_solicitud=? AND articulo= ? AND atributo=?";
	
	/**
	 * Método para insertar un atributo de una justificacion de servicio o medicamento
	 * @param con
	 * @param numeroSolicitud
	 * @param parametro
	 * @param atributo
	 * @param descripcion
	 * @param esArticulo
	 * @return
	 */
	public static int insertarAtributoJustificacion(Connection con,int numeroSolicitud,int parametro,int atributo,String descripcion,boolean esArticulo)
	{
		try
		{
			PreparedStatementDecorator pst;
			if(esArticulo)
			{
				pst= new PreparedStatementDecorator(con.prepareStatement(insertarAtributoJustificacionMedicamentoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			}
			else
			{
				pst= new PreparedStatementDecorator(con.prepareStatement(insertarAtributoJustificacionServicioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			}
			
			pst.setInt(1,numeroSolicitud);
			pst.setInt(2,parametro);
			pst.setInt(3,atributo);
			pst.setString(4,descripcion);
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarAtributoJustificacion de SqlBaseJustificacionDinamicaDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Metodo para modificar el atributo de la justificacion de una solicitud de servicios o medicamento
	 * @param con
	 * @param numeroSolicitud
	 * @param parametro
	 * @param atributo
	 * @param descripcion
	 * @param esArticulo
	 * @return
	 */
	public static int modificarAtributoJustificacion(Connection con,int numeroSolicitud,int parametro,int atributo,String descripcion,boolean esArticulo)
	{
		try
		{
			PreparedStatementDecorator pst;
			if(esArticulo)
			{
				pst= new PreparedStatementDecorator(con.prepareStatement(modificarAtributoJustificacionMedicamentoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			}
			else
			{
				pst= new PreparedStatementDecorator(con.prepareStatement(modificarAtributoJustificacionServicioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			}
			
			pst.setString(1,descripcion);
			pst.setInt(2,numeroSolicitud);
			pst.setInt(3,parametro);
			pst.setInt(4,atributo);
			
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en modificarAtributoJustificacion de SqlBaseJustificacionDinamicaDao: "+e);
			return -1;
		}
	}

}
