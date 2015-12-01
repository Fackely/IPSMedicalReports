package com.princetonsa.dto.facturacion;

import util.UtilidadTexto;
import util.Utilidades;

/**
 * Esta clase se encarga de 
 * @author Angela Maria Aguirre
 * @since 7/09/2010
 */
public class DTOBusquedaMontoAgrupacionServicio {
	
	private Integer codigoAgrupacionServicio;
	private int detalleCodigo;
	private Integer cantidadServicio;
	private Integer cantidadMonto;
	private Double valorMonto;
	private String acronimoTipoServicio;
	private String nombreTipoServicio;
	private Integer codigoGrupoServicio;
	private String descripcionGrupoServicio;
	private Integer codigoEspecialidad;
	private String nombreEspecialidad;
	private String valorHelper;
	
	/**
	 * 
	 * Este Método se encarga de asignar un valor
	 * específico al hashCode de un objeto de tipo DTOBusquedaMontoAgrupacionServicio
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
	 * de tipo DTOBusquedaMontoAgrupacionServicio
	 * @param Object obj
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public boolean equals(Object obj) {
		
		if(obj instanceof DTOBusquedaMontoAgrupacionServicio){	
			if(!(this.getCodigoGrupoServicio()!=(((
					DTOBusquedaMontoAgrupacionServicio) obj).getCodigoGrupoServicio()))){
				return false;
			}
			if((!UtilidadTexto.isEmpty(this.getAcronimoTipoServicio())) && 
					(!UtilidadTexto.isEmpty(this.getAcronimoTipoServicio()))){				
				if(!(this.getAcronimoTipoServicio().equals((((
						DTOBusquedaMontoAgrupacionServicio) obj).getAcronimoTipoServicio())))){
					return false;
				}				
			}		
			if(!(this.getCodigoEspecialidad()!=((
					DTOBusquedaMontoAgrupacionServicio) obj).getCodigoEspecialidad())){
				return false;
			}					
									
			if((this.getCantidadServicio()!=null) && 
					(((DTOBusquedaMontoAgrupacionServicio) obj).getCantidadServicio()!=null)){
				if(!(this.getCantidadServicio().equals(((
						DTOBusquedaMontoAgrupacionServicio) obj).getCantidadServicio()))){
					return false;
				}
			}
			if((this.getCantidadMonto()!=null) && 
					(((DTOBusquedaMontoAgrupacionServicio) obj).getCantidadMonto()!=null)){
				if(!(this.getCantidadMonto().equals(((
						DTOBusquedaMontoAgrupacionServicio) obj).getCantidadMonto()))){
					return false;
				}
			}	
			if((this.getValorMonto()!=null) && 
					(((DTOBusquedaMontoAgrupacionServicio) obj).getValorMonto()!=null)){
				if(!(this.getCantidadServicio().equals(((
						DTOBusquedaMontoAgrupacionServicio) obj).getValorMonto()))){
					return false;
				}
			}	
		}
		
		return true;
	}
	
	/**
	 * 
	 * Método constructor de la clase
	 */
	public DTOBusquedaMontoAgrupacionServicio(){
		
	}
	
	/**
	 * 
	 * Método constructor de la clase
	 */
	public DTOBusquedaMontoAgrupacionServicio(Integer codigoEspecialidad){
		this.codigoEspecialidad = codigoEspecialidad;
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoAgrupacionServicio
	
	 * @return retorna la variable codigoAgrupacionServicio 
	 * @author Angela Maria Aguirre 
	 */
	public Integer getCodigoAgrupacionServicio() {
		return codigoAgrupacionServicio;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoAgrupacionServicio
	
	 * @param valor para el atributo codigoAgrupacionServicio 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoAgrupacionServicio(Integer codigoAgrupacionServicio) {
		this.codigoAgrupacionServicio = codigoAgrupacionServicio;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo detalleCodigo
	
	 * @return retorna la variable detalleCodigo 
	 * @author Angela Maria Aguirre 
	 */
	public int getDetalleCodigo() {
		return detalleCodigo;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo detalleCodigo
	
	 * @param valor para el atributo detalleCodigo 
	 * @author Angela Maria Aguirre 
	 */
	public void setDetalleCodigo(int detalleCodigo) {
		this.detalleCodigo = detalleCodigo;
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
	 * del atributo acronimoTipoServicio
	
	 * @return retorna la variable acronimoTipoServicio 
	 * @author Angela Maria Aguirre 
	 */
	public String getAcronimoTipoServicio() {
		return acronimoTipoServicio;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo acronimoTipoServicio
	
	 * @param valor para el atributo acronimoTipoServicio 
	 * @author Angela Maria Aguirre 
	 */
	public void setAcronimoTipoServicio(String acronimoTipoServicio) {
		this.acronimoTipoServicio = acronimoTipoServicio;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombreTipoServicio
	
	 * @return retorna la variable nombreTipoServicio 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombreTipoServicio() {
		return nombreTipoServicio;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombreTipoServicio
	
	 * @param valor para el atributo nombreTipoServicio 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombreTipoServicio(String nombreTipoServicio) {
		this.nombreTipoServicio = nombreTipoServicio;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoGrupoServicio
	
	 * @return retorna la variable codigoGrupoServicio 
	 * @author Angela Maria Aguirre 
	 */
	public Integer getCodigoGrupoServicio() {
		return codigoGrupoServicio;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoGrupoServicio
	
	 * @param valor para el atributo codigoGrupoServicio 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoGrupoServicio(Integer codigoGrupoServicio) {
		this.codigoGrupoServicio = codigoGrupoServicio;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo descripcionGrupoServicio
	
	 * @return retorna la variable descripcionGrupoServicio 
	 * @author Angela Maria Aguirre 
	 */
	public String getDescripcionGrupoServicio() {
		return descripcionGrupoServicio;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo descripcionGrupoServicio
	
	 * @param valor para el atributo descripcionGrupoServicio 
	 * @author Angela Maria Aguirre 
	 */
	public void setDescripcionGrupoServicio(String descripcionGrupoServicio) {
		this.descripcionGrupoServicio = descripcionGrupoServicio;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoEspecialidad
	
	 * @return retorna la variable codigoEspecialidad 
	 * @author Angela Maria Aguirre 
	 */
	public Integer getCodigoEspecialidad() {
		return codigoEspecialidad;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoEspecialidad
	
	 * @param valor para el atributo codigoEspecialidad 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoEspecialidad(Integer codigoEspecialidad) {
		this.codigoEspecialidad = codigoEspecialidad;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombreEspecialidad
	
	 * @return retorna la variable nombreEspecialidad 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombreEspecialidad() {
		return nombreEspecialidad;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombreEspecialidad
	
	 * @param valor para el atributo nombreEspecialidad 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombreEspecialidad(String nombreEspecialidad) {
		this.nombreEspecialidad = nombreEspecialidad;
	}
	
	
	

}
