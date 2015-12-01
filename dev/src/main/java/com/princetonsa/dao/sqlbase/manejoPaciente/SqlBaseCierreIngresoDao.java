package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

/**
 * Mauricio Jaramillo
 * @author axioma
 * Fecha Mayo de 2008
 */

public class SqlBaseCierreIngresoDao
{

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseCierreIngresoDao.class);
	
	/**
     * Cadena para insertar el cierre de ingreso
     */
    private static String strInCierreIngreso = "INSERT INTO cierre_ingresos (codigo, id_ingreso, motivo_cierre, usuario_cierre, fecha_cierre, hora_cierre, activo, institucion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"; 

    /**
     * Cadena para actualizar en la tabla ingresos
     */
    private static String strUpCierreIngreso = "UPDATE ingresos SET cierre_manual = '"+ConstantesBD.acronimoSi+"', estado = '"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"'  WHERE id = ?";
    
    /**
     * Metodo que genera la inserccion en cierre ingreso
     * @param con
     * @param codigoInstitucionInt
     * @param criterios
     * @return
     */
    public static boolean cerrarIngreso(Connection con, HashMap vo)
	{
    	PreparedStatementDecorator ps = null;
    	boolean enTransaccion = true;
    	try
        {
        	ps =  new PreparedStatementDecorator(con.prepareStatement(strInCierreIngreso,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        	
        	/**
        	 * INSERT INTO cierre_ingresos (
        	 * codigo, 
        	 * id_ingreso, 
        	 * motivo_cierre, 
        	 * usuario_cierre, 
        	 * fecha_cierre, 
        	 * hora_cierre, 
        	 * activo, 
        	 * institucion) VALUES (?, ?, ?, ?, ?, ?, ?, ?) 
        	 */
        	        	
        	ps.setDouble(1,Utilidades.convertirADouble(UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_cierre_ingresos")+""));
            ps.setInt(2, Utilidades.convertirAEntero(vo.get("idIngreso")+""));
            ps.setString(3, vo.get("motivoCierre")+"");
            ps.setString(4, vo.get("usuarioCierre")+"");
            ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
            ps.setString(6, UtilidadFecha.getHoraActual(con));
            ps.setString(7, vo.get("activo")+"");
            ps.setInt(8, Utilidades.convertirAEntero(vo.get("institucion")+""));
            enTransaccion = (ps.executeUpdate()>0);
            if(enTransaccion)
            {
            	ps =  new PreparedStatementDecorator(con.prepareStatement(strUpCierreIngreso,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            	ps.setInt(1, Utilidades.convertirAEntero(vo.get("idIngreso")+""));
            	enTransaccion = (ps.executeUpdate()>0);
            	if(enTransaccion)
            		return true;
            	else
            	{
            		logger.error("ERROR ACTUALIZANDO EL REGISTRO EN INGRESOS");
            		return false;
            	}
            }
            else
            {
            	logger.error("ERROR EJECUNTANDO LA ADICION DEL REGISTRO EN CIERRE DE INGRESOS");
            	return false;
            }
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA ADICION DEL REGISTRO EN CIERRE DE INGRESOS");
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Método para obtener los ingresos cerrados por cierre manual de un paciente
     * pendientes por facturar
     * @param con
     * @param campos
     * @return
     */
    public static ArrayList<HashMap<String,Object>> obtenerIngresosCerradosPendientesXPaciente(Connection con,HashMap campos)
    {
    	ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
    	try
    	{
    		//*****************SE TOMAN LOS PARÁMETROS*****************************************
    		int codigoPaciente = Integer.parseInt(campos.get("codigoPaciente").toString());
    		//*********************************************************************************
    		
    		String consulta = "SELECT " +
    			"i.consecutivo as consecutivo," +
    			"i.anio_consecutivo as anio_consecutivo " +
    			"from manejopaciente.ingresos i " +
    			"where " +
    			"i.codigo_paciente = ? and " +
    			"i.estado = '"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"' and " +
    			"i.cierre_manual = '"+ConstantesBD.acronimoSi+"' and " +
    			"i.id in (select c.id_ingreso from manejopaciente.cuentas c WHERE c.id_ingreso = i.id and c.estado_cuenta in ("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaAsociada+","+ConstantesBD.codigoEstadoCuentaFacturadaParcial+"))";
    		
    		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
    		pst.setInt(1,codigoPaciente);
    		
    		ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
    		while(rs.next())
    		{
    			HashMap<String, Object> elemento = new HashMap<String, Object>();
    			elemento.put("consecutivo", rs.getString("consecutivo"));
    			elemento.put("anioConsecutivo", rs.getString("anio_consecutivo"));
    			resultados.add(elemento);
    		}
    	}
    	catch(SQLException e)
    	{
    		logger.error("Error en obtenerIngresosCerradosXPaciente: "+e);
    	}
    	
    	return resultados;
    }
    
}