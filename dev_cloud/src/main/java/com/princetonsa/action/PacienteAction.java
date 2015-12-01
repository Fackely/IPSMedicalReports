/*
 * @(#)PacienteAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.action;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.UtilidadBD;
import util.UtilidadFecha;

import com.princetonsa.actionform.PacienteForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.Paciente;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.HistObserGenerPaciente;
import com.servinte.axioma.orm.Pacientes;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.servicio.fabrica.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IHistObserGenerPacienteServicio;

/** 
 * Clase encargada del control de las observaciones generales ingresadas
 * para el paciente cargado en session en el CABEZOTE

 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 01 /Dic/ 2005
 */
public class PacienteAction extends Action
{
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 */
	private Logger logger= Logger.getLogger(PacienteAction.class);

	/**
	 * Método excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try{
			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

			if(form instanceof PacienteForm)
			{


				/**Intentamos abrir una conexion con la fuente de datos**/ 
				con = openDBConnection(con); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				PacienteForm forma=(PacienteForm)form;

				HttpSession session = request.getSession();
				PersonaBasica paciente= (PersonaBasica)session.getAttribute("pacienteActivo");

				Paciente mundo=new Paciente();

				String estado = forma.getEstado();
				logger.warn("[PacienteAction] estado=>"+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de PacienteAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("guardar"))
				{
					return this.accionGuardarObservacion(mapping, request, forma, con, mundo, paciente, usuario);
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de Paciente");
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
	 * Action para guardar la Observaciones Generales ingresadas para el paciente cargado en session
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param con
	 * @param mundo
	 * @param medico
	 * @param paciente
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionGuardarObservacion(ActionMapping mapping, HttpServletRequest request,  PacienteForm forma, Connection con, Paciente mundo, PersonaBasica paciente, UsuarioBasico usuario) throws Exception
	{
		
		IHistObserGenerPacienteServicio histoServi=AdministracionFabricaServicio.crearHistObserGenerPaciente();
		
		HistObserGenerPaciente histoO= new HistObserGenerPaciente();
		
		
		histoO.setFechaModifica(UtilidadFecha.getFechaActualTipoBD());
		Usuarios usuarioEntid = new Usuarios();
		usuarioEntid.setLogin(usuario.getLoginUsuario());
		histoO.setUsuarios(usuarioEntid);
		histoO.setHoraModifica(UtilidadFecha.getHoraActual());
		Pacientes pacientes = new Pacientes();
		pacientes.setCodigoPaciente(paciente.getCodigoPersona());
		histoO.setPacientes(pacientes);
		histoO.setObservacion(forma.getObservaciones());
		
		
		histoServi.insertar(histoO);
		
		
		
		
		
		int respuesta=mundo.insertarObservacionPaciente(con, paciente.getCodigoPersona(), forma.getObservaciones());
		UtilidadBD.closeConnection(con);
		if(respuesta>0)
		{
			forma.setObservaciones("");
			return mapping.findForward("infoAdicional");
		}
		else
		{
			return ComunAction.accionSalirCasoError(mapping,request,con,logger, "no se logro guardar la nota","error.salasCirugia.errorGuardandoNotaGeneral", true);
		}
	}
	/**
	 * Abrir la conexion con la base de datos
	 * @param con
	 * @return
	 */
	 public Connection openDBConnection(Connection con)
	{

		if(con != null)
			return con;
		
		try{
			String tipoBD = System.getProperty("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			con = myFactory.getConnection();
		}
		catch(Exception e)
		{
			logger.warn("Problemas con la base de datos al abrir la conexion");
			return null;
		}
	
		return con;
	}
		 
}
			