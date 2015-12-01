package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

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
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.facturacion.ConsultaLogRipsEntidadesSubForm;
import com.princetonsa.dto.facturacion.DtoResultadoConsultaLogRipsEntidadesSub;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IUsuariosServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IEntidadesSubcontratadasServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.ILogRipsEntidadesSubcontratadasServicio;

public class ConsultaLogRipsEntidadesSubAction extends Action{

	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, 
	 * HttpServletRequest, HttpServletResponse)
	*/
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
								HttpServletResponse response )throws Exception
	{
		
		if(form instanceof ConsultaLogRipsEntidadesSubForm){
			
			ConsultaLogRipsEntidadesSubForm forma = (ConsultaLogRipsEntidadesSubForm)form;
			String estado = forma.getEstado(); 
			InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			UsuarioBasico usuario 	= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			if(estado == null)
			{
				request.setAttribute("codigoDescripcionError","errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}
			
			//----------- Empezar
			else if(estado.equals("empezar"))
			{
				this.accionEmpezar(forma,usuario, mapping, ins);
			
				return mapping.findForward("principal");
			}
			else if(estado.equals("consultar"))
			{
				return this.accionConsultar(forma,usuario, mapping, ins,request);
			}
			else if(estado.equals("abrirDetalle"))
			{
				this.accionAbrirDetalle(forma,usuario, mapping, ins,request);
				return mapping.findForward("detalle");
			}
			else if(estado.equals("abrirDetalleArchivo"))
			{
				this.accionAbrirDetallePorArchivo(forma,usuario, mapping, ins,request);
				return mapping.findForward("detallePorArchivo");
			}
			else if(estado.equals("ordenar"))
			{
				return this.accionOrdenar(forma, mapping);
			}
			
			
			
		}
		return null;
	}
	
	
	
	/**
	 * Este método se encarga de inicializar los valores de los objetos de la
	 * página de consulta rips de entidades subcontratadas
	 * @param forma
	 * @param usuario Usuario que genera el proceso
	 * @param mapping Para hacer redirrección a los JSP
	 * @param ins Institución del usuario en sessión
	 * 
	 * @author Fabian Becerra
	 */
	private void accionEmpezar(ConsultaLogRipsEntidadesSubForm forma, UsuarioBasico usuario,  
			ActionMapping mapping, InstitucionBasica ins){
		
		forma.reset();
		
		
		try {
			Connection con=UtilidadBD.abrirConexion();
			forma.setPath(Utilidades.obtenerPathFuncionalidad(con,
			        ConstantesBD.codigoFuncionalidadRipsEntidadesSubcontratadas));
			UtilidadBD.cerrarConexion(con);
		} catch (SQLException e) {
			Log4JManager.error(e);
		}
		try{
			HibernateUtil.beginTransaction();
			//cargar los usuarios.
			IUsuariosServicio usuarioServicio=AdministracionFabricaServicio.crearUsuariosServicio();
			forma.setUsuarios(usuarioServicio.obtenerUsuariosSistemas(usuario.getCodigoInstitucionInt(),false));
			
			//cargar las entidades subcontratadas
			IEntidadesSubcontratadasServicio servicioEntidadesSub=FacturacionServicioFabrica.crearEntidadesSubcontratadasServicio();
			forma.setListaEntidadesSub(servicioEntidadesSub.listarEntidadesSubActivasEnSistema());
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error(e);
			HibernateUtil.abortTransaction();
		}
		
	}
	
	/**
	 * Este método se encarga de realizar la consulta rips de entidades subcontratadas
	 * @param forma
	 * @param usuario Usuario que genera el proceso
	 * @param mapping Para hacer redirrección a los JSP
	 * @param ins Institución del usuario en sessión
	 * 
	 * @author Fabian Becerra
	 */
	private ActionForward accionConsultar(ConsultaLogRipsEntidadesSubForm forma, UsuarioBasico usuario,  
			ActionMapping mapping, InstitucionBasica ins, HttpServletRequest request){
		ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> listaLog=null;
		try{
			HibernateUtil.beginTransaction();
			ILogRipsEntidadesSubcontratadasServicio servicioLog=FacturacionServicioFabrica.crearLogRipsEntidadesSubServicio();
			listaLog=servicioLog.consultarRegistrosLogRipsEntidadesSub(forma.getDtoFiltroConsultaProcesoRips());
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error(e);
			HibernateUtil.abortTransaction();
		}
		if(Utilidades.isEmpty(listaLog)){
			ActionErrors errores=new ActionErrors();
            errores.add("No se encontraron resultados", new ActionMessage("errors.modFactConsultaLogRips"));
            saveErrors(request, errores);
            return mapping.findForward("principal");
		}
		 else{
	        	
			 for(DtoResultadoConsultaLogRipsEntidadesSub log:listaLog){
				 log.setUsuarioProceso((UtilidadTexto.isEmpty(log.getPrimerApellidoUsuarioProceso()))?"":log.getPrimerApellidoUsuarioProceso()
							+" "+((UtilidadTexto.isEmpty(log.getSegundoApellidoUsuarioProceso()))?"":log.getSegundoApellidoUsuarioProceso())
							+" "+((UtilidadTexto.isEmpty(log.getPrimerNombreUsuarioProceso()))?"":log.getPrimerNombreUsuarioProceso())
							+" "+((UtilidadTexto.isEmpty(log.getSegundoNombreUsuarioProceso()))?"":log.getSegundoNombreUsuarioProceso())
							+" ("+log.getLoginUsuario()+")");
			 }
			 forma.setListaLogRips(listaLog);
	         return mapping.findForward("resultado");
	        }
	}
	
	
	/**
	 * Este método se encarga de realizar la consulta de rips entidades subcontratadas
	 * para el log seleccionado
	 * @param forma
	 * @param usuario Usuario que genera el proceso
	 * @param mapping Para hacer redirrección a los JSP
	 * @param ins Institución del usuario en sessión
	 * 
	 * @author Fabian Becerra
	 */
	private void accionAbrirDetalle(ConsultaLogRipsEntidadesSubForm forma, UsuarioBasico usuario,  
			ActionMapping mapping, InstitucionBasica ins, HttpServletRequest request){
		
		ILogRipsEntidadesSubcontratadasServicio servicioLog=FacturacionServicioFabrica.crearLogRipsEntidadesSubServicio();
		forma.setLogDetalleOrdenado(servicioLog.ordenarRegistrosParaDetalleLogRips(forma.getCodigoPkLogSeleccionado()));
		
	}
	
	
	/**
	 * Este método se encarga de realizar la consulta de rips entidades subcontratadas
	 * para el log y archivo seleccionado
	 * @param forma
	 * @param usuario Usuario que genera el proceso
	 * @param mapping Para hacer redirrección a los JSP
	 * @param ins Institución del usuario en sessión
	 * 
	 * @author Fabian Becerra
	 */
	private void accionAbrirDetallePorArchivo(ConsultaLogRipsEntidadesSubForm forma, UsuarioBasico usuario,  
			ActionMapping mapping, InstitucionBasica ins, HttpServletRequest request){
		try{
			HibernateUtil.beginTransaction();
			ILogRipsEntidadesSubcontratadasServicio servicioLog=FacturacionServicioFabrica.crearLogRipsEntidadesSubServicio();
			forma.setLogDetallePorArchivo(servicioLog.ordenarRegistrosLogRipsEntidadesSubPorArchivo(forma.getCodigoPkLogSeleccionado(),forma.getCodigoPkLogArchivoSeleccionado()));
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error(e);
			HibernateUtil.abortTransaction();
		}
		
	}
	
	
	private ActionForward accionOrdenar(ConsultaLogRipsEntidadesSubForm forma,
			ActionMapping mapping) 
	{
		Log4JManager.info("patron->" + forma.getPatronOrdenar());
		Log4JManager.info("des -->" + forma.getEsDescendente() );
		
		boolean ordenamiento= false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente")){
			
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		
		Collections.sort(forma.getListaLogRips() ,sortG);
		return mapping.findForward("resultado");
	}

	
}
