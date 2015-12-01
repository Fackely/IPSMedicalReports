package com.princetonsa.dao.sqlbase.consultaExterna;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

/**
 * @author Mauricio Jllo
 * Fecha Mayo de 2008
 */

public class SqlBaseAsignarCitasControlPostOperatorioDao
{

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseAsignarCitasControlPostOperatorioDao.class);
		
	/**
	 * Cadena SELECT que consulta las citas reservadas por control post-operatorio
	 */
	private static String strConCitasPostOperatorio = "SELECT "+
															"c.codigo AS codigocita, "+
															"getnomcentroatencion(cc.centro_atencion) AS centroatencion, "+
															"c.codigo_paciente AS codigopaciente, "+
															"getidpaciente(c.codigo_paciente) AS idpaciente, "+
															"getnombrepersona(c.codigo_paciente) AS nombrepaciente, "+
															"getnombreunidadconsulta(c.unidad_consulta) AS unidadconsulta, "+
															"to_char(sc.fecha_cita, 'DD/MM/YYYY') AS fechacita, "+
															"sc.hora_inicio_cita AS horacita, "+
															"coalesce(getnombrepersona(coalesce(ag.codigo_medico,"+ConstantesBD.codigoNuncaValido+")), 'NO ASIGNADO') AS medico, "+
															"getnombreconsultorio(ag.consultorio) AS consultorio, "+
															"getNombreEstadoCita(c.estado_cita) AS estadocita, "+
															"sc.servicio AS servicio, "+
															"'(' || getcodigoespecialidad(sc.servicio) || '-' || sc.servicio || ')' AS codigoservicio, "+
															"getnombreservicio(sc.servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombreservicio, "+
															"c.estado_liquidacion AS estadoliquidacion, "+
															"sc.estado AS estadoservicio, "+
															"sc.numero_solicitud AS numerosolicitud, "+
															"c.codigo_agenda AS agenda, "+
															"sc.especialidad AS especialidad, "+
															"sc.centro_costo AS centrocosto, "+
															"gettiposervicio(sc.servicio) AS tiposervicio, "+
															"tieneCitaServDifConsulta(c.codigo) AS sumatoria "+
														"FROM "+
															"cita c "+
															"INNER JOIN agenda ag ON (c.codigo_agenda = ag.codigo) "+
															"INNER JOIN servicios_cita sc ON (c.codigo = sc.codigo_cita) "+
															"INNER JOIN centros_costo cc ON (sc.centro_costo = cc.codigo) " +
															"INNER JOIN consultaexterna.unidades_consulta uc ON (c.unidad_consulta=uc.codigo) "+
														"WHERE "+
															"c.codigo_paciente = ? "+
															"AND c.estado_cita IN ("+ConstantesBD.codigoEstadoCitaReservada+", "+ConstantesBD.codigoEstadoCitaAsignada+", "+ConstantesBD.codigoEstadoCitaReprogramada+") "+
															"AND sc.control_post_operatorio_cx IS NOT NULL "+
															"AND sc.numero_solicitud IS NULL "+
															"AND sc.estado = '"+ConstantesIntegridadDominio.acronimoEstadoActivo+"' " +
															"AND uc.tipo_atencion='"+ConstantesIntegridadDominio.acronimoTipoAtencionGeneral+"' "+
														"ORDER BY c.codigo ";
	
	/**
	 * 
	 */
	public static String cadenaInsertarSubcuenta = "INSERT INTO sub_cuentas " +
													" ( " +
														" sub_cuenta," + //1
														" convenio," + //2
														" naturaleza_paciente," + //3
														" monto_cobro," + //4
														" fecha_modifica," + //5
														" usuario_modifica," + //6
														" contrato," + //7
														" ingreso," + //8
														" tipo_afiliado," + //9
														" clasificacion_socioeconomica," + //10
														" codigo_paciente," + //11
														" nro_prioridad," + //12
														" facturado," + //13
														" hora_modifica" + //14
													" ) " +
													" VALUES " +
													" ( " +
														"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?" +
													" )";
	
	/**
	 * 
	 */
	public static String strUpdateControlPostOperatorio = "UPDATE ingresos SET control_post_operatorio_cx = '"+ConstantesBD.acronimoSi+"' WHERE id = ?";
	
	/**
	 * Metodo que consulta la citas reservadas por control
	 * post-operatorio
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarCitasReservadas(Connection con, int codigoPaciente)
	{
		HashMap mapa = new HashMap<String, Object>();
		mapa.put("numRegistros","0");
		try
        {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConCitasPostOperatorio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        	ps.setInt(1, codigoPaciente);
        	logger.info("\n====>Consulta Citas Reservadas por Control P.O.: "+strConCitasPostOperatorio);
        	mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE CITAS RESERVADAS POR CONTROL P.O.");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
	}

		/**
	 * @param con
	 * @param int1
	 * @param usuario
	 * @param mapaSubcuenta
	 * @return
	 */
	public static int insertarSubCuenta(Connection con, int int1, String usuario, HashMap datosSubCuenta)
	{
		try
		{
			PreparedStatementDecorator psTemp =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarSubcuenta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			psTemp.setDouble(1, Utilidades.convertirADouble(int1+""));
			psTemp.setInt(2, Utilidades.convertirAEntero(datosSubCuenta.get("convenio").toString()));
			psTemp.setInt(3, Utilidades.convertirAEntero(datosSubCuenta.get("naturalezaPaciente").toString()));
			psTemp.setInt(4, Utilidades.convertirAEntero(datosSubCuenta.get("montoCobro").toString()));
			psTemp.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			psTemp.setString(6, usuario);
			psTemp.setInt(7, Utilidades.convertirAEntero(datosSubCuenta.get("contrato").toString()));
			psTemp.setInt(8, Utilidades.convertirAEntero(datosSubCuenta.get("ingreso").toString()));
			psTemp.setString(9, datosSubCuenta.get("tipoAfiliado").toString());
			psTemp.setInt(10, Utilidades.convertirAEntero(datosSubCuenta.get("clasificacion").toString()));
			psTemp.setInt(11, Utilidades.convertirAEntero(datosSubCuenta.get("codPaciente").toString()));
			psTemp.setInt(12, Utilidades.convertirAEntero(datosSubCuenta.get("nroPrioridad").toString()));
			psTemp.setString(13, datosSubCuenta.get("facturado").toString());
			psTemp.setString(14, UtilidadFecha.getHoraActual(con));
			
			logger.info("====>Inserccion SubCuenta: "+cadenaInsertarSubcuenta);
			logger.info("====>Parametros1: "+int1+" ==>"+datosSubCuenta.get("convenio")+" ==>"+datosSubCuenta.get("naturalezaPaciente")+" ==>"+datosSubCuenta.get("montoCobro")+" ==>"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con)));
			logger.info("====>Parametros2: "+usuario+" ==>"+datosSubCuenta.get("contrato")+" ==>"+datosSubCuenta.get("ingreso")+" ==>"+datosSubCuenta.get("tipoAfiliado")+" ==>"+datosSubCuenta.get("clasificacion"));
			logger.info("====>Parametros3: "+datosSubCuenta.get("codPaciente")+" ==>"+datosSubCuenta.get("nroPrioridad")+" ==>"+datosSubCuenta.get("facturado")+" ==>"+UtilidadFecha.getHoraActual(con));
			if(psTemp.executeUpdate() > 0)
				return int1;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			logger.info("\n====>ERROR INSERTANDO LA SUBCUENTA DEL PACIENTE");
			return ConstantesBD.codigoNuncaValido;
		}
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * @param con
	 * @param idIngreso
	 * @param updateInsert
	 * @return
	 */
	public static boolean controlPostOperatorio(Connection con, int idIngreso)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(strUpdateControlPostOperatorio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1, idIngreso);
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			logger.error(e+"ERROR ACTUALIZANDO EL CAMPO CONTROL POST OPERATORIO DEL INGRESO");
		}
		return false;
	}
	
}