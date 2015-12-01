/**
 * 
 */
package com.princetonsa.dto.capitacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.apache.struts.action.ActionErrors;

import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.Convenios;


/**
 * @author Cristhian Murillo, Fabian Becerra, Camilo Gomez
 */
public class DtoProcesoPresupuestoCapitado implements Serializable
{

	/** * */
	private static final long serialVersionUID = 1L;
	
	/** *Fecha inicio busqueda */
	private Date fechaInicio;
	
	/** * Fecha fin busqueda */
	private Date fechaFin;
	
	/** * Convenio busqueda */
	private Integer convenio;
	
	/** * contrato busqueda  */
	private Integer contrato;
	
	/** * Obervaciones busqueda */
	private String observaciones;
	
	/** * tipo proceso que se ejecuta*/
	private String tipoProceso;
	
	/** * Institución del usuario en sesión*/
	private Integer institucion;
	
	/** * Lista de contratos */
	private ArrayList<Convenios> listaConvenios = new ArrayList<Convenios>();
	
	/** * Lista de convenios */
	private ArrayList<Contratos> listaContratos = new ArrayList<Contratos>();
	
	/**  * Indica si la consulta de cada proceso debe traer registros con estado anulado */
	private boolean excluiranuladas;
	
	/** * Contiene los errores a mostrar */
	private ActionErrors errores;
	
	/** * Login de Usuario */
	private String loginUsuario;
	
	/** Parametros para la consulta del log */
	private Integer caseParaBusquedaLog;
	
	/** Log seleccionado */
	private DtoLogCierrePresuCapita dtoLogCierrePresuCapita;
	
	/** Estado del log */
	private String estado;
	
	/** *Fecha seleccionada para el cierre */
	private Date fechaCierre;
	
	/** *indica si el proceso registró o no información */
	private boolean noInformacion;
	
	/** Parametro de busqueda para estados de Autorizacion*/
	private String estadoAutorizacion;
	
	/** Parametro de busqueda para autorizaciones Anuladas de Articulos*/
	private Integer codArticulo;
	
	/** Parametro de busqueda para autorizaciones Anuladas de Servicios*/
	private Integer codServicio;
	
	/**
	 * Constructor de la clase
	 */
	public DtoProcesoPresupuestoCapitado() {
		this.reset();
	}
	
	/** * */
	private void reset() 
	{ 
		this.fechaFin			= null;
		this.fechaInicio		= null;
		this.convenio			= null;
		this.institucion 		= null;
		this.listaConvenios		= new ArrayList<Convenios>();
		this.listaContratos		= new ArrayList<Contratos>();
		this.excluiranuladas	= false;
		this.errores			= new ActionErrors();
		this.loginUsuario		= null;
		this.caseParaBusquedaLog= null;
		this.dtoLogCierrePresuCapita = new DtoLogCierrePresuCapita();
		this.estado				= null;
		this.fechaCierre		= null;
		this.noInformacion		= false;
		this.estadoAutorizacion	= null;
		this.codArticulo		= null;
		this.codServicio		= null;
	}


	
	/**
	 * Este Método se encarga de obtener el valor del atributo loginUsuario
	 * @return retorna la variable loginUsuario 
	 * @author Cristhian Murillo
	 */
	public String getLoginUsuario() {
		return loginUsuario;
	}

	/**
	 * Este Método se encarga de establecer el valor del atributo loginUsuario
	 * @param valor para el atributo fechaInicio 
	 * @author Cristhian Murillo
	 */
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	/**
	 * Este Método se encarga de obtener el valor del atributo fechaInicio
	 * @return retorna la variable fechaInicio 
	 * @author Cristhian Murillo
	 */
	public Date getFechaInicio() {
		return fechaInicio;
	}

	/**
	 * Este Método se encarga de establecer el valor del atributo fechaInicio
	 * @param valor para el atributo fechaInicio 
	 * @author Cristhian Murillo
	 */
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	/**
	 * Este Método se encarga de obtener el valor del atributo fechaFin
	 * @return retorna la variable fechaFin 
	 * @author Cristhian Murillo
	 */
	public Date getFechaFin() {
		return fechaFin;
	}

	/**
	 * Este Método se encarga de establecer el valor del atributo fechaFin
	 * @param valor para el atributo fechaFin 
	 * @author Cristhian Murillo
	 */
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	/**
	 * Este Método se encarga de obtener el valor del atributo convenio
	 * @return retorna la variable convenio 
	 * @author Cristhian Murillo
	 */
	public Integer getConvenio() {
		return convenio;
	}

	/**
	 * Este Método se encarga de establecer el valor del atributo convenio
	 * @param valor para el atributo convenio 
	 * @author Cristhian Murillo
	 */
	public void setConvenio(Integer convenio) {
		this.convenio = convenio;
	}

	/**
	 * Este Método se encarga de obtener el valor del atributo contrato
	 * @return retorna la variable contrato 
	 * @author Cristhian Murillo
	 */
	public Integer getContrato() {
		return contrato;
	}

	/**
	 * Este Método se encarga de establecer el valor del atributo contrato
	 * @param valor para el atributo contrato 
	 * @author Cristhian Murillo
	 */
	public void setContrato(Integer contrato) {
		this.contrato = contrato;
	}

	/**
	 * Este Método se encarga de obtener el valor del atributo observaciones
	 * @return retorna la variable observaciones 
	 * @author Cristhian Murillo
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * Este Método se encarga de establecer el valor del atributo observaciones
	 * @param valor para el atributo observaciones 
	 * @author Cristhian Murillo
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * Este Método se encarga de obtener el valor del atributo tipoProceso
	 * @return retorna la variable tipoProceso 
	 * @author Cristhian Murillo
	 */
	public String getTipoProceso() {
		return tipoProceso;
	}

	/**
	 * Este Método se encarga de establecer el valor del atributo tipoProceso
	 * @param valor para el atributo tipoProceso 
	 * @author Cristhian Murillo
	 */
	public void setTipoProceso(String tipoProceso) {
		this.tipoProceso = tipoProceso;
	}

	/**
	 * Este Método se encarga de obtener el valor del atributo institucion
	 * @return retorna la variable institucion 
	 * @author Cristhian Murillo
	 */
	public Integer getInstitucion() {
		return institucion;
	}

	/**
	 * Este Método se encarga de establecer el valor del atributo institucion
	 * @param valor para el atributo institucion 
	 * @author Cristhian Murillo
	 */
	public void setInstitucion(Integer institucion) {
		this.institucion = institucion;
	}

	/**
	 * Este Método se encarga de obtener el valor del atributo listaConvenios
	 * @return retorna la variable listaConvenios 
	 * @author Cristhian Murillo
	 */
	public ArrayList<Convenios> getListaConvenios() {
		return listaConvenios;
	}

	/**
	 * Este Método se encarga de establecer el valor del atributo listaConvenios
	 * @param valor para el atributo listaConvenios 
	 * @author Cristhian Murillo
	 */
	public void setListaConvenios(ArrayList<Convenios> listaConvenios) {
		this.listaConvenios = listaConvenios;
	}

	/**
	 * Este Método se encarga de obtener el valor del atributo listaContratos
	 * @return retorna la variable listaContratos 
	 * @author Cristhian Murillo
	 */
	public ArrayList<Contratos> getListaContratos() {
		return listaContratos;
	}

	/**
	 * Este Método se encarga de establecer el valor del atributo listaContratos
	 * @param valor para el atributo listaContratos 
	 * @author Cristhian Murillo
	 */
	public void setListaContratos(ArrayList<Contratos> listaContratos) {
		this.listaContratos = listaContratos;
	}

	/**
	 * Este Método se encarga de obtener el valor del atributo excluiranuladas
	 * @return retorna la variable excluiranuladas 
	 * @author Cristhian Murillo
	 */
	public boolean isExcluiranuladas() {
		return excluiranuladas;
	}

	/**
	 * Este Método se encarga de establecer el valor del atributo excluiranuladas
	 * @param valor para el atributo excluiranuladas 
	 * @author Cristhian Murillo
	 */
	public void setExcluiranuladas(boolean excluiranuladas) {
		this.excluiranuladas = excluiranuladas;
	}

	/**
	 * @return valor de errores
	 */
	public ActionErrors getErrores() {
		return errores;
	}

	/**
	 * @param errores el errores para asignar
	 */
	public void setErrores(ActionErrors errores) {
		this.errores = errores;
	}

	/**
	 * @return valor de caseParaBusquedaLog
	 */
	public Integer getCaseParaBusquedaLog() {
		return caseParaBusquedaLog;
	}

	/**
	 * @param caseParaBusquedaLog el caseParaBusquedaLog para asignar
	 */
	public void setCaseParaBusquedaLog(Integer caseParaBusquedaLog) {
		this.caseParaBusquedaLog = caseParaBusquedaLog;
	}

	/**
	 * @return valor de dtoLogCierrePresuCapita
	 */
	public DtoLogCierrePresuCapita getDtoLogCierrePresuCapita() {
		return dtoLogCierrePresuCapita;
	}

	/**
	 * @param dtoLogCierrePresuCapita el dtoLogCierrePresuCapita para asignar
	 */
	public void setDtoLogCierrePresuCapita(
			DtoLogCierrePresuCapita dtoLogCierrePresuCapita) {
		this.dtoLogCierrePresuCapita = dtoLogCierrePresuCapita;
	}

	/**
	 * @return valor de estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado el estado para asignar
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return valor de fechaCierre
	 */
	public Date getFechaCierre() {
		return fechaCierre;
	}

	/**
	 * @param fechaCierre el fechaCierre para asignar
	 */
	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public void setNoInformacion(boolean noInformacion) {
		this.noInformacion = noInformacion;
	}

	public boolean isNoInformacion() {
		return noInformacion;
	}

	public void setEstadoAutorizacion(String estadoAutorizacion) {
		this.estadoAutorizacion = estadoAutorizacion;
	}

	public String getEstadoAutorizacion() {
		return estadoAutorizacion;
	}

	public void setCodArticulo(Integer codArticulo) {
		this.codArticulo = codArticulo;
	}

	public Integer getCodArticulo() {
		return codArticulo;
	}

	public void setCodServicio(Integer codServicio) {
		this.codServicio = codServicio;
	}

	public Integer getCodServicio() {
		return codServicio;
	}


	
}
