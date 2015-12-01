package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.NumberFormat;


import net.sf.jasperreports.engine.JRDataSource;

import util.ConstantesBD;
import util.InfoDatosStr;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

public class DtoConsolidadoPresupuestoContratadoPorPromocion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long codigoEmpresaInstitucion;
	private long codigoPkPresupuesto;
	private long consecutivoPresupuesto;
	//private String codigoInstitucion;
	private Date fechaModifica;
	private int codigoPkPromocion;
	private String loginProfesionalContrato;
	private String primerNombreProfesionalContrato;
	private String segundoNombreProfesionalContrato;
	private String primerApellidoProfesionalContrato;
	private String segundoApellidoProfesionalContrato;
	private String nombreCompletoProfesionalContrato;
	private String nombrePromocion;
	private long codigoPkPrograma;
	private String codigoPrograma;
	private String nombrePrograma;
	private String razonSocial;
	private String fechaInicial;
	private String fechaFinal;
	private Date fechaInicialvige;
	private Date fechaFinalvige;
	private int codigoPaciente;
	private String tipoId;
	private String numeroId;
	private String primerNombre;
	private String segundoNombre;
	private String primerApellido;
	private String segundoApellido;
	private String nombreCompletoPaciente;
	private String numeroContrato;
	private Date fechaContrato;
	@SuppressWarnings("unused")
	private String ayudanteFechaIniVige;
	@SuppressWarnings("unused")
	private String ayudanteFechaFinVige;
	@SuppressWarnings("unused")
	private String ayudanteFechaContrato;
	private int consCentroAtencionDuenio;
	private String descCentroAtencionDuenio;
	private InfoDatosStr servicio;
	private String nombreUsuarioProceso;
	private String logoDerecha;
	private String logoIzquierda;
	private String descripcionEmpresaInstitucion;
	private String descripcionCentroAtencion;
	private String descripcionCiudad;
	private String descripcionPais;
	private String descripcionRegionCobertura;
	
	private BigDecimal totalPorPromocion;
	private String totalPromociones;
	private String ayudanteNombreServicio;
	private int consecutivoCentroAtencion;
	private int consCentroAtencionContrato;
	private String descCentroAtencionContrato;
	private String estadoPresupuesto;
	private String ayudanteNumeroId;
	
	private BigDecimal valorPresupuesto;
	private BigDecimal valorDescuentoProm;
	
	private String descripcionInstCentroCont;
	
	private BigDecimal totalPromo;
	private String totalPromocion;
	 /**
	  * Atributo que almacena el valor del resupuesto para ser mostrado en el reporte
	  */
    private String valorPresupuestoFormato;
    
    /**
	  * Atributo que almacena el valor de la promocion para ser mostrada en el reporte
	  */
    private String valorDescuentoPromFormato;
	private String ayudanteEstadoPresupuesto;
	//private String nombreInstitucion;
	private boolean parametroMultiEmp;
    /** Objeto jasper para el subreporte de cambio Promocion */
	private JRDataSource dsResultadoPromocion;
    private ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion> listadoConsolidadoPorPromocion;
		
	public DtoConsolidadoPresupuestoContratadoPorPromocion() {
		//this.totalPresupuestoPorEstado=ConstantesBD.codigoNuncaValido;
		this.totalPromo = new BigDecimal("00000000.00");
		this.razonSocial="";
		this.fechaInicial="";
		this.fechaFinal="";
		this.nombreUsuarioProceso="";
		this.logoDerecha="";
		this.logoIzquierda="";
		this.descripcionEmpresaInstitucion="";
		this.descripcionCentroAtencion="";
		this.descripcionCiudad="";
		this.descripcionPais="";
		//this.nombreCompletoPaciente="";
		//this.numeroContrato=ConstantesBD.codigoNuncaValidoLong;
		this.descripcionRegionCobertura="";
		this.setServicio(new InfoDatosStr());
		this.listadoConsolidadoPorPromocion=new ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion>();
		//this.setTotalPorPromocion(ConstantesBD.codigoNuncaValido);
		this.descCentroAtencionContrato="";
		//this.nombreInstitucion="";
		this.parametroMultiEmp=false;
		this.setAyudanteEstadoPresupuesto("");
		//this.rutaEstilos="";
	}
	public int getConsCentroAtencionDuenio() {
		return consCentroAtencionDuenio;
	}
	public void setConsCentroAtencionDuenio(int consCentroAtencionDuenio) {
		this.consCentroAtencionDuenio = consCentroAtencionDuenio;
	}
	public String getDescCentroAtencionDuenio() {
		return descCentroAtencionDuenio;
	}
	public void setDescCentroAtencionDuenio(String descCentroAtencionDuenio) {
		this.descCentroAtencionDuenio = descCentroAtencionDuenio;
	}
	public int getCodigoPaciente() {
		return codigoPaciente;
	}
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}
	public String getTipoId() {
		return tipoId;
	}
	public void setTipoId(String tipoId) {
		this.tipoId = tipoId;
	}
	public String getNumeroId() {
		return numeroId;
	}
	public void setNumeroId(String numeroId) {
		this.numeroId = numeroId;
	}
	public String getPrimerNombre() {
		return primerNombre;
	}
	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}
	public String getSegundoNombre() {
		return segundoNombre;
	}
	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}
	public String getPrimerApellido() {
		return primerApellido;
	}
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}
	public String getSegundoApellido() {
		return segundoApellido;
	}
	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}
	public String getNombreCompletoPaciente() {
		return nombreCompletoPaciente;
	}
	public void setNombreCompletoPaciente(String nombreCompletoPaciente) {
		this.nombreCompletoPaciente = nombreCompletoPaciente;
	}
	public String getNombreCompletoProfesionalContrato() {
			return nombreCompletoProfesionalContrato;
	
	}
	public void setNombreCompletoProfesionalContrato(
			String nombreCompletoProfesionalContrato) {
		this.nombreCompletoProfesionalContrato = nombreCompletoProfesionalContrato;
	}
	
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getFechaInicial() {
		return fechaInicial;
	}
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	public String getFechaFinal() {
		return fechaFinal;
	}
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	public InfoDatosStr getServicio() {
		return servicio;
	}
	public void setServicio(InfoDatosStr servicio) {
		this.servicio = servicio;
	}
	public String getNombreUsuarioProceso() {
		return nombreUsuarioProceso;
	}
	public void setNombreUsuarioProceso(String nombreUsuarioProceso) {
		this.nombreUsuarioProceso = nombreUsuarioProceso;
	}
	public String getLogoDerecha() {
		return logoDerecha;
	}
	public void setLogoDerecha(String logoDerecha) {
		this.logoDerecha = logoDerecha;
	}
	public String getLogoIzquierda() {
		return logoIzquierda;
	}
	public void setLogoIzquierda(String logoIzquierda) {
		this.logoIzquierda = logoIzquierda;
	}
	public String getDescripcionEmpresaInstitucion() {
		return descripcionEmpresaInstitucion;
	}
	public void setDescripcionEmpresaInstitucion(
			String descripcionEmpresaInstitucion) {
		this.descripcionEmpresaInstitucion = descripcionEmpresaInstitucion;
	}
	public String getDescripcionCentroAtencion() {
		return descripcionCentroAtencion;
	}
	public void setDescripcionCentroAtencion(String descripcionCentroAtencion) {
		this.descripcionCentroAtencion = descripcionCentroAtencion;
	}
	public String getDescripcionCiudad() {
		return descripcionCiudad;
	}
	public void setDescripcionCiudad(String descripcionCiudad) {
		this.descripcionCiudad = descripcionCiudad;
	}
	public String getDescripcionPais() {
		return descripcionPais;
	}
	public void setDescripcionPais(String descripcionPais) {
		this.descripcionPais = descripcionPais;
	}
	public String getDescripcionRegionCobertura() {
		return descripcionRegionCobertura;
	}
	public void setDescripcionRegionCobertura(String descripcionRegionCobertura) {
		this.descripcionRegionCobertura = descripcionRegionCobertura;
	}
	public String getAyudanteNombreServicio() {
		return ayudanteNombreServicio;
	}
	public void setAyudanteNombreServicio(String ayudanteNombreServicio) {
		this.ayudanteNombreServicio = ayudanteNombreServicio;
	}
	public int getConsecutivoCentroAtencion() {
		return consecutivoCentroAtencion;
	}
	public void setConsecutivoCentroAtencion(int consecutivoCentroAtencion) {
		this.consecutivoCentroAtencion = consecutivoCentroAtencion;
	}
	public String getDescCentroAtencionContrato() {
		return descCentroAtencionContrato;
	}
	public void setDescCentroAtencionContrato(String descCentroAtencionContrato) {
		this.descCentroAtencionContrato = descCentroAtencionContrato;
	}
	public boolean isParametroMultiEmp() {
		return parametroMultiEmp;
	}
	public void setParametroMultiEmp(boolean parametroMultiEmp) {
		this.parametroMultiEmp = parametroMultiEmp;
	}
	public ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion> getListadoConsolidadoPorPromocion() {
		return listadoConsolidadoPorPromocion;
	}
	public void setListadoConsolidadoPorPromocion(
			ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion> listadoConsolidadoPorPromocion) {
		this.listadoConsolidadoPorPromocion = listadoConsolidadoPorPromocion;
	}
	public JRDataSource getDsResultadoPromocion() {
		return dsResultadoPromocion;
	}
	public void setDsResultadoPromocion(JRDataSource dsResultadoPromocion) {
		this.dsResultadoPromocion = dsResultadoPromocion;
	}
	public void setNombrePromocion(String nombrePromocion) {
		this.nombrePromocion = nombrePromocion;
	}
	public String getNombrePromocion() {
		return nombrePromocion;
	}
	public void setFechaInicialvige(Date fechaInicialvige) {
		this.fechaInicialvige = fechaInicialvige;
	}
	public Date getFechaInicialvige() {
		return fechaInicialvige;
	}
	public void setFechaFinalvige(Date fechaFinalvige) {
		this.fechaFinalvige = fechaFinalvige;
	}
	public Date getFechaFinalvige() {
		return fechaFinalvige;
	}
	public void setNombrePrograma(String nombrePrograma) {
		this.nombrePrograma = nombrePrograma;
	}
	public String getNombrePrograma() {
		return nombrePrograma;
	}
	public void setCodigoEmpresaInstitucion(long codigoEmpresaInstitucion) {
		this.codigoEmpresaInstitucion = codigoEmpresaInstitucion;
	}
	public long getCodigoEmpresaInstitucion() {
		return codigoEmpresaInstitucion;
	}
	public void setFechaModifica(Date fechaModifica) {
		this.fechaModifica = fechaModifica;
	}
	public Date getFechaModifica() {
		return fechaModifica;
	}
	/*public void setCodigoInstitucion(String codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}
	public String getCodigoInstitucion() {
		return codigoInstitucion;
	}*/
	public void setConsCentroAtencionContrato(int consCentroAtencionContrato) {
		this.consCentroAtencionContrato = consCentroAtencionContrato;
	}
	public int getConsCentroAtencionContrato() {
		return consCentroAtencionContrato;
	}
	public void setEstadoPresupuesto(String estadoPresupuesto) {
		this.estadoPresupuesto = estadoPresupuesto;
	}
	public String getEstadoPresupuesto() {
		return estadoPresupuesto;
	}
	
	public int getCodigoPkPromocion() {
		return codigoPkPromocion;
	}
	public void setCodigoPkPromocion(int codigoPkPromocion) {
		this.codigoPkPromocion = codigoPkPromocion;
	}
	public void setConsecutivoPresupuesto(long consecutivoPresupuesto) {
		this.consecutivoPresupuesto = consecutivoPresupuesto;
	}
	public long getCodigoPkPrograma() {
		return codigoPkPrograma;
	}
	public void setCodigoPkPrograma(long codigoPkPrograma) {
		this.codigoPkPrograma = codigoPkPrograma;
	}
	public String getCodigoPrograma() {
		return codigoPrograma;
	}
	public void setCodigoPrograma(String codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}
	public void setLoginProfesionalContrato(String loginProfesionalContrato) {
		this.loginProfesionalContrato = loginProfesionalContrato;
	}
	public String getLoginProfesionalContrato() {
		return loginProfesionalContrato;
	}
	public String getPrimerNombreProfesionalContrato() {
		return primerNombreProfesionalContrato;
	}
	public void setPrimerNombreProfesionalContrato(
			String primerNombreProfesionalContrato) {
		this.primerNombreProfesionalContrato = primerNombreProfesionalContrato;
	}
	public String getSegundoNombreProfesionalContrato() {
		return segundoNombreProfesionalContrato;
	}
	public void setSegundoNombreProfesionalContrato(
			String segundoNombreProfesionalContrato) {
		this.segundoNombreProfesionalContrato = segundoNombreProfesionalContrato;
	}
	public String getPrimerApellidoProfesionalContrato() {
		return primerApellidoProfesionalContrato;
	}
	public void setPrimerApellidoProfesionalContrato(
			String primerApellidoProfesionalContrato) {
		this.primerApellidoProfesionalContrato = primerApellidoProfesionalContrato;
	}
	public String getSegundoApellidoProfesionalContrato() {
		return segundoApellidoProfesionalContrato;
	}
	public void setSegundoApellidoProfesionalContrato(
			String segundoApellidoProfesionalContrato) {
		this.segundoApellidoProfesionalContrato = segundoApellidoProfesionalContrato;
	}
	public void setConsecutivoPresupuesto(Long consecutivoPresupuesto) {
		this.consecutivoPresupuesto = consecutivoPresupuesto;
	}
	public Long getConsecutivoPresupuesto() {
		return consecutivoPresupuesto;
	}
	public long getCodigoPkPresupuesto() {
		return codigoPkPresupuesto;
	}
	public void setCodigoPkPresupuesto(long codigoPkPresupuesto) {
		this.codigoPkPresupuesto = codigoPkPresupuesto;
	}
	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}
	public String getNumeroContrato() {
		return numeroContrato;
	}
	public void setFechaContrato(Date fechaContrato) {
		this.fechaContrato = fechaContrato;
	}
	public Date getFechaContrato() {
		return fechaContrato;
	}
	public void setAyudanteNumeroId(String ayudanteNumeroId) {
		this.ayudanteNumeroId = ayudanteNumeroId;
	}
	public String getAyudanteNumeroId() {
		this.ayudanteNumeroId = tipoId + " " + numeroId;
		return ayudanteNumeroId;
	}
	public void setValorPresupuesto(BigDecimal valorPresupuesto) {
		this.valorPresupuesto = valorPresupuesto;
	}
	public BigDecimal getValorPresupuesto() {
		return valorPresupuesto;
	}
	public void setValorDescuentoProm(BigDecimal valorDescuentoProm) {
		this.valorDescuentoProm = valorDescuentoProm;
	}
	public BigDecimal getValorDescuentoProm() {
		return valorDescuentoProm;
	}
	public void setAyudanteEstadoPresupuesto(String ayudanteEstadoPresupuesto) {
		this.ayudanteEstadoPresupuesto = ayudanteEstadoPresupuesto;
	}
	public String getAyudanteEstadoPresupuesto() {
		return ayudanteEstadoPresupuesto;
	}
	public void setTotalPorPromocion(BigDecimal totalPorPromocion) {
		this.totalPorPromocion = totalPorPromocion;
	}
	public BigDecimal getTotalPorPromocion() {
		return totalPorPromocion;
	}
	public String getAyudanteFechaIniVige() {
		return UtilidadFecha.conversionFormatoFechaAAp(fechaInicialvige);
	}
	public void setAyudanteFechaIniVige(String ayudanteFechaIniVige) {
		this.ayudanteFechaIniVige = ayudanteFechaIniVige;
	}
	public String getAyudanteFechaFinVige() {
		return UtilidadFecha.conversionFormatoFechaAAp(fechaFinalvige);
	}
	public void setAyudanteFechaFinVige(String ayudanteFechaFinVige) {
		this.ayudanteFechaFinVige = ayudanteFechaFinVige;
	}
	public String getAyudanteFechaContrato() {
		return UtilidadFecha.conversionFormatoFechaAAp(fechaContrato);
	}
	public void setAyudanteFechaContrato(String ayudanteFechaContrato) {
		this.ayudanteFechaContrato = ayudanteFechaContrato;
	}
	public void setValorPresupuestoFormato(String valorPresupuestoFormato) {
		this.valorPresupuestoFormato = valorPresupuestoFormato;
	}
	public String getValorPresupuestoFormato() {
		BigDecimal m= new BigDecimal(valorPresupuesto.doubleValue());
		NumberFormat formatter = new DecimalFormat("###,##0.00");
		String number=formatter.format(m);
		if(number.equals("0,00")){
			valorPresupuestoFormato="-";
		}
		else{
			valorPresupuestoFormato=number;
		}
		return valorPresupuestoFormato;
	}
	public void setValorDescuentoPromFormato(String valorDescuentoPromFormato) {
		this.valorDescuentoPromFormato = valorDescuentoPromFormato;
	}
	public String getValorDescuentoPromFormato() {
		BigDecimal a=valorDescuentoProm;
		BigDecimal m= new BigDecimal(a.doubleValue());
		NumberFormat formatter = new DecimalFormat("###,##0.00");
		String number=formatter.format(m);
		if(number.equals("0,00")){
			valorDescuentoPromFormato="-";
		}
		else{
			valorDescuentoPromFormato=number;
		}
		return valorDescuentoPromFormato;
	}
	public void setTotalPromociones(String totalPromociones) {
		this.totalPromociones = totalPromociones;
	}
	public String getTotalPromociones() {
		BigDecimal a=totalPorPromocion;
		BigDecimal m= new BigDecimal(a.doubleValue());
		NumberFormat formatter = new DecimalFormat("###,##0.00");
		String number=formatter.format(m);
		if(number.equals("0,00")){
			totalPromociones="-";
		}
		else{
			totalPromociones=number;
		}
		return totalPromociones;
	}
	public void setDescripcionInstCentroCont(String descripcionInstCentroCont) {
		this.descripcionInstCentroCont = descripcionInstCentroCont;
	}
	public String getDescripcionInstCentroCont() {
		this.descripcionInstCentroCont = "Institucion: "+ descripcionEmpresaInstitucion + " - " + "Centro de Atención que contrató: " + descCentroAtencionContrato + " - " +
		descripcionCiudad + " (" + descripcionPais + ") " + "- " + "Región: " + descripcionRegionCobertura; 
		return descripcionInstCentroCont;
	}
	public void setTotalPromo(BigDecimal totalPromo) {
		this.totalPromo = totalPromo;
	}
	public BigDecimal getTotalPromo() {
		return totalPromo;
	}
	public void setTotalPromocion(String totalPromocion) {
		this.totalPromocion = totalPromocion;
	}
	public String getTotalPromocion() {
		BigDecimal a=totalPromo;
		BigDecimal m= new BigDecimal(a.doubleValue());
		NumberFormat formatter = new DecimalFormat("###,##0.00");
		String number=formatter.format(m);
		if(number.equals("0,00")){
			totalPromocion="-";
		}
		else{
			totalPromocion=number;
		}
		return totalPromocion;
	}
}
