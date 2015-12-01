/*
 * Author: Juan Sebastian Castaño
 */
package com.princetonsa.dao.facturasVarias;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import util.ResultadoBoolean;

import com.princetonsa.dto.consultaExterna.DtoMultasCitas;
import com.servinte.axioma.orm.HistoricoEncabezado;

public interface GenModFacturasVariasDao {
	
	/**
	 * metodo para la consulta de los datos del asocio del deudor, osea la empresa o el tercero
	 * @param con
	 * @param tipoDeudor
	 * @param codigoDeudor
	 * @param institucion
	 * @return
	 */
	public HashMap<String, Object> cargarAsocioDeudor(Connection con, String tipoDeudor, int codigoDeudor, int institucion);
	

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
	public ResultadoBoolean insertarFacturaVaria(Connection con, int consecutivo,String estadoFactura, int centroAtencion, int centroCosto, Date fecha, int concepto, double valorFactura, int deudor, String observaciones, String usuario, int institucion,ArrayList<DtoMultasCitas> multasFactura, int codigoFacturaVaria, HistoricoEncabezado historicoEncabezado);

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
	public HashMap<String, Object> buscarFacturasVarias (Connection con, String noFactura, int centroAtencion, String tipoDeudor, int deudor, String fechaInicial, String fechaFinal, int institucion);
	
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
	public boolean guardarModFacturaVaria(Connection con, int consecutivo, String codigoFacVar, String estadoFactura, int centroAtencion, int centroCosto, Date fecha, int concepto, double valorFactura, int deudor, String observaciones, String usuario,  int institucion, ArrayList<DtoMultasCitas> multasFactura);
	
	/**
	 * Metodo de consulta de conceptos de facturas varias para realizar el reporte
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public  String consultarConceptosFacVarias (Connection con, int consecutivo);
	
	/**
	 * Metodo encargado de consultar la informacion de las facturas varias.
	 * @param connection
	 * @param codigoFacturaVaria
	 * @param institucion
	 * @return
	 */
	public HashMap buscarFacturasVarias (Connection connection,int codigoFacturaVaria, String institucion);
	
	/**
	 * Método para obtener las multas del paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<DtoMultasCitas> obtenerMultasPaciente(Connection con,HashMap campos);
	
	/**
	 * Método para obtener las multas de la factura
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<DtoMultasCitas> obtenerMultasFactura(Connection con,HashMap campos);
	
}
