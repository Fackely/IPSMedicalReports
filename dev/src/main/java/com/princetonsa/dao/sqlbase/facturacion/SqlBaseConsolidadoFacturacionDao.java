package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;

/**
 * @author Mauricio Jllo
 * Fecha Junio de 2008
 */

public class SqlBaseConsolidadoFacturacionDao
{

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseConsolidadoFacturacionDao.class); 
	
	/**
	 * Cadena que consulta los consolidados de facturacion
	 */
	private static String strConConsolidadoFacturacion = "SELECT " +
															"f.convenio AS convenio, " +
															"getnombreconvenio(f.convenio) AS nomconvenio, " +
															"sc.contrato AS codcontrato, " +
															"getnumerocontrato(sc.contrato) AS numcontrato, " +
															"getnomconvcontrato(sc.contrato) AS contrato, " +
															"f.consecutivo_factura AS factura, " +
															"to_char(f.fecha, 'DD/MM/YYYY') AS fechafactura, " +
															"getnombreviaingreso(f.via_ingreso) AS viaingreso, " +
															"getnombrepersona(f.cod_paciente) AS paciente, " +
															"getidpaciente(f.cod_paciente) AS idpaciente, " +
															"getnombreestrato(f.estrato_social) AS estrato, " +
															"coalesce(sc.nro_carnet, '') AS ficha, " +
															"f.valor_convenio AS valorconvenio, " +
															"f.valor_bruto_pac AS valorpaciente, " +
															"f.valor_total AS valortotal " +
														"FROM " +
															"facturas f " +
															"INNER JOIN sub_cuentas sc ON(f.sub_cuenta = sc.sub_cuenta) " +
														"WHERE " +
															"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" ";
	
	/**
	 * Cadena para insertar el log de consolidado de facturacion
	 */
	private static String strInsLogConsolidadoFacturacion = "INSERT INTO facturacion.log_consolidado_facturacion VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	/**
	 * Metodo que ejecuta la consulta de consolidado
	 * de facturacion
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarConsolidadoFacturacion(Connection con, HashMap criterios, String orderGroupBy)
	{
		HashMap mapa = new HashMap<String, Object>();
		mapa.put("numRegistros","0");
		
		String consulta = strConConsolidadoFacturacion;
		
		//Filtramos la consulta por la factura inicial y la factura final. Como no es requerido se debe validar
		if(UtilidadCadena.noEsVacio(criterios.get("facturaInicial")+"") && UtilidadCadena.noEsVacio(criterios.get("facturaFinal")+""))
			consulta += "AND f.consecutivo_factura BETWEEN "+criterios.get("facturaInicial")+" AND "+criterios.get("facturaFinal")+" ";
		
		//Filtramos la consulta por la fecha inicial y la fecha final. Como no es requerido se debe validar
		if(UtilidadCadena.noEsVacio(criterios.get("fechaInicial")+"") && UtilidadCadena.noEsVacio(criterios.get("fechaFinal")+""))
			consulta += "AND f.fecha BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"' ";
		
		//Filtramos la consulta por la via de ingreso. Como no es requerido se debe validar
		if(UtilidadCadena.noEsVacio(criterios.get("viaIngreso")+""))
			consulta += "AND f.via_ingreso = "+criterios.get("viaIngreso")+" ";
		
		//Filtramos la consulta por el convenio. Como no es requerido se debe validar
		if(UtilidadCadena.noEsVacio(criterios.get("convenio")+""))
			consulta += "AND f.convenio = "+criterios.get("convenio")+" ";
		
		//Filtramos la consulta por el contrato. Como no es requerido se debe validar
		if(UtilidadCadena.noEsVacio(criterios.get("contrato")+""))
			consulta += "AND sc.contrato = "+criterios.get("contrato")+" ";
		
		consulta += orderGroupBy;
		
		try
        {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            logger.info("\n\n====>Consulta Consolidados de Facturacion: "+consulta);
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE CONSOLIDADOS DE FACTURACION");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
	}

	/**
	 * Metodo que inserta el log de consolidados de facturacion
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static void insertarLog(Connection con, HashMap criterios)
	{
		try
        {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsLogConsolidadoFacturacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        	
        	
        	/**
        	 *        Column       |          Type          | Modifiers
--------------------+------------------------+-----------
 codigo             | integer                | not null
 fecha_grabacion    | date                   | not null
 hora_grabacion     | character varying(5)   | not null
 usuario            | character varying(30)  | not null
 nombre_reporte     | character varying(30)  | not null
 tipo_salida        | character varying(30)  | not null
 institucion        | integer                | not null
 factura_inicial    | integer                |
 factura_final      | integer                |
 fecha_inicial      | date                   |
 fecha_final        | date                   |
 convenio           | integer                |
 contrato           | integer                |
 via_ingreso        | integer                |
 ruta_archivo_plano | character varying(512) |

        	 */
        	
        	ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_log_consolidado_facturacio"));
        	ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
            ps.setString(3, UtilidadFecha.getHoraActual(con));
            ps.setString(4, criterios.get("usuario").toString());
            ps.setString(5, "Consolidado de Facturacion");
            ps.setString(6, criterios.get("tipoSalida").toString());
            ps.setInt(7, Utilidades.convertirAEntero(criterios.get("institucion").toString()));
            
            if(!criterios.get("facturaInicial").toString().equals(""))
				ps.setInt(8, Utilidades.convertirAEntero(criterios.get("facturaInicial").toString()));
			else
				ps.setNull(8, Types.INTEGER);
            
            if(!criterios.get("facturaFinal").toString().equals(""))
				ps.setInt(9, Utilidades.convertirAEntero(criterios.get("facturaFinal").toString()));
			else
				ps.setNull(9, Types.INTEGER);
            
            if(!criterios.get("fechaInicial").toString().equals(""))
				ps.setDate(10, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial").toString())));
			else
				ps.setNull(10, Types.DATE);
            
            if(!criterios.get("fechaFinal").toString().equals(""))
				ps.setDate(11, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal").toString())));
			else
				ps.setNull(11, Types.DATE);
            
            if(!criterios.get("convenio").toString().equals(""))
				ps.setInt(12, Utilidades.convertirAEntero(criterios.get("convenio").toString()));
			else
				ps.setNull(12, Types.INTEGER);
            
            if(!criterios.get("contrato").toString().equals(""))
				ps.setInt(13, Utilidades.convertirAEntero(criterios.get("contrato").toString()));
			else
				ps.setNull(13, Types.INTEGER);
            
            if(!criterios.get("viaIngreso").toString().equals(""))
				ps.setInt(14, Utilidades.convertirAEntero(criterios.get("viaIngreso").toString()));
			else
				ps.setNull(14, Types.INTEGER);
            
            if(!criterios.get("ruta").toString().equals(""))
				ps.setString(15, criterios.get("ruta").toString());
			else
				ps.setNull(15, Types.VARCHAR);
            
            if(ps.executeUpdate()>0)
            	logger.info("=====>SE INSERTO CORRECTAMENTE EL LOG DE CONSOLIDADO DE FACTURACION");
            
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA INSERCION EN EL LOG DE CONSOLIDADO DE FACTURACION");
            e.printStackTrace();
        }
	}

	public static String obtenerCondiciones(String facturaInicial,
			String facturaFinal, String fechaInicial, String fechaFinal,
			String viaIngreso, String convenio, String contrato,
			String orderYgroup) {
		String condiciones = "f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" ";
	        
        //Filtramos la consulta por la factura inicial y la factura final. Como no es requerido se debe validar
		if(UtilidadCadena.noEsVacio(facturaInicial) && UtilidadCadena.noEsVacio(facturaFinal))
			condiciones += "AND f.consecutivo_factura BETWEEN "+facturaInicial+" AND "+facturaFinal+" ";
		
		//Filtramos la consulta por la fecha inicial y la fecha final. Como no es requerido se debe validar
		if(UtilidadCadena.noEsVacio(fechaInicial) && UtilidadCadena.noEsVacio(fechaFinal))
			condiciones += "AND to_char(f.fecha, 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"' ";
		
		//Filtramos la consulta por la via de ingreso. Como no es requerido se debe validar
		if(UtilidadCadena.noEsVacio(viaIngreso))
			condiciones += "AND f.via_ingreso = "+viaIngreso+" ";
		
		//Filtramos la consulta por el convenio. Como no es requerido se debe validar
		if(UtilidadCadena.noEsVacio(convenio))
			condiciones += "AND f.convenio = "+convenio+" ";
		
		//Filtramos la consulta por el contrato. Como no es requerido se debe validar
		if(UtilidadCadena.noEsVacio(contrato))
			condiciones += "AND sc.contrato = "+contrato+" ";
		
		condiciones+=orderYgroup;	
		return condiciones;
	}
	
}