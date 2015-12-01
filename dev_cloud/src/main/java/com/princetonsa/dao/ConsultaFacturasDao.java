/*
 * @(#)ConsultaFacturasDao.java
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
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 10 /Jun/ 2005
 */
public interface ConsultaFacturasDao 
{

	/**
	 * Método que carga las facturas dado el Codigo del paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public  ResultSetDecorator cargarFacturasPaciente(Connection con, int codigoPaciente)throws SQLException;
	
	
	
	/**
	 * Método para la busqueda de Facturas
	 * @param con
	 * @param facturaInicial
	 * @param facturaFinal
	 * @param fechaElaboracionIncial
	 * @param fechaElaboracionFinal
	 * @param responsable
	 * @param viaIngreso
	 * @param estadoFactura
	 * @param estadoPaciente
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap busquedaFacturasTodos(Connection con,int facturaInicial, int facturaFinal, String fechaElaboracionIncial, String fechaElaboracionFinal, int responsable, int viaIngreso, String tipopaciente,int estadoFactura, int estadoPaciente, String usuario, int codigoCentroAtencion, double entidadSubcontratada, double empresaInstitucion);
	
	
	/**
	 * Método que carga el detalle de una factura dado su consecutivo
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 */
	public  ResultSetDecorator cargarDetalleFactura(Connection con, double codigoFactura) throws SQLException;
	
	
	/**
	 * Método que carga las solictudes de un factura dado su consecutivo
	 * @param con
	 * @param consecutivoFactura
	 * @param institucion
	 * @return
	 */
	public  ResultSetDecorator cargarSolicitudesFactura(Connection con, double codigoFactura, String tipoArticulo, int tipoServicio) throws SQLException;



	/**
	 * Metodo para cargar los recibos de caja Asociados a una Factura.
	 * @param con
	 * @param nroFactura
	 */
	public Collection cargarListadoRecibosCaja(Connection con, double codigoFactura);
	
	/**
	 * 
	 * @param con
	 * @param facturaInicial
	 * @param facturaFinal
	 * @param fechaElaboracionIncial
	 * @param fechaElaboracionFinal
	 * @param responsable
	 * @param viaIngreso
	 * @param estadoFactura
	 * @param estadoPaciente
	 * @param usuario
	 * @param codigoCentroAtencion
	 * @param entidadSubcontratada
	 * @param empresaInstitucion
	 * @return
	 */
	public HashMap<Object, Object> cargarListadoConvenio(Connection con, int facturaInicial, int facturaFinal, String fechaElaboracionIncial, String fechaElaboracionFinal, int responsable, int viaIngreso, String tipoPaciente, int estadoFactura, int estadoPaciente, String usuario, int codigoCentroAtencion, double entidadSubcontratada, double empresaInstitucion);
	
	/**
	 * 
	 * @param con
	 * @param facturaInicial
	 * @param facturaFinal
	 * @param fechaElaboracionIncial
	 * @param fechaElaboracionFinal
	 * @param responsable
	 * @param viaIngreso
	 * @param estadoFactura
	 * @param estadoPaciente
	 * @param usuario
	 * @param codigoCentroAtencion
	 * @param entidadSubcontratada
	 * @param empresaInstitucion
	 * @return
	 */
	public HashMap<Object, Object> cargarListadoFacturadoRadicado(Connection con, int facturaInicial, int facturaFinal, String fechaElaboracionIncial, String fechaElaboracionFinal, int responsable, int viaIngreso, String tipoPaciente, int estadoFactura, int estadoPaciente, String usuario, int codigoCentroAtencion, double entidadSubcontratada, double empresaInstitucion);

	/**
	 * 
	 * @param con
	 * @param facturaInicial
	 * @param facturaFinal
	 * @param fechaElaboracionIncial
	 * @param fechaElaboracionFinal
	 * @param responsable
	 * @param viaIngreso
	 * @param estadoFactura
	 * @param estadoPaciente
	 * @param usuario
	 * @param codigoCentroAtencion
	 * @param entidadSubcontratada
	 * @param empresaInstitucion
	 * @return
	 */
	public String armarWhereFiltroFacturas(int facturaInicial, int facturaFinal, String fechaElaboracionIncial, String fechaElaboracionFinal, int responsable, int viaIngreso,String tipoPaciente, int estadoFactura, int estadoPaciente, String usuario, int codigoCentroAtencion, double entidadSubcontratada, double empresaInstitucion);
	
	/**
	 * Metodo de consulta de los datos de la factura para la generacion de reporte por solicitud
	 * @param con
	 * @param codFactura
	 * @return
	 */
	public HashMap<Object, Object> consultaDatosFactura(Connection con, String codFactura);
	
	/**
	 * 
	 * @param facturaInicial
	 * @param facturaFinal
	 * @param fechaElaboracionIncial
	 * @param fechaElaboracionFinal
	 * @param responsable
	 * @param viaIngreso
	 * @param tipoPaciente
	 * @param estadoFactura
	 * @param estadoPaciente
	 * @param usuario
	 * @param codigoCentroAtencion
	 * @param entidadSubcontratada
	 * @param empresaInstitucion
	 * @return
	 */
	public double cantidadRegistrosBusqueda(int facturaInicial, int facturaFinal, String fechaElaboracionIncial, String fechaElaboracionFinal, int responsable, int viaIngreso,String tipoPaciente, int estadoFactura, int estadoPaciente, String usuario, int codigoCentroAtencion, double entidadSubcontratada, double empresaInstitucion);


    /**
     * 
     * @param codigoCuenta
     * @param codSubCuenta
     * @return
     */
	public String consultarNumeroAutorizacion(int codigoCuenta, int codSubCuenta);
}