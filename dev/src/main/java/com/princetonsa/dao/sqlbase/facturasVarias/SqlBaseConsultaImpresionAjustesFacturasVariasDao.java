package com.princetonsa.dao.sqlbase.facturasVarias;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.facturasVarias.ConsultaImpresionAjustesFacturasVarias;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;


/**
 *
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com 
 */
public class SqlBaseConsultaImpresionAjustesFacturasVariasDao
{

	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
		public static Logger logger = Logger.getLogger(SqlBaseConsultaImpresionAjustesFacturasVariasDao.class);
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
		
		private static String [] indicesCriterios = ConsultaImpresionAjustesFacturasVarias.indicesCriteriosBusqueda;
		/*----------------------------------------------------------------------------------------
		 *           ATRIBUTOS PARA LA GENERACION MODIFICACION AJUSTES FACTURAS VARIAS
		 -----------------------------------------------------------------------------------------*/
		/**
		 * String encargado de consultar los datos de ajustes de facturas
		 * varias.
		 */
		private static final String strCadenaConsultaAjustesFacVarias = " SELECT " +
																					" afv.codigo As codigo0," +
																					" afv.consecutivo As consecutivo1," +
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
																					"END AS deudor12, " +
																					" getnombreconceptoajuste(afv.concepto_ajuste) As nom_concepto13," +
																					" fv.consecutivo As cosec_fac14," +
																					" afv.motivo_aprob_anul As motivo_anula15, " +
																					" CASE WHEN afv.estado='"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"' THEN getnombreusuario(afv.usuario_aprob_anul) ELSE '' END As usuario_anula16," +
																					" CASE WHEN afv.estado='"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"' THEN to_char(afv.fecha_aprob_anul,'DD/MM/YYYY') ELSE '' END As fecha_anula17, " +
																					" CASE WHEN afv.estado='"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"' THEN getnombreusuario(afv.usuario_aprob_anul) ELSE '' END As usuario_aprueba18," +
																					" CASE WHEN afv.estado='"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"' THEN to_char(afv.fecha_aprob_anul,'DD/MM/YYYY') ELSE '' END As fecha_aprueba19 " +
																		" FROM ajus_facturas_varias afv " +
																		" INNER JOIN facturas_varias fv ON (fv.codigo_fac_var=afv.factura)" +
																		"INNER JOIN deudores d ON (d.codigo = fv.deudor)";
																		
		

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
			 
			 
			 cadena+=obtenerWhere(criterios, ajusTodos);
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
	 * Metodo encargado de generar las clausulas where
	 * de la ocnsulta.
	 * @param criterios
	 * @param ajusTodos
	 * @return
	 */
	public static String obtenerWhere (HashMap criterios, boolean ajusTodos)
	{
			 String where =" WHERE afv.institucion="+criterios.get(indicesCriterios[4]);
			 String order="";
			 //Validamos si la busqueda se llamo desde la aprobaci�n/anulaci�n de ajustes facturas varias para solo invocar los ajustes pendientes
			 if(!ajusTodos)
				 where += " AND afv.estado = '"+ConstantesIntegridadDominio.acronimoEstadoPendiente+"' ";
			 else
				 if (UtilidadCadena.noEsVacio(criterios.get(indicesCriterios[12])+"") && !(criterios.get(indicesCriterios[12])+"").equals(ConstantesBD.codigoNuncaValido+""))
					 where += " AND afv.estado = '"+criterios.get(indicesCriterios[12])+"' ";
					 
			 //tipo de deudor
			logger.info("tipo deudor....."+criterios.get(indicesCriterios[5]));
			 if (!(criterios.get(indicesCriterios[5])+"").equals("") && !(criterios.get(indicesCriterios[5])+"").equals("null"))
			 {
				 where +="  AND d.tipo='"+criterios.get(indicesCriterios[5])+"'";
				//deudor 
				if (!(criterios.get(indicesCriterios[6])+"").equals("") && !(criterios.get(indicesCriterios[6])+"").equals("null")&& !(criterios.get(indicesCriterios[6])+"").equals(ConstantesBD.codigoNuncaValido))
				{
					if ((criterios.get(indicesCriterios[5])+"").equals(ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa))
						where +="  AND d.CODIGO_EMPRESA="+criterios.get(indicesCriterios[6]);
					if ((criterios.get(indicesCriterios[5])+"").equals(ConstantesIntegridadDominio.acronimoTipoDeudorOtros))
						where +="  AND d.CODIGO_TERCERO="+criterios.get(indicesCriterios[6]);
					if ((criterios.get(indicesCriterios[5])+"").equals(ConstantesIntegridadDominio.acronimoPaciente))
						where +="  AND d.CODIGO_PACIENTE="+criterios.get(indicesCriterios[6]);
				}
			 }
		
				 
			 // ajuste
			 if (!(criterios.get(indicesCriterios[0])+"").equals("") && !(criterios.get(indicesCriterios[0])+"").equals("null") && !(criterios.get(indicesCriterios[0]).equals(ConstantesBD.codigoNuncaValido)))
				 where +="  AND afv.consecutivo="+criterios.get(indicesCriterios[0]);
			 
			 // factura
			 if (!(criterios.get(indicesCriterios[1])+"").equals("") && !(criterios.get(indicesCriterios[1])+"").equals("null") && !(criterios.get(indicesCriterios[1]).equals(ConstantesBD.codigoNuncaValido)))
			 	 where +="  AND fv.consecutivo="+criterios.get(indicesCriterios[1]);
			 
			 // Rango Entre Fechas (Fecha Inicial  - Fecha Final )
			 if (!(criterios.get(indicesCriterios[2])+"").equals("") && !(criterios.get(indicesCriterios[2])+"").equals("null") &&
				 !(criterios.get(indicesCriterios[3])+"").equals("") && !(criterios.get(indicesCriterios[3])+"").equals("null"))
				 where +="  AND afv.fecha_ajuste between '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesCriterios[2])+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesCriterios[3])+"")+"'";
			 
			 //codigo Pk de ajustes facturas varias 
			 if (!(criterios.get(indicesCriterios[8])+"").equals("") && !(criterios.get(indicesCriterios[8])+"").equals("null")&& !(criterios.get(indicesCriterios[8])+"").equals(ConstantesBD.codigoNuncaValido))
				 where += " AND afv.codigo="+criterios.get(indicesCriterios[8]);
			 
			 order=" ORDER BY afv.consecutivo";
			 
			 return where+order;
	}
		 
		 
}