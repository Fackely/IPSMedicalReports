/*
 * @(#)OracleConsultaFacturasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao.oracle;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.dao.ConsultaFacturasDao;
import com.princetonsa.dao.sqlbase.SqlBaseConsultaFacturasDao;


/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 24 /May/ 2005
 */
public class OracleConsultaFacturasDao implements ConsultaFacturasDao 
{

	/**
	 * Método que carga los datos de una cuenta del paciente cargado en sesion y muestra
	 * las fechas de Admision o de apertura de la Cuenta segun sea su via de 
	 * Ingreso(urgencias-hospitalizacion o consulta extrena/ambulatorios)
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public ResultSetDecorator cargarFacturasPaciente (Connection con, int codigoPaciente) throws SQLException
	{
		return SqlBaseConsultaFacturasDao.cargarFacturasPaciente(con, codigoPaciente);
	}
	
	
	/**
	 * Metodo para la busuqeda de facturas (TODOS)
	 * @param con
	 * @param facturaInicial
	 * @param facturaFinal
	 * @param fechaElaboracionIncial
	 * @param fechaElaboracionFinal
	 * @param responsable
	 * @param viaIngreso
	 * @param estadoFactura
	 * @param estadoPaciente
	 */
	@SuppressWarnings("rawtypes")
	public HashMap busquedaFacturasTodos(Connection con,int facturaInicial, int facturaFinal, String fechaElaboracionIncial, String fechaElaboracionFinal, int responsable, int viaIngreso, String tipoPaciente,int estadoFactura, int estadoPaciente, String usuario, int codigoCentroAtencion, double entidadSubcontratada, double empresaInstitucion)
	{
		return SqlBaseConsultaFacturasDao.busquedaFacturasTodos(con,facturaInicial, facturaFinal, fechaElaboracionIncial, fechaElaboracionFinal, responsable,viaIngreso,tipoPaciente,estadoFactura, estadoPaciente, usuario, codigoCentroAtencion, entidadSubcontratada, empresaInstitucion);
	}
	
	/**
	 * Método que carga el detalle de una factura dado su consecutivo
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 */
	public  ResultSetDecorator cargarDetalleFactura(Connection con, double codigoFactura) throws SQLException
	{
		return SqlBaseConsultaFacturasDao.cargarDetalleFactura(con, codigoFactura);
	}
	
	
	/**
	 * Método que carga las solicitudes de un factura dado su consecutivo
	 * @param con
	 * @param consecutivoFactura
	 * @param institucion
	 * @return
	 */
	public  ResultSetDecorator cargarSolicitudesFactura(Connection con, double codigoFactura, String tipoArticulo, int tipoServicio) throws SQLException
	{
		return SqlBaseConsultaFacturasDao.cargarSolicitudesFactura(con, codigoFactura, tipoArticulo, tipoServicio);
	}
	

	/**
	 * Metodo para cargar los recibos de caja Asociados a una Factura.
	 * @param con
	 * @param nroFactura
	 */
	public Collection cargarListadoRecibosCaja(Connection con, double codigoFactura)
	{
		return SqlBaseConsultaFacturasDao.cargarListadoRecibosCaja(con, codigoFactura);
	}
	
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
	public HashMap<Object, Object> cargarListadoConvenio(Connection con, int facturaInicial, int facturaFinal, String fechaElaboracionIncial, String fechaElaboracionFinal, int responsable, int viaIngreso, String tipoPaciente,int estadoFactura, int estadoPaciente, String usuario, int codigoCentroAtencion, double entidadSubcontratada, double empresaInstitucion)
	{
		return SqlBaseConsultaFacturasDao.cargarListadoConvenio(con, facturaInicial, facturaFinal, fechaElaboracionIncial, fechaElaboracionFinal, responsable, viaIngreso, tipoPaciente,  estadoFactura, estadoPaciente, usuario, codigoCentroAtencion, entidadSubcontratada, empresaInstitucion);
	}

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
	public String armarWhereFiltroFacturas(int facturaInicial, int facturaFinal, String fechaElaboracionIncial, String fechaElaboracionFinal, int responsable, int viaIngreso,String tipoPaciente,int estadoFactura, int estadoPaciente, String usuario, int codigoCentroAtencion, double entidadSubcontratada, double empresaInstitucion)
	{
		return SqlBaseConsultaFacturasDao.armarWhereFiltroFacturas(facturaInicial, facturaFinal, fechaElaboracionIncial, fechaElaboracionFinal, responsable, viaIngreso, tipoPaciente, estadoFactura, estadoPaciente, usuario, codigoCentroAtencion, entidadSubcontratada, empresaInstitucion);
	}
	
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
	public HashMap<Object, Object> cargarListadoFacturadoRadicado(Connection con, int facturaInicial, int facturaFinal, String fechaElaboracionIncial, String fechaElaboracionFinal, int responsable, int viaIngreso, String tipoPaciente, int estadoFactura, int estadoPaciente, String usuario, int codigoCentroAtencion, double entidadSubcontratada, double empresaInstitucion)
	{
		return SqlBaseConsultaFacturasDao.cargarListadoFacturadoRadicado(con, facturaInicial, facturaFinal, fechaElaboracionIncial, fechaElaboracionFinal, responsable, viaIngreso, tipoPaciente, estadoFactura, estadoPaciente, usuario, codigoCentroAtencion, entidadSubcontratada, empresaInstitucion);
	}
	
	/**
	 * Metodo de consulta los datos de la factura para la generacion de reporte por solicitud
	 * @param con
	 * @param codFactura
	 * @return
	 */	
	public HashMap<Object, Object> consultaDatosFactura(Connection con, String codFactura)
	{
		return SqlBaseConsultaFacturasDao.consultaDatosFactura(con, codFactura);
	}
	
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
	public double cantidadRegistrosBusqueda(int facturaInicial, int facturaFinal, String fechaElaboracionIncial, String fechaElaboracionFinal, int responsable, int viaIngreso,String tipoPaciente, int estadoFactura, int estadoPaciente, String usuario, int codigoCentroAtencion, double entidadSubcontratada, double empresaInstitucion)
	{
		return SqlBaseConsultaFacturasDao.cantidadRegistrosBusqueda(facturaInicial, facturaFinal, fechaElaboracionIncial, fechaElaboracionFinal, responsable, viaIngreso, tipoPaciente, estadoFactura, estadoPaciente, usuario, codigoCentroAtencion, entidadSubcontratada, empresaInstitucion);
	}


	@Override
	public String consultarNumeroAutorizacion(int codigoCuenta, int codSubCuenta) {
		
		return SqlBaseConsultaFacturasDao.consultarNumeroAutorizacion(codigoCuenta,codSubCuenta);
	}
}
