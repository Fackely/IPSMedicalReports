package com.servinte.axioma.dto.manejoPaciente;

import java.io.Serializable;
import java.math.BigDecimal;

import util.ConstantesIntegridadDominio;

import com.servinte.axioma.dto.facturacion.MontoCobroDto;

/**
 * Clase encargada de maperar la información de la subcuenta
 * 
 * @author ricruico
 *
 */
public class InfoSubCuentaDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8655997876195709191L;
	
	/**
	 * Atributo que representa en codigoPk del tipo Afiliafo asociado a la subcuenta;
	 */
	private Character codigoTipoAfiliado;
	
	/**
	 * Atributo que representa en nombre del tipo Afiliafo asociado a la subcuenta;
	 */
	private String nombreTipoAfiliado;
	
	/**
	 * Atributo que representa en codigoPk de la naturaleza asociada a la subcuenta;
	 */
	private Integer codigoNaturaleza;
	
	/**
	 * Atributo que representa en nombre de la naturaleza asociada a la subcuenta;
	 */
	private String nombreNaturaleza;
	
	/**
	 * Atributo que representa en codigoPk del estrato social asociado a la subcuenta;
	 */
	private Integer codigoEstratoSocial;
	
	/**
	 * Atributo que representa en nombre del estrato social asociado a la subcuenta;
	 */
	private String nombreEstratoSocial;
	
	/**
	 * Atributo que representa la parametrización de montos de cobro
	 */
	private MontoCobroDto montoCobro= new MontoCobroDto();
	
	/**
	 * Atributo que representa si la subcuenta maneja montos
	 */
	private boolean subCuentaManejaMontos;
	
	/**
	 * Atributo que representa el valor del porcentaje del monto de la subcuenta
	 */
	private Double subCuentaPorcentajeMonto;
	
	/**
	 * Constructor de la clase
	 */
	public InfoSubCuentaDto(){
		
	}

	/**
	 * Constructor de la clase para mapear el resultado de la consulta de 
	 * ingreso.consultarInfoSubCuentaPorIngresoPorContrato
	 * 
	 * @param codigoTipoAfiliado
	 * @param nombreTipoAfiliado
	 * @param codigoNaturaleza
	 * @param nombreNaturaleza
	 * @param codigoEstratoSocial
	 * @param nombreEstratoSocial
	 */
	public InfoSubCuentaDto(Character codigoTipoAfiliado,
			String nombreTipoAfiliado, Integer codigoNaturaleza,
			String nombreNaturaleza, Integer codigoEstratoSocial,
			String nombreEstratoSocial, Integer codDetalleMonto,
			Integer codigoTipoMonto, String nombreTipoMonto,
			String tipoDetalleMonto, Integer cantidadMonto,
			Double porcentajeMonto, Double valorMonto, String manejaMontos, 
			BigDecimal porcentajeMontoSubcuenta) {
		this.codigoTipoAfiliado = codigoTipoAfiliado;
		this.nombreTipoAfiliado = nombreTipoAfiliado;
		this.codigoNaturaleza = codigoNaturaleza;
		this.nombreNaturaleza = nombreNaturaleza;
		this.codigoEstratoSocial = codigoEstratoSocial;
		this.nombreEstratoSocial = nombreEstratoSocial;
		MontoCobroDto dto = new MontoCobroDto();
		dto.setCodDetalleMonto(codDetalleMonto);
		if(dto.getCodDetalleMonto() == null){
			//Para evitar el NullPointer
			dto.setCodDetalleMonto(-2);
		}
		dto.setTipoMonto(codigoTipoMonto);
		dto.setTipoMontoNombre(nombreTipoMonto);
		dto.setTipoDetalleMonto(tipoDetalleMonto);
		dto.setCantidadMonto(cantidadMonto);
		dto.setPorcentajeMonto(porcentajeMonto);
		dto.setValorMonto(valorMonto);
		String descripcion="";
		if(manejaMontos.equals(ConstantesIntegridadDominio.acronimoTipoPacienteManejaMontos)){
			this.subCuentaManejaMontos=true;
			if(dto.getTipoDetalleMonto().equals(ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroDET)){
				descripcion=dto.getTipoMontoNombre()+" Detallado";
			}
			else if(dto.getTipoDetalleMonto().equals(ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroGEN)){
				descripcion=dto.getTipoMontoNombre();
				if(dto.getPorcentajeMonto() != null){
					descripcion=descripcion+" "+dto.getPorcentajeMonto().toString()+" %";
				}
				else if(dto.getValorMonto() != null){
					descripcion=descripcion+" $"+dto.getValorMonto().toString();
				}
				if(dto.getCantidadMonto() != null){
					descripcion=descripcion+" Cantidad ("+dto.getCantidadMonto().toString()+")";
				}
				descripcion+=" General";
			}
		}
		else{
			this.subCuentaManejaMontos=false;
			double porcentajeSub=0D;
			descripcion="Monto Cobro ";
			if(porcentajeMontoSubcuenta != null){
				porcentajeSub=porcentajeMontoSubcuenta.doubleValue();
				descripcion=descripcion+porcentajeMontoSubcuenta.toString()+" %";
			}
			else{
				descripcion+="0 %";
			}
			this.subCuentaPorcentajeMonto=porcentajeSub;
		}
		dto.setDescripcionMonto(descripcion);
		this.montoCobro=dto;
	}

	/**
	 * @return the codigoTipoAfiliado
	 */
	public Character getCodigoTipoAfiliado() {
		return codigoTipoAfiliado;
	}

	/**
	 * @param codigoTipoAfiliado the codigoTipoAfiliado to set
	 */
	public void setCodigoTipoAfiliado(Character codigoTipoAfiliado) {
		this.codigoTipoAfiliado = codigoTipoAfiliado;
	}

	/**
	 * @return the nombreTipoAfiliado
	 */
	public String getNombreTipoAfiliado() {
		return nombreTipoAfiliado;
	}

	/**
	 * @param nombreTipoAfiliado the nombreTipoAfiliado to set
	 */
	public void setNombreTipoAfiliado(String nombreTipoAfiliado) {
		this.nombreTipoAfiliado = nombreTipoAfiliado;
	}

	/**
	 * @return the codigoNaturaleza
	 */
	public Integer getCodigoNaturaleza() {
		return codigoNaturaleza;
	}

	/**
	 * @param codigoNaturaleza the codigoNaturaleza to set
	 */
	public void setCodigoNaturaleza(Integer codigoNaturaleza) {
		this.codigoNaturaleza = codigoNaturaleza;
	}

	/**
	 * @return the nombreNaturaleza
	 */
	public String getNombreNaturaleza() {
		return nombreNaturaleza;
	}

	/**
	 * @param nombreNaturaleza the nombreNaturaleza to set
	 */
	public void setNombreNaturaleza(String nombreNaturaleza) {
		this.nombreNaturaleza = nombreNaturaleza;
	}

	/**
	 * @return the codigoEstratoSocial
	 */
	public Integer getCodigoEstratoSocial() {
		return codigoEstratoSocial;
	}

	/**
	 * @param codigoEstratoSocial the codigoEstratoSocial to set
	 */
	public void setCodigoEstratoSocial(Integer codigoEstratoSocial) {
		this.codigoEstratoSocial = codigoEstratoSocial;
	}

	/**
	 * @return the nombreEstratoSocial
	 */
	public String getNombreEstratoSocial() {
		return nombreEstratoSocial;
	}

	/**
	 * @param nombreEstratoSocial the nombreEstratoSocial to set
	 */
	public void setNombreEstratoSocial(String nombreEstratoSocial) {
		this.nombreEstratoSocial = nombreEstratoSocial;
	}

	/**
	 * @return the montoCobro
	 */
	public MontoCobroDto getMontoCobro() {
		return montoCobro;
	}

	/**
	 * @param montoCobro the montoCobro to set
	 */
	public void setMontoCobro(MontoCobroDto montoCobro) {
		this.montoCobro = montoCobro;
	}

	/**
	 * @return the subCuentaManejaMontos
	 */
	public boolean isSubCuentaManejaMontos() {
		return subCuentaManejaMontos;
	}

	/**
	 * @param subCuentaManejaMontos the subCuentaManejaMontos to set
	 */
	public void setSubCuentaManejaMontos(boolean subCuentaManejaMontos) {
		this.subCuentaManejaMontos = subCuentaManejaMontos;
	}

	/**
	 * @return the subCuentaPorcentajeMonto
	 */
	public Double getSubCuentaPorcentajeMonto() {
		return subCuentaPorcentajeMonto;
	}

	/**
	 * @param subCuentaPorcentajeMonto the subCuentaPorcentajeMonto to set
	 */
	public void setSubCuentaPorcentajeMonto(Double subCuentaPorcentajeMonto) {
		this.subCuentaPorcentajeMonto = subCuentaPorcentajeMonto;
	}

}
