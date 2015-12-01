package com.princetonsa.action.historiaClinica;

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
import util.Listado;
import util.UtilidadBD;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.actionform.historiaClinica.EventosAdversosForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.EventosAdversos;

/**
 * Clase para el manejo de la parametrizacion 
 * de los eventos adversos
 * Date: 2008-05-12
 * @author garias@princetonsa.com
 */
public class EventosAdversosAction extends Action 
{
	
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(EventosAdversosAction.class);
	
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
    		if(response==null);
    		if(form instanceof EventosAdversosForm)
    		{

    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}
    			EventosAdversosForm forma = (EventosAdversosForm)form;
    			String estado = forma.getEstado();

    			ActionErrors errores = new ActionErrors();

    			logger.info("\n\n\n ESTADO (PARAMETRIZACIÓN MOTIVOS) ---> "+estado+"\n\n");

    			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

    			if(paciente==null)
    				errores.add("Paciente", new ActionMessage("errors.required","Paciente"));

    			if(estado == null)
    			{
    				forma.reset();
    				logger.warn("Estado No Valido Dentro Del Flujo de la Parametrización de los Eventos Adversos (null)");
    				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else 
    				/*------------------------------
    				 * 		ESTADO > empezar
    				 *-------------------------------*/
    				if(estado.equals("empezar"))
    				{   
    					forma.reset();
    					forma.setClasificacionEventosMap(UtilidadesHistoriaClinica.consultarClasificacionesEventos(con, usuario.getCodigoInstitucionInt()));
    					UtilidadBD.closeConnection(con);
    					return mapping.findForward("principal");	
    				}
    				else
    					/*------------------------------
    					 * 		ESTADO > ingresarModificar
    					 *-------------------------------*/
    					if(estado.equals("ingresarModificar"))
    					{   
    						accionConsultar(con, forma, usuario.getCodigoInstitucion());
    						UtilidadBD.closeConnection(con);
    						return mapping.findForward("principal");	
    					}
    			/*------------------------------
    			 * 		ESTADO > ingresar
    			 *-------------------------------*/
    			if(estado.equals("ingresar"))
    			{
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("principal");	
    			}
    			/*------------------------------
    			 * 		ESTADO > guardarIngreso
    			 *-------------------------------*/
    			if(estado.equals("guardarIngreso"))
    			{
    				errores = accionValidarDatos(forma, errores, Integer.parseInt(forma.getEventosMap().get("numRegistros").toString()));
    				if(errores.isEmpty())
    					errores = accionGuardarIngreso(con, forma, usuario.getCodigoInstitucion(), usuario.getLoginUsuario(), errores);

    				if(!errores.isEmpty()){
    					saveErrors(request,errores);
    					forma.setEstado("ingresar");
    				}	

    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("principal");	
    			}
    			else
    				/*------------------------------
    				 * 		ESTADO > consultar
    				 *-------------------------------*/
    				if(estado.equals("consultar"))
    				{   
    					accionConsultar(con, forma, usuario.getCodigoInstitucion());
    					UtilidadBD.closeConnection(con);
    					return mapping.findForward("principal");	
    				}
    			/*------------------------------
    			 * 		ESTADO > modificar
    			 *-------------------------------*/
    			if(estado.equals("modificar"))
    			{
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("principal");	
    			}
    			/*------------------------------
    			 * 		ESTADO > guardarCambios
    			 *-------------------------------*/
    			if(estado.equals("guardarCambios"))
    			{
    				errores = accionValidarDatos(forma, errores, forma.getPosMap());
    				if(errores.isEmpty())
    					errores = accionGuardarCambios(con, forma, usuario.getCodigoInstitucion(), usuario.getLoginUsuario(), errores);

    				if(!errores.isEmpty()){
    					saveErrors(request,errores);
    					forma.setEstado("modificar");
    				}	

    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("principal");	
    			}
    			/*------------------------------
    			 * 		ESTADO > eliminar
    			 *-------------------------------*/
    			if(estado.equals("eliminar"))
    			{
    				eliminar(con, forma, usuario.getLoginUsuario());
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("principal");	
    			}
    			/*------------------------------
    			 * 		ESTADO > ordenar
			 -------------------------------*/
    			if (estado.equals("ordenar"))
    			{
    				accionOrdenar(forma);
    				forma.setEstado(forma.getEstadoAux());
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("principal");
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


	private ActionErrors accionValidarDatos(EventosAdversosForm forma, ActionErrors errores, int pos) {
		if(forma.getEventosMap("codigo_"+pos).toString().equals(""))
    		errores.add("Validación codigo", new ActionMessage("errors.required","Código "));
		if(forma.getEventosMap("descripcion_"+pos).toString().equals(""))
    		errores.add("Validación Descripción", new ActionMessage("errors.required","Descripción "));
		if(forma.getEventosMap("tipo_"+pos).toString().equals(ConstantesBD.codigoNuncaValido+""))
    		errores.add("Validación Tipo de evento", new ActionMessage("errors.required","Tipo de evento "));
		if(forma.getEventosMap("clasificacion_evento_"+pos).toString().equals(ConstantesBD.codigoNuncaValido+""))
    		errores.add("Validación Clasificación", new ActionMessage("errors.required","Clasificación "));
		return errores;
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param codigoInstitucion
	 * @param usuario
	 * @param errores
	 * @return
	 */
	private ActionErrors accionGuardarIngreso(Connection con, EventosAdversosForm forma, String codigoInstitucion, String usuario, ActionErrors errores) {
    	int pos=Integer.parseInt((forma.getEventosMap().get("numRegistros").toString()));
    	EventosAdversos mundo = new EventosAdversos();
		mundo.setEventosMap("codigo",forma.getEventosMap("codigo_"+pos));
		mundo.setInstitucion(codigoInstitucion);
		mundo.setEventosMap("descripcion",forma.getEventosMap("descripcion_"+pos));
		mundo.setEventosMap("tipo",forma.getEventosMap("tipo_"+pos));
		mundo.setEventosMap("clasificacion",forma.getEventosMap("clasificacion_evento_"+pos));
		mundo.setUsuario(usuario);
		if(mundo.ingresar(con, mundo)){
			forma.setEventosMap(mundo.consultar(con, mundo));
			forma.setEstado("operacionExitosa");
		}
		else
			errores.add("Validación Codigo del Motivo", new ActionMessage("errors.yaExisteAmplio","El código del evento ",""));
		return errores;
    }	

	/**
     * 
     * @param con
     * @param forma
     */
	private void eliminar(Connection con, EventosAdversosForm forma, String usuario) {
		EventosAdversos mundo = new EventosAdversos();
		mundo.setEventosMap("codigopk",forma.getEventosMap("codigopk_"+forma.getPosMap()));
		mundo.setUsuario(usuario);
		if(mundo.inactivar(con, mundo)){
			forma.setEventosMap("eliminado_"+forma.getPosMap(), ConstantesBD.acronimoSi);
			forma.setEstado("operacionExitosa");
		}	
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param codigoInstitucion
	 * @param usuario
	 * @param errores 
	 */
	private ActionErrors accionGuardarCambios(Connection con, EventosAdversosForm forma, String codigoInstitucion, String usuario, ActionErrors errores) {

		EventosAdversos mundo = new EventosAdversos();
		mundo.setInstitucion(codigoInstitucion);
		mundo.setEventosMap("codigo",forma.getEventosMap("codigo_"+forma.getPosMap()));
		mundo.setEventosMap("descripcion",forma.getEventosMap("descripcion_"+forma.getPosMap()));
		mundo.setEventosMap("tipo",forma.getEventosMap("tipo_"+forma.getPosMap()));
		mundo.setUsuario(usuario);
		mundo.setEventosMap("codigopk",forma.getEventosMap("codigopk_"+forma.getPosMap()));
		mundo.setEventosMap("clasificacion",forma.getEventosMap("clasificacion_evento_"+forma.getPosMap()));
		if(mundo.modificar(con, mundo)){
			accionConsultar(con, forma, codigoInstitucion);
			forma.setEstado("consultarModificar");
			forma.setEstado("operacionExitosa");
		}
		else
			errores.add("Validación Codigo del Motivo", new ActionMessage("errors.yaExisteAmplio","El código del evento ",""));
		return errores;
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param codigoInstitucion
	 */
	private void accionConsultar(Connection con, EventosAdversosForm forma, String codigoInstitucion) {
		EventosAdversos mundo = new EventosAdversos();
		mundo.setInstitucion(codigoInstitucion);
		forma.setEventosMap(mundo.consultar(con, mundo));
	}
	
	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenar(EventosAdversosForm forma) {
		int numReg = Integer.parseInt(forma.getEventosMap("numRegistros")+"");
		String[] indicesEventosMap = {"codigopk_","codigo_","descripcion_","tipo_","desctipo_","clasificacion_evento_","descclasificacion_","sepuedeeliminar_","eliminado_",""};
		forma.setEventosMap(Listado.ordenarMapa(indicesEventosMap, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getEventosMap(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setEventosMap("numRegistros",numReg+"");
		forma.setEventosMap("INDICES_MAPA",indicesEventosMap);
		forma.setEstado(forma.getEstadoAux());
	}
}