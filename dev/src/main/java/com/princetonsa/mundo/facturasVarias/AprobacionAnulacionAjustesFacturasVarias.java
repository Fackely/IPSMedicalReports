package com.princetonsa.mundo.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturasVarias.AprobacionAnulacionAjustesFacturasVariasDao;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Mauricio Jaramillo H.
 * Fecha: Agosto de 2008
 */

public class AprobacionAnulacionAjustesFacturasVarias
{

	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private AprobacionAnulacionAjustesFacturasVariasDao objetoDao;
	
	/*------------------------------------------------------------------------------------------------------
	 * INDICES PARA EL MANEJO DE LOS KEYS EN LOS MAPAS
	 * ----------------------------------------------------------------------------------------------------*/

	/**
	 * Indices de la consulta de ajustes de facturas varias
	 */
	public static String [] indicesAjustesFacturasVarias = {"codigo0_","consecutivo1_","tipoAjuste2_","fechaAjuste3_","factura4_","conceptoAjuste5_",
									   "valorAjuste6_","observaciones7_","estado8_","estaBd9_","institucion10_","usuarioModifica11_","deudor12_",
									   "nomConcepto13_","cosecFac14_"};
	
	
	/**
	 * Indices del mapa Criterios para la consulta avanzada de ajustes
	 * de facturas varias
	 */
	public static String [] indicesCriteriosBusqueda= {"consecutivo0","factura1","fechaIni2","fechaFin3","institucion4","tipoDeudor5","deudor6",
													   "codigoFacVar7","codigoAjus8","nomDeu9","tipoIdent10","numIdent11"};
	
	/**
	 * 
	 */
	public static String [] indicesFacturasVarias = {"codigo0_","codigoFacVar1_","nomEstadoFactura2_","fechaAjuste3_","valorFactura4_",
													 "estadoFactura5_","nomDeudor6_","deudor7_","estado8_","concepto9_","institucion10_",
													 "saldoFactura11_","fecha12_","consecutivo13_","nomConcepto14_","fechaAprobacion15_",
													 "numIdent16_"};

	/*---------------------------------------------------------------------------------------------------------------------*/
	
	/**
	 * Metodo Reset
	 */
	public AprobacionAnulacionAjustesFacturasVarias()
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
			objetoDao=myFactory.getAprobacionAnulacionAjustesFacturasVariasDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
	}
	
	/**
	 * Método que consulta la información de los ajustes
	 * pendientes de facturas varias
	 * @param con
	 * @param codigoInstitucionInt
	 * @return
	 */
	public HashMap consultarAjustesFacturasVarias(Connection con, int codigoInstitucionInt)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAprobacionAnulacionAjustesFacturasVariasDao().consultarAjustesFacturasVarias(con, codigoInstitucionInt);
	}

	/**
	 * Método que inserta los datos de aprobación/anulación
	 * del ajuste de facturas varias
	 * @param con
	 * @param criterios
	 * @return
	 */
	public boolean actualizarAprobacionAnulacionAjusteFacturasVarias(Connection con, HashMap criterios)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAprobacionAnulacionAjustesFacturasVariasDao().actualizarAprobacionAnulacionAjusteFacturasVarias(con, criterios);
	}

	/**
	 * Método que inserta en el log tipo
	 * base de datos
	 * @param con
	 * @param criterios
	 * @return
	 */
	public boolean generarLogAprobacionAnulacion(Connection con, HashMap criterios)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAprobacionAnulacionAjustesFacturasVariasDao().generarLogAprobacionAnulacion(con, criterios);
	}

	/**
	 * Metodo encargado de generar las clausulas where
	 * de la consulta.
	 * @param criterios
	 * @param ajusTodos
	 * @return
	 */
	public static String obtenerWhere(HashMap criterios)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAprobacionAnulacionAjustesFacturasVariasDao().obtenerWhere(criterios);
	}
	
}