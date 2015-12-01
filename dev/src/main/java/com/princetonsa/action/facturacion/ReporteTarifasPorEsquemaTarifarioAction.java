package com.princetonsa.action.facturacion;

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
import util.reportes.GeneradorReporte;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.facturacion.ReporteTarifasPorEsquemaTarifarioForm;
import com.princetonsa.dto.facturacion.DTOEsquemasReporteTarifasPorEsquemaTarifario;
import com.princetonsa.dto.facturacion.DTOTarifasServicios;
import com.princetonsa.dto.facturacion.DtoReporteTarifasPorEsquemaTarifario;
import com.princetonsa.dto.facturacion.DtoTarifasPorEsquemaTarifario;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.generadorReporte.facturacion.tarifaPorEsquemaTarifario.GeneradorReportePlanoTarifasPorEsquemaTarifario;
import com.servinte.axioma.generadorReporte.facturacion.tarifaPorEsquemaTarifario.GeneradorReporteTarifasPorEsquemaTarifario;
import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.orm.EsquemasTarifarios;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.impl.administracion.EspecialidadesServicio;
import com.servinte.axioma.servicio.impl.facturacion.EsquemasTarifariosServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IEspecialidadesServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IEsquemasTarifariosServicio;

/**
 * 
 * Clase usada para consultar las tarifas de un esquema tarifario de acuerdo a
 * los criterios de búsqueda seleccionados.
 * 
 * @author Luis Fernando Hincapi&eacute; Ospina
 * @since 10/11/2010
 */
public class ReporteTarifasPorEsquemaTarifarioAction extends Action {

	/**
	 * M&eacute;todo execute de la clase.
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (form instanceof ReporteTarifasPorEsquemaTarifarioForm) {

			ReporteTarifasPorEsquemaTarifarioForm forma = (ReporteTarifasPorEsquemaTarifarioForm) form;

			InstitucionBasica ins = (InstitucionBasica) request.getSession()
					.getAttribute("institucionBasica");
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request
					.getSession());

			String estado = forma.getEstado();
			Log4JManager.info("estado " + estado);

			UtilidadTransaccion.getTransaccion().begin();
			try {
				if (estado.equals("empezar")) {
					return empezar(forma, usuario, mapping, ins);
				} else {
					if (estado.equals("cambiarEsquemaTarifario")) {
					forma.getDtoFiltros().setNombreEsquemaTarifario(
							consultarNombreEsquemaTarifario(forma,
									forma.getDtoFiltros().getCodigoEsquemaTarifario()));
						return mapping.findForward("principal");
					} else {
						if (estado.equals("cambiarEspecialidad")) {
							forma.getDtoFiltros().setNombreEspecialidad(
									consultarNombreEspecialidad(forma,
											forma.getDtoFiltros().getCodigoEspecialidad()));
							return mapping.findForward("principal");
						} else {
							if(estado.equals("imprimirReporte")){
								String nombreUsuario = usuario.getNombreUsuario();
								forma.getDtoFiltros().setUsuarioProceso(nombreUsuario);
	
								consultarTarifasPorEsquemaTarifario(forma,ins,usuario,request,mapping);
								forma.setEnumTipoSalida(null);
								forma.setTipoSalida(null);
								return mapping.findForward("principal");
							} else {
								if(estado.equals("inicializarNomArch")){
									forma.setNombreArchivoGenerado("");
									return mapping.findForward("principal");
								} else {
									if (estado.equals("insertarServicio")) {
										forma.setRegistrosNuevos(forma.getRegistrosNuevos() + 1);
										return mapping.findForward("principal");
									}
								}
							}
						}
					}
				}
				UtilidadTransaccion.getTransaccion().commit();
			} catch (Exception e) {
				UtilidadTransaccion.getTransaccion().rollback();
				Log4JManager.error("Error generando el reporte de tarifas por esquema tarifario",e);
			} 
			finally
			{
				UtilidadTransaccion.getTransaccion().commit();
			}
		}
		return ComunAction.accionSalirCasoError(mapping, request, null, null,
				"errors.estadoInvalido", "errors.estadoInvalido", true);
	}

	/**
	 * M&eacute;todo encargado de inicializar los valores de los objetos de la
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
	public ActionForward empezar(ReporteTarifasPorEsquemaTarifarioForm forma,
			UsuarioBasico usuario, ActionMapping mapping, InstitucionBasica ins)
			throws Exception {

		forma.reset();

		forma.getDtoFiltros().setRutaLogo(ins.getLogoJsp());
		forma.getDtoFiltros().setUbicacionLogo(ins.getUbicacionLogo());
		forma.getDtoFiltros().setRazonSocial(ins.getRazonSocial());

		IEsquemasTarifariosServicio esquemaTarifarioServicio = new EsquemasTarifariosServicio();
		ArrayList<EsquemasTarifarios> listaEsquemasTarifarios = esquemaTarifarioServicio
				.listarEsquemasTarifarios(false, true);
		forma.setListaEsquemasTarifarios(listaEsquemasTarifarios);

		IEspecialidadesServicio especialidadServicio = new EspecialidadesServicio();
		ArrayList<Especialidades> listaEspecialidades = (ArrayList<Especialidades>) especialidadServicio
				.listarEspecialidadesEnOrden();
		forma.setListaEspecialidades(listaEspecialidades);

		int institucion = usuario.getCodigoInstitucionInt();
		if (ValoresPorDefecto.getInstitucionMultiempresa(institucion).equals(ConstantesBD.acronimoSi)) {
			forma.setListaEmpresaInstitucion(
					FacturacionServicioFabrica.crearEmpresasInstitucionServicio().listarEmpresaInstitucion());
			if ((forma.getListaEmpresaInstitucion()!=null) && (forma.getListaEmpresaInstitucion().size()==1)) {
				forma.getDtoFiltros().setCodigoEmpresaInstitucion(
						forma.getListaEmpresaInstitucion().get(0).getCodigo());
			}
			forma.getDtoFiltros().setMultiempresa(true);
		} else {
			forma.getDtoFiltros().setMultiempresa(false);
		}

		String utilizaProgramasOdonto = ValoresPorDefecto
				.getUtilizanProgramasOdontologicosEnInstitucion(institucion);
		if (utilizaProgramasOdonto.equals(ConstantesBD.acronimoSi)) {
			forma.setMostarFiltroPrograma(true);
			forma.getDtoFiltros().setUtilizaProgramasOdontologicos(true);
		} else {
			forma.setMostarFiltroPrograma(false);
			forma.getDtoFiltros().setUtilizaProgramasOdontologicos(false);
		}

		return mapping.findForward("principal");
	}

	/**
	 * 
	 * M&eacute;todo encargado de consultar el nombre del esquema tarifario seleccionado.
	 * 
	 * @param forma
	 * @param codigoEsquemaTarifario
	 * @return nombreEsquemaTarifario
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 * 
	 */
	public String consultarNombreEsquemaTarifario(ReporteTarifasPorEsquemaTarifarioForm forma,
			int codigoEsquemaTarifario) {

		String nombreEsquemaTarifario = "";

		for (EsquemasTarifarios esquemaTarifario : forma.getListaEsquemasTarifarios()) {
			if (esquemaTarifario.getCodigo() == codigoEsquemaTarifario) {
				nombreEsquemaTarifario = esquemaTarifario.getNombre();
			}
		}

		return nombreEsquemaTarifario;

	}

	/**
	 * 
	 * M&eacute;todo encargado de consultar el nombre de la especialidad
	 * seleccionada.
	 * 
	 * @param forma
	 * @param codigoEspecialidad
	 * @return nombreEspecialidad
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 * 
	 */
	public String consultarNombreEspecialidad(ReporteTarifasPorEsquemaTarifarioForm forma,
			int codigoEspecialidad) {

		String nombreEspecialidad = "";

		for (Especialidades especialidad : forma.getListaEspecialidades()) {
			if (especialidad.getCodigo() == codigoEspecialidad) {
				nombreEspecialidad = especialidad.getNombre();
			}
		}

		return nombreEspecialidad;

	}

	/**
	 * 
	 * M&eacute;todo encargado de consultar las tarifas por esquema tarifario.
	 * 
	 * @param forma
	 * @param ins
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 * 
	 */
	public void consultarTarifasPorEsquemaTarifario(ReporteTarifasPorEsquemaTarifarioForm forma,
			InstitucionBasica ins,
			UsuarioBasico usuario,
			HttpServletRequest request,
			ActionMapping mapping){

		IEsquemasTarifariosServicio esquemaTarifarioServicio = 
			FacturacionServicioFabrica.crearEsquemasTarifariosServicio();

		DtoReporteTarifasPorEsquemaTarifario dtoConsulta = new DtoReporteTarifasPorEsquemaTarifario();

		DTOEsquemasReporteTarifasPorEsquemaTarifario esquemaTarifario = new DTOEsquemasReporteTarifasPorEsquemaTarifario();
		esquemaTarifario.setCodigoEsquemaTarifario(forma.getDtoFiltros().getCodigoEsquemaTarifario());
		esquemaTarifario.setNombreEsquemaTarifario(forma.getDtoFiltros().getNombreEsquemaTarifario());

		listarTarifas(forma,esquemaTarifario.getCodigoEsquemaTarifario());
		esquemaTarifario.setListaTarifas(forma.getListaTarifas());

		if ((esquemaTarifario.getListaTarifas() != null)
				&& (esquemaTarifario.getListaTarifas().size() > 0)) {
			dtoConsulta.setUtilizaProgramasOdontologicos(forma.getDtoFiltros().isUtilizaProgramasOdontologicos());

			dtoConsulta.setCodigoEsquemaTarifario(forma.getDtoFiltros().getCodigoEsquemaTarifario());
			dtoConsulta.setNombreEsquemaTarifario(forma.getDtoFiltros().getNombreEsquemaTarifario());
			dtoConsulta.setCodigoEspecialidad(forma.getDtoFiltros().getCodigoEspecialidad());
			dtoConsulta.setNombreEspecialidad(forma.getDtoFiltros().getNombreEspecialidad());

			if (dtoConsulta.isUtilizaProgramasOdontologicos()) {
				dtoConsulta.setCodigoPrograma(forma.getDtoFiltros().getProgramas().getCodigo());
				dtoConsulta.setNombrePrograma(forma.getDtoFiltros().getProgramas().getNombre());
				dtoConsulta.getProgramas().setCodigo(dtoConsulta.getCodigoPrograma());
				dtoConsulta.getProgramas().setNombre(dtoConsulta.getNombrePrograma());
			} else {
				if (forma.getDtoFiltros().getServicio().getCodigo() == "") {
					dtoConsulta.setCodigoServicio(String.valueOf(ConstantesBD.codigoNuncaValido));
				} else {
					dtoConsulta.setCodigoServicio(forma.getDtoFiltros().getServicio().getCodigo());
				}
				dtoConsulta.setDescripcionServicio(forma.getDtoFiltros().getServicio().getDescripcion());
				dtoConsulta.getServicio().setCodigo(dtoConsulta.getCodigoServicio());
				dtoConsulta.getServicio().setDescripcion(dtoConsulta.getDescripcionServicio());
			}

			dtoConsulta.setUsuarioProceso(usuario.toString());
			dtoConsulta.setRazonSocial(ins.getRazonSocial());
			dtoConsulta.setUbicacionLogo(ins.getUbicacionLogo());
			dtoConsulta.setMultiempresa(
					UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(Integer.parseInt(ins.getCodigo()))));

			forma.getDtoFiltros().setNombreEsquemaTarifario(esquemaTarifario.getNombreEsquemaTarifario());

			ArrayList<DTOTarifasServicios> listaTarifasEsquemaTarifario = esquemaTarifarioServicio
					.obtenerTarifasEsquemaTarifario(dtoConsulta);

			if ((listaTarifasEsquemaTarifario != null)
					&& (listaTarifasEsquemaTarifario.size() > 0)) {
				esquemaTarifario.setListaTarifasServicios(listaTarifasEsquemaTarifario);

				forma.setListaTarifasEsquemaTarifario(listaTarifasEsquemaTarifario);
				imprimirReporte(forma,esquemaTarifario);
			} else {
				ActionErrors errores = new ActionErrors();
				MessageResources mensajes = MessageResources
						.getMessageResources("com.servinte.mensajes.facturacion.ReporteTarifasPorEsquemaTarifarioForm");

				errores.add("No se encontraron resultados", new ActionMessage(
							"errors.notEspecific",mensajes.getMessage("reporteTarifasPorEsquemaTarifario.noExisteRegistros")));
				saveErrors(request, errores);
				forma.setNombreArchivoGenerado(null);
			}
		} else {
			ActionErrors errores = new ActionErrors();
			MessageResources mensajes = MessageResources
					.getMessageResources("com.servinte.mensajes.facturacion.ReporteTarifasPorEsquemaTarifarioForm");

			errores.add("No se encontraron resultados", new ActionMessage(
					"errors.notEspecific",mensajes.getMessage("reporteTarifasPorEsquemaTarifario.noExisteRegistros")));
			saveErrors(request, errores);
			forma.setNombreArchivoGenerado(null);
		}

	}

	/**
	 * M&eacute;todo encargado de listar las tarifas de un esquema tarifio.
	 * 
	 * @param forma
	 * @param codigoEsquemaTarifario
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void listarTarifas(ReporteTarifasPorEsquemaTarifarioForm forma,
			int codigoEsquemaTarifario) {

		IEsquemasTarifariosServicio servicio = FacturacionServicioFabrica
				.crearEsquemasTarifariosServicio();

		ArrayList<DtoTarifasPorEsquemaTarifario> listaTarifas = servicio
				.obtenerTarifas(codigoEsquemaTarifario);

		forma.setListaTarifas(listaTarifas);

	}

	/**
	 * 
	 * M&eacute;todo encargado de imprimir el reporte.
	 * 
	 * @param forma
	 * @param esquemaTarifario
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 * 
	 */
	private void imprimirReporte(ReporteTarifasPorEsquemaTarifarioForm forma, 
			DTOEsquemasReporteTarifasPorEsquemaTarifario esquemaTarifario) {

		int tipoSalida 	= Integer.parseInt(forma.getTipoSalida());
		String nombreArchivo="";

		if (tipoSalida > 0) {

			GeneradorReporte generadorReporte = null;
			if(tipoSalida == EnumTiposSalida.PLANO.getCodigo() ) {
				generadorReporte = new GeneradorReportePlanoTarifasPorEsquemaTarifario(
						forma.getDtoFiltros().isUtilizaProgramasOdontologicos(),
						esquemaTarifario);
			}else{
				DtoReporteTarifasPorEsquemaTarifario filtroIngresos = forma.getDtoFiltros();
				generadorReporte = new GeneradorReporteTarifasPorEsquemaTarifario(
						filtroIngresos, esquemaTarifario);
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
					nombreArchivo = generadorReporte.exportarReportePDF(reporte, "Reporte Tarifas por Esquema Tarifario");
					break;
				case PLANO:
					nombreArchivo = generadorReporte.exportarReporteTextoPlano(reporte, "Reporte Tarifas por Esquema Tarifario");
					break;
				case HOJA_CALCULO:
					nombreArchivo = generadorReporte.exportarReporteExcel(reporte, "Reporte Tarifas por Esquema Tarifario");
					break;
			}

			forma.setTipoSalida(null);
			forma.setEnumTipoSalida(null);
			forma.setNombreArchivoGenerado(nombreArchivo);

		}
	}

}
