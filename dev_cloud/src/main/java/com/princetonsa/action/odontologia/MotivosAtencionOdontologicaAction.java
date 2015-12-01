package com.princetonsa.action.odontologia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

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
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.actionform.odontologia.MotivosAtencionOdontologicaForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.MotivosAtencionOdontologica;
import com.princetonsa.sort.odontologia.SortGenerico;


public class MotivosAtencionOdontologicaAction extends Action{
	
	private Logger logger = Logger.getLogger(MotivosAtencionOdontologicaAction.class);
	
	public ActionForward execute(   ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response ) throws Exception
			{	
		Connection con = null;
		try{{


			//se obtiene el usuario cargado en sesion.
			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

			//se obtiene el paciente cargado en sesion.
			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

			//se obtiene la institucion
			InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

			//se instancia la forma
			MotivosAtencionOdontologicaForm forma = (MotivosAtencionOdontologicaForm)form;		

			//se instancia el mundo
			MotivosAtencionOdontologica mundo = new MotivosAtencionOdontologica();

			//optenemos el estado que contiene la forma.
			String estado = forma.getEstado();

			//se instancia la variable para manejar los errores.
			ActionErrors errores=new ActionErrors();

			//forma.setMensaje(new ResultadoBoolean(false));

			//se instancia la variable para manejar los errores.


			logger.info("\n\n***************************************************************************");
			logger.info(" 	  EL ESTADO DE MOTIVOS ATENCION ODONTOLOGICA ES ====>> "+estado);
			logger.info("\n***************************************************************************");

			// ESTADO --> NULL
			if(estado == null)
			{
				forma.reset(usuario.getCodigoInstitucionInt());
				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");

				return mapping.findForward("paginaError");
			}			
			else if(estado.equals("empezar"))
			{
				forma.resetMensaje();
				return this.accionEmpezar(con, forma, mundo, mapping, usuario,request);					   			
			}
			else if(estado.equals("nuevoRegistro"))
			{
				forma.setCodigoNuevo("");
				forma.setNombreNuevo("");
				forma.resetMensaje();
				forma.setTipoNuevo(ConstantesBD.codigoNuncaValido);
				return mapping.findForward("principal");
			}
			else if(estado.equals("guardarNuevoRegistro"))
			{
				return this.accionGuardarNuevoRegistro(con, forma, mundo, mapping, usuario,request);		
			}
			else if(estado.equals("modificarRegistro"))
			{
				forma.setCodigoPk(forma.getListaMotivosAtencion().get(forma.getIndiceModificar()).getCodigoPk());
				forma.setCodigoNuevo(forma.getListaMotivosAtencion().get(forma.getIndiceModificar()).getCodigo());
				forma.setNombreNuevo(forma.getListaMotivosAtencion().get(forma.getIndiceModificar()).getNombre());
				forma.setTipoNuevo(forma.getListaMotivosAtencion().get(forma.getIndiceModificar()).getTipo());
				return mapping.findForward("principal");
			}
			else if(estado.equals("guardarModificarRegistro"))
			{
				return this.accionGuardarModificarRegistro(con, forma, mundo, mapping, usuario,request);		
			}
			else if(estado.equals("eliminarRegistro"))
			{
				return this.accionEliminarRegistro(con, forma, mundo, mapping, usuario,request);	
			}
			else if (forma.getEstado().equals("ordenar")) 
			{
				forma.resetMensaje();
				return accionOrdenar(mapping, forma, usuario);
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
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionOrdenar(ActionMapping mapping,MotivosAtencionOdontologicaForm forma, UsuarioBasico usuario) 
	{	
		logger.info("patron->" + forma.getPatronOrdenar());
		logger.info("des -->" + forma.getEsDescendente() );
		
		boolean ordenamiento= false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente")){
			
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		
		Collections.sort(forma.getListaMotivosAtencion(),sortG);		
		return mapping.findForward("principal");
		
	}
	
	/**
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionEliminarRegistro(Connection con, MotivosAtencionOdontologicaForm forma, MotivosAtencionOdontologica mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{
		forma.setCodigoPk(forma.getListaMotivosAtencion().get(forma.getIndiceModificar()).getCodigoPk());
		if(mundo.eliminarMotivoAtencionO(forma.getCodigoPk()))
		{
			forma.setListaMotivosAtencion(mundo.consultarMotivoAtencionO(new ArrayList<Integer>(), usuario.getCodigoInstitucionInt()));
			forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
		}
		else
			forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));		
		
		return mapping.findForward("principal");
	}
	
	/**
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionGuardarModificarRegistro(Connection con, MotivosAtencionOdontologicaForm forma, MotivosAtencionOdontologica mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{				
		if(Utilidades.convertirAEntero(forma.getCodigoNuevo()) != forma.getListaMotivosAtencion().get(forma.getIndiceModificar()).getCodigoPk() ||
				!forma.getNombreNuevo().equals(forma.getListaMotivosAtencion().get(forma.getIndiceModificar()).getNombre()) ||
				forma.getTipoNuevo() != forma.getListaMotivosAtencion().get(forma.getIndiceModificar()).getTipo())
		{
			if(mundo.modificarMotivoAtencionO(forma.getCodigoPk(),forma.getCodigoNuevo(), forma.getNombreNuevo(), forma.getTipoNuevo(), usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario()))
			{				
				forma.setListaMotivosAtencion(mundo.consultarMotivoAtencionO(new ArrayList<Integer>(), usuario.getCodigoInstitucionInt()));
				forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
			}
			else
				forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));		
		}
		else
			forma.setMensaje(new ResultadoBoolean(true,"No hubo modificaciones."));		
		
		return mapping.findForward("principal");
	}
	
	/**
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionGuardarNuevoRegistro(Connection con, MotivosAtencionOdontologicaForm forma, MotivosAtencionOdontologica mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{
		if(mundo.insertarMotivoAtencionO(forma.getCodigoNuevo(), forma.getNombreNuevo(), forma.getTipoNuevo(), usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario()))
			forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
		else
			forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
		
		forma.setListaMotivosAtencion(mundo.consultarMotivoAtencionO(new ArrayList<Integer>(), usuario.getCodigoInstitucionInt()));
		
		return mapping.findForward("principal");
	}
	
	/**
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionEmpezar(Connection con, MotivosAtencionOdontologicaForm forma, MotivosAtencionOdontologica mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{
		forma.reset(usuario.getCodigoInstitucionInt());
		
		forma.setTiposMotivo(mundo.consultarTiposMotivo());		
		forma.setListaMotivosAtencion(mundo.consultarMotivoAtencionO(new ArrayList<Integer>(), usuario.getCodigoInstitucionInt()));
								
		return mapping.findForward("principal");
	}
}