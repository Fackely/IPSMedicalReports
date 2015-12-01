/*
 * Creado  27/09/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.RespuestaHashMap;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para un pedido de insumos.
 * 
 * @version 1.0, 27/09/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan Arlen L&oacute;pez Correa.</a>
 */
@SuppressWarnings("unchecked")
public class SqlBasePedidosInsumosDao
{
    /**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseAdminMedicamentosDao.class);
	
	/**
	 * String para almacenar la consulta de todos los centros de costo
	 */
	private final static String listadoCentrosCostoStr="select codigo, nombre, tipo_area from centros_costo";
	
	/**
	 * String para almacenar la consulta de los centros de costo de 
	 * tipo subalmacen.
	 */
	private final static String listadoCentrosCostoSubalmacenStr="select codigo as codigo, " +
																		"nombre as nombre, " +
																		"tipo_area as tipo_area " +
																		"from centros_costo cc " +
																		"where cc.tipo_area=(select codigo from tipos_area where nombre='Subalmacen')";
	
	/**
	 * Obtiene el último cod de la sequence para Pedido 
	 */
	private final static String ultimaSequenciaStr= "SELECT MAX(codigo) AS seq_pedido FROM pedido ";
	
	/**
	 * String para insertar una anulacion de pedido.
	 */
	private final static String insertarAnulacionPedidoStr="INSERT INTO  anulacion_pedidos " +
																			"(pedido, " +
																			 "motivo_anulacion, " +
																			 "fecha, "+
																			 "hora, "+
																			 "usuario)  "+
																			" VALUES (?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+", ? ) ";

	
	/**
	 * String para modificar un pedido.
	 */
	private final static String modificarPedidoStr="UPDATE pedido SET " +
																			"estado = ?, " +
																			"centro_costo_solicitante = ? , " +
																			"centro_costo_solicitado = ? , " +
																			"urgente = ? , " +
																			"observaciones_generales = ? " +
																			"WHERE codigo = ? ";

	
	
	
	/**
	 * String para insertar el detalle de pedido.
	 */
	private final static String insertarDetallePedidoStr="INSERT INTO detalle_pedidos (pedido,articulo,cantidad) VALUES (?,?,?)";

	
	/**
	 * String para modificar el detalle de pedido.
	 */
	private final static String modificarDetallePedidoStr="UPDATE detalle_pedidos SET cantidad = ? WHERE pedido = ? AND articulo = ? ";

	
	/**
	 * Strin para consultar un pedido especifico.
	 */
	private final static String consultarUnPedidoStr="select " +
																		"p.codigo as codigo, " +
																		"p.estado AS codigo_estado, "+
																		"getNombreEstadoPedido(p.estado) as estado, " +
																		"p.fecha_grabacion as fecha_grabacion, " +
																		"p.hora_grabacion as hora_grabacion, " +
																		"p.fecha as fecha, " +
																		"p.hora as hora, " +
																		"p.centro_costo_solicitante as centro_costo_solicitante, " +
																		"p.centro_costo_solicitado as centro_costo_solicitado, " +
																		"p.urgente as urgente, " +
																		"p.observaciones_generales as observaciones_generales, " +
																		"p.usuario as usuario, " +
																		"dt.articulo as articulo, " +
																		"dt.cantidad as cantidad, " +
																		"getNomUnidadMedida(v.unidad_medida) as unidad_medida, " +
																		"v.descripcion as descripcion, " +
																		"v.concentracion as concentracion, " +
																		"a.motivo_anulacion as motivo_anulacion, "+
																		"a.fecha AS fecha_anulacion, "+
																		"a.hora AS hora_anulacion, " +
																		"a.usuario AS usuario_anulacion, "+
																		"getNomFormaFarmaceutica(v.forma_farmaceutica) as forma_farmaceutica, " +
																		"gettotalexisarticulosxalmacen(p.centro_costo_solicitado,v.codigo,v.institucion) AS existencias "+
																		"from pedido p " +
																		"inner join detalle_pedidos dt on (dt.pedido = p.codigo) " +
																		"inner join view_articulos v on (dt.articulo = v.codigo) " +
																		"left outer join anulacion_pedidos a ON(a.pedido = p.codigo) "+
																		"where p.codigo=? "; 
 

	/**
	 * Cadena que consulta los datos generales de un pedido
	 */
	private static final String cargarDatosGeneralesPedidoStr="SELECT "+ 
		"estado AS codigo_estado,"+ 
		"getnomestadopedidos(estado) AS nombre_estado,"+ 
		"to_char(fecha_grabacion,'DD/MM/YYYY') AS fecha_grabacion,"+ 
		"hora_grabacion AS hora_grabacion,"+
		"to_char(fecha,'DD/MM/YYYY') AS fecha,"+ 
		"hora AS hora,"+ 
		"centro_costo_solicitante AS codigo_centro_costo,"+
		"getnomcentrocosto(centro_costo_solicitante) AS nombre_centro_costo,"+ 
		"centro_costo_solicitado AS codigo_farmacia,"+ 
		"getnomcentrocosto(centro_costo_solicitado) AS nombre_farmacia,"+
		"urgente AS urgente,"+
		"observaciones_generales AS observaciones,"+
		"usuario AS usuario "+ 
		"FROM pedido";
	
	/**
	 * Cadena que consulta el detalle de artículos de un pedido de insumos
	 */
	private static final String cargarDetallePedidoStr="SELECT "+ 
		"a.codigo ||', '|| a.descripcion AS articulo,"+ 
		"CASE WHEN a.concentracion IS NULL THEN '' ELSE a.concentracion END AS concentracion,"+ 
		"CASE WHEN getNomFormaFarmaceutica(a.forma_farmaceutica) IS NULL THEN '' ELSE getNomFormaFarmaceutica(a.forma_farmaceutica) END  AS formaFarmaceutica,"+	
		"CASE WHEN getNomUnidadMedida(a.unidad_medida) IS NULL THEN '' ELSE getNomUnidadMedida(a.unidad_medida) END AS unidadMedida,"+  
		"CASE WHEN getNomNaturalezaArticulo(a.naturaleza) IS NULL THEN '' ELSE getNomNaturalezaArticulo(a.naturaleza) END  AS naturaleza,"+
		"d.cantidad AS cantidadDespacho," +
		"gettotalexisarticulosxalmacen(c.codigo,a.codigo,c.institucion) AS existenciaXAlmacen "+ 
		"FROM articulo a " +
		"INNER JOIN detalle_pedidos d ON(a.codigo=d.articulo) " +
		"INNER JOIN pedido p ON(d.pedido=p.codigo) " +
		"INNER JOIN centros_costo c ON(c.codigo=p.centro_costo_solicitado) " +
		"WHERE ";
	
	/**
	 * Cadena que consulta los datos de anulación de un pedido
	 */
	private static final String cargarDatosAnulacionStr = "SELECT " +
		"motivo_anulacion AS motivo," +
		"to_char(fecha,'DD/MM/YYYY') AS fecha," +
		"hora AS hora, " +
		"usuario AS usuario " +
		"FROM anulacion_pedidos WHERE pedido= ?";
	
	/**
	 * Cadena que consulta los datos de la peticion del pedido
	 */
	private static final String cargarDatosPeticionPedidoStr = "SELECT "+ 
		"pq.codigo AS numero_peticion, "+
		"p.primer_apellido || ' ' || coalesce(p.segundo_apellido,'') || ' ' || p.primer_nombre || ' ' || coalesce(p.segundo_nombre,'') AS nombre_paciente, "+
		"p.tipo_identificacion || ' ' || p.numero_identificacion AS id_paciente, "+
		"coalesce(getconsecutivoingreso(c.id_ingreso),'') AS numero_ingreso "+ 
		"FROM pedidos_peticiones_qx ppq "+ 
		"INNER JOIN peticion_qx pq ON(pq.codigo=ppq.peticion) "+ 
		"INNER JOIN personas p ON(p.codigo=pq.paciente) "+ 
		"LEFT OUTER JOIN solicitudes_cirugia sc ON(sc.codigo_peticion=pq.codigo) "+ 
		"LEFT OUTER JOIN solicitudes s ON(s.numero_solicitud=sc.numero_solicitud) "+ 
		"LEFT OUTER JOIN cuentas c ON(c.id=s.cuenta) "+ 
		"WHERE "+ 
		"ppq.pedido = ?";
	/**
	 * metodo que refresca la conexion con la fuente de datos
	 * @param con  una conexion con la fuente de datos que der nula abre una nueva conexion
	 * @return  la misma conexion si esta vigente o una nueva de ser nula
	 */
	
	public static Connection refreshDBConnection(Connection con){
		
		try{
		  if (con == null || con.isClosed()){
			  DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			  con = myFactory.getConnection();
		  }
		}
		catch(SQLException e){
				logger.warn(" Error al intentar reabrir la conexion con la base de datos [Clase: SqlBaseDevolucionPedidos ]" + e);
		}
    
		return con;		 		
	}
	
	
	/**
	 * Carga el Listado de todos los centros de costo.
	 * @param con, Connection, conexión abierta con una fuente de datos.
	 * @return ResulSet list.
	 */
	public static ResultSetDecorator listadoCentrosCosto(Connection con)
	{
		try
		{
			PreparedStatementDecorator listadoStatement= new PreparedStatementDecorator(con.prepareStatement(listadoCentrosCostoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return new ResultSetDecorator(listadoStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del listado de los centros de costo: SqlBasePedidosInsumoDao "+e.toString());
			return null;
		}
	}
	
	/**
	 * Carga el Listado de los centros de costo que sean de tipo Subalmacen.
	 * @param con, Connection, conexión abierta con una fuente de datos.
	 * @return ResulSet list.
	 */
	public static ResultSetDecorator listadoCentrosCostoSubalmacen(Connection con)
																	        
	{
		try
		{
			PreparedStatementDecorator listadoStatement= new PreparedStatementDecorator(con.prepareStatement(listadoCentrosCostoSubalmacenStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return new ResultSetDecorator(listadoStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del listado de los centros de costo de tipo Subalmacen: SqlBasePedidosInsumoDao "+e.toString());
			return null;
		}
	}
	
	/**
	 * Carga el siguiente codigo pedido   (table= pedido))
	 * @param con Connection con la fuente de datos
	 * @return int ultimoCodigoSequence, 0 no efectivo.
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
			logger.warn(e+" Error en la consulta del último codigo pedido: SqlBasePedidosInsumosDao "+e.toString());
			return 0;
		}
	}
	
	/**
	 * Metodo para insertar un Pedido (tabla = <code>pedido</code>).
	 * @param esQuirurgico2 
	 * @param con, Connection con la fuente de datos.
	 * @param fecha, fecha de grabación.
	 * @param hora, hora de grabación.
	 * @param centroCostoSolicitante, centro de costo que solicita.
	 * @param centroCostoSolicitado, centro de costo que despecha.
	 * @param urgente,  true ó false.
	 * @param obsevacionesGenerales, observaciones del pedido.
	 * @param usuario, usuario que realza el pedido.
	 * @return int, 1 efectivo de lo contrario 0.
	 */
	public static int insertarAdmin(Connection con, String fecha, 
											        String hora, 
											        int centroCostoSolicitante,
											        int centroCostoSolicitado,
											        boolean urgente,
											        String obsevacionesGenerales,
											        String usuario,
											        boolean terminarPedido,
											        String insertarPedidoStr,
											        String fechaPedido,
											        String horaPedido,
											        String entidadSubcontratada, 
											        String esQuirurgico,
											        String autoPorSub)
	{
	  int resp=0;
	  
	  try
	  {
	      if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
	      
	      PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarPedidoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	      
	      if(terminarPedido)
	          ps.setInt(1,ConstantesBD.codigoEstadoPedidoTerminado);
	      else
	          ps.setInt(1,ConstantesBD.codigoEstadoPedidoSolicitado);
	      
	      ps.setString(2,fecha);
	      ps.setString(3,hora);
	      ps.setString(4,fechaPedido);
	      ps.setString(5,horaPedido);
	      ps.setInt(6,centroCostoSolicitante);
	      ps.setInt(7,centroCostoSolicitado);
	      ps.setBoolean(8,urgente);
	      ps.setString(9,obsevacionesGenerales);
	      ps.setString(10,usuario);
	      ps.setString(11,esQuirurgico);
	     
	      if(entidadSubcontratada!=null){
	    	  if(entidadSubcontratada.equals("")){
	    		  ps.setNull(12, Types.VARCHAR);
	    	  }else{ ps.setString(12, entidadSubcontratada); }
	      }else{ ps.setNull(12, Types.VARCHAR); }
	    
	      if(!UtilidadTexto.isEmpty(autoPorSub.charAt(0)+"")){
	    	  ps.setString(13,autoPorSub.charAt(0)+"");
	      }
	      
	      
	          
	      resp=ps.executeUpdate();
	      
	  }
	  catch(SQLException e)
	  {
	      logger.warn(e+" Error en la inserción de datos: SqlBasePedidosInsumosDao "+e.toString() );
	      resp=0;
	  }
	  
	  return resp;  
	}
	
	

	/**
	 * Método implementado para modificar el encabezado de un Pedido
	 * @param con
	 * @param codigoPedido
	 * @param centroCostoSolicitante
	 * @param centroCostoSolicitado
	 * @param urgente
	 * @param obsevacionesGenerales
	 * @param estado
	 * @param motivoAnulacion
	 * @param usuario
	 * @return
	 */
	public static int modificarPedido(Connection con, int codigoPedido, int centroCostoSolicitante, int centroCostoSolicitado, boolean urgente, String obsevacionesGenerales, int estado, String motivoAnulacion, String usuario) 
	{
		int resp = 0;
		PreparedStatementDecorator ps = null;
	
		
		try
		{
		
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
	
	
			// si es el estado del pedido es diferente de 'ANULADO' modificamos sus valores
			if( estado != ConstantesBD.codigoEstadoPedidoAnulado )
			{
				ps =  new PreparedStatementDecorator(con.prepareStatement(modificarPedidoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				ps.setInt(1, estado);
				ps.setInt(2,centroCostoSolicitante);
				ps.setInt(3,centroCostoSolicitado);
				ps.setBoolean(4,urgente);		
				ps.setString(5,obsevacionesGenerales);
				ps.setInt(6, codigoPedido);
		
				resp = ps.executeUpdate();
			}
			else
			{
				// cambiamos el estado del pedido a 'ANULADO'
				ps =  new PreparedStatementDecorator(con.prepareStatement("UPDATE pedido SET estado = " + estado + " WHERE codigo = " +codigoPedido,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				resp = ps.executeUpdate();
				
				if(resp == 0)
					return 0;
				
				// insertamos su correspondiente registro de anulacion
				ps =  new PreparedStatementDecorator(con.prepareStatement(insertarAnulacionPedidoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoPedido);
				ps.setString(2, motivoAnulacion);
				ps.setString(3, usuario);
			
				resp = ps.executeUpdate();
			}
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la modificacion del pedido: SqlBasePedidosInsumosDao "+e.toString() );
			resp=0;
		}
	
		return resp;  
	}

	
	/**
	 * Metodo para insertar el detalle del pedido.
	 * @param con, Connection con la fuente de datos.
	 * @param codigoPedido, codigo del pedido.
	 * @param codigoArticulo, codigo del articulo.
	 * @param cantidad, cantidad del pedido de un articulo.
	 * @return int 1 efectivo, 0 de lo contrario.
	 */
	public static int insertarDetallePedido(Connection con, int codigoPedido, int codigoArticulo, int cantidad)
	{
	    int resp=0;
	    
	    try
	    {
	        if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
	        
	        PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarDetallePedidoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,codigoPedido);
	        ps.setInt(2,codigoArticulo);
	        ps.setInt(3,cantidad);
	       
	        resp=ps.executeUpdate();
	        
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+" Error en la inserción de datos: SqlBasePedidosInsumosDao "+e.toString() );
		    resp=0;
	    }
	    
	    return resp;
	}

	

	public static int modificarDetallePedido(Connection con, int codigoPedido, int codigoArticulo, int cantidad)
	{
	    int resp=0;
	    
	    try
	    {
	        if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
	        
	        PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(modificarDetallePedidoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,cantidad);
	        ps.setInt(2,codigoPedido);
	        ps.setInt(3,codigoArticulo);
	       
	        resp = ps.executeUpdate();
	        
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+" Error en la modoficacion de detalle del pedido: SqlBasePedidosInsumosDao "+e.toString() );
		      resp=0;
	    }
	    
	    return resp;
	}
	
	
	public static int eliminarDetallePedido(Connection con, int codigoPedido, int codigoArticulo)
	{
	    int resp=0;
	    
	    try
	    {
	        if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
	        
	        PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement("DELETE FROM detalle_pedidos WHERE pedido = ? AND articulo = ? ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,codigoPedido);
	        ps.setInt(2,codigoArticulo);
	       
	        resp = ps.executeUpdate();
	        
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+" Error en la eliminacion de detalle del pedido: SqlBasePedidosInsumosDao "+e.toString() );
		      resp=0;
	    }
	    
	    return resp;
	}
	
	
	/**
	 * Metodo para realizar la consulta de un pedido realizado.
	 * @param con, Connection con la fuente de datos.
	 * @param codigoPedido, Codigo del pedido a consultar.
	 * @return ResultSet.
	 */
	public static ResultSetDecorator listarPedidoInsumo(Connection con, int codigoPedido)
	{
	    try
	    {
	        if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}  
	        
	        PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultarUnPedidoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,codigoPedido);
	        return new ResultSetDecorator(ps.executeQuery());
	        
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en la consulta de Pedido, con el codigo->"+codigoPedido+" "+": SqlBasePedidosInsumosDao "+e.toString() );
		   return null;
	    }
	}
	
	
	
	public static String getNombreCentroCosto(Connection con, int codCentroCosto){
		ResultSetDecorator rs = null;
				
    try{
        if (con == null || con.isClosed()){
        	DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
        	con = myFactory.getConnection();
        }  
        
        PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement("SELECT nombre FROM centros_costo WHERE codigo = " +codCentroCosto,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        rs =new ResultSetDecorator(ps.executeQuery());
        if(rs != null){
        	rs.next();
        	return rs.getString("nombre");
        }
    }
    catch(SQLException e){
        logger.warn(e+"Error en la consulta del Nombre del Centro de Costo : SqlBasePedidosInsumosDao:   "+e.toString() );
        return "";
    }

		return "";
	}
	
	
	/**
	 * metodo de busqueda generica
	 * @param con una conexion con la fuente de datos
	 * @param columns  array de strings con las columnas deseadas
	 * @param from   clausula from de la consulta
	 * @param where  clausula where de la consulta
	 * @param orderBy  campo por el cual se ordenara el resultado de la consulta 
	 * @return un resultset con los resultados de la consulta
	 */
	public static ResultSetDecorator buscar(Connection con, String[] selectedColumns, String from, String where, String orderBy, String orderDirection)
	{
		
		logger.info("\n entro a buscar ");
		PreparedStatementDecorator busqueda;
		ResultSetDecorator rs = null;
		String selectQuery = "";
		int k;
		
	  try
		{
			con = refreshDBConnection(con);
			selectQuery = "SELECT ";
			for(k=0; k<(selectedColumns.length-1); k++){
				selectQuery += selectedColumns[k] + ", ";
			}
			selectQuery  += selectedColumns[k];
			selectQuery = selectQuery +" FROM  "+from +(( ( where.equals("") )? " " : " WHERE " +where)  +(( ( orderBy.equals("") )? " " : " ORDER BY ( " +orderBy + ") ")) +orderDirection);

			
			logger.info("\n consulta-->"+selectQuery);
			busqueda =  new PreparedStatementDecorator(con.prepareStatement(selectQuery,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs = new ResultSetDecorator(busqueda.executeQuery());
		}
		catch (Exception e)
		{
			logger.error("Error realizando la búsqueda de Moviemiento de Pedidos [ Tabla(s): " +from +" ]:"  +"\n query: " +selectQuery + "\n excepcion: " +e);
		}
    
		return rs;
	}

	

	public static String[] puedoMostrarFuncionalidad(Connection con, int codigoFuncionalidad, String loginUsuario, boolean onlyPath){
		String query = 
		"SELECT "+
		" DISTINCT(nombre_func) AS nombreFuncionalidad, " +
		" archivo_func AS path " +
		" FROM "+
		" roles_usuarios ru, "+
		" usuarios u, "+
		" funcionalidades f, "+
		" roles_funcionalidades rf "+
		" WHERE "+
		" ru.login = u.login AND "+
		" rf.nombre_rol = ru.nombre_rol AND " +
		" f.codigo_func = rf.codigo_func AND " +
		" f.codigo_func IN(?) AND " +
		" u.login= ? ";
	
		PreparedStatementDecorator busqueda;
		ResultSetDecorator rs = null;
		String[] funcionality = new String[2];
		int qIndex = -1;
		
		funcionality[0] = "";
		funcionality[1] = "";
		
		try{
  		con = refreshDBConnection(con);
			busqueda =  new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			busqueda.setInt(1, codigoFuncionalidad);
			busqueda.setString(2, loginUsuario);
			
			rs = new ResultSetDecorator(busqueda.executeQuery());

			if( rs != null){
				if( rs.next() ){
					funcionality[0] = rs.getString("nombreFuncionalidad");
					funcionality[1] = rs.getString("path");
					
					// si solo me interesa la ruta mas no los posibles parametros que pueda tener la pagina
					if( onlyPath == true){
						qIndex = funcionality[1].indexOf('?'); 
						if( qIndex != -1 ){
							funcionality[1] = funcionality[1].substring(0, qIndex);
						}
					}
				}
			}
		}
		catch (Exception e){
			logger.error("Error realizando la búsqueda de Funcionalidad Permitida en SqlBasePedidosInsumos.puedoMostrarFuncionalidad()  \n query: " +query + "\n excepcion: " +e);
		}
    
		return funcionality;
	}
	
	/**
	 * Método implementado para consultar los datos generales de un pedido
	 * de insumos
	 * @param con
	 * @param pedido
	 * @return
	 */
	public static HashMap cargarDatosGeneralesPedido(Connection con,int pedido)
	{
		//columnas
		String[] columnas={
				"codigo_estado",
				"nombre_estado",
				"fecha_grabacion",
				"hora_grabacion",
				"fecha",
				"hora",
				"codigo_centro_costo",
				"nombre_centro_costo",
				"codigo_farmacia",
				"nombre_farmacia",
				"urgente",
				"observaciones",
				"usuario"
				};
		try
		{

			String consulta = cargarDatosGeneralesPedidoStr;
			consulta += " WHERE codigo = ?";
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,pedido);
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarDatosGeneralesPedido de SqlBasePedidosInsumosDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método usado para consultar el detalle de artículos del 
	 * pedido de insumos
	 * @param con
	 * @param pedido
	 * @return
	 */
	public static HashMap cargarDetallePedido(Connection con,int pedido)
	{
		//columnas
		String[] columnas={
				"articulo",
				"concentracion",
				"formaFarmaceutica",
				"unidadMedida",
				"naturaleza",
				"cantidadDespacho",
				"existenciaXAlmacen"
				};
		try
		{
			String consulta = cargarDetallePedidoStr;
			consulta += " d.pedido = "+pedido;
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(st.executeQuery(consulta)),false,true);
			
			return listado.getMapa();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarDetallePedido de SqlBasePedidosInsumosDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método implementado para cargar los datos de anulación de un pedido
	 * @param con
	 * @param codigoPedido
	 * @return
	 */
	public static HashMap cargarDatosAnulacion(Connection con,int codigoPedido)
	{
		//columnas
		String[] columnas={
				"motivo",
				"fecha",
				"hora",
				"usuario"
				};
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarDatosAnulacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoPedido);
			
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarDatosAnulacion de SqlBasePedidosInsumosDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método que consulta los datos adicionales de la peticion de un pedido
	 * @param con
	 * @param codigoPedido
	 * @return
	 */
	public static HashMap cargarDatosPeticionPedido(Connection con,String codigoPedido)
	{
		logger.info("\n entre a cargarDatosPeticionPedido codigo pedido"+codigoPedido);
		HashMap resultados = new HashMap();
		resultados.put("numRegistros","0");
		try
		{
			logger.info("\n cadena -->"+cargarDatosPeticionPedidoStr);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarDatosPeticionPedidoStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Integer.parseInt(codigoPedido));
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), false, true);
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarDatosPeticionPedido: "+e);
		}
		return resultados;
	}
	


}
