package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.Utilidades;

public class SqlBaseConsultaTarifasArticulosDao 
{

	
	/**
	 * 
	 */
	public static Logger logger=Logger.getLogger(SqlBaseConsultaTarifasArticulosDao.class);
	
	/**
	 * 
	 */
	private static final String cadenaConsultaArticulos=" Select a.codigo as codigoarticulo, a.codigo_interfaz as codigointerfaz,  getdescarticulo(a.codigo) as descripcionarticulo, va.descripcionclase as descripcionclase, va.descripcionnaturaleza as descripcionnaturaleza  FROM articulo a inner join view_articulos va on(a.codigo=va.codigo) WHERE 1=1";
	
	/**
	 * 
	 */
	private static final String cadenaDetalleArticulos=" Select " +
																" ti.codigo as codigotarifa, " +
																" ti.articulo as codigoarticulo, " +
																" ti.esquema_tarifario as esquematarifario, " +
																" et.nombre as descesquematarifario, " +
																" ti.valor_tarifa as valortarifa, " +
																" ti.tipo_tarifa as tipotarifa, " +
																" ti.porcentaje as porcentaje, " +
																" ti.fecha_vigencia AS fechavigencia, " +
																" ti.actualiz_automatic as actualizautomatic, " +
																" getdescarticulo(ti.articulo) as descripcionarticulo, " +
																" va.descripcionclase as descripcionclase, " +
																" va.descripcionnaturaleza as descripcionnaturaleza, " +
																" a.estado as estadoarticulo, " +
																" a.precio_ultima_compra as precioultimacompra, " +
																" a.precio_base_venta as preciobaseventa," +
																" a.costo_promedio as costopromedio, " +
																" CASE WHEN ti.tipo_tarifa='VALFIJ' THEN getintegridaddominio(ti.tipo_tarifa)||' - '||ti.valor_tarifa ELSE "+
																" CASE WHEN ti.tipo_tarifa='PULCOM' THEN getintegridaddominio(ti.tipo_tarifa)||' - '||a.precio_ultima_compra ELSE "+
																" CASE WHEN ti.tipo_tarifa='PBAVEN' THEN getintegridaddominio(ti.tipo_tarifa)||' - '||a.precio_base_venta ELSE "+
																" CASE WHEN ti.tipo_tarifa='COSPRO' THEN getintegridaddominio(ti.tipo_tarifa)||' - '||a.costo_promedio ELSE "+
																" CASE WHEN ti.tipo_tarifa='PCOMAL' THEN getintegridaddominio(ti.tipo_tarifa)||' - '||a.precio_compra_mas_alta ELSE '' END END END END END As tipotarifadetallado,"+
																" a.precio_compra_mas_alta as preciocompramasalta  " +
															" FROM tarifas_inventario ti " +
															" inner join esquemas_tarifarios et on(et.codigo=ti.esquema_tarifario) " +
															" inner join  articulo a on(a.codigo=ti.articulo) " +
															" inner join view_articulos va on(a.codigo=va.codigo) " +
															"WHERE ti.articulo=? ORDER BY ti.fecha_vigencia DESC ";
	
	
	/**********************************************************************************
	 * MODIFICACION POR ANEXO 632
	 **********************************************************************************/
	private static final String cadenaConsultaReporteAnalisisCosto=" SELECT " +
																			" getnombrealmacen(getcodigoalmacenxarticulo(ti.articulo)) As nombre_almacen," +
																			" getCodArtAxiomaInterfazTipo(ti.articulo,?) as codigoarticulo," +
																			" coalesce(getdescripcionarticulo(ti.articulo),'') ||' CONC:'|| coalesce(getconcentracionarticulo(ti.articulo),'') ||' F.F:'|| coalesce(getformafarmaceuticaarticulo(ti.articulo),'')  AS descripcion_articulo," +
																			" getnombreesquematarifario(ti.esquema_tarifario) as esquematarifario," +
																			" va.costopromedio As costo_promedio," +
																			" ti.valor_tarifa as valortarifa," +
																			" 	 (((ti.valor_tarifa - va.costopromedio)*100) / case when va.costopromedio>0 then va.costopromedio else 1 end) As diferencia " +
																	" FROM tarifas_inventario ti" +
																	" INNER JOIN view_articulos va on(va.codigo=ti.articulo)";
	
	
	/**
	 * Metodo encargado de armar la consulta de analisis de costo
	 * @author Jhony Alexander Duque A.
	 * @param criterios
	 * ---------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------
	 * -- institucion  --> Requerido
	 * -- codigoArticulo  --> opcional
	 * -- descripcionArticulo  --> opcional
	 * -- codigoInterfaz  --> opcional
	 * -- clase  --> opcional
	 * -- grupo  --> opcional
	 * -- subgrupo  --> opcional
	 * -- naturaleza  --> opcional
	 * -- esquemaTarifario  --> opcional
	 * -- tipoCodigoArticulo --> Requerido
	 * @return String
	 */
	public static String obtenerConsulta (HashMap criterios)
	{
		String cadena=cadenaConsultaReporteAnalisisCosto.replace("?", criterios.get("tipoCodigoArticulo")+"");
		
		String where=" WHERE va.institucion="+criterios.get("institucion");
		
		if(UtilidadCadena.noEsVacio(criterios.get("codigoArticulo")+""))
			where+=" AND  va.codigo="+criterios.get("codigoArticulo");

		if(UtilidadCadena.noEsVacio(criterios.get("descripcionArticulo")+""))
			where+=" AND UPPER(va.descripcion) LIKE UPPER('%"+criterios.get("descripcionArticulo")+"%')";

		if(UtilidadCadena.noEsVacio(criterios.get("codigoInterfaz")+""))
			where+=" AND va.codigo_interfaz="+criterios.get("codigoInterfaz");
		
		if(UtilidadCadena.noEsVacio(criterios.get("clase")+"") && !(criterios.get("clase")+"").equals("0"))
			where+=" AND va.clase="+criterios.get("clase");
		
		if(UtilidadCadena.noEsVacio(criterios.get("grupo")+"") && !(criterios.get("grupo")+"").equals("0"))
			where+=" AND va.grupo="+criterios.get("grupo");
		
		if(UtilidadCadena.noEsVacio(criterios.get("subgrupo")+"") &&  !(criterios.get("subgrupo")+"").equals("0"))
			where+=" AND va.subgrupo="+criterios.get("subgrupo");
		
		if(UtilidadCadena.noEsVacio(criterios.get("naturaleza")+""))
			where+=" AND va.naturaleza='"+criterios.get("naturaleza")+"'";
		
		if (UtilidadCadena.noEsVacio(criterios.get("esquemaTarifario")+"") && !(criterios.get("esquemaTarifario")+"").equals(ConstantesBD.codigoNuncaValido+"")  )
			where+=" AND ti.esquema_tarifario="+criterios.get("esquemaTarifario");
		
		
		return cadena+where;
	}
	
	
	
    /**
     * Metodo encargado de ejecutar la  consulta enviada.
     * @param connection
     * @param consulta
     * @return
     */
    public static HashMap ejecutarConsulta (Connection connection,String consulta)
    {
    	logger.info("\n entre a ejecutarConsulta \n consunlta -->  "+consulta);
    	try
    	{
    		PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
    		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
			return mapaRetorno;
		} catch (SQLException e) {
			logger.info("\n problema consultandao el reporte "+e);
		}
    	
    	return null;
    	
    }
	
	
	/**********************************************************************************
	 * FIN MODIFICACION POR ANEXO 632
	 **********************************************************************************/
	
	
	/**
	 * 
	 * @param con 
	 * @param codigoArticulo
	 * @param descripcionArticulo
	 * @param codigoInterfaz
	 * @param clase
	 * @param grupo
	 * @param subgrupo
	 * @param naturaleza
	 * @param codigoEstandarBusquedaArticulos 
	 * @return
	 */
	public static HashMap<String, Object> consultarArticulos(Connection con, String codigoArticulo, String descripcionArticulo, String codigoInterfaz, String clase, String grupo, String subgrupo, String naturaleza, String codigoEstandarBusquedaArticulos) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		String cadena=cadenaConsultaArticulos;
		if(!codigoArticulo.equals(""))
		{
			if(codigoEstandarBusquedaArticulos.equals(ConstantesIntegridadDominio.acronimoInterfaz))
				cadena+=" AND a.codigo_interfaz='"+codigoArticulo+"'";
			else
				cadena+=" AND  va.codigo="+codigoArticulo;
		}
		if(!descripcionArticulo.equals(""))
		{	
			cadena+=" AND UPPER(va.descripcion) LIKE UPPER('%"+descripcionArticulo+"%')";
		}
		if(!codigoInterfaz.equals(""))
		{
			cadena+=" AND a.codigo_interfaz='"+codigoInterfaz+"'";
		}
		if(!clase.equals("0"))
		{
			cadena+=" AND va.clase="+clase;
		}
		if(!grupo.equals("0"))
		{
			cadena+=" AND va.grupo="+grupo;
		}
		if(!subgrupo.equals("0"))
		{
			cadena+=" AND va.subgrupo="+subgrupo;
		}
		if(!naturaleza.equals(""))
		{
			cadena+=" AND va.naturaleza='"+naturaleza+"'";
		}
		
		cadena+="  order by va.codigo";
		
		logger.info("====>Consulta Articulos: "+cadena);
		
		try
		{
			PreparedStatementDecorator busqueda= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(busqueda.executeQuery()));
		}
		catch(SQLException e)
		{
		e.printStackTrace();
		}
		return mapa;
	}

	
	/**
	 * 
	 * @param con
	 * @param articulo
	 * @return
	 */
	public static HashMap<String, Object> consultarDetalleArticulos(Connection con, String articulo) 
	{
		logger.info("\n consultarDetalleArticulos articulo -->"+articulo);
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaDetalleArticulos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(articulo));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
		}
		catch (SQLException e) 
		{
			logger.info("ERROR AL EJECUTAR LA CONSULTA DEL DETALLE DE LOS ARTICULOS "+e);
			e.printStackTrace();
		}
		return mapa;
	}

}
