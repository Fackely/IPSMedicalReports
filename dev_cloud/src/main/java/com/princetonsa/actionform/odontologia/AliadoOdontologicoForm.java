package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadTexto;

import com.princetonsa.dto.odontologia.DtoAliadoOdontologico;

import com.princetonsa.mundo.odontologia.AliadoOdontologico;


/**
 * 
 * @author axioma
 *
 */
public class AliadoOdontologicoForm extends ValidatorForm  
{
	/**
	 * 
	 */
	private ArrayList<HashMap<String, Object>> terceroRazonSocial= new ArrayList<HashMap<String,Object>>();
	
	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(AliadoOdontologicoForm.class);
	
	/**
	 * 
	 */
	
	private DtoAliadoOdontologico dtoAliadoOdontologico= new DtoAliadoOdontologico();
	
	/**
	 * 
	 */
	private String estado;
	
	/**
	 * 
	 */
	private String patronOrdenar;
	
	/**
	 * 
	 */
	private String esDescendente;
	
	/**
	 * 
	 */
	private int posArray;
	
	/**
	 * lista aliado
	 */
	private ArrayList<DtoAliadoOdontologico> listaAliadoOdontologico= new ArrayList<DtoAliadoOdontologico>();

	/**
	 * 
	 */
	public void reset(boolean resetListado)
	{
		this.dtoAliadoOdontologico = new DtoAliadoOdontologico();
		if(resetListado)
			this.listaAliadoOdontologico = new ArrayList<DtoAliadoOdontologico>();
		this.posArray = ConstantesBD.codigoNuncaValido;
		this.patronOrdenar = "";
		this.esDescendente = "";
		this.terceroRazonSocial = new ArrayList<HashMap<String,Object>>();
	}
	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) 
	{
      
		ActionErrors errores = new ActionErrors();
		
		if(this.getEstado().equals("resultadoBusquedaAvanzada"))
		{
			
			
			if(UtilidadTexto.isEmpty(this.getDtoAliadoOdontologico().getDescripcion())  &&  UtilidadTexto.isEmpty(this.getDtoAliadoOdontologico().getNitTerceroRazSoci()) )
			{
				errores.add("", new ActionMessage("errors.notEspecific", " Es Requerido Almenos un Criterio Para Realizar La Busqueda"));
			}
			
			if(!errores.isEmpty())
			{
				
				this.setEstado("ErroresbusquedaAvanzada");
				
			}
			
		}
		
		if(this.getEstado().equals("guardar") || this.getEstado().equals("guardarModificar"))
	 	{
			logger.info("paso por aqui !!! A");
			validarCodigo(errores);
			validarDescripcion(errores);
			validarTerceros(errores);
			validarDireccion(errores);
			validarTelefono(errores);
			validadEstado(errores);
			logger.info("errores es vacio? "+errores.isEmpty());
			if(!errores.isEmpty())
			{
				if(this.getEstado().equals("guardar"))
					this.estado="mostrarErrores";
				if(this.getEstado().equals("guardarModificar"))
					this.estado="mostrarErroresModificar";
				
			}
	 	}
      return errores;
   }
	
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> getTerceroRazonSocial() {
		return terceroRazonSocial;
	}
	
	/**
	 * 
	 * @param terceroRazonSocial
	 */
	public void setTerceroRazonSocial(
			ArrayList<HashMap<String, Object>> terceroRazonSocial) {
		this.terceroRazonSocial = terceroRazonSocial;
	}
	
	/**
	 * 
	 * @param errores
	 */
	private void validarCodigo(ActionErrors errores) 
	{
		if(UtilidadTexto.isEmpty(this.getDtoAliadoOdontologico().getCodigo().trim()))
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
				if(!this.getListaAliadoOdontologico().get(this.getPosArray()).getCodigo().equals(this.getDtoAliadoOdontologico().getCodigo()+""))
				{
					validarExistenciaCodigo(errores);
				}
			}
		}	
	}


	/**
	 * 
	 * @param errores
	 */
	private void validarExistenciaCodigo(ActionErrors errores) 
	{
		DtoAliadoOdontologico dto = new DtoAliadoOdontologico();
		dto.setCodigo(this.getDtoAliadoOdontologico().getCodigo());
        dto.setInstitucion(this.getDtoAliadoOdontologico().getInstitucion());     
		if(AliadoOdontologico.cargar(dto).size()>0)
		{
			errores.add("", new ActionMessage("errors.yaExisteAmplio", "El código "+dto.getCodigo(), ". Por favor verifique"));
			
		}
	}
	
	/**
	 * 
	 * @param errores
	 */
	private void validarDescripcion(ActionErrors errores)
	{
		if(UtilidadTexto.isEmpty(this.getDtoAliadoOdontologico().getDescripcion().trim()))
		{
			errores.add("", new ActionMessage("errors.required", "La descripcion"));
		}
		
	}
	
	/**
	 * 
	 * @param errores
	 */
	private void validarTerceros(ActionErrors errores)
	{
		
		if(this.getDtoAliadoOdontologico().getTerceros().getCodigo()<=0)
		{
			errores.add("", new ActionMessage("errors.required","El tercero"));
			logger.info("EL TERCERO ES--->"+ dtoAliadoOdontologico.getTerceros());
		}
	}
	
	/**
	 * 
	 * @param errores
	 */
	private void validarDireccion(ActionErrors errores)
	{
		if(UtilidadTexto.isEmpty(this.getDtoAliadoOdontologico().getDireccion().trim()))
		{
			errores.add("", new ActionMessage("errors.required","La direccion"));
		}
	}
	
	/**
	 * 
	 * @param errores
	 */
	private void validarTelefono(ActionErrors errores)
	{
		if(UtilidadTexto.isEmpty(this.getDtoAliadoOdontologico().getTelefono().trim()))
		{
			errores.add("", new ActionMessage("errors.required","El telefono"));
		}
	}
	
	/**
	 * 
	 * @param errores
	 */
	private void validadEstado(ActionErrors errores)
	{
		if(UtilidadTexto.isEmpty(this.getDtoAliadoOdontologico().getEstado()))
		{
			errores.add("", new ActionMessage("errors.required","El estado"));
		}
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
	 * @param dtoAliadoOdontologicos the dtoAliadoOdontologicos to set
	 */
	public void setDtoAliadoOdontologico(
			DtoAliadoOdontologico dtoAliadoOdontologico) {
		this.dtoAliadoOdontologico = dtoAliadoOdontologico;
	}

	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}
	
	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return the esDescendente
	 */
	public String getEsDescendente() {
		return esDescendente;
	}

	/**
	 * @param esDescendente the esDescendente to set
	 */
	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}

	/**
	 * @return the posArray
	 */
	public int getPosArray() {
		return posArray;
	}

	/**
	 * @param posArray the posArray to set
	 */
	public void setPosArray(int posArray) {
		this.posArray = posArray;
	}

	/**
	 * @return the dtoAliadoOdontologico
	 */
	public DtoAliadoOdontologico getDtoAliadoOdontologico() {
		return dtoAliadoOdontologico;
	}


	/**
	 * @return the listaAliadoOdontologico
	 */
	public ArrayList<DtoAliadoOdontologico> getListaAliadoOdontologico() {
		return listaAliadoOdontologico;
	}


	/**
	 * @param listaAliadoOdontologico the listaAliadoOdontologico to set
	 */
	public void setListaAliadoOdontologico(
			ArrayList<DtoAliadoOdontologico> listaAliadoOdontologico) {
		this.listaAliadoOdontologico = listaAliadoOdontologico;
	}

	
}