package com.princetonsa.action.manejoPaciente;

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

import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.actionform.manejoPaciente.EncuestaCalidadAtencionForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.EncuestaCalidadAtencion;
import com.princetonsa.mundo.manejoPaciente.MotivosSatisfaccionInsatisfaccion;


/**
 * Clase para el manejo del workflow de EncuestaCalidadAtencionAction
 * Date: 2008-05-09
 * @author lgchavez@princetonsa.com
 */
public class EncuestaCalidadAtencionAction extends Action 
{
	
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(EncuestaCalidadAtencionAction.class);
	
	/**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{
    	Connection con = null;
    	try{
    		if(response==null);
    		if(form instanceof EncuestaCalidadAtencionForm)
    		{

    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}

    			EncuestaCalidadAtencionForm forma = (EncuestaCalidadAtencionForm)form;
    			String estado = forma.getEstado();

    			logger.info("\n\n ESTADO ENCUESTA CALIDAD ATENCION---->"+estado+"\n\n");
    			ActionErrors errores = new ActionErrors();
    			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

    			if(estado == null)
    			{
    				forma.reset();
    				logger.warn("Estado no valido dentro del Flujo de Unidad de Procedimiento (null)");
    				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else if(estado.equals("empezar"))
    			{    			
    				//Validaciones
    				errores = validarPacienteCargado(con, forma, usuario, paciente, request, errores);
    				if(!errores.isEmpty())
    				{
    					saveErrors(request,errores);	
    					UtilidadBD.closeConnection(con);
    					return mapping.findForward("error");	
    				}
    				else{
    					return this.accionEmpezar(forma, con, mapping, usuario, paciente);
    				}



    			}
    			else if (estado.equals("redireccion"))
    			{
    				UtilidadBD.closeConnection(con);
    				response.sendRedirect(forma.getLinkSiguiente());
    				return null;
    			}
    			else
    				if (estado.equals("ordenar"))
    				{
    					accionOrdenarMapa(forma);
    					UtilidadBD.closeConnection(con);
    					return mapping.findForward("empezar");
    				}
    				else
    					if(estado.equals("cargarEncuesta"))
    					{
    						return this.accionCargarEncuesta(forma, con, mapping, usuario, paciente);
    					}
    					else
    						if(estado.equals("mostrarCapa"))
    						{
    							return this.accionMostrarCapa(forma, con, mapping, usuario, paciente);
    						}
    						else
    							if(estado.equals("guardar"))
    							{
    								return this.accionGuardar(forma, con, mapping, usuario, paciente);
    							}    
    							else
    								if(estado.equals("modificar"))
    								{
    									return this.accionModificar(forma, con, mapping, usuario, paciente);
    								}
    								else
    									if (estado.equals("cargarMotivos"))
    									{
    										return mapping.findForward("encuesta");
    									}
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
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @param paciente
     * @return
     */
    private ActionForward accionModificar(EncuestaCalidadAtencionForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente) {
		forma.setEncuesta("ingreso",forma.getIngreso() );
    	forma.setEncuesta("usuario",usuario.getLoginUsuario() );
    	EncuestaCalidadAtencion eca=new EncuestaCalidadAtencion();
    	eca.modificarEncuestas(con,forma.getEncuesta());
    	return this.accionMostrarCapa(forma, con, mapping, usuario, paciente);
    }
    
    /**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @param paciente
     * @return
     */
    private ActionForward accionGuardar(EncuestaCalidadAtencionForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente) {
		forma.setEncuesta("ingreso",forma.getIngreso() );
    	forma.setEncuesta("usuario",usuario.getLoginUsuario() );
    	EncuestaCalidadAtencion eca=new EncuestaCalidadAtencion();
    	eca.guardarEncuestas(con,forma.getEncuesta());
		HashMap mapa=new HashMap();
		
    	return this.accionMostrarCapa(forma, con, mapping, usuario, paciente);
    	}






	private ActionForward accionMostrarCapa(EncuestaCalidadAtencionForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente) {
    	EncuestaCalidadAtencion eca=new EncuestaCalidadAtencion();
    	String area=forma.getEncuesta().get("area").toString();
    	String centroa=forma.getEncuesta().get("centroAtencion").toString();
    	forma.setEncuesta(eca.consultarEncuestas(con, forma.getIngreso(), forma.getEncuesta().get("area").toString()));
    	forma.setEncuesta("area", area);
    	forma.setEncuesta("centroAtencion", centroa);
    	logger.info("mapa encuesta>>>"+forma.getEncuesta());
    	
    	if(Integer.parseInt(forma.getEncuesta().get("numRegistros").toString())==0)
    	{
	    	forma.setEncuesta("fecha_0", UtilidadFecha.getFechaActual());
			forma.setEncuesta("hora_0", UtilidadFecha.getHoraActual());
			forma.setCalificacion("");
    	}
    	else
    		forma.setCalificacion(forma.getEncuesta().get("calificacion_0").toString());
    	
    	return mapping.findForward("encuesta");
	}



	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @param paciente
     * @return
     */
	private ActionForward accionCargarEncuesta(EncuestaCalidadAtencionForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente) {
		forma.reset1();
		forma.setCentrosCosto(Utilidades.obtenerCentrosCosto(usuario.getCodigoInstitucionInt(), "", false, Integer.parseInt(forma.getCentroAtencion()),false));
		forma.setEncuesta("centroAtencion", Utilidades.obtenerNombreCentroAtencion(con, Integer.parseInt(forma.getCentroAtencion()+"")));
		MotivosSatisfaccionInsatisfaccion mundo = new MotivosSatisfaccionInsatisfaccion();
		mundo.setInstitucion(usuario.getCodigoInstitucion());
		forma.setMotivos(mundo.consultar(con, mundo));
		EncuestaCalidadAtencion eca=new EncuestaCalidadAtencion();
		logger.info("mapa motivos >>>"+forma.getMotivos());
		logger.info("ingreso >>>"+forma.getIngreso());
		return mapping.findForward("encuesta");
	}


	/**
     * Función que valida si el paciente se encuentra cargado
     * @param con
     * @param forma
     * @param usuario
     * @param paciente
     * @param request
     * @param errores
     * @return
     */
    private ActionErrors validarPacienteCargado(Connection con, EncuestaCalidadAtencionForm forma, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, ActionErrors errores) {
    	if(paciente.getCodigoPersona()<=0)
			errores.add("Paciente Cargado", new ActionMessage("errors.required","Paciente Cargado"));
    	return errores;
	}
    
	/**
     * Función que valida que el usuario que ingresa sea un profesional de la salud
     * @param con
     * @param forma
     * @param usuario
     * @param request
     * @param errores
     * @return
     */
	private ActionErrors validarProfesionalDeLaSalud(Connection con, EncuestaCalidadAtencionForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionErrors errores) {
		if(!UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con, usuario, false))
			errores.add("Validación Ocupación Medico Especialista", new ActionMessage("errors.required","Validación Ocupación Medico Especialista"));
		return errores;
	}

    
    /**
	 * metodo encargado del ordenamiento del mapa ingresos
	 * @param forma
	 */

	public static void accionOrdenarMapa(EncuestaCalidadAtencionForm forma)
	{
		String [] indicesMap = {"centroatencion_",
				"noingreso_",
				"estadoing_",
				"fechaing_",
				"fechaegr_" ,
				"viaingresotipopac_",
				"codigo_centroatencion_",
				"codviaingresotipopac_"
				};

		
		int numReg = Integer.parseInt(forma.getIngresosMap().get("numRegistros").toString());
		forma.setIngresosMap(Listado.ordenarMapa(indicesMap, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getIngresosMap(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.getIngresosMap().put("numRegistros",numReg);
		forma.getIngresosMap().put("INDICES_MAPA",indicesMap);
	}
    
	/**
     * 
     * @param forma
     * @param con
     * @param mapping
	 * @param paciente 
     * @return
     */
	private ActionForward accionEmpezar(EncuestaCalidadAtencionForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		forma.reset();
		EncuestaCalidadAtencion eca=new EncuestaCalidadAtencion();
		eca.setPaciente(paciente.getCodigoPersona()+"");
		forma.setIngresosMap(eca.consultar(con, eca));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("empezar");
	}	
}
