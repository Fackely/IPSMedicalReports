package com.princetonsa.dto.facturacion;

import util.UtilidadTexto;
import util.Utilidades;



/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/09/2010
 */
public class DTOBusquedaMontoServicioEspecifico {
	
	private int codigoServicioEspecifico;
	private int detalleMonto;
	private Integer cantidadServicio;
	private Integer cantidadMonto;
	private Double valorMonto;
	private String descripcionServicio;
	private int codigoServicio;
	private String valorHelper;
	
	/**
	 * 
	 * Este Método se encarga de asignar un valor
	 * específico al hashCode de un objeto de tipo DTOBusquedaMontoServicioEspecifico
	 * 
	 * @return int
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public int hashCode() {
		return 0;
	}
	
	/**
	 * 
	 * Este Método se encarga de comparar dos objetos
	 * de tipo DTOBusquedaMontoServicioEspecifico
	 * @param Object obj
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof DTOBusquedaMontoServicioEspecifico){	
			if(!(this.getCodigoServicio()==(((DTOBusquedaMontoServicioEspecifico) obj).getCodigoServicio()))){
				return false;
			}
			if(this.getCantidadServicio()!=null && 
					((DTOBusquedaMontoServicioEspecifico) obj).getCantidadServicio()!=null){
				if(!(this.getCantidadServicio().equals(
						((DTOBusquedaMontoServicioEspecifico) obj).getCantidadServicio()!=null))){
					return false;
				}
			}
					
			if(this.getCantidadMonto()!=null && 
					((DTOBusquedaMontoServicioEspecifico) obj).getCantidadMonto()!=null){
				if(!(this.getCantidadMonto().equals(
						((DTOBusquedaMontoServicioEspecifico) obj).getCantidadMonto()!=null))){
					return false;
				}
			}			
			if(this.getValorMonto()!=null && 
					((DTOBusquedaMontoServicioEspecifico) obj).getValorMonto()!=null){
				if(!(this.getValorMonto().equals(
						((DTOBusquedaMontoServicioEspecifico) obj).getValorMonto()!=null))){
					return false;
				}
			}
		}		
		return true;
	}
	
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoServicioEspecifico
	
	 * @return retorna la variable codigoServicioEspecifico 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoServicioEspecifico() {
		return codigoServicioEspecifico;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoServicioEspecifico
	
	 * @param valor para el atributo codigoServicioEspecifico 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoServicioEspecifico(int codigoServicioEspecifico) {
		this.codigoServicioEspecifico = codigoServicioEspecifico;
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo cantidadServicio
	
	 * @return retorna la variable cantidadServicio 
	 * @author Angela Maria Aguirre 
	 */
	public Integer getCantidadServicio() {
		return cantidadServicio;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo cantidadServicio
	
	 * @param valor para el atributo cantidadServicio 
	 * @author Angela Maria Aguirre 
	 */
	public void setCantidadServicio(Integer cantidadServicio) {
		this.cantidadServicio = cantidadServicio;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo cantidadMonto
	
	 * @return retorna la variable cantidadMonto 
	 * @author Angela Maria Aguirre 
	 */
	public Integer getCantidadMonto() {
		return cantidadMonto;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo cantidadMonto
	
	 * @param valor para el atributo cantidadMonto 
	 * @author Angela Maria Aguirre 
	 */
	public void setCantidadMonto(Integer cantidadMonto) {
		this.cantidadMonto = cantidadMonto;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo valorMonto
	
	 * @return retorna la variable valorMonto 
	 * @author Angela Maria Aguirre 
	 */
	public Double getValorMonto() {
		return valorMonto;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo valorMonto
	
	 * @param valor para el atributo valorMonto 
	 * @author Angela Maria Aguirre 
	 */
	public void setValorMonto(Double valorMonto) {
		if(valorMonto!=null && valorMonto.doubleValue()>=0){
			this.valorMonto = valorMonto;
		}else{
			this.valorMonto = null;
		}
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo valorHelper
	
	 * @return retorna la variable valorHelper 
	 * @author Angela Maria Aguirre 
	 */
	public String getValorHelper() {
		return valorHelper;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo valorHelper
	
	 * @param valor para el atributo valorHelper 
	 * @author Angela Maria Aguirre 
	 */
	public void setValorHelper(String valorHelper) {
		this.valorHelper=valorHelper;
		if(!UtilidadTexto.isEmpty(valorHelper)){
			Double valorAux = Utilidades.convertirADouble(valorHelper, false);
			this.valorMonto = valorAux;			
		}else{
			this.valorMonto = null;
		}
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo descripcionServicio
	
	 * @return retorna la variable descripcionServicio 
	 * @author Angela Maria Aguirre 
	 */
	public String getDescripcionServicio() {
		return descripcionServicio;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo descripcionServicio
	
	 * @param valor para el atributo descripcionServicio 
	 * @author Angela Maria Aguirre 
	 */
	public void setDescripcionServicio(String descripcionServicio) {
		this.descripcionServicio = descripcionServicio;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoServicio
	
	 * @return retorna la variable codigoServicio 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoServicio() {
		return codigoServicio;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoServicio
	
	 * @param valor para el atributo codigoServicio 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}
	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo detalleMonto
	 *
	 * @author Angela Aguirre 
	 */
	public int getDetalleMonto() {
		return detalleMonto;
	}
	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo detalleMonto
	 *
	 * @author Angela Aguirre 
	 */
	public void setDetalleMonto(int detalleMonto) {
		this.detalleMonto = detalleMonto;
	}

}
