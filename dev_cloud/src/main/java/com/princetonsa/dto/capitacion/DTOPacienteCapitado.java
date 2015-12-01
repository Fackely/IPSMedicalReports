package com.princetonsa.dto.capitacion;

import java.io.Serializable;
import java.math.BigDecimal;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.servinte.axioma.orm.Convenios;

/**
 * Dto encargado de almacenar los 
 * datos del Paciente Capitado
 * @author Diana Carolina G
 * 
 */

public class DTOPacienteCapitado extends DtoPersonas  implements Serializable  {

	private static final long serialVersionUID = 1L;
	
	
	private Convenios convenio = new Convenios();
	private Integer codigoClasificacionSE;
	private String clasificacionSocioEconomica;
	private String tipoAfiliado;
	private Character tipoAfiliadoChar;
	private String tipoMontoCobro;
	private BigDecimal valorPorcentajeMontoCobro;
	private BigDecimal valorMontoCobro;
	private int semanasCotizacion;
	private int edadPaciente;
	private int codigoViaIngreso;
	private int codigoNaturalezaPaciente;
	private String acronimotipoPaciente;
	private long codigoPkEntidadSubcontratada;
	private String descripcionEntidadSubOtra;
	private String direccionEntidadSubOtra;
	private String telefonoEntidadSubOtra;
	private int idCuenta;
	private int idIngreso;
	Integer estadoCuenta;
	private int codigoCentroCosto;
	private String nombreCentroCosto;
	private String nombreViaIngreso;
	private Integer codigoContrato;
	//Contiene la edad del paciente a partir de la fecha de nacimiento hasta la fecha de generación de la autorización.
	private String edadPacienteCapitado;	
	
	// Diagnostico	
	private String diagnosticoIngEst;		
	private Integer tipoCieDxIngEst;	
	private String descripcionDiagnosticoIngEst;
	
	/**
	 * Atributo que almacena el codigo del centro de costo que responde el servicio
	 * Medicamentos o insumo de ingreso estancia
	 */
	private int centroCostoRespondeIngreEstancia;
	
	/**
	 * Atributo que permite almacenar la autorización de ingreso estancia
	 */
	private long codIngresoEstancia;
	
	/**
	 * Atributo que almacena el tipo de entidad que ejecuta para el centro de costo del ingreso
	 * estancia.
	 */
	private String tipoEntidadEjecuta;
	
	
	
	public DTOPacienteCapitado(){
		
	}

	public String getClasificacionSocioEconomica() {
		return clasificacionSocioEconomica;
	}

	public void setClasificacionSocioEconomica(String clasificacionSocioEconomica) {
		this.clasificacionSocioEconomica = clasificacionSocioEconomica;
	}

	public String getTipoAfiliado() {
		return tipoAfiliado;
	}

	public void setTipoAfiliado(String tipoAfiliado) {
		this.tipoAfiliado = tipoAfiliado;
	}

	public String getTipoMontoCobro() {
		return tipoMontoCobro;
	}

	public void setTipoMontoCobro(String tipoMontoCobro) {
		this.tipoMontoCobro = tipoMontoCobro;
	}

	public BigDecimal getValorPorcentajeMontoCobro() {
		return valorPorcentajeMontoCobro;
	}

	public void setValorPorcentajeMontoCobro(BigDecimal valorPorcentajeMontoCobro) {
		this.valorPorcentajeMontoCobro = valorPorcentajeMontoCobro;
	}

	public int getSemanasCotizacion() {
		return semanasCotizacion;
	}

	public void setSemanasCotizacion(int semanasCotizacion) {
		this.semanasCotizacion = semanasCotizacion;
	}

	public void setConvenio(Convenios convenio) {
		this.convenio = convenio;
	}

	public Convenios getConvenio() {
		return convenio;
	}

	public int getEdadPaciente() {
		return edadPaciente;
	}

	public void setEdadPaciente(int edadPaciente) {
		this.edadPaciente = edadPaciente;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoNaturalezaPaciente
	
	 * @return retorna la variable codigoNaturalezaPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoNaturalezaPaciente() {
		return codigoNaturalezaPaciente;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoNaturalezaPaciente
	
	 * @param valor para el atributo codigoNaturalezaPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoNaturalezaPaciente(int codigoNaturalezaPaciente) {
		this.codigoNaturalezaPaciente = codigoNaturalezaPaciente;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo acronimotipoPaciente
	
	 * @return retorna la variable acronimotipoPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public String getAcronimotipoPaciente() {
		return acronimotipoPaciente;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo acronimotipoPaciente
	
	 * @param valor para el atributo acronimotipoPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public void setAcronimotipoPaciente(String acronimotipoPaciente) {
		this.acronimotipoPaciente = acronimotipoPaciente;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoViaIngreso
	
	 * @return retorna la variable codigoViaIngreso 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoViaIngreso() {
		return codigoViaIngreso;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoViaIngreso
	
	 * @param valor para el atributo codigoViaIngreso 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoViaIngreso(int codigoViaIngreso) {
		this.codigoViaIngreso = codigoViaIngreso;
	}

	public void setTipoAfiliadoChar(Character tipoAfiliadoChar) {
		this.tipoAfiliadoChar = tipoAfiliadoChar;
	}

	public Character getTipoAfiliadoChar() {
		return tipoAfiliadoChar;
	}

	public void setCodigoPkEntidadSubcontratada(long codigoPkEntidadSubcontratada) {
		this.codigoPkEntidadSubcontratada = codigoPkEntidadSubcontratada;
	}

	public long getCodigoPkEntidadSubcontratada() {
		return codigoPkEntidadSubcontratada;
	}

	public void setIdCuenta(int idCuenta) {
		this.idCuenta = idCuenta;
	}

	public int getIdCuenta() {
		return idCuenta;
	}

	public void setIdIngreso(int idIngreso) {
		this.idIngreso = idIngreso;
	}

	public int getIdIngreso() {
		return idIngreso;
	}

	/**
	 * Este Método se encarga de obtener el valor del atributo estadoCuenta
	 * @return retorna la variable estadoCuenta 
	 * @author Cristhian Murillo
	 */
	public Integer getEstadoCuenta() {
		return estadoCuenta;
	}

	/**
	 * Este Método se encarga de establecer el valor del atributo estadoCuenta
	 * @param valor para el atributo estadoCuenta 
	 * @author Cristhian Murillo
	 */
	public void setEstadoCuenta(Integer estadoCuenta) {
		this.estadoCuenta = estadoCuenta;
	}

	/**
	 * @return the codigoCentroCosto
	 */
	public int getCodigoCentroCosto() {
		return codigoCentroCosto;
	}

	/**
	 * @param codigoCentroCosto the codigoCentroCosto to set
	 */
	public void setCodigoCentroCosto(int codigoCentroCosto) {
		this.codigoCentroCosto = codigoCentroCosto;
	}

	/**
	 * @return the nombreCentroCosto
	 */
	public String getNombreCentroCosto() {
		return nombreCentroCosto;
	}

	/**
	 * @param nombreCentroCosto the nombreCentroCosto to set
	 */
	public void setNombreCentroCosto(String nombreCentroCosto) {
		this.nombreCentroCosto = nombreCentroCosto;
	}

	/**
	 * @return the nombreViaIngreso
	 */
	public String getNombreViaIngreso() {
		return nombreViaIngreso;
	}

	/**
	 * @param nombreViaIngreso the nombreViaIngreso to set
	 */
	public void setNombreViaIngreso(String nombreViaIngreso) {
		this.nombreViaIngreso = nombreViaIngreso;
	}

	public void setCodigoContrato(Integer codigoContrato) {
		this.codigoContrato = codigoContrato;
	}

	public Integer getCodigoContrato() {
		return codigoContrato;
	}

	public void setDescripcionEntidadSubOtra(String descripcionEntidadSubOtra) {
		this.descripcionEntidadSubOtra = descripcionEntidadSubOtra;
	}

	public String getDescripcionEntidadSubOtra() {
		return descripcionEntidadSubOtra;
	}

	public void setDireccionEntidadSubOtra(String direccionEntidadSubOtra) {
		this.direccionEntidadSubOtra = direccionEntidadSubOtra;
	}

	public String getDireccionEntidadSubOtra() {
		return direccionEntidadSubOtra;
	}

	public void setTelefonoEntidadSubOtra(String telefonoEntidadSubOtra) {
		this.telefonoEntidadSubOtra = telefonoEntidadSubOtra;
	}

	public String getTelefonoEntidadSubOtra() {
		return telefonoEntidadSubOtra;
	}

	public void setDiagnosticoIngEst(String diagnosticoIngEst) {
		this.diagnosticoIngEst = diagnosticoIngEst;
	}

	public String getDiagnosticoIngEst() {
		return diagnosticoIngEst;
	}

	public void setTipoCieDxIngEst(Integer tipoCieDxIngEst) {
		this.tipoCieDxIngEst = tipoCieDxIngEst;
	}

	public Integer getTipoCieDxIngEst() {
		return tipoCieDxIngEst;
	}

	public void setDescripcionDiagnosticoIngEst(
			String descripcionDiagnosticoIngEst) {
		this.descripcionDiagnosticoIngEst = descripcionDiagnosticoIngEst;
	}

	public String getDescripcionDiagnosticoIngEst() {
		return descripcionDiagnosticoIngEst;
	}

	public void setCodigoClasificacionSE(Integer codigoClasificacionSE) {
		this.codigoClasificacionSE = codigoClasificacionSE;
	}

	public Integer getCodigoClasificacionSE() {
		return codigoClasificacionSE;
	}
	
	public void setEdadPacienteCapitado(String edadPacienteCapitado) {
		this.edadPacienteCapitado = edadPacienteCapitado;
	}

	public String getEdadPacienteCapitado() {
		return edadPacienteCapitado;
	}

	/**
	 * @return the valorMontoCobro
	 */
	public BigDecimal getValorMontoCobro() {
		return valorMontoCobro;
	}

	/**
	 * @param valorMontoCobro the valorMontoCobro to set
	 */
	public void setValorMontoCobro(BigDecimal valorMontoCobro) {
		this.valorMontoCobro = valorMontoCobro;
	}

	public int getCentroCostoRespondeIngreEstancia() {
		return centroCostoRespondeIngreEstancia;
	}

	public void setCentroCostoRespondeIngreEstancia(
			int centroCostoRespondeIngreEstancia) {
		this.centroCostoRespondeIngreEstancia = centroCostoRespondeIngreEstancia;
	}

	public long getCodIngresoEstancia() {
		return codIngresoEstancia;
	}

	public void setCodIngresoEstancia(long codIngresoEstancia) {
		this.codIngresoEstancia = codIngresoEstancia;
	}

	public String getTipoEntidadEjecuta() {
		return tipoEntidadEjecuta;
	}

	public void setTipoEntidadEjecuta(String tipoEntidadEjecuta) {
		this.tipoEntidadEjecuta = tipoEntidadEjecuta;
	}

	
}
