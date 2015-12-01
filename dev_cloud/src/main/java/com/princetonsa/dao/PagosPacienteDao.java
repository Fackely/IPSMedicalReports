/*
 * Created on 16/07/2004
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.Collection;

/**
 * autor Juanda
 */
public interface PagosPacienteDao
{
	/**
	 * M�todo para insertar un pago de un paciente
	 * @param con
	 * @param entidad
	 * @param tipoMonto
	 * @param documento
	 * @param fecha
	 * @param diagnostico
	 * @param tipoCie
	 * @param descripcion
	 * @param valorPago
	 * @param origenPago
	 * @param usuario
	 * @param institucion
	 * @param codigoPaciente
	 * @param tipoRegimen
	 * @return
	 */
	public int insertar(Connection con, String entidad, int tipoMonto, String documento, String fecha, String diagnostico, int tipoCie, String descripcion, double valorPago, int origenPago, String usuario, int institucion, int codigoPaciente, String tipoRegimen);

	/**
	 * M�todo para insertar un pago temporal de un paciente
	 * @param con
	 * @param tipoMonto
	 * @param diagnostico
	 * @param tipoCie
	 * @param valorPago
	 * @param institucion
	 * @param codigoPaciente
	 * @param tipoRegimen
	 * @param numeroTransaccion
	 * @return C�digo del pago insertado
	 */
	public int insertarPagoTemporal(Connection con, int tipoMonto, String diagnostico, int tipoCie, double valorPago, int institucion, int codigoPaciente, String tipoRegimen, int numeroTransaccion);

	/**
	 * M�todo que devuelve el siguiente numero de transacci�n
	 * para los pagos temporales del paciente
	 * @param con
	 * @return
	 */
	public int siguienteNumeroTransaccion(Connection con);

	/**
	 * B�squeda avanzada de pagos por paciente
	 * @param con
	 * @param entidad
	 * @param tipoMonto
	 * @param documento
	 * @param fecha
	 * @param diagnostico
	 * @param tipoCie
	 * @param descripcion
	 * @param valor
	 * @param origen
	 * @param usuario
	 * @param institucion
	 * @param tipoRegimen
	 * @return
	 */
	public Collection consultaModificar(Connection con, String entidad, int tipoMonto, String documento, String fecha, String diagnostico, int tipoCie, String descripcion, double valor, int origen, String usuario, int institucion, String tipoRegimen, int paciente);

	/**
	 * M�todo el cual hace la consulta de un solo pago dado el codigo del mismo
	 * @param con
	 * @param codigo
	 * @return
	 */
	public Collection consultarPago(Connection con, int codigo);

	/**
	 * M�todo para modificar un pago de un paciente
	 * @param con
	 * @param entidad
	 * @param tipoMonto
	 * @param documento
	 * @param fecha
	 * @param diagnostico
	 * @param tipoCie
	 * @param descripcion
	 * @param valorPago
	 * @param institucion
	 * @param tipoRegimen
	 * @return
	 */
	public boolean modificar(Connection con, int codigo, String entidad, int tipoMonto, String documento, String fecha, String diagnostico, int tipoCie, String descripcion, double valor, int institucion, String tipoRegimen);

	/**
	 * Eliminar un pago especifico dado su c�digo
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean borrar(Connection con, int codigo);
	
	/**
	 * M�todo para consultar los pagos acumulados de los pacientes 
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public Collection consultarPagosAcumulados(Connection con, int codigoPaciente);

	/**
	 * M�todo para consultar los pagos acumulados de un paciente
	 * para el a�o actual (Comparado con la fecha del sistema) 
	 * @param con
	 * @param codigoPaciente
	 * @param tipoMonto
	 * @param tipoRegimen
	 * @return double con el valor de los pagos acumulados del paciente
	 */
	public double consultarPagoAcumuladoAnioActual(Connection con, int codigoPaciente, int tipoMonto, String tipoRegimen);
	
	/**
	 * M�todo para consultar los pagos acumulados de un paciente
	 * para el a�o actual (Comparado con la fecha del sistema) 
	 * @param con
	 * @param codigoPaciente
	 * @param diagnostico
	 * @param tipoCie Tipo de cie del diagn�stico
	 * @param tipoMonto
	 * @param tipoRegimen
	 * @return double con el valor de los pagos acumulados del paciente por diagnostico
	 */
	public double consultarPagoAcumuladoXDiagnostico(Connection con, int codigoPaciente, String diagnostico, int tipoCie, int tipoMonto, String tipoRegimen);

	/**
	 * Ver el detalle de los pagos por paciente
	 * @param con
	 * @param tipoMonto
	 * @param anio
	 * @param diagnostico
	 * @param tipoCie
	 * @param tipoRegimen
	 * @return
	 */
	public Collection detalle(Connection con, int tipoMonto, String anio, String diagnostico, int codigoPaciente, int tipoCie, String tipoRegimen);

	/**
	 * M�todo para borrar los pagos de los pacientes temporales insertados
	 * durante el proceso de facturaci�n
	 * @param con
	 * @param numeroTransaccion
	 * @return numero de elemntos eliminados
	 */
	public int borrarPagosPacienteTempo(Connection con, int numeroTransaccion);
	
	/**
	 * M�todo que actualiza la informaci�n de anulaci�n de los pagos de una paciente dada la factura
	 * @param con
	 * @param anulado
	 * @param loginUsuario
	 * @param codigoInstitucion
	 * @param origenPago
	 * @param codigoDIANFactura
	 * @return
	 */
	public boolean  actuallizarAnulacionPagosPacienteDadoFactura(Connection con, String anulado, String loginUsuario, int codigoInstitucion,  int origenPago, String codigoDIANFactura);

	/**
	 * M�todo que actualiza el diagn�stico de pagos paciente, si cuando se va ha realizar la atenci�n de la cita, la cuenta
	 * del paciente ya se encuentra facturada y el origen del pago es interno
	 * @param con
	 * @param numeroSolicitud
	 * @param diagnostico
	 * @param tipoCie
	 * @return
	 */
	public boolean actualizarDiagnosticoCuentaFacturada(Connection con, int numeroSolicitud, String diagnostico, int tipoCie);

}
