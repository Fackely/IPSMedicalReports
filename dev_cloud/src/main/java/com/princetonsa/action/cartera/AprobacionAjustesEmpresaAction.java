
/*
 * Creado   23/08/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.action.cartera;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.BloqueosConcurrencia;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.cartera.AprobacionAjustesEmpresaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cartera.AprobacionAjustesEmpresa;
import com.princetonsa.pdf.AjustesPdf;

/**
 * Clase para manejar el workflow de  
 * aprobacion ajustes empresa
 *
 * @version 1.0, 23/08/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class AprobacionAjustesEmpresaAction extends Action 
{
    /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(AprobacionAjustesEmpresaAction.class);
	
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
	    if(form instanceof AprobacionAjustesEmpresaForm)
	    {	        
	    
		    	    
		    //intentamos abrir una conexion con la fuente de datos 
			con = openDBConnection(con);
			if(con == null)
			{
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				return mapping.findForward("paginaError");
			}
			
			AprobacionAjustesEmpresaForm aprobForm = (AprobacionAjustesEmpresaForm) form;
			AprobacionAjustesEmpresa mundoAprob=new AprobacionAjustesEmpresa();
			HttpSession sesion = request.getSession();			
			UsuarioBasico usuario = null;
			usuario = getUsuarioBasicoSesion(sesion);
			String estado=aprobForm.getEstado();
			logger.warn("estado->"+estado);
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo de AprobacionAjustesEmpresaAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				this.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}	
			else if(estado.equals("empezarAprobacion") )
			{			    
				 aprobForm.reset();
				 aprobForm.resetMaps();
				 mundoAprob.reset();
				 aprobForm.setAccion("empezarAprobacion");
				 this.cerrarConexion(con);
				 return mapping.findForward("paginaPrincipal");
			}
			else if(estado.equals("generarConsulta") )
			{			    
				 return this.realizarConsultaAjuste(con,aprobForm,mundoAprob,mapping,usuario);
			}
			else if(estado.equals("seleccionarRegistro") )
			{			    
				 return this.seleccionarRegistro(con,aprobForm,mapping,mundoAprob,usuario);
			}
			else if(estado.equals("generarDetalle") )
			{			    
				 return this.realizarConsultaDetalle(con,mapping);
			}
			else if(estado.equals("volver") )
			{	
			    this.cerrarConexion(con);
				return mapping.findForward("paginaPrincipal");
			}
			else if(estado.equals("generarAprobacion"))
			{
			  return this.validacionesAprobacion(con,aprobForm,mapping,mundoAprob,usuario,request);			    
			}	
			else if(estado.equals("imprimir"))
			{
				return this.imprimirDetalleAjuste(con,aprobForm,mapping,request,usuario);   
			}
			else if(estado.equals("cancelarAprobacion"))
			{
			    logger.warn("Proceso Cancelado !");
				request.setAttribute("codigoDescripcionError", "errors.procesoCancelado");
				this.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
	    }
	    else
		{
			logger.error("El form no es compatible con el form de AprobacionAjustesCarteraForm");
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
	
	
	private ActionForward imprimirDetalleAjuste(Connection con, AprobacionAjustesEmpresaForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario)
	{
		String nombreArchivo;
		Random r= new Random();
		nombreArchivo= "/aBorrar" + r.nextInt() + ".pdf";
		AjustesPdf.imprimirDetalleAjusteConsulta(con,ValoresPorDefecto.getFilePath()+ nombreArchivo, forma.getMapAjustes().get("codigo_ajuste_"+forma.getRegSeleccionado())+"", usuario, request);
		UtilidadBD.closeConnection(con);
		request.setAttribute("nombreArchivo", nombreArchivo);
		request.setAttribute("nombreVentana", "Aprobacion Ajustes");
		return mapping.findForward("abrirPdf");
	}
	
	/**
	 * Metodo para realizar las validaciones de
	 * la aprobación de ajustes.
	 * @param con
	 * @param aprobForm
	 * @param mapping
	 * @param mundoAprob
	 * @param usuario
	 * @return findForward ActionForward
	 */
	private ActionForward validacionesAprobacion(Connection con, 
													            AprobacionAjustesEmpresaForm aprobForm, 
													            ActionMapping mapping,
													            AprobacionAjustesEmpresa mundoAprob,
													            UsuarioBasico usuario,
													            HttpServletRequest request)
	{	    
        ActionErrors errores = new ActionErrors();
	    String[] dato=aprobForm.getFechaAprobacion().split("/");
	    boolean hayError=false;	    
	    aprobForm.setCodAjusteSeleccionado(Double.parseDouble(aprobForm.getMapAjustes("codigo_ajuste_"+aprobForm.getRegSeleccionado())+""));
        mundoAprob.setInstitucion(usuario.getCodigoInstitucionInt());
	    //validacion del cierre mensual
	    if(UtilidadValidacion.existeCierreMensual(Integer.parseInt(dato[1]),Integer.parseInt(dato[2]),usuario.getCodigoInstitucionInt()))
		{  
	        logger.warn("Ya se genero el cierre mensual");		        
            errores.add("cierre generado", new ActionMessage("error.cierre.yaTieneCierreMensual"));	
            hayError=true;
		}
	    //validacion del cierre de saldos iniciales
	    if(UtilidadValidacion.existeCierreSaldosIniciales(usuario.getCodigoInstitucionInt()))
	    {
	        Date fecha1 = null,fecha2=null;	
	        String fTemp="";
	        final SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/yyyy");
	        try 
	        {
                fecha1=dateFormatter.parse(dato[1]+"/"+dato[2]);
                fTemp=mundoAprob.fechaCierreSaldoInicial(con);
    	        fecha2=dateFormatter.parse(fTemp);
            } catch (ParseException e) 
            {                
                e.printStackTrace();
            }	        
	        if(fecha1.compareTo(fecha2)<=0)
	        {
	            logger.warn("La fecha de aprobacion es menor a la fecha del cierre de saldos iniciales");			        
                errores.add("cierre generado", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia",aprobForm.getFechaAprobacion(),fTemp+" de Cierre de Saldo Inicial"));
                hayError=true;
	        }
	    }
	    //validacion del estado generado del ajuste
	    dato=UtilidadValidacion.obtenerEstadoAjusteCartera(con,aprobForm.getCodAjusteSeleccionado(),usuario.getCodigoInstitucionInt()).split("@");            
        if(dato.length!=0)
        if(Integer.parseInt(dato[0]) != ConstantesBD.codigoEstadoCarteraGenerado)	   
        {
            logger.warn("El ajuste no se encuentra en estado Generado");        
            errores.add("cierre generado", new ActionMessage("error.cartera.ajustes.ajusteNoSeEncuentraGenerado",aprobForm.getMapAjustes("consecutivo_ajuste_"+aprobForm.getRegSeleccionado()+"")));
            hayError=true;
        }
        //validacion de la existencia de facturas para el ajuste
        logger.info("CODIGO AJUSTE --->"+aprobForm.getCodAjusteSeleccionado());
        if(!UtilidadValidacion.esAjusteDistribuidoCompletamete(con,aprobForm.getCodAjusteSeleccionado()))
        {
        	logger.warn("AJUSTE NO DISTRIBUIDO COMPLETAMENTE.");			        
            errores.add("AJUSTE INCOMLETO", new ActionMessage("error.cartera.ajustes.ajusteNoDistribuidoCompletamente",aprobForm.getMapAjustes("consecutivo_ajuste_"+aprobForm.getRegSeleccionado()+"")));
            hayError=true;
        }
        else
        {
        	//ESTA VALIDACION CREO QUE NO SIRVE PARA NADA, NO LA QUITO POR SI ALGO. 
	        if(!mundoAprob.existeInfoAjustesXFacturas(con,aprobForm.getCodAjusteSeleccionado(),usuario.getCodigoInstitucionInt()))
	        {
	            logger.warn("no existe informacion de facturas asociadas al ajuste");			        
	            errores.add("cierre generado", new ActionMessage("error.cartera.ajustes.noExistenFacturasParaElAjuste",aprobForm.getMapAjustes("consecutivo_ajuste_"+aprobForm.getRegSeleccionado()+"")));
	            hayError=true;
	        }
	        else
	        {
		        //validacion de las facturas asociadas al ajuste se encuentren en estado facturadas
	            HashMap facturas=new HashMap();
			    facturas=(HashMap)aprobForm.getMapAjustes("facturas_"+aprobForm.getRegSeleccionado());
		        
		        for(int k=0;k<Integer.parseInt(facturas.get("numRegFact")+"");k++)
		        {
			        if(!mundoAprob.existefacturaEnEstadoFacturada(con,Integer.parseInt(facturas.get("factura_"+k)+""),usuario.getCodigoInstitucionInt()))
			        {
			            logger.warn("la factura no se encuentra en estado facturada");			        
			            errores.add("cierre generado", new ActionMessage("error.cartera.ajustes.facturasSinEstadoFacturada",aprobForm.getMapAjustes("consecutivo_ajuste_"+aprobForm.getRegSeleccionado()+"")));						
						k=Integer.parseInt(facturas.get("numRegFact")+"");//para terminar el ciclo en caso de que una factura no se encuentre en estado facturada
						hayError=true;
			        }		        
		        }      
		       	//validación de la existencia de información de las facturas a nivel de los servicios       
	            for(int k=0;k<Integer.parseInt(facturas.get("numRegFact")+"");k++)
		        {	                
	                if((facturas.get("tipo_factura_sistema_"+k)+"").equals("true"))
			        {	                    
	                    if(!mundoAprob.existeAjusteDeLaFacturaANivelDeServicios(con,Integer.parseInt(facturas.get("factura_"+k)+""),aprobForm.getCodAjusteSeleccionado(),usuario.getCodigoInstitucionInt()))
			            {
			                logger.warn("no existe informacion de facturas asociadas al ajuste a nivel de los servicios");			        
				            errores.add("cierre generado", new ActionMessage("error.cartera.ajustes.noExisteInfoDeLaFacturaServicios",aprobForm.getMapAjustes("consecutivo_ajuste_"+aprobForm.getRegSeleccionado()+"")));						
							k=Integer.parseInt(facturas.get("numRegFact")+"");//para terminar el ciclo en caso de que una factura no se encuentre a nivel de los servicios
							hayError=true;
			            }
			        }
		        }
	            //validación de facturas que tengan definidos ajustes a solicitudes de cirugías, verificando información
	            // del ajuste a nivel de los asocios
	            for(int k=0;k<Integer.parseInt(facturas.get("numRegFact")+"");k++)
	            {                   
	                if((facturas.get("tipo_factura_sistema_"+k)+"").equals("true"))
	                {                       
	                    if(!mundoAprob.ajusteFacturaASolicitudDeCxExisteAsocio(con,aprobForm.getCodAjusteSeleccionado(),Integer.parseInt(facturas.get("factura_"+k)+"")))
	                    {
	                        logger.warn("la factura tiene definido ajuste a solicitud de Cx, y no posee información a nivle de los asocios");                  
	                        errores.add("cierre generado", new ActionMessage("error.cartera.ajustes.noExisteInfoDeLaFacturaAsocios",aprobForm.getMapAjustes("consecutivo_ajuste_"+aprobForm.getRegSeleccionado()+"")));                     
	                        k=Integer.parseInt(facturas.get("numRegFact")+"");//para terminar el ciclo en caso de que una factura no se encuentre a nivel de los servicios
	                        hayError=true;
	                    }
	                }
	            }     
	        }
        }
        saveErrors(request, errores);
        if(!hayError)
        {
            aprobForm.setAccion("confirmar");
            return this.generarAprobacion(con,aprobForm,mapping,mundoAprob,usuario,request);            
        }
        else
        {
            aprobForm.setAccion("erroresPresentes");
		    this.cerrarConexion(con);
			return mapping.findForward("paginaPrincipal");		
        }
	}
	
	/**
	 * metodo para realizar el proceso
	 * de guardar la informacion en la 
	 * BD.
	 * @param con
	 * @param aprobForm
	 * @param mapping
	 * @param mundoAprob
	 * @return
	 */
	private ActionForward generarAprobacion(Connection con,
										        			AprobacionAjustesEmpresaForm aprobForm, 
												            ActionMapping mapping,
												            AprobacionAjustesEmpresa mundoAprob,
									 			            UsuarioBasico usuario,
												            HttpServletRequest request)
	{
	    mundoAprob.setMapaAprobacion(aprobForm.getMapAjustes());//se copia todo el mapa al mundo, para genrar la aprobacion por medio de los datos que este contiene
	    mundoAprob.setRegSeleccionado(aprobForm.getRegSeleccionado());//se copia al mundo el numero del registro seleccionado, para aprobar el ajuste
	    mundoAprob.setInstitucion(usuario.getCodigoInstitucionInt());
	    mundoAprob.consultarDetalleFacturas(con);
        mundoAprob.consultarDetalleFacturasAsocio(con);
	    mundoAprob.setFechaAprobacion(UtilidadFecha.conversionFormatoFechaABD(aprobForm.getFechaAprobacion()));
	    mundoAprob.setUsuario(usuario.getLoginUsuario());
	   
	    ArrayList filtro = new ArrayList();
	 	filtro.add((int)aprobForm.getNumAjuste());
		UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueAjusteGeneralDeterminado,filtro);
		UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueAjusteFacturaDeterminado,filtro);
		UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueAjusteDetFacturaDeterminado,filtro);
		UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloqueAjusteAsocioDetFacturaDeterminado,filtro);
		logger.info("NUMERO AJUSTE--->"+aprobForm.getNumAjuste());
		String[] estadoAjuste=UtilidadValidacion.obtenerEstadoAjusteCartera(con,aprobForm.getNumAjuste(),usuario.getCodigoInstitucionInt()).split("@");
		if(Integer.parseInt(estadoAjuste[0])!=ConstantesBD.codigoEstadoCarteraGenerado)
		{
			ActionErrors errores=new ActionErrors();
			errores.add("AJUSTE DIFERENTE GENERADO",new ActionMessage("error.cartera.ajustes.ajusteNoSeEncuentraGenerado"));
			saveErrors(request, errores);
	    	UtilidadBD.abortarTransaccion(con);
	    	UtilidadBD.closeConnection(con);	   	        
	    	return mapping.findForward("paginaErroresActionErrors");  
		}
	    
	    boolean endTransaction=mundoAprob.generarAprobacionTrans(con,aprobForm.isAplicarAjusteCuentaPagarMedico());
	    logger.info(" ajuste aprobado : "+endTransaction);
	    if(!endTransaction)
	    {
	       // ActionErrors errores = new ActionErrors(); 
	        logger.warn("No se guardo informacion, error en la BD.");        
            /*errores.add("cierre generado", new ActionMessage("errors.sinActualizar"));
            saveErrors(request, errores);*/
            return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error al reversar el ajuste, CAUSA: El Ajuste ya fué reversado",  "Error al reversar el ajuste, CAUSA: El Ajuste ya fué reversado", false);
	    }
	    this.cerrarConexion(con);
		return mapping.findForward("paginaResumen");
	}
	
	/**
	 * Metodo para realizar la consulta del 
	 * detalle de las facturas que pertenecen
	 * a un ajuste.
     * @param con Connection     
     * @param mapping ActionMapping
     * @return
     */
    private ActionForward realizarConsultaDetalle(Connection con, 													           
													            ActionMapping mapping) 
    {          
        this.cerrarConexion(con);
		return mapping.findForward("paginaDetalle");       
    }

    /**
	 * Metodo para seleccionar un registro 
     * @param aprobForm AprobacionAjustesEmpresaForm
     * @param mapping ActionMapping
     * @return findForward ActionForward
     */
    private ActionForward seleccionarRegistro(Connection con,
            									AprobacionAjustesEmpresaForm aprobForm, 
            									ActionMapping mapping,
            									AprobacionAjustesEmpresa mundoAprob,
            									UsuarioBasico usuario) 
    {
        aprobForm.setMapAjustes("mostrarPopOut","false");
        aprobForm.setMapAjustes("selected_"+aprobForm.getRegSeleccionado(),"true");        
        this.generarDetalleAjuste(con,aprobForm,mundoAprob,usuario);//se genera el detalle de facturas asociadas a un ajuste, en caso de que el usuario genere la aprobacion sin antes haber visto el detalle, y que no se halla generado el mismo
        this.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");       
    }

    /**
     * Metodo para generar el detalle de facturas
     * cuando el ajuste es por CxC
     * @param con
     * @param aprobForm
     * @param mundoAprob
     * @param usuario
     */
    private void generarDetalleAjuste(Connection con,
									            AprobacionAjustesEmpresaForm aprobForm,
									            AprobacionAjustesEmpresa mundoAprob,
									            UsuarioBasico usuario)
    {
        //realizar la consulta del detalle de facturas en caso de ser por CxC
        mundoAprob.setMapaAprobacion(aprobForm.getMapAjustes());//se pasan los datos de la busqueda anterior al mapa del mundo, para adicionar el detalle        
        mundoAprob.setRegSeleccionado(aprobForm.getRegSeleccionado());
        aprobForm.setCodAjusteSeleccionado(Double.parseDouble(aprobForm.getMapAjustes("codigo_ajuste_"+aprobForm.getRegSeleccionado())+""));
        mundoAprob.setInstitucion(usuario.getCodigoInstitucionInt());
        mundoAprob.realizarConsulta2(con,aprobForm.getCodAjusteSeleccionado());
        aprobForm.resetMaps();//se limpian los mapas del form para no duplicar informacion
        aprobForm.setMapAjustes(mundoAprob.getMapaAprobacion());
    }   
    /**
	 * Metodo para realizar la consulta avanzada
	 * de aprobación de ajustes.
	 * @param con Connection
     * @param aprobForm AprobacionAjustesEmpresaForm
     * @param mundoAprob AprobacionAjustesEmpresa
     * @param mapping ActionMapping
     * @return 
     */
    private ActionForward realizarConsultaAjuste(Connection con,
										            AprobacionAjustesEmpresaForm aprobForm, 
										            AprobacionAjustesEmpresa mundoAprob, 
										            ActionMapping mapping,
										            UsuarioBasico usuario) 
    {          
        boolean esBusquedaXAjuste=false,yaSeGeneroMensaje=false;        
        String[] dato={};
        String temp="";
        String acronimoAjuste="";
        if(!aprobForm.getConsecutivoAjuste().equals(""))//porque en la forma se maneja el consecutivo del ajuste como String, y por debajo como double para buscar por codigo
        {
            if(aprobForm.getCodTipoAjuste()==ConstantesBD.codigoConceptosCarteraCredito)              
                acronimoAjuste=ConstantesBD.ajustesCreditoFuncionalidadAjustes.getAcronimo();
            if(aprobForm.getCodTipoAjuste()==ConstantesBD.codigoConceptosCarteraDebito)
                acronimoAjuste=ConstantesBD.ajustesDebitoFuncionalidadAjustes.getAcronimo();
            aprobForm.setNumAjuste(Utilidades.obtenercodigoAjusteEmpresa(aprobForm.getConsecutivoAjuste(),acronimoAjuste,usuario.getCodigoInstitucionInt())); 
        }
        if(aprobForm.getCodTipoAjuste()!=ConstantesBD.codigoNuncaValido && !aprobForm.getConsecutivoAjuste().equals("")
           && aprobForm.getFechaAjuste().equals("") && aprobForm.getNumCuentaCobro()==ConstantesBD.codigoNuncaValidoDouble 
           && aprobForm.getNumFactura()==ConstantesBD.codigoNuncaValidoDouble && aprobForm.getCodConvenio()==ConstantesBD.codigoNuncaValido)
        {
            esBusquedaXAjuste=true;                                    
            temp=UtilidadValidacion.obtenerEstadoAjusteCartera(con,aprobForm.getNumAjuste(),usuario.getCodigoInstitucionInt());            
            if(!temp.equals(""))
                dato=temp.split("@");
            {
	            if(dato.length!=0)
		            if(Integer.parseInt(dato[0]) != ConstantesBD.codigoEstadoCarteraGenerado)	   
		            {
		                aprobForm.setMensajeBusqueda("AJUSTE "+aprobForm.getNumAjuste()+" EN ESTADO "+dato[1]+". NO SE PERMITE SU APROBACION");
		                yaSeGeneroMensaje=true;
		            }
            }
        }        
        this.copiarDatosAlMundo(aprobForm,mundoAprob,usuario);
        mundoAprob.construirConsulta1();
        mundoAprob.realizarConsulta1(con,true);
        mundoAprob.verificarValorPoolAjustesFactura(con);
        aprobForm.setMapAjustes(mundoAprob.getMapaAprobacion());        
        if(mundoAprob.getRegSeleccionado()!=ConstantesBD.codigoNuncaValido)
            aprobForm.setRegSeleccionado(mundoAprob.getRegSeleccionado());//se toma el codigo del ajuste que se carga, para realizar las validaciones, ya que no se elejira uno de una lista, entonces se coloca el que se carga por defecto
        if(!aprobForm.getMapAjustes().isEmpty())
        {
            if(Integer.parseInt(aprobForm.getMapAjustes("numReg")+"")==1)
            {  
                generarDetalleAjuste(con,aprobForm,mundoAprob,usuario);//se genera el detalle de facturas asociadas a un ajuste
            }      
            else
            {
            	aprobForm.setMapAjustes("mostrarPopOut","true");
            }
            aprobForm.setUsuarioAprueba(usuario.getLoginUsuario());
            aprobForm.setFechaAprobacion(UtilidadFecha.getFechaActual());            
        }
        else if(aprobForm.getMapAjustes().isEmpty() && esBusquedaXAjuste && !yaSeGeneroMensaje)
        {
            aprobForm.setMensajeBusqueda("NO SE ENCONTRO EL AJUSTE  "+aprobForm.getConsecutivoAjuste()+". VERIFICAR");
        }
        else if(aprobForm.getMapAjustes().isEmpty() && !esBusquedaXAjuste)
        {
            aprobForm.setMensajeBusqueda("LA BUSQUEDA NO ARROJO INFORMACION !");
        }  
        else if(aprobForm.getMapAjustes().isEmpty() && !yaSeGeneroMensaje)
        {
            aprobForm.setMensajeBusqueda("LA BUSQUEDA NO ARROJO INFORMACION !");
        }   
        
        
        this.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");
    }
    
    /**
     * Metodo para copiar los datos del form al 
     * mundo
     * @param aprobForm AprobacionAjustesEmpresaForm
     * @param mundoAprob AprobacionAjustesEmpresa
     */ 
    private void copiarDatosAlMundo(AprobacionAjustesEmpresaForm aprobForm,AprobacionAjustesEmpresa mundoAprob,UsuarioBasico usuario)
    {
       mundoAprob.setCodTipoAjuste(aprobForm.getCodTipoAjuste());
       mundoAprob.setNumAjuste(aprobForm.getNumAjuste());
       mundoAprob.setFechaAjuste(aprobForm.getFechaAjuste());
       mundoAprob.setNumCuentaCobro(aprobForm.getNumCuentaCobro());
       mundoAprob.setNumFactura(aprobForm.getNumFactura());
       mundoAprob.setCodConvenio(aprobForm.getCodConvenio());
       mundoAprob.setInstitucion(usuario.getCodigoInstitucionInt());
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
