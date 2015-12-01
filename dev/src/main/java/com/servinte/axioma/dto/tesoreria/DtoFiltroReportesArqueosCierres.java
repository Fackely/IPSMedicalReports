package com.servinte.axioma.dto.tesoreria;

import java.io.Serializable;

/**
 * Clase que almacena la informaci�n necesaria para generar los encabezados de los reportes:
 * Arqueo Caja Resumido y Detallado DCU 1127
 * Cierre Turno Caja DCU 1038
 * Entrega Caja Mayor DCU 1039
 * Impresi�n Traslado Entre Cajas DCU 1024
 * Entrega Transportadora DCU 1025
 *  
 * @author Fabi�n Becerra
 *
 */
public class DtoFiltroReportesArqueosCierres implements Serializable{

	/** * */
	private static final long serialVersionUID = 1L;
	
	/***************ATRIBUTOS UTILIZADOS EN LOS TITULOS DEL REPORTE*************/	
	/**
	 * Atributo que almacena la razon social de la instituci�n
	 */
	private String razonSocial;
	
	/**
	 * Atributo que almacena el nit de la instituci�n
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
	 * Atributo que almacena la actividad economica de la instituci�n
	 */
	private String actividadEconomica;
	
	/**
	 * Atributo que almacena la direcci�n de la instituci�n
	 */
	private String direccion;
	
	/**
	 * Atributo que almacena el tel�fono de la instituci�n
	 */
	private String telefono;
	
	/**
	 * Atributo que almacena la descripci�n del centro de Atenci�n
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
	 * Atributo que almacena la fecha de generaci�n del arqueo
	 */
	private String fechaGeneracion;
	
	/**
	 * Atributo que almacena la hora de generaci�n del arqueo
	 */
	private String horaGeneracion;
	
	/**
	 * Atributo que almacena el usuario que gener� el arqueo 
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
	 * Atributo que almacena la caja para la cual se est� generando el arqueo
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
	 * Atributo que almacena la fecha de aceptaci�n de la solicitud del traslado 
	 */
	private String fechaAceptacion;
	
	/**
	 * Atributo que almacena la hora de aceptaci�n de la solicitud del traslado 
	 */
	private String horaAceptacion;
	
	/**
	 * Atributo que almacena la caja de aceptaci�n de la solicitud del traslado
	 */
	private String cajaAcepta;
	
	/**
	 * Atributo que almacena el cajero que acepta la solicitud del traslado
	 */
	private String cajeroAcepta;
	
	/**
	 * Atributo que almacena la raz�n social de la transportadora responsable de la entrega transportadora
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
	 * Atributo que almacena el n�mero de carn� del responsable de la entrega transportadora
	 */
	private String numCarnet;
	
	/**
	 * Atributo que almacena el n�mero de guia del responsable de la entrega transportadora
	 */
	private String numGuia;
	
	/**
	 * Atributo que almacena el n�mero de carro registrado en la entrega transportadora
	 */
	private String numCarro;
	
	/**
	 * Atributo que almacena el n�mero de carro registrado en la entrega transportadora
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
	 * M�todo que inicializa los atributos de la clase 
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
	 * M�todo que devuelve el valor del atributo razonSocial
	 * @return razonSocial
	 */
	public String getRazonSocial() {
		return razonSocial;
	}


	/**
	 * M�todo que almacena el valor del atributo razonSocial
	 * @param razonSocial
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}


	/**
	 * M�todo que devuelve el valor del atributo nit
	 * @return nit
	 */
	public String getNit() {
		return nit;
	}


	/**
	 * M�todo que almacena el valor del atributo nit
	 * @param nit
	 */
	public void setNit(String nit) {
		this.nit = nit;
	}


	/**
	 * M�todo que devuelve el valor del atributo ubicacionLogo
	 * @return ubicacionLogo
	 */
	public String getUbicacionLogo() {
		return ubicacionLogo;
	}


	/**
	 * M�todo que almacena el valor del atributo ubicacionLogo
	 * @param ubicacionLogo
	 */
	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
	}


	/**
	 * M�todo que devuelve el valor del atributo rutaLogo
	 * @return rutaLogo
	 */
	public String getRutaLogo() {
		return rutaLogo;
	}


	/**
	 * M�todo que almacena el valor del atributo rutaLogo
	 * @param rutaLogo
	 */
	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
	}


	/**
	 * M�todo que devuelve el valor del atributo actividadEconomica
	 * @return actividadEconomica
	 */
	public String getActividadEconomica() {
		return actividadEconomica;
	}


	/**
	 * M�todo que almacena el valor del atributo actividadEconomica
	 * @param actividadEconomica
	 */
	public void setActividadEconomica(String actividadEconomica) {
		this.actividadEconomica = actividadEconomica;
	}


	/**
	 * M�todo que devuelve el valor del atributo direccion
	 * @return direccion
	 */
	public String getDireccion() {
		return direccion;
	}


	/**
	 * M�todo que almacena el valor del atributo direccion
	 * @param direccion
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}


	/**
	 * M�todo que devuelve el valor del atributo telefono
	 * @return telefono
	 */
	public String getTelefono() {
		return telefono;
	}


	/**
	 * M�todo que almacena el valor del atributo telefono
	 * @param telefono
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}


	/**
	 * M�todo que devuelve el valor del atributo centroAtencion
	 * @return centroAtencion
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}


	/**
	 * M�todo que almacena el valor del atributo centroAtencion
	 * @param centroAtencion
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}


	/**
	 * M�todo que devuelve el valor del atributo nombreUsuario
	 * @return nombreUsuario
	 */
	public String getNombreUsuario() {
		return nombreUsuario;
	}


	/**
	 * M�todo que almacena el valor del atributo nombreUsuario
	 * @param nombreUsuario
	 */
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}


	/**
	 * M�todo que devuelve el valor del atributo nroConsecutivo
	 * @return nroConsecutivo
	 */
	public String getNroConsecutivo() {
		return nroConsecutivo;
	}


	/**
	 * M�todo que almacena el valor del atributo nroConsecutivo
	 * @param nroConsecutivo
	 */
	public void setNroConsecutivo(String nroConsecutivo) {
		this.nroConsecutivo = nroConsecutivo;
	}


	/**
	 * M�todo que devuelve el valor del atributo fechaGeneracion
	 * @return fechaGeneracion
	 */
	public String getFechaGeneracion() {
		return fechaGeneracion;
	}


	/**
	 * M�todo que almacena el valor del atributo fechaGeneracion
	 * @param fechaGeneracion
	 */
	public void setFechaGeneracion(String fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}


	/**
	 * M�todo que devuelve el valor del atributo horaGeneracion
	 * @return horaGeneracion
	 */
	public String getHoraGeneracion() {
		return horaGeneracion;
	}


	/**
	 * M�todo que almacena el valor del atributo horaGeneracion
	 * @param horaGeneracion
	 */
	public void setHoraGeneracion(String horaGeneracion) {
		this.horaGeneracion = horaGeneracion;
	}


	/**
	 * M�todo que devuelve el valor del atributo usuarioGeneracion
	 * @return usuarioGeneracion
	 */
	public String getUsuarioGeneracion() {
		return usuarioGeneracion;
	}


	/**
	 * M�todo que almacena el valor del atributo usuarioGeneracion
	 * @param usuarioGeneracion
	 */
	public void setUsuarioGeneracion(String usuarioGeneracion) {
		this.usuarioGeneracion = usuarioGeneracion;
	}


	/**
	 * M�todo que devuelve el valor del atributo fechaArqueo
	 * @return fechaArqueo
	 */
	public String getFechaArqueo() {
		return fechaArqueo;
	}


	/**
	 * M�todo que almacena el valor del atributo fechaArqueo
	 * @param fechaArqueo
	 */
	public void setFechaArqueo(String fechaArqueo) {
		this.fechaArqueo = fechaArqueo;
	}


	/**
	 * M�todo que devuelve el valor del atributo fechaCierre
	 * @return fechaCierre
	 */
	public String getFechaCierre() {
		return fechaCierre;
	}


	/**
	 * M�todo que almacena el valor del atributo fechaCierre
	 * @param fechaCierre
	 */
	public void setFechaCierre(String fechaCierre) {
		this.fechaCierre = fechaCierre;
	}


	/**
	 * M�todo que devuelve el valor del atributo cajero
	 * @return cajero
	 */
	public String getCajero() {
		return cajero;
	}


	/**
	 * M�todo que almacena el valor del atributo cajero
	 * @param cajero
	 */
	public void setCajero(String cajero) {
		this.cajero = cajero;
	}


	/**
	 * M�todo que devuelve el valor del atributo caja
	 * @return caja
	 */
	public String getCaja() {
		return caja;
	}


	/**
	 * M�todo que almacena el valor del atributo caja
	 * @param caja
	 */
	public void setCaja(String caja) {
		this.caja = caja;
	}


	/**
	 * M�todo que devuelve el valor del atributo cajaMayorPrincipal
	 * @return cajaMayorPrincipal
	 */
	public String getCajaMayorPrincipal() {
		return cajaMayorPrincipal;
	}


	/**
	 * M�todo que almacena el valor del atributo cajaMayorPrincipal
	 * @param cajaMayorPrincipal
	 */
	public void setCajaMayorPrincipal(String cajaMayorPrincipal) {
		this.cajaMayorPrincipal = cajaMayorPrincipal;
	}


	/**
	 * M�todo que devuelve el valor del atributo estadoSolicitud
	 * @return estadoSolicitud
	 */
	public String getEstadoSolicitud() {
		return estadoSolicitud;
	}


	/**
	 * M�todo que almacena el valor del atributo estadoSolicitud
	 * @param estadoSolicitud
	 */
	public void setEstadoSolicitud(String estadoSolicitud) {
		this.estadoSolicitud = estadoSolicitud;
	}


	/**
	 * M�todo que devuelve el valor del atributo cajaTraslada
	 * @return cajaTraslada
	 */
	public String getCajaTraslada() {
		return cajaTraslada;
	}


	/**
	 * M�todo que almacena el valor del atributo cajaTraslada
	 * @param cajaTraslada
	 */
	public void setCajaTraslada(String cajaTraslada) {
		this.cajaTraslada = cajaTraslada;
	}


	/**
	 * M�todo que devuelve el valor del atributo cajeroTraslada
	 * @return cajeroTraslada
	 */
	public String getCajeroTraslada() {
		return cajeroTraslada;
	}


	/**
	 * M�todo que almacena el valor del atributo cajeroTraslada
	 * @param cajeroTraslada
	 */
	public void setCajeroTraslada(String cajeroTraslada) {
		this.cajeroTraslada = cajeroTraslada;
	}


	/**
	 * M�todo que devuelve el valor del atributo fechaAceptacion
	 * @return fechaAceptacion
	 */
	public String getFechaAceptacion() {
		return fechaAceptacion;
	}


	/**
	 * M�todo que almacena el valor del atributo fechaAceptacion
	 * @param fechaAceptacion
	 */
	public void setFechaAceptacion(String fechaAceptacion) {
		this.fechaAceptacion = fechaAceptacion;
	}


	/**
	 * M�todo que devuelve el valor del atributo horaAceptacion
	 * @return horaAceptacion
	 */
	public String getHoraAceptacion() {
		return horaAceptacion;
	}


	/**
	 * M�todo que almacena el valor del atributo horaAceptacion
	 * @param horaAceptacion
	 */
	public void setHoraAceptacion(String horaAceptacion) {
		this.horaAceptacion = horaAceptacion;
	}


	/**
	 * M�todo que devuelve el valor del atributo cajaAcepta
	 * @return cajaAcepta
	 */
	public String getCajaAcepta() {
		return cajaAcepta;
	}


	/**
	 * M�todo que almacena el valor del atributo cajaAcepta
	 * @param cajaAcepta
	 */
	public void setCajaAcepta(String cajaAcepta) {
		this.cajaAcepta = cajaAcepta;
	}


	/**
	 * M�todo que devuelve el valor del atributo cajeroAcepta
	 * @return cajeroAcepta
	 */
	public String getCajeroAcepta() {
		return cajeroAcepta;
	}


	/**
	 * M�todo que almacena el valor del atributo cajeroAcepta
	 * @param cajeroAcepta
	 */
	public void setCajeroAcepta(String cajeroAcepta) {
		this.cajeroAcepta = cajeroAcepta;
	}


	/**
	 * M�todo que devuelve el valor del atributo transportadora
	 * @return transportadora
	 */
	public String getTransportadora() {
		return transportadora;
	}


	/**
	 * M�todo que almacena el valor del atributo transportadora
	 * @param transportadora
	 */
	public void setTransportadora(String transportadora) {
		this.transportadora = transportadora;
	}


	/**
	 * M�todo que devuelve el valor del atributo nitTransportadora
	 * @return nitTransportadora
	 */
	public String getNitTransportadora() {
		return nitTransportadora;
	}


	/**
	 * M�todo que almacena el valor del atributo nitTransportadora
	 * @param nitTransportadora
	 */
	public void setNitTransportadora(String nitTransportadora) {
		this.nitTransportadora = nitTransportadora;
	}


	/**
	 * M�todo que devuelve el valor del atributo responsableRecibe
	 * @return responsableRecibe
	 */
	public String getResponsableRecibe() {
		return responsableRecibe;
	}


	/**
	 * M�todo que almacena el valor del atributo responsableRecibe
	 * @param responsableRecibe
	 */
	public void setResponsableRecibe(String responsableRecibe) {
		this.responsableRecibe = responsableRecibe;
	}


	/**
	 * M�todo que devuelve el valor del atributo numCarnet
	 * @return numCarnet
	 */
	public String getNumCarnet() {
		return numCarnet;
	}


	/**
	 * M�todo que almacena el valor del atributo numCarnet
	 * @param numCarnet
	 */
	public void setNumCarnet(String numCarnet) {
		this.numCarnet = numCarnet;
	}


	/**
	 * M�todo que devuelve el valor del atributo numGuia
	 * @return numGuia
	 */
	public String getNumGuia() {
		return numGuia;
	}


	/**
	 * M�todo que almacena el valor del atributo numGuia
	 * @param numGuia
	 */
	public void setNumGuia(String numGuia) {
		this.numGuia = numGuia;
	}


	/**
	 * M�todo que devuelve el valor del atributo numCarro
	 * @return numCarro
	 */
	public String getNumCarro() {
		return numCarro;
	}


	/**
	 * M�todo que almacena el valor del atributo numCarro
	 * @param numCarro
	 */
	public void setNumCarro(String numCarro) {
		this.numCarro = numCarro;
	}


	/**
	 * M�todo que devuelve el valor del atributo entidadFinanciera
	 * @return entidadFinanciera
	 */
	public String getEntidadFinanciera() {
		return entidadFinanciera;
	}


	/**
	 * M�todo que almacena el valor del atributo entidadFinanciera
	 * @param entidadFinanciera
	 */
	public void setEntidadFinanciera(String entidadFinanciera) {
		this.entidadFinanciera = entidadFinanciera;
	}


	/**
	 * M�todo que devuelve el valor del atributo tipoNroCuentaBancaria
	 * @return tipoNroCuentaBancaria
	 */
	public String getTipoNroCuentaBancaria() {
		return tipoNroCuentaBancaria;
	}


	/**
	 * M�todo que almacena el valor del atributo tipoNroCuentaBancaria
	 * @param tipoNroCuentaBancaria
	 */
	public void setTipoNroCuentaBancaria(String tipoNroCuentaBancaria) {
		this.tipoNroCuentaBancaria = tipoNroCuentaBancaria;
	}


	/**
	 * M�todo que devuelve el valor del atributo exito
	 * @return exito
	 */
	public boolean isExito() {
		return exito;
	}


	/**
	 * M�todo que almacena el valor del atributo exito
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
