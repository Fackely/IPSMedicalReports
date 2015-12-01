/*
 * Jun 15, 2007
 * Proyect axioma
 * Paquete com.princetonsa.dto.manejoPaciente
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
 */
package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dto.cargos.DtoDetalleCargo;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class DtoSolicitudesSubCuenta implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private DtoDetalleCargo detalleCargo;
	
	/**
	 * Codigo de la relacion, sol-art/serv-subcuenta
	 */
	private String codigo;
	
	/**
	 * Fecha de solicitud en formato dd/mm/yyyy
	 */
	private String fechaSolicitud;
	
	/**
	 * Hora de la solicitud
	 */
	private String horaSolicitud;
	
	/**
	 * Nuemero de la solicitud
	 */
	private String numeroSolicitud;
	
	/**
	 * Numero Consecutivo de la Solicitud.
	 */
	private String consecutivoSolicitud;
	
	/**
	 * Finalidad de cada servicio/solicitud
	 */
	private int finalidadSolicitud;
	
	/**
	 * codigo de subcuenta - responsable
	 */
	private String subCuenta;
	
	/**
	 * codigo del servicio, en caso de se un articulo este campo esta en blanco.
	 */
	private InfoDatosString servicio;
	
	/**
	 * 
	 */
	private String codigoCups;
	
	/**
	 * codigo de articulo, en caso de ser un servicio este campo esta en blanco.
	 */
	private InfoDatosString articulo;
	
	/**
	 * Codigo de la cuenta a la que se genero la solicitud.
	 */
	private String cuenta;
	
	/**
	 * Porcentaje de distribucion que le corresponde (excluyente con el monto).
	 */
	private String procentaje;
	
	/***
	 * Cantidad del servicio o articulo. siempre se encuentra lleno
	 */
	private String cantidad;
	
	/**
	 * Monto que le corresponde. (excluyente con el porcentaje).
	 */
	private String monto;
	
	/**
	 * Valor calculado del cargo.
	 */
	private String valorCargoCalculado;
	
	/**
	 * Indica si esta cubiero o no por el responsable asignado.
	 */
	private String cubierto;
	
	/**
	 * Indica el tipo de solicitud.
	 */
	private InfoDatosInt tipoSolicitud;
	
	/**
	 * Indica si la relacion hace parte de un paquete o no.
	 */
	private String paquetizada;
	
	/**
	 * En caso de estar paquetizada, este atributo contiene el numero de solicitud padre.
	 */
	private String solicitudPadre;
	
	/**
	 * En caso de ser un asocio de una cirugia, aque se almacena el servicio original de la cirugia.
	 */
	private InfoDatosString servicioCX;
	
	/**
	 * 
	 */
	private String codigoCupsCx;
	
	
	/**
	 * Tipo de asocio, en caso de cirugias.
	 */
	private InfoDatosInt tipoAsocio;
	
	/**
	 * InfoDatos para manejar el estado del cargo
	 */
	private InfoDatosInt estadoCargo;
	
	/**
	 * contienen el tipo de distribucion.
	 */
	private InfoDatosString tipoDistribucion;
	
	/**
	 * 
	 */
	private InfoDatosInt centroCostoSolicita;
	
	/**
	 * 
	 */
	private InfoDatosInt centroCostoEjecuta;
	
	
	/**
	 * Atributo que me indica cuantos responsables, incluyendo el do consulta, reponden por el mismo Servicio.
	 */
	private int numResponsablesMismoServicio;
	
	/**
	 * Atributo que me indica cuantos responsables, incluyendo el do consulta, reponden por el mismo Articulo.
	 */
	private int numResponsablesMismoArticulo;
		
	/**
	 * Atributo que me indica cuantos responsables, incluyendo el do consulta, y que reponden por el mismo Servicio han sido facturados.
	 */
	private int numResponsablesFacturadosMismoServicio;
	
	/**
	 * Atributo que me indica cuantos responsables, incluyendo el do consulta, y que reponden por el mismo Articulo han sido facturados..
	 */
	private int numResponsablesFacturadosMismoArticulo;
	
	
	
	/////////atributos del cargo
	
	/**
	 * Codigo del detalle del cargo
	 */
	private String codigoDetalleCargo;
	
	/**
	 * Convenio que responde por el cargo.
	 */
	private String convenio;
	
	
	private int codConvenio;
	
	/**
	 * Esquema tarifario con el que se calculo la tarifa.
	 */
	private String esquemaTarifario;
	
	/**
	 * Cantidad cargada
	 */
	private String cantidadCargada;
	
	/**
	 * Valor unitario tarifa
	 */
	private String valorUnitarioTarifa;
	
	/**
	 * Valor unitario cargado
	 */
	private String valorUnitarioCargado;
	
	/**
	 * valor total cargado.
	 */
	private String valorTotalCargado;
	
	/**
	 * Porcentaje cargado.
	 */
	private String porcentajeCargado;
	
	/**
	 * porcentaje recargo
	 */
	private String porcentajeRecargo;
	
	/**
	 * Valor unitario recargo.
	 */
	private String valorUnitarioRecargo;
	
	/**
	 * Porcentaje descuento.
	 */
	private String porcentajeDcto;
	
	/**
	 * Valor unitario descuento.
	 */
	private String valorUnitarioDcto;
	
	/**
	 * Valor unitario iva.
	 */
	private String valorUnitarioIva;
	
	/**
	 * Numero autorizacion del cargo.
	 */
	private String nroAutorizacion;
	
	
	/**
	 * codigo del cargo padre, solo aplica para servicios paquetizados.
	 */
	private String cargoPadre;
	
	/**
	 * Observaciones generadas al momento del cargo.
	 */
	private String observacionesCargo;
	
	/**
	 * 
	 */
	private String usuarioModifica;
	
	
	/**
	 * 
	 */
	private String facturado;
	
	/**
	 * Codigo que indica si maneja portatil o no
	 */
	private InfoDatosString portatil;
	
	/**
	 * Consecutivo registro cargo asocio honorarios
	 */
	private String detCxHonorarios;
	
	/**
	 * Consecutivo registro cargo asocio salas / materiales
	 */
	private String detAsocioCxSalasMat;
	
	/**
	 * 
	 */
	private String tipoContrato;
	
	/**
	 * 
	 */
	private int ingreso;
	
	/**
	 * 
	 */
	private int detcxhonorarios;
	
	/**
	 * 
	 */
	private int detasicxsalasmat;
	
	/**
	 * 
	 */
	private String tipoEntidadEjecuta;
	
	/**
	 * 
	 */
	private String centroAtencionIngreso;
	
	
	/**
	 * 
	 */
	private int codCentroAtencionIngreso;
	
	
	/**
	 * 
	 */
	private int codNaturalezaPaciente;
	/**
	 * 
	 */
	private String naturalezaPaciente;
	
	/**
	 * 
	 */
	private String codTipoPaciente;
	
	/**
	 * 
	 */
	private String tipoPaciente;
	
	/**
	 * 
	 */
	private int codViaIngreso;
	
	/**
	 * 
	 */
	private String viaIngreso;
	
	/**
	 * 
	 */
	private int codigoPaciente;
	/**
	 * 
	 */
	private String nombrePaciente;
	
	/**
	 * 
	 */
	private String tipoIdPaciente;
	
	/**
	 * 
	 */
	private String numeroIdPaciente;
	
	/**
	 * 
	 */
	private String codServicioCodManualEstandar;
	
	/**
	 * 
	 */
	private String codProfesional;
	
	/**
	 * 
	 */
	private String profesional;
	
	/**
	 * 
	 */
	private String especialidadSolicitadaProfesional;
	/**
	 * 
	 */
	private boolean urgenteSolicitud;
	
	/**
	 * 
	 */
	private String especialidadServicioSolicitud;
	
			
 	/**
 	 * SOLICITUD DE MEDICAMENTOS E INSUMOS PENDIENTES POR AUTORIZAR 
 	 */
 	//private String codigoArticulo; 
	private String descArticulo;
	private String unidadMedidaArticulo;
	private String dosisArticulo;
	private String frecuArticulo;
	private String tipoFrecueArticulo;
	private String viaArticulo;
	private String duracionArticulo;
	private String nroDosisTotalArticulo;
	private String esMedicamento;
	private String naturalezaArticulo;
	private String observaArticulo;
	private ArrayList <DtoSolicitudesSubCuenta> agrupaListadoAutoriPendEntSub ;
	private boolean guardoArticuloServicio;
	

	/**
	 * 
	 */
	public DtoSolicitudesSubCuenta() 
	{
		this.detalleCargo=new DtoDetalleCargo();
		this.codigo="";
		this.fechaSolicitud="";
		this.horaSolicitud="";
		this.numeroSolicitud="";
		this.consecutivoSolicitud="";
		this.finalidadSolicitud=ConstantesBD.codigoNuncaValido;
		this.subCuenta="";
		this.servicio=new InfoDatosString();
		this.articulo=new InfoDatosString();
		this.cuenta="";
		this.procentaje="";
		this.cantidad="";
		this.monto="";
		this.valorCargoCalculado="";
		this.cubierto="";
		this.tipoSolicitud=new InfoDatosInt();
		this.paquetizada="";
		this.solicitudPadre="";
		this.servicioCX=new InfoDatosString();
		this.tipoAsocio=new InfoDatosInt();
		this.estadoCargo=new InfoDatosInt();
		this.tipoDistribucion=new InfoDatosString();
		this.centroCostoSolicita=new InfoDatosInt();
		this.centroCostoEjecuta=new InfoDatosInt();
		this.numResponsablesMismoServicio=0;
		this.numResponsablesMismoArticulo=0;
		this.numResponsablesFacturadosMismoServicio=0;
		this.numResponsablesFacturadosMismoArticulo=0;
		
		
		this.facturado=ConstantesBD.acronimoNo;
		
		this.codigoCups="";
		this.codigoCupsCx="";
		this.codigoDetalleCargo="";
		this.convenio="";
		this.esquemaTarifario="";
		this.cantidadCargada="";
		this.valorUnitarioTarifa="";
		this.valorUnitarioCargado="";
		this.valorTotalCargado="";
		this.porcentajeCargado="";
		this.porcentajeRecargo="";
		this.valorUnitarioRecargo="";
		this.porcentajeDcto="";
		this.valorUnitarioDcto="";
		this.valorUnitarioIva="";
		this.nroAutorizacion="";
		this.cargoPadre="";
		this.observacionesCargo="";
		this.usuarioModifica="";
		this.portatil =new InfoDatosString();
		this.detcxhonorarios=ConstantesBD.codigoNuncaValido;
		this.detasicxsalasmat=ConstantesBD.codigoNuncaValido;
		this.tipoContrato="";
		this.ingreso=ConstantesBD.codigoNuncaValido;
		this.tipoEntidadEjecuta="";
		this.codCentroAtencionIngreso=ConstantesBD.codigoNuncaValido;
		this.centroAtencionIngreso="";
		
		this.naturalezaPaciente=new String("");
		this.tipoPaciente=new String("");
		this.viaIngreso="";
		this.codNaturalezaPaciente=ConstantesBD.codigoNuncaValido;
		this.codTipoPaciente=new String("");
		this.codViaIngreso=ConstantesBD.codigoNuncaValido;
		this.nombrePaciente=new String("");
		this.tipoIdPaciente=new String("");
		this.numeroIdPaciente=new String("");
		this.codServicioCodManualEstandar=new String("");
		this.codProfesional=new String("");
		this.profesional=new String("");
		this.codConvenio=ConstantesBD.codigoNuncaValido;
		this.codigoPaciente=ConstantesBD.codigoNuncaValido;
		this.urgenteSolicitud = false;
		this.especialidadServicioSolicitud=new String("");
		this.especialidadSolicitadaProfesional= new String("");
		
		this.descArticulo="";
		this.unidadMedidaArticulo="";
		this.dosisArticulo="";
		this.frecuArticulo=ConstantesBD.codigoNuncaValido+"";
		this.viaArticulo="";
		this.duracionArticulo=ConstantesBD.codigoNuncaValido+"";
		this.nroDosisTotalArticulo=ConstantesBD.codigoNuncaValido+"";
		this.esMedicamento="";
		this.observaArticulo="";
		this.guardoArticuloServicio=false;
		
	}

	
	/**
	 * @return the detalleCargo
	 */
	public DtoDetalleCargo getDetalleCargo() 
	{
		return detalleCargo;
	}

	/**
	 * @param detalleCargo the detalleCargo to set
	 */
	public void setDetalleCargo(DtoDetalleCargo detalleCargo) 
	{
		this.detalleCargo = detalleCargo;
	}

	/**
	 * @return the articulo
	 */
	public InfoDatosString getArticulo() {
		return articulo;
	}

	/**
	 * @param articulo the articulo to set
	 */
	public void setArticulo(InfoDatosString articulo) {
		this.articulo = articulo;
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
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the cubierto
	 */
	public String getCubierto() {
		return cubierto;
	}

	/**
	 * @param cubierto the cubierto to set
	 */
	public void setCubierto(String cubierto) {
		this.cubierto = cubierto;
	}

	/**
	 * @return the cuenta
	 */
	public String getCuenta() {
		return cuenta;
	}

	/**
	 * @param cuenta the cuenta to set
	 */
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	/**
	 * @return the monto
	 */
	public String getMonto() {
		return monto;
	}

	/**
	 * @param monto the monto to set
	 */
	public void setMonto(String monto) {
		this.monto = monto;
	}

	/**
	 * @return the numeroSolicitud
	 */
	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	/**
	 * @return the paquetizada
	 */
	public String getPaquetizada() {
		return paquetizada;
	}

	/**
	 * @param paquetizada the paquetizada to set
	 */
	public void setPaquetizada(String paquetizada) {
		this.paquetizada = paquetizada;
	}

	/**
	 * @return the procentaje
	 */
	public String getProcentaje() {
		return procentaje;
	}

	public String getDetCxHonorarios() {
		return detCxHonorarios;
	}


	public void setDetCxHonorarios(String detCxHonorarios) {
		this.detCxHonorarios = detCxHonorarios;
	}


	public String getDetAsocioCxSalasMat() {
		return detAsocioCxSalasMat;
	}


	public void setDetAsocioCxSalasMat(String detAsocioCxSalasMat) {
		this.detAsocioCxSalasMat = detAsocioCxSalasMat;
	}


	/**
	 * @param procentaje the procentaje to set
	 */
	public void setProcentaje(String procentaje) {
		this.procentaje = procentaje;
	}

	/**
	 * @return the servicio
	 */
	public InfoDatosString getServicio() {
		return servicio;
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(InfoDatosString servicio) {
		this.servicio = servicio;
	}

	/**
	 * @return the solicitudPadre
	 */
	public String getSolicitudPadre() {
		return solicitudPadre;
	}

	/**
	 * @param solicitudPadre the solicitudPadre to set
	 */
	public void setSolicitudPadre(String solicitudPadre) {
		this.solicitudPadre = solicitudPadre;
	}

	/**
	 * @return the subCuenta
	 */
	public String getSubCuenta() {
		return subCuenta;
	}

	/**
	 * @param subCuenta the subCuenta to set
	 */
	public void setSubCuenta(String subCuenta) {
		this.subCuenta = subCuenta;
	}

	/**
	 * @return the tipoSolicitud
	 */
	public InfoDatosInt getTipoSolicitud() {
		return tipoSolicitud;
	}

	/**
	 * @param tipoSolicitud the tipoSolicitud to set
	 */
	public void setTipoSolicitud(InfoDatosInt tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}

	/**
	 * @return the valorCargoCalculado
	 */
	public String getValorCargoCalculado() {
		return valorCargoCalculado;
	}

	/**
	 * @param valorCargoCalculado the valorCargoCalculado to set
	 */
	public void setValorCargoCalculado(String valorCargoCalculado) {
		this.valorCargoCalculado = valorCargoCalculado;
	}

	/**
	 * @return the servicioCX
	 */
	public InfoDatosString getServicioCX() {
		return servicioCX;
	}

	/**
	 * @param servicioCX the servicioCX to set
	 */
	public void setServicioCX(InfoDatosString servicioCX) {
		this.servicioCX = servicioCX;
	}

	/**
	 * @return the tipoAsocio
	 */
	public InfoDatosInt getTipoAsocio() {
		return tipoAsocio;
	}

	/**
	 * @param tipoAsocio the tipoAsocio to set
	 */
	public void setTipoAsocio(InfoDatosInt tipoAsocio) {
		this.tipoAsocio = tipoAsocio;
	}

	/**
	 * @return the estadoCargo
	 */
	public InfoDatosInt getEstadoCargo() {
		return estadoCargo;
	}

	/**
	 * @param estadoCargo the estadoCargo to set
	 */
	public void setEstadoCargo(InfoDatosInt estadoCargo) {
		this.estadoCargo = estadoCargo;
	}

	/**
	 * @return the tipoDistribucion
	 */
	public InfoDatosString getTipoDistribucion() {
		return tipoDistribucion;
	}

	/**
	 * @param tipoDistribucion the tipoDistribucion to set
	 */
	public void setTipoDistribucion(InfoDatosString tipoDistribucion) {
		this.tipoDistribucion = tipoDistribucion;
	}

	/**
	 * @return the consecutivoSolicitud
	 */
	public String getConsecutivoSolicitud() {
		return consecutivoSolicitud;
	}

	/**
	 * @param consecutivoSolicitud the consecutivoSolicitud to set
	 */
	public void setConsecutivoSolicitud(String consecutivoSolicitud) {
		this.consecutivoSolicitud = consecutivoSolicitud;
	}

	/**
	 * @return the fechaSolicitud
	 */
	public String getFechaSolicitud() {
		return fechaSolicitud;
	}

	/**
	 * @param fechaSolicitud the fechaSolicitud to set
	 */
	public void setFechaSolicitud(String fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	/**
	 * @return the centroCostoEjecuta
	 */
	public InfoDatosInt getCentroCostoEjecuta() {
		return centroCostoEjecuta;
	}

	/**
	 * @param centroCostoEjecuta the centroCostoEjecuta to set
	 */
	public void setCentroCostoEjecuta(InfoDatosInt centroCostoEjecuta) {
		this.centroCostoEjecuta = centroCostoEjecuta;
	}

	/**
	 * @return the centroCostoSolicita
	 */
	public InfoDatosInt getCentroCostoSolicita() {
		return centroCostoSolicita;
	}

	/**
	 * @param centroCostoSolicita the centroCostoSolicita to set
	 */
	public void setCentroCostoSolicita(InfoDatosInt centroCostoSolicita) {
		this.centroCostoSolicita = centroCostoSolicita;
	}

	/**
	 * @return the numResponsablesFacturadosMismoArticulo
	 */
	public int getNumResponsablesFacturadosMismoArticulo() {
		return numResponsablesFacturadosMismoArticulo;
	}

	/**
	 * @param numResponsablesFacturadosMismoArticulo the numResponsablesFacturadosMismoArticulo to set
	 */
	public void setNumResponsablesFacturadosMismoArticulo(
			int numResponsablesFacturadosMismoArticulo) {
		this.numResponsablesFacturadosMismoArticulo = numResponsablesFacturadosMismoArticulo;
	}

	/**
	 * @return the numResponsablesFacturadosMismoServicio
	 */
	public int getNumResponsablesFacturadosMismoServicio() {
		return numResponsablesFacturadosMismoServicio;
	}

	/**
	 * @param numResponsablesFacturadosMismoServicio the numResponsablesFacturadosMismoServicio to set
	 */
	public void setNumResponsablesFacturadosMismoServicio(
			int numResponsablesFacturadosMismoServicio) {
		this.numResponsablesFacturadosMismoServicio = numResponsablesFacturadosMismoServicio;
	}

	/**
	 * @return the numResponsablesMismoArticulo
	 */
	public int getNumResponsablesMismoArticulo() {
		return numResponsablesMismoArticulo;
	}

	/**
	 * @param numResponsablesMismoArticulo the numResponsablesMismoArticulo to set
	 */
	public void setNumResponsablesMismoArticulo(int numResponsablesMismoArticulo) {
		this.numResponsablesMismoArticulo = numResponsablesMismoArticulo;
	}

	/**
	 * @return the numResponsablesMismoServicio
	 */
	public int getNumResponsablesMismoServicio() {
		return numResponsablesMismoServicio;
	}

	/**
	 * @param numResponsablesMismoServicio the numResponsablesMismoServicio to set
	 */
	public void setNumResponsablesMismoServicio(int numResponsablesMismoServicio) {
		this.numResponsablesMismoServicio = numResponsablesMismoServicio;
	}

	/**
	 * @return the cantidadCargada
	 */
	public String getCantidadCargada() {
		return cantidadCargada;
	}

	/**
	 * @param cantidadCargada the cantidadCargada to set
	 */
	public void setCantidadCargada(String cantidadCargada) {
		this.cantidadCargada = cantidadCargada;
	}

	/**
	 * @return the cargoPadre
	 */
	public String getCargoPadre() {
		return cargoPadre;
	}

	/**
	 * @param cargoPadre the cargoPadre to set
	 */
	public void setCargoPadre(String cargoPadre) {
		this.cargoPadre = cargoPadre;
	}

	/**
	 * @return the codigoDetalleCargo
	 */
	public String getCodigoDetalleCargo() {
		return codigoDetalleCargo;
	}

	/**
	 * @param codigoDetalleCargo the codigoDetalleCargo to set
	 */
	public void setCodigoDetalleCargo(String codigoDetalleCargo) {
		this.codigoDetalleCargo = codigoDetalleCargo;
	}

	/**
	 * @return the convenio
	 */
	public String getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	/**
	 * @return the esquemaTarifario
	 */
	public String getEsquemaTarifario() {
		return esquemaTarifario;
	}

	/**
	 * @param esquemaTarifario the esquemaTarifario to set
	 */
	public void setEsquemaTarifario(String esquemaTarifario) {
		this.esquemaTarifario = esquemaTarifario;
	}

	/**
	 * @return the nroAutorizacion
	 */
	public String getNroAutorizacion() {
		return nroAutorizacion;
	}

	/**
	 * @param nroAutorizacion the nroAutorizacion to set
	 */
	public void setNroAutorizacion(String nroAutorizacion) {
		this.nroAutorizacion = nroAutorizacion;
	}

	/**
	 * @return the observacionesCargo
	 */
	public String getObservacionesCargo() {
		return observacionesCargo;
	}

	/**
	 * @param observacionesCargo the observacionesCargo to set
	 */
	public void setObservacionesCargo(String observacionesCargo) {
		this.observacionesCargo = observacionesCargo;
	}

	/**
	 * @return the porcentajeCargado
	 */
	public String getPorcentajeCargado() {
		return porcentajeCargado;
	}

	/**
	 * @param porcentajeCargado the porcentajeCargado to set
	 */
	public void setPorcentajeCargado(String porcentajeCargado) {
		this.porcentajeCargado = porcentajeCargado;
	}

	/**
	 * @return the porcentajeDcto
	 */
	public String getPorcentajeDcto() {
		return porcentajeDcto;
	}

	/**
	 * @param porcentajeDcto the porcentajeDcto to set
	 */
	public void setPorcentajeDcto(String porcentajeDcto) {
		this.porcentajeDcto = porcentajeDcto;
	}

	/**
	 * @return the porcentajeRecargo
	 */
	public String getPorcentajeRecargo() {
		return porcentajeRecargo;
	}

	/**
	 * @param porcentajeRecargo the porcentajeRecargo to set
	 */
	public void setPorcentajeRecargo(String porcentajeRecargo) {
		this.porcentajeRecargo = porcentajeRecargo;
	}

	/**
	 * @return the valorTotalCargado
	 */
	public String getValorTotalCargado() {
		return valorTotalCargado;
	}

	/**
	 * @param valorTotalCargado the valorTotalCargado to set
	 */
	public void setValorTotalCargado(String valorTotalCargado) {
		this.valorTotalCargado = valorTotalCargado;
	}

	/**
	 * @return the valorUnitarioCargado
	 */
	public String getValorUnitarioCargado() {
		return valorUnitarioCargado;
	}

	/**
	 * @param valorUnitarioCargado the valorUnitarioCargado to set
	 */
	public void setValorUnitarioCargado(String valorUnitarioCargado) {
		this.valorUnitarioCargado = valorUnitarioCargado;
	}

	/**
	 * @return the valorUnitarioDcto
	 */
	public String getValorUnitarioDcto() {
		return valorUnitarioDcto;
	}

	/**
	 * @param valorUnitarioDcto the valorUnitarioDcto to set
	 */
	public void setValorUnitarioDcto(String valorUnitarioDcto) {
		this.valorUnitarioDcto = valorUnitarioDcto;
	}

	/**
	 * @return the valorUnitarioIva
	 */
	public String getValorUnitarioIva() {
		return valorUnitarioIva;
	}

	/**
	 * @param valorUnitarioIva the valorUnitarioIva to set
	 */
	public void setValorUnitarioIva(String valorUnitarioIva) {
		this.valorUnitarioIva = valorUnitarioIva;
	}

	/**
	 * @return the valorUnitarioRecargo
	 */
	public String getValorUnitarioRecargo() {
		return valorUnitarioRecargo;
	}

	/**
	 * @param valorUnitarioRecargo the valorUnitarioRecargo to set
	 */
	public void setValorUnitarioRecargo(String valorUnitarioRecargo) {
		this.valorUnitarioRecargo = valorUnitarioRecargo;
	}

	/**
	 * @return the valorUnitarioTarifa
	 */
	public String getValorUnitarioTarifa() {
		return valorUnitarioTarifa;
	}

	/**
	 * @param valorUnitarioTarifa the valorUnitarioTarifa to set
	 */
	public void setValorUnitarioTarifa(String valorUnitarioTarifa) {
		this.valorUnitarioTarifa = valorUnitarioTarifa;
	}
	
	/**
	 * Metodo que retorna la descripcion del articulo o servicio,segun aplique.
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public InfoDatosString getArtSer()
	{
		if(UtilidadTexto.isEmpty(this.servicio.getCodigo()))
		{
			return this.articulo;
		}
		else
		{
			if(this.tipoSolicitud.getCodigo()==ConstantesBD.codigoTipoSolicitudCirugia)
				return this.servicioCX;
		}
		return this.servicio;
	}

	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * @return the facturado
	 */
	public String getFacturado() {
		return facturado;
	}

	/**
	 * @param facturado the facturado to set
	 */
	public void setFacturado(String facturado) {
		this.facturado = facturado;
	}



	/**
	 * @return the portatil
	 */
	public InfoDatosString getPortatil() {
		return portatil;
	}

	/**
	 * @param portatil the portatil to set
	 */
	public void setPortatil(InfoDatosString portatil) {
		this.portatil = portatil;
	}

	/**
	 * 
	 * @return
	 */
	public int getDetcxhonorarios() 
	{
		return detcxhonorarios;
	}

	/**
	 * 
	 * @param detcxhonorarios
	 */
	public void setDetcxhonorarios(int detcxhonorarios) 
	{
		this.detcxhonorarios = detcxhonorarios;
	}

	/**
	 * 
	 * @return
	 */
	public int getDetasicxsalasmat() 
	{
		return detasicxsalasmat;
	}

	/**
	 * 
	 * @param detasicxsalasmat
	 */
	public void setDetasicxsalasmat(int detasicxsalasmat) 
	{
		this.detasicxsalasmat = detasicxsalasmat;
	}

	/**
	 * 
	 * @return
	 */
	public String getCodigoCups() {
		return codigoCups;
	}

	/**
	 * 
	 * @param codigoCups
	 */
	public void setCodigoCups(String codigoCups) {
		this.codigoCups = codigoCups;
	}

	/**
	 * 
	 * @return
	 */
	public String getCodigoCupsCx() {
		return codigoCupsCx;
	}

	/**
	 * 
	 * @param codigoCupsCx
	 */
	public void setCodigoCupsCx(String codigoCupsCx) {
		this.codigoCupsCx = codigoCupsCx;
	}


	public String getTipoContrato() {
		return tipoContrato;
	}


	public void setTipoContrato(String tipoContrato) {
		this.tipoContrato = tipoContrato;
	}


	public int getIngreso() {
		return ingreso;
	}


	public void setIngreso(int ingreso) {
		this.ingreso = ingreso;
	}


	public String getTipoEntidadEjecuta() {
		return tipoEntidadEjecuta;
	}


	public void setTipoEntidadEjecuta(String tipoEntidadEjecuta) {
		this.tipoEntidadEjecuta = tipoEntidadEjecuta;
	}


	public String getCentroAtencionIngreso() {
		return centroAtencionIngreso;
	}


	public void setCentroAtencionIngreso(String centroAtencionIngreso) {
		this.centroAtencionIngreso = centroAtencionIngreso;
	}


	public int getCodCentroAtencionIngreso() {
		return codCentroAtencionIngreso;
	}


	public void setCodCentroAtencionIngreso(int codCentroAtencionIngreso) {
		this.codCentroAtencionIngreso = codCentroAtencionIngreso;
	}


	public String getNaturalezaPaciente() {
		return naturalezaPaciente;
	}


	public void setNaturalezaPaciente(String naturalezaPaciente) {
		this.naturalezaPaciente = naturalezaPaciente;
	}


	public String getTipoPaciente() {
		return tipoPaciente;
	}


	public void setTipoPaciente(String tipoPaciente) {
		this.tipoPaciente = tipoPaciente;
	}


	public String getViaIngreso() {
		return viaIngreso;
	}


	public void setViaIngreso(String viaIngreso) {
		this.viaIngreso = viaIngreso;
	}


	public int getCodNaturalezaPaciente() {
		return codNaturalezaPaciente;
	}


	public void setCodNaturalezaPaciente(int codNaturalezaPaciente) {
		this.codNaturalezaPaciente = codNaturalezaPaciente;
	}


	public String getCodTipoPaciente() {
		return codTipoPaciente;
	}


	public void setCodTipoPaciente(String codTipoPaciente) {
		this.codTipoPaciente = codTipoPaciente;
	}


	public int getCodViaIngreso() {
		return codViaIngreso;
	}


	public void setCodViaIngreso(int codViaIngreso) {
		this.codViaIngreso = codViaIngreso;
	}


	public String getNombrePaciente() {
		return nombrePaciente;
	}


	public void setNombrePaciente(String nombrePaciente) {
		this.nombrePaciente = nombrePaciente;
	}


	public String getTipoIdPaciente() {
		return tipoIdPaciente;
	}


	public void setTipoIdPaciente(String tipoIdPaciente) {
		this.tipoIdPaciente = tipoIdPaciente;
	}


	public String getNumeroIdPaciente() {
		return numeroIdPaciente;
	}


	public void setNumeroIdPaciente(String numeroIdPaciente) {
		this.numeroIdPaciente = numeroIdPaciente;
	}


	public String getCodServicioCodManualEstandar() {
		return codServicioCodManualEstandar;
	}


	public void setCodServicioCodManualEstandar(String codServicioCodManualEstandar) {
		this.codServicioCodManualEstandar = codServicioCodManualEstandar;
	}


	public String getCodProfesional() {
		return codProfesional;
	}


	public void setCodProfesional(String codProfesional) {
		this.codProfesional = codProfesional;
	}


	public String getProfesional() {
		return profesional;
	}


	public void setProfesional(String profesional) {
		this.profesional = profesional;
	}


	public int getCodConvenio() {
		return codConvenio;
	}


	public void setCodConvenio(int codConvenio) {
		this.codConvenio = codConvenio;
	}


	public int getCodigoPaciente() {
		return codigoPaciente;
	}


	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}


	public String getHoraSolicitud() {
		return horaSolicitud;
	}


	public void setHoraSolicitud(String horaSolicitud) {
		this.horaSolicitud = horaSolicitud;
	}


	public boolean isUrgenteSolicitud() {
		return urgenteSolicitud;
	}


	public void setUrgenteSolicitud(boolean urgenteSolicitud) {
		this.urgenteSolicitud = urgenteSolicitud;
	}


	public String getEspecialidadServicioSolicitud() {
		return especialidadServicioSolicitud;
	}


	public void setEspecialidadServicioSolicitud(
			String especialidadServicioSolicitud) {
		this.especialidadServicioSolicitud = especialidadServicioSolicitud;
	}


	/**
	 * @return the especialidadSolicitadaProfesional
	 */
	public String getEspecialidadSolicitadaProfesional() {
		return especialidadSolicitadaProfesional;
	}


	/**
	 * @param especialidadSolicitadaProfesional the especialidadSolicitadaProfesional to set
	 */
	public void setEspecialidadSolicitadaProfesional(
			String especialidadSolicitadaProfesional) {
		this.especialidadSolicitadaProfesional = especialidadSolicitadaProfesional;
	}

	/**
	 * 
	 * @return
	 */
	public double getValorTotalCargoCalculoCompleto()
	{
		double tarifa= (Utilidades.convertirADouble(this.getValorUnitarioTarifa())>=0?Utilidades.convertirADouble(this.getValorUnitarioTarifa()):0);
		double cantidad= (Utilidades.convertirADouble(this.getCantidadCargada())>=0?Utilidades.convertirADouble(this.getCantidadCargada()):0);
		double recargo= (Utilidades.convertirADouble(this.getValorUnitarioRecargo())>=0?Utilidades.convertirADouble(this.getValorUnitarioRecargo()):0);
		if(recargo<=0)
		{	
			double porcentajeRecargo= (Utilidades.convertirADouble(this.getPorcentajeRecargo())>=0?Utilidades.convertirADouble(this.getPorcentajeRecargo()):0);
			recargo=(porcentajeRecargo>0 && tarifa>0)?((tarifa*porcentajeRecargo)/100):0;
		}	
		double descuento= (Utilidades.convertirADouble(this.getValorUnitarioDcto())>=0?Utilidades.convertirADouble(this.getValorUnitarioDcto()):0);
		if(descuento<=0)
		{	
			double porcentajeDescuento= (Utilidades.convertirADouble(this.getPorcentajeDcto())>=0?Utilidades.convertirADouble(this.getPorcentajeDcto()):0);
			descuento=(porcentajeDescuento>0 && tarifa>0)?((tarifa*porcentajeDescuento)/100):0;
		}	
		return ((tarifa+recargo-descuento)*cantidad)>0?((tarifa+recargo-descuento)*cantidad):0;
	}


	public String getDescArticulo() {
		return descArticulo;
	}


	public void setDescArticulo(String descArticulo) {
		this.descArticulo = descArticulo;
	}


	public String getUnidadMedidaArticulo() {
		return unidadMedidaArticulo;
	}


	public void setUnidadMedidaArticulo(String unidadMedidaArticulo) {
		this.unidadMedidaArticulo = unidadMedidaArticulo;
	}


	public String getDosisArticulo() {
		return dosisArticulo;
	}


	public void setDosisArticulo(String dosisArticulo) {
		this.dosisArticulo = dosisArticulo;
	}


	public String getFrecuArticulo() {
		return frecuArticulo;
	}


	public void setFrecuArticulo(String frecuArticulo) {
		this.frecuArticulo = frecuArticulo;
	}


	public void setTipoFrecueArticulo(String tipoFrecueArticulo) {
		this.tipoFrecueArticulo = tipoFrecueArticulo;
	}


	public String getTipoFrecueArticulo() {
		return tipoFrecueArticulo;
	}


	public String getViaArticulo() {
		return viaArticulo;
	}


	public void setViaArticulo(String viaArticulo) {
		this.viaArticulo = viaArticulo;
	}


	public String getDuracionArticulo() {
		return duracionArticulo;
	}


	public void setDuracionArticulo(String duracionArticulo) {
		this.duracionArticulo = duracionArticulo;
	}


	public String getNroDosisTotalArticulo() {
		return nroDosisTotalArticulo;
	}


	public void setNroDosisTotalArticulo(String nroDosisTotalArticulo) {
		this.nroDosisTotalArticulo = nroDosisTotalArticulo;
	}


	public String getEsMedicamento() {
		return esMedicamento;
	}


	public void setEsMedicamento(String esMedicamento) {
		this.esMedicamento = esMedicamento;
	}


	public String getObservaArticulo() {
		return observaArticulo;
	}


	public void setObservaArticulo(String observaArticulo) {
		this.observaArticulo = observaArticulo;
	}


	public ArrayList<DtoSolicitudesSubCuenta> getAgrupaListadoAutoriPendEntSub() {
		return agrupaListadoAutoriPendEntSub;
	}


	public void setAgrupaListadoAutoriPendEntSub(
			ArrayList<DtoSolicitudesSubCuenta> agrupaListadoAutoriPendEntSub) {
		this.agrupaListadoAutoriPendEntSub = agrupaListadoAutoriPendEntSub;
	}


	public void setGuardoArticuloServicio(boolean guardoArticuloServicio) {
		this.guardoArticuloServicio = guardoArticuloServicio;
	}


	public boolean isGuardoArticuloServicio() {
		return guardoArticuloServicio;
	}


	public void setNaturalezaArticulo(String naturalezaArticulo) {
		this.naturalezaArticulo = naturalezaArticulo;
	}


	public String getNaturalezaArticulo() {
		return naturalezaArticulo;
	}


	public int getFinalidadSolicitud() {
		return finalidadSolicitud;
	}


	public void setFinalidadSolicitud(int finalidadSolicitud) {
		this.finalidadSolicitud = finalidadSolicitud;
	}

}
