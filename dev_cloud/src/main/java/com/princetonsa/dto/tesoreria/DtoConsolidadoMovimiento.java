package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.util.ArrayList;

import util.adjuntos.DTOArchivoAdjunto;

import com.servinte.axioma.mundo.impl.tesoreria.ETipoMovimiento;
import com.servinte.axioma.orm.MovimientosCaja;

/**
 * Clase usada para:
 * 
 *  - Mostrar ultimo cierre de la caja
 *	- Realizar aceptaciones
 *	- Realizar solicitudes
 *
 * @author Jorge Armando Agudelo Quintero
 * @anexo 226
 */
public class DtoConsolidadoMovimiento implements Serializable{


	private static final long serialVersionUID = 1L;
	
	/**
	 * Enumeraci&oacute;n que indica el tipo de Movimiento del DTO.
	 */
	private  ETipoMovimiento eTipoMovimiento ;
	
	private ArrayList<DtoReciboDevolucion> 	reciboCajaDTOs;
	private ArrayList<DtoReciboDevolucion> 	devolucionReciboCajaDTOs;
	private ArrayList<DtoTrasladoCaja> 		trasladoCajaDTOs;
	private ArrayList<DtoEntregaCaja> 		entregaTransportadoraValoresDTOs;
	private ArrayList<DtoEntregaCaja> 		entregaCajaMayorPrincipalDTOs;
	private ArrayList<DtoCuadreCaja> 		cuadreCajaDTOs;
	private ArrayList<DtoTotalesDocumento> 	totalesDocumentoDTOs;
	private ArrayList<DtoFaltanteSobrante>	faltanteSobranteDTOs;
	
	private DtoConsultaTrasladoRecaudoMayorEnCierre trasladoCajaRecaudoEnCierre;
	private DtoConsultaTrasladoRecaudoMayorEnCierre trasladoCajaMayorEnCierre;
	
	private ArrayList<DtoTotalesParciales> totalesParcialesTrasladosDTOs;
	private ArrayList<DtoTotalesParciales> totalesParcialesEntrTransDTOs;
	private ArrayList<DtoTotalesParciales> totalesParcialesEntrCajaDTOs;
	private ArrayList<DtoTotalesParciales> totalesParcialesFaltanteSobranteDTOs;
	
	private double totalRecibosCaja;
	private double totalDevolRecibosCaja;
	private double totalTrasladosCajaRecibido;
	private double totalTrasladosCajaFaltante;
	private double totalTrasladosCajaTrasladado;
	private double totalEntregaTransportadoraCajaSistema;
	private double totalEntregaTransportadoraCajaFaltante;
	private double totalEntregaTransportadoraCajaEntregado;
	private double totalEntregaCajaMayor;
	
	private double totalTrasladoCajaRecaudoEnCierre;
	private double totalTrasladoCajaMayorEnCierre;
	
	/**
	 * Atributo que contiene el número total de documentos generados en el arqueo
	 */
	private int numeroTotalDocumentos;
	
	/**
	 * Inc 2067
	 * Atributo para contener el valor de la base
	 * @author DiaRuiPe
	 */
	private double valorBase;
	
	
	/**
	 * Manejo de los archivos adjuntos del movimiento
	 */
	private ArrayList<DTOArchivoAdjunto> archivosAdjuntosMovimiento;
	
	/**
	 *  MovimientoCajas
	 */
	private MovimientosCaja movimientosCaja;
	
	/**
	 * Constructor de la clase. Se inicializan los atributos
	 */
	public DtoConsolidadoMovimiento() {
		
		
		reciboCajaDTOs 					= new ArrayList<DtoReciboDevolucion>();
		devolucionReciboCajaDTOs 		= new ArrayList<DtoReciboDevolucion>();
		trasladoCajaDTOs 				= new ArrayList<DtoTrasladoCaja>();
		entregaCajaMayorPrincipalDTOs 	= new ArrayList<DtoEntregaCaja>();
		entregaTransportadoraValoresDTOs= new ArrayList<DtoEntregaCaja>();
		cuadreCajaDTOs 					= new ArrayList<DtoCuadreCaja>();
		totalesDocumentoDTOs 			= new ArrayList<DtoTotalesDocumento>();
		faltanteSobranteDTOs 			= new ArrayList<DtoFaltanteSobrante>();
		
		trasladoCajaRecaudoEnCierre = new DtoConsultaTrasladoRecaudoMayorEnCierre();
		trasladoCajaMayorEnCierre 	= new DtoConsultaTrasladoRecaudoMayorEnCierre();
		
		totalesParcialesTrasladosDTOs 			= new ArrayList<DtoTotalesParciales>();
		totalesParcialesEntrTransDTOs 			= new ArrayList<DtoTotalesParciales>();
		totalesParcialesEntrCajaDTOs 			= new ArrayList<DtoTotalesParciales>();
		totalesParcialesFaltanteSobranteDTOs	= new ArrayList<DtoTotalesParciales>();
		
		totalRecibosCaja 						= 0;
		totalDevolRecibosCaja 					= 0;
		totalTrasladosCajaRecibido 				= 0;
		totalTrasladosCajaFaltante 				= 0;
		totalTrasladosCajaTrasladado 			= 0;
		totalEntregaTransportadoraCajaSistema 	= 0;
		totalEntregaTransportadoraCajaFaltante 	= 0;
		totalEntregaTransportadoraCajaEntregado = 0;
		totalEntregaCajaMayor 					= 0;
		
		totalTrasladoCajaRecaudoEnCierre		= 0;
		totalTrasladoCajaMayorEnCierre			= 0;
		
		setNumeroTotalDocumentos(0);
		setValorBase(0);
	}


	/**
	 * @return the eTipoMovimiento
	 */
	public ETipoMovimiento getETipoMovimiento() {
		return eTipoMovimiento;
	}


	/**
	 * @param eTipoMovimiento the eTipoMovimiento to set
	 */
	public void setETipoMovimiento(ETipoMovimiento eTipoMovimiento) {
		this.eTipoMovimiento = eTipoMovimiento;
	}


	/**
	 * @return the reciboCajaDTOs
	 */
	public ArrayList<DtoReciboDevolucion> getReciboCajaDTOs() {
		return reciboCajaDTOs;
	}


	/**
	 * @param reciboCajaDTOs the reciboCajaDTOs to set
	 */
	public void setReciboCajaDTOs(ArrayList<DtoReciboDevolucion> reciboCajaDTOs) {
		this.reciboCajaDTOs = reciboCajaDTOs;
	}


	/**
	 * @return the devolucionReciboCajaDTOs
	 */
	public ArrayList<DtoReciboDevolucion> getDevolucionReciboCajaDTOs() {
		return devolucionReciboCajaDTOs;
	}


	/**
	 * @param devolucionReciboCajaDTOs the devolucionReciboCajaDTOs to set
	 */
	public void setDevolucionReciboCajaDTOs(
			ArrayList<DtoReciboDevolucion> devolucionReciboCajaDTOs) {
		this.devolucionReciboCajaDTOs = devolucionReciboCajaDTOs;
	}


	/**
	 * @return the trasladoCajaDTOs
	 */
	public ArrayList<DtoTrasladoCaja> getTrasladoCajaDTOs() {
		return trasladoCajaDTOs;
	}


	/**
	 * @param trasladoCajaDTOs the trasladoCajaDTOs to set
	 */
	public void setTrasladoCajaDTOs(ArrayList<DtoTrasladoCaja> trasladoCajaDTOs) {
		this.trasladoCajaDTOs = trasladoCajaDTOs;
	}


	/**
	 * @return the entregaTransportadoraValoresDTOs
	 */
	public ArrayList<DtoEntregaCaja> getEntregaTransportadoraValoresDTOs() {
		return entregaTransportadoraValoresDTOs;
	}


	/**
	 * @param entregaTransportadoraValoresDTOs the entregaTransportadoraValoresDTOs to set
	 */
	public void setEntregaTransportadoraValoresDTOs(
			ArrayList<DtoEntregaCaja> entregaTransportadoraValoresDTOs) {
		this.entregaTransportadoraValoresDTOs = entregaTransportadoraValoresDTOs;
	}


	/**
	 * @return the entregaCajaMayorPrincipalDTOs
	 */
	public ArrayList<DtoEntregaCaja> getEntregaCajaMayorPrincipalDTOs() {
		return entregaCajaMayorPrincipalDTOs;
	}


	/**
	 * @param entregaCajaMayorPrincipalDTOs the entregaCajaMayorPrincipalDTOs to set
	 */
	public void setEntregaCajaMayorPrincipalDTOs(
			ArrayList<DtoEntregaCaja> entregaCajaMayorPrincipalDTOs) {
		this.entregaCajaMayorPrincipalDTOs = entregaCajaMayorPrincipalDTOs;
	}


	/**
	 * @return the cuadreCajaDTOs
	 */
	public ArrayList<DtoCuadreCaja> getCuadreCajaDTOs() {
		return cuadreCajaDTOs;
	}


	/**
	 * @param cuadreCajaDTOs the cuadreCajaDTOs to set
	 */
	public void setCuadreCajaDTOs(ArrayList<DtoCuadreCaja> cuadreCajaDTOs) {
		this.cuadreCajaDTOs = cuadreCajaDTOs;
	}


	/**
	 * @return the totalesDocumentoDTOs
	 */
	public ArrayList<DtoTotalesDocumento> getTotalesDocumentoDTOs() {
		return totalesDocumentoDTOs;
	}


	/**
	 * @param totalesDocumentoDTOs the totalesDocumentoDTOs to set
	 */
	public void setTotalesDocumentoDTOs(
			ArrayList<DtoTotalesDocumento> totalesDocumentoDTOs) {
		this.totalesDocumentoDTOs = totalesDocumentoDTOs;
	}


	/**
	 * @return the faltanteSobranteDTOs
	 */
	public ArrayList<DtoFaltanteSobrante> getFaltanteSobranteDTOs() {
		return faltanteSobranteDTOs;
	}


	/**
	 * @param faltanteSobranteDTOs the faltanteSobranteDTOs to set
	 */
	public void setFaltanteSobranteDTOs(
			ArrayList<DtoFaltanteSobrante> faltanteSobranteDTOs) {
		this.faltanteSobranteDTOs = faltanteSobranteDTOs;
	}


	/**
	 * @return the totalesParcialesTrasladosDTOs
	 */
	public ArrayList<DtoTotalesParciales> getTotalesParcialesTrasladosDTOs() {
		return totalesParcialesTrasladosDTOs;
	}


	/**
	 * @param totalesParcialesTrasladosDTOs the totalesParcialesTrasladosDTOs to set
	 */
	public void setTotalesParcialesTrasladosDTOs(
			ArrayList<DtoTotalesParciales> totalesParcialesTrasladosDTOs) {
		this.totalesParcialesTrasladosDTOs = totalesParcialesTrasladosDTOs;
	}


	/**
	 * @return the totalesParcialesEntrTransDTOs
	 */
	public ArrayList<DtoTotalesParciales> getTotalesParcialesEntrTransDTOs() {
		return totalesParcialesEntrTransDTOs;
	}


	/**
	 * @param totalesParcialesEntrTransDTOs the totalesParcialesEntrTransDTOs to set
	 */
	public void setTotalesParcialesEntrTransDTOs(
			ArrayList<DtoTotalesParciales> totalesParcialesEntrTransDTOs) {
		this.totalesParcialesEntrTransDTOs = totalesParcialesEntrTransDTOs;
	}


	/**
	 * @return the totalesParcialesEntrCajaDTOs
	 */
	public ArrayList<DtoTotalesParciales> getTotalesParcialesEntrCajaDTOs() {
		return totalesParcialesEntrCajaDTOs;
	}


	/**
	 * @param totalesParcialesEntrCajaDTOs the totalesParcialesEntrCajaDTOs to set
	 */
	public void setTotalesParcialesEntrCajaDTOs(
			ArrayList<DtoTotalesParciales> totalesParcialesEntrCajaDTOs) {
		this.totalesParcialesEntrCajaDTOs = totalesParcialesEntrCajaDTOs;
	}


	/**
	 * @return the totalesParcialesFaltanteSobranteDTOs
	 */
	public ArrayList<DtoTotalesParciales> getTotalesParcialesFaltanteSobranteDTOs() {
		return totalesParcialesFaltanteSobranteDTOs;
	}


	/**
	 * @param totalesParcialesFaltanteSobranteDTOs the totalesParcialesFaltanteSobranteDTOs to set
	 */
	public void setTotalesParcialesFaltanteSobranteDTOs(
			ArrayList<DtoTotalesParciales> totalesParcialesFaltanteSobranteDTOs) {
		this.totalesParcialesFaltanteSobranteDTOs = totalesParcialesFaltanteSobranteDTOs;
	}


	/**
	 * @return the totalRecibosCaja
	 */
	public double getTotalRecibosCaja() {
		return totalRecibosCaja;
	}


	/**
	 * @param totalRecibosCaja the totalRecibosCaja to set
	 */
	public void setTotalRecibosCaja(double totalRecibosCaja) {
		this.totalRecibosCaja = totalRecibosCaja;
	}


	/**
	 * @return the totalDevolRecibosCaja
	 */
	public double getTotalDevolRecibosCaja() {
		return totalDevolRecibosCaja;
	}


	/**
	 * @param totalDevolRecibosCaja the totalDevolRecibosCaja to set
	 */
	public void setTotalDevolRecibosCaja(double totalDevolRecibosCaja) {
		this.totalDevolRecibosCaja = totalDevolRecibosCaja;
	}


	/**
	 * @return the totalTrasladosCajaRecibido
	 */
	public double getTotalTrasladosCajaRecibido() {
		return totalTrasladosCajaRecibido;
	}


	/**
	 * @param totalTrasladosCajaRecibido the totalTrasladosCajaRecibido to set
	 */
	public void setTotalTrasladosCajaRecibido(double totalTrasladosCajaRecibido) {
		this.totalTrasladosCajaRecibido = totalTrasladosCajaRecibido;
	}


	/**
	 * @return the totalTrasladosCajaFaltante
	 */
	public double getTotalTrasladosCajaFaltante() {
		return totalTrasladosCajaFaltante;
	}


	/**
	 * @param totalTrasladosCajaFaltante the totalTrasladosCajaFaltante to set
	 */
	public void setTotalTrasladosCajaFaltante(double totalTrasladosCajaFaltante) {
		this.totalTrasladosCajaFaltante = totalTrasladosCajaFaltante;
	}


	/**
	 * @return the totalTrasladosCajaTrasladado
	 */
	public double getTotalTrasladosCajaTrasladado() {
		return totalTrasladosCajaTrasladado;
	}


	/**
	 * @param totalTrasladosCajaTrasladado the totalTrasladosCajaTrasladado to set
	 */
	public void setTotalTrasladosCajaTrasladado(double totalTrasladosCajaTrasladado) {
		this.totalTrasladosCajaTrasladado = totalTrasladosCajaTrasladado;
	}


	/**
	 * @return the totalEntregaTransportadoraCajaSistema
	 */
	public double getTotalEntregaTransportadoraCajaSistema() {
		return totalEntregaTransportadoraCajaSistema;
	}


	/**
	 * @param totalEntregaTransportadoraCajaSistema the totalEntregaTransportadoraCajaSistema to set
	 */
	public void setTotalEntregaTransportadoraCajaSistema(
			double totalEntregaTransportadoraCajaSistema) {
		this.totalEntregaTransportadoraCajaSistema = totalEntregaTransportadoraCajaSistema;
	}


	/**
	 * @return the totalEntregaTransportadoraCajaFaltante
	 */
	public double getTotalEntregaTransportadoraCajaFaltante() {
		return totalEntregaTransportadoraCajaFaltante;
	}


	/**
	 * @param totalEntregaTransportadoraCajaFaltante the totalEntregaTransportadoraCajaFaltante to set
	 */
	public void setTotalEntregaTransportadoraCajaFaltante(
			double totalEntregaTransportadoraCajaFaltante) {
		this.totalEntregaTransportadoraCajaFaltante = totalEntregaTransportadoraCajaFaltante;
	}


	/**
	 * @return the totalEntregaTransportadoraCajaEntregado
	 */
	public double getTotalEntregaTransportadoraCajaEntregado() {
		return totalEntregaTransportadoraCajaEntregado;
	}


	/**
	 * @param totalEntregaTransportadoraCajaEntregado the totalEntregaTransportadoraCajaEntregado to set
	 */
	public void setTotalEntregaTransportadoraCajaEntregado(
			double totalEntregaTransportadoraCajaEntregado) {
		this.totalEntregaTransportadoraCajaEntregado = totalEntregaTransportadoraCajaEntregado;
	}


	/**
	 * @return the totalEntregaCajaMayor
	 */
	public double getTotalEntregaCajaMayor() {
		return totalEntregaCajaMayor;
	}


	/**
	 * @param totalEntregaCajaMayor the totalEntregaCajaMayor to set
	 */
	public void setTotalEntregaCajaMayor(double totalEntregaCajaMayor) {
		this.totalEntregaCajaMayor = totalEntregaCajaMayor;
	}


	/**
	 * @return the movimientosCaja
	 */
	public MovimientosCaja getMovimientosCaja() {
		return movimientosCaja;
	}


	/**
	 * @param movimientosCaja the movimientosCaja to set
	 */
	public void setMovimientosCaja(MovimientosCaja movimientosCaja) {
		this.movimientosCaja = movimientosCaja;
	}


	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo archivosAdjuntosMovimiento
	 * 
	 * @return  Retorna la variable archivosAdjuntosMovimiento
	 */
	public ArrayList<DTOArchivoAdjunto> getArchivosAdjuntosMovimiento() {
		return archivosAdjuntosMovimiento;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo archivosAdjuntosMovimiento
	 * 
	 * @return  Retorna la variable archivosAdjuntosMovimiento
	 */
	public DTOArchivoAdjunto getArchivoAdjuntoMovimiento(int index) {
		if(index>=archivosAdjuntosMovimiento.size())
		{
			for(int i=archivosAdjuntosMovimiento.size(); i<=index; i++)
			{
				archivosAdjuntosMovimiento.add(new DTOArchivoAdjunto());
			}
		}
		return archivosAdjuntosMovimiento.get(index);
	}


	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo archivosAdjuntosMovimiento
	 * 
	 * @param  valor para el atributo archivosAdjuntosMovimiento 
	 */
	public void setArchivosAdjuntosMovimiento(
			ArrayList<DTOArchivoAdjunto> archivosAdjuntosMovimiento) {
		this.archivosAdjuntosMovimiento = archivosAdjuntosMovimiento;
	}

	/**
	 * Obtiene el n&uacute;mero de documentos adjuntos
	 * @return int con la cantidad de documentos
	 * @author Yennifer Guerrero
	 */
	public int getNumeroDocumentosAdjuntos()
	{
		if(archivosAdjuntosMovimiento==null)
		{
			return 0;
		}
		return archivosAdjuntosMovimiento.size();	
	}


	/**
	 * Método que retorna el valor del atributo trasladoCajaRecaudoEnCierre
	 * @return trasladoCajaRecaudoEnCierre
	 */
	public DtoConsultaTrasladoRecaudoMayorEnCierre getTrasladoCajaRecaudoEnCierre() {
		return trasladoCajaRecaudoEnCierre;
	}


	/**
	 * Método que almacena el valor del atributo the trasladoCajaRecaudoEnCierre
	 * @param trasladoCajaRecaudoEnCierre
	 */
	public void setTrasladoCajaRecaudoEnCierre(
			DtoConsultaTrasladoRecaudoMayorEnCierre trasladoCajaRecaudoEnCierre) {
		this.trasladoCajaRecaudoEnCierre = trasladoCajaRecaudoEnCierre;
	}


	/**
	 * Método que retorna el valor del atributo trasladoCajaMayorEnCierre
	 * @return trasladoCajaMayorEnCierre
	 */
	public DtoConsultaTrasladoRecaudoMayorEnCierre getTrasladoCajaMayorEnCierre() {
		return trasladoCajaMayorEnCierre;
	}


	/**
	 * Método que almacena el valor del atributo the trasladoCajaMayorEnCierre
	 * @param trasladoCajaMayorEnCierre
	 */
	public void setTrasladoCajaMayorEnCierre(
			DtoConsultaTrasladoRecaudoMayorEnCierre trasladoCajaMayorEnCierre) {
		this.trasladoCajaMayorEnCierre = trasladoCajaMayorEnCierre;
	}


	/**
	 * Método que retorna el valor del atributo totalTrasladoCajaRecaudoEnCierre
	 * @return totalTrasladoCajaRecaudoEnCierre
	 */
	public double getTotalTrasladoCajaRecaudoEnCierre() {
		return totalTrasladoCajaRecaudoEnCierre;
	}


	/**
	 * Método que almacena el valor del atributo the totalTrasladoCajaRecaudoEnCierre
	 * @param totalTrasladoCajaRecaudoEnCierre
	 */
	public void setTotalTrasladoCajaRecaudoEnCierre(
			double totalTrasladoCajaRecaudoEnCierre) {
		this.totalTrasladoCajaRecaudoEnCierre = totalTrasladoCajaRecaudoEnCierre;
	}


	/**
	 * Método que retorna el valor del atributo totalTrasladoCajaMayorEnCierre
	 * @return totalTrasladoCajaMayorEnCierre
	 */
	public double getTotalTrasladoCajaMayorEnCierre() {
		return totalTrasladoCajaMayorEnCierre;
	}


	/**
	 * Método que almacena el valor del atributo the totalTrasladoCajaMayorEnCierre
	 * @param totalTrasladoCajaMayorEnCierre
	 */
	public void setTotalTrasladoCajaMayorEnCierre(
			double totalTrasladoCajaMayorEnCierre) {
		this.totalTrasladoCajaMayorEnCierre = totalTrasladoCajaMayorEnCierre;
	}


	public void setValorBase(double valorBase) {
		this.valorBase = valorBase;
	}


	public double getValorBase() {
		return valorBase;
	}


	/**
	 * @param numeroTotalDocumentos the numeroTotalDocumentos to set
	 */
	public void setNumeroTotalDocumentos(int numeroTotalDocumentos) {
		this.numeroTotalDocumentos = numeroTotalDocumentos;
	}


	/**
	 * @return the numeroTotalDocumentos
	 */
	public int getNumeroTotalDocumentos() {
		return numeroTotalDocumentos;
	}
}
