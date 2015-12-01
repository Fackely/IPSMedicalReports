package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import net.sf.jasperreports.engine.JRDataSource;

import util.ConstantesBD;
import util.InfoDatosStr;

public class DtoConsolidadoPresupuestoContratadoPorEstado implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private int totalPresupuestoPorEstado;
	private String razonSocial;
	private String fechaInicial;
	private String fechaFinal;
	private InfoDatosStr servicio;
	private String nombreUsuarioProceso;
	private String logoDerecha;
	private String logoIzquierda;
	private String descripcionEmpresaInstitucion;
	private String descripcionCentroAtencion;
	private String descripcionCiudad;
	private String descripcionPais;
	private String descripcionRegionCobertura;
	private int totalEstadoContratado;
	private int totalEstadoCancelado;
	private int totalEstadoSuspendido;
	private int totalEstadoTerminado;
	private ArrayList<DtoPresupuestosOdontologicosContratados> listadoConsolidadoPorEstado;
	private int totalPorEstado;
	private String ayudanteNombreServicio;
	private int consecutivoCentroAtencion;
	private String descCentroAtencionContrato;
	private String nombreInstitucion;
	private boolean parametroMultiEmp;
	
	//private String rutaEstilos;
	
	/***Plano***/
	private String descCentroAtencionDuenio;
	private Date fechaContrato;
	private long numeroContrato;
	private String tipoId;
	private String numeroId;
	private String primerApellido;
	private String segundoApellido;
	private String primerNombre;
	private String segundoNombre;
	private String primerNombreProfesionalContrato;
	private String primerApellidoProfesionalContrato;
	private String primerNombreProfesionalValoro;
	private String primerApellidoProfesionalValoro;
	private String codigoPrograma;
	private String codigoPaquete;
	private double valorPresupuesto;
	private String ayudanteEstadoPresupuesto;
	private long codigoPkPresupuesto;
	private String descPaisOriginalPlano;
	//private String estadoPresupuesto;
	
	/****Plano***/
	
	
	/** Objeto jasper para el subreporte del consolidado */
    transient private JRDataSource dsResultadoConsulta;
    
  
    /** Objeto jasper para el subreporte del consolidado por Estado */
    transient private JRDataSource dsListadoConsolidadoPorEstado;
    
    /** Objeto jasper para el reporte en plano */
  //  transient private JRDataSource dsListadoResultadoPlano;
    
	
	
	
	
	/**
	 * 
	 */
	public DtoConsolidadoPresupuestoContratadoPorEstado() {
		this.totalPresupuestoPorEstado=ConstantesBD.codigoNuncaValido;
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
		this.descripcionRegionCobertura="";
		this.setServicio(new InfoDatosStr());
		this.totalEstadoContratado=ConstantesBD.codigoNuncaValido;
		this.totalEstadoCancelado=ConstantesBD.codigoNuncaValido;
		this.totalEstadoSuspendido=ConstantesBD.codigoNuncaValido;
		this.totalEstadoTerminado=ConstantesBD.codigoNuncaValido;
		this.listadoConsolidadoPorEstado=new ArrayList<DtoPresupuestosOdontologicosContratados>();
		this.totalPorEstado=ConstantesBD.codigoNuncaValido;
		this.descCentroAtencionContrato="";
		this.nombreInstitucion="";
		this.parametroMultiEmp=false;
		//this.rutaEstilos="";
		
		/*****Plano *******/
		this.setDescCentroAtencionDuenio("");
		this.fechaContrato= null;
		this.numeroContrato=ConstantesBD.codigoNuncaValidoLong;
		this.tipoId="";
		this.numeroId="";
		this.primerApellido="";
		this.segundoApellido="";
		this.primerNombre="";
		this.segundoNombre="";
		this.primerNombreProfesionalContrato="";
		this.primerApellidoProfesionalContrato="";
		this.primerNombreProfesionalValoro="";
		this.primerApellidoProfesionalValoro="";
		this.codigoPrograma="";
		this.codigoPaquete="";
		this.valorPresupuesto=ConstantesBD.codigoNuncaValidoDouble;
		//this.setEstadoPresupuesto("");
		this.ayudanteEstadoPresupuesto="";
		this.codigoPkPresupuesto=ConstantesBD.codigoNuncaValidoLong;
		this.setDescPaisOriginalPlano("");
		/****Plano ********/
	}


	public int getTotalPresupuestoPorEstado() {
		return totalPresupuestoPorEstado;
	}


	public void setTotalPresupuestoPorEstado(int totalPresupuestoPorEstado) {
		this.totalPresupuestoPorEstado = totalPresupuestoPorEstado;
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


	public void setDsResultadoConsulta(JRDataSource dsResultadoConsulta) {
		this.dsResultadoConsulta = dsResultadoConsulta;
	}


	public JRDataSource getDsResultadoConsulta() {
		return dsResultadoConsulta;
	}


	public int getTotalEstadoContratado() {
		return totalEstadoContratado;
	}


	public void setTotalEstadoContratado(int totalEstadoContratado) {
		this.totalEstadoContratado = totalEstadoContratado;
	}


	public int getTotalEstadoCancelado() {
		return totalEstadoCancelado;
	}


	public void setTotalEstadoCancelado(int totalEstadoCancelado) {
		this.totalEstadoCancelado = totalEstadoCancelado;
	}


	public int getTotalEstadoSuspendido() {
		return totalEstadoSuspendido;
	}


	public void setTotalEstadoSuspendido(int totalEstadoSuspendido) {
		this.totalEstadoSuspendido = totalEstadoSuspendido;
	}


	public int getTotalEstadoTerminado() {
		return totalEstadoTerminado;
	}


	public void setTotalEstadoTerminado(int totalEstadoTerminado) {
		this.totalEstadoTerminado = totalEstadoTerminado;
	}

	public void setListadoConsolidadoPorEstado(
			ArrayList<DtoPresupuestosOdontologicosContratados> listadoConsolidadoPorEstado) {
		this.listadoConsolidadoPorEstado = listadoConsolidadoPorEstado;
	}


	public ArrayList<DtoPresupuestosOdontologicosContratados> getListadoConsolidadoPorEstado() {
		return listadoConsolidadoPorEstado;
	}


	public void setDsListadoConsolidadoPorEstado(
			JRDataSource dsListadoConsolidadoPorEstado) {
		this.dsListadoConsolidadoPorEstado = dsListadoConsolidadoPorEstado;
	}


	public JRDataSource getDsListadoConsolidadoPorEstado() {
		return dsListadoConsolidadoPorEstado;
	}


	public void setTotalPorEstado(int totalPorEstado) {
		this.totalPorEstado = totalPorEstado;
	}


	public int getTotalPorEstado() {
		return totalPorEstado;
	}


	public void setServicio(InfoDatosStr servicio) {
		this.servicio = servicio;
	}


	public InfoDatosStr getServicio() {
		return servicio;
	}


	public String getAyudanteNombreServicio() {
		return ayudanteNombreServicio;
	}


	public void setAyudanteNombreServicio(String ayudanteNombreServicio) {
		this.ayudanteNombreServicio = ayudanteNombreServicio;
	}


	public void setConsecutivoCentroAtencion(int consecutivoCentroAtencion) {
		this.consecutivoCentroAtencion = consecutivoCentroAtencion;
	}


	public int getConsecutivoCentroAtencion() {
		return consecutivoCentroAtencion;
	}


	public void setDescCentroAtencionContrato(String descCentroAtencionContrato) {
		this.descCentroAtencionContrato = descCentroAtencionContrato;
	}


	public String getDescCentroAtencionContrato() {
		return descCentroAtencionContrato;
	}


	public String getDescCentroAtencionDuenio() {
		return descCentroAtencionDuenio;
	}


	public void setDescCentroAtencionDuenio(String descCentroAtencionDuenio) {
		this.descCentroAtencionDuenio = descCentroAtencionDuenio;
	}


	public Date getFechaContrato() {
		return fechaContrato;
	}


	public void setFechaContrato(Date fechaContrato) {
		this.fechaContrato = fechaContrato;
	}


	public long getNumeroContrato() {
		return numeroContrato;
	}


	public void setNumeroContrato(long numeroContrato) {
		this.numeroContrato = numeroContrato;
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


	public String getPrimerNombreProfesionalContrato() {
		return primerNombreProfesionalContrato;
	}


	public void setPrimerNombreProfesionalContrato(
			String primerNombreProfesionalContrato) {
		this.primerNombreProfesionalContrato = primerNombreProfesionalContrato;
	}


	public String getPrimerApellidoProfesionalContrato() {
		return primerApellidoProfesionalContrato;
	}


	public void setPrimerApellidoProfesionalContrato(
			String primerApellidoProfesionalContrato) {
		this.primerApellidoProfesionalContrato = primerApellidoProfesionalContrato;
	}


	public String getPrimerNombreProfesionalValoro() {
		return primerNombreProfesionalValoro;
	}


	public void setPrimerNombreProfesionalValoro(
			String primerNombreProfesionalValoro) {
		this.primerNombreProfesionalValoro = primerNombreProfesionalValoro;
	}


	public String getPrimerApellidoProfesionalValoro() {
		return primerApellidoProfesionalValoro;
	}


	public void setPrimerApellidoProfesionalValoro(
			String primerApellidoProfesionalValoro) {
		this.primerApellidoProfesionalValoro = primerApellidoProfesionalValoro;
	}


	public String getCodigoPrograma() {
		return codigoPrograma;
	}


	public void setCodigoPrograma(String codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}


	public String getCodigoPaquete() {
		return codigoPaquete;
	}


	public void setCodigoPaquete(String codigoPaquete) {
		this.codigoPaquete = codigoPaquete;
	}


	public double getValorPresupuesto() {
		return valorPresupuesto;
	}


	public void setValorPresupuesto(double valorPresupuesto) {
		this.valorPresupuesto = valorPresupuesto;
	}


	/*public void setEstadoPresupuesto(String estadoPresupuesto) {
		this.estadoPresupuesto = estadoPresupuesto;
	}


	public String getEstadoPresupuesto() {
		return estadoPresupuesto;
	}*/


	public String getAyudanteEstadoPresupuesto() {
		return ayudanteEstadoPresupuesto;
	}


	public void setAyudanteEstadoPresupuesto(String ayudanteEstadoPresupuesto) {
		this.ayudanteEstadoPresupuesto = ayudanteEstadoPresupuesto;
	}


	/*public void setRutaEstilos(String rutaEstilos) {
		this.rutaEstilos = rutaEstilos;
	}


	public String getRutaEstilos() {
		return rutaEstilos;
	}*/


	public void setCodigoPkPresupuesto(long codigoPkPresupuesto) {
		this.codigoPkPresupuesto = codigoPkPresupuesto;
	}


	public long getCodigoPkPresupuesto() {
		return codigoPkPresupuesto;
	}


	public void setNombreInstitucion(String nombreInstitucion) {
		this.nombreInstitucion = nombreInstitucion;
	}


	public String getNombreInstitucion() {
		return nombreInstitucion;
	}


	public void setParametroMultiEmp(boolean parametroMultiEmp) {
		this.parametroMultiEmp = parametroMultiEmp;
	}


	public boolean isParametroMultiEmp() {
		return parametroMultiEmp;
	}


	public void setDescPaisOriginalPlano(String descPaisOriginalPlano) {
		this.descPaisOriginalPlano = descPaisOriginalPlano;
	}


	public String getDescPaisOriginalPlano() {
		return descPaisOriginalPlano;
	}




	/*public void setDsListadoResultadoPlano(JRDataSource dsListadoResultadoPlano) {
		this.dsListadoResultadoPlano = dsListadoResultadoPlano;
	}


	public JRDataSource getDsListadoResultadoPlano() {
		return dsListadoResultadoPlano;
	}*/


}
