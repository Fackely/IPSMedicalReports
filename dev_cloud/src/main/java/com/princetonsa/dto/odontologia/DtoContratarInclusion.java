
package com.princetonsa.dto.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import org.apache.struts.action.ActionErrors;

import util.ConstantesBD;

import com.princetonsa.mundo.PersonaBasica;
import com.servinte.axioma.mundo.impl.odontologia.presupuesto.PresupuestoExclusionesInclusionesMundo;

/**
 * Clase que contiene la información necesaria para realizar
 * el proceso de contratación de una inclusión
 * 
 * @author Jorge Armando Agudelo Quintero
 */
public class DtoContratarInclusion {

	/**
	 * Información asociada al encabezado de la inclusión,
	 * los totales y la solicitud de Descuento
	 */
	private DtoEncabezadoInclusion encabezadoInclusion;
	
	/**
	 * Información del Descuento Odontologico que se va a asociar al
	 * proceso de contratación de la inclusión.
	 * Recibe null cuando no hay descuento
	 * 
	 */
	private DtoPresupuestoOdontologicoDescuento dtoDcto;
	
//	/**
//	 * Solicitud de Descuento, asociada al proceso de contratación
//	 * en el caso de existir. 
//	 * Recibe null cuando no hay descuento
//	 */
//	private InfoDefinirSolucitudDsctOdon solicitudDescuento;
	
	/**
	 * Errores que se pueden generar durante
	 * el proceso de validación
	 */
	private ActionErrors errores;
	
	/**
	 * Paciente asociado al proceso de contratación
	 * de las inclusiones
	 */
	private PersonaBasica paciente;
	
	/**
	 * Registros de las inclusiones a contratar
	 */
	private ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion;
	
	/**
	 * Listado con las Sumatorias del Presupuesto.
	 * En este atributo se almacena el total de los convenios para las inclusiones que se
	 * visualizan en el detalle del registro de inclusión
	 *
	 * Convenios con los totales a contratar
	 */
	private ArrayList<DtoPresupuestoTotalConvenio> listaSumatoriaConvenios;
	
	/**
	 * Conexión con la base de datos
	 */
	private Connection connection;
	
	/**
	 * Código del presupuesto del paciente
	 */
	private BigDecimal codigoPresupuesto;
	
	/**
	 * Código del plan de tratamiento del paciente
	 */
	private BigDecimal codigoPlanTratamiento;
	

	/**
	 * Login del usuario en sesión
	 */
	private String loginUsuario;
	
	/**
	 * Estado del proceso de contratación de la inclusión
	 */
	private String estado;
	
	/**
	 * Indica si se debe o no tener en cuenta la validación del Descuento
	 * Odontológico
	 */
	private boolean validarDescuento;
	
//	/**
//	 * Código del Encabezado asociado al proceso de contratación
//	 * de la Inclusión
//	 */
//	private long codigoIncluPresuEncabezado;
	
	/**
	 * Código de la institución
	 */
	private int codigoInstitucion;
	
	/**
	 * Código del centro de Atención
	 */
	private int codigoCentroAtencion;
	
	/**
	 * Validación que se realiza durante el proceso de contratación
	 * Valores posibles: constantes Definidas en {@link PresupuestoExclusionesInclusionesMundo}
	 */
	private String validacion;
	
	/**
	 * Decision que toma el usuario frente a la validación que se le presenta
	 */
	private String decision;
	 
	
	/**
	 * Lista con los contratos que se deben modificar ya que se debe realizar una reserva de anticipo
	 */
	private ArrayList<DtoValorAnticipoPresupuesto> listaAnticiposPresupuesto;
	
	
	/**
	 * Registros de las inclusiones a contratar con los nuevos calculos
	 * que determinan si aplican nuevos bonos y promociones
	 */
	private ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusionClon;
	
	
	/**
	 * Constructor de la Clase
	 */
	public DtoContratarInclusion() {
		
		this.encabezadoInclusion = new DtoEncabezadoInclusion();
		this.dtoDcto = new DtoPresupuestoOdontologicoDescuento();
		this.errores = new ActionErrors();
		this.paciente = new PersonaBasica();
		this.registrosContratarInclusion = new ArrayList<DtoRegistroContratarInclusion>();
		this.listaSumatoriaConvenios = new ArrayList<DtoPresupuestoTotalConvenio>();
		this.connection = null;
		this.codigoPresupuesto = new BigDecimal(ConstantesBD.codigoNuncaValido);
		this.codigoPlanTratamiento = new BigDecimal(ConstantesBD.codigoNuncaValido);
		this.loginUsuario = "";
		this.estado = "";
		this.validarDescuento = false;
		//this.codigoIncluPresuEncabezado = ConstantesBD.codigoNuncaValidoLong;
		this.codigoInstitucion = ConstantesBD.codigoNuncaValido;
		this.codigoCentroAtencion = ConstantesBD.codigoNuncaValido;
		//this.solicitudDescuento = new InfoDefinirSolucitudDsctOdon();
		this.validacion = "";
		this.decision = "";
		this.listaAnticiposPresupuesto = new ArrayList<DtoValorAnticipoPresupuesto>();
		this.registrosContratarInclusionClon = new ArrayList<DtoRegistroContratarInclusion>();
		
	}


//	/**
//	 * Constructor de la Clase
//	 */
//	public DtoContratarInclusion (DtoPresupuestoOdontologicoDescuento dtoDcto, ActionErrors errores, PersonaBasica  paciente,
//			ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion, ArrayList<DtoPresupuestoTotalConvenio> listaSumatoriaConvenios,
//			Connection connection, BigDecimal codigoPresupuesto, BigDecimal codigoPlanTratamiento, String loginUsuario, String  estado, 
//			boolean validarDescuento, int codigoInstitucion, int codigoCentroAtencion, String validacion, String decision) {
//		
//		this.dtoDcto = dtoDcto;
//		this.errores = errores;
//		this.paciente = paciente;
//		this.registrosContratarInclusion = registrosContratarInclusion;
//		this.listaSumatoriaConvenios = listaSumatoriaConvenios;
//		this.connection = connection;
//		this.codigoPresupuesto = codigoPresupuesto;
//		this.codigoPlanTratamiento = codigoPlanTratamiento;
//		this.loginUsuario = loginUsuario;
//		this.estado = estado;
//		this.validarDescuento = validarDescuento;
//		this.codigoInstitucion = codigoInstitucion;
//		this.codigoCentroAtencion = codigoCentroAtencion;
//		this.validacion = validacion;
//		this.decision = decision;
//	}
	
	

	/**
	 * Constructor de la Clase
	 */
	public DtoContratarInclusion (DtoEncabezadoInclusion encabezadoInclusion,ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion, 
			ArrayList<DtoPresupuestoTotalConvenio> listaSumatoriaConvenios, BigDecimal codigoPresupuesto, BigDecimal codigoPlanTratamiento, String loginUsuario, 
			int codigoInstitucion, int codigoCentroAtencion) {

		this.setEncabezadoInclusion(encabezadoInclusion);
		this.setRegistrosContratarInclusion(registrosContratarInclusion);
		this.setListaSumatoriaConvenios(listaSumatoriaConvenios);
		this.setCodigoPresupuesto(codigoPresupuesto);
		this.setCodigoPlanTratamiento(codigoPlanTratamiento);
		this.setLoginUsuario(loginUsuario);
		this.setCodigoInstitucion(codigoInstitucion);
		this.setCodigoCentroAtencion(codigoCentroAtencion);
		
	}
	
	
	
	/**
	 * @return the dtoDcto
	 */
	public DtoPresupuestoOdontologicoDescuento getDtoDcto() {
		return dtoDcto;
	}


	/**
	 * @param dtoDcto the dtoDcto to set
	 */
	public void setDtoDcto(DtoPresupuestoOdontologicoDescuento dtoDcto) {
		this.dtoDcto = dtoDcto;
	}


	/**
	 * @return the errores
	 */
	public ActionErrors getErrores() {
		return errores;
	}


	/**
	 * @param errores the errores to set
	 */
	public void setErrores(ActionErrors errores) {
		this.errores = errores;
	}


	/**
	 * @return the paciente
	 */
	public PersonaBasica getPaciente() {
		return paciente;
	}


	/**
	 * @param paciente the paciente to set
	 */
	public void setPaciente(PersonaBasica paciente) {
		this.paciente = paciente;
	}


	/**
	 * @return the registrosContratarInclusion
	 */
	public ArrayList<DtoRegistroContratarInclusion> getRegistrosContratarInclusion() {
		return registrosContratarInclusion;
	}


	/**
	 * @param registrosContratarInclusion the registrosContratarInclusion to set
	 */
	public void setRegistrosContratarInclusion(
			ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusion) {
		this.registrosContratarInclusion = registrosContratarInclusion;
	}


	/**
	 * @return the listaSumatoriaConvenios
	 */
	public ArrayList<DtoPresupuestoTotalConvenio> getListaSumatoriaConvenios() {
		return listaSumatoriaConvenios;
	}


	/**
	 * @param listaSumatoriaConvenios the listaSumatoriaConvenios to set
	 */
	public void setListaSumatoriaConvenios(
			ArrayList<DtoPresupuestoTotalConvenio> listaSumatoriaConvenios) {
		this.listaSumatoriaConvenios = listaSumatoriaConvenios;
	}

	/**
	 * @return the codigoPresupuesto
	 */
	public BigDecimal getCodigoPresupuesto() {
		return codigoPresupuesto;
	}


	/**
	 * @param codigoPresupuesto the codigoPresupuesto to set
	 */
	public void setCodigoPresupuesto(BigDecimal codigoPresupuesto) {
		this.codigoPresupuesto = codigoPresupuesto;
	}


	/**
	 * @return the codigoPlanTratamiento
	 */
	public BigDecimal getCodigoPlanTratamiento() {
		return codigoPlanTratamiento;
	}


	/**
	 * @param codigoPlanTratamiento the codigoPlanTratamiento to set
	 */
	public void setCodigoPlanTratamiento(BigDecimal codigoPlanTratamiento) {
		this.codigoPlanTratamiento = codigoPlanTratamiento;
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
	 * @return the validarDescuento
	 */
	public boolean isValidarDescuento() {
		return validarDescuento;
	}


	/**
	 * @param validarDescuento the validarDescuento to set
	 */
	public void setValidarDescuento(boolean validarDescuento) {
		this.validarDescuento = validarDescuento;
	}


//	/**
//	 * @return the codigoIncluPresuEncabezado
//	 */
//	public long getCodigoIncluPresuEncabezado() {
//		return codigoIncluPresuEncabezado;
//	}
//
//
//	/**
//	 * @param codigoIncluPresuEncabezado the codigoIncluPresuEncabezado to set
//	 */
//	public void setCodigoIncluPresuEncabezado(long codigoIncluPresuEncabezado) {
//		this.codigoIncluPresuEncabezado = codigoIncluPresuEncabezado;
//	}


	/**
	 * @return the codigoInstitucion
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}


	/**
	 * @param codigoInstitucion the codigoInstitucion to set
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}


	/**
	 * @return the codigoCentroAtencion
	 */
	public int getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}


	/**
	 * @param codigoCentroAtencion the codigoCentroAtencion to set
	 */
	public void setCodigoCentroAtencion(int codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}


	/**
	 * @param connection the connection to set
	 */
	public void setConnection(Connection connection) {
		this.connection = connection;
	}


	/**
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}


//	/**
//	 * @param solicitudDescuento the solicitudDescuento to set
//	 */
//	public void setSolicitudDescuento(InfoDefinirSolucitudDsctOdon solicitudDescuento) {
//		this.solicitudDescuento = solicitudDescuento;
//	}
//
//
//	/**
//	 * @return the solicitudDescuento
//	 */
//	public InfoDefinirSolucitudDsctOdon getSolicitudDescuento() {
//		return solicitudDescuento;
//	}


	/**
	 * @param validacion the validacion to set
	 */
	public void setValidacion(String validacion) {
		this.validacion = validacion;
	}


	/**
	 * @return the validacion
	 */
	public String getValidacion() {
		return validacion;
	}


	/**
	 * @param decision the decision to set
	 */
	public void setDecision(String decision) {
		this.decision = decision;
	}


	/**
	 * @return the decision
	 */
	public String getDecision() {
		return decision;
	}


	/**
	 * @param listaAnticiposPresupuesto the listaAnticiposPresupuesto to set
	 */
	public void setListaAnticiposPresupuesto(
			ArrayList<DtoValorAnticipoPresupuesto> listaAnticiposPresupuesto) {
		this.listaAnticiposPresupuesto = listaAnticiposPresupuesto;
	}


	/**
	 * @return the listaAnticiposPresupuesto
	 */
	public ArrayList<DtoValorAnticipoPresupuesto> getListaAnticiposPresupuesto() {
		return listaAnticiposPresupuesto;
	}


	/**
	 * @param registrosContratarInclusionClon the registrosContratarInclusionClon to set
	 */
	public void setRegistrosContratarInclusionClon(
			ArrayList<DtoRegistroContratarInclusion> registrosContratarInclusionClon) {
		this.registrosContratarInclusionClon = registrosContratarInclusionClon;
	}


	/**
	 * @return the registrosContratarInclusionClon
	 */
	public ArrayList<DtoRegistroContratarInclusion> getRegistrosContratarInclusionClon() {
		return registrosContratarInclusionClon;
	}


	/**
	 * @param encabezadoInclusion the encabezadoInclusion to set
	 */
	public void setEncabezadoInclusion(DtoEncabezadoInclusion encabezadoInclusion) {
		this.encabezadoInclusion = encabezadoInclusion;
	}


	/**
	 * @return the encabezadoInclusion
	 */
	public DtoEncabezadoInclusion getEncabezadoInclusion() {
		return encabezadoInclusion;
	}
	
}
