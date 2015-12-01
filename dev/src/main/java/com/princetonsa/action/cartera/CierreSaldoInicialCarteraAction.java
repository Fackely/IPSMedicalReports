
/*
 * Creado   14/07/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.action.cartera;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.cartera.CierresCarteraForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cartera.CierreSaldoInicialCartera;


/**
 * Clase para manejar el workflow de cierre de 
 * saldos iniciales
 *
 * @version 1.0, 14/07/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class CierreSaldoInicialCarteraAction extends Action 
{

    /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(CierreSaldoInicialCarteraAction.class);
	
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
	    if(form instanceof CierresCarteraForm)
	    {	        
	    
		   
		    
		    //intentamos abrir una conexion con la fuente de datos 
			con = openDBConnection(con);
			if(con == null)
			{
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				return mapping.findForward("paginaError");
			}
			
			CierresCarteraForm cierreForm = (CierresCarteraForm) form;
			HttpSession sesion = request.getSession();
			
			UsuarioBasico usuario = null;
			usuario = getUsuarioBasicoSesion(sesion);
			
			String estado = cierreForm.getEstado();
			logger.warn("[CierreSaldoInicialCarteraAction] estado->"+estado);
			String accion = cierreForm.getAccion();
			logger.warn("[CierreSaldoInicialCarteraAction] accion->"+accion);
			    
			CierreSaldoInicialCartera mundoCierre = new CierreSaldoInicialCartera();
			
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo de ConceptosCarteraAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				this.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}		
			else if(estado.equals("cierreInicial") )
			{			    
			    cierreForm.reset();
			    cierreForm.setTipoCierre(estado);
			    cierreForm.setAccion("cierreInicial");
			    boolean existeCierre=UtilidadValidacion.existeCierreSaldosIniciales(usuario.getCodigoInstitucionInt());
			    if(existeCierre)
			    {			        
			        mundoCierre.reset();
			        mundoCierre.setInstitucion(usuario.getCodigoInstitucionInt());
		            mundoCierre.cargarResumenCierre(con);
		            copiarDatosAlForm(cierreForm,mundoCierre);
		            this.cerrarConexion(con);
		    	    return mapping.findForward("paginaResumen");			        
			    }
			    else
			    {
				    if(ValoresPorDefecto.getFechaCorteSaldoInicialC(usuario.getCodigoInstitucionInt()).trim().equals(""))
				    {
				        this.cerrarConexion(con);
				        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "no esta definida la fecha de corte inicial", "error.noDefinidaFechaCorteSaldoInicial", true);   
				    }
				    else
				    {
				        asignarFechaSaldoInicial(cierreForm,usuario);
				        String fecha = cierreForm.getYearCierre()+"-"+cierreForm.getMesCierre();
					    cierreForm.setExistenFacturasParaCierre(mundoCierre.validarExistenFacturas(con,fecha,usuario.getCodigoInstitucionInt(),true));
					    cierreForm.setContFactConCuentaCobro(mundoCierre.getContFactConCuentaCobro());
					    this.cerrarConexion(con);
					    return mapping.findForward("paginaPrincipal");
				    }
			    }
			}			
			else if (estado.equals("cierreMensual"))
			{	
			    cierreForm.reset();
			    cierreForm.setAccion("cierreMensual");
			    cierreForm.setTipoCierre(estado);		    
			    			        
		        int ultimoMesCierre = mundoCierre.ultimoCierreGenerado(con,UtilidadFecha.getMesAnioDiaActual("anio"),ConstantesBD.codigoTipoCierreMensualStr,usuario.getCodigoInstitucionInt());			        
		        if(ultimoMesCierre!=0)
		        {			        
		            cierreForm.setYearCierre(UtilidadFecha.getMesAnioDiaActual("anio")+"");
			        if(ultimoMesCierre<12)
			            ultimoMesCierre ++;
			        String ultimoMesCierreStr="0"+ultimoMesCierre;//el formato debe ser mm, por la busqueda de facturas que esta la fecha en formato(yyyy-mm-dd)
			        cierreForm.setMesCierre(ultimoMesCierreStr);			        
		        }
		        else
		        {
		            cierreForm.setYearCierre(UtilidadFecha.getMesAnioDiaActual("anio")+"");
		            int mesAnterior = UtilidadFecha.getMesAnioDiaActual("mes");
		            if(UtilidadFecha.getMesAnioDiaActual("mes") > 1)
		                mesAnterior--;
		            String mesAnteriorStr="0"+mesAnterior;//el formato debe ser mm, por la busqueda de facturas que esta la fecha en formato(yyyy-mm-dd)
		            cierreForm.setMesCierre(mesAnteriorStr);			            
		        }
		        this.cerrarConexion(con);
			    return mapping.findForward("paginaPrincipal");     
			    	    
			}
			else if(estado.equals("cierreAnual"))
			{
			    cierreForm.reset();
			    cierreForm.setAccion("cierreAnual");
			    String anioAnterior=(UtilidadFecha.getMesAnioDiaActual("anio")-1)+"";
			    cierreForm.setYearCierre(anioAnterior);
			    mundoCierre.reset();
			    this.cerrarConexion(con);
			    return mapping.findForward("paginaPrincipal");        
			}
			else if(estado.equals("generarCierre") && cierreForm.getAccion().equals("cierreAnual"))
			{
			    if(mundoCierre.existenCierres(con,Integer.parseInt(cierreForm.getYearCierre()),ConstantesBD.valorMesCierreAnualInt,ConstantesBD.codigoTipoCierreAnualStr,usuario.getCodigoInstitucionInt()))
			    {
			        logger.warn("Ya se genero el cierre anual");
			        ArrayList atributosError = new ArrayList();
		            atributosError.add(0,cierreForm.getYearCierre());
		            atributosError.add(1,"");
					request.setAttribute("codigoDescripcionError", "error.cierreAnualEfectuado");
					request.setAttribute("atributosError", atributosError);
					this.cerrarConexion(con);
					return mapping.findForward("paginaError"); 
			    }
			    else
			    {
			       return this.consultarValidarCierresMensualesAnualesInicial(con,cierreForm,mundoCierre,usuario,mapping,request);  
			    }
			}
			else if(estado.equals("generarCierre") && cierreForm.getAccion().equals("cierreInicial"))
			{
			    return this.generarCierreCartera(con,cierreForm,mapping,usuario);
			}
			else if(estado.equals("generarCierre") && cierreForm.getAccion().equals("cierreMensual"))
			{			    
			    return this.consultarFacturasParaValidarCierre(con,cierreForm,mundoCierre,usuario,mapping,request);			    
			}
			else if(estado.equals("volverGenerarCierre") && cierreForm.getAccion().equals("cierreMensual"))//generar el cierre cartera si ya fue generado previamente
			{				   
			    return this.generarCierreCartera(con,cierreForm,mapping,usuario);
			}
			else if(estado.equals("cancelarCierre"))
			{		        
                request.setAttribute("codigoDescripcionError", "error.cierreCancelado");	
                this.cerrarConexion(con);
                return mapping.findForward("paginaError");
			}
			else if(estado.equals("consultaCierre"))
			{               	
			    cierreForm.reset();
			    cierreForm.setAccion("empezarConsulta");
			    mundoCierre.reset();
			    this.cerrarConexion(con);
                return mapping.findForward("paginaPrincipal");
			}
			else if(estado.equals("consultar"))
			{		    
			 return this.realizarCnsultaCierresMensuales(con,cierreForm,mapping,mundoCierre,usuario);   
			}
			else if(estado.equals("ordenarColumna"))
			{
			    return this.accionOrdenarColumna(con,cierreForm,mapping);
			}
	    }
	    else
		{
			logger.error("El form no es compatible con el form de CierresCarteraForm");
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
	 * Metodo para ordenar el hashmap que 
	 * contiene el resultado de la consulta.
     * @param con
     * @param generacionForm
     * @param mapping
     * @param request
     * @return
     */
    private ActionForward accionOrdenarColumna(Connection con, 
            											CierresCarteraForm cierreForm, 
											            ActionMapping mapping) 
    {
        String[] indices={
				            "convenio_", 
				            "valor_cartera_", 
				            "valor_ajuste_debito_", 
				            "valor_ajuste_credito_",
				            "valor_pago_",
				            "anio_cierre_",
				            "mes_cierre_",
				            "codigo_cierre_",
				            "saldo_Anterior_",
				            "saldo_Final_",
				            "nombre_empresa_",
				            "nombre_convenio_"				            				            
	            		};
        
        cierreForm.setCierres(Listado.ordenarMapa(indices,
											                cierreForm.getPatronOrdenar(),
											                cierreForm.getUltimoPatron(),
											                cierreForm.getCierres(),
											                cierreForm.getNumRegHashMap()));
        cierreForm.setUltimoPatron(cierreForm.getPatronOrdenar());
        this.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");  
    }
	
	/**
	 * Metodo para realizar la consulta de 
	 * cierres efectuados, segun los filtros elegidos
	 * por el usuario
	 * @param con
	 * @param cierreForm
	 * @param mapping
	 * @param mundoCierre
	 * @param usuario
	 * @return
	 */
	private ActionForward realizarCnsultaCierresMensuales(Connection con,
																        CierresCarteraForm cierreForm,
																        ActionMapping mapping,
																        CierreSaldoInicialCartera mundoCierre,
																        UsuarioBasico usuario)
	{	 
	    String[] dato={};
	    cierreForm.setAccion("generarConsulta");
	    if(cierreForm.getYearCierre().equals(""))
	        mundoCierre.setYear(-1+"");
	    else	        
	        mundoCierre.setYear(cierreForm.getYearCierre());
	    if(cierreForm.getMesCierre().equals(""))
	        mundoCierre.setMes(-1+"");
	    else
	        mundoCierre.setMes(cierreForm.getMesCierre());
	    //if(cierreForm.getCodigoConvenioStr().length()==0)
	    {
	        if(cierreForm.getCodConvenio()!=-1)
	            cierreForm.setCodigoConvenioStr(cierreForm.getCodConvenio()+"");
	    }
	    if(cierreForm.getCodigoConvenioStr().length()!=0)
	    {
	        dato=cierreForm.getCodigoConvenioStr().split("-");	        
	    }	   
	    mundoCierre.setInstitucion(usuario.getCodigoInstitucionInt());
	    cierreForm.setCierres(mundoCierre.consultaAvanzadaCierres(con,dato));
	    //mapa temporal con los totales, mientras se arregla el ordenar hashmap
	    cierreForm.setMapaTemp(mundoCierre.getTotalAjustesCredito());
	    cierreForm.setNumRegHashMap(Integer.parseInt(cierreForm.getCierres("numReg")+""));
	    this.cerrarConexion(con);
        return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * 
	 * @param con Connection, conexión con la fuente de datos.
	 * @param cierreForm CierresCarteraForm
	 * @param mundoCierre CierreSaldoInicialCartera
	 * @param usuario UsuarioBasico
	 * @param mapping ActionMapping
	 * @param request HttpServletRequest
	 * @return findForward ActionForward
	 */
	private ActionForward consultarValidarCierresMensualesAnualesInicial(Connection con,
						        														CierresCarteraForm cierreForm,
						        														CierreSaldoInicialCartera mundoCierre,
						        														UsuarioBasico usuario,
						        														ActionMapping mapping,
						        														HttpServletRequest request)
	{
	    boolean existeTodosMesesCierre=true;
	    int mes=1;	           
        mundoCierre.setInstitucion(usuario.getCodigoInstitucionInt());
        mundoCierre.setYear(cierreForm.getYearCierre());
        mundoCierre.setTipoCierre(ConstantesBD.codigoTipoCierreMensualStr);
        cierreForm.setCierresMensualesGenerados(mundoCierre.cierresMensualesGenerados(con)); 
        String mesesSinCierre="";
        int mesCierreInicial=ConstantesBD.codigoNuncaValido;
        String codTipoCierre=ConstantesBD.codigoTipoCierreAnualStr;
        copiarDatosAlMundo(cierreForm,usuario,mundoCierre,codTipoCierre);
        while(mes<=12)  
        {
            if(!cierreForm.getCierresMensualesGenerados().contains(mes+""))  
            {
                mesesSinCierre+="-"+mes+"";
                existeTodosMesesCierre=false;
            }
            mes ++;
        }
        if(!existeTodosMesesCierre)
        {     
		    if(!UtilidadValidacion.existeCierreSaldosIniciales(usuario.getCodigoInstitucionInt()))
		    {
				request.setAttribute("codigoDescripcionError", "error.cierre.faltanCierres");				
				this.cerrarConexion(con);
				return mapping.findForward("paginaError");   
		    }
		    else
		    {
		        mesCierreInicial = mundoCierre.ultimoCierreGenerado(con,Integer.parseInt(cierreForm.getYearCierre()),ConstantesBD.codigoTipoCierreSaldoInicialStr,usuario.getCodigoInstitucionInt());
		        if(mesCierreInicial==12)
		        {
		            mundoCierre.generarCierreAnual(con);
		            cierreForm.reset();
		            cierreForm.setEstado("resumen");
		            mundoCierre.cargarResumenCierre(con);
		            copiarDatosAlForm(cierreForm,mundoCierre);
		            this.cerrarConexion(con);
		    	    return mapping.findForward("paginaResumen");    
		        }
		        else
		        {		            
		            mes = mesCierreInicial+1;	        
			        boolean existeMesesCierreDespuesDeSaldoInicial=true;
			        mesesSinCierre="";
			        while(mes<=12)  
			        {
			            if(!cierreForm.getCierresMensualesGenerados().contains(mes+""))	
			            {
			                existeMesesCierreDespuesDeSaldoInicial=false;			                
			                mesesSinCierre+="-"+mes+"";
			            }
			            mes ++;
			        }
			        if(!existeMesesCierreDespuesDeSaldoInicial)
			        {
						request.setAttribute("codigoDescripcionError", "error.cierre.faltanCierres");				
						this.cerrarConexion(con);
						return mapping.findForward("paginaError");     
			        }
			        else
			        {
			            mundoCierre.generarCierreAnual(con);
			            cierreForm.reset();
			            cierreForm.setEstado("resumen");
			            mundoCierre.cargarResumenCierre(con);
			            copiarDatosAlForm(cierreForm,mundoCierre);
			            this.cerrarConexion(con);
			    	    return mapping.findForward("paginaResumen");  
			        }
		        }
		    }
        }
        else
        {
            logger.info("Existen todos los cierres mensuales del año "+"["+cierreForm.getYearCierre()+"]"+" Continua el proceso");
            int anioInmediatamenteAnterior=Integer.parseInt(cierreForm.getYearCierre())-1;
            if(!mundoCierre.existenCierres(con,anioInmediatamenteAnterior,ConstantesBD.valorMesCierreAnualInt,ConstantesBD.codigoTipoCierreAnualStr,usuario.getCodigoInstitucionInt()))
            {                 
                mesCierreInicial = mundoCierre.ultimoCierreGenerado(con,Integer.parseInt(cierreForm.getYearCierre()),ConstantesBD.codigoTipoCierreSaldoInicialStr,usuario.getCodigoInstitucionInt());
                if( mesCierreInicial!= 0 )
                {                    
	                if(mesCierreInicial!=1)
	                {                    
	                    logger.warn("Falta el Cierre Anual del Año Anterior. Proceso Cancelado");
		                ArrayList atributosError = new ArrayList();
			            atributosError.add(0,anioInmediatamenteAnterior+"");
						request.setAttribute("codigoDescripcionError", "error.cierre.faltaCierreAnualAnioAnterior");		
						request.setAttribute("atributosError", atributosError);
						this.cerrarConexion(con);
						return mapping.findForward("paginaError");
	                }
	                else
	                {
	                    mundoCierre.generarCierreAnual(con);
	                    cierreForm.reset();
			            cierreForm.setEstado("resumen");
			            mundoCierre.cargarResumenCierre(con);
			            copiarDatosAlForm(cierreForm,mundoCierre);
			            this.cerrarConexion(con);
			    	    return mapping.findForward("paginaResumen"); 
	                }
                }
                else
                {
                    logger.warn("Falta el Cierre Anual del Año Anterior. Proceso Cancelado");
                    ArrayList atributosError = new ArrayList();
		            atributosError.add(0,anioInmediatamenteAnterior+"");
					request.setAttribute("codigoDescripcionError", "error.cierre.faltaCierreAnualAnioAnterior");		
					request.setAttribute("atributosError", atributosError);
					this.cerrarConexion(con);
					return mapping.findForward("paginaError"); 
                }
            }
            else
            {
                mundoCierre.generarCierreAnual(con);
                cierreForm.reset();
	            cierreForm.setEstado("resumen");
	            mundoCierre.cargarResumenCierre(con);
	            copiarDatosAlForm(cierreForm,mundoCierre);
	            this.cerrarConexion(con);
	    	    return mapping.findForward("paginaResumen"); 
            }
        } 
	}
	
	/**	
	 * Metodo para validar si el mes al cual
	 * se le va a generar el cierre mensual, ya se tiene 
	 * registro de cierre de saldo inicial para ese
	 * mes.
	 * @param con Connection, conexión con la fuente de datos
	 * @param cierreForm CierresCarteraForm
	 * @param mundoCierre CierreSaldoInicialCartera
	 * @param usuario UsuarioBasico
	 */
	private ActionForward consultarFacturasParaValidarCierre (Connection con,
															        CierresCarteraForm cierreForm,
															        CierreSaldoInicialCartera mundoCierre,
															        UsuarioBasico usuario,
															        ActionMapping mapping,
															        HttpServletRequest request)
	{
		//validacion que se puso por la tarea 7614 del xplanner 3 
		if(mundoCierre.existenCierres(con,Integer.parseInt(cierreForm.getYearCierre()),ConstantesBD.valorMesCierreAnualInt,ConstantesBD.codigoTipoCierreAnualStr,usuario.getCodigoInstitucionInt()))
	    {
			logger.warn("Ya se genero el cierre anual");
	        ArrayList atributosError = new ArrayList();
            atributosError.add(0,cierreForm.getYearCierre());
            atributosError.add(1,"NO SE PUEDE PROCESAR MES "+cierreForm.getMesCierre()+" AÑO "+cierreForm.getYearCierre()+".");
			request.setAttribute("codigoDescripcionError", "error.cierreAnualEfectuado");
			request.setAttribute("atributosError", atributosError);
			this.cerrarConexion(con);
			return mapping.findForward("paginaError"); 
	    }
	    else
	    {
	        if(UtilidadValidacion.existeCierreSaldosIniciales(usuario.getCodigoInstitucionInt()))
		    {
	            //int mesCierreInicial = mundoCierre.ultimoCierreGenerado(con,UtilidadFecha.getMesAnioDiaActual("anio"),ConstantesBD.codigoTipoCierreSaldoInicialStr,usuario.getCodigoInstitucionInt());
	            String fechaCierreInicial=mundoCierre.obtenerFechaCierreSaldoIncialCartera(con,usuario.getCodigoInstitucionInt());
	            if(UtilidadFecha.compararFechas("31/"+fechaCierreInicial, "00:00", "31/"+cierreForm.getMesCierre()+"/"+cierreForm.getYearCierre(), "00:00").isTrue())
	            {
	                ArrayList atributosError = new ArrayList();
	                String[] fechaVector=fechaCierreInicial.split("/");
	                atributosError.add(0,fechaVector[0]);
	                atributosError.add(1,fechaVector[1]);
					request.setAttribute("codigoDescripcionError", "error.cierre.cierreInicialYaGeneradoParaElMes");	
					request.setAttribute("atributosError", atributosError);
					this.cerrarConexion(con);
					return mapping.findForward("paginaError");      
	            }
		    }
            return this.consultarFacturasParaValidarCierre2(con,cierreForm,mundoCierre,usuario,mapping,request);
	    }
	}
	
	/**
	 * Metodo para validar si existe cierre anual y 
	 * cancelar el proceso.
	 * Valida si existen facturas para generar el cierre, 
	 * si no existen, se cancela el proceso. 
	 * Verifica si existe el cierre para el mes a generar,
	 * si existe se reproceso el cierre.
	 * @param con Connection, conexión con la fuente de datos
	 * @param cierreForm CierresCarteraForm
	 * @param mundoCierre CierreSaldoInicialCartera
	 * @param usuario UsuarioBasico
	 */
	private ActionForward consultarFacturasParaValidarCierre2 (Connection con,
																		        CierresCarteraForm cierreForm,
																		        CierreSaldoInicialCartera mundoCierre,
																		        UsuarioBasico usuario,
																		        ActionMapping mapping,
																		        HttpServletRequest request)
	{
		
        if(mundoCierre.existenCierres(con,Integer.parseInt(cierreForm.getYearCierre()),Integer.parseInt(cierreForm.getMesCierre()),ConstantesBD.codigoTipoCierreMensualStr,usuario.getCodigoInstitucionInt()))
        {				            
         cierreForm.setMesCierreYaGenerado(true);  
         this.cerrarConexion(con);
         return mapping.findForward("paginaPrincipal");				         
        }
        else
        {
		    String fecha = cierreForm.getYearCierre()+"-"+cierreForm.getMesCierre();
	        cierreForm.setExistenFacturasParaCierre(mundoCierre.validarExistenFacturas(con,fecha,usuario.getCodigoInstitucionInt(),false));        
	
	        if(!cierreForm.isExistenFacturasParaCierre())
	        {           
	            logger.warn("NO EXISTEN FACTURAS PARA GENERAR EL CIERRE MENSUAL. PROCESO CANCELADO");
	            ArrayList atributosError = new ArrayList();
	            atributosError.add(0,cierreForm.getYearCierre());
	            atributosError.add(1,cierreForm.getMesCierre());
	            request.setAttribute("codigoDescripcionError", "error.cierre.noExistenFacturas");
	            request.setAttribute("atributosError", atributosError);
				this.cerrarConexion(con);
				return mapping.findForward("paginaError");
	        }
	        else
	        {	 
	        	return this.generarCierreCartera(con,cierreForm,mapping,usuario);
	        }
        }
	}
	
		
	/**
	 * Metodo para tomar de valores por defecto
	 * la fecha de corte de saldo inicial, y asignarla
	 * automaticamente a la forma.
	 * @param cierreForm CierresCarteraForm
	 * @param usuario UsuarioBasico
	 */
	private void asignarFechaSaldoInicial(CierresCarteraForm cierreForm,UsuarioBasico usuario)
	{
	    String fechaCorteSaldoInicial=ValoresPorDefecto.getFechaCorteSaldoInicialC(usuario.getCodigoInstitucionInt()); 
	    int first=fechaCorteSaldoInicial.indexOf("/");
	    String mes = fechaCorteSaldoInicial.substring(0,first);
	    String year = fechaCorteSaldoInicial.substring(first+1);
	    cierreForm.setMesCierre(mes);
	    cierreForm.setYearCierre(year);
	}
	
	/**
     * @param con Connection
     * @param cierreForm CierresCarteraForm
     * @param mapping ActionMapping     
     * @param UsuarioBasico usuario
     * @return findForward ActionForward
     */
    private ActionForward generarCierreCartera(Connection con, 
													            CierresCarteraForm cierreForm, 
													            ActionMapping mapping,													            
													            UsuarioBasico usuario) 
    {
        CierreSaldoInicialCartera mundoCierre = new CierreSaldoInicialCartera();
        mundoCierre.reset();
        String codTipoCierre="";
        
        if(cierreForm.getTipoCierre().equals("cierreInicial"))
            codTipoCierre=ConstantesBD.codigoTipoCierreSaldoInicialStr;
        else if(cierreForm.getTipoCierre().equals("cierreMensual"))
            codTipoCierre=ConstantesBD.codigoTipoCierreMensualStr;
        
        this.copiarDatosAlMundo(cierreForm,usuario,mundoCierre,codTipoCierre);               
        boolean esCancelarCierre=mundoCierre.generarTotalesCierre(con,usuario.getCodigoInstitucionInt(),cierreForm.getTipoCierre());
        if(esCancelarCierre)
        {
            cierreForm.reset();
            cierreForm.setCancelarCierre(esCancelarCierre);
            cierreForm.getFactConPagosPendientesOAjustes().addAll(mundoCierre.getFactConAjustesPendientes());
            cierreForm.getFactConPagosPendientesOAjustes().addAll(mundoCierre.getFactConPagosPendientes());                        
            this.cerrarConexion(con);
    	    return mapping.findForward("paginaPrincipal");
        }
        else
        {
            cierreForm.reset();            
            mundoCierre.cargarResumenCierre(con);
            this.copiarDatosAlForm(cierreForm,mundoCierre);
            if(cierreForm.getEstado().equals("volverGenerarCierre"))
                this.generarLog(cierreForm);
            cierreForm.setEstado("resumen");
            this.cerrarConexion(con);
    	    return mapping.findForward("paginaResumen");
        }
        
    }
    
    /**
     * Metodo para generar el log, cuando es reprocesado
     * un mes de cierre.
     * @param cierreForm CierresCarteraForm
     */
    private void generarLog (CierresCarteraForm cierreForm)
    {
        String log="";
        log= 
				"\n            ====CIERRE MENSUAL REPROCESADO===== " +
				"\n*  Usuario [" +cierreForm.getUsuario()+"] "+
				"\n*  Fecha ["+cierreForm.getFechaGeneracion()+"] " +
				"\n*  Hora    ["+cierreForm.getHoraGeneracion()+"] "+
				"\n*  Mes Reprocesado ["+UtilidadFecha.obtenerNombreMes(Integer.parseInt(cierreForm.getMesCierre()))+"] "+							
				"\n========================================================\n\n\n " ;       
        LogsAxioma.enviarLog(ConstantesBD.logCierreCarteraCodigo,log,ConstantesBD.tipoRegistroLogModificacion,cierreForm.getUsuario());
       
    }
    
    /**
     * metodo para pasar los datos del form al mundo
     * @param cierreForm CierresCarteraForm
     * @param usuario UsuarioBasico
     * @param mundoCierre CierreSaldoInicialCartera
     */
    private void copiarDatosAlMundo (CierresCarteraForm cierreForm,
																	            UsuarioBasico usuario,
																	            CierreSaldoInicialCartera mundoCierre,
																	            String codTipoCierre)
    {          
        mundoCierre.setYear(cierreForm.getYearCierre());
        if(codTipoCierre.equals(ConstantesBD.codigoTipoCierreAnualStr))
            mundoCierre.setMes(ConstantesBD.valorMesCierreAnualInt+"");//como no existe mes, porque el cierre anual solo es procesado por el año, se inserta el mes actual para el cierre
        else
            mundoCierre.setMes(cierreForm.getMesCierre());
        mundoCierre.setObservaciones(cierreForm.getObservaciones());
        mundoCierre.setTipoCierre(codTipoCierre);
        mundoCierre.setInstitucion(usuario.getCodigoInstitucionInt());
        mundoCierre.setUsuario(usuario.getLoginUsuario());        
        mundoCierre.setFechaCierre(cierreForm.getYearCierre()+"-"+cierreForm.getMesCierre());  
        if(cierreForm.getEstado().equals("volverGenerarCierre"))
            mundoCierre.setAccion("recalcularCierreMensual");
    }
    
    /**
     * metodo para copiar los datos del mundo al
     * form
     * @param cierreForm CierresCarteraForm
     * @param mundoCierre CierreSaldoInicialCartera
     */
    private void copiarDatosAlForm(CierresCarteraForm cierreForm,CierreSaldoInicialCartera mundoCierre)
    {
       cierreForm.setYearCierre(mundoCierre.getYear()); 
       cierreForm.setMesCierre(mundoCierre.getMes());
       cierreForm.setObservaciones(mundoCierre.getObservaciones());
       cierreForm.setFechaGeneracion(mundoCierre.getFechaGeneracion());
       cierreForm.setHoraGeneracion(mundoCierre.getHoraGeneracion());
       cierreForm.setUsuario(mundoCierre.getUsuario());
    }
    /**
	 * 
	 * @param con
	 * @return
	 */
	public Connection openDBConnection(Connection con)
	{

		if(con != null)
		return con;
					
		try{
			String tipoBD = System.getProperty("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			con = myFactory.getConnection();
			}
			catch(Exception e)
			{
			    logger.warn(e+"Problemas con la base de datos al abrir la conexion"+e.toString());
				return null;
			}
					
			return con;
	}
		 
	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer un forward
	 * @param con Conexión con la fuente de datos
	 */
	public void cerrarConexion (Connection con)
	{
	    try{
	        if (con!=null&&!con.isClosed())
	        {
	        	UtilidadBD.closeConnection(con);
	        }
	    }
	    catch(Exception e){
	        logger.error(e+"Error al tratar de cerrar la conexion con la fuente de datos. \n Excepcion: " +e.toString());
	    }
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
