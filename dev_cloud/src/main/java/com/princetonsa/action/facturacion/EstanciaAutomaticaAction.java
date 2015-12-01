/*
 * Created on Aug 19, 2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.Listado;
import util.LogsAxioma;
import util.RespuestaValidacion;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.facturacion.EstanciaAutomaticaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.manejoPaciente.DtoDetAutorizacionEst;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.CargosDirectos;
import com.princetonsa.mundo.facturacion.EntidadesSubContratadas;
import com.princetonsa.mundo.facturacion.EstanciaAutomatica;
import com.princetonsa.mundo.manejoPaciente.Autorizaciones;
import com.princetonsa.mundo.medicamentos.DespachoMedicamentos;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.servinte.axioma.bl.manejoPaciente.facade.ManejoPacienteFacade;
import com.servinte.axioma.bl.manejoPaciente.interfaz.IProcesoGeneracionAutorizacionMundo;
import com.servinte.axioma.dto.facturacion.MontoCobroDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.DatosPacienteAutorizacionDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.interfaz.facturacion.convenio.IConvenioServicio;

/**
 * @author sebacho
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EstanciaAutomaticaAction extends Action {
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(EstanciaAutomaticaAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
													ActionForm form,
													HttpServletRequest request,
													HttpServletResponse response ) throws Exception
													{
		Connection con=null;
		try{
			if (response==null); //Para evitar que salga el warning
			if(form instanceof EstanciaAutomaticaForm)
			{

				//SE ABRE CONEXION
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				//OBJETOS A USAR
				EstanciaAutomaticaForm estanciaAutomaticaForm =(EstanciaAutomaticaForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				String estado=estanciaAutomaticaForm.getEstado(); 
				logger.info("estado Estancia Automatica-->"+estado);


				if(estado == null)
				{
					estanciaAutomaticaForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Estancia Automatica (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				//****ESTADOS PARA LA ESTANCIA POR ÁREA***********************
				else if (estado.equals("empezarArea"))
				{
					return accionEmpezar(con,estanciaAutomaticaForm,mapping,"empezarArea",paciente,request,usuario);
				}
				else if(estado.equals("buscarArea"))
				{
					return accionBuscarArea(con,estanciaAutomaticaForm,mapping);
				}
				else if(estado.equals("generarArea"))
				{
					return accionGenerarArea(con,estanciaAutomaticaForm,mapping,usuario);
				}
				//***********************************************************
				//****ESTADOS PARA LA ESTANCIA POR PACIENTE******************
				else if(estado.equals("empezarPaciente"))
				{
					return accionEmpezar(con,estanciaAutomaticaForm,mapping,"empezarPaciente",paciente,request,usuario);
				}
				else if(estado.equals("generarPaciente"))
				{
					return accionGenerarPaciente(con,estanciaAutomaticaForm,mapping,request,usuario,paciente);
				}
				else if(estado.equals("mostrarArchivo")||estado.equals("mostrarArchivoArea"))
				{
					return accionMostrarArchivo(con,estanciaAutomaticaForm,mapping,request,usuario);
				}
				//***********************************************************
				//******ESTADOS PARA LA CONSULTA LOG ESTANCIA AUTOMATICA********
				else if(estado.equals("empezarConsulta"))
				{
					return accionEmpezar(con,estanciaAutomaticaForm,mapping,"empezarConsulta",paciente,request,usuario);
				}
				else if(estado.equals("buscarConsulta"))
				{
					return accionBuscarConsulta(con,estanciaAutomaticaForm,mapping,request);
				}
				else if(estado.equals("consultar"))
				{
					return accionConsulta(con,estanciaAutomaticaForm,mapping,request);
				}
				else if(estado.equals("ordenar"))
				{
					return accionOrdenar(con,estanciaAutomaticaForm,mapping,request);
				}
				else if (estado.equals("mostrarArchivoConsulta"))
				{
					return accionMostrarArchivoConsulta(con,estanciaAutomaticaForm,mapping,request,usuario);
				}
				//***************************************************************
				else
				{
					estanciaAutomaticaForm.reset();
					logger.warn("Estado no valido dentro del flujo de Estancia Automatica(null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}	
		}catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;	
	}

	/**
	 * Método implementado para mostrar el contenido del archivo de inconsistencias en popUp
	 * @param con
	 * @param estanciaAutomaticaForm
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @return
	 */
	private ActionForward accionMostrarArchivoConsulta(Connection con, EstanciaAutomaticaForm estanciaAutomaticaForm, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) 
	{
		/// Se instancia objeto de EstanciaAutomatica
		EstanciaAutomatica estancia = new EstanciaAutomatica();
		
		String separador=System.getProperty("file.separator");
		String rutaLog = LogsAxioma.getRutaLogs()+ConstantesBD.logFolderModuloFacturacion+
			separador+"logEstanciaAutomatica"+separador+estanciaAutomaticaForm.getPathArchivoInconsistencias();
		
		estanciaAutomaticaForm.setPathArchivoInconsistencias(rutaLog);
		
		//Se carga el archivo de inconsistencias
		estanciaAutomaticaForm.setInconsistencias(estancia.cargarArchivoInconsistencias(estanciaAutomaticaForm.getPathArchivoInconsistencias()));
		estanciaAutomaticaForm.setNumInconsistencias(Integer.parseInt(estanciaAutomaticaForm.getInconsistencias("numRegistros").toString()));
		
		this.cerrarConexion(con);
		estanciaAutomaticaForm.setEstado("consultar"); //se reestablece el estado
		return mapping.findForward("archivo");
	}

	/**
	 * Método implementado para mostrar el archivo de inconsistencias de la estancia automática
	 * @param con
	 * @param estanciaAutomaticaForm
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @return
	 */
	private ActionForward accionMostrarArchivo(Connection con, EstanciaAutomaticaForm estanciaAutomaticaForm, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) 
	{
		// Se instancia objeto de EstanciaAutomatica
		EstanciaAutomatica estancia = new EstanciaAutomatica();
		
		//Se carga el archivo de inconsistencias
		estanciaAutomaticaForm.setInconsistencias(estancia.cargarArchivoInconsistencias(estanciaAutomaticaForm.getPathArchivoInconsistencias()));
		estanciaAutomaticaForm.setNumInconsistencias(Integer.parseInt(estanciaAutomaticaForm.getInconsistencias("numRegistros").toString()));
		
		if(estanciaAutomaticaForm.getEstado().equals("mostrarArchivoArea"))
			estanciaAutomaticaForm.setEstado("buscarArea");
		
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método usado para ordenar el listado de los registros
	 * LOG de Estancia Automatica
	 * @param con
	 * @param estanciaAutomaticaForm
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionOrdenar(Connection con, EstanciaAutomaticaForm estanciaAutomaticaForm, ActionMapping mapping, HttpServletRequest request) {
		try
		{
			//columnas del listado
			String[] indices={
					"tipo_",
					"fecha_inicial_",
					"fecha_final_",
					"fecha_grabacion_",
					"area_",
					"paciente_",
					"usuario_",
					"inconsistencia_",
					"reporte_",
					"ind_gen_est_"
				};
			
			estanciaAutomaticaForm.setDatosLog(Listado.ordenarMapa(indices,
					estanciaAutomaticaForm.getIndice(),
					estanciaAutomaticaForm.getUltimoIndice(),
					estanciaAutomaticaForm.getDatosLog(),
					estanciaAutomaticaForm.getTamanoDatosLog()));
			
			estanciaAutomaticaForm.setUltimoIndice(estanciaAutomaticaForm.getIndice());
			estanciaAutomaticaForm.setEstado("consultar");
			
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("principal");
		}
		catch(SQLException e)
		{
			logger.error("Error en accionOrdenar de EstanciaAutomaticaAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en EstanciaAutomaticaAction", "errors.problemasDatos", true);
		}
	}

	/**
	 * Método que realiza la consulta de los LOG Estancia Automática
	 * @param con
	 * @param estanciaAutomaticaForm
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionConsulta(Connection con, EstanciaAutomaticaForm estanciaAutomaticaForm, ActionMapping mapping, HttpServletRequest request) {
		try
		{
			//**Se instancia objeto estancia Automatica*********
			EstanciaAutomatica estancia=new EstanciaAutomatica();
			//se realiza la búsqueda
			estanciaAutomaticaForm.setDatosLog(
				estancia.consultaLogEstanciaAutomatica(
						con,
						estanciaAutomaticaForm.getAnio(),
						estanciaAutomaticaForm.getMes(),
						Integer.parseInt(estanciaAutomaticaForm.getCentroCosto()),
						estanciaAutomaticaForm.getTipoGeneracion(),
						estanciaAutomaticaForm.getUsuario(),
						estanciaAutomaticaForm.getCodigoCentroAtencion(),
						estanciaAutomaticaForm.getIndicativoGeneracionEstancia()
				)
			);
			//**********************************************
			//se asigna el tamaño de los resultados de la búsqueda
			estanciaAutomaticaForm.setTamanoDatosLog(Integer.parseInt(estanciaAutomaticaForm.getDatosLog().get("numRegistros")+""));
			
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("principal");
		}
		catch(SQLException e)
		{
			logger.error("Error en accionConsultar de EstanciaAutomaticaAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en EstanciaAutomaticaAction", "errors.problemasDatos", true);
		}
	}

	/**
	 * Método que maneja el estado de buscarConsulta
	 * @param con
	 * @param estanciaAutomaticaForm
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionBuscarConsulta(Connection con, EstanciaAutomaticaForm estanciaAutomaticaForm, ActionMapping mapping, HttpServletRequest request) {
		try
		{
			if(estanciaAutomaticaForm.getTipoGeneracion()==ConstantesBD.codigoTipoEstanciaPorArea)
			{
				//se asigna un centro de costo inválido para consultas
				estanciaAutomaticaForm.setCentroCosto("-2");
			}
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("principal");
		}
		catch(SQLException e)
		{
			logger.error("Error en accionBuscarConsulta de EstanciaAutomaticaAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en EstanciaAutomaticaAction", "errors.problemasDatos", true);
		}
	}

	/**
	 * Método usado para generar la estancia automática en Paciente
	 * @param con
	 * @param estanciaAutomaticaForm
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @param paciente
	 * @return
	 * @throws Exception 
	 */
	private ActionForward accionGenerarPaciente(Connection con, EstanciaAutomaticaForm estanciaAutomaticaForm, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario, PersonaBasica paciente) throws Exception {
		
		try
		{
			//*******objetos a usar*********
			boolean huboResultados=false; //bandera para indicar que si hubo resultados 
			//contador de registros encontrados
			int contador=0; 
			int numFechas = 0;
			String indica="Manual";
			//se reinician valores
			Convenios convenioResponsable = null;	
			IConvenioServicio convenioServicio = FacturacionServicioFabrica.crearConvenioServicio();			
			estanciaAutomaticaForm.setEstadoPagina("");
			estanciaAutomaticaForm.setPathArchivoInconsistencias("");
			estanciaAutomaticaForm.setInconsistencias(new HashMap());
			estanciaAutomaticaForm.setNumInconsistencias(0);
			//****se instancia objeto EstanciaAutomatica **************
			EstanciaAutomatica estancia=new EstanciaAutomatica();
			estancia.setIdCuenta(paciente.getCodigoCuenta());
			estancia.setPaciente(paciente.getApellidosNombresPersona(false));
			estancia.setTipoEstancia(ConstantesBD.codigoTipoEstanciaPorPaciente);
			//*********SE RECORRE CADA DÍA DESDE LA FECHA INICIAL HASTA LA FECHA FINAL*************************
			String fechaInicial=UtilidadFecha.conversionFormatoFechaABD(estanciaAutomaticaForm.getFechaInicialEstancia());
			String fechaFinal=UtilidadFecha.conversionFormatoFechaABD(estanciaAutomaticaForm.getFechaFinalEstancia());
			//mientras que la fecha inicial sea menor o igual a la fecha Final
			ManejoPacienteFacade manejoPacienteFacade = new ManejoPacienteFacade();
			Cargos cargos = null;
			
			while(fechaInicial.compareTo(fechaFinal)<=0)
			{
				
				//******INICIO DE PROCESO DE GENERACIÓN DE ESTANCIA PARA CADA DÍA*********
				//se asigna nueva fecha al objeto
				estancia.setFechaInicialEstancia(UtilidadFecha.conversionFormatoFechaAAp(fechaInicial));
				//se verifica y consulta que se pueda generar estancia para la fecha
				estanciaAutomaticaForm.setCuentasEstancia(estancia.consultaCuentaEstanciaPorPaciente(con,usuario.getCodigoInstitucionInt()));
				int numRegistros=Integer.parseInt(estanciaAutomaticaForm.getCuentasEstancia().get("numRegistros")+"");
				logger.info("numero de registros >>>>>> "+numRegistros);
				//si se encontraron resultados se puede generar estancia
				if(numRegistros>0)
				{
					contador++;
					huboResultados=true;
					//se cambia el estado de la pagina
					estanciaAutomaticaForm.setEstadoPagina("generacion");
					//*****SE CONSULTA EL SERVICIO ASOCIADO A LA CAMA*********
					int servicio=estancia.obtenerServicioCargoEstancia(con,indica);
					logger.info("Servicio >>>>>>> "+servicio);
					//validaciones según si se tomó el servicio
					//CASO 1) No se encontró el servicio (Inconsistencia)
					if(servicio<=0)
					{
						//se genera la inconsistencia
						//se verifica si habían cama sUCI para editar la inconsistencia
						if(estancia.isHayCamasUCI()){
							if (estancia.getTipoMonitoreo().getCodigo()!=0){
								estancia.generarInconsistencias("B","Falta definir servicio para el Tipo de Monitoreo "+Utilidades.getTipoMonitoreo(con,estancia.getTipoMonitoreo().getCodigo())[1],null,indica);
							}
						}
						else {
							estancia.generarInconsistencias("B","Falta definir servicio",null,indica);
						}
					}
					
					//CASO 2) Se encontró el servicio
					else if(servicio>0)
					{
						//Se asigna centro de costo solicitado a HashMap Cuenta
						logger.info("\n\n\n\n\n\n\n	CENTRO COSTO ESTANCIA MANUAL >>>"+estancia.getCodigoCentroCosto()+"");
						estanciaAutomaticaForm.getCuentasEstancia().put("centro_costo_solicitado_"+0,estancia.getCodigoCentroCosto()+"");
						/*
						 * Se desarrollan los cambios en el DCU 352 V1.8: Permitir generar autorizaciones automaticas
						 * de capitacion subcontratada.
						 * Validar si el convenio responsable de la cuenta del paciente es de tipo capitado y maneja
						 * capitacion subcontratada
						 */
					
						HibernateUtil.beginTransaction();  
						convenioResponsable = convenioServicio.findById(paciente.getCodigoConvenio());		    	         		
						
	    	         	if(convenioResponsable != null &&  
	    	     				((convenioResponsable.getTiposContrato().getCodigo())==ConstantesBD.codigoTipoContratoCapitado) &&         					
	    	     				(convenioResponsable.getCapitacionSubcontratada()==ConstantesBD.acronimoSiChar)){
	    	         		
	    	         		String consecutivoAutoEntSub = "";
	    	         		String consecutivoAutoCapiSub = "";
	    	         		boolean convenioManejaPresupuesto=false;
	    	         		
	    	         		if (convenioResponsable.getManejaPresupCapitacion()!= null){
	    	         			if (convenioResponsable.getManejaPresupCapitacion() == ConstantesBD.acronimoSiChar){
		    	         			convenioManejaPresupuesto=true;
		    	         		}
	    	         		}
	    	         			
	    	         		//Verifico si se encuentra definido el consecutivo de Autorizaciones de Entidad Subcontratada
	    	         		consecutivoAutoEntSub = UtilidadBD.obtenerValorActualTablaConsecutivos(
	    	    					con, ConstantesBD.nombreConsecutivoAutorizacionEntiSub, usuario.getCodigoInstitucionInt());
	    	         			
	    	         		//Verifico si se encuentra definido el consecutivo de capitación Subcontratada
	    	         		consecutivoAutoCapiSub = UtilidadBD.obtenerValorActualTablaConsecutivos(
	    	    					con, ConstantesBD.nombreConsecutivoAutorizacionPoblacionCapitada, usuario.getCodigoInstitucionInt());
	    	         		
	    	         		UtilidadBD.finalizarTransaccion(con);		    	         			
	    	         		if (!UtilidadTexto.isEmpty(consecutivoAutoEntSub) && !UtilidadTexto.isEmpty(consecutivoAutoCapiSub)) 
	    	     			{
	    	         				
	    	         			//OBTENEMOS LOS RESPONSABLES DE LA CUENTA
	    	         			ArrayList<DtoSubCuentas> dtoSubCuentasVector= UtilidadesHistoriaClinica.obtenerResponsablesIngreso(con, paciente.getCodigoIngreso(),false,new String[0],false, "" /*subCuenta*/,ConstantesBD.codigoNuncaValido /*Vía de ingreso*/);
	    	         			DtoSubCuentas dtoSubCuenta = new DtoSubCuentas();
	    	         			dtoSubCuenta=dtoSubCuentasVector.get(0);
	    	         			
	    	         			//Genero la autorización de entidad subcontratad por la capitación
	    	         			AutorizacionCapitacionDto generacionAutorizacion = null;
	    	         			OrdenAutorizacionDto ordenAutorizar = null;
	    	         			ServicioAutorizacionOrdenDto servicioAutorizar = null;
	    	         			DatosPacienteAutorizacionDto datosPacienteAutorizar = null;
	    	         			MontoCobroDto montoCobro = null;
	    	         			
	    	         			//Envio el servicio a autorizar
	    	         			servicioAutorizar = new ServicioAutorizacionOrdenDto();
	    	         			servicioAutorizar.setCodigo(servicio);
	    	         			servicioAutorizar.setAutorizar(true);
	    	         			long cantidad = 1;
	    	         			servicioAutorizar.setCantidad(cantidad);
	    	         			servicioAutorizar.setUrgente(ConstantesBD.acronimoNoChar);
	    	         				
	    	         			//Envio la iformación de la solicitud
	    	         			ordenAutorizar = new OrdenAutorizacionDto();
	    	         			ordenAutorizar.setCodigoCentroCostoEjecuta(estancia.getCodigoCentroCosto());
	    	         			ordenAutorizar.setCodigoViaIngreso(ConstantesBD.codigoViaIngresoHospitalizacion);
	    	         			ordenAutorizar.setEsPyp(false);
	    	         			ordenAutorizar.setTipoEntidadEjecuta(estancia.getTipo_entidad_autoriza());
	    	         			ordenAutorizar.getContrato().getConvenio().setCodigo(paciente.getCodigoConvenio());
	    	         			ordenAutorizar.getContrato().setCodigo(paciente.getCodigoContrato());
	    	         			ordenAutorizar.getContrato().getConvenio().setConvenioManejaPresupuesto(convenioManejaPresupuesto);
	    	         			
	    	         			if (convenioResponsable.getCapitacionSubcontratada().equals(ConstantesBD.acronimoSiChar)){
	    	         				ordenAutorizar.getContrato().getConvenio().setConvenioManejaCapiSub(true);
	    	         			}else {
	    	         				ordenAutorizar.getContrato().getConvenio().setConvenioManejaCapiSub(false);
	    	         			}
	    	         			ordenAutorizar.setMigrado(ConstantesBD.acronimoNoChar);
	    	         			ordenAutorizar.getServiciosPorAutorizar().add(servicioAutorizar);
	    	         			ordenAutorizar.setClaseOrden(ConstantesBD.claseOrdenCargoDirecto);
	    	         			ordenAutorizar.setTipoOrden(ConstantesBD.codigoTipoSolicitudEstancia);
	    	         			
	    	         			//Envio los datos del paciente para la autorización de capita
	    	         			datosPacienteAutorizar = new DatosPacienteAutorizacionDto();
	    	         			datosPacienteAutorizar.setTipoPaciente(dtoSubCuenta.getTipoPaciente());
	    	         			datosPacienteAutorizar.setTipoAfiliado(dtoSubCuenta.getTipoAfiliado());
	    	         			datosPacienteAutorizar.setClasificacionSocieconomica(dtoSubCuenta.getClasificacionSocioEconomica());
	    	         			datosPacienteAutorizar.setCodConvenioCuenta(paciente.getCodigoConvenio());
	    	         			datosPacienteAutorizar.setNaturalezaPaciente(dtoSubCuenta.getNaturalezaPaciente());
	    	         			datosPacienteAutorizar.setCuenta(paciente.getCodigoCuenta());
	    	         			datosPacienteAutorizar.setCodigoPaciente(paciente.getCodigoPersona());
	    	         			datosPacienteAutorizar.setCuentaAbierta(true);
	    	         			if (dtoSubCuenta.getMontoCobro() != 0){
	    	         				datosPacienteAutorizar.setCuentaManejaMontos(true);
	    	         				datosPacienteAutorizar.setPorcentajeMontoCuenta(dtoSubCuenta.getPorcentajeMontoCobro());
	    	         			} else {
	    	         				datosPacienteAutorizar.setCuentaManejaMontos(false);
	    	         			}
	    	         				
	    	         			//Envio la información del monto para la generación de la autorización
	    	         			montoCobro = new MontoCobroDto();
	    	         			montoCobro.setTipoDetalleMonto(dtoSubCuenta.getTipoDetalleMonto());
	    	         			montoCobro.setValorMonto(dtoSubCuenta.getValorMontoGeneral());
	    	         			montoCobro.setPorcentajeMonto(dtoSubCuenta.getPorcentajeMontoGeneral());
	    	         			montoCobro.setTipoMonto(dtoSubCuenta.getTipoMonto());
	    	         			montoCobro.setCantidadMonto(dtoSubCuenta.getCantidadMontoGeneral());
	    	         			montoCobro.setCodDetalleMonto(dtoSubCuenta.getMontoCobro());
	    	         			
	    	         			//Envio la información correspondiente al dto del proceso de autorización
	    	         			generacionAutorizacion = new AutorizacionCapitacionDto();
	    	         			generacionAutorizacion.setTipoAutorizacion(ConstantesIntegridadDominio.acronimoAutomatica);
	    	         			generacionAutorizacion.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
	    	         			generacionAutorizacion.setLoginUsuario(usuario.getLoginUsuario());
	    	         			generacionAutorizacion.setCentroAtencion(usuario.getCodigoCentroAtencion());
	    	         			
	    	         			generacionAutorizacion.getOrdenesAutorizar().add(ordenAutorizar);
	    	         			generacionAutorizacion.setDatosPacienteAutorizar(datosPacienteAutorizar);
	    	         			generacionAutorizacion.setMontoCobroAutorizacion(montoCobro);
	    	         			
	    	         			//Se hace el llamado al proceso 1106 
	    	         			boolean esEstancia = true;
	    	         			manejoPacienteFacade.generarProcesoAutorizacionEstanciaAutomaticaCargosPendientes(generacionAutorizacion, 
	    	         					estancia, estanciaAutomaticaForm.getCuentasEstancia(), usuario, servicio, 0, esEstancia, cargos);
	    	         			
	    	         			if (!generacionAutorizacion.isProcesoExitoso()){
	    	         				estancia.generarInconsistencias("B","Falló el proceso de generación de la autorización de capitación subcontratada",null,indica);
	    	         			}
	    	         		}
	    	         		else { //No tiene consecutivos disponibles
	    	         			estancia.generarInconsistencias("B","No se encuentran definidos los consecutivos para generar la autorización de capitación subcontratada",null,indica);
	    	         		}
	    	         	} else {//El convenio no es capitado
	    	         		
	    	         		int resp = manejoPacienteFacade.generarEstanciaAutomatica(con, estancia, 0, estanciaAutomaticaForm.getCuentasEstancia(), usuario, servicio);
	    	         		
	    	         		//Verifico si se pudo generar correctamente la solicitud
	    	         		if (resp < 1){
	    	         			UtilidadBD.abortarTransaccion(con);
	    	         		} else {
	    	         			UtilidadBD.finalizarTransaccion(con);
	    	         		}
	    	         	}
					}
	    	         		
				}//fin if
				//**********************************************************************
				
				
				//*****sele suma un día a la fecha inicial*******
				fechaInicial=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.calcularFechaSobreFechaReferencia(1,fechaInicial,true));
				numFechas ++;
			}//fin while
			
			//****************************************************************
			//si no se enocntraron estancia se genera inconsistencia
			if(contador==0)
			{
				estancia.setHuboInconsistencias(true);
				estancia.generarInconsistencias("B","No se encontraron estancias",null,indica);
			}
			//se reasignan las fechas para el log e inconsistencias
			estancia.setFechaInicialEstancia(estanciaAutomaticaForm.getFechaInicialEstancia());
			estancia.setFechaFinalEstancia(estanciaAutomaticaForm.getFechaFinalEstancia());
			
			//*****GENERACIÓN DEL ARCHIVO DE INCONSISTENCIAS******
			if(estancia.isHuboInconsistencias())
			{
				//se genera el archivo de las inconsistencias
				estanciaAutomaticaForm.setPathArchivoInconsistencias(estancia.generarArchivoInconsistencias(con,usuario));
			}
			
			//******GENERACIÓN DE LOG BASE DE DATOS*****************
			
			estancia.insertarLogEstanciaAutomatica(con,
					ConstantesBD.codigoTipoEstanciaPorPaciente,
					usuario.getLoginUsuario(),
					usuario.getCodigoInstitucionInt(),
					-1, //se ignora el centros de costo
					paciente.getCodigoPersona(),
					usuario.getCodigoCentroAtencion(),
					indica); 
			//***********************************************************
			
			//se revisa si hubo resultados (sin importar si fueron existosos o no)
			if(!huboResultados)
			{
				//si no hay se pone estado error
				estanciaAutomaticaForm.setEstadoPagina("error");
				
				//se consulta el número de estancias ya generadas por el paciente
				HashMap campos = new HashMap();
				campos.put("idCuenta",paciente.getCodigoCuenta());
				campos.put("fechaInicial",estanciaAutomaticaForm.getFechaInicialEstancia());
				campos.put("fechaFinal",estanciaAutomaticaForm.getFechaFinalEstancia());
				int numEstGeneradas = estancia.numeroEstanciasPaciente(con,campos);
				
				if(numEstGeneradas==numFechas||numEstGeneradas>0)
					estanciaAutomaticaForm.setMensajeError("Ya se generó estancia para las fechas seleccionadas");
				else		
					estanciaAutomaticaForm.setMensajeError("No se encontraron admisiones hospitalarias para generar estancia entre las fechas "+estanciaAutomaticaForm.getFechaInicialEstancia()+" y "+estanciaAutomaticaForm.getFechaFinalEstancia());
				
			}
			return mapping.findForward("principal");
		}
		catch(SQLException e)
		{
			logger.error("Error en accionGenerarPaciente de EstanciaAutomaticaAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en EstanciaAutomaticaAction", "errors.problemasDatos", true);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		
	}

	/**
	 * Método usado para generar la estancia automática en Area
	 * @param con
	 * @param estanciaAutomaticaForm
	 * @param mapping
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	private ActionForward accionGenerarArea(Connection con, EstanciaAutomaticaForm estanciaAutomaticaForm, ActionMapping mapping, UsuarioBasico usuario) throws Exception {
		//*******se reinicia valor del estado página****************
		estanciaAutomaticaForm.setEstadoPagina("");
		estanciaAutomaticaForm.setPathArchivoInconsistencias("");
		estanciaAutomaticaForm.setInconsistencias(new HashMap());
		estanciaAutomaticaForm.setNumInconsistencias(0);
		String indica="Manual";
		int numeroTraslados=0;
		Convenios convenioResponsable = null;	
		IConvenioServicio convenioServicio = FacturacionServicioFabrica.crearConvenioServicio();			
		//******SE REALIZA LA CONSULTA INICIAL***************
		EstanciaAutomatica estancia=new EstanciaAutomatica();
		this.llenarMundo(estancia,estanciaAutomaticaForm);
		estancia.setCentroAtencion(usuario.getCodigoCentroAtencion());
		estanciaAutomaticaForm.setCuentasEstancia(estancia.consultaCuentasEstanciaPorAreaAutomatico(con, usuario.getCodigoInstitucionInt()));
		ManejoPacienteFacade manejoPacienteFacade = new ManejoPacienteFacade();
		Cargos cargos=null;
		//****************************************************
		int numeroCuentas=Integer.parseInt(estanciaAutomaticaForm.getCuentasEstancia().get("numRegistros")+"");
		//se verifica si se encontraron admisiones
		if(numeroCuentas>0)
		{
			//se cambia el estado de la pagina
			estanciaAutomaticaForm.setEstadoPagina("generacion");
			//iteración de cada cuenta consultada
			for(int i=0;i<numeroCuentas;i++)
			{
				//se toman datos generales
				estancia.setIdCuenta(Integer.parseInt(estanciaAutomaticaForm.getCuentasEstancia().get("cuenta_"+i)+""));
				//se obtiene nombre del paciente para las inconsistencias
				estancia.setPaciente(estanciaAutomaticaForm.getCuentasEstancia().get("paciente_"+i)+"");
				estancia.setCodigoPaciente(Integer.parseInt(estanciaAutomaticaForm.getCuentasEstancia().get("codigo_paciente_"+i)+""));
				
				
				//se consulta si el paciente tiene traslados
				numeroTraslados=Integer.parseInt(estanciaAutomaticaForm.getCuentasEstancia().get("traslados_"+i)+"");
				
				//se verifica que la cuenta tenga traslados
				//de lo contrario se genera inconsistencia
				if(numeroTraslados>0)
				{
					
					//*****SE CONSULTA EL SERVICIO ASOCIADO A LA CAMA*********
					int servicio=estancia.obtenerServicioCargoEstancia(con,indica);
					//validaciones según si se tomó el servicio
					//CASO 1) No se encontró el servicio (Inconsistencia)
					if(servicio<=0)
					{
						//se genera la inconsistencia
						//se verifica si habían cama sUCI para editar la inconsistencia
						if(estancia.isHayCamasUCI()){
							if (estancia.getTipoMonitoreo().getCodigo()!=0){
								estancia.generarInconsistencias("B","Falta definir servicio para el Tipo de Monitoreo "+Utilidades.getTipoMonitoreo(con,estancia.getTipoMonitoreo().getCodigo())[1],null,indica);
							}
						}else{
							estancia.generarInconsistencias("B","Falta definir servicio",null,indica);
						}
						
					}
					//CASO 2) Se encontró el servicio
					else if(servicio>0)
					{
						//Se asigna centro de costo solicitado a HashMap Cuenta
						logger.info("\n\n\n\n\n\n\n	CENTRO COSTO ESTANCIA MANUAL >>>"+estancia.getCodigoCentroCosto()+"");
						estanciaAutomaticaForm.getCuentasEstancia().put("centro_costo_solicitado_"+i,estancia.getCodigoCentroCosto()+"");
						
						/*
						 * Se desarrollan los cambios en el DCU 132 V1.8: Permitir generar autorizaciones automaticas
						 * de capitacion subcontratada.
						 * Validar si el convenio responsable de la cuenta del paciente es de tipo capitado y maneja
						 * capitacion subcontratada
						 */
						
						//OBTENEMOS LOS RESPONSABLES DE LA CUENTA
						int ingreso = Integer.parseInt(estanciaAutomaticaForm.getCuentasEstancia().get("ingreso_"+i)+"");
						
	         			ArrayList<DtoSubCuentas> dtoSubCuentasVector= UtilidadesHistoriaClinica.obtenerResponsablesIngreso(con, ingreso,false,new String[0],false, "" /*subCuenta*/,ConstantesBD.codigoNuncaValido /*Vía de ingreso*/);
	         			DtoSubCuentas dtoSubCuenta = new DtoSubCuentas();
	         			dtoSubCuenta=dtoSubCuentasVector.get(0);
	         			
	         			HibernateUtil.beginTransaction();                                                                
	         			if(dtoSubCuenta.getConvenio().getCodigo() != 0){
	         				convenioResponsable = convenioServicio.findById(dtoSubCuenta.getConvenio().getCodigo());
	         			}
						if(convenioResponsable != null &&
	    	     				((convenioResponsable.getTiposContrato().getCodigo())==ConstantesBD.codigoTipoContratoCapitado) &&         					
	    	     				(convenioResponsable.getCapitacionSubcontratada()==ConstantesBD.acronimoSiChar)){

							String consecutivoAutoEntSub = "";
	    	         		String consecutivoAutoCapiSub = "";
	    	         		boolean convenioManejaPresupuesto=false;
	    	         		
	    	         		if (convenioResponsable.getManejaPresupCapitacion()!= null){
	    	         			if (convenioResponsable.getManejaPresupCapitacion() == ConstantesBD.acronimoSiChar){
		    	         			convenioManejaPresupuesto=true;
		    	         		}
	    	         		}
	    	         		
	    	         		//Verifico si se encuentra definido el consecutivo de Autorizaciones de Entidad Subcontratada
	    	         		consecutivoAutoEntSub = UtilidadBD.obtenerValorActualTablaConsecutivos(
	    	    					con, ConstantesBD.nombreConsecutivoAutorizacionEntiSub, usuario.getCodigoInstitucionInt());
	    	         		//Verifico si se encuentra definido el consecutivo de capitación Subcontratada
	    	         		consecutivoAutoCapiSub = UtilidadBD.obtenerValorActualTablaConsecutivos(
	    	    					con, ConstantesBD.nombreConsecutivoAutorizacionPoblacionCapitada, usuario.getCodigoInstitucionInt());
	    	         		
	    	         		UtilidadBD.finalizarTransaccion(con);		    	         			
	    	         		
	    	         		if (!UtilidadTexto.isEmpty(consecutivoAutoEntSub) && !UtilidadTexto.isEmpty(consecutivoAutoCapiSub)){
	    	         			
	    	         			//Genero la autorización de entidad subcontratad por la capitación
	    	         			AutorizacionCapitacionDto generacionAutorizacion = null;
	    	         			OrdenAutorizacionDto ordenAutorizar = null;
	    	         			ServicioAutorizacionOrdenDto servicioAutorizar = null;
	    	         			DatosPacienteAutorizacionDto datosPacienteAutorizar = null;
	    	         			MontoCobroDto montoCobro = null;
	    	         			
	    	         			//Envio el servicio a autorizar
	    	         			servicioAutorizar = new ServicioAutorizacionOrdenDto();
	    	         			servicioAutorizar.setCodigo(servicio);
	    	         			servicioAutorizar.setAutorizar(true);
	    	         			long cantidad = 1;
	    	         			servicioAutorizar.setCantidad(cantidad);
	    	         			servicioAutorizar.setUrgente(ConstantesBD.acronimoNoChar);
	    	         				
	    	         			//Envio la iformación de la solicitud
	    	         			ordenAutorizar = new OrdenAutorizacionDto();
	    	         			ordenAutorizar.setCodigoCentroCostoEjecuta(estancia.getCodigoCentroCosto());
	    	         			ordenAutorizar.setCodigoViaIngreso(ConstantesBD.codigoViaIngresoHospitalizacion);
	    	         			ordenAutorizar.setEsPyp(false);
	    	         			ordenAutorizar.setTipoEntidadEjecuta(estancia.getTipo_entidad_autoriza());
	    	         			ordenAutorizar.getContrato().getConvenio().setCodigo(dtoSubCuenta.getConvenio().getCodigo());
	    	         			ordenAutorizar.getContrato().setCodigo(dtoSubCuenta.getContrato());
	    	         			ordenAutorizar.getContrato().getConvenio().setConvenioManejaPresupuesto(convenioManejaPresupuesto);
	    	         			
	    	         			if (convenioResponsable.getCapitacionSubcontratada().equals(ConstantesBD.acronimoSiChar)){
	    	         				ordenAutorizar.getContrato().getConvenio().setConvenioManejaCapiSub(true);
	    	         			}else {
	    	         				ordenAutorizar.getContrato().getConvenio().setConvenioManejaCapiSub(false);
	    	         			}
	    	         			ordenAutorizar.setMigrado(ConstantesBD.acronimoNoChar);
	    	         			ordenAutorizar.getServiciosPorAutorizar().add(servicioAutorizar);
	    	         			ordenAutorizar.setClaseOrden(ConstantesBD.claseOrdenCargoDirecto);
	    	         			ordenAutorizar.setTipoOrden(ConstantesBD.codigoTipoSolicitudEstancia);
	    	         			
	    	         			//Envio los datos del paciente para la autorización de capita
	    	         			datosPacienteAutorizar = new DatosPacienteAutorizacionDto();
	    	         			datosPacienteAutorizar.setTipoPaciente(dtoSubCuenta.getTipoPaciente());
	    	         			datosPacienteAutorizar.setTipoAfiliado(dtoSubCuenta.getTipoAfiliado());
	    	         			datosPacienteAutorizar.setClasificacionSocieconomica(dtoSubCuenta.getClasificacionSocioEconomica());
	    	         			datosPacienteAutorizar.setCodConvenioCuenta(dtoSubCuenta.getConvenio().getCodigo());
	    	         			datosPacienteAutorizar.setNaturalezaPaciente(dtoSubCuenta.getNaturalezaPaciente());
	    	         			datosPacienteAutorizar.setCuenta(estancia.getIdCuenta());
	    	         			datosPacienteAutorizar.setCodigoPaciente(estancia.getCodigoPaciente());
	    	         			datosPacienteAutorizar.setCuentaAbierta(true);
	    	         			
	    	         			if (dtoSubCuenta.getMontoCobro() != 0){
	    	         				datosPacienteAutorizar.setCuentaManejaMontos(true);
	    	         				datosPacienteAutorizar.setPorcentajeMontoCuenta(dtoSubCuenta.getPorcentajeMontoCobro());
	    	         			} else {
	    	         				datosPacienteAutorizar.setCuentaManejaMontos(false);
	    	         			}
	    	         				
	    	         			//Envio la información del monto para la generación de la autorización
	    	         			montoCobro = new MontoCobroDto();
	    	         			montoCobro.setTipoDetalleMonto(dtoSubCuenta.getTipoDetalleMonto());
	    	         			montoCobro.setValorMonto(dtoSubCuenta.getValorMontoGeneral());
	    	         			montoCobro.setPorcentajeMonto(dtoSubCuenta.getPorcentajeMontoGeneral());
	    	         			montoCobro.setTipoMonto(dtoSubCuenta.getTipoMonto());
	    	         			montoCobro.setCantidadMonto(dtoSubCuenta.getCantidadMontoGeneral());
	    	         			montoCobro.setCodDetalleMonto(dtoSubCuenta.getMontoCobro());
	    	         			
	    	         			//Envio la información correspondiente al dto del proceso de autorización
	    	         			generacionAutorizacion = new AutorizacionCapitacionDto();
	    	         			generacionAutorizacion.setTipoAutorizacion(ConstantesIntegridadDominio.acronimoAutomatica);
	    	         			generacionAutorizacion.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
	    	         			generacionAutorizacion.setLoginUsuario(usuario.getLoginUsuario());
	    	         			generacionAutorizacion.setCentroAtencion(usuario.getCodigoCentroAtencion());
	    	         			
	    	         			generacionAutorizacion.getOrdenesAutorizar().add(ordenAutorizar);
	    	         			generacionAutorizacion.setDatosPacienteAutorizar(datosPacienteAutorizar);
	    	         			generacionAutorizacion.setMontoCobroAutorizacion(montoCobro);
	    	         			
	    	         			//Se hace el llamado al proceso 1106 
	    	         			boolean esEstancia = true;
	    	         			manejoPacienteFacade.generarProcesoAutorizacionEstanciaAutomaticaCargosPendientes(generacionAutorizacion, 
	    	         					estancia, estanciaAutomaticaForm.getCuentasEstancia(), usuario, servicio, i, esEstancia, cargos);
	    	         			
	    	         			if (!generacionAutorizacion.isProcesoExitoso()){
	    	         				estancia.generarInconsistencias("B","Falló el proceso de generación de la autorización de capitación subcontratada",null,indica);
	    	         			}
	    	     			}
	    	         		else { //No tiene consecutivos disponibles
	    	         			estancia.generarInconsistencias("B","No se encuentran definidos los consecutivos para generar la autorización de capitación subcontratada",null,indica);
	    	         		}
						} else {//El convenio no es capitado
	    	         		int resp = manejoPacienteFacade.generarEstanciaAutomatica(con, estancia, i, estanciaAutomaticaForm.getCuentasEstancia(), usuario, servicio);
	    	         		
	    	         		//Verifico si se pudo generar correctamente la solicitud
	    	         		if (resp < 1){
	    	         			UtilidadBD.abortarTransaccion(con);
	    	         		} else {
	    	         			UtilidadBD.finalizarTransaccion(con);
	    	         		}
	    	         	}
						
					}//if del servicio
				}
				//se genera la inconsistencia cuando
				//la cuenta no tiene traslados
				else{
					estancia.setHuboInconsistencias(true);
					estancia.generarInconsistencias("B","Cuenta sin traslados de cama",null,indica);
				}
			}
			//se verifica si se encontraron estancias
			//*****GENERACIÓN DEL ARCHIVO DE INCONSISTENCIAS******
			if(estancia.isHuboInconsistencias())
			{
				//se genera el archivo de las inconsistencias
				estanciaAutomaticaForm.setPathArchivoInconsistencias(estancia.generarArchivoInconsistencias(con,usuario));
			}
			//******GENERACIÓN DE LOG BASE DE DATOS*****************
			int respprueba=estancia.insertarLogEstanciaAutomatica(con,
					ConstantesBD.codigoTipoEstanciaPorArea,
					usuario.getLoginUsuario(),
					usuario.getCodigoInstitucionInt(),
					estancia.getCodigoCentroCosto(),
					0,//se ignora el paciente 
					usuario.getCodigoCentroAtencion(),
					indica); 
			//***********************************************************
		}
		//no se encontraron admisiones
		else
		{
			estanciaAutomaticaForm.setEstadoPagina("error");
		}
		//se reinicia estado anterior
		estanciaAutomaticaForm.setEstado("buscarArea");
		try
		{
			UtilidadBD.cerrarConexion(con);
		}
		catch(SQLException e)
		{
			logger.error("Error al cerrar la conexión!!!"+e);
		}
		return mapping.findForward("principal");
			
	}

	/**
	 * @param con
	 * @param numeroSolicitud
	 * @param usuario
	 * @param servicio
	 * @param pos
	 * @param mapaCuentas
	 * @param estancia
	 * @return
	 */
	private int insertarCargoEstancia(Connection con, int numeroSolicitud, UsuarioBasico usuario, int servicio, int pos, HashMap mapaCuentas, EstanciaAutomatica estancia,String indica) {
		try
		{
			int idCuenta=Integer.parseInt(mapaCuentas.get("cuenta_"+pos)+"");
			String ingreso = mapaCuentas.get("ingreso_"+pos).toString();
			int centroCosto=Integer.parseInt(mapaCuentas.get("centro_costo_solicitado_"+pos)+"");
			int codigoPaciente=Integer.parseInt(mapaCuentas.get("codigo_paciente_"+pos)+"");
			//se cargan los datos de la cuenta
			//se cargan los datos del contrato
			//debo hacer uan nueva implementación para obtener información del
			//contrato y generar el cargo
			//se crea instancia de Cargo
			/*Cargo cargo=new Cargo();
			//******GENERACIÓN DEL CARGO DE LA ESTANCIA********************
			String[] mensajes=cargo.generarCargoTransaccional(
					con,
					numeroSolicitud,
					centroCosto,
					contrato.getCodigo(),
					contrato.isEstaVencido(),
					Integer.parseInt(cuenta.getCodigoViaIngreso()),
					ConstantesBD.codigoTipoSolicitudEstancia,
					contrato.getCodigoTipoTarifarioProcedimiento(),
					usuario.getLoginUsuario(),
					1,
					0,
					servicio,
					false,
					"",
					ConstantesBD.continuarTransaccion,
					false/*utilizarValorTarifaOpcional,
					-1/*valorTarifaOpcional);*/
			
			//GENERACION DEL CARGO Y SUBCUENTA - EVALUACION COBERTURA
			int a=0;
			PersonaBasica paciente= new PersonaBasica();
			paciente.cargar(con, codigoPaciente);
			paciente.cargarPacienteXingreso(con, ingreso, usuario.getCodigoInstitucion(), usuario.getCodigoCentroAtencion()+"");
		    Cargos cargos= new Cargos();
		    cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
																				usuario, 
																				paciente, 
																				false/*dejarPendiente*/, 
																				numeroSolicitud, 
																				ConstantesBD.codigoTipoSolicitudEstancia /*codigoTipoSolicitudOPCIONAL*/, 
																				idCuenta, 
																				centroCosto/*codigoCentroCostoEjecutaOPCIONAL*/, 
																				servicio/*codigoServicioOPCIONAL*/, 
																				1/*cantidadServicioOPCIONAL*/, 
																				ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOPCIONAL*/, 
																				ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
																				/*"" -- numeroAutorzacionOPCIONAL*/
																				""/*esPortatil*/,false,estancia.getFechaInicialEstancia(),
																				"" /*subCuentaCoberturaOPCIONAL*/);

		    
		    Vector<String> mensajes= cargos.getInfoErroresCargo().getMensajesErrorDetalle();
		    
		    /*
		     * Se envia la información correspondiente para la generación de la autorización de capitación 
		     * subcontratada 
		     */
		    estancia.setConvenioResponsablePaciente(cargos.getDtoDetalleCargo().getCodigoConvenio());
		    estancia.setCodigoContratoConvenio(cargos.getDtoDetalleCargo().getCodigoContrato());
		    estancia.setClasificacionSocioEconomica(cargos.getInfoResponsableCoberturaGeneral().getDtoSubCuenta().getClasificacionSocioEconomica());
		    estancia.setCodigoPaciente(cargos.getInfoResponsableCoberturaGeneral().getDtoSubCuenta().getCodigoPaciente());
		    estancia.setTipoAfiliado(cargos.getInfoResponsableCoberturaGeneral().getDtoSubCuenta().getTipoAfiliado());
		    estancia.setNaturalezaPaciente(cargos.getInfoResponsableCoberturaGeneral().getDtoSubCuenta().getNaturalezaPaciente());
		    estancia.setTipoPaciente(cargos.getInfoResponsableCoberturaGeneral().getDtoSubCuenta().getTipoPaciente());
		    estancia.setPorcentajeMontoCobro(cargos.getInfoResponsableCoberturaGeneral().getDtoSubCuenta().getPorcentajeMontoCobro());
		    estancia.setTipoDetalleMonto(cargos.getInfoResponsableCoberturaGeneral().getDtoSubCuenta().getTipoDetalleMonto());
		    estancia.setValorMontoGeneral(cargos.getInfoResponsableCoberturaGeneral().getDtoSubCuenta().getValorMontoGeneral());
		    estancia.setPorcentajeMontoGeneral(cargos.getInfoResponsableCoberturaGeneral().getDtoSubCuenta().getPorcentajeMontoGeneral());
		    estancia.setTipoMonto(cargos.getInfoResponsableCoberturaGeneral().getDtoSubCuenta().getTipoMonto());
		    estancia.setCantidadMontoGeneral(cargos.getInfoResponsableCoberturaGeneral().getDtoSubCuenta().getCantidadMontoGeneral());
		    estancia.setCodDetalleMontoCobro(cargos.getInfoResponsableCoberturaGeneral().getDtoSubCuenta().getMontoCobro());
		    
		    
		    for(int i=0;i<mensajes.size();i++)
		    	logger.info("mensajes encontrados => "+mensajes.get(i));
		    
			//se instancia un objeto solicitud y se carga para obtener
			//el consecutivo de la orden médica
			Solicitud solicitud=new Solicitud();
			solicitud.cargar(con,numeroSolicitud);
			//se editan las inconsistencias en caso de que no se haya generado cargo
			
			
			/**
		     * Victor Gomez
		     */
		    // Generar Solicitudes de Autorizacion Autormatica
		    //************************************************************************************************
			logger.info("\n\n\nIngreso Automatico de Solicitud de Autorizacion de Estancias\n\n\n");
			ActionErrors errores = new ActionErrors();
			ArrayList<DtoDetAutorizacionEst> dtoaux = new ArrayList<DtoDetAutorizacionEst>();
			dtoaux =Autorizaciones.cargarInfoBasicaDetAutorizacionEstancia(con, 13 , 
		    		cargos.getDtoDetalleCargo().getCodigoSubcuenta()+"", null, null,
		    		ConstantesIntegridadDominio.acronimoAutomatica);
			errores = Autorizaciones.insertarSolAutorizcionAdmEstAutomatica(con, dtoaux,usuario);
			if(errores.isEmpty())
				logger.info("error en la insercion de solicitud de autorizacione de tipo estancia");
			logger.info("\n\n\nFIN Ingreso Automatico de Solicitud de Autorizacion de Estancias\n\n\n");
		    //************************************************************************************************
			
			
			for(int i=0;i<mensajes.size();i++)
			{
				if(mensajes.get(i).equals("error.cargo.contratoVencido"))
					estancia.generarInconsistencias("A","El contrato especificado en la generación del cargo está vencido",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.esquemaNoSeleccionado"))
					estancia.generarInconsistencias("A","El contrato no tiene un esquema tarifario definido",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.noHayTarifaISS")||
						mensajes.get(i).toString().startsWith("error.cargo.noHayTarifaEsquemaTarifarioISSCita"))
					estancia.generarInconsistencias("A","No hay tarifa ISS para el servicio dado",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.noHayTarifaSoat")||
						mensajes.get(i).toString().startsWith("error.cargo.noHayTarifaEsquemaTarifarioSoatCita"))
					estancia.generarInconsistencias("A","No hay tarifa Soat para el servicio dado",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.noHayTarifaEsquemaTarifarioISS"))
					estancia.generarInconsistencias("A","No hay tarifa para el servicio seleccionado del esquema tarifario: ISS",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.noHayTarifaEsquemaTarifarioSoat"))
					estancia.generarInconsistencias("A","No hay tarifa para el servicio seleccionado del esquema tarifario: Soat",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.noHayValorTarifa"))
					estancia.generarInconsistencias("A","No existe valor de tarifa para el servicio dado",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.hayRepetidos"))
					estancia.generarInconsistencias("A","Ya existe un registro en la BD para el servicio - contrato - convenio que desea ingresar",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.contratoVencidoPaciente"))
					estancia.generarInconsistencias("A","El contrato del paciente se encuentra vencido",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.contratoVencidoPaciente"))
					estancia.generarInconsistencias("A","El contrato del paciente se encuentra vencido",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.tipoComplejidad.noExiste"))
					estancia.generarInconsistencias("A","El convenio maneja complejidad y la cuenta no tiene asignada un tipo complejidad",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.noSeEspecificoServicio"))
					estancia.generarInconsistencias("A","No se especificó servicio en la generación del cargo",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				
			}
			return 1;
		  }
	    catch(SQLException slqe)
	    {
	        logger.warn("Error generando el cargo de la solicitud en EstanciaAutomaticaAction: "+ numeroSolicitud+" "+slqe);
	        UtilidadBD.abortarTransaccion(con);
			return 0;
	    }
	    catch(Exception e)
	    {
	        logger.warn("Error generando el cargo de la solicitud en EstanciaAutomaticaAction: "+ numeroSolicitud+" "+e);
	        UtilidadBD.abortarTransaccion(con);
			return 0;
	    }
	}

	/**
	 * Método para insertar el cargo directo de la solicitud de Estancia
	 * @param con
	 * @param numeroSolicitud
	 * @param usuario
	 * @param servicio
	 * @return
	 */
	private int insertarCargoDirectoEstancia(Connection con, int numeroSolicitud, UsuarioBasico usuario, int servicio) 
	{
		//se instancia el objeto Cargo
		CargosDirectos cargo=new CargosDirectos();
		cargo.llenarMundoCargoDirecto(numeroSolicitud,usuario.getLoginUsuario(),ConstantesBD.codigoTipoRecargoSinRecargo,servicio,"",true,"");
			//se realiza la inserción de los datos del cargo directo
		return cargo.insertar(con);
	}

	/**
	 * Método para insertar una solicitud estancia en la tabla solicitudes
	 * @param con
	 * @param estancia
	 * @param pos
	 * @param mapaCuentas
	 * @return
	 */
	private int insertarSolicitudEstancia(Connection con, EstanciaAutomatica estancia, int pos, HashMap mapaCuentas) {
		//se instancia el objeto solicitud
		Solicitud solicitud=new Solicitud();
		solicitud.clean();
		solicitud.setFechaSolicitud(estancia.getFechaInicialEstancia());
		solicitud.setHoraSolicitud(UtilidadFecha.getHoraActual());
		solicitud.setTipoSolicitud(new InfoDatosInt(ConstantesBD.codigoTipoSolicitudEstancia));
		solicitud.setEspecialidadSolicitante(new InfoDatosInt(ConstantesBD.codigoEspecialidadMedicaNinguna));
		solicitud.setOcupacionSolicitado(new InfoDatosInt(ConstantesBD.codigoOcupacionMedicaNinguna));
		solicitud.setCentroCostoSolicitante(new InfoDatosInt(Integer.parseInt(mapaCuentas.get("centro_costo_solicitante_"+pos)+"")));
		solicitud.setCentroCostoSolicitado(new InfoDatosInt(Integer.parseInt(mapaCuentas.get("centro_costo_solicitado_"+pos)+"")));
	    solicitud.setCodigoCuenta(Integer.parseInt(mapaCuentas.get("cuenta_"+pos)+""));
	    solicitud.setCobrable(true);
	    solicitud.setVaAEpicrisis(false);
	    solicitud.setUrgente(false);
	    //solicitud.setEstadoFacturacion(new InfoDatosInt(ConstantesBD.codigoEstadoFPendiente));
	    try
	    {
	        //se instancia este objeto para cambiar el estado Historia Clinica de la solicitud
	    	int numeroSolicitud=solicitud.insertarSolicitudGeneralTransaccional(con, ConstantesBD.inicioTransaccion);
	        DespachoMedicamentos despacho=new DespachoMedicamentos();
	        despacho.setNumeroSolicitud(numeroSolicitud);
	        //se cambia el estado Historia Clínica de la solicitud
	        int resp=despacho.cambiarEstadoMedicoSolicitudTransaccional(con,ConstantesBD.continuarTransaccion,ConstantesBD.codigoEstadoHCCargoDirecto/*,""*/);
	        if(resp<=0)
	        	return 0;
	        else
	        	return numeroSolicitud;
	    }
	    catch(SQLException sqle)
	    {
	        logger.warn("Error en la transaccion del insert en la solicitud básica");
			return 0;
	    }
	}

	/**
	 * @param con
	 * @param estanciaAutomaticaForm
	 * @param mapping
	 * @param estado
	 * @param paciente
	 * @param request
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, EstanciaAutomaticaForm estanciaAutomaticaForm, ActionMapping mapping, String estado, PersonaBasica paciente, HttpServletRequest request, UsuarioBasico usuario) {
		try
		{
			estanciaAutomaticaForm.reset();
			if(estado.equals("empezarPaciente"))
			{					
				//*******VALIDACIONES*************************************
				RespuestaValidacion resp = UtilidadValidacion.esValidoPacienteCargado(con, paciente);
				//Si paciente está cargado en sesión
				if(!resp.puedoSeguir)
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en validacion del paciente", resp.textoRespuesta, true);
				}
				
				//No se permite si el ingreso fue realizado por Ingreso Pacientes Entidades SubContratadas		
				if(IngresoGeneral.esIngresoComoEntidadSubContratada(con,paciente.getCodigoIngreso()).toString().equals(ConstantesBD.acronimoSi))
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.ingresoPacienteEntidadSub","Ingreso de Paciente en Entidades Subcontratadas. Entidad Subcontratada "+EntidadesSubContratadas.getDescripcionEntidadSubXIngreso(con,paciente.getCodigoIngreso()+"") , false);					
								
				//Si es paciente con ingreso de hospital día
				if(paciente.isHospitalDia())
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente con ingreso de hospital día", "errores.paciente.ingresoHospitalDia", true);
				}
				//Si hay admisión hospitalaria abierta
				if(!UtilidadValidacion.esCuentaHospitalizacion(con,paciente.getCodigoCuenta(),ConstantesBD.codigoEstadoCuentaFacturada,true,usuario.getCodigoInstitucionInt()))
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente sin admsion abierta", "errors.paciente.noAdmisionHospitalizacion", true);
				}				
				/**Validaciones de concurrencia**/
				//Si está en proceso de Facturación
				if(UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(), "") )
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoFact", "error.facturacion.cuentaEnProcesoFact", true);
				}
				//Si está en proceso de distribución
				if(UtilidadValidacion.estaEnProcesoDistribucion(con, paciente.getCodigoPersona(), usuario.getLoginUsuario()) )
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoDistribucion", "error.facturacion.cuentaEnProcesoDistribucion", true);
				}
				
				
				//Se carga la fecha de admisión de la cuenta
				String fechaAdmision=UtilidadValidacion.getFechaAdmision(con,paciente.getCodigoCuenta());
				//se carga la fecha de orden de salida (si la tiene)
				String fechaOrdenSalida=Utilidades.obtenerFechaOrdenSalida(con,paciente.getCodigoCuenta(),usuario.getCodigoInstitucionInt());
				//se asignan los valores al Formulario
				estanciaAutomaticaForm.setFechaOrdenSalida(fechaOrdenSalida);
				estanciaAutomaticaForm.setFechaAdmision(fechaAdmision);
				estanciaAutomaticaForm.setFechaInicialEstancia(UtilidadFecha.conversionFormatoFechaAAp(fechaAdmision));
				estanciaAutomaticaForm.setFechaFinalEstancia(UtilidadFecha.conversionFormatoFechaAAp(fechaAdmision));
			}
			else if(estado.equals("empezarConsulta"))
			{
				estanciaAutomaticaForm.setCentroCosto("-2");
				estanciaAutomaticaForm.setCodigoCentroAtencion(usuario.getCodigoCentroAtencion());
			}
			
			estanciaAutomaticaForm.setEstado(estado);
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("principal");
		}
		catch(SQLException e)
		{
			logger.error("Error al cerrar la conexión en EstanciaAutomaticaAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente sin admsion abierta", "errors.paciente.noAdmision", true);
		}
		
	}

	/**
	 * @param con
	 * @param estanciaAutomaticaForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionBuscarArea(Connection con, EstanciaAutomaticaForm estanciaAutomaticaForm, ActionMapping mapping) {
		try
		{
			///*******se reinician valores****************
			estanciaAutomaticaForm.setEstadoPagina("");
			estanciaAutomaticaForm.setPathArchivoInconsistencias("");
			estanciaAutomaticaForm.setInconsistencias(new HashMap());
			estanciaAutomaticaForm.setNumInconsistencias(0);
			//se toma la fecha anterior a la fecha Actual
			estanciaAutomaticaForm.setFechaInicialEstancia(UtilidadFecha.calcularFechaSobreFechaReferencia(1,UtilidadFecha.getFechaActual(),false));
			//************************************************************
			UtilidadBD.cerrarConexion(con);
		}
		catch(SQLException e)
		{
			logger.error("Error al cerrar la concexión en EstanciaAutomaticaAction: "+e);
		}
		return mapping.findForward("principal");
	}

	/**
	 * @param estancia
	 * @param estanciaAutomaticaForm
	 */
	private void llenarMundo(EstanciaAutomatica estancia, EstanciaAutomaticaForm estanciaAutomaticaForm) {
		if(estanciaAutomaticaForm.getEstado().equals("buscarArea")||
			estanciaAutomaticaForm.getEstado().equals("generarArea"))
		{
			estancia.setCodigoCentroCosto(Integer.parseInt(estanciaAutomaticaForm.getCentroCosto().split("-")[0]));
			estancia.setNombreCentroCosto(estanciaAutomaticaForm.getCentroCosto().split("-")[1]);
			estancia.setFechaInicialEstancia(estanciaAutomaticaForm.getFechaInicialEstancia());
			estancia.setTipoEstancia(ConstantesBD.codigoTipoEstanciaPorArea);
		}
		
	}	
	

	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer un forward
	 * @param con Conexión con la fuente de datos
	 */
	private void cerrarConexion (Connection con)
	{
	    try
		{
	        UtilidadBD.cerrarConexion(con);
	    }
	    catch(Exception e)
		{
	        logger.error(e+"Error al tratar de cerrar la conexion con la fuente de datos. \n Excepcion: " +e.toString());
	    }
	}
	
	
	
	
	
	
	
	
}
