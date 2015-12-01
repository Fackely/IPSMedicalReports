package com.princetonsa.actionform.tesoreria;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.servinte.axioma.dto.tesoreria.DTONotaPaciente;
import com.servinte.axioma.dto.tesoreria.DtoConceptoNotasPacientes;
import com.servinte.axioma.dto.tesoreria.DtoInfoIngresoPacienteControlarAbonoPacientes;
import com.servinte.axioma.dto.tesoreria.DtoNotasPorNaturaleza;
import com.servinte.axioma.dto.tesoreria.DtoResumenNotasPaciente;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.EmpresasInstitucion;

public class NotasPacientesForm extends ActionForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/** 
	 * Variable que maneja el paramentro general ¿Controlar abono de pacientes por No. de Ingreso? 
	 */
	 private boolean controlaAbonoPacienteXIngreso;


	/**
	 * Total de valor devolcuion para tomar el valor por medio de ajax y mostrarlo
	 */
	
	private String totalValorDevolucion;

	/**
	 * Total de Nuevo saldo para tomar el valor por medio de ajax y mostrarlo
	 */
	
	private String totalNuevoSaldo;

	/**
	 * Login del usuario que genera la devolucion
	 */
	
	private String loginUsuario;
	

	/**
	 * Consecutivo generado para la nota devolucion
	 */
	
	private BigDecimal consecutivo;
	
	/**
	 * Fecha de la devolucion
	 */
	
	private String fechaNota;
	
	/**
	 * Hora de la devolucion
	 */
	
	private String horaNota;
	
	/**
	 * Saldo anterior del paciente
	 */
	
	private String saldoAnterior;
	
	/**
	 * Saldo anterior del paciente
	 */
	
	private String saldoActual;
	
	/**
	 * Usuario que genera la devolucion
	 */
	
	private String usuarioGeneraNota;
	
	/**
	 * Si hace la insercion correctamente de la 
	 * nota devolucion abono
	 */
	
	private boolean insercionCorrecta; 
	
	/**
	 * Motivo de la nota devolucion
	 */
	
	private String observacionesNotaPaciente;
	
	/**
	 * Numero identificacion paciente
	 */
	
	private String numeroIdentificacion;

	/**
	 * Nombre completo paciente
	 */
	
	private String nombreCompleto;


	/**
	 * Maneja los estados de la aplicacion de devoluciones de abonos del paciente
	 */
	private String estado;
	
	
	/**
	 * Maneja los estados de la aplicacion de devoluciones de abonos del paciente
	 */

	private String tipoIdentificacionPac;
	
	/**
	 * Atributo con los numeros de digitos de captura
	 * para el campo identificacion del paciente
	 */
	
	private int numDigCaptNumIdPac;

	private ArrayList<HashMap<String, Object>> listTipIdent;

	private String numeroIdentificacionPac;
	
	private String tipoFlujoPaciente;
	
	private String numDiasAntFechaActual;

	private String multiploMinGenCitas;

	private String validaPresupuestoContratado;

	private String utilProgOdonPlanTra;

	private String minutosEsperaAsgCitOdoCad;

	private boolean existePaciente;

	private DtoPaciente paciente;

	private boolean tieneIngresoPaciente;
	
	private boolean tienePlanTratamientoInactivo;
	
	private DTONotaPaciente dtoNotaPaciente;
	
	private int indexListaIngreso;
	
	private boolean institucionMultiempresa;
	
	private ArrayList<CentroAtencion> listaCentrosAtencion;
	
	private CentroAtencion centroAtencionOrigen;
	
	private CentroAtencion centroAtencionRegistro;
	
	private String naturalezaNotasPaciente;
	
	private String naturalezaNotasPacienteSeleccionada;
	
	private String nombreNaturalezaNota;
	
	private ArrayList<DtoConceptoNotasPacientes> listaConceptosNotasPacientes;
	
	private DtoConceptoNotasPacientes dtoConceptoNotaPaciente;
	
	/** * Lista de Instituciones usados como parametros de busqueda*/
	private ArrayList<EmpresasInstitucion> listaEmpresaInstitucion;
	
	/** * Lista de Usuarios usados como parametros de busqueda*/
	private ArrayList<DtoUsuarioPersona> listaUsuarios;
	
	private String fechaInicialBusqueda;
	
	private String fechaFinalBusqueda;
	
	private String numeroNotaInicialBusqueda;
	
	private String numeroNotaFinalBusqueda;
	
	private String naturalezaNotaBusqueda;
	
	private EmpresasInstitucion empresaInstitucion;
	
	private String[] centrosAtencionSeleccionados;
	
	private String[] centrosAtencionSeleccionadosTmp;
	
	private DtoUsuarioPersona usuarioGenera;
	
	private LinkedHashMap<String, DtoNotasPorNaturaleza> mapaDtoResumenNotasPaciente;
	
	private ArrayList<DtoResumenNotasPaciente> listaDtoResumenNotasPaciente;
	
	private boolean manejoEspecialInstOdonto;
	
	/**
	 * Atributo que almacena el nombre de la columna por la cual deben ser
	 * ordenados los registros encontrados.
	 */
	private String patronOrdenar;
	
	/**
	 * Atributo usado para ordenar descendentemente.
	 */
	private String esDescendente;	

	private String claveOrdenar;
	
	private String naturalezaOrdenar;
	
	private String codigoPkNotaSeleccionado;
	
	private boolean esUnicoResultado;
	
	private boolean busquedaPorRango;
	
	private String tipoSalida;
	
	private String nombreArchivo;
	
	/**
	 * Reset de la forma
	 */
	public void reset()
	{
		this.listTipIdent = new ArrayList<HashMap<String,Object>>();
		this.tipoIdentificacionPac = "";
		this.estado	= "";
		this.setNumDigCaptNumIdPac(ConstantesBD.codigoNuncaValido);
		this.numeroIdentificacionPac = "";
		this.tipoFlujoPaciente="";
		this.numDiasAntFechaActual = "";
		this.multiploMinGenCitas = "";
		this.validaPresupuestoContratado = "";
		this.utilProgOdonPlanTra = "";
		this.minutosEsperaAsgCitOdoCad = "";
		this.existePaciente = false;
		this.paciente = new DtoPaciente();
		this.tieneIngresoPaciente = false;
		this.tienePlanTratamientoInactivo = false;
		dtoNotaPaciente=new DTONotaPaciente();
		consecutivo = BigDecimal.ZERO;
		observacionesNotaPaciente = "";
		insercionCorrecta = false;
		fechaNota = "";
		horaNota = "";
		saldoAnterior = "";
		saldoActual = "";
		usuarioGeneraNota = "";
		numeroIdentificacion = "";
		nombreCompleto = "";
		indexListaIngreso = ConstantesBD.codigoNuncaValido;
		listaCentrosAtencion = new ArrayList<CentroAtencion>();
		centroAtencionOrigen = null;
		centroAtencionRegistro = null;
		institucionMultiempresa = false;
		naturalezaNotasPaciente = "";
		naturalezaNotasPacienteSeleccionada = "";
		nombreNaturalezaNota = "";
		dtoConceptoNotaPaciente = null;
		listaConceptosNotasPacientes = new ArrayList<DtoConceptoNotasPacientes>();
		
		empresaInstitucion = null;
		centrosAtencionSeleccionados = null;
		centrosAtencionSeleccionadosTmp = null;
		fechaInicialBusqueda = "";
		fechaFinalBusqueda = "";
		numeroNotaInicialBusqueda = "";
		numeroNotaFinalBusqueda = "";
		naturalezaNotaBusqueda = "";
		usuarioGenera = null;
		mapaDtoResumenNotasPaciente = new LinkedHashMap<String, DtoNotasPorNaturaleza>();
		listaDtoResumenNotasPaciente = new ArrayList<DtoResumenNotasPaciente>();
		manejoEspecialInstOdonto = false;
		patronOrdenar = "";
		esDescendente = "";
		setClaveOrdenar("");
		setNaturalezaOrdenar("");
		setCodigoPkNotaSeleccionado("");
		setEsUnicoResultado(false);
		setControlaAbonoPacienteXIngreso(false);
		setBusquedaPorRango(false);
		setTipoSalida("");
		setNombreArchivo("");
	}
	
	
	/**
	* Valida las propiedades que han sido establecidas para este request HTTP, y retorna un objeto
	* <code>ActionErrors</code> que encapsula los errores de validación encontrados. Si no se
	* encontraron errores de validación, retorna <code>null</code>.
	* @param mapping Mapa usado para elegir esta instancia
	* @param request <i>Servlet Request</i> que está siendo procesado en este momento
	* @return <code>ActionErrors</code> con los (posibles) errores encontrados al validar este
	* formulario, o <code>null</code> si no se encontraron errores.
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errores = new ActionErrors();
		int index = 0;
		
		//********************NUEVOS ESTADOS DE VALDIACION*******************************
		if(this.estado.equals("validarPaciente"))
		{

			if(this.getCentroAtencionOrigenHelper() == ConstantesBD.codigoNuncaValido)
			{
				errores.add("", new ActionMessage("errors.required","Centro de Atención"));
			}
			if(this.tipoIdentificacionPac.equals(""))
			{
				errores.add("", new ActionMessage("errors.required","El tipo de identificación"));
			}
			if(this.numeroIdentificacionPac.trim().equals(""))
			{
				errores.add("", new ActionMessage("errors.required","El número de identificación"));
			}
			if(UtilidadCadena.tieneCaracteresEspecialesNumeroId(this.numeroIdentificacionPac))
			{
				errores.add("", new ActionMessage("errors.caracteresInvalidos","El número de identificación"));
			}
			
			if(!errores.isEmpty())
			{
				this.estado = "";
			}
		}
		
		if(this.estado.equals("guardarNotaPaciente")) {
			if(UtilidadTexto.isEmpty(this.naturalezaNotasPacienteSeleccionada))	{
				errores.add("", new ActionMessage("errors.required","Naturaleza Nota"));
			}
			if(getDtoConceptoNotaPacienteHelper() == ConstantesBD.codigoNuncaValidoLong) {
				errores.add("", new ActionMessage("errors.required","Concepto Nota Paciente"));
			}
			if(!errores.isEmpty()) {
				this.estado = "";
			}
			
			if (dtoNotaPaciente.getListaInfoNotaPaciente() != null) {
				for (DtoInfoIngresoPacienteControlarAbonoPacientes dtoInfoNotaPaciente : dtoNotaPaciente.getListaDtoInfoIngresoPacienteControlarAbonoPacientes()) {
					String valor = request.getParameter("listaInfoNotaPaciente["+index+"].valorDevolucion");
					if (!UtilidadTexto.isEmpty(valor)) {
						valor = valor.replaceAll(",", "");
						double valorNota = Double.parseDouble(valor);
							dtoInfoNotaPaciente.setValorDevolucion(valorNota);
					} 
					index++;
				}
			}
		}
		
		
//		//********************************************************************************
//		
//		
//		if(this.estado.equals("buscarAgenOdon") || this.estado.equals("desplazamientoFecha"))
//		{
//			// Validacion del parametro general Múltiplo en Minutos para la Generación de Citas Odontológicas
//			if(this.multiploMinGenCitas.equals("") || this.multiploMinGenCitas==null || Utilidades.convertirAEntero(this.multiploMinGenCitas)<=0)
//				errores.add("fechas",new ActionMessage("errors.required", "El Parametro General Múltiplo en Minutos para Generación de Citas Odontológicas "));
//			
//			if(errores.isEmpty())
//			{
//				// validacion de las fechas
//				boolean fechaInicialValida = false, fechaFinalValida = false;
//	
//				if(!this.fechaInicial.equals("") && !this.fechaFinal.equals("")){
//					fechaInicialValida = true;
//					fechaFinalValida = true;
//				}else{
//					if(!this.fechaInicial.equals("") || !this.fechaFinal.equals(""))
//						errores.add("fechas",new ActionMessage("errors.notEspecific", !this.fechaInicial.equals("")?"El Campo Fecha Final":"El Campo Fecha Inicia"));
//				}
//					
//				// se valida que la fecha inicial y la final esten en el formato adecuado
//				if(fechaInicialValida && fechaFinalValida)
//				{
//					if(!UtilidadFecha.validarFecha(this.fechaInicial)){
//						errores.add("fechaInicio",new ActionMessage("errors.formatoFechaInvalido", "Fecha Inicial"));
//						fechaInicialValida = false;
//					}			
//					if(!UtilidadFecha.validarFecha(this.fechaFinal)){
//						errores.add("fechaFinal",new ActionMessage("errors.formatoFechaInvalido", "Fecha Final"));
//						fechaFinalValida = false;
//					}
//				}
//				
//				// valida que la fecha cumplan con la estipulaciones de validaciones del anexo 864
//				if(fechaInicialValida&& fechaFinalValida)
//				{
//					
//					if(!this.numDiasAntFechaActual.equals("")&&Utilidades.convertirAEntero(this.numDiasAntFechaActual)>0)
//					{
//						int decremento = Utilidades.convertirAEntero(this.numDiasAntFechaActual)*ConstantesBD.codigoNuncaValido;
//						logger.info("valor decremento: "+decremento);
//						
//						// validar que la fecha de incio se igual o mayor a la fecha de actual
//						if(!UtilidadFecha.esFechaMenorQueOtraReferencia(
//								UtilidadFecha.conversionFormatoFechaAAp(this.fechaInicial), 
//								UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.getFechaActual(),decremento,false)))
//						{
//							// se valida la fecha final si es igual o mayor a la fecha inicial
//							if(UtilidadFecha.esFechaMenorQueOtraReferencia(
//									UtilidadFecha.conversionFormatoFechaAAp(this.fechaFinal), 
//									UtilidadFecha.conversionFormatoFechaAAp(this.fechaInicial))){
//								// error en la fecha final
//								errores.add("fechaFinal",new ActionMessage("errors.fechaAnteriorIgualActual", "Final", "Inicial"));
//								fechaFinalValida = false;
//							}
//						}else{
//							// error en la fecha inicial
//							errores.add("fechaInicio",new ActionMessage("errors.fechaAnteriorIgualActual", "Inicial", "Actual menos el número de días anteriores. "));
//							fechaInicialValida = false;
//						}
//					}else{
//						// validar que la fecha de incio se igual o mayor a la fecha de actual
//						if(!UtilidadFecha.esFechaMenorQueOtraReferencia(
//								UtilidadFecha.conversionFormatoFechaAAp(this.fechaInicial), 
//								UtilidadFecha.getFechaActual()))
//						{
//							// se valida la fecha final si es igual o mayor a la fecha inicial
//							if(UtilidadFecha.esFechaMenorQueOtraReferencia(
//									UtilidadFecha.conversionFormatoFechaAAp(this.fechaFinal), 
//									UtilidadFecha.conversionFormatoFechaAAp(this.fechaInicial))){
//								// error en la fecha final
//								errores.add("fechaFinal",new ActionMessage("errors.fechaAnteriorIgualActual", "Final", "Inicial"));
//								fechaFinalValida = false;
//							}
//						}else{
//							// error en la fecha inicial
//							errores.add("fechaInicio",new ActionMessage("errors.fechaAnteriorIgualActual", "Inicial", "Actual"));
//							fechaInicialValida = false;
//						}
//					}
//					
//					
//				}
//				
//				// se valida que el rango entre la fecha inicial y la final no se superior al  tres meses
//				// esta parte se valida si las validaciones de la fecha inicial y final han sido correctas
//				if(fechaInicialValida&& fechaFinalValida){
//					int nroMeses = UtilidadFecha.numeroMesesEntreFechas(
//							UtilidadFecha.conversionFormatoFechaAAp(this.fechaInicial),
//							UtilidadFecha.conversionFormatoFechaAAp(this.fechaFinal),true);
//					if (nroMeses > 3)
//						errores.add("rango agenda mayor", new ActionMessage("error.agenda.rangoMayorTresMeses", "AGENDA ODONTOLOGICA"));
//				}
//				// fin validacion de las fechas	
//			}
//		}
//		
//		if(this.estado.equals("realizarBusquedaRango")){
//			
//			if (this.dtoBusquedaAgendaRango.getEstadoCita().trim().equals(ConstantesIntegridadDominio.acronimoAreprogramar) ||
//					this.dtoBusquedaAgendaRango.getEstadoCita().trim().equals(ConstantesIntegridadDominio.acronimoEstadoCancelado)) {
//				
//				if (UtilidadTexto.isEmpty(this.dtoBusquedaAgendaRango.getTipoCancelacion())) {
//					errores.add("", new ActionMessage("errors.required","El Tipo de Cancelación"));
//				}
//			}
//			
//		}
		
		return errores;
	}

	
	/**
	 * 
	 * 
	 * GETS Y SETS
	 * 
	 * 
	 */
	
	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}


	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	/**
	 * @return the tipoIdentificacionPac
	 */
	public String getTipoIdentificacionPac() {
		return tipoIdentificacionPac;
	}

	/**
	 * @param tipoIdentificacionPac the tipoIdentificacionPac to set
	 */
	public void setTipoIdentificacionPac(String tipoIdentificacionPac) {
		this.tipoIdentificacionPac = tipoIdentificacionPac;
	}
	
	/**
	 * @return the listTipIdent
	 */
	public ArrayList<HashMap<String, Object>> getListTipIdent() {
		return listTipIdent;
	}

	/**
	 * @param listTipIdent the listTipIdent to set
	 */
	public void setListTipIdent(ArrayList<HashMap<String, Object>> listTipIdent) {
		this.listTipIdent = listTipIdent;
	}

	/**
	 * @param numDigCaptNumIdPac the numDigCaptNumIdPac to set
	 */
	public void setNumDigCaptNumIdPac(int numDigCaptNumIdPac) {
		this.numDigCaptNumIdPac = numDigCaptNumIdPac;
	}


	/**
	 * @return the numDigCaptNumIdPac
	 */
	public int getNumDigCaptNumIdPac() {
		return numDigCaptNumIdPac;
	}
	
	/**
	 * @return the numeroIdentificacionPac
	 */
	public String getNumeroIdentificacionPac() {
		return numeroIdentificacionPac;
	}

	/**
	 * @param numeroIdentificacionPac the numeroIdentificacionPac to set
	 */
	public void setNumeroIdentificacionPac(String numeroIdentificacionPac) {
		this.numeroIdentificacionPac = numeroIdentificacionPac;
	}

	/**
	 * @return the tipoFlujoPaciente
	 */
	public String getTipoFlujoPaciente() {
		return tipoFlujoPaciente;
	}

	/**
	 * @param tipoFlujoPaciente the tipoFlujoPaciente to set
	 */
	public void setTipoFlujoPaciente(String tipoFlujoPaciente) {
		this.tipoFlujoPaciente = tipoFlujoPaciente;
	}
	
	/**
	 * @return the numDiasAntFechaActual
	 */
	public String getNumDiasAntFechaActual() {
		return numDiasAntFechaActual;
	}


	/**
	 * @param numDiasAntFechaActual the numDiasAntFechaActual to set
	 */
	public void setNumDiasAntFechaActual(String numDiasAntFechaActual) {
		this.numDiasAntFechaActual = numDiasAntFechaActual;
	}

	/**
	 * @return the multiploMinGenCitas
	 */
	public String getMultiploMinGenCitas() {
		return multiploMinGenCitas;
	}


	/**
	 * @param multiploMinGenCitas the multiploMinGenCitas to set
	 */
	public void setMultiploMinGenCitas(String multiploMinGenCitas) {
		this.multiploMinGenCitas = multiploMinGenCitas;
	}

	/**
	 * @return the validaPresupuestoContratado
	 */
	public String getValidaPresupuestoContratado() {
		return validaPresupuestoContratado;
	}


	/**
	 * @param validaPresupuestoContratado the validaPresupuestoContratado to set
	 */
	public void setValidaPresupuestoContratado(String validaPresupuestoContratado) {
		this.validaPresupuestoContratado = validaPresupuestoContratado;
	}

	/**
	 * @return the utilProgOdonPlanTra
	 */
	public String getUtilProgOdonPlanTra() {
		return utilProgOdonPlanTra;
	}


	/**
	 * @param utilProgOdonPlanTra the utilProgOdonPlanTra to set
	 */
	public void setUtilProgOdonPlanTra(String utilProgOdonPlanTra) {
		this.utilProgOdonPlanTra = utilProgOdonPlanTra;
	}

	/**
	 * @return the minutosEsperaAsgCitOdoCad
	 */
	public String getMinutosEsperaAsgCitOdoCad() {
		return minutosEsperaAsgCitOdoCad;
	}


	/**
	 * @param minutosEsperaAsgCitOdoCad the minutosEsperaAsgCitOdoCad to set
	 */
	public void setMinutosEsperaAsgCitOdoCad(String minutosEsperaAsgCitOdoCad) {
		this.minutosEsperaAsgCitOdoCad = minutosEsperaAsgCitOdoCad;
	}


	/**
	 * @return the existePaciente
	 */
	public boolean isExistePaciente() {
		return existePaciente;
	}


	/**
	 * @param existePaciente the existePaciente to set
	 */
	public void setExistePaciente(boolean existePaciente) {
		this.existePaciente = existePaciente;
	}
	
	
	/**
	 * @return the dtoDevolucionAbonosPaciente
	 */
	public DTONotaPaciente getDtoNotaPaciente() {
		return dtoNotaPaciente;
	}


	/**
	 * @param dtoDevolucionAbonosPaciente the dtoDevolucionAbonosPaciente to set
	 */
	public void setDtoNotaPaciente(
			DTONotaPaciente dtoNotaPaciente) {
		this.dtoNotaPaciente = dtoNotaPaciente;
	}
	
	/**
	 * @param PACIENTE
	 */
	

	/**
	 * @return the paciente
	 */
	public DtoPaciente getPaciente() {
		return paciente;
	}

	/**
	 * @param paciente the paciente to set
	 */
	public void setPaciente(DtoPaciente paciente) {
		this.paciente = paciente;
	}
	
	
	public int getCodigoPKPaciente()
	{
		return this.paciente.getCodigo();
	}

	public void setCodigoPKPaciente(int codigo)
	{
		this.paciente.setCodigo(codigo);
	}
	
	public String getTipoIdenPac()    
	{
		return this.paciente.getTipoId();
	}

	public void setTipoIdenPac(String tipoId)
	{
		this.paciente.setTipoId(tipoId);
	}
	
	public String getNumeroIdenPac()
	{
		return this.paciente.getNumeroId();
	}

	public void setNumeroIdenPac(String numeroId)
	{
		this.paciente.setNumeroId(numeroId);
	}
	
	public String getPrimerNomPac() 
	{
		return this.paciente.getPrimerNombre();
	}

	public void setPrimerNomPac(String primerNombre)
	{
		this.paciente.setPrimerNombre(primerNombre);
	}
	
	public String getSegundoNomPac()
	{
		return this.paciente.getSegundoNombre();
	}

	public void setSegundoNomPac(String segundoNombre)
	{
		this.paciente.setSegundoNombre(segundoNombre);
	}
	
	public String getPrimerApellPac()
	{
		return this.paciente.getPrimerApellido();
	}

	public void setPrimerApellPac(String primerApellido)
	{
		this.paciente.setPrimerApellido(primerApellido);
	}
	
	public String getSegundoApellPac()
	{
		return this.paciente.getSegundoApellido();
	}

	public void setSegundoApellPac(String segundoApellido)
	{
		this.paciente.setSegundoApellido(segundoApellido);
	}

	public String getConvenioPac()
	{
		return this.paciente.getConvenio();
	}
	
	public void setConvenioPac(String convenio)
	{
		this.paciente.setConvenio(convenio);
	}
	
	/**
	 * @return the tieneIngresoPaciente
	 */
	public boolean isTieneIngresoPaciente() {
		return tieneIngresoPaciente;
	}

	/**
	 * @param tieneIngresoPaciente the tieneIngresoPaciente to set
	 */
	public void setTieneIngresoPaciente(boolean tieneIngresoPaciente) {
		this.tieneIngresoPaciente = tieneIngresoPaciente;
	}
		
	/**
	 * @param tienePlanTratamientoInactivo the tienePlanTratamientoInactivo to set
	 */
	public void setTienePlanTratamientoInactivo(boolean tienePlanTratamientoInactivo) {
		this.tienePlanTratamientoInactivo = tienePlanTratamientoInactivo;
	}


	/**
	 * @return the tienePlanTratamientoInactivo
	 */
	public boolean isTienePlanTratamientoInactivo() {
		return tienePlanTratamientoInactivo;
	}
	
	/**
	 * @return the nombreCompleto
	 */
	public String getNombreCompleto() {
		return nombreCompleto;
	}


	/**
	 * @param nombreCompleto the nombreCompleto to set
	 */
	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}
	
	
	/**
	 * @return the observacionesNotaPaciente
	 */
	public String getObservacionesNotaPaciente() {
		return observacionesNotaPaciente;
	}


	/**
	 * @param observacionesNotaPaciente the observacionesNotaPaciente to set
	 */
	public void setObservacionesNotaPaciente(String observacionesNotaPaciente) {
		this.observacionesNotaPaciente = observacionesNotaPaciente;
	}

	/**
	 * @return the numeroIdentificacion
	 */
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}


	/**
	 * @param numeroIdentificacion the numeroIdentificacion to set
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}
	
	
	/**
	 * @return the insercionCorrecta
	 */
	public boolean isInsercionCorrecta() {
		return insercionCorrecta;
	}


	/**
	 * @param insercionCorrecta the insercionCorrecta to set
	 */
	public void setInsercionCorrecta(boolean insercionCorrecta) {
		this.insercionCorrecta = insercionCorrecta;
	}
	
	/**
	 * @return the consecutivo
	 */
	public BigDecimal getConsecutivo() {
		return consecutivo;
	}


	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(BigDecimal consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getFechaNota() {
		return fechaNota;
	}


	public void setFechaNota(String fechaNota) {
		this.fechaNota = fechaNota;
	}


	public String getHoraNota() {
		return horaNota;
	}


	public void setHoraNota(String horaNota) {
		this.horaNota = horaNota;
	}


	/**
	 * @return the saldoAnterior
	 */
	public String getSaldoAnterior() {
		return saldoAnterior;
	}


	/**
	 * @param saldoAnterior the saldoAnterior to set
	 */
	public void setSaldoAnterior(String saldoAnterior) {
		this.saldoAnterior = saldoAnterior;
	}


	/**
	 * @return the saldoActual
	 */
	public String getSaldoActual() {
		return saldoActual;
	}


	/**
	 * @param saldoActual the saldoActual to set
	 */
	public void setSaldoActual(String saldoActual) {
		this.saldoActual = saldoActual;
	}


	public String getUsuarioGeneraNota() {
		return usuarioGeneraNota;
	}


	public void setUsuarioGeneraNota(String usuarioGeneraNota) {
		this.usuarioGeneraNota = usuarioGeneraNota;
	}


	/**
	 * @return the loginUsuario
	 */
	public String getLoginUsuario() {
		return loginUsuario;
	}


	/**
	 * @param loginUsuario the loginUsuario to set
	 */
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	/**
	 * @return the indexListaIngreso
	 */
	public int getIndexListaIngreso() {
		return indexListaIngreso;
	}


	/**
	 * @param indexListaIngreso the indexListaIngreso to set
	 */
	public void setIndexListaIngreso(int indexListaIngreso) {
		this.indexListaIngreso = indexListaIngreso;
	}
	
	
	/**
	 * @return the totalNuevoSaldo
	 */
	public String getTotalNuevoSaldo() {
		return totalNuevoSaldo;
	}


	/**
	 * @param totalNuevoSaldo the totalNuevoSaldo to set
	 */
	public void setTotalNuevoSaldo(String totalNuevoSaldo) {
		this.totalNuevoSaldo = totalNuevoSaldo;
	}

	/**
	 * @return the totalValorDevolucion
	 */
	public String getTotalValorDevolucion() {
		return totalValorDevolucion;
	}


	/**
	 * @param totalValorDevolucion the totalValorDevolucion to set
	 */
	public void setTotalValorDevolucion(String totalValorDevolucion) {
		this.totalValorDevolucion = totalValorDevolucion;
	}

	
	/**
	 * @return the controlaAbonoPacienteXIngreso
	 */
	public boolean isControlaAbonoPacienteXIngreso() {
		return controlaAbonoPacienteXIngreso;
	}


	/**
	 * @param controlaAbonoPacienteXIngreso the controlaAbonoPacienteXIngreso to set
	 */
	public void setControlaAbonoPacienteXIngreso(
			boolean controlaAbonoPacienteXIngreso) {
		this.controlaAbonoPacienteXIngreso = controlaAbonoPacienteXIngreso;
	}


	public ArrayList<CentroAtencion> getListaCentrosAtencion() {
		return listaCentrosAtencion;
	}


	public void setListaCentrosAtencion(
			ArrayList<CentroAtencion> listaCentrosAtencion) {
		this.listaCentrosAtencion = listaCentrosAtencion;
	}


	public boolean isInstitucionMultiempresa() {
		return institucionMultiempresa;
	}


	public void setInstitucionMultiempresa(boolean institucionMultiempresa) {
		this.institucionMultiempresa = institucionMultiempresa;
	}


	public CentroAtencion getCentroAtencionOrigen() {
		return centroAtencionOrigen;
	}


	public void setCentroAtencionOrigen(CentroAtencion centroAtencionOrigen) {
		this.centroAtencionOrigen = centroAtencionOrigen;
	}

	/**
	 * @param set centroAtencionOrigen
	 */
	public void setCentroAtencionOrigenHelper(int codigo) {
		boolean asigno = false;
		if (this.getListaCentrosAtencion() != null) {
			for (CentroAtencion centroAtencion : getListaCentrosAtencion()) {
				if (centroAtencion.getConsecutivo() == codigo) {
					asigno = true;
					this.setCentroAtencionOrigen(centroAtencion);
				}
			}
			if (!asigno) {
				this.setCentroAtencionOrigen(null);
			}
		}
	}
	
	/**
	 * @return centroAtencionOrigen
	 */
	public int getCentroAtencionOrigenHelper() {
		if (centroAtencionOrigen == null) {
			return ConstantesBD.codigoNuncaValido;
		}
		return centroAtencionOrigen.getConsecutivo();
		
	}	

	public CentroAtencion getCentroAtencionRegistro() {
		return centroAtencionRegistro;
	}


	public void setCentroAtencionRegistro(CentroAtencion centroAtencionRegistro) {
		this.centroAtencionRegistro = centroAtencionRegistro;
	}

	public String getNaturalezaNotasPaciente() {
		return naturalezaNotasPaciente;
	}

	public void setNaturalezaNotasPaciente(String naturalezaNotasPaciente) {
		this.naturalezaNotasPaciente = naturalezaNotasPaciente;
	}

	public String getNaturalezaNotasPacienteSeleccionada() {
		return naturalezaNotasPacienteSeleccionada;
	}


	public void setNaturalezaNotasPacienteSeleccionada(
			String naturalezaNotasPacienteSeleccionada) {
		this.naturalezaNotasPacienteSeleccionada = naturalezaNotasPacienteSeleccionada;
	}


	public ArrayList<DtoConceptoNotasPacientes> getListaConceptosNotasPacientes() {
		return listaConceptosNotasPacientes;
	}


	public void setListaConceptosNotasPacientes(
			ArrayList<DtoConceptoNotasPacientes> listaConceptosNotasPacientes) {
		this.listaConceptosNotasPacientes = listaConceptosNotasPacientes;
	}
	
	public DtoConceptoNotasPacientes getDtoConceptoNotaPaciente() {
		return dtoConceptoNotaPaciente;
	}


	public void setDtoConceptoNotaPaciente(DtoConceptoNotasPacientes dtoConceptoNotaPaciente) {
		this.dtoConceptoNotaPaciente = dtoConceptoNotaPaciente;
	}


	/**
	 * @param set DtoConceptoNotaPaciente
	 */
	public void setDtoConceptoNotaPacienteHelper(long codigo) {
		boolean asigno = false;
		if (this.getListaConceptosNotasPacientes() != null) {
			for (DtoConceptoNotasPacientes concepto : getListaConceptosNotasPacientes()) {
				if (concepto.getCodigoPk() == codigo) {
					asigno = true;
					this.setDtoConceptoNotaPaciente(concepto);
				}
			}
			if (!asigno) {
				this.setDtoConceptoNotaPaciente(null);
			}
		}
	}
	
	/**
	 * @return ConceptoNotaPaciente
	 */
	public long getDtoConceptoNotaPacienteHelper() {
		if (dtoConceptoNotaPaciente == null) {
			return ConstantesBD.codigoNuncaValido;
		}
		return dtoConceptoNotaPaciente.getCodigoPk();
		
	}


	/**
	 * @param nombreNaturalezaNota the nombreNaturalezaNota to set
	 */
	public void setNombreNaturalezaNota(String nombreNaturalezaNota) {
		this.nombreNaturalezaNota = nombreNaturalezaNota;
	}


	/**
	 * @return the nombreNaturalezaNota
	 */
	public String getNombreNaturalezaNota() {
		if (!UtilidadTexto.isEmpty(naturalezaNotasPacienteSeleccionada)) {
			nombreNaturalezaNota = 
				(String)ValoresPorDefecto.getIntegridadDominio(naturalezaNotasPacienteSeleccionada);
		} else {
			nombreNaturalezaNota = "";
		}
		return nombreNaturalezaNota;
	}


	public ArrayList<EmpresasInstitucion> getListaEmpresaInstitucion() {
		return listaEmpresaInstitucion;
	}


	public void setListaEmpresaInstitucion(
			ArrayList<EmpresasInstitucion> listaEmpresaInstitucion) {
		this.listaEmpresaInstitucion = listaEmpresaInstitucion;
	}

	
	/**
	 * @param set empresaInstitucion
	 */
	public void setEmpresaInstitucionHelper(long codigo) {
		boolean asigno = false;
		if (this.getListaEmpresaInstitucion() != null) {
			for (EmpresasInstitucion empresaInstitucion : getListaEmpresaInstitucion()) {
				if (empresaInstitucion.getCodigo() == codigo) {
					asigno = true;
					this.setEmpresaInstitucion(empresaInstitucion);
				}
			}
			if (!asigno) {
				this.setEmpresaInstitucion(null);
			}
		}
	}
	
	/**
	 * @return empresaInstitucion
	 */
	public long getEmpresaInstitucionHelper() {
		if (empresaInstitucion == null) {
			return ConstantesBD.codigoNuncaValido;
		}
		return empresaInstitucion.getCodigo();
		
	}	

	/**
	 * @param listaUsuarios the listaUsuarios to set
	 */
	public void setListaUsuarios(ArrayList<DtoUsuarioPersona> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}


	/**
	 * @return the listaUsuarios
	 */
	public ArrayList<DtoUsuarioPersona> getListaUsuarios() {
		return listaUsuarios;
	}

	/**
	 * @param usuarioGenera the usuarioGenera to set
	 */
	public void setUsuarioGenera(DtoUsuarioPersona usuarioGenera) {
		this.usuarioGenera = usuarioGenera;
	}


	/**
	 * @return the usuarioGenera
	 */
	public DtoUsuarioPersona getUsuarioGenera() {
		return usuarioGenera;
	}


	/**
	 * @param set centroAtencionOrigen
	 */
	public void setUsuarioGeneraHelper(String login) {
		boolean asigno = false;
		if (this.getListaUsuarios() != null) {
			for (DtoUsuarioPersona usuario : getListaUsuarios()) {
				if (usuario.getLogin().equals(login)) {
					asigno = true;
					this.setUsuarioGenera(usuario);
				}
			}
			if (!asigno) {
				this.setUsuarioGenera(null);
			}
		}
	}
	
	/**
	 * @return centroAtencionOrigen
	 */
	public String getUsuarioGeneraHelper() {
		if (usuarioGenera == null) {
			return ConstantesBD.codigoNuncaValido+"";
		}
		return usuarioGenera.getLogin();
		
	}	

	/**
	 * @param fechaInicialBusqueda the fechaInicialBusqueda to set
	 */
	public void setFechaInicialBusqueda(String fechaInicialBusqueda) {
		this.fechaInicialBusqueda = fechaInicialBusqueda;
	}


	/**
	 * @return the fechaInicialBusqueda
	 */
	public String getFechaInicialBusqueda() {
		return fechaInicialBusqueda;
	}


	/**
	 * @param fechaFinalBusqueda the fechaFinalBusqueda to set
	 */
	public void setFechaFinalBusqueda(String fechaFinalBusqueda) {
		this.fechaFinalBusqueda = fechaFinalBusqueda;
	}


	/**
	 * @return the fechaFinalBusqueda
	 */
	public String getFechaFinalBusqueda() {
		return fechaFinalBusqueda;
	}


	public String getNumeroNotaInicialBusqueda() {
		return numeroNotaInicialBusqueda;
	}


	public void setNumeroNotaInicialBusqueda(String numeroNotaInicialBusqueda) {
		this.numeroNotaInicialBusqueda = numeroNotaInicialBusqueda;
	}


	public String getNumeroNotaFinalBusqueda() {
		return numeroNotaFinalBusqueda;
	}


	public void setNumeroNotaFinalBusqueda(String numeroNotaFinalBusqueda) {
		this.numeroNotaFinalBusqueda = numeroNotaFinalBusqueda;
	}


	public String getNaturalezaNotaBusqueda() {
		return naturalezaNotaBusqueda;
	}


	public void setNaturalezaNotaBusqueda(String naturalezaNotaBusqueda) {
		this.naturalezaNotaBusqueda = naturalezaNotaBusqueda;
	}

	/**
	 * @param centrosAtencionSeleccionados the centrosAtencionSeleccionados to set
	 */
	public void setCentrosAtencionSeleccionados(
			String[] centrosAtencionSeleccionados) {
		this.centrosAtencionSeleccionados = centrosAtencionSeleccionados;
	}


	/**
	 * @return the centrosAtencionSeleccionados
	 */
	public String[] getCentrosAtencionSeleccionados() {
		return centrosAtencionSeleccionados;
	}


	/**
	 * @param empresaInsitucion the empresaInsitucion to set
	 */
	public void setEmpresaInstitucion(EmpresasInstitucion empresaInstitucion) {
		this.empresaInstitucion = empresaInstitucion;
	}


	/**
	 * @return the empresaInsitucion
	 */
	public EmpresasInstitucion getEmpresaInstitucion() {
		return empresaInstitucion;
	}


	public LinkedHashMap<String, DtoNotasPorNaturaleza> getMapaDtoResumenNotasPaciente() {
		return mapaDtoResumenNotasPaciente;
	}


	public void setMapaDtoResumenNotasPaciente(
			LinkedHashMap<String, DtoNotasPorNaturaleza> mapaDtoResumenNotasPaciente) {
		this.mapaDtoResumenNotasPaciente = mapaDtoResumenNotasPaciente;
	}


	/**
	 * @param centrosAtencionSeleccionadosTmp the centrosAtencionSeleccionadosTmp to set
	 */
	public void setCentrosAtencionSeleccionadosTmp(
			String[] centrosAtencionSeleccionadosTmp) {
		this.centrosAtencionSeleccionadosTmp = centrosAtencionSeleccionadosTmp;
	}


	/**
	 * @return the centrosAtencionSeleccionadosTmp
	 */
	public String[] getCentrosAtencionSeleccionadosTmp() {
		return centrosAtencionSeleccionadosTmp;
	}


	/**
	 * @param manejoEspecialInstOdonto the manejoEspecialInstOdonto to set
	 */
	public void setManejoEspecialInstOdonto(boolean manejoEspecialInstOdonto) {
		this.manejoEspecialInstOdonto = manejoEspecialInstOdonto;
	}


	/**
	 * @return the manejoEspecialInstOdonto
	 */
	public boolean isManejoEspecialInstOdonto() {
		return manejoEspecialInstOdonto;
	}
	
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public String getEsDescendente() {
		return esDescendente;
	}

	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}


	/**
	 * @param claveOrdenar the claveOrdenar to set
	 */
	public void setClaveOrdenar(String claveOrdenar) {
		this.claveOrdenar = claveOrdenar;
	}


	/**
	 * @return the claveOrdenar
	 */
	public String getClaveOrdenar() {
		return claveOrdenar;
	}


	/**
	 * @param naturalezaOrdenar the naturalezaOrdenar to set
	 */
	public void setNaturalezaOrdenar(String naturalezaOrdenar) {
		this.naturalezaOrdenar = naturalezaOrdenar;
	}


	/**
	 * @return the naturalezaOrdenar
	 */
	public String getNaturalezaOrdenar() {
		return naturalezaOrdenar;
	}


	/**
	 * @param codigoPkNotaSeleccionado the codigoPkNotaSeleccionado to set
	 */
	public void setCodigoPkNotaSeleccionado(String codigoPkNotaSeleccionado) {
		this.codigoPkNotaSeleccionado = codigoPkNotaSeleccionado;
	}


	/**
	 * @return the codigoPkNotaSeleccionado
	 */
	public String getCodigoPkNotaSeleccionado() {
		return codigoPkNotaSeleccionado;
	}


	/**
	 * @param esUnicoResultado the esUnicoResultado to set
	 */
	public void setEsUnicoResultado(boolean esUnicoResultado) {
		this.esUnicoResultado = esUnicoResultado;
	}


	/**
	 * @return the esUnicoResultado
	 */
	public boolean isEsUnicoResultado() {
		return esUnicoResultado;
	}


	/**
	 * @param busquedaPorRango the busquedaPorRango to set
	 */
	public void setBusquedaPorRango(boolean busquedaPorRango) {
		this.busquedaPorRango = busquedaPorRango;
	}


	/**
	 * @return the busquedaPorRango
	 */
	public boolean isBusquedaPorRango() {
		return busquedaPorRango;
	}


	/**
	 * @param listaDtoResumenNotasPaciente the listaDtoResumenNotasPaciente to set
	 */
	public void setListaDtoResumenNotasPaciente(
			ArrayList<DtoResumenNotasPaciente> listaDtoResumenNotasPaciente) {
		this.listaDtoResumenNotasPaciente = listaDtoResumenNotasPaciente;
	}


	/**
	 * @return the listaDtoResumenNotasPaciente
	 */
	public ArrayList<DtoResumenNotasPaciente> getListaDtoResumenNotasPaciente() {
		return listaDtoResumenNotasPaciente;
	}


	/**
	 * @param nombreArchivo the nombreArchivo to set
	 */
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}


	/**
	 * @param tipoSalida the tipoSalida to set
	 */
	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}


	/**
	 * @return the tipoSalida
	 */
	public String getTipoSalida() {
		return tipoSalida;
	}


	/**
	 * @return the nombreArchivo
	 */
	public String getNombreArchivo() {
		return nombreArchivo;
	}

	


}
