package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import org.apache.log4j.Logger;


import util.ConstantesBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares Validaciones Solicitud 
 *
 *	@version 1.0,Abril 31, 2004
 */
public class SqlBaseValidacionesSolicitudDao 
{
	private static Logger logger=Logger.getLogger(SqlBaseValidacionesSolicitudDao.class);
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar 
	 * un signo vital una BD Genérica. 
	 */
	static String cargarDatosBasicosStr="SELECT cuenta as codigoCuenta, centro_costo_solicitante as codigoCentroCostoQueSolicita, centro_costo_solicitado as codigoCentroCostoSolicitado, ocupacion_solicitada as codigoOcupacionSolicitada, tipo as codigoTipoSolicitud, estado_historia_clinica as codigoEstadoHistoriaClinica, codigo_medico as codigoMedicoCreador, codigo_medico_interpretacion as codigoMedicoInterpretacion from solicitudes where numero_solicitud=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para obtener 
	 * el código de manejo de la interconsulta para una BD Genérica.  
	 */
	private static String cargarCodigoManejoInterconsultaStr ="SELECT soli.codigo_manejo_interconsulta  as codigoManejoInterconsulta, serv.especialidad as codigoEspecialidad from solicitudes_inter soli, servicios serv where soli.codigo_servicio_solicitado=serv.codigo and soli.numero_solicitud=?";
	
	/**
	 * Primera parte de la cadena constante con el <i>statement</i> 
	 * necesario para saber si una solicitud es de tipo otros para 
	 * una BD Genérica.  
	 */
	private static String esTipoOtrosStr1="SELECT count(1) as numResultados from ";
	
	/**
	 * Segunda parte de la cadena constante con el <i>statement</i> 
	 * necesario para saber si una solicitud es de tipo otros para 
	 * una BD Genérica.  
	 */
	private static String esTipoOtrosStr2=" where numero_solicitud =? and nombre_otros is not null";

	/**
	 * Cadena constante con el <i>statement</i> necesario para revisar 
	 * si se pidió cambio de tratante para una BD Genérica.  
	 */
	private static String tieneSolicitudCambioTratanteStr="SELECT count(1) as numResultados from sol_cambio_tratante where solicitud=? and activa=" + ValoresPorDefecto.getValorTrueParaConsultas() + "";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para revisar 
	 * si se puede responder una solicitud en caso de ser de interconsulta
	 * para una BD Genérica.  
	 */
	private static String puedoResponderSolicitudCasoConsultaExternaStr="SELECT count(1) as numResultados "+ 
		"from cita ci "+ 
		"INNER JOIN servicios_cita sc ON(sc.codigo_cita=ci.codigo) "+
		"INNER JOIN agenda ag ON(ag.codigo=ci.codigo_agenda) "+ 
		"where sc.numero_solicitud=? AND (ci.estado_cita="+ConstantesBD.codigoEstadoCitaAsignada+" OR ci.estado_cita="+ConstantesBD.codigoEstadoCitaReprogramada+") and ag.codigo_medico=?";
	
	private static String puedoModificarSolicitudCasoConsultaExternaStr=
		"SELECT count(1) as numResultados "+ 
		"from cita ci "+ 
		"INNER JOIN servicios_cita sc ON(sc.codigo_cita=ci.codigo) "+
		"INNER JOIN agenda ag ON(ag.codigo=ci.codigo_agenda) "+ 
		"where sc.numero_solicitud=? ";
	
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para saber
	 * la respuesta de una interconsulta es pediátrica para una BD 
	 * Genérica.  
	 */
	private static String respuestaInterconsultaEsPediatricaStr="SELECT count(1) as numResultados from valoraciones_pediatricas where numero_solicitud=?";

    /**
     * Cadena constante con el <i>statement</i> necesario para saber
     * la respuesta de una interconsulta es de odontología para una BD 
     * Genérica.  
     */
    private static String respuestaInterconsultaEsOdontologiaStr="SELECT count(1) as numResultados from val_odontologia where num_valoracion=?";

    /**
	 * Cadena constante con el <i>statement</i> necesario para saber
	 * una solicitud está anulada para una BD Genérica.  
	 */
	private static String estaSolicitudAnuladaStr="SELECT count(1) as numResultados from anulaciones_solicitud where solicitud=?";
	
	/**
	 * Cadena para consultar si una solicitud de ha sido despachada o no. con el motoro Oracle se debe referir a una tabla dummy llamada dual
	 */
	private static String solicitudDespachadaStr=" SELECT getSolDespachada(?) as despachada";
	
	/**
	 * Cadena para consultar si una solicitud esta en estado de facturacion pendiente.
	 */
	private static String estadoFactucionPendiente=" SELECT esSolicitudTotalPendiente(numero_solicitud) as pendiente from solicitudes where numero_solicitud =?";

	/**
	 * Cadena para consultar si una solicitud esta en estado solicitada.
	 */
	private static String estadoSolicitada=" SELECT case when estado_historia_clinica="+ConstantesBD.codigoEstadoHCSolicitada+" then " + ValoresPorDefecto.getValorTrueParaConsultas() + " else " + ValoresPorDefecto.getValorFalseParaConsultas() + " end as solicitada from solicitudes where numero_solicitud =?";
	
	/**
	 * Cadena para consultar si una solicitud esta en estado solicitada.
	 */
	private static String estadoDespachada=" SELECT case when estado_historia_clinica="+ConstantesBD.codigoEstadoHCDespachada+" then " + ValoresPorDefecto.getValorTrueParaConsultas() + " else " + ValoresPorDefecto.getValorFalseParaConsultas() + " end as despachada from solicitudes where numero_solicitud =?";
	/**
	 * Implementación del método que carga los datos mínimos necesarios 
	 * para conocer los permisos de acceso a una solicitud en una BD 
	 * Genérica
	 *
	 * @see com.princetonsa.dao.ValidacionesDao#cargarDatosBasicos (Connection , int ) throws SQLException
	 */
	
	/**
	 * Statement para obtener la institución que realizó la solicitud
	 */
	private static String obtenertInstitucionStr="SELECT i.codigo AS codigo, i.nit AS nit, i.razon_social AS nombre from instituciones i INNER JOIN ingresos ing ON(ing.institucion=i.codigo) INNER JOIN cuentas c ON(c.id_ingreso=ing.id) INNER JOIN solicitudes s ON(s.cuenta=c.id) where s.numero_solicitud=?";
	
	private static String obtenertInstitucionEvolucionStr="SELECT i.codigo AS codigo, i.nit AS nit, i.razon_social AS nombre from instituciones i INNER JOIN ingresos ing ON(ing.institucion=i.codigo) INNER JOIN cuentas c ON(c.id_ingreso=ing.id) INNER JOIN solicitudes s ON(s.cuenta=c.id) INNER JOIN valoraciones v ON(s.numero_solicitud=v.numero_solicitud) INNER JOIN evoluciones e ON(e.valoracion=v.numero_solicitud) where e.codigo=?";
	
	/**
	 * Solo los médicos que han atendido citas pueden solicitar procedimientos
	 */
	private static String puedoSolicitarProcedimientoConsultaExternaStr="SELECT count(1) AS numResultados "+ 
		"from cita c "+ 
		"INNER JOIN servicios_cita sc ON(sc.codigo_cita=c.codigo) "+ 
		"INNER JOIN valoraciones v ON(v.numero_solicitud=sc.numero_solicitud) "+ 
		"INNER JOIN solicitudes s ON(s.numero_solicitud=v.numero_solicitud) WHERE s.cuenta=? AND v.codigo_medico=?"; 
	
	private static String respuestaInterconsultaEsOftalmologiaStr="SELECT count(1) AS numResultados FROM (SELECT numero_solicitud FROM val_oftal_sintoma GROUP BY numero_solicitud UNION SELECT numero_solicitud FROM valoracion_of_tonometria GROUP BY numero_solicitud) valoracion_oftalmologia WHERE numero_solicitud=?";
	
	public static ResultSetDecorator cargarDatosBasicos (Connection con, int numeroSolicitud) throws SQLException
	{
		PreparedStatementDecorator cargarDatosBasicosStatement= new PreparedStatementDecorator(con.prepareStatement(cargarDatosBasicosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargarDatosBasicosStatement.setInt(1, numeroSolicitud);
		return new ResultSetDecorator(cargarDatosBasicosStatement.executeQuery());
	}

	/**
	 * Implementación del método que carga el código de manejo de una
	 * interconsulta en una BD Genérica
	 *
	 * @see com.princetonsa.dao.ValidacionesDao#cargarCodigoManejoInterconsulta (Connection , int ) throws SQLException
	 */
	public static ResultSetDecorator cargarCodigoManejoInterconsulta (Connection con, int numeroSolicitud) throws SQLException
	{
		PreparedStatementDecorator cargarCodigoManejoInterconsultaStatement= new PreparedStatementDecorator(con.prepareStatement(cargarCodigoManejoInterconsultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargarCodigoManejoInterconsultaStatement.setInt(1, numeroSolicitud);
		return new ResultSetDecorator(cargarCodigoManejoInterconsultaStatement.executeQuery());
	}
	
	/**
	 * Implementación del método que revisa si una solicitud esta anulada
	 * en una BD Genérica
	 *
	 * @see com.princetonsa.dao.ValidacionesDao#estaSolicitudAnulada (Connection , int ) throws SQLException
	 */
	public static boolean estaSolicitudAnulada (Connection con, int numeroSolicitud) throws SQLException
	{
		PreparedStatementDecorator estaSolicitudAnuladaStatement= new PreparedStatementDecorator(con.prepareStatement(estaSolicitudAnuladaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		estaSolicitudAnuladaStatement.setInt(1, numeroSolicitud);
		
		ResultSetDecorator rs=new ResultSetDecorator(estaSolicitudAnuladaStatement.executeQuery());
		
		if(rs.next())
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
			throw new SQLException ("Error en un count en estaSolicitudAnulada");
		}
	}

	

	/**
	 * Implementación del método que revisa si la solicitud es de tipo otros
	 * en una BD Genérica
	 *
	 * @see com.princetonsa.dao.ValidacionesDao#esTipoOtros (Connection , int , int ) throws SQLException
	 */
	public static  boolean esTipoOtros (Connection con, int numeroSolicitud, int codigoTipoSolicitud) throws SQLException
	{
		String nombreTabla;
		if (codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudInterconsulta)
		{
			nombreTabla=" solicitudes_inter ";
		}
		else if(true)
		{
			nombreTabla=" sol_procedimientos ";
		}
		PreparedStatementDecorator esTipoOtrosStatement= new PreparedStatementDecorator(con.prepareStatement(esTipoOtrosStr1 + nombreTabla + esTipoOtrosStr2,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		esTipoOtrosStatement.setInt(1, numeroSolicitud);

		ResultSetDecorator rs=new ResultSetDecorator(esTipoOtrosStatement.executeQuery());
		
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
			throw new SQLException ("Error en un count en esTipoOtros (SqlBaseValidacionesSolicitudDao)");
		}
	}

	/**
	 * Implementación del método que revisa si la respuesta de una solicitud
	 * de interconsulta es pediátrica o no en una BD Genérica
	 *
	 * @see com.princetonsa.dao.ValidacionesDao#respuestaInterconsultaEsPediatrica (Connection , int ) throws SQLException
	 */
	public static boolean respuestaInterconsultaEsPediatrica (Connection con, int numeroSolicitud) throws SQLException
	{
		PreparedStatementDecorator respuestaInterconsultaEsPediatricaStatement= new PreparedStatementDecorator(con.prepareStatement(respuestaInterconsultaEsPediatricaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		respuestaInterconsultaEsPediatricaStatement.setInt(1, numeroSolicitud);

		ResultSetDecorator rs=new ResultSetDecorator(respuestaInterconsultaEsPediatricaStatement.executeQuery());
	
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
			throw new SQLException ("Error en un count en respuestaInterconsultaEsPediatrica (SqlBaseValidacionesSolicitudDao)");
		}
	}

    /**
     * Implementación del método que revisa si la respuesta de una solicitud
     * de interconsulta es de odontología o no en una BD Genérica
     *
     * @see com.princetonsa.dao.ValidacionesDao#respuestaInterconsultaEsOdontologia (Connection , int ) throws SQLException
     */
    public static boolean respuestaInterconsultaEsOdontologia (Connection con, int numeroSolicitud) throws SQLException
    {
        PreparedStatementDecorator respuestaInterconsultaEsOdontologiaStatement= new PreparedStatementDecorator(con.prepareStatement(respuestaInterconsultaEsOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        respuestaInterconsultaEsOdontologiaStatement.setInt(1, numeroSolicitud);

        ResultSetDecorator rs=new ResultSetDecorator(respuestaInterconsultaEsOdontologiaStatement.executeQuery());
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
            throw new SQLException ("Error en un count en respuestaInterconsultaEsOdontologia (SqlBaseValidacionesSolicitudDao)");
        }
    }

	/**
	 * Implementación del método que revisa si una solicitud generó pedido
	 * de cambio de médico tratante en una BD Genérica
	 *
	 * @see com.princetonsa.dao.ValidacionesDao#tieneSolicitudCambioTratante (Connection , int ) throws SQLException
	 */
	public static boolean tieneSolicitudCambioTratante (Connection con, int numeroSolicitud) 
	{
		boolean exito = false;
		try
		{
			PreparedStatementDecorator tieneSolicitudCambioTratanteStatement= new PreparedStatementDecorator(con.prepareStatement(tieneSolicitudCambioTratanteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			tieneSolicitudCambioTratanteStatement.setInt(1, numeroSolicitud);
			ResultSetDecorator rs=new ResultSetDecorator(tieneSolicitudCambioTratanteStatement.executeQuery());
			if (rs.next())
			{
				if (rs.getInt("numResultados")>0)
					exito = true;
				else
					exito = false;
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en tieneSolicitudCambioTratante: "+e);
			exito = false;
		}
		
		return exito;
		
	}
	/**
	 * Metodo que retorna un boolean indicando si la solicitud tiene despachos
	 * @param con, Conexion
	 * @param numeroSolicitud, Numero de solicitud
	 * @return boolean.
	 */
	public static boolean solicitudMedicamentosTieneDespachos(Connection con, int numeroSolicitud)
	{
		PreparedStatementDecorator ps;
		String cadena=solicitudDespachadaStr;
		
		try {
			if((System.getProperty("TIPOBD")+"").equals("ORACLE"))
				cadena=cadena+" FROM DUAL ";
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,numeroSolicitud);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			rs.next();
			return rs.getBoolean("despachada");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	

	/**
	 * Implementación del método que revisa si se puede responder una
	 * solicitud en caso de ser consulta externa en una BD Genérica
	 *
	 * @see com.princetonsa.dao.ValidacionesDao#puedoResponderSolicitudCasoConsultaExterna(Connection , int , int ) throws SQLException
	 */
	public static boolean puedoResponderSolicitudCasoConsultaExterna(Connection con, int numeroSolicitud, int codigoMedicoQuiereResponder) throws SQLException
	{
		PreparedStatementDecorator puedoResponderSolicitudCasoConsultaExternaStatement= new PreparedStatementDecorator(con.prepareStatement(puedoResponderSolicitudCasoConsultaExternaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		puedoResponderSolicitudCasoConsultaExternaStatement.setInt(1, numeroSolicitud);
		puedoResponderSolicitudCasoConsultaExternaStatement.setInt(2, codigoMedicoQuiereResponder);
		ResultSetDecorator rs = new ResultSetDecorator(puedoResponderSolicitudCasoConsultaExternaStatement.executeQuery());
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
			throw new SQLException ("Error en un count en puedoResponderSolicitudCasoConsultaExterna (SqlBaseValidacionesSolicitudDao)");
		}
	}
	public static boolean puedoModificarSolicitudCasoConsultaExterna(Connection con, int numeroSolicitud) throws SQLException
		{
			PreparedStatementDecorator puedoModificarSolicitudCasoConsultaExternaStatement= new PreparedStatementDecorator(con.prepareStatement(puedoModificarSolicitudCasoConsultaExternaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			puedoModificarSolicitudCasoConsultaExternaStatement.setInt(1, numeroSolicitud);
			
			ResultSetDecorator rs = new ResultSetDecorator(puedoModificarSolicitudCasoConsultaExternaStatement.executeQuery());
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
				throw new SQLException ("Error en un count en puedoResponderSolicitudCasoConsultaExterna (SqlBaseValidacionesSolicitudDao)");
			}
		}
	/**
	 * Metodo que retorna un boolena indicando si la solicitud esta en estado facturacion pendiente; 
	 * de Medicamentos.
	 * @param con, Conexion
	 * @param nuemoroSolicitud, Numero de solicitud que se desa anular o modificar.
	 * @return boolena, true se puede a/m, false no se puede
	 */
	public static boolean solicitudEstadoFacturacionPendiente(Connection con,int nuemoroSolicitud)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(estadoFactucionPendiente,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,nuemoroSolicitud);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			rs.next();
			return UtilidadTexto.getBoolean(rs.getString(1));
		}
		catch(SQLException e)
		{
			logger.error("errore realizando la consulta solicituEstadoFacturacionPendiente"+e);
		}
		return false;
	}
	
	/**
	 * Metodo que retorna un boolena indicando si la solicitud esta en estado solicitado; 
	 * de Medicamentos.
	 * @param con, Conexion
	 * @param nuemoroSolicitud, Numero de solicitud que se desa anular o modificar.
	 * @return boolena, true se puede a/m, false no se puede
	 */
	public static boolean solicitudEstadoSolicitada(Connection con,int nuemoroSolicitud)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(estadoSolicitada,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,nuemoroSolicitud);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			rs.next();
			return rs.getBoolean("solicitada");
		}
		catch(SQLException e)
		{
			logger.error("errore realizando la consulta solicitudEstadoSolicitada"+e);
		}
		return false;
	}

	/**
	 * @param con
	 * @param numeroSolicitud
	 * @return Vector de String
	 * Posición 0 el nit
	 * Posición 1 el nombre
	 * Posición 2 el código
	 */
	public static String[] obtenerInstitucion(Connection con, int numeroSolicitud, boolean esEvolucion)
	{
		try
		{
			PreparedStatementDecorator institucion;
			if(!esEvolucion)
			{
				institucion =  new PreparedStatementDecorator(con.prepareStatement(obtenertInstitucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			}
			else
			{
				institucion =  new PreparedStatementDecorator(con.prepareStatement(obtenertInstitucionEvolucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			}
			institucion.setInt(1, numeroSolicitud);
			ResultSetDecorator resultado=new ResultSetDecorator(institucion.executeQuery());
			String[] res=new String[3];
			if(resultado.next())
			{
				res[0]=resultado.getString("nit");
				res[1]=resultado.getString("nombre");
				res[2]=resultado.getString("codigo");
				return res;
			}
			else
			{
				return null;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando la institución la cua hizo la solicitud "+e);
			return null;
		}
	}
	
	/**
	 * Método para verificar si el médico puede o no hacer solicitud de procedimientos
	 * para un paciente con vía de ingreso "Consulta Externa"
	 * @param con
	 * @param codigoCuenta
	 * @param codigoMedico
	 * @return
	 */
	public static boolean tieneCitasAtendidas(Connection con, int codigoCuenta, int codigoMedico)
	{
		try
		{
			PreparedStatementDecorator puedoSolicitarProcedimientosConsultaExterna= new PreparedStatementDecorator(con.prepareStatement(puedoSolicitarProcedimientoConsultaExternaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			puedoSolicitarProcedimientosConsultaExterna.setInt(1, codigoCuenta);
			puedoSolicitarProcedimientosConsultaExterna.setInt(2, codigoMedico);
			ResultSetDecorator rs = new ResultSetDecorator(puedoSolicitarProcedimientosConsultaExterna.executeQuery());
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
				logger.error("Error consultando si el médico ha atendido citas");
				return false;
			}
		}
		catch(SQLException e)
		{
			logger.error("Error consultando si el médico ha atendido citas");
			return false;
		}
	}

	/**
	 * Metodo que retorna un boolena indicando si la solicitud esta en estado despachada;
	 * @param con
	 * @param numeroSolicitud
	 * @return boolean, true se el estado es despachada
	 */
	public static boolean solicitudEstadoDespachada(Connection con, int numeroSolicitud)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(estadoDespachada,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,numeroSolicitud);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			rs.next();
			return rs.getBoolean("despachada");
		}
		catch(SQLException e)
		{
			logger.error("errore realizando la consulta solicitudEstadodespachada "+e);
		}
		return false;
	}

    /**
     * Implementación del método que revisa si la respuesta de una solicitud
     * de interconsulta es de oftalmología o no en una BD Genérica
     *
     * @see com.princetonsa.dao.ValidacionesDao#respuestaInterconsultaEsOdontologia (Connection , int ) throws SQLException
     */
	public static boolean respuestaInterconsultaEsOftalmologica(Connection con, int numeroSolicitud)
	{
		try
		{
			PreparedStatementDecorator respuestaInterconsultaEsOdontologiaStatement= new PreparedStatementDecorator(con.prepareStatement(respuestaInterconsultaEsOftalmologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			respuestaInterconsultaEsOdontologiaStatement.setInt(1, numeroSolicitud);
	
			ResultSetDecorator rs=new ResultSetDecorator(respuestaInterconsultaEsOdontologiaStatement.executeQuery());
			if (rs.next())
			{
				if (rs.getInt("numResultados")>0)
				{
					return true;
				}
				rs.close();
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en la consulta del tipo de valoracion oftalmologica "+e);
		}
		return false;
	}

}
