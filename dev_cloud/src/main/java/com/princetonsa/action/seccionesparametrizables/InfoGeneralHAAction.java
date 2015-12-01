package com.princetonsa.action.seccionesparametrizables;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;

import com.princetonsa.actionform.seccionesparametrizables.InfoGeneralHAForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.ordenesmedicas.cirugias.HojaQuirurgica;
import com.princetonsa.mundo.parametrizacion.InfoGeneralHA;
import com.princetonsa.mundo.salasCirugia.HojaAnestesia;

/**
 * 
 * @author wilson
 *
 */
public class InfoGeneralHAAction extends Action 
{
	/**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(InfoGeneralHAAction.class);
    
    /**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{
    	Connection con=null;
    	try {
    		if (response==null); 
    		if(form instanceof InfoGeneralHAForm)
    		{
    			con = UtilidadBD.abrirConexion();
    			if(con == null)
    			{
    				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}

    			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    			boolean validacionCapitacion=false;
    			InfoGeneralHAForm forma =(InfoGeneralHAForm)form;
    			String estado=forma.getEstado();

    			logger.info("\n\n\n************************************************************");
    			logger.warn("El estado en INFO GENERAL HA es------->"+estado);
    			logger.info("************************************************************\n\n\n");

    			if(estado == null)
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de INFO GENERAL (null) ");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}

    			//*********************MONITOREO************************************************//
    			else if(estado.equals("empezarMonitoreo") || estado.equals("resumenMonitoreo"))
    			{
    				return this.accionEmpezarMonitoreo(forma,request, mapping, con, usuario);
    			}
    			else if(estado.equals("guardarMonitoreo"))
    			{
    				return this.accionGuardarMonitoreo(forma,request, mapping, con, usuario);
    			}
    			//*********************FIN MONITOREO************************************************//

    			//*********************PROTECCIONES************************************************//
    			else if(estado.equals("empezarProteccion") || estado.equals("resumenProteccion"))
    			{
    				return this.accionEmpezarProteccion(forma,request, mapping, con, usuario);
    			}
    			else if(estado.equals("guardarProteccion"))
    			{
    				return this.accionGuardarProteccion(forma, request, mapping, con, usuario);
    			}
    			//*********************FIN MONITOREO************************************************//

    			//*********************PESO TALLA************************************************//
    			else if(estado.equals("empezarPesoTalla") || estado.equals("resumenPesoTalla"))
    			{
    				return this.accionEmpezarPesoTalla(forma,request, mapping, con, usuario);
    			}
    			else if(estado.equals("guardarPesoTalla"))
    			{
    				return this.accionGuardarPesoTalla(forma,request, mapping, con, usuario, validacionCapitacion);
    			}
    			else if(estado.equals("continuarTallaPeso"))
    			{
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("pesoTalla");
    			}

    			//*********************FIN MONITOREO************************************************//

    			else
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de INFO GENERAL ");
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

    
    //***********************************************MONITOREOS*****************************************************************
    
 	/**
     * 
     * @param forma
     * @param mapping
     * @param con
     * @param usuario
     * @return
     */
	private ActionForward accionEmpezarMonitoreo(InfoGeneralHAForm forma, HttpServletRequest request, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.reset();
		if(!forma.getEstado().contains("resumen"))
			inicializarParametrosRequest(forma,request);
		forma.setMapa(InfoGeneralHA.cargarMonitoreos(con, forma.getNumeroSolicitud(), forma.getCentroCosto(), usuario.getCodigoInstitucionInt()));		
		validacionMostrarInfo(forma);		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("monitoreo");
	}
	
	
	/**
	 * Validacione para actualizar el indicador de mostrarInformacion
	 * @param InfoGeneralHAForm forma 
	 * */
	private void validacionMostrarInfo(InfoGeneralHAForm forma)
	{
		//Utilidades.imprimirMapa(forma.getMapa());
		
		//Validacion para mostrar informacion 
		if((forma.getMapa().isEmpty() || (forma.getMapa().containsKey("numRegistros") && forma.getMapa().get("numRegistros").toString().equals("0")))  
				&& forma.getMostrarDatosInfoActivo())
		{
			logger.info("no hay informaci�n monitoreo,proteccion ");
			forma.getMostrarDatosInfo().setNombre(ConstantesBD.acronimoNo);
		}
		else
		{
			for(int i = 0; i < Integer.parseInt(forma.getMapa("numRegistros").toString()); i++)
			{
				if(forma.getMapa("checkeado_"+i).toString().equals(ConstantesBD.acronimoSi))
				{
					logger.info("no paso validaciones mostrar informacion. mostrar datos info  >>"+forma.getMostrarDatosInfoActivo()+" >> "+forma.getMapa().get("numRegistros").toString());
					forma.getMostrarDatosInfo().setNombre(ConstantesBD.acronimoSi);
					return;
				}			
			}	
			
			logger.info("no hay informaci�n monitoreo,proteccion ");
			forma.getMostrarDatosInfo().setNombre(ConstantesBD.acronimoNo);
		}		
	}
    
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarMonitoreo(InfoGeneralHAForm forma,HttpServletRequest request, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		UtilidadBD.iniciarTransaccion(con);
		for(int w=0; w<Integer.parseInt(forma.getMapa("numRegistros")+""); w++)
		{
			//primero el eliminar
			if(UtilidadTexto.getBoolean(forma.getMapa("estabd_"+w)+""))
			{
				if(!UtilidadTexto.getBoolean(forma.getMapa("checkeado_"+w)+""))
					InfoGeneralHA.eliminarMonitoreo(con, Integer.parseInt(forma.getMapa("codigomonitoreohojaanestesia_"+w)+""));
			}
			else
			{
				if(UtilidadTexto.getBoolean(forma.getMapa("checkeado_"+w)+""))
				{	
					InfoGeneralHA.insertarMonitoreos(con, llenarMapaInsercionMonitoreo(forma,w, usuario));
				}	
			}
		}
		
		//en este punto insertamos los anestesiologos
		HojaAnestesia hojaAnestesia= new HojaAnestesia();
		hojaAnestesia.insertarActualizarAnestesiologos(con, forma.getNumeroSolicitud(), usuario);
		
		UtilidadBD.finalizarTransaccion(con);
		
		forma.setEstado("resumenMonitoreo");
		return this.accionEmpezarMonitoreo(forma, request,mapping, con, usuario);
	}

	/**
	 * 
	 * @param forma
	 * @return
	 */
	private HashMap<Object, Object> llenarMapaInsercionMonitoreo(InfoGeneralHAForm forma, int index, UsuarioBasico usuario) 
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		mapa.put("numerosolicitud", forma.getNumeroSolicitud());
		mapa.put("codigomonitoreoinstcc", forma.getMapa("codigo_monitoreo_inst_cc_"+index));
		mapa.put("codigomonitoreo", forma.getMapa("codigo_monitoreo_"+index));
		mapa.put("loginusuario", usuario.getLoginUsuario());
		return mapa;
	}
    
	//*************************************************FIN MONITOREOS****************************************************************
	
	//*************************************************PROTECCIONES*******************************************************************
	
	/**
     * 
     * @param forma
     * @param mapping
     * @param con
     * @param usuario
     * @return
     */
	private ActionForward accionEmpezarProteccion(InfoGeneralHAForm forma, HttpServletRequest request, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{	
		forma.reset();
		if(!forma.getEstado().contains("resumen"))
			inicializarParametrosRequest(forma,request);
		forma.setMapa(InfoGeneralHA.cargarProtecciones(con, forma.getNumeroSolicitud(), forma.getCentroCosto(), usuario.getCodigoInstitucionInt()));		
		validacionMostrarInfo(forma);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("proteccion");
	}
    
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarProteccion(InfoGeneralHAForm forma, HttpServletRequest request, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		UtilidadBD.iniciarTransaccion(con);
		for(int w=0; w<Integer.parseInt(forma.getMapa("numRegistros")+""); w++)
		{
			//primero el eliminar
			if(UtilidadTexto.getBoolean(forma.getMapa("estabd_"+w)+""))
			{
				if(!UtilidadTexto.getBoolean(forma.getMapa("checkeado_"+w)+""))
					InfoGeneralHA.eliminarProteccion(con, Integer.parseInt(forma.getMapa("codigoproteccionhojaanestesia_"+w)+""));
			}
			else
			{
				if(UtilidadTexto.getBoolean(forma.getMapa("checkeado_"+w)+""))
				{	
					InfoGeneralHA.insertarProtecciones(con, llenarMapaInsercionProteccion(forma,w, usuario));
				}	
			}
		}
		
		//en este punto insertamos los anestesiologos
		HojaAnestesia hojaAnestesia= new HojaAnestesia();
		hojaAnestesia.insertarActualizarAnestesiologos(con, forma.getNumeroSolicitud(), usuario);
		
		UtilidadBD.finalizarTransaccion(con);
		
		forma.setEstado("resumenProteccion");
		return this.accionEmpezarProteccion(forma, request, mapping, con, usuario);
	}

	/**
	 * 
	 * @param forma
	 * @return
	 */
	private HashMap<Object, Object> llenarMapaInsercionProteccion(InfoGeneralHAForm forma, int index, UsuarioBasico usuario) 
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		mapa.put("numerosolicitud", forma.getNumeroSolicitud());
		mapa.put("codigoproteccioninstcc", forma.getMapa("codigo_proteccion_inst_cc_"+index));
		mapa.put("codigoproteccion", forma.getMapa("codigo_proteccion_"+index));
		mapa.put("loginusuario", usuario.getLoginUsuario());
		return mapa;
	}
	
	//******************************************FIN PROTECCIONES*********************************************************************
	
	//******************************************PESO Y TALLA*************************************************************************
	
	/**
	 * 
	 */
	private ActionForward accionEmpezarPesoTalla(InfoGeneralHAForm forma,HttpServletRequest request, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.reset();
		if(!forma.getEstado().contains("resumen"))
			inicializarParametrosRequest(forma,request);
		Vector<String> vector= InfoGeneralHA.cargarPesoTalla(con, forma.getNumeroSolicitud(), usuario.getCodigoInstitucionInt());
		if(vector.size()>0)
		{
			forma.setMapa("peso", vector.get(0));
			forma.setMapa("talla", vector.get(1));
		}
		else
		{
			//postular los de la valoracion preanestesia
			forma.setMapa("peso", "");
			forma.setMapa("talla", "");
		}
		
		if(forma.getMapa("peso").toString().equals("") &&
				forma.getMapa("talla").toString().equals(""))
			forma.getMostrarDatosInfo().setNombre(ConstantesBD.acronimoNo);
		else
			forma.getMostrarDatosInfo().setNombre(ConstantesBD.acronimoSi);
			
		UtilidadBD.closeConnection(con);
		return mapping.findForward("pesoTalla");
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarPesoTalla(InfoGeneralHAForm forma,HttpServletRequest request, ActionMapping mapping, Connection con, UsuarioBasico usuario, boolean validacionCapitacion) 
	{
		UtilidadBD.iniciarTransaccion(con);
		
		///lo primero es verificar si existe insercion en la hoja de anestesia
		
		if(!UtilidadTexto.getBoolean(HojaQuirurgica.existeHojaAnestesia(con, forma.getNumeroSolicitud()+"")))
		{	
			HojaAnestesia mundo= new HojaAnestesia();
			mundo.crearHojaAnestesiaBasica(
					 con,
					 ConstantesBD.acronimoNo,
					 forma.getNumeroSolicitud()+"",
					 usuario.getCodigoInstitucionInt(),
					 "",
					 usuario.getCodigoPersona()+"",
					 validacionCapitacion
					 );
		}			 
		
		InfoGeneralHA.modificarPesoTalla(con, Float.parseFloat(forma.getMapa("peso")+"") , Float.parseFloat(forma.getMapa("talla")+""), forma.getNumeroSolicitud());
		//en este punto insertamos los anestesiologos
		HojaAnestesia hojaAnestesia= new HojaAnestesia();
		hojaAnestesia.insertarActualizarAnestesiologos(con, forma.getNumeroSolicitud(), usuario);
		UtilidadBD.finalizarTransaccion(con);
		forma.setEstado("resumenPesoTalla");
		return this.accionEmpezarPesoTalla(forma,request, mapping, con, usuario);
	}

	//******************************************FIN PESO Y TALLA*************************************************************************
	
	/**
	 * Inicializa los datos pasados por parametros a la funcionalidad
	 * @param InfoGeneralHAForm forma
	 * @param HttpServletRequest request
	 * */
	private void inicializarParametrosRequest(InfoGeneralHAForm forma,HttpServletRequest request)
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