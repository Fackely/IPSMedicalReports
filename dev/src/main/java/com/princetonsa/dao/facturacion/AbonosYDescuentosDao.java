package com.princetonsa.dao.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import util.ResultadoBoolean;

import com.princetonsa.dto.tesoreria.DtoFiltroConsultaAbonos;
import com.princetonsa.dto.tesoreria.DtoMovmimientosAbonos;

import util.ResultadoBoolean;

/**
 * 
 * @author wilson
 *
 */
public interface AbonosYDescuentosDao 
{

	/**
	 * Método para consultar los abonos disponibles del paciente
	 * @param con
	 * @param codigoPaciente
	 * @param ingreso
	 * @return
	 */
	public double consultarAbonosDisponibles(Connection con,int codigoPaciente, Integer ingreso);
	
	/**
	 * Método para insertar un movimiento de abonos
	 * @param con
	 * @param codigoPaciente
	 * @param codigoDocumento
	 * @param tipoDocumento
	 * @param valor
	 * @param institucion
	 * @param ingreso
	 * @param codigoCentroAtencion 
	 * @return
	 */
	public int insertarMovimientoAbonos(Connection con,int codigoPaciente,int codigoDocumento,
			int tipoDocumento,double valor,int institucion, Integer ingreso, int codigoCentroAtencion);
	
	/**
	 * Carga el codigo de la tabla movimientos_abonos dado el codigo de la factura y el tipo de mov
	 * @param con
	 * @param tipoMovAbonos
	 * @param codigoFactura
	 * @return
	 */
	public int cargarCodigoPKMovimientosAbono(Connection con, int tipoMovAbonos, int codigoFactura);
	
	/**
	 * Método para insertar un clon de un insert de mov de abonos, diferenciado por el tipo
	 * @param con
	 * @param codigoMovimientoAbono
	 * @param tipoMovimientoAbono
	 * @param usuario 
	 * @return
	 */
	public int insertarClonMovimientoAbonosDadoCodigo(	Connection con,int codigoMovimientoAbono, int tipoMovimientoAbono, int codigoCentroAtencion) ;
	
	/**
	 * 
	 * @param codigoPersona
	 * @return
	 */
	public ArrayList<DtoMovmimientosAbonos> obtenerAbonosRecibosCaja(int codigoPersona);

	/**
	 * 
	 * @param codigoPersona
	 * @return
	 */
	public ArrayList<DtoMovmimientosAbonos> obtenerAbonosAplicadosFacturas(int codigoPersona);

	
	/**
	 * 
	 * @param codigoPersona
	 * @return
	 */
	public ArrayList<DtoMovmimientosAbonos> obtenerAbonosDevolucionRC(int codigoPersona);
	
	/**
	 * Lista los movimientos de los abonos en general, los cuales no se hace necesario
	 * consultar un consecutivo o un estado en una tabla espec&iacute;fica
	 * @param codigoPaciente C&oacute;digo del paciente relacionado
	 * @return Lista de los tipos de movimientos asociados al paciente
	 */
	public ArrayList<DtoMovmimientosAbonos> obtenerAbonosGeneral(int codigoPersona);
	
	/**
	 * Lista los movimientos de los abonos relacionados a las devoluciones de los recibos de caja
	 * @param codigoPersona
	 * @return {@link ArrayList}<{@link DtoMovmimientosAbonos}> Lista con los elementos retornados
	 * @author Juan David Ram&iacute;rez L&oacute;pez
	 */
	public ArrayList<DtoMovmimientosAbonos> obtenerAbonosAnulaciones(int codigoPersona); 

	/**
	 * Método implementado para eliminar los abonos de un documento especifico
	 * @param con
	 * @param codigoMovimientoAbono
	 * @param tipoMovimientoAbono
	 * @return
	 */
	public ResultadoBoolean eliminarAbonos(Connection con,BigDecimal codigoMovimientoAbono,int tipoMovimientoAbono);

	/**
	 * 
	 * @param codigoPaciente
	 * @return
	 */
	public BigDecimal obtenerValorReservadoAbono(int codigoPaciente);

	/**
	 * 
	 * @param codigoPersona
	 * @param dtoFiltro
	 * @return
	 */
	public ArrayList<DtoMovmimientosAbonos> obtenerAbonosGeneralAvanzada(
			int codigoPersona, DtoFiltroConsultaAbonos dtoFiltro);

	
}