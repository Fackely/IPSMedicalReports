/*
 * Creado   16/06/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ConstantesValoresPorDefecto;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.inventarios.ConstantesBDInventarios;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
 
/**
 * @version 1.0, 29/06/2005
 * @author <a href="mailto:acardona@PrincetonSA.com">Angela Cardona</a>
 * @author [restructurada 1/12/2005] <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class SqlBaseConsecutivosDisponiblesDao 
{
    /**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseConsecutivosDisponiblesDao.class);
	/**
	 * query para realizar los inserts de consecutivos
	 */		
	private static final String insertarStr="INSERT INTO consecutivos (nombre,valor,anio_vigencia,institucion) VALUES (?,?,?,?)";
	/**
	 * query para realizar las modificaciones de consecutivos
	 */
	private static final String modificarStr="UPDATE consecutivos SET ";	
	/**
	 * query para realizar las modificaciones de consecutivos
	 */
	private static final String modificarUsoConsecutivosStr="UPDATE uso_consecutivos SET ";
	/**
	 * cadena para la actualización de un consecutivo
	 */
	private static final String actualizarValorConsecutivoStr="UPDATE consecutivos SET valor=? WHERE nombre=?  AND institucion=? ";
	
	/**
	 * cadena para la actualizacion del consecutivo multiempresa
	 * */
	private static final String actualizarValorConsecutivosMultiEmpresaStr ="UPDATE empresas_institucion SET valor_consecutivo_fact = ?, anio_vigencia = ? WHERE institucion = ? AND codigo = ? ";
	
	/**
	 * query para consultar los consecutivos disponibles por modulo
	 */
	private static final String consultaConsecutivosXModuloStr="SELECT " +
																		"cm.nombre as nombre," +
																		"cm.descripcion as descripcion," +
																		"cm.modulo as modulo," +
																		"CASE WHEN c.valor IS NULL THEN '' ELSE c.valor END as valor," +
																		"CASE WHEN c.anio_vigencia IS NULL THEN '' ELSE c.anio_vigencia END as  anio_vigencia " +
																		"from consecutivos_modulos cm " +
																		"left join consecutivos c on(cm.nombre=c.nombre and c.institucion=?) " +
																		"where cm.modulo=? ";
	/**
	 * Cadena del SELECT de consulta para los consecutivos disponibles del sistema
	 */	
	private static final String SELECTconsultarStr = "SELECT " +
																" CASE WHEN co.nombre IS NULL THEN '' ELSE co.nombre END AS nombre," +
																" com.descripcion AS descripcion," +
																" co.valor AS valor," +
																" co.anio_vigencia AS anio_vigencia,"+
																" co.institucion AS institucion";
	/**
	 * query para realizar la consulta de consecutivos inventarios, para los casos de
	 * por almacen y unico en el sistema
	 */
	private static final String consultaConsecutivosInventariosStr="SELECT " +
																			"tti.descripcion as tipo_transaccion," +
																			"ci.nombre as nombre," +
																			"tti.consecutivo as cod_tipo_transaccion," +
																			"CASE WHEN ci.valor IS NULL THEN '' ELSE ci.valor END as valor," +
																			"CASE WHEN ci.anio_vigencia IS NULL OR ci.anio_vigencia='-1' THEN ''  ELSE ci.anio_vigencia END as anio_vigencia " +
																			"from tipos_trans_inventarios tti " +
																			"left join consecutivos_inventarios ci on " +
																			"(ci.tipo_transaccion=tti.consecutivo and " +
																			"ci.institucion=? and ci.nombre=?";	
	/**
	 * query para realizar las modificaciones de consecutivos inventarios
	 */
	private static final String modificarConsecInvStr="UPDATE consecutivos_inventarios SET ";
	/**
	 * query para consultar el valor de un consecutivo
	 */
	private static final String obtenerConsecutivoInventariosStr="SELECT valor as valor from consecutivos_inventarios where tipo_transaccion=? AND institucion=?";
	/**
	 * query para incrementar el valor de consecutivos inventarios
	 */
	private static final String actualizarConsecutivoInventariosStr=" UPDATE consecutivos_inventarios SET valor=? where tipo_transaccion=? AND institucion=?";
    /**
     * query para consulta de los modulos 
     */
    private static final String consultarModulosStr="SELECT codigo as codigo,nombre as nombre from modulos order by nombre";
    
    /**
     * Consulta el tipo de consecutivo de facturas varias
     */
    private static String consultaTipoConsecutivoFV="SELECT * FROM valores_por_defecto WHERE parametro='"+ ConstantesValoresPorDefecto.nombreValoresDefectoTipoConsecutivoManejar +"' AND valor='"+ ConstantesIntegridadDominio.acronimoTipoConsecutivoPropiFacturasVarias +"'";
    
	private static final String consultaConsecutivoModificable="SELECT con.usado as usado " +								
																"from administracion.uso_consecutivos con where con.nombre = ? ";
	
	/**
	 * metodo para insertar consecutivos
	 * @param con
	 * @param nombre
	 * @param descripcion
	 * @param valor
	 * @param anioVigencia
	 * @param institucion
	 * @return
	 */
	public static boolean insertar (Connection con, String nombre,String valor, String anioVigencia, int institucion) 
	{
	    int resp = 0;	    
	    try
		  {
		      if (con == null || con.isClosed()) 
				{
					DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
					con = myFactory.getConnection();
				}
		      PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,insertarStr);
		      ps.setString(1,nombre);		      
		      ps.setString(2,valor);
		      ps.setString(3,anioVigencia.trim());
		      ps.setInt(4,institucion);
		      resp=ps.executeUpdate();		      
		      if(resp < 0)
		          return  false;
		      else
		          return  true;		      
		  }
	    catch (SQLException e)
	    {
	        logger.warn(e+" Error en la inserción de datos: SqlBaseConsecutivosDisponiblesDao "+e.toString() );
	        return false;
	    }	    
	}
	
	/**
	 * Método usado para modificar un consecutivo disponible por institucion
	 * @param con
	 * @param nombre
	 * @param valor
	 * @param anioVigencia
	 * @param institucion
	 * @param checkbox
	 * @return
	 */
	public synchronized static boolean modificar (Connection con, String nombre,
									String valor,String anioVigencia,
									int institucion, boolean checkbox)
	{
	    int resp = 0;
	    PreparedStatement pst=null;
	    PreparedStatement pst2=null;
	    try
		{
	        if (con == null || con.isClosed()) 
			{
	            DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();	            
			}		                   
		    String modificar = modificarStr;
		        	            
	        modificar+=" valor = '"+valor+"'";			    
	        
	        
	        modificar+=" ,anio_vigencia = '"+anioVigencia.trim()+"'";
	        
	        modificar+=" WHERE nombre = '"+nombre+"' AND institucion="+institucion;		    
	        pst= con.prepareStatement(modificar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);		    
			resp=pst.executeUpdate();		
			
			//modificarUsoConsecutivosStr
			
		    String modificarCUStr = modificarUsoConsecutivosStr;
            
		    modificarCUStr+=" valor = '"+valor+"'";			    
	        
	       	            
		    modificarCUStr+=" ,anio_vigencia = '"+anioVigencia.trim()+"'";
	        
	        modificarCUStr+=" WHERE nombre = '"+nombre+"' AND institucion="+institucion+" AND usado is null AND finalizado is null";
	        logger.info("modificar consecutivo "+modificarCUStr );

	        pst2= con.prepareStatement(modificarCUStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);		    
			resp=pst2.executeUpdate();		
			pst2.close();
			
			
				return true;
			
		 }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en la modificacion Consecutivos: SqlBaseConsecutivosDisponiblesDao "+e.toString() );
		   return false;
	    }
	    finally{
	    	try{
				if(pst2!=null){
					pst2.close();
				}
				if(pst!=null){
					pst.close();
				}
			}
			catch (SQLException e) {
				logger.error("ERROR cerrando Objetos Persistentes", e);
			}
	    }
	}
	
	/**
	 * metodo para realizar la consulta de un
	 * consecutivo
	 * @param con Connection
	 * @param institucion int
	 * @param modulo int
	 * @param parametro String 
	 * @return HashMap
	 * @author jarloc
	 */
	public static HashMap consultarConsecutivo (Connection con,int institucion,int modulo,String parametro)
	{
	    try
	    {
	        PreparedStatementDecorator ps = null;	        
	        String consulta=consultaConsecutivosXModuloStr+" AND c.nombre=?";	        
	        if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			} 
	        ps=new PreparedStatementDecorator(con, consulta);
	        ps.setInt(1,institucion);
	        ps.setInt(2,modulo);
	        ps.setString(3,parametro);        
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
	        HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
	        rs.close();
	        ps.close();
	        return mapaRetorno;
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en la consulta: consultarConsecutivo "+e.toString() );
	        return null;
	    }
	}	
	
	/**
	 * Metodo para la actualizacion del valor
	 * @param con
	 * @param valor
	 * @param nombre
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean actualizarValorConsecutivoStr(	Connection con, 
																			String valor, 
																			String nombre,
																			int codigoInstitucion) 
	{
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		try
		{
		    if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			pst= con.prepareStatement(actualizarValorConsecutivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setString(1,valor);
			pst.setString(2, nombre);
			pst.setInt(3,codigoInstitucion);
			if(pst.executeUpdate()>0)
			{
				pst2= con.prepareStatement("UPDATE uso_consecutivos SET valor=? WHERE nombre=?  AND institucion=? and usado is null and finalizado is null",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst2.setString(1,valor);
				pst2.setString(2, nombre);
				pst2.setInt(3,codigoInstitucion);
				if(pst2.executeUpdate()>0)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
			    return false;
			}
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la actualizarValorConsecutivoStr de datos: SqlBaseConsecutivosDisponiblesDao "+e.toString());
			return false; 						
		}	
	    finally{
	    	try{
				if(pst2!=null){
					pst2.close();
				}
				if(pst!=null){
					pst.close();
				}
			}
			catch (SQLException e) {
				logger.error("ERROR cerrando Objetos Persistentes", e);
			}
	    }
	}
	
	/**
	 * metodo para consultar los consecutivos por modulo
	 * @param con Connection
	 * @param institucion int
	 * @param modulo int 
	 * @return HashMap
	 * @author jarloc
	 */
	public static HashMap consultaConsecutivosXModulo (Connection con,int institucion,int modulo,ArrayList rest)
	{
	    try
	    {
	        PreparedStatementDecorator ps = null;
	        String query=consultaConsecutivosXModuloStr;
	        
	        if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			} 
	        if(!rest.isEmpty())
	        {
	            query+="  "; 
	            Iterator it=rest.iterator();
	            while(it.hasNext())
	            {	                
	                query+="AND cm.nombre!='"+it.next()+"'";	                
	            }	           
	        }
	        query+=" ORDER BY cm.descripcion";
	        logger.info("--->"+query+"\n-->"+modulo);
	        ps=  new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,institucion);
	        ps.setInt(2,modulo);
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
	        HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
	        rs.close();
	        ps.close();
	        return mapaRetorno;
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en la consulta: consultaConsecutivosXModulo "+e.toString() );
		   return null;
	    }
	}
	/**
	 * Metodo para consultar los consecutivos de 
	 * inventarios para los casos de consecutivos por
	 * almacen y unico en el sistema
	 * @param con Connection
	 * @param nombre String 
	 * @param institucion int 
	 * @return HashMap
	 * @author jarloc
	 */
	public static HashMap consultaConsecutivosInventarios(Connection con,String nombre,int institucion,int almacen,boolean esUnRegistro,int transaccion)	
	{
	    try
	    {
	        PreparedStatementDecorator ps = null;	
	        String query=consultaConsecutivosInventariosStr;
	        if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			} 
	        if(nombre.equals(ConstantesBDInventarios.codigoConsecutivoTransXAlmacen))
	            query+=" and ci.almacen="+almacen;
	        else if(nombre.equals(ConstantesBDInventarios.codigoConsecutivoTransUnicoSistema))
	            query+=" and ci.almacen is null";
	        query+=")";	        
	        if(esUnRegistro)
	        {
	            query+=" where ci.tipo_transaccion="+transaccion; 
	        }
	        else
	        {
	            String temp="",valor="";            
	            boolean esPrimero=true;
	            Vector rest=new Vector();	            
	            ValoresPorDefecto.cargarValoresIniciales(con);
	            rest.add(ValoresPorDefecto.getCodigoTransSoliPacientes(institucion,true));
	            rest.add(ValoresPorDefecto.getCodigoTransDevolPacientes(institucion,true));
	            rest.add(ValoresPorDefecto.getCodigoTransaccionPedidos(institucion,true));
	            rest.add(ValoresPorDefecto.getCodigoTransDevolucionPedidos(institucion,true));
	            Iterator it=rest.iterator();	            
	            while(it.hasNext())
	            {	                
	                valor=it.next()+"";
	                if(!valor.equals(""))
	                {	                    
	                    if(!esPrimero)
	                        temp+=",";
	                    temp+=valor;    
	                    esPrimero=false; 
	                }
	            }
	            if(!temp.equals(""))
	                query+=" WHERE tti.consecutivo not in("+temp+")";
	        }
	        ps=  new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,institucion);
	        ps.setString(2,nombre);	   
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());	        
	        HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
	        rs.close();
	        ps.close();
	        return mapaRetorno;
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en la consulta: consultaConsecutivosInventarios"+e.toString() );
		   return null;
	    } 
	}
	/**
	 * metodo para insertar consecutivos
	 * inventarios por almacen o unico
	 * en el sistema
	 * @param con Connection
	 * @param almacen int
	 * @param transaccion int
	 * @param institucion int
	 * @param nombre String
	 * @param valor String
	 * @param anioVig String
	 * @return booelan
	 * @author jarloc
	 */
	public static boolean insertarConsecutivoInventarios (Connection con,int almacen,int transaccion,int institucion,String nombre,String valor,String anioVig,String query) 
	{
	    int resp = 0;	    
	    try
		  {
		      if (con == null || con.isClosed()) 
				{
					DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
					con = myFactory.getConnection();
				}
		      PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		      if(almacen!=ConstantesBD.codigoNuncaValido)
		          ps.setInt(1,almacen);
		      else
		          ps.setObject(1,null);
		      ps.setInt(2,transaccion);
		      ps.setInt(3,institucion);
		      ps.setString(4,nombre);
		      ps.setString(5,valor);
		      if(UtilidadTexto.isEmpty(anioVig))
		    	  anioVig=" ";
	    	  ps.setString(6,anioVig);
		      resp=ps.executeUpdate();		      
		      if(resp < 0)
		          return  false;
		      else
		          return  true;		      
		  }
	    catch (SQLException e)
	    {
	        logger.warn(e+" Error en la inserción de datos: insertarConsecutivoInventarios "+e.toString() );
	        return false;
	    }	    
	}
	/**
	 * metodo para modificar consecutivo sinventarios
	 * @param con Connection 
	 * @param nombre String
	 * @param valor String
	 * @param anioVigencia String
	 * @param institucion int
	 * @param checkbox boolean
	 * @return boolean
	 * @author jarloc
	 */
	public static boolean modificarConsecutivoInv (Connection con, String nombre,String valor,
	        									   String anioVigencia,int institucion, boolean checkbox,
	        									   int almacen,int transaccion)
	{
		int resp = 0;
		String modificar = modificarConsecInvStr;		
		try
		{
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();	            
			}		        
			modificar+=" valor = '"+valor+"'";			
			if(checkbox)
			{
				if(anioVigencia.equals(""))
				anioVigencia="-1";	            
				modificar+=" ,anio_vigencia = '"+anioVigencia+"'";
			} 
			modificar+=" WHERE nombre = '"+nombre+"' AND institucion="+institucion+" AND tipo_transaccion="+transaccion;
			if(almacen==ConstantesBD.codigoNuncaValido)
			    modificar+=" AND almacen IS NULL";
			else
			    modificar+=" AND almacen="+almacen;
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	
			resp=ps.executeUpdate();			
			if(resp>0)
			return true;
			else
			return false;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en la modificacion Consecutivos: modificarConsecutivoInv "+e.toString() );
			return false;
		}	    
	}
	/**
	 * metodo para obtener el valor de un consecutivo
	 * de los casos especiales de inventarios
	 * @param con Connection
	 * @param tipoTrans int, código del tipo de trnasacción
	 * @param almacen int, código del almacen
	 * @return int, valor del consecutivo
	 * @author jarloc
	 */
	public static int obtenerConsecutivoInventario (Connection con,int tipoTrans,int almacen,int institucion)
	{	   
	    try
	    {
	        if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			} 
	        PreparedStatementDecorator ps = null;	        
	        String consulta=obtenerConsecutivoInventariosStr;
	        if(almacen==ConstantesBD.codigoNuncaValido)
	            consulta+=" AND almacen IS NULL";
	        else
	            consulta+=" AND almacen="+almacen;	        
	        ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        
	        ps.setInt(1,tipoTrans);
	        ps.setInt(2,institucion);	        
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
	        while(rs.next())
	            return rs.getInt("valor");
	    }
	    catch(SQLException e)
	    {
	    	e.printStackTrace();
	        logger.warn(e+"Error en la consulta: obtenerConsecutivoInventario "+e.toString() );		   
	    }
	    return ConstantesBD.codigoNuncaValido;
	}
	/**
	 * Metodo para actualiazar el valor del
	 * consecutivo de inventario
	 * @param con Connection
	 * @param valor int, valor para actualizar
	 * @param tipoTransaccion int, código del tipo de transacción
	 * @param almacen int, código del almacen
	 * @param institucion int, código de la institución
	 * @return boolean
	 * @author jarloc
	 */
	public static boolean actualizarValorConsecutivoInventarios (Connection con,int valor,int tipoTransaccion,int almacen,int institucion)
	{
		int resp = 0;				
		try
		{
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();	            
			}		 
			String modificar=actualizarConsecutivoInventariosStr;
		    if(almacen==ConstantesBD.codigoNuncaValido)
		        modificar+=" AND almacen IS NULL";
		    else
		        modificar+=" AND almacen="+almacen;			

			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,valor);
			ps.setInt(2,tipoTransaccion);
			ps.setInt(3,institucion);
			resp=ps.executeUpdate();			
			if(resp>0)
			    return true;
			else
			    return false;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en la modificacion Consecutivos: actualizarValorConsecutivoInventarios "+e.toString() );
			return false;
		}	    
	}
    /**
     * metodo para consultar los modulos
     * @param con Connection
     * @return HashMap
     * @author jarloc
     */
    public static HashMap consultaModulos (Connection con)
    {
        try
        {
            PreparedStatementDecorator ps = null;                      
            if (con == null || con.isClosed()) 
            {
                DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
                con = myFactory.getConnection();
            } 
            ps=  new PreparedStatementDecorator(con.prepareStatement(consultarModulosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            logger.info("------------------->>>"+consultarModulosStr);
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
	        rs.close();
	        ps.close();
	        return mapaRetorno;
        }
        catch(SQLException e)
        {
            logger.warn(e+"Error en la consulta: consultaModulos "+e.toString() );
            return null;
        }
    }   
    
    
    /**
     * Actualiza la informacion del consecutivo empresas institucion
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static boolean actualizarConsecutivoMultiEmpresa (Connection con,HashMap parametros)
	{						
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(actualizarValorConsecutivosMultiEmpresaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			if(!parametros.get("valor").equals(""))
				ps.setObject(1,parametros.get("valor"));
			else
				ps.setNull(1,Types.NULL);
			
			if(!parametros.get("anio").equals(""))
				ps.setObject(2,parametros.get("anio"));
			else
				ps.setNull(2,Types.NULL);				

			ps.setObject(3,parametros.get("institucion"));
			ps.setObject(4,parametros.get("empresa"));
			
			if(ps.executeUpdate()>0)			
				return true;
			else
			    return false;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en la modificacion Consecutivos: actualizarValorConsecutivoMultiEmpresa "+e.toString() );
			return false;
		}	    
	}
	
	/**
	 * Validacion del tipo de consecutivo a manejar de facturas varias. Anexo 562
	 * @param con
	 * @return
	 */
	public static int validaTipoConsecutivoFacturasVarias(Connection con)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		int bnd=0;
		
		PreparedStatementDecorator pst2;
		
		try{
			logger.info("VALIDACION DEL TIPO CONSECUTIVO FACTURAS VARIAS--->"+consultaTipoConsecutivoFV);
			
			pst2 =  new PreparedStatementDecorator(con.prepareStatement(consultaTipoConsecutivoFV, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs= new ResultSetDecorator(pst2.executeQuery());
			
			if (rs.next())
				bnd=1;
			
			pst2.close();
			rs.close();
			
			
			
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de clase y grupo de Articulo principal");
		}		
		return bnd;
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar si un consecutivo ha 
	 * sido usado
	 * 
	 * @param Connection con,String nombreParametro
	 * @return String
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static String consultarConsecutivoUsado(Connection con,String nombreParametro){
		String resultado="";
		PreparedStatement pst=null;
		ResultSet rs=null;
		try{
			pst= con.prepareStatement(consultaConsecutivoModificable,ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet);
			pst.setString(1,nombreParametro);
			rs=pst.executeQuery();
			
			if(rs.next()){
				resultado = rs.getString("usado");
			}
		
		}catch (SQLException e)
		{
			logger.error(e+" Error en consulta de consecutivo usado");
		}	
		finally{
			try{
				if(rs!=null){
					rs.close();
				}
				if(pst!=null){
					pst.close();
				}
			}
			catch (SQLException e) {
				logger.error("ERROR cerrando Objetos Persistentes", e);
			}
		}		
		return resultado;
	}
	
}