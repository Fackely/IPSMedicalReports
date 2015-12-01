/**
 * Author: Juan Sebastian Castaño
 */
package com.princetonsa.dao.oracle.facturasVarias;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import util.ResultadoBoolean;

import com.princetonsa.dao.facturasVarias.GenModFacturasVariasDao;
import com.princetonsa.dao.sqlbase.facturasVarias.SqlBaseGenModFacturasVariasDao;
import com.princetonsa.dto.consultaExterna.DtoMultasCitas;
import com.servinte.axioma.orm.HistoricoEncabezado;

public class OracleGenModFacturasVariasDao implements GenModFacturasVariasDao {
	
	/**
	 * metodo para la consulta de los datos del asocio del deudor, osea la empresa o el tercero
	 * @param con
	 * @param tipoDeudor
	 * @param codigoDeudor
	 * @param institucion
	 * @return
	 */
	public HashMap<String, Object> cargarAsocioDeudor(Connection con, String tipoDeudor, int codigoDeudor, int institucion)
	{
		return SqlBaseGenModFacturasVariasDao.cargarAsocioDeudor(con, tipoDeudor, codigoDeudor, institucion);
	}
	

	/**
	 * Metodo de insercion de un nuevo registro de facturas varias
	 * 
	 * @param con
	 * @param consecutivo
	 * @param estadoFactura
	 * @param centroAtencion
	 * @param centroCosto
	 * @param fecha
	 * @param concepto
	 * @param valorFactura
	 * @param deudor
	 * @param observaciones
	 * @param usuario
	 * @param institucion
	 * @param multasFactura
	 * @param codigoFacturaVaria
	 * @param historicoEncabezado
	 * @return
	 */
	public ResultadoBoolean insertarFacturaVaria(Connection con, int consecutivo, String estadoFactura, int centroAtencion, int centroCosto, Date fecha, int concepto, double valorFactura,  int deudor, String observaciones, String usuario, int institucion,ArrayList<DtoMultasCitas> multasFactura, int codigoFacturaVaria, HistoricoEncabezado historicoEncabezado)
	{
		return SqlBaseGenModFacturasVariasDao.insertarFacturaVaria(con, consecutivo, estadoFactura, centroAtencion, centroCosto, fecha, concepto, valorFactura,  deudor, observaciones, usuario, institucion, multasFactura, codigoFacturaVaria, historicoEncabezado);
	}

	/**
	 * Metodo de consulta de facturas varias
	 * @param con
	 * @param noFactura
	 * @param centroAtencion
	 * @param tipoDeudor
	 * @param deudor
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public HashMap<String, Object> buscarFacturasVarias (Connection con, String noFactura, int centroAtencion, String tipoDeudor, int deudor, String fechaInicial, String fechaFinal, int institucion)
	{
		return SqlBaseGenModFacturasVariasDao.buscarFacturasVarias(con, noFactura, centroAtencion, tipoDeudor, deudor, fechaInicial, fechaFinal, institucion);
	}
	
	
	/**
	 * Metodo de modificacion de un registro de factura varia
	 * @param con
	 * @param consecutivo
	 * @param codigoFacVar
	 * @param estadoFactura
	 * @param centroAtencion
	 * @param centroCosto
	 * @param fecha
	 * @param concepto
	 * @param valorFactura
	 * @param tipoDeudor
	 * @param deudor
	 * @param observaciones
	 * @param usuario
	 * @param institucion
	 * @return
	 */
	public boolean guardarModFacturaVaria(Connection con, int consecutivo, String codigoFacVar, String estadoFactura, int centroAtencion, int centroCosto, Date fecha, int concepto, double valorFactura, int deudor, String observaciones, String usuario,  int institucion, ArrayList<DtoMultasCitas> multasFactura)
	{
		return SqlBaseGenModFacturasVariasDao.guardarModFacturaVaria(con, consecutivo, codigoFacVar, estadoFactura, centroAtencion, centroCosto, fecha, concepto, valorFactura, deudor, observaciones, usuario, institucion, multasFactura);
	}
	
	/**
	 * Metodo de consulta de conceptos de facturas varias para realizar el reporte
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public String consultarConceptosFacVarias (Connection con, int consecutivo)
	{
		return SqlBaseGenModFacturasVariasDao.consultarConceptosFacVarias(con, consecutivo);
	}

	/**
	 * Metodo encargado de consultar la informacion de las facturas varias.
	 * @param connection
	 * @param codigoFacturaVaria
	 * @param institucion
	 * @return
	 */
	public HashMap buscarFacturasVarias (Connection connection,int codigoFacturaVaria, String institucion)
	{
		return SqlBaseGenModFacturasVariasDao.buscarFacturasVarias(connection, codigoFacturaVaria, institucion);
	}
	
	/**
	 * Método para obtener las multas del paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<DtoMultasCitas> obtenerMultasPaciente(Connection con,HashMap campos)
	{
		return SqlBaseGenModFacturasVariasDao.obtenerMultasPaciente(con, campos);
	}
	
	/**
	 * Método para obtener las multas de la factura
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<DtoMultasCitas> obtenerMultasFactura(Connection con,HashMap campos)
	{
		return SqlBaseGenModFacturasVariasDao.obtenerMultasFactura(con, campos);
	}
	
}
