
/*
 * Creado   23/01/2006
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.action.inventarios;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

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
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.ResultadoInteger;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.interfaces.UtilidadBDInventarios;
import util.inventarios.ConstantesBDInventarios;
import util.inventarios.UtilidadInventarios;
import util.inventarios.UtilidadValidacionInventarios;

import com.princetonsa.actionform.inventarios.DespachoTrasladoAlmacenForm;
import com.princetonsa.dto.interfaz.DtoInterfazTransaccionAxInv;
import com.princetonsa.mundo.Articulo;
import com.princetonsa.mundo.ConsecutivosDisponibles;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.AlmacenParametros;
import com.princetonsa.mundo.inventarios.DespachoTrasladoAlmacen;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * Clase que implementa los metodos para manejar el WorkFlow
 *
 * @version 1.0, 23/01/2006
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class DespachoTrasladoAlmacenAction extends Action 
{
	
    /**
     * manejador de los logs de la clase
     */
    private Logger logger=Logger.getLogger(DespachoTrasladoAlmacenAction.class);
    /**
	 * M�todo execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
													        ActionForm form, 
													        HttpServletRequest request, 
													        HttpServletResponse response) throws Exception
													        {

		Connection con = null;
		try{

			if(form instanceof DespachoTrasladoAlmacenForm)
			{
				DespachoTrasladoAlmacenForm forma=(DespachoTrasladoAlmacenForm)form;
				DespachoTrasladoAlmacen mundo=new DespachoTrasladoAlmacen(); 
				HttpSession sesion = request.getSession();			
				UsuarioBasico usuario = null;
				usuario = Utilidades.getUsuarioBasicoSesion(sesion);
				String estado=forma.getEstado();
				logger.warn("[DespachoTrasladoAlmacenAction] --> "+estado);


				con = UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}			
				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de DespachoTrasladoAlmacenAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				if(estado.equals("empezar"))
				{
					forma.reset(usuario.getCodigoInstitucionInt());
					return this.accionValidacionesIniciales(con,usuario,request,mapping,mundo,forma);			  		  
				}
				if(estado.equals("ordenarListadoSolicitudes"))
				{			  
					this.accionOrdenarMapaSolicitudes(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaTraslados");  
				}
				if(estado.equals("cargarSolicitud"))
				{			  
					this.accionCargarSolicitud(con,forma,mundo,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");  
				}
				if(estado.equals("generarDespacho"))
				{		  
					return this.accionGenerarDespacho(con,forma,mundo,mapping,request,usuario);			  
				}
				if(estado.equals("imprimirDespacho"))
				{         
					this.generarReporte(con,forma,usuario,request);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaResumen");              
				}
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}


			}
			else
			{
				logger.error("El form no es compatible con el form de DespachoTrasladoAlmacenForm");
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
     * @param con
     * @param forma
     * @param mundo
     * @param mapping
     * @param request
     * @param usuario
     * @return
     */
    private ActionForward accionGenerarDespacho(Connection con, DespachoTrasladoAlmacenForm forma, DespachoTrasladoAlmacen mundo, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) 
    {
        ActionErrors errores;
        errores = new ActionErrors();
        /**para la fecha de despacho no se encuentre generado el cierre de inventarios*/
        if(UtilidadTexto.getBoolean(forma.getEsDespachar()))
        {
            if(UtilidadInventarios.existeCierreInventarioParaFecha(UtilidadFecha.getFechaActual(),usuario.getCodigoInstitucionInt()))
            {
                errores = new ActionErrors();
                errores.add("cierre de inventarios generado", new ActionMessage("error.inventarios.existeCierreInventarios",UtilidadFecha.getFechaActual()));
                saveErrors(request, errores);
                UtilidadBD.closeConnection(con);            
                return mapping.findForward("paginaErroresActionErrors"); 
            }
            
            /**validar las existencias del almacen segun el parametro general existencias negativas*/
            errores=this.validacionExistencias(con,forma,mundo,usuario,errores);
            if(!errores.isEmpty())
            {
                saveErrors(request, errores);
                UtilidadBD.closeConnection(con);            
                return mapping.findForward("paginaPrincipal"); 
            }        
        }
        HashMap mapa=new HashMap();        
       
        /**Continuar con el proceso de despacho*/
        mapa.put("numeroSolicitud",forma.getMapaTraslados("numero_traslado_"+forma.getRegSeleccionado()));
        mapa.put("fechaDespacho",UtilidadFecha.getFechaActual());
        mapa.put("horaDespacho",UtilidadFecha.getHoraActual());
        if(UtilidadTexto.getBoolean(forma.getEsDespachar()))//solo se guarda el usuario cuando de genera el despacho final
            mapa.put("usuarioDespacho",usuario.getLoginUsuario());
        else
            mapa.put("usuarioDespacho","");
        mundo.setInstitucion(usuario.getCodigoInstitucionInt());
        mundo.setUsuario(usuario.getLoginUsuario());                
        UtilidadBD.iniciarTransaccion(con);
        boolean generoDespacho=mundo.generarDespachoTrans(con,forma.getMapaDetalleSolicitud(),mapa,UtilidadTexto.getBoolean(forma.getEsDespachar()));
        if(!generoDespacho)
        {
	        errores = new ActionErrors();  
	        if(!mundo.getMensaje().trim().equals(""))
	        {
	        	errores.add("concurrencia", new ActionMessage("error.errorEnBlanco",mundo.getMensaje()));	
	        }
	        else
	        {
	        	errores.add("no se grabo informacion despacho", new ActionMessage("errors.noSeGraboInformacion","DEL DESPACHO DEL TRASLADO ALMACEN"));
	        }
	        UtilidadBD.abortarTransaccion(con);
	        saveErrors(request, errores);
	        UtilidadBD.closeConnection(con);	        
	        return mapping.findForward("paginaErroresActionErrors"); 
        }  
        /**para la fecha de despacho no se encuentre generado el cierre de inventarios*/
        if(UtilidadTexto.getBoolean(forma.getEsDespachar()))
        {
            errores=this.actualizarExistenciasAlmacenes(con,forma,mundo,usuario,errores);
            if(!errores.isEmpty())
            {
                saveErrors(request, errores);
                UtilidadBD.abortarTransaccion(con);
                UtilidadBD.closeConnection(con);            
                return mapping.findForward("paginaPrincipal"); 
            }
	        /**validaciones de stock minimo, maximo y punto de pedido*/
	        this.validacionesDeStockPuntoPedido(forma, usuario);
        }
        String tipoConSolicitante=(forma.getMapaTraslados().get("tipo_consignacion_solicitante_"+forma.getRegSeleccionado())+"");
    	String tipoConSolicitado=(forma.getMapaTraslados().get("tipo_consignacion_solicitado_"+forma.getRegSeleccionado())+"");
    	
    	
    	logger.info(">>>>>>>>> tipoConSolicitante ->"+tipoConSolicitante+"<--");
    	logger.info(">>>>>>>>> tipoConSolicitado ->"+tipoConSolicitado+"<--");
    	
    	//si es el mismo tipo de almacen no debe hacer nada adicional.
        if(!tipoConSolicitado.equals(tipoConSolicitante)&&forma.isInterfazCompras()&&UtilidadTexto.getBoolean(forma.getEsDespachar()))
        {
        	HashMap<String, Object> mapaInterfaz=new HashMap();
        	int almacen=usuario.getCodigoCentroCosto();
        	int transaccionInterfaz=ConstantesBD.codigoNuncaValido;
        	int transaccionAxioma=ConstantesBD.codigoNuncaValido;
        	String consecutivo="";
        	ConsecutivosDisponibles consec=new ConsecutivosDisponibles();
        	for(int i=0;i<Utilidades.convertirAEntero(forma.getAlmacenesConsignacion().get("numRegistros")+"");i++)
        	{
        		Utilidades.imprimirMapa(forma.getAlmacenesConsignacion());
        		if(almacen==Utilidades.convertirAEntero(forma.getAlmacenesConsignacion().get("codigo_"+i)+""))
        		{
        			transaccionInterfaz=ConstantesBD.codigoTransaccionEntradaTrasaladoDeConsignacion;
        			i=Utilidades.convertirAEntero(forma.getAlmacenesConsignacion().get("numRegistros")+"");
        		}
        		else if(Utilidades.convertirAEntero(forma.getMapaTraslados("cod_almacen_solicitante_"+forma.getRegSeleccionado())+"")==Utilidades.convertirAEntero(forma.getAlmacenesConsignacion().get("codigo_"+i)+""))
        		{	
        			transaccionInterfaz=ConstantesBD.codigoTransaccionSalidadTrasladoAConsignacion;
        			i=Utilidades.convertirAEntero(forma.getAlmacenesConsignacion().get("numRegistros")+"");
        		}
        	}
        	for(int i=0;i<Utilidades.convertirAEntero(forma.getMapaDetalleSolicitud().get("numRegistros")+"");i++)
        	{
        		Utilidades.imprimirMapa(forma.getMapaTraslados());
        		transaccionAxioma=UtilidadInventarios.obtenerTipoTransaccionInterfaz(con,transaccionInterfaz,usuario.getCodigoInstitucionInt());
        		if(transaccionAxioma>0)
        		{
	        		consecutivo=consec.obtenerConsecutivoInventario(con,transaccionAxioma,Utilidades.convertirAEntero(forma.getMapaTraslados("cod_almacen_solicitante_"+forma.getRegSeleccionado())+""),usuario.getCodigoInstitucionInt())+"";
	        		mapaInterfaz.put("transaccion",transaccionAxioma);
	        		mapaInterfaz.put("numeroTransaccionAxioma", UtilidadInventarios.generarEncabezadoTransaccion(con,Integer.parseInt(consecutivo),transaccionAxioma,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),usuario.getLoginUsuario(),ConstantesBD.codigoNuncaValido,"",ConstantesBDInventarios.codigoEstadoTransaccionInventarioPendiente,Utilidades.convertirAEntero(forma.getMapaTraslados("cod_almacen_solicitante_"+forma.getRegSeleccionado())+""),true));
	        		mapaInterfaz.put("indicativoTransaccion", transaccionInterfaz);
	        		mapaInterfaz.put("identificaionProveedor", forma.getMapaDetalleSolicitud("proveedorconsignacion_"+i));
	        		mapaInterfaz.put("codigoArticulo", forma.getMapaDetalleSolicitud("articulo_"+i));
	        		mapaInterfaz.put("cantidad", forma.getMapaDetalleSolicitud("cantidaddespachada_"+i));
	        		mapaInterfaz.put("almacensolicita", forma.getMapaTraslados("cod_almacen_solicitante_"+forma.getRegSeleccionado()));
	        		mapaInterfaz.put("almacendespacha", forma.getMapaTraslados("cod_almacen_solicitado_"+forma.getRegSeleccionado()));
        		}
        		else
        		{
        			errores = new ActionErrors();  
       	        	errores.add("concurrencia", new ActionMessage("error.errorEnBlanco","Falta definir la transaccion axioma equivalente a la transaccion shaio "+transaccionInterfaz));	
        	        saveErrors(request, errores);
        	        UtilidadBD.abortarTransaccion(con);
        	        UtilidadBD.closeConnection(con);	        
        	        return mapping.findForward("paginaErroresActionErrors");
        		}
	        	try
	        	{
	        		ResultadoInteger resultadoInteger=generarRegistroTransaccionInterfaz(con, mapaInterfaz, usuario);
	    	        forma.setWarnings("warningInterfaz",resultadoInteger.getDescripcion());
	        	}
	        	catch (Exception e) 
	        	{
	        		UtilidadBD.abortarTransaccion(con);
	        		UtilidadBD.closeConnection(con);
	                return mapping.findForward("paginaResumen");
				}
        	}
        }
        UtilidadBD.finalizarTransaccion(con);
        UtilidadBD.closeConnection(con);
	    return mapping.findForward("paginaResumen");  
    }
    /**
     * metodo para realizar la actualizaci�n de 
     * existencias de los almacenes
     * @param con
     * @param forma
     * @param mundo
     * @param usuario 
     * @param errores
     */
    private ActionErrors actualizarExistenciasAlmacenes(Connection con, DespachoTrasladoAlmacenForm forma, DespachoTrasladoAlmacen mundo, UsuarioBasico usuario, ActionErrors errores)
    {       
        mundo.setInstitucion(usuario.getCodigoInstitucionInt());
        boolean enTransaccion=mundo.actualizarExistenciasAlmacenes(con,forma.getMapaDetalleSolicitud(),Integer.parseInt(forma.getMapaTraslados("cod_almacen_solicitante_"+forma.getRegSeleccionado())+""),usuario.getCodigoCentroCosto(),Integer.parseInt(forma.getMapaTraslados("numero_traslado_"+forma.getRegSeleccionado())+""));
        if(!enTransaccion)
            errores.add("no se grabo informacion despacho", new ActionMessage("errors.noSeGraboInformacion","DE LA ACTUALIZACION DE LAS EXISTENCIAS POR ALMACEN"));
        return errores;
    }
    /**
     * metodo para validar las existencias segun 
     * el parametro existencias negativas
     * @param con 
     * @param forma 
     * @param mundo 
     * @param usuario
     * @param errores 
     */
    private ActionErrors validacionExistencias(Connection con, DespachoTrasladoAlmacenForm forma, DespachoTrasladoAlmacen mundo, UsuarioBasico usuario, ActionErrors errores) 
    {
        ValoresPorDefecto.cargarValoresIniciales(con);        
        boolean esExistenciaNegativa=AlmacenParametros.manejaExistenciasNegativas(con, usuario.getCodigoCentroCosto(), usuario.getCodigoInstitucionInt());
        ArrayList array=new ArrayList();
        int existenciaNegativa=0;
        Object warning;
        HashMap mapa=new HashMap ();
        String codigosArticulos="";
        String valoresExistenciasXArticulo="";
        mapa.put("numeroSolicitud",forma.getMapaTraslados("numero_traslado_"+forma.getRegSeleccionado()));
        for(int k=0;k<Integer.parseInt(forma.getMapaDetalleSolicitud("numRegistros")+"");k++)
        {   
        	String codigo=forma.getMapaDetalleSolicitud("codigo_"+k)+"";
        	if(UtilidadTexto.isEmpty(codigo))
        		codigo="-1";
        	mapa.put("codigo", codigo);
            mapa.put("articulo",forma.getMapaDetalleSolicitud("articulo_"+k));   
            mapa.put("cantidaddespachada",forma.getMapaDetalleSolicitud("cantidaddespachada_"+k));
            int exitencias=Integer.parseInt(forma.getMapaDetalleSolicitud("existenciaactual_"+k)+"");
            if(Articulo.articuloManejaLote(con,Integer.parseInt(forma.getMapaDetalleSolicitud("articulo_"+k)+""), usuario.getCodigoInstitucionInt()))
            {
            	exitencias=UtilidadInventarios.existenciasArticuloAlmacenLote(Integer.parseInt(forma.getMapaDetalleSolicitud("articulo_"+k)+""),usuario.getCodigoCentroCosto(),forma.getMapaDetalleSolicitud("lote_"+k)+"",UtilidadFecha.conversionFormatoFechaABD(forma.getMapaDetalleSolicitud("fechavencimiento_"+k)+""));	
            }
            if(Integer.parseInt(forma.getMapaDetalleSolicitud("cantidaddespachada_"+k)+"")>exitencias)
            {
                if(!esExistenciaNegativa)
                {
                    errores.add("articulo existencia", new ActionMessage("error.inventarios.articuloConExistenciaInferiorACantidad",forma.getMapaDetalleSolicitud("descripcion_"+k)+"",exitencias,usuario.getCentroCosto()));
                }
                else
                {
                    existenciaNegativa=exitencias-Integer.parseInt(forma.getMapaDetalleSolicitud("cantidaddespachada_"+k)+"");
                    codigosArticulos+=forma.getMapaDetalleSolicitud("articulo_"+k)+",";
	                valoresExistenciasXArticulo+=existenciaNegativa+",";
                }
            }  
        }
        if(!codigosArticulos.trim().equals(""))
        {	
            warning="ARTICULO(S) [ "+codigosArticulos+" ] QUEDA(N) CON EXISTENCIA(S) NEGATIVA(S) [ "+valoresExistenciasXArticulo+" ] [bz-14]";
	        array.add(warning);
	        forma.setWarnings("warningValidacionExistencias",array);
        }    
        return errores;             
    }
    /**
     * metodo para realizar la consulta del detalle
     * de la solicitud
     * @param con
     * @param mundo
     * @param forma
     * @param usuario
     */
    private void accionCargarSolicitud(Connection con, DespachoTrasladoAlmacenForm forma, DespachoTrasladoAlmacen mundo, UsuarioBasico usuario) 
    {
        mundo.setInstitucion(usuario.getCodigoInstitucionInt());
        mundo.setAlmacen(usuario.getCodigoCentroCosto());
        forma.setMapaDetalleSolicitud(mundo.consultarDetalleSolicitud(con,Integer.parseInt(forma.getMapaTraslados("numero_traslado_"+forma.getRegSeleccionado())+"")));
    }
    /**
     * metodo para realizar las validaciones
     * iniciales
     * @param con
     * @param usuario
     * @param request
     * @param mapping
     * @param mundo
     * @param forma
     * @return
     */
    private ActionForward accionValidacionesIniciales(Connection con, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping, DespachoTrasladoAlmacen mundo, DespachoTrasladoAlmacenForm forma) 
    {
        ActionErrors errores = new ActionErrors();
        if(!UtilidadValidacionInventarios.esCentroCostoSubalmacen(usuario.getCodigoCentroCosto(),usuario.getCodigoInstitucionInt()))
        {             
            logger.warn("usuario no pertenece a un almacen");
            errores.add("usuario no pertenece a un almacen", new ActionMessage("error.usuarioNoPerteneceAlmacen"));            
        }
        if(!UtilidadInventarios.esAlmacenUsuarioAutorizado(usuario.getLoginUsuario(),usuario.getCodigoCentroCosto(),usuario.getCodigoInstitucionInt()))
        {
            logger.warn("usuario no autorzado para el almacen "+usuario.getCentroCosto());
            errores.add("usuario no autorizado", new ActionMessage("error.usuarioNoAutorizadoAlmacen",usuario.getCentroCosto()));
        }
        if(!errores.isEmpty())
        {
            saveErrors(request, errores);
            UtilidadBD.closeConnection(con);
            return mapping.findForward("paginaErroresActionErrors");
        } 
        else
        {
            this.generarConsultaListadoSolicitudes(con,forma,mundo,usuario);
	        UtilidadBD.closeConnection(con);
	        return mapping.findForward("paginaTraslados");  
        }
    }
    /**
     * metodo para generar el listado de las
     * solicitudes
     * @param con
     * @param forma
     * @param mundo
     * @param usuario
     */
    private void generarConsultaListadoSolicitudes(Connection con, DespachoTrasladoAlmacenForm forma, DespachoTrasladoAlmacen mundo, UsuarioBasico usuario) 
    {
       forma.setMapaTraslados(mundo.consultarListadoTrasladosAlmacenes(con,ConstantesBDInventarios.codigoEstadoTrasladoInventarioCerrada,usuario.getCodigoCentroCosto()));         
    }
    /**
     * metodo para ordenar las solicitudes
     * @param con
     * @param forma
     * @param mapping
     * @return
     * @author jarloc
     */	
	private void accionOrdenarMapaSolicitudes(DespachoTrasladoAlmacenForm forma) 
    {         
	    Object temp=forma.getMapaTraslados("numRegistros");
	    String[] indices={
				            "numero_traslado_", 
				            "hora_elaboracion",				            
				            "fecha_elaboracion_",	
				            "prioritario_",				            
				            "usuario_elabora_",	
				            "almacen_solicitante_",	
                            "cod_almacen_solicitante",
				            "observaciones_",
				            "fecha_hora_elaboracion_",
				            "nombre_centro_atencion_"
	            		};
		forma.setMapaTraslados(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaTraslados(),Integer.parseInt(temp+"")));		
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setMapaTraslados("numRegistros",temp);
    }
    /**
     * metodo para realizar las validaciones 
     * correspondientes a a stock minimo,
     * maximo y punto de pedido.
     * los mensajes de alerta si son generados
     * se muestran en el resumen, estos dejan 
     * continuar con el proceso.
     * @param forma
     * @param usuario
     */
    private void validacionesDeStockPuntoPedido(DespachoTrasladoAlmacenForm forma,UsuarioBasico usuario)
    {
        String articulosStockMinimo="",articulosStockMaximo="",articulosPuntoPedido="";
        ArrayList array=new ArrayList();
        Object warning;
        for(int k=0;k<Integer.parseInt(forma.getMapaDetalleSolicitud("numRegistros")+"");k++)   
        {
            if(!UtilidadInventarios.existenciasArticuloMayorIgualStockMinimo(Integer.parseInt(forma.getMapaDetalleSolicitud("articulo_"+k)+""),usuario.getCodigoInstitucionInt()))
            {
                articulosStockMinimo=forma.getMapaDetalleSolicitud("descripcion_"+k)+""+",";                   
            }
            if(!UtilidadInventarios.existenciasArticuloMenorIgualStockMaximo(Integer.parseInt(forma.getMapaDetalleSolicitud("articulo_"+k)+""),usuario.getCodigoInstitucionInt()))
            {
                articulosStockMaximo=forma.getMapaDetalleSolicitud("descripcion_"+k)+""+",";                   
            }
            if(!UtilidadInventarios.existenciasArticuloMayorIgualPuntoPedido(Integer.parseInt(forma.getMapaDetalleSolicitud("articulo_"+k)+""),usuario.getCodigoInstitucionInt()))
            {
                articulosPuntoPedido=forma.getMapaDetalleSolicitud("descripcion_"+k)+""+",";                   
            }
        }       
        if(!articulosStockMinimo.equals(""))
        {
            warning="ARTICULOS "+articulosStockMinimo+" QUEDAN CON CANTIDAD MENOR AL STOCK MINIMO. [bz-16]"; 
            array.add(warning);
            forma.setWarnings("warningsStocksPuntoPedido",array);
        }       
        if(!articulosStockMaximo.equals(""))
        {
            warning="ARTICULOS "+articulosStockMaximo+" QUEDAN CON CANTIDAD MAYOR AL STOCK MAXIMO. [bz-16]"; 
            array.add(warning);
            forma.setWarnings("warningsStocksPuntoPedido",array);           
        }
        if(!articulosPuntoPedido.equals(""))
        {
            warning="ARTICULOS "+articulosPuntoPedido+" QUEDAN CON CANTIDAD MENOR AL PUNTO DE PEDIDO. [bz-16]"; 
            array.add(warning);
            forma.setWarnings("warningsStocksPuntoPedido",array);       
        }       
    }
    
    /**
	 * M�todo para generar el reporte de la consulta de los centros de costo
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 */
	private void generarReporte(Connection con, DespachoTrasladoAlmacenForm forma, UsuarioBasico usuario, HttpServletRequest request) 
    {
			DesignEngineApi comp;
			InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
            comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/","DespachoTrasladoAlmacen.rptdesign");
            comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
            comp.insertGridHeaderOfMasterPage(0,1,1,4);

            Vector v=new Vector();
            v.add(ins.getRazonSocial());
            v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());
            //Si no se posee actividad economica no mostrar el campo
            if(!ins.getActividadEconomica().equals(""))
            {
                v.add("Actividad Económica: "+ins.getActividadEconomica());
            }
            v.add(ins.getDireccion()+"          "+ins.getTelefono());

            comp.insertLabelInGridOfMasterPage(0,1,v);

            
            //83962
    		String DataSet = "detalleListadoArticulos", consulta = "";

    		//Modificamos el DataSet
    		comp.obtenerComponentesDataSet(DataSet);
    		consulta = comp.obtenerQueryDataSet();

			if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(usuario.getCodigoInstitucionInt()).equals(ConstantesIntegridadDominio.acronimoInterfaz))
	        {
                consulta = consulta.replace("1=1", "coalesce(getCodArticuloAxiomaInterfaz(dts.articulo, '"+ConstantesIntegridadDominio.acronimoInterfaz+"'), '')"); //interfaz
	        }
			else
			{
                consulta = consulta.replace("1=1", "dts.articulo");
			}
    		
    		comp.modificarQueryDataSet(consulta);
            
            
            
            
            //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
            if(!newPathReport.equals(""))
            {
                request.setAttribute("isOpenReport", "true");
                request.setAttribute("newPathReport", newPathReport);
            }            
            comp.updateJDBCParameters(newPathReport);
    }
	
	/**
	 * Metodo que inserta en ax_inv de shaio
	 * @param con
	 * @param mapa
	 * @param usuario
	 */
	private ResultadoInteger generarRegistroTransaccionInterfaz(Connection con,HashMap<String, Object> mapa,UsuarioBasico usuario) 
	{
		logger.info("CARGANDO DATOS PARA LA INTERFAZ");
		Utilidades.imprimirMapa(mapa);
		UtilidadBDInventarios utilInterfaz=new UtilidadBDInventarios();
		DtoInterfazTransaccionAxInv dto= new DtoInterfazTransaccionAxInv();
		dto.setTipoTransAxioma(mapa.get("transaccion")+"");
		dto.setNumeroTransaccionAxioma(mapa.get("numeroTransaccionAxioma")+"");
		dto.setIndicativoTransaccion(mapa.get("indicativoTransaccion")+"");
		dto.setIdentificacionProveedor(mapa.get("identificaionProveedor")+"");
		dto.setCodigoArticulo(mapa.get("codigoArticulo")+"");
		dto.setCodigoArticuloInterfaz(UtilidadInventarios.obtenerCodigoInterfazArticulo(con,Utilidades.convertirAEntero(dto.getCodigoArticulo())));
		
		Double valorTotal = UtilidadInventarios.obtenerValorArticuloProveedorConveProveedor(con, dto.getIdentificacionProveedor(), Utilidades.convertirAEntero(dto.getCodigoArticulo()));
		Double valorIva =  UtilidadInventarios.obtenerValorIvaArticuloProveedorConveProveedor(con,dto.getIdentificacionProveedor(), Utilidades.convertirAEntero(dto.getCodigoArticulo()));
		
		logger.info("VALOR TOTAL --->"+UtilidadTexto.formatearValores(valorTotal+"","#.######")+"<-");
		logger.info("VALOR IVA   --->"+UtilidadTexto.formatearValores(valorIva+"","#.######")+"<-");
		double resultado = valorTotal-valorIva; 
		logger.info("VALOR RESULTADO --->"+UtilidadTexto.formatearValores(resultado+"","#.######")+"<-");
		
		dto.setValorUnitario(UtilidadTexto.formatearValores(resultado+"","#.######"));
		dto.setValorIva(valorIva+"");
		dto.setCantidad(mapa.get("cantidad")+"");
		dto.setOrigenTransaccion("2");//alimentacion por axioma
		dto.setIndicativoCostoDonacion(ConstantesBD.acronimoNo);
		dto.setFechaTransaccion(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		dto.setHoraTransaccion(UtilidadFecha.getHoraActual());
		dto.setEstadoRegistro("0");//estado no procesado
		dto.setFechaRegistro(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		dto.setHoraRegistro(UtilidadFecha.getHoraActual());
		
		// adicion Campos Oct 27 de 2008
		dto.setAlmacenSolicita(mapa.get("almacensolicita")+"");
		dto.setAlmacendespacha(mapa.get("almacendespacha")+"");
		
		//Adicion Campos Nov 19 de 2008
		dto.setAlmacenInterfaz(mapa.get("almacensolicita")+"");
		dto.setAlmacenConsignacion(mapa.get("almacendespacha")+"");
		
		
		dto.setUsuario(usuario.getLoginUsuario());
		
		ResultadoInteger resultadoInteger=utilInterfaz.insertarTransaccionInterfaz(dto,usuario.getCodigoInstitucionInt(),false);
		
		return resultadoInteger;
	}
}
