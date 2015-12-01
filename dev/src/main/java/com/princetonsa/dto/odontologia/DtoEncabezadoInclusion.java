
package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.odontologia.InfoDefinirSolucitudDsctOdon;

import com.servinte.axioma.orm.IncluPresuConvenio;
import com.servinte.axioma.orm.IncluPresuEncabezado;
import com.servinte.axioma.orm.IncluProgramaConvenio;
import com.servinte.axioma.orm.IncluServicioConvenio;
import com.servinte.axioma.orm.Personas;

/**
 * Clase que contiene la información del Encabezado de un registro del proceso
 * de contratación de la inclusión
 * 
 * @author Jorge Armando Agudelo Quintero
 */
public class DtoEncabezadoInclusion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Encabezado de la Inclusión
	 */
	private IncluPresuEncabezado incluPresuEncabezado;
	
	/**
	 * Totales del proceso de la contratación de la inclusión
	 */
	private DtoTotalesContratarInclusion totalesContratarInclusion;
	
	/**
	 * Solicitud de descuento
	 */
	private InfoDefinirSolucitudDsctOdon solicitudDescuento;
	
	
	/**
	 * Constructor
	 */
	public DtoEncabezadoInclusion() {
		
		incluPresuEncabezado = new IncluPresuEncabezado();
	
		totalesContratarInclusion = new DtoTotalesContratarInclusion();
		
		solicitudDescuento = new InfoDefinirSolucitudDsctOdon();
	}


	/**
	 * @return the incluPresuEncabezado
	 */
	public IncluPresuEncabezado getIncluPresuEncabezado() {
		return incluPresuEncabezado;
	}


	/**
	 * @param incluPresuEncabezado the incluPresuEncabezado to set
	 */
	public void setIncluPresuEncabezado(IncluPresuEncabezado incluPresuEncabezado) {
		this.incluPresuEncabezado = incluPresuEncabezado;
	}


	/**
	 * @return the totalesContratarInclusion
	 */
	public DtoTotalesContratarInclusion getTotalesContratarInclusion() {
		return totalesContratarInclusion;
	}


	/**
	 * @param totalesContratarInclusion the totalesContratarInclusion to set
	 */
	public void setTotalesContratarInclusion(
			DtoTotalesContratarInclusion totalesContratarInclusion) {
		this.totalesContratarInclusion = totalesContratarInclusion;
	}


	/**
	 * @return the solicitudDescuento
	 */
	public InfoDefinirSolucitudDsctOdon getSolicitudDescuento() {
		return solicitudDescuento;
	}


	/**
	 * @param solicitudDescuento the solicitudDescuento to set
	 */
	public void setSolicitudDescuento(
			InfoDefinirSolucitudDsctOdon solicitudDescuento) {
		this.solicitudDescuento = solicitudDescuento;
	}
	

	/**
	 * Método que reinicializa los totales
	 */
	public void resetTotales (){
		
		this.totalesContratarInclusion = new DtoTotalesContratarInclusion();
	}
	


	/**
	 * Método que se encarga de totalizar todos los valores del registro del
	 * proceso de contratación de las inclusiones, cuando se carga un registro
	 * del proceso de Inclusión desde el encabezado.
	 */
	public void cargarTotalesInclusion() {
		
		totalesContratarInclusion.setTotalInclusiones(calcularValorTotalInclusion());
		
		totalesContratarInclusion.setDescuento(cargarDescuentoOdontologico());
		
		totalesContratarInclusion.setTotalInclusionesAContratar(calcularTotalInclusionesAContratar());
	
	}
	

	
	/**
	 * Método que se encarga de recalcular los valores para
	 * contratar , obtendiendo de nuevo el descuento y utilizando
	 * la base previamente calculada.
	 */
	public void recalcularTotalesConDescuentoInclusion() {
		
		totalesContratarInclusion.setDescuento(cargarDescuentoOdontologico());
		
		totalesContratarInclusion.setTotalInclusionesAContratar(calcularTotalInclusionesAContratar());
	}
	


	/**
	 * Metodo que devuelve el valor el valor total a contratar
	 * por la solicitud de inclusión
	 * 
	 * @return
	 */
	private BigDecimal calcularTotalInclusionesAContratar() {
		
		return this.totalesContratarInclusion.getTotalInclusiones().subtract(this.totalesContratarInclusion.getDescuento());
	}


	/**
	 * Método que toma el valor del descuento odontológico
	 * Autorizado de la solicitud de descuento
	 * 
	 * @return
	 */
	private BigDecimal cargarDescuentoOdontologico() {
		
		if(this.solicitudDescuento!=null && (
				solicitudDescuento.getAcronimoEstadoSolicitud().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoAutorizado)
				|| solicitudDescuento.getAcronimoEstadoSolicitud().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoContratado))){
			
			return solicitudDescuento.getValorDescuento();
		}
		
		return new BigDecimal(0);
	}


	/**
	 * Método que retorna el valor total de las Inclusiones
	 * 
	 * @param indiceRegistroInclusion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private BigDecimal calcularValorTotalInclusion (){
		
		BigDecimal valorTotalInclusiones = new BigDecimal(0);
		BigDecimal totalInclusionesParaDescuento = new BigDecimal(0);
		
		if(this.incluPresuEncabezado!=null){

			for (Iterator<IncluPresuConvenio> convenios = this.incluPresuEncabezado.getIncluPresuConvenios().iterator(); convenios.hasNext();) {
				
				IncluPresuConvenio incluPresuConvenio = (IncluPresuConvenio) convenios.next();
				
				for (Iterator<IncluProgramaConvenio> programasConvenio = incluPresuConvenio.getIncluProgramaConvenios().iterator(); programasConvenio.hasNext();) {
					
					IncluProgramaConvenio incluProgramaConvenio = (IncluProgramaConvenio) programasConvenio.next();
					
					if(incluProgramaConvenio.getContratado() == ConstantesBD.acronimoSiChar){
					
						for (Iterator<IncluServicioConvenio> serviciosConvenio = incluProgramaConvenio.getIncluServicioConvenios().iterator(); serviciosConvenio.hasNext();) {
							
							/*
							 * Siempre debemos tener la tarifa sin el descuento comercial, en el caso de que aplique
							 */
							IncluServicioConvenio incluServicioConvenio = (IncluServicioConvenio) serviciosConvenio.next();
	
							BigDecimal valorUnitarioMenosDctoComercial = incluServicioConvenio.getValorUnitario().subtract(incluServicioConvenio.getDctoComercialUnitario());
							
							if(incluServicioConvenio.getSeleccionadoPromocion() == ConstantesBD.acronimoSiChar)
							{
								if(incluServicioConvenio.getPorcentajeDctoPromocion()!=null
										&& incluServicioConvenio.getPorcentajeDctoPromocion().doubleValue() > 0){
									
									BigDecimal descuento = valorUnitarioMenosDctoComercial.multiply(incluServicioConvenio.getPorcentajeDctoPromocion()).divide(new BigDecimal(100));
									
									BigDecimal valorConDescuento = valorUnitarioMenosDctoComercial.subtract(descuento);
									
									valorTotalInclusiones = valorTotalInclusiones.add(valorConDescuento);
									
								}else if (incluServicioConvenio.getValorDescuentoPromocion()!=null
										&& incluServicioConvenio.getValorDescuentoPromocion().doubleValue() > 0){
									
									BigDecimal valorConDescuento = valorUnitarioMenosDctoComercial.subtract(incluServicioConvenio.getValorDescuentoPromocion());
									
									valorTotalInclusiones = valorTotalInclusiones.add(valorConDescuento);
								}
								
							}else{
								
								/*
								 * Quiere decir que se debe tomar la tarifa completa para el servicio
								 */
								
								valorTotalInclusiones = valorTotalInclusiones.add(valorUnitarioMenosDctoComercial);
								
								
								if(incluServicioConvenio.getSeleccionadoPromocion() == ConstantesBD.acronimoNoChar
									&& incluServicioConvenio.getSeleccionadoBono() == ConstantesBD.acronimoNoChar){
									
									totalInclusionesParaDescuento = totalInclusionesParaDescuento.add(valorUnitarioMenosDctoComercial);
								}
							}
						}
					}
				}
			}
//			
//			for (Iterator<InclusionesPresupuesto> inclusionesPresupuesto = this.incluPresuEncabezado.getInclusionesPresupuestos().iterator(); inclusionesPresupuesto.hasNext();) {
//				
//				InclusionesPresupuesto inclusionPresupuesto = (InclusionesPresupuesto) inclusionesPresupuesto.next();
//				
//				if(inclusionPresupuesto!=null && inclusionPresupuesto.getValor()!=null){
//					
//					valorTotalInclusiones =  valorTotalInclusiones.add(inclusionPresupuesto.getValor());
//				}
//			}
		}
		
		this.getTotalesContratarInclusion().setTotalInclusionesParaDescuento(totalInclusionesParaDescuento);

		return valorTotalInclusiones;
	}
	
	
	/**
	 * Método que retorna el nombre completo del responsable que realiza la Inclusión
	 * 
	 * @return
	 */
	public String getNombreCompletoResponsableInclusion(){
		
		String nombreResponsable = "";
		
		IncluPresuEncabezado incluPresuEncabezado = this.getIncluPresuEncabezado();
		
		if(incluPresuEncabezado!=null){
			
			Personas persona = incluPresuEncabezado.getUsuarios().getPersonas();
			
			nombreResponsable = persona.getPrimerNombre() +" "+ persona.getSegundoNombre() +" "+ 
								persona.getPrimerApellido() +" "+ persona.getSegundoApellido();
		}
		
		return nombreResponsable;
	}
	
	
	/**
	 * Método que retorna el la descripcion del centro de atención
	 * al cual esta asociado el proceso de contratación de la inclusión
	 * 
	 * @return
	 */
	public String getDescripcionCentroAtencion(){
		
		IncluPresuEncabezado incluPresuEncabezado = this.getIncluPresuEncabezado();
		
		if(incluPresuEncabezado!=null && incluPresuEncabezado.getCentroAtencion()!=null){
			
			return incluPresuEncabezado.getCentroAtencion().getDescripcion();
		}
		
		return "";
	}
	
	
	/**
	 * Método que retorna el la descripcion del centro de atención
	 * al cual esta asociado el proceso de contratación de la inclusión
	 * 
	 * @return
	 */
	public String getNumeroOtroSi(){
		
		IncluPresuEncabezado incluPresuEncabezado = this.getIncluPresuEncabezado();
		
		if(incluPresuEncabezado!=null && incluPresuEncabezado.getOtrosSi()!=null){
			
			return incluPresuEncabezado.getPresupuestoOdontologico().getConsecutivo() + " - " +
					incluPresuEncabezado.getOtrosSi().getConsecutivo();
		}
		
		return "";
	}
}