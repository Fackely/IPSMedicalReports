package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * Esta clase se encarga de almacenar los codigos propietario de los servicios 
 * iniciales y los valores generados desde la consulta
 * @author Fabian Becerra
 * @author Javier Gonzalez
 * @since 27 Oct 2010
 */
public class DtoReporteCambioServiciosOrdenarServicios implements Serializable{

	  
	private static final long serialVersionUID = 1L;
	/**
	 * Atributo que almacena el codigo propietario del servicio
	 */
	private String codigoPropietario;
	private String codigoPropietarioAnteriores;
	private String codigoPropietarioNuevos;
	private long codigoPkCita;
	private int codigoPk;
	private int codigoServiciosAnteriores;
	private int codigoServiciosNuevos;
		
	/**
	 * Atributo que almacena el valor Inicial de los servicios
	 * la primera vez que se solicita el cambio de servicios
	 */
	private BigDecimal valorInicial;
	
	/**
	 * Atributo que almacena el valor final de los servicios
	 * la ultima vez que se solicita el cambio de servicios
	 */
	private BigDecimal valorFinal;
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor del 
	 * atributo codigoPropietario
	 * 
	 * @param  valor del atributo codigoPropietario
	 */
	public void setCodigoPropietario(String codigoPropietario) {
		this.codigoPropietario = codigoPropietario;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo codigoPropietario
	 * 
	 * @return  Retorna la variable codigoPropietario
	 */
	public String getCodigoPropietario() {
		return codigoPropietario;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor del 
	 * atributo valorInicial
	 * 
	 * @param  valor del atributo valorInicial
	 */
	public void setValorInicial(BigDecimal valorInicial) {
		this.valorInicial = valorInicial;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo valorInicial
	 * 
	 * @return  Retorna la variable valorInicial
	 */
	public BigDecimal getValorInicial() {
		return valorInicial;
	}
	
	/**
	 * M&eacute;todo que se encarga de establecer el valor del 
	 * atributo valorFinal
	 * 
	 * @param  valor del atributo valorFinal
	 */
	public void setValorFinal(BigDecimal valorFinal) {
		this.valorFinal = valorFinal;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor del 
	 * atributo valorFinal
	 * 
	 * @return  Retorna la variable valorFinal
	 */
	public BigDecimal getValorFinal() {
		return valorFinal;
	}

	public void setCodigoPropietarioAnteriores(
			String codigoPropietarioAnteriores) {
		this.codigoPropietarioAnteriores = codigoPropietarioAnteriores;
	}

	public String getCodigoPropietarioAnteriores() {
		return codigoPropietarioAnteriores;
	}

	public void setCodigoPropietarioNuevos(String codigoPropietarioNuevos) {
		this.codigoPropietarioNuevos = codigoPropietarioNuevos;
	}

	public String getCodigoPropietarioNuevos() {
		return codigoPropietarioNuevos;
	}

	public void setCodigoPkCita(long codigoPkCita) {
		this.codigoPkCita = codigoPkCita;
	}

	public long getCodigoPkCita() {
		return codigoPkCita;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public int getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoServiciosAnteriores(int codigoServiciosAnteriores) {
		this.codigoServiciosAnteriores = codigoServiciosAnteriores;
	}

	public int getCodigoServiciosAnteriores() {
		return codigoServiciosAnteriores;
	}

	public void setCodigoServiciosNuevos(int codigoServiciosNuevos) {
		this.codigoServiciosNuevos = codigoServiciosNuevos;
	}

	public int getCodigoServiciosNuevos() {
		return codigoServiciosNuevos;
	}

	   
	   
}

