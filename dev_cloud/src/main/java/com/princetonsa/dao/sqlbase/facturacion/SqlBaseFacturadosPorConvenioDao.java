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

public class SqlBaseFacturadosPorConvenioDao
{

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseFacturadosPorConvenioDao.class); 
	
	/**
	 * Cadena que consulta los consolidados de facturacion por Servicios
	 */
	private static String strConFacturadosPorConvenioGrupoServicio = "SELECT " +
																		"f.convenio AS convenio, " +
																		"getnombreconvenio(f.convenio) AS nomconvenio, " +
																		"getnumerocontrato(sc.contrato) AS numcontrato, " +
																		"sc.contrato AS contrato, " +
																		"getnomconvcontrato(sc.contrato) AS desccontrato, " +
																		"s.grupo_servicio AS codrompimiento, " +
																		"getnombregruposervicio(s.grupo_servicio) AS nomrompimiento, " +
																		"getnombreviaingreso(f.via_ingreso) AS viaingreso, " +
																		"sum(dfs.cantidad_cargo) AS cantidad, " +
																		"sum(dfs.valor_total) AS valortotal " +
																	"FROM " +
																		"facturas f " +
																		"INNER JOIN sub_cuentas sc ON(f.sub_cuenta = sc.sub_cuenta) " +
																		"INNER JOIN det_factura_solicitud dfs ON(f.codigo = dfs.factura) " +
																		"INNER JOIN servicios s ON(dfs.servicio = s.codigo) " +
																	"WHERE " +
																		"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" " +
																		"AND dfs.tipo_cargo = "+ConstantesBD.codigoTipoCargoServicios+" ";

	private static String strConFacturadosPorConvenioNaturalezaServicio = "SELECT " +
																				"f.convenio AS convenio, " +
																				"getnombreconvenio(f.convenio) AS nomconvenio, " +
																				"getnumerocontrato(sc.contrato) AS numcontrato, " +
																				"sc.contrato AS contrato, " +
																				"getnomconvcontrato(sc.contrato) AS desccontrato, " +
																				"s.naturaleza_servicio AS codrompimiento, " +
																				"getnaturalezaservicio(s.naturaleza_servicio) AS nomrompimiento, " +
																				"getnombreviaingreso(f.via_ingreso) AS viaingreso, " +
																				"sum(dfs.cantidad_cargo) AS cantidad, " +
																				"sum(dfs.valor_total) AS valortotal " +
																			"FROM " +
																				"facturas f " +
																				"INNER JOIN sub_cuentas sc ON(f.sub_cuenta = sc.sub_cuenta) " +
																				"INNER JOIN det_factura_solicitud dfs ON(f.codigo = dfs.factura) " +
																				"INNER JOIN servicios s ON(dfs.servicio = s.codigo) " +
																			"WHERE " +
																				"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" " +
																				"AND dfs.tipo_cargo = "+ConstantesBD.codigoTipoCargoServicios+" ";
	
	private static String strConFacturadosPorConvenioServicio = "SELECT " +
																	"f.convenio AS convenio, " +
																	"getnombreconvenio(f.convenio) AS nomconvenio, " +
																	"getnumerocontrato(sc.contrato) AS numcontrato, " +
																	"sc.contrato AS contrato, " +
																	"getnomconvcontrato(sc.contrato) AS desccontrato, " +
																	"dfs.servicio AS codrompimiento, "+
																	"CASE WHEN coalesce(getobtenercodigocupsserv(dfs.servicio, getTipoCodigoConvenio(f.convenio)), getobtenercodigocupsserv(dfs.servicio, "+ConstantesBD.codigoTarifarioCups+")) = '' THEN getobtenercodigocupsserv(dfs.servicio, "+ConstantesBD.codigoTarifarioCups+") ELSE coalesce(getobtenercodigocupsserv(dfs.servicio, getTipoCodigoConvenio(f.convenio)), getobtenercodigocupsserv(dfs.servicio, "+ConstantesBD.codigoTarifarioCups+")) END AS codigopropietario, "+
																	"CASE WHEN coalesce(getnombreservicio(dfs.servicio, getTipoCodigoConvenio(f.convenio)), getnombreservicio(dfs.servicio, "+ConstantesBD.codigoTarifarioCups+")) = '' THEN getnombreservicio(dfs.servicio, "+ConstantesBD.codigoTarifarioCups+") ELSE coalesce(getnombreservicio(dfs.servicio, getTipoCodigoConvenio(f.convenio)), getnombreservicio(dfs.servicio, "+ConstantesBD.codigoTarifarioCups+")) END AS nomrompimiento, "+
																	"getnombreviaingreso(f.via_ingreso) AS viaingreso, " +
																	"sum(dfs.cantidad_cargo) AS cantidad, " +
																	"sum(dfs.valor_total) AS valortotal " +
																"FROM " +
																	"facturas f " +
																	"INNER JOIN sub_cuentas sc ON(f.sub_cuenta = sc.sub_cuenta) " +
																	"INNER JOIN det_factura_solicitud dfs ON(f.codigo = dfs.factura) " +
																	"INNER JOIN servicios s ON(dfs.servicio = s.codigo) " +
																"WHERE " +
																	"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" " +
																	"AND dfs.tipo_cargo = "+ConstantesBD.codigoTipoCargoServicios+" ";
	
	/**
	 * Cadena que consulta los consolidados de facturacion por Articulos
	 */
	private static String strConFacturadosPorConvenioClaseArticulo = "SELECT " +
																		"f.convenio AS convenio, " +
																		"getnombreconvenio(f.convenio) AS nomconvenio, " +
																		"getnumerocontrato(sc.contrato) AS numcontrato, " +
																		"sc.contrato AS contrato, " +
																		"getnomconvcontrato(sc.contrato) AS desccontrato, " +
																		"si.clase AS codrompimiento, " +
																		"getnombreclaseinventario(si.clase) AS nomrompimiento, " +
																		"getnombreviaingreso(f.via_ingreso) AS viaingreso, " +
																		"sum(dfs.cantidad_cargo) AS cantidad, " +
																		"sum(dfs.valor_total) AS valortotal " +
																	"FROM " +
																		"facturas f " +
																		"INNER JOIN sub_cuentas sc ON(f.sub_cuenta = sc.sub_cuenta) " +
																		"INNER JOIN det_factura_solicitud dfs ON(f.codigo = dfs.factura) " +
																		"INNER JOIN articulo a ON(dfs.articulo = a.codigo) " +
																		"INNER JOIN subgrupo_inventario si ON(a.subgrupo = si.codigo) " +
																	"WHERE " +
																		"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" " +
																		"AND dfs.tipo_cargo = "+ConstantesBD.codigoTipoCargoArticulos+" ";
	
	private static String strConFacturadosPorConvenioNaturalezaArticulo = "SELECT " +
																				"f.convenio AS convenio, " +
																				"getnombreconvenio(f.convenio) AS nomconvenio, " +
																				"getnumerocontrato(sc.contrato) AS numcontrato, " +
																				"sc.contrato AS contrato, " +
																				"getnomconvcontrato(sc.contrato) AS desccontrato, " +
																				"a.naturaleza AS codrompimiento, " +
																				"getnomnaturalezaarticulo(a.naturaleza) AS nomrompimiento, " +
																				"getnombreviaingreso(f.via_ingreso) AS viaingreso, " +
																				"sum(dfs.cantidad_cargo) AS cantidad, " +
																				"sum(dfs.valor_total) AS valortotal " +
																			"FROM " +
																				"facturas f " +
																				"INNER JOIN sub_cuentas sc ON(f.sub_cuenta = sc.sub_cuenta) " +
																				"INNER JOIN det_factura_solicitud dfs ON(f.codigo = dfs.factura) " +
																				"INNER JOIN articulo a ON(dfs.articulo = a.codigo) " +
																			"WHERE " +
																				"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" " +
																				"AND dfs.tipo_cargo = "+ConstantesBD.codigoTipoCargoArticulos+" ";
	
	private static String strConFacturadosPorConvenioArticulo = "SELECT " +
																	"f.convenio AS convenio, " +
																	"getnombreconvenio(f.convenio) AS nomconvenio, " +
																	"getnumerocontrato(sc.contrato) AS numcontrato, " +
																	"sc.contrato AS contrato, " +
																	"getnomconvcontrato(sc.contrato) AS desccontrato, " +
																	"a.codigo AS codrompimiento, " +
																	"a.descripcion AS nomrompimiento, " +
																	"coalesce(a.concentracion, '') AS concentracion, " +
																	"coalesce(getnomformafarmaceutica(a.forma_farmaceutica), '') AS forma, " +
																	"getnombreviaingreso(f.via_ingreso) AS viaingreso, " +
																	"sum(dfs.cantidad_cargo) AS cantidad, " +
																	"sum(dfs.valor_total) AS valortotal " +
																"FROM " +
																	"facturas f " +
																	"INNER JOIN sub_cuentas sc ON(f.sub_cuenta = sc.sub_cuenta) " +
																	"INNER JOIN det_factura_solicitud dfs ON(f.codigo = dfs.factura) " +
																	"INNER JOIN articulo a ON(dfs.articulo = a.codigo) " +
																"WHERE " +
																	"f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" " +
																	"AND dfs.tipo_cargo = "+ConstantesBD.codigoTipoCargoArticulos+" ";
	
	/**
	 * Cadena para insertar el log de servicios/articulos facturados por convenio
	 */
	private static String strInsLogFacturadosXConvenio = "INSERT INTO facturacion.log_facturados_x_convenio VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	/**
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static HashMap consultarFacturadosPorConvenio(Connection con, HashMap criterios, boolean porSerArt)
	{
		HashMap mapa = new HashMap<String, Object>();
		mapa.put("numRegistros","0");
		String consulta = "", condiciones = "";
		
		//Validamos que consulta ejecutar segun lo seleccionado en la vista
		if(porSerArt)
		{
			//Por Grupo de Servicios			
			if(criterios.get("radioServicio").equals("1"))
				consulta = strConFacturadosPorConvenioGrupoServicio;
			//Por Naturaleza de Servicios			
			else if(criterios.get("radioServicio").equals("2"))
				consulta = strConFacturadosPorConvenioNaturalezaServicio;
			//Por Servicios			
			else if(criterios.get("radioServicio").equals("3"))
				consulta = strConFacturadosPorConvenioServicio;
		}
		else
		{
			//Por Clase de Inventarios			
			if(criterios.get("radioArticulo").equals("1"))
				consulta = strConFacturadosPorConvenioClaseArticulo;
			//Por Naturaleza de Articulos			
			else if(criterios.get("radioArticulo").equals("2"))
				consulta = strConFacturadosPorConvenioNaturalezaArticulo;
			//Por Articulos			
			else if(criterios.get("radioArticulo").equals("3"))
				consulta = strConFacturadosPorConvenioArticulo;
		}
		
		//Validaciones generales para cualquiera que fuera la consulta seleccionada o ejecutada
		condiciones += "AND f.fecha BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"' ";
		if(UtilidadCadena.noEsVacio(criterios.get("convenio")+""))
			condiciones += "AND f.convenio = "+criterios.get("convenio")+" ";
		if(UtilidadCadena.noEsVacio(criterios.get("contrato")+""))
			condiciones += "AND sc.contrato = "+criterios.get("contrato")+" ";
		
		if(UtilidadCadena.noEsVacio(consulta))
			consulta += condiciones;
		
		//Validamos la consulta para ingresar el group by y el order by respectivo
		if(porSerArt)
		{
			//Por Grupo de Servicios
			if(criterios.get("radioServicio").equals("1"))
				consulta += "GROUP BY f.convenio, sc.contrato, s.grupo_servicio, f.via_ingreso ORDER BY nomconvenio, numcontrato, nomrompimiento";
			//Por Naturaleza de Servicios
			else if(criterios.get("radioServicio").equals("2"))
				consulta += "GROUP BY f.convenio, sc.contrato, f.via_ingreso, s.naturaleza_servicio ORDER BY nomconvenio, numcontrato, nomrompimiento";
			//Por Servicios
			else if(criterios.get("radioServicio").equals("3"))
				consulta += "GROUP BY f.convenio, sc.contrato, f.via_ingreso, dfs.servicio ORDER BY nomconvenio, numcontrato, nomrompimiento";
		}
		else
		{
			//Por Clase de Articulo
			if(criterios.get("radioArticulo").equals("1"))
				consulta += "GROUP BY f.convenio, sc.contrato, si.clase, f.via_ingreso ORDER BY nomconvenio, numcontrato, nomrompimiento";
			//Por Naturaleza de Articulo
			else if(criterios.get("radioArticulo").equals("2"))
				consulta += "GROUP BY f.convenio, sc.contrato, a.naturaleza, f.via_ingreso ORDER BY nomconvenio, numcontrato, nomrompimiento";
			//Por Articulos
			else if(criterios.get("radioArticulo").equals("3"))
				consulta += "GROUP BY f.convenio, sc.contrato, a.codigo, a.descripcion, a.concentracion, a.forma_farmaceutica, f.via_ingreso ORDER BY nomconvenio, numcontrato, nomrompimiento";
		}
		
		//Validamos si la consulta viene vacia o no con la intencion de ejecutarla
		if(UtilidadCadena.noEsVacio(consulta))
		{
			try
	        {
	        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	            logger.info("\n\n====>Consulta de Servicios/Articulos Facturados por Convenio: "+consulta);
	            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
	        }
	        catch (SQLException e)
	        {
	            logger.error("ERROR EJECUNTANDO LA CONSULTA DE SERVICIOS/ARTICULOS FACTURADOS POR CONVENIO");
	            e.printStackTrace();
	        }
	        return (HashMap)mapa.clone();
		}
		else
			return mapa;
	       
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
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsLogFacturadosXConvenio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        	ps.setInt(1, Utilidades.convertirAEntero(UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_log_facturados_x_convenio")+""));
        	ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
            ps.setString(3, UtilidadFecha.getHoraActual(con));
            ps.setString(4, criterios.get("usuario")+"");
            ps.setString(5, "Facturados X Convenio");
            ps.setString(6, criterios.get("tipoSalida")+"");
            ps.setInt(7, Utilidades.convertirAEntero(criterios.get("institucion")+""));
            ps.setDate(8, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")));
			ps.setDate(9, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")));
			
            if(!criterios.get("convenio").toString().equals(""))
				ps.setInt(10, Utilidades.convertirAEntero(criterios.get("convenio")+""));
			else
				ps.setNull(10, Types.INTEGER);
            
            if(!criterios.get("contrato").toString().equals(""))
				ps.setInt(11, Utilidades.convertirAEntero(criterios.get("contrato")+""));
			else
				ps.setNull(11, Types.INTEGER);
            
            ps.setString(12, criterios.get("checkServicios")+"");
            ps.setString(13, criterios.get("checkArticulos")+"");
            
            if(!criterios.get("ruta").toString().equals(""))
				ps.setString(14, criterios.get("ruta")+"");
			else
				ps.setNull(14, Types.VARCHAR);
            
            if(ps.executeUpdate()>0)
            	logger.info("=====>SE INSERTO CORRECTAMENTE EL LOG DE SERVICIOS/ARTICULOS FACTURADOS POR CONVENIO");
            
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA INSERCION EN EL LOG DE SERVICIOS/ARTICULOS FACTURADOS POR CONVENIO");
            e.printStackTrace();
        }
	}	
	
}