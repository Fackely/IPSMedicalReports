package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

/**
 * @author Mauricio Jaramillo
 * Fecha: Agosto de 2008
 */

public class SqlBasePacientesPorFacturarDao
{

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBasePacientesPorFacturarDao.class); 
	
	/**
	 * Cadena principal del SELECT del union de los consumos por facturar de pacientes
	 */
	private static String strConSelectUnionConsumosPorFacturar = "SELECT "+
																	"tabla.codconvenio AS codconvenio, "+
																	"tabla.convenio AS convenio, "+
																	"tabla.codviaingreso AS codviaingreso, "+
																	"tabla.nomviaingreso AS nomviaingreso, "+
																	"tabla.tipopaciente AS tipopaciente, "+
																	"tabla.ingreso AS ingreso, "+
																	"tabla.fechaingreso AS fechaingreso, "+
																	"tabla.fechaegreso AS fechaegreso, "+
																	"tabla.idpaciente AS idpaciente, "+
																	"tabla.nombrepaciente AS nombrepaciente, "+
																	"sum(tabla.valor) AS valor, "+
																	"tabla.usuario AS usuario "+
																"FROM ";
	
	/**
	 * Cadena que SELECT para los consumos por facturar de pacientes
	 */
	private static String strConSelectConsumosPorFacturar  =  "SELECT "+
																	"con.codigo AS codconvenio, "+
																	"con.nombre AS convenio, "+
																	"cu.via_ingreso AS codviaingreso, "+
																	"getnombreviaingreso(cu.via_ingreso) AS nomviaingreso, "+
																	"getnombretipopaciente(cu.tipo_paciente) AS tipopaciente, "+
																	"i.consecutivo AS ingreso, "+
																	"to_char(i.fecha_ingreso, 'DD/MM/YYYY') AS fechaingreso, "+
																	"coalesce(to_char(i.fecha_egreso, 'DD/MM/YYYY'), '') AS fechaegreso, "+
																	"getidpaciente(i.codigo_paciente) AS idpaciente, "+
																	"getnombrepersona(i.codigo_paciente) AS nombrepaciente, "+
																	"coalesce(sum(dc.valor_total_cargado), 0) AS valor, "+
																	"coalesce(i.usuario_modifica, '') AS usuario ";
	
	/**
	 * Cadena FROM 1 para los consumos por facturar de pacientes
	 */
	private static String strConFromConsumosPorFacturar1 = "FROM "+
																"det_cargos dc "+
																"INNER JOIN solicitudes s ON (dc.solicitud = s.numero_solicitud) "+
																"INNER JOIN cuentas cu ON (s.cuenta = cu.id) "+
																"INNER JOIN ingresos i ON (cu.id_ingreso = i.id) "+
																"INNER JOIN convenios con ON (dc.convenio = con.codigo) "+
															"WHERE "+
																"dc.facturado = '"+ConstantesBD.acronimoNo+"' "+
																//Modificado por la Tarea 38233 
																//"AND dc.tipo_solicitud IN ("+ConstantesBD.codigoTipoSolicitudMedicamentos+", "+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+", "+ConstantesBD.codigoTipoSolicitudCirugia+") "+
																"AND dc.servicio_cx IS NULL " +
																"AND dc.estado<>"+ConstantesBD.codigoEstadoFPendiente+" ";
	
	/**
	 * Cadena FROM 2 para los consumos por facturar de pacientes
	 */
	private static String strConFromConsumosPorFacturar2 = "FROM "+
																"det_cargos dc "+
																"INNER JOIN tipos_asocio ta ON (dc.tipo_asocio = ta.codigo) "+
																"INNER JOIN solicitudes s ON (dc.solicitud = s.numero_solicitud) "+
																"INNER JOIN solicitudes_cirugia sc ON (s.numero_solicitud = sc.numero_solicitud) "+
																"INNER JOIN cuentas cu ON (s.cuenta = cu.id) "+
																"INNER JOIN ingresos i ON (cu.id_ingreso = i.id) "+
																"INNER JOIN convenios con ON (dc.convenio = con.codigo) "+
															"WHERE "+
																"dc.facturado = '"+ConstantesBD.acronimoNo+"' "+
																"AND dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" ";
																
	/**
	 * Cadena GROUP BY para consumos por facturar de pacientes
	 */
	private static String strConGroupConsumosPorFacturar = "GROUP BY "+
																"con.codigo, con.nombre, cu.via_ingreso, i.consecutivo, i.fecha_ingreso, i.fecha_egreso, i.codigo_paciente, i.usuario_modifica, cu.tipo_paciente ";
	
	/**
	 * Cadena ORDER BY de la consulta de consumos por facturar de pacientes
	 */
	private static String strConOrderByConsumosPorFacturar = "GROUP BY " +
																"tabla.codconvenio, " +
																"tabla.convenio, " +
																"tabla.codviaingreso, " +
																"tabla.nomviaingreso, " +
																"tabla.tipopaciente, " +
																"tabla.ingreso, " +
																"tabla.fechaingreso, " +
																"tabla.fechaegreso, " +
																"tabla.idpaciente, " +
																"tabla.nombrepaciente, " +
																"tabla.usuario " +
															"ORDER BY " +
																"nomviaingreso, " +
																"convenio";
	
	/**
	 * Metodo que consulta los consumos por facturar de pacientes
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static HashMap consultarConsumosPorFacturar(Connection con, HashMap criterios)
	{
		HashMap mapa = new HashMap<String, Object>();
		mapa.put("numRegistros","0");
		String consulta = "", condiciones = "";
		
		//========== INICIO VALIDACIONES GENERALES CAMPOS SELECCIONADOS ===============
		//Filtramos por Centro de Atencion. Como es requerido no hay necesidad de validarlo
		condiciones += "AND i.centro_atencion = "+criterios.get("centroAtencion")+" ";
		//Filtramos por Fechas de Ingreso. Como es requerido no hay necesidad de validarlo
		condiciones += "AND i.fecha_ingreso BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"' ";
		//Filtramos por Tipo Convenio. 
		if(!criterios.get("tipoConvenio").toString().equals(""))
			condiciones += "AND con.tipo_convenio = '"+criterios.get("tipoConvenio")+"' ";
		//Filtramos por Tipo Egreso. Si es con egreso es True, sino se manda False, sino es ambos no se filtra por ello
		if(criterios.get("tipoEgreso").equals(ConstantesIntegridadDominio.acronimoIndicativoConEgreso))
			condiciones += "AND getexisteegreso(cu.id) ="+ValoresPorDefecto.getValorTrueParaConsultas()+" ";
		else if(criterios.get("tipoEgreso").equals(ConstantesIntegridadDominio.acronimoIndicativoSinEgreso))
			condiciones += "AND getexisteegreso(cu.id) ="+ValoresPorDefecto.getValorFalseParaConsultas()+" ";
		//Filtramos por la Vía de Ingreso. Como no es requerido se debe validar
		if(UtilidadCadena.noEsVacio(criterios.get("viaIngreso")+""))
			condiciones += "AND cu.via_ingreso = "+criterios.get("viaIngreso")+" ";
		//Filtramos por el Convenio. Como no es requerido se debe validar
		if(UtilidadCadena.noEsVacio(criterios.get("convenio")+""))
			condiciones += "AND con.codigo = "+criterios.get("convenio")+" ";
		//========== FIN VALIDACIONES GENERALES CAMPOS SELECCIONADOS =================
		
		//Armammos la consulta colocando declarando primero los campos del UNION
		consulta = strConSelectUnionConsumosPorFacturar;
		//Armamos la consulta colocando la primera parte del UNION
		consulta += "(("+strConSelectConsumosPorFacturar+strConFromConsumosPorFacturar1+condiciones+strConGroupConsumosPorFacturar+")";
		//Palabra UNION
		consulta += " UNION ";
		//Armamos la consulta colocando la segunda parte del UNION
		consulta += "("+strConSelectConsumosPorFacturar+strConFromConsumosPorFacturar2+condiciones+strConGroupConsumosPorFacturar+")) tabla ";
		//Añadimos la parte del Order By para poder hacer bien el rompimiento en archivo plano
		consulta += strConOrderByConsumosPorFacturar;
		
		try
        {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            logger.info("\n\n====>Consulta Consumos Por Facturar: "+consulta);
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE CONSUMOS DE PACIENTES POR FACTURAR");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
	}
	
	

}