package com.princetonsa.action.consultaExterna;

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
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.Administracion.UtilidadesAdministracion;

import com.princetonsa.actionform.consultaExterna.ServiAdicionalesXProfAtenOdontoForm;
import com.princetonsa.dto.consultaExterna.DtoServiciosAdicionalesProfesionales;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.consultaExterna.ServiAdicionalesXProfAtenOdonto;
import com.princetonsa.sort.odontologia.SortGenerico;


public class ServiAdicionalesXProfAtenOdontoAction extends Action{
 
	private static Logger logger = Logger.getLogger(ServiAdicionalesXProfAtenOdontoAction.class);
    private ServiAdicionalesXProfAtenOdonto mundo;
	

	public ActionForward execute(ActionMapping mapping,
			 ActionForm form, 
			 HttpServletRequest request, 
			 HttpServletResponse response) throws Exception
			 {

		Connection con = null;

		try {

			if(response == null);

			if (form instanceof ServiAdicionalesXProfAtenOdontoForm) 
			{			 

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("CodigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				//Usuario cargado en session
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				//paciente cargado en sesion 
				PersonaBasica paciente= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				//Institucion
				InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

				mundo = new ServiAdicionalesXProfAtenOdonto();

				ServiAdicionalesXProfAtenOdontoForm forma = (ServiAdicionalesXProfAtenOdontoForm)form;		
				String estado = forma.getEstado();		 

				logger.info("-------------------------------------");
				logger.info("Valor del Estado    >> "+forma.getEstado());			 
				logger.info("-------------------------------------");
				logger.info("-------------------------------------");

				if(estado.equals("empezar"))
				{ 
					forma.reset();	  
					UtilidadBD.closeConnection(con); 
					return mapping.findForward("menuPrincipal");

				}else if(estado.equals("ingresarModificar"))
				{
					forma.reset();
					forma.setFlujo("modificar");
					return accionEmpezarIngresarModificar(con,forma,usuario,request, mapping);				

				}else if(estado.equals("consultar"))
				{
					forma.reset();
					forma.setFlujo("consultar");
					return accionConsultar(con,forma,usuario,request, mapping);				

				}
				else if(estado.equals("buscarporProfesional"))
				{	
					forma.resetMensajeExito();
					return accionConsultarServiciosAdicionales(con,forma,usuario,request, mapping);

				}else if (estado.equals("consultaporProfesional"))
				{
					return accionConsultaServicios(con,forma,usuario,request,mapping);

				}else if(estado.equals("agregarServicio"))
				{
					forma.resetMensajeExito(); 
					return accionAgregarServicio(con, forma, usuario,mapping);

				}else if (estado.equals("eliminarServicio"))
				{   
					forma.resetMensajeExito(); 
					return accionEliminarServicio(con,forma,mapping,usuario);

				}else if (estado.equals("guardar"))
				{
					return accionGuardar(con, forma,usuario,mapping,request,paciente);

				}else if(estado.equals("eliminarAsig"))
				{  
					UtilidadBD.closeConnection(con); 
					return accionEliminarServicioAsignado(forma,usuario,mapping,request);

				}else if (estado.equals("ordenar"))
				{
					UtilidadBD.closeConnection(con); 
					return accionOrdenar(forma, mapping, request);
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
	 * 
	 * @param forma
	 * @param mapping
	 * @param request
	 * @return
	 */
    private ActionForward accionOrdenar(ServiAdicionalesXProfAtenOdontoForm forma, ActionMapping mapping,HttpServletRequest request) {
		
    	 boolean ordenamiento= false;
			
			if(forma.getEsDescendente().equals(forma.getPatronOrdenar()))
			{
				forma.setEsDescendente(forma.getPatronOrdenar()+"descendente") ;
			}else{
				forma.setEsDescendente(forma.getPatronOrdenar());
			}	
			
			logger.info("patron ORDENAR-> " + forma.getPatronOrdenar());
			logger.info("DESCENDENTE --> " + forma.getEsDescendente() );
			
			if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente")){
				
				ordenamiento = true;
			}
			
			SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
			Collections.sort(forma.getListServiciosAdicionales(),sortG);
			if(forma.getFlujo().equals("consultar"))
			{
			  return mapping.findForward("consultaServiciosAdd");
			}
			  else
			  {
				  return mapping.findForward("empezarIngresarModificarServ");
			  }
			

	}



	/**
     * Metodo para realizar ( por el flujo de Consulta ) la consulta de los Servicios Adicionales asociados a un Profesional
     * @param con
     * @param forma
     * @param usuario
     * @param request
     * @param mapping
     * @return
     */
	private ActionForward accionConsultaServicios(Connection con,ServiAdicionalesXProfAtenOdontoForm forma, UsuarioBasico usuario,HttpServletRequest request, ActionMapping mapping) {
		
		int codProfesional= Utilidades.convertirAEntero(forma.getCodProfesionalSel());
		int codEstandar = Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt()));
		forma.setListServiciosAdicionales(mundo.consultarServiciosAdicionalesProfesionales(codProfesional,codEstandar));
		UtilidadBD.closeConnection(con); 
		return mapping.findForward("consultaServiciosAdd");	
	}


	/**
	 * Metodo para Inicializar el flujo de Consulta 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionConsultar(Connection con,ServiAdicionalesXProfAtenOdontoForm forma, UsuarioBasico usuario,HttpServletRequest request, ActionMapping mapping) {
		
		forma.setListProfesionales(UtilidadesAdministracion.obtenerProfesionales(con, usuario.getCodigoInstitucionInt(), -1, false, true, ConstantesBD.codigoNuncaValido));
		UtilidadBD.closeConnection(con); 
		return mapping.findForward("consultaServiciosAdd");
	}


	/**
	 * Metodo para eliminar un Servicio Adicional ya existente 
	 * @param forma
	 * @param mapping
	 * @param request
	 * @return
	 */
   private ActionForward accionEliminarServicioAsignado(ServiAdicionalesXProfAtenOdontoForm forma,UsuarioBasico usuario, ActionMapping mapping,HttpServletRequest request) {
		
	   ActionErrors errores =new ActionErrors();
	   int codigoServicio = Utilidades.convertirAEntero(((DtoServiciosAdicionalesProfesionales)forma.getListServiciosAdicionales().get(forma.getPosServicioAdd())).getCodigoServicio()); 
	   int codigoProfesional=Utilidades.convertirAEntero(((DtoServiciosAdicionalesProfesionales)forma.getListServiciosAdicionales().get(forma.getPosServicioAdd())).getCodigoMedico());
	   int codInstitucion=Utilidades.convertirAEntero(((DtoServiciosAdicionalesProfesionales)forma.getListServiciosAdicionales().get(forma.getPosServicioAdd())).getInstitucion());
	   int codEstandar = Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt()));
	   
		if(mundo.eliminarServicioExistente(codigoServicio,codigoProfesional,codInstitucion))
		{
			forma.resetListServicios();
			forma.setListServiciosAdicionales(mundo.consultarServiciosAdicionalesProfesionales(codigoProfesional,codEstandar));	
			forma.setListServiciosSel("numRegistros", "0");			
			forma.setListServiciosSel("codigosServiciosInsertados", mundo.obtenerCodigosServicios(forma.getListServiciosAdicionales()));
			forma.getMensajeExito().put("operacionExitosa", ConstantesBD.acronimoSi);
			forma.getMensajeExito().put("mensaje", "Proceso Exitoso!!! Se eliminó correctamente el Servicio");
			
		}else
		{
			errores.add("descripcion",new ActionMessage("errors.notEspecific","No se pudo realizar el proceso de Eliminación"));
		    saveErrors(request, errores);
		}
		
		 
		return mapping.findForward("empezarIngresarModificarServ");
	}


	/**
     * Metodo para realizar la Insercion de un servicio Adicional a un profesional
     * @param con
     * @param forma
     * @param usuario
     * @param mapping
     * @param request
     * @param paciente
     * @return
     */
    private ActionForward accionGuardar(Connection con,ServiAdicionalesXProfAtenOdontoForm forma, UsuarioBasico usuario,ActionMapping mapping, HttpServletRequest request,PersonaBasica paciente) {
		
    	forma.resetMensajeExito();
    	ActionErrors errores = new ActionErrors();
    	int codProfesional= Utilidades.convertirAEntero(forma.getCodProfesionalSel());	
    	int codEstandar = Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt()));
		String codigosInsertados="";
    	errores = mundo.validarDatosNuevos(forma.getCodProfesionalSel(),forma.getListServiciosSel(),forma.getListServiciosAdicionales());
    
    if(errores.isEmpty())
	  {
		UtilidadBD.iniciarTransaccion(con);
		
			if(mundo.insertarServiciosAdicionales(con, forma.getListServiciosSel(),codProfesional, usuario.getLoginUsuario(),usuario.getCodigoInstitucionInt()))
			{
				UtilidadBD.finalizarTransaccion(con);
				forma.getMensajeExito().put("operacionExitosa", ConstantesBD.acronimoSi);
				forma.getMensajeExito().put("mensaje", "Proceso Exitoso!!! El servicio se insertó correctamente");
				forma.resetListServicios();
				forma.setListServiciosAdicionales(mundo.consultarServiciosAdicionalesProfesionales(codProfesional, codEstandar));	
				forma.setListServiciosSel("numRegistros", "0");			
				forma.setListServiciosSel("codigosServiciosInsertados", mundo.obtenerCodigosServicios(forma.getListServiciosAdicionales())); 
			}
			else
			{
				forma.getMensajeExito().put("operacionExitosa", ConstantesBD.acronimoNo);
				forma.getMensajeExito().put("mensaje", "No se pudo Adicionar el Servicio"); 
				errores.add("descripcion",new ActionMessage("errors.notEspecific","No Fue Posible la Insercion del Servicio"));
				saveErrors(request, errores);
				UtilidadBD.abortarTransaccion(con);		
			}
		
	  }else
	   {
		  saveErrors(request, errores);	
	   }
    
      UtilidadBD.closeConnection(con);
	  return mapping.findForward("empezarIngresarModificarServ");
	}



	/**
     * Metodo para eliminar un servicio seleccionado en opcion de ser guardado   
     * @param con
     * @param forma
     * @param mapping
     * @param usuario
     * @return
     */
	private ActionForward accionEliminarServicio(Connection con,ServiAdicionalesXProfAtenOdontoForm forma, ActionMapping mapping,UsuarioBasico usuario) {
		
		//Se consulta el primer servicio antes de eliminar
		int codigoServicioAntes = ConstantesBD.codigoNuncaValido;
		for(int i=0;i<forma.getNumServicios();i++)
			if(!UtilidadTexto.getBoolean(forma.getListServiciosSel("fueEliminado_"+i)+""))
			{
				codigoServicioAntes = Integer.parseInt(forma.getListServiciosSel("codigoServicio_"+i).toString());
				i = forma.getNumServicios();
			}
		
		
		//Se elimina el servicio
		forma.setListServiciosSel("fueEliminado_"+forma.getIndex(), ConstantesBD.acronimoSi);
		
		//Se consulta el primer servicio antes de eliminar
		int codigoServicioDespues = ConstantesBD.codigoNuncaValido;
		for(int i=0;i<forma.getNumServicios();i++)
			if(!UtilidadTexto.getBoolean(forma.getListServiciosSel("fueEliminado_"+i)+""))
			{
				codigoServicioDespues = Integer.parseInt(forma.getListServiciosSel("codigoServicio_"+i).toString());
				i = forma.getNumServicios();
			}
		
		
			
		//***********Se reeditan los codigos insertados******************************************+
		String codigosServiciosInsertados = "";
		for(int i=0;i<forma.getNumServicios();i++)
		{
			//Mientras que no se haya eliminado el servicio se toma como existente
			if(!UtilidadTexto.getBoolean(forma.getListServiciosSel("fueEliminado_"+i)+""))
				codigosServiciosInsertados += forma.getListServiciosSel("codigoServicio_"+i)+",";
		}
		forma.setListServiciosSel("codigosServiciosInsertados", codigosServiciosInsertados);
		
		//*****************************************************************************************
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("empezarIngresarModificarServ");
	}

	/**
	 * Metodo para consultar los servicios adicionales asociados a un Profesional
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionConsultarServiciosAdicionales(Connection con,ServiAdicionalesXProfAtenOdontoForm forma, UsuarioBasico usuario,HttpServletRequest request, ActionMapping mapping) {
		
		forma.setBuscarServicios(false);	
		int codProfesional= Utilidades.convertirAEntero(forma.getCodProfesionalSel());
		int codEstandar = Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt()));
		forma.resetListServicios();
		forma.setListServiciosAdicionales(mundo.consultarServiciosAdicionalesProfesionales(codProfesional,codEstandar));
		forma.setListServiciosSel("codigosServiciosInsertados", mundo.obtenerCodigosServicios(forma.getListServiciosAdicionales()));
		if(codProfesional>0){
			forma.setBuscarServicios(true);	
		}
		UtilidadBD.closeConnection(con); 
		return mapping.findForward("empezarIngresarModificarServ");
	}


	/**
	 * Metodo para inicializar el Flujo de Ingresar modificar de Servicios Adicionales profesioanales de la Salud de Atencion Odontologica
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezarIngresarModificar(Connection con,ServiAdicionalesXProfAtenOdontoForm forma, UsuarioBasico usuario,	HttpServletRequest request, ActionMapping mapping) {
		
		forma.setListProfesionales(UtilidadesAdministracion.obtenerProfesionales(con, usuario.getCodigoInstitucionInt(), -1, false, true, ConstantesBD.codigoNuncaValido));
		forma.setListServiciosSel("numRegistros", "0");
		forma.setListServiciosSel("codigosServiciosInsertados", "");
		UtilidadBD.closeConnection(con); 
		return mapping.findForward("empezarIngresarModificarServ");

	}
	
	
	/**
	 * Metodo para agregar un servicio adicional con opcion de ser guardado
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param paciente
	 * @return
	 */
	private ActionForward accionAgregarServicio(Connection con, ServiAdicionalesXProfAtenOdontoForm forma, UsuarioBasico usuario, ActionMapping mapping) 
	{
		//Se re-edita la descripcion del servicio
		forma.setListServiciosSel("descripcionServicio_"+forma.getNumServicios(), 
				forma.getListServiciosSel("descripcionServicio_"+forma.getNumServicios())+" - "+forma.getListServiciosSel("esPos_"+forma.getNumServicios()));
		
		//Se aumenta en 1 el número de servicios del mapa
		forma.setListServiciosSel("numRegistros", (forma.getNumServicios()+1));
		
		//Se listan nuevamente los servicios insertados
		String codigosServiciosInsertados = "";
		for(int i=0;i<forma.getNumServicios();i++)
		{
			//Mientras que no se haya eliminado el servicio se toma como existente
			if(!UtilidadTexto.getBoolean(forma.getListServiciosSel("fueEliminado_"+i)+""))
				codigosServiciosInsertados += forma.getListServiciosSel("codigoServicio_"+i)+",";
		}
		forma.setListServiciosSel("codigosServiciosInsertados", codigosServiciosInsertados);
		
		logger.info("MAPA CIRUGIA CARGOS DIRECTOS**********************************************");
		Utilidades.imprimirMapa(forma.getListServiciosSel());
		logger.info("**************************************************************************");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("empezarIngresarModificarServ");
	}



	
	
	
}
