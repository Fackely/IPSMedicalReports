package com.princetonsa.action.historiaClinica;

import java.io.IOException;
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
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.historiaClinica.ValoracionPacientesCuidadosEspecialesForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.ValoracionPacientesCuidadosEspeciales;

/**
 * Clase para el manejo de valoracion pacientes en cuidados especiales
 * Date: 2008-05-29
 * @author garias@princetonsa.com
 */
public class ValoracionPacientesCuidadosEspecialesAction extends Action 
{
	
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(ValoracionPacientesCuidadosEspecialesAction.class);
	
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
    		if(form instanceof ValoracionPacientesCuidadosEspecialesForm)
    		{
    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}
    			ValoracionPacientesCuidadosEspecialesForm forma = (ValoracionPacientesCuidadosEspecialesForm)form;
    			String estado = forma.getEstado();

    			ActionErrors errores = new ActionErrors();

    			logger.info("\n\n\n ESTADO (VALORACION PACIENTES CUIDADOS ESPECIALES) ---> "+estado+"\n\n");

    			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

    			if(estado == null)
    			{
    				forma.reset();
    				logger.warn("Estado No Valido Dentro Del Flujo de la Valoración Pacientes Cuidados Especiales (null)");
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
    					return accionEmpezar (con, request, errores, mapping, forma, usuario);
    				}
    				else 
    					/*------------------------------
    					 * 		ESTADO > consultar
    					 *-------------------------------*/
    					if(estado.equals("consultar"))
    					{   
    						return accionConsultar (con, request, errores, mapping, forma, usuario);
    					}
    			/*------------------------------
    			 * 		ESTADO > ordenar
			 -------------------------------*/
    			if (estado.equals("ordenar"))
    			{
    				return accionOrdenar (con, request, errores, mapping, forma, usuario);
    			}
    			/*------------------------------
    			 * 		ESTADO > ordenar
			 -------------------------------*/
    			if (estado.equals("ingresarValoracion"))
    			{
    				return accionIngresarValoracion (con, request, errores, mapping, forma, usuario, response);
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
     * Método implementado para ingresar una valoración de cuidado especial
     * @param con
     * @param request
     * @param errores
     * @param mapping
     * @param forma
     * @param usuario
     * @param response
     * @return
     */
	private ActionForward accionIngresarValoracion(Connection con, HttpServletRequest request, ActionErrors errores, ActionMapping mapping, ValoracionPacientesCuidadosEspecialesForm forma, UsuarioBasico usuario, HttpServletResponse response) 
	{
		PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
		paciente.setCodigoPersona(Integer.parseInt(forma.getValoracionPacientesCuidadosEspecialesMap("codigo_paciente_"+forma.getPosMap()).toString()));
		UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
		
		UtilidadBD.closeConnection(con);
		try
		{
			String path = "../valoracionesDummy/valoraciones.do?" +
				"estado=empezar" +
				"&cuidadoEspecial="+ConstantesBD.acronimoSi;			
			response.sendRedirect(path);
			return null;
		}
		catch(IOException e)
		{
			logger.error("Error al direccionar la valoracion del cuidado especial: "+e);
			errores.add("",new ActionMessage("error.errorEnBlanco","Error al tratar de abrir la valoracion de cuidado especial. Por favor reportar el error al admisnitrador del sistema"));
			saveErrors(request, errores);
			return mapping.findForward("paginaErroresActionErrors");
		}
		
	}

	private ActionForward accionOrdenar(Connection con, HttpServletRequest request, ActionErrors errores, ActionMapping mapping, ValoracionPacientesCuidadosEspecialesForm forma, UsuarioBasico usuario) {
		String[] indices = {"ingreso_", 
							"codigo_paciente_",
							"nombre_", 
							"identificacion_", 
							"edad_", 
							"sexo_", 
							"codarea_",
							"area_", 
							"cama_",
							"dxprincipal_",
							"fechahoraingreso_",
							"profesional_",
							"fechahoraorden_",
							"valoracionordena_",
							"evolucionordena_",
							"valoracion_",
							"valoracionordenatipo_",
							""};
		int numReg = Integer.parseInt(forma.getValoracionPacientesCuidadosEspecialesMap("numRegistros")+"");
		String tipoMonitoreo = forma.getValoracionPacientesCuidadosEspecialesMap("tipoMonitoreo").toString();
		forma.setValoracionPacientesCuidadosEspecialesMap(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getValoracionPacientesCuidadosEspecialesMap(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setValoracionPacientesCuidadosEspecialesMap("numRegistros",numReg+"");
		forma.setValoracionPacientesCuidadosEspecialesMap("tipoMonitoreo", tipoMonitoreo);
		forma.setValoracionPacientesCuidadosEspecialesMap("INDICES_MAPA",indices);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	private ActionForward accionConsultar(Connection con, HttpServletRequest request, ActionErrors errores, ActionMapping mapping, ValoracionPacientesCuidadosEspecialesForm forma, UsuarioBasico usuario) {
		ValoracionPacientesCuidadosEspeciales mundo = new ValoracionPacientesCuidadosEspeciales();
		if (!forma.getValoracionPacientesCuidadosEspecialesMap().get("tipoMonitoreo").equals(ConstantesBD.codigoNuncaValido)){
			mundo.setInstitucion(usuario.getCodigoInstitucion());
			mundo.setTipoMonitoreo(Integer.parseInt(forma.getValoracionPacientesCuidadosEspecialesMap().get("tipoMonitoreo").toString()));
			forma.setValoracionPacientesCuidadosEspecialesMap(mundo.consultar(con, mundo));
			forma.getValoracionPacientesCuidadosEspecialesMap().put("tipoMonitoreo", mundo.getTipoMonitoreo());
			forma.setCentrosCosto(usuario.getCentrosCosto());
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	private ActionForward accionEmpezar(Connection con, HttpServletRequest request, ActionErrors errores, ActionMapping mapping, ValoracionPacientesCuidadosEspecialesForm forma, UsuarioBasico usuario) {
		forma.reset();
		ValoracionPacientesCuidadosEspeciales mundo = new ValoracionPacientesCuidadosEspeciales();
		mundo.setInstitucion(usuario.getCodigoInstitucion());
		forma.setTiposMonitoreoMap(mundo.consultarTiposMonitoreo(con, mundo));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
}