package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;

import com.princetonsa.actionform.inventarios.MovimientosAlmacenConsignacionForm;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.inventarios.MovimientosAlmacenConsignacion;

/**
 * Anexo 684
 * Creado el 9 de Octubre de 2008
 * @author Felipe Perez Granda
 * @mail lfperez@princetonsa.com
 */

public class SqlBaseMovimientosAlmacenConsignacionDao 
{
   /**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseMovimientosAlmacenConsignacionDao.class);
	
	/**
	 * Reporte detallado almacen por proveedor
	 * Cadena consulta general sin numero ingreso
	 */
	private static String strCadenaConsultaGeneral = "SELECT DISTINCT " +
			"t.numero_identificacion AS nit, " +
			"cp.proveedor AS proveedor, " +
			"t.descripcion AS descripcion, " +
			"vmi.almacen AS almacenPropiedad, " +
			"vmi.almacendestino AS almacenConsignacion, " +
			"vmi.codigotransaccion AS nroTransaccion, " +
			//"getCodArtAxiomaInterfazTipo(a.codigo,'?') AS codArt, " + //se esta enviando el acronimo, debe ser la funcion getCodArticuloAxiomaInterfaz
			"getCodArticuloAxiomaInterfaz(a.codigo,'?') AS codArt, " +
			"a.descripcion AS articulo, " +
			"getunidadmedidaarticulo(a.codigo) AS uMed, " +
			"vmi.cantidadentrada AS cant, " +
			"vmi.costounitario AS valUnid, " +
			"vmi.valorentrada AS valTotal " +
			"FROM " +
			"view_movimientos_inventarios vmi " +
			"INNER JOIN articulo a ON (a.codigo=convertiranumero(vmi.articulo)) " +
			"INNER JOIN centros_costo cc ON (cc.codigo = convertiranumero(vmi.almacen)) " +
			"INNER JOIN convenio_proveedor cp ON (cp.codigo_axioma=a.codigo) " +
			"INNER JOIN terceros t ON (t.numero_identificacion=cp.proveedor and t.codigo||''=vmi.tercero) " +
			"INNER JOIN tipos_trans_inventarios tti on (vmi.codigotransaccion=tti.codigo and tti.indicativo_consignacion='S') " +
			"WHERE ";

	/**
	 * Reporte detallado almac�n por proveedor
	 * Cadena consulta con numero ingreso
	 */
	private static String strCadenaConsultaConNumIngreso = "SELECT DISTINCT " +
			//"vmip.proveedorcatalogo AS nit, " +
			"t.numero_identificacion AS nit, " +
			"cp.proveedor AS proveedor, " +
			"t.descripcion AS descripcion, " +
			"vmip.almacen AS almacenPropiedad, " +
			"vmip.almacendestino AS almacenConsignacion, " +
			"vmip.codigotransaccion AS nroTransaccion, " +
			//"getCodArtAxiomaInterfazTipo(a.codigo,'?') AS codArt, " + //se esta enviando el acronimo, debe ser la funcion getCodArticuloAxiomaInterfaz
			"getCodArticuloAxiomaInterfaz(a.codigo,'?') AS codArt, " +
			"a.descripcion AS articulo, " +
			"getunidadmedidaarticulo(a.codigo) AS uMed, " +
			"vmip.cantidadentrada AS cant, " +
			"vmip.costounitario AS valUnid, " +
			"vmip.valorentrada AS valTotal " +
			"FROM " +
			"inventarios.view_movim_invent_paciente vmip " +
			"INNER JOIN articulo a ON (a.codigo=convertiranumero(vmi.articulo)) " +
			"INNER JOIN centros_costo cc ON (cc.codigo = convertiranumero(vmi.almacen)) " +
			"INNER JOIN cuentas c ON (c.id=vmip.numeroingreso) " +
			"INNER JOIN convenio_proveedor cp ON (cp.codigo_axioma=a.codigo) " +
			"INNER JOIN terceros t ON (t.numero_identificacion=cp.proveedor) " +
			"INNER JOIN tipos_trans_inventarios tti on (vmip.codigotransaccion=tti.codigo and tti.indicativo_consignacion='S') " +
			"WHERE ";
	
	/**
	 * Reporte detallado paciente por proveedor
	 * Cadena consulta
	 */
	private static String strCadenaConsultaPaciente = "SELECT DISTINCT " +
			"vmip.numeroingreso AS nroIngreso, " +
			"getnombrepersona(c.codigo_paciente) AS paciente, " +
			"vmip.almacen AS almacen, " +
			"vmip.codigotransaccion AS nroTransaccion, " +
			"t.numero_identificacion AS nit, " +
			"cp.proveedor AS proveedor, " +
			"t.descripcion AS descripcion, " +
			//"getCodArtAxiomaInterfazTipo(a.codigo,'?') AS codArt, " + //se esta enviando el acronimo, debe ser la funcion getCodArticuloAxiomaInterfaz
			"getCodArticuloAxiomaInterfaz(a.codigo,'?') AS codArt, " +
			"a.descripcion AS articulo, " +
			"getunidadmedidaarticulo(a.codigo) AS uMed, " +
			"vmip.cantidadentrada AS cant, " +
			"vmip.costounitario AS valUnid, " +
			"vmip.valorentrada AS valTotal " +
			"FROM " +
			"inventarios.view_movim_invent_paciente vmip " +
			"INNER JOIN articulo a ON (a.codigo=convertiranumero(vmip.articulo)) " +
			"INNER JOIN centros_costo cc ON (cc.codigo = convertiranumero(vmip.almacen)) " +
			"INNER JOIN cuentas c ON (c.id=vmip.numeroingreso) " +
			"INNER JOIN convenio_proveedor cp ON (cp.codigo_axioma=a.codigo) " +
			"INNER JOIN terceros t ON (t.numero_identificacion=cp.proveedor) " +
			"INNER JOIN tipos_trans_inventarios tti on (vmip.codigotransaccion=tti.codigo and tti.indicativo_consignacion='S') " +
			"WHERE ";
	
	/**
	 * Ordenamiento
	 */
	
	private static String strCadenaOrdenamiento = "GROUP BY " +
			"t.numero_identificacion, " +
			"vmi.almacen, " +
			"vmi.almacendestino, " +
			"vmi.codigotransaccion, " +
			"a.codigo, " +
			"a.descripcion, " +
			"vmi.cantidadentrada, " +
			"vmi.costounitario, " +
			"vmi.valorentrada, " +
			"t.descripcion, " +
			"cp.proveedor " +
			"ORDER BY " +
			"t.numero_identificacion ";
	
	/**
     * Cadena que realiza la consulta de las existencias de articulos
     */
    private static String cadena1 =	
    	"SELECT DISTINCT " +
			"aa.articulo as codigo," +
			"va.codigo_interfaz as codigoint," +
			"getdescarticulosincodigo(va.codigo) as descripcion," +
			"descripcionum as unidadmedida," +
			"va.estado as estado," +
			"getubicacionarticulo(va.codigo,aa.almacen) as ubicacion," +
			"va.costopromedio as costo," +
			"aa.existencias as existencias," +
			"(va.costopromedio*aa.existencias) as valtotal," +
			"aa.almacen AS almacen " +
		"FROM " +
			"articulos_almacen aa " +
			"INNER JOIN view_articulos va on(aa.articulo=va.codigo) " ;
			
    /**
     * Cadena para realizar la consulta de un articulo en el caso de que no posea existencias
     */
    private static String cadena2=
    	"SELECT DISTINCT " +
			"va.codigo as codigo," +
			"va.codigo_interfaz as codigoint," +
			"getdescarticulosincodigo(va.codigo) as descripcion," +
			"va.descripcionum as unidadmedida," +
			"va.estado as estado," +
			"getubicacionarticulo(va.codigo,axa.almacen) as ubicacion," +
			"va.costopromedio as costo," +
			"aa.existencias as existencias," +
			"(va.costopromedio*va.existencias) as valtotal," +
			"axa.almacen AS almacen " +
		"FROM " +
			"view_articulos va " +
		"INNER JOIN det_articulos_por_almacen daxa on (va.codigo=daxa.articulo) " +
		"INNER JOIN articulos_por_almacen axa on (daxa.codigo_art_por_almacen=axa.codigo_pk) "+
		"INNER JOIN articulos_almacen aa ON (aa.articulo=va.codigo AND aa.almacen=axa.almacen) ";
    
    /**
     * Cadena de consulta a art�culos
     */
    private static String cadena3=
    	"SELECT DISTINCT " +
			"aa.articulo as codigo," +
			"va.codigo_interfaz as codigoint, "+
			"getdescarticulosincodigo(va.codigo) as descripcion," +
			"descripcionum as unidadmedida," +
			"va.estado as estado, "+
			"getubicacionarticulo(aa.articulo,aa.almacen) as ubicacion, "+
			"0 as costo," +
			"0 as existencias," +
			"0 as valtotal, " +
			"aa.almacen AS almacen " +
		"FROM " +
			"articulos_almacen aa " +
		"INNER JOIN view_articulos va ON (aa.articulo = va.codigo) ";

	/**
	 * Metodo que consulta las Tipos de Transacciones
	 * @param con
	 * @return
	 */
	public static HashMap consultarTransacciones(Connection con, int codInstitucion) 
	{
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			String cadena = "SELECT " +
					"consecutivo AS consecutivo, " +
					"descripcion AS descripcion, " +
					"codigo AS codigo, " +
					"coalesce(codigo_interfaz, '') AS codigointerfaz " +
					"FROM tipos_trans_inventarios " +
					"WHERE institucion = '" +
					codInstitucion+"' "+
					"AND indicativo_consignacion='S' ORDER BY descripcion"; 
			logger.info("===> La institucion es: "+codInstitucion);
			logger.info("===>Consulta Transacciones Inventarios: "+cadena);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarTransacciones: "+e);
		}
		return mapa;
	}
	
	/**
	 * Metodo que consulta los proveedores
	 * @param con
	 * @return
	 */
	public static HashMap consultarProveedores(Connection con, String proveedor) 
	{
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			String cadena = "SELECT " +
					"t.numero_identificacion, " +
					"t.descripcion " +
					"FROM terceros t  " +
					"INNER JOIN convenio_proveedor cp on(cp.proveedor=t.numero_identificacion) " +
					"WHERE cp.proveedor= '" +
					proveedor+"' ";
				
			//logger.info("====>Consulta Proveedores: "+cadena);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarProveedores: "+e);
		}
		return mapa;
	}
	
	/**
	 * Metodo que consulta los movimientos con respecto a los tipos de transaccion
	 * @param mundo
	 * @param oldQuery
	 * @param usuario
	 * @return HasMap mapa
	 */
	public static HashMap consultarMovimientos(Connection con, MovimientosAlmacenConsignacionForm forma, MovimientosAlmacenConsignacion mundo, HashMap criterios)
	{
		HashMap mapa = new HashMap<String, Object>();
        mapa.put("numRegistros","0");
        String consulta = "", groupBy = "", codigoAImprimir = "";
         
        /*
         * Validaciones tipo de codigo que selecciono para imprimir en el reporte por Tipo de Transacci�n
         */
        logger.info("====>Tipo de Articulo a Imprimir: "+criterios.get("tipoCodigoArticulo"));
		if((criterios.get("tipoCodigoArticulo")+"").equals(ConstantesIntegridadDominio.acronimoAxioma))
			codigoAImprimir = "1";
		else if((criterios.get("tipoCodigoArticulo")+"").equals(ConstantesIntegridadDominio.acronimoInterfaz))
			codigoAImprimir = "2";
		else if((criterios.get("tipoCodigoArticulo")+"").equals(ConstantesIntegridadDominio.acronimoAmbos))
			codigoAImprimir = "3";
		
		/*
         * Seleccionamos la consulta correspondiente seg�n el tipo de reporte seleccionado
         * con el fin de concatenar o armar la consulta, utilizando la cadena del select
         * com�n m�s los atributos particulares, m�s la cadena FROM y la cadena GROUP
         * correspondiente. Tambi�n validamos si el tipo de reporte utiliza la variable para
         * saber que c�digo se va imprimir: Axioma, Interfaz, Ambos
         */
        logger.info("====>Tipo de Reporte: "+criterios.get("tipoReporte"));
        
        consulta=obtenerConsulta(con, forma, mundo, codigoAImprimir, 0);
			
		//*****************FIN FILTROS GENERALES PARA CADA UNO DE LOS TIPOS DE REPORTE*********************************
        	
		/*
		 * Concatenamos el Group By a la consulta
		 */
		logger.info("===> Cadena consulta: "+consulta);
		try
        {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        	
        	//Validamos el tipo de reporte con el fin de saber que consultas son las que necesitan el par�metro
        	/*
        	if(criterios.get("tipoReporte").equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoAlmacenXProveedor))
        	{
        		ps.setObject(1, codigoAImprimir);
        	}
        	*/
        	
            logger.info("====>Consulta Movimientos Almacen Consignacion: "+consulta);
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, false);
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE MOVIMIENTOS ALMACEN CONSIGNACION");
            e.printStackTrace();
        }
        return mapa;
	}
	
	/**
<<<<<<< .working
	 * M�todo Encargado de cargar el valueObject
=======
<<<<<<< .working
=======
	 * M�todo Encargado de cargar el valueObject
>>>>>>> .merge-right.r50108
	 * @param rs
	 * @return HashMap
	 */
	private static HashMap cargarValueObject(ResultSetDecorator rs)
    {
		HashMap mapa=new HashMap();
		int cont=0;
		mapa.put("numRegistros","0");
		try
		{
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					mapa.put(rsm.getColumnLabel(i)+"_"+cont, rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
		}
		catch (SQLException e)
		{
			logger.error("ERROR GENERANDO EL VALUE OBJECT");
			e.printStackTrace();
		}
		mapa.put("numRegistros", cont+"");
		return (HashMap)mapa.clone();
    }
	
	/**
<<<<<<< .working
	 * M�todo encargado de obtener la consulta para mandarla por debajo al birt
=======
>>>>>>> .merge-right.r50029
	 * M�todo encargado de obtener la consulta para mandarla por debajo al birt
>>>>>>> .merge-right.r50108
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param codigoAImprimir
	 * @param bandera
	 * @return String
	 */
	public static String obtenerConsulta(
			Connection con, MovimientosAlmacenConsignacionForm forma, MovimientosAlmacenConsignacion mundo, String codigoAImprimir, int bandera)
	{
		String query="", consulta="", groupBy="";
		groupBy = strCadenaOrdenamiento;
		logger.info("===> SqlBase La bandera viene en: "+bandera);
		
		//******************************INICIO VALIDACIONES EN EL WHERE DE LA CONSULTA***************************
		
		String condiciones = "", condicionesPaciente = "", codigoTransacciones = mundo.tiposTransaccionesEscogidas(con, forma);
			
		/*
		 * Filtramos por el campo centro de atencion. Como es requerido no es necesario validarlo
		 */
		condiciones = "cc.centro_atencion = "+forma.getCodigoCentroAtencion()+" ";
		condicionesPaciente = "cc.centro_atencion = "+forma.getCodigoCentroAtencion()+" ";
		logger.info("===> El centro de atenci�n es: "+forma.getCodigoCentroAtencion()+"");
		
		/*
		 * Validamos el campo Almacen. Ya que no es requerido
		 */
		if(UtilidadCadena.noEsVacio(forma.getAlmacen()))
		{
			logger.info("===> El almacen escogido es: "+forma.getAlmacen()+"");
			condiciones += "AND vmi.almacen = '"+forma.getAlmacen()+"' ";
			condicionesPaciente += "AND vmip.almacen = '"+forma.getAlmacen()+"' ";
		}

		/*
		 * Se valida el nit ingresado
		 */
		if(UtilidadCadena.noEsVacio(forma.getNit()))
		{
			logger.info("===> El nit ingresado es: "+forma.getNit()+"");
			condiciones += "AND t.numero_identificacion = '"+forma.getNit()+"' ";
			condicionesPaciente += "AND t.numero_identificacion = '"+forma.getNit()+"' ";
		}
		
		/*
		 * Se valida el proveedor seleccionado
		 */
		if(UtilidadCadena.noEsVacio(forma.getProveedor()))
		{
			logger.info("===> El proveedor seleccionado es: "+forma.getProveedor()+"");
			condiciones += "AND cp.proveedor= '"+forma.getProveedor()+"' ";
			condicionesPaciente += "AND cp.proveedor= '"+forma.getProveedor()+"' ";
		}
			
		/*
		 * Se valida el numero de ingreso
		 */
		if(UtilidadCadena.noEsVacio(forma.getNumIngreso()))
		{
			logger.info("===> El numero de ingreso es: "+forma.getNumIngreso()+"");
			condicionesPaciente += "AND vmip.numeroingreso = '"+forma.getNumIngreso()+"' ";
		}
			
		logger.info("===> Aqui vamos a ver las fechas: ");
		
		/*
		 * Filtramos la consulta por las fechas de transacci�n.
		 * Como no es requerido se debe validar
		 */
		if(UtilidadCadena.noEsVacio(forma.getFechaInicial()) && UtilidadCadena.noEsVacio(forma.getFechaFinal()+""))
		{
			logger.info("===> El rango de fechas es: "+
					UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial()+"")+
					" - "+ UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal()+""));
			condiciones += "AND vmi.fechaelaboracion BETWEEN '"+
			UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial()+"")+
			"' AND '"+UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal()+"")+"' ";
			condicionesPaciente += "AND vmip.fechaelaboracion BETWEEN '"+
			UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial()+"")+
			"' AND '"+UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal()+"")+"' ";
		}
		
		/*
		 * Validamos el campo Tipos de Transacci�n que se alla se�alado uno por lo menos. Ya que no es requerido
		 */
		if(UtilidadCadena.noEsVacio(codigoTransacciones))
		{
			logger.info("===> Los tipos de transacci�n son: "+codigoTransacciones);
			condiciones += "AND vmi.codigotransaccion IN ("+codigoTransacciones+") ";
			condicionesPaciente += "AND vmip.codigotransaccion IN ("+codigoTransacciones+") ";
		}
		
		/*
		 * Validamos la busqueda por el filtro de art�culo seleccionado
		 */
		if(!(forma.getArticulo()+"").equals((ConstantesBD.codigoNuncaValido)+""))
		{
			logger.info("===> Filtro del art�culo: "+forma.getArticulo());
			condiciones += "AND a.codigo = "+forma.getArticulo()+" ";
			condicionesPaciente += "AND a.codigo = "+forma.getArticulo()+" ";
		}
		//******************************FIN VALIDACIONES EN EL WHERE DE LA CONSULTA*******************************
		
		/*
		 * Validamos que tipo de reporte se esta usando para llamar a la consulta correcta
		 */
		if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteDetalladoAlmacenXProveedor))
		{
			logger.info("===> SqlBase Reporte Detallado Almacion Por Proveedor: "+
					ConstantesIntegridadDominio.acronimoTipoReporteDetalladoAlmacenXProveedor);
		
			if(UtilidadCadena.noEsVacio(forma.getNumIngreso()+""))
			{
				logger.info("===> SqlBase Busqueda CON numero de ingreso !!!");
				logger.info("===> El codigo a impimir es: "+codigoAImprimir);
				logger.info("===> Tipo codigo Articulo: "+forma.getTipoCodigoArticulo());
				consulta = strCadenaConsultaConNumIngreso;
				consulta = consulta.replace("?", forma.getTipoCodigoArticulo());
			}
			
			else
			{
				logger.info("===> SqlBase Busqueda SIN numero de ingreso !!!");
				logger.info("===> El codigo a impimir es: "+codigoAImprimir);
				logger.info("===> Tipo codigo Articulo: "+forma.getTipoCodigoArticulo());
				forma.getTipoCodigoArticulo();
				consulta = strCadenaConsultaGeneral;
				consulta = consulta.replace("?", forma.getTipoCodigoArticulo());
			}
			
			query = consulta+condiciones+groupBy;
		}
		
		else
		{
			logger.info("===> SqlBase Reporte Detallado Paciente Por Proveedor: "+
					ConstantesIntegridadDominio.acronimoTipoReporteDetalladoPacienteXProveedor);
			logger.info("===> Busqueda de la consulta !!!");
			logger.info("===> El codigo a impimir es: "+codigoAImprimir);
			logger.info("===> Tipo codigo Articulo: "+forma.getTipoCodigoArticulo());
			forma.getTipoCodigoArticulo();
			consulta = strCadenaConsultaPaciente;
			consulta = consulta.replace("?", forma.getTipoCodigoArticulo());

			query = consulta+condicionesPaciente;
		}
		return query;
	}
}