package com.princetonsa.action.seccionesparametrizables;

import java.sql.Connection;
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

import com.princetonsa.actionform.seccionesparametrizables.MonitoreoHemodinamicaForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.parametrizacion.MonitoreoHemodinamica;
import com.princetonsa.mundo.salasCirugia.HojaAnestesia;

/**
 * 
 * @author wilson
 *
 */
public class MonitoreoHemodinamicaAction extends Action
{
	/**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(MonitoreoHemodinamicaAction.class);
    
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
    		if(form instanceof MonitoreoHemodinamicaForm)
    		{
    			con = UtilidadBD.abrirConexion();
    			if(con == null)
    			{
    				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}

    			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    			MonitoreoHemodinamicaForm forma =(MonitoreoHemodinamicaForm)form;
    			String estado=forma.getEstado();

    			logger.info("\n\n\n************************************************************");
    			logger.warn("El estado en MONITOREO HEMODINAMICA es------->"+estado);
    			logger.info("************************************************************\n\n\n");

    			if(estado == null)
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de MONITOREO HEMODINAMICA (null) ");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else if(estado.equals("empezarMonitoreo") || estado.equals("resumen"))
    			{
    				return this.accionEmpezar(forma, mapping, con, usuario, request);
    			}
    			else if(estado.equals("resumenCompleto"))
    			{
    				return this.accionCargarMonitoreoCompleto(forma, mapping, con, usuario, request);
    			}    		
    			else if(estado.equals("cargarMonitoreo"))
    			{
    				return this.accionCargarMonitoreo(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("nuevoMonitoreo"))
    			{
    				return this.accionNuevoMonitoreo(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("continuar"))
    			{
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("monitoreoHemodinamica");
    			}
    			else if(estado.equals("guardar"))
    			{
    				return this.accionGuardar(forma, mapping, con, usuario, request);
    			}
    			else
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de MONITOREO HEMODINAMICA");
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
     * @param usuario
     * @return
     */
	private ActionForward accionEmpezar(MonitoreoHemodinamicaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request) 
	{
		forma.reset();
		
		if(!forma.getEstado().equals("resumen"))
			inicializarParametrosRequest(forma,request);
		
		//@todo hacer este desarrollo
		forma.setMapaCamposOtrasSecciones(MonitoreoHemodinamica.cargarCamposOtrasSecciones(con, forma.getNumeroSolicitud(), usuario.getCodigoInstitucionInt()));
		
		if(!UtilidadTexto.isNumber(forma.getMapaCamposOtrasSecciones("peso")+"") || !UtilidadTexto.isNumber(forma.getMapaCamposOtrasSecciones("talla")+""))
		{
			if(!forma.getEsConsulta())
			{
				ActionErrors errores = new ActionErrors(); 
				errores.add("", new ActionMessage("error.monitoreo.pesoTalla"));
				saveErrors(request, errores);
	            UtilidadBD.closeConnection(con);
	            return mapping.findForward("monitoreoHemodinamica");
			}	
			else
			{
				forma.setEstaBD(false);
				forma.setMapa("numRegistros","0");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("monitoreoHemodinamica");
			}
								
		}
		
		forma.setMapaListado(MonitoreoHemodinamica.obtenerListadoMonitoreos(con, forma.getNumeroSolicitud()));
		if(Integer.parseInt(forma.getMapaListado("numRegistros")+"")>0)
		{
			UtilidadBD.closeConnection(con);
			return mapping.findForward("listadoMonitoreoHemodinamica");
		}	
		else
		{	
			forma.setEstaBD(false);
			forma.setMapa(MonitoreoHemodinamica.cargarMonitoreo(con, /*codigo_mon_hemo_hoja_anestesia_*/ ConstantesBD.codigoNuncaValido, forma.getCentroCosto(), usuario.getCodigoInstitucionInt()));
			UtilidadBD.closeConnection(con);
			return mapping.findForward("monitoreoHemodinamica");
		}	
	}
	
	
	/**
	 * carga todos los Monitoreos
	 * @param MonitoreoHemodinamicaForm forma
	 * @param ActionMapping mapping
	 * @param Connection con
	 * @param UsuarioBasico usuario
	 * @param HttpServletRequest request
	 * */
	private ActionForward accionCargarMonitoreoCompleto(MonitoreoHemodinamicaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request)
	{	
		int numRegistros = 0;
		
		//Se Carga el Mapa de listado
		accionEmpezar(forma, mapping, con, usuario, request);
	
		con = UtilidadBD.abrirConexion();
		
		numRegistros = Integer.parseInt(forma.getMapaListado("numRegistros").toString());
		
		Utilidades.imprimirMapa(forma.getMapaListado());
		
		if(numRegistros > 0)
		{
			for(int i = 0; i < numRegistros; i++)
			{
				forma.setCodigoMonHemoHojaAnestesia(Integer.parseInt(forma.getMapaListado("codigo_mon_hemo_hoja_anestesia_"+i)+""));
				forma.setMapaListado("mapaMonitoreo_"+i,MonitoreoHemodinamica.cargarMonitoreo(con, forma.getCodigoMonHemoHojaAnestesia(), forma.getCentroCosto(), usuario.getCodigoInstitucionInt()));		
			}
			
			forma.getMostrarDatosInfo().setNombre(ConstantesBD.acronimoSi);
		}
		else
		{
			logger.info("no exite informacion de monitoreo Hemodinamica");
			forma.getMostrarDatosInfo().setNombre(ConstantesBD.acronimoNo);
		}		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("resumenCompleto");
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCargarMonitoreo(MonitoreoHemodinamicaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.setEstaBD(true);
		forma.setFecha(forma.getMapaListado("fecha_"+forma.getIndex())+"");
		forma.setHora(forma.getMapaListado("hora_"+forma.getIndex())+"");
		forma.setCodigoMonHemoHojaAnestesia(Integer.parseInt(forma.getMapaListado("codigo_mon_hemo_hoja_anestesia_"+forma.getIndex())+""));
		forma.setMapa(MonitoreoHemodinamica.cargarMonitoreo(con, forma.getCodigoMonHemoHojaAnestesia(), forma.getCentroCosto(), usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("monitoreoHemodinamica");
	}

	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionNuevoMonitoreo(MonitoreoHemodinamicaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.setEstaBD(false);
		forma.setFecha(UtilidadFecha.getFechaActual());
		forma.setHora(UtilidadFecha.getHoraActual());
		forma.setMapa(MonitoreoHemodinamica.cargarMonitoreo(con, /*codigo_mon_hemo_hoja_anestesia_*/ ConstantesBD.codigoNuncaValido, forma.getCentroCosto(), usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("monitoreoHemodinamica");
	}
	
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardar(MonitoreoHemodinamicaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario, HttpServletRequest request) 
	{
		logger.info("\n ESTA EN BD-->"+forma.getEstaBD()+"\n");
		
		UtilidadBD.iniciarTransaccion(con);
		
		if(!forma.getEstaBD())
		{	
			int codigoMonitoreoHojaAnestesia= MonitoreoHemodinamica.insertarMonHemoHojaAnestesia(con, forma.getNumeroSolicitud(), forma.getFecha(), forma.getHora(), usuario.getLoginUsuario());
			
			if(codigoMonitoreoHojaAnestesia>0)
			{
				for(int w=0; w<Integer.parseInt(forma.getMapa("numRegistros").toString());w++)
				{
					MonitoreoHemodinamica.insertarDetMonHemoHojaAnestesia(con, codigoMonitoreoHojaAnestesia, Integer.parseInt(forma.getMapa("cod_mon_hemo_anes_inst_cc_"+w)+""), forma.getMapa("valor_"+w)+"");
				}	
			}
		}	
		else
		{	
			MonitoreoHemodinamica.modificarMonHemoHojaAnestesia(con, forma.getFecha(), forma.getHora(), usuario.getLoginUsuario(), forma.getCodigoMonHemoHojaAnestesia());
			
			for(int w=0; w<Integer.parseInt(forma.getMapa("numRegistros").toString());w++)
			{
				if(UtilidadTexto.getBoolean(forma.getMapa("estabd_"+w)+""))
					MonitoreoHemodinamica.modificarDetMonHemoHojaAnestesia(con, forma.getCodigoMonHemoHojaAnestesia(), Integer.parseInt(forma.getMapa("cod_mon_hemo_anes_inst_cc_"+w)+""), forma.getMapa("valor_"+w)+"");
				else
					MonitoreoHemodinamica.insertarDetMonHemoHojaAnestesia(con, forma.getCodigoMonHemoHojaAnestesia(), Integer.parseInt(forma.getMapa("cod_mon_hemo_anes_inst_cc_"+w)+""), forma.getMapa("valor_"+w)+"");
			}
		}
		
		//en este punto insertamos los anestesiologos
		HojaAnestesia hojaAnestesia= new HojaAnestesia();
		hojaAnestesia.insertarActualizarAnestesiologos(con, forma.getNumeroSolicitud(), usuario);
		
		UtilidadBD.finalizarTransaccion(con);
		
		forma.setEstado("resumen");
		
		return this.accionEmpezar(forma, mapping, con, usuario, request);
	}
	
	
	/**
	 * Inicializa los datos pasados por parametros a la funcionalidad
	 * @param MonitoreoHemodinamicaForm forma
	 * @param HttpServletRequest request
	 * */
	private void inicializarParametrosRequest(MonitoreoHemodinamicaForm forma,HttpServletRequest request)
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
