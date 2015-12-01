/**
 * 
 */
package com.servinte.axioma.action.consultaExterna;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.ConstantesBDManejoPaciente;
import util.reportes.GeneradorReporte;

import com.princetonsa.dto.comun.DtoCheckBox;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.actionForm.consultaExterna.IdentificadoresOrdenadoresConsultaForm;
import com.servinte.axioma.bl.administracion.facade.AdministracionFacade;
import com.servinte.axioma.bl.consultaExterna.facade.ConsultaExternaFacade;
import com.servinte.axioma.bl.facturacion.facade.FacturacionFacade;
import com.servinte.axioma.bl.inventario.facade.InventarioFacade;
import com.servinte.axioma.dto.administracion.CentroAtencionDto;
import com.servinte.axioma.dto.administracion.FuncionalidadDto;
import com.servinte.axioma.dto.administracion.ProfesionalSaludDto;
import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;
import com.servinte.axioma.dto.consultaExterna.GrupoOrdenadoresConsultaExternaDto;
import com.servinte.axioma.dto.consultaExterna.OrdenadoresConsultaExternaPlanoDto;
import com.servinte.axioma.dto.facturacion.ConvenioDto;
import com.servinte.axioma.dto.facturacion.GrupoServicioDto;
import com.servinte.axioma.dto.inventario.ClaseInventarioDto;
import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.generadorReporte.consultaExterna.ordenadoresConsultaExterna.GeneradorReporteIdentificadorOrdenadoresConsultaExterna;
import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * @author jeilones
 * @created 29/10/2012
 *
 */
public class IdentificadoresOrdenadoresConsultaAction extends DispatchAction{
	
	private static final String KEY_ERROR_NO_ESPECIFIC="errors.notEspecific";
	private static final String KEY_ERROR_CAST="errors.castForm";
	private static final String KEY_ERROR_REQUIRED="errors.required";
	
	private MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.consultaExterna.IdentificadoresOrdenadoresConsultaForm");
	
	private static final String FORWARD_MENU_REPORTES="menuReportes";
	private static final String FORWARD_CONSULTAR_ORDENADORES="consultarOrdenadores";
	private static final String KEY_ERROR_SIN_TARIFA_ARTICULOS = "errores.IdentificadoresOrdenadoresConsulta.sinTarifaArticulos";
	private static final String KEY_ERROR_SIN_TARIFA_SERVICIOS = "errores.IdentificadoresOrdenadoresConsulta.sinTarifaServicios";
	private static final String KEY_ERROR_SIN_ESQUEMAS_TARIFA_DEFINIDOS = "errores.IdentificadoresOrdenadoresConsulta.noEsquemaTarifario";
	private static final String KEY_ERROR_SIN_ESQUEMAS_SIN_RESULTADOS= "errores.IdentificadoresOrdenadoresConsulta.sinResultados";
	
	private static final String NOMBRE_REPORTE="IdentificadorOrdenadoresConsultaExterna";
	private static final String NOMBRE_REPORTE_PROFESIONALES="Reporte Identificar Profesionales Ordenadores en Consulta Externa";
	private static final String NOMBRE_REPORTE_GRUPOS_SERV_CLASES_INVENT="Reporte Identificar Ordenadores en Consulta Externa";
	
	/**
	 * Carga las funcionalidades de reportes existentes
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @author jeilones
	 * @created 20/11/2012
	 */
	public ActionForward cargarMenuReportes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores=new ActionMessages();
		IdentificadoresOrdenadoresConsultaForm forma=null;
		try {
			
			if(form instanceof IdentificadoresOrdenadoresConsultaForm){
				forma = (IdentificadoresOrdenadoresConsultaForm) form;
			}
			else{
				throw new ClassCastException();
			}
			
			AdministracionFacade administracionFacade=new AdministracionFacade();
			List<FuncionalidadDto> listaFuncionalidades=administracionFacade.consultarFuncionalidades(ConstantesBD.codigoFuncionalidadReportesConsultaExterna);
			forma.setListaFuncionalidades(listaFuncionalidades);
			
			forma.setHayMensajeExito(false);
		} catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}catch (IPSException ipse) {
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
		}catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		return mapping.findForward(FORWARD_MENU_REPORTES);
	}

	/**
	 * Carga los filtros de busqueda para el reporte de 
	 * identificadores de ordenadores de consulta externa
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @author jeilones
	 * @created 20/11/2012
	 */
	public ActionForward consultarIdOdernadoresConsulta(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores=new ActionMessages();
		IdentificadoresOrdenadoresConsultaForm forma=null;
		try {
			
			if(form instanceof IdentificadoresOrdenadoresConsultaForm){
				forma = (IdentificadoresOrdenadoresConsultaForm) form;
			}
			else{
				throw new ClassCastException();
			}
			
			forma.setPermitirPopUpImpresion(false);
			forma.setNombreArchivoGenerado("");
			
			obtenerTiposTipoReporteOrdenadoresConsultaExt(forma);
			
			FacturacionFacade facturacionFacade=  new FacturacionFacade();
			InventarioFacade inventarioFacade= new InventarioFacade();
			ConsultaExternaFacade consultaExternaFacade= new ConsultaExternaFacade();
			
			forma.setFechaInicialAtencionCita(null);
			forma.setFechaFinalAtencionCita(null);
			
			List<GrupoServicioDto> listaGruposServiciosDto= facturacionFacade.consultarGruposServicio();
			forma.setListaGruposServicio(listaGruposServiciosDto);
			forma.setIdGrupoServicio(ConstantesBD.codigoNuncaValido+"");
			
			List<ClaseInventarioDto> listaClasesInventarioDto= inventarioFacade.consultarClaseInventario();
			forma.setListaClasesInventario(listaClasesInventarioDto);
			forma.setIdClaseInventario(ConstantesBD.codigoNuncaValido+"");
			
			try{
				HibernateUtil.beginTransaction();
				List<DtoCentrosAtencion> centroAtencionActivos=new ArrayList<DtoCentrosAtencion>();
				centroAtencionActivos=com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio.crearLocalizacionServicio().listarTodosCentrosAtencion();
				
				List<CentroAtencionDto>listaCentrosAtencion=new ArrayList<CentroAtencionDto>(0);
				for (DtoCentrosAtencion centroAtencion : centroAtencionActivos) 
				{
					CentroAtencionDto centroAtencionDto=new CentroAtencionDto();
					centroAtencionDto.setCodigo(centroAtencion.getConsecutivo());
					StringBuffer nombre=new StringBuffer(centroAtencion.getDescripcion());
					nombre.append(" - ")
						.append(centroAtencion.getDescripcionCiudad())
						.append(" (")
						.append(UtilidadTexto.organizarTildes( centroAtencion
									.getDescripcionRegion()))
						.append(") ");					
					centroAtencionDto.setNombre(nombre.toString());
					listaCentrosAtencion.add(centroAtencionDto);
				}
				forma.setListaCentrosAtencion(listaCentrosAtencion);
				forma.setIdCentroAtencion(ConstantesBD.codigoNuncaValido+"");
				HibernateUtil.endTransaction();
			}
			catch (Exception e) {
				Log4JManager.error(e);
				HibernateUtil.abortTransaction();
			}
			
			List<DtoUnidadesConsulta> listaUnidadAgenda= consultaExternaFacade.consultarUnidadAgenda(Integer.parseInt(forma.getIdCentroAtencion()));
			forma.setListaUnidadesConsultas(listaUnidadAgenda);
			forma.setIdUnidadConsulta(ConstantesBD.codigoNuncaValido+"");
			
			UsuarioBasico usuarioActual = Utilidades.getUsuarioBasicoSesion(request.getSession());
			
			List<ConvenioDto>convenios=consultaExternaFacade.consultarTodosConveniosPorInstitucion(usuarioActual.getCodigoInstitucionInt());
			forma.setListaConvenios(convenios);
			forma.setIdConvenio(ConstantesBD.codigoNuncaValido+"");
			
			List<ProfesionalSaludDto>profesionalesAtiendenCitas=consultaExternaFacade.consultarProfesionalesAtiendenCitas(usuarioActual.getCodigoInstitucionInt());
			forma.setListaProfesionalesSalud(profesionalesAtiendenCitas);
			forma.setLoginProfesionalSalud(ConstantesBD.codigoNuncaValido+"");
			
			forma.setIdTipoReporte(ConstantesBDManejoPaciente.codigoTipoReporteOrdenarodesConsultaExternaProf+"");
			forma.setTipoSalida(ConstantesBD.codigoNuncaValido+"");
			
			forma.setHayMensajeExito(false);
			
		} catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}catch (IPSException ipse) {
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
		}catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		return mapping.findForward(FORWARD_CONSULTAR_ORDENADORES);
	}
	
	/**
	 * Vuelve al menu de funcionalidades de reportes de consulta externa
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @author jeilones
	 * @created 20/11/2012
	 */
	public ActionForward volverMenuReportes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		return mapping.findForward(FORWARD_MENU_REPORTES);
	}
	
	/**
	 * Consulta todas las unidades de consulta relacionadas 
	 * al centro de atencion seleccionado
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @author jeilones
	 * @created 20/11/2012
	 */
	public ActionForward cargarUnidadesConsultaXCentroAtencion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores=new ActionMessages();
		IdentificadoresOrdenadoresConsultaForm forma=null;
		try {
			
			if(form instanceof IdentificadoresOrdenadoresConsultaForm){
				forma = (IdentificadoresOrdenadoresConsultaForm) form;
			}
			else{
				throw new ClassCastException();
			}
			ConsultaExternaFacade consultaExternaFacade= new ConsultaExternaFacade();
			
			List<DtoUnidadesConsulta> listaUnidadAgenda= consultaExternaFacade.consultarUnidadAgenda(Integer.parseInt(forma.getIdCentroAtencion()));
			forma.setListaUnidadesConsultas(listaUnidadAgenda);
			forma.setIdUnidadConsulta(ConstantesBD.codigoNuncaValido+"");
			
			forma.setHayMensajeExito(false);
		}catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}catch (IPSException ipse) {
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
		}catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		return mapping.findForward(FORWARD_CONSULTAR_ORDENADORES);
	}
	
	/**
	 * Cargar los tipos de reportes para identificadores de ordenadores de consulta externa
	 * 
	 * @param forma
	 * @author jeilones
	 * @created 31/10/2012
	 */
	private void obtenerTiposTipoReporteOrdenadoresConsultaExt(IdentificadoresOrdenadoresConsultaForm forma){
    	ArrayList<DtoCheckBox> tiposConsecutivoAutorizacion = new ArrayList<DtoCheckBox>();
    	DtoCheckBox tipo = new DtoCheckBox();
    	tipo.setCodigo(String.valueOf(ConstantesBDManejoPaciente.codigoTipoReporteOrdenarodesConsultaExternaProf));
    	tipo.setNombre(fuenteMensaje.getMessage(
    							"identificadoresOrdenadoresConsultaForm.consulta.tipoReporte"+
    							ConstantesBDManejoPaciente.codigoTipoReporteOrdenarodesConsultaExternaProf));
    	tiposConsecutivoAutorizacion.add(tipo);
    	
    	tipo= new DtoCheckBox();
    	tipo.setCodigo(String.valueOf(ConstantesBDManejoPaciente.codigoTipoReporteOrdenarodesConsultaExternaGrupoClase));
    	tipo.setNombre(fuenteMensaje.getMessage(
    							"identificadoresOrdenadoresConsultaForm.consulta.tipoReporte"+
    							ConstantesBDManejoPaciente.codigoTipoReporteOrdenarodesConsultaExternaGrupoClase));
    	tiposConsecutivoAutorizacion.add(tipo);
    	
    	forma.setListaTipoReporte(tiposConsecutivoAutorizacion);
    }
	
	
	/**
	 * Valida los filtros de busqueda y crea el reporte segun el tipo seleccionado por el usuario
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @author jeilones
	 * @created 20/11/2012
	 */
	public ActionForward crearReporte(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores=new ActionMessages();
		IdentificadoresOrdenadoresConsultaForm forma=null;
		try{
			if(form instanceof IdentificadoresOrdenadoresConsultaForm){
				forma = (IdentificadoresOrdenadoresConsultaForm) form;
			}
			else{
				throw new ClassCastException();
			}
			
			String estado=request.getParameter("estado");
			
			if(estado.equals("crearReporte")&&!forma.isPermitirPopUpImpresion()){
				if(forma.getFechaInicialAtencionCita()==null){
					errores.add("", new ActionMessage(KEY_ERROR_REQUIRED, fuenteMensaje.getMessage("identificadoresOrdenadoresConsultaForm.consulta.fechaInicial")));
				}
				if(forma.getFechaFinalAtencionCita()==null){
					errores.add("", new ActionMessage(KEY_ERROR_REQUIRED, fuenteMensaje.getMessage("identificadoresOrdenadoresConsultaForm.consulta.fechaFinal")));
				}
				
				if(forma.getIdTipoReporte()==null
						||forma.getIdTipoReporte().trim().isEmpty()
						||(!forma.getIdTipoReporte().trim().equals(ConstantesBDManejoPaciente.codigoTipoReporteOrdenarodesConsultaExternaProf+"")
						&&!forma.getIdTipoReporte().trim().equals(ConstantesBDManejoPaciente.codigoTipoReporteOrdenarodesConsultaExternaGrupoClase+""))){
					errores.add("", new ActionMessage(KEY_ERROR_REQUIRED, fuenteMensaje.getMessage("identificadoresOrdenadoresConsultaForm.consulta.tipoReporte")));
				}
				
				forma.setHayMensajeExito(false);
				
				if(!errores.isEmpty()){
					saveErrors(request, errores);
				}else{
					forma.setPermitirPopUpImpresion(true);
				}
				return mapping.findForward(FORWARD_CONSULTAR_ORDENADORES);
			}
			
			GrupoOrdenadoresConsultaExternaDto filtrosGrupoOrdenadoresConsultaExternaDto=new GrupoOrdenadoresConsultaExternaDto();
			
			filtrosGrupoOrdenadoresConsultaExternaDto.setFechaInicialAtencionCita(forma.getFechaInicialAtencionCita());
			filtrosGrupoOrdenadoresConsultaExternaDto.setFechaFinalAtencionCita(forma.getFechaFinalAtencionCita());
			
			filtrosGrupoOrdenadoresConsultaExternaDto.setFiltroIdCentroAtencion(forma.getIdCentroAtencion());
			for(CentroAtencionDto centroAtencionDto:forma.getListaCentrosAtencion()){
				if(forma.getIdCentroAtencion().equals(""+centroAtencionDto.getCodigo())){
					filtrosGrupoOrdenadoresConsultaExternaDto.setFiltroCentroAtencion(centroAtencionDto.getNombre());
					break;
				}
			}
			
			filtrosGrupoOrdenadoresConsultaExternaDto.setFiltroIdUnidadConsulta(forma.getIdUnidadConsulta());
			for(DtoUnidadesConsulta unidadesConsulta:forma.getListaUnidadesConsultas()){
				if(forma.getIdUnidadConsulta().equals(""+unidadesConsulta.getCodigo())){
					filtrosGrupoOrdenadoresConsultaExternaDto.setUnidadesConsulta(unidadesConsulta.getDescripcion());
					break;
				}
			}
			
			filtrosGrupoOrdenadoresConsultaExternaDto.setFiltroIdConvenio(forma.getIdConvenio());
			for(ConvenioDto convenioDto:forma.getListaConvenios()){
				if(forma.getIdConvenio().equals(""+convenioDto.getCodigo())){
					filtrosGrupoOrdenadoresConsultaExternaDto.setConvenio(convenioDto.getNombre());
					break;
				}
			}
			
			ProfesionalSaludDto profesionalSalud=new ProfesionalSaludDto();
			profesionalSalud.setLoginUsuario(forma.getLoginProfesionalSalud());
			
			filtrosGrupoOrdenadoresConsultaExternaDto.setFiltroProfesionalSalud(profesionalSalud);
			for(ProfesionalSaludDto profesionalSaludDto:forma.getListaProfesionalesSalud()){
				if(forma.getLoginProfesionalSalud().equals(""+profesionalSaludDto.getLoginUsuario())){
					filtrosGrupoOrdenadoresConsultaExternaDto.setProfesionalSalud(profesionalSaludDto.getNombreCompleto());
					break;
				}
			}
			
			filtrosGrupoOrdenadoresConsultaExternaDto.setFiltroIdGrupoServicio(forma.getIdGrupoServicio());
			for(GrupoServicioDto grupoServicioDto:forma.getListaGruposServicio()){
				if(forma.getIdGrupoServicio().equals(""+grupoServicioDto.getCodigo())){
					filtrosGrupoOrdenadoresConsultaExternaDto.setGrupoServicio(grupoServicioDto.getDescripcion());
					break;
				}
			}
			
			filtrosGrupoOrdenadoresConsultaExternaDto.setFiltroIdClaseInventario(forma.getIdClaseInventario());
			for(ClaseInventarioDto claseInventarioDto:forma.getListaClasesInventario()){
				if(forma.getIdClaseInventario().equals(""+claseInventarioDto.getCodigo())){
					filtrosGrupoOrdenadoresConsultaExternaDto.setClaseInventario(claseInventarioDto.getNombre());
					break;
				}
			}
			
			SimpleDateFormat format=new SimpleDateFormat(ConstantesBD.formatoFechaApp);
			
			UsuarioBasico usuarioActual = Utilidades.getUsuarioBasicoSesion(request.getSession());
			InstitucionBasica institucionActual = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			ConsultaExternaFacade consultaExternaFacade=new ConsultaExternaFacade();
			String stringIdEsquemaTarifarioArticulos=ValoresPorDefecto.getEsquemaTariMedicamentosValorizarOrden(usuarioActual.getCodigoInstitucionInt());
			String stringIdEsquemaTarifarioServicios=ValoresPorDefecto.getEsquemaTariServiciosValorizarOrden(usuarioActual.getCodigoInstitucionInt());
			
			boolean hayEsquemasTarifarios=true;
			
			if(stringIdEsquemaTarifarioArticulos==null
					||stringIdEsquemaTarifarioArticulos.trim().isEmpty()
					||stringIdEsquemaTarifarioArticulos.trim().equals(ConstantesBD.codigoNuncaValido+"")
					||stringIdEsquemaTarifarioServicios==null
					||stringIdEsquemaTarifarioServicios.trim().isEmpty()
					||stringIdEsquemaTarifarioServicios.trim().equals(ConstantesBD.codigoNuncaValido+"")){
				errores.add("",new ActionMessage(KEY_ERROR_SIN_ESQUEMAS_TARIFA_DEFINIDOS));
				hayEsquemasTarifarios=false;
			}
			if(hayEsquemasTarifarios){
				List<MedicamentoInsumoAutorizacionOrdenDto>articulos=new ArrayList<MedicamentoInsumoAutorizacionOrdenDto>(0);
				int esquemaTarifarioArticulos = 0;
				if(stringIdEsquemaTarifarioArticulos!=null&&!stringIdEsquemaTarifarioArticulos.isEmpty()&&!stringIdEsquemaTarifarioArticulos.split("-")[0].trim().isEmpty()){
					esquemaTarifarioArticulos=Integer.parseInt(stringIdEsquemaTarifarioArticulos.split("-")[0]);
					articulos=consultaExternaFacade.verificarTarifasArticulos(filtrosGrupoOrdenadoresConsultaExternaDto,esquemaTarifarioArticulos);
				}
				
				
				List<ServicioAutorizacionOrdenDto>servicios=new ArrayList<ServicioAutorizacionOrdenDto>(0);
				int esquemaTarifarioServicios = 0;
				if(stringIdEsquemaTarifarioServicios!=null&&!stringIdEsquemaTarifarioServicios.isEmpty()&&!stringIdEsquemaTarifarioServicios.split("-")[0].trim().isEmpty()){
					esquemaTarifarioServicios=Integer.parseInt(stringIdEsquemaTarifarioServicios.split("-")[0]);
					servicios=consultaExternaFacade.verificarTarifasServicios(filtrosGrupoOrdenadoresConsultaExternaDto,esquemaTarifarioServicios);
				}
				//Se verifica que no haya articulos ni servicios sin una tarifa definida
				if(articulos.isEmpty()&&servicios.isEmpty()){
					
					/**Cargar informacion general**/
					
					filtrosGrupoOrdenadoresConsultaExternaDto.setUbicacionLogo(institucionActual
							.getUbicacionLogo());
					filtrosGrupoOrdenadoresConsultaExternaDto.setRutaLogo(institucionActual.getLogoJsp());
	
					filtrosGrupoOrdenadoresConsultaExternaDto.setRazonSocial(institucionActual
							.getRazonSocialInstitucionCompleta());
					filtrosGrupoOrdenadoresConsultaExternaDto.setInstitucion(usuarioActual.getCodigoInstitucionInt());
					filtrosGrupoOrdenadoresConsultaExternaDto.setNit(institucionActual.getNit());
					filtrosGrupoOrdenadoresConsultaExternaDto.setActividadEconomica(institucionActual
							.getActividadEconomica());
					filtrosGrupoOrdenadoresConsultaExternaDto.setCentroAtencion(usuarioActual.getCentroAtencion());
					filtrosGrupoOrdenadoresConsultaExternaDto.setDireccion(institucionActual.getDireccion());
					filtrosGrupoOrdenadoresConsultaExternaDto.setTelefono(institucionActual.getTelefono());
					StringBuffer nombreUsuario=new StringBuffer(usuarioActual.getNombreUsuario());
					nombreUsuario.append(" (").append(usuarioActual.getLoginUsuario()).append(")");
					filtrosGrupoOrdenadoresConsultaExternaDto.setUsuario(nombreUsuario.toString());
					
					if(forma.getFechaInicialAtencionCita()!=null){
						filtrosGrupoOrdenadoresConsultaExternaDto.setFechaInicial(format.format(forma.getFechaInicialAtencionCita()));
						filtrosGrupoOrdenadoresConsultaExternaDto.setFiltroFechaInicialAtencionCita(format.format(forma.getFechaInicialAtencionCita()));
					}
					if(forma.getFechaFinalAtencionCita()!=null){
						filtrosGrupoOrdenadoresConsultaExternaDto.setFechaFinal(format.format(forma.getFechaFinalAtencionCita()));
						filtrosGrupoOrdenadoresConsultaExternaDto.setFiltroFechaFinalAtencionCita(format.format(forma.getFechaFinalAtencionCita()));
					}
					/**Fin cargue informacion general*/
					
					GeneradorReporte generadorReporte=null;
	
					filtrosGrupoOrdenadoresConsultaExternaDto.setCodigoTipoReporte(Integer.parseInt(forma.getIdTipoReporte()));
					filtrosGrupoOrdenadoresConsultaExternaDto.setCodigoTipoImpresion(Integer.parseInt(forma.getTipoSalida()));
					
					//Se imprime el reporte de a cuerdo al tipo seleccionado
					if(forma.getTipoSalida().equals(EnumTiposSalida.PDF.getCodigo()+"")||forma.getTipoSalida().equals(EnumTiposSalida.HOJA_CALCULO.getCodigo()+"")){
						if(forma.getIdTipoReporte().equals(ConstantesBDManejoPaciente.codigoTipoReporteOrdenarodesConsultaExternaProf+"")){
							filtrosGrupoOrdenadoresConsultaExternaDto.setTipoReporte(NOMBRE_REPORTE_PROFESIONALES);
							
							List<ProfesionalSaludDto>profesionales=consultaExternaFacade.consultarIdentificadorOrdenadoresConsulta(filtrosGrupoOrdenadoresConsultaExternaDto, esquemaTarifarioArticulos,esquemaTarifarioServicios);
							
							if(profesionales.isEmpty()){
								errores.add("",new ActionMessage(KEY_ERROR_SIN_ESQUEMAS_SIN_RESULTADOS));
							}else{						
								filtrosGrupoOrdenadoresConsultaExternaDto.setProfesionales(profesionales);
							}	
						}else{
							if(forma.getIdTipoReporte().equals(ConstantesBDManejoPaciente.codigoTipoReporteOrdenarodesConsultaExternaGrupoClase+"")){
								filtrosGrupoOrdenadoresConsultaExternaDto.setTipoReporte(NOMBRE_REPORTE_GRUPOS_SERV_CLASES_INVENT);
								
								filtrosGrupoOrdenadoresConsultaExternaDto=consultaExternaFacade.consultarIdentificadorOrdenadoresConsultaGrupoClase(filtrosGrupoOrdenadoresConsultaExternaDto, esquemaTarifarioArticulos,esquemaTarifarioServicios);
								
								if(filtrosGrupoOrdenadoresConsultaExternaDto.getGruposServicio().isEmpty()
										&&filtrosGrupoOrdenadoresConsultaExternaDto.getClasesInventario().isEmpty()){
									errores.add("",new ActionMessage(KEY_ERROR_SIN_ESQUEMAS_SIN_RESULTADOS));
								}
							}
						}
						if(errores.isEmpty()){
							generadorReporte=new GeneradorReporteIdentificadorOrdenadoresConsultaExterna(filtrosGrupoOrdenadoresConsultaExternaDto);
							
							JasperPrint reporteOriginal = generadorReporte.generarReporte();
			
							String nombreArchivoOriginal ="";
							if(forma.getTipoSalida().equals(EnumTiposSalida.PDF.getCodigo()+"")){
								nombreArchivoOriginal= generadorReporte.exportarReportePDF(reporteOriginal, NOMBRE_REPORTE);
							}else{
								if(forma.getTipoSalida().equals(EnumTiposSalida.HOJA_CALCULO.getCodigo()+"")){
									nombreArchivoOriginal= generadorReporte.exportarReporteExcel(reporteOriginal, NOMBRE_REPORTE);
								}
							}
							forma.setNombreArchivoGenerado(nombreArchivoOriginal);
							
							forma.setHayMensajeExito(true);
						}
							
					}else{
						if(forma.getTipoSalida().equals(EnumTiposSalida.PLANO.getCodigo()+"")){
							List<OrdenadoresConsultaExternaPlanoDto> ordenadoresConsultaExternaPlano=new ArrayList<OrdenadoresConsultaExternaPlanoDto>(0);
							if(forma.getIdTipoReporte().equals(ConstantesBDManejoPaciente.codigoTipoReporteOrdenarodesConsultaExternaProf+"")){
								filtrosGrupoOrdenadoresConsultaExternaDto.setTipoReporte(NOMBRE_REPORTE_PROFESIONALES);
								ordenadoresConsultaExternaPlano=consultaExternaFacade.consultarIdentificadorOrdenadoresConsultaPlano(filtrosGrupoOrdenadoresConsultaExternaDto, esquemaTarifarioArticulos,esquemaTarifarioServicios);
							}else{
								if(forma.getIdTipoReporte().equals(ConstantesBDManejoPaciente.codigoTipoReporteOrdenarodesConsultaExternaGrupoClase+"")){
									filtrosGrupoOrdenadoresConsultaExternaDto.setTipoReporte(NOMBRE_REPORTE_GRUPOS_SERV_CLASES_INVENT);
									ordenadoresConsultaExternaPlano=consultaExternaFacade.consultarIdentificadorOrdenadoresConsultaGrupoClasePlano(filtrosGrupoOrdenadoresConsultaExternaDto, esquemaTarifarioArticulos,esquemaTarifarioServicios);
								}
							}
							
							if(ordenadoresConsultaExternaPlano.isEmpty()){
								errores.add("",new ActionMessage(KEY_ERROR_SIN_ESQUEMAS_SIN_RESULTADOS));
							}else{
								filtrosGrupoOrdenadoresConsultaExternaDto.setOrdenadoresConsultaExternaPlano(ordenadoresConsultaExternaPlano);
								
								generadorReporte=new GeneradorReporteIdentificadorOrdenadoresConsultaExterna(filtrosGrupoOrdenadoresConsultaExternaDto);
								
								JasperPrint reporteOriginal = generadorReporte.generarReporte();
								
								String nombreArchivoOriginal = generadorReporte.exportarReporteTextoPlano(reporteOriginal, NOMBRE_REPORTE);
								forma.setNombreArchivoGenerado(nombreArchivoOriginal);
								
								forma.setHayMensajeExito(true);
							}
						}
					}
				}else{
					if(!articulos.isEmpty()){
						StringBuffer articulosSinTarifa=new StringBuffer("");
						for(int i=0;i<articulos.size();i++){
							MedicamentoInsumoAutorizacionOrdenDto medicamento=articulos.get(i);
							articulosSinTarifa.append(medicamento.getCodigo());
							if(i<(articulos.size()-1)){
								articulosSinTarifa.append(", ");
							}
						}
						errores.add("",new ActionMessage(KEY_ERROR_SIN_TARIFA_ARTICULOS,articulosSinTarifa));
					}
					if(!servicios.isEmpty()){
						StringBuffer serviciosSinTarifa=new StringBuffer("");
						for(int i=0;i<servicios.size();i++){
							ServicioAutorizacionOrdenDto servicio=servicios.get(i);
							serviciosSinTarifa.append(servicio.getCodigo());
							if(i<(servicios.size()-1)){
								serviciosSinTarifa.append(", ");
							}
						}
						errores.add("",new ActionMessage(KEY_ERROR_SIN_TARIFA_SERVICIOS,serviciosSinTarifa));
					}
				}
			}
			
		} catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}catch (IPSException ipse) {
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
		}catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		
		forma.setTipoSalida(ConstantesBD.codigoNuncaValido+"");
		forma.setPermitirPopUpImpresion(false);
		
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		return mapping.findForward(FORWARD_CONSULTAR_ORDENADORES);
	}
}
