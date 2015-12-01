/*
 * Created on Feb 8, 2005
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import com.princetonsa.dao.DaoFactory;
import util.ConstantesBD;
import util.UtilidadBD;


/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *
 * Clase para las transacciones de la Actualización de los Números de Autorización
 */
public class SqlBaseActualizacionAutorizacionDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseActualizacionAutorizacionDao.class);
	
	/**
	 * Statement para consultar el estado de la cuenta del paciente segun su via de ingreso
	 */
	//SELECT getnombreviaingreso(mc.via_ingreso), (admH.fecha_admision||'-'||admH.hora_admision) as fechaAdmisionHospitalaria,(admU.fecha_admision||'-'||admU.hora_admision) as fechaAdmisionUrgencias, (c.fecha_apertura||'-'||c.hora_apertura) as fechaApertura, (i.fecha_egreso||'-'||i.hora_egreso) as fechaEgreso, admH.numero_autorizacion as autorizacionHospitalizacion, admU.numero_autorizacion as autorizacionUrgencias  FROM montos_cobro mc INNER JOIN cuentas c on(c.monto_cobro=mc.codigo) LEFT OUTER JOIN admisiones_hospi admH on(c.id=admh.cuenta) LEFT OUTER JOIN admisiones_urgencias admU on(c.id=admU.cuenta)  INNER JOIN ingresos i on(c.id_ingreso=i.id) where c.id=?";
	private final static String consultarTodasDatosPacienteXCuentaStr=" SELECT " +
	                                                                 " c.via_ingreso as codigoviaingreso, "+
																	 " getnombreviaingreso(c.via_ingreso) as viaingreso, "+
																	 " CASE WHEN (to_char(admH.fecha_admision,'"+ConstantesBD.formatoFechaAp+"')||' - '||to_char(admH.hora_admision, 'HH24:MI')) IS NULL THEN "+
																	 " ( CASE WHEN (to_char(admU.fecha_admision,'"+ConstantesBD.formatoFechaAp+"')||' - '||to_char(admU.hora_admision, 'HH24:MI')) IS NULL THEN "+
																	 " (to_char(c.fecha_apertura,'"+ConstantesBD.formatoFechaAp+"')||' - '||to_char(c.hora_apertura, 'HH24:MI')) ELSE (to_char(admU.fecha_admision,'"+ConstantesBD.formatoFechaAp+"')||' - '||to_char(admU.hora_admision, 'HH24:MI')) END  ) "+
																	 " ELSE (to_char(admH.fecha_admision,'"+ConstantesBD.formatoFechaAp+"')||' - '||admH.hora_admision) END AS fechaHoraIngreso, (to_char(e.fecha_egreso,'"+ConstantesBD.formatoFechaAp+"')||' - '||to_char(e.hora_egreso,'HH24:MI')) as fechaHoraEgreso, "+
																	 " CASE WHEN admH.numero_autorizacion IS NULL THEN  admU.numero_autorizacion ELSE admH.numero_autorizacion END AS numeroautorizacion, "+
																	 " CASE WHEN admH.numero_autorizacion IS NULL THEN  admU.numero_autorizacion ELSE admH.numero_autorizacion END AS numeroautorizacionold, "+
																	 " c.id as cuenta, "+
																	 " c.estado_cuenta as estadoCuenta," +
																	 " getnomcentroatencion(cc.centro_atencion) AS nombreCentroAtencion "+
																	 " FROM cuentas c  "+
																	 " LEFT OUTER JOIN admisiones_hospi admH on(c.id=admh.cuenta) "+
																	 " LEFT OUTER JOIN admisiones_urgencias admU on(c.id=admU.cuenta) "+
																	 " LEFT OUTER JOIN egresos e ON(c.id=e.cuenta) " +
																	 " INNER JOIN centros_costo cc ON (c.area=cc.codigo) "+
																	 " WHERE c.codigo_paciente=? and " +
																	 "c.estado_cuenta not in ("+ConstantesBD.codigoEstadoCuentaCerrada+","+ConstantesBD.codigoEstadoCuentaExcenta+","+ConstantesBD.codigoEstadoCuentaProcesoFacturacion+")";
	
	/**
	 * Statement para actualizar el numero de autorizacion solo en la admision de hospitalizacion
	 */
	private final static String actualizarNumeroAutorizacionAdmisionHospitalizacionStr=" UPDATE admisiones_hospi SET numero_autorizacion=? WHERE cuenta=?";
	
	/**
	 * Statement para actualizar el numero de autorizacion solo en la admision de urgencias
	 */
	private final static String actualizarNumeroAutorizacionAdmisionUrgenciasStr="UPDATE admisiones_urgencias SET numero_autorizacion=? WHERE cuenta=?";
	
	/**
	 * Statement para actualizar el numero de solicitud de todas las solicitudes asociadas a una cuenta
	 */
	private final static String actualizarTodasSolicitudesxCuentaStr="UPDATE solicitudes SET numero_autorizacion=? WHERE cuenta=?";
	
	/**
	 * Statement que consulta todas las ordenes medicas dada una cuenta
	 */
	private final static String consultaSolicitudesOrdenStr= "SELECT "+ 
		"to_char(fecha_solicitud,'DD/MM/YYYY') AS fechaSolicitud, "+
		"hora_solicitud AS horaSolicitud, "+
		"consecutivo_ordenes_medicas AS consecutivo, "+
		"numero_solicitud AS numeroSolicitud, "+
		"numero_autorizacion AS numeroAutorizacion, "+
		"tipo AS tipo, "+
		//codigo del servicio
		"CASE WHEN getServicioSolicitud(numero_solicitud,tipo) IS NULL THEN "+ 
		" -1 "+
		"ELSE "+
		"getServicioSolicitud(numero_solicitud,tipo) "+
		"END AS codigoServicio, "+
		//Descripcion del servicio
		"CASE WHEN getServicioSolicitud(numero_solicitud,tipo) IS NULL THEN "+ 
			"getnomtiposolicitud(tipo) "+
		"ELSE "+
			"getnombreservicio(getServicioSolicitud(numero_solicitud,tipo),"+ConstantesBD.codigoTarifarioCups+") "+
		"END AS descripcionServicio, "+
		//Indicativo de justificación
		"CASE WHEN getServicioJustificado(numero_solicitud,tipo,?)=0 AND " +
				"tipo != "+ConstantesBD.codigoTipoSolicitudCirugia+" THEN " +
			"'false' " +
		"ELSE " +
			"'true' " +
		"END AS esJustificado "+
		"FROM "+ 
		"solicitudes "+
		"WHERE "+ 
		"tipo NOT IN ("+ConstantesBD.codigoTipoSolicitudMedicamentos+","+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+") AND " +
		"estado_historia_clinica != "+ConstantesBD.codigoEstadoHCAnulada+"  AND " +
		"cuenta =? ";
	

	
	/**
	 *Statement que consulta todos las solicitudes de medicamentos asociadas a una cuenta 
	 */
	private final static String consultaMedicamentosStr=" SELECT "+
																	"cm.articulo AS codigoServicio, "+ 
																	"to_char(sol.fecha_solicitud, '"+ConstantesBD.formatoFechaAp +"') as fechaSolicitud, "+ 
																	"to_char(sol.hora_solicitud, 'HH24:MI') as horaSolicitud, "+ 
																	"sol.numero_solicitud as numeroSolicitud, "+ 
																	"sol.consecutivo_ordenes_medicas as consecutivo, "+ 
																	"getdescarticulo(cm.articulo) as descripcionServicio, "+ 
																	"sol.tipo AS tipo, "+ 
																	"sol.numero_autorizacion as numeroAutorizacion, "+ 
																	"CASE WHEN getEsMedicamentoJustificado(sol.numero_solicitud,cm.articulo)=0 THEN 'false' ELSE 'true' END AS esJustificado "+ 
														"FROM solicitudes sol "+ 
														"INNER JOIN solicitudes_medicamentos sm ON(sm.numero_solicitud=sol.numero_solicitud) "+ 
														"INNER JOIN det_cargos cm ON(cm.numero_solicitud=sm.numero_solicitud) "+ 
														"WHERE sol.cuenta = ? AND cm.eliminado='"+ConstantesBD.acronimoNo+"'  and estado_historia_clinica != " + ConstantesBD.codigoEstadoHCAnulada;
																										
													
	/**
	 * Statement que actualiza todos los numeros de autorizacion de una solicitud que NO sea una solicitudede medicamentos
	 */
	private final static String modificarSolicitudesStr="UPDATE solicitudes SET numero_autorizacion=? WHERE tipo<>"+ConstantesBD.codigoTipoSolicitudMedicamentos+" and numero_solicitud=?";
	
	/**
	 * Statement que actualiza todos los numeros de autorizacion de una solicitud que SOLO sean una solicitud de medicamentos
	 */
	private final static String modificarMedicamentosStr="UPDATE solicitudes SET numero_autorizacion=? WHERE tipo="+ConstantesBD.codigoTipoSolicitudMedicamentos+" and numero_solicitud=?";
	
	/**
	 * Cadena que consulta el numero de autorizacion de una cirugia
	 */
	private final static String modificarNumeroAutorizacionCirugiaStr = "UPDATE sol_cirugia_por_servicio SET numero_autorizacion = ? WHERE codigo = ?";
	

	
	/**
	 * Método que carga los datos de una cuenta del paciente cargado en sesion y muestra
	 * las fechas de Admision o de apertura de la Cuenta segun sea su via de 
	 * Ingreso(urgencias-hospitalizacion o consulta extrena/ambulatorios)
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap cargarDatosPacienteCuenta(Connection con, int codigoPaciente)  throws SQLException
	{
		HashMap map= null;
		PreparedStatementDecorator cargarStatement=null;
		ResultSetDecorator rs=null;
		try
		{
			cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consultarTodasDatosPacienteXCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, codigoPaciente);
			rs=new ResultSetDecorator(cargarStatement.executeQuery());
			String[] colums={"viaIngreso","codigoviaingreso", "fechaHoraIngreso", "fechaHoraEgreso", "numeroAutorizacion","numeroAutorizacionOld", "cuenta", "estadoCuenta", "nombreCentroAtencion"};
			map=UtilidadBD.resultSet2HashMap(colums, rs, true, true).getMapa();
		}
		catch(Exception e){
			Log4JManager.error("ERROR cargarDatosPacienteCuenta", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(cargarStatement != null){
					cargarStatement.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return map;
	}
	
	
	/**
	 * Método para actualizar el numero de autorizacion en una admision de hospitalizacion
	 * @param con
	 * @param numeroAutorizacion
	 * @param cuenta
	 * @return
	 */
	public static int modificarNumeroAutorizacionAdmisionHospitalizacionTransaccional(Connection con, String numeroAutorizacion, int cuenta, String estado) 
	{
		int resp=0;	
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try{
				if (con == null || con.isClosed()) 
				{
					throw new SQLException ("Error SQL: Conexión cerrada");
				}
				if(estado.equals(ConstantesBD.inicioTransaccion))
				{
					myFactory.beginTransaction(con);
				}
					PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(actualizarNumeroAutorizacionAdmisionHospitalizacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

				ps.setString(1, numeroAutorizacion);
				ps.setInt(2, cuenta);
			
				resp=ps.executeUpdate();
				
				//Terminamos la transaccion, sea con un rollback o un commit.
				if (resp < 1) 
				{
					resp = 0;
					myFactory.abortTransaction(con);
				}
				if (estado.equals(ConstantesBD.finTransaccion))
				{
					myFactory.endTransaction(con);
				}

		}
		catch(SQLException e)
		{
			try
			{
				myFactory.abortTransaction(con);
			}
			catch(SQLException e1)
			{
				logger.error("no se pudo abortar la transaccion"+e1);
			}
			logger.warn(e+" Error en la modificación de datos de admision Hospitalización: SqlBaseActualizacionAutorizacionDao "+e.toString());
			resp=0;			
		}	
		return resp;	
	}
	
	
	/**
	 * Método para actualizar el numero de autorizacion en una admision de urgencias
	 * @param con
	 * @param numeroAutorizacion
	 * @param cuenta
	 * @return
	 */
	public static int modificarNumeroAutorizacionAdmisionUrgenciasTransaccional(Connection con, String numeroAutorizacion, int cuenta, String estado) 
	{
		int resp=0;	
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try{
				if (con == null || con.isClosed()) 
				{
					throw new SQLException ("Error SQL: Conexión cerrada");
				}
				if(estado.equals(ConstantesBD.inicioTransaccion))
				{
					myFactory.beginTransaction(con);
				}
				
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(actualizarNumeroAutorizacionAdmisionUrgenciasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

				ps.setString(1, numeroAutorizacion);
				ps.setInt(2, cuenta);
			
				resp=ps.executeUpdate();
				//Terminamos la transaccion, sea con un rollback o un commit.
				if (resp < 1) 
				{
					resp = 0;
					myFactory.abortTransaction(con);
				}
				if (estado.equals(ConstantesBD.finTransaccion))
				{
					myFactory.endTransaction(con);
				}
		}
		catch(SQLException e)
		{
			try
			{
				myFactory.abortTransaction(con);
			}
			catch(SQLException e1)
			{
				logger.error("no se pudo abortar la transaccion"+e1);
			}
			logger.warn(e+" Error en la modificación de datos de admision de Urgencias: SqlBaseActualizacionAutorizacionDao "+e.toString());
			resp=0;			
		}	
		return resp;	
	}
	
	
	/**
	 * Método para actualizar el numero de autorizacion de todas las solicitudes
	 * asociadas a una cuenta
	 * @param con
	 * @param numeroAutorizacion
	 * @param cuenta
	 * @return
	 */
	public static int modificarNumeroAutorizacionTodasSolicitudesXCuentaTransaccional(Connection con, String numeroAutorizacion, int cuenta, String estado) 
	{
		int resp=0;
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try{
				if (con == null || con.isClosed()) 
				{
					throw new SQLException ("Error SQL: Conexión cerrada");
				}
				if(estado.equals(ConstantesBD.inicioTransaccion))
				{
					myFactory.beginTransaction(con);
				}
				
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(actualizarTodasSolicitudesxCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

				ps.setString(1, numeroAutorizacion);
				ps.setInt(2, cuenta);
			
				resp=ps.executeUpdate();
				//Terminamos la transaccion, sea con un rollback o un commit.
				if (resp < 1) 
				{
					resp = 0;
					myFactory.abortTransaction(con);
				}
				if (estado.equals(ConstantesBD.finTransaccion))
				{
					myFactory.endTransaction(con);
				}
		}
		catch(SQLException e)
		{
			try
			{
				myFactory.abortTransaction(con);
			}
			catch(SQLException e1)
			{
				logger.error("no se pudo abortar la transaccion"+e1);
			}
			logger.warn(e+" Error en la modificación de datos de todas las solcitudes asociadas a una cuenta: SqlBaseActualizacionAutorizacionDao "+e.toString());
			resp=0;			
		}	
		return resp;	
	}
	
	
	/**
	 * Método para cargar todas las solicitudes de ordenes medicas
	 * (Valoraciones-Procedimientos-Interconsultas-Evoluciones)
	 * @param con
	 * @param cuenta
	 * @param contrato (para verificar la justificacion)
	 * @return
	 */
	public static ResultSetDecorator cargarSolicitudesOrden(Connection con, int cuenta, int contrato)
	{
		try
		{
			PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consultaSolicitudesOrdenStr+" ORDER BY consecutivo ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, contrato);
			cargarStatement.setInt(2, cuenta);
			
			return new ResultSetDecorator(cargarStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de las solicitudes medicas : SqlBaseActualizacionAutorizacionDao"+e.toString());
			return null;
		}
	}
	
	/**
	 * Método para cargar todas las solicitudes de medicamentos asociadas a una cuenta
	 * @param con
	 * @param cuenta
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap cargarMedicamentos(Connection con, int cuenta)
	{
		HashMap map= null;
		PreparedStatementDecorator cargarStatement=null;
		ResultSetDecorator rs=null;
		try
		{
			cargarStatement= new PreparedStatementDecorator(con.prepareStatement(consultaMedicamentosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, cuenta);
			
			rs=new ResultSetDecorator(cargarStatement.executeQuery());
			String[] colums={"fechaSolicitud", "horaSolicitud", "consecutivo", "descripcionServicio","numeroAutorizacion", "numeroSolicitud","codigoServicio","esJustificado"};
			map=UtilidadBD.resultSet2HashMap(colums, rs, false, true).getMapa();
		}
		catch(Exception e){
			Log4JManager.error("ERROR cargarMedicamentos", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(cargarStatement != null){
					cargarStatement.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return map;
	}
	
	
	/**
	 * Método para actualizar el numero de autorizacion de todas las solicitudes que NO sean de medicamentos
	 * @param con
	 * @param numeroAutorizacion
	 * @param numeroSolicitud
	 * @return
	 */
	public static int modificarNumeroAutorizacionSolicitudes(Connection con, String numeroAutorizacion, int numeroSolicitud) 
	{
		int resp=0;	
		try{
				if (con == null || con.isClosed()) 
				{
					throw new SQLException ("Error SQL: Conexión cerrada");
				}
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarSolicitudesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

				ps.setString(1, numeroAutorizacion);
				ps.setInt(2, numeroSolicitud);
			
				resp=ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la modificación de los numero de autorizacion de todas las solictudes que no son medicamentos: SqlBaseActualizacionAutorizacionDao "+e.toString());
			resp=0;			
		}	
		return resp;	
	}
	
	/**
	 * Método para actualizar el numero de autorizacion de todas las solicitudes que SOLO sean de medicamentos
	 * @param con
	 * @param numeroAutorizacion
	 * @param numeroSolicitud
	 * @return
	 */
	public static int modificarNumeroAutorizacionMedicamentos(Connection con, String numeroAutorizacion, int numeroSolicitud) 
	{
		int resp=0;	
		try{
				if (con == null || con.isClosed()) 
				{
					throw new SQLException ("Error SQL: Conexión cerrada");
				}
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarMedicamentosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

				ps.setString(1, numeroAutorizacion);
				ps.setInt(2, numeroSolicitud);
			
				resp=ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la modificación de los numero de autorizacion de todas las solictudes de medicamentos: SqlBaseActualizacionAutorizacionDao "+e.toString());
			resp=0;			
		}	
		return resp;	
	}
	
	/**
	 * Busqueda Avanzada de una solicitud
	 * @param con
	 * @param fechaSolicitud
	 * @param consecutivo
	 * @param descripcionServicio
	 * @param numeroAutorizacion
	 * @param contrato
	 * @return
	 */
	public static ResultSetDecorator busquedaAvanzada (Connection con, int cuenta,String fechaSolicitud, String horaSolicitud, String consecutivo,String descripcionServicio,String numeroAutorizacion,int contrato, boolean esServicio)
	{
				try
				{
					PreparedStatementDecorator ps = null;
				
					if (con == null || con.isClosed()) 
					{
						DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
						con = myFactory.getConnection();
					}  
				
					boolean existe= false;
					String avanzadaStr = "";
					
					if(!fechaSolicitud.equals(""))
					{
						avanzadaStr+=" fechaSolicitud = '"+fechaSolicitud+"'";
						existe = true;
					}
					if(!horaSolicitud.equals(""))
					{
						if(existe)
							avanzadaStr += " AND ";
						avanzadaStr+=" horaSolicitud = '"+horaSolicitud+"'";
						existe = true;
					}
					if(!consecutivo.equals(""))
					{	
						if(existe)
							avanzadaStr = " AND ";
						avanzadaStr+=" consecutivo = '"+consecutivo+"'";
						existe = true;
					}
					if(!descripcionServicio.equals(""))
					{	
						if(existe)
							avanzadaStr = " AND ";
						avanzadaStr+=" descripcionServicio LIKE '%"+descripcionServicio+"%' ";
						existe = true;
					}
					if(!numeroAutorizacion.equals("0") && !numeroAutorizacion.equals(""))
					{	
						if(existe)
							avanzadaStr = " AND ";
						avanzadaStr+=" numeroAutorizacion LIKE '%"+numeroAutorizacion+"%' ";
						existe = true;
					}
					String consulta="";
					
					consulta = "SELECT "+
						"s.fechaSolicitud, "+
						"s.horaSolicitud, "+
						"s.consecutivo, "+
						"s.numeroAutorizacion, "+
						"s.numeroSolicitud, "+
						"s.tipo, "+
						"s.codigoServicio, "+
						"s.descripcionServicio," +
						"s.esJustificado "+ 
						"FROM (" + (esServicio?consultaSolicitudesOrdenStr:consultaMedicamentosStr)  + ") s " + 
						(avanzadaStr.equals("")?"":" WHERE "+avanzadaStr)+
						" ORDER BY consecutivo ";
					
					ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					if(esServicio)
					{	
						ps.setInt(1,contrato);
						ps.setInt(2,cuenta);
					}
					else
						ps.setInt(1,cuenta);
					return new ResultSetDecorator(ps.executeQuery());
				}
				catch(SQLException e)
				{
					logger.warn(e+"Error en busquedaAvanzadaActualizacionAutorizacion de Solicitudes : SqlBaseActualizacionAutorizacionDao "+e.toString() );
					return null;
				}	    
	}
	
	/**
	 * Método que modifica el numero de autorizacion de una cirugia
	 * @param con
	 * @param numeroAutorizacion
	 * @param codigoCirugia
	 * @return
	 */
	public static int modificarNumeroAutorizacionCirugia(Connection con,String numeroAutorizacion,int codigoCirugia)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(modificarNumeroAutorizacionCirugiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,numeroAutorizacion);
			pst.setInt(2,codigoCirugia);
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en modificarNumeroAutorizacionCirugia de SqlBaseActualizacionAutorizacionDao: "+e);
			return -1;
		}
	}
	
}
