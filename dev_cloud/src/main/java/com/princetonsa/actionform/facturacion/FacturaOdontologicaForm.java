package com.princetonsa.actionform.facturacion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.facturacion.DtoIngresosFactura;
import com.princetonsa.dto.facturacion.DtoResponsableFacturaOdontologica;
import com.princetonsa.dto.facturacion.DtoResumenFacturaOdontologica;

/**
 * 
 * @author axioma
 *
 */
public class FacturaOdontologicaForm extends ValidatorForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6814658394562370542L;

	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private String estado = "";
	
	/**
	 * lista los ingresos a facturar, cuenta activa, ingreso cerrado o abierto
	 */
	private ArrayList<DtoIngresosFactura> listaIngresosFactura;
	
	/**
	 * lista de los responsables con sus correspondientes solicitudes y cargos
	 */
	private ArrayList<DtoResponsableFacturaOdontologica> listaResponsablesConSolicitudes;
	
	
	/**
	 * cuenta seleccionada para facturar
	 */
	private BigDecimal cuenta;
	
	/**
	 * Siguiente pagina cuando se cancela el proceso de facturación
	 */
	private String siguientePagina;
	
	/**
	 * lista de las facturas generadas para el resumen
	 */
	private ArrayList<DtoResumenFacturaOdontologica> listaResumenFacturas;
	
	/**
	 * reset de los atributos de la forma
	 */
	public void reset()
	{
		this.listaIngresosFactura= new ArrayList<DtoIngresosFactura>();
		this.cuenta= BigDecimal.ZERO;
		this.siguientePagina="";
		this.listaResponsablesConSolicitudes= new ArrayList<DtoResponsableFacturaOdontologica>();
		this.listaResumenFacturas= new ArrayList<DtoResumenFacturaOdontologica>();
	}
	
	/**
	 * Validate the properties that have been set from this HTTP request, and
	 * return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found.  If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 * error messages.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		// Perform validator framework validations
		ActionErrors errors = super.validate(mapping, request);
		return errors;
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the listaIngresosFactura
	 */
	public ArrayList<DtoIngresosFactura> getListaIngresosFactura() {
		return listaIngresosFactura;
	}

	/**
	 * @param listaIngresosFactura the listaIngresosFactura to set
	 */
	public void setListaIngresosFactura(
			ArrayList<DtoIngresosFactura> listaIngresosFactura) {
		this.listaIngresosFactura = listaIngresosFactura;
	}

	/**
	 * @return the cuenta
	 */
	public BigDecimal getCuenta() {
		return cuenta;
	}

	/**
	 * @param cuenta the cuenta to set
	 */
	public void setCuenta(BigDecimal cuenta) {
		this.cuenta = cuenta;
	}

	/**
	 * @return the siguientePagina
	 */
	public String getSiguientePagina() {
		return siguientePagina;
	}

	/**
	 * @param siguientePagina the siguientePagina to set
	 */
	public void setSiguientePagina(String siguientePagina) {
		this.siguientePagina = siguientePagina;
	}

	/**
	 * @return the listaResponsablesConSolicitudes
	 */
	public ArrayList<DtoResponsableFacturaOdontologica> getListaResponsablesConSolicitudes() {
		return listaResponsablesConSolicitudes;
	}

	/**
	 * @param listaResponsablesConSolicitudes the listaResponsablesConSolicitudes to set
	 */
	public void setListaResponsablesConSolicitudes(
			ArrayList<DtoResponsableFacturaOdontologica> listaResponsablesConSolicitudes) {
		this.listaResponsablesConSolicitudes = listaResponsablesConSolicitudes;
	}

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Vector getContratosVector() 
	{
		Vector contratos= new Vector();
		if(this.getListaResponsablesConSolicitudes()!=null)
		{	
			for(DtoResponsableFacturaOdontologica dto: this.getListaResponsablesConSolicitudes())
			{
				contratos.add(dto.getContrato().getCodigo());
			}
		}	
		return contratos;
	}

	/**
	 * @return the listaResumenFacturas
	 */
	public ArrayList<DtoResumenFacturaOdontologica> getListaResumenFacturas() {
		return listaResumenFacturas;
	}

	/**
	 * @param listaResumenFacturas the listaResumenFacturas to set
	 */
	public void setListaResumenFacturas(
			ArrayList<DtoResumenFacturaOdontologica> listaResumenFacturas) {
		this.listaResumenFacturas = listaResumenFacturas;
	}
	
	
}
