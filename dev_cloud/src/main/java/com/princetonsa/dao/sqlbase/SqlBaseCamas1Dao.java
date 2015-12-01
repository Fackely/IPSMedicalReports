/*
 * @(#)SqlBaseCamas1Dao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.UtilidadCadena;
import util.Utilidades;
import util.ValoresPorDefecto;
import com.princetonsa.dao.DaoFactory;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para el manejo de camas
 *
 * @version 1.0, Mayo 23 / 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class SqlBaseCamas1Dao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseCamas1Dao.class);

	/**
	 * Listado de las camas creadas con antelacion, filtradas por el codigo de la institucion
	 */
	private final static String listadoCamas1StrPos =	"SELECT "+ 
		"c.codigo AS codigo, "+ 
		"c.habitacion AS codigo_habitacion, "+ 
		"coalesce(getnomhabitacioncama(c.codigo),'') AS habitacion, "+
		"c.numero_cama AS cama, "+ 
		"c.descripcion, "+ 
		"c.estado AS codigoEstado, "+
		"ec.nombre AS nombreEstado, "+ 
		"c.centro_costo AS codigoCentroCosto, "+ 
		"cc.nombre AS nombreCentroCosto, "+ 
		"c.tipo_usuario_cama AS codigoTipoUsuarioCama, "+ 
		"tuc.nombre AS nombreTipoUsuarioCama, "+
		"CASE WHEN c.es_uci ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'SI' ELSE 'NO' END AS esUciStr, "+
		"coalesce(getnombrepisocama(c.codigo),'') AS piso, "+
		"coalesce(getnomtipohabitacioncama(c.codigo),'') as tipo_habitacion "+ 
		"FROM  camas1 c "+ 
		"INNER JOIN estados_cama ec ON(c.estado=ec.codigo) "+ 
		"INNER JOIN centros_costo cc ON(cc.codigo=c.centro_costo) "+ 
		"INNER JOIN tipos_usuario_cama tuc ON(tuc.codigo=c.tipo_usuario_cama) "+ 
		"WHERE "+ 
		"c.institucion=? and cc.centro_atencion = ? "+ 
		"ORDER BY piso,habitacion, cama ";
	private final static String listadoCamas1StrOra ="SELECT "+ 
	"c.codigo AS codigo, "+ 
	"c.habitacion AS codigo_habitacion, "+ 
	"coalesce(getnomhabitacioncama(c.codigo),'') AS habitacion, "+
	"c.numero_cama AS cama, "+ 
	"c.descripcion, "+ 
	"c.estado AS codigoEstado, "+
	"ec.nombre AS nombreEstado, "+ 
	"c.centro_costo AS codigoCentroCosto, "+ 
	"cc.nombre AS nombreCentroCosto, "+ 
	"c.tipo_usuario_cama AS codigoTipoUsuarioCama, "+ 
	"tuc.nombre AS nombreTipoUsuarioCama, "+
	"CASE WHEN c.es_uci = 1 THEN 'SI' ELSE 'NO' END AS esUciStr, "+
	"coalesce(getnombrepisocama(c.codigo),'') AS piso, "+
	"coalesce(getnomtipohabitacioncama(c.codigo),'') as tipo_habitacion "+ 
	"FROM  manejopaciente.camas1 c "+ 
	"INNER JOIN manejopaciente.estados_cama ec ON(c.estado=ec.codigo) "+ 
	"INNER JOIN administracion.centros_costo cc ON(cc.codigo=c.centro_costo) "+ 
	"INNER JOIN manejopaciente.tipos_usuario_cama tuc ON(tuc.codigo=c.tipo_usuario_cama) "+ 
	"WHERE "+ 
	"c.institucion=? and cc.centro_atencion = ? "+ 
	"ORDER BY piso,habitacion, cama ";
																	
	/**
	 * Detalle de una cama, filtrado por habitacion, cama, institucion 
	 */
	private final static String detalleCama1Str= 	"SELECT " +
																		"c.codigo AS codigo, " +
																		"coalesce(c.habitacion,0) AS habitacion, " +
																		"coalesce(hab.nombre,'') AS nombreHabitacion, " +
																		"c.numero_cama AS cama, " +
																		"c.descripcion AS descripcionCama, " +
																		"c.estado AS codigoEstado, " +
																		"ec.nombre AS nombreEstado, " +
																		"CASE WHEN (c.estado="+ConstantesBD.codigoEstadoCamaFueraServicio+" OR c.estado="+ConstantesBD.codigoEstadoCamaMantenimiento+") " +
																		"THEN "+ValoresPorDefecto.getValorTrueParaConsultas()+" ELSE "+ValoresPorDefecto.getValorFalseParaConsultas()+" END AS esModificableEstado, " +
																		"c.tipo_usuario_cama AS codigoTipoUsurioCama, " +
																		"'Nom:'|| tuc.nombre || " +
																		"'    Sexo:' || coalesce(getdescripcionsexo(sexo),'Sin Res.') || " +
																		"'    Ind.SR:' || tuc.ind_sexo_restrictivo || " +
																		"'    Edad Ini:' || tuc.edad_inicial || " +
																		"'    Edad Fin:' || tuc.edad_final  AS nombreTipoUsuarioCama, " +
																		"c.centro_costo AS codigoCentroCosto, " +
																		"cc.nombre AS nombreCentroCosto, " +
																		"c.es_uci AS esUciBool," +
																		"cc.centro_atencion AS centro_atencion," +
																		"coalesce(hab.piso,0) AS piso," +
																		"getnomtipohabitacioncama(c.codigo) AS tipoHabitacion, " +
																		"getnombrepisocama(c.codigo) As nombrePiso, " +
																		"c.asignable_admision As asignable_admision " +
																		//"CASE WHEN c.es_uci ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'SI' ELSE 'NO' END AS esUci " +
																		"FROM  " +
																		"camas1 c " +
																		"INNER JOIN estados_cama ec ON (c.estado=ec.codigo) " +
																		"INNER JOIN  tipos_usuario_cama tuc ON (c.tipo_usuario_cama=tuc.codigo) " +
																		"INNER JOIN centros_costo cc ON (c.centro_costo=cc.codigo) " +
																		"LEFT OUTER JOIN habitaciones hab ON (hab.codigo=c.habitacion) " +
																		"WHERE " +
																		"c.codigo=? " +
																		"ORDER BY c.habitacion, cama "; 
	
	/**
	 * Modifica los atributos de una cama dado su numero de cama y su habitacion
	 */
	private final static String modificarCama1Str= 	"UPDATE camas1 SET " +
			"habitacion = ?, "+
			"descripcion=?, " +
			"estado=?, " +
			"tipo_usuario_cama=?, " +
			"centro_costo=?, " +
			"es_uci=?," +
			"asignable_admision=? " +
			"WHERE " +
			"codigo=? ";
	
	/**
	 * Contiene toda la informacion de la cama ocupada para el CENSO DE CAMAS para la primera parte del UNION	
	 */
	private static final String infoAdicionalCamaOcupadaStrParte1 = "(SELECT pe.primer_nombre as primerNombre, "
																									+ "pe.segundo_nombre as segundoNombre, "
																									+ "pe.primer_apellido as primerApellido, "
																									+ "pe.segundo_apellido as segundoApellido, "
																									+ "pe.numero_identificacion as numId, "
																									+ "pe.tipo_identificacion as tipoId, "
																									+ "s.nombre as sexo, "
																									+ "ah.fecha_admision as fechaIngreso, "
																									+	"ah.hora_admision as horaIngreso, "
																									+ "ca.centro_costo as centroCostoCama, "
																									+ "ca.codigo as codCama, "
																									+ "ca.numero_cama as numCama, " 
																									+ "ca.habitacion as habitacionCama, "
																									+ "pe.fecha_nacimiento as fechaNacimiento, "
																									+ "co.nombre as nombreConvenio, "
																									+ "co.codigo as codConvenio, "
																									+ "cu.id as codCuenta "
																									+ "FROM admisiones_hospi ah " +
																									" INNER JOIN cuentas cu ON(cu.id = ah.cuenta) " +
																									" INNER JOIN personas pe (pe.codigo = cu.codigo_paciente) " +
																									" INNER JOIN sexo s ON(s.codigo = pe.sexo) " +
																									" INNER JOIN camas1 ca ON(ca.codigo = ah.cama ) " +
																									" INNER JOIN sub_cuentas sc ON(sc.ingreso = cu.id_ingreso AND sc.nro_prioridad =1)" +
																									" INNER JOIN convenios co ON(co.codigo=sc.convenio) "
																									+ "WHERE  "
																									+ " ca.estado =  " + ConstantesBD.codigoEstadoCamaOcupada
																									+ "AND ah.estado_admision =  " + ConstantesBD.codigoEstadoAdmisionHospitalizado
																									+ " AND ca.institucion= ? ";
	/**
	 * Contiene toda la informacion de la cama ocupada para el CENSO DE CAMAS para la segunda parte del UNION	
	 */
	private static final String infoAdicionalCamaOcupadaStrParte2 = "SELECT pe.primer_nombre as primerNombre, "
																									+ "pe.segundo_nombre as segundoNombre, "
																									+ "pe.primer_apellido as primerApellido, "
																									+ "pe.segundo_apellido as segundoApellido, "
																									+ "pe.numero_identificacion as numId, "
																									+ "pe.tipo_identificacion as tipoId, "
																									+ "s.nombre as sexo, "
																									+ "au.fecha_ingreso_observacion as fechaIngreso, "
																									+ "au.hora_ingreso_observacion as horaIngreso, "
																									+ "ca.centro_costo as centroCostoCama, "
																									+ "ca.codigo as codCama, "
																									+ "ca.numero_cama as numCama, "
																									+ "ca.habitacion as habitacionCama, "
																									+ "pe.fecha_nacimiento as fechaNacimiento, "
																									+ "co.nombre as nombreConvenio, "
																									+ "co.codigo as codConvenio, " 
																									+ "cu.id as codCuenta "																		
																									+ "FROM admisiones_urgencias au" +
																									" INNER JOIN cuentas cu ON(cu.id=au.cuenta) " +
																									" INNER JOIN personas pe ON(pe.codigo=cu.codigo_paciente) " +
																									" INNER JOIN sexo s ON(s.codigo=pe.sexo) " +
																									" INNER JOIN camas1 ca ON(ca.codigo = au.cama_observacion) " +
																									" INNER JOIN sub_cuentas sc ON(sc.ingreso = cu.id_ingreso AND sc.nro_prioridad =1) " +
																									" INNER JOIN convenios co on(co.codigo=sc.convenio) "
																									+ "WHERE  "
																									+ " ca.estado =  " + ConstantesBD.codigoEstadoCamaOcupada	
																									+ " AND au.fecha_egreso_observacion IS null "	
																									+ " AND ca.institucion= ? ";
																									
	
	/**
	 * obtiene el ultimo codigo insertado en la tablas camas1
	 */
	private final static String cargarCodigoUltimaInsercionStr = "SELECT MAX(codigo)  AS codigo from camas1";
	
	/**
	 * Listado de camas para el censo de camas
	 */
	private static final String listarCensoCamasStr = "SELECT c.codigo as codCama, " 
		+"c.numero_cama as numCama, c.descripcion as descripcionCama, " 
		+"c.estado as estadoCama, " 
		+"ec.nombre as nombreEstadoCama, " 
		+"c.centro_costo as codCentroCostoCama, " 
		+"cc.nombre as nombreCentroCostoCama "
		+ "FROM camas1 c, centros_costo cc, estados_cama ec "
		+ "WHERE c.centro_costo = cc.codigo "
		+ "AND ec.codigo = c.estado "
		+ "c.institucion=? ";
	
	/**
	 * Cadena que verifica si ya existe una cama con la misma
	 * habitacion, numero de cama e institucion en el mismo centro de atencion
	 */
	private static final String existeCamaEnCentroAtencionStr = "SELECT " +
		"count(1) AS cuenta " +
		"FROM camas1 c " +
		"INNER JOIN centros_costo cc ON(cc.codigo=c.centro_costo) " +
		"WHERE " +
		"c.habitacion = ? and c.numero_cama = ? and c.institucion = ? and " +
		"cc.centro_atencion IN (SELECT centro_atencion FROM centros_costo WHERE codigo = ?) ";
																			
	
	
	/**
	 * Método que contiene el Resulset del Listado de las camas 
	 * creadas con antelacion, filtradas por el codigo de la institucion
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param int, codigo de la institucion
	 * @param centroAtencion
	 * @param Tipo_BD 
	 * @return Resultset con las 
	 * @throws SQLException
	 */
	public static  ResultSetDecorator listadoCamas1(Connection con, int codigoInstitucion,int centroAtencion, int Tipo_BD) throws SQLException
	{
		logger.info("\n entre a  listadoCamas1");
		ResultSetDecorator respuesta=null;
		if(con==null || con.isClosed())
		{
			try
			{
				DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo realizar la conexión "+e.toString());
				respuesta= null;
			}
		}
		try
		{
			String listadoCamas1Str="";
			switch(Tipo_BD)
			{
			case DaoFactory.ORACLE:
				listadoCamas1Str=listadoCamas1StrOra;
				break;
			case DaoFactory.POSTGRESQL:
				listadoCamas1Str=listadoCamas1StrPos;
				break;
				default:
					break;
			
			}
			logger.info("listadoCamas1Str -->"+listadoCamas1Str+"---"+centroAtencion);
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(listadoCamas1Str,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoInstitucion);
			ps.setInt(2,centroAtencion);
			respuesta=new ResultSetDecorator(ps.executeQuery());				
		}
		catch(SQLException e)
		{
			logger.warn("Error en el listado camas  " +e.toString());
			respuesta=null;
		}
		return respuesta;
	}
	
	/**
	 * Detalle de la cama filtrado por el codigo de la cama
	 * @param con
	 * @param codigo
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator detalleCama1(Connection con, int codigo) 
	{
	    try
	    {
			PreparedStatementDecorator detalleStatement= new PreparedStatementDecorator(con.prepareStatement(detalleCama1Str,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			detalleStatement.setInt(1, codigo);
			return new ResultSetDecorator(detalleStatement.executeQuery());
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+" Error en cargar detalleCama: SqlBaseCamas1Dao "+e.toString());
	        return null;
	    }
	}
	
	/**
	 * Metodo que modifica una cama dado su codigo
	 * @param con
	 * @param habitacion
	 * @param descripcion
	 * @param estado
	 * @param tipoUsuarioCama
	 * @param centroCosto
	 * @param esUci
	 * @param codigoCama
	 * @return true si fue modificado
	 */
	public static boolean modificarCama1(	Connection con,
			String habitacion,
			String descripcion, 
			int estado,
			int tipoUsuarioCama,
			int centroCosto, 
			boolean esUci, 
			int codigo,
			String asignableAdmision) 
	{
		int resp=0;	
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarCama1Str,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1,habitacion);
			ps.setString(2,descripcion);
			ps.setInt(3, estado);
			ps.setInt(4,tipoUsuarioCama);
			ps.setInt(5,centroCosto);
			ps.setBoolean(6,esUci);
			ps.setString(7,asignableAdmision);
			ps.setInt(8,codigo);
			resp=ps.executeUpdate();
			
			if(resp>0)
			{
			    return true;
			}
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la modificacion de datos: SqlBaseCamas1Dao "+e.toString());
			resp=0;			
		}	
		return false;	
	}

	/**
	 * Inserta una cama 
	 * @param con
	 * @param habitacion
	 * @param numeroCama
	 * @param descripcion
	 * @param estado
	 * @param esUci
	 * @param codigoInstitucion
	 * @param tipoUsuarioCama
	 * @param insertarCama1Str
	 * @return true - false
	 */
	public static boolean  insertarCama1(	Connection con,
			                                                int codigo,   
															int habitacion,
															String numeroCama,
															String descripcion,
															int estado,
															boolean esUci,
															int codigoInstitucion,
															int tipoUsuarioCama,
															int codigoCentroCosto,
															String ingresarCama1Str)
	{
		int resp=0;
		try
		{
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(ingresarCama1Str,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    ps.setInt(1,codigo);
			ps.setInt(2,habitacion);
			ps.setString(3, numeroCama);
			ps.setString(4,descripcion);
			ps.setInt(5,estado);
			ps.setBoolean(6,esUci);
			ps.setInt(7,codigoInstitucion);
			ps.setInt(8, tipoUsuarioCama);
			ps.setInt(9, codigoCentroCosto);
			resp=ps.executeUpdate();
			if(resp>0)
			    return true;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseCamas1Dao "+e.toString() );
			resp=0;
		}
		return false;
	}
	
	/**
	 * Busqueda Avanzada de las camas
	 * @param con
	 * @param habitacion
	 * @param piso
	 * @param tipoHabitacion
	 * @param numeroCama
	 * @param descripcionCama
	 * @param codigoEstado
	 * @param codigoTipoUsuario
	 * @param esUciAux
	 * @param codigoServicio
	 * @param nombreServicio
	 * @param codigoInstitucion
	 * @param codigoCentroCosto
	 * @param codigoCentroAtencion
	 * @return
	 * @throws SQLException
	 */
	public static  ResultSetDecorator busquedaAvanzadaCama1(	Connection con,
			int habitacion,
			String piso,
			String tipoHabitacion,
			String numeroCama,
			String descripcionCama,
			int codigoEstado,
			int codigoTipoUsuario,
			int esUciAux,
			int codigoServicio,
			String nombreServicio,
			int codigoInstitucion,
			int codigoCentroCosto,
			int codigoCentroAtencion,
			String asignableAdmision) throws SQLException
	{
		ResultSetDecorator respuesta=null;
		String consultaArmada="";
		if(con==null || con.isClosed())
		{
			try
			{
				DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo realizar la conexión "+e.toString());
				respuesta= null;
			}
		}
		try
		{
			consultaArmada=armarConsulta(	habitacion,piso, tipoHabitacion, numeroCama, descripcionCama, codigoEstado, codigoTipoUsuario, esUciAux, codigoServicio, nombreServicio, codigoInstitucion, codigoCentroCosto,codigoCentroAtencion,asignableAdmision);
			logger.info("\n consulta -->"+consultaArmada);
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultaArmada,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			respuesta=new ResultSetDecorator(ps.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn("Error en la búsqueda avanzada de camas1 " +e.toString());
			respuesta=null;
		}
		return respuesta;
	}
	
	/**
	 * metodo que arma la consulta por los criterios de busqueda que entregue el usuario
	 * @param habitacion
	 * @param numeroCama
	 * @param descripcionCama
	 * @param tipoHabitacion 
	 * @param numeroCama2 
	 * @param codigoEstado
	 * @param codigoTipoUsuario
	 * @param esUciAux
	 * @param codigoServicio
	 * @param nombreServicio
	 * @param codigoInstitucion
	 * @param codigoCentroAtencion 
	 * @return
	 */
	private static String armarConsulta  (		int habitacion,
												String piso, 
												String tipoHabitacion,
																String numeroCama,
																String descripcionCama,
																 
																int codigoEstado,
																int codigoTipoUsuario,
																int esUciAux,
																int codigoServicio,
																String nombreServicio,
																int codigoInstitucion,
																int codigoCentroCosto, 
																int codigoCentroAtencion,
																String asignableAdmision)
	{
		logger.info("\n armarConsulta asignableAdmision -->"+asignableAdmision);
		String consulta= "SELECT " +
								"c.codigo AS codigo, " +
								"c.habitacion AS codigo_habitacion, "+ 
								"coalesce(getnomhabitacioncama(c.codigo),'') AS habitacion, "+
								"c.numero_cama AS cama, " +
								"c.descripcion, " +
								"c.estado AS codigoEstado, " +
								"ec.nombre AS nombreEstado, " +
								"c.centro_costo AS centroCosto, " +
								"cc.nombre AS nombreCentroCosto, " +
								"c.tipo_usuario_cama AS codigoTipoUsuarioCama, " +
								"tuc.nombre AS nombreTipoUsuarioCama, " +
								"CASE WHEN c.es_uci = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'SI' ELSE 'NO' END AS esUciStr, " +
								"coalesce(getnombrepisocama(c.codigo),'') AS piso, "+
								"coalesce(getnomtipohabitacioncama(c.codigo),'') as tipo_habitacion "+ 
								"FROM  " +
								"camas1 c " +
								"INNER JOIN estados_cama ec ON(c.estado=ec.codigo) "+ 
								"INNER JOIN centros_costo cc ON(cc.codigo=c.centro_costo) "+ 
								"INNER JOIN tipos_usuario_cama tuc ON(tuc.codigo=c.tipo_usuario_cama) ";
		
		if(!piso.equals("")||!tipoHabitacion.equals(""))
			consulta += " INNER JOIN habitaciones hab ON(hab.codigo=c.habitacion) ";
		
		
		consulta += "WHERE " +
		"c.institucion= "+codigoInstitucion+" ";
		
		//logger.info("\n codigoServicio -->"+codigoServicio+" nombreServicio -->"+nombreServicio);
		if(codigoServicio>0 && nombreServicio.equals(""))
		{
		    consulta= consulta+"AND c.codigo IN (" +
		    															"SELECT sc.codigo_cama " +
		    															"FROM " +
		    															"servicios_cama sc, " +
		    															"referencias_servicio rs " +
		    															"WHERE " +
		    															"rs.servicio= sc.servicio " +
		    															"AND rs.tipo_tarifario =0 " +
		    															"AND sc.servicio="+codigoServicio+
		    														") ";
		}
		else if (!nombreServicio.equals(""))
		{
		    consulta= consulta+"AND c.codigo IN (" +
																		"SELECT sc.codigo_cama " +
																		"FROM " +
																		"servicios_cama sc, " +
																		"referencias_servicio rs " +
																		"WHERE " +
																		"rs.servicio= sc.servicio " +
																		"AND rs.tipo_tarifario =0 " +
																		"AND UPPER(rs.descripcion) LIKE UPPER('%"+nombreServicio+"%')"+
																	") ";
		}
		
		if(habitacion > 0)
		{
			consulta = consulta + "AND c.habitacion="+habitacion+" ";
		}
		
		if(!numeroCama.trim().equals(""))
		{
			consulta = consulta + " AND c.numero_cama= '"+numeroCama+"' ";
		}
		
		if(descripcionCama != null && !descripcionCama.equals(""))
		{
			consulta = consulta + " AND UPPER(c.descripcion) LIKE  UPPER('%"+descripcionCama+"%') ";
		}
		//este puede ser cero=disponible
		if(codigoEstado>=0)
		{
			consulta = consulta + " AND c.estado="+codigoEstado+" ";
		}
		//este puede ser cero=todos
		if(codigoTipoUsuario >=0)
		{
			consulta = consulta + " AND c.tipo_usuario_cama="+codigoTipoUsuario+" ";
		}
		if(esUciAux==1)
		{
			consulta = consulta + "AND c.es_uci= " + ValoresPorDefecto.getValorTrueParaConsultas() + " "	;	
		}
		
		if(esUciAux==2)
		{
			consulta = consulta + "AND c.es_uci=  "+ValoresPorDefecto.getValorTrueParaConsultas()+ " "	;	
		}
		
		if(esUciAux==0)
		{
			consulta = consulta + "AND c.es_uci=  "+ValoresPorDefecto.getValorFalseParaConsultas()+ " "	;	
		}
		
		if(codigoCentroCosto>0)
		{
		    consulta = consulta+ "AND c.centro_costo="+codigoCentroCosto+" ";
		}
		
		if(codigoCentroAtencion>0&&codigoCentroCosto<=0)
		{
			consulta += " AND cc.centro_atencion = "+codigoCentroAtencion+" ";
		}
		
		if (UtilidadCadena.noEsVacio(asignableAdmision))
			consulta += " AND c.asignable_admision = '"+asignableAdmision+"' ";
		
		if(!piso.equals(""))
			consulta += " AND hab.piso = "+piso+" ";
		
		if(!tipoHabitacion.equals(""))
			consulta += " AND hab.tipo_habitacion = '"+tipoHabitacion+"' AND hab.institucion = "+codigoInstitucion+" ";
		
		consulta+="ORDER BY habitacion, cama";
		
		return consulta;
	}
	
	/**
	 * Carga el última cama insertada
	 * @param con
	 * @return
	 */
	public static ResultSetDecorator cargarUltimaCodigoCamaInsertado(Connection con)
	{
		try
		{
			PreparedStatementDecorator cargarUltimoStatement= new PreparedStatementDecorator(con.prepareStatement(cargarCodigoUltimaInsercionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return new ResultSetDecorator(cargarUltimoStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del último código de cama: SqlBaseCamas1Dao "+e.toString());
			return null;
		}
	}
	
	/**
	 * Carga la informacion de las camas ocupadas para el CENSO DE CAMAS
	 * @param con
	 * @param infoAdicionalCamaOcupadaStr
	 * @param codigoInstitucion
	 * @param restringirPorCentroCosto
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator infoAdicionalCamasOcupadas(Connection con, int codigoInstitucion, int centroCosto) throws SQLException
	{
	    String consultaTempPart1="";
	    String consultaTempPart2="";
	    if(centroCosto>0)
	    {
	        consultaTempPart1= infoAdicionalCamaOcupadaStrParte1+" AND ca.centro_costo = ? ";
	        consultaTempPart2= infoAdicionalCamaOcupadaStrParte2+" AND ca.centro_costo = ? ";
	    }
		PreparedStatementDecorator infoAdicCamasStmnt =  new PreparedStatementDecorator(con.prepareStatement(consultaTempPart1+" UNION "+consultaTempPart2+ ") ORDER BY centroCostoCama ASC, habitacionCama ASC, numCama ASC",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		infoAdicCamasStmnt.setInt(1, codigoInstitucion);
		infoAdicCamasStmnt.setInt(2, codigoInstitucion);
		if(centroCosto>0)
	    {
		    infoAdicCamasStmnt.setInt(3, centroCosto);
		    infoAdicCamasStmnt.setInt(4, centroCosto);
	    }
		return new ResultSetDecorator(infoAdicCamasStmnt.executeQuery());
	}
	
	/**
	 * Listado de las camas para el censo de camas, 
	 * @param con
	 * @param codigoInstitucion
	 * @param centroCosto
	 * @return
	 */
	public static ResultSetDecorator listarCensoCamas(Connection con, int codigoInstitucion, int centroCosto)
	{
		ResultSetDecorator resultado;
		String consulta=listarCensoCamasStr;
		try
		{
		    if(centroCosto>0)
		        consulta+=" AND c.centro_costo = ? " ;
			PreparedStatementDecorator listarCensoCamasStmnt =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			listarCensoCamasStmnt.setInt(1, codigoInstitucion);
			if(centroCosto>0)
			    listarCensoCamasStmnt.setInt(2, centroCosto);
			resultado=new ResultSetDecorator(listarCensoCamasStmnt.executeQuery());
			return resultado;
		}
		catch(SQLException e)
		{
			logger.error("Error listando camas1 por centro de costo: "+e);
			return null;
		}		
	}
	
	/**
	 * Método implementado para verificar si ya existe una cama
	 * con la misma habitacion, numero de cama, institucion en el mismo
	 * de centro de atención.
	 * @param con
	 * @param habitacion
	 * @param numeroCama
	 * @param institucion
	 * @param centroCosto
	 * @return
	 */
	public static boolean existeCamaEnCentroAtencion(Connection con,int habitacion,String numeroCama,int institucion,int centroCosto)
	{
		try
		{
			boolean existe = false;
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(existeCamaEnCentroAtencionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,habitacion);
			pst.setString(2,numeroCama);
			pst.setInt(3,institucion);
			pst.setInt(4,centroCosto);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					existe = true;
			
			return existe;
		}
		catch(SQLException e)
		{
			logger.error("Error en existeCamaEnCentroAtencion de SQlBaseCamas1Dao: "+e);
			return false;
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static int obtenerCamaDadaCuenta(Connection con, String codigoCuenta)
	{
		String codigoViaIngreso= Utilidades.obtenerViaIngresoCuenta(con, codigoCuenta);
		String consulta="";
		int resp=ConstantesBD.codigoNuncaValido;
		
		if(codigoViaIngreso.equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
		{
			consulta=	"SELECT c1.codigo " +
						"from traslado_cama tc " +
						"inner join camas1 c1 on(c1.codigo=tc.codigo_nueva_cama)  "+
                        "where tc.cuenta="+codigoCuenta+" and fecha_finalizacion is null ";
		}
		else if(codigoViaIngreso.equals(ConstantesBD.codigoViaIngresoUrgencias+""))
		{
			consulta=	"SELECT c1.codigo "+
						"from admisiones_urgencias au " +
						"inner join camas1 c1 on(c1.codigo=au.cama_observacion) "+
						"where cuenta="+codigoCuenta;
		}
		
		logger.info("\n CONSULTA CODIGO CAMA-->"+consulta+"\n");
		
		if(codigoViaIngreso.equals(ConstantesBD.codigoViaIngresoHospitalizacion+"") || codigoViaIngreso.equals(ConstantesBD.codigoViaIngresoUrgencias+""))
		{	
			try
			{
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
				if(rs.next())
				{
					resp= rs.getInt("codigo");
				}
			}
			catch (SQLException e) 
			{
				logger.error("Error obtener cama cuenta");
				e.printStackTrace();
			}
		}
		return resp;
	}
	
}	