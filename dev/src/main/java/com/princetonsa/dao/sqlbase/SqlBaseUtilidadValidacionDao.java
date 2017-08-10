/*
 * @(#)SqlBaseUtilidadValidacionDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.SimpleTimeZone;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosString;
import util.MD5Hash;
import util.RespuestaValidacion;
import util.RespuestaValidacionTratante;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a UtilidadValidacion
 *
 *	@version 1.0, Mar 24, 2004
 */
public class SqlBaseUtilidadValidacionDao 
{

	/**
	 * Cadena constante con el <i>statement</i> necesario para revisar 
	 * si existe antecedente pedíatrico dado el codigo del paciente.  
	 */
	private static final String existeAntecedentePediatricoStr="select count(*) as numResultados from antecedentes_pediatricos where codigo_paciente=?" ;

	/**
	 * Consulta para validar si para una evolucion y cuenta, su estado de salida fue muerto 
	 */
	private static final String EVOLUCION_CON_EGRESO_ESTADO_SALIDA_MUERTO = "SELECT EGRE.ESTADO_SALIDA muerto" +
			" FROM EVOLUCIONES EVO INNER JOIN EGRESOS EGRE ON EVO.CODIGO = EGRE.EVOLUCION WHERE " +
			" EGRE.ESTADO_SALIDA = "+ ValoresPorDefecto.getValorTrueParaConsultas() + 
			" AND EVO.ORDEN_SALIDA =  " + ValoresPorDefecto.getValorTrueParaConsultas() +
			" AND EGRE.CUENTA = ? AND EGRE.EVOLUCION = ? ";
	/**
	 * Para hacer logs de esta clase.
	 */
	private static Logger logger = Logger.getLogger(SqlBaseUtilidadValidacionDao.class);

	/**
	 * Implementación del método que permite obtener los permisos de acceso
	 * a una solicitud en una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#obtenerPermisosAcceso_validacionAccesoSolicitud (Connection , int , String ) throws SQLException
	 */
	public static int[] obtenerPermisosAcceso_validacionAccesoSolicitud (Connection con, int numeroSolicitud, String tipoSolicitud) throws SQLException
	{
		String validacionAccesoEvolucionStr="";
		PreparedStatementDecorator validacionAccesoEvolucionStatement;
		int respuesta[]=new int[2];
		
		
		
		if (tipoSolicitud==null)
		{
			throw new SQLException("El tipo de solicitud no puede ser nulo (validacionAccesoEvolucion)");
		}
		else if ( !(tipoSolicitud.equals(ConstantesBD.solicitudValoracion)||tipoSolicitud.equals(ConstantesBD.solicitudEvolucion)||tipoSolicitud.equals(ConstantesBD.solicitudFarmacia))   )
		{
			throw new SQLException("El tipo de solicitud no es válido (validacionAccesoEvolucion)");
		}
		

		//El caso de farmacia es especial, pues puede estar en dos
		//tablas, la tabla con referencia y la tabla "otros"
		if (tipoSolicitud.equals(ConstantesBD.solicitudFarmacia))
		{
			throw new SQLException("El tipo de solicitud Farmacia aún no ha sido implementado (validacionAccesoEvolucion)");
		}
		else
		{
			if (tipoSolicitud.equals(ConstantesBD.solicitudValoracion))
			{
				
				validacionAccesoEvolucionStr="SELECT ocupacion_solicitada as codigoOcupacionMedica, centro_costo_solicitado as codigoCentroCosto from solicitudes where numero_solicitud =?";
			}
			else if (tipoSolicitud.equals(ConstantesBD.solicitudEvolucion))
			{
				validacionAccesoEvolucionStr="SELECT sol.ocupacion_solicitada as codigoOcupacionMedica, sol.centro_costo_solicitado as codigoCentroCosto from solicitudes sol, evoluciones ev where ev.valoracion=sol.numero_solicitud and ev.codigo =?";
			}
			validacionAccesoEvolucionStatement= new PreparedStatementDecorator(con.prepareStatement(validacionAccesoEvolucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			validacionAccesoEvolucionStatement.setInt(1, numeroSolicitud);

		}

		ResultSetDecorator rs=new ResultSetDecorator(validacionAccesoEvolucionStatement.executeQuery());
		if (rs.next())
		{
			respuesta[0]=rs.getInt("codigoOcupacionMedica");
			respuesta[1]=rs.getInt("codigoCentroCosto");
		}
		else
		{
			rs.close();
			validacionAccesoEvolucionStatement.close();
			throw new SQLException ("No se encontró ninguna solicitud del tipo " + tipoSolicitud + " con número de solicitud " + numeroSolicitud );
		}
		rs.close();
		validacionAccesoEvolucionStatement.close();
		return respuesta;
	}

	/**
	 * Implementación del método que permite averiguar el número de
	 * valoraciones que hay en una cuenta en una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#numeroSolicitudesValoracionCuenta (Connection , int ) throws SQLException
	 */
	public static int numeroSolicitudesValoracionCuenta (Connection con, int idCuenta) throws SQLException
	{
		String numeroSolicitudesValoracionCuentaStr="SELECT count(1) as numResultados from solicitudes where cuenta=?";
		PreparedStatementDecorator numeroSolicitudesValoracionCuentaStatement= new PreparedStatementDecorator(con.prepareStatement(numeroSolicitudesValoracionCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		numeroSolicitudesValoracionCuentaStatement.setInt(1, idCuenta);
		int resultado=0;
		ResultSetDecorator rs=new ResultSetDecorator(numeroSolicitudesValoracionCuentaStatement.executeQuery());
		if (rs.next())
		{
			resultado=rs.getInt("numResultados");
		}
		else
		{
			rs.close();
			numeroSolicitudesValoracionCuentaStatement.close();
			throw new SQLException ("Error en un count en numeroSolicitudesValoracionCuenta");
		}
		rs.close();
		numeroSolicitudesValoracionCuentaStatement.close();
		return resultado;
	}

	/**
	 * Implementación del método que permite averiguar la fecha y
	 * hora de entrada a observación de un paciente con admisión
	 * de urgencias en una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#obtenerFechayHoraObservacionAdmisionUrgencias (Connection , int , int ) throws SQLException
	 */
	public static String[] obtenerFechayHoraObservacionAdmisionUrgencias (Connection con, int codigoAdmision, int anioAdmision) throws SQLException
	{
		String resultado[]=new String[2];
		String obtenerFechayHoraObservacionAdmisionUrgenciasStr= "SELECT to_char(fecha_ingreso_observacion,'"+ConstantesBD.formatoFechaBD+"') as fechaObservacion, hora_ingreso_observacion as horaObservacion from admisiones_urgencias where codigo=? and anio=?";
		PreparedStatementDecorator obtenerFechayHoraObservacionAdmisionUrgenciasStatement= new PreparedStatementDecorator(con.prepareStatement(obtenerFechayHoraObservacionAdmisionUrgenciasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		obtenerFechayHoraObservacionAdmisionUrgenciasStatement.setInt(1, codigoAdmision);
		obtenerFechayHoraObservacionAdmisionUrgenciasStatement.setInt(2, anioAdmision);
		ResultSetDecorator rs=new ResultSetDecorator(obtenerFechayHoraObservacionAdmisionUrgenciasStatement.executeQuery());


		if (rs.next())
		{
			resultado[0]=rs.getString("fechaObservacion");
			resultado[1]=UtilidadFecha.convertirHoraACincoCaracteres(rs.getString("horaObservacion"));

			
		}
		else
		{
			throw new SQLException ("La admisión de urgencias especificada NO existe en el sistema (obtenerFechayHoraObservacionAdmisionUrgencias)");
		}
		rs.close();
		obtenerFechayHoraObservacionAdmisionUrgenciasStatement.close();
		return resultado;
	}

	/**
	 * Implementación del método que permite averiguar si existe una
	 * evolucion para una cuenta particular con fecha superior a la dada.
	 * esta debe presentarse en el formato de la BD
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeEvolucionConFechaSuperior (Connection , int , String , String ) throws SQLException
	 */
	public static boolean existeEvolucionConFechaSuperior (Connection con, int idCuenta,String existeEvolucionConFechaSuperiorStr) throws SQLException
	{
		
		//Consulta Anterior : "(SELECT ev.numero_solicitud from evoluciones ev, solicitudes_evolucion solev where ev.numero_solicitud=solev.numero_solicitud and solev.id_cuenta=? and ev.fecha_evolucion > ?) UNION (SELECT ev.numero_solicitud from evoluciones ev, solicitudes_evolucion solev where ev.numero_solicitud=solev.numero_solicitud and solev.id_cuenta=? and ev.fecha_evolucion = ? and ev.hora_evolucion > ? )";

		boolean resultado=false;
		logger.info("-->"+existeEvolucionConFechaSuperiorStr+"--->"+idCuenta);
		PreparedStatementDecorator existeEvolucionConFechaSuperiorStatement= new PreparedStatementDecorator(con.prepareStatement(existeEvolucionConFechaSuperiorStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		existeEvolucionConFechaSuperiorStatement.setInt(1, idCuenta);
		existeEvolucionConFechaSuperiorStatement.setInt(2, idCuenta);
		
		ResultSetDecorator rs=new ResultSetDecorator(existeEvolucionConFechaSuperiorStatement.executeQuery());
		if (rs.next())
		{
			//Si existia una evolucion, por eso nos da resultado
			resultado=true;
		}
		rs.close();
		existeEvolucionConFechaSuperiorStatement.close();
		return resultado;
	}

	/**
	 * Implementación del método que busca la fecha y hora máxima
	 * de evolución para una cuenta en una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#obtenerMaximaFechaYHoraEvolucion (Connection , int ) throws SQLException
	 */
	public static String[] obtenerMaximaFechaYHoraEvolucion (Connection con, int idCuenta) 
	{
		try
		{
		
			String respuestaArreglo[];
	
			String obtenerMaximaFechaYHoraEvolucionStr="select " +
			"to_char(e.fecha_evolucion,'YYYY-MM-DD') as fecha_evolucion, " +
			"max(e.hora_evolucion) as hora_evolucion " +
			"from solicitudes s " +
			"inner join evoluciones e on(e.valoracion=s.numero_solicitud) " +
			"where " +
			"s.cuenta = ? and " +
			"e.fecha_evolucion= ( select max(ev.fecha_evolucion) from solicitudes sol, evoluciones ev where sol.numero_solicitud=ev.valoracion and sol.cuenta=?  ) group by fecha_evolucion";
			PreparedStatementDecorator obtenerMaximaFechaYHoraEvolucionStatement= new PreparedStatementDecorator(con.prepareStatement(obtenerMaximaFechaYHoraEvolucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			obtenerMaximaFechaYHoraEvolucionStatement.setInt(1, idCuenta);
			obtenerMaximaFechaYHoraEvolucionStatement.setInt(2, idCuenta);
			ResultSetDecorator rs=new ResultSetDecorator(obtenerMaximaFechaYHoraEvolucionStatement.executeQuery());
	
			if(rs.next())
			{
				respuestaArreglo=new String[2];
				respuestaArreglo[0]=rs.getString("fecha_evolucion");
				respuestaArreglo[1]=rs.getString("hora_evolucion");
				rs.close();
				obtenerMaximaFechaYHoraEvolucionStatement.close();
				return respuestaArreglo;
			}
			rs.close();
			obtenerMaximaFechaYHoraEvolucionStatement.close();
	
			return null;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerMaximaFechaYHoraEvolucion de SqlBaseUtilidadValidacionDao:"+e);
			return null;
		}

	}

	/**
	 * Implementación del método que busca la fecha y hora máxima
	 * de valoración para una cuenta en una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#obtenerMaximaFechaYHoraValoracion (Connection , int ) throws SQLException
	 */
	public static String[] obtenerMaximaFechaYHoraValoracion (Connection con, int idCuenta) throws SQLException
	{

		String respuestaArreglo[];

		String obtenerMaximaFechaYHoraValoracionStr="SELECT fecha_valoracion, max(hora_valoracion) as hora_valoracion from valoraciones val2, solicitudes sol2 where val2.numero_solicitud=sol2.numero_solicitud and sol2.cuenta=1 and val2.fecha_valoracion = (SELECT max(val.fecha_valoracion) from solicitudes sol, valoraciones val where sol.numero_solicitud=val.numero_solicitud and sol.cuenta=?) group by val2.fecha_valoracion";
		PreparedStatementDecorator obtenerMaximaFechaYHoraValoracionStatement= new PreparedStatementDecorator(con.prepareStatement(obtenerMaximaFechaYHoraValoracionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		obtenerMaximaFechaYHoraValoracionStatement.setInt(1, idCuenta);
		ResultSetDecorator rs=new ResultSetDecorator(obtenerMaximaFechaYHoraValoracionStatement.executeQuery());

		if (rs.next())
		{
			respuestaArreglo=new String[2];
			respuestaArreglo[0]=rs.getString("fecha_valoracion");
			respuestaArreglo[1]=rs.getString("hora_valoracion");
			rs.close();
			obtenerMaximaFechaYHoraValoracionStatement.close();
			return respuestaArreglo;
		}
		
		rs.close();
		obtenerMaximaFechaYHoraValoracionStatement.close();

		return null;

	}

	/**
	 * Implementación del método que revisa si una cuenta tiene
	 * valoraciones para una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#tieneValoraciones (Connection , int ) throws SQLException
	 */
	public static boolean tieneValoraciones (Connection con, int idCuenta) throws SQLException
	{
		//String tieneValoracionesStr="SELECT count(1) as numResultados from valoraciones val, solicitudes sol where cuenta=? and val.numero_solicitud=sol.numero_solicitud";
		String tieneValoracionesStr="SELECT count(1) as numResultados from valoraciones val INNER JOIN solicitudes sol on (sol.numero_solicitud=val.numero_solicitud) WHERE sol.cuenta=?";
		
		PreparedStatementDecorator tieneValoracionesStatement= new PreparedStatementDecorator(con.prepareStatement(tieneValoracionesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		tieneValoracionesStatement.setInt(1, idCuenta);
		ResultSetDecorator rs=new ResultSetDecorator(tieneValoracionesStatement.executeQuery());
		if (rs.next())
		{
			if (rs.getInt("numResultados")>0)
			{
				rs.close();
				tieneValoracionesStatement.close();
				return true;
			}
			else
			{
				rs.close();
				tieneValoracionesStatement.close();
				return false;
			}
		}
		else
		{
			rs.close();
			tieneValoracionesStatement.close();
			throw new SQLException ("Error en un count en tieneValoraciones");
		}
	}

	/**
	 * Implementación del método que busca la fecha y hora de la
	 * primera valoración para una cuenta en una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#obtenerFechaYHoraPrimeraValoracion (Connection , int ) throws SQLException
	 */
	public static String[] obtenerFechaYHoraPrimeraValoracion (Connection con, int idCuenta) throws SQLException
	{
		String respuestaArreglo[];
		String obtenerFechaYHoraPrimeraValoracionStr="SELECT to_char(fecha_valoracion, 'YYYY-MM-DD') as fecha_valoracion, hora_valoracion from valoraciones where numero_solicitud=(SELECT min(sol.numero_solicitud) from solicitudes sol, valoraciones val where sol.numero_solicitud=val.numero_solicitud and sol.cuenta=?) AND cuidado_especial = '"+ConstantesBD.acronimoNo+"'";
		PreparedStatementDecorator obtenerFechaYHoraPrimeraValoracionStatement= new PreparedStatementDecorator(con.prepareStatement(obtenerFechaYHoraPrimeraValoracionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		obtenerFechaYHoraPrimeraValoracionStatement.setInt(1, idCuenta);
		ResultSetDecorator rs=new ResultSetDecorator(obtenerFechaYHoraPrimeraValoracionStatement.executeQuery());

		if (rs.next())
		{
			respuestaArreglo=new String[2];
			respuestaArreglo[0]=rs.getString("fecha_valoracion");
			respuestaArreglo[1]=rs.getString("hora_valoracion");
			return respuestaArreglo;
		}

		return null;
	}

	/**
	 * Implementación del método que encuentra el número de solicitud
	 * primera valoración para una cuenta en una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#obtenerNumeroSolicitudPrimeraValoracion (Connection , int ) throws SQLException
	 */
	public static int obtenerNumeroSolicitudPrimeraValoracion (Connection con, int idCuenta) throws SQLException
	{
		String obtenerFechaYHoraPrimeraValoracionStr="SELECT numero_solicitud as numeroSolicitud " +
			"from solicitudes " +
			"where cuenta = ? and " +
			"tipo in ("+ConstantesBD.codigoTipoSolicitudInicialUrgencias+","+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+") order by numero_solicitud";
		PreparedStatementDecorator obtenerFechaYHoraPrimeraValoracionStatement= new PreparedStatementDecorator(con.prepareStatement(obtenerFechaYHoraPrimeraValoracionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		try {

			obtenerFechaYHoraPrimeraValoracionStatement.setInt(1, idCuenta);
			ResultSetDecorator rs=new ResultSetDecorator(obtenerFechaYHoraPrimeraValoracionStatement.executeQuery());

			if (rs.next())
			{
				return rs.getInt("numeroSolicitud");
			}
			else
			{
				return -1;
			}

		} 
		catch (Exception e) 
		{
			logger.error("Eror intentando obtenerNumeroSolicitudPrimeraValoracion "+ e.toString());
			return -1;
		}
	}


	/**
	 * Retorna si existe una solicitud de valoración de hospitalización o
	 * urgencias para la cuenta dada
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeSolicitudValoracionCuenta(Connection , int , int ) throws SQLException
	 */
	public static HashMap<String,Object> existeSolicitudValoracionCuenta(Connection con, int idCuenta)
	{
		Logger logger = Logger.getLogger(SqlBaseUtilidadValidacionDao.class);
		String consulta 	=	"SELECT numero_solicitud AS codigo_axioma_solicitud, "
									+	"estado_historia_clinica AS estado_historia_clinica, "
									+ "centro_costo_solicitado AS centro_costo_solicitado "
									+	"FROM solicitudes s "
									+ "WHERE s.cuenta = "+idCuenta+" "
									+ "AND "
									+	"s.tipo IN ("+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+","+ConstantesBD.codigoTipoSolicitudInicialUrgencias+") ";

		try
		{
			PreparedStatementDecorator consultaSttmnt =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator resultado = new ResultSetDecorator(consultaSttmnt.executeQuery());
			
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(resultado), false, true);
			resultado.close();
			return mapaRetorno;
		}
		catch(SQLException se)
		{
			logger.warn("No se pudo hacer la consulta por el siguiente problema "+se.getMessage());
			HashMap<String, Object> resultados = new HashMap<String, Object>();
			resultados.put("codigoAxiomaSolicitud",ConstantesBD.codigoNuncaValido);
			resultados.put("estadoHistoriaClinica",ConstantesBD.codigoNuncaValido);
			resultados.put("centroCostoSolicitado",ConstantesBD.codigoNuncaValido);
			resultados.put("numRegistros","0");
			return resultados;
		}
	}

	/**
	 * Método que implementa la recuperación del centro
	 * de costo tratante siempre y cuando no sea de urgencias
	 * en una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#getCodigoCentroCostoTratante_noUrgencias (Connection , int ) throws SQLException
	 */
	public static int getCodigoCentroCostoTratante_noUrgencias (Connection con, int idCuenta, int institucion)
	{
		logger.info("\n ENTRE A  getCodigoCentroCostoTratante_noUrgencias ");
		try {
			if(esCuentaUrgencias(con, idCuenta)||esCuentaConsultaExterna(con, idCuenta)||esCuentaAmbulatorios(con, idCuenta))
			{
				return Integer.parseInt(getAreaPaciente(con, idCuenta).split(ConstantesBD.separadorSplit)[0]);
			}
		
		
			String getCodigoCentroCostoTratanteStr="SELECT " +
					"tc.centro_costo as codigoCentroCosto " +
					"from tratantes_cuenta tc " +
					"where  ( (tc.fecha_inicio < CURRENT_DATE) " +
					"or (tc.fecha_inicio = CURRENT_DATE " +
					"and to_timestamp('tc.hora_inicio','hh:mi') <= to_timestamp(to_char("+ValoresPorDefecto.getSentenciaHoraActualBD()+"||'','HH24:MI'),'hh:mi'))  ) " +
					"and ( (tc.fecha_fin>CURRENT_DATE or tc.fecha_fin IS NULL) " +
					"or (tc.fecha_fin = CURRENT_DATE and (to_timestamp('tc.hora_fin','hh:mi')>= to_timestamp(to_char("+ValoresPorDefecto.getSentenciaHoraActualBD()+"||'','HH24:MI'),'hh:mi') " +
					"or tc.hora_fin IS NULL)  ) ) and tc.cuenta=?";
			
			logger.info("\n la consulta es --> "+getCodigoCentroCostoTratanteStr +" la cuenta -->"+idCuenta);
			PreparedStatementDecorator getCodigoCentroCostoTratanteStatement= new PreparedStatementDecorator(con.prepareStatement(getCodigoCentroCostoTratanteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			getCodigoCentroCostoTratanteStatement.setInt(1, idCuenta);
	
			ResultSetDecorator rs=new ResultSetDecorator(getCodigoCentroCostoTratanteStatement.executeQuery());
	
			if (rs.next())
			{
				return rs.getInt("codigoCentroCosto");
			}
			else
			{
				return -1024;
			}
		
		} catch (SQLException e) 
		{
			logger.info("\n proble en  getCodigoCentroCostoTratante_noUrgencias "+e);
			
			return -1024;
		}
	}
	
	/**
	 * Método que verifica si un medico es tratante:
	 * Se consulta si el area de la cuenta del paciente hace 
	 * parte de alguno de los centros de costos del usuario del médico
	 * @param con
	 * @param login
	 * @param idCuenta
	 * @return
	 */
	public static boolean esMedicoTratante(Connection con,String login,int idCuenta)
	{
		try
		{
			String consulta = "SELECT "+
				"count(1) AS cuenta "+ 
				"FROM centros_costo_usuario ccu "+ 
				"INNER JOIN cuentas c ON(c.area=ccu.centro_costo) "+ 
				"WHERE ccu.usuario = ? AND c.id = ?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,login);
			pst.setInt(2,idCuenta);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					return true;
				else
					return false;
			else
				return false;
		}
		catch(SQLException e)
		{
			logger.error("Error en esMedicoTratante de SqlBaseUtilidadValidacionDao: "+e);
			return false;
		}
	}
	
	/**
	 * Método que busca el código del centro de costo 
	 * para una solicitud determinada 
	 * @param con
	 * @param numeroSolicitud (índice solicitides),
	 * @return cód
	 * @throws SQLException
	 */
	public static int getCodigoCentroCostoSolicitanteXNumeroSolicitud (Connection con, int numeroSolicitud) throws SQLException
	{
	    String getCodigoCentroCostoXNumeroSolicitudStr="SELECT centro_costo_solicitante AS codigoCentroCostoSolicitante FROM solicitudes WHERE numero_solicitud=?";
	    PreparedStatementDecorator getCodigoCentroCostoXNumeroSolicitudStatement= new PreparedStatementDecorator(con.prepareStatement(getCodigoCentroCostoXNumeroSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    getCodigoCentroCostoXNumeroSolicitudStatement.setInt(1,numeroSolicitud);
	    
	    ResultSetDecorator rs=new ResultSetDecorator( getCodigoCentroCostoXNumeroSolicitudStatement.executeQuery());
	    
	    if(rs.next())
	    {
	        return rs.getInt("codigoCentroCostoSolicitante");
	    }
	    else
	    {
	        return -1024;
	    }
	}

	/**
	 * Método que implementa la revisión de evoluciones suficientes
	 * con orden de salida para dar orden de salida en una BD
	 * Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existenEvolucionesSuficientesOrdenSalida (Connection , int , int , int , int , int ) throws SQLException
	 */
	public static RespuestaValidacion existenEvolucionesSuficientesOrdenSalida (Connection con, int idCuenta, int codigoCentroCostoTratante, int diaEvolucion, int mesEvolucion, int anioEvolucion, boolean esUrgencias, int institucion) throws SQLException
	{
		
		logger.info("1.1");
		PreparedStatementDecorator buscarCCTratanteCuentaStr= new PreparedStatementDecorator(con.prepareStatement("SELECT area AS centro_costo FROM cuentas WHERE id=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		buscarCCTratanteCuentaStr.setInt(1, idCuenta);
		ResultSetDecorator resultado=new ResultSetDecorator(buscarCCTratanteCuentaStr.executeQuery());
		if(resultado.next())
		{
			codigoCentroCostoTratante=resultado.getInt("centro_costo");
		}
		logger.info("1.2");
		String fechaValoracion="";
		String arregloFechaValoracion[];
		String buscarFechaValoracionStr;
		
		buscarFechaValoracionStr="SELECT  to_char(fecha_valoracion, 'YYYY-MM-DD') from solicitudes sol, valoraciones val where sol.numero_solicitud=val.numero_solicitud and sol.cuenta=? and (sol.tipo="+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+" or sol.tipo="+ConstantesBD.codigoTipoSolicitudInicialUrgencias+") order by fecha_valoracion asc";
		

		//Este número me dice cuantas evoluciones existen
		//en esta cuenta
		int numeroEvoluciones=0;

		PreparedStatementDecorator buscarFechaValoracionStatement= new PreparedStatementDecorator(con.prepareStatement(buscarFechaValoracionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		buscarFechaValoracionStatement.setInt(1,idCuenta);

		ResultSetDecorator rs=new ResultSetDecorator(buscarFechaValoracionStatement.executeQuery());

		logger.info("1.3");
		if (rs.next())
		{
			fechaValoracion=rs.getString(1);
		}
		else
		{
			throw new SQLException ("No existe ninguna Valoracion para la cuenta espeficada. Metodo existenEvolucionesSuficientesOrdenSalida");
		}
		rs.close();

		//Una vez se tiene la fecha de valoración, hay que contar todas las evoluciones que
		//existen para esta cuenta, del centro de costo tratante agrupadas por fecha

		String buscarNumeroEvolucionesTratanteStr="select count(1) as numResultados from ( SELECT count(1) as numResultados from evoluciones evol, solicitudes sol where sol.numero_solicitud=evol.valoracion and sol.cuenta=? group by evol.fecha_evolucion )  tablaTemporal";

		PreparedStatementDecorator buscarNumeroEvolucionesTratanteStatement= new PreparedStatementDecorator(con.prepareStatement(buscarNumeroEvolucionesTratanteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		buscarNumeroEvolucionesTratanteStatement.setInt(1, idCuenta);

		rs=new ResultSetDecorator(buscarNumeroEvolucionesTratanteStatement.executeQuery());

		logger.info("1.4");
		if (rs.next())
		{
			numeroEvoluciones=rs.getInt("numResultados");
		}
		else
		{
			//Como se está agrupando, para que no aparezcan repetidos, puede que no de ningún resultado
			//caso en que un count normal nos daría 0
			numeroEvoluciones=0;
		}
		rs.close();

		//Ahora revisamos que ese número sea exacmente la diferencia
		//entre la fecha actual y la fecha de valoración (El dia actual no
		//se cuenta pues la evolucion que se esta llenando cuenta como
		//tal)

		arregloFechaValoracion=fechaValoracion.split("-", 3);

		//Si el número de días es menor o igual que el número de evoluciones
		//lo dejamos salir sin problema
		int numDiasEntreFechas=UtilidadFecha.numeroDiasEntreFechas(diaEvolucion, mesEvolucion, anioEvolucion, Integer.parseInt(arregloFechaValoracion[2]), Integer.parseInt(arregloFechaValoracion[1]), Integer.parseInt(arregloFechaValoracion[0]));
		logger.info("1.5");
		if (numDiasEntreFechas<numeroEvoluciones)
		{
			return new RespuestaValidacion("Hay tantas valoraciones como dias de permanencia. Se puede dar orden de salida", true);
		}
		else if (numDiasEntreFechas==0)
		{
			return new RespuestaValidacion("No hay  que validar todavia porque solo tenemos un dia (el de hoy)", true);
		}
		else if(existeEvolucionHoy(con, idCuenta)&&numDiasEntreFechas==numeroEvoluciones)
		{
			//Se puede presentar el caso que hayan tantas dias como evoluciones
			//pero como no se cuenta la del dia de hoy, si esta existe, falta la de un día
			logger.info("1.6");
			return new RespuestaValidacion(fechaValoracion, false);
		}
		else
		{
			logger.info("1.7");
			return new RespuestaValidacion(fechaValoracion, false);
		}
	}

	/**
	 * Implementación del método que revisa si hay una evolución
	 * del día de hoy en una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeEvolucionHoy (Connection , int , int ) throws SQLException
	 */
	public static boolean existeEvolucionHoy (Connection con, int idCuenta) throws SQLException
	{
		String existeEvolucionHoyStr="SELECT count(1) as numResultados from evoluciones evol, solicitudes sol where sol.cuenta=? and evol.fecha_evolucion=CURRENT_DATE and sol.numero_solicitud=evol.valoracion ";

		PreparedStatementDecorator existeEvolucionHoyStatement= new PreparedStatementDecorator(con.prepareStatement(existeEvolucionHoyStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		existeEvolucionHoyStatement.setInt(1, idCuenta);

		ResultSetDecorator rs=new ResultSetDecorator(existeEvolucionHoyStatement.executeQuery());
		if (rs.next())
		{
			if (rs.getInt("numResultados")<1)
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
			rs.close();
			throw new SQLException ("Error en un count en existeEvolucionHoy");
		}
	}

	/**
	 * Implementación del método que revisa el tipo de evolucion
	 * en una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#revisarTipoEvolucion (Connection , int ) throws SQLException
	 */
	public static RespuestaValidacion revisarTipoEvolucion (Connection con, int numeroEvolucion) throws SQLException
	{
		RespuestaValidacion resp=new RespuestaValidacion ("El número de solicitud para esta evolución no existe en el sistema. Por favor utilice los menúes", false);

		String existeEvolucionStr="SELECT tipo_evolucion as codigoTipoEvolucion from evoluciones where codigo=?";

		PreparedStatementDecorator existeEvolucionStatement= new PreparedStatementDecorator(con.prepareStatement(existeEvolucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		existeEvolucionStatement.setInt(1, numeroEvolucion);

		ResultSetDecorator rs=new ResultSetDecorator(existeEvolucionStatement.executeQuery());

		int codigoTipoEvolucion=0;

		if (rs.next())
		{
			codigoTipoEvolucion=rs.getInt("codigoTipoEvolucion");
			if (codigoTipoEvolucion==0)
			{
				//Es evolucion General
				return new RespuestaValidacion("General", true);
			}
			else if (codigoTipoEvolucion==1)
			{
				//Es evolucion Urgencias
				return new RespuestaValidacion("Urgencias", true);
			}
			else if (codigoTipoEvolucion==2)
			{
				//Es evolucion Hospitalización
				return new RespuestaValidacion("Hospitalizado", true);
			}
			else
			{
				//Si no sale ninguno de estos es porque alguien genero una
				//evolucion mal formada (-1). En este caso vamos a dar un
				//mensaje claro al usuario
				return new RespuestaValidacion("Evolución mal formada. Comuniquese con su administrador de red (posible hacker)", false);
			}
		}
		else
		{
			//Retornamos el valor inicial de resp, que indica al usuario que no
			//existe la evolucion especificada
			return resp;
		}
	}

	/**
	 * Implementación del método que revisa la cuenta a la
	 * que pertenece una evolución en una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeCuentaParaEvolucion (Connection , int ) throws SQLException
	 */
	public static RespuestaValidacion existeCuentaParaEvolucion (Connection con, int codigoEvolucion) throws SQLException
	{

		String existeCuentaParaEvolucionStr="SELECT sol.cuenta as codigoCuenta from evoluciones evol, solicitudes sol where evol.codigo=? and evol.valoracion=sol.numero_solicitud" ;
		PreparedStatementDecorator existeCuentaParaEvolucionStatement= new PreparedStatementDecorator(con.prepareStatement(existeCuentaParaEvolucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		existeCuentaParaEvolucionStatement.setInt(1, codigoEvolucion);

		ResultSetDecorator rs=new ResultSetDecorator(existeCuentaParaEvolucionStatement.executeQuery());

		if (rs.next())
		{
			return new RespuestaValidacion (rs.getString("codigoCuenta"), true);
		}
		else
		{
			rs.close();
			return new RespuestaValidacion ("No se encontró cuenta para la evolución especificada", false);
		}
	}


	/**
	 * Implementación del método que revisa si un centro de costo
	 * particular puede modificar una evolución y en que medida en
	 * una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionModificarEvolucion (Connection con, int , int , int ) throws SQLException
	 */
	public static RespuestaValidacionTratante validacionModificarEvolucion (Connection con, int codigoEvolucion, int idCuenta, int codigoCentroCosto, int institucion) throws SQLException
	{
		//Si es un usuario únicamente puede añadir cosas SI la cuenta
		//está cerrada

		ResultSetDecorator rs;

		 //Antes que nada vamos a revisar si existe efectivamente
		 //la evolución
		String existeEvolucionStr = "SELECT count(1) as numResultados from evoluciones where codigo=?";

		PreparedStatementDecorator existeEvolucionStatement= new PreparedStatementDecorator(con.prepareStatement(existeEvolucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		existeEvolucionStatement.setInt(1, codigoEvolucion);

		rs=new ResultSetDecorator(existeEvolucionStatement.executeQuery());
		if (rs.next())
		{
			if (rs.getInt("numResultados")<1)
			{
				rs.close();
				return new RespuestaValidacionTratante ("No se puede cambiar la evolucion ya que esta no existe", false, false);
			}
			else
			{
				//Continua la validacion, solo cerramos el resultSet
				rs.close();
			}
		}
		else
		{
			rs.close();
			throw new SQLException ("Error en un count en validacionModificarEvolucion");
		}

		//Vamos a revisar si la evolución pertenece a la cuenta o no
		if (!evolucionPerteneceACuenta(con, codigoEvolucion, idCuenta))
		{
			return new RespuestaValidacionTratante ("No se puede cambiar la evolucion ya que el código de la evolución no pertenece a la cuenta especificada. Por favor utilize los menus.", false, false);
		}

		//Si la cuenta esta cerrada cualquier usuario (incluido médico) puede agregar cosas
		if (estaCuentaCerrada(con, idCuenta))
		{
			return new RespuestaValidacionTratante ("Puede ingresar nuevos datos en la evolucion ya que la cuenta está cerrada", true, false);
		}


		if (codigoCentroCosto==0)
		{
			return new RespuestaValidacionTratante ("No se puede cambiar la evolucion ya que el usuario no es médico y como tal solo puede modificar si la cuenta está facturada", false, false);
		}
		else
		{

			boolean esTratante=false;

			if (getCodigoCentroCostoTratante_noUrgencias(con, idCuenta, institucion)==codigoCentroCosto)
			{
				esTratante=true;
			}

			//Si entramos a este else es porque es un médico
			//Si no hay egreso para esta cuenta, cualquier médico lo puede cambiar


			if (esTratante)
			{
				return new RespuestaValidacionTratante ("El médico pertenece al grupo tratante, luego lo puede modificar", true, true);
			}

			String existeEgresoStr="SELECT count(1) as numResultados from egresos where cuenta=? and codigo_medico is not null";
			PreparedStatementDecorator existeEgresoStatement= new PreparedStatementDecorator(con.prepareStatement(existeEgresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			existeEgresoStatement.setInt(1, idCuenta);

			rs=new ResultSetDecorator(existeEgresoStatement.executeQuery());

			if (rs.next())
			{

				if (rs.getInt("numResultados")<1)
				{
					rs.close();
					//ESTE CASO ES EL QUE HAY QUE CAMBIAR PORQUE
					//ACA SE REVISA SI ES TRATANTE O SI ES EL QUE LLENO LA EVOLUCION
					//DE RESTO SE RETORNA FALSE

					String esDeCentroCostoCreadorStr="SELECT count(1) as numResultados from evoluciones evol where evol.codigo=? ";
					PreparedStatementDecorator esDeCentroCostoCreadorStatement= new PreparedStatementDecorator(con.prepareStatement(esDeCentroCostoCreadorStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					esDeCentroCostoCreadorStatement.setInt(1, codigoEvolucion);
					ResultSetDecorator rs1=new ResultSetDecorator(esDeCentroCostoCreadorStatement.executeQuery());

					if (rs1.next())
					{
						if (rs1.getInt("numResultados")>0)
						{
							rs1.close();
							//El médico es tratante, retornamos true
							return new RespuestaValidacionTratante ("El médico pertenece al centro de costo del médico que lleno la evolución, luego la puede modificar", true, true);
						}
					}
					else
					{
						rs1.close();
						throw new SQLException ("Error en un count en validacionModificarEvolucion");
					}


					//En este caso hay egreso y el médico no es tratante, retornamos false
					//a menos que la cuenta este cerrada (y no esta cerrada porque fué lo
					//primero que validamos

					return new RespuestaValidacionTratante ("El médico no es del grupo tratante no pertenece al centro de costo del médico que lleno la evolución,  y la cuenta no está cerrada luego NO la puede modificar", false, false);

				}
				else
				{
					//Continuamos la validación
					rs.close();
					//En este caso hay egreso y el médico no es tratante, retornamos false
					//a menos que la cuenta este cerrada (y no está cerrada, porque eso
					//fué validado al principio)
					return new RespuestaValidacionTratante ("No se puede cambiar la evolucion, ya que existe un egreso/ orden de salida, la cuenta aun no esta facturada y el médico no pertenece al grupo tratante", false, false);
				}
			}
			else
			{
				rs.close();
				throw new SQLException ("Error en un count en validacionModificarEvolucion");
			}

		}
	}

	/**
	 * Método que dice si una evolución pertenece a una cuenta dada
	 * en una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#evolucionPerteneceACuenta (Connection , int , int ) throws SQLException
	 */
	public static boolean evolucionPerteneceACuenta (Connection con, int codigoEvolucion, int idCuenta) throws SQLException
	{
		String evolucionPerteneceACuentaStr="SELECT count(1) as numResultados from solicitudes sol, evoluciones evol where evol.valoracion=sol.numero_solicitud and evol.codigo=? and sol.cuenta=?";
		PreparedStatementDecorator evolucionPerteneceACuentaStatement= new PreparedStatementDecorator(con.prepareStatement(evolucionPerteneceACuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		evolucionPerteneceACuentaStatement.setInt(1, codigoEvolucion);
		evolucionPerteneceACuentaStatement.setInt(2, idCuenta);

		ResultSetDecorator rs=new ResultSetDecorator(evolucionPerteneceACuentaStatement.executeQuery());
		if (rs.next())
		{
			if (rs.getInt("numResultados")>0)
			{
				rs.close();
				evolucionPerteneceACuentaStatement.close();
				return true;
			}
			else
			{
				rs.close();
				evolucionPerteneceACuentaStatement.close();
				return false;
			}
		}
		else
		{
			rs.close();
			evolucionPerteneceACuentaStatement.close();
			throw new SQLException ("Error en un count en evolucionPerteneceACuenta");
		}
	}

	/**
	 * Método que revisa si una cuenta esta cerrada en una
	 * BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#estaCuentaCerrada (Connection , int ) throws SQLException
	 */
	public static boolean estaCuentaCerrada (Connection con, int idCuenta) throws SQLException
	{
		String estaCuentaCerradaStr="SELECT estado_cuenta as estadoCuenta from cuentas where id=?";
		PreparedStatementDecorator estaCuentaCerradaStatement= new PreparedStatementDecorator(con.prepareStatement(estaCuentaCerradaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		estaCuentaCerradaStatement.setInt(1, idCuenta);
		ResultSetDecorator rs=new ResultSetDecorator(estaCuentaCerradaStatement.executeQuery());
		if (rs.next())
		{
			if (rs.getInt("estadoCuenta")==ConstantesBD.codigoEstadoCuentaCerrada)
			{
				rs.close();
				estaCuentaCerradaStatement.close();
				return true;
			}
			else
			{
				rs.close();
				estaCuentaCerradaStatement.close();
				return false;
			}
		}
		else
		{
			rs.close();
			estaCuentaCerradaStatement.close();
			throw new SQLException ("La cuenta especificada en estaCuentaCerrada NO existe!");
		}
	}

	/**
	 * Método que revisa si una cuenta es de urgencias en una
	 * BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#esCuentaUrgencias (Connection , int )  throws SQLException
	 */
	public static boolean esCuentaUrgencias (Connection con, int idCuenta) throws SQLException
	{
		String esCuentaUrgenciasStr="SELECT count(1) as numResultados from admisiones_urgencias where cuenta=?";
		PreparedStatementDecorator esCuentaUrgenciasStatement= new PreparedStatementDecorator(con.prepareStatement(esCuentaUrgenciasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		esCuentaUrgenciasStatement.setInt(1, idCuenta);
		ResultSetDecorator rs=new ResultSetDecorator(esCuentaUrgenciasStatement.executeQuery());
		if (rs.next())
		{
			if (rs.getInt("numResultados")>=1)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			throw new SQLException ("Error en un count en esCuentaUrgencias");
		}
	}
	
	

	/**
	 * Método que revisa si una cuenta es de consulta externa en una
	 * BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#esCuentaConsultaExterna (Connection , int )  throws SQLException
	 */
	public static boolean esCuentaConsultaExterna (Connection con, int idCuenta) throws SQLException
	{
		String esCuentaConsultaExternaStr="SELECT count(1) as numResultados from cuentas c where c.id=? AND c.via_ingreso="+ConstantesBD.codigoViaIngresoConsultaExterna;
		PreparedStatementDecorator esCuentaConsultaExternaStatement= new PreparedStatementDecorator(con.prepareStatement(esCuentaConsultaExternaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		esCuentaConsultaExternaStatement.setInt(1, idCuenta);
		ResultSetDecorator rs=new ResultSetDecorator(esCuentaConsultaExternaStatement.executeQuery());
		if (rs.next())
		{
			if (rs.getInt("numResultados")>=1)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			throw new SQLException ("Error en un count en esCuentaConsultaExterna");
		}
	}

	/**
	 * Método que revisa si una cuenta es de ambulatorios en una
	 * BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#esCuentaAmbularios (Connection , int )  throws SQLException
	 */
	public static boolean esCuentaAmbulatorios (Connection con, int idCuenta) throws SQLException
	{
		String esCuentaAmbulatoriosStr="SELECT count(1) as numResultados from cuentas c where c.id=? AND c.via_ingreso="+ConstantesBD.codigoViaIngresoAmbulatorios;
		PreparedStatementDecorator esCuentaAmbulatoriosStatement= new PreparedStatementDecorator(con.prepareStatement(esCuentaAmbulatoriosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		esCuentaAmbulatoriosStatement.setInt(1, idCuenta);
		ResultSetDecorator rs=new ResultSetDecorator(esCuentaAmbulatoriosStatement.executeQuery());
		if (rs.next())
		{
			if (rs.getInt("numResultados")>=1)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			throw new SQLException ("Error en un count en esCuentaAmbulatorios");
		}
	}
	
	/**
	 * Método que revisa si una cuenta es de urgencias en una
	 * BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#esCuentaUrgencias (Connection , int )  throws SQLException
	 */
	public static boolean existeEvolucionDadaFecha (Connection con, int idCuenta,  int dia, int mes, int anio, int idCuentaAsociada) throws SQLException
	{
		//String existeEvolucionDadaFechaStr="SELECT count(1) as numResultados from solicitudes sol, evoluciones evol where sol.numero_solicitud=evol.valoracion and sol.cuenta=? and evol.fecha_evolucion=?";
		 String existeEvolucionDadaFechaStr =  "	SELECT count(1) as numResultados 													" +
											   "		   FROM solicitudes sol															" +
											   "				INNER JOIN evoluciones evol ON (sol.numero_solicitud=evol.valoracion)	" +
											   "					 WHERE sol.cuenta=? 												" +
											   "					   AND evol.fecha_evolucion=?										";		
		
		if(idCuentaAsociada>0)
			//existeEvolucionDadaFechaStr="SELECT count(1) as numResultados from solicitudes sol, evoluciones evol where sol.numero_solicitud=evol.valoracion and (sol.cuenta=? or sol.cuenta=?) and evol.fecha_evolucion=?";
			existeEvolucionDadaFechaStr="SELECT count(1) as numResultados	 												" +
										"		FROM solicitudes sol														" +
										"			 INNER JOIN evoluciones evol ON (sol.numero_solicitud=evol.valoracion)	" +
										"				  WHERE (sol.cuenta=? or sol.cuenta=?) 								" +
										"					AND evol.fecha_evolucion=?										";
		
		PreparedStatementDecorator existeEvolucionDadaFechaStatement= new PreparedStatementDecorator(con.prepareStatement(existeEvolucionDadaFechaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		logger.info("existeEvolucionDadaFechaStatement-->"+existeEvolucionDadaFechaStr+" idCuenta->"+idCuenta+" fecha->"+anio+"-"+mes+"-"+dia+" asociada->"+idCuentaAsociada );
		
		if(idCuentaAsociada<=0)
		{	
			existeEvolucionDadaFechaStatement.setInt(1, idCuenta);
			existeEvolucionDadaFechaStatement.setString(2,  anio + "-" + mes + "-" + dia);
		}
		else
		{
			existeEvolucionDadaFechaStatement.setInt(1, idCuenta);
			existeEvolucionDadaFechaStatement.setInt(2, idCuentaAsociada);
			existeEvolucionDadaFechaStatement.setString(3,  anio + "-" + mes + "-" + dia);
		}
		

		
		ResultSetDecorator rs=new ResultSetDecorator(existeEvolucionDadaFechaStatement.executeQuery());

		if (rs.next())
		{
			if (rs.getInt("numResultados")<1)
			{
				rs.close();
				return false;
			}
			else
			{
				rs.close();
				return true;
			}
		}
		else
		{
			rs.close();
			throw new SQLException ("Error en un count en existeEvolucionDadaFecha");
		}
	}

	/**
	 * Indica si existen evoluciones en un rango de fechas, devolviendo un arrayList con las fechas que tienen evol
	 * @param con
	 * @param idCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param idCuentaAsociada
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList existeEvolucionesRangoFechas(Connection con, int idCuenta,  String fechaInicial, String fechaFinal, int idCuentaAsociada) 
	{
		ArrayList fechas= new ArrayList();
		String inStr=idCuenta+"";
		if(idCuentaAsociada>0)
			inStr+=", "+idCuentaAsociada;
		String existeEvolucionDadaFechaStr =  	"SELECT " +
		 											"fecha_evolucion as fecha1 " +
		 										"FROM " +
		 											"solicitudes sol " +
		 										"INNER JOIN " +
		 											"evoluciones evol ON (sol.numero_solicitud=evol.valoracion)	" +
											   "WHERE " +
											   		"sol.cuenta in("+inStr+") " +
											   		"AND evol.fecha_evolucion>=to_date(?, 'YYYY-MM-DD') AND evol.fecha_evolucion<=to_date(?, 'YYYY-MM-DD') ";		
		
		PreparedStatementDecorator existeEvolucionDadaFechaStatement;
		try
		{
			existeEvolucionDadaFechaStatement =  new PreparedStatementDecorator(con.prepareStatement(existeEvolucionDadaFechaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		 
			logger.info("existeEvolucionDadaFechaStatement-->"+existeEvolucionDadaFechaStr+" fechaInicial-->"+fechaInicial+" fechaFinal->"+fechaFinal);
			
			existeEvolucionDadaFechaStatement.setString(1, fechaInicial);
			existeEvolucionDadaFechaStatement.setString(2, fechaFinal);
			
			ResultSetDecorator rs=new ResultSetDecorator(existeEvolucionDadaFechaStatement.executeQuery());
	
			while (rs.next())
			{
				fechas.add(rs.getDate("fecha1")+"");
			}
			rs.close();
			existeEvolucionDadaFechaStatement.close();
		}	
		catch (SQLException e)
		{
			logger.error("Error consultando existeEvolucionesRangoFechas "+e);
		}
		return fechas;
	}
	
	/**
	 * Método que implementa la revision de permisos para epicrisis
	 * en su sección de notas y definición para una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#permisosEpicrisisNotaYDefinicion (Connection , int , String , String , int , String , boolean ) throws SQLException
	 */
	public static RespuestaValidacionTratante permisosEpicrisisNotaYDefinicion (Connection con, int idEpicrisis, PersonaBasica paciente, UsuarioBasico usuario, boolean esEpicrisisHospitalizacion, int institucion) throws SQLException
	{
		String numeroIdentificacionPaciente=paciente.getNumeroIdentificacionPersona();
		String codigoTipoIdentificacionPaciente=paciente.getCodigoTipoIdentificacionPersona(); 
		int centroCostoMedico=usuario.getCodigoCentroCosto(); 
		String codigoInstitucion=usuario.getCodigoInstitucion();
		
		boolean encontroFacturada=false;

		String busquedaCuentasStr;
		
		//Aunque a primera vista parezca que no se necesitan los datos de
		//paciente (id) e institución, se manejan por seguridad (Este método
		//es llamado a partir de un dato por parámetro, así que puede venir
		//falseado, de ahí las validaciones extras)
		if (esEpicrisisHospitalizacion)
		{
			//Buscamos las que tienen valoración hospitalaria
			//busquedaCuentasStr="SELECT cue.id, cue.estado_cuenta as estadoCuenta from cuentas cue, solicitudes sol, ingresos ing, val_hospitalizacion valh, personas per where cue.codigo_paciente=per.codigo and cue.id_ingreso=? and per.tipo_identificacion=? and per.numero_identificacion=? and ing.institucion=? and cue.id=sol.cuenta and ing.id=cue.id_ingreso and sol.numero_solicitud=valh.numero_solicitud";
			busquedaCuentasStr="   SELECT cue.id, cue.estado_cuenta as estadoCuenta 											" +
							   "					  FROM cuentas cue	 														" +
							   "						   INNER JOIN solicitudes sol ON ( sol.cuenta=cue.id ) 					" +
							   "						   INNER JOIN ingresos ing ON ( cue.id_ingreso=ing.id )  				" +
							   "						   INNER JOIN val_hospitalizacion valh  ON ( sol.numero_solicitud=valh.numero_solicitud )  " +
							   "						   INNER JOIN personas per ON ( cue.codigo_paciente=per.codigo )  		" +
							   "						        WHERE cue.id_ingreso=? 											" +
							   "							      AND per.tipo_identificacion=? 								" +
							   "								  AND per.numero_identificacion=? 								" +
							   "								  AND ing.institucion=? 										";
		}
		else
		{
			//Buscamos las que tienen valoración de urgencias
			//busquedaCuentasStr="SELECT cue.id, cue.estado_cuenta as estadoCuenta from cuentas cue, solicitudes sol, ingresos ing, valoraciones_urgencias valu, personas per  where cue.codigo_paciente=per.codigo and cue.id_ingreso=? and per.tipo_identificacion=? and per.numero_identificacion=? and ing.institucion=? and cue.id=sol.cuenta and ing.id=cue.id_ingreso and sol.numero_solicitud=valu.numero_solicitud";
			busquedaCuentasStr="  SELECT cue.id, cue.estado_cuenta as estadoCuenta 														" +
							   "		 FROM cuentas cue																				" +
							   "			  INNER JOIN solicitudes sol ON ( sol.cuenta=cue.id ) 										" +
							   "			  INNER JOIN ingresos ing ON ( cue.id_ingreso=ing.id ) 										" +
							   "			  INNER JOIN valoraciones_urgencias valu  ON ( sol.numero_solicitud=valu.numero_solicitud )	" +
							   "			  INNER JOIN personas per ON ( cue.codigo_paciente=per.codigo )  							" +
							   "			  WHERE cue.id_ingreso=? 																	" +
							   "				AND per.tipo_identificacion=? 															" +
							   "				AND per.numero_identificacion=? 														" +
							   "				AND ing.institucion=?  																	";
		}
		PreparedStatementDecorator busquedaCuentasStatement= new PreparedStatementDecorator(con.prepareStatement(busquedaCuentasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		busquedaCuentasStatement.setInt(1, idEpicrisis);
		busquedaCuentasStatement.setString(2, codigoTipoIdentificacionPaciente);
		busquedaCuentasStatement.setString(3, numeroIdentificacionPaciente);
		busquedaCuentasStatement.setString(4, codigoInstitucion);

		ResultSetDecorator rs=new ResultSetDecorator(busquedaCuentasStatement.executeQuery());
		//Solo tomamos en cuenta la prim. cuenta del ingreso (Teoricamente
		//solo debe haber una de urg /hosp por ingreso)
		if (rs.next())
		{
			int codigoCentroCostoTratante=getCodigoCentroCostoTratanteMetodoLento(con, rs.getInt("id"), institucion);
			int estadoCuenta=rs.getInt("estadoCuenta");

			if (estadoCuenta==1)
			{
				encontroFacturada=true;
			}
			if (codigoCentroCostoTratante==centroCostoMedico)
			{
				//Si la cuenta es la cuenta abierta, admisión abierta
				// y el usuario NO es médico  No tiene permisos acceso
				String mensaje = UtilidadValidacion.esMedico(usuario);
				if(paciente.getCodigoCuenta()>0&&rs.getInt("id")==paciente.getCodigoCuenta()&&!mensaje.equals(""))
				{
					//se verifica si no se había definido la ocupación medico especialista y general
					//de parámetros generales
					if(mensaje.equals("errors.noOcupacionMedica"))
						return new RespuestaValidacionTratante ("Falta definir ocupación médico(a) general y/o especialista. Revisar Parámetros Generales", false, false);
					else //if(mensaje.equals("errors.noProfesionalSaludMedico"))
						return new RespuestaValidacionTratante ("No puede llenar la evolución porque el usuario no es médico (a pesar de pertenecer al Centro de Costo tratante)", false, false);
				}
				else
				{
					return new RespuestaValidacionTratante ("Usted es el medico tratante y tiene acceso total", true, true);
				}
			}
			if (encontroFacturada)
			{
				//Si se encontró facturada y antes no nos salimos
				//de la validación es porque el máximo permiso
				//es el de usuario
				return new RespuestaValidacionTratante ("Usted tiene acceso a la epicrisis especificada pero solo como usuario", true, false);
			}

		}
		else
		{
			//No dió ningún resultado, luego no tiene permisos para nada
			return new RespuestaValidacionTratante ("Usted no tiene acceso a la epicrisis especificada", false, false);
		}

		//Si llegamos a este punto es porque no habia
		//ningún registro y si lo había era para el
		//medico tratante y este no lo era
		return new RespuestaValidacionTratante ("Usted no tiene acceso a la epicrisis especificada", false, false);
	}

	/**
	 * Método que implementa la búsqueda lenta del centro de costo
	 * tratante para una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#getCodigoCentroCostoTratanteMetodoLento (Connection , int ) throws SQLException
	 */
	public static int getCodigoCentroCostoTratanteMetodoLento (Connection con, int idCuenta, int institucion) throws SQLException
	{
		return getCodigoCentroCostoTratante_noUrgencias(con, idCuenta, institucion);
	}

	/**
	 * Método que implementa la revisión de número de evoluciones
	 * de acuerdo a criterios de epicrisis para una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#faltanEvolucionesEpicrisis (Connection , int , boolean ) throws SQLException
	 */
	public static RespuestaValidacion faltanEvolucionesEpicrisis (Connection con, int idIngreso, boolean esEpicrisisHospitalizacion, boolean esUrgencias, int institucion) throws SQLException
	{
		int idCuenta=0, codigoCentroCostoTratante=0;

		//Si ya hay egreso esto no se debe validar, ya que la misma evolucion
		//se encarga de esto (Solo podría ocurrir si alguien altera a mano la BD)

		//Esta consulta busca la última cuenta para este ingreso que corresponda a la hospitalizacion
		//y que tenga alguna valoracion asociada
		int resp[]=buscarCuentaAdmisionYCCostoDadoIng(con, idIngreso,esEpicrisisHospitalizacion, institucion);
		idCuenta=resp[0];
		codigoCentroCostoTratante=resp[1];


		//Ahora vamos a ver si tiene una orden de salida valida
		//(El campo de número de identificacion en nulo)
		String buscarOrdenSalidaStr="SELECT count(1) as numResultados from egresos where codigo_medico is not null and cuenta=?";

		PreparedStatementDecorator buscarOrdenSalidaStatement= new PreparedStatementDecorator(con.prepareStatement(buscarOrdenSalidaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		buscarOrdenSalidaStatement.setInt(1, idCuenta);

		ResultSetDecorator rs=new ResultSetDecorator(buscarOrdenSalidaStatement.executeQuery());

		if (rs.next())
		{
			if (rs.getInt("numResultados")<1)
			{
				rs.close();
				//En este caso no se ha dado orden de salida, así que debe revisar
				//hasta el día de hoy

				//Sacamos el día de hoy

				GregorianCalendar calendar = new GregorianCalendar(new SimpleTimeZone(-18000000, "America/Bogota"));

				int anioAct = calendar.get(Calendar.YEAR);
				int mesAct  = calendar.get(Calendar.MONTH)+1;
				int diaAct  = calendar.get(Calendar.DAY_OF_MONTH);
				return existenEvolucionesSuficientesOrdenSalida (con, idCuenta, codigoCentroCostoTratante, diaAct, mesAct, anioAct, esUrgencias, institucion) ;
			}
			else
			{
				//En este caso hay egreso luego no hay que revisar nada
				rs.close();
				return new RespuestaValidacion("El paciente ya tiene orden de salida, por lo que no hay que validar numero de evoluciones", true);
			}
		}
		else
		{
			//Si esta consulta no da un resultado (es un count) es porque algo muy, pero
			//muy malo esta pasando
			throw new SQLException ("Error Desconocido en la búsqueda de evoluciones para epicrisis (faltanEvolucionesEpicrisis)");
		}
	}

	/**
	 * Método que busca la cuenta dado un ingreso
	 * y un tipo de admision. Además da el centro de
	 * costo tratante de la cuenta
	 *
	 * @param con Conexión con la fuente de datos
	 * @param idIngreso Código del ingreso para el
	 * que se va a buscar cuenta
	 * @param esDeHospitalizacion Boolean que
	 * define i se quiere buscar por adm. hosp o urg
	 * @return
	 * @throws SQLException
	 */
	private static int[] buscarCuentaAdmisionYCCostoDadoIng (Connection con, int idIngreso, boolean esDeHospitalizacion, int institucion) throws SQLException
	{
		int respuesta[]=new int[2];

		String buscarCuentaAdmisionStr="";
		if (esDeHospitalizacion)
		{
			buscarCuentaAdmisionStr="SELECT "+ 
				"cue.id "+ 
				"from cuentas cue "+ 
				"INNER JOIN ingresos ing ON(ing.id=cue.id_ingreso) "+ 
				"INNER JOIN admisiones_hospi adh ON(adh.cuenta=cue.id) "+ 
				"where ing.id=?" ;
		}
		else
		{
			buscarCuentaAdmisionStr="SELECT " +
				"cue.id " +
				"from cuentas cue "+ 
				"INNER JOIN ingresos ing ON(ing.id=cue.id_ingreso) "+ 
				"INNER JOIN admisiones_urgencias adu ON(adu.cuenta=cue.id) "+ 
				"where ing.id=?" ;
		}

		PreparedStatementDecorator buscarCuentaAdmisionStatement= new PreparedStatementDecorator(con.prepareStatement(buscarCuentaAdmisionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		buscarCuentaAdmisionStatement.setInt(1, idIngreso);

		ResultSetDecorator rs=new ResultSetDecorator(buscarCuentaAdmisionStatement.executeQuery());

		if (rs.next())
		{
			respuesta[0]=rs.getInt("id");
			respuesta[1]=getCodigoCentroCostoTratanteMetodoLento(con, respuesta[0], institucion);
			rs.close();
			return respuesta;
		}
		else
		{
			rs.close();
			throw new SQLException ("La cuenta especificada para buscarCuentaAdmisionYCCostoDadoIng  no existe o no tiene la admisión especificada");
		}
	}

	/**
	 * Método que revisa si  una cuenta está en cama de observación
	 * para una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#estaEnCamaObservacion(Connection , int ) throws SQLException
	 */
	public static boolean estaEnCamaObservacion(Connection con, int idCuenta) throws SQLException
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try{
			String noEstaEnCamaObservacionStr="select vu.codigo_conducta_valoracion from solicitudes s INNER JOIN valoraciones_urgencias vu on(vu.numero_solicitud=s.numero_solicitud) where s.cuenta=?";
			pst= con.prepareStatement(noEstaEnCamaObservacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, idCuenta);
			rs=pst.executeQuery();
	
			if (rs.next()){
				if (rs.getInt("codigo_conducta_valoracion")==3){
					return true;
				}
				else{
					return false;
				}
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR estaEnCamaObservacion",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR estaEnCamaObservacion", e);
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
		return false;
	}

	/**
	 * Método que revisa si  una cuenta tiene una valoración con conducta
	 * a seguir cama de observación para una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#tieneValoracionConConductaCamaObservacion (Connection , int ) throws SQLException
	 */
	public static boolean tieneValoracionConConductaCamaObservacion (Connection con, int idCuenta) throws SQLException
	{
		String tieneValoracionConConductaCamaObservacionStr="SELECT count(1) as numResultados from solicitudes sol, valoraciones_urgencias valu  where sol.numero_solicitud=valu.numero_solicitud and valu.codigo_conducta_valoracion=3 and sol.cuenta=?";
		PreparedStatementDecorator tieneValoracionConConductaCamaObservacionStatement= new PreparedStatementDecorator(con.prepareStatement(tieneValoracionConConductaCamaObservacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		tieneValoracionConConductaCamaObservacionStatement.setInt(1, idCuenta);

		ResultSetDecorator rs=new ResultSetDecorator(tieneValoracionConConductaCamaObservacionStatement.executeQuery());
		if (rs.next())
		{
			if (rs.getInt("numResultados")<1)
			{
				rs.close();
				return false;
			}
			else
			{
				rs.close();
				return true;
			}
		}
		else
		{
			rs.close();
			throw new SQLException ("Error en un count en tieneValoracionConConductaCamaObservacion ");
		}
	}

	/**
	 * Método que revisa si una cuenta tiene una valoración la
	 * cual tenga una de las conductas a seguir
	 * pasadas por parámetro
	 * @param con Conexión con la BD
	 * @param idCuenta id de la cuenta que se desea validar
	 * @param conductas Array int con los códigos de las conductas a validar
 	 * @return true si hay valoraciones con alguna de las conductas dadas.
 	 */
	public static boolean validacionConductaASeguir (Connection con, int idCuenta, int[] conductas)
	{
		String tieneValoracionConConductaCamaObservacionStr="SELECT count(1) as numResultados FROM solicitudes sol INNER JOIN valoraciones_urgencias valu ON(sol.numero_solicitud=valu.numero_solicitud) WHERE sol.cuenta=? AND (valu.codigo_conducta_valoracion=?";

		if(conductas.length<1)
		{
			logger.error("No se pasó ninguna conducta a seguir para validar");
			return false;
		}
		for(int i=1; i<conductas.length; i++)
		{
			tieneValoracionConConductaCamaObservacionStr+=" OR valu.codigo_conducta_valoracion=?";
		}
		tieneValoracionConConductaCamaObservacionStr+=")";
		PreparedStatementDecorator tieneValoracionConConductaCamaObservacionStatement;
		try
		{
			tieneValoracionConConductaCamaObservacionStatement =  new PreparedStatementDecorator(con.prepareStatement(tieneValoracionConConductaCamaObservacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			tieneValoracionConConductaCamaObservacionStatement.setInt(1, idCuenta);
			for(int i=0; i<conductas.length; i++)
			{
				tieneValoracionConConductaCamaObservacionStatement.setInt(i+2, conductas[i]);
			}
			ResultSetDecorator rs=new ResultSetDecorator(tieneValoracionConConductaCamaObservacionStatement.executeQuery());
			if (rs.next())
			{
				if (rs.getInt("numResultados")<1)
				{
					rs.close();
					return false;
				}
				else
				{
					rs.close();
					return true;
				}
			}
			else
			{
				rs.close();
				throw new SQLException ("Error en un count en validacionConductaASeguir ");
			}
		}
		catch (SQLException e)
		{
			logger.error("Error validando las vías de ingreso de la valoracion de urgencias "+e);
			return false;
		}
	}

	/**
	 * Método que revisa si  se puede reversar un egreso de
	 * hospitalización para una BD Genérica
	 */
	public static RespuestaValidacionTratante puedoReversarEgresoHospitalizacion (Connection con, int idCuenta, int codigoCentroCosto, int institucion) throws SQLException
	{
		logger.info("\n entre a puedoReversarEgresoHospitalizacion ");
		String usuarioResponsable="", numIdMedico="",destinosalida="";
		int centroCostoTratante=0;

		String existeAdmisionHospitalizacionParaCuentaStr="SELECT count(1) as numResultados from admisiones_hospi  where cuenta=?";

		PreparedStatementDecorator existeAdmisionHospitalizacionParaCuentaStatement= new PreparedStatementDecorator(con.prepareStatement(existeAdmisionHospitalizacionParaCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		existeAdmisionHospitalizacionParaCuentaStatement.setInt(1, idCuenta);

		ResultSetDecorator rs=new ResultSetDecorator(existeAdmisionHospitalizacionParaCuentaStatement.executeQuery());
		logger.info("\n ############### 1 ########################");
		if (rs.next())
		{
			if (rs.getInt("numResultados")<1)
			{
				rs.close();
				logger.info("\n ****** 1 **********");
				return new RespuestaValidacionTratante("No se puede hacer reversión de egreso, ya que el egreso fué de urgencias y automático",  false, false);
			}
			//No se hace un else porque en ese caso se debe continuar el flujo de las
			//validaciones
		}
		else
		{
			rs.close();
			throw new SQLException ("Error en un count en puedoReversarEgresoHospitalizacion");
		}

		//A continuación vamos a revisar que no sea un egreso automático

		String esEgresoAutomaticoStr="SELECT es_automatico as esAutomatico from egresos where cuenta=?" ;
		PreparedStatementDecorator esEgresoAutomaticoStatement= new PreparedStatementDecorator(con.prepareStatement(esEgresoAutomaticoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		esEgresoAutomaticoStatement.setInt(1, idCuenta);

		rs=new ResultSetDecorator(esEgresoAutomaticoStatement.executeQuery());
		logger.info("\n ############### 2 ########################");
		if (rs.next())
		{
			if (rs.getBoolean("esAutomatico"))
			{
				rs.close();
				logger.info("\n ****** 2 **********");
				return new RespuestaValidacionTratante("No puede hacer reversión de egreso a un egreso automático", false, false);
			}

			//NO hay else porque se debe continuar la validación
			//El hecho que no hay nada mal hasta ahora no indica
			//que después no pueda haber algo mal
		}
		else
		{
			//Si da ningún resultado es porque no hay egreso para esta cuenta
			rs.close();
			logger.info("\n ****** 3 **********");
			return new RespuestaValidacionTratante("No puede hacer reversión de egreso porque no existe egreso ni orden de salida", false, false);
		}

		String informacionGeneralStr="SELECT codigo_medico as codigoMedico, usuario_responsable  as usuarioResponsable, destino_salida As destinosalida from egresos where cuenta=?" ;
		PreparedStatementDecorator informacionGeneralStatement= new PreparedStatementDecorator(con.prepareStatement(informacionGeneralStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		informacionGeneralStatement.setInt(1, idCuenta);

		rs=new ResultSetDecorator(informacionGeneralStatement.executeQuery());

		boolean terminoEgreso=false;
		logger.info("\n ############### 3 ########################");
		if (rs.next())
		{
			numIdMedico=rs.getString("codigoMedico");
			if (UtilidadTexto.isEmpty(numIdMedico))
			{
				rs.close();
				logger.info("\n ****** 4 **********");
				return new RespuestaValidacionTratante("No puede hacer reversión de egreso porque está en medio de una de estas. Por favor de orden de salida si quiere repetirla", false, false);
			}
			logger.info("\n ############### 7 ########################");
			usuarioResponsable=rs.getString("usuarioResponsable");
			
			destinosalida=rs.getString("destinosalida");
			
			if (UtilidadTexto.isEmpty(usuarioResponsable))
			{
				terminoEgreso=false;
			}
			else
			{
				terminoEgreso=true;
			}

			//Podemos utilizar el métdo noUrgencias, ya que este método (ya sea por su solo nombre) funciona solo para hospitalización
			centroCostoTratante=getCodigoCentroCostoTratante_noUrgencias(con, idCuenta, institucion);
			logger.info("\n ############### 11 ########################");
			if (centroCostoTratante==codigoCentroCosto)
			{
				logger.info("\n ############### 8 ########################");
				//En este caso el médico es tratante y puede hacer la
				//reversión de egreso sin problemas
				rs.close();
				return new RespuestaValidacionTratante("No hay ningún problema para hacer la reversión", true, terminoEgreso);
			}
			else
			{
				logger.info("\n ############### 12 ########################");
				if (UtilidadCadena.noEsVacio(destinosalida))
				{
					logger.info("\n ############### 9 ########################");
					rs.close();
					return new RespuestaValidacionTratante("No hay ningún problema para hacer la reversión", true, terminoEgreso);
				}
				else
				//En este caso es un usuario, solo si el usuarioResponsable
				//NO es null, puede hacer la reversion
					if (!UtilidadTexto.isEmpty(usuarioResponsable))
					{
						logger.info("\n ############### 10 ########################");
						rs.close();
						return new RespuestaValidacionTratante("No hay ningún problema para hacer la reversión", true, terminoEgreso);
					}
					else
					{
						rs.close();
						logger.info("\n ****** 5 **********");
						return new RespuestaValidacionTratante("Una reversión justo después de la orden de salida y antes del egreso por usuario, solo puede realizarla el médico tratante", false, terminoEgreso);
					}
			}

		}
		else
		{
			rs.close();
			logger.info("\n ****** 6 **********");
			return new RespuestaValidacionTratante("No se puede hacer reversión de egreso, pues no existe ningún egreso para esta cuenta", false, terminoEgreso);
		}
	}

	/**
	 * Método que revisa si  para una cuenta dada se debe se
	 * debe mostar el motivo de reversión de egreso para una
	 * BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#deboMostrarMotivoReversionEgreso (Connection , int ) throws SQLException
	 */
	public static RespuestaValidacion deboMostrarMotivoReversionEgreso (Connection con, int idCuenta) throws SQLException
	{
		logger.info("\n\n\n ********************************* deboMostrarMotivoReversionEgreso**************************************");
		ResultSetDecorator rs;

		//Primero vamos a buscar si hay un egreso para esta
		//cuenta y de paso sacamos la fecha del egreso
		String fechaEgreso="", motivoReversion="", horaEgreso="";

		String buscarEgresoYFechaStr="SELECT hora_grabacion as horaEgreso, to_char(fecha_grabacion,'"+ConstantesBD.formatoFechaBD+"') as fechaEgreso, motivo_reversion as motivoReversion from egresos where codigo_medico IS null and cuenta=?";
		PreparedStatementDecorator buscarEgresoYFechaStatement= new PreparedStatementDecorator(con.prepareStatement(buscarEgresoYFechaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		buscarEgresoYFechaStatement.setInt(1, idCuenta);
		
		logger.info("buscarEgresoYFechaStr-->"+buscarEgresoYFechaStr+"-->"+idCuenta);
		rs=new ResultSetDecorator(buscarEgresoYFechaStatement.executeQuery());

		if (rs.next())
		{
			fechaEgreso=rs.getString("fechaEgreso");
			horaEgreso=UtilidadFecha.convertirHoraACincoCaracteres(rs.getString("horaEgreso"));
			motivoReversion=rs.getString("motivoReversion");
		}
		else
		{
			//No se encontró ningún egreso con las condiciones dadas
			rs.close();
			logger.info("No esta en reversion de egreso");
			return new RespuestaValidacion("No esta en reversion de egreso", false);
		}

		rs.close();
		String fechaEvolucion="", horaEvolucion="";

		//En este punto sabemos que el paciente tuvo en algún momento reversion
		//del egreso, ahora debemos verificar si ya se mostro o no
		//(Debe ser estrictamente mayor o si no se saldrá la que dio orden de salida)
		String buscarEvolucionFechaMayorStr="SELECT to_char(evol.fecha_grabacion,'"+ConstantesBD.formatoFechaBD+"') as fechaEvolucion, evol.hora_grabacion as horaEvolucion from solicitudes sol, evoluciones evol where sol.numero_solicitud=evol.valoracion and sol.cuenta=? and to_char(fecha_evolucion,'"+ConstantesBD.formatoFechaBD+"')>=? order by fecha_evolucion desc, evol.hora_evolucion desc";
		PreparedStatementDecorator buscarEvolucionFechaMayorStatement= new PreparedStatementDecorator(con.prepareStatement(buscarEvolucionFechaMayorStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		buscarEvolucionFechaMayorStatement.setInt(1, idCuenta);
		buscarEvolucionFechaMayorStatement.setString(2, fechaEgreso);
		
		logger.info("buscarEvolucionFechaMayorStr-->"+buscarEvolucionFechaMayorStr+" ->idCuenta="+idCuenta+" fechaEg->"+fechaEgreso);
		
		rs=new ResultSetDecorator(buscarEvolucionFechaMayorStatement.executeQuery());
		if (rs.next())
		{
			fechaEvolucion=rs.getString("fechaEvolucion");
			horaEvolucion=UtilidadFecha.convertirHoraACincoCaracteres(rs.getString("horaEvolucion"));

			//Ahora bien, si una de estas fechas de evolucion termina
			//siendo mayor que la de egreso, ya se mostro.

			if (fechaEvolucion.compareTo(fechaEgreso)>0)
			{
				//En este caso claramente hubo una evolución después del egreso
				//Luego NO hay que mostrarla de nuevo
				
				logger.info("Esta no es la primera evolución después de la reversión de egreso, luego no se debe mostrar");
				rs.close();
				return new RespuestaValidacion("Esta no es la primera evolución después de la reversión de egreso, luego no se debe mostrar", false);
			}
			else if (horaEvolucion.compareTo( horaEgreso )>0)
			{

				//En este caso también hubo una evolución después del egreso
				//mismo día pero más tarde (Se deben comparar unicamente los primeros
				//caracteres o se pierde)
				logger.info("Esta no es la primera evolución después de la reversión de egreso, luego no se debe mostrar 1");
				rs.close();
				return new RespuestaValidacion("Esta no es la primera evolución después de la reversión de egreso, luego no se debe mostrar", false);
			}
			else
			{
				logger.info("Se muestra");
				//Si no pasa nada de esto, lo mostramos
				rs.close();
				return new RespuestaValidacion(motivoReversion, true);
			}
		}
		else
		{
			logger.info("Se muestra porque todas las evoluciones eran menores");
			//En este caso todas las evoluciones eran menores,
			//luego hay que mostrar el motivo
			rs.close();
			return new RespuestaValidacion(motivoReversion, true);
		}
	}

	/**
	 * Método implementa la revisión de si existe epicrisis dado su
	 * código para una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeEpicrisis (Connection , int , boolean ) throws SQLException
	 */
	public static RespuestaValidacion existeEpicrisis (Connection con, int idEpicrisis, boolean esEpicrisisHospitalizacion) throws SQLException
	{
		String existeEpicrisisStr="SELECT ingreso from epicrisis where ingreso=?" ;
		PreparedStatementDecorator existeEpicrisisStatement= new PreparedStatementDecorator(con.prepareStatement(existeEpicrisisStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		existeEpicrisisStatement.setInt(1, idEpicrisis);

		ResultSetDecorator rs=new ResultSetDecorator(existeEpicrisisStatement.executeQuery());

		if (rs.next())
		{
			//En el caso que haya alguna epicrisis, el codigo no importa,
			//es mas lo pasamos, lo unico realmente importante es que
			//salio un resultado, luego retornamos true


			if (esEpicrisisHospitalizacion)
			{
				//Si es epicrisis de hospitalización debe tener en ese mismo ingreso
				//al menos una valoración de hospitalización
				rs.close();

				//String epicrisisTieneValoracionHospitalariaStr="SELECT cue.id from cuentas cue, solicitudes sol, val_hospitalizacion valh where cue.id=sol.cuenta and sol.numero_solicitud=valh.numero_solicitud and cue.id_ingreso=?" ;
				String epicrisisTieneValoracionHospitalariaStr="SELECT cue.id from cuentas cue" +
															   "	INNER JOIN solicitudes sol ON (sol.cuenta=cue.id)	" +
															   "	INNER JOIN val_hospitalizacion valh ON (sol.numero_solicitud=valh.numero_solicitud)" +
															   "	 	WHERE cue.id_ingreso=?";
				PreparedStatementDecorator epicrisisTieneValoracionHospitalariaStatement= new PreparedStatementDecorator(con.prepareStatement(epicrisisTieneValoracionHospitalariaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				epicrisisTieneValoracionHospitalariaStatement.setInt(1, idEpicrisis);

				rs=new ResultSetDecorator(epicrisisTieneValoracionHospitalariaStatement.executeQuery());
				if (rs.next())
				{
					rs.close();
					return new RespuestaValidacion("El paciente tiene epicrisis hospitalaria", true);
				}
				else
				{
					rs.close();
					return new RespuestaValidacion ("El paciente tiene epicrisis pero no valoracion hospitalaria, por favor utilize la funcionalidad de epicrisis de urgencias", false);
				}

			}
			else
			{
				rs.close();
				//Si es epicrisis de urgencias debe tener en ese mismo ingreso
				//al menos una valoración de urgencias
				//String epicrisisTieneValoracionUrgenciasStr="SELECT cue.id, valu.codigo_conducta_valoracion as codigoConductaSeguir from cuentas cue, solicitudes sol, valoraciones_urgencias valu where cue.id=sol.cuenta and sol.numero_solicitud=valu.numero_solicitud and cue.id_ingreso=?";
				String epicrisisTieneValoracionUrgenciasStr="SELECT cue.id, valu.codigo_conducta_valoracion as codigoConductaSeguir " +
															"		FROM cuentas cue" +
															"			 INNER JOIN solicitudes sol ON (sol.cuenta=cue.id)" +
															"			 INNER JOIN valoraciones_urgencias valu ON (sol.numero_solicitud=valu.numero_solicitud) " +
															"			      WHERE cue.id_ingreso=?";
				PreparedStatementDecorator epicrisisTieneValoracionUrgenciasStatement= new PreparedStatementDecorator(con.prepareStatement(epicrisisTieneValoracionUrgenciasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				rs=new ResultSetDecorator(epicrisisTieneValoracionUrgenciasStatement.executeQuery());
				if (rs.next())
				{

					//Aunque en este punto ya hay epicrisis de urgencias, solo se puede acceder
					//a ella si la conducta a seguir
					if (rs.getInt("codigoConductaSeguir")!=3)
					{
						rs.close();
						return new RespuestaValidacion("No existe epicrisis de urgencias para este paciente, ya que la conducta a seguir no es cama de observacion", false);
					}
					else
					{
						//Conducta a seguir esperada (cama observacion, luego puede acceder sin problema)
						rs.close();
						return new RespuestaValidacion("El paciente tiene epicrisis de urgencias", true);
					}
				}
				else
				{
					rs.close();
					return new RespuestaValidacion ("El paciente tiene epicrisis pero no valoracion de urgencias, por favor utilize la funcionalidad de epicrisis de hospitalizacion", false);
				}
			}

		}
		else
		{
			rs.close();
			return new RespuestaValidacion ("El paciente no tiene ha creado ninguna valoracion, hasta que no lo haga, no existe epicrisis (ni de urgencias ni de hospitalizacion)", false);
		}
	}

	/**
	 * Método implementa la revisión de si existe un paciente
	 * para una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existePaciente (Connection , String , String ) throws SQLException
	 */
	public static boolean existePaciente (Connection con, int codigoPaciente) throws SQLException
	{
		boolean respuesta=false;

		int numResultados=0;
		ResultSetDecorator rs=null;

		String consultaExistePacienteStr="select count(1) as numResultados from pacientes where codigo_paciente=?";
		PreparedStatementDecorator consultaExistePacienteStatement= new PreparedStatementDecorator(con.prepareStatement(consultaExistePacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaExistePacienteStatement.setInt(1, codigoPaciente);

		rs=new ResultSetDecorator(consultaExistePacienteStatement.executeQuery());

		if (rs.next())
		{
			numResultados+=rs.getInt("numResultados");
		}

		if (numResultados != 0) {
			respuesta=true;
		}

		rs.close();
		return respuesta;
	}

	/**
	 * Método implementa la revisión de si existe un paciente
	 * para una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existePaciente (Connection , String , String ) throws SQLException
	 */
	public static boolean existePaciente (Connection con, String tipoId, String numeroId) throws SQLException
	{
		boolean respuesta=false;

		int numResultados=0;
		ResultSetDecorator rs=null;

		String consultaExistePacienteStr="select count(1) as numResultados from manejopaciente.pacientes pac, administracion.personas per where per.numero_identificacion=? and per.tipo_identificacion=? and pac.codigo_paciente=per.codigo";
		PreparedStatementDecorator consultaExistePacienteStatement= new PreparedStatementDecorator(con.prepareStatement(consultaExistePacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaExistePacienteStatement.setString(1, numeroId);
		consultaExistePacienteStatement.setString(2, tipoId);

		rs=new ResultSetDecorator(consultaExistePacienteStatement.executeQuery());

		if (rs.next())
		{
			numResultados+=rs.getInt("numResultados");
		}

		if (numResultados != 0) {
			respuesta=true;
		}

		rs.close();
		return respuesta;
	}

	/**
	 * Método implementa la revisión del ingreso de cuentas
	 * para una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionIngresarCuenta (Connection ,  String , String , String ) throws SQLException
	 */
	public static RespuestaValidacion validacionIngresarCuenta (Connection con,  String tipoId, String numeroId, String codigoInstitucion) throws SQLException
	{
		int numResultados=0;
		ResultSetDecorator rs=null;

		RespuestaValidacion respuesta= new RespuestaValidacion ("No hay problema para crear esta Cuenta", true);

		String consultaIngresarCuentaStr="SELECT count(1) as numResultados " +
				"FROM cuentas c " +
				"INNER JOIN ingresos ing ON(c.id_ingreso=ing.id) " +
				"INNER JOIN personas per ON(per.codigo=ing.codigo_paciente) WHERE per.numero_identificacion=? and per.tipo_identificacion=? and c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaActiva+"  and ing.institucion=?" ;
		PreparedStatementDecorator consultaIngresarCuentaStatement= new PreparedStatementDecorator(con.prepareStatement(consultaIngresarCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaIngresarCuentaStatement.setString(1, numeroId);
		consultaIngresarCuentaStatement.setString(2, tipoId);
		consultaIngresarCuentaStatement.setString(3, codigoInstitucion);

		rs=new ResultSetDecorator(consultaIngresarCuentaStatement.executeQuery());

		if (rs.next())
		{
			numResultados=rs.getInt("numResultados");
		}
		rs.close();
		//Si ya existe un Cuenta, en el String de la respuesta vamos
		//a dar el codigo del mismo
		if (numResultados>0)
		{
			String consultaCodigoCuentaAnteriorStr="SELECT "+
				"c.id as id, "+
				"getnomcentroatencion(cc.centro_atencion) as centro_atencion "+ 
				"FROM personas per "+ 
				"INNER JOIN ingresos ing ON(ing.codigo_paciente=per.codigo) "+
				"INNER JOIN cuentas c ON(c.id_ingreso=ing.id) "+ 
				"INNER JOIN centros_costo cc ON(cc.codigo=c.area) "+ 
				"WHERE "+ 
				"per.numero_identificacion=? and per.tipo_identificacion=? and ing.institucion=? and estado_cuenta=0" ;
			PreparedStatementDecorator consultaCodigoCuentaAnteriorStatement= new PreparedStatementDecorator(con.prepareStatement(consultaCodigoCuentaAnteriorStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultaCodigoCuentaAnteriorStatement.setString(1, numeroId);
			consultaCodigoCuentaAnteriorStatement.setString(2, tipoId);
			consultaCodigoCuentaAnteriorStatement.setString(3, codigoInstitucion);
			rs=new ResultSetDecorator(consultaCodigoCuentaAnteriorStatement.executeQuery());

			String codigoCuentaAnterior="(Sin Codigo)";
			String centroAtencion = "No definido";
			if (rs.next())
			{
				codigoCuentaAnterior=rs.getString("id");
				centroAtencion = rs.getString("centro_atencion");
			}
			rs.close();
			return new RespuestaValidacion (": Ya existe una cuenta abierta para este paciente, con codigo " + codigoCuentaAnterior+" en el centro de atención "+centroAtencion, false);
		}
		return respuesta;
	}

	/**
	 * Método implementa la revisión de la creación de ingreso
	 * para una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionIngresarIngreso (Connection , String , String , String ) throws SQLException
	 */
	public static RespuestaValidacion validacionIngresarIngreso (Connection con, String tipoId, String numeroId, String codigoInstitucion) throws SQLException
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		PreparedStatement pst1=null;
		ResultSet rs1=null;
		RespuestaValidacion respuesta= new RespuestaValidacion ("No hay problema para crear este ingreso", true);
		try{
		int numResultados=0;
		String codigoIngresoAnterior="", consultaCodigoIngresoAnteriorStr="",centroAtencion = "";
		String consultaIngresarIngresoStr="SELECT count(1) as numResultados FROM " +
			"manejopaciente.ingresos ing " +
			"INNER JOIN administracion.personas per ON(per.codigo=ing.codigo_paciente) " +
			"WHERE " +
			"ing.institucion=? and " +
			"ing.estado = '"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' and " +
			"per.numero_identificacion=? and " +
			"per.tipo_identificacion=? ";
			pst= con.prepareStatement(consultaIngresarIngresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setString(1, codigoInstitucion);
			pst.setString(2, numeroId);
			pst.setString(3, tipoId);
		

			rs=pst.executeQuery();

		if (rs.next())
		{
			numResultados=rs.getInt("numResultados");
		}
		//Si ya existe un ingreso, en el String de la respuesta vamos
		//a dar el codigo del mismo.
		if (numResultados>0)
		{
			consultaCodigoIngresoAnteriorStr="SELECT "+ 
				"ing.consecutivo as id, "+
				"getnomcentroatencion(cc.centro_atencion) as centro_atencion "+ 
				"FROM personas per "+ 
				"INNER JOIN ingresos ing ON(ing.codigo_paciente=per.codigo) "+
				"INNER JOIN cuentas c ON(c.id_ingreso=ing.id) "+ 
				"INNER JOIN centros_costo cc ON(cc.codigo=c.area) "+ 
				"WHERE "+ 
				"per.numero_identificacion=? and per.tipo_identificacion=? and ing.estado = '"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' and ing.institucion=?" ; 
				pst1= con.prepareStatement(consultaCodigoIngresoAnteriorStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst1.setString(1, numeroId);
				pst1.setString(2, tipoId);
				pst1.setString(3, codigoInstitucion);

				rs1=pst1.executeQuery();

				if (rs1.next())
			{
					codigoIngresoAnterior=rs1.getString("id");
					centroAtencion = rs1.getString("centro_atencion");
				}
				respuesta= new RespuestaValidacion ("Ya existe un ingreso abierto para este paciente, con el codigo " + codigoIngresoAnterior + " en el centro de atención "+centroAtencion , false);
			}
		}
		catch(Exception e){
			logger.error("############## ERROR validacionIngresarIngreso", e);
		}
		finally{
			try{
				if(rs1 != null){
					rs1.close();
				}
				if(pst1 != null){
					pst1.close();
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
		return respuesta;
	}

	/**
	 * Método implementa la validación del ingreso de una persona
	 * para una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionIngresarPersona (Connection , String , String , String , String , String , String ) throws SQLException
	 */
	public static RespuestaValidacion validacionIngresarPersona (Connection con, String tipoId, String numeroId, String codBarrio, String rolPersona) throws SQLException
	{
		
		int numResultados;
		ResultSetDecorator rs=null;
		RespuestaValidacion respuesta= new RespuestaValidacion ("No hay problema para ingresar la persona", true);

		//Puede que el usuario no haya seleccionado el barrio

		if (codBarrio!=null && !codBarrio.equals(""))
		{
		
			String consultaBarrioStr="select count(1) as numResultados from administracion.barrios where codigo=?";
			PreparedStatementDecorator consultaBarrioStatement= new PreparedStatementDecorator(con.prepareStatement(consultaBarrioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			consultaBarrioStatement.setInt(1, Integer.parseInt(codBarrio));
	
			rs=new ResultSetDecorator(consultaBarrioStatement.executeQuery());
	
			if (rs.next())
			{
				numResultados=rs.getInt("numResultados");
				if (numResultados==0)
				{
					rs.close();
					return new RespuestaValidacion ("Por favor seleccione la ciudad y el departamento de la persona", false);
				}
			}
	
			rs.close();
		}
		
		String consultaIdentificacionStr;
		if (rolPersona.equals("persona"))
		{
			consultaIdentificacionStr="SELECT count(1) as numResultados from administracion.personas where tipo_identificacion=? and numero_identificacion=?";
		}
		else
		{
			//Completamos la consulta con medico, paciente, usuario +s
			
			String parteQuery = rolPersona.toLowerCase();
			if(rolPersona.toLowerCase().equals("paciente")){
				parteQuery= "manejopaciente.paciente";
			}
			else if(rolPersona.toLowerCase().equals("medico")){
				parteQuery= "administracion.medico";
			}
			
			consultaIdentificacionStr="SELECT count(1) as numResultados from administracion.personas per, " + parteQuery + "s med where per.tipo_identificacion=? and per.numero_identificacion=? and per.codigo=med.codigo_"+ rolPersona.toLowerCase();
			Log4JManager.info(consultaIdentificacionStr);
		}
		PreparedStatementDecorator consultaIdentificacionStatement= new PreparedStatementDecorator(con.prepareStatement(consultaIdentificacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaIdentificacionStatement.setString(1, tipoId);
		consultaIdentificacionStatement.setString(2, numeroId);

		rs=new ResultSetDecorator(consultaIdentificacionStatement.executeQuery());
		if (rs.next())
		{
			numResultados=rs.getInt("numResultados");
			if (numResultados>0)
			{
				if (rolPersona!=null&&rolPersona.equals("Medico"))
				{
					rs.close();
					return new RespuestaValidacion ("Este Profesional de la Salud ya existe en el sistema", false);
				}
				else
				{
					rs.close();
					return new RespuestaValidacion ("Este " + rolPersona  + " ya existe en el sistema", false);
				}
			}
		}
		rs.close();
		return respuesta;
	}

	/**
	 * Método implementa la validación de permisos de acceso
	 * a un paciente en una institución para una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionPermisosInstitucionPaciente (Connection , int , String ) throws SQLException
	 */
	public static RespuestaValidacion validacionPermisosInstitucionPaciente (Connection con, int codigoPaciente, String codigoInstitucion) throws SQLException
	{
		ResultSetDecorator rs=null;
		int numResultados=0;

		//Asumimos que todo salio bien, si alguna cosa sale mal
		//salimos con un return interior
		RespuestaValidacion respuesta= new RespuestaValidacion ("La inserción no tuvo ning&uacute;n problema", true);

		if	(codigoPaciente<1||codigoInstitucion==null ||codigoInstitucion.equals("") )
		{
			return new RespuestaValidacion ("Por favor no trabaje con valores vacios", false);
		}

		//Ahora revisamos que esa identificacion corresponda a un paciente

		String existePacienteStr="SELECT count(1) as numResultados from manejopaciente.pacientes where codigo_paciente=?";
		PreparedStatementDecorator existePacienteStatement= new PreparedStatementDecorator(con.prepareStatement(existePacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		existePacienteStatement.setInt(1, codigoPaciente);

		rs=new ResultSetDecorator(existePacienteStatement.executeQuery());

		if (rs.next())
		{
			numResultados=rs.getInt("numResultados");
		}
		if (numResultados==0)
		{
			rs.close();
			return new RespuestaValidacion ("1-El paciente no existe en el sistema", false);
		}
		rs.close();

		//Ahora revisamos que el instituto también exista
		if (!existeInstitucion(con, codigoInstitucion))
		{
			return new RespuestaValidacion ("El instituto especificado no existe en el sistema", false);
		}

		//Llegado a este punto se que existe tanto paciente como instituto
		//Si ya existe la pareja en la tabla Pacientes_Institutos es porque
		//ya fue insertado previamente, así que le decimos al usuario lo que
		//paso

		String existeParejaStr="SELECT count(1) as numResultados from manejopaciente.pacientes_instituciones where codigo_paciente=? and codigo_institucion=?" ;
		PreparedStatementDecorator existeParejaStatement= new PreparedStatementDecorator(con.prepareStatement(existeParejaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		existeParejaStatement.setInt(1, codigoPaciente);
		existeParejaStatement.setString(2, codigoInstitucion);

		rs=new ResultSetDecorator(existeParejaStatement.executeQuery());

		if (rs.next())
		{
			numResultados=rs.getInt("numResultados");
		}
		if (numResultados>0)
		{
			rs.close();
			return new RespuestaValidacion ("No hubo necesidad de crear el paciente dado que ya existia en esta institucion", true);
		}
		rs.close();

		//Si llegamos a este punto es porque existe tanto paciente como la institución
		//pero no la pareja, luego tenemos que insertar esta en la base de datos

		String insertarParejaStr="insert INTO manejopaciente.pacientes_instituciones (codigo_paciente, codigo_institucion) values (?, ?)";
		PreparedStatementDecorator insertarParejaStatement= new PreparedStatementDecorator(con.prepareStatement(insertarParejaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		insertarParejaStatement.setInt(1, codigoPaciente);
		insertarParejaStatement.setString(2, codigoInstitucion);

		if (insertarParejaStatement.executeUpdate()==0)
		{
			return new RespuestaValidacion ("No se pudo crear el paciente en esta institución", false);
		}
		return respuesta;
	}

	/**
	 * Método implementa la validación de permisos de acceso
	 * a un paciente en una institución a segundo nivel para una
	 * BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionPermisosInstitucionPaciente2 (Connection , int , String , String ) throws SQLException
	 */
	public static RespuestaValidacion validacionPermisosInstitucionPaciente2 (Connection con, int codigoPaciente, String codigoInstitucionBrinda, String codigoInstitucionRecibe) throws SQLException
	{
		ResultSetDecorator rs=null;
		int numResultados=0;

		//Asumimos que todo salio bien, si alguna cosa sale mal
		//salimos con un return interior
		RespuestaValidacion respuesta= new RespuestaValidacion ("La inserción no tuvo ningún problema", true);

		//Como prerequisto, necesitamos que la persona tenga permiso de primer Nivel
		RespuestaValidacion temporal = validacionPermisosInstitucionPaciente (con, codigoPaciente, codigoInstitucionRecibe) ;

		//Si ni siquiera fué posible dar permisos de primer nivel,
		//no se va a poder dar permisos de segundo nivel
		if (!temporal.puedoSeguir)
		{
			return temporal;
		}

		//Como el método de nivel 1 revisa la validez de los datos, basta revisar
		//que el código de la institución que brinda el permiso no sea ni vacío ni
		//nulo

		if	(codigoInstitucionBrinda==null||codigoInstitucionBrinda.equals(""))
		{
			return new RespuestaValidacion ("Por favor no trabaje con valores vacios", false);
		}

		//Ahora revisamos que el instituto que brinda el permiso exista
		if (!existeInstitucion(con, codigoInstitucionBrinda))
		{
			return new RespuestaValidacion ("El instituto especificado no existe en el sistema", false);
		}

		//Llegado a este punto se que existe tanto paciente como instituto
		//Si ya existe la pareja en la tabla Pacientes_Institutos2 es porque
		//ya tiene permisos de segundo nivel, salimos del método pero
		//diciendo que todo salío bien

		String existeParejaStr="SELECT count(1)  as numResultados from manejopaciente.pacientes_instituciones2 where codigo_paciente=? and codigo_institucion_duena=? and codigo_institucion_permitida=?" ;
		PreparedStatementDecorator existeParejaStatement= new PreparedStatementDecorator(con.prepareStatement(existeParejaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		existeParejaStatement.setInt(1, codigoPaciente);
		existeParejaStatement.setString(2, codigoInstitucionBrinda);
		existeParejaStatement.setString(3, codigoInstitucionRecibe);

		rs=new ResultSetDecorator(existeParejaStatement.executeQuery());

		if (rs.next())
		{
			numResultados=rs.getInt("numResultados");
		}
		if (numResultados>0)
		{
			rs.close();
			return new RespuestaValidacion ("No hubo necesidad de dar permisos de nivel 2, porque ya los tenía", true);
		}

		//Si llegamos a este punto es porque
		//1.Existe tanto paciente como la institución
		//2.La Institución que recibe tiene permisos de Nivel 1
		//3.No tiene permisos de nivel 2,
		//asi que es el momento de darlos

		String insertarParejaStr="insert INTO manejopaciente.pacientes_instituciones2 (codigo_paciente, codigo_institucion_duena, codigo_institucion_permitida) values (?, ?, ? )";
		PreparedStatementDecorator insertarParejaStatement= new PreparedStatementDecorator(con.prepareStatement(insertarParejaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		insertarParejaStatement.setInt(1, codigoPaciente);
		insertarParejaStatement.setString(2, codigoInstitucionBrinda);
		insertarParejaStatement.setString(3, codigoInstitucionRecibe);

		if (insertarParejaStatement.executeUpdate()==0)
		{
			return new RespuestaValidacion ("No se pudo crear el paciente en esta institución", false);
		}

		return respuesta;
	}

	/**
	 * Método implementa la revisión de si una institución
	 * existe para una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeInstitucion (Connection , String ) throws SQLException
	 */
	private static boolean existeInstitucion (Connection con, String codigoInstitucion) throws SQLException
	{
		String existeInstitutoStr="SELECT count(1) as numResultados from administracion.instituciones where codigo=?" ;
		PreparedStatementDecorator existeInstitutoStatement= new PreparedStatementDecorator(con.prepareStatement(existeInstitutoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		existeInstitutoStatement.setString(1, codigoInstitucion);

		ResultSetDecorator rs=new ResultSetDecorator(existeInstitutoStatement.executeQuery());
		if (rs.next())
		{
			if (rs.getInt("numResultados")>0)
			{
				rs.close();
				return true;
			}
			else
			{
				rs.close();
				return false;
			}
		}
		else
		{
			rs.close();
			throw  new SQLException ("Error en un count en existeInstitucion");
		}
	}

	/**
	 * Método implementa la búsqueda del código de un
	 * paciente dada su identificación para una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#getCodigoPersona (Connection , String , String ) throws SQLException
	 */
	public static int getCodigoPersona (Connection con, String codigoTipoIdentificacion, String numeroIdentificacion )
	{
		try
		{
			
			
			PreparedStatementDecorator buscarCodigoPacienteStatement= new PreparedStatementDecorator(con.prepareStatement("SELECT codigo from personas where tipo_identificacion=? and numero_identificacion =?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			buscarCodigoPacienteStatement.setString(1, codigoTipoIdentificacion);
			buscarCodigoPacienteStatement.setString(2, numeroIdentificacion);
			ResultSetDecorator rs=new ResultSetDecorator(buscarCodigoPacienteStatement.executeQuery());
			if (rs.next())
			{
				int codigo=rs.getInt("codigo");
				rs.close();
				return codigo;
			}
			else
			{
				rs.close();
				return -1;
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en getCodigoPersona: "+e);
			return -1;
		}
	}
	
	/**
	 * Método implementa la búsqueda del código de un
	 * paciente dada su identificación para una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#getCodigoPersona (Connection , String , String ) throws SQLException
	 */
	public static int buscarCodigoPaciente (Connection con, String codigoTipoIdentificacion, String numeroIdentificacion ) 
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		int codigo=ConstantesBD.codigoNuncaValido;
		try
		{
			pst= con.prepareStatement("SELECT p.codigo from administracion.personas p INNER JOIN manejopaciente.pacientes pac ON(pac.codigo_paciente=p.codigo) where p.tipo_identificacion=? and p.numero_identificacion =?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setString(1, codigoTipoIdentificacion);
			pst.setString(2, numeroIdentificacion);
			rs=pst.executeQuery();
			if (rs.next()){
				codigo=rs.getInt("codigo");
			}
		}
		catch (Exception e) {
			Log4JManager.error("ERROR buscarCodigoPaciente", e);
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
			catch (SQLException sqle) {
				Log4JManager.error("ERROR buscarCodigoPaciente", sqle);
		}
		}
		return codigo;
	}

	/**
	 * Método que implementa la revisión de permisos para ver
	 * un paciente dada una institución para una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#puedoImprimirPaciente(Connection , int , String ) throws SQLException
	 */
	public static boolean puedoImprimirPaciente(Connection con, int codigoPaciente, String codigoInstitucion) throws SQLException
	{
		boolean respuesta=false;

		int numResultados=0;
		ResultSetDecorator rs=null;
		String consultaExistePacienteStr="SELECT count(1) as numResultados from pacientes_instituciones where codigo_paciente=? and codigo_institucion=?" ;

		PreparedStatementDecorator consultaExistePacienteStatement= new PreparedStatementDecorator(con.prepareStatement(consultaExistePacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaExistePacienteStatement.setInt (1, codigoPaciente);
		consultaExistePacienteStatement.setInt (2, Utilidades.convertirAEntero(codigoInstitucion));

		rs=new ResultSetDecorator(consultaExistePacienteStatement.executeQuery());

		if (rs.next())
		{
			numResultados+=rs.getInt("numResultados");
		}

		if (numResultados != 0)
		{
			respuesta=true;
		}
		rs.close();
		return respuesta;
	}

	/**
	 * Método que implementa las validaciones para ingresar
	 * una admisión hospitalaria para una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionIngresarAdmisionHospitalaria (Connection ,  int , int , String ) throws SQLException
	 */
	public static RespuestaValidacion validacionIngresarAdmisionHospitalaria (Connection con,  int codigoPaciente, int idCuenta, String codigoInstitucion) throws SQLException
	{

		int numResultados=0;
		ResultSetDecorator rs=null;

		RespuestaValidacion respuesta= new RespuestaValidacion ("No hay problema para crear esta Admision Hospitalaria", true);

		//Primero validamos que la cuenta exista y:
		//a. pertenezca al paciente especificado
		//b. pertenezca a la institución especificada
		//c. Este en estado "Activa"

		String consultaExistenciaCuentaStr="SELECT count(1) as numResultados " +
			"from cuentas c " +
			"INNER JOIN ingresos ing ON(ing.id=c.id_ingreso) " +
			"where c.id=? and c.codigo_paciente=? and ing.institucion=? and c.estado_cuenta=? ";
		logger.info("idCuenta => "+idCuenta);
		logger.info("codigoPaciente => "+codigoPaciente);
		logger.info("codigoInstitucion => "+codigoInstitucion);
		PreparedStatementDecorator consultaExistenciaCuentaStatement= new PreparedStatementDecorator(con.prepareStatement(consultaExistenciaCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaExistenciaCuentaStatement.setInt(1, idCuenta);
		consultaExistenciaCuentaStatement.setInt(2, codigoPaciente);
		consultaExistenciaCuentaStatement.setString(3, codigoInstitucion);
		consultaExistenciaCuentaStatement.setInt(4, ConstantesBD.codigoEstadoCuentaActiva);

		rs=new ResultSetDecorator(consultaExistenciaCuentaStatement.executeQuery());

		if (rs.next())
		{
			numResultados=rs.getInt("numResultados");
		}
		//Si no hay resultados es porque la cuenta no existe
		if (numResultados==0)
		{
			rs.close();
			return new RespuestaValidacion ("La cuenta especificada para esta admisión no existe para este paciente en esta institucion o esta cerrada", false);
		}
		rs.close();


		//Ahora vamos a verificar que NO haya ninguna otra admision abierta para este paciente

		String consultaIngresarAdmisionStr="SELECT count(1) as numResultados from admisiones_hospi where cuenta =? and estado_admision=?";
		PreparedStatementDecorator consultaIngresarAdmisionStatement= new PreparedStatementDecorator(con.prepareStatement(consultaIngresarAdmisionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaIngresarAdmisionStatement.setInt(1, idCuenta);
		consultaIngresarAdmisionStatement.setInt(2, ConstantesBD.codigoEstadoAdmisionHospitalizado);

		rs=new ResultSetDecorator(consultaIngresarAdmisionStatement.executeQuery());
		if (rs.next())
		{
			numResultados=rs.getInt("numResultados");
		}
		if (numResultados>0)
		{
			rs.close();
			return new RespuestaValidacion ("La admisión no puede ser creada ya que existe otra admisión hospitalaria abierta en el sistema", false);
		}
		rs.close();
		return respuesta;
	}

	
	/**
	 * Método que implementa la revisión de permisos de ingreso
	 * de evolución para una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validarIngresarEvolucion (Connection , PersonaBasica, UsuarioBasico ) throws SQLException
	 */
	public static RespuestaValidacionTratante validarIngresarEvolucion (Connection con, PersonaBasica paciente, UsuarioBasico usuario) throws SQLException
	{
		ResultSetDecorator rs;
		int centroCostoUsuario=usuario.getCodigoCentroCosto();
		
		logger.info("Acabamos de entrar a validar lo primero");
		
		//No revisamos si hay valoración anterior si existe asocio
		//if (!paciente.getExisteAsocio())
		{
			//Una vez se tiene la cuenta  hay que revisar que haya al menos una valoración
			//RESPONDIDA por un médico de este centro de costo
			String tieneValoracionAnteriorStr;
			/*if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
			{*/
				tieneValoracionAnteriorStr="SELECT count(1) as numResultados from solicitudes sol, valoraciones val where sol.numero_solicitud=val.numero_solicitud  and sol.cuenta=?"; //and ("+ValoresPorDefecto.getCentroCostoUrgencias(usuario.getCodigoInstitucionInt())+"=? or sol.centro_costo_solicitado=" + ConstantesBD.codigoCentroCostoTodos+ ")";
			/*}
			else
			{
				tieneValoracionAnteriorStr="SELECT count(1) as numResultados from solicitudes sol, valoraciones val where sol.numero_solicitud=val.numero_solicitud  //and sol.cuenta=? and (sol.centro_costo_solicitado=? or sol.centro_costo_solicitado=" + ConstantesBD.codigoCentroCostoTodos+ ")";
			}*/

			PreparedStatementDecorator tieneValoracionAnteriorStatement=new PreparedStatementDecorator(con.prepareStatement (tieneValoracionAnteriorStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info("La consulta>>>>>>>>>>>>>"+tieneValoracionAnteriorStr);
			logger.info("La cuenta>>>>>>>>>>>>>"+paciente.getCodigoCuenta());
			logger.info("El centro costo>>>>>>>>>>>>>"+centroCostoUsuario);
			
			tieneValoracionAnteriorStatement.setInt(1, paciente.getCodigoCuenta());
			//tieneValoracionAnteriorStatement.setInt(2, centroCostoUsuario);

			rs=new ResultSetDecorator(tieneValoracionAnteriorStatement.executeQuery());

			if (rs.next())
			{
				if (rs.getInt("numResultados")<1)
				{
					rs.close();

					if(esMedicoTratante(con, usuario.getLoginUsuario(), paciente.getCodigoCuenta()) || esAdjuntoCuenta(con, paciente.getCodigoCuenta(), usuario.getLoginUsuario()))
					{
						if(!tieneValoraciones(con, paciente.getCodigoCuenta()))
						{
							return new RespuestaValidacionTratante("No se puede registrar evolución, está pendiente la valoración del paciente", false, false);
						}
						else
						{
							logger.info("Esto se fue por aca>>>>>>>>>>>>>>>>>>>");
							return new RespuestaValidacionTratante("", true, true);
						}
					}
					else
					{
						return new RespuestaValidacionTratante("Solo los médicos del centro de costo adjunto o tratante pueden ingresar Evoluciones", false, false);
					}
				}
			}
			else
			{
				//Si esta consulta no da un resultado (es un count) es porque algo muy, pero
				//muy malo esta pasando
				logger.info("Acabamos de entrar a validar lo primero Esta es la primera>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				throw new SQLException ("Error Desconocido en la validación del ingreso de la evolución (Búsqueda Valoraciones Anteriores)");
			}
			rs.close();
		}
		
		
		logger.info("Acabamos de entrar a validar lo primero>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>11111");

		boolean tieneEgreso=tieneEgreso(con, paciente.getCodigoCuenta());
		//Por último vamos a revisar que no haya egreso para este cuenta
		
		
		logger.info("Acabamos de entrar a validar lo primero>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+tieneEgreso);
		
		
		if (tieneEgreso)
		{
			
			int codigoEvolucion= obtenerCodigoUltimaEvolucion(con, paciente.getCodigoCuenta());
			logger.info(">>>>>>>>>> CE"+codigoEvolucion);
			if(codigoEvolucion>0)
			{
				int destinoSalida=obtenerDestinoSalida(con, codigoEvolucion);
				logger.info(">>>>>>>>>> DS"+destinoSalida);
				if(destinoSalida==ConstantesBD.codigoDestinoSalidaCirugiaAmbulatoria||destinoSalida==ConstantesBD.codigoDestinoSalidaHospitalizacion||destinoSalida==ConstantesBD.codigoDestinoSalidaHospitalizar||destinoSalida==ConstantesBD.codigoDestinoSalidaTrasladoCuidadoEspecial)
				{
					int cuentaAsocio=obtenerAsocioCuenta(con, paciente.getCodigoIngreso());
					logger.info(">>>>>>>>>> CA"+cuentaAsocio);
					if(cuentaAsocio<0)
					{
						return new RespuestaValidacionTratante("evolucion", true, false);
					}
					else
					{
						return new RespuestaValidacionTratante("No puede llenar una evolución si ya se dió orden de salida", false, false);
					}
				}
				else
				{	
					return new RespuestaValidacionTratante("No puede llenar una evolución si ya se dió orden de salida", false, false);
				}
			}
			else
			{
			
			
				logger.info("Esto significa que tiene egreso>>>>>>");
				
				//En este punto hay que saber que mensaje se debe
				//mostrar, si hay valoracion de urgencias y la conducta
				//a seguir es 1,2 o 4, se le dice que para la conducta a
				//seguir escogida en la valoracion de urgencias, NO
				//se puede crear evolucion
				String mirarConductaASeguirValoracionUrgenciasStr="SELECT valu.codigo_conducta_valoracion as codigoConducta from solicitudes sol, valoraciones_urgencias valu  where sol.numero_solicitud=valu.numero_solicitud and sol.cuenta=?";
				PreparedStatementDecorator mirarConductaASeguirValoracionUrgenciasStatement= new PreparedStatementDecorator(con.prepareStatement(mirarConductaASeguirValoracionUrgenciasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				mirarConductaASeguirValoracionUrgenciasStatement.setInt(1, paciente.getCodigoCuenta());
	
				rs=new ResultSetDecorator(mirarConductaASeguirValoracionUrgenciasStatement.executeQuery());
				if (rs.next())
				{
					//Si hay conducta, la revisamos
					//si es diferente de 3 mostramos el otro mensaje
					//si no no manejamos mas casos sino dejamos
					//que termine los dos if y se vaya con el caso por
					//defecto
					int codigoConducta=rs.getInt("codigoConducta");
					String mensaje = "No se puede realizar evolución, debe modificar la conducta a seguir en la valoración a cama observacion y/o reanimación de urgencias";
					
					
					logger.info("Esto significa que esta mirando conducta>>>>>>"+codigoConducta);
					
					switch(codigoConducta)
					{
						case ConstantesBD.codigoConductaSeguirHospitalizarPiso:
						case ConstantesBD.codigoConductaSeguirTrasladoCuidadoEspecial:
						case ConstantesBD.codigoConductaSeguirSalaCirugiaAmbulatoria:
							mensaje = "No se puede realizar evolución, debe reversar el asocio cuenta, reversar el egreso y modificar la conducta a seguir en la valoración a cama observacion y/o reanimación de urgencias";
						break;
						case ConstantesBD.codigoConductaSeguirSalidaSinObservacion:
							mensaje = "No se puede realizar evolución, debe reversar el egreso y modificar la conducta a seguir en la valoración a cama observacion y/o reanimación de urgencias";
						break;
							
					}
					
					rs.close();
					return new RespuestaValidacionTratante(mensaje, false, false);
					
				}
	
				rs.close();
				return new RespuestaValidacionTratante("No puede llenar una evolución si ya se dió orden de salida", false, false);
			}	
		}

		//En este punto tengo valoracion llena, no tengo egresos, todo esta bien
		//Solo falta sacar el hecho de si el centro de costo del médico es tratante
		//o no

		return validarCentroCostoTratanteAdjunto(con, paciente, centroCostoUsuario, usuario);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static int obtenerAsocioCuenta(Connection con, int ingreso) 
	{
		String consulta="select cuenta_final from asocios_cuenta where ingreso=? and cuenta_final is not null " ;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			
			logger.info("consulta>>>>>>>>>>"+consulta);
			logger.info("codigoCuenta>>>>>>>>>>"+ingreso);
			
			ps.setInt(1, ingreso);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getInt(1);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoEvolucion
	 * @return
	 */
	public static int obtenerDestinoSalida(Connection con, int codigoEvolucion) 
	{
		String consulta="SELECT " +
						"destino_salida as destinosalida " +
						"from " +
							"egresos " +
						"where " +
							"evolucion=? ";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			
			logger.info("consulta>>>>>>>>>>"+consulta);
			logger.info("codigoCuenta>>>>>>>>>>"+codigoEvolucion);
			
			ps.setInt(1, codigoEvolucion);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getInt(1);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static int obtenerCodigoUltimaEvolucion(Connection con, int codigoCuenta) 
	{
		String consulta="SELECT " +
						"max(e.codigo) as ultimaevol " +
						"from " +
							"evoluciones e " +
							"inner join solicitudes s on(e.valoracion=s.numero_solicitud) " +
						"where " +
							"s.cuenta=? ";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			
			logger.info("consulta>>>>>>>>>>"+consulta);
			logger.info("codigoCuenta>>>>>>>>>>"+codigoCuenta);
			
			ps.setInt(1, codigoCuenta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getInt(1);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param paciente
	 * @param centroCostoUsuario
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	private static RespuestaValidacionTratante validarCentroCostoTratanteAdjunto(Connection con, PersonaBasica paciente, int centroCostoUsuario, UsuarioBasico usuario) throws SQLException
	{
		//if (UtilidadValidacion.esCentroCostoTratante(con, paciente, centroCostoUsuario, usuario.getCodigoInstitucionInt()))
		if (esMedicoTratante(con, usuario.getLoginUsuario(), paciente.getCodigoCuenta()))
		{
			//En este punto reviso si el usuario es médico, si no lo es
			//no puede ingresar a llenar evolución
			String mensaje = UtilidadValidacion.esMedico(usuario); 
			if (mensaje.equals(""))
			{
				return new RespuestaValidacionTratante("Puede Ingresar la evolución y el médico es tratante", true, true);
			}
			else
			{
				logger.info("\n VALIDACION HOSPITAL DIA -->"+paciente.isHospitalDia()+" ES PROFESIONMAL DE LA SALUD->"+UtilidadValidacion.esProfesionalSalud(usuario));
				//si el paciente es hospital dia no debe validar la ocupacion medica
				if(paciente.isHospitalDia())
				{
					if(!UtilidadValidacion.esProfesionalSalud(usuario))
					{	
						logger.info("warning hospital dia");
						return new RespuestaValidacionTratante ("No puede llenar la evolución porque el usuario no es médico (a pesar de pertenecer al Centro de Costo tratante)", false, false);
					}
					else
					{
						return new RespuestaValidacionTratante("Puede Ingresar la evolución y es profesional de la salud y hospital dia", true, false);
					}
				}
				else
				{
					logger.info("warning NOOOO hospital dia");
					//se verifica si está definida la ocupación médico especialista y/o general
					//de parámetros generales
					if(mensaje.equals("errors.noOcupacionMedica"))
						return new RespuestaValidacionTratante("Falta definir ocupación médico(a) general y/o especialista. Revisar Parámetros Generales", false, false);
					else
						return new RespuestaValidacionTratante("No puede llenar la evolución porque el usuario no es médico (a pesar de pertenecer al Centro de Costo tratante)", false, false);
				}	
			}
			
		}
		else
		{
			//Se debe verificar que el adjunto sea médico
			String mensaje = UtilidadValidacion.esMedico(usuario);
			if(!mensaje.equals(""))
			{
				if(mensaje.equals("errors.noOcupacionMedica"))
					return new RespuestaValidacionTratante("Falta definir ocupación médico(a) general y/o especialista. Revisar Parámetros Generales", false, false);
				else if(mensaje.equals("errors.noProfesionalSaludMedico"))
					return new RespuestaValidacionTratante("No puede llenar la evolución porque el usuario no es médico", false, false);
			}
			
			//Si pasamos todas las validaciones anteriores
			//hay valoración anterior, no egreso, y NO es
			//tratante, con el nuevo concepto de adjunto debemos
			//validar que por lo menos sea adjunto (Pudo ser solo
			//por concepto o que el manejo adjunto ya terminó)
			//No importa si es o no médico

			if (UtilidadValidacion.esAdjuntoCuenta(con, paciente.getCodigoCuenta(), usuario.getLoginUsuario()))
			{
				return new RespuestaValidacionTratante("Puede Ingresar la evolución pero el médico no es tratante", true, false);
			}
			else
			{
				return new RespuestaValidacionTratante("Solo los médicos del centro de costo adjunto o tratante pueden ingresar Evoluciones", false, false);
			}
		}
	}

	/**
	 * Método que implementa la revisión de si una cuenta tiene
	 * egreso (solo tiene en cuenta egreso normales y automáticos)
	 * para una BD Genérica.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#tieneEgreso (Connection , int ) throws SQLException
	 */
	public static boolean tieneEgreso (Connection con, int idCuenta) throws SQLException
	{
		String tieneEgresoStr="SELECT count(1) as numResultados from egresos where (codigo_medico is not null or es_automatico=" + ValoresPorDefecto.getValorTrueParaConsultas() + ") and cuenta=?" ;
		PreparedStatementDecorator tieneEgresoStatement= new PreparedStatementDecorator(con.prepareStatement(tieneEgresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		tieneEgresoStatement.setInt(1, idCuenta);
		ResultSetDecorator rs=new ResultSetDecorator(tieneEgresoStatement.executeQuery());
		if (rs.next())
		{
			if (rs.getInt("numResultados")>0)
			{
				rs.close();
				return true;
			}
			else
			{
				rs.close();
				return false;
			}
		}
		else
		{
			rs.close();
			throw new SQLException ("Fallo un count en tieneEgreso");
		}
	}

	/**
	 * Método que implementa la revisión de si un egreso puede
	 * ser finalizado para una BD Genérica.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#puedoFinalizarEgreso (Connection , int ) throws SQLException
	 */
	public static RespuestaValidacion puedoFinalizarEgreso (Connection con, int idCuenta) throws SQLException
	{
		String puedoFinalizarEgresoStr="SELECT es_automatico as esAutomatico, codigo_medico as numIdMedico, usuario_responsable as usuario from egresos where cuenta=?" ;
		PreparedStatementDecorator puedoFinalizarEgresoStatement= new PreparedStatementDecorator(con.prepareStatement(puedoFinalizarEgresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		puedoFinalizarEgresoStatement.setInt(1, idCuenta);

		ResultSetDecorator rs=new ResultSetDecorator(puedoFinalizarEgresoStatement.executeQuery());

		if (rs.next())
		{
			if (rs.getBoolean("esAutomatico"))
			{
				rs.close();
				return new RespuestaValidacion("No se puede crear el egreso porque el paciente tiene egreso automático", false);
			}
			else if (UtilidadTexto.isEmpty(rs.getString("numIdMedico")))
			{
				rs.close();
				//caso de una reversión de egreso
				return new RespuestaValidacion("Paciente sin orden de salida en la evolucion", false);
			}
			else if (!UtilidadTexto.isEmpty(rs.getString("usuario")))
			{
				rs.close();
				return new RespuestaValidacion("No se puede crear este egreso porque ya existe", false);
			}
			else
			{
				//Si no fue porque todo salió bien
				rs.close();
				return new RespuestaValidacion("No hay problema para crear este egreso", true);
			}
		}
		else
		{
			rs.close();
			return new RespuestaValidacion("Paciente sin orden de salida en la evolucion.", false);
		}
	}

	/**
	 * Método que implementa la revisión de si un egreso está
	 * completo para una BD Genérica.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeEgresoCompleto (Connection , int ) throws SQLException
	 */
	public static boolean existeEgresoCompleto (Connection con, int idCuenta) throws SQLException
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try{
			String existeEgresoCompletoStr="SELECT count(1) as numResultados from egresos where codigo_medico is not null and usuario_responsable is not null and cuenta=?";
			pst= con.prepareStatement(existeEgresoCompletoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, idCuenta);
			rs=pst.executeQuery();
	
			if (rs.next()){
				if (rs.getInt("numResultados")<1){
					return false;
				}
				else{
					return true;
				}
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR existeEgresoCompleto",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR existeEgresoCompleto", e);
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
		return false;
	}

	/**
	 * Método que implementa la revisión de si existe egreso un
	 * egreso automático para una BD Genérica.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeEgresoAutomatico (Connection , int ) throws SQLException
	 */
	public static RespuestaValidacion existeEgresoAutomatico (Connection con, int idCuenta) throws SQLException
	{
		String existeEgresoAutomaticoStr="SELECT usuario_responsable as usuarioResponsable from egresos where es_automatico=" + ValoresPorDefecto.getValorTrueParaConsultas() + " and cuenta=?" ;
		PreparedStatementDecorator existeEgresoAutomaticoStatement= new PreparedStatementDecorator(con.prepareStatement(existeEgresoAutomaticoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		existeEgresoAutomaticoStatement.setInt(1, idCuenta);

		ResultSetDecorator rs=new ResultSetDecorator(existeEgresoAutomaticoStatement.executeQuery());

		if (rs.next())
		{
			String usuarioResponsable=rs.getString("usuarioResponsable");
			rs.close();
			return new RespuestaValidacion(usuarioResponsable, true);
		}
		else
		{
			rs.close();
			return new RespuestaValidacion("", false);
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static boolean existeEgresoMedico(Connection con, int cuenta)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
		{
			String consulta="SELECT cuenta from egresos where cuenta=? and destino_salida is not null";
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, cuenta);
			rs=pst.executeQuery();
			if (rs.next())
			{
				return true;
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR existeEgresoMedico",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR existeEgresoMedico", e);
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
		return false;
	}
	
	/**adición sebastián
	 * 
	 * Este metodo valida que el login de usuario que va a ser
	 * ingresado en el sistema no exista previamente
	 * 
	 * @param con
	 * @param login
	 * @return
	 * @throws SQLException
	 */
	public static RespuestaValidacion validacionIngresarUsuario (Connection con, String login){
			int numResultados;
			ResultSetDecorator rs = null;
			RespuestaValidacion respuesta= new RespuestaValidacion ("No hay problema para ingresar el usuario", true);

			try{
				String consultaLoginStr="select count(1) as numResultados from usuarios where login=?";
				PreparedStatementDecorator consultaLoginStatement= new PreparedStatementDecorator(con.prepareStatement(consultaLoginStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				consultaLoginStatement.setString(1, login);
				rs=new ResultSetDecorator(consultaLoginStatement.executeQuery());

				if (rs.next())
				{
					numResultados=rs.getInt("numResultados");
					if (numResultados>0)
					{
						rs.close();
						return new RespuestaValidacion ("Por favor intente con otro login, el que escogio ya esta siendo usado por otra persona", false);
					}
				}
				rs.close();
				return respuesta;
				}
			catch(SQLException e){
					logger.error("Error validando el login de usuario en SQLBaseUtilidadValidacionDao: "+e);
					return null;
				}
		}
	/**
	 * Método que implementa las validaciones necesarias para saber
	 * si se puede ingresar un usuario para una BD Genérica.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionIngresarUsuario (Connection , String ,  String , String , String , String , String ) throws SQLException
	 */
	public static RespuestaValidacion validacionIngresarUsuario (Connection con, String login,  String tipoId, String numeroId,  String codBarrio) throws SQLException
	{

		int numResultados;
		ResultSetDecorator rs = null;
		RespuestaValidacion respuesta= new RespuestaValidacion ("No hay problema para ingresar el usuario", true);

		//Puede que el usuario no haya seleccionado el barrio
		logger.info("codBarrio=> "+codBarrio);
		if (codBarrio==null||codBarrio.equals(""))
		{
			return new RespuestaValidacion ("Por favor seleccione la ciudad y el departamento de la persona", false);
		}

		if (!existeBarrio(con, codBarrio))
		{
			return new RespuestaValidacion ("Por favor seleccione la ciudad y el departamento de la persona", false);
		}

		String consultaLoginStr="select count(1) as numResultados from usuarios where login=?";
		PreparedStatementDecorator consultaLoginStatement= new PreparedStatementDecorator(con.prepareStatement(consultaLoginStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaLoginStatement.setString(1, login);
		rs=new ResultSetDecorator(consultaLoginStatement.executeQuery());

		if (rs.next())
		{
			numResultados=rs.getInt("numResultados");
			if (numResultados>0)
			{
				rs.close();
				return new RespuestaValidacion ("Por favor intente con otro login, el que escogio ya esta siendo usado por otra persona", false);
			}
		}
		rs.close();

		String consultaIdentificacionStr="SELECT count(1) as numResultados from usuarios us, personas per where per.tipo_identificacion=? and per.numero_identificacion=? and per.codigo=us.codigo_persona";
		PreparedStatementDecorator consultaIdentificacionStatement= new PreparedStatementDecorator(con.prepareStatement(consultaIdentificacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaIdentificacionStatement.setString(1, tipoId);
		consultaIdentificacionStatement.setString(2, numeroId);
		rs=new ResultSetDecorator(consultaIdentificacionStatement.executeQuery());

		if (rs.next())
		{
			numResultados=rs.getInt("numResultados");
			if (numResultados>0)
			{
				rs.close();
				return new RespuestaValidacion ("Este usuario ya existe en el sistema", false);
			}
		}

		rs.close();
		return respuesta;
	}

	/**
	 * Método que implementa las validaciones necesarias para saber
	 * si se puede modificar un usuario para una BD Genérica.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionModificarUsuario (Connection , String , String , String , String , String , String , String ) throws SQLException
	 */
	public static RespuestaValidacion validacionModificarUsuario (Connection con, String tipoId, String numeroId, String tipoIdViejo, String numeroIdViejo, String codBarrio) throws SQLException
	{
		int numResultados;
		ResultSetDecorator rs = null;
		RespuestaValidacion respuesta= new RespuestaValidacion ("No hay problema para ingresar el usuario", true);


		//Puede que el usuario no haya seleccionado el barrio

		if (codBarrio==null||codBarrio.equals(""))
		{
			return new RespuestaValidacion ("Por favor seleccione la ciudad y el departamento de la persona", false);
		}

		if (!existeBarrio(con, codBarrio))
		{
			return new RespuestaValidacion ("Por favor seleccione la ciudad y el departamento de la persona", false);
		}

		// Si la persona quiere mantener su numero de identificacion
		// la validacion no es necesaria

		if (tipoId.equals(tipoIdViejo) && numeroId.equals(numeroIdViejo)) return respuesta;


		String consultaIdentificacionStr="SELECT count(1) as numResultados from personas where tipo_identificacion=? and numero_identificacion=?";
		PreparedStatementDecorator consultaIdentificacionStatement= new PreparedStatementDecorator(con.prepareStatement(consultaIdentificacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaIdentificacionStatement.setString(1, tipoId);
		consultaIdentificacionStatement.setString(2, numeroId);
		rs=new ResultSetDecorator(consultaIdentificacionStatement.executeQuery());

		if (rs.next())
		{
			numResultados=rs.getInt("numResultados");
			if (numResultados>0)
			{
				rs.close();
				return new RespuestaValidacion ("Este usuario ya existe en el sistema", false);
			}
		}

		//Con el siguiente caso nos aseguramos que ese usuario solo existe como usuario
		//para que ya en este punto, donde sabemos que el usuario quiere cambiar su
		//identificación . Ahora vamos a revisar que no vaya a haber ningún medico
		//o paciente con esa misma identificación, si no nos vamos a encontrar con
		//un error de constraints


		//Cancino: estas validaciones no son necesariaas con el nuevo esquema de codigo id interno
		/*consultaIdentificacion="SELECT count(1) as numResultados from medicos med, personas per where per.tipo_identificacion='" + tipoIdViejo  + "' and per.numero_identificacion='" + numeroIdViejo  + "' and med.codigo_medico=per.codigo";
		a=tagDao.resultadoConsulta(con, consultaIdentificacion);
		rs=a.getResultSet();
		con=a.getConnection();
		if (rs.next())
		{
			numResultados=rs.getInt("numResultados");
			if (numResultados>0)
			{
				return new RespuestaValidacion ("Este usuario ya existe como medico, no puede cambiar su identificacion", false);
			}
		}

		consultaIdentificacion="SELECT count(1) as numResultados from pacientes pac, personas per where per.tipo_identificacion='" + tipoIdViejo  + "' and per.numero_identificacion='" + numeroIdViejo  + "' and per.codigo=pac.codigo_paciente";
		a=tagDao.resultadoConsulta(con, consultaIdentificacion);
		rs=a.getResultSet();
		con=a.getConnection();
		if (rs.next())
		{
			numResultados=rs.getInt("numResultados");
			if (numResultados>0)
			{
				return new RespuestaValidacion ("Este usuario ya existe como paciente, no puede cambiar su identificacion", false);
			}
		}*/

		rs.close();
		return respuesta;
	}

	/**
	 * Método que implementa la revisión de si existe un barrio para
	 * una BD Genérica.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeBarrio (Connection , String , String , String ) throws SQLException
	 */
	private static boolean existeBarrio (Connection con, String codBarrio) throws SQLException
	{
		int numResultados=0;
		String consultaBarrioStr="select count(1) as numResultados from barrios where codigo=?";
		PreparedStatementDecorator consultaBarrioStatement= new PreparedStatementDecorator(con.prepareStatement(consultaBarrioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		consultaBarrioStatement.setString(1, codBarrio);

		ResultSetDecorator rs=new ResultSetDecorator(consultaBarrioStatement.executeQuery());

		if (rs.next())
		{
			numResultados=rs.getInt("numResultados");
			if (numResultados==0)
			{
				rs.close();
				return false;
			}
			else
			{
				rs.close();
				return true;
			}
		}
		else
		{
			rs.close();
			throw new SQLException ("No se pudo realizar un count en existeBarrio ");
		}
	}

	/**
	 * Método que implementa la revisión de si se puede modificar una
	 * persona no usuario para una BD Genérica.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionModificarPersonaNoUsuario (Connection ,  String , String , String ) throws SQLException
	 */
	public static RespuestaValidacion validacionModificarPersonaNoUsuario (Connection con,  String codBarrio) throws SQLException
	{
		RespuestaValidacion respuesta= new RespuestaValidacion ("No hay problema para ingresar el medico", true);


		//Puede que el usuario no haya seleccionado el barrio

		
		 if (codBarrio==null||codBarrio.equals(""))
		  {
			return new RespuestaValidacion ("Por favor seleccione la ciudad y el departamento de la persona", false);
		  }

		  if (!existeBarrio(con, codBarrio))
		  {
			return new RespuestaValidacion ("Por favor seleccione la ciudad y el departamento de la persona", false);
		  }
	   
		return respuesta;
	}

	/**
	 * Método que implementa la revisión de si se puede ingresar una
	 * una historia clínica para una BD Genérica.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionIngresarHistoriaClinica (Connection ,  String , String ) throws SQLException
	 */
	public static RespuestaValidacion validacionIngresarHistoriaClinica (Connection con,  String tipoId, String numeroId) throws SQLException
	{
		int numResultados;
		ResultSetDecorator rs=null;

		RespuestaValidacion respuesta= new RespuestaValidacion ("No hay problema para crear esta historia Clinica", true);

		String consultaHistoriaClinicaStr="SELECT count(1) as numResultados FROM historias_clinicas hc, personas per WHERE per.numero_identificacion=? and per.tipo_identificacion=? and per.codigo=hc.codigo_paciente";
		PreparedStatementDecorator consultaHistoriaClinicaStatement= new PreparedStatementDecorator(con.prepareStatement(consultaHistoriaClinicaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaHistoriaClinicaStatement.setString(1, numeroId);
		consultaHistoriaClinicaStatement.setString(2, tipoId);

		rs=new ResultSetDecorator(consultaHistoriaClinicaStatement.executeQuery());

		if (rs.next())
		{
			numResultados=rs.getInt("numResultados");
			if (numResultados>0)
			{
				rs.close();
				return new RespuestaValidacion ("Este paciente ya tiene historia clinica", false);
			}
		}
		rs.close();
		return respuesta;
	}

	/**
	 * Método que valida si se puede cambiar un documento de
	 * identificación para una BD Genérica.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionCambiarDocumento (Connection , String , String , String , String ) throws SQLException
	 */
	public static RespuestaValidacion validacionCambiarDocumento (Connection con, String tipoIdNuevo, String numeroIdNuevo, String tipoIdViejo, String numeroIdViejo) throws SQLException
	{
		int numResultados=0;
		ResultSetDecorator rs=null;

		RespuestaValidacion respuesta= new RespuestaValidacion ("No hay problema para cambiar el documento de este paciente", true);

		// Hay dos cosas que se deben averiguar cuando se trata de actualizar una
		// identificacion, la primera es si el documento destino ya es usado por otra
		// persona y si la persona original existe (En este caso no hay un error
		// pero el usuario no se da cuenta que la actualización no tiene lugar)

		if (existePaciente(con, tipoIdNuevo, numeroIdNuevo))
		{
			return new RespuestaValidacion ("El número del documento al que usted quiere cambiarse pertenece a otra persona", false);
		}
		else
		{
			if (!existePaciente(con, tipoIdViejo, numeroIdViejo))
			{
				return new RespuestaValidacion ("No hay ninguna persona con esa identificacion", false);
			}
		}

		//Con el siguiente caso nos aseguramos que ese paciente solo no existe sino
		//como paciente y (máximo) como usuario.

		String consultaIdentificacionStr="SELECT count(1) as numResultados from medicos med, personas per where per.tipo_identificacion=? and per.numero_identificacion=? and per.codigo=med.codigo_medico";
		PreparedStatementDecorator consultaIdentificacionStatement= new PreparedStatementDecorator(con.prepareStatement(consultaIdentificacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaIdentificacionStatement.setString(1, tipoIdViejo);
		consultaIdentificacionStatement.setString(2, numeroIdViejo);

		rs=new ResultSetDecorator(consultaIdentificacionStatement.executeQuery());

		if (rs.next())
		{
			numResultados=rs.getInt("numResultados");
			if (numResultados>0)
			{
				rs.close();
				return new RespuestaValidacion ("Este paciente existe como medico, no puede cambiar su identificacion", false);
			}
		}

		rs.close();
		return respuesta;
	}

	/**
	 * Método que implementa la revisión de si existe un médico
	 * para una BD Genérica.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeMedico (Connection , String , String ) throws SQLException
	 */
	public static boolean existeMedico (Connection con, String tipoId, String numeroId) throws SQLException
	{
		boolean respuesta=false;

		int numResultados=0;
		ResultSetDecorator rs=null;

		String consultaExisteMedicoStr="select count(1) as numResultados from medicos med, personas per where per.numero_identificacion=? and per.tipo_identificacion=? and per.codigo=med.codigo_medico";
		PreparedStatementDecorator consultaExisteMedicoStatement= new PreparedStatementDecorator(con.prepareStatement(consultaExisteMedicoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaExisteMedicoStatement.setString(1, numeroId);
		consultaExisteMedicoStatement.setString(2, tipoId);

		rs=new ResultSetDecorator(consultaExisteMedicoStatement.executeQuery());

		if (rs.next()) {
			numResultados+=rs.getInt("numResultados");
		}

		if (numResultados != 0) {
			respuesta=true;
		}

		rs.close();
		return respuesta;
	}

	/**
	 * Método que implementa la revisión de si existe un usuario
	 * para una BD Genérica.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeUsuario (Connection , String , String ) throws SQLException
	 */
	public static boolean existeUsuario (Connection con, String tipoId, String numeroId) throws SQLException
	{
		boolean respuesta=false;

		int numResultados=0;
		ResultSetDecorator rs=null;

		String consultaExisteUsuarioStr="select count(1) as numResultados from usuarios us, personas per where per.numero_identificacion=? and per.tipo_identificacion=? and per.codigo=us.codigo_persona";
		PreparedStatementDecorator consultaExisteUsuarioStatement= new PreparedStatementDecorator(con.prepareStatement(consultaExisteUsuarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaExisteUsuarioStatement.setString(1, numeroId);
		consultaExisteUsuarioStatement.setString(2, tipoId);

		rs=new ResultSetDecorator(consultaExisteUsuarioStatement.executeQuery());

		if (rs.next())
		{
			numResultados+=rs.getInt("numResultados");
		}

		if (numResultados != 0)
		{
			respuesta=true;
		}
		rs.close();
		return respuesta;
	}

	/**
	 * Método que implementa la revisión de si existe una admisión
	 * hospitalaria abierta dado su código, para una BD Genérica.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeAdmisionHospitalariaAbiertaCodigo (Connection , String , String ) throws SQLException
	 */
	public static boolean existeAdmisionHospitalariaAbiertaCodigo (Connection con, String codigoAdmision, String codigoInstitucion) throws SQLException
	{
		boolean respuesta=false;

		int numResultados=0;
		ResultSetDecorator rs=null;

		String consultaAdmisionHospitalariaAbiertaCodigoStr="SELECT count(1) as numResultados from admisiones_hospi adh where adh.codigo=? and adh.estado_admision=1 and (select institucion from ingresos where id = (select id_ingreso from cuentas where id=adh.cuenta) )=?" ;
		PreparedStatementDecorator consultaAdmisionHospitalariaAbiertaCodigoStatement= new PreparedStatementDecorator(con.prepareStatement(consultaAdmisionHospitalariaAbiertaCodigoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaAdmisionHospitalariaAbiertaCodigoStatement.setString(1, codigoAdmision);
		consultaAdmisionHospitalariaAbiertaCodigoStatement.setString(2, codigoInstitucion);

		rs=new ResultSetDecorator(consultaAdmisionHospitalariaAbiertaCodigoStatement.executeQuery());

		if (rs.next())
		{
			numResultados+=rs.getInt("numResultados");
		}

		if (numResultados != 0)
		{
			respuesta=true;
		}

		rs.close();
		return respuesta;
	}

	/**
	 * Método que implementa la revisión de si existe una admisión
	 * hospitalaria dado su código, para una BD Genérica.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeAdmisionHospitalariaCodigo (Connection , String , String ) throws SQLException
	 */
	public static RespuestaValidacion existeAdmisionHospitalariaCodigo (Connection con, String codigoAdmision, String codigoInstitucion) throws SQLException
	{
		int codigo=0;
		ResultSetDecorator rs=null;

		String consultaExisteAdmisionHospitalariaCodigoStr="SELECT max(codigo) as codigo from admisiones_hospi adh where adh.codigo=? and (select institucion from ingresos where id = (select id_ingreso from cuentas where id=adh.cuenta) )=?";
		PreparedStatementDecorator consultaExisteAdmisionHospitalariaCodigoStatement= new PreparedStatementDecorator(con.prepareStatement(consultaExisteAdmisionHospitalariaCodigoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		consultaExisteAdmisionHospitalariaCodigoStatement.setString(1, codigoAdmision);
		consultaExisteAdmisionHospitalariaCodigoStatement.setString(2, codigoInstitucion);

		rs=new ResultSetDecorator(consultaExisteAdmisionHospitalariaCodigoStatement.executeQuery());

		if (rs.next())
		{
			codigo+=rs.getInt("codigo");
			if (codigo>0)
			{
				rs.close();
				return new RespuestaValidacion (codigo + "", true);
			}
		}
		rs.close();
		return new RespuestaValidacion (0 + "", false);
	}

	/**
	 * Método que implementa la revisión de si existe una admisión
	 * hospitalaria abierta dado el código del paciente, para una BD
	 * Genérica.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeAdmisionHospitalariaAbiertaPaciente (Connection , String , String , String ) throws SQLException
	 */
	public static RespuestaValidacion existeAdmisionHospitalariaAbiertaPaciente (Connection con, String tipoId, String numeroId, String codigoInstitucion) throws SQLException
	{

		int codigo=0;
		ResultSetDecorator rs=null;

		String existeAdmisionHospitalariaAbiertaPacienteStr="select max(codigo) as codigo from admisiones_hospi where estado_admision=1 and cuenta IN (select id from cuentas where id_ingreso IN (SELECT id from ingresos ing, personas per where per.numero_identificacion=? and per.tipo_identificacion=? and ing.institucion=? and per.codigo=ing.codigo_paciente))";
		PreparedStatementDecorator existeAdmisionHospitalariaAbiertaPacienteStatement= new PreparedStatementDecorator(con.prepareStatement(existeAdmisionHospitalariaAbiertaPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		existeAdmisionHospitalariaAbiertaPacienteStatement.setString(1, numeroId);
		existeAdmisionHospitalariaAbiertaPacienteStatement.setString(2, tipoId);
		existeAdmisionHospitalariaAbiertaPacienteStatement.setString(3, codigoInstitucion);

		rs=new ResultSetDecorator(existeAdmisionHospitalariaAbiertaPacienteStatement.executeQuery());

		if (rs.next())
		{
			codigo+=rs.getInt("codigo");
			if (codigo>0)
			{
				rs.close();
				return new RespuestaValidacion (codigo + "", true);
			}
		}
		rs.close();
		return new RespuestaValidacion (0 + "", false);
	}

	/**
	 * Método que implementa la revisión de si existe una admisión
	 * hospitalaria dado el código del paciente, para una BD Genérica.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeAdmisionHospitalariaPaciente (Connection , String , String , String ) throws SQLException
	 */
	public static RespuestaValidacion existeAdmisionHospitalariaPaciente (Connection con, String tipoId, String numeroId, String codigoInstitucion) throws SQLException
	{
		int codigo=0;
		ResultSetDecorator rs=null;

		//Selecciono el último codigo
		String existeAdmisionHospitalariaPacienteStr="select max(codigo) as codigo from admisiones_hospi where cuenta IN (select id from cuentas where id_ingreso IN (SELECT id from ingresos ing, personas per where per.numero_identificacion=? and per.tipo_identificacion=? and ing.institucion=? and per.codigo=ing.codigo_paciente))";
		PreparedStatementDecorator existeAdmisionHospitalariaPacienteStatement= new PreparedStatementDecorator(con.prepareStatement(existeAdmisionHospitalariaPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		existeAdmisionHospitalariaPacienteStatement.setString(1, numeroId);
		existeAdmisionHospitalariaPacienteStatement.setString(2, tipoId);
		existeAdmisionHospitalariaPacienteStatement.setString(3, codigoInstitucion);

		rs=new ResultSetDecorator(existeAdmisionHospitalariaPacienteStatement.executeQuery());

		if (rs.next())
		{
			codigo+=rs.getInt("codigo");
			if (codigo>0)
			{
				rs.close();
				return new RespuestaValidacion (codigo + "", true);
			}
		}

		rs.close();
		return new RespuestaValidacion (codigo + "", false);
	}

	/**
	 * Método que implementa la revisión de si se puede insertar
	 * un usuario médico para una BD Genérica.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionInsertarUsuarioMedico (Connection , String , String , String ) throws SQLException
	 */
	public static RespuestaValidacion validacionInsertarUsuarioMedico (Connection con, String tipoId, String numeroId, String login,int validarUsuario) throws SQLException
	{
		ResultSetDecorator rs=null;
		int numResultados=0;

		//Asumimos que todo salio bien, si alguna cosa sale mal
		//salimos con un return interior
		RespuestaValidacion respuesta= new RespuestaValidacion ("1-No hay problema para crear este medico como usuario", true);

		if (login==null||login.equals(""))
		{
			return new RespuestaValidacion ("0-Por favor haga click atras y escriba un login valido", false);
		}

		//Ahora revisamos que esa identificacion corresponda a un medico
		if(validarUsuario!=ConstantesBD.codigoNuncaValido)
		{

			String existeMedicoStr="SELECT count(1) as numResultados from medicos med, personas per where per.numero_identificacion=? and per.tipo_identificacion=? and per.codigo=med.codigo_medico";
			PreparedStatementDecorator existeMedicoStatement= new PreparedStatementDecorator(con.prepareStatement(existeMedicoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			existeMedicoStatement.setString(1, numeroId);
			existeMedicoStatement.setString(2, tipoId);
	
			rs=new ResultSetDecorator(existeMedicoStatement.executeQuery());
	
			if (rs.next())
			{
				numResultados=rs.getInt("numResultados");
			}
			rs.close();
		}
		else
			numResultados = 1;
		if (numResultados==0)
		{
			rs.close();
			return new RespuestaValidacion ("1-El medico no existe en el sistema. Por favor use el menu", false);
		}
		
		if(validarUsuario<1){
			String existeUsuarioStr="SELECT count(1) as numResultados from usuarios us, personas per where per.numero_identificacion=? and per.tipo_identificacion=? and us.codigo_persona=per.codigo";
			PreparedStatementDecorator existeUsuarioStatement= new PreparedStatementDecorator(con.prepareStatement(existeUsuarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			existeUsuarioStatement.setString(1, numeroId);
			existeUsuarioStatement.setString(2, tipoId);
	
			rs=new ResultSetDecorator(existeUsuarioStatement.executeQuery());
	
			if (rs.next())
			{
				numResultados=rs.getInt("numResultados");
			}
			if (numResultados>0)
			{
				rs.close();
				return new RespuestaValidacion ("2-Usted ya tiene un login previo. Por favor utilicelo", false);
			}
		}

		if(validarUsuario<2){
			if (existeUsuarioDadoLogin(con, login))
			{
				rs.close();
				return new RespuestaValidacion ("3-Este login ya fue registrador por otro usuario. Por favor seleccione otro", false);
			}
		}
			rs.close();
			return respuesta;
		
	}

	/**
	 * Método que implementa la revisión de si existe un usuario dado
	 * su login para una BD Genérica.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeUsuarioDadoLogin (Connection , String ) throws SQLException
	 */
	private static boolean existeUsuarioDadoLogin (Connection con, String login) throws SQLException
	{
		String existeLoginStr="SELECT count(1) as numResultados from usuarios where login=?";
		PreparedStatementDecorator existeLoginStatement= new PreparedStatementDecorator(con.prepareStatement(existeLoginStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		existeLoginStatement.setString(1, login);

		ResultSetDecorator rs=new ResultSetDecorator(existeLoginStatement.executeQuery());
		if (rs.next())
		{
			if (rs.getInt("numResultados")>0)
			{
				rs.close();
				return true;
			}
			else
			{
				rs.close();
				return false;
			}
		}
		else
		{
			rs.close();
			throw new SQLException ("Error en un count en existeUsuarioDadoLogin");
		}
	}

	/**
	 * Método que implementa la revisión de si se puede insertar un
	 * usuario paciente para una BD Genérica.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionInsertarUsuarioPaciente (Connection , String , String , String ) throws SQLException
	 */
	public static RespuestaValidacion validacionInsertarUsuarioPaciente (Connection con, String tipoId, String numeroId, String login) throws SQLException
	{
		//Asumimos que todo salio bien, si alguna cosa sale mal
		//salimos con un return interior
		RespuestaValidacion respuesta= new RespuestaValidacion ("1-No hay problema para crear este paciente como usuario", true);

		if (login==null||login.equals(""))
		{
			return new RespuestaValidacion ("0-Por favor haga click atras y escriba un login valido", false);
		}

		//Ahora revisamos que esa identificacion corresponda a un paciente
		if (!existePaciente(con, tipoId, numeroId))
		{
			return new RespuestaValidacion ("1-El paciente no existe en el sistema. Por favor use el menu", false);
		}
		if (existeUsuario(con, tipoId, numeroId))
		{
			return new RespuestaValidacion ("2-Usted ya tiene un login previo. Por favor utilicelo", false);
		}
		if (existeUsuarioDadoLogin(con, login))
		{
			return new RespuestaValidacion ("3-Este login ya fue registrador por otro usuario. Por favor seleccione otro", false);
		}
		return respuesta;
	}

	/**
	 * Método que implementa la revisión de permisos de acceso a
	 * pacientes por institución/médico para una BD Genérica.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionPermisosInstitucionMedico (Connection , String , String , String ) throws SQLException
	 */
	public static RespuestaValidacion validacionPermisosInstitucionMedico (Connection con, String tipoId, String numeroId, String codigoInstitucion) throws SQLException
	{
		ResultSetDecorator rs=null;
		int numResultados=0;

		//Asumimos que todo salio bien, si alguna cosa sale mal
		//salimos con un return interior
		RespuestaValidacion respuesta= new RespuestaValidacion ("La inserci&oacute;n no tuvo ning&uacute;n problema", true);

		if	(tipoId==null||numeroId==null||codigoInstitucion==null||tipoId.equals("")||numeroId.equals("") ||codigoInstitucion.equals("") )
		{
			return new RespuestaValidacion ("Por favor no trabaje con valores vacios", false);
		}

		//Ahora revisamos que esa identificacion corresponda a un médico

		if (!existeMedico(con, tipoId, numeroId))
		{
			return new RespuestaValidacion ("1-El Profesional de la salud no existe en el sistema", false);
		}

		//Ahora revisamos que el instituto también exista

		String existeInstitutoStr="SELECT count(1) as numResultados from instituciones where codigo=?";
		PreparedStatementDecorator existeInstitutoStatement= new PreparedStatementDecorator(con.prepareStatement(existeInstitutoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		existeInstitutoStatement.setString(1, codigoInstitucion);

		rs=new ResultSetDecorator(existeInstitutoStatement.executeQuery());

		if (rs.next())
		{
			numResultados=rs.getInt("numResultados");
			if (numResultados==0)
			{
				rs.close();
				return new RespuestaValidacion ("El instituto especificado no existe en el sistema", false);
			}
		}
		else
		{
			rs.close();
			throw new SQLException ("Fallo un count en validacionPermisosInstitucionMedico ");
		}
		rs.close();

		//Llegado a este punto se que existe tanto médico como instituto
		//Si ya existe la pareja en la tabla Medicos_Institutos es porque
		//ya fue insertado previamente, así que le decimos al usuario lo que
		//paso

		int codigoMedico=getCodigoPersona(con, tipoId, numeroId);

		String existeParejaStr="SELECT count(1)  as numResultados from medicos_instituciones where codigo_medico=? and codigo_institucion=?";
		PreparedStatementDecorator existeParejaStatement= new PreparedStatementDecorator(con.prepareStatement(existeParejaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		existeParejaStatement.setInt(1, codigoMedico);
		existeParejaStatement.setString(2, codigoInstitucion);

		rs=new ResultSetDecorator(existeParejaStatement.executeQuery());

		if (rs.next())
		{
			numResultados=rs.getInt("numResultados");
		}
		if (numResultados>0)
		{
			rs.close();
			return new RespuestaValidacion ("El Profesional de la Salud con tipo de identificación " + tipoId + " y número de identificaci&oacute;n " + numeroId  + " ya existe en el sistema", true);
		}
		rs.close();

		//Si llegamos a este punto es porque existe tanto paciente como la institución
		//pero no la pareja, luego tenemos que insertar esta en la base de datos

		String insertarParejaStr="INSERT INTO medicos_instituciones (codigo_medico, codigo_institucion) values (?,?)";
		PreparedStatementDecorator insertarParejaStatement= new PreparedStatementDecorator(con.prepareStatement(insertarParejaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		insertarParejaStatement.setInt(1, codigoMedico);
		insertarParejaStatement.setString(2, codigoInstitucion);

		if (insertarParejaStatement.executeUpdate()==0)
		{
			return new RespuestaValidacion ("No se pudo crear el profesional de la salud en esta institución", false);
		}
		return respuesta;
	}

	/**
	 * Método que implementa la revisión de si se puede cambiar
	 * el consecutivo de historias clínicas para una BD Genérica.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionCambioConsecutivoHistoriasClinicas (Connection , String ) throws SQLException
	 */
	public static RespuestaValidacion validacionCambioConsecutivoHistoriasClinicas (Connection con, String nuevoConsecutivoStr ) throws SQLException
	{
		ResultSetDecorator rs=null;
		int maximoConsecutivo, nuevoConsecutivo;

		//Asumimos que todo salio bien, si alguna cosa sale mal
		//salimos con un return interior
		RespuestaValidacion respuesta= new RespuestaValidacion ("No hay ningún problema para cambiar el consecutivo", true);

		//Antes que nada nos aseguramos que nos hayan pasado un número

		try
		{
			nuevoConsecutivo=Integer.parseInt(nuevoConsecutivoStr);

			//No debemos empezar el consecutivo en un número negativo o en 0, luego
			//lo primero que revisamos es que no nos hayan pasado ninguno de estos
			//valores

			if (nuevoConsecutivo <1)
			{
				return new RespuestaValidacion ("El consecutivo debe ser un número positivo mayor que 0. No se puede cambiar el consecutivo", false);
			}

			//El otro problema que hay que evitar es que se ponga un consecutivo menor
			//a alguno ya usado en el sistema (Ej. No podemos definir que la historia
			//clínica empieze con un consecutivo 10, si existe ya algún paciente con un
			//consecutivo mayor, 13 porque eventualmente se presentarán colisiones en la
			//inserción)

			String encontrarMaximoConsecutivoStr="select max(id) as idMaximo from ingresos";
			PreparedStatementDecorator encontrarMaximoConsecutivoStatement= new PreparedStatementDecorator(con.prepareStatement(encontrarMaximoConsecutivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			rs=new ResultSetDecorator(encontrarMaximoConsecutivoStatement.executeQuery());

			//Una vez ejecutada la consulta sacamos el máximo
			if (rs.next())
			{
				maximoConsecutivo=rs.getInt("idMaximo");
				if (maximoConsecutivo>=nuevoConsecutivo)
				{
					rs.close();
					return new RespuestaValidacion ("El consecutivo no se puede cambiar ya que existe una historia clínica con id mayor al que se quiere imponer", false);
				}
			}
			rs.close();
			return respuesta;
		}
		catch (Exception e)
		{
			rs.close();
			return new RespuestaValidacion ("Por favor utilice un número valido (Nada de letras)", false);
		}
	}

	/**
	 * Método que implementa la validación de si se puede activar
	 * un médico para una BD Genérica.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionActivarMedico (Connection , String , String , String ) throws SQLException
	 */
	public static RespuestaValidacion validacionActivarMedico (Connection con, String tipoId, String numeroId, String codigoInstitucion) throws SQLException
	{

		RespuestaValidacion respuesta=new RespuestaValidacion ("0-No hay ningún problema con los permisos)", true);

		int numResultados=0;
		ResultSetDecorator rs=null;

		//Primero vamos a ver si el médico esta inactivo
		String medicoActivoStr="SELECT count(1) as numResultados from medicos_inactivos mi, personas per where per.numero_identificacion=? and per.tipo_identificacion=? and mi.codigo_institucion=? AND mi.codigo_medico=per.codigo";
		PreparedStatementDecorator medicoActivoStatement= new PreparedStatementDecorator(con.prepareStatement(medicoActivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		medicoActivoStatement.setString(1, numeroId);
		medicoActivoStatement.setString(2, tipoId);
		medicoActivoStatement.setString(3, codigoInstitucion);

		rs=new ResultSetDecorator(medicoActivoStatement.executeQuery());
		if (rs.next())
		{
			numResultados=rs.getInt("numResultados");
			if (numResultados==0)
			{
				rs.close();
				return new RespuestaValidacion ("2-El Medico no está inactivo", false);
			}
		}
		rs.close();

		//Ahora revisamos si es necesario hacer el proceso con el usuario
		//(si este médico tiene usuario)
		String tieneMedicoUsuarioStr="SELECT count(1) as numResultados from usuarios us, usuarios_inactivos usinac, personas per where us.login=usinac.login and per.numero_identificacion=? and per.tipo_identificacion=? AND per.codigo=us.codigo_persona";
		PreparedStatementDecorator tieneMedicoUsuarioStatement= new PreparedStatementDecorator(con.prepareStatement(tieneMedicoUsuarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		tieneMedicoUsuarioStatement.setString(1, numeroId);
		tieneMedicoUsuarioStatement.setString(2, tipoId);

		rs=new ResultSetDecorator(tieneMedicoUsuarioStatement.executeQuery());

		if (rs.next())
		{
			numResultados=Integer.parseInt(rs.getString("numResultados"));
			if (numResultados==0)
			{
				rs.close();
				return new RespuestaValidacion ("3-No necesita Activar Usuario", true);
			}
		}
		rs.close();
		return respuesta;
	}

	/**
	 * Método que implementa la validación de si se puede cambiar
	 * el password de administrador para una BD Genérica.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionCambiarPasswordAdministrador (Connection , String , String ) throws SQLException
	 */
	public static RespuestaValidacion validacionCambiarPasswordAdministrador (Connection con, String login, String codigoInstitucion,String password) throws SQLException
	{

		RespuestaValidacion respuesta=new RespuestaValidacion ("No Hubo Ningun problema para cambiar el Password)", true);

		int numResultados=0;
		ResultSetDecorator rs=null;

		//Primero vamos a ver si el usuario pertenece a esta institución
		String usuarioPerteneceInstitucionStr="SELECT count(1) as numResultados from usuarios where login=? and institucion=?" ;
		PreparedStatementDecorator usuarioPerteneceInstitucionStatement= new PreparedStatementDecorator(con.prepareStatement(usuarioPerteneceInstitucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		usuarioPerteneceInstitucionStatement.setString(1, login);
		usuarioPerteneceInstitucionStatement.setString(2, codigoInstitucion);

		rs=new ResultSetDecorator(usuarioPerteneceInstitucionStatement.executeQuery());
		if (rs.next())
		{
			numResultados=Integer.parseInt(rs.getString("numResultados"));
			if (numResultados==0)
			{
				rs.close();
				return new RespuestaValidacion ("El login del usuario dado no pertenece a esta institucion ", false);
			}
		}
		rs.close();

		String superAdministradorStr="SELECT count(1) as numResultados from roles_usuarios where login= ? and nombre_rol ='superadministrador'";
		PreparedStatementDecorator superAdministradorStatement= new PreparedStatementDecorator(con.prepareStatement(superAdministradorStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		superAdministradorStatement.setString(1, login);

		rs=new ResultSetDecorator(superAdministradorStatement.executeQuery());
		if (rs.next())
		{
			numResultados=Integer.parseInt(rs.getString("numResultados"));
			if (numResultados>0)
			{
				rs.close();
				return new RespuestaValidacion ("No se puede cambiar la contraseña del superadministrador", false);
			}
		}
		rs.close();
		
		
		String consultaTempo="SELECT password as pwd from usuarios where login= ? ";
		String tempoPasswd=MD5Hash.hashPassword(password);
		PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaTempo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ps.setString(1, login);

		rs=new ResultSetDecorator(ps.executeQuery());
		if (rs.next())
		{
			if(rs.getString(1).equals(tempoPasswd))
			{
				rs.close();
				return new RespuestaValidacion ("No se puede cambiar la contraseña por la misma que tenia anteriormente.", false);
			}
		}
		rs.close();
		
		
		return respuesta;
	}

	/**
	 * Método que implementa la validación de si se puede reversar
	 * un egreso por tiempo para una BD Genérica.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#puedoReversarEgresoPorTiempo(Connection , int ) throws SQLException
	 */
	public static ResultSetDecorator puedoReversarEgresoPorTiempo(Connection ac_con, int ai_idCuenta) throws SQLException
	{
		String ls_consulta	=
			"SELECT "	+	"to_char(CURRENT_DATE,'"+ConstantesBD.formatoFechaBD+"') "			+ "AS fechaActual,"						+
							""+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+" "		+ "AS horaActual,"						+
							"to_char(eg.fecha_egreso,'"+ConstantesBD.formatoFechaBD+"') "		+ "AS fechaEgreso,"						+
							"eg.hora_egreso "		+ "AS horaEgreso,"						+
							"to_char(ev.fecha_evolucion,'"+ConstantesBD.formatoFechaBD+"') "	+ "AS fechaEvolucion,"					+
							"ev.hora_evolucion "	+ "AS horaEvolucion "					+
			"FROM "		+	"solicitudes "	+ "sol,"								+
							"evoluciones "				+ "ev,"								+
							"egresos "					+ "eg "								+
			"WHERE "	+	"sol.cuenta"			+ "=?  AND "	+
							"ev.valoracion"	+ "=sol.numero_solicitud"	+ " AND "	+
							"ev.orden_salida"		+ "=" + ValoresPorDefecto.getValorTrueParaConsultas() + ""					+ " AND "	+
							"eg.cuenta"				+ "=sol.cuenta"			+ " AND "	+
							"eg.evolucion"			+ "=ev.codigo";
		logger.info("Consulta: "+ls_consulta+" id_cuenta: "+ai_idCuenta);
		PreparedStatementDecorator ls_consultaStatement=new PreparedStatementDecorator(ac_con.prepareStatement(ls_consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ls_consultaStatement.setInt(1, ai_idCuenta);
		
		return new ResultSetDecorator(ls_consultaStatement.executeQuery());
	}

	/**
	 * Método que implementa la revisión de si una cuenta tiene
	 * fecha de admisión para una BD Genérica.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#tieneFechaAdmisionCuenta (Connection , int , int , int ) throws SQLException
	 */
	public static RespuestaValidacion tieneFechaAdmisionCuenta (Connection con, int idCuenta, int idAdmision, int anioAdmision) throws SQLException
	{
		String consultaFechaStr="";
		if (anioAdmision>0)
		{
			consultaFechaStr="SELECT to_char(fecha_admision, 'YYYY-MM-DD') as fechaDada from admisiones_urgencias where codigo=" + idAdmision + " and anio="+ anioAdmision;
		}
		else if (idAdmision>0)
		{
			consultaFechaStr="SELECT to_char(fecha_admision, 'YYYY-MM-DD') as fechaDada from admisiones_hospi where codigo="+idAdmision;
		}
		else
		{
			consultaFechaStr="SELECT to_char(fecha_apertura, 'YYYY-MM-DD') as fechaDada from cuentas where id="+ idCuenta;
		}
		//Ahora tenemos todos los (hasta el momento) posibles casos
		PreparedStatementDecorator consultaFechaStatement= new PreparedStatementDecorator(con.prepareStatement(consultaFechaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		ResultSetDecorator rs=new ResultSetDecorator(consultaFechaStatement.executeQuery());
		if (rs.next())
		{
			RespuestaValidacion a=new RespuestaValidacion(rs.getString("fechaDada"), true);
			rs.close();
			return a;
		}
		else
		{
			rs.close();
			return new RespuestaValidacion("No se encontró ninguna fecha para las condiciones dadas en tieneFechaAdmisionCuenta", false);
		}
	}

	/**
	 * Método que implementa la revisión de si un paciente en
	 * urgencias ya tiene asignada cama, para una BD Genérica.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#tieneCamaParaEgresoUrgencias (Connection , int , int ) throws SQLException
	 */
	public static boolean tieneCamaParaEgresoUrgencias (Connection con, int idAdmision, int anioAdmision) throws SQLException
	{
		String tieneDatosObservacionAdmisionUrgenciasCompletoStr="SELECT count(1) as numResultados from admisiones_urgencias where fecha_ingreso_observacion is not null and hora_ingreso_observacion is not null  and codigo=? and anio=?";
		PreparedStatementDecorator tieneDatosObservacionAdmisionUrgenciasCompletoStatement= new PreparedStatementDecorator(con.prepareStatement(tieneDatosObservacionAdmisionUrgenciasCompletoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		tieneDatosObservacionAdmisionUrgenciasCompletoStatement.setInt(1, idAdmision);
		tieneDatosObservacionAdmisionUrgenciasCompletoStatement.setInt(2, anioAdmision);

		ResultSetDecorator rs=new ResultSetDecorator(tieneDatosObservacionAdmisionUrgenciasCompletoStatement.executeQuery());

		if (rs.next())
		{
			if (rs.getInt("numResultados")<1)
			{
				rs.close();
				return false;
			}
			else
			{
				rs.close();
				return true;
			}
		}
		else
		{
			rs.close();
			throw new SQLException ("Error en un count en tieneCamaParaEgresoUrgencias");
		}
	}

	/**
	 * Método que implementa la validación de si se puede reversar
	 * un egreso de urgencias para una BD Genérica.
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#puedoReversarEgresoUrgencias(Connection , int , int ) throws SQLException
	 */
	public static RespuestaValidacionTratante puedoReversarEgresoUrgencias(Connection ac_con, int ai_idCuenta, int ai_codigoCentroCosto) throws SQLException
	{
		boolean		lb_terminoEgreso;
		ResultSetDecorator	lrs_rs;
		String		ls_admisionUrgencias;
		String		ls_informacionGeneral;
		String		ls_numIdMedico;
		String		ls_usuarioResponsable;

		ls_admisionUrgencias =	"SELECT " 	+	"COUNT(1) as numResultados "	+
								"FROM " 	+	"admisiones_urgencias "			+
								"WHERE "	+	"cuenta=?";

		ls_numIdMedico			= "";
		PreparedStatementDecorator ls_admisionUrgenciasStatement=new PreparedStatementDecorator(ac_con.prepareStatement(ls_admisionUrgencias,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ls_admisionUrgenciasStatement.setInt(1, ai_idCuenta);

		lrs_rs					= new ResultSetDecorator(ls_admisionUrgenciasStatement.executeQuery());

		if(lrs_rs.next())
		{
			if(lrs_rs.getInt("numResultados") < 1)
			{
				lrs_rs.close();
				return new	RespuestaValidacionTratante
							(
								"No se puede hacer reversión de egreso, ya que la cuenta especificada no es de paciente de urgencias",  false, false
							);
			}

			//No se hace un else porque en ese caso se debe continuar el flujo de las //validaciones
		}
		else
		{
			lrs_rs.close();
			throw new SQLException ("Error en un count en puedoReversarEgresoUrgencias");
		}

		lrs_rs.close();
		lrs_rs = null;


		ls_informacionGeneral = "SELECT codigo_medico as codigoMedico, usuario_responsable as usuarioResponsable from egresos where cuenta=?";
		PreparedStatementDecorator ls_informacionGeneralStatement =new PreparedStatementDecorator(ac_con.prepareStatement(ls_informacionGeneral ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ls_informacionGeneralStatement.setInt(1, ai_idCuenta);
		lrs_rs=new ResultSetDecorator(ls_informacionGeneralStatement.executeQuery());
		if (lrs_rs.next())
		{
			ls_numIdMedico = lrs_rs.getString("codigoMedico");
			if(UtilidadTexto.isEmpty(ls_numIdMedico))
			{
				return new	RespuestaValidacionTratante("El paciente esta en medio de una reversión de egreso", false, false);
			}
			ls_usuarioResponsable	= lrs_rs.getString("usuarioResponsable");
			lb_terminoEgreso		= !UtilidadTexto.isEmpty(ls_usuarioResponsable);
			if(lb_terminoEgreso)
			{
				/*
					El paciente terminó todo el egreso. Cualquier usuario con
					acceso a esta funcionalidad puede reversar el egreso
				*/
				lrs_rs.close();
				return new RespuestaValidacionTratante("No hay ningún problema para hacer la reversión", true, true);
			}
			else
			{

				/* El paciente no terminó el egreso y solo tiene orden de salida */

				/* El médico es tratante y puede reversar el egreso */
				if(ConstantesBD.codigoCentroCostoUrgencias == ai_codigoCentroCosto)
				{
					lrs_rs.close();
					return new RespuestaValidacionTratante("No hay ningún problema para hacer la reversión", true, false);
				}
				/* El médico no es tratante y no puede reversar el egreso */
				else
				{
					lrs_rs.close();
					return
						new RespuestaValidacionTratante
						(
							"Solo un médico del centro de costo de urgencias puede reversar el egreso",
							false,
							false
						);
				}
			}
		}
		else
		{
			lrs_rs.close();
			return
				new RespuestaValidacionTratante
				(
					"No se puede hacer reversión de egreso, pues no existe ningún egreso para esta cuenta",
					false,
					false
				);
		}
	}

	/**
	 * Método implementa la búsqueda del nombre de una
	 * persona para una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#obtenerNombrePersona (Connection , int ) throws SQLException
	 */
	public static String obtenerNombrePersona (Connection con, int codigoPersona) 
	{
		try
		{
			String tieneValoracionUrgenciasSemiEgresoStr= "SELECT coalesce(primer_nombre,'') || ' ' ||  coalesce(segundo_nombre,'') || ' ' || coalesce(primer_apellido,'') || ' ' || coalesce(segundo_apellido,'') as nombre from personas where codigo=?";
			logger.info("\n  obtenerNombrePersona consulta --> "+tieneValoracionUrgenciasSemiEgresoStr);
			PreparedStatementDecorator tieneValoracionUrgenciasSemiEgresoStatement= new PreparedStatementDecorator(con.prepareStatement(tieneValoracionUrgenciasSemiEgresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			tieneValoracionUrgenciasSemiEgresoStatement.setInt(1, codigoPersona);
			ResultSetDecorator rs=new ResultSetDecorator(tieneValoracionUrgenciasSemiEgresoStatement.executeQuery());
			if (rs.next())
				return rs.getString("nombre");
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerNombrePersona: "+e);
			
		}
		return "Persona inexistente en el sistema";
	}
	
	//***********************************************************************************************************************

	/**
	 * Método que implementa la revisión de la capacidad
	 * de crear una cuenta en asocio para una BD Genérica 
	 * @param Connection con
	 * @param int idIngreso
	 */
	public static boolean puedoCrearCuentaAsocio (Connection con, int idIngreso) throws SQLException
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try{
			String strExistenCuentasPendientesXAsocio = 
					"SELECT COUNT(codigo) AS cantidad " +
					"FROM manejopaciente.asocios_cuenta " +
					"WHERE ingreso = ? AND cuenta_final IS NULL AND activo = "+ValoresPorDefecto.getValorTrueParaConsultas();
			
			pst= con.prepareStatement(strExistenCuentasPendientesXAsocio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, idIngreso);		
			rs=pst.executeQuery();
			
			if (rs.next()){
				if (rs.getInt("cantidad")<1){				
					return false;
				}
				else{				
					return true;
				}
			}
			else{
				return false;
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR puedoCrearCuentaAsocio",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR puedoCrearCuentaAsocio", e);
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
		return false;
	}
	
	//***********************************************************************************************************************

	/**
	 * Método que revisa el estado de la cuenta (si está en facturación)
	 * para una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionCuentaFacturada(Connection , int , boolean ) throws SQLException
	 */
	public static boolean validacionCuentaFacturada(Connection con, int idIngreso, boolean esHospitalizacion) throws SQLException
	{
		String validacionCuentaFacturadaStr;

		if (esHospitalizacion)
		{
			validacionCuentaFacturadaStr="SELECT cue.estado_cuenta from cuentas cue, admisiones_hospi adh where cue.id=adh.cuenta and cue.id_ingreso=?" ;
		}
		else
		{
			validacionCuentaFacturadaStr="SELECT cue.estado_cuenta from cuentas cue, admisiones_urgencias adu where cue.id=adu.cuenta and cue.id_ingreso=?";
		}

		PreparedStatementDecorator validacionCuentaFacturadaStatement= new PreparedStatementDecorator(con.prepareStatement(validacionCuentaFacturadaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		validacionCuentaFacturadaStatement.setInt(1, idIngreso);
		ResultSetDecorator rs=new ResultSetDecorator(validacionCuentaFacturadaStatement.executeQuery());

		if (rs.next())
		{
			if (rs.getInt("estado_cuenta")==1)
			{
				rs.close();
				return true;
			}
			else
			{
				rs.close();
				return false;
			}
		}
		else
		{
			rs.close();
			throw new SQLException ("No se encontró cuenta en validacionCuentaFacturada");
		}
	}

	/**
	 * Método que implementa la revisión de existencia de un centro
	 * de costo para una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeCentroCostoValido (Connection ) throws SQLException
	 */
	public static RespuestaValidacion existeCentroCostoValido (Connection con) throws SQLException
	{
		String existeCentroCostoValidoStr="SELECT min(codigo) as codigoCentroCosto from centros_costo";
		PreparedStatementDecorator existeCentroCostoValidoStatement= new PreparedStatementDecorator(con.prepareStatement(existeCentroCostoValidoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		ResultSetDecorator rs=new ResultSetDecorator(existeCentroCostoValidoStatement.executeQuery());
		if (rs.next())
		{
			rs.close();
			return new RespuestaValidacion (rs.getString("codigoCentroCosto"), true);
		}
		else
		{
			rs.close();
			throw new SQLException ("No hay centro de costo válido en existeCentroCostoValido");
		}
	}

	/**
	 * Método que implementa la revisión de si un usuario es
	 * paciente para una institución dada en una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#esUsuarioPaciente (Connection , int , String ) throws SQLException
	 */
	public static ResultSetDecorator esUsuarioPaciente (Connection con, int codigoUsuario, String codigoInstitucion) throws SQLException
	{
		String esUsuarioPacienteStr="SELECT count(1) as numResultados from pacientes_instituciones where codigo_paciente=? and codigo_institucion=?";
		PreparedStatementDecorator esUsuarioPacienteStatement= new PreparedStatementDecorator(con.prepareStatement(esUsuarioPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		esUsuarioPacienteStatement.setInt(1, codigoUsuario);
		esUsuarioPacienteStatement.setString(2, codigoInstitucion);
		return new ResultSetDecorator(esUsuarioPacienteStatement.executeQuery());
	}

	
	/**
	 * Metodo para saber si un usuario esta activo en el sistema.
	 * @param con
	 * @param codigoPersona
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator esUsuarioActivo (Connection con, String login, int codigoInstitucion) throws SQLException
	{
		String esUsuarioActivoStr="SELECT count(1) as numResultados from usuarios_inactivos where login=? and codigo_institucion=?";
		PreparedStatementDecorator esUsuarioActivoStatement= new PreparedStatementDecorator(con.prepareStatement(esUsuarioActivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		esUsuarioActivoStatement.setString(1, login);
		esUsuarioActivoStatement.setInt(2, codigoInstitucion);
		return new ResultSetDecorator(esUsuarioActivoStatement.executeQuery());
	}
	

	/**
	 * Método que implementa la revisión de si una cuenta
	 * tiene semi-egreso para una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#tieneSemiEgreso (Connection , int ) throws SQLException
	 */
	public static ResultSetDecorator tieneSemiEgreso (Connection con, int idCuenta) throws SQLException
	{
		String tieneValoracionUrgenciasSemiEgresoStr="SELECT usuario_responsable as usuarioResponsable from egresos where evolucion is null and cuenta=?" ;
		PreparedStatementDecorator tieneValoracionUrgenciasSemiEgresoStatement= new PreparedStatementDecorator(con.prepareStatement(tieneValoracionUrgenciasSemiEgresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		tieneValoracionUrgenciasSemiEgresoStatement.setInt(1, idCuenta);
		return new ResultSetDecorator(tieneValoracionUrgenciasSemiEgresoStatement.executeQuery());
	}

	/**
	 * Método que implementa la revisión de si un centro de costo
	 * es adjunto en cuenta para una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#esAdjuntoCuenta (Connection , int , String ) throws SQLException
	 */
	public static boolean esAdjuntoCuenta (Connection con, int idCuenta, String loginUsuario) throws SQLException
	{
		String esAdjuntoCuentaStr="SELECT count(1) as numResultados FROM adjuntos_cuenta adc where adc.cuenta=? and adc.centro_costo IN(SELECT centro_costo FROM centros_costo_usuario WHERE usuario=?) and (fecha_inicio<CURRENT_DATE or (fecha_inicio=CURRENT_DATE and hora_inicio<='"+UtilidadFecha.getHoraActual()+"')) and (fecha_fin IS NULL or fecha_fin>CURRENT_DATE or (fecha_fin=CURRENT_DATE and hora_fin>='"+UtilidadFecha.getHoraActual()+"') )";
		
		PreparedStatementDecorator esAdjuntoCuentaStatement= new PreparedStatementDecorator(con.prepareStatement(esAdjuntoCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		esAdjuntoCuentaStatement.setInt(1, idCuenta);
		esAdjuntoCuentaStatement.setString(2, loginUsuario);
		ResultSetDecorator rs=new ResultSetDecorator( esAdjuntoCuentaStatement.executeQuery());
		if (rs.next())
		{
			if (rs.getInt("numResultados")>0)
			{
				rs.close();
				return true;
			}
			else
			{
				rs.close();
				return false;
			}
		}
		else
		{
			throw new SQLException ("Error en un count en esAdjuntoCuenta");
		}
	}

	/**
	 * Método que implementa la revisión de si para cuenta existen
	 * solicitudes de transferencia de manejo para una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeSolicitudTransferenciaManejoPrevia(Connection , int ) throws SQLException
	 */
	public static boolean existeSolicitudTransferenciaManejoPrevia(Connection con, int idCuenta) throws SQLException
	{
		String existeSolicitudTransferenciaManejoPreviaStr="SELECT count(1) as numResultados from sol_cambio_tratante ct INNER JOIN solicitudes s ON(ct.solicitud=s.numero_solicitud)where ct.cuenta=? and ct.activa=" + ValoresPorDefecto.getValorTrueParaConsultas() + " and s.estado_historia_clinica<>"+ConstantesBD.codigoEstadoHCAnulada;
		PreparedStatementDecorator existeSolicitudTransferenciaManejoPreviaStatement= new PreparedStatementDecorator(con.prepareStatement(existeSolicitudTransferenciaManejoPreviaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		existeSolicitudTransferenciaManejoPreviaStatement.setInt(1, idCuenta);
		ResultSetDecorator rs=new ResultSetDecorator(existeSolicitudTransferenciaManejoPreviaStatement.executeQuery());
		if (rs.next())
		{
			if (rs.getInt("numResultados")>0)
			{
				rs.close();
				return true;
			}
			else
			{
				rs.close();
				return false;
			}
		}
		else
		{
			throw new SQLException ("Error en un count en existeSolicitudTransferenciaManejoPrevia");
		}
	}

	/**
	 * Método que implementa la revisión de si una cuenta tiene como
	 * vía de ingreso consulta externa para una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#esViaIngresoConsultaExterna (Connection , int ) throws SQLException
	 */
	public static boolean esViaIngresoConsultaExterna (Connection con, int idCuenta) throws SQLException
	{
		String esViaIngresoConsultaExternaStr="SELECT count(1) as numResultados from cuentas cue where cue.id=? and cue.via_ingreso=?";
		PreparedStatementDecorator esViaIngresoConsultaExternaStatement= new PreparedStatementDecorator(con.prepareStatement(esViaIngresoConsultaExternaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		esViaIngresoConsultaExternaStatement.setInt(1, idCuenta);
		esViaIngresoConsultaExternaStatement.setInt(2, ConstantesBD.codigoViaIngresoConsultaExterna);

		ResultSetDecorator rs=new ResultSetDecorator(esViaIngresoConsultaExternaStatement.executeQuery());

		if (rs.next())
		{
			if (rs.getInt("numResultados")>0)
			{
				rs.close();
				return true;
			}
			else
			{
				rs.close();
				return false;
			}
		}
		else
		{
			throw new SQLException ("Error en un count en esViaIngresoConsultaExterna ");
		}
	}

	/**
	 * Método que revisa si hay solicitudes incompletas para
	 * un adjunto en una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#haySolicitudesIncompletasAdjunto (Connection , int , int ) throws SQLException
	 */
	public static ResultadoBoolean haySolicitudesIncompletasAdjunto (Connection con, int idCuenta, int codigoCentroCosto, int codigoCuentaAsociada, int institucion) throws SQLException
	{
		ResultadoBoolean respuesta;
		respuesta=new ResultadoBoolean (false);
		respuesta.setDescripcion("");

		String haySolicitudesIncompletasAdjuntoStr="";
		boolean validarInterpretadas=UtilidadTexto.getBoolean(util.ValoresPorDefecto.getValidarEstadoSolicitudesInterpretadas(institucion));
		PreparedStatementDecorator haySolicitudesIncompletasAdjuntoStatement;
		if(validarInterpretadas)
		{
			//haySolicitudesIncompletasAdjuntoStr="SELECT count(1) as numResultados from solicitudes where estado_historia_clinica !=? and estado_historia_clinica!=? and estado_historia_clinica!=? and (cuenta=? or cuenta=?) and centro_costo_solicitante=?";
			
			haySolicitudesIncompletasAdjuntoStr="SELECT sum(tabla.numeroResultados) as numResultados FROM( " +
																						//primer select <> a respondidas
																						"SELECT " +
																							"count(1) as numeroResultados " +
																						"from " +
																							"solicitudes " +
																						"where " +
																							"estado_historia_clinica not in("+ConstantesBD.codigoEstadoHCAnulada+","+ConstantesBD.codigoEstadoHCInterpretada+", "+ConstantesBD.codigoEstadoHCAdministrada+", "+ConstantesBD.codigoEstadoHCRespondida+") " +
																							" and cuenta in(?,?) and centro_costo_solicitante=? " +
																						"union all " +
																						//segundo select = a respondidas pero que requieren interpretacion
																						"SELECT " +
																						"count(1) as numeroResultados " +
																						"from " +
																							"solicitudes " +
																						"where " +
																							"estado_historia_clinica ="+ConstantesBD.codigoEstadoHCRespondida+" " +
																							" and cuenta in(?,?) and centro_costo_solicitante=? " +
																							" and getservrequiereinterpretacion(getserviciosolicitud(numero_solicitud,tipo))='"+ConstantesBD.acronimoSi+"' " +
																				")tabla";
			
			logger.info("\n\n\n haySolicitudesIncompletasAdjuntoStr->"+haySolicitudesIncompletasAdjuntoStr+" ->idcuenta->"+idCuenta+" asocio->"+codigoCuentaAsociada+" centrocosto->"+codigoCentroCosto);
			
			haySolicitudesIncompletasAdjuntoStatement= new PreparedStatementDecorator(con.prepareStatement(haySolicitudesIncompletasAdjuntoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			haySolicitudesIncompletasAdjuntoStatement.setInt(1, idCuenta);
			haySolicitudesIncompletasAdjuntoStatement.setInt(2, codigoCuentaAsociada);
			haySolicitudesIncompletasAdjuntoStatement.setInt(3, codigoCentroCosto);
			haySolicitudesIncompletasAdjuntoStatement.setInt(4, idCuenta);
			haySolicitudesIncompletasAdjuntoStatement.setInt(5, codigoCuentaAsociada);
			haySolicitudesIncompletasAdjuntoStatement.setInt(6, codigoCentroCosto);
		}
		else
		{
			haySolicitudesIncompletasAdjuntoStr="SELECT count(1) as numResultados from solicitudes where estado_historia_clinica !=? and estado_historia_clinica!=? and estado_historia_clinica!=? and estado_historia_clinica!=? and (cuenta=? or cuenta=?) and centro_costo_solicitante=?";
			haySolicitudesIncompletasAdjuntoStatement= new PreparedStatementDecorator(con.prepareStatement(haySolicitudesIncompletasAdjuntoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			haySolicitudesIncompletasAdjuntoStatement.setInt(1, ConstantesBD.codigoEstadoHCAnulada);
			haySolicitudesIncompletasAdjuntoStatement.setInt(2, ConstantesBD.codigoEstadoHCInterpretada);
			haySolicitudesIncompletasAdjuntoStatement.setInt(3, ConstantesBD.codigoEstadoHCAdministrada);
			haySolicitudesIncompletasAdjuntoStatement.setInt(4, ConstantesBD.codigoEstadoHCRespondida);
			haySolicitudesIncompletasAdjuntoStatement.setInt(5, idCuenta);
			haySolicitudesIncompletasAdjuntoStatement.setInt(6, codigoCuentaAsociada);
			haySolicitudesIncompletasAdjuntoStatement.setInt(7, codigoCentroCosto);
		}

		ResultSetDecorator rs=new ResultSetDecorator(haySolicitudesIncompletasAdjuntoStatement.executeQuery());
		if (rs.next())
		{
			if (rs.getInt("numResultados")>0)
			{
				respuesta.setResultado(true);
				if(validarInterpretadas)
				{
					respuesta.setDescripcion("El grupo adjunto aún tiene solicitudes pendientes (Todas deben estar interpretadas, administradas y/o anuladas)");
				}
				else
				{
					respuesta.setDescripcion("El grupo adjunto aún tiene solicitudes pendientes (Todas deben estar respondidas, administradas y/o anuladas)");
				}
			}
			else
			{
				respuesta.setResultado(false);
				if(validarInterpretadas)
				{
					respuesta.setDescripcion("No hay ninguna solicitud en estado diferente a interpretada ,administrada y/o anulada");
				}
				else
				{
					respuesta.setDescripcion("No hay ninguna solicitud en estado diferente a respondida ,administrada y/o anulada");
				}
			}
			return respuesta;
		}
		else
		{
			throw new SQLException ("Error en un count en haySolicitudesIncompletasAdjunto");
		}
	}

	/**
	 * Método que revisa si hay solicitudes incompletas para
	 * una cuenta en una BD Genérica
	 * @throws BDException 
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#haySolicitudesIncompletasEnCuenta (Connection , int ) throws SQLException
	 */
	public static ResultadoBoolean haySolicitudesIncompletasEnCuenta (Connection con, int codigoIngreso, int institucion, boolean esEvolucion, boolean validarMedicamentos) throws SQLException, BDException
	{
		ResultadoBoolean respuesta;
		respuesta=new ResultadoBoolean (false);
		respuesta.setDescripcion("");
		int numSolEnProcesoSolicitadas = 0;
		int numSolIncompletas = 0;
		boolean incompletas = false; //variable que da un definitivo para indicar si hay solicitudes incomplewtas
		ResultSetDecorator rs0 = null;

		String haySolicitudesIncompletasEnCuentaStr="";
		PreparedStatementDecorator haySolicitudesIncompletasEnCuentaStatement;
		boolean validarInterpretadas=UtilidadTexto.getBoolean(util.ValoresPorDefecto.getValidarEstadoSolicitudesInterpretadas(institucion));
		if(validarInterpretadas)
		{

			String restriccionSolMedEvol="";
			//si no se tiene que validar, se debe sacaar el tipo medicamentos en la consulta.
			if(!validarMedicamentos)
				restriccionSolMedEvol=" AND s.tipo<>"+ConstantesBD.codigoTipoSolicitudMedicamentos;

			haySolicitudesIncompletasEnCuentaStr="SELECT sum(tabla.numeroResultados) as numResultados from (" +
																			//primer select <> a respondidas
																			"SELECT " +
																				"count(1) as numeroResultados " +
																			"from cuentas c " +
																			"INNER JOIN solicitudes s ON(s.cuenta=c.id) " +
																			"where " +
																				" c.id_ingreso = ? and s.estado_historia_clinica NOT IN(?,?,?,?,?)   " +restriccionSolMedEvol+
																			"union all " +
																			//segundo select = a respondidas pero que requieren interpretacion
																			"SELECT " +
																				"count(1) as numeroResultados " +
																			"FROM cuentas c " +
																				"INNER JOIN solicitudes s ON(s.cuenta=c.id) " +
																			"WHERE " +
																				"c.id_ingreso = ? " +
																				"and s.estado_historia_clinica = "+ConstantesBD.codigoEstadoHCRespondida+" " +
																				"and getservrequiereinterpretacion(getserviciosolicitud(s.numero_solicitud,s.tipo))='"+ConstantesBD.acronimoSi+"' " +
																				" " +restriccionSolMedEvol+
																		")tabla ";
			
			logger.info("\n\n\n SOLICITUDES INCOMPLETAS->"+haySolicitudesIncompletasEnCuentaStr+" ->("+ConstantesBD.codigoEstadoHCAnulada+","+ConstantesBD.codigoEstadoHCInterpretada+","+ ConstantesBD.codigoEstadoHCAdministrada+","+ConstantesBD.codigoEstadoHCRespondida+","+ConstantesBD.codigoEstadoHCCargoDirecto+") codigoIngreso->"+codigoIngreso+"\n\n\n");
			
			//haySolicitudesIncompletasEnCuentaStr="SELECT count(1) as numResultados from solicitudes where estado_historia_clinica NOT IN (?,?,?,?)  and cuenta in (?,?) ";
			
			haySolicitudesIncompletasEnCuentaStatement= new PreparedStatementDecorator(con.prepareStatement(haySolicitudesIncompletasEnCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			haySolicitudesIncompletasEnCuentaStatement.setInt(1, codigoIngreso);
			haySolicitudesIncompletasEnCuentaStatement.setInt(2, ConstantesBD.codigoEstadoHCAnulada);
			haySolicitudesIncompletasEnCuentaStatement.setInt(3, ConstantesBD.codigoEstadoHCInterpretada);
			haySolicitudesIncompletasEnCuentaStatement.setInt(4, ConstantesBD.codigoEstadoHCAdministrada);
			haySolicitudesIncompletasEnCuentaStatement.setInt(5, ConstantesBD.codigoEstadoHCRespondida);
			haySolicitudesIncompletasEnCuentaStatement.setInt(6, ConstantesBD.codigoEstadoHCCargoDirecto);
			
			haySolicitudesIncompletasEnCuentaStatement.setInt(7, codigoIngreso);
		}
		else
		{
			String restriccionSolMedEvol="";
			if(!validarMedicamentos)
				restriccionSolMedEvol=" AND tipo<>"+ConstantesBD.codigoTipoSolicitudMedicamentos;
			

			haySolicitudesIncompletasEnCuentaStr="SELECT count(1) as numResultados from cuentas c inner join solicitudes s ON(s.cuenta=c.id) where c.id_ingreso = ? and s.estado_historia_clinica NOT IN(?,?,?,?,?) "+restriccionSolMedEvol;
			haySolicitudesIncompletasEnCuentaStatement= new PreparedStatementDecorator(con.prepareStatement(haySolicitudesIncompletasEnCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			haySolicitudesIncompletasEnCuentaStatement.setInt(1, codigoIngreso);
			haySolicitudesIncompletasEnCuentaStatement.setInt(2, ConstantesBD.codigoEstadoHCAnulada);
			haySolicitudesIncompletasEnCuentaStatement.setInt(3, ConstantesBD.codigoEstadoHCInterpretada);
			haySolicitudesIncompletasEnCuentaStatement.setInt(4, ConstantesBD.codigoEstadoHCAdministrada);
			haySolicitudesIncompletasEnCuentaStatement.setInt(5, ConstantesBD.codigoEstadoHCRespondida);
			haySolicitudesIncompletasEnCuentaStatement.setInt(6, ConstantesBD.codigoEstadoHCCargoDirecto);
		}
		ResultSetDecorator rs=new ResultSetDecorator(haySolicitudesIncompletasEnCuentaStatement.executeQuery());
		if (rs.next())
		{
			numSolIncompletas = rs.getInt("numResultados");
			if (numSolIncompletas>0)
			{
				//**********+SE VERIFICA SI HAY SOLICITUDES EN PROCESO Y SOLICITADAS********************+
				haySolicitudesIncompletasEnCuentaStr="SELECT sum(tabla.numeroResultados) as numResultados FROM (" +
																				"SELECT " +
																					"count(1) as numeroResultados " +
																				"from cuentas c" +
																					" inner join solicitudes s ON(s.cuenta=c.id) " +
																				"where " +
																					"c.id_ingreso = ? " +
																					"and s.estado_historia_clinica in (?,?,?)  " +
																					" and tipo ="+ConstantesBD.codigoTipoSolicitudProcedimiento+" ";
				
				if(validarInterpretadas)
				{
					haySolicitudesIncompletasEnCuentaStr+=
																				" union all " +
																				"SELECT " +
																					"count(1) as numeroResultados " +
																				"from cuentas c " +
																					"inner join solicitudes s ON(s.cuenta=c.id) " +
																				"where " +
																					"c.id_ingreso = "+codigoIngreso+" " +
																					"and s.estado_historia_clinica = "+ConstantesBD.codigoEstadoHCRespondida+"  " +//estados
																					"and tipo ="+ConstantesBD.codigoTipoSolicitudProcedimiento+" " +
																					"and getservrequiereinterpretacion(getserviciosolicitud(numero_solicitud,tipo))='"+ConstantesBD.acronimoSi+"' ";
				}																
				
				haySolicitudesIncompletasEnCuentaStr+= ")tabla ";	
				
				logger.info("\n\n\n SOLICITUDES EN PROCESO --> "+haySolicitudesIncompletasEnCuentaStr+" ("+ConstantesBD.codigoEstadoHCEnProceso+","+ConstantesBD.codigoEstadoHCSolicitada+","+ConstantesBD.codigoEstadoHCTomaDeMuestra+") codigoIngreso->"+codigoIngreso);
				
				haySolicitudesIncompletasEnCuentaStatement= new PreparedStatementDecorator(con.prepareStatement(haySolicitudesIncompletasEnCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				haySolicitudesIncompletasEnCuentaStatement.setInt(1, codigoIngreso);
				haySolicitudesIncompletasEnCuentaStatement.setInt(2, ConstantesBD.codigoEstadoHCEnProceso);
				haySolicitudesIncompletasEnCuentaStatement.setInt(3, ConstantesBD.codigoEstadoHCSolicitada);
				haySolicitudesIncompletasEnCuentaStatement.setInt(4, ConstantesBD.codigoEstadoHCTomaDeMuestra);
				
				rs0 = new ResultSetDecorator(haySolicitudesIncompletasEnCuentaStatement.executeQuery());
				if(rs0.next())
					numSolEnProcesoSolicitadas = rs0.getInt("numResultados");
				
				logger.info("numSolIncompletas=>"+numSolIncompletas+", numSolEnProcesoSolicitadas=> "+numSolEnProcesoSolicitadas);
				
				//87501
				//se verifica si las solicitudes incompletas son las que están en proceso
				if(numSolIncompletas==numSolEnProcesoSolicitadas)
					incompletas = verificarSolicitudesEnProcesoIncompletas(con,codigoIngreso,institucion,validarInterpretadas);
				else
					incompletas = true;
				///*****************************************************************************************************
				
				if(incompletas)
				{
				
					respuesta.setResultado(true);
					if(validarInterpretadas)
					{
						respuesta.setDescripcion("Esta cuenta aún tiene solicitudes pendientes (Todas deben estar interpretadas, administradas y/o anuladas), excepto para pacientes de urgencias que se van a hospitalizar");
					}
					else
					{
						respuesta.setDescripcion("Esta cuenta aún tiene solicitudes pendientes (Todas deben estar respondidas, administradas y/o anuladas), excepto para pacientes de urgencias que se van a hospitalizar");
					}
				}
				else
				{
					respuesta.setResultado(false);
					if(validarInterpretadas)
					{
						respuesta.setDescripcion("No hay ninguna solicitud en estado diferente a interpretada, administrada y/o anulada");
					}
					else
					{
						respuesta.setDescripcion("No hay ninguna solicitud en estado diferente a respondida, administrada y/o anulada");
					}
				}
			}
			else
			{
				respuesta.setResultado(false);
				if(validarInterpretadas)
				{
					respuesta.setDescripcion("No hay ninguna solicitud en estado diferente a interpretada, administrada y/o anulada");
				}
				else
				{
					respuesta.setDescripcion("No hay ninguna solicitud en estado diferente a respondida, administrada y/o anulada");
				}
			}
			return respuesta;
		}
		else
		{
			throw new SQLException ("Error en un count en haySolicitudesIncompletasEnCuenta");
		}
	}

	/**
	 * Método que verifica si existen solicitudes en proceso incompletas
	 * @param con
	 * @param idCuenta
	 * @param codigoCuentaAsociada
	 * @param institucion 
	 * @param validarInterpretadas 
	 * @return
	 * @throws BDException 
	 */
	private static boolean verificarSolicitudesEnProcesoIncompletas(Connection con, int codigoIngreso, int institucion, boolean validarInterpretadas) throws SQLException, BDException
	{
		boolean incompletas = false;
		int estadoAdicional = validarInterpretadas?ConstantesBD.codigoEstadoHCRespondida:0;
		
		logger.info("********* 0: " + validarInterpretadas);
		
		try
		{
			String consulta = "SELECT "+ 
				"c.via_ingreso AS via_ingreso, "+ 
				"sp.codigo_servicio_solicitado AS codigo_servicio, "+
				"s.estado_historia_clinica AS estado, " +
				"getservrequiereinterpretacion(getserviciosolicitud(s.numero_solicitud,s.tipo)) as req_int "+
				"FROM cuentas c "+  
				"INNER JOIN solicitudes s ON(s.cuenta=c.id) "+ 
				"INNER JOIN sol_procedimientos sp ON(sp.numero_solicitud=s.numero_solicitud) "+ 
				"WHERE "+ 
				"c.id_ingreso = ? AND " +
				"s.estado_historia_clinica IN ("+ConstantesBD.codigoEstadoHCEnProceso+","+
					ConstantesBD.codigoEstadoHCSolicitada+","+
					ConstantesBD.codigoEstadoHCTomaDeMuestra+") ";
			
			if(validarInterpretadas)
			{
				consulta+="UNION SELECT "+ 
							"c.via_ingreso AS via_ingreso, "+ 
							"sp.codigo_servicio_solicitado AS codigo_servicio, "+
							"s.estado_historia_clinica AS estado, " +
							"getservrequiereinterpretacion(getserviciosolicitud(s.numero_solicitud,s.tipo)) as req_int "+
							"FROM cuentas c "+  
							"INNER JOIN solicitudes s ON(s.cuenta=c.id) "+ 
							"INNER JOIN sol_procedimientos sp ON(sp.numero_solicitud=s.numero_solicitud) "+ 
							"WHERE "+ 
							"c.id_ingreso = "+codigoIngreso+" AND " +
							"s.estado_historia_clinica ="+ConstantesBD.codigoEstadoHCRespondida+" "+ 
							"and getservrequiereinterpretacion(getserviciosolicitud(s.numero_solicitud,s.tipo))='"+ConstantesBD.acronimoSi+"' ";
			}
			logger.info("\n\n\n verificarSolicitudesEnProcesoIncompletas-> "+consulta+" codigoIngreso->"+codigoIngreso+" \n\n\n");
			
			//(validarInterpretadas?ConstantesBD.codigoEstadoHCRespondida:0)+")";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoIngreso);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				boolean esCargo = false;
				//87501
				//Se revisa primero el cargo solicitud
				if(
					((rs.getInt("estado") == ConstantesBD.codigoEstadoHCSolicitada) ||
					(rs.getInt("estado") == ConstantesBD.codigoEstadoHCTomaDeMuestra) ||
					(rs.getInt("estado") == estadoAdicional)  ||
					(rs.getInt("estado") == ConstantesBD.codigoEstadoHCEnProceso)) 
					&&
					(!UtilidadTexto.getBoolean(rs.getString("req_int"))) 
				)
					
					
					
				{
					esCargo = esServicioViaIngresoCargoSolicitud(con,rs.getString("via_ingreso"),rs.getString("codigo_servicio"),institucion+"");
					logger.info("*********** 1 " + esCargo);
				}
				
				//Se revisa luego el cargo proceso si

				/*if (!esCargo && 
					(rs.getInt("estado")==ConstantesBD.codigoEstadoHCEnProceso||rs.getInt("estado")==estadoAdicional) &&
					(!UtilidadTexto.getBoolean("req_int"))
				)*/
				
				if ( !esCargo &&
					((rs.getInt("estado") == ConstantesBD.codigoEstadoHCEnProceso) ||
					(rs.getInt("estado") == estadoAdicional)) &&
					(!UtilidadTexto.getBoolean(rs.getString("req_int")))
				)
				{
					esCargo = esServicioViaIngresoCargoProceso(con,rs.getString("via_ingreso"),rs.getString("codigo_servicio"),institucion+"");
					logger.info("********* 2 " + esCargo);
					
				}
				logger.info("*********** 3 " + esCargo);
				
				if(!esCargo)
					incompletas = true;

				logger.info("********* Inco: " + incompletas);
				
			}
		}
		catch(SQLException e)
		{
			incompletas = false;
			logger.error("Error en verificarSolicitudesEnProcesoIncompletas de SQlBaseUtilidadValidacionDao: "+e);
		}
		return incompletas;
	}

	/**
	 * Método que revisa si un centro de costo tiene una solicitud
	 * de cambio de manejo tratante para la cuenta dada en una BD
	 * Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeSolicitudTransferenciaManejoPreviaDadoCentroCosto (Connection con, int idCuenta, int codigoCentroCosto) throws SQLException
	 */
	public static int existeSolicitudTransferenciaManejoPreviaDadoCentroCosto (Connection con, int idCuenta, int codigoCentroCosto) throws SQLException
	{
		String haySolicitudCambioTratanteStr="SELECT sol.numero_solicitud as numeroSolicitud from sol_cambio_tratante solcambt, solicitudes sol where solcambt.cuenta=? and (sol.centro_costo_solicitado=? or sol.centro_costo_solicitado=" + ConstantesBD.codigoCentroCostoTodos +") and activa=" + ValoresPorDefecto.getValorTrueParaConsultas() + " and solcambt.solicitud=sol.numero_solicitud AND sol.estado_historia_clinica<>"+ConstantesBD.codigoEstadoHCAnulada;
		PreparedStatementDecorator haySolicitudCambioTratanteStatement= new PreparedStatementDecorator(con.prepareStatement(haySolicitudCambioTratanteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		haySolicitudCambioTratanteStatement.setInt(1, idCuenta);
		haySolicitudCambioTratanteStatement.setInt(2, codigoCentroCosto);
		ResultSetDecorator rs=new ResultSetDecorator(haySolicitudCambioTratanteStatement.executeQuery());

		if (rs.next())
		{
			return rs.getInt("numeroSolicitud");
		}
		else
		{
			return 0;
		}
	}

	/**
	* Indica si un servicio es excepcion al convenio de la cuenta.
	* @param	ac_con		Conexión a una fuente de datos
	* @param	ai_cuenta	Codigo de la cuenta
	* @param	ai_servicio	Código del servicio
	* @return	true si el servicio esta paramatrizado como una excepción al convenio de la cuenta.
	*			false de lo contrario
	* @throws	SQLException
	*/
	public static boolean esExcepcionServicio(
		Connection	ac_con,
		int			ai_cuenta,
		int			ai_servicio
	)throws SQLException
	{
		boolean				lb_abrirConexion;
		boolean				lb_resp;
		PreparedStatementDecorator	lps_ps;
		ResultSetDecorator			lrs_rs;

		final String ls_consulta  =
			"SELECT ( case when COUNT(*)=1 then '"+ValoresPorDefecto.getValorTrueParaConsultas()+"' else '"+ValoresPorDefecto.getValorFalseParaConsultas()+"' end) AS esExcepcion "+ 
			"FROM cuentas c "+ 
			"INNER JOIN sub_cuentas sc ON(sc.ingreso=c.id_ingreso AND sc.nro_prioridad = 1) "+ 
			"INNER JOIN contratos co ON(co.convenio=sc.convenio) "+ 
			"INNER JOIN excepciones_servicios es ON(es.contrato=co.codigo) "+ 
			"WHERE c.id=? AND es.servicio =?";

		/* Verificar el estado de la conexión y abrir una nueva si es necesario */
		lb_abrirConexion = (ac_con == null || ac_con.isClosed() );
		if(lb_abrirConexion )
			ac_con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getConnection();

		/* Preparar la consulta sobre las excepciones de servicios */
		lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(ls_consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		/* Establecer los atributos de la búsqueda */
		lps_ps.setInt(1, ai_cuenta);
		lps_ps.setInt(2, ai_servicio);
		/* Obtener la cuenta de expeciones que aplican al servicio / convenio */
		lb_resp = (lrs_rs = new ResultSetDecorator(lps_ps.executeQuery()) ).next() ? lrs_rs.getBoolean(1) : false;

		/* Liberar recursos de base de datos */
		lrs_rs.close();
		lps_ps.close();

		/* Cerrar la conexión si es necesario */
		if(lb_abrirConexion)
			UtilidadBD.closeConnection(ac_con);
		return lb_resp;
	}

	/**
	* Cierra el ingreso del paciente y deja la cuenta en estado excenta
	* @param	ac_con		Conexión a una fuente de datos
	* @param	ai_cuenta	Codigo de la cuenta
	* @return	true Si la cuenta pudo ser cerrada de manera exitosa
	*			false de lo contrario
	* @throws	SQLException
	*/
	public static boolean cerrarCuentaConsultaExterna(
		Connection	ac_con,
		int			ai_cuenta
	)throws SQLException
	{
		boolean				lb_abrirConexion;
		boolean				lb_resp;
		DaoFactory			ldf_df;
		int					li_ingreso;
		PreparedStatementDecorator	lps_ps;
		ResultSetDecorator			lrs_rs;

		final String ls_cerrarCuenta =
			"UPDATE "	+	"cuentas "		+
			"SET "		+	"estado_cuenta"	+ "=" + ConstantesBD.codigoEstadoCuentaExcenta + " " +
			"WHERE "	+	"id"			+ "=?";

		final String ls_cerrarIngreso =
			"UPDATE "	+	"ingresos "		+
			"SET "		+	"fecha_egreso"	+ "=CURRENT_DATE,"	+
							"hora_egreso"	+ "="+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+" "	+
			"WHERE "	+	"id"			+ "=?";

		final String ls_obtenerIngreso =
			"SELECT "	+ "c.id_ingreso AS ingreso "	+
			"FROM "		+ "cuentas c "					+
			"WHERE "	+ "c.id=?";

		final String ls_solicitudesCargadas =
			"SELECT "	+	"COUNT(*)<1 AS solicitudesCargadas "		+
			"FROM "		+	"solicitudes s "							+
			"WHERE "	+	"s.cuenta"				+ "=?"	+ " AND "	+
							"esSolicitudTotalCargada(s.numero_solicitud)"	+ "='" 	+ ConstantesBD.acronimoSi+"'";

		/* Obtener una instancia del objeto principal de acceso a fuente de datos */
		ldf_df = DaoFactory.getDaoFactory(System.getProperty("TIPOBD") );

		/* Verificar el estado de la conexión y abrir una nueva si es necesario */
		lb_abrirConexion = (ac_con == null || ac_con.isClosed() );
		if(lb_abrirConexion )
			ac_con = ldf_df.getConnection();

		/* Iniciar transacción */
		lb_resp		= ldf_df.beginTransaction(ac_con);
		li_ingreso	= -1;

		if(lb_resp)
		{
			/* Preparar la consulta sobre las solicitudes */
			lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(ls_solicitudesCargadas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			/* Establecer los atributos de la búsqueda */
			lps_ps.setInt(1, ai_cuenta);

			/*
				Obtener si todas las solicitudes de esta cuenta estan en estado de facturación
				diferente a 'Cargada'
			*/
			lb_resp = (lrs_rs = new ResultSetDecorator(lps_ps.executeQuery()) ).next() ? lrs_rs.getBoolean(1) : false;

			/* Liberar recursos de base de datos */
			lrs_rs.close();
			lps_ps.close();
		}

		if(lb_resp)
		{
			/* Obtener el código del ingreso de la cuenta del paciente */
			lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(ls_obtenerIngreso,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			/* Establecer los atributos de la búsqueda */
			lps_ps.setInt(1, ai_cuenta);

			if( (lrs_rs = new ResultSetDecorator(lps_ps.executeQuery()) ).next() )
				li_ingreso = lrs_rs.getInt(1);

			/* Liberar recursos de base de datos */
			lrs_rs.close();
			lps_ps.close();

			lb_resp = li_ingreso > -1;
		}

		if(lb_resp)
		{
			/* Cerrar el ingreso del paciente */
			lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(ls_cerrarIngreso,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			/* Establecer los atributos de la modificación */
			lps_ps.setInt(1, li_ingreso);

			lb_resp = lps_ps.executeUpdate() == 1;

			/* Liberar recursos de base de datos */
			lps_ps.close();
		}

		if(lb_resp)
		{
			/* Cerrar el ingreso del paciente */
			lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(ls_cerrarCuenta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			/* Establecer los atributos de la modificación */
			lps_ps.setInt(1, ai_cuenta);

			lb_resp = lps_ps.executeUpdate() == 1;

			/* Liberar recursos de base de datos */
			lps_ps.close();
		}

		/* Terminar transaccionalidad */
		if(lb_resp)
		{
			/* El resultado de cierre de la cuenta y del ingreso fue exitoso */
			ldf_df.endTransaction(ac_con);
		}
		else
		{
			/* Se presentaron errores en el cierre de la cuenta o del ingreso */
			ldf_df.abortTransaction(ac_con);
		}

		/* Cerrar la conexión si es necesario */
		if(lb_abrirConexion)
			UtilidadBD.closeConnection(ac_con);
		return lb_resp;
	}
	
	/**
	 * Implementación del método que permite revisar
	 * un antecedente pedíatrico en una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#existeAntecedentePediatrico(Connection ,int )throws SQLException
	 */
	public static boolean existeAntecedentePediatrico(Connection con,int codigoPaciente)throws SQLException{

		PreparedStatementDecorator existeAntecedentePediatricoStatement= new PreparedStatementDecorator(con.prepareStatement(existeAntecedentePediatricoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		existeAntecedentePediatricoStatement.setInt(1, codigoPaciente);

		ResultSetDecorator rs=new ResultSetDecorator(existeAntecedentePediatricoStatement.executeQuery());

		if (rs.next())
		{
			if(rs.getInt("numResultados")>0){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	/**
	 * Implementación del método que realiza la validación del asocio de
	 * de cuentas en una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#validacionAsocioCuenta (Connection , int ) throws SQLException
	 */
	public static ResultadoBoolean validacionAsocioCuenta (Connection con, int idCuenta, String viaIngreso, String tipoPaciente) throws SQLException
	{		
		//Solo se pueden asociar cuentas con admisión de urgencias y de Hospitalización - Cirugía Ambulatoria
		if(viaIngreso.equals(ConstantesBD.codigoViaIngresoUrgencias+"") ||
			(viaIngreso.equals(ConstantesBD.codigoViaIngresoHospitalizacion+"") && 
				tipoPaciente.equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria+"")))
		{
			String obtenerConductaASeguirStr = "";
			
			if(viaIngreso.equals(ConstantesBD.codigoViaIngresoUrgencias+""))
			{
				//Ahora revisamos que tenga valoración
				obtenerConductaASeguirStr="SELECT valu.codigo_conducta_valoracion as codigoConductaASeguir " +
						"from valoraciones_urgencias valu, solicitudes sol " +
						"where valu.numero_solicitud=sol.numero_solicitud and sol.cuenta=?";
			}
			else
			{
				obtenerConductaASeguirStr="SELECT destino_salida as codigoDestinoSalida from egresos  where cuenta=?";				
			}
								
			PreparedStatementDecorator obtenerConductaASeguirStatement= new PreparedStatementDecorator(con.prepareStatement(obtenerConductaASeguirStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			obtenerConductaASeguirStatement.setInt(1, idCuenta);
			ResultSetDecorator rs=new ResultSetDecorator(obtenerConductaASeguirStatement.executeQuery());
	
			if (rs.next())
			{
				int conductaSeguirOdestinoSalida = rs.getInt(1);
				rs.close();
				
				if(viaIngreso.equals(ConstantesBD.codigoViaIngresoUrgencias+""))
				{				
					//Caso para la evolucion 
					if (conductaSeguirOdestinoSalida==ConstantesBD.codigoConductaSeguirCamaObservacion)
					{
						if (existeEgresoCompleto(con, idCuenta))
						{
							String obtenerCampoDestinoSalidaEgresoStr="SELECT destino_salida as codigoDestinoSalida from egresos  where cuenta=?";
							PreparedStatementDecorator obtenerCampoDestinoSalidaEgresoStatement= new PreparedStatementDecorator(con.prepareStatement(obtenerCampoDestinoSalidaEgresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							obtenerCampoDestinoSalidaEgresoStatement.setInt(1, idCuenta);
							
							rs=new ResultSetDecorator(obtenerCampoDestinoSalidaEgresoStatement.executeQuery());
							//No hay if porque existe egreso completo
							rs.next();
							if (rs.getInt("codigoDestinoSalida")==ConstantesBD.codigoDestinoSalidaHospitalizacion || 
									rs.getInt("codigoDestinoSalida")==ConstantesBD.codigoDestinoSalidaCirugiaAmbulatoria)
							{
								return new ResultadoBoolean (true);
							}
							else
							{
								return new ResultadoBoolean (false, "error.asociocuenta.destinoASalidaNoHospitalizacion");
							}
						}
						else
						{
							return new ResultadoBoolean (false, "error.asociocuenta.camaObservacionSinEgreso");
						}
					}
					//Caso para la valoracion
					else if (conductaSeguirOdestinoSalida==ConstantesBD.codigoConductaSeguirHospitalizarPiso || 
								conductaSeguirOdestinoSalida==ConstantesBD.codigoConductaSeguirSalaCirugiaAmbulatoria)
					{
						RespuestaValidacion resp2=existeEgresoAutomatico(con, idCuenta);
						if (resp2.puedoSeguir&&resp2.textoRespuesta!=null)
						{
							return new ResultadoBoolean(true);
						}
						else
						{
							return new ResultadoBoolean (false, "error.asociocuenta.reversionEgresoAutomatico");
						}
					}
					else
					{
						return new ResultadoBoolean (false, "error.asociocuenta.conductaSeguirNoAplica");
					}
				}
				else
				{
					if (existeEgresoCompleto(con, idCuenta))
					{						
						if (conductaSeguirOdestinoSalida==ConstantesBD.codigoDestinoSalidaHospitalizar)
						{
							return new ResultadoBoolean (true);
						}
						else
						{
							return new ResultadoBoolean (false, "error.asociocuenta.destinoASalidaNoHospitalizar");
						}
					}
					else
					{
						return new ResultadoBoolean (false, "error.asociocuenta.SinEgreso");
					}					
				}				
			}	
			else
			{
				rs.close();
				if(viaIngreso.equals(ConstantesBD.codigoViaIngresoUrgencias+""))
					return new ResultadoBoolean (false, "error.asociocuenta.cuentaSinValU");
				else
					return new ResultadoBoolean (false, "error.asociocuenta.cuentaSinEvolSal");				
			}
			
		}
		else
			return new ResultadoBoolean (false, "error.asociocuenta.cuentaNoUrgencias");
	}
	
	/**
	 * Implementación del método que busca el diagnostico a poner
	 * en la valoración de hospitalización después de un asocio en 
	 * una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#obtenerDiagnosticoParaNuevaValoracionHospEnAsocioCuenta (Connection , int ) throws SQLException
	 */
	public static String[] obtenerDiagnosticoParaNuevaValoracionHospEnAsocioCuenta (Connection con, int idCuentaAsociada,String viaIngresoCuentaVieja) throws SQLException
	{
		String resp[]=new String[2];
		
		logger.info("llamadao a obtenerDiagnosticoParaNuevaValoracionHospEnAsocioCuenta");
		
		//Primero revisamos si la cuenta tuvo egreso (Caso cama de 
		//observación) y de ahí sacamos el diagnostico definitivo principal
		String obtenerDiagnosticoCasoEgresoStr="SELECT evdiag.acronimo_diagnostico, evdiag.tipo_cie_diagnostico from evol_diagnosticos evdiag, egresos eg  where eg.cuenta=? and evdiag.principal=" + ValoresPorDefecto.getValorTrueParaConsultas() + " and evdiag.definitivo=" + ValoresPorDefecto.getValorTrueParaConsultas() + " and eg.evolucion=evdiag.evolucion";
		PreparedStatementDecorator statementReutilizable= new PreparedStatementDecorator(con.prepareStatement(obtenerDiagnosticoCasoEgresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		statementReutilizable.setInt(1, idCuentaAsociada);
		ResultSetDecorator rs=new ResultSetDecorator(statementReutilizable.executeQuery());
		
		logger.info("valor de sql 1 >> "+obtenerDiagnosticoCasoEgresoStr.replace("?",idCuentaAsociada+""));
		
		if (rs.next())
		{
			resp[0]=rs.getString("acronimo_diagnostico");
			resp[1]=rs.getString("tipo_cie_diagnostico");
			rs.close();
			return resp;
		}
		rs.close();
		
		String temporal = ConstantesBD.codigoTipoSolicitudInicialUrgencias+"";
		String obtenerDiagnosticoCasoNoEgresoStr = "";
		
		//Caso cuando viene de HOSPITALIZACION
		if(viaIngresoCuentaVieja.equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
		{
			temporal = ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+"";
			//Si no hubo diagnostico tomamos la valoración de hospitalizacion
			//y sacamos su diagnosticos de ingreso
			obtenerDiagnosticoCasoNoEgresoStr="SELECT " +
				"vh.diagnostico_ingreso as acronimo_diagnostico, " +
				"vh.diagnostico_cie_ingreso AS tipo_cie_diagnostico " +
				"from solicitudes sol " +
				"INNER JOIN val_hospitalizacion vh on(vh.numero_solicitud=sol.numero_solicitud) " +
				"where sol.cuenta =? and " +
				"sol.tipo=" + temporal+ " ";
			statementReutilizable= new PreparedStatementDecorator(con.prepareStatement(obtenerDiagnosticoCasoNoEgresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		}
		//Caso cuando viene de URGENCIAS
		else
		{
			
		
			//Si no hubo diagnostico tomamos la valoración de urgencias
			//y sacamos su diagnosticos definitivo principal
			obtenerDiagnosticoCasoNoEgresoStr="SELECT " +
				"vald.acronimo_diagnostico, " +
				"vald.tipo_cie_diagnostico " +
				"from solicitudes sol, val_diagnosticos vald " +
				"where sol.cuenta =? and " +
				"sol.tipo=" + temporal+ " "+
				"and vald.principal=" + ValoresPorDefecto.getValorTrueParaConsultas() + " and sol.numero_solicitud=vald.valoracion";
			statementReutilizable= new PreparedStatementDecorator(con.prepareStatement(obtenerDiagnosticoCasoNoEgresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		}
		statementReutilizable.setInt(1, idCuentaAsociada);
		rs=new ResultSetDecorator(statementReutilizable.executeQuery());
		
		logger.info("valor de sql 1 >> "+obtenerDiagnosticoCasoNoEgresoStr.replace("?",idCuentaAsociada+""));

		if (rs.next())
		{
			resp[0]=rs.getString("acronimo_diagnostico");
			resp[1]=rs.getString("tipo_cie_diagnostico");
			rs.close();
			return resp;
		}
		else
		{
			rs.close();
			throw new SQLException("Error en obtenerDiagnosticoParaNuevaValoracionHospEnAsocioCuenta No existe ni valoración de urgencias ni evolución con orden de salida para esta cuenta");
		}
	}
	
	/**
	 * Implementación del método que busca el código del centro de costo
	 * de un médico en una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#getCodigoCentroCostoDadoMedico (Connection , int ) throw SQLException
	 */
	public static int getCodigoCentroCostoDadoMedico (Connection con, String loginMedico) throws SQLException
	{
		String getCodigoCentroDadoMedicoStr="SELECT centro_costo as centroCosto from usuarios where login= ? ";
		PreparedStatementDecorator getCodigoCentroDadoMedicoStatement= new PreparedStatementDecorator(con.prepareStatement(getCodigoCentroDadoMedicoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		getCodigoCentroDadoMedicoStatement.setString(1, loginMedico);
		ResultSetDecorator rs=new ResultSetDecorator(getCodigoCentroDadoMedicoStatement.executeQuery());
		if (rs.next())
		{
			int resp=rs.getInt("centroCosto");
			rs.close();
			return resp;
		}
		else
		{
			throw new SQLException ("El médico específicado para el método getCodigoCentroDadoMedico no existe");
		}
	}
	
	/**
	 * Permite verificar que la cuenta tiene un medico adjunto
	 * @param cuenta
	 * @return
	 */
	public static boolean existeManejoConjunto(Connection con, int cuenta, int centroCosto)
	{
		try
		{
			String buscarExisteManejoStr="SELECT count(1) AS numRegistros from solicitudes s INNER JOIN adjuntos_cuenta a ON(a.solicitud=s.numero_solicitud) WHERE s.cuenta=? AND s.centro_costo_solicitado=?";
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(buscarExisteManejoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			stm.setInt(1, cuenta);
			stm.setInt(2, centroCosto);
			ResultSetDecorator resultado=new ResultSetDecorator(stm.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("numRegistros")>0?true:false;
			}
			return false;
		}
		catch(SQLException e)
		{
			logger.error("Error verificando el adjunto de la cuenta");
			return false;
		}
	}
	
	/**
	 * Permite verificar que la cuenta tiene manejo conjunto activo de solicitudes de interconsulta
	 * @param cuenta
	 * @return
	 */
	public static boolean existeManejoConjuntoActivoSolInterconsulta(Connection con, int cuenta, int centroCosto)
	{
		try
		{
			String buscarExisteManejoStr="SELECT count(1) AS numRegistros from solicitudes s INNER JOIN solicitudes_inter si ON (si.numero_solicitud=s.numero_solicitud) INNER JOIN adjuntos_cuenta a ON(a.solicitud=s.numero_solicitud) WHERE s.cuenta=? AND s.centro_costo_solicitado=? AND si.manejo_conjunto_finalizado="+ValoresPorDefecto.getValorFalseParaConsultas();
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(buscarExisteManejoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			stm.setInt(1, cuenta);
			stm.setInt(2, centroCosto);
			logger.info("\n\n\n existeManejoConjuntoActivoSolInterconsulta-->"+buscarExisteManejoStr+" cuenta->"+cuenta+" cc->"+centroCosto);
			ResultSetDecorator resultado=new ResultSetDecorator(stm.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("numRegistros")>0?true:false;
			}
			return false;
		}
		catch(SQLException e)
		{
			logger.error("Error verificando el adjunto de la cuenta ");
			return false;
		}
	}
	
	
	/**
	 * método que permite encontrar resultados de agenda que se encuentren en estado activo
	 * y con la unidad de consulta que llega de parámetro 
	 * @param con
	 * @param unidadConsulta
	 * @param centroAtencion
	 * @return
	 */
	public static boolean existeValoresCancelarAgenda(Connection con, int unidadConsulta, 
																							String fechaInicial, String fechaFinal, 
																							String horaInicial, String horaFinal,
																							int consultorio, int dia, int codigoMedico, int centroAtencion,
																							String centrosAtencion, String unidadesAgenda)
	{
		try
		{
			String consulta= "SELECT  codigo FROM agenda WHERE activo = " + ValoresPorDefecto.getValorTrueParaConsultas() + " ";
			if(unidadConsulta>0)
				consulta+=" AND unidad_consulta = "+unidadConsulta;
			else
				consulta+=" AND unidad_consulta IN ( "+unidadesAgenda+") ";
			
			if(!horaInicial.equals("") && horaInicial!=null) //si hay hora inicial entonces la final es requerida
			 	consulta+= " AND (hora_inicio>='"+horaInicial+"' AND hora_fin<= '"+horaFinal+"') ";
			
			if(consultorio>0) 	
			 	consulta+= " AND consultorio = "+consultorio;
			 
			if(dia>0)
			 	consulta+=" AND dia = "+dia;
			 
			 if(codigoMedico>0)
			 	consulta+=" AND codigo_medico = "+codigoMedico;
			 
			 if(centroAtencion!=ConstantesBD.codigoNuncaValido)
				 consulta+=" AND centro_atencion = "+centroAtencion;
			 else
				 consulta+=" AND centro_atencion IN ("+centrosAtencion+") ";
			 
			consulta+=	" AND (fecha >= '"+fechaInicial+"' AND fecha <= '"+fechaFinal+"') "; 
		
			PreparedStatementDecorator consultaPS=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(consultaPS.executeQuery());
			if (rs.next())
				return true;
			else
				return false;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de la agenda: SqlBaseUtilidadValidacionDao "+e.toString() );
			return false;			
		}
	}
	
	/**
	 * Implementación del método que permite revisar si existe
	 * una valoración pediatrica hospitalaria para una cuenta dada
	 * en una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#tieneValoracionPediatricaHospitalariaConsultarIngreso (Connection , int ) throws SQLException
	 */
	public static int tieneValoracionPediatricaHospitalariaConsultarIngreso (Connection con, int idCuenta) throws SQLException
	{
		int respuesta=0;
		String tieneValoracionPediatricaHospitalariaConsultarIngresoStr="SELECT sol.numero_solicitud as numeroSolicitud from solicitudes sol INNER JOIN valoraciones_pediatricas valped ON (sol.numero_solicitud=valped.numero_solicitud)  where sol.tipo=" + ConstantesBD.codigoTipoSolicitudInicialHospitalizacion + " and sol.cuenta=?";
		PreparedStatementDecorator tieneValoracionPediatricaHospitalariaConsultarIngresoStatement= new PreparedStatementDecorator(con.prepareStatement(tieneValoracionPediatricaHospitalariaConsultarIngresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		tieneValoracionPediatricaHospitalariaConsultarIngresoStatement.setInt(1, idCuenta);
		ResultSetDecorator rs=new ResultSetDecorator(tieneValoracionPediatricaHospitalariaConsultarIngresoStatement.executeQuery());
		if (rs.next())
		{
			respuesta=rs.getInt("numeroSolicitud");
			rs.close();
		}
		return respuesta;
	}
	
	/**
	 * método que permite activar o no la modificación 
	 * del número de identificación de terceros,
	 * únicamente en los casos en los cuales este tercero 
	 * no ha sido utilizado en empresas.
	 * (a futuro se tiene que validar que no se utilice en ningún lado)
	 * @param con, Conexión
	 * @param codigo, Código del tercero
	 * @return
	 */
	public static boolean hanUtilizadoTercero (Connection con, int codigo)
	{
		try
		{
			String consulta= "SELECT codigo FROM empresas WHERE tercero = ?";
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigo);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if (rs.next())
				return true;
			else
				return false;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de terceros: SqlBaseUtilidadValidacionDao "+e.toString() );
			return false;
		}
	}
	
	/**
	 * Implementación del método que permite buscar el código de la
	 * valoración inicial para una cuenta dada en una BD Genérica
	 * @param tipoBD 
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#buscarCodigoValoracionInicial (Connection , int ) throws SQLException
	 */
	public static int buscarCodigoValoracionInicial (Connection con, int idCuenta, int tipoBD) throws SQLException
	{
		String buscarCodigoValoracionInicialStr="SELECT sol.numero_solicitud as numeroSolicitud from solicitudes sol INNER JOIN valoraciones val ON (sol.numero_solicitud=val.numero_solicitud) where sol.cuenta =? and (sol.tipo=? or sol.tipo=? ) ";
		if(tipoBD==DaoFactory.POSTGRESQL)
			buscarCodigoValoracionInicialStr += " ORDER BY sol.numero_solicitud DESC "+ValoresPorDefecto.getValorLimit1()+" 1";
		else if(tipoBD==DaoFactory.ORACLE)
			buscarCodigoValoracionInicialStr += " AND rownum = 1 ORDER BY sol.numero_solicitud DESC ";
		
		PreparedStatementDecorator buscarCodigoValoracionInicialStatement= new PreparedStatementDecorator(con.prepareStatement(buscarCodigoValoracionInicialStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		buscarCodigoValoracionInicialStatement.setInt(1, idCuenta);
		buscarCodigoValoracionInicialStatement.setInt(2, ConstantesBD.codigoTipoSolicitudInicialHospitalizacion);
		buscarCodigoValoracionInicialStatement.setInt(3, ConstantesBD.codigoTipoSolicitudInicialUrgencias);
		ResultSetDecorator rs=new ResultSetDecorator(buscarCodigoValoracionInicialStatement.executeQuery());
		if (rs.next())
		{
			int resp=rs.getInt("numeroSolicitud");
			rs.close();
			return resp;
		}
		else
		{
			rs.close();
			return 0;
		}
	}
	
	/**
	 * Adición sebastián
	 * Implementación del método que permite buscar el código de la
	 * solicitud de la valoración inicial para una cuenta dada en una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#buscarCodigoValoracionInicial (Connection , int ) throws SQLException
	 */
	public static int buscarCodigoSolicitudValoracionInicial (Connection con, int idCuenta) 
	{
		try
		{
			String buscarCodigoValoracionInicialStr="SELECT numero_solicitud as numeroSolicitud from solicitudes where cuenta =? and (tipo=? or tipo=? )";
			PreparedStatementDecorator buscarCodigoValoracionInicialStatement= new PreparedStatementDecorator(con.prepareStatement(buscarCodigoValoracionInicialStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			buscarCodigoValoracionInicialStatement.setInt(1, idCuenta);
			buscarCodigoValoracionInicialStatement.setInt(2, ConstantesBD.codigoTipoSolicitudInicialHospitalizacion);
			buscarCodigoValoracionInicialStatement.setInt(3, ConstantesBD.codigoTipoSolicitudInicialUrgencias);
			ResultSetDecorator rs=new ResultSetDecorator(buscarCodigoValoracionInicialStatement.executeQuery());
			if (rs.next())
			{
				int resp=rs.getInt("numeroSolicitud");
				rs.close();
				return resp;
			}
			else
			{
				rs.close();
				return 0;
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en buscarCodigoSolicitudValoracionInicial de SqlBaseUtilidadValidacion: "+e);
			return 0;
		}
	}
	
	/**
	 * Implementación del método que dado el código de un 
	 * centro de costo devuelve su nombre, para una BD 
	 * Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#getNombreCentroCosto (Connection , int ) throws SQLException
	 */
	public static String getNombreCentroCosto (Connection con, int codigoCentroCostoBuscado) throws SQLException
	{
	    String getNombreCentroCostoStr="SELECT nombre from centros_costo where codigo =?";
	    PreparedStatementDecorator getNombreCentroCostoStatement= new PreparedStatementDecorator(con.prepareStatement(getNombreCentroCostoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    getNombreCentroCostoStatement.setInt(1, codigoCentroCostoBuscado);
	    ResultSetDecorator rs=new ResultSetDecorator(getNombreCentroCostoStatement.executeQuery());
	    if (rs.next())
	    {
	        String respuesta=rs.getString("nombre");
	        rs.close();
	        return respuesta;
	    }
	    else
	    {
	        rs.close();
	        return "";
	    }
	}
	
	/**
	 * Implementación del método que dado el código de una
	 * cuenta devuelve la fecha de admisión de la misma,
	 * si esta existe, si no tiene admisión lanza una
	 * excepción para una BD genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#getFechaAdmision (Connection , int ) throws SQLException
	 */
	public static String getFechaAdmision (Connection con, int idCuenta) throws SQLException
	{
	    String getFechaHoraAdmisionStr="SELECT to_char(fecha_admision, 'YYYY-MM-DD') as fechaAdmision from admisiones_urgencias where cuenta = ? UNION ALL SELECT to_char(fecha_admision, 'YYYY-MM-DD') as fechaAdmision from admisiones_hospi where cuenta =?";
	    PreparedStatementDecorator getFechaHoraAdmisionStatement= new PreparedStatementDecorator(con.prepareStatement(getFechaHoraAdmisionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    getFechaHoraAdmisionStatement.setInt(1, idCuenta);
	    getFechaHoraAdmisionStatement.setInt(2, idCuenta);
	    ResultSetDecorator rs=new ResultSetDecorator(getFechaHoraAdmisionStatement.executeQuery());
	    if (rs.next())
	    {
	        String respuesta=rs.getString("fechaAdmision");
	        rs.close();
	        return respuesta;
	    }
	    else
	    {
	        throw new SQLException ("La cuenta Especificada no tiene Admisión");
	    }
	}
	
	/**
	 * Implementación del método que dado el código de una
	 * cuenta devuelve la hora de admisión de la misma,
	 * si esta existe, si no tiene admisión lanza una
	 * excepción para una BD genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#getHoraAdmision (Connection , int ) throws SQLException
	 */
	public static String getHoraAdmision (Connection con, int idCuenta, String getHoraAdmisionStr) throws SQLException
	{
	    PreparedStatementDecorator getHoraAdmisionStatement= new PreparedStatementDecorator(con.prepareStatement(getHoraAdmisionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    getHoraAdmisionStatement.setInt(1, idCuenta);
	    getHoraAdmisionStatement.setInt(2, idCuenta);
	    ResultSetDecorator rs=new ResultSetDecorator(getHoraAdmisionStatement.executeQuery());
	    if (rs.next())
	    {
	        String respuesta=rs.getString("horaAdmision");
	        rs.close();
	        return respuesta;
	    }
	    else
	    {
	        throw new SQLException ("La cuenta Especificada no tiene Admisión");
	    }
	}

	/**
	 * Cargar la hora de apertura de la cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 * @throws SQLException
	 */
	public static String obtenerHoraAperturaCuenta(Connection con, int idCuenta)
	{
	    String consultaStr="SELECT hora_apertura AS hora_apertura FROM cuentas WHERE id = ?";
	    
		try
		{
			PreparedStatementDecorator getHoraAdmisionStatement= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    getHoraAdmisionStatement.setInt(1, idCuenta);
		    logger.info("--->"+consultaStr+"--"+idCuenta);
		    ResultSetDecorator rs=new ResultSetDecorator(getHoraAdmisionStatement.executeQuery());
		    if (rs.next())
		    {
		        String respuesta=rs.getString("hora_apertura");
		        rs.close();
		        return respuesta;
		    }
		    else
		    {
		        return "";
		    }
		}
		catch (SQLException e)
		{
			logger.error("Error consultando la cuenta : "+e);
			return null;
		}
	}

	/**
	 * Implementación del método que revisa si se debe
	 * mostrar el mensaje de la cancelación del tratante
	 * antes de llenar una evolución para una BD genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#deboMostrarMensajeCancelacionTratanteEnIngresoEvolucion (Connection , int , int ) throws SQLException
	 */
	public static boolean deboMostrarMensajeCancelacionTratanteEnIngresoEvolucion (Connection con, int idCuenta, int centroCostoMedico, String consulta) throws SQLException
	{
		logger.info("deboMostrarMensajeCancelacionTratanteEnIngresoEvolucionStr-->"+consulta+" -->idCuenta="+idCuenta+" centroCostoMe-->"+centroCostoMedico);
	    PreparedStatementDecorator deboMostrarMensajeCancelacionTratanteEnIngresoEvolucionStatement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    deboMostrarMensajeCancelacionTratanteEnIngresoEvolucionStatement.setInt(1, idCuenta);
	    deboMostrarMensajeCancelacionTratanteEnIngresoEvolucionStatement.setInt(2, centroCostoMedico);
	    ResultSetDecorator rs=new ResultSetDecorator(deboMostrarMensajeCancelacionTratanteEnIngresoEvolucionStatement.executeQuery());
	    if (rs.next())
	    {
	        int resp=rs.getInt("numResultados");
	        if (resp>0)
	        {
	            return true;
	        }
	        else
	        {
	            return false;
	        }
	    }
	    else
	    {
	        rs.close();
	        throw new SQLException ("No funcionó un count en deboMostrarMensajeCancelacionTratanteEnIngresoEvolucion (SqlBaseUtilidadValidacion)");
	    }
	}
	
	/**
	 * Verificar si el paciente (codigoPaciente) tiene citas atendidas por el médico (codigoMedico)
	 * @param con
	 * @param codigoMedico
	 * @param codigoPaciente
	 * @return
	 */
	public static boolean tieneCitaRespondida(Connection con, int codigoMedico, int codigoPaciente)
	{
		String tieneCitaRespondidaStr="SELECT c.estado_cita from cita c INNER JOIN agenda a ON(a.codigo=c.codigo_agenda) where a.codigo_medico=? AND c.codigo_paciente=? AND c.estado_cita="+ConstantesBD.codigoEstadoCitaAtendida;
		try
		{
			PreparedStatementDecorator tieneCitaRespondida= new PreparedStatementDecorator(con.prepareStatement(tieneCitaRespondidaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			tieneCitaRespondida.setInt(1, codigoMedico);
			tieneCitaRespondida.setInt(2, codigoPaciente);
			ResultSetDecorator resultado=new ResultSetDecorator(tieneCitaRespondida.executeQuery());
			return resultado.next();
		}
		catch (SQLException e)
		{
			logger.error("Error verificando si el paciente "+codigoPaciente+" tiene citas respondidas por el médico "+codigoMedico+" "+e);
			return false;
		}
	}
	
	/**
	 * Método para verificar la existencia de cuenta
	 * asociada
	 * @param con
	 * @param ingreso
	 * @param sentenciaSQL
	 * @return codigo de la cuenta asociada si existe, 0 de lo contrario
	 */
	public static int tieneCuentaAsociada(Connection con, int ingreso, String sentenciaSQL)
	{
		PreparedStatementDecorator cuentaAsociada=null;
		ResultSetDecorator resultado = null;
		try
		{
			cuentaAsociada= new PreparedStatementDecorator(con.prepareStatement(sentenciaSQL));
			cuentaAsociada.setInt(1, ingreso);
			cuentaAsociada.setInt(2, ingreso);
			resultado=new ResultSetDecorator(cuentaAsociada.executeQuery());
			while(resultado.next())
			{
				return resultado.getInt("cuentainicial");
			}
			return 0;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando la existencia de la cuenta Asociada "+e);
			return 0;
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(cuentaAsociada, resultado, null);
		}
	}
	
	
	/**
	 * 
	 * @return
	 */
	public static StringBuffer getConsultaCuentasAsociadas() {
		StringBuffer sentenciaSQL = new StringBuffer("SELECT ACU.CUENTA_INICIAL AS cuentaInicial, ");
		sentenciaSQL.append("       ACU.CUENTA_FINAL AS cuentaFinal ");
		sentenciaSQL.append("  FROM asocios_cuenta ACU");
		sentenciaSQL.append(" LEFT JOIN cuentas CUI");
		sentenciaSQL.append("   ON CUI.ID = ACU.CUENTA_INICIAL");
		sentenciaSQL.append(" LEFT JOIN cuentas CUF");
		sentenciaSQL.append("     ON CUF.ID = ACU.CUENTA_FINAL");
		sentenciaSQL.append(" WHERE ACU.ACTIVO = ?");
		sentenciaSQL.append("   AND CUI.ID_INGRESO = CUF.ID_INGRESO");
		sentenciaSQL.append("   AND CUF.ID_INGRESO = ?");
		sentenciaSQL.append("   AND (CUI.ID IS NOT NULL");
		sentenciaSQL.append("    OR CUF.ID IS NOT NULL) ");
		sentenciaSQL.append(" ORDER BY ACU.CODIGO");
		
		return sentenciaSQL;
	}
	
	/**
	 * Implementación del método que dado el código de una 
	 * cuenta o subcuenta devuelve su estado, para una BD 
	 * Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#cuentaOSubcuentaTieneEstadoDefinido (Connection , int , int , boolean ) throws SQLException
	 */
	public static boolean cuentaOSubcuentaTieneEstadoDefinido (Connection con, int idCuenta, int estadoAVerificar, boolean esSubcuenta) throws SQLException
	{
	    String cuentaTieneEstadoDefinidoStr;
	    
	    if (esSubcuenta)
	    {
	        cuentaTieneEstadoDefinidoStr="SELECT count(1) as numResultados from sub_cuentas where sub_cuenta=? and estado=?";
	    }
	    else
	    {
	        cuentaTieneEstadoDefinidoStr="SELECT count(1) as numResultados from cuentas where id=? and estado_cuenta=?";  
	    }

	    PreparedStatementDecorator cuentaTieneEstadoDefinidoStatement = new PreparedStatementDecorator(con.prepareStatement(cuentaTieneEstadoDefinidoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    cuentaTieneEstadoDefinidoStatement.setInt(1, idCuenta);
	    cuentaTieneEstadoDefinidoStatement.setInt(2, estadoAVerificar);
	    ResultSetDecorator rs=new ResultSetDecorator(cuentaTieneEstadoDefinidoStatement.executeQuery());
	    if (rs.next())
	    {
	        int numRespuestas=rs.getInt("numResultados");
	        rs.close();
	        if (numRespuestas>0)
	        {
	            return true;
	        }
	        else
	        {
	            return false;
	        }
	    }
	    else
	    {
	        throw new SQLException ("Error e un count en cuentaTieneEstadoDefinido");
	    }
	}
	
	/**
	 * Implementación del método que dado el código de una 
	 * cuenta o subcuenta devuelve el nombre de su estado, 
	 * para una BD Genérica
	 *
	 * @see com.princetonsa.dao.UtilidadValidacionDao#getNombreEstadoCuentaOSubcuenta(Connection , int, boolean )throws SQLException
	 */
	public static String getNombreEstadoCuentaOSubcuenta(Connection con, int id, boolean esSubCuenta)throws SQLException
	{
	    String getNombreEstadoStr;
	    
	    if (esSubCuenta)
	    {
	        getNombreEstadoStr="SELECT ec.nombre from sub_cuentas sc INNER JOIN estados_cuenta ec ON (sc.estado=ec.codigo) where sc.sub_cuenta=?";   
	    }
	    else
	    {
	        getNombreEstadoStr="SELECT ec.nombre from cuentas cue INNER JOIN estados_cuenta ec ON (cue.estado_cuenta=ec.codigo) where cue.id=?";
	    }
	    
	    PreparedStatementDecorator getNombreEstadoCuentaStatement= new PreparedStatementDecorator(con.prepareStatement(getNombreEstadoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    getNombreEstadoCuentaStatement.setInt(1, id);
	    ResultSetDecorator rs=new ResultSetDecorator(getNombreEstadoCuentaStatement.executeQuery());
	    if (rs.next())
	    {
	        String nombre=rs.getString("nombre");
	        rs.close();
	        return nombre;
	    }
	    else
	    {
	        throw new SQLException ("El código de cuenta específicado en getNombreEstadoCuentaOSubCuenta de UtilidadValidacionDao no existe" );
	    }
	    
	}

	
	/**
	 * Metodo que me indica si una cuenta es igual a un estado.
	 * @param con, Conexion.
	 * @param id, Id de la cuenta
	 * @param estado, estado que se validara.
	 * @return true si la cuenta es asociada
	 */
	public static boolean igualEstadoCuenta(Connection con, int id,int estado)
	{
		String consulta="SELECT estado_cuenta as estadocuenta from cuentas where id=?";
		try
		{
			PreparedStatementDecorator esAsociada= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			esAsociada.setInt(1,id);
			ResultSetDecorator rs=new ResultSetDecorator(esAsociada.executeQuery());
			if(rs.next())
			{
				int codigoEstado=rs.getInt("estadocuenta");
				if(codigoEstado==estado)
					return true;
			}
		}
		catch(SQLException e)
		{
			logger.error("Se produjo un error consultando el estado de la cuenta"+e);
			return false;
		}
		return false;
	}
	
	/**
	 * Método que indica si la solicitud pasada por parámetros fue originada
	 * en la cuenta pasada por parámetros
	 * La comparación se hace por tiempo
	 * @param con
	 * @param numeroSolicitud
	 * @param idCuenta
	 * @return true si la solicitud pertenece a la cuenta, false de lo contrario
	 */
	public static boolean perteneceSolicitudACuentaComparacionXTiempo(Connection con, int numeroSolicitud, int idCuenta)
	{
		try
		{
			String[] fechasValoracion=obtenerFechaYHoraPrimeraValoracion(con, idCuenta);
			if(fechasValoracion==null)
			{
				PreparedStatementDecorator fechaStm= new PreparedStatementDecorator(con.prepareStatement("SELECT to_char(fecha_apertura,'YYYY-MM-DD') AS fecha, hora_apertura AS hora FROM cuentas where id=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				fechaStm.setInt(1, idCuenta);
				ResultSetDecorator resultado=new ResultSetDecorator(fechaStm.executeQuery());
				if(resultado.next())
				{
					fechasValoracion = new String[2];
					fechasValoracion[0]=resultado.getString("fecha");
					fechasValoracion[1]=resultado.getString("hora");
				}
				else
				{
					logger.error("Error obteniendo las fechas de la cuenta "+idCuenta);
					return false;
				}
			}
			String obtenerCuentaOriginalSolicitudStr="SELECT count(1) AS numResultados FROM solicitudes s WHERE numero_solicitud=? AND (s.fecha_solicitud>to_date(?,'YYYY-MM-DD') OR (s.fecha_solicitud=to_date(?,'YYYY-MM-DD') AND s.hora_solicitud>?))";
			PreparedStatementDecorator buscarSolicitud= new PreparedStatementDecorator(con.prepareStatement(obtenerCuentaOriginalSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			buscarSolicitud.setInt(1, numeroSolicitud);
			buscarSolicitud.setString(2, fechasValoracion[0]);
			buscarSolicitud.setString(3, fechasValoracion[0]);
			buscarSolicitud.setString(4, fechasValoracion[1]);
			ResultSetDecorator resultado=new ResultSetDecorator(buscarSolicitud.executeQuery());
			if(resultado.next())
			{
				if(resultado.getInt("numResultados")>0)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando la cuenta de la solicitud dada: "+e);
			return false;
		}
	}
	
	/**
	 * Método para verificar si el paciente tiene o no evoluciones
	 * @param con Conexión con la BD
	 * @param idCuenta Codigo de la cuenta
	 * @return true si el paciente tiene evoluciones, false de lo contrario
	 */
	public static boolean tieneEvoluciones(Connection con, int idCuenta)
	{
		String tieneEvolucionesStr="SELECT count(1) AS numResultados FROM evoluciones ev INNER JOIN solicitudes sol ON(sol.numero_solicitud=ev.valoracion) WHERE sol.cuenta=?";
		try
		{
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(tieneEvolucionesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setInt(1, idCuenta);
			ResultSetDecorator resultado=new ResultSetDecorator(statement.executeQuery());
			if(resultado.next())
			{
				if(resultado.getInt("numResultados")>0)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando la existencia de evoluciones para la cuenta "+idCuenta+": "+e);
			return false;
		}
	}

	/**
	 * Método para validar que la cuenta dada no se encuentre en proceso de facturación
	 * @param con Conexión con la BD
	 * @param codigoPaciente Código del paciente
	 * @param idSesionOPCIONAL, si viene cargado eso quiere decir que no vamos a filtrar por ese numero de sesion
	 * @return true si la cuenta está en proceso de facturación por otro usuario, false de lo contrario
	 */
	public static boolean estaEnProcesofacturacion(Connection con, int codigoPaciente, String idSesionOPCIONAL)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try
		{
			String validacionStr="SELECT count(1) AS numResultados FROM cuentas cue INNER JOIN pacientes pac on(cue.codigo_paciente=pac.codigo_paciente) INNER JOIN cuentas_proceso_fact proc ON(proc.cuenta=cue.id) WHERE cue.estado_cuenta="+ConstantesBD.codigoEstadoCuentaProcesoFacturacion+" AND pac.codigo_paciente=?";
			
			if(!UtilidadTexto.isEmpty(idSesionOPCIONAL))
			{
				validacionStr+=" AND id_sesion<> '"+idSesionOPCIONAL+"' ";
			}
			
			pst = con.prepareStatement(validacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoPaciente);
			rs = pst.executeQuery();
			if(rs.next())
			{
				if(rs.getInt("numResultados")>0)
				{
					return true;
				}
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR estaEnProcesofacturacion",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR estaEnProcesofacturacion", e);
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
		return false;
	}
	
	
	
	/**
	 * Método que verifica si la tarifa del artículo ya existe
	 * @param con Conexión con la BD
	 * @param articulo Código del articulo
	 * @param esquemaTarifario Esquema tarifario al que pertenece la tarifa
	 * @return true si existe la tarifa, false de lo contrario
	 */
	public static boolean existeTarifaParaArticulo(Connection con, int articulo, int esquemaTarifario)
	{
		try
		{
			String verificarTarifa="SELECT count(1) AS numResultados FROM tarifas_inventario WHERE articulo=? AND esquema_tarifario=?";
			PreparedStatementDecorator verificacionTarifaStm= new PreparedStatementDecorator(con.prepareStatement(verificarTarifa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			verificacionTarifaStm.setInt(1, articulo);
			verificacionTarifaStm.setInt(2,esquemaTarifario);
			ResultSetDecorator resultado=new ResultSetDecorator(verificacionTarifaStm.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("numResultados")>0;
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error verificando la existencia de la tarifa "+e);
			return false;
			
		}
	}

	/**
	 * Metodo para realizar la validacion de si exite en la base
	 * de datos un paciente con el mismo nombre y apellidos esta
	 * validacion se esta haciendo por institucion, en caso de 
	 * que se necesite que no se por institucion debe cambiarse
	 * el inner join de la tabla pacientes_instituciones por la 
	 * tabla pacientes;
	 * @param con
	 * @param primerNombre
	 * @param segundoNombre
	 * @param primerApellido
	 * @param segundoApellido
	 * @return
	 */
	public static String validacionExistePacienteMismoNombre(Connection con, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido) 
	{
		try
		{
			String cadena="SELECT p.tipo_identificacion as tipoidentificacion, p.numero_identificacion as numeroidentificacion from personas p inner join pacientes_instituciones pi on pi.codigo_paciente = p.codigo where p.primer_nombre=? and p.segundo_nombre=? and primer_apellido=? and segundo_apellido=?";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,primerNombre);
			ps.setString(2,segundoNombre);
			ps.setString(3,primerApellido);
			ps.setString(4,segundoApellido);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getString("tipoidentificacion")+"@@@@"+rs.getString("numeroidentificacion");
			}
			else
			{
				return "";
			}
			
		}
		catch (SQLException e)
		{
			logger.error("Error realizando validacion "+e);
			return "";
			
		}
	}

	
	/**
	 * Metodo que retorna true si el articulo es un medicamento(independiente de que sea pos o no)
	 * @param conexion
	 * @param articulo
	 * @return boolean, respuesta true indica si es medicamento.
	 */
	public static boolean esMedicamento(Connection con, int articulo,String cadena)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,articulo);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getBoolean(1);
			}
		}
		catch (SQLException e)
		{
			logger.error("Error realizando validacion "+e);
			e.printStackTrace();
		}
		return false;
	}

	/**Metodo que retorna true si existe un tipo de regimen y naturaleza 
	 * @param con
	 * @param codigoRegimen
	 * @param codigoNaturaleza
	 * @return respuesta true si existe tipo de regimen y naturaleza 
	 */
	public static boolean existeExcNatuRegimen(Connection con, String codigoRegimen, int codigoNaturaleza)
	{
			try
			{
				String cadena="SELECT true as respuesta from excepciones_naturaleza where acr_regimen = ? and cod_naturaleza=?";
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1,codigoRegimen);
				ps.setInt(2,codigoNaturaleza);
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
				{
					return rs.getBoolean("respuesta");
				}
			}
			catch (SQLException e)
			{
				logger.error("Error realizando validacion "+e);
				
			}
			return false;
	}

	/**
	 * Metodo que retorna un boolean indicanto si el diagnostico con ese tipo CIE ya existe en el sitema.
	 * @param con
	 * @param codigo
	 * @param tipoCie
	 * @return
	 */
	public static boolean diagnosticoExistente(Connection con, String codigo, int tipoCie) 
	{
		try
		{
			String cadena="SELECT 'true' as respuesta from diagnosticos where upper(acronimo) = upper(?) and tipo_cie=?";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigo);
			ps.setInt(2,tipoCie);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getBoolean("respuesta");
			}
		}
		catch (SQLException e)
		{
			logger.error("Error realizando validacion "+e);
			
		}
		return false;
	}

	/**
	 * Método para cargar el tipo de diagnóstico principal de la última evolución
	 * en caso de no existir evoluciones lo toma de la valoracioçón inicial
	 * @param con
	 * @param codigoCuenta
	 * @param revisarEnEvolucion
	 * Retorna el código del tipo de diagnóstico
	 */
	public static int obtenerTipoDiagnosticoPrincipal(Connection con, int codigoCuenta, boolean revisarEnEvolucion)
	{
		try
		{
			String obtenerTipoDiagPrincipalStr;
			if(revisarEnEvolucion)
			{
				
				String cadenaTempo="select max(ev.fecha_evolucion || '"+ConstantesBD.separadorSplit+"' || hora_evolucion) from solicitudes sol, evoluciones ev where sol.numero_solicitud=ev.valoracion and sol.cuenta=?";
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaTempo));
				ps.setInt(1, codigoCuenta);
				logger.info("-->"+cadenaTempo+"----"+codigoCuenta);
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
				{
					String[] fechorval=rs.getString(1).split(ConstantesBD.separadorSplit);
					if(fechorval.length>1&&!UtilidadTexto.isEmpty(fechorval[0])&&!UtilidadTexto.isEmpty(fechorval[1]))
					{
						obtenerTipoDiagPrincipalStr="select tipo_diagnostico_principal AS tipoDiagnosticoPrincipal from evoluciones where fecha_evolucion='"+fechorval[0]+"' and hora_evolucion = '"+fechorval[1]+"'";

						logger.info(obtenerTipoDiagPrincipalStr);
						PreparedStatementDecorator obtenerStm= new PreparedStatementDecorator(con.prepareStatement(obtenerTipoDiagPrincipalStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						ResultSetDecorator resultado=new ResultSetDecorator(obtenerStm.executeQuery());
						if(resultado.next())
						{
							return resultado.getInt("tipoDiagnosticoPrincipal");
						}
						else
						{
							return 0;
						}
					}
				}
				return 0;
			}
			else
			{
				obtenerTipoDiagPrincipalStr="SELECT tipo_diagnostico_principal as tipoDiagnosticoPrincipal from valoraciones_consulta where numero_solicitud=(SELECT min(sol.numero_solicitud) from solicitudes sol, valoraciones val where sol.numero_solicitud=val.numero_solicitud and sol.cuenta=?)";
				PreparedStatementDecorator obtenerStm= new PreparedStatementDecorator(con.prepareStatement(obtenerTipoDiagPrincipalStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				obtenerStm.setInt(1, codigoCuenta);
				ResultSetDecorator resultado=new ResultSetDecorator(obtenerStm.executeQuery());
				if(resultado.next())
				{
					return resultado.getInt("tipoDiagnosticoPrincipal");
				}
				else
				{
					return 0;
				}
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el tipo de diagnóstico principal : "+e);
			return 0;
		}
	}

	/**
	 * Metodo que consulta una funcionalidad particular la cual no tenga una entrada en
	 * dependencias funcionalidades, entonces se filtra segun el rol y el codigo de la 
	 * funcionalidad
	 * @param con
	 * @param login
	 * @param codigoFuncionalidad
	 * @return devuelve el nombre de la funcionalidad con su path 
	 */
	public static String funcionalidadADibujarNoEntradaDependencias(Connection con, String login, int codigoFuncionalidad)
	{
		String consulta=		"SELECT " +
									"etiqueta_func AS nombreFuncionalidad, " +
									"archivo_func AS path " +
									"from " +
									"roles_usuarios ru " +
									"INNER JOIN usuarios u ON(ru.login=u.login) " +
									"INNER JOIN roles_funcionalidades rf ON(rf.nombre_rol=ru.nombre_rol) " +
									"INNER JOIN funcionalidades f ON(f.codigo_func="+codigoFuncionalidad+") " +
									"WHERE " +
									"u.login=? " +
									"AND rf.codigo_func="+codigoFuncionalidad;
		String resultado="";
		try
		{
			PreparedStatementDecorator pst=null;
			
			pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,login);
			ResultSetDecorator rs= new ResultSetDecorator(pst.executeQuery());
			
			
			if(rs.next())
			{
			    resultado= rs.getString("nombreFuncionalidad")+ConstantesBD.separadorSplit+rs.getString("path");
			}
		}
		catch(Exception e)
		{
			logger.error("Error consultando las funcionalidades del tercer nivel para el usuario "+login+": "+e);
		}
		
		return resultado;
	}
	
	/**
	 * Metodo que determina si un usuario puede interpretar una solicitud de interconsulta o procedimiento
	 * @param numeroSolicitud
	 * @param codigoCentroCostoUsuario
	 * @return
	 */
	public static boolean puedoInterpretarSolicitudInterconsultaProcedimiento(Connection con, int numeroSolicitud, int codigoCentroCostoUsuario)
	{
	    //@todo arreglarlo para que valide pero contra el centro de costo del tratante de la cuenta
	    String consulta="SELECT numero_solicitud FROM solicitudes WHERE numero_solicitud=? AND centro_costo_solicitante=? ";
	    try
		{
			PreparedStatementDecorator pst=null;
			pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, numeroSolicitud);
			pst.setInt(2, codigoCentroCostoUsuario);
			ResultSetDecorator rs= new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
			    return true;
			}
		}
		catch(Exception e)
		{
			logger.error("Error consultando  puedoInterpretarSolicitudInterconsultaProcedimiento"+e);
			return false;
		}
		return false;
	}
	
	/**
	 *Metodo que retorna el manejo en que se encuentra una solicitud específica
	 *retornta true si la solicitud se encuentra en manejo conjunto
	 */
	public static boolean isManejoConjunto(Connection con,int numeroSolicitud)
	{
	    String consulta="SELECT manejo_conjunto_finalizado as manejo_conjunto_finalizado FROM solicitudes_inter WHERE numero_solicitud=?";
	    try
		{
			PreparedStatementDecorator pst=null;
			pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, numeroSolicitud);
			ResultSetDecorator rs= new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
			    return (!rs.getBoolean("manejo_conjunto_finalizado"));
			}
		}
		catch(Exception e)
		{
			logger.error("Error consultando  puedoInterpretarSolicitudInterconsultaProcedimiento"+e);
			return false;
		}
		return false;
	}

	/**
	 * Metodo que dado el centro de costo de un usuario, y el numero de solicitud me dice si la puedo modificar o no.
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoCentroCosto
	 * @return
	 **/
	public static boolean medicoPuedeResponderSolicitud(Connection con, int numeroSolicitud, int codigoCentroCosto) 
	{
	    String consulta="SELECT numero_solicitud from solicitudes where numero_solicitud=? and centro_costo_solicitado = ?";
	    try
		{
			PreparedStatementDecorator pst=null;
			pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, numeroSolicitud);
			pst.setInt(2,codigoCentroCosto);
			ResultSetDecorator rs= new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
			    return true;
			}
		}
		catch(Exception e)
		{
			logger.error("Error consultando  puedoInterpretarSolicitudInterconsultaProcedimiento"+e);
			return false;
		}
		return false;
	}

	/**
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static int obtenerIngreso(Connection con, int idCuenta) 
	{
		try
		{
			String consulta="SELECT id_ingreso as ingreso from cuentas where id=?";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, idCuenta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if (rs.next())
			{
				return rs.getInt("ingreso");
			}
		}
		catch(Exception e)
		{
			logger.error("Error consultando  puedoInterpretarSolicitudInterconsultaProcedimiento"+e);
		}

		return 0;
	}
	
	/**
	 * Metodo que indica si una solicitud de interconsulta tiene manejo conjunto finalizado
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean esSolicitudManejoConjuntoFinalizado(Connection con, int numeroSolicitud)
	{
	    String consulta="SELECT manejo_conjunto_finalizado FROM solicitudes_inter WHERE numero_solicitud=? AND codigo_manejo_interconsulta="+ConstantesBD.codigoManejoConjunto;
	    try
		{
			PreparedStatementDecorator pst=null;
			pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, numeroSolicitud);
			ResultSetDecorator rs= new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
			    return true;
			}
		}
		catch(Exception e)
		{
			logger.error("Error consultando  esSolicitudManejoConjuntoFinalizado"+e);
			return false;
		}
		return false;
	}
	
	/**
	 * Método que verifica si la tarifa ISS del servicio ya existe
	 * @param con Conexión con la BD
	 * @param servicio Código del servicio
	 * @param esquemaTarifario Esquema tarifario al que pertenece la tarifa
	 * @return true si existe la tarifa, false de lo contrario
	 */
	public static boolean existeTarifaISSParaServicio(Connection con, int servicio, int esquemaTarifario)
	{
		try
		{
			String verificarTarifa="SELECT count(1) AS numResultados FROM tarifas_iss WHERE servicio=? AND esquema_tarifario=?";
			PreparedStatementDecorator verificacionTarifaStm= new PreparedStatementDecorator(con.prepareStatement(verificarTarifa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			verificacionTarifaStm.setInt(1, servicio);
			verificacionTarifaStm.setInt(2,esquemaTarifario);
			logger.info("===>Consulta Existe Tarifa: "+verificarTarifa+" ===>Servicio: "+servicio+" ===>Esquema Tarifario: "+esquemaTarifario);
			ResultSetDecorator resultado=new ResultSetDecorator(verificacionTarifaStm.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("numResultados")>0;
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error verificando la existencia de la tarifa "+e);
			return false;
			
		}
	}
	
	/**
	 * Método que verifica si la tarifa SOAT del servicio ya existe
	 * @param con Conexión con la BD
	 * @param servicio Código del servicio
	 * @param esquemaTarifario Esquema tarifario al que pertenece la tarifa
	 * @return true si existe la tarifa, false de lo contrario
	 */
	public static boolean existeTarifaSOATParaServicio(Connection con, int servicio, int esquemaTarifario)
	{
		try
		{
			String verificarTarifa="SELECT count(1) AS numResultados FROM tarifas_soat WHERE servicio=? AND esquema_tarifario=?";
			PreparedStatementDecorator verificacionTarifaStm= new PreparedStatementDecorator(con.prepareStatement(verificarTarifa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			verificacionTarifaStm.setInt(1, servicio);
			verificacionTarifaStm.setInt(2,esquemaTarifario);
			ResultSetDecorator resultado=new ResultSetDecorator(verificacionTarifaStm.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("numResultados")>0;
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error verificando la existencia de la tarifa "+e);
			return false;
			
		}
	}

	/**
	 * @param con
	 * @param codigoConvenio
	 * @param institucion
	 * @return
	 */
	public static boolean existeCierreSalodosIniciales(Connection con, int institucion) 
	{
		try
		{
			String cadena="SELECT count(1) AS numResultados FROM cierres_saldos WHERE institucion=? AND tipo_cierre=?";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			ps.setString(2,ConstantesBD.codigoTipoCierreSaldoInicialStr);
			ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());
			if(resultado.next())
			{
				int res=resultado.getInt("numResultados");
				if(res>0)
					return true;
			}
		}	
		catch (SQLException e)
		{
			logger.error("Error verificando el cierre de saldos iniciales en utilidad validacion "+e);
		}
		return false;
	}
	
	/**
	 * metodo que verifica la ecistencia de cierre saldos iniciales capitacion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static boolean existeCierreSaldosInicialesCapitacion(Connection con, int institucion) 
	{
		try
		{
			String cadena="SELECT count(1) AS numResultados FROM cierres_saldos_capitacion WHERE institucion=? AND tipo_cierre=?";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			ps.setString(2,ConstantesBD.codigoTipoCierreSaldoInicialStr);
			ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());
			if(resultado.next())
			{
				int res=resultado.getInt("numResultados");
				if(res>0)
					return true;
			}
		}	
		catch (SQLException e)
		{
			logger.error("Error verificando el cierre de saldos iniciales capitacion en utilidad validacion "+e);
		}
		return false;
	}
	
	
	
	/**
	 * Método que verifica si el paciente tiene antecedentes gineco-obstetricos
	 * @param con
	 * @param codigoPersona
	 * @return true si existen antecedentes, false de lo contrario
	 */
	public static boolean existeAntecedenteGinecoobstetrico(Connection con, int codigoPersona)
	{
		String existeAntecedenteGinecoobstetricoStr="SELECT count(1) AS numResultados from ant_gineco_obste WHERE codigo_paciente=?";
		
		try
		{
			if(con==null || con.isClosed())
			{
			con=UtilidadBD.abrirConexion();
			}
		} catch (SQLException e1)
		{
			logger.warn("No se pudo realizar la conexión (SqlBaseUtilidadValidacionDao)"+e1.toString());
		}
		
		try
		{
			PreparedStatementDecorator existeAntecedenteGinecoobstetricoStm= new PreparedStatementDecorator(con.prepareStatement(existeAntecedenteGinecoobstetricoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			existeAntecedenteGinecoobstetricoStm.setInt(1, codigoPersona);
			ResultSetDecorator resultado=new ResultSetDecorator(existeAntecedenteGinecoobstetricoStm.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("numResultados") > 0;
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando la existencia de antecdentes gineco obatetricos : "+e);
			return false;
		}
	}
	
	/**
	 * Método que verifica si el paciente tiene información histórica de antecedentes gineco-obstétricos
	 * @param con
	 * @param codigoPersona
	 * @return true si existen antecedentes, false de lo contrario
	 */
	public static boolean existeAntecedenteGinecoHisto (Connection con, int codigoPersona)
	{
		String existeAntecedenteGinecoHistoStr="SELECT count(1) AS numResultados from ant_gineco_histo WHERE codigo_paciente=?";
		try
		{
			PreparedStatementDecorator existeAntecedenteGinecoHistoStm= new PreparedStatementDecorator(con.prepareStatement(existeAntecedenteGinecoHistoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			existeAntecedenteGinecoHistoStm.setInt(1, codigoPersona);
			ResultSetDecorator resultado=new ResultSetDecorator(existeAntecedenteGinecoHistoStm.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("numResultados") > 0;
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando la existencia de historico antecedentes gineco obstétricos : "+e);
			return false;
		}
	}

	/**
	 * Método para validar que la cuenta dada no se encuentre en proceso de distribucion
	 * @param con Conexión con la BD
	 * @param codigoPaciente Código del paciente
	 * @param login del usuario que trata de acceder a la facturación
	 * @return true si la cuenta está en proceso de distribucion por otro usuario, false de lo contrario
	 */
	public static boolean estaEnProcesoDistribucion(Connection con, int codigoPaciente, String loginUsuario)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		boolean resultado=false;
		try
		{
			logger.info("############## Inicio estaEnProcesoDistribucion");
			String validacionStr="";
			//Se modifican las consultas porque ya no se esta guardando en estas tablas 05/Dic/2011 
//			if(loginUsuario!=null)
//			{
//				validacionStr="SELECT count(1) AS numResultados FROM cuentas cue INNER JOIN pacientes pac ON (pac.codigo_paciente=cue.codigo_paciente) INNER JOIN cuentas_proceso_dist proc ON(proc.cuenta=cue.id)WHERE cue.estado_cuenta="+ConstantesBD.codigoEstadoCuentaProcesoDistribucion+" AND pac.codigo_paciente=? AND proc.usuario!=?";
//			}
//			else
//			{
//				validacionStr="SELECT count(1) AS numResultados FROM cuentas cue INNER JOIN pacientes pac on (pac.codigo_paciente=cue.codigo_paciente) INNER JOIN cuentas_proceso_dist proc ON(proc.cuenta=cue.id)WHERE cue.estado_cuenta="+ConstantesBD.codigoEstadoCuentaProcesoDistribucion+" AND pac.codigo_paciente=?";
//			}
			validacionStr="SELECT count(0) AS numResultados FROM ingresos_procesos_distribucion WHERE id_ingreso IN (SELECT id FROM ingresos WHERE codigo_paciente=?)";
			pst= con.prepareStatement(validacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoPaciente);
//			if(loginUsuario!=null)
//			{
//				pst.setString(2, loginUsuario);
//			}
			rs=pst.executeQuery();
			if(rs.next())
			{
				if(rs.getInt("numResultados")>0)
				{
					resultado=true;
				}
			}
		}
		catch(SQLException e){
			logger.error("############## SQLERROR estaEnProcesoDistribucion", e);
	    }
		catch(Exception e){
			logger.error("############## ERROR estaEnProcesoDistribucion", e);
		}
		finally {
			try {
				if (pst != null){ 
					pst.close(); 
				}
				if (rs != null){ 
					rs.close(); 
				}
			} catch(SQLException excep) {
	        	logger.error("ERROR CERRANDO PreparedStatements y ResultSet",excep);
	        }
		}
		logger.info("############## Fin estaEnProcesoDistribucion");
		return resultado;
	}
	
	/**
	 * Metodo que devuelve el numero de autorización dado el codigo de institucion y un like por 'autorizaci'
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	/*
	public static int getCodNumeroAutorizacionAtributosSolicitud (Connection con, int codigoInstitucion)
	{
		String consultaStr="select codigo from atributos_solicitud where upper(nombre) like upper('%autorizaci%') and institucion=?";
		try
		{
			PreparedStatementDecorator consultaStm= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultaStm.setInt(1, codigoInstitucion);
			ResultSetDecorator resultado=consultaStm.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("codigo") ;
			}
			else
			{
				return ConstantesBD.codigoNuncaValido;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el atributo de la solicitud de numero de autorizacion : "+e);
			return ConstantesBD.codigoNuncaValido;
		}
	}
	*/
	/**
	 * Metodo que devuelve la fecha de apertura de la cuenta
	 * @param con Connection
	 * @param idCuenta int
	 * @author jarloc
	 * @return fecha String
	 * @see com.princetonsa.dao.UtilidadValidacionDao#getFechaAperturaCuenta (Connection , int ) 
	 */
	public static String obtenerFechaAperturaCuenta (Connection con, int idCuenta)
	{
		String consultaStr="SELECT to_char(fecha_apertura,'yyyy-mm-dd') AS fecha_apertura FROM cuentas WHERE id = ?";
		String resultadoR="";
		try
		{
			PreparedStatementDecorator consultaStm= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultaStm.setInt(1, idCuenta);
			ResultSetDecorator resultado=new ResultSetDecorator(consultaStm.executeQuery());
			if(resultado.next())
			{
				resultadoR=resultado.getString("fecha_apertura") ;
			}
			resultado.close();
			consultaStm.close();
		}
		catch (SQLException e)
		{
			logger.error("Error consultando la fecha de apertura de la cuenta: "+e);
		}
		return resultadoR;
	}

	/**
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public static boolean existeCuentaCobro(Connection con, double cuentaCobro, int institucion) 
	{
		String consultaStr="SELECT numero_cuenta_cobro AS cuentaCobro from cuentas_cobro where numero_cuenta_cobro = ? and institucion=?";
		boolean resultado=false;
		try
		{
			PreparedStatementDecorator consultaStm= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultaStm.setDouble(1, cuentaCobro);
			consultaStm.setInt(2,institucion);
			ResultSetDecorator rs=new ResultSetDecorator(consultaStm.executeQuery());
			resultado= rs.next();
			rs.close();
			consultaStm.close();
		}
		catch (SQLException e)
		{
			logger.error("Error consultando la existencia de la cuenta de cobro: "+e);
		}
		return resultado;
	}
	
	/**
	 * @param con
	 * @param mes
	 * @param anio
	 * @param Institucion
	 * @return
	 */
	public static boolean existeCierreMensual(Connection con, int mes, int anio, int Institucion) 
	{
		String consultaStr="SELECT codigo from cierres_saldos where mes_cierre=? and anio_cierre=? and institucion=? and tipo_cierre=?";
		try
		{
			PreparedStatementDecorator consultaStm= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultaStm.setInt(1,mes);
			consultaStm.setInt(2,anio);
			consultaStm.setInt(3,Institucion);
			consultaStm.setString(4,ConstantesBD.codigoTipoCierreMensualStr);
			ResultSetDecorator rs=new ResultSetDecorator(consultaStm.executeQuery());
			return rs.next();
		}
		catch (SQLException e)
		{
			logger.error("Error consultando la existencia de la cuenta de cobro: "+e);
		}
		return false;
	}

	/**
	 * @param con
	 * @param mes
	 * @param anio
	 * @param institucion
	 * @return
	 */
	public static boolean fechaMenorIgualAFechaCierreSalodsIniciales(Connection con, int mes, int anio, int institucion) 
	{
		String consultaStr="SELECT mes_cierre as mescierre,anio_cierre as aniocierre from cierres_saldos where institucion=? and tipo_cierre=?";
		try
		{
			PreparedStatementDecorator consultaStm= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultaStm.setInt(1,institucion);
			consultaStm.setString(2,ConstantesBD.codigoTipoCierreSaldoInicialStr);
			ResultSetDecorator rs=new ResultSetDecorator(consultaStm.executeQuery());
			if(rs.next())
			{
				if(anio<rs.getInt("aniocierre"))
				{
					return true;
				}
				else if(rs.getInt("aniocierre")==anio)
				{
					if(mes<=rs.getInt("mescierre"))
					{
						return true;
					}
				}
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando fechaMenorIgualAFechaCierreSalodsIniciales: "+e);
		}
		return false;
	}
	
	/**
	 * Método que indica si la cama ha sido o no ocupada en el rango de fecha y hora igual o mayor al entregado 
	 * @param con
	 * @param fecha
	 * @param hora
	 * @param codigoAxiomaCama
	 * @return
	 */
	public static boolean esCamaOcupadaRangoFechaHoraMayorIgualDado(Connection con, String fecha, String hora, int codigoAxiomaCama) 
	{
		String consultaStr=	"SELECT fecha_asignacion, hora_asignacion FROM traslado_cama WHERE " +
										"((fecha_asignacion>?) or (fecha_asignacion=? AND hora_asignacion>=?)) and codigo_nueva_cama=?";
		try
		{
			PreparedStatementDecorator consultaStm= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultaStm.setString(1,fecha);
			consultaStm.setString(2,fecha);
			consultaStm.setString(3,hora);
			consultaStm.setInt(4,codigoAxiomaCama);
			ResultSetDecorator rs=new ResultSetDecorator(consultaStm.executeQuery());
			return rs.next();
		}
		catch (SQLException e)
		{
			logger.error("Error consultando si la cama a sido ocupada en una fecha posterior: "+e);
		}
		return false;
	}
	
	
	
	/**
	 * Método que me indica si el paciente tiene un traslado posterior a la fecha - hora de uno nuevo
	 * @param con
	 * @param fecha
	 * @param hora
	 * @param codigoPaciente
	 * @return
	 */
	public static boolean pacienteTieneTrasladoRangoFechaHoraMayorIgualDado(Connection con, String fecha, String hora, int codigoPaciente) 
	{
		String consultaStr=	"SELECT fecha_asignacion, hora_asignacion FROM traslado_cama WHERE ((fecha_asignacion>?) or (fecha_asignacion=? AND hora_asignacion>=?)) and cuenta=?";
		try
		{
			PreparedStatementDecorator consultaStm= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultaStm.setDate(1,Date.valueOf(fecha));
			consultaStm.setDate(2,Date.valueOf(fecha));
			consultaStm.setString(3,hora);
			consultaStm.setInt(4,codigoPaciente);
			ResultSetDecorator rs=new ResultSetDecorator(consultaStm.executeQuery()); 
			return rs.next();
		}
		catch (SQLException e)
		{
			logger.error("Error consultando si un paciente tiene traslado posterior a una fecha - hora dada: "+e);
		}
		return false;
	}
	/**
	 * Método que verifica si una cuenta es de hospitalización
	 * y si tiene un estado diferente o igual al especificado
	 * @param con
	 * @param idCuenta
	 * @param estado
	 * @param diferente
	 * @param institucion
	 * @return
	 */
	public static boolean esCuentaHospitalizacion(Connection con,int idCuenta,int estado,boolean diferente,int institucion)
	{
		try
		{
			String consulta="SELECT c.id " +
					"FROM cuentas c " +
					"INNER JOIN ingresos i on(i.id=c.id_ingreso) " +
					"WHERE " +
					"c.via_ingreso="+ConstantesBD.codigoViaIngresoHospitalizacion+" AND ";
			if(diferente)
				consulta+="c.estado_cuenta!="+estado+" AND ";
			else
				consulta+="c.estado_cuenta="+estado+" AND ";
			consulta+="c.id=? AND i.institucion=?";
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,idCuenta);
			pst.setInt(2,institucion);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return true;
			else
				return false;
		}
		catch(SQLException e)
		{
			logger.error("Error en esCuentaHospitalizacionAbierta de SqlBaseUtilidadValidacionDao: "+e);
			return false;
		}
	}

	/**
	 * Método que verifica si una cuenta es de hospitalización
	 * y si tiene un estado diferente o igual al especificado
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static boolean esCuentaHospitalizacion(Connection con,String idCuenta)
	{
		try
		{
			String consulta="SELECT c.id " +
					"FROM cuentas c,ingresos i " +
					"WHERE i.id=c.id_ingreso AND " +
					"c.via_ingreso="+ConstantesBD.codigoViaIngresoHospitalizacion+" AND c.id=? ";
			
			logger.info("\n\n esCuentaHospitalizacion--> "+consulta+" "+idCuenta);
			
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,idCuenta);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return true;
			else
				return false;
		}
		catch(SQLException e)
		{
			logger.error("Error en esCuentaHospitalizacionAbierta de SqlBaseUtilidadValidacionDao: "+e);
			return false;
		}
	}
	
	
	/**
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public static boolean cuentaCobroAjustesPendientes(Connection con, double cuentaCobro, int institucion) 
	{
		try
		{
			String consulta="SELECT count(1) as ajustes from ajustes_empresa where estado = ? and cuenta_cobro=? and institucion=?";
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,ConstantesBD.codigoEstadoCarteraGenerado);
			pst.setDouble(2,cuentaCobro);
			pst.setInt(3,institucion);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				return (rs.getInt("ajustes")>0);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error al consultar si la cuentaCobro tiene ajustes pendientes de SqlBaseUtilidadValidacionDao: "+e);
		}
		return false;
	}

	/**
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public static boolean cuentaCobroConFactEnAjustesPendiente(Connection con, double cuentaCobro, int institucion) 
	{
		try
		{
			String consulta="select count(1) as numFacturas from facturas f inner join cuentas_cobro c on(c.numero_cuenta_cobro=f.numero_cuenta_cobro) inner join ajus_fact_empresa afe on f.codigo=afe.factura inner join ajustes_empresa ae on ae.codigo=afe.codigo  where ae.estado=? and c.numero_cuenta_cobro=? and f.institucion=?";
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,ConstantesBD.codigoEstadoCarteraGenerado);
			pst.setDouble(2,cuentaCobro);
			pst.setInt(3,institucion);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				return (rs.getInt("numFacturas")>0);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error a consultar si la cuenta de cobro esta ligada a facturas con ajustes pendientes SqlBaseUtilidadValidacionDao: "+e);
		}
		return false;
	}
	/**
	 * metodo para obtener el estado de un ajuste especifico
	 * @param con Connection
	 * @param codigo int, código del ajuste
	 * @param institucion int, código de la institución
	 * @return String, con el estado del ajuste y la descripcion
	 * separados por un @
	 * @author jarloc
	 */
	public static String obtenerEstadoAjusteCartera(Connection con, double codigo,int institucion)
	{
	    String consulta=" SELECT " +
					    		"a.estado as estado," +
					    		"e.descripcion as descripcion " +
					    		"from ajustes_empresa a " +
					    		"inner join estados_cartera e on(e.codigo=a.estado) where a.codigo=? and institucion=?";
	    String dato="";
	    try
	    {
	        PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        pst.setDouble(1,codigo);
	        pst.setInt(2,institucion);
	        ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
	        while(rs.next())
	        {
	            dato=rs.getInt("estado")+"@"+rs.getString("descripcion");	            
	        }	        
	    }
	    catch(SQLException e)
		{
			logger.error("Error en obtenerEstadoAjusteCartera de SqlBaseUtilidadValidacionDao: "+e);			 
		}
	    return dato;
	}

	/**
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public static boolean esAjusteReversado(Connection con, double codigoAjuste) 
	{
		String consulta="SELECT * FROM ajustes_empresa WHERE codigo =? AND ajuste_reversado=?";
		PreparedStatementDecorator pst;
		try {
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,codigoAjuste);
	        pst.setBoolean(2,true);
	        ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public static boolean existeDistribucionAjusteNivelFacturas(Connection con, double codigoAjuste) 
	{
		String consulta="SELECT * from ajus_fact_empresa where codigo=?";
		PreparedStatementDecorator pst;
		try {
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,codigoAjuste);
	        ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param con
	 * @param codigoAjuste
	 * @param factura
	 * @return
	 */
	public static boolean existeDistribucionAjusteNivelServicios(Connection con, double codigoAjuste, int factura) 
	{
		String consulta="SELECT * from ajus_det_fact_empresa where codigo=? and factura=?";
		PreparedStatementDecorator pst;
		try {
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,codigoAjuste);
			pst.setInt(2,factura);
	        ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Método para verificar si un centro de costo es SubAlmacen
	 * @param con Conexión con la BD
	 * @param centroCosto codigo del centro de costo a verificar
	 * @return true si el centro de costo es subalmacen
	 */
	public static boolean esCentroCostoSubalmacen(Connection con, int centroCosto)
	{
		try
		{
			String consulta="SELECT count(1) as numResultados FROM centros_costo WHERE codigo=? AND tipo_area="+ConstantesBD.codigoTipoAreaSubalmacen;
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setInt(1, centroCosto);
			ResultSetDecorator resultado=new ResultSetDecorator(statement.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("numResultados")>0?true:false;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error verificando si el centro de costo pertenece a subalmacen : "+e);
		}
		return false;
	}
	
	/**
	 * Utilidad que verifica si una solicitud tiene artículos sin cantidad
	 * @param con Conexión con la BD
	 * @param numeroSolicitud Solicitud a verificar
	 * @return true si la solicitud tiene artículos sin cantidad, false de lo contrario
	 */
	public static boolean esSolicitudSinCantidad(Connection con, int numeroSolicitud, String consultarCantidadesStr)
	{
		try
		{
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(consultarCantidadesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setInt(1, numeroSolicitud);
			ResultSetDecorator resultado=new ResultSetDecorator(statement.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("numResultados")>0?true:false;
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error verificando las cantidades de los artículos de la solicitud "+numeroSolicitud+" : "+e);
			return false;
		}
	}

	/**
	 * Metodo que indica si ya existe la parametrizacion de tipos de asocios
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static boolean esTiposAsociosParametrizados(Connection con, int institucion)
	{
	    try
	    {
	        String verificarStr="SELECT count(1) as contador FROM tipos_asocio WHERE institucion=?";
	        PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(verificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,institucion);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{    
			    if(rs.getInt("contador")>0)
			        return true;
			    else
			        return false;
			}    
			return false;
	    }
	    catch (SQLException e)
	    {
	        logger.error("Error en esTiposAsociosParametrizados de SqlBaseUtilidadValidacionDao: "+e);
			return false;
	    }
	}

	/**
	 * @param con
	 * @param consecutivoCaja
	 * @return
	 */
	public static boolean cajaUtilizadaEnCajerosCaja(Connection con, int consecutivoCaja) 
	{
		try
	    {
	        String verificarStr="SELECT count(1) as cajas from cajas_cajeros where caja=?";
	        PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(verificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,consecutivoCaja);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{    
			    if(rs.getInt("cajas")>0)
			        return true;
			}    
			return false;
	    }
	    catch (SQLException e)
	    {
	        logger.error("Error en cajaUtilizadaEnCajerosCaja de SqlBaseUtilidadValidacionDao: "+e);
			return true;
	    }
	}

	/**
	 * @param con
	 * @param consecutivoCaja
	 * @return
	 */
	public static boolean cajaUtilizadaEnRecibosCaja(Connection con, int consecutivoCaja) 
	{
		try
	    {
	        String verificarStr="SELECT count(1) as cajas from recibos_caja where caja=?";
	        PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(verificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,consecutivoCaja);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{    
			    if(rs.getInt("cajas")>0)
			        return true;
			}    
			return false;
	    }
	    catch (SQLException e)
	    {
	        logger.error("Error en cajaUtilizadaEnRecibosCaja de SqlBaseUtilidadValidacionDao: "+e);
			return true;
	    }
	}

	/**
	 * @param con
	 * @param consecutivoEntdadFinanciera
	 * @return
	 */
	public static boolean entidadFinanceraUtilizadaEnTarjetasFinancieras(Connection con, int consecutivoEntdadFinanciera) 
	{
		try
	    {
	        String verificarStr="SELECT count(1) as entfin from tarjetas_financieras where entidad_financiera=?";
	        PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(verificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,consecutivoEntdadFinanciera);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{    
			    if(rs.getInt("entfin")>0)
			        return true;
			}    
			return false;
	    }
	    catch (SQLException e)
	    {
	        logger.error("Error en entidadFinanceraUtilizadaEnTarjetasFinancieras de SqlBaseUtilidadValidacionDao: "+e);
			return true;
	    }
	}

	/**
	 * @param con
	 * @param consecutivoCaja
	 * @param logginUsuario
	 * @return
	 */
	public static boolean cajaCajeroUtilizadaEnRecibosCaja(Connection con, int consecutivoCaja, String logginUsuario) 
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
	    {
			String verificarStr="SELECT count(1) AS cantidad FROM recibos_caja WHERE caja = ? and usuario = ?";
			ps = con.prepareStatement(verificarStr);
			ps.setInt(1, consecutivoCaja);
			ps.setString(2, logginUsuario);
			rs = ps.executeQuery();
			if(rs.next())
			{    
			    if(rs.getInt("cantidad") > 0)
			    {
			        return true;
			    }
			}
	    }
	    catch (SQLException e)
	    {
	        logger.error("Error en cajaCajeroUtilizadaEnRecibosCaja de SqlBaseUtilidadValidacionDao: "+e);
	    }
		finally
		{
			try
			{
				if(ps!=null)
				{
					ps.close();
				}
				if(rs!=null)
				{
					rs.close();
				}
			}
			catch (SQLException e2)
			{
				logger.error("Error en cajaCajeroConTurno de SqlBaseUtilidadValidacionDao: "+e2);
			}
		}
		return false;
	}
	
	/**
	 * @param con
	 * @param consecutivoCaja
	 * @param logginUsuario
	 * @return
	 */
	public static boolean cajaCajeroConTurno(Connection con, int consecutivoCaja, String logginUsuario) 
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
	    {
	        String sql = "SELECT count (*) AS cantidad FROM tesoreria.turno_de_caja WHERE turno_de_caja.caja = ? AND turno_de_caja.cajero = ?";
	        ps = con.prepareStatement(sql);
	        ps.setInt(1, consecutivoCaja);
	        ps.setString(2, logginUsuario);
	        rs = ps.executeQuery();
	        if(rs.next())
	        {
	        	if(rs.getInt("cantidad") > 0)
	        	{
	        		return true;
	        	}
	        }
	    }
	    catch (SQLException e)
	    {
	        logger.error("Error en cajaCajeroConTurno de SqlBaseUtilidadValidacionDao: "+e);
	    }
		finally
		{
			try
			{
				if(ps!=null)
				{
					ps.close();
				}
				if(rs!=null)
				{
					rs.close();
				}
			}
			catch (SQLException e2)
			{
				logger.error("Error en cajaCajeroConTurno de SqlBaseUtilidadValidacionDao: "+e2);
			}
		}
		return false;
	}

	/**
	 * @param con
	 * @param consecutivoTarjeta
	 * @return
	 */
	public static boolean terjetaFinancieraUtilizadaEnMovimientosTarjetas(Connection con, int consecutivoTarjeta) 
	{
		try
	    {
	        String verificarStr=" SELECT count(1) as cantidad from movimientos_tarjetas where codigo_tarjeta = ?";
	        PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(verificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,consecutivoTarjeta);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{    
			    if(rs.getInt("cantidad")>0)
			        return true;
			}    
			return false;
	    }
	    catch (SQLException e)
	    {
	        logger.error("Error en terjetaFinancieraUtilizadaEnMovimientosTarjetas de SqlBaseUtilidadValidacionDao: "+e);
			return true;
	    }
	}

	/**
	 * @param con
	 * @param formaPago
	 * @return
	 */
	public static boolean formaPagoUtilizadaEnRecibosCaja(Connection con, int formaPago)
	{
		try
	    {
	        String verificarStr = "SELECT count(1) AS cantidad FROM detalle_pagos_rc WHERE forma_pago = ?";
	        PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(verificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,formaPago);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{    
			    if(rs.getInt("cantidad")>0)
			        return true;
			    //******************INICIO TAREA 65225***************************
			    //Según la Tarea 65225. Consultar que la Forma de Pago no este en la Tabla det_traslados_caja
			    else
			    {
			    	verificarStr = "SELECT count(1) AS cantidad FROM det_traslados_caja WHERE forma_pago = ?";
			    	pst =  new PreparedStatementDecorator(con.prepareStatement(verificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1,formaPago);
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						if(rs.getInt("cantidad")>0)
					        return true;
						else
							return false;
					}
					else
						return false;
			    }
			    //*******************FIN TAREA 65225****************************
			}
			else
				return false;
	    }
	    catch (SQLException e)
	    {
	        logger.error("Error en formaPagoUtilizadaEnRecibosCaja de SqlBaseUtilidadValidacionDao: "+e);
			return true;
	    }
	}
    
    /**
     * @param con
     * @param numeroReciboCaja
     * @param institucion
     * @return
     */
    public static String existeReciboCaja(Connection con, String numeroReciboCaja, int institucion,int codigoCentroAtencion)
    {
    	String resultado=ConstantesBD.codigoNuncaValido+"";
        String consulta="SELECT numero_recibo_caja from recibos_caja where consecutivo_recibo=? and institucion =? "+(codigoCentroAtencion>0?" and centro_atencion="+codigoCentroAtencion+"":"");
		PreparedStatementDecorator pst;
		try {
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,numeroReciboCaja);
			pst.setInt(2,institucion);
	        ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
	        if(rs.next())
	        	resultado=rs.getString(1);
	        rs.close();
	        pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultado;
    }
    
    
    /**
     * Dice si esta parametrizado la cobertura de los accidentees de transito
     * @param con
     * @param institucion
     * @return
     */
    public static boolean existeParametrizacionCobreturaAccTransito(Connection con, int institucion)
    {
        String consulta="SELECT * from cob_accidentes_transito WHERE institucion =?";
		PreparedStatementDecorator pst;
		try 
		{
			
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,institucion);
	        ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			return rs.next();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
    }
    /**
     * Dice si esta parametrizado el salario minimo
     * @param con
     * @param institucion
     * @return
     */
    public static boolean existeParametrizacionSalarioMinimo(Connection con)
    {
        String consulta="SELECT * from salario_minimo";
		PreparedStatementDecorator pst;
		try 
		{
			
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			return rs.next();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
    }
    
    /**
	 * Método que indica si una cuenta o subcuenta tiene requisistos paciente por cuenta o subcuenta(distibucion) pendientes
	 * de entregar.
	 * @param con
	 * @param codigoConvenio
	 * @param codigoInstitucion
	 * @param codigoSubcuentaOCuenta
	 * @return
	 */
	public static boolean faltanRequisitosPacienteXCuenta( Connection con,  String codigoSubcuentaOCuenta)
	{
		String consultaStr="";
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
		{
			consultaStr="SELECT count(1) as faltantes FROM requisitos_pac_subcuenta WHERE cumplido="+ValoresPorDefecto.getValorFalseParaConsultas()+" AND subcuenta= "+codigoSubcuentaOCuenta;
			pst =  con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs=pst.executeQuery();
			if(rs.next()){
				if(Integer.parseInt(rs.getInt("faltantes")+"")>0)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR faltanRequisitosPacienteXCuenta",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR faltanRequisitosPacienteXCuenta", e);
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
        return true;
	}
	
	/**
	 * Método usado para verificar si un médico se encuentra inactivo
	 * 
	 * @param con
	 * @param codigoMedico
	 * @param institucion
	 * @return
	 */
	public static boolean estaMedicoInactivo(Connection con,int codigoMedico,int institucion)
	{
		try
		{
			boolean resp=false;
			String estaMedicoInactivoStr="SELECT count(1) AS resultado FROM medicos_inactivos WHERE codigo_medico=? AND codigo_institucion=?";
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(estaMedicoInactivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoMedico);
			pst.setInt(2,institucion);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				if(rs.getInt("resultado")>0)
				{	
					resp= true;
				}
			}
			return resp;
		}
		catch(SQLException e)
		{
			logger.error("Error en estaMedicoInactivo de SqlBaseUtilidadValidacionDao: "+e);
			return false;
		}
	}
	
	/**
	 * metodo que indica si un servicio tiene o no excepcion 
	 * @param con
	 * @param codigoConvenioStr
	 * @param codigoServicioStr
	 * @return
	 */
	public static boolean esServicioConExcepcion(Connection con, String codigoConvenioStr, String codigoServicioStr)
	{
	    try
		{
			boolean resp=false;
			String esServicioConExcepcionStr="SELECT count(1) AS resultado FROM excepciones_servicios e, contratos c WHERE  e.contrato= c.codigo AND e.servicio =? AND c.convenio=?";
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(esServicioConExcepcionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,codigoServicioStr);
			pst.setString(2,codigoConvenioStr);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				if(rs.getInt("resultado")>0)
					resp= true;
			}
			return resp;
		}
		catch(SQLException e)
		{
			logger.error("Error en  esServicioConExcepcion de SqlBaseUtilidadValidacionDao: "+e);
			return false;
		}
	}
	
	/**
	 * metodo que indica si un servicio es pos o no
	 * @param con
	 * @param codigoServicioStr
	 * @return
	 */
	public static boolean esServicioPos(Connection con, String codigoServicioStr)
	{
	    try
		{
			boolean resp=false;
			String esServicioPosStr="SELECT espos FROM servicios  WHERE  codigo=? ";
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(esServicioPosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,codigoServicioStr);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				resp= rs.getBoolean("espos");
			return resp;
		}
		catch(SQLException e)
		{
			logger.error("Error en  esServicioConExcepcion de SqlBaseUtilidadValidacionDao: "+e);
			return false;
		}
	}
	
	/**
	 * metodo que indica si un servicio esta cubierto por el convenio
	 * @param con
	 * @param codigoConvenioStr
	 * @param codigoServicioStr
	 * @return
	 */
	public static boolean esServicioCubiertoXConvenio(Connection con, String codigoConvenioStr, String codigoServicioStr)
	{
	    boolean esServicioConExcepcion= esServicioConExcepcion(con, codigoConvenioStr, codigoServicioStr);
		boolean esServicioPos= esServicioPos(con, codigoServicioStr);
		boolean esConvenioParticular=false;
		
		if(codigoConvenioStr.equals(ConstantesBD.codigoConvenioParticular+""))
		   esConvenioParticular=true;
		
		if(esServicioPos)
		{
		    if(esServicioConExcepcion)
		    {
		        // En este caso no importa si es Convenio Particular  o NO
		        return false;
		    }
		    else
		    {
		        return true;
		    }
		}
		else
		{
		    if(esServicioConExcepcion)
		    {
		        if(esConvenioParticular)
		            return false;
		        else 
		            return true;
		    }
		    else
		    {
		        if(esConvenioParticular)
		            return true;
		        else
		            return false;
		    }
		}
	}
	
	/**
	 * Metodo que verifica la existencia de los pagos parciales realizados por un paciente de una factura
	 * @param con
	 * @param codigoInstitucion
	 * @param codigoEstadoPago
	 * @param codigoAxiomaFactura
	 * @return
	 */
	public static boolean existenPagosParcialesFacturaPaciente(Connection con,  int codigoInstitucion, int codigoEstadoPago, String codigoAxiomaFactura, String consultaStr)
	{
	    try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoInstitucion);
			pst.setInt(2, codigoEstadoPago);
			pst.setString(3, codigoAxiomaFactura);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{    
			    if(rs.getDouble("valor")>0)
			        return true;
			    else
			        return false;
			}    
			return false;
		}
		catch(SQLException e)
		{
			logger.error("Error en  existenPagosParcialesFacturaPaciente de SqlBaseUtilidadValidacionDao: "+e);
			return false;
		}
	}
	
	/**
	 * Método para saber si un médico es de una especialidad específica
	 * @param con
	 * @param codigoMedico
	 * @param codigoEspecialidad
	 * @return true si el médico es de la especialidad mandada como parámetro sino retorna false 
	 */
	public static boolean esMedicoEspecialidad (Connection con, int codigoMedico, int codigoEspecialidad)
	{
		String consultaStr= "SELECT count(*) AS num " +
									   			"FROM especialidades_medicos " +
									   				"WHERE codigo_medico=? " +
									   					"AND codigo_especialidad=? " +
									   					"AND activa_sistema="+ValoresPorDefecto.getValorTrueParaConsultas();
		
		try
		{
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoMedico);
			ps.setInt(2, codigoEspecialidad);
			
			ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());
			
			if(resultado.next())
			{
				if(resultado.getInt("num") > 0)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			return false;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta si un médico tiene una especialidad específica: SqlBaseUtilidadValidacionDao "+e.toString());
			return false;
		}
	}
	
	/**
	 * Método que me indica si una peticion yçtiene asociada una solicitud 
	 * @param con Conexión con la base de Datos
	 * @param codigoPeticion Petición a evaluar
	 * @return int -1 En caso de error, 0 en caso de no existir orden asociada, o el número de la orden asociada a la petición
	 */
	public static int[] estaPeticionAsociada(Connection con, int codigoPeticion)
	{
		try
		{
			PreparedStatementDecorator validarPeticion= new PreparedStatementDecorator(con.prepareStatement("SELECT s.numero_solicitud AS numero_solicitud, s.consecutivo_ordenes_medicas AS numero_orden FROM solicitudes_cirugia sc INNER JOIN solicitudes s on(s.numero_solicitud=sc.numero_solicitud) WHERE sc.codigo_peticion=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			validarPeticion.setInt(1, codigoPeticion);
			ResultSetDecorator resultado=new ResultSetDecorator(validarPeticion.executeQuery());
			if(resultado.next())
			{
				int numeroOrden=resultado.getInt("numero_orden");
				int numeroSolicitud=resultado.getInt("numero_solicitud");
				int[] retorno={numeroOrden, numeroSolicitud};
				return retorno;
			}
			else
			{
				int[] retorno={0, 0};
				return retorno;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error validando si existe solicitud de cirugía asociada a la petición "+codigoPeticion+": "+e);
			int[] retorno={-1, -1};
			return retorno;
		}
	}

    
    /**
     * metodo que indica si una hoja qx ya tiene una solicitud asociada dado el numero de solicitud
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static boolean esHojaQxAsociadaSolicitud(Connection con, String numeroSolicitud)
    {
        try
        {
            int resp=0;
            String esHojaQxAsociadaSolicitudStr="SELECT COUNT(1) as contador FROM hoja_quirurgica WHERE numero_solicitud = ? ";
            PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(esHojaQxAsociadaSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            pst.setString(1,numeroSolicitud);
            
            ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
            if(rs.next())
                resp= rs.getInt("contador");
            if(resp>0)
                return true;
            else    
                return false;
        }
        catch(SQLException e)
        {
            logger.error("Error en  esHojaQxAsociadaSolicitud de SqlBaseUtilidadValidacionDao: "+e);
            return false;
        }
    }
    
    /**
     * metodo que indica si una hoja anestesia ya tiene una solicitud asociada dado el numero de solicitud
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static boolean esHojaAnestesiaAsociadaSolicitud(Connection con, String numeroSolicitud)
    {
        try
        {
            int resp=0;
            String esHojaAnestesiaAsociadaSolicitudStr="SELECT COUNT(1) as contador FROM hoja_anestesia WHERE numero_solicitud = ? ";
            PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(esHojaAnestesiaAsociadaSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            pst.setString(1,numeroSolicitud);
            
            ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
            if(rs.next())
                resp= rs.getInt("contador");
            if(resp>0)
                return true;
            else    
                return false;
        }
        catch(SQLException e)
        {
            logger.error("Error en  esHojaAnestesiaAsociadaSolicitud de SqlBaseUtilidadValidacionDao: "+e);
            return false;
        }
    }
    
    /**
     * metodo que indica si una nota recuperacion ya tiene una solicitud asociada dado el numero de solicitud
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static boolean esNotaRecuperacionAsociadaSolicitud(Connection con, String numeroSolicitud)
    {
        try
        {
            int resp=0;
            String esNotaRecuperacionAsociadaSolicitudStr="SELECT COUNT(1) as contador FROM notas_recuperacion_general WHERE numero_solicitud = ? ";
            PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(esNotaRecuperacionAsociadaSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            pst.setString(1,numeroSolicitud);
            
            ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
            if(rs.next())
                resp= rs.getInt("contador");
            if(resp>0)
                return true;
            else    
                return false;
        }
        catch(SQLException e)
        {
            logger.error("Error en  esNotaRecuperacionAsociadaSolicitudStr de SqlBaseUtilidadValidacionDao: "+e);
            return false;
        }
    }
    
    /**
     * metodo que indica si una nota general enfermeria ya tiene una solicitud asociada dado el numero de solicitud
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static boolean esNotaGeneralEnfAsociadaSolicitud(Connection con, String numeroSolicitud)
    {
        try
        {
            int resp=0;
            String esNotaGeneralEnfAsociadaSolicitudStr="SELECT COUNT(1) as contador FROM notas_enfermeria WHERE numero_solicitud = ? ";
            PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(esNotaGeneralEnfAsociadaSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            pst.setString(1,numeroSolicitud);
            
            ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
            if(rs.next())
                resp= rs.getInt("contador");
            if(resp>0)
                return true;
            else    
                return false;
        }
        catch(SQLException e)
        {
            logger.error("Error en  esNotaRecuperacionAsociadaSolicitudStr de SqlBaseUtilidadValidacionDao: "+e);
            return false;
        }
    }
    
    /**
     * metodo que indica si un consumo materiales ya tiene una solicitud asociada dado el numero de solicitud
     * @param con
     * @param numeroSolicitud
     * @return
     */
    public static boolean esConsumoMaterialesAsociadaSolicitud(Connection con, String numeroSolicitud)
    {
        try
        {
            int resp=0;
            String esConsumoMaterialesAsociadaSolicitudStr="SELECT COUNT(1) as contador FROM materiales_qx WHERE numero_solicitud = ? ";
            PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(esConsumoMaterialesAsociadaSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            pst.setString(1,numeroSolicitud);
            
            ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
            if(rs.next())
                resp= rs.getInt("contador");
            if(resp>0)
                return true;
            else    
                return false;
        }
        catch(SQLException e)
        {
            logger.error("Error en  esConsumoMaterialesAsociadaSolicitud de SqlBaseUtilidadValidacionDao: "+e);
            return false;
        }
    }
    
    
    /**
     * Indica si la ocupacion de un profesional de la salud esta parametrizada como ocupaciones que realizan cirugias 
     * @param con
     * @param codigoOcupacion
     * @param codigoInstitucion
     * @return
     */
    public static boolean esOcupacionQueRealizaCx(Connection con, int codigoOcupacion, int codigoInstitucion)
    {
        try
        {
            int resp=0;
            String esOcupacionQueRealizaCxStr="SELECT COUNT(1) as contador FROM ocupa_realizan_qx_inst WHERE ocupacion = ?  and institucion=? ";
            PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(esOcupacionQueRealizaCxStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            pst.setInt(1,codigoOcupacion);
            pst.setInt(2, codigoInstitucion);
            ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
            if(rs.next())
                resp= rs.getInt("contador");
            if(resp>0)
                return true;
            else    
                return false;
        }
        catch(SQLException e)
        {
            logger.error("Error en  esOcupacionQueRealizaCx de SqlBaseUtilidadValidacionDao: "+e);
            return false;
        }
    }
    
	/**
	 * Utilidad para verificar la existensia de la hoja de anestesia
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean existeHojaAnestesia(Connection con, int numeroSolicitud)
	{
		try
		{
			String consulta="SELECT count(1) AS numResultados FROM hoja_anestesia WHERE numero_solicitud=?";
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setInt(1, numeroSolicitud);
			ResultSetDecorator resultado=new ResultSetDecorator(statement.executeQuery());
			if(resultado.next())
			{
				int numResultados=resultado.getInt("numResultados");
				return numResultados>0;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando la exuistensia de la hoja de anestesia: "+e);
			return false;
		}
		return false;
	}
	
	/**
	 * Utilidad para verificar la existensia de la hoja obstétrica para un paciente cargado
	 * @param con
	 * @param codigoPaciente
	 * @return true si existe sino retorna false
	 */
	public static boolean existeHojaObstetrica(Connection con, int codigoPaciente)
	{
		try
		{
			String consulta="SELECT count(1) AS numResultados FROM hoja_obstetrica WHERE paciente=?";
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setInt(1, codigoPaciente);
			ResultSetDecorator resultado=new ResultSetDecorator(statement.executeQuery());
			if(resultado.next())
			{
				int numResultados=resultado.getInt("numResultados");
				return numResultados>0;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando la existencia de la hoja obstétrica para un paciente (SqlBaseUtilidadValidacionDao): "+e);
			return false;
		}
		return false;
	}
	
	/**
	 * Método que verifica si la cuenta tiene solicitudes de un tipo
	 * especifico, con un estado medico o de facturacion específico
	 * @param con
	 * @param idCuenta
	 * @param tipo
	 * @param estadoMedico
	 * @param estadoFacturacion
	 * @return
	 */
	public static boolean haySolicitudesEnCuenta(Connection con,int idCuenta,int tipo,int estadoMedico)
	{
		try
		{
			String consulta = "SELECT count(1) AS resultado FROM solicitudes " +
				"WHERE cuenta="+idCuenta;
			
			//TIPO SOLICITUD*******************************
			if(tipo>0)
				consulta += " AND tipo = "+tipo;
			
			//CODIGO ESTADO HISTORIA CLINICA**********************
			if(estadoMedico>0)
				consulta += " AND estado_historia_clinica= "+estadoMedico;
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				if(rs.getInt("resultado")>0)
					return true;
				else
					return false;
			else
				return false;
		}
		catch(SQLException e)
		{
			logger.error("Error en haySolicitudesEnCuenta de SqlBaseUtilidadValidacionDao: "+e);
			return false;
		}
		
	}
	
	/**
	 * Método implementado para verificar si una cirugía requiere justificacion
	 * @param con
	 * @param numeroSolicitud
	 * @param servicio (cirugia)
	 * @param contrato actual de la cuenta que contiene la solicitud
	 * @return
	 */
	public static boolean cirugiaRequiereJustificacion(Connection con,int numeroSolicitud,int servicio,int contrato)
	{
		try
		{
			String consulta = "SELECT " +
				"CASE WHEN getesservicioscobrables(?,?) = 1 AND count(1) = 0 " +
				"THEN '1' ELSE '0' END AS requiereJustificacion " +
			"FROM desc_atr_sol_int_proc WHERE numero_solicitud = ? and servicio =? ";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,servicio);
			pst.setInt(2,contrato);
			pst.setInt(3,numeroSolicitud);
			pst.setInt(4,servicio);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				return UtilidadTexto.getBoolean(rs.getString("requiereJustificacion"));
			else
				return false;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en cirugiaRequiereJustificacion de SqlBaseUtilidadValidacionDao: "+e);
			return false;
		}
	}

	/**
	 * Metodo para saber si una solcicitud de Procedimientos es multiple.
	 * @param con
	 * @param numeroSolicitud
	 * @return Retorna True Si la solicitud es multiple de lo contrario False. 
	 */
	public static boolean esSolicitudMultiple(Connection con,int numeroSolicitud)
	{
		try
		{
			
			//esta consulta deberia verificar si la consulta tiene el campo solicitud_multiple en true
			//lo que esta haciendo es verficando si la solicitud no se ha finalizado
			String consulta = "	SELECT count(1) AS resultado			 " +
							  "		   FROM sol_procedimientos   	     " +
							  "		     	WHERE numero_solicitud = " + numeroSolicitud +
 							  "				  AND finalizada = " + ValoresPorDefecto.getValorFalseParaConsultas(); 
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery(consulta));
			
			if(rs.next())
				if(rs.getInt("resultado")>0)
					return true;
				else
					return false;
			else
				return false;
		}
		catch(SQLException e)
		{
			logger.error("Error Consultando Si la Solicitud Nro " + numeroSolicitud + " es Multiple  SqlBaseUtilidadValidacionDao: "+e);
			return false;
		}
		
	}
	
		/**
	 * Revisar Validez del contrato por fechas
	 * @param con
	 * @param cuenta
	 * @return Nombre Convenio, fecha inicio y fecha fin del contrato, cadena con representacion booleana si es valido o no
	 * Ej:
	 * 	0 - Cafesalud EPS
	 * 	1 - 01/01/2000
	 *  2 - 31/12/2006
	 *  3 - true
	 */
	public static String[] revisarValidezContrato(Connection con, int cuenta)
	{
		//consulta ya arreglada con capitacion
		String consulta="SELECT "+ 
			"getnombreconvenio(sc.convenio) AS convenio, "+ 
			"to_char(cont.fecha_inicial,'dd/mm/yyyy') AS fecha_inicial, "+ 
			"to_char(cont.fecha_final,'dd/mm/yyyy') AS fecha_final, "+ 
			"CASE WHEN CURRENT_DATE>=cont.fecha_inicial AND CURRENT_DATE<=cont.fecha_final THEN 'true' ELSE 'false' END AS vigente "+ 
			"FROM cuentas cue "+ 
			"INNER JOIN sub_cuentas sc ON(sc.ingreso = cue.id_ingreso AND sc.nro_prioridad = 1) "+
			"INNER JOIN contratos cont ON(cont.codigo=sc.contrato  ) "+ 
			"WHERE cue.id=?";
		try
		{
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			stm.setInt(1, cuenta);
			ResultSetDecorator rs=new ResultSetDecorator(stm.executeQuery());
			String[] convenio={"","","","false"};
			while(rs.next())
			{
				convenio=new String[]
				{
					rs.getString("convenio"),
					rs.getString("fecha_inicial"),
					rs.getString("fecha_final"),
					rs.getString("vigente")
				};
				if(UtilidadTexto.getBoolean(rs.getString("vigente")))
				{
					return convenio;
				}
			}
			/*
			 * No existe convenio vigente
			 * Si llega a este punto hay una ionconsistencia en la BD
			 */
			return convenio;
		}
		catch (SQLException e)
		{
			logger.error("Error consultando la validez del contrato para la cuenta "+cuenta+": "+e);
			return null;
		}
	}
	
	/**
	 * Utilidad para verificar la existencia de la hoja oftalmológica para un paciente cargado
	 * @param con
	 * @param codigoPaciente
	 * @return true si existe sino retorna false
	 */
	public static boolean existeHojaOftalmologica(Connection con, int codigoPaciente)
	{
		try
		{
			String consulta="SELECT count(1) AS numResultados FROM hoja_oftalmologica WHERE paciente=?";
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setInt(1, codigoPaciente);
			ResultSetDecorator resultado=new ResultSetDecorator(statement.executeQuery());
			if(resultado.next())
			{
				int numResultados=resultado.getInt("numResultados");
				return numResultados>0;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando la existencia de la hoja oftalmológica para un paciente (SqlBaseUtilidadValidacionDao): "+e);
			return false;
		}
		return false;
	}

	/**
	 * Verifica si esciste un monto en la BD
	 * @param con
	 * @param convenio
	 * @param viaIngreso
	 * @param tipoAfiliado
	 * @param estratoSocial
	 * @param tipoMonto
	 * @param valor
	 * @param porcentaje
	 * @return
	 */
	public static boolean existeMontoEnBD(Connection con, int convenio, int viaIngreso, String tipoAfiliado, int estratoSocial, int tipoMonto, float valor, float porcentaje, boolean activo, String fecha)
	{
		try
		{
			String consulta="SELECT count(1) as numResultados FROM montos_cobro WHERE convenio = ? AND via_ingreso = ? AND tipo_afiliado = ? AND estrato_social = ? AND tipo_monto = ? AND activo = ? AND vigencia_inicial = ? ";
			
			if(valor!=-1)
				consulta+=" AND valor="+valor;
			else
				consulta+=" AND valor IS NULL";
			
			if(porcentaje!=-1)
				consulta+=" AND porcentaje="+porcentaje;
			else
				consulta+=" AND porcentaje IS NULL";
			
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			stm.setInt(1, convenio);
			stm.setInt(2, viaIngreso);
			stm.setString(3, tipoAfiliado);
			stm.setInt(4, estratoSocial);
			stm.setInt(5, tipoMonto);
			stm.setBoolean(6, activo);
			stm.setString(7, UtilidadFecha.conversionFormatoFechaABD(fecha));
			ResultSetDecorator resultado=new ResultSetDecorator(stm.executeQuery());
			
			logger.info("===>Existe Monto en BD: "+consulta+" ===>Convenio: "+convenio+" ===>Via Ingreso: "+viaIngreso+" ===>Tipo Afiliado: "+tipoAfiliado+" ===>Estrato Social: "+estratoSocial+" ===>Tipo Monto: "+tipoMonto+" ===>Activo: "+activo+" ===>Fecha Vigencia Inicial:"+fecha);
			
			if(resultado.next())
			{
				
				return resultado.getInt("numResultados")>0?true:false;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando si existe el monto en la BD: "+e);
		}
		return true;
	}

	/**
	 * 
	 * @param con
	 * @param profesional
	 * @param fecha
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public static boolean seCruzaAgendaMedicoFechaHora(Connection con, int profesional, String fecha, String horaInicial, String horaFinal) 
	{
		try
		{
			String consulta="select * from agenda where activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" and codigo_medico = '"+profesional+"'  and fecha='"+UtilidadFecha.conversionFormatoFechaABD(fecha)+"' and ('"+horaInicial+"' between hora_inicio and hora_fin or '"+horaFinal+"' between hora_inicio and hora_fin)";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return true;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error en  seCruzaAgendaMedicoFechaHora (SqlBaseUtilidadValidacionDao): "+e);
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	/**
	 * metodo que indica si se debe generar recibos de caja automaticos para
	 * una via de ingreso dada
	 * @param con
	 * @param codigoViaIngreso
	 * @return
	 */
	public static boolean esReciboCajaAutomaticoViaIngresoDada(Connection con, int codigoViaIngreso)
	{
		try
		{
			String consulta="SELECT recibo_automatico AS esAutomatico FROM vias_ingreso WHERE codigo=?";
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setInt(1, codigoViaIngreso);
			ResultSetDecorator resultado=new ResultSetDecorator(statement.executeQuery());
			if(resultado.next())
				return UtilidadTexto.getBoolean(resultado.getString("esAutomatico"));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando esReciboCajaAutomaticoViaIngresoDada (SqlBaseUtilidadValidacionDao): "+e);
			return false;
		}
		return false;
	}
	
	
	/**
	 * 
	 * Método que consulta el código [pos 1] y la descripción [pos 2] del concepto tipo régimen
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param acronimoTipoRegimen
	 * @param tiposIngresoTesoreria
	 * @param esActivo
	 * @return
	 */
	public static HashMap<Object, Object> getCodigoDescripcionConceptoTipoRegimen(Connection con, int codigoInstitucion, String acronimoTipoRegimen, Vector<Object> tiposIngresoTesoreria, boolean esActivo)
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		String resp[]={"",""};
		
		try
		{
			String consulta = "SELECT DISTINCT " +
									"cit.codigo AS codigo_concepto, " +
									"cit.descripcion AS descripcion_concepto, " +
									"cit.codigo_tipo_ingreso as codigo_tipo_ingreso " +
								"FROM " +
									"conceptos_ing_tesoreria cit " +
								"WHERE " +
									"cit.institucion=? and cit.es_activo = ?";
								//"AND ctr.acronimo_tr=?";
			
			if(tiposIngresoTesoreria.size()>0)
			{
				consulta+=" AND cit.codigo_tipo_ingreso in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(tiposIngresoTesoreria, false)+") ";
			}
			
			logger.info("\n\n\n\n SQL getCodigoDescripcionConceptoTipoRegimen / "+consulta);
			logger.info("codigoInstitucion "+codigoInstitucion);
			
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setInt(1, codigoInstitucion);
			statement.setBoolean(2, esActivo);
			//statement.setString(2, acronimoTipoRegimen);
			ResultSetDecorator resultado=new ResultSetDecorator(statement.executeQuery());
			mapa= UtilidadBD.cargarValueObject(resultado);
			
			resultado.close();
			statement.close();
		}
		catch (SQLException e)
		{
			logger.error("Error consultando esReciboCajaAutomaticoViaIngresoDada (SqlBaseUtilidadValidacionDao): "+e);
		}
		return mapa;
	}
	
	/**
	 * Método para saber si existe un centro ed costo asociado a una via de ingreso 
	 * de una insitucion determinada
	 * @param con
	 * @param centroCosto
	 * @param via
	 * @return
	 */
	public static boolean existeCentroCostoXViaIngreso(Connection con, int centroCosto, int viaIngreso, int institucion)
	{
		ResultSetDecorator rs;
		int temp = 0;
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement("SELECT count(1) as cantidad FROM centro_costo_via_ingreso WHERE centro_costo=? and via_ingreso=? AND institucion=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, centroCosto);
			pst.setInt(2, viaIngreso);
			pst.setInt(3, institucion);
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				temp = rs.getInt("cantidad");
			}
			if(temp > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
			
		}
		catch (SQLException e)
		{
			logger.error("Error existeCentroCostoXViaIngreso : [SqlBaseUtilidadValidacionDao] : "+e);
			return false;
		}
	}
	
	/**
	 * Método implementado para consultar el codigo de area y el
	 * nombre del área del paciente, el formato es:
	 * codigoArea + constantesBD.separadorSplit + nombreArea
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static String getAreaPaciente(Connection con,int idCuenta)
	{
		try
		{
			//El nombre del area se adiciona el nombre de su respecitvo centro de atencion
			String consulta = "SELECT " +
				"c.area AS codigoArea," +
				"getnomcentrocosto(c.area) || ' - ' || ca.descripcion AS nombreArea "+ 
				"FROM cuentas c " +
				"INNER JOIN centros_costo cc ON(cc.codigo=c.area) " +
				"INNER JOIN centro_atencion ca ON(ca.consecutivo=cc.centro_atencion) " +
				"WHERE c.id = ?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			pst.setInt(1,idCuenta);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				return rs.getInt("codigoArea")+ConstantesBD.separadorSplit+rs.getString("nombreArea");
			else
				return "0"+ConstantesBD.separadorSplit+"";
		}
		catch(SQLException e)
		{
			logger.error("Error en getAreaPaciente de SQlBaseUtilidadValidacionDao: "+e);
			return "0"+ConstantesBD.separadorSplit+"";
		}
	}
	
	/**
	 * Método que verifica si un paciente está muerto
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static boolean esPacienteMuerto(Connection con,int codigoPaciente)
	{
		try
		{
			String consulta = "SELECT esta_vivo FROM manejopaciente.pacientes WHERE codigo_paciente = "+codigoPaciente;
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
						
			if(rs.next())							
				return !UtilidadTexto.getBoolean(rs.getString("esta_vivo"));			
			else		
				return true;	
			
		}
		catch(SQLException e)
		{
			logger.error("Error en esPacienteMuerto de SQlBaseUItilidadValidacionDao: "+e);
			return false;
		}
	}
	
	/**
	 * Método implementado para actualizar al paciente como muerto
	 * en la base de datos
	 * @param con
	 * @param codigoPaciente
	 * @param esVivo
	 * @param estado
	 * @return
	 */
	public static boolean actualizarPacienteAMuertoTransaccional(Connection con,int codigoPaciente,boolean esVivo,String fechaMuerte,String horaMuerte, String certificadoDefuncion, String estado)
	{
		try
		{
			if(estado.equals(ConstantesBD.inicioTransaccion))
				UtilidadBD.iniciarTransaccion(con);
			
			
			String consulta = "UPDATE pacientes SET esta_vivo = ?,fecha_muerte=?, hora_muerte=?, certificado_defuncion=? WHERE codigo_paciente = ?";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setBoolean(1,esVivo);
			if(!fechaMuerte.equals(""))
				pst.setDate(2,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaMuerte)));
			else
				pst.setNull(2,Types.DATE);
			
			if(!horaMuerte.equals(""))
				pst.setString(3,horaMuerte);
			else
				pst.setNull(3,Types.TIME);
			
			if(!certificadoDefuncion.equals(""))
				pst.setString(4, certificadoDefuncion);
			else
				pst.setNull(4, Types.CHAR);
			
			pst.setObject(5,codigoPaciente);
			
			int resp = pst.executeUpdate();
			
			if(resp>0)
			{
				if(estado.equals(ConstantesBD.finTransaccion))
					UtilidadBD.finalizarTransaccion(con);
				return true;
			}
			else
			{
				UtilidadBD.abortarTransaccion(con);
				return false;
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarPacienteAMuertoTransaccional de SqlBaseUtilidadValidacionDao: "+e);
			return false;
		}
	}
	
	/**
	 * Método para verificar la existencioa de cuentas asociadas a un contrato
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean existeCuentasAbiertasAsociadasAContrato(Connection con, int codigoContrato)
	{
		try
		{
			PreparedStatementDecorator stm =  new PreparedStatementDecorator(con.prepareStatement("SELECT count(1) AS numRegistros FROM manejopaciente.sub_cuentas WHERE contrato=? AND facturado='"+ConstantesBD.acronimoNo+"'"));
			stm.setInt(1, codigoContrato);
			ResultSetDecorator resultado=new ResultSetDecorator(stm.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("numRegistros")>0?true:false;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando si el constrato ("+codigoContrato+")tiene o no cuentas asociadas "+e);
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param consecutivoCategoria
	 * @return
	 */
	public static boolean categoriaTriageUtilizadaEnSignosSintomasSistema(Connection con, int consecutivoCategoria) 
	{
		try
		{
			PreparedStatementDecorator stm =  new PreparedStatementDecorator(con.prepareStatement("SELECT count(1) as resultado from signos_sintomas_x_sistema where consecutivo_categoria_triage = "+consecutivoCategoria,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(stm.executeQuery());
			if(rs.next())
			{
				return rs.getInt("resultado")>0;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando si la categoria triage es utilizada en signos_sintomas_x_sistema "+e);
		}
		return false;
	}
	
	/**
	 * Obtiene los consecutivos pk del triage 
	 * 
	 * @param con
	 * @param acronimoTipoIdentificacion
	 * @param numeroIdentificacion
	 * @param codigoCentroAtencion
	 * @param codigoInstitucion
	 * @return pos 0 --> consecutivoTriage  , pos 1 --> cpnsecutivofechaTriage
	 */
	public static String[] getConsecutivosTriage(Connection con, String acronimoTipoIdentificacion, String numeroIdentificacion, String codigoCentroAtencion, int codigoInstitucion) 
	{
		String[] consecutivos={"", ""};
		/*String consulta=	"SELECT " +
							"t.consecutivo AS consecutivoTriage, " +
							"t.consecutivo_fecha AS consecutivoFechaTriage " +
							"FROM triage t " +
							"INNER JOIN pacientes_triage pt ON (t.consecutivo=pt.consecutivo_triage AND t.consecutivo_fecha=pt.consecutivo_fecha_triage) " +
							"INNER JOIN destino_paciente dp ON (dp.consecutivo=t.destino) " +
							"WHERE t.tipo_identificacion=? " +
							"and t.numero_identificacion=? " +
							"and t.numero_admision IS NULL " +
							"AND t.anio_admision IS NULL " +
							"AND pt.atendido= "+ValoresPorDefecto.getValorTrueParaConsultas()+" "+  
							"AND dp.indicador_admi_urg=" +ValoresPorDefecto.getValorTrueParaConsultas()+" "+
							"AND pt.centro_atencion=? AND t.institucion=? ";*/
		
		String consulta=	"SELECT " +
						"t.consecutivo AS consecutivoTriage, " +
						"t.consecutivo_fecha AS consecutivoFechaTriage " +
						"FROM historiaclinica.triage t " +
						"INNER JOIN historiaclinica.destino_paciente dp ON (dp.consecutivo=t.destino) " +
						"WHERE t.tipo_identificacion=? " +
						"and t.numero_identificacion=? " +
						"and t.numero_admision IS NULL " +
						"and t.no_responde_llamado = "+ValoresPorDefecto.getValorFalseParaConsultas()+" " +
						"AND t.anio_admision IS NULL " +
						"AND dp.indicador_admi_urg=" +ValoresPorDefecto.getValorTrueParaConsultas()+" "+
						"AND t.centro_atencion=? AND t.institucion=? ";
		try
		{
			PreparedStatementDecorator stm =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			stm.setString(1, acronimoTipoIdentificacion);
			stm.setString(2, numeroIdentificacion);
			stm.setString(3, codigoCentroAtencion);
			stm.setInt(4, codigoInstitucion);
			
			ResultSetDecorator rs=new ResultSetDecorator(stm.executeQuery());
			if(rs.next())
			{
				consecutivos[0]=rs.getString("consecutivoTriage");
				consecutivos[1]=rs.getString("consecutivoFechaTriage");
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando getConsecutivosTriage "+e);
		}
		return consecutivos;
	}
	
	/**
	 * Método para saber si un triage determinado para una institucion tiene
	 * un numero de admision asignado
	 * @param con
	 * @param consecutivoTriage
	 * @param consecutivoFechaTriage
	 * @param institucion
	 * @return
	 */
	public static boolean existeAdmisionParaTriage(Connection con, String consecutivoTriage, String consecutivoFechaTriage, int institucion) 
	{
		try
		{
			PreparedStatementDecorator stm =  new PreparedStatementDecorator(con.prepareStatement("SELECT numero_admision as numeroAdmision FROM triage WHERE consecutivo = ? AND consecutivo_fecha = ? AND institucion= ?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			stm.setString(1,consecutivoTriage);
			stm.setString(2,consecutivoFechaTriage);
			stm.setInt(3, institucion);
			ResultSetDecorator rs=new ResultSetDecorator(stm.executeQuery());
			if(rs.next())
			{
				return rs.getInt("numeroAdmision") > 0;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error existeAdmisionParaTriage [SqlBaseUtilidadValidacion]: "+e);
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param consecutivoCategoria
	 * @return
	 */
	public static boolean categoriaTriageUtilizadaEnTriage(Connection con, int consecutivoCategoria)
	{
		try
		{
			PreparedStatementDecorator stm =  new PreparedStatementDecorator(con.prepareStatement("SELECT count(1) as resultado from triage where categoria_triage = "+consecutivoCategoria,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(stm.executeQuery());
			if(rs.next())
			{
				return rs.getInt("resultado")>0;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando si la categoria triage es utilizada en categoriaTriageUtilizadaEnTriage "+e);
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @param codigoAjuste
	 * @return
	 */
	public static boolean cuentaCobroConFactEnAjustesPendienteDiferenteActual(Connection con, double cuentaCobro, int institucion, double codigoAjuste) 
	{
		try
		{
			String consulta="select count(1) as numFacturas from facturas f inner join cuentas_cobro c on(c.numero_cuenta_cobro=f.numero_cuenta_cobro) inner join ajus_fact_empresa afe on f.codigo=afe.factura inner join ajustes_empresa ae on ae.codigo=afe.codigo  where ae.estado=? and c.numero_cuenta_cobro=? and f.institucion=? and ae.codigo <> "+codigoAjuste;
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,ConstantesBD.codigoEstadoCarteraGenerado);
			pst.setDouble(2,cuentaCobro);
			pst.setInt(3,institucion);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				return (rs.getInt("numFacturas")>0);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error a consultar si la cuenta de cobro esta ligada a facturas con ajustes pendientes SqlBaseUtilidadValidacionDao: "+e);
		}
		return false;

	}
	
	/**
	 * Método implementado para verificar si un paciente tiene cuentas Urgencias
	 * en estado Cuenta Activa o Asociada
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static boolean tieneCuentaUrgenciasAbierta(Connection con,String codigoPaciente)
	{
		try
		{
			boolean existe = false;
			String consulta = "SELECT " +
				"count(1) AS cuenta " +
				"FROM cuentas c " +
				"INNER JOIN ingresos i ON(i.id=c.id_ingreso) " +
				"WHERE " +
				"c.codigo_paciente=? AND "+
				"c.estado_cuenta IN ("+ConstantesBD.codigoEstadoCuentaActiva+"," + ConstantesBD.codigoEstadoCuentaAsociada+", "+ConstantesBD.codigoEstadoCuentaFacturadaParcial+") AND " +
				"c.via_ingreso = "+ConstantesBD.codigoViaIngresoUrgencias+" AND " +
				"i.estado = '"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"'";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,codigoPaciente);
				
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				if(rs.getInt("cuenta")>0)
					existe = true;
				else
					existe = false;
			}
			else
				existe = false;
			
			return existe;
		}
		catch(SQLException e)
		{
			logger.error("Error en tieneCuentaUrgenciasAbierta de SqlBaseUtilidadValidacionDao: "+e);
			return false;
		}
	}

	/**
	 * Método para saber si una solicitud de cirugia que ya tiene hoja de quirurgica
	 * tiene la participacion de anestesiologo
	 * @param con
	 * @param consecutivoOrdenes
	 * @return
	 */
	public static boolean solCxNecesitaHojaAntesia(Connection con, int consecutivoOrdenes)
	{
		PreparedStatementDecorator pst = null;
		ResultSetDecorator rs = null;
		try
		{
			boolean existe = false;
			
			String consulta = " SELECT hqx.participo_anestesiologo  as participoanestesiologo " +
							  " FROM hoja_quirurgica hqx " +
							  " INNER JOIN solicitudes_cirugia solcx ON(hqx.numero_solicitud=solcx.numero_solicitud) " +
							  " INNER JOIN solicitudes sol ON(solcx.numero_solicitud=sol.numero_solicitud) " +
							  " WHERE sol.consecutivo_ordenes_medicas= ? ";
			
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			pst.setInt(1,consecutivoOrdenes);
				
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				if(!UtilidadCadena.noEsVacio(rs.getString("participoanestesiologo")))
					existe=true;
				else
					existe = rs.getBoolean("participoanestesiologo") ;
			}
			else
			{
				existe = true;
			}
			
			return existe;
		}
		catch(SQLException e)
		{
			logger.error("Error en solCxNecesitaHojaAntesia de SqlBaseUtilidadValidacionDao: "+e);
			return false;
		}
		finally
		{
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
		}
	}	
	/**
	 * Utilidad que obtiene los datos del médico que guardó la hoja
	 * de anestesia
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String getDatosMedicoHojaAnestesia(Connection con, int numeroSolicitud)
	{
		String consultaStr="SELECT datos_medico AS datos_medico " +
											"		FROM hoja_anestesia WHERE numero_solicitud =?";
		try
		{
		    PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    ps.setInt(1, numeroSolicitud);
		     ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
		     
		    if (rs.next())
		    {
		        String respuesta=rs.getString("datos_medico");
		        return respuesta;
		    }
		    else
		    {
		        return "";
		    }
		}
		catch (SQLException e) 
		{
			logger.error("Error en getDatosMedicoHojaAnestesia de SqlBaseUtilidadValidacionDao: "+e);
			return "";
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param codTipoPrograma
	 * @param institucion
	 * @return
	 */
	public static boolean esTipoProgramaPYPUsado(Connection con, String codTipoPrograma, int institucion) 
	{
		try
		{
			boolean resultado = false;
			String consulta = " SELECT count(1) as num from programas_salud_pyp where tipo_programa = ? and institucion=?";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codTipoPrograma);
			ps.setInt(2,institucion);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado = rs.getInt("num")>0 ;
			}
			else
			{
				resultado = false;
			}
			
			return resultado;
		}
		catch(SQLException e)
		{
			logger.error("Error en solCxNecesitaHojaAntesia de SqlBaseUtilidadValidacionDao: "+e);
			return false;
		}
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public static boolean existeProgramaSaludPYP(Connection con, String codigo, int institucion) 
	{
		try
		{
			boolean resultado = false;
			String consulta = " SELECT count(1) as num from programas_salud_pyp where codigo=? and institucion=?";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigo);
			ps.setInt(2,institucion);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado = rs.getInt("num")>0 ;
			}
			else
			{
				resultado = false;
			}
			
			return resultado;
		}
		catch(SQLException e)
		{
			logger.error("Error en solCxNecesitaHojaAntesia de SqlBaseUtilidadValidacionDao: "+e);
			return false;
		}
	}

	/**
	 * 
	 * @param con
	 * @param programa
	 * @param actividad
	 * @param institucion
	 * @return
	 */
	public static boolean existeRelacionProgramaActividadPYP(Connection con, String programa, String actividad, int institucion) 
	{
		try
		{
			boolean resultado = false;
			long actividadLong = Long.parseLong(actividad);
			String consulta = " SELECT count(1) as num from actividades_programa_pyp where programa=? and actividad=? and institucion=?";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,programa);
			ps.setLong(2,actividadLong);
			ps.setInt(3,institucion);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado = rs.getInt("num")>0 ;
			}
			else
			{
				resultado = false;
			}
			
			return resultado;
		} catch(SQLException e) {
			logger.error("Error en UtilidadValidacion de existeRelacionProgramaActividadPYP: "+e);
			return false;
		} catch (Exception e) {
			logger.error("Error en UtilidadValidacion de existeRelacionProgramaActividadPYP: "+e);
			return false;
		}
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean esActividadProgramaUsaoEnActProConvenios(Connection con, String codigo) 
	{
		try
		{
			boolean resultado = false;
			long codigoLong = Long.parseLong(codigo); 
			String consulta = " SELECT count(1) as num from prog_act_pyp_convenio where actividad_programa_pyp = ?";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setLong(1,codigoLong);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next()) {
				resultado = rs.getInt("num")>0 ;
			} else {
				resultado = false;
			}
			
			return resultado;
		} catch(SQLException e) {
			logger.error("Error en UtilidadValidacion de esActividadProgramaUsaoEnActProConvenios: "+e);
			return false;
		} catch (Exception e) {
			logger.error("Error en UtilidadValidacion de esActividadProgramaUsaoEnActProConvenios: "+e);
			return false;
		}
	}
	
	/**
	 * existe cuenta cobro capitada
	 * @param con
	 * @param cuentaCobroCapitada
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean existeCuentaCobroCapitada(Connection con, String cuentaCobroCapitada, int codigoInstitucion) 
	{
		try
		{
			String consulta = " SELECT numero_cuenta_cobro from cuentas_cobro_capitacion where numero_cuenta_cobro=? and institucion=?";
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,cuentaCobroCapitada);
			ps.setInt(2, codigoInstitucion);
			if(ps.executeQuery().next())
				return true;
		}
		catch(SQLException e)
		{
			logger.error("Error en UtilidadValidacion existeCuentaCobroCapitada: "+e);
		}
		return false;
	}
	
	/**
	 * Método que verifica si un convenio es de PYP
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean esConvenioPYP(Connection con,String codigoConvenio)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		boolean resultado = false;
		try
		{
			logger.info("############## Inicio esConvenioPYP");
			String consulta = "SELECT pyp FROM convenios WHERE codigo = ?";
			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,Utilidades.convertirAEntero(codigoConvenio));
			rs = pst.executeQuery();
			if(rs.next()){
				resultado = rs.getBoolean("pyp");
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR esConvenioPYP",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR esConvenioPYP", e);
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
		logger.info("############## Fin esConvenioPYP");
		return resultado;
	}
	
	/**
	 * Método que obtiene el año y mes de cierre saldo de capitación
	 * de acuerdo a los parámetros enviados 
	 * @param institucion
	 * @param tipoCierre
	 * @return String[]={año_cierre, mes_cierre}
	 * 					sino exite return vacio String[]={"",""}
	 */
	public static String[] obtenerFechaMesCierreSaldoCapitacion(Connection con, int institucion, String tipoCierre)
	{
		String anioMesCierre[]={"",""};
		
		try
		{
		String consultaStr= "SELECT anio_cierre AS anio_cierre, mes_cierre AS mes_cierre" +
											" 			FROM capitacion.cierres_saldos_capitacion" +
											"				WHERE institucion=? and tipo_cierre=?";
		
		PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		ps.setInt(1, institucion);
		ps.setString(2, tipoCierre);
		
		ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());

			if (rs.next())
			{
				anioMesCierre[0]=rs.getString("anio_cierre");
				anioMesCierre[1]=rs.getString("mes_cierre");
			}
		}	
		catch (SQLException e) 
		{
			logger.error("Error en obtenerFechaMesCierreSaldoCapitacion de SqlBaseUtilidadValidacionDao: "+e);
		}
		return anioMesCierre;
	}
	
	/**
	 * Método que verifica si un paciente ya tiene información de PYP
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean tienePacienteInformacionPYP(Connection con,HashMap campos)
	{
		try
		{
			boolean resultado = false;
			String consulta = "SELECT count(1) AS cuenta FROM programas_pyp_paciente " +
				"WHERE paciente = "+campos.get("codigoPaciente")+" and institucion = "+campos.get("institucion");
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					resultado = true;
			
			return resultado;
		}
		catch(SQLException e)
		{
			logger.error("Error en tienePacienteInformacionPYP de SQlBaseUtilidadValidacionDao: "+e);
			return false;
		}
	}
	
	/**
	 * Utilidad para verificar si un paciente tiene registros de ordenes ambulatorias
	 * @param con
	 * @param codigoPaciente
	 * @param codigoInstitucion
	 * @return true si existe sino retorna false
	 */
	public static boolean tienePacienteOrdenesAmbulatorias(Connection con, int codigoPaciente, int codigoInstitucion)
	{
		try
		{
			boolean resultado = false;
			String consultaStr = "SELECT count(1) AS num " +
											"		FROM ordenes.ordenes_ambulatorias " +
											"			WHERE codigo_paciente= "+codigoPaciente+" AND institucion= "+codigoInstitucion;
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery(consultaStr));
			
			if(rs.next())
				if(rs.getInt("num")>0)
					resultado = true;
			
			return resultado;
		}
		catch(SQLException e)
		{
			logger.error("Error en tienePacienteOrdenesAmbulatorias de SqlBaseUtilidadValidacionDao: "+e);
			return false;
		}
	}

	/**
	 * 
	 * @param con
	 * @param consecutivoGrupo
	 * @param institucion
	 * @return
	 */
	public static boolean puedoEliminarGrupoEtareoCreDes(Connection con, String consecutivoGrupo, String institucion) 
	{
		boolean resultado=false;
		UtilidadBD.iniciarTransaccion(con);
		String cadena="DELETE FROM grup_etareo_creci_desa WHERE consecutivo="+consecutivoGrupo+" AND institucion="+institucion;
		try 
		{
			//se hace la prueba borrando el registro por que así queda contemplado que el registro no se use en ninguna otra tabla 
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultado=ps.executeUpdate()>0;
			
		} 
		catch (SQLException e) 
		{
			
			UtilidadBD.abortarTransaccion(con);
			resultado=false;
		}
		UtilidadBD.abortarTransaccion(con);
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param convenio
	 * @return
	 */
	public static boolean esConvenioCapitado(Connection con, int convenio) 
	{
		boolean resultado=false;
		String cadena="SELECT codigo from convenios where tipo_contrato ="+ConstantesBD.codigoTipoContratoCapitado+" and codigo="+convenio;
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultado=(new ResultSetDecorator(ps.executeQuery())).next();
		} 
		catch (SQLException e) 
		{
			resultado=false;
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public static boolean esAjusteDistribuidoCompletamete(Connection con, String consulta) 
	{
		boolean resultado=false;
		
		PreparedStatementDecorator ps = null;
		
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			resultado=(new ResultSetDecorator(ps.executeQuery())).next();
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR SQLException -- esAjusteDistribuidoCompletamete",e);
			resultado=false;
		
		}catch(Exception e){
			logger.error("ERROR Exception -- esAjusteDistribuidoCompletamete", e);
		
		}
		finally{
			try{
				if(ps != null){
					ps.close();
				}
			}
			catch (SQLException se) {
				logger.error("Error close ResultSet ", se);
			}
		}
		return resultado;
	}
	
	/**
	 * Método implementado para verificar que el grupo de la actividad
	 * tenga centros de costo parametrizados en la funcionalidad
	 * Centros Costo x Grupo Servicios con centro de atencion que se encuentren
	 * en Actividades por centro atencion
	 * @param con
	 * @param consecutivoActividad
	 * @param centroAtencion
	 * @param institucion
	 * @return
	 */
	public static boolean existeCentroCostoXGrupoServicioActividad(Connection con,String consecutivoActividad,int centroAtencion,int institucion)
	{
		try
		{
			boolean existe = false;
			String consulta = "SELECT "+
				"count(1) as cuenta "+
				"FROM actividades_pyp ap "+ 
				"INNER JOIN servicios s ON(s.codigo=ap.servicio) "+ 
				"INNER JOIN centro_costo_grupo_ser ccgs ON(ccgs.grupo_servicio=s.grupo_servicio) "+ 
				"INNER JOIN centros_costo cc ON(cc.codigo=ccgs.centro_costo) "+
				"WHERE "+ 
				"ap.consecutivo = ? AND "+ 
				"ccgs.centro_atencion = ? AND "+ 
				"cc.centro_atencion IN (SELECT centro_atencion FROM " +
					"act_pyp_centro_atencion " +
					"WHERE " +
					"actividad_pyp=ap.consecutivo and " +
					"institucion = ? and activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+")";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1,consecutivoActividad);
			pst.setObject(2,centroAtencion);
			pst.setObject(3,institucion);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					existe = true;
			
			return existe;
		}
		catch(SQLException e)
		{
			logger.error("Error en existeCentroCostoXGrupoServicioEnServicio de SqlBaseUtilidadValidacionDao: "+e);
			return false;
		}
	}
	
	/**
	 * Método que verifica si el paciente tiene antecedentes vacunas
	 * @param con
	 * @param codigoPersona
	 * @return true si existen antecedentes, false de lo contrario
	 */
	public static boolean existeAntecedenteVacunas (Connection con, int codigoPersona)
	{
		String consultaStr="SELECT count(1) AS numResultados from antecedentes_vacunas " +
							"		WHERE codigo_paciente=?";
		
		try
		{
			if(con==null || con.isClosed())
			{
			con=UtilidadBD.abrirConexion();
			}
		} catch (SQLException e1)
		{
			logger.warn("No se pudo realizar la conexión (SqlBaseUtilidadValidacionDao)"+e1.toString());
		}
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoPersona);
			ResultSetDecorator resultado=new ResultSetDecorator(ps.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("numResultados") > 0;
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando la existencia de antecedentes vacunas : "+e);
			return false;
		}
	}
	
	/**
	 * metodo que indica si por ingreso existe accidente de transito
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static boolean esAccidenteTransito(Connection con, int idIngreso) 
	{
		PreparedStatement pst=null;
		ResultSet rs= null;
		try 
		{
			String cadena="SELECT indicativo_acc_transito AS accidente FROM cuentas WHERE id_ingreso=? and indicativo_acc_transito = ?";
			pst = con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,idIngreso);
			pst.setBoolean(2, true);
			rs=pst.executeQuery();
			if(rs.next()){
				return rs.getBoolean("accidente");
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR esAccidenteTransito",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR esAccidenteTransito", e);
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
		return false;
	}

	/**
	 * metodo que indica si por ingreso existe accidente de transito en un estado dado
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static boolean esAccidenteTransitoEstadoDado(Connection con, int idIngreso, String estado) 
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		boolean resultado=false;
		try 
		{
			String cadena="SELECT ingreso from view_registro_accid_transito WHERE ingreso=? AND estado IN ('"+estado+"')";
			pst = con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,idIngreso);
			rs=pst.executeQuery();
			resultado=(rs.next());
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR esAccidenteTransitoEstadoDado",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR esAccidenteTransitoEstadoDado", e);
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
		return resultado;
		
	}


	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public static boolean esServicioMultiple(Connection con, String codigoServicio)
	{
		String cadena="SELECT gs.codigo from grupos_servicios gs inner join servicios s on(s.grupo_servicio=gs.codigo) where s.codigo='"+codigoServicio+"' and gs.multiple='"+ConstantesBD.acronimoSi+"'";
		logger.info(cadena+" "+codigoServicio);
		boolean resultado=false;
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			resultado=(rs.next());
			rs.close();
		} 
		catch (SQLException e) 
		{
			logger.error("Error consultando esServicioMultiple : "+e);
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param viaIngreso
	 * @param servicio
	 * @param institucion
	 * @return
	 */
	public static boolean esServicioViaIngresoCargoProceso(Connection con, String viaIngreso, String servicio, String institucion) throws BDException
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		boolean resultado=false;
		
		try {
			Log4JManager.info("############## Inicio esServicioViaIngresoCargoProceso");
			String cadena="SELECT codigo from cargo_via_ingreso_servicio where via_ingreso='"+viaIngreso+"' and servicio='"+servicio+"' and institucion='"+institucion+"' and cargo_proceso='"+ConstantesBD.acronimoSi+"'";
			
			pst= con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs=pst.executeQuery();
			if(rs.next()){
				resultado=true;
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
		Log4JManager.info("############## Fin esServicioViaIngresoCargoProceso");
		return resultado;
	}

	
	/**
	 * 
	 * @param con
	 * @param viaIngreso
	 * @param servicio
	 * @param institucion
	 * @return
	 */
	public static boolean esServicioViaIngresoCargoSolicitud(Connection con, String viaIngreso, String servicio, String institucion) throws BDException
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		boolean resultado=false;
		
		try {
			Log4JManager.info("############## Inicio esServicioViaIngresoCargoSolicitud");
			String cadena="SELECT codigo from cargo_via_ingreso_servicio where via_ingreso='"+viaIngreso+"' and servicio='"+servicio+"' and institucion='"+institucion+"' and cargo_solicitud='"+ConstantesBD.acronimoSi+"'";
			
			pst= con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			
			rs=pst.executeQuery();
			if(rs.next()){
				resultado=true;
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
		Log4JManager.info("############## Fin esServicioViaIngresoCargoSolicitud");
		return resultado;
	}

	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean estipoTransInvUsada(Connection con, String codigo)
	{
		boolean enTransaccion=UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM tipos_trans_inventarios where consecutivo='"+codigo+"'";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			enTransaccion=ps.executeUpdate()>0;
			ps.close();
		}
		catch (SQLException e)
		{
			enTransaccion=false;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return enTransaccion;
	}
	
	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static Vector obtenerSolicitudesTipoServicioPartosCesarea(Connection con, String cuenta, String cuentaAsociada)
	{
		String cadena=	"SELECT scxs.codigo as consecutivo " +
						"from " +
							"solicitudes sol " +
							"inner join sol_cirugia_por_servicio scxs on (sol.numero_solicitud=scxs.numero_solicitud) " +
							"inner join servicios  ser on(scxs.servicio=ser.codigo and ser.tipo_servicio='"+ConstantesBD.codigoServicioPartosCesarea+"') " +
						"where " +
							"( sol.cuenta=? ";
		
		if(!cuentaAsociada.equals(""))
		{
			cadena+=" or sol.cuenta="+cuentaAsociada;
		}
		
		cadena+=") AND sol.estado_historia_clinica not in( "+ConstantesBD.codigoEstadoHCAnulada+") AND  esSolicitudAnuladaInactiva(sol.numero_solicitud) ='"+ConstantesBD.acronimoNo+"' ";
		
		Vector vector= new Vector();
		
		try 
		{
			logger.info("\n\n obtenerSolicitudesTipoServicioPartosCesarea-->"+cadena+" cuenta->"+cuenta);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setString(1, cuenta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				vector.add(rs.getString("consecutivo"));
			}
			rs.close();
			ps.close();
		} 
		catch (SQLException e) 
		{
			logger.error("Error consultando obtenerSolicitudesTipoServicioPartosCesarea : "+e);
		}
		return vector;
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoCx
	 * @return
	 */
	public static boolean existeInfoRecienNacido(Connection con, String consecutivoCx)
	{
		String cadena="SELECT consecutivo from info_parto_hijos where cirugia = ?";
		try 
		{
			logger.info("existeInfoRecienNacido-->"+cadena+" consecutivoCx-->"+consecutivoCx);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, consecutivoCx);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			boolean resultado=rs.next();
			rs.close();
			return resultado;
				
		} 
		catch (SQLException e) 
		{
			logger.error("Error consultando existeInfoRecienNacido : "+e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoCx
	 * @return
	 */
	public static boolean existeInfoPartosFinalizada(Connection con, String codigoPaciente, String consecutivoCx)
	{
		String cadena="SELECT consecutivo FROM informacion_parto WHERE  codigo_paciente=? and cirugia = ? and finalizado='"+ConstantesBD.acronimoSi+"'";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info("existeInfoPartosFinalizada >>>>>>>"+cadena+"  codigoPaciente"+codigoPaciente+"  consecutivoCx"+consecutivoCx);
			
			ps.setString(1, codigoPaciente);
			ps.setString(2, consecutivoCx);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			boolean resultado=rs.next();
			rs.close();
			return resultado;
				
		} 
		catch (SQLException e) 
		{
			logger.error("Error consultando existeInfoPartosFinalizada : "+e);
		}
		return false;
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public static boolean pacienteTieneIngresoAbierto(Connection con, int codigoPersona)
	{
		boolean valido = false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("SELECT count(1) AS cuenta from ingresos where estado='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' and codigo_paciente="+codigoPersona,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					valido = true;
			rs.close();
			ps.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return valido;
	}

	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public static String obtenerFechaEgresoUltimoIngresoPaciente(Connection con, int codigoPersona,String cadena)
	{
		String resultado="";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				resultado=rs.getString(1);
			rs.close();
			ps.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public static String obtenerFechaFacturaCuenta(Connection con, int codigoPersona,String consulta)
	{
		String resultado="";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				resultado=rs.getString(1);
			rs.close();
			ps.close();
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return resultado;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @return
	 */
	public static boolean articuloTieneCantidadUnidosisActiva(Connection con, int codigoArticulo)
	{
		String cadena="SELECT count(1) FROM unidosis_x_articulo WHERE  articulo=? and cantidad is not null and activo='"+ConstantesBD.acronimoSi+"'";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoArticulo);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				if(rs.getInt(1)>0)
				{
					rs.close();
					ps.close();
					return true;
				}
			}
		} 
		catch (SQLException e) 
		{
			logger.error("Error consultando articuloTieneCantidadUnidosisActiva : "+e);
		}
		return false;

	}
	
	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @return
	 */
	public static boolean articuloTieneCantidadUnidosisActivaDadoUnidosis(Connection con, int codigoUnidodis)
	{
		String cadena="SELECT count(1) FROM unidosis_x_articulo WHERE  codigo=? and cantidad is not null and activo='"+ConstantesBD.acronimoSi+"'";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoUnidodis);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				if(rs.getInt(1)>0)
				{
					rs.close();
					ps.close();
					return true;
				}
			}
		} 
		catch (SQLException e) 
		{
			logger.error("Error consultando articuloTieneCantidadUnidosisActivaDadoUnidosis : "+e);
		}
		return false;

	}

	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean esSolicitudMedicamentos(Connection con, String numeroSolicitud)
	{
		String cadena="SELECT count(1) FROM solicitudes WHERE  numero_solicitud=? and (tipo="+ConstantesBD.codigoTipoSolicitudMedicamentos+" or tipo="+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+")";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Integer.parseInt(numeroSolicitud));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				if(rs.getInt(1)>0)
				{
					rs.close();
					ps.close();
					return true;
				}
			}
		} 
		catch (SQLException e) 
		{
			logger.error("Error consultando esSolicitudMedicamentos : "+e);
		}
		return false;
	}

	
	/**
	 * 
	 * @param con
	 * @param clase
	 * @return
	 */
	public static boolean esClaseInventarioUsada(Connection con, int clase)
	{
		
		try 
		{
			int cuenta0 =0, cuenta1 = 1;
			
			String cadena="SELECT count(1) FROM grupo_inventario WHERE  clase=?";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, clase);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				cuenta0 = rs.getInt(1);
			
			cadena = "SELECT count(1)  FROM clase_inv_unidad_fun WHERE clase_inventario = ? ";
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, clase);
			rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				cuenta1 = rs.getInt(1);
			rs.close();
			ps.close();
			if(cuenta0>0||cuenta1>0)
				return true;
			else
				return false;
		} 
		catch (SQLException e) 
		{
			logger.error("Error consultando esClaseInventarioUsada : "+e);
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param grupo
	 * @return
	 */
	public static boolean esClaseGrupoInventarioUsado(Connection con, int grupo,int clase)
	{
		
		try 
		{
			int cuenta0 = 0, cuenta1 = 0;
			
			String cadena="SELECT count(1) FROM subgrupo_inventario WHERE  clase=? and grupo=?";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, clase);
			ps.setInt(2, grupo);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				cuenta0 = rs.getInt(1);
			
			cadena="SELECT count(1) FROM grupo_inv_unidad_fun WHERE  clase_inventario=? and grupo_inventario=?";
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, clase);
			ps.setInt(2, grupo);
			rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				cuenta1 = rs.getInt(1);
			rs.close();
			ps.close();
			if(cuenta0>0||cuenta1>0)
				return true;
			else
				return false;
		} 
		catch (SQLException e) 
		{
			logger.error("Error consultando esClaseGrupoInventarioUsado : "+e);
		}
		return false;
	}

	
	/**
	 * 
	 * @param con
	 * @param subGrupo
	 * @param clase
	 * @param grupo
	 * @return
	 */
	public static boolean esClaseGrupoSubGrupoInventarioUsado(Connection con, int subGrupo, int clase, int grupo)
	{
		
		try
		{
			int cuenta0 = 0, cuenta1 = 0;
			
			String cadena="SELECT count(1) from articulo a inner join subgrupo_inventario sbi on(sbi.codigo=a.subgrupo) where sbi.clase=? and sbi.grupo=? and sbi.codigo=?";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, clase);
			ps.setInt(2, grupo);
			ps.setInt(3, subGrupo);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				cuenta0 = rs.getInt(1);
			
			cadena="SELECT count(1) from subgrupo_inv_unidad_fun where clase_inventario=? and grupo_inventario=? and codigo=?";
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, clase);
			ps.setInt(2, grupo);
			ps.setInt(3, subGrupo);
			rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				cuenta1 = rs.getInt(1);
			
			rs.close();
			ps.close();
			if(cuenta0>0||cuenta1>0)
				return true;
			else
				return false;
			
			
		} 
		catch (SQLException e) 
		{
			logger.error("Error consultando esClaseGrupoSubGrupoInventarioUsado : "+e);
		}
		return false;
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoPaquete
	 * @param institucion
	 * @return
	 */
	public static boolean esPaqueteUsado(Connection con, String codigoPaquete, int institucion) 
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM paquetes WHERE codigo_paquete=? and institucion=? ";
		boolean respuesta=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigoPaquete);
			ps.setInt(2, institucion);
			ps.executeUpdate();
			respuesta=true;
			ps.close();
		}
		catch(SQLException e)
		{
			respuesta=false;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	
	
	/**
	 * Metodo encargado de idenficar si un consentimiento informado
	 * ha sido usado o no
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public static boolean esConsentimientoInformadoUsado(Connection con, String codigo, int institucion) 
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM consentimientoinformado WHERE codigo=? and institucion=? ";
		boolean respuesta=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigo);
			ps.setInt(2, institucion);
			ps.executeUpdate();
			ps.close();
			respuesta=true;
		}
		catch(SQLException e)
		{
			respuesta=false;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPaquete
	 * @param institucion
	 * @return
	 */
	public static boolean esConceptoUsado(Connection con, String codigo, int institucion) 
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM conceptos_ing_tesoreria WHERE codigo=? and institucion=? ";
		boolean respuesta=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigo);
			ps.setInt(2, institucion);
			ps.executeUpdate();
			ps.close();
			respuesta=false;
		}
		catch(SQLException e)
		{
			respuesta=true;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	/**
	 * esNaturalezaArticulosUsado
	 */
	public static boolean esNaturalezaArticulosUsado(Connection con, String codigo, int institucion) 
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM naturaleza_articulo WHERE acronimo=? and institucion=? ";
		boolean respuesta=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigo);
			ps.setInt(2, institucion);
			ps.executeUpdate();
			respuesta=true;
		}
		catch(SQLException e)
		{
			respuesta=false;
		}
		
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	/**
	 * esViasIngresoUsado
	 */
	public static boolean esViasIngresoUsado(Connection con, int codigo) 
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM vias_ingreso WHERE codigo=?";
		boolean respuesta=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			ps.executeUpdate();
			respuesta=true;
		}
		catch(SQLException e)
		{
			respuesta=false;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	/**
	 * esUbicacionGeograficaUsado
	 */
	public static boolean esUbicacionGeograficaUsado(Connection con, String codigo) 
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM paises WHERE codigo_pais=?";
		boolean respuesta=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigo);
			ps.executeUpdate();
			respuesta=true;
		}
		catch(SQLException e)
		{
			respuesta=false;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	/**
	 * esDepartamentoUsado
	 */
	public static boolean esDepartamentoUsado(Connection con, String codigo_departamento, String codigo_pais) 
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM departamentos WHERE codigo_departamento=? AND codigo_pais=?";
		boolean respuesta=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigo_departamento);
			ps.setString(2, codigo_pais);
			ps.executeUpdate();
			respuesta=false;
		}
		catch(SQLException e)
		{
			respuesta=true;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	/**
	 * esCiudadUsado
	 */
	public static boolean esCiudadUsado(Connection con, String codigo_ciudad,String codigo_departamento, String codigo_pais) 
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM ciudades WHERE codigo_ciudad=? AND codigo_departamento=? AND codigo_pais=?";
		boolean respuesta=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigo_ciudad);
			ps.setString(2, codigo_departamento);
			ps.setString(3, codigo_pais);
			ps.executeUpdate();
			respuesta=true;
		}
		catch(SQLException e)
		{
			respuesta=false;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	/**
	 * esLocalidadUsado
	 */
	public static boolean esLocalidadUsado(Connection con, String codigo_localidad, String codigo_ciudad,String codigo_departamento, String codigo_pais) 
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM localidades WHERE codigo_localidad=? AND codigo_ciudad=? AND codigo_departamento=? AND codigo_pais=?";
		boolean respuesta=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigo_localidad);
			ps.setString(2, codigo_ciudad);
			ps.setString(3, codigo_departamento);
			ps.setString(4, codigo_pais);
			ps.executeUpdate();
			respuesta=true;
		}
		catch(SQLException e)
		{
			respuesta=false;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	public static boolean esTiposMonedaUsado(Connection con,int codigo,int institucion)
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM tipos_moneda WHERE codigo=? AND institucion=?";
		boolean respuesta=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			ps.setInt(2, institucion);
			ps.executeUpdate();
			respuesta=false;
		}
		catch (SQLException e) 
		{
			respuesta=true;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	public static boolean esVigenciaConvenioUsado(Connection con,int codigo,int institucion)
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM grupos WHERE codigo=? AND institucion=?";
		boolean respuesta=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			ps.setInt(2, institucion);
			ps.executeUpdate();
			respuesta=false;
		}
		catch (SQLException e) 
		{
			respuesta=true;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	public static boolean esAsocioXUvrTipoSalaUsado(Connection con,int codigo)
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM asocios_x_uvr_tipo_sala WHERE codigo=? ";
		boolean respuesta=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			ps.executeUpdate();
			respuesta=false;
		}
		catch (SQLException e) 
		{
			respuesta=true;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	public static boolean esAsociosXUvrUsado(Connection con,int codigo)
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM asocios_x_uvr WHERE codigo=? ";
		boolean respuesta=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			ps.executeUpdate();
			respuesta=false;
		}
		catch (SQLException e) 
		{
			respuesta=true;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	
	public static boolean esInclusionesExclusionesUsado(Connection con, String codigo, int institucion) 
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM inclusiones_exclusiones WHERE codigo=? and institucion=? ";
		boolean respuesta=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigo);
			ps.setInt(2, institucion);
			ps.executeUpdate();
			respuesta=true;
		}
		catch(SQLException e)
		{
			respuesta=false;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}

	public static boolean esPaqueteConvenioUsado(Connection con, int codigo) 
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM paquetes_convenio WHERE codigo=? ";
		boolean respuesta=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			ps.executeUpdate();
			respuesta=true;
		}
		catch(SQLException e)
		{
			respuesta=false;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo del detalle de Porcentajes Cx Multi (true = depende , false = no depende)
	 * @param Connection con
	 * @param String codigo
	 * @param String codigo Encabezado
	 * */
	public static boolean esDetallePorcentajeCxMultiUsado(Connection con, int codigo, int codigo_encab) 
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM porcentajes_cx_multi WHERE codigo=? and codigo_encab = ? ";
		boolean respuesta=true;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			ps.setInt(2, codigo_encab);
			ps.executeUpdate();
			respuesta=false;
		}
		catch(SQLException e)
		{
			respuesta=true;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo del encabezado del Porcentajes Cx Multi (true = depende , false = no depende)
	 * @param Connection con
	 * @param int institucion
	 * @param String codigo Encabezado 
	 * */
	public static boolean esEncaPorcentajeCxMultiUsado(Connection con, int institucion, int codigo_encab) 
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM enca_porcen_cx_multi WHERE institucion=? and codigo = ? ";
		boolean respuesta=true;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			ps.setInt(2, codigo_encab);
			ps.executeUpdate();
			respuesta=false;
		}
		catch(SQLException e)
		{
			respuesta=true;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static boolean esEventoCatastrofico(Connection con, int codigoIngreso)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
		{
			String cadena="SELECT count(1) AS eventocatastrofico FROM cuentas WHERE id_ingreso=? and tipo_evento='"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"'";
			pst= con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,codigoIngreso);
			rs=pst.executeQuery();
			if(rs.next()){
				return rs.getInt(1)>0;
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR esEventoCatastrofico",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR esEventoCatastrofico", e);
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
		return false;
	}

	/**
	 * metodo que indica si por ingreso existe evento catastrofico en un estado dado
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static boolean esEventoCatastroficoEstadoDado(Connection con, int idIngreso, String estado) 
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try 
		{
			String cadena="SELECT ingreso from view_reg_evento_catastrofico WHERE ingreso=? AND estado IN ('"+estado+"')";
			pst= con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,idIngreso);
			rs=pst.executeQuery();
			if(rs.next()){
				return true;
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerTarifaBaseServicio",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerTarifaBaseServicio", e);
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
		return false;
	}
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de tipos convenio (true = depende , false = no depende)
	 * @param Connection con
	 * @param String codigo
	 * @param int institucion
	 * */
	public static boolean esTiposConvenioUsado(Connection con, String codigo, int institucion) 
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM tipos_convenio WHERE codigo=? and institucion=? ";
		boolean respuesta=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigo);
			ps.setInt(2, institucion);
			ps.executeUpdate();
			respuesta=true;
		}
		catch(SQLException e)
		{
			respuesta=false;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	
	
	public static boolean esServiciosEsteticosUsado(Connection con, String codigo, int institucion) 
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM servicios_grupos_esteticos WHERE codigo=? and institucion=? ";
		boolean respuesta=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigo);
			ps.setInt(2, institucion);
			ps.executeUpdate();
			respuesta=true;
		}
		catch(SQLException e)
		{
			respuesta=false;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de cobertura (true = depende , false = no depende)
	 * @param Connection con
	 * @param String codigoCobertura
	 * @param int institucion
	 * */
	public static boolean esCoberturaUsado(Connection con, String codigoCobertura, int institucion) 
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM facturacion.cobertura WHERE codigo_cobertura=? and institucion=? ";
		boolean respuesta=true;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigoCobertura);
			ps.setInt(2, institucion);
			ps.executeUpdate();
			respuesta=false;
		}
		catch(SQLException e)
		{
			respuesta=true;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de Condicion de toma de Examen (true = depende , false = no depende)
	 * @param Connection con
	 * @param String codigoCobertura
	 * @param int institucion
	 * */
	public static boolean esCondicionTmExamenUsado(Connection con, int codigoExamen, int institucion) 
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM facturacion.examen_conditoma WHERE codigo_examenct=? and institucion=? ";
		boolean respuesta=true;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoExamen);
			ps.setInt(2, institucion);
			ps.executeUpdate();
			respuesta=false;
		}
		catch(SQLException e)
		{
			respuesta=true;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de piso  (true = depende , false = no depende)
	 * @param Connection con
	 * @param String codigoPiso
	 * @param int institucion
	 * */
	public static boolean esPisosUsado(Connection con, int codigo) 
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM pisos WHERE codigo=? ";
		boolean respuesta=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			ps.executeUpdate();
			respuesta=true;
		}
		catch(SQLException e)
		{
			respuesta=false;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de tipo habitacion  (true = depende , false = no depende)
	 * @param Connection con
	 * @param String codigo
	 * @param int institucion
	 * */
	public static boolean esTipoHabitacionUsado(Connection con, String codigo, int institucion) 
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM tipo_habitacion WHERE codigo=? and institucion=? ";
		boolean respuesta=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigo);
			ps.setInt(2, institucion);
			ps.executeUpdate();
			respuesta=true;
		}
		catch(SQLException e)
		{
			respuesta=false;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo Parametros de Almacenes (true = depende , false = no depende)
	 * @param Connection con
	 * @param String codigoAlmacenParametros
	 * @param int institucion
	 * */
	public static boolean esAlmacenParametrosUsado(Connection con, int codigoAlmacenParametros, int institucion) 
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM inventarios.almacen_parametros WHERE codigo=? and institucion=? ";
		boolean respuesta=true;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoAlmacenParametros);
			ps.setInt(2, institucion);
			ps.executeUpdate();
			respuesta=false;
		}
		catch(SQLException e)
		{
			respuesta=true;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}	
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de habitaciones  (true = depende , false = no depende)
	 * @param Connection con
	 * @param int codigo
	 * */
	public static boolean esHabitacionesUsado(Connection con, int codigo) 
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM habitaciones WHERE codigo=?  ";
		boolean respuesta=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			ps.executeUpdate();
			respuesta=true;
		}
		catch(SQLException e)
		{
			respuesta=false;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo Instituciones SIRC  (true = depende , false = no depende)
	 * @param Connection con
	 * @param String codigoInstSirc
	 * @param int institucion
	 * */
	public static boolean esInstitucionSircUsado(Connection con, String codigoInstSirc, int institucion) 
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM historiaclinica.instituciones_sirc WHERE codigo=? and institucion=? ";
		boolean respuesta=true;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigoInstSirc);
			ps.setInt(2, institucion);
			ps.executeUpdate();
			respuesta=false;
		}
		catch(SQLException e)
		{
			respuesta=true;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de tipos usuario cama  (true = depende , false = no depende)
	 * @param Connection con
	 * @param String codigo
	 * */
	public static boolean esTiposUsuarioCamaUsado(Connection con, int codigo, int institucion) 
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM tipos_usuario_cama WHERE codigo=? and institucion=? ";
		boolean respuesta=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			ps.setInt(2, institucion);
			ps.executeUpdate();
			respuesta=true;
		}
		catch(SQLException e)
		{
			respuesta=false;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de Centros de Costos  (true = depende , false = no depende)
	 * @param Connection con
	 * @param String codigoCentroCostos
	 * @param int institucion
	 * */
	public static boolean esCentroCostoUsado(Connection con,String codigoCentroCosto, int institucion) 
	{
		//UtilidadBD.iniciarTransaccionSinMensaje(con);
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM centros_costo WHERE codigo=? and institucion=? ";
		boolean respuesta=true;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(codigoCentroCosto));
			ps.setInt(2, institucion);
			ps.executeUpdate();
			ps.close();
			respuesta=false;
		}
		catch(SQLException e)
		{
			respuesta=true;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	
	/**
	 * Verifica que otras funcionalidades no manejen el consecutivo de Centros de Atencion  (true = depende , false = no depende)
	 * @param Connection con
	 * @param String codigoCentroAtencion
	 * @param int institucion
	 * */
	public static boolean esCentroAtencionUsado(Connection con, String codigoCentroAtencion, int institucion) 
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM centro_atencion WHERE consecutivo=? and cod_institucion=? ";
		boolean respuesta=true;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigoCentroAtencion);
			ps.setInt(2, institucion);
			ps.executeUpdate();
			respuesta=false;
		}
		catch(SQLException e)
		{
			respuesta=true;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	
	/**
	 * Verifica que otras funcionalidades no manejen el codigo de Asocio (true = depende , false = no depende)
	 * @param Connection con
	 * @param String codigoAsocio
	 * @param int institucion
	 * */
	public static boolean esAsocioSalaCirugiaUsado(Connection con, String codigoAsocio, int institucion) 
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM tipos_asocio WHERE codigo_asocio=? and institucion=? ";
		boolean respuesta=true;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigoAsocio);
			ps.setInt(2, institucion);
			ps.executeUpdate();
			respuesta=false;
		}
		catch(SQLException e)
		{
			respuesta=true;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}

	/**
	 * Metodo encargado de verificar si un concepto especifico es 
	 * siendo utilizado o no.
	 * @param Connection con
	 * @param String codigo
	 * @param int institucion
	 * */
	public static boolean esConceptoEspecificoUsado(Connection con, String codigo, String institucion) 
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM glosas.conceptos_especificos WHERE codigo="+codigo+" AND institucion="+institucion;
		boolean respuesta=true;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.executeUpdate();
			respuesta=false;
		}
		catch(SQLException e)
		{
			respuesta=true;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static boolean esContrarreferencia(Connection con, int codigoIngreso)
	{
		String cadena="SELECT count(1) AS contrarreferencia FROM cuentas WHERE id_ingreso=? and tipo_evento='"+ConstantesIntegridadDominio.acronimoContrarreferencia+"'";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoIngreso);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt(1)>0;
		} 
		catch (SQLException e) 
		{
			logger.error("Error consultando esContrarreferencia : "+e);
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param estado
	 * @return
	 */
	public static boolean esContrarreferenciaEstadoDado(Connection con, int idIngreso, String estado) 
	{
		String cadena="SELECT ingreso from referencia WHERE ingreso=? AND estado IN ('"+estado+"')";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,idIngreso);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return true;
		} 
		catch (SQLException e) 
		{
			logger.error("Error consultando esContrarreferenciaEstadoDado : "+e);
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean convenioManejaComplejidad(Connection con, int codigoConvenio) 
	{
		String cadena="SELECT maneja_complejidad  from convenios WHERE codigo=?";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoConvenio);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return UtilidadTexto.getBoolean(rs.getString(1));
			}
		} 
		catch (SQLException e) 
		{
			logger.error("Error consultando convenioManejaComplejidad : "+e);
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param numeroCuentaCobro
	 * @return
	 */
	public static boolean esNumeroCuentaCobroUsado(Connection con, double numeroCuentaCobro) 
	{
		String cadena="SELECT count(1) from cuentas_cobro where numero_cuenta_cobro=?";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,numeroCuentaCobro);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt(1)>0;
		} 
		catch (SQLException e) 
		{
			logger.error("Error consultando esNumeroCuentaCobroUsado : "+e);
		}
		return true;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean esConvenioUsado(Connection con, int codigo) 
	{	
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM convenios WHERE codigo=? ";
		boolean respuesta=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			ps.executeUpdate();
			respuesta=false;
		}
		catch(SQLException e)
		{
			respuesta=true;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static int esCuentaValida(Connection con, int codigoCuenta) 
	{
		try
		{
			int cuenta = 0;
			String cadena="SELECT  " +
				"c.id " +
				"from cuentas c " +
				"inner join ingresos i on(c.id_ingreso=i.id) " +
				"where " +
				"c.id=? and " +
				"(c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaActiva+" OR  " +
					"c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaFacturadaParcial+" OR " +
					"c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaAsociada+") AND getcuentafinal(c.id) IS NULL ";

			//logger.info("\nesCuentaValida-->"+cadena+"cuenta="+codigoCuenta+"\n");
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoCuenta);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				cuenta = rs.getInt("id");
			return cuenta;
		}
		catch(SQLException e)
		{
			logger.error("Error en esCuentaValida: "+e);
			return 0;
		}
	}

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param codigoEstado
	 * @return
	 */
	public static boolean responsableTieneServicioEstado(Connection con, String subCuenta, int codigoEstado) 
	{
		try
		{
			String cadena="SELECT count(1) from det_cargos where sub_cuenta="+Utilidades.convertirALong(subCuenta)+" and estado="+codigoEstado;
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getInt(1)>0;
		}
		catch(SQLException e)
		{
			logger.error("Error en esCuentaValida: "+e);
		}
		return false;
	}

	/**
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static boolean ingresoEstaEnProcesoDistribucion(Connection con, int codigoIngreso,String usuario) 
	{
		try
		{
			String cadena="";
			if(UtilidadTexto.isEmpty(usuario))
				cadena = "SELECT count(1) FROM ingresos_procesos_distribucion WHERE id_ingreso = "+codigoIngreso;
			else
				cadena = "SELECT count(1) FROM ingresos_procesos_distribucion WHERE id_ingreso = "+codigoIngreso+" AND usuario!='"+usuario+"'";
			
			logger.info("===>Consulta Ingreso Proceso de Distribución: "+cadena);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getInt(1)>0;
		}
		catch(SQLException e)
		{
			logger.error("Error en ingresoEstaEnProcesoDistribucion: "+e);
		}
		return false;
	}	 
	
	/**
	 * 
	 * @param con
	 * @param tipoId
	 * @param numeroId
	 * @return
	 * @throws SQLException
	 */
	public static boolean existePacientes (Connection con, String tipoId, String numeroId)
	{
		try
		{
			ResultSetDecorator rs=null;
	
			String consultaExisteUsuarioStr="select codigo as numResultados from pacientes p, personas per where per.numero_identificacion=? and per.tipo_identificacion=? and per.codigo=p.codigo_paciente";
			PreparedStatementDecorator consultaExisteUsuarioStatement= new PreparedStatementDecorator(con.prepareStatement(consultaExisteUsuarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultaExisteUsuarioStatement.setString(1, numeroId);
			consultaExisteUsuarioStatement.setString(2, tipoId);
	
			rs=new ResultSetDecorator(consultaExisteUsuarioStatement.executeQuery());
			
			return rs.next();
		}
		catch(SQLException e)
		{
			logger.error("Error en existePacientes: "+e);
		}
		return false;

	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public static boolean pacienteTieneMovimientoAbonos(Connection con, int codigoPersona) 
	{
		try
		{
			ResultSetDecorator rs=null;
	
			String consultaExisteUsuarioStr="SELECT count(1) AS num_resultados FROM movimientos_abonos WHERE paciente=?";
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con, consultaExisteUsuarioStr);
			ps.setInt(1, codigoPersona);
			rs=new ResultSetDecorator(ps.executeQuery());
			boolean resultado=false;
			if(rs.next())
			{
				resultado=rs.getInt("num_resultados")>0;
			}
			rs.close();
			ps.close();
			return resultado;
		}
		catch(SQLException e)
		{
			logger.error("Error en pacienteTieneMovimientoAbonos: ",e);
		}
		return false;
	}

	/**
	 * Método para verificar si el paciente tiene o no evoluciones para el día en que se genera la orden
	 * @param con
	 * @param codigoCuenta
	 * @param fecha
	 * @return
	 */
	public static boolean tieneEvolucionesParaElDia(Connection con, int codigoCuenta, String fecha) 
	{
		String tieneEvolucionesStr="SELECT count(1) AS numResultados FROM evoluciones ev INNER JOIN solicitudes sol " +
										"ON(sol.numero_solicitud=ev.valoracion) WHERE sol.cuenta=? and ev.fecha_evolucion=?";
		try
		{
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(tieneEvolucionesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setInt(1, codigoCuenta);
			statement.setString(2, fecha);
			ResultSetDecorator resultado=new ResultSetDecorator(statement.executeQuery());
			if(resultado.next())
			{
				if(resultado.getInt("numResultados")>0)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando la existencia de evoluciones para la cuenta "+codigoCuenta+" con Fecha de Evolucion"+fecha+": "+e);
			return false;
		}
	}
	
	
	/**
	 * Valida si las notas de enfermeria esta cerrada
	 * @param Connection con
	 * @param int codigoCuenta
	 * */
	public static boolean esCerradaNotasEnfermeria(Connection con, int codigoCuenta)
	{
		PreparedStatement pst=null;
		ResultSet rs= null;
		try
		{
			String cadena = "SELECT nota_finalizada FROM registro_enfermeria WHERE cuenta = ? ";
			pst = con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1,codigoCuenta);
			
			rs = pst.executeQuery();
			
			if(rs.next()){
				if(rs.getString(1).equals(ConstantesBD.acronimoSi)){
					return true;
				}
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerTarifaBaseServicio",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerTarifaBaseServicio", e);
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
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static String[] obtenerFechaYHoraCuidadoEspecial(Connection con, int idCuenta) 
	{
		String cuidadoEspecial[]={"","",""};
		
		try
		{
		String consultaStr= "SELECT tc.fecha_asignacion AS fecha_asignacion, substr(tc.hora_asignacion,1,5) AS hora_asignacion, c.es_uci as es_uci " +
											"FROM traslado_cama tc " +
											"INNER JOIN camas1 c ON(c.codigo=tc.codigo_nueva_cama) " +
											"WHERE cuenta=? ";
		
		PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		ps.setInt(1, idCuenta);
		
		ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());

			if (rs.next())
			{
				
				cuidadoEspecial[0]=rs.getString("es_uci");
				cuidadoEspecial[1]=rs.getString("fecha_asignacion");
				cuidadoEspecial[2]=rs.getString("hora_asignacion");
				
			}
		}	
		catch (SQLException e) 
		{
			logger.error("Error en obtenerFechaMesCierreSaldoCapitacion de SqlBaseUtilidadValidacionDao: "+e);
		}
		return cuidadoEspecial;
	}

	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static boolean tipoMonitoreoRequiereValoracion(Connection con, int idCuenta) 
	{
		String cadena="select requiere_valoracion from ingresos_cuidados_especiales i inner join tipo_monitoreo tm on(tm.codigo=i.tipo_monitoreo) inner join cuentas c on(c.id_ingreso=i.ingreso) where c.id=? and i.estado='"+ConstantesIntegridadDominio.acronimoEstadoActivo+"' ";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,idCuenta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return UtilidadTexto.getBoolean(rs.getString(1));
			}
		} 
		catch (SQLException e) 
		{
			logger.error("Error consultando tipo monitoreo : "+e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static boolean tieneValoracionCuidadoEspecial(Connection con, int idCuenta) 
	{
		String cadena="select count(*) as cuenta from ingresos_cuidados_especiales i inner join cuentas c on(c.id_ingreso=i.ingreso) where c.id=? and i.estado='"+ConstantesIntegridadDominio.acronimoEstadoActivo+"' and i.valoracion is not null ";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info("cadenaVal>>>>>>>>>>>>>"+cadena);
			logger.info("cadenaVal>>>>>>>>>>>>>"+idCuenta);
			
			ps.setInt(1,idCuenta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getInt(1)>0?true:false;
			}
		} 
		catch (SQLException e) 
		{
			logger.error("Error consultando tipo monitoreo : "+e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static boolean tieneIngresoActivoCuidadoEspecial(Connection con, int idCuenta) 
	{
		String cadena="select i.codigo from ingresos_cuidados_especiales i inner join cuentas c on(c.id_ingreso=i.ingreso) where c.id=? and i.estado='"+ConstantesIntegridadDominio.acronimoEstadoActivo+"' ";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try 
		{
			ps= con.prepareStatement(cadena);
			ps.setInt(1,idCuenta);
			rs=ps.executeQuery();
			if(rs.next())
			{
				return true;
			}
		} 
		catch (SQLException e) 
		{
			logger.error("Error consultando tipo monitoreo : "+e);
		}
		finally{
			if(ps != null){				
				try {
					ps.close();
				} catch (SQLException e) {}
			}
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {}
			}
		}
		
		return false;
	}

	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static boolean monitoreoEvolucionRequiereValoracion(Connection con, int codigoEvolucion) 
	{
		String cadena="select requiere_valoracion from evoluciones e inner join tipo_monitoreo tm on(tm.codigo=e.proced_quirurgicos_obst) where e.codigo=? ";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info("cadena>>>>>>>>>"+cadena);
			logger.info("codigoEvolucion>>>>>>>>>"+codigoEvolucion);
			
			ps.setInt(1,codigoEvolucion);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return true;
			}
			else
			{
				return false;
			}
		} 
		catch (SQLException e) 
		{
			logger.error("Error consultando tipo monitoreo : "+e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEvolucion
	 * @return
	 */
	public static boolean conductaSeguirUltimaEvolucion(Connection con, int codigoEvolucion) 
	{
		String cadena="select conducta_seguir from evoluciones where codigo=? and conducta_seguir="+ConstantesBD.codigoConductaASeguirTrasladarAPisoEvolucion+" ";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info("cadena CS >>>>>>>>>>"+cadena);
			logger.info("codigoEvolucion CS >>>>>>>>>>"+codigoEvolucion);
			
			ps.setInt(1,codigoEvolucion);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return true;
			}
		} 
		catch (SQLException e) 
		{
			logger.error("Error consultando tipo monitoreo : "+e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static boolean tieneIngresoCuidadosFinalizado(Connection con, int codigoIngreso) 
	{
		String cadena="select codigo from ingresos_cuidados_especiales where ingreso=? and estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"' ";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info("cadena FIN >>>>>>>>>>"+cadena);
			logger.info("codigoIngreso FIN >>>>>>>>>>"+codigoIngreso);
			
			ps.setInt(1,codigoIngreso);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return true;
			}
		} 
		catch (SQLException e) 
		{
			logger.error("Error consultando tipo monitoreo : "+e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static boolean pacienteFalleceCirugia(Connection con, int codigoCuenta) 
	{
		String cadena="select sc.numero_solicitud from solicitudes_cirugia sc inner join solicitudes s on(s.numero_solicitud=sc.numero_solicitud) where s.cuenta=? and fecha_fallece is not null and hora_fallece is not null ";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info("PACIENTE FALLE EN CIRUGIA >>"+cadena);
			logger.info("PACIENTE FALLE EN CIRUGIA --> CUENTA >>"+codigoCuenta);
			
			ps.setInt(1,codigoCuenta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return true;
			}
		} 
		catch (SQLException e) 
		{
			logger.error("Error consultando Info Fallecimiento : "+e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static boolean estadoSolicitudAnulacion(Connection con, int codigoFactura) 
	{
		String cadena="SELECT codigo_pk FROM solicitud_anul_fact where codigo_factura=? and (estado='"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"' OR  (estado='"+ConstantesIntegridadDominio.acronimoEstadoPendiente+"' AND (anulacion_autorizada='"+ConstantesBD.acronimoSi+"' OR anulacion_autorizada IS NULL ))) ";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info("SOLICITUD ANULACION FACTURA >>"+cadena);
			logger.info("SOLICTUD ANULACION FACTURA --> FACTURA >>"+codigoFactura);
			
			ps.setInt(1,codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return true;
			}
		} 
		catch (SQLException e) 
		{
			logger.error("Error consultando Solicitud Anulacion Factura : "+e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param loginUsuario
	 * @return
	 */
	public static boolean usuarioAutorizadoAnular(Connection con, String loginUsuario, int centroAtencion) 
	{
		String cadena="SELECT codigo FROM usu_autorizan_anul_fac where usuario=? AND centro_atencion=? ";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info("SOLICITUD ANULACION FACTURA >>"+cadena);
			logger.info("SOLICTUD ANULACION FACTURA --> USUARIO >>"+loginUsuario);
			logger.info("SOLICTUD ANULACION FACTURA --> CENTRO ATENCION >>"+loginUsuario);
			
			ps.setString(1,loginUsuario);
			ps.setInt(2, centroAtencion);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return true;
			}
		} 
		catch (SQLException e) 
		{
			logger.error("Error consultando Solicitud Anulacion Factura : "+e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static boolean facturaAprobadaAnular(Connection con, int codigoFactura) 
	{
		String cadena="SELECT codigo_pk FROM solicitud_anul_fact where codigo_factura=? AND estado='"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"' ";
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info("SOLICITUD ANULACION FACTURA >>"+cadena);
			logger.info("SOLICTUD ANULACION FACTURA --> USUARIO >>"+codigoFactura);
			
			ps.setInt(1,codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return true;
			}
		} 
		catch (SQLException e) 
		{
			logger.error("Error consultando Solicitud Anulacion Factura : "+e);
		}
		return false;
	}
	
	
	/**
	 * verifica si el paciente se encuentra como paciente con capitacion vigente
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static InfoDatosString esPacienteCapitadoVigente(Connection con, HashMap parametros, String cadena)
	{
		InfoDatosString respuesta = new InfoDatosString();
		respuesta.setActivo(false);
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				respuesta.setActivo(true);
				respuesta.setDescripcion(rs.getString(1));
				respuesta.setCodigo(rs.getString(2));				
			}
		}
		catch (SQLException e)
		{
			logger.info("error en es paciente capitado vigente >> "+parametros+" >> "+cadena);
			logger.error("ERROR", e);
		}		
		
		return respuesta;
	}
	
	/**
	 * Método que indica si un contrato se 
	 * esta usando en otras funcionalidades 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean esContratoUsado(Connection con, int codigo) 
	{	
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		boolean respuesta = false;
		String cadena = "DELETE FROM esq_tar_inventarios_contrato WHERE contrato = ? ";
		//logger.info("===>Cadena Consulta: "+cadena+" ===>Contrato: "+codigo);
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			if(ps.executeUpdate() > 0)
			{
				cadena = "DELETE FROM esq_tar_procedimiento_contrato WHERE contrato = ? ";
				//logger.info("===>Cadena Consulta: "+cadena+" ===>Contrato: "+codigo);
				try
				{
					ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, codigo);
					if(ps.executeUpdate() > 0)
					{
						cadena = "DELETE FROM contratos WHERE codigo = ? ";
						//logger.info("===>Cadena Consulta: "+cadena+" ===>Contrato: "+codigo);
						try
						{
							ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							ps.setInt(1, codigo);
							if(ps.executeUpdate() > 0)
								respuesta = false;
							else
								respuesta = true;
						}
						catch(SQLException e)
						{
							respuesta = true;
						}
					}
					else
						respuesta = true;
				}
				catch(SQLException e)
				{
					respuesta = true;
				}
			}
			else
				respuesta = true;
		}
		catch(SQLException e)
		{
			respuesta = true;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}

	/**
	 * Método encargado de verificar si un texto respuesta procedimiento está siendo usado o no
	 * retorna true si el texto NO es usado
	 * retorna false si el texto SI es usado
	 * @author Ing. Felipe Pérez Granda
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return boolean true/false 
	 */
	public static boolean esTextoRespuestaProcedimientosUsado(Connection con, String codigo, String institucion)
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena = "DELETE FROM textos_resp_proc WHERE codigo = "+codigo+" AND institucion = "+institucion;
		boolean respuesta=true;
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if (ps.executeUpdate()>0)
			respuesta = false;
		}
		catch(SQLException e)
		{
			respuesta = true;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoGrupo
	 * @return
	 */
	public static boolean esGrupoEspecialUsado(Connection con, String codigoGrupo) 
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM grupo_especial_articulos WHERE codigo_pk=? ";
		boolean respuesta=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigoGrupo);
			ps.executeUpdate();
			respuesta=true;
		}
		catch(SQLException e)
		{
			respuesta=false;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoAjuste
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean esConceptoAjusteUsado(Connection con, String codigoAjuste, int codigoInstitucion) 
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM concepto_ajustes_cartera WHERE codigo=? and institucion=? ";
		boolean respuesta=false;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codigoAjuste);
			ps.setInt(2, codigoInstitucion);
			ps.executeUpdate();
			respuesta=true;
		}
		catch(SQLException e)
		{
			respuesta=false;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}

	/**
	 * Metodo encargado de consultar el ultimo ingreso
	 * del paciente y verificar si este tuvo tipo evento
	 * catastrófico
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static int esUltimoIngreso(Connection con, String codigoPaciente, int tipoBD)
	{
		String cadena = "SELECT id AS id FROM ingresos WHERE codigo_paciente = "+codigoPaciente+" ";
		
		switch(tipoBD)
		{
			case DaoFactory.ORACLE:
				cadena += "AND rownum = 1 ORDER BY id DESC ";
			break;
			case DaoFactory.POSTGRESQL:
				cadena += "ORDER BY id DESC "+ValoresPorDefecto.getValorLimit1()+" 1 ";
			break;
		}
		 
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			logger.info("===>Consulta Ultimo Ingreso: "+cadena);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt("id");
		} 
		catch (SQLException e) 
		{
			logger.error("Error consultando esUltimoIngreso: "+e);
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Método encargado de verificar si un artículo está siendo usado o no
	 * retorna false si el artículo NO es usado
	 * retorna true si el artículo SI es usado
	 * @author Ing. Felipe Pérez Granda
	 * @param con Connection
	 * @param codArticulo String
	 * @param institucion String
	 * @return respuesta true/false boolean 
	 */
	public static boolean esArticuloUsado(Connection con, String codArticulo, String institucion)
	{
		logger.info("===> Voy a validar si el artículo con codigo: "+codArticulo+", institución : "+institucion+" está siendo usado");
		PreparedStatementDecorator ps = null;
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		boolean respuesta=false;
		
		//Se comenta porque no se esta usando este bloque.
		/*esArticuloUsadoDependencia(con, codArticulo, "DELETE FROM articulos_info_medica WHERE codigo_articulo="+codArticulo);
		esArticuloUsadoDependencia(con, codArticulo, "DELETE FROM grupo_especial_x_articulo WHERE articulo="+codArticulo);
		esArticuloUsadoDependencia(con, codArticulo, "DELETE FROM unidosis_x_articulo WHERE articulo="+codArticulo);
		esArticuloUsadoDependencia(con, codArticulo, "DELETE FROM vias_admin_articulo WHERE articulo="+codArticulo);*/
		
		String cadena = "DELETE FROM articulo WHERE codigo = "+codArticulo+" AND institucion = "+institucion;
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if (ps.executeUpdate()>0)
				respuesta = false;
		}
		catch(SQLException e)
		{
			//e.printStackTrace();
			respuesta = true;
		} finally {
			try {
				if(ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				Log4JManager.error("esArticuloUsado :ERROR CERRANDO EL PREPARED STATEMENT");
			}
		}
			
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}
	
	/**
	 * 
	 * @param con
	 * @param codArticulo
	 * @return
	 */
	private static boolean esArticuloUsadoDependencia(Connection con, String codArticulo, String cadenaSql)
	{
		boolean respuesta=false;
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaSql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if (ps.executeUpdate()>0)
			respuesta = true;
		}
		catch(SQLException e)
		{
			respuesta = true;
		}
		return respuesta;
	}

	/**
	 * 
	 * @param con
	 * @return
	 */
	public static String obtenerMaximoConsecutivoJustificacionNoPosArticulos(Connection con) 
	{
		String cadena="select to_number(max(consecutivo),'999999999999999999999999') from justificacion_art_sol";
		logger.info(cadena);
		String respuesta=ConstantesBD.codigoNuncaValido+"";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString(1);
		}
		catch(SQLException e)
		{
			logger.info("ERROR / obtenerMaximoConsecutivoJustificacionNoPosArticulos / "+e);
		}
		return respuesta;
	}

	/**
	 * 
	 * @param con
	 * @return
	 */
	public static String obtenerMaximoConsecutivoJustificacionNoPosServicios(Connection con) 
	{
		String cadena="select to_number(max(consecutivo),'999999999999999999999999') from justificacion_serv_sol";
		
		String respuesta=ConstantesBD.codigoNuncaValido+"";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString(1);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return respuesta;
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoUsuario
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean esUsuarioAUtorizadoAnulacionUsado(Connection con, String codigoUsuario, int codigoInstitucion) 
	{
		UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena = "DELETE FROM usu_autorizan_anul_fac WHERE codigo = "+codigoUsuario+" AND institucion = "+codigoInstitucion;
		boolean respuesta=true;
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if (ps.executeUpdate()>0)
				respuesta = false;
		}
		catch(SQLException e)
		{
			respuesta = true;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return respuesta;
	}

	public static boolean validarPacienteEnValoracion(Connection con,
			int codigoPersona) {
		String sentencia="SELECT paciente FROM historiaclinica.pacientes_en_valoracion WHERE paciente=?";
		try {
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			psd.setInt(1, codigoPersona);
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			boolean resultado=false;
			if(rsd.next())
			{
				resultado=true;
			}
			psd.close();
			return resultado;
		} catch (SQLException e) {
			logger.error("Error verificando paciente en valoracion",e);
		}
		return false;
		
	}

	public static boolean esDeudorCarteraPaciente(Connection con, PersonaBasica paciente) {
		try{
			String sentencia=
					"SELECT " +
						"count(1) AS resultados " +
					"FROM " +
						"carterapaciente.deudorco " +
					"WHERE " +
						"codigo_paciente=? ";
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
			psd.setInt(1, paciente.getCodigoPersona());
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			boolean esDeudor=false;
			if(rsd.next())
			{
				esDeudor=rsd.getInt("resultados")>0;
			}
			rsd.close();
			psd.close();
			return esDeudor;
		}catch (SQLException e)
		{
			logger.error("Error varificando si el paciente "+paciente.getNombrePersona()+" "+paciente.getCodigoTipoIdentificacionPersona()+":"+paciente.getNumeroIdentificacionPersona()+"es deudor", e);
		}
		return false;
	}

	public static boolean verificarBloqueoIngresoPacienteODeudor(Connection con, PersonaBasica paciente, int viaIngreso) {
		try{
			String sentenciaConsultarBloqueos=
					"SELECT " +
						"bloquea_ing_deu_x_saldo_mora AS bloquea_ing_deu_x_saldo_mora, " +
						"bloquea_ing_pac_x_saldo_mora AS bloquea_ing_pac_x_saldo_mora " +
					"FROM manejopaciente.garantia_paciente " +
					"WHERE " +
						"acronimo_tipo_paciente=? " +
						"AND codigo_via_ingreso=?";
			PreparedStatementDecorator psdConsultaBloqueos=new PreparedStatementDecorator(con, sentenciaConsultarBloqueos);
			psdConsultaBloqueos.setString(1, paciente.getCodigoTipoPaciente());
			psdConsultaBloqueos.setInt(2, viaIngreso);
			logger.info(psdConsultaBloqueos);
			boolean bloqueaDeudor=false;
			boolean bloqueaPaciente=false;
			ResultSetDecorator rsdConsultaBloqueos= new ResultSetDecorator(psdConsultaBloqueos.executeQuery());
			if(rsdConsultaBloqueos.next())
			{
				bloqueaDeudor=UtilidadTexto.getBoolean(rsdConsultaBloqueos.getString("bloquea_ing_deu_x_saldo_mora"));
				bloqueaPaciente=UtilidadTexto.getBoolean(rsdConsultaBloqueos.getString("bloquea_ing_pac_x_saldo_mora"));
			}
			if(esDeudorCarteraPaciente(con, paciente)&&bloqueaDeudor)
			{
				return true;
			}
			else
			{
				return bloqueaPaciente;
			}
		}catch (SQLException e) {
			logger.error("Error verificando el bloqueo del ingreso por deudor", e);
		}
		return false;
	}

	public static boolean tieneCuotasPendientes(Connection con, PersonaBasica paciente, int numeroDiasMora) {
		String sentencia=
				"SELECT " +
					"fecha_inicio +((cdf.nro_cuota-1)*df.dias_por_couta) AS fecha_limite " +
				"FROM " +
					"carterapaciente.documentos_garantia dg " +
				"INNER JOIN " +
					"carterapaciente.datos_financiacion df " +
						"ON(dg.codigo_pk=df.codigo_pk_docgarantia) " +
				"INNER JOIN " +
					"carterapaciente.cuotas_datos_financiacion cdf " +
						"ON(cdf.dato_financiacion=df.codigo_pk) " +
				"WHERE " +
						"codigo_paciente=? " +
					"AND " +
						"fecha_inicio +((cdf.nro_cuota+1)*df.dias_por_couta) < CURRENT_DATE-?";
		try{
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
			psd.setInt(1, paciente.getCodigoPersona());
			psd.setInt(2, numeroDiasMora);
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			boolean resultado=rsd.next();
			rsd.close();
			psd.close();
			return resultado;
		}catch (SQLException e) {
				logger.error("Error consultando si el paciente tiene cuotas pendientes ",e);
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param convencion
	 * @return
	 */
	public static boolean existeConvencionMedico(Connection con, int convencion) {
		String sentencia= "SELECT codigo_medico FROM medicos WHERE convencion=? ";
		try{
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
			psd.setInt(1, convencion);
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			boolean resultado=rsd.next();
			rsd.close();
			psd.close();
			return resultado;
		}catch (SQLException e) {
			logger.error("Error consultando si existe la convencion del medico ",e);
		}
		return false;
	}
	
	
	/**
 * 	
 * @param con
 * @param codigoPersona
 * @param loginUsuario
 * @return
 */
	public static boolean validarPacienteEnValoracionDiferenteUsuario(Connection con,
			int codigoPersona, String loginUsuario) {
		String sentencia="SELECT paciente FROM historiaclinica.pacientes_en_valoracion WHERE paciente=? AND usuario<>? ";
		try {
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			psd.setInt(1, codigoPersona);
			psd.setString(2,loginUsuario);
			logger.info("\n\n CONSULTA validarPacienteEnValoracionDiferenteUsuario >> "+sentencia+" codigoPersona >"+codigoPersona+" usuario >"+loginUsuario);
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			boolean resultado=false;
			if(rsd.next())
			{
				resultado=true;
			}
			psd.close();
			return resultado;
		} catch (SQLException e) {
			logger.error("Error verificando paciente en valoracion",e);
		}
		return false;
		
	}

	/**
	 * 
	 * @param institucion
	 * @param valorConsecutivo
	 * @return
	 */
	public static boolean esConsecutvioReciboCajaUsado(int institucion,
			String valorConsecutivo) {
		Connection con=UtilidadBD.abrirConexion();
		boolean resultado=false;
		String consulta = "SELECT count(1) from tesoreria.recibos_caja where consecutivo_recibo =? and institucion=?";
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setString(1, valorConsecutivo);
			pst.setInt(2, institucion);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				if(rs.getInt(1) > 0)
				{
					pst.close();
					rs.close();
					resultado=true;
				}
			pst.close();
			rs.close();
		}
		catch(SQLException e)
		{
		logger.error("Error en esConsecutvioAsignadoRecibo: "+e);
		}
		UtilidadBD.closeConnection(con);
		return resultado;
	}

	public static boolean esNaturalezaValidaTipoRegimen(int natPaciente,
			String tipoRegimen) {
		Connection con=UtilidadBD.abrirConexion();
		boolean resultado=false;
		String consulta = "select count(1) from manejopaciente.EXCEPCIONES_NATURALEZA where acr_regimen='"+tipoRegimen+"' and cod_naturaleza="+natPaciente;
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				if(rs.getInt(1) > 0)
				{
					pst.close();
					rs.close();
					resultado=true;
				}
			pst.close();
			rs.close();
		}
		catch(SQLException e)
		{
		logger.error("Error en esConsecutvioAsignadoRecibo: "+e);
		}
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	  
	/**
	 * Método que verifica si para ese egreso su estado de salida fue muerto
	 * @param con
	 * @param cuenta
	 * @return
	 * @author javrammo
	 * @since ipsm_1.1.1
	 */
	public static boolean esEvolucionConOrdenSalidaPacienteMuerto(Connection con,int idCuenta, int idEvolucion)
	{
		boolean resp = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			if(con != null){
				ps = con.prepareStatement(EVOLUCION_CON_EGRESO_ESTADO_SALIDA_MUERTO);
				ps.setInt(1, idCuenta);
				ps.setInt(2, idEvolucion);
				rs = ps.executeQuery();
				if(rs.next()){
					resp = true;
				}
			}			
			
		} catch (Exception e) {
			logger.error("Error en egresoConEstadoSalidaMuerto: "+e);
		}finally{
			if(ps != null){
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error("Error en egresoConEstadoSalidaMuerto: "+e);
				}
			}
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("Error en egresoConEstadoSalidaMuerto: "+e);
				}
			}
		}
	
		return resp;
	}	

}