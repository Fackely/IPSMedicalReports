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
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.odontologia.ReportePresupuestosOdontologicosContratadosForm;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.odontologia.DtoConsolidadoPresupuestoContratadoPorEstado;
import com.princetonsa.dto.odontologia.DtoReportePresupuestosOdontologicosContratados;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.generadorReporte.odontologia.presupuestosOdontoContratados.GeneradorReportePresupuestosOdontoContratados;
import com.servinte.axioma.generadorReporte.odontologia.presupuestosOdontoContratados.GeneradorReportePresupuestosOdontoContratadosPlano;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.PaquetesOdontologicos;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.fabrica.odontologia.facturacion.PaquetesOdontologicosFabricaServicio;
import com.servinte.axioma.servicio.fabrica.odontologia.presupuesto.PresupuestoFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IMedicosServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.facturacion.IPaquetesOdontologicosServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoOdontologicoServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ILocalizacionServicio;

/**
 * Esta clase se encarga de controlar los procesos de la funcionalidad
 * Reporte Presupuestos Odontol&oacute;gicos en el m&oacute;dulo de odontolog&iacute;a
 *
 * @author  Carolina G&oacute;mez
 * @since  08/10/2010
 *
 */

public class ReportePresupuestosOdontologicosContratadosAction extends Action {

	ReportePresupuestosOdontologicosContratadosForm repPresOdontoContForm;
	/**
	 * M&eacute;todo execute de la clase.
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {	
		
		if (form instanceof ReportePresupuestosOdontologicosContratadosForm) {
			
			@SuppressWarnings("unused")
			ActionForward actionForward=null;
			ReportePresupuestosOdontologicosContratadosForm forma= (ReportePresupuestosOdontologicosContratadosForm) form;
			
			InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request
					.getSession());
			
			String estado = forma.getEstado();
			Log4JManager.info("estado " + estado);

						
			try {
				UtilidadTransaccion.getTransaccion().begin();
				ActionForward forward=null;
				 if (estado.equals("empezar")) {
					
					 forward= cargarDatosReportePresupuestosOdontologicosCont(
							mapping, forma, ins, usuario);
					
				}
				 if(estado.equals("cambiarPais")){
					 listarCiudades(forma);
					 forward= mapping.findForward("principal");
				}
				if(estado.equals("cambiarCiudad")){
					listarCiudades(forma);
					listarCentrosAtencion(forma);
					forward= mapping.findForward("principal");
				}
				if(estado.equals("cambiarRegion")){
					listarRegiones(forma);
					listarCentrosAtencion(forma);
					forward= mapping.findForward("principal");
				}
				if(estado.equals("cambiarEmpresaInstitucion")){
					listarCentrosAtencion(forma);
					forward= mapping.findForward("principal");
				}
				if(estado.equals("seleccionarPaquete")){
					deshabilitaPaquete(forma);
					forward= mapping.findForward("principal");
				}
				if(estado.equals("imprimirReporte")){
					
					String nombreUsuario = usuario.getNombreUsuario();
					forma.getDtoFiltrosPresupuestosContratados().setNombreUsuarioProceso(nombreUsuario);
	
					generarReportePresupuestos(forma, ins, request,usuario);
					forma.setEnumTipoSalida(null);
					forma.setTipoSalida(null);
					forward= mapping.findForward("principal");
				}
				if(estado.equals("inicializarNomArch")){
					forma.setNombreArchivoGenerado("");
					forward= mapping.findForward("principal");
				}
				if(estado.equals("inicializarFiltroValorFin")){
					forma.getDtoFiltrosPresupuestosContratados().setValorContratoFinal(null);
					forward= mapping.findForward("principal");
				}
				 if(estado.equals("insertarServicio"))  
			       {
			           forma.setRegistrosNuevos(forma.getRegistrosNuevos()+1);
			           forward= mapping.findForward("principal");			
			       }
				 UtilidadTransaccion.getTransaccion().commit();
				 return forward;
			} catch (Exception e) {
				Log4JManager.error(e);
				UtilidadTransaccion.getTransaccion().rollback();
				Log4JManager.error("Error generando el reporte de presupuestos odontologicos", e);
			}
		}
		return ComunAction.accionSalirCasoError(mapping, request, null, null,
				"errors.estadoInvalido", "errors.estadoInvalido", true);
	
	}

	private void deshabilitaPaquete(
			ReportePresupuestosOdontologicosContratadosForm forma) {
		int codigoPaquete = forma.getDtoFiltrosPresupuestosContratados().getCodigoPaqueteOdonto();
		
		if (!UtilidadTexto.isEmpty(codigoPaquete) && codigoPaquete>0 ) {
			forma.setDeshabilitaPrograma("true");
		}else {
			forma.setDeshabilitaPrograma("false");
		}
	}

	private ActionForward cargarDatosReportePresupuestosOdontologicosCont(
			ActionMapping mapping,
			ReportePresupuestosOdontologicosContratadosForm forma,
			InstitucionBasica ins, UsuarioBasico usuario) {
		forma.reset();	
		
		Connection con=HibernateUtil.obtenerConexion();		
		forma.setPath(Utilidades.obtenerPathFuncionalidad(con, 
				ConstantesBD.codigoFuncionalidadReportesOdontologia));
		
		
		int codigoInstitucion = usuario.getCodigoInstitucionInt();
		String codigoPaisResidencia = ValoresPorDefecto.getPaisResidencia(codigoInstitucion);
		String[] codigosPais=codigoPaisResidencia.split("-");
		
		String razonSocial = ins.getRazonSocial();
		
		forma.getDtoFiltrosPresupuestosContratados().setRazonSocial(razonSocial);
		
		
		if(!codigoPaisResidencia.trim().equals("-")){
			forma.getDtoFiltrosPresupuestosContratados().setCodigoPaisResidencia(codigosPais[0]);
		}
		forma.setListaPaises(AdministracionFabricaServicio.crearLocalizacionServicio().listarPaises());

		listarCiudades(forma);
		listarRegiones(forma);
		
		forma.setEsMultiempresa(ValoresPorDefecto.getInstitucionMultiempresa(codigoInstitucion));
		if (forma.getEsMultiempresa().equals(ConstantesBD.acronimoSi)) {
			forma.setListaEmpresaInstitucion(FacturacionServicioFabrica.crearEmpresasInstitucionServicio().listarEmpresaInstitucion());
			forma.getDtoFiltrosPresupuestosContratados().setEsMultiempresa(true);
		}
		else{
			forma.getDtoFiltrosPresupuestosContratados().setEsMultiempresa(false);
		}
		
		listarCentrosAtencion(forma);
		forma.getDtoFiltrosPresupuestosContratados().setCiudadDeptoPais(new String());
		
		listarIndicativoContratoPresupuesto(forma);
		listarProfesionalesAuxiliaresyOdontologos(forma, usuario);
		
		//****** lista Profesionales con ocupación Odontologo
		listarProfesionalesOdontologos(forma, usuario);
		
		//****** listarPaquetesOdontologicos
		
		listarPaquetesOdontologicos(forma);
		
		//****** listarPaquetesOdontologicos
		
		//***** Define si se Utiliza Programas Odontologicos en la Institución 
		int institucion = usuario.getCodigoInstitucionInt();
		String utilizaProgramasOdonto = ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(institucion);
		forma.setUtilizaProgramasOdonto(utilizaProgramasOdonto);
		
		//*****Define si se Utiliza Programas Odontologicos en la Institución 
		
		
		
		return mapping.findForward("principal");
	}

	private void listarPaquetesOdontologicos(
			ReportePresupuestosOdontologicosContratadosForm forma) {
		IPaquetesOdontologicosServicio servicio = PaquetesOdontologicosFabricaServicio.crearIPaquetesOdontologicosServicio();
		
		String codigoPaquete = null;
		String  descripcionPaquete = null; 
		int codigoEspecialidad = 0;
		
		ArrayList<PaquetesOdontologicos> listadoPaquetesOdonto = servicio
	 	.listarPaquetesOdontologicos(codigoPaquete, descripcionPaquete, codigoEspecialidad);
		
		if (listadoPaquetesOdonto != null && listadoPaquetesOdonto.size() > 0) {
			forma.setListadoPaquetesOdonto(listadoPaquetesOdonto);
			
			int paquete = forma.getDtoFiltrosPresupuestosContratados().getCodigoPaqueteOdonto();
			
			for (PaquetesOdontologicos registro : listadoPaquetesOdonto) {
				if (paquete == registro.getCodigoPk()) {
					forma.getDtoFiltrosPresupuestosContratados().setNombrePaquete(registro.getDescripcion());
				}
			}
			
		} else {
			forma.setListadoPaquetesOdonto(null);
		}
	}

	private void listarProfesionalesOdontologos(
			ReportePresupuestosOdontologicosContratadosForm forma,
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
			ReportePresupuestosOdontologicosContratadosForm forma,
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
			ReportePresupuestosOdontologicosContratadosForm forma) {
		IPresupuestoOdontologicoServicio servicio = PresupuestoFabricaServicio.crearPresupuestoOdontologicoServicio();
		
		ArrayList<DtoIntegridadDominio> listaIndicativoContrato = servicio.listarIndicativoContrato();
		
		if (listaIndicativoContrato != null && listaIndicativoContrato.size() > 0) {
			forma.setListadoIndicativoContrato(listaIndicativoContrato);
			
			String indicativo = forma.getDtoFiltrosPresupuestosContratados().getIndicativoContrato();
			
			for (DtoIntegridadDominio registro : listaIndicativoContrato) {
				if (indicativo.equals(registro.getAcronimo())) {
					forma.getDtoFiltrosPresupuestosContratados().setNombreIndicativo(registro.getDescripcion());
				}
			}
			
		} else {
			forma.setListadoIndicativoContrato(null);
		}
	}

	private void listarCentrosAtencion(
			ReportePresupuestosOdontologicosContratadosForm forma) {
		ILocalizacionServicio servicio = AdministracionFabricaServicio.crearLocalizacionServicio();
		long empresaInstitucion = forma.getDtoFiltrosPresupuestosContratados().getCodigoEmpresaInstitucion();
		String ciudadDtoPais= forma.getDtoFiltrosPresupuestosContratados().getCiudadDeptoPais();
		long codigoRegion = forma.getDtoFiltrosPresupuestosContratados().getCodigoRegion();
		ArrayList<DtoCentrosAtencion> listaCentrosAtencion = null;
		
		if (codigoRegion <= 0 && empresaInstitucion <=0 &&
				UtilidadTexto.isEmpty(ciudadDtoPais) || ciudadDtoPais.trim().equals(ConstantesBD.codigoNuncaValido +"")) {
			
			//lista todos los centros de atención del sistema
			 listaCentrosAtencion =  AdministracionFabricaServicio.crearLocalizacionServicio().listarTodosCentrosAtencion();
			
		}else if (!UtilidadTexto.isEmpty(ciudadDtoPais) && !ciudadDtoPais.trim().equals(ConstantesBD.codigoNuncaValido + "")) {
			
			String vec[]=forma.getDtoFiltrosPresupuestosContratados().getCiudadDeptoPais().split(ConstantesBD.separadorSplit);
			forma.getDtoFiltrosPresupuestosContratados().setCodigoCiudad(vec[0]);
			forma.getDtoFiltrosPresupuestosContratados().setCodigoDpto(vec[1]);
			forma.getDtoFiltrosPresupuestosContratados().setCodigoPais(vec[2]);
			
			if (empresaInstitucion <= 0) {	
				//lista todos por ciudad
				listaCentrosAtencion = servicio.listarTodosPorCiudad(vec[0], vec[2], vec[1]);
			} else {
				//lista todos por empresa institucion y ciudad
				listaCentrosAtencion = servicio.listarTodosPorEmpresaInstitucionYCiudad(
						empresaInstitucion, forma.getDtoFiltrosPresupuestosContratados().getCodigoCiudad(),
						forma.getDtoFiltrosPresupuestosContratados().getCodigoPais(), 
						forma.getDtoFiltrosPresupuestosContratados().getCodigoDpto());
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
			ReportePresupuestosOdontologicosContratadosForm forma) {
		Long codigoRegion= forma.getDtoFiltrosPresupuestosContratados().getCodigoRegion();
		
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
			ReportePresupuestosOdontologicosContratadosForm forma) {
		String ciudadDeptoPais= forma.getDtoFiltrosPresupuestosContratados().getCiudadDeptoPais();
		
		if(UtilidadTexto.isEmpty(ciudadDeptoPais) || ciudadDeptoPais.trim().equals(ConstantesBD.codigoNuncaValido + "")){
			
			forma.setListaCiudades(AdministracionFabricaServicio.crearLocalizacionServicio()
					.listarCiudadesPorPais(forma.getDtoFiltrosPresupuestosContratados().getCodigoPaisResidencia()));
			forma.setDeshabilitaCiudad(false);
			forma.setDeshabilitaRegion(false);
		}else{
			forma.setListaCiudades(AdministracionFabricaServicio.crearLocalizacionServicio()
					.listarCiudadesPorPais(forma.getDtoFiltrosPresupuestosContratados().getCodigoPaisResidencia()));
			forma.setDeshabilitaRegion(true);
		}
	}
	
	/**
	 * 
	 * @param forma
	 * @param ins
	 * @param request
	 */
	private void generarReportePresupuestos(
			ReportePresupuestosOdontologicosContratadosForm forma,
			InstitucionBasica ins, HttpServletRequest request, UsuarioBasico usuario ) {
		
		IPresupuestoOdontologicoServicio servicio = PresupuestoFabricaServicio.crearPresupuestoOdontologicoServicio();
		DtoReportePresupuestosOdontologicosContratados filtroPresupuesto = forma.getDtoFiltrosPresupuestosContratados();
		
		String tSalidaReporte ="";
		tSalidaReporte = forma.getTipoSalida();
		
		forma.getDtoFiltrosPresupuestosContratados().setUbicacionLogo(ins.getUbicacionLogo());
		String rutaLogo = ins.getLogoJsp();
		
		
		forma.getDtoFiltrosPresupuestosContratados().setRutaLogo(rutaLogo);
		
		ArrayList<DtoConsolidadoPresupuestoContratadoPorEstado> listadoResultado = servicio.consolidarConsultaPresupuestosContratados(filtroPresupuesto,tSalidaReporte);
		
			
		if (!Utilidades.isEmpty(listadoResultado)) {
			
			for (DtoConsolidadoPresupuestoContratadoPorEstado dto : listadoResultado) {
				
				dto.setDescCentroAtencionContrato(dto.getListadoConsolidadoPorEstado().get(0).getDescCentroAtencionContrato());
				dto.setDescripcionCiudad(dto.getListadoConsolidadoPorEstado().get(0).getDescripcionCiudad());
				dto.setDescripcionPais(dto.getListadoConsolidadoPorEstado().get(0).getDescripcionPais());
				dto.setDescripcionRegionCobertura(dto.getListadoConsolidadoPorEstado().get(0).getDescripcionRegionCobertura());
				dto.setDescripcionEmpresaInstitucion(dto.getListadoConsolidadoPorEstado().get(0).getDescripcionEmpresaInstitucion());
				dto.setNombreInstitucion(dto.getListadoConsolidadoPorEstado().get(0).getNombreInstitucion());
				
				
			}
			
			imprimirReporte(forma, listadoResultado);
		}
		else
		{
			ActionErrors errores=new ActionErrors();
			MessageResources mensajes=MessageResources.getMessageResources("com.servinte.mensajes.odontologia.ReportePresupuestosOdontologicosContratadosForm");
			errores.add("No se encontraron resultados", new ActionMessage("errors.notEspecific", mensajes.getMessage("ReportePresupuestosOdontologicosContratadosForm.informar.noExisteRegistros")));
			saveErrors(request, errores);
			forma.setNombreArchivoGenerado(null);
			
		}
		
	}
	

	private void imprimirReporte(ReportePresupuestosOdontologicosContratadosForm forma, 
			ArrayList<DtoConsolidadoPresupuestoContratadoPorEstado> listadoResultadoConsulta) {
		
		int tipoSalida 	= Integer.parseInt(forma.getTipoSalida());
		String nombreArchivo="";
		
		if (tipoSalida > 0) {
			
			ArrayList<DtoConsolidadoPresupuestoContratadoPorEstado> listadoResultado = listadoResultadoConsulta;
			DtoReportePresupuestosOdontologicosContratados filtroIngresos = forma.getDtoFiltrosPresupuestosContratados();
			
			GeneradorReportePresupuestosOdontoContratados generadorReporte = 
				new GeneradorReportePresupuestosOdontoContratados(listadoResultado, filtroIngresos);
			
			GeneradorReportePresupuestosOdontoContratadosPlano generadorReportePlano =
				new GeneradorReportePresupuestosOdontoContratadosPlano(listadoResultado, filtroIngresos);
			
			
			JasperPrint reporte = generadorReporte.generarReporte();
			//JasperPrint reportePlano = generadorReportePlano.generarReporte();
			
			if (tipoSalida == EnumTiposSalida.PDF.getCodigo() ) {
				forma.setEnumTipoSalida(EnumTiposSalida.PDF);
				
			} else if (tipoSalida == EnumTiposSalida.HOJA_CALCULO.getCodigo() ) {
				forma.setEnumTipoSalida(EnumTiposSalida.HOJA_CALCULO);
				
			}  else if (tipoSalida == EnumTiposSalida.PLANO.getCodigo() ) {
				forma.setEnumTipoSalida(EnumTiposSalida.PLANO);
			} 
			
			switch (forma.getEnumTipoSalida()) {
			
			case PDF:
				nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ReportePresupuestosOdontologicosContratados");
				break;
				
			case PLANO:
				JasperPrint reportePlano = generadorReportePlano.generarReporte(); //OJO
				nombreArchivo = generadorReportePlano.exportarReporteTextoPlano(reportePlano, "ReportePresupuestosOdontologicosContratadosPlano");
				break; 
				
			case HOJA_CALCULO:
				nombreArchivo = generadorReporte.exportarReporteExcel(reporte, "ReportePresupuestosOdontologicosContratados");
				break;
			}
			
			forma.setTipoSalida(null);
			forma.setEnumTipoSalida(null);
			forma.setNombreArchivoGenerado(nombreArchivo);
			
			
		}
	}

}
