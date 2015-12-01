package com.princetonsa.mundo.glosas;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.AprobacionAjustesEmpresaDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.glosas.RegistrarRespuestaDao;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cartera.AjustesDetalleFacturaEmpresa;
import com.princetonsa.mundo.cartera.AjustesEmpresa;
import com.princetonsa.mundo.cartera.AjustesFacturaEmpresa;

/**
 * @author Diego Bedoya
 * Registrar Respuesta (GLOSAS)
 */
public class RegistrarRespuesta
{
	/**
	 * Manejo de Logs
	 */
	
	private static AprobacionAjustesEmpresaDao aprobacionDao;
	
	private static Logger logger = Logger.getLogger(RegistrarRespuesta.class);
	
	static RegistrarRespuestaDao getRegistrarRespuestaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistrarRespuestaDao();
	}
	
	/**
	 * Medtodo que consulta el encabezado de una glosa
	 * @param con
	 * @param glosaSistema
	 * @return
	 */
	public static HashMap consultaEncabezadoGlosa(Connection con, String glosaSistema, String llamado)
	{
		return getRegistrarRespuestaDao().consultaEncabezadoGlosa(con, glosaSistema, llamado);
	}
	
	/**
	 * Metodo que consulta la respuesta de la glosa si la hay con detalle facturas
	 * @param con
	 * @param glosaSistema
	 * @return
	 */
	public HashMap consultaRespuestaDetalleGlosa(Connection con, String glosaSistema)
	{
		return getRegistrarRespuestaDao().consultaRespuestaDetalleGlosa(con, glosaSistema);
	}
	
	/**
	 * Metodo que consulta todas las facturas asociadas a una respeusta glosa
	 * @param con
	 * @param respuesta
	 * @return
	 */
	public HashMap consultaFacturasRespuestaGlosa(Connection con, String respuesta)
	{
		return getRegistrarRespuestaDao().consultaFacturasRespuestaGlosa(con, respuesta);
	}
	
	/**
	 * Metodo que consulta las facturas asociadas a una glosa
	 * @param con
	 * @param codGlosa
	 * @return
	 */
	public HashMap consultaFacturasPorGlosa(Connection con, String codGlosa, String factura, String fecha)
	{
		return getRegistrarRespuestaDao().consultaFacturasPorGlosa(con, codGlosa, factura, fecha);
	}
	
	/**
	 * Metodp que inserta un encabezado respuesta
	 * @param con
	 * @param criterios
	 * @return
	 */
	public int insertarEncabezadoRespuesta(Connection con, HashMap criterios)
	{
		return getRegistrarRespuestaDao().insertarEncabezadoRespuesta(con, criterios);
	}
	
	/**
	 * Metodo que actualiza los datos basicos de una respuesta
	 * @param con
	 * @param criterios
	 * @return
	 */
	public boolean actualizarEncabezadoRespuesta(Connection con, HashMap criterios)
	{
		return getRegistrarRespuestaDao().actualizarEncabezadoRespuesta(con, criterios);
	}
	
	/**
	 * Metodo que inserta cada detalle factura de respuesta
	 * @param con
	 * @param criterios
	 * @return
	 */
	public int insertarDetalleRespuesta(Connection con, HashMap criterios)
	{
		return getRegistrarRespuestaDao().insertarDetalleRespuesta(con, criterios);
	}
	
	/**
	 * Metodo que elimina todos los asocios de respuesta glosa
	 * @param con
	 * @param codigoDetalleFactura
	 * @return
	 */
	public boolean eliminarAsociosRespuesta(Connection con, String codigoDetalleFactura)
	{
		return getRegistrarRespuestaDao().eliminarAsociosRespuesta(con, codigoDetalleFactura);
	}
	
	/**
	 * Metodo que elimina todos los detalles de factura Respuesta
	 * @param con
	 * @param facturaRespuesta
	 * @return
	 */
	public boolean eliminarDetalleFacturaRespuesta(Connection con, String facturaRespuesta)
	{
		return getRegistrarRespuestaDao().eliminarDetalleFacturaRespuesta(con, facturaRespuesta);
	}
	
	/**
	 * Metodo que elimina todos los detalles de facturas Externas Respuesta
	 * @param con
	 * @param facturaRespuesta
	 * @return
	 */
	public boolean eliminarDetalleFacturaExtRespuesta(Connection con, String facturaRespuesta)
	{
		return getRegistrarRespuestaDao().eliminarDetalleFacturaExtRespuesta(con, facturaRespuesta);
	}
	
	/**
	 * Metodo que elimina una factura Respuesta
	 * @param con
	 * @param codrespuesta
	 * @param codfactura
	 * @return
	 */
	public boolean eliminarFacturaRespuesta(Connection con, String codfacresp)
	{
		return getRegistrarRespuestaDao().eliminarFacturaRespuesta(con, codfacresp);
	}
	
	/**
	 * Metodo que consulta el detalle factura respuesta
	 * @param con
	 * @param codfacresp
	 * @return
	 */
	public HashMap consultaDetalleFacturaRespuesta(Connection con, String codfacresp)
	{
		return getRegistrarRespuestaDao().consultaDetalleFacturaRespuesta(con, codfacresp);
	}
	
	/**
	 * Metodo que dirige el almacenamiento o actualizacion de la Respuesta con su detalles
	 * @param con
	 * @param encabezadoRespuesta
	 * @param detalleRespuesta
	 * @param operacion
	 * @return
	 */
	public boolean guardarRespuestaConDetalleGlosa(Connection con, HashMap informacionRespuestaMap, HashMap detalleRespuestaMap, String operacion, UsuarioBasico usuario)
	{
		String consecutivoResp="";
		double contador=0;
		
		Utilidades.imprimirMapa(informacionRespuestaMap);
		Utilidades.imprimirMapa(detalleRespuestaMap);
		
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		//se recorre el mapa para sumar los valores de las facturas que se asociaron a la respuesta
		//y asi tener un total valor respuesta
		for(int x=0;x<Utilidades.convertirAEntero(detalleRespuestaMap.get("numRegistros")+"");x++)
		{
			if((detalleRespuestaMap.get("eliminar_"+x)+"").equals(ConstantesBD.acronimoNo))
				contador += Utilidades.convertirADouble(detalleRespuestaMap.get("valorglosa_"+x)+"");
		}
		
		if(operacion.equals("INSERTAR"))
		{
			HashMap<String, Object> criterios = new HashMap<String, Object>();
			//cuando se va a insertar una nueva Respuesta Glosa se genera un consecutivo
			consecutivoResp=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoRespuestaGlosas, usuario.getCodigoInstitucionInt());
			//se carga el mapa para insertar
			criterios.put("consecutivo", consecutivoResp);
			criterios.put("fecharesp", informacionRespuestaMap.get("fecharesp")+"");
			criterios.put("usuarioresp", usuario.getLoginUsuario());
			criterios.put("conciliacion", informacionRespuestaMap.get("conciliacion")+"");
			criterios.put("observaciones", informacionRespuestaMap.get("observresp")+"");
			criterios.put("estado", ConstantesIntegridadDominio.acronimoEstadoRespuestaGlosaRegistrada);
			criterios.put("institucion", usuario.getCodigoInstitucionInt());
			criterios.put("usuariomod", usuario.getLoginUsuario());
			criterios.put("valorresp", contador);
			criterios.put("codglosa", informacionRespuestaMap.get("codglosa")+"");
			//guardar nuevo encabezado respeusta glosa
			int codigoSeq=this.insertarEncabezadoRespuesta(con, criterios);
			if(codigoSeq>0)
			{
				informacionRespuestaMap.put("codrespuesta", codigoSeq);
				transaccion=true;
			}
			else
				transaccion=false;
		}
		else
		{
			if(operacion.equals("ACTUALIZAR"))
			{
				if(transaccion)
				{
					HashMap<String, Object> criterios = new HashMap<String, Object>();
					//se carga el mapa para actualizar
					criterios.put("conciliacion", informacionRespuestaMap.get("conciliacion")+"");
					criterios.put("observaciones", informacionRespuestaMap.get("observresp")+"");
					criterios.put("usuariomod", usuario.getLoginUsuario());
					criterios.put("valorresp", contador);
					criterios.put("codresp", informacionRespuestaMap.get("codrespuesta")+"");
					//actualiza el encabezado
					transaccion=this.actualizarEncabezadoRespuesta(con, criterios);
				}
			}
		}
		if(transaccion)
		{
			//se recorren las facturas de la respuesta
			for(int j=0;j<Utilidades.convertirAEntero(detalleRespuestaMap.get("numRegistros")+"");j++)
			{
				//si son para eliminar
				if((detalleRespuestaMap.get("eliminar_"+j)+"").equals(ConstantesBD.acronimoSi) && (detalleRespuestaMap.get("esconsulta_"+j)+"").equals(ConstantesBD.acronimoSi))
				{
					HashMap<String, Object> detalleFacturaRMap = new HashMap<String, Object>();
					//se consultan los detalles por factura
					detalleFacturaRMap=this.consultaDetalleFacturaRespuesta(con, detalleRespuestaMap.get("codfacresp_"+j)+"");
					//se recorren los detalles por factura
					for(int z=0;z<Utilidades.convertirAEntero(detalleFacturaRMap.get("numRegistros")+"");z++)
					{
						//se eliminan todo los asocios ligados a cada detalle factura respuesta
						if(transaccion)
							transaccion=this.eliminarAsociosRespuesta(con, detalleFacturaRMap.get("codigo_"+z)+"");
					}
					//se eliminan todos los detalles factura interna ligados a cada factura
					if(transaccion)
						transaccion=this.eliminarDetalleFacturaRespuesta(con, detalleRespuestaMap.get("codfacresp_"+j)+"");
					//se eliminan todos los detalles factura externa ligados a cada factura
					if(transaccion)
						transaccion=this.eliminarDetalleFacturaExtRespuesta(con, detalleRespuestaMap.get("codfacresp_"+j)+"");
					//se elimina la factura ligada a la respuesta
					if(transaccion)
						transaccion=this.eliminarFacturaRespuesta(con, detalleRespuestaMap.get("codfacresp_"+j)+"");
				}
				//si es nueva factura asociada a la respuesta y no se elimino luego
				if((detalleRespuestaMap.get("esconsulta_"+j)+"").equals(ConstantesBD.acronimoNo) && (detalleRespuestaMap.get("eliminar_"+j)+"").equals(ConstantesBD.acronimoNo))
				{
					HashMap<String, Object> criterios = new HashMap<String, Object>();
					//se carga mapa con datos fact respeusta glosa
					criterios.put("codresp", informacionRespuestaMap.get("codrespuesta")+"");
					criterios.put("codfactura", detalleRespuestaMap.get("codfactura_"+j)+"");
					criterios.put("codaudi", detalleRespuestaMap.get("codaudi_"+j)+"");
					criterios.put("valorfact", detalleRespuestaMap.get("valorglosa_"+j)+"");
					criterios.put("sistema", UtilidadTexto.convertirSN(detalleRespuestaMap.get("sistema_"+j)+""));
					if(transaccion)
					{
						int codigoSeqFac=this.insertarDetalleRespuesta(con, criterios);
						if(codigoSeqFac>0)
						{
							transaccion=true;
							detalleRespuestaMap.put("codfacresp_"+j, codigoSeqFac);
							detalleRespuestaMap.put("esconsulta_"+j, ConstantesBD.acronimoSi);
						}
						else
							transaccion=false;
					}
				}
			}
		}
		if(transaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
			if(operacion.equals("INSERTAR"))
			{
				//si guardo encabezado se finaliza consecutivo
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoRespuestaGlosas, usuario.getCodigoInstitucionInt(), consecutivoResp, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
			}
			logger.info("\n\n#####################TRANSACCION FINALIZADA CON EXITO###################");
			return true;
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			if(operacion.equals("INSERTAR"))
			{
				//sino guardo encabezado se deja consecutivo pa reutilizar
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoRespuestaGlosas, usuario.getCodigoInstitucionInt(), consecutivoResp, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
			}
			logger.info("\n\n#####################ERROR. TRANSACCION ABORTADA######################");
		}		
		return false;
	}
	
	/**
	 * Metodo que guarda el detalle Factura Externa de la Respuesta Glosa
	 * @param con
	 * @param solicitudesMap
	 * @param asociosMap
	 * @param usuario
	 * @return
	 */
	public static boolean guardarDetalleFacturaExt(Connection con, HashMap detalleFacturaExtMap, UsuarioBasico usuario)
	{
		// Inicio de transaccion
		boolean transaccion = UtilidadBD.iniciarTransaccion(con);
		
		for (int i = 0; i < Utilidades.convertirAEntero(detalleFacturaExtMap.get("numRegistros")+""); i++) 
		{
			AjustesEmpresa mundoAjustes = new AjustesEmpresa();
			AjustesFacturaEmpresa mundoAjustesFactura = new AjustesFacturaEmpresa();
			AjustesDetalleFacturaEmpresa mundoAjusteDetalleFactura = new AjustesDetalleFacturaEmpresa();
			double codigoAjusteInsertado=ConstantesBD.codigoNuncaValidoDoubleNegativo;
			int codigoDetalleFacturaRespInsertado=ConstantesBD.codigoNuncaValido;
			// Solo si se eligio ajustar solicitud serv / art por concepto respuesta
			if((detalleFacturaExtMap.get("checkajuste_"+i)+"").equals(ConstantesBD.acronimoSi))
			{
				HashMap<String, Object> mapa = new HashMap<String, Object>();
				String codigoConcepto=(detalleFacturaExtMap.get("codconceptor_"+i)+"").split(ConstantesBD.separadorSplit)[1];
				String codConceptoAjuste=(detalleFacturaExtMap.get("codconceptor_"+i)+"").split(ConstantesBD.separadorSplit)[2];
				mapa.put("institucion", usuario.getCodigoInstitucionInt());
				mapa.put("castigocartera", "false");
				mapa.put("conceptocastigocartera", "");
				mapa.put("usuario", usuario.getLoginUsuario());
				mapa.put("cuentacobro", detalleFacturaExtMap.get("numerocxc_"+i)+"");
				mapa.put("conceptoajuste", codConceptoAjuste);
				mapa.put("metodoajuste", ConstantesBD.tipoMetodoAjusteCarteraManual);
				mapa.put("valorajuste", detalleFacturaExtMap.get("valorresp_"+i)+"");
				mapa.put("observaciones", "");
				mapa.put("estado", ConstantesBD.codigoEstadoCarteraAprobado);
				mapa.put("ajustereversado", "false");
				mapa.put("codajustereversado", "0");
				if(codigoConcepto.equals(ConstantesBD.codigoConceptosCarteraDebito+""))
				{
					mapa.put("naturaleza", ConstantesBD.nombreConsecutivoAjustesDebito);
					mapa.put("tipoAjuste", ConstantesBD.codigoAjusteDebitoCuentaCobro);
				}
				else
				{
					mapa.put("naturaleza", ConstantesBD.nombreConsecutivoAjustesCredito);
					mapa.put("tipoAjuste", ConstantesBD.codigoAjusteCreditoCuentaCobro);
				}
				if(transaccion)
				{
					// inserta encabezado ajuste
					mundoAjustes= generarEncabezadoAjuste(con, mundoAjustes, mapa);
				}
				if(mundoAjustes.getCodigo()>0)
				{
					codigoAjusteInsertado=mundoAjustes.getCodigo();
					transaccion=true;
				}
				else
				{
					transaccion=false;
				}
				
				
				if(transaccion)
				{
					mapa.put("codigoajuste", mundoAjustes.getCodigo());
					mapa.put("factura", detalleFacturaExtMap.get("factura_"+i)+"");
					// inserta ajuste factura
					transaccion=generarAjusteFacturaEmpresa(con, mundoAjustesFactura, mapa);
				}
			}
			
			// Se verifica si el registro de concepto de respuesta es para insertar
			if((detalleFacturaExtMap.get("nuevo_"+i)+"").equals(ConstantesBD.acronimoSi))
			{				
				HashMap<String, Object> mapaInsertFactExt = new HashMap<String, Object>();
				// Se baja el codigo concepto de respuesta del value de cada select
				String codigoConcepto=(detalleFacturaExtMap.get("codconceptor_"+i)+"").split(ConstantesBD.separadorSplit)[0];
				// Se llena el mapa para insertar un nuevo concepto respuesta por concepto glosa solicitud serv/art
				mapaInsertFactExt.put("fact_resp_glosa",detalleFacturaExtMap.get("codfactresp_"+i)+"");
				mapaInsertFactExt.put("concepto_glosa",detalleFacturaExtMap.get("codconcepto_"+i)+"");
				mapaInsertFactExt.put("concepto_respuesta",codigoConcepto);
				mapaInsertFactExt.put("institucion",usuario.getCodigoInstitucionInt());
				mapaInsertFactExt.put("motivo",detalleFacturaExtMap.get("motivoconcresp_"+i)+"");
				mapaInsertFactExt.put("cantidad_respuesta",detalleFacturaExtMap.get("cantidadresp_"+i)+"");
				mapaInsertFactExt.put("valor_respuesta",detalleFacturaExtMap.get("valorresp_"+i)+"");
				mapaInsertFactExt.put("ajuste",codigoAjusteInsertado);
				if(transaccion)
					codigoDetalleFacturaRespInsertado=getRegistrarRespuestaDao().insertarDetalleFacturaExternaRespuesta(con, mapaInsertFactExt);
				if(codigoDetalleFacturaRespInsertado>0)
				{
					transaccion=true;
					detalleFacturaExtMap.put("coddetfactresp_"+i, codigoDetalleFacturaRespInsertado);
				}
				else
				{
					transaccion=false;
				}
			}
			else
			{
				// Se verifica si el registro de concepto de respuesta es para Modificar
				if((detalleFacturaExtMap.get("nuevo_"+i)+"").equals(ConstantesBD.acronimoNo))
				{				
					HashMap<String, Object> mapaUpdateExt = new HashMap<String, Object>();
					// Se baja el codigo concepto de respuesta del value de cada select
					String codigoConcepto=(detalleFacturaExtMap.get("codconceptor_"+i)+"").split(ConstantesBD.separadorSplit)[0];
					// Se llena el mapa para insertar un nuevo concepto respuesta por concepto glosa solicitud serv/art
					mapaUpdateExt.put("concepto_respuesta",codigoConcepto);
					mapaUpdateExt.put("motivo",detalleFacturaExtMap.get("motivoconcresp_"+i)+"");
					mapaUpdateExt.put("cantidad_respuesta",detalleFacturaExtMap.get("cantidadresp_"+i)+"");
					mapaUpdateExt.put("valor_respuesta",detalleFacturaExtMap.get("valorresp_"+i)+"");
					// se verifica si el registro ya tiene un ajuste, para no actualizarlo sino se actualiza con el que se genere
					if(Utilidades.convertirADouble(detalleFacturaExtMap.get("tieneajuste_"+i)+"")>0)
						mapaUpdateExt.put("ajuste",detalleFacturaExtMap.get("tieneajuste_"+i)+"");
					else
						mapaUpdateExt.put("ajuste",codigoAjusteInsertado);
					mapaUpdateExt.put("codigo",detalleFacturaExtMap.get("coddetfactresp_"+i)+"");
					if(transaccion)
						transaccion=getRegistrarRespuestaDao().actualizarDetalleFacturaExternaRespuesta(con, mapaUpdateExt);
				}
			}
		}
		
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
	 * Metodo que guarda el detalle Factura con Asocios de la Respuesta Glosa
	 * @param con
	 * @param solicitudesMap
	 * @param asociosMap
	 * @param usuario
	 * @return
	 */
	public static boolean guardarDetalleFactura(Connection con, HashMap solicitudesMap, HashMap asociosMap, UsuarioBasico usuario, String guardarAjuste)
	{
		// Inicio de transaccion
		boolean transaccion = UtilidadBD.iniciarTransaccion(con);
		ArrayList<String> asociosInsertados= new ArrayList<String>();
		int n=0;
		double valorTotalAjusteD=0;
		double valorTotalAjusteC=0;
		HashMap<String, Object> mapa = new HashMap<String, Object>();
		AjustesEmpresa mundoAjustes = new AjustesEmpresa();
		AjustesFacturaEmpresa mundoAjustesFactura = new AjustesFacturaEmpresa();
		AjustesDetalleFacturaEmpresa mundoAjusteDetalleFactura = new AjustesDetalleFacturaEmpresa();
		double codigoAjusteInsertadoD=ConstantesBD.codigoNuncaValidoDoubleNegativo, codigoAjusteDetInsertadoD= ConstantesBD.codigoNuncaValidoDoubleNegativo;
		double codigoAjusteInsertadoC=ConstantesBD.codigoNuncaValidoDoubleNegativo, codigoAjusteDetInsertadoC= ConstantesBD.codigoNuncaValidoDoubleNegativo;
		int codigoDetalleFacturaRespInsertado=ConstantesBD.codigoNuncaValido;
		int codigoAsocioDetalleFacturaRespInsertado=ConstantesBD.codigoNuncaValido;
		int indiceDeb=0;
		int indiceCre=0;
		
		//*********Insertar las solicitudes*******************//
		
		for (int i=0;i<(Utilidades.convertirAEntero(solicitudesMap.get("numRegistros")+""));i++) 
		{			
			String codigoConcepto0=(solicitudesMap.get("codconceptor_"+i)+"").split(ConstantesBD.separadorSplit)[0];
			String codigoConcepto=(solicitudesMap.get("codconceptor_"+i)+"").split(ConstantesBD.separadorSplit)[1];
			String codConceptoAjuste="";			
			if((solicitudesMap.get("codconceptor_"+i)+"").substring((solicitudesMap.get("codconceptor_"+i)+"").length()-1).equals("@"))
				codConceptoAjuste="0";
			else
				codConceptoAjuste=(solicitudesMap.get("codconceptor_"+i)+"").split(ConstantesBD.separadorSplit)[2];
			
			// Solo si se eligio ajustar solicitud serv / art por concepto respuesta			
			if((solicitudesMap.get("checkajuste_"+i)+"").equals(ConstantesBD.acronimoSi))
			{				
				mapa.put("institucion_"+n, usuario.getCodigoInstitucionInt());
				mapa.put("castigocartera_"+n, "false");
				mapa.put("conceptocastigocartera_"+n, "");
				mapa.put("usuario_"+n, usuario.getLoginUsuario());
				mapa.put("cuentacobro_"+n, solicitudesMap.get("numerocxc_"+i)+"");
				mapa.put("conceptoajuste_"+n, codConceptoAjuste);
				mapa.put("metodoajuste_"+n, ConstantesBD.tipoMetodoAjusteCarteraManual);
				mapa.put("valorajuste_"+n, solicitudesMap.get("valorresp_"+i)+"");
				mapa.put("observaciones_"+n, "");
				mapa.put("estado_"+n, ConstantesBD.codigoEstadoCarteraAprobado);
				mapa.put("ajustereversado_"+n, "false");
				mapa.put("codajustereversado_"+n, "0");
				mapa.put("detfactsolicitud_"+n, solicitudesMap.get("detfactsolicitud_"+i)+"");
				mapa.put("pool_"+n, solicitudesMap.get("pool_"+i)+"");
				mapa.put("codigomedico_"+n, solicitudesMap.get("codigomedico_"+i)+"");
				mapa.put("escirugia_"+n, solicitudesMap.get("escirugia_"+i)+"");
				mapa.put("codigoConcepto_", codigoConcepto0);
				if(codigoConcepto.equals(ConstantesBD.codigoConceptosCarteraDebito+""))
				{
					valorTotalAjusteD += Utilidades.convertirADouble(mapa.get("valorajuste_"+n)+"");
					if((solicitudesMap.get("numerocxc_"+i)+"").equals(""))
					{
						mapa.put("naturaleza_"+n, ConstantesBD.nombreConsecutivoAjustesDebito);
						mapa.put("tipoAjuste_"+n, ConstantesBD.codigoAjusteDebitoFactura);
					}
					else
					{	
						mapa.put("naturaleza_"+n, ConstantesBD.nombreConsecutivoAjustesDebito);
						mapa.put("tipoAjuste_"+n, ConstantesBD.codigoAjusteDebitoCuentaCobro);
					}
				}
				else
				{
					valorTotalAjusteC += Utilidades.convertirADouble(mapa.get("valorajuste_"+n)+"");
					if((solicitudesMap.get("numerocxc_"+i)+"").equals(""))
					{
						mapa.put("naturaleza_"+n, ConstantesBD.nombreConsecutivoAjustesCredito);
						mapa.put("tipoAjuste_"+n, ConstantesBD.codigoAjusteCreditoFactura);
					}
					else
					{
						mapa.put("naturaleza_"+n, ConstantesBD.nombreConsecutivoAjustesCredito);
						mapa.put("tipoAjuste_"+n, ConstantesBD.codigoAjusteCreditoCuentaCobro);
					}
				}		
				n++;						
			}
			/*
			else if((solicitudesMap.get("checkajuste_"+i)+"").equals(ConstantesBD.acronimoNo))
			{
				if(Utilidades.convertirAEntero(codigoConcepto) > 0)
					guardarAjuste=ConstantesBD.acronimoNo;
			}
			*/
		}	
		
		if(n > 0 && guardarAjuste == ConstantesBD.acronimoSi)
		{
			HashMap<String, Object> criterios= new HashMap<String, Object>();
			int encontro=0;
			
			criterios.put("institucion",mapa.get("institucion_"+0));
			criterios.put("castigocartera",mapa.get("castigocartera_"+0));
			criterios.put("conceptocastigocartera",mapa.get("conceptocastigocartera_"+0));
			criterios.put("usuario",mapa.get("usuario_"+0));
			criterios.put("cuentacobro",mapa.get("cuentacobro_"+0));
			criterios.put("conceptoajuste",mapa.get("conceptoajuste_"+0));
			criterios.put("metodoajuste",mapa.get("metodoajuste_"+0));
			criterios.put("naturaleza",mapa.get("naturaleza_"+0)); 
			criterios.put("tipoAjuste",mapa.get("tipoAjuste_"+0)); 
			if(Utilidades.convertirAEntero(mapa.get("tipoAjuste_"+0)+"") == ConstantesBD.codigoAjusteDebitoFactura || 
				Utilidades.convertirAEntero(mapa.get("tipoAjuste_"+0)+"") == ConstantesBD.codigoAjusteDebitoCuentaCobro)
				criterios.put("valorajuste",valorTotalAjusteD);
			if(Utilidades.convertirAEntero(mapa.get("tipoAjuste_"+0)+"") == ConstantesBD.codigoAjusteCreditoFactura || 
				Utilidades.convertirAEntero(mapa.get("tipoAjuste_"+0)+"") == ConstantesBD.codigoAjusteCreditoCuentaCobro)
				criterios.put("valorajuste",valorTotalAjusteC);
			criterios.put("observaciones",mapa.get("observaciones_"+0));
			criterios.put("estado",mapa.get("estado_"+0));
			criterios.put("ajustereversado",mapa.get("ajustereversado_"+0));
			criterios.put("codajustereversado",mapa.get("codajustereversado_"+0));
								
			if(transaccion)
			{
				// inserta encabezado ajuste
				mundoAjustes= generarEncabezadoAjuste(con, mundoAjustes, criterios);
			}
			if(mundoAjustes.getCodigo()>0)
			{
				if(Utilidades.convertirAEntero(criterios.get("tipoAjuste")+"") == ConstantesBD.codigoAjusteDebitoFactura || 
					Utilidades.convertirAEntero(criterios.get("tipoAjuste")+"") == ConstantesBD.codigoAjusteDebitoCuentaCobro)
					codigoAjusteInsertadoD=mundoAjustes.getCodigo();
				if(Utilidades.convertirAEntero(criterios.get("tipoAjuste")+"") == ConstantesBD.codigoAjusteCreditoFactura || 
					Utilidades.convertirAEntero(criterios.get("tipoAjuste")+"") == ConstantesBD.codigoAjusteCreditoCuentaCobro)
					codigoAjusteInsertadoC=mundoAjustes.getCodigo();
				transaccion=true;
			}
			else
				transaccion=false;		
			
			if(transaccion)
			{
				criterios.put("codigoajuste", mundoAjustes.getCodigo());
				criterios.put("factura", solicitudesMap.get("factura_"+0)+"");
				// inserta ajuste factura
				transaccion=generarAjusteFacturaEmpresa(con, mundoAjustesFactura, criterios);				
			}
								
			for(int k=0;k<n;k++)
			{				
				if((Utilidades.convertirAEntero(mapa.get("tipoAjuste_"+k)+"") != Utilidades.convertirAEntero(mapa.get("tipoAjuste_"+0)+"")) && encontro == 0)
				{
					criterios.put("institucion",mapa.get("institucion_"+k));
					criterios.put("castigocartera",mapa.get("castigocartera_"+k));
					criterios.put("conceptocastigocartera",mapa.get("conceptocastigocartera_"+k));
					criterios.put("usuario",mapa.get("usuario_"+k));
					criterios.put("cuentacobro",mapa.get("cuentacobro_"+k));
					criterios.put("conceptoajuste",mapa.get("conceptoajuste_"+k));
					criterios.put("metodoajuste",mapa.get("metodoajuste_"+k));
					criterios.put("naturaleza",mapa.get("naturaleza_"+k)); 
					criterios.put("tipoAjuste",mapa.get("tipoAjuste_"+k)); 
					if(Utilidades.convertirAEntero(mapa.get("tipoAjuste_"+k)+"") == ConstantesBD.codigoAjusteDebitoFactura || 
						Utilidades.convertirAEntero(mapa.get("tipoAjuste_"+k)+"") == ConstantesBD.codigoAjusteDebitoCuentaCobro)
						criterios.put("valorajuste",valorTotalAjusteD);
					if(Utilidades.convertirAEntero(mapa.get("tipoAjuste_"+k)+"") == ConstantesBD.codigoAjusteCreditoFactura || 
						Utilidades.convertirAEntero(mapa.get("tipoAjuste_"+k)+"") == ConstantesBD.codigoAjusteCreditoCuentaCobro)
						criterios.put("valorajuste",valorTotalAjusteC);
					criterios.put("observaciones",mapa.get("observaciones_"+k));
					criterios.put("estado",mapa.get("estado_"+k));
					criterios.put("ajustereversado",mapa.get("ajustereversado_"+k));
					criterios.put("codajustereversado",mapa.get("codajustereversado_"+k));
					encontro=1;
				}
			}		
						
			if(encontro > 0)
			{
				if(transaccion)
				{
					// inserta encabezado ajuste
					mundoAjustes= generarEncabezadoAjuste(con, mundoAjustes, criterios);
				}
				if(mundoAjustes.getCodigo()>0)
				{
					if(Utilidades.convertirAEntero(criterios.get("tipoAjuste")+"") == ConstantesBD.codigoAjusteDebitoFactura || 
						Utilidades.convertirAEntero(criterios.get("tipoAjuste")+"") == ConstantesBD.codigoAjusteDebitoCuentaCobro)
						codigoAjusteInsertadoD=mundoAjustes.getCodigo();
					if(Utilidades.convertirAEntero(criterios.get("tipoAjuste")+"") == ConstantesBD.codigoAjusteCreditoFactura || 
						Utilidades.convertirAEntero(criterios.get("tipoAjuste")+"") == ConstantesBD.codigoAjusteCreditoCuentaCobro)
						codigoAjusteInsertadoC=mundoAjustes.getCodigo();
					transaccion=true;
				}
				else
					transaccion=false;
				
				if(transaccion)
				{
					criterios.put("codigoajuste", mundoAjustes.getCodigo());
					criterios.put("factura", solicitudesMap.get("factura_"+0)+"");
					// inserta ajuste factura
					transaccion=generarAjusteFacturaEmpresa(con, mundoAjustesFactura, criterios);					
				}
			}					
			
			for (int i=0;i<n;i++) 
			{			
				if(transaccion)
				{
					if(Utilidades.convertirAEntero(mapa.get("tipoAjuste_"+i)+"") == ConstantesBD.codigoAjusteDebitoFactura || 
						Utilidades.convertirAEntero(mapa.get("tipoAjuste_"+i)+"") == ConstantesBD.codigoAjusteDebitoCuentaCobro)
						{
							indiceDeb=i;
							criterios.put("codigoajuste",codigoAjusteInsertadoD);
						}
					if(Utilidades.convertirAEntero(mapa.get("tipoAjuste_"+i)+"") == ConstantesBD.codigoAjusteCreditoFactura || 
						Utilidades.convertirAEntero(mapa.get("tipoAjuste_"+i)+"") == ConstantesBD.codigoAjusteCreditoCuentaCobro)
						{
							indiceCre=i;
							criterios.put("codigoajuste",codigoAjusteInsertadoC);
						}
					criterios.put("conceptoajuste",mapa.get("conceptoajuste_"+i));
					criterios.put("valorajuste",mapa.get("valorajuste_"+i)+"");					
					criterios.put("detfactsolicitud", mapa.get("detfactsolicitud_"+i)+"");
					criterios.put("pool", solicitudesMap.get("pool_"+i)+"");
					criterios.put("codigomedico", solicitudesMap.get("codigomedico_"+i)+"");
					criterios.put("escirugia", solicitudesMap.get("escirugia_"+i)+"");
					// inserta ajuste detalle factura
										
					transaccion=generarAjusteDetalleFacturaEmpresa(con, mundoAjusteDetalleFactura, criterios);		
					if(Utilidades.convertirAEntero(mapa.get("tipoAjuste_"+i)+"") == ConstantesBD.codigoAjusteDebitoFactura || 
						Utilidades.convertirAEntero(mapa.get("tipoAjuste_"+i)+"") == ConstantesBD.codigoAjusteDebitoCuentaCobro)
						{
							codigoAjusteDetInsertadoD=mundoAjusteDetalleFactura.getCodigopk();
						}
					if(Utilidades.convertirAEntero(mapa.get("tipoAjuste_"+i)+"") == ConstantesBD.codigoAjusteCreditoFactura || 
						Utilidades.convertirAEntero(mapa.get("tipoAjuste_"+i)+"") == ConstantesBD.codigoAjusteCreditoCuentaCobro)
						{
							codigoAjusteDetInsertadoC=mundoAjusteDetalleFactura.getCodigopk();
						}
				}
			}
			
			if(transaccion)
			{ 
				if(codigoAjusteInsertadoD > 0)
					transaccion=getRegistrarRespuestaDao().actualizarValoresFacturas(con,
							Utilidades.convertirAEntero(solicitudesMap.get("factura_"+0)+""),
							valorTotalAjusteD,usuario.getCodigoInstitucionInt(),Utilidades.convertirAEntero(mapa.get("tipoAjuste_"+indiceDeb)+""));
				if(codigoAjusteInsertadoC > 0)
					transaccion=getRegistrarRespuestaDao().actualizarValoresFacturas(con,
							Utilidades.convertirAEntero(solicitudesMap.get("factura_"+0)+""),
							valorTotalAjusteC,usuario.getCodigoInstitucionInt(),Utilidades.convertirAEntero(mapa.get("tipoAjuste_"+indiceCre)+""));
			}
		}		
				
		for(int i=0;i<(Utilidades.convertirAEntero(asociosMap.get("numRegistros")+""));i++)
		{
			String codigoConcepto2="";
			String codConceptoAjuste2="";
			if(!(asociosMap.get("codconceptor_"+i)+"").equals("-1"))
			{
				codigoConcepto2=(asociosMap.get("codconceptor_"+i)+"").split(ConstantesBD.separadorSplit)[1];
				codConceptoAjuste2=(asociosMap.get("codconceptor_"+i)+"").split(ConstantesBD.separadorSplit)[2];
				
				if((asociosMap.get("checkajuste_"+i)+"").equals(ConstantesBD.acronimoNo))
				{	
					if(Utilidades.convertirAEntero(codigoConcepto2) > 0)
						guardarAjuste = ConstantesBD.acronimoNo;
				}
			}
		}
		
		int cont=0;
		// Mapa para los asocios en el mundo de Ajustes Detalle Factura
		HashMap<String, Object> asociosGuardar = new HashMap<String, Object>();
		
		for (int i=0;i<(Utilidades.convertirAEntero(solicitudesMap.get("numRegistros")+""));i++) 
		{
			// Se verifica si el registro de concepto de respuesta es para insertar
			if((solicitudesMap.get("nuevo_"+i)+"").equals(ConstantesBD.acronimoSi))
			{					
				HashMap<String, Object> mapaInsertSol = new HashMap<String, Object>();
				// Se baja el codigo concepto de respuesta del value de cada select
				String codigoConcepto="";
				if(!(asociosMap.get("codconceptor_"+i)+"").equals("-1"))
					codigoConcepto=(solicitudesMap.get("codconceptor_"+i)+"").split(ConstantesBD.separadorSplit)[0];
				// Se llena el mapa para insertar un nuevo concepto respuesta por concepto glosa solicitud serv/art
				mapaInsertSol.put("fact_resp_glosa",solicitudesMap.get("codfactresp_"+i)+"");
				mapaInsertSol.put("det_auditoria_glosa",solicitudesMap.get("coddetfact_"+i)+"");
				mapaInsertSol.put("det_factura_solicitud",solicitudesMap.get("detfactsol_"+i)+"");
				mapaInsertSol.put("concepto_glosa",solicitudesMap.get("codconcepto_"+i)+"");
				mapaInsertSol.put("concepto_respuesta",codigoConcepto);
				mapaInsertSol.put("institucion",usuario.getCodigoInstitucionInt());
				mapaInsertSol.put("motivo",solicitudesMap.get("motivoconcresp_"+i)+"");
				mapaInsertSol.put("cantidad_respuesta",solicitudesMap.get("cantidadresp_"+i)+"");
				mapaInsertSol.put("valor_respuesta",solicitudesMap.get("valorresp_"+i)+"");
				
				for(int m=0;m<n;m++)
				{
					if((mapaInsertSol.get("det_factura_solicitud")+"").equals(mapa.get("detfactsolicitud_"+m)))
					{							
						if(Utilidades.convertirAEntero(mapa.get("tipoAjuste_"+m)+"") == ConstantesBD.codigoAjusteDebitoFactura || 
							Utilidades.convertirAEntero(mapa.get("tipoAjuste_"+m)+"") == ConstantesBD.codigoAjusteDebitoCuentaCobro)
							mapaInsertSol.put("ajuste",codigoAjusteDetInsertadoD);
						if(Utilidades.convertirAEntero(mapa.get("tipoAjuste_"+m)+"") == ConstantesBD.codigoAjusteCreditoFactura || 
							Utilidades.convertirAEntero(mapa.get("tipoAjuste_"+m)+"") == ConstantesBD.codigoAjusteCreditoCuentaCobro)
							mapaInsertSol.put("ajuste",codigoAjusteDetInsertadoC);
					}
				}
													
				if(transaccion)
					codigoDetalleFacturaRespInsertado=getRegistrarRespuestaDao().insertarDetalleFacturaRespuesta(con, mapaInsertSol);
				if(codigoDetalleFacturaRespInsertado>0)
				{
					transaccion=true;
					solicitudesMap.put("coddetfactresp_"+i, codigoDetalleFacturaRespInsertado);
				}
				else
				{
					transaccion=false;
				}
			}
			else
			{
				// Se verifica si el registro de concepto de respuesta es para Modificar
				if((solicitudesMap.get("nuevo_"+i)+"").equals(ConstantesBD.acronimoNo))
				{	
					HashMap<String, Object> mapaUpdateSol = new HashMap<String, Object>();
					// Se baja el codigo concepto de respuesta del value de cada select
					String codigoConcepto="";
					if(!(asociosMap.get("codconceptor_"+i)+"").equals("-1"))
						codigoConcepto=(solicitudesMap.get("codconceptor_"+i)+"").split(ConstantesBD.separadorSplit)[0];
					// Se llena el mapa para insertar un nuevo concepto respuesta por concepto glosa solicitud serv/art
					mapaUpdateSol.put("concepto_respuesta",codigoConcepto);
					mapaUpdateSol.put("motivo",solicitudesMap.get("motivoconcresp_"+i)+"");
					mapaUpdateSol.put("cantidad_respuesta",solicitudesMap.get("cantidadresp_"+i)+"");
					mapaUpdateSol.put("valor_respuesta",solicitudesMap.get("valorresp_"+i)+"");
					mapaUpdateSol.put("det_fact_solicitud", solicitudesMap.get("detfactsolicitud_"+i));
					// se verifica si el registro ya tiene un ajuste, para no actualizarlo sino se actualiza con el que se genere
					if(Utilidades.convertirADouble(solicitudesMap.get("tieneajuste_"+i)+"")>0)
						mapaUpdateSol.put("ajuste",solicitudesMap.get("tieneajuste_"+i)+"");
					else
					{
						for(int m=0;m<n;m++)
						{
							if((mapaUpdateSol.get("det_fact_solicitud")+"").equals(mapa.get("detfactsolicitud_"+m)))
							{								
								if(Utilidades.convertirAEntero(mapa.get("tipoAjuste_"+m)+"") == ConstantesBD.codigoAjusteDebitoFactura || 
									Utilidades.convertirAEntero(mapa.get("tipoAjuste_"+m)+"") == ConstantesBD.codigoAjusteDebitoCuentaCobro)
									mapaUpdateSol.put("ajuste",mundoAjusteDetalleFactura.getCodigopk());
								if(Utilidades.convertirAEntero(mapa.get("tipoAjuste_"+m)+"") == ConstantesBD.codigoAjusteCreditoFactura || 
									Utilidades.convertirAEntero(mapa.get("tipoAjuste_"+m)+"") == ConstantesBD.codigoAjusteCreditoCuentaCobro)
									mapaUpdateSol.put("ajuste",mundoAjusteDetalleFactura.getCodigopk());
							}
						}
					}
					mapaUpdateSol.put("codigo",solicitudesMap.get("coddetfactresp_"+i)+"");
					if(transaccion)
						transaccion=getRegistrarRespuestaDao().actualizarDetalleFacturaRespuesta(con, mapaUpdateSol);
				}
			}
						
			//se adiciona un key unico para determinar si ya se insertó o no los asocios, los asocios van por solicitud y uno de los n conceptos
			//por esa razon se debe saber si ya se insetó o no.
			if(!asociosInsertados.contains("asocio_"+(solicitudesMap.get("codsolicitud_"+i))))
			{
				asociosInsertados.add("asocio_"+(solicitudesMap.get("codsolicitud_"+i)));
				//------------------------Insertar Los Asocios-----------------------//
				for (int j = 0; j < Utilidades.convertirAEntero(asociosMap.get("numRegistros")+""); j++) 
				{
					String codigoConcepto2="";
					String codConceptoAjuste2="";
					if(!(asociosMap.get("codconceptor_"+i)+"").equals("-1"))
					{
						codigoConcepto2=(asociosMap.get("codconceptor_"+i)+"").split(ConstantesBD.separadorSplit)[1];
						codConceptoAjuste2=(asociosMap.get("codconceptor_"+i)+"").split(ConstantesBD.separadorSplit)[2];
					}
					double codigoAjusteInsertadoAso=ConstantesBD.codigoNuncaValidoDoubleNegativo;
					//************** Si los asocios corresponden a la misma solicitud que se esta iterando en el for Principal*************//
					if((solicitudesMap.get("codsolicitud_"+i)+"").equals(asociosMap.get("codsolicitud_"+j)+""))
					{	
						// Solo si se eligio ajustar Asocio solicitud serv / art por concepto respuesta
						if((asociosMap.get("checkajuste_"+j)+"").equals(ConstantesBD.acronimoSi))
						{	
							HashMap<String, Object> mapaAso = new HashMap<String, Object>();
							if(transaccion)
							{		// se llena mapa para ajuste
									if(codigoConcepto2.equals(ConstantesBD.codigoConceptosCarteraDebito+""))
										mapaAso.put("codigoajuste", codigoAjusteInsertadoD);									
									else if(codigoConcepto2.equals(ConstantesBD.codigoConceptosCarteraCredito+""))
										mapaAso.put("codigoajuste", codigoAjusteInsertadoC);						
																
									mapaAso.put("factura", solicitudesMap.get("factura_"+i));
									mapaAso.put("detasofacsol",solicitudesMap.get("detfactsolicitud_"+i));
									mapaAso.put("servicioasocio",asociosMap.get("servicioaso_"+j));
									mapaAso.put("valorajuste", asociosMap.get("valorresp_"+j)+"");
									mapaAso.put("conceptoajuste", codConceptoAjuste2);
									mapaAso.put("institucion", usuario.getCodigoInstitucionInt());				
									mapaAso.put("consecutivoasodetfac", asociosMap.get("consecutivoasodetfact_"+j));
							
								// inserta encabezado ajuste
								//if(getRegistrarRespuestaDao().insertarAjusteAsocios(mapaAso))
									//transaccion=true;
								//else
									//transaccion=false;
							}
							// Se llena el mapa que necesita el mundo que guarda los asocios ajustes
							asociosGuardar.put("codigoajuste", mapaAso.get("codigoajuste"));
							asociosGuardar.put("factura", mapaAso.get("factura")+"");
							asociosGuardar.put("consecitivoasodetfac_"+cont, asociosMap.get("consecutivoasodetfact_"+j)+"");
							asociosGuardar.put("codigodetfactura_"+cont, asociosMap.get("detfactsolicitud_"+j)+"");
							asociosGuardar.put("codigoservicio_"+cont, asociosMap.get("servicioaso_"+j)+"");
							asociosGuardar.put("codigomedico_"+cont, asociosMap.get("codigomedicoaso_"+j)+"");
							asociosGuardar.put("codigopool_"+cont, asociosMap.get("poolaso_"+j)+"");
							asociosGuardar.put("valorajuste_"+cont, asociosMap.get("valorresp_"+j)+"");
							
							double valAjuste=Utilidades.convertirADouble(asociosMap.get("valorresp_"+j)+"");
							double valAjustePool=0;
					        double valAjusteInstitucion=0;
					        double porcentajePool=Utilidades.convertirADouble(asociosMap.get("porcentajepoolaso_"+j)+"");
					        valAjustePool=valAjuste*porcentajePool/100;
					        valAjusteInstitucion=valAjuste-valAjustePool;
							
					        asociosGuardar.put("valajustepool_"+cont, valAjustePool);
					        asociosGuardar.put("valajusteinstitucion_"+cont, valAjusteInstitucion);
					        asociosGuardar.put("concepto_"+cont, mapaAso.get("conceptoajuste")+"");
					        asociosGuardar.put("institucion", mapaAso.get("institucion")+"");
					        cont++;
						}					
						
						// Se verifica si el registro de Asocio concepto de respuesta es para insertar
						if((asociosMap.get("nuevo_"+j)+"").equals(ConstantesBD.acronimoSi))
						{				
							HashMap<String, Object> mapaInsertAsoSol = new HashMap<String, Object>();
							// Se baja el codigo concepto de respuesta del value de cada select
							String codigoConcepto=(asociosMap.get("codconceptor_"+j)+"").split(ConstantesBD.separadorSplit)[0];
							// Se llena el mapa para insertar un nuevo concepto respuesta por concepto glosa solicitud serv/art
							mapaInsertAsoSol.put("det_fact_resp_glosa",codigoDetalleFacturaRespInsertado);
							mapaInsertAsoSol.put("asocio_auditoria_glosa",asociosMap.get("codigoasocio_"+j)+"");
							mapaInsertAsoSol.put("asocio_det_factura",asociosMap.get("consecutivoasodetfact_"+j)+"");
							mapaInsertAsoSol.put("concepto_glosa",asociosMap.get("codconcepto_"+j)+"");
							mapaInsertAsoSol.put("concepto_respuesta",codigoConcepto);
							mapaInsertAsoSol.put("institucion",usuario.getCodigoInstitucionInt());
							mapaInsertAsoSol.put("motivo",asociosMap.get("motivoconcresp_"+j)+"");
							mapaInsertAsoSol.put("cantidad_respuesta",asociosMap.get("cantidadresp_"+j)+"");
							mapaInsertAsoSol.put("valor_respuesta",asociosMap.get("valorresp_"+j)+"");
							mapaInsertAsoSol.put("ajuste",codigoAjusteInsertadoAso);
							
							if(transaccion && Utilidades.convertirAEntero(mapaInsertAsoSol.get("concepto_respuesta")+"") > 0)
								codigoAsocioDetalleFacturaRespInsertado=getRegistrarRespuestaDao().insertarAsocioDetalleFacturaRespuesta(con, mapaInsertAsoSol);
							if(Utilidades.convertirAEntero(mapaInsertAsoSol.get("concepto_respuesta")+"") > 0)
							{
								if(codigoAsocioDetalleFacturaRespInsertado>0)
									transaccion=true;
								else
									transaccion=false;
							}
							else
								transaccion=true;
						}
						else
						{
							// Se verifica si el registro de Asocio concepto de respuesta es para Modificar
							if((asociosMap.get("nuevo_"+j)+"").equals(ConstantesBD.acronimoNo))
							{				
								HashMap<String, Object> mapaUpdateAsoSol = new HashMap<String, Object>();
								// Se baja el codigo concepto de respuesta del value de cada select
								String codigoConcepto=(asociosMap.get("codconceptor_"+j)+"").split(ConstantesBD.separadorSplit)[0];
								// Se llena el mapa para insertar un nuevo concepto respuesta por concepto glosa solicitud serv/art
								mapaUpdateAsoSol.put("concepto_respuesta",codigoConcepto);
								mapaUpdateAsoSol.put("motivo",asociosMap.get("motivoconcresp_"+j)+"");
								mapaUpdateAsoSol.put("cantidad_respuesta",asociosMap.get("cantidadresp_"+j)+"");
								mapaUpdateAsoSol.put("valor_respuesta",asociosMap.get("valorresp_"+j)+"");
								// se verifica si el registro ya tiene un ajuste, para no actualizarlo sino se actualiza con el que se genere
								if(Utilidades.convertirADouble(asociosMap.get("tieneajuste_"+j)+"")>0)
									mapaUpdateAsoSol.put("ajuste",asociosMap.get("tieneajuste_"+j)+"");
								else
									mapaUpdateAsoSol.put("ajuste",codigoAjusteInsertadoAso);
								mapaUpdateAsoSol.put("codigo",solicitudesMap.get("coddetfactresp_"+i)+"");
								if(transaccion)
									transaccion=getRegistrarRespuestaDao().actualizarAsocioDetalleFacturaRespuesta(con, mapaUpdateAsoSol);
							}
						}
					}				
					//********************************************Fin**********************************************************************//			
				}	
			}			
		}
		
		asociosGuardar.put("numRegistros", cont);
		
		// Se insertan todos los asocios de la solicitud
		if(transaccion && guardarAjuste == ConstantesBD.acronimoSi)
			transaccion=generarAjusteAsociosDetalleFacturaEmpresa(con, mundoAjusteDetalleFactura, asociosGuardar);		
			
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
	 * Metodo que inserta nuevo encabezado de Ajuste
	 * @param con
	 * @param mundoAjustes
	 * @param criteriosMap
	 * @return
	 */
	private static AjustesEmpresa generarEncabezadoAjuste(Connection con, AjustesEmpresa mundoAjustes, HashMap criteriosMap) 
	{
		// fecha Actual
		String fechaActual = UtilidadFecha.getFechaActual();
		// hora Actual
		String horaActual = UtilidadFecha.getHoraActual();
		// Se obtiene el consecutivo de Ajuste
		String consecutivo=UtilidadBD.obtenerValorConsecutivoDisponible(criteriosMap.get("naturaleza")+"",Utilidades.convertirAEntero(criteriosMap.get("institucion")+""));
		// Se llenan los atributos del mundo Ajuste necesario para generar Encabezado Ajuste
		mundoAjustes.setConsecutivoAjuste(consecutivo);
		mundoAjustes.setTipoAjuste(Utilidades.convertirAEntero(criteriosMap.get("tipoAjuste")+""));
		mundoAjustes.setInstitucion(Utilidades.convertirAEntero(criteriosMap.get("institucion")+""));
		mundoAjustes.setCastigoCartera(UtilidadTexto.getBoolean(criteriosMap.get("castigocartera")+""));
		mundoAjustes.setConceptoCastigoCartera(criteriosMap.get("conceptocastigocartera")+"");
		mundoAjustes.setFechaAjuste(fechaActual);
		mundoAjustes.setFechaElaboracion(fechaActual);
		mundoAjustes.setHoraElaboracion(horaActual);
		mundoAjustes.setUsuario(criteriosMap.get("usuario")+"");
		logger.info("\n\ncuenta cobro:: "+criteriosMap);
		mundoAjustes.setCuentaCobro(Utilidades.convertirADouble(criteriosMap.get("cuentacobro")+""));
		mundoAjustes.setConceptoAjuste(criteriosMap.get("conceptoajuste")+"");
		mundoAjustes.setMetodoAjuste(criteriosMap.get("metodoajuste")+"");
		mundoAjustes.setValorAjuste(Utilidades.convertirADouble(criteriosMap.get("valorajuste")+""));
		mundoAjustes.setObservaciones(criteriosMap.get("observaciones")+"");
		mundoAjustes.setEstado(Utilidades.convertirAEntero(criteriosMap.get("estado")+""));
		mundoAjustes.setAjusteResversado(UtilidadTexto.getBoolean(criteriosMap.get("ajustereversado")+""));
		mundoAjustes.setCodAjusteReversado(Utilidades.convertirADouble(criteriosMap.get("codajustereversado")+""));
		// Se inserta encabezado Ajuste
		if(mundoAjustes.insertarAjusteGeneral(con))
			//si guardo encabezado se finaliza consecutivo
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,criteriosMap.get("naturaleza")+"", Utilidades.convertirAEntero(criteriosMap.get("institucion")+""), consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
		else
			//sino guardo encabezado se deja consecutivo pa reutilizar
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,criteriosMap.get("naturaleza")+"", Utilidades.convertirAEntero(criteriosMap.get("institucion")+""), consecutivo, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
		return mundoAjustes;
	}
	
	/**
	 * Metodo que inserta el Ajuste Factura
	 * @param con
	 * @param mundoAjustes
	 * @param criteriosMap
	 * @return
	 */
	private static boolean generarAjusteFacturaEmpresa(Connection con, AjustesFacturaEmpresa mundoAjustes, HashMap criteriosMap)
	{
		// Se llenan los atributos del mundo Ajuste necesario para generar Ajuste Factura
		mundoAjustes.setCodigo(Utilidades.convertirADouble(criteriosMap.get("codigoajuste")+""));
		mundoAjustes.setFactura(Utilidades.convertirAEntero(criteriosMap.get("factura")+""));
		mundoAjustes.setMetodoAjuste(criteriosMap.get("metodoajuste")+"");
		mundoAjustes.setValorAjuste(Utilidades.convertirADouble(criteriosMap.get("valorajuste")+""));
		mundoAjustes.setConceptoAjuste(criteriosMap.get("conceptoajuste")+"");
		mundoAjustes.setInstitucion(Utilidades.convertirAEntero(criteriosMap.get("institucion")+""));
		// Se inserta Ajuste Factura
		if(mundoAjustes.insertarAjusteFacturaEmpresa(con))
			return true;
		return false;
	}
	
	/**
	 * Metodo que inserta el Ajuste Detalle Factura
	 * @param con
	 * @param mundoAjustes
	 * @param criteriosMap
	 * @return
	 */
	private static boolean generarAjusteDetalleFacturaEmpresa(Connection con, AjustesDetalleFacturaEmpresa mundoAjustes, HashMap criteriosMap)
	{
		// Se llenan los atributos del mundo Ajuste necesario para generar Ajuste Detalle Factura

		mundoAjustes.setCodigo(Utilidades.convertirADouble(criteriosMap.get("codigoajuste")+""));
		mundoAjustes.setFactura(Utilidades.convertirAEntero(criteriosMap.get("factura")+""));
		mundoAjustes.setDetFactSolicitud(Utilidades.convertirAEntero(criteriosMap.get("detfactsolicitud")+""));
		mundoAjustes.setCodigoPool(Utilidades.convertirAEntero(criteriosMap.get("pool")+""));
		mundoAjustes.setCodigoMedicoResponde(Utilidades.convertirAEntero("codigomedico"));
		mundoAjustes.setMetodoAjuste(criteriosMap.get("metodoajuste")+"");
		mundoAjustes.setValorAjuste(Utilidades.convertirADouble(criteriosMap.get("valorajuste")+""));
		
		double valAjuste=Utilidades.convertirADouble(criteriosMap.get("valorajuste")+"");
		double valAjustePool=0;
        double valAjusteInstitucion=0;
        if(UtilidadTexto.getBoolean(criteriosMap.get("escirugia")+""))
        {
        	valAjustePool=valAjuste*Utilidades.obtenerPorcentajePoolFacturacion(con, Utilidades.convertirAEntero(criteriosMap.get("detfactsolicitud")+""))/100;
        	valAjusteInstitucion=valAjuste-valAjustePool;
        }
		
		mundoAjustes.setValorAjustePool(valAjustePool);
		mundoAjustes.setValorAjusteInstitucion(valAjusteInstitucion);
		mundoAjustes.setConceptoAjuste(criteriosMap.get("conceptoajuste")+"");
		mundoAjustes.setInstitucion(Utilidades.convertirAEntero(criteriosMap.get("institucion")+""));
		// Se inserta Ajuste Detalle Factura
		if(mundoAjustes.insertarAjuste(con))
			return true;
		return false;
	}
	
	/**
	 * Metodo que inserta los ajustes de los asocios del detalle factura
	 * @param con
	 * @param mundoAjustes
	 * @param asociosGuardarMap
	 * @return
	 */
	private static boolean generarAjusteAsociosDetalleFacturaEmpresa(Connection con, AjustesDetalleFacturaEmpresa mundoAjustes, HashMap asociosGuardarMap)
	{
		// Se carga el mapa de los asocios
		mundoAjustes.setAsociosServicio(asociosGuardarMap);
		if(mundoAjustes.insertarAsociosServicio(con))
			return true;
		return false;
	}
	
	/**
	 * Metodo que consulta todas las solicitudes servicio/articulo asociadas a una factura glosa
	 * @param con
	 * @param codAudi
	 * @return
	 */
	public HashMap consultaSolicitudesDetalleFactura(Connection con, String codAudi)
	{
		return getRegistrarRespuestaDao().consultaSolicitudesDetalleFactura(con, codAudi);
	}
	
	/**
	 * Metodo que consulta los conceptos respuesta por tipo concepto
	 * @param con
	 * @param tipoConcepto
	 * @return
	 */
	public HashMap consultaConceptosRespuesta(Connection con, String tipoConcepto)
	{
		return getRegistrarRespuestaDao().consultaConceptosRespuesta(con, tipoConcepto);
	}
	
	/**
	 * Metodo que consulta el detalle de una factura externa
	 * @param con
	 * @param codAudi
	 * @return
	 */
	public HashMap consultaDetalleFacturasExternas(Connection con, String codAudi)
	{
		return getRegistrarRespuestaDao().consultaDetalleFacturasExternas(con, codAudi);
	}
	
	/**
	 * Metodo que consulta todos los asocios de todas las solicitudes de una factura glosa
	 * @param con
	 * @param codAudi
	 * @return
	 */
	public HashMap consultaAsociosDetalleFactura(Connection con, String codAudi)
	{
		return getRegistrarRespuestaDao().consultaAsociosDetalleFactura(con, codAudi);
	}
	
	/**
	 * Metodo que inserta un detalle Factura Respuesta
	 * @param con
	 * @param criterios
	 * @return
	 */
	public int insertarDetalleFacturaRespuesta(Connection con, HashMap criterios)
	{
		return getRegistrarRespuestaDao().insertarDetalleFacturaRespuesta(con, criterios);
	}
	
	/**
	 * Metodo que inserta un Asocio detalle Factura Respuesta
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static int insertarAsocioDetalleFacturaRespuesta(Connection con, HashMap criterios)
	{
		return getRegistrarRespuestaDao().insertarAsocioDetalleFacturaRespuesta(con, criterios);
	}
	
	/**
	 * Metodo que actualiza un detalle factura respuesta por concepto
	 * @param con
	 * @param criterios
	 * @return
	 */
	public boolean actualizarDetalleFacturaRespuesta(Connection con, HashMap criterios)
	{
		return getRegistrarRespuestaDao().actualizarDetalleFacturaRespuesta(con, criterios);
	}
	
	/**
	 * Metodo que actualiza un asocio detalle factura respuesta por concepto
	 * @param con
	 * @param criterios
	 * @return
	 */
	public boolean actualizarAsocioDetalleFacturaRespuesta(Connection con, HashMap criterios)
	{
		return getRegistrarRespuestaDao().actualizarAsocioDetalleFacturaRespuesta(con, criterios);
	}
	
	/**
	 * Metodo que inserta un detalle factura externa respuesta
	 * @param con
	 * @param criterios
	 * @return
	 */
	public int insertarDetalleFacturaExternaRespuesta(Connection con, HashMap criterios)
	{
		return getRegistrarRespuestaDao().insertarDetalleFacturaExternaRespuesta(con, criterios);
	}
	
	/**
	 * Metodo que actualiza un detalle factura externa respuesta
	 * @param con
	 * @param criterios
	 * @return
	 */
	public boolean actualizarDetalleFacturaExternaRespuesta(Connection con, HashMap criterios)
	{
		return getRegistrarRespuestaDao().actualizarDetalleFacturaExternaRespuesta(con, criterios);
	}
}