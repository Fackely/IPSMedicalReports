
/*
 * Creado   1/11/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.action.cartera;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadValidacion;

import com.princetonsa.actionform.cartera.AprobacionPagosCarteraForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cartera.AprobacionPagosCartera;
import com.princetonsa.mundo.cartera.CierreSaldoInicialCartera;


/**
* Clase para manejar el workflow de  
 *aprobacion pagos cartera empresa
 *
 * @version 1.0, 1/11/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class AprobacionPagosCarteraAction extends Action 
{
    /**
     * manejador de los logs de la clase
     */
    private Logger logger=Logger.getLogger(AprobacionPagosCarteraAction.class);
    /**
	 * Método execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
													        ActionForm form, 
													        HttpServletRequest request, 
													        HttpServletResponse response) throws Exception
	{
		Connection con = null;		 
		try{
	    if(form instanceof AprobacionPagosCarteraForm)
	    {
	           
		    //intentamos abrir una conexion con la fuente de datos 
			con = UtilidadBD.abrirConexion();
			if(con == null)
			{
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				return mapping.findForward("paginaError");
			}
			AprobacionPagosCarteraForm formAprob=(AprobacionPagosCarteraForm)form;
			AprobacionPagosCartera mundoAprob=new AprobacionPagosCartera ();
			HttpSession sesion = request.getSession();			
			UsuarioBasico usuario = null;
			usuario = getUsuarioBasicoSesion(sesion);
			String estado=formAprob.getEstado();
			logger.warn("estado->"+estado);
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo de AprobacionPagosCarteraAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}	
			else if(estado.equals("empezar") )
			{			    
				 formAprob.reset();				 
				 mundoAprob.reset();				
				 UtilidadBD.cerrarConexion(con);
				 return mapping.findForward("paginaPrincipal");
			}
			else if(estado.equals("generarConsulta") )
			{
			    return this.accionGenerarConsultaAplicacionPagos(con,formAprob,mundoAprob,mapping);			    
			}
			else if(estado.equals("consultarDetalle") )
			{
			    return this.accionGenerarConsultaDetallePagos(con,formAprob,mundoAprob,mapping,usuario);			    
			}			
			else if(estado.equals("generarAprobacion") )
			{
			    return this.validaciones(con,formAprob,mundoAprob,mapping,usuario,request);			    
			}
	    }
	    else
		{
			logger.error("El form no es compatible con el form de AprobacionPagosCarteraForm");
			request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
			return mapping.findForward("paginaError");
		}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
        return null;
	}	
    /**
     * Metodo para generar la aprobacion de
     * la aplicacion del pago
     * @param con
     * @param formAprob
     * @param mundoAprob
     * @param mapping
     * @param usuario
     * @return
     */
    private ActionForward accionGenerarAprobacion(Connection con, 
													            AprobacionPagosCarteraForm formAprob, 
													            AprobacionPagosCartera mundoAprob, 
													            ActionMapping mapping, UsuarioBasico usuario,
													            HttpServletRequest request) 
    {        
        HashMap mapa=new HashMap ();
        mapa=(HashMap)formAprob.getMapaApliPagos("detalle");
        mundoAprob.setMapaApliPagos("facturas",mapa);
        mapa=new HashMap ();
        mapa=mundoAprob.valoresAplicacionPagosBalanceados(con);
        mundoAprob.setMapaApliPagos("cxc",mapa);
        mundoAprob.setInstitucion(usuario.getCodigoInstitucionInt());
        mundoAprob.setNumeroAplicacionPago(formAprob.getCodAplicacionPago()+"");
        mundoAprob.setFechaAprobacion(formAprob.getFechaAprobacion());
        mundoAprob.setUsuario(usuario.getLoginUsuario());
        boolean inserto=mundoAprob.generarAprobacionTrans(con);
        UtilidadBD.closeConnection(con);
        if(inserto)
          return mapping.findForward("paginaResumen");   
        else
        {
            ActionErrors errores = new ActionErrors(); 
            errores.add("no se genero la aprobacion", new ActionMessage("prompt.noSeGraboInformacion"));
            saveErrors(request, errores);
            return mapping.findForward("paginaErroresActionErrors");
        }
    }
    /**
     * metodo para realizar las validaciones.
     * @param con
     * @param formAprob
     * @param mundoAprob
     * @param mapping
     * @param usuario
     * @param request
     * @return
     */
    private ActionForward validaciones (Connection con, 
											            AprobacionPagosCarteraForm formAprob, 
											            AprobacionPagosCartera mundoAprob, 
											            ActionMapping mapping, UsuarioBasico usuario,
											            HttpServletRequest request)
    {
        CierreSaldoInicialCartera cierre=new CierreSaldoInicialCartera();
        int anio=UtilidadFecha.getMesAnioDiaActual("anio");
        int mes=UtilidadFecha.getMesAnioDiaActual("mes");     
        ActionErrors errores = new ActionErrors();
        boolean existeError=false,hayFacturas=false,esPrimero=true;
        HashMap mapa;
        String facturas="",separador="";
        if(cierre.existenCierres(con,anio,mes,ConstantesBD.codigoTipoCierreMensualStr,usuario.getCodigoInstitucionInt()))
        {
            logger.warn("Ya se genero el cierre mensual [PROCESO CANCELADO]");           
            errores.add("existe cierre mensual", new ActionMessage("error.cierre.yaTieneCierreMensual"));    
            existeError=true;
        }
        if(UtilidadValidacion.existeCierreSaldosIniciales(usuario.getCodigoInstitucionInt()))
        {            
            int ultimoMesCierre = cierre.ultimoCierreGenerado(con,UtilidadFecha.getMesAnioDiaActual("anio"),ConstantesBD.codigoTipoCierreSaldoInicialStr,usuario.getCodigoInstitucionInt());
            if(UtilidadFecha.getMesAnioDiaActual("mes")<ultimoMesCierre)
            {
                logger.warn("Cierre Saldo inicial posterior a la aprobacion [PROCESO CANCELADO]");
                errores.add("cierre inicial posterior", new ActionMessage("error.cierre.cierreInicialYaGeneradoParaElMes",ultimoMesCierre+"",UtilidadFecha.getMesAnioDiaActual("anio")+""));
                existeError=true;                              		    		    			 
            }
        }
        if(formAprob.getMapaApliPagos().containsKey("detalle"))
        {
	        mapa=new HashMap ();
	        mapa=(HashMap)formAprob.getMapaApliPagos("detalle");
	        hayFacturas=false;esPrimero=true;
	        facturas="";separador="";
	        for(int k=0;k<Integer.parseInt(mapa.get("numRegistros")+"");k++)
	        {
	         if(Integer.parseInt(mapa.get("estado_facturacion_"+k)+"")!=ConstantesBD.codigoEstadoFacturacionFacturada)   
	         {
	             hayFacturas=true;
	             facturas+=separador+(mapa.get("factura_"+k)+"");
	             esPrimero=false;
	         }	
	         if(!esPrimero)
	             separador="-";
	        }
	        if(hayFacturas)
	         {
		         logger.warn("Facturas sin estado facturada");           
	             errores.add("existe cierre mensual", new ActionMessage("error.facturacion.debenTenerEstado","["+facturas+"]","Facturada"));
	             existeError=true;
	         }
        }
        mundoAprob.setNumeroAplicacionPago(formAprob.getCodAplicacionPago()+"");
        mundoAprob.setInstitucion(usuario.getCodigoInstitucionInt());
        mapa=new HashMap ();
        mapa=mundoAprob.valoresAplicacionPagosBalanceados(con);
        if(mapa!=null && !mapa.isEmpty())
        {
            for(int k=0;k<Integer.parseInt(mapa.get("numRegistros")+"");k++)
            {
            	if(Double.parseDouble(mapa.get("valor_pago_cxc_"+k)+"")!=Double.parseDouble(mapa.get("valor_pagos_fact_"+k)+""))  
                {
                    logger.warn("aplicacion de pago descuadrado");           
                    errores.add("valores descuadrados", new ActionMessage("error.aprobacionPagosEmpresa.valoresDescuadrados",mapa.get("numero_cuenta_cobro_"+k)+""));
                    existeError=true; 
                }
            }                      
        }
        if(formAprob.getMapaApliPagos().containsKey("detalle"))
        {
	        mapa=new HashMap ();
	        mapa=(HashMap)formAprob.getMapaApliPagos("detalle");
	        hayFacturas=false;esPrimero=true;
	        facturas="";separador="";
	        for(int k=0;k<Integer.parseInt(mapa.get("numRegistros")+"");k++)
	        {
	         if(Double.parseDouble(mapa.get("valor_pago_"+k)+"")>Double.parseDouble(mapa.get("saldo_factura_"+k)+""))   
	         {
	             hayFacturas=true;
	             facturas+=separador+(mapa.get("factura_"+k)+"");
	             esPrimero=false;	               
	         }
	         if(!esPrimero)
	             separador="-";
	        }
	        if(hayFacturas)
	         {
	            logger.warn("aplicacion de pago descuadrado");           
                errores.add("valores descuadrados", new ActionMessage("error.aprobacionPagosEmpresa.saldoFactura","["+facturas+"]"));
                existeError=true; 
	         }
        }
        if(!existeError)
        {	        
           return this.accionGenerarAprobacion(con, formAprob, mundoAprob, mapping, usuario, request);
        }  
        saveErrors(request, errores);
        UtilidadBD.closeConnection(con);
        return mapping.findForward("paginaErroresActionErrors"); 
    }
    /**
	 * metodo para consultar el detalle de la
	 * aplicación de pagos y los conceptos si 
	 * fueron generados
     * @param con Connection
     * @param formAprob
     * @param mundoAprob
     * @param mapping
     * @return
     */
    private ActionForward accionGenerarConsultaDetallePagos(Connection con, 
            AprobacionPagosCarteraForm formAprob, 
            AprobacionPagosCartera mundoAprob, 
            ActionMapping mapping,UsuarioBasico usuario)  
    {
        HashMap mapa=new HashMap();
        mundoAprob.setNumeroAplicacionPago(formAprob.getCodAplicacionPago()+"");
        mapa=mundoAprob.generarConsultaDetalleAplicacionPagos(con);
        formAprob.setMapaApliPagos("detalle", mapa);
        mapa=new HashMap();
        mundoAprob.setInstitucion(usuario.getCodigoInstitucionInt());
        mapa=mundoAprob.generarConsultaConceptosAplicacionPagos(con,formAprob.getCodAplicacionPago());
        formAprob.setMapaApliPagos("conceptos", mapa);
        UtilidadBD.closeConnection(con);
        return mapping.findForward("paginaDetalle");    
    }
    /**
	 * Metodo para generar la consulta de aplicación
	 * de pagos empresa, segun los filtros seleccionados
     * @param con Connection
     * @param formAprob Form
     * @param mundoAprob Mundo
     * @param mapping 
     * @return
     */
    private ActionForward accionGenerarConsultaAplicacionPagos(Connection con, 
																	            AprobacionPagosCarteraForm formAprob, 
																	            AprobacionPagosCartera mundoAprob, 
																	            ActionMapping mapping) 
    {
        mundoAprob.setNumeroAplicacionPago(formAprob.getNumeroAplicacionPago());
        mundoAprob.setDocumento(formAprob.getDocumento());
        mundoAprob.setTipo(formAprob.getTipo()+"");
        mundoAprob.setCodConvenio(formAprob.getCodConvenio()+"");
        mundoAprob.setEstadoPago(formAprob.getEstadoPago()+"");
        if(formAprob.getTipoFuncionalidad().equals("aprobacion"))
            formAprob.setMapaApliPagos(mundoAprob.generarConsultaAplicacionPagos(con,true));
        else
            formAprob.setMapaApliPagos(mundoAprob.generarConsultaAplicacionPagos(con,false));
        UtilidadBD.closeConnection(con);
        return mapping.findForward("paginaListado");          
    }
    /**
	 * 
	 * @param session
	 * @return
	 */
	private UsuarioBasico getUsuarioBasicoSesion(HttpSession session)
	{
	    UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
			if(usuario == null)
			    logger.warn("El usuario no esta cargado (null)");
			
			return usuario;
	}
}
