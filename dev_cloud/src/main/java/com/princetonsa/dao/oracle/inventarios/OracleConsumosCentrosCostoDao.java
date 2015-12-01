package com.princetonsa.dao.oracle.inventarios;

import java.util.HashMap;

import com.princetonsa.dao.inventarios.ConsumosCentrosCostoDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseConsumosCentrosCostoDao;
import java.sql.Connection;

public class OracleConsumosCentrosCostoDao implements ConsumosCentrosCostoDao
{
	public String consultaPedidos ="SELECT " +
			"t.\"cc_solicitante\", " +
			"t.\"cc_solicitante_cod\", " +
			"t.\"codigopedido\", " +
			"t.\"fecha_p\", " +
			"t.\"hora_p\", " +
			"t.\"articulo\", " +
			"t.\"codigo_interfaz_articulo\", " +
			"t.\"descripcion\", " +
			"t.\"cantidad\", " +
			"t.\"costo_unitario\", " +
			"t.\"cc_solicitado\", " +
			"t.\"nom_clase\", " +
			"t.\"valor_total\" " +
		"FROM " +
		"(SELECT " +
			"getnomcentrocosto(p.centro_costo_solicitante) AS \"cc_solicitante\", " +
			"cc.codigo as \"cc_solicitante_cod\", " +
			"p.codigo AS \"codigopedido\", " +
			"p.fecha AS \"fecha_p\", " +
			"p.hora AS \"hora_p\", " +
			"dp.articulo AS \"articulo\", " +
			"va.codigo_interfaz AS \"codigo_interfaz_articulo\", " +
			"va.descripcion AS \"descripcion\", " +
			"dp.cantidad AS \"cantidad\", " +
			"ddp.costo_unitario AS \"costo_unitario\", " +
			//"cc.nombre || ' - ' || va.descripcionclase AS costo_clase, " +
			"cc.nombre as \"cc_solicitado\", " +
			"va.descripcionclase AS \"nom_clase\", " +
			"(dp.cantidad * ddp.costo_unitario) AS \"valor_total\" " +
		"FROM  " +
			"pedido p " +
		"INNER JOIN " +
			"centros_costo cc ON (p.centro_costo_solicitado = cc.codigo) " +
		"INNER JOIN " +
			"almacen_parametros ap ON (cc.codigo = ap.codigo) " +
		"INNER JOIN " +
			"detalle_pedidos dp ON (p.codigo = dp.pedido) " +
		"INNER JOIN " +
			"despacho_pedido dpp ON (p.codigo = dpp.pedido) " +
		"INNER JOIN " +
			"detalle_despacho_pedido ddp ON (p.codigo = ddp.pedido AND dp.articulo=ddp.articulo) " +
		"INNER JOIN " +
			"view_articulos va ON (va.codigo = dp.articulo) ";
	
	public String orderConsultaPedidos = "ORDER BY t.\"cc_solicitante\", t.\"cc_solicitante_cod\", t.\"cc_solicitado\"";
	
	public HashMap<String, Object> consultaCentroCosto (Connection con, int centroAtencion)
	{
		return SqlBaseConsumosCentrosCostoDao.consultaCentroCosto(con, centroAtencion);
	}
	
	public HashMap<String, Object> consultaClases (Connection con)
	{
		return SqlBaseConsumosCentrosCostoDao.consultaClases(con);
	}
	
	public HashMap<String, Object> consultaPedidos (Connection con, String fechaInicial, String fechaFinal, int centroAtencion, String almacen, String centroCosto, String clase)
	{
		return SqlBaseConsumosCentrosCostoDao.consultaPedidos(con, fechaInicial, fechaFinal, centroAtencion, almacen, centroCosto, clase, this.consultaPedidos, this.orderConsultaPedidos);
	}
}