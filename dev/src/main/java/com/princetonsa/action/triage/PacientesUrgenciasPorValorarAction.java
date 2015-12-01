package com.princetonsa.action.triage;

import java.sql.Connection;
import java.sql.SQLException;

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

import util.ConstantesIntegridadDominio;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadSesion;
import util.UtilidadValidacion;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.triage.PacientesUrgenciasPorValorarForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.triage.PacientesUrgenciasPorValorar;

public class PacientesUrgenciasPorValorarAction extends Action
{
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 */
	private Logger logger= Logger.getLogger(PacientesUrgenciasPorValorarAction.class);

	/**
	 * Método excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try {
			if(form instanceof PacientesUrgenciasPorValorarForm)
			{
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
					return mapping.findForward("paginaError");
				}

				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				PersonaBasica paciente= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				PacientesUrgenciasPorValorarForm forma=(PacientesUrgenciasPorValorarForm)form;
				PacientesUrgenciasPorValorar mundo = new PacientesUrgenciasPorValorar();

				String estado = forma.getEstado();
				logger.warn("[PacientesUrgenciasPorValorarAction] estado->"+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de PacientesUrgenciasPorValorarAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}

				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(forma, mapping, usuario, con, mundo);
				}
				else if(estado.equals("ordenarColumna"))
				{
					return this.accionOrdenarColumna(con, forma, mapping);
				}
				else if(estado.equals("preingresar"))
				{
					UtilidadSesion.notificarCambiosObserver(Integer.parseInt(forma.getMapaPacientes("codigopaciente_"+forma.getPosicionMapa())+""), getServlet().getServletContext());

					/**para cargar el paciente que corresponda**/
					paciente.setCodigoPersona((Integer.parseInt(forma.getMapaPacientes("codigopaciente_"+forma.getPosicionMapa())+"")));
					paciente.cargar(con,(Integer.parseInt(forma.getMapaPacientes("codigopaciente_"+forma.getPosicionMapa())+"")));
					paciente.cargarPaciente(con, (Integer.parseInt(forma.getMapaPacientes("codigopaciente_"+forma.getPosicionMapa())+"")),usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
					this.setObservable(paciente);

					ActionErrors errores = new ActionErrors();
					if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
					{
						errores.add("errors.ingresoEstadoDiferenteAbierto", new ActionMessage("errors.ingresoEstadoDiferenteAbierto"));
					}
					if(UtilidadValidacion.esCuentaValida(con, paciente.getCodigoCuenta())<1)
					{
						errores.add("errors.paciente.cuentaNoValida", new ActionMessage("errors.paciente.cuentaNoValida"));
					}
					UtilidadBD.cerrarConexion(con);
					if(errores.isEmpty())
						response.sendRedirect("../valoracionesDummy/valoraciones.do?estado=empezar");
					else
					{
						saveErrors(request, errores);
						return mapping.findForward("paginaPrincipal");		
					}
				}
				else if (estado.equals("redireccion"))
				{
					return accionRedireccion(con,forma,response,mapping,request);
				}
				else
				{
					UtilidadBD.cerrarConexion(con);
				}
			}
			else
			{
				logger.error("El form no es compatible con el form ");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
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
	 * Método implementado para realizar la paginación del listado de paciente de urgencias x valorar
	 * @param con
	 * @param forma
	 * @param response
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, PacientesUrgenciasPorValorarForm forma, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) 
	{
		try
		{
			
		    UtilidadBD.cerrarConexion(con);
			response.sendRedirect(forma.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccion de PacientesUrgenciasPorValorarAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en PacientesUrgenciasPorValorarAction", "errors.problemasDatos", true);
		}
	}

	/**
	 * Action para empezar la funcionalidad con los datos requeridos
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @param con
	 * @param mundo
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar (PacientesUrgenciasPorValorarForm forma, ActionMapping mapping, UsuarioBasico usuario, Connection con, PacientesUrgenciasPorValorar mundo) throws SQLException
	{
		forma.reset();
		try
		{
			forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
		}
		catch(Exception e)
		{
			//por si de pronto genera error
			forma.setMaxPageItems(20);
		}
		forma.setMapaPacientes(mundo.consultarPacientesUrgPorValorar(con, usuario.getCodigoCentroAtencion(), usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");		
	}
	
	/**
	 * Action para ordenar por cualquiera de las columnas del mapa
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarColumna(Connection con, PacientesUrgenciasPorValorarForm forma, ActionMapping mapping) 
    {
        String[] indices = {
					            "consecutivotriage_",
					            "codigopaciente_",
					            "fechapk_",
					            "nombrepaciente_",
					            "tipoid_",
					            "numeroid_",
					            "fechatriage_",
					            "horatriage_",
					            "fechaadmision_",
					            "horaadmision_",
					            "codigoategoria_",
					            "calificaciontriage_",
					            "nombrecolor_",
					            "codigosala_",
					            "sala_",
					            "observaciones_"
	            		  };
        
        int tmp = Integer.parseInt(forma.getMapaPacientes("numRegistros")+"");
        forma.setMapaPacientes(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaPacientes(),Integer.parseInt(forma.getMapaPacientes("numRegistros")+"")));
        forma.setUltimoPatron(forma.getPatronOrdenar());
        forma.setMapaPacientes("numRegistros",tmp+"");
        UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");
    }
	
	/**
	* Método para hacer que el paciente
	* pueda ser visto por todos los usuario en la aplicacion
	* @param paciente
	*/
	private void setObservable(PersonaBasica paciente)
	{
		ObservableBD observable = (ObservableBD)getServlet().getServletContext().getAttribute("observable");
		if (observable != null) {
			synchronized (observable) {
				observable.setChanged();
				observable.notifyObservers(new Integer(paciente.getCodigoPersona()));
			}
		}
	}
	
	
}