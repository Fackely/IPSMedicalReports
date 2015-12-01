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
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.facturacion.ConsultaProcesarRipsEntidadesSubForm;
import com.princetonsa.dto.facturacion.DtoResultadoConsultaLogRipsEntidadesSub;
import com.princetonsa.dto.facturacion.DtoResultadoProcesarRipsAutorizacion;
import com.princetonsa.dto.facturacion.DtoServicios;
import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Articulo;
import com.servinte.axioma.orm.AutorizacionesEntSubServi;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.fabrica.inventario.InventarioServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IUsuariosServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IEntidadesSubcontratadasServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.ILogRipsEntidadesSubcontratadasServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IServiciosServicio;
import com.servinte.axioma.servicio.interfaz.inventario.IArticulosServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionesEntSubArticuloServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionesEntSubServiServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionesEntidadesSubServicio;

public class ConsultaProcesarRipsEntidadesSubAction extends Action{
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, 
	 * HttpServletRequest, HttpServletResponse)
	*/
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
								HttpServletResponse response )throws Exception
	{
		
		if(form instanceof ConsultaProcesarRipsEntidadesSubForm){
			
			ConsultaProcesarRipsEntidadesSubForm forma = (ConsultaProcesarRipsEntidadesSubForm)form;
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
			else if(estado.equals("abrirDetalleAutorizaciones"))
			{
				this.accionAbrirDetalleAutorizaciones(forma,usuario, mapping, ins,request);
				return mapping.findForward("detalle");
			}
			else if(estado.equals("abrirDetalleServMedAutorizados"))
			{
				this.accionAbrirDetalleServMedi(forma,usuario, mapping, ins,request);
				return mapping.findForward("detalleServMedAutorizados");
			}
			else if(estado.equals("abrirDetallePorAutorizacion"))
			{
				this.accionAbrirDetallePorAutorizacion(forma,usuario, mapping, ins,request);
				return mapping.findForward("detalleAutorizacion");
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
	private void accionEmpezar(ConsultaProcesarRipsEntidadesSubForm forma, UsuarioBasico usuario,  
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
	private ActionForward accionConsultar(ConsultaProcesarRipsEntidadesSubForm forma, UsuarioBasico usuario,  
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
			 
			 forma.setListaProcesoRips(listaLog);
	         return mapping.findForward("resultado");
	        }
	}
	
	/**
	 * Este método se encarga de realizar la consulta de rips entidades subcontratadas
	 * y ordenar el resultado para mostrarse en el detalle de las autorizaciones
	 * @param forma
	 * @param usuario Usuario que genera el proceso
	 * @param mapping Para hacer redirrección a los JSP
	 * @param ins Institución del usuario en sessión
	 * 
	 * @author Fabian Becerra
	 */
	private void accionAbrirDetalleAutorizaciones(ConsultaProcesarRipsEntidadesSubForm forma, UsuarioBasico usuario,  
			ActionMapping mapping, InstitucionBasica ins, HttpServletRequest request){
		try{
			HibernateUtil.beginTransaction();
			ILogRipsEntidadesSubcontratadasServicio servicioLog=FacturacionServicioFabrica.crearLogRipsEntidadesSubServicio();
			forma.setLogDetalleOrdenado(servicioLog.ordenarConsultaRegistrosLogRipsEntidadesSubPorNumerosAutorizacion(forma.getCodigoPkLogProcesoSeleccionado()));
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error(e);
			HibernateUtil.abortTransaction();
		}
	}
	
	/**
	 * Este método se encarga de realizar la consulta de los servicios y medicamentos de la
	 * autorización seleccionada y organizarlos para su presentación en la página
	 * @param forma
	 * @param usuario Usuario que genera el proceso
	 * @param mapping Para hacer redirrección a los JSP
	 * @param ins Institución del usuario en sessión
	 * 
	 * @author Fabian Becerra
	 */
	private void accionAbrirDetalleServMedi(ConsultaProcesarRipsEntidadesSubForm forma, UsuarioBasico usuario,  
			ActionMapping mapping, InstitucionBasica ins, HttpServletRequest request){
		try{
			HibernateUtil.beginTransaction();
			IAutorizacionesEntSubServiServicio autorEntSubServiServicio
				= ManejoPacienteServicioFabrica.crearAutorizacionesEntSubServiServicio();
			IAutorizacionesEntSubArticuloServicio autorEntSubArtiServicio
				= ManejoPacienteServicioFabrica.crearAutorizacionesEntSubArticulo();
			IAutorizacionesEntidadesSubServicio servicioAutorizaciones
				= ManejoPacienteServicioFabrica.crearAutorizacionEntidadesSubServicio();
			
			AutorizacionesEntidadesSub autorizacion=servicioAutorizaciones.obtenerAutorizacionEntSubPorConsecutivoAutorizacion(forma.getCodigoPkAutorizacionSeleccionada());
			
			DtoAutorizacionEntSubcontratadasCapitacion dtoParametros=new DtoAutorizacionEntSubcontratadasCapitacion();
			dtoParametros.setAutorizacion(autorizacion.getConsecutivo());
			ArrayList<AutorizacionesEntSubServi> listaServiciosAutorizacion=autorEntSubServiServicio.listarAutorizacionesEntSubServiPorAutoEntSub(dtoParametros);
			ArrayList<DtoArticulosAutorizaciones> listaArticAutor=autorEntSubArtiServicio.listarautorizacionesEntSubArticuPorAutoEntSub(dtoParametros);
			
			String codigoBusquedaArticulos=ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(ins.getCodigoInstitucionBasica());
			if(UtilidadTexto.isEmpty(codigoBusquedaArticulos))
				codigoBusquedaArticulos=ConstantesIntegridadDominio.acronimoAxioma;
			String codigoBusquedaServicios=ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(ins.getCodigoInstitucionBasica());
			if(UtilidadTexto.isEmpty(codigoBusquedaServicios))
				codigoBusquedaServicios="0";
			DtoResultadoProcesarRipsAutorizacion dtoAutoriz;
			ArrayList<DtoResultadoProcesarRipsAutorizacion> listaServArt=new ArrayList<DtoResultadoProcesarRipsAutorizacion>();
			IServiciosServicio servicioServicio=FacturacionServicioFabrica.crearServiciosServicio();
			IArticulosServicio servicioArticulos=InventarioServicioFabrica.crearArticulosServicio();
			
			
			for(AutorizacionesEntSubServi dtoServicio:listaServiciosAutorizacion){
				dtoAutoriz = new DtoResultadoProcesarRipsAutorizacion();
				ArrayList<DtoServicios> servicio=servicioServicio.obtenerServiciosXTipoTarifarioOficial((int)dtoServicio.getServicios().getCodigo(), Utilidades.convertirAEntero(codigoBusquedaServicios));
				if(!Utilidades.isEmpty(servicio)){
					dtoAutoriz.setCodigoServArt(servicio.get(0).getCodigoPropietarioServicio());
					dtoAutoriz.setDescripcionServArt(servicio.get(0).getDescripcionServicio());
				}
				
				dtoAutoriz.setCantidadServArt(dtoServicio.getCantidad()+"");
				dtoAutoriz.setValorServArt(dtoServicio.getValorTarifa()+"");
				listaServArt.add(dtoAutoriz);
			}
			for(DtoArticulosAutorizaciones dtoArticulo:listaArticAutor){
				dtoAutoriz=new DtoResultadoProcesarRipsAutorizacion();
				
				Articulo art=servicioArticulos.obtenerArticuloPorId(dtoArticulo.getCodigoArticulo());
				if(codigoBusquedaArticulos.equals(ConstantesIntegridadDominio.acronimoAxioma)){
					dtoAutoriz.setCodigoServArt(art.getCodigo()+"");
				}else
					if(codigoBusquedaArticulos.equals(ConstantesIntegridadDominio.acronimoInterfaz)){
						dtoAutoriz.setCodigoServArt(art.getCodigoInterfaz()+"");
					}
				
				dtoAutoriz.setDescripcionServArt(dtoArticulo.getDescripcionArticulo());
				dtoAutoriz.setCantidadServArt(dtoArticulo.getCantidadAutorizadaArticulo());
				dtoAutoriz.setValorServArt(dtoArticulo.getValorArticulo()+"");
				listaServArt.add(dtoAutoriz);
			}
			forma.setListaServArt(listaServArt);
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error(e);
			HibernateUtil.abortTransaction();
		}
		
	}
	
	
	
	
	
	/**
	 * Este método se encarga de realizar la consulta de rips entidades subcontratadas
	 * y organizar el resultado para presentar el detalle de las inconsistencias de una autorización
	 * @param forma
	 * @param usuario Usuario que genera el proceso
	 * @param mapping Para hacer redirrección a los JSP
	 * @param ins Institución del usuario en sessión
	 * 
	 * @author Fabian Becerra
	 */
	private void accionAbrirDetallePorAutorizacion(ConsultaProcesarRipsEntidadesSubForm forma, UsuarioBasico usuario,  
			ActionMapping mapping, InstitucionBasica ins, HttpServletRequest request){
		try{
			HibernateUtil.beginTransaction();
			ILogRipsEntidadesSubcontratadasServicio servicioLog=FacturacionServicioFabrica.crearLogRipsEntidadesSubServicio();
			forma.setLogDetalleOrdenado(servicioLog.ordenarConsultaRegistrosLogRipsEntidadesSubParaDetallePorNumeroAutorizacion(forma.getCodigoPkLogProcesoSeleccionado(),forma.getCodigoPkArchivo(),forma.getCodigoPkRegistro()));
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error(e);
			HibernateUtil.abortTransaction();
		}
	}
	
	
	private ActionForward accionOrdenar(ConsultaProcesarRipsEntidadesSubForm forma,
			ActionMapping mapping) 
	{
		Log4JManager.info("patron->" + forma.getPatronOrdenar());
		Log4JManager.info("des -->" + forma.getEsDescendente() );
		
		boolean ordenamiento= false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente")){
			
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		
		Collections.sort(forma.getListaProcesoRips() ,sortG);
		return mapping.findForward("resultado");
	}
	
	
}
