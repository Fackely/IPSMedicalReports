package com.princetonsa.dao.sqlbase.facturasVarias;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.facturasVarias.GeneracionModificacionAjustesFacturasVarias;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;


/**
 *
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com 
 */
public class SqlBaseGeneracionModificacionAjustesFacturasVariasDao
{

	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
		public static Logger logger = Logger.getLogger(SqlBaseGeneracionModificacionAjustesFacturasVariasDao.class);
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
		

		/*----------------------------------------------------------------------------------------
		 *           ATRIBUTOS PARA LA GENERACION MODIFICACION AJUSTES FACTURAS VARIAS
		 -----------------------------------------------------------------------------------------*/
		/**
		 * String encargado de consultar los datos de ajustes de facturas
		 * varias.
		 */
		
		
		private static final String strCadenaConsultaAjustesFacVarias = " SELECT " +
																					" afv.codigo As codigo0," +
																					" afv.consecutivo || coalesce('- '||afv.anio_consecutivo,'') As consecutivo1," +
																					" afv.tipo_ajuste As tipo_ajuste2," +
																					" to_char(afv.fecha_ajuste,'DD/MM/YYYY') As fecha_ajuste3," +
																					" afv.factura As factura4," +
																					" afv.concepto_ajuste As concepto_ajuste5," +
																					" afv.valor_ajuste As valor_ajuste6," +
																					" afv.observaciones As observaciones7," +
																					" afv.estado As estado8," +
																					" '"+ConstantesBD.acronimoSi+"' As esta_bd9, " +
																					"CASE " +
																						"WHEN d.tipo ='"+ConstantesIntegridadDominio.acronimoPaciente+"' THEN getnombrepersona(d.codigo_paciente)||' - '|| getidentificacionpaciente(d.codigo_paciente) " +
																						"WHEN d.tipo='"+ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa+"' THEN getnombreempresa(d.codigo_empresa)||' - '|| getnitempresa(d.codigo_empresa)  " +
																						"WHEN d.tipo ='"+ConstantesIntegridadDominio.acronimoTipoDeudorOtros+"' THEN getdescripciontercero(d.codigo_tercero)||' - '|| getnittercero(d.codigo_tercero) " +
																						"WHEN d.tipo= '"+ConstantesIntegridadDominio.acronimoOtro+"' THEN  d.primer_nombre ||'  '|| d.primer_apellido || ' - '|| d.numero_identificacion " +
																					"END AS deudor12," +
																					" getnombreconceptoajuste(afv.concepto_ajuste) As nom_concepto13," +
																					" fv.consecutivo As cosec_fac14, " +
																					" case when  afv.estado='"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"' then afv.motivo_aprob_anul else '' end As motivo_anula15, " +
																					" case when  afv.estado='"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"' then afv.usuario_aprob_anul else '' end As usuario_anula16, " +
																					" case when  afv.estado='"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"' then to_char(afv.fecha_aprob_anul,'DD/MM/YYYY') else '' end As fecha_anula17, " +
																					" case when  afv.estado='"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"' then afv.usuario_aprob_anul else '' end As usuario_aprueba18, " +
																					" case when  afv.estado='"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"' then to_char(afv.fecha_aprob_anul,'DD/MM/YYYY') else '' end As fecha_aprueba19 " +
																					
																		" FROM ajus_facturas_varias afv " +
																		" INNER JOIN facturas_varias fv ON (fv.codigo_fac_var=afv.factura)"+
																		"INNER JOIN deudores d ON (d.codigo = fv.deudor) " ;
																		
		
		
		
		/**
		 * String encargado de ingresar los datos de ajustes facturas varias
		 */
		private static final String strInsertarAjusteFacVarias =" INSERT INTO ajus_facturas_varias (consecutivo,institucion,tipo_ajuste,fecha_ajuste," +
																									" factura, concepto_ajuste,valor_ajuste," +
																									" observaciones, estado, usuario_modifica,codigo,anio_consecutivo)" +
																						   " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
		

		private static final String strCadenaConsultaDatosFactura = " SELECT " +
																				" '' As codigo0," +
																				" fv.codigo_fac_var As codigo_fac_var1," +
																				" getintegridaddominio(fv.estado_factura) As nom_estado_factura2," +
																				" '' As fecha_ajuste3," +
																				" fv.valor_factura As factura4," +
																				" fv.estado_factura As estado_factura5," +
																				"CASE " +
																					"WHEN d.tipo ='"+ConstantesIntegridadDominio.acronimoPaciente+"' THEN getnombrepersona(d.codigo_paciente) " +
																					"WHEN d.tipo='"+ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa+"' THEN getnombreempresa(d.codigo_empresa) " +
																					"WHEN d.tipo ='"+ConstantesIntegridadDominio.acronimoTipoDeudorOtros+"' THEN getdescripciontercero(d.codigo_tercero) " +
																					"WHEN d.tipo= '"+ConstantesIntegridadDominio.acronimoOtro+"' THEN  d.primer_nombre ||'  '|| d.primer_apellido " +
																				"END AS nom_deudor6," +
																				" fv.deudor As deudor7," +
																				" '' As estado8," +
																				" fv.concepto As concepto9," +
																				" (fv.valor_factura+coalesce (fv.ajustes_debito,0))-(coalesce(fv.ajustes_credito,0)+coalesce(fv.valor_pagos,0))  As saldo_factura11," +
																				" to_char(fv.fecha,'DD/MM/YYYY') As fecha12, " +
																				" fv.consecutivo As consecutivo13," +
																				" getdescconceptofacvar(fv.concepto) As nom_concepto14," +
																				" to_char(fv.fecha_aprobacion,'DD/MM/YYYY') As fecha_aprobacion15," +
																				"CASE " +
																					"WHEN d.tipo ='"+ConstantesIntegridadDominio.acronimoPaciente+"' THEN getidentificacionpaciente(d.codigo_paciente)  " +
																					"WHEN d.tipo='"+ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa+"' THEN getnitempresa(d.codigo_empresa)  " +
																					"WHEN d.tipo ='"+ConstantesIntegridadDominio.acronimoTipoDeudorOtros+"' THEN getnittercero(d.codigo_tercero)  " +
																					"WHEN d.tipo= '"+ConstantesIntegridadDominio.acronimoOtro+"' THEN  d.numero_identificacion " +
																				"END  AS  num_ident16 " +
																	" FROM facturas_varias fv " +
																	"INNER JOIN deudores d ON (d.codigo = fv.deudor) "+
																	" WHERE estado_factura='"+ConstantesIntegridadDominio.acronimoEstadoFacturaAprobada+"'" ;
																	
		
		private static final String strCadenaActualizacionAjuste = " UPDATE  ajus_facturas_varias SET concepto_ajuste=?, " +
																									" fecha_ajuste=?, " +
																									" valor_ajuste=?, " +
																									" observaciones=?," +
																									" usuario_modifica=? " +
																						" WHERE  codigo=? " ;
																	
		
		
		//------------------INDICES PARA EL MANEJO DE LAS KEY'S DE LOS MAPAS ---------------------
		
		private static String [] indicesajustesFacturasVarias=GeneracionModificacionAjustesFacturasVarias.indicesAjustesFacturasVarias;
		
		private static String [] indicesCriterios = GeneracionModificacionAjustesFacturasVarias.indicesCriteriosBusqueda;
		
		private static String [] indicesFacturasVarias=GeneracionModificacionAjustesFacturasVarias.indicesFacturasVarias;
		
		//----------------------------------------------------------------------------------------
		
		
		
		/*----------------------------------------------------------------------------------------
		 *           FIN ATRIBUTOS PARA LA GENERACION MODIFICACION AJUSTES FACTURAS VARIAS
		 -----------------------------------------------------------------------------------------*/
		

		/*----------------------------------------------------------------------------------------
		 *              METODOS PARA LA GENERACION MODIFICACION AJUSTES FACTURAS VARIAS
		 -----------------------------------------------------------------------------------------*/
		
		
		/**
		 * Metodo encargado de modificar un ajuste.
		 * @param connection
		 * @param datos
		 * ---------------------------
		 * KEY'S DEL MAPA DATOS
		 * ---------------------------
		 * --  conceptoAjuste5_
		 * -- fechaAjuste3_
		 * -- valorAjuste6_
		 * -- observaciones7_
		 * -- codigo0_
		 * -- usuarioModifica11_
		 */
		public static boolean modificarAjuste (Connection connection, HashMap datos)
		{
			 logger.info("\n******* entre a modificarAjuste --> "+datos);
			 String cadena = strCadenaActualizacionAjuste;
			 try 
			 {
				 
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet)); 
				
				/**
				 * UPDATE  ajus_facturas_varias SET concepto_ajuste=?, " +
																	" fecha_ajuste=?, " +
																	" valor_ajuste=?, " +
																	" observaciones=?," +
																	" usuario_modifica=? " +
														" WHERE  codigo=? 
				 */
				
				//conceptoAjuste5_
				ps.setString(1, datos.get(indicesajustesFacturasVarias[5])+"");
				//fechaAjuste3_
				ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(datos.get(indicesajustesFacturasVarias[3])+"")));
				//valorAjuste6_
				ps.setDouble(3, Utilidades.convertirADouble(datos.get(indicesajustesFacturasVarias[6])+""));
				//observaciones7_
				ps.setString(4, datos.get(indicesajustesFacturasVarias[7])+"");
				//usuarioModifica11_
				ps.setString(5, datos.get(indicesajustesFacturasVarias[11])+"");
				//codigo0_
				ps.setDouble(6, Utilidades.convertirADouble(datos.get(indicesajustesFacturasVarias[0])+""));
				
				if (ps.executeUpdate()>0)
					return true;
				
			 } 
			 catch (SQLException e) 
			 {
				 
				logger.info(" \n problema modificando el ajuste de factiras varias "+e);
			 }
			 
			 return false;
		}
		
		
		/**
		 * Metodo encargado de consultar  las facturas varias
		 * los key's del mapa criterios son:
		 * ----------------------------------------
		 * --factura1
		 * --fechaIni2
		 * --fechaFin3
		 * --institucion4
		 * --tipoDeudor5
		 * --deudor6
		 * LOS KEYS DEL MAPA RESULTADO:
		 * ---------------------------------------
		 * consecutivo0_,codigoFacVar1_,valorFactura2_
		 * fecha3_,nomEstadoFactura4_,estadoFactura5_
		 * nomDeudor6_,deudor7_,nomConcepto8_,concepto9_
		 * saldoFactura10_
		 * 
		 */
		public  static HashMap consultaFacturasVarias (Connection connection,HashMap criterios)
		{
			 logger.info("\n******* entre a consultaFacturasVarias --> "+criterios);
			HashMap result = new HashMap ();
			
			String cadena = strCadenaConsultaDatosFactura;
			String where = " AND fv.institucion="+criterios.get(indicesCriterios[4]);
			
			// ajuste consecutivo
			if (UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[0])+"") && !(criterios.get(indicesCriterios[0])+"").equals(ConstantesBD.codigoNuncaValido))
			{
				cadena+= " INNER JOIN ajus_facturas_varias afv ON (afv.factura=fv.codigo_fac_var)";	
				where += " AND afv.consecutivo="+criterios.get(indicesCriterios[0]);
			}
				// factura varias consecutivo
			if (UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[1])+"")) 
				where += " AND fv.consecutivo="+criterios.get(indicesCriterios[1]);
				
			//tipo de deudor
			 if (UtilidadCadena.noEsVacio(criterios.get("tipoDeudor")+""))
				 where +="  AND d.tipo='"+criterios.get("tipoDeudor")+"'";
			
			//deudor
			if (UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[6])+"") && !(criterios.get(indicesCriterios[6])+"").equals(ConstantesBD.codigoNuncaValido+"")) 
				where += " AND fv.deudor="+criterios.get(indicesCriterios[6]);
			
			 // Rango Entre Fechas (Fecha Inicial  - Fecha Final )
			 if (UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[2])+"")  && UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[3])+""))
				  where +="  AND fv.fecha between '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesCriterios[2])+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesCriterios[3])+"")+"'";

			 //codigo Pk de facturas varias 
			 if (UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[7])+"") && !(criterios.get(indicesCriterios[7])+"").equals(ConstantesBD.codigoNuncaValido+""))
			 	 where += " AND fv.codigo_fac_var="+criterios.get(indicesCriterios[7]);
			 
			 cadena +=where;
			logger.info("\n cadena --> "+cadena);
			try 
			{
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						
				result=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
				
			}
			catch (SQLException e) 
			{
				logger.info("\n problema consultando los datos de facturas varias "+e);
			}
			
			
			return result;
		}
		
		
		
		/**
		 * Metodo encargado de consultar los ajustes de facturas varias
		 * Los keys del mapa criterios son:
		 * ---------------------------------
		 * --consecutivo0 --> Opcional
		 * --factura1 --> Opcional
		 * --fechaIni2--> Opcional
		 * --fechaFin3--> Opcional
		 * --institucion4--> Requerido
		 * ----------------------------------
		 * Los key's del mapa resultado
		 * ----------------------------------
		 * codigo0_,consecutivo1_,tipoAjuste2_,
		 * fechaAjuste3_,factura4_,conceptoAjuste5_,
		 * valorAjuste6_,observaciones7_,estado8_,estaBd9_
		 */
		 public static HashMap  consultaAjustesFacturasVarias (Connection connection,HashMap criterios, boolean ajusTodos)
		 {
			 logger.info("\n******* entre a consultaAjustesFacturasVarias --> "+criterios);
			 
			 HashMap result = new HashMap();
			 
			 String cadena = strCadenaConsultaAjustesFacVarias;
			 String where =" WHERE afv.institucion="+criterios.get(indicesCriterios[4]);
			 
			 //Validamos si la busqueda se llamo desde la aprobación/anulación de ajustes facturas varias para solo invocar los ajustes pendientes
			 if(!ajusTodos)
				 where += " AND afv.estado = '"+ConstantesIntegridadDominio.acronimoEstadoPendiente+"' ";
			 
			//deudor 
			if (UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[6])+"") && !(criterios.get(indicesCriterios[6])+"").equals(ConstantesBD.codigoNuncaValido+""))
				 where +="  AND fv.deudor="+criterios.get(indicesCriterios[6]);
				 
			 //tipo de deudor
			 if (UtilidadCadena.noEsVacio(criterios.get("tipoDeudor")+""))
				 where +="  AND d.tipo='"+criterios.get("tipoDeudor")+"'";
				 
			 // ajuste
			 if (UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[0])+"") && !(criterios.get(indicesCriterios[0])+"").equals(ConstantesBD.codigoNuncaValido+""))
				 where +="  AND afv.consecutivo="+criterios.get(indicesCriterios[0]);
			 
			 // factura
			 if (UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[1])+"") && !(criterios.get(indicesCriterios[1])+"").equals(ConstantesBD.codigoNuncaValido+""))
			 	 where +="  AND fv.consecutivo="+criterios.get(indicesCriterios[1]);
			 
			 // Rango Entre Fechas (Fecha Inicial  - Fecha Final )
			 if (UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[2])+"")  && UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[3])+""))				 
				 where +="  AND afv.fecha_ajuste between '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesCriterios[2])+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesCriterios[3])+"")+"'";
			 
			 //codigo Pk de ajustes facturas varias 
			 if (UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[8])+"") && !(criterios.get(indicesCriterios[8])+"").equals(ConstantesBD.codigoNuncaValido+""))
				 where += " AND afv.codigo="+criterios.get(indicesCriterios[8]);
			 
			 //where+=" order by afv.consecutivo desc ";
			  cadena+=where+" order by 2";

			 try 
			 {
				 logger.info("\n cadena --> "+cadena);
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				result=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
				
			 } 
			 catch (SQLException e) 
			 {
				 logger.info("\n problema consultando los ajustes de facturas varias "+e);
			 }
			 
			 			 
			 return result;
		 }
		
		 
		 
		 /**
		  * Metodo encargado de ingresar los datos de ajustes
		  * facturas varias
		  * @param connection
		  * @param datos
		  * -------------------------------
		  * Key's del mapa datos
		  * -------------------------------
		  * --consecutivo1_ --> Requerido
		  * --institucion10_ --> Requerido
		  * --tipoAjuste2_ --> Requerido
		  * --fechaAjuste3_ --> Requerido
		  * --factura4_ --> Requerido
		  * --conceptoAjuste5_ --> Requerido
		  * --valorAjuste6_ --> Requerido
		  * --observaciones7_ --> Opcional
		  * --estado8_ --> Requerido
		  * --usuarioModifica11_ --> Requerido
		  * @return
		  */
		 public static int insertarAjustesFacturasVarias (Connection connection,HashMap datos)
		 {
			 logger.info("\n ****** ENTRE A insertarAjustesFacturasVarias CON DATOS --> "+datos);
			 
			 String cadena = strInsertarAjusteFacVarias;
			
			 try 
			 {
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				//codigo
				int codigo = UtilidadBD.obtenerSiguienteValorSecuencia(connection, "seq_ajus_facturas_varias");
				
				/**
				 * INSERT INTO ajus_facturas_varias (
				 * consecutivo,
				 * institucion,
				 * tipo_ajuste,
				 * fecha_ajuste,
				 * factura, 
				 * concepto_ajuste,
				 * valor_ajuste,
				 * observaciones, 
				 * estado, 
				 * usuario_modifica,
				 * codigo,
				 * anio_consecutivo) VALUES (?,?,?,?,?,?,?,?,?,?,?)
				 */
				
				
				//consecutivo1_
				ps.setDouble(1, Utilidades.convertirADouble(datos.get(indicesajustesFacturasVarias[1])+""));
				//institucion10_
				ps.setInt(2, Utilidades.convertirAEntero(datos.get(indicesajustesFacturasVarias[10])+""));
				//tipoAjuste2_
				ps.setString(3, datos.get(indicesajustesFacturasVarias[2])+"");
				//fechaAjuste3_
				ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(datos.get(indicesajustesFacturasVarias[3])+"")));
				//factura4_
				ps.setDouble(5, Utilidades.convertirADouble(datos.get(indicesajustesFacturasVarias[4])+""));
				//conceptoAjuste5_
				ps.setString(6, datos.get(indicesajustesFacturasVarias[5])+"");
				//valorAjuste6_
				ps.setDouble(7, Utilidades.convertirADouble(datos.get(indicesajustesFacturasVarias[6])+""));
				//observaciones7_
				if (!(datos.get(indicesajustesFacturasVarias[7])+"").equals("") && !(datos.get(indicesajustesFacturasVarias[7])+"").equals("null"))
					ps.setString(8, datos.get(indicesajustesFacturasVarias[7])+"");
				else
					ps.setNull(8,Types.VARCHAR);
				//estado8_
				ps.setString(9, datos.get(indicesajustesFacturasVarias[8])+"");
				//usuarioModifica11_
				ps.setString(10, datos.get(indicesajustesFacturasVarias[11])+"");
				//codigo
				ps.setDouble(11, Utilidades.convertirADouble(codigo+""));
				//anio consecutivo
				if (UtilidadCadena.noEsVacio(datos.get(indicesajustesFacturasVarias[15])+"") && !(datos.get(indicesajustesFacturasVarias[15])+"").equals(ConstantesBD.codigoNuncaValido+""))
					ps.setString(12, datos.get(indicesajustesFacturasVarias[15])+"");
				else
					ps.setNull(12, Types.VARCHAR);
				
				if(ps.executeUpdate()>0)
					return codigo;
					
				
			 } 
			 catch (SQLException e) 
			 {
				 logger.info("\n problema ingresando los datos de ajustes de facturas varias "+e);
			 }
			 
			 
			 return ConstantesBD.codigoNuncaValido;
		 }
		 
		 
		 
		/*----------------------------------------------------------------------------------------
		 *              FIN METODOS PARA LA GENERACION MODIFICACION AJUSTES FACTURAS VARIAS
		 -----------------------------------------------------------------------------------------*/
		
}