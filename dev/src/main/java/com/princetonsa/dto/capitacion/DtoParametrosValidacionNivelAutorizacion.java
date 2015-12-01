/**
 * 
 */
package com.princetonsa.dto.capitacion;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * @author Fabian Becerra
 */
public class DtoParametrosValidacionNivelAutorizacion implements Serializable
{
	
	/** *  */
	private static final long serialVersionUID = 1L;

	/** * loginUsuario  */
	private String loginUsuario;
	
	/** * viaIngreso  */
	private int viaIngreso;
	
	/** * codigoServicio  */
	private int codigoServicio;
	
	/** * codigoArticulo  */
	private int codigoArticulo;
	
	/** * institucion  */
	private int institucion;
	
	/** * centroCostoEjecuta  */
	private int centroCostoEjecuta;
	
	/** * tipoPaciente */
	private String tipoPaciente;
	
	/** * naturalezaPAciente */
	private int naturalezaPAciente;
	
	//AGREGADOS PARA AUTORIZACION MANUAL
	/** * Atributo que almacena lo seleccionado en el campo tipo entidad autoriza  */
	private String tipoEntidadAutoriza;
	
	/** * Atributo que almacena el codigo de la entidad subcontratada seleccionada  */
	private int codigoEntidadSubcontratadaSeleccionada;
	
	/** * Atributo que almacena el acronimo de la funcionalidad desde donde es llamado el proceso */
	private String funcionalidad;
	
	/** 
	 * Diferencia una orden médica de una Ambulatoria, PyP o Petición  
	 * False = Tipo orden médica
	 * True =  tipo Ambulatoria, PyP o Petición
	 * */
	private boolean tipoAmbulatoriaPeticionesOPyP;
	
	/** * Define si debe ejecutar el punto 4 (Buscar y Validar Prioridad Entidad 
	 * Subcontratada al ingresar a la Funcionalidad) de la validación DCU 1105  */
	private boolean buscarValidarPrioridadEntidadSubcontratadaIngresarFuncionalidad;
	
	/** * tipoOrden */
	private String tipoOrden;
	
	/** * Id del ingreso asociado */
	private Integer idIngreso;
	
	/** * Centro Atención asociado */
	private Integer centroAtencion;
	
	/** * Centro Atención asociado */
	private Integer centroAtencionAsignado;
	
	
	/**
	 * Constructor de la clase
	 */
	public DtoParametrosValidacionNivelAutorizacion()
	{
		this.loginUsuario="";
		this.viaIngreso=ConstantesBD.codigoNuncaValido;
		this.codigoArticulo=ConstantesBD.codigoNuncaValido;
		this.codigoServicio=ConstantesBD.codigoNuncaValido;
		this.centroCostoEjecuta=ConstantesBD.codigoNuncaValido;
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.tipoPaciente="";
		this.naturalezaPAciente=ConstantesBD.codigoNuncaValido;
		this.tipoEntidadAutoriza="";
		this.codigoEntidadSubcontratadaSeleccionada=ConstantesBD.codigoNuncaValido;
		this.funcionalidad="";
		this.tipoAmbulatoriaPeticionesOPyP	= false;
		this.buscarValidarPrioridadEntidadSubcontratadaIngresarFuncionalidad = false;
		this.tipoOrden = null;
		this.idIngreso = null;
		this.centroAtencion = null;
		this.centroAtencionAsignado = null;
	}

	/**
	 * @return valor de loginUsuario
	 */
	public String getLoginUsuario() {
		return loginUsuario;
	}

	/**
	 * @param loginUsuario el loginUsuario para asignar
	 */
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	/**
	 * @return valor de viaIngreso
	 */
	public int getViaIngreso() {
		return viaIngreso;
	}

	/**
	 * @param viaIngreso el viaIngreso para asignar
	 */
	public void setViaIngreso(int viaIngreso) {
		this.viaIngreso = viaIngreso;
	}

	/**
	 * @return valor de codigoServicio
	 */
	public int getCodigoServicio() {
		return codigoServicio;
	}

	/**
	 * @param codigoServicio el codigoServicio para asignar
	 */
	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	/**
	 * @return valor de codigoArticulo
	 */
	public int getCodigoArticulo() {
		return codigoArticulo;
	}

	/**
	 * @param codigoArticulo el codigoArticulo para asignar
	 */
	public void setCodigoArticulo(int codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	/**
	 * @return valor de institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion el institucion para asignar
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return valor de centroCostoEjecuta
	 */
	public int getCentroCostoEjecuta() {
		return centroCostoEjecuta;
	}

	/**
	 * @param centroCostoEjecuta el centroCostoEjecuta para asignar
	 */
	public void setCentroCostoEjecuta(int centroCostoEjecuta) {
		this.centroCostoEjecuta = centroCostoEjecuta;
	}

	/**
	 * @return valor de tipoPaciente
	 */
	public String getTipoPaciente() {
		return tipoPaciente;
	}

	/**
	 * @param tipoPaciente el tipoPaciente para asignar
	 */
	public void setTipoPaciente(String tipoPaciente) {
		this.tipoPaciente = tipoPaciente;
	}

	/**
	 * @return valor de naturalezaPAciente
	 */
	public int getNaturalezaPAciente() {
		return naturalezaPAciente;
	}

	/**
	 * @param naturalezaPAciente el naturalezaPAciente para asignar
	 */
	public void setNaturalezaPAciente(int naturalezaPAciente) {
		this.naturalezaPAciente = naturalezaPAciente;
	}

	/**
	 * @return valor de tipoEntidadAutoriza
	 */
	public String getTipoEntidadAutoriza() {
		return tipoEntidadAutoriza;
	}

	/**
	 * @param tipoEntidadAutoriza el tipoEntidadAutoriza para asignar
	 */
	public void setTipoEntidadAutoriza(String tipoEntidadAutoriza) {
		this.tipoEntidadAutoriza = tipoEntidadAutoriza;
	}

	/**
	 * @return valor de codigoEntidadSubcontratadaSeleccionada
	 */
	public int getCodigoEntidadSubcontratadaSeleccionada() {
		return codigoEntidadSubcontratadaSeleccionada;
	}

	/**
	 * @param codigoEntidadSubcontratadaSeleccionada el codigoEntidadSubcontratadaSeleccionada para asignar
	 */
	public void setCodigoEntidadSubcontratadaSeleccionada(
			int codigoEntidadSubcontratadaSeleccionada) {
		this.codigoEntidadSubcontratadaSeleccionada = codigoEntidadSubcontratadaSeleccionada;
	}

	/**
	 * @return valor de funcionalidad
	 */
	public String getFuncionalidad() {
		return funcionalidad;
	}

	/**
	 * @param funcionalidad el funcionalidad para asignar
	 */
	public void setFuncionalidad(String funcionalidad) {
		this.funcionalidad = funcionalidad;
	}

	/**
	 * @return valor de tipoAmbulatoriaPeticionesOPyP
	 */
	public boolean isTipoAmbulatoriaPeticionesOPyP() {
		return tipoAmbulatoriaPeticionesOPyP;
	}

	/**
	 * @param tipoAmbulatoriaPeticionesOPyP el tipoAmbulatoriaPeticionesOPyP para asignar
	 */
	public void setTipoAmbulatoriaPeticionesOPyP(
			boolean tipoAmbulatoriaPeticionesOPyP) {
		this.tipoAmbulatoriaPeticionesOPyP = tipoAmbulatoriaPeticionesOPyP;
	}

	/**
	 * @return valor de buscarValidarPrioridadEntidadSubcontratadaIngresarFuncionalidad
	 */
	public boolean isBuscarValidarPrioridadEntidadSubcontratadaIngresarFuncionalidad() {
		return buscarValidarPrioridadEntidadSubcontratadaIngresarFuncionalidad;
	}

	/**
	 * @param buscarValidarPrioridadEntidadSubcontratadaIngresarFuncionalidad el buscarValidarPrioridadEntidadSubcontratadaIngresarFuncionalidad para asignar
	 */
	public void setBuscarValidarPrioridadEntidadSubcontratadaIngresarFuncionalidad(
			boolean buscarValidarPrioridadEntidadSubcontratadaIngresarFuncionalidad) {
		this.buscarValidarPrioridadEntidadSubcontratadaIngresarFuncionalidad = buscarValidarPrioridadEntidadSubcontratadaIngresarFuncionalidad;
	}

	/**
	 * @return valor de tipoOrden
	 */
	public String getTipoOrden() {
		return tipoOrden;
	}

	/**
	 * @param tipoOrden el tipoOrden para asignar
	 */
	public void setTipoOrden(String tipoOrden) {
		this.tipoOrden = tipoOrden;
	}

	/**
	 * @return valor de idIngreso
	 */
	public Integer getIdIngreso() {
		return idIngreso;
	}

	/**
	 * @param idIngreso el idIngreso para asignar
	 */
	public void setIdIngreso(Integer idIngreso) {
		this.idIngreso = idIngreso;
	}

	/**
	 * @return valor de centroAtencion
	 */
	public Integer getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion el centroAtencion para asignar
	 */
	public void setCentroAtencion(Integer centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return valor de centroAtencionAsignado
	 */
	public Integer getCentroAtencionAsignado() {
		return centroAtencionAsignado;
	}

	/**
	 * @param centroAtencionAsignado el centroAtencionAsignado para asignar
	 */
	public void setCentroAtencionAsignado(Integer centroAtencionAsignado) {
		this.centroAtencionAsignado = centroAtencionAsignado;
	}

}
