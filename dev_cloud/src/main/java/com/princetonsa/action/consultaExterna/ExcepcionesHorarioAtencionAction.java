package com.princetonsa.action.consultaExterna;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.ValoresPorDefecto;
import util.Administracion.UtilidadesAdministracion;
import util.consultaExterna.UtilidadesConsultaExterna;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.consultaExterna.ExcepcionesHorarioAtencionForm;
import com.princetonsa.dto.administracion.DtoProfesional;
import com.princetonsa.dto.consultaExterna.DtoConsultorios;
import com.princetonsa.dto.consultaExterna.DtoExcepcionesHorarioAtencion;
import com.princetonsa.mundo.Usuario;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.consultaExterna.ExcepcionesHorarioAtencion;
import com.princetonsa.mundo.parametrizacion.CentroAtencion;
import com.princetonsa.sort.odontologia.SortGenerico;




public class ExcepcionesHorarioAtencionAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			
		
		throws Exception {
		if (response == null);
		if (form instanceof ExcepcionesHorarioAtencionForm) 
		{
		
			ExcepcionesHorarioAtencionForm forma = (ExcepcionesHorarioAtencionForm) form;
			UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
			String estado = forma.getEstado();
			Log4JManager.info("************ estado '' "+estado);
			
			
			if (estado==null)
			{
				 return mapping.findForward("paginaError");
			}
			
			else if (estado.equals("empezar"))
			{
				// Obtener centros de atención validos para el usuario
				Connection con=UtilidadBD.abrirConexion();
				HashMap centrosAtencionValidos=UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(con, usuario.getLoginUsuario(), ConstantesBD.codigoActividadAutorizadaExcepcionesHorarioAtencion);
				UtilidadBD.closeConnection(con);
				
				if(Integer.parseInt(centrosAtencionValidos.get("numRegistros")+"")<=0)
				{
					ActionErrors errores = new ActionErrors();
					errores.add("", new ActionMessage("errors.notEspecific","El usuario no puede realizar excepciones a los horarios de atención"));
					saveErrors(request, errores);
					return mapping.findForward("paginaErroresActionErrors");
					
				}
				return accionEmpezar(forma, usuario, mapping);
				
			}else
				
			if (estado.equals("listarCentrosAtencion"))
			{
				forma.setExito(false);
				return accionListarCentrosAtencion(forma, usuario, mapping);
				
			}
			
			else if (estado.equals("nuevo"))
			{
				forma.setExito(false);
				DtoExcepcionesHorarioAtencion dtoExcepcion = new DtoExcepcionesHorarioAtencion();
				// coloca por default el usuario de la session
				CentroAtencion centroAtencion=new CentroAtencion();
				centroAtencion.setConsecutivo(forma.getCentroAtencion());
				dtoExcepcion.setCentroAtencion(centroAtencion);
				// se ingresa el usuario modifica
				dtoExcepcion.setUsuarioModifica(usuario.getLoginUsuario());
				// se ingresa la institución del usuario
				dtoExcepcion.setInstitucion(usuario.getCodigoInstitucionInt());
				// ingresa el dto al arreglo
				forma.getListaDtoExcepcionesha().add(dtoExcepcion);
				// envía la posición del arreglo
				forma.setPosicion(forma.getListaDtoExcepcionesha().size()-1);
				
				forma.setEstadoAnterior(estado);
								
				return accionRecargarCentroAtencion(forma,usuario,mapping);
				
			}
			else if(estado.equals("cambiarProfesional"))
			{
				if(forma.getCentroAtencion()>0 && forma.getProfesionalSalud()>0)
				{
					Connection con=UtilidadBD.abrirConexion();
					forma.setListaDtoExcepcionesha(ExcepcionesHorarioAtencion.listar(con,usuario.getCodigoInstitucionInt(),forma.getCentroAtencion(), forma.getProfesionalSalud()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
			}
			
			else if (estado.equals("modificar"))
			{
						
				forma.setExito(false);
				Connection con = UtilidadBD.abrirConexion();
			
				// consulta centros de atencion llena el arreglo 
				forma.setCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(con, usuario.getCodigoInstitucionInt(), ""));
				
				String tipoAtencion=ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica;
				//llena un hasmap de unidades de conuslta
				forma.setListadoUnidadesConsulta(UtilidadesConsultaExterna.ObtenerUnidadesAgendaXcentrosAtencion(con,forma.getListaDtoExcepcionesha().get(forma.getPosicion()).getDtoCentroAtencion().getConsecutivo(),tipoAtencion, null));
				//llena un array hasmap de profesionales segun la unidad de agenda
			
				forma.setProfesionales(UtilidadesAdministracion.obtenerProfesionalesXCentroAtencion(con, usuario.getCodigoCentroAtencion()));
			
				//llena un dto de consultorios 
				forma.setListaDtoConsultorios(UtilidadesConsultaExterna.ObtenerConsultorios(con, forma.getListaDtoExcepcionesha().get(forma.getPosicion()).getDtoCentroAtencion().getConsecutivo()));
				//llena un dto de uauarios
				forma.setListaDtoUsuario((UtilidadesAdministracion.obtenerUsuarios(new Usuario(), usuario.getCodigoInstitucionInt(), false)));
				
				UtilidadBD.closeConnection(con);
		
				forma.setEstadoAnterior(estado);
				
				return mapping.findForward("principal");
			}
			
			
			
			else if (estado.equals("actualizar"))
			{
				return this.accionActualizar(forma, mapping);
			}
			
			else if (estado.equals("eliminar"))
			{
				return this.accionEliminar(forma, mapping, usuario);
			}
			
			else if (estado.equals("recargarCentroAtencion"))
			{
				return accionRecargarCentroAtencion(forma,usuario,mapping);
			}
			
			
			else if (estado.equals("guardar"))
			{
				return this.accionGuarda(forma, mapping, usuario);
			}
			
			else if (estado.equals("recargarUnidadAgenda"))
			{
				return accionRecargarUnidadAgenda(forma,usuario,mapping);
			}
			else if (estado.equals("ordenar"))
			{
				forma.setExito(false);
				return accionOrdenar(forma,mapping);
			}
			else if (estado.equals("redireccion"))
			{
				forma.setExito(false);
				return accionRedireccion(forma,response);
			}
		
			
			
		}
		return mapping.findForward("principal");
		}

	
	private ActionForward accionRedireccion(
			ExcepcionesHorarioAtencionForm forma, HttpServletResponse response) 
	{
		try 
		{
			response.sendRedirect(forma.getLinkSiguiente());
		} 
		catch (IOException e) 
		{
			Log4JManager.error("Error en redireccion: ",e);
		}
		return null;
	}


	private ActionForward accionOrdenar(ExcepcionesHorarioAtencionForm forma,
			ActionMapping mapping) 
	{
		Log4JManager.info("patron->" + forma.getPatronOrdenar());
		Log4JManager.info("des -->" + forma.getEsDescendente() );
		
		boolean ordenamiento= false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente")){
			
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		
		Collections.sort(forma.getListaDtoExcepcionesha() ,sortG);
		return mapping.findForward("principal");
	}


	/**
	 * Guardar la excepción al horario de atención
	 * @param forma Formulario
	 * @param mapping Objeto para redireccionar
	 * @param usuario Usuario en sesión
	 * @return Forward a la página deseada
	 */
	private ActionForward accionGuarda(ExcepcionesHorarioAtencionForm forma, ActionMapping mapping, UsuarioBasico usuario) {
		
		Connection con=UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		
		DtoExcepcionesHorarioAtencion listaDtoExcepciones=forma.getListaDtoExcepcionesha().get(forma.getPosicion());
		listaDtoExcepciones.setCodigoMedico(forma.getProfesionalSalud());
		ExcepcionesHorarioAtencion.insertar(listaDtoExcepciones, con);		
	    
		UtilidadBD.finalizarTransaccion(con);
		UtilidadBD.closeConnection(con);
		
		forma.setExito(true);
		
		return accionListarCentrosAtencion(forma, usuario, mapping);
	
		
	}

	
	
	private ActionForward accionActualizar(ExcepcionesHorarioAtencionForm forma, ActionMapping mapping) {
		
		
		Connection con=UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		
		DtoExcepcionesHorarioAtencion listaDtoExcepciones=forma.getListaDtoExcepcionesha().get(forma.getPosicion());
		ExcepcionesHorarioAtencion.modificar(  listaDtoExcepciones, con);		
		
		
		UtilidadBD.finalizarTransaccion(con);
		ExcepcionesHorarioAtencion.cargar( con,listaDtoExcepciones);
		UtilidadBD.closeConnection(con);
		forma.setExito(true);
		return mapping.findForward("principal");
	}
	
	
	private ActionForward accionEliminar(ExcepcionesHorarioAtencionForm forma, ActionMapping mapping,
			UsuarioBasico usuario) {
		
		
		Connection con=UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		
		
	
		DtoExcepcionesHorarioAtencion listaDtoExcepciones=forma.getListaDtoExcepcionesha().get(forma.getPosicion());
		listaDtoExcepciones.setUsuarioModifica(usuario.getLoginUsuario());
		listaDtoExcepciones.setUsuarioEliminacion(usuario.getLoginUsuario());
		listaDtoExcepciones.setEliminado(ConstantesBD.acronimoSi);
		ExcepcionesHorarioAtencion.eliminar(  listaDtoExcepciones, con);		
			
		
		
		
		UtilidadBD.finalizarTransaccion(con);
		
		//ExcepcionesHorarioAtencion.cargar( con,listaDtoExcepciones);
		
		forma.getListaDtoExcepcionesha().remove(forma.getPosicion());
		
		UtilidadBD.closeConnection(con);
		
		
		
		forma.setExito(true);
		
		return mapping.findForward("principal");
	}
	

	private ActionForward accionRecargarCentroAtencion(
			ExcepcionesHorarioAtencionForm forma, UsuarioBasico usuario,
			ActionMapping mapping) 
	{
		Connection con = UtilidadBD.abrirConexion();
		
		int centroAtencion = forma.getCentroAtencion();
		int profesionalSalud = forma.getProfesionalSalud();
		
		if(centroAtencion>0 && profesionalSalud>0)
		{
			String tipoAtencion=ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica;
			forma.setListaDtoConsultorios(UtilidadesConsultaExterna.ObtenerConsultorios(con, centroAtencion));
			forma.setListadoUnidadesConsulta(UtilidadesConsultaExterna.ObtenerUnidadesAgendaXcentrosAtencion(con, centroAtencion,tipoAtencion, true));
		}
		else
		{
			forma.setListaDtoConsultorios(new ArrayList<DtoConsultorios>());
			forma.getListadoUnidadesConsulta().put("numRegistros","0");
			forma.setProfesionales(new ArrayList<DtoProfesional>());
		}
		
		UtilidadBD.closeConnection(con);
		
		forma.setEstado(forma.getEstadoAnterior());
		
		
		
		return mapping.findForward("principal");
	}

	
	
	private ActionForward accionRecargarUnidadAgenda(
			ExcepcionesHorarioAtencionForm forma, UsuarioBasico usuario,
			ActionMapping mapping) 
	{
		Connection con = UtilidadBD.abrirConexion();
		//forma.getListaDtoExcepcionesha().get(forma.getPosicion()).setUnidadAgenda((Utilidades.convertirAEntero(forma.getIndicador())));
		
		int unidadAgenda= forma.getListaDtoExcepcionesha().get(forma.getPosicion()).getUnidadAgenda();
		
		if(unidadAgenda>0)
		{
			forma.setProfesionales(UtilidadesAdministracion.obtenerProfesionalesXCentroAtencion(con, usuario.getCodigoCentroAtencion()));
		}
		else
		{
			forma.setProfesionales(new ArrayList<DtoProfesional>());
		}
		
		UtilidadBD.closeConnection(con);
		forma.setEstado(forma.getEstadoAnterior());
		return mapping.findForward("principal");
	}

	
	
	
	
	

	private ActionForward accionEmpezar(ExcepcionesHorarioAtencionForm  forma, UsuarioBasico usuario, ActionMapping mapping) {
		
		forma.reset();
		Connection con=UtilidadBD.abrirConexion();
		//postulo el centro de atencion del usuario.
		forma.setCentroAtencion(usuario.getCodigoCentroAtencion());
		UtilidadBD.closeConnection(con);
		
		forma.setMaxPageItems(ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()));
		
		return accionCosultar(forma,mapping,usuario);
	}
	

	private ActionForward accionListarCentrosAtencion(ExcepcionesHorarioAtencionForm  forma, UsuarioBasico usuario, ActionMapping mapping) {
		
		
		Connection con=UtilidadBD.abrirConexion();
		ArrayList<DtoExcepcionesHorarioAtencion> listaDtoExcepcioens=ExcepcionesHorarioAtencion.listar(con,usuario.getCodigoInstitucionInt(),forma.getCentroAtencion(), forma.getProfesionalSalud());	
		forma.setListaDtoExcepcionesha(listaDtoExcepcioens);
		UtilidadBD.closeConnection(con);
		
		return accionCosultar(forma,mapping,usuario);
	}



	// metodo para llenar el arreglo con los campos
	private ActionForward accionCosultar(ExcepcionesHorarioAtencionForm forma, ActionMapping mapping, UsuarioBasico usuario) 
	{

		Connection con = UtilidadBD.abrirConexion();
		// consulta centros de atencion llena el arreglo 
		forma.setCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(con, usuario.getCodigoInstitucionInt(), ""));
		
		
		//llena un hasmap de unidades de conuslta
		String tipoAtencion=ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica;
		forma.setListadoUnidadesConsulta(UtilidadesConsultaExterna.ObtenerUnidadesAgendaXcentrosAtencion(con, usuario.getCodigoCentroAtencion(),tipoAtencion, null));
		//llena un array hasmap de profesionales segun la unidad de agenda
		if (forma.getCentroAtencion()>0)
		{
			if(forma.getProfesionalSalud()>0)
			{
				ArrayList<DtoExcepcionesHorarioAtencion> listaDtoExcepcioens=ExcepcionesHorarioAtencion.listar(con,usuario.getCodigoInstitucionInt(),forma.getCentroAtencion(), forma.getProfesionalSalud());	
				forma.setListaDtoExcepcionesha(listaDtoExcepcioens);
			}
			else
			{
				forma.setProfesionales(UtilidadesAdministracion.obtenerProfesionalesXCentroAtencion(con, usuario.getCodigoCentroAtencion()));
			}
		}
		
		//llena un dto de consultorios 
		forma.setListaDtoConsultorios(UtilidadesConsultaExterna.ObtenerConsultorios(con, usuario.getCodigoCentroAtencion()));
		//llena un dto de uauarios
		forma.setListaDtoUsuario((UtilidadesAdministracion.obtenerUsuarios(new Usuario(), usuario.getCodigoInstitucionInt(), false)));
		
		UtilidadBD.closeConnection(con);
		
		return mapping.findForward("principal");
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
