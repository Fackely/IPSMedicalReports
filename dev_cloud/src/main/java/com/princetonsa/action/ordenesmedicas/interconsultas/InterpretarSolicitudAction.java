/*
 * @(#)SolicitarAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.action.ordenesmedicas.interconsultas;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadValidacion;
import util.UtilidadCadena;

import com.princetonsa.actionform.ordenesmedicas.interconsultas.SolicitarForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.mundo.solicitudes.ValidacionesSolicitud;

/**
 * Esta clase encapsula el flujo relacionado con una Solicitud
 * General, antes de pasar a la opción específica
 *
 * @version 1.0, Feb 10, 2004
 */
public class InterpretarSolicitudAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private Logger logger = Logger.getLogger(InterpretarSolicitudAction.class);
	/**
	 * Usuario que invoca la accion
	 */
	private UsuarioBasico usuario;
	/**
	 * Paciente cargado por el usuario
	 */
	private PersonaBasica paciente;
	/**
	 * estado del Action
	 */
	private String estado;
	/**
	 * estado del formulario, modificable o no
	 */
	//private boolean modificar=false;
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
															ActionForm form,
															HttpServletRequest request,
															HttpServletResponse response ) throws Exception
															{
		Connection con=null;
		try {
			if (response==null); //Para evitar que salga el warning
			if(form instanceof SolicitarForm)
			{
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				this.usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				this.paciente=(PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				SolicitarForm solicitarForm =(SolicitarForm)form;
				this.estado=solicitarForm.getEstado();
				logger.warn("[InterpretarSolicitudesAction] estado=>"+estado);
				ActionForward validacionesGenerales = this.validacionesUsuario(mapping, request, paciente, usuario);
				if (validacionesGenerales != null)
				{
					this.cerrarConexion(con);
					return validacionesGenerales ;
				}

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de Solicitar (null) ");

					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{	

					return this.accionEmpezar(mapping, con, solicitarForm);

				}
				else if (estado.equals("interpretar"))
				{ 
					return this.accionInterpretar( mapping,solicitarForm, con, request);
				}
				else if (estado.equals("cancelar"))
				{
					return this.accionCancelar(request, mapping, con);
				}
				else
				{
					logger.warn("Estado no valido dentro del flujo de interpretacion (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.warn("Tipo de forma invalida, (Se esperaba InterpretarForm)");

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
	 * Este método especifica las acciones a realizar en el estado
	 * empezar, se ayuda de métodos auxiliares para cada tipo
	 * de Servicio
	 * 
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @param solicitarForm Form para pre llenar datos, si es
	 * necesario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar(ActionMapping mapping, Connection con, SolicitarForm solicitarForm) throws SQLException
	{
		
		//la info viene cargada desde el resumen, luego no limpiamos el form
		solicitarForm.setNumeroServicios(1);	
		Solicitud s=new Solicitud();
		s.cargar(con,solicitarForm.getNumeroSolicitud());
		ValidacionesSolicitud vs=new ValidacionesSolicitud(con,solicitarForm.getNumeroSolicitud(),this.usuario,this.paciente);
		this.cerrarConexion(con);
		if(s.getEstadoHistoriaClinica().getCodigo()==ConstantesBD.codigoEstadoHCInterpretada){
			//desviamos al formulario de resumen editable
			solicitarForm.setInterpretar(vs.puedoModificarSolicitudInterpretada().isTrue());

			solicitarForm.setUltimaInterpretacion(s.getInterpretacion());
			solicitarForm.setVaEpicrisis(s.isVaAEpicrisis());
			solicitarForm.setEstado("modificar");
			//this.modificar=true;
			return mapping.findForward("resumen");
			
		}else {
			solicitarForm.setEstado("interpretar");
			solicitarForm.setInterpretar(vs.puedoInterpretar().isTrue());

			//desviamos al formulario de interpretacion
			return mapping.findForward("interpretar");
		}
	}

	

	private ActionForward  accionInterpretar(ActionMapping mapping, SolicitarForm solicitarForm, Connection con, HttpServletRequest request) throws SQLException, IOException 
	{
		
	
		Solicitud s=new Solicitud();
		s.setNumeroSolicitud(solicitarForm.getNumeroSolicitud());
		s.cargar(con, solicitarForm.getNumeroSolicitud());
		//añadir informacion extra a la interpretacion.
		String interpretacion="";
		
		/*if(s.getTipoSolicitud().getCodigo()==ConstantesBD.codigoTipoSolicitudInterconsulta)
		{
		    if(UtilidadValidacion.esSolicitudManejoConjuntoFinalizado(con, solicitarForm.getNumeroSolicitud()))
		    {
		        interpretacion=UtilidadCadena.cargarObservaciones(solicitarForm.getInterpretacion(),"",this.usuario);
				s.interpretarSolicitudInterconsulta(con,interpretacion,this.usuario);
				this.cerrarConexion(con);
				solicitarForm.reset();
		    }
		}
		else
		{    */
			//En el caso de que la interpretacion sea vacía se añade un caracter
			//para que los datos del médico sean almacenados
			if(solicitarForm.getInterpretacion().equals(""))
				solicitarForm.setInterpretacion("-");
			
			interpretacion=UtilidadCadena.cargarObservaciones(solicitarForm.getInterpretacion(),"",this.usuario);
			int resp = s.interpretarSolicitud(con,this.usuario.getCodigoPersona(),interpretacion);
			solicitarForm.reset();
			
			if (resp == -1)
			{
				request.getSession().setAttribute("solicitudYaInterpretada","true");
			}
			this.cerrarConexion(con);
			return mapping.findForward("interpretar2");
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * cancelar, por ahora no tiene implementación
	 * 
	 * @param request Request de http
	 * @param mapping Mapping de la aplicación 
	 * @param con Conexión con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionCancelar(HttpServletRequest request, ActionMapping mapping, Connection con) throws SQLException
	{
		this.cerrarConexion(con);
		request.setAttribute("descripcionError", "Accion no implementada");
		return mapping.findForward("paginaError");
	}

	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer
	 * un forward
	 * @param con Conexión con la fuente de datos
	 * @throws SQLException
	 */
	private void cerrarConexion (Connection con) throws SQLException
	{
		if (con!=null&&!con.isClosed())
		{
			UtilidadBD.closeConnection(con);
		}
	}
		
	/**
	 * Método que se encarga de realizar las validaciones comunes
	 * a todos los estados / tipos en la solicitud general. Valida que
	 * el usuario sea válido, que sea profesional de la salud y que
	 * haya al menos un paciente cargado
	 *  
	 * @param map Mapping de la aplicación
	 * @param req Request de Http
	 * @param paciente paciente cargado
	 * @param medico medico / usuario que intenta acceder a la
	 * funcionalidad
	 * @return
	 */
	private ActionForward validacionesUsuario(ActionMapping map, HttpServletRequest req, PersonaBasica paciente, UsuarioBasico medico )
	{
		if(medico == null)
		{
			logger.warn("Profesional de la salud no válido (null)");			

			req.setAttribute("codigoDescripcionError", "errors.usuario.noCargado");
			return map.findForward("paginaError");
		}
		else
		if( !UtilidadValidacion.esProfesionalSalud(medico) )
		{
			req.setAttribute("codigoDescripcionError", "errors.usuario.noAutorizado");
			return map.findForward("paginaError");
		}		
		else 
		if( paciente == null || paciente.getTipoIdentificacionPersona().equals("") )
		{
			logger.warn("El paciente no es válido (null)");

			req.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
			return map.findForward("paginaError");
		}
		return null;
	}
	
	/**
	 * Método que llena los datos generales consignados por el usuario
	 * (e inferidos) necesarios para la solicitud general.
	 * 
	 * @param solicitarForm Form con los datos llenados por el usuario
	 * @param paciente Paciente al que se le va a realizar la solicitud
	 * @param medico Medico que está realizando la solicitud
	 * @return
	 */
	/*private Solicitud llenarSolicitud (SolicitarForm solicitarForm, PersonaBasica paciente, UsuarioBasico medico)
	{

		//Por cuestión de referencia, se maneja un objeto por cada
		//iteración
		Solicitud solicitudTemp=new Solicitud();
		solicitudTemp.setFechaSolicitud(solicitarForm.getFechaSolicitud());
		solicitudTemp.setHoraSolicitud(solicitarForm.getHoraSolicitud());

		if(solicitarForm.getCodigoTipoSolicitud()==ConstantesBD.codigoServicioInterconsulta)
		{
			//Definimos el tipo de solicitud específico para interconsulta y
			//su valor cobrable
			solicitudTemp.setTipoSolicitud(new InfoDatosInt (ConstantesBD.codigoTipoSolicitudInterconsulta, ""));
			solicitudTemp.setCobrable(true);
		}
		else if(solicitarForm.getCodigoTipoSolicitud()==ConstantesBD.codigoServicioProcedimiento)
		{
			//Definimos el tipo de solicitud específico para procedimientos y
			//su valor cobrable
			solicitudTemp.setTipoSolicitud(new InfoDatosInt (ConstantesBD.codigoTipoSolicitudProcedimiento, ""));
			solicitudTemp.setCobrable(true);
		}
		solicitudTemp.setNumeroAutorizacion(solicitarForm.getNumeroAutorizacion());
		solicitudTemp.setEspecialidadSolicitante(new InfoDatosInt(solicitarForm.getEspecialidadSolicitante(), ""));
		solicitudTemp.setOcupacionSolicitado(new InfoDatosInt(solicitarForm.getOcupacionSolicitada(), ""));
		solicitudTemp.setCentroCostoSolicitante(new InfoDatosInt(medico.getCodigoCentroCosto(), ""));
		solicitudTemp.setCentroCostoSolicitado(new InfoDatosInt(solicitarForm.getCentroCostoSolicitado(), ""));
		solicitudTemp.setCodigoMedicoSolicitante(medico.getCodigoPersona());
		solicitudTemp.setCodigoCuenta(paciente.getCodigoCuenta());
			
		//No va a epicrisis porque todavía no ha sido ejecutada
		solicitudTemp.setVaAEpicrisis(false);
		solicitudTemp.setUrgente(solicitarForm.isUrgenteOtros());

		return solicitudTemp;
	}*/
}
