package com.princetonsa.mundo.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.actionform.facturasVarias.ConsultaMovimientoFacturaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturasVarias.ConsultaMovimientoFacturaDao;

/**
 * @author Mauricio Jaramillo
 * Fecha: Agosto de 2008
 */

public class ConsultaMovimientoFactura
{

	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private ConsultaMovimientoFacturaDao objetoDao;
	
	/**
	 * Metodo Reset
	 */
	public ConsultaMovimientoFactura()
	{
		init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * @param tipoBD
	 * @return
	 */
	private boolean init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(tipoBD);
			objetoDao=myFactory.getConsultaMovimientoFacturaDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
	}

	/**
	 * @param con
	 * @param forma
	 * @return
	 */
	public static HashMap consultarMovimientosFactura(Connection con, ConsultaMovimientoFacturaForm forma)
	{
		HashMap criterios = new HashMap();
		criterios.put("fechaInicial", forma.getFechaInicial());
		criterios.put("fechaFinal", forma.getFechaFinal());
		criterios.put("codigoPaisResidencia", forma.getCodigoPaisResidencia());
		criterios.put("codigoCiudad", forma.getCodigoCiudad());
		criterios.put("codigoRegion", forma.getCodigoRegion());
		criterios.put("codigoEmpresaInstitucion", forma.getCodigoEmpresaInstitucion());
		criterios.put("esMultiempresa", forma.getEsMultiempresa());
		criterios.put("consecutivoCentroAtencion", forma.getConsecutivoCentroAtencion());
		criterios.put("tipoDeudor", forma.getTipoDeudor());
		criterios.put("deudor", forma.getCodigoDeudor());
		criterios.put("codigoestadoFacturaVaria", forma.getCodigoestadoFacturaVaria());
		criterios.put("codigoConcepto", forma.getCodigoConcepto());
		criterios.put("loginUsuario", forma.getLoginUsuario());
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaMovimientoFacturaDao().consultarMovimientosFactura(con, criterios);
	}

	/**
	 * Método que consulta las condiciones establecidas 
	 * para la consulta de movimientos de deudor por factura
	 * para generar el reporte
	 * @param con
	 * @param forma
	 * @return
	 */
	public String consultarCondicionesMovimientosFactura(Connection con, ConsultaMovimientoFacturaForm forma)
	{
		HashMap criterios = new HashMap();
		criterios.put("fechaInicial", forma.getFechaInicial());
		criterios.put("fechaFinal", forma.getFechaFinal());
		criterios.put("tipoDeudor", forma.getTipoDeudor());
		criterios.put("deudor", forma.getCodigoDeudor());		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaMovimientoFacturaDao().consultarCondicionesMovimientosFactura(con, criterios);
	}

	/**
	 * Método que consulta la información de la factura
	 * y del deudor del consecutivo de factura varias
	 * seleccionado
	 * @param con
	 * @param deudor
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public HashMap consultarDetalleMovimientosFactura(Connection con, String consecutivoFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaMovimientoFacturaDao().consultarDetalleMovimientosFactura(con, consecutivoFactura);
	}

	/**
	 * Métdo que consulta la información de los ajustes
	 * de la factura varia seleccionada
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 */
	public HashMap consultarDetalleAjustesFactura(Connection con, String codigoFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaMovimientoFacturaDao().consultarDetalleAjustesFactura(con, codigoFactura);
	}

	/**
	 * Método que consulta la información de los pagos
	 * de la factura varia seleccionada
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 */
	public HashMap consultarDetallePagosFactura(Connection con, String codigoFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaMovimientoFacturaDao().consultarDetallePagosFactura(con, codigoFactura);
	}

	/**
	 * Método que consulta la información del resumen de
	 * movimientos de la factura varia seleccionada
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 */
	public HashMap consultarDetalleResumenMovimientosFactura(Connection con, String codigoFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaMovimientoFacturaDao().consultarDetalleResumenMovimientosFactura(con, codigoFactura);
	}
	
}