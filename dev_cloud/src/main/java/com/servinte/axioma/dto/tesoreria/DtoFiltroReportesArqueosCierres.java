package com.servinte.axioma.dto.tesoreria;

import java.io.Serializable;

/**
 * Clase que almacena la información necesaria para generar los encabezados de los reportes:
 * Arqueo Caja Resumido y Detallado DCU 1127
 * Cierre Turno Caja DCU 1038
 * Entrega Caja Mayor DCU 1039
 * Impresión Traslado Entre Cajas DCU 1024
 * Entrega Transportadora DCU 1025
 *  
 * @author Fabián Becerra
 *
 */
public class DtoFiltroReportesArqueosCierres implements Serializable{

	/** * */
	private static final long serialVersionUID = 1L;
	
	/***************ATRIBUTOS UTILIZADOS EN LOS TITULOS DEL REPORTE*************/	
	/**
	 * Atributo que almacena la razon social de la institución
	 */
	private String razonSocial;
	
	/**
	 * Atributo que almacena el nit de la institución
	 */
	private String nit;
	
	/**
	 * Atributo que almacena la ubicacion del logo en los reportes
	 */
	private String ubicacionLogo;
	
	/**
	 * Atributo que almacena la ruta del logo de los reportes
	 */
	private String rutaLogo;
	
	/**
	 * Atributo que almacena la actividad economica de la institución
	 */
	private String actividadEconomica;
	
	/**
	 * Atributo que almacena la dirección de la institución
	 */
	private String direccion;
	
	/**
	 * Atributo que almacena el teléfono de la institución
	 */
	private String telefono;
	
	/**
	 * Atributo que almacena la descripción del centro de Atención
	 */
	private String centroAtencion;
	
	/**
	 * Atributo que almacena el nombre del usuario 
	 */
	private String nombreUsuario;
	
	/***************ATRIBUTOS UTILIZADOS EN LOS ENCABEZADOS DEL REPORTE*************/
	
	/**
	 * Atributo que almacena el consecutivo del arqueo
	 */
	private String nroConsecutivo;
	
	/**
	 * Atributo que almacena la fecha de generación del arqueo
	 */
	private String fechaGeneracion;
	
	/**
	 * Atributo que almacena la hora de generación del arqueo
	 */
	private String horaGeneracion;
	
	/**
	 * Atributo que almacena el usuario que generó el arqueo 
	 */
	private String usuarioGeneracion;
	
	/**
	 * Atributo que almacena la fecha seleccionada del arqueo
	 */
	private String fechaArqueo;
	
	/**
	 * Atributo que almacena la fecha seleccionada del cierre 
	 */
	private String fechaCierre;
	
	/**
	 * Atributo que almacena el cajero 
	 */
	private String cajero;
	
	/**
	 * Atributo que almacena la caja para la cual se está generando el arqueo
	 */
	private String caja;
	
	/**
	 * Atributo que almacena la caja mayor principal
	 */
	private String cajaMayorPrincipal;
	
	/**
	 * Atributo que almacena el estado de la solicitud traslado caja recaudo
	 */
	private String estadoSolicitud;
	
	/**
	 * Atributo que almacena la caja del traslado
	 */
	private String cajaTraslada;
	
	/**
	 * Atributo que almacena el cajero del traslado
	 */
	private String cajeroTraslada;
	
	/**
	 * Atributo que almacena la fecha de aceptación de la solicitud del traslado 
	 */
	private String fechaAceptacion;
	
	/**
	 * Atributo que almacena la hora de aceptación de la solicitud del traslado 
	 */
	private String horaAceptacion;
	
	/**
	 * Atributo que almacena la caja de aceptación de la solicitud del traslado
	 */
	private String cajaAcepta;
	
	/**
	 * Atributo que almacena el cajero que acepta la solicitud del traslado
	 */
	private String cajeroAcepta;
	
	/**
	 * Atributo que almacena la razón social de la transportadora responsable de la entrega transportadora
	 */
	private String transportadora;
	
	/**
	 * Atributo que almacena el nit de la transportadora responsable de la entrega transportadora
	 */
	private String nitTransportadora;
	
	/**
	 * Atributo que almacena los nombres del responsable de la entrega transportadora
	 */
	private String responsableRecibe;
	
	/**
	 * Atributo que almacena el número de carné del responsable de la entrega transportadora
	 */
	private String numCarnet;
	
	/**
	 * Atributo que almacena el número de guia del responsable de la entrega transportadora
	 */
	private String numGuia;
	
	/**
	 * Atributo que almacena el número de carro registrado en la entrega transportadora
	 */
	private String numCarro;
	
	/**
	 * Atributo que almacena el número de carro registrado en la entrega transportadora
	 */
	private String entidadFinanciera;
	
	/**
	 * Atributo que almacena el tipo y numero de cuenta registrado en la entrega transportadora
	 */
	private String tipoNroCuentaBancaria;
	
	/**
	 * Atributo que indica si el arqueo caja ya ha sido almacenado para mostrar o no el consecutivo de arqueo
	 */
	private boolean exito;
	
	/**
	 * Atributo que indica si el reporte es llamado desde la consulta 
	 */
	private boolean esConsulta;
	
	
	/**
	 * Método que inicializa los atributos de la clase 
	 */
	public void reset()
	{
		this.razonSocial	=		"";
		this.nit			=		"";
		this.ubicacionLogo	=		"";
		this.rutaLogo		=		"";
		this.actividadEconomica = 	"";
		this.direccion		=		"";
		this.telefono		=		"";
		this.centroAtencion	=		"";
		this.nombreUsuario	=		"";
		this.nroConsecutivo =		"";
		this.fechaGeneracion=		"";
		this.horaGeneracion =		"";
		this.usuarioGeneracion=		"";
		this.fechaArqueo	=		"";
		this.fechaCierre	=		"";
		this.cajero			=		"";
		this.caja			=		"";
		this.cajaMayorPrincipal=	"";
		this.exito			=       false;
		this.estadoSolicitud=		"";
		this.cajaTraslada	=		"";
		this.cajeroTraslada =		"";
		this.fechaAceptacion=		"";
		this.horaAceptacion =		"";
		this.cajaAcepta		= 		"";
		this.cajeroAcepta	= 		"";
		this.transportadora =		"";
		this.nitTransportadora=		"";
		this.responsableRecibe=		"";
		this.numCarnet		=		"";
		this.numGuia		=		"";
		this.numCarro		=		"";
		this.entidadFinanciera=		"";
		this.tipoNroCuentaBancaria=	"";
		this.esConsulta		= 		false;
	}


	/**
	 * Método que devuelve el valor del atributo razonSocial
	 * @return razonSocial
	 */
	public String getRazonSocial() {
		return razonSocial;
	}


	/**
	 * Método que almacena el valor del atributo razonSocial
	 * @param razonSocial
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}


	/**
	 * Método que devuelve el valor del atributo nit
	 * @return nit
	 */
	public String getNit() {
		return nit;
	}


	/**
	 * Método que almacena el valor del atributo nit
	 * @param nit
	 */
	public void setNit(String nit) {
		this.nit = nit;
	}


	/**
	 * Método que devuelve el valor del atributo ubicacionLogo
	 * @return ubicacionLogo
	 */
	public String getUbicacionLogo() {
		return ubicacionLogo;
	}


	/**
	 * Método que almacena el valor del atributo ubicacionLogo
	 * @param ubicacionLogo
	 */
	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
	}


	/**
	 * Método que devuelve el valor del atributo rutaLogo
	 * @return rutaLogo
	 */
	public String getRutaLogo() {
		return rutaLogo;
	}


	/**
	 * Método que almacena el valor del atributo rutaLogo
	 * @param rutaLogo
	 */
	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
	}


	/**
	 * Método que devuelve el valor del atributo actividadEconomica
	 * @return actividadEconomica
	 */
	public String getActividadEconomica() {
		return actividadEconomica;
	}


	/**
	 * Método que almacena el valor del atributo actividadEconomica
	 * @param actividadEconomica
	 */
	public void setActividadEconomica(String actividadEconomica) {
		this.actividadEconomica = actividadEconomica;
	}


	/**
	 * Método que devuelve el valor del atributo direccion
	 * @return direccion
	 */
	public String getDireccion() {
		return direccion;
	}


	/**
	 * Método que almacena el valor del atributo direccion
	 * @param direccion
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}


	/**
	 * Método que devuelve el valor del atributo telefono
	 * @return telefono
	 */
	public String getTelefono() {
		return telefono;
	}


	/**
	 * Método que almacena el valor del atributo telefono
	 * @param telefono
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}


	/**
	 * Método que devuelve el valor del atributo centroAtencion
	 * @return centroAtencion
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}


	/**
	 * Método que almacena el valor del atributo centroAtencion
	 * @param centroAtencion
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}


	/**
	 * Método que devuelve el valor del atributo nombreUsuario
	 * @return nombreUsuario
	 */
	public String getNombreUsuario() {
		return nombreUsuario;
	}


	/**
	 * Método que almacena el valor del atributo nombreUsuario
	 * @param nombreUsuario
	 */
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}


	/**
	 * Método que devuelve el valor del atributo nroConsecutivo
	 * @return nroConsecutivo
	 */
	public String getNroConsecutivo() {
		return nroConsecutivo;
	}


	/**
	 * Método que almacena el valor del atributo nroConsecutivo
	 * @param nroConsecutivo
	 */
	public void setNroConsecutivo(String nroConsecutivo) {
		this.nroConsecutivo = nroConsecutivo;
	}


	/**
	 * Método que devuelve el valor del atributo fechaGeneracion
	 * @return fechaGeneracion
	 */
	public String getFechaGeneracion() {
		return fechaGeneracion;
	}


	/**
	 * Método que almacena el valor del atributo fechaGeneracion
	 * @param fechaGeneracion
	 */
	public void setFechaGeneracion(String fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}


	/**
	 * Método que devuelve el valor del atributo horaGeneracion
	 * @return horaGeneracion
	 */
	public String getHoraGeneracion() {
		return horaGeneracion;
	}


	/**
	 * Método que almacena el valor del atributo horaGeneracion
	 * @param horaGeneracion
	 */
	public void setHoraGeneracion(String horaGeneracion) {
		this.horaGeneracion = horaGeneracion;
	}


	/**
	 * Método que devuelve el valor del atributo usuarioGeneracion
	 * @return usuarioGeneracion
	 */
	public String getUsuarioGeneracion() {
		return usuarioGeneracion;
	}


	/**
	 * Método que almacena el valor del atributo usuarioGeneracion
	 * @param usuarioGeneracion
	 */
	public void setUsuarioGeneracion(String usuarioGeneracion) {
		this.usuarioGeneracion = usuarioGeneracion;
	}


	/**
	 * Método que devuelve el valor del atributo fechaArqueo
	 * @return fechaArqueo
	 */
	public String getFechaArqueo() {
		return fechaArqueo;
	}


	/**
	 * Método que almacena el valor del atributo fechaArqueo
	 * @param fechaArqueo
	 */
	public void setFechaArqueo(String fechaArqueo) {
		this.fechaArqueo = fechaArqueo;
	}


	/**
	 * Método que devuelve el valor del atributo fechaCierre
	 * @return fechaCierre
	 */
	public String getFechaCierre() {
		return fechaCierre;
	}


	/**
	 * Método que almacena el valor del atributo fechaCierre
	 * @param fechaCierre
	 */
	public void setFechaCierre(String fechaCierre) {
		this.fechaCierre = fechaCierre;
	}


	/**
	 * Método que devuelve el valor del atributo cajero
	 * @return cajero
	 */
	public String getCajero() {
		return cajero;
	}


	/**
	 * Método que almacena el valor del atributo cajero
	 * @param cajero
	 */
	public void setCajero(String cajero) {
		this.cajero = cajero;
	}


	/**
	 * Método que devuelve el valor del atributo caja
	 * @return caja
	 */
	public String getCaja() {
		return caja;
	}


	/**
	 * Método que almacena el valor del atributo caja
	 * @param caja
	 */
	public void setCaja(String caja) {
		this.caja = caja;
	}


	/**
	 * Método que devuelve el valor del atributo cajaMayorPrincipal
	 * @return cajaMayorPrincipal
	 */
	public String getCajaMayorPrincipal() {
		return cajaMayorPrincipal;
	}


	/**
	 * Método que almacena el valor del atributo cajaMayorPrincipal
	 * @param cajaMayorPrincipal
	 */
	public void setCajaMayorPrincipal(String cajaMayorPrincipal) {
		this.cajaMayorPrincipal = cajaMayorPrincipal;
	}


	/**
	 * Método que devuelve el valor del atributo estadoSolicitud
	 * @return estadoSolicitud
	 */
	public String getEstadoSolicitud() {
		return estadoSolicitud;
	}


	/**
	 * Método que almacena el valor del atributo estadoSolicitud
	 * @param estadoSolicitud
	 */
	public void setEstadoSolicitud(String estadoSolicitud) {
		this.estadoSolicitud = estadoSolicitud;
	}


	/**
	 * Método que devuelve el valor del atributo cajaTraslada
	 * @return cajaTraslada
	 */
	public String getCajaTraslada() {
		return cajaTraslada;
	}


	/**
	 * Método que almacena el valor del atributo cajaTraslada
	 * @param cajaTraslada
	 */
	public void setCajaTraslada(String cajaTraslada) {
		this.cajaTraslada = cajaTraslada;
	}


	/**
	 * Método que devuelve el valor del atributo cajeroTraslada
	 * @return cajeroTraslada
	 */
	public String getCajeroTraslada() {
		return cajeroTraslada;
	}


	/**
	 * Método que almacena el valor del atributo cajeroTraslada
	 * @param cajeroTraslada
	 */
	public void setCajeroTraslada(String cajeroTraslada) {
		this.cajeroTraslada = cajeroTraslada;
	}


	/**
	 * Método que devuelve el valor del atributo fechaAceptacion
	 * @return fechaAceptacion
	 */
	public String getFechaAceptacion() {
		return fechaAceptacion;
	}


	/**
	 * Método que almacena el valor del atributo fechaAceptacion
	 * @param fechaAceptacion
	 */
	public void setFechaAceptacion(String fechaAceptacion) {
		this.fechaAceptacion = fechaAceptacion;
	}


	/**
	 * Método que devuelve el valor del atributo horaAceptacion
	 * @return horaAceptacion
	 */
	public String getHoraAceptacion() {
		return horaAceptacion;
	}


	/**
	 * Método que almacena el valor del atributo horaAceptacion
	 * @param horaAceptacion
	 */
	public void setHoraAceptacion(String horaAceptacion) {
		this.horaAceptacion = horaAceptacion;
	}


	/**
	 * Método que devuelve el valor del atributo cajaAcepta
	 * @return cajaAcepta
	 */
	public String getCajaAcepta() {
		return cajaAcepta;
	}


	/**
	 * Método que almacena el valor del atributo cajaAcepta
	 * @param cajaAcepta
	 */
	public void setCajaAcepta(String cajaAcepta) {
		this.cajaAcepta = cajaAcepta;
	}


	/**
	 * Método que devuelve el valor del atributo cajeroAcepta
	 * @return cajeroAcepta
	 */
	public String getCajeroAcepta() {
		return cajeroAcepta;
	}


	/**
	 * Método que almacena el valor del atributo cajeroAcepta
	 * @param cajeroAcepta
	 */
	public void setCajeroAcepta(String cajeroAcepta) {
		this.cajeroAcepta = cajeroAcepta;
	}


	/**
	 * Método que devuelve el valor del atributo transportadora
	 * @return transportadora
	 */
	public String getTransportadora() {
		return transportadora;
	}


	/**
	 * Método que almacena el valor del atributo transportadora
	 * @param transportadora
	 */
	public void setTransportadora(String transportadora) {
		this.transportadora = transportadora;
	}


	/**
	 * Método que devuelve el valor del atributo nitTransportadora
	 * @return nitTransportadora
	 */
	public String getNitTransportadora() {
		return nitTransportadora;
	}


	/**
	 * Método que almacena el valor del atributo nitTransportadora
	 * @param nitTransportadora
	 */
	public void setNitTransportadora(String nitTransportadora) {
		this.nitTransportadora = nitTransportadora;
	}


	/**
	 * Método que devuelve el valor del atributo responsableRecibe
	 * @return responsableRecibe
	 */
	public String getResponsableRecibe() {
		return responsableRecibe;
	}


	/**
	 * Método que almacena el valor del atributo responsableRecibe
	 * @param responsableRecibe
	 */
	public void setResponsableRecibe(String responsableRecibe) {
		this.responsableRecibe = responsableRecibe;
	}


	/**
	 * Método que devuelve el valor del atributo numCarnet
	 * @return numCarnet
	 */
	public String getNumCarnet() {
		return numCarnet;
	}


	/**
	 * Método que almacena el valor del atributo numCarnet
	 * @param numCarnet
	 */
	public void setNumCarnet(String numCarnet) {
		this.numCarnet = numCarnet;
	}


	/**
	 * Método que devuelve el valor del atributo numGuia
	 * @return numGuia
	 */
	public String getNumGuia() {
		return numGuia;
	}


	/**
	 * Método que almacena el valor del atributo numGuia
	 * @param numGuia
	 */
	public void setNumGuia(String numGuia) {
		this.numGuia = numGuia;
	}


	/**
	 * Método que devuelve el valor del atributo numCarro
	 * @return numCarro
	 */
	public String getNumCarro() {
		return numCarro;
	}


	/**
	 * Método que almacena el valor del atributo numCarro
	 * @param numCarro
	 */
	public void setNumCarro(String numCarro) {
		this.numCarro = numCarro;
	}


	/**
	 * Método que devuelve el valor del atributo entidadFinanciera
	 * @return entidadFinanciera
	 */
	public String getEntidadFinanciera() {
		return entidadFinanciera;
	}


	/**
	 * Método que almacena el valor del atributo entidadFinanciera
	 * @param entidadFinanciera
	 */
	public void setEntidadFinanciera(String entidadFinanciera) {
		this.entidadFinanciera = entidadFinanciera;
	}


	/**
	 * Método que devuelve el valor del atributo tipoNroCuentaBancaria
	 * @return tipoNroCuentaBancaria
	 */
	public String getTipoNroCuentaBancaria() {
		return tipoNroCuentaBancaria;
	}


	/**
	 * Método que almacena el valor del atributo tipoNroCuentaBancaria
	 * @param tipoNroCuentaBancaria
	 */
	public void setTipoNroCuentaBancaria(String tipoNroCuentaBancaria) {
		this.tipoNroCuentaBancaria = tipoNroCuentaBancaria;
	}


	/**
	 * Método que devuelve el valor del atributo exito
	 * @return exito
	 */
	public boolean isExito() {
		return exito;
	}


	/**
	 * Método que almacena el valor del atributo exito
	 * @param exito
	 */
	public void setExito(boolean exito) {
		this.exito = exito;
	}


	/**
	 * @return the esConsulta
	 */
	public boolean isEsConsulta() {
		return esConsulta;
	}


	/**
	 * @param esConsulta the esConsulta to set
	 */
	public void setEsConsulta(boolean esConsulta) {
		this.esConsulta = esConsulta;
	}

}
