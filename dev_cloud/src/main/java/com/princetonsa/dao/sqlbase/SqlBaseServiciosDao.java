/*
 * @(#)SqlBaseServiciosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.Beans.BeanBusquedaServicio;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantillaServDiag;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a Servicios
 *
 *	@version 1.0, Mar 30, 2004
 */
public class SqlBaseServiciosDao 
{
    /**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseServiciosDao.class);
    
	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar un servicio 
	 * presente en el sistema dado su codigo en una BD Genérica.
	 */
	private static final String cargarServicioStr="SELECT " +
													"ser.codigo, " +
													"ser.especialidad as codigoEspecialidad, " +
													"esp.nombre as especialidad, " +
													"ser.tipo_servicio as codigoTipo, " +
													"tip.nombre as tipo, " +
													"ser.naturaleza_servicio as codigoNaturaleza, " +
													"nat.nombre as naturaleza, " +
													"ser.sexo as codigoSexo,  " +
													"ser.espos, " +
													"ser.espossubsidiado, "+
													"ser.requiere_interpretacion as requiereInterpretacion, " +
													"ser.costo||'' as costo, " +
													"ser.realiza_institucion as realizaInstitucion, " +
													"ser.activo,  " +
													"coalesce(getobtenercodigocupsserv(ser.codigo, "+ConstantesBD.codigoTarifarioCups+"), '') AS codigoCups," +
													"ser.atencion_odontologica AS atencionodontologica," +
													"ser.convencion," +
													"ser.minutos_duracion AS minutos_duracion " +
												"from servicios ser, " +
													"especialidades esp, " +
													"tipos_servicio tip, " +
													"naturalezas_servicio nat " +
												"where " +
													"ser.especialidad=esp.codigo and " +
													"ser.tipo_servicio=tip.acronimo and " +
													"ser.naturaleza_servicio=nat.acronimo and " +
													"ser.codigo=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar los servicios 
	 * presentes en el sistema en una BD Genérica.
	 */
	private static final String cargarServiciosStr="SELECT " +
														"ser.codigo, " +
														"ser.especialidad as codigoEspecialidad, " +
														"esp.nombre as especialidad, " +
														"ser.tipo_servicio as codigoTipo, " +
														"tip.nombre as tipo, " +
														"ser.naturaleza_servicio as codigoNaturaleza, " +
														"nat.nombre as naturaleza, " +
														"ser.sexo as codigoSexo,  " +
														"ser.espos,ser.espossubsidiado, ser.activo, " +
														"ser.unidades_uvr AS unidadesUvr,  " +
														"ser.nivel AS nivel, " +
														"ser.requiere_interpretacion as requiereInterpretacion, " +
														"coalesce(ser.requiere_diagnostico,'') as requiereDiagnostico, " +
														"ser.costo||'' as costo, " +
														"ser.realiza_institucion as realizaInstitucion, " +
														"coalesce(ser.portatil_asociado, -1) as portatilAsociado, "+
														"getcodigoespecialidad(coalesce(ser.portatil_asociado, -1)) || '-' || coalesce(ser.portatil_asociado, -1) || ' ' || getnombreservicio(coalesce(ser.portatil_asociado, -1),0) as desPortatilAsociado, "+
														"ns.descripcion as descNivel, " +
														"coalesce(getobtenercodigocupsserv(ser.codigo, "+ConstantesBD.codigoTarifarioCups+"), '') AS codigoCups, " +
														"ser.atencion_odontologica AS atencionodontologica," +
														"ser.convencion," +
														"ser.minutos_duracion AS minutos_duracion " +
													"from " +
														"servicios ser, " +
														"especialidades esp, " +
														"tipos_servicio tip, " +
														"naturalezas_servicio nat, " +
														"nivel_atencion ns " +
													"where " +
														"ser.especialidad=esp.codigo and " +
														"ser.tipo_servicio=tip.acronimo and " +
														"ser.naturaleza_servicio=nat.acronimo and " +
														"ser.nivel=ns.consecutivo " +
													"order by ser.codigo";

	/**
	 * Cadena
	 */
	private static final String descripcionAutomaticoStr = "SELECT " +
	"ser.codigo as codigo, " +
	"ref.codigo_propietario || ' ' || ref.descripcion as descripcion, " +
	"ser.automatico as automatico, " +
	"ser.automatico as automatico_old "+
	" FROM servicios ser, referencias_servicio ref WHERE ser.codigo=ref.servicio AND ref.tipo_tarifario=0 AND ser.automatico=? "+
	"order by ref.descripcion";
	

	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar los sexos 
	 * presentes en el sistema en una BD Genérica.
	 */
	private static final String cargarSexosStr="SELECT codigo, nombre from sexo";


	/**
	 * Cadena constante con el <i>statement</i> necesario cargar los códigos 
	 * particulares de un servicio insertado en una BD Genérica.
	 */
	private static final String cargarCodigosParticularesStr="SELECT tipo_tarifario as tipoTarifario, codigo_propietario as nombre, descripcion, unidades from referencias_servicio where servicio=?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar el código
	 * particular de un servicio en el sistema en una BD Genérica.
	 */
	private static final String insertarCodigoParticularStr="INSERT INTO referencias_servicio (servicio, tipo_tarifario, codigo_propietario, descripcion) VALUES (?, ?, ?, ?)";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar el código
	 * particular de Soat en el sistema en una BD Genérica.
	 */
	private static final String insertarCodigoParticularSoatStr="INSERT INTO referencias_servicio (servicio, tipo_tarifario, codigo_propietario, unidades, descripcion) VALUES (?, "+  ConstantesBD.codigoTarifarioSoat +", ?, ?,  ?)";

	/**
	 * Cadena constante con el <i>statement</i> necesario modificar 
	 * un código particular de servicio previamente existente en una BD Genérica.
	 */
	private static final String existeCodigoParticularStr="SELECT count(1) as numResultados from referencias_servicio where servicio=? and tipo_tarifario=?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario modificar 
	 * un servicio previamente existente en una BD Genérica.
	 */
	private static final String modificarCodigoParticularStr="UPDATE referencias_servicio set codigo_propietario = ?, descripcion=? where servicio=? and tipo_tarifario=?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario modificar 
	 * un servicio soat previamente existente en una BD Genérica.
	 */
	private static final String modificarCodigoParticularSoatStr="UPDATE referencias_servicio set codigo_propietario = ?, unidades=?, descripcion=? where servicio=? and tipo_tarifario=" + ConstantesBD.codigoTarifarioSoat;

	/**
	 * Cadena constante con el <i>statement</i> necesario modificar 
	 * un servicio previamente existente en una BD Genérica.
	 */
	
		
	private static final String modificarServicioStr="UPDATE servicios set especialidad=?, tipo_servicio=?, naturaleza_servicio=?, sexo=?,   espos=?,espossubsidiado=?, activo=?, unidades_uvr = ?, grupo_servicio = ?, nivel=?, requiere_interpretacion=?, costo=?, realiza_institucion=?, requiere_diagnostico = ?, portatil_asociado = ?, atencion_odontologica=?, convencion=?, minutos_duracion=? where codigo=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * servicios ya usados en una BD Genérica.
	 */
	//private static final String busquedaServiciosYaUsadosStr="select codigoServicio from (SELECT codigo_servicio_solicitado as codigoServicio from sol_procedimientos UNION SELECT codigo_servicio_solicitado as codigoServicio from solicitudes_consulta UNION SELECT codigo_servicio_solicitado as codigoServicio from solicitudes_inter UNION SELECT codigo_servicio as codigoServicio from unidades_consulta) temp1 group by codigoServicio";
	//camio ocasionado por unidades de consulta
	private static final String busquedaServiciosYaUsadosStr="select codigoServicio from (SELECT codigo_servicio_solicitado as codigoServicio from sol_procedimientos UNION SELECT codigo_servicio_solicitado as codigoServicio from solicitudes_consulta UNION SELECT codigo_servicio_solicitado as codigoServicio from solicitudes_inter UNION SELECT codigo_servicio as codigoServicio from servicios_unidades_consulta) temp1 group by codigoServicio";

	/**
	 * Cadena constante con el <i>statement</i> para la consulta de las
	 * unidades Uvr
	 */
	private static final String consultaUnidadesUvr= "SELECT CASE WHEN unidades_uvr IS NULL THEN 0 ELSE unidades_uvr END AS unidadesUvr FROM servicios WHERE codigo = ?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario búscar el código de 
	 * servicio insertado rescientemente en el sistema en una BD Genérica.
	 */
	private static final String buscarCodigoServicioInsertadoStr = "SELECT max(codigo) as codigoServicio from servicios where especialidad=? and tipo_servicio=? and naturaleza_servicio=? and espos=? and espossubsidiado=?";
	
	/**
	 * vector sting de indice del mapa de coberturas
	 * */
	private static final String[] indicesMapa = {"codigo_","especialidad_","tiposervicio_","naturalezaservicio_","sexo_","formularios_","espos_","espossubsidiado_","activo_","unidadesuvr_","gruposervicio_","nivel_","tomamuestra_","respuestamultiple_","automatico_"};
 
	
	/**
	 * Cadena para la busqueda de servicios dado el codigo axioma o codigo cups o codigo iss o codigo soat o descripcion cups
	 */
	private static final String busquedaAvanzadaServiciosPorCodigos=		"SELECT distinct s.codigo AS codigoAxioma, " +
																											"(SELECT rs.codigo_propietario FROM referencias_servicio rs WHERE s.codigo=rs.servicio AND rs.tipo_tarifario="+ConstantesBD.codigoTarifarioCups+" ) AS codigoPropietarioCups," +
																											"(SELECT rs.codigo_propietario FROM referencias_servicio rs WHERE s.codigo=rs.servicio AND rs.tipo_tarifario="+ConstantesBD.codigoTarifarioISS+" ) AS codigoPropietarioIss, " +
																											"(SELECT rs.codigo_propietario FROM referencias_servicio rs WHERE s.codigo=rs.servicio AND rs.tipo_tarifario="+ConstantesBD.codigoTarifarioSoat+" ) AS codigoPropietarioSoat, " +
																											"(SELECT rs.descripcion FROM referencias_servicio rs WHERE s.codigo=rs.servicio AND rs.tipo_tarifario="+ConstantesBD.codigoTarifarioCups+") AS descripcion, " +
																											"s.especialidad AS codigoEspecialidad, " +
																											"e.nombre AS nombreEspecialidad, " +
																											"s.espos AS booleanEsPos, " +
																											"s.esPosSubsidiado as esPosSubsidiado, " +
																											"CASE WHEN s.espos ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'POS' ELSE 'NOPOS' END AS esPos, " +
                                                                                                            "0 AS mostrarMensajeEsServicioQx " +
																											"FROM servicios s " +
																											"INNER JOIN referencias_servicio refs ON (refs.servicio=s.codigo) " +
																											"INNER JOIN especialidades e ON (e.codigo= s.especialidad) " +
																											"WHERE " +
																											"s.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+"  ";
	
	
	
	/**
	 * Cadena constante con el <i>statement</i> necesario modificar 
	 * un servicio que se cargue automaticamente previamente existente en una BD Genérica.
	 */	
	private static final String actualizarAutomaticoStr="UPDATE servicios set automatico=?  where codigo=?";
	
	
	
	/**
	 * Implementación del método que carga todos los servicios 
	 * en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.ServiciosDao#cargarServicios (Connection ) throws SQLException 
	 */
	public static ResultSetDecorator cargarServicios (Connection con) throws SQLException
	{
		PreparedStatementDecorator cargarServiciosStatement= new PreparedStatementDecorator(con.prepareStatement(cargarServiciosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		return new ResultSetDecorator(cargarServiciosStatement.executeQuery());
	}

	/**
	 * Implementación del método que carga un servicio 
	 * en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.ServiciosDao#cargarServicio (Connection , int) throws SQLException 
	 */
	public static ResultSetDecorator cargarServicio (Connection con, int codigoServicio) throws SQLException
	{
		PreparedStatementDecorator cargarServicioStatement= new PreparedStatementDecorator(con.prepareStatement(cargarServicioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargarServicioStatement.setInt(1, codigoServicio);
		return new ResultSetDecorator(cargarServicioStatement.executeQuery());
	}

	/**
	 * Implementación del método que carga todos los sexos en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.ServiciosDao#cargarSexos (Connection ) throws SQLException 
	 */
	public static HashMap cargarSexos (Connection con) throws SQLException
	{
		PreparedStatementDecorator cargarSexosStatement= new PreparedStatementDecorator(con.prepareStatement(cargarSexosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ResultSetDecorator rs=new ResultSetDecorator(cargarSexosStatement.executeQuery());
		HashMap resultado=UtilidadBD.cargarValueObject(rs);
		cargarSexosStatement.close();
		rs.close();
		return resultado;
	}



	/**
	 * Implementación del método que carga todos los códigos particulares de un
	 * servicio en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.ServiciosDao#cargarCodigosParticulares (Connection , int ) throws SQLException 
	 */
	public static HashMap cargarCodigosParticulares (Connection con, int codigoServicio) throws SQLException
	{
		//logger.info(cargarCodigosParticularesStr+" "+codigoServicio);
		PreparedStatementDecorator cargarCodigosParticularesStatement=new PreparedStatementDecorator(con.prepareStatement(cargarCodigosParticularesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargarCodigosParticularesStatement.setInt(1,codigoServicio);
		ResultSetDecorator rs=new ResultSetDecorator(cargarCodigosParticularesStatement.executeQuery());
		HashMap mapa=UtilidadBD.cargarValueObject(rs);
		rs.close();
		cargarCodigosParticularesStatement.close();
		return mapa;
	}

	/**
	 * Implementación del método que carga las unidades Uvr de un 
	 * servicio en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.ServiciosDao#cargarCodigosParticulares (Connection , int ) throws SQLException 
	 */
	public static ResultSetDecorator consultaUnidadesUvr (Connection con, int codigoServicio) throws SQLException
	{
		PreparedStatementDecorator cargarUVRStatement= new PreparedStatementDecorator(con.prepareStatement(consultaUnidadesUvr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargarUVRStatement.setInt(1,codigoServicio);
		return new ResultSetDecorator(cargarUVRStatement.executeQuery());
	}
	
	
	/**
	 * Implementación del método que inserta un codigo particular de servicio 
	 * en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.ServiciosDao#insertarCodigoParticular (Connection , int , int , String , String , boolean ) throws SQLException 
	 */
	public static int insertarCodigoParticular (Connection con, int codigoServicio, int codigoTipoTarifario, String codigoPropietario, String descripcion) throws SQLException
	{
		PreparedStatementDecorator insertarCodigoParticularStatement= new PreparedStatementDecorator(con.prepareStatement(insertarCodigoParticularStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		insertarCodigoParticularStatement.setInt(1, codigoServicio);
		insertarCodigoParticularStatement.setInt(2, codigoTipoTarifario);
		insertarCodigoParticularStatement.setString(3, codigoPropietario);
		insertarCodigoParticularStatement.setString(4, descripcion.replaceAll("'", "`"));
		return insertarCodigoParticularStatement.executeUpdate();
	}

	/**
	 * Implementación del método que inserta un codigo particular de servicio 
	 * Soat en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.ServiciosDao#insertarCodigoParticularSoat (Connection , int , String , double , String ) throws SQLException 
	 */
	public static int insertarCodigoParticularSoat (Connection con, int codigoServicio, String codigoPropietario, double unidades, String descripcion) throws SQLException
	{
		PreparedStatementDecorator insertarCodigoParticularSoatStatement= new PreparedStatementDecorator(con.prepareStatement(insertarCodigoParticularSoatStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		insertarCodigoParticularSoatStatement.setInt(1, codigoServicio);
		insertarCodigoParticularSoatStatement.setString(2, codigoPropietario);
		
		if(unidades<0)
			insertarCodigoParticularSoatStatement.setObject(3,null);
		else
			insertarCodigoParticularSoatStatement.setDouble(3, unidades);
		insertarCodigoParticularSoatStatement.setString(4, descripcion.replaceAll("'", "`"));
		return insertarCodigoParticularSoatStatement.executeUpdate();
	}

	/**
	 * Implementación del método que dice si un codigo particular de servicio existe 
	 * en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.ServiciosDao#existeCodigoParticular (Connection , int , int ) throws SQLException 
	 */
	private static boolean existeCodigoParticular (Connection con, int codigoServicio, int codigoTipoTarifario)
	{
		try{
			PreparedStatementDecorator existeCodigoParticularStatement= new PreparedStatementDecorator(con.prepareStatement(existeCodigoParticularStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			existeCodigoParticularStatement.setInt(1, codigoServicio);
			existeCodigoParticularStatement.setInt(2, codigoTipoTarifario);
			ResultSetDecorator rs= new ResultSetDecorator(existeCodigoParticularStatement.executeQuery());
			
			boolean resultado=false;
			if (rs.next())
			{
				if (rs.getInt("numResultados")==1)
				{
					resultado=true;
				}
				else
				{
					resultado=false;
				}
			}
			else
			{
				logger.error("Error ejecutando un count en existeCodigoParticular");
			}
			rs.close();
			existeCodigoParticularStatement.close();
			return resultado;
		}
		catch (SQLException e) {
			logger.error("Error ejecutando un count en existeCodigoParticular "+e);
		}
		return false;
	}

	/**
	 * Implementación del método que modifica un codigo particular de servicio  
	 * en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.ServiciosDao#existeCodigoParticular (Connection , int , int ) throws SQLException 
	 */
	public static int modificarCodigoParticular (Connection con, int codigoServicio, int codigoTipoTarifario, String codigoPropietario, String descripcion) throws SQLException
	{
		if (!existeCodigoParticular(con, codigoServicio, codigoTipoTarifario))
		{
			return insertarCodigoParticular(con, codigoServicio, codigoTipoTarifario, codigoPropietario, descripcion);
		}
		else
		{
			PreparedStatementDecorator modificarCodigoParticularStatement =  new PreparedStatementDecorator(con.prepareStatement(modificarCodigoParticularStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	
			modificarCodigoParticularStatement .setString(1, codigoPropietario);
			modificarCodigoParticularStatement .setString(2, descripcion.replaceAll("'", "`"));
			modificarCodigoParticularStatement .setInt(3, codigoServicio);
			modificarCodigoParticularStatement .setInt(4, codigoTipoTarifario);
			return modificarCodigoParticularStatement .executeUpdate();
		}
	}

	/**
	 * Implementación del método que modifica un codigo particular Soat 
	 * de servicio en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.ServiciosDao#modificarCodigoParticularSoat (Connection , int , String , double , String ) throws SQLException 
	 */
	  public static int modificarCodigoParticularSoat (Connection con, int codigoServicio, String codigoPropietario, double unidades, String descripcion) throws SQLException
	  {
		  if (!existeCodigoParticular(con, codigoServicio, ConstantesBD.codigoTarifarioSoat))
		  {
			  return insertarCodigoParticularSoat(con, codigoServicio, codigoPropietario, unidades, descripcion);
		  }
		  else
		  {
			  PreparedStatementDecorator modificarCodigoParticularSoatStatement =  new PreparedStatementDecorator(con.prepareStatement(modificarCodigoParticularSoatStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	
			  modificarCodigoParticularSoatStatement .setString(1, codigoPropietario);
			  if(!(unidades+"").equals("") && !(unidades+"").equals(null))
			  {
				  if (unidades<0)
				  {
					  modificarCodigoParticularSoatStatement .setObject(2, null);
				  }
				  else
				  {
					  modificarCodigoParticularSoatStatement .setDouble(2, unidades);
				  }
			  }
			  else
			  {
				  modificarCodigoParticularSoatStatement .setDouble(2, unidades);
			  }
			  modificarCodigoParticularSoatStatement .setString(3, descripcion.replaceAll("'", "`"));
			  modificarCodigoParticularSoatStatement .setInt(4, codigoServicio);
			  return modificarCodigoParticularSoatStatement .executeUpdate();
		  }
	  }

	/**
	 * Implementación del método que modificar un servicio en una BD Genérica
	 * @param esPosSubsidiado 
	 * 
	 * @see com.princetonsa.dao.ServiciosDao#modificarServicio (Connection , int , int , String , String , int , int , int , boolean ) throws SQLException 
	 */
	public static int modificarServicio (Connection con, int codigoServicio, int codigoEspecialidad, String codigoTipoServicio, String codigoNaturalezaServicio, int codigoSexo,  HashMap formularios, boolean esPos, String esPosSubsidiado, boolean activo, double unidadesUvr, int codigoGrupoServicio, String consecutivoNivel, String requiereInterpretacion, String costo, String realizaInstitucion,String requiereDiagnostico, int codigoPortatilAsociado,int codigoInstitucion, String atencionOdontologica, int convencion, int minutosDuracion) throws SQLException
	{
		int resp = 0;
		
		PreparedStatementDecorator modificarServicioStatement= new PreparedStatementDecorator(con.prepareStatement(modificarServicioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		modificarServicioStatement.setInt(1, codigoEspecialidad);
		modificarServicioStatement.setString(2, codigoTipoServicio);
		modificarServicioStatement.setString(3, codigoNaturalezaServicio);
		
		if (codigoSexo>0)
		{
			modificarServicioStatement.setInt(4, codigoSexo);
		}
		else
		{
			modificarServicioStatement.setObject(4, null);
		}
		
		modificarServicioStatement.setBoolean(5, esPos);
		modificarServicioStatement.setString(6,esPosSubsidiado);
		modificarServicioStatement.setBoolean(7, activo);
		
		if (unidadesUvr<0)
		{
			modificarServicioStatement.setObject(8, null);
		}
		else
		{
			modificarServicioStatement.setDouble(8, unidadesUvr);
		}
		
		
		modificarServicioStatement.setInt(9,codigoGrupoServicio);
		
		modificarServicioStatement.setInt(10, Utilidades.convertirAEntero(consecutivoNivel));
		
       	modificarServicioStatement.setString(11,requiereInterpretacion);
		
		if (Utilidades.convertirADouble(costo) < 0)
		{
			modificarServicioStatement.setObject(12, null);
		}
		else
		{
			modificarServicioStatement.setDouble(12, Utilidades.convertirADouble(costo));
		}
		if(UtilidadTexto.isEmpty(realizaInstitucion))
			modificarServicioStatement.setObject(13, null);
		else
			modificarServicioStatement.setObject(13, realizaInstitucion);
		
		if(!requiereDiagnostico.equals(""))
			modificarServicioStatement.setString(14,requiereDiagnostico);
		else
			modificarServicioStatement.setNull(14,Types.VARCHAR);
		
		if(codigoPortatilAsociado != ConstantesBD.codigoNuncaValido)
			modificarServicioStatement.setInt(15,codigoPortatilAsociado);
		else
			modificarServicioStatement.setNull(15,Types.INTEGER);
		
		modificarServicioStatement.setString(16, atencionOdontologica);
		
		if(convencion != ConstantesBD.codigoNuncaValido)
			modificarServicioStatement.setInt(17,convencion);
		else
			modificarServicioStatement.setNull(17,Types.INTEGER);
		
		if(minutosDuracion != ConstantesBD.codigoNuncaValido)
			modificarServicioStatement.setInt(18,minutosDuracion);
		else
			modificarServicioStatement.setNull(18,Types.INTEGER);
		
		modificarServicioStatement.setInt(19, codigoServicio);
		resp = modificarServicioStatement.executeUpdate();
		
		if(resp>0)
			//****************MODIFICACION DE LOS FORMULARIOS******************************
			resp = insertarModificarEliminarFormularios(con, formularios, codigoServicio, codigoInstitucion);
			//****************************************************************************
		
		return resp;
	}

	/**
	 * Implementación del método que carga los servicios restringidos
	 * en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.ServiciosDao#cargarServiciosRestringido (Connection , BeanBusquedaServicio ) throws SQLException 
	 */
	public static HashMap cargarServiciosRestringido (Connection con, BeanBusquedaServicio restriccion, int institucion) throws SQLException
	{
		String consultaCodigos = "SELECT s.codigo from servicios s ";
		String codigoTarifario= ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion);
		if(UtilidadTexto.isEmpty(codigoTarifario))
		{
			codigoTarifario=ConstantesBD.codigoTarifarioCups+"";
		}
		
		int i;
		String palabraComunicacion=" where ";
		String criteriosBusqueda[]=restriccion.getCriteriosBusqueda();
		String codigosTarifarios[]=restriccion.getCodigosTarifarios();
		String joinSonria="",joinGenerico="",joinCups="", joinAux="", joinSoat="", joinISS="", restCups="", restSoat="", restSonria="", restISS="", restAux="",  restGenerico="";
		
		//Se verifica si la busqueda se hará por formulario asociado
		for(int j=0;j<criteriosBusqueda.length;j++)
			if (criteriosBusqueda[j].equals("formulario"))
				consultaCodigos += " INNER JOIN form_resp_serv frs ON(frs.servicio = s.codigo) ";
		
		for (i=0;i<criteriosBusqueda.length;i++)
		{
			//Las restricciones más sencillas (Estan en la misma tabla)
			if (criteriosBusqueda[i].equals("codigoServicioPortatil"))
			{
				if (restriccion.getCodigoServicioPortatil()>=0)
				{
					consultaCodigos=consultaCodigos + palabraComunicacion + " s.portatil_asociado= " + restriccion.getCodigoServicioPortatil();
					palabraComunicacion=  " and ";
				}
			}
			if (criteriosBusqueda[i].equals("especialidad"))
			{
				if (restriccion.getEspecialidad()>=0)
				{
					consultaCodigos=consultaCodigos + palabraComunicacion + " s.especialidad= " + restriccion.getEspecialidad();
					palabraComunicacion=  " and ";
				}
			}
			if (criteriosBusqueda[i].equals("sexo"))
			{
				if (restriccion.getSexo()>0)
				{
					consultaCodigos=consultaCodigos + palabraComunicacion + " s.sexo=" +restriccion.getSexo();
				}
			}
			else if (criteriosBusqueda[i].equals("codigoAxioma"))
			{
				//if(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion))
				if (restriccion.getCodigoAxioma()>=0)
				{
					consultaCodigos=consultaCodigos + palabraComunicacion + " s.codigo=" + restriccion.getCodigoAxioma();
					palabraComunicacion=  " and ";
				}
			}
			else if (criteriosBusqueda[i].trim().equals("requiereInterpretacion"))
			{
				if (!restriccion.getRequiereInterpretacion().equals(""))
				{
					consultaCodigos=consultaCodigos + palabraComunicacion + " s.requiere_interpretacion='" + restriccion.getRequiereInterpretacion()+"' ";
					palabraComunicacion=  " and ";
				}
			}
			else if (criteriosBusqueda[i].trim().equals("requiereDiagnostico"))
			{
				if (!restriccion.getRequiereDiagnostico().equals(""))
				{
					consultaCodigos=consultaCodigos + palabraComunicacion + " s.requiere_diagnostico='" + restriccion.getRequiereDiagnostico()+"' ";
					palabraComunicacion=  " and ";
				}
			}
			else if (criteriosBusqueda[i].equals("valor"))
			{
				if (restriccion.getValor()>=0)
				{
					consultaCodigos=consultaCodigos + palabraComunicacion + " s.costo=" + restriccion.getValor();
					palabraComunicacion=  " and ";
				}
			}
			else if (criteriosBusqueda[i].equals("realizaInstitucion"))
			{
				if (!restriccion.getRealizaInstitucion().equals(""))
				{
					consultaCodigos=consultaCodigos + palabraComunicacion + " s.realiza_institucion='" + restriccion.getRealizaInstitucion()+"'";
					palabraComunicacion=  " and ";
				}
			}
			else if (criteriosBusqueda[i].equals("tipoServicio"))
			{
				if (restriccion.getTipoServicio()!=null)
				{
					consultaCodigos=consultaCodigos + palabraComunicacion + " s.tipo_servicio='" + restriccion.getTipoServicio() +"'";
					palabraComunicacion=  " and ";
				}
			}
			
			else if (criteriosBusqueda[i].equals("naturalezaServicio"))
			{
				if (restriccion.getNaturalezaServicio()!=null)
				{
					consultaCodigos=consultaCodigos + palabraComunicacion + " s.naturaleza_servicio='" + restriccion.getNaturalezaServicio() +"'";
					palabraComunicacion=  " and ";
				}
			}
			else if (criteriosBusqueda[i].equals("codigoPos"))
			{
				if (restriccion.getEsPos())
				{
					consultaCodigos=consultaCodigos + palabraComunicacion + " s.espos=" + ValoresPorDefecto.getValorTrueParaConsultas() + " ";
				}
				else
				{
					consultaCodigos=consultaCodigos + palabraComunicacion + " s.espos=" + ValoresPorDefecto.getValorFalseParaConsultas() + " ";
				}
				palabraComunicacion=  " and ";
			}
			else if (criteriosBusqueda[i].equals("esPosSubsidiado"))
			{
				//Si me ponen de restricción -1, debemos buscar los que tengan este campo en nulo
				if (!UtilidadTexto.isEmpty(restriccion.getEsPosSubsidiado()))
				{
					consultaCodigos=consultaCodigos + palabraComunicacion + " s.espossubsidiado='" + restriccion.getEsPosSubsidiado()+"' ";
					palabraComunicacion=  " and ";
				}
			}
			else if (criteriosBusqueda[i].equals("formulario"))
			{
				//Si me ponen de restricción -1, debemos buscar los que tengan este campo en nulo
				if (restriccion.getCodigoFormulario()!=-1)
				{
					consultaCodigos=consultaCodigos + palabraComunicacion + " frs.plantilla=" + restriccion.getCodigoFormulario();
					palabraComunicacion=  " and ";
				}
				else
				{
					consultaCodigos=consultaCodigos + palabraComunicacion + " s.codigo not in (select frst.servicio from form_resp_serv frst) ";
					palabraComunicacion=  " and ";
				}
			}
			else if (criteriosBusqueda[i].equals("nivel"))
			{
				//Si me ponen de restricción "", debemos buscar los que tengan este campo en nulo
				if (!restriccion.getNivel().trim().equals(""))
				{
					consultaCodigos=consultaCodigos + palabraComunicacion + " s.nivel = "+restriccion.getNivel();
					palabraComunicacion=  " and ";
				}
			}
			else if (criteriosBusqueda[i].equals("activo"))
			{
				if (restriccion.getActivo() > 0)
					consultaCodigos=consultaCodigos + palabraComunicacion + " s.activo=" + ValoresPorDefecto.getValorTrueParaConsultas() + " ";
				else
					consultaCodigos=consultaCodigos + palabraComunicacion + " s.activo=" + ValoresPorDefecto.getValorFalseParaConsultas() + " ";
				
				palabraComunicacion=  " and ";
			}
			else if (criteriosBusqueda[i].equals("unidadesUvr"))
			{
				if (restriccion.getUnidadesUvr()!=-1 )
				{
					consultaCodigos=consultaCodigos + palabraComunicacion + " s.unidades_uvr = "+restriccion.getUnidadesUvr();
				}
				else
				{
					consultaCodigos=consultaCodigos + palabraComunicacion + " s.unidades_uvr IS NULL ";
				}
				palabraComunicacion=  " and ";
			}
			
			else if(criteriosBusqueda[i].equals("grupoServicio"))
			{
				if(restriccion.getGrupoServicio()>=0)
				{
					consultaCodigos=consultaCodigos + palabraComunicacion + " s.grupo_servicio=" + restriccion.getGrupoServicio();
					palabraComunicacion=  " and ";
				}
			}
			
			else if(criteriosBusqueda[i].equals("atencionOdontologica"))
			{
				if(!restriccion.getAtencionOdontologica().toString().equals(""))
				{
					consultaCodigos=consultaCodigos + palabraComunicacion + " s.atencion_odontologica='"+restriccion.getAtencionOdontologica()+"'";
					palabraComunicacion=  " and ";
				}
			}
			
			//Otras tablas (No se pueden agrupar codigo y descripcion porque vienen en indices diferentes
			
			else if (criteriosBusqueda[i].equals("codigoCups"))
			{
				if (restriccion.getCodigoCups()!=null)
				{
					joinCups=" INNER JOIN referencias_servicio refser1 ON (refser1.tipo_tarifario="+ ConstantesBD.codigoTarifarioCups + " and ser.codigo=refser1.servicio " + restCups +")";
					restCups+=" and UPPER(refser1.codigo_propietario) like '%" + (restriccion.getCodigoCups()).toUpperCase() + "%' ";
				}
			}
			
			else if (criteriosBusqueda[i].equals("descripcionCups"))
			{
				if (restriccion.getDescripcionCups()!=null)
				{
					joinCups=" INNER JOIN referencias_servicio refser1 ON (refser1.tipo_tarifario="+ ConstantesBD.codigoTarifarioCups + " and ser.codigo=refser1.servicio " + restCups +")";
					restCups+=" and UPPER(refser1.descripcion) like '%" + (restriccion.getDescripcionCups()).toUpperCase() + "%'";

				}
			}
		/**
		 	else if (criteriosBusqueda[i].equals("codigoISS"))
			{
				if (restriccion.getCodigoISS()!=null)
				{
					joinISS=" INNER JOIN referencias_servicio refser2 ON(refser2.tipo_tarifario=" + ConstantesBD.codigoTarifarioISS +" and ser.codigo=refser2.servicio " + restISS +")";
					restISS+=" and UPPER(refser2.codigo_propietario) like '%" + (restriccion.getCodigoISS()).toUpperCase() + "%'";
				}
			}
			else if (criteriosBusqueda[i].equals("descripcionISS"))
			{
				if (restriccion.getDescripcionISS()!=null)
				{
					joinISS=" INNER JOIN referencias_servicio refser2 ON( refser2.tipo_tarifario=" + ConstantesBD.codigoTarifarioISS +" and ser.codigo=refser2.servicio " + restISS +")";
					restISS+=" and UPPER(refser2.descripcion) like '%" + (restriccion.getDescripcionISS()).toUpperCase() + "%'";
				}
			}
			else if (criteriosBusqueda[i].equals("codigoSOAT"))
			{
				if (restriccion.getCodigoSOAT()!=null)
				{
					joinSoat=" INNER JOIN referencias_servicio refser3 ON ( refser3.tipo_tarifario="+ ConstantesBD.codigoTarifarioSoat +" and ser.codigo=refser3.servicio " + restSoat + ")";
					restSoat+=" and UPPER(refser3.codigo_propietario) like '%" + (restriccion.getCodigoSOAT()).toUpperCase() + "%'";
				}
			}
			else if (criteriosBusqueda[i].equals("descripcionSOAT"))
			{
				if (restriccion.getDescripcionSOAT()!=null)
				{
					joinSoat=" INNER JOIN referencias_servicio refser3 ON ( refser3.tipo_tarifario="+ ConstantesBD.codigoTarifarioSoat +" and ser.codigo=refser3.servicio " + restSoat + ")";
					restSoat+=" and UPPER(refser3.descripcion) like '%" + (restriccion.getDescripcionSOAT()).toUpperCase() + "%'";
				}
			}		
			
			else if (criteriosBusqueda[i].equals("codigoSONRIA"))
			{
				if (restriccion.getCodigoSONRIA()!=null)
				{
					joinSonria=" INNER JOIN referencias_servicio refser4 ON ( refser4.tipo_tarifario="+ ConstantesBD.codigoTarifarioSonria +" and ser.codigo=refser4.servicio " + restSonria + ")";
					restSonria+=" and UPPER(refser4.codigo_propietario) like '%" + (restriccion.getCodigoSONRIA()).toUpperCase() + "%'";
				}
			}
			else if (criteriosBusqueda[i].equals("descripcionSONRIA"))
			{
				if (restriccion.getDescripcionSONRIA()!=null)
				{
					joinSonria=" INNER JOIN referencias_servicio refser4 ON ( refser4.tipo_tarifario="+ ConstantesBD.codigoTarifarioSonria +" and ser.codigo=refser4.servicio " + restSonria + ")";
					restSonria+=" and UPPER(refser4.descripcion) like '%" + (restriccion.getDescripcionSONRIA()).toUpperCase() + "%'";
				}
			}
			
		*/
			else if (criteriosBusqueda[i].equals("unidadesSoat"))
			{
				joinSoat=" INNER JOIN referencias_servicio refser3 ON ( refser3.tipo_tarifario="+ ConstantesBD.codigoTarifarioSoat +" and ser.codigo=refser3.servicio " + restSoat + ")";
				if (restriccion.getUnidadesSoat()!=-1 )
					restSoat+=" and refser3.unidades = "+restriccion.getUnidadesSoat();
				else
					restSoat+=" and refser3.unidades IS NULL ";
			}

		} // Cierro el for
		
		
		
		if(codigosTarifarios!=null && codigosTarifarios.length>0)
		{	
			int contCodTarif=2;
			ArrayList<Integer> codigos= new ArrayList<Integer>();
			
				for (int j=0;j<codigosTarifarios.length;j++)
				{
					joinAux="";
					restAux="";
					if(!codigosTarifarios[j].equals(""))
					{	
					       String tarifario[]=codigosTarifarios[j].split(ConstantesBD.separadorSplit);
										  
							//estructura Tarifarios = codigoTipoTarifario+separadorSplit+textoIntroducido+separadorSplit+tipo(codigo-descripcion)+separadorSplit+posicion
							
							//0=codigoTarifario
							//1=valorBusqueda
							//2=tipoCriterio ( codigo-descripcion)
							//3=posicion
							
							
								if(tarifario!=null && tarifario.length>0)			
								{			   
								  
									if(!tarifario[1].isEmpty() &&  tarifario[2].equals("codigo"))
									{
										joinAux=" INNER JOIN referencias_servicio refser"+contCodTarif+" ON ( refser"+contCodTarif+".tipo_tarifario="+ tarifario[0] +" and ser.codigo=refser"+contCodTarif+".servicio " + restAux + ") ";
										restAux+=" and UPPER(refser"+contCodTarif+".codigo_propietario) like '%" + tarifario[1].toUpperCase() + "%'";
										
									}
									else
									{
									  if(!tarifario[1].isEmpty() &&  tarifario[2].equals("descripcion"))
									   {
										  if(!codigos.contains(Utilidades.convertirAEntero(tarifario[3])))
										   {  
									        joinAux=" INNER JOIN referencias_servicio refser"+contCodTarif+" ON ( refser"+contCodTarif+".tipo_tarifario="+ tarifario[0] +" and ser.codigo=refser"+contCodTarif+".servicio " + restAux + ") ";
									        restAux+=" and UPPER(refser"+contCodTarif+".descripcion) like '%" + tarifario[1].toUpperCase() + "%'";   
										   }else
									       {
									    	 restAux+=" and UPPER("+nombreTablaExistente(codigosTarifarios,Utilidades.convertirAEntero(tarifario[3]))+".descripcion) like '%" + tarifario[1].toUpperCase() + "%'"; 
									       }									        
										 										   
										
									   }
									}									
									codigos.add(Utilidades.convertirAEntero(tarifario[3]));		 
								}
								
								contCodTarif++;
						}						
							
							joinGenerico+=joinAux;
							restGenerico+=restAux;
					   }
					
				}
		    
		
		
		//Después de empezar a usar INNER JOIN, estas
		//instrucciones no servían, es más, generaban
		//error
		/*if (joinCups.length()>=1)
		{
			restCups=" and refser1.tipo_tarifario="+ ConstantesBD.codigoTarifarioCups + " and ser.codigo=refser1.servicio " + restCups;
		}
		if (joinISS.length()>=1)
		{
			restISS=" and refser2.tipo_tarifario=" + ConstantesBD.codigoTarifarioISS +" and ser.codigo=refser2.servicio " + restISS;
		}
		if (joinSoat.length()>=1)
		{
			restSoat=" and refser3.tipo_tarifario="+ ConstantesBD.codigoTarifarioSoat +" and ser.codigo=refser3.servicio " + restSoat; 
		}*/
		
		String consultaFinal="SELECT " +
								"ser.codigo, " +
								"ser.especialidad as codigoEspecialidad, " +
								"esp.nombre as especialidad, " +
								"ser.tipo_servicio as codigoTipo, " +
								"tip.nombre as tipo, " +
								"ser.naturaleza_servicio as codigoNaturaleza, " +
								"nat.nombre as naturaleza, " +
								"ser.sexo as codigoSexo, " +
								//"ser.formulario as codigoFormulario, " +
								"ser.espos, " +
								"ser.espossubsidiado,"+
								"ser.activo, " +
								"ser.unidades_uvr AS unidadesUvr,  " +
								"gs.codigo AS codigoGrupo,  " +
								"gs.acronimo || '-' || gs.descripcion AS grupo,  " +
								"ser.requiere_interpretacion as requiereInterpretacion, " +
								"coalesce(ser.requiere_diagnostico,'') as requiereDiagnostico, " +
								"ser.costo||'' as costo, " +
								"ser.realiza_institucion as realizaInstitucion, " +
								"ser.nivel AS nivel, " +
								"coalesce(ser.portatil_asociado, -1) as portatilAsociado, "+
								"getnombreservicio(coalesce(ser.portatil_asociado, ser.codigo),"+codigoTarifario+") as descripcionServicio,"+
								"getcodigoespecialidad(coalesce(ser.portatil_asociado, -1)) || '-' || coalesce(ser.portatil_asociado, -1) || ' ' || getnombreservicio(coalesce(ser.portatil_asociado, -1),"+codigoTarifario+") as desPortatilAsociado, "+
								"ns.descripcion AS descNivel, " +
								"coalesce(getobtenercodigocupsserv(ser.codigo, "+codigoTarifario+"), '') AS codigoCups," +
								"ser.atencion_odontologica AS atencionodontologica," +
								"ser.convencion," +
								"ser.minutos_duracion AS minutosduracion," +
								"coalesce(con.archivo_convencion,'') AS archivo " +
							"FROM servicios ser " +
							"INNER JOIN especialidades esp ON (ser.especialidad=esp.codigo) " +
							"INNER JOIN tipos_servicio tip ON (ser.tipo_servicio=tip.acronimo ) " +
							"INNER JOIN naturalezas_servicio nat ON (ser.naturaleza_servicio=nat.acronimo ) " +
							"INNER JOIN grupos_servicios gs ON (ser.grupo_servicio=gs.codigo) " +
							"INNER JOIN nivel_atencion ns ON (ns.consecutivo=ser.nivel) " +
							"LEFT OUTER JOIN odontologia.convenciones_odontologicas con ON (con.consecutivo=ser.convencion) " +
							joinCups + joinGenerico + " " +
							"LEFT OUTER JOIN referencias_servicio refserord ON (ser.codigo=refserord.servicio and refserord.tipo_tarifario=" + codigoTarifario + ") " +
							"WHERE " +
								"ser.codigo IN (" + consultaCodigos + ") " + restCups + restGenerico + " order by ser.codigo asc";
		
		logger.info("\n\n\nConsulta Final de Servicios: "+consultaFinal);
		PreparedStatementDecorator pst=new PreparedStatementDecorator(con.prepareStatement(consultaFinal,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
		
		HashMap resultado=UtilidadBD.cargarValueObject(rs);
		rs.close();
		pst.close();
		return resultado;
	}

	
	/**
	 * Metodo encargado de Consultar el NOMBRE de la Tabla (busqueda dinamica del CodigoTarifario) utilizada en la Busqueda de Servicios
	 * @param arreglo
	 * @param posicion
	 * @return
	 */
	private static String nombreTablaExistente(String[] arreglo, int posicion)
	{
		String nombreTabla="";
		int cont=2;
		for(int j=0;j<arreglo.length;j++)
		{
			if(!arreglo[j].equals(""))
			{	
			     String tarifario[]=arreglo[j].split(ConstantesBD.separadorSplit);
			     if(tarifario[3].equals(posicion+""))
			     {
			    	 return "refser"+cont;
			     }
			     cont++;
			}
		}
		
		return nombreTabla;
	}
	
	/**
	 * Implementación del método que busca los códigos de los servicios
	 * ya usados en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.ServiciosDao#busquedaServiciosYaUsados (Connection ) throws SQLException 
	 */
	public static Collection busquedaServiciosYaUsados (Connection con) throws SQLException
	{
		Collection col=new ArrayList();
		PreparedStatementDecorator busquedaServiciosYaUsadosStatement= new PreparedStatementDecorator(con.prepareStatement(busquedaServiciosYaUsadosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ResultSetDecorator rs=new ResultSetDecorator(busquedaServiciosYaUsadosStatement.executeQuery());
		while (rs.next())
		{
			col.add(rs.getString("codigoServicio"));
		}
		rs.close();
		return col;
	}

	/**
	 * Implementación del método que inserta un servicio en una BD Genérica
	 * @param esPosSubsidiado 
	 * @param tipoBD 
	 * 
	 * @see com.princetonsa.dao.ServiciosDao#insertarServicio (Connection , int , String , String , int , int , int , boolean ) throws SQLException 
	 */
	public static int insertarServicio (Connection con, int codigoEspecialidad, String codigoTipoServicio, 
			String codigoNaturalezaServicio, int codigoSexo, HashMap formularios, 
			boolean esPos, String esPosSubsidiado, boolean activo, double unidadesUvr, int grupoServicio, 
			String insertarServicioStr, String nivel, String requiereInterpretacion, String costo, String realizaInstitucion,
			String requiereDiagnostico, int codigoServicioPortatil, int codigoInstitucion, int tipoBD, String atencionOdontologica, int convencion, int minutosDuracion) throws SQLException
	{
		logger.info("insertarServicioStr---------->>>>>>>>>>>>>>>>"+insertarServicioStr
				+"\ncodigoEspecialidad--------"+codigoEspecialidad
				+"\ncodigoTipoServicio--------"+ codigoTipoServicio
				+"\ncodigoNaturalezaServicio--"+codigoTipoServicio
				+"\ncodigoSexo----------------"+codigoSexo
				+"\nesPos---------------------"+esPos
				+"\nesPosSubsidiado-----------"+esPosSubsidiado
				+"\nactivo--------------------"+activo
				+"\nunidadesUvr---------------"+unidadesUvr
				+"\ngrupoServicio-------------"+grupoServicio
				+"\nnivel---------------------"+Utilidades.convertirAEntero(nivel)
				+"\nrequiereInterpretacion----"+requiereInterpretacion
				+"\ncosto---------------------"+Utilidades.convertirADouble(costo)
				+"\nrealizaInstitucion--------"+realizaInstitucion
				+"\nrequiereDiagnostico-------"+requiereDiagnostico
				+"\ncodigoServicioPortatil----"+codigoServicioPortatil
				+"\natencionodontologica----"+atencionOdontologica
				+"\nconvencion----"+convencion
				+"\nminutosduracion----"+minutosDuracion);
		
		PreparedStatementDecorator insertarServicioStatement= new PreparedStatementDecorator(con.prepareStatement(insertarServicioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		insertarServicioStatement.setInt(1, codigoEspecialidad);
		insertarServicioStatement.setString(2, codigoTipoServicio);
		insertarServicioStatement.setString(3, codigoNaturalezaServicio);
		
		if (codigoSexo>0)
		{
			insertarServicioStatement.setInt(4, codigoSexo);
		}
		else
		{
			insertarServicioStatement.setObject(4, null);
		}
		
		switch(tipoBD){
			case DaoFactory.ORACLE:
				
				if (esPos == true){
					insertarServicioStatement.setInt(5, 1);
				}
				else if(esPos == false){
					insertarServicioStatement.setInt(5, 0);
				}
				
				insertarServicioStatement.setString(6, esPosSubsidiado);
				
				if (activo == true){
					insertarServicioStatement.setInt(7, 1);
				}
				else if(activo == false){
					insertarServicioStatement.setInt(7, 0);
				}
				
				break;
			case DaoFactory.POSTGRESQL:
				insertarServicioStatement.setBoolean(5, esPos);
				insertarServicioStatement.setString(6, esPosSubsidiado);
				insertarServicioStatement.setBoolean(7, activo);
				break;
			default:
				break;
				
		}
				
		if(unidadesUvr<0)
			insertarServicioStatement.setObject(8,null);
		else
			insertarServicioStatement.setDouble(8, unidadesUvr);
		
		insertarServicioStatement.setInt(9,grupoServicio);
		
		insertarServicioStatement.setInt(10, Utilidades.convertirAEntero(nivel));
		
		if(requiereInterpretacion==null || requiereInterpretacion.trim().equals(""))
			insertarServicioStatement.setObject(11, ConstantesBD.acronimoNo);
        else
        	insertarServicioStatement.setString(11,requiereInterpretacion);

		if (Utilidades.convertirADouble(costo) < 0)
		{
			insertarServicioStatement.setObject(12, null);
		}
		else
		{
			insertarServicioStatement.setDouble(12, Utilidades.convertirADouble(costo));
		}
				
		
		
		//insertarServicioStatement.setString(12, costo);
		
		insertarServicioStatement.setString(13, realizaInstitucion);
		
		if(!requiereDiagnostico.equals(""))
			insertarServicioStatement.setString(14,requiereDiagnostico);
		else
			insertarServicioStatement.setNull(14, Types.VARCHAR);
		
		if(codigoServicioPortatil != ConstantesBD.codigoNuncaValido)
			insertarServicioStatement.setInt(15, codigoServicioPortatil);
		else
			insertarServicioStatement.setNull(15, Types.INTEGER);
		
		insertarServicioStatement.setString(16, atencionOdontologica);
		
		if(convencion != ConstantesBD.codigoNuncaValido)
			insertarServicioStatement.setInt(17, convencion);
		else
			insertarServicioStatement.setNull(17, Types.INTEGER);

		if(minutosDuracion != ConstantesBD.codigoNuncaValido)
			insertarServicioStatement.setInt(18, minutosDuracion);
		else
			insertarServicioStatement.setNull(18, Types.INTEGER);
		
		if (insertarServicioStatement.executeUpdate()<1)
		{
			return 0;
		}
		
		//Llegados a este punto sabemos que la inserción salió correctamente
		//ahora vamos a buscar el código del insertado
		
		PreparedStatementDecorator buscarCodigoServicioInsertadoStatement= new PreparedStatementDecorator(con.prepareStatement(buscarCodigoServicioInsertadoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		buscarCodigoServicioInsertadoStatement.setInt(1, codigoEspecialidad);
		buscarCodigoServicioInsertadoStatement.setString(2, codigoTipoServicio);
		buscarCodigoServicioInsertadoStatement.setString(3, codigoNaturalezaServicio);
		buscarCodigoServicioInsertadoStatement.setBoolean(4, esPos);
		buscarCodigoServicioInsertadoStatement.setString(5, esPosSubsidiado);
		
		ResultSetDecorator rs=new ResultSetDecorator(buscarCodigoServicioInsertadoStatement.executeQuery());
		int codigoServicio = ConstantesBD.codigoNuncaValido;
		
		if (rs.next())
			 codigoServicio = rs.getInt("codigoServicio");
		
		//***********SE INSERTAN LOS FORMULARIOS DEL SERVICIO**********************************
		if(codigoServicio>0)
		{
			if(insertarModificarEliminarFormularios(con, formularios, codigoServicio, codigoInstitucion)<=0)
				codigoServicio = 0;
		}
		//***************************************************************************************
		
		return codigoServicio;
		
	}
	
	/**
	 * Método para insertar/modificar/eliminar los formularios de un servicio
	 * @param con
	 * @param formularios
	 * @param codigoServicio
	 * @param codigoInstitucion
	 * @return
	 */
	private static int insertarModificarEliminarFormularios(Connection con,HashMap formularios,int codigoServicio,int codigoInstitucion)
	{
		int respuesta = 1;
		String consulta = "";
		PreparedStatementDecorator pst = null;
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			for(int i=0;i<Utilidades.convertirAEntero(formularios.get("numRegistros")+"");i++)
			{
				if(UtilidadTexto.getBoolean(formularios.get("activo_"+i)+""))
				{
					//Si inicialmente no hay consecutivo se trata de verificar si ya existe un registro similar
					//que no tenga servicio asociado para asociarle el servicio
					if(Utilidades.convertirAEntero(formularios.get("consecutivo_"+i)+"")<=0)
					{
						//Se verifica si ya existe una parametrización libre para insertar el servicio
						consulta = "select " +
							"codigo_pk as consecutivo " +
							"from form_resp_serv " +
							"WHERE " +
							"plantilla = "+formularios.get("codigoPkPlantilla_"+i)+" and " +
							"institucion = "+codigoInstitucion+" and servicio is null";
						st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
						rs =  new ResultSetDecorator(st.executeQuery(consulta));
						
						if(rs.next())
							formularios.put("consecutivo_"+i, rs.getString("consecutivo"));
						else
							formularios.put("consecutivo_"+i, ConstantesBD.codigoNuncaValido+"");
					}
					
					if(Utilidades.convertirAEntero(formularios.get("consecutivo_"+i)+"")>0)
					{
						//Se realiza actualización del servicio en el registro
						consulta = "UPDATE form_resp_serv SET servicio = ? WHERE codigo_pk = ?";
						pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						pst.setInt(1,codigoServicio);
						pst.setInt(2,Utilidades.convertirAEntero(formularios.get("consecutivo_"+i)+""));
						if(pst.executeUpdate()<=0)
							respuesta = 0;
					}
					else
					{
						//Se realiza inserción del formulario asociado al servicio
						consulta = "INSERT INTO form_resp_serv (codigo_pk,institucion,plantilla,servicio,diag_proc_form) VALUES (?,?,?,?,?)";
						int consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_form_resp_serv");
						pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						pst.setInt(1,consecutivo);
						pst.setInt(2,codigoInstitucion);
						pst.setInt(3,Integer.parseInt(formularios.get("codigoPkPlantilla_"+i)+""));
						pst.setInt(4,codigoServicio);
						if(Utilidades.convertirAEntero(formularios.get("codigoDiagnostico_"+i)+"")>0)
							pst.setInt(5,Utilidades.convertirAEntero(formularios.get("codigoDiagnostico_"+i)+""));
						else
							pst.setNull(5,Types.INTEGER);
						
						if(pst.executeUpdate()<=0)
							respuesta = 0;
						
					}
				}
				//Si no está activo y tiene consecutivo se intenta eliminar
				else if(Utilidades.convertirAEntero(formularios.get("consecutivo_"+i)+"")>0)
				{
					//Se realiza la eliminación del registro
					consulta = "DELETE FROM form_resp_serv WHERE codigo_pk = ?";
					pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setInt(1,Utilidades.convertirAEntero(formularios.get("consecutivo_"+i)+""));
					if(pst.executeUpdate()<=0)
						respuesta = 0;
				}
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarModificarEliminarFormularios: "+e);
		}
		return respuesta;
	}
	
	/**
	 * busqueda de servicios dado el codigo axioma o codigo cups o codigo iss o codigo soat o descripcion cups 
	 * @param con
	 * @param codigoAxioma
	 * @param codigoPropietarioCups
	 * @param codigoPropietarioIss
	 * @param codigoPropietarioSoat
	 * @param descripcionCups
	 * @param codigoSexo
	 * @param codigosServiciosInsertados
	 * @return
	 * @throws SQLException
	 */
	public static  ResultSetDecorator busquedaAvanzadaServiciosXCodigos(	Connection con,
										        				String codigoAxioma,
																String codigoPropietarioCups,
																String codigoPropietarioIss,
																String codigoPropietarioSoat,
																String descripcionCups,
																int codigoSexo,
                                                                int codigoConvenioResponsable,
                                                                int codigoContratoCuenta
															  ) throws SQLException
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
			consultaArmada=armarConsulta(		codigoAxioma, codigoPropietarioCups, codigoPropietarioIss, codigoPropietarioSoat, descripcionCups, codigoSexo);
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultaArmada,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, codigoContratoCuenta);
			respuesta=new ResultSetDecorator(ps.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn("Error en la búsqueda avanzada de los servicios " +e.toString());
			respuesta=null;
		}
		return respuesta;
	}
	
	/**
	 * Metodo que arma la consulta de los servicios
	 * @param codigoAxioma
	 * @param codigoPropietarioCups
	 * @param codigoPropietarioIss
	 * @param codigoPropietarioSoat
	 * @param descripcionCups
	 * @param codigoSexo
	 * @param codigosServiciosInsertados
	 * @return
	 */
	private static String armarConsulta  (		String codigoAxioma,
																String codigoPropietarioCups,
																String codigoPropietarioIss,
																String codigoPropietarioSoat,
																String descripcionCups,
																int codigoSexo
																)
	{
		String consulta= busquedaAvanzadaServiciosPorCodigos;
		consulta+=" AND (s.sexo="+codigoSexo+" OR s.sexo IS NULL) ";
		
		if(codigoAxioma != null && !codigoAxioma.equals(""))
		{
		    consulta+=" AND refs.servicio = "+codigoAxioma+" ";
			//consulta = consulta + "AND UPPER(e.razon_social) LIKE  UPPER('%"+razonSocial+"%')  ";
		}
		
		if(codigoPropietarioCups != null && !codigoPropietarioCups.equals(""))
		{
			consulta += " AND (refs.tipo_tarifario= "+ConstantesBD.codigoTarifarioCups+" AND refs.codigo_propietario= '"+codigoPropietarioCups+"' ) ";
		}
		
		if(codigoPropietarioIss != null && !codigoPropietarioIss.equals(""))
		{
			consulta += " AND (refs.tipo_tarifario= "+ConstantesBD.codigoTarifarioISS+" AND refs.codigo_propietario= '"+codigoPropietarioIss+"' ) ";
		}
		
		if(codigoPropietarioSoat != null && !codigoPropietarioSoat.equals(""))
		{
			consulta += " AND (refs.tipo_tarifario= "+ConstantesBD.codigoTarifarioSoat+" AND refs.codigo_propietario= '"+codigoPropietarioSoat+"' ) ";
		}
		
		if(descripcionCups != null && !descripcionCups.equals(""))
		{
			consulta +=" AND (refs.tipo_tarifario= "+ConstantesBD.codigoTarifarioCups+" AND UPPER(refs.descripcion) LIKE UPPER('%"+descripcionCups+"%') )";
		}
		
		consulta+=" ORDER BY s.codigo ";
		return consulta;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public static HashMap consultarTarifarios(Connection con, int codigoServicio) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			String cadena="SELECT codigo,nombre,codigo_propietario as codigopropietario,descripcion, case when tipo_tarifario is null then 'Mem' else 'BD' end as tiporegistro  from tarifarios_oficiales tof left outer join referencias_servicio rs on(rs.tipo_tarifario=tof.codigo and rs.servicio=?) where codigo>2 "; //mayor al codigo 2, toca dejar quemado por que soat - iss ya esta quemado en muchas partes.  
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoServicio);
			ResultSetDecorator resultado=new ResultSetDecorator(pst.executeQuery());
			mapa=UtilidadBD.cargarValueObject(resultado);
			resultado.close();
			pst.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarTarifarios: "+e);
		}
		return mapa;
	}
	
	
	

	
	/**
	 * Implementación del método que carga el Automatico
	 * en una BD Genérica. Es de la utilidad de servicios automaticos de presupuesto.
	 * @see com.princetonsa.dao.ServiciosDao#cargarServiciosautomatico (Connection , String )
	 */
	public static HashMap cargarServiciosautomatico (Connection con, String automatic)
	{
		HashMap mapa = new HashMap();	
		try
		{
		
			PreparedStatementDecorator cargarServiciosautomaticoStatement= new PreparedStatementDecorator(con.prepareStatement(descripcionAutomaticoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarServiciosautomaticoStatement.setString(1, automatic);
	
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarServiciosautomaticoStatement.executeQuery()));
			mapa.put("INDICES_MAPA", indicesMapa);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return mapa;
		
	}
	
		
	/**
	 * Implementación del método que modificar el Automatico en la base de datos
	 * Es para cambiar el estado del campo automatico; indispensable para mostrar
	 * los servicios que tienen el acronimoDBsi en el campo automatico.
	 * 
	 * @see com.princetonsa.dao.ServiciosDao#modificarAutomatico (Connection , String) 
	 */
	public static boolean actualizarAutomatico (Connection con, HashMap vo)
	{
		try
		{
			PreparedStatementDecorator actualizarAutomaticoStatement= new PreparedStatementDecorator(con.prepareStatement(actualizarAutomaticoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			actualizarAutomaticoStatement.setObject(1, vo.get("automatico"));
			logger.info("ingresa automatico valor ---->    "+vo.get("automatico"));
			logger.info("el codigo ---->    "+vo.get("codigo"));
			actualizarAutomaticoStatement.setObject(2, vo.get("codigo"));
			
			if(actualizarAutomaticoStatement.executeUpdate()>0)
				return true;

		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;	
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @param tipoTarifario
	 * @return
	 */
	public static String obtenerCodigoTarifarioServicio (Connection con, int codigoServicio, int tipoTarifario)
	{
		String consulta="SELECT getcodigopropservicio2("+codigoServicio+", "+tipoTarifario+") as cod "; 
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info(" obtenerCodigoTarifarioServicio---->    "+consulta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("cod");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return "";	
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @param tipoTarifario
	 * @return
	 */
	public static String obtenerCodigoTarifarioServicioConDesc (Connection con, int codigoServicio, int tipoTarifario)
	{
		String consulta="SELECT getcodigopropservicio("+codigoServicio+", "+tipoTarifario+") as cod "; 
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info(" obtenerCodigoTarifarioServicio---->    "+consulta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("cod");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return "";	
	}
	
	
	/**
	 * Método para obtener el arreglo de los formularios para asignarle al servicio
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static ArrayList<DtoPlantillaServDiag> obtenerArregloFormularios(Connection con,int codigoInstitucion)
	{
		ArrayList<DtoPlantillaServDiag> resultados = new ArrayList<DtoPlantillaServDiag>();
		try
		{
			String consulta = "SELECT distinct "+
				"p.codigo_pk as codigo_pk_plantilla, "+
				"p.codigo_plantilla as codigo_plantilla, "+
				"p.nombre_plantilla as nombre_plantilla, "+
				"coalesce(dpf.codigo_pk,"+ConstantesBD.codigoNuncaValido+") as codigo_diagnostico, "+
				"coalesce(dpf.descripcion,'') as nombre_diagnostico "+
				"FROM plantillas p "+
				"LEFT OUTER JOIN form_resp_serv frs ON(frs.plantilla = p.codigo_pk) "+
				"LEFT OUTER JOIN diag_procedimientos_form dpf ON(dpf.codigo_pk = frs.diag_proc_form) "+
				"WHERE "+
				"p.fun_param = "+ConstantesCamposParametrizables.funcParametrizableRespuestaProcedimientos+" and p.institucion = "+codigoInstitucion+" and p.mostrar_modificacion = '"+ConstantesBD.acronimoSi+"' " +
				"ORDER BY nombre_plantilla,nombre_diagnostico ";
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			while(rs.next())
			{
				DtoPlantillaServDiag elemento = new DtoPlantillaServDiag();
				elemento.setCodigoPkPlantilla(rs.getInt("codigo_pk_plantilla"));
				elemento.setCodigoPlantilla(rs.getInt("codigo_plantilla"));
				elemento.setDescripcionPlantilla(rs.getString("nombre_plantilla"));
				elemento.setCodigoDiagnostico(rs.getInt("codigo_diagnostico"));
				elemento.setDescripcionDiagnostico(rs.getString("nombre_diagnostico"));
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerArregloFormularios: "+e);
		}
		return resultados;
	}
	
	
	/**
	 * Método para cargar el numero de formularios de un servicio
	 * @param con
	 * @param codigoServicio
	 * @param codigoInstitucion
	 * @return
	 */
	public static int getNumeroFormulariosServicio(Connection con,int codigoServicio,int codigoInstitucion)
	{		
		try
		{
			String consulta = "SELECT "+
				"count(p.codigo_pk) " +				 
				"FROM form_resp_serv frs "+ 
				"INNER JOIN plantillas p ON(p.codigo_pk = frs.plantilla) "+				
				"WHERE "+ 
				"frs.servicio = ? and frs.institucion = ? and p.mostrar_modificacion = '"+ConstantesBD.acronimoSi+"'";
			
			//logger.info("valor de numero >> "+consulta+" >> "+codigoServicio);
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoServicio);
			pst.setInt(2,codigoInstitucion);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				return rs.getInt(1);	
			

		}
		catch(SQLException e)
		{
			logger.error("Error en getNumeroFormulariosServicio: "+e);
		}
		
		return 0;
	}
	
	
	/**
	 * Método para cargar los formularios de un servicio
	 * @param con
	 * @param codigoServicio
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap<String, Object> cargarFormulariosServicio(Connection con,int codigoServicio,int codigoInstitucion)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			String consulta = "SELECT "+
				"frs.codigo_pk as consecutivo, "+ 
				"p.codigo_pk as codigo_pk_plantilla, "+ 
				"p.codigo_plantilla as codigo_plantilla, "+ 
				"p.nombre_plantilla as nombre_plantilla, "+ 
				"coalesce(dpf.codigo_pk,"+ConstantesBD.codigoNuncaValido+") as codigo_diagnostico, "+
				"coalesce(dpf.descripcion,'') as nombre_diagnostico, " +
				"'"+ConstantesBD.acronimoSi+"' as activo "+ 
				"FROM form_resp_serv frs "+ 
				"INNER JOIN plantillas p ON(p.codigo_pk = frs.plantilla) "+ 
				"LEFT OUTER JOIN diag_procedimientos_form dpf ON(dpf.codigo_pk = frs.diag_proc_form) "+ 
				"WHERE "+ 
				"frs.servicio = ? and frs.institucion = ? and p.mostrar_modificacion = '"+ConstantesBD.acronimoSi+"'";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoServicio);
			pst.setInt(2,codigoInstitucion);
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			pst.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarFormulariosServicio: "+e);
		}
		return resultados;
	}
	
	/**
	 * 
	 * @param codigoPropietario
	 * @param tipoTarifario
	 * @return
	 */
	public static int obtenerServicioDadoCodigoTarifario(Connection con, String codigoPropietario, int tipoTarifario)
	{
		String consulta="(SELECT rf.servicio FROM referencias_servicio rf WHERE rf.codigo_propietario=? and rf.tipo_tarifario=?) " +
						"UNION (SELECT rf.servicio FROM referencias_servicio rf WHERE rf.codigo_propietario=? and rf.tipo_tarifario="+ConstantesBD.codigoTarifarioCups+") ";
		
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setString(1,codigoPropietario);
			pst.setInt(2,tipoTarifario);
			pst.setString(3,codigoPropietario);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
	
			if(rs.next())
				return rs.getInt(1);	
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerServicioDadoCodigoTarifario: "+e);
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @param tipoTarifario
	 * @return
	 */
	public static String obtenerNombreServicio(Connection con, int codigoServicio, int tipoTarifario, String consultaNombreServicio)
	{
		String nombre="";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaNombreServicio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoServicio);
			ps.setInt(2,tipoTarifario);
			logger.info(" obtenerNombreServicio---->    "+consultaNombreServicio);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				nombre = rs.getString("nom");
			ps.close();
			rs.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return nombre;	
	}
	
	
	
	/**
	 * 
	 * @param codigoServicio
	 * @return
	 */
	public static int  obtenerMinutosDuracionServicio(int codigoServicio, Connection con){
		
		
		int numeroRetorno =ConstantesBD.codigoNuncaValido;
		String  consulta=" select minutos_duracion as minutosDuracion from  facturacion.servicios where codigo="+codigoServicio;
		
		
		
		try {
		
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con , consulta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				numeroRetorno = rs.getInt("minutosDuracion");
			}
			
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
			
			
		} catch (SQLException e) {
			
			Log4JManager.info(e);
			Log4JManager.error(e);
			
		}
		finally
		{
			
		}
		
		return  numeroRetorno;
	}
	
	
} 