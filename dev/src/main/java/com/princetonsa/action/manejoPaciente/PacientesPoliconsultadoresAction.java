package com.princetonsa.action.manejoPaciente;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.constantes.estadosJsp.IconstantesEstadosJsp;

import com.princetonsa.dto.administracion.DtoEspecialidades;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.manejoPaciente.DtoDetallesPacientesPoliconsultadores;
import com.princetonsa.dto.manejoPaciente.DtoPacientesPoliconsultadores;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.administracion.Especialidades;
import com.servinte.axioma.dto.manejoPaciente.PacientePoliconsultadoresPlanoDto;
import com.servinte.axioma.generadorReporte.manejoPaciente.pacientesPoliconsultadores.GeneradorReportePacientesPoliconsultadores;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IPacientesPoliconsultadoresMundo;
import com.servinte.axioma.orm.TiposIdentificacion;
import com.servinte.axioma.orm.TiposServicio;
import com.servinte.axioma.orm.ViasIngreso;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ITiposIdentificacionServicio;

/**
 * Fecha Febrero - 2012
 * 
 * @author cesgompe
 * 
 */

public class PacientesPoliconsultadoresAction extends Action {

	/** * Log */
	Logger logger = Logger.getLogger(PacientesPoliconsultadoresAction.class);

	/** * Contiene la lista de mensajes correspondiente a esta forma */
	MessageResources fuenteMensaje = MessageResources
			.getMessageResources("com.servinte.mensajes.manejoPaciente.PacientesPoliconsultadores");

	/**
	 * pacientesPoliconsultadores con metedos de CU
	 */
	IPacientesPoliconsultadoresMundo pacientesPoliconsultadores;

	/*----------------------------------------------------------------------------------
	 * 						METODO EXECUTE DEL ACTION
	 ----------------------------------------------------------------------------------*/
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (form instanceof PacientesPoliconsultadoresForm) {
			PacientesPoliconsultadoresForm forma = (PacientesPoliconsultadoresForm) form;
			ActionErrors errores = new ActionErrors();
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request
					.getSession());
			InstitucionBasica institucionActual = (InstitucionBasica) request
					.getSession().getAttribute("institucionBasica");
			String estado = forma.getEstado();
			PersonaBasica paciente=(PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			String tipoSalida=forma.getTipoSalida();
			// VALIDACIONES DE ESTADO Y RE DIRECCION A OPERACIONES
			// CORRESPONDIENTES
			if (estado.equals(IconstantesEstadosJsp.ESTADO_EMPEZAR)) {
				return valoreiniciales(forma, usuario, response, request,
						mapping);
			} else if ("determinarValidacionCampo".equals(estado)) {
				determinarValidacionIdentificacion(forma, request,
						usuario.getCodigoInstitucionInt());
				return mapping.findForward("numIdentificacionPaciente");
			} else if (estado.equals("validarPaciente")) {
				if(tipoSalida.equals("2")){
					determinarValidacionIdentificacion(forma, request,
							usuario.getCodigoInstitucionInt());
					return accionReportePlano(forma, usuario, mapping, request, 
							errores, institucionActual, paciente);
				}
				else {
				determinarValidacionIdentificacion(forma, request,
						usuario.getCodigoInstitucionInt());
				return accionValidarPaciente(forma, usuario, mapping, request,
						errores, institucionActual, paciente);
			}
		}
		}
		return mapping.findForward("principal");
	}

	/**
	 * Metodo de carga e inicializacion de elementos a usar en el cu
	 * 
	 * @param forma
	 * @param usuario
	 * @param response
	 * @param request
	 * @param mapping
	 * @return Estado inicial
	 */
	@SuppressWarnings("unchecked")
	private ActionForward valoreiniciales(PacientesPoliconsultadoresForm forma,
			UsuarioBasico usuario, HttpServletResponse response,
			HttpServletRequest request, ActionMapping mapping) {
		forma.reset();
		HibernateUtil.beginTransaction();
		try {
			pacientesPoliconsultadores = AdministracionFabricaMundo
					.crearPacientesPoliconsutadores();

			// --> Tipos Identificaci贸n
			// String[] listaTipoDeTiposIdentificacion = {
			// ConstantesIntegridadDominio.acronimoTipoTipoIdentificacionPersona,
			// ConstantesIntegridadDominio.acronimoTipoTipoIdentificacionAmbos
			// };

			// forma.setListTipIdent(pacientesPoliconsultadores.obtenerTipoIdentificacion(listaTipoDeTiposIdentificacion));
			forma.setListTipIdent(Utilidades.obtenerTiposIdentificacion(
					"ingresoPaciente", usuario.getCodigoInstitucionInt()));

			// Cargamos todas las vias de ingreso
			forma.setViaIngresoList(pacientesPoliconsultadores
					.obtenerViasIngreso());

			// cargar especialidades
			forma.setEspecialidadList(Especialidades.cargarEspecialidadesTipo(
					usuario.getCodigoInstitucionInt(),
					ConstantesIntegridadDominio.acronimoTipoEspecialidadMedica));

			// cargar los convenios
			forma.setConvenioList(pacientesPoliconsultadores
					.obtenerConveniosPorInstitucion(usuario
							.getCodigoInstitucionInt()));
			// cargar los tipos de servicios
			forma.setTipoServicioList(pacientesPoliconsultadores
					.obtenerTiposServicio());
			// cargar las unidades agenda
			forma.setUnidadAgendaList(pacientesPoliconsultadores
					.obtenerUnidadesConsulta());
			// si es cantidad de digitos
			forma.setNumDigCaptNumId(Integer.parseInt(ValoresPorDefecto
					.getNumDigCaptNumIdPac(usuario.getCodigoInstitucionInt())));
			HibernateUtil.endTransaction();
		} catch (Exception e) {
			Log4JManager.error(e);
			HibernateUtil.abortTransaction();
		}

		return mapping.findForward("principal");
	}

	/**
	 * 
	 * M茅todo que se encarga de determinar como se debe validar el campo de
	 * ingreso de n煤mero de identificaci贸n del paciente
	 * 
	 * @param forma
	 * @param request
	 * @param codigoInstitucion
	 */
	private void determinarValidacionIdentificacion(
			PacientesPoliconsultadoresForm forma, HttpServletRequest request,
			int codigoInstitucion) {

		UtilidadTransaccion.getTransaccion().begin();

		ITiposIdentificacionServicio tiposIdentificacionServicio = AdministracionFabricaServicio
				.crearTiposIdentificacionServicio();

		String numDigCaptNumIdPac = ValoresPorDefecto
				.getNumDigCaptNumIdPac(codigoInstitucion);

		if (UtilidadTexto.isNumber(numDigCaptNumIdPac)) {

			forma.setNumDigCaptNumId(Integer.parseInt(numDigCaptNumIdPac));

		} else {

			forma.setNumDigCaptNumId(20);
		}

		if (!"".equals(forma.getTipoIdentificacionPac())) {

			TiposIdentificacion tipoIdentificacion = tiposIdentificacionServicio
					.obtenerTipoIdentificacionPorAcronimo(forma
							.getTipoIdentificacionPac());

			if (tipoIdentificacion != null) {

				if (tipoIdentificacion.getSoloNumeros() != null
						&& tipoIdentificacion.getSoloNumeros().equals(
								ConstantesBD.acronimoSi.charAt(0))) {

					request.setAttribute("validacionCampo", "soloNumero");

				} else {

					request.setAttribute("validacionCampo", "alfanumerico");
				}
			}
		}

		UtilidadTransaccion.getTransaccion().commit();
	}

	@SuppressWarnings("deprecation")
	private ActionForward accionValidarPaciente(
			PacientesPoliconsultadoresForm forma, UsuarioBasico usuario,
			ActionMapping mapping, HttpServletRequest request,
			ActionErrors errores, InstitucionBasica institucionActual,PersonaBasica paciente) {

		int idConvenio = 0;
		int idViaIngreso = 0;
		int idEspecialidad = 0;
		int idUnidadAgenda = 0;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");

		UtilidadTransaccion.getTransaccion().begin();
		try {
			/**
			 * Creaci贸n del reporte PacientesPoliconsultadores
			 */
			DtoPacientesPoliconsultadores dtoFinalReporte = new DtoPacientesPoliconsultadores();

			ArrayList<DtoPacientesPoliconsultadores> lista = new ArrayList<DtoPacientesPoliconsultadores>(
					0);
			ArrayList<DtoPacientesPoliconsultadores> listaConvenio = new ArrayList<DtoPacientesPoliconsultadores>(
					0);			
			GeneradorReportePacientesPoliconsultadores generadorReporte = null;
			JasperPrint reporteOriginal = null;
			int cantidadIngresos = 1;

			
			if (forma.getConvenio().isEmpty()) {
				idConvenio = -1;
			} else {
				idConvenio = Integer.parseInt(forma.getConvenio());
			}

			if (forma.getViaIngreso().isEmpty()) {
				idViaIngreso = -1;
			} else {
				idViaIngreso = Integer.parseInt(forma.getViaIngreso());
			}
			
			if (forma.getEspecialidad().isEmpty()) {
				idEspecialidad = -1;
			} else {
				idEspecialidad = Integer.parseInt(forma.getEspecialidad());
			}
			
			if (forma.getUnidadAgenda().isEmpty()) {
				idUnidadAgenda = -1;
			} else {
				idUnidadAgenda = Integer.parseInt(forma.getUnidadAgenda());
			}

			lista.addAll(pacientesPoliconsultadores
					.obtenerConveniosPoliconsultadores(forma.getFechaInicial(),
							forma.getFechaFinal(), idConvenio,
							forma.getTipoIdentificacionPac(),
							forma.getNumeroIdentificacion(), idViaIngreso, idEspecialidad, idUnidadAgenda, forma.getTipoServicio(),paciente.getCodigoPersona()));
			

			if(lista.size()==0){
				errores.add("", new ActionMessage("errors.notEspecific","No Existe Informaci贸n"));
				saveErrors(request, errores);
				//forma.setEstado(IconstantesEstadosJsp.ESTADO_EMPEZAR);
				forma.setTipoSalida("");
				//MT5717
				forma.setNombreArchivoGenerado("");				
				//FIN
				return mapping.findForward("principal");

			}
			

				dtoFinalReporte.setUbicacionLogo(institucionActual
					.getUbicacionLogo());
			String rutalogo = institucionActual.getLogoJsp();
			// dtoFinalReporte.setRutaLogo("../../../../../../../web/imagenes/logo_clinica_reporte.gif");
			dtoFinalReporte.setRutaLogo(rutalogo);

			dtoFinalReporte.setRazonSocial(institucionActual
					.getRazonSocialInstitucionCompleta());
			dtoFinalReporte.setInstitucion(usuario.getCodigoInstitucionInt());
			dtoFinalReporte.setNit(institucionActual.getNit());
			dtoFinalReporte.setActividadEconomica(institucionActual
					.getActividadEconomica());
			dtoFinalReporte.setCentroAtencion(usuario.getCentroAtencion());
			dtoFinalReporte.setDireccion(institucionActual.getDireccion());
			dtoFinalReporte.setTelefono(institucionActual.getTelefono());
			dtoFinalReporte
					.setTipoReporte("Reporte Identificar Pacientes Policonsultadores");
			dtoFinalReporte.setUsuario(usuario.getNombreUsuario());
			dtoFinalReporte.setFechaInicial(forma.getFechaInicial());
			dtoFinalReporte.setFechaFinal(forma.getFechaFinal());
			dtoFinalReporte.setCantidadServicios(forma.getCantidadServicios());

			// ALMACENO LOS SUBREPORTES
			ArrayList<DtoDetallesPacientesPoliconsultadores> listaEspecialidad = new ArrayList<DtoDetallesPacientesPoliconsultadores>(0);
			for (int i = 0; i < lista.size(); i++) {
				if (i == lista.size() - 1) {
					listaEspecialidad.add(lista.get(i).getDtoDetalles());
					lista.get(i).setListaDetalles(listaEspecialidad);
					lista.get(i).setCantidadIngresos(String.valueOf(cantidadIngresos)); 
					listaConvenio.add(lista.get(i));
					cantidadIngresos=1;
					listaEspecialidad = null;
					listaEspecialidad = new ArrayList<DtoDetallesPacientesPoliconsultadores>();
					break;
				}
				if (lista.get(i).getCodigoConvenio() != lista.get(i + 1)
						.getCodigoConvenio() ) {					
					listaEspecialidad.add(lista.get(i).getDtoDetalles());
					lista.get(i).setListaDetalles(listaEspecialidad);	
					lista.get(i).setCantidadIngresos(String.valueOf(cantidadIngresos)); 
					listaConvenio.add(lista.get(i));
					cantidadIngresos=1;
					listaEspecialidad = null;
					listaEspecialidad = new ArrayList<DtoDetallesPacientesPoliconsultadores>();
				} else if( lista.get(i).getCodigoPaciente() != lista.get(i+1).getCodigoPaciente() ){
					listaEspecialidad.add(lista.get(i).getDtoDetalles());
					lista.get(i).setListaDetalles(listaEspecialidad);	
					lista.get(i).setCantidadIngresos(String.valueOf(cantidadIngresos)); 
					listaConvenio.add(lista.get(i));
					cantidadIngresos=1;
					listaEspecialidad = null;
					listaEspecialidad = new ArrayList<DtoDetallesPacientesPoliconsultadores>();
				} else {
					cantidadIngresos++;
					listaEspecialidad.add(lista.get(i).getDtoDetalles());
				}
			}			
			ArrayList<DtoDetallesPacientesPoliconsultadores> listaUnidades;
			DtoDetallesPacientesPoliconsultadores unidad;		
			boolean estado, ultimo;
			for( int k = 0; k < listaConvenio.size(); k++ ){
				listaUnidades = new ArrayList<DtoDetallesPacientesPoliconsultadores>(0);
				estado = true;
				ultimo = false;
				for( int w = 0; w < listaConvenio.get(k).getListaDetalles().size();){
					if (w == listaConvenio.get(k).getListaDetalles().size() - 1) {												
						unidad = new DtoDetallesPacientesPoliconsultadores();			
						unidad.setCantidadIngresos(listaConvenio.get(k).getListaDetalles().get( w ).getCantidadIngresos());
						unidad.setNombreEspecialidad(listaConvenio.get(k).getListaDetalles().get( w ).getNombreEspecialidad());
						unidad.setNombreUnidadAgenda(listaConvenio.get(k).getListaDetalles().get( w ).getNombreUnidadAgenda());
						if( !ultimo ){
							listaUnidades.add( unidad );
							unidad = null;
						}	
						listaConvenio.get(k).getListaDetalles().get(w).setListaUnidades(listaUnidades);
						listaConvenio.get(k).setCantidadIngresos(String.valueOf(listaConvenio.get(k).getListaDetalles().size()));
						listaUnidades = null;
						break;
					}
					if( listaConvenio.get(k).getListaDetalles().get(w).getCodigoEspecialidad() == 
						listaConvenio.get(k).getListaDetalles().get(w + 1).getCodigoEspecialidad() ) {
						
						if( estado ){
							unidad = new DtoDetallesPacientesPoliconsultadores();			
							unidad.setCantidadIngresos(listaConvenio.get(k).getListaDetalles().get( w ).getCantidadIngresos());
							unidad.setNombreEspecialidad(listaConvenio.get(k).getListaDetalles().get( w ).getNombreEspecialidad());
							unidad.setNombreUnidadAgenda(listaConvenio.get(k).getListaDetalles().get( w ).getNombreUnidadAgenda());
							listaUnidades.add( unidad );
							unidad = null;
							estado = false;
						}
						listaConvenio.get(k).getListaDetalles().get( w ).setCantidadIngresos( listaConvenio.get(k).getListaDetalles().get( w ).getCantidadIngresos() + listaConvenio.get(k).getListaDetalles().get( w + 1 ).getCantidadIngresos() );
												
						unidad = new DtoDetallesPacientesPoliconsultadores();			
						unidad.setCantidadIngresos(listaConvenio.get(k).getListaDetalles().get( w + 1 ).getCantidadIngresos());
						unidad.setNombreEspecialidad(listaConvenio.get(k).getListaDetalles().get( w + 1 ).getNombreEspecialidad());
						unidad.setNombreUnidadAgenda(listaConvenio.get(k).getListaDetalles().get( w + 1 ).getNombreUnidadAgenda());
						
						listaUnidades.add( unidad );
						unidad = null;
						listaConvenio.get(k).getListaDetalles().remove( w + 1 );
					}else {
						
						if( estado ){
							unidad = new DtoDetallesPacientesPoliconsultadores();			
							unidad.setCantidadIngresos(listaConvenio.get(k).getListaDetalles().get( w ).getCantidadIngresos());
							unidad.setNombreEspecialidad(listaConvenio.get(k).getListaDetalles().get( w ).getNombreEspecialidad());
							unidad.setNombreUnidadAgenda(listaConvenio.get(k).getListaDetalles().get( w ).getNombreUnidadAgenda());
							listaUnidades.add( unidad );
							unidad = null;
							estado = false;
						}
						
						listaConvenio.get(k).getListaDetalles().get(w).setListaUnidades(listaUnidades);						
						listaUnidades = null;						
						
						unidad = new DtoDetallesPacientesPoliconsultadores();			
						unidad.setCantidadIngresos(listaConvenio.get(k).getListaDetalles().get( w + 1 ).getCantidadIngresos());
						unidad.setNombreEspecialidad(listaConvenio.get(k).getListaDetalles().get( w + 1 ).getNombreEspecialidad());
						unidad.setNombreUnidadAgenda(listaConvenio.get(k).getListaDetalles().get( w + 1 ).getNombreUnidadAgenda());
						
						listaUnidades = new ArrayList<DtoDetallesPacientesPoliconsultadores>(0);
						listaUnidades.add( unidad );						
						unidad = null;
											
						w++;
						ultimo = true;
					}					
				}
			}
			
			dtoFinalReporte.setListaConvenios(listaConvenio);
			String tipoSalida=forma.getTipoSalida();
						
			generadorReporte = new GeneradorReportePacientesPoliconsultadores(dtoFinalReporte, tipoSalida);

			reporteOriginal = generadorReporte.generarReporte();

			//pdf 1, plano 2 , excel 3
			
			if(forma.getTipoSalida().equals(String.valueOf(EnumTiposSalida.PDF.getCodigo()))){
				forma.setNombreArchivoGenerado(generadorReporte.exportarReportePDF(
						reporteOriginal,
				"FormatoReporteIdentificarPacientesPoliconsultadores"));
			/*}else if(forma.getTipoSalida().equals(String.valueOf(EnumTiposSalida.PLANO.getCodigo()))){
				forma.setNombreArchivoGenerado(generadorReporte.exportarReporteTextoPlano(
						reporteOriginal,
				"FormatoReporteIdentificarPacientesPoliconsultadoresPlano"));*/

			}else if(forma.getTipoSalida().equals(String.valueOf(EnumTiposSalida.HOJA_CALCULO.getCodigo()))){
				forma.setNombreArchivoGenerado(generadorReporte.exportarReporteExcel(
						reporteOriginal,
				"FormatoReporteIdentificarPacientesPoliconsultadores"));

			}
			// forma.setNombreArchivoCopia(generadorReporte.exportarReportePDF(reporteOriginal,
			// "FormatoReporteIdentificarPacientesPoliconsultadores"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		forma.setTipoSalida("");
		UtilidadTransaccion.getTransaccion().commit();
		return mapping.findForward("principal");
	}
	
	private ActionForward accionReportePlano(
			PacientesPoliconsultadoresForm forma, UsuarioBasico usuario,
			ActionMapping mapping, HttpServletRequest request,
			ActionErrors errores, InstitucionBasica institucionActual,PersonaBasica paciente) {
			
			int idConvenio = 0;
			int idViaIngreso = 0;
			int idEspecialidad = 0;
			int idUnidadAgenda = 0;
	
			UtilidadTransaccion.getTransaccion().begin();
		
		
		try {
			
			GeneradorReportePacientesPoliconsultadores generadorReporte = null;
			JasperPrint reporteOriginal = null;
			String tipoSalida=forma.getTipoSalida();
			
			List<DtoPacientesPoliconsultadores>lista=new ArrayList<DtoPacientesPoliconsultadores>(0);
			
			if (forma.getConvenio().isEmpty()) {
				idConvenio = -1;
			} else {
				idConvenio = Integer.parseInt(forma.getConvenio());
			}

			if (forma.getViaIngreso().isEmpty()) {
				idViaIngreso = -1;
			} else {
				idViaIngreso = Integer.parseInt(forma.getViaIngreso());
			}
			
			if (forma.getEspecialidad().isEmpty()) {
				idEspecialidad = -1;
			} else {
				idEspecialidad = Integer.parseInt(forma.getEspecialidad());
			}
			
			if (forma.getUnidadAgenda().isEmpty()) {
				idUnidadAgenda = -1;
			} else {
				idUnidadAgenda = Integer.parseInt(forma.getUnidadAgenda());
			}
			lista.addAll(pacientesPoliconsultadores
					.obtenerConveniosPoliconsultadores(forma.getFechaInicial(),
							forma.getFechaFinal(), idConvenio,
							forma.getTipoIdentificacionPac(),
							forma.getNumeroIdentificacion(), idViaIngreso, idEspecialidad, idUnidadAgenda, forma.getTipoServicio(),paciente.getCodigoPersona()));
			
			List<PacientePoliconsultadoresPlanoDto> listaPlano=new ArrayList<PacientePoliconsultadoresPlanoDto>(0);

			
			
			HashMap<Integer, Integer>cantidadIngresosXConvenio=new HashMap<Integer, Integer>(0);
			//Suma ingresos por convenio
			for(DtoPacientesPoliconsultadores dtoPacientesPoliconsultadores:lista){
				Integer count=cantidadIngresosXConvenio.get(dtoPacientesPoliconsultadores.getCodigoConvenio());
				if(count==null){
					count=0;
				}
				count++;
				cantidadIngresosXConvenio.put(dtoPacientesPoliconsultadores.getCodigoConvenio(),count);
				
				PacientePoliconsultadoresPlanoDto planoDto=new PacientePoliconsultadoresPlanoDto();
				
				planoDto.setFechaInicial(dtoPacientesPoliconsultadores.getFechaInicial());
				planoDto.setFechaFinal(dtoPacientesPoliconsultadores.getFechaFinal());
				planoDto.setCodigoConvenio(dtoPacientesPoliconsultadores.getCodigoConvenio());
				planoDto.setConvenio(dtoPacientesPoliconsultadores.getConvenio());
				planoDto.setPaciente(dtoPacientesPoliconsultadores.getPaciente());
				planoDto.setDocumento(dtoPacientesPoliconsultadores.getDocumento());
				planoDto.setNombreViasIngreso(dtoPacientesPoliconsultadores.getDtoDetalles().getNombreViasIngreso());
				planoDto.setTipoServicio(dtoPacientesPoliconsultadores.getDtoDetalles().getTipoServicio());
				planoDto.setNombreEspecialidad(dtoPacientesPoliconsultadores.getDtoDetalles().getNombreEspecialidad());
				planoDto.setNombreUnidadAgenda(dtoPacientesPoliconsultadores.getDtoDetalles().getNombreUnidadAgenda());
				planoDto.setCantidadServicios(dtoPacientesPoliconsultadores.getDtoDetalles().getCantidadIngresos()+"");
				
				listaPlano.add(planoDto);
				
			}
			//Total de ingresos por convenio
			for(PacientePoliconsultadoresPlanoDto planoDto:listaPlano){
				planoDto.setCantidadIngresos(cantidadIngresosXConvenio.get(planoDto.getCodigoConvenio()).toString());
			}
			
			PacientePoliconsultadoresPlanoDto planoDto=new PacientePoliconsultadoresPlanoDto();
          //MT 5627		se agraga al reporte los filtos de busqueda seg鷑 el DCU 1037 
						
			planoDto.setPaciente(forma.getNumeroIdentificacion());
			planoDto.setCantidadServicios(forma.getCantidadServicios());
			
			if(!forma.getConvenio().isEmpty()&&!forma.getConvenio().equals(""))	{
				for(DtoConvenio listaconvenio:(forma.getConvenioList())){				
					if(listaconvenio.getCodigo()==Integer.parseInt(forma.getConvenio())){
						planoDto.setConvenio(listaconvenio.getDescripcion());
						break;
					}
				}
			}
			
			if(!forma.getViaIngreso().isEmpty()&&!forma.getViaIngreso().equals(""))	{
				for(ViasIngreso listaViaIngreso:forma.getViaIngresoList()){
					
					if(listaViaIngreso.getCodigo()==Integer.parseInt(forma.getViaIngreso())){
						planoDto.setNombreViasIngreso(listaViaIngreso.getNombre());
						break;
					}				
				}	
			}	
			if(!forma.getTipoServicio().isEmpty()&&!forma.getTipoServicio().equals(""))	{
				for(TiposServicio listaTipoServicio:forma.getTipoServicioList()){					
					if(listaTipoServicio.getAcronimo().equals(forma.getTipoServicio())){
						planoDto.setTipoServicio(listaTipoServicio.getNombre());
						break;
					}				
				}
			}
			if(!forma.getEspecialidad().isEmpty()&&!forma.getEspecialidad().equals(""))	{
				for(DtoEspecialidades listaEspecialidades:forma.getEspecialidadList()){
					
					if( listaEspecialidades.getCodigo()==Integer.parseInt(forma.getEspecialidad())){
						planoDto.setNombreEspecialidad( listaEspecialidades.getDescripcion());
						break;
					}				
				}
		     }
			
			planoDto.setFechaInicial(forma.getFechaInicial());
			planoDto.setFechaFinal(forma.getFechaFinal());
			//Fin MT
			
			planoDto.setJRDPacientes(new JRBeanCollectionDataSource( listaPlano) );
			generadorReporte = new GeneradorReportePacientesPoliconsultadores(planoDto, tipoSalida);
			reporteOriginal = generadorReporte.generarReporte();
			
			forma.setNombreArchivoGenerado(generadorReporte.exportarReporteTextoPlano(
					reporteOriginal,"FormatoReporteIdentificarPacientesPoliconsultadoresPlano"));
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		forma.setTipoSalida("");
		UtilidadTransaccion.getTransaccion().commit();
		return mapping.findForward("principal");
	}
}