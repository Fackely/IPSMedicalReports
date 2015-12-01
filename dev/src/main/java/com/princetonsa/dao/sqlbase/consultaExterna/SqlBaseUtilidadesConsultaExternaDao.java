/*
 * Septiembre 16, 2007
 */package com.princetonsa.dao.sqlbase.consultaExterna;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ConstantesValoresPorDefecto;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.consultaExterna.UtilidadesConsultaExterna;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoEspecialidades;
import com.princetonsa.dto.consultaExterna.DtoConsultorios;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;

 /**
  * 
  * @author sgomez
  * Objeto usado para el acceso comï¿½n a la fuente de datos
  * de utilidades propias del mï¿½dulo de CONSULTA EXTERNA
  */
public class SqlBaseUtilidadesConsultaExternaDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseUtilidadesConsultaExternaDao.class);
	
	private static String sqlConsultarSolicitudesSinFacturar = 
		"SELECT distinct tabl.numero_solicit AS numero_solicit " +
			"FROM " +
			"(SELECT " +
			"CASE " +
			"WHEN sc.numero_solicitud  = dfs.solicitud " +
			"AND dfs.factura           = f.codigo " +
			"AND f.estado_facturacion <> 1 " +
			"THEN sc.numero_solicitud " +
			"WHEN dfs.solicitud IS NULL " +
			"THEN sc.numero_solicitud " +
			"ELSE "+ ConstantesBD.codigoNuncaValido +" " +
			"END AS numero_solicit " +
			"FROM servicios_cita sc " +
			"LEFT OUTER JOIN det_factura_solicitud dfs " +
			"ON (sc.numero_solicitud = dfs.solicitud) " +
			"LEFT OUTER JOIN facturas f " +
			"ON (dfs.factura = f.codigo) " +
			"WHERE sc.codigo_cita = ? " +
			") tabl " +
			"WHERE tabl.numero_solicit <> "+ ConstantesBD.codigoNuncaValido;
	
	
	/**
	 * Cadena que verifica si se deben mostrar otros servicios en la respuesta de citas
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean deboMostrarOtrosServiciosCita(Connection con,HashMap campos)
	{
		try
		{
			String unidadConsulta = "";
			String cadenaServicios = "";
			int numServicios = 0;
			
			if(!UtilidadTexto.isEmpty(campos.get("codigoCita")+""))
			{
				//********SE CONSULTAN LOS SERVICIOS Y LA UNIDAD DE AGENDA DE LA CITA************
				String consulta = "SELECT c.unidad_consulta,sc.servicio AS codigo_servicio "+ 
					"FROM cita c "+ 
					"INNER JOIN servicios_cita sc ON(sc.codigo_cita=c.codigo) "+ 
					"WHERE "+ 
					"c.codigo = "+campos.get("codigoCita")+" AND sc.estado = '"+ConstantesIntegridadDominio.acronimoEstadoActivo+"'";
				logger.info(consulta);
				Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
				
				while(rs.next())
				{
					unidadConsulta = rs.getString("unidad_consulta");
					if(!cadenaServicios.equals(""))
						cadenaServicios += ",";
					cadenaServicios += rs.getString("codigo_servicio");
				}
				//********************************************************************************************
				//********SE CONSULTAN EL Nï¿½MERO DE SERVICIOS QUE NO HACEN PARTE DE LA CITA**********
				consulta = "SELECT count(1) As cuenta " +
					"FROM servicios_unidades_consulta " +
					"WHERE unidad_consulta = "+unidadConsulta+" AND codigo_servicio NOT IN ("+cadenaServicios+")";
				
				logger.info(consulta);
				st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				rs =  new ResultSetDecorator(st.executeQuery(consulta));
				
				if(rs.next())
					numServicios = rs.getInt("cuenta");
				//***********************************************************************************************
				
				if(numServicios>0)
					return true;
				else
					return false;
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en deboMostrarOtrosServiciosCita: ",e);
		}
		return false;
	}
	
	/**
	 * Mï¿½todo que consulta los otros servicios que no estan en la cita pero que aplican a su unidad de agenda
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap<String, Object> consultarOtrosServiciosCita(Connection con,HashMap campos)
	{
		HashMap<String,Object> resultados = new HashMap<String, Object>();
		
		//Utilidades.imprimirMapa(campos);
		PreparedStatement pst=null;
		ResultSet rs=null;
		PreparedStatement pst2=null;
		ResultSet rs2=null;
		try
		{
			String unidadConsulta = "";
			String codigoSexoPaciente = "";
			String nombreUnidadConsulta = "";
			String cadenaServicios = "";
			boolean validarSexoPaciente = UtilidadTexto.getBoolean(campos.get("validarSexoPaciente").toString());
			
			//********SE CONSULTAN LOS SERVICIOS Y LA UNIDAD DE AGENDA DE LA CITA************
			String consulta = "SELECT " +
				"c.unidad_consulta, " +
				"p.sexo AS codigo_sexo_paciente, " +
				"getnombreunidadconsulta(c.unidad_consulta) AS nombre_unidad_consulta, " +
				"sc.servicio AS codigo_servicio "+ 
				"FROM cita c " +
				"INNER JOIN personas p ON(p.codigo=c.codigo_paciente) "+ 
				"INNER JOIN servicios_cita sc ON(sc.codigo_cita=c.codigo) "+ 
				"WHERE "+ 
				"c.codigo = "+campos.get("codigoCita")+" AND sc.estado = '"+ConstantesIntegridadDominio.acronimoEstadoActivo+"'";
			
			pst  = con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
			rs =  pst.executeQuery();
			
			while(rs.next())
			{
				unidadConsulta = rs.getString("unidad_consulta");
				codigoSexoPaciente = rs.getString("codigo_sexo_paciente");
				nombreUnidadConsulta = rs.getString("nombre_unidad_consulta");
				if(!cadenaServicios.equals(""))
					cadenaServicios += ",";
				cadenaServicios += rs.getString("codigo_servicio");
			}
			//*****************************************************************************
			
			
			//*****SE CONSULTAN LOS SERVICIOS RESTANTES DE LA UNIDAD DE CONSULTA***********
			consulta = "SELECT " +
				"suc.codigo_servicio AS codigo_servicio," +
				"coalesce(uc.especialidad||'','') As codigo_especialidad, " +
				"gettiposervicio(suc.codigo_servicio) AS codigo_tipo_servicio, " +
				"'(' || getcodigoespecialidad(suc.codigo_servicio) || '-' || suc.codigo_servicio || ') ' || getnombreservicio(suc.codigo_servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombre_servicio " +
				"FROM servicios_unidades_consulta suc " +
				"INNER JOIN unidades_consulta uc on (uc.codigo = suc.unidad_consulta) " +
				"WHERE suc.unidad_consulta = "+unidadConsulta+" AND suc.codigo_servicio not IN ("+cadenaServicios+")";
			
			if(validarSexoPaciente)			
				consulta+= " AND (getcodigosexoservicio(suc.codigo_servicio) = "+ConstantesBD.codigoNuncaValido+" OR getcodigosexoservicio(suc.codigo_servicio) = "+codigoSexoPaciente+") ";
						
			//logger.info("valor de la cadena sql >> "+consulta);
			
			pst2  = con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
			rs2=pst2.executeQuery();
			int cont=0;
			resultados.put("numRegistros","0");
			ResultSetMetaData rsm=rs2.getMetaData();
			while(rs2.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					String alias=rsm.getColumnLabel(i).toLowerCase();
					int index=alias.indexOf("_");
					while(index>0)
					{
						index=alias.indexOf("_");
						try{
							String caracter=alias.charAt(index+1)+"";
							{
								alias=alias.replaceFirst("_"+caracter, caracter.toUpperCase());
							}
						}
						catch(IndexOutOfBoundsException e)
						{
							break;
						}
					}
					resultados.put(alias+"_"+cont, rs2.getObject(rsm.getColumnLabel(i))==null?"":rs2.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			resultados.put("numRegistros", cont+"");
			resultados.put("codigoUnidadAgenda",unidadConsulta);
			resultados.put("nombreUnidadAgenda",nombreUnidadConsulta);
			resultados.put("codigoSexoPaciente",codigoSexoPaciente);
			//******************************************************************************			
		}
		catch(Exception e){
			logger.error("############## ERROR consultarOtrosServiciosCita", e);
		}
		finally{
			try{
				if(rs2 != null){
					rs2.close();
				}
				if(pst2 != null){
					pst2.close();
				}
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return resultados;
	}
	
	/**
	 * Mï¿½todo que consulta los centros de costo X unidad de agenda
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static ArrayList<HashMap<String, Object>> consultarCentrosCostoXUnidadAgenda(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta = "SELECT " +
				"ccuncon.centro_costo AS codigo_centro_costo, " +
				"getnomcentrocosto(ccuncon.centro_costo) AS nombre_centro_costo " +
				"FROM consultaexterna.cen_costo_x_un_consulta ccuncon " +
				"INNER JOIN  administracion.centros_costo cc ON (cc.codigo = ccuncon.centro_costo ) " +
				"WHERE unidad_consulta = "+campos.get("codigoUnidadAgenda")+"   ";
				
			   if(Utilidades.convertirAEntero(campos.get("codigoCentroAtencion")+"")>0)
				{	
					consulta+=" AND cc.centro_atencion = "+campos.get("codigoCentroAtencion")+" ";
				} 
				consulta+=" ORDER BY nombre_centro_costo";
			
			logger.info("Consulta Centros Costo : "+consulta);
			pst  = con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
			rs =  pst.executeQuery();
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo", rs.getObject("codigo_centro_costo"));
				elemento.put("nombre", rs.getObject("nombre_centro_costo"));
				resultados.add(elemento);
			}
		}
		catch(Exception e){
			logger.error("############## ERROR consultarCentrosCostoXUnidadAgenda", e);
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
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return resultados;
	}
	
	/**
	 * Mï¿½todo que consulta los consultorios libres que no se encuentren en los horarios de atencion indicados por el 
	 * dia y la hora inicio y final
	 * @param Connection con
	 * @param String centroAtencion
	 * @param int diaSemana
	 * @param String horaInicio
	 * @param String horaFinal
	 * */
	public static 	HashMap consultarConsultoriosLibresHorarioAtencion(Connection con,
					int centroAtencion,
					int diaSemana,
					String horaInicio,
					String horaFinal,
					String estado)
	{
		
		HashMap mapa = new HashMap();
		String cadena ="";	
		Statement st;
		
		
		//***************Consulta los Consultorios que no se encuentren dentro de horarios de
		//***************Atencion que cumplan con las validaciones
		cadena ="SELECT c.codigo, c.codigo_consultorio || ' ' || c.descripcion AS descripcion " +
				"FROM consultorios c " +
				"WHERE "+ 
				"activo ="+estado + " "+ 
				"AND codigo NOT IN (SELECT ha.consultorio " +
									"FROM horario_atencion ha " +
									"WHERE dia = "+diaSemana+" " +
									"AND ('"+horaInicio+"' BETWEEN hora_inicio AND hora_fin OR "+
									"'"+horaFinal+"' BETWEEN hora_inicio AND hora_fin OR "+
									"hora_inicio BETWEEN '"+horaInicio+"' AND '"+horaFinal+"'"+
									") AND ha.consultorio IS NOT NULL ";
		
		logger.info("centro atencion ------------------------*-----> "+centroAtencion);
		
		if(centroAtencion > ConstantesBD.codigoNuncaValido)
			cadena = cadena + " AND ha.centro_atencion ="+centroAtencion+")  AND centro_atencion ="+centroAtencion;

		cadena +=" ORDER BY c.descripcion ";
		
		try
		{			
			st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(cadena)));			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
				
		return mapa;
	}	
	
	/**
	 * Mï¿½todo implementado para obtener los codigos propietarios de los servicios asociados a una
	 * cita separados por coma
	 * @param con
	 * @param campos
	 * @return
	 */
	public static String obtenerListadoCodigosServiciosCita (Connection con,HashMap campos)
	{
		String listado = "";
		try
		{
			String consulta = 	"SELECT rs.codigo_propietario as listado " +
								"FROM servicios_cita sc " +
								"INNER JOIN referencias_servicio rs ON(rs.servicio=sc.servicio and rs.tipo_tarifario = ?) " +
								"WHERE sc.codigo_cita = ?";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("codigoManual").toString()));
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("codigoCita").toString()));
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
				listado += rs.getString("listado")+", ";
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerListadoServiciosCita: "+e);
		}
		return listado;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param convenio
	 * @return
	 */
	public static  int  consultarCantidadMaximaCitasControlPostOperatorioConvenio(Connection con, int convenio)
	{
		int cant=0;
		
		String cadena ="SELECT citas_max_control as cant from convenios where codigo="+convenio;   
 
		try 
		{
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
		ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
		if(rs.next())
		return rs.getInt(1);
		} 
		catch (Exception e) 
		{
		logger.info("\n problema consultando la cantidad maxima de citas control psotoperatorio  "+e+" [consulta] "+cadena);
		}
		
		return cant;
	}
	
	/**
	 * 
	 * @param con
	 * @param convenio
	 * @return
	 */
	public static int consultarDiasControlPostOperatorioConvenio(Connection con, int convenio)
	{
		int cant=0;
		
		String cadena ="SELECT dias_control_post as cant from convenios where codigo="+convenio;   
 
		try 
		{
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
		ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
		if(rs.next())
		return rs.getInt(1);
		} 
		catch (Exception e) 
		{
		logger.info("\n problema consultando dias control psotoperatorio  "+e+" [consulta] "+cadena);
		}
		
		return cant;	
	}
	
	/**
	 * 
	 * @param con
	 * @param paciente
	 * @return
	 */
	public static HashMap validarControlPostOperatorio(Connection con, int paciente, String fechaCita, int cantCPO, int diasCPO, int especialidad,Diagnostico diagnostico)
	{
		HashMap mapa=new HashMap();
		String cadena ="SELECT " +
			" getnombreespecialidad(scps.especialidad),	" + //Agregado por pruebas Duvian 2008/07/21
			" scps.especialidad as codigoEspecialidadqinterviene," +//Agregado por pruebas Duvian 2008/07/21
			" coalesce(scps.numero_solicitud, "+ConstantesBD.codigoNuncaValido+") as solicitud," +
			" coalesce(scps.codigo, "+ConstantesBD.codigoNuncaValido+") as codigopo	," +
			" getNumConsultasCirugia(scps.codigo) as cuantasCitas	" +//Agregado por pruebas Duvian 2008/07/21
			" FROM ingresos i " +
			" INNER JOIN cuentas c ON (i.id=c.id_ingreso) " +
			" INNER JOIN solicitudes s ON (c.id=s.cuenta AND s.tipo = "+ConstantesBD.codigoTipoSolicitudCirugia+" AND s.estado_historia_clinica IN ("+ConstantesBD.codigoEstadoHCRespondida+","+ConstantesBD.codigoEstadoHCInterpretada+","+ConstantesBD.codigoEstadoHCCargoDirecto+")) " +
			" INNER JOIN solicitudes_cirugia sc ON (s.numero_solicitud=sc.numero_solicitud AND sc.ind_qx IN ('"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia +"', '"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugiaCargoDirecto+"', '"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento+"', '"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruentoCargoDirecto+"') )" ;
		
		
		//Si se ingresa especialidad el filtro se hace por especialidad
		if(especialidad>0)
			cadena += " INNER JOIN sol_cirugia_por_servicio scps ON (scps.numero_solicitud=sc.numero_solicitud AND scps.especialidad = "+especialidad+")" ;
		else
			cadena += " INNER JOIN sol_cirugia_por_servicio scps ON (scps.numero_solicitud=sc.numero_solicitud)" ;
		
		//Se verifica si el filtro se desea hacer por diagnï¿½stico
		if(!diagnostico.getAcronimo().trim().equals("")){
			cadena += " INNER JOIN INFORME_QX_POR_ESPECIALIDAD informe ON(informe.codigo=scps.COD_INFORME_ESPECIALIDAD)";
			cadena += " INNER JOIN diag_post_opera_sol_cx dpo ON(dpo.COD_INFORME_ESPECIALIDAD=informe.codigo AND dpo.diagnostico = '"+diagnostico.getAcronimo()+"' AND dpo.tipo_cie = "+diagnostico.getTipoCIE()+" AND dpo.principal = "+ValoresPorDefecto.getValorTrueParaConsultas()+") ";
		}
			
		
		cadena +=" WHERE " +
			"	i.codigo_paciente="+paciente+" and " +
			"	(to_date('"+UtilidadFecha.conversionFormatoFechaABD(fechaCita)+"','"+ConstantesBD.formatoFechaBD+"')-sc.fecha_final_cx)<"+diasCPO+" and " +
			"	getNumConsultasCirugia(scps.codigo) < "+cantCPO+" " +
			" ORDER BY sc.fecha_final_cx desc";
		
 		logger.info("\n\n\n\n\n	[Sentencia Validacion Control Postoperatorio]	\n\n"+cadena+"\n\n\n\n\n");
		try 
		{
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
		mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (Exception e) 
		{
		logger.info("\n problema consultando dias control psotoperatorio  "+e+" [consulta] "+cadena);
		}
		return mapa;
	}
	
	
	
	/**
	 * Metodo encargado de consultar el codigo
	 * medico de la cita.
	 * @param connection
	 * @param numSol
	 * @return
	 */
	public static String obtenerCodigoMedicoCita (Connection connection,String numSol)
	{
		String cadena ="SELECT coalesce(a.codigo_medico,'-1') as codMedico from servicios_cita sc " +
							" INNER JOIN cita c ON (c.codigo=sc.codigo_cita)" +
							" INNER JOIN agenda a ON (a.codigo=c.codigo_agenda) " +
							" WHERE sc.numero_solicitud=?";
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(numSol));
			
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			return rs.getString(1);
		} 
		catch (Exception e) 
		{
		 logger.info("\n problema consultando el medico de la cita "+e);
		}
		
		return "";
	}
	
	/**
	 * Mï¿½todo implementado para obtener la fecha/hora de la cita
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public static String[] obtenerFechaHoraCita (Connection con,String codigoCita)
	{
		String[] fechaHora = {"","",""};
		try
		{
			String consulta = "SELECT " +
				"to_char(a.fecha,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cita," +
				"substr(hora_inicio,0,6) AS hora_inicio," +
				"substr(hora_fin,0,6) AS hora_fin " +
				"FROM cita c " +
				"INNER JOIN agenda a ON(a.codigo=c.codigo_agenda)  " +
				"WHERE c.codigo = "+codigoCita;
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
			{
				fechaHora[0] = rs.getString("fecha_cita");
				fechaHora[1] = rs.getString("hora_inicio");
				fechaHora[2] = rs.getString("hora_fin");
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerFechaHoraCita: "+e);
		}
		return fechaHora;
	}
	
	/**
	 * Metodo implementado para obtener los centros de atencion validos para el usuario
	 * @param con
	 * @param usuario
	 * @return
	 */
	public static HashMap centrosAtencionValidosXUsuario(Connection con, String usuario, int actividad)
	{
		logger.info("Actividad > "+actividad);
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			String consulta = "SELECT DISTINCT uauc.centro_atencion as codigo, getnomcentroatencion(uauc.centro_atencion) as nombre FROM unid_agenda_usu_caten uauc INNER JOIN unid_agenda_act_auto uaaa ON (uaaa.unid_agenda_usu_caten=uauc.codigo) WHERE uauc.usuario_autorizado=? AND uaaa.actividad_autorizada="+actividad+" order by nombre";
			if (actividad==ConstantesBD.codigoNuncaValido)
				consulta = "SELECT DISTINCT centro_atencion as codigo, getnomcentroatencion(centro_atencion) as nombre FROM unid_agenda_usu_caten WHERE usuario_autorizado=? order by nombre";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setString(1, usuario);
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
			logger.info("\n\n**********************************************");
			logger.info("valor consulta >> "+consulta);
			logger.info("usuario = "+usuario+" | actividad = "+actividad);
		}
		catch(SQLException e)
		{
			logger.error("Error en centrosAtencionValidosXUsuario: "+e);
		}
		String todos="";
		for(int i=0; i<Integer.parseInt(mapa.get("numRegistros").toString()); i++){
			todos += mapa.get("codigo_"+i)+", ";
		}
		todos += ConstantesBD.codigoNuncaValido+"";
		mapa.put("todos", todos);
		Utilidades.imprimirMapa(mapa);
		return mapa;
	}
	
	
	/**
	 * Metodo implementado para obtener los centros de atencion validos para el usuario
	 * @param con
	 * @param usuario
	 * @return ArrayList<HashMap>
	 * @author Vï¿½ctor Gï¿½mez
	 */
	public static ArrayList<HashMap<String,Object>> centrosAtencionValidosXUsuario(Connection con, String usuario, int actividad, String tipoAtencion)
	{
		logger.info("Actividad > "+actividad);
		logger.info("Tipo Atencion > "+tipoAtencion);
		ArrayList<HashMap<String,Object>> array= new ArrayList<HashMap<String,Object>>();
		String consulta = "";
		try
		{
			consulta = "SELECT DISTINCT " +
					"uauc.centro_atencion as codigo, " +
					"getnomcentroatencion(uauc.centro_atencion) as nombre " +
					"FROM consultaexterna.unid_agenda_usu_caten uauc " +
					"INNER JOIN consultaexterna.unid_agenda_act_auto uaaa ON (uaaa.unid_agenda_usu_caten=uauc.codigo) " +
					"INNER JOIN consultaexterna.unidades_consulta uc ON (uc.codigo = uauc.unidad_agenda ) " +
					"WHERE uauc.usuario_autorizado=? " +
					"AND uaaa.actividad_autorizada="+actividad+" " +
					"AND uc.tipo_atencion= ?  order by nombre";
			if (actividad==ConstantesBD.codigoNuncaValido)
				consulta = "SELECT DISTINCT " +
						"uauc.centro_atencion as codigo, " +
						"getnomcentroatencion(uauc.centro_atencion) as nombre " +
						"FROM unid_agenda_usu_caten uauc " +
						"INNER JOIN consultaexterna.unidades_consulta uc ON (uc.codigo = uauc.unidad_agenda ) " +
						"WHERE uauc.usuario_autorizado=? " +
						"AND uc.tipo_atencion= ?  order by nombre";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setString(1, usuario);
			ps.setString(2, tipoAtencion);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				HashMap mapa = new HashMap();
				mapa.put("codigo", rs.getInt("codigo"));
				mapa.put("nombre", rs.getString("nombre"));
				array.add(mapa);
			}
			logger.info("\n\n**********************************************");
			logger.info("valor consulta >> "+consulta);
			logger.info("usuario = "+usuario+" | actividad = "+actividad+" | tipo Atencion= "+tipoAtencion);
		}
		catch(SQLException e)
		{
			logger.error("Error en centrosAtencionValidosXUsuario: "+e);
			logger.info("\n\n**********************************************");
			logger.info("valor consulta >> "+consulta);
			logger.info("usuario = "+usuario+" | actividad = "+actividad+" | tipo Atencion= "+tipoAtencion);
		}
		String todos="";
		if(array.size()>0)
		{
			for(int i=0; i<array.size(); i++){
				todos += array.get(i).get("codigo").toString()+", ";
			}
			todos += ConstantesBD.codigoNuncaValido+"";
			HashMap<String,Object> mapaAux = new HashMap<String,Object>(); 
			mapaAux.put("todos", todos);
			mapaAux.put("nombre", "Todos Autorizados");
			array.add(mapaAux);
		}
		return array;
	}
	
	
	
	
	/**
	 * Metodo implementado para obtener las unidades de agenda validas para el usuario
	 * @param con
	 * @param usuario
	 * @return
	 */
	public static HashMap unidadesAgendaXUsuario(Connection con, String usuario, int centroAtencion, int actividad, String tipoAtencion, String codigosServicios)
	{
		logger.info("Actividad    > "+actividad);
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		String ca = centroAtencion+"";
		if (centroAtencion == ConstantesBD.codigoNuncaValido){
			ca = UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(con, usuario, actividad).get("todos").toString();
		}
		try
		{
			
			String consulta = "SELECT DISTINCT " +
									"uauc.unidad_agenda as codigo, " +
									"getnombreunidadconsulta(uauc.unidad_agenda) as nombre, " +
									"uauc.centro_atencion	as codcentroatencion " +
								" FROM " +
									"unid_agenda_usu_caten uauc ";
						
			// JOIN
			
			if(actividad!=ConstantesBD.codigoNuncaValido)
				consulta += 	"INNER JOIN " +
									"unid_agenda_act_auto uaaa ON (uaaa.unid_agenda_usu_caten=uauc.codigo) ";
			
			if(!UtilidadTexto.isEmpty(codigosServicios))
				consulta += 		"INNER JOIN " +
									"servicios_unidades_consulta suc ON (suc.unidad_consulta=uauc.unidad_agenda) ";
			
			if(!tipoAtencion.equals(""))
				consulta += 	"INNER JOIN " +
									"unidades_consulta uc ON (uauc.unidad_agenda=uc.codigo) ";
			
			// WHERE
			
			consulta +=			"WHERE " +
									"uauc.usuario_autorizado=? " +
									"AND uauc.centro_atencion IN ("+ca+") ";
			
			if(actividad!=ConstantesBD.codigoNuncaValido)
				consulta +=			"AND uaaa.actividad_autorizada="+actividad+" ";
									
			if(!UtilidadTexto.isEmpty(codigosServicios))	
				consulta +=			"AND suc.codigo_servicio IN ("+codigosServicios+") ";
			
			if(!tipoAtencion.equals(""))
				consulta += " AND uc.tipo_atencion='"+tipoAtencion+"' ";
			
			consulta+=			"ORDER BY nombre";
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
			ps.setString(1, usuario);
			logger.info(ps);
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
			logger.info("\n\n**********************************************");
			logger.info("valor consulta >> "+ps);
		}
		catch(SQLException e)
		{
			logger.error("Error en centrosUnidadesAgendaXUsuario: ",e);
		}
		String todos="";
		for(int i=0; i<Integer.parseInt(mapa.get("numRegistros").toString()); i++){
			todos += mapa.get("codigo_"+i)+", ";
		}
		todos += ConstantesBD.codigoNuncaValido+"";
		mapa.put("todos", todos);
		Utilidades.imprimirMapa(mapa);
		return mapa;
	}
	
	
	/**
	 * M&eacute;todo implementado para obtener las unidades de agenda v&aacute;lidas para el usuario.
	 * Tambi&eacute;n se involucra en la consulta, si se envian los c&oacute;digos, los servicios 
	 * que deben estar asociados a las unidades de agenda.
	 * 
	 * @param con
	 * @param usuario
	 * @param centroAtencion
	 * @param actividad
	 * @param tipoAtencion
	 * @param codigosServicios
	 * @return ArrayList<HashMap>
	 * @author Jorge Armando Agudelo
	 */
	public static ArrayList<HashMap> unidadesAgendaXUsuarioArray(Connection con, String usuario, int centroAtencion, int actividad, String tipoAtencion, String codigosServicios)
	{
		logger.info("Actividad    > "+actividad);
		ArrayList<HashMap> array= new ArrayList<HashMap>();
		ArrayList<HashMap<String,Object>> arraytmp= new ArrayList<HashMap<String,Object>>();
		String consulta = "";
		String ca = centroAtencion+"";
		if (centroAtencion == ConstantesBD.codigoNuncaValido){
			arraytmp = UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(con, usuario, actividad,tipoAtencion);
			if(arraytmp.size()>0)
			{
				ca = arraytmp.get(arraytmp.size()-1).get("todos").toString();
			}
			else
			{
				ca = ConstantesBD.codigoNuncaValido+"";
			}
		}
		
		try
		{
			
//			
//			consulta = "SELECT DISTINCT " +
//					"uauc.unidad_agenda as codigo, " +
//					"getnombreunidadconsulta(uauc.unidad_agenda) as nombre " +
//					"FROM consultaexterna.unid_agenda_usu_caten uauc " +
//					"INNER JOIN consultaexterna.unid_agenda_act_auto uaaa ON (uaaa.unid_agenda_usu_caten=uauc.codigo) " +
//					"INNER JOIN consultaexterna.unidades_consulta uc ON (uc.codigo = uauc.unidad_agenda ) " +
//					"WHERE uauc.usuario_autorizado=? " +
//					"AND uaaa.actividad_autorizada="+actividad+" " +
//					"AND uc.tipo_atencion= ? " +
//					"AND uauc.centro_atencion IN ("+ca+") order by nombre";
			
			consulta = "SELECT DISTINCT " +
			"uauc.unidad_agenda as codigo, " +
			"getnombreunidadconsulta(uauc.unidad_agenda) as nombre " +
			"FROM consultaexterna.unid_agenda_usu_caten uauc ";

			if(!UtilidadTexto.isEmpty(codigosServicios))
				consulta += 		"INNER JOIN " +
									"servicios_unidades_consulta suc ON (suc.unidad_consulta=uauc.unidad_agenda) ";
		
			
			if (actividad==ConstantesBD.codigoNuncaValido){
				consulta += "INNER JOIN consultaexterna.unidades_consulta uc ON (uc.codigo = uauc.unidad_agenda ) " +
						"WHERE usuario_autorizado=? " +
						"AND uc.tipo_atencion= ? " +
						"AND uauc.centro_atencion IN ("+ca+") ";
				
			}else{
					
				consulta +="INNER JOIN consultaexterna.unid_agenda_act_auto uaaa ON (uaaa.unid_agenda_usu_caten=uauc.codigo) " +
				"INNER JOIN consultaexterna.unidades_consulta uc ON (uc.codigo = uauc.unidad_agenda ) " +
				"WHERE uauc.usuario_autorizado=? " +
				"AND uaaa.actividad_autorizada="+actividad+" " +
				"AND uc.tipo_atencion= ? " +
				"AND uauc.centro_atencion IN ("+ca+") ";
			}

									
			if(!UtilidadTexto.isEmpty(codigosServicios)){
				
				consulta +=	"AND suc.codigo_servicio IN ("+codigosServicios+") order by nombre";
				
			}else{
				
				consulta+= " order by nombre ";
			}
			
			Log4JManager.info("//////////////////////////////////// *********************************************** " + consulta);
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setString(1, usuario);
			ps.setString(2, tipoAtencion);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				HashMap mapa = new HashMap();
				mapa.put("codigo", rs.getInt("codigo"));
				mapa.put("nombre", rs.getString("nombre"));
				array.add(mapa);
			}
			
			logger.info("\n\n**********************************************");
			logger.info("valor consulta >> "+consulta);
			logger.info("usuario = "+usuario+" | actividad = "+actividad+" | centro atencion = "+centroAtencion+" | tipo atencion= "+tipoAtencion);
		}
		catch(SQLException e)
		{
			logger.error("Error en centrosUnidadesAgendaXUsuario: "+e);
			logger.info("\n\n**********************************************");
			logger.info("valor consulta >> "+consulta);
			logger.info("usuario = "+usuario+" | actividad = "+actividad+" | centro atencion = "+centroAtencion+" | tipo atencion= "+tipoAtencion);
		}
		String todos="";
		
		if(array.size()>0)
		{	
			for(int i=0; i<array.size(); i++){
				todos += array.get(i).get("codigo").toString()+", ";
			}
			todos += ConstantesBD.codigoNuncaValido+"";
			HashMap mapaAux = new HashMap(); 
			mapaAux.put("todos", todos);
			mapaAux.put("nombre", "Todos los Autorizados");
			array.add(mapaAux);
		}
		
		return array;
	}
	
	
	/**
	 * Mï¿½todo para obtener el codigo de la agenda de la cita
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public static String obtenerCodigoAgendaCita(Connection con,String codigoCita)
	{
		String resp = "";
		try
		{
			String consulta = "SELECT codigo_agenda FROM cita WHERE codigo = "+codigoCita;
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				resp = rs.getString("codigo_agenda");
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCodigoAgendaCita: "+e);
		}
		return resp;
	}
	
	/**
	 * Mï¿½todo que verifica si el profesional tiene agenda generada
	 * @param con
	 * @param codigoProfesional
	 * @param tipoBD
	 * @return
	 */
	public static boolean tieneProfesionalAgendaGenerada(Connection con,int codigoProfesional,int tipoBD)
	{
		boolean tiene = false;
		try
		{
			String consultaPostgresql = "SELECT sum(t.cuenta) as total from "+ 
				"( "+ 
					//Agenda que aun no tiene citas asignadas
					"( "+
					"SELECT count(1) as cuenta "+ 
					"FROM agenda "+ 
					"WHERE "+ 
					"codigo_medico = "+codigoProfesional+" and "+ 
					"(fecha > CURRENT_DATE OR (fecha = CURRENT_DATE and "+(tipoBD==DaoFactory.POSTGRESQL?"hora_inicio > current_time":"hora_inicio > "+ValoresPorDefecto.getSentenciaHoraActualBD()+"")+")) and "+ 
					"cupos > 0 and "+ 
					"activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" "+
					") "+ 
					"UNION "+
					//Agenda que ya tien citas asignadas
					"( "+ 
					"SELECT count(1)  as cuenta "+ 
					"FROM agenda a "+ 
					"INNER JOIN cita c ON(c.codigo_agenda=a.codigo) "+ 
					"WHERE "+ 
					"a.codigo_medico = "+codigoProfesional+" and "+ 
					"(a.fecha > CURRENT_DATE OR (a.fecha = CURRENT_DATE and "+(tipoBD==DaoFactory.POSTGRESQL?"a.hora_inicio > current_time":"a.hora_inicio > "+ValoresPorDefecto.getSentenciaHoraActualBD()+"")+")) and "+
					"c.estado_cita in ("+ConstantesBD.codigoEstadoCitaAsignada+","+ConstantesBD.codigoEstadoCitaReservada+","+ConstantesBD.codigoEstadoCitaReprogramada+") "+ 
					") "+
				") t";
			
			String consultaOracle = "SELECT sum(t.cuenta) as total from "+ 
				"( "+ 
					//Agenda que aun no tiene citas asignadas
					"( "+
					"SELECT count(1) as cuenta "+ 
					"FROM agenda "+ 
					"WHERE "+ 
					"codigo_medico = "+codigoProfesional+" and "+ 
					"(fecha >  (SELECT TO_CHAR(sysdate, 'yyyy-mm-dd') FROM dual) OR (fecha =  (SELECT TO_CHAR(sysdate, 'yyyy-mm-dd') FROM dual) and "+(tipoBD==DaoFactory.ORACLE?"hora_inicio >  (SELECT TO_CHAR(sysdate, 'hh:mi:ss') FROM dual)":"hora_inicio > substr( (SELECT TO_CHAR(sysdate, 'hh:mi:ss') FROM dual),0,6)")+")) and "+ 
					"cupos > 0 and "+ 
					"activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" "+
					") "+ 
					"UNION "+
					//Agenda que ya tien citas asignadas
					"( "+ 
					"SELECT count(1)  as cuenta "+ 
					"FROM agenda a "+ 
					"INNER JOIN cita c ON(c.codigo_agenda=a.codigo) "+ 
					"WHERE "+ 
					"a.codigo_medico = "+codigoProfesional+" and "+ 
					"(a.fecha >  (SELECT TO_CHAR(sysdate, 'yyyy-mm-dd') FROM dual) OR (a.fecha =  (SELECT TO_CHAR(sysdate, 'yyyy-mm-dd') FROM dual) and "+(tipoBD==DaoFactory.ORACLE?"a.hora_inicio >  (SELECT TO_CHAR(sysdate, 'hh:mi:ss') FROM dual)":"a.hora_inicio > substr( (SELECT TO_CHAR(sysdate, 'hh:mi:ss') FROM dual),0,6)")+")) and "+
					"c.estado_cita in ("+ConstantesBD.codigoEstadoCitaAsignada+","+ConstantesBD.codigoEstadoCitaReservada+","+ConstantesBD.codigoEstadoCitaReprogramada+") "+ 
					") "+
				") t";
			
			String consulta = "";
			
			switch(tipoBD){
			case DaoFactory.POSTGRESQL:
				consulta = consultaPostgresql;
				break;
			case DaoFactory.ORACLE:
				consulta = consultaOracle;
				break;
			}
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				if(rs.getInt("total")>0)
					tiene = true;
		}
		catch(SQLException e)
		{
			logger.error("Error en tieneProfesionalAgendaGenerada: "+e);
			tiene = false;
		}
		return tiene;
	}
	
	/**
	 * Mï¿½todo que verifica si el paciente tiene citas con estado NO ASISTIï¿½ 
	 * que esten entre los dï¿½as de restricciï¿½n.
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean pacienteConInasistencia(Connection con,HashMap campos)
	{
		boolean inasistencia = false;
		try
		{
			//***************SE TOMAN LOS PARï¿½METROS*****************************
			int codigoPaciente = Integer.parseInt(campos.get("codigoPaciente").toString());
			int numeroDias = Integer.parseInt(campos.get("numeroDias").toString());
			//********************************************************************
			
			String consulta = "SELECT " +
				"count(1) as cuenta " +
				"from cita c " +
				"inner join agenda a ON(a.codigo = c.codigo_agenda) " +
				"WHERE " +
				"c.codigo_paciente = "+codigoPaciente+" and " +
				"c.estado_cita = "+ConstantesBD.codigoEstadoCitaNoCumplida+" and " +
				"(current_date - a.fecha) <= "+numeroDias;
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs =  new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					inasistencia = true;
		}
		catch(SQLException e)
		{
			logger.error("Error en pacienteConInasistencia: "+e);
		}
		
		return inasistencia;
	}
/**
 * Metodo que verifica si una actividad asociada a una unidad de agenda estï¿½ autorizada
 * @param con
 * @param unidadAgenda
 * @param actividad
 * @param usuario
 * @param centroAtencion
 * @param isUniAgenOrAgen Atributo que diferencia en si le mandan la Unidad de Agenda (unidad_consulta) o la Agenda
 * @return
 */
	public static boolean esActividadAurtorizada(Connection con, int unidadAgenda, int actividad, String usuario, int centroAtencion, boolean isUniAgenOrAgen) {
		
		String consulta= "SELECT " +
						 "unidagen.centro_atencion AS centroatencion, " +
						 "unidagen.unidad_agenda AS unidadagenda, " +
						 "unidagen.usuario_autorizado AS usuario, " +
						 "unidagendact.actividad_autorizada AS codigoactividad, " +
						 "actauto.nombre AS nombreactividad " +
						 "FROM consultaexterna.unid_agenda_usu_caten unidagen " +
						 "INNER JOIN consultaexterna.unid_agenda_act_auto unidagendact ON (unidagendact.unid_agenda_usu_caten = unidagen.codigo ) " +
						 "INNER JOIN consultaexterna.activi_autorizadas actauto ON (actauto.codigo = unidagendact.actividad_autorizada) ";
						
					if(isUniAgenOrAgen)//le envia directamente la unidad de agenda (unidad_consutal de la agenda)
						consulta+="WHERE unidagen.unidad_agenda = "+unidadAgenda+" ";
					else
						consulta+="WHERE unidagen.unidad_agenda IN (SELECT agen.unidad_consulta FROM agenda agen WHERE agen.codigo = "+unidadAgenda+" ) "; 
						
					consulta+="AND  unidagen.centro_atencion = "+centroAtencion+" " +
						 "AND unidagen.usuario_autorizado = '"+ usuario+"' AND unidagendact.actividad_autorizada = " + actividad+" ";
					  
		try
		{
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con, consulta);
			logger.info("\nConsulta actividad >> "+ps);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
		    if(rs.next())
		    {
		    	return true;
		    }
		}
		   catch (Exception e) {			
			 e.printStackTrace();
			 logger.info("error en  consultar la Actividad Autorizada >>  cadena >>  "+consulta+" ");
		 }
		
		return false;
	}
	

	/**
	 * Metodo que valida si una actividad asociada a una unidad de agenda un usuario y un centro de atencion está autorizada
	 * o la Cita Tiene un servicio que esté se encuentre entre los Servicios adicionales del Profesional 
	 * @param con
	 * @param codigoCita
	 * @param unidadAgenda
	 * @param actividad
	 * @param usuario
	 * @param centroAtencion
	 * @param isUniAgenOrAgen
	 * @return
	 */
	
public static boolean esActividadAurtorizadaOServAddProf(Connection con,int codigoCita, int unidadAgenda, int actividad, String usuario, int centroAtencion, boolean isUniAgenOrAgen) {
		
		String consulta= "SELECT " +
						 "unidagen.centro_atencion AS centroatencion, " +
						 "unidagen.unidad_agenda AS unidadagenda, " +
						 "unidagen.usuario_autorizado AS usuario, " +
						 "unidagendact.actividad_autorizada AS codigoactividad, " +
						 "actauto.nombre AS nombreactividad " +
						 "FROM consultaexterna.unid_agenda_usu_caten unidagen " +
						 "INNER JOIN consultaexterna.unid_agenda_act_auto unidagendact ON (unidagendact.unid_agenda_usu_caten = unidagen.codigo ) " +
						 "INNER JOIN consultaexterna.activi_autorizadas actauto ON (actauto.codigo = unidagendact.actividad_autorizada) ";
						
					if(isUniAgenOrAgen)//le envia directamente la unidad de agenda (unidad_consutal de la agenda)
						consulta+="WHERE unidagen.unidad_agenda = "+unidadAgenda+" ";
					else
						consulta+="WHERE unidagen.unidad_agenda IN (SELECT agen.unidad_consulta FROM agenda agen WHERE agen.codigo = "+unidadAgenda+" ) "; 
						
					consulta+="AND  unidagen.centro_atencion = "+centroAtencion+" " +
						 "AND unidagen.usuario_autorizado = '"+ usuario+"' AND unidagendact.actividad_autorizada = " + actividad+" "+					 
					 
					  " UNION " +
					 
						 " SELECT "+ ConstantesBD.codigoNuncaValido+" AS centroatencion, "+
						          " "+ConstantesBD.codigoNuncaValido+" AS unidadagenda, "+ 
				                  " us.login AS usuario,"+
				                  " "+actividad+" AS codigoactividad, "+ 
				                  "' ' AS nombreactividad "+
				         "FROM odontologia.serv_adicionales_profesionales seraddprof "+  
				              " INNER JOIN usuarios us ON (us.codigo_persona = seraddprof.codigo_medico) " +  
				         "WHERE seraddprof.codigo_servicio IN (SELECT servicio FROM odontologia.servicios_cita_odontologica WHERE cita_odontologica= ? ) "+
				         "AND us.login = ? ";  	
					
		
		try
		{
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con, consulta);
			ps.setInt(1, codigoCita);
			ps.setString(2, usuario);			
			logger.info("\nConsulta actividad >> "+ps);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
		    if(rs.next())
		    {
		    	return true;
		    }
		}
		   catch (Exception e) {			
			 e.printStackTrace();
			 logger.info("error en  consultar la Actividad Autorizada >>  cadena >>  "+consulta+" ");
		 }
		
		return false;
	}
	
	
	

	/**
	 * Método que verifica las actividades autorizadas para el usuario al listar las citas odontológicas programadas
	 * @param con Comexión con la bD
	 * @param cita Código de la cita
	 * @param actividad Actividad a avalidar
	 * @param usuario Usuario que realiza la consulta
	 * @param centroAtencion Centro de atención del usuario
	 * @return true en caso de tener autorizada la actividad
	 */
	public static boolean esActividadAurtorizadaProgramacionCitaOdo(Connection con, int cita, int actividad, String usuario, int centroAtencion) {
		String consulta= "SELECT " +
						 "unidagen.centro_atencion AS centroatencion, " +
						 "unidagen.unidad_agenda AS unidadagenda, " +
						 "unidagen.usuario_autorizado AS usuario, " +
						 "unidagendact.actividad_autorizada AS codigoactividad, " +
						 "actauto.nombre AS nombreactividad " +
						 "FROM consultaexterna.unid_agenda_usu_caten unidagen " +
						 "INNER JOIN consultaexterna.unid_agenda_act_auto unidagendact ON (unidagendact.unid_agenda_usu_caten = unidagen.codigo ) " +
						 "INNER JOIN consultaexterna.activi_autorizadas actauto ON (actauto.codigo = unidagendact.actividad_autorizada) "+
						 "WHERE unidagen.unidad_agenda IN " +
								"(" +
									"SELECT DISTINCT unidad_consulta FROM consultaexterna.servicios_unidades_consulta where codigo_servicio IN(SELECT servicio FROM odontologia.servicios_cita_odontologica WHERE cita_odontologica=?)" +
								")"+
								/*"AND unidagen.centro_atencion = ? " + Tarea XPlanner 2010: 23773*/
								"AND unidagen.usuario_autorizado = ? AND unidagendact.actividad_autorizada = ? "+ 
		                 
						 "UNION " +
						 
						 " SELECT "+ ConstantesBD.codigoNuncaValido+" AS centroatencion, "+
						          " "+ConstantesBD.codigoNuncaValido+" AS unidadagenda, "+ 
				                  " us.login AS usuario,"+
				                  " "+actividad+" AS codigoactividad, "+ 
				                  "' ' AS nombreactividad "+
				         "FROM odontologia.serv_adicionales_profesionales seraddprof "+  
				              " INNER JOIN usuarios us ON (us.codigo_persona = seraddprof.codigo_medico) " +  
				         "WHERE seraddprof.codigo_servicio IN(SELECT servicio FROM odontologia.servicios_cita_odontologica WHERE cita_odontologica= ? ) "+
				         "AND us.login = ? ";						 				
		PreparedStatementDecorator ps=new PreparedStatementDecorator(con,consulta);
		ResultSetDecorator rs=null;
		try
		{
			ps.setInt(1, cita);
			// ps.setInt(2, centroAtencion); XPlanner 2010: 23773
			ps.setString(2, usuario);
			ps.setInt(3, actividad);
			ps.setInt(4, cita);
			ps.setString(5, usuario);
			
			//Log4JManager.info(ps);
			rs = new ResultSetDecorator(ps.executeQuery());
			boolean respuesta=false;
		    if(rs.next())
		    {
		    	respuesta=true;
		    }
		    return respuesta;
		}
		catch (Exception e) {			
			Log4JManager.error("error en  consultar la Actividad Autorizada >>  cadena >>  "+consulta+" ", e);
		}
		finally
		{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param codigosEstados
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> consultaEstadosCita(
			Connection con, ArrayList<Integer> codigosEstados) {
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String, Object>>();
		try {
			String seccionSELECT = "SELECT codigo AS codigo, "
					+ "nombre AS nombre " + "FROM " + "estados_cita ";

			String seccionWHERE = "WHERE codigo IN (";

			if (!codigosEstados.equals(null)) {
				for (Iterator iterador = codigosEstados.listIterator(); iterador
						.hasNext();) {
					seccionWHERE += iterador.next() + ",";
				}
				seccionWHERE = seccionWHERE.substring(0,
						seccionWHERE.length() - 1)
						+ ")";
				seccionSELECT += seccionWHERE;
			}
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con
					.prepareStatement(seccionSELECT, ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery()); 
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo", rs.getObject("codigo"));
				elemento.put("nombre", rs.getObject("nombre"));
				resultados.add(elemento);
			}
		} catch (Exception e) {
			logger.error("Error en obtenerProfesionales: "+e);
			e.printStackTrace();
		}
		return resultados;
	}
	/**
	 * Metodo que retorna los motivos de Anulacion y/o Condonacion de Multas que estan activos en una institucion
	 * @param con
	 * @param codInstitucion
	 * @return
	 */
	public static HashMap consultaMotivosAnulacionCondonacionMulta(Connection con, int codInstitucion)
	{
		String consulta="SELECT " +
						"consecutivo AS consecutivo,  " +
						"codigo AS codigo, " +
						"institucion AS institucion, " +
						"descripcion AS descripcion, " +
						"activo AS activo " +
						"FROM " +
						"mot_anu_cond_multas " +
						"WHERE institucion = "+codInstitucion+"  AND activo = '"+ ConstantesBD.acronimoSi+"' ";
		
		logger.info("\n \n CADENA Motivos Anulacion Condonacion >>  "+consulta+" ");
		try
		{
		PreparedStatementDecorator ps=new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
		
		return UtilidadBD.cargarValueObject(rs, true, false); 
		
		}
		   catch (Exception e) {			
			 e.printStackTrace();
			 logger.info("error en  consultar los Motivos de Anulacion y Condonacion de Multas >>  cadena >>  "+consulta+" ");
		 }
		
		return null;
		
	}
	
	
	/**
	 * Verifica si la Institucion Maneja Multas poerImcumplimiento de Citas Segï¿½n la Institucion y el Modulo
	 * que para este caso es Consulta Externa
	 * @param con
	 * @param HashMap parametors
	 * @return ArrayList<HashMap<String, Object>> resultados
	 * 
	 */
	public static ArrayList<HashMap<String, Object>> institucionManejaMultasIncumCitas(Connection con, HashMap parametros)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String, Object>>();
		String consulta = "", parametrosModulo = "";
		String[] paraModulos ;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try{
			consulta = "SELECT " +
					"vpdm.parametro AS parametro, " +
					"vpd.valor as valor, " +
					"vpd.nombre as nombre " +
					"FROM valores_por_defecto_modulos vpdm " +
					"INNER JOIN valores_por_defecto vpd ON (vpd.parametro = vpdm.parametro AND vpd.institucion = ? ) " +
					"WHERE vpdm.modulo = ? ";
					
			if(parametros.containsKey("parametrosModulo")){
				parametrosModulo = " AND vpdm.parametro IN (";
				paraModulos = (String[]) parametros.get("parametrosModulo");
				for(int i=0;i<paraModulos.length ;i++){
					if(i!=(paraModulos.length-1))
						parametrosModulo += "'"+paraModulos[i].toString()+"',";
					else
						parametrosModulo += "'"+paraModulos[i].toString()+"'";
				}
				parametrosModulo += ")";
			}
			consulta += parametrosModulo;
			pst = con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			pst.setInt(2, Utilidades.convertirAEntero(parametros.get("modulo").toString()));
			logger.info("La Consulta >>>>>>>>>>> "+consulta);
			logger.info("cod_institucion >>>>>>>>>>> "+parametros.get("institucion").toString());
			logger.info("La Consulta >>>>>>>>>>> "+parametros.get("modulo").toString());
			rs= pst.executeQuery(); 
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("parametro", rs.getObject("parametro"));
				elemento.put("valor", rs.getObject("valor"));
				elemento.put("nombre", rs.getObject("nombre"));
				resultados.add(elemento);
			}
		}catch(Exception e){
			logger.error("############## ERROR institucionManejaMultasIncumCitas", e);
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
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return resultados;
	}
	
	/**
	 * Verifica el estado de las citas del paciente
	 * @param con
	 * @param HashMap parametors
	 * @return ArrayList<HashMap<String, Object>> resultados
	 */
	@SuppressWarnings("rawtypes")
	public static ArrayList<HashMap<String, Object>> estadoCitasPaciente(Connection con, HashMap parametros)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String, Object>>();
		String consulta = "";
		PreparedStatement pst=null;
		ResultSet rs=null;
		try{
			consulta = "SELECT " +
							"infomul.codigo AS codigo," +
							"infomul.codigo_paciente AS codigo_paciente, " +
							"infomul.codigo_agenda AS codigo_agenda, " +
							"infomul.nom_paciente AS nom_paciente, " +
							"infomul.estado_cita, " +
							"infomul.nombre AS nom_estado_cita, " +
							"infomul.servicio AS servicio, " +
							"infomul.solicitud AS solicitud, " +
							"infomul.tienemulta AS tienemulta, " +
							"infomul.nom_agenda AS nom_agenda, " +
							"to_char(infomul.fecha, '"+ConstantesBD.formatoFechaBD+"') AS fecha, " +
							"infomul.hora_inicio AS hora_inicio, " +
							"infomul.hora_fin AS hora_fin, " +
							"infomul.descripcion AS centro_atencion, ";
					
			if(parametros.get("fitralconvenio").equals(ConstantesBD.acronimoSi))
					consulta +="con.codigo AS cod_convenio, " +
					"CASE WHEN con.codigo IS NULL THEN '"+ConstantesBD.acronimoNo+"' ELSE '"+ConstantesBD.acronimoSi+"' END AS man_multas_con, ";
				
					consulta += "infomul.nom_profesional " +
					"FROM (SELECT c.codigo, c.codigo_paciente, c.codigo_agenda, getnombrepersona(c.codigo_paciente) as nom_paciente, c.estado_cita, " +
					"estc.nombre, serc.servicio, coalesce(serc.numero_solicitud,-1) as solicitud, " +
					"CASE WHEN mc.consecutivo IS NULL THEN '"+ConstantesBD.acronimoNo+"' ELSE '"+ConstantesBD.acronimoSi+"' END AS tienemulta, " +
					"unic.descripcion AS nom_agenda, " +
					"agen.fecha, agen.hora_inicio, agen.hora_fin, caten.descripcion, " +
					"getnombrepersona(agen.codigo_medico) as nom_profesional " +
					"FROM cita c  " +
					"INNER JOIN estados_cita estc ON (estc.codigo = c.estado_cita ) " +
					"INNER JOIN servicios_cita serc ON (serc.codigo_cita = c.codigo) " +
					// Multas en estado Generado o Facturado
					"LEFT JOIN consultaexterna.multas_citas mc ON (mc.cita = c.codigo ) " +
					"LEFT JOIN agenda agen ON (agen.codigo = c.codigo_agenda) " +
					"LEFT JOIN centro_atencion caten ON (caten.consecutivo = agen.centro_atencion) " +
					"LEFT JOIN unidades_consulta unic ON ( unic.codigo = agen.unidad_consulta) " +
					// citas en estado Paciente No Asistio
					"WHERE c.estado_cita = "+ConstantesBD.codigoEstadoCitaNoCumplida+" " +
					"AND c.codigo_paciente = ? " +
					// que la fecha de generacion de la cita sea mayor o  igual a la fecha de inicial de control del multas
					// de l ainstitucion que maneja multas por incumplimiento de citas
					"AND to_char(c.fecha_gen, '"+ConstantesBD.formatoFechaBD+"') >= ? " +
					//Filtro de los estados de la multa
					"AND (mc.estado IN('"+ConstantesIntegridadDominio.acronimoEstadoGenerado+"','"+ConstantesIntegridadDominio.acronimoEstadoFacturado+"') or mc.cita is null) "+
					//no funciona en oracle//"order by c.codigo_paciente " +
					") infomul ";
					
			
			if(parametros.get("fitralconvenio").equals(ConstantesBD.acronimoSi))
					consulta += "LEFT JOIN convenios con ON (con.man_mul_inc_citas = '"+ConstantesBD.acronimoSi+"' " +
					"AND con.codigo IN (SELECT det.convenio FROM det_cargos det WHERE det.solicitud = infomul.solicitud)) ";
			
			pst = con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, Utilidades.convertirAEntero(parametros.get("codPaciente").toString()));
			pst.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaIniControl").toString())));
			rs= pst.executeQuery(); 
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codCita", rs.getObject("codigo"));
				elemento.put("codPaciente", rs.getObject("codigo_paciente"));
				elemento.put("codAgenda", rs.getObject("codigo_agenda"));
				elemento.put("nombrePaciente", rs.getObject("nom_paciente"));
				elemento.put("codEstadoCita", rs.getObject("estado_cita"));
				elemento.put("codNombreEstadoCita", rs.getObject("nom_estado_cita"));
				elemento.put("codServicioCita", rs.getObject("servicio"));
				elemento.put("SolicitudSerCita", rs.getObject("solicitud"));
				elemento.put("tieneMulta", rs.getObject("tienemulta"));
				elemento.put("nombreAgenda", rs.getObject("nom_agenda"));
				elemento.put("fechaCita", rs.getObject("fecha"));
				elemento.put("horaInicioCita", rs.getObject("hora_inicio"));
				elemento.put("horaFinalCita", rs.getObject("hora_fin"));
				elemento.put("centroAtencion", rs.getObject("centro_atencion"));
				elemento.put("nombreProfesional", rs.getObject("nom_profesional"));
				if(parametros.get("fitralconvenio").equals(ConstantesBD.acronimoSi)){
					elemento.put("codConvenio", rs.getObject("cod_convenio"));
					elemento.put("convenioManejaMultIncCita", rs.getObject("man_multas_con"));
				}
				resultados.add(elemento);
			}
		}catch(Exception e){
			logger.error("############## ERROR estadoCitasPaciente", e);
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
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return resultados;
	}

	public static HashMap UnidadesAgendaXcentrosAtencion(Connection con,
			int conscutivoCentroAtencion, String tipoAtencion, Boolean activas) {
		
		String consulta="";
		
		if(activas==null){
			consulta=		"SELECT Distinct uc.descripcion as descripcion ,"+
							" uc.codigo as codigo "+
							"FROM consultaexterna.cen_costo_x_un_consulta cxu "+
						    "INNER JOIN consultaexterna.unidades_consulta uc "+
						    "ON(uc.codigo=cxu.unidad_consulta) "+
						    "inner join     administracion.centros_costo cc "+
						    "on(cc.codigo=cxu.centro_costo) "+
						    "inner join administracion.centro_atencion ca "+
						    "on(ca.consecutivo=cc.centro_atencion) where ca.consecutivo= ?";
		}
		else
		{
			consulta=		"SELECT Distinct uc.descripcion as descripcion ,"+
			" uc.codigo as codigo "+
			"FROM consultaexterna.cen_costo_x_un_consulta cxu "+
		    "INNER JOIN consultaexterna.unidades_consulta uc "+
		    "ON(uc.codigo=cxu.unidad_consulta) "+
		    "inner join     administracion.centros_costo cc "+
		    "on(cc.codigo=cxu.centro_costo) "+
		    "inner join administracion.centro_atencion ca "+
		    "on(ca.consecutivo=cc.centro_atencion) where ca.consecutivo= ?" +
		    "AND uc.activa=?";
		}
		
		if(!tipoAtencion.equals(""))
		{
			consulta += " and uc.tipo_atencion = '"+tipoAtencion+"' ";
		}
		
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		
		PreparedStatementDecorator ps;
		
		try{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, conscutivoCentroAtencion);
			if(activas!=null)
			{
				ps.setBoolean(2, activas);
			}
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			
			logger.info(ps);
		} 
		catch(SQLException e)
		{
			logger.info("\n\n ERROR. CONSULTANDO UNIDADES DE AGENDA X CENTROS DE ATENCIONSQL------>>>>>> "+e);
			
			e.printStackTrace();
		}
		return resultados;	
		
		
	}
	
	
	

	
	
	
	
	
	
	
	public static ArrayList<DtoConsultorios> consultoriosXcentrosAtencion(
			Connection con, int conscutivoCentroAtencion,String sentencia) {
		
		
		PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
		try {
			psd.setInt(1, conscutivoCentroAtencion);
			
		
			
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			
			ArrayList<DtoConsultorios> resultado=new ArrayList<DtoConsultorios>();
			
			while(rsd.next())
			{
				DtoConsultorios dto=new DtoConsultorios();
				dto.setCodigo(rsd.getInt("codigo"));
				dto.setDescripcion(rsd.getString("descripcion"));
				resultado.add(dto);
			}
			
			UtilidadBD.cerrarObjetosPersistencia(psd, rsd, null);
			return resultado;
		} catch (SQLException e) {
			logger.info(psd);
			logger.error("Error consultando la BD ", e);
		}
		return null;
		
		
	}
	
	
	
	
	
	/**
	 * Metodo que realiza la validacion de los parametros generales 
	 * Institucion Maneja Multas por Incumplimiento de Citas
	 * Bloquear atencion de Citas por Incumplimiento de Citas
	 * Fecha  Inicio control de Multas por Incumplimiento de Citas
	 * @param con
	 * @param usuario
	 * @return un mapa con citasincumplidas, fechacontrol, errores
	 */
	public static HashMap validacionesBloqueoAtencionCitas(Connection con, UsuarioBasico usuario , int codigoPaciente)
	{
	    ArrayList<HashMap<String, Object>> parametrosGenerales=new ArrayList<HashMap<String, Object>>() ;
	    ArrayList<HashMap<String, Object>> citasIncumplidas = new ArrayList<HashMap<String, Object>>();
	    String [] parametrosModulo = {ConstantesValoresPorDefecto.nombreInstitucionManejaMultasPorIncumplimiento, ConstantesValoresPorDefecto.nombreFechaInicioControlMultasIncumplimientoCitas, ConstantesValoresPorDefecto.nombreBloqueaAtencionCitasPorIncump};
	    boolean manejaIncumplimientoCitas=false; 
	    boolean bloqueoAtencionCitasXIncumplimiento= false;
	    String fechaInicioControlMultasXIncumplimiento=new String("");
	    HashMap respuesta=new HashMap();
	    respuesta.put("citasincumplidas","");
	    respuesta.put("fechacontrol","");
	    respuesta.put("errores","");
	    
	    ActionErrors errores = new ActionErrors(); 
	    
	    parametrosGenerales = UtilidadesConsultaExterna.institucionManejaMultasIncumCitas(con, usuario.getCodigoInstitucion(), ConstantesBD.codigoModuloConsultaExterna, parametrosModulo);
		for(int i=0;i < parametrosGenerales.size();i++)
		{
			logger.info("parametro -->"+parametrosGenerales.get(i).get("parametro"));
			logger.info("valor -->"+parametrosGenerales.get(i).get("valor"));
	    	if((parametrosGenerales.get(i).get("parametro")+"").equals(ConstantesValoresPorDefecto.nombreInstitucionManejaMultasPorIncumplimiento)   &&  parametrosGenerales.get(i).get("valor").toString().equals(ConstantesBD.acronimoSi))
	          {
	    	    logger.info("\nManeja Incumplimiento = "+ manejaIncumplimientoCitas);  
	    		manejaIncumplimientoCitas=true;
	          }
	          
	          if((parametrosGenerales.get(i).get("parametro")+"").equals(ConstantesValoresPorDefecto.nombreBloqueaAtencionCitasPorIncump)   &&  parametrosGenerales.get(i).get("valor").toString().equals(ConstantesBD.acronimoSi))
	          {
	        	  logger.info("\nBloqueo Incumplimiento = "+ bloqueoAtencionCitasXIncumplimiento);
	        	  bloqueoAtencionCitasXIncumplimiento=true;
	          }
	          
	          if((parametrosGenerales.get(i).get("parametro")+"").equals(ConstantesValoresPorDefecto.nombreFechaInicioControlMultasIncumplimientoCitas)   &&  !parametrosGenerales.get(i).get("valor").toString().equals(""))
	          {
	    	      fechaInicioControlMultasXIncumplimiento=parametrosGenerales.get(i).get("valor").toString();
	          }
	    }
	    
	    if(manejaIncumplimientoCitas && bloqueoAtencionCitasXIncumplimiento)
		  { 
			 	
			  if(fechaInicioControlMultasXIncumplimiento.equals(""))
			   { 
			    errores.add("",new ActionMessage("errors.notEspecific","No se encuentra parametrizada la Fecha Inicio Control de Multas por Incumplimiento de Citas"));
			   }
			    else
			     {
			     logger.info(".............------------>"+fechaInicioControlMultasXIncumplimiento);
			     citasIncumplidas=UtilidadesConsultaExterna.estadoCitasPaciente(con, codigoPaciente, fechaInicioControlMultasXIncumplimiento, false);				    
			        
				     if(citasIncumplidas.size()>0)
				     {   
				    	 errores.add("",new ActionMessage("errors.notEspecific","Paciente con Multas pendientes por Concepto de Citas"));
				     }
			     }
	           
		  }  
	      respuesta.put("errores", errores);
	      respuesta.put("citasincumplidas",citasIncumplidas);
	      respuesta.put("fechacontrol",fechaInicioControlMultasXIncumplimiento);
	    
	    return respuesta;
	}
	
	public static ArrayList<Integer> consultarSolicitudesSinFacturar (Connection con, int codigoCita)
	{
//		sqlConsultarSolicitudesSinFacturar
		ArrayList<Integer> solicitudes = new ArrayList<Integer>();
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(sqlConsultarSolicitudesSinFacturar, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoCita);
			
			logger.info("La Consulta >>>>>>>>>>> "+sqlConsultarSolicitudesSinFacturar);
			logger.info("cod_institucion >>>>>>>>>>> "+codigoCita);
			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery()); 
			
			while(rs.next())
			{
				solicitudes.add(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return solicitudes;
	}
	
	/**
	 * consulta los consutorios 
	 * @param con
	 * @param institucion
	 * @param centroAtencion
	 * @return
	 */
	public static ArrayList<HashMap> consultoriosCentroAtencionTipo(Connection con, int institucion, int centroAtencion)
	{
		ArrayList<HashMap> array = new ArrayList<HashMap>();
		String consulta = "";
		try{
			consulta = "SELECT " +
				"c.codigo AS codigo, " +
				"c.descripcion AS descripcion " +
				"FROM consultaexterna.consultorios c " + 
				"INNER JOIN administracion.centro_atencion ca ON (c.centro_atencion=ca.consecutivo) " +
				"WHERE c.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" "+
				"AND ca.cod_institucion="+institucion+" ";
			if(centroAtencion!=ConstantesBD.codigoNuncaValido)
			{
				consulta+=" AND c.centro_atencion="+centroAtencion+" ";
			}
			consulta += " ORDER BY descripcion ";
			
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				HashMap mapa = new HashMap();
				mapa.put("codigo", rs.getInt("codigo"));
				mapa.put("descripcion", rs.getString("descripcion"));
				array.add(mapa);
			}
			logger.info("La Consulta >>>>>>>>>>> "+sqlConsultarSolicitudesSinFacturar);
			logger.info("cod_institucion: "+institucion+" centro atencion: "+centroAtencion);
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Error en la Consulta >>>>>>>>>>> "+sqlConsultarSolicitudesSinFacturar);
			logger.info("cod_institucion: "+institucion+" centro atencion: "+centroAtencion);
		}
		return array;
	}
	
	
	/**
	 * Mï¿½todo para obtener el codigo medico de la agenda
	 * @param con
	 * @param codigoAgenda
	 * @return
	 */
	public static int obtenerCodigoMedicoAgenda(Connection con,int codigoAgenda)
	{
		int codigoMedico = ConstantesBD.codigoNuncaValido;
		try
		{
			String consulta = " SELECT coalesce(codigo_medico,"+ConstantesBD.codigoNuncaValido+") as codigo_medico from agenda where codigo = ?";
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
			pst.setInt(1,codigoAgenda);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				codigoMedico = rs.getInt("codigo_medico");
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCodigoMedicoAgenda: "+e);
		}
		return codigoMedico;
		
	}
	
	/**
	 * metodo que obtiene las actividades autorizadas por una unidad de agenda
	 * @param con
	 * @param centroAtencion
	 * @param unidadAgenda
	 * @param usuario
	 * @param institucion
	 * @return ArrayList<HashMap>
	 * @author Vï¿½ctor Hugo Gï¿½mez L.
	 */
	public static ArrayList<HashMap> actividadesAutorizadasXUniAgend(Connection con, int centroAtencion, int unidadAgenda, String usuario, int institucion)
	{
		ArrayList<HashMap> array = new ArrayList<HashMap>();
		String consulta = "";
		try
		{
			consulta = "SELECT " + 
				"aa.codigo as codigo_activi, " + 
				"aa.nombre as nom_activi " +
				"FROM consultaexterna.unid_agenda_usu_caten uauc " + 
				"INNER JOIN consultaexterna.unid_agenda_act_auto uaaa ON (uaaa.unid_agenda_usu_caten = uauc.codigo ) " + 
				"INNER JOIN consultaexterna.activi_autorizadas aa ON (aa.codigo = uaaa.actividad_autorizada )  " +
				"WHERE uauc.centro_atencion = ?  " +
				"AND uauc.unidad_agenda = ?  " +
				"AND uauc.usuario_autorizado = ? " + 
				"AND uauc.institucion = ? ";
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setInt(1, centroAtencion);
			pst.setInt(2, unidadAgenda);
			pst.setString(3, usuario);
			pst.setInt(4, institucion);
			logger.info("Consulta: "+pst);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				HashMap mapa = new HashMap();
				mapa.put("codigo", rs.getInt("codigo_activi"));
				mapa.put("actividad", rs.getString("nom_activi"));
				array.add(mapa);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en actividades autorizada x unidad de agenda: "+e);
			logger.info("ERROR Consulta: "+consulta);
		}
		return array;
	}
	
	/**
	 * obtener los motivos cancelacion de un cita
	 * @param con
	 * @param activo
	 * @param tipoCancelacion
	 * @return
	 */
	public static ArrayList<HashMap> obtenerMotivosCancelacion(Connection con, String activo, String tipoCancelacion)
	{
		ArrayList<HashMap> array = new ArrayList<HashMap>();
		String consulta = "";
		String where = " WHERE 1=1 ";
		try
		{
			consulta = "SELECT " +
				"codigo as cod_mot_can, " +
				"descripcion as desp_mot_can, " +
				"tipo_cancelacion as tip_cancelacion  " +
				"FROM consultaexterna.motivos_cancelacion_cita ";
			if(activo!=null && !activo.equals("") )
				where+=" AND activo = '"+activo+"' ";
			if(tipoCancelacion!=null && !tipoCancelacion.equals(""))
				where+=" AND tipo_cancelacion IN ("+tipoCancelacion+") ";
			consulta+=where+" ORDER BY desp_mot_can ASC ";
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			logger.info("Consulta ****************************: "+pst);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				HashMap mapa = new HashMap();
				mapa.put("codigo", rs.getInt("cod_mot_can"));
				mapa.put("descripcion", rs.getString("desp_mot_can"));
				mapa.put("tipo_cancelacion", rs.getInt("tip_cancelacion"));
				array.add(mapa);
			}
		}catch(SQLException e)
		{
			logger.error("Error en actividades autorizada x unidad de agenda: "+e);
			logger.info("ERROR Consulta: "+consulta);
		}
		return array;
	}
	
	/**
	 * Mï¿½todo implementado para verificar 
	 * @param con
	 * @param usuario
	 * @param codigoUnidadAgenda
	 * @return
	 */
	public static boolean validarCentrosCostoUsuarioEnUnidadAgenda(Connection con,UsuarioBasico usuario, int codigoUnidadAgenda)
	{
		boolean validacion = false;
		try
		{
			//Se arma el listado de centros de costo
			String centrosCosto = "";
			for(int i=0;i<usuario.getNumCentrosCosto();i++)
			{
				centrosCosto += (centrosCosto.equals("")?"":",") + usuario.getCentrosCosto("codigo_"+i);
			}
			
			String consulta = "SELECT " +
				"count(1) as cuenta " +
				"from consultaexterna.cen_costo_x_un_consulta " +
				"WHERE unidad_consulta = ? and centro_costo in ("+centrosCosto+") ";
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setInt(1,codigoUnidadAgenda);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				if(rs.getInt("cuenta")>0)
				{
					validacion = true;
				}
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en validarCentrosCostoUsuarioEnUnidadAgenda :",e);
		}
		return validacion;
	}
	
	/**
	 * Mï¿½todo para obtener los paises permitidos para un usuario segï¿½n la parametrizacion de la funcinalidad
	 * unidade sde agenda por susuario por centro de atencion
	 * @param con
	 * @param loginUsuario
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerPaisesPermitidosXUsuarioXUnidadAgendaXCentroAtencion(Connection con,String loginUsuario,String tipoAtencion)
	{
		ArrayList<HashMap<String, Object>> paises = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "SELECT distinct "+
				"ca.pais as codigo_pais, "+
				"administracion.getdescripcionpais(ca.pais) as nombre_pais "+ 
				"from consultaexterna.unid_agenda_usu_caten uauc " +
				"INNER JOIN consultaexterna.unidades_consulta uc ON (uc.codigo = uauc.unidad_agenda ) "+ 
				"INNER JOIN consultaexterna.unid_agenda_act_auto uaaa ON (uaaa.unid_agenda_usu_caten = uauc.codigo ) "+
				"INNER JOIN administracion.centro_atencion ca ON(ca.consecutivo = uauc.centro_atencion) "+ 
				"WHERE uauc.usuario_autorizado = ? and uc.tipo_atencion = ? order by nombre_pais";
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setString(1,loginUsuario);
			pst.setString(2,tipoAtencion);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getString("codigo_pais"));
				elemento.put("nombre",rs.getString("nombre_pais"));
				paises.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerPaisesPermitidosXUsuarioXUnidadAgendaXCentroAtencion: ",e);
		}
		return paises;
	}
	
	/**
	 * Mï¿½todo implementado para cargar las ciudades permitidas para un usuario segï¿½n la parametrizacion
	 * de unidades de agenda x usuario x centro de atencion
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerCiudadesPermitidasXUsuarioXUnidadAgendaXCentroAtencion(Connection con,HashMap parametros)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		try
		{
			//************SE TOMAN LOS PARï¿½METROS************************
			String codigoPais = parametros.get("codigoPais").toString();
			String loginUsuario = parametros.get("loginUsuario").toString();
			String tipoAtencion = parametros.get("tipoAtencion").toString();
			//***********************************************************
			
			String consulta = "SELECT distinct "+ 
				"ca.pais as codigo_pais, "+
				"ca.departamento as codigo_depto, "+
				"ca.ciudad as codigo_ciudad, "+
				"administracion.getdescripcionpais(ca.pais) as nombre_pais, "+ 
				"administracion.getnombredepto(ca.pais,ca.departamento) as nombre_depto, "+
				"administracion.getnombreciudad(ca.pais,ca.departamento,ca.ciudad) as nombre_ciudad "+ 
				"from consultaexterna.unid_agenda_usu_caten uauc " +
				"INNER JOIN consultaexterna.unidades_consulta uc ON (uc.codigo = uauc.unidad_agenda ) "+ 
				"INNER JOIN consultaexterna.unid_agenda_act_auto uaaa ON (uaaa.unid_agenda_usu_caten = uauc.codigo ) "+
				"INNER JOIN administracion.centro_atencion ca ON(ca.consecutivo = uauc.centro_atencion) "+ 
				"WHERE uauc.usuario_autorizado = ? and uc.tipo_atencion = ?  and ca.pais = ? order by nombre_ciudad";
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setString(1,loginUsuario);
			pst.setString(2,tipoAtencion);
			pst.setString(3,codigoPais);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigoPais", rs.getObject("codigo_pais"));
				elemento.put("nombrePais", rs.getObject("nombre_pais"));
				elemento.put("codigoDepto", rs.getObject("codigo_depto"));
				elemento.put("nombreDepto", rs.getObject("nombre_depto"));
				elemento.put("codigoCiudad", rs.getObject("codigo_ciudad"));
				elemento.put("nombreCiudad", rs.getObject("nombre_ciudad"));
				
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCiudadesPermitidasXUsuarioXUnidadAgendaXCentroAtencion: ",e);
		}
		return resultados;
	}
	
	/**
	 * Mï¿½todo para obtener los centros de atencion permitios x usuario segï¿½n la parametrizacion
	 * de unidades de agenda x usuario x centro de atencionn filtrando por ciudad
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerCentrosAtencionPermitidosUsuarioXCiudad(Connection con,HashMap parametros)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>(); 
		try
		{
			//***************SE TOMAN LOS PARï¿½METROS*************************************
			String codigoPais = parametros.get("codigoPais").toString();
			String codigoCiudad = parametros.get("codigoCiudad").toString();
			String codigoDepto = parametros.get("codigoDepto").toString();
			String loginUsuario = parametros.get("loginUsuario").toString();
			String tipoAtencion = parametros.get("tipoAtencion").toString();
			//**************************************************************************
			
			String consulta =  "SELECT distinct "+
				"ca.consecutivo as codigo_centro_atencion, "+
				"ca.descripcion as descripcion_centro_atencion "+ 
				"from consultaexterna.unid_agenda_usu_caten uauc "+ 
				"INNER JOIN consultaexterna.unidades_consulta uc ON (uc.codigo = uauc.unidad_agenda ) "+ 
				"INNER JOIN consultaexterna.unid_agenda_act_auto uaaa ON (uaaa.unid_agenda_usu_caten = uauc.codigo ) "+
				"INNER JOIN administracion.centro_atencion ca ON(ca.consecutivo = uauc.centro_atencion) "+ 
				"WHERE " +
				"uauc.usuario_autorizado = ? and " +
				"uc.tipo_atencion = ? and " +
				"ca.pais = ? and " +
				"ca.departamento = ? and " +
				"ca.ciudad = ? " +
				"order by descripcion_centro_atencion;";
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setString(1,loginUsuario);
			pst.setString(2,tipoAtencion);
			pst.setString(3,codigoPais);
			pst.setString(4,codigoDepto);
			pst.setString(5,codigoCiudad);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo", rs.getObject("codigo_centro_atencion"));
				elemento.put("descripcion", rs.getObject("descripcion_centro_atencion"));
				resultados.add(elemento);
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCentrosAtencionPermitidosUsuarioXCiudad: ",e);
		}
		
		return resultados;
	}
	
	/**
	 * Método para obtener el codigo del medico 
	 */
	public static int obtenerCodigoMedicoAgendaXCita(Connection con,int codigoCita)
	{
		int codigoMedico=ConstantesBD.codigoNuncaValido;
		try
		{
			
			String consulta= "select a.codigo_medico AS codigomedico from cita c inner join agenda a on (a.codigo=c.codigo_agenda) where c.codigo=?";
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
			pst.setInt(1,codigoCita);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			logger.info("la consulta----->"+consulta);
			logger.info("la cita----->"+codigoCita);
			
			
			if(rs.next())
			{
				codigoMedico = rs.getInt("codigomedico");
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCodigoMedicoAgendaXCita: "+e);
		}
		return codigoMedico;
	}
	
	
	/**
	 * Metodo para traer el codigo del medico que responde en la solicitud
	 */
	public static int obtenerCodigoMedicoRespondeSolicitud(Connection con, int nroSolicitud)
	{
		int codigoMedico=ConstantesBD.codigoNuncaValido;
		try
		{
			String consulta="select coalesce(s.codigo_medico_responde,0) AS codigomedicoresponde FROM solicitudes s inner join servicios_cita sc on (sc.numero_solicitud= s.numero_solicitud) inner join agenda a on (a.codigo=sc.codigo_agenda) where s.numero_solicitud=?";
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
			pst.setInt(1,nroSolicitud);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				codigoMedico = rs.getInt("codigomedicoresponde");
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCodigoMedicoAgendaXCita: "+e);
		}
		return codigoMedico;
	}
	
	/**
	 * 
	 */
	public static int obtenerSolicitudXCita(Connection con, int nroCita)
	{
		int solicitud=ConstantesBD.codigoNuncaValido;
		try
		{
			String consulta=	"SELECT " +
									"numero_solicitud AS numerosol " +
								"FROM " +
									"servicios_cita " +
								"WHERE " +
									"codigo_cita=?";
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
			pst.setInt(1,nroCita);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				solicitud = rs.getInt("numerosol");
			}

		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerSolicitudXCita: "+e);
		}
		return solicitud;
	}

	/**
	 * 
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public static String esReservaOrdenAmbulatoria(Connection con,int codigoCita) 
	{
		String numeroOrden="";
		try
		{
			String consulta=	"SELECT consecutivo_orden  as consecutivo from ordenes.ordenes_amb_reservas_citas oarc inner join ordenes_ambulatorias oa on (oa.codigo=oarc.orden) where oarc.codigo_cita=?";
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
			pst.setInt(1,codigoCita);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				numeroOrden = rs.getString("consecutivo");
			}

		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerSolicitudXCita: "+e);
		}
		return numeroOrden;
	}

	/**
	 * 
	 * @param con
	 * @param tipo
	 * @param codigoEspecialidad
	 * @param codigoCentroAtencion
	 * @param filtrarActivas
	 * @return
	 */
	public static ArrayList<DtoUnidadesConsulta> obtenerUnidadesAgendaXcentrosAtencionXEspecialidad(Connection con, String tipo, int codigoEspecialidad,int codigoCentroAtencion, boolean filtrarActivas) 
	{
		ArrayList<DtoUnidadesConsulta> resultado=new ArrayList<DtoUnidadesConsulta>();
		ArrayList<DtoUnidadesConsulta> resultadoConsulta=new ArrayList<DtoUnidadesConsulta>();
		
		String consulta="SELECT DISTINCT uc1.codigo as codigo,uc1.descripcion from unidades_consulta uc1 " +
				"inner join unid_agenda_usu_caten uauc1 on (uauc1.unidad_agenda=uc1.codigo) " +
				"inner join especialidades e1 on (uc1.especialidad=e1.codigo) " +
				"inner join centro_atencion ca1 on(ca1.consecutivo=uauc1.centro_atencion) " +
				"ORDER BY uc1.descripcion";
				
		
		Log4JManager.info("*******************CONSULTA UNIDADES AGENDA: "+ consulta);
		try
		{
			PreparedStatementDecorator ps1=new PreparedStatementDecorator(con,consulta);
			ResultSetDecorator rs1=new ResultSetDecorator(ps1.executeQuery());
			while(rs1.next())
			{
				DtoUnidadesConsulta dto1=new DtoUnidadesConsulta();
				dto1.setCodigo(rs1.getInt("codigo"));
				resultadoConsulta.add(dto1);
			
			}
			rs1.close();
			ps1.close();
			
			
			for (DtoUnidadesConsulta registro : resultadoConsulta) {
				
				String consolidadoConsulta = "SELECT DISTINCT  " +
				" uc.codigo as codigo," +
				" uc.descripcion as descripcion, " +
				" uc.activa as activa," +
				" uc.tipo_atencion as tipoatencion," +
				" uc.color as color," +
				" e.codigo as codigoespecialidad," +
				" e.consecutivo as consecutivoespecialidad," +
				" e.nombre as nombreespecialidad," +
				" uauc.centro_atencion as codigocentroatencion," +
				" ca.descripcion as descripcioncentroatencion," +
				" ca.consecutivo as consecutivocentroatencion " +
						" from unidades_consulta uc " +
						" inner join unid_agenda_usu_caten uauc on (uauc.unidad_agenda=uc.codigo) " +
						" inner join especialidades e on (uc.especialidad=e.codigo) " +
						" inner join centro_atencion ca on(ca.consecutivo=uauc.centro_atencion) " +
						"where 1=1  AND uc.codigo="+ registro.getCodigo();
				
				if(!tipo.trim().equals(""))
				{
					consolidadoConsulta=consolidadoConsulta+" and uc.tipo_atencion='"+tipo+"'";
				}
				if(codigoEspecialidad>0)
				{
					consolidadoConsulta=consolidadoConsulta+" and e.codigo="+codigoEspecialidad;
				}
				if(codigoCentroAtencion>0)
				{
					consolidadoConsulta=consolidadoConsulta+" and uauc.centro_atencion="+codigoCentroAtencion;
				}
				if(filtrarActivas)
				{
					consolidadoConsulta=consolidadoConsulta+" and uc.activa='"+ValoresPorDefecto.getValorTrueParaConsultas()+"'";
				}
				
				
				Log4JManager.info("*******************CONSOLIDADO UNIDADES AGENDA: "+ consolidadoConsulta);
				try
				{
					PreparedStatementDecorator ps=new PreparedStatementDecorator(con,consolidadoConsulta);
					ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
					int contador = 0;
					while(rs.next())
					{
						if(contador == 0){
							DtoUnidadesConsulta dto=new DtoUnidadesConsulta();
							dto.setActiva(rs.getBoolean("activa"));
							dto.setCodigo(rs.getInt("codigo"));
							dto.setColor(rs.getString("color"));
							dto.setDescripcion(rs.getString("descripcion"));
							dto.setTipoAtencion(rs.getString("tipoatencion"));
							DtoEspecialidades dtoEsp=new DtoEspecialidades();
							dtoEsp.setCodigo(rs.getInt("codigoespecialidad"));
							dtoEsp.setConsecutivo(rs.getString("consecutivoespecialidad"));
							dtoEsp.setDescripcion(rs.getString("nombreespecialidad"));
							dto.setEspecialidades(dtoEsp);
							DtoCentrosAtencion dtoCA=new DtoCentrosAtencion();
							dtoCA.setConsecutivo(rs.getInt("codigocentroatencion"));
							dtoCA.setCodigo(rs.getString("consecutivocentroatencion"));
							dtoCA.setDescripcion(rs.getString("descripcioncentroatencion"));
							dto.setCentroAtencion(dtoCA);
							resultado.add(dto);
						}
						contador ++;
					}
					rs.close();
					ps.close();
					
				
				}
				catch(SQLException e)
				{
					Log4JManager.error("error",e);
				}
		
			}
		}
		catch(SQLException e)
		{
			Log4JManager.error("error",e);
		}
		return resultado;
	}
	
	
	/**
	 * Método que permite verificar si existe agenda creada para un centro de costo y las unidades de agenda
	 * asociadas a un servicio para una fecha mayor o igual o la fecha pasada por parámetro
	 * @param codigoCentroCostro
	 * @param codigoServicio
	 * @param fecha
	 * @return
	 */
	public static boolean existeAgendaXCentroCostoXServicio(Connection con, int codigoCentroCosto, int codigoServicio, String fecha)
	{
		boolean existe = false;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta=	"select count(*) AS conteo from consultaexterna.servicios_unidades_consulta suc "+
								"inner join consultaexterna.agenda ag ON (ag.unidad_consulta=suc.unidad_consulta) "+
								"inner join administracion.centros_costo cc ON (cc.centro_atencion = ag.centro_atencion) "+
								"where suc.codigo_servicio=? and cc.codigo=? and ag.fecha >= to_date('"+fecha+"','dd/MM/yyyy')";
			
			pst = con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet );
			pst.setInt(1,codigoServicio);
			pst.setInt(2,codigoCentroCosto);
			rs = pst.executeQuery();
			
			if(rs.next())
			{
				int conteo = rs.getInt("conteo");
				if(conteo > 0){
					existe=true;
				}
			}

		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerSolicitudXCita: "+e);
		}
		finally{
			try {
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			} catch (SQLException e) {
				logger.error("Error Cerrando PreparedStatement ",e);
			}
		}
		return existe;
	}
}
	
		
