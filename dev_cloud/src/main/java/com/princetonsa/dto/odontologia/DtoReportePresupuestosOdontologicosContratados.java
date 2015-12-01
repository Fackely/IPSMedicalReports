package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.Date;

import util.ConstantesBD;
import util.InfoDatosStr;
import util.UtilidadFecha;
import util.UtilidadTexto;

public class DtoReportePresupuestosOdontologicosContratados implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo que almacena la fecha inicial desde la cual se
	 * realiza la b&uacute;squeda 
	 */
	
	private Date fechaInicial;
	
	/**
	 * Atributo que almacena la fecha final hasta la cual se
	 * realiza la b&uacute;squeda 
	 */
	
	private Date fechaFinal;
	
	/**
	 * Atributo que almacena el c&oacute;digo de un pa&iacute;s de residencia.
	 */
	private String codigoPaisResidencia;
	
	/**
	 * Atributo que almacena el c&oacute;digo que identifica a 
	 * una determinada ciudad.
	 */
	
	private String ciudadDeptoPais;
	
	/**
	 * Atributo que almacena el c&oacute;digo de la regi&oacute;n de cobertura.
	 */
	private long codigoRegion;
	
	/**
	 * Atributo que almacena el c&oacute;digo de la empresa-institucion.
	 */
	private long codigoEmpresaInstitucion;
	
	/**
	 * Atributo que almacena el primer valor de la llave
	 * compuesta que identifica una ciudad.
	 */
	private String codigoCiudad;
	
	/**
	 * Atributo que almacena el segundo valor de la llave
	 * compuesta que identifica una ciudad.
	 */
	private String codigoPais;
	
	/**
	 * Atributo que almacena el tercer valor de la llave
	 * compuesta que identifica una ciudad.
	 */
	private String codigoDpto;
	
	/**
	 * Atributo que almacena el c&oacute;digo del centro de 
	 * atenci&oacute;n.
	 */
	private Integer consecutivoCentroAtencion;


	/**
	 * Atributo que almacena el indicativo de contrato
	 * de un presupuesto en estado contratado.
	 */
	private String indicativoContrato;
	
	
	/**
	 * Atributo que almacena el nombre del indicativo 
	 * de contrato.
	 */
	private String nombreIndicativo;
	
	/**
	 * Atributo que almacena el codigo del profesional
	 * que contrat&oacute;.
	 */
	private Integer codigoProfesionalContrato;
	
	/**
	 * Atributo que almacena el codigo del profesional
	 * que valor&oacute;..
	 */
	private Integer codigoProfesionalValoro;
	
	/**
	 * Atributo que almacena el valor del contrato a partir del cual se buscan
	 * los registros de presupuestos
	 */
	private Double valorContratoInicial;
	
	/**
	 * Atributo que almacena el valor del contrato hasta el cual se buscan
	 * los registros de presupuestos contratados
	 */
	
	private Double valorContratoFinal;
	
	
	/**
	 * Atributo que almacena el c&oacute;digo del paquete
	 * odontol&oacute;gico.
	 */
	private int codigoPaqueteOdonto;
	
	/**
	 * Atributo que almacena el c&oacute;digo del programa seleccionado.
	 */
	private long codigoPrograma;
	
	
	/**
	 * Atributo que permite obtener el servicio por medio de
	 *  la b&uacute;squeda gen&eacute;rica. 
	 */
	private InfoDatosStr servicio;

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo fechaInicial
	 * @param valor para el atributo fechaInicial
	 */
	
	/**
	 * Atributo que almacena la raz&oacute;n social
	 * de la instituci&oacute;n.
	 */
	private String razonSocial;
	
	
	/**
	 * Atributo que almacena el login del profesional que 
	 * valor&oacute;
	 */
	private String login;
	
	/**
	 * Atributo que almacena el login del profesional que
	 * contrat&oacute;
	 */
	private String loginProfesionalContrato;
	
	
	/**
	 * Atributo que almacena el nombre del paquete seleccionado.
	 */
	private String nombrePaquete;
	
	/**
	 * Atributo que almacena la ruta del logo a mostrar en el reporte.
	 */
	private String rutaLogo;
	
	private String ubicacionLogo;
	
	private String fechaInicialFormateada;
	private String fechaFinalFormateada;
	private String nombreUsuarioProceso;
	
	private String ayudanteServicio;
	private String rutaEstilos;
	
	private boolean esMultiempresa;
	private long codigoInstitucion;
	
	private int totalEstadoContratado;
	

	/**
	 * Constructor del DtoReportePresupuestosOdontologicosContratados
	 */
	
	public DtoReportePresupuestosOdontologicosContratados() {
		this.fechaInicial= null;
		this.fechaFinal=null; 
		this.codigoPaisResidencia="";
		this.ciudadDeptoPais="";
		this.codigoRegion=ConstantesBD.codigoNuncaValidoLong;
		this.codigoEmpresaInstitucion=ConstantesBD.codigoNuncaValidoLong;
		this.codigoCiudad="";
		this.codigoPais="";
		this.codigoDpto="";
		this.consecutivoCentroAtencion=ConstantesBD.codigoNuncaValido;
		this.indicativoContrato="";
		this.nombreIndicativo="";
		this.codigoProfesionalContrato=ConstantesBD.codigoNuncaValido;
		this.codigoProfesionalValoro=ConstantesBD.codigoNuncaValido;
		this.valorContratoInicial=ConstantesBD.codigoNuncaValidoDouble;
		this.valorContratoFinal=ConstantesBD.codigoNuncaValidoDouble;
		this.codigoPaqueteOdonto=ConstantesBD.codigoNuncaValido;
		this.codigoPrograma=ConstantesBD.codigoNuncaValidoLong;
		this.setServicio(new InfoDatosStr());
		this.razonSocial="";
		this.login="";
		this.loginProfesionalContrato="";
		this.nombrePaquete="";
		this.rutaLogo = "";
		this.ubicacionLogo = "";
		this.fechaInicialFormateada="";
		this.fechaFinalFormateada="";
		this.nombreUsuarioProceso="";
		this.setAyudanteServicio("");
		this.setRutaEstilos("");
		this.codigoInstitucion=ConstantesBD.codigoNuncaValidoLong;
		this.setTotalEstadoContratado(ConstantesBD.codigoNuncaValido);
		
	}
	
	
	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo fechaInicial
	 * 
	 * @return  Retorna la variable fechaInicial
	 */

	public Date getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo fechaFinal
	 * 
	 * @return  Retorna la variable fechaInicial
	 */
	
	public Date getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo fechaFinal
	 * @param valor para el atributo fechaFinal
	 */
	
	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor
	 * del atributo codigoPaisResidencia
	 * @return codigoPaisResidencia
	 */
	
	public String getCodigoPaisResidencia() {
		return codigoPaisResidencia;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo codigoPaisResidencia
	 * @param codigoPaisResidencia
	 */
	
	public void setCodigoPaisResidencia(String codigoPaisResidencia) {
		this.codigoPaisResidencia = codigoPaisResidencia;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 * del atributo ciudadDeptoPais
	 * @return ciudadDeptoPais
	 */
	
	public String getCiudadDeptoPais() {
		return ciudadDeptoPais;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo ciudadDeptoPais
	 * @param ciudadDeptoPais
	 */

	public void setCiudadDeptoPais(String ciudadDeptoPais) {
		this.ciudadDeptoPais = ciudadDeptoPais;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 * del atributo codigoRegion
	 * @return codigoRegion
	 */
	
	public long getCodigoRegion() {
		return codigoRegion;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo codigoRegion
	 * @param codigoRegion
	 */

	public void setCodigoRegion(long codigoRegion) {
		this.codigoRegion = codigoRegion;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 * del atributo codigoEmpresaInstitucion
	 * @return codigoEmpresaInstitucion
	 */

	public long getCodigoEmpresaInstitucion() {
		return codigoEmpresaInstitucion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo codigoEmpresaInstitucion
	 * @param codigoEmpresaInstitucion
	 */
	
	public void setCodigoEmpresaInstitucion(long codigoEmpresaInstitucion) {
		this.codigoEmpresaInstitucion = codigoEmpresaInstitucion;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 * del atributo codigoCiudad
	 * @return codigoCiudad
	 */

	public String getCodigoCiudad() {
		return codigoCiudad;
	}

	/**
	 *  M&eacute;todo que se encarga de establecer el valor
	 *  del atributo codigoCiudad
	 * @param codigoCiudad
	 */
	
	public void setCodigoCiudad(String codigoCiudad) {
		this.codigoCiudad = codigoCiudad;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor
	 * del atributo codigoPais
	 * @return codigoPais
	 */
	
	public String getCodigoPais() {
		return codigoPais;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo codigoPais
	 * @return codigoPais
	 */

	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 * del atributo codigoDpto
	 * @return codigoDpto
	 */
	
	public String getCodigoDpto() {
		return codigoDpto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo codigoDpto
	 * @param codigoDpto
	 */
	
	public void setCodigoDpto(String codigoDpto) {
		this.codigoDpto = codigoDpto;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 * del atributo consecutivoCentroAtencion
	 * @return consecutivoCentroAtencion
	 */

	public Integer getConsecutivoCentroAtencion() {
		return consecutivoCentroAtencion;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo consecutivoCentroAtencion 
	 * @param consecutivoCentroAtencion
	 */

	public void setConsecutivoCentroAtencion(Integer consecutivoCentroAtencion) {
		this.consecutivoCentroAtencion = consecutivoCentroAtencion;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 * del atributo indicativoContrato
	 * @return indicativoContrato
	 */
	
	public String getIndicativoContrato() {
		return indicativoContrato;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo indicativoContrato
	 * @param indicativoContrato
	 */
	
	public void setIndicativoContrato(String indicativoContrato) {
		this.indicativoContrato = indicativoContrato;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 * del atributo codigoProfesionalContrato
	 * @return codigoProfesionalContrato
	 */
	
	public Integer getCodigoProfesionalContrato() {
		return codigoProfesionalContrato;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo codigoProfesionalContrato
	 * @param codigoProfesionalContrato
	 */
	
	public void setCodigoProfesionalContrato(Integer codigoProfesionalContrato) {
		this.codigoProfesionalContrato = codigoProfesionalContrato;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 * del atributo codigoProfesionalValoro
	 * @return codigoProfesionalValoro
	 */
	
	public Integer getCodigoProfesionalValoro() {
		return codigoProfesionalValoro;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo codigoProfesionalValoro
	 * @param codigoProfesionalValoro
	 */
	
	public void setCodigoProfesionalValoro(Integer codigoProfesionalValoro) {
		this.codigoProfesionalValoro = codigoProfesionalValoro;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 * del atributo valorContratoInicial
	 * @return valorContratoInicial
	 */
	
	public Double getValorContratoInicial() {
		return valorContratoInicial;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo valorContratoInicial
	 * @param valorContratoInicial
	 */
	
	public void setValorContratoInicial(Double valorContratoInicial) {
		this.valorContratoInicial = valorContratoInicial;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 * del atributo valorContratoFinal
	 * @return valorContratoFinal
	 */

	public Double getValorContratoFinal() {
		return valorContratoFinal;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo valorContratoFinal
	 * @param valorContratoFinal
	 */
	
	public void setValorContratoFinal(Double valorContratoFinal) {
		this.valorContratoFinal = valorContratoFinal;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 * del atributo codigoPaqueteOdonto
	 * @return codigoPaqueteOdonto
	 */
	
	public int getCodigoPaqueteOdonto() {
		return codigoPaqueteOdonto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo codigoPaqueteOdonto
	 * @param codigoPaqueteOdonto
	 */
	
	public void setCodigoPaqueteOdonto(int codigoPaqueteOdonto) {
		this.codigoPaqueteOdonto = codigoPaqueteOdonto;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor
	 * del atributo codigoPrograma
	 * @return codigoPrograma
	 */
	
	public long getCodigoPrograma() {
		return codigoPrograma;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo codigoPrograma
	 * @param codigoPrograma
	 */
	
	public void setCodigoPrograma(long codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor
	 * del atributo servicio
	 * 
	 * @return servicio
	 */

	public InfoDatosStr getServicio() {
		return servicio;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo servicio
	 * @param servicio
	 */

	public void setServicio(InfoDatosStr servicio) {
		this.servicio = servicio;
	}
	

	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo razonSocial
	 * @param razonSocial
	 */
	
	
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor
	 * del atributo razonSocial
	 * @return razonSocial
	 */
	
	
	public String getRazonSocial() {
		return razonSocial;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo nombreIndicativo
	 * 
	 * @param  valor para el atributo nombreIndicativo 
	 */

	public void setNombreIndicativo(String nombreIndicativo) {
		this.nombreIndicativo = nombreIndicativo;
	}

	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombreIndicativo
	 * 
	 * @return  Retorna la variable nombreIndicativo
	 */
	
	public String getNombreIndicativo() {
		
		if (UtilidadTexto.isEmpty(nombreIndicativo) || 
				nombreIndicativo.trim().equals("-1") ) {
			
			nombreIndicativo = "Todos";
		}
		
		return nombreIndicativo;
	}

	
	/**
	 *  M&eacute;todo que se encarga de establecer el valor 
	 * del atributo login
	 * @param login
	 */
	
	public void setLogin(String login) {
		this.login = login;
	}
 
	/**
	 * 
	 * M&eacute;todo que se encarga de obtener el valor 
	 * del atributo login
	 * @return login
	 */
	
	public String getLogin() {
		return login;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor
	 * del atributo nombrePaquete
	 * @param nombrePaquete
	 */
	
	public void setNombrePaquete(String nombrePaquete) {
		this.nombrePaquete = nombrePaquete;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor
	 * del atributo nombrePaquete
	 * @return nombrePaquete
	 */
	
	public String getNombrePaquete() {
		return nombrePaquete;
	}




	public void setLoginProfesionalContrato(String loginProfesionalContrato) {
		this.loginProfesionalContrato = loginProfesionalContrato;
	}




	public String getLoginProfesionalContrato() {
		return loginProfesionalContrato;
	}


	public String getRutaLogo() {
		return rutaLogo;
	}


	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
	}


	public String getUbicacionLogo() {
		return ubicacionLogo;
	}


	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
	}


	public String getFechaInicialFormateada() {
		
		fechaInicialFormateada = UtilidadFecha.conversionFormatoFechaAAp(fechaInicial);
		
		return fechaInicialFormateada;
	}


	public void setFechaInicialFormateada(String fechaInicialFormateada) {
		this.fechaInicialFormateada = fechaInicialFormateada;
	}


	public String getFechaFinalFormateada() {
		
		fechaFinalFormateada = UtilidadFecha.conversionFormatoFechaAAp(fechaFinal);
		
		return fechaFinalFormateada;
	}


	public void setFechaFinalFormateada(String fechaFinalFormateada) {
		this.fechaFinalFormateada = fechaFinalFormateada;
	}


	public String getNombreUsuarioProceso() {
		return nombreUsuarioProceso;
	}


	public void setNombreUsuarioProceso(String nombreUsuarioProceso) {
		this.nombreUsuarioProceso = nombreUsuarioProceso;
	}


	public void setAyudanteServicio(String ayudanteServicio) {
		this.ayudanteServicio = ayudanteServicio;
	}


	public String getAyudanteServicio() {
		
		if(UtilidadTexto.isEmpty(servicio.getNombre())||servicio.getNombre().equals(ConstantesBD.codigoNuncaValido+"")){
			this.ayudanteServicio = "Todos";
		}else
			this.ayudanteServicio = servicio.getNombre();
			
		
		return ayudanteServicio;
	}


	public void setRutaEstilos(String rutaEstilos) {
		this.rutaEstilos = rutaEstilos;
	}


	public String getRutaEstilos() {
		return rutaEstilos;
	}


	public void setEsMultiempresa(boolean esMultiempresa) {
		this.esMultiempresa = esMultiempresa;
	}


	public boolean isEsMultiempresa() {
		return esMultiempresa;
	}


	public void setCodigoInstitucion(long codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}


	public long getCodigoInstitucion() {
		return codigoInstitucion;
	}


	public void setTotalEstadoContratado(int totalEstadoContratado) {
		this.totalEstadoContratado = totalEstadoContratado;
	}


	public int getTotalEstadoContratado() {
		return totalEstadoContratado;
	}



}
