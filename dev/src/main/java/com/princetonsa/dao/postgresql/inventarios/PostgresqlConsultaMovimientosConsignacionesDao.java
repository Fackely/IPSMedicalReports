package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import util.UtilidadFecha;

import com.princetonsa.dao.inventarios.ConsultaMovimientosConsignacionesDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseConsultaMovimientosConsignacionesDao;

public class PostgresqlConsultaMovimientosConsignacionesDao implements
		ConsultaMovimientosConsignacionesDao 
		
{
	
	/**
	 * 
	 */
	public HashMap consultarMovimientosConsignaciones(Connection con, String centroAtencion, String almacen, String proveedor, String fechaInicial, String fechaFinal, String tipoCodigo) 
	{
		String cadenaSql="SELECT " +
							"t.numero_identificacion as nitproveedor, " +
							"t.descripcion as descproveedor, " +
							"a.codigo as  codigoarticulo, " +
							"a.codigo_interfaz as codigointerfaz, " +
							"a.descripcion as descarticulo, " +
							"getunidadmedidaarticulo(a.codigo) as unidadmedida, " +
							"sum(cantidadentrada::numeric) as cantidadentrada, " +
							"sum(cantidadsalida::numeric) as cantidadsalida, " +
							"a.costo_promedio as costopromedio " +
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
							"vmi.proveedorcompra is not null and cc.centro_atencion= "+centroAtencion+" ";
		
		cadenaSql+=" AND vmi.fechaelaboracion between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' and '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"'";
				
		if(!almacen.equals(""))
			cadenaSql+=" AND vmi.almacen='"+almacen+"'";
		
		if(!proveedor.equals(""))
			cadenaSql+=" AND vmi.proveedorcompra='"+proveedor+"'";
		
		cadenaSql+="  group by t.numero_identificacion,t.descripcion,a.codigo,a.codigo_interfaz,a.descripcion,unidadmedida,a.costo_promedio";
		
		return SqlBaseConsultaMovimientosConsignacionesDao.consultarMovimientosConsignaciones(con, centroAtencion, almacen, proveedor, fechaInicial, fechaFinal, tipoCodigo, cadenaSql);
	}

	
	/**
	 * 
	 */
	public String cambiarConsulta(Connection con, String fechaInicial, String fechaFinal, String almacen, String proveedor, String centroAtencion) 
	{
		return SqlBaseConsultaMovimientosConsignacionesDao.cambiarConsulta(con, fechaInicial, fechaFinal, almacen, proveedor, centroAtencion);
	}
	
	
}
