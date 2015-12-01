package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadTexto;

import com.princetonsa.dto.facturacion.DtoContactoEmpresa;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ContactosEmpresa;

/**
 * 
 * @author axioma
 *
 */



public class ContactosEmpresaForm  extends ValidatorForm  {
	
	
	
	private boolean empezarConsulta;
	
	
	/**
	 * 
	 */
	
	private int codEmpresa;
	/**
	 * 
	 */
	private ArrayList<DtoContactoEmpresa> listContactosEmpresa =new ArrayList<DtoContactoEmpresa>();
	
	private ArrayList<DtoContactoEmpresa> resulatdoBusqueda =new ArrayList<DtoContactoEmpresa>();
	/**
	 * 
	 */
	private String estado;
	
	/**
	 * 
	 */
	private int posArray;
	
	/**
	 * 
	 */
	private String criterioBusqueda;
	
	/**
	 * 
	 */
	
	private DtoContactoEmpresa contactosEmpresa;
	
	private String logContactosOriginal="";
	
	/**
	 * para el ordenamiento
	 */
	private String patronOrdenar;
	
	private String estadoAnterior;
  

	public void reset(){
		this.contactosEmpresa =new DtoContactoEmpresa();
		this.listContactosEmpresa =new ArrayList<DtoContactoEmpresa>();
		this.resulatdoBusqueda = new ArrayList<DtoContactoEmpresa>();
	    this.estado="";
		this.posArray=ConstantesBD.codigoNuncaValido;
		this.criterioBusqueda="";
		this.patronOrdenar="";
		this.logContactosOriginal="";
		//this.codEmpresa=ConstantesBD.codigoNuncaValido;
		
		this.patronOrdenar="";
		this.estadoAnterior="";
	     } 
	
	
	/**
	 * 
	 */
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		
		
		if(this.getEstado().equals("guardar") || this.getEstado().equals("guardarModificar"))
	 	{
			
			validarNombre(errores, usuario);
			
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
	 * @param error
	 * @param usuario
	 */
	public void  validarNombre(ActionErrors error,UsuarioBasico usuario){
		
		
		if(UtilidadTexto.isEmpty(this.getContactosEmpresa().getNombre()))
				{
				error.add("", new ActionMessage("errors.required", "El Nombre"));
				}
		else
		{
			if(this.estado.equals("guardar"))
				{
					validarExistenciaNombre(error, usuario);
				}
		}

		
		
		
	}

	/**
	 * 
	 * @param error
	 * @param usuario
	 */
	private void validarExistenciaNombre(ActionErrors error, UsuarioBasico usuario ) {
		DtoContactoEmpresa dto = new DtoContactoEmpresa();
		dto.setNombre(this.getContactosEmpresa().getNombre());
		dto.setEmpresa(this.getContactosEmpresa().getEmpresa());
		dto.setInstitucion(usuario.getCodigoInstitucionInt());
		
		if(ContactosEmpresa.cargar(dto).size()>0)
		{
			error.add("", new ActionMessage("errors.yaExisteAmplio","El Nombre "+getContactosEmpresa().getNombre(), ". Por favor verifique" ));
			
		}
	}
	
	/**
	 * 
	 * @param listContactosEmpresa
	 */
	public void setListContactosEmpresa(ArrayList<DtoContactoEmpresa> listContactosEmpresa) {
		this.listContactosEmpresa = listContactosEmpresa;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<DtoContactoEmpresa> getListContactosEmpresa() {
		return listContactosEmpresa;
	}

	/**
 	* 
 	* @param estado
 	*/
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * 
	 * @return
	 */
	public String getEstado() {
		return estado;
	}


	/**
	 * 
	 * @param posArray
	 */
	public void setPosArray(int posArray) {
		this.posArray = posArray;
	}

	/**
	 * 
	 * @return
	*/
	public int getPosArray() {
		return posArray;
	}

	/**
	 * 
	 * @param criterioBusqueda
	 */
	public void setCriterioBusqueda(String criterioBusqueda) {
		this.criterioBusqueda = criterioBusqueda;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getCriterioBusqueda() {
		return criterioBusqueda;
	}

	/**
	 * 
	 * @param patronOrdenar
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * 
	 * @return
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * 
	 * @param contactosEmpresa
	 */
	public void setContactosEmpresa(DtoContactoEmpresa contactosEmpresa) {
		this.contactosEmpresa = contactosEmpresa;
	}

	/**
	 * 
	 * @return
	 */
	public DtoContactoEmpresa getContactosEmpresa() {
		return contactosEmpresa;
	}


	public void setResulatdoBusqueda(ArrayList<DtoContactoEmpresa> resulatdoBusqueda) {
		this.resulatdoBusqueda = resulatdoBusqueda;
	}


	public ArrayList<DtoContactoEmpresa> getResulatdoBusqueda() {
		return resulatdoBusqueda;
	}


	public void setCodEmpresa(int codEmpresa) {
		this.codEmpresa = codEmpresa;
	}


	public int getCodEmpresa() {
		return codEmpresa;
	}


	public void setEmpezarConsulta(boolean empezarConsulta) {
		this.empezarConsulta = empezarConsulta;
	}


	public boolean isEmpezarConsulta() {
		return empezarConsulta;
	}


	public String getLogContactosOriginal() {
		return logContactosOriginal;
	}


	public void setLogContactosOriginal(String logContactosOriginal) {
		this.logContactosOriginal = logContactosOriginal;
	}


	public String getEstadoAnterior() {
		return estadoAnterior;
	}


	public void setEstadoAnterior(String estadoAnterior) {
		this.estadoAnterior = estadoAnterior;
	}
	
	
	
	
	

}
