/*
 * Abril 16, 2010
 */
package com.princetonsa.actionform.tesoreria;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadTexto;
import util.Utilidades;

import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.ConcEgrXusuXcatencion;
import com.servinte.axioma.orm.RegistroEgresosDeCaja;
import com.servinte.axioma.orm.delegate.facturacion.TerceroDelegate;
import com.servinte.axioma.orm.delegate.tesoreria.CajasDelegate;
import com.servinte.axioma.orm.delegate.tesoreria.ConcEgrXusuXcatencionDelegate;


/**
 * @author Cristhian Murillo
 *
 * Clase que almacena y carga la informaci&oacute;n a la vista utilizada para la funcionalidad
 */
public class RegistroEgresosDeCajaForm extends ValidatorForm 
{
	
	/* ATRIBUTOS*/
	
	/**
	 * Variable para manejar la direcci&oacute;n del workflow 
	 */
	private String estado;
	
	/**
	 * DTO 
	 */
	private RegistroEgresosDeCaja dto = new RegistroEgresosDeCaja();
	
	
	/**
	 * Lista DTO 
	 */
	private ArrayList<RegistroEgresosDeCaja> listaDto;

	
	/**
	 * Indica si se debe mostrar el formulario nuevo/modificar 
	 */
	private boolean mostrarFormularioIngreso;

	
	
	
	/**
	 * Lista DTO Cajas 
	 */
	private ArrayList<Cajas> listaCajas;
	
	
	/**
	 * Codigo de la caja
	 */
	private String strCaja;
	
	
	/**
	 * Caja seleccionada
	 */
	private Cajas cajaSeleccionada;
	
	
	/**
	 * Lista ConceptosDeEgreso
	 */
	private ArrayList<ConcEgrXusuXcatencion> listaConceptosEgresoXusuXca;
	
	
	/**
	 * Variable para hacer el cast de la llave primaria de ConceptoDeEgreso
	 */
	private String strConceptoEgresoXusuXca;
	
	
	/**
	 * Variable para hacer el cast de la llave primaria de ConceptoDeEgreso
	 */
	private int intTercero;
	
	
	

	/**
	 * Variable definir si el egreso fue autorizado o no
	 */
	private boolean autorizado;
	
	
	
	
	
	
	/* SETS Y GETS */
	
	/**
	 * @return the autorizado
	 */
	public boolean isAutorizado() {
		return autorizado;
	}


	/**
	 * @param autorizado the autorizado to set
	 */
	public void setAutorizado(boolean autorizado) {
		this.autorizado = autorizado;
	}


	/**
	 * @return the cajaSeleccionada
	 */
	public Cajas getCajaSeleccionada() {
		return cajaSeleccionada;
	}


	/**
	 * @param cajaSeleccionada the cajaSeleccionada to set
	 */
	public void setCajaSeleccionada(Cajas cajaSeleccionada) {
		this.cajaSeleccionada = cajaSeleccionada;
	}
	
	

	/**
	 * @return the intTercero
	 */
	public int getIntTercero() {
		
		if(this.getDto().getTerceros() == null)
		{
			intTercero = -1;
			//intTercero = 0;  
		}
		else
		{
			intTercero = this.getDto().getTerceros().getCodigo();
		}
		
		return intTercero;
	}


	/**
	 * @param intTercero the intTercero to set
	 */
	public void setIntTercero(int intTercero) {
		this.intTercero = intTercero;
		
		if(intTercero>0){
			dto.setTerceros(new TerceroDelegate().findById(intTercero));
		}
		else
		{
			dto.setTerceros(null);
		}
	}

	
	
	
	/**
	 * @return the listaConceptosEgresoXusuXca
	 */
	public ArrayList<ConcEgrXusuXcatencion> getListaConceptosEgresoXusuXca() {
		return listaConceptosEgresoXusuXca;
	}


	/**
	 * @param listaConceptosEgresoXusuXca the listaConceptosEgresoXusuXca to set
	 */
	public void setListaConceptosEgresoXusuXca(
			ArrayList<ConcEgrXusuXcatencion> listaConceptosEgresoXusuXca) {
		this.listaConceptosEgresoXusuXca = listaConceptosEgresoXusuXca;
	}


	/**
	 * @return the strConceptoEgresoXusuXca
	 */
	public String getStrConceptoEgresoXusuXca() {
		try {
			strConceptoEgresoXusuXca = dto.getConcEgrXusuXcatencion().getCodigoPk()+"";
		} catch (Exception e) { 
			e.printStackTrace();
		}
		
		return strConceptoEgresoXusuXca;
	}


	/**
	 * @param strConceptoEgresoXusuXca the strConceptoEgresoXusuXca to set
	 */
	public void setStrConceptoEgresoXusuXca(String strConceptoEgresoXusuXca) {
		
		if (!UtilidadTexto.isEmpty(strConceptoEgresoXusuXca)){
			Long concXusuXca = Utilidades.convertirALong(strConceptoEgresoXusuXca);
			dto.setConcEgrXusuXcatencion(new ConcEgrXusuXcatencionDelegate().findById(concXusuXca));
		}
		else {
			dto.setConcEgrXusuXcatencion(null);
		}
	}


	/**
	 * @return the strCaja
	 */
	public String getStrCaja() 
	{
		if(getCajaSeleccionada() == null)
		{
			strCaja = "";
		}
		else
		{
			strCaja = getCajaSeleccionada().getConsecutivo()+"";
		}
		
		return strCaja;
	}


	/**
	 * @param strCaja the strCaja to set
	 */
	public void setStrCaja(String strCaja) 
	{
		int caja = Utilidades.convertirAEntero(strCaja);

		if(caja>0){
			setCajaSeleccionada(new CajasDelegate().findById(caja));
			this.strCaja = strCaja;
		}
		else
		{
			setCajaSeleccionada(null);
		}
	}


	
	
	/**
	 * @return the listaCajas
	 */
	public ArrayList<Cajas> getListaCajas() {
		return listaCajas;
	}


	/**
	 * @param listaCajas the listaCajas to set
	 */
	public void setListaCajas(ArrayList<Cajas> listaCajas) {
		this.listaCajas = listaCajas;
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
	 * @return the dto
	 */
	public RegistroEgresosDeCaja getDto() {
		return dto;
	}


	/**
	 * @param dto the dto to set
	 */
	public void setDto(RegistroEgresosDeCaja dto) {
		this.dto = dto;
	}


	/**
	 * @return the listaDto
	 */
	public ArrayList<RegistroEgresosDeCaja> getListaDto() {
		return listaDto;
	}


	/**
	 * @param listaDto the listaDto to set
	 */
	public void setListaDto(ArrayList<RegistroEgresosDeCaja> listaDto) {
		this.listaDto = listaDto;
	}
	

	
	/**
	 * Indica si se debe mostrar el formulario nuevo/modificar
	 * @return the mostrarFormularioIngreso
	 */
	public boolean isMostrarFormularioIngreso() {
		return mostrarFormularioIngreso;
	}

	
	/**
	 * Indica si se debe mostrar el formulario nuevo/modificar
	 * @param mostrarFormularioIngreso the mostrarFormularioIngreso to set
	 */
	public void setMostrarFormularioIngreso(boolean mostrarFormularioIngreso) {
		this.mostrarFormularioIngreso = mostrarFormularioIngreso;
	}
	
	
	
	
	

	/* METODOS */
	
	/**
	 * Reset de la forma
	 */
	public void reset()
	{
		this.dto 						= new RegistroEgresosDeCaja();
		this.listaDto 					= new ArrayList<RegistroEgresosDeCaja>();
		this.mostrarFormularioIngreso	= false;
		this.intTercero					= -1;
		this.listaCajas					= new ArrayList<Cajas>();
		this.listaConceptosEgresoXusuXca= new ArrayList<ConcEgrXusuXcatencion>();
		this.strCaja					= "";
		this.strConceptoEgresoXusuXca	= "";
		//this.cajaSeleccionada			= new Cajas();
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
		ActionErrors errores= new ActionErrors();
		
		if(  (estado.equals("guardar")) || (estado.equals("guardarmodificar"))  )
		{
			// CAJA
			if(getCajaSeleccionada() == null)
			{
				errores.add("error caja", new ActionMessage("errors.required", "La Caja"));
				setMostrarFormularioIngreso(true);
			}
			// -----
			
			// CONCEPTO DE EGRESO POR USUARIO
			if(dto.getConcEgrXusuXcatencion() == null)
			{
				errores.add("error concepto_egreso_por_usuario", new ActionMessage("errors.required", "El concepto de Egreso por Usuario"));
				setMostrarFormularioIngreso(true);
			}
			// -----
			
			// TERCERO
			if(dto.getTerceros() == null)
			{
				errores.add("error tercero", new ActionMessage("errors.required", "El Tercero"));
				setMostrarFormularioIngreso(true);
			}
			// -----
			
			// VALOR EGRESO
			if(dto.getValorEgreso() == null)
			{
				errores.add("error valor_egreso", new ActionMessage("errors.required", "El Valor de Egreso"));
				setMostrarFormularioIngreso(true);
			}
			else {
				
				if(!UtilidadTexto.isNumber(dto.getValorEgreso()+""))
				{
					errores.add("error valor_egreso", new ActionMessage("errors.numDecimales", "El Valor de Egreso"));
					setMostrarFormularioIngreso(true);
				}
				else
				{
					if(dto.getValorEgreso().intValue()<=0)
					{
						errores.add("error valor_egreso", new ActionMessage("error.tesoreria.valorMayorCero", "El Valor de Egreso"));
						setMostrarFormularioIngreso(true);
					}
				}
			}
			// -----
			
			// FACTURA
			if( UtilidadTexto.isEmpty(dto.getFactura()+"")) 
			{
				errores.add("error factura", new ActionMessage("errors.required", "La Factura"));
				setMostrarFormularioIngreso(true);
			}
			// -----
		}
		
		return errores;
	}

	
	
}



