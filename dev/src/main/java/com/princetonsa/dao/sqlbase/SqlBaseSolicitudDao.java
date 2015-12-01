/*
 * @(#)SqlBaseSolicitudDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesValoresPorDefecto;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.inventario.DtoArticulos;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a Solicitud
 *
 *	@version 1.0, Mar 30, 2004
 */

public class SqlBaseSolicitudDao 
{
	/** 
	 * Cadena constante con la sentencia necesaria para interpretar una solicitud 
	 */
	private static final String is_interpretar =
		"UPDATE solicitudes SET interpretacion=?, codigo_medico_interpretacion=?, fecha_interpretacion=CURRENT_DATE, hora_interpretacion="+ValoresPorDefecto.getSentenciaHoraActualBD()+", estado_historia_clinica=" +ConstantesBD.codigoEstadoHCInterpretada + " WHERE numero_solicitud=? AND estado_historia_clinica=" + ConstantesBD.codigoEstadoHCRespondida;
	
	/** 
	 * Cadena constante con la sentencia necesaria para interpretar una solicitud
	 * (Se diferencia de la anterior porque la fecha/hora interpertacion se manda como parámetro) y no se toma en cuenta el estado anterior de la solicitud
	 */
	private static final String interpretarSolicitudStr =
		"UPDATE solicitudes SET " +
			"interpretacion=?, " +
			"codigo_medico_interpretacion=?, " +
			"fecha_interpretacion=?, " +
			"hora_interpretacion=?, " +
			"estado_historia_clinica=" +ConstantesBD.codigoEstadoHCInterpretada + " " +
		"WHERE numero_solicitud=? ";

	/** 
	 * Cadena constante con la sentencia necesaria para cargar una solicitud 
	 */
	// quitar numero_autorizacion
	// "sol.numero_autorizacion as numeroAutorizacion, " +
	private static final String cargarSolicitudStr= "SELECT " + 
													"to_char(sol.fecha_grabacion,'yyyy-mm-dd') as fechaGrabacion," +
													"sol.hora_grabacion as horaGrabacion," +
													"to_char(sol.fecha_solicitud,'yyyy-mm-dd') as fechaSolicitud," +
													"sol.hora_solicitud as horaSolicitud," +
													"sol.tipo as codigoTipoSolicitud," +
													"tipsol.nombre as tipoSolicitud, " +
													"sol.especialidad_solicitante as codigoEspecialidadSolicitante, " +
													"esp1.nombre as especialidadSolicitante, " +
													"sol.ocupacion_solicitada as codigoOcupacionSolicitada, " +
													"ocmed.nombre as ocupacionSolicitada, " +
													"sol.centro_costo_solicitante as codigoCentroCostoSolicitante, " +
													"cc1.nombre as centroCostoSolicitante, " +
													"sol.centro_costo_solicitado as codigoCentroCostoSolicitado, " +
													"cc2.nombre as centroCostoSolicitado, " +
													"sol.codigo_medico as codigoMedico, " +
													"sol.consecutivo_ordenes_medicas as consecutivoOrdenesMedicas, " +
													"sol.cuenta, " +
													"sol.cobrable, " +
													"sol.interpretacion, " +
													"CASE WHEN sol.fecha_interpretacion IS NULL THEN '' ELSE to_char(sol.fecha_interpretacion,'yyyy-mm-dd') END as fechaInterpretacion, " +
													"CASE WHEN sol.hora_interpretacion  IS NULL THEN '' ELSE sol.hora_interpretacion||'' END as horaInterpretacion, " +
													"sol.codigo_medico_interpretacion as codigoMedicoInterpretacion, " +
													"sol.va_epicrisis as vaEpicrisis, " +
													"sol.urgente, " +
													"sol.estado_historia_clinica as codigoEstadoHistoriaClinica," +
													"esth.nombre as estadoHistoriaClinica, " +
													"sol.datos_medico as datosMedico, " +
													"sol.datos_medico_responde as datosMedicoResponde, " +
													"getnombrepersona(sol.codigo_medico_responde) AS nombreMedicoResponde," +
													"sol.liq_asocios AS liquidarAsocio," +
													"coalesce(sol.pyp,"+ValoresPorDefecto.getValorFalseParaConsultas()+") as pyp, " +
													"coalesce(sol.justificacion_solicitud,'') AS justificacion_solicitud, " +
													"coalesce(sol.especialidad_solicitada,-1) AS especialidad_solicitada " +
													"from solicitudes sol  " +
													"INNER JOIN tipos_solicitud tipsol ON (sol.tipo=tipsol.codigo)  " +
													"INNER JOIN especialidades esp1 ON (sol.especialidad_solicitante=esp1.codigo) " +
													"INNER JOIN ocupaciones_medicas ocmed ON (sol.ocupacion_solicitada=ocmed.codigo)  " +
													"INNER JOIN centros_costo cc1 ON (sol.centro_costo_solicitante =cc1.codigo)  " +
													"INNER JOIN centros_costo cc2 ON (sol.centro_costo_solicitado=cc2.codigo)  " +
													"INNER JOIN estados_sol_his_cli esth ON ( sol.estado_historia_clinica=esth.codigo) " +
													"where sol.numero_solicitud=?";
	/**
	 * Cadena constante para inactivar una solicitud de cambio tratante
	 */
	private static String inactivarSolicitudCambioTratanteStr = "UPDATE sol_cambio_tratante  SET activa = " + ValoresPorDefecto.getValorFalseParaConsultas() + "  WHERE solicitud = ? ";

	/**
	 * Cadena constante para revisar si hay solicitud de cambio tratante
	 * previa
	 */
	private static String haySolicitudCambioTratantePreviaStr="SELECT count(1) as numResultados from sol_cambio_tratante where cuenta=? and activa=" + ValoresPorDefecto.getValorTrueParaConsultas() + "";

	/**
	 * Cadena constante para insertar entrada en tabla anulaciones_solicitud
	 */
	private static String insercionTablaAnulacionesSolicitudStr="INSERT INTO anulaciones_solicitud (solicitud, motivo, fecha, hora, codigo_medico) VALUES (?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?)";
	
	/**
	 * Cadena constante para finalizar la atención conjunta
	 */
	private static String finalizarAtencionConjuntaStr="UPDATE adjuntos_cuenta set nota_finalizacion =?, fecha_fin=CURRENT_DATE, hora_fin="+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+" where cuenta=? and fecha_fin is null and hora_fin is null ";
	
	/**
	 * 
	 */
	private static String seleccionarSolicitudesAdjuntosStr="SELECT solicitud as sol FROM adjuntos_cuenta where cuenta=?";
	
	/**
	 * Sentencia que se encarga de cambiar el estado de facturación de la
	 * solicitud 
	 */
	private static final String cambiarEstadoFacturacionSolicitudStr  ="UPDATE det_cargos set estado=? where solicitud=?";

	/**
	 * Sentencia que se encarga de cambiar el estado de historia clínica de la
	 * solicitud 
	 */
	private static final String cambiarEstadoHistClinicaSolicitudStr =	"UPDATE solicitudes SET estado_historia_clinica = ?  WHERE numero_solicitud = ? ";

	
	/**
	 * Cadena constante con la sentencia para actualizar el estado del campo liquidar asocios en la tabla solicitudes  
	 * */
	private static final String cambiarLiquidarAsociosSolicitudStr = "UPDATE solicitudes SET liq_asocios = ? WHERE numero_solicitud = ? ";
	
	
	/**
	 *  Cadena constante con la sentencia necesaria modificar la interpretación de una solicitud 
	 */
	private static final String is_modificarInterpretacion =
		"UPDATE solicitudes SET interpretacion=interpretacion||? WHERE numero_solicitud=? AND estado_historia_clinica=3";
		
	/**
	 *  Cadena constante con la sentencia necesaria cambiar el centro de costo solicitado 
	 */
	private static final String cambiarCentroCostoSolicitadoStr  =	"UPDATE solicitudes SET centro_costo_solicitado = ?  WHERE numero_solicitud = ? ";
	
	/**
	 *  Cadena constante con la sentencia necesaria cambiar el centro de costo solicitante 
	 */
	private static final String cambiarCentroCostoSolicitanteStr  =	"UPDATE solicitudes SET centro_costo_solicitante = ?  WHERE numero_solicitud = ? ";

	/**
	 * Cadena para actualizar el número de Autorización
	 */
	//private static final String actualizarNumeroAutorizacionStr="UPDATE solicitudes SET numero_autorizacion=? WHERE numero_solicitud=?";
	
	/**
	 * Cadena para actualizar la Fecha y la Hora de la solicitud
	 */
	private static final String actualizarFechaHoraSolicitudStr="UPDATE solicitudes SET fecha_solicitud=?, hora_solicitud=? WHERE numero_solicitud=?";

	/**
	 * Cadena para actualizar la Fecha y la Hora de la solicitud
	 */
	private static final String actualizarPrioridadUrgenteSolicitudStr="UPDATE solicitudes SET urgente=? WHERE numero_solicitud=?";

	/**
	 *  Cadena para cargar el número de Autorización
	 */
	//private static final String cargarNumeroAutorizacionStr="SELECT coalesce(numero_autorizacion,'') AS numeroAutorizacion FROM solicitudes WHERE numero_solicitud=?";

	/**
	 *  Cadena para cargar el último número de solicitud
	 */
	private static final String obtenerUltimoNumeroSolicitud="SELECT MAX(numero_solicitud) FROM solicitudes";

	/**
	 * Consulta para averiguar el número de la solicitud inicial de la cuenta 
	 * de hospitalización para que los adjuntos previos (de la cuenta de 
	 * urgencias) queden con una relación a la cuenta de hospitalización 
	 * (dada por esa solicitud) 
	 */
	private static final String buscarNumeroSolicitudInicialCuentaHospitalizacionStr="SELECT numero_solicitud as numeroSolicitud from solicitudes where cuenta=? and tipo=" + ConstantesBD.codigoTipoSolicitudInicialHospitalizacion;

	/**
	 * La siguiente consulta busca y saca los datos necesarios de los 
	 * adjuntos de la cuenta de urgencias (No requiere la hora fin ni fecha 
	 * fin porque deben ser nulos para que esten todavía activos) 
	 */
	private static String buscarDatosAdjuntosStr="SELECT adj.codigo_medico, adj.fecha_inicio, adj.hora_inicio, adj.centro_costo from adjuntos_cuenta adj where adj.cuenta=? and fecha_fin IS NULL ";

	/**
	 * Con la siguiente consulta se revisa si existen solicitudes que se 
	 * deban mover en caso de asocio 
	 */
	private static String existenSolicitudesAMoverCasoAsocioStr="select count(1) as numResultados from solicitudes where cuenta=? and (estado_historia_clinica=" + ConstantesBD.codigoEstadoHCSolicitada+" OR estado_historia_clinica=" + ConstantesBD.codigoEstadoHCDespachada+")";

	/**
	 * Consulta que permite mover todas las solicitudes en estado 
	 * "solicitada" a la nueva cuenta después de un asocio   
	 */
	private static String moverSolicitudesCasoAsocioStr="UPDATE solicitudes set cuenta=? where cuenta=? and (estado_historia_clinica=" + ConstantesBD.codigoEstadoHCSolicitada+" OR estado_historia_clinica=" + ConstantesBD.codigoEstadoHCDespachada + ")";

	/**
	 * Maneja los logs del módulo de control de Solicitudes
	 */
	private static Logger logger = Logger.getLogger(SqlBaseSolicitudDao.class);

	/**
	 * Consulta del motivo de la anulación de una interconsulta
	 */
	private static final String consultaMotivoAnulacionSolicitud= "SELECT motivo AS motivo, codigo_medico AS codigo_medico, fecha as fecha, hora as hora from anulaciones_solicitud where solicitud=?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para modificar 
	 * el va a epicrisis en la base de datos Genérica.
	 */
	private static final String modificarEpicrisisStr="UPDATE solicitudes set va_epicrisis=? WHERE numero_solicitud=?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para quitar (cerrar
	 * en bd porque no se elimina) el centro de costo tratante actual en una
	 * BD Genérica.
	 */
	private static final String cerrarAntiguaEntradaTratanteStr="UPDATE tratantes_cuenta SET fecha_fin=CURRENT_DATE, hora_fin="+ValoresPorDefecto.getSentenciaHoraActualBD()+" where  cuenta=? and fecha_fin IS NULL and hora_fin IS NULL";

	
	/**
	 * Cadena constante con el <i>statement</i> necesario para saber 
	 * si hay adjunto previo en una BD Genérica.
	 */
	private static final String hayAdjuntoPrevioStr= "SELECT " +
		"count(1) as numResultados " +
		"from adjuntos_cuenta adc,   " +
		"solicitudes sol1, " +
		"solicitudes sol2 " +
		"where " +
		"adc.solicitud=sol1.numero_solicitud and " +
		"sol1.cuenta=sol2.cuenta and " +
		"sol1.centro_costo_solicitado=sol2.centro_costo_solicitado and " +
		"sol2.numero_solicitud=? and  " +
		"(adc.fecha_inicio<CURRENT_DATE or (adc.fecha_inicio=CURRENT_DATE and adc.hora_inicio<="+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+")) and " +
		"(adc.fecha_fin IS NULL or adc.fecha_fin>CURRENT_DATE or (adc.fecha_fin=CURRENT_DATE and adc.hora_fin>="+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+") )";
	
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para cancelar
	 * un tratante en una BD Genérica.
	 */
	private static final String insertarCancelacionTratanteStr="INSERT INTO cancelacion_tratantes (codigo, codigo_medico, fecha, hora) values ((SELECT max(codigo) from sol_cambio_tratante where cuenta =?), ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+")";
	
	/**
	 * Consultar las solicitudes de un paciente
	 */
	// quitar numero_autorizacion
	// "	numero_autorizacion as numeroAutorizacion, " +
	private static String consultarSolicitudesInternas=" select " +
												"	numero_solicitud as numeroSolicitud, " +
												"	to_char(sol.fecha_grabacion,'yyyy-mm-dd') as fechaGrabacion, " +
												"	hora_grabacion as horaGrabacion," +
												"	hora_solicitud as horaSolicitud, " +
												"	tipo as tipo, " +
												"	getNomTipoSolicitud(tipo) as nombretipo, " +												
												"	especialidad_solicitante as especialidadSolicitante, " +
												"	ocupacion_solicitada as ocupacionSolicitada, " +
												"	centro_costo_solicitante as centroCostoSolicitante, " +
												"	centro_costo_solicitado as centroCostoSolicitado, " +
												"	codigo_medico as codigoMedico, " +
												"	consecutivo_ordenes_medicas as consecutivoOrdenesMedicas, " +
												"	cuenta as cuenta, cobrable as cobrable, " +
												"	interpretacion as interpretacion, " +
												"	fecha_interpretacion as fechaInterpretacion, " +
												"	hora_interpretacion as horaInterpretacion, " +
												"	codigo_medico_interpretacion as codigoMedicoInterpretacion, " +
												"	va_epicrisis as vaEpicrisis, " +
												"	urgente as urgente, " +
												"	estado_historia_clinica as estadoHistoriaClinica, " +
												"	getEstadoSolhis(estado_historia_clinica) as nomEstadoHistoriaClinica"+
												" from solicitudes" +
												" where cuenta=? and centro_costo_solicitado <> "+ConstantesBD.codigoCentroCostoExternos;

	private static final String obtenerEntidadesSubcontratadasCentroCostoStr = "SELECT " +
																				"es.codigo_pk as consecutivo," +
																				"es.codigo," +
																				"es.razon_social " +
																				"from centros_costo_entidades_sub cces " +
																				"inner join entidades_subcontratadas es ON(es.codigo_pk = cces.entidad_subcontratada and es.activo = '"+ConstantesBD.acronimoSi+"') " +
																				"WHERE cces.centro_costo = ? and cces.institucion = ? " +
																				"order by cces.nro_prioridad";
    /**
	 * Cadena constante con el <i>statement</i> necesario para
     * asignar el médico de que responde (código y datos)
     */
	private static final String actualizarMedicoRespondeStr="UPDATE solicitudes set codigo_medico_responde=?, datos_medico_responde=? where numero_solicitud=?";
	
	/**
	 * Cadena que actualiza el campo especialidad solicitada, esta contiene la especialidad del profesional que responde
	 */
	private static final String actualizarEspecialidadProfesionalRespondeStr = "UPDATE solicitudes SET especialidad_solicitada = ?  WHERE numero_solicitud = ? ";
	/**
	 * Cadena constante para cambiar la opcion de manejo
	 * de la solicitud, se utiliza para el caso
	 * en el cual se hizo otra solicitud de interconsulta
	 * con opcion cambio médico tratante
	 */
	private static final String cambiarAManejoConjuntoStr="UPDATE solicitudes_inter SET codigo_manejo_interconsulta="+ConstantesBD.codigoManejoConjunto+", manejo_conjunto_finalizado=" + ValoresPorDefecto.getValorFalseParaConsultas() + " WHERE numero_solicitud=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar
	 * las solicitudes activas en una BD Genérica.
	 */
	private static final String buscarSolicitudesActivasStr="SELECT ct.solicitud as numeroSolicitud, sol.codigo_medico as codigoMedico, sol.centro_costo_solicitado as codigoCentroCosto from sol_cambio_tratante ct, solicitudes sol where ct.cuenta=? and activa=" + ValoresPorDefecto.getValorTrueParaConsultas() + " and ct.solicitud=sol.numero_solicitud";

	/**
	 * Cadena constante con el <i>statement</i> necesario para cancelar
	 * las solicitudes de cambio de manejo para una cuenta en una
	 * BD Genérica.
	 */
	private static final String cancelarSolicitudCambioManejoAnteriorDadaCuentaStr="UPDATE sol_cambio_tratante set activa=" + ValoresPorDefecto.getValorFalseParaConsultas() + " where solicitud = (SELECT (vali.numero_solicitud) from sol_cambio_tratante solct, valoraciones_consulta vali where solct.solicitud=vali.numero_solicitud and solct.activa="+ValoresPorDefecto.getValorTrueParaConsultas()+" and solct.cuenta=?)";

	/**
	 * Cadena constante con el <i>statement</i> necesario para cancelar
	 * las solicitudes de cambio de manejo para una cuenta en una
	 * BD Genérica.
	 */
	private static final String cancelarSolicitudCambioManejoAnteriorStr="UPDATE sol_cambio_tratante set activa=" + ValoresPorDefecto.getValorFalseParaConsultas() + " where cuenta = ?";

	/**
	 * Statement con el ingreso de la justificación
	 */
	private static String ingresarAtributoStr="INSERT INTO desc_atr_sol_int_proc(numero_solicitud, servicio, atributo, descripcion) VALUES(?, ?, ?, ?)";

	private static String actualizarTipoStr="UPDATE solicitudes SET tipo = ? where numero_solicitud=?";
	
	private static String strActualizarInfoSolicitud = "UPDATE solicitudes SET justificacion_solicitud = ? WHERE numero_solicitud = ? ";
	 

	// Cambios Segun Anexo 809
	/**
	 * Cadena que consulta la especilidad solicitada de una solicitud
	 */
	private static String strObtenerEspecilidadSolicitada = "SELECT coalesce(s.especialidad_solicitada,"+ConstantesBD.codigoNuncaValido+") AS espSolicitada FROM solicitudes s WHERE s.numero_solicitud = ? "; 
	// Fin Cambios Segun Anexo 809
	
	
	/**
	 *Consulta que obtiene los pools de un medico segun las validaciones en el anexo 209 
	 */
	private static String consultarPoolXMedico="select pool from participaciones_pooles pool where pool.MEDICO=?  AND POOL.FECHA_INGRESO<=CURRENT_DATE AND (POOL.FECHA_RETIRO IS NULL OR POOL.FECHA_RETIRO>=CURRENT_DATE)";
	
	
	
	
	//****************************************************************************************************************
	//***********************************METODOS**********************************************************************
	//****************************************************************************************************************
		
	
	
	
	/**
	 * Implementación del método que actualiza el
	 * médico que responde en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.SolicitudDao#actualizarMedicoResponde (con, numeroSolicitud, idMedico, detalleMedico) ;
	 */
	public static int actualizarMedicoResponde (Connection con, int numeroSolicitud, int idMedico, String detalleMedico) throws SQLException
	{
		int exito=ConstantesBD.codigoNuncaValido;
	    PreparedStatementDecorator actualizarMedicoRespondeStatement= new PreparedStatementDecorator(con.prepareStatement(actualizarMedicoRespondeStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    actualizarMedicoRespondeStatement.setInt(1, idMedico);
	    actualizarMedicoRespondeStatement.setString(2, detalleMedico);
	    actualizarMedicoRespondeStatement.setInt(3, numeroSolicitud);
	    exito = actualizarMedicoRespondeStatement.executeUpdate();
	    actualizarMedicoRespondeStatement.close();
	    return exito;
	}
	
	/**
	 * Implementacion del metodo que actualiza la especialidad del Profesional que responde
	 * @param con
	 * @param numeroSolicitud
	 * @param especialidad
	 * @return
	 * @throws SQLException
	 */
	public static int actualizarEspecialidadProfResponde (Connection con, int numeroSolicitud, int especialidad) throws SQLException
	{
	    PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(actualizarEspecialidadProfesionalRespondeStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    ps.setInt(1, especialidad);
	    ps.setInt(2, numeroSolicitud);
	    
	    return ps.executeUpdate();
	}
	
	

	/**
	 * Método para anular una solicitud en una BD Genérica
	 *
	 * @see com.princetonsa.dao.SolicitudDao#anularSolicitud (Connection , int , String , int  ) throws SQLException
	 */
	public static int anularSolicitud (Connection con, int numeroSolicitud, String motivoAnulacion, int  codigoMedicoAnulacion) throws SQLException
	{
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insercionTablaAnulacionesSolicitudStr ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, numeroSolicitud);
			ps.setString(2,motivoAnulacion);
			ps.setInt(3,codigoMedicoAnulacion);
			if (ps.executeUpdate()<1)
			{
				return 0;
			}
			ps.close();
			String cadena="UPDATE solicitudes set estado_historia_clinica=? where numero_solicitud=?";
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, ConstantesBD.codigoEstadoHCAnulada);
			ps.setInt(2, numeroSolicitud);
			if(ps.executeUpdate()>0)
			{
				cadena="UPDATE det_cargos set estado=? where solicitud=?";
				ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, ConstantesBD.codigoEstadoFAnulada);
				ps.setInt(2, numeroSolicitud);
				ps.executeUpdate();
				return 1;
			}
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Actualiza el número de autorización
	 * 
	 * @see com.princetonsa.dao.SolicitudDao#actualizarNumeroAutorizacionTransaccional(Connection , int , String , String )
	 */
	/*public static ResultadoBoolean actualizarNumeroAutorizacionTransaccional(Connection con, int numeroSolicitud, String numeroAutorizacion, String estado)
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		ResultadoBoolean resp=new ResultadoBoolean(false);
		// inicio trnasacción
		if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
		{
			resp = setCommit(con, false);
			if(!resp.isTrue())
			{
				resp.setDescripcion("Error modificarTransaccional Genérica de procedimientos en setCommit(con, false)");
				return resp;
			}	
			try
			{
				resp.setResultado(myFactory.beginTransaction(con));
			}
			catch (SQLException e)
			{
				resp.setResultado(false);
				resp.setDescripcion("Error modificarTransaccional Genérica de procedimientos empezando la transacción");
				return resp;
			}
			if(!resp.isTrue())
			{
				resp.setDescripcion("Error modificarTransaccional Genérica de procedimientos empezando la transacción");
				return resp;
			}
		}
		// verificando la conección
		try {
			if (con == null || con.isClosed()) 
			{
				logger.warn("La conexión llegó cerrada. Se lanzó la siguiente excepción:\nError SQL: Conexión cerrada");
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			PreparedStatementDecorator actualizacionSt =  new PreparedStatementDecorator(con.prepareStatement(actualizarNumeroAutorizacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			// quitar numeroAutorizacion
			actualizacionSt.setString(1,numeroAutorizacion);
			actualizacionSt.setInt(2, numeroSolicitud);
			
			int resultadoUpdate=actualizacionSt.executeUpdate();
			if(resultadoUpdate<1)
			{
				logger.error("No se actualizó el numero de autorizacion GenéricaSolicitudDao");
				return new ResultadoBoolean(false,"No se actualizó el numero de autorizacion GenéricaSolicitudDao");
			}
			else
			{
				resp.setResultado(true);
				if (estado!=null&&estado.equals(ConstantesBD.finTransaccion))
				{
					myFactory.endTransaction(con);
				}
			}
		}
		catch (SQLException e)
		{	
			try {myFactory.abortTransaction(con);} catch (SQLException e2){}
			logger.error("Error tratando de actualizar el numero de autorizacion: "+e);
			return new ResultadoBoolean(false,"Error tratando de actualizar el numero de autorizacion: "+e);
		}
		return resp;
	}*/

	/**
	 * Método para actualizar la fecha y la hora de la solicitud
	 * @param con Conección con la base de datos
	 * @param numeroSolicitud Solictud la cual se quiere modificar
	 * @param fecha Fecha actualizada
	 * @param hora Hora actualizada
	 * @param estado Estado de la transacción
	 * @return entero mayor que 1 si se realizó correctamente la modificación
	 */
	public static int actualizarFechaHoraTransaccional(Connection con, int numeroSolicitud, String fecha, String hora, String estado)
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resultadoUpdate=0;
		// inicio trnasacción
		if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
		{
			try
			{
				myFactory.beginTransaction(con);
			}
			catch (SQLException e)
			{
				logger.error("Error modificando la fecha y la hora de la solicitud al iniciar transacción: "+e);
				return 0;
			}
		}
		// verificando la conección
		try 
		{
			if (con == null || con.isClosed()) 
			{
				logger.warn("No hay conección con la base de datos");
				return 0;
			}
			PreparedStatementDecorator actualizacionSt =  new PreparedStatementDecorator(con.prepareStatement(actualizarFechaHoraSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			actualizacionSt.setString(1,fecha);
			actualizacionSt.setString(2,hora);
			actualizacionSt.setInt(3, numeroSolicitud);
			
			resultadoUpdate=actualizacionSt.executeUpdate();
			if(resultadoUpdate<1)
			{
				logger.error("No se actualizó ni la fecha ni la hora");
			}
		}
		catch (SQLException e)
		{	
			try
			{
				myFactory.abortTransaction(con);
			}
			catch (SQLException e1)
			{
				logger.error("Error abortando transacción en metodo de actualizacion de fecha y hora: "+e1);
			}
			logger.error("Error modificando la fecha y la hora de la solicitud: "+e);
			return 0;
		}
		if (estado.equals(ConstantesBD.finTransaccion))
		{
			try
			{
				myFactory.endTransaction(con);
			}
			catch (SQLException e1)
			{
				logger.error("Error modificando la fecha y la hora de la solicitud al finalizar la transacción: "+e1);
				return 0;
			}
		}
		return resultadoUpdate;
	}

	/**
	 * Método que actualiza el tipo de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoTipoSolicitudValoracionGinecoObstetrica
	 * @return Numero de registros modificados
	 */
	public static int actualizarTipoSolicitud(Connection con, int numeroSolicitud, int tipoSolicitud)
	{
		try
		{
			if(con==null || con.isClosed())
			{
			con=UtilidadBD.abrirConexion();
			}
		} catch (SQLException e1)
		{
			logger.warn("No se pudo realizar la conexión (SqlBaseSolicitudDao)"+e1.toString());
		}
		
		try
		{
			PreparedStatementDecorator actualizarTipoStm= new PreparedStatementDecorator(con.prepareStatement(actualizarTipoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			actualizarTipoStm.setInt(1, tipoSolicitud);
			actualizarTipoStm.setInt(2, numeroSolicitud);
			return actualizarTipoStm.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error modificando el tipo de solicitud : "+e);
			return 0;
		}
	}

	/**
	 * @param con
	 * @param numeroSolicitud
	 * @param pool
	 * @return
	 */
	public static int actualizarPoolSolicitud(Connection con, int numeroSolicitud, int pool) 
	{
		int exito=ConstantesBD.codigoNuncaValido;
		String consulta="UPDATE solicitudes SET pool=? where numero_solicitud= ?";
		try
		{
			logger.info("Alejo Cerrando Conexiones 2");
			logger.info("SQL: " + consulta);
			
			PreparedStatementDecorator actualizarTipoStm= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			actualizarTipoStm.setInt(1, pool);
			actualizarTipoStm.setInt(2, numeroSolicitud);
			exito = actualizarTipoStm.executeUpdate();
			actualizarTipoStm.close();
		}
		catch (SQLException e)
		{
			logger.error("Error modificando el tipo de solicitud : "+e);
		}
		return exito;
	}

	/**
	 * Método que actualiza el indicativo pyp de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param pyp
	 * @return
	 */
	public static int actualizarIndicativoPYP(Connection con,String numeroSolicitud,boolean pyp)
	{
		try
		{
			String consulta = "UPDATE solicitudes SET pyp = ? WHERE numero_solicitud = ?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setBoolean(1,pyp);
			pst.setObject(2,numeroSolicitud);
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarIndicativoPYP de SqlBaseSolicitudDao: "+e);
			return 0;
		}
	}

	/**
	 * Método para actualizar la prioridad de la solicitud
	 * @param con Conección con la base de datos
	 * @param numeroSolicitud Solictud la cual se quiere modificar
	 * @param urgente Prioridad (true --> urgente)
	 * @param estado Estado de la transacción
	 * @return entero mayor que 1 si se realizó correctamente la modificación
	 */
	public static int actualizarPrioridadUrgenteTransaccional(Connection con, int numeroSolicitud, boolean urgente, String estado)
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resultadoUpdate=0;
		// inicio trnasacción
		if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
		{
			try
			{
				myFactory.beginTransaction(con);
			}
			catch (SQLException e)
			{
				logger.error("Error modificando la prioridad de la solicitud al iniciar transacción: "+e);
				return 0;
			}
		}
		// verificando la conección
		try 
		{
			if (con == null || con.isClosed()) 
			{
				logger.warn("No hay conección con la base de datos");
				return 0;
			}
			PreparedStatementDecorator actualizacionSt =  new PreparedStatementDecorator(con.prepareStatement(actualizarPrioridadUrgenteSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			actualizacionSt.setBoolean(1,urgente);
			actualizacionSt.setInt(2, numeroSolicitud);
			
			resultadoUpdate=actualizacionSt.executeUpdate();
			if(resultadoUpdate<1)
			{
				logger.error("No se actualizó ni la fecha ni la hora");
			}
		}
		catch (SQLException e)
		{	
			try
			{
				myFactory.abortTransaction(con);
			}
			catch (SQLException e1)
			{
				logger.error("Error abortando transacción en metodo de actualizacion de la prioridad: "+e1);
			}
			logger.error("Error modificando la prioridad de la solicitud: "+e);
			return 0;
		}
		if (estado.equals(ConstantesBD.finTransaccion))
		{
			try
			{
				myFactory.endTransaction(con);
			}
			catch (SQLException e1)
			{
				logger.error("Error modificando la prioridad de la solicitud al finalizar la transacción: "+e1);
				return 0;
			}
		}
		return resultadoUpdate;
	}

	/**
	 * Método que implementa la cargada de una solicitud 
	 * en una BD Genérica
	 *
	 * @see com.princetonsa.dao.SolicitudDao#cargarSolicitud (Connection , int ) throws SQLException
	 */
	public static ResultSetDecorator cargarSolicitud (Connection con, int numeroSolicitud) throws SQLException
	{
		PreparedStatementDecorator cargarSolicitudStatement= new PreparedStatementDecorator(con.prepareStatement(cargarSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		logger.info("-->"+cargarSolicitudStr);
		logger.info("-->"+numeroSolicitud);
		cargarSolicitudStatement.setInt(1, numeroSolicitud);
		return new ResultSetDecorator(cargarSolicitudStatement.executeQuery());
	}

	
	/**
	 * Actualiza el valor de liquidar Asocios de la tabla Solicitudes
	 * @param Connection con
	 * @param int NumeroSolicitud
	 * @param String liqudarAsocios
	 * */
	public static boolean cambiarLiquidacionAsociosSolicitud(Connection con, int numeroSolicitud, String liquidarAsocios)	
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cambiarLiquidarAsociosSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setObject(1,liquidarAsocios);
			ps.setObject(2,numeroSolicitud);
			
			if(ps.executeUpdate()>0)
				return true;
			
		}	
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	

	/**
	 * Método que implementa los cambios de solicitud 
	 * una BD Genérica
	 *
	 * @see com.princetonsa.dao.SolicitudDao#cambiarEstadosSolicitud (Connection , int , int , int )
	 */
	public static ResultadoBoolean cambiarEstadosSolicitud (Connection con, int numeroSolicitud, int codigoEstadoFacturacion, int codigoEstadoHistoriaClinica)
	{
		int respuesta;		
		
		try
		{
			if( codigoEstadoHistoriaClinica == 0 )
			{		
				PreparedStatementDecorator cambiarEstadoSolicitudStatement =  new PreparedStatementDecorator(con.prepareStatement(cambiarEstadoFacturacionSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				cambiarEstadoSolicitudStatement.setInt(1, codigoEstadoFacturacion);
				cambiarEstadoSolicitudStatement.setInt(2, numeroSolicitud);		
				respuesta = cambiarEstadoSolicitudStatement.executeUpdate();
			}
			else		
			if( codigoEstadoFacturacion == 0 )
			{		
				PreparedStatementDecorator cambiarEstadoSolicitudStatement =  new PreparedStatementDecorator(con.prepareStatement(cambiarEstadoHistClinicaSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				cambiarEstadoSolicitudStatement.setInt(1, codigoEstadoHistoriaClinica);
				cambiarEstadoSolicitudStatement.setInt(2, numeroSolicitud);		
				respuesta = cambiarEstadoSolicitudStatement.executeUpdate();
			}
			else
			{
				
				PreparedStatementDecorator cambiarEstadoSolicitudStatement =  new PreparedStatementDecorator(con.prepareStatement(cambiarEstadoFacturacionSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				cambiarEstadoSolicitudStatement.setInt(1, codigoEstadoFacturacion);
				cambiarEstadoSolicitudStatement.setInt(2, numeroSolicitud);		
				respuesta = cambiarEstadoSolicitudStatement.executeUpdate();
				
				cambiarEstadoSolicitudStatement =  new PreparedStatementDecorator(con.prepareStatement(cambiarEstadoHistClinicaSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				cambiarEstadoSolicitudStatement.setInt(1, codigoEstadoHistoriaClinica);
				cambiarEstadoSolicitudStatement.setInt(2, numeroSolicitud);		
				respuesta = cambiarEstadoSolicitudStatement.executeUpdate();
			}
		}
		catch( SQLException sqe )
		{
			logger.warn("No actualizó ningun estado --> "+sqe.getMessage());
			return new ResultadoBoolean(false, "No actualizó ningun estado --> "+sqe.getMessage());
		}
		
		if( respuesta > 0 )
			return new ResultadoBoolean(true);
		else
			return new ResultadoBoolean(false, "No actualizo ningun estado");
	}

	/**
		 * Cambia dentro de una transaccion los estados de facturación o de historica clinica, o ambos,
		 * 
		 * @see com.princetonsa.dao.SolicitudDao#cambiarEstadosSolicitudTransaccional (Connection , int , int , int , String )
		 */
		public static ResultadoBoolean cambiarEstadosSolicitudTransaccional (Connection con, int numeroSolicitud, int codigoEstadoFacturacion, int codigoEstadoHistoriaClinica, String estado)
		{
			int respuesta;
	//		inicio transaccion
			DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	
			ResultadoBoolean resp=new ResultadoBoolean(false);
			if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
			{
				resp = setCommit(con, false);			
				if(!resp.isTrue())
				{
					resp.setDescripcion("Error insertarTransaccional Genérica de procedimientos en setCommit(con, false)");
					return resp;
				}	
				
				try
				{
					resp.setResultado(myFactory.beginTransaction(con));
				}
				catch (SQLException e)
				{
					resp.setResultado(false);
					resp.setDescripcion("Error insertarTransaccional Genérica de procedimientos empezando la transacción");
					return resp;
				}
				
				if(!resp.isTrue())
				{
					resp.setDescripcion("Error insertarTransaccional Genérica de procedimientos empezando la transacción");
					return resp;
				}
			}
	
			try
			{
				if( codigoEstadoHistoriaClinica == 0 )
				{		
					PreparedStatementDecorator cambiarEstadoSolicitudStatement =  new PreparedStatementDecorator(con.prepareStatement(cambiarEstadoFacturacionSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					cambiarEstadoSolicitudStatement.setInt(1, codigoEstadoFacturacion);
					cambiarEstadoSolicitudStatement.setInt(2, numeroSolicitud);		
					respuesta = cambiarEstadoSolicitudStatement.executeUpdate();
				}
				else		
				if( codigoEstadoFacturacion == 0 )
				{		
					PreparedStatementDecorator cambiarEstadoSolicitudStatement =  new PreparedStatementDecorator(con.prepareStatement(cambiarEstadoHistClinicaSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					cambiarEstadoSolicitudStatement.setInt(1, codigoEstadoHistoriaClinica);
					cambiarEstadoSolicitudStatement.setInt(2, numeroSolicitud);		
					respuesta = cambiarEstadoSolicitudStatement.executeUpdate();
				}
				else
				{
					PreparedStatementDecorator cambiarEstadoSolicitudStatement =  new PreparedStatementDecorator(con.prepareStatement(cambiarEstadoFacturacionSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					cambiarEstadoSolicitudStatement.setInt(1, codigoEstadoFacturacion);
					cambiarEstadoSolicitudStatement.setInt(2, numeroSolicitud);		
					respuesta = cambiarEstadoSolicitudStatement.executeUpdate();
					
					cambiarEstadoSolicitudStatement =  new PreparedStatementDecorator(con.prepareStatement(cambiarEstadoHistClinicaSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					cambiarEstadoSolicitudStatement.setInt(1, codigoEstadoHistoriaClinica);
					cambiarEstadoSolicitudStatement.setInt(2, numeroSolicitud);		
					respuesta = cambiarEstadoSolicitudStatement.executeUpdate();
				}
			}
			catch( SQLException sqe )
			{
				logger.warn("No actualizó ningun estado --> "+sqe.getMessage());
				return new ResultadoBoolean(false, "No actualizó ningun estado --> "+sqe.getMessage());
			}
			
			if( respuesta < 1 )			
				return new ResultadoBoolean(false, "No actualizo ningun estado");
				
	//		fin transaccion
			if( estado != null && estado.equals(ConstantesBD.finTransaccion) )
			{
				resp = setCommit(con, true);			
				if( resp.isTrue() == false )
				{
					resp.setDescripcion("Error insertarTransaccional Genérica en setCommit(con, false) _ "+resp.getDescripcion());
					return resp;
				}		
				try
				{
					myFactory.endTransaction(con);
				}
				catch (SQLException e)
				{
					resp.setResultado(false);
					resp.setDescripcion("Error insertarTransaccional bd genérica empezando la transacción _ "+resp.getDescripcion());
					return resp;
				}
			}
			return new ResultadoBoolean(true);
		}

	/**
	 * Retorna el número de autorización
	 * 
	 * @see com.princetonsa.dao.SolicitudDao#cargarNumeroAutorizacion(Connection , int )
	 */
	/*	
	public static ResultadoCollectionDB cargarNumeroAutorizacion(Connection con, int numeroSolicitud)
	{
		try{
			PreparedStatementDecorator cargarSt=  new PreparedStatementDecorator(con.prepareStatement(cargarNumeroAutorizacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarSt.setInt(1,numeroSolicitud);
			ResultSetDecorator resultado = cargarSt.executeQuery());
			Collection colResultado =	UtilidadBD.resultSet2Collection(resultado);
			if(!colResultado.isEmpty())
				return new ResultadoCollectionDB(true, "", colResultado);
			else
				return new ResultadoCollectionDB(false, "La consulta no arrojó ningún resultado en GenéricaSolicitud");
		}
		catch(SQLException e)
		{
			logger.error("Error cargando el número de autorización: "+e.getMessage());
			return new ResultadoCollectionDB(false, "Error cargando el número de autorización: "+e.getMessage());
		}
		catch(Exception e)
		{
			logger.error("Error inesperado cargando el número de autorización en GenéricaSolicitud: "+e.getMessage());
			return new ResultadoCollectionDB(false, "Error inesperado cargando el número de autorización en GenéricaSolicitud: "+e.getMessage());
		}
	}
	*/
	

	/**
	* Método que carga el motivo de anulación de una solicitud
	* de Interconsulta en una BD Genérica 
	 * 
	 * @see com.princetonsa.dao.SolicitudDao#cargarMotivoAnulacionSolicitudInterconsulta (Connection , int ) throws SQLException
	*/
	public static ResultSetDecorator cargarMotivoAnulacionSolicitudInterconsulta (Connection con, int numeroSolicitud) throws SQLException
	{
		PreparedStatementDecorator cargarMotivoAnulacionSolicitudStatement= new PreparedStatementDecorator(con.prepareStatement(consultaMotivoAnulacionSolicitud,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargarMotivoAnulacionSolicitudStatement.setInt(1, numeroSolicitud);
		return new ResultSetDecorator(cargarMotivoAnulacionSolicitudStatement.executeQuery());
	}

	/**
	* Método que implementa el cambio de centro de costo solicitado
	* en una BD Genérica 
	 * 
	 * @see com.princetonsa.dao.SolicitudDao#cambiarCentroCostoSolicitado(Connection , int , int )
	*/
	public static ResultadoBoolean cambiarCentroCostoSolicitado(Connection con, int numeroSolicitud, int centroCostoSolicitado)
	{
		try {
			PreparedStatementDecorator cambiarCentroCostoSolicitado=  new PreparedStatementDecorator(con.prepareStatement(cambiarCentroCostoSolicitadoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cambiarCentroCostoSolicitado.setInt(1,centroCostoSolicitado);
			cambiarCentroCostoSolicitado.setInt(2,numeroSolicitud);
			if(cambiarCentroCostoSolicitado.executeUpdate()==0)
				return new ResultadoBoolean(false,"Error cambiando el centro de costo solicitado");
			else
				return new ResultadoBoolean(true);
		}
		catch (SQLException e) {
			logger.error("Error cambiando el centro de costo solicitado: "+e.getMessage());
			return new ResultadoBoolean(false,"Error cambiando el centro de costo solicitado: "+e.getMessage());
		}
	}

	/**
	* Método que implementa el cambio de centro de costo solicitante
	* en una BD Genérica 
	 * 
	 * @see com.princetonsa.dao.SolicitudDao#cambiarCentroCostoSolicitante(Connection , int , int )
	*/
	public static ResultadoBoolean cambiarCentroCostoSolicitante(Connection con, int numeroSolicitud, int centroCostoSolicitante)
	{
		try {
			PreparedStatementDecorator cambiarCentroCostoSolicitante=  new PreparedStatementDecorator(con.prepareStatement(cambiarCentroCostoSolicitanteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cambiarCentroCostoSolicitante.setInt(1,centroCostoSolicitante);
			cambiarCentroCostoSolicitante.setInt(2,numeroSolicitud);
			if(cambiarCentroCostoSolicitante.executeUpdate()==0)
				return new ResultadoBoolean(false,"Error cambiando el centro de costo solicitante");
			else
				return new ResultadoBoolean(true);
		}
		catch (SQLException e) {
			logger.error("Error cambiando el centro de costo solicitante: "+e.getMessage());
			return new ResultadoBoolean(false,"Error cambiando el centro de costo solicitante: "+e.getMessage());
		}
	}

	/**
	 * Método que implementa la cerrada /eliminación de un tratante para
	 * urgencias en una BD Genérica (Queda en BD registro con fecha/
	 * hora inicio y fecha/hora fin)
	 *
	 * @see com.princetonsa.dao.SolicitudDao#cerrarAntiguaEntradaTratante (Connection, int ) throws SQLException
	 */
	public static int cerrarAntiguaEntradaTratante (Connection con, int idCuenta ) throws SQLException
	{
		PreparedStatementDecorator cerrarAntiguaEntradaTratanteStatement= new PreparedStatementDecorator(con.prepareStatement(cerrarAntiguaEntradaTratanteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cerrarAntiguaEntradaTratanteStatement.setInt(1, idCuenta);
		return cerrarAntiguaEntradaTratanteStatement.executeUpdate();
	}

	/**
	 * Método que implementa la cerrada /eliminación de un tratante para
	 * urgencias en una BD Genérica (Queda en BD registro con fecha/
	 * hora inicio y fecha/hora fin)
	 *
	 * @see com.princetonsa.dao.SolicitudDao#cerrarAntiguaEntradaTratanteDadaSolicitud(Connection, int ) throws SQLException
	 */
	public static int cerrarAntiguaEntradaTratanteDadaSolicitud (Connection con, int numeroSolicitud) throws SQLException
	{
		String buscarCodigoCuentaDadaSolicitudStr="SELECT cuenta from solicitudes where numero_solicitud=?";
		PreparedStatementDecorator buscarCodigoCuentaDadaSolicitudStatement= new PreparedStatementDecorator(con.prepareStatement(buscarCodigoCuentaDadaSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		buscarCodigoCuentaDadaSolicitudStatement.setInt(1, numeroSolicitud);
		
		ResultSetDecorator rs=new ResultSetDecorator(buscarCodigoCuentaDadaSolicitudStatement.executeQuery());
		if (rs.next())
		{
			int idCuenta=rs.getInt("cuenta");
			return cerrarAntiguaEntradaTratante(con, idCuenta);
		}
		else
		{
			//No había ningún tratante para la cuenta de la solicitud
			//dada
			return 0;
		}
	}

	/**
	 * Metodo que carga todas las solicitudes de una cuenta, y retorna una colleccion de solicitudes
	 * @armando
	 * @param con, conexion
	 * @param cuenta, cuenta que se desea consultar
	 * @return  collection, datos de la consulta.
	 */
	
	public static Collection cargarSolicitudesInternas(Connection con, int cuenta) 
	{
		try
		{
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarSolicitudesInternas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1, cuenta);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consulta.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando las solicitudes"+e);
			return null;
		}
	}

	/**
	 * Método para la creación (sin validaciones) de un adjunto para
	 * cuenta en una BD Genérica
	 *
	 * @see com.princetonsa.dao.SolicitudDao#crearAdjuntoCuenta (Connection , int , int ) throws SQLException
	 */
	public static int crearAdjuntoCuenta (Connection con, int numeroSolicitud, int codigoMedicoSolicitante, int codigoCentroCostoSolicitante, String insertarAdjuntoCuentaStr) throws SQLException
	{
	
		if (!hayAdjuntoPrevio(con, numeroSolicitud) )
		{
			PreparedStatementDecorator insertarAdjuntoCuentaStatement= new PreparedStatementDecorator(con.prepareStatement(insertarAdjuntoCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insertarAdjuntoCuentaStatement.setInt(1, numeroSolicitud);
			insertarAdjuntoCuentaStatement.setInt(2, codigoMedicoSolicitante);
			insertarAdjuntoCuentaStatement.setInt(3, codigoCentroCostoSolicitante);
			insertarAdjuntoCuentaStatement.setInt(4, numeroSolicitud);
			return insertarAdjuntoCuentaStatement.executeUpdate();
		}
		else
		{
			return 1;
		}
	}

	/**
	 * Implementación del método que realiza los cambios necesarios
	 * para aceptar una solicitud de cambio de médico tratante en una 
	 * BD Genérica
	 *
	 * @see com.princetonsa.dao.SolicitudDao#(Connection , int , int ) throws SQLException
	 */
	public static boolean cambiosRequeridosAceptacionTratanteCasoAsocio (Connection con, int numeroSolicitud, int idCuentaDestino) throws SQLException
	{
		//Si la solicitud pertenece a la cuenta destino quiere decir que no hay 
		//necesidad de mover la solicitud a la cuenta destino
		String solicitudPerteneceACuentaDestinoStr="SELECT count(1) as numResultados from solicitudes where numero_solicitud=? and cuenta=?";
		PreparedStatementDecorator solicitudPerteneceACuentaDestinoStatement= new PreparedStatementDecorator(con.prepareStatement(solicitudPerteneceACuentaDestinoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		solicitudPerteneceACuentaDestinoStatement.setInt(1, numeroSolicitud );
		solicitudPerteneceACuentaDestinoStatement.setInt(2, idCuentaDestino);
		ResultSetDecorator rs=new ResultSetDecorator(solicitudPerteneceACuentaDestinoStatement.executeQuery());
		if (rs.next())
		{
			if (rs.getInt("numResultados")>0)
			{
				//Quiere decir que la cuenta si pertene a la cuenta destino (donde
				//debe quedar el cambio de tratante), luego NO hay que hacer nada
				return true;
			}
			else
			{
				//Tenemos que mover la solicitud a la cuenta de destino
				String moverSolicitudACuentaDestinoStr="UPDATE solicitudes set cuenta=? where numero_solicitud=?";
				PreparedStatementDecorator moverSolicitudACuentaDestinoStatement= new PreparedStatementDecorator(con.prepareStatement(moverSolicitudACuentaDestinoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				moverSolicitudACuentaDestinoStatement.setInt(1, idCuentaDestino);
				moverSolicitudACuentaDestinoStatement.setInt(2, numeroSolicitud);
				if (moverSolicitudACuentaDestinoStatement.executeUpdate()>0)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		else
		{
			throw new SQLException("Error en un count en cambiosRequeridosAceptacionTratanteCasoAsocio (SqlBase)");
		}
		
	}

	/**
	 * Implementación del método que cambia la solicitud de médico
	 * tratante en una BD Genérica
	 *
	 * @see com.princetonsa.dao.SolicitudDao#cambiarSolicitudMedicoTratante (Connection , int , int , int , String ) throws SQLException
	 */
	public static int cambiarSolicitudMedicoTratante (Connection con, int numeroSolicitud, int idCuenta, int codigoMedico, String estado, String insertarEntradaSolicitudesCambioTratanteStr) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int respDecision=0, resp1=0, resp2=0;
		boolean inicTrans=false;
	
		try
		{
	
			if (con == null || con.isClosed())
			{
				//Como es transaccional NO voy a tratar de abrir una nueva conexión, sino que mandaré una excepction
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
	
			if (estado==null)
			{
				throw new SQLException ("Error SQL: No se seleccionó ningun estado para la transacción");
			}
	
			if (estado.equals("empezar"))
			{
				inicTrans= myFactory.beginTransaction(con);
			}
			else
			{
				//De todas maneras así no sea transacción debo dejar en claro que todo salio bien
				inicTrans=true;
			}
	
			//INICIO DE FUNC. COMO TAL
	
			//Revisamos si hay o no alguna solicitud previa
			//Para esta cuenta
			if (!haySolicitudCambioTratantePrevia(con, idCuenta))
			{
				resp1=insertarEntradaSolicitudesCambioTratante(con, idCuenta, numeroSolicitud, insertarEntradaSolicitudesCambioTratanteStr);
				resp2=1;
			}
			else
			{
				logger.info("SQL / cancelarSolicitudCambioManejoAnteriorDadaCuentaStr / "+cancelarSolicitudCambioManejoAnteriorDadaCuentaStr);
				logger.info("Cuenta: "+idCuenta);
				
				PreparedStatementDecorator cancelarSolicitudCambioManejoAnteriorStatement= new PreparedStatementDecorator(con.prepareStatement(cancelarSolicitudCambioManejoAnteriorDadaCuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				cancelarSolicitudCambioManejoAnteriorStatement.setInt(1, idCuenta);
				try
				{
					respDecision=cancelarSolicitudCambioManejoAnteriorStatement.executeUpdate();
				}
				catch (SQLException e)
				{
					if( logger.isDebugEnabled() )
					{
						logger.debug("Problemas de Integridad Referencial! Mas de una solicitud de cambio de manejo tratante. Error " + numeroSolicitud + "-" + idCuenta + "-" + codigoMedico);
					}
					throw new SQLException("Problemas con la BD. Comuniquese con el administrador e informe del error " + numeroSolicitud + "-" + idCuenta + "-" + codigoMedico);
				}
	
				if (respDecision==1)
				{
					//Si hizo el update quiere decir que existía una
					//valoración de interconsulta, luego debemos
					//insertar una entrada en cancelación tratantes
	
					PreparedStatementDecorator insertarCancelacionTratanteStatement= new PreparedStatementDecorator(con.prepareStatement(insertarCancelacionTratanteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					insertarCancelacionTratanteStatement.setInt(1, idCuenta);
					insertarCancelacionTratanteStatement.setInt(2, codigoMedico);
	
					try
					{
						resp1=insertarCancelacionTratanteStatement.executeUpdate();
						resp2=insertarEntradaSolicitudesCambioTratante(con, idCuenta, numeroSolicitud, insertarEntradaSolicitudesCambioTratanteStr);
					}
					catch (SQLException e)
					{
						if( logger.isDebugEnabled() )
						{
							logger.debug("Problemas de Integridad Referencial! Mas de una solicitud de cambio de manejo tratante(2). Error " + numeroSolicitud + "-" + idCuenta + "-" + codigoMedico);
						}
						throw new SQLException("Problemas con la BD. Comuniquese con el administrador e informe del error " + numeroSolicitud + "-" + idCuenta + "-" + codigoMedico);
					}
				}
				else
				{
					//Si no había respondido la valoración de interconsulta, hacemos
					//la actualización
					resp1=funcionalidadTratanteCasoNoInterconsulta(con, idCuenta, numeroSolicitud, insertarEntradaSolicitudesCambioTratanteStr);
					resp2=1;
				}
	
					//FIN DE FUNC. COMO TAL
				}
	
			if (!inicTrans||resp1<1||resp2<1)
			{
				// Terminamos la transaccion, sea con un rollback o un commit.
				myFactory.abortTransaction(con);
				return 0;
			}
			else
			{
				if (estado.equals("finalizar"))
				{
					myFactory.endTransaction(con);
				}
				return 1;
			}
		}
		catch (SQLException e)
		{
			//Por alguna razón no salió bien
			logger.error("ERROR:",e);
			throw e;
		}
	}

	/**
	 * 
	 * @param con
	 * @param dtoSolicitudesSubCuenta
	 * @return
	 */
	public static boolean eliminarSolicitudSubCuenta(Connection con, DtoSolicitudesSubCuenta dtoSolicitudesSubCuenta) 
	{
		String eliminarStr="DELETE FROM solicitudes_subcuenta WHERE 1=1 ";
		if(!UtilidadTexto.isEmpty(dtoSolicitudesSubCuenta.getNumeroSolicitud()))
			eliminarStr+=" AND solicitud = "+dtoSolicitudesSubCuenta.getNumeroSolicitud();
		if(!UtilidadTexto.isEmpty(dtoSolicitudesSubCuenta.getArticulo().getCodigo()))
			eliminarStr+=" AND articulo= "+dtoSolicitudesSubCuenta.getArticulo().getCodigo();
		if(!UtilidadTexto.isEmpty(dtoSolicitudesSubCuenta.getServicio().getCodigo()))
			eliminarStr+=" AND servicio= "+dtoSolicitudesSubCuenta.getServicio().getCodigo();
		if(!UtilidadTexto.isEmpty(dtoSolicitudesSubCuenta.getSubCuenta()))
			eliminarStr+=" AND sub_cuenta= "+Utilidades.convertirALong(dtoSolicitudesSubCuenta.getSubCuenta());
		if(!UtilidadTexto.isEmpty(dtoSolicitudesSubCuenta.getCodigo()))
			eliminarStr+=" AND codigo= "+dtoSolicitudesSubCuenta.getCodigo();
		if(!UtilidadTexto.isEmpty(dtoSolicitudesSubCuenta.getCuenta()))
			eliminarStr+=" AND cuenta= "+dtoSolicitudesSubCuenta.getCuenta();
		
		//@todo agregar los que se necesiten
		
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if(pst.executeUpdate()>0)
				return true;
		}
		catch(SQLException e)
		{
			logger.error("Error en eliminarSolicitudSubCuenta de SqlBaseSolicitudDao: "+e);
		}
		return false;
	}

	/**
	 * Actualizar la cobertura de la solicitud sub cuenta
	 * 
	 * @param con
	 * @param dtoSolicitudesSubCuenta
	 * @return
	 * @author jeilones
	 * @created 5/02/2013
	 */
	public static boolean actualizarSolicitudSubCuenta(Connection con, DtoSolicitudesSubCuenta dtoSolicitudesSubCuenta) 
	{
		String actualizarStr="UPDATE solicitudes_subcuenta SET cobertura = ? WHERE ";
		if(!UtilidadTexto.isEmpty(dtoSolicitudesSubCuenta.getNumeroSolicitud()))
			actualizarStr+=" solicitud = "+dtoSolicitudesSubCuenta.getNumeroSolicitud()+" AND ";
		if(!UtilidadTexto.isEmpty(dtoSolicitudesSubCuenta.getArticulo().getCodigo()))
			actualizarStr+=" articulo= "+dtoSolicitudesSubCuenta.getArticulo().getCodigo()+" AND ";
		if(!UtilidadTexto.isEmpty(dtoSolicitudesSubCuenta.getServicio().getCodigo()))
			actualizarStr+=" servicio= "+dtoSolicitudesSubCuenta.getServicio().getCodigo()+" AND ";
		if(!UtilidadTexto.isEmpty(dtoSolicitudesSubCuenta.getSubCuenta()))
			actualizarStr+=" sub_cuenta= "+Utilidades.convertirALong(dtoSolicitudesSubCuenta.getSubCuenta())+" AND ";
		if(!UtilidadTexto.isEmpty(dtoSolicitudesSubCuenta.getCodigo()))
			actualizarStr+=" codigo= "+dtoSolicitudesSubCuenta.getCodigo()+" AND ";
		if(!UtilidadTexto.isEmpty(dtoSolicitudesSubCuenta.getCuenta()))
			actualizarStr+=" cuenta= "+dtoSolicitudesSubCuenta.getCuenta();
		
		//@todo agregar los que se necesiten
		if(actualizarStr.trim().endsWith(" AND ")){
			actualizarStr=actualizarStr.substring(0,actualizarStr.length()-5);
		}
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(actualizarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, dtoSolicitudesSubCuenta.getCubierto());
			if(pst.executeUpdate()>0)
				return true;
		}
		catch(SQLException e)
		{
			logger.error("Error en eliminarSolicitudSubCuenta de SqlBaseSolicitudDao: "+e);
		}
		return false;
	}
	
	/**
	 * Método que implementa la finalización de atención conjunta 
	 * una BD Genérica
	 *
	 * @see com.princetonsa.dao.SolicitudDao#finalizarAtencionConjunta (Connection , int , int , String ) throws SQLException
	 */
	public static int finalizarAtencionConjunta (Connection con, int idCuenta, int codigoCentroCostoAdjunto, String notaFinalizacion) throws SQLException
	{
		int numSol= seleccionarSolicitudesInterconsultaAdjunto(con, idCuenta);
		if(numSol>0)
		{	
			String consulta = finalizarAtencionConjuntaStr;
			/*if(codigoCentroCostoAdjunto>0)
				consulta += " AND centro_costo = "+codigoCentroCostoAdjunto;*/
			PreparedStatementDecorator finalizarAtencionConjuntaStatement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			finalizarAtencionConjuntaStatement.setString(1, notaFinalizacion);
			finalizarAtencionConjuntaStatement.setInt(2, idCuenta);
			finalizarAtencionConjuntaStatement.executeUpdate();
		}	
		return numSol;
	}

	/**
	 * Este método se encarga de toda la funcionalidad correspondiente
	 * a solicitar cambio de tratante en caso de no existir interconsulta
	 * anterior pero SI Solicitud cambio tratante. Es un método privado
	 * porque consiste simplemente en el conj. de acciones y la
	 * transaccionalidad se maneja en los métodos que la usan
	 *
	 * @param con Conexión con la BD Genérica
	 * @param idCuenta Código de la cuenta
	 * @return
	 * @throws SQLException
	 */
	private static int funcionalidadTratanteCasoNoInterconsulta(Connection con, int idCuenta, int numeroSolicitud, String insertarEntradaSolicitudesCambioTratanteStr) throws SQLException
	{
		boolean encontreResultado=false;
		int respuestaTemporal=0;
		PreparedStatementDecorator generalStatement;
	
		//Buscamos el código de la(s) solicitudes tratantes existentes
		PreparedStatementDecorator buscarSolicitudesActivasStatement= new PreparedStatementDecorator(con.prepareStatement(buscarSolicitudesActivasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		buscarSolicitudesActivasStatement.setInt(1, idCuenta);
		ResultSetDecorator rs=new ResultSetDecorator(buscarSolicitudesActivasStatement.executeQuery());
		while  (rs.next())
		{
			PreparedStatementDecorator cambiarAManejoConjunto= new PreparedStatementDecorator(con.prepareStatement(cambiarAManejoConjuntoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			cambiarAManejoConjunto.setInt(1, rs.getInt("numeroSolicitud"));
			respuestaTemporal=cambiarAManejoConjunto.executeUpdate();
			if (respuestaTemporal==0)
			{
				return 0;
			}
			encontreResultado=true;
		}
		if (encontreResultado)
		{
			generalStatement= new PreparedStatementDecorator(con.prepareStatement(cancelarSolicitudCambioManejoAnteriorStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			generalStatement.setInt(1, idCuenta);
			respuestaTemporal=generalStatement.executeUpdate();
	
			generalStatement.close();
	
			if (respuestaTemporal==0)
			{
				return 0;
			}
			return insertarEntradaSolicitudesCambioTratante(con, idCuenta, numeroSolicitud, insertarEntradaSolicitudesCambioTratanteStr);
		}
		else
		{
			return 0;
		}
	}

	/**
	 * Obtiene el numero de solicitud a inasertar para evitar problemas
	 * de concurrencia
	 * @param con
	 * @param consulta
	 * @return
	 */
	public static int getNumeroSolicitudAinsertar(Connection con, String consulta)
	{
		try
		{
			logger.info("Alejo Cerrando Conexiones 1");
			logger.info("SQL: " + consulta);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt("codigosolic");
			
			rs.close();// alejo
			ps.close();//alejo
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el numero de solicitud : "+e);
		}
		return -1;
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static int getCodigoTipoSolicitud(Connection con, String numeroSolicitud) throws BDException
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		int resultado=0;
		
		try	{
			Log4JManager.info("############## Inicio getCodigoTipoSolicitud");
			String consulta = "SELECT tipo as tipo FROM  solicitudes WHERE numero_solicitud = ?";
			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setString(1,numeroSolicitud);
			rs= pst.executeQuery();
			if(rs.next()){
				resultado=rs.getInt("tipo");
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
		Log4JManager.info("############## Fin getCodigoTipoSolicitud");
		return resultado;
	}

	/**
	 * Método que revisa si existe una entrada previa para el centro
	 * de costo y cuenta definidos en una solicitud en adjuntos_cuenta
	 * 
	 * @param con Conexión con la BD Genérica
	 * @param numeroSolicitud Número de la solicitud que se quiere
	 * postular como futuro adjunto
	 * @return
	 * @throws SQLException
	 */
	public static boolean hayAdjuntoPrevio (Connection con, int numeroSolicitud) throws SQLException
	{
		PreparedStatementDecorator hayAdjuntoPrevioStatement= new PreparedStatementDecorator(con.prepareStatement(hayAdjuntoPrevioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		hayAdjuntoPrevioStatement.setInt(1, numeroSolicitud);
		ResultSetDecorator rs=new ResultSetDecorator(hayAdjuntoPrevioStatement.executeQuery());
		if (rs.next())
		{
			if (rs.getInt("numResultados")>0)
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
			throw new SQLException ("Error en un count en hayAdjuntoPrevio");
		}
	}

	/**
	 * Método que implementa la revisión de existencia de solicitud 
	 * de cambio tratante en una BD Genérica
	 *
	 * @see com.princetonsa.dao.SolicitudDao#haySolicitudCambioTratantePrevia (Connection , int ) throws SQLException
	 */
	public static boolean haySolicitudCambioTratantePrevia (Connection con, int idCuenta) throws SQLException
	{	
		PreparedStatementDecorator haySolicitudCambioTratantePreviaStatement= new PreparedStatementDecorator(con.prepareStatement(haySolicitudCambioTratantePreviaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		haySolicitudCambioTratantePreviaStatement.setInt(1, idCuenta);
		ResultSetDecorator rs=new ResultSetDecorator(haySolicitudCambioTratantePreviaStatement.executeQuery());
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
			throw new SQLException("Error en un count en haySolicitudCambioTratantePrevia ");
		}
	}

	/**
	* Implementación para Genérica del método que interpreta una solicitud
	* 
	* @param ac_con				Conexión abierta a una fuente de datos
	* @param ai_numeroSolicitud	Número de la solicitud a ser interpretada
	* @param ai_codigoMedico	Código del médico que realiza la interpretación de la consulta
	* @param as_interpretacion	Texto de la interpretación de la solicitud
	* @param ab_vaEpicrisis		Indicador de si la solicitud va o no a la epicrisis
	* @return Número de solicitudes interpretadas.
	* @throws SQLException si se presenta un error en la base de datos Genérica
	*/
	public static int interpretarSolicitud(
		Connection	ac_con,
		int			ai_numeroSolicitud,
		int			ai_codigoMedico,
		String		as_interpretacion
		
	)
	{
		try{
			PreparedStatementDecorator lps_ps=null;
			DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	
			
			/* Abrir conexión a la base de datos si es necesario */
			if(ac_con == null || ac_con.isClosed() )
				ac_con = myFactory.getConnection();
	
			/* Preparar la consulta para interpretación de la solicitud */
			lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(is_interpretar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	
			/* Ajustar los parámetros de la interpretación */
			lps_ps.setString(1, as_interpretacion);
			lps_ps.setInt(2, ai_codigoMedico);
			lps_ps.setInt(3, ai_numeroSolicitud);
			
			/* Ejecutar la iterpretación */
			if (lps_ps.executeUpdate() == 0)  //-Esto es para saber si la solicitud ya fue interpretada. 
				return -1;
			else
				return 1;
		}
		catch(SQLException e)
		{
			logger.error("Error insertando la interpretación "+e);
			return 0;
		}
	}
	
	/**
	 * Método que inserta la interpretacion de una solicitud de procedimientos,
	 * se diferencia del otro método de que la fecha/hora interpretacion se mandan como parámetros
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int interpretarSolicitud(Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(interpretarSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1,campos.get("interpretacion"));
			if(Utilidades.convertirAEntero(campos.get("codigoMedico").toString())>0)
				pst.setObject(2,campos.get("codigoMedico"));
			else
				pst.setNull(2,Types.INTEGER);
			pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInterpretacion").toString()));
			pst.setObject(4,campos.get("horaInterpretacion"));
			pst.setObject(5,campos.get("numeroSolicitud"));
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en interpretarSolicitud de SqlBaseSolicitudDao: "+e);
			return 0;
		}
	}

	/**
	 * Método que implementa la inserción de la solicitud inicial de
	 * valoración en una BD Genérica
	 *
	 * @see com.princetonsa.dao.SolicitudDao#insertarSolicitudValoracionInicial (Connection , String , int , int , int , boolean , int ) throws SQLException
	 */
	// String numeroAutorizacion, 
	public static int insertarSolicitudValoracionInicial (Connection con, int codigoCentroCostoSolicitante, int codigoCentroCostoSolicitado, int codigoCuenta, boolean cobrable, int codigoTipoSolicitud, String insertarSolicitudValoracionInicialStr, int numeroSolicitudAInsertar) throws SQLException
	{
		PreparedStatementDecorator	insertarSolicitudValoracionInicialStatement;
		insertarSolicitudValoracionInicialStatement =  new PreparedStatementDecorator(con.prepareStatement(insertarSolicitudValoracionInicialStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		insertarSolicitudValoracionInicialStatement.setString(1,numeroSolicitudAInsertar+"");
		// quitar numeroAutorizacion
		//insertarSolicitudValoracionInicialStatement.setString(2, numeroAutorizacion);
		insertarSolicitudValoracionInicialStatement.setInt(2, codigoCentroCostoSolicitante);
		insertarSolicitudValoracionInicialStatement.setInt(3, codigoCentroCostoSolicitado);
		insertarSolicitudValoracionInicialStatement.setInt(4, codigoCuenta);
		insertarSolicitudValoracionInicialStatement.setBoolean(5, cobrable);
		insertarSolicitudValoracionInicialStatement.setInt(6, codigoTipoSolicitud);
	
		/* Verificar si la solicitud pudo ser insertada correctamente */
		if(insertarSolicitudValoracionInicialStatement.executeUpdate() == 1)
		{
			return numeroSolicitudAInsertar;
		}
	
		/* Liberar recursos de base de datos */
		insertarSolicitudValoracionInicialStatement.close();
	
		return -1;
	}

	/**
	 * Método que implementa la inserción de una entrada
	 * de tratante en una BD Genérica
	 *
	 * @see com.princetonsa.dao.SolicitudDao#insertarTratante (Connection , int , int , String , String ) throws SQLException
	 */
	public static int insertarTratante (Connection con, int numeroSolicitud, int codigoMedico, int codigoCentroCostoTratante, String insertarTratanteStr) throws SQLException
	{
		PreparedStatementDecorator insertarTratanteStatement= new PreparedStatementDecorator(con.prepareStatement(insertarTratanteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		insertarTratanteStatement.setInt(1, numeroSolicitud);
		if (codigoMedico>0)
		{
			insertarTratanteStatement.setInt(2, codigoMedico);
		}
		else
		{
			insertarTratanteStatement.setString(2, null);
		}
		insertarTratanteStatement.setInt(3, codigoCentroCostoTratante);
		insertarTratanteStatement.setInt(4, numeroSolicitud);
		return insertarTratanteStatement.executeUpdate();
	}

	/**
	 * Método la inserción de una solicitud general
	 * en una BD Genérica
	 * @param solPYP 
	 * @param especialidadSolicitadaOrdAmbulatorias 
	 *
	 * @see com.princetonsa.dao.SolicitudDao#insertarSolicitudGeneral (Connection , String , String , int , boolean , String , int , int , int , int  , int , int , boolean , boolean , String, int , int ) throws SQLException
	 */
	// modificar
	// String numeroAutorizacion, 
	public static int insertarSolicitudGeneral (
			Connection con,
			String fechaSolicitud,
			String horaSolicitud,
			int codigoTipoSolicitud,
			boolean cobrable,			
			int codigoEspecialidadSolicitante,
			int codigoOcupacionSolicitada,
			int codigoCentroCostoSolicitante,
			int  codigoCentroCostoSolicitado,
			int codigoMedicoCreador,
			int codigoCuenta,
			boolean vaEpicrisis,
			boolean urgente,
			int estadoClinico,
			String datosMedico, 
			boolean solPYP,
			boolean tieneCita, 
			String liquidarAsocio, 
			String insertarSolicitudGeneralExternaStr,
			String insertarSolicitudGeneralInternaStr, 
			int numeroSolicitudAInsertar,
			String justificacionSolicitud,
			int especialidadSolicitadaOrdAmbulatorias,
			int codigoMedicoResponde,
			DtoDiagnostico dtoDiagnostico)
	{
		logger.info("OJOJOJOJOJOJOJ num sol a insertar primer parametro::::"+numeroSolicitudAInsertar+", ultimo parametro;::"+especialidadSolicitadaOrdAmbulatorias);
		String insertarSolicitudGeneralStr;
		ArrayList<Integer> pool = new ArrayList<Integer>();
		Integer poolDefinitivo= new Integer(0);
		/*if (ConstantesBD.codigoCentroCostoExternos==codigoCentroCostoSolicitado)
		{
			insertarSolicitudGeneralStr=insertarSolicitudGeneralExternaStr;
		}
		else
		{
		
		}*/
		insertarSolicitudGeneralStr=insertarSolicitudGeneralInternaStr;
		
		logger.info("LA CONSULTA PARA INSERTAR LA SOLICITUD-------->"+insertarSolicitudGeneralInternaStr);
		
		
		try
		{
			//MT 6503
			if(System.getProperty("TIPOBD").equals("POSTGRESQL"))
			{
	        	Statement stmt = con.createStatement();
	            stmt.execute("SET standard_conforming_strings TO off");
	            stmt.close();
			}
			//FIN MT 6503
			
			//se consultan los pools si trae mas de uno se coloca null ya que solo debe tener un solo pool asociado
			pool = consultarValorPoolMedico(con, codigoMedicoResponde);
			if (pool.size()>1) {
				poolDefinitivo=null;
			}else if(pool.size()>0){
				poolDefinitivo=pool.get(ConstantesBD.valorInicial);
			}
			
			
			PreparedStatementDecorator insertarSolicitudStatement= new PreparedStatementDecorator(con, insertarSolicitudGeneralStr);
			
			insertarSolicitudStatement.setString(1, numeroSolicitudAInsertar+"");
	
			/*if (UtilidadCadena.noEsVacio(fechaSolicitud))
				insertarSolicitudStatement.setString(2, UtilidadFecha.conversionFormatoFechaABD(fechaSolicitud));
			else
				insertarSolicitudStatement.setNull(2, Types.DATE);
			*/
			insertarSolicitudStatement.setDate(2, new java.sql.Date(UtilidadFecha.conversionFormatoFechaStringDate(fechaSolicitud).getTime()));			
			
			insertarSolicitudStatement.setString(3, horaSolicitud.length()>5?horaSolicitud.substring(0, 5):horaSolicitud);
			insertarSolicitudStatement.setInt(4, codigoTipoSolicitud);
			insertarSolicitudStatement.setBoolean(5, cobrable);
			// quitar nmeroAutorizacion
			//insertarSolicitudStatement.setString(6, numeroAutorizacion);
				
			insertarSolicitudStatement.setInt(6, codigoEspecialidadSolicitante);
			logger.info("ocupacionSolicitada-->"+codigoOcupacionSolicitada);
			insertarSolicitudStatement.setInt(7, codigoOcupacionSolicitada);
			insertarSolicitudStatement.setInt(8, codigoCentroCostoSolicitante);
			insertarSolicitudStatement.setInt(9, codigoCentroCostoSolicitado);
			
			if(codigoMedicoCreador > 0)
				insertarSolicitudStatement.setInt(10, codigoMedicoCreador);
			else
				insertarSolicitudStatement.setNull(10, Types.INTEGER);
	
			insertarSolicitudStatement.setInt(11, codigoCuenta);
			insertarSolicitudStatement.setBoolean(12, vaEpicrisis);
			insertarSolicitudStatement.setBoolean(13, urgente);
			insertarSolicitudStatement.setInt(14, estadoClinico);
			insertarSolicitudStatement.setString(15, datosMedico);
			insertarSolicitudStatement.setBoolean(16,solPYP);
// SE MODIFICA ESTA VALIDACION POR CAMBIO SOLICITADO EN ANEXO 209 - MODIFICIACION PROFESIONAL DE LA MEDICINA
//			if(tieneCita)
//				insertarSolicitudStatement.setString(17, ConstantesBD.acronimoSi);
//			else
				insertarSolicitudStatement.setString(17, ConstantesBD.acronimoNo);
			
			insertarSolicitudStatement.setString(18,liquidarAsocio);
			
			if(!justificacionSolicitud.equals(""))
				insertarSolicitudStatement.setString(19,justificacionSolicitud.length()>4000?justificacionSolicitud.substring(0, 4000):justificacionSolicitud);
			else
				insertarSolicitudStatement.setNull(19,Types.VARCHAR);
			
			if(especialidadSolicitadaOrdAmbulatorias != ConstantesBD.codigoNuncaValido)
				insertarSolicitudStatement.setInt(20,especialidadSolicitadaOrdAmbulatorias);
			else
				insertarSolicitudStatement.setNull(20,Types.INTEGER);
			
			if (codigoMedicoResponde!=ConstantesBD.codigoNuncaValido&&codigoMedicoResponde>0)
				insertarSolicitudStatement.setInt(21, codigoMedicoResponde);
			else
				insertarSolicitudStatement.setNull(21,Types.INTEGER);
			
			//Modificaciones tarea 146930
			logger.info("codigoMedicoResponde-------->"+codigoMedicoResponde);
			if (codigoMedicoResponde!=ConstantesBD.codigoNuncaValido&&codigoMedicoResponde>0)
			{
				insertarSolicitudStatement.setString(22, fechaSolicitud);
				insertarSolicitudStatement.setString(23,(horaSolicitud.length()>5?horaSolicitud.substring(0, 5):horaSolicitud));
				insertarSolicitudStatement.setInt(24, codigoMedicoResponde);
				insertarSolicitudStatement.setInt(25, codigoMedicoResponde);
				insertarSolicitudStatement.setString(26, codigoMedicoResponde+"");
			}
			else
			{
				insertarSolicitudStatement.setString(22,"");
				insertarSolicitudStatement.setString(23,"");
				insertarSolicitudStatement.setInt(24, ConstantesBD.codigoNuncaValido);
				insertarSolicitudStatement.setInt(25, ConstantesBD.codigoNuncaValido);
				insertarSolicitudStatement.setString(26, ConstantesBD.codigoNuncaValido+"");
			}
			//Fin modificaciones tarea 146930
			
			if(dtoDiagnostico!=null && (!UtilidadTexto.isEmpty(dtoDiagnostico.getAcronimoDiagnostico()))&&
					(!UtilidadTexto.isEmpty(dtoDiagnostico.getTipoCieDiagnostico())))
			{
				insertarSolicitudStatement.setString(27, dtoDiagnostico.getAcronimoDiagnostico());
				insertarSolicitudStatement.setInt(28, dtoDiagnostico.getTipoCieDiagnosticoInt());
			}else{
				insertarSolicitudStatement.setNull(27, Types.VARCHAR);
				insertarSolicitudStatement.setNull(28, Types.INTEGER);
			}
			
			if (poolDefinitivo!=null&&!poolDefinitivo.equals(0)) {
				insertarSolicitudStatement.setInt(29, poolDefinitivo);
			}else{
				insertarSolicitudStatement.setNull(29, Types.INTEGER);
			}
			
			//NO IMPRIMIR LA CONSULTA YA QUE EL METODO TOSTRING GENERA EXCEPCION CUANDO ALGUNO
			//DE LOS PARAMETROS CONTIENE CARACTERES ESPECIALES
			//logger.info(" insertar Solicitud General --->SQL " +insertarSolicitudStatement); 
			
			int resp=insertarSolicitudStatement.executeUpdate();
			insertarSolicitudStatement.close();
			
			if (resp>0)
				return numeroSolicitudAInsertar;
			else
				return 0;
			
		}
		catch (SQLException e) 
		{
			logger.warn("error al ingresar la solcitud general > "+e.getMessage());
			logger.error("ERROR",e);
		}
		return 0;
	}

	/**
	 * Método para ingresar la justificación del procedimiento
	 * este método solo inserta un atributo, así que se llama
	 * cada vez que se quiere insertar uno
	 * @param con
	 * @param numeroSolicitud
	 * @param servicio
	 * @param atributo Parámetro tomado de ConstantesBD
	 * @param descripcion Texto del campo que se desea ingresar
	 * @param estado estado de la transacción
	 * @return int mayor que 0 si se insertó correctamente el atributo específico.
	 */
	public static int ingresarAtributoTransaccional(Connection con, int numeroSolicitud, int servicio, int atributo, String descripcion, String estado)
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try
		{
			if(estado.equals(ConstantesBD.inicioTransaccion))
			{
				myFactory.beginTransaction(con);
			}
			PreparedStatementDecorator ingresoJustificacion= new PreparedStatementDecorator(con.prepareStatement(ingresarAtributoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ingresoJustificacion.setInt(1,numeroSolicitud);
			ingresoJustificacion.setInt(2,servicio);
			ingresoJustificacion.setInt(3,atributo);
			ingresoJustificacion.setString(4,descripcion);
			int resultado=ingresoJustificacion.executeUpdate();
			if(estado.equals(ConstantesBD.finTransaccion))
			{
				myFactory.endTransaction(con);
			}
			return resultado;
	
		}
		catch (SQLException e)
		{
			logger.error("Error ingresando la Justificación de la interconsulta: "+e);
			return 0;
		}
	}

	/**
	 * Método que inserta una entrada en la tabla Solicitudes Cambio
	 * Tratante
	 *
	 * @param con Conexión con la BD Genérica
	 * @param idCuenta Código de la cuenta
	 * @param numeroSolicitud Número de la solicitud
	 * @return
	 * @throws SQLException
	 */
	private static int insertarEntradaSolicitudesCambioTratante  (Connection con, int idCuenta, int numeroSolicitud, String insertarEntradaSolicitudesCambioTratanteStr) throws SQLException
	{
		PreparedStatementDecorator insertarEntradaSolicitudesCambioTratanteStatement=new PreparedStatementDecorator(con.prepareStatement (insertarEntradaSolicitudesCambioTratanteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		insertarEntradaSolicitudesCambioTratanteStatement.setInt(1, idCuenta);
	
		insertarEntradaSolicitudesCambioTratanteStatement.setInt(2, numeroSolicitud);
		return insertarEntradaSolicitudesCambioTratanteStatement.executeUpdate();
	}

	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static int insertarSolicitudSubCuenta(Connection con, DtoSolicitudesSubCuenta dto,int codigo) throws BDException
	{
		PreparedStatement pst=null;
		
		try {
			Log4JManager.info("############## Inicio insertarSolicitudSubCuenta");
			String cadena="INSERT INTO solicitudes_subcuenta (codigo,cuenta,sub_cuenta,solicitud,servicio,articulo,porcentaje,monto,cantidad,cubierto,tipo_solicitud,paquetizada,sol_subcuenta_padre,servicio_cx,tipo_asocio,tipo_distribucion,usuario_modifica,fecha_modifica,hora_modifica,det_cx_honorarios,det_asocio_cx_salas_mat) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,current_date,?,?,?)";
			if(codigo>0)
			{
				pst =  con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst.setInt(1, codigo);
				pst.setInt(2, Utilidades.convertirAEntero(dto.getCuenta().replace("'", " ")));
				pst.setInt(3, Utilidades.convertirAEntero(dto.getSubCuenta().replace("'", " ")));
				pst.setInt(4, Utilidades.convertirAEntero(dto.getNumeroSolicitud().replace("'", " ")));
				
				if(UtilidadTexto.isEmpty(dto.getServicio().getCodigo()) 
					|| dto.getServicio().getCodigo().trim().equals(ConstantesBD.codigoNuncaValido+"")
					|| dto.getServicio().getCodigo().trim().equals(ConstantesBD.codigoServicioNoDefinido+""))
				{	
					pst.setNull(5, Types.INTEGER);
				}	
				else
				{
					pst.setInt(5, Utilidades.convertirAEntero(dto.getServicio().getCodigo().replace("'", " ")));
				}	
				
				if(UtilidadTexto.isEmpty(dto.getArticulo().getCodigo().trim()) 
						|| dto.getArticulo().getCodigo().trim().equals(ConstantesBD.codigoNuncaValido+"")
						|| dto.getArticulo().getCodigo().trim().equals("0"))
				{
					pst.setNull(6, Types.INTEGER);
				}	
				else
				{
					pst.setInt(6, Utilidades.convertirAEntero(dto.getArticulo().getCodigo()));
				}	
				if(UtilidadTexto.isEmpty(dto.getPorcentajeCargado())||Double.parseDouble(dto.getPorcentajeCargado())<=0)
				{
					pst.setNull(7, Types.NUMERIC);
				}	
				else
				{
					pst.setDouble(7, Utilidades.convertirADouble(dto.getPorcentajeCargado().replace("'", " ")));
				}	
				if(UtilidadTexto.isEmpty(dto.getMonto()) ||Double.parseDouble(dto.getMonto())<=0)
				{
					pst.setNull(8, Types.DOUBLE);
				}	
				else
				{
					pst.setDouble(8, Utilidades.convertirADouble(dto.getMonto().replace("'", " ")));
				}	
				pst.setDouble(9, Utilidades.convertirADouble(dto.getCantidad().replace("'", " ")));
				pst.setString(10, dto.getCubierto().replace("'", " "));
				
				pst.setInt(11, dto.getTipoSolicitud().getCodigo());
				pst.setString(12, dto.getPaquetizada().replace("'", " "));
	
				if(UtilidadTexto.isEmpty(dto.getSolicitudPadre().trim()))
				{
					pst.setNull(13, Types.INTEGER);
				}	
				else
				{
					pst.setInt(13, Utilidades.convertirAEntero(dto.getSolicitudPadre().replace("'", " ")));
				}	
				if(UtilidadTexto.isEmpty(dto.getServicioCX().getCodigo().trim()))
				{
					pst.setNull(14, Types.INTEGER);
				}	
				else
				{
					pst.setInt(14, Utilidades.convertirAEntero(dto.getServicioCX().getCodigo().replace("'", " ")));
				}	
				if(dto.getTipoAsocio().getCodigo()<1)
				{
					pst.setNull(15, Types.INTEGER);
				}	
				else
				{
					pst.setInt(15, dto.getTipoAsocio().getCodigo());
				}	
				pst.setString(16, dto.getTipoDistribucion().getCodigo().replace("'", " "));
				pst.setString(17, dto.getUsuarioModifica().replace("'", " "));
				pst.setString(18, UtilidadFecha.getHoraActual().replace("'", " "));
				if((dto.getDetcxhonorarios())>0){
					pst.setInt(19,(dto.getDetcxhonorarios()));
				}
				else{
					pst.setNull(19,Types.INTEGER);
				}
				if((dto.getDetasicxsalasmat())>0){
					pst.setInt(20,(dto.getDetasicxsalasmat()));
				}
				else{
					pst.setNull(20,Types.INTEGER);
				}
				pst.executeUpdate();
			}
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			codigo=ConstantesBD.codigoNuncaValido;
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			codigo=ConstantesBD.codigoNuncaValido;
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin insertarSolicitudSubCuenta");
		return codigo;
	}

	/**
	 * Método que implementa la inactivación de una solicitud 
	 * de cambio tratante en una BD Genérica
	 *
	 * @see com.princetonsa.dao.SolicitudDao#inactivarSolicitudCambioTratante(Connection , int )
	 */
	public static ResultadoBoolean inactivarSolicitudCambioTratante(Connection con, int numeroSolicitud)
	{
		try
		{
			PreparedStatementDecorator inactivarSolicitudCambioTratanteStttmnt =  new PreparedStatementDecorator(con.prepareStatement(inactivarSolicitudCambioTratanteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			inactivarSolicitudCambioTratanteStttmnt.setInt(1, numeroSolicitud);
			
			int resultado = inactivarSolicitudCambioTratanteStttmnt.executeUpdate();
			
			if( resultado < 1 )
				return new ResultadoBoolean(false, "No se pudo inactivar la solicitud de cambio tratante ");
			else
				return new ResultadoBoolean(true);
		}
		catch( SQLException sqe )
		{
			return new ResultadoBoolean(false, "No se pudo inactivar la solicitud de cambio tratante. Problemas en la base de datos :  "+sqe.getMessage());
		}
	}

	/**
	* Método que carga el motivo de anulación de una solicitud
	* de Interconsulta en una BD Genérica 
	 * 
	 * @see com.princetonsa.dao.SolicitudDao#modificarEpicrisis(Connection , int , boolean ) throws SQLException
	*/
	public static int modificarEpicrisis(Connection con , int numeroSolicitud,boolean estado) throws SQLException
	{
		PreparedStatementDecorator modificarEpicrisisStatement= new PreparedStatementDecorator(con.prepareStatement(modificarEpicrisisStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		modificarEpicrisisStatement.setBoolean(1, estado);
		modificarEpicrisisStatement.setInt(2,numeroSolicitud);
			
		return modificarEpicrisisStatement.executeUpdate();
	}

	/**
	* Implementación para Genérica del método que modifica la interpretación de una solicitud
	* 
	* @param ac_con				Conexión abierta a una fuente de datos
	* @param ai_numeroSolicitud	Número de la solicitud a ser interpretada
	* @param as_interpretacion	Texto de la interpretación de la solicitud
	* @param ab_vaEpicrisis		Indicador de si la solicitud va o no a la epicrisis
	* @return Número de solicitudes interpretadas.
	* @throws SQLException si se presenta un error en la base de datos Genérica
	*/
	public static int modificarInterpretacionSolicitud(
		Connection	ac_con,
		int			ai_numeroSolicitud,
		String		as_interpretacion
	)throws SQLException
	{
		PreparedStatementDecorator lps_ps;
	
		/* Abrir conexión a la base de datos si es necesario */
		if(ac_con == null || ac_con.isClosed() )
			ac_con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
	
		/* Preparar la consulta para la modificación de la interpretación de la solicitud */
		lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(is_modificarInterpretacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	
		/* Ajustar los parámetros de la modificación de la interpretación */
		lps_ps.setString(1, as_interpretacion + "\n\n");
		lps_ps.setInt(2, ai_numeroSolicitud);
	
		/* Ejecutar la modificación */
		return lps_ps.executeUpdate();
	}

	/**
		 * Método que implementa la movilización de solicitudes de la cuenta 
		 * de urgencias a la de hospitalización en una BD Genérica (De 
		 * acuerdo a reglas asocio) 
		 *
		 * @see com.princetonsa.dao.SolicitudDao#moverSolicitudesCasoAsocio (Connection , int , int ) throws SQLException
		 */
		public static int moverPorAsocio (Connection con, int cuentaOrigen, int cuentaDestino, String insertarAdjuntoAnteriorStr, int codigoMedicoTratante, String insertarTratanteStr, int codigoCentroCostoTratante, int institucion) throws SQLException
		{
	
			
	//		-----------Se consulta el último soporte ingresado en la cuenta de urgencias, para ingresarlo en la parametrización
			//-----------en el caso que no esté parametrizado en la cuenta de hospitalización
			
			String  consultaStr =  " SELECT getCodigoParamSoporCCIns(?, ?, ?, os.equipo_elemento_cc_inst) AS equipo_elemento " +
										                " FROM orden_soporte_respira os " +
										                "           WHERE os.codigo_histo_enca = " +
										                "			(SELECT MAX (codigo_histo_enca) as codigo_histo FROM orden_soporte_respira os " +
										                "			INNER JOIN encabezado_histo_orden_m eh ON (os.codigo_histo_enca=eh.codigo)" +
										                "			 INNER JOIN ordenes_medicas om ON (om.codigo=eh.orden_medica) " +
										                "			WHERE om.cuenta=?)";
			
			int codigoEquipo=0;
	
			try
			{
			PreparedStatementDecorator consultarNov =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));  
			consultarNov.setInt(1, institucion);
			consultarNov.setInt(2, ConstantesBD.codigoViaIngresoUrgencias);
			consultarNov.setInt(3, codigoCentroCostoTratante);
			consultarNov.setInt(4, cuentaOrigen);
			
			ResultSetDecorator resultado=new ResultSetDecorator(consultarNov.executeQuery());
			if(resultado.next())
				{
					codigoEquipo=resultado.getInt("equipo_elemento");
				}
			
			if (codigoEquipo==-1)
			{	
				//---------SE REALIZA LA CONSULTA DEL EQUIPO ELEMENTO QUE NO ESTA PARAMETRIZADO PARA INGRESARLO POSTERIORMENTE----------------------//
				String  consultaEquipo =  "SELECT tee.codigo AS codigo FROM equipo_elemento_cc_inst eecci " +
																"INNER JOIN equipo_elemento tee ON  (tee.codigo=eecci.equipo_elemento) " +
																"WHERE eecci.codigo=" +
																"(SELECT equipo_elemento_cc_inst FROM orden_soporte_respira " +
																"WHERE codigo_histo_enca=" +
																"(SELECT MAX (codigo_histo_enca) as codigo_histo FROM orden_soporte_respira os " +
																"INNER JOIN  encabezado_histo_orden_m eh ON (os.codigo_histo_enca=eh.codigo) " +
																"INNER JOIN ordenes_medicas om ON (om.codigo=eh.orden_medica)  " +
																"WHERE om.cuenta=?))";
	
	
				int tipoEquipo=0;
				
				try
				{
				PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaEquipo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));  
				consulta.setInt(1, cuentaOrigen);
							
				ResultSetDecorator resultado2=new ResultSetDecorator(consulta.executeQuery());
				if(resultado2.next())
				{
					tipoEquipo=resultado2.getInt("codigo");
				}
				}
				catch (SQLException e)
				{
				logger.error("Error Consultado el tipo de equipo elemento que se debe insertar :"+e.toString());
				}
				
				//------------------------------------------------------------------------------------------------------------------------------------------------------//
				
				
				//--------SE VERIFICA SI EL TIPO DE EQUIPO YA ESTA INSERTADO--------------------------------------
				int numRegistros = 0;
				String consultaRegistro = "SELECT " +
					"count(1) AS cuenta " +
					"FROM equipo_elemento_cc_inst " +
					"WHERE equipo_elemento = ? AND centro_costo = ? AND institucion = ? ";
				
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaRegistro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1,tipoEquipo);
				pst.setInt(2,codigoCentroCostoTratante);
				pst.setInt(3,institucion);
				
				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
				
				if(rs.next())
					numRegistros = rs.getInt("cuenta");
				
				if(numRegistros==0&&tipoEquipo>0)
				{
				
				
				
					//---------SE REALIZA LA INSERCIÓN DEL TIPO QUE NO ESTÁ PARAMETRIZADO----------------------//
					PreparedStatementDecorator ps;
					int consecutivo=0;
				
					String insertarStr = 	"INSERT INTO equipo_elemento_cc_inst (codigo, " +
																																	  "equipo_elemento," +
																																	  "centro_costo," +
																																	  "institucion," +
																																	  "activo) " +
																																	  " VALUES " +
																																	  " (?, ?, ?, ?, ?) ";
					
					
					try	{
									DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
									consecutivo=myFactory.incrementarValorSecuencia(con, "seq_equipo_elemento_cc_inst");
						
									ps =  new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
													
									ps.setInt(1, consecutivo);
									ps.setInt(2, tipoEquipo);
									ps.setInt(3, codigoCentroCostoTratante);
									ps.setInt(4, institucion);
									ps.setBoolean(5, true);
															
									ps.executeUpdate();
									
							}
							catch(SQLException e)
							{
									logger.warn(e+" Error en la inserción de la parametrización de equipo elemento por institución "+e.toString() );
							}
				}
				
				//------------------------------------------------------------------------------------------------------------------------------------------------------//
			}// if codigoTipo==-1
			else
			{
				logger.info("ESTA CORRECTAMENTE PARAMETRIZADO EL TIPO DE EQUIPO ELEMENTO EN HOSPITALIZACION \n");
			}
			
			} 
			catch (SQLException e)
			{
			logger.error("Error Consultado el equivalente de la parametrización de equipo elemento en urgencias en hospitalización :"+e.toString());
			}
	
			//----Fin de verificación si está parametrizado un equipo elemento en hospitalización en asocio de cuentas para insertarlo en la parametrizacion------//
			
			
			//En esta clase no se hace revisión por resp(num) porque
			//si se encuentra un error se envía a la parte superior, que en
			//el esquema manejado por Solicitud, es la que se encarga de
			//controlar las transacciones
			int codigoSolicitudInicialHospitalizacion=0;
	
			String actualizarSolicitudesCambioTratanteStr="UPDATE sol_cambio_tratante set cuenta=? where cuenta=? and activa=" + ValoresPorDefecto.getValorTrueParaConsultas();
			PreparedStatementDecorator actualizarSolicitudesCambioTratanteStatement= new PreparedStatementDecorator(con.prepareStatement(actualizarSolicitudesCambioTratanteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			actualizarSolicitudesCambioTratanteStatement.setInt(1, cuentaDestino);
			actualizarSolicitudesCambioTratanteStatement.setInt(2, cuentaOrigen);
			actualizarSolicitudesCambioTratanteStatement.executeUpdate();
			
			PreparedStatementDecorator buscarNumeroSolicitudInicialCuentaHospitalizacionStatement= new PreparedStatementDecorator(con.prepareStatement(buscarNumeroSolicitudInicialCuentaHospitalizacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			buscarNumeroSolicitudInicialCuentaHospitalizacionStatement.setInt(1, cuentaDestino);
			
			ResultSetDecorator rs=new ResultSetDecorator(buscarNumeroSolicitudInicialCuentaHospitalizacionStatement.executeQuery());
			if (rs.next())
			{
				codigoSolicitudInicialHospitalizacion=rs.getInt("numeroSolicitud");
				rs.close();
			}
			else
			{
				rs.close();
				logger.error("El método moverSolicitudesCasoAsocio solo puede ser llamado en caso de asocio de cuenta!(SqlBaseSolicitudDao)");
				throw new SQLException ("El método moverSolicitudesCasoAsocio solo puede ser llamado en caso de asocio de cuenta!");
			}
			
			PreparedStatementDecorator buscarDatosAdjuntosStatement= new PreparedStatementDecorator(con.prepareStatement(buscarDatosAdjuntosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			buscarDatosAdjuntosStatement.setInt(1, cuentaOrigen);
			rs=new ResultSetDecorator(buscarDatosAdjuntosStatement.executeQuery());
	
			//El siguiente sql inserta un adjunto producto de una consulta
			//de los existentes en la cuenta anterior
			int numAdjuntosAnteriores=0;
			PreparedStatementDecorator insertarAdjuntoAnteriorStatement= new PreparedStatementDecorator(con.prepareStatement(insertarAdjuntoAnteriorStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			while (rs.next())
			{
				insertarAdjuntoAnteriorStatement.setInt(1, codigoSolicitudInicialHospitalizacion);
				insertarAdjuntoAnteriorStatement.setInt(2, rs.getInt("codigo_medico"));
				insertarAdjuntoAnteriorStatement.setString(3, rs.getString("fecha_inicio"));
				insertarAdjuntoAnteriorStatement.setString(4, rs.getString("hora_inicio"));
				insertarAdjuntoAnteriorStatement.setInt(5, rs.getInt("centro_costo"));
				insertarAdjuntoAnteriorStatement.setInt(6, codigoSolicitudInicialHospitalizacion);
				insertarAdjuntoAnteriorStatement.addBatch();
				numAdjuntosAnteriores++;
			}
	
			//Cerramos la entrada de tratantes cuenta de la cuenta anterior
			
			//Primero averiguamos el número de la solicitud que creo la
			//entrada en tratantes cuenta
			String buscarSolicitudGeneroTratanteUrgenciasStr="SELECT solicitud as numeroSolicitud from tratantes_cuenta where cuenta=? and (fecha_inicio<CURRENT_DATE or (fecha_inicio=CURRENT_DATE and hora_inicio<="+ValoresPorDefecto.getSentenciaHoraActualBD()+")) and (fecha_fin IS NULL or fecha_fin>CURRENT_DATE or (fecha_fin=CURRENT_DATE and hora_fin>="+ValoresPorDefecto.getSentenciaHoraActualBD()+") )"; 
			PreparedStatementDecorator buscarSolicitudGeneroTratanteUrgenciasStatement= new PreparedStatementDecorator(con.prepareStatement(buscarSolicitudGeneroTratanteUrgenciasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			buscarSolicitudGeneroTratanteUrgenciasStatement.setInt(1, cuentaOrigen);
			rs=new ResultSetDecorator(buscarSolicitudGeneroTratanteUrgenciasStatement.executeQuery());
	
			int numeroSolicitudGeneroTratante=0;
	
			if (rs.next())
			{
				try
				{
					numeroSolicitudGeneroTratante=rs.getInt("numeroSolicitud");
					rs.close();
					//Una vez se tiene el número se cierra la solicitud de tratante
					//La cerramos (La misma solicitud solo puede generar un tratante)
					String cerrarTratanteAnteriorStr="UPDATE tratantes_cuenta set fecha_fin=CURRENT_DATE, hora_fin="+ValoresPorDefecto.getSentenciaHoraActualBD()+" where solicitud=?";
					PreparedStatementDecorator cerrarTratanteAnteriorStatement= new PreparedStatementDecorator(con.prepareStatement(cerrarTratanteAnteriorStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					cerrarTratanteAnteriorStatement.setInt(1, numeroSolicitudGeneroTratante);
					cerrarTratanteAnteriorStatement.executeUpdate();
				
					//Ahora para la cuenta vieja vamos a insertar una entrada con el
					//nuevo tratante
				
					PreparedStatementDecorator insertarTratanteStatement= new PreparedStatementDecorator(con.prepareStatement(insertarTratanteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					insertarTratanteStatement.setInt(1, numeroSolicitudGeneroTratante);
					insertarTratanteStatement.setInt(2, codigoMedicoTratante);
					insertarTratanteStatement.setInt(3, codigoCentroCostoTratante);
					insertarTratanteStatement.setInt(4, numeroSolicitudGeneroTratante);
					insertarTratanteStatement.executeUpdate();
				}
				catch(SQLException e)
				{
					logger.error("Excepción tratantes " + e);
					return 0;
				}
			}
			else
			{
				logger.error("No encontró moverSolicitudesCasoAsocio (SqlBase)");
				return 0;
			}
			
			
			
	
			PreparedStatementDecorator existenSolicitudesAMoverCasoAsocioStatement= new PreparedStatementDecorator(con.prepareStatement(existenSolicitudesAMoverCasoAsocioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			existenSolicitudesAMoverCasoAsocioStatement.setInt(1, cuentaOrigen);
			
			rs=new ResultSetDecorator(existenSolicitudesAMoverCasoAsocioStatement.executeQuery());
			if (rs.next())
			{
				if (rs.getInt("numResultados")>0)
				{
					PreparedStatementDecorator moverSolicitudesCasoAsocioStatement= new PreparedStatementDecorator(con.prepareStatement(moverSolicitudesCasoAsocioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					moverSolicitudesCasoAsocioStatement.setInt(1, cuentaDestino);
					moverSolicitudesCasoAsocioStatement.setInt(2, cuentaOrigen);
					return moverSolicitudesCasoAsocioStatement.executeUpdate();
				}
				else
				{
					//No existen solicitudes a mover, sin embargo, retornamos
					//que todo salió bien
					return 1;
				}
			}
			else
			{
				throw new SQLException("Error en un count en moverSolicitudesCasoAsocio (SqlBase)");
			}
			
					
			
		}

	/**
	 * Método que obtiene el número de la última solicitud insertada
	 *
	 * @param con Conexión con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public static int obtenerNumeroSolicitudRecienInsertada1 (Connection con) throws SQLException
	{
		PreparedStatementDecorator	lps_obtenerNumeroSolicitud;
		ResultSetDecorator			lrs_obtenerNumeroSolicitud;
		int li_numeroSolicitud=0;

		/* Preparar consulta */
		lps_obtenerNumeroSolicitud =  new PreparedStatementDecorator(con.prepareStatement(obtenerUltimoNumeroSolicitud,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		/* Ejecutar la consulta */
		lrs_obtenerNumeroSolicitud = new ResultSetDecorator(lps_obtenerNumeroSolicitud.executeQuery());

		/* Verificar si se obtuvo resultado de la consulta */
		if(lrs_obtenerNumeroSolicitud.next() )
			li_numeroSolicitud = lrs_obtenerNumeroSolicitud.getInt(1);

		/* Verificar si el número de solicitud es válido */
		if(li_numeroSolicitud < 0)
			li_numeroSolicitud = -1;

		/* Liberar recursos de base de datos */
		lrs_obtenerNumeroSolicitud.close();
		lps_obtenerNumeroSolicitud.close();

		return li_numeroSolicitud;
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static int obtenerCodigoCentroCostoSolicitado(Connection con, String numeroSolicitud)
	{
		try
		{
			String consulta = "SELECT centro_costo_solicitado as cc FROM  solicitudes WHERE numero_solicitud = ?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,numeroSolicitud);
			ResultSetDecorator rs= new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getInt("cc");
		}
		catch(SQLException e)
		{
			logger.error("Error en getCodigoCentroCostoSolicitado de SqlBaseSolicitudDao: "+e);
		}
		return 0;
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static int obtenerCodigoCentroCostoSolicitante(Connection con, String numeroSolicitud) throws BDException
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		int resultado=0;
		
		try {
			Log4JManager.info("############## Inicio obtenerCodigoCentroCostoSolicitante");
			String consulta = "SELECT centro_costo_solicitante as cc FROM  solicitudes WHERE numero_solicitud = ?";
			pst =  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setString(1,numeroSolicitud);
			rs= pst.executeQuery();
			if(rs.next()){
				resultado= rs.getInt("cc");
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
		Log4JManager.info("############## Fin obtenerCodigoCentroCostoSolicitante");
		return resultado;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap obteneInfoSolicitudInterfaz(Connection con, String numeroSolicitud)
	{
		HashMap mapa= new HashMap();
		String consulta="SELECT " +
							"coalesce(s.centro_costo_solicitante, -1) as ccsolicitante, " +
							"coalesce(s.centro_costo_solicitado, -1) as ccsolicitado, " +
							"coalesce(s.consecutivo_ordenes_medicas, -1) as consecutivoordenmedica, " +
							"coalesce(p.numero_identificacion, '') as numidmedicosolicita, " +
							"coalesce(p1.numero_identificacion, '') as numidmedicoejecuta, " +
							"sm.centro_costo_principal as ccprincipal," +
							"s.tipo as tipo " +
						"FROM " +
							"solicitudes s " +
							"left outer join solicitudes_medicamentos sm on(sm.numero_solicitud=s.numero_solicitud) " +
							"left outer join personas p on(p.codigo=s.codigo_medico) " +
							"left outer join personas p1 on(p1.codigo=s.codigo_medico_responde)" +
						"WHERE s.numero_solicitud=?";
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,numeroSolicitud);
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), false, false);
		}
		catch(SQLException e)
		{
			logger.error("Error en obteneInfoSolicitudInterfaz de SqlBaseSolicitudDao: "+e);
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	private static int seleccionarSolicitudesInterconsultaAdjunto(Connection con, int idCuenta)
	{
		PreparedStatementDecorator statement=null;
		try
		{
			statement =  new PreparedStatementDecorator(con.prepareStatement(seleccionarSolicitudesAdjuntosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setInt(1, idCuenta);
			ResultSetDecorator rs= new ResultSetDecorator(statement.executeQuery());
			if(rs.next())
				return rs.getInt("sol");
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * Método privado que actualiza el estado de autocommit
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param estado Estado en que debe quedar el auto commit
	 * @return
	 */
	private static ResultadoBoolean setCommit(Connection con, boolean estado)
	{
		try
		{
			con.setAutoCommit(estado);
		}
		catch(SQLException e)
		{
			logger.error("Problemas poniendo el commit en  "+estado+", Valoración Interconsulta "+e);
			return new ResultadoBoolean(false, "Problemas poniendo el commit en  "+estado+", Valoración Interconsulta "+e);
		}							
		
		return new ResultadoBoolean(true);
	}

	/**
	 * Metodo para consultar el Valor del Parametro General de Validar Registro Evoluciones
	 * @param con
	 * @return
	 */
	public static String consultarParametroEvoluciones(Connection con) 
	{
		String consultarParametroGeneral = "SELECT valor from valores_por_defecto where parametro= '"+ConstantesValoresPorDefecto.nombreValidarRegistroEvolucionParaGenerarOrdenes+"' ";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarParametroGeneral, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{	
				return rs.getString("valor");	
			}			
		}
		catch (SQLException e) 
		{
			logger.error("Error en consultar Parametro General");
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoMedico
	 * @return
	 */
	public static String obtenerFirmaDigitalMedico(Connection con, int numeroSolicitud)
	{
		String consulta="SELECT coalesce(firma_digital, '') FROM medicos where codigo_medico=(select codigo_medico from solicitudes where numero_solicitud=?) ";
		try 
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString(1);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String obtenerInfoMedicoSolicita(Connection con, int numeroSolicitud)
	{
		String consulta="SELECT coalesce(datos_medico, '') FROM solicitudes where numero_solicitud=? ";
		try 
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString(1);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * metodo que actualiza el numero de orden de medicamentos con diferente dosificacion
	 * @param con
	 * @param vectorNumeroSolicitudes
	 * @return
	 */
	public static boolean actualizarNumeroOrdenMedDiferenteDosificacion(Connection con, Vector<String> vectorNumeroSolicitudes)
	{
		String consulta="UPDATE solicitudes_medicamentos SET numero_orden_mdd="+UtilidadBD.obtenerSiguienteValorSecuencia(con, "ordenes.seq_numero_orden_mdd")+" where numero_solicitud in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(vectorNumeroSolicitudes, false)+")";
		
		try 
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
	 * @param numeroSolicitud
	 * @param institucion
	 * @return
	 */
	public static String obtenerLoginMedicoSolicita(Connection con, int numeroSolicitud, int institucion)
	{
		String consulta="SELECT login as resultado FROM usuarios where codigo_persona= (select codigo_medico from solicitudes where numero_solicitud=?) and institucion=? ";
		logger.info("consulta obtenerLoginMedicoSolicita-->"+consulta+" sol*>"+numeroSolicitud+" inst->"+institucion);
		try 
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			ps.setInt(2, institucion);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("resultado");
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * Método para actualizar los centros de costo de medicamentos x despachar
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int actualizarCentrosCostoMedicamentosXDespachar(Connection con,HashMap<String, Object> campos)
	{
		int resp = ConstantesBD.codigoNuncaValido;
		try
		{
			//*************TOMAR PARÁMETROS*********************************************
			int idCuenta = Utilidades.convertirAEntero(campos.get("idCuenta").toString());
			int codigoCentroCostoSolicitante = Utilidades.convertirAEntero(campos.get("codigoCentroCostoSolicitante").toString());
			int codigoCentroCostoSolicitado = Utilidades.convertirAEntero(campos.get("codigoCentroCostoSolicitado").toString());
			//**************************************************************************
			
			String consulta = "UPDATE solicitudes SET centro_costo_solicitante = ?, centro_costo_solicitado = ? " +
				"WHERE " +
				"cuenta = ? and " +
				"tipo = "+ConstantesBD.codigoTipoSolicitudMedicamentos+" and " +
				"estado_historia_clinica = "+ConstantesBD.codigoEstadoHCSolicitada+" and " +
				"getNumeroDespachos(numero_solicitud) = 0 ";
			
			logger.info("ACTUALIZACION DE CENTROS DE COSTO: "+consulta+", codigoCentroCostoSolicitante: "+codigoCentroCostoSolicitante+", codigoCentroCostoSolicitado: "+codigoCentroCostoSolicitado+", idCuenta: "+idCuenta);
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoCentroCostoSolicitante);
			pst.setInt(2,codigoCentroCostoSolicitado);
			pst.setInt(3,idCuenta);
			
			resp = pst.executeUpdate();
			
			
		}
		catch(SQLException e)
		{
			logger.error("Error actualizarCentrosCostoMedicamentosXDespachar: "+e);
		}
		logger.info("RESULTADO DE LA ACTUALIZACION: "+resp);
		return resp;
	}
	
	
	/**
	 * Actualiza la informacion de la solicitud
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static boolean actualizarInfoSolicitud(Connection con, HashMap parametros)
	{
		try
		{
			logger.info("valor de parametros >> "+parametros);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strActualizarInfoSolicitud,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,parametros.get("justificacionSolicitud").toString());
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("numeroSolicitud").toString()));
			
			if(ps.executeUpdate()>0)
				return true;				
			
		}catch (Exception e) {
			logger.info("error en actualizarInfoSolicitud "+parametros);
		}
		
		return false;
	}

	public static ArrayList<DtoEntidadSubcontratada> obtenerEntidadesSubcontratadasCentroCosto(
			Connection con, int codigo, int codigoInstitucionInt) {
		ArrayList<DtoEntidadSubcontratada> resultados = new ArrayList<DtoEntidadSubcontratada>();

		try
		{
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(obtenerEntidadesSubcontratadasCentroCostoStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigo);
			pst.setInt(2,codigoInstitucionInt);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				DtoEntidadSubcontratada entidad = new DtoEntidadSubcontratada();
				entidad.setConsecutivo(rs.getString("consecutivo"));
				entidad.setCodigo(rs.getString("codigo"));
				entidad.setRazonSocial(rs.getString("razon_social"));
				
				
				resultados.add(entidad);
			}
			
			pst.close();
			rs.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerEntidadesSubcontratadasCentroCosto: "+e);
		}
		return resultados;
	
	}
	
	// Cambios Segun Anexo 809
	/**
	  * Obtener la especilidad solicitada de un solicitud
	  * @param Connection con
	  * @param HashMap parametros
	  * @return HashMap<String, Object>
	  */
	public static HashMap<String, Object> obtenerEspecilidadSolicitada(Connection con, HashMap parametros) 
	{
		HashMap<String, Object> resultado = new HashMap<String, Object>(); 
		try
		{
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(strObtenerEspecilidadSolicitada,ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(parametros.get("numero_solicitud")+""));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				resultado.put("especilidadSolicitada", rs.getObject("espSolicitada"));
			pst.close();
			rs.close();
		}catch(SQLException e){
			logger.error("Error en obtenerEntidadesSubcontratadasCentroCosto: "+e);
		}
		return resultado;
	
	}
	// Fin Cambios Segun Anexo 809
	
	/**
	 * M&eacute;todo encargado de buscar los articulos asociados a 
	 * una solicitud de medicamentos generada desde un cargo directo de 
	 * articulos
	 * @author Diana Carolina G
	 */
	public static ArrayList<DtoArticulos> buscarArticulosCargosDirectosArticulos (Connection conn,
			int numeroSolicitud) {
		
		ArrayList<DtoArticulos> lista = new ArrayList<DtoArticulos>();
		try
		{
			String consulta="select art.codigo as codigoArticulo, "+
					"art.descripcion as descripcionArticulo, "+
					"art.naturaleza as acronimoNaturaleza, "+
					"na.es_medicamento as esMedicamento, "+
					"na.nombre as nombreNaturaleza, "+
					"um.nombre as nombreUnidadM, "+
					"um.acronimo as acronimoUnidadM, "+
					"ds.dosis as dosis, "+
					"uni.codigo as codigoUnidosis, "+
					"(select dp.cantidad from inventarios.detalle_despachos dp inner join inventarios.despacho d on (dp.despacho = d.orden) "+
					"where dp.articulo = art.codigo and d.numero_solicitud = ds.numero_solicitud) as cantidadArticulo "+
					"from detalle_solicitudes ds "+
					"inner join articulo art on (art.codigo = ds.articulo) "+
					"inner join naturaleza_articulo na on (na.acronimo = art.naturaleza) "+
					"LEFT JOIN inventarios.unidosis_x_articulo uni on (ds.unidosis_articulo=uni.codigo)  "+
					"LEFT JOIN inventarios.unidad_medida um on (uni.unidad_medida = um.acronimo)  "+
					"where ds.numero_solicitud = ? ";
			
			PreparedStatementDecorator ps = new PreparedStatementDecorator(conn.prepareStatement(consulta,ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, numeroSolicitud);
			ResultSet rs = ps.executeQuery();
			lista = poblarArticulosCargosDirectosArticulos(rs);
			
			
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return lista;
	}

	/**
	 * Este m&eacute;todo se encarga de poblar un 
	 * objeto de tipo DtoArticulos
	 * @param rs
	 * @return ArrayList<DtoArticulos>
	 * @author Diana Carolina G
	 */
	private static ArrayList<DtoArticulos> poblarArticulosCargosDirectosArticulos (
			ResultSet rs) throws Exception{
		
		ArrayList<DtoArticulos> lista = new ArrayList<DtoArticulos>();
		DtoArticulos dto=null;
		while(rs.next()){
			dto= new DtoArticulos();
			dto.setCodigoArticulo(rs.getInt("codigoArticulo"));
			dto.setDescripcionArticulo(rs.getString("descripcionArticulo"));
			dto.setNaturalezaArticulo(rs.getString("acronimoNaturaleza"));
			dto.setUnidad_medida(rs.getString("nombreUnidadM"));
			dto.setAcronimoUnidadMedida(rs.getString("acronimoUnidadM"));
			dto.setNombreNaturaleza(rs.getString("nombreNaturaleza"));
			dto.setEsMedicamento((rs.getString("esMedicamento")).charAt(0));
			dto.setCantidadArticulo(rs.getInt("cantidadArticulo"));
			dto.setDosis(rs.getString("dosis"));
			dto.setDosisXArticuloID(rs.getLong("codigoUnidosis"));
			lista.add(dto);
			
		}
		
		return lista;
	}
	

	/**
	 * Metodo que se encarga de consultar los pooles de un medico 
	 * @param con
	 * @param codigoMedico
	 * @return lista con los pooles asociados a un medico
	 */
	public static ArrayList<Integer> consultarValorPoolMedico(Connection con,Integer codigoMedico) 
	{
		String consultarParametroGeneral = SqlBaseSolicitudDao.consultarPoolXMedico;
		//lista que contiene los pooles 
		ArrayList<Integer> pool = new ArrayList<Integer>();
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarParametroGeneral, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//set de codigo de medico para hacer la consulta
			ps.setInt(1,codigoMedico );
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{	
				//se adiciona el pool a la lista 
				pool.add(rs.getInt(1));	
			}			
		}//control de errores 
		catch (SQLException e) 
		{
			logger.error("Error en consultar Parametro General");
		}
		
		return pool;
	}
	
	/**
	 * 
	 */
	public static String obtenerInterpretacionSolicitud(int numeroSolicitud) 
	{
		Connection con=UtilidadBD.abrirConexion();
		String resultado="";
		String consulta="SELECT CASE WHEN interpretacion IS NULL THEN ' ' ELSE interpretacion END as interpretacion FROM solicitudes where numero_solicitud=? ";
		try 
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
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
		UtilidadBD.closeConnection(con);
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static int obtenerCodigoCita(Connection con, int numeroSolicitud) 
	{
		int resultado=ConstantesBD.codigoNuncaValido;
		String consulta="SELECT sc.codigo_cita from solicitudes sol INNER JOIN servicios_cita sc ON(sc.numero_solicitud=sol.numero_solicitud) where sol.numero_solicitud=? ";
		try 
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				resultado=rs.getInt(1);
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
	 * @param numeroSolicitud
	 * @return
	 */
	public static int obtenerEspecilidadSolicitadaCita(Connection con,int numeroSolicitud) 
	{
		int resultado=ConstantesBD.codigoNuncaValido;
		String consulta="SELECT coalesce(sc.especialidad,ser.especialidad) AS codigo_especialidad " +
				"from solicitudes sol " +
				"INNER JOIN servicios_cita sc ON(sc.numero_solicitud=sol.numero_solicitud) " +
				"INNER JOIN servicios ser ON(ser.codigo=sc.servicio) " +
				"where sol.numero_solicitud=? ";
		logger.info(consulta);
		try 
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				resultado=rs.getInt(1);
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
	 * @param con
	 * @param numeroSolicitud
	 * @return si es tipo de solicitud de valoracion consulta externa
	 * @throws Exception
	 */
	public static Boolean esConsultaExterna(Connection con,int numeroSolicitud) throws Exception{
		Boolean res = false;
		Integer tipoSolicitud=new Integer(0);
		String consulta="";
		consulta = " select tipo from   ordenes.solicitudes s inner join  ordenes.TIPOS_SOLICITUD ts " +
				" on(s.tipo=ts.codigo) where  s.numero_solicitud = ? ";
		try 
		{
			PreparedStatement pst =con.prepareStatement(consulta);
			pst.setInt(1, numeroSolicitud);
			ResultSet resul = pst.executeQuery();
			if(resul.next()){
				tipoSolicitud=resul.getInt("tipo");
			}
			
			if(tipoSolicitud==ConstantesBD.codigoTipoSolicitudCita){
				res=true;
			}
			
		} 
		catch (SQLException e) 
		{
			logger.error("Se presento un error consultando el tipo de solicitud :"+e.getMessage());
			throw new Exception("Se presento un error consultando el tipo de solicitud :"+e.getMessage());
		}
		
		return res;
		
	}
	
	
}