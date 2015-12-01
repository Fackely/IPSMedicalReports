package com.princetonsa.mundo.glosas;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.actionform.glosas.RegistrarModificarGlosasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.glosas.RegistrarModificarGlosasDao;
import com.princetonsa.dto.glosas.DtoObsFacturaGlosas;

/**
 * 
 * @author Angela María Angel amangel@princetonsa.com
 *
 */

public class RegistrarModificarGlosas
{
	Logger logger = Logger.getLogger(RegistrarModificarGlosas.class);
	private static RegistrarModificarGlosasDao getRegistrarModificarGlosasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistrarModificarGlosasDao();
	}
	
	/**
	 * Metodo que actualiza la glosa modificada
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static int guardar(Connection con, HashMap criterios, String usuario, RegistrarModificarGlosasForm forma, String consecutivo)
	{
		criterios.put("consecutivo", consecutivo);
		criterios.put("usuario", usuario);
		return getRegistrarModificarGlosasDao().guardar(con, criterios, forma.getBanderaGuardar());
	}

	/**
	 * Metodo que consulta el detalle de la glosa
	 * @param con
	 * @param criteriosDetalle
	 * @param forma
	 * @return
	 */
	public static HashMap consultarDetalleGlosa(Connection con, int codGlosa, int institucion) {
		
		return getRegistrarModificarGlosasDao().consultarDetalleGlosa(con, codGlosa, institucion);
	}	
	
	/**
	 * Metodo que consulta el historico de factura en todas las glosas
	 * @param con
	 * @param codFactura
	 * @return
	 */
	public static HashMap accionHistoricoGlosa(Connection con, String codFactura)
	{
		return getRegistrarModificarGlosasDao().accionHistoricoGlosa(con, codFactura);
	}
	
	/**
	 * Metodo que consulta Unica Factura por Consecutivo
	 * @param con
	 * @param criterios
	 * @return
	 */
	public HashMap consultarUnicaFactura(Connection con , HashMap criterios)
	{
		return getRegistrarModificarGlosasDao().consultarUnicaFactura(con, criterios);
	}
	
	/**
	 * Metodo que guarda las facturas de la Glosa
	 * @param con
	 * @param criterios
	 * @param operacion
	 * @return
	 */
	public int guardarFacturas(Connection con, HashMap criterios, String operacion)
	{
		return getRegistrarModificarGlosasDao().guardarFacturas(con, criterios, operacion);
	}
	
	/**
	 * Metodo que consulta todos los contratos por detalle glosa factura
	 * @param con
	 * @param codigoGlosa
	 * @return
	 */
	public HashMap consultaContratosFactura(Connection con, String codigoGlosa)
	{
		return getRegistrarModificarGlosasDao().consultaContratosFactura(con, codigoGlosa);
	}
	
	/**
	 * Metodo que elimina una factura de una glosa con sus dependencias
	 * @param con
	 * @param forma
	 * @return
	 */
	public boolean eliminarFacturaGlosa(Connection con, String codAudi)
	{
		boolean transaccion = UtilidadBD.iniciarTransaccion(con);
		HashMap<String, Object> detFacturasMap = new HashMap<String, Object>();
		
		//se consulta los detalles de la Factura
		detFacturasMap=getRegistrarModificarGlosasDao().consultarDetalleFacturaGlosa(con, codAudi);
				
		for(int i=0;i<Utilidades.convertirAEntero(detFacturasMap.get("numRegistros")+"")&&transaccion;i++)
		{
			//se eliminan los conceptos del detalle de las Facturas de la Glosa
			if(transaccion)
				transaccion=getRegistrarModificarGlosasDao().eliminarConceptosDetalleFacturaGlosa(con, detFacturasMap.get("codigo_"+i)+"");
			HashMap<String, Object> detAsociosFacturasMap = new HashMap<String, Object>();
			//se consultan los Asocios del detalle de la Factura
			detAsociosFacturasMap=getRegistrarModificarGlosasDao().consultarAsociosDetalleFacturaGlosa(con, detFacturasMap.get("codigo_"+i)+"");
			for(int z=0;z<Utilidades.convertirAEntero(detAsociosFacturasMap.get("numRegistros")+"")&&transaccion;z++)
			{
				//se eliminan los conceptos de los asocios del detalle de la Factura
				if(transaccion)
					transaccion=getRegistrarModificarGlosasDao().eliminarConceptosAsociosDetalleFacturaGlosa(con, detAsociosFacturasMap.get("codigo_"+z)+"");
			}
			
			//se eliminan los asocios del detalle de la Factura
			if(transaccion)
				transaccion=getRegistrarModificarGlosasDao().eliminarAsociosDetalleFacturaGlosa(con, detFacturasMap.get("codigo_"+i)+"");
		}
		
		//se eliminan los detalles de la Factura
		if(transaccion)
			transaccion=getRegistrarModificarGlosasDao().eliminarDetalleFacturaGlosa(con, codAudi);
		
		//se eliminan los conceptos de la Auditoria de la Glosa
		if(transaccion)
			transaccion=getRegistrarModificarGlosasDao().eliminarConceptosAudiGlosa(con, codAudi);
		
		//se elimina la Auditoria de la Glosa
		if(transaccion)
			transaccion=getRegistrarModificarGlosasDao().eliminarAuditoriaGlosa(con, codAudi);
		 
		if(transaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
			logger.info("\n\n#####################TRANSACCION FINALIZADA CON EXITO###################");
			return true;
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			logger.info("\n\n#####################ERROR. TRANSACCION ABORTADA######################");
		}		
		return false;
	}
	
	/**
	 * Metodo que consulta los conceptos de la Glosa
	 * @param con
	 * @param estado
	 * @param tipoConcepto
	 * @return
	 */
	public HashMap consultaConceptos(Connection con, String estado, String tipoConcepto)
	{		
		return getRegistrarModificarGlosasDao().consultarConceptos(con, estado, tipoConcepto);
	}
	
	/**
	 * Metodo que consulta todos los conceptos de todas las
	 * Facturas asociadas a una glosa desde sus solicitudes
	 * hasta los asocios de las mismas
	 * @param con
	 * @param codigoGlosa
	 * @return
	 */
	public static HashMap consultarTodosConceptosFacturas(Connection con, String codigoGlosa)
	{
		return getRegistrarModificarGlosasDao().consultarTodosConceptosFacturas(con, codigoGlosa);
	}
	
	/**
	 * 
	 * @param codigo
	 * @return
	 */
	public static String obtenerFechaAprobacionGlosa(String codigo)
	{
		return getRegistrarModificarGlosasDao().obtenerFechaAprobacionGlosa(codigo);
	}

	
	/**
	 * Metodo que guarda los conceptos de la auditoria Glosa
	 * @param con
	 * @param criterios
	 * @param operacion
	 * @return
	 */
	public boolean guardarConceptoFactura(Connection con, HashMap criterios)
	{
		return getRegistrarModificarGlosasDao().guardarConceptoFactura(con, criterios);
	}
	
	/**
	 * Metodo que consulta las fechas de Radicacion de las Glosas asociadas a una Factura
	 * @param con
	 * @param codFactura
	 * @return
	 */
	public int consultaFechaRadicacion(Connection con, String codFactura, String codGlosa)
	{
		HashMap<String, Object> resultados= new HashMap<String, Object>();
		int numDias=0;
				
		resultados= getRegistrarModificarGlosasDao().consultaFechaRadicacion(con, codFactura);
		
		if(!(resultados.get("fechaglosa_0")+"").equals("") && !(resultados.get("fecharesp_0")+"").equals("") && !(resultados.get("fechaglosa_0")+"").equals("null") && !(resultados.get("fecharesp_0")+"").equals("null"))
			numDias=UtilidadFecha.numeroDiasEntreFechas(UtilidadFecha.conversionFormatoFechaAAp((resultados.get("fechaglosa_0")+"")), UtilidadFecha.conversionFormatoFechaAAp(resultados.get("fecharesp_0")+""));
		else
		{
			resultados= getRegistrarModificarGlosasDao().consultaFechaRadiCC(con, codFactura, codGlosa);
			if(!(resultados.get("fechaglosa_0")+"").equals("") && !(resultados.get("fecharad_0")+"").equals("") && !(resultados.get("fechaglosa_0")+"").equals("null") && !(resultados.get("fecharad_0")+"").equals("null"))
				numDias=UtilidadFecha.numeroDiasEntreFechas(UtilidadFecha.conversionFormatoFechaAAp((resultados.get("fechaglosa_0")+"")), UtilidadFecha.conversionFormatoFechaAAp(resultados.get("fecharad_0")+""));
		}		
		return numDias; 
	}
	
	/**
	 * Metodo que consulta las Glosas en estado Respondida o Conciliada asociadas a una factura especifica
	 * @param con
	 * @param codFatura
	 * @return
	 */
	public int consultarNumeroGlosasPorFactura(Connection con, String codFactura)
	{
		return getRegistrarModificarGlosasDao().consultarNumeroGlosasPorFactura(con, codFactura);
	}

	/**
	 * 
	 * @param con
	 * @param bigDecimal
	 * @return
	 */
	public static ArrayList<DtoObsFacturaGlosas> consultarObservacionesAuditoriasGlosas(Connection con, int codigoFactura) 
	{
		return getRegistrarModificarGlosasDao().consultarObsAuditoriaGlosa(con, codigoFactura);
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean insertarObservacion(Connection con,DtoObsFacturaGlosas dto)
	{
		return getRegistrarModificarGlosasDao().insertarObservacion(con, dto);
	}

	/**
	 * 
	 * @param con
	 * @param codigoGlosa
	 * @return
	 */
	public static String consultarEstadoGlosa(Connection con, String codigoGlosa) 
	{
		return getRegistrarModificarGlosasDao().consultarEstadoGlosa(con,codigoGlosa);
	}
	
}