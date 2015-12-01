package com.servinte.axioma.action.historiaClinica;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.axioma.util.log.Log4JManager;

import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.actionForm.historiaClinica.CurvasCrecimientoForm;
import com.servinte.axioma.bl.historiaClinica.facade.HistoriaClinicaFacade;
import com.servinte.axioma.dto.historiaClinica.CoordenadasCurvaCrecimientoDto;
import com.servinte.axioma.dto.historiaClinica.CurvaCrecimientoPacienteDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.generadorReporte.historiaClinica.curvasCrecimiento.CurvaCrecimientoDesarrolloDto;
import com.servinte.axioma.generadorReporte.historiaClinica.curvasCrecimiento.GeneradorReporteCurvasCrecimiento;
import com.servinte.axioma.generadorReporte.historiaClinica.curvasCrecimiento.SignosVitalesDto;

/**
 * @author hermorhu
 * @created 09-Oct-2012 
 */
public class CurvasCrecimientoAction extends DispatchAction{

	private static final String FORWARD_EMPEZAR = "empezar";
	
	private static final String KEY_SESSION_PACIENTE = "pacienteActivo";
	
	private static final String KEY_SESSION_INSTITUCION = "institucionBasica";
	
	private static final String DIRECTORIO_IMAGENES = System.getProperty("directorioImagenes");
	
	private static final String DIRECTORIO_SUPERIOR = "../";
	
	private static final String TITULO_REPORTE = "CURVAS DE CRECIMIENTO Y DESARROLLO";
	
	private static final int NUMERO_MESES = 12;
	
	private static final int NUMERO_DIAS_MES = 30;
	
	private static final String KEY_ERROR_CAST="errors.castForm";
	
	private static final String KEY_ERROR_NO_ESPECIFIC="errors.notEspecific";
	
	/**
	 * Metodo encargado de cargar los datos de las curvas de crecimiento al ingresar al componente
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @author hermorhu
	 * @created 09-Oct-2012 
	 */
	public ActionForward actionEmpezarCurvasCrecimiento(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores = new ActionMessages();
		try{
			CurvasCrecimientoForm forma = null;
			if(form instanceof CurvasCrecimientoForm) {
				forma = (CurvasCrecimientoForm) form;
			}
			else {
				throw new ClassCastException();
			}
			HistoriaClinicaFacade historiaClinicaFacade = new HistoriaClinicaFacade();
			PersonaBasica paciente 	= (PersonaBasica)request.getSession().getAttribute(KEY_SESSION_PACIENTE);
			InstitucionBasica institucion = (InstitucionBasica) request.getSession().getAttribute(KEY_SESSION_INSTITUCION);
			forma.reset();
		
			forma.setMostrarRutaJsp(ValoresPorDefecto.getMostrarNombreJSP());
		
			SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
			Date fechaCorte = null;
			fechaCorte = formatoDelTexto.parse(System.getProperty("FECHACORTECURVASCRECIMIENTO"));

			forma.setMostrarCurvasAnteriores(historiaClinicaFacade.existeCurvasAnteriores(paciente.getCodigoPersona(), institucion.getCodigoInstitucionBasica(), fechaCorte));
		
			//Fecha Atencion del paciente
			forma.setFechaAtencion(UtilidadFecha.getFechaActual());
		
			//Edad del Paciente
			String[] fechaNacimientoTemp = UtilidadFecha.conversionFormatoFechaABD(paciente.getFechaNacimiento()).split("-");
			String[] fechaActualTemp = UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()).split("-"); 
			String edadPaciente = UtilidadFecha.calcularEdadDetalladaCompleta(Integer.parseInt(fechaNacimientoTemp[0]),Integer.parseInt(fechaNacimientoTemp[1]),Integer.parseInt(fechaNacimientoTemp[2]),
					Integer.parseInt(fechaActualTemp[2]),Integer.parseInt(fechaActualTemp[1]),Integer.parseInt(fechaActualTemp[0]));
		
			forma.setEdadPaciente(edadPaciente);
		
			//Peso del paciente
			forma.setPeso(!forma.getPeso().isEmpty() ? forma.getPeso()+" Kg" : "");
		
			//Talla del paciente
			forma.setTalla(!forma.getTalla().isEmpty() ? forma.getTalla()+" cm" : "");
		
			//Indice de Masa Corporal del paciente
			forma.setImc(!forma.getImc().isEmpty() ? forma.getImc()+" Kg/m2" : "");
		
			//Perimetro Cefalico del paciente
			forma.setPerimetroCefalico(!forma.getPerimetroCefalico().isEmpty() ? forma.getPerimetroCefalico()+" cm" : "");
		
			//Graficas del Paciente
			int[] vectorEdad = UtilidadFecha.calcularVectorEdad(Integer.parseInt(fechaNacimientoTemp[0]),Integer.parseInt(fechaNacimientoTemp[1]),Integer.parseInt(fechaNacimientoTemp[2]),
					Integer.parseInt(fechaActualTemp[2]),Integer.parseInt(fechaActualTemp[1]),Integer.parseInt(fechaActualTemp[0])); 
		
			float edadMesesTemp = (vectorEdad[0]*NUMERO_MESES)+vectorEdad[1]+((float)vectorEdad[2]/NUMERO_DIAS_MES);
			int edadMeses = (int) Math.floor(edadMesesTemp);
		
			HttpSession session=request.getSession();
		
			@SuppressWarnings("unchecked")
			List<CurvaCrecimientoPacienteDto> curvasCrecimiento = (List<CurvaCrecimientoPacienteDto>)session.getAttribute("curvasCrecimientoPaciente");

			if(curvasCrecimiento != null){
				forma.setListaCurvasCrecimientoPaciente(curvasCrecimiento);
			}else{
				forma.setListaCurvasCrecimientoPaciente(historiaClinicaFacade.consultarCurvasCrecimientoPaciente(paciente.getCodigoPersona(), paciente.getCodigoSexo(), edadMeses));
			}
		
		}catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}catch(IPSException ipse){
			Log4JManager.error(ipse);
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
		}catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		
		return mapping.findForward(FORWARD_EMPEZAR);
	}
	
	/**
	 * Metodo encargado de obtener los datos para pintar la grafica de curva de crecimiento seleccionada
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @author hermorhu
	 * @created 09-Oct-2012 
	 */
	public ActionForward actionMostrarGrafica(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores = new ActionMessages();
		try{
			CurvasCrecimientoForm forma = null;
			if(form instanceof CurvasCrecimientoForm) {
				forma = (CurvasCrecimientoForm) form;
			}
			else {
				throw new ClassCastException();
			}
		
			forma.resetCurvaSeleccionada();
			forma.resetMensajes();
			
			//Curva de Crecimiento seleccionada
			forma.setCurvaCrecimientoSeleccionada(forma.getListaCurvasCrecimientoPaciente().get(Integer.parseInt(forma.getIndexCurvaSeleccionada())).getDtoCurvaCrecimientoParametrizab());

			//Si la curva de crecimiento seleccionada para el paciente no existe en el historico
			if(forma.getListaCurvasCrecimientoPaciente().get(Integer.parseInt(forma.getIndexCurvaSeleccionada())).getIdImagenParametrizada() == null){
				forma.setImagenCurvaCrecimientoSeleccionada(DIRECTORIO_IMAGENES + forma.getListaCurvasCrecimientoPaciente().get(Integer.parseInt(forma.getIndexCurvaSeleccionada())).getDtoCurvaCrecimientoParametrizab().getDtoImagenesParametrizadas().getImagenCurva());
			}
			//Si la curva de crecimiento seleccionada para el paciente ya existe al menos un registro en el historico
			else if(forma.getListaCurvasCrecimientoPaciente().get(Integer.parseInt(forma.getIndexCurvaSeleccionada())).getIdImagenParametrizada() != null){
			
				//Si la imagen almacenada en el historico es distinta a la almacenada en la parametrizacion
				if(!forma.getListaCurvasCrecimientoPaciente().get(Integer.parseInt(forma.getIndexCurvaSeleccionada())).getIdImagenParametrizada().equals( 
						forma.getListaCurvasCrecimientoPaciente().get(Integer.parseInt(forma.getIndexCurvaSeleccionada())).getDtoCurvaCrecimientoParametrizab().getDtoImagenesParametrizadas().getId())){
					forma.setImagenCurvaCrecimientoSeleccionada(DIRECTORIO_IMAGENES + forma.getListaCurvasCrecimientoPaciente().get(Integer.parseInt(forma.getIndexCurvaSeleccionada())).getDtoCurvaCrecimientoParametrizab().getDtoImagenesParametrizadas().getImagenCurva());
					forma.setImagenCurvaCrecimientoBloqueada(DIRECTORIO_IMAGENES + forma.getListaCurvasCrecimientoPaciente().get(Integer.parseInt(forma.getIndexCurvaSeleccionada())).getUrlUltimaCurvaModificada());
				}
				//Si la imagen almacenada en el historico es igual a la almacenada en la parametrizacion
				else{
					forma.setImagenCurvaCrecimientoSeleccionada(DIRECTORIO_IMAGENES + forma.getListaCurvasCrecimientoPaciente().get(Integer.parseInt(forma.getIndexCurvaSeleccionada())).getUrlUltimaCurvaModificada());
				}
			}
		
			//Agregan las coordenadas de puntos y de errores de la grafica seleccionada
			forma.setCoordenadasPuntos(forma.getListaCurvasCrecimientoPaciente().get(Integer.parseInt(forma.getIndexCurvaSeleccionada())).getCoordenadasCurvaCrecimiento());
			forma.setCoordenadasErrores(forma.getListaCurvasCrecimientoPaciente().get(Integer.parseInt(forma.getIndexCurvaSeleccionada())).getCoordenadasErroresPuntos());
		
			forma.setMostrarGrafica(true);
		
		}catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		
		return mapping.findForward(FORWARD_EMPEZAR);
	}
	
	/**
	 * Metodo encargado de guardar en session la informacion de las curvas necesaria para ser almacenada
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @author hermorhu
	 * @created 09-Oct-2012 
	 */
	public ActionForward actionGuardarCurva(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores = new ActionMessages();
		try{
			CurvasCrecimientoForm forma = null;
			if(form instanceof CurvasCrecimientoForm) {
				forma = (CurvasCrecimientoForm) form;
			}
			else {
				throw new ClassCastException();
			}
		
			forma.resetMensajes();
		
			//coordenadas puntos
			String[] coordenadasX = forma.getCoorX().split(","); 
			String[] coordenadasY = forma.getCoorY().split(",");
		 
			CoordenadasCurvaCrecimientoDto coordenadas = null;
		 
			//Se dejan en la lista solo las coordenadas que provienen de una atencion anterior
			ArrayList<CoordenadasCurvaCrecimientoDto> coordenadasCurvaSeleccionada = (ArrayList<CoordenadasCurvaCrecimientoDto>)forma.getListaCurvasCrecimientoPaciente().get(Integer.parseInt(forma.getIndexCurvaSeleccionada())).getCoordenadasCurvaCrecimiento(); 

			@SuppressWarnings("unchecked")
			ArrayList<CoordenadasCurvaCrecimientoDto> coordenadasCurvaSeleccionadaClone = (ArrayList<CoordenadasCurvaCrecimientoDto>) coordenadasCurvaSeleccionada.clone();

			for(CoordenadasCurvaCrecimientoDto coordenadasPuntos : coordenadasCurvaSeleccionadaClone){
				if(coordenadasPuntos.getId() == null){
					forma.getListaCurvasCrecimientoPaciente().get(Integer.parseInt(forma.getIndexCurvaSeleccionada())).getCoordenadasCurvaCrecimiento().remove(coordenadasPuntos);
				}
			}
		 
			for (int i=0; i < coordenadasX.length; i++) { 
			 
				if(!coordenadasX[i].isEmpty() && !coordenadasY[i].isEmpty()){
					coordenadas = new CoordenadasCurvaCrecimientoDto();
					coordenadas.setCoordenadaX(Integer.parseInt(coordenadasX[i]));
					coordenadas.setCoordenadaY(Integer.parseInt(coordenadasY[i]));
	         
					forma.getListaCurvasCrecimientoPaciente().get(Integer.parseInt(forma.getIndexCurvaSeleccionada())).getCoordenadasCurvaCrecimiento().add(coordenadas);           
				}
			} 
		 
			//coordenadas errores
			String[] coordenadasErroresX = forma.getCoorErrorX().split(","); 
			String[] coordenadasErroresY = forma.getCoorErrorY().split(",");
		 
			CoordenadasCurvaCrecimientoDto coordenadasError = null;
		 
			forma.getListaCurvasCrecimientoPaciente().get(Integer.parseInt(forma.getIndexCurvaSeleccionada())).getCoordenadasErroresPuntos().clear();
		 
			for (int i=0; i < coordenadasErroresX.length; i++) { 
			 
				if(!coordenadasErroresX[i].isEmpty() && !coordenadasErroresY[i].isEmpty()){
					coordenadasError = new CoordenadasCurvaCrecimientoDto();
					coordenadasError.setCoordenadaX(Integer.parseInt(coordenadasErroresX[i]));
					coordenadasError.setCoordenadaY(Integer.parseInt(coordenadasErroresY[i]));
	         
					forma.getListaCurvasCrecimientoPaciente().get(Integer.parseInt(forma.getIndexCurvaSeleccionada())).getCoordenadasErroresPuntos().add(coordenadasError);           
				}
			} 
		 
			forma.getListaCurvasCrecimientoPaciente().get(Integer.parseInt(forma.getIndexCurvaSeleccionada())).setImagenBase64(forma.getImagenBase64());
			forma.getListaCurvasCrecimientoPaciente().get(Integer.parseInt(forma.getIndexCurvaSeleccionada())).setGraficaDiligenciada(true);

			List<CurvaCrecimientoPacienteDto> curvasCrecimientoPaciente = forma.getListaCurvasCrecimientoPaciente();
		
			//Almacena en session la lista con las curva de crecimiento del paciente para ser almacenadas en el momento de Guardar la Valoracion o Evolucion
			HttpSession session=request.getSession();
			session.removeAttribute("curvasCrecimientoPaciente");
			session.setAttribute("curvasCrecimientoPaciente", curvasCrecimientoPaciente);
		
			forma.setMensajeCurvaGuardada(true);
			forma.resetGraficaCurvaCrecimiento();
		
		}catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
			
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}	
		
		return mapping.findForward(FORWARD_EMPEZAR);
	}
	
	/**
	 * Metodo encargado de imprimir la curva de crecimiento que esta seleccionada
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @author hermorhu
	 * @created 22-Oct-2012 
	 */
	public ActionForward actionImprimirCurvaCrecimiento(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ActionMessages errores = new ActionMessages();
		try{	
			CurvasCrecimientoForm forma = null;
			if(form instanceof CurvasCrecimientoForm) {
				forma = (CurvasCrecimientoForm) form;
			}
			else {
				throw new ClassCastException();
			}
		
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
			InstitucionBasica institucion = (InstitucionBasica) request.getSession().getAttribute(KEY_SESSION_INSTITUCION);
			PersonaBasica paciente 	= (PersonaBasica)request.getSession().getAttribute(KEY_SESSION_PACIENTE);
		
			HistoriaClinicaFacade historiaClinicaFacade = new HistoriaClinicaFacade();
		
			GeneradorReporteCurvasCrecimiento generadorReporteCurvasCrecimiento = null;
			JasperPrint reporteOriginal = null;

			//dto para la impresion de la curva selecionada
			CurvaCrecimientoDesarrolloDto dtoCurvaCrecimientoDesarrollo = new CurvaCrecimientoDesarrolloDto();
		
			dtoCurvaCrecimientoDesarrollo.setLogo(DIRECTORIO_SUPERIOR + institucion.getLogoJsp());
		
			dtoCurvaCrecimientoDesarrollo.setRazonSocial(institucion.getRazonSocialInstitucionCompleta());
			dtoCurvaCrecimientoDesarrollo.setNit(institucion.getNit());
			dtoCurvaCrecimientoDesarrollo.setActividadEconomica(institucion.getActividadEconomica());
			dtoCurvaCrecimientoDesarrollo.setDireccion(institucion.getDireccion());
			dtoCurvaCrecimientoDesarrollo.setTelefono(institucion.getTelefono());
			dtoCurvaCrecimientoDesarrollo.setCentroAtencion(usuario.getCentroAtencion());
			dtoCurvaCrecimientoDesarrollo.setTipoReporte(TITULO_REPORTE);
		
			dtoCurvaCrecimientoDesarrollo.setPaciente(paciente.getNombrePersona());
			dtoCurvaCrecimientoDesarrollo.setIdentificacion(paciente.getCodigoTipoIdentificacionPersona() +". No. "+ paciente.getNumeroIdentificacionPersona() +" de "+ paciente.getNombreCiudadExpedicion());
			dtoCurvaCrecimientoDesarrollo.setFechaNacimiento(paciente.getFechaNacimiento());
			dtoCurvaCrecimientoDesarrollo.setEdad(paciente.getEdadDetallada());
			dtoCurvaCrecimientoDesarrollo.setSexo(paciente.getSexo());
			dtoCurvaCrecimientoDesarrollo.setTipoAfiliacion(paciente.getTipoAfiliado());
			dtoCurvaCrecimientoDesarrollo.setViaIngreso(paciente.getUltimaViaIngreso());
			dtoCurvaCrecimientoDesarrollo.setFechaIngreso(paciente.getFechaIngreso() +" "+ paciente.getHoraIngreso());
			dtoCurvaCrecimientoDesarrollo.setCama(paciente.getCama());
			dtoCurvaCrecimientoDesarrollo.setResponsable(paciente.getConvenioPersonaResponsable());

			dtoCurvaCrecimientoDesarrollo = historiaClinicaFacade.consultarDatosPacienteFormatoImpresion(paciente.getCodigoIngreso(), dtoCurvaCrecimientoDesarrollo);

			List<SignosVitalesDto> listaSignosVitales = new ArrayList<SignosVitalesDto>();
		
			SignosVitalesDto dtoSingosVitales = new SignosVitalesDto();
			dtoSingosVitales.setFecha(forma.getFechaAtencion());
			dtoSingosVitales.setEdad(forma.getEdadPaciente());
			dtoSingosVitales.setPeso(forma.getPeso());
			dtoSingosVitales.setTalla(forma.getTalla());
			dtoSingosVitales.setImc(forma.getImc());
			dtoSingosVitales.setPerimetroCefalico(forma.getPerimetroCefalico());
		
			listaSignosVitales.add(dtoSingosVitales);
			dtoCurvaCrecimientoDesarrollo.setListaSignosVitales(listaSignosVitales);
		
			dtoCurvaCrecimientoDesarrollo.setTituloGrafica(forma.getListaCurvasCrecimientoPaciente().get(Integer.parseInt(forma.getIndexCurvaSeleccionada())).getDtoCurvaCrecimientoParametrizab().getTituloGrafica());
			dtoCurvaCrecimientoDesarrollo.setDescripcionGrafica(forma.getListaCurvasCrecimientoPaciente().get(Integer.parseInt(forma.getIndexCurvaSeleccionada())).getDtoCurvaCrecimientoParametrizab().getDescripcion());
			dtoCurvaCrecimientoDesarrollo.setImagenIzquierda(DIRECTORIO_SUPERIOR + DIRECTORIO_IMAGENES + forma.getListaCurvasCrecimientoPaciente().get(Integer.parseInt(forma.getIndexCurvaSeleccionada())).getDtoCurvaCrecimientoParametrizab().getDtoImagenesParametrizadas().getImagenIzquierda());
			dtoCurvaCrecimientoDesarrollo.setImagenDerecha(DIRECTORIO_SUPERIOR + DIRECTORIO_IMAGENES + forma.getListaCurvasCrecimientoPaciente().get(Integer.parseInt(forma.getIndexCurvaSeleccionada())).getDtoCurvaCrecimientoParametrizab().getDtoImagenesParametrizadas().getImagenDerecha());

			dtoCurvaCrecimientoDesarrollo.setFirmaElectronica(DIRECTORIO_SUPERIOR + System.getProperty("ADJUNTOS") + System.getProperty("FIRMADIGITAL")+"/"+usuario.getFirmaDigital());
			
			dtoCurvaCrecimientoDesarrollo.setImagenCurva(forma.getImagenBase64());

			//Se genera el reporte de la curva de crecimiento seleccionada
			generadorReporteCurvasCrecimiento = new GeneradorReporteCurvasCrecimiento(dtoCurvaCrecimientoDesarrollo);
			reporteOriginal = generadorReporteCurvasCrecimiento.generarReporte();
		
			//Se envia a la jsp el nombre del archivo generado para que lo muestre 
			forma.setNombreArchivoGenerado(generadorReporteCurvasCrecimiento.exportarReportePDF(reporteOriginal,"FormatoReporteCurvasCrecimiento"));
		
		}catch(ClassCastException cce){
			errores.add("", new ActionMessage(KEY_ERROR_CAST));
		}catch(IPSException ipse){
			Log4JManager.error(ipse);
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
		}catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
			
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}	
		return mapping.findForward(FORWARD_EMPEZAR);
	}
	
	
}
