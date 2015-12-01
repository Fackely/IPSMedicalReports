package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadTexto;

import com.princetonsa.dto.odontologia.DtoRecomendaciones;
import com.servinte.axioma.orm.RecomendacionesContOdonto;

/**
 * 
 * @author Edgar Carvajal
 *
 */
public class RecomendacionesContratoForm extends ValidatorForm
{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	/**
	 * POSTARRAY DE LA LISTA 
	 */
	private int postArray;
	/**
	 * 
	 */
	private String estado;
		
	/**
	 * DTO QUE CONTIENE LOS ATRIBUTOS DE RECOMENDACIONES  
	 */
	private DtoRecomendaciones dtoContOdon;
	
	/**
	 * LISTA ALMACENAR LOS DTO RECOMENDACIONES   
	 */
	private List<DtoRecomendaciones> listaDtoRecom;
	
	/**
	 * LISTA DE PARA MOSTRAR LAS RECOMENDACIONES 
	 */
	private List<RecomendacionesContOdonto> listaRecomendaciones = new ArrayList<RecomendacionesContOdonto>();

	/**
	 * ENTIDAD RECOMENDACIONES 
	 */
	private RecomendacionesContOdonto dtoRecomendaciones;
	
	
	/**
	 * LISTA DE CODIGOS RECOMENDACION 
	 * SE UTILIZA COMO AYUDANTE PARA CONSTRUIR EL ARRAY INTEGER DE CODGIOS DE RECOMENDACIONES
	 */
	private String listaCodigosRecomendacion;
	
	
	/**
	 *TAMANIO DE LA LISTA PARA LISTA DTO RECOMENDACIONES  
	 */
	private  int tamanioListaRecomendaciones;
	
	
	/**
	 * TAMANIO PARA LA ENTIDAD RECOMENTACINO
	 */
	private int tamaniolistaEntidadRecomendaciones;
	
	
	/**
	 * ATRIBUTO DE REFERENCIA PARA ORDENA LA LISTA DE RECOMENDACIONES  
	 */
	private String patronOrdenar;
	
	
	/**
	 *CONSTRUTOR 	 
	 */
	public RecomendacionesContratoForm(){
		reset();
	}

	
	
	
	/**
	 * ARRAY LIST QUE CONTIENE LOS CODIGOS RECOMENDACIONES.
	 * ESTE LISTA ES UTILIZADA PARA SABER CUALES CODIGO YA FUERON SELECCIONADOS EN OTRAS FUNCIONALIDADES 
	 */
	 private ArrayList<Integer> listaCodigoPk = new ArrayList<Integer>();
	 
	 
	 /**
	  * CODIGO INSTITUCION
	  */
	 private int codigoInstitucion;
	 
	
	
	/**
	 * RESET
	 * @author Edgar Carvajal Ruiz
	 */
	public void reset()
	{
		this.estado="";
		this.listaRecomendaciones= new ArrayList<RecomendacionesContOdonto>();
		this.dtoRecomendaciones= new RecomendacionesContOdonto();
		this.dtoRecomendaciones.setCodigo("");
		this.dtoRecomendaciones.setActivo(ConstantesBD.acronimoNo);
		this.dtoRecomendaciones.setDescripcion("");
		this.postArray=ConstantesBD.codigoNuncaValido;
		this.listaDtoRecom= new ArrayList<DtoRecomendaciones>();
		this.dtoContOdon = new DtoRecomendaciones();
		this.setListaCodigoPk(new ArrayList<Integer>());
		this.tamanioListaRecomendaciones=ConstantesBD.codigoNuncaValido;
		this.tamaniolistaEntidadRecomendaciones=ConstantesBD.codigoNuncaValido;
		this.patronOrdenar="";
	}
	
	
	
	/**
	 * RESET INSTITUCION
	 * Reset listaCodigosRecomendaciones 
	 */
	public void  reset2()
	{
		this.codigoInstitucion= ConstantesBD.codigoNuncaValido;
		this.listaCodigosRecomendacion = "";

	}
	
	
	
	/**
	 * VALIDACIONES GENERALES DE PRESENTACION
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		
		ActionErrors errores= new ActionErrors();
	
		
		/*
		 * VALIDACION CAMPOS REQUERIDOS 
		 */
		
		if(   (this.getEstado().equals("guardar"))   ||   (this.getEstado().equals("guardarModificar")) )
		{
			
			/*
			if(UtilidadTexto.isEmpty(this.dtoRecomendaciones.getCodigo())  )
			{
				errores.add("",	new ActionMessage("errors.notEspecific","El Código es Requerido"));
			}
			*/
		
			if(UtilidadTexto.isEmpty(this.dtoRecomendaciones.getDescripcion())  )
			{
				errores.add("",	new ActionMessage("errors.notEspecific","La Descripción es Requerida"));
			}
			
			if(!errores.isEmpty())
			{
				if(this.getEstado().equals("guardarModificar"))
				{
					this.setEstado("erroresModificar");
				}
				else if (this.getEstado().equals("guardar"))
				{
					this.setEstado("errores");
				}
			}	
			
		}

		
		return errores;
		
		
	}
	
	
	
	

	
	
	
	
	
	/**
	 * @return the postArray
	 */
	public int getPostArray() {
		return postArray;
	}



	/**
	 * @param postArray the postArray to set
	 */
	public void setPostArray(int postArray) {
		this.postArray = postArray;
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
	 * @return the dtoContOdon
	 */
	public DtoRecomendaciones getDtoContOdon() {
		return dtoContOdon;
	}



	/**
	 * @param dtoContOdon the dtoContOdon to set
	 */
	public void setDtoContOdon(DtoRecomendaciones dtoContOdon) {
		this.dtoContOdon = dtoContOdon;
	}



	/**
	 * @return the listaDtoRecom
	 */
	public List<DtoRecomendaciones> getListaDtoRecom() {
		return listaDtoRecom;
	}



	/**
	 * @param listaDtoRecom the listaDtoRecom to set
	 */
	public void setListaDtoRecom(List<DtoRecomendaciones> listaDtoRecom) {
		this.listaDtoRecom = listaDtoRecom;
	}



	/**
	 * @return the listaRecomendaciones
	 */
	public List<RecomendacionesContOdonto> getListaRecomendaciones() {
		return listaRecomendaciones;
	}



	/**
	 * @param listaRecomendaciones the listaRecomendaciones to set
	 */
	public void setListaRecomendaciones(
			List<RecomendacionesContOdonto> listaRecomendaciones) {
		this.listaRecomendaciones = listaRecomendaciones;
	}



	/**
	 * @return the dtoRecomendaciones
	 */
	public RecomendacionesContOdonto getDtoRecomendaciones() {
		return dtoRecomendaciones;
	}



	/**
	 * @param dtoRecomendaciones the dtoRecomendaciones to set
	 */
	public void setDtoRecomendaciones(RecomendacionesContOdonto dtoRecomendaciones) {
		this.dtoRecomendaciones = dtoRecomendaciones;
	}







	public void setListaCodigoPk(ArrayList<Integer> listaCodigoPk) {
		this.listaCodigoPk = listaCodigoPk;
	}







	public ArrayList<Integer> getListaCodigoPk() {
		return listaCodigoPk;
	}







	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}







	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}



	public void setListaCodigosRecomendacion(String listaCodigosRecomendacion) {
		this.listaCodigosRecomendacion = listaCodigosRecomendacion;
	}



	public String getListaCodigosRecomendacion() {
		return listaCodigosRecomendacion;
	}



	public void setTamanioListaRecomendaciones(int tamanioListaRecomendaciones) {
		this.tamanioListaRecomendaciones = tamanioListaRecomendaciones;
	}



	/**
	 * RETORNA EL TAMANIO DE LA LISTA QUE UN DTO UTILIZADO PARA LA VISTA.
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public int getTamanioListaRecomendaciones() {
		tamanioListaRecomendaciones=listaDtoRecom.size();
		return tamanioListaRecomendaciones;
	}



	public void setTamaniolistaEntidadRecomendaciones(
			int tamaniolistaEntidadRecomendaciones) {
		this.tamaniolistaEntidadRecomendaciones = tamaniolistaEntidadRecomendaciones;
	}



	public int getTamaniolistaEntidadRecomendaciones() 
	{
		this.tamaniolistaEntidadRecomendaciones=listaRecomendaciones.size();
		return tamaniolistaEntidadRecomendaciones;
	}



	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}



	public String getPatronOrdenar() {
		return patronOrdenar;
	}
	
	
	
	


	
	
	
	
	
	
	
	

}
