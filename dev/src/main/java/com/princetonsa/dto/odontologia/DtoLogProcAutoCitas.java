package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import util.ConstantesBD;
import util.InfoDatosDouble;
/**
 * 
 * @author axioma
 *
 */
@SuppressWarnings("serial")
public class DtoLogProcAutoCitas  implements Serializable
{
	/**
	 * Llave primaria
	 */
	private BigDecimal codigoPk ;
	
	/**
	 * Fecha de ejecución del proceso
	 */
	private String fechaEjecucion ;
	
	/**
	 * Hora de ejecución del proceso
	 */
	private String horaEjecucion ;
	
	/**
	 * Cita odontológica
	 */
	private BigDecimal citaOdontologica;
	
	/**
	 * Estado inicial de la cita
	 */
	private String estadoInicialCita ;
	
	/**
	 * Institución para la cual se ejecuta el proceso
	 */
	private int institucion;
	
	/**
	 * Valor de la cita
	 */
	private double valorTarifa;
	
	/**
	 * Ingreso del paciente al momento de la asignación de la cita
	 */
	private Integer ingreso;
	
	/**
	 * Código del paciente al cual se le asignó la cita
	 */
	private int codigoPaciente;
	
	/**
	 * Lista de los servicios de la cita
	 */
	ArrayList<InfoDatosDouble> listaLogServicios = new ArrayList<InfoDatosDouble>();
	
	/**
	 * Almacena los servicios asociados a la cita odontológica.
	 */
	ArrayList<DtoServicioCitaOdontologica> serviciosCita;
	
	/**
	 * Atributo que almacena el código del centro de atención de la cita.
	 */
	private int codigoCentroAtencion;
	
	/**
	 * 
	 */
	public DtoLogProcAutoCitas (){
		this.reset();
	}
	
	/**
	 * 
	 */
	void reset(){
		this.codigoPk =  new BigDecimal(ConstantesBD.codigoNuncaValidoDouble) ;
		this.fechaEjecucion ="" ;
		this.horaEjecucion="" ;
		this.citaOdontologica = new BigDecimal(ConstantesBD.codigoNuncaValidoDouble);
		this.estadoInicialCita ="";
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.listaLogServicios = new ArrayList<InfoDatosDouble>();
		this.valorTarifa=0;
		this.ingreso=null;
		this.codigoPaciente=ConstantesBD.codigoNuncaValido;
		this.serviciosCita = new ArrayList<DtoServicioCitaOdontologica>();
		this.codigoCentroAtencion = ConstantesBD.codigoNuncaValido;
	}

	/**
	 * @return Retorna el atributo codigoPk
	 */
	public BigDecimal getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk Asigna el atributo codigoPk
	 */
	public void setCodigoPk(BigDecimal codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * @return Retorna el atributo fechaEjecucion
	 */
	public String getFechaEjecucion() {
		return fechaEjecucion;
	}

	/**
	 * @param fechaEjecucion Asigna el atributo fechaEjecucion
	 */
	public void setFechaEjecucion(String fechaEjecucion) {
		this.fechaEjecucion = fechaEjecucion;
	}

	/**
	 * @return Retorna el atributo horaEjecucion
	 */
	public String getHoraEjecucion() {
		return horaEjecucion;
	}

	/**
	 * @param horaEjecucion Asigna el atributo horaEjecucion
	 */
	public void setHoraEjecucion(String horaEjecucion) {
		this.horaEjecucion = horaEjecucion;
	}

	/**
	 * @return Retorna el atributo citaOdontologica
	 */
	public BigDecimal getCitaOdontologica() {
		return citaOdontologica;
	}

	/**
	 * @param citaOdontologica Asigna el atributo citaOdontologica
	 */
	public void setCitaOdontologica(BigDecimal citaOdontologica) {
		this.citaOdontologica = citaOdontologica;
	}

	/**
	 * @return Retorna el atributo estadoInicialCita
	 */
	public String getEstadoInicialCita() {
		return estadoInicialCita;
	}

	/**
	 * @param estadoInicialCita Asigna el atributo estadoInicialCita
	 */
	public void setEstadoInicialCita(String estadoInicialCita) {
		this.estadoInicialCita = estadoInicialCita;
	}

	/**
	 * @return Retorna el atributo institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion Asigna el atributo institucion
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return Retorna el atributo valorTarifa
	 */
	public double getValorTarifa() {
		return valorTarifa;
	}

	/**
	 * @param valorTarifa Asigna el atributo valorTarifa
	 */
	public void setValorTarifa(double valorTarifa) {
		this.valorTarifa = valorTarifa;
	}

	/**
	 * @return Retorna el atributo listaLogServicios
	 */
	public ArrayList<InfoDatosDouble> getListaLogServicios() {
		return listaLogServicios;
	}

	/**
	 * @param listaLogServicios Asigna el atributo listaLogServicios
	 */
	public void setListaLogServicios(ArrayList<InfoDatosDouble> listaLogServicios) {
		this.listaLogServicios = listaLogServicios;
	}

	/**
	 * @return Retorna el atributo ingreso
	 */
	public Integer getIngreso() {
		return ingreso;
	}

	/**
	 * @param ingreso Asigna el atributo ingreso
	 */
	public void setIngreso(Integer ingreso) {
		this.ingreso = ingreso;
	}

	/**
	 * @return Retorna el atributo codigoPaciente
	 */
	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	/**
	 * @param codigoPaciente Asigna el atributo codigoPaciente
	 */
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo serviciosCita
	
	 * @return retorna la variable serviciosCita 
	 * @author Yennifer Guerrero 
	 */
	public ArrayList<DtoServicioCitaOdontologica> getServiciosCita() {
		return serviciosCita;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo serviciosCita
	
	 * @param valor para el atributo serviciosCita 
	 * @author Yennifer Guerrero
	 */
	public void setServiciosCita(
			ArrayList<DtoServicioCitaOdontologica> serviciosCita) {
		this.serviciosCita = serviciosCita;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoCentroAtencion
	 * @return retorna la variable codigoCentroAtencion 
	 * @author Yennifer Guerrero 
	 */
	public int getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoCentroAtencion
	 * @param valor para el atributo codigoCentroAtencion 
	 * @author Yennifer Guerrero
	 */
	public void setCodigoCentroAtencion(int codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}
}
