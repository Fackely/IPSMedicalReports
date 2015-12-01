/**
 * 
 */
package com.princetonsa.action.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.Utilidades;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.historiaClinica.ConsultaIngresosHospitalDiaForm;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.ConsultaIngresosHospitalDia;

/**
 * @author axioma
 *
 */
public class ConsultaIngresosHospitalDiaAction extends Action 
{

	private Logger logger=Logger.getLogger(ConsultaIngresosHospitalDiaAction.class);
	
	/**
	 * Método execute del action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	{

		Connection con = null;
		try {	
			if (form instanceof ConsultaIngresosHospitalDiaForm)
			{
				con = UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

				if(usuario == null)
				{
					logger.warn("Profesional de la salud no válido (null)");			
					request.setAttribute("codigoDescripcionError", "errors.usuario.noCargado");
					return mapping.findForward("paginaError");
				}

				ConsultaIngresosHospitalDiaForm forma = (ConsultaIngresosHospitalDiaForm) form;
				ConsultaIngresosHospitalDia mundo=new ConsultaIngresosHospitalDia();
				String estado=forma.getEstado();

				logger.info("[ConsultaIngresosHospitalDiaAction]  --> " + estado);

				if(estado.equals("empezar"))
				{
					forma.reset();
					return accionEmpezar(forma,mundo, con, mapping, usuario);
				} 
				else if(estado.equals("porPaciente"))
				{
					if((paciente==null || paciente.getTipoIdentificacionPersona().equals("")))
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente no cargado", "errors.paciente.noCargado", true);
					}
					return accionPorPaciente(forma,mundo,con,mapping,usuario,paciente);
				}
				else if(estado.equals("porPeriodo"))
				{
					return accionPorPeriodo(forma,mundo,con,mapping,usuario,paciente);
				}
				else if(estado.equals("consultaPeriodo"))
				{
					return accionConsultaPorPeriodo(forma,mundo,con,mapping,usuario,paciente);
				}
				else if(estado.equals("cargarIngreso"))
				{
					return accionCargarDetalleIngreso(forma,mundo,con,mapping,usuario,paciente,request);
				}

				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de ConsultaIngresosHospitalDiaForm");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
			}
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
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	private ActionForward accionConsultaPorPeriodo(ConsultaIngresosHospitalDiaForm forma, ConsultaIngresosHospitalDia mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		HashMap<String, Object> vo=new HashMap<String, Object>();
		vo.put("fechaInicial", UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicialFiltro()));
		vo.put("fechaFinal",UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinalFiltro()));
		vo.put("centroAtencion",forma.getCentroAtencionfiltro());
		vo.put("usuario",forma.getUsuarioFiltro());
		forma.setIngresosHospitalDia(mundo.consultarIngresosHospitalDia(con,vo,false));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ingresosPorPeriodo");
	}

	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param request 
	 * @return
	 */
	private ActionForward accionCargarDetalleIngreso(ConsultaIngresosHospitalDiaForm forma, ConsultaIngresosHospitalDia mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) 
	{
		forma.setDetalleIngreso(mundo.consultaDetalleIngreso(con,Utilidades.convertirAEntero(forma.getIngresosHospitalDia("ingreso_"+forma.getIndiceSeleccionado())+""),Utilidades.convertirAEntero(forma.getIngresosHospitalDia("cuenta_"+forma.getIndiceSeleccionado())+"")));
		//********************SE CARGA EL PACIENTE*******************************************
		ObservableBD observable = (ObservableBD)servlet.getServletContext().getAttribute("observable");
		try 
		{
			paciente.clean();
			paciente.cargar(con, Utilidades.convertirAEntero(""+forma.getIngresosHospitalDia("codigopaciente_"+forma.getIndiceSeleccionado())));
			paciente.cargarPacienteXingreso(con, forma.getIngresosHospitalDia("ingreso_"+forma.getIndiceSeleccionado())+"", usuario.getCodigoInstitucion(), usuario.getCodigoCentroAtencion()+"");
		
		}
		catch (Exception e) 
		{
			logger.info("Error cargando el paciente.: "+e);
		}
		observable.addObserver(paciente);
		request.getSession().removeAttribute("pacienteActivo");
		request.getSession().setAttribute("pacienteActivo",paciente);
		UtilidadSesion.notificarCambiosObserver(paciente.getCodigoPersona(),servlet.getServletContext());
		//***********************************************************************************

		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleIngreso");
	}

	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	private ActionForward accionPorPeriodo(ConsultaIngresosHospitalDiaForm forma, ConsultaIngresosHospitalDia mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		UtilidadBD.closeConnection(con);
		return mapping.findForward("porPeriodo");
	}
	

	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	private ActionForward accionPorPaciente(ConsultaIngresosHospitalDiaForm forma, ConsultaIngresosHospitalDia mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		forma.setIngresosHospitalDia(mundo.consultarIngresosHospitalDia(con,paciente.getCodigoPersona()+"",true));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("porPaciente");
	}

	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(ConsultaIngresosHospitalDiaForm forma, ConsultaIngresosHospitalDia mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

}

