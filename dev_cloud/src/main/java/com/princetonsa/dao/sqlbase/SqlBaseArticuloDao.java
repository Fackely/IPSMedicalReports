/*
 * Created on Apr 2, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.Articulo;

import util.Answer;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.inventarios.UtilidadInventarios;

/**
 * @author wrios
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SqlBaseArticuloDao
{
	
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseArticuloDao.class);
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar una cama desde la base de datos Genérica.
	 */
	private static final String cargarArticuloStr="SELECT " +
														"sub.clase AS clase, " +
														"sub.grupo AS grupo, " +
														"sub.subgrupo AS subgrupo, " +
														"a.naturaleza AS naturaleza, " +
														"a.forma_farmaceutica AS forma_farmaceutica, " +
														"a.unidad_medida AS unidad_medida, " +
														"a.concentracion AS concentracion, " +
														"a.estado AS estado, " +
														"a.minsalud AS minsalud, " +
														"a.descripcion AS descripcion, " +
														"a.codigo AS codigo," +
														"to_char(a.fecha_modifica, 'YYYY-MM-DD') as fecha," +
														"a.usuario_modifica as usuario," +
														"a.categoria as categoria," +
														"a.stock_minimo as stockminimo," +
														"a.stock_maximo as stockmaximo," +
														"a.punto_pedido as puntopedido," +
														"a.cantidad_compra as cantidadcompra," +
														"a.costo_promedio as costopromedio," +
														"a.registro_invima as registroinvima , " +
														"CASE WHEN maxima_cantidad_mes IS NULL THEN -1 ELSE maxima_cantidad_mes END as maxima_cantidad_mes , " +
														"multidosis as multidosis ,  " +
														"maneja_lotes as maneja_lotes ,  " +
														"maneja_fecha_vencimiento as maneja_fecha_vencimiento ,  " +
														"porcentaje_iva as porcentaje_iva ,  " +
														"CASE WHEN precio_ultima_compra IS NULL THEN -1 ELSE precio_ultima_compra END as precio_ultima_compra ,  " +
														"CASE WHEN precio_base_venta IS NULL THEN -1 ELSE precio_base_venta END as precio_base_venta , " +
														"CASE WHEN precio_compra_mas_alta IS NULL THEN -1 ELSE precio_compra_mas_alta END as precio_compra_mas_alta, " +
														"to_char(fecha_precio_base_venta, 'YYYY-MM-DD') as fecha_precio_base_venta , " +
														"(SELECT COUNT(1) FROM inventarios.articulo_almacen_x_lote b WHERE b.articulo=a.codigo ) as existe, " +
														"costo_donacion as costodonacion, " +
														"a.codigo_interfaz as codigointerfaz, " +
														"indicativo_automatico as indicativoautomatico, " +
														"indicativo_por_completar as indicativoporcompletar, " +
														"a.descripcion_alterna as descripcionalterna, " +
														"a.numero_expediente as numero_expediente, " +
														"a.cons_present_comercial as cons_present_comercial, " +
														"a.presentacion_comercial as presentacion_comercial, " +
														"a.clasificacion_atc as clasificacion_atc, " +
														"a.registro_art as registro_art, " +
														"a.vigencia as vigencia, " +
														"a.roles_x_producto as roles_x_producto, " +
														"a.titular as titular, " +
														"a.fabricante as fabricante, " +
														"a.importador as importador, " +
														"na.es_medicamento," +
														"a.atencion_odontologica AS atencionodontologica, " +
														"a.nivel_atencion AS nivelatencion " +
													"FROM " +
														"inventarios.articulo a " +
													"INNER JOIN " +
														"inventarios.subgrupo_inventario sub ON (sub.codigo=a.subgrupo) " +
													"INNER JOIN " +
														"inventarios.naturaleza_articulo na ON (na.acronimo = a.naturaleza AND na.institucion=a.institucion) " +
													"WHERE a.codigo= ?";	
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para cambiar el estado
	 * de una cama
	 */
	private static final String cambiarEstadoArticuloStr = "UPDATE articulo SET estado =? WHERE codigo =?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para modificar una cama en la base de datos Genérica.
	 */
	private static final String modificarArticuloStr="UPDATE articulo SET " +
															"descripcion=?, " +
															"naturaleza=?, " +
															"minsalud=?, " +
															"forma_farmaceutica=?, " +
															"unidad_medida=?, " +
															"concentracion=?, " +
															"estado=?," +
															"fecha_modifica=?," +
															"usuario_modifica=?," +
															"hora_modifica=?," +
															"stock_minimo=?," +
															"stock_maximo=?," +
															"punto_pedido=?," +
															"cantidad_compra=?," +
															"costo_promedio=?," +
															"categoria=?," +
															"registro_invima=?," +
															"maxima_cantidad_mes=?,  " +
															"multidosis=?,  " +
															"maneja_lotes=?,  " +
															"maneja_fecha_vencimiento=?,  " +
															"porcentaje_iva=?,  " +
															"codigo_interfaz=?, " +
															"indicativo_automatico=?, " +
															"indicativo_por_completar=?, " +
															"precio_ultima_compra=?,  " +
															"precio_base_venta=?,  " +
															"fecha_precio_base_venta=?, " +
															"precio_compra_mas_alta=?, " +
															"descripcion_alterna=?, " +
															"numero_expediente=?, " +
															"cons_present_comercial=?, " +
															"presentacion_comercial=?, " +
															"clasificacion_atc=?, " +
															"registro_art=?, " +
															"vigencia=?, " +
															"roles_x_producto=?, " +
															"titular=?, " +
															"fabricante=?, " +
															"importador=?, " +
															"nivel_atencion=? " +
														"where " +
															"codigo=?";
	
	/**
	 * 
	 */
	private static final String modificarInfoMArticuloStr="UPDATE articulos_info_medica SET tiempo_resp_esperado=?, efecto_deseado=?, efecto_secundario=?, observaciones=?, bibliografia=? where codigo_articulo = ? ";
	
	/**
	 * Seleccionar el último artículo ingresado
	 */
	private static final String ultimoArticuloStr="SELECT max(codigo) FROM articulo";
	
	/**
	 * Selecion las descripcion de todos los campos codigo de un articulo.
	 * @author armando
	 */
	private static String cargarDescripcionArticulo="SELECT " +
													"  inventarios.getNomNaturalezaArticulo(vista.naturaleza) AS naturaleza" +
													", inventarios.getNomFormaFarmaceutica(vista.forma_farmaceutica) AS formaFarmaceutica" +
													", inventarios.getNomUnidadMedida(vista.unidad_medida) AS unidadMedida " +
													", inventarios.getPosNaturalezaArticulo(vista.naturaleza) AS es_pos " +
													"  FROM " +
													"  inventarios.view_articulos vista " +
													"  WHERE vista.estado='1' "+
													"  and vista.codigo=?";
	/**
	 * Cadena para buscar artículos
	 */
	private static final String buscarArticulosStr = "SELECT   a.codigo AS codigo" +
															", a.minsalud AS minsalud" +
															", a.descripcion AS descripcion" +
															", a.naturaleza AS naturaleza" +
															", a.forma_farmaceutica AS formafarmaceutica" +
															", a.concentracion AS concentracion" +
															", s.subgrupo AS subgrupo" +
															", s.grupo AS grupo" +
															", s.clase AS clase" +
															", a.estado AS estado" +
															", a.unidad_medida AS unidadmedida" +
															", a.costo_donacion AS costodonacion" +
															", a.codigo_interfaz AS codigointerfaz" +
															", a.indicativo_automatico AS indicativoautomatico" +
															", a.indicativo_por_completar AS indicativoporcompletar" +
															", na.nombre as desnaturaleza" +
															", um.nombre as desunidadmedida" +
															", a.atencion_odontologica AS atencionodontologica " +
															", a.nivel_atencion AS nivelatencion "+
															" FROM articulo a" +
															" INNER JOIN subgrupo_inventario s ON(s.codigo=a.subgrupo)" +
															" INNER JOIN unidad_medida um on (a.unidad_medida = um.acronimo)" +
															" INNER JOIN naturaleza_articulo na on(a.naturaleza=na.acronimo)  WHERE 1=1";
	
	
	
	private static final String buscoubicacionStr="SELECT " +
														"det.articulo as uarticulo, " +
														"det.ubicacion as uubicacion, " +
														"getnombresubseccion(aa.seccion,aa.subseccion) as usubseccion, " +
														"getnombreseccion(aa.seccion) as useccion, " +
														"getnomcentroatencion(aa.centro_atencion) as ucentro_atencion, " +
														"getnombrealmacen(aa.almacen) as ualmacen " +
													"FROM " +
														"det_articulos_por_almacen det " +
													"INNER JOIN " +
														"articulos_por_almacen  aa on (det.codigo_art_por_almacen=aa.codigo_pk) "+
													"WHERE 1=1 ";
	
	/**
	 * Cadena para insertar un atributo de la justificacion de medicamentos
	 */
	private static final String insertarAtributoJustificacionStr="INSERT INTO desc_atributos_solicitud (numero_solicitud,articulo,atributo,descripcion) VALUES(?,?,?,?)";
	
	
	/**
	 * Cadena para verificar si hay existencias para un Articulo
	 */
	private static final String existenciaArticuloStr =
		"SELECT COUNT(1) as existe "+ 
		"FROM inventarios.articulo_almacen_x_lote a   "+ 
		"WHERE a.articulo= ? ";
	

	private static final String cargarViasAdminArticuloStr =
		"SELECT " +
		"s.codigo as codigo, " +
		"s.via_admin as via_admin, " +
		"s.forma_farmaceutica as forma_farmaceutica, " +
		"s.articulo as articulo, " +
		"s.activo as activo, " +
		"s2.nombre as nombre, " +
		"'BD' as tiporegistro, " +
		"'-1' as posEliminar " +
		"FROM inventarios.vias_admin_articulo s , articulo s1 , vias_administracion s2 " +
		"WHERE 1=1 " +
		"AND s1.codigo = ? " +
		"AND s.articulo = s1.codigo " +
		"AND s.via_admin = s2.codigo " +
		"ORDER BY s.codigo ";
	
		
	private static final String insertarViasAdminArticuloStr = 
		"INSERT INTO vias_admin_articulo (codigo, via_admin,forma_farmaceutica, articulo, activo,institucion) VALUES ";
	
	
	private static final String insertarGrupoEspecialArticuloStr = 
		"INSERT INTO grupo_especial_X_articulo (codigo_pk, articulo, grupo_especial, activo, fecha_modifica, hora_modifica, usuario_modifica) VALUES(?,?,?,?,?,?,?)";
	
	
	private static final String actualizarGrupoEspecialArticuloStr = "UPDATE grupo_especial_x_articulo SET activo=?, fecha_modifica=?, hora_modifica=?, usuario_modifica=? WHERE codigo_pk=?";
	
	
	private static final String actualizarViasAdminArticuloStr = "UPDATE inventarios.vias_admin_articulo SET activo=? WHERE codigo=? ";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 */
	private static final String cargarUnidosisArticuloStr =
		"SELECT " +
		"s.codigo as codigo, " +
		"s.articulo as articulo, " +
		"s.unidad_medida as unidad_medida, " +
		"CASE WHEN s.cantidad IS NULL THEN 0 ELSE s.cantidad END as cantidad, " +
		"s.activo as activounidosis, " +
		"s2.nombre as nombre, " +
		"'BD' as tiporegistro, " +
		"'-1' as posEliminar " +
		"FROM inventarios.unidosis_x_articulo s , articulo s1 , unidad_medida s2 " +
		"WHERE 1=1 " +
		"AND s1.codigo = ? " +
		"AND s.articulo = s1.codigo " +
		"AND s.unidad_medida = s2.acronimo " +
		"ORDER BY s.codigo ";
	
	/**
	 * 
	 */
	private static final String cargarGruposEspecialesArticuloStr = 
		"SELECT " +
		"ga.codigo_pk as codigo, " +
		"ga.grupo_especial as grupoespecial, " +
		"ga.activo as activo, " +
		"g.descripcion as nombre, " +
		"'BD' as tiporegistro " +
		"FROM grupo_especial_x_articulo ga " +
		"INNER JOIN grupo_especial_articulos g ON(g.codigo_pk=ga.grupo_especial) " +
		"WHERE ga.articulo=? ";
	
	/**
	 * 
	 */
	private static final String cargarDescripcionNivelAtencion = 
		"SELECT " +
		"a.codigo as codigo, " +
		"n.descripcion as descripcionnivel " +
		"FROM inventarios.articulo a " +
		"INNER JOIN capitacion.nivel_atencion n ON(a.nivel_atencion=n.consecutivo) " +
		"WHERE a.codigo=? ";
	
	/**
	 * 	
	 */
	private static final String insertarUnidosisArticuloStr = 
		"INSERT INTO inventarios.unidosis_x_articulo (codigo,articulo,unidad_medida,cantidad,activo) VALUES ";

	
	/**
	 * 
	 */
	private static final String actualizarUnidosisArticuloStr = "UPDATE inventarios.unidosis_x_articulo SET activo=?, cantidad=? WHERE codigo=? ";
	
	/**
	 * 
	 */
	private static String insertarStr="INSERT INTO articulos_info_medica VALUES (?,?,?,?,?,?)";
	
	/**
	 * 
	 */
	private static String consultaStrIMA="SELECT * FROM articulos_info_medica WHERE codigo_articulo=? ";
	
	/**
	 * 
	 */
	private static String consultaStrIMAAD="SELECT codigo, cod_articulo, nombre_archivo, nombre_original, '1' AS checkac FROM doc_adj_articulo WHERE cod_articulo=? ";
	
	/**
	 * 
	 */
	private static String[] indicesMapIMA={"tiempo_resp_esperado_","efecto_deseado_","efecto_secundario_","observaciones_","bibliografia_"};
	
	/**
	 * 
	 */
	private static String[] indicesMapIMAAD={"codigo_","cod_articulo_","nombre_archivo_","nombre_original_","checkac_"};
	
	/**
	 * 
	 */
	private static String eliminarAdjIM="DELETE FROM doc_adj_articulo WHERE codigo=? ";
	
	
	/**
	 * 
	 * @param con
	 * @param codArticulo
	 * @param articulo
	 * @return
	 */
	public static boolean insertarInformMed(Connection con, int codArticulo, Articulo articulo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codArticulo);
			ps.setString(2, articulo.getTiempoEsp());
			ps.setString(3, articulo.getEfectoDes());
			ps.setString(4, articulo.getEfectosSec());
			ps.setString(5, articulo.getObservaciones());
			ps.setString(6, articulo.getBibliografia());
						
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * Método para insertar un nuevo artículo 
	 * @param con
	 * @param claseArticulo
	 * @param grupoArticulo
	 * @param subgrupoArticulo
	 * @param naturalezaArticulo
	 * @param formaFarmaceutica
	 * @param concentracionArticulo
	 * @param unidadMedidaArticulo
	 * @param estadoArticulo
	 * @param minsalud
	 * @param descripcion
	 * @param variableSqlStr
	 * @param insertarArticuloStr
	 * @param categoria
	 * @param costoPromedio
	 * @param cantidadCompra
	 * @param puntoPedido
	 * @param stockMaximo
	 * @param stockMinimo
	 * @param usuario
	 * @param variableSqlStr 
	 * @return
	 */
	public static int insertarArticulo(
						Connection con,						//1
						String subgrupoArticulo,			//2
						String naturalezaArticulo,			//3
						String formaFarmaceutica,			//4
						String concentracionArticulo,		//5
						String unidadMedidaArticulo,		//6
						boolean estadoArticulo,				//7
						String minsalud,					//8
						String descripcion,					//9
						String fecha, 						//10
						String usuario, 					//11
						String horaCreacion, 				//12
						int stockMinimo, 					//13
						int stockMaximo, 					//14
						int puntoPedido, 					//15
						int cantidadCompra, 				//16
						String costoPromedio,				//17	
						double costoDonacion, 				//18
						String codigoInterfaz,				//19
						String indicativoAutomatico,		//20	
						String indicativoPorCompletar, 		//21
						int categoria, 						//22
						String registroINVIMA, 				//23
						double maximaCantidadMes, 			//24
						String multidosis, 					//25
						String manejaLotes,					//26
						String manejaFechaVencimiento,		//27
						double porcentajeIva,				//28
						double precioUltimaCompra,			//29
						double precioBaseVenta, 			//30
						double precioCompraMasAlta,			//31
						String fechaPrecioBaseVenta,		//32
						String descripcionAlterna,			//33
						int institucion,					//34			
						String variableSqlStr,//35
						String variableSeqViaAdmArtStr,//36
						String variableSeqUnidadesArticuloStr,//37
						HashMap mapaViaAdm,//38
						HashMap mapaUnidosis,//39
						HashMap mapaGrupoEspecial,//40
						HashMap cumMap,//41
						String atencionOdon,//42
						long consecutivoNivelAtencionArticulo
						)
		{

		PreparedStatementDecorator insertarArticuloStatement;
		try
		{
			insertarArticuloStatement =  new PreparedStatementDecorator(con.prepareStatement(variableSqlStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insertarArticuloStatement.setInt(1, Integer.parseInt(subgrupoArticulo));
			insertarArticuloStatement.setString(2, descripcion.trim().replaceAll("'", "`"));
			insertarArticuloStatement.setString(3, naturalezaArticulo.trim());
			insertarArticuloStatement.setString(4, minsalud.trim());
			if(!formaFarmaceutica.equals("0"))
			{
				insertarArticuloStatement.setString(5, formaFarmaceutica);
			}
			else
			{
				insertarArticuloStatement.setString(5, null);
			}
			
			insertarArticuloStatement.setString(6, concentracionArticulo.trim().replaceAll("'", "`"));
			if(!unidadMedidaArticulo.equals("0"))
			{
				insertarArticuloStatement.setString(7, unidadMedidaArticulo.trim().replaceAll("'", "`"));
			}
			else
			{
				insertarArticuloStatement.setString(7, null);
			}
			insertarArticuloStatement.setBoolean(8, estadoArticulo);
			insertarArticuloStatement.setDate(9,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD( fecha)));
			insertarArticuloStatement.setString(10, usuario);
			insertarArticuloStatement.setString(11, UtilidadFecha.getHoraActual());
			insertarArticuloStatement.setInt(12,categoria);
			insertarArticuloStatement.setInt(13, stockMinimo);
			insertarArticuloStatement.setInt(14,stockMaximo);
			insertarArticuloStatement.setInt(15,puntoPedido);
			insertarArticuloStatement.setInt(16,cantidadCompra);
			insertarArticuloStatement.setDouble(17, Double.parseDouble(costoPromedio));
			insertarArticuloStatement.setObject(18,registroINVIMA);
			
			if(maximaCantidadMes==0)
			{
				insertarArticuloStatement.setNull(19, Types.DOUBLE);
			}
			else
			{
				insertarArticuloStatement.setDouble(19, maximaCantidadMes);
			}
			
			insertarArticuloStatement.setString(20, multidosis);
			insertarArticuloStatement.setString(21, manejaLotes);
			insertarArticuloStatement.setString(22, manejaFechaVencimiento);
			insertarArticuloStatement.setDouble(23, porcentajeIva);

			if(precioUltimaCompra==0)
			{
				insertarArticuloStatement.setNull(24, Types.DOUBLE);
			}
			else
			{
				insertarArticuloStatement.setDouble(24, precioUltimaCompra);
			}
			
			if(precioBaseVenta==0)
			{
				insertarArticuloStatement.setNull(25, Types.DOUBLE);
			}
			else
			{
				insertarArticuloStatement.setDouble(25, precioBaseVenta);
			}		

			
			logger.info("fechaPrecioBaseVenta-->"+fechaPrecioBaseVenta+"<----");
			
			if(!fechaPrecioBaseVenta.equals(""))
			{
				insertarArticuloStatement.setDate(26,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD( fechaPrecioBaseVenta)));
			}
			else
			{
				insertarArticuloStatement.setString(26, null);
			}		
			
			insertarArticuloStatement.setInt(27,institucion);
			insertarArticuloStatement.setDouble(28,costoDonacion);
			insertarArticuloStatement.setString(29,codigoInterfaz);
			if(indicativoAutomatico.equalsIgnoreCase(""))
			{
				insertarArticuloStatement.setString(30, ConstantesBD.acronimoNo);
			}
			else
			{
				insertarArticuloStatement.setString(30, indicativoAutomatico);
			}
			if(indicativoPorCompletar.equalsIgnoreCase(""))
			{
				insertarArticuloStatement.setString(31, ConstantesBD.acronimoNo);
			}
			else
			{
				insertarArticuloStatement.setString(31, indicativoPorCompletar);
			}
			if(precioCompraMasAlta==0)
			{
				insertarArticuloStatement.setNull(32, Types.DOUBLE);
			}
			else
			{
				insertarArticuloStatement.setDouble(32, precioCompraMasAlta);
			}
			if(!descripcionAlterna.equals(""))
			{
				insertarArticuloStatement.setString(33, descripcionAlterna);
			}
			else
			{
				insertarArticuloStatement.setString(33, null);
			}
			insertarArticuloStatement.setString(34, cumMap.get("numero_expediente")+"");
			insertarArticuloStatement.setString(35, cumMap.get("cons_present_comercial")+"");
			insertarArticuloStatement.setString(36, cumMap.get("presentacion_comercial")+"");
			insertarArticuloStatement.setString(37, cumMap.get("clasificacion_atc")+"");
			insertarArticuloStatement.setString(38, cumMap.get("registro_art")+"");
			insertarArticuloStatement.setString(39, cumMap.get("vigencia")+"");
			insertarArticuloStatement.setString(40, cumMap.get("roles_x_producto")+"");
			insertarArticuloStatement.setString(41, cumMap.get("titular")+"");
			insertarArticuloStatement.setString(42, cumMap.get("fabricante")+"");
			insertarArticuloStatement.setString(43, cumMap.get("importador")+"");
			insertarArticuloStatement.setString(44, atencionOdon);
			
			if(consecutivoNivelAtencionArticulo==ConstantesBD.codigoNuncaValidoLong){
				insertarArticuloStatement.setNull(45, Types.LONGVARCHAR);
			}else{
				insertarArticuloStatement.setLong(45, consecutivoNivelAtencionArticulo);
			}
			
			
			//
			if(insertarArticuloStatement.executeUpdate()>0)
			{
				//return codigoUltimoArticulo(con);

				int codUltArticulo = codigoUltimoArticulo(con);
				//Se insertan las Vias de Administracion
				if ( codUltArticulo > 0)
				{
					insertarViasAdministracionArticulo(con, mapaViaAdm, codUltArticulo,variableSeqViaAdmArtStr,formaFarmaceutica,institucion);
					
					insertarUnidosisArticulo(con, mapaUnidosis, codUltArticulo, variableSeqUnidadesArticuloStr);
					
					insertarGrupoEspecialArticulos(con, mapaGrupoEspecial, codUltArticulo, usuario);
				}
				return codUltArticulo;
			}
			else
			{
				return 0;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error insertando el artículo "+e);
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * Método para cargar un artículo
	 * @param con
	 * @param codigoArticulo
	 * @return
	 * @throws SQLException
	 */
	public static Answer cargarArticulo(Connection con, int codigoArticulo)
	{
		try
		{
			if(con==null || con.isClosed())
			{
				con=UtilidadBD.abrirConexion();
			}
		} catch (SQLException e1)
		{
			logger.warn("No se pudo realizar la conexión (SqlBaseArticuloDao)"+e1.toString());
		}
		
		PreparedStatementDecorator cargarArticuloStatement;
		try
		{
			cargarArticuloStatement =  new PreparedStatementDecorator(con.prepareStatement(cargarArticuloStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						
			cargarArticuloStatement.setInt(1, codigoArticulo);
			return new Answer(new ResultSetDecorator(cargarArticuloStatement.executeQuery()), con);
		}
		catch (SQLException e)
		{
			logger.error("Error cargando el artículo "+codigoArticulo+": "+e);
			return null;
		}
	}
	
	/**
	 * Método para cargar un artículo con sus descripcions
	 * @param con
	 * @param codigoArticulo
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator cargarArticuloDescripciones(Connection con, int codigoArticulo)
	{
		try
		{
			if(con==null || con.isClosed())
			{
			con=UtilidadBD.abrirConexion();
			}
		} catch (SQLException e1)
		{
			logger.warn("No se pudo realizar la conexión (SqlBaseArticuloDao)"+e1.toString());
		}
		
		PreparedStatementDecorator cargarArticuloStatement;
		try
		{
			logger.info("cargarDescripcionArticulo- - - - -- - - -- -- - - -- > > > > > > >  > >  > >  >"+cargarDescripcionArticulo+"\n\n código articulo::::"+ codigoArticulo);
			cargarArticuloStatement =  new PreparedStatementDecorator(con.prepareStatement(cargarDescripcionArticulo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarArticuloStatement.setInt(1, codigoArticulo);
			return new ResultSetDecorator(cargarArticuloStatement.executeQuery());
		}
		catch (SQLException e)
		{
			logger.error("Error cargando el artículo "+codigoArticulo+": "+e);
			return null;
		}
	}

	/**
	 * Método para activar o inactivar el artículo
	 * @param con
	 * @param codigoArticulo
	 * @param estadoArticulo
	 * @return 0 si hubo algún error
	 */
	public static int cambiarEstadoArticulo(
						Connection con,
						String codigoArticulo,
						boolean estadoArticulo)
	{
		PreparedStatementDecorator cambiarEstadoArticuloStatement;
		try
		{
			cambiarEstadoArticuloStatement =  new PreparedStatementDecorator(con.prepareStatement(cambiarEstadoArticuloStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cambiarEstadoArticuloStatement.setBoolean(1, estadoArticulo);
			cambiarEstadoArticuloStatement.setString(2, codigoArticulo);
			return cambiarEstadoArticuloStatement.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error cambiando el estado de los artículos "+e);
			return 0;
		}
	}
	
	/**
	 * Método para modificar un artículo
	 * @param con
	 * @param claseArticulo
	 * @param grupoArticulo
	 * @param subgrupoArticulo
	 * @param naturalezaArticulo
	 * @param formaFarmaceutica
	 * @param concentracionArticulo
	 * @param unidadMedidaArticulo
	 * @param estadoArticulo
	 * @param minsalud
	 * @param descripcion
	 * @param codigo
	 * @param categoria
	 * @param costoPromedio
	 * @param cantidadCompra
	 * @param puntoPedido
	 * @param stockMaximo
	 * @param stockMinimo
	 * @param registroInvima 
	 * @param TIPO_BD 
	 * @param seqInsertarUnidosisArt 
	 * @return
	 * @throws SQLException
	 */
	public static int modificarArticulo(
			Connection con, String naturalezaArticulo, String formaFarmaceutica, String concentracionArticulo, String unidadMedidaArticulo, 
			boolean estadoArticulo, String minsalud, String descripcion, int codigo, int stockMinimo, int stockMaximo, int puntoPedido, 
			int cantidadCompra, String costoPromedio, String codigoInterfaz, String indicativoAutomatico, String indicativoPorCompletar,
			int categoria, String registroInvima, double maximaCantidadMes,  String multidosis, String manejaLotes, String manejaFechaVencimiento,
			double porcentajeIva, double precioUltimaCompra, double precioBaseVenta, double precioCompraMasAlta, String descripcionAlterna, 
			String fechaPrecioBaseVenta, int institucion, boolean manejoLoteDeSaN, HashMap mapaViaAdm, HashMap mapaUnidosis, 
			HashMap mapaGrupoEspecial, String usuario, HashMap cumMap, int TIPO_BD, String seqInsertarUnidosisArt, long nivelAtencion)
	{
		PreparedStatementDecorator modificarArticuloStatement;
		try
		{
			logger.info("===> Entré a modificarArticulo");
			logger.info("===> minsalud = "+minsalud);
			modificarArticuloStatement =  new PreparedStatementDecorator(con.prepareStatement(modificarArticuloStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			
			modificarArticuloStatement.setString(1, descripcion.replaceAll("'", "`"));
			
			modificarArticuloStatement.setString(2, naturalezaArticulo.trim());
			
			modificarArticuloStatement.setString(3, minsalud.trim());
			
			if(!formaFarmaceutica.equals("0"))
			{
				modificarArticuloStatement.setString(4, formaFarmaceutica);
			
			}
			else
			{
				modificarArticuloStatement.setNull(4, Types.VARCHAR);
			
			}
			modificarArticuloStatement.setString(5, unidadMedidaArticulo.trim().replaceAll("'", "`"));
			
			modificarArticuloStatement.setString(6, concentracionArticulo.trim().replaceAll("'", "`"));
			
			modificarArticuloStatement.setBoolean(7, (estadoArticulo));
			
			modificarArticuloStatement.setString(8, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			
			modificarArticuloStatement.setString(9, usuario);
			
			modificarArticuloStatement.setString(10, UtilidadFecha.getHoraActual());
			
			modificarArticuloStatement.setInt(11, stockMinimo);
			
			modificarArticuloStatement.setInt(12, stockMaximo);
			
			modificarArticuloStatement.setInt(13,puntoPedido);
			
			modificarArticuloStatement.setInt(14,cantidadCompra);
			
			modificarArticuloStatement.setDouble(15, Double.parseDouble(costoPromedio));
			
			modificarArticuloStatement.setInt(16,categoria);
			
			modificarArticuloStatement.setString(17,registroInvima);
			
			modificarArticuloStatement.setDouble(18, maximaCantidadMes);
			
			modificarArticuloStatement.setString(19, multidosis.trim());
			
			modificarArticuloStatement.setString(20, manejaLotes.trim());
			
			modificarArticuloStatement.setString(21, manejaFechaVencimiento.trim());
			
			modificarArticuloStatement.setDouble(22, porcentajeIva);
			
			modificarArticuloStatement.setString(23, codigoInterfaz);
			
			modificarArticuloStatement.setString(24, indicativoAutomatico);
			
			if(indicativoPorCompletar.equals(ConstantesBD.acronimoSi))
			{
				modificarArticuloStatement.setString(25, ConstantesBD.acronimoNo);
			
			}
			else
			{
				modificarArticuloStatement.setString(25, indicativoPorCompletar);
			
			}
			//modificarArticuloStatement.setDouble(20, precioUltimaCompra);
			//modificarArticuloStatement.setDouble(21, precioBaseVenta);

			if(precioUltimaCompra==0)
			{
				modificarArticuloStatement.setNull(26, Types.DOUBLE);
			
			}
			else
			{
				modificarArticuloStatement.setDouble(26, precioUltimaCompra);
			
			}
			
			if(precioBaseVenta==0)
			{
				modificarArticuloStatement.setNull(27, Types.DOUBLE);
			
			}
			else
			{
				modificarArticuloStatement.setDouble(27, precioBaseVenta);
			
			}	
			

			if(!fechaPrecioBaseVenta.equals(""))
			{
				modificarArticuloStatement.setDate(28,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD( fechaPrecioBaseVenta)));
			
			}
			else
			{
				modificarArticuloStatement.setString(28, null);
			
			}	
			
			if(precioCompraMasAlta==0)
			{
			
				modificarArticuloStatement.setNull(29, Types.DOUBLE);
			}
			else
			{
			
				modificarArticuloStatement.setDouble(29, precioCompraMasAlta);
			}
			if(!descripcionAlterna.equals(""))
			{
			
				modificarArticuloStatement.setString(30, descripcionAlterna);
			}
			else
			{
			
				modificarArticuloStatement.setString(30, null);
			}
			
			
			
			modificarArticuloStatement.setString(31, cumMap.get("numero_expediente")+"");
			
			modificarArticuloStatement.setString(32, cumMap.get("cons_present_comercial")+"");
			
			modificarArticuloStatement.setString(33, cumMap.get("presentacion_comercial")+"");
			
			modificarArticuloStatement.setString(34, cumMap.get("clasificacion_atc")+"");
			
			modificarArticuloStatement.setString(35, cumMap.get("registro_art")+"");
			
			modificarArticuloStatement.setString(36, cumMap.get("vigencia")+"");
			
			modificarArticuloStatement.setString(37, cumMap.get("roles_x_producto")+"");
			
			modificarArticuloStatement.setString(38, cumMap.get("titular")+"");
			
			modificarArticuloStatement.setString(39, cumMap.get("fabricante")+"");
			
			modificarArticuloStatement.setString(40, cumMap.get("importador")+"");
			
			if(nivelAtencion!=ConstantesBD.codigoNuncaValidoLong)
			{
				modificarArticuloStatement.setLong(41, nivelAtencion);
			}
			else
			{
			
				modificarArticuloStatement.setNull(41, Types.LONGVARCHAR);
			}
			
			//	modificarArticuloStatement.setDate(22, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD( fechaPrecioBaseVenta)));
			
			///
			modificarArticuloStatement.setInt(42, codigo);
			
			//return modificarArticuloStatement.executeUpdate();
			int resp = modificarArticuloStatement.executeUpdate();

			if (resp>0)
			{
			
				actualizarViasAdministracionArticulo(con, mapaViaAdm, codigo,formaFarmaceutica,institucion,TIPO_BD);
				
				actualizarUnidadesMedidaArticulo(con, mapaUnidosis, codigo, seqInsertarUnidosisArt);
				
				actualizarGrupoEspecialArticulo(con, mapaGrupoEspecial, codigo, usuario);
				
			}
			/////
			if ( resp > 0 && manejoLoteDeSaN==true)
			{
				String cadena="SELECT almacen,existencias from articulos_almacen where articulo="+codigo+" and institucion=2";
				PreparedStatementDecorator psTemp= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
				ResultSetDecorator rsTemp=new ResultSetDecorator(psTemp.executeQuery());
				while(rsTemp.next())
				{
					String cadena1="SELECT sum(existencias) from articulo_almacen_x_lote where articulo = "+codigo+" and almacen="+rsTemp.getInt(1);
					psTemp= new PreparedStatementDecorator(con.prepareStatement(cadena1,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
					ResultSetDecorator rsTemp1=new ResultSetDecorator(psTemp.executeQuery());
					if(rsTemp1.next())
					{
						if(!(rsTemp.getInt(2)==rsTemp1.getInt(1)))
						{
							int diferencia=rsTemp.getInt(2)-rsTemp1.getInt(1);
							UtilidadInventarios.actualizarExistenciasArticuloAlmacenSoloLoteTransaccional(con, codigo, rsTemp.getInt(1), true, diferencia, institucion, ConstantesBD.continuarTransaccion, "", "");
						}
					}
					else
					{
						UtilidadInventarios.actualizarExistenciasArticuloAlmacenSoloLoteTransaccional(con, codigo, rsTemp.getInt(1), true, rsTemp.getInt(2), institucion, ConstantesBD.continuarTransaccion, "", "");
					}
				}
					
			}
			return resp;
			/////
			
		}
		catch (Exception e)
		{
			logger.error("Error modificando el artículo numero: "+"12: "+e);
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Método encargado de modificar la información medica de un artículo cuya naturaleza sea Medicamento
	 * @param con
	 * @param codigo
	 * @param tiempoEsp
	 * @param efectoDes
	 * @param efectosSec
	 * @param observaciones
	 * @param bibliografia
	 * @return resp/0 int
	 */
	public static int modificarInfoMArticulo(Connection con, int codigo, String tiempoEsp, String efectoDes, String efectosSec, 
			String observaciones, String bibliografia)
	{
		logger.info("===> Entré a modificarInfoMArticulo");
		PreparedStatementDecorator modificarArticuloStatement;
		try
		{
			logger.info("===> Entré al try, Voy a preparar la consulta !!!");
			logger.info("===> Los datos a modificar son: ");
			logger.info("===> :P");
			logger.info("===> codigo = "+codigo);
			logger.info("===> tiempoEsp = "+tiempoEsp);
			logger.info("===> efectoDes = "+efectoDes);
			logger.info("===> efectoSec = "+efectosSec);
			logger.info("===> observaciones = "+observaciones);
			logger.info("===> bibliografia = "+bibliografia);
			modificarArticuloStatement =  new PreparedStatementDecorator(con.prepareStatement(modificarInfoMArticuloStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			modificarArticuloStatement.setString(1, tiempoEsp);
			modificarArticuloStatement.setString(2, efectoDes);
			modificarArticuloStatement.setString(3, efectosSec);
			modificarArticuloStatement.setString(4, observaciones);
			modificarArticuloStatement.setString(5, bibliografia);
			modificarArticuloStatement.setInt(6, codigo);
			
			int resp = modificarArticuloStatement.executeUpdate();
			logger.info("===> Vamos a retornar resp = "+resp);
			return resp;
			/////
			
		}
		catch (Exception e)
		{
			logger.error("Error modificando el artículo numero: " + codigo + " "+e);
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Método encargado de modificar la información medica de un artículo cuya naturaleza sea Medicamento
	 * @param con
	 * @param codigo
	 * @param tiempoEsp
	 * @param efectoDes
	 * @param efectosSec
	 * @param observaciones
	 * @param bibliografia
	 * @return true/false boolean
	 */
	public static boolean modificarInfoMedicaArticulo(Connection con, int codigo, String tiempoEsp, String efectoDes, String efectosSec, 
			String observaciones, String bibliografia)
	{
		HashMap <String,Object>  existeInformacionMedica = existeInfoMedica(con, codigo), resultados = new HashMap<String,Object>();
		logger.info("===> Entré a modificarInfoMedicaArticulo");
		logger.info("===> Existe Informacion Médica ? "+existeInformacionMedica);
		try
		{
			logger.info("===> Entré al try, Voy a preparar la consulta !!!");
			logger.info("===> Los datos a modificar son: ");
			logger.info("===> :P");
			logger.info("===> codigo = "+codigo);
			logger.info("===> tiempoEsp = "+tiempoEsp);
			logger.info("===> efectoDes = "+efectoDes);
			logger.info("===> efectoSec = "+efectosSec);
			logger.info("===> observaciones = "+observaciones);
			logger.info("===> bibliografia = "+bibliografia);
			String actualizacion = "UPDATE " +
					"articulos_info_medica " +
					"SET " +
						"tiempo_resp_esperado= '"+tiempoEsp+"', " +
						"efecto_deseado= '"+efectoDes+"', " +
						"efecto_secundario= '"+efectosSec+"', " +
						"observaciones= '"+observaciones+"', " +
						"bibliografia= '"+bibliografia+"' " +
						"where codigo_articulo =  "+codigo+" ";
			
			String insercion = "INSERT INTO " +
			"articulos_info_medica (codigo_articulo, tiempo_resp_esperado, efecto_deseado, efecto_secundario, observaciones, bibliografia)" +
			"VALUES (" +codigo+", '"+tiempoEsp+"', '"+efectoDes+"', '"+efectosSec+"', '"+observaciones+"', '"+bibliografia+"' )";
			int numRegistros = Integer.parseInt(existeInformacionMedica.get("numRegistros")+"");
			logger.info("===> numRegistros = "+numRegistros);
			int seEjecuto= 0;
			
			if(numRegistros == 0)
			{
				logger.info("===> numRegistros = 0 Entonces se va a INSERTAR ");
				logger.info("===> La consulta de inserción es: "+insercion);
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insercion, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				seEjecuto = ps.executeUpdate();
				logger.info("===> ps.execute() = "+seEjecuto);
				if(seEjecuto >0)
				{
					logger.info("===> SE INSERTÓ CORRECTAMENTE !!! :P");
					return true;
				}
					
				
			}
			else
			{
				logger.info("===> numRegistros != 0 Entonces se va a ACTUALIZAR !!!");
				logger.info("===> La consulta de actualizacion es: "+actualizacion);
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(actualizacion, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				seEjecuto = ps.executeUpdate();
				logger.info("===> ps.execute() = "+seEjecuto);
				if(seEjecuto >0)
				{
					logger.info("===> SE ACTUALIZÓ CORRECTAMENTE !!! :P");
					return true;
				}
					
			}
			
		}
		catch (Exception e)
		{
			logger.error("===> Error modificando el artículo numero: " + codigo + " "+e);
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	/**
	 * Método encargado de verificar si existe una información medica dado un codigo de artículo
	 * @author Felipe Pérez Granda
	 * @param connection Connection
	 * @param codigo int
	 * @return HashMap
	 */
	public static HashMap <String,Object>  existeInfoMedica (Connection connection, int codigo)
	{
		HashMap <String,Object>  mapa = new HashMap <String,Object> ();
		String consulta = "SELECT * FROM articulos_info_medica where codigo_articulo = "+codigo+"";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(consulta, ConstantesBD.typeResultSet, 
					ConstantesBD.concurrencyResultSet ));
			
			logger.info("===> Verificamos si hay registros de Info Medica, La consulta es: "+consulta);
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			return mapa;
		} 
		
		catch (SQLException e) 
		{
			logger.error("===> ERROR CONSULTANDO EL REGISTRO"+e);
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param con
	 * @param valor
	 * @param codigo
	 * @param tarifa
	 * @return
	 */
	public static boolean modificarTarifas(Connection con, double valor, int codigo, String tarifa)
	{
		//solo debe actualizar las tarifas de inventarios cuando el indicativo
		Vector<String> vectorCodigos= obtenerCodigosTarifasVigentesInv(con, codigo);
		
		if(vectorCodigos.size()>0)
		{
			String cadena="UPDATE tarifas_inventario SET valor_tarifa="+valor+" WHERE tipo_tarifa='"+tarifa+"' AND articulo="+codigo+" AND (actualiz_automatic='"+ConstantesBD.acronimoSi+"' or actualiz_automatic is null) ";
			cadena+=" AND codigo in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(vectorCodigos, false)+") ";
			
			logger.info("\n*********************************************************************************************************************");
			logger.info("modificarTarifas-->"+cadena);
			logger.info("*********************************************************************************************************************\n");
			try
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.executeUpdate();
				return true;
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
			return false;
		}
		return true;
	}	

	/**
	 * 
	 * @param con
	 * @param articulo
	 * @return
	 */
	private static Vector<String> obtenerCodigosTarifasVigentesInv(Connection con, int articulo)
	{
		Vector<String> vectorCodigos= new Vector<String>();
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		HashMap<Object, Object> mapaAgrupado= new HashMap<Object, Object>();
		int numRegistrosMapaAgrupado=0;
		mapaAgrupado.put("numRegistros", numRegistrosMapaAgrupado);
		
		String consulta="(" +
						"SELECT " +
							"codigo as codigo, " +
							"esquema_tarifario as esquema, " +
							"articulo as articulo, " +
							"to_char(fecha_vigencia, 'YYYY-MM-DD') as fechavigencia " +
						"FROM " +
							"tarifas_inventario " +
						"WHERE " +
							"articulo="+articulo+" " +
							"and fecha_vigencia<=current_date " +
							")" +
						"UNION ALL " +
						"(" +
						"SELECT " +
							"codigo as codigo, " +
							"esquema_tarifario as esquema, " +
							"articulo as articulo, " +
							"'' as fechavigencia " +
						"FROM " +
							"tarifas_inventario " +
						"WHERE " +
							"articulo="+articulo+" " +
							"and fecha_vigencia is null " +
						
						")"+
							"order by articulo, esquema, fechavigencia";		
		logger.info("\n\n******************************************************************************************************************");
		logger.info("obtenerCodigosTarifasVigentesInv--->"+consulta);
		logger.info("******************************************************************************************************************\n\n");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
			for(int w=0; w<Utilidades.convertirAEntero(mapa.get("numRegistros")+"");w++)
			{
				if(!existeEsquemaYarticuloVector(mapaAgrupado, mapa.get("articulo_"+w)+"", mapa.get("esquema_"+w)+""))
				{
					mapaAgrupado.put("codigo_"+numRegistrosMapaAgrupado, mapa.get("codigo_"+w)+"");
					mapaAgrupado.put("articulo_"+numRegistrosMapaAgrupado, mapa.get("articulo_"+w)+"");
					mapaAgrupado.put("esquema_"+numRegistrosMapaAgrupado, mapa.get("esquema_"+w)+"");
					numRegistrosMapaAgrupado++;
					mapaAgrupado.put("numRegistros", numRegistrosMapaAgrupado);
				}
			}
			
			for(int w=0; w<numRegistrosMapaAgrupado;w++)
			{
				vectorCodigos.add(mapaAgrupado.get("codigo_"+w)+"");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			logger.info("\n\n\n\n EROOORRR : "+e);
		}
		logger.info("codigos-------->"+vectorCodigos);
		
		return vectorCodigos;
	}
	
	/**
	 * 
	 * @param mapaAgrupado
	 * @param string
	 * @param string2
	 * @return
	 */
	private static boolean existeEsquemaYarticuloVector(HashMap<Object, Object> mapaAgrupado, String articulo, String esquema) 
	{
		for(int w=0; w<Utilidades.convertirAEntero(mapaAgrupado.get("numRegistros")+"");w++)
		{
			if((mapaAgrupado.get("articulo_"+w)+"").equals(articulo) && (mapaAgrupado.get("esquema_"+w)+"").equals(esquema))
				return true;
		}
		return false;
	}


	/**
	 * Método para obtener el código del último artículo ingresado
	 * @param con Connexión con la BD
	 * @return Entero con el código del articulo
	 */
	public static int codigoUltimoArticulo(Connection con)
	{
		PreparedStatementDecorator ultimoArticuloStatement;
		int codigo=ConstantesBD.codigoNuncaValido;
		try
		{
			ultimoArticuloStatement =  new PreparedStatementDecorator(con.prepareStatement(ultimoArticuloStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ultimoArticuloStatement.executeQuery());
			if(rs.next())
				codigo=rs.getInt(1);
		}
		catch (SQLException e)
		{
			logger.error("Error hallando el último artículo "+e);
		}
		return codigo;
	}

	/**
	 * @param con
	 * @param clase
	 * @param grupo
	 * @param subgrupo
	 * @param descripcion
	 * @param naturaleza
	 * @param minsalud
	 * @param formaFarmaceutica
	 * @param concentracion
	 * @param unidadMedida
	 * @param estadoArticulo
	 * @param cantidadCompra
	 * @param costoPromedio
	 * @param puntoPedido
	 * @param stockMaximo
	 * @param stockMinimo
	 * @param categoria
	 * @param fechaCreacion
	 * @param registroInvima 
	 * @return
	 */
	public static Collection buscar(Connection con, String clase, String grupo, String subgrupo, String codigo, String descripcion, String naturaleza, String minsalud, String formaFarmaceutica, String concentracion, String unidadMedida, boolean estadoArticulo, boolean buscarEstado, String stockMinimo, String stockMaximo, String puntoPedido, String costoPromedio, String precioCompraMasAlta, String codigoInterfaz,String indicativoAutomatico,String indicativoPorCompletar, String cantidadCompra, int categoria, String fechaCreacion, String registroInvima, String maximaCantidadMes,  String multidosis, String manejaLotes, String manejaFechaVencimiento, String porcentajeIva, String precioUltimaCompra, String precioBaseVenta, String fechaPrecioBaseVenta, int institucion, String descripcionAlterna, long consecutivoNivelAtencion)
	{
		String busquedaStr=buscarArticulosStr;
		if(!clase.equals(""))
		{
			busquedaStr+=" AND s.clase="+clase;
		}
		if(!grupo.equals(""))
		{
			busquedaStr+=" AND s.grupo="+grupo;
		}
		if(!subgrupo.equals(""))
		{
			busquedaStr+=" AND a.subgrupo="+subgrupo;
		}
		if(!codigo.equals(""))
		{
			
	       busquedaStr+=" AND a.codigo = "+codigo+" ";
	        
		}
		if(!descripcion.equals(""))
		{
			busquedaStr+=" AND UPPER(a.descripcion) LIKE UPPER('%"+descripcion+"%')";
		}
		if(!naturaleza.equals("")&&!naturaleza.equals("0"))
		{
			busquedaStr+=" AND a.naturaleza='"+naturaleza+"'";
		}
		if(!minsalud.equals(""))
		{
			busquedaStr+=" AND a.minsalud='"+minsalud+"'";
		}
		if(!formaFarmaceutica.equals(""))
		{
			busquedaStr+=" AND a.forma_farmaceutica='"+formaFarmaceutica+"'";
		}
		if(!concentracion.equals(""))
		{
			busquedaStr+=" AND UPPER(a.concentracion) LIKE UPPER('%"+concentracion+"%')";
		}
		if(!unidadMedida.equals(""))
		{
			busquedaStr+=" AND a.unidad_medida='"+unidadMedida+"'";
		}
		if(buscarEstado)
		{
			busquedaStr+=" AND a.estado=?";
		}
		if(!stockMinimo.equals(""))
		{
			busquedaStr+=" AND a.stock_minimo='"+stockMinimo+"'";
		}
		if(!stockMaximo.equals(""))
		{
			busquedaStr+=" AND a.stock_maximo='"+stockMaximo+"'";
		}
		if(!puntoPedido.equals(""))
		{
			busquedaStr+=" AND a.punto_pedido='"+puntoPedido+"'";
		}
		if(!cantidadCompra.equals(""))
		{
			busquedaStr+=" AND a.cantidad_compra='"+cantidadCompra+"'";
		}
		if(!costoPromedio.equals(""))
		{
			busquedaStr+=" AND a.costo_promedio='"+costoPromedio+"'";
		}
		if(!fechaCreacion.equals(""))
		{
			busquedaStr+=" AND a.fecha_modifica='"+fechaCreacion+"'";
		}
		if(categoria!=ConstantesBD.codigoNuncaValido)
		{
		    busquedaStr+=" AND categoria='"+categoria+"'";
		}
		if(!registroInvima.trim().equals(""))
		{
		    busquedaStr+=" AND  UPPER(a.registro_invima) LIKE UPPER('%"+registroInvima+"%')";
		}
		//
		if(!maximaCantidadMes.equals(""))
		{
			busquedaStr+=" AND a.maxima_cantidad_mes='"+maximaCantidadMes+"'";
		}		
		
		if(!multidosis.equals(""))
		{
			busquedaStr+=" AND a.multidosis='"+multidosis+"'";
		}	
		
		if(!manejaLotes.equals(""))
		{
			busquedaStr+=" AND a.maneja_lotes='"+manejaLotes+"'";
		}	
		
		if(!manejaFechaVencimiento.equals(""))
		{
			busquedaStr+=" AND a.maneja_fecha_vencimiento='"+manejaFechaVencimiento+"'";
		}	
		
		if(!porcentajeIva.equals(""))
		{
			busquedaStr+=" AND a.porcentaje_iva='"+porcentajeIva+"'";
		}	
		
		if(!precioUltimaCompra.equals(""))
		{
			busquedaStr+=" AND a.precio_ultima_compra='"+precioUltimaCompra+"'";
		}	
		
		if(!precioBaseVenta.equals(""))
		{
			busquedaStr+=" AND a.precio_base_venta='"+precioBaseVenta+"'";
		}	
		
		if(!fechaPrecioBaseVenta.equals(""))
		{
			busquedaStr+=" AND a.fecha_precio_base_venta='"+fechaPrecioBaseVenta+"'";
		}
		if(!indicativoAutomatico.equals("-1"))
		{
			busquedaStr+=" AND a.indicativo_automatico='"+indicativoAutomatico+"'";
		}
		if(!indicativoPorCompletar.equals("-1"))
		{
			busquedaStr+=" AND a.indicativo_por_completar='"+indicativoPorCompletar+"'";
		}
		if(!codigoInterfaz.equals(""))
		{
			busquedaStr+=" AND a.codigo_interfaz='"+codigoInterfaz+"'";
		}
		if(!precioCompraMasAlta.equals(""))
		{
			busquedaStr+=" AND a.precio_compra_mas_alta='"+precioCompraMasAlta+"'";
		}
		if(!descripcionAlterna.equals(""))
		{
			busquedaStr+=" AND UPPER(a.descripcion_alterna) LIKE UPPER('%"+descripcionAlterna+"%')";
		}
		/*if(descripcionAlterna.equals(""))
		{
			busquedaStr+=" AND UPPER(a.descripcion_alterna) LIKE UPPER('%"+descripcionAlterna+"%')";
		}*/
		if(consecutivoNivelAtencion!=ConstantesBD.codigoNuncaValido)
		{
			busquedaStr+=" AND a.nivel_atencion="+consecutivoNivelAtencion+"";
		}
		busquedaStr+=" ORDER BY a.descripcion";
		try
		{
			PreparedStatementDecorator busqueda= new PreparedStatementDecorator(con.prepareStatement(busquedaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info("BQ >>>>>>>>>>"+busquedaStr);
			
			if(buscarEstado)
			{
				busqueda.setBoolean(1,estadoArticulo);
			}
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(busqueda.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los articulos "+e);
			return null;
		}
	}

	
	/**
	 * Adición sebastián
	 * Método usado para insertar un atributo de la justificación de un artículo
	 * @param con
	 * @param numeroSolicitud
	 * @param articulo
	 * @param atributo
	 * @param descripcion
	 * @return
	 */
	public static int insertarAtributoJustificacion(Connection con,int numeroSolicitud,int articulo,int atributo,String descripcion)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(insertarAtributoJustificacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			pst.setInt(1,numeroSolicitud);
			pst.setInt(2,articulo);
			pst.setInt(3,atributo);
			pst.setString(4,descripcion);
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarAtributoJustificacion de SqlBaseArticulo: "+e);
			return -1;
		}
		
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @param institucion
	 * @return
	 */
	public static boolean articuloManejaFechaVencimiento(Connection con, int codigoArticulo, int institucion)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement("select maneja_fecha_vencimiento from articulo where codigo=? and institucion=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			pst.setInt(1,codigoArticulo);
			pst.setInt(2,institucion);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				return UtilidadTexto.getBoolean(rs.getString(1));
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en articuloManejaFechaVencimiento de SqlBaseArticulo: "+e);
			
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @param institucion
	 * @return
	 */
	public static boolean articuloManejaLote(Connection con, int codigoArticulo, int institucion)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement("select maneja_lotes from articulo where codigo=? and institucion=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			pst.setInt(1,codigoArticulo);
			pst.setInt(2,institucion);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				return UtilidadTexto.getBoolean(rs.getString(1));
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en articuloManejaLote de SqlBaseArticulo: "+e);
			
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static String existenciaArticulo(Connection con,String codigo) {
		try {
			
			int numRegs = 0;
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(existenciaArticuloStr));
			pst.setInt(1, Integer.parseInt(codigo)); 
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				numRegs= rs.getInt("existe"); 
				if (numRegs > 0)
				{
					return "0";
				}
				else
				{
					return "1";
				}
			}
			else
			{
				return "0";
			}

		}
		catch(SQLException e)
		{
			logger.error("Error en existenciaArticulo de SqlBaseArticuloDao: "+e);
			return "";
		}		
	}
	
	
	/**
	 * 	
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap cargarViasAdminArticulo(Connection con, int codigo) {
		try {

			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarViasAdminArticuloStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						
			pst.setInt(1, codigo);

			return UtilidadBD
					.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, false);
		} catch (SQLException e) {
			logger
					.error("Error en cargarViasAdminArticulo de SqlBaseArticulo: "
							+ e);
			return null;
		}
	}
	
	
	/**
	 * 
	 * @param con
	 * @param campos
	 * @param codigoArticulo
	 * @param seqInsertarViasAdm
	 * @param formaFarmaceutica
	 * @param institucion
	 * @throws SQLException
	 */
	private static void insertarViasAdministracionArticulo(Connection con, HashMap campos, int codigoArticulo, String seqInsertarViasAdm, String formaFarmaceutica,int institucion)
	{
		try
		{
			for(int i=0;i<Integer.parseInt(campos.get("numRegistros")+"");i++)
			{
				String consulta = insertarViasAdminArticuloStr
				+ " ( "
				+ seqInsertarViasAdm
				+ " , ?, ?, ?, ?,?)";
				
				if(campos.get("via_admin_"+i)!=null)
				{
					PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1, Integer.parseInt(campos.get("via_admin_"+i)+""));
					pst.setString(2, formaFarmaceutica);
					pst.setInt(3, codigoArticulo); 
					pst.setString(4, campos.get("activo_"+i)+"");
					pst.setInt(5,institucion);
					pst.executeUpdate();
					pst.close();
				}	
				
			}
		}
		catch (SQLException e) {
			logger.info("Error ingresando vias de administracion por artículo "+e);
		}
			return;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param campos
	 * @param codigoArticulo
	 * @throws SQLException
	 */
	private static void insertarGrupoEspecialArticulos(Connection con, HashMap campos, int codigoArticulo, String usuario) throws SQLException
	{
		for(int i=0;i<Utilidades.convertirAEntero(campos.get("numRegistros")+"");i++)
		{
			if(campos.get("grupoespecial_"+i)!=null)
			{
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(insertarGrupoEspecialArticuloStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setDouble(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_grupo_especial_X_articulo"));
				pst.setInt(2, codigoArticulo);
				pst.setDouble(3, Utilidades.convertirADouble(campos.get("grupoespecial_"+i)+""));
				pst.setString(4, campos.get("activo_"+i)+"");
				pst.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
				pst.setString(6, UtilidadFecha.getHoraActual());
				pst.setString(7, usuario);
				pst.executeUpdate();
			}
		}
		
		return;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param campos
	 * @param codigoArticulo
	 * @param usuario
	 * @throws SQLException
	 */
	public static void actualizarGrupoEspecialArticulo(Connection con, HashMap campos, int codigoArticulo, String usuario)throws SQLException
	{
		
		for(int i=0;i<Utilidades.convertirAEntero(campos.get("numRegistros")+"");i++)
		{
			String tipoReg = "";
			tipoReg = campos.get("tiporegistro_"+i)+"";
			if ( tipoReg.trim().equals("MEM"))
			{
				if(campos.get("grupoespecial_"+i)!=null)
				{
					PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(insertarGrupoEspecialArticuloStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setDouble(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_grupo_especial_X_articulo"));
					pst.setInt(2, codigoArticulo);
					pst.setDouble(3, Utilidades.convertirADouble(campos.get("grupoespecial_"+i)+""));
					pst.setString(4, campos.get("activo_"+i)+"");
					pst.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
					pst.setString(6, UtilidadFecha.getHoraActual());
					pst.setString(7, usuario);
					
					pst.executeUpdate();
				}
			}
			
			if ( tipoReg.trim().equals("BD"))
			{
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(actualizarGrupoEspecialArticuloStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setString(1, campos.get("activo_"+i)+"");
				pst.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
				pst.setString(3, UtilidadFecha.getHoraActual());
				pst.setString(4, usuario);
				pst.setDouble(5, Utilidades.convertirADouble(campos.get("codigo_"+i)+""));
				
				pst.executeUpdate();				
			}
					
			
		}
			return;
   }
	
	
	
	
	/**
	 * 
	 * @param con
	 * @param campos
	 * @param codigoArticulo
	 * @param formaFarmaceutica
	 * @param institucion
	 * @throws SQLException
	 */
	public static void actualizarViasAdministracionArticulo(Connection con, HashMap campos, int codigoArticulo, String formaFarmaceutica, int institucion, int TIPO_BD)throws SQLException
	{
		String consultaTemp="UPDATE inventarios.vias_admin_articulo SET activo='N' WHERE articulo="+codigoArticulo;
		PreparedStatementDecorator psTemp= new PreparedStatementDecorator(con.prepareStatement(consultaTemp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		psTemp.executeUpdate();
		
		String seqInsertarViasAdm="";
		for(int i=0;i<Integer.parseInt(campos.get("numRegistros")+"");i++)
		{
			String tipoReg = "";
			switch (TIPO_BD) {
			case DaoFactory.ORACLE:
				 seqInsertarViasAdm = "SEQ_VIA_ADM_ARTI.nextval ";
				break;
			case DaoFactory.POSTGRESQL:
				 seqInsertarViasAdm = "nextval('SEQ_VIA_ADM_ARTI')";
				break;

			default:
				break;
			}
			
			tipoReg = campos.get("tiporegistro_"+i)+"";
			if ( tipoReg.trim().equals("MEM"))
			{
				if(!UtilidadTexto.isEmpty(campos.get("via_admin_"+i)+""))
				{
					String consulta = insertarViasAdminArticuloStr
					+ " ( "
					+ seqInsertarViasAdm
					+ " , "+Utilidades.convertirAEntero(campos.get("via_admin_"+i)+"")+", '"+formaFarmaceutica+"', "+codigoArticulo+", '"+campos.get("activo_"+i)+"' ,"+institucion+")";
					
					logger.info("consulta --->"+consulta);
					PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.executeUpdate();
				}

			}
			
			if ( tipoReg.trim().equals("BD"))
			{
				if(formaFarmaceutica.trim().equals((campos.get("forma_farmaceutica_"+i)+"").trim()))
				{
					PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(actualizarViasAdminArticuloStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setString(1, campos.get("activo_"+i)+"".trim());
					pst.setInt(2, Integer.parseInt(campos.get("codigo_"+i)+""));
					pst.executeUpdate();				
				}
			}
					
			
		}
			return;
   }
	
	
	/**
	 * 
	 * @param con
	 * @param campos
	 * @param secuenciaGeneral
	 * @return
	 */
	public static boolean insertarViasAdminArticulo(Connection con, HashMap campos,String secuenciaGeneral) {
		try {
			String consulta = insertarViasAdminArticuloStr
					+ " ("
					+ secuenciaGeneral
					+ " , ?, ?, ?, ?)";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			pst.setInt(1, Integer.parseInt(campos.get("via_admin")+""));
			pst.setString(2, campos.get("forma_farmaceutica")+"");
			pst.setInt(3, Integer.parseInt(campos.get("articulo")+""));
			pst.setString(4, campos.get("activo")+"");
			

			return pst.executeUpdate()>0;
			
			
		} catch (SQLException e) {
			logger.error("Error en insertarViasAdminArticulo de SqlBaseArticulo: " + e);
			return false;
		}
	}
	
	
	/**
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean actualizarViasAdminArticulo(Connection con, HashMap campos) {
		try {
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(actualizarViasAdminArticuloStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			pst.setString(1, campos.get("activo")+"");
			pst.setInt(2, Integer.parseInt(campos.get("codigo")+""));
			
			return pst.executeUpdate()>0;
			
			
		} catch (SQLException e) {
			logger.error("Error en actualizarViasAdminArticulo de SqlBaseArticulo: " + e);
			return false;
		}
	}

	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap cargarUnidosisArticulo(Connection con, int codigo) {
		try {
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarUnidosisArticuloStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						
			pst.setInt(1, codigo);

			return UtilidadBD
					.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, false);
		} catch (SQLException e) {
			logger
					.error("Error en cargarUnidosisArticulo de SqlBaseArticulo: "
							+ e);
			return null;
		}
	}

	
	/**
	 * 
	 * @param con
	 * @param campos
	 * @param codigoArticulo
	 * @param seqInsertarUniMed
	 * @throws SQLException
	 */
	private static void insertarUnidosisArticulo(Connection con, HashMap campos, int codigoArticulo, String seqInsertarUniMed)throws SQLException
	{
		for(int i=0;i<Integer.parseInt(campos.get("numRegistros")+"");i++)
		{
			String consulta = insertarUnidosisArticuloStr
			+ " ( "
			+ seqInsertarUniMed
			+ " , ?, ?, ?, ?)";
			
			if(campos.get("unidad_medida_"+i)!=null)
			{
			
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				pst.setInt(1, codigoArticulo);
				pst.setString(2, campos.get("unidad_medida_"+i)+"");
				//pst.setInt(3, Integer.parseInt(campos.get("cantidad_"+i)+""));
				String vlrTmpcant = "";
				vlrTmpcant=campos.get("cantidad_"+i)+"";
			
				if ( vlrTmpcant==null || vlrTmpcant.equals("") )
				{ 
					pst.setNull(3, Types.DOUBLE);
					
				}
				else
				{
					pst.setDouble(3, Double.parseDouble(campos.get("cantidad_"+i)+""));						
				}	
				
				/*if(!vlrTmpcant.equals(""))
				{
					pst.setDouble(3, Double.parseDouble(campos.get("cantidad_"+i)+""));
				}
				else
				{
					pst.setDouble(3, Types.DOUBLE);
				}
				*/


				pst.setString(4, campos.get("activounidosis_"+i)+"");
				
				pst.executeUpdate();
			}
		}
			return;
	}

	
	/**
	 * 
	 * @param con
	 * @param campos
	 * @param codigoArticulo
	 * @throws SQLException
	 */
	public static void actualizarUnidadesMedidaArticulo(Connection con, HashMap campos, int codigoArticulo, String seqInsertarUnidosisArt)throws SQLException
	{
		
		for(int i=0;i<Integer.parseInt(campos.get("numRegistros")+"");i++)
		{
			String tipoReg = "";
			tipoReg = campos.get("tiporegistro_"+i)+"";
			if ( tipoReg.trim().equals("MEM"))
			{
				String consulta = insertarUnidosisArticuloStr
				+ " ( "
				+ seqInsertarUnidosisArt
				+ " , ?, ?, ?, ?)";
				
				if(campos.get("unidad_medida_"+i)!=null)
				{
				
					PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					pst.setInt(1, codigoArticulo);
					pst.setString(2, campos.get("unidad_medida_"+i)+"");
					String vlrTmpcant = "";
					vlrTmpcant=campos.get("cantidad_"+i)+"";
					
					
					//if((campos.get("cantidad_"+i)==null)&&(!campos.get("cantidad_"+i).toString().equals("")))
					if ( vlrTmpcant==null || vlrTmpcant.equals("") )
					{ 
						pst.setNull(3, Types.DOUBLE);
						
					}
					else
					{
						pst.setDouble(3, Double.parseDouble(campos.get("cantidad_"+i)+""));						
					}
					
					//pst.setDouble(3, Double.parseDouble(campos.get("cantidad_"+i)+""));
					pst.setString(4, campos.get("activounidosis_"+i)+"");
					
					pst.executeUpdate();
				}
			}
			
			if ( tipoReg.trim().equals("BD"))
			{
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(actualizarUnidosisArticuloStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				pst.setString(1, campos.get("activounidosis_"+i)+"".trim());
				//pst.setDouble(2, Double.parseDouble(campos.get("cantidad_"+i)+""));
				String vlrTmpcant = "";
				vlrTmpcant=campos.get("cantidad_"+i)+"";
				
				if ( vlrTmpcant==null || vlrTmpcant.equals("") )
				{ 
					pst.setNull(2, Types.DOUBLE);
					
				}
				else
				{
					pst.setDouble(2, Double.parseDouble(campos.get("cantidad_"+i)+""));						
				}				
				/*if(!vlrTmpcant.equals(""))
				{
					pst.setDouble(2, Double.parseDouble(campos.get("cantidad_"+i)+""));
				}
				else
				{
					//pst.setDouble(2, Types.DOUBLE);
					pst.setNull(2, Types.DOUBLE);
				}*/				
				
				pst.setInt(3, Integer.parseInt(campos.get("codigo_"+i)+""));
				
				pst.executeUpdate();				
			}
					
			
		}
			return;
   }
	
	
	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @return
	 */
	public static String obtenerCodigoInterfazNaturalezaArticulo(Connection con, int codigoArticulo)
	{
		String consulta= "SELECT coalesce(na.codigo_interfaz||'','') as interf FROM articulo a INNER JOIN naturaleza_articulo na ON (a.naturaleza= na.acronimo and na.institucion=a.institucion) WHERE a.codigo=?";
		
		try 
		{	
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoArticulo);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("interf");
		}
		catch (SQLException e) 
		{
			logger.error("Error insertando el estado de la cuenta en la tabla de proceso de facturación"+ e);
			e.printStackTrace();
		}
		return "";
	}
	
	
	/**
	 * 
	 * @param codigoArticulo
	 * @return
	 */
	public static boolean esMedicamento(int codigoArticulo,String consulta)
	{
		boolean retorna=true;
		Connection con=UtilidadBD.abrirConexion();
		try 
		{	
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("---->"+consulta+"--"+codigoArticulo);
			ps.setInt(1, codigoArticulo);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				retorna= UtilidadTexto.getBoolean(rs.getString(1));
		}
		catch (SQLException e) 
		{
			logger.error("Error esMedicamento"+ e);
			e.printStackTrace();
		}
		UtilidadBD.closeConnection(con);
		return retorna;
	}

	
	/**
	 * 
	 * @param codigoArticulo
	 * @return
	 */
	public static boolean esPos(int codigoArticulo)
	{
		String consulta="SELECT getespos(?)";
		boolean retorna=true;
		Connection con=UtilidadBD.abrirConexion();
		try 
		{	
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoArticulo);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				retorna= UtilidadTexto.getBoolean(rs.getString(1));
		}
		catch (SQLException e) 
		{
			logger.error("Error esMedicamento"+ e);
			e.printStackTrace();
		}
		UtilidadBD.closeConnection(con);
		return retorna;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param a
	 * @return
	 */
	public static HashMap consultarUbicacion (Connection con, Articulo a)
	{
	
		String cadena=buscoubicacionStr;
		
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
	
		logger.info("Sqlbase a.getCodigo()---->	"+a.getCodigo());
		
		if(Integer.parseInt(a.getCodigo())>0)
			cadena+= " AND  det.articulo= "+a.getCodigo();

		
		logger.info("Sentencia Sql:		\n"+cadena);
	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
	 * @param codigoArticulo
	 * @return
	 */
	public static boolean esArticuloMultidosis(Connection con, int codigoArticulo) 
	{
		String cadena="SELECT multidosis from articulo where codigo=?";
		try 
		{	
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoArticulo);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return UtilidadTexto.getBoolean(rs.getString(1));
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @return
	 */
	public static HashMap<String, Object> consultaInfoMArticulo (Connection con, int codigoArticulo)
	{
		HashMap<String, Object> resultadosIMA = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStrIMA, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));			
			pst.setInt(1, codigoArticulo);
			
			logger.info("consultaInfoMArticulo / "+consultaStrIMA);
			logger.info("Param 1 - "+codigoArticulo);
			
			resultadosIMA = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true, true);
			resultadosIMA.put("INDICES",indicesMapIMA);
			
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de Informacion Medica de un Articulo");
		}
		return resultadosIMA;
	}
	
	/**
	 * Método encargado de consultar los 4 códigos minSalud para un articulo tipo medicamento
	 * @author Felipe Pérez Granda
	 * @param con Conexión 
	 * @param codigoArticulo El código del artículo
	 * @return HashMap<String,Object>
	 */
	public static HashMap<String,Object> cargarMinSalud(Connection con, int codigoArticulo)
	{
		logger.info("===> Entre a cargarMinSalud !!!, codigoArticulo = "+codigoArticulo);
		String consulta = "SELECT minsalud FROM articulo WHERE codigo = "+codigoArticulo+" ";
		logger.info("===> La consulta es: "+consulta);
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
	        ps.close();
			return mapaRetorno;		
		}
		catch (SQLException e)
		{
			logger.error("===> Problema consultando los codigos minsalud de Artículo "+e);
		}
		return null;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @return
	 */
	public static HashMap<String, Object> consultaInfoMAdjArticulo (Connection con, int codigoArticulo)
	{
		HashMap<String, Object> resultadosIMA = new HashMap<String, Object>();
		
		PreparedStatementDecorator pst;
		
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStrIMAAD, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));			
			pst.setInt(1, codigoArticulo);
			
			resultadosIMA = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true, true);
			
			resultadosIMA.put("INDICES",indicesMapIMAAD);
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en consulta de los documentos aAdjuntos de un Articulo");
		}
		return resultadosIMA;
	}
	
	
	/**
	 * Metoto para eliminar un registro de la tabla docs adjuntos de un articulo por info medica
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean eliminarAdjIM(Connection con, String codigo)
	{
		PreparedStatementDecorator pst;
		
		try{
			pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarAdjIM, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));			
			pst.setString(1, codigo);
			
			if(pst.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			logger.error(e+" Error eliminando un Documento Adjunto del Articulo seleccionado.");
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @return
	 */
	public static HashMap cargarGrupoEspecialesArticulo(Connection con, int codigoArticulo) 
	{
		
		try 
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarGruposEspecialesArticuloStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						
			pst.setInt(1, codigoArticulo);

			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
			
		} 
		catch (SQLException e) 
		{
			logger.error("Error en cargarUnidosisArticulo de SqlBaseArticulo: "	+ e);
			return null;
		}
		
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @return
	 */
	public static String cargarDescripcionNivelAtencion(Connection con, int codigoArticulo) 
	{
		
		try 
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarDescripcionNivelAtencion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						
			pst.setInt(1, codigoArticulo);

			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				return rs.getString("descripcionnivel");
			pst.close();
			
			return "";
			
		} 
		catch (SQLException e) 
		{
			logger.error("Error en cargarDescripcionNivelAtencion de SqlBaseArticulo: "	+ e);
			return null;
		}
		
	}
	
	
}