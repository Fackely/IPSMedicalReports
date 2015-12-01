package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.reportes.GeneradorReporte;

import com.princetonsa.actionform.facturacion.VentasEmpresaConvenioForm;
import com.princetonsa.dto.facturacion.DTOConveniosReporteValoresFacturadosPorConvenio;
import com.princetonsa.dto.facturacion.DTOFacturasConvenios;
import com.princetonsa.dto.facturacion.DtoReporteValoresFacturadosPorConvenio;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.generadorReporte.facturacion.valorFacturadoPorConvenio.GeneradorReportePlanoValoresFacturadosPorConvenio;
import com.servinte.axioma.generadorReporte.facturacion.valorFacturadoPorConvenio.GeneradorReporteValoresFacturadosPorConvenio;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.orm.Ciudades;
import com.servinte.axioma.orm.EmpresasInstitucion;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ILocalizacionServicio;

/**
 * 
 * Clase usada para consultar los valores facturados por convenio de acuerdo a
 * los criterios de b&uacute;squeda seleccionados.
 * 
 * @author Luis Fernando Hincapi&eacute; Ospina
 * @since 01/12/2010
 */
public class VentasEmpresaConvenioAction extends Action {

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger = Logger.getLogger(VentasEmpresaConvenioAction.class);

	/**
	 * M&eacute;todo execute de la clase.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception {
		Connection con = null;
		try
		{
			if(form instanceof VentasEmpresaConvenioForm)
			{
				HibernateUtil.beginTransaction();
				VentasEmpresaConvenioForm forma = (VentasEmpresaConvenioForm) form;
				String estado = forma.getEstado();
	
				logger.info("Estado -->" + estado);
	
				con = UtilidadBD.abrirConexion();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				InstitucionBasica institucion = (InstitucionBasica) request.getSession().getAttribute("institucionBasica");
	
				if (estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de VentasEmpresaConvenioAction (null) ");
					request.setAttribute("codigoDescripcionError","errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{
					return empezar(forma,usuario,mapping,institucion);
				}
				else if(estado.equals("cambiarPais"))
				{
					listarCiudades(forma);
					forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
					return mapping.findForward("principal"); 
				}
				else if(estado.equals("cambiarCiudad"))
				{
					listarCentrosAtencion(forma);
					return cambiarCiudad(forma, mapping);
				}
				else if(estado.equals("cambiarEmpresaInstitucion"))
				{
					forma.getDtoFiltros().setNombreEmpresaInstitucion(consultarNombreEmpresaInstitucion(forma, forma.getDtoFiltros().getCodigoEmpresaInstitucion()));
					listarCentrosAtencion(forma);
					forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
					return mapping.findForward("principal");
				}
				else if (estado.equals("cambiarCentroAtencion"))
				{
					forma.getDtoFiltros().setNombreCentroAtencion(consultarNombreCentroAtencion(forma, forma.getDtoFiltros().getConsecutivoCentroAtencion()));
					return mapping.findForward("principal");
				}
				else if (estado.equals("cambiarEmpresa"))
				{
					listarConvenios(forma, forma.getDtoFiltros().getCodigoEmpresa());
					return mapping.findForward("principal");
				}
				else if (estado.equals("cambiarConvenio"))
				{
					return mapping.findForward("principal"); 
				}
				else if(estado.equals("imprimirReporte"))
				{
					String nombreUsuario = usuario.getNombreUsuario();
					forma.getDtoFiltros().setUsuarioProceso(nombreUsuario);
					consultarValoresFacturadosPorConvenio(forma, institucion, usuario, request);
					forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
					return mapping.findForward("principal");
				}
				else
				{
					forma.reset(usuario.getCodigoCentroAtencion() + "", usuario.getCodigoInstitucion());
					logger.warn("Estado no valido dentro del flujo de VALORES FACTURADOS POR CONVENIO");
					request.setAttribute("codigoDescripcionError","errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de VentasEmpresaConvenioForm");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
			}
		}
		catch (Exception e)
		{
			HibernateUtil.abortTransaction();
			Log4JManager.error(e);
			return mapping.findForward("paginaError");
		}
		finally
		{
			HibernateUtil.endTransaction();
			UtilidadBD.closeConnection(con);
		}
	}

	/**
	 * M&eacute;todo encargado de inicializar los valores de los objetos de l	a
	 * p&aacute;gina para la b&uacute;squeda de los datos del reporte.
	 * 
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param ins
	 * @return ActionForward
	 * 
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ActionForward empezar(VentasEmpresaConvenioForm forma,UsuarioBasico usuario, ActionMapping mapping, InstitucionBasica ins) throws Exception
	{
		forma.reset(usuario.getCodigoCentroAtencion() + "", usuario.getCodigoInstitucion());
		forma.getDtoFiltros().setRutaLogo(ins.getLogoJsp());
		forma.getDtoFiltros().setRazonSocial(ins.getRazonSocial());
		String sNit = UtilidadTexto.formatearValores(ins.getNit(), "###,##0");
		forma.getDtoFiltros().setNit(sNit.replaceAll(",","."));
		forma.getDtoFiltros().setActividadEconomica(ins.getActividadEconomica());
		int codigoInstitucion = usuario.getCodigoInstitucionInt();
		
		forma.setListaPaises(AdministracionFabricaServicio.crearLocalizacionServicio().listarPaises());
		String codigoPaisResidencia = ValoresPorDefecto.getPaisResidencia(codigoInstitucion);
		if ((forma.getListaPaises() != null) && (forma.getListaPaises().size() == 1))
		{
			forma.getDtoFiltros().setCodigoPais(forma.getListaPaises().get(0).getCodigoPais());
		}
		else
		{
			if (!UtilidadTexto.isEmpty(codigoPaisResidencia))
			{
				String[] codigosPais = codigoPaisResidencia.split("-");
				forma.getDtoFiltros().setCodigoPais(codigosPais[0]);
			}
		}

		listarCiudades(forma);

		if (ValoresPorDefecto.getInstitucionMultiempresa(codigoInstitucion).equals(ConstantesBD.acronimoSi))
		{
			forma.setMostarFiltroInstitucion(true);
			forma.setListaEmpresaInstitucion(FacturacionServicioFabrica.crearEmpresasInstitucionServicio().listarEmpresaInstitucion());
			if ((forma.getListaEmpresaInstitucion() != null) && (forma.getListaEmpresaInstitucion().size() == 1))
			{
				forma.getDtoFiltros().setCodigoEmpresaInstitucion(forma.getListaEmpresaInstitucion().get(0).getCodigo());
			}
			forma.getDtoFiltros().setMultiempresa(true);
		}
		else
		{
			forma.getDtoFiltros().setMultiempresa(false);
		}
		listarConvenios(forma,ConstantesBD.codigoNuncaValido);
		forma.setListaCentrosAtencion(AdministracionFabricaMundo.crearCentroAtencionMundo().listarTodosCentrosAtencion());
		return mapping.findForward("principal");
	}

	/**
	 * M&eacute;todo encargado de establecer los c&oacute;digos de Pa&iacute;s,
	 * Departamento y Ciudad en la variable dtoFiltros de la forma..
	 * 
	 * @param forma
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void obtenerCodigosPaisDeptoCiudad(VentasEmpresaConvenioForm forma) {

		String codigoCiudadDeptoPais = forma.getDtoFiltros().getCiudadDeptoPais();
		if ((!UtilidadTexto.isEmpty(codigoCiudadDeptoPais))
				&& (!(codigoCiudadDeptoPais.trim().equals("-1")))) {

			String codigos[] = codigoCiudadDeptoPais.split(ConstantesBD.separadorSplit);
			forma.getDtoFiltros().setCodigoCiudad(codigos[0]);
			forma.getDtoFiltros().setCodigoDpto(codigos[1]);
			forma.getDtoFiltros().setCodigoPais(codigos[2]);

		}

	}

	/**
	 * 
	 * M&eacute;todo encargado de incializar o no la lista de 
	 * centros de atenci&oacute; dependiendo de la ciudad seleccionada.
	 * 
	 * @param forma
	 * @param mapping
	 * @return ActionForward
	 * 
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ActionForward cambiarCiudad(VentasEmpresaConvenioForm forma,
			ActionMapping mapping) {

		String codigoCiudad = forma.getDtoFiltros().getCiudadDeptoPais();
		if ((!UtilidadTexto.isEmpty(codigoCiudad)) && (!(codigoCiudad.trim().equals("-1")))) {
			obtenerCodigosPaisDeptoCiudad(forma);
			listarCentrosAtencion(forma);
		} else {
			forma.getDtoFiltros().setCodigoDpto("");
			forma.getDtoFiltros().setCodigoCiudad("");
		}

		return mapping.findForward("principal");

	}
	
	
	/**
	 * 
	 * M&eacute;todo encargado de listar las ciudades existentes en el sistema
	 * que pertenecen a un pa&iacute;s determinado.
	 * 
	 * @param forma 
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	private void listarCiudades(VentasEmpresaConvenioForm forma) {

		if (!UtilidadTexto.isEmpty(forma.getDtoFiltros().getCodigoPais())) {
			forma.setListaCiudades(AdministracionFabricaServicio.crearLocalizacionServicio()
					.listarCiudadesPorPais(forma.getDtoFiltros().getCodigoPais()));

			if ((forma.getListaCiudades()!=null) && (forma.getListaCiudades().size()==1)) {
				Ciudades ciudad = forma.getListaCiudades().get(0);

				String codigoCiudad = ciudad.getId().getCodigoCiudad()
						+ ConstantesBD.separadorSplit
						+ ciudad.getDepartamentos().getId().getCodigoDepartamento()
						+ ConstantesBD.separadorSplit
						+ ciudad.getPaises().getCodigoPais();

				forma.getDtoFiltros().setCiudadDeptoPais(codigoCiudad);
			} else {
				forma.getDtoFiltros().setCiudadDeptoPais("");
				forma.getDtoFiltros().setCodigoDpto("");
				forma.getDtoFiltros().setCodigoCiudad("");
			}
		} else {
			forma.setListaCiudades(AdministracionFabricaServicio
					.crearLocalizacionServicio().listarCiudades());
		}

		obtenerCodigosPaisDeptoCiudad(forma);
		listarCentrosAtencion(forma);
	}

	/**
	 * 
	 * M&eacute;todo encargado de consultar el nombre de la instituci&oacute;n.
	 * 
	 * @param forma
	 * @param codigoEmpresaInstitucion
	 * @return nombreEmpresaInstitucion
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 * 
	 */
	public String consultarNombreEmpresaInstitucion(VentasEmpresaConvenioForm forma, 
			long codigoEmpresaInstitucion) {

		String nombreEmpresaInstitucion = "";

		for (EmpresasInstitucion empresasInstitucion : forma
				.getListaEmpresaInstitucion()) {
			if (empresasInstitucion.getCodigo() == codigoEmpresaInstitucion) {
				nombreEmpresaInstitucion = empresasInstitucion.getRazonSocial();
			}
		}

		return nombreEmpresaInstitucion;

	}

	/**
	 * 
	 * M&eacute;todo encargado de consultar el nombre del centro de
	 * atenci&oacute;n.
	 * 
	 * @param forma
	 * @param consecutivoCentroAtencion
	 * @return nombreCentroAtencion
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 * 
	 */
	public String consultarNombreCentroAtencion(
			VentasEmpresaConvenioForm forma, int consecutivoCentroAtencion) {

		String nombreCentroAtencion = "";

		for (DtoCentrosAtencion centroAtencion : forma
				.getListaCentrosAtencion()) {
			if (centroAtencion.getConsecutivo() == consecutivoCentroAtencion) {
				nombreCentroAtencion = centroAtencion.getDescripcion();
			}
		}

		return nombreCentroAtencion;

	}

	/**
	 * M&eacute;todo encargado de listar los centros de atenci&oacute;n del sistema
	 * de acuerdo a unos criterios espec&iacute;ficos.
	 * 
	 * @param forma
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void listarCentrosAtencion(VentasEmpresaConvenioForm forma) {

		ILocalizacionServicio servicio = AdministracionFabricaServicio.crearLocalizacionServicio();
		ArrayList<DtoCentrosAtencion> listaCentrosAtencion = null;
		long empresaInstitucion = forma.getDtoFiltros().getCodigoEmpresaInstitucion();
		String ciudadDtoPais= forma.getDtoFiltros().getCiudadDeptoPais();	

		if (empresaInstitucion == 0) {
			empresaInstitucion = ConstantesBD.codigoNuncaValidoLong;
		}
		if (empresaInstitucion != ConstantesBD.codigoNuncaValidoLong) {
			if ((!UtilidadTexto.isEmpty(ciudadDtoPais)) && (!ciudadDtoPais.trim().equals("-1"))) {								
				String codigos[]=ciudadDtoPais.split(ConstantesBD.separadorSplit);
				listaCentrosAtencion = servicio.listarTodosPorEmpresaInstitucionYCiudad(
						empresaInstitucion, codigos[0],codigos[2], codigos[1]);
			} else {
				listaCentrosAtencion = AdministracionFabricaServicio.crearLocalizacionServicio()
						.listarTodosPorEmpresaInstitucion(empresaInstitucion);
			}
		} else {
			if (!UtilidadTexto.isEmpty(forma.getDtoFiltros().getCodigoPais())) {
				if ((!UtilidadTexto.isEmpty(ciudadDtoPais)) && (!ciudadDtoPais.trim().equals("-1"))) {
					String codigos[] = ciudadDtoPais.split(ConstantesBD.separadorSplit);
					listaCentrosAtencion = AdministracionFabricaServicio
							.crearLocalizacionServicio().listarTodosPorCiudad(
									codigos[0], codigos[2], codigos[1]);
				} else {
					listaCentrosAtencion =AdministracionFabricaMundo.crearCentroAtencionMundo().listarTodosCentrosAtencion(); 
						/*AdministracionFabricaServicio
							.crearLocalizacionServicio().listarTodosPorPais(
									forma.getDtoFiltros().getCodigoPais());*/
				}
			} else {
				listaCentrosAtencion = AdministracionFabricaServicio
						.crearLocalizacionServicio().listarTodosCentrosAtencion();
			}
		}
		if ((listaCentrosAtencion != null) && (listaCentrosAtencion.size()==1)) {
			forma.getDtoFiltros().setConsecutivoCentroAtencion(listaCentrosAtencion.get(0).getConsecutivo());
			forma.setListaCentrosAtencion(listaCentrosAtencion);
			forma.getDtoFiltros().setNombreCentroAtencion(consultarNombreCentroAtencion(forma, forma
					.getDtoFiltros().getConsecutivoCentroAtencion()));
		}
		forma.setListaCentrosAtencion(listaCentrosAtencion);

	}

	/**
	 * Lista los convenios activos e inactivos de una empresa seleccionada
	 * cuando el c&oacute;digo de la empresa es v&aacute;lido o todos los convenios
	 * cuando el c&oacute;digo de la empresa no es v&aacute;lido.
	 * 
	 * @param forma
	 * @param codigoEmpresa
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void listarConvenios(VentasEmpresaConvenioForm forma,
			int codigoEmpresa) {

		if (codigoEmpresa == 0) {
			codigoEmpresa = ConstantesBD.codigoNuncaValido;
		}
		forma.setListaConvenios(FacturacionServicioFabrica
				.crearConvenioServicio().listarConveniosPorEmpresa(
						codigoEmpresa));

	}

	/**
	 * 
	 * M&eacute;todo encargado de consultar los valores facturados por convenio.
	 * 
	 * @param forma
	 * @param ins
	 * @param usuario
	 * @param request
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 * 
	 */
	public void consultarValoresFacturadosPorConvenio(
			VentasEmpresaConvenioForm forma, InstitucionBasica ins,
			UsuarioBasico usuario, HttpServletRequest request) {

		DtoReporteValoresFacturadosPorConvenio dtoConsulta = new DtoReporteValoresFacturadosPorConvenio();

		DTOConveniosReporteValoresFacturadosPorConvenio convenio = new DTOConveniosReporteValoresFacturadosPorConvenio();
		convenio.setFechaInicialFactura(UtilidadFecha.conversionFormatoFechaAAp(forma
				.getDtoFiltros().getFechaInicial()));
		convenio.setFechaFinalFactura(UtilidadFecha.conversionFormatoFechaAAp(forma
				.getDtoFiltros().getFechaFinal()));
		convenio.setNombreCentroAtencion(forma.getDtoFiltros().getNombreCentroAtencion());
		convenio.setNombreInstitucion(forma.getDtoFiltros().getNombreEmpresaInstitucion());

		dtoConsulta.setFechaInicial(forma.getDtoFiltros().getFechaInicial());
		dtoConsulta.setFechaFinal(forma.getDtoFiltros().getFechaFinal());
		dtoConsulta.setCiudadDeptoPais(forma.getDtoFiltros().getCiudadDeptoPais());
		dtoConsulta.setCodigoPais(forma.getDtoFiltros().getCodigoPais());
		dtoConsulta.setCodigoDpto(forma.getDtoFiltros().getCodigoDpto());
		dtoConsulta.setCodigoCiudad(forma.getDtoFiltros().getCodigoCiudad());
		dtoConsulta.setConsecutivoCentroAtencion(forma.getDtoFiltros().getConsecutivoCentroAtencion());
		dtoConsulta.setNombreCentroAtencion(forma.getDtoFiltros().getNombreCentroAtencion());
		if (forma.getDtoFiltros().getCodigoEmpresa() == 0) {
			forma.getDtoFiltros().setCodigoEmpresa(ConstantesBD.codigoNuncaValido);
		}
		dtoConsulta.setCodigoEmpresa(forma.getDtoFiltros().getCodigoEmpresa());
		dtoConsulta.setNitEmpresa(forma.getDtoFiltros().getNitEmpresa());
		dtoConsulta.setNombreEmpresa(forma.getDtoFiltros().getNombreEmpresa());
		dtoConsulta.setCodigoConvenio(forma.getDtoFiltros().getCodigoConvenio());
		dtoConsulta.setNombreConvenio(forma.getDtoFiltros().getNombreConvenio());

		dtoConsulta.setMultiempresa(UtilidadTexto.getBoolean(ValoresPorDefecto
				.getInstitucionMultiempresa(Integer.parseInt(ins.getCodigo()))));
		if (dtoConsulta.isMultiempresa()) {
			dtoConsulta.setCodigoEmpresaInstitucion(forma.getDtoFiltros().getCodigoEmpresaInstitucion());
			dtoConsulta.setNombreEmpresaInstitucion(forma.getDtoFiltros().getNombreEmpresaInstitucion());
		}

		dtoConsulta.setUsuarioProceso(usuario.toString());
		dtoConsulta.setRazonSocial(ins.getRazonSocial());

		ArrayList<DTOFacturasConvenios> listaValoresFacturadosConvenio = FacturacionServicioFabrica
				.crearConvenioServicio().obtenerValoresFacturadosConvenio(dtoConsulta);

		if ((listaValoresFacturadosConvenio != null)
				&& (listaValoresFacturadosConvenio.size() > 0)) {
			convenio.setListaValoresFacturadosConvenios(listaValoresFacturadosConvenio);

			forma.setListaValoresFacturadosConvenio(listaValoresFacturadosConvenio);
			imprimirReporte(forma,convenio);
		} else {
			ActionErrors errores = new ActionErrors();
			MessageResources mensajes = MessageResources
					.getMessageResources("com.servinte.mensajes.facturacion.VentasEmpresaConvenioForm");

			errores.add("No se encontraron resultados", new ActionMessage(
						"errors.notEspecific",mensajes.getMessage("ventasEmpresaConvenio.noExisteRegistros")));
			saveErrors(request, errores);
			forma.setNombreArchivoGenerado(null);
		}

	}

	/**
	 * 
	 * M&eacute;todo encargado de imprimir el reporte.
	 * 
	 * @param forma
	 * @param convenio
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 * 
	 */
	private void imprimirReporte(VentasEmpresaConvenioForm forma, 
			DTOConveniosReporteValoresFacturadosPorConvenio convenio) {

		int tipoSalida 	= Integer.parseInt(forma.getTipoSalida());
		String nombreArchivo="";

		if (tipoSalida > 0) {

			GeneradorReporte generadorReporte = null;
			if(tipoSalida == EnumTiposSalida.PLANO.getCodigo() ) {
				generadorReporte = new GeneradorReportePlanoValoresFacturadosPorConvenio(
						forma.getDtoFiltros(), convenio);
			}else{
				generadorReporte = new GeneradorReporteValoresFacturadosPorConvenio(
						forma.getDtoFiltros(), convenio);
			}

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
					nombreArchivo = generadorReporte.exportarReportePDF(reporte, "Reporte Valores Facturados por Convenio");
					break;
				case PLANO:
					nombreArchivo = generadorReporte.exportarReporteTextoPlano(reporte, "Reporte Valores Facturados por Convenio");
					break;
				case HOJA_CALCULO:
					nombreArchivo = generadorReporte.exportarReporteExcel(reporte, "Reporte Valores Facturados por Convenio");
					break;
			}

			forma.setEnumTipoSalida(null);
			forma.setNombreArchivoGenerado(nombreArchivo);
			forma.setEstado("archivoGenerado");

		}

	}

}
