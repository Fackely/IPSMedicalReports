package com.princetonsa.actionform.ordenesmedicas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.manejoPaciente.DtoIngresos;
import com.princetonsa.dto.ordenesmedicas.DtoIncapacidad;
import com.princetonsa.dto.ordenesmedicas.DtoRegistroIncapacidades;
import com.princetonsa.mundo.atencion.Diagnostico;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.ResultadoBoolean;

/**
 * @author Jairo G�mez Fecha Septiembre de 2009
 */

public class RegistroIncapacidadesForm extends ValidatorForm {
	// *************** Declaracion de variables ***************

	private String estado;
	
	private String estadoAnterior;
	
	private ResultadoBoolean mensaje;
	
	private ResultadoBoolean advertencia;
	
	private DtoRegistroIncapacidades registroIncapacidad;
	
	private DtoRegistroIncapacidades logRegistroIncapacidad;
	
	private HashMap tiposIncapacidad;
	
	private String valorConsecutivoIngreso;
	
	private String anioConsecutivoIngreso;
	
	private ArrayList<DtoIncapacidad> arrayListDtoIncapacidad;
	
	private int ingreso;
	
	private DtoIncapacidad selBusqueda;
	
	private HashMap centrosAtencion;
	
	private HashMap viasIngreso;
	
	private ArrayList convenios;
	
	private ArrayList<DtoIncapacidad> arraySelBusqueda;
	
	private int pacienteSeleccionadoRango;
	
	private int prioridadSeleccionada;
	
	private ArrayList<DtoIncapacidad> arrayConveniosIngreso;
	
	private String fechaFinalIncapacidadPraVez;
	
	private String acronimoDx;
	
	private int tipoCie;
	
	private String esDummy;
	
	private String especialidadDummy;
	
	private String cerrarPopUpDummy;
	
	private int ingresoEliminaAnula;
	
	private String solicitudDummy;
	
	private List<DtoIngresos> ingresosPaciente = new ArrayList<DtoIngresos>();
	
	private boolean mostrarIngresos;
	
	private String ingresoSeleccionado;
	
	private boolean seleccionarDiagnostico;
	
	private boolean seleccionarEspecialidad;
	
	private String especialidadProfesional;
	
	private String descripcionDx;
	
	private InfoDatosInt[] especialidadesProfesional= null;
	
	private ArrayList diagnosticos = null;
	
	private Diagnostico diagnostico = null;
	
	private String diagnosticoSeleccionado;
	
	private String diagnosticoPopUp;
	
	private String idIncapacidadConsulta;
	
		// ************ Fin Declaracion de variables **************

	/**
	 * Metodo que inicializa todas las variables.
	 */
	public void reset() {
		this.esDummy = ConstantesBD.acronimoNo;
		this.estado = "";
		this.estadoAnterior = "";
		this.mensaje = new ResultadoBoolean(false);
		this.advertencia = new ResultadoBoolean(false);
		this.registroIncapacidad = new DtoRegistroIncapacidades();
		this.tiposIncapacidad = new HashMap();
		this.valorConsecutivoIngreso = "";
		this.valorConsecutivoIngreso = "";
		this.logRegistroIncapacidad = new DtoRegistroIncapacidades();
		this.arrayListDtoIncapacidad = new ArrayList<DtoIncapacidad>();
		this.ingreso = ConstantesBD.codigoNuncaValido;
		this.selBusqueda = new DtoIncapacidad();
		this.centrosAtencion = new HashMap();
		this.viasIngreso = new HashMap();
		this.convenios = new ArrayList();
		this.arraySelBusqueda = new ArrayList<DtoIncapacidad>();
		this.pacienteSeleccionadoRango = ConstantesBD.codigoNuncaValido;
		this.prioridadSeleccionada = 1;
		this.arrayConveniosIngreso = new ArrayList<DtoIncapacidad>();
		this.fechaFinalIncapacidadPraVez = "";
		this.acronimoDx = "";
		this.tipoCie = ConstantesBD.codigoNuncaValido;
		this.especialidadDummy = "";
		this.cerrarPopUpDummy = ConstantesBD.acronimoNo;
		this.ingresoEliminaAnula = ConstantesBD.codigoNuncaValido;
		this.solicitudDummy = "";
		this.ingresosPaciente = new ArrayList<DtoIngresos>();
		this.mostrarIngresos = false;
		this.ingresoSeleccionado = "";
		this.seleccionarDiagnostico=false;
		this.seleccionarEspecialidad=false;
		this.especialidadProfesional = "";
		this.descripcionDx="";
		this.especialidadesProfesional= null;
		this.diagnosticos = new ArrayList();
		this.diagnosticoSeleccionado="";
		this.diagnosticoPopUp="";
		this.idIncapacidadConsulta="";
	}
	
	public void resetDummy() {
		this.estado = "";
		this.estadoAnterior = "";
		this.mensaje = new ResultadoBoolean(false);
		this.advertencia = new ResultadoBoolean(false);
		this.registroIncapacidad = new DtoRegistroIncapacidades();
		this.tiposIncapacidad = new HashMap();
		this.valorConsecutivoIngreso = "";
		this.valorConsecutivoIngreso = "";
		this.logRegistroIncapacidad = new DtoRegistroIncapacidades();
		this.arrayListDtoIncapacidad = new ArrayList<DtoIncapacidad>();
		this.ingreso = ConstantesBD.codigoNuncaValido;
		this.selBusqueda = new DtoIncapacidad();
		this.centrosAtencion = new HashMap();
		this.viasIngreso = new HashMap();
		this.convenios = new ArrayList();
		this.arraySelBusqueda = new ArrayList<DtoIncapacidad>();
		this.pacienteSeleccionadoRango = ConstantesBD.codigoNuncaValido;
		this.prioridadSeleccionada = 1;
		this.arrayConveniosIngreso = new ArrayList<DtoIncapacidad>();
		this.fechaFinalIncapacidadPraVez = "";
		this.acronimoDx = "";
		this.tipoCie = ConstantesBD.codigoNuncaValido;
		this.cerrarPopUpDummy = ConstantesBD.acronimoNo;
		this.ingresoEliminaAnula = ConstantesBD.codigoNuncaValido;
		this.ingresosPaciente = new ArrayList<DtoIngresos>();
		this.mostrarIngresos = false;
		this.ingresoSeleccionado = "";
		this.seleccionarDiagnostico=false;
		this.seleccionarEspecialidad=false;
		this.especialidadProfesional = "";
		this.descripcionDx="";
		this.especialidadesProfesional= null;
		this.diagnosticos = new ArrayList();
		this.diagnosticoSeleccionado="";
		this.diagnosticoPopUp="";
		this.idIncapacidadConsulta="";
	}

	/**
	 * Validate
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errores = new ActionErrors();
		
		return errores;
	}

	// *************** Declaracion de Metodos Get y Set ***************

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado
	 *            the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the mensaje
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	/**this.registroIncapacidad = new DtoRegistroIncapacidades();
	 * @param mensaje
	 *            the mensaje to set
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return the registroIncapacidad
	 */
	public DtoRegistroIncapacidades getRegistroIncapacidad() {
		return registroIncapacidad;
	}

	/**
	 * @param registroIncapacidad the registroIncapacidad to set
	 */
	public void setRegistroIncapacidad(DtoRegistroIncapacidades registroIncapacidad) {
		this.registroIncapacidad = registroIncapacidad;
	}

	/**
	 * @return the tiposIncapacidad
	 */
	public HashMap getTiposIncapacidad() {
		return tiposIncapacidad;
	}

	/**
	 * @param tiposIncapacidad the tiposIncapacidad to set
	 */
	public void setTiposIncapacidad(HashMap tiposIncapacidad) {
		this.tiposIncapacidad = tiposIncapacidad;
	}

	/**
	 * @return the valorConsecutivoIngreso
	 */
	public String getValorConsecutivoIngreso() {
		return valorConsecutivoIngreso;
	}

	/**
	 * @param valorConsecutivoIngreso the valorConsecutivoIngreso to set
	 */
	public void setValorConsecutivoIngreso(String valorConsecutivoIngreso) {
		this.valorConsecutivoIngreso = valorConsecutivoIngreso;
	}

	/**
	 * @return the anioConsecutivoIngreso
	 */
	public String getAnioConsecutivoIngreso() {
		return anioConsecutivoIngreso;
	}

	/**
	 * @param anioConsecutivoIngreso the anioConsecutivoIngreso to set
	 */
	public void setAnioConsecutivoIngreso(String anioConsecutivoIngreso) {
		this.anioConsecutivoIngreso = anioConsecutivoIngreso;
	}

	/**
	 * @return the advertencia
	 */
	public ResultadoBoolean getAdvertencia() {
		return advertencia;
	}

	/**
	 * @param advertencia the advertencia to set
	 */
	public void setAdvertencia(ResultadoBoolean advertencia) {
		this.advertencia = advertencia;
	}

	/**
	 * @return the logRegistroIncapacidad
	 */
	public DtoRegistroIncapacidades getLogRegistroIncapacidad() {
		return logRegistroIncapacidad;
	}

	/**
	 * @param logRegistroIncapacidad the logRegistroIncapacidad to set
	 */
	public void setLogRegistroIncapacidad(
			DtoRegistroIncapacidades logRegistroIncapacidad) {
		this.logRegistroIncapacidad = logRegistroIncapacidad;
	}

	/**
	 * @return the arrayListDtoIncapacidad
	 */
	public ArrayList<DtoIncapacidad> getArrayListDtoIncapacidad() {
		return arrayListDtoIncapacidad;
	}

	/**
	 * @param arrayListDtoIncapacidad the arrayListDtoIncapacidad to set
	 */
	public void setArrayListDtoIncapacidad(
			ArrayList<DtoIncapacidad> arrayListDtoIncapacidad) {
		this.arrayListDtoIncapacidad = arrayListDtoIncapacidad;
	}

	/**
	 * @return the ingreso
	 */
	public int getIngreso() {
		return ingreso;
	}

	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngreso(int ingreso) {
		this.ingreso = ingreso;
	}

	/**
	 * @return the selBusqueda
	 */
	public DtoIncapacidad getSelBusqueda() {
		return selBusqueda;
	}

	/**
	 * @param selBusqueda the selBusqueda to set
	 */
	public void setSelBusqueda(DtoIncapacidad selBusqueda) {
		this.selBusqueda = selBusqueda;
	}

	/**
	 * @return the centrosAtencion
	 */
	public HashMap getCentrosAtencion() {
		return centrosAtencion;
	}

	/**
	 * @param centrosAtencion the centrosAtencion to set
	 */
	public void setCentrosAtencion(HashMap centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}

	/**
	 * @return the viasIngreso
	 */
	public HashMap getViasIngreso() {
		return viasIngreso;
	}

	/**
	 * @param viasIngreso the viasIngreso to set
	 */
	public void setViasIngreso(HashMap viasIngreso) {
		this.viasIngreso = viasIngreso;
	}

	/**
	 * @return the convenios
	 */
	public ArrayList getConvenios() {
		return convenios;
	}

	/**
	 * @param convenios the convenios to set
	 */
	public void setConvenios(ArrayList convenios) {
		this.convenios = convenios;
	}

	/**
	 * @return the arraySelBusqueda
	 */
	public ArrayList<DtoIncapacidad> getArraySelBusqueda() {
		return arraySelBusqueda;
	}

	/**
	 * @param arraySelBusqueda the arraySelBusqueda to set
	 */
	public void setArraySelBusqueda(ArrayList<DtoIncapacidad> arraySelBusqueda) {
		this.arraySelBusqueda = arraySelBusqueda;
	}

	/**
	 * @return the pacienteSeleccionadoRango
	 */
	public int getPacienteSeleccionadoRango() {
		return pacienteSeleccionadoRango;
	}

	/**
	 * @param pacienteSeleccionadoRango the pacienteSeleccionadoRango to set
	 */
	public void setPacienteSeleccionadoRango(int pacienteSeleccionadoRango) {
		this.pacienteSeleccionadoRango = pacienteSeleccionadoRango;
	}

	/**
	 * @return the prioridadSeleccionada
	 */
	public int getPrioridadSeleccionada() {
		return prioridadSeleccionada;
	}

	/**
	 * @param prioridadSeleccionada the prioridadSeleccionada to set
	 */
	public void setPrioridadSeleccionada(int prioridadSeleccionada) {
		this.prioridadSeleccionada = prioridadSeleccionada;
	}

	/**
	 * @return the arrayConveniosIngreso
	 */
	public ArrayList<DtoIncapacidad> getArrayConveniosIngreso() {
		return arrayConveniosIngreso;
	}

	/**
	 * @param arrayConveniosIngreso the arrayConveniosIngreso to set
	 */
	public void setArrayConveniosIngreso(
			ArrayList<DtoIncapacidad> arrayConveniosIngreso) {
		this.arrayConveniosIngreso = arrayConveniosIngreso;
	}

	/**
	 * @return the estadoAnterior
	 */
	public String getEstadoAnterior() {
		return estadoAnterior;
	}

	/**
	 * @param estadoAnterior the estadoAnterior to set
	 */
	public void setEstadoAnterior(String estadoAnterior) {
		this.estadoAnterior = estadoAnterior;
	}

	/**
	 * @return the fechaFinalIncapacidadPraVez
	 */
	public String getFechaFinalIncapacidadPraVez() {
		return fechaFinalIncapacidadPraVez;
	}

	/**
	 * @param fechaFinalIncapacidadPraVez the fechaFinalIncapacidadPraVez to set
	 */
	public void setFechaFinalIncapacidadPraVez(String fechaFinalIncapacidadPraVez) {
		this.fechaFinalIncapacidadPraVez = fechaFinalIncapacidadPraVez;
	}

	/**
	 * @return the acronimoDx
	 */
	public String getAcronimoDx() {
		return acronimoDx;
	}

	/**
	 * @param acronimoDx the acronimoDx to set
	 */
	public void setAcronimoDx(String acronimoDx) {
		this.acronimoDx = acronimoDx;
	}

	/**
	 * @return the tipoCie
	 */
	public int getTipoCie() {
		return tipoCie;
	}

	/**
	 * @param tipoCie the tipoCie to set
	 */
	public void setTipoCie(int tipoCie) {
		this.tipoCie = tipoCie;
	}

	/**
	 * @return the esDummy
	 */
	public String getEsDummy() {
		return esDummy;
	}

	/**
	 * @param esDummy the esDummy to set
	 */
	public void setEsDummy(String esDummy) {
		this.esDummy = esDummy;
	}

	/**
	 * @return the especialidadDummy
	 */
	public String getEspecialidadDummy() {
		return especialidadDummy;
	}

	/**
	 * @param especialidadDummy the especialidadDummy to set
	 */
	public void setEspecialidadDummy(String especialidadDummy) {
		this.especialidadDummy = especialidadDummy;
	}

	/**
	 * @return the cerrarPopUpDummy
	 */
	public String getCerrarPopUpDummy() {
		return cerrarPopUpDummy;
	}

	/**
	 * @param cerrarPopUpDummy the cerrarPopUpDummy to set
	 */
	public void setCerrarPopUpDummy(String cerrarPopUpDummy) {
		this.cerrarPopUpDummy = cerrarPopUpDummy;
	}

	/**
	 * @return the ingresoEliminaAnula
	 */
	public int getIngresoEliminaAnula() {
		return ingresoEliminaAnula;
	}

	/**
	 * @param ingresoEliminaAnula the ingresoEliminaAnula to set
	 */
	public void setIngresoEliminaAnula(int ingresoEliminaAnula) {
		this.ingresoEliminaAnula = ingresoEliminaAnula;
	}

	/**
	 * @return the solicitudDummy
	 */
	public String getSolicitudDummy() {
		return solicitudDummy;
	}

	/**
	 * @param solicitudDummy the solicitudDummy to set
	 */
	public void setSolicitudDummy(String solicitudDummy) {
		this.solicitudDummy = solicitudDummy;
	}

	public List<DtoIngresos> getIngresosPaciente() {
		return ingresosPaciente;
	}

	public void setIngresosPaciente(List<DtoIngresos> ingresosPaciente) {
		this.ingresosPaciente = ingresosPaciente;
	}

	public boolean isMostrarIngresos() {
		return mostrarIngresos;
	}

	public void setMostrarIngresos(boolean mostrarIngresos) {
		this.mostrarIngresos = mostrarIngresos;
	}

	public String getIngresoSeleccionado() {
		return ingresoSeleccionado;
	}

	public void setIngresoSeleccionado(String ingresoSeleccionado) {
		this.ingresoSeleccionado = ingresoSeleccionado;
	}

	public boolean isSeleccionarDiagnostico() {
		return seleccionarDiagnostico;
	}

	public void setSeleccionarDiagnostico(boolean seleccionarDiagnostico) {
		this.seleccionarDiagnostico = seleccionarDiagnostico;
	}

	public boolean isSeleccionarEspecialidad() {
		return seleccionarEspecialidad;
	}

	public void setSeleccionarEspecialidad(boolean seleccionarEspecialidad) {
		this.seleccionarEspecialidad = seleccionarEspecialidad;
	}

	public String getEspecialidadProfesional() {
		return especialidadProfesional;
	}

	public void setEspecialidadProfesional(String especialidadProfesional) {
		this.especialidadProfesional = especialidadProfesional;
	}

	public String getDescripcionDx() {
		return descripcionDx;
	}

	public void setDescripcionDx(String descripcionDx) {
		this.descripcionDx = descripcionDx;
	}

	
	public void setEspecialidadesProfesional(
			InfoDatosInt[] especialidadesProfesional) {
		this.especialidadesProfesional = especialidadesProfesional;
	}

	public InfoDatosInt[] getEspecialidadesProfesional() {
		return especialidadesProfesional;
	}

	public ArrayList getDiagnosticos() {
		return diagnosticos;
	}

	public void setDiagnosticos(ArrayList diagnosticos) {
		this.diagnosticos = diagnosticos;
	}

	public Diagnostico getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(Diagnostico diagnostico) {
		this.diagnostico = diagnostico;
	}

	public String getDiagnosticoSeleccionado() {
		return diagnosticoSeleccionado;
	}

	public void setDiagnosticoSeleccionado(String diagnosticoSeleccionado) {
		this.diagnosticoSeleccionado = diagnosticoSeleccionado;
	}

	public String getDiagnosticoPopUp() {
		return diagnosticoPopUp;
	}

	public void setDiagnosticoPopUp(String diagnosticoPopUp) {
		this.diagnosticoPopUp = diagnosticoPopUp;
	}

	public String getIdIncapacidadConsulta() {
		return idIncapacidadConsulta;
	}

	public void setIdIncapacidadConsulta(String idIncapacidadConsulta) {
		this.idIncapacidadConsulta = idIncapacidadConsulta;
	}

	// ************* Fin Declaracion de Metodos Get y Set *************
}