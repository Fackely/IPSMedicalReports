package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.manejoPaciente.ConsultarIngresosPorTransplantes;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;






/**
 * SqlBaseConsultarIngresosPorTransplantesDao
 * @author Jhony Alexander Duque A.
 * 
 */
public class SqlBaseConsultarIngresosPorTransplantesDao
{
	/**
	 *Mensajes de error 
	 */
	static Logger logger = Logger.getLogger(SqlBaseConsultarIngresosPorTransplantesDao.class);
	
	
//	----------------------------
	//-------indices--------------
	
	private static String [] indicesCriterios=ConsultarIngresosPorTransplantes.indicesCriterios;
	
	/*----------------------------------------------------------------------------------------
	 *                      ATRIBUTOS TRASLADO SOLICITUDES POR TRANSPLANTE
	 -----------------------------------------------------------------------------------------*/
	private static final String strCadenaConsultaIngresosTransplante = " SELECT " +
																		" i.centro_atencion As centro_atencion0, " +
																		" getnomcentroatencion(i.centro_atencion) As nom_centro_costo1, " +
																		" i.id As id_ingreso2, " +
																		" i.consecutivo As consecutivo3, " +
																		" to_char(i.fecha_ingreso,'dd/mm/yyyy') As fecha_ingreso4, " +
																		" c.via_ingreso As via_ingreso5, " +
																		" getnombreviaingreso(c.via_ingreso)||' - '|| getnombretipopaciente(c.tipo_paciente) As nom_via_ingreso6, " +
																		" CASE WHEN i.transplante='"+ConstantesIntegridadDominio.acronimoIndicativoDonante+"'" +
																				" THEN 'Donante' else 'Receptor' END As nom_transplante7, " +
																		" i.codigo_paciente As paciente8," +
																		" getnombrepersona(i.codigo_paciente) As nom_persona9, " +
																		" getidpaciente(i.codigo_paciente) As ident_paciente10, " +
																		" getdescripcionsexo(getsexopaciente (i.codigo_paciente)) As sexo_paciente11," +
																		" getfechanacimientopaciente(i.codigo_paciente) As fecha_nacimiento12," +
																		" i.transplante As transplante13 " +
																	" FROM ingresos i" +
																	" INNER JOIN cuentas c on(c.id_ingreso=i.id) " ;

	private static final String strCadenaConsultaTrasladoSolicitudes=" SELECT " +
																				" getconsecutivosolicitud(dtst.solicitud_trasladada) As solicitud_trasladada0," +
																				" getconsecutivosolicitud(dtst.solicitud_generada) As solicitud_generada1," +
																				" to_char(dtst.fecha_traslado,'dd/mm/yyyy') As fecha_traslado2," +
																				" dtst.hora_traslado As hora_traslado3 " +
																			" FROM det_tras_sol_transplante dtst " +
																			" WHERE dtst.id_traslado=?";
	
	/*----------------------------------------------------------------------------------------
	 *                              FIN TRASLADO SOLICITUDES POR TRANSPLANTE
	 -----------------------------------------------------------------------------------------*/
	

	/*----------------------------------------------------------------------------------------
	 *                           				METODOS
	 -----------------------------------------------------------------------------------------*/
	
	/**
	 * Metodo  encargado de consultar del detalle de los traslados de solicitudes
	 * @param connection
	 * @param idTraslado
	 * @return mapa
	 * -------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -------------------------
	 * -- solicitudTrasladada0_
	 * -- solicitudGenerada1
	 * -- fechaTraslado2_
	 * -- horaTraslado3_
	 */
	public static HashMap consultarTrasladoSolicitudes (Connection connection,String idTraslado)
	{
		logger.info("\n entro a consultarTrasladoSolicitudes -->"+idTraslado);
		String cadena = strCadenaConsultaTrasladoSolicitudes;
		
		
		logger.info("\n cadena -->"+cadena);
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));	
			//id traslado
			ps.setDouble(1, Utilidades.convertirADouble(idTraslado));
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			ps.close();
			return mapaRetorno;
		}
		catch (Exception e) 
		{
			logger.info("\n problema consultando el detalle de traslados de ingresos "+e);
		}
		return null;
	}
	
	/**
	 * Metodo encargado de consultar los traslados de ingrespos a un paciente
	 * @param connection
	 * @param institucion
	 * @param tipoTransplante
	 * @param ingresoBuscar
	 * @return mapa
	 * -------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -------------------------
	 * -- consecutivo0_
	 * -- valorCampoSub1_
	 */
	public static HashMap consultarTraslado (Connection connection,String institucion,String tipoTransplante, String ingresoBuscar)
	{
		logger.info("\n entro a consultarTraslado -->"+institucion+" tipoTransplante -->"+tipoTransplante);
		String cadena =  "SELECT tst.consecutivo As consecutivo0," ;
		String where = " WHERE tst.institucion="+institucion;
		
		if (tipoTransplante.equals(ConstantesIntegridadDominio.acronimoIndicativoDonante))
			cadena+=" getnombrepersona(getpacientexingreso(tst.receptor)) As valor_campo_sub1 ";
		else
			cadena+=" getnombrepersona(getpacientexingreso(tst.donante)) As valor_campo_sub1 ";
		
		cadena+=" FROM  tras_sol_transplante tst  "; 
		
		if (tipoTransplante.equals(ConstantesIntegridadDominio.acronimoIndicativoDonante))
			where+=" AND tst.donante="+ingresoBuscar;
		else
			where+=" AND tst.receptor="+ingresoBuscar;
		
		cadena+=where;
		logger.info("\n cadena -->"+cadena);
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));	
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			ps.close();
			return mapaRetorno;
		}
		catch (Exception e) 
		{
			logger.info("\n problema consultando los traslados de solicitudes "+e);
		}
		return null;
	}
	
	
	/**
	 * Metodo encargado de consultar los ingrsos por transplantes
	 * @param connection
	 * @param criterios
	 * -----------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * -----------------------------
	 * -- centAten0
	 * -- viaIngreso1
	 * -- estIngreso2
	 * -- tipoTransp3
	 * -- fechaIniEgre4
	 * -- fechaFinEgre5
	 * -- institucion6
	 * @return mapa
	 * -------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -------------------------------
	 * centroAtencion0_,nomCentroCosto1_,
	 * idIngreso2_,consecutivo3_,
	 * fechaIngreso4_,viaIngreso5_,
	 * nomViaIngreso6_,transplante7_,
	 * paciente8_,nomPersona9_,
	 * identPaciente10_,sexoPaciente11_,
	 * fechaNacimiento12_
	 */
	public static HashMap consultarIngresosTransplante (Connection connection, HashMap criterios)
	{
		logger.info("entro a consultarIngresosTransplante --> "+criterios);
		String cadena=strCadenaConsultaIngresosTransplante;
		String where=" WHERE i.institucion=? AND c.estado_cuenta IN ('"+ConstantesBD.codigoEstadoCuentaActiva+"','"+ConstantesBD.codigoEstadoCuentaFacturadaParcial+"','"+ConstantesBD.codigoEstadoCuentaAsociada+"') ";
			
		//centro atencion --Opcional
		if (UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[0])+""))
			where+=" AND i.centro_atencion="+criterios.get(indicesCriterios[0])+"";
			
		//via ingreso --Opcional
		if (UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[1])+""))
			where+=" AND c.via_ingreso="+criterios.get(indicesCriterios[1])+"";
		else
			where+=" AND c.via_ingreso IN ('"+ConstantesBD.codigoViaIngresoHospitalizacion+"','"+ConstantesBD.codigoViaIngresoAmbulatorios+"')";
		
		//Estado del ingreso --Opcional
		if (UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[2])+""))
			where+=" AND i.estado='"+criterios.get(indicesCriterios[2])+"'";
		else
			where+=" AND i.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"','"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"')";
			
		//Tipo de transplante -- Requerido
		if (UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[3])+""))
			where+=" AND i.transplante='"+criterios.get(indicesCriterios[3])+"'";
		else
			where+=" AND i.transplante IN ('"+ConstantesIntegridadDominio.acronimoIndicativoDonante+"','"+ConstantesIntegridadDominio.acronimoIndicativoReceptor+"')";
		
		//		Valida por un rango de fechas
		if (UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[4])+"") && UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[5])+"") )
			where+=" AND i.fecha_ingreso BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesCriterios[4])+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesCriterios[5])+"")+"'" ;
		
		
		cadena+=where+" ORDER BY i.fecha_ingreso ";
		
		logger.info("\n cadena --> "+cadena);
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//institucion
			ps.setInt(1, Utilidades.convertirAEntero(criterios.get(indicesCriterios[6])+""));
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			ps.close();
			return mapaRetorno;
			
		}
		catch (SQLException e) 
		{
			logger.info("\n problema consultando los ingresos de transplantes "+e);
		}
		
		return null;
	}
	
	
	
	
	
	/*----------------------------------------------------------------------------------------
	 *                           			 FIN METODOS
	 -----------------------------------------------------------------------------------------*/

}