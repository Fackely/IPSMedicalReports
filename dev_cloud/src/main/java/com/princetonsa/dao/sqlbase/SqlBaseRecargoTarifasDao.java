
/*
 * Creado   14/10/2004
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
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para un recargo de tarifa 
 *
 * @version 1.0, 17/08/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan Lopez</a>
 */
public class SqlBaseRecargoTarifasDao {
    
    /**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseAdminMedicamentosDao.class);
    
    
	/**
	 * Almacena la consulta de Recargo de tarifas
	 */
	public static String consultarRecargoTarifaStr="select " +
																"rt.codigo as codigo, " +
																"rt.porcentaje as porcentaje, " +
																"rt.valor as valor, " +
																"CASE WHEN rt.via_ingreso IS NULL THEN 0 ELSE rt.via_ingreso END as via_ingreso, " +
																"rt.especialidad as especialidad, " +
																"rt.contrato as contrato, " +
																"rt.servicio as servicio, " +
																"rt.tipo_recargo as tipo_recargo, " +
																"ct.numero_contrato as numero_contrato, " +
																"co.nombre as nombre_convenio, " +
																"CASE WHEN rt.via_ingreso IS NULL THEN 'Todas' ELSE getNombreViaIngreso(rt.via_ingreso) END as nombre_via, " +
																"rt.tipo_paciente as codigo_tipo_paciente, " +
																"CASE WHEN rt.tipo_paciente IS NULL THEN 'Todos' ELSE getnombretipopaciente(rt.tipo_paciente) END as tipo_paciente," +
																"getNombreTipoRecargo(rt.tipo_recargo) as nombre_recargo, " +
																"CASE WHEN rt.especialidad IS NULL THEN 'Todas' ELSE getNombreEspecialidad(rt.especialidad) END as nombre_especialidad, " +
																"CASE WHEN rt.servicio IS NULL THEN 'Todos' ELSE getNombreServicio(rt.servicio,"+ConstantesBD.codigoTarifarioCups+") END as nombre_servicio " +
																"from recargos_tarifas rt " +
																"inner join contratos ct on (ct.codigo = rt.contrato) " +
																"inner join convenios co on (co.codigo = ct.convenio) " ;
	
	/**
	 * Almacena el String de la consulta de todos los registros existentes (tabla = recarga_tarifas)
	 */
	public static String consultaGeneralRecargoTarifasStr="select " +
																	"rt.contrato as contrato, " +
																	"rt.especialidad as especialidad, " +
																	"rt.servicio as servicio, " +
																	"rt.tipo_recargo as tipo_recargo, " +
																	"rt.via_ingreso as via_ingreso," +
																	"getnombretipopaciente(rt.tipo_paciente) as tipo_paciente " +
																	"from recargos_tarifas rt";
	
	/**
	 * Almacena el string para un recargo especifico, segun el codigo de RecargoTarifas.
	 */
	public static String consultaUnRecargoTarifaStr = 
													  "where rt.codigo = ? ";
	
	/**
	 * Obtiene el último cod de la sequence para Pedido 
	 */
	private final static String ultimaSequenciaStr= "SELECT MAX(codigo) AS seq_recargos_tarifas FROM recargos_tarifas ";
	
	/**
	 * Almacena el query de eliminacion de un recargo.
	 */
	private final static String eliminarRecargoStr="DELETE FROM recargos_tarifas WHERE codigo = ?";
	
	
	/**
	 * Inserta un recargo a una tarifa 
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param porcentaje. double, porcentaje de recargo de la tarifa
	 * @param valor. double, valor de recargo de la tarifa
	 * @param codigoTipoRecargo. int, código del tipo de recargo
	 * @param codigoServicio. int, código del servicio asociado esta tarifa, 0 es para todos
	 * @param codigoEspecialidad. int, código de la especialidad asociado esta tarifa, 0 es para todas
	 * @param codigoViaIngreso. int, código de la via de ingreso asociado esta tarifa, 0 es para todas
	 * @param codigoContrato. int, código del contrato para el cual es válido este recargo
	 * @return int, 0 no efectivo, >0 efectivo.
	 * @see com.princetonsa.dao.RecargoTarifasDao#insertar(java.sql.Connection, double, double, int, int, int, int, int)
	 */
	public static int insertarRecargoTarifa (Connection con, double porcentaje,
															 double valor,
															 int codigoViaIngreso,
															 String tipoPaciente,
															 int codigoEspecialidad,
															 int codigoContrato,
															 int codigoServicio,
															 int codigoTipoRecargo,
															 String insertarRecargoStr)
	{
		int resp=0;

		try
		{
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}

			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarRecargoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,porcentaje);
			ps.setDouble(2,valor);

			//si el codigo es 0 (todos), se inserta como null para evitar conflictos de inicialoizacion.
			if(codigoViaIngreso == 0)
				ps.setObject(3,null);
			else
				ps.setInt(3,codigoViaIngreso);
			if (!tipoPaciente.equals("-1"))
				ps.setString(4, tipoPaciente);
			else
				ps.setNull(4,Types.VARCHAR);
			//si el codigo es 0 (todos), se inserta como null para evitar conflictos de inicialoizacion.
			if(codigoEspecialidad == 0)
				ps.setObject(5,null);
			else
				ps.setInt(5,codigoEspecialidad);

			ps.setInt(6,codigoContrato);			

			//si el codigo es 0 (todos), se inserta como null para evitar conflictos de inicialoizacion.
			if(codigoServicio == 0)
				ps.setObject(7,null);
			else
				ps.setInt(7,codigoServicio);

			ps.setInt(8,codigoTipoRecargo);

			resp=ps.executeUpdate();

		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseRecargoTarifasDao "+e.toString() );
			resp=0;
		}

		return resp;  

	}

	
	/**
	 * Metodo que realiza la consulta de un registro de tarifa especifico.
	 * @param con, Connection con la fuente de datos.
	 * @param codigo, Codigo del recargo de tarifa.
	 * @return ResultSet.
	 * @see com.princetonsa.dao.RecargoTarifasDao#consultaRecargoTarifa(java.sql.Connection,int)
	 */
	public static ResultSetDecorator consultaRecargoTarifa (Connection con, int codigo)
	{
	    try
	    {
	        if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}  
	        
	        PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultarRecargoTarifaStr+consultaUnRecargoTarifaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,codigo);
	        logger.info("consulultar recargos>>>>"+consultarRecargoTarifaStr+consultaUnRecargoTarifaStr);
	        return new ResultSetDecorator(ps.executeQuery());
	        
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en la consulta de recargo tarifa, con el codigo->"+codigo+" "+": SqlBaseRecargoTarifasDao "+e.toString() );
		   return null;
	    }
	}
	
	/**
	 * Carga el siguiente codigo recargo   (table= recargo_tarifas))
	 * @param con Connection con la fuente de datos
	 * @return int ultimoCodigoSequence, 0 no efectivo.
	 * @see com.princetonsa.dao.RecargoTarifasDao#cargarUltimoCodigoSequence(java.sql.Connection)
	 */
	public static int cargarUltimoCodigoSequence(Connection con)
	{
		int ultimoCodigoSequence=0;
		try
		{
			PreparedStatementDecorator cargarUltimoStatement= new PreparedStatementDecorator(con.prepareStatement(ultimaSequenciaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs= new ResultSetDecorator(cargarUltimoStatement.executeQuery());
			if(rs.next())
			{
				ultimoCodigoSequence=rs.getInt(1);
				return ultimoCodigoSequence;
			}
			else
			{
				return 0;
			}
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del último codigo recargo: SqlBaseRecargoTarifasDao "+e.toString());
			return 0;
		}
	}
	
	/**
	 * Metodo para consultar todos los registros existentes de recargos.
	 * @param con, Connection con la fuente de datos.
	 * @return ResultSet, con todos los registros de recargos.
	 * @see com.princetonsa.dao.RecargoTarifasDao#consultaTodos(java.sql.Connection)
	 */
	public static ResultSetDecorator consultaTodos (Connection con)
	{
	    
	    try
	    {
	        if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}  
	        
	        PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultaGeneralRecargoTarifasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        logger.info("consultar todos >>>"+consultaGeneralRecargoTarifasStr);
	        return new ResultSetDecorator(ps.executeQuery());
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en la consultaTodos: SqlBaseRecargoTarifasDao "+e.toString() );
		   return null;
	    }
	}
	
	/**
	 * Metodo para realizar la consulta avanzada filtrando por los parametros elegidos.
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param porcentaje. double, porcentaje de recargo de la tarifa
	 * @param valor. double, valor de recargo de la tarifa
	 * @param codigoTipoRecargo. int, código del tipo de recargo
	 * @param codigoServicio. int, código del servicio asociado esta tarifa, 0 es para todos
	 * @param codigoEspecialidad. int, código de la especialidad asociado esta tarifa, 0 es para todas
	 * @param codigoViaIngreso. int, código de la via de ingreso asociado esta tarifa, 0 es para todas
	 * @param codigoContrato. int, código del contrato para el cual es válido este recargo
	 * @param nombreEspecialidad. String, nombre de la especialidad asociado esta tarifa
	 * @param nombreServicio. String, nombre del servicio asociado esta tarifa
	 * @return ResultSet, lista con los datos obtnidos de la consulta.
	 * @see com.princetonsa.dao.RecargoTarifasDao#consultaAvanzada(java.sql.Connection, double, double, int, int, int, int, int, String, String)
	 */
	public static ResultSetDecorator consultaAvanzada(Connection con,
	        
															        double porcentaje,
																	double valor,
																	int codigoViaIngreso,
																	String tipoPaciente,
																	int codigoEspecialidad,
																	int codigoContrato,
																	int codigoServicio,
																	int codigoTipoRecargo,
																	String nombreEspecialidad,
																	String nombreServicio) throws SQLException
{
	PreparedStatementDecorator ps;
    ResultSetDecorator rs=null;
	String consulta = new String(consultarRecargoTarifaStr);
	String condiciones="";
	String clausula=" WHERE ";
	String inner="";
	boolean existeFiltro=false;
	
		
	if(!nombreEspecialidad.equals("") && codigoEspecialidad == -1)
	{
	   inner=inner.concat("inner join especialidades es on (es.codigo = rt.especialidad) ");
	   consulta=consulta+inner;
	   condiciones=condiciones.concat(" AND ");
	   condiciones=condiciones.concat("UPPER(es.nombre) LIKE UPPER('%"+nombreEspecialidad+"%') ");
	   existeFiltro=true;
	}
	
	if(!nombreServicio.equals("") && codigoServicio == -1)
	{
	    inner=inner.concat("inner join referencias_servicio rs on (rt.servicio = rs.servicio AND rs.tipo_tarifario = "+ConstantesBD.codigoTarifarioCups+") ");
	    consulta=consulta+inner;
	    condiciones=condiciones.concat(" AND ");
		condiciones=condiciones.concat("UPPER(rs.descripcion) LIKE UPPER('%"+nombreServicio+"%') ");
		existeFiltro=true;
	}
	
	if(codigoContrato != 0)
	{
	    condiciones=condiciones.concat(" AND ");
	    condiciones=condiciones.concat("rt.contrato="+codigoContrato+" ");
	    existeFiltro=true;
	}
	
	if(porcentaje != 0.0)
	{
	    condiciones=condiciones.concat(" AND ");
	    condiciones=condiciones.concat("rt.porcentaje="+porcentaje+" ");
	    existeFiltro=true;
	}
	if(valor != 0)
	{
	    condiciones=condiciones.concat(" AND ");
	    condiciones=condiciones.concat("rt.valor="+valor+" ");
	    existeFiltro=true;
	}
	if(codigoViaIngreso != 0 && codigoViaIngreso != -1)
	{
	    condiciones=condiciones.concat(" AND ");
	    condiciones=condiciones.concat("rt.via_ingreso="+codigoViaIngreso+" ");
	    existeFiltro=true;
	
	    if(!tipoPaciente.equals("0")  && !tipoPaciente.equals("-1"))
		{
		    condiciones=condiciones.concat(" AND ");
		    condiciones=condiciones.concat("rt.tipo_paciente='"+tipoPaciente+"' ");
		    existeFiltro=true;
		}
	
	}
	if(codigoEspecialidad != -1)
	{
	    condiciones=condiciones.concat(" AND ");
	    condiciones=condiciones.concat("rt.especialidad="+codigoEspecialidad+" ");
	    existeFiltro=true;
	}
	
		
	if(codigoServicio != -1)
	{
	    condiciones=condiciones.concat(" AND ");
	    condiciones=condiciones.concat("rt.servicio ="+codigoServicio+" "); 
	    existeFiltro=true;
	}
	if(codigoTipoRecargo != 0 && codigoTipoRecargo != -1)
	{
	    condiciones=condiciones.concat(" AND ");
	    condiciones=condiciones.concat("rt.tipo_recargo ="+codigoTipoRecargo+" "); 
	    existeFiltro=true;
	}
	
	condiciones=condiciones.concat(" ORDER BY nombre_via,rt.servicio ASC");
	
	if(existeFiltro)
	{
	    condiciones=condiciones.replaceFirst(" AND ","");
	    consulta=consulta+clausula+condiciones;
	}
	else
	    consulta=consulta+condiciones;
	
	try
    {
		logger.info("CONSULTA AVANZADA >>>>"+consulta);
		
	    ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        rs=new ResultSetDecorator(ps.executeQuery());
    }
    catch(SQLException e)
    {
        logger.warn("Error en la consulta avanzada-> SqlBaseRecargoTarifasDao"+e.toString());
    }
    
    return rs;	
	}

	/**
	 * Método que elimina un recargo de tarifa dado su código  
	 * @param con, Connection con la fuente de datos.
	 * @param codigoRecargo, Codigo del recargo de tarifa por el cual se elimina.
	 * @return true si es efectivo de lo contrario false.
	 * @see com.princetonsa.dao.RecargoTarifasDao#eliminar(java.sql.Connection, int)
	 */
	public static boolean eliminar(Connection con, int codigoRecargo)
	{
		int resp=0;	
		try{
				if (con == null || con.isClosed()) 
				{
					throw new SQLException ("Error SQL para eliminar en SqlBaseRecargoTarifasDao: Conexión cerrada");
				}
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarRecargoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1,codigoRecargo);
				
				resp=ps.executeUpdate();
				if(resp>0)
					return true;
				else
					return false;
			}
			catch(SQLException e)
			{
				logger.warn(e+" Error en la eliminación de datos: SqlBaseRecargoTarifasDao "+e.toString());
				return false;			
			}	
	}
	
	
	/**
	 * Modifica un Recargo dado su código con los paramétros que fueron modificados.
	 * @param con Connection con la fuente de datos.
	 * @param codigoRecargo, codigo del Recargo
	 * @param porcentaje Porcentaje de recargo.
	 * @param valor Valor de recargo.
	 * @param codigoViaIngreso Codigo de la via de ingreso.
	 * @param codigoEspecialidad Codigo de la especialidad
	 * @param codigoServicio Codigo del servicio
	 * @param codigoTipoRecargo Codigo del tipo de recargo
	 * @return true modificó de lo contrario false.
	 * @see com.princetonsa.dao.RecargoTarifasDao#modificar(java.sql.Connection, int, double, double, int, int ,int, int)
	 */
	public static boolean modificar (Connection con,
	        										 int codigoRecargo,
	        										 double porcentaje,
	        										 double valor,
	        										 int codigoViaIngreso,
	        										 String tipoPaciente,
	        										 int codigoEspecialidad,
	        										 int codigoServicio,
	        										 int codigoTipoRecargo)
	
	{
	    int resp=0;	
	    boolean esPrimero=true;
	    double por=0.0,val=0.0;
	    
	    
		try
		{
				if (con == null || con.isClosed()) 
				{
					throw new SQLException ("Error SQL en modificar SqlBaseRecargoTarifasDao : Conexión cerrada");
				}
				
				String modificarRecargoStr="UPDATE recargos_tarifas SET ";
				
				if(codigoTipoRecargo > 0 )
				{	
				    modificarRecargoStr+=" tipo_recargo = "+codigoTipoRecargo;
				    esPrimero=false;
				}	
				
				if(codigoViaIngreso > 0 )
				{	
				    if(!esPrimero)
				        modificarRecargoStr+=" , ";
				    
				    esPrimero=false;
				    modificarRecargoStr+=" via_ingreso = "+codigoViaIngreso;					
				}	
				
				else if(codigoViaIngreso == 0)
				{	
				 if(!esPrimero)
				     modificarRecargoStr+=" , ";
					
				 	esPrimero=false;
				    modificarRecargoStr+=" via_ingreso = null ";					
				}	
				

				if(!tipoPaciente.equals("-1") )
				{	
				    if(!esPrimero)
				        modificarRecargoStr+=" , ";
				    
				    esPrimero=false;
				    modificarRecargoStr+=" tipo_paciente = '"+tipoPaciente+"' ";					
				}	
				else if(tipoPaciente.equals("-1"))
				{	
				 if(!esPrimero)
				     modificarRecargoStr+=" , ";
					
				 	esPrimero=false;
				    modificarRecargoStr+=" tipo_paciente = null ";					
				}	
				
				if(codigoServicio > 0 )
				{	
				    if(!esPrimero)
				        modificarRecargoStr+=" , ";
					
				    esPrimero=false;
				    modificarRecargoStr+=" servicio = "+codigoServicio;					
				}
				
				else if(codigoServicio == 0 )
				{	
				    if(!esPrimero)
				        modificarRecargoStr+=" , ";
					
				    esPrimero=false;
				    modificarRecargoStr+=" servicio = null ";
				}
				
				if(codigoEspecialidad > 0 )
				{	
				    if(!esPrimero)
				        modificarRecargoStr+=" , ";
					
				    esPrimero=false;
				    modificarRecargoStr+=" especialidad = "+codigoEspecialidad;					
				}
				
				else if(codigoEspecialidad == 0 )
				{	
				    if(!esPrimero)
				        modificarRecargoStr+=" , ";
					
				    esPrimero=false;
				    modificarRecargoStr+=" especialidad = null ";				    			    
				}
				
				if(porcentaje > 0 )
				{	
				    if(!esPrimero)
				        modificarRecargoStr+=" , ";
					    
				    esPrimero=false;
				    
				    por=porcentaje;
				    modificarRecargoStr+=" porcentaje = "+por+", valor = 0.0";					
				}
				
				if(valor > 0 )
				{	
				    if(!esPrimero)
				        modificarRecargoStr+=" , ";
					   
				    esPrimero=false;
				    
				    val=valor;
				    modificarRecargoStr+=" valor = "+val+", porcentaje = 0.0";					
				}
				
				modificarRecargoStr+=" WHERE codigo = "+codigoRecargo;
				
				logger.info(" SENTENCIA MODIFICACION >>>>"+modificarRecargoStr);
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarRecargoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));				
				resp=ps.executeUpdate();
				
				if(resp>0)
					return true;
				else
					return false;
				
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en la modificación de datos: SqlBaseRecargoTarifasDao "+e.toString());
			return false;			
		}	
	    
	      
	}
	
	
	/**
	 * Método que  carga  el resumen de la modificación de N recargos 
	 * en una BD PostgresSQL.
	 * @param con Connection con la fuente de datos.
	 * @param codigosModificados Codigos de los recargos que fueron modificados.
	 * @return ResultSetDecorator con lo registros.
	 * @see com.princetonsa.dao.RecargoTarifasDao#consultarRegistrosModificados(java.sql.Connection, int)
	 */
	public static ResultSetDecorator consultarRegistrosModificados (Connection con, Vector codigosModificados)
	{
	    PreparedStatementDecorator ps;
	    ResultSetDecorator rs=null;
	    String consulta = new String(consultarRecargoTarifaStr);
	    String separador = " , ";
	    
	    consulta = consulta + " WHERE rt.codigo IN ( ";
	    
	    for ( int k = 0; k < codigosModificados.size(); k ++ )
	    {
	        consulta = consulta + codigosModificados.get(k).toString();
	        
	        if ( k != codigosModificados.size()-1)
	        {
	            consulta = consulta + separador;
	        }
	        else
	            consulta = consulta + " )";
	    }
	    
	    
	    try
	    {
	    	logger.info("SENTENCIA CONSULTA MODIFICADOS >>"+consulta);
		    ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        rs=new ResultSetDecorator(ps.executeQuery());
	    }
	    catch(SQLException e)
	    {
	        logger.warn("Error en consultarRegistrosModificados -> SqlBaseRecargoTarifasDao"+e.toString());
	    }
	    
	    return rs;		    
	    
	}
}
