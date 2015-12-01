package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.ArticuloCatalogoDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseArticuloCatalogoDao;

public class PostgresqlArticuloCatalogoDao implements ArticuloCatalogoDao {

	/**
	 * cadena para la inserción en el Articulo
	 */
	private static final String cadenaInsertarStr="INSERT INTO articulo (codigo,subgrupo,descripcion,naturaleza,minsalud,forma_farmaceutica,concentracion,unidad_medida,estado,fecha_modifica,usuario_modifica,hora_modifica,categoria,stock_minimo,stock_maximo,punto_pedido,cantidad_compra,costo_promedio,registro_invima,maxima_cantidad_mes,multidosis,maneja_lotes,maneja_fecha_vencimiento,porcentaje_iva,precio_ultima_compra,precio_base_venta,fecha_precio_base_venta,institucion,codigo_interfaz,costo_donacion,indicativo_automatico,indicativo_por_completar,precio_compra_mas_alta) VALUES (nextval('seq_articulo'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	
	public Collection buscar(Connection con, String proveedor,
			String descripcion, String ref_proveedor,String codigo_axioma,int acronimo) {
		
		return SqlBaseArticuloCatalogoDao.buscar(con,proveedor,descripcion,ref_proveedor,codigo_axioma,acronimo);
	}

	public int insertar(Connection con, HashMap vo) {
		
		return SqlBaseArticuloCatalogoDao.insertar(con, vo, cadenaInsertarStr);
	}
	
	public HashMap consultarArticuloCatalogo(Connection con, int acronimo)
	{
		return SqlBaseArticuloCatalogoDao.consultarArticuloCatalogo(con,acronimo);
	}
	
	public boolean modificarArticuloCatalogo(Connection con, int codigoArticulo,int acronimo)
	{
		return SqlBaseArticuloCatalogoDao.modificarArticuloCatalogo(con, codigoArticulo,acronimo);
	}

	/**
	 * 
	 */
	public HashMap consultarTarifasArticulos(Connection con, String codigoArticulo) 
	{
		return SqlBaseArticuloCatalogoDao.consultarTarifasArticulos(con, codigoArticulo);
	}

	/**
	 * 
	 */
	public boolean guardarEsquemasInventario(Connection con, HashMap vo) 
	{
		String cadenaInsertarTarifaStr="INSERT INTO tarifas_inventario (codigo,esquema_tarifario,articulo,valor_tarifa,porcentaje_iva,tipo_tarifa,porcentaje,actualiz_automatic,usuario_modifica,fecha_modifica,hora_modifica) VALUES(nextval('seq_tarifas_inventario'),?,?,?,?,?,?,?,?,?,?)";
		return SqlBaseArticuloCatalogoDao.guardarEsquemasInventario(con, vo,cadenaInsertarTarifaStr);
	}

}
