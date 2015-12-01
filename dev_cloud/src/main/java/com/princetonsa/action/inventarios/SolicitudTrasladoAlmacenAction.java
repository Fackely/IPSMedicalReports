/*
 * Creado   11/01/2006
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.action.inventarios;

import java.io.IOException;
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

import util.BloqueosConcurrencia;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.inventarios.ConstantesBDInventarios;
import util.inventarios.UtilidadInventarios;
import util.inventarios.UtilidadValidacionInventarios;

import com.princetonsa.actionform.inventarios.SolicitudTrasladoAlmacenForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.SolicitudTrasladoAlmacen;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * @version 1.0, 11/01/2006
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class SolicitudTrasladoAlmacenAction extends Action 
{
    /**
     * manejador de los logs de la clase
     */
    private Logger logger=Logger.getLogger(SolicitudTrasladoAlmacenAction.class);
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
	    if(form instanceof SolicitudTrasladoAlmacenForm)
	    {
	        SolicitudTrasladoAlmacenForm forma=(SolicitudTrasladoAlmacenForm)form;
	        SolicitudTrasladoAlmacen mundo=new SolicitudTrasladoAlmacen(); 
	        HttpSession sesion = request.getSession();			
			UsuarioBasico usuario = null;
			usuario = Utilidades.getUsuarioBasicoSesion(sesion);
			String estado=forma.getEstado();
			logger.warn("\n estado->"+estado);
			logger.warn("\n accion->"+forma.getAccion());
			
			
			con = UtilidadBD.abrirConexion();
			if(con == null)
			{
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}			
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo de SolicitudTrasladoAlmacenAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}	
			if(estado.equals("empezar"))
			{
			  forma.reset();
			  forma.setMaxPageItem(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
			  forma.setModificarFecha(ConstantesBD.acronimoNo);
			  if(ValoresPorDefecto.getPermitirModificarFechaSolicitudTraslado(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi))
				  forma.setModificarFecha(ConstantesBD.acronimoSi);
			  return this.accionValidacionesIniciales(con,usuario,request,mapping,mundo,forma);			  
			}
			if(estado.equals("generarFiltroAlmacen"))
			{
				forma.setParejasClaseGrupo(UtilidadInventarios.obtenerParejasClaseGrupoInventario(con,usuario.getCodigoInstitucionInt(),usuario.getCodigoCentroCosto(),forma.getCodigoTransaccionTraslado()));
			    forma.setAccion("nuevaSolicitud");
			    UtilidadBD.closeConnection(con);
	            return mapping.findForward("paginaPrincipal");  
			}
			if(estado.equals("continuarSolicitud"))
			{			    
			    if(forma.getAccion().equals("nuevaSolicitud"))
			        return this.accionContinuarSolicitud(con,forma,mundo,usuario,request,mapping);
			    else if(forma.getAccion().equals("modificarSolicitud"))
			        return this.accionModificarSolicitud(con,forma,mundo,usuario,request,mapping,true);
			}			
			else if (estado.equals("redireccion"))
			{			    
			    UtilidadBD.cerrarConexion(con);			    
			    response.sendRedirect(forma.getLinkSiguiente());
			    return null;
			}
			if(estado.equals("eliminarArticulo"))
			{			    
			    this.accionEliminarRegistros(con,forma);
			    return this.redireccion(con, forma, response, request);
			}
			else if(estado.equals("agregarArticulo"))
			{			    
			    forma.setMapaArticulos("tipoRegistro_"+(forma.getNumRegistrosArticulos()-1),"MEM");
			    forma.setUltimoPatron("");
			    this.accionOrdenarMapa(forma);
			    UtilidadBD.closeConnection(con);
			    return this.redireccion(con, forma, response, request);				       
			}
			else if(estado.equals("generarDetalleTransaccion"))
			{	
			    ActionErrors errores=this.validarCierre(forma,request);
			    if(errores.isEmpty())
			    {
			    	try
			    	{
				    	//cuando estoy adicionando los articulos, puedo modificar la solicitud, por esta razon debo llamar el siguiente metodo.
				    	//this.accionModificarSolicitud(con,forma,mundo,usuario,request,mapping,false);
				        if(forma.getAccion().equals("nuevaSolicitud"))
				            return this.generarDetalleSolicitud(con,forma,mundo,request,mapping,usuario);
				        else if(forma.getAccion().equals("modificarSolicitud"))
					        return this.accionModificarDetalleSolicitud(con,forma,mundo,request,mapping,usuario);
			    	}
			    	catch (Exception e) 
			    	{
			    		//errores.add("Numero invalido",new ActionMessage("errors.invalid",1));
			    		request.setAttribute("codigoDescripcionError", "errors.caracterEspecial");
			    		return mapping.findForward("paginaError");
					}
			    }
			    else
			    {
			        UtilidadBD.closeConnection(con);
			        return mapping.findForward("paginaPrincipal");
			    }
			}
			if(estado.equals("generarAnulacion"))
			{
			  return this.accionAnularSolicitud(con,forma,mundo,usuario,mapping,request);			    
			}
			if(estado.equals("empezarBusqueda"))
			{
				logger.info("===> Estamos en empezarBusqueda");
			    forma.setMapaAtributos("almacen_despacha",forma.getCodAlmacen()+"");
			    forma.setMapaAtributos("nombre_almacen_despacha",forma.getNombreAlmacen());
			    forma.setMapaAtributos("almacen_solicitante",usuario.getCodigoCentroCosto()+"");
			    forma.setMapaAtributos("nombre_almacen_solicitante",usuario.getCentroCosto());
			    forma.setMapaAtributos("usuario_elabora",usuario.getLoginUsuario());
			    forma.setMapaAtributos("institucion",usuario.getCodigoInstitucionInt()+"");
			    forma.setMapaAtributos("estado",ConstantesBDInventarios.codigoEstadoTrasladoInventarioPendiente+"");
			    forma.setMapaAtributos("nombre_estado",ConstantesBDInventarios.nombreEstadoTransaccionInventarioPendiente);
			    forma.getMapaSolicitudes().clear();
			    forma.setMapaAtributos("mostrarListado","");
			    forma.setCargarDetalle("");
			    UtilidadBD.closeConnection(con);
		        return mapping.findForward("paginaBusqueda");
			}
			if(estado.equals("generarBusqueda"))
			{			      
			    return this.accionGenerarBusqueda(con,forma,mundo,mapping, request);			    
			}
			if(estado.equals("cargarSolicitud"))
			{			      
			    forma.setAccion("modificarSolicitud");
			    return this.accionCargarSolicitud(con,forma,mapping,usuario);		    
			}
			if(estado.equals("ordenarListadoSolicitudes"))
			{			      
			    this.accionOrdenarMapaSolicitudes(forma); 
			    UtilidadBD.closeConnection(con);
		        return mapping.findForward("paginaBusqueda");
			}
			if(estado.equals("generarReporteDetalle"))
            {   
				InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
            	DesignEngineApi comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/","DespachoTrasladoAlmacenResumen.rptdesign");

            	comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
                
            	comp.insertGridHeaderOfMasterPage(0,1,1,4);
                Vector v=new Vector();
                v.add(ins.getRazonSocial());
                v.add(ins.getTipoIdentificacion()+"     "+ins.getNit());  
                if(!ins.getActividadEconomica().equals(""))//si no se posee actividad economica no mostrar el campo
                    v.add("Actividad Económica: "+ins.getActividadEconomica());
                v.add(ins.getDireccion()+"     "+ins.getTelefono());
                comp.insertLabelInGridOfMasterPage(0,1,v);
                
                comp.insertLabelInGridPpalOfHeader(1,1,"SOLICITUD DE TRASLADO");
                comp.insertLabelInGridPpalOfHeader(2,0,ins.getEncabezadoFactura());


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
                //por ultimo se modifica la conexion a BD
                comp.updateJDBCParameters(newPathReport);
                UtilidadBD.cerrarConexion(con);
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
	        logger.error("El form no es compatible con el form de SolicitudTrasladoAlmacenForm");
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
     * metodo para modificar el detalle de la solicitud
     * @param con
     * @param forma
     * @param mundo
     * @param usuario
     * @param request
     * @param mapping
     * @param usuario
     * @return
     */
    private ActionForward accionModificarDetalleSolicitud(Connection con, SolicitudTrasladoAlmacenForm forma, SolicitudTrasladoAlmacen mundo, HttpServletRequest request, ActionMapping mapping, UsuarioBasico usuario) 
    {       
    	logger.info("\n entro a accionModificarDetalleSolicitud  ");
    	ActionErrors errores = new ActionErrors();  
        boolean existeCierre=false;
        if(UtilidadTexto.getBoolean(forma.getEsCerrarTransaccion()))
            existeCierre=UtilidadInventarios.existeCierreInventarioParaFecha(UtilidadFecha.getFechaActual(),usuario.getCodigoInstitucionInt());  
        if(existeCierre)
        {
            errores.add("existe cierre de inventarios", new ActionMessage("error.inventarios.existeCierreInventarios",UtilidadFecha.getFechaActual()));
   	        saveErrors(request, errores);
   	        UtilidadBD.closeConnection(con);	   	        
   	        return mapping.findForward("paginaErroresActionErrors");  
        }
        else
        {
	        mundo.setUsuario(usuario.getLoginUsuario());
	        mundo.setInstitucion(usuario.getCodigoInstitucionInt());
	        
	        ArrayList filtro=new ArrayList();
			filtro.add(forma.getNumeroTrasladoConsecutivo()+"");
			UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloquearSolicitudTraslado, filtro);
			//dummy para bloquear el registro
			//se actualiza el estado de la transaccion con el mismo que se tiene.
			mundo.actualizarEstadoSolicitud(con, Utilidades.obtenerEstadoSolicitudTraslado(con,forma.getNumeroTrasladoConsecutivo()+""), forma.getNumeroTrasladoConsecutivo(), usuario.getCodigoInstitucionInt(),true);
			
			if(Utilidades.obtenerEstadoSolicitudTraslado(con,forma.getNumeroTrasladoConsecutivo()+"")!=ConstantesBDInventarios.codigoEstadoTrasladoInventarioPendiente)
			{
				UtilidadBD.abortarTransaccion(con);
		        errores.add("no se grabo informacion modificacion", new ActionMessage("errors.noSeGraboInformacion","DE LA MODIFICACION SOLICITUD"));
		        errores.add("solicitud modificada por otro usuario", new ActionMessage("error.errorEnBlanco","La solicitud de traslado fue modificada por otro usuario."));
		        saveErrors(request, errores);
		        UtilidadBD.closeConnection(con);	        
		        return mapping.findForward("paginaErroresActionErrors"); 
			}
	        
	        boolean modifico=mundo.modificarDetalleSolicitudTrans(con,forma.getMapaArticulos(),forma.getRegEliminadosBD(),forma.getNumRegistrosArticulos(),forma.getNumeroTrasladoConsecutivo(),UtilidadTexto.getBoolean(forma.getEsCerrarTransaccion()));
	        if(!modifico)
		    {		          
		        errores.add("no se grabo informacion modificacion", new ActionMessage("errors.noSeGraboInformacion","DE LA MODIFICACION DEL DETALLE DE LA SOLICITUD"));
		        saveErrors(request, errores);
		        UtilidadBD.closeConnection(con);	        
		        return mapping.findForward("paginaErroresActionErrors"); 
		    }
	        else
	        {
	            UtilidadBD.closeConnection(con);
	            return mapping.findForward("paginaResumen");
	        } 
        }
    }
    
    /**
     * metodo para realizar la modificación de la
     * solicitud
     * @param con
     * @param forma
     * @param mundo
     * @param usuario
     * @param request
     * @param mapping
     * @return
     */
    private ActionForward accionModificarSolicitud(Connection con, SolicitudTrasladoAlmacenForm forma, SolicitudTrasladoAlmacen mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping,boolean retornarDetalle) 
    {
    	logger.info("\n accionModificarSolicitud");
    	
        HashMap mapa=new HashMap();
        mapa.put("prioritario",forma.getEsPrioritario());
        mapa.put("fecha_elaboracion",UtilidadFecha.conversionFormatoFechaABD(forma.getFechaElaboracion()));
        mapa.put("hora_elaboracion",forma.getHoraElaboracion());
        mapa.put("observaciones",forma.getObservaciones());
        mapa.put("numero_traslado",forma.getNumeroTrasladoConsecutivo()+"");
        mapa.put("institucion",usuario.getCodigoInstitucionInt()+"");
        mapa.put("nombre_almacen_solicita",usuario.getCentroCosto());
        mapa.put("almacen_despacha",forma.getNombreAlmacen());
        mapa.put("nombre_estado",forma.getMapaAtributos("estadoTraslado"));
        mundo.setInstitucion(usuario.getCodigoInstitucionInt());
        mundo.setUsuario(usuario.getLoginUsuario());
        
	    
		ArrayList filtro=new ArrayList();
		filtro.add(mapa.get("numero_traslado")+"");
		UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloquearSolicitudTraslado, filtro);
		
		if(Utilidades.obtenerEstadoSolicitudTraslado(con,mapa.get("numero_traslado")+"")!=ConstantesBDInventarios.codigoEstadoTrasladoInventarioPendiente)
		{
			UtilidadBD.abortarTransaccion(con);
	        ActionErrors errores = new ActionErrors();  
	        errores.add("no se grabo informacion modificacion", new ActionMessage("errors.noSeGraboInformacion","DE LA MODIFICACION SOLICITUD"));
	        errores.add("solicitud modificada por otro usuario", new ActionMessage("error.errorEnBlanco","La solicitud de traslado fue modificada por otro usuario."));
	        saveErrors(request, errores);
	        UtilidadBD.closeConnection(con);	        
	        return mapping.findForward("paginaErroresActionErrors"); 
		}
		
        
        boolean esActualizar=mundo.actualizarInformacionGeneralSolicitud(con,mapa,forma.getMapaSolicitudes(),forma.getRegSeleccionado());
        if(retornarDetalle)
        {
	        if(!esActualizar)
		    {
		        ActionErrors errores = new ActionErrors();  
		        errores.add("no se grabo informacion modificacion", new ActionMessage("errors.noSeGraboInformacion","DE LA MODIFICACION SOLICITUD"));
		        saveErrors(request, errores);
		        UtilidadBD.closeConnection(con);	        
		        return mapping.findForward("paginaErroresActionErrors"); 
		    }
	        else        
	            return this.accionCargarDetalleSolicitud(con,forma,mundo,mapping);        
        }
        return null;
    }
    /**
     * metodo para cargar el detalle de la 
     * solicitud
     * @param con
     * @param forma
     * @param mundo
     * @param request
     * @param mapping
     * @return
     */
    private ActionForward accionCargarDetalleSolicitud(Connection con, SolicitudTrasladoAlmacenForm forma, SolicitudTrasladoAlmacen mundo, ActionMapping mapping) 
    {
    	logger.info("\n entre a accionCargarDetalleSolicitud jhony");
    	HashMap mapa=new HashMap();
    	
        mapa=mundo.consultaDetalleSolicitud(con,forma.getNumeroTrasladoConsecutivo());
        forma.setNumRegistrosArticulos(Integer.parseInt(mapa.get("numRegistros")+""));
        forma.setCargarDetalle("visible");
        forma.setMapaArticulos(mapa);
        UtilidadBD.closeConnection(con);
        return mapping.findForward("paginaPrincipal");
    }
    /**
     * metodo para cargar la solicitud, para
     * realizar modificaciones
     * @param con
     * @param forma
     * @param mundo
     * @param mapping
     * @param usuario
     * @return
     */
    private ActionForward accionCargarSolicitud(Connection con, SolicitudTrasladoAlmacenForm forma, ActionMapping mapping, UsuarioBasico usuario) 
    {
        this.inicializarValoresModificacion(con,forma,usuario);
        UtilidadBD.closeConnection(con);
        return mapping.findForward("paginaPrincipal");
    }
    /**
     * @param con
     * @param forma
     * @param usuario
     * 
     */
    private void inicializarValoresModificacion(Connection con, SolicitudTrasladoAlmacenForm forma, UsuarioBasico usuario) 
    {
    	logger.info("\n entre a inicializarValoresModificacion mapa solicitudes "+forma.getMapaSolicitudes()+"  index-->"+forma.getRegSeleccionado() );
        forma.setMapaAtributos("estadoTraslado",forma.getMapaSolicitudes("nombre_estado_"+forma.getRegSeleccionado()));
        forma.setMapaAtributos("almacenSolicita",forma.getMapaSolicitudes("nombre_almacen_solicita_"+forma.getRegSeleccionado()));
        forma.setMapaAtributos("usuarioElabora",forma.getMapaSolicitudes("usuario_elabora_"+forma.getRegSeleccionado()));
        //ValoresPorDefecto.cargarValoresIniciales(con);        
        forma.setMapaAtributos("esModificarFecha",ValoresPorDefecto.getModificacionFechaInventario(usuario.getCodigoInstitucionInt()));
        forma.setFechaElaboracion(forma.getMapaSolicitudes("fecha_elaboracion_"+forma.getRegSeleccionado())+"");
        forma.setHoraElaboracion(forma.getMapaSolicitudes("hora_elaboracion_"+forma.getRegSeleccionado())+"");   
        forma.setNumeroTrasladoConsecutivo(Integer.parseInt(forma.getMapaSolicitudes("numero_traslado_"+forma.getRegSeleccionado())+""));
        forma.setEsPrioritario(forma.getMapaSolicitudes("prioritario_"+forma.getRegSeleccionado())+"");
        forma.setObservaciones(forma.getMapaSolicitudes("observaciones_"+forma.getRegSeleccionado())+"");
    }
    /**
     * @param con
     * @param forma
     * @param mundo
     * @param mapping
     * @param request
     * @return
     */
    private ActionForward accionGenerarBusqueda(
    		Connection con, 
    		SolicitudTrasladoAlmacenForm forma, 
    		SolicitudTrasladoAlmacen mundo, 
    		ActionMapping mapping,
    		HttpServletRequest request)
    {
        HashMap mapa=new HashMap();
        mapa=mundo.ejecutarBusquedaAvanzada(con,forma.getMapaAtributos());
        /*
         * Atributos para la generación y control de errores
         */
        ActionErrors errores = new ActionErrors();
        errores = validarBusqueda(con, forma, errores);
        
        if(!errores.isEmpty())
		{
			logger.info("===> Entré al if de guardarInsertar, Si Hay Errores");
			saveErrors(request, errores);
			return mapping.findForward("paginaBusqueda");
			//return mapping.findForward("nuevo");
		}
        
        forma.setMapaSolicitudes(mapa);
        if(Integer.parseInt(forma.getMapaSolicitudes("numRegistros").toString())>0)        
         forma.setMapaAtributos("mostrarListado","true");   
        else
         forma.setMapaAtributos("mostrarListado","false");
        logger.info("Estoy en accionGenerarBusqueda");
        
        
        UtilidadBD.closeConnection(con);
        return mapping.findForward("paginaBusqueda");
    }
    
    /**
	 * Método para realizar las validaciones la búsqueda
	 * @param con Connection
	 * @param forma SolicitudTrasladoAlmacenForm
	 * @param errores ActionErrors
	 * @return ActionErrors errores
	 */
	private ActionErrors validarBusqueda(Connection con, SolicitudTrasladoAlmacenForm forma ,ActionErrors errores)
	{
		/*
		 * Validaciones de los Números de Traslados
		 * Delimitacion de los parámetros de búsqueda:
		 * numero_traslado_final
		 * numero_traslado_inicial
		 */
		logger.info("===> Validaciones de los Números de Traslados");
		
		/*
		 * Validaciones de ambos numeros de traslados si vienen vacíos
		 */
		if((forma.getMapaAtributos("numero_traslado_inicial")+"").equals("")&&
			(forma.getMapaAtributos("numero_traslado_final")+"").equals(""))
		{
			logger.info("===> trasladoInicial y trasladoFinal Vienen Vacíos");
		}
		else 
		{
			/*
			 * Validaciones para cuando los numeros de traslados vengan por lo menos 1 de ellos lleno
			 * trasladoInicial diferente de vacío, el trasladoFinal es requerido
			 */
			if((forma.getMapaAtributos("numero_traslado_inicial")+"").equals(""))
			{
				logger.info("===> trasladoInicial es requerido");
				errores.add("Traslado inicial requerido",new ActionMessage("errors.required","El Número de Traslado Inicial "));
			}
			
			/*
			 * Validaciones para cuando los numeros de traslados vengan por lo menos 1 de ellos lleno
			 * trasladoFinal diferente de vacío, el trasladoInicial es requerido
			 */
			if((forma.getMapaAtributos("numero_traslado_final")+"").equals(""))
			{
				logger.info("===> trasladoFinal es requerido");
				errores.add("Traslado Final requerido",new ActionMessage("errors.required","El Número de Traslado Final "));
			}
			
			/*
			 * Validaciones para cuando los numeros de traslados vengan llenos
			 */
			if(!(forma.getMapaAtributos("numero_traslado_inicial")+"").equals("") &&
				!(forma.getMapaAtributos("numero_traslado_final")+"").equals(""))
			{
				logger.info("===> trasladoInicial y trasladoFinal Vienen Llenos");
				float
					trasladoInicial = Float.parseFloat((forma.getMapaAtributos("numero_traslado_inicial")+"")),
					trasladoFinal = Float.parseFloat(forma.getMapaAtributos("numero_traslado_final")+"");
				logger.info("===> trasladoInicial = "+trasladoInicial);
				logger.info("===> trasladoFinal = "+trasladoFinal);
				
				/*
				 * TrasladoInicial debe ser mayor que 0
				 */
				if(trasladoInicial < 0 )
				{
					logger.info("===> Traslado Inicial DEBE SER Mayor que 0");
					errores.add("Traslado Inicial Mayor que 0",
							new ActionMessage("error.errorEnBlanco",
									"El Número de la Transacción Inicial debe Ser Mayor que 0"));
				}
				/*
				 * TrasladoInicial debe ser menor que 999999999
				 */
				else if(trasladoInicial > 999999999 )
				{
					logger.info("===> Traslado Inicial DEBE SER Menor a 999999999");
					errores.add("Traslado Inicial De 9 Digitos",
							new ActionMessage("error.errorEnBlanco",
									"El Número de la Transacción Inicial No Puede Ser Mayor de 9 Dígitos"));
				}
				
				/* 
				 * TrasladoFinal debe ser mayor que 0
				 */
				if(trasladoFinal < 0 )
				{
					logger.info("===> Traslado Final DEBE SER Mayor que 0");
					errores.add("Traslado Inicial Mayor que 0",
							new ActionMessage("error.errorEnBlanco",
									"El Número de la Transacción Final debe Ser Mayor que 0"));
				}
				/*
				 * TrasladoFinal debe ser menor que 999999999
				 */
				else if(trasladoFinal > 999999999 )
				{
					logger.info("===> Traslado Final DEBE SER Menor a 999999999");
					errores.add("Traslado Final De 9 Digitos",
							new ActionMessage("error.errorEnBlanco",
									"El Número de la Transacción Final No Puede Ser Mayor de 9 Dígitos"));
				}
				
				/*
				 * TrasladoInicial debe de ser Menor o igual al Traslado Final
				 */
				if(trasladoInicial>trasladoFinal)
				{
					logger.info("===> Traslado Inicial DEBE SER Menor que Traslado Final");
					errores.add("Traslado Inicial Menor a Traslado Final",
							new ActionMessage("error.errorEnBlanco",
									"El Número de la Transacción Inicial Debe Ser Menor o Igual a el Número de la Transacción Final"));
				}
			}
			return errores;
		}
		
		logger.info("===> Validaciones de las Fechas:");
		/*
		 * Validaciones de las Fechas:
		 * 1)Validacion: Requerido fecha inicial
		 */
		boolean fecha=true;
		if (!UtilidadCadena.noEsVacio(forma.getMapaAtributos("fecha_elaboracion_inicial")+""))
		{
			fecha=false;
			errores.add("descripcion",new ActionMessage("errors.required","Seleccionar la Fecha Inicial"));
		}
		
		/*
		 * 2)Validacion: La fecha inicial sea menor a la fecha actual del sistema
		 */
		else if (
				!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getMapaAtributos("fecha_elaboracion_inicial")+"",
						UtilidadFecha.getFechaActual()))
			{
				fecha=false;
				errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+
						forma.getMapaAtributos("fecha_elaboracion_inicial")+"", "Actual "+UtilidadFecha.getFechaActual()));
			}
		
		/*
		 * 3)Validacion: Requerido fecha final
		 */
		if (!UtilidadCadena.noEsVacio(forma.getMapaAtributos("fecha_elaboracion_final")+""))
		{
			fecha=false;
			errores.add("descripcion",new ActionMessage("errors.required","Seleccionar la Fecha Final"));
		}
		
		/*
		 * 4)Validacion: La fecha final sea menor o igual a la fecha actual
		 */
		else if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getMapaAtributos("fecha_elaboracion_final")+"",UtilidadFecha.getFechaActual()))
		{
			fecha=false;
			errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+
					forma.getMapaAtributos("fecha_elaboracion_final")+"", "Actual "+UtilidadFecha.getFechaActual()));
		}
		
		/*
         * 5)Validacion: La fecha final sea menor o igual a la fecha actual
         */
        logger.info("===> Entré a validarBusqueda");
        logger.info("===> la fecha es: "+forma.getFechaElaboracion());
        
        if(!(forma.getMapaAtributos("fecha_elaboracion_inicial")+"").equals("") && 
        	!(forma.getMapaAtributos("fecha_elaboracion_final")+"").equals(""))
		{
			logger.info("===> Entré a validar si la fecha viene null ***");
			logger.info("===> Aqui vamos a validar las fechas Fecha Final*** -->"+ forma.getMapaAtributos("fecha_elaboracion_final"));
			logger.info("===> Aqui vamos a validar las fechas Fecha Inicial*** -->"+ forma.getMapaAtributos("fecha_elaboracion_inicial"));
			if (UtilidadFecha.numeroMesesEntreFechasExacta(forma.getMapaAtributos("fecha_elaboracion_inicial")+"", 
					forma.getMapaAtributos("fecha_elaboracion_final")+"") == -1)
			{
				logger.info("===> Entré a validar las fechas inicial y final ***");
				logger.info("===> Aqui vamos a validar las fechas Fecha Final*** -->"+ forma.getMapaAtributos("fecha_elaboracion_final"));
				logger.info("===> Aqui vamos a validar las fechas Fecha Inicial*** -->"+ forma.getMapaAtributos("fecha_elaboracion_inicial"));
				errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+
						forma.getMapaAtributos("fecha_elaboracion_inicial"), "Final "+forma.getMapaAtributos("fecha_elaboracion_final")+""));
			}
		}
        
        /*
         * 6)Validación: El rango entre las fechas debe ser menor a 3 meses
         */
        if (fecha)
		{
			logger.info(" El rango de fechas es: --> "+UtilidadFecha.numeroDiasEntreFechas(forma.getMapaAtributos("fecha_elaboracion_inicial")+"", 
					forma.getMapaAtributos("fecha_elaboracion_final")+""));
			logger.info(" El rango de fechas es: --> "+UtilidadFecha.numeroMesesEntreFechasExacta(forma.getMapaAtributos("fecha_elaboracion_inicial")+"", 
					forma.getMapaAtributos("fecha_elaboracion_final")+""));
			if(UtilidadFecha.numeroDiasEntreFechas(forma.getMapaAtributos("fecha_elaboracion_inicial")+"", forma.getMapaAtributos("fecha_elaboracion_final")+"")>92)
				//En donde el numero final al cual se compara, es el numero de meses del rango.
				errores.add("", new ActionMessage("errors.debeSerNumeroMenorIgual","El rango de dias entre fechas", "3 Meses"));
		}
        
		return errores;
	}
    
    /**
     * metodo para anular la solicitud
     * @param con
     * @param forma
     * @param mundo
     * @param usuario
     * @param mapping
     * @param request
     */
    private ActionForward accionAnularSolicitud(Connection con, SolicitudTrasladoAlmacenForm forma, SolicitudTrasladoAlmacen mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) 
    {
    	
    	ActionErrors errores = new ActionErrors();
    	
    	ArrayList filtro=new ArrayList();
		filtro.add(forma.getNumeroTrasladoConsecutivo()+"");
		UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloquearSolicitudTraslado, filtro);
		//dummy para bloquear el registro
		//se actualiza el estado de la transaccion con el mismo que se tiene.
		mundo.actualizarEstadoSolicitud(con, Utilidades.obtenerEstadoSolicitudTraslado(con,forma.getNumeroTrasladoConsecutivo()+""), forma.getNumeroTrasladoConsecutivo(), usuario.getCodigoInstitucionInt(),true);
		
		if(Utilidades.obtenerEstadoSolicitudTraslado(con,forma.getNumeroTrasladoConsecutivo()+"")!=ConstantesBDInventarios.codigoEstadoTrasladoInventarioPendiente)
		{
			UtilidadBD.abortarTransaccion(con);
	        errores.add("no se grabo informacion modificacion", new ActionMessage("errors.noSeGraboInformacion","DE LA ANULACION SOLICITUD"));
	        errores.add("solicitud modificada por otro usuario", new ActionMessage("error.errorEnBlanco","La solicitud de traslado fue modificada por otro usuario."));
	        saveErrors(request, errores);
	        UtilidadBD.closeConnection(con);	        
	        return mapping.findForward("paginaErroresActionErrors"); 
		}
	        
    	
        boolean esAnular=mundo.anularSolicitudTrans(con,forma.getNumeroTrasladoConsecutivo(),usuario.getLoginUsuario(),forma.getMotivoAnulacion(),usuario.getCodigoInstitucionInt());        
	    if(!esAnular)
	    {
	        errores.add("no se grabo informacion anulación", new ActionMessage("errors.noSeGraboInformacion","DE LA ANULACION SOLICITUD"));
	        saveErrors(request, errores);
	        UtilidadBD.closeConnection(con);	        
	        return mapping.findForward("paginaErroresActionErrors"); 
	    }
	    forma.setEstado("solicitudAnulada");
	    UtilidadBD.closeConnection(con);
        return mapping.findForward("paginaResumen");          
    }
    /**
     * metodo para validar si la solicitud posee
     * articulos y poder generar el cierre     
     * @param forma
     * @param request
     */
    private ActionErrors validarCierre(SolicitudTrasladoAlmacenForm forma, HttpServletRequest request) 
    {
        ActionErrors errores = new ActionErrors();
        if(UtilidadTexto.getBoolean(forma.getEsCerrarTransaccion()))
        {
            if(forma.getMapaArticulos().isEmpty() || (forma.getMapaArticulos().containsKey("numRegistros") && forma.getMapaArticulos("numRegistros").toString().equals("0")))
            {    
                errores.add("falta detalle de articulos", new ActionMessage("error.inventarios.faltaDetalleArticulos","TRASLADO"));
    	        saveErrors(request, errores);  	            	          
            }               
        }
        return errores;
    }
    /**
     * metodo para  generar el detalle de la
     * solicitud, por articulos
     * @param con
     * @param forma
     * @param mundo
     * @param response
     * @param request
     * @param mapping
     * @param usuario
     * @return
     */
    private ActionForward generarDetalleSolicitud(Connection con, SolicitudTrasladoAlmacenForm forma, SolicitudTrasladoAlmacen mundo, HttpServletRequest request, ActionMapping mapping, UsuarioBasico usuario) 
    {
    	logger.info("\n generarDetalleSolicitud");
        ActionErrors errores = new ActionErrors();
        boolean existeCierre=false;
        int numeroTraslado=ConstantesBD.codigoNuncaValido;
        //////////////////////////////////////////////////////////////////////////////////////////////
        //se inserta el encabnezado del traslado
        
        numeroTraslado=guardarEncabezadoTraslado(con, forma, mundo, usuario, request);
        ///////////////////////////////////////////////////////////////////////////////////////////
        
        
        if(UtilidadTexto.getBoolean(forma.getEsCerrarTransaccion()))
            existeCierre=UtilidadInventarios.existeCierreInventarioParaFecha(UtilidadFecha.getFechaActual(),usuario.getCodigoInstitucionInt());  
        if(existeCierre)
        {
            errores.add("existe cierre de inventarios", new ActionMessage("error.inventarios.existeCierreInventarios",UtilidadFecha.getFechaActual()));
   	        saveErrors(request, errores);
   	        UtilidadBD.closeConnection(con);	   	        
   	        return mapping.findForward("paginaErroresActionErrors");  
        }
        else
        {
	       if (forma.getNumeroTrasladoConsecutivo()>0)
	       {
        	forma.setConTrans(UtilidadBD.abrirConexion());
	        forma.setEnTransaccion(mundo.empezarTransaccional(forma.getConTrans()));
	        boolean generoDet=mundo.insertarDetalleSolicitud(forma.getConTrans(),forma.getMapaArticulos(),forma.getNumRegistrosArticulos(),forma.getNumeroTrasladoConsecutivo(),forma.isEnTransaccion());              
	        if(generoDet)
	        {
		        if(UtilidadTexto.getBoolean(forma.getEsCerrarTransaccion()))
		        {
		            boolean cerroSolicitud=mundo.cerrarSolicitud(forma.getConTrans(),forma.getNumeroTrasladoConsecutivo(),usuario.getCodigoInstitucionInt(),usuario.getLoginUsuario(),forma.isEnTransaccion());	            
		            if(!cerroSolicitud)
		            {
		                errores.add("no se grabo informacion del cierre", new ActionMessage("errors.noSeGraboInformacion","DEL CIERRE DE LA SOLICITUD"));
		    	        saveErrors(request, errores);
		    	        UtilidadBD.closeConnection(con);
		    	        UtilidadBD.closeConnection(forma.getConTrans());
		    	        return mapping.findForward("paginaErroresActionErrors");  
		            }
		        }
		        mundo.cerrarTransaccion(forma.getConTrans(),forma.isEnTransaccion());
	        }
	        else
	        {
	            errores.add("no se grabo informacion detalle", new ActionMessage("errors.noSeGraboInformacion","DEL DETALLE DE LA SOLICITUD"));
		        saveErrors(request, errores);
		        UtilidadBD.closeConnection(con);
		        UtilidadBD.closeConnection(forma.getConTrans());
		        return mapping.findForward("paginaErroresActionErrors");
	        }
	       }
	        UtilidadBD.closeConnection(forma.getConTrans());
	        UtilidadBD.closeConnection(con);
	        return mapping.findForward("paginaResumen");  
        }
    }
    
    
    
    
    private int guardarEncabezadoTraslado(Connection con,SolicitudTrasladoAlmacenForm forma, SolicitudTrasladoAlmacen mundo, UsuarioBasico usuario, HttpServletRequest request) 
    {
    	 UtilidadBD.iniciarTransaccion(con);
         ActionErrors errores = new ActionErrors();
         int numeroTraslado=ConstantesBD.codigoNuncaValido;
         String numTras=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBDInventarios.nombreConsecutivoInventarioTrasladoAlmacen,usuario.getCodigoInstitucionInt());
        // forma.setNumeroTrasladoConsecutivo();
         forma.setNumeroTrasladoConsecutivo(Integer.parseInt(numTras.trim().equals("")?ConstantesBD.codigoNuncaValido+"":numTras));
         
         if(forma.getNumeroTrasladoConsecutivo()==ConstantesBD.codigoNuncaValido)
         {            
             errores.add("falta definir consecutivo", new ActionMessage("error.inventarios.faltaDefinirConsecutivo"));
             saveErrors(request, errores);
             UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBDInventarios.nombreConsecutivoInventarioTrasladoAlmacen, usuario.getCodigoInstitucionInt(), numTras, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
             UtilidadBD.abortarTransaccion(con);
             UtilidadBD.closeConnection(con);
             return ConstantesBD.codigoNuncaValido;
         }
         else
         {
             this.copiarDatosAlMundo(forma,mundo,usuario);
             boolean inserto=mundo.generarInformacionGeneralSolicitud(con,forma.getNumeroTrasladoConsecutivo(),usuario.getLoginUsuario());
             //en caso de que no inserte se aborta desde el mundo
             if(!inserto)
             {
                 errores.add("no se inserto informacion", new ActionMessage("errors.noSeGraboInformacion","GENERAL DE LA SOLICITUD"));
                 UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBDInventarios.nombreConsecutivoInventarioTrasladoAlmacen, usuario.getCodigoInstitucionInt(), numTras, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
                 UtilidadBD.abortarTransaccion(con);
                 saveErrors(request, errores);
                 UtilidadBD.closeConnection(con);
                 return ConstantesBD.codigoNuncaValido;  
             }
             else
             {
                 forma.setCargarDetalle("visible");
             }
         }
         //si llega hasta aca entonces todo salio bien
         UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBDInventarios.nombreConsecutivoInventarioTrasladoAlmacen, usuario.getCodigoInstitucionInt(), numTras, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
         UtilidadBD.finalizarTransaccion(con);
         UtilidadBD.closeConnection(con);
         return forma.getNumeroTrasladoConsecutivo();       
    }
    
    /**
     * @param forma
     * @param mundo
     * @param usuario
     * @param mapping
     * @param request
     */
    private ActionForward accionContinuarSolicitud(Connection con,SolicitudTrasladoAlmacenForm forma, SolicitudTrasladoAlmacen mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) 
    {
          forma.setCargarDetalle("visible");
         UtilidadBD.closeConnection(con);
         return mapping.findForward("paginaPrincipal");            
    }
    /**
     * metodo para pasar valores de la forma al mundo
     * @param forma
     * @param mundo
     * @param usuario
     */
    private void copiarDatosAlMundo(SolicitudTrasladoAlmacenForm forma, SolicitudTrasladoAlmacen mundo, UsuarioBasico usuario) 
    {
       mundo.setAlmacenSolicitado(forma.getCodAlmacen());
       mundo.setAlmacenSolicitante(usuario.getCodigoCentroCosto());
       mundo.setInstitucion(usuario.getCodigoInstitucionInt()); 
       mundo.setFechaElaboracion(forma.getFechaElaboracion());
       mundo.setHoraElaboracion(forma.getHoraElaboracion());
       mundo.setEsPrioritario(forma.getEsPrioritario());
       mundo.setObservaciones(forma.getObservaciones());
    }
    /**
     * Metodo para realizar las validaciones
     * iniciales
     * @param con
     * @param usuario 
     * @param mapping
     * @param request
     * @param mundo
     * @param forma
     */
    private ActionForward accionValidacionesIniciales(Connection con, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping, SolicitudTrasladoAlmacen mundo, SolicitudTrasladoAlmacenForm forma) 
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
        ValoresPorDefecto.cargarValoresIniciales(con);
        forma.setCodigoTransaccionTraslado(ValoresPorDefecto.getCodigoTransTrasladoAlmacenes(usuario.getCodigoInstitucionInt(),true));
        String numTras=UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBDInventarios.nombreConsecutivoInventarioTrasladoAlmacen,usuario.getCodigoInstitucionInt());
        if(Integer.parseInt(numTras.trim().equals("")?ConstantesBD.codigoNuncaValido+"":numTras)<=0)
       	{
        	logger.warn("NO SE HA DEFINIDO EL CONSECUTIVO");
            errores.add("consecutivo sin definir", new ActionMessage("error.faltaDefinirConsecutivo","SOLICITUD DE TRASLADOS."));
       	}
        if(!forma.getCodigoTransaccionTraslado().equals(""))
        {
	        if(!UtilidadValidacionInventarios.esAlmacenAutorizadoParaTraslado(usuario.getCodigoInstitucionInt(),Integer.parseInt(forma.getCodigoTransaccionTraslado()),usuario.getCodigoCentroCosto()))
	        {
	            logger.warn("almacen "+usuario.getCentroCosto()+" no autorizado para traslados");
	            errores.add("usuario no autorizado", new ActionMessage("error.almacenNoAutorizado",usuario.getCentroCosto()));
	        }
        }   
        else
        {            
            errores.add("falta definir parametro", new ActionMessage("error.inventarios.sinDefinirTipoTransaccion","Código de Transacción Utilizado para Traslados de Almacenes"));
            saveErrors(request, errores);
            UtilidadBD.closeConnection(con);
            return mapping.findForward("paginaErroresActionErrors");
        }
        if(!errores.isEmpty())
        {
            saveErrors(request, errores);
            UtilidadBD.closeConnection(con);
            return mapping.findForward("paginaErroresActionErrors");
        } 
        else
        {
            return this.generarConsultaAlmacenes(con,forma,mundo,usuario,mapping,request);            
        }               		
    }
    /**
     * metodo para generar el listado de
     * almacenes
     * @param forma
     * @param mundo
     * @param usuario
     * @param mapping
     * @param request
     */
    private ActionForward generarConsultaAlmacenes(Connection con,SolicitudTrasladoAlmacenForm forma,SolicitudTrasladoAlmacen mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)
    {
        forma.setMapAlmacenes(mundo.genrarConsultaAlmacenes(usuario.getCodigoInstitucionInt(),usuario.getCodigoCentroCosto(),forma.getCodigoTransaccionTraslado()));
        if(Integer.parseInt(forma.getMapAlmacenes("numRegistros").toString())==0)
        {
            ActionErrors errores = new ActionErrors();
            errores.add("no hay definido almacen", new ActionMessage("error.inventarios.noHayDefifinidoNingunAlmacenTraslados"));
            saveErrors(request, errores);
            UtilidadBD.closeConnection(con);
            return mapping.findForward("paginaErroresActionErrors");
        }
        else if(Integer.parseInt(forma.getMapAlmacenes("numRegistros").toString())==1)
        {
            logger.warn("->Existe un solo almacen en la lista"); 
            forma.setAccion("nuevaSolicitud");
            inicializarValoresRegistroNuevo(con,forma,usuario,false);
            UtilidadBD.closeConnection(con);
            return mapping.findForward("paginaPrincipal");
        }
        else if(Integer.parseInt(forma.getMapAlmacenes("numRegistros").toString())>1)
        {
            logger.warn("->Existe mas de un almacen en la lista");
            inicializarValoresRegistroNuevo(con,forma,usuario,true);
            UtilidadBD.closeConnection(con);
            return mapping.findForward("paginaAlmacenes");
        }
        
        return null;
    }
    /**
     * metodo para inicializar los valores,
     * cuando se ingresa un nuevo registro
     * @param forma
     * @param usuario
     */
    private void inicializarValoresRegistroNuevo(Connection con,SolicitudTrasladoAlmacenForm forma, UsuarioBasico usuario,boolean rest)
    {
        if(!rest)
        {
            forma.setCodAlmacen(Integer.parseInt(forma.getMapAlmacenes("codigo_0")+""));
            forma.setNombreAlmacen((forma.getMapAlmacenes("nombre_0")+""));
            forma.setMapaAtributos("clase",forma.getMapAlmacenes("clase_0"));
            forma.setMapaAtributos("grupo",forma.getMapAlmacenes("grupo_0"));
        }
        forma.setMapaAtributos("estadoTraslado",ConstantesBDInventarios.nombreEstadoTransaccionInventarioPendiente);
        forma.setMapaAtributos("almacenSolicita",usuario.getCentroCosto());
        forma.setMapaAtributos("usuarioElabora",usuario.getLoginUsuario());
        ValoresPorDefecto.cargarValoresIniciales(con);        
        forma.setMapaAtributos("esModificarFecha",ValoresPorDefecto.getModificacionFechaInventario(usuario.getCodigoInstitucionInt()));
        forma.setFechaElaboracion(UtilidadFecha.getFechaActual());
        forma.setHoraElaboracion(UtilidadFecha.getHoraActual());
    }
    /**
	 * metodo para redireccionar el pager, 
	 * cuando se ingresan nuevos registros
	 * @param con
	 * @param forma
	 * @param response
	 * @param request
	 * @return
	 * @author jarloc
	 */
	 public ActionForward redireccion (	Connection con,SolicitudTrasladoAlmacenForm forma,HttpServletResponse response,HttpServletRequest request)
	 {
		forma.setOffset(((int)((forma.getNumRegistrosArticulos()-1)/forma.getMaxPageItem()))*forma.getMaxPageItem());
		if(request.getParameter("ultimaPage")==null)
		{
			if(forma.getNumRegistrosArticulos() > (forma.getOffset()+forma.getMaxPageItem()))
			forma.setOffset(((int)(forma.getNumRegistrosArticulos()/forma.getMaxPageItem()))*forma.getMaxPageItem());
			try 
			{
			    UtilidadBD.closeConnection(con);
			    response.sendRedirect("solicitud.jsp"+"?pager.offset="+forma.getOffset());
			} catch (IOException e) 
			{
			    e.printStackTrace();
			}
		}
		else
		{    
		    String ultimaPagina=request.getParameter("ultimaPage");	
		    int posOffSet=ultimaPagina.indexOf("offset=")+7;	    
		    if(forma.getNumRegistrosArticulos()>(forma.getOffset()+forma.getMaxPageItem()))
		        forma.setOffset(forma.getOffset()+forma.getMaxPageItem());	
			try 
			{
			    UtilidadBD.closeConnection(con);
			    response.sendRedirect(ultimaPagina.substring(0,posOffSet)+forma.getOffset());
			} 
			catch (IOException e) 
			{			
			    e.printStackTrace();
			}
		}		
		return null;
	 }
	 /**
	     * accion para eliminar articulos
	     * @param con
	     * @param forma
	     * @param mapping
	     * @return
	     * @author jarloc
	     */
	    private void accionEliminarRegistros(Connection con,SolicitudTrasladoAlmacenForm forma) 
	    {
	        int posEli=Integer.parseInt(forma.getMapaAtributos("posArticuloEliminar")+"");
	        int nuevaPos=forma.getNumRegistrosArticulos()-1;
	        forma.setNumRegistrosArticulos(nuevaPos);	
	        if((forma.getMapaArticulos("tipoRegistro_"+nuevaPos)+"").equals("BD"))
	            forma.setRegEliminadosBD(forma.getMapaArticulos("codigoArticulo_"+posEli));
	        if(posEli!=nuevaPos)        
		        for(int j=posEli;j<forma.getNumRegistrosArticulos();j++)
		        {                                 
		            forma.setMapaArticulos(("codigoArticulo_"+j),forma.getMapaArticulos("codigoArticulo_"+(j+1)));
		            forma.setMapaArticulos(("descripcionArticulo_"+j),forma.getMapaArticulos("descripcionArticulo_"+(j+1)));
		            forma.setMapaArticulos(("unidadMedidaArticulo_"+j),forma.getMapaArticulos("unidadMedidaArticulo_"+(j+1)));
		            forma.setMapaArticulos(("cantidadArticulo_"+j),forma.getMapaArticulos("cantidadArticulo_"+(j+1)));		            		            	            
		            forma.setMapaArticulos(("tipoRegistro_"+j),forma.getMapaArticulos("tipoRegistro_"+(j+1)));
		        }	        
	        forma.getMapaArticulos().remove("codigoArticulo_"+nuevaPos);
	        forma.getMapaArticulos().remove("descripcionArticulo_"+nuevaPos);
	        forma.getMapaArticulos().remove("unidadMedidaArticulo_"+nuevaPos);
	        forma.getMapaArticulos().remove("cantidadArticulo_"+nuevaPos);	
	        forma.getMapaArticulos().remove("tipoRegistro_"+nuevaPos);	        
	        UtilidadBD.closeConnection(con);		   
	    }
	    /**
	     * metodo para ordenar los articulos
	     * @param con
	     * @param forma
	     * @param mapping
	     * @return
	     * @author jarloc
	     */	
		private void accionOrdenarMapa(SolicitudTrasladoAlmacenForm forma) 
	    {                       
	        forma.setPatronOrdenar("descripcionArticulo_");
		    String[] indices={
					            "codigoArticulo_",
					            "codigoInterfaz_",
					            "descripcionArticulo_", 
					            "unidadMedidaArticulo_", 
					            "cantidadArticulo_",
					            "tipoRegistro_"
		            		};
			forma.setMapaArticulos(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaArticulos(),forma.getNumRegistrosArticulos()));		
			forma.setUltimoPatron(forma.getPatronOrdenar());
	    }
		/**
	     * metodo para ordenar las solicitudes
	     * @param con
	     * @param forma
	     * @param mapping
	     * @return
	     * @author jarloc
	     */	
		private void accionOrdenarMapaSolicitudes(SolicitudTrasladoAlmacenForm forma) 
	    {         
		    Object temp=forma.getMapaSolicitudes("numRegistros");
		    String[] indices={
					            "numero_traslado_", 
					            "estado_", 
					            "nombre_estado_", 
					            "prioritario_",
					            "fecha_elaboracion_",	
					            "hora_elaboracion_",	
					            "usuario_elabora_",	
					            "almacen_solicita_",
					            "nombre_almacen_solicita_",
					            "almacen_despacha_",
					            "observaciones_"
		            		};
			forma.setMapaSolicitudes(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaSolicitudes(),Integer.parseInt(temp+"")));		
			forma.setUltimoPatron(forma.getPatronOrdenar());
			forma.setMapaSolicitudes("numRegistros",temp);
	    }
}
