package com.princetonsa.action.odontologia;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.actionform.odontologia.HallazgosOdontologicosForm;
import com.princetonsa.dto.odontologia.DtoHallazgoOdontologico;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.HallazgosOdontologicos;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.orm.TipoHallazgoCeoCop;
import com.servinte.axioma.orm.delegate.odontologia.TipoHallazgoCOPCEODelegate;
import com.servinte.axioma.persistencia.UtilidadTransaccion;




/**
 * Control de la funcionalidad de Hallazgos odontológicos
 * @author Jorge Andrés Ortiz
 * @version 1.1
 */
public class HallazgosOdontologicosAction extends Action{

	HallazgosOdontologicos mundo;
	
	public ActionForward execute(ActionMapping mapping,
			 ActionForm form, 
			 HttpServletRequest request, 
			 HttpServletResponse response) throws Exception
			 {
		Connection con = null;
		try{
			if (form instanceof HallazgosOdontologicosForm) 
			{			 

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("CodigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				//Usuario cargado en session
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				mundo = new HallazgosOdontologicos();

				HallazgosOdontologicosForm forma = (HallazgosOdontologicosForm)form;		
				String estado = forma.getEstado();		 

				Log4JManager.info("Valor del Estado    >> "+forma.getEstado());			 

				if(estado.equals("empezar"))
				{ 
					forma.reset();	  
					UtilidadBD.closeConnection(con); 
					TipoHallazgoCOPCEODelegate tipoHallazgo = new TipoHallazgoCOPCEODelegate();
					ArrayList<TipoHallazgoCeoCop> listaTiposHallazgo = tipoHallazgo.listarHallazgosCOPCEO();
					forma.setListaTiposHallazgo(listaTiposHallazgo);
					UtilidadTransaccion.getTransaccion().commit();
					return mapping.findForward("menuPrincipal");
				}
				else if(estado.equals("ingresarModificar"))
				{
					return accionEmpezarIngresarModificar(con, forma,usuario,request, mapping);
				}else if(estado.equals("consultar"))
				{
					forma.reset();
					return accionConsultarHallazgos(con, forma,usuario,request, mapping);
				}			
				else if(estado.equals("modificar"))
				{
					UtilidadBD.closeConnection(con);
					return cargarDtoHallazgoModificar(forma,usuario,request, mapping);
				}
				else if(estado.equals("guardarModificar"))
				{				
					return accionGuardarModificar(con,forma,usuario,request, mapping);
				}
				else if(estado.equals("eliminar"))
				{
					return accionEliminarConvencion(con, forma,usuario,request, mapping);			  
				}
				else if(estado.equals("nuevo"))
				{
					UtilidadBD.closeConnection(con);
					forma.resetNuevoHallazgo();
					return mapping.findForward("ingresarModificarHallazgos");
				}
				else if(estado.equals("guardarNuevo"))
				{		
					return accionGuardarNuevoHallazgo(con,forma,usuario,request, mapping);			  

				}else if(estado.equals("ordenarIM"))
				{
					UtilidadBD.closeConnection(con);
					return accionOrdenar(forma,request,mapping,"ingresarModificarHallazgos");
				}
				else if(estado.equals("ordenarC"))
				{
					UtilidadBD.closeConnection(con);
					return accionOrdenar(forma,request,mapping,"consultaHallazgos");
				}
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.closeConnection(con);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("cargarConsultarAvanzada"))
				{
					return accionEmpezarBusqueda(forma, mapping);
				}
				else if(estado.equals("busquedaHallazgosAvanzada"))
				{
					return this.accionbusquedaHallazgosAvanzada(forma, mapping, usuario);
				}
				else if(estado.equals("ordenaAvanzado"))
				{
					return this.accionOrdenarAvanzada(mapping, forma, usuario);
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
	 * Eliminar un Hallazgo 
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarConvencion(Connection con, HallazgosOdontologicosForm forma, UsuarioBasico usuario,HttpServletRequest request, ActionMapping mapping)
	{
		ActionErrors errores =new ActionErrors();
		int codigoHallazgo = Utilidades.convertirAEntero(((DtoHallazgoOdontologico)forma.getHallazgosDentales().get(forma.getPosHallazgo())).getConsecutivo()); 
	
		if(mundo.eliminarHallazgoOdontologico(codigoHallazgo))
		{   
			forma.reset();
			forma.setHallazgosDentales(mundo.consultarHallazgosDentales(con, usuario.getCodigoInstitucionInt()));
		}else
		{
			errores.add("descripcion",new ActionMessage("errors.notEspecific","No se puudo realizar el proceso de Eliminacion"));
		    saveErrors(request, errores);
		}		
		return mapping.findForward("ingresarModificarHallazgos");
	}

	/**
	 * Guardar las modificaciones realizadas a un Hallazgo
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionGuardarModificar(Connection con,HallazgosOdontologicosForm forma, UsuarioBasico usuario,HttpServletRequest request, ActionMapping mapping) {
		ActionErrors errores = new ActionErrors();
		boolean seModifico=false;	 
		DtoHallazgoOdontologico hallazgo = new DtoHallazgoOdontologico();
		hallazgo=(DtoHallazgoOdontologico)forma.getHallazgosDentales().get(forma.getPosHallazgo()); 
		
		String[] vector = forma.getDiagnosticoSel().split(ConstantesBD.separadorSplit); 
		String descripcionDiagnostico= vector[0]+"-"+vector[1]+" "+vector[2];
		String diagnostico=vector[0];
		String tipocie=vector[1];
		forma.getNuevoHallazgo().setDescripcionDiagnostico(descripcionDiagnostico);
		forma.getNuevoHallazgo().setDiagnostico(diagnostico);
		forma.getNuevoHallazgo().setTipo_cie(tipocie);
		
		// Se valida datos Requeridos y repetidos
		errores = mundo.validacionNuevosDatos(forma.getNuevoHallazgo(),forma.getHallazgosDentales(),forma.getPosHallazgo());
		
		if(errores.isEmpty())
		{
			int consecutivoHallazgo= Utilidades.convertirAEntero(hallazgo.getConsecutivo());
			// Se valida si se hicieron Modificaciones a la informacion de la Convencion
			seModifico = mundo.validarExistenciaCambios(forma.getNuevoHallazgo(),hallazgo);
			
			if(seModifico)
			{
				UtilidadBD.iniciarTransaccion(con);
				if(mundo.modificarHallazgoOdontologico(con,forma.getNuevoHallazgo(),consecutivoHallazgo,usuario.getLoginUsuario()))
				{
					UtilidadBD.finalizarTransaccion(con);
					forma.reset();
					forma.setHallazgosDentales(mundo.consultarHallazgosDentales(con, usuario.getCodigoInstitucionInt()));
				} 	
				else
				{
					forma.setEstado("modificar");
					UtilidadBD.abortarTransaccion(con);
				}
			}
		}
		else
		{
			forma.setEstado("modificar");
			saveErrors(request, errores);	
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ingresarModificarHallazgos");
	}



	/**
	 * Guardar un nuevo hallazgo
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionGuardarNuevoHallazgo(Connection con,HallazgosOdontologicosForm forma, UsuarioBasico usuario,HttpServletRequest request, ActionMapping mapping)
	{
		ActionErrors errores = new ActionErrors();
		
		String[] vector = forma.getDiagnosticoSel().split(ConstantesBD.separadorSplit); 
		
		if(vector.length>=3)
		{
			String descripcionDiagnostico= vector[0]+"-"+vector[1]+" "+vector[2];
			String diagnostico=vector[0];
			String tipocie=vector[1];
			forma.getNuevoHallazgo().setDescripcionDiagnostico(descripcionDiagnostico);
			forma.getNuevoHallazgo().setDiagnostico(diagnostico);
			forma.getNuevoHallazgo().setTipo_cie(tipocie);
		}
		
		errores = mundo.validacionNuevosDatos(forma.getNuevoHallazgo(),forma.getHallazgosDentales(),ConstantesBD.codigoNuncaValido);
		if(errores.isEmpty())
		{
			UtilidadBD.iniciarTransaccion(con);
			
			if(mundo.crearNuevoHallazgoOdontologico(con,forma.getNuevoHallazgo(),usuario.getLoginUsuario(),usuario.getCodigoInstitucionInt()))
			{
				UtilidadBD.finalizarTransaccion(con);
				forma.reset();
				forma.setHallazgosDentales(mundo.consultarHallazgosDentales(con, usuario.getCodigoInstitucionInt()));
			} 
			else
			{   
				forma.setEstado("nuevo");
				errores.add("descripcion",new ActionMessage("errors.notEspecific","No Fue Posible la Insercion del Nuevo Hallazgo"));
				saveErrors(request, errores);
				UtilidadBD.abortarTransaccion(con);
			}	
			
		}else
		{
			forma.setEstado("nuevo");
			saveErrors(request, errores);
		}
		return mapping.findForward("ingresarModificarHallazgos");
	}


	/**
	 * Inicia la funcionalidad consultando los hallazgos Odontológicos
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezarIngresarModificar(Connection con, HallazgosOdontologicosForm forma, UsuarioBasico usuario,HttpServletRequest request, ActionMapping mapping) {
		forma.setHallazgosDentales(mundo.consultarHallazgosDentales(con, usuario.getCodigoInstitucionInt()));	
		return mapping.findForward("ingresarModificarHallazgos");
	}
	
	
	/**
	 * Consultar los hallazgos 
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionConsultarHallazgos(Connection con, HallazgosOdontologicosForm forma, UsuarioBasico usuario,	HttpServletRequest request, ActionMapping mapping) {
		
		forma.setEstado("consultar");
		forma.setHallazgosDentales(mundo.consultarHallazgosDentales(con, usuario.getCodigoInstitucionInt()));	
		return mapping.findForward("consultaHallazgos");
	}
	
	/**
	 * Cargar información del hallazgo a modificar
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward cargarDtoHallazgoModificar(HallazgosOdontologicosForm forma, UsuarioBasico usuario,HttpServletRequest request, ActionMapping mapping) 
	{
		
        forma.resetNuevoHallazgo();		
        DtoHallazgoOdontologico dtoNuevo=new DtoHallazgoOdontologico();
		try 
		{		
			PropertyUtils.copyProperties(dtoNuevo,(DtoHallazgoOdontologico)forma.getHallazgosDentales().get(forma.getPosHallazgo()));
			
		}catch(Exception e){
			Log4JManager.error(e);
		}
		forma.setNuevoHallazgo(dtoNuevo);
		forma.getNuevoHallazgo().getMapaDiagnosticos().put("principal", forma.getNuevoHallazgo().getDiagnostico()+ConstantesBD.separadorSplit+forma.getNuevoHallazgo().getTipo_cie()+ConstantesBD.separadorSplit+forma.getNuevoHallazgo().getDescripcionDiagnostico());
		return mapping.findForward("ingresarModificarHallazgos");
	}
	
	
	/**
	 * Metodo para ordenar por columnas los Hallazgos
	 * @param forma
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(HallazgosOdontologicosForm forma,HttpServletRequest request, ActionMapping mapping, String estadoRedireccion) {
		
		 boolean ordenamiento= false;
			
			if(forma.getEsDescendente().equals(forma.getPatronOrdenar()))
			{
				forma.setEsDescendente(forma.getPatronOrdenar()+"descendente") ;
			}else{
				forma.setEsDescendente(forma.getPatronOrdenar());
			}	
			
			if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente")){
				
				ordenamiento = true;
			}
			
			SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
			Collections.sort(forma.getHallazgosDentales(),sortG);
			return mapping.findForward(estadoRedireccion);
	}
	
	/**
	 * CARGAR BUSQUEDA AVANZDA DE HALLAZGOS ODONTOLOGICOS
	 * @param forma
	 * @return
	 */
	private ActionForward accionEmpezarBusqueda(HallazgosOdontologicosForm forma, ActionMapping mapping)
	{
		forma.setEstado("empezarConsulta");
		forma.getDtoHallazgoOdontologico().setCodigo("");
		forma.getDtoHallazgoOdontologico().setNombre("");
		forma.setListaHallazgos(new ArrayList<DtoHallazgoOdontologico>()); //LIMPIAR LISTA DE HALLAZGOS
		return mapping.findForward("consultaAvanzadaHallazgos");
	}
	
	/**
	 * CONSULSTAR HALLAGOSA CON BUSQUEDA AVANZADA 
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionbusquedaHallazgosAvanzada(HallazgosOdontologicosForm forma, ActionMapping mapping , UsuarioBasico usuario)
	{
		forma.getDtoHallazgoOdontologico().setInstitucion(usuario.getCodigoInstitucionInt());
		forma.getDtoHallazgoOdontologico().setActivo(ConstantesBD.acronimoSi);
		forma.getDtoHallazgoOdontologico().setYaFueSeleccionado(forma.getCodigoHallazgos());
		forma.setListaHallazgos(HallazgosOdontologicos.busquedaAvanzadaHallazgos(forma.getDtoHallazgoOdontologico()));
		
		return mapping.findForward("consultaAvanzadaHallazgos");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionOrdenarAvanzada(ActionMapping mapping,
			HallazgosOdontologicosForm forma, UsuarioBasico usuario) {
			
		boolean ordenamiento= false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente")){
			
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		
		Collections.sort(forma.getListaHallazgos(),sortG);
		return mapping.findForward("consultaAvanzadaHallazgos");
		
	}
	
}
