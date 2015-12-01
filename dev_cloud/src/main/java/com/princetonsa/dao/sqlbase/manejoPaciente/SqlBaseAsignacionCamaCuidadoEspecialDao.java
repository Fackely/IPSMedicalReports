package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;


import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.ValoresPorDefecto;





/**
 * Clase SqlBaseAsignacionCamaCuidadoEspecialDao
 * @author Luis Gabriel Chavez Salazar
 * lgchavez@princetonsa.com
 * 
 */
public class SqlBaseAsignacionCamaCuidadoEspecialDao
{
	/**
	 *Mensajes de error 
	 */
	static Logger logger = Logger.getLogger(SqlBaseAsignacionCamaCuidadoEspecialDao.class);

	
	
	/*----------------------------------------------------------------------------------------
	 *            ATRIBUTOS ASIGNACION CAMA PACIENTES CUIDADO ESPECIAL A PISO
	 -----------------------------------------------------------------------------------------*/
	
	private static final String strConsultaPacientes=" SELECT " +
															" getconsecutivoingreso(c.id_ingreso) As ingreso, " +
															" getnombrepersona(c.codigo_paciente) As paciente, " +
															" getidpaciente(c.codigo_paciente) As ident_pac, " +
															" getfechanacimientopaciente(c.codigo_paciente) As fecha_nacimiento, " +
															" getdescripcionsexo(getsexopaciente(c.codigo_paciente)) As sexo_pac, " +
															" getfechahoraordentraslado(c.id) As fecha_hora_orden, " +
															" getprofesionalultevolucion(c.id) As profesional, " +
															" getnomcentrocosto(c.area) As centro_costo," +
															" getnombretipomonitoreo(?) As tipo_monitoreo," +
															" manejopaciente.getNombDiagnosticoPpalXcuenta(c.id) As diagnostico_ppal," +
															" getnombreconvenioxingreso(c.id_ingreso) AS convenio, " +
															" getcamacuenta(c.id,c.via_ingreso) As cama," +
															" c.id As id_cuenta, " +
															" i.id AS idingreso," +
															" getFechaOrdenTraslado(c.id) as fecha," +
															" getHoraOrdenTraslado(c.id) as hora " +
														" FROM " +
														"	cuentas c  " +
														"INNER JOIN " +
														"	ingresos i ON (c.id_ingreso=i.id) " +
														" WHERE " +
															"     c.via_ingreso="+ConstantesBD.codigoViaIngresoHospitalizacion+" " +
															" AND c.tipo_paciente='"+ConstantesBD.tipoPacienteHospitalizado+"' " +
															" AND i.estado='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' "+
															" AND c.estado_cuenta IN ("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaAsociada+","+ConstantesBD.codigoEstadoCuentaFacturadaParcial+") "+
															" AND manejopaciente.getTieneIngresoCuidEspecActivo(c.id_ingreso)='N'" +
															" AND getconductasegirevolucion(c.id)='"+ConstantesBD.codigoConductaASeguirTrasladoCuidadoEspecial+"'" +
															" AND getTipoMonitoreoCuenta(c.id)=?" +
														" ORDER BY fecha desc, hora desc";
														
	
	
	private static final String strConsultaDetalle=" SELECT " +
														" getcamacuenta(c.id,c.via_ingreso) As cama_actual," +
														" getnombretipohabitacionxcama(getcodigocamaxcuenta(c.id,c.via_ingreso)) As tipo_hbit_actual," +
														" getnomcentrocosto(getcentrocostoxtipomonitoreo(?)) As c_costo_actual," +
														" getnombretipomonitoreo(?) As tipoMonActual," +
														" getcodigocamaxcuenta(c.id,c.via_ingreso) As codigo_cama_actual," +
														" '?' As codigo_tipo_monitoreo, " +
														" c.codigo_paciente As codigo_paciente," +
														" c.id As cuenta," +
														" getconvenioxingreso(c.id_ingreso) As convenio," +
														" c.id_ingreso As ingreso," +
														" getescamacuidadoespecial(getcodigocamaxcuenta(c.id,c.via_ingreso)) As camaEspecial	 " +
													" FROM " +
													"	cuentas c  " +
													" WHERE c.id=?"; 

	
	
	/*----------------------------------------------------------------------------------------
	 *           FIN ATRIBUTOS ASIGNACION CAMA CUIDADO ESPECIAL 
	 -----------------------------------------------------------------------------------------*/
	
	
	/*----------------------------------------------------------------------------------------
	 *           							METODOS 
	 -----------------------------------------------------------------------------------------*/
	/**
	 * Metodo encargado de consultasr los pacientes de cuidados especiales
	 * @param connection
	 * @param criterios
	 * @return mapa
	 * -----------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -----------------------------------
	 * -- ingreso_
	 * -- paciente_
	 * -- identPac_
	 * -- fechaNacimiento_
	 * -- sexoPac_
	 * -- fechaHoraOrden_
	 * -- profesional_
	 * -- centroCosto_
	 * -- tipoMonitoreo_
	 * -- diagnosticoPpal_
	 * -- convenio_
	 * -- cama_
	 */
	public static HashMap consultaPacientes (Connection connection, int tipoMonitoreo)
	{
		logger.info("\n entre a consultaPacientes [tipoMonitoreo] --> "+tipoMonitoreo);
		
		HashMap result= new HashMap ();
		String cadena=strConsultaPacientes;
		
		try 
		{
			logger.info("\n cadena --> "+cadena);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, tipoMonitoreo);
			ps.setInt(2, tipoMonitoreo);
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			ps.close();
			return mapaRetorno;
			
		}
		catch (SQLException e) 
		{
			logger.info("\n problema consutando los pacientes de cuidados especiales "+e);
		}
		
		return result;
	}
	
	
	
	/**
	 * Metodo encargado de consultar la informacion a mostrar en el detalle
	 * @param connection
	 * @param criterios
	 * ----------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ----------------------------
	 * -- idCuenta12_
	 * @return mapa
	 * ---------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * ---------------------------
	 * -- camaActual3_
	 * -- tipoHbitActual4_
	 * -- cCostoActual5_
	 * -- tipoMonActual6_
	 * -- codigoCamaActual16_
	 * -- codigoTipoMonitoreo17_
	 */
	public static HashMap consultaDetalle (Connection connection, HashMap criterios)
	{
		logger.info("\n entre a consultaDetalle  --> "+criterios);
		
		HashMap result= new HashMap ();
		String cadena=" SELECT " +
							" getcamacuenta(c.id,c.via_ingreso) As cama_actual," +
							" getnombretipohabitacionxcama(getcodigocamaxcuenta(c.id,c.via_ingreso)) As tipo_hbit_actual," +
							" getnomcentrocosto(c.area) As c_costo_actual," +
							" getnombretipomonitoreo( "+criterios.get("tipoMonitoreo")+") As tipoMonActual," +
							" getcodigocamaxcuenta(c.id,c.via_ingreso) As codigo_cama_actual," +
							" '"+criterios.get("tipoMonitoreo")+"' As codigo_tipo_monitoreo, " +
							" c.codigo_paciente As codigo_paciente," +
							" c.id As cuenta," +
							" getconvenioxingreso(c.id_ingreso) As convenio," +
							" c.id_ingreso As ingreso," +
							" getescamacuidadoespecial(getcodigocamaxcuenta(c.id,c.via_ingreso)) As camaEspecial	 " +
						" FROM " +
						"	cuentas c  " +
						" WHERE c.id="+criterios.get("idCuenta_")+""; 
		try 
		{
			logger.info("\n cadena --> "+cadena);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			ps.close();
			return mapaRetorno;
		}
		catch (SQLException e) 
		{
			logger.info("\n problema consutando el detalle de asignacion de cama cuidado especial "+e);
		}
		
		return result;
	}
	
	
	
	
	public static int guardarIngresoCuidadosEspeciales (Connection connection, HashMap datos)
	{
		logger.info("\n entre a guardarIngresoCuidadosEspeciales  --> "+datos);
		
		int result= 0;
		String cadena="INSERT INTO ingresos_cuidados_especiales (codigo," +
																"ingreso," +
																"estado," +
																"indicativo," +
																"tipo_monitoreo," +
            												  //"valoracion_orden," +
																"usuario_resp," +
																"fecha_resp," +
																"hora_resp," +
																"usuario_modifica," +
																"fecha_modifica," +
																"hora_modifica, " +
																"evolucion_orden," +
																"centro_costo" +
															//	"valoracion) " +
															") VALUES (" +
															""+UtilidadBD.obtenerSiguienteValorSecuencia(connection, "historiaclinica.seq_ingres_cuidados_especial")+", " +
															""+datos.get("ingreso")+","+
															"'"+ConstantesIntegridadDominio.acronimoEstadoActivo+"',"+
															"'"+ConstantesIntegridadDominio.acronimoManual+"',"+
															""+datos.get("monitoreo")+","+
														//	""+datos.get("valoorden")+","+
															"'"+datos.get("usuarior")+"',"+
															"'"+datos.get("fechar")+"',"+
															"'"+datos.get("horar")+"',"+
															"'"+datos.get("usuariom")+"',"+
															" CURRENT_DATE ,"+
															" "+ValoresPorDefecto.getSentenciaHoraActualBD()+", "+
															""+datos.get("evoorden")+"," +
															""+datos.get("cCosto")+""+
														//	""+datos.get("valoracion")+""+															
															")";
		

logger.info("\n\n [SENTENCIA INSERTA INGRESO_CUIDADOS_ESPECIALES ]"+cadena);
		
		
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			result=ps.executeUpdate();
			
		}
		catch (SQLException e) 
		{
			logger.info("\n PROBLEMA INSERTANDO INGRESO CUIDADO ESPECIAL "+e);
		}
		
		return result;
	}
	
	
}