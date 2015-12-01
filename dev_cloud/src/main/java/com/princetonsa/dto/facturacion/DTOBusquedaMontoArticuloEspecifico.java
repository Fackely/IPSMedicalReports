package com.princetonsa.dto.facturacion;

import util.UtilidadTexto;
import util.Utilidades;

import com.servinte.axioma.orm.DetalleMonto;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/09/2010
 */
public class DTOBusquedaMontoArticuloEspecifico {
	
	private int codigo;
	private DetalleMonto detalleMonto;
	private int articuloCodigo;
	private String articuloDescripcion;
	private Integer cantidadArticulos;
	private Integer cantidadMonto;
	private Double valorMonto;
	private int detalleCodigo;
	private String valorHelper;
	
	/**
	 * 
	 * Este Método se encarga de asignar un valor
	 * específico al hashCode de un objeto de tipo DTOBusquedaMontoArticuloEspecifico
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
	 * de tipo DTOBusquedaMontoArticuloEspecifico
	 * @param Object obj
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof DTOBusquedaMontoArticuloEspecifico){	
			if(!(this.getArticuloCodigo()==(((DTOBusquedaMontoArticuloEspecifico) obj).getArticuloCodigo()))){
				return false;
			}
			if(this.getCantidadArticulos()!=null && 
					((DTOBusquedaMontoArticuloEspecifico) obj).getCantidadArticulos()!=null){
				if(!(this.getCantidadArticulos().equals(
						((DTOBusquedaMontoArticuloEspecifico) obj).getCantidadArticulos()!=null))){
					return false;
				}
			}
					
			if(this.getCantidadMonto()!=null && 
					((DTOBusquedaMontoArticuloEspecifico) obj).getCantidadMonto()!=null){
				if(!(this.getCantidadMonto().equals(
						((DTOBusquedaMontoArticuloEspecifico) obj).getCantidadMonto()!=null))){
					return false;
				}
			}			
			if(this.getValorMonto()!=null && 
					((DTOBusquedaMontoArticuloEspecifico) obj).getValorMonto()!=null){
				if(!(this.getValorMonto().equals(
						((DTOBusquedaMontoArticuloEspecifico) obj).getValorMonto()!=null))){
					return false;
				}
			}
		}		
		return true;
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigo
	
	 * @return retorna la variable codigo 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigo() {
		return codigo;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigo
	
	 * @param valor para el atributo codigo 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo detalleMonto
	
	 * @return retorna la variable detalleMonto 
	 * @author Angela Maria Aguirre 
	 */
	public DetalleMonto getDetalleMonto() {
		return detalleMonto;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo detalleMonto
	
	 * @param valor para el atributo detalleMonto 
	 * @author Angela Maria Aguirre 
	 */
	public void setDetalleMonto(DetalleMonto detalleMonto) {
		this.detalleMonto = detalleMonto;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo articuloCodigo
	
	 * @return retorna la variable articuloCodigo 
	 * @author Angela Maria Aguirre 
	 */
	public int getArticuloCodigo() {
		return articuloCodigo;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo articuloCodigo
	
	 * @param valor para el atributo articuloCodigo 
	 * @author Angela Maria Aguirre 
	 */
	public void setArticuloCodigo(int articuloCodigo) {
		this.articuloCodigo = articuloCodigo;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo articuloDescripcion
	
	 * @return retorna la variable articuloDescripcion 
	 * @author Angela Maria Aguirre 
	 */
	public String getArticuloDescripcion() {
		return articuloDescripcion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo articuloDescripcion
	
	 * @param valor para el atributo articuloDescripcion 
	 * @author Angela Maria Aguirre 
	 */
	public void setArticuloDescripcion(String articuloDescripcion) {
		this.articuloDescripcion = articuloDescripcion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo cantidadArticulos
	
	 * @return retorna la variable cantidadArticulos 
	 * @author Angela Maria Aguirre 
	 */
	public Integer getCantidadArticulos() {
		return cantidadArticulos;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo cantidadArticulos
	
	 * @param valor para el atributo cantidadArticulos 
	 * @author Angela Maria Aguirre 
	 */
	public void setCantidadArticulos(Integer cantidadArticulos) {
		this.cantidadArticulos = cantidadArticulos;
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
	 * Este método se encarga de obtener el 
	 * valor del atributo detalleCodigo
	 *
	 * @author Angela Aguirre 
	 */
	public int getDetalleCodigo() {
		return detalleCodigo;
	}
	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo detalleCodigo
	 *
	 * @author Angela Aguirre 
	 */
	public void setDetalleCodigo(int detalleCodigo) {
		this.detalleCodigo = detalleCodigo;
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
		this.valorHelper = valorHelper;
		if(!UtilidadTexto.isEmpty(valorHelper)){
			Double valorAux = Utilidades.convertirADouble(valorHelper, false);
			this.valorMonto = valorAux;			
		}else{
			this.valorMonto = null;
		}
	}	

}
