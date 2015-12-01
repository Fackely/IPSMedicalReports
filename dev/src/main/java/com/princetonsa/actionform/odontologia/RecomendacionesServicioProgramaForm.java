package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
//import util.odontologia.recomendacion.InfoRecomeSerProg;

import com.servinte.axioma.orm.Programas;
import com.servinte.axioma.orm.RecomSerproSerpro;
import com.servinte.axioma.orm.RecomendacionesContOdonto;
import com.servinte.axioma.orm.RecomendacionesServProg;

/**
 * 
 * @author Edgar Carvajal
 *
 */
public class RecomendacionesServicioProgramaForm extends ValidatorForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	
	/**
	 * ATRIBUTO PARA MANEJAR EL ESTADO DE LA PAGINA 
	 */
	private String estado;
	
	
	
	/**
	 * LISTA DE CODIGO DE RECOMENDACIONES 
	 */
	private ArrayList<Integer> listaCodigos;
	
	
	
	/////////////////// ATRIBUTOS PARA EL MANEJO DE LA RECOMENDACIONES 
	


	/**
	 * DTO PARA LA RECOMENDACIONES CONTRATO
	 */
	private  RecomendacionesContOdonto dtoRecomendaciones;
	
	/**
	 * LISTA DE RECOMENDACIONES 
	 */
	private ArrayList<RecomendacionesContOdonto> listaRecomendaciones;
	

	
	
	//////////////////--------------
	
	/**
	 * Post Array de la lista Servicio programa
	 */
	private int postArrayServicioPrograma;
	
	/**
	 * Post array de las recomendaciones 
	 */
	private int postArrayRecomendacion;
	
	
	/**
	 * 	LISTA CODIGOS RECOMENDACIONES  
	 */
	private String listaCodigoRecomendaciones;
	
	
	
	
	/**
	 * @return the listaCodigoRecomendaciones
	 */
	public String getListaCodigoRecomendaciones() {
		return listaCodigoRecomendaciones;
	}










	/**
	 * @param listaCodigoRecomendaciones the listaCodigoRecomendaciones to set
	 */
	public void setListaCodigoRecomendaciones(String listaCodigoRecomendaciones) {
		this.listaCodigoRecomendaciones = listaCodigoRecomendaciones;
	}










	/**
	 * @return the listaCodigoProgramaServicios
	 */
	public String getListaCodigoProgramaServicios() {
		return listaCodigoProgramaServicios;
	}










	/**
	 * @param listaCodigoProgramaServicios the listaCodigoProgramaServicios to set
	 */
	public void setListaCodigoProgramaServicios(String listaCodigoProgramaServicios) {
		this.listaCodigoProgramaServicios = listaCodigoProgramaServicios;
	}










	/**
	 * LISTA CODIGOS PROGRAMAS SERVICIOS
	 */
	private String listaCodigoProgramaServicios;
	

	
	
	
	
	
	/////////////////// ATRIBUTOS PARA EL MANEJO DE LAS RECOMENDACIONES SERVICIO PROGRAMA

	
	/**
	 * DTO PARA MANEJAR LA RECOMENDACIONES SERVICIO PROGRAMA
	 */
	private RecomendacionesServProg dtoRecomenServicioPrograma;
	
	/**
	 * LISTA DE RECOMENDACIONES SERVICIO PROGRAMA
	 */
	private List<RecomendacionesServProg> listaRecomenSerProg;
	
	/**
	 * DTO RECOMENDACIONES PROG SER PROG SER
	 */
	private RecomSerproSerpro dtoSerProSerPro = new RecomSerproSerpro();
	
	/**
	 * 
	 */
	private String[] lisCodRecomendacion; 

	/**
	 * RESET
	 * @author Edgar Carvajal Ruiz
	 */
	public void  reset()
	{
		this.estado="";
		this.dtoRecomendaciones= new RecomendacionesContOdonto();
		this.dtoRecomenServicioPrograma = new RecomendacionesServProg();
		this.listaRecomendaciones = new ArrayList<RecomendacionesContOdonto>();
		this.listaRecomenSerProg = new ArrayList<RecomendacionesServProg>();
		this.lisCodRecomendacion = new String[]{};
		this.setDtoSerProSerPro(new RecomSerproSerpro());
		this.dtoSerProSerPro = new RecomSerproSerpro();
		this.setListaCodigos(new ArrayList<Integer>());
		this.dtoSerProSerPro.setProgramas(new Programas());
		this.postArrayRecomendacion=ConstantesBD.codigoNuncaValido;
		this.postArrayServicioPrograma=ConstantesBD.codigoNuncaValido;
		this.listaCodigoRecomendaciones="";
		this.listaCodigoProgramaServicios="";
	}
	
	
	
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.struts.validator.ValidatorForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		
		ActionErrors errores= new ActionErrors();
		
		if(this.estado.equals("guardar"))
		{
			if(this.getDtoRecomenServicioPrograma().getRecomSerproSerpros().size()<=0)
			{
				errores.add("",	new ActionMessage("errors.notEspecific","Debe asociar como minimo una Programa/Servicio"));
			}
			
			if(this.getDtoRecomenServicioPrograma().getRecomendacionesContOdontos().size()<=0)
			{
				errores.add("",	new ActionMessage("errors.notEspecific","Debe asociar como minimo una recomendacion"));
			}
		}
			
		return errores;
		
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


	/**
	 * @return the listaRecomendaciones
	 */
	public ArrayList<RecomendacionesContOdonto> getListaRecomendaciones() {
		return listaRecomendaciones;
	}


	/**
	 * @param listaRecomendaciones the listaRecomendaciones to set
	 */
	public void setListaRecomendaciones(
			ArrayList<RecomendacionesContOdonto> listaRecomendaciones) {
		this.listaRecomendaciones = listaRecomendaciones;
	}


	/**
	 * @return the dtoRecomenServicioPrograma
	 */
	public RecomendacionesServProg getDtoRecomenServicioPrograma() {
		return dtoRecomenServicioPrograma;
	}


	/**
	 * @param dtoRecomenServicioPrograma the dtoRecomenServicioPrograma to set
	 */
	public void setDtoRecomenServicioPrograma(RecomendacionesServProg dtoRecomenServicioPrograma) 
	{
		this.dtoRecomenServicioPrograma = dtoRecomenServicioPrograma;
	}


	/**
	 * @return the listaRecomenSerProg
	 */
	public List<RecomendacionesServProg> getListaRecomenSerProg() {
		return listaRecomenSerProg;
	}


	/**
	 * @param listaRecomenSerProg the listaRecomenSerProg to set
	 */
	public void setListaRecomenSerProg(	List<RecomendacionesServProg> listaRecomenSerProg) 
	{
		this.listaRecomenSerProg = listaRecomenSerProg;
	}



	public void setLisCodRecomendacion(String[] lisCodRecomendacion) {
		this.lisCodRecomendacion = lisCodRecomendacion;
	}


	public String[] getLisCodRecomendacion() {
		return lisCodRecomendacion;
	}










	public void setDtoSerProSerPro(RecomSerproSerpro dtoSerProSerPro) {
		this.dtoSerProSerPro = dtoSerProSerPro;
	}










	public RecomSerproSerpro getDtoSerProSerPro() {
		return dtoSerProSerPro;
	}










	public void setListaCodigos(ArrayList<Integer> listaCodigos) {
		this.listaCodigos = listaCodigos;
	}









	/**
	 * RETORNA LA LISTA DE CODIGOS DE LAS RECOMENDACIONES 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public ArrayList<Integer> getListaCodigos() 
	{
		return listaCodigos;
	}










	/**
	 * @return the postArrayServicioPrograma
	 */
	public int getPostArrayServicioPrograma() {
		return postArrayServicioPrograma;
	}










	/**
	 * @param postArrayServicioPrograma the postArrayServicioPrograma to set
	 */
	public void setPostArrayServicioPrograma(int postArrayServicioPrograma) {
		this.postArrayServicioPrograma = postArrayServicioPrograma;
	}










	/**
	 * @return the postArrayRecomendacion
	 */
	public int getPostArrayRecomendacion() {
		return postArrayRecomendacion;
	}










	/**
	 * @param postArrayRecomendacion the postArrayRecomendacion to set
	 */
	public void setPostArrayRecomendacion(int postArrayRecomendacion) {
		this.postArrayRecomendacion = postArrayRecomendacion;
	}








	
	

}
