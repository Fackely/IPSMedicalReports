/*
 * Created on Marzo 1, 2005
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;



/**
 * @author Sebastián Gómez Rivillas
 *
 * Clase que maneja las consultas de categorias de documentos biblioteca
 */
public class SqlBaseSolicitudesDao {
	
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseSolicitudesDao.class);
	
	/**
	 * Sección SELECT para realizar
	 * un listado de solicitudes de la cuenta de acuerdo a parámetros de revisión cuenta
	 */
	private static final String listarSolicitudesCuentaSELECT_01Str = "SELECT "+
		"getIndicativoRevisionCuenta(s.numero_solicitud,s.tipo,?,s.numero_autorizacion,s.pyp)  AS indicativo,"+ 
		"CASE WHEN s.numero_autorizacion IS NULL THEN '' ELSE s.numero_autorizacion END as numero_autorizacion,"+
		"s.consecutivo_ordenes_medicas as orden,"+
		"getnomtiposolicitud(s.tipo) as tipo,"+
		"s.fecha_grabacion as fecha,"+
		"s.numero_solicitud as id,"+
		"s.tipo as numero_tipo " + 
		" FROM solicitudes s ";
	
	/**
	 * Sección INNER para la consulta de solicitudes por subcuenta
	 */
	private static final String listarSolicitudesCuentaINNER_01Str =" INNER JOIN " +
		"solicitudes_subcuenta ssc ON(ssc.solicitud=s.numero_solicitud AND ssc.eliminado='"+ConstantesBD.acronimoNo+"' ) ";
	
	/**
	 * Sección WHERE para la consulta de solicitudes por cuenta
	 */
	private static final String listarSolicitudesCuentaWHERE_01Str = " WHERE "+ 
		"s.cuenta=? ";
	
	/**
	 * Sección WHERE para la consulta de solicitudes por subcuenta
	 */
	private static final String listarSolicitudesCuentaWHERE_02 = " WHERE "+
		"ssc.sub_cuenta=? "; 
	
	//no se usa.
	private static final String listarSolicitudesCuentaWHERE_03Str = " AND " +
		
		///Validación para verificar que los ingresos de urgencias y hospitalizacion tengan valoración
		"(" +
			"(s.tipo!="+ConstantesBD.codigoTipoSolicitudInicialUrgencias+" AND " +
			 "s.tipo!="+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+")" +
			" OR " +
			"((s.tipo="+ConstantesBD.codigoTipoSolicitudInicialUrgencias+" OR " +
				"s.tipo!="+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+") AND " +
			 "s.numero_solicitud IN (SELECT numero_solicitud FROM valoraciones) "+
			")" +
		") AND "+
		//************************************************************************************
		" ("+
			//indicador AUTH solicitudes servicios
			"(getEsServiciosCobrables(getServicioSolicitud(s.numero_solicitud,s.tipo), ?)=1 AND " +
			"s.tipo!="+ConstantesBD.codigoTipoSolicitudMedicamentos+" AND " +
			"s.tipo!="+ConstantesBD.codigoTipoSolicitudCirugia+" AND " +
			"(s.numero_autorizacion='' OR s.numero_autorizacion IS NULL)) " +
			"OR "+
			"(getServicioSolicitud(s.numero_solicitud,s.tipo) IS NULL AND " +
			"s.tipo!="+ConstantesBD.codigoTipoSolicitudMedicamentos+" AND " +
			"s.tipo!="+ConstantesBD.codigoTipoSolicitudCirugia+") " +
			"OR "+
			"(getEsServiciosCobrablesQx(s.numero_solicitud, ?)=1 AND " +
			"s.tipo = "+ConstantesBD.codigoTipoSolicitudCirugia+" AND " +
			"(s.numero_autorizacion='' OR s.numero_autorizacion IS NULL) ) "+
			"OR "+
			//indicador JUS solicitudes medicamentos
			"getEsMedicamentosJustificados(s.numero_solicitud)=0 " +
			"OR "+
			//indicador POOL solicitudes servicio y cirugias
			"(getEsPoolXSolicitud(s.numero_solicitud,s.tipo)=0 AND " +
			"s.tipo!="+ConstantesBD.codigoTipoSolicitudMedicamentos+" AND " +
					"s.tipo!="+ConstantesBD.codigoTipoSolicitudEstancia+"))";
	
	/**
	 * <code>listarSolicitudesServiciosStr</code> usada para listar los campos de aquella solicitud que es de servicios
	 */
	private static final String listarSolicitudesServiciosStr="SELECT "+
		"CASE WHEN s.tipo=" +ConstantesBD.codigoTipoSolicitudProcedimiento+" OR "+
			"s.tipo="+ConstantesBD.codigoTipoSolicitudInterconsulta+" OR "+
			"s.tipo="+ConstantesBD.codigoTipoSolicitudCita+"  "+
		"THEN "+
			"getcodigoespecialidad(getServicioSolicitud(s.numero_solicitud,s.tipo)) || '-' || "+
			"getServicioSolicitud(s.numero_solicitud,s.tipo) "+
		"ELSE "+
			"getServicioSolicitud(s.numero_solicitud,s.tipo) || '' "+
		"END AS codigo,"+
		"getNombreServicio(getServicioSolicitud(s.numero_solicitud,s.tipo), "+ConstantesBD.codigoTarifarioCups+") as descripcion, "+
		"count(1) as cantidad, "+
		//Se comentarean ya q esta funciones manejan las tablas cargos y cargo_medicamentos eliminadas de axioma
		//"getValorTotalCargo(s.numero_solicitud,getServicioSolicitud(s.numero_solicitud,s.tipo)) as valortotal, "+
		//"getFechaCargo(s.numero_solicitud,getServicioSolicitud(s.numero_solicitud,s.tipo)) as fechacargo, "+
		"administracion.getnombremedico(s.codigo_medico_responde) as medico, "+
		"CASE WHEN getEsPoolXSolicitud(s.numero_solicitud,s.tipo)=0 "+
		"THEN  '0' ELSE  "+
		//@todo tener en cuenta que el pool de las cx se va ha cargar del detalle honorarios, en ese momento entonces se envian los datos NO como un codigoNoValido, ver funcion sql getPoolXSolicitud
		"getDescripcionPool(getPoolXSolicitud(s.numero_solicitud,"+ConstantesBD.codigoNuncaValido+","+ConstantesBD.codigoNuncaValido+","+ConstantesBD.codigoTipoSolicitudCirugia+","+ConstantesBD.codigoNuncaValido+","+ConstantesBD.codigoNuncaValido+")) END as pool, "+
		"s.codigo_medico_responde AS codmedico," +
		"getFechaRespuestaSol(s.numero_solicitud,s.tipo) AS fecha_consulta "+
		"FROM solicitudes s "+
		"WHERE s.numero_solicitud=? "+
		"GROUP BY s.codigo_medico_responde,s.tipo,s.numero_solicitud";
	/**
	 * <code>listarSolicitudesMedicamentosStr</code> usada para listar los campos de aquellas solicitud que es de medicamentos
	 */
	private static final String listarSolicitudesMedicamentosStr="SELECT "+ 
		"CASE WHEN getesmedicamentojustificado(ls.numero_solicitud,ls.articulo) = 0 " +
		" THEN 'JUS' ELSE ' ' END as indicador, "+
		"ls.articulo as codigo, "+
		"getdescripcionarticulo(ls.articulo) || ' Conc:' ||  getconcentracionarticulo(ls.articulo) as descripcion, "+
		"gettotaladminfarmacia(ls.articulo,ls.numero_solicitud,false) as cantidad, "+
		//Se comentarean ya q esta funciones manejan las tablas cargos y cargo_medicamentos eliminadas de axioma
		//"getvalortotalcargomedicamentos(ls.numero_solicitud,ls.articulo) as valor_unitario, "+
		//"getvalortotalcargomedicamentos(ls.numero_solicitud,ls.articulo) as valor, "+
		//"getfechacargomedicamentos(ls.numero_solicitud,ls.articulo) as fecha "+
		"FROM "+
		"(SELECT dd.articulo as articulo, ds.numero_solicitud as numero_solicitud "+
			"FROM detalle_despachos dd "+ 
			"INNER JOIN despacho d ON(dd.despacho=d.orden) "+
			"INNER JOIN detalle_solicitudes ds on(ds.numero_solicitud=d.numero_solicitud) "+ 
			"WHERE "+ 
			"dd.articulo!=dd.art_principal AND "+ 
			"ds.numero_solicitud=? "+
			"UNION "+ 
			"SELECT ds.articulo as articulo, ds.numero_solicitud as numero_solicitud "+
			"FROM detalle_solicitudes ds "+
			"WHERE ds.numero_solicitud=?) ls,"+
		" solicitudes s "+
		"WHERE "+
		"s.numero_solicitud=ls.numero_solicitud";
	/**
	 * <code>insertarPoolMedicoXSolicitudStr</code> usada para insertar el pool
	 */
	private static final String insertarPoolMedicoXSolicitudStr="UPDATE solicitudes SET pool=? WHERE numero_solicitud=?";
	

//modifique esta alejo.. solo agregue la funcion del codigo del servicio
	/**	 * Consulta de las interconsultas y procedimientos asignados al paciente por solicitudes	 */
	private static final String listadoInterconsultasProcedimiento ="SELECT servicios.numero_solicitud, servicios.servicio " +
								"FROM " +
								"(" +
										"SELECT si.numero_solicitud AS numero_solicitud, getcodigoservicio(si.codigo_servicio_solicitado, "+ConstantesBD.codigoTarifarioCups+") || ' - ' || getnombreservicio(si.codigo_servicio_solicitado, "+ConstantesBD.codigoTarifarioCups+") AS servicio " +
											"FROM solicitudes s " +
												"INNER JOIN solicitudes_inter si ON (s.numero_solicitud=si.numero_solicitud) " +
												"INNER JOIN servicios ser ON (si.codigo_servicio_solicitado=ser.codigo) " +
												"INNER JOIN cuentas cue on(cue.id=s.cuenta) " +
													"WHERE cue.id_ingreso=? " +
									"UNION " +
										"SELECT sp.numero_solicitud AS numero_solicitud, getcodigoservicio(sp.codigo_servicio_solicitado, "+ConstantesBD.codigoTarifarioCups+") || ' - ' || getnombreservicio(sp.codigo_servicio_solicitado, "+ConstantesBD.codigoTarifarioCups+") AS servicio " +
											"FROM solicitudes s " +
											"INNER JOIN sol_procedimientos sp ON (s.numero_solicitud=sp.numero_solicitud) " +
											"INNER JOIN servicios ser ON (sp.codigo_servicio_solicitado=ser.codigo) " +
											"INNER JOIN cuentas cue on(cue.id=s.cuenta) " +
												"WHERE cue.id_ingreso=?  " +
									"UNION " +
										"SELECT s.numero_solicitud, getcodigoservicio(scs.servicio,"+ConstantesBD.codigoTarifarioCups+") || ' - ' || getnombreservicio(scs.servicio,"+ConstantesBD.codigoTarifarioCups+") AS servicio " +
											"FROM sol_cirugia_por_servicio scs INNER JOIN solicitudes s ON (s.numero_solicitud=scs.numero_solicitud) " +
											"INNER JOIN cuentas cue on(cue.id=s.cuenta) " +
											"WHERE cue.id_ingreso=?  " +
								") servicios " +
									"ORDER BY servicios.servicio" ;
	
	/**
	 * adición de Sebastián
	 * Método que obtiene la lista de solicitudes de acuerdo a los parámetros
	 * especificados en RevisionCuenta del módulo facturación
	 * @param con
	 * @param idCuenta
	 * @param contrato
	 * @return
	 */
	public static HashMap listarSolicitudes(Connection con,int idCuenta,int contrato,boolean opcion){
				try
				{
					String consulta = listarSolicitudesCuentaSELECT_01Str;
					PreparedStatementDecorator statement;
					if(opcion)
					{
						consulta += listarSolicitudesCuentaWHERE_01Str; //listarSolicitudesCuentaWHERE_03Str;
						statement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					}
					else
					{
						consulta += listarSolicitudesCuentaINNER_01Str + listarSolicitudesCuentaWHERE_02 ;
							//listarSolicitudesCuentaWHERE_03Str;
						statement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					}
					statement.setInt(1,contrato);
					statement.setInt(2,idCuenta);
					
					
					HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(statement.executeQuery()),true,false);
			        statement.close();
					return mapaRetorno;
		
				}
				catch(SQLException e)
				{
					
					logger.error("Error listando solicitudes SqlBaseSolicitudesDao: "+e);
					return null;
				}
		}
	
	/**
	 * Adición Sebastián
	 * Método usado en la revisión de la cuenta para la búsqueda
	 * avanzada de solicitudes
	 * @param con
	 * @param orden
	 * @param tipo
	 * @param fecha
	 * @param estado
	 * @param contrato
	 * @param opcion
	 * @return
	 */
	public static HashMap busquedaRevisionSolicitudes(
			Connection con,int idCuenta,int orden,int tipo,String fecha,
			int estado,int contrato,boolean opcion)
	{
		try
		{
			PreparedStatementDecorator statement;
			String consulta = listarSolicitudesCuentaSELECT_01Str;
			if(opcion)
				consulta += listarSolicitudesCuentaWHERE_01Str; // listarSolicitudesCuentaWHERE_03Str;
			else
				consulta += listarSolicitudesCuentaINNER_01Str + listarSolicitudesCuentaWHERE_02 ;
					//listarSolicitudesCuentaWHERE_03Str;
			
			if(orden!=-1)
			{
				consulta+=" AND s.consecutivo_ordenes_medicas="+orden;
			}
			if(tipo!=-1)
			{
				consulta+=" AND s.tipo="+tipo;
			}
			if(!fecha.equals(""))
			{
				consulta+=" AND s.fecha_grabacion='"+UtilidadFecha.conversionFormatoFechaABD(fecha)+"'";
			}
			statement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setInt(1,contrato);
			statement.setInt(2,idCuenta);
			
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(statement.executeQuery()),true,false);
	        statement.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en busquedaRevisionSolicitudes de SqlBaseSolicitudesDao: "+e);
			return null;
		}
	}
	
	/**
	 * adición de Sebastián
	 * Método que obtiene los campos de la solicitud que es de servicios
	 * @param con
	 * @param solicitud
	 * @return
	 */
	public static Collection listarSolicitudesServicios(Connection con,int solicitud){
		try
		{
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(listarSolicitudesServiciosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setInt(1,solicitud);
			
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(statement.executeQuery()));

		}
		catch(SQLException e)
		{
			
			logger.error("Error listando solicitudes de servicios en SqlBaseSolicitudesDao: "+e);
			return null;
		}
	}
	/**
	 * adición de Sebastián
	 * Método que obtiene los campos de la solicitud que es de medicamentos
	 * @param con
	 * @param solicitud
	 * @return
	 */
	public static Collection listarSolicitudesMedicamentos(Connection con,int solicitud){
		try
		{
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(listarSolicitudesMedicamentosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setInt(1,solicitud);
			statement.setInt(2,solicitud);
			
			
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(statement.executeQuery()));

		}
		catch(SQLException e)
		{
			
			logger.error("Error listando solicitudes de medicamentos en SqlBaseSolicitudesDao: "+e);
			return null;
		}

	}
	/**
	 * adición de Sebastián
	 * Método que inserta un pool de medico por solicitud en el caso de que el médico pertenezca
	 * a varios pooles
	 * @param con
	 * @param solicitud
	 * @param pool
	 * @return
	 */
	public static int insertarPoolMedicoXSolicitud(Connection con,int solicitud,int pool)
	{
			
			try{
				PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(insertarPoolMedicoXSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				statement.setInt(1,pool);
				statement.setInt(2,solicitud);
				
				return statement.executeUpdate();

			}
			catch(SQLException e){
				logger.error("Error insetrando pool por solicitud en SqlBaseSolicitudesDao: "+e);
				return -1;
			}
	}
	
	/**
	 * Método implementado para insertar el Log de modificacion de tarifas
	 * en la funcionalidad de Revisión de la Cuenta
	 * @param con
	 * @param cuenta
	 * @param solicitud
	 * @param servicio
	 * @param articulo
	 * @param tipoCargo
	 * @param tarifaInicial
	 * @param tarifaFinal
	 * @param usuario
	 * @param institucion
	 * @param estado
	 * @param consulta
	 * @return
	 */
	public static int insertarLogRevisionCuenta(Connection con,int cuenta,int solicitud,int servicio,int articulo,
			int tipoCargo,double tarifaInicial,double tarifaFinal,String usuario,int institucion,String estado,String consulta)
	{
		try
		{
			//se inicia transacción
			if(estado.equals(ConstantesBD.inicioTransaccion))
				UtilidadBD.iniciarTransaccion(con);
			
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			pst.setInt(1,cuenta);
			pst.setInt(2,solicitud);
			
			if(tipoCargo==ConstantesBD.codigoTipoCargoServicios)
			{
				pst.setInt(3,servicio);
				pst.setNull(4,Types.INTEGER);
			}
			else if(tipoCargo == ConstantesBD.codigoTipoCargoArticulos)
			{
				pst.setNull(3,Types.INTEGER);
				pst.setInt(4,articulo);
			}
			
			pst.setInt(5,tipoCargo);
			pst.setDouble(6,tarifaInicial);
			pst.setDouble(7,tarifaFinal);
			pst.setString(8,usuario);
			pst.setInt(9,institucion);
			
			int resp = pst.executeUpdate();
			
			if(resp>0)
			{
				//se finaliza transacción
				if(estado.equals(ConstantesBD.finTransaccion))
					UtilidadBD.finalizarTransaccion(con);
			}
			else
				UtilidadBD.abortarTransaccion(con);
			
			return resp;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarLogRevisionCuenta de SqlBaseSolicitudesDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Mètodo que consulta los procedimientos e interconsultas asociados al paciente, para
	 * mostrarlos en la ventana al ubicarse el usuario sobre la flecha de detalle en el listado de 
	 * solicitudes  de interconsultas y procedimientos
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static Collection consultarInterconsultasProcedimientos(Connection con, int codigoCuenta)
	{
		try
	    {
	     
	    PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(listadoInterconsultasProcedimiento,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    ps.setObject(1,Utilidades.obtenerCodigoIngresoDadaCuenta(codigoCuenta+""));
	    ps.setObject(2,Utilidades.obtenerCodigoIngresoDadaCuenta(codigoCuenta+""));
	    ps.setObject(3,Utilidades.obtenerCodigoIngresoDadaCuenta(codigoCuenta+""));
	     
	     return UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery()));			
	    }
	    catch (SQLException e)
	    {
	        logger.warn(e+" Error en la consulta de las interconsultas y procedimientos asignados al paciente: SqlBaseSolicitudesDao "+e.toString());
	        return null;  
	    }
	}

	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @param codigoContrato
	 * @return
	 */
	public static HashMap obtenerSolicitudesServicioIndicadorCapitado(Connection con, String idCuenta, int codigoContrato) 
	{
		HashMap mapa=new HashMap();
		int numServCapitados=0;
		int numReg=0;
		PreparedStatementDecorator ps=null;
		String consulta=" SELECT " +
							" sol.numero_solicitud as solicitud,	" +
							" solser.servicio as servicio , " +
							" getesnivelservcontratado(?,solser.servicio) as contratado " +
						" FROM 	solicitudes sol " +
						" INNER JOIN cuentas cue ON(cue.id=sol.cuenta) " +
						" INNER JOIN ((select solicitud as numero_solicitud,servicio from cargos ) UNION (select numero_solicitud,servicio from sol_cirugia_por_servicio)) solser on(sol.numero_solicitud=solser.numero_solicitud) " +//con este inner join filtramos de una que las solicitudes son diferentes a medicamentos 
						" WHERE " +
	             		" cue.id=? " +
	             		" AND cue.estado_cuenta IN("+ConstantesBD.codigoEstadoCuentaAsociada+", "+ConstantesBD.codigoEstadoCuentaActiva+", "+ConstantesBD.codigoEstadoCuentaProcesoDistribucion+") " +
	             		" AND sol.estado_historia_clinica NOT IN("+ConstantesBD.codigoEstadoHCAnulada+") ";
	    try {
	    	
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoContrato);
			ps.setString(2,idCuenta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				mapa.put("solicitud_"+numReg,rs.getString("solicitud"));
				mapa.put("servicio_"+numReg,rs.getString("servicio"));
				mapa.put("contratado_"+numReg,rs.getString("contratado"));
				if(rs.getBoolean("contratado"))
				{
					numServCapitados++;
				}
				numReg++;
			}
			ps.close();
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		mapa.put("numServCapitados",numServCapitados+"");
		mapa.put("numRegistros",numReg);
		
		return mapa;
	}

	
	
	/**
	 * Cadena para consultar el listado de solicitudes de procedimientos.
	 */
	private static String cadenaConsultaProcedimientos=
			"SELECT so.numero_solicitud AS numeroSolicitud," +
					" so.cobrable AS cobrable,"	+
					" so.consecutivo_ordenes_medicas AS consecutivoOrdenesMedicas," +
					" so.cuenta AS codigoCuenta," +
					" so.urgente AS urgente," + 
					" so.va_epicrisis AS vaEpicrisis," + 
					" so.centro_costo_solicitado AS codigoCentroCostoSolicitado," + 
					" case when so.pyp is null or so.pyp ="+ValoresPorDefecto.getValorFalseParaConsultas() +" then 'false' else 'true' end as pyp, " +
					" TO_CHAR(so.fecha_solicitud,'dd/mm/yyyy') AS fechaSolicitud," +
					" so.hora_solicitud||'' AS horaSolicitud," +
					" TO_CHAR(so.fecha_grabacion,'dd/mm/yyyy') AS fechaGrabacion," +
					" so.hora_grabacion||'' AS horaGrabacion," +
					" CASE WHEN so.fecha_interpretacion IS NULL THEN '' ELSE TO_CHAR(so.fecha_interpretacion,'dd/mm/yyyy') END AS fechaInterpretacion, " +
					" CASE WHEN so.hora_interpretacion IS NULL THEN '' ELSE so.hora_interpretacion||'' END AS horaInterpretacion," +
					" CASE WHEN rsp.fecha_ejecucion IS NULL THEN '' ELSE TO_CHAR(rsp.fecha_ejecucion,'dd/mm/yyyy') END AS fechaRespuesta," +
					" CASE WHEN rsp.hora_grabacion IS NULL THEN '' ELSE rsp.hora_grabacion||'' END AS horaRespuesta," +
					" CASE WHEN so.codigo_medico_interpretacion IS NULL THEN -1 ELSE codigo_medico_interpretacion END AS codigoMedicoInterpretacion," +
					" CASE WHEN so.interpretacion IS NULL THEN '' ELSE so.interpretacion END  AS interpretacion," +
					" CASE WHEN so.numero_autorizacion IS NULL THEN '' ELSE so.numero_autorizacion END AS numeroAutorizacion," +
					" getnomcentrocosto(so.centro_costo_solicitado) AS nombreCentroCostoSolicitado," +
					" so.centro_costo_solicitante "		+ "AS codigoCentroCostoSolicitante," +
					" getnomcentrocosto(so.centro_costo_solicitante) AS nombreCentroCostoSolicitante, " +
					" so.especialidad_solicitante AS codigoEspecialidadSolicitante," +
					" getnombreespecialidad(so.especialidad_solicitante) AS nombreEspecialidadSolicitante," +
					" so.estado_historia_clinica AS codigoEstadoHistoriaClinica,"	+
					" getestadosolhis(so.estado_historia_clinica) AS nombreEstadoHistoriaClinica,"	+
					" so.ocupacion_solicitada AS codigoOcupacionSolicitada," +
					" getocupacion(so.ocupacion_solicitada) AS nombreOcupacionSolicitada," +
					" so.tipo AS codigoTiposolicitud,"	+
					" ts.nombre AS nombreTiposolicitud,"	+
					" CASE WHEN so.codigo_medico IS NULL THEN -1 ELSE so.codigo_medico END AS codigoMedicoSolicitante,"	+
					" coalesce(getnombrepersona(so.codigo_medico),'') as nombreMedicoSolicita, "+
					" CASE WHEN so.codigo_medico_responde IS NULL THEN -1 ELSE so.codigo_medico_responde END AS codigoMedicoResponde," +
					" coalesce(getnombrepersona(so.codigo_medico_responde),'') as nombreMedicoResponde, "+
					" c.codigo_paciente AS codigoPaciente, " +
					" getnombrepersona(c.codigo_paciente) as nombrePaciente, "+
					" cc.centro_atencion AS codCentroAtencion," +
					" getnomcentroatencion(cc.centro_atencion) AS nomCentroAtencion, " +
					" CASE WHEN se.especialidad IS NULL THEN -1 ELSE se.especialidad END AS codigoEspecialidadSolicitada," +
					" getnombreespecialidad(se.especialidad) AS nombreEspecialidadSolicitada, " +
					" solicitud_multiple AS multiple," +
					" getcamacuenta(c.id,c.via_ingreso) as cama, " +
					" finalizada as finalizada," +
					" se.portatil_asociado As portatil, " +
					" CASE WHEN getSolicitudTieneIncluidos(so.numero_solicitud)>0 THEN "+ValoresPorDefecto.getValorTrueParaConsultas() +" ELSE "+ValoresPorDefecto.getValorFalseParaConsultas() +" END as incluyeServiciosArticulos "+
			" FROM solicitudes so " +
			" INNER JOIN tipos_solicitud ts ON(so.tipo=ts.codigo) " +
			" INNER JOIN cuentas c  ON (so.cuenta=c.id) " +
			" INNER JOIN centros_costo cc  on (c.area=cc.codigo) " +
			" INNER JOIN sol_procedimientos sp on (sp.numero_solicitud=so.numero_solicitud) " +
			" INNER JOIN servicios se ON (sp.codigo_servicio_solicitado=se.codigo) " +
			" LEFT OUTER JOIN res_sol_proc rsp on(rsp.numero_solicitud=so.numero_solicitud) " +
			" left outer join his_camas_cuentas hcc on(hcc.cuenta=so.cuenta) " + 
			" WHERE so.tipo = " + ConstantesBD.codigoTipoSolicitudProcedimiento;
	
	
	/**
	 * 
	 * @param con
	 * @param codigoCentroCostoSolicitado
	 * @param codigoCentroCostoSolicitante
	 * @param codigoCentroCostoTratante
	 * @param codigoMedico
	 * @param codigoOcupacionSolicitada
	 * @param codigoPaciente
	 * @param codigoTipoSolicitud
	 * @param estadoCuenta
	 * @param estadoHistoriaClinica
	 * @param codigoEspecialidadSolicitada
	 * @param codigoCuentaAsociada
	 * @param codigoCentroCostoIntentaAcceso
	 * @param codigoCuenta
	 * @param institucion
	 * @param resumenAtenciones
	 * @param codigoCentroAtencionCuenta
	 * @param centroCostoSolicitanteFiltro 
	 * @param fechaFinalFiltro 
	 * @param fechaInicialFiltro 
	 * @return
	 */
	public static Collection listarSolicitudesProcedimientos(Connection con, int codigoCentroCostoSolicitado, 
										int codigoCentroCostoSolicitante, int codigoCentroCostoTratante, int codigoMedico, 
										int codigoOcupacionSolicitada, int codigoPaciente, int codigoTipoSolicitud, int 
										estadoCuenta, int estadoHistoriaClinica, int[] codigoEspecialidadSolicitada,
										int codigoCuentaAsociada, int codigoCentroCostoIntentaAcceso, int codigoCuenta, int institucion, 
										boolean resumenAtenciones, int codigoCentroAtencionCuenta, String fechaInicialFiltro, String fechaFinalFiltro, String centroCostoSolicitanteFiltro,String areaFiltro,String centroCostoSolicitadoFiltro,String pisoFiltro,String habitacionFiltro,String camaFiltro,boolean requierePortatilFiltro)
	{
		int				tam;
		StringBuffer	sb;
		sb = new StringBuffer(cadenaConsultaProcedimientos);

		/* Filtrar el centro de costo solicitado */
		if(codigoCentroCostoSolicitado > -1)
		{
			sb.append(" AND so.centro_costo_solicitado IN(" +  ConstantesBD.codigoCentroCostoTodos + "," + ConstantesBD.codigoCentroCostoExternos + "," + codigoCentroCostoSolicitado + ")");
		}
		if(codigoCentroAtencionCuenta > -1 && !resumenAtenciones)
		{
			sb.append(" AND cc.centro_atencion = "+ codigoCentroAtencionCuenta);
		}
		
		
		/* Filtrar el centro de costo solicitante */
		if(codigoCentroCostoSolicitante > -1)
		{
			
			//Se deben manejar dos grandes casos, cuando queremos ver
			//los que se desean interpretar (estan en estado respondido)
			if (estadoHistoriaClinica==ConstantesBD.codigoEstadoHCRespondida)
			{
				if (codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudProcedimiento)
				{
					
					//Además del tratante, la puede intrepretar quien la solicitó
					sb.append(" AND (   " + codigoCentroCostoIntentaAcceso +"=" + codigoCentroCostoSolicitante + "  OR " + codigoCentroCostoIntentaAcceso +" IN (SELECT tc.centro_costo from tratantes_cuenta  tc  where so.numero_solicitud=tc.solicitud and tc.cuenta="+codigoCuenta+" and (tc.fecha_inicio<CURRENT_DATE or (tc.fecha_inicio=CURRENT_DATE and tc.hora_inicio<="+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+")) and (tc.fecha_fin IS NULL or tc.fecha_fin>CURRENT_DATE or (tc.fecha_fin=CURRENT_DATE and tc.hora_fin>="+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+") )  )                    )");
				}
			}
			else
			{
				//Para cuando no es interpretar se sigue manejando la validación
				//previa
				sb.append(" AND so.centro_costo_solicitante=" + codigoCentroCostoSolicitante);
			}
		}
		
		
		/* Filtrar la ocupación solicitada */
		if(codigoOcupacionSolicitada > -1)
			sb.append(" AND so.ocupacion_solicitada IN(" + ConstantesBD.codigoOcupacionMedicaTodos +"," + codigoOcupacionSolicitada + ")");

		if(codigoEspecialidadSolicitada != null &&(tam = codigoEspecialidadSolicitada.length) > 0)
		{
			int i;
			sb.append("AND((se.codigo IS NOT NULL AND(( so.tipo=" + ConstantesBD.codigoTipoSolicitudInterconsulta +" "+
				" AND se.especialidad IN(" + ConstantesBD.codigoEspecialidadMedicaTodos
			);

			for(i = 0; i < tam; i++)
				sb.append("," + codigoEspecialidadSolicitada[i]);

			sb.append(
				"))OR(so.tipo<>" + ConstantesBD.codigoTipoSolicitudInterconsulta + " AND se.especialidad	>-1))OR(" +
				"se.codigo IS NULL AND so.centro_costo_solicitado=" + ConstantesBD.codigoCentroCostoExternos +
				" AND (getTratanteSolicitud(so.numero_solicitud)=" + codigoCentroCostoTratante +
				"))))"
			);
		}

		/* Filtrar el médico solicitante */
		if(codigoMedico > -1)
			sb.append(" AND so.codigo_medico=" + codigoMedico);

		/* Filtar el paciente */
		if(codigoPaciente > -1)
			sb.append(" AND c.codigo_paciente=" + codigoPaciente);

        
		/* Filtrar el estado de la cuenta */
		if(estadoCuenta > -1)
		{
			sb.append(" AND (c.estado_cuenta=" + estadoCuenta+" or c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaFacturadaParcial);
			if(codigoCuentaAsociada > -1)
				sb.append(" OR c.estado_cuenta=" + ConstantesBD.codigoEstadoCuentaAsociada);
			sb.append(")");	
		}

		/* Filtar el estado de historia clínica */
		if(estadoHistoriaClinica > -1)
		{
			if(estadoHistoriaClinica==ConstantesBD.codigoEstadoHCSolicitada && codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudProcedimiento)
			{
				sb.append(" AND (so.estado_historia_clinica=" + estadoHistoriaClinica +" OR (so.estado_historia_clinica=" + ConstantesBD.codigoEstadoHCRespondida + " AND solicitud_multiple="+ValoresPorDefecto.getValorTrueParaConsultas()+" AND finalizada= "+ValoresPorDefecto.getValorFalseParaConsultas() +") )");
			}
			else
			{
				sb.append(" AND so.estado_historia_clinica=" + estadoHistoriaClinica);
			}
		}

		if(codigoCuenta>0)
		{
			if(codigoCuentaAsociada>0)
			{
				sb.append("AND so.cuenta in("+codigoCuenta+","+codigoCuentaAsociada+")");
			}
			else
			{
				sb.append("AND so.cuenta ="+codigoCuenta);	
			}
		}
		
		if(!resumenAtenciones)
			sb.append(" AND c.estado_cuenta IN("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaAsociada+","+ConstantesBD.codigoEstadoCuentaFacturadaParcial+")");
		
		if(!fechaInicialFiltro.equals("")&&!fechaFinalFiltro.equals(""))
		{
			sb.append(" AND so.fecha_grabacion  between '"+fechaInicialFiltro+"' and '"+fechaFinalFiltro+"'");
		}
		if(!centroCostoSolicitanteFiltro.equals(""))
		{
			sb.append(" AND so.centro_costo_solicitante ='"+centroCostoSolicitanteFiltro+"'");
		}
		if(!UtilidadTexto.isEmpty(areaFiltro))
		{
			sb.append(" AND c.area = '"+areaFiltro+"'");
		}
		if(!UtilidadTexto.isEmpty(centroCostoSolicitadoFiltro))
		{
			sb.append(" AND so.centro_costo_solicitado = '"+centroCostoSolicitadoFiltro+"'");
		}
		if(!UtilidadTexto.isEmpty(pisoFiltro))
		{
			sb.append(" AND hcc.codigopkpiso = '"+pisoFiltro+"'");
		}
		if(!UtilidadTexto.isEmpty(habitacionFiltro))
		{
			sb.append(" AND hcc.codigopkhabitacion = '"+habitacionFiltro+"'");
		}
		if(!UtilidadTexto.isEmpty(camaFiltro))
		{
			sb.append(" AND hcc.codigocama = '"+camaFiltro+"'");
		}
		
		logger.info("-->"+sb.toString());
		/* Obtener el conjunto solución de la búsqueda */
		try {
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator( new PreparedStatementDecorator(con.prepareStatement(sb.toString() ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet)).executeQuery()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	
	private static String cadenaConsultaInterconsultas=""+
							"SELECT so.numero_solicitud AS numeroSolicitud," +
									" so.cobrable AS cobrable,"	+
									" so.consecutivo_ordenes_medicas AS consecutivoOrdenesMedicas," +
									" so.cuenta AS codigoCuenta," +
									" so.urgente AS urgente," + 
									" so.va_epicrisis AS vaEpicrisis," + 
									" so.centro_costo_solicitado AS codigoCentroCostoSolicitado," + 
									" case when so.pyp is null or so.pyp = "+ValoresPorDefecto.getValorFalseParaConsultas()+" then 'false' else 'true' end as pyp, " +
									" TO_CHAR(so.fecha_solicitud,'"+ConstantesBD.formatoFechaBD+"') AS fechaSolicitud," +
									" substr(so.hora_solicitud,0,6) AS horaSolicitud," +
									" TO_CHAR(so.fecha_grabacion,'"+ConstantesBD.formatoFechaBD+"') AS fechaGrabacion," +
									" substr(so.hora_grabacion,0,6) AS horaGrabacion," +
									" CASE WHEN so.fecha_interpretacion IS NULL THEN '' ELSE TO_CHAR(so.fecha_interpretacion,'"+ConstantesBD.formatoFechaBD+"') END AS fechaInterpretacion, " +
									" CASE WHEN so.hora_interpretacion IS NULL THEN '' ELSE substr(so.hora_interpretacion,0,6) END AS horaInterpretacion," +
									" CASE WHEN v.fecha_valoracion IS NULL THEN '' ELSE TO_CHAR(v.fecha_valoracion,'"+ConstantesBD.formatoFechaBD+"') END AS fechaRespuesta," +
									" CASE WHEN v.hora_valoracion IS NULL THEN '' ELSE substr(v.hora_valoracion,0,6) END AS horaRespuesta," +
									" CASE WHEN so.codigo_medico_interpretacion IS NULL THEN -1 ELSE codigo_medico_interpretacion END AS codigoMedicoInterpretacion," +
									" CASE WHEN so.interpretacion IS NULL THEN '' ELSE so.interpretacion END  AS interpretacion," +
									" CASE WHEN so.numero_autorizacion IS NULL THEN '' ELSE so.numero_autorizacion END AS numeroAutorizacion," +
									" getnomcentrocosto(so.centro_costo_solicitado) AS nombreCentroCostoSolicitado," +
									" so.centro_costo_solicitante "		+ "AS codigoCentroCostoSolicitante," +
									" getnomcentrocosto(so.centro_costo_solicitante) AS nombreCentroCostoSolicitante, " +
									" so.especialidad_solicitante AS codigoEspecialidadSolicitante," +
									" getnombreespecialidad(so.especialidad_solicitante) AS nombreEspecialidadSolicitante," +
									" so.estado_historia_clinica AS codigoEstadoHistoriaClinica,"	+
									" getestadosolhis(so.estado_historia_clinica) AS nombreEstadoHistoriaClinica,"	+
									" so.ocupacion_solicitada AS codigoOcupacionSolicitada," +
									" getocupacion(so.ocupacion_solicitada) AS nombreOcupacionSolicitada," +
									" so.tipo AS codigoTiposolicitud,"	+
									" ts.nombre AS nombreTiposolicitud,"	+
									" CASE WHEN so.codigo_medico IS NULL THEN -1 ELSE so.codigo_medico END AS codigoMedicoSolicitante,"	+
									" coalesce(getnombrepersona(so.codigo_medico),'') as nombreMedicoSolicita, "+
									" CASE WHEN so.codigo_medico_responde IS NULL THEN -1 ELSE so.codigo_medico_responde END AS codigoMedicoResponde," +
									" getnombrepersona(so.codigo_medico_responde) as nombreMedicoResponde, "+
									" c.codigo_paciente AS codigoPaciente, " +
									" coalesce(getnombrepersona(c.codigo_paciente),'') as nombrePaciente, "+
									" cc.centro_atencion AS codCentroAtencion," +
									" getnomcentroatencion(cc.centro_atencion) AS nomCentroAtencion, " +
									" CASE WHEN se.especialidad IS NULL THEN -1 ELSE se.especialidad END AS codigoEspecialidadSolicitada," +
									" getnombreespecialidad(se.especialidad) AS nombreEspecialidadSolicitada, " +
									" 'false' AS multiple," +
									" getcamacuenta(c.id,c.via_ingreso) as cama, " +
									" '' as portatil," +
									" NULL as finalizada, " +
									///solo me importa el getBoolean
									" case when 1>0 then "+ValoresPorDefecto.getValorTrueParaConsultas()+" else "+ValoresPorDefecto.getValorFalseParaConsultas()+" end as incluyeServiciosArticulos " +
					"FROM solicitudes so " +
					"INNER JOIN tipos_solicitud ts ON(ts.codigo=so.tipo) " +
					"INNER JOIN cuentas c  ON ( c.id =so.cuenta) " +
					"INNER JOIN ingresos i  ON ( i.id =c.id_ingreso) " +
					"INNER JOIN centros_costo cc  on (c.area=cc.codigo) " +
					"INNER JOIN solicitudes_inter si ON (si.numero_solicitud=so.numero_solicitud) " +
					"INNER JOIN servicios se ON (si.codigo_servicio_solicitado=se.codigo) " +
					"LEFT OUTER JOIN valoraciones v ON (v.numero_solicitud=si.numero_solicitud) "+
					" left outer join his_camas_cuentas hcc on(hcc.cuenta=so.cuenta) " + 
					"WHERE so.tipo = "+ConstantesBD.codigoTipoSolicitudInterconsulta;

	/**
	 * 
	 * @param ac_con
	 * @param ai_codigoCentroCostoSolicitado
	 * @param ai_codigoCentroCostoSolicitante
	 * @param ai_codigoCentroCostoTratante
	 * @param ai_codigoMedico
	 * @param ai_codigoOcupacionSolicitada
	 * @param ai_codigoPaciente
	 * @param ai_codigoTipoSolicitud
	 * @param ai_estadoCuenta
	 * @param ai_estadoHistoriaClinica
	 * @param aia_codigoEspecialidadSolicitada
	 * @param codigoCuentaAsociada
	 * @param codigoCentroCostoIntentaAcceso
	 * @param codigoCuenta
	 * @param institucion
	 * @param resumenAtenciones
	 * @param codigoCentroAtencionCuenta
	 * @param centroCostoSolicitanteFiltro 
	 * @param fechaFinalFiltro 
	 * @param fechaInicialFiltro 
	 * @param j 
	 * @return
	 */
	public static Collection listarSolicitudesInterconsulta(Connection con, 
															int codigoCentroCostoSolicitado, 
															int codigoCentroCostoSolicitante, 
															int codigoCentroCostoTratante, 
															int codigoMedico, 
															int codigoOcupacionSolicitada, 
															int codigoPaciente, 
															int codigoTipoSolicitud, 
															int estadoCuenta, 
															int estadoHistoriaClinica, 
															int[] codigoEspecialidadSolicitada,
															int codigoCuentaAsociada, 
															int codigoCentroCostoIntentaAcceso, 
															int codigoCuenta, 
															int institucion, 
															boolean resumenAtenciones, 
															int codigoCentroAtencionCuenta, 
															String fechaInicialFiltro, 
															String fechaFinalFiltro, 
															String centroCostoSolicitanteFiltro,
															String areaFiltro,
															String centroCostoSolicitadoFiltro,
															String pisoFiltro,
															String habitacionFiltro,
															String camaFiltro,
															String codigoMedicoEnSesion, int Tipo_BD)
	{

		int	tam;
		StringBuffer	sb;
		sb = new StringBuffer(cadenaConsultaInterconsultas);

		/* Filtrar el centro de costo solicitado */
		
		/*
		Se comenta este filtro por cambios en el anexo 777
		Cambios en funcionalidades x excepciones en centros de costo respuesta Shaio
		if(codigoCentroCostoSolicitado > -1)
		{
			sb.append(" AND so.centro_costo_solicitado IN(" +  ConstantesBD.codigoCentroCostoTodos + "," + ConstantesBD.codigoCentroCostoExternos + "," + codigoCentroCostoSolicitado + ")");
		}
		*/
		
		
		if(codigoCentroAtencionCuenta > -1 && !resumenAtenciones)
		{
			sb.append(" AND cc.centro_atencion = "+ codigoCentroAtencionCuenta);
		}
		
		
		/* Filtrar el centro de costo solicitante */
		if(codigoCentroCostoSolicitante > -1)
		{
			
			//Se deben manejar dos grandes casos, cuando queremos ver
			//los que se desean interpretar (estan en estado respondido)
			if (estadoHistoriaClinica==ConstantesBD.codigoEstadoHCRespondida)
			{
				//Dentro del caso de interpretación hay dos subcasos a manejar
				//interconsulta y procedimientos
				if (codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudInterconsulta )
				{
					
					//En el caso de interconsulta solo deben aparecer las solicitudes
					//para las cuales el centro de costo del médico actual sea
					//el tratante
					//sb.append(" AND " + codigoCentroCostoIntentaAcceso +" IN (SELECT CASE WHEN tc.centro_costo="+ConstantesBD.codigoCentroCostoUrgencias+" THEN "+ValoresPorDefecto.getCentroCostoUrgencias(institucion)+" ELSE tc.centro_costo END from tratantes_cuenta tc where tc.solicitud=so.numero_solicitud and tc.cuenta="+codigoCuenta+" and (tc.fecha_inicio<CURRENT_DATE or (tc.fecha_inicio=CURRENT_DATE and tc.hora_inicio<="+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+")) and (tc.fecha_fin IS NULL or tc.fecha_fin>CURRENT_DATE or (tc.fecha_fin=CURRENT_DATE and tc.hora_fin>="+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+") )        ) ");
					switch(Tipo_BD)
					{
					case DaoFactory.ORACLE:
						sb.append(" AND (   " + codigoCentroCostoIntentaAcceso +"=" + codigoCentroCostoSolicitante + "  OR " + codigoCentroCostoIntentaAcceso +" IN (SELECT tc.centro_costo from tratantes_cuenta  tc  where so.numero_solicitud=tc.solicitud and tc.cuenta="+codigoCuenta+" and (tc.fecha_inicio<CURRENT_DATE or"+
						"(tc.fecha_inicio=CURRENT_DATE and tc.hora_inicio<=substr(to_char(sysdate, 'hh:mi'),0,6))) and (tc.fecha_fin IS NULL or tc.fecha_fin>CURRENT_DATE or (tc.fecha_fin=CURRENT_DATE and tc.hora_fin>=substr(to_char(sysdate, 'hh:mi'),0,6)) )  )                    )");
					
						break;
					case DaoFactory.POSTGRESQL:
						sb.append(" AND (   " + codigoCentroCostoIntentaAcceso +"=" + codigoCentroCostoSolicitante + "  OR " + codigoCentroCostoIntentaAcceso +" IN (SELECT tc.centro_costo from tratantes_cuenta  tc  where so.numero_solicitud=tc.solicitud and tc.cuenta="+codigoCuenta+" and (tc.fecha_inicio<CURRENT_DATE or"+
						"(tc.fecha_inicio=CURRENT_DATE and tc.hora_inicio<="+ValoresPorDefecto.getSentenciaHoraActualBD()+")) and (tc.fecha_fin IS NULL or tc.fecha_fin>CURRENT_DATE or (tc.fecha_fin=CURRENT_DATE and tc.hora_fin>="+ValoresPorDefecto.getSentenciaHoraActualBD()+") )  )                    )");
					
						break;
						default:
							break;
					
					}
						


				}
			}
			else
			{
				//Para cuando no es interpretar se sigue manejando la validación
				//previa
				sb.append(" AND so.centro_costo_solicitante=" + codigoCentroCostoSolicitante);
			}
		}
		
		
		/* Filtrar la ocupación solicitada */
		if(codigoOcupacionSolicitada > -1)
			sb.append(" AND so.ocupacion_solicitada IN(" + ConstantesBD.codigoOcupacionMedicaTodos +"," + codigoOcupacionSolicitada + ")");

		/*
			Filtrar la especialidad solicitada. Las especialidades solo deben filtrarse para
			los tipos de solicitud I (Interconsulta)
		*/
		
		if(codigoEspecialidadSolicitada != null && (tam = codigoEspecialidadSolicitada.length) > 0)
		{
			int i;
			sb.append("" +
					"AND" +
						"(" +
							"(" +
								"se.codigo IS NOT NULL " +
								"AND" +
									"(" +
										"( " +
											"so.tipo=" + ConstantesBD.codigoTipoSolicitudInterconsulta +" "+
											" AND se.especialidad IN" +
												"(" + ConstantesBD.codigoEspecialidadMedicaTodos
			);
			
			// Validar especialidades del médico
			if(!codigoMedicoEnSesion.equals("")){
				HashMap<String, Object> especialidadesMedico = new HashMap<String, Object>();
				especialidadesMedico.put("numRegistros", 0);
				especialidadesMedico = Utilidades.obtenerEspecialidadesMedico(con, codigoMedicoEnSesion+"");
				logger.info("Mapa Especialidades Medico - "+especialidadesMedico);
				for(i = 0; i < Utilidades.convertirAEntero(especialidadesMedico.get("numRegistros")+""); i++)
					sb.append("," + especialidadesMedico.get("codigo_"+i));
			}
				
			sb.append(
												")" +//especialidad
										")" +
										"OR" +
										"(" +
											"so.tipo<>" + ConstantesBD.codigoTipoSolicitudInterconsulta + " " +
											"AND se.especialidad	>-1" +
										")" +
									")" +
								"OR" +
								"(" +
									"se.codigo IS NULL " +
									"AND so.centro_costo_solicitado=" + ConstantesBD.codigoCentroCostoExternos +
									" AND " +
										"(getTratanteSolicitud(so.numero_solicitud)=" + codigoCentroCostoTratante +")" +
								")" +
							")" +
						")"
			);
		}

		/* Filtrar el médico solicitante */
		if(codigoMedico > -1)
			sb.append(" AND so.codigo_medico=" + codigoMedico);

		/* Filtar el paciente */
		if(codigoPaciente > -1)
			sb.append(" AND c.codigo_paciente=" + codigoPaciente);

        /* Filtrar el tipo de solicitud */
		if(codigoTipoSolicitud > -1)
		{
			String tipoSolicitud=codigoTipoSolicitud+"";
            sb.append(" AND so.tipo = "+tipoSolicitud+"");
       }

		/* Filtrar el estado de la cuenta */
		if(estadoCuenta > -1)
		{
			sb.append(" AND (c.estado_cuenta=" + estadoCuenta+" or c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaFacturadaParcial);
			if(codigoCuentaAsociada > -1)
				sb.append(" OR c.estado_cuenta=" + ConstantesBD.codigoEstadoCuentaAsociada);
			sb.append(")");	
		}


		/* Filtar el estado de historia clínica */
		if(estadoHistoriaClinica > -1)
		{
			if(estadoHistoriaClinica==ConstantesBD.codigoEstadoHCSolicitada && codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudProcedimiento)
			{
				sb.append(" AND (so.estado_historia_clinica=" + estadoHistoriaClinica +" OR (so.estado_historia_clinica=" + ConstantesBD.codigoEstadoHCRespondida + " AND multiple="+ValoresPorDefecto.getValorTrueParaConsultas()+" AND finalizada= "+ValoresPorDefecto.getValorFalseParaConsultas() +") )");
			}
			else
			{
				sb.append(" AND so.estado_historia_clinica=" + estadoHistoriaClinica);
			}
		}

		if(codigoCuenta>0)
		{
			if(codigoCuentaAsociada>0)
			{
				sb.append(" AND so.cuenta in("+codigoCuenta+","+codigoCuentaAsociada+")");
			}
			else
			{
				sb.append(" AND so.cuenta ="+codigoCuenta);	
			}
		}
		
		if(!resumenAtenciones)
			sb.append(" AND c.estado_cuenta IN("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaAsociada+","+ConstantesBD.codigoEstadoCuentaFacturadaParcial+")");

		
		
		if(!fechaInicialFiltro.equals("")&&!fechaFinalFiltro.equals(""))
		{
			sb.append(" AND to_char(so.fecha_grabacion, 'YYYY-MM-DD') between '"+fechaInicialFiltro+"' and '"+fechaFinalFiltro+"'");
		}
		if(!centroCostoSolicitanteFiltro.equals(""))
		{
			sb.append(" AND so.centro_costo_solicitante ='"+centroCostoSolicitanteFiltro+"'");
		}
		if(!UtilidadTexto.isEmpty(areaFiltro))
		{
			sb.append(" AND c.area = '"+areaFiltro+"'");
		}
		
		/*
		Se comenta este filtro por cambios en el anexo 777
		Cambios en funcionalidades x excepciones en centros de costo respuesta Shaio
		if(!UtilidadTexto.isEmpty(centroCostoSolicitadoFiltro))
		{
			sb.append(" AND so.centro_costo_solicitado = '"+centroCostoSolicitadoFiltro+"'");
		}
		*/
		
		if(!UtilidadTexto.isEmpty(pisoFiltro))
		{
			sb.append(" AND hcc.codigopkpiso = '"+pisoFiltro+"'");
		}
		if(!UtilidadTexto.isEmpty(habitacionFiltro))
		{
			sb.append(" AND hcc.codigopkhabitacion = '"+habitacionFiltro+"'");
		}
		if(!UtilidadTexto.isEmpty(camaFiltro))
		{
			sb.append(" AND hcc.codigocama = '"+camaFiltro+"'");
		}
		//El ingreso tiene que estar abierto y no puede ser de entidad subcontratada
		sb.append(" AND i.estado = '"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' AND i.pac_entidades_subcontratadas IS NULL ");
		
		/* Obtener el conjunto solución de la búsqueda */
		logger.info("\n\nCONUSLTA INTERCONSULTAS -->  \n"+sb);
		try {
			return
				UtilidadBD.resultSet2Collection(new ResultSetDecorator( new PreparedStatementDecorator(con.prepareStatement(sb.toString() ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet)).executeQuery()));
		} catch (SQLException e) {
			logger.info("\n\n\n ERROR :"+e);
			e.printStackTrace();
		}
		return null;
	}
	

	/**
	 * 
	 * @param con
	 * @param solicitud
	 * @return
	 */
	public static int obtenerViaIngresoSolicitud(Connection con, int solicitud) throws BDException
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		int resultado=ConstantesBD.codigoNuncaValido;
		
		try	{
			Log4JManager.info("############## Inicio obtenerViaIngresoSolicitud");
			String cadena="SELECT via_ingreso from cuentas c inner join solicitudes s on(c.id=s.cuenta) where s.numero_solicitud="+solicitud;
			
			pst= con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs=pst.executeQuery();
			if(rs.next()){
				resultado=rs.getInt(1);
			}
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin obtenerViaIngresoSolicitud");
		return resultado;
	}
	
	
	/**
	 * 
	 * @param numero
	 * @param estado
	 * @param con
	 * @return
	 */
	public static boolean  modificarEstadosOrdenesSolicitud(int numeroSolicitud, int estado, Connection con)
	{
		
		boolean retorna= false;
		String consultaStr= "update ordenes.solicitudes set estado_historia_clinica="+estado+" where numero_solicitud ="+numeroSolicitud;
		
		logger.info("*****************************************************************************************");
		logger.info("********************************************-	MODIFCAR ESTADO SOLICITUD  -*********************************************");
		logger.info("Consulta"+consultaStr);
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con, consultaStr);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			retorna=ps.executeUpdate()>0;
			
			ps.close();
			rs.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}	
		return retorna;
	}
	
}

