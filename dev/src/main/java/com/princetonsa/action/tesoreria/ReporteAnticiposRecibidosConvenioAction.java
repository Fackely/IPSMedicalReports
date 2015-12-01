package com.princetonsa.action.tesoreria;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.tesoreria.ReporteAnticiposRecibidosConvenioForm;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.tesoreria.DtoAnticiposRecibidosConvenio;
import com.princetonsa.dto.tesoreria.DtoReporteAnticiposRecibidosConvenio;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.generadorReporte.tesoreria.anticiposRecibidosConvenio.GeneradorReporteAnticiposRecibidosConvenio;
import com.servinte.axioma.generadorReporte.tesoreria.anticiposRecibidosConvenio.GeneradorReporteAnticiposRecibidosConvenioPlano;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.ConceptosIngTesoreria;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.EstadosRecibosCaja;
import com.servinte.axioma.servicio.fabrica.TesoreriaFabricaServicio;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.fabrica.facturacion.convenio.ConveniosFabricaServicio;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.convenio.IConvenioServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IConceptosIngTesoreriaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ILocalizacionServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IRecibosCajaServicio;

/**
 * Esta clase se encarga de controlar los procesos de la funcionalidad
 * Reporte Anticipos Recibidos del Convenio, del m&oacute;dulo de
 * Tesorer&iacute;a
 *
 * @author  Carolina G&oacute;mez
 * @since  26/11/2010
 *
 */

public class ReporteAnticiposRecibidosConvenioAction extends Action {
	
	ReporteAnticiposRecibidosConvenioForm reporteForm;  
	
	/**
	 * M&eacute;todo execute de la clase.
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {	
		
		if (form instanceof ReporteAnticiposRecibidosConvenioForm) {
			
			@SuppressWarnings("unused")
			ActionForward actionForward=null;
			ReporteAnticiposRecibidosConvenioForm forma= (ReporteAnticiposRecibidosConvenioForm) form;
			
			InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
			
			String estado = forma.getEstado();
			Log4JManager.info("estado " + estado);

						
			try {
				HibernateUtil.beginTransaction();
				ActionForward forward=null;
				 if (estado.equals("empezar")) {
					 forward= cargarDatosInicio(mapping, forma, ins, usuario);		
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
				if(estado.equals("imprimirReporte")){
					
					generarReporte(request, forma, ins, usuario);
					forma.setEnumTipoSalida(null);
					forma.setTipoSalida(null);
					forward= mapping.findForward("principal");
				}
				if(estado.equals("inicializarNomArch")){
					forma.setNombreArchivoGenerado("");
					forward= mapping.findForward("principal");
				}
				HibernateUtil.endTransaction();
				return forward;
			} catch (Exception e) {
				Log4JManager.error(e);
				HibernateUtil.abortTransaction();
				Log4JManager.error("Error generando el reporte anticipos recibidos del convenio", e);
			}
		}
		return ComunAction.accionSalirCasoError(mapping, request, null, null,
				"errors.estadoInvalido", "errors.estadoInvalido", true);
	}
	
	/**
	 * M&eacute;todo encargado de generar el reporte con la 
	 * listaResultado de la consulta que se imprimir&acute; 
	 * posteriormente
	 * @param request
	 * @param forma
	 * @param ins
	 * @author Diana Carolina G
	 */
	private void generarReporte(HttpServletRequest request,
			ReporteAnticiposRecibidosConvenioForm forma, InstitucionBasica ins, UsuarioBasico usuario) {
		
		forma.getDtoFiltros().setUbicacionLogo(ins.getUbicacionLogo());
		String rutaLogo = ins.getLogoJsp();
		
		String nombreUsuario = usuario.getNombreUsuario();
		forma.getDtoFiltros().setNombreUsuarioProceso(nombreUsuario);
		
		forma.getDtoFiltros().setRutaLogo(rutaLogo);
		
		IRecibosCajaServicio servicioRC=TesoreriaFabricaServicio.crearRecibosCajaServicio();
		DtoReporteAnticiposRecibidosConvenio filtros=forma.getDtoFiltros();
		ArrayList<DtoAnticiposRecibidosConvenio> listaResultado=servicioRC.consolidarConsultaAnticiposRecibidosConvenio(filtros);
		
		if(listaResultado!=null && listaResultado.size() > 0){
			imprimirReporte(forma, listaResultado);
		}
		else
		{
			forma.setEstado("sinDatos");
		}
	}

	/**
	 * M&eacute;todo encargado de enviar la informaci&oacute;n 
	 * necesaria para imprimir el reporte dependiendo del tipo de salida
	 * PDF,  HOJA_CALCULO, PLANO
	 * enviando toda la informa
	 * @param forma
	 * @param listaResultado
	 * @author Diana Carolina G
	 */
	private void imprimirReporte(ReporteAnticiposRecibidosConvenioForm forma,
			ArrayList<DtoAnticiposRecibidosConvenio> listaResultado) {
		
		int tipoSalida 	= Integer.parseInt(forma.getTipoSalida());
		String nombreArchivo="";
		
		if (tipoSalida > 0) {
			
			ArrayList<DtoAnticiposRecibidosConvenio> listadoResultado = listaResultado;
			DtoReporteAnticiposRecibidosConvenio filtroAnticipos = forma.getDtoFiltros();
			
			GeneradorReporteAnticiposRecibidosConvenio generadorReporte = 
				new GeneradorReporteAnticiposRecibidosConvenio(listadoResultado, filtroAnticipos);
			
			GeneradorReporteAnticiposRecibidosConvenioPlano generadorReportePlano =
				new GeneradorReporteAnticiposRecibidosConvenioPlano (listadoResultado, filtroAnticipos); 
			
			
			JasperPrint reporte = generadorReporte.generarReporte();
			
			
			if (tipoSalida == EnumTiposSalida.PDF.getCodigo() ) {
				forma.setEnumTipoSalida(EnumTiposSalida.PDF);
				
			} else if (tipoSalida == EnumTiposSalida.HOJA_CALCULO.getCodigo() ) {
				forma.setEnumTipoSalida(EnumTiposSalida.HOJA_CALCULO);
				
			}  else if (tipoSalida == EnumTiposSalida.PLANO.getCodigo() ) {
				forma.setEnumTipoSalida(EnumTiposSalida.PLANO);
			} 
			
			switch (forma.getEnumTipoSalida()) {
			
			case PDF:
				nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ReporteAnticiposRecibidosDelConvenio");
				break;
				
			case PLANO:
				JasperPrint reportePlano = generadorReportePlano.generarReporte();
				nombreArchivo = generadorReportePlano.exportarReporteTextoPlano(reportePlano, "ReportePresupuestosOdontologicosContratadosPlano");
				break; 
				
			case HOJA_CALCULO:
				nombreArchivo = generadorReporte.exportarReporteExcel(reporte, "ReporteAnticiposRecibidosDelConvenio");
				break;
			}
			
			forma.setTipoSalida(null);
			forma.setEnumTipoSalida(null);
			forma.setNombreArchivoGenerado(nombreArchivo);
		
		}
	}
	
	/**
	 * M&eacute;todo encargado de inicializar los criterios de
	 * b&uacute;squeda por los cuales se genera el Reporte Anticipos
	 * Recibidos del Convenio 
	 * @param ActionMapping mapping,
			ReporteAnticiposRecibidosConvenioForm forma, InstitucionBasica ins,
			UsuarioBasico usuario
	 * @author Diana Carolina G
	 * @return
	 */
	private ActionForward cargarDatosInicio(ActionMapping mapping,
			ReporteAnticiposRecibidosConvenioForm forma, InstitucionBasica ins,
			UsuarioBasico usuario) {
		
		Connection con=null;
		try{	
			forma.reset();	
				
			 con=UtilidadBD.abrirConexion();		
			 forma.setPath(Utilidades.obtenerPathFuncionalidad(con,ConstantesBD.codigoFuncionalidadMenuReportesTesoreria));
			 
			 int codigoInstitucion = usuario.getCodigoInstitucionInt();
				String codigoPaisResidencia = ValoresPorDefecto.getPaisResidencia(codigoInstitucion);
				String[] codigosPais=codigoPaisResidencia.split("-");
				
				String razonSocial = ins.getRazonSocial();
				
				forma.getDtoFiltros().setRazonSocial(razonSocial);
				
				
				if(!codigoPaisResidencia.trim().equals("-")){
					forma.getDtoFiltros().setCodigoPaisResidencia(codigosPais[0]);
				}
				forma.setListaPaises(AdministracionFabricaServicio.crearLocalizacionServicio().listarPaises());
	
				//listarCiudades*****
				listarCiudades(forma);
				//listarCiudades*****
					
					
				//listarRegiones******
				listarRegiones(forma);
				//listarRegiones******
				
				forma.setEsMultiempresa(ValoresPorDefecto.getInstitucionMultiempresa(codigoInstitucion));
				if (forma.getEsMultiempresa().equals(ConstantesBD.acronimoSi)) {
					forma.setListaEmpresaInstitucion(FacturacionServicioFabrica.crearEmpresasInstitucionServicio().listarEmpresaInstitucion());
					forma.getDtoFiltros().setEsMultiempresa(true);
				}
				else{
					forma.getDtoFiltros().setEsMultiempresa(false);
				}
				
				/********listarCentrosAtencion*********************/
				listarCentrosAtencion(forma);
				forma.getDtoFiltros().setCiudadDeptoPais(new String());
				/***********listarCentrosAtencion******************/
				
				/*******listarConvenios**************/
				listarConvenios(forma, codigoInstitucion);
				/*******listarConvenios**************/
				
				/******listarConceptos*******/
				listarConceptos(forma);
				/******listarConceptos*******/
				
				/*****listarEstadosRC******/
				listarEstadosRC(forma);
				/*****listarEstadosRC******/
		}
		catch (Exception e) {
			Log4JManager.error("ERROR ",e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
			
		 return mapping.findForward("principal");
	
  }

	/**
	 * M&eacute;todo encargado de listar los 
	 * estados de los recibos de caja
	 * @param forma
	 * @author Diana Carolina G
	 */
	private void listarEstadosRC(ReporteAnticiposRecibidosConvenioForm forma) {
		IRecibosCajaServicio servicioRC=TesoreriaFabricaServicio.crearRecibosCajaServicio();
		ArrayList<EstadosRecibosCaja> listaEstadosRC = servicioRC.obtenerEstadosRC();
		if(listaEstadosRC != null && listaEstadosRC.size()>0){
			forma.setListaEstadosRC(listaEstadosRC);
		}
		else{
			forma.setListaEstadosRC(null);
		}
	}

	/**
	 * M&eacute;todo encargado de listar los conceptos
	 * de tipo pago Anticipos Convenio Odontol&oacute;gicos
	 * @param forma
	 * @author Diana Carolina G
	 */
	private void listarConceptos(ReporteAnticiposRecibidosConvenioForm forma) {
		IConceptosIngTesoreriaServicio servicioConcepto=TesoreriaFabricaServicio.crearConceptosIngTesoreriaServicio();
		ArrayList<ConceptosIngTesoreria> listaConceptos=servicioConcepto.obtenerConceptosTipoIngAnticiposConvOdont();
		if(listaConceptos != null && listaConceptos.size()>0){
			
			if (listaConceptos.size() == 1) {
				
				forma.getDtoFiltros().setCodigoConceptoAnticipo(listaConceptos.get(0).getTipoIngTesoreria().getCodigo() + "");
				forma.getDtoFiltros().setMostrarConceptos(false);
			}
			
			forma.setListaConceptos(listaConceptos);
		}
		else{
			forma.setListaConceptos(null);
		}
	}


	/**
	 * M&eacute;todo encargado de listar los Convenios 
	 * existentes en el sistema tipo odontol&oacute; activos
	 * inactivos
	 * @param forma
	 * @param codigoInstitucion
	 * @author Diana Carolina G
	 */
	private void listarConvenios(ReporteAnticiposRecibidosConvenioForm forma,
			int codigoInstitucion) {
		IConvenioServicio servicioConvenio= ConveniosFabricaServicio.crearConvenioServicio();
		//int codigoInst = usuario.getCodigoInstitucionInt();
		ArrayList<Convenios> listaConvenios = servicioConvenio.listarConveniosActivosInactivosOdont(codigoInstitucion);
		
		if(listaConvenios != null && listaConvenios.size()>0){
			forma.setListaConvenios(listaConvenios);
		}
		else {
			forma.setListaConvenios(null);
		}
	}

	/**
	 * M&eacute;todo encargado de listar los centros de atenci&oacute;n
	 * existentes en el sistema
	 * @param forma
	 * @author Diana Carolina G
	 */
	private void listarCentrosAtencion(
			ReporteAnticiposRecibidosConvenioForm forma) {
		ILocalizacionServicio servicio = AdministracionFabricaServicio.crearLocalizacionServicio();
		long empresaInstitucion = forma.getDtoFiltros().getCodigoEmpresaInstitucion();
		String ciudadDtoPais= forma.getDtoFiltros().getCiudadDeptoPais();
		long codigoRegion = forma.getDtoFiltros().getCodigoRegion();
		ArrayList<DtoCentrosAtencion> listaCentrosAtencion = null;
		
		if (codigoRegion <= 0 && empresaInstitucion <=0 &&
				(UtilidadTexto.isEmpty(ciudadDtoPais) || ciudadDtoPais.trim().equals(ConstantesBD.codigoNuncaValido +""))) {
			
			//lista todos los centros de atención del sistema
			 listaCentrosAtencion =  AdministracionFabricaServicio.crearLocalizacionServicio().listarTodosCentrosAtencion();
			
		}else if (!UtilidadTexto.isEmpty(ciudadDtoPais) && !ciudadDtoPais.trim().equals(ConstantesBD.codigoNuncaValido + "")) {
			
			String vec[]=forma.getDtoFiltros().getCiudadDeptoPais().split(ConstantesBD.separadorSplit);
			forma.getDtoFiltros().setCodigoCiudad(vec[0]);
			forma.getDtoFiltros().setCodigoDpto(vec[1]);
			forma.getDtoFiltros().setCodigoPais(vec[2]);
			
			if (empresaInstitucion <= 0) {	
				//lista todos por ciudad
				listaCentrosAtencion = servicio.listarTodosPorCiudad(vec[0], vec[2], vec[1]);
			} else {
				//lista todos por empresa institucion y ciudad
				listaCentrosAtencion = servicio.listarTodosPorEmpresaInstitucionYCiudad(
						empresaInstitucion, forma.getDtoFiltros().getCodigoCiudad(),
						forma.getDtoFiltros().getCodigoPais(), 
						forma.getDtoFiltros().getCodigoDpto());
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


	/**
	 * M&eacute;todo encargado de listar las regiones de cobertura 
	 * @param forma
	 * @author Diana Carolina G
	 */
	private void listarRegiones(ReporteAnticiposRecibidosConvenioForm forma) {
		Long codigoRegion= forma.getDtoFiltros().getCodigoRegion();
		
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

	
	/**
	 * M&eacute;todo encargado de listar las ciudades existentes
	 * en el sistema que pertenecen a un pa&iacute;s
	 * @param forma
	 * @author Diana Carolina G
	 */
	private void listarCiudades(ReporteAnticiposRecibidosConvenioForm forma) {
		String ciudadDeptoPais= forma.getDtoFiltros().getCiudadDeptoPais();
		
		if(UtilidadTexto.isEmpty(ciudadDeptoPais) || ciudadDeptoPais.trim().equals(ConstantesBD.codigoNuncaValido + "")){
			
			forma.setListaCiudades(AdministracionFabricaServicio.crearLocalizacionServicio()
					.listarCiudadesPorPais(forma.getDtoFiltros().getCodigoPaisResidencia()));
			forma.setDeshabilitaCiudad(false);
			forma.setDeshabilitaRegion(false);
		}else{
			forma.setListaCiudades(AdministracionFabricaServicio.crearLocalizacionServicio()
					.listarCiudadesPorPais(forma.getDtoFiltros().getCodigoPaisResidencia()));
			forma.setDeshabilitaRegion(true);
		}
	}

}
