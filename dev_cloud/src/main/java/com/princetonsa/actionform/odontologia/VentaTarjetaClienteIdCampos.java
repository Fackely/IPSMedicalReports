package com.princetonsa.actionform.odontologia;

/**
 * Controlar los ID de los campos de la venta tarjeta cliente
 * @author Juan David Ramírez
 * @since 10 Septiembre 2010
 */
public enum VentaTarjetaClienteIdCampos {
	
	// Sección tipo tarjeta 
	CONVENIO("codigoConvenio"),
	TIPO_TARJETA("tipoTarjeta"),
	CONTRATO("contratoTarjeta"),
	SERIAL("serial_0"),
	NUMERO_TARJETA("nroTarjeta_0");
	
	private final String styleId;
	
	/**
	 * Constructor de la enumeración
	 * @param id styleId del campo
	 */
	VentaTarjetaClienteIdCampos(String styelId)
	{
		this.styleId=styelId;
	}

	/**
	 * Obtener el id del campo
	 * @return id
	 */
	public String getStyleId() {
		return styleId;
	}
	
}
