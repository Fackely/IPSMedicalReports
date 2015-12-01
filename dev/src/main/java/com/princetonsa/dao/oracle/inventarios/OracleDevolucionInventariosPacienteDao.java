package com.princetonsa.dao.oracle.inventarios;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.dao.inventarios.DevolucionInventariosPacienteDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseDevolucionInventariosPacienteDao;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class OracleDevolucionInventariosPacienteDao implements
		DevolucionInventariosPacienteDao {

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	public static Logger logger=Logger.getLogger(OracleDevolucionInventariosPacienteDao.class);
	
	/**
	 * Cadena para la insercion del detalle de la devolucion de solicitudes
	 */
	public static String cadenaInsercionDetalleDevolucionSolicitudes="INSERT INTO detalle_devol_med (codigo,devolucion,"+
																	"numero_solicitud,articulo,cantidad,lote,fecha_vencimiento) "+
																	"VALUES (seq_detalle_devol_med.nextval,?,?,?,?,?,?)";
	
	
	/**
	 * Cadena para la insercion de la devolucion de solicitudes
	 */
	public static String cadenaInsercionDevolucionSolicitudes="INSERT INTO devolucion_med (codigo,observaciones,fecha,hora,fecha_grabacion,"+
																"hora_grabacion,usuario,estado,centro_costo_devuelve,farmacia,tipo_devolucion,"+
																"motivo,institucion,tipo_comprobante,nro_comprobante,contabilizado) "+
																"VALUES (seq_devolucion_med.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	

	public HashMap<String, Object> consultaDetalleSolicitudes(Connection con,int despacho) 
	{
		return SqlBaseDevolucionInventariosPacienteDao.consultaDetalleSolicitudes(con, despacho);
	}

	public HashMap<String, Object> consultarIngresoCuentaPaciente(Connection con, int codigoPersona, int codigoCentroAtencion) 
	{
		return SqlBaseDevolucionInventariosPacienteDao.consultarIngresoCuentaPaciente(con, codigoPersona, codigoCentroAtencion);
	}

	public HashMap<String, Object> consultarPedidos(Connection con,	int codigoPersona) 
	{
		return SqlBaseDevolucionInventariosPacienteDao.consultarPedidos(con, codigoPersona);
	}

	public HashMap<String, Object> consultarSolicitudes(Connection con,	int codigoPersona) 
	{
		return SqlBaseDevolucionInventariosPacienteDao.consultarSolicitudes(con, codigoPersona);
	}

	public HashMap<String, Object> consultarFechaHoraDespachoSolicitudes(Connection con, int solicitud) 
	{
		return SqlBaseDevolucionInventariosPacienteDao.consultarFechaHoraDespachoSolicitudes(con, solicitud);
	}

	public boolean insertarDetalleDevolucionSolicitudes(Connection con, HashMap vo) 
	{
		return SqlBaseDevolucionInventariosPacienteDao.insertarDetalleDevolucionSolicitudes(con, vo, cadenaInsercionDetalleDevolucionSolicitudes);
	}

	public int insertarDevolucionSolicitudes(Connection con, HashMap vo) 
	{
		return SqlBaseDevolucionInventariosPacienteDao.insertarDevolucionSolicitudes(con, vo, cadenaInsercionDevolucionSolicitudes);
	}
	
	public HashMap<String, Object> consultaDetalleDevolucionSolicitudes(Connection con, int codigo) 
	{
		return SqlBaseDevolucionInventariosPacienteDao.consultaDetalleDevolucionSolicitudes(con, codigo);
	}

	public HashMap<String, Object> consultaDevolucionSolicitudes(Connection con, int codigo) 
	{
		return SqlBaseDevolucionInventariosPacienteDao.consultaDevolucionSolicitudes(con, codigo);
	}
}
