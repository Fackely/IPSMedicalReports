/*
 * @author Jorge Armando Osorio Velasquez
 */
package com.princetonsa.dto.interfaz;

import java.io.Serializable;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class DtoInterfazTransaccionAxInv implements Serializable
{
	/**
	 * Corresponde al tipo de transaccion de Axioma, se define segun el tipo de transaccion asociado
	 * a los indicativos de transaccion que se van a manejar en el sistema.
	 * Se alimenta desde el sistema Axioma
	 */
	private String tipoTransAxioma;
	
	/**
	 * Corresponde al numero de transaccion generado en axioma-consecutivo.
	 * Se alimenta desde axioma.
	 */
	private String numeroTransaccionAxioma;
	
	/**
	 * Campo definido en el sistema de interfaz, se alimenta segun el tipo de documento asociado 
	 * a los indicativos de transaccion que se van a manejar en el sistema.
	 * Se alimenta desde la interfaz.
	 */
	private String tipoDocumento;
	
	/**
	 * Corresponde al numero de transaccion generado en interfaz-consecutivo.
	 * Se alimenta desde interfaz.
	 */
	private String numeroDocumento;
	
	/**
	 * Campo definido en el sistema interfaz.
	 * Se alimenta desde la interfaz.
	 */
	private String codigoTransaccion;
	
	/**
	 * Campo para definir el tipo de movimiento generado, contiene la homologacion de los
	 * tipos de transacciones con axioma.
	 * Se alimienta desde la interfaz y desde axioma, de acuterdo al movimiento generado.
	 * 
	 */
	private String indicativoTransaccion;
	
	/**
	 * Campo para identificar el sistema de origina el movimiento, tendra los valores
	 * 1. Interfaz.
	 * 2. Axioma.
	 */
	private String origenTransaccion;
	
	/**
	 * Campo que se alimineta desde la interfaz. desde axioma, siempre se alimenta con 'N' desde axioma.
	 * solo permite los valores 'S' o 'N'.
	 */
	private String indicativoCostoDonacion;
	
	/**
	 * Campo para registrar el codigo correspondiente al almacen interfaz 
	 * con el cual se homoga cada almacen del sistema axioma
	 */
	private String almacenInterfaz;
	
	/**
	 * Campo que corresponde a la fecha del sistema en la cual se genera la transsacion
	 * desde el sistema axioma o desde la interfaz.
	 */
	private String fechaTransaccion;
	
	/**
	 * Campo que corresponde a la hora del sistema en la cual se genera la transaccion
	 * desde el sistema axioma. o desde el sistema interfaz.
	 */
	private String horaTransaccion;
	
	/**
	 * Campo para aignar  desde axioma la identificacion del proveedor en los casos de transacciones 
	 * generados como tipo - compra proveedor, corresponde al numero de identificacion del tercero
	 * de la tabla de terceros.
	 * Se alimenta desde Axioma.
	 */
	private String identificacionProveedor;
	
	/**
	 * Campo para registrar el codigo correspondiente al articulo de sistema interfaz, 
	 * campo codigo interfaz, de la tabla articulos axioma.
	 * Se liment desde la interfaz, y deste el sistema Axioma.
	 * Si al realizar el registro dedes axioma. se encuentra nulo el codigo interfaz en la tabla de articulos,
	 * se guardara tambien como nulo en el registro de tla tabla 'ax_inv'.
	 */
	private String codigoArticuloInterfaz;
	
	/**
	 * Campo para registra el codigo correspondeinte al articulo del sistema axioma.
	 * Se alimienta desde axioma.
	 */
	private String codigoArticulo;
	
	/**
	 * Campo que se alimenta desde los dos sistemas corresponde a la cantidad para
	 * cada articulo. de acuerdo al movimiento generado.
	 */
	private String cantidad;
	
	/**
	 * Campo que se alimineta desde los dos sistema,a corresponde al valor unitario para 
	 * cada articulo, de acuerdo al movimiento generado.
	 */
	private String valorUnitario;
	
	/**
	 * Campo para actualiza con el valor unitario de iva que se encuentra definido en las transacciones.
	 */
	private String valorIva;
	
	/**
	 * Campo para actualizar desde el sistema interfaz, el valor del costo en los casos de transaccion
	 * definidas con el indicativo costo de donacion = 'S'.
	 */
	private String costoDonacion;
	
	/**
	 * Estado desde el registro.
	 */
	private String estadoRegistro;
	
	/**
	 * Asignado por el sistema Axioma.
	 * Se alimenta desde el sistema axioma.
	 */
	private String codigoPaciente;
	
	/**
	 * Asignado por el sistema axioma.
	 * Se laimineta desde axioma.
	 */
	private String ingresoPaciente;
	
	/**
	 * Asignar automaticamente la fecha de generacion de cada registro, se asignara automaticamente
	 * la fecha del sistema del momento en el cual se insertan los registros en la tabla.
	 * Este campo no se actualiza con las modificaciones. 
	 */
	private String fechaRegistro;
	
	/**
	 * Asignar automaticamenteo la hora de generacion (Hora del sistema.)
	 * Este campo no se acutalizan con las transacciones
	 */
	private String horaRegistro;

	
	// CAMBIOS OCT 27 de 2008, la interfaz ax_inv requiere el centro de costo que solicita y el centro de costo que despacha
	
	/**
	 * Asignar el codigo del Centro de Costo del Almacen que solicita
	 */
	private String almacenSolicita;
	
	/**
	 * Asignar el codigo del Centro de Costo del Almacen que Despacha
	 */
	private String almacendespacha;
	
	
	// CAMBIOS NOV 12 de 2008, la interfaz ax_inv requiere el codigo interfaz del almacen en consignacion, tomado de los parametros Almacen.
	
	/**
	 * Campo para registrar el codigo correspondiente al almacen Consignacion
	 */
	private String almacenConsignacion;
	
	/**
	 * Campo para almacenar el login del usuario logueado en sesion.
	 */
	private String usuario;
	
	/**
	 * 
	 */
	private String tercero;
	
	/**
	 * 
	 */
	private String centroCostoOrdena;
	
	/**
	 * 
	 */
	private String centroCostoEjecuta;
	
	/**
	 * 
	 */
	private String valorUltimaCompra;
	
	/**
	 * 
	 */
	private String consecutivo;
	/**
	 * 
	 */
	private String valorCompraMasAlta;
	
	/**
	 * @param tipoTransAxioma
	 * @param numeroTransaccionAxioma
	 * @param tipoDocumento
	 * @param numeroDocumento
	 * @param codigoTransaccion
	 * @param indicativoTransaccion
	 * @param origenTransaccion
	 * @param indicativoCostoDonacion
	 * @param almacenInterfaz
	 * @param fechaTransaccion
	 * @param horaTransaccion
	 * @param identificacionProveedor
	 * @param codigoArticuloInterfaz
	 * @param codigoArticulo
	 * @param cantidad
	 * @param valorUnitario
	 * @param valorIva
	 * @param costoDonacion
	 * @param estadoRegistro
	 * @param codigoPaciente
	 * @param ingresoPaciente
	 * @param fechaRegistro
	 * @param horaRegistro
	 * @param almacenSolicita
	 * @param almacendespacha
	 * @param almacenConsignacion
	 * @param usuario;
	 */
	public DtoInterfazTransaccionAxInv() 
	{
		this.tipoTransAxioma = "";
		this.numeroTransaccionAxioma = "";
		this.tipoDocumento = "";
		this.numeroDocumento = "";
		this.codigoTransaccion = "";
		this.indicativoTransaccion = "";
		this.origenTransaccion = "";
		this.indicativoCostoDonacion = "";
		this.almacenInterfaz = "";
		this.fechaTransaccion = "";
		this.horaTransaccion = "";
		this.identificacionProveedor = "";
		this.codigoArticuloInterfaz = "";
		this.codigoArticulo = "";
		this.cantidad = "";
		this.valorUnitario = "";
		this.valorIva = "";
		this.costoDonacion = "";
		this.estadoRegistro = "";
		this.codigoPaciente = "";
		this.ingresoPaciente = "";
		this.fechaRegistro = "";
		this.horaRegistro = "";
		this.almacenSolicita="";
		this.almacendespacha="";
		this.almacenConsignacion="";
		this.usuario="";
		this.tercero="";
		this.centroCostoEjecuta="";
		this.centroCostoOrdena="";
		this.valorCompraMasAlta="";
		this.valorUltimaCompra="";
		this.consecutivo="";
	}

	/**
	 * @return the consecutivo
	 */
	public String getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	/**
	 * @return the centroCostoOrdena
	 */
	public String getCentroCostoOrdena() {
		return centroCostoOrdena;
	}

	/**
	 * @param centroCostoOrdena the centroCostoOrdena to set
	 */
	public void setCentroCostoOrdena(String centroCostoOrdena) {
		this.centroCostoOrdena = centroCostoOrdena;
	}

	/**
	 * @return the centroCostoEjecuta
	 */
	public String getCentroCostoEjecuta() {
		return centroCostoEjecuta;
	}

	/**
	 * @param centroCostoEjecuta the centroCostoEjecuta to set
	 */
	public void setCentroCostoEjecuta(String centroCostoEjecuta) {
		this.centroCostoEjecuta = centroCostoEjecuta;
	}

	/**
	 * @return the valorUltimaCompra
	 */
	public String getValorUltimaCompra() {
		return valorUltimaCompra;
	}

	/**
	 * @param valorUltimaCompra the valorUltimaCompra to set
	 */
	public void setValorUltimaCompra(String valorUltimaCompra) {
		this.valorUltimaCompra = valorUltimaCompra;
	}

	/**
	 * @return the valorCompraMasAlta
	 */
	public String getValorCompraMasAlta() {
		return valorCompraMasAlta;
	}

	/**
	 * @param valorCompraMasAlta the valorCompraMasAlta to set
	 */
	public void setValorCompraMasAlta(String valorCompraMasAlta) {
		this.valorCompraMasAlta = valorCompraMasAlta;
	}

	/**
	 * @return the tercero
	 */
	public String getTercero() {
		return tercero;
	}

	/**
	 * @param tercero the tercero to set
	 */
	public void setTercero(String tercero) {
		this.tercero = tercero;
	}

	/**
	 * @return the almacenSolicita
	 */
	public String getAlmacenSolicita() {
		return almacenSolicita;
	}

	/**
	 * @param almacenSolicita the almacenSolicita to set
	 */
	public void setAlmacenSolicita(String almacenSolicita) {
		this.almacenSolicita = almacenSolicita;
	}

	/**
	 * @return the almacendespacha
	 */
	public String getAlmacendespacha() {
		return almacendespacha;
	}

	/**
	 * @param almacendespacha the almacendespacha to set
	 */
	public void setAlmacendespacha(String almacendespacha) {
		this.almacendespacha = almacendespacha;
	}

	/**
	 * @return the almacenInterfaz
	 */
	public String getAlmacenInterfaz() {
		return almacenInterfaz;
	}

	/**
	 * @param almacenInterfaz the almacenInterfaz to set
	 */
	public void setAlmacenInterfaz(String almacenInterfaz) {
		this.almacenInterfaz = almacenInterfaz;
	}

	/**
	 * @return the cantidad
	 */
	public String getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @return the codigoArticulo
	 */
	public String getCodigoArticulo() {
		return codigoArticulo;
	}

	/**
	 * @param codigoArticulo the codigoArticulo to set
	 */
	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	/**
	 * @return the codigoArticuloInterfaz
	 */
	public String getCodigoArticuloInterfaz() {
		return codigoArticuloInterfaz;
	}

	/**
	 * @param codigoArticuloInterfaz the codigoArticuloInterfaz to set
	 */
	public void setCodigoArticuloInterfaz(String codigoArticuloInterfaz) {
		this.codigoArticuloInterfaz = codigoArticuloInterfaz;
	}

	/**
	 * @return the codigoPaciente
	 */
	public String getCodigoPaciente() {
		return codigoPaciente;
	}

	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(String codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	/**
	 * @return the codigoTransaccion
	 */
	public String getCodigoTransaccion() {
		return codigoTransaccion;
	}

	/**
	 * @param codigoTransaccion the codigoTransaccion to set
	 */
	public void setCodigoTransaccion(String codigoTransaccion) {
		this.codigoTransaccion = codigoTransaccion;
	}

	/**
	 * @return the costoDonacion
	 */
	public String getCostoDonacion() {
		return costoDonacion;
	}

	/**
	 * @param costoDonacion the costoDonacion to set
	 */
	public void setCostoDonacion(String costoDonacion) {
		this.costoDonacion = costoDonacion;
	}

	/**
	 * @return the estadoRegistro
	 */
	public String getEstadoRegistro() {
		return estadoRegistro;
	}

	/**
	 * @param estadoRegistro the estadoRegistro to set
	 */
	public void setEstadoRegistro(String estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}

	/**
	 * @return the fechaRegistro
	 */
	public String getFechaRegistro() {
		return fechaRegistro;
	}

	/**
	 * @param fechaRegistro the fechaRegistro to set
	 */
	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	/**
	 * @return the fechaTransaccion
	 */
	public String getFechaTransaccion() {
		return fechaTransaccion;
	}

	/**
	 * @param fechaTransaccion the fechaTransaccion to set
	 */
	public void setFechaTransaccion(String fechaTransaccion) {
		this.fechaTransaccion = fechaTransaccion;
	}

	/**
	 * @return the horaRegistro
	 */
	public String getHoraRegistro() {
		return horaRegistro;
	}

	/**
	 * @param horaRegistro the horaRegistro to set
	 */
	public void setHoraRegistro(String horaRegistro) {
		this.horaRegistro = horaRegistro;
	}

	/**
	 * @return the horaTransaccion
	 */
	public String getHoraTransaccion() {
		return horaTransaccion;
	}

	/**
	 * @param horaTransaccion the horaTransaccion to set
	 */
	public void setHoraTransaccion(String horaTransaccion) {
		this.horaTransaccion = horaTransaccion;
	}

	/**
	 * @return the identificacionProveedor
	 */
	public String getIdentificacionProveedor() {
		return identificacionProveedor;
	}

	/**
	 * @param identificacionProveedor the identificacionProveedor to set
	 */
	public void setIdentificacionProveedor(String identificacionProveedor) {
		this.identificacionProveedor = identificacionProveedor;
	}

	/**
	 * @return the indicativoCostoDonacion
	 */
	public String getIndicativoCostoDonacion() {
		return indicativoCostoDonacion;
	}

	/**
	 * @param indicativoCostoDonacion the indicativoCostoDonacion to set
	 */
	public void setIndicativoCostoDonacion(String indicativoCostoDonacion) {
		this.indicativoCostoDonacion = indicativoCostoDonacion;
	}

	/**
	 * @return the indicativoTransaccion
	 */
	public String getIndicativoTransaccion() {
		return indicativoTransaccion;
	}

	/**
	 * @param indicativoTransaccion the indicativoTransaccion to set
	 */
	public void setIndicativoTransaccion(String indicativoTransaccion) {
		this.indicativoTransaccion = indicativoTransaccion;
	}

	/**
	 * @return the ingresoPaciente
	 */
	public String getIngresoPaciente() {
		return ingresoPaciente;
	}

	/**
	 * @param ingresoPaciente the ingresoPaciente to set
	 */
	public void setIngresoPaciente(String ingresoPaciente) {
		this.ingresoPaciente = ingresoPaciente;
	}

	/**
	 * @return the numeroDocumento
	 */
	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	/**
	 * @param numeroDocumento the numeroDocumento to set
	 */
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	/**
	 * @return the numeroTransaccionAxioma
	 */
	public String getNumeroTransaccionAxioma() {
		return numeroTransaccionAxioma;
	}

	/**
	 * @param numeroTransaccionAxioma the numeroTransaccionAxioma to set
	 */
	public void setNumeroTransaccionAxioma(String numeroTransaccionAxioma) {
		this.numeroTransaccionAxioma = numeroTransaccionAxioma;
	}

	/**
	 * @return the origenTransaccion
	 */
	public String getOrigenTransaccion() {
		return origenTransaccion;
	}

	/**
	 * @param origenTransaccion the origenTransaccion to set
	 */
	public void setOrigenTransaccion(String origenTransaccion) {
		this.origenTransaccion = origenTransaccion;
	}

	/**
	 * @return the tipoDocumento
	 */
	public String getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @param tipoDocumento the tipoDocumento to set
	 */
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @return the tipoTransAxioma
	 */
	public String getTipoTransAxioma() {
		return tipoTransAxioma;
	}

	/**
	 * @param tipoTransAxioma the tipoTransAxioma to set
	 */
	public void setTipoTransAxioma(String tipoTransAxioma) {
		this.tipoTransAxioma = tipoTransAxioma;
	}

	/**
	 * @return the valorIva
	 */
	public String getValorIva() {
		return valorIva;
	}

	/**
	 * @param valorIva the valorIva to set
	 */
	public void setValorIva(String valorIva) {
		this.valorIva = valorIva;
	}

	/**
	 * @return the valorUnitario
	 */
	public String getValorUnitario() {
		return valorUnitario;
	}

	/**
	 * @param valorUnitario the valorUnitario to set
	 */
	public void setValorUnitario(String valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	/**
	 * @return the almacenConsignacion
	 */
	public String getAlmacenConsignacion() {
		return almacenConsignacion;
	}

	/**
	 * @param almacenConsignacion the almacenConsignacion to set
	 */
	public void setAlmacenConsignacion(String almacenConsignacion) {
		this.almacenConsignacion = almacenConsignacion;
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

}
