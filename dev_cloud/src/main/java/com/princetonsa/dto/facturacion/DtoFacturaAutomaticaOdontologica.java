package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import util.ElementoApResource;

/**
 * Dto para cargar los codigos de facturas generadas, o los errores 
 * generados en el proceso automatico de factura odontologica
 * 
 * @author Wilson Rios 
 *
 * Apr 28, 2010 - 8:44:52 AM
 */
public class DtoFacturaAutomaticaOdontologica implements Serializable 
{

	/**
	 * serial
	 */
	private static final long serialVersionUID = -136105728758263225L;
	
	/**
	 * codigo de la factura, solo se carga cuando es exitoso 
	 */
	private ArrayList<BigDecimal> codigosPkFactura;
	
	/**
	 * errores de la factura
	 */
	private ArrayList<ElementoApResource> erroresFactura;

	/**
	 * 
	 * Constructor de la clase
	 */
	public DtoFacturaAutomaticaOdontologica() 
	{
		super();
		this.codigosPkFactura = new ArrayList<BigDecimal>();
		this.erroresFactura = new ArrayList<ElementoApResource>();
	}

	/**
	 * @return the erroresFactura
	 */
	public ArrayList<ElementoApResource> getErroresFactura() {
		return erroresFactura;
	}

	/**
	 * @param erroresFactura the erroresFactura to set
	 */
	public void setErroresFactura(ArrayList<ElementoApResource> erroresFactura) {
		this.erroresFactura = erroresFactura;
	}
	
	/**
	 * @param erroresFactura the erroresFactura to set
	 */
	public void addErroresFactura(String error) {
		this.erroresFactura.add(new ElementoApResource(error));
	}

	/**
	 * @param erroresFactura the erroresFactura to set
	 */
	public void addFactura(BigDecimal facturaAutomatica) {
		this.codigosPkFactura.add(facturaAutomatica);
	}
	
	/**
	 * @return the codigosPkFactura
	 */
	public ArrayList<BigDecimal> getCodigosPkFactura() {
		return codigosPkFactura;
	}

	/**
	 * @param codigosPkFactura the codigosPkFactura to set
	 */
	public void setCodigosPkFactura(ArrayList<BigDecimal> codigosPkFactura) {
		this.codigosPkFactura = codigosPkFactura;
	}
	
	/**
	 * 
	 * Metodo para el logger de los atributos del objeto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public String logger()
	{
		String retorna= "\n\n\n\n\n\n\n\nFACTURAS ---->"+this.getCodigosPkFactura() +" errores-->"+this.getErroresFactura()+"\n\n\n\n\n\n\n\n\n\n\n\n";
		Log4JManager.info(retorna);
		return retorna;
	}
}