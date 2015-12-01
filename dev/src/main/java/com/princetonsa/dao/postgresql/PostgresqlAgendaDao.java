/*
* @(#)PostgresqlAgendaDao.java
*
* Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*/
package com.princetonsa.dao.postgresql;


import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;


import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.AgendaDao;
import com.princetonsa.dao.sqlbase.SqlBaseAgendaDao;

import java.util.HashMap;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
* Esta clase implementa el contrato estipulado en <code>AgendaDao</code>, y presta los servicios de
* acceso a una base de datos PostgreSQL requeridos por la clase <code>Agenda</code>.
*
* @version 1.0, Sep 15, 2003
* @author <a href="mailto:edgar@PrincetonSA.com">Edgar Prieto</a>
*/
public class PostgresqlAgendaDao implements AgendaDao
{
	private String activarAgenda="UPDATE agenda SET cupos=?, activo=true, usuario=?,codigo_medico=? WHERE unidad_consulta=? and consultorio=? and fecha=? and hora_inicio=? and activo=false";
	/** Constante que indica la funcionalidad de cancelación de la agenda */
	private static final int ii_CANCELAR = 0;

	/** Constante que indica la funcionalidad de generación de la agenda */
	private static final int ii_GENERAR = 1;

	/** Constante que indica la funcionalidad de listado de la agenda */
	private static final int ii_LISTAR = 2;

	/** Objeto para realizar acciones de registro */
	private Logger il_logger = Logger.getLogger(PostgresqlAgendaDao.class);
	
	private static Logger logger = Logger.getLogger(PostgresqlAgendaDao.class);

	/**
	* Cadena constante con el <i>Statement</i> PostgreSQL necesario para cancelar las citas ligadas
	* a un item de agenda. Se deben dejar las citas en estado a reprogramar
	*/
	private static final String is_cancelarCita =
		"UPDATE "	+	"cita "		+
		"SET "		+	"estado_cita"	+ "="	+ ConstantesBD.codigoEstadoCitaAReprogramar	+
						//"codigo_agenda"	+ "=NULL "									+
		"WHERE "	+	"codigo_agenda ";

	/**
	* Cadena constante con el <i>Statement</i> PostgreSQL necesario para consultar los items de
	* agenda a eliminar
	*/
	private static final String is_consultarAEliminar =
		"SELECT a.codigo" +
		" FROM agenda a " +
		"LEFT OUTER JOIN cita c on (c.codigo_agenda=a.codigo)" +
		" WHERE " +
		" a.fecha>=? AND a.fecha<=? AND " +
		" (c.estado_cita IS NULL or (c.estado_cita<>7 and c.estado_cita<>9))";

	/**
	* Cadena constante con el <i>Statement</i> PostgreSQL necesario para eliminar items de agenda
	* requeridos para la generación de la agenda
	*/
	private static final String is_eliminar =
		//"DELETE "	+
		"UPDATE "+
		" agenda "	+
		"SET activo=false "+
		"WHERE activo<>false AND codigo ";
	
	/**
	 * Cadena que consulta la agenda que no está ligada a citas y que se va a cancelar 
	 */
	private static final String consultaAgendaACancelarStr = "SELECT "+ 
		"a.codigo AS codigo_agenda, "+
		"to_char(a.fecha_gen,'DD/MM/YYYY') AS  fecha_generacion, "+
		"substr(a.hora_gen,0,6) AS hora_generacion, "+
		"a.usuario AS usuario, "+
		"getnombreunidadconsulta(a.unidad_consulta) As unidad_consulta, "+
		"a.dia As dia, "+
		"to_char(a.fecha,'DD/MM/YYYY') AS fecha, "+
		"substr(a.hora_inicio,0,6) As hora_inicio, "+
		"substr(a.hora_fin,0,6) AS hora_fin, "+
		"CASE WHEN a.codigo_medico IS NULL THEN '' ELSE getnombrepersona(a.codigo_medico) END As medico, "+
		"a.tiempo_sesion As tiempo_sesion, "+
		"a.cupos AS cupos, "+
		"CASE WHEN a.activo IS TRUE THEN 'Sí' ELSE 'No' END AS activo, "+
		"getnomcentroatencion(a.centro_atencion) AS centro_atencion, "+
		"c.descripcion AS consultorio "+ 
		"FROM agenda a "+ 
		"INNER JOIN consultorios c ON(c.codigo=a.consultorio) "+ 
		"LEFT OUTER JOIN cita ci ON(ci.codigo_agenda=a.codigo) "+ 
		"WHERE " +
		" a.codigo NOT IN (SELECT codigo_agenda FROM cupos_extra WHERE codigo_agenda = a.codigo) AND " +
		" a.codigo NOT IN (SELECT cod_agenda_modificada FROM log_reasignacion_citas WHERE cod_agenda_modificada=a.codigo) AND  " ; 
	
	/**
	 * Cadena que elimina la agenda que no está ligada a citas y que se va a cancelar
	 */
	private static final String eliminarAgendaACancelarStr = "DELETE FROM agenda WHERE codigo = ?";
		

	/**
	* Cadena constante con el <i>Statement</i> necesario para detallar los datos de un ítems de
	* agenda de consulta desde una base de datos PostgreSQL
	*/
	private static final String is_detalle =
		"SELECT "	+	"DISTINCT(a.codigo)"	+ "AS codigo,"					+
						"TEXT(a.fecha)"			+ "AS fecha,"					+
						"a.hora_inicio "		+ "AS horaInicio,"				+
						"a.hora_fin "			+ "AS horaFin,"					+
						"a.cupos "				+ "AS cupos,"					+
						"a.tiempo_sesion "		+ "AS sesion,"					+
						"uc.descripcion "		+ "AS nombreUnidadConsulta,"	+
						"c.descripcion "		+ "AS nombreConsultorio,"		+
						"p1.primer_nombre "		+ "AS primerNombreMedico,"		+
						"p1.segundo_nombre "	+ "AS segundoNombreMedico,"		+
						"p1.primer_apellido "	+ "AS primerApellidoMedico,"	+
						"p1.segundo_apellido "	+ "AS segundoApellidoMedico,"	+
						"p2.primer_nombre "		+ "AS primerNombreUsuario,"		+
						"p2.segundo_nombre "	+ "AS segundoNombreUsuario,"	+
						"p2.primer_apellido "	+ "AS primerApellidoUsuario,"	+
						"p2.segundo_apellido "	+ "AS segundoApellidoUsuario,"	+
						"TEXT(a.fecha_gen)"		+ "AS fechaGeneracion,"			+
						"TEXT(a.hora_gen)"		+ "AS horaGeneracion "			+
		"FROM "		+	"agenda "				+ "a "							+
						"INNER JOIN unidades_consulta "	+ "uc "	+ "ON(uc.codigo"	+ "=a.unidad_consulta)"	+
						"INNER JOIN consultorios "		+ "c "	+ "ON(c.codigo"		+ "=a.consultorio)"		+
						"LEFT OUTER JOIN("					+
							"medicos "					+ "m "						+
							"LEFT OUTER JOIN personas "		+ "p1 "	+ "ON(p1.codigo"	+ "=m.codigo_medico)"	+
						")"										+ "USING(codigo_medico)"					+
						"LEFT OUTER JOIN("					+
							"usuarios "					+ "u "						+
							"LEFT OUTER JOIN personas "		+ "p2 "	+ "ON(p2.codigo"	+ "=u.codigo_persona)"	+
						")"										+ "ON(u.login"		+ "=a.usuario)"			+
		"WHERE "	+	"a.codigo=?";

	
	/**
	* Cadena constante con el <i>Statement</i> necesario para insertar un item de agenda en una base
	* de datos PostgreSQL
	*/
	private static final String is_insertar =
		"INSERT INTO agenda"	+
		"("						+
			"unidad_consulta,"	+
			"consultorio,"		+
			"dia,"				+
			"fecha,"			+
			"hora_inicio,"		+
			"hora_fin,"			+
			"codigo_medico,"	+
			"tiempo_sesion,"	+
			"cupos,"			+
			"usuario,"			+
			"activo," +
			"centro_atencion"+
		")"						+
		"VALUES"				+
		"(?,?,?,?,?,?,?,?,?,?,true,?)";

	/**
	* Cadena constante con el <i>Statement</i> necesario para saber si 
	* existía previamente un item de agenda en una base de datos 
	* PostgreSQL
	*/
	private static final String existeAgenda =
	//"SELECT count(1) as numResultados from agenda where unidad_consulta=? and consultorio=? and fecha=? and hora_inicio=?";
	"SELECT a.activo as activo from agenda a where a.unidad_consulta=? AND a.consultorio=? AND a.fecha=? AND " +
		"((a.hora_inicio <= ? AND a.hora_fin >= ?) OR (a.hora_inicio <= ? AND a.hora_fin >= ?)) AND ((a.fecha=CURRENT_DATE AND a.hora_inicio>=CURRENT_TIME)OR a.fecha>CURRENT_DATE) " +		
		"  UNION " +		
		"SELECT ag.activo as activo  " +
			"FROM  agenda ag " +
			"WHERE  " +			
			"ag.codigo_medico=? "+
			"AND ag.fecha=? " +
			"AND ((ag.hora_inicio <= ? " +
			"			AND ag.hora_fin >= ?) OR (ag.hora_inicio <= ? AND ag.hora_fin >= ?)) " +
			"			AND((ag.fecha=CURRENT_DATE AND ag.hora_inicio>=CURRENT_TIME)OR ag.fecha>CURRENT_DATE)";
	
	
	
	/**
	* Cadena constante con el <i>Statement</i> necesario para listar ítems de agenda de consulta
	* desde una base de datos PostgreSQL
	*/
	private static final String is_listar =
		"SELECT "	+	"a.codigo "				+ "AS codigo,"					+
						"TEXT(a.fecha)"			+ "AS fecha,"					+
						"a.hora_inicio "		+ "AS horaInicio,"				+
						"a.hora_fin "			+ "AS horaFin,"					+
						"a.cupos "				+ "AS cupos,"					+
						"getnombrediasemana(a.dia) AS dia,"					+
						"c.descripcion "		+ "AS nombreConsultorio,"		+
						"m.ocupacion_medica "	+ "AS ocupacionMedica,"			+
						"case when a.codigo_medico is null then ' ' else getnombrepersona(a.codigo_medico) end as nombremedico,"	+						
						"true "					+ "AS esPos,"					+
						"0 " 					+ "AS codigo_sexo,"				+
						"uc.codigo "			+ "AS codigoUnidadConsulta,"	+
						"uc.descripcion "		+ "AS nombreUnidadConsulta,"	+
						"-1 "					+ "AS codigoServicio, "			+
						"c.centro_atencion AS codigoCentroAtencion," +
						"getnomcentroatencion(c.centro_atencion) AS nombreCentroAtencion," +
						"a.codigo_medico AS codigomedico," +
						"a.consultorio," +
						"'' AS servicioscita "			+
						"FROM "		+	"agenda a "	+
						"INNER JOIN unidades_consulta uc ON(uc.codigo = a.unidad_consulta) "		+
						"INNER JOIN consultorios c ON(c.codigo=a.consultorio) "			+
						"LEFT OUTER JOIN medicos m USING(codigo_medico) " ;

	
	
	/**
	* Elimina los items de agenda de una fuente de datos, reutilizando una conexión existente
	* @param ac_con				Conexión abierta con una fuente de datos
	* @param as_fechaInicio		Fecha de inicio de la agenda a eliminar
	* @param as_fechaFin		Fecha de finalización de la agenda a eliminar
	* @param as_horaInicio		Hora de inicio que se eliminará de la agenda
	* @param as_horaFin			Hora de finalización que se eliminará de la agenda
	* @param ai_unidadConsulta	Unidad de consulta que se eliminará de la agenda. Si es menor que 0
	*							serán eliminados todos los ítems de agenda que coincidan con los
	*							otros criterios
	* @param ai_consultorio		Consultorio que se eliminará de la agenda. Si es menor que 0 serán
	*							eliminados todos los ítems de agenda que coincidan con los otros
	*							criterios
	* @param ai_diaSemana		Día de la semana que se eliminará de la agenda. Si es menor que 0
	*							serán eliminados los items de agenda que coincidan cn los otros
	*							criterios
	* @param ai_codigoMedico	Código del médico que se eliminará de la agenda. Si es vacia serán
	*							eliminados todos los ítems de agenda que coincidan con los demás
	*							criterios
	*@param centroAtencion Código del centro de atención que se eliminará de la agenda. Si es vacia serán
	*							eliminados todos los ítems de agenda que coincidan con los demás
	*							criterios
	* @return Número de items de agenda eliminados
	*/
	public HashMap cancelarAgenda(
		Connection	ac_con,
		String		as_fechaInicio,
		String		as_fechaFin,
		String		as_horaInicio,
		String		as_horaFin,
		int			ai_unidadConsulta,
		int			ai_consultorio,
		int			ai_diaSemana,
		int			ai_codigoMedico,
		int 		centroAtencion,
		String centrosAtencion,
		String unidadesAgenda
	)throws Exception
	{
		boolean				lb_continuar;
		DaoFactory			ldf_df;
		int					li_cancelados;
		PreparedStatementDecorator	lps_consultar;
		PreparedStatementDecorator	lps_cancelarCita;
		PreparedStatementDecorator	lps_cancelar;
		ResultSetDecorator			lrs_rs;
		StringBuffer		lsb_sb;

		/* Obtener una instancia del objeto principal de acceso a fuente de datos */
		ldf_df = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		/* Verificar el estado de la conexión y abrir una nueva si es necesario */
		if(ac_con == null || ac_con.isClosed() )
			ac_con = ldf_df.getConnection();

		/* Iniciar transacción */
		lb_continuar	= ldf_df.beginTransaction(ac_con);
		li_cancelados	= 0;
		lsb_sb			= new StringBuffer();
		HashMap respuesta = new HashMap();

		/* Preparar la cancelación de agenda */
		if(lb_continuar)
		{
			/* Obtener los códigos de los ítems de agenda a eliminar */
			lps_consultar =
				new PreparedStatementDecorator(ac_con.prepareStatement(
					is_consultarAEliminar +
					restringirConsulta(
						ii_CANCELAR,
						as_horaInicio,
						as_horaFin,
						ai_unidadConsulta,
						ai_consultorio,
						ai_diaSemana,
						ai_codigoMedico,
						centroAtencion,
						centrosAtencion,
						unidadesAgenda
					),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet)
				);

			/* Establecer los atributos de la búsqueda */
			lps_consultar.setString(1, UtilidadFecha.conversionFormatoFechaABD(as_fechaInicio) );
			lps_consultar.setString(2, UtilidadFecha.conversionFormatoFechaABD(as_fechaFin) );

			lrs_rs = new ResultSetDecorator(lps_consultar.executeQuery());


			lsb_sb.append("IN (");

			while(lrs_rs.next() )
				lsb_sb.append(lrs_rs.getInt(1) + ",");

			lsb_sb.append("-1)");

			/*
				Actualizar las citas de que referencian a alguno de los ítems de agenda a cancelar
			*/
			lps_cancelarCita = new PreparedStatementDecorator(ac_con.prepareStatement(is_cancelarCita + lsb_sb.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet) );
			lps_cancelarCita.executeUpdate();

			/* Cancelar los ítems de agenda */
			lps_cancelar = new PreparedStatementDecorator(ac_con.prepareStatement(is_eliminar + lsb_sb.toString() ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			li_cancelados = lps_cancelar.executeUpdate();
			
			/* Consultar Agenda que no está ligada a citas  */
			String consulta = consultaAgendaACancelarStr + " a.codigo "+lsb_sb.toString()+" AND ci.codigo IS NULL ";
			lps_cancelar = new PreparedStatementDecorator(ac_con.prepareStatement(consulta ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			respuesta = UtilidadBD.cargarValueObject(new ResultSetDecorator(lps_cancelar.executeQuery()), true, true);
			
			/*Se elimina agenda que no está ligada a citas*/
			int numRegistros = Integer.parseInt(respuesta.get("numRegistros").toString());
			for(int i=0;i<numRegistros;i++)
			{
				lps_cancelar = new PreparedStatementDecorator(ac_con.prepareStatement(eliminarAgendaACancelarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				lps_cancelar.setObject(1, respuesta.get("codigoAgenda_"+i));
				lps_cancelar.executeUpdate();
			}
			
			/* Verificar que se haya cancelado mas de un item de agenda */
			lb_continuar = (li_cancelados > -1);
		}
		else
			respuesta.put("numRegistros", "0");

		/* Terminar la transacción */
		if(lb_continuar)
			ldf_df.endTransaction(ac_con);
		else
		{
			ldf_df.abortTransaction(ac_con);
			li_cancelados = 0;
		}
		
		respuesta.put("cancelados", li_cancelados+"");
		
		/* Ejecutar la eliminación */
		return respuesta;
	}

	/**
	* Obtiene los datos de un ítem de agenda, reutilizando una conexión existente
	* @param ac_con		Conexión abierta con una fuente de datos
	* @param ai_codigo	Código único del ítem de agenda a consultar
	* @return Datos del ítem de agenda de consultas solicitado
	*/
	public HashMap detalleAgenda(Connection ac_con, int ai_codigo)throws Exception
	{
		HashMap			ldb_db;
		List				ll_l;
		PreparedStatementDecorator	lps_ps;

		/* Verificar el estado de la conexión y abrir una nueva si es necesario */
		if(ac_con == null || ac_con.isClosed() )
			ac_con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();

		/* Preparar la consulta sobre el ítem de agenda */
		lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(is_detalle,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		/* Establecer los atributos de la búsqueda */
		lps_ps.setInt(1, ai_codigo);

		/* Obtener el conjunto solución de la búsqueda */
		ll_l = (List)UtilidadBD.resultSet2Collection(new ResultSetDecorator(lps_ps.executeQuery()) );

		/* El conjunto solución solo puede tener un elemento */
		if(ll_l.size() == 1)
			ldb_db = (HashMap) ll_l.get(0);
		else
			ldb_db = null;

		return ldb_db;
	}
	
	
	//************************************************************************************************
	//************************************************************************************************
	//************************************************************************************************
	//************************************************************************************************

	/**
	* Genera un agenda en una fuente de datos, reutilizando una conexion existente.
	* @param ac_con				Conexión abierta con una fuente de datos
	* @param as_fechaInicio		Fecha de inicio de la agenda a generar
	* @param as_fechaFin		Fecha de finalización de la agenda a generar
	* @param ai_unidadConsulta	Unidad de consulta sobre la cual se generará la agenda. Si es menor
	*							que 0 la agenda será generada para todas las unidades de consulta
	*							con horarios de atención definidos
	* @param ai_consultorio		Consultorio sobre el cual se generará la agenda. Si es menor que 0
	*							la agenda será generada para todos los consultorios con horarios de
	*							atención definidos
	* @param ai_diaSemana		Día de la semana sobre el cual se generará la agenda. Si es menor
	*							que 0 la agenda será generada para todos los diás de la semana con
	*							horarios de atención definidos
	* @param ai_codigoMedico	Código del médico sobre el cual se generará la agenda. Si es vacia
	*							la agenda será generada para todos los médicos con horarios de
	*							atención definidos
	* @param as_codigoUsuario	Identificación del usuario que genera la agenda
	* @param centroAtencion
	* @return Número de items de agenda generados
	*/
	public InfoDatosInt generarAgenda(
										Connection	ac_con,
										String		as_fechaInicio,
										String		as_fechaFin,
										int			ai_unidadConsulta,
										int			ai_consultorio,
										int			ai_diaSemana,
										int			ai_codigoMedico,
										String		as_codigoUsuario,
										int 		centroAtencion,
										HashMap		horaAtencionConMap,
										String		centrosAtencion,
										String		unidadesAgenda
									)	throws Exception
	{		
		//Variables---------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------		
		
		int					li_errores;
		int					li_resp;
		int 				numRegistros;
		long				ll_fechaFin;
		long				ll_fechaInicio;
		SimpleDateFormat	lsdf_fechas;
		StringBuffer		lsb_fechas;
		String	[]			traslapeHorarios={"","","","",""}; 			
		
		boolean horaYaPaso=false;
		boolean horarioNoCruza=false, existeCruce = false;
		String comentarioCruces = ""; //variable para llenar las descripciones de los cruces

		/* Inicializar el número de errores encontrados */
		li_errores = 0;

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
		
		//------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------

		
		//------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------
		
		
		/* Obtener el conjunto de fechas sobre las cuales se ha de generar la agenda */	
		for(li_resp=0; UtilidadFecha.esFechaMenorIgualQueOtraReferencia(as_fechaInicio, as_fechaFin);li_resp++)
		{
			lsb_fechas.append(
					"SELECT DATE '"											+
					UtilidadFecha.conversionFormatoFechaABD(as_fechaInicio)	+
					"' AS fecha"
				);
			
			logger.warn("nueva fecha inicio: "+as_fechaInicio);
			if(UtilidadFecha.esFechaMenorQueOtraReferencia(as_fechaInicio, as_fechaFin))
				lsb_fechas.append(" UNION ");
			
			as_fechaInicio = UtilidadFecha.incrementarDiasAFecha(as_fechaInicio, 1, false);
		}
					
		/* Procesar la información sí y solo sí hay una fecha o mas en el rango de fechas */
		if(li_resp > 0)
		{
			PreparedStatementDecorator	lps_consulta;
			HashMap				lrs_consulta;
			StringBuffer				ls_generar=new StringBuffer();

			/* Verificar el estado de la conexión y abrir una nueva si es necesario */
			if(ac_con == null || ac_con.isClosed() )
				ac_con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			
			/*
				Generar la consulta para obtener los horarios de atención necesarios para generar la
				agenda
			*/
			ls_generar = SqlBaseAgendaDao.generarConsultaHorariosGeneracionAgenda(centroAtencion,lsb_fechas,ai_unidadConsulta,ai_consultorio,ai_diaSemana,ai_codigoMedico,centrosAtencion,unidadesAgenda,DaoFactory.POSTGRESQL);
						
			/* Preparar la consulta sobre los horarios de atención */
			lps_consulta = new PreparedStatementDecorator(ac_con.prepareStatement(ls_generar.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info("Cadena de consulta de  generar agenda >> "+ls_generar.toString());			

			/* Obtener los horarios de atención necesarios para generar la agenda */
			lrs_consulta = UtilidadBD.cargarValueObject(new ResultSetDecorator(lps_consulta.executeQuery()));		
						
			//completa los consultorios a los horarios de atencion que no lo tengan			
			if(horaAtencionConMap!=null){
				logger.info("valor del mapa POSTGRES >> "+horaAtencionConMap.get("consultorio_0"));
				lrs_consulta = completarHorarios(horaAtencionConMap,lrs_consulta);
			}			

			/* Verificar si existen horarios de atención para las fechas especificadas */			
			if(!lrs_consulta.get("numRegistros").equals("0"))
			{		
				
				int					li_tiempoSesion;
				long				ll_horaInicio;
				long				ll_horaFin;
				long				horaActual;
				PreparedStatementDecorator	lps_insercion;
				SimpleDateFormat	lsdf_horas;
				
				
				

				/* Obtener todas las consultas en el rango de horas del horario atención */
				lsdf_horas	= new SimpleDateFormat("H:mm");

				/* Exije una interpretación estricta del formato de hora esperado */
				lsdf_horas.setLenient(false);
				
				/* Inicializar el número de ítems encontrados */
				li_resp = 0;
				
				/* Inicializa el array de Mensaje de error por traslape*/
				traslapeHorarios[0] = "";

				/*
					Preparar la sentencia de insericíón de ítems de agenda de consulta en la fuente
					de datos
				*/				
				lps_insercion = new PreparedStatementDecorator(ac_con.prepareStatement(is_insertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

				/* Establecer el usuario que generó la agenda */
				lps_insercion.setString(10, as_codigoUsuario);		

				PreparedStatementDecorator revision;
				ResultSetDecorator rs;
				
				numRegistros = Integer.parseInt(lrs_consulta.get("numRegistros").toString());
				
				//logger.info("valor del mapa >> "+lrs_consulta);
				
				/* Procesar cada registro de horario de atención obtenido */
				for(int i = 0; i < numRegistros; i++)			
				{
					//verifica que el consultorio sea diferente de Null
					if(!lrs_consulta.get("consultorio_"+i).equals("") 
							&& !lrs_consulta.get("consultorio_"+i).equals(ConstantesBD.codigoNuncaValido+""))
					{		
						
						//logger.info("consultorio >> "+lrs_consulta.get("consultorio_"+i)+" >> "+i);
						
						//**************VERIFICAR QUE NO HAYA CRUZE DE AGENDA POR CADA CUPO DEL HORARIO**************************
						/*
						Obtener las horas iniciales y finales del rengo de horas del horario de
						atención
						*/
						try
						{
							ll_horaFin		= lsdf_horas.parse(lrs_consulta.get("hora_fin_"+i).toString()).getTime();
							ll_horaInicio	= lsdf_horas.parse(lrs_consulta.get("hora_inicio_"+i).toString()).getTime();
							horaActual = lsdf_horas.parse(UtilidadFecha.getHoraActual()).getTime();
						}
						catch(ParseException lpe_e)
						{
							il_logger.warn(lpe_e);
	
							/*
								Error al generar intentar obtener las fechas para el rango de generación
								de ítems de agenda
							*/
							lpe_e.printStackTrace();
	
							/* Evitar la inserción de ítems de agenda para este horario de ateción */
							ll_horaFin		= -1;
							ll_horaInicio	= 1;
							horaActual = -1;
						}
						//si la hora fin del horario de atencion es menor que la hora actual y la fecha actual se encuentra entre los rangos
						//de la generacion de agenda, se dice que ya pasó el horario de atención
						if(ll_horaFin<=horaActual&&(UtilidadFecha.validarFechaRango(as_fechaInicio, as_fechaFin, UtilidadFecha.getFechaActual())))
							horaYaPaso=true;
						
						li_tiempoSesion = Integer.parseInt(lrs_consulta.get("tiempo_sesion_"+i).toString());
						
						existeCruce = false;
						/* Obtener el conjunto de fechas sobre las cuales se ha de generar la agenda */
						for(;ll_horaInicio <= ll_horaFin; ll_horaInicio += (60000 * li_tiempoSesion))
						{
							//validacion de la fecha final, la fecha final no debe superar el estipulado en el horario de atencion
							if((ll_horaInicio+(60000 * (li_tiempoSesion - 1))) <=  ll_horaFin)
							{
								///Verifica que el cupo del horario de atencion no se cruce con agendas ya generadas.
								horarioNoCruza = false;						
								lps_consulta.clearParameters();
								traslapeHorarios[0] = "";
								
								String cadena = "SELECT count(a.activo) as activo " +
								"from agenda a " +
								"where a.consultorio="+lrs_consulta.get("consultorio_"+i)+" " +
								"AND a.activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" "+
								"AND a.fecha= '"+lrs_consulta.get("fecha_"+i)+"' "+
								"AND (('"+UtilidadFecha.conversionFormatoHoraABD(ll_horaInicio)+"' >= a.hora_inicio  "+
								"	AND '"+UtilidadFecha.conversionFormatoHoraABD(ll_horaInicio)+"' <= a.hora_fin) " +
								"		OR ('"+UtilidadFecha.conversionFormatoHoraABD(ll_horaInicio + (60000 * li_tiempoSesion) -1)+"' >= a.hora_inicio " +
										" AND '"+UtilidadFecha.conversionFormatoHoraABD(ll_horaInicio + (60000 * li_tiempoSesion) -1)+"' <= a.hora_fin )) " +
								"AND ((a.fecha=CURRENT_DATE AND a.hora_inicio>=CURRENT_TIME) OR a.fecha>CURRENT_DATE) ";	
															
								lps_consulta = new PreparedStatementDecorator(ac_con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));						
								rs = new ResultSetDecorator(lps_consulta.executeQuery());
								
								//Si el conteo es mayor que cero el horario de atencion se cruza con los 
								//cupos de una agenda ya generados.						
								
								if(rs.next())
								{
									//logger.info("valor de rs >> "+rs.getInt(1));
									
									if(rs.getInt(1) > 0)
									{
										horarioNoCruza = true;
										traslapeHorarios[0] = lrs_consulta.get("consultorio_"+i)+"";
										traslapeHorarios[1] = lrs_consulta.get("descripcion_consultorio_"+i)+"";
										traslapeHorarios[2] = lrs_consulta.get("fecha_"+i)+"";
										traslapeHorarios[3] = UtilidadFecha.conversionFormatoHoraABD(ll_horaInicio)+"";
										traslapeHorarios[4] = UtilidadFecha.conversionFormatoHoraABD(ll_horaInicio + (60000 * li_tiempoSesion) -1)+"";								
									}
								}
								rs = null;
								//-----------------------------------------------------------------------------------------------
								
								if(!horarioNoCruza)
								{
									/* Establecer los atributos de texto de la cláusula de inserción */
									String fechaAgenda = lrs_consulta.get("fecha_"+i).toString();
									lps_insercion.setString(4,  fechaAgenda);
									
									if(!lrs_consulta.get("codigo_medico_"+i).toString().equals(""))
										lps_insercion.setString(7, lrs_consulta.get("codigo_medico_"+i).toString());
									else
										lps_insercion.setNull(7,Types.NULL);
				
									/* Establecer los atributos numéricos de la cláusula de inserción */
									lps_insercion.setObject(1, lrs_consulta.get("unidad_consulta_"+i));
									lps_insercion.setObject(2, lrs_consulta.get("consultorio_"+i));
									lps_insercion.setObject(3, lrs_consulta.get("dia_"+i));
									lps_insercion.setInt(8, li_tiempoSesion );
									lps_insercion.setObject(9, lrs_consulta.get("pacientes_sesion_"+i));
									lps_insercion.setObject(11, lrs_consulta.get("centro_atencion_"+i)); //---Centro atención
				
									
																																			
									boolean existe = false;
									rs=null;
									revision=new PreparedStatementDecorator(ac_con.prepareStatement(existeAgenda,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
									revision.setObject(1 , lrs_consulta.get("unidad_consulta_"+i));
									revision.setObject(2,  lrs_consulta.get("consultorio_"+i));
									revision.setString(3, fechaAgenda);
									/**
									 * Tanto la hora inicial como la hora final del horario de atencion deben ser validados
									 * contra la hora inicio y fin de las agendas ya generadas que cumplen con las mismas condiciones
									 */
									revision.setString(4,UtilidadFecha.conversionFormatoHoraABD(ll_horaInicio));
									revision.setString(5,UtilidadFecha.conversionFormatoHoraABD(ll_horaInicio));
									revision.setString(6,UtilidadFecha.conversionFormatoHoraABD(ll_horaInicio + (60000 * li_tiempoSesion) -1));
									revision.setString(7,UtilidadFecha.conversionFormatoHoraABD(ll_horaInicio + (60000 * li_tiempoSesion) - 1));
																
									//parametros del UNION 
									if(!lrs_consulta.get("codigo_medico_"+i).equals(""))
										revision.setObject(8,  lrs_consulta.get("codigo_medico_"+i));
									else
										revision.setObject(8, ConstantesBD.codigoNuncaValido);
									
									revision.setString(9, fechaAgenda);
									revision.setString(10,UtilidadFecha.conversionFormatoHoraABD(ll_horaInicio));
									revision.setString(11,UtilidadFecha.conversionFormatoHoraABD(ll_horaInicio));
									revision.setString(12,UtilidadFecha.conversionFormatoHoraABD(ll_horaInicio + (60000 * li_tiempoSesion) -1));
									revision.setString(13,UtilidadFecha.conversionFormatoHoraABD(ll_horaInicio + (60000 * li_tiempoSesion) - 1));
										
									//logger.info("existeAgenda >> "+existeAgenda+" unidadConsulta > "+lrs_consulta.get("unidad_consulta_"+i)+" consultorio >> "+lrs_consulta.get("consultorio_"+i)+" fechaAgenda >> "+fechaAgenda+" hora1 "+UtilidadFecha.conversionFormatoHoraABD(ll_horaInicio)+" hora2 "+UtilidadFecha.conversionFormatoHoraABD(ll_horaInicio)+" hora3 >> "+UtilidadFecha.conversionFormatoHoraABD(ll_horaInicio + (60000 * li_tiempoSesion) -1)+" hora4 >> "+UtilidadFecha.conversionFormatoHoraABD(ll_horaInicio + (60000 * li_tiempoSesion) - 1)+" codigo medico >> "+lrs_consulta.get("codigo_medico_"+i));
									
									
									try{
										rs=new ResultSetDecorator(revision.executeQuery());
									}
									catch(SQLException e)
									{
										il_logger.warn("Error buscando agenda existente: "+e);
									}
										
										
									if(rs.next())
										if(rs.getBoolean("activo"))
											existe = true;
									
									if (!existe)
									{
										/* Establecer la hora de inicio de cada sesión */
										lps_insercion.setString(
											5,
											UtilidadFecha.conversionFormatoHoraABD(ll_horaInicio)
										);
										lps_insercion.setString(
											6,
											UtilidadFecha.conversionFormatoHoraABD(
												ll_horaInicio + (60000 * li_tiempoSesion) -1
											)
										);
			
										try
										{
											// Fecha actual y patrón de fecha a utilizar en las validaciones
											String fechaAgendaApp = UtilidadFecha.conversionFormatoFechaAAp(fechaAgenda);							
											final Date fechaActualDate = new Date();
											final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");							
											boolean insertar = false;							
					
											// Valida que la fecha de ejecución tenga el formato de fecha establecido
											Date fechaAgendaDate = null;
											try 
											{
												String fechaActualStr = UtilidadFecha.getFechaActual();
												fechaAgendaDate = dateFormatter.parse(fechaAgendaApp);
											
												// Valida que la fecha sea igual
												if ( fechaActualStr.equals(fechaAgendaApp) ) 
												{
													final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd/MM/yyyy:HH:mm");
													Date fechaHoraAgendaDate = dateTimeFormatter.parse(fechaAgendaApp + ":" + UtilidadFecha.conversionFormatoHoraABD(ll_horaInicio));
													Date fechaActualDate2 = dateTimeFormatter.parse(UtilidadFecha.getFechaActual()+":"+UtilidadFecha.getHoraActual());
												
													int fha = fechaActualDate2.compareTo(fechaHoraAgendaDate);									
												
													if( fha < 0 )	
													{
														insertar = true;
													}
													else
													{
														insertar = false;
													}
												}
												else
												if( fechaActualDate.compareTo(fechaAgendaDate) < 0 )
													insertar = true;
											}	
											catch (java.text.ParseException e) 
											{
												insertar = false;
											}
										
											if( insertar )
											{
												/* Ejecutar la inserción del ítem de agenda */
												lps_insercion.executeUpdate();
												li_resp++;
											}
										}
										catch(SQLException lse_e)
										{
											/* Se presentó un error al insetar el ítem de agenda */
											il_logger.warn(lse_e);
											/* Incrementar el número de errores encontrados */
											li_errores++;
										}
									}
									else
									{
										li_errores++;
										
										/**PreparedStatementDecorator actualizarAgendaStm=new PreparedStatementDecorator(ac_con.prepareStatement(this.activarAgenda,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
										actualizarAgendaStm.setInt(1, lrs_consulta.getInt(9) );
										actualizarAgendaStm.setString(2, as_codigoUsuario );
										actualizarAgendaStm.setString(3,lrs_consulta.getString(7));
										actualizarAgendaStm.setInt(4, lrs_consulta.getInt(1) );
										actualizarAgendaStm.setInt(5, lrs_consulta.getInt(2) );
										actualizarAgendaStm.setString(6, fechaAgenda);
										actualizarAgendaStm.setString(7,UtilidadFecha.conversionFormatoHoraABD(ll_horaInicio));
										actualizarAgendaStm.executeUpdate();
										li_resp++;
										**/	
									}
									
								}
								else if(horarioNoCruza&& !traslapeHorarios[0].equals(""))
									existeCruce = true;
								
								
								//---------------------------------------------------------------------------------------------------
							}
							else
							{
								logger.info("\n\n//--------------");
								logger.info("valor del horario de atención no tomado en cuenta para la generación de la agenda, excede el limite del horario de atencion >> \nhorario inicio del nuevo cupo. \nhorario fin maximo  >> "+UtilidadFecha.conversionFormatoHoraABD(ll_horaFin)+" tiempo sesion >> "+li_tiempoSesion+" \ndato del cupo invalido. hora inicio >> "+UtilidadFecha.conversionFormatoHoraABD(ll_horaInicio)+" hora fin >> "+UtilidadFecha.conversionFormatoHoraABD(ll_horaInicio+(60000 * li_tiempoSesion)));
								logger.info("//--------------\n\n");
							}
						} //Fin FOR iteracion de las horas inicio - hora fin
						
						
						if(existeCruce)
							comentarioCruces += (comentarioCruces.equals("")?"":ConstantesBD.separadorSplit) + lrs_consulta.get("descripcion_consultorio_"+i) + ". Fecha: "+UtilidadFecha.conversionFormatoFechaAAp(lrs_consulta.get("fecha_"+i)+"")+". Horario: "+UtilidadFecha.convertirHoraACincoCaracteres(lrs_consulta.get("hora_inicio_"+i)+"")+" a "+UtilidadFecha.convertirHoraACincoCaracteres(lrs_consulta.get("hora_fin_"+i)+"")+" ";
						
						
						
						//*********************************************************************************************
					}
				} //Fin FOR iteracion de horarios de atencion
	
				/* Liberar recursos */
				lps_insercion.close();
				lps_insercion = null;				
			}
			/* No existen horarios de atención que cumplan todos los parametros especificados */
			else
			{
				logger.info("---------> PASA POR AK 6 <---------");
				
				//Construir consulta para verificar si alguna de las fechas está dentro de las excepciones de agenda
				
				String consultaExcepcionesAgenda="SELECT f.fecha AS fecha from ( ";
				//-----Si el centro de atención es diferente de todos se realiza el filtro en la excepción de agenda ---//
				if(centroAtencion!=ConstantesBD.codigoNuncaValido)
					consultaExcepcionesAgenda+=lsb_fechas.toString()+")f WHERE f.fecha IN (SELECT fecha FROM excepciones_agenda WHERE centro_atencion="+centroAtencion+")";
				else
					consultaExcepcionesAgenda+=lsb_fechas.toString()+")f WHERE f.fecha IN (SELECT fecha FROM excepciones_agenda WHERE centro_atencion IN ("+centrosAtencion+"))";
				
				PreparedStatementDecorator	ps_excepciones;
				ResultSetDecorator rs_excepciones;
				
				ps_excepciones = new PreparedStatementDecorator(ac_con.prepareStatement(consultaExcepcionesAgenda,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				rs_excepciones=new ResultSetDecorator(ps_excepciones.executeQuery());
				
				if(rs_excepciones.next())
				{
					rs_excepciones.beforeFirst();
					StringBuffer fechasExcepcion=new StringBuffer();
					
					int cont=0;
					while(rs_excepciones.next())
					{
						if(cont>0)
							fechasExcepcion.append(" , "+rs_excepciones.getString("fecha"));
						else
							fechasExcepcion.append(rs_excepciones.getString("fecha"));
						cont++;
					}
					
					rs_excepciones.close();
					ps_excepciones.close();
					rs_excepciones = null;
					ps_excepciones = null;
					//throw new Exception("error.agenda.noHorariosAtencion");
					return new InfoDatosInt(-2, fechasExcepcion.toString());
				}
				else
				{
					throw new Exception("error.agenda.noHorariosAtencion");
				}
			}

			/* Liberar recursos */			
			lps_consulta.close();
			lps_consulta = null;
			}
		else
			throw new Exception("error.agenda.rangoFechas");

		if(li_resp == 0)
		{
			//Si huubo comentario de cruces se envía
			if(!comentarioCruces.equals(""))
				return new InfoDatosInt(-4,comentarioCruces);
				
			
			/* Algún ítem de agenda no pudo ser generado */
			if(horaYaPaso)
				throw new Exception("error.agenda.horaFinalYaPaso");
			else
				throw new Exception("error.agenda.agendaTotalGenerada");
		}
		else if(li_errores > 0)
		{
			/* Algún ítem de agenda no pudo ser generado */
			return new InfoDatosInt(li_resp,"parcial");
		}
		return new InfoDatosInt(li_resp,"");
	}

	/**
	* Lista una agenda, desde una fuente de datos, reutilizando una conexión existente
	 * @param ai_nivelBusqueda	Indica el nivel de búsqueda que se requiere para el listado
	 * @param ai_sexoPaciente	Código del sexo del paciente para el cual se reservará la cita
	 * @param as_fechaInicio		Fecha de inicio de la agenda a listar
	 * @param as_fechaFin		Fecha de finalización de la agenda a listar
	 * @param as_horaInicio		Hora de inicio de la agenda a listar
	 * @param as_horaFin			Hora de finalización de la agenda a listar
	 * @param ai_unidadConsulta	Unidad de consulta de la agenda a listar. Si es menor que 0 serán
	*							listados todos los ítems de agenda que coincidan con los otros
	*							criterios
	 * @param ai_consultorio		Consultorio de la agenda a listar. Si es menor que 0 serán listados
	*							todos los ítems de agenda que coincidan con los otros criterios
	 * @param ai_diaSemana		Día de la semana de la agenda a listar. Si es menor que 0 serán
	*							listados los items de agenda que coincidan cn los otros criterios
	 * @param ai_codigoMedico	Código del médico de la agenda a listar. Si es vacia serán listados
	*							todos los ítems de agenda que coincidan con los demás criterios
	 * @param con				Conexión abierta con una fuente de datos
	 * @param as_codigoPaciente	Identificación del paciente para el cual se reservará la cita
	 * @param centroAtencion
	 * @return Datos del conjunto de items de agenda solicitados
	*/
	public Collection listarAgenda(
		Connection	con,
		int	ai_nivelBusqueda,
		int	ai_codigoPaciente,
		int	ai_sexoPaciente,
		String as_fechaInicio,
		String as_fechaFin,
		String as_horaInicio,
		String as_horaFin,
		int	ai_unidadConsulta,
		int	ai_consultorio,
		int	ai_diaSemana,
		int	ai_codigoMedico,
		int	institucion,
		int centroAtencion,
		boolean esReserva,
		boolean disponibles,
		String centrosAtencion,
		String unidadesAgenda
	)throws Exception
	{
		Collection			coleccion;
		PreparedStatementDecorator	pst;
		StringBuffer		consulta;

		/* Verificar el estado de la conexión y abrir una nueva si es necesario */
		if(con == null || con.isClosed() )
			con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();

		/* Preparar la consulta sobre la agenda */
		consulta = new StringBuffer();

		if(!as_fechaInicio.equals("") )
			consulta.append(
				" AND a.fecha >= '" + UtilidadFecha.conversionFormatoFechaABD(as_fechaInicio) + "'"
			);

		if(!as_fechaFin.equals("") )
			consulta.append(
				" AND a.fecha <= '" + UtilidadFecha.conversionFormatoFechaABD(as_fechaFin) + "'"
			);

		consulta.append(
			restringirConsulta(
				ii_LISTAR,
				as_horaInicio,
				as_horaFin,
				ai_unidadConsulta,
				ai_consultorio,
				ai_diaSemana,
				ai_codigoMedico,
				centroAtencion,
				centrosAtencion,
				unidadesAgenda
			)
		);
		consulta.append(" AND a.activo=true");
		if(ai_nivelBusqueda == AgendaDao.LISTAR_CUPOS_DISPONIBLES)
		{
			
			
			if(esReserva&&disponibles)
				consulta.append(" AND a.cupos>0 ");
			
			String minutosCitaCaducada=ValoresPorDefecto.getMinutosEsperaCitaCaduca(institucion);
			if(minutosCitaCaducada.trim().equals(""))
			{
				minutosCitaCaducada="0";
			}
			
			String horaAVerificar="";
			if(esReserva)
			{
				horaAVerificar=ValoresPorDefecto.getSentenciaHoraActualBD();
			}
			else
			{
				horaAVerificar="(SELECT to_char((TIMESTAMP WITH TIME ZONE 'epoch' + (SELECT EXTRACT(EPOCH FROM CURRENT_TIME)-("+minutosCitaCaducada+"*60)) * INTERVAL '1 second'), 'HH24:MI'))";
			}
			
			consulta.append(" AND ((a.fecha=CURRENT_DATE AND substr(a.hora_inicio,0,6)||'' >= "+horaAVerificar+")OR a.fecha>CURRENT_DATE)");

			/*lsb_consulta.append(
				" AND a.codigo NOT IN("						+
					"SELECT "	+	"c.codigo_agenda "		+
					"FROM "		+	"cita c "				+
					"WHERE "	+	"c.codigo_paciente="	+ ai_codigoPaciente + " AND " +
									//"c.codigo_agenda IS NOT NULL)"
									"c.estado_cita<>"+ConstantesBD.codigoEstadoCitaAReprogramar+")"
			);*/

			/*if(ai_sexoPaciente != -1)
				lsb_consulta.append(" AND(s.sexo IS NULL OR s.sexo=" + ai_sexoPaciente + ")");*/
			
			
		}

		
		
		
		/* Eliminar el primer " AND " de la cunsulta si existe*/
		if(consulta.length() > 0)
			consulta.replace(1, 4, "WHERE");
		
		
		

		//consulta.append(" ORDER BY a.fecha, a.unidad_consulta, a.consultorio, a.hora_inicio");
		consulta.append(" ORDER BY a.fecha||'-'||a.hora_inicio, getnomcentroatencion(c.centro_atencion), uc.descripcion");
		
		Log4JManager.info("CONSULTA AGENDA : \n "+is_listar + consulta.toString());
		
		pst =  new PreparedStatementDecorator(con.prepareStatement(is_listar + consulta.toString() ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		
		/* Obtener el conjunto solución de la búsqueda */
		coleccion = UtilidadBD.resultSet2Collection(new ResultSetDecorator(pst.executeQuery()) );

		if(coleccion.size() < 1)
			coleccion = null;

		return coleccion;
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
	*@param centroAtencion sobre el cual se restringirá la búsqueda de horarios de
	*							atención o de ítems de agenda. Si es igual a -1 no habrá
	*							restricciones. De lo contrario se restringirán los registros que
	*							coincidan con este parámetro
	* @return Clausula WHERE con las retricciones necesarias
	*/
	private String restringirConsulta(
		int		ai_funcionalidad,
		String	as_horaInicio,
		String	as_horaFin,
		int		ai_unidadConsulta,
		int		ai_consultorio,
		int		ai_diaSemana,
		int		ai_codigoMedico,
		int 		centroAtencion,
		String 		centrosAtencion,
		String		unidadesAgenda
	)
	{
		String			ls_tabla;
		StringBuffer	lsb_where;
		
		lsb_where = new StringBuffer();

		/* Determinar desde que tabla se deben traer los datos */
		switch(ai_funcionalidad)
		{
			case ii_CANCELAR:
				ls_tabla = "a.";
				lsb_where.append(
					" AND((a.fecha=CURRENT_DATE AND a.hora_inicio>=CURRENT_TIME)OR a.fecha>CURRENT_DATE)"
				);
				break;
			case ii_GENERAR:
				ls_tabla = "ha.";
				lsb_where.append(
					"  AND fg.fecha>=CURRENT_DATE "
					);
				break;
			case ii_LISTAR:
				ls_tabla = "a.";
				break;
			default:
				ls_tabla = "";
		}

		/* Validar la hora de inicio */
		if(!as_horaInicio.equals("") )
			lsb_where.append(" AND " + ls_tabla + "hora_inicio>='" + as_horaInicio + "'");

		/* Validar la hora de finalización */
		if(!as_horaFin.equals("") )
			lsb_where.append(" AND " + ls_tabla + "hora_fin<='" + as_horaFin + "'");

		logger.info("///////////////////////////////////////////////////");
		logger.info("ai_unidadConsulta = "+ai_unidadConsulta);
		logger.info("unidadesAgenda = "+unidadesAgenda);
		
		
		/* Validar la unidad de consulta */
		if(ai_unidadConsulta > -1)
			lsb_where.append(" AND " + ls_tabla + "unidad_consulta=" + ai_unidadConsulta);
		else if(!unidadesAgenda.trim().equals("-1"))
			lsb_where.append(" AND " + ls_tabla + "unidad_consulta IN ("+unidadesAgenda+")");

		
		/* Validar el consultorio del horario de atención */
		if(ai_funcionalidad == ii_GENERAR)
		{
			if(ai_consultorio > -1)
				lsb_where.append(" AND (" + ls_tabla + "consultorio=" + ai_consultorio+" OR "+ls_tabla+"consultorio IS NULL) ");				
			else
				lsb_where.append(" AND (" + ls_tabla + "consultorio<>-1 OR "+ls_tabla+"consultorio IS NULL) ");				
		}
		else
		{
			if(ai_consultorio > -1)
				lsb_where.append(" AND " + ls_tabla + "consultorio=" + ai_consultorio);
			else
				lsb_where.append(" AND " + ls_tabla + "consultorio<>-1");
		}

		
		
		/* Validar el día de la semana del horario de atención */
		if(ai_funcionalidad == ii_GENERAR)
		{
			if(ai_diaSemana > -1)
				lsb_where.append(" AND fg.diaSemana=" + ai_diaSemana);
			else
				lsb_where.append(" AND fg.diaSemana<>-1");
		}
		else 
		{
			if(ai_diaSemana > -1)
				lsb_where.append(" AND "+ ls_tabla +"dia =" + ai_diaSemana);
			else
				lsb_where.append(" AND "+ ls_tabla +"dia <>-1");			
		}
		
		logger.info("///////////////////////////////////////////////////");
		logger.info("centroAtencion = "+centroAtencion);
		logger.info("centrosAtencion = "+centrosAtencion);
		
		/* Validar el centro de atención, para agregar filtro si es deferente de -1 */
		if(centroAtencion > -1 && ai_funcionalidad!=ii_GENERAR)
			lsb_where.append(" AND " + ls_tabla + "centro_atencion=" + centroAtencion);
		//-------Cuando es Generar el centro de atención se filtra por el consultorio ----------//
		else if (centroAtencion > -1 && ai_funcionalidad==ii_GENERAR)
			lsb_where.append(" AND c.centro_atencion=" + centroAtencion);
		else if (ai_funcionalidad==ii_GENERAR && centroAtencion==ConstantesBD.codigoNuncaValido)
			lsb_where.append(" AND c.centro_atencion IN (" + centrosAtencion + ")");
		

		/* Validar el código del médico del horario de atención */
		if(ai_codigoMedico == -2)
		{
			/* Generar agenda para los horarios de atención que no tienen asignado el médico */
			lsb_where.append(" AND " + ls_tabla + "codigo_medico IS NULL");
		}
		else if(ai_codigoMedico > -1)
		{
			lsb_where.append(" AND " + ls_tabla + "codigo_medico=" + ai_codigoMedico + "");
		}


		return lsb_where.toString();
	}
	
	public Collection listarCitasOcupadasAEliminar(Connection con, 
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
			return SqlBaseAgendaDao.listarCitasOcupadasAEliminar(con, 
					horaInicio, 
					horaFin, 
					fechaInicio, 
					fechaFin, 
					unidadConsulta,
					consultorio,
					diaSemana,
					codigoMedico,
					centroAtencion, 
					centrosAtencion,
					unidadesAgenda);
		}
	
		
	/**
	 * 
	 * @param con
	 * @param is_horaInicio
	 * @param is_horaFin
	 * @param is_fechaInicio
	 * @param is_fechaFin
	 * @param ii_unidadConsulta
	 * @param ii_consultorio
	 * @param ii_diaSemana
	 * @param ii_codigoMedico
	 * @param centroAtencion
	 * @return
	 */
	public HashMap listarCitasNoAtendidasAEliminar(Connection con,String horaInicio,String horaFin,String fechaInicio,String fechaFin,int unidadConsulta,int consultorio,int diaSemana,int codigoMedico,int centroAtencion, String centrosAtencion, String unidadesAgenda)
	{
		return SqlBaseAgendaDao.listarCitasNoAtendidasAEliminar(con,horaInicio,horaFin,fechaInicio,fechaFin,unidadConsulta,consultorio,diaSemana,codigoMedico,centroAtencion, centrosAtencion, unidadesAgenda);
	}


	
	/**
	 * Completa los horarios de atencion que no posean numero del consultorio
	 * @param HashMap horaAtencionConMap
	 * @param ResultSetDecorator lrs_consulta
	 * */
	public HashMap completarHorarios(HashMap horaAtencionConMap, HashMap lrs_consulta )
	{
		
		int numRegistros =  Integer.parseInt(lrs_consulta.get("numRegistros").toString());
		int numRegistros1 = Integer.parseInt(horaAtencionConMap.get("numRegistros").toString());
		
		try
		{
			for(int k = 0; k <numRegistros ; k++)			
			{				
				for(int i = 0; i<numRegistros1; i++)
				{		
					if(lrs_consulta.get("codigo_"+k).toString().trim().equals(horaAtencionConMap.get("codigo_"+i).toString().trim())
							&& !lrs_consulta.get("consultorio_"+k).equals(ConstantesBD.codigoNuncaValido+""))
					{						
						lrs_consulta.put("consultorio_"+k,horaAtencionConMap.get("consultorio_"+i));				
						lrs_consulta.put("descripcion_consultorio_"+k,horaAtencionConMap.get("descripcion_"+i));
						i = numRegistros;
					}						
				}				
												
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
		
		
		return lrs_consulta;
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
	 * */
	public HashMap getHorariosAtencionSinConsultorios(
													  Connection 	con,
													  String		as_fechaInicio,
													  String		as_fechaFin,
													  int			ai_unidadConsulta,
													  int			ai_diaSemana,
													  int			ai_codigoMedico,															  
													  int 			centroAtencion,
													  String 		unidadesAgenda,
													  String		centrosAtencion
													 )
	{
		return SqlBaseAgendaDao.getHorariosAtencionSinConsultorios(con,
																   as_fechaInicio, 
																   as_fechaFin, 
																   ai_unidadConsulta, 
																   ai_diaSemana, 
																   ai_codigoMedico, 
																   centroAtencion,
																   unidadesAgenda,
																   centrosAtencion,DaoFactory.POSTGRESQL);
	}	
}