package com.princetonsa.action.consultaExterna;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.UtilidadBD;
import util.Utilidades;
import util.Administracion.UtilidadesAdministracion;
import util.consultaExterna.UtilidadesConsultaExterna;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.consultaExterna.ConsultaMultasAgendaCitasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.consultaExterna.ConsultaMultasAgendaCitas;

public class ConsultaMultasAgendaCitasAction extends Action {
	Logger logger = Logger.getLogger(ConsultaMultasAgendaCitasAction.class);
	String estado="";
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		UsuarioBasico usuario;
		Connection con = null;
		ConsultaMultasAgendaCitasForm consultaMultasAgendaCitasForm = null;

		// se obtiene el usuario cargado en sesion.
		usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");

		try {

			if (form instanceof ConsultaMultasAgendaCitasForm) {
				consultaMultasAgendaCitasForm = (ConsultaMultasAgendaCitasForm) form;

				estado = consultaMultasAgendaCitasForm.getEstado();
				logger.warn("Estado Motivos Anulacion Condonacion Form [" + estado + "]");
			}
			try {
				con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			} catch (SQLException e) {
				logger.warn("No se pudo abrir la conexión" + e.toString());
				consultaMultasAgendaCitasForm.clean();

				logger.warn("Problemas con la base de datos " + e);
				request
				.setAttribute("codigoDescripcionError",
				"errors.problemasBd");
				return mapping.findForward("paginaError");
			}

			// se obtiene el paciente cargado en sesion.
			PersonaBasica paciente = (PersonaBasica) request.getSession()
			.getAttribute("pacienteActivo");

			// se obtiene la institucion
			InstitucionBasica institucion = (InstitucionBasica) request
			.getSession().getAttribute("institucionBasica");

			// se instancia la forma
			ConsultaMultasAgendaCitasForm forma = (ConsultaMultasAgendaCitasForm) form;

			// se instancia el mundo
			ConsultaMultasAgendaCitas mundo = new ConsultaMultasAgendaCitas();

			// se instancia la variable para manejar los errores.
			ActionErrors errores = new ActionErrors();

			/*
			 * cuando necesites mostrar mensajes de error forma.setMensaje(new
			 * ResultadoBoolean(false));
			 */

			logger.info("\n\n\n\n estado -----------> "+estado);

			if(estado == null)
			{
				forma.clean();
				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
				UtilidadBD.cerrarConexion(con);

				return mapping.findForward("paginaError");
			}
			else if (estado.equals("empezar")) {
				return this.accionEmpezar(forma, mundo, mapping,con, usuario, request);
			} else if (estado.equals("paciente")) {
				return this.accionPaciente(forma, mundo, mapping,con,paciente, usuario, request);
			}else if (estado.equals("rango")) {
				return this.accionRango(forma, mundo, mapping,con, usuario, request);
			}else if (estado.equals("buscar")){
				return this.accionBuscar(forma, mundo, mapping,con, usuario, request);
			}else if(estado.equals("filtrarUnidadesAgenda")){
				return this.accionfiltrarUnidadesAgenda(forma, mundo, mapping,con, usuario, response);

			}
			else if (estado.equals("ordenar"))
			{
				return accionOrdenar(forma, mundo, mapping, con, usuario, request);
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

	private ActionForward accionOrdenar(ConsultaMultasAgendaCitasForm forma,
			ConsultaMultasAgendaCitas mundo, ActionMapping mapping,
			Connection con, UsuarioBasico usuario, HttpServletRequest request) {
			String[] indices = mundo.indicesListado;
		
			
			
		int num = forma.getConsultaMultasAgendaCitas1();
		
		
		
		forma.setConsultaMultasAgendaCitas(Listado.ordenarMapa
				(indices,
				forma.getIndice(),
				forma.getUltimoIndice(),
				forma.getConsultaMultasAgendaCitas(),
				num));
		
				
	
		
		forma.setConsultaMultasAgendaCitas("numRegistros",num+"");
		
		forma.setUltimoIndice(forma.getIndice());
		
		
		UtilidadBD.closeConnection(con);
		forma.setEstado("buscar");
		return mapping.findForward("principal");
	}

	private ActionForward accionfiltrarUnidadesAgenda(
			ConsultaMultasAgendaCitasForm forma,
			ConsultaMultasAgendaCitas mundo, ActionMapping mapping,
			Connection con, UsuarioBasico usuario, HttpServletResponse response) {
	
		forma.setUnidadAgendaMap(UtilidadesConsultaExterna.ObtenerUnidadesAgendaXcentrosAtencion(con,Utilidades.convertirAEntero(forma.getCentroAtencionSel()+""),"", null));
		
		forma.setEstado("rango");
		
		return mapping.findForward("principal");
	}

	private ActionForward accionPaciente(ConsultaMultasAgendaCitasForm forma,
			ConsultaMultasAgendaCitas mundo, ActionMapping mapping,
			Connection con, PersonaBasica paciente, UsuarioBasico usuario,
			HttpServletRequest request) {
		
		if(paciente==null || paciente.getCodigoPersona()<=0)
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.noCargado", "errors.paciente.noCargado", true);
		}
		else
		{
			
			forma.setConsultaMultasAgendaCitas(mundo.consultaMultasAgendaCitasPaciente(paciente.getCodigoPersona(),usuario.getCodigoInstitucion()));
			
		}
		
		return mapping.findForward("principal");
	}

	private ActionForward accionBuscar(ConsultaMultasAgendaCitasForm forma,
			ConsultaMultasAgendaCitas mundo, ActionMapping mapping,
			Connection con, UsuarioBasico usuario, HttpServletRequest request) {
		
		
		
		HashMap<String, Object> ELMAPA = new HashMap<String, Object>();
		
		ELMAPA.put("numRegistros", "0");
		
		ELMAPA.put("convenio", forma.getConvenio());
		
		ELMAPA.put("centroAtencion", forma.getCentroAtencionSel());
		
		ELMAPA.put("profesional", forma.getProfesional());
		
		ELMAPA.put("estadoCita", forma.getEstadoCita());
		
		ELMAPA.put("institucion", usuario.getCodigoInstitucion());
		
		ELMAPA.put("unidadAgenda", forma.getUnidadAgenda());
		
		
		
		forma.setConsultaMultasAgendaCitas(mundo.consultaMultasAgendaCitas(forma.getFechainicial(),forma.getFechafinal(),ELMAPA));
	
		
		return mapping.findForward("principal");
	}

	private ActionForward accionRango(ConsultaMultasAgendaCitasForm forma,
			ConsultaMultasAgendaCitas mundo, ActionMapping mapping,
			Connection con, UsuarioBasico usuario, HttpServletRequest request) {
		
		
		forma.clean();
		
		ArrayList<Integer> estadosCita = new ArrayList<Integer>();
		
		estadosCita.add(1);
		
		estadosCita.add(2);
		
		estadosCita.add(3);
		
		forma.setUnidadAgendaMap(UtilidadesConsultaExterna.ObtenerUnidadesAgendaXcentrosAtencion(con,Utilidades.convertirAEntero(forma.getCentroAtencion()+""),"", null));
		
		forma.setCentrosAtencionMap(Utilidades.obtenerCentrosAtencion(usuario.getCodigoInstitucionInt()));
		
		forma.setProfesionalMap(UtilidadesAdministracion.obtenerProfesionales(con, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, false, true, ConstantesBD.codigoNuncaValido));
		
		forma.setConveniosMap(Utilidades.obtenerConvenios(con, "", "", false, "", true));
		
		forma.setEstadoCitaMap(UtilidadesConsultaExterna.consultaEstadosCita(con, estadosCita));
				
		return mapping.findForward("principal");
		
	}

	private ActionForward accionEmpezar(ConsultaMultasAgendaCitasForm forma,
			ConsultaMultasAgendaCitas mundo, ActionMapping mapping,
			Connection con, UsuarioBasico usuario, HttpServletRequest request) {

		forma.clean();
		return mapping.findForward("principal");
		
	}
	
	
	
}
