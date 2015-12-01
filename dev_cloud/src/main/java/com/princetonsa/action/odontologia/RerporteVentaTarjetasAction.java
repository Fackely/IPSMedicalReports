package com.princetonsa.action.odontologia;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

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
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.odontologia.RerporteVentaTarjetasForm;
import com.princetonsa.dto.comun.DtoCheckBox;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.odontologia.DtoFiltroReporteIngresosTarjetasCliente;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.TarjetaCliente;
import com.servinte.axioma.dto.odontologia.ventaTarjeta.DtoResultadoReporteVentaTarjetas;
import com.servinte.axioma.generadorReporte.odontologia.ingresosTarjetaCliente.GeneradorReporteIngresosTarjetaCliente;
import com.servinte.axioma.generadorReporte.odontologia.ingresosTarjetaCliente.GeneradorReporteIngresosTarjetaClienteArchivoPlano;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IUsuariosServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ILocalizacionServicio;

/**
 * @author armando
 *
 */
public class RerporteVentaTarjetasAction extends Action {

	/**
	 * M&eacute;todo execute de la clase.
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {	
		
		if (form instanceof RerporteVentaTarjetasForm) {
			
			@SuppressWarnings("unused")
			ActionForward actionForward=null;
			RerporteVentaTarjetasForm forma = (RerporteVentaTarjetasForm) form;
			
			InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request
					.getSession());
			
			String estado = forma.getEstado();
			Log4JManager.info("estado " + estado);

						
			try {
				UtilidadTransaccion.getTransaccion().begin();
				ActionForward forward=null;
				if (estado.equals("empezar")) {
					forward= empezar(forma, usuario, mapping, ins);
				}	
				if(estado.equals("cambiarPais")){
					listarCiudades(forma, usuario, mapping);
					forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
					forma.setNombreArchivoGenerado(null);
					forward= mapping.findForward("principal");
				}
				if(estado.equals("cambiarCiudad")){
					listarCiudades(forma, usuario, mapping);
					listarCentrosAtencion(forma, usuario, mapping);
					forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
					forma.setNombreArchivoGenerado(null);
					forward= mapping.findForward("principal");
				}
				if(estado.equals("cambiarRegion")){
					listarRegiones(forma, usuario, mapping);
					listarCentrosAtencion(forma, usuario, mapping);
					forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
					forma.setNombreArchivoGenerado(null);
					forward= mapping.findForward("principal");
				}
				if(estado.equals("cambiarEmpresaInstitucion")){
					listarCentrosAtencion(forma, usuario, mapping);
					forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
					forma.setNombreArchivoGenerado(null);
					forward= mapping.findForward("principal");
				}
				
				if(estado.equals("cambiarClaseVenta")){
					forward= mapping.findForward("principal");
				}
				
				if(estado.equals("generarReporte")){
			
						String nombreUsuario = usuario.getNombreUsuario();
						forma.getFiltroIngresos().setNombreUsuario(nombreUsuario);
						
						generarReporteIngresos(forma, ins, request);
						forma.setEnumTipoSalida(null);
						forma.setTipoSalida(null);
						forma.setNombreArchivoGenerado(null);
						
						
						forward= mapping.findForward("principal");
				}
				
				if (estado.equals("imprimirReporte")) {
					if (forma.getTipoSalida() != null && !forma.getTipoSalida().trim().equals("-1")) {
						imprimirReporte(forma);
						forma.setTipoSalida(null);
						forma.setEnumTipoSalida(null);
					}
					
					forward= mapping.findForward("principal");
				}
				
				if(estado.equals("inicializarFiltroEdadFinal")){
					forma.getFiltroIngresos().setEdadFinal(null);
					forward= mapping.findForward("principal");
				}
				
				UtilidadTransaccion.getTransaccion().commit();
				return forward;
			} catch (Exception e) {
				UtilidadTransaccion.getTransaccion().rollback();
				Log4JManager.error("Error generando el reporte de ingresos odontologicos", e);
			}
		}
		return ComunAction.accionSalirCasoError(mapping, request, null, null,
				"errors.estadoInvalido", "errors.estadoInvalido", true);

	}

	/**
	 * Este m&eacute;todo se encarga de inicializar los valores de los objetos de la
	 * p&aacute;gina para la b&uacute;squeda de los criterios seleccione
	 * con el fin de realizar el reporte de los ingresos odontol&oacute;gicos.
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 * 
	 * @throws Exception
	 * @author Yennifer Guerrero
	 * @param ins 
	 */
	public ActionForward empezar(RerporteVentaTarjetasForm forma,
			UsuarioBasico usuario, ActionMapping mapping, InstitucionBasica ins)
			throws Exception {
		
		forma.reset();	
		
		Connection con = HibernateUtil.obtenerConexion();		
		forma.setPath(Utilidades.obtenerPathFuncionalidad(con, 
				ConstantesBD.codigoFuncionalidadReportesOdontologia));
		
		int codigoInstitucion = usuario.getCodigoInstitucionInt();
		String codigoPaisResidencia = ValoresPorDefecto.getPaisResidencia(codigoInstitucion);
		String[] codigosPais=codigoPaisResidencia.split("-");
		
		String razonSocial = ins.getRazonSocial();
		
		forma.getFiltroIngresos().setRazonSocial(razonSocial);
		
		String fechaActual = UtilidadFecha.getFechaActual(con);
		forma.setFechaActual(fechaActual);
		
		if(!codigoPaisResidencia.trim().equals("-")){
			forma.getFiltroIngresos().setCodigoPaisResidencia(codigosPais[0]);
		}
		forma.setListaPaises(AdministracionFabricaServicio.crearLocalizacionServicio().listarPaises());
		
		listarCiudades(forma, usuario, mapping);
		listarRegiones(forma, usuario, mapping);
		
		forma.setEsMultiempresa(ValoresPorDefecto.getInstitucionMultiempresa(codigoInstitucion));
		if (forma.getEsMultiempresa().equals(ConstantesBD.acronimoSi)) {
			forma.setListaEmpresaInstitucion(FacturacionServicioFabrica.crearEmpresasInstitucionServicio().listarEmpresaInstitucion());
		}
		
		listarCentrosAtencion(forma, usuario, mapping);
		
		forma.getFiltroIngresos().setCiudadDeptoPais(new String());
		
		//cargar los usuarios.
		IUsuariosServicio usuarioServicio=AdministracionFabricaServicio.crearUsuariosServicio();
		forma.setUsuarios(usuarioServicio.obtenerUsuariosSistemas(usuario.getCodigoInstitucionInt(),false));
		
		listarSexoPaciente(forma, usuario, mapping);
		
		return mapping.findForward("principal");
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de generar el listado de las regiones de 
	 * cobertura. 
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * 
	 * @author Yennifer Guerrero
	 */
	private void listarRegiones(RerporteVentaTarjetasForm forma,
			UsuarioBasico usuario, ActionMapping mapping) {
		
		Long codigoRegion= forma.getFiltroIngresos().getCodigoRegion();
		
		if (codigoRegion <=0 ) {
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
	 * Este m&eacute;todo se encarga de listar las ciudades existentes en el sistema
	 * que pertenecen a un determinado pa&iacute;s.
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * 
	 * @author Yennifer Guerrero
	 */
	private void listarCiudades(RerporteVentaTarjetasForm forma,
			UsuarioBasico usuario, ActionMapping mapping) {
		
		String ciudadDeptoPais= forma.getFiltroIngresos().getCiudadDeptoPais();
		
		if(UtilidadTexto.isEmpty(ciudadDeptoPais) || ciudadDeptoPais.trim().equals("-1")){
			
			forma.setListaCiudades(AdministracionFabricaServicio.crearLocalizacionServicio()
					.listarCiudadesPorPais(forma.getFiltroIngresos().getCodigoPaisResidencia()));
			forma.setDeshabilitaCiudad(false);
			forma.setDeshabilitaRegion(false);
		}else{
			forma.setListaCiudades(AdministracionFabricaServicio.crearLocalizacionServicio()
					.listarCiudadesPorPais(forma.getFiltroIngresos().getCodigoPaisResidencia()));
			forma.setDeshabilitaRegion(true);
		}
	}

	/**
	 * Este m&eacute;todo se encarga de listar los centros de atención del sistema
	 * de acuerdo a unos criterios en especifico.
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * 
	 * @author Yennifer Guerrero
	 */
	private void listarCentrosAtencion(RerporteVentaTarjetasForm forma,
			UsuarioBasico usuario, ActionMapping mapping) {
		
		ILocalizacionServicio servicio = AdministracionFabricaServicio.crearLocalizacionServicio();
		long empresaInstitucion = forma.getFiltroIngresos().getCodigoEmpresaInstitucion();
		String ciudadDtoPais= forma.getFiltroIngresos().getCiudadDeptoPais();
		long codigoRegion = forma.getFiltroIngresos().getCodigoRegion();
		ArrayList<DtoCentrosAtencion> listaCentrosAtencion = null;
		
		if (codigoRegion <= 0 && empresaInstitucion <=0 &&
				(UtilidadTexto.isEmpty(ciudadDtoPais) || ciudadDtoPais.trim().equals("-1"))) {
			
			//lista todos los centros de atención del sistema
			 listaCentrosAtencion =  AdministracionFabricaServicio.crearLocalizacionServicio().listarTodosCentrosAtencion();
			
		}else if (!UtilidadTexto.isEmpty(ciudadDtoPais) && !ciudadDtoPais.trim().equals("-1")) {
			
			String vec[]=forma.getFiltroIngresos().getCiudadDeptoPais().split(ConstantesBD.separadorSplit);
			forma.getFiltroIngresos().setCodigoCiudad(vec[0]);
			forma.getFiltroIngresos().setCodigoDpto(vec[1]);
			forma.getFiltroIngresos().setCodigoPais(vec[2]);
			
			if (empresaInstitucion <= 0) {	
				//lista todos por ciudad
				listaCentrosAtencion = servicio.listarTodosPorCiudad(vec[0], vec[2], vec[1]);
			} else {
				//lista todos por empresa institucion y ciudad
				listaCentrosAtencion = servicio.listarTodosPorEmpresaInstitucionYCiudad(
						empresaInstitucion, forma.getFiltroIngresos().getCodigoCiudad(),
						forma.getFiltroIngresos().getCodigoPais(), 
						forma.getFiltroIngresos().getCodigoDpto());
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
	 * Este m&eacute;todo se encarga de listar los sexos de los pacientes.
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * 
	 * @author Yennifer Guerrero
	 */
	private void listarSexoPaciente(RerporteVentaTarjetasForm forma,
			UsuarioBasico usuario, ActionMapping mapping) {
		ArrayList<DtoCheckBox> lista = new ArrayList<DtoCheckBox>();
		
		Connection con=UtilidadBD.abrirConexion();
		
		ArrayList<HashMap<String, Object>> listadoSexos = Utilidades.obtenerSexos(con);
		
		UtilidadBD.closeConnection(con);
		
		for (HashMap<String, Object> hashMap : listadoSexos) {
			
			DtoCheckBox dto = new DtoCheckBox();
			dto.setCodigo(hashMap.get("codigo").toString());
			dto.setNombre(hashMap.get("nombre").toString());
			lista.add(dto);
		}
		
		forma.setListaSexoComprador(lista);
	}
	
	
	/**
	 * Este m&eacute;todo se encarga de 
	 * @param forma
	 * @param valIni
	 *
	 * @author Yennifer Guerrero
	 * @param response 
	 */
	private void generarReporteIngresos(RerporteVentaTarjetasForm forma,InstitucionBasica ins,
			 HttpServletRequest request) {
		
		ArrayList<DtoResultadoReporteVentaTarjetas> listaResultado = new ArrayList<DtoResultadoReporteVentaTarjetas>();
		
		String esMultiempresa = forma.getEsMultiempresa();
		
		if (esMultiempresa.equals(ConstantesBD.acronimoSi)) {
			
		}
		
		String rutaLogo = ins.getLogoJsp();
		String ubicacionLogo = ins.getUbicacionLogo();
		
		forma.setRutaLogo(rutaLogo);
		forma.getFiltroIngresos().setRutaLogo(rutaLogo);
		forma.setUbicacionLogo(ubicacionLogo);
		forma.getFiltroIngresos().setUbicacionLogo(ubicacionLogo);
		
		listaResultado = TarjetaCliente.consultarDatosReporte(forma.getFiltroIngresos());
		
		if (listaResultado != null && listaResultado.size() > 0) {
			forma.setListadoResultado(listaResultado);
			request.setAttribute("esPopUp", "true");
		} else {
			forma.setListadoResultado(null);
			forma.setEstado("sinDatos");
			request.setAttribute("esPopUp", "false");
		}
	}
	
	
	/**
	 * Este m&eacute;todo se encarga de enviar los datos necesarios a 
	 * los métodos que se encargan de la impresión del reporte.
	 * @param forma
	 * @return
	 *
	 * @author Yennifer Guerrero
	 * @param response 
	 */
	private void imprimirReporte(RerporteVentaTarjetasForm forma) {
		
		int tipoSalida 	= Integer.parseInt(forma.getTipoSalida());
		String nombreArchivo="";
		
		if (tipoSalida > 0) {
			
			ArrayList<DtoResultadoReporteVentaTarjetas> listadoResultado = forma.getListadoResultado();
			DtoFiltroReporteIngresosTarjetasCliente filtroIngresos = forma.getFiltroIngresos();
			
			GeneradorReporteIngresosTarjetaCliente generadorReporte = 
				new GeneradorReporteIngresosTarjetaCliente(listadoResultado, filtroIngresos);
			
			GeneradorReporteIngresosTarjetaClienteArchivoPlano generadorReportePlano = 
				new GeneradorReporteIngresosTarjetaClienteArchivoPlano(listadoResultado, filtroIngresos);
			
			JasperPrint reporte = generadorReporte.generarReporte();
			JasperPrint reportePlano = generadorReportePlano.generarReporte();
			
			if (tipoSalida == EnumTiposSalida.PDF.getCodigo() ) {
				forma.setEnumTipoSalida(EnumTiposSalida.PDF);
				
			} else if (tipoSalida == EnumTiposSalida.HOJA_CALCULO.getCodigo() ) {
				forma.setEnumTipoSalida(EnumTiposSalida.HOJA_CALCULO);
				
			} else if (tipoSalida == EnumTiposSalida.PLANO.getCodigo() ) {
				forma.setEnumTipoSalida(EnumTiposSalida.PLANO);
			} 
			
			switch (forma.getEnumTipoSalida()) {
			
			case PDF:
				nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ReporteIngresosTarjetaCliente");
				break;
				
			case PLANO:
				nombreArchivo = generadorReportePlano.exportarReporteTextoPlano(reportePlano, "ReporteIngresosTarjetaCliente");
				break;
				
			case HOJA_CALCULO:
				nombreArchivo = generadorReporte.exportarReporteExcel(reporte, "ReporteIngresosTarjetaCliente");
				break;
			}
			
			forma.setNombreArchivoGenerado(nombreArchivo);
			
		}
	}
}
