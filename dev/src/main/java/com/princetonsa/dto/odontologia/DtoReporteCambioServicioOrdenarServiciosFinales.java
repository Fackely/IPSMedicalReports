package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Esta clase se encarga de almacenar los codigos propietario de los servicios 
 * finales generados desde la consulta
 * @author Fabian Becerra
 * @author Javier Gonzalez
 * @since 27 Oct 2010
 */
public class DtoReporteCambioServicioOrdenarServiciosFinales implements Serializable {
	  
	private static final long serialVersionUID = 1L;
	/**
	 * Atributo que almacena el codigo propietario del servicio
	 */
	private String codigoPropietario;
	private int codigoPk;
	private String codigoPropietarioNuevos;
	private int codigoServiciosNuevos;
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

	public int getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public String getCodigoPropietarioNuevos() {
		return codigoPropietarioNuevos;
	}

	public void setCodigoPropietarioNuevos(String codigoPropietarioNuevos) {
		this.codigoPropietarioNuevos = codigoPropietarioNuevos;
	}

	public int getCodigoServiciosNuevos() {
		return codigoServiciosNuevos;
	}

	public void setCodigoServiciosNuevos(int codigoServiciosNuevos) {
		this.codigoServiciosNuevos = codigoServiciosNuevos;
	}

	public BigDecimal getValorFinal() {
		return valorFinal;
	}

	public void setValorFinal(BigDecimal valorFinal) {
		this.valorFinal = valorFinal;
	}
	
	
}
