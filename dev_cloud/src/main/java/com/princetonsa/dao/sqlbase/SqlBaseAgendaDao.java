/*
 * Created on 14-abr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.princetonsa.dao.sqlbase;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
 
/**
 * @author juanda
 *
 */
public class SqlBaseAgendaDao
{
	private static Logger logger = Logger.getLogger(SqlBaseAgendaDao.class);
	
	/**
	* Cadena constante con el <i>Statement</i> necesario para listar ítems de agenda de consulta
	* desde una base de datos Genérica
	*/
	private static String listarEliminados =
	"SELECT a.codigo AS codigo, " +
	"a.fecha || '' AS fecha, " +
	"a.hora_inicio AS horaInicio, " +
	"a.hora_fin AS horaFin, " +
	"c.descripcion AS nombreConsultorio, " +
	"m.ocupacion_medica AS ocupacionMedica, " +
	"p.primer_nombre AS primerNombreMedico, " +
	"p.segundo_nombre AS segundoNombreMedico, " +
	"p.primer_apellido AS primerApellidoMedico, " +
	"p.segundo_apellido AS segundoApellidoMedico, " +
	"'true' AS esPos, " +
	"-1 AS codigo_sexo, " +
	"uc.codigo AS codigoUnidadConsulta, " +
	"uc.descripcion AS nombreUnidadConsulta, " +
	"p2.tipo_identificacion AS tipoIdentificacion," +
	"p2.numero_identificacion AS numeroIdentificacion," +
	"p2.primer_nombre AS primerNombrePaciente, " +
	"p2.segundo_nombre AS segundoNombrePaciente, " +
	"p2.primer_apellido AS primerApellidoPaciente, " +
	"p2.segundo_apellido AS segundoApellidoPaciente, " +
	"p2.telefono AS telefono, " +
	"-11 AS codigoServicio " +
	"FROM agenda a " +
	"INNER JOIN unidades_consulta uc ON(uc.codigo = a.unidad_consulta) " +
	"INNER JOIN consultorios c ON(c.codigo=a.consultorio) " +
	"INNER JOIN medicos m ON(a.codigo_medico=m.codigo_medico) " +
	"INNER JOIN personas p ON(p.codigo=m.codigo_medico) " +
	"INNER JOIN cita ci ON (ci.codigo_agenda=a.codigo) " +
	"INNER JOIN personas p2 ON(p2.codigo=ci.codigo_paciente) " +
	"WHERE 1=1 and ";
	
	
	
	@SuppressWarnings("rawtypes")
	public static Collection listarCitasOcupadasAEliminar(
			Connection con, 
			String horaInicio, 
			String horaFin, 
			String fechaInicio, 
			String fechaFin, 
			int unidadConsulta,
			int consultorio,
			int diaSemana,
			int codigoMedico,
			int centroAtencion,
			String centrosAtencion,
			String unidadesAgenda)
	{
		try
		{
			if(con == null || con.isClosed() )
							con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
		}
		catch (SQLException e)
		{
			logger.error("Error abriendo la conexión: " +e);
		}
		try{
			PreparedStatementDecorator listarCitasOcupadasAEliminar=null;
			String consulta=listarEliminados+
										"to_char(fecha,'yyyy-mm-dd') >='"+UtilidadFecha.conversionFormatoFechaABD(fechaInicio)+"' AND to_char(fecha,'yyyy-mm-dd') <='"+UtilidadFecha.conversionFormatoFechaABD(fechaFin)+"'" +
										"AND((fecha=CURRENT_DATE AND hora_inicio >='"+UtilidadFecha.getHoraActual()+"')OR fecha >CURRENT_DATE)"+
										" AND ci.estado_cita<>7 AND ci.estado_cita<>9 "+restringirConsulta(	horaInicio,
																																horaFin,
																																unidadConsulta,
																																consultorio,
																																diaSemana,
																																codigoMedico,
																																centroAtencion, 
																																centrosAtencion,
																																unidadesAgenda
																															);
			
			
			logger.info(consulta);
			listarCitasOcupadasAEliminar= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator resultado=new ResultSetDecorator(listarCitasOcupadasAEliminar.executeQuery());
			Collection col=UtilidadBD.resultSet2Collection(resultado);
			resultado.close();
			listarCitasOcupadasAEliminar.close();
			return col;
		}
		catch(SQLException e)
		{
			logger.warn("Error cargando lista para eliminar: "+e.getMessage(),e);
			return null;
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param horaInicio
	 * @param horaFin
	 * @param fechaInicio
	 * @param fechaFin
	 * @param unidadConsulta
	 * @param consultorio
	 * @param diaSemana
	 * @param codigoMedico
	 * @param centroAtencion
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap listarCitasNoAtendidasAEliminar(Connection con, String horaInicio, String horaFin, String fechaInicio, String fechaFin, int unidadConsulta, int consultorio, int diaSemana, int codigoMedico, int centroAtencion, String centrosAtencion, String unidadesAgenda) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs=null;
		try
		{
			
			String consulta=listarEliminados+
												"to_char(fecha,'yyyy-mm-dd') >='"+UtilidadFecha.conversionFormatoFechaABD(fechaInicio)+"' AND to_char(fecha,'yyyy-mm-dd') <='"+UtilidadFecha.conversionFormatoFechaABD(fechaFin)+"' " +
												"AND((fecha=CURRENT_DATE AND hora_inicio>='"+UtilidadFecha.getHoraActual()+"')OR fecha >CURRENT_DATE)"+
												" AND ci.estado_cita=9 "+restringirConsulta(horaInicio,horaFin,unidadConsulta,consultorio,diaSemana,codigoMedico,centroAtencion,centrosAtencion,unidadesAgenda);
			
			pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs=new ResultSetDecorator(pst.executeQuery());
			mapa=UtilidadBD.cargarValueObject(rs);
		}
		catch(Exception e){
			Log4JManager.error("ERROR listarCitasNoAtendidasAEliminar", e);
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
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return mapa;
	}

	/**
	* Genera una clausula WHERE válida de acuerdo a los párametros de entrada para búsquedas sobre
	* el horario de atención
	* @param ai_funcionalidad	Indica la funcionalidad desde donde es invocado el método
	* @param ai_unidadConsulta	Unidad de consulta sobre la cual se restringirá la búsqueda de
	*							horarios de atención o de ítems de agenda. Si es menor que 0 la
	*							agenda no habrá restricciones. De lo contrario se restringirán los
	*							registros que coincidan con este parámetro
	* @param ai_consultorio		Consultorio sobre el cual se restringirá la búsqueda de horarios de
	*							atención o de ítems de agenda. Si es menor que 0 no habrá
	*							restricciones. De lo contrario se restringirán los registros que
	*							coincidan con este parámetro
	* @param ai_diaSemana		Día de la semana sobre el cual se restringirá la búsqueda de
	*							horarios de atención o de ítems de agenda. Si es menor que 0 no
	*							habrá restricciones. De lo contrario se restringirán todos los
	*							registros que coincidan con este parámetro será generada para todos
	*							los diás de la semana con horarios de atención definidos
	* @param as_horaInicio		Hora de inicio sobre la se restringirá la búsqueda de ítems de
	*							agenda. Si es vacia no habrá restricciones. De lo contrario se
	*							restringiran los paramentros que coincidan con este parámentro.
	* @param as_horaFin			Hora de finalización sobre la se restringirá la búsqueda de ítems de
	*							agenda. Si es vacia no habrá restricciones. De lo contrario se
	*							restringiran los paramentros que coincidan con este parámentro.
	* @param ai_codigoMedico	Código del médico sobre el cual se restringirá la búsqueda de
	*							horarios de de atención o de ítems de agenda. Si es vacia no habra
	*							restricciones. De lo contrario se restringirán los registros que
	*							coincidan con esteparámetro
	*@param centroAtencion Código del centro de atención sobre el cual se restringirá la búsqueda de
	*							horarios de de atención o de ítems de agenda. Si es -1 no habra
	*							restricciones. De lo contrario se restringirán los registros que
	*							coincidan con este parámetro
	* @return Clausula WHERE con las retricciones necesarias
	*/
	private static String restringirConsulta(
		String	as_horaInicio,
		String	as_horaFin,
		int		ai_unidadConsulta,
		int		ai_consultorio,
		int		ai_diaSemana,
		int		ai_codigoMedico,
		int 		centroAtencion,
		String centrosAtencion,
		String unidadesAgenda
	)
	{
		StringBuffer	lsb_where;
		
		lsb_where = new StringBuffer();

		/* Validar la hora de inicio */
		if(!as_horaInicio.equals("") )
		{
			as_horaInicio=as_horaInicio+ ":00";
			lsb_where.append(" AND a.hora_inicio>='" + as_horaInicio + "'");
		}

		/* Validar la hora de finalización */
		if(!as_horaFin.equals("") )
		{
			as_horaFin=as_horaFin+":00";
			lsb_where.append(" AND a.hora_inicio<='" + as_horaFin + "'");
		}

		/* Validar la unidad de consulta */
		if(ai_unidadConsulta > -1)
			lsb_where.append(" AND a.unidad_consulta=" + ai_unidadConsulta);
		else
			lsb_where.append(" AND a.unidad_consulta IN (" + unidadesAgenda+") ");
		
		/* Validar el consultorio del horario de atención */
		if(ai_consultorio > -1)
			lsb_where.append(" AND a.consultorio=" + ai_consultorio);

		/* Validar el día de la semana del horario de atención */
		if(ai_diaSemana > -1)
			lsb_where.append(" AND dia=" + ai_diaSemana);

		/* Validar el código del médico del horario de atención */
		if(ai_codigoMedico > -1)
			lsb_where.append(" AND a.codigo_medico=" + ai_codigoMedico + "");
		
		/* Validar el centro de atención seleccionado */
		if(centroAtencion != ConstantesBD.codigoNuncaValido)
			lsb_where.append(" AND c.centro_atencion=" + centroAtencion + "");
		else
			lsb_where.append(" AND c.centro_atencion IN (" + centrosAtencion + ") ");

		return lsb_where.toString();
	}
	
	
	
	
	/**
	 * Consulta los Horarios de Atencion que no poseen Consultorios
	 * @param Connection con 
	 * @param String as_fechaInicio
	 * @param String as_fechaFin
	 * @param ai_unidadConsulta
	 * @param ai_diaSemana
	 * @param ai_codigoMedico,
	 * @param centroAtencion
	 * @param i 
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap getHorariosAtencionSinConsultorios(
															  Connection 	con,
															  String		as_fechaInicio,
															  String		as_fechaFin,
															  int			ai_unidadConsulta,
															  int			ai_diaSemana,
															  int			ai_codigoMedico,															  
															  int 			centroAtencion,
															  String 		unidadesAgenda,
															  String 		centrosAtencion, int Tipo_BD
															 )
	{
		HashMap mapa = new HashMap();
		mapa.put("numRegistros","0");
		
		
		int					li_resp;
		long				ll_fechaFin;
		long				ll_fechaInicio;
		SimpleDateFormat	lsdf_fechas;
		StringBuffer		lsb_fechas;
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs=null;
		
		String preConsultaSql = "SELECT " +
							  	"ha.codigo,"  +
							  	"ha.unidad_consulta," +	
							  	"uc.descripcion AS nombreUnidadConsulta, " +
							  	"'"+ConstantesBD.codigoNuncaValido+"' AS consultorio," +						  						  
							  	"ha.dia," +
							  	"d.dia AS nombredia," +							  	
							  	"ha.hora_inicio," +
							  	"ha.hora_fin," +
							  	"ha.tiempo_sesion, " +
							  	"ha.codigo_medico," +
							  	"p.primer_apellido || ' ' || p.segundo_apellido || ' ' || p.primer_nombre || ' ' || p.segundo_nombre AS nombremedico," +							
							  	"ha.tiempo_sesion," +
							  	"ha.pacientes_sesion," +
							  	"ha.centro_atencion, " +
							  	"getnomcentroatencion(ha.centro_atencion) AS nombrecentroatencion " +
								"FROM horario_atencion ha " +								
								"INNER JOIN (select fecha, administracion.obtenerdiasemana(fgt.fecha) as diaSemana from ( @ ) fgt )fg ON(fg.diaSemana=((CASE WHEN ha.dia=7 THEN 0 ELSE ha.dia END))) "+
								"LEFT OUTER JOIN unidades_consulta uc ON(uc.codigo=ha.unidad_consulta) " +
								"LEFT OUTER JOIN dias_semana d ON(d.codigo=ha.dia) " +					  
								"LEFT OUTER JOIN personas p ON (p.codigo=ha.codigo_medico) ";
					
								
		preConsultaSql+="WHERE ha.consultorio IS NULL ";
						  		
		try
		{
			/* Inicializar el rango de fechas para la búsqueda de horarios de atención */
			lsb_fechas = new StringBuffer();
	
			/* Obtener todas fechas sobre las cuales se debe generar agenda */
			lsdf_fechas	= new SimpleDateFormat("dd/MM/yyyy");
	
			/* Exije una interpretación estricta del formato de fecha/hora esperado */
			lsdf_fechas.setLenient(false);
	
			/* Obtener las fechas iniciales y finales del rengo de fechas de generación de la agenda */		
			//Fechas en Milisegundos
			ll_fechaFin		= lsdf_fechas.parse(as_fechaFin).getTime();
			ll_fechaInicio	= lsdf_fechas.parse(as_fechaInicio).getTime();			
			
			for(li_resp = 0; ll_fechaInicio <= ll_fechaFin; ll_fechaInicio += 86400000, li_resp++)
			{
				switch(Tipo_BD)
				{
				case DaoFactory.ORACLE:
					lsb_fechas.append(
							"SELECT to_date('"+UtilidadFecha.conversionFormatoFechaABD(ll_fechaInicio)+"','yyyy-mm-dd') AS fecha from dual"
						);
					break;
				case DaoFactory.POSTGRESQL:
					lsb_fechas.append(
							"SELECT DATE '"											+
							UtilidadFecha.conversionFormatoFechaABD(ll_fechaInicio)	+
							"' AS fecha"
						);
					break;
				default:
					break;
				}
				
	
				if(ll_fechaInicio < ll_fechaFin)
					lsb_fechas.append(" UNION ");
			}			
			/* Procesar la información sí y solo sí hay una fecha o mas en el rango de fechas */
			if(li_resp > 0)
			{				
				//-----Si el centro de atención es diferente de todos se realiza el filtro en la excepción de agenda ---//
				if(centroAtencion!=ConstantesBD.codigoNuncaValido)
					preConsultaSql += " AND ha.centro_atencion = "+centroAtencion+" AND fg.fecha NOT IN (SELECT fecha FROM excepciones_agenda WHERE centro_atencion="+centroAtencion+") ";
				else
					preConsultaSql += " AND ha.centro_atencion IN ("+centrosAtencion+") AND fg.fecha NOT IN (SELECT fecha FROM excepciones_agenda) ";
				
				
				/* Validar la unidad de consulta */
				if(ai_unidadConsulta > -1)
					preConsultaSql += " AND ha.unidad_consulta="+ai_unidadConsulta;
				else
					preConsultaSql += " AND ha.unidad_consulta IN ("+unidadesAgenda+") ";		
				
				/* Validar el día de la semana del horario de atención */
				if(ai_diaSemana > -1)
					preConsultaSql += " AND ha.dia=" + ai_diaSemana;
				else
					preConsultaSql += " AND ha.dia<>-1";
				
				/* Validar el código del médico del horario de atención */
				if(ai_codigoMedico == -2)				
					preConsultaSql += " AND ha.codigo_medico IS NULL ";				
				else if(ai_codigoMedico > -1)				
					preConsultaSql += " AND ha.codigo_medico=" + ai_codigoMedico;
				
				preConsultaSql = preConsultaSql.replaceFirst("@",lsb_fechas.toString());
				
				switch(Tipo_BD)
				{
				case DaoFactory.POSTGRESQL:
					
					preConsultaSql += " GROUP BY ha.codigo," +
					  "ha.unidad_consulta," +
					  "nombreUnidadConsulta," +
					  "consultorio," +
					  "ha.dia," +
					  "nombredia," +
					  "ha.hora_inicio," +
					  "ha.hora_fin," +
					  "ha.tiempo_sesion," +
					  "ha.codigo_medico," +
					  "nombremedico," +
					  "ha.tiempo_sesion," +
					  "ha.pacientes_sesion," +								  
					  "ha.centro_atencion," +
					  "nombrecentroatencion ";
					break;
				case DaoFactory.ORACLE:
					
					preConsultaSql += " GROUP BY ha.codigo," +
					  "ha.unidad_consulta," +
					  "uc.descripcion," +
					  "'-1'," +
					  "ha.dia," +
					  "d.dia," +
					  "ha.hora_inicio," +
					  "ha.hora_fin," +
					  "ha.tiempo_sesion," +
					  "ha.codigo_medico," +
					  "p.primer_apellido," +
					  "p.segundo_apellido," +
					  "p.primer_nombre," +
					  "p.segundo_nombre," +
					  "ha.tiempo_sesion," +
					  "ha.pacientes_sesion," +								  
					  "ha.centro_atencion," +
					  "getnomcentroatencion(ha.centro_atencion)";
					 
					break;
				default:
					break;
				}
				
				logger.info("--->"+preConsultaSql);
								
				pst =  new PreparedStatementDecorator(con.prepareStatement(preConsultaSql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));		
				rs=new ResultSetDecorator(pst.executeQuery());
				mapa = UtilidadBD.cargarValueObject(rs);
				}
		}
		catch(Exception e){
			Log4JManager.error("ERROR getHorariosAtencionSinConsultorios", e);
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
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}	
		return mapa;		
	}

	/**
	 * Método implementado para generar la consulta de los horarios de atencion en la generación de la agenda
	 * @param centroAtencion
	 * @param lsb_fechas
	 * @param ai_unidadConsulta
	 * @param ai_consultorio
	 * @param ai_diaSemana
	 * @param ai_codigoMedico
	 * @param centrosAtencion
	 * @param unidadesAgenda
	 * @param Tipo_BD 
	 * @return
	 */
	public static StringBuffer generarConsultaHorariosGeneracionAgenda(int centroAtencion, StringBuffer lsb_fechas, int ai_unidadConsulta, int ai_consultorio, int ai_diaSemana, int ai_codigoMedico, String centrosAtencion, String unidadesAgenda, int Tipo_BD) 
	{
		
		StringBuffer consulta = new StringBuffer();
		consulta.append("SELECT * FROM (");
		
		//******************SE ARMA SUBCONSULTA DE HORARIOS DE ATENCIÓN CON CONSULTORIO Y CON DÍA PARAMETRIZADO**************************
		consulta.append("( "+
			"SELECT "+ 
			"ha.codigo, "+
			"ha.unidad_consulta, "+
			"ha.consultorio, "+
			"CASE WHEN fg.diaSemana = 0 THEN 7 ELSE fg.diaSemana END  AS dia, "+
			"to_char(fg.fecha,'"+ConstantesBD.formatoFechaBD+"') as fecha, "+
			"ha.hora_inicio, "+
			"ha.hora_fin, "+
			"ha.codigo_medico, "+
			"ha.tiempo_sesion, "+
			"ha.pacientes_sesion, "+
			"c.centro_atencion, "+
			"c.descripcion AS descripcion_consultorio "+ 
			"FROM horario_atencion ha "+ 
			"INNER JOIN consultorios c on(c.codigo=ha.consultorio) ");
			
		consulta.append("INNER JOIN ( " +
										" select " +
													" fecha, " +
													" administracion.obtenerdiasemana(fgt.fecha) as  diaSemana " +
										" from ( "+lsb_fechas.toString()+" ) fgt " +
								" ) fg on( (CASE WHEN ha.dia=7 THEN 0 ELSE ha.dia END)=fg.diaSemana) ");
			
		consulta.append("WHERE "+ 
								" fg.fecha NOT IN " +
								 									" (SELECT  fecha   FROM excepciones_agenda "+(centroAtencion!=ConstantesBD.codigoNuncaValido?" WHERE centro_atencion="+centroAtencion:" WHERE centro_atencion IN ("+centrosAtencion+") ")+") "+ 
								" AND to_date(fg.fecha||'','YYYY-MM-DD') >=to_date(CURRENT_DATE||'','YYYY-MM-DD')  ");
		//Se realiza filtro de las unidades de agenda
		if(ai_unidadConsulta!=ConstantesBD.codigoNuncaValido)
			consulta.append(" AND ha.unidad_consulta = "+ai_unidadConsulta);
		else
			consulta.append(" AND ha.unidad_consulta IN ("+unidadesAgenda+") ");
		//Se realiza filtro de consultorio
		if(ai_consultorio>-1)
			consulta.append(" AND ha.consultorio = "+ai_consultorio);
		else
			consulta.append(" AND ha.consultorio <> -1 ");
		//Se filtra el día de la semana
		if(ai_diaSemana>-1)
			consulta.append(" AND fg.diaSemana = "+ai_diaSemana);
		else
			consulta.append(" AND fg.diaSemana <> -1 ");
		//Se filtra el centro de atencion
		if(centroAtencion!=ConstantesBD.codigoNuncaValido)
			consulta.append(" AND ha.centro_atencion = "+centroAtencion);
		else
			consulta.append(" AND ha.centro_atencion IN ("+centrosAtencion+") ");
		//Se filtra el profesional
		if(ai_codigoMedico == -2)
			/* Generar agenda para los horarios de atención que no tienen asignado el médico */
			consulta.append(" AND ha.codigo_medico IS NULL");
		else if(ai_codigoMedico > -1)
			consulta.append(" AND ha.codigo_medico=" + ai_codigoMedico);
		consulta.append(") UNION ");
		//**************************************************************************************************************************************
		//*************SE ARMA SUBCONSULTA DE HORARIOS DE ATENCION SIN CONSULTORIO Y CON DÍA*****************************************************
		consulta.append("( "+
			"SELECT "+ 
			"ha.codigo, "+
			"ha.unidad_consulta, "+
			"ha.consultorio, "+
			"CASE WHEN fg.diaSemana = 0 THEN 7 ELSE fg.diaSemana END  AS dia, "+
			"to_char(fg.fecha,'yyyy-mm-dd') as fecha, "+
			"ha.hora_inicio, "+
			"ha.hora_fin, "+
			"ha.codigo_medico, "+
			"ha.tiempo_sesion, "+
			"ha.pacientes_sesion, "+
			"ha.centro_atencion AS centro_atencion, "+
			"'' AS descripcion_consultorio "+ 
			"FROM horario_atencion ha ");
		
			consulta.append("INNER JOIN ( select fecha, administracion.obtenerdiasemana(fgt.fecha) as   diaSemana from ( "+lsb_fechas.toString()+" ) fgt ) fg on(CASE WHEN ha.dia=7 THEN 0 ELSE ha.dia END=fg.diaSemana) ");
			
			consulta.append("WHERE "+ 
			"fg.fecha NOT IN (SELECT fecha FROM excepciones_agenda "+(centroAtencion!=ConstantesBD.codigoNuncaValido?" WHERE centro_atencion="+centroAtencion:" WHERE centro_atencion IN ("+centrosAtencion+") ")+") "+ 
			"AND to_date(fg.fecha||'','YYYY-MM-DD') >=to_date(CURRENT_DATE||'','YYYY-MM-DD')  ");
		//Se realiza filtro de las unidades de agenda
		if(ai_unidadConsulta!=ConstantesBD.codigoNuncaValido)
			consulta.append(" AND ha.unidad_consulta = "+ai_unidadConsulta);
		else
			consulta.append(" AND ha.unidad_consulta IN ("+unidadesAgenda+") ");
		consulta.append(" AND ha.consultorio IS NULL  ");
		//Se filtra el día de la semana
		if(ai_diaSemana>-1)
			consulta.append(" AND fg.diaSemana = "+ai_diaSemana);
		else
			consulta.append(" AND fg.diaSemana <> -1 ");
		//Se filtra el centro de atencion
		if(centroAtencion!=ConstantesBD.codigoNuncaValido)
			consulta.append(" AND ha.centro_atencion = "+centroAtencion);
		else
			consulta.append(" AND ha.centro_atencion IN ("+centrosAtencion+") ");
		//Se filtra el profesional
		if(ai_codigoMedico == -2)
			/* Generar agenda para los horarios de atención que no tienen asignado el médico */
			consulta.append(" AND ha.codigo_medico IS NULL");
		else if(ai_codigoMedico > -1)
			consulta.append(" AND ha.codigo_medico=" + ai_codigoMedico);
		consulta.append(") UNION ");
		//**********************************************************************************************************************************
		//***************SE ARMA SUBCONSULTA DE HORARIOS CON CONSULTORIO Y SIN DÍA PARAMETRIZADO***********************************************************
		consulta.append("( "+
			"SELECT "+ 
			"ha.codigo, "+
			"ha.unidad_consulta, "+
			"ha.consultorio, "+
			"CASE WHEN fg.diaSemana = 0 THEN 7 ELSE fg.diaSemana END as dia, "+
			"to_char(fg.fecha,'yyyy-mm-dd') as fecha, "+
			"ha.hora_inicio, "+
			"ha.hora_fin, "+
			"ha.codigo_medico, "+
			"ha.tiempo_sesion, "+
			"ha.pacientes_sesion, "+
			"c.centro_atencion, "+
			"c.descripcion AS descripcion_consultorio "+ 
			"FROM horario_atencion ha   "+ 
			"INNER JOIN consultorios c on(c.codigo=ha.consultorio) ") ;
			
		
		
			consulta.append("INNER JOIN ( select fecha, administracion.obtenerdiasemana(fgt.fecha) as    diaSemana from ( "+lsb_fechas.toString()+" ) fgt ) fg on( ");
		
			consulta.append("  fg.fecha NOT IN (SELECT fecha FROM excepciones_agenda "+(centroAtencion!=ConstantesBD.codigoNuncaValido?" WHERE centro_atencion="+centroAtencion:" WHERE centro_atencion IN ("+centrosAtencion+") ")+")  " +
				"AND to_date(fg.fecha||'','YYYY-MM-DD') >=to_date(CURRENT_DATE||'','YYYY-MM-DD') ) ");
		
			consulta.append(" where ");
		
		//Se realiza filtro de las unidades de agenda
		if(ai_unidadConsulta!=ConstantesBD.codigoNuncaValido)
			consulta.append("ha.unidad_consulta = "+ai_unidadConsulta);
		else
			consulta.append(" ha.unidad_consulta IN ("+unidadesAgenda+") ");
		//Se realiza filtro de consultorio
		if(ai_consultorio>-1)
			consulta.append(" AND ha.consultorio = "+ai_consultorio);
		else
			consulta.append(" AND ha.consultorio <> -1 ");
		consulta.append(" AND ha.dia IS NULL ");
		//Se filtra el centro de atencion
		if(centroAtencion!=ConstantesBD.codigoNuncaValido)
			consulta.append(" AND ha.centro_atencion = "+centroAtencion);
		else
			consulta.append(" AND ha.centro_atencion IN ("+centrosAtencion+") ");
		//Se filtra el profesional
		if(ai_codigoMedico == -2)
			/* Generar agenda para los horarios de atención que no tienen asignado el médico */
			consulta.append(" AND ha.codigo_medico IS NULL");
		else if(ai_codigoMedico > -1)
			consulta.append(" AND ha.codigo_medico=" + ai_codigoMedico);
		consulta.append(") UNION ");
		//*******************************************************************************************************************
		//************+SE ARMA SUBCONSULTA DE HORARIOS SIN CONSULTORIO Y SIN DIA PARAMETRIZADO******************************
		consulta.append("( "+
			"SELECT "+ 
			"ha.codigo, "+
			"ha.unidad_consulta, "+
			"ha.consultorio, "+
			"CASE WHEN fg.diaSemana = 0 THEN 7 ELSE fg.diaSemana END as dia, "+
			"to_char(fg.fecha,'yyyy-mm-dd') as fecha, "+
			"ha.hora_inicio, "+
			"ha.hora_fin, "+
			"ha.codigo_medico, "+
			"ha.tiempo_sesion, "+
			"ha.pacientes_sesion, "+
			"ha.centro_atencion as centro_atencion, "+ 
			"'' AS descripcion_consultorio "+ 
			"FROM horario_atencion ha ");
		
		consulta.append("INNER JOIN ( select fecha, administracion.obtenerdiasemana(fgt.fecha) as    diaSemana from ( "+lsb_fechas.toString()+" ) fgt ) fg on( ");
		
			
			consulta.append(" fg.fecha NOT IN (SELECT fecha FROM excepciones_agenda "+(centroAtencion!=ConstantesBD.codigoNuncaValido?" WHERE centro_atencion="+centroAtencion:" WHERE centro_atencion IN ("+centrosAtencion+") ")+") " +
				"AND to_date(fg.fecha||'','YYYY-MM-DD') >=to_date(CURRENT_DATE||'','YYYY-MM-DD')  "+
			") "+
			"WHERE ");
		//Se realiza filtro de las unidades de agenda
		if(ai_unidadConsulta!=ConstantesBD.codigoNuncaValido)
			consulta.append(" ha.unidad_consulta = "+ai_unidadConsulta);
		else
			consulta.append(" ha.unidad_consulta IN ("+unidadesAgenda+") "); 
		consulta.append("AND ha.consultorio IS NULL    AND ha.dia IS NULL   ");
		//Se filtra el centro de atencion
		if(centroAtencion!=ConstantesBD.codigoNuncaValido)
			consulta.append(" AND ha.centro_atencion = "+centroAtencion);
		else
			consulta.append(" AND ha.centro_atencion IN ("+centrosAtencion+") ");
		//Se filtra el profesional
		if(ai_codigoMedico == -2)
			/* Generar agenda para los horarios de atención que no tienen asignado el médico */
			consulta.append(" AND ha.codigo_medico IS NULL  ");
		else if(ai_codigoMedico > -1)
			consulta.append(" AND ha.codigo_medico=" + ai_codigoMedico);
		consulta.append(") ");
		//***********************************************************************************************************************
		consulta.append(" ) t "+
			"GROUP BY   " +
			"    t.codigo,t.unidad_consulta,t.consultorio,t.dia,t.fecha,t.hora_inicio,t.hora_fin,t.codigo_medico,t.tiempo_sesion,t.pacientes_sesion,t.centro_atencion,t.descripcion_consultorio "+ 
			"ORDER BY " +
			"    t.consultorio ASC");
		
		
		return consulta;
	}

	
}