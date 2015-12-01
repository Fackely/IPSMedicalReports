/*
 * @(#)FormatoImpresionFactura.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.FormatoImpresionFacturaDao;


/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 09 /Feb/ 2006
 */
public class FormatoImpresionFactura
{

	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(FormatoImpresionFactura.class);
	
	/**
     * Constructor del objeto (Solo inicializa el acceso a la fuente de datos)
     */
    public FormatoImpresionFactura()
    {
        this.init(System.getProperty("TIPOBD"));
    }
    
    /**
	 * El DAO usado por el objeto <code>FormatoImpresionFacturaDao</code> 
	 * para acceder a la fuente de datos. 
	 */
	private FormatoImpresionFacturaDao formatoImpresionFacturaDao ;
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD) 
	{

		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

		if (myFactory != null)
		{
			formatoImpresionFacturaDao = myFactory.getFormatoImpresionFacturaDao();
			wasInited = (formatoImpresionFacturaDao != null);
		}

		return wasInited;
	}

	
	
	/**************************************************************************************
	 *						     IMPLEMETACION DE METODOS								  *
	**************************************************************************************/
	
	/**
	 * Método para consultar los formatos de impresion de factura existentes en el sistema
	 * @param con
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarFormatosExistentes (Connection con, int institucion) throws SQLException
	{
		return formatoImpresionFacturaDao.consultarFormatosExistentes(con, institucion);
	}
	
	
	/**
	 * Método para consultar los datos del formato de impresion de factura basicos
	 * @param con
	 * @param institucion
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarFormatoFacturaBasico(Connection con, int institucion, int codigoFormato) throws SQLException
	{
		return formatoImpresionFacturaDao.consultarFormatoFacturaBasico(con, institucion, codigoFormato);
	}
	
	
	/**
	 * Método para consultar los datos de la seccion principal
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarDatosSeccionPpal(Connection con, int codigoFormato) throws SQLException
	{
		return formatoImpresionFacturaDao.consultarDatosSeccionPpal(con, codigoFormato);
	}
	
	/**
	 * Método para consultar las subsecciones de la seccion de encabezado del formato de impresion de factura
	 * asi mismo trae los campos y el valor de cada uno de ellos
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarTodoSubSecciones(Connection con, int codigoFormato) throws SQLException
	{
		return formatoImpresionFacturaDao.consultarTodoSubSecciones(con, codigoFormato);
	}
	
	/**
	 * Método para consultar todo lo de la seccion de articulos
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarSecArticulos(Connection con, int codigoFormato) throws SQLException
	{
		return formatoImpresionFacturaDao.consultarSecArticulos(con, codigoFormato);
	}
	
	/**
	 * Método para consultar todo lo relacionado con la seccion de servicios del formato de impresion
	 * de factura
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarSecServicios(Connection con, int codigoFormato) throws SQLException
	{
		return formatoImpresionFacturaDao.consultarSecServicios(con, codigoFormato);
	}
	
	/**
	 * Método para consultar los datos de la seccion de totales de formato de impresion de factura
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarSecTotales(Connection con, int codigoFormato) throws SQLException
	{
		return formatoImpresionFacturaDao.consultarSecTotales(con, codigoFormato);
	}
	
	/**
	 * Método para la consulta de los datos de la seccion de nota de pie pagina del formato de impresion de factura
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarDatosSecNotaPie(Connection con, int codigoFormato) throws SQLException
	{
		return formatoImpresionFacturaDao.consultarDatosSecNotaPie(con, codigoFormato);
	}
	
	/**
	 * Método para consultar los datos estaticos de la seccion de encabezado
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarDatosSecEncabezado (Connection con, int codigoFormato) 
	{
		try
		{
			return formatoImpresionFacturaDao.consultarDatosSecEncabezado(con, codigoFormato);
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarDatosSecEncabezado de FormatoImpresionFactura: "+e);
			return null;
		}
	}
	
	/**
	 * Metodo para cosultar las firmas dado un codigo de formato
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarFirmas(Connection con, int codigoFormato) throws SQLException
	{
		return formatoImpresionFacturaDao.consultarFirmas(con, codigoFormato);
	}
	
	/**
	 * Método para insertar un formato de impresion basico
	 * @param con
	 * @param mapaFormatoBasico
	 * @return
	 * @throws SQLException
	 */
	public int insertarFormatoImpresionBasico(Connection con, HashMap mapaFormatoBasico, int institucion) throws SQLException
	{
		return formatoImpresionFacturaDao.insertarFormatoImpresionBasico(con, mapaFormatoBasico, institucion);
	}
	
	/**
	 * Método para insertar los datos de la seccion principal del formato de impresion de factura
	 * @param con
	 * @param mapaSeccionPpal
	 * @param codigoFormato
	 * @return
	 */
	public int insertarDatosSecPpal(Connection con, HashMap mapaSeccionPpal, int codigoFormato) throws SQLException
	{
		return formatoImpresionFacturaDao.insertarDatosSecPpal(con, mapaSeccionPpal, codigoFormato);
	}
	
	/**
	 * Método para insertar el valor de los campos de todas las sub secciones de la seccion del encabezado del
	 * formato de impresion de factura
	 * @param con
	 * @param codigoFormato
	 * @param mapaSeccionEncabezado
	 * @return
	 * @throws SQLException
	 */
	public int insertarTodoSubSecciones(Connection con, int codigoFormato, HashMap mapaSeccionEncabezado) throws SQLException
	{
		return formatoImpresionFacturaDao.insertarTodoSubSecciones(con, codigoFormato, mapaSeccionEncabezado);
	}
	
	/**
	 * Método para insertar los datos basicos de la seccion de encabezado del formato de impresión de factura
	 * @param con
	 * @param mapaDatosSecEncabezado
	 * @param codigoFormato
	 * @return
	 */
	public int insertarDatosBasicosSecEncabezado(Connection con, HashMap mapaDatosSecEncabezado, int codigoFormato) throws SQLException
	{
		return formatoImpresionFacturaDao.insertarDatosBasicosSecEncabezado(con, mapaDatosSecEncabezado, codigoFormato);
	}
	
	/**
	 * Metodo para insertar los datos de la seccin de servicios del formato de impresion de presupuesto
	 * @param con
	 * @param codigoFormato
	 * @param mapaSecServicios
	 * @return
	 * @throws SQLException
	 */
	public int insertarSecServicios(Connection con, int codigoFormato, HashMap mapaSecServicios )throws SQLException
	{
		return formatoImpresionFacturaDao.insertarSecServicios(con, codigoFormato, mapaSecServicios);
	}
	
	/**
	 * Método para insertar los datos de la seccion de articulos del formato de impresion de factura
	 * @param con
	 * @param codigoFormato
	 * @param mapaSecArticulos
	 * @return
	 * @throws SQLException
	 */
	public int insertarSecArticulos(Connection con, int codigoFormato, HashMap mapaSecArticulos )throws SQLException
	{
		return formatoImpresionFacturaDao.insertarSecArticulos(con, codigoFormato, mapaSecArticulos);
	}
	
	/**
	 * Método para insertar los datos de la seccion de totales de un formato de impresion de factura
	 * @param con
	 * @param codigoFormato
	 * @param mapaSecTotales
	 * @return
	 * @throws SQLException
	 */
	public int insertarSecTotales(Connection con, int codigoFormato, HashMap mapaSecTotales) throws SQLException
	{
		return formatoImpresionFacturaDao.insertarSecTotales(con, codigoFormato, mapaSecTotales);
	}
	
	/**
	 * Método para insertar los datos basicos de la seccion de nota de pie de pagina
	 * @param con
	 * @param codigoFormato
	 * @param mapaSecNotaPie
	 * @return
	 * @throws SQLException
	 */
	public int insertarDatosSecNotaPie(Connection con, int codigoFormato, HashMap mapaSecNotaPie)throws SQLException
	{
		return formatoImpresionFacturaDao.insertarDatosSecNotaPie(con, codigoFormato, mapaSecNotaPie);
	}
	
	/**
	 * Método para insertar las firmas de la seccion de nota de pie de pagina del formato de impresion de factura
	 * @param con
	 * @param codigoFormato
	 * @param mapaFirmas
	 * @return
	 * @throws SQLException
	 */
	public int insertarFirmasSecNotaPie(Connection con, int codigoFormato, HashMap mapaFirmas)throws SQLException
	{
		return formatoImpresionFacturaDao.insertarFirmasSecNotaPie(con, codigoFormato, mapaFirmas);
	}
	
	/**
	 * Método para actualizar los datos del formato de impresion de factura basico
	 * @param con
	 * @param mapaFormatoBasico
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public int actualizarFormatoImpresionBasico(Connection con, HashMap mapaFormatoBasico, int codigoFormato) throws SQLException
	{
		return formatoImpresionFacturaDao.actualizarFormatoImpresionBasico(con, mapaFormatoBasico, codigoFormato);
	}
	
	/**
	 * Método para actualizar los datos de la seccion principal del formato de impresion de factura
	 * @param con
	 * @param mapaSeccionPpal
	 * @param codigoFormato
	 * @return
	 */
	public int actualizarDatosSecPpal(Connection con, HashMap mapaSeccionPpal, int codigoFormato) throws SQLException
	{
		return formatoImpresionFacturaDao.actualizarDatosSecPpal(con, mapaSeccionPpal, codigoFormato);
	}
	
	/**
	 * Método para actualizar los datos basicos de la seccion de encabezado del formato de impresión de factura
	 * @param con
	 * @param mapaDatosSecEncabezado
	 * @param codigoFormato
	 * @return
	 */
	public int actualizarDatosBasicosSecEncabezado(Connection con, HashMap mapaDatosSecEncabezado, int codigoFormato) throws SQLException
	{
		return formatoImpresionFacturaDao.actualizarDatosBasicosSecEncabezado(con, mapaDatosSecEncabezado, codigoFormato);
	}
	
	/**
	 * Método para insertar el valor de los campos de todas las sub secciones de la seccion del encabezado del
	 * formato de impresion de factura
	 * @param con
	 * @param codigoFormato
	 * @param mapaSeccionEncabezado
	 * @return
	 * @throws SQLException
	 */
	public int actualizarTodoSubSecciones(Connection con, int codigoFormato, HashMap mapaSeccionEncabezado) throws SQLException
	{
		return formatoImpresionFacturaDao.actualizarTodoSubSecciones(con, codigoFormato, mapaSeccionEncabezado);
	}
	
	/**
	 * Metodo para actualizar los datos de la seccion de servicios del formato de impresion de factura
	 * @param con
	 * @param codigoFormato
	 * @param mapaSecServicios
	 * @return
	 * @throws SQLException
	 */
	public int actualizarSecServicios(Connection con, int codigoFormato, HashMap mapaSecServicios )throws SQLException
	{
		return formatoImpresionFacturaDao.actualizarSecServicios(con, codigoFormato, mapaSecServicios);
	}
	
	/**
	 * Método para actualizar los datos de la seccion de articulos del formato de impresion de factura
	 * @param con
	 * @param codigoFormato
	 * @param mapaSecArticulos
	 * @return
	 * @throws SQLException
	 */
	public int actualizarSecArticulos(Connection con, int codigoFormato, HashMap mapaSecArticulos )throws SQLException
	{
		return formatoImpresionFacturaDao.actualizarSecArticulos(con, codigoFormato, mapaSecArticulos);
	}
	
	/**
	 * Método para actualizar los datos de la seccion de totales de un formato de impresion de factura
	 * @param con
	 * @param codigoFormato
	 * @param mapaSecTotales
	 * @return
	 * @throws SQLException
	 */
	public int actualizarSecTotales(Connection con, int codigoFormato, HashMap mapaSecTotales) throws SQLException
	{
		return formatoImpresionFacturaDao.actualizarSecTotales(con, codigoFormato, mapaSecTotales);
	}
	
	/**
	 * Método para actualizar los datos basicos de la seccion de nota de pie de pagina
	 * @param con
	 * @param codigoFormato
	 * @param mapaSecNotaPie
	 * @return
	 * @throws SQLException
	 */
	public int actualizarDatosSecNotaPie(Connection con, int codigoFormato, HashMap mapaSecNotaPie)throws SQLException
	{
		return formatoImpresionFacturaDao.actualizarDatosSecNotaPie(con, codigoFormato, mapaSecNotaPie);
	}
	
	/**
	 * Método para actualizar las firmas de la seccion de nota de pie de pagina del formato de impresion de factura
	 * @param con
	 * @param codigoFormato
	 * @param mapaFirmas
	 * @return
	 * @throws SQLException
	 */
	public int actualizarFirmasSecNotaPie(Connection con, int codigoFormato, HashMap mapaFirmas)throws SQLException
	{
		return formatoImpresionFacturaDao.actualizarFirmasSecNotaPie(con, codigoFormato, mapaFirmas);
	}
	
	/**
	 * Método implementado para consultar los campos específicos de una
	 * subseccion del encabezado con su configuración
	 * @param con
	 * @param codigoFormato
	 * @param codigoSeccion
	 * @param soloImprimir (para filtrar que solo se consulten los campos para imprimir)
	 * @return
	 */
	public HashMap consultarSubSeccionEncabezado(Connection con,int codigoFormato,int codigoSeccion,boolean soloImprimir)
	{
		return formatoImpresionFacturaDao.consultarSubSeccionEncabezado(con,codigoFormato,codigoSeccion,soloImprimir);
	}
	
	/**
	 * Método para consultar las prioridades activas de las secciones del encabezado
	 * y las retorna ordenadas
	 * Nota * Se excluye la seccion de institucion y las secciones cuya prioridad no se encuentra definida
	 * @param con
	 * @param codigoFormato
	 * @return
	 */
	public HashMap consultarPrioridadesSeccionEncabezado(Connection con,int codigoFormato)
	{
		return formatoImpresionFacturaDao.consultarPrioridadesSeccionEncabezado(con,codigoFormato);
	}
	
	/**
	 * Método para consultar los datos parametrizados de la seccion principal dado un codigo de formato
	 * @param con
	 * @param codigoFormato
	 * @return
	 */
	public HashMap consultarDatosSecPpalParametrizados(Connection con,int codigoFormato) throws SQLException
	{
		return formatoImpresionFacturaDao.consultarDatosSecPpalParametrizados(con, codigoFormato);
	}
	/**
	 * Método que recibe una consulta dinamica armada previamente parqa consultar los datos del cuerpo
	 * de la Impresion de facturas. 
	 * Retorna un HashMap con todos los datos organizados y con las columnas necesarias segun lo parametrizado
	 * en el Formato de Impresion de factura
	 * @param con
	 * @param cadenaConsultaStr
	 * @return
	 */
	public HashMap consultaCuerpoImpresionFacturaServicios(Connection con, String cadenaConsultaStr)
	{
		return formatoImpresionFacturaDao.consultaCuerpoImpresionFacturaServicios(con, cadenaConsultaStr);
	}
	/**
	 * Método que recibe una consulta dinamica armada previamente parqa consultar los datos del cuerpo
	 * de la Impresion de facturas. 
	 * Retorna un HashMap con todos los datos organizados y con las columnas necesarias segun lo parametrizado
	 * en el Formato de Impresion de factura para la seccion de articulos
	 * @param con
	 * @param cadenaConsultaStr
	 * @return
	 */
	public HashMap consultaCuerpoImpresionFacturaArticulos(Connection con, String cadenaConsultaStr)
	{
		return formatoImpresionFacturaDao.consultaCuerpoImpresionFacturaArticulos(con, cadenaConsultaStr);
	}
	
	/**
	 * Método que consulta los campos, las descripciones parametrizadas y el valor correspondiente de cada campo 
	 * para la seccion de totales de el formato de impresion de factura dado el consecutivo de factura y el codigo
	 * del formato
	 * @param con
	 * @param consecutivoFactura
	 * @param codigoFormato
	 * @return
	 */
	public HashMap consultaCamposValoresSecTotales(Connection con, int consecutivoFactura, int codigoFormato)
	{
		return formatoImpresionFacturaDao.consultaCamposValoresSecTotales(con, consecutivoFactura, codigoFormato);
	}
	
	/**
	 * Método para saber el total de una factira segun el codigo del campo
	 * Campo es el codigo de que total se quiere obtener
	 * @param con
	 * @param consecutivoFactura
	 * @param codigoCampo
	 * @return
	 */
	public String consultaValorTotalXFacturaXCampo(Connection con, int consecutivoFactura, int codigoCampo)
	{
		return formatoImpresionFacturaDao.consultaValorTotalXFacturaXCampo(con, consecutivoFactura, codigoCampo);
	}
	
	/**
	 * Método para obtener todos los servicios Qx dado el codigo de la factura
	 * Dentro de serviciosQx(hashMap) esta otro mapa con el detalle de los asocios correspondientes a esa
	 * solicitud
	 * @param con
	 * @param codigoFactura
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarQx(Connection con, int codigoFactura) throws SQLException
	{
		return formatoImpresionFacturaDao.consultarQx(con, codigoFactura) ;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static String[] obtenerEncabezadoPieFactura(String codigoFactura, double empresaInstitucion)
	{
		Connection con= UtilidadBD.abrirConexion();
		String[] a= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoImpresionFacturaDao().obtenerEncabezadoPieFactura(con, codigoFactura, empresaInstitucion);
		UtilidadBD.closeConnection(con);
		return a;
	}


	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String obtenerFechaAperturaCuentaFactura(String codigoFactura) 
	{
		Connection con= UtilidadBD.abrirConexion();
		String fecha= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoImpresionFacturaDao().obtenerFechaAperturaCuentaFactura(con, codigoFactura);
		UtilidadBD.closeConnection(con);
		return fecha;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static String obtenerFechaFactura(String codigoFactura)
	{
		Connection con= UtilidadBD.abrirConexion();
		String fecha= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoImpresionFacturaDao().obtenerFechaFactura(con, codigoFactura);
		UtilidadBD.closeConnection(con);
		return fecha;
	}
	
}