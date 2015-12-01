package com.princetonsa.dto.cargos;

import java.io.Serializable;

import com.princetonsa.dto.facturacion.DtoContratoEntidadSub;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;

/**
 * Esta clase se encarga de contener los datos
 * para el cálculo de la tarifa de un servicio o
 * artículo determinado
 * 
 * @author Angela Maria Aguirre
 * @since 2/12/2010
 */
public class DTOCalculoTarifaServicioArticulo  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int codigoArticuloServicio;
	private String fechaVigencia; 
	private  DtoContratoEntidadSub contratoEntidadSubcontratada;	
	private DtoEntidadSubcontratada entidadSubcontratada;
	private boolean esServicio;
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoArticuloServicio
	
	 * @return retorna la variable codigoArticuloServicio 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoArticuloServicio() {
		return codigoArticuloServicio;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoArticuloServicio
	
	 * @param valor para el atributo codigoArticuloServicio 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoArticuloServicio(int codigoArticuloServicio) {
		this.codigoArticuloServicio = codigoArticuloServicio;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo fechaVigencia
	
	 * @return retorna la variable fechaVigencia 
	 * @author Angela Maria Aguirre 
	 */
	public String getFechaVigencia() {
		return fechaVigencia;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo fechaVigencia
	
	 * @param valor para el atributo fechaVigencia 
	 * @author Angela Maria Aguirre 
	 */
	public void setFechaVigencia(String fechaVigencia) {
		this.fechaVigencia = fechaVigencia;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo contrato
	
	 * @return retorna la variable contrato 
	 * @author Angela Maria Aguirre 
	 */
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo contratoEntidadSubcontratada
	
	 * @return retorna la variable contratoEntidadSubcontratada 
	 * @author Angela Maria Aguirre 
	 */
	public DtoContratoEntidadSub getContratoEntidadSubcontratada() {
		return contratoEntidadSubcontratada;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo contratoEntidadSubcontratada
	
	 * @param valor para el atributo contratoEntidadSubcontratada 
	 * @author Angela Maria Aguirre 
	 */
	public void setContratoEntidadSubcontratada(
			DtoContratoEntidadSub contratoEntidadSubcontratada) {
		this.contratoEntidadSubcontratada = contratoEntidadSubcontratada;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo entidadSubcontratada
	
	 * @return retorna la variable entidadSubcontratada 
	 * @author Angela Maria Aguirre 
	 */
	public DtoEntidadSubcontratada getEntidadSubcontratada() {
		return entidadSubcontratada;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo entidadSubcontratada
	
	 * @param valor para el atributo entidadSubcontratada 
	 * @author Angela Maria Aguirre 
	 */
	public void setEntidadSubcontratada(DtoEntidadSubcontratada entidadSubcontratada) {
		this.entidadSubcontratada = entidadSubcontratada;
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo esServicio
	
	 * @return retorna la variable esServicio 
	 * @author Angela Maria Aguirre 
	 */
	public boolean isEsServicio() {
		return esServicio;
	}
	
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo esServicio
	
	 * @param valor para el atributo esServicio 
	 * @author Angela Maria Aguirre 
	 */
	public void setEsServicio(boolean esServicio) {
		this.esServicio = esServicio;
	}	

}
