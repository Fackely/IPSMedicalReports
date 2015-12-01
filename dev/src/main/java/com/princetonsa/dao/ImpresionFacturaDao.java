
/*
 * @author artotor
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import com.princetonsa.dto.facturacion.DtoDatosRepsonsableFactura;

/**
 * 
 * @author artotor
 *
 */
public interface ImpresionFacturaDao {

	/**
	 * Metodo que realiza la consulta especifica para el encabezado de una impresion de factura en formato estatico.
	 * @param con
	 * @param codigoFactira
	 * @return
	 */
	public abstract HashMap consultarSeccionPacienteFormatoEstatico(Connection con, String codigoFactira);
	
	/**
	 * Metodo que realiza la consulta especifica para el encabezado de una impresion de factura en formato estatico.
	 * @param con
	 * @param codigoFactira
	 * @return
	 */
	public abstract HashMap anexoSolicitudesMedicamentosFechaFactura(Connection con, String codigoFactira);
    /**
     * metodo para consultar la información de la institución
     * @param con Connection
     * @param codigoInstitucion int 
     * @return  HashMap
     */
    public abstract HashMap consultarSeccionInstitucionFormatoEstatico( Connection con, int codigoInstitucion, String codigoFactura, double empresaInstitucion );
    /**
     * metodo para consultar la información de la instituciónpara el formato de impresion de Versalles
     * @param con
     * @param codigoInstitucion
     * @param codigoFactura
     * @param empresaInstitucion
     * @return
     */
    public abstract HashMap consultarSeccionInstitucionFormatoVersalles(Connection con, int codigoInstitucion, String codigoFactura,double empresaInstitucion);
    /**
     * metodo para consultar los servicios de una factura
     * @param con Connection
     * @param codigoFactira String
     * @return HashMap
     */
    public abstract HashMap consultarSeccionServiciosFormatoEstatico( Connection con, String codigoFactira );
    /**
     * metodo para consultar el detalle de los servicios
     * @param con Connection
     * @param numSolicitud String 
     * @return HashMap
     */
    public abstract HashMap consultarSeccionDetServiciosFormatoEstatico( Connection con,String codigoDetalleFactura );
    /**
     * metodo para consultar los articulos de la factura
     * @param con Connection
     * @param codigoFactira  String 
     * @return HashMap
     */
    public abstract HashMap consultarSeccionArticulosFormatoEstatico( Connection con,String codigoFactira, String filtrarXInsumos );

    /**
     * metodo para consultar los articulos de la factura
     * @param con Connection
     * @param codigoFactira  String 
     * @return HashMap
     */
	public abstract HashMap anexoSolicitudesMedicamentosOrdenFechaFactura(Connection con, String codigoFactira);

	/**
	 * 
	 * @param con
	 * @param codSubCuenta
	 * @return
	 */
	public abstract HashMap consultarInfoCitaDadaCuenta(Connection con, String codSubCuenta);
	
	/**
	 * Metodo que informacion del paciente para el formato de Impresion de Versalles
	 * y pasa valor a letras
	 * @param con
	 * @param codConvenio
	 * @return
	 */
	public HashMap consultarValorLetrasValor(Connection con, String codConvenio);

	/**
	 * Metodo para consultar informacion del paciente para el formato de impresion de Versalles
	 * @param con
	 * @param codigoFactira
	 * @return
	 */
	public abstract HashMap consultarSeccionPacienteFormatoVersalles(Connection con, String codigoFactira);

	/**
	 * Metodo para consultar servicios para el formato de impresion de Versalles
	 * @param con
	 * @param codigoFactira
	 * @return
	 */
	public abstract HashMap consultarSeccionServiciosFormatoVersalles(Connection con, String codigoFactira);

    /**
     * metodo para consultar el detalle de los servicios para el formato de impresion Versalles
     * @param con Connection
     * @param codigoDetalleFactura 
     * @return HashMap
     */
	public abstract HashMap consultarSeccionDetServiciosFormatoVersalles(Connection con, String codigoDetalleFactura);

	 /**
     * metodo para consultar los articulos de la factura para el formato de impresion Versalles
     * @param con Connection
     * @param codigoFactira  String 
     * @return HashMap
     */
	public abstract HashMap consultarSeccionArticulosFormatoVersalles(Connection con, String codigoFactira, String filtrarXInsumos);
	
	/**
	 * @param con
	 * @param cuenta
	 * @param convenio
	 * @return
	 * @throws SQLException
	 */
	public   DtoDatosRepsonsableFactura obtenerNombreResponsable(Connection con,Integer cuenta, Integer convenio) throws SQLException;
	
	/**
	 * @param con
	 * @param convenio
	 * @param ingreso
	 * @return
	 * @throws SQLException
	 */
	public  String consultarTipoMontoFactura(Connection con,Integer convenio, Integer ingreso) throws SQLException;
}
