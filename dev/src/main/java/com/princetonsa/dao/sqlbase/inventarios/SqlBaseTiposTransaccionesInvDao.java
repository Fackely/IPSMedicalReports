
/*
 * Creado   11/11/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * 
 *
 * @version 1.0, 11/11/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class SqlBaseTiposTransaccionesInvDao 
{
    /**
     * manejador de errores de la clase
     */
    public static Logger logger= Logger.getLogger(SqlBaseTiposTransaccionesInvDao.class);
    
    /**
     * query para consultar los tipos de transacciones
     */
    public static final String consultaTiposTransaccionesInvStr="SELECT " +
																    		"consecutivo as consecutivo," +
																    		"CASE WHEN getDescTipoConceptoInv(tipos_conceptos_inv) IS NULL THEN 'Seleccione' ELSE getDescTipoConceptoInv(tipos_conceptos_inv) END as descripcion_concepto," +
																    		"CASE WHEN tipos_conceptos_inv IS NULL THEN -1 ELSE tipos_conceptos_inv END as codigo_concepto," +
																    		"CASE WHEN getDescTipoCostoInv(tipos_costo_inv) IS NULL THEN 'Seleccione' ELSE  getDescTipoCostoInv(tipos_costo_inv) END as descripcion_costo," +
																    		"CASE WHEN tipos_costo_inv IS NULL THEN -1 ELSE tipos_costo_inv END as codigo_costo," +
																    		"codigo as codigo," +
																    		"descripcion as descripcion," +
																    		"activo as activo, " +
																    		"indicativo_consignacion as indicativo_consignacion," +
																    		"coalesce(codigo_interfaz,'') as codigo_interfaz " +
																    		"from tipos_trans_inventarios ";
	/**
     * query para modificar registros de transacciones
     */
    public static final String actualizarTiposTransaccionesInvStr="UPDATE tipos_trans_inventarios SET ";
    /**
     * query para eliminar registros de transacciones
     */
    public static final String eliminarTiposTransaccionesInvStr="DELETE FROM tipos_trans_inventarios WHERE consecutivo=?";
    /**
     * query para consultar lo campos que pueden ser modificados
     */
    public static final String camposAModificarStr="SELECT descripcion as descripcion,activo as activo,indicativo_consignacion as indicativo_consignacion,coalesce(codigo_interfaz,'') AS codigo_interfaz from tipos_trans_inventarios where consecutivo=?";
    
    /**
     * metodo para realizar la consulta/busqueda avanzada 
     * de registros tipos transacciones inventario 
     * @param con Connection 
     * @param codConcepto int 
     * @param codCosto int 
     * @param codigo String
     * @param descripcion String
     * @param activo String
     * @param institucion int 
     * @return HashMap
     * @see com.princetonsa.dao.inventarios.TiposTransaccionesInv#generarConsultaTiposTransaccionesInv(java.sql.Connection,int,int,String,String,String,int)
     */
    public static HashMap generarConsultaTiposTransaccionesInv(Connection con,int codConcepto,int codCosto,String codigo,String descripcion,String activo,String indicativo_consignacion, int institucion,String codigoInterfaz)
    {
        try
	    {           
            PreparedStatementDecorator ps = null; 
            String query=generarConsultaOModificacion(codConcepto, codCosto, codigo, descripcion, activo, indicativo_consignacion, institucion,codigoInterfaz);
            query+=" ORDER BY descripcion";
            logger.info("query---->"+query);
            ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	 
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
	        rs.close();
	        ps.close();
	        return mapaRetorno;
	    }   
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en generarConsultaTiposTransaccionesInv"+e.toString() );
		   return null;
	    }
    }
   /**
    * metodo para construir la consulta segun
    * los filtros enviados    
 * @param codigoInterfaz 
    */
    private static String generarConsultaOModificacion(int codConcepto,int codCosto,String codigo,String descripcion,String activo,String indicativo_consignacion, int institucion, String codigoInterfaz)
    {
        boolean esPrimero = true;
        String query=consultaTiposTransaccionesInvStr+" WHERE ";            
        if(codConcepto!=ConstantesBD.codigoNuncaValido)
        {
            query+=" tipos_conceptos_inv="+codConcepto;
            esPrimero = false;
        }
        if(codCosto!=ConstantesBD.codigoNuncaValido)
        {
            if(!esPrimero)
                query+=" AND ";
            query+=" tipos_costo_inv="+codCosto;
            esPrimero = false;
        }
        if(!codigo.equals(""))
        {
            if(!esPrimero)
                query+=" AND ";
            query+=" codigo='"+codigo+"'";
            esPrimero = false;
        }
        if(!descripcion.equals(""))
        {
            if(!esPrimero)
                query+=" AND ";
            query+=" descripcion='"+descripcion+"'";
            esPrimero = false;
        }
        if(!activo.equals(""))
        {
            if(!esPrimero)
                query+=" AND ";
            
            if(UtilidadTexto.getBoolean(activo))
            	query+=" activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" ";
            else
            	query+=" activo="+ValoresPorDefecto.getValorFalseParaConsultas()+" ";
            
            esPrimero = false;
        }
        if(!UtilidadTexto.isEmpty(indicativo_consignacion))
        {
	        if(!indicativo_consignacion.equals(""))
	        {
	            if(!esPrimero)
	                query+=" AND ";
	            query+=" indicativo_consignacion='"+indicativo_consignacion+"'";
	            esPrimero = false;
	        }
        }
        if(!codigoInterfaz.equals(""))
        {
            if(!esPrimero)
                query+=" AND ";
            query+=" codigo_interfaz='"+codigoInterfaz.trim()+"'";
            esPrimero = false;
        }
        if(!esPrimero)
            query+=" AND ";
        query+=" institucion="+institucion;
        esPrimero = false;  
        return query;
    }
    /**
     * metodo para insertar registros de tipos de 
     * transacciones inventario 
     * @param con Connection 
     * @param codConcepto int
     * @param codCosto int
     * @param codigo String
     * @param descripcion String
     * @param activo String
     * @param institucion int
     * @param query String
     * @param insertarTiposTransaccionesInvStr 
     * @return boolean
     * @see com.princetonsa.dao.inventarios.TiposTransaccionesInv#generarActualizacionTiposTransaccionesInv(java.sql.Connection,int,int,String,String,String,int,String)
     */
    public static boolean generarInsertTiposTransaccionesInv(Connection con,int codConcepto,int codCosto,String codigo,String descripcion,String activo,String indicativo_consignacion, int institucion,String codigoInterfaz, String query)
    {
        try
	    {
            PreparedStatementDecorator ps = null;            
            ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            /**
             * INSERT INTO " +
				    		"tipos_trans_inventarios " +
				    		"(consecutivo," +
				    		"institucion," +
				    		"tipos_conceptos_inv," +
				    		"tipos_costo_inv," +
				    		"codigo," +
				    		"descripcion," +
				    		"activo, indicativo_consignacion,codigo_interfaz) " +
				    		"VALUES ('seq_tipos_trans_inventarios'),?,?,?,?,?,?,?,?)
             */
            
            
            ps.setInt(1,institucion);
            if(codConcepto==ConstantesBD.codigoNuncaValido)
                ps.setNull(2,Types.INTEGER);
            else
                ps.setInt(2,codConcepto);
            if(codCosto==ConstantesBD.codigoNuncaValido)
            	ps.setNull(3,Types.INTEGER);
            else
                ps.setInt(3,codCosto);
            ps.setString(4,codigo);
            ps.setString(5,descripcion);
            ps.setBoolean(6,UtilidadTexto.getBoolean(activo));
            ps.setString(7,indicativo_consignacion);
            if(!codigoInterfaz.equals(""))
            	ps.setString(8,codigoInterfaz);
            else
            	ps.setNull(8,Types.CHAR);
            int r=ps.executeUpdate();
            if(r>0)
                return true;
            else
                return false;
	    }
        catch(SQLException e)
	    {
	        logger.warn(e+"Error en generarInsertTiposTransaccionesInv"+e.toString() );
		   return false;
	    }
    }
    /**
     * metodo para generar la modificación 
     * de registros.
     * @param con Connection    
     * @param consecutivo int
     * @param descripcion String
     * @param activo String
     * @param institucion int
     * @return boolean
     * @see com.princetonsa.dao.inventarios.TiposTransaccionesInv#generarUpdateTiposTransaccionesInv(java.sql.Connection,int,String,String,int)
     */
    public static boolean generarUpdateTiposTransaccionesInv(Connection con,int consecutivo,String descripcion,String activo,String indicativo_consignacion,String codigoInterfaz)
    {
        try
        {                       
            String query=actualizarTiposTransaccionesInvStr;
            boolean esPrimero=false; 
            if(!descripcion.equals(""))
            {
                query+=" descripcion='"+descripcion+"'";
			    esPrimero=false;
            }
            if(!activo.equals(""))
	        {
	            if(!esPrimero)
	                query+=" , ";
	            esPrimero=false;	
	            if(UtilidadTexto.getBoolean(activo))
	            	query+=" activo="+ValoresPorDefecto.getValorTrueParaConsultas();
	            else
	            	query+=" activo="+ValoresPorDefecto.getValorFalseParaConsultas();
	        }
            if(!indicativo_consignacion.equals(""))
	        {
	            if(!esPrimero)
	                query+=" , ";
	            esPrimero=false;	            
	            query+=" indicativo_consignacion='"+indicativo_consignacion+"'";	  
	        }
            if(!codigoInterfaz.equals(""))
            {
            	if(!esPrimero)
            		query+=", ";
            	esPrimero = false;
            	query += " codigo_interfaz ='"+codigoInterfaz+"' ";
            }
            query+=" WHERE consecutivo="+consecutivo;
            logger.info("-->"+query);
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));            
	        int r=ps.executeUpdate();
	        if(r>0)
	            return true;
	        else
	            return false;
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en generarUpdateTiposTransaccionesInv"+e.toString() );
		   return false;
	    }
    }
    /**
     * metodo para eliminar registros.
     * @param con Connection
     * @param consecutivo int
     * @return boolean
     * @see com.princetonsa.dao.inventarios.TiposTransaccionesInv#generarDeleteTiposTransaccionesInv(java.sql.Connection,int)
     */
    public static boolean generarDeleteTiposTransaccionesInv(Connection con, int consecutivo)    
    {
        try
        {                        
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarTiposTransaccionesInvStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, consecutivo);  
	        int r=ps.executeUpdate();
	        if(r>0)
	            return true;
	        else
	            return false;
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en generarDeleteTiposTransaccionesInv"+e.toString() );
		   return false;
	    }  
    }
    /**
     * metodo para verifiacar si existen modificaciones.
     * @param con Connection
     * @param consecutivo int     
     * @return ResultSet
     * @see com.princetonsa.dao.inventarios.TiposTransaccionesInv#verificarModificacionesInv(java.sql.Connection,int)
     */
    public static ResultSetDecorator verificarModificacionesInv(Connection con,int consecutivo)
    {
        try
        { 
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(camposAModificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));  
            ps.setInt(1,consecutivo);  
            return new ResultSetDecorator(ps.executeQuery());            
        }
        catch(SQLException e)
	    {
	        logger.warn(e+"Error en verificarModificacionesInv"+e.toString() );		   
	    }
        return null;          
    }
}
