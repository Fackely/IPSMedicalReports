package com.princetonsa.action.consultaExterna;

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

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadBD;

import com.princetonsa.actionform.consultaExterna.MotivosCitaForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.consultaExterna.MotivosCita;

public class MotivosCitaAction extends Action
{
	private Logger logger = Logger.getLogger(MotivosCitaAction.class);
	
	public ActionForward execute(   ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response ) throws Exception
			{	

		Connection con = null;

		try {

			if(form instanceof MotivosCitaForm)
			{


				//se obtiene el usuario cargado en sesion.
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				//se obtiene el paciente cargado en sesion.
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

				//se obtiene la institucion
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

				//se instancia la forma
				MotivosCitaForm forma = (MotivosCitaForm)form;		

				//se instancia el mundo
				MotivosCita mundo = new MotivosCita();

				//optenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				//se instancia la variable para manejar los errores.
				ActionErrors errores=new ActionErrors();

				forma.setMensaje(new ResultadoBoolean(false));

				//se instancia la variable para manejar los errores.


				logger.info("\n\n***************************************************************************");
				logger.info(" 	  EL ESTADO DE MOTIVOS CITA ES ====>> "+estado);
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
					return this.accionEmpezar(forma, mundo, mapping, usuario,request);					   			
				}
				else if(estado.equals("nuevoRegistro"))
				{
					forma.setCodigo("");
					forma.setDescripcion("");
					forma.setActivo(ConstantesBD.acronimoSi);
					// se adiciona campo tipo motivo
					forma.setTipoMotivo("");
					return mapping.findForward("principal");
				}
				else if(estado.equals("guardarNuevoRegistro"))
				{
					return this.accionGuardarNuevoRegistro(forma, mundo, mapping, usuario,request);		
				}
				else if(estado.equals("modificarRegistro"))
				{
					forma.setCodigo(forma.getListaMotivosCita().get(forma.getIndiceModificar()).getCodigo());
					forma.setDescripcion(forma.getListaMotivosCita().get(forma.getIndiceModificar()).getDescripcion());
					forma.setActivo(forma.getListaMotivosCita().get(forma.getIndiceModificar()).getActivo());
					forma.setCodigoPk(forma.getListaMotivosCita().get(forma.getIndiceModificar()).getCodigoPk());
					// se adiciona campo tipo motivo
					forma.setTipoMotivo(forma.getListaMotivosCita().get(forma.getIndiceModificar()).getTipoMotivo());
					logger.info("va a insertar en el update "+ forma.getListaMotivosCita().get(forma.getIndiceModificar()).getTipoMotivo() );




					return mapping.findForward("principal");
				}
				else if(estado.equals("guardarModificar"))
				{
					return this.accionGuardarModificar(forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("eliminarRegistro"))
				{
					return this.accionEliminarRegistro(forma, mundo, mapping, usuario,request);
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
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionEliminarRegistro(MotivosCitaForm forma, MotivosCita mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{	
		if(mundo.insertarLogMotivoCita(forma.getListaMotivosCita().get(forma.getIndiceModificar()).getCodigo(),forma.getListaMotivosCita().get(forma.getIndiceModificar()).getDescripcion(),
				forma.getListaMotivosCita().get(forma.getIndiceModificar()).getActivo(),ConstantesBD.acronimoSi,usuario.getLoginUsuario(),forma.getListaMotivosCita().get(forma.getIndiceModificar()).getCodigoPk(),forma.getListaMotivosCita().get(forma.getIndiceModificar()).getTipoMotivo()))
		{
			if(mundo.eliminarMotivoCita(forma.getListaMotivosCita().get(forma.getIndiceModificar()).getCodigoPk()))
				forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
			else
				forma.setMensaje(new ResultadoBoolean(true,"El Registro no se Actualizï¿½ Satisfactoriamente."));
			forma.setListaMotivosCita(mundo.consultarMotivosCita());
		}
		else
			forma.setMensaje(new ResultadoBoolean(true,"El Registro no se Actualizó Satisfactoriamente."));
		
		return mapping.findForward("principal");
	}
	
	/**
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionGuardarModificar(MotivosCitaForm forma, MotivosCita mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{	
		ActionErrors errores= new ActionErrors();
		int numReg=forma.getListaMotivosCita().size();
		if(forma.getListaMotivosCita().get(forma.getIndiceModificar()).getCodigo().equals(forma.getCodigo()) && 
			forma.getListaMotivosCita().get(forma.getIndiceModificar()).getDescripcion().equals(forma.getDescripcion()) &&
			//se adiciona campo tipo motivo
			forma.getListaMotivosCita().get(forma.getIndiceModificar()).getTipoMotivo().equals(forma.getTipoMotivo()) &&
			forma.getListaMotivosCita().get(forma.getIndiceModificar()).getActivo().equals(forma.getActivo()))
		{
					forma.setMensaje(new ResultadoBoolean(true,"No se realizaron modificaciones"));
					
		}
		
		else
		{
			
			
			for(int i=0;i<numReg;i++)
			{
				if(forma.getListaMotivosCita().get(i).getCodigo().equals(forma.getCodigo())&&i!=forma.getIndiceModificar())
					errores.add("descripcion",new ActionMessage("prompt.generico","El codigo "+forma.getCodigo()+" fue insertado anteriormente."));
				if(forma.getListaMotivosCita().get(i).getDescripcion().equals(forma.getDescripcion())&&i!=forma.getIndiceModificar())
					errores.add("descripcion",new ActionMessage("prompt.generico","La Descripcion "+forma.getDescripcion()+" fue insertado anteriormente."));
			}
			
			if(!errores.isEmpty())
				saveErrors(request, errores);
			else
			{		
				if(mundo.insertarLogMotivoCita(forma.getListaMotivosCita().get(forma.getIndiceModificar()).getCodigo(),forma.getListaMotivosCita().get(forma.getIndiceModificar()).getDescripcion(),
						forma.getListaMotivosCita().get(forma.getIndiceModificar()).getActivo(),ConstantesBD.acronimoNo,usuario.getLoginUsuario(),forma.getListaMotivosCita().get(forma.getIndiceModificar()).getCodigoPk(),forma.getListaMotivosCita().get(forma.getIndiceModificar()).getTipoMotivo()))
				{
					if(mundo.modificarMotivoCita(forma.getCodigo(),forma.getDescripcion(),forma.getActivo(),forma.getCodigoPk(),usuario.getLoginUsuario(),forma.getTipoMotivo()))
						forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
					else
						forma.setMensaje(new ResultadoBoolean(true,"El Registro no se Actualizó Satisfactoriamente."));
					forma.setListaMotivosCita(mundo.consultarMotivosCita());
				}
				else
					forma.setMensaje(new ResultadoBoolean(true,"El Registro no se Actualizó Satisfactoriamente."));
			}
		}
		
		return mapping.findForward("principal");
	}
	
	/**
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionGuardarNuevoRegistro(MotivosCitaForm forma, MotivosCita mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{		
		ActionErrors errores= new ActionErrors();
		int numReg=forma.getListaMotivosCita().size();
		int codigoPk=0;	
		
		for(int i=0;i<numReg;i++)
		{
			
		
			
			if(forma.getListaMotivosCita().get(i).getCodigo().equals(forma.getCodigo())&&forma.getListaMotivosCita().get(i).getCodigoPk()!=forma.getCodigoPk())
				errores.add("descripcion",new ActionMessage("prompt.generico","El codigo "+forma.getCodigo()+" fue insertado anteriormente."));
			if(forma.getListaMotivosCita().get(i).getDescripcion().equals(forma.getDescripcion())&&forma.getListaMotivosCita().get(i).getCodigoPk()!=forma.getCodigoPk())
				errores.add("descripcion",new ActionMessage("prompt.generico","La Descripcion "+forma.getDescripcion()+" fue insertado anteriormente."));
		}
		
		if(!errores.isEmpty())
		{
			forma.setEstado("nuevoRegistro");
			saveErrors(request, errores);
		
		}
		else
		{
			codigoPk= mundo.guardarMotivoCita(forma.getCodigo(),forma.getDescripcion(),forma.getActivo(),usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario(),forma.getTipoMotivo());
		
			if(codigoPk > 0){
				forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
				
			}else
				forma.setMensaje(new ResultadoBoolean(true,"El Registro no se Insertó Satisfactoriamente."));
			
			forma.setListaMotivosCita(mundo.consultarMotivosCita());
		}
		
		
		logger.info("llega al return");
		return mapping.findForward("principal");
		
	}
	
	/**
     * Inicia en el forward de Conceptos Retencion
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionEmpezar(MotivosCitaForm forma, MotivosCita mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{		
		forma.reset(usuario.getCodigoInstitucionInt());		
		
		forma.setListaMotivosCita(mundo.consultarMotivosCita());
		
		return mapping.findForward("principal");
	}
}