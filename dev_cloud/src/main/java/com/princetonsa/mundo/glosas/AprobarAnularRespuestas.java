package com.princetonsa.mundo.glosas;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.glosas.AprobarAnularRespuestasDao;
import com.princetonsa.dao.sqlbase.glosas.SqlBaseAprobarAnularRespuestasDao;


public class AprobarAnularRespuestas
{
	Logger logger = Logger.getLogger(AprobarAnularRespuestas.class);
	private static AprobarAnularRespuestasDao getAprobarAnularRespuestasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAprobarAnularRespuestasDao();
	}
	
	/**
	 * Metodo encargado de guardar la Glosa y la Respuesta
	 * @param con
	 * @param codrespuesta
	 * @param checkApAn
	 * @param motivo
	 * @param usuario
	 * @return
	 */
	public boolean guardar(Connection con, String codrespuesta, String checkApAn, String motivo, String usuario) {
		HashMap<String, Object> criterios = new HashMap<String, Object>();
		criterios.put("codrespuesta", codrespuesta);
		criterios.put("check", checkApAn);
		criterios.put("motivo", motivo);
		criterios.put("usuario", usuario);
		return getAprobarAnularRespuestasDao().guardar(con, criterios);
	}
	
	/**
	 * Metodo que consulta todas las facturas de la Glosa
	 * @param con
	 * @param codglosa
	 * @return
	 */
	public HashMap consultaNumFacturasGlosa(Connection con, String codglosa) {		
		return getAprobarAnularRespuestasDao().consultaNumFacturasGlosa(con, codglosa);
	}	
	
	/**
	 * Metodo que valida si las faturas de la Glosa corresponden tambien a la Respuesta
	 * @param con
	 * @param codrespuesta
	 * @param codfactura
	 * @return
	 */
	public HashMap validarRespuesta(Connection con, String codrespuesta){
		return getAprobarAnularRespuestasDao().validarRespuesta(con, codrespuesta);
	}
	
	/**
	 * Metodo que consulta el campo conciliacion de la Respuesta
	 * @param con
	 * @param codrespuesta
	 * @return
	 */
	public String consultarConciliar(Connection con, String codrespuesta)
	{
		return SqlBaseAprobarAnularRespuestasDao.consultarConciliar(con, codrespuesta);
	}
	
	/**
	 * Metodo que actualiza el estado de la Glosa
	 * @param con
	 * @param codglosa
	 * @param conciliar
	 * @param estadoResp
	 * @return
	 */
	public boolean guardarEstadoGlosa(Connection con, String codglosa, String conciliar, String estadoResp)
	{
		return SqlBaseAprobarAnularRespuestasDao.guardarEstadoGlosa(con, codglosa, conciliar, estadoResp);		
	}
	
	/**
	 * Metodo que consulta las Facturas asociadas a una Glosa
	 * @param con
	 * @param codGlosa
	 * @return
	 */
	public boolean consultaFacturasGlosa(Connection con, String codGlosa)
	{
		HashMap<String, Object> facturasGlosa= new HashMap<String, Object>();
		HashMap<String, Object> detalleFacturasGlosa= new HashMap<String, Object>();
		HashMap<String, Object> asociosSolicitudDetalleFacturasGlosa= new HashMap<String, Object>();
		HashMap<String, Object> conceptosDetFactRespGlosa= new HashMap<String, Object>();
		HashMap<String, Object> conceptosAsociosDetFactRespGlosa= new HashMap<String, Object>();
		
		int numConceptosDetFactGlosa = 0;
		int numConceptosAsociosDetFactGlosa = 0;		
		
		boolean band= true;
				
		//Se consultan las Facturas asociadas a la GLosa
		facturasGlosa= getAprobarAnularRespuestasDao().consultaFacturasGlosa(con, codGlosa);
		
		for(int i=0;i< (Utilidades.convertirAEntero(facturasGlosa.get("numRegistros")+"")); i++)
		{
			//se consultan las solicitudes de cada Factura d ela Glosa
			detalleFacturasGlosa=getAprobarAnularRespuestasDao().consultaDetalleFacturaGlosa(con, facturasGlosa.get("codaudi_"+i)+"");
						
			for(int j=0;j< (Utilidades.convertirAEntero(detalleFacturasGlosa.get("numRegistros")+"")); j++)
			{				 
				//se consulta el numero de conceptos de cada solicitud de las Facturas de la Glosa 
				//y se almacena en la posicion de la solicitud consultada
				numConceptosDetFactGlosa=getAprobarAnularRespuestasDao().consultaConceptosDetalleFacturaGlosa(con, detalleFacturasGlosa.get("detaudifact_"+j)+"");				
				detalleFacturasGlosa.put("numConceptos_"+j, numConceptosDetFactGlosa);
				
				//se consultan los asocios de cada solicitud de las Facturas de la Respuesta Glosa
				asociosSolicitudDetalleFacturasGlosa= getAprobarAnularRespuestasDao().consultaAsociosSolicitudDetalleFacturaGlosa(con, detalleFacturasGlosa.get("detaudifact_"+j)+"");
				//se consulta el numero de conceptos de cada solicitud de las Facturas de la Respuesta Glosa
				conceptosDetFactRespGlosa.put("numConceptos_"+j, getAprobarAnularRespuestasDao().consultarConceptosDetFacRespGlosa(con, detalleFacturasGlosa.get("detaudifact_"+j)+""));
				for(int k=0;k< (Utilidades.convertirAEntero(asociosSolicitudDetalleFacturasGlosa.get("numRegistros")+"")); k++)
				{
					//se consulta el numero de conceptos por asocios de las Facturas de la Glosa
					numConceptosAsociosDetFactGlosa= getAprobarAnularRespuestasDao().consultaConceptosAsociosSolicitudDetalleFacturaGlosa(con, asociosSolicitudDetalleFacturasGlosa.get("codasocio_"+k)+"");
					asociosSolicitudDetalleFacturasGlosa.put("numConceptos_"+k, numConceptosAsociosDetFactGlosa);
					
					//se consulta el numero de conceptos por asocios de las Facturas de la Respuesta Glosa
					conceptosAsociosDetFactRespGlosa.put("numConceptos_"+k, getAprobarAnularRespuestasDao().consultarConceptosAsociosRespGlosa(con, asociosSolicitudDetalleFacturasGlosa.get("codasocio_"+k)+""));
				}
			}
		}		
		
		//se valida si todos los conceptos de Glosa tienen asociado un concepto de Respuesta
		for(int i=0;i< (Utilidades.convertirAEntero(facturasGlosa.get("numRegistros")+"")) && band; i++)
		{						
			for(int j=0;j< (Utilidades.convertirAEntero(detalleFacturasGlosa.get("numRegistros")+"")) && band; j++)
			{				
				if(!detalleFacturasGlosa.get("numConceptos_"+j).equals(conceptosDetFactRespGlosa.get("numConceptos_"+j)))
					band= false;
				for(int k=0;k< (Utilidades.convertirAEntero(asociosSolicitudDetalleFacturasGlosa.get("numRegistros")+"")) && band; k++)
				{
					if(!asociosSolicitudDetalleFacturasGlosa.get("numConceptos_"+k).equals(conceptosAsociosDetFactRespGlosa.get("numConceptos_"+k)))
						band= false;
				}
			}
		}
		return band;
	}
	
	/**
	 * Metodo que consulta las Facturas asociadas a una Glosa
	 * @param con
	 * @param codGlosa
	 * @return
	 */
	public static boolean validarEstadoAjustes(Connection con, String codRespuesta, String estado)
	{	
		HashMap<String, Object> facturasRta= new HashMap<String, Object>();
		HashMap<String, Object> estadoAjustesFactExt= new HashMap<String, Object>();
		HashMap<String, Object> estadoAjustesSolFactInt= new HashMap<String, Object>();
		HashMap<String, Object> estadoAjustesAsoFactInt= new HashMap<String, Object>();
		String ajusteConceptoFactRespGlosa= "";	
		
		boolean band= true;
				
		//Se consultan las Facturas asociadas a la GLosa
		facturasRta= getAprobarAnularRespuestasDao().validarRespuesta(con, codRespuesta);
						
		for(int i=0;i< (Utilidades.convertirAEntero(facturasRta.get("numRegistros")+"")); i++)
		{
			//se valida si es factura externa o interna
			if((facturasRta.get("facinterna_"+i)+"").equals("N"))
			{
				//se consulta el estado de los ajustes de cada factura segun el concepto glosa para facturas externas
				estadoAjustesFactExt= getAprobarAnularRespuestasDao().consultarEstadoAjustesFacExterna(con, facturasRta.get("codigo_"+i)+"");	
				for(int k=0;i<(Utilidades.convertirAEntero(estadoAjustesFactExt.get("nunRegistros")+""));k++)
				{
					if((estadoAjustesFactExt.get("estado_"+k)+"").equals("Anulado"))
						band=false;
				}
			}
			else if((facturasRta.get("facinterna_"+i)+"").equals("S"))
			{
				//se consulta el estado de los ajustes de cada solicitud de facturas segun el concepto glosa para facturas inetrnas
				estadoAjustesSolFactInt= getAprobarAnularRespuestasDao().consultarEstadoAjustesDetFacInterna(con, facturasRta.get("codigo_"+i)+"");
				for(int k=0;i<(Utilidades.convertirAEntero(estadoAjustesSolFactInt.get("nunRegistros")+""));k++)
				{
					if(!(estadoAjustesSolFactInt.get("estado_"+k)+"").equals(estado))
						band=false;
					estadoAjustesAsoFactInt=getAprobarAnularRespuestasDao().consultarEstadoAjustesAsoFacInterna(con, estadoAjustesSolFactInt.get("codigo_"+i)+"");
					for(int j=0;j<(Utilidades.convertirAEntero(estadoAjustesAsoFactInt.get("nunRegistros")+""));j++)
					{
						if(!(estadoAjustesAsoFactInt.get("estado_"+j)+"").equals(estado))
							band=false;
					}
				}
			}
		}		
		return band;
	}
}