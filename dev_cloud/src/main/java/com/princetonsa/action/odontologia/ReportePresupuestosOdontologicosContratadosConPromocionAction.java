package com.princetonsa.action.odontologia;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.odontologia.ReportePresupuestosOdontologicosContratadosConPromocionForm;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.odontologia.DtoConsolidadoPresupuestoContratadoPorPromocion;
import com.princetonsa.dto.odontologia.DtoReportePresupuestosOdontologicosContratadosConPromocion;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.generadorReporte.odontologia.presupuestosOdontoContrataPromocion.GeneradorREportePlanoPresupuestosOdontologicosComtratadosConPromocion;
import com.servinte.axioma.generadorReporte.odontologia.presupuestosOdontoContrataPromocion.GeneradorREportePresupuestosOdontologicosContratadosConPromocion;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Ciudades;
import com.servinte.axioma.orm.Paises;
import com.servinte.axioma.orm.Programas;
import com.servinte.axioma.orm.PromocionesOdontologicas;
import com.servinte.axioma.orm.RecomSerproSerpro;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.PromocionesOdontologicasFabricaServicio;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.fabrica.odontologia.presupuesto.PresupuestoFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IMedicosServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.IPromocionesOdontologicasServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoOdontologicoServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ILocalizacionServicio;

/**
 * Esta clase se encarga de controlar los procesos de la funcionalidad
 * Reporte Presupuestos Odontol&oacute;gicos contratados con promoci&oacute;n en el m&oacute;dulo de odontolog&iacute;a
 *
 * @author  Javier Gonz&aacute;lez
 * @since  03/11/2010
 *
 */


public class ReportePresupuestosOdontologicosContratadosConPromocionAction extends Action{
	/**
	 * M&eacute;todo execute de la clase.
	 */
		@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {	
		
		if (form instanceof ReportePresupuestosOdontologicosContratadosConPromocionForm) {
			
			@SuppressWarnings("unused")
			ActionForward actionForward=null;
			ReportePresupuestosOdontologicosContratadosConPromocionForm forma= (ReportePresupuestosOdontologicosContratadosConPromocionForm) form;
			
			InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request
					.getSession());
			
			String estado = forma.getEstado();
			Log4JManager.info("estado " + estado);

						
			try {
				UtilidadTransaccion.getTransaccion().begin();
				ActionForward forward=null;
				 if (estado.equals("empezar")) {
					
					 forward= cargarDatosReportePresupuestosOdontologicosContProm(
							mapping, forma, ins, usuario);
					
				 }
				 if(estado.equals("cambiarPais")){
					 listarCiudades(forma);
					 forma.setTipoSalida(null);
						forma.setEnumTipoSalida(null);
						forma.setNombreArchivoGenerado(null);
						forward= mapping.findForward("principal");
				}
				if(estado.equals("cambiarCiudad")){
					listarCiudades(forma);
					listarCentrosAtencion(forma);
					listarPromocionesOdontologicas(forma);
					forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
					forma.setNombreArchivoGenerado(null);
					forward= mapping.findForward("principal");
				}
				if(estado.equals("cambiarRegion")){
					listarRegiones(forma);
					listarCentrosAtencion(forma);
					listarPromocionesOdontologicas(forma);
					forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
					forma.setNombreArchivoGenerado(null);
					forward= mapping.findForward("principal");
				}
				if(estado.equals("cambiarEmpresaInstitucion")){
					listarCentrosAtencion(forma);
					listarPromocionesOdontologicas(forma);
					forward= mapping.findForward("principal");
				}if(estado.equals("cambiarPromocion")){
					listarPromocionesOdontologicas(forma);
					forward= mapping.findForward("principal");
				}
				if(estado.equals("seleccionarPromOdonto")){
					
					forward= deshabilitaPrograma(forma,mapping);
				}
				if(estado.equals("imprimirReporte")){
					
					String nombreUsuario = usuario.getNombreUsuario();
					forma.getDtoFiltrosPresupuestosContratadosConPromocion().setNombreUsuarioProceso(nombreUsuario);
					
					if (forma.getTipoSalida() != null && !forma.getTipoSalida().trim().equals("-1")) {
					generarReportePresupuestosConPromo(forma, ins, request,usuario);
					forma.setEnumTipoSalida(null);
					forma.setTipoSalida(null);
					}
					forward= mapping.findForward("principal");
				}
				if(estado.equals("inicializarNomArch")){
					forma.setNombreArchivoGenerado("");
					forward= mapping.findForward("principal");
				}
				if(estado.equals("adicionarPrograma")){
					forma.getDtoFiltrosPresupuestosContratadosConPromocion().setCodigoPromocionOdontologica(ConstantesBD.codigoNuncaValido);
					forward= mapping.findForward("principal");																	
				}
				UtilidadTransaccion.getTransaccion().commit();
				return forward;
			} catch (Exception e) {
				UtilidadTransaccion.getTransaccion().rollback();
				Log4JManager.error("Error generando el reporte de presupuestos odontologicos contratados con promocion", e);
			}
		}
		return ComunAction.accionSalirCasoError(mapping, request, null, null,
				"errors.estadoInvalido", "errors.estadoInvalido", true);
	
	}
		
		
		private void generarReportePresupuestosConPromo(
			ReportePresupuestosOdontologicosContratadosConPromocionForm forma,
			InstitucionBasica ins, HttpServletRequest request,
			UsuarioBasico usuario) {
			
			int tipoSalida         = Integer.parseInt(forma.getTipoSalida());
		        String nombreArchivo="";
		        
		        if (tipoSalida > 0) {
		        	
		        	DtoReportePresupuestosOdontologicosContratadosConPromocion  filtroPresuContraPromo = forma.getDtoFiltrosPresupuestosContratadosConPromocion();
		        			        	
		        	ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion> consultaPresuPromo;
	        		
		        	if(tipoSalida==EnumTiposSalida.PDF.getCodigo()||tipoSalida==EnumTiposSalida.HOJA_CALCULO.getCodigo()){
	        			IPresupuestoOdontologicoServicio servicio = PresupuestoFabricaServicio.crearPresupuestoOdontologicoServicio();
	        			consultaPresuPromo = servicio.consolidarConsultaPresupuestosContratadosConPromocion(filtroPresuContraPromo);
	        		}else{
	        			IPresupuestoOdontologicoServicio servicio = PresupuestoFabricaServicio.crearPresupuestoOdontologicoServicio();
	        			consultaPresuPromo = servicio.ordenarResultadoConsultaPlano(filtroPresuContraPromo);
	        		}
	        		if (consultaPresuPromo != null && consultaPresuPromo.size()>0) {	
	        			filtroPresuContraPromo.setUbicacionLogo(ins.getUbicacionLogo());
	            		String rutaLogo = ins.getLogoJsp();
	            		filtroPresuContraPromo.setRutaLogo(rutaLogo);
	            		
	            		GeneradorREportePresupuestosOdontologicosContratadosConPromocion generadorReporte=null;
	        			JasperPrint reporte=null;
	        			GeneradorREportePlanoPresupuestosOdontologicosComtratadosConPromocion generadorReportePlano=null;
	                    
	                    if (tipoSalida == EnumTiposSalida.PDF.getCodigo()) {
	                    		generadorReporte =
	                            new GeneradorREportePresupuestosOdontologicosContratadosConPromocion(consultaPresuPromo, filtroPresuContraPromo);
	                    		reporte = generadorReporte.generarReporte();
	                            forma.setEnumTipoSalida(EnumTiposSalida.PDF);
	                            
	                    } else if (tipoSalida == EnumTiposSalida.HOJA_CALCULO.getCodigo() ) {
	                    		
	                    	generadorReporte=
	                            new GeneradorREportePresupuestosOdontologicosContratadosConPromocion(consultaPresuPromo, filtroPresuContraPromo);
	                    		reporte = generadorReporte.generarReporte();
	                            forma.setEnumTipoSalida(EnumTiposSalida.HOJA_CALCULO);
	                            
	                    } else if (tipoSalida == EnumTiposSalida.PLANO.getCodigo() ) {
	                    		generadorReportePlano =
	                    			
	                            new GeneradorREportePlanoPresupuestosOdontologicosComtratadosConPromocion(consultaPresuPromo,filtroPresuContraPromo);
	                    		reporte = generadorReportePlano.generarReporte();
	                            forma.setEnumTipoSalida(EnumTiposSalida.PLANO);
	                    }
	                    
	                    switch (forma.getEnumTipoSalida()) {
	                    
	                    case PDF:
	                            nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ReportePresupuestosOdontologicosContratadosConPromocion");
	                            break;
	                            
	                    case PLANO:
	                    		nombreArchivo = generadorReportePlano.exportarReporteTextoPlano(reporte,  "ReportePresupuestosOdontologicosContratadosConPromocion");
	                            break;
	                            
	                    case HOJA_CALCULO:
	                            nombreArchivo = generadorReporte.exportarReporteExcel(reporte, "ReportePresupuestosOdontologicosContratadosConPromocion");
	                            break;
	                    }
	                    
	                    forma.setNombreArchivoGenerado(nombreArchivo);
	                    
	    	        }
	        		 else{
	        		   	ActionErrors errores=new ActionErrors();
	     	            errores.add("No se encontraron resultados", new ActionMessage("errors.modOdontoReportesPresuConPro"));
	     	            saveErrors(request, errores);
	     	          
	     	        }
           
		        }      
		 }
        	        	
    			
        
			
	
		

		private ActionForward deshabilitaPrograma(
				ReportePresupuestosOdontologicosContratadosConPromocionForm forma,
				ActionMapping mapping) {
			if(forma.getDtoFiltrosPresupuestosContratadosConPromocion().getCodigoPromocionOdontologica()!=ConstantesBD.codigoNuncaValido){
				forma.setDtoSerProSerPro(new RecomSerproSerpro());
				forma.getDtoSerProSerPro().setProgramas(new Programas());
				forma.getDtoFiltrosPresupuestosContratadosConPromocion().setCodigoPrograma(ConstantesBD.codigoNuncaValidoLong);
				forma.setListaCodigoProgramaServicios("");
			}
			
			return mapping.findForward("principal");		
		}
		
		private ActionForward cargarDatosReportePresupuestosOdontologicosContProm(
				ActionMapping mapping,
				ReportePresupuestosOdontologicosContratadosConPromocionForm forma,
				InstitucionBasica ins, UsuarioBasico usuario) {
			
						
			forma.reset();	
			
			Connection con=HibernateUtil.obtenerConexion();		
			forma.setPath(Utilidades.obtenerPathFuncionalidad(con, 
			ConstantesBD.codigoFuncionalidadReportesPromociones));
			
			int codigoInstitucion = usuario.getCodigoInstitucionInt();
			
			String codigoPaisResidencia = ValoresPorDefecto.getPaisResidencia(codigoInstitucion);	
			
			String[] codigosPais=codigoPaisResidencia.split("-");
			
			if(!codigoPaisResidencia.trim().equals("-")){
				forma.getDtoFiltrosPresupuestosContratadosConPromocion().setCodigoPaisResidencia(codigosPais[0]);
			}
			ArrayList<Paises> listaPaises = AdministracionFabricaServicio.crearLocalizacionServicio().listarPaises();
			
			forma.setListaPaises(listaPaises);
			if (listaPaises.size() == 1) {
				forma.getDtoFiltrosPresupuestosContratadosConPromocion().setCodigoPaisResidencia(listaPaises.get(0).getCodigoPais());
			}
			
			String razonSocial = ins.getRazonSocial();
			
			forma.getDtoFiltrosPresupuestosContratadosConPromocion().setRazonSocial(razonSocial);
			
			listarCiudades(forma);
			listarRegiones(forma);
			
			forma.setEsMultiempresa(ValoresPorDefecto.getInstitucionMultiempresa(codigoInstitucion));
			if (forma.getEsMultiempresa().equals(ConstantesBD.acronimoSi)) {
				forma.setListaEmpresaInstitucion(FacturacionServicioFabrica.crearEmpresasInstitucionServicio().listarEmpresaInstitucion());
				forma.getDtoFiltrosPresupuestosContratadosConPromocion().setEsMultiempresa(true);
			}
			else{
				forma.getDtoFiltrosPresupuestosContratadosConPromocion().setEsMultiempresa(false);
			}
			
			listarCentrosAtencion(forma);
			forma.getDtoFiltrosPresupuestosContratadosConPromocion().setCiudadDeptoPais(new String());
			
			listarIndicativoContratoPresupuesto(forma);
			listarProfesionalesAuxiliaresyOdontologos(forma, usuario);
			
			//****** lista Profesionales con ocupación Odontologo
			listarProfesionalesOdontologos(forma, usuario);
								
			//*********listar promiciones odontologicas
			
			listarPromocionesOdontologicas(forma);
			
			//****** listarProgramasOdontologicos
			
			//***** Define si se Utiliza Programas Odontologicos en la Institución 
			
			String utilizaProgramasOdonto = ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(codigoInstitucion);
			forma.setUtilizaProgramasOdonto(utilizaProgramasOdonto);
			if(utilizaProgramasOdonto.equals(ConstantesBD.acronimoSi)){
				forma.setMostarFiltroPrograma(true);
			}
			
			//*****Define si se Utiliza Programas Odontologicos en la Institución 
			
			
			
			return mapping.findForward("principal");
		}

		private void listarPromocionesOdontologicas(
				ReportePresupuestosOdontologicosContratadosConPromocionForm forma) {
			IPromocionesOdontologicasServicio servicio = PromocionesOdontologicasFabricaServicio.crearIPromocionesOdontologicasServicio();
			Integer centroAtencion = forma.getDtoFiltrosPresupuestosContratadosConPromocion().getConsecutivoCentroAtencion();
			String ciudadDtoPais= forma.getDtoFiltrosPresupuestosContratadosConPromocion().getCiudadDeptoPais();
			long codigoRegion = forma.getDtoFiltrosPresupuestosContratadosConPromocion().getCodigoRegion();
			ArrayList<PromocionesOdontologicas> listadoPromocionesOdonto = null;
			
			if (forma.getCodigoPrograma()>0 && !UtilidadTexto.isEmpty(forma.getUtilizaProgramasOdonto())){
				//deshabilitaPromocion(forma);
				forma.setListadoPromocionesOdon(null);
			}else if (codigoRegion <= 0 && centroAtencion <=0 &&
					UtilidadTexto.isEmpty(ciudadDtoPais) || ciudadDtoPais.trim().equals(ConstantesBD.codigoNuncaValido +"")) {
				
				//lista todos las promociones del sistema
				listadoPromocionesOdonto =  servicio.listarPromocionesOdontologicas();
				
			}else if (!UtilidadTexto.isEmpty(ciudadDtoPais) && !ciudadDtoPais.trim().equals(ConstantesBD.codigoNuncaValido + "")) {
				
				String vec[]=forma.getDtoFiltrosPresupuestosContratadosConPromocion().getCiudadDeptoPais().split(ConstantesBD.separadorSplit);
				forma.getDtoFiltrosPresupuestosContratadosConPromocion().setCodigoCiudad(vec[0]);
				forma.getDtoFiltrosPresupuestosContratadosConPromocion().setCodigoDpto(vec[1]);
				forma.getDtoFiltrosPresupuestosContratadosConPromocion().setCodigoPais(vec[2]);
				
				if (centroAtencion <= 0) {	
					//lista todos por ciudad
					listadoPromocionesOdonto = servicio.listarTodosPorCiudad(vec[0], vec[2], vec[1]);
				} else {
					//lista todos por centro atencion y ciudad
					listadoPromocionesOdonto = servicio.listarTodosPorCentroAtencionYCiudad(
							centroAtencion, forma.getDtoFiltrosPresupuestosContratadosConPromocion().getCodigoCiudad(),
							forma.getDtoFiltrosPresupuestosContratadosConPromocion().getCodigoPais(), 
							forma.getDtoFiltrosPresupuestosContratadosConPromocion().getCodigoDpto());
				}
				
			}else if (codigoRegion > 0) {
				if (centroAtencion > 0) {
					//lista todos por region y centro de atencion
					listadoPromocionesOdonto = servicio.listarTodosPorCentroAtencionYRegion(
							centroAtencion, codigoRegion);
					
				} else {
					//listar por region
					listadoPromocionesOdonto = servicio.listarTodosPorRegion(codigoRegion);
				}
				
			}else {
				//lista todos por centro de atencion
				listadoPromocionesOdonto = servicio.listarTodosPorCentroAtencion(centroAtencion);
			}		
			
			if (listadoPromocionesOdonto != null && listadoPromocionesOdonto.size() > 0) {
				
				forma.setListadoPromocionesOdon(listadoPromocionesOdonto);
												
			} else {
				forma.setListadoPromocionesOdon(null);
			}
		}

		
		private void listarProfesionalesOdontologos(
				ReportePresupuestosOdontologicosContratadosConPromocionForm forma,
				UsuarioBasico usuario) {
			IMedicosServicio servicio = AdministracionFabricaServicio.crearMedicosServicio();
			int codigoInstitucionD = usuario.getCodigoInstitucionInt();
			
			ArrayList<DtoPersonas> listaProfesionalesOdont = servicio.obtenerMedicosOdontologos(codigoInstitucionD);
			
			if (listaProfesionalesOdont != null) {
				forma.setListaProfesionalesOdont(listaProfesionalesOdont);
				
			} else {
				forma.setListaProfesionalesOdont(null);
			}
		}
	
		private void listarProfesionalesAuxiliaresyOdontologos(
				ReportePresupuestosOdontologicosContratadosConPromocionForm forma,
				UsuarioBasico usuario) {
			IMedicosServicio servicio = AdministracionFabricaServicio.crearMedicosServicio();
			int codigoInstitucion = usuario.getCodigoInstitucionInt();
			
			ArrayList<DtoPersonas> listaProfesionales = servicio.obtenerTodosMedicosOdonto(codigoInstitucion);
			
			if (listaProfesionales != null) {
				forma.setListaProfesionales(listaProfesionales);
				
			} else {
				forma.setListaProfesionales(null);
			}
		}

		private void listarIndicativoContratoPresupuesto(
				ReportePresupuestosOdontologicosContratadosConPromocionForm forma) {
			IPresupuestoOdontologicoServicio servicio = PresupuestoFabricaServicio.crearPresupuestoOdontologicoServicio();
			
			ArrayList<DtoIntegridadDominio> listaIndicativoContrato = servicio.listarIndicativoContrato();
			
			if (listaIndicativoContrato != null && listaIndicativoContrato.size() > 0) {
				forma.setListadoIndicativoContrato(listaIndicativoContrato);
				
				String indicativo = forma.getDtoFiltrosPresupuestosContratadosConPromocion().getIndicativoContrato();
				
				for (DtoIntegridadDominio registro : listaIndicativoContrato) {
					if (indicativo.equals(registro.getAcronimo())) {
						forma.getDtoFiltrosPresupuestosContratadosConPromocion().setNombreIndicativo(registro.getDescripcion());
					}
				}
				
			} else {
				forma.setListadoIndicativoContrato(null);
			}
		}

		private void listarCentrosAtencion(
				ReportePresupuestosOdontologicosContratadosConPromocionForm forma) {
			ILocalizacionServicio servicio = AdministracionFabricaServicio.crearLocalizacionServicio();
			long empresaInstitucion = forma.getDtoFiltrosPresupuestosContratadosConPromocion().getCodigoEmpresaInstitucion();
			String ciudadDtoPais= forma.getDtoFiltrosPresupuestosContratadosConPromocion().getCiudadDeptoPais();
			long codigoRegion = forma.getDtoFiltrosPresupuestosContratadosConPromocion().getCodigoRegion();
			ArrayList<DtoCentrosAtencion> listaCentrosAtencion = null;
			
			if (codigoRegion <= 0 && empresaInstitucion <=0 &&
					UtilidadTexto.isEmpty(ciudadDtoPais) || ciudadDtoPais.trim().equals(ConstantesBD.codigoNuncaValido +"")) {
				
				//lista todos los centros de atención del sistema
				 listaCentrosAtencion =  AdministracionFabricaServicio.crearLocalizacionServicio().listarTodosCentrosAtencion();
				
			}else if (!UtilidadTexto.isEmpty(ciudadDtoPais) && !ciudadDtoPais.trim().equals(ConstantesBD.codigoNuncaValido + "")) {
				
				String vec[]=forma.getDtoFiltrosPresupuestosContratadosConPromocion().getCiudadDeptoPais().split(ConstantesBD.separadorSplit);
				forma.getDtoFiltrosPresupuestosContratadosConPromocion().setCodigoCiudad(vec[0]);
				forma.getDtoFiltrosPresupuestosContratadosConPromocion().setCodigoDpto(vec[1]);
				forma.getDtoFiltrosPresupuestosContratadosConPromocion().setCodigoPais(vec[2]);
				
				if (empresaInstitucion <= 0) {	
					//lista todos por ciudad
					listaCentrosAtencion = servicio.listarTodosPorCiudad(vec[0], vec[2], vec[1]);
				} else {
					//lista todos por empresa institucion y ciudad
					listaCentrosAtencion = servicio.listarTodosPorEmpresaInstitucionYCiudad(
							empresaInstitucion, forma.getDtoFiltrosPresupuestosContratadosConPromocion().getCodigoCiudad(),
							forma.getDtoFiltrosPresupuestosContratadosConPromocion().getCodigoPais(), 
							forma.getDtoFiltrosPresupuestosContratadosConPromocion().getCodigoDpto());
				}
				
			}else if (codigoRegion > 0) {
				if (empresaInstitucion > 0) {
					//lista todos por region y empresa institucion
					listaCentrosAtencion = servicio.listarTodosPorEmpresaInstitucionYRegion(
							empresaInstitucion, codigoRegion);
					
				} else {
					//listar por region
					listaCentrosAtencion = servicio.listarTodosPorRegion(codigoRegion);
				}
				
			} else {
				//lista todos por institucion
				listaCentrosAtencion = servicio.listarTodosPorEmpresaInstitucion(empresaInstitucion);
			}
			
			if (listaCentrosAtencion != null && listaCentrosAtencion.size()>0) {
				forma.setListaCentrosAtencion(listaCentrosAtencion);
			}else{
				forma.setListaCentrosAtencion(null);
			}
		}

		private void listarRegiones(
				ReportePresupuestosOdontologicosContratadosConPromocionForm forma) {
			Long codigoRegion= forma.getDtoFiltrosPresupuestosContratadosConPromocion().getCodigoRegion();
			
			if (codigoRegion == 0 || codigoRegion == ConstantesBD.codigoNuncaValidoLong  ) {
				forma.setListaRegiones(AdministracionFabricaServicio.crearLocalizacionServicio()
						.listarRegionesCoberturaActivas());
				forma.setDeshabilitaCiudad(false);
				forma.setDeshabilitaRegion(false);
			} else {
				forma.setListaRegiones(AdministracionFabricaServicio.crearLocalizacionServicio()
						.listarRegionesCoberturaActivas());
				forma.setDeshabilitaCiudad(true);
			}
		}

		private void listarCiudades(
				ReportePresupuestosOdontologicosContratadosConPromocionForm forma) {
			String ciudadDeptoPais= forma.getDtoFiltrosPresupuestosContratadosConPromocion().getCiudadDeptoPais();
			
			if(UtilidadTexto.isEmpty(ciudadDeptoPais) || ciudadDeptoPais.trim().equals(ConstantesBD.codigoNuncaValido + "")){
				
				forma.setListaCiudades(AdministracionFabricaServicio.crearLocalizacionServicio()
						.listarCiudadesPorPais(forma.getDtoFiltrosPresupuestosContratadosConPromocion().getCodigoPaisResidencia()));
				forma.setDeshabilitaCiudad(false);
				forma.setDeshabilitaRegion(false);
				
				if(forma.getListaCiudades()!=null && forma.getListaCiudades().size()==1){
					Ciudades ciudad = forma.getListaCiudades().get(0);
					
					String codigoCiudad=ciudad.getId().getCodigoCiudad()+ ConstantesBD.separadorSplit
					+ ciudad.getDepartamentos().getId().getCodigoDepartamento()+ ConstantesBD.separadorSplit
					+ ciudad.getPaises().getCodigoPais();
					
					forma.getDtoFiltrosPresupuestosContratadosConPromocion().setCiudadDeptoPais(codigoCiudad);
				}
			}else{
				forma.setListaCiudades(AdministracionFabricaServicio.crearLocalizacionServicio()
						.listarCiudadesPorPais(forma.getDtoFiltrosPresupuestosContratadosConPromocion().getCodigoPaisResidencia()));
				forma.setDeshabilitaRegion(true);
			}
		}
		
		
		

}
