package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadTexto;

import com.princetonsa.dto.odontologia.DtoMotivoDescuento;
import com.princetonsa.mundo.odontologia.MotivosDescuentos;

/**
 * 
 * @author 
 *
 */
public class MotivosDescuentosOdonForm extends ValidatorForm{
	
	/**
	 * variable para manejar el estado del flujo
	 */
	private String estado;
	
	/**
	 * variable para manejar el estado del flujo
	 */
	private String patronOrdenar;
	
	/**
	 * lista descuentos
	 */
	private ArrayList<DtoMotivoDescuento> listMotivosDescuentos;
	
	/**
	 * Constructor
	 */
	public MotivosDescuentosOdonForm()
	{
		this.reset();
	}
	
	/**
	 * 
	 */
	private DtoMotivoDescuento dtoMotivosDesc;
	
	/**
	 * 
	 */
	private int posArray;
	
	/**
	 * 
	 */
	private String mensaje;
	
	/**
	 * AYUDANTE PARA VALIDAR LA INTERFAZ GRAFICA
	 */
	private boolean existeMotivoParametrizado; 
	
	
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
		
		if(this.getEstado().equals("guardar") || this.getEstado().equals("guardarModificar"))
	 	{
			validarCodigo(errores);
			validarDescripcion(errores);
			validarTipo(errores);
			validarIndicativo(errores);
			
			//Validación por anexo 137893
			for (int i=0;i<this.listMotivosDescuentos.size();i++)
			{
				StringTokenizer descripcionDto = new StringTokenizer(this.dtoMotivosDesc.getDescripcion());
				StringTokenizer descripcionLista = new StringTokenizer(this.listMotivosDescuentos.get(i).getDescripcion());
				List<String> descDto = new ArrayList<String>();
				List<String> descLista = new ArrayList<String>();
				
				while (descripcionDto.hasMoreElements()) {
					String desc = (String) descripcionDto.nextElement();
					descDto.add(desc.toUpperCase());
				}
				
				while (descripcionLista.hasMoreElements()) {
					String  desc = (String ) descripcionLista.nextElement();
					descLista.add(desc.toUpperCase());
					
				}
				
				if (descDto.equals(descLista)) {
					errores.add("", new ActionMessage("errors.notEspecific", "Ya exite la descripción \""+this.dtoMotivosDesc.getDescripcion()+"\" para otro motivo ", ""));
				}	
			}
			
			
			
			//Fin Validacion Anexo 137893
		}
		if(this.getEstado().equals("eliminar"))
		{
			validarExistenciaMotivo(errores);
		}
		
		if(!errores.isEmpty())
		{
			if(this.getEstado().equals("guardar"))
				this.estado="mostrarErrores";
			if(this.getEstado().equals("guardarModificar"))
				this.estado="mostrarErroresModificar";
		}
	
		return errores;
	}
	
	/**
	 * 
	 * @param errores
	 */
	private void validarTipo(ActionErrors errores) 
	{
		if(UtilidadTexto.isEmpty(this.getDtoMotivosDesc().getTipo().trim()))
		{
			errores.add("", new ActionMessage("errors.required", "El Tipo"));
		}
		
	}

	
	/**
	 * 
	 * @param errores
	 */
	private void validarIndicativo(ActionErrors errores) 
	{
		if(UtilidadTexto.isEmpty(this.getDtoMotivosDesc().getIndicativo().trim()))
		{
			errores.add("", new ActionMessage("errors.required", "El Indicativo"));
		}
		
	}
	/**
	 * 
	 */
	public void reset() 
	{
		this.dtoMotivosDesc=new DtoMotivoDescuento();
		this.patronOrdenar="";
		this.listMotivosDescuentos= new ArrayList<DtoMotivoDescuento>();
		this.existeMotivoParametrizado=Boolean.FALSE;
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
	 * @return the dtoMotivosDesc
	 */
	public DtoMotivoDescuento getDtoMotivosDesc() {
		return dtoMotivosDesc;
	}

	/**
	 * @param dtoMotivosDesc the dtoMotivosDesc to set
	 */
	public void setDtoMotivosDesc(DtoMotivoDescuento dtoMotivosDesc) {
		this.dtoMotivosDesc = dtoMotivosDesc;
	}

	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}
	
	/**
	 * 
	 * @return
	 */
	
	public int getPosArray() {
		return posArray;
	}
	
	

	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return the listMotivosDescuentos
	 */
	public ArrayList<DtoMotivoDescuento> getListMotivosDescuentos() {
		return listMotivosDescuentos;
	}

	/**
	 * @param listMotivosDescuentos the listMotivosDescuentos to set
	 */
	public void setListMotivosDescuentos(
			ArrayList<DtoMotivoDescuento> listMotivosDescuentos) {
		this.listMotivosDescuentos = listMotivosDescuentos;
	}

	/**
	 * @param posArray the posArray to set
	 */
	public void setPosArray(int posArray) {
		this.posArray = posArray;
	}
	
	/**
	 * 
	 * @param errores
	 */
	private void validarExistenciaCodigo(ActionErrors errores) 
	{
		DtoMotivoDescuento dto = new DtoMotivoDescuento();
		dto.setCodigoMotivo(this.getDtoMotivosDesc().getCodigoMotivo());
        dto.setInstitucion(this.getDtoMotivosDesc().getInstitucion());     
		if(MotivosDescuentos.cargar(dto).size()>0)
		{
			errores.add("", new ActionMessage("errors.yaExisteAmplio", "El código "+dto.getCodigoMotivo(), ". Por favor verifique"));
			
		}
	}
	
	
	/**
	 * 
	 * @param errores
	 */
	
	private void validarCodigo(ActionErrors errores) 
	{
		if(UtilidadTexto.isEmpty(this.getDtoMotivosDesc().getCodigoMotivo().trim()))
		{	
			errores.add("", new ActionMessage("errors.required", "El código"));
		}
		else
		{
			if(this.getEstado().equals("guardar"))
 		   	{	
				validarExistenciaCodigo(errores);
 		   	}
			else if(this.getEstado().equals("guardarModificar"))
			{
				if(!this.getListMotivosDescuentos().get(this.getPosArray()).getCodigoMotivo().equals(this.getDtoMotivosDesc().getCodigoMotivo()+""))
				{
					validarExistenciaCodigo(errores);
				}
				
				
			}
			
		}	
	}

	/**
	 * VALIDAR EXISTENCIA MOTIVO DE DESCUENTO
	 * @param errores
	 */
	private void validarExistenciaMotivo(ActionErrors errores) {
		if( MotivosDescuentos.validarExistenciaMotivos(this.getDtoMotivosDesc().getCodigoPk())>0)
		{
			errores.add("", new ActionMessage("errors.notEspecific", " El Motivo de Atención Ya Esta Utilizado"));
		}
	}
	
	
	

	
	
	
	/**
	 * VALIDAR DESCRIPCION
	 * @param errores
	 */
	private void validarDescripcion(ActionErrors errores)
	{
		if(UtilidadTexto.isEmpty(this.getDtoMotivosDesc().getDescripcion().trim()))
		{
			errores.add("", new ActionMessage("errors.required", "La descripcion"));
		}
		
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * 
	 * @param mensaje
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public void setExisteMotivoParametrizado(boolean existeMotivoParametrizado) {
		this.existeMotivoParametrizado = existeMotivoParametrizado;
	}

	public boolean getExisteMotivoParametrizado() {
		return existeMotivoParametrizado;
	}
}	
