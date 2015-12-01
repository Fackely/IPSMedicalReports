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
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.decorator.ResultSetDecorator;

import com.princetonsa.dao.AnulacionRecibosCajaDao;
import com.princetonsa.dao.sqlbase.SqlBaseAnulacionRecibosCajaDao;

/**
 * @version 1.0, 30/09/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class OracleAnulacionRecibosCajaDao implements AnulacionRecibosCajaDao
{
    /**
     * @param con
     * @param numeroReciboCaja
     * @param institucion
     */
    public ResultSetDecorator cargarReciboCaja(Connection con, String numeroReciboCaja, int institucion)
    {
        return SqlBaseAnulacionRecibosCajaDao.cargarReciboCaja(con,numeroReciboCaja,institucion);
    }
    
    
    /**
     * @param con
     * @param numeroReciboCaja
     * @param institucion
     * @return
     */
    public ResultSetDecorator consultarDetalleConceptosRecibosCaja(Connection con, String numeroReciboCaja, int institucion)
    {
        return SqlBaseAnulacionRecibosCajaDao.consultarDetalleConceptosRecibosCaja(con,numeroReciboCaja,institucion);
    }
    
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
     * @return
     */
    public int ingresarAnulacionReciboCaja(Connection con, String numeroAnulacionReciboCaja, String numeroReciboCaja, int institucion, String fechaAnulacion, String horaAnulacion, String usuario, int motivo, String observaciones, int codigoCentroAtencion)
    {
        return SqlBaseAnulacionRecibosCajaDao.ingresarAnulacionReciboCaja(con,numeroAnulacionReciboCaja,numeroReciboCaja,institucion,fechaAnulacion,horaAnulacion,usuario,motivo,observaciones,codigoCentroAtencion);
    }
    
    
    /**
     * @param con
     * @param codigoTipoDocumento
     * @param numeroReciboCaja
     * @param institucion
     * @return
     */
    public ResultSetDecorator consultarPagosFacturaPacienteReciboCaja(Connection con, int codigoTipoDocumento, String numeroReciboCaja, int institucion)
    {
        return SqlBaseAnulacionRecibosCajaDao.consultarPagosFacturaPacienteReciboCaja(con,codigoTipoDocumento,numeroReciboCaja,institucion);
    }
    
    
    /**
     * @param con
     * @param codigoTipoDocumento
     * @param numeroReciboCaja
     * @param institucion
     * @return
     */
    public ResultSetDecorator consultarPagosEmpresaReciboCaja(Connection con, int codigoTipoDocumento, String numeroReciboCaja, int institucion)
    {
        return SqlBaseAnulacionRecibosCajaDao.consultarPagosEmpresaReciboCaja(con,codigoTipoDocumento,numeroReciboCaja,institucion);
    }
    
    
    /**
     * @param con
     * @param codigoTipoDocumento
     * @param numeroReciboCaja
     * @param institucion
     * @return
     */
    public ResultSetDecorator consultarAbonosReciboCaja(Connection con, int codigoTipoDocumento, String numeroReciboCaja, int institucion)
    {
        return SqlBaseAnulacionRecibosCajaDao.consultarAbonosReciboCaja(con,codigoTipoDocumento,numeroReciboCaja,institucion);
    }
    
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
    public boolean generarAbonos(Connection con, String numeroAnulacionReciboCaja, int tipoMovimiento, String fecha, String hora, String[] codigos)
    {
        String cadena="insert into movimientos_abonos(codigo,paciente,codigo_documento,tipo,valor,fecha,hora,institucion,centro_atencion_duenio,ingreso) (select seq_movimientos_abonos.nextval,paciente,?,?,valor,?,?,institucion,centro_atencion_duenio,ingreso from movimientos_abonos where codigo in";
        return SqlBaseAnulacionRecibosCajaDao.generarAbonos(con,numeroAnulacionReciboCaja,tipoMovimiento,fecha,hora,codigos,cadena);
    }
    
    
    /**
     * @param con
     * @param codigoAbono
     * @return
     */
    public ResultSetDecorator consultarValorOperacionAbono(Connection con, int codigoAbono)
    {
        return SqlBaseAnulacionRecibosCajaDao.consultarValorOperacionAbono(con,codigoAbono);
    }
    

    
    /**
     * @param con
     * @param paciente
     * @return
     */
    public ResultSetDecorator consultarTodosAbonosReciboCaja(Connection con, int paciente, Integer ingreso)
    {
        return SqlBaseAnulacionRecibosCajaDao.consultarTodosAbonosReciboCaja(con,paciente, ingreso);
    }
    
    /**
     * Consulta el pago general de empresa
     * @param Connection con
     * @param HashMap parametros
     * @return HashMap<String,Object>
     */
    public HashMap<String,Object> consultarPagoGeneralEmpresa(Connection con, HashMap parametros)
    {
    	return SqlBaseAnulacionRecibosCajaDao.consultarPagoGeneralEmpresa(con, parametros);
    }
    
    /**
     * Actualizar estado pagos genral empresa
     * @param con
     * @param HashMap
     * @return boolean
     */
    public boolean actualizarEstadoPagGenEmp(Connection con, HashMap parametros)
    {
    	return SqlBaseAnulacionRecibosCajaDao.actualizarEstadoPagGenEmp(con, parametros);
    }
    
    /**
     * Actualizar estado pagos genral empresa
     * @param con
     * @param HashMap
     * @return boolean
     */
    public boolean actualizarEstadoAplPagFacVar(Connection con, HashMap parametros)
    {
    	return SqlBaseAnulacionRecibosCajaDao.actualizarEstadoAplPagFacVar(con, parametros);
    }
    
    /**
     * Consulta la Factura Varia
     * @param Connection con
     * @param HashMap parametros
     * @return HashMap<String,Object>
     */
    public HashMap<String,Object> consultarFacturaVaria(Connection con, HashMap parametros)
    {
    	return SqlBaseAnulacionRecibosCajaDao.consultarFacturaVaria(con, parametros);
    }
    
    /**
     * Consulta los consecutivos de las multas asociadas a la factura varia
     * @param Connection con
     * @param HashMap parametros
     * @return HashMap<String,Object>
     */
    public ArrayList<String> consultarConsecutivosMultaCitas(Connection con, HashMap parametros)
    {
    	return SqlBaseAnulacionRecibosCajaDao.consultarConsecutivosMultaCitas(con, parametros);
    }
    
    /**
     * actualizar el estado de las multas de citas asociada a una factura varia
     * @param con
     * @param HashMap
     * @return boolean
     */
    public boolean actualizarEstadoMultaCita(Connection con, HashMap parametros)
    {
    	return SqlBaseAnulacionRecibosCajaDao.actualizarEstadoMultaCita(con, parametros);
    }
    
    /**
     * actualizar el estado de las multas de citas asociada a una factura varia
     * @param con
     * @param HashMap
     * @return boolean
     */
    public boolean actualizarValorPagoFacVaria(Connection con, HashMap parametros)
    {
    	return SqlBaseAnulacionRecibosCajaDao.actualizarValorPagoFacVaria(con, parametros);
    }


	@Override
	public ResultSetDecorator obtenerValorAnticipoDipoContratConveRecibCaja(Connection con, int numContrato) {
		
		return  SqlBaseAnulacionRecibosCajaDao.obtenerValorAnticipoDipoContratConveRecibCaja(con, numContrato);
	}


	@Override
	public ResultSetDecorator esReciboCajaXConceptAnticipConvenioOdonto(Connection con,String numeroReciboCaja) {
		
		return SqlBaseAnulacionRecibosCajaDao.esReciboCajaXConceptAnticipConvenioOdonto(con, numeroReciboCaja);
	}


	@Override
	public boolean actualizarValorAnticipo(Connection con, int numContrato,	double valorAntRecConvenio, String usuario) {
		
		return SqlBaseAnulacionRecibosCajaDao.actualizarValorAnticipoContrato(con,numContrato,valorAntRecConvenio, usuario);
	}
}
