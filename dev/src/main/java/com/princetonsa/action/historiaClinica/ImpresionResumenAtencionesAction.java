package com.princetonsa.action.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.InfoDatos;
import util.InfoDatosBD;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.integracion.dtoIntegracion.ComandoPeticion;
import util.integracion.dtoIntegracion.HttpContextComando;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.reportes.dinamico.GeneradorReporteDinamico;

import com.princetonsa.action.cartera.ConceptosCarteraAction;
import com.princetonsa.actionform.AntecedentesGinecoObstetricosForm;
import com.princetonsa.actionform.historiaClinica.ImpresionResumenAtencionesForm;
import com.princetonsa.dao.sqlbase.SqlBaseImpresionResumenAtencionesDao;
import com.princetonsa.dao.sqlbase.SqlBaseResumenAtencionesDao;
import com.princetonsa.dto.historiaClinica.DtoIngresoHistoriaClinica;
import com.princetonsa.mundo.AntecedentesVacunas;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.antecedentes.AntecedentePediatrico;
import com.princetonsa.mundo.antecedentes.AntecedentesGinecoObstetricos;
import com.princetonsa.mundo.antecedentes.Embarazo;
import com.princetonsa.mundo.antecedentes.HijoBasico;
import com.princetonsa.mundo.antecedentes.InfoMadre;
import com.princetonsa.mundo.antecedentes.InfoPadre;
import com.princetonsa.mundo.enfermeria.RegistroEnfermeria;
import com.princetonsa.mundo.historiaClinica.ImpresionResumenAtenciones;
import com.princetonsa.mundo.historiaClinica.RegistroEventosAdversos;
import com.princetonsa.mundo.historiaClinica.RegistroResumenParcialHistoriaClinica;
import com.princetonsa.mundo.resumenAtenciones.ResumenAtenciones;
import com.princetonsa.mundo.salasCirugia.HojaAnestesia;
import com.servinte.axioma.bl.manejoPaciente.facade.ManejoPacienteFacade;
import com.servinte.axioma.bl.salasCirugia.facade.SalasCirugiaFacade;
import com.servinte.axioma.dto.historiaClinica.InfoIngresoDto;
import com.servinte.axioma.dto.manejoPaciente.DtoNotaAclaratoria;
import com.servinte.axioma.dto.salascirugia.EspecialidadDto;
import com.servinte.axioma.dto.salascirugia.InformacionActoQxDto;
import com.servinte.axioma.dto.salascirugia.InformeQxDto;
import com.servinte.axioma.dto.salascirugia.IngresoSalidaPacienteDto;
import com.servinte.axioma.dto.salascirugia.NotaAclaratoriaDto;
import com.servinte.axioma.dto.salascirugia.NotaEnfermeriaDto;
import com.servinte.axioma.dto.salascirugia.NotaRecuperacionDto;
import com.servinte.axioma.dto.salascirugia.PeticionQxDto;
import com.servinte.axioma.dto.salascirugia.SolicitudCirugiaDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.generadorReporte.historiaClinica.ConstantesImpresionHistoriaClinica;
import com.servinte.axioma.generadorReporte.historiaClinica.DtoFiltroImpresionHistoriaClinica;
import com.servinte.axioma.generadorReporte.historiaClinica.DtoHojaQuirurgicaAnestesia;
import com.servinte.axioma.generadorReporte.historiaClinica.DtoImpresionHistoriaClinica;
import com.servinte.axioma.generadorReporte.historiaClinica.DtoResultadoImpresionHistoriaClinica;
import com.servinte.axioma.generadorReporte.historiaClinica.DtoSignosVitalesHC;
import com.servinte.axioma.generadorReporte.historiaClinica.GeneradorDisenioReporteHistoriaClinica;
import com.servinte.axioma.generadorReporte.historiaClinica.GeneradorReporteHistoriaClinica;
import com.servinte.axioma.generadorReporteHistoriaClinica.comun.IConstantesReporteHistoriaClinica;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.fabrica.historiaClinica.HistoriaClinicaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.historiaClinica.INotaAclaratoriaMundo;

public class ImpresionResumenAtencionesAction extends Action
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ConceptosCarteraAction.class);

	//Constantes de seccion para el reporte
	private static int codigoSeccionValoracionInicialVariosIngresos=0;
	private static int codigoSeccionEpicrisis=1;
	private static int codigoSeccionAntecedentes=2;
	private static int codigoSeccionValoracionInicial=3;
	private static int codigoSeccionEvolucionesMedicas=4;
	private static int codigoSeccionOrdenesMedicas=5;
	private static int codigoSeccionSignosVitales=6;
	private static int codigoSeccionSoporteRespiratorio=7;
	private static int codigoSeccionControlLiquidos=8;
	private static int codigoSeccionCateteresSondas=9;
	private static int codigoSeccionCuidadosEspeciales=10;
	private static int codigoSeccionNotasEnfermeria=11;
	private static int codigoSeccionHojaNeurologica=12;
	private static int codigoSeccionAdminMedicamentos=13;
	private static int codigoSeccionConsumoInsumos=14;
	private static int codigoSeccionCirugias=15;
	private static int codigoSeccionRespInterpretaInterconsultas=16;
	private static int codigoSeccionRespInterprataProcedimientos=17;
	private static int codigoSeccionCargosDirectos = 18;
	private static int codigoSeccionValoracionesCE=19;
	private static int codigoSeccionEventosAdversos=20;
	private static int codigoSeccionResumenParcialHistoriaClinica=21;
	private static int codigoSeccionValoracionesCuidadoEspecial=22;
	private static int codigoSubseccionOrdenesMedicamentos=23;
	private static int codigoSubseccionOrdenesProcedimientos=24;
	private static int codigoSubseccionHojaAnestesia=25;
	private static int codigoSubseccionHojaAdministracionMedicamentos=26;
	private static int codigoSubseccionHojaQuirurgica=27;
	private static int codigoSeccionOrdenesAmbulatorias=28;
	private static int codigoSeccionConsultasPYP=29;
	private static int codigoSeccionResultadosLaboratorios=30;
	private static int codigoSeccionValoracionEnfermeria=31;
	private static int codigoSeccionNotasGeneralesCirugia=32;
	private static int codigoSeccionNotasRecuperacion=33;
	private static int codigoSeccionNotasAclaratorias=34;







	/**
	 * Método execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception
			{
		Connection con = null;
		try {
			if(form instanceof ImpresionResumenAtencionesForm )
			{
				
				//INTENTAMOS ABRIR UNA CONEXION CON LA FUENTE DE DATOS
				con = UtilidadBD.abrirConexion(); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				
//				logger.info("Recibe la peticion para integracion");
				String clave = request.getParameter("claveComandoIntegracion");
				HttpContextComando contexto = (HttpContextComando)request.getSession().getServletContext().getAttribute("contextoComando");
				ComandoPeticion comando =   contexto.recibir(clave);

                ImpresionResumenAtencionesForm forma = (ImpresionResumenAtencionesForm) comando.getFormulario();
				//ImpresionResumenAtencionesForm forma = (ImpresionResumenAtencionesForm) form;
				HttpSession sesion = request.getSession();
					
				//SE OBTIENE EL USUARIO DE SESION
				UsuarioBasico usuario = null;
//				usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				usuario=(UsuarioBasico)comando.getUsuario();
				

				//SE OBTIENE EL PACIENTE
//				PersonaBasica paciente = (PersonaBasica)sesion.getAttribute("pacienteActivo");
				PersonaBasica paciente =(PersonaBasica)comando.getPaciente();

				//SE OBTIENE la  lista  de  dto
//				List<DtoIngresoHistoriaClinica> listaDtoHc=(List<DtoIngresoHistoriaClinica>)sesion.getAttribute("listaIngresos");
				List<DtoIngresoHistoriaClinica> listaDtoHc=(List<DtoIngresoHistoriaClinica>)comando.getListaDtoHc();
				if(listaDtoHc!=null){
					forma.setListaIngresosSeleccionados(ingresosSeleccioandos(listaDtoHc));
				}

				//SE OBTIENE EL MUNDO
				ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones  ();

				//SE CONSULTAN LAS CUENTAS DEL PACIENTE 
				ResumenAtenciones resumenAtencionesMundo2 = new ResumenAtenciones();
				List cuenta2 = (ArrayList) resumenAtencionesMundo2.busquedaCuentas(con, paciente.getCodigoPersona());

				//SE OBTIENE EL CODIGO ULTIMA VIA DE INGRESO 
				HashMap valoresPacienteIngreso2 = new HashMap();
				Integer codigoUltimaViaIngreso=new Integer (paciente.getCodigoUltimaViaIngreso());
				
				if(cuenta2!=null && cuenta2.size()>0 ){
					valoresPacienteIngreso2 =(HashMap)cuenta2.get(0);
					String codeViaIngreso =String.valueOf(valoresPacienteIngreso2.get("codigoviaingreso"));
					if(!UtilidadTexto.isEmpty(String.valueOf(valoresPacienteIngreso2.get("codigoviaingreso")))){
						paciente.setCodigoUltimaViaIngreso(Integer.valueOf(codeViaIngreso));
					}
				}
				
				//Se asigna la via ingreso actual en caso de que sea llamado por el vinvulo de Ordenes de las ultimas 24 horas
				forma.setViaIngreso(paciente.getCodigoUltimaViaIngreso()+"");

				//SE OBTIENE LA INSTITUCION DE SESION
//				InstitucionBasica institucionActual = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
				InstitucionBasica institucionActual = (InstitucionBasica)comando.getInstitucionActual();
				//SE OBTIENE EL ESTADO 
				String estado = forma.getEstado();
				logger.warn("estado [ImpresionResumenAtencionesAction] --> "+estado);

				//SI EL ESTADO LLEGA NULO 
				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ConceptosCarteraAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("generarReporte")||estado.equals("generarReporteNotasAclaratorias"))
				{
					//SE VALIDA EL PARAMETRO GUARDADO EN BASE DE DATOS PARA IDENTIFICAR SI SE IMPRIME EL REPORTE EN PDF O EN JSP
					Integer estadoFuncionalidad = UtilidadesHistoriaClinica.consultarEstadoFuncionalidadHistoriaClincia(con);

					if(estadoFuncionalidad.equals(IConstantesReporteHistoriaClinica.estadoParametroTipoHistoriaClinica) || forma.isImpresionPop()){
						//SEGUN EL VALOR DEL PARAMETRO DE LA TABLA PARAM_SECCION_CLIENTE  SE GENERA EL REPORTE EN FORMATO PDF 
						return reporteDynamicReports(forma, request, mundo, con,paciente, usuario, mapping, listaDtoHc, estado,
																	institucionActual, response, sesion,codigoUltimaViaIngreso,estadoFuncionalidad);

					}else{
						//SEGUN EL VALOR DEL PARAMETRO DE LA TABLA PARAM_SECCION_CLIENTE  SE GENERA EL REPORTE ANTIGUO QUE ESTA FORMADO POR UN JSP 
						return reporteSoloJsp(forma, request, response, mundo, con, paciente, usuario, mapping,estadoFuncionalidad);
					}
				}
			}
			else
			{
				//ENVIA A LA PAGINA DE ERROR 
				logger.error("El form no es compatible con el form de ConceptosCarteraForm");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
			}

			return null;

		}//CONTROL DE ERRORES
		catch (Exception e) {
			e.printStackTrace();
			logger.error("Error generando el reporte de Historia Clinica"+e.getMessage());
			return null;
		}
		finally{
			//SE CIERRA LA CONEXION A BD 
			UtilidadBD.closeConnection(con);
		}
}







	/**
	 * Metodo que se encarga de generar el reporte de historia clinica en formato pdf
	 * @param forma
	 * @param request
	 * @param mundo
	 * @param con
	 * @param paciente
	 * @param usuario
	 * @param mapping
	 * @param listaDtoHc
	 * @param estado
	 * @param institucionActual
	 * @param response
	 * @param sesion
	 * @param codigoUltimaViaIngreso
	 * @return ActionForward para mostrar el rpeorte generado
	 * @throws SQLException
	 * @throws IOException
	 * @throws IPSException 
	 * @throws NumberFormatException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ActionForward reporteDynamicReports(ImpresionResumenAtencionesForm forma,HttpServletRequest request,
			ImpresionResumenAtenciones mundo,Connection con,PersonaBasica paciente,
			UsuarioBasico usuario,ActionMapping mapping,List<DtoIngresoHistoriaClinica> listaDtoHc,
			String estado,InstitucionBasica institucionActual,HttpServletResponse response,HttpSession sesion,
			Integer codigoUltimaViaIngreso,Integer estadoFuncionalidad) throws SQLException, IOException, NumberFormatException, IPSException{
		logger.info("Entro al metodo reporteDynamicReports");


		DtoImpresionHistoriaClinica dto=new DtoImpresionHistoriaClinica();
		DtoFiltroImpresionHistoriaClinica filtro= new DtoFiltroImpresionHistoriaClinica();
		if(dto.isImprimirValoracionesEnfermeria()){
			logger.warn("Esta en valoracion enferemeria");
		}

		//INICIA EN 1 POR QUE SI NO ES DE VARIOS INGRESOS ENTONCES SOLO GENERA UN REPORTE 
		Integer tamanoCiclo=1;
		Boolean varios=false;

		//REPORTE QUE CONTIENE LOS RPEORTES DE OTROS INGRESOS 
		VerticalListBuilder list = cmp.verticalList();
		JasperReportBuilder report = report();
		Boolean primerIngreso = true;
		Boolean pintar=false;

		//SI SE IMPRIMEN VARIOS INGRESOS SE OBTIENE EL LISTADO DE INGRESOS SELECCIONADOS 
		if(listaDtoHc!=null && forma.getTipoImpresion().equals("varios")){
			tamanoCiclo=forma.getListaIngresosSeleccionados().split(",").length;
			varios=true;
		}

		//SE VALIDA SI SE SELECCIONO ALGUNA SECCION A PINTAR 
		if(varios){
			if(!forma.getListaIngresosSeleccionados().equals("")){
				pintar=true;
			}
		}else{
			pintar=true;
		}

		//LISTADO DE INGRESOS SELCCIONADOS PARA IMPRIMIR Y SE DA LA VUELTA A LA CADENA PARA ORDENAR SEGUN EL DCU 
		if(listaDtoHc!=null && listaDtoHc.size()>0){
			if(!listaDtoHc.get(0).isOrdenamiento()){
				forma.setListaIngresosSeleccionados(UtilidadesHistoriaClinica.darVueltaCadena(forma.getListaIngresosSeleccionados()));
			}
		}

		//CICLO PARA IMPRIMIR EL REPORTE DE VARIOS INGRESOS , EN CASO DE SOLO SER UNO YA ESTA INICIALZIADO EN 1 
		for (int q = 0; q < tamanoCiclo && pintar ; q++) {

			//SI SON VARIOS INGRESOS SE SETTEA LOS SIGUIENTES VALORES PARA QUE LAS CONSULTAS TRAIGAN LOS DATOS ASOCIADOS A ESE INGRESO
			if(listaDtoHc!=null && varios){
				Integer iterador = Integer.valueOf( forma.getListaIngresosSeleccionados().split(",")[q]);
				forma.setCuenta(listaDtoHc.get(iterador).getCuenta());
				forma.setViaIngreso(listaDtoHc.get(iterador).getCodigoViaIngreso());
				forma.setIdIngreso(listaDtoHc.get(iterador).getIng2());
				forma.setFiltroAsocio(listaDtoHc.get(iterador).getAsocio());
			}

			//DESDE LA FUNCIONALIDAD DE NOTAS ACLARATORIAS SE PUEDE IMPIMIR SOLO ESTA SECCION ACA SE VALDIA 
			if(!estado.equals(IConstantesReporteHistoriaClinica.constanteGenerarReporteNotasAclratorias)){

				forma.reset();

				//VALIDACIONES DE SOLICITUDES DE FACTURAS 
				if(request.getParameter(IConstantesReporteHistoriaClinica.parametroSolicitudesFactura)!=null)
				{
					forma.setSolicitudesFactura(request.getParameter(IConstantesReporteHistoriaClinica.parametroSolicitudesFactura).toString());
				}

				//SE LISTAN LAS SECCIONES A IMPRIMIR EN UN MAP  DEPENDIENDO SI ES UNO O VARIOS INGRESOS SE LLAMA AL METODO 
				if(!varios){

					//UN SOLO INGRESO
					mostrarSecciones(forma,estadoFuncionalidad);
				}else{

					//VARIOS INGRESOS 
					mostrarSeccionesVariosIngresos(forma);
				}

				//SE ADICIONAN VALORES BOOLEANOS PARA SABER SI SE IMPRIME CADA SECCION 
				filtro=cargarFiltroBusquedaImpresionHistoriaClinica(con,forma,paciente);

				/**
				* Tipo Modificacion: Segun incidencia 6493
				* Autor: Alejandro Aguirre Luna
				* usuario: aleagulu
				* Fecha: 11/02/2013
				* Descripcion: 
				* 			   Se ha cambiado lo modificaciòn realizada el 
				* 			   11/01/2013 en donde se hacia llamado la metodo
				* 			   consultarInformacionHistoriaClinica
				* 			   no sobrecargado. Ahora es importante conocer el tipo de paciente
				* 			   cargado, puede ser Hospitalizado, (H), Ambulatorio (A) ó 
				* 			   Cirugia Ambulatoria (C). 	   		   
				**/
				//SE CONSULTA CRONOLOGICAMENTE EL ORDEN DE LAS SECCIONES A IMPRIMIR 
//				dto.setResultadoSeccionEvolucion(ImpresionResumenAtenciones.consultarInformacionHistoriaClinica(con,filtro,ConstantesImpresionHistoriaClinica.codigoSeccionEvoluciones,paciente));
//				dto.setResultadoSeccionEvolucion(ImpresionResumenAtenciones.consultarInformacionHistoriaClinica(con,filtro,ConstantesImpresionHistoriaClinica.codigoSeccionEvoluciones));
				/**
				* Tipo Modificacion: Segun incidencia 5055
				* Autor: Alejandro Aguirre Luna
				* usuario: aleagulu
				* Fecha: 25/02/2013
				* Descripcion: 
				* Se cambia la modificación realizada el 11/02/2013
				* debido a que se va a utilizar el mètodo consultarInformacionHistoriaClinica
				* sobrecargado debido a que permite enviar el IdIngreso para lograr
				* obtener en la consulta todas la cuentas asociadas al paciente. Se debe tener 
				* en cuenta que hasta el momento solo se realiza la modificación para obtener 
				* la Descripcion Quirirgica y Anestesica de la tercera cuenta, pero esto deberia 
				* funcionar para todas las atenciones.    		   
				**/
				dto.setResultadoSeccionEvolucion(ImpresionResumenAtenciones.consultarInformacionHistoriaClinica(con,filtro,ConstantesImpresionHistoriaClinica.codigoSeccionEvoluciones,paciente, forma.getIdIngreso()));
				RegistroEnfermeria mundoRegistroEnfer=new RegistroEnfermeria();

				String fechaInicialBD=""; 
				String fechaFinalBD="";
				
				if(!forma.isEsBusquedaResumen() && !forma.isImpresionPop()) {
					forma.setFechaInicial("");
					forma.setFechaFinal("");
					forma.setHoraInicial("");
					forma.setHoraFinal("");
				}	

				//SE TOMA A FECHA INICIAL Y FINAL QUE SERAN USADAS EN LAS CONSULTAS DE CADA SECCION 
				if(UtilidadCadena.noEsVacio(forma.getFechaInicial())){
					fechaInicialBD=UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial());
				}

				if(UtilidadCadena.noEsVacio(forma.getFechaFinal())){
					fechaFinalBD=UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal());
				}


				//SE OBTIENE CUALES SERIAN LAS CUENTAS DE ASOCIO Y DE HOSPITALIZACION
				int codigoCuenta=Integer.parseInt(forma.getCuenta());

				//SE OBTIENE LA CUENTA ASOCIADA 
				int cuentaAsociada=UtilidadValidacion.tieneCuentaAsociada(con, Integer.parseInt(forma.getIdIngreso()));

				//SE VALIDA EL TIPO DE VALORACION A IMPRIMIR URGENCIAS U HOSPITALIZACION O LAS DOS 
				if(cuentaAsociada!=0)
				{   
					if(forma.getFiltroAsocio().equals(IConstantesReporteHistoriaClinica.constanteValidarUrgencias))
					{
						codigoCuenta=cuentaAsociada;
					}
					else if(forma.getFiltroAsocio().equals(IConstantesReporteHistoriaClinica.constanteValidacionValoracionHospitalizacion))
					{
						cuentaAsociada=codigoCuenta;
					}
				}


				//SE INICIA CON LA EVALUACION DE LAS SECCIONES PARA SABER QUE IMPRIMO.

				//SE ADICIONAN LOS  SIGUIENTES VALORES PARA MOSTRAR EN EL RPEORTE 
				dto.setIdIngreso(forma.getIdIngreso());
				dto.setCuenta(forma.getCuenta());
				dto.setCuentaAsociada(String.valueOf(cuentaAsociada));

				//MT 4431: Se debe tomar la fecha de egreso de la tabla ingresos.
				/*
				 * Para evitar ir a la BD consulto la informacion de las Cuentas
				 */
				ManejoPacienteFacade manejoPacienteFacade = new ManejoPacienteFacade();
				forma.setIngresoSelecccionado(manejoPacienteFacade.obtenerInfoIngreso(Integer.parseInt(dto.getIdIngreso())));
				//Fin MT 4431	
				

				if(UtilidadTexto.isEmpty(forma.getFiltroAsocio())){
					forma.setFiltroAsocio("");
				}

				//CADA SECCION VALIDA SI SE IMPRIME Y ESTA INFORMACION SE ADICIONA AL DTO QUE SERA ENVIADO AL REPORTE 


				//SECCION SIGNOS VITALES
				if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteSignosVitales))))
				{
					accionCargarSeccionSignosVitales(con, mundoRegistroEnfer, forma, usuario.getCodigoInstitucionInt(), paciente.getCodigoArea(), UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, forma.getIdIngreso()+""), fechaInicialBD, fechaFinalBD, forma.getHoraInicial(), forma.getHoraFinal(), forma.getFiltroAsocio());
					dto.setSignosVitales(new DtoSignosVitalesHC(forma.getSignosVitalesInstitucionCcosto(), forma.getSignosVitalesFijosHisto(), forma.getSignosVitalesParamHisto(), forma.getSignosVitalesHistoTodos()));
					dto.setImprimirSignosVitales(true);
				}

				//SECCION VALORACIONES DE ENFERMERIA
				if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteValoracionesEnfermeria))))
				{
					forma.setValoracionesEnfermeria(mundo.consultarrValoracionesEnfermeria(con,forma.getIdIngreso(),fechaInicialBD, fechaFinalBD, forma.getHoraInicial(), forma.getHoraFinal()));
					dto.setImprimirValoracionesEnfermeria(true);
					dto.setValoracionesEnfermeria(forma.getValoracionesEnfermeria());
				}

				//SECCION ADMINISTRACION DE MEDICAMENTOS 
				if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteAdministracionMedicamentos))))
				{
					cargarAdminMedicamentos(con, forma,  mundo);
					dto.setImprimirAdminMedicamentos(true);
					dto.setAdminMedicamente(forma.getAdminMedicamentos());
				}

				//SECCION CONSUMOS INSUMOS 
				if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteConsumoInsumos)))){
					forma.setInsumos(mundo.consultarInsumos(con, crearValueObjectFiltro(forma,ImpresionResumenAtencionesAction.codigoSeccionConsumoInsumos)));
					dto.setImprimirConsumosInsumos(true);
					dto.setConsumosInsumos(forma.getInsumos());
				}			

				//SECCION REPUESTA INTERPRETACION DE MEDICAMENTOS 
				if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteRespuestaInterpretacionProcedimientos)))){
					forma.setRepuestaInterpretacionProcedimientos(mundo.consultarRespuestaInterpretacionProcedimientos(con, crearValueObjectFiltro(forma,ImpresionResumenAtencionesAction.codigoSeccionRespInterprataProcedimientos)));
					dto.setInterpretacionRespuesta(forma.getRepuestaInterpretacionProcedimientos());
					dto.setImprimirInterpretacionRespuesta(true);
				}

				//SECCION ANOTACIONES DE ENFERMERIA
				if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteNotasEnfermeria))))
				{
					forma.setHistoricoAnotacionesEnfermeria(mundoRegistroEnfer.consultarAnotacionesEnfermeriaImpresionHC(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, forma.getIdIngreso()),fechaInicialBD, fechaFinalBD, forma.getHoraInicial(), forma.getHoraFinal(), forma.getFiltroAsocio().trim()));
					logger.warn(forma.getHistoricoAnotacionesEnfermeria());
					dto.setImprimirNotasEnfermeria(true);
					dto.setNotasEnfermeria(forma.getHistoricoAnotacionesEnfermeria());
				}

				//SECCION CATETERES Y SONDAS
				if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteCateteresSondas)))){
					accionCargarSeccionCateterSonda(con, mundoRegistroEnfer, forma, usuario.getCodigoInstitucionInt(), paciente.getCodigoArea(), UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, forma.getIdIngreso()+""), fechaInicialBD, fechaFinalBD, forma.getHoraInicial(), forma.getHoraFinal(), forma.getFiltroAsocio());
					dto.setImprimirCateteresSondas(true);
					dto.setColumnasDinamicasCateteresSondas(forma.getColCateteresSondaInstitucionCcosto());
					dto.setCateteresSondas(forma.getMapaHistoricoCateterSonda());
				}

				//CUIDADOS ESPECIALES
				if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteCuidadosEspeciales)))){
					accionCargarSeccionCuidadosEspeciales(con, mundoRegistroEnfer, forma, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, forma.getIdIngreso()+""), fechaInicialBD, fechaFinalBD, forma.getHoraInicial(), forma.getHoraFinal(), forma.getFiltroAsocio());
					dto.setImprimirCuidadosEspeciales(true);
					dto.setCuidadosEspeciales(forma.getMapaHistoricoCuidadosEspeciales());
				}

				//SE SETTEA LA INFORMACION DEL ENCABEZADOS DEL PACIENTE 
				dto.setEncabezadoPaciente(mundo.obtenerEncabezadoPaciente(con, forma.getIdIngreso()));

				//SECCION SOPORTE RESPIRATORIO 
				if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteSoporteRespiratorio))))
				{
					forma.setSoporteRespiratorio(mundo.consultarSoporteRespiratorio(con,crearValueObjectFiltro(forma,ImpresionResumenAtencionesAction.codigoSeccionSoporteRespiratorio)));
					dto.setImprimirSoporteRespiratorio(true);
					dto.setSoporteRespiratorio(forma.getSoporteRespiratorio());
				}

				//SECCION CONTROL DE LIQUIDOS
				if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteControLiquidos))))
				{
					cargarControlLiquidos(con, request, 0, paciente, usuario, forma, codigoCuenta, cuentaAsociada);
					cargarControlLiquidos(con, request, 1, paciente, usuario, forma, codigoCuenta, cuentaAsociada);
					dto.setImprimirControlLiquidos(true);
					dto.setControlLiquidos(forma.getMapaControlLiq()); 
				}

				//SECCION ORDENES AMBULATORIAS
				if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteOrdenesAmbulatorias))))
				{
					this.accionCargarInformacionOrdenesAmbulatorias(con,mundo,forma);
					dto.setImprimirOrdenaAmbulatorias(true);
					dto.setOrdenesAmbulatorias(forma.getOrdenesAmbulatorias());					
				}

				//SECCION ANTECEDENTES 
				if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteAntecedentes))))
				{
					cargarAntecedentesVarios(con, 1, paciente, usuario, forma);
					dto.setImprimirAntecedentes(true);
					dto.setAntecedentes(forma.getMapaAntOtros());

				}

				//SECCION HOJA QUIRURGICA - ANESTESIA - NOTAS CIRUGIA - NOTAS RECUPERACION 
				//LAS NOTAS DEPENDEN SI HAY UNA SOLICITUD DE CIRUGIA ENTONCES SEHACEN LAS SIGUIENTES VALDACIONES PARA IMPRIMIR SOLO LO QUE 
				//SE ESTA SOLICITANDO
				if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteCirugias))) 
						|| UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteHojaQuirurgica))) 
						|| UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteHojaAnestesia)))
						|| UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteNotasGeneralesCirugia)))
						|| UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteNotasRecuperacion))))
				{


					if( (!(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteCirugias))))) 
							&& !(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteNotasRecuperacion))))
							&& UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteNotasGeneralesCirugia))))
					{
						//SI SE IMPRIME SOLO LAS NOTAS GENERALES  
						dto.setImprimirNotasGeneralesCirugia(true);
					}
					else if( ((UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteCirugias))) 
					)) && !UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteNotasGeneralesCirugia)))
					&& !UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteNotasRecuperacion)))
					)
					{
						//SI SE IMPRIME SOLO LA HOJA DE CIRUGIA 
						dto.setImprimirInformacionCirugia(true);

					}else if( (!(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteCirugias))) 
					)) && !UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteNotasGeneralesCirugia)))
					&& UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteNotasRecuperacion)))
					)
					{
						//SI SE IMPRIME SOLO LAS NOTAS DE RECUPERACION 
						dto.setImprimirNotasRecuperacion(true);

					}
					else if( ((UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteCirugias))) 
					)) && UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteNotasGeneralesCirugia)))
					&& UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteNotasRecuperacion)))
					)
					{
						//SI SE IMPRIMEN LAS 3 
						dto.setImprimirNotasGeneralesCirugia(true);
						dto.setImprimirInformacionCirugia(true);
						dto.setImprimirNotasRecuperacion(true);
					}

					else if( (!(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteCirugias))) 
					)) && UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteNotasGeneralesCirugia)))
					&& UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteNotasRecuperacion)))
					)
					{
						//SI SOLO IMPRIMEN NOTAS GENERALES Y RECUPERACION 
						dto.setImprimirNotasGeneralesCirugia(true);
						dto.setImprimirNotasRecuperacion(true);
					}

					else if( ((UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteCirugias))) 
					)) && !UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteNotasGeneralesCirugia)))
					&& UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteNotasRecuperacion)))
					)
					{
						//SI IMPRIMEN HOJA DE CIRUGIA Y NOTAS DE RECUPERACION 
						dto.setImprimirInformacionCirugia(true);
						dto.setImprimirNotasRecuperacion(true);
					}

					else if( ((UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteCirugias))) 
					)) && UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteNotasGeneralesCirugia)))
					&& !UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteNotasRecuperacion)))
					)
					{
						//SI IMPRIME LA HOJA DE CIRUGIA Y NOTAS GENERALES 
						dto.setImprimirInformacionCirugia(true);
						dto.setImprimirNotasGeneralesCirugia(true);
					}
					ArrayList <DtoResultadoImpresionHistoriaClinica> resultado=dto.getResultadoSeccionEvolucion();
					ArrayList<DtoHojaQuirurgicaAnestesia> listaQuirurgica=new ArrayList<DtoHojaQuirurgicaAnestesia>();
					
					/**
					* Tipo Modificacion: Segun incidencia 5055
					* Autor: Alejandro Aguirre Luna
					* usuario: aleagulu
					* Fecha: 25/02/2013
					* Descripcion: Mediante la variable contadorDeCuentas se determina la posicion
					* actual de las cuentas del paciente. Se utiliza en le mètodo accionCargarSeccionCirugias
					* para enviar la posicion actual de todas las cuentas relacionadas al IdIngreso. 
					**/
					int contadorDeCuentas = 0;
					//SE CONSULTAN TODAS LAS CIRUGIAS Y LAS PARTES QUE CONFORMAN LA HOJA QUIRUGICA Y LAS NOTAS 
					for(int i=0;i<resultado.size();i++)
					{
						DtoResultadoImpresionHistoriaClinica dtoTmp=(DtoResultadoImpresionHistoriaClinica)resultado.get(i);
						if(dtoTmp.getCodigoTipoEvolucion()==ConstantesImpresionHistoriaClinica.codigoSeccionCirugias){

							/**
							* Tipo Modificacion: Segun incidencia 5055
							* Autor: Alejandro Aguirre Luna
							* usuario: aleagulu
							* Fecha: 25/02/2013
							* Descripcion: Se llama al metodo sobrecargado accionCargarSeccionCirugias
							* que permite el envio del argumento contadorDeCuentas. 
							* Esto con el fin de determinar la posicion actual de la cuenta para 
							* establecer la impresion de la Hoja Quirurjica cuando se tienen más de
							* tres cuentas. Se deja en comentario la implementación anterior.   
							**/
								//SE CONSULTA LA INFORMACION DE LAS CIRUGIAS 
								/*accionCargarSeccionCirugias(con, mundo, 
										forma, usuario.getCodigoInstitucionInt(),
										paciente.getCodigoArea(), UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, forma.getIdIngreso()+""), 
										fechaInicialBD, fechaFinalBD, forma.getHoraInicial(), forma.getHoraFinal(),
										forma.getFiltroAsocio(),dtoTmp.getCodigoPk());*/
							
							DtoHojaQuirurgicaAnestesia  dtoHojaQuirurgicaAnestesia = new DtoHojaQuirurgicaAnestesia();
							
							accionCargarSeccionCirugias(con, mundo, 
									forma, usuario.getCodigoInstitucionInt(),
									paciente.getCodigoArea(), dtoTmp.getCodigoCuenta()+"", 
									fechaInicialBD, fechaFinalBD, forma.getHoraInicial(), forma.getHoraFinal(),
									forma.getFiltroAsocio(),dtoTmp.getCodigoPk(), contadorDeCuentas,dtoHojaQuirurgicaAnestesia);
							// Incrementar el contador para seguir con la siguiente cuenta. 
							contadorDeCuentas++;

							//SE CONSULTA  LAS PETICIONES DE CIRUGIA
							dtoHojaQuirurgicaAnestesia.setMapaCodigosPeticionCirugia((HashMap)forma.getMapaCodigosPeticionCirugia().clone());

							//SE CONSULTA LOS ENCABEZADOS DE LA HOJA DE ANESTECIA 
							dtoHojaQuirurgicaAnestesia.setMapaEncabezadosHojaAnestesia((HashMap)forma.getMapaEncabezadosHojaAnestesia().clone());

							//SE CONSULTA LABORATORIO 
							dtoHojaQuirurgicaAnestesia.setMapaExamenesLaboratorioPreanestesia((HashMap)forma.getMapaExamenesLaboratorioPreanestesia().clone());

							//SE CONSULTA LOS EXAMENES FISICOS
							dtoHojaQuirurgicaAnestesia.setMapaHistoExamenesFisicosText((HashMap)forma.getMapaHistoExamenesFisicosText().clone());
							dtoHojaQuirurgicaAnestesia.setMapaHistoExamenesFisicosTextArea((HashMap)forma.getMapaHistoExamenesFisicosTextArea().clone());

							//SE CONSULTA CONLUSIONES
							dtoHojaQuirurgicaAnestesia.setMapaHistoConclusiones((HashMap)forma.getMapaHistoConclusiones().clone());

							//SE CONSULTA DATOS DE LA HOJA QUIRUGICA
							dtoHojaQuirurgicaAnestesia.setMapaHojaQuirur((HashMap)forma.getMapaHojaQuirur().clone());

							//SE CONSULTA LOS MAPAS DE ENFERMERIA
							dtoHojaQuirurgicaAnestesia.setMapaNotasEnfer((HashMap)forma.getMapaNotasEnfer().clone());

							//SE CONSULTA NOTAS DE ENFERMERIA
							dtoHojaQuirurgicaAnestesia.setMapaNotasRecuperacion((HashMap)forma.getMapaNotasRecuperacion().clone());

							//SE CONSULTA NOTAS DE RECUPERACION
							dtoHojaQuirurgicaAnestesia.setMapaNotasRecuperacionDetalle((HashMap)forma.getInformacionNotasRecuperacion().clone());

							//SE ADICIONA LA IFNROMACION DELA HOJA QUIRUGICA 
							resultado.get(i).setHojaQuirurgica(dtoHojaQuirurgicaAnestesia);
						}

					}
					dto.setResultadoSeccionEvolucion(resultado);

				}

				/**
				 * Control Cambio MT 1605 Version Cambio 1.3.0
				 * @author leoquico
				 * @fecha  24/07/2013
				 * @descripcion Boton Resumen Historia Clinica Validacion para cuando no existe informacion 
				 */
				boolean noInformacionHC = false;
				
				if (forma.getImpresionResumenHC() != null && !forma.getImpresionResumenHC().isEmpty() && forma.getCodigoManual()!=null) { 
				    
					noInformacionHC = mundo.validarHistoriaClinica(forma, dto);
				}
				if (noInformacionHC) {
					try
					{
						/*
						 * Se envia la respuesta ajax con mensaje: No Se encuentra informacion Boton HC 
						 */
						response.setCharacterEncoding("UTF-8");
			            response.setContentType("text/plain");
						response.setHeader("Cache-Control", "no-cache");
						response.getWriter().write(forma.getCodigoManual());
						
					}
					catch(IOException e)
					{
						logger.error("Error al enviar respuesta AJAX en accionHistoria Clinica: "+e);
					}
					
					return null;
				}

				//SE CONSULTA LA SECCION DE RESUMEN PARCIAL DE HISTORIA CLINICA 
				if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteResumenParcialHistoriaClinica))))
				{
					dto.setHayResumenParcial(true);
					RegistroResumenParcialHistoriaClinica rphc=new RegistroResumenParcialHistoriaClinica();
					HashMap mapa=new HashMap();		

					//SE CONSULTA EL RESUMEN DE HISTORIA CLINICA DEPENDIENDO SI ES DE ASOCIO O NO 
					mapa.put(IConstantesReporteHistoriaClinica.constanteIngreso,forma.getIdIngreso());
					if(forma.getFiltroAsocio().trim().equals(IConstantesReporteHistoriaClinica.constanteFiltroAsocioA)){
						dto.setResumenParcialHc(rphc.consultarNotasAsocio(con, mapa));
					}else{
						dto.setResumenParcialHc(rphc.consultarNotas(con, mapa));
					}
				}
			}

			//SE CONSULTA LA INFORMACION DE LAS NOTAS ACLARATORIAS 
			if(estado.equals( IConstantesReporteHistoriaClinica.constanteGenerarReporteNotasAclratorias) || UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteNotasAclaratoriasSeccion))))
			{
				dto.setImprimirNotasAclaratorias(true);
				try{
					HibernateUtil.beginTransaction();
					INotaAclaratoriaMundo notaAclaratoriaMundo = HistoriaClinicaFabricaMundo.crearNotaAclaratoriaMundo();
					List<DtoNotaAclaratoria> notasAclaratorias=	notaAclaratoriaMundo.buscarNotasAclaratoriasPorIngreso(Integer.valueOf(forma.getIdIngreso()));
					dto.setListaNotasAclaratorias(notasAclaratorias);
					HibernateUtil.endTransaction();
				}//CONTROL DE ERRORES 
				catch (Exception e) {
					logger.error("error consultando las notas aclaratorias "+e);
					HibernateUtil.abortTransaction();
				}
			}




			try {

				//SE INSTANCIA LA PERSONA 
				PersonaBasica personaBasica = new PersonaBasica();

				//SE ABRE LA CONEXION A BD
				con = UtilidadBD.abrirConexion();

				//SE CARGA LA PERSONA CON DATOS DE VIA DE INGRESO ...ETC
				personaBasica.cargar(con, usuario.getCodigoPersona());
				personaBasica.cargarPaciente(con, personaBasica.getCodigoPersona(),usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
				String viaEgreso = personaBasica.obtenerViaEgreso(con,String.valueOf(paciente.getCodigoPersona()));

				//SE OBTIENEN LA CUENTAS 
				ResumenAtenciones resumenAtencionesMundo = new ResumenAtenciones();
				List cuenta = (ArrayList) resumenAtencionesMundo.busquedaCuentas(con, paciente.getCodigoPersona());
				//HashMap valoresPacienteIngreso = new HashMap();

				if(cuenta!=null && cuenta.size()>0){
					for(HashMap valoresPacienteIngreso:(List<HashMap>)cuenta){
						if(valoresPacienteIngreso.get(IConstantesReporteHistoriaClinica.constanteCodigoCuenta).toString().equals(forma.getCuenta())){
							//valoresPacienteIngreso =(HashMap)cuenta.get(q);
		
							//SE BTIENE EL CODIGO DE VIA DE INGRESO 
							String codeViaIngreso =String.valueOf(valoresPacienteIngreso.get(IConstantesReporteHistoriaClinica.constanteCodigoviaingreso));
							String fecha =String.valueOf(valoresPacienteIngreso.get(IConstantesReporteHistoriaClinica.constanteFechaegreso));
							String hora =String.valueOf(valoresPacienteIngreso.get(IConstantesReporteHistoriaClinica.constanteHoraegreso));
		
							//SE OBTIENE EL CODIGO DE INGRESO EN STRING 
							dto= obtenerCodigoViaIngreso(dto, codeViaIngreso);
		
							if(fecha !=null && hora !=null 
									&& !fecha.equals("")&& !fecha.equals("")){
								dto.setFechaEgreso(fecha.split(" ")[0]+" "+hora);
							}
		
							//SE OBTIENE EL CODIGO DE VIA DE EGRESO 
							dto =obtenerCodigoViaEgreso(dto, viaEgreso);
							break;
						}
					}
				}


			}//CONTROL DE ERRORES 
			catch (SQLException e) {
				logger.error("error consultando las via de ingreso y egreso del paciente "+e.getMessage());
			}

			//SE VALIDA SI SOLO SE VA A IMPRIMIR LAS NOTAS ACLARATORIAS O EL REPORTE COMPLETO 
			Boolean imprimirSoloNotasAclaratorias = false;
			if(estado.equals(IConstantesReporteHistoriaClinica.constanteGenerarReporteNotasAclratorias)){
				imprimirSoloNotasAclaratorias = true;
			}

			//SI SOLO SE IMPRIME UN INGRESO
			//SI SOLO SE IMPRIME UN INGRESO
			if(!varios){
				//hermorhu - MT6422 
				//La impresion en el detalle de un ingreso se convierte en subreporte para que el reporte quede igual 
				//al reporte de varios ingresos.
				report = generarPdf(dto,filtro,mundo.obtenerEncabezadoPaciente(con, forma.getIdIngreso()),usuario,paciente,institucionActual,forma.getIdIngreso(),imprimirSoloNotasAclaratorias,primerIngreso,/*varios*/true);
				list.add(cmp.subreport(report));
			}else{

				//EN CASO DE IMPRIMIR VARIOS INGRESOS
				report = generarPdf(dto,filtro,mundo.obtenerEncabezadoPaciente(con, forma.getIdIngreso()),usuario,paciente,institucionActual,forma.getIdIngreso(),imprimirSoloNotasAclaratorias,primerIngreso,varios);

				//EL PRIMER INGRESO TIENE UN ENCABEZADO DIFERENTE A LOS DEMAS 
				if(primerIngreso){
//					list.add(cmp.subreport(report.title()));
					list.add(cmp.subreport(report));
					primerIngreso = false;
				}else{
					//DESDE EL SEGUNDO INGREO EN ADELANTE HAY SALTO DE PAGINA Y SE MANTIENE LA NUMERACION DEL REPORTE 
					JasperReportBuilder reporteTemp=new JasperReportBuilder();
					
//					list.add(cmp.subreport(report));
					reporteTemp.summary(cmp.subreport(report));
					list.add(cmp.subreport(reporteTemp.summaryOnANewPage()));
				}
			}
		}

		//SI SE IMPRIMEN VARIOS INGRESOS SE CONSOLIDA EN UN REPORTE QUE CONTIENE MAS REPORTES Y SE ADICIONA FOOTER PARA LA UNICA NUMERACION 
		//hermorhu - MT6422 
		GeneradorDisenioReporteHistoriaClinica disenio = new GeneradorDisenioReporteHistoriaClinica();
		Map<String, String> paramms = new HashMap<String, String>();
		paramms.put(IConstantesReporteHistoriaClinica.usuarioProceso, usuario.getNombreUsuario() + " (" + usuario.getLoginUsuario() + ")");
		
		/*
		 * Se unifican los reportes en uno solo.
		 */
		JasperReportBuilder reporte = report();
		reporte.addSummary(list);
			
		//SE ADICIONA EL FOOTER
		reporte.pageFooter(disenio.crearcomponentePiePagina(paramms));
		reporte.setSummaryWithPageHeaderAndFooter(true);
		
		//BUILD DEL REPORTE
		reporte.setPageMargin(disenio.crearMagenesReporte());
		reporte.build();
			
		//INSTANCIA DEL GENERADOR DEL REPORTE 
		GeneradorReporteDinamico generadorReporteDinamico = new GeneradorReporteDinamico();

		//SE EXPORTA A PDF
		String nombreReporte = generadorReporteDinamico.exportarReportePDF(reporte, IConstantesReporteHistoriaClinica.nombreReporteHistoriaClinica);

		//SE SETTEA EL NOMBRE DEL REPORTE 
		forma.setNombreArchivoGenerado(nombreReporte.replaceAll("\\\\", "\\/"));

		//SE CIERRA LA CONEXION A LA BD 
		UtilidadBD.closeConnection(con);


		//SE ADICIONA AL PACIENTE SU CODIGO DE ULTIMO INGRESO 
		paciente.setCodigoUltimaViaIngreso(codigoUltimaViaIngreso);

		//SE ACTUALIZA EL PACIENTE ACTIVO A SESSION YA QUE EN LAS CONSULTAS SE MODIFICA EL CODIGO DE VIA DE INGRESO 
		sesion.setAttribute("pacienteActivo",paciente);

		//SE OBTIENE EL PACIENTE ACTIVO CON VALORES CORRECTOS 
		paciente = (PersonaBasica)sesion.getAttribute(IConstantesReporteHistoriaClinica.constantePacienteActivo);

		logger.info("Sale del mtodo reporteDynamicReports");

		try
		{
			/*
			 * Se envia la respuesta ajax a la generacion del reporte
			 * para que abra el popup con el pdf generado
			 */
			response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(forma.getNombreArchivoGenerado());
			
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroContrato: "+e);
		}

		//RETORNA NULL POR QUE NO TIENE QUE RE DIRIGIR A NINGUN LADO 
		return null;	
	}

	/**
	 * Metodo que obtiene el nombre  de via de ingreso del paciente a partir de el codigo de via de ingreso  
	 * @param dto
	 * @param codeViaIngreso
	 * @return DtoImpresionHistoriaClinica
	 */
	public DtoImpresionHistoriaClinica obtenerCodigoViaIngreso(DtoImpresionHistoriaClinica dto , String codeViaIngreso){
		logger.info("Entro al metodo obtenerCodigoViaIngreso ");

		//SE VALIDA QUE SEA DIFERENTE DE NULL 
		if (codeViaIngreso!=null) {

			//SE COMPARA EL CODIGO DE LA VIA DE INGRESO Y SE SETTEA SU NOMBRE CORRESPONDIENTE 
			if (codeViaIngreso.equals(IConstantesReporteHistoriaClinica.constanteViaIngresoUno)) {
				dto.setCodigoViaIngreso(IConstantesReporteHistoriaClinica.constanteHospitalizacion);
			}else if (codeViaIngreso.equals(IConstantesReporteHistoriaClinica.constanteViaIngresoDos)) {
				dto.setCodigoViaIngreso(IConstantesReporteHistoriaClinica.constanteAmbulatorio);
			}else if (codeViaIngreso.equals(IConstantesReporteHistoriaClinica.constanteViaIngresoTres)) {
				dto.setCodigoViaIngreso(IConstantesReporteHistoriaClinica.constanteUrgencia);
			}else if (codeViaIngreso.equals(IConstantesReporteHistoriaClinica.constanteViaIngresoCuatro)) {
				dto.setCodigoViaIngreso(IConstantesReporteHistoriaClinica.constanteConsultaExterna);
			}else if (codeViaIngreso.equals(IConstantesReporteHistoriaClinica.constanteViaIngresoDiez)) {
				dto.setCodigoViaIngreso(IConstantesReporteHistoriaClinica.constanteUrgHospHospitalizacion);
			}else if (codeViaIngreso.equals(IConstantesReporteHistoriaClinica.constanteViaIngresoOnce)) {
				dto.setCodigoViaIngreso(IConstantesReporteHistoriaClinica.constanteUrgHospCirugia);
			}else if (codeViaIngreso.equals(IConstantesReporteHistoriaClinica.constanteViaIngresoDoce)) {
				dto.setCodigoViaIngreso(IConstantesReporteHistoriaClinica.constanteHosHosHospitalizacion);
			}else if (codeViaIngreso.equals(IConstantesReporteHistoriaClinica.constanteViaIngresoTrece)) {
				dto.setCodigoViaIngreso(IConstantesReporteHistoriaClinica.constanteUrgencia);
			}else if (codeViaIngreso.equals(IConstantesReporteHistoriaClinica.constanteViaIngresoCartoce)) {
				dto.setCodigoViaIngreso(IConstantesReporteHistoriaClinica.constanteHospitalizacion);
			}else if (codeViaIngreso.equals(IConstantesReporteHistoriaClinica.constanteViaIngresoQuince)) {
				dto.setCodigoViaIngreso(IConstantesReporteHistoriaClinica.constanteUrgUrg);
			}
		}


		logger.info("Salio del metodo obtenerCodigoViaIngreso");
		//SE RETORNA EL DTO CON EL NOMBRE  DE VIA DE INGRESO 
		return dto;
	}


	/**
	 * Método que obtiene el nombre de la via de egreso a partir del codigo 
	 * @param dto
	 * @param viaEgreso
	 * @return DtoImpresionHistoriaClinica con el codigo de la via de egreso 
	 */
	public DtoImpresionHistoriaClinica obtenerCodigoViaEgreso(DtoImpresionHistoriaClinica dto ,String viaEgreso){

		logger.info("Entro del metodo obtenerCodigoViaEgreso");

		//SE VALIDA QUE SEA DIFERENTE DE NULL
		if (viaEgreso!=null && !viaEgreso.equals("")) {

			//SE COMPARA CADA CODIGO PARA SABER EL NOMBRE DE LA VIA DE EGRESO QUE LE CORRESPONDE
			if (viaEgreso.equals(IConstantesReporteHistoriaClinica.constanteViaIngresoUno)) {
				dto.setViaEgreso(IConstantesReporteHistoriaClinica.constanteHospitalizacion);
			}else if (viaEgreso.equals(IConstantesReporteHistoriaClinica.constanteViaIngresoDos)) {
				dto.setViaEgreso(IConstantesReporteHistoriaClinica.constanteAmbulatorio);
			}else if (viaEgreso.equals(IConstantesReporteHistoriaClinica.constanteViaIngresoTres)) {
				dto.setViaEgreso(IConstantesReporteHistoriaClinica.constanteUrgencia);
			}else if (viaEgreso.equals(IConstantesReporteHistoriaClinica.constanteViaIngresoCuatro)) {
				dto.setViaEgreso(IConstantesReporteHistoriaClinica.constanteConsultaExterna);
			}else if (viaEgreso.equals(IConstantesReporteHistoriaClinica.constanteViaIngresoDiez)) {
				dto.setViaEgreso(IConstantesReporteHistoriaClinica.constanteUrgHospHospitalizacion);
			}else if (viaEgreso.equals(IConstantesReporteHistoriaClinica.constanteViaIngresoOnce)) {
				dto.setViaEgreso(IConstantesReporteHistoriaClinica.constanteUrgHospCirugia);
			}else if (viaEgreso.equals(IConstantesReporteHistoriaClinica.constanteViaIngresoDoce)) {
				dto.setViaEgreso(IConstantesReporteHistoriaClinica.constanteHosHosHospitalizacion);
			}else if (viaEgreso.equals(IConstantesReporteHistoriaClinica.constanteViaIngresoTrece)) {
				dto.setViaEgreso(IConstantesReporteHistoriaClinica.constanteUrgencia);
			}else if (viaEgreso.equals(IConstantesReporteHistoriaClinica.constanteViaIngresoCartoce)) {
				dto.setViaEgreso(IConstantesReporteHistoriaClinica.constanteHospitalizacion);
			}else if (viaEgreso.equals(IConstantesReporteHistoriaClinica.constanteViaIngresoQuince)) {
				dto.setViaEgreso(IConstantesReporteHistoriaClinica.constanteUrgUrg);
			}
		}else {

			//SI EL CODIGO NO CORRESPONDE A NINGUNO INDICA QUE ES EL MISMO CODIGO DE VIA DE INGRESO 
			dto.setViaEgreso(dto.getCodigoViaIngreso());
		}

		logger.info("Salio del metodo obtenerCodigoViaEgreso");

		//SE RETORNA EL DTO CON LOS DATOS SOLICITADOS 
		return dto;
	}



	/**
	 * Reporte de Historia Clinica antiguo que esta hecho con JSP 
	 * @param forma
	 * @param request
	 * @param mundo
	 * @param con
	 * @param paciente
	 * @param usuario
	 * @param mapping
	 * @return ActionForward para mostrar el reporte 
	 * @throws SQLException
	 * @throws IPSException 
	 * @throws NumberFormatException 
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public ActionForward reporteSoloJsp(ImpresionResumenAtencionesForm forma,HttpServletRequest request,
			HttpServletResponse response,
			ImpresionResumenAtenciones mundo,Connection con,PersonaBasica paciente,UsuarioBasico usuario,ActionMapping mapping,Integer estadoFuncionalidad) throws SQLException, NumberFormatException, IPSException, IOException{
		logger.info("entro al metodo reporteSoloJsp");
		forma.reset();

		//SE VALIDAN SOLICITUDES DE FACTURA
		if(request.getParameter(IConstantesReporteHistoriaClinica.constanteSolicitudesFactura)!=null)
		{
			forma.setSolicitudesFactura(request.getParameter(IConstantesReporteHistoriaClinica.constanteSolicitudesFactura).toString());
		}

		//SE VALIDAN LAS SECCIONES A MOSTRAR 
		mostrarSecciones(forma,estadoFuncionalidad);

		//SE REALIZAN VALIDACIONES PARA MOSTRAR O NO UNA VALORACION INICIAL 
		if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteValoracionInicial))))
		{

//			//SE VALIDA SI ES FILTRO DE ASOCIO 
//			if(forma.getFiltroAsocio().trim().equals(IConstantesReporteHistoriaClinica.constanteFiltroAsocioA))
//			{
//				//LAS ATENCIONES VIENEN SEPARADAS POR COMAS --> "," DE LOS CHECKS DE PRESENTACION 
//				String[] aten=forma.getAtenciones().split(",");
//				for(int i=0;i<aten.length;i++)
//				{
//
//					//SE VALIDA SI ES VALORACION DE URGENCIAS O DE HOSPITALIZACION
//					if(forma.getViaIngreso().equals(ConstantesBD.codigoViaIngresoHospitalizacion)&&forma.getViaIngreso().equals(ConstantesBD.codigoViaIngresoUrgencias)){
//						if(aten[i].equals(ImpresionResumenAtencionesAction.codigoSeccionValoracionInicial+""))
//						{
//							forma.setImprimirVIHospitalizacion(true);
//						}
//						if(aten[i].equals(ImpresionResumenAtencionesAction.codigoSeccionValoracionInicial+IConstantesReporteHistoriaClinica.constanteFiltroAsocioA))
//						{
//							forma.setImprimirVIUrgencias(true);
//						}
//					}
//				}
//			}
//			else if (forma.getFiltroAsocio().trim().equals(IConstantesReporteHistoriaClinica.constanteValidarUrgencias))
//			{
//				forma.setImprimirVIUrgencias(true);
//			}
//			else if (forma.getFiltroAsocio().trim().equals(IConstantesReporteHistoriaClinica.constanteValidacionValoracionHospitalizacion))
//			{
//				forma.setImprimirVIHospitalizacion(true);
//			}
//			else
//			{
//				forma.setImprimirVIUrgencias(true);
//				forma.setImprimirVIHospitalizacion(false);
//			}

			/**
			 * MT 7009
			 * validaciones para todos los posibles escenarios URG, HOSP, URG->HOSP con copia y URG->HOSP sin copia  
			 */
			if(forma.getViaIngreso().equals(String.valueOf(ConstantesBD.codigoViaIngresoHospitalizacion)))
						{
							forma.setImprimirVIHospitalizacion(true);
						}
			else if(forma.getViaIngreso().equals(String.valueOf(ConstantesBD.codigoViaIngresoUrgencias)))
						{
							forma.setImprimirVIUrgencias(true);
						}
			
			boolean valoracionHospEsCopiaValoracionUrg = SqlBaseResumenAtencionesDao.valoracionHospEsCopiaValoracionUrg(con, Utilidades.convertirAEntero(forma.getCuenta()));
			
			if(forma.getFiltroAsocio().trim().equals(IConstantesReporteHistoriaClinica.constanteFiltroAsocioA) && valoracionHospEsCopiaValoracionUrg)
			{
				forma.setImprimirVIUrgencias(true);
				forma.setImprimirVIHospitalizacion(false);
			}
			else if(forma.getFiltroAsocio().trim().equals(IConstantesReporteHistoriaClinica.constanteFiltroAsocioA) && !valoracionHospEsCopiaValoracionUrg)
			{
				forma.setImprimirVIUrgencias(true);
				forma.setImprimirVIHospitalizacion(true);
			}
			/**
			 * Fin MT 7009
			 */
		}
		//SE CARGA LAS VALORACIONES DE CONSULTA EXTERNA
		if(UtilidadTexto.getBoolean(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteValoracionesCE).toString())){
			this.obtenerEnlacesValoracionesConsultaExterna(con, crearValueObjectFiltro(forma,ImpresionResumenAtencionesAction.codigoSeccionValoracionesCE), forma, usuario, paciente);
		}

		//SECCION DE INSUMOS 
		if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteConsumoInsumos)))){
			forma.setInsumos(mundo.consultarInsumos(con, crearValueObjectFiltro(forma,ImpresionResumenAtencionesAction.codigoSeccionConsumoInsumos)));
		}

		//SECCION RESPUESTAINTERPRETACIONPROCEDIMIENTO;
		if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteRespuestaInterpretacionProcedimientos)))){
			forma.setRepuestaInterpretacionProcedimientos(mundo.consultarRespuestaInterpretacionProcedimientos(con, crearValueObjectFiltro(forma,ImpresionResumenAtencionesAction.codigoSeccionRespInterprataProcedimientos)));
		}

		//SECCION DE ORDENES MEDICAS
		if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteOrdenesMedicas)))){
			this.accionCargarInformacionOrdenesMedicas(con,mundo,forma,paciente);
		}

		//ORDENES DE MEDICAMENTOS 
		if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteOrdenesMedicamentos)))){
			this.accionCargarInformacionOrdenesMedicamentos(con,mundo,forma);
		}

		//ORDENES DE PROCEDIMIENTOS
		if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteOrdenesProcedimientos)))){
			this.accionCargarInformacionOrdenesProcedimientos(con,mundo,forma);
		}

		//ORDENES AMBULATORIAS
		if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteOrdenesAmbulatorias)))){
			this.accionCargarInformacionOrdenesAmbulatorias(con,mundo,forma);
		}

		//CONSULTAS DE PYP
		if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteConsultasPYP)))){
			this.accionCargarInformacionConsultasPYP(con,mundo,forma);
		}

		//ADMINISTRACION DE MEDICAMENTOS 
		if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteHojaAdministracionMedicamentos)))){
			cargarAdminMedicamentos(con, forma,  mundo);
		}


		//SECCION EVENTOS ADVERSOS
		if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteEventosAdversos)))){
			RegistroEventosAdversos rea = new RegistroEventosAdversos();
			rea.setIngreso(forma.getIdIngreso());
			forma.setEventosAdversos(rea.consultarDetalleXCuenta2(con, rea));				
		}			

		//RESUMEN PARCIAL HISTORIA CLINICA
		if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteResumenParcialHistoriaClinica))))
		{
			RegistroResumenParcialHistoriaClinica rphc=new RegistroResumenParcialHistoriaClinica();
			HashMap mapa=new HashMap();					
			mapa.put(IConstantesReporteHistoriaClinica.constanteIngreso,forma.getIdIngreso());
			if(!forma.getIdIngreso().equals(""))						
				forma.setResumenParcialHistoriaClinica(rphc.consultarNotas(con, mapa));										
		}		

		//VALORACIONES CUIDADO ESPECIAL
		if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteValoracionesCuidadoEspecial))))
		{
			ResumenAtenciones mundoResumen = new ResumenAtenciones();
			forma.setValoracionesCuidadoEspecial(
					mundoResumen.cargarValoracionesCuidadoEspecial(
							con, 
							forma.getIdIngreso(), 
							forma.getFechaInicial(), 
							forma.getHoraInicial(), 
							forma.getFechaFinal(), 
							forma.getHoraFinal()
					)
			);
		}

		//SE SETTEA LA INFORMACION DE LA INSTITUCION 
		forma.setInfoInstitucion(UtilidadesHistoriaClinica.obtenerInfoInstitucionXIngreso(con, forma.getIdIngreso()));


		//SECCION DE RESPUESTA A INTERCONSULTA
		if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteRespuestaInterpretacionInterconsulta))))
		{
			int[] codigosEstados={ConstantesBD.codigoEstadoHCRespondida,  ConstantesBD.codigoEstadoHCInterpretada};
			forma.setSolicitudesInterConsulta(mundo.obtenerSolicitudesEstadoTipoFiltro(con, codigosEstados, ConstantesBD.codigoTipoSolicitudInterconsulta,crearValueObjectFiltro(forma,ImpresionResumenAtencionesAction.codigoSeccionRespInterpretaInterconsultas)));
		}

		//SECCION DE EVOLUCIONES 
		if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteEvoluciones)))){
			forma.setCodigosEvoluciones(mundo.obtenerEvoluciones(con,crearValueObjectFiltro(forma,ImpresionResumenAtencionesAction.codigoSeccionEvolucionesMedicas)));
		}

		//SECCION DE SOPORTE RESPIRATORIO 
		if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteSoporteRespiratorio)))){
			forma.setSoporteRespiratorio(mundo.consultarSoporteRespiratorio(con,crearValueObjectFiltro(forma,ImpresionResumenAtencionesAction.codigoSeccionSoporteRespiratorio)));
		}

		//SE CONSULTA LA INFORMACION DEL ENCABEZADO DEL PACIENTE 
		forma.setEncabezadoImpresion(mundo.obtenerEncabezadoPaciente(con, forma.getIdIngreso()));

		/**CALCULAR EDAD*/
		int diaComp = 0;                                                                                                                   
		int mesComp = 0;
		int anioComp = 0;
		
		int  anioNac = 0;
		int mesNac = 0;
		int  diaNac= 0;
		
		diaComp = UtilidadFecha.getMesAnoDiaFecha("dia", paciente.getFechaNacimiento());                                                                                                                   
		mesComp = UtilidadFecha.getMesAnoDiaFecha("mes", paciente.getFechaNacimiento());
		anioComp = UtilidadFecha.getMesAnoDiaFecha("anio", paciente.getFechaNacimiento());
		
		boolean utilizarEdadActual=false;
		
		if(forma.getEncabezadoImpresion().get("fecha_muerte_0")!=null
				&&!forma.getEncabezadoImpresion().get("fecha_muerte_0").toString().trim().isEmpty()){
			String fechaMuerte=(String) forma.getEncabezadoImpresion().get("fecha_muerte_0");
			anioNac = UtilidadFecha.getMesAnoDiaFecha("anio", fechaMuerte);
			mesNac = UtilidadFecha.getMesAnoDiaFecha("mes", fechaMuerte);
			diaNac= UtilidadFecha.getMesAnoDiaFecha("dia", fechaMuerte);
		}else{
			if(forma.getEncabezadoImpresion().get("fecha_egreso_0")!=null
					&&!forma.getEncabezadoImpresion().get("fecha_egreso_0").toString().trim().isEmpty()){
				String fechaEgreso=(String) forma.getEncabezadoImpresion().get("fecha_egreso_0");
				anioNac = UtilidadFecha.getMesAnoDiaFecha("anio", fechaEgreso);
				mesNac = UtilidadFecha.getMesAnoDiaFecha("mes", fechaEgreso);
				diaNac= UtilidadFecha.getMesAnoDiaFecha("dia", fechaEgreso);
			}else{
				forma.getEncabezadoImpresion().put(IConstantesReporteHistoriaClinica.edad+"_0",forma.getEncabezadoImpresion().get("edad_anios_0"));
				utilizarEdadActual=true;
			}
		}
		
		if(!utilizarEdadActual){
			String edad = UtilidadFecha.calcularEdadDetalladaCompleta( anioComp , mesComp ,   diaComp,diaNac, mesNac,anioNac);
			String[]stringEdad=edad.split("y");
			int[] vectorEdad = UtilidadFecha.calcularVectorEdad(anioComp , mesComp ,   diaComp,diaNac, mesNac,anioNac);
			if(vectorEdad[0]>5){
				forma.getEncabezadoImpresion().put(IConstantesReporteHistoriaClinica.edad+"_0",edad.split("y")[0]);
			}else{
				if(vectorEdad[0]>0&&vectorEdad[0]<6){
					if(stringEdad.length>1){
						forma.getEncabezadoImpresion().put(IConstantesReporteHistoriaClinica.edad+"_0",stringEdad[0]+" y "+edad.split("y")[1]);
					}else{
						forma.getEncabezadoImpresion().put(IConstantesReporteHistoriaClinica.edad+"_0",edad.split("y")[0]);
					}
				}else{
					if(vectorEdad[1]>0&&vectorEdad[1]<13){
						if(stringEdad.length==1){
							forma.getEncabezadoImpresion().put(IConstantesReporteHistoriaClinica.edad+"_0",stringEdad[0]);
						}else{
							if(stringEdad.length==2){
								forma.getEncabezadoImpresion().put(IConstantesReporteHistoriaClinica.edad+"_0",stringEdad[0]+" y "+stringEdad[1]);
							}
						}
					}else{
						if(vectorEdad[2]<31){
							if(stringEdad.length==1){
								forma.getEncabezadoImpresion().put(IConstantesReporteHistoriaClinica.edad+"_0",stringEdad[0]);
							}else{
								if(stringEdad.length==2){
									forma.getEncabezadoImpresion().put(IConstantesReporteHistoriaClinica.edad+"_0",stringEdad[1]);
								}
							}
						}
					}
				}
			}
		}
		
		/****/
		
		//SECCION DE ADMINISTRACION DE MEDICAMENTOS
		if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteAdministracionMedicamentos)))){
			cargarAdminMedicamentos(con, forma,  mundo);
		}

		//SE OBTIENE CUALES SERAS LAS CUENTAS DE ASOCIO Y HOSPITALIZACION
		int codigoCuenta=Integer.parseInt(forma.getCuenta());

		int cuentaAsociada=UtilidadValidacion.tieneCuentaAsociada(con, Integer.parseInt(forma.getIdIngreso()));

		// SI EXISTE CUENTA DE ASOCIO  CUENDO HAY CUENTA DE ASOCIO LA CUENTA ACTIVA ES LA DE HOSPITALIZACI?N Y LA CUENTA DE URGENCIAS ES LA CUENTA DE ASOCIO 
		if(cuentaAsociada!=0)
		{
			if(forma.getFiltroAsocio().equals(IConstantesReporteHistoriaClinica.constanteValidarUrgencias))
			{
				codigoCuenta=cuentaAsociada;
			}
			else if(forma.getFiltroAsocio().equals(IConstantesReporteHistoriaClinica.constanteValidacionValoracionHospitalizacion))
			{
				cuentaAsociada=codigoCuenta;
			}
		}


		//SE INSTANCIA EL MUNDO DE REGISTRO ENFERMERIA
		RegistroEnfermeria mundoRegistroEnfer=new RegistroEnfermeria();


		//SE CONSULTA LA FECHA INICIAL Y LA FECHA FINAL 
		String fechaInicialBD="", fechaFinalBD="";
		if(UtilidadCadena.noEsVacio(forma.getFechaInicial())){
			fechaInicialBD=UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial());
		}

		if(UtilidadCadena.noEsVacio(forma.getFechaFinal())){
			fechaFinalBD=UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal());
		}


		//SECCION ANOTACIONES DE ENFERMER?A 
		if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteNotasEnfermeria)))){
			forma.setHistoricoAnotacionesEnfermeria(mundoRegistroEnfer.consultarAnotacionesEnfermeriaImpresionHC(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, forma.getIdIngreso()),fechaInicialBD, fechaFinalBD, forma.getHoraInicial(), forma.getHoraFinal(), forma.getFiltroAsocio().trim()));
		}

		//SECCION SIGNOS VITALES 
		if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteSignosVitales)))){
			accionCargarSeccionSignosVitales(con, mundoRegistroEnfer, forma, usuario.getCodigoInstitucionInt(), paciente.getCodigoArea(), UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, forma.getIdIngreso()+""), fechaInicialBD, fechaFinalBD, forma.getHoraInicial(), forma.getHoraFinal(), forma.getFiltroAsocio());
		}

		//SECCION CATETERES SONDA 
		if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteCateteresSondas)))){
			accionCargarSeccionCateterSonda(con, mundoRegistroEnfer, forma, usuario.getCodigoInstitucionInt(), paciente.getCodigoArea(), UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, forma.getIdIngreso()+""), fechaInicialBD, fechaFinalBD, forma.getHoraInicial(), forma.getHoraFinal(), forma.getFiltroAsocio());
		}

		//SECCION CUIDADOS ESPECIALES DE ENFERMER?A
		if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteCuidadosEspeciales)))){
			accionCargarSeccionCuidadosEspeciales(con, mundoRegistroEnfer, forma, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, forma.getIdIngreso()+""), fechaInicialBD, fechaFinalBD, forma.getHoraInicial(), forma.getHoraFinal(), forma.getFiltroAsocio());
		}

		//SECCION DE RESULTADO DE LABORATORIOS
		if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteResultadosLaboratorios)))){
			forma.setResultadoLaboratorios(mundo.consultarResultatadosLaboratorios(con,forma.getIdIngreso(),fechaInicialBD, fechaFinalBD, forma.getHoraInicial(), forma.getHoraFinal()));
		}

		//SECCION DE VALORACION DE ENFERMERIA 
		if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteValoracionesEnfermeria)))){
			forma.setValoracionesEnfermeria(mundo.consultarrValoracionesEnfermeria(con,forma.getIdIngreso(),fechaInicialBD, fechaFinalBD, forma.getHoraInicial(), forma.getHoraFinal()));
		}

		//SECCION HOJA NEUROLOGICA 
		if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteHojaNeurologica)))){
			accionCargarSeccionHojaNeurologica(con, mundoRegistroEnfer, forma, usuario.getCodigoInstitucionInt(), paciente.getCodigoArea(), UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, forma.getIdIngreso()+""), fechaInicialBD, fechaFinalBD, forma.getHoraInicial(), forma.getHoraFinal(), forma.getFiltroAsocio());
		}

		//SECCION CIRUG?AS
		if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteCirugias))) 
				|| UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteHojaQuirurgica))) 
				|| UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteHojaAnestesia)))){
			accionCargarSeccionCirugias(con, mundo, forma, usuario.getCodigoInstitucionInt(), paciente.getCodigoArea(), UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, forma.getIdIngreso()+""), fechaInicialBD, fechaFinalBD, forma.getHoraInicial(), forma.getHoraFinal(), forma.getFiltroAsocio(),ConstantesBD.codigoNuncaValido);
		}

		//SECCION CONTROL LIQUIDOS
		if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteControLiquidos))))
		{
			//PARA SABER SI EXISTE INFORMACION DE CONTROL DE LIQUIDOS Y MEDICAMENTOS
			cargarControlLiquidos(con, request, 0, paciente, usuario, forma, codigoCuenta, cuentaAsociada);

			//-- PARA TRAER TODA LA INFORMACION DE CONTROL DE LIQUIDOS Y MEDICAMENTOS DADOS LOS PARAMETROS DE BUSQUEDA.
			cargarControlLiquidos(con, request, 1, paciente, usuario, forma, codigoCuenta, cuentaAsociada);					
		}

		if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteAntecedentes))))
		{

			//MT 4431: Se debe tomar la fecha de egreso de la tabla ingresos.
			/*
			 * Para evitar ir a la BD consulto la informacion de las Cuentas
			 */
			ManejoPacienteFacade manejoPacienteFacade = new ManejoPacienteFacade();
			forma.setIngresoSelecccionado(manejoPacienteFacade.obtenerInfoIngreso(Integer.parseInt(forma.getIdIngreso())));
			//Fin MT 4431				
			
			
			//PARA SABER SI EXISTE INFORMACION DE CONTROL DE LIQUIDOS Y MEDICAMENTOS 
			cargarAntecedentesAlergias(con, 0, paciente, usuario, forma);

			//PARA TRAER TODA LA INFORMACION DE CONTROL DE LIQUIDOS Y MEDICAMENTOS DADOS LOS PARAMETROS DE BUSQUEDA.
			cargarAntecedentesAlergias(con, 1, paciente, usuario, forma);

			//PARA SABER SI HAY REGISTRADA INFORMACION
			cargarAntecedentesFamiliares(con, 0, paciente, usuario, forma); 
			//TRAER LA INFORMACION.
			cargarAntecedentesFamiliares(con, 1, paciente, usuario, forma); 

			//PARA SABER SI HAY REGISTRADA INFORMACION
			cargarAntecedentesFamiliaresOculares(con, 0, paciente, usuario, forma); 

			//TRAER LA INFORMACION.
			cargarAntecedentesFamiliaresOculares(con, 1, paciente, usuario, forma); 

			//PARA SABER SI HAY REGISTRADA INFORMACION 
			cargarAntecedentesFamiliares(con,  0, paciente, usuario, forma);  

			//TRAER LA INFORMACION.
			cargarAntecedentesFamiliares(con, 1, paciente, usuario, forma); 

			//PARA SABER SI HAY REGISTRADA INFORMACION
			cargarAntecedentesFamiliaresOculares(con, 0, paciente, usuario, forma); 

			//TRAER LA INFORMACION.
			cargarAntecedentesFamiliaresOculares(con, 1, paciente, usuario, forma); 

			//PARA SABER SI HAY REGISTRADA INFORMACION
			cargarAntecedentesPersonalesOculares(con, 0, paciente, usuario, forma); 

			//TRAER LA INFORMACION.
			cargarAntecedentesPersonalesOculares(con, 1, paciente, usuario, forma); 

			//ANTECEDENTES GINECOBSTETRICOS SI EXISTEN 
			cargarAntecedentesGinecoObstetricos(con,request,  0, paciente, usuario, forma);

			//ANTECEDENTES GINECOBSTETRICOS CON LA INFORMACION 
			cargarAntecedentesGinecoObstetricos(con,request,  1, paciente, usuario, forma);

			//PARA SABER SI HAY REGISTRADA INFORMACION
			cargarAntecedentesMedicamentos(con, 0, paciente, usuario, forma); 

			//TRAER LA INFORMACION.
			cargarAntecedentesMedicamentos(con, 1, paciente, usuario, forma); 

			//SECCION DE ANTECEDENTES , PRIMERO SE CONSULTA SI EXISTEN DATOS Y DESPUES SE CONSULTAN LOS DATOS 
			cargarAntecedentesMedicos(con, 0, paciente, usuario, forma); 
			cargarAntecedentesMedicos(con, 1, paciente, usuario, forma);

			//SECCION DE ANTECEDENTES TOXICOS , PRIMERO SE CONSULTA SI EXISTEN DATOS Y DESPUES SE CONSULTAN LOS DATOS 
			cargarAntecedentesToxicos(con, 0, paciente, usuario, forma);
			cargarAntecedentesToxicos(con, 1, paciente, usuario, forma);

			//SECCION DE ANTECEDENTES TRANSFUSIONALES , PRIMERO SE CONSULTA SI EXISTEN DATOS Y DESPUES SE CONSULTAN LOS DATOS 
			cargarAntecedentesTransfusionales(con, 0, paciente, usuario, forma);				
			cargarAntecedentesTransfusionales(con, 1, paciente, usuario, forma);				

			//SECCION DE ANTECEDENTES VACUNAS , PRIMERO SE CONSULTA SI EXISTEN DATOS Y DESPUES SE CONSULTAN LOS DATOS 
			cargarAntecedentesVacunas(con, 0, paciente, usuario, forma);				
			cargarAntecedentesVacunas(con, 1, paciente, usuario, forma);				

			//SECCION DE ANTECEDENTES VARIOS , PRIMERO SE CONSULTA SI EXISTEN DATOS Y DESPUES SE CONSULTAN LOS DATOS 
			cargarAntecedentesVarios(con, 0, paciente, usuario, forma);
			cargarAntecedentesVarios(con, 1, paciente, usuario, forma);

			//SECCION DE ANTECEDENTES PEDIATRICOS , PRIMERO SE CONSULTA SI EXISTEN DATOS Y DESPUES SE CONSULTAN LOS DATOS 
			cargarAntecedentesPediatricos(con, 0, paciente, usuario, forma);
			cargarAntecedentesPediatricos(con, 1, paciente, usuario, forma);

			//SECCION DE ANTECEDENTES ODONTOLOGICOS , PRIMERO SE CONSULTA SI EXISTEN DATOS Y DESPUES SE CONSULTAN LOS DATOS 
			cargarAntecedentesOdontologicos(con, 0, paciente, usuario, forma);
			cargarAntecedentesOdontologicos(con, 1, paciente, usuario, forma);  
		}

		//SE FINALZIA LACONEXION A BD
		UtilidadBD.closeConnection(con);


		logger.info("Salio del metodo reporteSoloJsp");

		try
		{
			/*
			 * Se envia la respuesta ajax a la generacion del reporte
			 * para que abra el popup con la pagina JSP 
			 */
			response.setCharacterEncoding("UTF-8");
            response.setContentType("text/plain");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(IConstantesReporteHistoriaClinica.constanteSlash+IConstantesReporteHistoriaClinica.PAGINA_JSP_HISTORIA_CLINICA);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroContrato: "+e);
		}

		return null;
	}



	/**
	 * 
	 * @param resultado
	 * @param filtro 
	 * @param encabezadoPaciente
	 * @param usuario
	 * @param paciente
	 * @param institucionActual
	 * @return
	 */
	private JasperReportBuilder generarPdf(
			DtoImpresionHistoriaClinica dto,
			DtoFiltroImpresionHistoriaClinica filtro, HashMap encabezadoPaciente, UsuarioBasico usuario,
			PersonaBasica paciente, InstitucionBasica institucionActual,String idIngreso,Boolean imprimirSoloNotasAclaratorias,Boolean primerIngreso,Boolean varios) {

		logger.info("Entro del metodo generarPdf");

		//REPORTE GLOBAL 
		JasperReportBuilder reporteExportar=null;


		Map<String , String> parametros = new HashMap<String, String>();

		//INSTANCIA DE CLASE QUE ARMA EL REPORTE DE HC
		GeneradorReporteHistoriaClinica reporte = new GeneradorReporteHistoriaClinica();
		try {

			//SE CONSULTA Y GENERA EL REPORTE DE HC 
			reporteExportar= 	reporte.buildReportFormatoHc(parametros, dto,filtro,encabezadoPaciente,usuario,paciente,institucionActual,idIngreso,imprimirSoloNotasAclaratorias,primerIngreso,varios);

		}//CONTROL DE ERRORES  
		catch (Exception e) {
			logger.error("Error generando Reporte:"+e.getMessage());
		}

		logger.info("Salio del metodo generarPdf");

		//SE RETORNA EL REPORTE GENERADO
		return reporteExportar;
	}



	/**
	 * Cargar la informacion de consultas PYP
	 * @param con
	 * @param mundo
	 * @param forma
	 */
	private void accionCargarInformacionConsultasPYP(Connection con,
			ImpresionResumenAtenciones mundo,
			ImpresionResumenAtencionesForm forma) {
		forma.setMapaConsultasPYP(mundo.obtenerConsultasPYP(con,crearValueObjectFiltro(forma,ImpresionResumenAtencionesAction.codigoSeccionConsultasPYP)));
	}





	/**
	 * Cargar Ordenes Ambulatorias
	 * @param con
	 * @param mundo
	 * @param forma
	 */
	private void accionCargarInformacionOrdenesAmbulatorias(Connection con, ImpresionResumenAtenciones mundo, ImpresionResumenAtencionesForm forma) {
		forma.setOrdenesAmbulatorias(mundo.consultarOrdenesAmbulatorias(con,crearValueObjectFiltro(forma,ImpresionResumenAtencionesAction.codigoSeccionOrdenesAmbulatorias)));
	}


	/**
	 * Método implementado para consultar las adminitraciones de medicamentos
	 * @param con
	 * @param forma
	 * @param mundo
	 */
	private void cargarAdminMedicamentos(Connection con, ImpresionResumenAtencionesForm forma, ImpresionResumenAtenciones mundo) 
	{
		HashMap vo = crearValueObjectFiltro(forma,ImpresionResumenAtencionesAction.codigoSeccionAdminMedicamentos);
		HashMap mapaMedicamentosGenericos = new HashMap();

		//Se consultan los medicamentos de la administracion
		HashMap mapaAdmin = mundo.consultarAdminMedicamentos(con,vo,ConstantesBD.codigoNuncaValido);

		//Se consulta el detalle de adminsitracion de cada articulo
		for(int i=0;i<Integer.parseInt(mapaAdmin.get("numRegistros").toString());i++)
		{
			vo.put("articulo",mapaAdmin.get("articulo_"+i));
			HashMap mapaDetalle = mundo.consultarDetalleArticuloAdmin(con, vo);

			Integer numDetalle = Utilidades.convertirAEntero(mapaDetalle.get("numRegistros").toString());
			for (int j = 0; j < numDetalle; j++) {
				if(!UtilidadTexto.isEmpty(String.valueOf(mapaDetalle.get("codigo_articulo_principal_"+j)))){
					mapaMedicamentosGenericos= mundo.consultarAdminMedicamentos(con,vo,Integer.valueOf(String.valueOf(mapaDetalle.get("codigo_articulo_principal_"+j))));
					mapaDetalle.put("detalle_medicamento_principal_"+j, mapaMedicamentosGenericos);
				}else{
					mapaDetalle.put("detalle_medicamento_principal_"+j, ConstantesBD.codigoNuncaValido);
				}
			}
			mapaAdmin.put("detalle_"+i, mapaDetalle);
		}

		//Se asigna el mapa
		forma.setAdminMedicamentos(mapaAdmin);

	}



	/**
	 * Método que valida si una seccion fue seleccionada o no con varios ingresos 
	 * @param forma
	 */
	private void mostrarSeccionesVariosIngresos(ImpresionResumenAtencionesForm forma){
		logger.info("Entro al metodo mostrarSeccionesVariosIngresos");

		String []listaSecciones = forma.getAtenciones().split(",");
		forma.setSecciones("valoracionInicial",validacionSeccion(listaSecciones, "0"));
		forma.setSecciones("valoracionesCE",validacionSeccion(listaSecciones, "0") );
		forma.setSecciones("consultasPYP",validacionSeccion(listaSecciones, "1") );
		forma.setSecciones("antecedentes",validacionSeccion(listaSecciones, "2") );
		forma.setSecciones("respuestaInterpretacionInterconsulta",validacionSeccion(listaSecciones, "3") );
		forma.setSecciones("evoluciones",validacionSeccion(listaSecciones, "4") );
		forma.setSecciones("cirugias",validacionSeccion(listaSecciones, "5") );
		forma.setSecciones("ordenesMedicas",validacionSeccion(listaSecciones, "6") );
		forma.setSecciones("valoracionesEnfermeria",validacionSeccion(listaSecciones, "7") );
		forma.setSecciones("signosVitales",validacionSeccion(listaSecciones, "8") );
		forma.setSecciones("soporteRespiratorio",validacionSeccion(listaSecciones, "9") );
		forma.setSecciones("controLiquidos",validacionSeccion(listaSecciones, "10") );
		forma.setSecciones("cateteresSondas",validacionSeccion(listaSecciones, "11") );
		forma.setSecciones("cuidadosEspeciales",validacionSeccion(listaSecciones, "12") );
		forma.setSecciones("hojaNeurologica",validacionSeccion(listaSecciones, "13") );
		forma.setSecciones("notasEnfermeria",validacionSeccion(listaSecciones, "14") );
		forma.setSecciones("notasGeneralesCirugia",validacionSeccion(listaSecciones, "15") );
		forma.setSecciones("notasRecuperacion",validacionSeccion(listaSecciones, "16") );
		forma.setSecciones("administracionMedicamentos",validacionSeccion(listaSecciones, "17") );
		forma.setSecciones("consumoInsumos",validacionSeccion(listaSecciones, "18") );
		forma.setSecciones("respuestaInterpretacionProcedimientos",validacionSeccion(listaSecciones, "19") );
		forma.setSecciones("resumenParcialHistoriaClinica",validacionSeccion(listaSecciones, "20") );
		forma.setSecciones("ordenesAmbulatorias",validacionSeccion(listaSecciones, "21") );
		forma.setSecciones("notasAclaratorias",validacionSeccion(listaSecciones, "22") );

		logger.info("Salio al metodo mostrarSeccionesVariosIngresos");
	}

	/**
	 * Metodo que obtiene el valor de si una seccion esta o no 
	 * @param listaSecciones
	 * @param valorPivot
	 * @return Boolean 
	 */
	public Boolean validacionSeccion(String[] listaSecciones,String valorPivot){
		logger.info("Entro al metodo validacionSeccion" );

		//VARIABLE A RETORNAR
		Boolean res = false;

		//VALIDA SI UNA SECCION EESTA PARA SER IMPRESA
		for (int i = 0; i < listaSecciones.length; i++) {
			if(listaSecciones[i].equals(valorPivot)){
				return true;
			}
		}


		logger.info("Salio al metodo validacionSeccion" );

		//RETORNO SOLICITADO
		return res;
	}







	/**
	 * Metodo que valida las secciones que van a ser impresas con un solo ingreso 
	 * @param forma
	 */
	private void mostrarSecciones(ImpresionResumenAtencionesForm forma,Integer estadoFuncionalidad)
	{
		logger.info("Entro al metodo mostrarSecciones");

		forma.setSecciones("epicrisis", mostrarSeccion(forma,ImpresionResumenAtencionesAction.codigoSeccionEpicrisis)+"");
		forma.setSecciones("antecedentes", mostrarSeccion(forma,ImpresionResumenAtencionesAction.codigoSeccionAntecedentes)+"");

		//CUANDO SE SELECCIONA VALRACION TAMBIEN SE DEBEN MRIMIR LAS DE COSNULTA EXTERNA 
		if(estadoFuncionalidad.equals(IConstantesReporteHistoriaClinica.estadoParametroTipoHistoriaClinica)){

			if(mostrarSeccion(forma,ImpresionResumenAtencionesAction.codigoSeccionValoracionInicial)){
				forma.setSecciones("valoracionInicial", mostrarSeccion(forma,ImpresionResumenAtencionesAction.codigoSeccionValoracionInicial)+"");
				forma.setSecciones("valoracionesCE", new Boolean(true));
			}else{
				forma.setSecciones("valoracionInicial", mostrarSeccion(forma,ImpresionResumenAtencionesAction.codigoSeccionValoracionInicial)+"");
				forma.setSecciones("valoracionesCE", new Boolean(false));
			}
		}else{
			//EN CASO DE IMPRIMIR LA VERSION ANTIGUA DEL REPORTE
			forma.setSecciones("valoracionInicial", mostrarSeccion(forma,ImpresionResumenAtencionesAction.codigoSeccionValoracionInicial)+"");
			forma.setSecciones("valoracionesCE",mostrarSeccion(forma,ImpresionResumenAtencionesAction.codigoSeccionValoracionesCE)+"");
		}
		forma.setSecciones("evoluciones", mostrarSeccion(forma,ImpresionResumenAtencionesAction.codigoSeccionEvolucionesMedicas)+"");
		forma.setSecciones("ordenesMedicas", mostrarSeccion(forma,ImpresionResumenAtencionesAction.codigoSeccionOrdenesMedicas)+"");
		forma.setSecciones("signosVitales", mostrarSeccion(forma,ImpresionResumenAtencionesAction.codigoSeccionSignosVitales)+"");
		forma.setSecciones("soporteRespiratorio", mostrarSeccion(forma,ImpresionResumenAtencionesAction.codigoSeccionSoporteRespiratorio)+"");
		forma.setSecciones("controLiquidos", mostrarSeccion(forma,ImpresionResumenAtencionesAction.codigoSeccionControlLiquidos)+"");
		forma.setSecciones("cateteresSondas", mostrarSeccion(forma,ImpresionResumenAtencionesAction.codigoSeccionCateteresSondas)+"");
		forma.setSecciones("cuidadosEspeciales", mostrarSeccion(forma,ImpresionResumenAtencionesAction.codigoSeccionCuidadosEspeciales)+"");
		forma.setSecciones("resultadosLaboratorios", mostrarSeccion(forma,ImpresionResumenAtencionesAction.codigoSeccionResultadosLaboratorios)+"");
		forma.setSecciones("valoracionesEnfermeria", mostrarSeccion(forma,ImpresionResumenAtencionesAction.codigoSeccionValoracionEnfermeria)+"");
		forma.setSecciones("notasEnfermeria", mostrarSeccion(forma,ImpresionResumenAtencionesAction.codigoSeccionNotasEnfermeria)+"");
		forma.setSecciones("hojaNeurologica", mostrarSeccion(forma,ImpresionResumenAtencionesAction.codigoSeccionHojaNeurologica)+"");
		forma.setSecciones("administracionMedicamentos", mostrarSeccion(forma,ImpresionResumenAtencionesAction.codigoSeccionAdminMedicamentos)+"");
		forma.setSecciones("consumoInsumos", mostrarSeccion(forma,ImpresionResumenAtencionesAction.codigoSeccionConsumoInsumos)+"");
		forma.setSecciones("cirugias", mostrarSeccion(forma,ImpresionResumenAtencionesAction.codigoSeccionCirugias)+"");
		forma.setSecciones("respuestaInterpretacionInterconsulta", mostrarSeccion(forma,ImpresionResumenAtencionesAction.codigoSeccionRespInterpretaInterconsultas)+"");
		forma.setSecciones("respuestaInterpretacionProcedimientos", mostrarSeccion(forma,ImpresionResumenAtencionesAction.codigoSeccionRespInterprataProcedimientos)+"");
		forma.setSecciones("eventosAdversos", mostrarSeccion(forma, ImpresionResumenAtencionesAction.codigoSeccionEventosAdversos)+"");
		forma.setSecciones("resumenParcialHistoriaClinica", mostrarSeccion(forma, ImpresionResumenAtencionesAction.codigoSeccionResumenParcialHistoriaClinica)+"");
		forma.setSecciones("valoracionesCuidadoEspecial", mostrarSeccion(forma, ImpresionResumenAtencionesAction.codigoSeccionValoracionesCuidadoEspecial)+"");
		forma.setSecciones("cargosDirectos", mostrarSeccion(forma, ImpresionResumenAtencionesAction.codigoSeccionCargosDirectos)+"");		
		forma.setSecciones("ordenesMedicamentos", mostrarSeccion(forma, ImpresionResumenAtencionesAction.codigoSubseccionOrdenesMedicamentos)+"");
		forma.setSecciones("ordenesProcedimientos", mostrarSeccion(forma, ImpresionResumenAtencionesAction.codigoSubseccionOrdenesProcedimientos)+"");
		forma.setSecciones("hojaQuirurgica", mostrarSeccion(forma, ImpresionResumenAtencionesAction.codigoSubseccionHojaQuirurgica)+"");
		forma.setSecciones("hojaAdministracionMedicamentos", mostrarSeccion(forma, ImpresionResumenAtencionesAction.codigoSubseccionHojaAdministracionMedicamentos)+"");
		forma.setSecciones("hojaAnestesia", mostrarSeccion(forma, ImpresionResumenAtencionesAction.codigoSubseccionHojaAnestesia)+"");
		forma.setSecciones("ordenesAmbulatorias", mostrarSeccion(forma, ImpresionResumenAtencionesAction.codigoSeccionOrdenesAmbulatorias)+"");
		forma.setSecciones("consultasPYP", mostrarSeccion(forma, ImpresionResumenAtencionesAction.codigoSeccionConsultasPYP)+"");
		forma.setSecciones("notasGeneralesCirugia", mostrarSeccion(forma, ImpresionResumenAtencionesAction.codigoSeccionNotasGeneralesCirugia)+"");
		forma.setSecciones("notasRecuperacion", mostrarSeccion(forma, ImpresionResumenAtencionesAction.codigoSeccionNotasRecuperacion)+"");
		forma.setSecciones("notasAclaratorias", mostrarSeccion(forma, ImpresionResumenAtencionesAction.codigoSeccionNotasAclaratorias)+"");
		logger.info("Salio al metodo mostrarSecciones");
	}


	/**
	 * 
	 * @param forma
	 * @param codigoSeccion
	 * @return
	 */
	private boolean mostrarSeccion(ImpresionResumenAtencionesForm forma, int codigoSeccion)
	{
		String[] aten=forma.getAtenciones().split(",");
		for(int i=0;i<aten.length;i++)
		{
			if(aten[i].equals(codigoSeccion+""))
			{
				return true;
			}
			if(aten[i].equals(codigoSeccion+"A"))
			{
				return true;
			}
		}
		return false;
	}


	/**
	 * 
	 */
	private HashMap crearValueObjectFiltro(ImpresionResumenAtencionesForm forma,int codigoSeccion)
	{
		HashMap vo=new HashMap();
		vo.put("ingreso",forma.getIdIngreso());
		vo.put("cuenta","");
		if(!forma.getFiltroAsocio().trim().equals("")&&!forma.getFiltroAsocio().trim().equals("A"))
			vo.put("cuenta",forma.getCuenta());
		vo.put("fechaInicial",forma.getFechaInicial());
		vo.put("fechaFinal",forma.getFechaFinal());
		vo.put("horaInicial",forma.getHoraInicial());
		vo.put("horaFinal", forma.getHoraFinal());
		vo.put("solicitudes", forma.getSolicitudesFactura());
		
		/**
		 * MT 8472
		 * @author javrammo
		 * Sin importar si el ingreso tiene asocio, no se debe filtar por cuenta
		 * debe traer toda la info del ingreso, para su impresion.
		 * Ademas no se contempla cuando se tienen dos asocios.
		 */
		
		/*Connection con=UtilidadBD.abrirConexion();
		int cuentaAsociada=UtilidadValidacion.tieneCuentaAsociada(con, Integer.parseInt(forma.getIdIngreso()));
		if(cuentaAsociada<=0)
		{
			vo.put("cuentaAsocio","");
		}
		if(cuentaAsociada>0&&forma.getFiltroAsocio().trim().equals("A")){
			vo.put("cuentaAsocio",cuentaAsociada);
			String[] aten=forma.getAtenciones().split(",");
			boolean cuenta1=false,cuenta2=false;
			for(int i=0;i<aten.length;i++)
			{
				if(aten[i].equals(codigoSeccion+""))
				{
					cuenta1=true;
				}
				if(aten[i].equals(codigoSeccion+"A"))
				{
					cuenta2=true;
				}
			}
			if(cuenta1&&!cuenta2)
			{
				vo.put("cuenta",forma.getCuenta());
			}
			else if(!cuenta1&&cuenta2)
			{
				vo.put("cuenta",cuentaAsociada);
			}
		}
		UtilidadBD.closeConnection(con);*/
		return vo;
	}

	/**
	 * 
	 * @param con
	 * @param mundo
	 * @param forma
	 */
	private void accionCargarInformacionOrdenesMedicamentos(Connection con,ImpresionResumenAtenciones mundo,ImpresionResumenAtencionesForm forma)
	{
		//cargar solicitudes medicamentos insumos.
		forma.setOrdenesMedicamentos(mundo.consultarSolicitudesMedicamentoInsumos(con,crearValueObjectFiltro(forma,ImpresionResumenAtencionesAction.codigoSeccionOrdenesMedicas)));
	}

	/**
	 * 
	 * @param con
	 * @param mundo
	 * @param forma
	 */
	private void accionCargarInformacionOrdenesProcedimientos(Connection con,ImpresionResumenAtenciones mundo,ImpresionResumenAtencionesForm forma)
	{
		//cargar solicitudes procedimientos
		forma.setOrdenesProcedimientos(mundo.consultarSolicitudesProcedimientos(con,crearValueObjectFiltro(forma,ImpresionResumenAtencionesAction.codigoSeccionOrdenesMedicas)));
	}

	/**
	 * 
	 * @param con
	 * @param mundo
	 * @param forma
	 */
	private void accionCargarInformacionHojaAdministracionMedicamentos(Connection con,ImpresionResumenAtenciones mundo,ImpresionResumenAtencionesForm forma)
	{
		//cargar solicitudes procedimientos
		forma.setAdminMedicamentos(mundo.consultarAdminMedicamentos(con,crearValueObjectFiltro(forma,ImpresionResumenAtencionesAction.codigoSeccionOrdenesMedicas),ConstantesBD.codigoNuncaValido));
	}


	/**
	 * 
	 * @param con
	 * @param mundo
	 * @param forma
	 */
	private void accionCargarInformacionOrdenesMedicas(Connection con,ImpresionResumenAtenciones mundo,ImpresionResumenAtencionesForm forma,PersonaBasica paciente)
	{


		//cargar la informacion de la orden medica
		forma.setOrdenesMedicas(mundo.consultarOrdenMedica(con,crearValueObjectFiltro(forma,ImpresionResumenAtencionesAction.codigoSeccionOrdenesMedicas),paciente.getCodigoIngreso()));

		//cargar cirugias
		forma.setOrdenesCirugia(mundo.consultarOrdenesCirugias(con,crearValueObjectFiltro(forma,ImpresionResumenAtencionesAction.codigoSeccionOrdenesMedicas)));

		//cargar solicitudes medicamentos insumos.
		forma.setOrdenesMedicamentos(mundo.consultarSolicitudesMedicamentoInsumos(con,crearValueObjectFiltro(forma,ImpresionResumenAtencionesAction.codigoSeccionOrdenesMedicas)));

		//cargar solicitudes procedimientos
		forma.setOrdenesProcedimientos(mundo.consultarSolicitudesProcedimientos(con,crearValueObjectFiltro(forma,ImpresionResumenAtencionesAction.codigoSeccionOrdenesMedicas)));

		//cargar solicitudes interconsulta
		forma.setOrdenesInterconsulta(mundo.consultarSolicitudesInterconsultas(con,crearValueObjectFiltro(forma,ImpresionResumenAtencionesAction.codigoSeccionOrdenesMedicas)));

	}


	/********FIN METODOS ARMANDO*************************************/

	/****METODOS WILSON **********************************************/

	/**
	 * 
	 */
	private HashMap getCriteriosBusqueda(UsuarioBasico usuario, ImpresionResumenAtencionesForm forma)
	{
		HashMap criteriosBusqueda= new HashMap();
		criteriosBusqueda.put("idIngreso", forma.getIdIngreso());
		criteriosBusqueda.put("codigoInstitucion", usuario.getCodigoInstitucion());
		criteriosBusqueda.put("codigoCentroAtencion", usuario.getCodigoCentroAtencion()+"");
		criteriosBusqueda.put("codigoPersona", usuario.getCodigoPersona()+"");
		return criteriosBusqueda;
	}

	/********FIN METODOS WILSON*************************************/


	/**
	 * Método que consulta la información de la sección signos vitales para la impresión
	 * @param con
	 * @param mundoRegEnfer
	 * @param forma
	 * @param institucion
	 * @param centroCosto
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion
	 */
	private void accionCargarSeccionSignosVitales(Connection con, RegistroEnfermeria mundoRegEnfer,ImpresionResumenAtencionesForm forma, int institucion, int centroCosto, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion) 
	{
		//-----------Se cargan los tipos de signos vitales parametrizados por instituciï¿½n centro de costo-----------//
		forma.setSignosVitalesInstitucionCcosto(mundoRegEnfer.consultarTiposInstitucionCCosto(con, institucion, cuentas, 1));

		//----Se quitan los tipos de signos vitales repetidos en la colección------//
		if(forma.getSignosVitalesInstitucionCcosto()!=null && forma.getSignosVitalesInstitucionCcosto().size()>0)
			forma.setSignosVitalesInstitucionCcosto(Utilidades.coleccionSinRegistrosRepetidos(forma.getSignosVitalesInstitucionCcosto(), "codigo_tipo"));

		//------------- Consulta el historico de los signos vitales fijos--------------------------//
		forma.setSignosVitalesFijosHisto(mundoRegEnfer.consultarSignosVitalesFijosHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion));

		//------------- Consulta el historico de los signos vitales pamatrizados por institucion centro costo--------------------------//
		if(forma.getSignosVitalesInstitucionCcosto()!=null && forma.getSignosVitalesInstitucionCcosto().size()>0)
			forma.setSignosVitalesParamHisto(mundoRegEnfer.consultarSignosVitalesParamHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion));

		//------------- Consulta los codigo históricos, fecha registro y hora registro de los signos vitales fijos y parametrizados--------------------------//
		if(  ( forma.getSignosVitalesFijosHisto()!=null &&  forma.getSignosVitalesFijosHisto().size()>0) || 
				(forma.getSignosVitalesParamHisto()!=null && forma.getSignosVitalesParamHisto().size()>0))
			forma.setSignosVitalesHistoTodos(mundoRegEnfer.consultarSignosVitalesHistoTodosImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion));
	}

	/**
	 * Método que consulta la información de la sección cateter sonda para la impresión
	 * @param con
	 * @param mundoRegEnfer
	 * @param forma
	 * @param institucion
	 * @param centroCosto
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion
	 */
	private void accionCargarSeccionCateterSonda(Connection con, RegistroEnfermeria mundoRegEnfer,ImpresionResumenAtencionesForm forma, int institucion, int centroCosto, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion) 
	{
		//-----------Se cargan las columnas de cateteres y sondas parametrizadas por institucion centro de costo-----------//
		forma.setColCateteresSondaInstitucionCcosto(mundoRegEnfer.consultarTiposInstitucionCCosto(con, institucion, cuentas, 3));

		//----Se quitan las columnas de cateter sonda repetidas en la coleccion------//
		forma.setColCateteresSondaInstitucionCcosto(Utilidades.coleccionSinRegistrosRepetidos(forma.getColCateteresSondaInstitucionCcosto(), "codigo_tipo"));

		//---------------- Consulta del historico de los cateter sonda fijos --------------------// 
		forma.setCateterSondaFijosHisto(mundoRegEnfer.consultarCateterSondaFijosHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion));

		//---------------- Consulta del historico de los cateter sonda parametrizados --------------------//
		if(forma.getColCateteresSondaInstitucionCcosto().size()>0)
			forma.setCateterSondaParamHisto(mundoRegEnfer.consultarCateterSondaParamHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion));

		//---------------- Consulta del historico de los cateter sonda parametrizados y fijos --------------------//
		if(forma.getCateterSondaFijosHisto().size()>0 || forma.getCateterSondaParamHisto().size()>0)
			forma.setCateterSondaHistoTodos(mundoRegEnfer.consultarCateterSondaTodosHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion));

		HashMap mapaCateterHisto=new HashMap();
		mapaCateterHisto=formarHistoricoCateterSonda(forma);

		//----------Se verifica si hay registros en el histórico de cateter sonda para ordenarlo----------//
		if(Integer.parseInt(mapaCateterHisto.get("numRegistros")+"")>0)
		{
			//-------Se ordena el histórico por la fecha de inserción del cateter sonda -----------// 	
			ordenarHistoricoCateterSonda(forma, mapaCateterHisto);
		}
		else
		{
			forma.setMapaHistoricoCateterSonda(mapaCateterHisto);
		}
	}

	/**
	 * Mï¿½todo que ordena el mapa que contiene el listado de cateter sonda
	 * @param forma
	 * @param mapa
	 * @return
	 */

	private boolean ordenarHistoricoCateterSonda (ImpresionResumenAtencionesForm forma, HashMap mapa)
	{
		int indicesFijos=15;
		int numIndices=forma.getColCateteresSondaInstitucionCcosto().size()+indicesFijos;

		//------- Se construye el vector de strings con los indices del mapa para poder ordenarlo ----//
		String[] indices=new String[numIndices];

		indices[0]="cateterSondaRegEnfer_";
		indices[1]="nombreArticulo_";
		indices[2]="codigoArticuloCcIns_";
		indices[3]="viaInsercion_";
		indices[4]="fechaInsercion_";
		indices[5]="horaInsercion_";
		indices[6]="fechaRetiro_";
		indices[7]="horaRetiro_";
		indices[8]="curaciones_";
		indices[9]="observaciones_";
		indices[10]="fecha_registro_";
		indices[11]="hora_registro_";
		indices[12]="nombre_usuario_";

		if(forma.getColCateteresSondaInstitucionCcosto().size() > 0)
		{
			Iterator iterador1=forma.getColCateteresSondaInstitucionCcosto().iterator();
			for (int numFila=0; numFila<forma.getColCateteresSondaInstitucionCcosto().size(); numFila++)
			{
				HashMap fila1=(HashMap)iterador1.next();
				indices[indicesFijos]="valorParam_"+fila1.get("codigo_tipo")+"_";
				indicesFijos++;
			}
		}

		//-----Numero de registros del mapa------------//
		int num = forma.getCateterSondaHistoTodos().size();


		forma.setMapaHistoricoCateterSonda(Listado.ordenarMapa(indices,
				"fechaInsercion_",
				"",
				mapa,
				num));

		forma.getMapaHistoricoCateterSonda().put("numRegistros", new Integer(num));


		return true;
	}

	/**
	 * Método que realiza la mezcla de la consulta de los historicos de los cateter sonda fijos
	 * y parametrizados, y los llena en un HashMap para despues mezclarlos
	 * @param forma
	 * @return HashMap
	 */
	private HashMap formarHistoricoCateterSonda(ImpresionResumenAtencionesForm forma)
	{
		//-------------- Se guarda en una matriz los cateteres sonda fijos -----------------------------//
		Vector[] matrizCateteresFijos = new Vector[forma.getCateterSondaFijosHisto().size()];
		if(forma.getCateterSondaFijosHisto().size() > 0)
		{
			Iterator iterador1=forma.getCateterSondaFijosHisto().iterator();
			for (int numFila=0; numFila<forma.getCateterSondaFijosHisto().size(); numFila++)
			{
				HashMap fila1=(HashMap)iterador1.next();
				matrizCateteresFijos[numFila]=new Vector();
				matrizCateteresFijos[numFila].add(fila1.get("cateter_sonda_reg_enfer"));
				matrizCateteresFijos[numFila].add(fila1.get("via_insercion"));

				if (UtilidadCadena.noEsVacio(fila1.get("fecha_insercion")+""))
					matrizCateteresFijos[numFila].add(fila1.get("fecha_insercion"));
				else
					matrizCateteresFijos[numFila].add("");

				if (UtilidadCadena.noEsVacio(fila1.get("hora_insercion")+""))
					matrizCateteresFijos[numFila].add(fila1.get("hora_insercion"));
				else
					matrizCateteresFijos[numFila].add("");

				if (UtilidadCadena.noEsVacio(fila1.get("fecha_retiro")+""))
					matrizCateteresFijos[numFila].add(fila1.get("fecha_retiro"));
				else
					matrizCateteresFijos[numFila].add("");

				if (UtilidadCadena.noEsVacio(fila1.get("hora_retiro")+""))
					matrizCateteresFijos[numFila].add(fila1.get("hora_retiro"));
				else
					matrizCateteresFijos[numFila].add("");

				matrizCateteresFijos[numFila].add(fila1.get("curaciones"));
				matrizCateteresFijos[numFila].add(fila1.get("observaciones"));

				matrizCateteresFijos[numFila].add(fila1.get("fecha_registro"));
				matrizCateteresFijos[numFila].add(fila1.get("hora_registro"));
				//matrizCateteresFijos[numFila].add(fila1.get("nombre_usuario"));
			}
		}

		//-------------- Se guarda en una matriz los cateteres sonda parametrizados -----------------------------//
		Vector[] matrizCateteresParam = new Vector[forma.getCateterSondaParamHisto().size()];
		if(forma.getCateterSondaParamHisto().size() > 0)
		{
			Iterator iterador2=forma.getCateterSondaParamHisto().iterator();
			for (int numFila2=0; numFila2<forma.getCateterSondaParamHisto().size(); numFila2++)
			{
				HashMap fila2=(HashMap)iterador2.next();
				matrizCateteresParam[numFila2]=new Vector();
				matrizCateteresParam[numFila2].add(fila2.get("cateter_sonda_reg_enfer"));
				matrizCateteresParam[numFila2].add(fila2.get("codigo_tipo"));
				matrizCateteresParam[numFila2].add(fila2.get("col_cateter_sonda_cc_ins"));
				matrizCateteresParam[numFila2].add(fila2.get("valor"));
			}
		}

		//--------------- Se recorre el hisorico de cateteres fijos y parametrizados y se va guardando en el hashMap
		HashMap nuevoMapaCateterHisto=new HashMap();
		if(forma.getCateterSondaHistoTodos().size() > 0)
		{
			Iterator iterador3=forma.getCateterSondaHistoTodos().iterator();

			for (int numFila3=0; numFila3<forma.getCateterSondaHistoTodos().size(); numFila3++)
			{
				HashMap fila3=(HashMap)iterador3.next();

				nuevoMapaCateterHisto.put("cateterSondaRegEnfer_"+numFila3, fila3.get("cateter_sonda_reg_enfer"));
				nuevoMapaCateterHisto.put("nombreArticulo_"+numFila3, fila3.get("nombre_articulo"));
				nuevoMapaCateterHisto.put("codigoArticuloCcIns_"+numFila3, fila3.get("codigo_articulo_cc_ins"));
				nuevoMapaCateterHisto.put("nombre_usuario_"+numFila3, fila3.get("nombre_usuario"));
				for(int fil1=0; fil1<matrizCateteresFijos.length; fil1++)
				{
					if(matrizCateteresFijos[fil1].elementAt(0).equals(fila3.get("cateter_sonda_reg_enfer")))
					{
						nuevoMapaCateterHisto.put("viaInsercion_"+numFila3, matrizCateteresFijos[fil1].elementAt(1)+"");
						nuevoMapaCateterHisto.put("fechaInsercion_"+numFila3, matrizCateteresFijos[fil1].elementAt(2)+"");
						nuevoMapaCateterHisto.put("horaInsercion_"+numFila3,matrizCateteresFijos[fil1].elementAt(3)+"");
						nuevoMapaCateterHisto.put("fechaRetiro_"+numFila3,matrizCateteresFijos[fil1].elementAt(4)+"");
						nuevoMapaCateterHisto.put("horaRetiro_"+numFila3,matrizCateteresFijos[fil1].elementAt(5)+"");
						nuevoMapaCateterHisto.put("curaciones_"+numFila3,matrizCateteresFijos[fil1].elementAt(6)+"");
						nuevoMapaCateterHisto.put("observaciones_"+numFila3,matrizCateteresFijos[fil1].elementAt(7)+"");
						nuevoMapaCateterHisto.put("fecha_registro_"+numFila3,matrizCateteresFijos[fil1].elementAt(8)+"");
						nuevoMapaCateterHisto.put("hora_registro_"+numFila3,matrizCateteresFijos[fil1].elementAt(9)+"");
						//nuevoMapaCateterHisto.put("nombre_usuario_"+numFila3,matrizCateteresFijos[fil1].elementAt(12)+"");
						break;
					}
					//--------Si llegï¿½ hasta aquï¿½ entonces no tiene valor
					if(fil1==matrizCateteresFijos.length-1)
					{
						nuevoMapaCateterHisto.put("viaInsercion_"+numFila3, "");
						nuevoMapaCateterHisto.put("fechaInsercion_"+numFila3, "");
						nuevoMapaCateterHisto.put("horaInsercion_"+numFila3, "");
						nuevoMapaCateterHisto.put("fechaRetiro_"+numFila3,"");
						nuevoMapaCateterHisto.put("horaRetiro_"+numFila3,"");
						nuevoMapaCateterHisto.put("curaciones_"+numFila3,"");
						nuevoMapaCateterHisto.put("observaciones_"+numFila3,"");
						nuevoMapaCateterHisto.put("fecha_registro_"+numFila3,"");
						nuevoMapaCateterHisto.put("hora_registro_"+numFila3,"");
						nuevoMapaCateterHisto.put("nombre_usuario_"+numFila3,"");
					}
				}//for

				//----------- Se agregan los valores histï¿½ricos de los cateteres sonda paremetrizados -----------------// 
				if(forma.getColCateteresSondaInstitucionCcosto().size() > 0)
				{
					Iterator iterador4=forma.getColCateteresSondaInstitucionCcosto().iterator();

					for (int numFila4=0; numFila4<forma.getColCateteresSondaInstitucionCcosto().size(); numFila4++)
					{
						HashMap fila4=(HashMap)iterador4.next();
						for(int fil2=0; fil2<matrizCateteresParam.length; fil2++)
						{
							if(matrizCateteresParam[fil2].elementAt(0).equals(fila3.get("cateter_sonda_reg_enfer")) && matrizCateteresParam[fil2].elementAt(1).equals(fila4.get("codigo_tipo")))
							{
								nuevoMapaCateterHisto.put("valorParam_"+fila4.get("codigo_tipo")+"_"+numFila3, matrizCateteresParam[fil2].elementAt(3)+"");
								break;
							}
							//--------Si llegï¿½ hasta aquï¿½ entonces no tiene valor
							if(fil2==matrizCateteresParam.length-1)
							{
								nuevoMapaCateterHisto.put("valorParam_"+fila4.get("codigo_tipo")+"_"+numFila3, "");
							}
						}//for
					}//for
				} //if  
			}//for numFila3

			nuevoMapaCateterHisto.put("numRegistros", new Integer(forma.getCateterSondaHistoTodos().size()));
		}  //if 
		else
		{
			nuevoMapaCateterHisto.put("numRegistros", new Integer(0));
		}

		return nuevoMapaCateterHisto;
	}


	/**
	 * Metodo para cargar el control de liquidos de registro de enfermeria de una paciente especifico. 
	 *
	 */
	/**
	 * Metodo para cargar el control de liquidos de registro de enfermeria de una paciente especifico. 
	 *
	 */
	private void cargarControlLiquidos(Connection con, HttpServletRequest request, int tipoInfomacion, PersonaBasica paciente, UsuarioBasico usuario, ImpresionResumenAtencionesForm forma, int codigoCuenta, int cuentaAsociada) throws SQLException 
	{
		RegistroEnfermeria mundo = new RegistroEnfermeria();
		HashMap parametros = new HashMap();

		//-- Almacenar los parametros de busqueda.
		parametros.put("fechaInicio", UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial())); 
		parametros.put("fechaFinal", UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal())); 
		parametros.put("horaInicio", forma.getHoraInicial()); 
		parametros.put("horaFinal", forma.getHoraFinal()); 

		//-- Colocar los parametros requeridos en el mapa. 


		parametros.put("cuentas",UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, forma.getIdIngreso()+""));

		parametros.put("centroCosto", paciente.getCodigoArea()+"");
		parametros.put("institucion",usuario.getCodigoInstitucionInt()+"");
		parametros.put("filtroAsocio",forma.getFiltroAsocio()+"");


		if (tipoInfomacion == 0) //-- Es para Saber Si hay registro de control de liquidos.  
		{
			//-- Determinar la natruraleza de la consulta.
			parametros.put("nroConsulta","1");

			//-- Enviar a Consultar.
			forma.setMapaControlLiq(mundo.consultarControlLiquidos(con, parametros));

			//-- Para Quitar 
			forma.setMapaControlLiq("fechaHistoricoDieta","0");
			forma.setMapaControlLiq("paginadorLiqAdmin","0");
			forma.setMapaControlLiq("paginadorLiqElim","0");
		}

		if (tipoInfomacion == 1) //-- Cargar los Informaci?n de Control de liquidos. 
		{
			HashMap mp = new HashMap();

			//-- Consultar los nombres de los medicamentos Administrados. (Seccion Control Liquidos)  
			parametros.put("nroConsulta","6");
			forma.setMapaControlLiq( mundo.consultarControlLiquidos(con, parametros) );

			//-- Consultar los nombres de los medicamentos Administrados. (Seccion Control Liquidos)  
			parametros.put("nroConsulta","2");
			mp = mundo.consultarControlLiquidos(con, parametros);
			forma.setMapaControlLiq("nroRegMedAdm", mp.get("numRegistros")+"");
			mp.remove("numRegistros");
			forma.getMapaControlLiq().putAll(mp);

			//-- Cargar los codigo y los nombres de los liquidos eliminados. (SOLAMENTE HAY PARAMETRIZABLES).
			mp.clear();
			parametros.put("nroConsulta","3");
			mp = mundo.consultarControlLiquidos(con, parametros);			
			forma.setMapaControlLiq("nroRegMedElim", mp.get("numRegistros")+"");
			mp.remove("numRegistros");
			forma.getMapaControlLiq().putAll(mp);

			//------Cargar la informacion registrada de liquidos administados ( parametrizables y no parametrizables ) y eliminados (parametrizados).  
			mp.clear();
			parametros.put("nroConsulta","4");
			mp = mundo.consultarControlLiquidos(con, parametros);
			forma.setMapaControlLiq("nroRegBalLiqAdm", mp.get("numRegistros")+"");
			mp.remove("numRegistros");
			forma.getMapaControlLiq().putAll(mp);

			//----Carga el numero de registros eliminados en el mapa.
			mp.clear();
			parametros.put("nroConsulta","5");
			mp = mundo.consultarControlLiquidos(con, parametros);
			forma.setMapaControlLiq("nroRegBalLiqElim", mp.get("numRegistros")+"");
			forma.setMapaControlLiq("fechaHistoricoDieta","1");
			forma.setMapaControlLiq("paginadorLiqAdmin","0");
			forma.setMapaControlLiq("paginadorLiqElim","0");
		}
	}
	/**
	 * Metodo para cargar Antecedentes Alergias
	 * @param con
	 * @param request
	 * @param tipoInfomacion
	 * @param paciente
	 * @param usuario
	 * @param forma
	 * @throws SQLException
	 */
	private void cargarAntecedentesAlergias(Connection con, int tipoInfomacion, PersonaBasica paciente, UsuarioBasico usuario, ImpresionResumenAtencionesForm forma) throws SQLException 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();

		HashMap mp = new HashMap();

		if (tipoInfomacion == 0)  //-- Consultar si hay informacion de antecedentes alergias. 
		{
			forma.getMapaAntAlergia().clear();

			mp.put("nroConsulta","1");
			mp.put("paciente", paciente.getCodigoPersona() + "");

			mp = mundo.consultarInformacion(con, mp);

			forma.setMapaAntAlergia("observaciones", mp.get("observaciones_0")+"");

			if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
			{
				forma.setMapaAntAlergia("hayAntecedentes","SI");
			}
			else
			{
				forma.setMapaAntAlergia("hayAntecedentes","NO");
			}

		}
		else					  //-- Traer toda la informaci?n de antecedentes de alergias. 		
		{
			mp.put("nroConsulta","2");
			mp.put("paciente",paciente.getCodigoPersona()+"");



			forma.getMapaAntAlergia().putAll(mundo.consultarInformacion(con, mp));
		}
	}


	/**
	 * Metodo para cargar Antecedentes Familiares
	 * @param con
	 * @param request
	 * @param tipoInfomacion
	 * @param paciente
	 * @param usuario
	 * @param forma
	 * @throws SQLException
	 */

	private void cargarAntecedentesFamiliares(Connection con, int tipoInfomacion, PersonaBasica paciente, UsuarioBasico usuario, ImpresionResumenAtencionesForm forma) throws SQLException 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();

		HashMap mp = new HashMap();

		if (tipoInfomacion == 0)  //-- Consultar si hay informacion de antecedentes alergias. 
		{
			forma.getMapaAntFamiliares().clear();

			mp.put("nroConsulta","3");
			mp.put("paciente", paciente.getCodigoPersona() + "");



			mp = mundo.consultarInformacion(con, mp);

			forma.setMapaAntFamiliares("observaciones", mp.get("observaciones_0")+"");

			if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
			{
				forma.setMapaAntFamiliares("hayAntecedentes","SI");
			}
			else
			{
				forma.setMapaAntFamiliares("hayAntecedentes","NO");
			}

		}
		else					  //-- Traer toda la informaci?n de antecedentes de alergias. 		
		{
			mp.put("nroConsulta","4");
			mp.put("paciente",paciente.getCodigoPersona()+"");



			forma.getMapaAntFamiliares().putAll(mundo.consultarInformacion(con, mp));
		}
	}

	/**
	 * Metodo para cargar Antecedentes Familiares Oculares 
	 * @param con
	 * @param request
	 * @param tipoInfomacion
	 * @param paciente
	 * @param usuario
	 * @param forma
	 * @throws SQLException
	 */

	private void cargarAntecedentesFamiliaresOculares(Connection con, int tipoInfomacion, PersonaBasica paciente, UsuarioBasico usuario, ImpresionResumenAtencionesForm forma) throws SQLException 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();

		HashMap mp = new HashMap();

		if (tipoInfomacion == 0)  //-- Consultar si hay informacion de antecedentes  
		{
			forma.getMapaAntFamOftal().clear();

			mp.put("nroConsulta","5");
			mp.put("paciente", paciente.getCodigoPersona() + "");
			mp.put("institucion", usuario.getCodigoInstitucionInt() + "");




			mp = mundo.consultarInformacion(con, mp);

			forma.setMapaAntFamOftal("observaciones", mp.get("observaciones_0")+"");

			if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
			{
				forma.setMapaAntFamOftal("hayAntecedentes","SI");
			}
			else
			{
				forma.setMapaAntFamOftal("hayAntecedentes","NO");
			}

		}
		else					  //-- Traer toda la informaci?n de antecedentes  		
		{
			mp.put("nroConsulta","6");
			mp.put("paciente",paciente.getCodigoPersona()+"");
			mp.put("institucion", usuario.getCodigoInstitucionInt() + "");




			forma.getMapaAntFamOftal().putAll(mundo.consultarInformacion(con, mp));
		}
	}

	/**
	 * Metodo para cargar Antecedentes Familiares Oculares 
	 * @param con
	 * @param request
	 * @param tipoInfomacion
	 * @param paciente
	 * @param usuario
	 * @param forma
	 * @throws SQLException
	 */

	private void cargarAntecedentesPersonalesOculares(Connection con, int tipoInfomacion, PersonaBasica paciente, UsuarioBasico usuario, ImpresionResumenAtencionesForm forma) throws SQLException 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();

		HashMap mp = new HashMap();

		if (tipoInfomacion == 0)  //-- Consultar si hay informacion de antecedentes  
		{
			forma.getMapaAntPersoOftal().clear();

			mp.put("nroConsulta","7");
			mp.put("paciente", paciente.getCodigoPersona() + "");
			mp.put("institucion", usuario.getCodigoInstitucionInt() + "");




			mp = mundo.consultarInformacion(con, mp);

			forma.setMapaAntPersoOftal("observaciones", mp.get("observaciones_0")+"");

			if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
			{
				forma.setMapaAntPersoOftal("hayAntecedentes","SI");
			}
			else
			{
				forma.setMapaAntPersoOftal("hayAntecedentes","NO");
			}

		}
		else					  //-- Traer toda la informaci?n de antecedentes  		
		{
			mp.put("nroConsulta","8");
			mp.put("paciente",paciente.getCodigoPersona()+"");
			mp.put("institucion", usuario.getCodigoInstitucionInt() + "");




			forma.getMapaAntPersoOftal().putAll(mundo.consultarInformacion(con, mp));

			mp.put("nroConsulta","9");  //-- consultar la seccion quirurgicos



			mp = mundo.consultarInformacion(con, mp);

			forma.setMapaAntPersoOftal("numRegQuirur",mp.get("numRegistros")+"");
			mp.remove("numRegistros");
			forma.getMapaAntPersoOftal().putAll(mp);   
		}
	}

	//-------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------------


	/**
	 * Metodo para cargar los antecedentes GinecoObstetricos. 
	 * @param con
	 * @param request
	 * @param tipoInfomacion
	 * @param paciente
	 * @param usuario
	 * @param forma
	 * @throws SQLException
	 */

	private void cargarAntecedentesGinecoObstetricos(Connection con, HttpServletRequest request, int tipoInfomacion, PersonaBasica paciente, UsuarioBasico usuario, ImpresionResumenAtencionesForm forma) throws SQLException 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		HashMap mp = new HashMap();

		//-- Almacenar los parametros de busqueda.
		mp.put("fechaInicio", UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial())); 
		mp.put("fechaFinal", UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal())); 
		mp.put("horaInicio", forma.getHoraInicial()); 
		mp.put("horaFinal", forma.getHoraFinal()); 		


		if (tipoInfomacion == 0)  //-- Consultar si hay informacion de antecedentes  
		{
			forma.getMapaAntGineco().clear();
			mp.put("nroConsulta","10");
			mp.put("paciente", paciente.getCodigoPersona() + "");
			mp.put("institucion", usuario.getCodigoInstitucionInt() + "");
			mp = mundo.consultarInformacion(con, mp);

			forma.setMapaAntGineco("observaciones", mp.get("observaciones_0")+"");

			if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
			{
				forma.setMapaAntGineco("hayAntecedentes","SI");
			}
			else
			{
				forma.setMapaAntGineco("hayAntecedentes","NO");
			}
		}
		else					  //-- Traer toda la informaci?n de antecedentes  		
		{
			AntecedentesGinecoObstetricosForm antecedentesBean = new AntecedentesGinecoObstetricosForm();

			// Se carga toda la informaci?n de la bd en el momento para mostrar un resumen completo
			AntecedentesGinecoObstetricos antecedentes = new AntecedentesGinecoObstetricos();
			antecedentes.setPaciente(paciente);


			//-- Hacer Otra Funci?n de Cargar. Con los Parametros de Busqueda en el MAPA
			HashMap mapa = new HashMap();
			mapa.put("paciente", paciente.getCodigoPersona()+"" );
			antecedentes.cargar(con, mapa);
			//antecedentes.cargar(con, 0);


			cargarBeanCompleto(antecedentes,antecedentesBean,forma);

			//request.setAttribute("numeroEmbarazos", ""+antecedentesBean.getNumeroEmbarazos());
			request.setAttribute("numeroEmbarazos", ""+forma.getMapaAntGineco("NumeroEmbarazos"));

			//request.setAttribute("numMetodosAnticonceptivos", ""+antecedentesBean.getNumMetodosAnticonceptivos());
			request.setAttribute("numeroEmbarazos", ""+forma.getMapaAntGineco("NumMetodosAnticonceptivos"));

			//--
			int nroRows = UtilidadCadena.vInt(forma.getMapaAntGineco("NumeroEmbarazos")+"");
			for(int i=1; i<=nroRows; i++)
			{
				String nH = (String)antecedentesBean.getValue("numeroHijos_" +i);
				int numH = Integer.parseInt(nH);

				request.setAttribute("numeroHijos_"+i, nH);
				forma.setMapaAntGineco("numeroHijos_"+i, nH);

				for(int j=1; j<=numH; j++)
				{
					//String numeroTiposPartoVaginalTemp=(String)antecedentesBean.getValue("numTiposPartoVaginal_"+i+"_"+j);
					String numeroTiposPartoVaginalTemp = forma.getMapaAntGineco("numTiposPartoVaginal_"+i+"_"+j)+"";

					if (numeroTiposPartoVaginalTemp!=null)
					{
						request.setAttribute("numTiposPartoVaginal_"+i+"_"+j, antecedentesBean.getValue("numTiposPartoVaginal_"+i+"_"+j));
						forma.setMapaAntGineco("numTiposPartoVaginal_"+i+"_"+j, forma.getMapaAntGineco("numTiposPartoVaginal_"+i+"_"+j)+"");
					}
					else
					{
						request.setAttribute("numTiposPartoVaginal_"+i+"_"+j, "0");
						forma.setMapaAntGineco("numTiposPartoVaginal_"+i+"_"+j, "0");
					}

				}
			}
		}
	}

	/**
	 * Carga el bean de antecedentes ginecoobstetricos con los historicos
	 * @param forma 
	 */
	public void cargarBeanCompleto(AntecedentesGinecoObstetricos antecedentes, AntecedentesGinecoObstetricosForm bean, ImpresionResumenAtencionesForm forma)
	{
		// carga los basicos del bean
		cargarBean(antecedentes, bean, forma);

		// los historicos
		ArrayList historicos = antecedentes.getAntecedentesHistoricos();
		bean.setHistoricos(historicos);
		forma.setHistoricos(historicos);
	}


	/**
	 * Carga el form de antecedentes ginecoobst?tricos con la informaci?n
	 * pertinente contenida en el objeto.
	 * @param forma 
	 * @param 	AntecedentesGinecoObstetricos, antecedentes
	 * @param 	AntecedentesGinecoObstetricosForm, bean
	 */
	public void cargarBean(AntecedentesGinecoObstetricos antecedentes, AntecedentesGinecoObstetricosForm bean, ImpresionResumenAtencionesForm forma)
	{	
		//---No historicos y no modificables despues de grabados
		if( !antecedentes.getRangoEdadMenarquia().getAcronimo().equals("") )
		{
			bean.setRangoEdadMenarquia(antecedentes.getRangoEdadMenarquia().getCodigo());
			forma.setMapaAntGineco("RangoEdadMenarquia",antecedentes.getRangoEdadMenarquia().getCodigo()+"");

			if( antecedentes.getRangoEdadMenarquia().getCodigo() == -1 )
			{
				bean.setNombreRangoEdadMenarquia("No se ha grabado informaci?n");
				forma.setMapaAntGineco("RangoEdadMenarquia","-1");
				forma.setMapaAntGineco("NombreRangoEdadMenarquia","No se ha grabado informaci?n");
			}	
			else
			{
				forma.setMapaAntGineco("ExisteRangoEdadMenarquia","true");
				forma.setMapaAntGineco("NombreRangoEdadMenarquia",antecedentes.getRangoEdadMenarquia().getValue()+"");
				bean.setNombreRangoEdadMenarquia(antecedentes.getRangoEdadMenarquia().getValue());
				bean.setExisteRangoEdadMenarquia(true);
			}
		}

		forma.setMapaAntGineco("OtroEdadMenarquia",antecedentes.getOtroEdadMenarquia());
		bean.setOtraEdadMenarquia(antecedentes.getOtroEdadMenarquia());


		if( !antecedentes.getRangoEdadMenopausia().getAcronimo().equals("") )
		{
			bean.setRangoEdadMenopausia(antecedentes.getRangoEdadMenopausia().getCodigo());
			forma.setMapaAntGineco("RangoEdadMenopausia",antecedentes.getRangoEdadMenopausia().getCodigo()+"");

			if( antecedentes.getRangoEdadMenopausia().getCodigo() == -1 )
			{
				bean.setNombreRangoEdadMenopausia("No se ha grabado informaci?n");
				forma.setMapaAntGineco("NombreRangoEdadMenopausia","No se ha grabado informaci?n");
			}	
			else
			{
				forma.setMapaAntGineco("NombreRangoEdadMenopausia",antecedentes.getRangoEdadMenopausia().getValue()+"");
				forma.setMapaAntGineco("ExisteRangoEdadMenopausia","true");

				bean.setNombreRangoEdadMenopausia(antecedentes.getRangoEdadMenopausia().getValue());
				bean.setExisteRangoEdadMenopausia(true);
			}
		}
		bean.setOtraEdadMenopausia(antecedentes.getOtroEdadMenopausia());
		forma.setMapaAntGineco("OtraEdadMenopausia",antecedentes.getOtroEdadMenopausia()+"");

		if(antecedentes.getInicioVidaSexual() == 0 )
		{
			bean.setInicioVidaSexual("");
			forma.setMapaAntGineco("InicioVidaSexual","");
		}	
		else
		{
			bean.setInicioVidaSexual(""+antecedentes.getInicioVidaSexual());
			forma.setMapaAntGineco("InicioVidaSexual",""+antecedentes.getInicioVidaSexual());
		}	

		if(antecedentes.getInicioVidaObstetrica() == 0 ) 
		{
			bean.setInicioVidaObstetrica("");
			forma.setMapaAntGineco("InicioVidaObstetrica","");
		}	
		else
		{
			bean.setInicioVidaObstetrica(""+antecedentes.getInicioVidaObstetrica());
			forma.setMapaAntGineco("InicioVidaObstetrica",""+antecedentes.getInicioVidaObstetrica());
		}	

		String observacionesStr =  antecedentes.getObservaciones();

		if( observacionesStr == null )
			observacionesStr = "";

		forma.setMapaAntGineco("observacionesStr",observacionesStr);


		//if( bean.estado.equals("resumen") )
		{
			bean.setObservacionesViejas(observacionesStr.replaceAll("\n", "<br>"));
			forma.setMapaAntGineco("ObservacionesViejas",observacionesStr.replaceAll("\n", "<br>"));
		}	
		//else
		//bean.setObservacionesViejas(observacionesStr.replaceAll("<br>", "\n"));
		//		Fin no historicos y no modificables despues de grabados

		// 		M?todos anticonceptivos
		ArrayList metodosAnticonceptivos = antecedentes.getMetodosAnticonceptivos();
		bean.setNumMetodosAnticonceptivos(metodosAnticonceptivos.size());
		forma.setMapaAntGineco("NumMetodosAnticonceptivos",metodosAnticonceptivos.size()+"");


		for( int i=0; i < metodosAnticonceptivos.size(); i++ )
		{
			InfoDatos metodo = (InfoDatos)metodosAnticonceptivos.get(i);

			bean.setValue("metodosAnticonceptivos_"+metodo.getAcronimo(), metodo.getAcronimo());
			forma.setMapaAntGineco("metodosAnticonceptivos_"+metodo.getAcronimo(), metodo.getAcronimo());

			bean.setValue("nombreMetodosAnticonceptivos_"+metodo.getAcronimo(), metodo.getValue());
			forma.setMapaAntGineco("nombreMetodosAnticonceptivos_"+metodo.getAcronimo(), metodo.getAcronimo());

			bean.setValue("descMetodosAnticonceptivos_"+metodo.getAcronimo(), metodo.getDescripcion());
			forma.setMapaAntGineco("descMetodosAnticonceptivos_"+metodo.getAcronimo(), metodo.getAcronimo());

			bean.setValue("existeMetodoAnticonceptivo_"+metodo.getAcronimo(), "true");
			forma.setMapaAntGineco("existeMetodoAnticonceptivo_"+metodo.getAcronimo(), metodo.getAcronimo());

			if( !metodo.getDescripcion().trim().equals("") )
			{
				bean.setValue("existeDescMetodoAnticonceptivo_"+metodo.getAcronimo(), "true");
				forma.setMapaAntGineco("existeDescMetodoAnticonceptivo_"+metodo.getAcronimo(), metodo.getAcronimo());
			}	
		}
		//		Fin m?todos anticonceptivos

		//		Embarazos
		ArrayList embarazos = antecedentes.getEmbarazos();

		bean.setNumeroEmbarazos(embarazos.size());
		forma.setMapaAntGineco("NumeroEmbarazos", embarazos.size()+"");
		forma.setMapaAntGineco("NumGestaciones", embarazos.size()+"");

		bean.setNumGestaciones(bean.getNumeroEmbarazos());
		for(int i=1; i<=embarazos.size(); i++)
		{
			Embarazo embarazo = (Embarazo)embarazos.get(i-1);

			bean.setValue("codigo_"+i, embarazo.getCodigo()+"");
			forma.setMapaAntGineco("codigo_"+i, embarazo.getCodigo()+"");

			//Guardamos el n?mero de hijos por embarazo
			bean.setValue("numeroHijos_" + i, embarazo.getHijos().size() + "");
			forma.setMapaAntGineco("numeroHijos_" + i, embarazo.getHijos().size() + "");

			bean.setValue("mesesGestacion_"+i, Float.toString(embarazo.getMesesGestacion()));
			forma.setMapaAntGineco("mesesGestacion_"+i, Float.toString(embarazo.getMesesGestacion()));

			bean.setValue("fechaTerminacion_"+i, embarazo.getFechaTerminacion());
			forma.setMapaAntGineco("fechaTerminacion_"+i, embarazo.getFechaTerminacion());

			bean.setValue("duracion_"+i,embarazo.getDuracion());
			forma.setMapaAntGineco("duracion_"+i,embarazo.getDuracion());

			bean.setValue("ruptura_"+i,embarazo.getTiempoRupturaMembranas());
			forma.setMapaAntGineco("ruptura_"+i,embarazo.getTiempoRupturaMembranas());

			bean.setValue("legrado_"+i,embarazo.getLegrado());
			forma.setMapaAntGineco("legrado_"+i,embarazo.getLegrado());

			int compTempo[]=embarazo.getComplicacion();
			for(int y=0;y<compTempo.length;y++)
			{
				if(compTempo[y]!=0)
				{
					bean.setValue("complicacionEmbarazo_"+i+"_"+compTempo[y],compTempo[y]+"");
					forma.setMapaAntGineco("complicacionEmbarazo_"+i+"_"+compTempo[y],compTempo[y]+"");
				}
			}
			Vector nombresComplicaciones=embarazo.getNombresComplicaciones();
			for(int y=0;y<nombresComplicaciones.size();y++)
			{
				bean.setValue("nombreComplicacionEmbarazo_"+i+"_"+y,nombresComplicaciones.elementAt(y));
				forma.setMapaAntGineco("nombreComplicacionEmbarazo_"+i+"_"+y,nombresComplicaciones.elementAt(y)+"");
			}

			Vector otrasComplicaciones=embarazo.getOtraComplicacion();
			for(int j=0; j<otrasComplicaciones.size();j++)
			{
				bean.setValue("otraComplicacionEmbarazo_"+i+"_"+j, otrasComplicaciones.elementAt(j)+"");
				forma.setMapaAntGineco("otraComplicacionEmbarazo_"+i+"_"+j, otrasComplicaciones.elementAt(j)+"");
			}
			bean.setValue("numOtraComplicacion_"+i, new Integer(otrasComplicaciones.size()));
			forma.setMapaAntGineco("numOtraComplicacion_"+i, otrasComplicaciones.size()+"");

			bean.setValue("tipoTrabajoParto_"+i, Integer.toString(embarazo.getTrabajoParto().getCodigo()));
			forma.setMapaAntGineco("tipoTrabajoParto_"+i, embarazo.getTrabajoParto().getCodigo()+"");


			if( embarazo.getTrabajoParto().getCodigo() == -1 )
			{
				bean.setValue("nombreTipoTrabajoParto_"+i, "");
				forma.setMapaAntGineco("nombreTipoTrabajoParto_"+i, "");
			}
			else
			{
				bean.setValue("nombreTipoTrabajoParto_"+i, embarazo.getTrabajoParto().getValue());
				forma.setMapaAntGineco("nombreTipoTrabajoParto_"+i, embarazo.getTrabajoParto().getValue());
			}

			bean.setValue("otroTipoTrabajoParto_"+i, embarazo.getOtroTrabajoParto());
			forma.setMapaAntGineco("otroTipoTrabajoParto_"+i, embarazo.getOtroTrabajoParto());

			//	Hijos embarazo.
			ArrayList hijos = embarazo.getHijos();
			ArrayList formasPartoVaginal;
			bean.setValue("numeroHijos_"+i, ""+hijos.size());
			forma.setMapaAntGineco("numeroHijos_"+i, ""+hijos.size());

			for(int j=1; j<=hijos.size(); j++)
			{
				HijoBasico hijo = new HijoBasico(); 
				hijo = (HijoBasico)hijos.get(j-1);

				boolean partoVaginal = true;

				if( hijo.isVivo() )
				{
					bean.setNumVivos(bean.getNumVivos()+1);
					bean.setNumVivosGrabados(bean.getNumVivos());
					bean.setValue("vitalidad_"+i+"_"+j, "vivo");
					forma.setMapaAntGineco("vitalidad_"+i+"_"+j, "vivo");
				}
				else
				{
					bean.setNumMuertos(bean.getNumMuertos()+1);
					bean.setNumMuertosGrabados(bean.getNumMuertos());
					bean.setValue("vitalidad_"+i+"_"+j, "muerto");
					forma.setMapaAntGineco("vitalidad_"+i+"_"+j, "muerto");
				}

				if( hijo.isAborto() )
				{
					bean.setValue("cargadoBD_"+i+"_"+j, "true");
					forma.setMapaAntGineco("cargadoBD_"+i+"_"+j, "true");

					bean.setValue("tiposParto_"+i+"_"+j, new String("4"));
					forma.setMapaAntGineco("tiposParto_"+i+"_"+j, new String("4"));

					bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Mortinato"));
					forma.setMapaAntGineco("nombreTiposParto_"+i+"_"+j, new String("Mortinato"));

					partoVaginal = false;
					bean.setNumAbortos(bean.getNumAbortos()+1);
					forma.setMapaAntGineco("NumAbortos", ""+bean.getNumAbortos()+1);

					bean.setNumAbortosGrabados(bean.getNumAbortos());
					forma.setMapaAntGineco("NumAbortosGrabados", ""+bean.getNumAbortos());

				} 
				else
					if( hijo.isCesarea() )
					{
						bean.setValue("cargadoBD_"+i+"_"+j, "true");
						forma.setMapaAntGineco("cargadoBD_"+i+"_"+j, "true");

						bean.setValue("tiposParto_"+i+"_"+j, new String("5"));
						forma.setMapaAntGineco("tiposParto_"+i+"_"+j, new String("5"));

						bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Cesarea"));
						forma.setMapaAntGineco("nombreTiposParto_"+i+"_"+j, new String("Cesarea"));

						partoVaginal = false;
						bean.setNumCesareas(bean.getNumCesareas()+1);
						forma.setMapaAntGineco("NumCesareas", ""+(bean.getNumCesareas()+1));

						bean.setNumCesareasGrabadas(bean.getNumCesareas());
						forma.setMapaAntGineco("NumCesareasGrabadas", ""+bean.getNumCesareas());
					} 
					else
						if( hijo.getOtroTipoParto() != null && !hijo.getOtroTipoParto().equals("") )
						{
							bean.setValue("cargadoBD_"+i+"_"+j, "true");
							forma.setMapaAntGineco("cargadoBD_"+i+"_"+j, "true");

							bean.setValue("tiposParto_"+i+"_"+j, new String("0"));
							forma.setMapaAntGineco("tiposParto_"+i+"_"+j, new String("0"));

							bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Otro"));
							forma.setMapaAntGineco("nombreTiposParto_"+i+"_"+j, new String("Otro"));

							bean.setValue("otroTipoParto_" +i+"_"+j, hijo.getOtroTipoParto());
							forma.setMapaAntGineco("otroTipoParto_" +i+"_"+j, hijo.getOtroTipoParto());

							partoVaginal = false;
						}
						else
							if( ( formasPartoVaginal = hijo.getFormasNacimientoVaginal() ).size() > 0 )
							{
								bean.setValue("cargadoBD_"+i+"_"+j, "true");
								forma.setMapaAntGineco("cargadoBD_"+i+"_"+j, "true");

								bean.setNumPartos(bean.getNumPartos()+1);
								forma.setMapaAntGineco("NumPartos", ""+(bean.getNumPartos()+1));

								bean.setNumPartosGrabados(bean.getNumPartos());
								forma.setMapaAntGineco("NumPartosGrabados", ""+bean.getNumPartos());

								boolean esvalido = true;					
								if(formasPartoVaginal.size()==1)
								{
									InfoDatos tipoPVInfo = (InfoDatos)formasPartoVaginal.get(0);
									// En este caso no debemos mostrarlo de forma tradicional
									if( tipoPVInfo.getCodigo() == -2 )
									{
										bean.setValue("tiposParto_"+i+"_"+j, new String("3"));
										forma.setMapaAntGineco("tiposParto_"+i+"_"+j, new String("3"));

										bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Parto Vaginal"));
										forma.setMapaAntGineco("nombreTiposParto_"+i+"_"+j, new String("Parto Vaginal"));

										bean.setValue("numTiposPartoVaginal_"+i+"_"+j, "0");
										forma.setMapaAntGineco("numTiposPartoVaginal_"+i+"_"+j, "0");

										esvalido = false;
									}
								}
								if( esvalido )
								{

									bean.setValue("tiposParto_"+i+"_"+j, new String("3"));
									forma.setMapaAntGineco("tiposParto_"+i+"_"+j, new String("3"));

									bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Parto Vaginal"));
									forma.setMapaAntGineco("nombreTiposParto_"+i+"_"+j, new String("Parto Vaginal"));

									bean.setValue("numTiposPartoVaginal_"+i+"_"+j, ""+formasPartoVaginal.size());
									forma.setMapaAntGineco("numTiposPartoVaginal_"+i+"_"+j, ""+formasPartoVaginal.size());

									for(int k=0; k<formasPartoVaginal.size(); k++)
									{
										InfoDatos tipoPVInfo = (InfoDatos)formasPartoVaginal.get(k);

										bean.setValue("tipoPartoVaginal_"+i+"_"+j+"_"+k, tipoPVInfo.getAcronimo());
										forma.setMapaAntGineco("tipoPartoVaginal_"+i+"_"+j+"_"+k, tipoPVInfo.getAcronimo());

										bean.setValue("nombreTipoPartoVaginal_"+i+"_"+j+"_"+k, tipoPVInfo.getValue());
										forma.setMapaAntGineco("nombreTipoPartoVaginal_"+i+"_"+j+"_"+k, tipoPVInfo.getValue());

									}

								}

							}
				if( partoVaginal && hijo.getOtraFormaNacimientoVaginal() != null && !hijo.getOtraFormaNacimientoVaginal().equals("") )
				{
					bean.setValue("otroTipoPartoVaginal_"+i+"_"+j, hijo.getOtraFormaNacimientoVaginal());
					forma.setMapaAntGineco("otroTipoPartoVaginal_"+i+"_"+j, hijo.getOtraFormaNacimientoVaginal());

					bean.setValue("tiposParto_"+i+"_"+j, new String("3"));
					forma.setMapaAntGineco("tiposParto_"+i+"_"+j, new String("3"));

					bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Parto Vaginal"));
					forma.setMapaAntGineco("nombreTiposParto_"+i+"_"+j, new String("Parto Vaginal"));
				}
				String tempoSexo=hijo.getSexo()+"";
				if(tempoSexo!=null && !tempoSexo.equals("null") && (tempoSexo.equals(ConstantesBD.codigoSexoMasculino+"") || tempoSexo.equals(ConstantesBD.codigoSexoFemenino+"")))
				{
					bean.setValue("sexo_"+i+"_"+j,hijo.getSexo()+"");			
					forma.setMapaAntGineco("sexo_"+i+"_"+j,hijo.getSexo()+"");
				}
				else
				{
					bean.setValue("sexo_"+i+"_"+j,"-1");
					forma.setMapaAntGineco("sexo_"+i+"_"+j,"-1");
				}
				bean.setValue("peso_"+i+"_"+j, hijo.getPeso());
				forma.setMapaAntGineco("peso_"+i+"_"+j, hijo.getPeso());

				bean.setValue("lugar_"+i+"_"+j, hijo.getLugar());
				forma.setMapaAntGineco("lugar_"+i+"_"+j, hijo.getLugar());
			}
			//	Fin hijos embarazo			
		}
		//		Fin embarazos
	}

	//-------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------------

	private void cargarAntecedentesMedicamentos(Connection con, int tipoInfomacion, PersonaBasica paciente, UsuarioBasico usuario, ImpresionResumenAtencionesForm forma) throws SQLException 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();

		HashMap mp = new HashMap();

		if (tipoInfomacion == 0)  //-- Consultar si hay informacion de antecedentes alergias. 
		{
			forma.getMapaAntMedicamento().clear();

			mp.put("nroConsulta","11");
			mp.put("paciente", paciente.getCodigoPersona() + "");



			mp = mundo.consultarInformacion(con, mp);

			forma.setMapaAntMedicamento("observaciones", mp.get("observaciones_0")+"");

			if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
			{
				forma.setMapaAntMedicamento("hayAntecedentes","SI");
			}
			else
			{
				forma.setMapaAntMedicamento("hayAntecedentes","NO");
			}

		}
		else					  //-- Traer toda la informaci?n de antecedentes de alergias. 		
		{
			mp.put("nroConsulta","12");
			mp.put("paciente",paciente.getCodigoPersona()+"");



			forma.getMapaAntMedicamento().putAll(mundo.consultarInformacion(con, mp));
		}
	}

	/**
	 * 
	 * @param con
	 * @param request
	 * @param tipoInfomacion
	 * @param paciente
	 * @param usuario
	 * @param forma
	 * @throws SQLException
	 */
	private void cargarAntecedentesMedicos(Connection con, int tipoInfomacion, PersonaBasica paciente, UsuarioBasico usuario, ImpresionResumenAtencionesForm forma) throws SQLException 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();

		HashMap mp = new HashMap();

		if (tipoInfomacion == 0)  //-- Consultar si hay informacion de antecedentes alergias. 
		{
			forma.getMapaAntMedicos().clear();

			mp.put("nroConsulta","13");
			mp.put("paciente", paciente.getCodigoPersona() + "");



			mp = mundo.consultarInformacion(con, mp);

			forma.setMapaAntMedicos("observaciones", mp.get("observaciones_0")+"");

			if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
			{
				forma.setMapaAntMedicos("hayAntecedentes","SI");
			}
			else
			{
				forma.setMapaAntMedicos("hayAntecedentes","NO");
			}

		}
		else					  //-- Traer toda la informaci?n de antecedentes	
		{
			mp.put("nroConsulta","14");



			mp.put("paciente",paciente.getCodigoPersona()+"");
			forma.getMapaAntMedicos().putAll(mundo.consultarInformacion(con, mp));

			mp.put("nroConsulta","15");  //-- consultar la seccion quirurgicos



			mp = mundo.consultarInformacion(con, mp);
			forma.setMapaAntMedicos("numRegQuirur",mp.get("numRegistros")+"");
			mp.remove("numRegistros");
			forma.getMapaAntMedicos().putAll(mp);   
		}
	}


	/**
	 * 
	 * @param con
	 * @param request
	 * @param tipoInfomacion
	 * @param paciente
	 * @param usuario
	 * @param forma
	 * @throws SQLException
	 */
	private void cargarAntecedentesToxicos(Connection con, int tipoInfomacion, PersonaBasica paciente, UsuarioBasico usuario, ImpresionResumenAtencionesForm forma) throws SQLException 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();

		HashMap mp = new HashMap();
		mp.put("fechaInicio", UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial())); 
		mp.put("fechaFinal", UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal())); 
		mp.put("horaInicio", forma.getHoraInicial()); 
		mp.put("horaFinal", forma.getHoraFinal()); 

		if (tipoInfomacion == 0)  //-- Consultar si hay informacion de antecedentes alergias. 
		{
			forma.getMapaAntToxicos().clear();

			mp.put("nroConsulta","16");
			mp.put("paciente", paciente.getCodigoPersona() + ""); 

			mp = mundo.consultarInformacion(con, mp);			
			forma.setMapaAntToxicos("observaciones", mp.get("observaciones_0")+"");

			if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
			{
				forma.setMapaAntToxicos("hayAntecedentes","SI");
			}
			else
			{
				forma.setMapaAntToxicos("hayAntecedentes","NO");
			}

		}
		else//-- Traer toda la informaci?n de antecedentes	
		{
			mp.put("nroConsulta","17");
			mp.put("paciente",paciente.getCodigoPersona()+"");
			forma.setMapaAntToxicos(mundo.consultarInformacion(con, mp));

			if(!forma.getMapaAntToxicos("numRegistros").toString().equals("0"))
				forma.setMapaAntToxicos("hayAntecedentes","SI");
		}
	}


	/**
	 * Mètodo que carga la información de cuidados especiales de enfermería para mostrar 
	 * en la impresión de historia clínica de acuerdo a los parámetros de busqueda
	 * @param con
	 * @param mundoRegistroEnfer
	 * @param forma
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param filtroAsocio
	 */
	private void accionCargarSeccionCuidadosEspeciales(Connection con, RegistroEnfermeria mundoRegistroEnfer, ImpresionResumenAtencionesForm forma, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion) 
	{
		HashMap mapaCuidadosEspecialesHisto=new HashMap();
		mapaCuidadosEspecialesHisto.put("numRegistros","0");
		//----------Se consultan los posibles históricos de cuidados especiales (presenta=true or (presenta=false and descripcion!='')------------------------//
		mapaCuidadosEspecialesHisto=mundoRegistroEnfer.consultarCuidadosEspecialesHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion);


		HashMap mapaCuidadosEspecialesHistoDetalle=new HashMap();
		mapaCuidadosEspecialesHistoDetalle.put("numRegistros","0");
		if(Integer.parseInt(mapaCuidadosEspecialesHisto.get("numRegistros")+"")>0)
		{
			//----------Se consulta el detalle de los historicos de cuidados especiales (presenta=true or (presenta=false and descripcion!='')------------------------//
			mapaCuidadosEspecialesHistoDetalle=mundoRegistroEnfer.consultarCuidadosEspecialesDetalleHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion);
		}

		//----------Se crea el mapa histórico de cuidados especiales de enfermería para la impresión HC ---------//
		forma.setMapaHistoricoCuidadosEspeciales(this.formarHistoricoCuidadosEspeciales(mapaCuidadosEspecialesHisto, mapaCuidadosEspecialesHistoDetalle));

	}


	/**
	 * Mètodo que construye el mapa histórico de cuidados especiales de enfermería con los dos mapas, el que contiene
	 * los encabezados históricos y los detalles de cada encabezado
	 * @param mapaCuidadosEspecialesHisto
	 * @param mapaCuidadosEspecialesHistoDetalle
	 * @return
	 */
	private HashMap formarHistoricoCuidadosEspeciales(HashMap mapaCuidadosEspecialesHisto, HashMap mapaCuidadosEspecialesHistoDetalle) 
	{
		HashMap mapaCuidadosEspeciales=new HashMap();

		//---------Se asigna el mapa -------------//
		mapaCuidadosEspeciales=mapaCuidadosEspecialesHisto;


		if(mapaCuidadosEspeciales!=null)
		{
			int numRegistros=Integer.parseInt(mapaCuidadosEspeciales.get("numRegistros")+"");
			int numRegDetalle=Integer.parseInt(mapaCuidadosEspecialesHistoDetalle.get("numRegistros")+"");
			int contReg=0;
			String codigoEnca="",esMedico="";
			boolean entro=false;

			for(int i=0; i<numRegistros; i++)
			{
				contReg=0;
				entro=false;
				codigoEnca=mapaCuidadosEspeciales.get("codigo_enca_"+i)+"";
				esMedico=mapaCuidadosEspeciales.get("es_medico_"+i)+"";
				for(int j=0; j<numRegDetalle; j++)
				{
					if(codigoEnca.equals(mapaCuidadosEspecialesHistoDetalle.get("codigo_enca_"+j)+"") && esMedico.equals(mapaCuidadosEspecialesHistoDetalle.get("es_medico_"+j)+""))	
					{
						//------Se asigna el nombre del cuidado de enfermería ----------//
						mapaCuidadosEspeciales.put("nombre_cuidado_"+codigoEnca+"_"+contReg, mapaCuidadosEspecialesHistoDetalle.get("nombre_cuidado_"+j));

						//------Se asigna si presenta o no observaciones ----------//
						if(UtilidadTexto.getBoolean(mapaCuidadosEspecialesHistoDetalle.get("presenta_"+j)+""))
						{
							mapaCuidadosEspeciales.put("presenta_"+codigoEnca+"_"+contReg, "[SI]");
						}
						else
						{
							mapaCuidadosEspeciales.put("presenta_"+codigoEnca+"_"+contReg, "[NO]");
						}

						mapaCuidadosEspeciales.put("controlespecial_"+codigoEnca+"_"+contReg, mapaCuidadosEspecialesHistoDetalle.get("controlespecial_"+j));


						//-----------Se asignan las observaciones ----------------------//
						mapaCuidadosEspeciales.put("observaciones_"+codigoEnca+"_"+contReg, mapaCuidadosEspecialesHistoDetalle.get("observaciones_"+j));
						entro=true;
						contReg++;
					}
					else
					{
						if(entro)
						{
							mapaCuidadosEspeciales.put("numRegEnca_"+codigoEnca, contReg);
							entro=false;
							break;
						}
					}
					if(j==numRegDetalle-1)
					{
						mapaCuidadosEspeciales.put("numRegEnca_"+codigoEnca, contReg);
					}
				}//for numRegDetalle

			}//for numRegistros
		}//if mapaCuidadosEspeciales!=null

		return mapaCuidadosEspeciales;
	}

	/**
	 * Método que consulta la información de la sección Hoja Neurológica para la impresión
	 * de la historia clínica
	 * @param con
	 * @param mundoRegEnfer
	 * @param forma
	 * @param institucion
	 * @param centroCosto
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion
	 */
	private void accionCargarSeccionHojaNeurologica(Connection con, RegistroEnfermeria mundoRegEnfer,ImpresionResumenAtencionesForm forma, int institucion, int centroCosto, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion) 
	{
		//-----------Se consultan las especificaciones Glasgow por institución centro de costo-----------//
		forma.setEscalasGlasgowInstitucionCCosto(mundoRegEnfer.consultarTiposInstitucionCCosto(con,institucion, cuentas, 12));

		//----Se quitan los tipos de escala glasgow en la coleccion------//
		forma.setEscalasGlasgowInstitucionCCosto(Utilidades.coleccionSinRegistrosRepetidos(forma.getEscalasGlasgowInstitucionCCosto(), "codigo_tipo"));

		//-----Se consulta el histórico de escala glasgow de acuerdo a los parámetros de búsqueda de la impresion hc-----//
		forma.setMapaHistoricoEscalaGlasgow(mundoRegEnfer.consultarEscalaGlasgowHistoImpresionHC(con,cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion));

		//-----Se consulta el histórico de pupilas de acuerdo a los parámetros de búsqueda de la impresion hc-----//
		forma.setMapaHistoricoPupilas(mundoRegEnfer.consultarPupilasHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion));

		//----- Se consulta el histórico de convulsiones de acuerdo a los parámetros de búsqueda de la impresion hc-----//
		forma.setMapaHistoricoConvulsiones(mundoRegEnfer.consultarConvulsionesHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion));

		//----- Se consulta el histórico de control de esfinteres de acuerdo a los parámetros de búsqueda de la impresion hc-----//
		forma.setMapaHistoricoControlEsfinteres(mundoRegEnfer.consultarControlEsfinteresHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion));

		//----- Se consulta el histórico de fuerza muscular de acuerdo a los parámetros de búsqueda de la impresion hc-----//
		forma.setMapaHistoricoFuerzaMuscular(mundoRegEnfer.consultarFuerzaMuscularHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion));
	}

	/**
	 * Método que carga toda la información de la sección cirugías, incluyendo la hoja quirúrgica y
	 * la hoja de anestesia
	 * @param con
	 * @param mundo
	 * @param forma
	 * @param codigoInstitucionInt
	 * @param codigoArea
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicialBD
	 * @param fechaFinalBD
	 * @param horaInicial
	 * @param horaFinal
	 * @param filtroAsocio
	 */
	private void accionCargarSeccionCirugias(Connection con, ImpresionResumenAtenciones mundo, ImpresionResumenAtencionesForm forma, int codigoInstitucionInt, int codigoArea, String cuentas, String fechaInicialBD, String fechaFinalBD, String horaInicial, String horaFinal, String filtroAsocio,Integer numeroSolicitud) 
	{

		//---------- Método que consulta las peticiones y números de solicitud que existen de acuerdo a los parámetros de búsqueda --------------//
		//arreglo momentaneo para el asocio de cuentas, deberia permitir todas las cuentas
		//en este caso solo permite dos.
		String [] cuentastmp=cuentas.split(",");
		String cuenta="0";
		String asociada="0";
		if (cuentastmp.length>1)
			asociada=cuentastmp[1];
		cuenta=cuentastmp[0];
		//--
		forma.setMapaCodigosPeticionCirugia(mundo.consultarPeticionesCirugiaImpresionHc(con, cuenta+"", asociada+"", fechaInicialBD, fechaFinalBD, forma.getHoraInicial(), forma.getHoraFinal(), forma.getFiltroAsocio(), forma.getSolicitudesFactura()));	

		//---------- Se crea una cadena con los códigos de petición separados por coma para la consulta de las secciones -----------//
		String listadoPeticiones="";
		String listadoSolicitudes="";

		if(forma.getMapaCodigosPeticionCirugia() != null)
		{
			listadoPeticiones=obtenerListaSeparadasComa(forma.getMapaCodigosPeticionCirugia(),"codigo_peticion_");
			listadoSolicitudes=obtenerListaSeparadasComa(forma.getMapaCodigosPeticionCirugia(),"numero_solicitud_");
		}


		//----------Si no es vacío se consultan cada una de las secciones de hoja quirúrgica y hoja de anestesia -----//
		if(UtilidadCadena.noEsVacio(listadoPeticiones))
		{
			//------------ Se consultan los posibles encabezados de la hoja de anestesia -----------------//
			forma.setMapaEncabezadosHojaAnestesia(mundo.consultarEncabezadosHojaAnestesia(con, listadoPeticiones));

			//------------- Se consultan los exámenes de laboratorio de la preanestesia ----------------------//
			forma.setMapaExamenesLaboratorioPreanestesia(mundo.consultarExamenesLaboratorioPreanestesia(con, listadoPeticiones));

			//------------ Se consulta el histórico de los exámenes físico de tipo text ------------------------//
			forma.setMapaHistoExamenesFisicosText(mundo.consultarHistoExamenesFisicos(con, listadoPeticiones, true));

			//------------ Se consulta el histórico de los exámenes físico de tipo text area ------------------------//
			forma.setMapaHistoExamenesFisicosTextArea(mundo.consultarHistoExamenesFisicos(con, listadoPeticiones, false));

			//------------ Se consulta el histórico de las conclusiones de preanestesia------------------------//
			forma.setMapaHistoConclusiones(mundo.consultarHistoConclusiones(con, listadoPeticiones));

			/**************************************** HOJA DE ANESTESIA ******************************************************/
			//------------ Se consulta el histórico de balance de líquidos de la hoja de anestesia (1) ------------------------//
			//ESTA PARTE ESTA MAL
			//forma.setMapaHistoBalanceLiquidosHojaAnestesia(mundo.consultarHistoSeccionesHojaAnestesia(con, listadoSolicitudes, 1));

			//------------ Se consulta el histórico de medicamentos de la hoja de anestesia (2) ------------------------//
			//ESTA PARTE ESTA MAL
			//forma.setMapaHistoMedicamentosHojaAnestesia(mundo.consultarHistoSeccionesHojaAnestesia(con, listadoSolicitudes, 2));

			//---------------------SECCION TECNICA ANESTESIA ----------------------------------//
			HojaAnestesia mundoHojaAnestesia=new HojaAnestesia();

			//Consulta de los tipos de t?cnicas de anestesia general que tienen opciones para la instituci?n
			//@todo Cambios Anexo 526 -529
			//forma.setListadoTecAnestesiaOpcionesGral (mundoHojaAnestesia.consultarTipoParametrizado(con, codigoInstitucionInt, 1));

			//Consulta de los tipos de t?cnicas de anestesia general sin opciones para la instituci?n
			//@todo Cambios Anexo 526 -529
			//forma.setListadoTecAnestesiaGral (mundoHojaAnestesia.consultarTipoParametrizado(con, codigoInstitucionInt, 2));

			//Consulta de los tipos de t?cnicas de anestesia regional para la instituci?n
			//@todo Cambios Anexo 526 -529
			//forma.setListadoTecAnestesiaRegional (mundoHojaAnestesia.consultarTipoParametrizado(con, codigoInstitucionInt, 3));

			//---Se consulta el hist?rico de las t?cnicas de anestesia ---------------//

			//ESTA PARTE ESTA MAL
			//this.cargarTecnicaAnestesia(con, listadoSolicitudes, forma, mundo);

			//------------------ SECCION SIGNOS VITALES HOJA ANESTESIA -----------------------//
			//-------Se consultan los signos de vitales parametrizados para la instici?n --------------//
			//@todo Cambios Anexo 526 -529
			//forma.setListadoSignosVitales(mundoHojaAnestesia.consultarTipoParametrizado(con, codigoInstitucionInt, 6));

			//Se cargan los tiempos con sus respectivas observaciones en la secci?n signos vitales

			//ESTA PARTE ESTA MAL
			//this.cargarTiemposSignosVitales (con, listadoSolicitudes, forma, mundo);

			//Se cargan los valores de los signos vitales
			//ESTA PARTE ESTA MAL	
			//this.cargarValoresSignosVitales(con, listadoSolicitudes, forma, mundo);


			//------------------ Hoja Quirurgica ------------------------------------------
			cargarHojaQuirurgica(con, listadoSolicitudes, forma,codigoInstitucionInt);  

			//-- Notas Generales de Enfermeria 
			cargarNotasGeneralesEnfermeria(con, listadoSolicitudes, forma);

			//-- Notas de Recuperacion (verificar Si HAY) 
			cargarNotasRecuperacion(con, 0, codigoInstitucionInt,  listadoSolicitudes, forma);

			//-- Notas de Recuperacion 
			cargarNotasRecuperacion(con, 1, codigoInstitucionInt,  listadoSolicitudes, forma); 
		}


	}
	
	
	private void accionCargarSeccionCirugias(Connection con, ImpresionResumenAtenciones mundo, ImpresionResumenAtencionesForm forma, int codigoInstitucionInt, int codigoArea, String cuenta, String fechaInicialBD, String fechaFinalBD, String horaInicial, String horaFinal, String filtroAsocio,Integer numeroSolicitud, int i,DtoHojaQuirurgicaAnestesia dtoHojaQuirurgicaAnestesia) throws IPSException 
	{

		//---------- Método que consulta las peticiones y números de solicitud que existen de acuerdo a los parámetros de búsqueda --------------//
		//arreglo momentaneo para el asocio de cuentas, deberia permitir todas las cuentas
		//en este caso solo permite dos.
		/**
		* Tipo Modificacion: Segun incidencia 5055
		* Autor: Alejandro Aguirre Luna
		* usuario: aleagulu
		* Fecha: 25/02/2013
		* Descripcion: Se adiciona la biblioteca PersonaBasica para la utilizacion 
		* 	       del objeto PersonaBasica en la funcion 
		* 	       consultarInformacionHistoriaClinica
		**/
		forma.setMapaCodigosPeticionCirugia(mundo.consultarPeticionesCirugiaImpresionHcXNumeroSolicitud(con, cuenta+"", fechaInicialBD, fechaFinalBD, forma.getHoraInicial(), forma.getHoraFinal(), forma.getFiltroAsocio(), forma.getSolicitudesFactura(),numeroSolicitud));

		//---------- Se crea una cadena con los códigos de petición separados por coma para la consulta de las secciones -----------//
		String listadoPeticiones="";
		String listadoSolicitudes="";

		if(forma.getMapaCodigosPeticionCirugia() != null)
		{
			listadoPeticiones=obtenerListaSeparadasComa(forma.getMapaCodigosPeticionCirugia(),"codigo_peticion_");
			listadoSolicitudes=obtenerListaSeparadasComa(forma.getMapaCodigosPeticionCirugia(),"numero_solicitud_");
		}


		//----------Si no es vacío se consultan cada una de las secciones de hoja quirúrgica y hoja de anestesia -----//
		if(UtilidadCadena.noEsVacio(listadoPeticiones))
		{
			//------------ Se consultan los posibles encabezados de la hoja de anestesia -----------------//
			forma.setMapaEncabezadosHojaAnestesia(mundo.consultarEncabezadosHojaAnestesia(con, listadoPeticiones));

			//------------- Se consultan los exámenes de laboratorio de la preanestesia ----------------------//
			forma.setMapaExamenesLaboratorioPreanestesia(mundo.consultarExamenesLaboratorioPreanestesia(con, listadoPeticiones));

			//------------ Se consulta el histórico de los exámenes físico de tipo text ------------------------//
			forma.setMapaHistoExamenesFisicosText(mundo.consultarHistoExamenesFisicos(con, listadoPeticiones, true));

			//------------ Se consulta el histórico de los exámenes físico de tipo text area ------------------------//
			forma.setMapaHistoExamenesFisicosTextArea(mundo.consultarHistoExamenesFisicos(con, listadoPeticiones, false));

			//------------ Se consulta el histórico de las conclusiones de preanestesia------------------------//
			forma.setMapaHistoConclusiones(mundo.consultarHistoConclusiones(con, listadoPeticiones));

			/**************************************** HOJA DE ANESTESIA ******************************************************/
			//------------ Se consulta el histórico de balance de líquidos de la hoja de anestesia (1) ------------------------//
			//ESTA PARTE ESTA MAL
			//forma.setMapaHistoBalanceLiquidosHojaAnestesia(mundo.consultarHistoSeccionesHojaAnestesia(con, listadoSolicitudes, 1));

			//------------ Se consulta el histórico de medicamentos de la hoja de anestesia (2) ------------------------//
			//ESTA PARTE ESTA MAL
			//forma.setMapaHistoMedicamentosHojaAnestesia(mundo.consultarHistoSeccionesHojaAnestesia(con, listadoSolicitudes, 2));

			//---------------------SECCION TECNICA ANESTESIA ----------------------------------//
			HojaAnestesia mundoHojaAnestesia=new HojaAnestesia();

			//Consulta de los tipos de t?cnicas de anestesia general que tienen opciones para la instituci?n
			//@todo Cambios Anexo 526 -529
			//forma.setListadoTecAnestesiaOpcionesGral (mundoHojaAnestesia.consultarTipoParametrizado(con, codigoInstitucionInt, 1));

			//Consulta de los tipos de t?cnicas de anestesia general sin opciones para la instituci?n
			//@todo Cambios Anexo 526 -529
			//forma.setListadoTecAnestesiaGral (mundoHojaAnestesia.consultarTipoParametrizado(con, codigoInstitucionInt, 2));

			//Consulta de los tipos de t?cnicas de anestesia regional para la instituci?n
			//@todo Cambios Anexo 526 -529
			//forma.setListadoTecAnestesiaRegional (mundoHojaAnestesia.consultarTipoParametrizado(con, codigoInstitucionInt, 3));

			//---Se consulta el hist?rico de las t?cnicas de anestesia ---------------//

			//ESTA PARTE ESTA MAL
			//this.cargarTecnicaAnestesia(con, listadoSolicitudes, forma, mundo);

			//------------------ SECCION SIGNOS VITALES HOJA ANESTESIA -----------------------//
			//-------Se consultan los signos de vitales parametrizados para la instici?n --------------//
			//@todo Cambios Anexo 526 -529
			//forma.setListadoSignosVitales(mundoHojaAnestesia.consultarTipoParametrizado(con, codigoInstitucionInt, 6));

			//Se cargan los tiempos con sus respectivas observaciones en la secci?n signos vitales

			//ESTA PARTE ESTA MAL
			//this.cargarTiemposSignosVitales (con, listadoSolicitudes, forma, mundo);

			//Se cargan los valores de los signos vitales
			//ESTA PARTE ESTA MAL	
			//this.cargarValoresSignosVitales(con, listadoSolicitudes, forma, mundo);


			//------------------ Hoja Quirurgica ------------------------------------------
			/**
			 * MT 6497 Usabilidad de Hoja Qx
			 * */
			try{
				SalasCirugiaFacade salasCirugiaFacade=new SalasCirugiaFacade();
				String[] numerosSolicitudes=listadoSolicitudes.split(",");
				/**
				 * Map<Integer(codigoSolicitud),IngresoSalidaPacienteDto>
				 * */
				Map<Integer,IngresoSalidaPacienteDto>mapaIngresoSalidaPaciente=new HashMap<Integer, IngresoSalidaPacienteDto>(0);
				/**
				 * Map<Integer(codigoSolicitud),InformacionActoQxDto>
				 * */
				Map<Integer,InformacionActoQxDto>mapaInformacionActoQuirurgico=new HashMap<Integer, InformacionActoQxDto>(0);
				/**
				 * Map<Integer(codigoSolicitud),List<EspecialidadDto>>
				 * */
				Map<Integer,List<EspecialidadDto>>mapaEspecialidadesXSolicitud=new HashMap<Integer, List<EspecialidadDto>>(0);
				/**
				 * Map<Integer(codigoSolicitud),Map<Integer(codigoEspecialidad), InformeQxDto>>
				 * */
				Map<Integer,Map<Integer, InformeQxDto>>mapaDescripcionesOperatorias=new HashMap<Integer, Map<Integer,InformeQxDto>>(0);
				/**
				 * Map<Integer(codigoSolicitud),Map<Integer(codigoEspecialidad), List<NotaAclaratoriaDto>>>
				 * */
				Map<Integer,Map<Integer, List<NotaAclaratoriaDto>>>mapaNotasAclaratorias=new HashMap<Integer, Map<Integer,List<NotaAclaratoriaDto>>>(0);
				/**
				 * Map<Integer(codigoSolicitud),List<NotaEnfermeriaDto>>
				 * */
				Map<Integer, List<NotaEnfermeriaDto>>mapaNotasEnfermeria=new HashMap<Integer, List<NotaEnfermeriaDto>>(0);
				/**
				 * Map<Integer(codigoSolicitud),List<NotaRecuperacionDto>>
				 * */
				Map<Integer, List<NotaRecuperacionDto>>mapaNotasRecuperacion=new HashMap<Integer, List<NotaRecuperacionDto>>(0);
				
				for(String numSolicitud:numerosSolicitudes){
					Integer codigoSolicitud=Integer.parseInt(numSolicitud);
					
					SolicitudCirugiaDto solicitudCirugiaDto=new SolicitudCirugiaDto();
					solicitudCirugiaDto.setNumeroSolicitud(codigoSolicitud);
					
					IngresoSalidaPacienteDto ingresoSalidaPacienteDto=salasCirugiaFacade.consultarIngresoSalidaPaciente(codigoSolicitud);
					mapaIngresoSalidaPaciente.put(codigoSolicitud, ingresoSalidaPacienteDto);
					
					InformacionActoQxDto informacionActoQxDto=salasCirugiaFacade.consultarInformacionActoQx(solicitudCirugiaDto,Integer.parseInt(forma.getIdIngreso()));
					mapaInformacionActoQuirurgico.put(codigoSolicitud, informacionActoQxDto);
					
					List<EspecialidadDto>especialidades=salasCirugiaFacade.consultarEspecialidadesInformeQx(codigoSolicitud.intValue());
					mapaEspecialidadesXSolicitud.put(codigoSolicitud, especialidades);
					
					PeticionQxDto peticionQxDto=new PeticionQxDto();
					peticionQxDto.setSolicitudCirugia(solicitudCirugiaDto);
					peticionQxDto.setEsSolicitud(true);
					
					Map<Integer, InformeQxDto> mapaInformeQx=null;
					Map<Integer, List<NotaAclaratoriaDto>> mapaNotasAclaratoriasXEspecialidad=null;
					for (EspecialidadDto especialidadDto : especialidades) {
						InformeQxDto informeQxDto=salasCirugiaFacade.consultarProfesionalesInformeQx(peticionQxDto, especialidadDto, null, codigoInstitucionInt);
						if(mapaInformeQx==null){
							mapaInformeQx=new HashMap<Integer, InformeQxDto>(0);
						}
						mapaInformeQx.put(especialidadDto.getCodigo(), informeQxDto);
						mapaDescripcionesOperatorias.put(codigoSolicitud, mapaInformeQx);
						
						List<NotaAclaratoriaDto>notasAclaratorias=salasCirugiaFacade.consultarNotasAclaratorias(informeQxDto.getInformeQxEspecialidad().getCodigo(),true);
						mapaNotasAclaratoriasXEspecialidad=mapaNotasAclaratorias.get(codigoSolicitud);
						if(mapaNotasAclaratoriasXEspecialidad==null){
							mapaNotasAclaratoriasXEspecialidad=new HashMap<Integer, List<NotaAclaratoriaDto>>(0);
						}
						mapaNotasAclaratoriasXEspecialidad.put(especialidadDto.getCodigo(), notasAclaratorias);
						mapaNotasAclaratorias.put(codigoSolicitud, mapaNotasAclaratoriasXEspecialidad);
						
					}
					
					List<NotaEnfermeriaDto>notasEnfermeria=salasCirugiaFacade.consultarNotasEnfermeria(codigoSolicitud,true);
					List<NotaRecuperacionDto>notasRecuperacion=salasCirugiaFacade.consultarNotasRecuperacion(codigoInstitucionInt, numeroSolicitud, false,true);
					
					mapaNotasEnfermeria.put(codigoSolicitud, notasEnfermeria);
					mapaNotasRecuperacion.put(codigoSolicitud, notasRecuperacion);
					
					/**
					 * CONSULTAR NOTAS DE RECUPERACION DE ANTECEDENTES DE ALERGIAS?
					 * */
					
				}
				
				dtoHojaQuirurgicaAnestesia.setMapaIngresoSalidaPaciente(mapaIngresoSalidaPaciente);
				dtoHojaQuirurgicaAnestesia.setMapaInformacionActoQuirurgico(mapaInformacionActoQuirurgico);
				dtoHojaQuirurgicaAnestesia.setMapaEspecialidadesXSolicitud(mapaEspecialidadesXSolicitud);
				dtoHojaQuirurgicaAnestesia.setMapaDescripcionesOperatorias(mapaDescripcionesOperatorias);
				dtoHojaQuirurgicaAnestesia.setMapaNotasAclaratorias(mapaNotasAclaratorias);
				dtoHojaQuirurgicaAnestesia.setMapaNotasEnfermeria(mapaNotasEnfermeria);
				dtoHojaQuirurgicaAnestesia.setMapaNotasRecuperacionCirugia(mapaNotasRecuperacion);
				
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				throw e;
			} catch (IPSException e) {
				// TODO Auto-generated catch block
				throw e;
			}
			cargarHojaQuirurgica(con, listadoSolicitudes, forma,codigoInstitucionInt);  

			//-- Notas Generales de Enfermeria 
			cargarNotasGeneralesEnfermeria(con, listadoSolicitudes, forma);

			//-- Notas de Recuperacion (verificar Si HAY) 
			cargarNotasRecuperacion(con, 0, codigoInstitucionInt,  listadoSolicitudes, forma);

			//-- Notas de Recuperacion 
			cargarNotasRecuperacion(con, 1, codigoInstitucionInt,  listadoSolicitudes, forma); 
		}


	}
	

	/**
	 * Metodo para consultar y cargar la informaci?n de la secci?n T?cnica de Anestesia
	 * @param con
	 * @param listadoSolicitudes
	 * @param forma
	 * @param mundo
	 * @return true si existe informaci?n de t?cnicas de anestesia
	 */
	public boolean cargarTecnicaAnestesia(Connection con, String listadoSolicitudes, ImpresionResumenAtencionesForm forma, ImpresionResumenAtenciones mundo) 
	{
		Collection colTecAnestesia=null;

		try
		{
			colTecAnestesia=mundo.consultarHistoTecnicaAnestesia(con, listadoSolicitudes);
			Iterator ite=colTecAnestesia.iterator();
			int cont=0;
			for (int i=0; i<colTecAnestesia.size(); i++)
			{
				if (ite.hasNext())
				{
					HashMap colTecAnest=(HashMap) ite.next();
					forma.setMapaHistoTecAnestesia("valorTecAnestesia_"+colTecAnest.get("codigo")+"_"+colTecAnest.get("numero_solicitud"), colTecAnest.get("valor"));
					forma.setMapaHistoTecAnestesia("numero_solicitud_"+cont, colTecAnest.get("numero_solicitud"));
					cont++;
				}
			}//for
			forma.setMapaHistoTecAnestesia("numRegistros",(Object)cont);

		}
		catch(Exception e)
		{
			logger.warn("Error al Consultar la t?cnica de anestesia en el mundo" +e.toString());
			colTecAnestesia=null;
		}

		return false;
	}

	/**
	 * Metodo para consultar y cargar los tiempos con sus respectivas observaciones en la secci?n signos vitales
	 * @param con
	 * @param listadoSolicitudes
	 * @param forma
	 * @param mundo
	 * @return true si existe informaci?n de los tiempos de signos vitales
	 */
	public boolean cargarTiemposSignosVitales(Connection con, String listadoSolicitudes, ImpresionResumenAtencionesForm forma, ImpresionResumenAtenciones mundo) 
	{
		Collection colTiemposSVitales=null;

		try
		{
			colTiemposSVitales=mundo.consultarHistoSignosVitales (con, listadoSolicitudes, 1);
			Iterator ite=colTiemposSVitales.iterator();
			int cont=0;
			for (int i=0; i<colTiemposSVitales.size(); i++)
			{
				if (ite.hasNext())
				{
					HashMap colTSVital=(HashMap) ite.next();
					if(colTSVital.get("numero_tiempo") != null && !colTSVital.get("numero_tiempo").equals(""))
					{
						forma.setMapaHistoSignosVitales("tiempoSigVital_"+colTSVital.get("numero_tiempo")+"_"+colTSVital.get("numero_solicitud"), colTSVital.get("valor_tiempo"));
						forma.setMapaHistoSignosVitales("observacionSigVital_"+colTSVital.get("numero_tiempo")+"_"+colTSVital.get("numero_solicitud"), colTSVital.get("observaciones"));
						forma.setMapaHistoSignosVitales("numero_solicitudTiempo_"+cont, colTSVital.get("numero_solicitud"));
						cont++;
					}
				}//if
			}//for
			forma.setMapaHistoSignosVitales("numRegistrosTiempos",(Object)cont);
		}//try
		catch(Exception e)
		{
			logger.warn("Error al Consultar los tiempos de los signos vitales" +e.toString());
			colTiemposSVitales=null;
		}
		return false;
	}

	/**
	 * Metodo para consultar y cargar los valores de los signos vitales para cada tiempo
	 * @param con
	 * @param listadoSolicitudes
	 * @param forma
	 * @param mundo
	 * @return true si existe informaci?n de los valores de los signos vitales
	 */
	public boolean cargarValoresSignosVitales(Connection con, String listadoSolicitudes, ImpresionResumenAtencionesForm forma, ImpresionResumenAtenciones mundo) 
	{
		Collection colValoresSVitales=null;

		try
		{
			colValoresSVitales=mundo.consultarHistoSignosVitales (con, listadoSolicitudes, 2);
			Iterator ite=colValoresSVitales.iterator();
			int cont=0;
			for (int i=0; i<colValoresSVitales.size(); i++)
			{
				if (ite.hasNext())
				{
					HashMap colVSVital=(HashMap) ite.next();
					if(colVSVital.get("numero_tiempo") != null && !colVSVital.get("numero_tiempo").equals("") && colVSVital.get("signo_vital_inst") != null  && !colVSVital.get("signo_vital_inst").equals(""))
					{
						forma.setMapaHistoSignosVitales("valorSigVital_"+colVSVital.get("signo_vital_inst")+"_"+colVSVital.get("numero_tiempo")+"_"+colVSVital.get("numero_solicitud"), colVSVital.get("valor_signo_vital"));
						forma.setMapaHistoSignosVitales("numero_solicitudValor_"+cont, colVSVital.get("numero_solicitud"));
						cont++;
					}
				}//if
			}//for
			forma.setMapaHistoSignosVitales("numRegistrosValores",(Object)cont);
		}//try
		catch(Exception e)
		{
			logger.warn("Error al Consultar los valores para cada signo vital en cada tiempo" +e.toString());
			colValoresSVitales=null;
		}

		return false;
	}

	/**
	 * Método que obtiene la lista de las peticiones o solicitudes, enviando el indice
	 * organiza en un String separado por comas y poderlo enviar a la consulta de cada una de las secciones
	 * @param mapaCodigosPeticionCirugia
	 * @param indiceMapa
	 * @return 
	 */
	private String obtenerListaSeparadasComa(HashMap mapaCodigosPeticionCirugia, String indice) 
	{
		StringBuffer listadoPeticiones=new StringBuffer();
		if(mapaCodigosPeticionCirugia != null && UtilidadCadena.noEsVacio(mapaCodigosPeticionCirugia.get("numRegistros")+""))
		{
			int numRegistros=Integer.parseInt(mapaCodigosPeticionCirugia.get("numRegistros")+"");
			int cont=0;

			for(int i=0; i<numRegistros; i++)
			{
				if(cont==0)
				{
					listadoPeticiones.append(mapaCodigosPeticionCirugia.get(indice+i)+"");
				}
				else
				{
					listadoPeticiones.append(","+mapaCodigosPeticionCirugia.get(indice+i)+"");
				}
				cont++;

			}//for
			return listadoPeticiones.toString();
		}//if mapaCodigosPeticionCirugia!=nul
		else
			return "";
	}

	/**
	 * 
	 * @param con
	 * @param request
	 * @param tipoInfomacion
	 * @param paciente
	 * @param usuario
	 * @param forma
	 * @throws SQLException
	 */
	private void cargarAntecedentesTransfusionales(Connection con, int tipoInfomacion, PersonaBasica paciente, UsuarioBasico usuario, ImpresionResumenAtencionesForm forma) throws SQLException  
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		HashMap mp = new HashMap();

		if (tipoInfomacion == 0)  //-- Consultar si hay informacion de antecedentes alergias. 
		{
			forma.getMapaAntTransfusionales().clear();

			mp.put("nroConsulta","18");
			mp.put("paciente", paciente.getCodigoPersona() + "");



			mp = mundo.consultarInformacion(con, mp);

			forma.setMapaAntTransfusionales("observaciones", mp.get("observaciones_0")+"");

			if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
			{
				forma.setMapaAntTransfusionales("hayAntecedentes","SI");
			}
			else
			{
				forma.setMapaAntTransfusionales("hayAntecedentes","NO");
			}

		}
		else					  //-- Traer toda la informaci?n de antecedentes	
		{
			mp.put("nroConsulta","19");
			mp.put("paciente",paciente.getCodigoPersona()+"");

			forma.setMapaAntTransfusionales(mundo.consultarInformacion(con, mp));
			
			//hermorhu - MT6903
			if(Integer.parseInt(forma.getMapaAntTransfusionales("numRegistros").toString()) > 0 ) {
				forma.setMapaAntTransfusionales("hayAntecedentes","SI");
			}
		}
	}	

	/**
	 * 
	 * @param con
	 * @param request
	 * @param tipoInfomacion
	 * @param paciente
	 * @param usuario
	 * @param forma
	 * @throws SQLException
	 */
	private void cargarAntecedentesVacunas(Connection con,  int tipoInfomacion, PersonaBasica paciente, UsuarioBasico usuario, ImpresionResumenAtencionesForm forma) throws SQLException  
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		HashMap mp = new HashMap();

		if (tipoInfomacion == 0)  //-- Consultar si hay informacion de antecedentes alergias. 
		{
			forma.getMapaVacunas().clear();

			mp.put("nroConsulta","20");
			mp.put("paciente", paciente.getCodigoPersona() + "");



			mp = mundo.consultarInformacion(con, mp);

			forma.setMapaVacunas("observaciones", mp.get("observaciones_0")+"");

			if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
			{
				forma.setMapaVacunas("hayAntecedentes","SI");
			}
			else
			{
				forma.setMapaVacunas("hayAntecedentes","NO");
			}

		}
		else					  //-- Traer toda la informaci?n de antecedentes	
		{
			HashMap mapaParam = new HashMap(); 
			AntecedentesVacunas mundoVacu = new AntecedentesVacunas();


			mapaParam.put("codigoPaciente", paciente.getCodigoPersona()+"");
			mapaParam.put("filtroAsocio",forma.getFiltroAsocio()+"");

			mapaParam.put("nroConsulta","1"); 
			//----Consultar los tipos de inmunizaci?n de los antecedentes de vacunas
			forma.setMapaTiposInmunizacion(mundoVacu.consultarInformacion(con, mapaParam));

			//---- Establecer el numero de la consulta 2.
			mapaParam.put("nroConsulta","2");


			HashMap mapaTemp=new HashMap();
			mapaTemp.putAll(mundoVacu.consultarInformacion(con, mapaParam));

			//------Si el n?mero de registros es igual a 1 se realiza la consulta de los datos ------------//
			if(UtilidadCadena.noEsVacio(mapaTemp.get("numRegistros")+""))
			{
				if(Integer.parseInt(mapaTemp.get("numRegistros")+"")==1)
				{
					//------Se asignan las observaciones generales al form --------//
					forma.setMapaVacunas("observaciones", mapaTemp.get("observaciones_0")+"");

					mapaParam.put("nroConsulta","3");
					mapaParam.put("codigoPaciente", paciente.getCodigoPersona() + "");

					//----Se consulta la posible informaci?n guardada al paciente de dosis, refuerzo y comentarios de las vacunas ----//
					forma.setMapaVacunas(mundoVacu.consultarInformacion(con, mapaParam));
					
					//hermorhu - MT6903
					if(Integer.parseInt(forma.getMapaVacunas("numRegistros").toString()) > 0) {
						forma.setMapaVacunas("hayAntecedentes","SI");
					}
					
				}
			}	
		}
	}		

	/**
	 * 
	 * @param con
	 * @param request
	 * @param tipoInfomacion
	 * @param paciente
	 * @param usuario
	 * @param forma
	 * @throws SQLException
	 */
	private void cargarAntecedentesVarios(Connection con, int tipoInfomacion, PersonaBasica paciente, UsuarioBasico usuario, ImpresionResumenAtencionesForm forma) throws SQLException  
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		HashMap mp = new HashMap();

		/*mp.put("fechaInicio", UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial())); 
		mp.put("fechaFinal", UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal())); 
		mp.put("horaInicio", forma.getHoraInicial()); 
		mp.put("horaFinal", forma.getHoraFinal());*/ 
		
		
		//hermorhu - MT6866
		//caso en que la via ingreso del paciente sea Consulta Externa los antecendentes los debe traer hasta la fecha y hora de la ultima valoracion
		if(!forma.getViaIngreso().isEmpty() && Integer.parseInt(forma.getViaIngreso()) == ConstantesBD.codigoViaIngresoConsultaExterna) {
			
			InfoIngresoDto ultimaValoracion = mundo.obtenerDatosUltimaValoracionConsultaExternaXCuenta(con, Integer.parseInt(forma.getCuenta()));
			
			if(ultimaValoracion != null && forma.getIngresoSelecccionado().getFechaEgreso() != null) {
				
				String fechaUltimaValoracion = UtilidadFecha.conversionFormatoFechaAAp(ultimaValoracion.getFechaEgreso());
				String fechaEgreso = UtilidadFecha.conversionFormatoFechaAAp(forma.getIngresoSelecccionado().getFechaEgreso());
				
				if(UtilidadFecha.compararFechas(fechaUltimaValoracion, ultimaValoracion.getHoraEgreso(), fechaEgreso, forma.getIngresoSelecccionado().getHoraEgreso()).isTrue()){		
					mp.put(SqlBaseImpresionResumenAtencionesDao.KEY_FECHA_EGRESO, ultimaValoracion);
				}else {
					mp.put(SqlBaseImpresionResumenAtencionesDao.KEY_FECHA_EGRESO, forma.getIngresoSelecccionado());
				}	
			}else {
				mp.put(SqlBaseImpresionResumenAtencionesDao.KEY_FECHA_EGRESO, forma.getIngresoSelecccionado());
			}
			
		}else {
			mp.put(SqlBaseImpresionResumenAtencionesDao.KEY_FECHA_EGRESO, forma.getIngresoSelecccionado());		
		}


		if (tipoInfomacion == 0)  //-- Consultar si hay informacion de antecedentes alergias. 
		{
			forma.getMapaAntOtros().clear();

			mp.put("nroConsulta","21");
			mp.put("paciente", paciente.getCodigoPersona() + "");



			mp = mundo.consultarInformacion(con, mp);

			forma.setMapaAntOtros("observaciones", mp.get("observaciones_0")+"");

			if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
			{
				forma.setMapaAntOtros("hayAntecedentes","SI");
			}
			else
			{
				forma.setMapaAntOtros("hayAntecedentes","NO");
			}

		}
		else					  //-- Traer toda la informaci?n de antecedentes	
		{
			mp.put("nroConsulta","22");
			mp.put("paciente",paciente.getCodigoPersona()+"");



			forma.setMapaAntOtros(mundo.consultarInformacion(con, mp));

			if ( UtilidadCadena.vIntMayor(forma.getMapaAntOtros("numRegistros")+"") )
				forma.setMapaAntOtros("hayAntecedentes","SI");
			else
				forma.setMapaAntOtros("hayAntecedentes","NO");
		}
	}

	/**
	 * @param mapping
	 * @param form
	 * @param request
	 * @throws Exception
	 */
	private void cargarAntecedentesPediatricos(Connection con,  int tipoInfomacion, PersonaBasica paciente, UsuarioBasico usuario, ImpresionResumenAtencionesForm forma) throws SQLException 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		AntecedentePediatrico antecedente	= new AntecedentePediatrico();
		HashMap mp = new HashMap();

		mp.put("fechaInicio", UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial())); 
		mp.put("fechaFinal", UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal())); 
		mp.put("horaInicio", forma.getHoraInicial()); 
		mp.put("horaFinal", forma.getHoraFinal()); 


		if (tipoInfomacion == 0)  //-- Consultar si hay informacion de antecedentes. 
		{
			forma.getMapaAntPediatricos().clear();


			mp.put("nroConsulta","23");
			mp.put("paciente", paciente.getCodigoPersona() + "");
			mp = mundo.consultarInformacion(con, mp);

			forma.setMapaAntPediatricos("observaciones", mp.get("observaciones_0")+"");

			if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
			{
				forma.setMapaAntPediatricos("hayAntecedentes","SI");
			}
			else
			{
				forma.setMapaAntPediatricos("hayAntecedentes","NO");
			}

		}
		else					  //-- Traer toda la informaci?n de antecedentes	
		{
			//--Si el paciente tiene ya antecedente:
			//--boolean estaAntecedentePaciente = antecedente.cargar2( con, paciente.getCodigoPersona() );
			HashMap mx = new HashMap(); 
			mp.put("nroConsulta","37");
			mp.put("paciente", paciente.getCodigoPersona() + "");

			mx = mundo.consultarInformacion(con, mp);
			forma.setMapaAntPediatricos("numRegPat", mx.get("numRegistros")+"");
			mx.remove("numRegistros");
			forma.getMapaAntPediatricos().putAll(mx);


			boolean estaAntecedentePaciente = antecedente.cargarResumenAtencion( con, mp );

			if(estaAntecedentePaciente)
			{

				antecedente.setInfoMadre(new InfoMadre() );
				antecedente.getInfoMadre().cargar(con, paciente.getCodigoPersona() );
				antecedente.setInfoPadre(new InfoPadre() );
				antecedente.getInfoPadre().cargar(con, paciente.getCodigoPersona() );
				antecedente.setPaciente(paciente);

				cargarAntPediatricos(antecedente, forma);

				forma.setAntPed(antecedente);
			}
		}	
	}

	/**
	 * Metodo para cargar los mapas de antecedentes Pediatricos 
	 *
	 */
	void cargarAntPediatricos(AntecedentePediatrico antecedente, ImpresionResumenAtencionesForm forma)
	{
		int size = 0;

		if (antecedente.getTiposParto()!=null)
			size = antecedente.getTiposParto().size();

		for(int i = 0;  i < size; i++)
		{
			InfoDatos tipoParto	= (InfoDatosBD) antecedente.getTiposParto().get(i);
			String codigoParto	= tipoParto.getCodigo()+"";

			forma.getTiposParto().put(codigoParto + "", codigoParto + "-" + tipoParto.getValue() );
			forma.getMotivosTiposParto().put(codigoParto + "", tipoParto.getDescripcion() );

			forma.getTiposPartoList().add(tipoParto);
		}

		if( antecedente.getOtroTipoParto() != null )
		{
			/* Si tiene escrito un motivo de este tipo de parto se debe incluir en el Map */
			forma.getTiposParto().put("0", "0-Otro");

			/* Se debe meter su comentario en un cuadernito para que vea lo anterior que sabemos que no es modificable */
			forma.getMotivosTiposParto().put("0", antecedente.getMotivoTipoPartoOtro() );

			InfoDatos tipoParto	= new InfoDatosBD(0, "Otro", antecedente.getMotivoTipoPartoOtro());
			forma.getTiposPartoList().add(tipoParto);
		}


		forma.getTiposPartoCarga().putAll(forma.getTiposParto());
		forma.getMotivosTiposPartoCarga().putAll(forma.getMotivosTiposParto());
	}

	/**
	 * 
	 * @param con
	 * @param request
	 * @param tipoInfomacion
	 * @param paciente
	 * @param usuario
	 * @param forma
	 * @throws SQLException
	 */
	private void cargarAntecedentesOdontologicos(Connection con,  int tipoInfomacion, PersonaBasica paciente, UsuarioBasico usuario, ImpresionResumenAtencionesForm forma) throws SQLException 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		HashMap mp = new HashMap();

		mp.put("fechaInicio", UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial())); 
		mp.put("fechaFinal", UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal())); 
		mp.put("horaInicio", forma.getHoraInicial()); 
		mp.put("horaFinal", forma.getHoraFinal()); 


		if (tipoInfomacion == 0)  //-- Consultar si hay informacion de antecedentes alergias. 
		{
			forma.getMapaAntOdonto().clear();

			mp.put("nroConsulta","24");
			mp.put("paciente", paciente.getCodigoPersona() + "");




			mp = mundo.consultarInformacion(con, mp);

			forma.setMapaAntOdonto("observaciones", mp.get("observaciones_0")+"");

			if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
			{
				forma.setMapaAntOdonto("hayAntecedentes","SI");
			}
			else
			{
				forma.setMapaAntOdonto("hayAntecedentes","NO");
			}

		}
		else					  //-- Traer toda la informaci?n de antecedentes	
		{
			mp.put("nroConsulta","25");
			mp.put("paciente",paciente.getCodigoPersona()+"");
			mp.put("institucion",usuario.getCodigoInstitucionInt()+"");



			forma.getMapaAntOdonto().putAll(mundo.consultarInformacion(con, mp));

			HashMap mx = new HashMap();
			mp.put("nroConsulta","26");
			mp.put("paciente",paciente.getCodigoPersona()+"");
			mp.put("institucion",usuario.getCodigoInstitucionInt()+"");
			mx = mundo.consultarInformacion(con, mp);
			forma.setMapaAntOdonto("numRegT", mx.get("numRegistros")+"");
			mx.remove("numRegistros");
			forma.getMapaAntOdonto().putAll(mx);

			mx.clear();
			mp.put("nroConsulta","27");
			mp.put("paciente",paciente.getCodigoPersona()+"");
			mx = mundo.consultarInformacion(con, mp);
			forma.setMapaAntOdonto("numRegP", mx.get("numRegistros")+"");
			mx.remove("numRegistros");
			forma.getMapaAntOdonto().putAll(mx);

		}
	}


	/**
	 * 
	 * @param con
	 * @param request
	 * @param tipoInfomacion
	 * @param paciente
	 * @param usuario
	 * @param forma
	 * @throws SQLException
	 */
	private void cargarHojaQuirurgica (Connection con, String solicitudes, ImpresionResumenAtencionesForm forma,int codinstitu) 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		HashMap mp = new HashMap();
		HashMap mx = new HashMap();

		mp.put("solicitudes", solicitudes); 
		forma.getMapaHojaQuirur().clear();

		//-- Consultar la informacion de encabezado.	

		mp.put("nroConsulta","28");
		mx = mundo.consultarInformacion(con, mp);
		forma.setMapaHojaQuirur("numRegEnca", mx.get("numRegistros")+"");
		mx.remove("numRegistros");
		forma.getMapaHojaQuirur().putAll(mx);

		//-- Consultar los diagnosticos del encabezado 	

		mp.put("nroConsulta","29");
		mx = mundo.consultarInformacion(con, mp);
		forma.setMapaHojaQuirur("numRegDiag", mx.get("numRegistros")+"");
		mx.remove("numRegistros");
		forma.getMapaHojaQuirur().putAll(mx);

		//-- Consultar la informacion de las cirugias (SERVICIOS) 	

		mp.put("nroConsulta","30");
		mp.put("institucion",codinstitu);
		mx = mundo.consultarInformacion(con, mp);
		forma.setMapaHojaQuirur("numRegCir", mx.get("numRegistros")+"");
		mx.remove("numRegistros");
		forma.getMapaHojaQuirur().putAll(mx);

		//-- Consultar la informacion de las cirugias (SERVICIOS) 	

		mp.put("nroConsulta","31");
		mx = mundo.consultarInformacion(con, mp);
		forma.setMapaHojaQuirur("numRegDs", mx.get("numRegistros")+"");
		mx.remove("numRegistros");
		forma.getMapaHojaQuirur().putAll(mx);

		//-- Consultar la informacion Quirurgica de Todas Las Hojas 	

		mp.put("nroConsulta","32");
		mx = mundo.consultarInformacion(con, mp);
		forma.setMapaHojaQuirur("numRegIq", mx.get("numRegistros")+"");
		mx.remove("numRegistros");
		forma.getMapaHojaQuirur().putAll(mx);

		//-- Consultar la informacion de Observaciones Generales y Patologias 	

		mp.put("nroConsulta","33");
		mx = mundo.consultarInformacion(con, mp);
		forma.setMapaHojaQuirur("numRegOb", mx.get("numRegistros")+"");
		mx.remove("numRegistros");
		forma.getMapaHojaQuirur().putAll(mx);

	}

	/**
	 * 
	 * @param con
	 * @param solicitudes
	 * @param forma
	 */
	private void cargarNotasGeneralesEnfermeria(Connection con, String solicitudes, ImpresionResumenAtencionesForm forma) 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		HashMap mp = new HashMap();

		mp.put("solicitudes", solicitudes); 
		forma.getMapaNotasEnfer().clear();

		//-- Consultar Toda la informacion 
		mp.put("nroConsulta","34");
		forma.setMapaNotasEnfer(mundo.consultarInformacion(con, mp));
	}

	/**
	 * 
	 * @param con
	 * @param solicitudes
	 * @param forma
	 */
	private void cargarNotasRecuperacion(Connection con, int tipoInformacion, int institucion, String solicitudes, ImpresionResumenAtencionesForm forma) 
	{
		ImpresionResumenAtenciones mundo = new ImpresionResumenAtenciones();
		HashMap mp = new HashMap();
		try {
			if (tipoInformacion == 0)  //-- Consultar si hay informacion de antecedentes alergias. 
			{
				forma.getMapaNotasRecuperacion().clear();

				mp.put("nroConsulta","35");
				mp.put("solicitudes", solicitudes);
				mp = mundo.consultarInformacion(con, mp);

				if ( UtilidadCadena.vIntMayor(mp.get("numRegistros")+"") )
				{
					forma.setMapaNotasRecuperacion("hayNotasRecuperacion","SI");
				}
				else
				{
					forma.setMapaNotasRecuperacion("hayNotasRecuperacion","NO");
				}

				forma.setMapaNotasRecuperacion("numRegObser", mp.get("numRegistros")+"");
				mp.remove("numRegistros");
				forma.getMapaNotasRecuperacion().putAll(mp);
			}
			else	//-- Traer toda la informaci?n de antecedentes	
			{
				mp = new HashMap();
				mp.put("nroConsulta","36");
				mp.put("solicitudes", solicitudes);
				mp.put("institucion", institucion+"");
				forma.getMapaNotasRecuperacion().putAll(mundo.consultarInformacion(con, mp));

				forma.setInformacionNotasRecuperacion(mundo.consultarTiposNotasRecuperacion(con, Integer.valueOf(solicitudes)));

			}

		} catch (NumberFormatException e) {
			logger.error("error consultando notas de recuperacion "+e.getMessage());
		} catch (SQLException e) {
			logger.error("error consultando notas de recuperacion"+e.getMessage());
		}
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	private void obtenerEnlacesValoracionesConsultaExterna(Connection con, HashMap vo, ImpresionResumenAtencionesForm forma, UsuarioBasico usuario, PersonaBasica paciente)
	{
		HashMap mapa= ImpresionResumenAtenciones.obtenerEnlacesValoracionesConsultaExterna(con, vo);
		forma.setMapaEnlacesValoracionCE("numRegistros", mapa.get("numRegistros").toString());
		for (int w=0; w<Integer.parseInt(mapa.get("numRegistros").toString()); w++)
		{

			forma.setMapaEnlacesValoracionCE("solicitud_"+w, mapa.get("numerosolicitud_"+w));
			forma.setMapaEnlacesValoracionCE("codigo_especialidad_"+w, mapa.get("codigo_especialidad_"+w));
			forma.setMapaEnlacesValoracionCE("codigo_cita_"+w, mapa.get("codigo_cita_"+w));
			forma.setMapaEnlacesValoracionCE("tipo_servicio_"+w, mapa.get("tipo_servicio_"+w));
			if(forma.getMapaEnlacesValoracionCE("tipo_servicio_"+w).toString().equals(ConstantesBD.codigoServicioInterconsulta+""))
				forma.setMapaEnlacesValoracionCE("enlace_"+w,"../valoracionConsulta/valoracion.do?estado=imprimir&vieneDeHistoriaAtenciones=true&numeroSolicitud="+mapa.get("numerosolicitud_"+w)+"&codigoEspecialidad="+mapa.get("codigo_especialidad_"+w)+"&codigoCita="+mapa.get("codigo_cita_"+w));			
			else
				forma.setMapaEnlacesValoracionCE("enlace_"+w,"");

			/**
			 * NOTA * PENDIENTE DESARROLLAR LA IMPRESION DE LOS DEMAS TIPOS DE SERVICIOS
			 */
		}
	}


	///************************************************************************************************************************************************************************************************************************************/////////////
	/**
	 * 
	 * @param con 
	 * @param forma
	 * @param paciente 
	 */
	private DtoFiltroImpresionHistoriaClinica cargarFiltroBusquedaImpresionHistoriaClinica(Connection con, ImpresionResumenAtencionesForm forma, PersonaBasica paciente) 
	{
		String fechaInicialBD="", fechaFinalBD="";
		String horaInicial = "";
		String horaFinal = "";
		
		if(forma.isEsBusquedaResumen() || forma.isImpresionPop()) {
			if(UtilidadCadena.noEsVacio(forma.getFechaInicial()))
				fechaInicialBD=UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial());
	
			if(UtilidadCadena.noEsVacio(forma.getFechaFinal()))
				fechaFinalBD=UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal());
			
			horaInicial = forma.getHoraInicial(); 
			horaFinal = forma.getHoraFinal();		
		}	

		//------------------Se obtiene cuales ser?an las cuentas de asocio y de hospitalizaci?n -------------//
		int codigoCuenta=Integer.parseInt(forma.getCuenta());

		logger.info("valor del codigo cuenta >> "+codigoCuenta+" "+forma.getCuenta());

		int cuentaAsociada=UtilidadValidacion.tieneCuentaAsociada(con, Integer.parseInt(forma.getIdIngreso()));

		/* Si existe cuenta de asocio 
		 	Cuendo hay cuenta de asocio la cuenta activa es la de hospitalizaci?n
		 	y la cuenta de urgencias es la cuenta de asocio 
		 */
		if(UtilidadTexto.isEmpty(forma.getFiltroAsocio())){
			forma.setFiltroAsocio("");
		}
		
		if(cuentaAsociada!=0)
		{
			if(forma.getFiltroAsocio().equals("U"))
			{
				codigoCuenta=cuentaAsociada;
			}
			else if(forma.getFiltroAsocio().equals("HH") || forma.getFechaInicial().equals("HA"))
			{
				cuentaAsociada=codigoCuenta;
			}
		}


		//------------------ SE INICIA CON LA EVALUACION DE LAS SECCIONES PARA SABER QUE IMPRIMO. ---------------------------------------------//


		DtoFiltroImpresionHistoriaClinica dto=new DtoFiltroImpresionHistoriaClinica();
		
		ResumenAtenciones resumenAtenciones = new ResumenAtenciones();

//		fechaInicialBD="";
//		fechaFinalBD="";
		dto.setIngreso(Utilidades.convertirAEntero(forma.getIdIngreso()));
		dto.setCuenta(Utilidades.convertirAEntero(forma.getCuenta()));
		dto.setCuentaAsociada(cuentaAsociada);
		dto.setFechaInicial(fechaInicialBD);
		dto.setFechaFinal(fechaFinalBD);
		dto.setHoraInicial(horaInicial);
		dto.setHoraFinal(horaFinal);

		/*
		 * VALORACIONES.
		 */
		if(UtilidadTexto.getBoolean(forma.getSecciones("valoracionInicial")+""))
		{
			/*
			 * SE EVALUA SI SE IMPRIMEN LAS DOS VALORACIONES(URGENCIAS-HOSPITALIZACION) O SOLO LA DE UNA VIA DE INGRESOS.
			 */

			if(UtilidadTexto.isEmpty(forma.getFiltroAsocio())){
				forma.setFiltroAsocio("");
			}

			if(forma.getFiltroAsocio().trim().equals("A"))
			{
				String[] aten=forma.getAtenciones().split(",");
				for(int i=0;i<aten.length;i++)
				{
					/**
					* Tipo Modificacion: Segun incidencia 6496
					* Autor: Alejandro Aguirre Luna
					* Fecha: 15/02/2013
					* Descripcion: Se deja en comentario las siguientes lineas debido a que no se tenia en cuenta cuando se seleccionaba la valoración inicial pero sin que fuera de varios ingresos. 
					**/
					/*if(aten[i].equals(ImpresionResumenAtencionesAction.codigoSeccionValoracionInicialVariosIngresos+"") || aten[i].equals(ImpresionResumenAtencionesAction.codigoSeccionValoracionInicial+"A")  || aten[i].equals(ImpresionResumenAtencionesAction.codigoSeccionValoracionInicial+""))
					{
						forma.setImprimirVIUrgencias(true);
					}*/
					if(aten[i].equals(ImpresionResumenAtencionesAction.codigoSeccionValoracionInicialVariosIngresos+"") || aten[i].equals(ImpresionResumenAtencionesAction.codigoSeccionValoracionInicial+"A"))
					{
						forma.setImprimirVIUrgencias(true);
					}
					
					if((aten[i].equals(ImpresionResumenAtencionesAction.codigoSeccionValoracionInicial+"") && !aten[i].contains("A")) || (aten[i].equals(ImpresionResumenAtencionesAction.codigoSeccionValoracionInicialVariosIngresos+"")))
					{
						//MT-5571
						try
						{
							if(!SqlBaseResumenAtencionesDao.valoracionHospEsCopiaValoracionUrg(con,Utilidades.convertirAEntero(forma.getCuenta())) ||
									!resumenAtenciones.numeroObservacionesSolicitudesIgualesValUrgHosp(forma.getIdIngreso())) // MT13612 - Se imprime si las observaciones son diferentes a las de Urgencias
							{
								forma.setImprimirVIHospitalizacion(true);
							} 
						}
						catch (SQLException e)
						{
							logger.error(e);
						}
					}
				}
			}
			else if (forma.getFiltroAsocio().trim().equals("U"))
			{
				forma.setImprimirVIUrgencias(true);
			}
			else if (forma.getFiltroAsocio().trim().equals("HH") || forma.getFiltroAsocio().trim().equals("HA"))
			{
				forma.setImprimirVIHospitalizacion(true);
			}
			else
			{
				//hermorhu - MT7078
				//FIXME Soluciona la impresion desde historia de atenciones cuando el paciente tiene varios ingresos... ya que trae siempre la via del ultimo ingreso
				String viaIngresoPaciente = Utilidades.obtenerViaIngresoCuenta(con, forma.getCuenta());
				int viaIngreso = Integer.parseInt(viaIngresoPaciente);
				
//				int viaIngreso = paciente.getCodigoUltimaViaIngreso();
//				if(!UtilidadTexto.isEmpty(forma.getViaIngreso())){
					
//					viaIngreso = Integer.parseInt(forma.getViaIngreso());
//				}
				if(viaIngreso ==ConstantesBD.codigoViaIngresoUrgencias)
				{
					forma.setImprimirVIUrgencias(true);
				}
				//else if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
				else if( viaIngreso ==ConstantesBD.codigoViaIngresoHospitalizacion)
				{

					if(util.UtilidadValidacion.tieneCuentaAsociada(con, Integer.parseInt(forma.getIdIngreso()))>0)
					{
						forma.setImprimirVIUrgencias(true);
					}
					forma.setImprimirVIHospitalizacion(true);
				}
			}
			
			dto.setImprimirValHospitalizacion(forma.isImprimirVIHospitalizacion());
			dto.setImprimirValUrgencias(forma.isImprimirVIUrgencias());
		}


		if(UtilidadTexto.getBoolean(forma.getSecciones("evoluciones")+""))
		{
			dto.setImprimirEvoluciones(true);
		}

		if(UtilidadTexto.getBoolean(forma.getSecciones("respuestaInterpretacionInterconsulta")+""))
		{
			dto.setImprimirRespuestaInterpretacionInterconsulta(true);
		}
		if(UtilidadTexto.getBoolean(forma.getSecciones("valoracionesCE").toString()))
		{
			dto.setImprimirValoracionesConsultaExterna(true);
		}
		if(UtilidadTexto.getBoolean(forma.getSecciones("signosVitales")+""))
		{
			dto.setImprimirSignosVitales(true);
		}
		if(UtilidadTexto.getBoolean(forma.getSecciones("valoracionesEnfermeria")+""))
		{
			dto.setImprimirValoracionesEnfermeria(true);
		}
		if(UtilidadTexto.getBoolean(forma.getSecciones("administracionMedicamentos")+""))
		{
			dto.setImprimirAdminMedicamentos(true);
		}
		if(UtilidadTexto.getBoolean(forma.getSecciones("consumoInsumos")+""))
		{
			dto.setImprimirConsumosInsumos(true);
		}
		if(UtilidadTexto.getBoolean(forma.getSecciones("ordenesMedicas")+""))
		{
			dto.setImprimirOrdenesMedicas(true);
		}		
		if(UtilidadTexto.getBoolean(forma.getSecciones("respuestaInterpretacionProcedimientos")+""))
		{
			dto.setImprimirInterpretacionRespuesta(true);
		}

		//----------------- SECCION ANOTACIONES DE ENFERMER?A ------------------------------------//
		if(UtilidadTexto.getBoolean(forma.getSecciones("notasEnfermeria")+""))
		{
			dto.setImprimirNotasEnfermeria(true);
		}
		if(UtilidadTexto.getBoolean(forma.getSecciones("cateteresSondas")+""))
		{
			dto.setImprimirCateteresSondas(true);
		}
		if(UtilidadTexto.getBoolean(forma.getSecciones("cuidadosEspeciales")+""))
		{
			dto.setImprimirCuidadosEspeciales(true);
		}
		if(UtilidadTexto.getBoolean(forma.getSecciones("soporteRespiratorio")+""))
		{
			dto.setImprimirSoporteRespiratorio(true);
		}
		if(UtilidadTexto.getBoolean(forma.getSecciones("controLiquidos")+""))
		{
			dto.setImprimirControlLiquidos(true);
		}
		if(UtilidadTexto.getBoolean(forma.getSecciones("ordenesAmbulatorias")+""))
		{
			dto.setImprimirOrdenaAmbulatorias(true);
		}
		if(UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteCirugias))) 
				|| UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteHojaQuirurgica))) 
				|| UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteHojaAnestesia)))
				|| UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteNotasGeneralesCirugia)))
				|| UtilidadTexto.getBoolean(String.valueOf(forma.getSecciones(IConstantesReporteHistoriaClinica.constanteNotasRecuperacion)))){
			dto.setImprimirCirugia(true);
		}
		return dto;
	}

	/**
	 * 
	 * @param resultado
	 */
	private void cargarDetalleImpresion(ArrayList<DtoResultadoImpresionHistoriaClinica> resultado) 
	{
		for(int i=0;i<resultado.size();i++)
		{
			if((resultado.get(i)).getCodigoTipoEvolucion()==ConstantesImpresionHistoriaClinica.tipoEvolucionValoracionUrgencias)
			{

			}
			else if((resultado.get(i)).getCodigoTipoEvolucion()==ConstantesImpresionHistoriaClinica.tipoEvolucionValoracionHospitalizacion)
			{

			} 
			else if((resultado.get(i)).getCodigoTipoEvolucion()==ConstantesImpresionHistoriaClinica.tipoEvolucionEvoluciones)
			{

			}
			else if((resultado.get(i)).getCodigoTipoEvolucion()==ConstantesImpresionHistoriaClinica.tipoEvolucionRespuestaInterpretacionInterconsulta)
			{

			}
		}
	}


	public String ingresosSeleccioandos(List<DtoIngresoHistoriaClinica> listaDtoHc){

		String listaIngresosSeleccionados="";
		for(int i=0;i<listaDtoHc.size();i++){

			if(listaDtoHc.get(i).getIngreso()!=null){


				if(listaDtoHc.get(i).getIngreso().equals(true))
				{
					if(!listaIngresosSeleccionados.equals("")){
						listaIngresosSeleccionados += ',';
					}
					listaIngresosSeleccionados += i + "";
				}

			}
		}

		return listaIngresosSeleccionados;

	}


}
