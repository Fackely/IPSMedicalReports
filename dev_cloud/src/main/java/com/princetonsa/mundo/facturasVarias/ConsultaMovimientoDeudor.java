package com.princetonsa.mundo.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.actionform.facturasVarias.ConsultaMovimientoDeudorForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturasVarias.ConsultaMovimientoDeudorDao;

/**
 * @author Mauricio Jllo
 * Fecha: Agosto de 2008
 */
public class ConsultaMovimientoDeudor
{

	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private ConsultaMovimientoDeudorDao objetoDao;
	
	/**
	 * Metodo Reset
	 */
	public ConsultaMovimientoDeudor()
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
			objetoDao=myFactory.getConsultaMovimientoDeudorDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
	}
	
	/**
	 * @param con
	 * @return
	 */
	public static HashMap consultarMovimientosDeudores(Connection con, ConsultaMovimientoDeudorForm forma) 
	{
		HashMap criterios = new HashMap();
		criterios.put("fechaInicial", forma.getFechaInicial());
		criterios.put("fechaFinal", forma.getFechaFinal());
		criterios.put("tipoDeudor", forma.getTipoDeudor());
		criterios.put("deudor", forma.getCodigoDeudor());		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaMovimientoDeudorDao().consultarMovimientosDeudores(con, criterios);
	}

	/**
	 * Método que consulta las condiciones establecidas 
	 * para la consulta de movimientos de deudor para
	 * generar el reporte
	 * @param con
	 * @param forma
	 * @return
	 */
	public static String consultarCondicionesMovimientosDeudor(Connection con, ConsultaMovimientoDeudorForm forma)
	{
		HashMap criterios = new HashMap();
		criterios.put("fechaInicial", forma.getFechaInicial());
		criterios.put("fechaFinal", forma.getFechaFinal());
		criterios.put("tipoDeudor", forma.getTipoDeudor());
		criterios.put("deudor", forma.getCodigoDeudor());		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaMovimientoDeudorDao().consultarCondicionesMovimientosDeudor(con, criterios);
	}

	/**
	 * Método que consulta el detallle de los 
	 * movimientos de un deudor seleccionado
	 * @param con
	 * @param deudor
	 * @return
	 */
	public HashMap consultarDetalleMovimientosDeudor(Connection con, int deudor, String fechaInicial, String fechaFinal)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaMovimientoDeudorDao().consultarDetalleMovimientosDeudor(con, deudor, fechaInicial, fechaFinal);
	}
	
}
