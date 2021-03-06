package com.princetonsa.dao.postgresql.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ResultadoBoolean;

import com.princetonsa.dao.facturacion.AbonosYDescuentosDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseAbonosYDescuentosDao;
import com.princetonsa.dto.tesoreria.DtoFiltroConsultaAbonos;
import com.princetonsa.dto.tesoreria.DtoMovmimientosAbonos;

/**
 * 
 * @author wilson
 *
 */
public class PostgresqlAbonosYDescuentosDao implements AbonosYDescuentosDao
{

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(PostgresqlAbonosYDescuentosDao.class);
	
	/**
	 * Cadena para insertar un movimiento de abonos
	 */
	private static final String insertarMovimientoAbonosStr="INSERT INTO " +
		"movimientos_abonos (codigo,paciente,codigo_documento,tipo,valor,fecha,hora,institucion,centro_atencion_duenio, ingreso, centro_atencion) " +
		"VALUES (NEXTVAL('seq_movimientos_abonos'),?,?,?,?,CURRENT_DATE,CURRENT_TIME,?,?,?,?)";
	
	/**
	 * Cadena para insertar un clon de un insert de mov de abonos, diferenciado por el tipo
	 */
	private static final String insertarClonMovimientoAbonosDadoCodigoStr=
			"INSERT INTO "+
				"movimientos_abonos (codigo,paciente,codigo_documento,tipo,valor,fecha,hora,institucion, centro_atencion_duenio, ingreso, centro_atencion) " +
				
				"(" +
					"SELECT nextval('seq_movimientos_abonos'), " +
							"paciente, " +
							"codigo_documento," +
							"?," +
							"valor," +
							"CURRENT_DATE, " +
							"CURRENT_TIME, " +
							"institucion, centro_atencion_duenio, ingreso, centro_atencion " +
					"FROM " +
						"movimientos_abonos " +
					"WHERE " +
						"codigo=?" +
				")";
	
	/**
	 * M?todo para consultar los abonos disponibles del paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public double consultarAbonosDisponibles(Connection con,int codigoPaciente, Integer ingreso)
	{
		String consultarAbonosDisponiblesStr="SELECT getabonodisponible(?, ?) AS abono_disponible";
		return SqlBaseAbonosYDescuentosDao.consultarAbonosDisponibles(con,codigoPaciente, consultarAbonosDisponiblesStr, ingreso);
	}
	
	/**
	 * M?todo para insertar un movimiento de abonos
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
	 * M?todo para insertar un clon de un insert de mov de abonos, diferenciado por el tipo
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

	/**
	 * 
	 */
	public ArrayList<DtoMovmimientosAbonos> obtenerAbonosRecibosCaja(int codigoPersona) 
	{
		return SqlBaseAbonosYDescuentosDao.obtenerAbonosRecibosCaja(codigoPersona);
	}
	
	@Override
	public ArrayList<DtoMovmimientosAbonos> obtenerAbonosGeneral(int codigoPersona)
	{
		return SqlBaseAbonosYDescuentosDao.obtenerAbonosGeneral(codigoPersona);
	}

	@Override
	public ArrayList<DtoMovmimientosAbonos> obtenerAbonosAnulaciones(
			int codigoPersona)
	{
		return SqlBaseAbonosYDescuentosDao.obtenerAbonosAnulaciones(codigoPersona);
	}

	/**
	 * M?todo implementado para eliminar los abonos de un documento especifico
	 * @param con
	 * @param codigoMovimientoAbono
	 * @param tipoMovimientoAbono
	 * @return
	 */
	public ResultadoBoolean eliminarAbonos(Connection con,BigDecimal codigoMovimientoAbono,int tipoMovimientoAbono)
	{
		return SqlBaseAbonosYDescuentosDao.eliminarAbonos(con, codigoMovimientoAbono, tipoMovimientoAbono);
	}
	
	@Override
	public BigDecimal obtenerValorReservadoAbono(int codigoPaciente) {
		String consulta="SELECT manejopaciente.getReservaAbono(?) ";
		return SqlBaseAbonosYDescuentosDao.obtenerValorReservadoAbono(codigoPaciente, consulta);
	}
	

	@Override
	public ArrayList<DtoMovmimientosAbonos> obtenerAbonosGeneralAvanzada(
			int codigoPersona, DtoFiltroConsultaAbonos dtoFiltro) {
		return SqlBaseAbonosYDescuentosDao.obtenerAbonosGeneralAvanzada(codigoPersona,dtoFiltro);
	}
}