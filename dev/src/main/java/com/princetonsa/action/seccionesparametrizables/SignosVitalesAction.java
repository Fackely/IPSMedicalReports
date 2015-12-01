package com.princetonsa.action.seccionesparametrizables;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.seccionesparametrizables.SignosVitalesForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.ordenesmedicas.cirugias.SolicitudesCx;
import com.princetonsa.mundo.parametrizacion.SignosVitales;
import com.princetonsa.mundo.salasCirugia.HojaAnestesia;

/**
 * 
 * @author wilson
 *
 */
public class SignosVitalesAction extends Action 
{
	/**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(SignosVitalesAction.class);
    
    /**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{
    	Connection con = null;
    	try {
    		if (response==null); 
    		if(form instanceof SignosVitalesForm)
    		{
    			con = UtilidadBD.abrirConexion();
    			if(con == null)
    			{
    				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}

    			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    			SignosVitalesForm forma =(SignosVitalesForm)form;
    			String estado=forma.getEstado();
    			logger.info("\n\n\n************************************************************");
    			logger.warn("El estado en SIGNOS VITALES es------->"+estado);
    			logger.info("************************************************************\n\n\n");

    			if(estado == null)
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de Signos Vitales (null) ");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else if(estado.equals("empezarSignosVitalesAnestesia"))
    			{
    				return this.accionEmpezarSignosVitalesAnestesia(forma,request,  mapping, con, usuario);
    			}
    			else if(estado.equals("nuevoSignoVitalAnestesia"))
    			{
    				return this.accionNuevoSignoVitalAnestesia(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("modificarSignoVitalAnestesia"))
    			{
    				return this.accionModificarSignoVitalAnestesia(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("guardarNuevoSignoVitalAnestesia"))
    			{
    				return this.accionGuardarNuevoSignoVitalAnestesia(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("guardarModificarSignoVitalAnestesia"))
    			{
    				return this.accionGuardarModificarSignoVitalAnestesia(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("eliminarSignoVitalAnestesia"))
    			{
    				return this.accionEliminarSignoVitalAnestesia(forma, mapping, con, usuario);
    			}
    			else
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de Signos Vitales");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    		}
    		return null;
    	} catch (Exception e) {
    		Log4JManager.error(e);
    		return null;
    	}
    	finally{
    		UtilidadBD.closeConnection(con);
    	}
    }

    /**
     * 
     * @param forma
     * @param mapping
     * @param con
     * @return
     */
	private ActionForward accionEmpezarSignosVitalesAnestesia(SignosVitalesForm forma,HttpServletRequest request, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.reset();
		
		if(request != null && !forma.getEstado().equals("resumen"))
			inicializarParametrosRequest(forma,request);
		
		forma.setMapaSignosVitales(SignosVitales.obtenerSignosVitalesHojaAnestesia(con, forma.getNumeroSolicitud(), forma.getCentroCosto(), usuario.getCodigoInstitucionInt(), "" /*graficar*/));
		validacionMostrarInfo(forma,forma.getMapaCamposSignosVitales());
		
		logger.info("\n**********************************************************************************************************");
		logger.info("MAPA SIGNOS VITALES TITULO-->"+forma.getMapaTitulosSignosVitales());
		logger.info("\n**********************************************************************************************************\n");
		
		SolicitudesCx solicitudCx = new SolicitudesCx();		
		solicitudCx.cargarEncabezadoSolicitudCx(forma.getNumeroSolicitud()+"");
		
		if(	UtilidadTexto.isEmpty(solicitudCx.getFechaIngresoSala()) 
			||  UtilidadTexto.isEmpty(solicitudCx.getHoraIngresoSala()))
		{	
			ActionErrors errores = new ActionErrors(); 
			errores.add("", new ActionMessage("errors.required", "La fecha/hora ingreso sala"));
        	saveErrors(request, errores);
        	forma.setExisteFechaHoraIngresoSala(false);
     	}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("signosVitalesAnestesia");
	}
    
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionNuevoSignoVitalAnestesia(SignosVitalesForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		llenarMapaNuevo(forma, usuario);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("signosVitalesAnestesia");
	}
	
	/**
	 * despues de llenar los campos de signos vitales dinamocamente, 
	 * entonces se debe cargar el mapa para insertar tambien dinamicamente
	 * @param forma
	 */
	private void llenarMapaNuevo(SignosVitalesForm forma, UsuarioBasico usuario) 
	{
		//primero setiamos lo fijo, que son los campos fecha, hora, observaciones, graficar, estabd 
		forma.setMapaSignoVitalActualizado("fecha", UtilidadFecha.getFechaActual());
		forma.setMapaSignoVitalActualizado("hora", UtilidadFecha.getHoraActual());
		forma.setMapaSignoVitalActualizado("observaciones", "");
		forma.setMapaSignoVitalActualizado("graficar", ConstantesBD.acronimoSi);
		forma.setMapaSignoVitalActualizado("estabd", ConstantesBD.acronimoNo);
		forma.setMapaSignoVitalActualizado("numero_solicitud", forma.getNumeroSolicitud());
		forma.setMapaSignoVitalActualizado("login_usuario", usuario.getLoginUsuario());
		
		forma.setMapaSignoVitalActualizado("numSignosVitales", forma.getMapaTitulosSignosVitales("numRegistros"));
	 	for(int w=0; w<Utilidades.convertirAEntero(forma.getMapaTitulosSignosVitales("numRegistros")+""); w++)
	 	{
	 		forma.setMapaSignoVitalActualizado("codigo_sv_anest_inst_"+w, forma.getMapaTitulosSignosVitales("codigo_sv_anest_inst_"+w));
	 		forma.setMapaSignoVitalActualizado("signo_vital_"+w, forma.getMapaTitulosSignosVitales("signo_vital_"+w)+"");
	 		forma.setMapaSignoVitalActualizado("valor_maximo_"+w, forma.getMapaTitulosSignosVitales("valor_maximo_"+w)+"");
	 		forma.setMapaSignoVitalActualizado("valor_minimo_"+w, forma.getMapaTitulosSignosVitales("valor_minimo_"+w)+"");
	 		forma.setMapaSignoVitalActualizado("es_requerido_"+w, forma.getMapaTitulosSignosVitales("es_requerido_"+w)+"");
	 		forma.setMapaSignoVitalActualizado("nombre_"+w, forma.getMapaTitulosSignosVitales("nombre_"+w)+"");
	 		forma.setMapaSignoVitalActualizado("valor_"+w, "");
	 		forma.setMapaSignoVitalActualizado("formula_"+w, forma.getMapaTitulosSignosVitales("formula_"+w)+"");
	 	}
	 	
	 	logger.info("\n**********************************************************************************************************");
		logger.info("MAPA NUEVO SIGNO VITAL-->"+forma.getMapaSignoVitalActualizado());
		logger.info("\n**********************************************************************************************************\n");
	 	
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionModificarSignoVitalAnestesia(SignosVitalesForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		llenarMapaModificar(forma, usuario);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("signosVitalesAnestesia");
	}
	
	/**
	 * despues de llenar los campos de signos vitales dinamocamente, 
	 * entonces se debe cargar el mapa para insertar tambien dinamicamente
	 * @param forma
	 */
	private void llenarMapaModificar(SignosVitalesForm forma, UsuarioBasico usuario) 
	{
		//primero setiamos lo fijo, que son los campos fecha, hora, observaciones, graficar, estabd 
		forma.setMapaSignoVitalActualizado("fecha", forma.getMapaCamposSignosVitales("fecha_"+forma.getIndex()));
		forma.setMapaSignoVitalActualizado("hora", forma.getMapaCamposSignosVitales("hora_"+forma.getIndex()));
		forma.setMapaSignoVitalActualizado("observaciones", forma.getMapaCamposSignosVitales("observaciones_"+forma.getIndex()));
		forma.setMapaSignoVitalActualizado("graficar", forma.getMapaCamposSignosVitales("graficar_"+forma.getIndex()));
		forma.setMapaSignoVitalActualizado("estabd", ConstantesBD.acronimoSi);
		forma.setMapaSignoVitalActualizado("numero_solicitud", forma.getNumeroSolicitud());
		forma.setMapaSignoVitalActualizado("login_usuario", usuario.getLoginUsuario());
		//key para poder modificar
		forma.setMapaSignoVitalActualizado("codigo_tiempo", forma.getMapaCamposSignosVitales("codigo_tiempo_"+forma.getIndex()));
		
		
		forma.setMapaSignoVitalActualizado("numSignosVitales", forma.getMapaTitulosSignosVitales("numRegistros"));
	 	for(int w=0; w<Utilidades.convertirAEntero(forma.getMapaTitulosSignosVitales("numRegistros")+""); w++)
	 	{
	 		forma.setMapaSignoVitalActualizado("codigo_sv_anest_inst_"+w, forma.getMapaTitulosSignosVitales("codigo_sv_anest_inst_"+w));
	 		forma.setMapaSignoVitalActualizado("signo_vital_"+w, forma.getMapaTitulosSignosVitales("signo_vital_"+w)+"");
	 		forma.setMapaSignoVitalActualizado("valor_maximo_"+w, forma.getMapaTitulosSignosVitales("valor_maximo_"+w)+"");
	 		forma.setMapaSignoVitalActualizado("valor_minimo_"+w, forma.getMapaTitulosSignosVitales("valor_minimo_"+w)+"");
	 		forma.setMapaSignoVitalActualizado("es_requerido_"+w, forma.getMapaTitulosSignosVitales("es_requerido_"+w)+"");
	 		forma.setMapaSignoVitalActualizado("nombre_"+w, forma.getMapaTitulosSignosVitales("nombre_"+w)+"");
	 		
	 		if(UtilidadTexto.isEmpty(forma.getMapaTitulosSignosVitales("formula_"+w).toString()))
	 			forma.setMapaSignoVitalActualizado("valor_"+w, forma.getMapaCamposSignosVitales("valor_"+forma.getMapaTitulosSignosVitales("signo_vital_"+w)+"_"+forma.getIndex()));
	 		else
	 			forma.setMapaSignoVitalActualizado("valor_"+w,ConstantesBD.codigoNuncaValido+"");
	 		forma.setMapaSignoVitalActualizado("formula_"+w, forma.getMapaTitulosSignosVitales("formula_"+w)+"");
	 	}
	 	
	 	logger.info("\n**********************************************************************************************************");
		logger.info("MAPA SIGNO VITAL A ACTUALIZAR-->"+forma.getMapaSignoVitalActualizado());
		logger.info("\n**********************************************************************************************************\n");
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarNuevoSignoVitalAnestesia(SignosVitalesForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		UtilidadBD.iniciarTransaccion(con);
		SignosVitales.insertarTiempoSignoVitalAnestesia(con, forma.getMapaSignoVitalActualizado());
		//en este punto insertamos los anestesiologos
		HojaAnestesia hojaAnestesia= new HojaAnestesia();
		hojaAnestesia.insertarActualizarAnestesiologos(con, forma.getNumeroSolicitud(), usuario);
		UtilidadBD.finalizarTransaccion(con);
		
		forma.setEstado("resumen");
		return this.accionEmpezarSignosVitalesAnestesia(forma,null, mapping, con, usuario);
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarModificarSignoVitalAnestesia(SignosVitalesForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		UtilidadBD.iniciarTransaccion(con);
		SignosVitales.modificarTiempoSignoVitalAnestesia(con, forma.getMapaSignoVitalActualizado());
		//en este punto insertamos los anestesiologos
		HojaAnestesia hojaAnestesia= new HojaAnestesia();
		hojaAnestesia.insertarActualizarAnestesiologos(con, forma.getNumeroSolicitud(), usuario);
		UtilidadBD.finalizarTransaccion(con);
		forma.setEstado("resumen");
		return this.accionEmpezarSignosVitalesAnestesia(forma,null, mapping, con, usuario);
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminarSignoVitalAnestesia(SignosVitalesForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		UtilidadBD.iniciarTransaccion(con);
		SignosVitales.eliminarTiempoSignoVitalAnestesia(con, Integer.parseInt(forma.getMapaCamposSignosVitales("codigo_tiempo_"+forma.getIndex())+""));
		//en este punto insertamos los anestesiologos
		HojaAnestesia hojaAnestesia= new HojaAnestesia();
		hojaAnestesia.insertarActualizarAnestesiologos(con, forma.getNumeroSolicitud(), usuario);
		UtilidadBD.finalizarTransaccion(con);
		forma.setEstado("resumen");
		return this.accionEmpezarSignosVitalesAnestesia(forma,null, mapping, con, usuario);
	}
	
	
	/**
	 * Validacione para actualizar el indicador de mostrarInformacion
	 * @param InfoGeneralHAForm forma 
	 * */
	private void validacionMostrarInfo(SignosVitalesForm forma,HashMap mapa)
	{
		//Utilidades.imprimirMapa(forma.getMapa());
		
		//Validacion para mostrar informacion 
		if((mapa.isEmpty() || (mapa.containsKey("numRegistros") && mapa.get("numRegistros").toString().equals("0")))  
				&& forma.getMostrarDatosInfoActivo())
		{
			logger.info("no hay información Signos Vitales");
			forma.getMostrarDatosInfo().setNombre(ConstantesBD.acronimoNo);
		}
		else
		{
			logger.info("no paso validaciones mostrar informacion. Signos Vitales. mostrar datos info  >>"+forma.getMostrarDatosInfoActivo());
			forma.getMostrarDatosInfo().setNombre(ConstantesBD.acronimoSi);
			return;	
		}		
	}
	
	/**
	 * Inicializa los datos pasados por parametros a la funcionalidad
	 * @param SignosVitalesForm forma
	 * @param HttpServletRequest request
	 * */
	private void inicializarParametrosRequest(SignosVitalesForm forma,HttpServletRequest request)
	{
		if (!(request.getParameter("esSinEncabezado")+"").equals("") && !(request.getParameter("esSinEncabezado")+"").equals("null"))
			forma.setEsSinEncabezado(request.getParameter("esSinEncabezado")+"");
		else
			forma.setEsSinEncabezado(ConstantesBD.acronimoNo);		
		
		if (!(request.getParameter("mostrarDatosInfoActivo")+"").equals("") && !(request.getParameter("mostrarDatosInfoActivo")+"").equals("null"))
			forma.setMostrarDatosInfoActivo(UtilidadTexto.getBoolean(request.getParameter("mostrarDatosInfoActivo")+""));
		else
			forma.setMostrarDatosInfoActivo(false);		
		
		if (!(request.getParameter("ocultarMenu")+"").equals("") && !(request.getParameter("ocultarMenu")+"").equals("null"))
			forma.setOcultarMenu(UtilidadTexto.getBoolean(request.getParameter("ocultarMenu")+""));
		else
			forma.setOcultarMenu(false);	
	}	
}