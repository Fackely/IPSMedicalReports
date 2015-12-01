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
	 * Método para insertar un pago de un paciente
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
	 * Método para insertar un pago temporal de un paciente
	 * @param con
	 * @param tipoMonto
	 * @param diagnostico
	 * @param tipoCie
	 * @param valorPago
	 * @param institucion
	 * @param codigoPaciente
	 * @param tipoRegimen
	 * @param numeroTransaccion
	 * @return Código del pago insertado
	 */
	public int insertarPagoTemporal(Connection con, int tipoMonto, String diagnostico, int tipoCie, double valorPago, int institucion, int codigoPaciente, String tipoRegimen, int numeroTransaccion);

	/**
	 * Método que devuelve el siguiente numero de transacción
	 * para los pagos temporales del paciente
	 * @param con
	 * @return
	 */
	public int siguienteNumeroTransaccion(Connection con);

	/**
	 * Búsqueda avanzada de pagos por paciente
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
	 * Método el cual hace la consulta de un solo pago dado el codigo del mismo
	 * @param con
	 * @param codigo
	 * @return
	 */
	public Collection consultarPago(Connection con, int codigo);

	/**
	 * Método para modificar un pago de un paciente
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
	 * Eliminar un pago especifico dado su código
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean borrar(Connection con, int codigo);
	
	/**
	 * Método para consultar los pagos acumulados de los pacientes 
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public Collection consultarPagosAcumulados(Connection con, int codigoPaciente);

	/**
	 * Método para consultar los pagos acumulados de un paciente
	 * para el año actual (Comparado con la fecha del sistema) 
	 * @param con
	 * @param codigoPaciente
	 * @param tipoMonto
	 * @param tipoRegimen
	 * @return double con el valor de los pagos acumulados del paciente
	 */
	public double consultarPagoAcumuladoAnioActual(Connection con, int codigoPaciente, int tipoMonto, String tipoRegimen);
	
	/**
	 * Método para consultar los pagos acumulados de un paciente
	 * para el año actual (Comparado con la fecha del sistema) 
	 * @param con
	 * @param codigoPaciente
	 * @param diagnostico
	 * @param tipoCie Tipo de cie del diagnóstico
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
	 * Método para borrar los pagos de los pacientes temporales insertados
	 * durante el proceso de facturación
	 * @param con
	 * @param numeroTransaccion
	 * @return numero de elemntos eliminados
	 */
	public int borrarPagosPacienteTempo(Connection con, int numeroTransaccion);
	
	/**
	 * Método que actualiza la información de anulación de los pagos de una paciente dada la factura
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
	 * Método que actualiza el diagnóstico de pagos paciente, si cuando se va ha realizar la atención de la cita, la cuenta
	 * del paciente ya se encuentra facturada y el origen del pago es interno
	 * @param con
	 * @param numeroSolicitud
	 * @param diagnostico
	 * @param tipoCie
	 * @return
	 */
	public boolean actualizarDiagnosticoCuentaFacturada(Connection con, int numeroSolicitud, String diagnostico, int tipoCie);

}
