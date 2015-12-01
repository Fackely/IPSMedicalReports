package com.princetonsa.action.administracion;

import java.sql.Connection;
import java.util.Collections;

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
import util.UtilidadSesion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.administracion.EspecialidadesForm;
import com.princetonsa.dto.administracion.DtoEspecialidades;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.administracion.Especialidades;
import com.princetonsa.sort.administracion.SortEspecialidades;

/**
 * @author Víctor Hugo Gómez L.
 */

public class EspecialidadesAction extends Action {
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private Logger logger = Logger.getLogger(EspecialidadesAction.class);

	/**
	 * Metodo excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, 
								ActionForm form,
								HttpServletRequest request, 
								HttpServletResponse response)
			throws Exception {
		Connection con = null;
		try{
		if (response == null);
		if (form instanceof EspecialidadesForm) {
			
			con = UtilidadBD.abrirConexion();
			if (con == null) {
				request.setAttribute("codigoDescripcionError","errors.problemasBd");
				return mapping.findForward("paginaError");
			}

			UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
			EspecialidadesForm forma = (EspecialidadesForm) form;
			Especialidades especialidades = new Especialidades(); 
			String estado = forma.getEstado();
			forma.setMaxItems(Utilidades.convertirAEntero(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())+""));
			
			logger.warn("\n\n\nEl estado en Especialidades es------->"+ estado + "\n");

			if (estado == null) {
				forma.reset();
				logger.warn("Estado no valido dentro del flujo de Especilidades (null) ");
				request.setAttribute("codigoDescripcionError","errors.estadoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
			
			else if (estado.equals("empezar")) {
				forma.reset();
				cargarCentrosCostos(con,usuario, forma);
				return cargarEspecialdades(con, mapping, usuario, forma, especialidades);
			}else if (estado.equals("redireccion")) {
				if(forma.getPosEspecialidad()!=ConstantesBD.codigoNuncaValido)
					forma.getEspecialidades().get(forma.getPosEspecialidad()).setModificar(ConstantesBD.acronimoSi);
				UtilidadBD.closeConnection(con);
				if(!forma.getPaginaLinkSiguiente().equals(""))
					response.sendRedirect(forma.getPaginaLinkSiguiente());
				else
					return mapping.findForward("empezar");
				
			}else if (estado.equals("nuevaEspecialidad")) {
				DtoEspecialidades elem = new DtoEspecialidades();
				elem.setModificar(ConstantesBD.acronimoSi);
				elem.setIngresar(ConstantesBD.acronimoSi);
				forma.getEspecialidades().add(elem);
				UtilidadBD.closeConnection(con);
				UtilidadSesion.redireccionar(forma.getPaginaLinkSiguiente(), 
						Utilidades.convertirAEntero(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())+""),
						forma.getEspecialidades().size(), response, request, "ingresarEspecilidades.jsp", true);
			}else if (estado.equals("guardar")) {
				return guardarEspecialdades(con,mapping,usuario,forma,especialidades,response,request);
			}else if (estado.equals("eliminar")) {
				return eliminarEspecialdades(con,mapping,usuario,forma,especialidades,request);
			}else if (estado.equals("cargarBusquedaAvanzada")){
				//forma.setBusquedaAvanzada(ConstantesBD.acronimoSi);
				if(forma.getMostrarBusCon().equals(ConstantesBD.acronimoNo))
					return mapping.findForward("empezar");
				else
					return mapping.findForward("resumen");
			}else if (estado.equals("busquedaAvanzada")){
				forma.setBusquedaAvanzada(ConstantesBD.acronimoSi);
				return busquedaAvanzadaEspecialidades(con, mapping, usuario, forma, especialidades);
			}else if (estado.equals("consultar")){
				forma.reset();
				cargarCentrosCostos(con,usuario, forma);
				return cargarEspecialdades(con, mapping, usuario, forma, especialidades);
			}else if(estado.equals("ordenar")){
				return accionOrdenar(mapping, con, forma);
			}
			
			else {
				forma.reset();
				logger.warn("Estado no valido dentro del flujo de especialidades -> "+ estado);
				request.setAttribute("codigoDescripcionError","errors.formaTipoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
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
	 * @param mapping
	 * @param con
	 * @param forma
	 * @return
	 */
	private ActionForward accionOrdenar(ActionMapping mapping, Connection con,
			EspecialidadesForm forma) {
		SortEspecialidades sort= new SortEspecialidades();
		sort.setPatronOrdenar(forma.getPatronOrdenar());
		Collections.sort(forma.getEspecialidades(), sort);
		UtilidadBD.closeConnection(con);
		if(forma.getMostrarOrdRes().equals(ConstantesBD.acronimoNo))
			return mapping.findForward("empezar");
		else
			return mapping.findForward("resumen");
	}
	
	/**
	 * Método para consultar las especialidades de una instituicion 
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param forma
	 * @param especialidades
	 * @return
	 */
	private ActionForward cargarEspecialdades(Connection con, ActionMapping mapping, UsuarioBasico usuario, EspecialidadesForm forma, Especialidades especialidades) 
	{		
		// cargar especialidades
		forma.setEspecialidades(especialidades.cargarListadoEspecialidades(con, usuario.getCodigoInstitucionInt()+""));
		if(!forma.getEstado().equals("consultar"))
			forma.setEspecialidades(especialidades.verificarEspecialidadesUsadas(con, forma.getEspecialidades()));
		UtilidadBD.closeConnection(con);
    	return mapping.findForward("empezar");
	}
	
	/**
	 * Método para cargar centros costo
	 * @param con
	 * @param usuario
	 * @param forma
	 * @return
	 */
	private void cargarCentrosCostos(Connection con, UsuarioBasico usuario, EspecialidadesForm forma)
	{
		// cargar centros costo
		forma.getCentrosCosto().clear();
		forma.setCentrosCosto(UtilidadesManejoPaciente.obtenerCentrosCosto(con,
				usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoAreaDirecto+"",false,ConstantesBD.codigoNuncaValido));
	}
	
	/**
	 * Método para ingrear y/o modificar Especilidades 
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param forma
	 * @param especialidades
	 * @return
	 */
	private ActionForward guardarEspecialdades(Connection con, ActionMapping mapping, UsuarioBasico usuario, EspecialidadesForm forma, Especialidades especialidades, HttpServletResponse response, HttpServletRequest request) 
	{		
		especialidades.setEspecialidades(forma.getEspecialidades());
		forma.setEspecialidades(especialidades.guardarEspecilidades(con,usuario));
		forma.setTieneError(false);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("empezar");
	}
	
	/**
	 * Método para Eliminar una Especilidades 
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param forma
	 * @param especialidades
	 * @return
	 */
	private ActionForward eliminarEspecialdades(Connection con, ActionMapping mapping, UsuarioBasico usuario, EspecialidadesForm forma, Especialidades especialidades, HttpServletRequest request) 
	{	
		ActionErrors errores = new ActionErrors();
		logger.info("Codigo Especialida Eliminar >>>>"+forma.getEspecialidades().get(forma.getPosEspecialidad()).getCodigo());
		if(forma.getEspecialidades().get(forma.getPosEspecialidad()).getIngresar().equals(ConstantesBD.acronimoSi))
		{
			forma.getEspecialidades().get(forma.getPosEspecialidad()).setEliminar(ConstantesBD.acronimoSi);
			forma.getEspecialidades().remove(forma.getPosEspecialidad());
			forma.setPosEspecialidad(ConstantesBD.codigoNuncaValido);
			forma.setTieneError(false);
			return mapping.findForward("empezar");
		}else{
			if(especialidades.deleteEspecialidades(con, forma.getEspecialidades().get(forma.getPosEspecialidad()).getCodigo())>0)
			{
				forma.getEspecialidades().get(forma.getPosEspecialidad()).setEliminar(ConstantesBD.acronimoSi);
				forma.getEspecialidades().remove(forma.getPosEspecialidad());
				forma.setPosEspecialidad(ConstantesBD.codigoNuncaValido);
				forma.setTieneError(false);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("empezar");
			}else{
				errores.add("consecutivo", new ActionMessage("errors.notEspecific","No se Puede Eliminar la Especialidad esta siendo Utilizada. "));
				UtilidadBD.closeConnection(con);
				forma.setTieneError(true);
				saveErrors(request, errores);
				return mapping.findForward("empezar");
			}
		}
	}
	
	/**
	 * Método para consultar las especialidades de una instituicion 
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param forma
	 * @param especialidades
	 * @return
	 */
	private ActionForward busquedaAvanzadaEspecialidades(Connection con, ActionMapping mapping, UsuarioBasico usuario, EspecialidadesForm forma, Especialidades especialidades) 
	{		
		// cargar especialidades - busqueda avanzada
		forma.getEspecialidades().clear();
		forma.setEspecialidades(especialidades.busquedaAvanzadaEspecialidades(con, usuario.getCodigoInstitucion(), 
				forma.getCodigoBus(), forma.getDescripcionBus(), Utilidades.convertirAEntero(forma.getCodigoCentroCostoBus())));
		forma.setEspecialidades(especialidades.verificarEspecialidadesUsadas(con, forma.getEspecialidades()));
		UtilidadBD.closeConnection(con);
		if(forma.getMostrarBusCon().equals(ConstantesBD.acronimoNo))
		{
			logger.info("valor: N");
			return mapping.findForward("empezar");
		}else{
			logger.info("valor: S");
			return mapping.findForward("resumen");
		}
	}
	
}
