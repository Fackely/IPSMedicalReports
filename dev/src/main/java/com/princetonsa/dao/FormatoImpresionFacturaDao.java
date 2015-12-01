/*
 * @(#)FormatoImpresionFacturaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */


package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Interfaz para el acceder a la fuente de datos de los formatos
 *  de impresión de factura
 *
 * @version 1.0, 09 /Feb/ 2006
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
 
public interface FormatoImpresionFacturaDao 
{
	
	
	/**
	 * Método para consultar los formatos de impresion de factura existentes en el sistema
	 * @param con
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarFormatosExistentes (Connection con, int institucion) throws SQLException;
	
	
	/**
	 * Método para consultar los datos del formato de impresion de factura basicos
	 * @param con
	 * @param institucion
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarFormatoFacturaBasico(Connection con, int institucion, int codigoFormato) throws SQLException;
	
	
	/**
	 * Método para consultar los datos de la seccion principal
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarDatosSeccionPpal(Connection con, int codigoFormato) throws SQLException;
	
	/**
	 * Método para consultar las subsecciones de la seccion de encabezado del formato de impresion de factura
	 * asi mismo trae los campos y el valor de cada uno de ellos
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarTodoSubSecciones(Connection con, int codigoFormato) throws SQLException;
	
	/**
	 * Método para consultar todo lo de la seccion de articulos
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarSecArticulos(Connection con, int codigoFormato) throws SQLException;
	
	/**
	 * Método para consultar todo lo relacionado con la seccion de servicios del formato de impresion
	 * de factura
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarSecServicios(Connection con, int codigoFormato) throws SQLException;
	
	/**
	 * Método para consultar los datos de la seccion de totales de formato de impresion de factura
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarSecTotales(Connection con, int codigoFormato) throws SQLException;
	
	/**
	 * Método para la consulta de los datos de la seccion de nota de pie pagina del formato de impresion de factura
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarDatosSecNotaPie(Connection con, int codigoFormato) throws SQLException;
	
	/**
	 * Método para consultar los datos estaticos de la seccion de encabezado
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarDatosSecEncabezado (Connection con, int codigoFormato) throws SQLException;
	
	
	/**
	 * Meotod para cosultar las firmas dado un codigo de formato
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarFirmas(Connection con, int codigoFormato) throws SQLException;
	
	
	/**
	 * Método para insertar los datos de la seccion principal del formato de impresion de factura
	 * @param con
	 * @param mapaSeccionPpal
	 * @param codigoFormato
	 * @return
	 */
	public int insertarDatosSecPpal(Connection con, HashMap mapaSeccionPpal, int codigoFormato) throws SQLException;
	
	/**
	 * Método para insertar un formato de impresion basico
	 * @param con
	 * @param mapaFormatoBasico
	 * @return
	 * @throws SQLException
	 */
	public int insertarFormatoImpresionBasico(Connection con, HashMap mapaFormatoBasico, int institucion) throws SQLException;
	
	/**
	 * Método para insertar los datos basicos de la seccion de encabezado del formato de impresión de factura
	 * @param con
	 * @param mapaDatosSecEncabezado
	 * @param codigoFormato
	 * @return
	 */
	public int insertarDatosBasicosSecEncabezado(Connection con, HashMap mapaDatosSecEncabezado, int codigoFormato) throws SQLException;
	
	/**
	 * Método para insertar el valor de los campos de todas las sub secciones de la seccion del encabezado del
	 * formato de impresion de factura
	 * @param con
	 * @param codigoFormato
	 * @param mapaSeccionEncabezado
	 * @return
	 * @throws SQLException
	 */
	public int insertarTodoSubSecciones(Connection con, int codigoFormato, HashMap mapaSeccionEncabezado) throws SQLException;
	
	/**
	 * Metodo para insertar los datos de la seccin de servicios del formato de impresion de presupuesto
	 * @param con
	 * @param codigoFormato
	 * @param mapaSecServicios
	 * @return
	 * @throws SQLException
	 */
	public int insertarSecServicios(Connection con, int codigoFormato, HashMap mapaSecServicios )throws SQLException;
	
	/**
	 * Método para insertar los datos de la seccion de articulos del formato de impresion de factura
	 * @param con
	 * @param codigoFormato
	 * @param mapaSecArticulos
	 * @return
	 * @throws SQLException
	 */
	public int insertarSecArticulos(Connection con, int codigoFormato, HashMap mapaSecArticulos )throws SQLException;
	
	/**
	 * Método para insertar los datos de la seccion de totales de un formato de impresion de factura
	 * @param con
	 * @param codigoFormato
	 * @param mapaSecTotales
	 * @return
	 * @throws SQLException
	 */
	public int insertarSecTotales(Connection con, int codigoFormato, HashMap mapaSecTotales) throws SQLException;
	
	/**
	 * Método para insertar los datos basicos de la seccion de nota de pie de pagina
	 * @param con
	 * @param codigoFormato
	 * @param mapaSecNotaPie
	 * @return
	 * @throws SQLException
	 */
	public int insertarDatosSecNotaPie(Connection con, int codigoFormato, HashMap mapaSecNotaPie)throws SQLException;
	
	/**
	 * Método para insertar las firmas de la seccion de nota de pie de pagina del formato de impresion de factura
	 * @param con
	 * @param codigoFormato
	 * @param mapaFirmas
	 * @return
	 * @throws SQLException
	 */
	public int insertarFirmasSecNotaPie(Connection con, int codigoFormato, HashMap mapaFirmas)throws SQLException;
	
	/**
	 * Método para actualizar los datos del formato de impresion de factura basico
	 * @param con
	 * @param mapaFormatoBasico
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public int actualizarFormatoImpresionBasico(Connection con, HashMap mapaFormatoBasico, int codigoFormato) throws SQLException;
	
	/**
	 * Método para actualizar los datos de la seccion principal del formato de impresion de factura
	 * @param con
	 * @param mapaSeccionPpal
	 * @param codigoFormato
	 * @return
	 */
	public int actualizarDatosSecPpal(Connection con, HashMap mapaSeccionPpal, int codigoFormato) throws SQLException;
	
	/**
	 * Método para actualizar los datos basicos de la seccion de encabezado del formato de impresión de factura
	 * @param con
	 * @param mapaDatosSecEncabezado
	 * @param codigoFormato
	 * @return
	 */
	public int actualizarDatosBasicosSecEncabezado(Connection con, HashMap mapaDatosSecEncabezado, int codigoFormato) throws SQLException;
	
	/**
	 * Método para insertar el valor de los campos de todas las sub secciones de la seccion del encabezado del
	 * formato de impresion de factura
	 * @param con
	 * @param codigoFormato
	 * @param mapaSeccionEncabezado
	 * @return
	 * @throws SQLException
	 */
	public int actualizarTodoSubSecciones(Connection con, int codigoFormato, HashMap mapaSeccionEncabezado) throws SQLException;
	
	/**
	 * Metodo para actualizar los datos de la seccion de servicios del formato de impresion de factura
	 * @param con
	 * @param codigoFormato
	 * @param mapaSecServicios
	 * @return
	 * @throws SQLException
	 */
	public int actualizarSecServicios(Connection con, int codigoFormato, HashMap mapaSecServicios )throws SQLException;
	
	/**
	 * Método para actualizar los datos de la seccion de articulos del formato de impresion de factura
	 * @param con
	 * @param codigoFormato
	 * @param mapaSecArticulos
	 * @return
	 * @throws SQLException
	 */
	public int actualizarSecArticulos(Connection con, int codigoFormato, HashMap mapaSecArticulos )throws SQLException;
	
	/**
	 * Método para actualizar los datos de la seccion de totales de un formato de impresion de factura
	 * @param con
	 * @param codigoFormato
	 * @param mapaSecTotales
	 * @return
	 * @throws SQLException
	 */
	public int actualizarSecTotales(Connection con, int codigoFormato, HashMap mapaSecTotales) throws SQLException;
	
	/**
	 * Método para actualizar los datos basicos de la seccion de nota de pie de pagina
	 * @param con
	 * @param codigoFormato
	 * @param mapaSecNotaPie
	 * @return
	 * @throws SQLException
	 */
	public int actualizarDatosSecNotaPie(Connection con, int codigoFormato, HashMap mapaSecNotaPie)throws SQLException;
	
	/**
	 * Método para actualizar las firmas de la seccion de nota de pie de pagina del formato de impresion de factura
	 * @param con
	 * @param codigoFormato
	 * @param mapaFirmas
	 * @return
	 * @throws SQLException
	 */
	public int actualizarFirmasSecNotaPie(Connection con, int codigoFormato, HashMap mapaFirmas)throws SQLException;
	
	/**
	 * Método implementado para consultar los campos específicos de una
	 * subseccion del encabezado con su configuración
	 * @param con
	 * @param codigoFormato
	 * @param codigoSeccion
	 * @param soloImprimir (para filtrar que solo se consulten los campos para imprimir)
	 * @return
	 */
	public HashMap consultarSubSeccionEncabezado(Connection con,int codigoFormato,int codigoSeccion,boolean soloImprimir);
	
	/**
	 * Método para consultar las prioridades activas de las secciones del encabezado
	 * y las retorna ordenadas
	 * Nota * Se excluye la seccion de institucion y las secciones cuya prioridad no se encuentra definida
	 * @param con
	 * @param codigoFormato
	 * @return
	 */
	public HashMap consultarPrioridadesSeccionEncabezado(Connection con,int codigoFormato);

	/**
	 * Método para consultar los datos parametrizados de la seccion principal dado un codigo de formato
	 * @param con
	 * @param codigoFormato
	 * @return
	 */
	public HashMap consultarDatosSecPpalParametrizados(Connection con,int codigoFormato) throws SQLException ; 
	
	/**
	 * Método que recibe una consulta dinamica armada previamente parqa consultar los datos del cuerpo
	 * de la Impresion de facturas. 
	 * Retorna un HashMap con todos los datos organizados y con las columnas necesarias segun lo parametrizado
	 * en el Formato de Impresion de factura
	 * @param con
	 * @param cadenaConsultaStr
	 * @return
	 */
	public HashMap consultaCuerpoImpresionFacturaServicios(Connection con, String cadenaConsultaStr);
	
	/**
	 * Método que recibe una consulta dinamica armada previamente parqa consultar los datos del cuerpo
	 * de la Impresion de facturas. 
	 * Retorna un HashMap con todos los datos organizados y con las columnas necesarias segun lo parametrizado
	 * en el Formato de Impresion de factura para la seccion de articulos
	 * @param con
	 * @param cadenaConsultaStr
	 * @return
	 */
	public HashMap consultaCuerpoImpresionFacturaArticulos(Connection con, String cadenaConsultaStr);
	
	/**
	 * Método que consulta los campos, las descripciones parametrizadas y el valor correspondiente de cada campo 
	 * para la seccion de totales de el formato de impresion de factura dado el consecutivo de factura y el codigo
	 * del formato
	 * @param con
	 * @param consecutivoFactura
	 * @param codigoFormato
	 * @return
	 */
	public HashMap consultaCamposValoresSecTotales(Connection con, int consecutivoFactura, int codigoFormato);
	
	/**
	 * Método para saber el total de una factira segun el codigo del campo
	 * Campo es el codigo de que total se quiere obtener
	 * @param con
	 * @param consecutivoFactura
	 * @param codigoCampo
	 * @return
	 */
	public String consultaValorTotalXFacturaXCampo(Connection con, int consecutivoFactura, int codigoCampo);
	
	
	/**
	 * Método para obtener todos los servicios Qx dado el codigo de la factura
	 * Dentro de serviciosQx(hashMap) esta otro mapa con el detalle de los asocios correspondientes a esa
	 * solicitud
	 * @param con
	 * @param codigoFactura
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarQx(Connection con, int codigoFactura) throws SQLException;
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public String[] obtenerEncabezadoPieFactura(Connection con, String codigoFactura, double empresaInstitucion);

	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public String obtenerFechaAperturaCuentaFactura(Connection con, String codigoFactura);

	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public String obtenerFechaFactura(Connection con, String codigoFactura);
}