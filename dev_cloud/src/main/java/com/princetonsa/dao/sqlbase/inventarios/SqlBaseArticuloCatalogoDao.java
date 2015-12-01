package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class SqlBaseArticuloCatalogoDao {
	
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	public static Logger logger=Logger.getLogger(SqlBaseArticuloCatalogoDao.class);
	
	/**
	 * Cadena para la busqueda
	 */
	private static final String cadenaBuscarStr="SELECT acronimo as acronimo, proveedor as proveedor, descripcion as descripcion, "+
												"ref_proveedor as ref_proveedor, val_uni_compra as val_uni_compra, "+
												"val_uni_iva as val_uni_iva, fecha_ini_vigencia as fecha_ini_vigencia, "+
												"fecha_fin_vigencia as fecha_fin_vigencia, unidad_medida as unidad_medida, "+
												"clase_inventario as clase_inventario, grupo_inventario as grupo_inventario, "+
												"subgrupo_inventario as subgrupo_inventario, "+
												"naturaleza_articulo as naturaleza_articulo, coalesce(codigo_axioma||'','') as codigo_axioma "+
												"FROM catalogo_proveedor WHERE fecha_ini_vigencia<CURRENT_DATE ";
	
	/**
	 * Cadena consultar detalle de Articulo de Catalogo
	 */
	private static final String cadenaConsultarArticuloCatalogoStr="SELECT acronimo as acronimo, proveedor as proveedor, descripcion as descripcion, "+
																	"ref_proveedor as ref_proveedor, val_uni_compra as val_uni_compra, "+
																	"val_uni_iva as val_uni_iva, fecha_ini_vigencia as fecha_ini_vigencia, "+
																	"fecha_fin_vigencia as fecha_fin_vigencia, unidad_medida as unidad_medida, "+
																	"clase_inventario as clase_inventario, grupo_inventario as grupo_inventario, "+
																	"subgrupo_inventario as subgrupo_inventario, "+
																	"naturaleza_articulo as naturaleza_articulo, coalesce(codigo_axioma||'','') as codigo_axioma "+
																	"FROM catalogo_proveedor ";
	

	/**
	 * 
	 */
	private static final String cadenaConsultaEsquemasTarifarios="SELECT et.codigo as codigo,et.nombre,case when ti.articulo is null then '"+ConstantesBD.acronimoNo+"' else '"+ConstantesBD.acronimoSi+"' end as asignado from esquemas_tarifarios et left outer join tarifas_inventario ti on(et.codigo=ti.esquema_tarifario and ti.articulo=?) where es_inventario="+ValoresPorDefecto.getValorTrueParaConsultas();

	/**
	 * Insertar Articulo
	 */
	public static int insertar(Connection con, HashMap vo, String cadenaInsertarStr)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 
			 * maxima_cantidad_mes,
			 * multidosis,
			 * maneja_lotes,
			 * maneja_fecha_vencimiento,
			 * porcentaje_iva,
			 * precio_ultima_compra,
			 * precio_base_venta,
			 * fecha_precio_base_venta,
			 * institucion,
			 * codigo_interfaz,
			 * costo_donacion,
			 * indicativo_automatico,
			 * indicativo_por_completar,precio_compra_mas_alta) VALUES ('seq_articulo'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
			 */
			
			ps.setInt(1,Utilidades.convertirAEntero(vo.get("subgrupo")+""));
			ps.setString(2,vo.get("descripcion")+"");
			ps.setString(3,vo.get("naturaleza")+"");
			ps.setNull(4, Types.CHAR);
			ps.setNull(5, Types.VARCHAR);
			ps.setNull(6, Types.VARCHAR);
			ps.setString(7,vo.get("unidad_medida")+"");
			ps.setObject(8, null);
			ps.setDate(9,Date.valueOf(vo.get("fecha_creacion")+""));
			ps.setString(10,vo.get("usuario_creacion")+"");
			ps.setString(11,vo.get("hora_creacion")+"");
			ps.setInt(12, Utilidades.convertirAEntero(vo.get("categoria")+""));
			ps.setInt(13, Utilidades.convertirAEntero(vo.get("stock_minimo")+""));
			ps.setInt(14,Utilidades.convertirAEntero(vo.get("stock_maximo")+""));
			ps.setInt(15,Utilidades.convertirAEntero(vo.get("punto_pedido")+""));
			ps.setInt(16, Utilidades.convertirAEntero(vo.get("cantidad_compra")+""));
			ps.setDouble(17, Utilidades.convertirADouble(vo.get("costo_promedio")+""));
			ps.setNull(18, Types.VARCHAR);
			ps.setDouble(19, Utilidades.convertirADouble(vo.get("maxima_cantidad_mes")+""));
			ps.setString(20, vo.get("multidosis")+"");
			ps.setString(21, vo.get("maneja_lotes")+"");
			ps.setString(22, vo.get("maneja_fecha_vencimiento")+"");
			ps.setDouble(23, Utilidades.convertirADouble(vo.get("porcentaje_iva")+""));
			ps.setDouble(24, Utilidades.convertirADouble(vo.get("precio_ultima_compra")+""));
			ps.setDouble(25, Utilidades.convertirADouble(vo.get("precio_base_venta")+""));
			ps.setNull(26, Types.DATE);
			ps.setInt(27, Utilidades.convertirAEntero(vo.get("institucion")+""));
			ps.setNull(28, Types.CHAR);
			ps.setDouble(29, Utilidades.convertirADouble(vo.get("costo_donacion")+""));
			ps.setString(30, vo.get("indicativo_automatico")+"");
			ps.setString(31, vo.get("indicativo_por_completar")+"");
			ps.setDouble(32, Utilidades.convertirADouble(vo.get("precio_compra_mas_alta")+""));
			if(ps.executeUpdate()>0)
				{
					String cadena="select max(codigo) from articulo";
					ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
					if(rs.next())
					return rs.getInt(1);
				}
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Consultar Articulo por Catalogo
	 */
	public static Collection buscar(Connection con, String proveedor,String descripcion,String ref_proveedor, String codigo_axioma,int acronimo)
	{
		String cadena=cadenaBuscarStr;
		if(acronimo!=-1)
		{
			cadena+="AND acronimo="+acronimo;
		}
		if(!proveedor.equals(""))
		{
			cadena+="AND upper(proveedor) like upper('%"+proveedor+"%')";
		}
		if(!descripcion.equals(""))
		{
			cadena+=" AND UPPER(descripcion) LIKE UPPER('%"+descripcion+"%')";
		}
		if(!ref_proveedor.equals(""))
		{
			cadena+=" AND upper(ref_proveedor) like upper('%"+ref_proveedor+"%')";
		}
		if(!codigo_axioma.equals(""))
		{
			cadena+=" AND codigo_axioma="+codigo_axioma;
		}
		try
		{
			logger.info(cadena);
			PreparedStatementDecorator busqueda= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(busqueda.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error en la Búsqueda en el catalogo "+e);
			return null;
		}
	}
	
	/**
	 * Modificar catalogo_proveedor para actualizarle el codigo_axioma despues de creado el articulo
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarArticuloCatalogo(Connection con, int codigoArticulo,int acronimo)
	{
		String cadena="UPDATE catalogo_proveedor SET codigo_axioma="+codigoArticulo+" WHERE acronimo="+acronimo;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return ps.executeUpdate()>0;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Consultar articulo de catalogo
	 * @param con
	 * @param acronimo
	 * @return
	 */
	public static HashMap consultarArticuloCatalogo(Connection con, int acronimo)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		String cadena=cadenaConsultarArticuloCatalogoStr;
		try
		{
			cadena+="WHERE acronimo="+acronimo;
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,false);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @return
	 */
	public static HashMap consultarTarifasArticulos(Connection con, String codigoArticulo) 
	{
		
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaEsquemasTarifarios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(codigoArticulo));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean guardarEsquemasInventario(Connection con, HashMap vo,String cadena) 
	{
		for(int a=0;a<Utilidades.convertirAEntero(vo.get("numRegistros")+"");a++)
		{
			if((vo.get("asignado_"+a)+"").equals(ConstantesBD.acronimoSi))
			{
				try
				{
					PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					/**
					 * INSERT INTO tarifas_inventario (
					 * codigo,
					 * esquema_tarifario,
					 * articulo,
					 * valor_tarifa,
					 * porcentaje_iva,
					 * tipo_tarifa,
					 * porcentaje,
					 * actualiz_automatic,
					 * usuario_modifica,
					 * fecha_modifica,
					 * hora_modifica) VALUES('seq_tarifas_inventario'),?,?,?,?,?,?,?,?,?,?)
					 */
					
					ps.setInt(1,Utilidades.convertirAEntero(vo.get("codigo_"+a)+""));
					ps.setInt(2,Utilidades.convertirAEntero(vo.get("articulo")+""));
					ps.setDouble(3,Utilidades.convertirADouble(vo.get("valor_tarifa")+""));
					ps.setDouble(4,Utilidades.convertirADouble(vo.get("porcentaje_iva")+""));
					ps.setString(5,vo.get("tipo_tarifa")+"");
					ps.setDouble(6,Utilidades.convertirADouble(vo.get("porcentaje")+""));
					ps.setString(7,vo.get("actualiz_automatic")+"");
					ps.setString(8,vo.get("usuario_modifica")+"");
					ps.setDate(9,Date.valueOf(vo.get("fecha_modifica")+""));
					ps.setString(10,vo.get("hora_modifica")+"");
					ps.executeUpdate();
				}
				catch(SQLException e)
				{
					e.printStackTrace();
				}
			}
		}
		return true;
	}

}
