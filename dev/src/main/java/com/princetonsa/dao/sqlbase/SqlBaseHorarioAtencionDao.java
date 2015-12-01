/*
 * @(#)SqlBaseHorarioAtencionDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoHorarioAtencion;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatos;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;

/**
 * Esta clase implementa la funcionalidad com�n a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL est�ndar. M�todos particulares a Horario de Atenci�n
 *
 *	@version 1.0, Apr 1, 2004
 */
public class SqlBaseHorarioAtencionDao 
{
	
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseHorarioAtencionDao.class);

	/**
	* Cadena constante con el <i>Statement</i> necesario para cargar un horario de atenci�n en una
	* base de datos Gen�rica
	*/
	private static final String is_cargar ="SELECT DISTINCT (ha.codigo) AS codigo, " +
										   "ha.unidad_consulta AS codigoUnidadConsulta, " +										   
										   "CASE WHEN ha.consultorio IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE ha.consultorio END AS codigoConsultorio,"+
										   "CASE WHEN ha.dia IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ElSE ha.dia END AS codigoDiaSemana,"			+						
										   "CASE WHEN ha.codigo_medico IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE ha.codigo_medico END AS codigoMedico,"+									   										   
										   "ha.hora_inicio AS horaInicio," +
										   "ha.hora_fin AS horaFin, " +
										   "ha.tiempo_sesion AS tiempoSesion, " +
										   "ha.pacientes_sesion AS pacientesSesion, " +
										   "uc.descripcion AS nombreUnidadConsulta, " +
										   "CASE WHEN ha.consultorio IS NULL THEN '' ELSE c.descripcion END AS nombreConsultorio, " +
										   "d.dia    AS nombreDiaSemana, " +
										   "p.primer_nombre AS primerNombreMedico, " +
										   "p.segundo_nombre AS segundoNombreMedico, " +
										   "p.primer_apellido AS primerApellidoMedico, " +
										   "p.segundo_apellido AS segundoApellidoMedico, " +
										   "ha.centro_atencion AS codigocentroatencion," +
										   "getnomcentroatencion(ha.centro_atencion) AS nombreCentroAtencion, " +
										   "uc.tipo_atencion AS tipoatencion " +
										   "FROM horario_atencion ha " +
										   "LEFT OUTER JOIN unidades_consulta uc ON(uc.codigo=ha.unidad_consulta) " +
										   "LEFT OUTER JOIN consultorios c ON(c.codigo=ha.consultorio) " +
										   "LEFT OUTER JOIN dias_semana d ON(d.codigo=ha.dia) " +
										   "LEFT OUTER JOIN medicos m ON (m.codigo_medico=ha.codigo_medico) " +
										   "LEFT OUTER JOIN personas p ON (p.codigo=m.codigo_medico) WHERE ha.codigo=?";

		/**
	* Cadena constante con el <i>Statement</i> necesario eliminar un horario de atenci�n en una base
	* de datos Gen�rica
	*/
	private static final String is_eliminar =
		"DELETE "	+
		"FROM "		+	"horario_atencion "	+
		"WHERE "	+	"codigo"	+ "=?";

	/**
	* Cadena constante con el <i>Statement</i> necesario para insertar un horario de atenci�n en una
	* base de datos Gen�rica
	*/
	private static final String is_insertarOra=	"INSERT INTO horario_atencion"	+
											 "( " +
											 "codigo,"			+
											 "centro_atencion,"			+
											 "unidad_consulta,"			+
											 "consultorio,"				+
											 "dia,"						+
											 "codigo_medico,"			+
											 "hora_inicio,"				+
											 "hora_fin,"					+
											 "tiempo_sesion,"			+
											 "pacientes_sesion"			+
											 ")"								+
											 "VALUES"						+
											 "(consultaexterna.seq_horario_atencion.NEXTVAL,?,?,?,?,?,?,?,?,?)";
	private static final String is_insertarPos=	"INSERT INTO horario_atencion"	+
	 "( " +
	 "codigo,"			+
	 "centro_atencion,"			+
	 "unidad_consulta,"			+
	 "consultorio,"				+
	 "dia,"						+
	 "codigo_medico,"			+
	 "hora_inicio,"				+
	 "hora_fin,"					+
	 "tiempo_sesion,"			+
	 "pacientes_sesion"			+
	 ")"								+
	 "VALUES"						+
	 "(nextval(('seq_horario_atencion'::text)::regclass),?,?,?,?,?,?,?,?,?)";
	
	
	//private static final String is_insertarPos=

	/**
	* Cadena constante con el <i>Statement</i> necesario para modificar un horario de atenci�n en
	* una base de datos Gen�rica
	*/
	private static final String is_modificar =
		"UPDATE "	+	"horario_atencion "			+
		"SET "		+	"unidad_consulta"	+ "=?,"	+
						"consultorio"		+ "=?,"	+
						"dia"				+ "=?,"	+
						"codigo_medico"		+ "=?,"	+
						"hora_inicio"		+ "=?,"	+
						"hora_fin"			+ "=?,"	+
						"tiempo_sesion"		+ "=?,"	+
						"pacientes_sesion"	+ "=? "	+
		"WHERE "	+	"codigo"			+ "=?";

		
	private static final String consultarEspecialidadUniAgen = "SELECT descripcion, activa, " +
			"coalesce(especialidad,"+ConstantesBD.codigoNuncaValido+") AS especialidad, tipo_atencion AS tipoatencion " +
			"FROM unidades_consulta WHERE codigo = ? ";
	
	/**
	 * Cadena que consulta el tipo de servicio de los servicios asociados a la unidad de consulta
	 */
	private static final String consultarTipoServiciosUnidadConsulta = "SELECT " +
			"s.tipo_servicio " +
			"FROM servicios s " +
			"WHERE codigo IN " +
			"(SELECT suc.codigo_servicio FROM servicios_unidades_consulta suc WHERE suc.unidad_consulta = ? ) ";
	
	/**
	 * Cadena para consultar los Horarios de Atencion
	 */
	private static String strConHorarioAtencion = "SELECT DISTINCT ha.codigo AS codigo, " +
			"getnomcentroatencion(ha.centro_atencion) AS nomCentroAt, "+ 
			"ha.centro_atencion AS codCentroAt, " +
			"ha.codigo_medico AS codmedico, " +
			"CASE WHEN ha.codigo_medico IS NULL THEN '"+ConstantesBD.codigoNuncaValido+"' ELSE getnombrepersona2(ha.codigo_medico) END AS nombremedico, "+ 
			"CASE WHEN ha.codigo_medico IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE m.convencion END AS convencion, " +
			"ha.unidad_consulta AS codUniA, " +
			"getnombreunidadconsulta(ha.unidad_consulta) AS nomUniA, "+ 
			"ua.color AS colorUniA, " +
			"ha.hora_inicio AS horainicio, " +
			"ha.hora_fin AS horafin, "+ 
			"ha.consultorio AS consultorio, "+
			"getnombreconsultorio(ha.consultorio) AS nomconsultorio, " +
			"ha.dia AS dia, " +
			"getnombrediasemana(ha.dia) AS nomdia "+
			"FROM consultaexterna.horario_atencion ha "+ 
			"INNER JOIN consultaexterna.unidades_consulta ua ON(ua.codigo=ha.unidad_consulta) "+
			"LEFT OUTER JOIN administracion.medicos m ON(m.codigo_medico=ha.codigo_medico) ";
	
	private static String strConHorarioAtencionWhere = "WHERE ha.centro_atencion=? ";
	
	/**
	 * Cadena para consultar los codigos  de los Horarios de Atencion
	 */
	private static String strConCodigosHorarioAtencion = "SELECT DISTINCT ha.codigo AS codigo, " +
															" ha.centro_atencion, ha.unidad_consulta, ha.consultorio " +
															"FROM consultaexterna.horario_atencion ha "+ 
															"INNER JOIN consultaexterna.unidades_consulta ua " +
															"ON(ua.codigo=ha.unidad_consulta) "+
															"LEFT OUTER JOIN administracion.medicos m " +
															"ON(m.codigo_medico=ha.codigo_medico) ";
	
	/**
	 * Cadena para consultar los centros de atencion validos para el usuario en sesion
	 */
	private static String strConCentroAtencionValidosXUsuario = "SELECT " +
																	"DISTINCT uauc.centro_atencion as codigo " +
																"FROM unid_agenda_usu_caten uauc " +
																"INNER JOIN unid_agenda_act_auto uaaa ON (uaaa.unid_agenda_usu_caten=uauc.codigo) " +
																"WHERE uauc.usuario_autorizado = ? " +
																	"AND uaaa.actividad_autorizada = ? ";
	
	/**
	 * Cadena para consultar las unidades de agenda valdias para el usuario en sesion
	 */
	private static String strConUnidadesAgendaValidasXUsuario = "SELECT DISTINCT " +
																	"uauc.unidad_agenda as codigo " +
																"FROM unid_agenda_usu_caten uauc " +
																	"INNER JOIN unid_agenda_act_auto uaaa ON (uaaa.unid_agenda_usu_caten=uauc.codigo) " +
																	"INNER JOIN unidades_consulta uc ON (uauc.unidad_agenda=uc.codigo) " +
																"WHERE " +
																	"uauc.usuario_autorizado=? " +
																	"AND uaaa.actividad_autorizada=? " +
																	"AND uauc.centro_atencion=? " ;
	
	
	public static ArrayList<DtoHorarioAtencion> consultarHA(HashMap<String, Object> parametros)
	{
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs= null;
		
		ArrayList<DtoHorarioAtencion> lista = new ArrayList<DtoHorarioAtencion>();
		
		Connection con;		
		con= UtilidadBD.abrirConexion();
		
		String consulta= strConHorarioAtencion + strConHorarioAtencionWhere;
 
		ArrayList<Integer> dias = (ArrayList<Integer>) parametros.get("dias");
		
		String where = "";
		String order = "";
		try{
			//Unidad de Agenda
			if(Integer.parseInt(parametros.get("unidadAgenda")+"") != ConstantesBD.codigoNuncaValido)
				where += " AND ha.unidad_consulta= "+Integer.parseInt(parametros.get("unidadAgenda")+"")+" ";
			
			//Con consultorio
			if((parametros.get("consultorioAsignado")+"").equals(ConstantesBD.acronimoSi)){
				where += " AND ha.consultorio IS NOT NULL ";
				if(Integer.parseInt(parametros.get("consultorio")+"") != ConstantesBD.codigoNuncaValido){
					where +=" AND ha.consultorio= "+Integer.parseInt(parametros.get("consultorio")+"")+" ";
				}
			} else if((parametros.get("consultorioAsignado")+"").equals(ConstantesBD.acronimoNo)){
				where += " AND ha.consultorio IS NULL ";
			}
			
			//Con profesional
			if((parametros.get("profesionalAsignado")+"").equals(ConstantesBD.acronimoSi)){
				where += " AND ha.codigo_medico IS NOT NULL ";
				if(Integer.parseInt(parametros.get("profesional")+"") != ConstantesBD.codigoNuncaValido){
					where +=" ha.codigo_medico = "+Integer.parseInt(parametros.get("profesional")+"")+" ";
				}
			} else if((parametros.get("profesionalAsignado")+"").equals(ConstantesBD.acronimoNo)){
				where += " AND ha.codigo_medico IS NULL";
			}
			
			if(Integer.parseInt(parametros.get("tipoReporte")+"") == 0){
				if(!dias.isEmpty())
					where +=" AND dia IN ("+UtilidadTexto.convertirArrayIntegerACodigosSeparadosXComas(dias)+") ";
					order +=" ORDER BY consultorio, horainicio ";
			}else{
				order +=" ORDER BY dia, horainicio ";	
			}
			
			consulta += where + order;
			
			ps =  new PreparedStatementDecorator(con,consulta);
			//centro de atencion
			ps.setInt(1, Integer.parseInt(parametros.get("centroAtencion")+""));
			logger.info("\n\nconsulta horarios atencion:::::"+ps);
			rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoHorarioAtencion dto = new DtoHorarioAtencion();
				dto.setCodigo(rs.getInt("codigo"));
				dto.setCentroAtencion(rs.getInt("codCentroAt"));
				dto.setNomCentroAtencion(rs.getString("nomCentroAt"));
				dto.setCodigoMedico(rs.getInt("codmedico"));
				dto.setNombreMedico(rs.getString("nombremedico"));
				dto.setConvencion(rs.getInt("convencion"));
				dto.setUnidadConsulta(rs.getInt("codUniA"));
				dto.setNombreUniAgenda(rs.getString("nomUniA"));
				dto.setColorUniAgenda(rs.getString("colorUniA"));
				dto.setHoraInicio(rs.getString("horainicio"));
				dto.setHoraFin(rs.getString("horafin"));
				dto.setConsultorio(rs.getInt("consultorio"));
				dto.setNombreConsultorio(rs.getString("nomconsultorio"));
				dto.setDia(rs.getInt("dia"));
				dto.setNombreDia(rs.getString("nomdia"));
				lista.add(dto);
			}
			
		}catch (SQLException e){
            logger.error("ERROR SQLException consultarHA: ", e);
     
        }catch(Exception ex){
			logger.error("ERROR Exception consultarHA: ", ex);
		
        }finally{
			try{
				if(ps != null){
					ps.close();
				}	
				if(rs != null){
					rs.close();
				}	
				UtilidadBD.cerrarConexion(con);
			}catch (SQLException se) {
				logger.error("###########  Error close PreparedStatement", se);
			}
		}
		return lista;
	}
	
	/**
	* Dada la identificaci�n de un horario de atenci�n, carga los datos correspondientes desde la
	* fuente de datos.
	* @param ac_con		Conexi�n abierta con una fuente de datos
	* @param ai_codigo	Identificador �nico del horario de atenci�n
	* @return <code>HashMap</code> con los datos pedidos y una conexi�n abierta con la fuente de
	* datos
	*/
	public static HashMap  cargarHorarioAtencion(Connection ac_con, int ai_codigo)throws SQLException
	{
		PreparedStatementDecorator	lps_ps;
		ResultSetDecorator rs;
		
		
		/* Verificar el estado de la conexi�n y abrir una nueva si es necesario */
		if(ac_con == null || ac_con.isClosed() )
		{
			ac_con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
		}
		
		/* Preparar la consulta sobre el horario de atenci�n */
		//logger.info("Consulta: "+is_cargar);
		//logger.info("Codigo: "+ai_codigo);
		lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(is_cargar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		/* Establecer los atributos de la b�squeda */
		lps_ps.setInt(1, ai_codigo);
		HashMap mapa= new HashMap();
		/* Obtener el conjunto soluci�n de la b�squeda */
		  try
		  {
			  rs= new ResultSetDecorator(lps_ps.executeQuery());
			  mapa = UtilidadBD .cargarValueObject(rs, false, true);
		   }
		  catch( SQLException e)
		  {
		  logger.info("\n\n Error : "+e);
		  }
		  return mapa;
		}

	/**
	* Dada la identificaci�n de un horario de atenci�n, elimina el horario de atenci�n de la fuente
	* de datos.
	* @param ac_con		Conexi�n abierta con una fuente de datos
	* @param ai_codigo	Identificador �nico del horario de atenci�n
	* @return N�mero de horarios de atenci�n eliminados
	*/
	public static int eliminarHorarioAtencion(Connection ac_con, int ai_codigo)throws SQLException
	{
		PreparedStatementDecorator lps_ps;

		/* Verificar el estado de la conexi�n y abrir una nueva si es necesario */
		if(ac_con == null || ac_con.isClosed() )
			ac_con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();

		/* Preparar la consulta sobre el horario de atenci�n */
		lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(is_eliminar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		logger.info("is_eliminar..........>>>>>>>>>< "+is_eliminar);

		/* Establecer los atributos de la eliminaci�n */
		lps_ps.setInt(1, ai_codigo);

		/* Ejecutar la eliminaci�n */
		return lps_ps.executeUpdate();
	}

	/**
	* Dado un conjunto de identificaciones de horarios de atenci�n, elimina los horario de atenci�n
	* de la fuente de datos.
	* @param ac_con			Conexi�n abierta con una fuente de datos
	* @param aia_codigos	Conjunto de identificadores �nicos de los horario de atenci�n
	* @return N�mero de horarios de atenci�n eliminados
	*/
	public static int eliminarHorarioAtencion(Connection ac_con, int[] aia_codigos)throws SQLException
	{
		int li_resp;

		/* Ejecutar la eliminaci�n solo si existe m�s de un c�digo a eliminar */
		if(aia_codigos.length > 0)
		{
			PreparedStatementDecorator	lps_ps;
			StringBuffer		lsb_eliminar;

			/* Preparar la sentencia de eliminaci�n */
			lsb_eliminar = new StringBuffer(is_eliminar);

			for(int li_i = 0, li_tam = aia_codigos.length - 1; li_i < li_tam; li_i++)
				lsb_eliminar.append(" OR codigo=?");

			/* Verificar el estado de la conexi�n y abrir una nueva si es necesario */
			if(ac_con == null || ac_con.isClosed() )
				ac_con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();

			/* Preparar eliminaci�n de los horarios de atenci�n */
			lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(lsb_eliminar.toString() ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			/* Establecer los atributos de la eliminaci�n */
			for(int li_i = 0; li_i < aia_codigos.length; li_i++)
				lps_ps.setInt(li_i + 1, aia_codigos[li_i]);

			/* Ejecutar la eliminaci�n */
			li_resp = lps_ps.executeUpdate();
		}
		else
			li_resp = 0;

		/* Obtener el n�mero de registros eliminados */
		return li_resp;
	}

	/**
	* Inserta un horario de atenci�n en una fuente de datos, reutilizando una conexion existente.
	* @param ac_con					Conexi�n abierta con una fuente de datos
	* @param ai_unidadConsulta		Unidad de consulta a la que ser� asignado el horario de atenci�n
	* @param ai_consultorio			Consultorio al cual ser� asignado el horario de atenci�n
	* @param ai_diaSemana			D�a de la semana a la cual ser� asignado el horario de atenci�n
	* @param ai_codigoMedico		C�digo del m�dico al cual ser� asignado el horario de atenci�n
	* @param as_horaInicio			Hora de inicio del horario de atenci�n para el d�a de la semana
	*								especificado
	* @param as_horaFin				Hora de finalizaci�n del horario de atenci�n para el d�a de la
	*								semana especificado
	* @param ai_duracionConsulta	Tiempo de duraci�n (en minutos) de una consulta en este horario
	*								de atenci�n
	* @param ai_pacientesSesion		M�ximo n�mero de pacientes que se pueden atender por sesi�n de
	*								consulta
	 * @param Tipo_BD 
	* @return C�digo asignado al horario de atenci�n
	*/
	public static int insertarHorarioAtencion(
												Connection	ac_con,
												int			ai_centroAtencion,
												int			ai_unidadConsulta,
												int			ai_consultorio,
												int			ai_diaSemana,
												int			ai_codigoMedico,
												String		as_horaInicio,
												String		as_horaFin,
												int			ai_duracionConsulta,
												int			ai_pacientesSesion, int Tipo_BD
											)throws SQLException
	{
		boolean				lb_continuar;
		DaoFactory			ldf_df;
		int					li_codigo;

		/* Obtener una instancia del objeto principal de acceso a fuente de datos */
		ldf_df = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		/* Verificar el estado de la conexi�n y abrir una nueva si es necesario */
		if(ac_con == null || ac_con.isClosed() )
			ac_con = ldf_df.getConnection();

		/* Iniciar una nueva transacci�n */
		lb_continuar = ldf_df.beginTransaction(ac_con);
		
		

		/* Validar si el horario de atenci�n especificado puede ser asignado */
		if(lb_continuar)
		{			
			InfoDatos resultado = validarHorarioAtencion(ac_con,ai_centroAtencion,-1,ai_unidadConsulta,ai_consultorio,ai_diaSemana,ai_codigoMedico,as_horaInicio,as_horaFin,false);
			switch(resultado.getCodigo())
			{
				/* El horario de atenci�n es v�lido */
				case 0:
					break;
				/* El horario de atenci�n interfiere con otro asignado para el mismo consultorio */
				case 1:
					/* Terminar la transacci�n */
					ldf_df.endTransaction(ac_con);
					throw new SQLException("El horario de atenci�n solicitado para este consultorio ya ha " +"sido asignado"+ConstantesBD.separadorSplit+resultado.getNombre());
				/* El horario de atenci�n interfiere con otro asignado para el mismo m�dico */
				case 2:
					/* Terminar la transacci�n */
					ldf_df.endTransaction(ac_con);
					throw new SQLException("El horario de atenci�n solicitado para este profesional ya ha sido " +"asignado"+ConstantesBD.separadorSplit+resultado.getNombre());
					/* El horario de atenci�n interfiere con otro asignado para el mismo m�dico */
				case 3:
					/* Terminar la transacci�n */
					ldf_df.endTransaction(ac_con);
					throw new SQLException("El horario de atenci�n solicitado para este d�a sin consultorio y sin profesional ya ha sido " +"asignado"+ConstantesBD.separadorSplit+resultado.getNombre());
			}
		}
		li_codigo = -1;
		/* Verificar si se pudo iniciar transacci�n */
		if(lb_continuar)
		{
			PreparedStatementDecorator lps_insertar;
			String is_insertar="";
switch(Tipo_BD)
{
case DaoFactory.ORACLE:
	is_insertar=is_insertarOra;
	break;
case DaoFactory.POSTGRESQL:
	is_insertar=is_insertarPos;
	break;
	default:
		break;

}
			/* Preparar la inserci�n del horario de atenci�n */			
			lps_insertar = new PreparedStatementDecorator(ac_con.prepareStatement(is_insertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
			lps_insertar.setInt(1,ai_centroAtencion);
			/* Establecer atributos num�ricos */
			lps_insertar.setInt(2, ai_unidadConsulta);
			if(ai_consultorio == ConstantesBD.codigoNuncaValido || ai_consultorio < 0)
				lps_insertar.setNull(3,Types.NULL);
			else
				lps_insertar.setInt(3, ai_consultorio);
			if(ai_diaSemana == ConstantesBD.codigoNuncaValido || ai_diaSemana < 0)
				lps_insertar.setNull(4,Types.NULL);
			else
				lps_insertar.setInt(4, ai_diaSemana);
			if(ai_codigoMedico<0)			
				lps_insertar.setNull(5, Types.INTEGER);			
			else			
				lps_insertar.setInt(5, ai_codigoMedico);
			/* Establecer atributos cadena */
			lps_insertar.setString(6, as_horaInicio);
			lps_insertar.setString(7, as_horaFin);
			/* Ejecutar la inserci�n */
			lps_insertar.setInt(8, ai_duracionConsulta);
			lps_insertar.setInt(9, ai_pacientesSesion);
		
			try			
			{
				lb_continuar = (lps_insertar.executeUpdate() == 1);
				
			}
			catch(SQLException e)
			{
				logger.error("ERROR Insertando el horario de atencion: " + e);
				return -1;
			}
		}
		/* Verificar si se pudo insertar el horario de atenci�n */
		if(lb_continuar)
		{
			
			PreparedStatementDecorator	lps_consultar;
			ResultSetDecorator			lrs_consultar;

			/* Preparar consulta */
			lps_consultar = new PreparedStatementDecorator(ac_con.prepareStatement("SELECT MAX(CODIGO) FROM horario_atencion",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			/* Ejecutar la consulta */
			lrs_consultar = new ResultSetDecorator(lps_consultar.executeQuery());

			/* Verificar si se obtuvo resultado de la consulta */
			if(lrs_consultar.next() )
				li_codigo = lrs_consultar.getInt(1);
		}
		
		/* Terminar la transacci�n */
		ldf_df.endTransaction(ac_con);
		return li_codigo;
	}

	
	
	
	
	/**
	* Lista los c�digo de los horario de atenci�n
	* @param ac_con Conexi�n abierta con una fuente de datos
	* @param centroAtencion
	* @return <code>Collection</code> con los c�digos de los horarios de atenci�n
	*/
	public static Collection listarCodigosHorarioAtencion(Connection ac_con, int centroAtencion, String unidadesAgenda, int institucion)throws SQLException
	{
		/**
		* Cadena constante con el <i>Statement</i> necesario para listar los c�digos de los horarios de
		* atenci�n en una base de datos Gen�rica
		*/
		String is_listar =
			"SELECT "	+	"ha.codigo "			+ "AS codigo "	+
			"FROM "		+	"horario_atencion ha "			+
			"LEFT OUTER JOIN  unidades_consulta uc ON (ha.unidad_consulta=uc.codigo)" +
			"LEFT OUTER JOIN consultorios con ON (ha.consultorio=con.codigo) " +
			"WHERE " +
			"ha.centro_atencion=? and uc.codigo IN ("+unidadesAgenda+") " +
			"and ( ha.codigo_medico NOT IN  (SELECT codigo_medico FROM medicos_inactivos WHERE codigo_institucion = "+institucion+") or (ha.codigo_medico IS NULL))" +
			"ORDER BY "						+
							"uc.descripcion,  "+
							"ha.dia, " +
							"ha.hora_inicio || '' " ;
		
		
		List				ll_l;
		PreparedStatementDecorator	lps_ps;

		/* Verificar el estado de la conexi�n y abrir una nueva si es necesario */
		if(ac_con == null || ac_con.isClosed() )
			ac_con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();

		/* Preparar la consulta sobre el horario de atenci�n */
		lps_ps = new PreparedStatementDecorator(ac_con,is_listar);
		lps_ps.setInt(1, centroAtencion);
		logger.info("\n\nla consulta de  horariooos::: "+lps_ps);
		/* Obtener el conjunto soluci�n de la b�squeda */
		ll_l = (List)UtilidadBD.resultSet2Collection(new ResultSetDecorator(lps_ps.executeQuery()) );

		/* El conjunto soluci�n solo puede tener un elemento */
		if(ll_l.size() < 1)
			ll_l = null;

		return (Collection)ll_l;
	}

	
	
	/**
	* Modifica un horario de atenci�n en una fuente de datos, reutilizando una conexion existente.
	* @param ac_con					Conexi�n abierta con una fuente de datos
	* @param ai_codigo				Identificador �nico del horario de atenci�n
	* @param ai_unidadConsulta		Unidad de consulta a la que ser� asignado el horario de atenci�n
	* @param ai_consultorio			Consultorio al cual ser� asignado el horario de atenci�n
	* @param ai_diaSemana			D�a de la semana a la cual ser� asignado el horario de atenci�n
	* @param ai_codigoMedico		C�digo del m�dico al cual ser� asignado el horario de atenci�n
	* @param as_horaInicio			Hora de inicio del horario de atenci�n para el d�a de la semana
	*								especificado
	* @param as_horaFin				Hora de finalizaci�n del horario de atenci�n para el d�a de la
	*								semana especificado
	* @param ai_duracionConsulta	Tiempo de duraci�n (en minutos) de una consulta en este horario
	*								de atenci�n
	* @param ai_pacientesSesion		M�ximo n�mero de pacientes que se pueden atender por sesi�n de
	*								consulta
	* @return n�mero de horarios de atenci�n modificados
	*/
	public static int modificarHorarioAtencion(
												Connection	ac_con,
												int 		ai_centroAtencion,
												int			ai_codigo,
												int			ai_unidadConsulta,
												int			ai_consultorio,
												int			ai_diaSemana,
												int			ai_codigoMedico,
												String		as_horaInicio,
												String		as_horaFin,
												int			ai_duracionConsulta,
												int			ai_pacientesSesion
											)throws SQLException
	{
		boolean				lb_continuar;
		DaoFactory			ldf_df;
		int					li_resp;
		PreparedStatementDecorator	lps_ps;

		/* Obtener una instancia del objeto principal de acceso a fuente de datos */
		ldf_df = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		/* Verificar el estado de la conexi�n y abrir una nueva si es necesario */
		if(ac_con == null || ac_con.isClosed() )
			ac_con = ldf_df.getConnection();

		/* Iniciar una nueva transacci�n */
		lb_continuar = ldf_df.beginTransaction(ac_con);

		/* Validar si el horario de atenci�n especificado puede ser asignado */
		if(lb_continuar)
		{
			InfoDatos resultado = validarHorarioAtencion
				(					
					ac_con,
					ai_centroAtencion,
					ai_codigo,
					ai_unidadConsulta,
					ai_consultorio,
					ai_diaSemana,
					ai_codigoMedico,
					as_horaInicio,
					as_horaFin,
					true
				);
			switch(resultado.getCodigo())
			{
				/* El horario de atenci�n interfiere con otro asignado para el mismo consultorio */
				case 1:
					ldf_df.endTransaction(ac_con);
					throw new SQLException("El horario de atenci�n solicitado para este consultorio ya ha " +"sido asignado"+ConstantesBD.separadorSplit+resultado.getNombre());
				/* El horario de atenci�n interfiere con otro asignado para el mismo m�dico */
				case 2:
					ldf_df.endTransaction(ac_con);
					throw new SQLException("El horario de atenci�n solicitado para este profesional ya ha sido " +"asignado"+ConstantesBD.separadorSplit+resultado.getNombre());
				/* El horario de atenci�n interfiere con otro asignado para el mismo d�a, sin consultorio y sin profesional */
				case 3:
					ldf_df.endTransaction(ac_con);
					throw new SQLException("El horario de atenci�n solicitado para este d�a sin consultorio y sin profesional ya ha sido " +"asignado"+ConstantesBD.separadorSplit+resultado.getNombre());
			}
		}
		if(lb_continuar)
		{
			/* Preparar la modificaci�n del horario de atenci�n */
			lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(is_modificar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("is_modificar.............>>>>>>> "+is_modificar);

			/* Establecer atributos num�ricos de la cl�usula SET */
			lps_ps.setInt(1, ai_unidadConsulta);
			
			
			if(ai_consultorio == ConstantesBD.codigoNuncaValido || ai_consultorio < 0)
				lps_ps.setNull(2,Types.NULL);
			else
				lps_ps.setInt(2, ai_consultorio);
			
			
			if(ai_diaSemana == ConstantesBD.codigoNuncaValido || ai_diaSemana < 0)
				lps_ps.setNull(3,Types.NULL);
			else
				lps_ps.setInt(3, ai_diaSemana);
				
			
			
			if(ai_codigoMedico<0)			
				lps_ps.setNull(4, Types.INTEGER);			
			else			
				lps_ps.setInt(4, ai_codigoMedico);
									
			
			/* Establecer atributos cadena de la cl�usula SET */
			lps_ps.setString(5, as_horaInicio);
			lps_ps.setString(6, as_horaFin);

			lps_ps.setInt(7, ai_duracionConsulta);
			lps_ps.setInt(8, ai_pacientesSesion);

			/* Establecer atributos num�ricos de la cl�usula WHERE */
			lps_ps.setInt(9, ai_codigo);


			li_resp = lps_ps.executeUpdate();
		}
		else
			li_resp = -1;

		/* Terminar la transacci�n */
		ldf_df.endTransaction(ac_con);

		/* Ejecutar la modificaci�n */
		return li_resp;
	}

	
	
	
	
	/**
	* Verifica si un horario de ateci�n ya ha sido asignado parcial o totalmente a un consultorio
	* y/o m�dico
	* @param ac_con				Conexi�n abierta con una fuente de datos
	* @param ai_consultorio		Consultorio al cual ser� asignado el horario de atenci�n
	* @param ai_diaSemana		D�a de la semana a la cual ser� asignado el horario de atenci�n
	* @param ai_codigoMedico	C�digo del m�dico al cual ser� asignado el horario de atenci�n
	* @param as_horaInicio		Hora de inicio del horario de atenci�n para el d�a de la semana
	*							especificado
	* @param as_horaFin			Hora de finalizaci�n del horario de atenci�n para el d�a de la
	*							semana especificado
	* @return 0 - si el horario de atenci�n no ha sido asignado<br>, 1 - si el horario de atenci�n
	* ya fue asignado al consultorio especificado, y 2 - si el horario de atenci�n ya fue asignado
	* al m�dico especificado.
	*/
	private static InfoDatos validarHorarioAtencion(Connection ac_con,
											  int ai_centroAtencion,
											  int ai_codigo,
											  int ai_unidadConsulta,
											  int ai_consultorio,
											  int ai_diaSemana,
											  int ai_codigoMedico,
											  String as_horaInicio,
											  String as_horaFin,
											  boolean ab_modificar)throws SQLException
	{
		int					li_resp;
		long				ll_conflictos = 0;
		ResultSetDecorator			lrs_rs;
		PreparedStatementDecorator	lps_ps;
		String				ls_consulta;
		String				unidadAgenda = "", temp0 = "";

		/*
			Preparar la consulta. Se asume que el horario de atenci�n no ha sido asignado parcial o
			totalmente
		*/
		li_resp	= 0;
		
		try
		{
		
			//----------------------------------------------------------------------------------------------
			//Busqueda de horarios de atencion con el consultorio 
			if(ai_consultorio != ConstantesBD.codigoNuncaValido)
			{		
				
				ls_consulta =	"SELECT getnombreunidadconsulta(unidad_consulta) AS unidad_agenda "+
								"FROM horario_atencion "+
								"WHERE  @ "+
								"('"+as_horaInicio+"' BETWEEN hora_inicio AND hora_fin OR "+
								"'"+as_horaFin+"' BETWEEN hora_inicio AND hora_fin OR "+
								"hora_inicio BETWEEN '"+as_horaInicio+"' AND '"+as_horaFin+"'"+
								") " +
								"AND consultorio="+ai_consultorio+" AND centro_atencion=? ";				
		
				if(ai_diaSemana != ConstantesBD.codigoNuncaValido)
					ls_consulta = ls_consulta.replaceFirst("@"," dia = "+ai_diaSemana+" AND ");
				else
					ls_consulta = ls_consulta.replaceFirst("@","");
					
					
				if(ab_modificar)
					ls_consulta += " AND codigo<>? ";
		
				
				logger.info("valor de la consulta >> "+ls_consulta);
				
				lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(ls_consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
				
				/* Establecer atributos cadena de la consulta */									
				lps_ps.setInt(1,ai_centroAtencion);
		
				if(ab_modificar)
					lps_ps.setInt(2, ai_codigo);
		
				/*
					Obtener el n�mero de horarios de atenci�n para el consultorio especificado que afecta
					este horario
				*/
				lrs_rs = new ResultSetDecorator(lps_ps.executeQuery());			
				
				ll_conflictos	= 0;
				while(lrs_rs.next())
				{	
					/* Este horario afecta a uno o mas horarios de atenci�n asignados al consultorio */
					ll_conflictos = 1;
					temp0 = lrs_rs.getString("unidad_agenda");
					if(unidadAgenda.indexOf(temp0)==ConstantesBD.codigoNuncaValido)
						unidadAgenda += (unidadAgenda.equals("")?"":", ") + temp0;
				}				
									
				
		
				if(ll_conflictos != 0)
					li_resp = 1;
			}		
			
			//----------------------------------------------------------------------------------------------
			//Busqueda de horarios de atencion con codigo del medico
			if(ai_codigoMedico != ConstantesBD.codigoNuncaValido)
			{
				
				if(li_resp == 0 )
				{				
					lrs_rs = null;				
					lps_ps = null;
		
					/* Preparar la consulta */
					ls_consulta =	"SELECT getnombreunidadconsulta(unidad_consulta) AS unidad_agenda "+
									"FROM horario_atencion "+
									"WHERE  @  "+
									"('"+as_horaInicio+"' BETWEEN hora_inicio AND hora_fin OR "+
									"'"+as_horaFin+"' BETWEEN hora_inicio AND hora_fin OR "+
									"hora_inicio BETWEEN '"+as_horaInicio+"' AND '"+as_horaFin+"'"+
									") " +					
									"AND codigo_medico="+ai_codigoMedico+" AND centro_atencion=? ";		
				
					if(ai_diaSemana != ConstantesBD.codigoNuncaValido)
						ls_consulta = ls_consulta.replaceFirst("@"," dia = "+ai_diaSemana+" AND ");
					else
						ls_consulta = ls_consulta.replaceFirst("@","");
					
					
					if(ab_modificar)
						ls_consulta += " AND codigo<>? ";		
					
									
					lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(ls_consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
					
					/* Establecer atributos cadena de la consulta */								
					lps_ps.setInt(1, ai_centroAtencion);
		
					if(ab_modificar)
						lps_ps.setInt(2, ai_codigo);					
		
					/*
						Obtener el n�mero de horarios de atenci�n para el m�dico especificado que afecta
						este horario			
					 */
					
					lrs_rs = new ResultSetDecorator(lps_ps.executeQuery());
					ll_conflictos = 0;
					while(lrs_rs.next() )
					{
						ll_conflictos = 1;
						temp0 = lrs_rs.getString("unidad_agenda");
						if(unidadAgenda.indexOf(temp0)==ConstantesBD.codigoNuncaValido)
							unidadAgenda += (unidadAgenda.equals("")?"":", ") + temp0;
					}
						
		
					if(ll_conflictos != 0)
					{
						/* Este horario afecta a uno o mas horarios de atenci�n asignados al m�dico */
						li_resp = 2;
					}
				}
			}
			
			
			//---------------------------------------------------------------------------------
			//Busqueda de horarios de atencion sin consultorio , sin medico
			if(ai_codigoMedico == ConstantesBD.codigoNuncaValido && 
				ai_consultorio == ConstantesBD.codigoNuncaValido && 
				ai_diaSemana != ConstantesBD.codigoNuncaValido )
			{
				if(li_resp == 0 )
				{				
					lrs_rs = null;				
					lps_ps = null;
		
					/**
					 * Se modifica la consulta para que evalue si existe informaci&oacute;n del horario de atenci&oacute;n 
					 * para la unidad de agenda seleccionada.					 * 
					 * Inc. Mantis 664.
					 * @author Diana Ruiz
					 */		
										
					/* Preparar la consulta */
					ls_consulta =	"SELECT getnombreunidadconsulta(unidad_consulta) AS unidad_agenda "+
									"FROM horario_atencion "+
									"WHERE  dia = "+ai_diaSemana+" AND  " +
									"unidad_consulta= "+ai_unidadConsulta+" AND " +									
									"('"+as_horaInicio+"' BETWEEN hora_inicio AND hora_fin OR "+
									"'"+as_horaFin+"' BETWEEN hora_inicio AND hora_fin OR "+
									"hora_inicio BETWEEN '"+as_horaInicio+"' AND '"+as_horaFin+"'"+
									") " +					
									"AND consultorio IS NULL AND codigo_medico IS NULL AND centro_atencion=? ";		
				
					if(ab_modificar)
						ls_consulta += " AND codigo<>? ";		
					
									
					lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(ls_consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
					
					/* Establecer atributos cadena de la consulta */								
					lps_ps.setInt(1, ai_centroAtencion);
		
					if(ab_modificar)
						lps_ps.setInt(2, ai_codigo);					
		
					/*
						Obtener el n�mero de horarios de atenci�n para el m�dico especificado que afecta
						este horario			
					 */
					
					lrs_rs = new ResultSetDecorator(lps_ps.executeQuery());
					ll_conflictos = 0;
					if(lrs_rs.next() )
					{
									
						ll_conflictos = 1;
						temp0 = lrs_rs.getString("unidad_agenda");
						if(unidadAgenda.indexOf(temp0)==ConstantesBD.codigoNuncaValido)
							unidadAgenda += (unidadAgenda.equals("")?"":", ") + temp0;
					}
						
		
					if(ll_conflictos != 0)
					{
						/* Este horario afecta a uno o mas horarios de atenci�n que no tienen consultorio , ni medico */
						li_resp = 3;
					}
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		logger.info("valor de la validacion >> "+li_resp);
		logger.info("valor de la uniad agenda >> "+unidadAgenda);
		InfoDatos resultado = new InfoDatos();
		resultado.setCodigo(li_resp);
		resultado.setNombre(unidadAgenda);
		return resultado;
	}
	
	
	/**
	* consultar la Especialidad Unidad de Agenda
	* @param con Conexi�n abierta con una fuente de datos
	* @param HashMap parametros
	* @return HashMap<String,Object>
	*/
	public static HashMap<String,Object> getEspecialidad(Connection con, HashMap parametros)
	{
		HashMap<String,Object> datos = new HashMap<String,Object>(); 
		try
		{
			PreparedStatementDecorator	ps;
			/* Verificar el estado de la conexi�n y abrir una nueva si es necesario */
			if(con == null || con.isClosed() )
				con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			
			String profesionales = getTiposSeviciosUnidadConsulta(con,parametros.get("codigo").toString());
			//logger.info("profecionales >>>>>"+profesionales);
			ps = new PreparedStatementDecorator(con.prepareStatement(consultarEspecialidadUniAgen,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("codigo").toString()));
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				datos.put("descripcion", rs.getString("descripcion"));
				datos.put("activa", rs.getString("activa"));
				datos.put("especialidad", rs.getInt("especialidad"));
				datos.put("profesionales", profesionales);
				datos.put("tipoatencion", rs.getString("tipoatencion"));
			}
			ps.close();
		}catch (Exception e) {
			logger.info("Error en la Consulta >>>"+e);
		}
		return datos;
	}
	
	/**
	* consultar tipos de servicios asociados a la unidad de agenda
	* @param con Conexi�n abierta con una fuente de datos
	* @param HashMap parametros
	* @return HashMap<String,Object>
	*/
	private static String getTiposSeviciosUnidadConsulta(Connection con, String unidad_consulta)
	{
		String tipoProfecionales = "";
		boolean procedimientos = false;
		boolean consulta = false;
		try
		{
			PreparedStatementDecorator	ps;
			/* Verificar el estado de la conexi�n y abrir una nueva si es necesario */
			if(con == null || con.isClosed() )
				con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
	
			ps = new PreparedStatementDecorator(con,consultarTipoServiciosUnidadConsulta);
			ps.setInt(1, Utilidades.convertirAEntero(unidad_consulta));
			logger.info("\n\nel query::: "+ps);
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				if(rs.getString("tipo_servicio").charAt(0)== ConstantesBD.codigoServicioProcedimiento
						|| rs.getString("tipo_servicio").charAt(0)==ConstantesBD.codigoServicioNoCruentos)
					procedimientos = true;
				else if(rs.getString("tipo_servicio").charAt(0)==ConstantesBD.codigoServicioInterconsulta)
					consulta = true;
			}
			
			if(procedimientos && consulta)
				tipoProfecionales = ConstantesIntegridadDominio.acronimoAmbos;
			else if(procedimientos)
				tipoProfecionales = String.valueOf(ConstantesBD.codigoServicioProcedimiento);
			else if(consulta)
				tipoProfecionales = String.valueOf(ConstantesBD.codigoServicioInterconsulta);
			
			ps.close();
		}catch (Exception e) {
			logger.info("Error en la Consulta >>>"+e);
		}		
		logger.info("\n\nprocedimiento >>>>"+procedimientos);
		logger.info(" consutla >>>>"+consulta);
		logger.info(" tipo profesiona >>>"+tipoProfecionales);
		return tipoProfecionales;
	}
	
	
	public static ArrayList<HashMap<String, Object>> consultarHAAvanzada(Connection con, HashMap<String, Object> parametros){
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs= null;
		
		ArrayList<HashMap<String, Object>> lista = null;
		HashMap<String, Object> codigoHA = null;
		
		ArrayList<HashMap<String, Object>> listaTotal = new ArrayList<HashMap<String, Object>>();
		
		ArrayList<Integer> codigosCentrosAtencion = null; 
				
		String consulta= "";
 
		ArrayList<Integer> dias = (ArrayList<Integer>) parametros.get("dias");
		
		String seccionWHERE = "";
		String seccionORDERBY = "";

		try{

		if(Integer.parseInt(parametros.get("centroAtencion")+"") != ConstantesBD.codigoNuncaValido){
			codigosCentrosAtencion = new ArrayList<Integer>();
			codigosCentrosAtencion.add(Integer.parseInt(parametros.get("centroAtencion")+""));
		}else{	
			codigosCentrosAtencion = obtenerCentrosAtencionValidosXUsuario(con, parametros.get("usuario")+"", ConstantesBD.codigoActividadAutorizadaHorarioAtencion);
		}
		
		for(int i=0 ; i<codigosCentrosAtencion.size() ; i++){
			
			consulta= strConCodigosHorarioAtencion;
			seccionWHERE = "";
			seccionORDERBY = "";
			
			//Centro de atenci�n
			seccionWHERE +=  " ha.centro_atencion = "
				+ codigosCentrosAtencion.get(i);
			
			//Unidad de Agenda
			if(Integer.parseInt(parametros.get("unidadAgenda")+"") != ConstantesBD.codigoNuncaValido){
				seccionWHERE += (seccionWHERE.length() > 0 ? " AND" : "")
						+ " ha.unidad_consulta = "
						+ Integer.parseInt(parametros.get("unidadAgenda")+"")+" ";
			}else{
				seccionWHERE += (seccionWHERE.length() > 0 ? " AND" : "")
						+ " ha.unidad_consulta IN "
						+ " ( "+UtilidadTexto.convertirArrayIntegerACodigosSeparadosXComas(obtenerUnidadesAgendaValidasXUsuario(con, parametros.get("usuario")+"", codigosCentrosAtencion.get(i), ConstantesBD.codigoActividadAutorizadaHorarioAtencion)) +" ) ";
			}
			
			//Con consultorio
			if((parametros.get("consultorioAsignado")+"").equals(ConstantesBD.acronimoSi)){
				seccionWHERE += (seccionWHERE.length() > 0 ? " AND" : "")
						+ " ha.consultorio IS NOT NULL ";
						
				if(Integer.parseInt(parametros.get("consultorio")+"") != ConstantesBD.codigoNuncaValido){
					seccionWHERE += (seccionWHERE.length() > 0 ? " AND" : "")
							+ " ha.consultorio = "
							+ Integer.parseInt(parametros.get("consultorio")+"")+" ";
				}
			} else if((parametros.get("consultorioAsignado")+"").equals(ConstantesBD.acronimoNo)){
				seccionWHERE += (seccionWHERE.length() > 0 ? " AND" : "")
						+ " ha.consultorio IS NULL ";
			}
			
			//Con profesional
			if((parametros.get("profesionalAsignado")+"").equals(ConstantesBD.acronimoSi)){
				seccionWHERE += (seccionWHERE.length() > 0 ? " AND" : "")
						+ " ha.codigo_medico IS NOT NULL ";
				
				if(Integer.parseInt(parametros.get("profesional")+"") != ConstantesBD.codigoNuncaValido){
					seccionWHERE += (seccionWHERE.length() > 0 ? " AND" : "")
							+ " ha.codigo_medico = "
							+ Integer.parseInt(parametros.get("profesional")+"")+" ";
				}
			} else if((parametros.get("profesionalAsignado")+"").equals(ConstantesBD.acronimoNo)){
				seccionWHERE += (seccionWHERE.length() > 0 ? " AND" : "")
						+ " ha.codigo_medico IS NULL ";
			}
			
			//Dias
			if(!dias.isEmpty()){
				seccionWHERE +=" AND dia IN ("+UtilidadTexto.convertirArrayIntegerACodigosSeparadosXComas(dias)+") ";
			}
			
			if(!seccionWHERE.equals("")){
				consulta+=" WHERE ";
				seccionORDERBY = " ORDER BY   ha.centro_atencion, ha.unidad_consulta, ha.consultorio"; 
			}
			
			consulta += seccionWHERE + seccionORDERBY;
			
			ps =  new PreparedStatementDecorator(con,consulta);
			logger.info("\n\nconsulta busqueda avanzada horarios atencion:::::"+ps);
			rs = new ResultSetDecorator(ps.executeQuery());
			
			lista = new ArrayList<HashMap<String,Object>>();
			
			while(rs.next()){
				
				codigoHA = new HashMap<String, Object>();
				codigoHA.put("codigo",rs.getInt("codigo"));
				lista.add(codigoHA);
			}
			
			Iterator<HashMap<String, Object>> iter = lista.iterator();
			while (iter.hasNext()){
			  listaTotal.add((HashMap<String, Object>) iter.next());
			}
			
		 }
		
		}catch (SQLException e){
            logger.error("ERROR SQLException consultarHAAvanzada: ", e);
     
        }catch(Exception ex){
			logger.error("ERROR Exception consultarHAAvanzada: ", ex);
		
        }finally{
			try{
				if(ps != null){
					ps.close();
				}	
				if(rs != null){
					rs.close();
				}	
			}catch (SQLException se) {
				logger.error("###########  Error close PreparedStatement", se);
			}
		}
		return listaTotal;
	}
	
	/**
	 * Metodo implementado para obtener el codigo de los centros de atencion validos para el usuario
	 * @param con
	 * @param usuario
	 * @return
	 */
	public static ArrayList<Integer> obtenerCentrosAtencionValidosXUsuario(Connection con, String usuario, int actividad)
	{
		ArrayList<Integer> codigosCentrosAtencionAutorizados = null;
		
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		
		try{
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(strConCentroAtencionValidosXUsuario, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setString(1, usuario);
			ps.setInt(2, actividad);
			
			rs = new ResultSetDecorator(ps.executeQuery());
			
			logger.info(" consulta obtenerCentrosAtencionValidosXUsuario -->"+strConCentroAtencionValidosXUsuario);
			logger.info("-- usuario = "+usuario+" - eactividad = "+actividad);
			
			codigosCentrosAtencionAutorizados = new ArrayList<Integer>();
			
			while(rs.next())
				codigosCentrosAtencionAutorizados.add(rs.getInt("codigo"));	
			
			if(codigosCentrosAtencionAutorizados.isEmpty())
				codigosCentrosAtencionAutorizados.add(ConstantesBD.codigoNuncaValido);
		
		}catch (SQLException e){
            logger.error("ERROR SQLException obtenerCentrosAtencionValidosXUsuario: ", e);
     
        }catch(Exception ex){
			logger.error("ERROR Exception obtenerCentrosAtencionValidosXUsuario: ", ex);
		
        }finally{
			try{
				if(ps != null){
					ps.close();
				}	
				if(rs != null){
					rs.close();
				}	
			}catch (SQLException se) {
				logger.error("###########  Error close PreparedStatement", se);
			}
		}
		return codigosCentrosAtencionAutorizados;
	}
	
	/**
	 * Metodo implementado para obtener el codigo de las unidades de agenda validas para el usuario
	 * @param con
	 * @param usuario
	 * @return
	 */
	public static ArrayList<Integer> obtenerUnidadesAgendaValidasXUsuario(Connection con, String usuario, int centroAtencion, int actividad){
		
		ArrayList<Integer> codigosUnidadesAgendaAutorizadas = null;
		
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		
		try{
			
		ps =  new PreparedStatementDecorator(con.prepareStatement(strConUnidadesAgendaValidasXUsuario, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
		ps.setString(1, usuario);
		ps.setInt(2, actividad);
		ps.setInt(3, centroAtencion);
		
		rs = new ResultSetDecorator(ps.executeQuery());
		
		logger.info(" consulta obtenerUnidadesAgendaValidasXUsuario -->"+strConUnidadesAgendaValidasXUsuario);
		logger.info("-- usuario = "+usuario+" - actividad = "+actividad+" centroAtencion = "+centroAtencion);
		
		codigosUnidadesAgendaAutorizadas = new ArrayList<Integer>();
		
		while(rs.next())
			codigosUnidadesAgendaAutorizadas.add(rs.getInt("codigo"));	
		
		if(codigosUnidadesAgendaAutorizadas.isEmpty())
			codigosUnidadesAgendaAutorizadas.add(ConstantesBD.codigoNuncaValido);
			
		}catch (SQLException e){
            logger.error("ERROR SQLException obtenerUnidadesAgendaValidasXUsuario: ", e);
     
        }catch(Exception ex){
			logger.error("ERROR Exception obtenerUnidadesAgendaValidasXUsuario: ", ex);
		
        }finally{
			try{
				if(ps != null){
					ps.close();
				}	
				if(rs != null){
					rs.close();
				}	
			}catch (SQLException se) {
				logger.error("###########  Error close PreparedStatement", se);
			}
		}
		return codigosUnidadesAgendaAutorizadas;
	}
	
}
