package com.princetonsa.dao.oracle.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import util.UtilidadFecha;

import com.princetonsa.dao.inventarios.ConsultaMovimientosConsignacionesDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseConsultaMovimientosConsignacionesDao;

public class OracleConsultaMovimientosConsignacionesDao implements
		ConsultaMovimientosConsignacionesDao 

{

	
	/**
	 * 
	 */
	public HashMap consultarMovimientosConsignaciones(Connection con, String centroAtencion, String almacen, String proveedor, String fechaInicial, String fechaFinal, String tipoCodigo) 
	{
		String cadenaSql1="SELECT " +
							"t.numero_identificacion as \"nitproveedor\", " +
							"t.descripcion as \"descproveedor\", " +
							"a.codigo as  \"codigoarticulo\", " +
							"a.codigo_interfaz as \"codigointerfaz\", " +
							"a.descripcion as \"descarticulo\", " +
							"getunidadmedidaarticulo(a.codigo) as \"unidadmedida\", " +
							"sum(cantidadentrada) as \"cantidadentrada\", " +
							"sum(cantidadsalida) as \"cantidadsalida\", " +
							"a.costo_promedio as \"costopromedio\" " +
						"FROM " +
							"view_movimientos_inventarios vmi " +
						"INNER JOIN " +
							"tipos_trans_inventarios tti on (vmi.codigotransaccion=tti.codigo and tti.indicativo_consignacion='S') " +
						"INNER JOIN " +
							"centros_costo cc ON(cc.codigo=convertiranumero(vmi.almacen)) " +
						"INNER JOIN " +
							"articulo a ON(a.codigo=convertiranumero(vmi.articulo)) " +
						"INNER JOIN " +
							"terceros t on(t.numero_identificacion=vmi.proveedorcompra) " +
						"WHERE " +
							"vmi.proveedorcompra is not null and cc.centro_atencion="+centroAtencion+"";
		
		cadenaSql1+=" AND vmi.fechaelaboracion between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' and '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"'";
		
		if(!almacen.equals(""))
		cadenaSql1+=" AND vmi.almacen='"+almacen+"'";
		
		if(!proveedor.equals(""))
		cadenaSql1+=" AND vmi.proveedorcompra='"+proveedor+"'";
		
		cadenaSql1+="  group by t.numero_identificacion,t.descripcion,a.codigo,a.codigo_interfaz,a.descripcion,getunidadmedidaarticulo(a.codigo),a.costo_promedio";

		
		/*String cadenaSql2 = "SELECT "+
								"t.\"nitproveedor\","+
								"t.\"descproveedor\","+
								"t.\"codigoarticulo\","+
								"t.\"codigointerfaz\","+
								"t.\"descarticulo\","+
								"t.\"unidadmedida\","+
								"sum(t.\"cantidadentrada\"),"+
								"sum(t.\"cantidadsalida\"),"+
								"t.\"costopromedio\""+
							"FROM ("+cadenaSql1+") " +
							"GROUP BY " +
								"t.\"nitproveedor\","+
								"t.\"descproveedor\","+
								"t.\"codigoarticulo\","+
								"t.\"codigointerfaz\","+
								"t.\"descarticulo\","+
								"t.\"unidadmedida\","+
								"t.\"costopromedio\"";*/
		
		
		
		return SqlBaseConsultaMovimientosConsignacionesDao.consultarMovimientosConsignaciones(con, centroAtencion, almacen, proveedor, fechaInicial, fechaFinal, tipoCodigo, cadenaSql1);
	}

	
	/**
	 * 
	 */
	public String cambiarConsulta(Connection con, String fechaInicial, String fechaFinal, String almacen, String proveedor, String centroAtencion) 
	{
		return SqlBaseConsultaMovimientosConsignacionesDao.cambiarConsulta(con, fechaInicial, fechaFinal, almacen, proveedor, centroAtencion);
	}

	
}
