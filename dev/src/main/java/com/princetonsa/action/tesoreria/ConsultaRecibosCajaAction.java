/*
 * Created on 5/10/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
 */
package com.princetonsa.action.tesoreria;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.tesoreria.ConsultaRecibosCajaForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.tesoreria.ConsultaRecibosCaja;
import com.princetonsa.pdf.ConsultaRecibosCajaPdf;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.fabrica.facturasvarias.FacturasVariasMundoFabrica;
import com.servinte.axioma.mundo.interfaz.facturasvarias.IFacturasVariasMundo;
import com.servinte.axioma.servicio.fabrica.TesoreriaFabricaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IRecibosCajaServicio;

/**
 * @version 1.0, 5/10/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class ConsultaRecibosCajaAction extends Action
{
    /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ConsultaRecibosCajaAction.class);
	/**
	 * Método execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
													        ActionForm form, 
													        HttpServletRequest request, 
													        HttpServletResponse response) throws Exception
	{
	    if(form instanceof ConsultaRecibosCajaForm)
	    {
			ConsultaRecibosCajaForm forma=(ConsultaRecibosCajaForm) form;
			ConsultaRecibosCaja mundo = new ConsultaRecibosCaja();
			String estado = forma.getEstado();
			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			logger.warn("estado [ConsultaRecibosCajaAction.java]-->"+estado);
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo de ConceptosCarteraAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}
			else if (estado.equals("empezar"))
			{
			    forma.reset(usuario.getCodigoInstitucionInt());
			    mundo.reset();
			    return mapping.findForward("paginaPrincipal");
			}
			//Estado agregado x anexo 958
			else if (estado.equals("empezarDesdeFV"))
			{
			    forma.reset(usuario.getCodigoInstitucionInt());
			    forma.setCodigoCentroAtencion(usuario.getCodigoCentroAtencion());
			    
			    forma.setListadoConceptos(Utilidades.obtenerConceptosIngresoTesoreria(ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC, ConstantesIntegridadDominio.acronimoFactura));
			    
			    mundo.reset();
			    return mapping.findForward("paginaPrincipal");
			}
			
			
			else if (estado.equals("buscar"))
			{
				if(forma.getIndexTipoMoneda()>ConstantesBD.codigoNuncaValido)
				{
					forma.setDescripcionConversion("Valores Conversion Moneda: "+forma.getTiposMonedaTagMap("descripciontipomoneda_"+forma.getIndexTipoMoneda())+" "+forma.getTiposMonedaTagMap("simbolotipomoneda_"+forma.getIndexTipoMoneda())+" "+UtilidadTexto.formatearValores(forma.getTiposMonedaTagMap("factorconversion_"+forma.getIndexTipoMoneda())+""));
					forma.setFactorConversion(Utilidades.convertirADouble(forma.getTiposMonedaTagMap("factorconversion_"+forma.getIndexTipoMoneda())+""));
				}
				else
				{
					forma.setDescripcionConversion("");
					forma.setFactorConversion(1);
				}
				
			    this.cargarMundoParaBusqueda(forma,mundo);
				
			    return mapping.findForward("listadoRecibosCaja");
			}
			else if (estado.equals("ordenar"))
			{
			    this.ordenarMapaListadoRecibosCaja(forma);
			    return mapping.findForward("listadoRecibosCaja");
			}
			else if (estado.equals("detalle"))
			{
			    detalleReciboCaja(forma,mundo);
			    return mapping.findForward("detalleRecibosCaja");
			}
			else if (estado.equals("imprimirListado"))
			{
				String nombreArchivo;
				Random r= new Random();
				nombreArchivo= "/aBorrar" + r.nextInt() + ".pdf";
				
				logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
				logger.info("Se va a imprimir.. asignando nombre en Consulta Recibos Caja Action");
				logger.info("<<<<<<<<<<<<<<<<<<<<<");
				logger.info(ValoresPorDefecto.getFilePath()+ nombreArchivo);
				
				ConsultaRecibosCajaPdf.imprimirListado(ValoresPorDefecto.getFilePath()+ nombreArchivo,forma,Utilidades.getUsuarioBasicoSesion(request.getSession()), request);
				request.setAttribute("nombreArchivo",nombreArchivo);
				request.setAttribute("nombreVentana", "Impresion Recibos Caja");
				return mapping.findForward("abrirPdf");
			}
			else if (estado.equals("imprimirDetalle"))
			{
    			imprimirReciboCaja(request, forma, usuario);
    			
    			return mapping.findForward("detalleRecibosCaja");
			}
			else{
				
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}
		}
		else
		{
			logger.error("El form no es compatible con el form de ConceptosCarteraForm");
			request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
			return mapping.findForward("paginaError");
		}
	}
	
	/**
	 * 
	 * @param forma
	 * @param mundo
	 */
	@SuppressWarnings("unchecked")
	private void detalleReciboCaja(ConsultaRecibosCajaForm forma,ConsultaRecibosCaja mundo)
	{
		try{
			HibernateUtil.beginTransaction();
			forma.setNumeroReciboCaja(forma.getRecibosCaja().get("numerorecibo_"+forma.getIndexReciboDetalle())+"");
		    forma.setConsecutivoReciboCaja(forma.getRecibosCaja().get("consecutivorc_"+forma.getIndexReciboDetalle())+"");
		    forma.setFechaReciboCaja(forma.getRecibosCaja().get("fecha_"+forma.getIndexReciboDetalle())+"");
		    forma.setHoraReciboCaja(forma.getRecibosCaja().get("hora_"+forma.getIndexReciboDetalle())+"");
		    forma.setCodigoEstadoReciboCaja(Integer.parseInt(forma.getRecibosCaja().get("codigoestado_"+forma.getIndexReciboDetalle())+""));
		    forma.setDescripcionEstadoReciboCaja(forma.getRecibosCaja().get("descripcionestado_"+forma.getIndexReciboDetalle())+"");
		    forma.setUsuarioReciboCaja(forma.getRecibosCaja().get("usuario_"+forma.getIndexReciboDetalle())+" - "+forma.getRecibosCaja().get("nombreusuario_"+forma.getIndexReciboDetalle()));
		    forma.setUsuarioElaboraReciboCaja(forma.getRecibosCaja().get("usuario_"+forma.getIndexReciboDetalle())+" - "+forma.getRecibosCaja().get("nombreusuario_"+forma.getIndexReciboDetalle()));
		    forma.setConsecutivoCaja(forma.getRecibosCaja().get("consecutivocaja_"+forma.getIndexReciboDetalle())+"");
		    forma.setCodigoCaja(forma.getRecibosCaja().get("codigocaja_"+forma.getIndexReciboDetalle())+"");
		    forma.setDescripcionCaja(forma.getRecibosCaja().get("descripcioncaja_"+forma.getIndexReciboDetalle())+"");
		    forma.setRecibidoDe(forma.getRecibosCaja().get("recibidode_"+forma.getIndexReciboDetalle())+"");
		    forma.setObservaciones(forma.getRecibosCaja().get("observaciones_"+forma.getIndexReciboDetalle())+"");
		    forma.setValorTotalReciboCaja(forma.getRecibosCaja().get("valortotal_"+forma.getIndexReciboDetalle())+"");
		    
		    forma.setTipoIdBeneficiario(forma.getRecibosCaja().get("tipoIdBeneficiario_"+forma.getIndexReciboDetalle())+"");
		    forma.setNumeroIdBeneficiario(forma.getRecibosCaja().get("idBeneficiario_"+forma.getIndexReciboDetalle())+"");
		    
		  
		    Log4JManager.info("-------------------------- pos sel-" + forma.getRecibosCaja().get("nombreusuario_"+forma.getIndexReciboDetalle()) +"-");
			
		    ////////*****************CONSULTA DE LOS CONCEPTOS DEL RECIBO DE CAJA
		    mundo.consultarConceptosReciboCaja(forma.getInstitucion(),forma.getConsecutivoReciboCaja());
		    forma.setConceptosRC((HashMap)mundo.getConceptosRC().clone());
		    
		 
			//intentamos abrir una conexion con la fuente de datos 
		    Connection con=HibernateUtil.obtenerConexion();
	
			if (Utilidades.obtenerFiltroValorConceptoIngreso(con,Utilidades.convertirAEntero(forma.getConceptosRC().get("codigoconceptorc_0")+"")).equals(ConstantesIntegridadDominio.acronimoFactura))
			{
				String codigoFactura = (String) forma.getConceptosRC().get("docsoporteconceptorc_0");
				
				if(!UtilidadTexto.isEmpty(codigoFactura) && UtilidadTexto.isNumber(codigoFactura)){
					
					/*
					 * Se realiza modificación para no mostrar el sufijo de la factura varia
					 * MT-1561
					 */
					IFacturasVariasMundo facturasVariasMundo = FacturasVariasMundoFabrica.crearFacturasVariasMundo();
					long codigoFacVar = Utilidades.convertirALong(codigoFactura);
					long consecutivoFacVar = 0;
					
					if (codigoFacVar != ConstantesBD.codigoNuncaValidoLong) {
						consecutivoFacVar = facturasVariasMundo.obtenerConsecutivoFacturaVaria(codigoFacVar);
					}
					
					forma.getConceptosRC().put("docsoporteconceptorc_0", consecutivoFacVar);
					//IFacturasVariasServicio facturasVariasServicio = FacturasVariasFabricaServicio.crearFacturasVariasServicio();
						
					//String prefijoConsecutivo = facturasVariasServicio.obtenerPrefijoConsecutivo(codigo);
				}
			}
		    
		    ////////*****************CONSULTA DE LAS FORMAS DE PAGO DEL RECIBO DE CAJA
		    mundo.consultarFormasPagoReciboCaja(forma.getInstitucion(),forma.getConsecutivoReciboCaja());
		    forma.setFormasPagoRC((HashMap)mundo.getFormasPagoRC().clone());
		    
		    if(forma.getCodigoEstadoReciboCaja()==ConstantesBD.codigoEstadoReciboCajaAnulado) {
		        ///////********************REALIZAR LA CONSULTA DE LA INFORMACION DE LA ANULACION DE RECIBO DE CAJA
		        mundo.consultarAnulacionReciboCaja(forma.getInstitucion(),forma.getConsecutivoReciboCaja());
		        forma.setAnulacionRC((HashMap)mundo.getAnulacionRC().clone());
		    }
		    
		    //Busco los detalles de la devolucion - Tarea 1593
		    forma.setListadoDevolucion(mundo.consultarDevolucionesAprobadas(forma.getInstitucion(), forma.getConsecutivoReciboCaja()));
		    HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error(e);
			HibernateUtil.abortTransaction();
		}
	}
	
    /**
     * @param forma
     */
    private void ordenarMapaListadoRecibosCaja(ConsultaRecibosCajaForm forma)
    {
    	String totalRC=forma.getRecibosCaja().get("totalRC").toString();
    	String totalRCAnulados=forma.getRecibosCaja().get("totalRCAnulados").toString();
    	String totalRCMenosAnulados=forma.getRecibosCaja().get("totalRCMenosAnulados").toString();
        String[] indices={"numerorecibo_","consecutivorc_","fecha_","hora_","codigoestado_","descripcionestado_","usuario_","nombreusuario_","valortotal_","observaciones_","consecutivocaja_","codigocaja_","descripcioncaja_","recibidode_", "nombrecentroatencion_", "idBeneficiario_", "tipoIdBeneficiario_"};
        int numReg=Integer.parseInt(forma.getRecibosCaja().get("numeroregistros")+"");
        forma.setRecibosCaja(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getRecibosCaja(),numReg));
        forma.setUltimoPatron(forma.getPatronOrdenar());
        forma.getRecibosCaja().put("numeroregistros",numReg+"");
        forma.getRecibosCaja().put("totalRC",totalRC);
        forma.getRecibosCaja().put("totalRCAnulados",totalRCAnulados);
        forma.getRecibosCaja().put("totalRCMenosAnulados",totalRCMenosAnulados);
    }

    /**
	 * Metodo para cargar el mundo con los datos de la busqueda
	 * @param forma
	 * @param mundo
	 */
	public void cargarMundoParaBusqueda(ConsultaRecibosCajaForm forma,ConsultaRecibosCaja mundo)
	{
		mundo.setInstitucion(forma.getInstitucion());
	    mundo.setReciboCajaInicial(forma.getReciboCajaInicial());
	    mundo.setReciboCajaFinal(forma.getReciboCajaFinal());
	    mundo.setFechaReciboCajaInicial(forma.getFechaReciboCajaInicial());
	    mundo.setFechaReciboCajaFina(forma.getFechaReciboCajaFina());
	    mundo.setCodigoConceptoReciboCaja(forma.getCodigoConceptoReciboCaja());
	    mundo.setEstadoReciboCaja(forma.getEstadoReciboCaja());
	    mundo.setUsuarioElaboraReciboCaja(forma.getUsuarioElaboraReciboCaja());
	    mundo.setCajaElaboraReciboCaja(forma.getCajaElaboraReciboCaja());
	    mundo.setCodigoFormaPago(forma.getCodigoFormaPago());
	    mundo.setCodigoCentroAtencion(forma.getCodigoCentroAtencion());
	    mundo.setDocSoporte(forma.getDocSoporte());
	    mundo.setTipoIdBeneficiario(forma.getTipoIdBeneficiario());
	    mundo.setNumeroIdBeneficiario(forma.getNumeroIdBeneficiario());
	    
	    mundo.ejecutarBusquedaAvanzada();
	    forma.setRecibosCaja((HashMap)mundo.getRecibosCaja().clone());
	}
	
	
	/**
	 * Método que se encarga de imprimir el detalle del recibo de caja
	 * 
	 * @param request
	 * @param forma
	 * @param usuario
	 */
	private void imprimirReciboCaja(HttpServletRequest request,	ConsultaRecibosCajaForm forma, UsuarioBasico usuario) {
		
		try{
			HibernateUtil.beginTransaction();
			InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			IRecibosCajaServicio recibosCajaServicio  = TesoreriaFabricaServicio.crearRecibosCajaServicio();
			
			HashMap<String, String> parametros = new HashMap<String, String>();
			
			parametros.put("numReciboCaja", forma.getNumeroReciboCaja());
			parametros.put("consecutivorc", forma.getConsecutivoReciboCaja());
			parametros.put("identificacionPaciente", forma.getNumeroIdBeneficiario());
			parametros.put("tipoIdentificacion", forma.getTipoIdBeneficiario());
			parametros.put("observacionesImprimir", forma.getObservaciones());
			parametros.put("usuarioElabora", forma.getRecibosCaja().get("nombreusuario_"+forma.getIndexReciboDetalle()) +"");
			parametros.put("funcionalidadOrigen", "ConsultaRecibosCaja");
			
			String newPathReport = recibosCajaServicio.imprimirReciboCaja(usuario, institucion, parametros);
			
			if (!newPathReport.equals("")) 
			{
				Log4JManager.info("-------------------------- pos sel " + forma.getIndexReciboDetalle());
				Log4JManager.info("-------------------------- pos sel-" + forma.getRecibosCaja().get("nombreusuario_"+forma.getIndexReciboDetalle()) +"-");
				
				request.setAttribute("isOpenReport", "true");
				request.setAttribute("newPathReport", newPathReport);
			}
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error(e);
			HibernateUtil.abortTransaction();
		}
	}
}
