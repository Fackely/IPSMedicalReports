/*
 * Creado  17/08/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * Clase para manejar
 *
 * @version 1.0, 17/08/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan Lopez</a>
 */
public class SqlBaseRegUnidadesConsultaDao
{
    /**
     * Manejador de los Logs de la clase
     */
    private static Logger logger=Logger.getLogger(SqlBaseRegUnidadesConsultaDao.class);
    
    /**
     *String para realizar la consulta avanzada 
     */
    private static String consultaAvanzadaString="SELECT DISTINCT "+
    												"u.codigo, "+
    												"u.descripcion, "+
    												"u.activa," +
    												"getintegridaddominio(u.tipo_atencion) AS tipoatencion," +
    												"coalesce(esp.nombre, '') AS nomespecialidad, " +
    												"color AS color "+
    											"FROM "+
    												"unidades_consulta u " +
    												"LEFT OUTER JOIN servicios_unidades_consulta suc ON (suc.unidad_consulta=u.codigo) " +
    												"LEFT OUTER JOIN especialidades esp ON (esp.codigo=u.especialidad)"+
    											"WHERE "+
    												"1=1 ";
    
    /**
     * String para consultar por un solo campo
     */
    private static String consultaModificarStr="select codigo,descripcion,activa,especialidad,color,tipo_atencion  from unidades_consulta where ";
    /**
     * String para modificar.
     */
    private static String modificarUnidadStr="update unidades_consulta set descripcion=?,activa=?,especialidad=?,color=? where codigo=?";
    /**
     *String para eliminar un registro por codigo. 
     */
    private static String eliminarUnidadStr="DELETE  from unidades_consulta where codigo=?";
    /**
     * Consultar todos los registros.
     */
    private static String consultaTodoStr="select uc.codigo,uc.descripcion,uc.activa,coalesce(esp.nombre,'') AS nomespecialidad, getintegridaddominio(uc.tipo_atencion) AS tipoatencion from unidades_consulta uc LEFT OUTER JOIN especialidades esp ON(esp.codigo=uc.especialidad) order by descripcion asc ";
    /**
     * String para consultar el CUPS de tarifarios oficiales y la descripcion del 
     * servicio.
     */
    private static String consultaCupsStr="SELECT rs.descripcion, rs.servicio from referencias_servicio rs, tarifarios_oficiales ts where rs.tipo_tarifario=ts.codigo and ts.nombre='CUPS'";
    
    /**
     * Cadena que consulta los servicios de una unidad de consulta espec�fica
     */
    private static String obtenerServiciosUnidadConsultaStr = "SELECT "+ 
    	"coalesce(uc.especialidad||'','') AS codigo_especialidad, "+
    	"suc.codigo_servicio AS codigo_servicio, "+
    	"'(' || s.especialidad || '-' || suc.codigo_servicio || ') ' || getnombreservicio(suc.codigo_servicio,"+ConstantesBD.codigoTarifarioCups+") AS descripcion_servicio,"+
    	"coalesce(s.sexo,0) AS codigo_sexo, "+
    	"CASE WHEN s.espos = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS es_pos, " +
    	"tieneServicioCondiciones(suc.codigo_servicio,?) AS tiene_condiciones," +
    	"s.tipo_servicio AS tipo_servicio " +
    	"FROM servicios_unidades_consulta suc "+ 
    	"inner join servicios s on (s.codigo=suc.codigo_servicio) " +
    	"inner join unidades_consulta uc on (uc.codigo = suc.unidad_consulta) " +
    	"WHERE " +
    	"suc.unidad_consulta= ? ";


   private static String consultarEspecialidad = "SELECT codigo AS codigo, nombre AS descripicion FROM especialidades WHERE codigo = ? ";
   
   /**
    * Cadena que verifica si l aunidad de agenda esta asociada con un horario de atencion
    */
   private static String consultaUnidadConAsoHorarioAten = "SELECT COUNT(codigo) AS numregaso FROM horario_atencion WHERE unidad_consulta = ? ";
    
    /** 
     * Metodo para insertar los datos correpondientes a una unidad de consulta.
     * 
     * @param con Connection, Conexion a la base de datos.
     * @param Tipo_BD 
     * @param codigoEspecialidad 
     * @param descripcion, Descripcion de la unidad de consulta.
     * @param codservicio, Codigo del servicio.
     * @param estado, Estado Activo o Inactivo.
     */
    public static int insertarRegUniCon(Connection con, String descripcion, boolean activo, int especialidad, String cadena, int Tipo_BD, String tipoAtencion, String color)
    {
       try
	   {
    	
    	   
    	   int ban=0;
           PreparedStatementDecorator insertarRegistroStatement= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
           insertarRegistroStatement.setString(1,descripcion);
           switch(Tipo_BD)
    	   {
    	       	   case DaoFactory.ORACLE:
    	       		   
    	       		   if(activo==true)
    	       		   {
    	       			insertarRegistroStatement.setInt(2,1);
    	       		   }
    	       			else if (activo==false)
    	       			{
    	       				logger.info("falso");
    	       				insertarRegistroStatement.setInt(2,0);
    	       			}
    	       		   
    	       		 
    	       	break;
    	   case DaoFactory.POSTGRESQL:
    		   insertarRegistroStatement.setBoolean(2,activo);
    		   break;
    		   default:
    	   break;
    	   }
           
           if(especialidad!=ConstantesBD.codigoNuncaValido)
        	   insertarRegistroStatement.setInt(3, especialidad);
           else
        	   insertarRegistroStatement.setInt(3, Types.NULL);
           insertarRegistroStatement.setString(4, tipoAtencion);
           if(color.equals(""))
        	   insertarRegistroStatement.setNull(5, Types.VARCHAR);
           else
        	   insertarRegistroStatement.setString(5, color);
           
           insertarRegistroStatement.executeUpdate();
           
           PreparedStatementDecorator ps;
           ResultSetDecorator rs=null;
	       ps= new PreparedStatementDecorator(con.prepareStatement("SELECT max(codigo) as codigo from unidades_consulta",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	       rs=new ResultSetDecorator(ps.executeQuery());
	       if(rs.next())
	       	return rs.getInt("codigo");
       }
        catch(SQLException e)
        {
            logger.error("Error al insertar RegistroUnidadConsulta-> SqlBaseRegUnidadesConsultaDao"+e); 
        }
        return -1;
    }
    
    
   /**
    * Metodo para realizar la busqueda avanzada segun por los 
    * campos que se desee consultar.
    * 
     * @param con Connection, Conexion a la base de datos.
     * @param codigo Codigo de la unidad de consulta.
     * @param descripcion Descripcion de la unidad de consulta.
     * @param codServicio Codigo de la especialidad.
     * @param especialidad 
 * @param Tipo_BD 
     * @param codigoEspecialidad Codigo de la especialidad.
     * @param activo, Estado true o false.
     * @return ResultSetDecorator con toda la consulta.
     */
    public static ResultSetDecorator consultaAvanzada(Connection con, int codigo, String descripcion, int codServicio, boolean activo,int especialidad, String Temp, int Tipo_BD)
    {
        PreparedStatementDecorator ps;
        ResultSetDecorator rs=null;
        String consultaString;
        consultaString=consultaAvanzadaString;
       
       
        if(codigo>=0)
            consultaString+=" AND u.codigo= '"+codigo+"'";
        if(!descripcion.equals(""))
            consultaString+=" AND UPPER(u.descripcion) LIKE UPPER('%"+descripcion+"%')";
        if(codServicio>0)
            consultaString+=" AND suc.codigo_servicio= '"+codServicio+"'";
        if(!Temp.equals(""))
        {
        	        	switch(Tipo_BD)
            {
            case DaoFactory.ORACLE:
            	if(activo==false)
            		consultaString+=" AND u.activa= 0";
            	else if(activo==true) 
            		consultaString+=" AND u.activa=1";
        	            	break;
            case DaoFactory.POSTGRESQL:
            	consultaString+=" AND u.activa= '"+activo+"'";
            	break;
            	default:
            		}            
        }
        consultaString+=" ORDER BY u.descripcion asc";                        
        try
        {
        	logger.info("Consulta "+consultaString);
            ps= new PreparedStatementDecorator(con.prepareStatement(consultaString,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            rs=new ResultSetDecorator(ps.executeQuery());
        }
        catch(SQLException e)
        {
            logger.warn("Error en la consulta avanzada-> SqlBaseRegUnidadesConsultaDao"+e.toString());
        }
        
        return rs;
        
    }


/**
 * Metodo para realizar la busqueda del registro que se modificara,
 * segun el codigo correspondiente.
 * @param con, Connection con la BD.
 * @param codigo, Codigo por el cual se buscara.
 * @return ResultSet
 */
public static ResultSetDecorator consultaModificar(Connection con, int codigo)
{
    PreparedStatementDecorator ps;
    ResultSetDecorator rs=null;
    String consultaString="";
    consultaString=consultaModificarStr;
    
    consultaString+=" codigo= '"+codigo+"'";
    
    //logger.info("valor de la consulta >> "+consultaString);
   
    try
    {
        ps= new PreparedStatementDecorator(con.prepareStatement(consultaString,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        rs=new ResultSetDecorator(ps.executeQuery());
    }
    catch(SQLException e)
    {
        logger.warn("Error en la consultaModificacion"+e.toString());   
    }
    return rs;
}


/**
 * Metodo empleado para realizar una consulta general, de todos
 * los registros presentes en la BD, para esta tabla.
 * @param con, Connection con la BD.
 * @return, ResultSet
 */
public static ResultSetDecorator consultarTodo(Connection con)
{
   PreparedStatementDecorator ps;
   ResultSetDecorator rs=null;
   try
   {
       if (con == null || con.isClosed()) 
		{
			throw new SQLException ("Error SQL: Conexi�n cerrada");
		}
       ps= new PreparedStatementDecorator(con.prepareStatement(consultaTodoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
       rs=new ResultSetDecorator(ps.executeQuery());
   }
   catch(SQLException e)
   {
       logger.warn(e+"Error en la ConsultaTodo"+e.toString());
   }
   return rs;
}


/**
 * 
 * @param con
 * @param servicio
 * @return
 */
public static ResultSetDecorator consultarCUPS(Connection con)
{
    PreparedStatementDecorator ps;
    ResultSetDecorator rs=null;
    try
    {
        if (con == null || con.isClosed()) 
		{
			throw new SQLException ("Error SQL: Conexi�n cerrada");
		}
        ps= new PreparedStatementDecorator(con.prepareStatement(consultaCupsStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        rs=new ResultSetDecorator(ps.executeQuery());
        
    }
    catch(SQLException e)
    {
        logger.warn(e+"Error en la ConsultaTodo"+e.toString()); 
    }
    return rs;
}


/**
 * Metodo empleado para modificar todos los campos de un registro, 
 * segun el codigo.
 * @param especialidad 
 * @param color 
 * @param con, Connection con la BD.
 * @param codigo, Codigo por el cual se modificara el registro.
 * @param descripcion, Campo Descripcion.
 * @param activa, Campo de Estado.
 */
public static int modificar(Connection con, int codigo, String descripcion,boolean activa, int especialidad, String color)
{
    int resp = 0;
    
    try
    {
        PreparedStatementDecorator modificarUnidadStatement;
        modificarUnidadStatement =  new PreparedStatementDecorator(con.prepareStatement(modificarUnidadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        //if(!descripcion.equals(""))
            modificarUnidadStatement.setString(1,descripcion);
        modificarUnidadStatement.setBoolean(2,activa);
        modificarUnidadStatement.setInt(3,especialidad);
        modificarUnidadStatement.setString(4,color);
        modificarUnidadStatement.setInt(5,codigo);
        resp = modificarUnidadStatement.executeUpdate();
        
    }
    catch(SQLException e)
    {
        logger.warn("Error en la modificacion-> SqlBaseRegUnidadesConsultaDao"+e.toString());
    }
    
    return resp;
}


/**
 * Metodo para eliminar un Registro.
 * @param con, Connection con la BD.
 * @param codigo, Codigo por el cual se eliminara el registro.
 * @return, int 1 exitoso, 0 de lo contrario.
 */
public static int eliminar(Connection con, int codigo)
{
   int elimino=0;
    try
    {
        if (con == null || con.isClosed()) 
		{
			throw new SQLException ("Error SQL: Conexi�n cerrada");
		}
		PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarUnidadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		ps.setInt(1,codigo);
		
		elimino=ps.executeUpdate(); 
    }
    catch(SQLException e)
    {
        logger.warn(e+" Error eliminando informacion->SqlBaseRegUnidadesConsultaDao "+e.toString());
        elimino=0;
    }
    return elimino; 
}


	/**
	 * @param con
	 * @param codigoUC
	 * @param servicio
	 * @return
	 */
	public static boolean insertarDetalle(Connection con, int codigoUC, int servicio,int especialidad) 
	{
	       try
		   {
	           PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("insert into servicios_unidades_consulta(unidad_consulta,codigo_servicio) values (?,?)",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	           ps.setInt(1,codigoUC);
	           ps.setInt(2,servicio);
	           
	           /*
	           if(especialidad != ConstantesBD.codigoNuncaValido)
	        	   ps.setInt(3,especialidad);
	           else
	        	   ps.setNull(3,Types.NULL);    	
	           */
	           
	           return ps.executeUpdate()>0;
	       }
	        catch(SQLException e)
	        {
	            logger.error("Error al insertar RegistroUnidadConsulta-> SqlBaseRegUnidadesConsultaDao"+e); 
	        }
		return false;
	}


	/**
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static ResultSetDecorator consultaServiciosModificacion(Connection con, int codigo) 
	{
		PreparedStatementDecorator ps;
	    ResultSetDecorator rs=null;

	    String consultaString = "SELECT " +
						    		"suc.codigo_servicio," +
						    		"getnombreespecialidad(uc.especialidad) AS descripcionespecialidad," +
						    		"s.tipo_servicio," +
						    		"s.especialidad AS especialidadser," +
						    		"rs.descripcion," +
						    		"rs.codigo_propietario as codigocups " +
					    		"FROM " +
					    			"servicios_unidades_consulta suc " +
					    		"INNER JOIN " +
					    			"referencias_servicio rs  on(rs.servicio=suc.codigo_servicio) " +
					    		"INNER JOIN " +
					    			"servicios s on (s.codigo=suc.codigo_servicio) " +
					    		"INNER JOIN " +
					    			"unidades_consulta uc ON (uc.codigo = suc.unidad_consulta) " +
					    		"WHERE " +
					    			"rs.tipo_tarifario=0 and suc.unidad_consulta=?";
	    try
	    {
	        ps= new PreparedStatementDecorator(con.prepareStatement(consultaString,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    	ps.setInt(1,codigo);
	        rs=new ResultSetDecorator(ps.executeQuery());
	    }
	    catch(SQLException e)
	    {
	        logger.warn("Error en la consultaModificacion"+e.toString());   
	    }
	    return rs;
	}


	/**
	 * @param con
	 * @param codigoT
	 * @param codigoServicio
	 * @return
	 */
	public static int eliminarServicio(Connection con, int codigoT, int codigoServicio) 
	{
		try
	    {
	        if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexi�n cerrada");
			}
	        String cadena="DELETE FROM servicios_unidades_consulta WHERE codigo_servicio = ? and unidad_consulta=?";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoServicio);
			ps.setInt(2,codigoT);			
			
			return ps.executeUpdate(); 
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+" Error eliminando informacion->SqlBaseRegUnidadesConsultaDao "+e.toString());
	        return ConstantesBD.codigoNuncaValido;
	    }
	}


	/**
	 * @param con
	 * @param codigoT
	 * @param servicioNuevo
	 * @param servicioAntiguo
	 * @return
	 */
	public static int modificarServico(Connection con, int codigoT, int servicioNuevo, int servicioAntiguo, int especialidad) 
	{
		try
	    {
	        if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexi�n cerrada");
			}
	        String cadena="UPDATE servicios_unidades_consulta SET codigo_servicio = ?, especialidad = ? where codigo_servicio=? and unidad_consulta=?";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,servicioNuevo);
			
			if(especialidad != ConstantesBD.codigoNuncaValido)
				ps.setInt(2,especialidad);
	        else
	        	ps.setNull(2,Types.NULL);
			
			ps.setInt(3,servicioAntiguo);
			ps.setInt(4,codigoT);			
			
			return ps.executeUpdate(); 
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+" Error MODIFICANDO->SqlBaseRegUnidadesConsultaDao "+e.toString());
	        return ConstantesBD.codigoNuncaValido;
	    }
	}


	/**
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static int eliminarDetalles(Connection con, int codigo)
	{
		try
	    {
	        if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexi�n cerrada");
			}
	        String cadena="DELETE FROM servicios_unidades_consulta WHERE unidad_consulta=?";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigo);
			return ps.executeUpdate(); 
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+" Error eliminando informacion->SqlBaseRegUnidadesConsultaDao "+e.toString());
	        return ConstantesBD.codigoNuncaValido;
	    }
	}
	
	/**
	 * M�todo que realiza la consulta de los servicios de la unuidad de consulta
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap<String, Object> obtenerServiciosUnidadConsulta(Connection con,HashMap campos)
	{
		try
		{
			String consulta = obtenerServiciosUnidadConsultaStr ;
			
			//Se verifica si se va a buscar por un servicio espec�fico
			if(!campos.get("codigoServicio").toString().equals(""))
				consulta += " AND suc.codigo_servicio = "+campos.get("codigoServicio");
			
			//Se pregunta si se debe filtrar el sexo del paciente
			if(UtilidadTexto.getBoolean(campos.get("validarSexoPaciente").toString()) 
					&& Utilidades.convertirAEntero(campos.get("codigoPaciente").toString()) > 0)
				consulta += " AND (s.sexo IS NULL OR s.sexo = getsexopaciente("+campos.get("codigoPaciente").toString()+") ) ";			
			
			consulta += " ORDER BY descripcion_servicio ";
			
			logger.info("valor de la sql >> "+consulta+" >> "+campos);
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setObject(1,campos.get("institucion"));
			pst.setObject(2,campos.get("codigoUnidadConsulta"));
			
			HashMap mapa = new HashMap();
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			logger.info("-----------------------------------------------------------------");
			Utilidades.imprimirMapa(mapa);
			logger.info("-----------------------------------------------------------------");
			return mapa; 
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerServiciosUnidadConsulta: "+e);
			return null;
		}
	}
	
	/**
	 * M�todo que realiza la consulta de la especialidad 
	 * @param con
	 * @param campos
	 * @return
	 */
	public static String obtenerEspecialidad(Connection con,HashMap campos)
	{
		String especialidad = "";
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarEspecialidad, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("codigo").toString()));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				especialidad = rs.getString("descripicion");
			pst.close();
		}
		catch(SQLException e){
			logger.error("Error en obtenerServiciosUnidadConsulta: "+e);
		}
		return especialidad;
	}
	
	/**
	 * Metodo que cuenta cuanta veces esta asociada una unidad de agenda con la tabla horario de atencion
	 * @param con
	 * @param unidad_consulta
	 * @return
	 */
	public static int verificarUniAgenAsoHorarioAten(Connection con, int unidad_consulta)
	{
		int numregaso = ConstantesBD.codigoNuncaValido;
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaUnidadConAsoHorarioAten , ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,unidad_consulta);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				numregaso = rs.getInt("numregaso");
			pst.close();
		}
		catch(SQLException e){
			logger.error("Error en obtenerServiciosUnidadConsulta: "+e);
		}
		return numregaso;
	}
	
}