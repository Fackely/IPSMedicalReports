package com.princetonsa.actionform.historiaClinica;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.dto.historiaClinica.DtoEvolucion;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.mundo.atencion.SignoVital;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;
import com.princetonsa.mundo.parametrizacion.SignosVitales;
import com.servinte.axioma.dto.historiaClinica.HistoricoImagenPlantillaDto;

public class EvolucionesForm extends ValidatorForm 
{

	private String estado;
	
	private DtoPlantilla plantilla;
	
	private DtoEvolucion evolucion;
	
	
	private String fechaMaximaEvolucionFormatoBD="";
	
	private String fechaMaximaValoracionFormatoBD="";
	
	private String horaMaximaEvolucion="";
	
	private String horaMaximaValoracion="";

	private String fechaPrimeraValoracionFormatoBD="";
		
	private String horaPrimeraValoracion="";

	
	private String fechaCuentaOAdmision="";
	
	
	
	
	//
	private String fechaEvolucion;
	
	private String warningOrdenSalida;
	
	private String warningReferencias="";
	
	private boolean esAdjunto;
	
	private boolean esTratante=false;
	
	private boolean modificableAsocio;
	
	private boolean puedoFinalizarManejo;
	
	private ArrayList<SignoVital> signosVitales;
	
	private HashMap mapaDietaHistorico;
	
	private HashMap mapaDieta;
	
	//Atributo para saber si se muestra o no informacion del paciente en la impresion 
	private boolean ocultarInfoPaciente;	
	
	//---------------------------SIGNOS VITALES 24 HORAS-----------------------------------------------//
    /**
     * Coleccion para traer el Listado de tipos Signos Vitales parametrizados en enfermer?a
     * por centro de costo instituci?n    
     */
    private Collection signosVitalesInstitucionCcosto;
    
    /**
     * Coleccion para traer el listado hist?rico de los signos vitales fijos de las
     * ?ltimas 24 horas
     */
    private Collection signosVitalesFijosHisto;
    
    /**
     * Collecci?n para traer el listado hist?rico de los signos vitales parametrizados por 
     * instituci?n centro de costo de las ?ltimas 24 horas
     */
    private Collection signosVitalesParamHisto;
    
    /**
     * Collecci?n para traer el listado con los c?digos hist?ricos, fecha registro y hora registro,
     * de los signos vitales fijos y parametrizados de las ?ltimas 24 horas  
     */
    private Collection signosVitalesHistoTodos;
	
   
    /**
     * si la conducta a seguir es remitir
     * este atributo se llena
     */
    private String acronimoTipoReferencia;
    
    /**
     * mapa con las conductas a seguir 
     */
    private HashMap conductasSeguirTagMap;
    
    /**
     * Variable para llevar el valor de la comparacion de las especialidades
     */
    private boolean controlInterpretacion;
    
    private String ordenSalida="false";
    
    private String otroDestinoSalida="";
    
    private int numDiagnosticosDefinitivos;
    
    private String diagnosticoDefinitivo;
	
    
    
    private HashMap<String, Object> diagnosticosRelacionados;
	
	/**
	 * Variable que obtiene el listado de los diagnosticos seleccionados
	 */
	private String diagnosticosSeleccionados;
    
    
	
	//Menejar Evolucion Vieja.
	private int numeroSolicitudPidioConvertirMedicoATratante;
	
	private boolean deseaFinalizarAtencion;
	
	private String notaFinalizacionAtencion;
	
	private String convertirMedicoATratante;
	
	
	private String tipoPaciente;
	
	private int ultimaViaIngreso;
	
	
	private HashMap especialidadesCuentaMapa;
	
	private boolean deboMostrarMensajeCancelacionTratante=false;
	
	private String puedeDarOrdenSalida="false";
	
	private String tipoEvolucion="";
	
	
	///Reingreso
	private HashMap mapaE;
	
	private int indI;
	
	private boolean aceptarReingreso; // indica si se debe aceptar el reingreso
	
	
	
	private int codigoUltimaEvolucion;
	
	////
	private String tipoMonitoreoActual;
	
	
	//**********ATRIBUTOS PARA LA IMPRESION********************************
	private ParametrizacionInstitucion institucion = new ParametrizacionInstitucion();
	private String nombreCentroAtencion = "";
	//***********************************************************************
	
	private boolean ocultarEncabezado;
	private boolean vieneDeHistorico; //para verificar si el llamado a evoluciones viene del historico
	private boolean vieneDeHistoriaAtenciones; //para verificar si el llamado a evoluciones viene de historia de atenciones
	
	private String debeDarMotivoRegresionEgreso="false";
	
	private String motivoReversionEgreso="";
	
	/** especialidad seleccionada*/
	private String selectEspecialidad = "";
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//Atributos para el manejo de las curvas de crecimiento
	private List<HistoricoImagenPlantillaDto> dtoHistoricoImagenPlantilla;
	private HistoricoImagenPlantillaDto curvaSeleccionada;
	private Boolean mostrarDetalles;
	private Integer indiceCurvaSeleccionada;
	private String edadCalculada;
	private String funcionalidad;
		
	public List<HistoricoImagenPlantillaDto> getDtoHistoricoImagenPlantilla() {
		return dtoHistoricoImagenPlantilla;
	}
	public void setDtoHistoricoImagenPlantilla(List<HistoricoImagenPlantillaDto> dtoHistoricoImagenPlantilla) {
		this.dtoHistoricoImagenPlantilla = dtoHistoricoImagenPlantilla;
	}
	public HistoricoImagenPlantillaDto getCurvaSeleccionada() {
		return curvaSeleccionada;
	}
	public void setCurvaSeleccionada(HistoricoImagenPlantillaDto curvaSeleccionada) {
		this.curvaSeleccionada = curvaSeleccionada;
	}
	public Boolean getMostrarDetalles() {
		return mostrarDetalles;
	}
	public void setMostrarDetalles(Boolean mostrarDetalles) {
		this.mostrarDetalles = mostrarDetalles;
	}
	public Integer getIndiceCurvaSeleccionada() {
		return indiceCurvaSeleccionada;
	}
	public void setIndiceCurvaSeleccionada(Integer indiceCurvaSeleccionada) {
		this.indiceCurvaSeleccionada = indiceCurvaSeleccionada;
	}
	public String getEdadCalculada() {
		return edadCalculada;
	}
	public void setEdadCalculada(String edadCalculada) {
		this.edadCalculada = edadCalculada;
	}
	public String getFuncionalidad() {
		return funcionalidad;
	}
	public void setFuncionalidad(String funcionalidad) {
		this.funcionalidad = funcionalidad;
	}
	//fin curvas
	
	
	// para identificar si tiene permisos para imprimir
	
	private Boolean permisoImpresionHCV=false;
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 *
	 */
	public void reset()
	{
		
		this.estado="";
		this.fechaEvolucion=UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual());
		this.warningOrdenSalida="";
		this.esAdjunto=false;
		this.esTratante=false;
		this.puedoFinalizarManejo=false;
		this.plantilla= new DtoPlantilla();
		this.evolucion= new DtoEvolucion();
		this.signosVitales= new ArrayList<SignoVital>();
		this.mapaDietaHistorico= new HashMap();
		this.mapaDieta= new HashMap();
		
		this.ordenSalida="false";
		this.otroDestinoSalida="";
		
		this.signosVitalesInstitucionCcosto= new ArrayList();
		this.signosVitalesFijosHisto= new ArrayList();
		this.signosVitalesHistoTodos= new ArrayList();
		this.signosVitalesParamHisto= new ArrayList();
		
		this.acronimoTipoReferencia="";
		this.conductasSeguirTagMap= new HashMap();
		this.controlInterpretacion=false;
		
		this.numDiagnosticosDefinitivos=ConstantesBD.codigoNuncaValido;
		this.diagnosticoDefinitivo="";
		
		
		this.diagnosticosRelacionados=new HashMap<String, Object>();
		this.diagnosticosSeleccionados="";
		
		this.fechaMaximaEvolucionFormatoBD="";
		this.horaMaximaEvolucion="";
		this.fechaPrimeraValoracionFormatoBD="";
		this.horaMaximaValoracion="";
		this.fechaMaximaValoracionFormatoBD="";
		this.horaPrimeraValoracion="";
		
		this.fechaCuentaOAdmision="";
		
		this.numeroSolicitudPidioConvertirMedicoATratante=ConstantesBD.codigoNuncaValido;
		this.deseaFinalizarAtencion=false;
		this.notaFinalizacionAtencion="";
		this.convertirMedicoATratante="p";
		
		this.tipoPaciente="";
		this.ultimaViaIngreso=ConstantesBD.codigoNuncaValido;
		
		this.especialidadesCuentaMapa=new HashMap();
		this.deboMostrarMensajeCancelacionTratante=false;
		this.tipoEvolucion="";
		this.puedeDarOrdenSalida="false";
		
		this.mapaE= new HashMap();
		mapaE.put("numRegistros", 0);
		this.indI=0;
		this.aceptarReingreso = false;
		
		this.codigoUltimaEvolucion=ConstantesBD.codigoNuncaValido;
		this.warningReferencias="";
		
		//Atributos para la impresion de la evolucion
		this.institucion = new ParametrizacionInstitucion();
		this.nombreCentroAtencion = "";
		this.ocultarEncabezado=false;
		this.vieneDeHistorico = false;
		this.vieneDeHistoriaAtenciones = false;
		this.tipoMonitoreoActual="";
		this.debeDarMotivoRegresionEgreso="false";
		this.motivoReversionEgreso="";
		this.modificableAsocio= false;
		this.selectEspecialidad = "";
		this.permisoImpresionHCV=false;
	}

	
	public Boolean getPermisoImpresionHCV() {
		return permisoImpresionHCV;
	}
	public void setPermisoImpresionHCV(Boolean permisoImpresionHCV) {
		this.permisoImpresionHCV = permisoImpresionHCV;
	}
	/**
	 * 
	 *
	 */
	public void resetMensaje()
	{
		this.warningOrdenSalida="";
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * 
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFechaEvolucion() {
		return fechaEvolucion;
	}

	/**
	 * 
	 * @param fechaEvolucion
	 */
	public void setFechaEvolucion(String fechaEvolucion) {
		this.fechaEvolucion = fechaEvolucion;
	}

	/**
	 * 
	 * @return
	 */
	public String getWarningOrdenSalida() {
		return warningOrdenSalida;
	}

	/**
	 * 
	 * @param warningOrdenSalida
	 */
	public void setWarningOrdenSalida(String warningOrdenSalida) {
		this.warningOrdenSalida = warningOrdenSalida;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isEsAdjunto() {
		return esAdjunto;
	}

	/**
	 * 
	 * @param esAdjunto
	 */
	public void setEsAdjunto(boolean esAdjunto) {
		this.esAdjunto = esAdjunto;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getEsAdjunto() {
		return esAdjunto;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isEsTratante() {
		return esTratante;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getEsTratante() {
		return esTratante;
	}

	/**
	 * 
	 * @param esTratante
	 */
	public void setEsTratante(boolean esTratante) {
		this.esTratante = esTratante;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isPuedoFinalizarManejo() {
		return puedoFinalizarManejo;
	}

	/**
	 * 
	 * @param puedoFinalizarManejo
	 */
	public void setPuedoFinalizarManejo(boolean puedoFinalizarManejo) {
		this.puedoFinalizarManejo = puedoFinalizarManejo;
	}

	/**
	 * 
	 * @return
	 */
	public DtoPlantilla getPlantilla() {
		return plantilla;
	}

	/**
	 * 
	 * @param plantilla
	 */
	public void setPlantilla(DtoPlantilla plantilla) {
		this.plantilla = plantilla;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<SignoVital> getSignosVitales() {
		return signosVitales;
	}

	/**
	 * 
	 * @param signosVitales
	 */
	public void setSignosVitales(ArrayList<SignoVital> signosVitales) {
		this.signosVitales = signosVitales;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaDietaHistorico() {
		return mapaDietaHistorico;
	}

	/**
	 * 
	 * @param mapaDietaHistorico
	 */
	public void setMapaDietaHistorico(HashMap mapaDietaHistorico) {
		this.mapaDietaHistorico = mapaDietaHistorico;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaDietaHistorico(Object key) {
		return mapaDietaHistorico.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param dato
	 */
	public void setMapaDietaHistorico(Object key, Object dato) {
		this.mapaDietaHistorico.put(key, dato);
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaDieta() {
		return mapaDieta;
	}

	/**
	 * 
	 * @param mapaDieta
	 */
	public void setMapaDieta(HashMap mapaDieta) {
		this.mapaDieta = mapaDieta;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaDieta(Object key) {
		return mapaDieta.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param dato
	 */
	public void setMapaDieta(Object key, Object dato) {
		this.mapaDieta.put(key, dato);
	}

	/**
	 * 
	 * @return
	 */
	public String getAcronimoTipoReferencia() {
		return acronimoTipoReferencia;
	}

	/**
	 * 
	 * @param acronimoTipoReferencia
	 */
	public void setAcronimoTipoReferencia(String acronimoTipoReferencia) {
		this.acronimoTipoReferencia = acronimoTipoReferencia;
	}

	/**
	 * 
	 * @return
	 */
	public int getCodigoConductaSeguir() {
		return evolucion.getCodigoConductaSeguir();
	}

	/**
	 * 
	 * @param codigoConductaSeguir
	 */
	public void setCodigoConductaSeguir(int codigoConductaSeguir) {
		this.evolucion.setCodigoConductaSeguir(codigoConductaSeguir);
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getConductasSeguirTagMap() {
		return conductasSeguirTagMap;
	}

	/**
	 * 
	 * @param conductasSeguirTagMap
	 */
	public void setConductasSeguirTagMap(HashMap conductasSeguirTagMap) {
		this.conductasSeguirTagMap = conductasSeguirTagMap;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isControlInterpretacion() {
		return controlInterpretacion;
	}

	/**
	 * 
	 * @param controlInterpretacion
	 */
	public void setControlInterpretacion(boolean controlInterpretacion) {
		this.controlInterpretacion = controlInterpretacion;
	}

	/**
	 * 
	 * @return
	 */
	public Collection getSignosVitalesFijosHisto() {
		return signosVitalesFijosHisto;
	}

	/**
	 * 
	 * @param signosVitalesFijosHisto
	 */
	public void setSignosVitalesFijosHisto(Collection signosVitalesFijosHisto) {
		this.signosVitalesFijosHisto = signosVitalesFijosHisto;
	}

	/**
	 * 
	 * @return
	 */
	public Collection getSignosVitalesHistoTodos() {
		return signosVitalesHistoTodos;
	}

	/**
	 * 
	 * @param signosVitalesHistoTodos
	 */
	public void setSignosVitalesHistoTodos(Collection signosVitalesHistoTodos) {
		this.signosVitalesHistoTodos = signosVitalesHistoTodos;
	}

	/**
	 * 
	 * @return
	 */
	public Collection getSignosVitalesInstitucionCcosto() {
		return signosVitalesInstitucionCcosto;
	}

	/**
	 * 
	 * @param signosVitalesInstitucionCcosto
	 */
	public void setSignosVitalesInstitucionCcosto(
			Collection signosVitalesInstitucionCcosto) {
		this.signosVitalesInstitucionCcosto = signosVitalesInstitucionCcosto;
	}

	/**
	 * 
	 * @return
	 */
	public Collection getSignosVitalesParamHisto() {
		return signosVitalesParamHisto;
	}

	/**
	 * 
	 * @param signosVitalesParamHisto
	 */
	public void setSignosVitalesParamHisto(Collection signosVitalesParamHisto) {
		this.signosVitalesParamHisto = signosVitalesParamHisto;
	}

	/**
	 * 
	 * @return
	 */
	public DtoEvolucion getEvolucion() {
		return evolucion;
	}

	/**
	 * 
	 * @param evolucion
	 */
	public void setEvolucion(DtoEvolucion evolucion) {
		this.evolucion = evolucion;
	}

	/**
	 * 
	 * @return
	 */
	public String getOrdenSalida() {
		return ordenSalida;
	}

	/**
	 * 
	 * @param ordenSalida
	 */
	public void setOrdenSalida(String ordenSalida) {
		this.ordenSalida = ordenSalida;
	}

	/**
	 * 
	 * @return
	 */
	public String getOtroDestinoSalida() {
		return otroDestinoSalida;
	}

	/**
	 * 
	 * @param otroDestinoSalida
	 */
	public void setOtroDestinoSalida(String otroDestinoSalida) {
		this.otroDestinoSalida = otroDestinoSalida;
	}

	/**
	 * 
	 * @return
	 */
	public int getNumDiagnosticosDefinitivos() {
		return numDiagnosticosDefinitivos;
	}

	/**
	 * 
	 * @param numDiagnosticosDefinitivos
	 */
	public void setNumDiagnosticosDefinitivos(int numDiagnosticosDefinitivos) {
		this.numDiagnosticosDefinitivos = numDiagnosticosDefinitivos;
	}

	/**
	 * 
	 * @return
	 */
	public String getDiagnosticoDefinitivo() {
		return diagnosticoDefinitivo;
	}

	/**
	 * 
	 * @param diagnosticoDefinitivo
	 */
	public void setDiagnosticoDefinitivo(String diagnosticoDefinitivo) {
		this.diagnosticoDefinitivo = diagnosticoDefinitivo;
	}

	/**
	 * 
	 * @param numRegistros
	 */
	public void setNumDiagRelacionados(int numRegistros)
	{
		this.diagnosticosRelacionados.put("numRegistros",numRegistros);
	}
	
	/**
	 * Método para obtener el número de diagnósticos relacionados
	 * @return
	 */
	public int getNumDiagRelacionados()
	{
		return Utilidades.convertirAEntero(this.getDiagnosticosRelacionados("numRegistros")+"", true);
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getDiagnosticosRelacionados() {
		return diagnosticosRelacionados;
	}

	/**
	 * 
	 * @param diagnosticosRelacionados
	 */
	public void setDiagnosticosRelacionados(
			HashMap<String, Object> diagnosticosRelacionados) {
		this.diagnosticosRelacionados = diagnosticosRelacionados;
	}

	/**
	 * 
	 * @return
	 */
	public String getDiagnosticosSeleccionados() {
		return diagnosticosSeleccionados;
	}

	/**
	 * 
	 * @param diagnosticosSeleccionados
	 */
	public void setDiagnosticosSeleccionados(String diagnosticosSeleccionados) {
		this.diagnosticosSeleccionados = diagnosticosSeleccionados;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getDiagnosticosRelacionados(String key) {
		return diagnosticosRelacionados.get(key);
	}

	/**
	 * @param diagnosticosRelacionados the diagnosticosRelacionados to set
	 */
	public void setDiagnosticosRelacionados(String key,Object obj) {
		this.diagnosticosRelacionados.put(key,obj);
	}

	/**
	 * 
	 * @return
	 */
	public String getFechaMaximaEvolucionFormatoBD() {
		return fechaMaximaEvolucionFormatoBD;
	}

	/**
	 * 
	 * @param fechaMaximaEvolucionFormatoBD
	 */
	public void setFechaMaximaEvolucionFormatoBD(
			String fechaMaximaEvolucionFormatoBD) {
		this.fechaMaximaEvolucionFormatoBD = fechaMaximaEvolucionFormatoBD;
	}

	/**
	 * 
	 * @return
	 */
	public String getHoraMaximaEvolucion() {
		return horaMaximaEvolucion;
	}

	/**
	 * 
	 * @param horaMaximaEvolucion
	 */
	public void setHoraMaximaEvolucion(String horaMaximaEvolucion) {
		this.horaMaximaEvolucion = horaMaximaEvolucion;
	}

	/**
	 * 
	 * @return
	 */
	public String getFechaPrimeraValoracionFormatoBD() {
		return fechaPrimeraValoracionFormatoBD;
	}

	/**
	 * 
	 * @param fechaPrimeraValoracionFormatoBD
	 */
	public void setFechaPrimeraValoracionFormatoBD(
			String fechaPrimeraValoracionFormatoBD) {
		this.fechaPrimeraValoracionFormatoBD = fechaPrimeraValoracionFormatoBD;
	}

	/**
	 * 
	 * @return
	 */
	public String getHoraMaximaValoracion() {
		return horaMaximaValoracion;
	}

	/**
	 * 
	 * @param horaMaximaValoracion
	 */
	public void setHoraMaximaValoracion(String horaMaximaValoracion) {
		this.horaMaximaValoracion = horaMaximaValoracion;
	}

	/**
	 * 
	 * @return
	 */
	public String getFechaMaximaValoracionFormatoBD() {
		return fechaMaximaValoracionFormatoBD;
	}

	/**
	 * 
	 * @param fechaMaximaValoracionFormatoBD
	 */
	public void setFechaMaximaValoracionFormatoBD(
			String fechaMaximaValoracionFormatoBD) {
		this.fechaMaximaValoracionFormatoBD = fechaMaximaValoracionFormatoBD;
	}

	/**
	 * 
	 * @return
	 */
	public String getHoraPrimeraValoracion() {
		return horaPrimeraValoracion;
	}

	/**
	 * 
	 * @param horaPrimeraValoracion
	 */
	public void setHoraPrimeraValoracion(String horaPrimeraValoracion) {
		this.horaPrimeraValoracion = horaPrimeraValoracion;
	}

	/**
	 * 
	 * @return
	 */
	public String getFechaCuentaOAdmision() {
		return fechaCuentaOAdmision;
	}

	/**
	 * 
	 * @param fechaCuentaOAdmision
	 */
	public void setFechaCuentaOAdmision(String fechaCuentaOAdmision) {
		this.fechaCuentaOAdmision = fechaCuentaOAdmision;
	}

	/**
	 * 
	 * @return
	 */
	public int getNumeroSolicitudPidioConvertirMedicoATratante() {
		return numeroSolicitudPidioConvertirMedicoATratante;
	}

	/**
	 * 
	 * @param numeroSolicitudPidioConvertirMedicoATratante
	 */
	public void setNumeroSolicitudPidioConvertirMedicoATratante(
			int numeroSolicitudPidioConvertirMedicoATratante) {
		this.numeroSolicitudPidioConvertirMedicoATratante = numeroSolicitudPidioConvertirMedicoATratante;
	}

	/**
	 * 
	 * @return
	 */
	public boolean getDeseaFinalizarAtencion() {
		return deseaFinalizarAtencion;
	}

	/**
	 * @param b
	 */
	public void setDeseaFinalizarAtencion(boolean b) {
		deseaFinalizarAtencion = b;
	}

	/**
	 * @return
	 */
	public String getNotaFinalizacionAtencion() {
		return notaFinalizacionAtencion;
	}

	/**
	 * @param string
	 */
	public void setNotaFinalizacionAtencion(String string) {
		notaFinalizacionAtencion = string;
	}

	/**
	 * 
	 * @return
	 */
	public String getConvertirMedicoATratante() {
		return convertirMedicoATratante;
	}

	/**
	 * 
	 * @param convertirMedicoATratante
	 */
	public void setConvertirMedicoATratante(String convertirMedicoATratante) {
		this.convertirMedicoATratante = convertirMedicoATratante;
	}


	public String getTipoPaciente() {
		return tipoPaciente;
	}

	/**
	 * 
	 * @param tipoPaciente
	 */
	public void setTipoPaciente(String tipoPaciente) {
		this.tipoPaciente = tipoPaciente;
	}

	/**
	 * 
	 * @return
	 */
	public int getUltimaViaIngreso() {
		return ultimaViaIngreso;
	}

	/**
	 * 
	 * @param ultimaViaIngreso
	 */
	public void setUltimaViaIngreso(int ultimaViaIngreso) {
		this.ultimaViaIngreso = ultimaViaIngreso;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getEspecialidadesCuentaMapa() {
		return especialidadesCuentaMapa;
	}

	/**
	 * 
	 * @param especialidadesCuentaMapa
	 */
	public void setEspecialidadesCuentaMapa(HashMap especialidadesCuentaMapa) {
		this.especialidadesCuentaMapa = especialidadesCuentaMapa;
	}
	
	/**
     * @return Returns the deboMostrarMensajeCancelacionTratante.
     */
    public boolean getDeboMostrarMensajeCancelacionTratante()
    {
        return deboMostrarMensajeCancelacionTratante;
    }
    
    /**
     * @param deboMostrarMensajeCancelacionTratante The deboMostrarMensajeCancelacionTratante to set.
     */
    public void setDeboMostrarMensajeCancelacionTratante(
            boolean deboMostrarMensajeCancelacionTratante)
    {
        this.deboMostrarMensajeCancelacionTratante = deboMostrarMensajeCancelacionTratante;
    }

    /**
     * 
     * @return
     */
	public String getTipoEvolucion() {
		return tipoEvolucion;
	}

	/**
	 * 
	 * @param tipoEvolucion
	 */
	public void setTipoEvolucion(String tipoEvolucion) {
		this.tipoEvolucion = tipoEvolucion;
	}

	/**
	 * 
	 * @return
	 */
	public String getPuedeDarOrdenSalida() {
		return puedeDarOrdenSalida;
	}

	/**
	 * 
	 * @param puedeDarOrdenSalida
	 */
	public void setPuedeDarOrdenSalida(String puedeDarOrdenSalida) {
		this.puedeDarOrdenSalida = puedeDarOrdenSalida;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaE() {
		return mapaE;
	}
	
	/**
	 * 
	 * @param mapaE
	 */
	public void setMapaE(HashMap mapaE) {
		this.mapaE = mapaE;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaE(String key) {
		return mapaE.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaE(String key, Object value) {
		this.mapaE.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public int getIndI() {
		return indI;
	}

	/**
	 * 
	 * @param indI
	 */
	public void setIndI(int indI) {
		this.indI = indI;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isAceptarReingreso() {
		return aceptarReingreso;
	}

	/**
	 * 
	 * @param aceptarReingreso
	 */
	public void setAceptarReingreso(boolean aceptarReingreso) {
		this.aceptarReingreso = aceptarReingreso;
	}

	/**
	 * 
	 * @return
	 */
	public int getCodigoUltimaEvolucion() {
		return codigoUltimaEvolucion;
	}

	/**
	 * 
	 * @param codigoUltimaEvolucion
	 */
	public void setCodigoUltimaEvolucion(int codigoUltimaEvolucion) {
		this.codigoUltimaEvolucion = codigoUltimaEvolucion;
	}

	/**
	 * 
	 * @return
	 */
	public String getWarningReferencias() {
		return warningReferencias;
	}

	/**
	 * 
	 * @param warningReferencias
	 */
	public void setWarningReferencias(String warningReferencias) {
		this.warningReferencias = warningReferencias;
	}
	
	
	
	/**
	 * @return the institucion
	 */
	public ParametrizacionInstitucion getInstitucion() {
		return institucion;
	}


	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(ParametrizacionInstitucion institucion) {
		this.institucion = institucion;
	}


	/**
	 * @return the nombreCentroAtencion
	 */
	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}


	/**
	 * @param nombreCentroAtencion the nombreCentroAtencion to set
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}


	/**
	 * @return the ocultarEncabezado
	 */
	public boolean isOcultarEncabezado() {
		return ocultarEncabezado;
	}


	/**
	 * @param ocultarEncabezado the ocultarEncabezado to set
	 */
	public void setOcultarEncabezado(boolean ocultarEncabezado) {
		this.ocultarEncabezado = ocultarEncabezado;
	}
	

	/**
	 * 
	 * @return
	 */
	public String getTipoMonitoreoActual() {
		return tipoMonitoreoActual;
	}

	/**
	 * @return the vieneDeHistorico
	 */
	public boolean isVieneDeHistorico() {
		return vieneDeHistorico;
	}


	/**
	 * @param vieneDeHistorico the vieneDeHistorico to set
	 */
	public void setVieneDeHistorico(boolean vieneDeHistorico) {
		this.vieneDeHistorico = vieneDeHistorico;
	}
	
	/**
	 * Métiodo que veriffica si el llamado a la evolucion es por el flujo
	 * normal u otro método
	 * @return the vieneNormal
	 */
	public boolean isVieneNormal() 
	{
		boolean normal = true;
		
		if(this.vieneDeHistorico||this.vieneDeHistoriaAtenciones)
			normal = false;
		
		return normal;
	}


	/**
	 * 
	 * @param tipoMonitoreoActual
	 */
	public void setTipoMonitoreoActual(String tipoMonitoreoActual) {
		this.tipoMonitoreoActual = tipoMonitoreoActual;
	}


	/**
	 * @return the vieneDeHistoriaAtenciones
	 */
	public boolean isVieneDeHistoriaAtenciones() {
		return vieneDeHistoriaAtenciones;
	}


	/**
	 * @param vieneDeHistoriaAtenciones the vieneDeHistoriaAtenciones to set
	 */
	public void setVieneDeHistoriaAtenciones(boolean vieneDeHistoriaAtenciones) {
		this.vieneDeHistoriaAtenciones = vieneDeHistoriaAtenciones;
	}


	/**
	 * @return the ocultarInfoPaciente
	 */
	public boolean isOcultarInfoPaciente() {
		return ocultarInfoPaciente;
	}


	/**
	 * @param ocultarInfoPaciente the ocultarInfoPaciente to set
	 */
	public void setOcultarInfoPaciente(boolean ocultarInfoPaciente) {
		this.ocultarInfoPaciente = ocultarInfoPaciente;
	}

	/**
	 * 
	 * @return
	 */
	public String getDebeDarMotivoRegresionEgreso() {
		return debeDarMotivoRegresionEgreso;
	}

	/**
	 * 
	 * @param debeDarMotivoRegresionEgreso
	 */
	public void setDebeDarMotivoRegresionEgreso(String debeDarMotivoRegresionEgreso) {
		this.debeDarMotivoRegresionEgreso = debeDarMotivoRegresionEgreso;
	}

	/**
	 * 
	 * @return
	 */
	public String getMotivoReversionEgreso() {
		return motivoReversionEgreso;
	}

	/**
	 * 
	 * @param motivoReversionEgreso
	 */
	public void setMotivoReversionEgreso(String motivoReversionEgreso) {
		this.motivoReversionEgreso = motivoReversionEgreso;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isModificableAsocio() {
		return modificableAsocio;
	}

	/**
	 * 
	 * @param modificableAsocio
	 */
	public void setModificableAsocio(boolean modificableAsocio) {
		this.modificableAsocio = modificableAsocio;
	}


	/**
	 * @return valor de selectEspecialidad
	 */
	public String getSelectEspecialidad() {
		return selectEspecialidad;
	}


	/**
	 * @param selectEspecialidad el selectEspecialidad para asignar
	 */
	public void setSelectEspecialidad(String selectEspecialidad) {
		this.selectEspecialidad = selectEspecialidad;
	}
}
