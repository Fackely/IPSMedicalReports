package com.princetonsa.dao.oracle.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ResultadoBoolean;
import util.ValoresPorDefecto;

import com.princetonsa.dao.facturacion.AbonosYDescuentosDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseAbonosYDescuentosDao;
import com.princetonsa.dto.tesoreria.DtoFiltroConsultaAbonos;
import com.princetonsa.dto.tesoreria.DtoMovmimientosAbonos;

/**
 * 
 * @author wilson
 *
 */
public class OracleAbonosYDescuentosDao implements AbonosYDescuentosDao
{
	/**
	 * Cadena para insertar un movimiento de abonos
	 */
	private static final String insertarMovimientoAbonosStr="INSERT INTO " +
																	"movimientos_abonos (codigo,paciente,codigo_documento,tipo,valor,fecha,hora,institucion,centro_atencion_duenio, ingreso, centro_atencion) " +
																	"VALUES (seq_movimientos_abonos.nextval,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?,?,?)";
	
	/**
	 * Cadena para insertar un clon de un insert de mov de abonos, diferenciado por el tipo
	 */
	private static final String insertarClonMovimientoAbonosDadoCodigoStr=
		"INSERT INTO "+
		"movimientos_abonos (codigo,paciente,codigo_documento,tipo,valor,fecha,hora,institucion, centro_atencion_duenio, ingreso, centro_atencion) " +
		
		"(" +
			"SELECT tesoreria.seq_movimientos_abonos.nextval, " +
					"paciente, " +
					"codigo_documento," +
					"?," +
					"valor," +
					"CURRENT_DATE, " +
					ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
					"institucion, centro_atencion_duenio, " +
					"ingreso," +
					"centro_atencion  " +
			"FROM " +
				"tesoreria.movimientos_abonos " +
			"WHERE " +
				"codigo=?" +
		")";

	
	/**
	 * Método para consultar los abonos disponibles del paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public double consultarAbonosDisponibles(Connection con,int codigoPaciente, Integer ingreso)
	{
		String consultarAbonosDisponiblesStr="SELECT getabonodisponible(?, ?) AS abono_disponible from dual ";
		return SqlBaseAbonosYDescuentosDao.consultarAbonosDisponibles(con,codigoPaciente, consultarAbonosDisponiblesStr, ingreso);
	}

	/**
	 * Método para insertar un movimiento de abonos
	 * @param con
	 * @param codigoPaciente
	 * @param codigoDocumento
	 * @param tipoDocumento
	 * @param valor
	 * @param institucion
	 * @return
	 */
	public int insertarMovimientoAbonos(Connection con,int codigoPaciente,int codigoDocumento,
			int tipoDocumento,double valor,int institucion, Integer ingreso, int codigoCentroAtencion)
	{
		return SqlBaseAbonosYDescuentosDao.insertarMovimientoAbonos(con,
				insertarMovimientoAbonosStr,codigoPaciente,codigoDocumento,
				tipoDocumento,valor,institucion, ingreso, codigoCentroAtencion);
	}
	
	/**
	 * Carga el codigo de la tabla movimientos_abonos dado el codigo de la factura y el tipo de mov
	 * @param con
	 * @param tipoMovAbonos
	 * @param codigoFactura
	 * @return
	 */
	public int cargarCodigoPKMovimientosAbono(Connection con, int tipoMovAbonos, int codigoFactura)
	{
	    return SqlBaseAbonosYDescuentosDao.cargarCodigoPKMovimientosAbono(con, tipoMovAbonos, codigoFactura);
	}
	
	/**
	 * Método para insertar un clon de un insert de mov de abonos, diferenciado por el tipo
	 * @param con
	 * @param codigoMovimientoAbono
	 * @param tipoMovimientoAbono
	 * @return
	 */
	public int insertarClonMovimientoAbonosDadoCodigo(	Connection con,int codigoMovimientoAbono, int tipoMovimientoAbono, int codigoCentroAtencion) 
	{
		return SqlBaseAbonosYDescuentosDao.insertarClonMovimientoAbonosDadoCodigo(con, codigoMovimientoAbono, 
		        														tipoMovimientoAbono, codigoCentroAtencion, insertarClonMovimientoAbonosDadoCodigoStr);
	}
	
	/**
	 * 
	 */
	public ArrayList<DtoMovmimientosAbonos> obtenerAbonosAplicadosFacturas(int codigoPersona) 
	{
		return SqlBaseAbonosYDescuentosDao.obtenerAbonosAplicadosFacturas(codigoPersona);
	}

	/**
	 * 
	 */
	public ArrayList<DtoMovmimientosAbonos> obtenerAbonosDevolucionRC(int codigoPersona) 
	{
		return SqlBaseAbonosYDescuentosDao.obtenerAbonosDevolucionRC(codigoPersona);
	}

	@Override
	public ArrayList<DtoMovmimientosAbonos> obtenerAbonosRecibosCaja(int codigoPersona) 
	{
		return SqlBaseAbonosYDescuentosDao.obtenerAbonosRecibosCaja(codigoPersona);
	}
	
	@Override
	public ArrayList<DtoMovmimientosAbonos> obtenerAbonosGeneral(
			int codigoPersona)
	{
		return SqlBaseAbonosYDescuentosDao.obtenerAbonosGeneral(codigoPersona);
	}

	@Override
	public ArrayList<DtoMovmimientosAbonos> obtenerAbonosAnulaciones(
			int codigoPersona)
	{
		return SqlBaseAbonosYDescuentosDao.obtenerAbonosAnulaciones(codigoPersona);
	}

	@Override
	public ResultadoBoolean eliminarAbonos(Connection con,BigDecimal codigoMovimientoAbono,int tipoMovimientoAbono)
	{
		return SqlBaseAbonosYDescuentosDao.eliminarAbonos(con, codigoMovimientoAbono, tipoMovimientoAbono);
	}

	@Override
	public BigDecimal obtenerValorReservadoAbono(int codigoPaciente) {
		String consulta="SELECT manejopaciente.getReservaAbono(?) from dual ";
		return SqlBaseAbonosYDescuentosDao.obtenerValorReservadoAbono(codigoPaciente, consulta);
	}

	@Override
	public ArrayList<DtoMovmimientosAbonos> obtenerAbonosGeneralAvanzada(
			int codigoPersona, DtoFiltroConsultaAbonos dtoFiltro) {
		return SqlBaseAbonosYDescuentosDao.obtenerAbonosGeneralAvanzada(codigoPersona,dtoFiltro);
	}
}
