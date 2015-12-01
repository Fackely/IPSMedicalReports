/*
 * Created on 30/09/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * @version 1.0, 30/09/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public interface AnulacionRecibosCajaDao
{
   
    /**
     * @param con
     * @param numeroReciboCaja
     * @param institucion
     */
    public ResultSetDecorator cargarReciboCaja(Connection con, String numeroReciboCaja, int institucion);

    
    /**
     * @param con
     * @param numeroReciboCaja
     * @param institucion
     * @return
     */
    public ResultSetDecorator consultarDetalleConceptosRecibosCaja(Connection con, String numeroReciboCaja, int institucion);

    /**
     * 
     * @param con
     * @param numeroAnulacionReciboCaja
     * @param numeroReciboCaja
     * @param institucion
     * @param fechaAnulacion
     * @param horaAnulacion
     * @param usuario
     * @param motivo
     * @param observaciones
     * @param codigoCentroAtencion 
     * @return
     */
    public int ingresarAnulacionReciboCaja(Connection con, String numeroAnulacionReciboCaja, String numeroReciboCaja, int institucion, String fechaAnulacion, String horaAnulacion, String usuario, int motivo, String observaciones, int codigoCentroAtencion);


    
    /**
     * @param con
     * @param codigoTipoDocumento
     * @param numeroReciboCaja
     * @param institucion
     * @return
     */
    public ResultSetDecorator consultarPagosFacturaPacienteReciboCaja(Connection con, int codigoTipoDocumento, String numeroReciboCaja, int institucion);


    
    /**
     * @param con
     * @param codigoTipoDocumento
     * @param numeroReciboCaja
     * @param institucion
     * @return
     */
    public ResultSetDecorator consultarPagosEmpresaReciboCaja(Connection con, int codigoTipoDocumento, String numeroReciboCaja, int institucion);


    
    /**
     * @param con
     * @param codigoTipoDocumento
     * @param numeroReciboCaja
     * @param institucion
     * @return
     */
    public ResultSetDecorator consultarAbonosReciboCaja(Connection con, int codigoTipoDocumento, String numeroReciboCaja, int institucion);


    
    /**
     * 
     * @param con
     * @param numeroAnulacionReciboCaja
     * @param tipoMovimiento
     * @param fecha
     * @param hora
     * @param codigos
     * @return
     */
    public boolean generarAbonos(Connection con, String numeroAnulacionReciboCaja, int tipoMovimiento, String fecha, String hora, String[] codigos);


    
    /**
     * @param con
     * @param codigoAbono
     * @return
     */
    public ResultSetDecorator consultarValorOperacionAbono(Connection con, int codigoAbono);


    
    /**
     * @param con
     * @param paciente
     * @return
     */
    public ResultSetDecorator consultarTodosAbonosReciboCaja(Connection con, int paciente, Integer ingreso);
    
    
    /**
     * Consulta el pago general de empresa
     * @param Connection con
     * @param HashMap parametros
     * @return HashMap<String,Object>
     */
    public HashMap<String,Object> consultarPagoGeneralEmpresa(Connection con, HashMap parametros);
    
    /**
     * Actualizar estado pagos genral empresa
     * @param con
     * @param HashMap
     * @return boolean
     */
    public boolean actualizarEstadoPagGenEmp(Connection con, HashMap parametros);
    
    /**
     * Actualizar estado pagos genral empresa
     * @param con
     * @param HashMap
     * @return boolean
     */
    public boolean actualizarEstadoAplPagFacVar(Connection con, HashMap parametros);
    
    /**
     * Consulta la Factura Varia
     * @param Connection con
     * @param HashMap parametros
     * @return HashMap<String,Object>
     */
    public HashMap<String,Object> consultarFacturaVaria(Connection con, HashMap parametros);
    
    /**
     * Consulta los consecutivos de las multas asociadas a la factura varia
     * @param Connection con
     * @param HashMap parametros
     * @return HashMap<String,Object>
     */
    public ArrayList<String> consultarConsecutivosMultaCitas(Connection con, HashMap parametros);
    
    /**
     * actualizar el estado de las multas de citas asociada a una factura varia
     * @param con
     * @param HashMap
     * @return boolean
     */
    public boolean actualizarEstadoMultaCita(Connection con, HashMap parametros);

    /**
     * actualizar el estado de las multas de citas asociada a una factura varia
     * @param con
     * @param HashMap
     * @return boolean
     */
    public boolean actualizarValorPagoFacVaria(Connection con, HashMap parametros);


    /**
     * 
     * @param con
     * @param numContrato
     * @return
     */
	public ResultSetDecorator obtenerValorAnticipoDipoContratConveRecibCaja(Connection con, int numContrato);

    
	/**
	 * 
	 * @param con
	 * @param numeroReciboCaja
	 * @return
	 */
	public ResultSetDecorator esReciboCajaXConceptAnticipConvenioOdonto(Connection con,String numeroReciboCaja);


	/**
	 * 
	 * @param con
	 * @param numContrato
	 * @param valorAntRecConvenio
	 * @param usuario 
	 * @return
	 */
	public boolean actualizarValorAnticipo(Connection con, int numContrato, double valorAntRecConvenio, String usuario);
}
