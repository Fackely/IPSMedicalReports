package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

public class SqlBaseReasignarAgendaDao 
{
	
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseReasignarAgendaDao.class);

	/**
	 * Cadena para realizar las consultas del listado de agendas.
	 */
	//toda la info -->private static String cadenaListadoAgendas="SELECT a.codigo as codigo,a.fecha_gen as fechageneracion,a.hora_gen as horageneracion,a.usuario as usuario,getnombrepersona(u.codigo_persona) as usuariogenera,a.unidad_consulta as unidadconsulta,getnombreunidadconsulta(a.unidad_consulta) as nombreunidadconsulta,a.consultorio as consultorio,c.descripcion as descconsultorio,a.dia as codigodia,ds.dia as descdia,a.fecha as fechaagenda,a.hora_inicio as horainicio,a.hora_fin as horafin,a.codigo_medico as codigoprofesional,getnombrepersona(a.codigo_medico) as nombreprofesional,a.tiempo_sesion as tiemposesion,a.cupos as cupos,'false' as reasignar from agenda a inner join usuarios u on(a.usuario=u.login) inner join consultorios c on(a.consultorio=c.codigo) inner join dias_semana ds on(a.dia=ds.codigo) where a.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" and  (a.fecha between ?  and ?) and a.codigo_medico=?";
	private static String cadenaListadoAgendas="SELECT " +
		"a.codigo as codigo," +
		"a.unidad_consulta as unidadconsulta," +
		"getnombreunidadconsulta(a.unidad_consulta) as nombreunidadconsulta," +
		"a.centro_atencion as codigocentroatencion," +
		"getnomcentroatencion(a.centro_atencion) as nombrecentroatencion," +
		"a.consultorio as consultorio," +
		"c.descripcion as descconsultorio," +
		"a.fecha as fechaagenda," +
		"a.hora_inicio as horainicio," +
		"a.hora_fin as horafin," +
		"a.cupos as cupos," +
		"'false' as reasignar " +
		"from agenda a " +
		"inner join usuarios u on(a.usuario=u.login) " +
		"inner join consultorios c on(a.consultorio=c.codigo) " +
		"inner join dias_semana ds on(a.dia=ds.codigo) " +
		"where " +
		"a.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" and  (a.fecha between ?  and ?) ";
	
	/**
	 * Cadena para consultar las citas de una agenda.
	 */
	private static String cadenaConsultaCitasAgenda="SELECT c.codigo_agenda as agenda,c.codigo as codigocita,c.estado_cita as estadocita,ec.nombre as descestadocita from cita c inner join estados_cita ec on (c.estado_cita=ec.codigo)  where codigo_agenda=?";
	
	/**
	 * cadena para reasignar la agenda.
	 */
	private static String cadenaReasignaAgenda="UPDATE agenda SET  codigo_medico = ?  where codigo= ?";
	
	/**
	 * cadena para consultar los logs. 
	 */
	private static String cadenaConsultaLogs="SELECT " +
													"getnomcentroatencion( a.centro_atencion ) as centroatencion," +
													"lrc.codigo as codigolog," +
													"lrc.fecha_proceso as fecha," +
													"lrc.hora_proceso as hora," +
													"lrc.usuario as usuario," +
													"a.fecha as fechacita," +
													"a.hora_inicio as horacita," +
													"a.unidad_consulta as unidadconsulta," +
													"getnombreunidadconsulta(a.unidad_consulta) as nombreunidadconsulta," +
													"lrc.codigo_medico_anterior as codmedicoanterior," +
													"getnombrepersona(lrc.codigo_medico_anterior) as nommedicoanterior," +
													"a.codigo_medico as codigoprofesional," +
													"getnombrepersona(a.codigo_medico) as nombreprofesional " +
												"FROM " +
													"log_reasignacion_citas lrc " +
												"INNER JOIN " +
													"agenda a on(lrc.cod_agenda_modificada=a.codigo) " +
												"WHERE " +
													"(lrc.fecha_proceso  between ?  and ?)";
	
	/**
	 * Metodo que busca las agendas generadas.
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap buscarAgendas(Connection con, HashMap vo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try 
		{
			String cadena=cadenaListadoAgendas;
			if(Integer.parseInt(vo.get("unidadConsulta").toString())>0)
				cadena=cadena+" and a.unidad_consulta="+vo.get("unidadConsulta");
			else
				cadena=cadena+" and a.unidad_consulta IN ("+vo.get("unidadesAgenda")+") ";
			
			if(Integer.parseInt(vo.get("centroAtencion").toString())>0)
				cadena=cadena+" and a.centro_atencion="+vo.get("centroAtencion");
			else
				cadena=cadena+" and a.centro_atencion IN ("+vo.get("centrosAtencion")+") ";
			
			if(!vo.get("profesional").toString().equals(ConstantesBD.codigoNuncaValido+""))
				cadena=cadena+" and a.codigo_medico="+vo.get("profesional").toString();
			else
				cadena += " and a.codigo_medico IS NULL ";
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setDate(1,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial").toString())));
            ps.setDate(2,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal").toString())));
            
            logger.info("Parama 1 - "+vo.get("fechaInicial"));
            logger.info("Parama 2 - "+vo.get("fechaFinal"));
            logger.info("Consulta - "+cadena);
            
            logger.info("mapa de agendas:");
            Utilidades.imprimirMapa(mapa);
            
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} catch (SQLException e) 
		{
			logger.error("ERROR EJECUTANDO LA CONSULTA DE LAS AGENDA [SqlBaseReasignarAgendaDao]");
			e.printStackTrace();
		}
		
		
		return mapa;
	}

	

	/**
	 * 
	 * @param con
	 * @param codigoAgenda
	 * @return
	 */
	public static HashMap consultarCitasAgenda(Connection con, int codigoAgenda) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaCitasAgenda,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,codigoAgenda);
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} catch (SQLException e) 
		{
			logger.error("ERROR EJECUTANDO LA CONSULTA DE LAS CITAS DE UNA AGENDA [SqlBaseReasignarAgendaDao]");
			e.printStackTrace();
		}
		return mapa;
	}


	

	/**
	 * 
	 * @param con
	 * @param codigoAgenda
	 * @param nuevoProfesional
	 * @return
	 */
	public static boolean reasignarProfesiona(Connection con, int codigoAgenda, int nuevoProfesional) 
	{
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaReasignaAgenda,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,nuevoProfesional);
            ps.setInt(2,codigoAgenda);
            int resultado=ps.executeUpdate();
            ps.close();
            return (resultado>0);
		} catch (SQLException e) 
		{
			logger.error("ERROR REASIGNANDO LA AGENDA [SqlBaseReasignarAgendaDao]");
			e.printStackTrace();
		}
		return false;
	}



	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean insertarLogReasignacionAgenda(Connection con, HashMap vo,String cadena) 
	{
		try 
		{
			logger.info("SQL / insertarLogReasignacionAgenda / "+cadena);
			Utilidades.imprimirMapa(vo);
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setDate(1,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
            ps.setString(2,UtilidadFecha.getHoraActual());
            ps.setString(3,vo.get("usuario").toString());
            ps.setInt(4,Integer.parseInt(vo.get("codigoAgenda").toString()));
            if(Integer.parseInt(vo.get("medicoAnterior").toString())>0)
            	ps.setInt(5,Integer.parseInt(vo.get("medicoAnterior").toString()));
            else
            	ps.setNull(5,Types.INTEGER);
            int resultado=ps.executeUpdate();
            ps.close();
            return (resultado>0);
		} catch (SQLException e) 
		{
			logger.error("ERROR EN LA GENRACION DEL LOG TIPO BD LA AGENDA [SqlBaseReasignarAgendaDao]");
			e.printStackTrace();
		}
		return false;	}



	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap ejecutarBusquedalogs(Connection con, HashMap vo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try 
		{
			String cadena=cadenaConsultaLogs;
			if(Integer.parseInt(vo.get("profesional").toString())>0)
				cadena=cadena+ " and lrc.codigo_medico_anterior="+vo.get("profesional").toString();// and lrc.usuario=?
			if(!vo.get("usuario").toString().trim().equals(""))
				cadena=cadena+ " and lrc.usuario='"+vo.get("usuario").toString()+"'";
			
			if(Integer.parseInt(vo.get("centroAtencion").toString())>0)
				cadena=cadena+" and a.centro_atencion="+vo.get("centroAtencion");
			else
				cadena=cadena+" and a.centro_atencion IN ("+vo.get("centrosAtencion")+") ";
			
			logger.info("SQL / ejecutarBusquedalogs / "+cadena);
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setDate(1,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial").toString())));
            ps.setDate(2,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal").toString())));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} catch (SQLException e) 
		{
			logger.error("ERROR EJECUTANDO LA CONSULTA DE LOS LOGs [SqlBaseReasignarAgendaDao]");
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Método para verificar si un profesional pertenece a una unidad de agenda
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean perteneceProfesionalAUnidadAgenda(Connection con,HashMap campos)
	{
		boolean pertenece = false;
		try
		{
			//*********SE TOMAN LOS PARÁMETROS**********************************************
			int codigoProfesional = Integer.parseInt(campos.get("codigoProfesional").toString());
			int codigoUnidadAgenda = Integer.parseInt(campos.get("codigoUnidadAgenda").toString());
			//******************************************************************************
			
			//Se verifica que el médico pertenezca a la unidad de agenda por especialidad
			int cuentaEspecialidad = 0;
			String consulta = "SELECT " +
				"count(1) as cuenta " +
				"from especialidades_medicos " +
				"where " +
				"codigo_medico  = "+codigoProfesional+" and " +
				"activa_sistema = "+ValoresPorDefecto.getValorTrueParaConsultas()+" and " +
				"codigo_especialidad in (select especialidad from unidades_consulta where codigo = "+codigoUnidadAgenda+" and especialidad is not null)";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				cuentaEspecialidad = rs.getInt("cuenta");
			
			//Se verifica que el médico pertenezca a la unidad de agenda por centro de costo
			int cuentaCentroCosto = 0;
			/*consulta = "SELECT " +
				"count(1) as cuenta " +
				"FROM usuarios usu " +
				"inner join centros_costo_usuario ccu ON (ccu.usuario = usu.login) " +
				"WHERE " +
				"usu.codigo_persona = "+codigoProfesional+" and " +
				"ccu.centro_costo in (select centro_costo from cen_costo_x_un_consulta where unidad_consulta = "+codigoUnidadAgenda+") and " +
				"(SELECT count(1) FROM unidades_consulta where unidad_consulta = "+codigoUnidadAgenda+" and especialidad is null) > 0";
			st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			rs =  new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				cuentaCentroCosto = rs.getInt("cuenta");
			*/
			
			
			if(cuentaEspecialidad>0 || cuentaCentroCosto > 0)
				pertenece = true;
		}
		catch(SQLException e)
		{
			logger.error("Error en perteneceProfesionalAUnidadAgenda: "+e);
		}
		return pertenece;
	}
	
	/**
	 * Método para obtener los profesionales que aplican para una unidad de agenda específica
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerProfesionalesUnidadAgenda(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		try
		{
			//**********SE TOMAN CAMPOS*****************************************
			int codigoUnidadAgenda = Integer.parseInt(campos.get("codigoUnidadAgenda").toString());
			//******************************************************************
			
			String consulta = "SELECT DISTINCT "+ 
				"m.codigo_medico AS codigo_profesional, "+
				"getnombrepersona(m.codigo_medico) As nombre_profesional "+ 
				"FROM medicos m " +
				"INNER JOIN especialidades_medicos em ON(em.codigo_medico = m.codigo_medico) " +
				"INNER JOIN usuarios usu ON(usu.codigo_persona = m.codigo_medico) " +
				"inner join centros_costo_usuario ccu ON (ccu.usuario = usu.login) " +
				"WHERE " +
				"(" +
					"(" +
						"em.codigo_especialidad IN (select especialidad from unidades_consulta where codigo = "+codigoUnidadAgenda+" and especialidad is not null) " +
						" AND " +
						"em.activa_sistema  = "+ValoresPorDefecto.getValorTrueParaConsultas()+" AND " +
						"(SELECT count(1) FROM servicios_unidades_consulta where unidad_consulta = "+codigoUnidadAgenda+" and gettiposervicio(codigo_servicio) = '"+ConstantesBD.codigoServicioInterconsulta+"') > 0 "+
					")" +
					" OR " +
					"(" +
						"ccu.centro_costo in (select centro_costo from cen_costo_x_un_consulta where unidad_consulta = "+codigoUnidadAgenda+") AND " +
						" (SELECT count(1) FROM servicios_unidades_consulta where unidad_consulta = "+codigoUnidadAgenda+" and gettiposervicio(codigo_servicio) <> '"+ConstantesBD.codigoServicioInterconsulta+"') > 0 "+
					") " +
				") " +
				"ORDER BY nombre_profesional ";
			logger.info("consulta validacion reasignacion: "+consulta);
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo", rs.getObject("codigo_profesional"));
				elemento.put("nombre", rs.getObject("nombre_profesional"));
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerProfesionalesUnidadAgenda: "+e);
		}
		return resultados;
	}
}
