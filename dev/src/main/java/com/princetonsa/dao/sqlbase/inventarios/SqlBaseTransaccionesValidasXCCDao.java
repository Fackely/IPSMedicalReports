
/*
 * Creado   21/11/2005
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
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * 
 *
 * @version 1.0, 21/11/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class SqlBaseTransaccionesValidasXCCDao 
{
    /**
     * manejador de errores de la clase
     */
    public static Logger logger= Logger.getLogger(SqlBaseTransaccionesValidasXCCDao.class);
    /**
     * query para consultar los registros existentes
     */
    public static final String consultarTransaccionesValidasStr="SELECT " +
															    		"tvi.codigo as codigo," +
															    		"tvi.centros_costo as cod_centro_costo," +
															    		"getnomcentrocosto(tvi.centros_costo) as nom_centro_costo," +
															    		"tvi.tipos_trans_inventario as cod_tipos_trans_inventario," +
															    		"getDescTipoTransInventario(tvi.tipos_trans_inventario) as desc_tipos_trans_inventario," +
															    		"CASE WHEN tvi.clase_inventario IS NULL THEN -1 ELSE tvi.clase_inventario END as cod_clase_inventario," +
															    		"CASE WHEN getNomClaseInventario(tvi.clase_inventario) IS NULL THEN '' ELSE getNomClaseInventario(tvi.clase_inventario) END as nom_clase_inventario," +
															    		"CASE WHEN tvi.grupo_inventario IS NULL THEN -1 ELSE tvi.grupo_inventario END as cod_grupo_inventario," +
															    		"CASE WHEN getNomGrupoInventario(tvi.grupo_inventario,tvi.clase_inventario) IS NULL THEN '' ELSE getNomGrupoInventario(tvi.grupo_inventario,tvi.clase_inventario) END as nom_grupo_inventario," +
															    		"tti.codigo as codigoStrTrans," +
															    		"getDescTipoConceptoInv(tti.tipos_conceptos_inv) as desc_tipo_concepto " +
															    		"from trans_validas_x_cc_inven tvi " +
															    		"inner join tipos_trans_inventarios tti on (tti.consecutivo=tvi.tipos_trans_inventario) " +
															    		"where tvi.institucion=? ";
    /**
     * query par aactualizar registros de transacciones validas
     */
    public static final String updateTransaccionesValidasStr=" UPDATE trans_validas_x_cc_inven SET ";
    
    /**
     * query para eliminar registros de transacciones validas
     */
    public static final String deleteTransaccionesValidasStr = "DELETE FROM trans_validas_x_cc_inven WHERE codigo=? AND institucion=?";
    
    /**
     * query para consultar lo campos que pueden ser modificados
     */
    public static final String camposAModificarStr = "SELECT " +
												    		"centros_costo as cod_centros_costo," +												    		
												    		"tipos_trans_inventario as cod_tipos_trans_inventario," +												    		
												    		"CASE WHEN clase_inventario IS NULL THEN -1 ELSE clase_inventario END as cod_clase_inventario," +												    		
												    		"CASE WHEN grupo_inventario IS NULL THEN -1 ELSE grupo_inventario END as cod_grupo_inventario " +												    		
												    		"from trans_validas_x_cc_inven " +
												    		"where codigo=? and institucion=?";
    /**
     * query para consultar los códigos de los registros
     */
    public static final String registrosBDStr = "SELECT codigo from trans_validas_x_cc_inven where institucion=?";
    
    /**
     * Query para eliminar la transacción como tal y su detalle. Desde la Página principal
     */
    public static final String deleteTransStr = "DELETE FROM trans_validas_x_cc_inven " +
    													"WHERE centros_costo = ? " +
    														   "AND tipos_trans_inventario = ? " +
    														   "AND institucion = ? ";
    
    /**
     * metodo para generar la consulta de 
     * transacciones validas
     * @param con Connection
     * @param institucion int
     * @return HashMap
     */
    public static HashMap generarConsulta(Connection con,int institucion,int codCentroCosto,boolean esVerificar, int transaccionFiltro)
    {
        try
        { 
            String query="";
            if(!esVerificar)
                query=consultarTransaccionesValidasStr+" AND tvi.centros_costo=? and tti.consecutivo="+transaccionFiltro +" and tvi.clase_inventario is not null " ;
            else
                query=consultarTransaccionesValidasStr+" AND tvi.codigo=? ";
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));           
            ps.setInt(1,institucion);  
            ps.setInt(2,codCentroCosto);
            logger.info("\n Transacciones validas x centro costo="+query+"-->inst->"+institucion+" cc="+codCentroCosto);
            
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());    
            HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
	        rs.close();
	        ps.close();
	        return mapaRetorno;
        }
        catch(SQLException e)
	    {
	        logger.warn(e+"Error en generarConsulta"+e.toString() );		   
	    }
        return null;   
    }
    /**
     * metodo para insertar los registros
     * de transacciones validas
     * @param con Connection
     * @param codCC int 
     * @param codTipoTrans int
     * @param codClaseInv int
     * @param codGrupoInv int
     * @param institucion int
     * @param query String
     * @return boolean
     */
    public static boolean generarInsert(Connection con,int codCC,int codTipoTrans,int codClaseInv,int codGrupoInv,int institucion,String query, String secuencia)
    {
    	
        logger.info("===>Codigo Tipo Transaccion: "+codTipoTrans);
    	try
	    {
        	 int r=ConstantesBD.codigoNuncaValido;
            PreparedStatementDecorator ps = null;
            
            if (codGrupoInv==0 && codClaseInv!=ConstantesBD.codigoNuncaValido){
            	String cadenaSql = "INSERT INTO " +
            							"trans_validas_x_cc_inven " +
            							"(codigo," +
            							"centros_costo," +
							    		"tipos_trans_inventario," +
							    		"clase_inventario," +
							    		"grupo_inventario," +
							    		"institucion) " +
            						"( " +
            							"SELECT "+secuencia+","+codCC+","+codTipoTrans+","+codClaseInv+",gi.codigo,"+institucion+" FROM grupo_inventario gi WHERE gi.institucion="+institucion+" AND gi.clase="+codClaseInv+" AND gi.codigo NOT IN ("+Utilidades.obtenerGruposTransValidasXCC(con, institucion, codCC, codTipoTrans, codClaseInv)+"))";
            	ps= new PreparedStatementDecorator(con.prepareStatement(cadenaSql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            	
            	logger.info("CONSULTA:::: "+cadenaSql);
            	r=ps.executeUpdate();
            }
            else 
            {
            	logger.info("INSERTA TRANSACCION 1 REGISTREO-->"+query+" CC-->"+codCC+" tipoTrans->"+codTipoTrans+" clase->"+codClaseInv+" grupo->"+codGrupoInv+" ins->"+institucion);
            	
            	ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                
                /**
                 * INSERT INTO " +
    														    		"trans_validas_x_cc_inven" +
    														    		"(codigo,centros_costo," +
    														    		"tipos_trans_inventario," +
    														    		"clase_inventario," +
    														    		"grupo_inventario," +
    														    		"institucion) " +
    														    		"VALUES('seq_trans_validas_x_cc_inven'),?,?,?,?,?)
                 */
                
                ps.setInt(1, codCC);
                ps.setInt(2, codTipoTrans);
                if(codClaseInv==ConstantesBD.codigoNuncaValido)
                    ps.setNull(3, Types.INTEGER);
                else
                    ps.setInt(3, codClaseInv);
                if(codGrupoInv==ConstantesBD.codigoNuncaValido)
                    ps.setNull(4, Types.INTEGER);
                else
                    ps.setInt(4, codGrupoInv);
                ps.setInt(5, institucion);
                r=ps.executeUpdate();
            }
            if(r>0)
                return true;
            else
                return false;
	    }
        catch(SQLException e)
	    {
	        logger.warn(e+"Error en generarInsert"+e.toString() );
		   return false;
	    }        
    }
    /**
     * metodo para actualizar los registros de 
     * transacciones validas
     * @param con Connection
     * @param codigo int
     * @param codCC int 
     * @param codTipoTrans int
     * @param codClaseInv int
     * @param codGrupoInv int
     * @param institucion int 
     * @return boolean 
     */
    public static boolean gerarUpdate(Connection con,int codigo,int codCC,int codTipoTrans,int codClaseInv,int codGrupoInv,int institucion)
    {
        try
        {                       
            String query=updateTransaccionesValidasStr;
            boolean esPrimero=false; 
            if(codCC!=ConstantesBD.codigoNuncaValido)
            {
                query+=" centros_costo="+codCC;
			    esPrimero=false;
            }
            if(codTipoTrans!=ConstantesBD.codigoNuncaValido)
	        {
	            if(!esPrimero)
	                query+=" , ";
	            esPrimero=false;	            
	            query+=" tipos_trans_inventario="+codTipoTrans;	  
	        }
            if(codClaseInv!=ConstantesBD.codigoNuncaValido)
	        {
	            if(!esPrimero)
	                query+=" , ";
	            esPrimero=false;	            
	            query+=" clase_inventario="+codClaseInv;	  
	        }
            if(codGrupoInv!=ConstantesBD.codigoNuncaValido)
	        {
	            if(!esPrimero)
	                query+=" , ";
	            esPrimero=false;	            
	            query+=" grupo_inventario="+codGrupoInv;	  
	        }
            query+=" WHERE codigo="+codigo+" AND institucion="+institucion;
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));            
	        int r=ps.executeUpdate();
	        if(r>0)
	            return true;
	        else
	            return false;
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en gerarUpdate"+e.toString() );
		   return false;
	    }
    }
    /**
     * metodo para eliminar registros de transacciones
     * validas
     * @param con Connection
     * @param codigo int
     * @param institucion int
     * @return boolean
     */
    public static boolean generarDelete(Connection con, int codigo, int institucion)
    {
        try
	    {
            PreparedStatementDecorator ps = null;            
            ps= new PreparedStatementDecorator(con.prepareStatement(deleteTransaccionesValidasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, codigo);
            ps.setInt(2, institucion);            
            int r=ps.executeUpdate();
            if(r>0)
                return true;
            else
                return false;
	    }
        catch(SQLException e)
	    {
	        logger.warn(e+"Error en generarDelete"+e.toString() );
		   return false;
	    }
    }
    /**
     * metodo para consultar los valores
     * actuales de los registros de transacciones
     * validas
     * @param con Connection
     * @param codigo int
     * @param institucion int
     * @return HashMap
     */
    public static HashMap generarConsultaCamposAModificarStr(Connection con,int codigo,int institucion)
    {
        try
        { 
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(camposAModificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));  
            ps.setInt(1,codigo);
            ps.setInt(2,institucion);  
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());    
            HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
	        rs.close();
	        ps.close();
	        return mapaRetorno;
        }
        catch(SQLException e)
	    {
	        logger.warn(e+"Error en generarConsultaCamposAModificarStr"+e.toString() );		   
	    }
        return null;   
    }
    /**
     * metodo para consultar los códigos
     * de los registros insertados en la BD
     * @param con Connection
     * @param institucion int 
     * @return HashMap
     */
    public static  HashMap generarConsultaRegistrosBD(Connection con,int institucion)
    {
        try
        { 
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(registrosBDStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));           
            ps.setInt(1,institucion);  
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());    
            HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
	        rs.close();
	        ps.close();
	        return mapaRetorno;
        }
        catch(SQLException e)
	    {
	        logger.warn(e+"Error en generarConsultaRegistrosBD"+e.toString() );		   
	    }
        return null;     
    }
	
    /**
	 * Método que carga las Transacciones de Inventarios según el centro
	 * de costo seleccionado
	 * @param con
	 * @param codCentroCosto
	 * @return
	 */
    public static HashMap<String, Object> cargarTransaccionesInventarios(Connection con, int codCentroCosto, int institucion)
    {
    	HashMap mapa = new HashMap();
    	String consulta = "SELECT " +
	    						"tti.consecutivo AS consecutivo, " +
	    						"tti.codigo_interfaz AS codigointerfaz, " +
	    						"tti.descripcion AS descripcion, " +
	    						"tti.tipos_conceptos_inv AS tipoconcepto " +
	    					"FROM " +
	    						"trans_validas_x_cc_inven tvci " +
	    						"INNER JOIN tipos_trans_inventarios tti ON (tvci.tipos_trans_inventario = tti.consecutivo) " +
	    					"WHERE " +
	    						"tvci.centros_costo = "+codCentroCosto+" " +
	    						"AND tvci.institucion = "+institucion+" " +
	    						"AND tti.activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" " +
	    					"GROUP BY " +
	    						"tti.consecutivo, tti.codigo_interfaz, tti.descripcion, tti.tipos_conceptos_inv " +
	    					"ORDER BY " +
	    						"tti.descripcion ";
    	try
        { 
            logger.info("===>Consulta Tipos de Transacciones por Centro de Costo: "+consulta);
    		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));           
            ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());    
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
    /**
	 * @param con
	 * @param consecutivoTrans
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
    public static boolean eliminarTransaccion(Connection con, String consecutivoTrans, int centroCosto, int institucion)
	{
    	try
	    {
            PreparedStatementDecorator ps = null;            
            ps =  new PreparedStatementDecorator(con.prepareStatement(deleteTransStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, centroCosto);
            ps.setInt(2, Utilidades.convertirAEntero(consecutivoTrans));
            ps.setInt(3, institucion);
            int r = ps.executeUpdate();
            if(r > 0)
                return true;
            else
                return false;
	    }
        catch(SQLException e)
	    {
	       logger.warn("ERROR EN ELIMINAR LA TRANSACCIÓN DE INVENTARIOS: "+e.toString());
		   return false;
	    }
	}
	
    /**
	 * Método que inserta una nueva Transacción 
	 * @param con
	 * @param criterios
	 * @return
	 */
    public static boolean insertarTransaccion(Connection con, HashMap<String, Object> criterios)
	{
    	String strInsertTransaccion = "INSERT INTO trans_validas_x_cc_inven " +
    									"(" +
    										"codigo, " +
    										"centros_costo, " +
    										"tipos_trans_inventario, " +
    										"clase_inventario, " +
    										"grupo_inventario, " +
    										"institucion"+
    									") " +
    									"VALUES (?,?,?,?,?,?)";
    	try
		{
    		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsertTransaccion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_trans_validas_x_cc_inven"));
    		ps.setInt(2, Utilidades.convertirAEntero(criterios.get("centroCosto")+""));
    		ps.setInt(3, Utilidades.convertirAEntero(criterios.get("transaccion")+""));
        	
	  		/*if(UtilidadCadena.noEsVacio(criterios.get("claseInventario")+""))
	  			ps.setInt(4, Utilidades.convertirAEntero(criterios.get("claseInventario")+""));
        	else*/
        	ps.setNull(4, Types.INTEGER);
	  		
	  		/*if(UtilidadCadena.noEsVacio(criterios.get("grupoInventario")+""))
	  			ps.setInt(5, Utilidades.convertirAEntero(criterios.get("grupoInventario")+""));
        	else*/
        	ps.setNull(5, Types.INTEGER);
	  		
	  		ps.setInt(6, Utilidades.convertirAEntero(criterios.get("institucion")+""));
	  		
	  		if(ps.executeUpdate() > 0)
        	{
        		logger.info("SE INSERTO CORRECTAMENTE LA TRANSACCION DE INVENTARIO POR CENTRO DE COSTO");
        		return true;
        	}
        	else
        		return false;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
    
}