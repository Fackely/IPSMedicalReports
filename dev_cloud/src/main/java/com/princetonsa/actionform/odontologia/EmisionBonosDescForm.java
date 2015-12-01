package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.InfoPacienteBonoPresupuesto;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.odontologia.DtoEmisionBonosDesc;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.EmisionBonosDesc;


/**
 * Form para la funcionalidad Emisi&oacute;n Bonos Descuento Odont&oacute;logico 
 * @author Edgar Carvajal
 * @since Agosto 2009
 */
@SuppressWarnings("serial")
public class EmisionBonosDescForm  extends ValidatorForm {
	
	/** 
	 * Temporal para el nombre del convenio
	 */
	private String tmpNombreConvenio="";
	
	/** 
	 * C&oacute;digo del convenio seleccionado
	 */
	private int codigoConvenio;
	
	/**
	 * Lista con la emisi&oacute;n de bonos de descuento odontol&oacute;gico
	 */
	private ArrayList<DtoEmisionBonosDesc> listaEmisionBonosDesc= new ArrayList<DtoEmisionBonosDesc>();
	
	/**	
	 * Resultado de la b&uacute;squeda de los descuentos odontol&oacute;gicos
	 */
	private ArrayList<DtoEmisionBonosDesc> resultadoBusqueda= new ArrayList<DtoEmisionBonosDesc>();
	
	/**
	 * DTO utilizado para el ingreso de la emisi&oacute;n
	 */
	private DtoEmisionBonosDesc dtoEmisionBonosDesc;
	
	/**
	 * Estado para el flujo de la aplicaci&oacute;n 
	 */
	private String estado;
	
	/**
	 * Posici&oacute;n del array para las modificaciones y eliminaciones
	 */
	private int posArray;
	
	/**
	 * Criterio de b&uacute;squeda
	 */
	private String criterioBusqueda;
	
	/**
	 * Patr&oacute;n de ordenamiento
	 */
	private String patronOrdenar;
	
	/**
	 * Indica si el ordenamiento es ascendente o descendente
	 */
	private String esDescendente;
 
	/**
	 * Indica si utiliza programas o servicios
	 */
	private String utilizaProgramas;
	
	/**
	 * Lista de los convenios para seleccionar
	 */
	private ArrayList<HashMap<String, Object>> listConvenios = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * Temporal para el nombre del programa
	 */
	private String tmpNombrePrograma;
	
	/**
	 * Tama&ntilde;o de la lista
	 */
	private int tamanoListaPaciente;
	
	/**
	 * DTO para el manejo de la informaci&oacute;n del paciente
	 */
	private InfoPacienteBonoPresupuesto dtoInfoPaciente ;
	private ArrayList<InfoPacienteBonoPresupuesto> listaPaciente ;
	
	/**
	 * Limpia los DTO del formulario 
	 */
	public void resetBusqueda(){
		this.dtoInfoPaciente = new InfoPacienteBonoPresupuesto();
		this.setListaPaciente(new ArrayList<InfoPacienteBonoPresupuesto>());
		this.listaPaciente= new ArrayList<InfoPacienteBonoPresupuesto>();
		this.setTamanoListaPaciente(0);
	}
	
	/**
	 * Limpia los campos del formulario
	 */
	public void reset()
	{
			this.tmpNombrePrograma="";
			this.listaEmisionBonosDesc= new ArrayList<DtoEmisionBonosDesc>();
			this.resultadoBusqueda= new ArrayList<DtoEmisionBonosDesc>();
			this.dtoEmisionBonosDesc= new DtoEmisionBonosDesc();
			this.posArray=0;
			this.estado="";
			this.criterioBusqueda="";
			this.patronOrdenar="";
			if(this.estado.equals("empezar"))
			{
				this.listConvenios= new ArrayList<HashMap<String,Object>>();
			}
			this.utilizaProgramas=ConstantesBD.acronimoSi;
			this.esDescendente="";
	}
	
	

	
	/**
	 * Validador de los campos de la forma
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request 	) 
	{
		ActionErrors errores = new ActionErrors();
		UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		
		if(this.getEstado().equals("guardar"))
		{
			validarRangoSerialesFechas(errores,0, usuario);
		}
		if (this.getEstado().equals("guardarModificar"))
		{
			MessageResources resources=MessageResources.getMessageResources("com.servinte.mensajes.odontologia.EmisionBonosDescForm");
			validacionesPorDefecto(errores,resources);
			validarRangoSerialesFechas(errores,this.getDtoEmisionBonosDesc().getCodigo(), usuario);
		}
		
		if(this.getEstado().equals("eliminar"))
		{
			this.validarExistenciaBonos(errores);
		}
		
		if(!errores.isEmpty())
		{
			if(this.getEstado().equals("guardar"))
				this.estado="mostrarErrores";
			if(this.getEstado().equals("guardarModificar"))
				this.estado="mostrarErroresModificar";
			if(this.getEstado().equals("eliminar"))
				this.estado="mostrarErroresEliminar";
		}
		
	return errores;
	}

	/**
	 * Valida la existencia de los abonos
	 * @param errores {@link ActionErrors} Errores presentados en la validaci&oacute;n
	 */
	private void validarExistenciaBonos(ActionErrors errores) {
		if( EmisionBonosDesc.existeSerialSubCuentas(this.getListaEmisionBonosDesc().get(this.getPosArray()).getSerialInicial(), this.getListaEmisionBonosDesc().get(this.getPosArray()).getSerialFinal(),this.getListaEmisionBonosDesc().get(this.getPosArray()).getConvenioPatrocinador().getCodigo()))
		{
			errores.add("", new ActionMessage("errors.notEspecific", "No Se Permite La Eliminación, El Bono Esta Asociado A Un Paciente"));
		}
	}

	/**
	 * Validaciones generales del sistema
	 * @param errores {@link ActionErrors} Errores presentados en la validaci&oacute;n
	 * @param messageResources {@link MessageResources} Objeto de mensajes para internacionalizaci&oacute;n
	 */
	private void validacionesPorDefecto(ActionErrors errores, MessageResources messageResources) {
		validarConvenio(errores);
		validarPrograma(errores);
		validarTamanoSerial(errores); // VALIDAR
		validarFecha(errores);
		valorDescuentoPorcentaje(errores);
		validacionesId(errores, messageResources);
		
	}	 
	
	/**
	 * Valida que los ID sean requeridos
	 * @param errores {@link ActionErrors} Errores presentados en la validaci&oacute;n
	 * @param messageResources {@link MessageResources} Objeto de mensajes para internacionalizaci&oacute;n
	 */
	private void validacionesId(ActionErrors errores, MessageResources messageResources) {
		if(UtilidadTexto.isEmpty(this.dtoEmisionBonosDesc.getId()))
		{
			errores.add("", new ActionMessage("errors.required", messageResources.getMessage("EmisionBonosDescForm.id")));
		}
	}	 
	 
	/**
	 * 
	 * @param errores
	 */
	private void validarFecha(ActionErrors errores) 
		{
		
		if(this.getDtoEmisionBonosDesc().getFechaVigenciaInicial().isEmpty())
			 errores.add("", new ActionMessage("errors.required", "La fecha de vigencia inicial "));
		
		if(this.getDtoEmisionBonosDesc().getFechaVigenciaFinal().isEmpty())
			 errores.add("", new ActionMessage("errors.required", "La fecha de vigencia final "));
		
		if(!UtilidadFecha.esFechaValidaSegunAp(this.getDtoEmisionBonosDesc().getFechaVigenciaInicial())&&!this.getDtoEmisionBonosDesc().getFechaVigenciaInicial().isEmpty())
				{
			    errores.add("", new ActionMessage("errors.formatoFechaInvalido", this.getDtoEmisionBonosDesc().getFechaVigenciaInicial()));    
			     }
		 if(!UtilidadFecha.esFechaValidaSegunAp(this.getDtoEmisionBonosDesc().getFechaVigenciaFinal())&&!this.getDtoEmisionBonosDesc().getFechaVigenciaFinal().isEmpty())
				{
			    errores.add("", new ActionMessage("errors.formatoFechaInvalido", this.getDtoEmisionBonosDesc().getFechaVigenciaFinal()));    
			     }
		 if(!UtilidadTexto.isEmpty(this.getDtoEmisionBonosDesc().getFechaVigenciaInicial())&&!UtilidadTexto.isEmpty(this.getDtoEmisionBonosDesc().getFechaVigenciaFinal()))
		 {
			 if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getDtoEmisionBonosDesc().getFechaVigenciaInicial(), this.getDtoEmisionBonosDesc().getFechaVigenciaFinal()))
			 		{
			 			errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual","La Fecha Incial"+this.getDtoEmisionBonosDesc().getFechaVigenciaInicial(),"Fecha Final"+this.getDtoEmisionBonosDesc().getFechaVigenciaFinal()));
			 		}
		 	}
		}
	
	/**
	 * 
	 * @param errores
	 */
	private void valorDescuentoPorcentaje(ActionErrors errores) 
	{
		
		
		if(this.getDtoEmisionBonosDesc().getValorDescuento()<=0 && this.getDtoEmisionBonosDesc().getPorcentajeDescuento()<=0)
		{
			errores.add("", new ActionMessage("errors.required", " El Valor de Descuento 0 el Porcentaje"));
		}
		if(this.getDtoEmisionBonosDesc().getValorDescuento()<=0)
		{
		 if(this.getDtoEmisionBonosDesc().getPorcentajeDescuento()>=0)
			 if(this.getDtoEmisionBonosDesc().getPorcentajeDescuento()>100)
			 {
				errores.add("", new ActionMessage("errors.range"," El Porcentaje " ," 1","100%"));
			 }
		}

		
	 }
	
	
	/**
	 * 
	 * @param errores
	 */
	private void validarTamanoSerial(ActionErrors errores){
		
		
		if(this.getDtoEmisionBonosDesc().getSerialInicial().doubleValue()>0 && this.getDtoEmisionBonosDesc().getSerialFinal().doubleValue()>0)
		{
		 if(this.getDtoEmisionBonosDesc().getSerialInicial().doubleValue()>=this.getDtoEmisionBonosDesc().getSerialFinal().doubleValue())
		   {
				errores.add("", new ActionMessage("errors.MayorQue", "Serial Final","Serial Inicial"));
		   }
	   }
		else
		{
			errores.add("", new ActionMessage("errors.required", "Los Seriales"));
		}	
	}
	
	/**
	 * 
	 * @param errores
	 */
	private void validarConvenio(ActionErrors errores )
	{
		if(this.getDtoEmisionBonosDesc().getConvenioPatrocinador().getCodigo()<=0)
		{
		  errores.add("", new ActionMessage("errors.required", "El Convenio"));
		}
	}
	
	/**
	 * 
	 * @param errores
	 */
	private void validarPrograma(ActionErrors errores )
	{
		
			if(this.utilizaProgramas.equals(ConstantesBD.acronimoSi))
			{
					if(this.getDtoEmisionBonosDesc().getPrograma().getCodigo()<=0)
					{
						errores.add("", new ActionMessage("errors.required", "El Programa"));
					}
			}
			else
				
			{
				if(this.getDtoEmisionBonosDesc().getServicio().getCodigo()<=0)
				{
					errores.add("", new ActionMessage("errors.required", "El Servicio"));
				}
			}
	}
	
	/**
	 * 
	 * @param errores
	 * @param codigoPkNotIn
	 */
	public void validarRangoSerialesFechas(ActionErrors errores,  double codigoPkNotIn , UsuarioBasico usuario )
	{
		this.getDtoEmisionBonosDesc().setInstitucion(usuario.getCodigoInstitucionInt());
		
		if(EmisionBonosDesc.existeCruceSerialesIdYConvenio(this.getDtoEmisionBonosDesc(), codigoPkNotIn)){
			errores.add("", new ActionMessage("errors.crucesSeriales",this.getDtoEmisionBonosDesc().getConvenioPatrocinador().getDescripcion(), this.getDtoEmisionBonosDesc().getSerialInicial(),this.getDtoEmisionBonosDesc().getSerialFinal()));
		}
		
		if(EmisionBonosDesc.existeCruceFechasConvenioPrograma(this.getDtoEmisionBonosDesc(), codigoPkNotIn)){
			errores.add("", new ActionMessage("errors.crucesFechas",this.getDtoEmisionBonosDesc().getPrograma().getCodigo(), this.getDtoEmisionBonosDesc().getFechaVigenciaInicial(),this.getDtoEmisionBonosDesc().getFechaVigenciaFinal()));
     	}	
	}

	/**
	 * @return the listaEmisionBonosDesc
	 */
				
	public ArrayList<DtoEmisionBonosDesc> getListaEmisionBonosDesc() {
		return listaEmisionBonosDesc;
	}

	/**
	 * @param listaEmisionBonosDesc the listaEmisionBonosDesc to set
	 */
	public void setListaEmisionBonosDesc(
			ArrayList<DtoEmisionBonosDesc> listaEmisionBonosDesc) {
		this.listaEmisionBonosDesc = listaEmisionBonosDesc;
	}

	/**
	 * @return the resultadoBusqueda
	 */
	public ArrayList<DtoEmisionBonosDesc> getResultadoBusqueda() {
		return resultadoBusqueda;
	}

	/**
	 * @param resultadoBusqueda the resultadoBusqueda to set
	 */
	public void setResultadoBusqueda(
			ArrayList<DtoEmisionBonosDesc> resultadoBusqueda) {
		this.resultadoBusqueda = resultadoBusqueda;
	}

	/**
	 * @return the dtoEmisionBonosDesc
	 */
	public DtoEmisionBonosDesc getDtoEmisionBonosDesc() {
		return dtoEmisionBonosDesc;
	}

	/**
	 * @param dtoEmisionBonosDesc the dtoEmisionBonosDesc to set
	 */
	public void setDtoEmisionBonosDesc(DtoEmisionBonosDesc dtoEmisionBonosDesc) {
		this.dtoEmisionBonosDesc = dtoEmisionBonosDesc;
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

	public InfoPacienteBonoPresupuesto getDtoInfoPaciente() {
		return dtoInfoPaciente;
	}




	public void setDtoInfoPaciente(InfoPacienteBonoPresupuesto dtoInfoPaciente) {
		this.dtoInfoPaciente = dtoInfoPaciente;
	}




	/**
	 * @return the criterioBusqueda
	 */
	public String getCriterioBusqueda() {
		return criterioBusqueda;
	}

	/**
	 * @param criterioBusqueda the criterioBusqueda to set
	 */
	public void setCriterioBusqueda(String criterioBusqueda) {
		this.criterioBusqueda = criterioBusqueda;
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
	 * @return the listConvenios
	 */
	public ArrayList<HashMap<String, Object>> getListConvenios() {
		return listConvenios;
	}

	/**
	 * @param listConvenios the listConvenios to set
	 */
	public void setListConvenios(ArrayList<HashMap<String, Object>> listConvenios) {
		this.listConvenios = listConvenios;
	}

	/**
	 * 
	 * @param codigoConvenio
	 */
	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	
	/**
	 * 
	 * @return
	 */

	public int getCodigoConvenio() {
		return codigoConvenio;
	}


	/**
	 * 
	 * @param tmpNombrePrograma
	 */
	public void setTmpNombrePrograma(String tmpNombrePrograma) {
		this.tmpNombrePrograma = tmpNombrePrograma;
	}

	/**
	 * 
	 * @return
	 */

	public String getTmpNombrePrograma() {
		return tmpNombrePrograma;
	}



	public void setTmpNombreConvenio(String tmpNombreConvenio) {
		this.tmpNombreConvenio = tmpNombreConvenio;
	}



	public String getTmpNombreConvenio() {
		return tmpNombreConvenio;
	}




	public void setUtilizaProgramas(String utilizaProgramas) {
		this.utilizaProgramas = utilizaProgramas;
	}




	public String getUtilizaProgramas() {
		return utilizaProgramas;
	}




	public void setListaPaciente(ArrayList<InfoPacienteBonoPresupuesto> listaPaciente) {
		this.listaPaciente = listaPaciente;
	}




	public ArrayList<InfoPacienteBonoPresupuesto> getListaPaciente() {
		return listaPaciente;
	}




	/**
	 * @param tamanoListaPaciente the tamanoListaPaciente to set
	 */
	public void setTamanoListaPaciente(int tamanoListaPaciente) {
		this.tamanoListaPaciente = tamanoListaPaciente;
	}




	/**
	 * @return the tamanoListaPaciente
	 */
	public int getTamanoListaPaciente() {
		tamanoListaPaciente=this.listaEmisionBonosDesc.size();
		return tamanoListaPaciente;
	}

	/**
	 * @param esDescendente the esDescendente to set
	 */
	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}

	/**
	 * @return the esDescendente
	 */
	public String getEsDescendente() {
		return esDescendente;
	}
   
}

