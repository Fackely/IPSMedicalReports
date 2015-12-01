/**
 * 
 */
package com.princetonsa.action.tesoreria;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;

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
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.tesoreria.ConsultaAbonosPacienteForm;
import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.tesoreria.DtoFiltroConsultaAbonos;
import com.princetonsa.dto.tesoreria.DtoMovmimientosAbonos;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.AbonosYDescuentos;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.generadorReporte.tesoreria.consolidadoMovimientos.GeneradorReporteConsolidadoMovimientos;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.ICentroAtencionMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IPacientesMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IMovimientosAbonosMundo;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ILocalizacionServicio;

/**
 * @author axioma
 *
 */
public class ConsultaAbonosPacienteAction extends Action 
{
	 /**
	 * Método execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
													        ActionForm form, 
													        HttpServletRequest request, 
													        HttpServletResponse response) throws Exception
													        {
		Connection connection =null;
		try{
			if(form instanceof ConsultaAbonosPacienteForm)
			{
				ConsultaAbonosPacienteForm forma=(ConsultaAbonosPacienteForm)form;
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
				PersonaBasica paciente=Utilidades.getPersonaBasicaSesion(request.getSession());
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

				String estado=forma.getEstado();
				Log4JManager.info("estado--->"+estado);

				connection = UtilidadBD.abrirConexion();
				forma.setPath(Utilidades.obtenerPathFuncionalidad(connection, 
						ConstantesBD.codigoFuncionalidadMenuConsultaAbonosPaciente));
				UtilidadBD.closeConnection(connection);

				String rutaLogo = institucion.getLogoJsp();
				String ubicacionLogo = institucion.getUbicacionLogo();
				forma.setMostrarMensajeInfo(ConstantesBD.acronimoNo);

				if(estado == null)
				{
					Log4JManager.error("Estado no valido ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					return mapping.findForward("paginaError");
				}	
				if(paciente.getCodigoPersona()<1)
				{
					return ComunAction.accionSalirCasoError(mapping, request, null, "Paciente no Cargado", "errors.paciente.noCargado", true);
				}

				if(!UtilidadValidacion.pacienteTieneMovimientoAbonos(paciente.getCodigoPersona()))
				{
					return ComunAction.accionSalirCasoError(mapping, request, null, "Paciente sin movimientos de Abonos", "Paciente sin movimientos de Abonos. Por favor verifique.", false);
				}

				if(estado.equals("empezar") )
				{
					try{
						HibernateUtil.beginTransaction();
						ILocalizacionServicio servicioLocalizacion=AdministracionFabricaServicio.crearLocalizacionServicio();
						forma.resetBusquedaAvanzada();
						forma.setCentrosAtencion(servicioLocalizacion.listarTodosCentrosAtencion());
						forma.setSaldoAbonos(UtilidadTexto.formatearValores(Utilidades.obtenerAbonosDisponiblesPaciente(paciente.getCodigoPersona(),ConstantesBD.codigoNuncaValido,usuario.getCodigoInstitucionInt())));
						AbonosYDescuentos abonos=new AbonosYDescuentos();
						forma.setAbonosRealizados(abonos.obtenerAbonosRecibosCaja(paciente.getCodigoPersona()));
						forma.getAbonosRealizados().addAll(abonos.obtenerAbonosAplicadosFacturas(paciente.getCodigoPersona()));
						forma.getAbonosRealizados().addAll(abonos.obtenerAbonosDevolucionRC(paciente.getCodigoPersona()));
						forma.getAbonosRealizados().addAll(abonos.obtenerAbonosGeneral(paciente.getCodigoPersona()));
						forma.getAbonosRealizados().addAll(abonos.obtenerAbonosAnulacionesRecibosCaja(paciente.getCodigoPersona()));
						//				forma.getAbonosRealizados().addAll(abonos.obtenerAbonosNotaDevolucion(paciente.getCodigoPersona()));
	
						ArrayList<DtoMovmimientosAbonos> listaMovimientos=forma.getAbonosRealizados();
						Collections.sort(listaMovimientos);
						Collections.reverse(listaMovimientos);
						forma.setAbonosRealizados(listaMovimientos);
	
						forma.setCentroAtencionBusqueda(usuario.getCodigoCentroAtencion());
	
						this.calcularTotalMovimientoListado(forma);
						HibernateUtil.endTransaction();
					}
					catch (Exception e) {
						Log4JManager.error(e);
						HibernateUtil.abortTransaction();
					}
					return mapping.findForward("principal");
				}
				else if(estado.equals("ejecutarBusqeda"))
				{
					return ejecutarBusquedaAvanzada(mapping, forma, paciente, usuario, request);
				}

				else if (estado.equals("mostrarDctoDetalleMovimientoAbono")) {

					forma.setDtoMovimientoSeleccionado(forma.getAbonosRealizados().get(forma.getIndiceMovimientoSeleccionado()));

					forma.getDtoMovimientoSeleccionado().setCentroAtencionDuenio(usuario.getCentroAtencion());
					return mapping.findForward("detalleDctoMovimientoAbono");
				}

				else if(estado.equals("empezarConsolidado") ){
					try{
						HibernateUtil.beginTransaction();
						boolean ordenarPorTipoMovimiento = true;
						IMovimientosAbonosMundo mundo = TesoreriaFabricaMundo.crearMovimientosAbonosMundo();
						ArrayList<DtoMovmimientosAbonos> listadoMovimientos = mundo.obtenerConsolidadoPorTipoMovimiento(paciente.getCodigoPersona());
	
						if (listadoMovimientos != null && listadoMovimientos.size() > 0) {
							ArrayList<DtoMovmimientosAbonos> listadoOrdenado = accionOrdenar(forma, listadoMovimientos, ordenarPorTipoMovimiento, false);
							forma.setListadoConsolidadoMovimientos(listadoOrdenado);
						}
	
						forma.setSaldoAbonos(UtilidadTexto.formatearValores(Utilidades.obtenerAbonosDisponiblesPaciente(paciente.getCodigoPersona(),ConstantesBD.codigoNuncaValido,usuario.getCodigoInstitucionInt())));
						HibernateUtil.endTransaction();
					}
					catch (Exception e) {
						Log4JManager.error(e);
						HibernateUtil.abortTransaction();
					}
					return mapping.findForward("principalConsolidado");
				}

				else if (estado.equals("imprimirReporteConsolidado")) {

					if (forma.getTipoSalida() != null && !forma.getTipoSalida().trim().equals("-1")) {
						boolean esConsolidado = true;
						imprimirReporteMovimientos(forma, usuario, paciente, rutaLogo, ubicacionLogo, institucion, esConsolidado);
						forma.setTipoSalida(null);
						forma.setEnumTipoSalida(null);
					}

					return mapping.findForward("principalConsolidado");
				}

				else if (estado.equals("imprimirReporteDetalle")) {
					boolean esConsolidado = false;
					imprimirReporteMovimientos(forma, usuario, paciente, rutaLogo, ubicacionLogo, institucion, esConsolidado);
					forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);

					return mapping.findForward("principal");
				}

				else if (estado.equals("inicializarNombreArchivo")) {
					forma.setNombreArchivoGenerado("");

					return mapping.findForward("principalConsolidado");
				}

				else if (estado.equals("inicializarNombreArchivoDetalle")) {
					forma.setNombreArchivoGenerado("");

					return mapping.findForward("principal");
				}

				else if (estado.equals("ordenarListadoAbonos")) {
					boolean ordenarListadoAbonos = true;
					boolean ordenarPorTipoMovimiento = false;

					accionOrdenar(forma, forma.getAbonosRealizados(), ordenarPorTipoMovimiento, ordenarListadoAbonos);
					return mapping.findForward("principal");
				}

				else if (estado.equals("recargar")) {
					return mapping.findForward("principal");
				}

				else if (estado.equals("abrirBusquedaAvanzada")) {
					forma.resetBusquedaAvanzada();
					forma.setCentroAtencionBusqueda(usuario.getCodigoCentroAtencion());
					return mapping.findForward("busquedaAvanzada");
				}

				else
				{
					Log4JManager.error("Estado no valido");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				Log4JManager.error("El form no es compatible con el form Forma.");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
			}

		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(connection);
		}
		return null;
}

	/**
	 * Este Método se encarga de realizar la búsqueda avanzada de los movimientos de abonos
	 * paciente.
	 * @param forma
	 * @author Yennifer Guerrero
	 * @param mapping 
	 * @param paciente 
	 * @param usuario 
	 * @param request 
	 */
	private ActionForward ejecutarBusquedaAvanzada(ActionMapping mapping, ConsultaAbonosPacienteForm forma, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request) {
		
		ActionErrors errores=null;
		errores=new ActionErrors();
		MessageResources mensajes=MessageResources.getMessageResources("com.servinte.mensajes.tesoreria.ConsultaConsolMovAbonosPacienteForm");
		String fechaActual = UtilidadFecha.getFechaActual();
		
		if (UtilidadTexto.isEmpty(forma.getIngresoBusqueda()) && forma.getCentroAtencionBusqueda() <= 0 
				&& UtilidadTexto.isEmpty(forma.getFechaInicialBusqueda()) && UtilidadTexto.isEmpty(forma.getFechaFinalBusqueda())
				&& forma.getTipoMovimientoBusqueda() <= 0) {
			
			errores.add("", new ActionMessage("errors.notEspecific", mensajes.getMessage("ConsultaConsolMovAbonosPacienteForm.requeridoAlMenosUnCampo")));
			
		}else if(!UtilidadTexto.isEmpty(forma.getFechaInicialBusqueda()) && 
				!UtilidadTexto.isEmpty(forma.getFechaFinalBusqueda())){
			
			if(UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getFechaFinalBusqueda(),forma.getFechaInicialBusqueda())){
				errores.add("FECHA FIN MENOR QUE FECHA INICIAL.", new ActionMessage(
								 "errors.fechaAnteriorIgualAOtraDeReferencia"," Final "+forma.getFechaFinalBusqueda()," Inicial "+forma.getFechaInicialBusqueda()));
			}
		}
		
		if (!UtilidadTexto.isEmpty(forma.getFechaInicialBusqueda()) &&
				UtilidadFecha.esFechaMenorQueOtraReferencia(fechaActual, forma.getFechaInicialBusqueda())) {
			
			errores.add("", new ActionMessage("errors.notEspecific", mensajes.getMessage("ConsultaConsolMovAbonosPacienteForm.fechaInicialPosteriorActual")));
		}
		
		if (!UtilidadTexto.isEmpty(forma.getFechaFinalBusqueda()) &&
				UtilidadFecha.esFechaMenorQueOtraReferencia(fechaActual, forma.getFechaFinalBusqueda())) {
			
			errores.add("", new ActionMessage("errors.notEspecific", mensajes.getMessage("ConsultaConsolMovAbonosPacienteForm.fechaFinalPosteriorActual")));
		}
	
		if (errores.isEmpty()) {
			try{
				HibernateUtil.beginTransaction();
				ILocalizacionServicio servicioLocalizacion=AdministracionFabricaServicio.crearLocalizacionServicio();
				forma.setCentrosAtencion(servicioLocalizacion.listarTodosCentrosAtencion());
				forma.setSaldoAbonos(UtilidadTexto.formatearValores(Utilidades.obtenerAbonosDisponiblesPaciente(paciente.getCodigoPersona(),ConstantesBD.codigoNuncaValido,usuario.getCodigoInstitucionInt())));
				DtoFiltroConsultaAbonos dtoFiltro=new DtoFiltroConsultaAbonos();
				dtoFiltro.setCentroAtencionBusqueda(forma.getCentroAtencionBusqueda());
				dtoFiltro.setFechaFinalBusqueda(forma.getFechaFinalBusqueda());
				dtoFiltro.setFechaInicialBusqueda(forma.getFechaInicialBusqueda());
				dtoFiltro.setIngresoBusqueda(forma.getIngresoBusqueda());
				dtoFiltro.setTipoMovimientoBusqueda(forma.getTipoMovimientoBusqueda());
				AbonosYDescuentos abonos=new AbonosYDescuentos();
				
				ArrayList<DtoMovmimientosAbonos> listadoResultadoBusqueda = abonos.obtenerAbonosGeneralAvanzada(paciente.getCodigoPersona(),dtoFiltro);
				
				if (listadoResultadoBusqueda != null && listadoResultadoBusqueda.size() > 0) {
					
					forma.setAbonosRealizados(listadoResultadoBusqueda);
					this.calcularTotalMovimientoListado(forma);
					
					return mapping.findForward("principal");
				
				}else{
					forma.setMostrarMensajeInfo(ConstantesBD.acronimoSi);
				}
				HibernateUtil.endTransaction();
			}
			catch (Exception e) {
				Log4JManager.error(e);
				HibernateUtil.abortTransaction();
			}
			
		}
		saveErrors(request, errores);
		return mapping.findForward("busquedaAvanzada");
		
	}

	/**
	 * 
	 * @param forma
	 */
	private void calcularTotalMovimientoListado(ConsultaAbonosPacienteForm forma) 
	{
		double totalAbono=0;
		for(DtoMovmimientosAbonos abono:forma.getAbonosRealizados())
		{
			if(abono.getOperacion().equals("suma"))
			{
				totalAbono=totalAbono+abono.getValor();
			}
			else if(abono.getOperacion().equals("resta"))
			{
				totalAbono=totalAbono-abono.getValor();
			}
			Log4JManager.info("-ABONO---->"+abono.getValor());
			Log4JManager.info("----->"+totalAbono);
		}
		forma.setSaldoAbonosListado(totalAbono+"");
	}
	
	/**
	 * Este Método se encarga de ordenar el listado de movimientos
	 * por fecha o por orden según el parámetro ordenarPorTipoMovimiento.
	 * @param listadoMovimientosAbonos
	 * @param ordenarPorTipoMovimiento
	 * @return
	 * @author Yennifer Guerrero
	 * @param forma 
	 */
	public ArrayList<DtoMovmimientosAbonos> accionOrdenar(ConsultaAbonosPacienteForm forma, 
			ArrayList<DtoMovmimientosAbonos> listadoMovimientosAbonos, 
			boolean ordenarPorTipoMovimiento, boolean ordenarListado){
		
		boolean ordenamiento = false;
		
		if (ordenarListado) {
			
			if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente"))
			{
				ordenamiento = true;
			}
			
			SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
			Collections.sort(listadoMovimientosAbonos, sortG);
			
		}else if (ordenarPorTipoMovimiento) {
			ordenamiento = ordenarPorTipoMovimiento;
			
			SortGenerico sortG=new SortGenerico("Orden",ordenamiento);
			Collections.sort(listadoMovimientosAbonos, sortG);
		}else{
			SortGenerico sortG=new SortGenerico("Fecha",ordenamiento);
			Collections.sort(listadoMovimientosAbonos, sortG);
		}
		
		return listadoMovimientosAbonos;
	}
	
	/**
	 * Este Método se encarga de
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @author Yennifer Guerrero
	 * @param rutaLogo 
	 * @param ubicacionLogo 
	 * @param institucion 
	 * @param esConsolidado2 
	 */
	private void imprimirReporteMovimientos(ConsultaAbonosPacienteForm forma,
			UsuarioBasico usuario, PersonaBasica paciente, String rutaLogo, String ubicacionLogo, InstitucionBasica institucion, boolean esConsolidado) {
		try{
			HibernateUtil.beginTransaction();
			DtoPaciente infoPaciente = new DtoPaciente();
			IPacientesMundo mundo = ManejoPacienteFabricaMundo.crearPacientesMundo();
			ICentroAtencionMundo centroAtencionMundo = AdministracionFabricaMundo.crearCentroAtencionMundo();
			DtoPersonas dtoPaciente = mundo.obtenerDatosPaciente(paciente.getCodigoPersona());
			
			com.servinte.axioma.orm.CentroAtencion centroAtencion = centroAtencionMundo.buscarPorCodigo(usuario.getCodigoCentroAtencion());
			
			infoPaciente.setCodigo(paciente.getCodigoPersona());
			infoPaciente.setNumeroIdentificacion(paciente.getCodigoTipoIdentificacionPersona() + " " +paciente.getNumeroIdentificacionPersona());
			infoPaciente.setNombreCompletoPersona(paciente.getNombrePersona());
			infoPaciente.setCentroAtencion(dtoPaciente.getDescripcionCentroAtencionDuenio());
			
			
			DtoMovmimientosAbonos dtoReporte = new DtoMovmimientosAbonos();
			
			dtoReporte.setInfoPaciente(infoPaciente);
			dtoReporte.setRutaLogo(rutaLogo);
			dtoReporte.setUbicacionLogo(ubicacionLogo);
			dtoReporte.setNombreInstitucion(usuario.getInstitucion());
			dtoReporte.setNit("NIT: " +institucion.getNit() + "-" + institucion.getDigitoVerificacion());
			if(centroAtencion.getEmpresasInstitucion()!=null)
				dtoReporte.setActividadEconomica(UtilidadTexto.isEmpty(centroAtencion.getEmpresasInstitucion().getActividadEco())?"":centroAtencion.getEmpresasInstitucion().getActividadEco());
			dtoReporte.setDireccionTelefono("Dirección: "+ centroAtencion.getDireccion() + ",  Teléfono: " + (UtilidadTexto.isEmpty(centroAtencion.getTelefono())?"":centroAtencion.getTelefono()));
			dtoReporte.setCentroAtencionReporte("Centro de Atención: " + usuario.getCentroAtencion());
			dtoReporte.setUsuarioGeneraReporte(usuario.getNombreUsuario() + "(" + usuario.getLoginUsuario() + ")");
			dtoReporte.setTotalSaldoPaciente(forma.getSaldoAbonos());
			
			int tipoSalida 	= Integer.parseInt(forma.getTipoSalida());
			
			if (tipoSalida == EnumTiposSalida.PLANO.getCodigo()) {
				dtoReporte.setEsArchivoPlano(true);
			}
			
			if (esConsolidado) {
				dtoReporte.setListadoConsolidadoMovimientos(forma.getListadoConsolidadoMovimientos());
				dtoReporte.setEsConsolidado(true);
			}else{
				dtoReporte.setListadoDetalleMovimientos(forma.getAbonosRealizados());
				dtoReporte.setEsConsolidado(false);
			}
			
			imprimirReporte(forma, dtoReporte);
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error("ERROR imprimirReporteMovimientos",e);
			HibernateUtil.abortTransaction();
		}
	}
	
	/**
	 * Este Método se encarga de
	 * @param forma
	 * @author Yennifer Guerrero
	 * @param dtoReporte 
	 */
	private void imprimirReporte(ConsultaAbonosPacienteForm forma, DtoMovmimientosAbonos dtoReporte) {
		
		int tipoSalida 	= Integer.parseInt(forma.getTipoSalida());
		String nombreArchivo="";
		
		if (tipoSalida > 0) {
			
			GeneradorReporteConsolidadoMovimientos generadorReporte = 
				new GeneradorReporteConsolidadoMovimientos(dtoReporte);
			
			JasperPrint reporte = generadorReporte.generarReporte();
			
			if (tipoSalida == EnumTiposSalida.PDF.getCodigo() ) {
				forma.setEnumTipoSalida(EnumTiposSalida.PDF);
				
			} else if (tipoSalida == EnumTiposSalida.HOJA_CALCULO.getCodigo() ) {
				forma.setEnumTipoSalida(EnumTiposSalida.HOJA_CALCULO);
				
			} else if (tipoSalida == EnumTiposSalida.PLANO.getCodigo() ) {
				forma.setEnumTipoSalida(EnumTiposSalida.PLANO);
			} 
			
			switch (forma.getEnumTipoSalida()) {
			
			case PDF:
				nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ReporteMovimientosAbonos");
				break;
				
			case PLANO:
				nombreArchivo = generadorReporte.exportarReporteTextoPlano(reporte, "ReporteMovimientosAbonos");
				break;
				
			case HOJA_CALCULO:
				nombreArchivo = generadorReporte.exportarReporteExcel(reporte, "ReporteMovimientosAbonos");
				break;
			}
			
			forma.setNombreArchivoGenerado(nombreArchivo);
			
		}
	}
}
