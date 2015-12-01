/*
 * @(#)SqlBaseConsultoriosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */
package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.consultaExterna.DtoConsultorios;
import com.princetonsa.dto.odontologia.DtoAgendaOdontologica;
import com.princetonsa.mundo.manejoPaciente.Consultorios;

/**
 * Consultas estandar de consultorios
 * @author Wilson Rios
 * wrios@princetonsa.com
 */
public class SqlBaseConsultoriosDao 
{
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseConsultoriosDao.class);
	
	/**
	 * cadena para la eliminacion
	 */
	private static final String cadenaEliminacionStr="DELETE FROM consultorios WHERE codigo=?";
	
	/**
	 * cadena para la modificacion
	 */
	private static final String cadenaModificacionStr="UPDATE consultorios SET codigo_consultorio=?, descripcion=?, centro_atencion=?, activo=? WHERE codigo=?";
	
	/**
	 * cadena para la insercion
	 */
	private static final String cadenaInsertarStr="INSERT INTO consultorios (codigo, codigo_consultorio, descripcion, centro_atencion, activo) VALUES (?, ?, ?, ?, ?)";
	
	/**
	 * consulta la info de consultorios, EN CASO DE AGREGAR UN NUEVO CAMPO ENTONCES agregarlo al <INDICES_MAPA>
	 */
	private static final String cadenaConsultaStr= "SELECT " +
	
														"codigo as codigo, " +
														"codigo_consultorio as codigoconsultorio, " +
														"centro_atencion as codigocentroatencion, " +
														"getnomcentroatencion(centro_atencion) as centroatencion, " +
														"descripcion as descripcion, " +
														"CASE WHEN activo ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS activo, " +
														"'"+ConstantesBD.acronimoSi+"' AS estabd " +
													"FROM " +
														"consultorios " +
													"WHERE 1=1 ";
	
	
	/**
	 * Consulta los n consultorios x centro atencion 
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap consultoriosXCentroAtencion(Connection con, int centroAtencion)
	{
		String cadena= cadenaConsultaStr;
		
		if(centroAtencion>0)
			cadena+=" AND centro_atencion= "+centroAtencion+" ";
		
		cadena+=" ORDER BY codigoconsultorio ";
		
		logger.info("consultorios x centro-->"+cadena+" ->"+centroAtencion);
		
		String[] indicesMapa={"codigo_", "codigoconsultorio_", "codigocentroatencion_", "centroatencion_", "descripcion_", "activo_", "acronimotipo_", "tipo_", "estabd_"};
		HashMap mapa= new HashMap();
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		logger.warn(mapa);
		
		mapa.put("INDICES_MAPA", indicesMapa);
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param string
	 * @return
	 */
	public static boolean eliminarRegistro(Connection con, int codigo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaEliminacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarConsultorio(Connection con, int codigo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena= cadenaConsultaStr+
						" and codigo=? ";
		cadena+=" ORDER BY codigoconsultorio ";
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public static boolean insertar(Connection con, Consultorios consultorio)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO consultorios (codigo, codigo_consultorio, descripcion, centro_atencion, activo) VALUES (?, ?, ?, ?, ?)
			 */
			
			ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_consultorios"));
			ps.setInt(2, consultorio.getCodigoConsultorio());
			ps.setString(3, consultorio.getDescripcion());
			ps.setInt(4, consultorio.getCentroAtencion());

			if(consultorio.getActivo().equals(ConstantesBD.acronimoSi))
				ps.setBoolean(5, UtilidadTexto.getBoolean(ValoresPorDefecto.getValorTrueParaConsultas()));
			else
				ps.setBoolean(5, UtilidadTexto.getBoolean(ValoresPorDefecto.getValorFalseParaConsultas()));
			
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public static boolean modificar(Connection con, Consultorios consultorio)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE consultorios SET codigo_consultorio=?, descripcion=?, centro_atencion=?, activo=? WHERE codigo=?
			 */
			
			ps.setInt(1, consultorio.getCodigoConsultorio());
			ps.setString(2, consultorio.getDescripcion());
			ps.setInt(3, consultorio.getCentroAtencion());
			
			if(consultorio.getActivo().equals(ConstantesBD.acronimoSi))
				ps.setBoolean(4, UtilidadTexto.getBoolean(ValoresPorDefecto.getValorTrueParaConsultas()));
			else
				ps.setBoolean(4, UtilidadTexto.getBoolean(ValoresPorDefecto.getValorFalseParaConsultas()));
			
			ps.setInt(5, consultorio.getCodigo());
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Lista los consultorios asociados a los horarios de atención que
	 * concuerden con los parámetros de búsqueda enviados
	 * @param dtoAgenda Dto con los datos de la agenda
	 * @return Lista de consultorios que cumplan las condiciones
	 */
	public static ArrayList<DtoConsultorios> listarConsultoriosParaGenerarAgenda(DtoAgendaOdontologica dtoAgenda)
	{
		if(
				UtilidadTexto.isEmpty(dtoAgenda.getFechaInicio())
				||
				UtilidadTexto.isEmpty(dtoAgenda.getFechaFin())
				||
				dtoAgenda.getCentroAtencion()<=0
		)
		{
			return new ArrayList<DtoConsultorios>();
		}
		
		String sentencia=
				"SELECT " +
					"DISTINCT " +
						"con.codigo AS codigo , " +
						"con.descripcion AS descripcion " +
				"FROM " +
					"consultorios con " +
				"INNER JOIN " +
					"horario_atencion ha " +
						"ON(ha.consultorio=con.codigo) " +
				"WHERE " +
					"con.activo=true ";
		if(dtoAgenda.getCentroAtencion()>0)
		{
			sentencia+="AND ha.centro_atencion = ? ";
		}
		if(dtoAgenda.getCodigoMedico()>0)
		{
			sentencia+="AND ha.codigo_medico = ? ";
		}
		if(dtoAgenda.getUnidadAgenda()>0)
		{
			sentencia+="AND ha.unidad_consulta = ? ";
		}
		if(dtoAgenda.getDia()>0)
		{
			sentencia+="AND ha.dia = ? ";
		}
		else
		{
			try
			{
				int numeroDias=(int)UtilidadFecha.obtenerDiferenciaEntreFechas(dtoAgenda.getFechaInicio(), dtoAgenda.getFechaFin(), Calendar.DATE)+1;
				if(numeroDias>0)
				{
					sentencia+="AND ha.dia IN(";
					String fechaInicial=dtoAgenda.getFechaInicio();
					for(int i=0; i<numeroDias; i++)
					{
						int diaSemana=1+UtilidadFecha.obtenerNumeroDiaSeman(fechaInicial);
						if(i!=0)
						{
							sentencia+=", ";
						}
						sentencia+=diaSemana;
						fechaInicial=UtilidadFecha.incrementarDiasAFecha(fechaInicial, 1, false);
					}
					sentencia+=")";
				}
			}catch (ParseException e)
			{
				Log4JManager.error("Error consultando los consultorios que aplican al horario de atención", e);
				return null;
			}
		}
		ArrayList<DtoConsultorios> listaConsultorios=new ArrayList<DtoConsultorios>();
		Connection con=UtilidadBD.abrirConexion();
		PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
		try
		{
			int cont=1;
			if(dtoAgenda.getCentroAtencion()>0)
			{
				psd.setInt(cont, dtoAgenda.getCentroAtencion());
				cont++;
			}
			if(dtoAgenda.getCodigoMedico()>0)
			{
				psd.setInt(cont, dtoAgenda.getCodigoMedico());
				cont++;
			}
			if(dtoAgenda.getUnidadAgenda()>0)
			{
				psd.setInt(cont, dtoAgenda.getUnidadAgenda());
				cont++;
			}
			if(dtoAgenda.getDia()>0)
			{
				psd.setInt(cont, dtoAgenda.getDia());
				cont++;
			}
			ResultSetDecorator rs=new ResultSetDecorator(psd.executeQuery());
			while(rs.next())
			{
				DtoConsultorios consultorio=new DtoConsultorios();
				consultorio.setCodigo(rs.getInt("codigo"));
				consultorio.setDescripcion(rs.getString("descripcion"));
				listaConsultorios.add(consultorio);
			}
			rs.close();
		} catch (SQLException e)
		{
			Log4JManager.error("Error consultando los consultorios que aplican al horario de atención", e);
		}
		finally
		{
			psd.cerrarPreparedStatement();
			UtilidadBD.closeConnection(con);
		}
		return listaConsultorios;
	}

	/**
	 * Lista los consultorios que no se encuentren asociados a los horarios de atención
	 * que concuerden con los parámetros de búsqueda enviados
	 * Los consultorios se asignan en el atributo listConsultoriosXGenerar
	 * @param dtoAgenda DTO con los datos de la agenda
	 */
	public static void listarConsultoriosParaGenerarAgendaSinConsultorio(DtoAgendaOdontologica dtoAgenda)
	{
		if(
				UtilidadTexto.isEmpty(dtoAgenda.getFechaInicio())
				||
				UtilidadTexto.isEmpty(dtoAgenda.getFechaFin())
				||
				dtoAgenda.getCentroAtencion()<=0
		)
		{
			return;
		}
		
		String dias="";

		String sentencia=
				"SELECT " +
					"conext.codigo AS codigo, " +
					"conext.descripcion AS descripcion " +
				"FROM " +
					"consultorios conext " +
				"WHERE " +
					"codigo NOT IN ("+
						"SELECT " +
							"DISTINCT " +
								"con.codigo AS codigo " +
						"FROM " +
							"consultorios con " +
						"INNER JOIN " +
							"horario_atencion ha " +
								"ON(ha.consultorio=con.codigo) " +
						"WHERE " +
							"con.activo=true ";
		if(dtoAgenda.getCentroAtencion()>0)
		{
			sentencia+="AND ha.centro_atencion = ? ";
		}
		if(dtoAgenda.getCodigoMedico()>0)
		{
			sentencia+="AND ha.codigo_medico = ? ";
		}
		if(dtoAgenda.getUnidadAgenda()>0)
		{
			sentencia+="AND ha.unidad_consulta = ? ";
		}
		if(dtoAgenda.getDia()>0)
		{
			sentencia+="AND ha.dia = ? ";
		}
		else
		{
			try
			{
				int numeroDias=(int)UtilidadFecha.obtenerDiferenciaEntreFechas(dtoAgenda.getFechaInicio(), dtoAgenda.getFechaFin(), Calendar.DATE)+1;
				if(numeroDias>0)
				{
					sentencia+="AND ha.dia IN(";
					String fechaInicial=dtoAgenda.getFechaInicio();
					for(int i=0; i<numeroDias; i++)
					{
						int diaSemana=1+UtilidadFecha.obtenerNumeroDiaSeman(fechaInicial);
						if(i!=0)
						{
							dias+=", ";
						}
						sentencia+=diaSemana;
						fechaInicial=UtilidadFecha.incrementarDiasAFecha(fechaInicial, 1, false);
					}
					sentencia+=dias+")";
				}
			}catch (ParseException e)
			{
				Log4JManager.error("Error consultando los consultorios que aplican al horario de atención", e);
				dtoAgenda.setListConsultoriosXGenerar(new ArrayList<DtoConsultorios>());
				return;
			}
		}
		sentencia+=
					")" +
					"AND codigo NOT IN (" +
						"SELECT " +
							"DISTINCT " +
							"con.codigo AS codigo " +
						"FROM " +
							"consultorios con " +
						"INNER JOIN " +
							"horario_atencion ha " +
								"ON(ha.consultorio=con.codigo) " +
						"WHERE " +
								"con.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" " +
							"AND " +
								"ha.centro_atencion = ? ";
		if(dtoAgenda.getDia()>0)
		{
			sentencia+="AND ha.dia = ? ";
		}else if(dias.length()>0)
		{
			sentencia+="AND " +
								"AND ha.dia IN("+dias+") ";
		}
		sentencia+=
							"AND " +
							" (" +
								"? BETWEEN ha.hora_inicio AND ha.hora_fin " +
								"OR " +
								"? BETWEEN ha.hora_inicio AND ha.hora_fin " +
								"OR " +
								"(? < ha.hora_inicio AND ? > ha.hora_fin) " +
							") " +
					") AND " +
						"conext.centro_atencion=?";
		ArrayList<DtoConsultorios> listaConsultorios=new ArrayList<DtoConsultorios>();
		Connection con=UtilidadBD.abrirConexion();
		PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
		try
		{
			int cont=1;
			if(dtoAgenda.getCentroAtencion()>0)
			{
				psd.setInt(cont, dtoAgenda.getCentroAtencion());
				cont++;
			}
			if(dtoAgenda.getCodigoMedico()>0)
			{
				psd.setInt(cont, dtoAgenda.getCodigoMedico());
				cont++;
			}
			if(dtoAgenda.getUnidadAgenda()>0)
			{
				psd.setInt(cont, dtoAgenda.getUnidadAgenda());
				cont++;
			}
			if(dtoAgenda.getDia()>0)
			{
				psd.setInt(cont, dtoAgenda.getDia());
				cont++;
			}
			if(dtoAgenda.getCentroAtencion()>0)
			{
				psd.setInt(cont, dtoAgenda.getCentroAtencion());
				cont++;
			}
			if(dtoAgenda.getDia()>0)
			{
				psd.setInt(cont, dtoAgenda.getDia());
				cont++;
			}
			if(!UtilidadTexto.isEmpty(dtoAgenda.getHoraInicio()))
			{
				psd.setString(cont, dtoAgenda.getHoraInicio());
				cont++;
			}
			if(!UtilidadTexto.isEmpty(dtoAgenda.getHoraFin()))
			{
				psd.setString(cont, dtoAgenda.getHoraFin());
				cont++;
			}
			if(!UtilidadTexto.isEmpty(dtoAgenda.getHoraInicio()))
			{
				psd.setString(cont, dtoAgenda.getHoraInicio());
				cont++;
			}
			if(!UtilidadTexto.isEmpty(dtoAgenda.getHoraFin()))
			{
				psd.setString(cont, dtoAgenda.getHoraFin());
				cont++;
			}
			if(dtoAgenda.getCentroAtencion()>0)
			{
				psd.setInt(cont, dtoAgenda.getCentroAtencion());
				cont++;
			}

			ResultSetDecorator rs=new ResultSetDecorator(psd.executeQuery());
			while(rs.next())
			{
				DtoConsultorios consultorio=new DtoConsultorios();
				consultorio.setCodigo(rs.getInt("codigo"));
				consultorio.setDescripcion(rs.getString("descripcion"));
				listaConsultorios.add(consultorio);
			}
			rs.close();
		} catch (SQLException e)
		{
			Log4JManager.error("Error consultando los consultorios que aplican al horario de atención", e);
		}
		finally
		{
			psd.cerrarPreparedStatement();
			UtilidadBD.closeConnection(con);
		}
		dtoAgenda.setListConsultoriosXGenerar(listaConsultorios);
	}
	
}
