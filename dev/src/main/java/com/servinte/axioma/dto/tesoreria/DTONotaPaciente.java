/**
 * 
 */
package com.servinte.axioma.dto.tesoreria;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import util.ConstantesBD;
import util.UtilidadTexto;

import com.princetonsa.dto.administracion.DtoPaciente;

 /**
 * Esta clase se encarga de transferir los datos de los objetos 
 * entre las diferentes capas.
 * @author Yennifer Guerrero
 * @since 9/03/2011
 */
public class DTONotaPaciente implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Consecutivo Ingreso
	 */
	
	private String consecutivoIngreso;

	/**
	 * Nombre del centro de atencion ingresos
	 */
	
	private String nombreCentroAtencionOrigen;
	
	/**
	 * Saldo Actual del paciente
	 */
	private double nuevoSaldo;

	/**
	 * Saldo Actual del paciente
	 */
	private double saldoActual;
	

	/**
	 * Consecutivo de devolucion Abono
	 */
	private BigDecimal consecutivo;
	
	/**
	 * Fecha de devolucion Abono Date
	 */
	private Date fechaIngresoNota;
	
	/**
	 * Fecha de devolucion Abono String
	 */
	private String fecha;
	
	/**
	 * Hora de devolucion Abono 
	 */
	private String hora;
	
	/**
	 * Centro de Atencion Origen
	 */
	private int centroAtencionOrigen;
	
	/**
	 * Centro de Atencion Registro 
	 */
	private int centroAtencionRegistro;
	
	/**
	 * Codigo Paciente devolucion Abono 
	 */
	private int codPaciente;
	
	/**
	 * Motivo de devolucion Abono
	 */
	private String observaciones;
	
	
	/**
	 * CodigoPk Detalle Devolucion Abono
	 */
	private long codigoPkDetalleNotaPaciente;

	/**
	 * Codigo de ingreso Devolcion Abono
	 */
	
	private Integer ingreso;
	
	/**
	 * Valor Nota
	 */

	private BigDecimal valorNota;
	
	
	/**
	 * Centro Atencion Duenio
	 */

	private int centroAtencionByCentroAtencionDuenio;


	/**
	 * CodigoPk Devolucion Abonos Paciente
	 */
	private long codigoPk;
	
	/**
	 * Datos del paciente
	 */
	private DtoPaciente paciente;
	
	/**
	 * Login Devolcion Paciente
	 */
	private String loginUsuario;
	
	private String naturalezaNota;
	
	private String descripcionConcepto;
	
	private String usuarioGeneraNota;
	
	private String totalValorNotaPaciente;
	
	private String nombreCompletoPaciente;
	
	private String identificacionPaciente;
	
	private String nombreCentroAtencionDuenio;
	
	/**
	 * Lista de los ingresos por paciente
	 */
	private ArrayList<DTONotaPaciente> listaInfoNotaPaciente = new ArrayList<DTONotaPaciente>();

	
	private ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes> listaDtoInfoIngresoPacienteControlarAbonoPacientes;
	
	/**
	 * Método constructor de la clase
	 * @author Yennifer Guerrero
	 */
	public DTONotaPaciente() {
		
		this.codigoPk = ConstantesBD.codigoNuncaValidoLong;
		this.centroAtencionOrigen = ConstantesBD.codigoNuncaValido;
		//this.codigoPaciente = ConstantesBD.codigoNuncaValido;
		this.loginUsuario = "";
		this.consecutivo = new BigDecimal(ConstantesBD.codigoNuncaValido);
		this.fecha = "";
		this.hora = "";
		this.observaciones = "";
		this.saldoActual = 0;
		this.nuevoSaldo = 0;
		this.valorNota = BigDecimal.ZERO;
		this.ingreso = ConstantesBD.codigoNuncaValido;
		this.consecutivoIngreso = "";
		this.naturalezaNota = "";
		this.descripcionConcepto = "";
		this.setUsuarioGeneraNota("");
		this.setTotalValorNotaPaciente("");
		this.setNombreCompletoPaciente("");
		this.setIdentificacionPaciente("");
		this.setNombreCentroAtencionDuenio("");
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoPk
	
	 * @return retorna la variable codigoPk 
	 * @author Yennifer Guerrero 
	 */
	public long getCodigoPk() {
		return codigoPk;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoPk
	
	 * @param valor para el atributo codigoPk 
	 * @author Yennifer Guerrero
	 */
	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoPaciente
	
	 * @return retorna la variable codigoPaciente 
	 * @author Yennifer Guerrero 
	 */
	public int getCodigoPaciente() {
		return paciente.getCodigo();
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoPaciente
	
	 * @param valor para el atributo codigoPaciente 
	 * @author Yennifer Guerrero
	 */
	public void setCodigoPaciente(int codigoPaciente) {
		if(paciente==null)
		{
			paciente=new DtoPaciente();
		}
		this.paciente.setCodigo(codigoPaciente);
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo loginUsuario
	
	 * @return retorna la variable loginUsuario 
	 * @author Yennifer Guerrero 
	 */
	public String getLoginUsuario() {
		return loginUsuario;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo loginUsuario
	
	 * @param valor para el atributo loginUsuario 
	 * @author Yennifer Guerrero
	 */
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo consecutivo
	
	 * @return retorna la variable consecutivo 
	 * @author Yennifer Guerrero 
	 */
	public BigDecimal getConsecutivo() {
		return consecutivo;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo consecutivo
	
	 * @param valor para el atributo consecutivo 
	 * @author Yennifer Guerrero
	 */
	public void setConsecutivo(BigDecimal consecutivo) {
		this.consecutivo = consecutivo;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo fecha
	
	 * @return retorna la variable fecha 
	 * @author Yennifer Guerrero 
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo fecha
	
	 * @param valor para el atributo fecha 
	 * @author Yennifer Guerrero
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo hora
	
	 * @return retorna la variable hora 
	 * @author Yennifer Guerrero 
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo hora
	
	 * @param valor para el atributo hora 
	 * @author Yennifer Guerrero
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}


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

	/**
	 * @return the listaInfoNotaPaciente
	 */
	public ArrayList<DTONotaPaciente> getListaInfoNotaPaciente() {
		return listaInfoNotaPaciente;
	}

	/**
	 * @param listaInfoNotaPaciente the listaInfoNotaPaciente to set
	 */
	public void setListaInfoNotaPaciente(
			ArrayList<DTONotaPaciente> listaInfoNotaPaciente) {
		this.listaInfoNotaPaciente = listaInfoNotaPaciente;
	}

	/**
	 * Obtiene el total de la devolución de abonos 
	 * @return double
	 */
	public double getTotalDevolucionAbonos()
	{
		double total=0;
		for(DtoInfoIngresoPacienteControlarAbonoPacientes ingreso:listaDtoInfoIngresoPacienteControlarAbonoPacientes)
		{
			total+=ingreso.getValorDevolucion();
		}
		return total;
	}
	
	/**
	 * Obtiene el total de la devolución de abonos formateado
	 * @return String
	 */
	public String getTotalDevolucionAbonosFormateado()
	{
		double total=0;
		String totalFormateado="";
		for(DtoInfoIngresoPacienteControlarAbonoPacientes ingreso:listaDtoInfoIngresoPacienteControlarAbonoPacientes)
		{
			total+=ingreso.getValorDevolucion();
			totalFormateado = UtilidadTexto.formatearValores(total);
		}
		return totalFormateado;
	}
	
	/**
	 * Obtiene el total del saldo actual
	 * @return double
	 */
	public double getTotalSaldoActual()
	{
		double total=0;
		for(DtoInfoIngresoPacienteControlarAbonoPacientes ingreso:listaDtoInfoIngresoPacienteControlarAbonoPacientes)
		{
			total+=ingreso.getSaldoActual();
		}
		return total;
	}

	
	/**
	 * Obtiene el total del saldo actual formateado
	 * @return String
	 */
	public String getTotalSaldoActualFormateado()
	{
		double total=0;
		String totalFormateado="";
		for(DtoInfoIngresoPacienteControlarAbonoPacientes ingreso:listaDtoInfoIngresoPacienteControlarAbonoPacientes)
		{
			total+=ingreso.getSaldoActual();
			totalFormateado = UtilidadTexto.formatearValores(total);
		}
		return totalFormateado;
	}
	
	/**
	 * Obtiene el total del Nuevo saldo
	 * @return String
	 */
	public String getTotalNuevoSaldo()
	{
		
		double total=0;
		String nuevoSaldoFormateado="";
		
		total+=this.getTotalSaldoActual()-this.getTotalDevolucionAbonos();
		nuevoSaldoFormateado = UtilidadTexto.formatearValores(total);
		
		return nuevoSaldoFormateado;
	}
	
	/**
	 * Obtiene saldo anterior
	 * @return
	 */
	public String getTotalSaldoAnteriorFormateado()
	{
		double total=0;
		String totalFormateado="";

		total+=this.getTotalSaldoActual();
		totalFormateado = UtilidadTexto.formatearValores(total);

		return totalFormateado;
	}
	

	
	/**
	 * @return the codPaciente
	 */
	public int getCodPaciente() {
		return codPaciente;
	}

	/**
	 * @param codPaciente the codPaciente to set
	 */
	public void setCodPaciente(int codPaciente) {
		this.codPaciente = codPaciente;
	}
	
	/**
	 * @return the ingreso
	 */
	public Integer getIngreso() {
		return ingreso;
	}

	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngreso(Integer ingreso) {
		this.ingreso = ingreso;
	}

	
	/**
	 * @return the centroAtencionByCentroAtencionDuenio
	 */
	public int getCentroAtencionByCentroAtencionDuenio() {
		return centroAtencionByCentroAtencionDuenio;
	}

	/**
	 * @param centroAtencionByCentroAtencionDuenio the centroAtencionByCentroAtencionDuenio to set
	 */
	public void setCentroAtencionByCentroAtencionDuenio(
			int centroAtencionByCentroAtencionDuenio) {
		this.centroAtencionByCentroAtencionDuenio = centroAtencionByCentroAtencionDuenio;
	}

	
	/**
	 * @return the nombreCentroAtencionIngresos
	 */
	public String getNombreCentroAtencionOrigen() {
		return nombreCentroAtencionOrigen;
	}

	/**
	 * @param nombreCentroAtencionIngresos the nombreCentroAtencionIngresos to set
	 */
	public void setNombreCentroAtencionOrigen(String nombreCentroAtencionOrigen) {
		this.nombreCentroAtencionOrigen = nombreCentroAtencionOrigen;
	}

	/**
	 * @return the nuevoSaldo
	 */
	public double getNuevoSaldo() {
		//return nuevoSaldo;
		nuevoSaldo = this.saldoActual-this.valorNota.doubleValue();
		return nuevoSaldo;
	}
	
	/**
	 * @return the nuevoSaldo
	 */
	public String getNuevoSaldoFormateado() {
		//return nuevoSaldo;
		nuevoSaldo = this.saldoActual-this.valorNota.doubleValue();
		String nuevoSaldoFormateado = UtilidadTexto.formatearValores(nuevoSaldo);
		return nuevoSaldoFormateado;
	}

	/**
	 * @param nuevoSaldo the nuevoSaldo to set
	 */
	public void setNuevoSaldo(double nuevoSaldo) {
		this.nuevoSaldo = nuevoSaldo;
	}
	
	
	/**
	 * @return the saldoActual
	 */
	public double getSaldoActual() {
		return saldoActual;
	}

	/**
	 * @return the saldoActual
	 */
	public String getSaldoActualFormateado() {
		
		String saldoActualFormateado = UtilidadTexto.formatearValores(this.saldoActual);
		return saldoActualFormateado;
	}
	
	/**
	 * @param saldoActual the saldoActual to set
	 */
	public void setSaldoActual(double saldoActual) {
		this.saldoActual = saldoActual;
	}


	/**
	 * @return the valorDevolucion
	 */
	public BigDecimal getValorDevolucion() {
		return valorNota;
	}

	/**
	 * @return the valorDevolucion
	 */
	public String getValorDevolucionFormateado() {
	
		String valorDevolucionFormateado = UtilidadTexto.formatearValores(this.valorNota.doubleValue());
		return valorDevolucionFormateado;
	}

	/**
	 * @param valorDevolucion the valorDevolucion to set
	 */
	public void setValorDevolucion(BigDecimal valorDevolucion) {
		this.valorNota = valorDevolucion;
	}
	
	public String getSaldoActualFormateado(double saldoActual){
		
		String saldoActualFormateado="";
		saldoActualFormateado = UtilidadTexto.formatearValores(this.saldoActual);
		return saldoActualFormateado;
		
	}
	
	public String getNuevoSaldoFormateado(double nuevoSaldo){
		
		String nuevoSaldoFormateado="";
		nuevoSaldoFormateado = UtilidadTexto.formatearValores(this.nuevoSaldo);
		return nuevoSaldoFormateado;
		
	}
	/**
	 * @return the consecutivoIngreso
	 */
	public String getConsecutivoIngreso() {
		return consecutivoIngreso;
	}

	/**
	 * @param consecutivoIngreso the consecutivoIngreso to set
	 */
	public void setConsecutivoIngreso(String consecutivoIngreso) {
		this.consecutivoIngreso = consecutivoIngreso;
	}

	public Date getFechaIngresoNota() {
		return fechaIngresoNota;
	}

	public void setFechaIngresoNota(Date fechaIngresoNota) {
		this.fechaIngresoNota = fechaIngresoNota;
	}

	public int getCentroAtencionOrigen() {
		return centroAtencionOrigen;
	}

	public void setCentroAtencionOrigen(int centroAtencionOrigen) {
		this.centroAtencionOrigen = centroAtencionOrigen;
	}

	public int getCentroAtencionRegistro() {
		return centroAtencionRegistro;
	}

	public void setCentroAtencionRegistro(int centroAtencionRegistro) {
		this.centroAtencionRegistro = centroAtencionRegistro;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public long getCodigoPkDetalleNotaPaciente() {
		return codigoPkDetalleNotaPaciente;
	}

	public void setCodigoPkDetalleNotaPaciente(long codigoPkDetalleNotaPaciente) {
		this.codigoPkDetalleNotaPaciente = codigoPkDetalleNotaPaciente;
	}

	public BigDecimal getValorNota() {
		return valorNota;
	}

	public void setValorNota(BigDecimal valorNota) {
		this.valorNota = valorNota;
	}

	/**
	 * @param listaDtoInfoIngresoPacienteControlarAbonoPacientes the listaDtoInfoIngresoPacienteControlarAbonoPacientes to set
	 */
	public void setListaDtoInfoIngresoPacienteControlarAbonoPacientes(
			ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes> listaDtoInfoIngresoPacienteControlarAbonoPacientes) {
		this.listaDtoInfoIngresoPacienteControlarAbonoPacientes = listaDtoInfoIngresoPacienteControlarAbonoPacientes;
	}

	/**
	 * @return the listaDtoInfoIngresoPacienteControlarAbonoPacientes
	 */
	public ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes> getListaDtoInfoIngresoPacienteControlarAbonoPacientes() {
		return listaDtoInfoIngresoPacienteControlarAbonoPacientes;
	}

	/**
	 * @param naturalezaNota the naturalezaNota to set
	 */
	public void setNaturalezaNota(String naturalezaNota) {
		this.naturalezaNota = naturalezaNota;
	}

	/**
	 * @return the naturalezaNota
	 */
	public String getNaturalezaNota() {
		return naturalezaNota;
	}

	/**
	 * @param descripcionConcepto the descripcionConcepto to set
	 */
	public void setDescripcionConcepto(String descripcionConcepto) {
		this.descripcionConcepto = descripcionConcepto;
	}

	/**
	 * @return the descripcionConcepto
	 */
	public String getDescripcionConcepto() {
		return descripcionConcepto;
	}

	/**
	 * @param usuarioGeneraNota the usuarioGeneraNota to set
	 */
	public void setUsuarioGeneraNota(String usuarioGeneraNota) {
		this.usuarioGeneraNota = usuarioGeneraNota;
	}

	/**
	 * @return the usuarioGeneraNota
	 */
	public String getUsuarioGeneraNota() {
		return usuarioGeneraNota;
	}

	/**
	 * @param totalValorNotaPaciente the totalValorNotaPaciente to set
	 */
	public void setTotalValorNotaPaciente(String totalValorNotaPaciente) {
		this.totalValorNotaPaciente = totalValorNotaPaciente;
	}

	/**
	 * @return the totalValorNotaPaciente
	 */
	public String getTotalValorNotaPaciente() {
		return totalValorNotaPaciente;
	}

	/**
	 * @param nombreCompletoPaciente the nombreCompletoPaciente to set
	 */
	public void setNombreCompletoPaciente(String nombreCompletoPaciente) {
		this.nombreCompletoPaciente = nombreCompletoPaciente;
	}

	/**
	 * @return the nombreCompletoPaciente
	 */
	public String getNombreCompletoPaciente() {
		return nombreCompletoPaciente;
	}

	/**
	 * @param identificacionPaciente the identificacionPaciente to set
	 */
	public void setIdentificacionPaciente(String identificacionPaciente) {
		this.identificacionPaciente = identificacionPaciente;
	}

	/**
	 * @return the identificacionPaciente
	 */
	public String getIdentificacionPaciente() {
		return identificacionPaciente;
	}

	/**
	 * @param nombreCentroAtencionDuenio the nombreCentroAtencionDuenio to set
	 */
	public void setNombreCentroAtencionDuenio(String nombreCentroAtencionDuenio) {
		this.nombreCentroAtencionDuenio = nombreCentroAtencionDuenio;
	}

	/**
	 * @return the nombreCentroAtencionDuenio
	 */
	public String getNombreCentroAtencionDuenio() {
		return nombreCentroAtencionDuenio;
	}


	
}
