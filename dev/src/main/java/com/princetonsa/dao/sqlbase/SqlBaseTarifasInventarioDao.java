/*
 * SqlBaseTarifasInventarioDao.java 
 * Autor			:  Miguel Díaz
 * Creado el	:  14-Mar-2005
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;


/**
 * 
 * @author Miguel Díaz
 *
 * Princeton S.A.
 */
public class SqlBaseTarifasInventarioDao {

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseTarifasInventarioDao.class);
	
	/**
	 * Modificar tarifas de inventario
	 */
	private static final String modificarTarifaInventarioStr = "UPDATE tarifas_inventario SET valor_tarifa = ?, porcentaje_iva= ?, porcentaje= ?, actualiz_automatic= ?, tipo_tarifa= ?, usuario_modifica=?, fecha_modifica=?, hora_modifica=?, fecha_vigencia=? WHERE codigo = ? ";
	
	/**
	 * Sentencia SQL para eliminar una tarifa de inventario
	 */
	private static final String eliminarTarifaInventarioStr = "DELETE FROM tarifas_inventario WHERE codigo = ? "; 
	
	/**
	 * Sentencia para realizar la búsqueda avanzada
	 */
	private static final String buscarTarifaInventarioStr = "SELECT " +
																"ti.codigo AS codigo, " +
																"ti.esquema_tarifario AS esquemaTarifario, " +
																"ti.articulo AS articulo, " +
																"a.descripcion AS descripcionArticulo, " +
																"getnomnaturalezaarticulo(a.naturaleza) AS naturalezaArticulo, " +
																"getnomformafarmaceutica(a.forma_farmaceutica) AS formaFarmaceutica, " +
																"coalesce(a.concentracion,'') AS concentracionArticulo, " +
																"getunidadmedidaarticulo(ti.articulo) AS unidadmedida, " +
																"ti.valor_tarifa AS tarifa, " +
																"ti.porcentaje_iva AS iva, " +
																"coalesce(ti.porcentaje||'', '') AS porcentaje, " +
																"coalesce(ti.actualiz_automatic||'', '') AS actualizAutomatic, " +
																"ti.tipo_tarifa AS tipoTarifa, " +
																"getintegridaddominio(ti.tipo_tarifa) as nombreTipoTarifa, " +
																"coalesce(to_char(ti.fecha_vigencia,'dd/mm/yyyy'),'') AS fechavigencia " +
															"from " +
																"articulo a  " +
																"INNER JOIN tarifas_inventario ti ON(a.codigo=ti.articulo) " +
															"WHERE " +
																"ti.esquema_tarifario=? ";
	
	/**
	 * Sentencia para la busqueda de las tarifas existentes por esquema articulo
	 */
	private static String postgresBuscarTodasTarifasInventarioStr=	"(" +
																"SELECT " +
																	"ti.codigo AS codigo, " +
																	"ti.esquema_tarifario AS esquematarifario, " +
																	"ti.articulo AS codarticulo, " +
																	"ti.esquema_tarifario AS esquematarifario, " +
																	"a.descripcion AS descripcionarticulo, " +
																	"getnomnaturalezaarticulo(a.naturaleza) AS naturalezaarticulo, " +
																	"getnomformafarmaceutica(a.forma_farmaceutica) AS formafarmaceutica, " +
																	"coalesce(a.concentracion,'') AS concentracionarticulo, " +
																	"getunidadmedidaarticulo(ti.articulo) AS unidadmedida, " +
																	"ti.valor_tarifa AS valortarifa, " +
																	"ti.porcentaje_iva AS porcentajeiva, " +
																	"coalesce(ti.porcentaje||'', '') AS porcentaje, " +
																	"coalesce(ti.actualiz_automatic||'', '') AS actualizautomatic, " +
																	"ti.tipo_tarifa AS tipotarifa, " +
																	"getintegridaddominio(ti.tipo_tarifa) AS nombretipotarifa, " +
																	"ti.fecha_vigencia AS fechavigencia " +
																"FROM tarifas_inventario ti " +
																	"INNER JOIN articulo a ON (a.codigo=ti.articulo) " +
																"WHERE ti.esquema_tarifario=? AND ti.articulo=? and ti.fecha_vigencia<=CURRENT_DATE ORDER BY ti.fecha_vigencia DESC "+ValoresPorDefecto.getValorLimit1()+" 1 " +
															")" +
															"UNION ALL " +
															"(" +
																"SELECT " +
																	"ti.codigo AS codigo, " +
																	"ti.esquema_tarifario AS esquematarifario, " +
																	"ti.articulo AS codarticulo, " +
																	"ti.esquema_tarifario AS esquematarifario, " +
																	"a.descripcion AS descripcionarticulo, " +
																	"getnomnaturalezaarticulo(a.naturaleza) AS naturalezaarticulo, " +
																	"getnomformafarmaceutica(a.forma_farmaceutica) AS formafarmaceutica, " +
																	"coalesce(a.concentracion,'') AS concentracionarticulo, " +
																	"getunidadmedidaarticulo(ti.articulo) AS unidadmedida, " +
																	"ti.valor_tarifa AS valortarifa, " +
																	"ti.porcentaje_iva AS porcentajeiva, " +
																	"coalesce(ti.porcentaje||'', '') AS porcentaje, " +
																	"coalesce(ti.actualiz_automatic||'', '') AS actualizautomatic, " +
																	"ti.tipo_tarifa AS tipotarifa, " +
																	"getintegridaddominio(ti.tipo_tarifa) AS nombretipotarifa, " +
																	"ti.fecha_vigencia AS fechavigencia " +
																"FROM tarifas_inventario ti " +
																	"INNER JOIN articulo a ON (a.codigo=ti.articulo) " +
																"WHERE ti.esquema_tarifario=? AND ti.articulo=? and ti.codigo not in(select ti1.codigo from tarifas_inventario ti1 where ti1.esquema_tarifario=ti.esquema_tarifario AND ti1.articulo=ti.articulo and ti1.fecha_vigencia<=CURRENT_DATE ORDER BY ti1.fecha_vigencia DESC "+ValoresPorDefecto.getValorLimit1()+" 1) ORDER BY ti.fecha_vigencia DESC " +
															")";
	
	
	
	private static String oracleBuscarTodasTarifasInventarioStr=	"(" +
																"SELECT " +
																	"ti.codigo AS codigo, " +
																	"ti.esquema_tarifario AS esquematarifario, " +
																	"ti.articulo AS codarticulo, " +
																	"ti.esquema_tarifario AS esquematarifario, " +
																	"a.descripcion AS descripcionarticulo, " +
																	"inventarios.getnomnaturalezaarticulo(a.naturaleza) AS naturalezaarticulo, " +
																	"inventarios.getnomformafarmaceutica(a.forma_farmaceutica) AS formafarmaceutica, " +
																	"coalesce(a.concentracion,'') AS concentracionarticulo, " +
																	"inventarios.getunidadmedidaarticulo(ti.articulo) AS unidadmedida, " +
																	"ti.valor_tarifa AS valortarifa, " +
																	"ti.porcentaje_iva AS porcentajeiva, " +
																	"coalesce(ti.porcentaje||'', '') AS porcentaje, " +
																	"coalesce(ti.actualiz_automatic||'', '') AS actualizautomatic, " +
																	"ti.tipo_tarifa AS tipotarifa, " +
																	"getintegridaddominio(ti.tipo_tarifa) AS nombretipotarifa, " +
																	"ti.fecha_vigencia AS fechavigencia " +
																"FROM tarifas_inventario ti " +
																	"INNER JOIN articulo a ON (a.codigo=ti.articulo) " +
																"WHERE ti.esquema_tarifario=? AND ti.articulo=? " +
																	"and ti.fecha_vigencia<= (select to_date(to_char(sysdate,'ddmmyyyy'),'ddmmyyyy') from dual) and rownum = 1 " +
															")" +
															"UNION ALL " +
															"(" +
																"SELECT " +
																	"ti.codigo AS codigo, " +
																	"ti.esquema_tarifario AS esquematarifario, " +
																	"ti.articulo AS codarticulo, " +
																	"ti.esquema_tarifario AS esquematarifario, " +
																	"a.descripcion AS descripcionarticulo, " +
																	"inventarios.getnomnaturalezaarticulo(a.naturaleza) AS naturalezaarticulo, " +
																	"getnomformafarmaceutica(a.forma_farmaceutica) AS formafarmaceutica, " +
																	"coalesce(a.concentracion,'') AS concentracionarticulo, " +
																	"inventarios.getunidadmedidaarticulo(ti.articulo) AS unidadmedida, " +
																	"ti.valor_tarifa AS valortarifa, " +
																	"ti.porcentaje_iva AS porcentajeiva, " +
																	"coalesce(ti.porcentaje||'', '') AS porcentaje, " +
																	"coalesce(ti.actualiz_automatic||'', '') AS actualizautomatic, " +
																	"ti.tipo_tarifa AS tipotarifa, " +
																	"getintegridaddominio(ti.tipo_tarifa) AS nombretipotarifa, " +
																	"ti.fecha_vigencia AS fechavigencia " +
																"FROM tarifas_inventario ti " +
																	"INNER JOIN articulo a ON (a.codigo=ti.articulo) " +
																"WHERE ti.esquema_tarifario=? AND ti.articulo=? and ti.codigo not in" +
																"(select ti1.codigo from tarifas_inventario ti1 where ti1.esquema_tarifario=ti.esquema_tarifario AND " +
																"ti1.articulo=ti.articulo and ti1.fecha_vigencia<= (select to_date(to_char(sysdate,'ddmmyyyy'),'ddmmyyyy') from dual) " +
																"and rownum = 1) )";

	/**
	 * Sentencia para cargar las tarifas de inventario
	 */
	private static final String cargarTarifaInventarioStr="SELECT " +
														"codigo AS codigo, " +
														"valor_tarifa AS valor, " +
														"porcentaje_iva AS iva , " +
														"porcentaje AS porcentaje, " +
														"actualiz_automatic AS actualizAutomatic, " +
														"tipo_tarifa AS tipoTarifa," +
														"coalesce(usuario_modifica,'') AS usuario_modifica," +
														"coalesce(to_char(fecha_modifica,'"+ConstantesBD.formatoFechaAp+"'),'') AS fecha_modifica," +
														"coalesce(substr(hora_modifica,0,6),'') AS hora_modifica " +
														"from tarifas_inventario " +
														"WHERE articulo=? AND esquema_tarifario=?";

	/**
	 * Statement de inserción para el log de base de datos
	 */
	private static final String cadenaLogStr = "INSERT INTO log_tarifas_inventario (codigo, articulo, tipo_tarifa, porcentaje, valor_tarifa, usuario, fecha, hora, actualiz_automatic, esquema_tarifario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	/**
	 * Metodo que consulta todas las Tarifas Inventario por esquema articulo
	 * @param con
	 * @param esquemaTarifario
	 * @param codArticulo
	 * @param tipoBD 
	 * @return
	 */
	public static HashMap consultarTodasTarifasInventarios(Connection con, String esquemaTarifario, String codArticulo, int tipoBD)
	{
		HashMap<String, Object> resultado=new HashMap<String, Object>();
		
		String buscarTodasTarifasInventarioStr = "";
		
		switch(tipoBD){
			case DaoFactory.ORACLE:
				buscarTodasTarifasInventarioStr = oracleBuscarTodasTarifasInventarioStr;
				break;
			case DaoFactory.POSTGRESQL:
				buscarTodasTarifasInventarioStr = postgresBuscarTodasTarifasInventarioStr;
				break;
			default:
				break;
		}
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(buscarTodasTarifasInventarioStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setString(1, esquemaTarifario);
			ps.setString(2, codArticulo);
			ps.setString(3, esquemaTarifario);
			ps.setString(4, codArticulo);
			
			resultado=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true, true);
		} 
		catch (SQLException e)
		{
			logger.info("\n\n ERROR. CONSULTANDO TODAS TARIFAS POR ESQUEMA ARTICULO>>>>>"+e);
		}
		return resultado;
	}

	/**
	 * Método para modificar las tarifas de inventario
	 * @param con
	 * @param codigo
	 * @param valor_tarifa
	 * @param porcentaje_iva
	 * @param fechaVigencia
	 * @return Número de rgistros modificados
	 */
	public static int modificar(Connection con, int codigo, double valor_tarifa, double porcentaje_iva, double porcentaje, String actualiz_automatic, String tipo_tarifa, String usuario_modifica, String fecha_modifica, String hora_modifica, int esquema_tarifario, String fechaVigencia){
		int resultado = 0; 		
	
		int insertoBasico=ConstantesBD.codigoNuncaValido;
		int insertoLog=ConstantesBD.codigoNuncaValido;
		try
		{
			
			PreparedStatementDecorator modificar =  new PreparedStatementDecorator(con.prepareStatement(modificarTarifaInventarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			modificar.setDouble(1, valor_tarifa);
			modificar.setDouble(2, porcentaje_iva);
			
			if(tipo_tarifa.trim().equals(ConstantesIntegridadDominio.acronimoValorFijo))
			{
				modificar.setObject(3, null);
			}
			else
			{	
				modificar.setDouble(3, porcentaje);
			}
			if (UtilidadTexto.isEmpty(actualiz_automatic))
			{
				modificar.setObject(4, null);
			}
			else
			{
				modificar.setString(4, actualiz_automatic);
			}
			modificar.setString(5, tipo_tarifa);
			modificar.setString(6, usuario_modifica);
			modificar.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			modificar.setString(8, UtilidadFecha.getHoraActual());
			if(!fechaVigencia.equals(""))
				modificar.setString(9, UtilidadFecha.conversionFormatoFechaABD(fechaVigencia));
			else
				modificar.setNull(9, Types.DATE);
			modificar.setInt(10, codigo);
			
			insertoBasico = modificar.executeUpdate();
			
			if(insertoBasico>0)
			{
				insertoLog=logTarifasInventario(con, obtenerCodigoArticulo(con, codigo), tipo_tarifa, porcentaje, valor_tarifa, usuario_modifica, fecha_modifica, hora_modifica, actualiz_automatic, esquema_tarifario);
			}
			
			if(insertoBasico>0 && insertoLog>0)
			{
				return insertoBasico;
			}
			if( resultado <= 0 )
			{
				logger.error("Error modificando Tarifas Inventario [ Tabla: tarifas_inventario ] --> ningún registro fue modificado ");
			}
		}
		catch (SQLException e){
			logger.error("Error modificando Tarifas Inventario [ Tabla: tarifas_inventario ] " + codigo + ": " + e);
		}
		
		return resultado;
	}

	/**
	 * Método para eliminar una tarifa de inventario
	 * @param con
	 * @param codigo
	 * @return Número de registros eliminados
	 */
	public static int eliminar(Connection con, int codigo)
	{
		int resultado = 0; 		
		try
		{
			PreparedStatementDecorator eliminar =  new PreparedStatementDecorator(con.prepareStatement(eliminarTarifaInventarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			eliminar.setInt(1, codigo);
			resultado = eliminar.executeUpdate();
			if( resultado <= 0 ){
				logger.error("Error eliminando Tarifas Inventario [ Tabla: tarifas_inventario ] --> ningún registro fue eliminado ");
			}
		}
		catch (SQLException e){
			logger.error("Error modificando Tarifas Inventario [ Tabla: tarifas_inventario ] " + codigo + ": " + e);
		}
		return resultado;
	}

	/**
	 * Método para cargar una tarifa de inventario dado el código del articulo
	 * y el esquema tarifario
	 * @param con conexión con la BD
	 * @param codigoArticulo Código del articulo 
	 * @param esquemaTarifario Código del esquema tarifario
	 * @return Coleccion con los datos
	 */
	public static Collection cargar(Connection con, int articulo, int esquemaTarifario)
	{
		try{
			PreparedStatementDecorator cargarStm =  new PreparedStatementDecorator(con.prepareStatement(cargarTarifaInventarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStm.setInt(1, articulo);
			cargarStm.setInt(2, esquemaTarifario);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(cargarStm.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error cargando la tarifa de inventarios para el artículo "+articulo+": "+e);
			return null;
		}
	}
	
	/**
	 * Método para insertar una nueva tarifa en la BD
	 * @param con Conexión con la BD
	 * @param esquemaTarifario Esquema tarifario para la tarifa
	 * @param articulo Código del articulo
	 * @param tarifa Valor de la tarifa
	 * @param iva Porcentaje del Iva aplicado a la taria
	 * @param sentenciaSql Sentencia de inserción en la BD
	 * @param fechaVigencia
	 * @return numero de registros insertados
	 */
	public static int insertar(Connection con, int esquemaTarifario, int articulo, double tarifa, double iva, double porcentaje, String actualizAutomatic, String tipoTarifa, String usuarioModifica, String fechaModifica, String horaModifica, String sentenciaSql, String fechaVigencia)
	{
		int insertoBasico=ConstantesBD.codigoNuncaValido;
		int insertoLog=ConstantesBD.codigoNuncaValido;
		try{
			
			PreparedStatementDecorator insert =  new PreparedStatementDecorator(con.prepareStatement(sentenciaSql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insert.setInt(1, esquemaTarifario);
			insert.setInt(2, articulo);
			insert.setDouble(3, tarifa);
			insert.setDouble(4, iva);
			insert.setDouble(5, porcentaje);
			
			if (UtilidadTexto.isEmpty(actualizAutomatic))
			{
				insert.setObject(6, null);
			}
			else
			{
				insert.setString(6, actualizAutomatic);
			}
			
			insert.setString(7, tipoTarifa);
			insert.setString(8,usuarioModifica);
			insert.setDate(9, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			insert.setString(10, UtilidadFecha.getHoraActual());
			if(!fechaVigencia.equals(""))
				insert.setString(11, UtilidadFecha.conversionFormatoFechaABD(fechaVigencia));
			else
				insert.setNull(11, Types.DATE);
			insertoBasico= insert.executeUpdate();
			if(insertoBasico>0)
			{
				insertoLog=logTarifasInventario(con, articulo, tipoTarifa, porcentaje, tarifa, usuarioModifica, fechaModifica, horaModifica, actualizAutomatic, esquemaTarifario);
			}
			
			if(insertoBasico>0 && insertoLog>0)
			{
				return insertoBasico;
			}
		}
		catch(SQLException e)
		{
			logger.error("Error insertando la tarifa de inventarios: "+e);
		}
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * Método para buscar una tarifa de inventario
	 * @param con
	 * @param articulo
	 * @param esquemaTarifario
	 * @param tarifa
	 * @param iva
	 * @param porcentaje
	 * @param actualizAutomatic
	 * @param tipoTarifa
	 * @param tipoBD 
	 * @return Collection con listado de tarifas que cumplen con los criterios de búsqueda
	 */
	public static Collection buscar(Connection con, String articulo, String descripcionAtriculo, String naturalezaArticulo, String formaFarmaceutica, String concentracionArticulo, int esquemaTarifario, double tarifa, double iva, double porcentaje, String actualizAutomatic, String tipoTarifa, int institucion, String remite, int tipoBD)
	{
		//***************INICIO CAMBIO TAREA 38488 *********************************
		String codigoArticuloBusqueda = ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(institucion);
		logger.info("===>Tipo de Codigo Articulo para Busqueda: "+codigoArticuloBusqueda);
		
		String busquedaStr = buscarTarifaInventarioStr;
		
		if(UtilidadCadena.noEsVacio(articulo))
		{
			if(codigoArticuloBusqueda.equals(ConstantesIntegridadDominio.acronimoAxioma))
				busquedaStr += " AND ti.articulo = "+Utilidades.convertirAEntero(articulo);
			else if(codigoArticuloBusqueda.equals(ConstantesIntegridadDominio.acronimoInterfaz))
				busquedaStr += " AND a.codigo_interfaz = '"+articulo+"'";
		}
		//***************FIN CAMBIO TAREA 38488 ***********************************
		
		if(tarifa!=0)
		{
			busquedaStr += " AND ti.valor_tarifa="+tarifa;
		}
		if(iva!=0)
		{
			busquedaStr += " AND ti.porcentaje_iva="+iva;
		}
		if(!UtilidadTexto.isEmpty(tipoTarifa))
		{
			busquedaStr += " AND ti.tipo_tarifa = '"+tipoTarifa+"' ";
		}
		if(!UtilidadTexto.isEmpty(actualizAutomatic))
		{
			busquedaStr += " AND ti.actualiz_automatic = '"+actualizAutomatic+"' ";
		}
		
		if(descripcionAtriculo!=null)
		{
			if(!descripcionAtriculo.equals(""))
			{
				busquedaStr += " AND UPPER(a.descripcion) LIKE UPPER('%"+descripcionAtriculo+"%')";
			}
		}
		if(remite.equals("consulta"))

			switch(tipoBD){
			case DaoFactory.ORACLE:
				busquedaStr+=" AND ((getObtenerFechaVigenciaInvent(ti.esquema_tarifario,ti.articulo) IS NOT NULL " +
							"AND ti.fecha_vigencia=(TO_DATE(getObtenerFechaVigenciaInvent(ti.esquema_tarifario,ti.articulo)))) " +
								"OR (getObtenerFechaVigenciaInvent(ti.esquema_tarifario,ti.articulo) IS NULL AND ti.fecha_vigencia IS NULL)) " +
									"ORDER BY ti.articulo, ti.fecha_vigencia DESC ";
				break;
			case DaoFactory.POSTGRESQL:
				busquedaStr+=" AND ((getObtenerFechaVigenciaInvent(ti.esquema_tarifario,ti.articulo) IS NOT NULL " +
							"AND ti.fecha_vigencia=(getObtenerFechaVigenciaInvent(ti.esquema_tarifario,ti.articulo)::date)) " +
								"OR (getObtenerFechaVigenciaInvent(ti.esquema_tarifario,ti.articulo) IS NULL AND ti.fecha_vigencia IS NULL)) " +
									"ORDER BY ti.articulo, ti.fecha_vigencia DESC ";
				break;
			}
			
		if(remite.equals("impresion"))
			busquedaStr+=" ORDER BY ti.articulo,ti.fecha_vigencia DESC";
		
		try
		{logger.info("busquedaStr-------->>>>>>>>>>>>>>>>>>"+busquedaStr+"\n " +
				"" +
				"esquemaTarifario"+esquemaTarifario);
			PreparedStatementDecorator buscarStm= new PreparedStatementDecorator(con.prepareStatement(busquedaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			buscarStm.setInt(1, esquemaTarifario);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(buscarStm.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error en la búsqueda de tarifas de inventario "+e);
			return null;
		}
	}
	
	/**
	 * Metodo para consultar las fechas vigencia segun esquema tarifario y articulo
	 * @param con
	 * @param esquemaTarifario
	 * @param articulo
	 * @param cadena codigos Articulo
	 * @return
	 */
	public static HashMap consultarFechasVigencia(Connection con, String esquemaTarifario, String articulo, String cadenaCodigosArticulos)
	{
		logger.info("\n\nCADENA CODIGOS SERVICIOS>>>>>>>>>>>>>"+cadenaCodigosArticulos);
		HashMap<String, Object> resultados=new HashMap<String, Object>();
		String cadena="SELECT DISTINCT " +
							"ti.codigo AS codigo, " +
							"ti.esquema_tarifario AS esquematarifario, " +
							"ti.articulo AS articulo, " +
							"a.descripcion AS descripcionarticulo, " +
							"getnomnaturalezaarticulo(a.naturaleza) AS naturalezaarticulo, " +
							"getnomformafarmaceutica(a.forma_farmaceutica) AS formafarmaceutica, " +
							"coalesce(a.concentracion,'') AS concentracionarticulo, " +
							"getunidadmedidaarticulo(ti.articulo) AS unidadmedida, " +
							"ti.valor_tarifa AS tarifa, " +
							"ti.porcentaje_iva AS iva, " +
							"coalesce(ti.porcentaje||'', '') AS porcentaje, " +
							"coalesce(ti.actualiz_automatic||'', '') AS actualizautomatic, " +
							"ti.tipo_tarifa AS tipotarifa, " +
							"getintegridaddominio(ti.tipo_tarifa) as nombretipotarifa, " +
							"ti.fecha_vigencia AS fechavigencia " +
						"FROM articulo a " +
							"INNER JOIN tarifas_inventario ti ON(a.codigo=ti.articulo) " +
						"WHERE ";
		cadena+="ti.esquema_tarifario="+esquemaTarifario;
		if(!articulo.equals(""))
			cadena+=" AND ti.articulo="+articulo+" ORDER BY ti.fecha_vigencia DESC ";
		else
			cadena+=" AND ti.articulo IN ("+cadenaCodigosArticulos+") GROUP BY ti.esquema_tarifario," +
						"ti.articulo," +
						"ti.codigo," +
						"a.descripcion," +
						"a.naturaleza," +
						"a.forma_farmaceutica," +
						"a.concentracion," +
						"ti.valor_tarifa," +
						"ti.porcentaje_iva," +
						"ti.porcentaje," +
						"ti.fecha_vigencia ORDER BY ti.articulo, ti.fecha_vigencia DESC ";
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true, true);
		} 
		catch (SQLException e)
		{
			logger.info("\n\n ERROR. CONSULTANDO FECHAS VIGENCIA INVENTARIO POR ESQUEMA ARTICULO>>>>>"+e);
		}
		logger.info("\n\nCONSULTA FECHAS VIGENCIA INVENTARIO SQL >>>>>>>>>>"+cadena);
		return resultados;
	}
	
	/**
	 * 
	 * @param con
	 * @param esquemaTarifario
	 * @param articulo
	 * @param tarifa
	 * @param iva
	 * @param porcentaje
	 * @param actualizAutomatic
	 * @param tipoTarifa
	 * @param usuarioModifica
	 * @param fechaModifica
	 * @param horaModifica
	 * @param sentenciaSql1
	 * @return
	 */
	public static int logTarifasInventario(Connection con, int articulo, String tipoTarifa, double porcentaje, double tarifa, String usuario, String fecha, String hora, String actualizAutomatic, int esquemaTarifario)
	{
		try{
			PreparedStatementDecorator insert1 =  new PreparedStatementDecorator(con.prepareStatement(cadenaLogStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insert1.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con,"Seq_log_tarifas_inventario"));
			insert1.setInt(2, articulo);
			insert1.setString(3, tipoTarifa);
			insert1.setDouble(4, porcentaje);
			insert1.setDouble(5, tarifa);
			insert1.setString(6,usuario);
			insert1.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			insert1.setString(8, UtilidadFecha.getHoraActual());
			if (UtilidadTexto.isEmpty(actualizAutomatic))
			{
				insert1.setObject(9, null);
				
			}
			else
			{	
				insert1.setString(9, actualizAutomatic);
			}
			insert1.setInt(10, esquemaTarifario);
			return insert1.executeUpdate();
		
		}
		catch(SQLException e)
		{
			logger.error("Error insertando el log tarifa de inventarios: "+e);
			return 0;
		}
	}

	/**
	 * 
	 * @param con
	 * @param codigoTarifa
	 * @return
	 */
	private static int obtenerCodigoArticulo(Connection con, int codigo)
	{
		String consulta="select articulo as arti from tarifas_inventario where codigo=?";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getInt("arti");
			}
		}
		catch(SQLException e)
		{
			logger.error("Error obtenerCodigoArticulo: "+e);
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
}
