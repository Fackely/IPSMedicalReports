package com.princetonsa.dao.tesoreria;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.decorator.ResultSetDecorator;

public interface AprobacionAnulacionDevolucionesDao 
{
	
	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param devolInicial
	 * @param devolFinal
	 * @param motivoDevolucion
	 * @param tipoId
	 * @param numeroId
	 * @param centroAtencion
	 * @param caja
	 * @param reciboCaja 
	 * @return
	 */
	HashMap BusquedaDevoluciones(Connection con, String fechaInicial, String fechaFinal, String devolInicial, String devolFinal, String motivoDevolucion, String tipoId, String numeroId, String centroAtencion, String caja, String reciboCaja);

	
	/**
	 * 
	 * @param con
	 * @param devolucion
	 * @return
	 */
	HashMap ConsultaDetalleDevolucion(Connection con, int devolucion);

	
	/**
	 * 
	 * @param con
	 * @param recibo
	 * @return
	 */
	String ConsultaEstadoRecibo(Connection con, int recibo);

	/**
	 * 
	 * @param con
	 * @param codigoDevol
	 * @return
	 */
	String consultaTipoConcepto(Connection con, int codigoDevol);

	/**
	 * 
	 * @param con
	 * @param documento
	 * @return
	 */
	String consultaPagoConvenio(Connection con, int documento);
	
	/**
	 * 
	 * @param con
	 * @param codigoDevol
	 * @param estadoDevolucion
	 * @param loginUsuario
	 * @param motivoAnulacion
	 * @param valor
	 * @param turnoAfecta
	 * @return
	 */
	int insertarDevolucion(Connection con, int codigoDevol, String estadoDevolucion, String loginUsuario, String motivoAnulacion, double valor, long turnoAfecta);
	
	/**
	 * 
	 * @param con
	 * @param devolucion
	 * @return
	 */
	HashMap consultaDetalleDevolucionConsecutivo(Connection con, int devolucion);


	/**
	 * 
	 * @param con
	 * @param numeroReciboCaja
	 * @return
	 */
	ResultSetDecorator esReciboCajaXConceptAnticipConvenioOdonto(Connection con, String numeroReciboCaja);

    
	/**
	 * 
	 * @param con
	 * @param numContrato
	 * @return
	 */
	ResultSetDecorator obtenerValorAnticipoDipoContratConveRecibCaja(Connection con, int numContrato);


	ResultSetDecorator consultarValorConceptosRecibosCaja(Connection con,String numReciboCaja, int codInstitucion);


	boolean actualizarValorAnticipo(Connection con, int numContratoOdontologia,	double nuevoValorAnticipoRecConv, String usuario);
	

}
