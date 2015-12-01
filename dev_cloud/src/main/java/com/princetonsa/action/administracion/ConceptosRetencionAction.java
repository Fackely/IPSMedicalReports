package com.princetonsa.action.administracion;

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
import util.Utilidades;

import com.princetonsa.actionform.administracion.ConceptosRetencionForm;
import com.princetonsa.dto.administracion.DtoConceptosRetencion;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.administracion.ConceptosRetencion;

/**
 * 
 * @author Angela María Angel amangel@axioma-md.com
 *
 */

public class ConceptosRetencionAction extends Action{
	
	private Logger logger = Logger.getLogger(ConceptosRetencionAction.class);
	
	public ActionForward execute(   ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response ) throws Exception
	{	
		Connection con = null;
		try{
		if(form instanceof ConceptosRetencionForm)
		{
			
			
			//se obtiene el usuario cargado en sesion.
			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			//se obtiene el paciente cargado en sesion.
			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			
			//se obtiene la institucion
			InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			//se instancia la forma
			ConceptosRetencionForm forma = (ConceptosRetencionForm)form;		
			
			//se instancia el mundo
			ConceptosRetencion mundo = new ConceptosRetencion();
			
			//optenemos el estado que contiene la forma.
			String estado = forma.getEstado();
			
			//se instancia la variable para manejar los errores.
			ActionErrors errores=new ActionErrors();
			
			forma.setMensaje(new ResultadoBoolean(false));
			
			//se instancia la variable para manejar los errores.
			
			
			logger.info("\n\n***************************************************************************");
			logger.info(" 	  EL ESTADO DE CONCEPTOS RETENCION ES ====>> "+estado);
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
				return this.accionEmpezar(con, forma, mundo, mapping, usuario,request);					   			
			}
			else if(estado.equals("nuevoRegistro"))
			{
				return this.accionNuevoRegistro(con, forma, mundo, mapping, usuario,request);					   			
			}	
			else if(estado.equals("seleccionTipoRet"))
			{
				return this.accionSeleccionTipoRet(con, forma, mundo, mapping, usuario,request);
			}
			else if(estado.equals("guardarNuevoReg"))
			{
				return this.accionGuardarNuevoReg(con, forma, mundo, mapping, usuario,request);
			}
			else if(estado.equals("detalleCuenta"))
			{
				return this.accionDetalleCuenta(con, forma, mundo, mapping, usuario,request);
			}
			else if(estado.equals("detalleCuentasModificar"))
			{
				return this.accionDetalleCuentasModificar(con, forma, mundo, mapping, usuario,request);
			}
			else if(estado.equals("modificarRegistro"))
			{
				return this.accionModificarRegistro(con, forma, mundo, mapping, usuario,request);
			}
			else if(estado.equals("guardarModificar"))
			{
				return this.accionGuardarModificar(con, forma, mundo, mapping, usuario,request);
			}
			else if(estado.equals("nuevo"))
			{
				return mapping.findForward("principal");
			}
			else if(estado.equals("modificar"))
			{
				return mapping.findForward("principal");
			}
			else if(estado.equals("eliminarRegistro"))
			{
				return this.accionEliminarRegistro(con, forma, mundo, mapping, usuario,request);
			}
			else if(estado.equals("updateValues"))
			{
				String codCuentaRet=request.getParameter("cuentaRet");
				String codCuentaDB=request.getParameter("cuentaDB");
				String codCuentaCR=request.getParameter("cuentaCR");
				forma.setCuentaRet(codCuentaRet);
				forma.setCuentaDB(codCuentaDB);
				forma.setCuentaCR(codCuentaCR);
				return null;
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
     * Inicia en el forward de Conceptos Retencion
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionEliminarRegistro(Connection con, ConceptosRetencionForm forma, ConceptosRetencion mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{	
		int detalle=0;
		int i=Utilidades.convertirAEntero(forma.getIndiceDetalle());
		
		
		ActionErrors errores = new ActionErrors();
		
		DtoConceptosRetencion nuevo= (DtoConceptosRetencion)forma.getListaConceptosRetencion().get(i);
		
		detalle=mundo.consultarVigDetConcepto(Utilidades.convertirAEntero(nuevo.getConsecutivo()));
		
		if(detalle > 0)
			errores.add("descripcion",new ActionMessage("prompt.generico","No es posible eliminar el concepto."));
		
		if(!errores.isEmpty())		
			saveErrors(request,errores);
		else
		{	
			if(mundo.eliminarConceptoRetencion(usuario.getLoginUsuario(), Utilidades.convertirAEntero(nuevo.getConsecutivo())))
				forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
			else
				forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
			
			forma.setListaConceptosRetencion(mundo.consultarConceptosRetencion(usuario.getCodigoInstitucionInt(),0));
		}
		
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
	private ActionForward accionGuardarModificar(Connection con, ConceptosRetencionForm forma, ConceptosRetencion mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{	
		int i=Utilidades.convertirAEntero(forma.getIndiceDetalle());
		
		boolean guardo=false;
		
		ActionErrors errores = new ActionErrors();
		
		for(int j=0; j<forma.getListaConceptosRetencion().size();j++)
		{
			if(forma.getListaConceptosRetencion().get(i).getActivo().equals(ConstantesBD.acronimoSi) && Utilidades.convertirAEntero(forma.getListaConceptosRetencion().get(j).getConsecutivo()) != Utilidades.convertirAEntero(forma.getConsecutivoConceptoRet()))
			{
				if(Utilidades.convertirAEntero(forma.getListaConceptosRetencion().get(j).getCodigoConcepto()) == Utilidades.convertirAEntero(forma.getCodConcepto()))
					errores.add("descripcion",new ActionMessage("prompt.generico","El Codigo de Concepto ya fue Asignado."));
			}
		}
		
		
		if(!errores.isEmpty())		
		{
			forma.setEstado("modificarRegistro");
			saveErrors(request,errores);
		}
		else
		{	
			DtoConceptosRetencion nuevo= (DtoConceptosRetencion)forma.getListaConceptosRetencion().get(i);
			
			guardo=mundo.insertarLogConceptoRetencion(Utilidades.convertirAEntero(nuevo.getConsecutivo()), nuevo.getTipoRetencionConsecutivo(), nuevo.getCodigoConcepto(), nuevo.getDescripcion(), nuevo.getCodigoInterfaz(), nuevo.getCuentaRet(), nuevo.getCuentaDBautoRet(), nuevo.getCuentaCRautoRet(), usuario.getLoginUsuario());
						
			if(guardo && mundo.actualizarConceptoRetencion(forma.getConseTipoRet(), forma.getCodConcepto(), forma.getDescConcepto(), forma.getCodInterfaz(), forma.getCuentaRet(), forma.getCuentaDB(), forma.getCuentaCR(), usuario.getLoginUsuario(), Utilidades.convertirAEntero(forma.getConsecutivoConceptoRet())))
				forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
			else
				forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
			
			forma.setListaConceptosRetencion(mundo.consultarConceptosRetencion(usuario.getCodigoInstitucionInt(),0));
		}
		
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
	private ActionForward accionModificarRegistro(Connection con, ConceptosRetencionForm forma, ConceptosRetencion mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{	
		int i=Utilidades.convertirAEntero(forma.getIndiceDetalle());
		int detalle=0;
		
		DtoConceptosRetencion nuevo= (DtoConceptosRetencion)forma.getListaConceptosRetencion().get(i);
							
		forma.setConseTipoRet(nuevo.getTipoRetencionConsecutivo());
		forma.setCuentaRet(nuevo.getCuentaRet());
		forma.setCuentaDB(nuevo.getCuentaDBautoRet());
		forma.setCuentaCR(nuevo.getCuentaCRautoRet());
		forma.setConsecutivoTipoRet(nuevo.getTipoRet().getCodigo()+"");
		forma.setCodConcepto(nuevo.getCodigoConcepto());
		forma.setCodInterfaz(nuevo.getCodigoInterfaz());
		forma.setDescConcepto(nuevo.getDescripcion());
		forma.setConsecutivoConceptoRet(nuevo.getConsecutivo());
		forma.setDescripcion(nuevo.getTipoRet().getCodigo()+" "+nuevo.getTipoRet().getDescripcion()+" "+nuevo.getTipoRet().getSigla()+" "+nuevo.getTipoRet().getCodigoInterfaz());
		
		detalle=mundo.consultarDetalleConcepto(nuevo.getTipoRetencionConsecutivo());
		
		if(detalle > 0)
			forma.setModificarTipoRet("f");
		else
			forma.setModificarTipoRet("t");
		
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
	private ActionForward accionDetalleCuentasModificar(Connection con, ConceptosRetencionForm forma, ConceptosRetencion mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{	
		int cuenta= 0;
				
		if(forma.getModificar().equals("t"))
		{
			forma.setConsulta("f");
		}
				
		cuenta=Utilidades.convertirAEntero(forma.getCuentaRet());
		forma.setDescCuentaRet(mundo.consultarCuentaContable(cuenta));
		
		cuenta=Utilidades.convertirAEntero(forma.getCuentaDB());
		forma.setDescCuentaDB(mundo.consultarCuentaContable(cuenta));
		
		cuenta=Utilidades.convertirAEntero(forma.getCuentaCR());
		forma.setDescCuentaCR(mundo.consultarCuentaContable(cuenta));	
		
		return mapping.findForward("detalleCuentas");
	}
	
	/**
     * Inicia en el forward de Conceptos Retencion
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionDetalleCuenta(Connection con, ConceptosRetencionForm forma, ConceptosRetencion mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{	
		int i=Utilidades.convertirAEntero(forma.getIndiceDetalle());
				
		if(forma.getConsulta().equals("t"))
		{
			DtoConceptosRetencion nuevo= (DtoConceptosRetencion)forma.getListaConceptosRetencion().get(i);
											
			forma.setCuentaRet(nuevo.getCuentaRet());
			forma.setCuentaDB(nuevo.getCuentaDBautoRet());
			forma.setCuentaCR(nuevo.getCuentaCRautoRet());					
		}
		
		int cuenta= 0;
		
		cuenta=Utilidades.convertirAEntero(forma.getCuentaRet());
		forma.setDescCuentaRet(mundo.consultarCuentaContable(cuenta));
		
		cuenta=Utilidades.convertirAEntero(forma.getCuentaDB());
		forma.setDescCuentaDB(mundo.consultarCuentaContable(cuenta));
		
		cuenta=Utilidades.convertirAEntero(forma.getCuentaCR());
		forma.setDescCuentaCR(mundo.consultarCuentaContable(cuenta));
		
		return mapping.findForward("detalleCuentas");
	}
	
	/**
     * Inicia en el forward de Conceptos Retencion
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionGuardarNuevoReg(Connection con, ConceptosRetencionForm forma, ConceptosRetencion mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{					
		ActionErrors errores = new ActionErrors();
		
		for(int i=0; i<forma.getListaConceptosRetencion().size();i++)
		{
			if(forma.getListaConceptosRetencion().get(i).getActivo().equals(ConstantesBD.acronimoSi))
			{
				if(Utilidades.convertirAEntero(forma.getListaConceptosRetencion().get(i).getCodigoConcepto()) == Utilidades.convertirAEntero(forma.getCodConcepto()))
					errores.add("descripcion",new ActionMessage("prompt.generico","El Codigo de Concepto ya fue Asignado."));
			}
		}
		
		if(!errores.isEmpty())		
		{
			forma.setEstado("nuevoRegistro");
			saveErrors(request,errores);
		}
		else
		{	
			if(mundo.insertarConceptoRetencion(Utilidades.convertirAEntero(forma.getConsecutivoTipoRet()), forma.getCodConcepto(), usuario.getCodigoInstitucionInt(), forma.getDescConcepto(), forma.getCodInterfaz(), forma.getCuentaRet(), forma.getCuentaDB(), forma.getCuentaCR(), usuario.getLoginUsuario()))
				forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
			else
				forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
			
			forma.setListaConceptosRetencion(mundo.consultarConceptosRetencion(usuario.getCodigoInstitucionInt(),0));
		}
		
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
	private ActionForward accionSeleccionTipoRet(Connection con, ConceptosRetencionForm forma, ConceptosRetencion mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{	
		forma.setListaTiposRetencion(mundo.consultarTiposRetencion(usuario.getCodigoInstitucionInt()));
		forma.setNumeroRegTipoRet(forma.getListaTiposRetencion().size());
		
		return mapping.findForward("tipoRet");
	}	
	
	/**
     * Inicia en el forward de Conceptos Retencion
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionNuevoRegistro(Connection con, ConceptosRetencionForm forma, ConceptosRetencion mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{
		forma.setConsecutivoTipoRet("");
		forma.setCodConcepto("");
		forma.setDescConcepto("");
		forma.setCodInterfaz("");
		forma.setCuentaRet("");
		forma.setCuentaDB("");
		forma.setCuentaCR("");
		forma.setDescripcion("");		
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
	private ActionForward accionEmpezar(Connection con, ConceptosRetencionForm forma, ConceptosRetencion mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{
		forma.reset(usuario.getCodigoInstitucionInt());
				
		forma.setListaConceptosRetencion(mundo.consultarConceptosRetencion(usuario.getCodigoInstitucionInt(),0));
				
		return mapping.findForward("principal");
	}
}