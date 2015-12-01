package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;

import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.servinte.axioma.orm.Convenios;

/**
 * Esta clase se encarga de contener los datos de la entidad 
 * de auorizacion capitación subcontratada
 * 
 * @author Angela Maria Aguirre
 * @since 7/12/2010
 */
public class DTOAutorizacionCapitacionSubcontratada implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long codigoPK;
	private long consecutivo;
	
	/**
	 * Campo que identifica si la autorización es 
	 * automática o manual 
	 */
	private String tipoAutorizacion;
	private char indicativoTemporal;	
	private Integer indicadorPrioridad;
	
	/**
	 * Atributo que determina la entidad a recobrar
	 */
	private Convenios codigoConvenioRecobro;
	
	/**
	 * Atributo que almacena el nombre del convenio de la entidad de 
	 * recobro, si esta no existe como convenio en el sistema
	 */
	private String otroConvenioRecobro;
	
	private DtoEntidadSubcontratada entidadSubcontratada;	

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoPK
	
	 * @return retorna la variable codigoPK 
	 * @author Angela Maria Aguirre 
	 */
	public long getCodigoPK() {
		return codigoPK;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoPK
	
	 * @param valor para el atributo codigoPK 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoPK(long codigoPK) {
		this.codigoPK = codigoPK;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo consecutivo
	
	 * @return retorna la variable consecutivo 
	 * @author Angela Maria Aguirre 
	 */
	public long getConsecutivo() {
		return consecutivo;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo consecutivo
	
	 * @param valor para el atributo consecutivo 
	 * @author Angela Maria Aguirre 
	 */
	public void setConsecutivo(long consecutivo) {
		this.consecutivo = consecutivo;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo tipoAutorizacion
	
	 * @return retorna la variable tipoAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public String getTipoAutorizacion() {
		return tipoAutorizacion;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo tipoAutorizacion
	
	 * @param valor para el atributo tipoAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setTipoAutorizacion(String tipoAutorizacion) {
		this.tipoAutorizacion = tipoAutorizacion;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo indicativoTemporal
	
	 * @return retorna la variable indicativoTemporal 
	 * @author Angela Maria Aguirre 
	 */
	public char getIndicativoTemporal() {
		return indicativoTemporal;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo indicativoTemporal
	
	 * @param valor para el atributo indicativoTemporal 
	 * @author Angela Maria Aguirre 
	 */
	public void setIndicativoTemporal(char indicativoTemporal) {
		this.indicativoTemporal = indicativoTemporal;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo indicadorPrioridad
	
	 * @return retorna la variable indicadorPrioridad 
	 * @author Angela Maria Aguirre 
	 */
	public Integer getIndicadorPrioridad() {
		return indicadorPrioridad;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo indicadorPrioridad
	
	 * @param valor para el atributo indicadorPrioridad 
	 * @author Angela Maria Aguirre 
	 */
	public void setIndicadorPrioridad(Integer indicadorPrioridad) {
		this.indicadorPrioridad = indicadorPrioridad;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoConvenioRecobro
	
	 * @return retorna la variable codigoConvenioRecobro 
	 * @author Angela Maria Aguirre 
	 */
	public Convenios getCodigoConvenioRecobro() {
		return codigoConvenioRecobro;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoConvenioRecobro
	
	 * @param valor para el atributo codigoConvenioRecobro 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoConvenioRecobro(Convenios codigoConvenioRecobro) {
		this.codigoConvenioRecobro = codigoConvenioRecobro;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo otroConvenioRecobro
	
	 * @return retorna la variable otroConvenioRecobro 
	 * @author Angela Maria Aguirre 
	 */
	public String getOtroConvenioRecobro() {
		return otroConvenioRecobro;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo otroConvenioRecobro
	
	 * @param valor para el atributo otroConvenioRecobro 
	 * @author Angela Maria Aguirre 
	 */
	public void setOtroConvenioRecobro(String otroConvenioRecobro) {
		this.otroConvenioRecobro = otroConvenioRecobro;
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
}
