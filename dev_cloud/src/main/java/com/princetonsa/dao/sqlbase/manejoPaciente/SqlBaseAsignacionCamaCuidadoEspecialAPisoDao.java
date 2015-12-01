package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.manejoPaciente.AsignacionCamaCuidadoEspecialAPiso;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;





/**
 * Clase SqlBaseAsignacionCamaCuidadoEspecialDao
 * @author Jhony Alexander Duque A.
 * 
 */
public class SqlBaseAsignacionCamaCuidadoEspecialAPisoDao
{
	/**
	 *Mensajes de error 
	 */
	static Logger logger = Logger.getLogger(SqlBaseAsignacionCamaCuidadoEspecialAPisoDao.class);

	//indices
	
	static String [] indicesListado=AsignacionCamaCuidadoEspecialAPiso.indicesListado;
	static String [] indicesIngresosCuiEsp= AsignacionCamaCuidadoEspecialAPiso.indicesIngCuiEsp;
	
	/*----------------------------------------------------------------------------------------
	 *            ATRIBUTOS ASIGNACION CAMA PACIENTES CUIDADO ESPECIAL A PISO
	 -----------------------------------------------------------------------------------------*/
	
	private static final String strConsultaPacientes=" SELECT " +
															" getconsecutivoingreso(ice.ingreso) As ingreso0, " +
															" getnombrepersona(c.codigo_paciente) As paciente1, " +
															" getidpaciente(c.codigo_paciente) As ident_pac2, " +
															" getfechanacimientopaciente(c.codigo_paciente) As fecha_nacimiento3, " +
															" getdescripcionsexo(getsexopaciente(c.codigo_paciente)) As sexo_pac4, " +
															" getfechahoraordentraslado(c.id) As fecha_hora_orden5, " +
															" getprofesionalultevolucion(c.id) As profesional6, " +
															" getnomcentrocosto(c.area) As centro_costo7," +
															" getnombretipomonitoreo(ice.tipo_monitoreo) As tipo_monitoreo8," +
															" manejopaciente.getNombDiagnosticoPpalXcuenta(c.id) As diagnostico_ppal9," +
															" getnombreconvenioxingreso(ice.ingreso) AS convenio10, " +
															" getcamacuenta(c.id,c.via_ingreso) As cama11," +
															" c.id As id_cuenta12," +
															" c.codigo_paciente As codigo_persona13 " +
														" FROM ingresos_cuidados_especiales ice " +
														" INNER JOIN cuentas c ON (c.id_ingreso=ice.ingreso) " +
															" where ice.estado='"+ConstantesIntegridadDominio.acronimoEstadoActivo+"'" +
															" AND getconductasegirevolucion(c.id)='"+ConstantesBD.codigoConductaASeguirTrasladarAPisoEvolucion+"'";
	
	
	private static final String strConsultaDetalle=" SELECT " +
															" getcamacuenta(c.id,c.via_ingreso) As cama_actual3," +
															" getnombretipohabitacionxcama(getcodigocamaxcuenta(c.id,c.via_ingreso)) As tipo_hbit_actual4," +
															" getnomcentrocosto(c.area) As c_costo_actual5," +
															" getnombretipomonitoreo(ice.tipo_monitoreo) As tipo_mon_actual6," +
															" getcodigocamaxcuenta(c.id,c.via_ingreso) As codigo_cama_actual16," +
															" ice.tipo_monitoreo As codigo_tipo_monitoreo17," +
															" c.codigo_paciente As codigo_paciente18," +
															" c.id As cuenta19," +
															" getconvenioxingreso(ice.ingreso) As convenio20," +
															" ice.ingreso As ingreso21," +
															" getfechahoraordentraslado(c.id) As fecha_hora_orden22 ," +
															" ice.codigo AS cod_ingreso_cuidado_especial23 " +//FIXME ACA	
														" FROM ingresos_cuidados_especiales ice " +
														" INNER JOIN cuentas c ON (c.id_ingreso=ice.ingreso) " +
														" WHERE c.id=?"; 
	
	
	private static final String strActualizaEstadoIngresosCuiEsp=" UPDATE ingresos_cuidados_especiales SET fecha_finaliza=?,hora_finaliza=?,usuario_finaliza=?,estado=? WHERE codigo=? ";
	
	/*----------------------------------------------------------------------------------------
	 *           FIN ATRIBUTOS ASIGNACION CAMA PACIENTES CUIDADO ESPECIAL A PISO
	 -----------------------------------------------------------------------------------------*/
	
	
	/*----------------------------------------------------------------------------------------
	 *           							METODOS 
	 -----------------------------------------------------------------------------------------*/
	/**
	 * Metodo encargado de actualizar el estado de ingreso a cuidados especiales
	 * a FINALIZADA.
	 * @param connection
	 * @param criterios
	 * -----------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ------------------------------
	 * -- fechaFinaliza0
	 * -- horaFinaliza1
	 * -- usuarioFinaliza2
	 * -- estado3
	 * -- ingreso4
	 * @return true/false
	 */
	public static boolean actualizarEstadoIngresoCuiEsp (Connection connection,HashMap criterios)
	{
		logger.info("\n entro a actualizarEstadoIngresoCuiEsp --> "+criterios);
		String cadena=strActualizaEstadoIngresosCuiEsp;
		
		logger.info("\n cadena --> "+cadena);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			/**
			 *  UPDATE ingresos_cuidados_especiales SET fecha_finaliza=?,hora_finaliza=?,usuario_finaliza=?,estado=? WHERE codigo=? 
			 */
			
			//fecha finaliza
			ps.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesIngresosCuiEsp[0])+"")));
			//hora finaliza
			ps.setString(2, criterios.get(indicesIngresosCuiEsp[1])+"");
			//usuario finaliza
			ps.setString(3, criterios.get(indicesIngresosCuiEsp[2])+"");
			//estado
			ps.setString(4, criterios.get(indicesIngresosCuiEsp[3])+"");
			//codigoIngresoCuidadoEspecial
			ps.setInt(5, Utilidades.convertirAEntero(criterios.get(indicesIngresosCuiEsp[4])+""));
			
			if (ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			logger.info("\n problema actualizando el estado de ingresos cuidados especiales "+e);
		}
		
		
		return false;
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
		String cadena=strConsultaDetalle;
		
		try 
		{
			logger.info("\n cadena --> "+cadena);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//cuenta
			ps.setInt(1, Utilidades.convertirAEntero(criterios.get(indicesListado[12])+""));
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,true);
			ps.close();
			return mapaRetorno;
			
		}
		catch (SQLException e) 
		{
			logger.info("\n problema consutando el detalle de asignacion de cama a pacientes cuidado especial a piso "+e);
		}
		
		return result;
	}
	
	
	/**
	 * Metodo encargado de consultasr los pacientes de cuidados especiales
	 * @param connection
	 * @param criterios
	 * @return mapa
	 * -----------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -----------------------------------
	 * -- ingreso0_
	 * -- paciente1_
	 * -- identPac2_
	 * -- fechaNacimiento3_
	 * -- sexoPac4_
	 * -- fechaHoraOrden5_
	 * -- profesional6_
	 * -- centroCosto7_
	 * -- tipoMonitoreo8_
	 * -- diagnosticoPpal9_
	 * -- convenio10_
	 * -- cama11_
	 */
	public static HashMap consultaPacientes (Connection connection, HashMap criterios)
	{
		logger.info("\n entre a consultaPacientes  --> "+criterios);
		
		HashMap result= new HashMap ();
		String cadena=strConsultaPacientes;
		
		cadena+=" ORDER BY fecha_hora_orden5 DESC ";
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
			logger.info("\n problema consutando los pacientes de cuidados especiales "+e);
		}
		
		return result;
	}
	
}