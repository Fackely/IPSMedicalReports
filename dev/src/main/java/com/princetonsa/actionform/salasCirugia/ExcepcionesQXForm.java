/*
 * @(#)ExcepcionesQXForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.actionform.salasCirugia;

import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import com.princetonsa.mundo.salasCirugia.ExcepcionesQX;
import util.ConstantesBD;
import util.UtilidadCadena;

/**
 * Form que contiene todos los datos específicos para generar 
 * Excepciones QX Form 
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Oct 12 , 2005
 * @author wrios 
 */
public class ExcepcionesQXForm extends ValidatorForm
{
	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	/**
	 * Para manjar los logger de la clase ExcepcionesQXForm
	 */
	Logger logger = Logger.getLogger(ExcepcionesQXForm.class);
	
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	
	
	/*-----------------------------------------------
	 * 				ATRIBUTOS DEL PAGER
	 ------------------------------------------------*/
	/**
	 * String Patron Ordenar 
	 * **/
	private String patronOrdenar;
	
	/**
	 * String Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;
	
	/**
	 * String Link Siguiente
	 * */
	private String linkSiguiente;	
	
	/*-----------------------------------------------
	 * 				FIN ATRIBUTOS DEL PAGER
	 ------------------------------------------------*/
	
	/*-------------------------------------------------
	 * 	   ATRIBUTOS DE EXCEPCIONES QX
	 -------------------------------------------------*/
	/**
	 * HashMap de almacenamiento de datos. 
	 */
	private HashMap excepcionesQx;
	
	private HashMap excepcionesQxClone;
	
	private HashMap excepcionesQxEliminados;
	
	private HashMap vigencias;
	
	private HashMap vigenciasClone;
	
	private HashMap vigenciasEliminados;
	
	private HashMap criteriosBusqueda;
	
	/**
	 * Atributo de estado para el manejo del action
	 */
	private String estado;
	
	/**
	 * maneja el index dentro del mapa eliminados
	 */
	private String indexEliminado;
	
	/**
	 * 
	 */
	private String indexDestino;
	
	private String [] indices = ExcepcionesQX.indices;
	
	/*-------------------------------------------------
	 * 	  FIN ATRIBUTOS DE EXCEPCIONES QX
	 -------------------------------------------------*/
	
	/*-------------------------------------------------
	 * 	   ATRIBUTOS DE LOS PARAMETROS DE BUSQUEDA
	 --------------------------------------------------*/
	/**
	 * alamcena los convenios
	 */
	private ArrayList<HashMap<String, Object>>convenios;
	
	/**
	 *almacena los tipos de servicios 
	 */
	private ArrayList<HashMap<String , Object>> tiposServicio;
	/**
	 *almacena los tipos de cirugia 
	 */	
	private ArrayList<HashMap<String , Object>> tiposCirugia;
	
	/**
	 * almacena los asocios
	 */
	private ArrayList<HashMap<String , Object>> asocios;
	
    /**
	 * ArrayList con la informacion de tipos de sala
	 */
	private ArrayList<HashMap<String , Object>> tipoSala;
	
	/**
	 * almacen los tipo de liquidacion
	 */
	private ArrayList<HashMap<String, Object>> tiposLiquidacion;
	
	/**
	 * almacen los centros de costo
	 */
	private ArrayList<HashMap<String, Object>> centroCosto;
	
	/**
	 * Almacena los grupos de servicios 
	 */
	private ArrayList<HashMap<String, Object>> gruposServicio;
	
	/**
	 * Almacena las especialidades
	 */
	private ArrayList<HashMap<String, Object>> especialidades;

	/**
	 * Almacena los contratos
	 */
	private ArrayList<HashMap<String, Object>> contratos;
	
	/*----------------------------------------------------------
	 * ADICIONADO POR LA SOLICITUD DE LESLY EL DIA 18/10/2008
	 *----------------------------------------------------------/
	
	/**
     * Carga las vias de ingreso
     */
    private ArrayList<HashMap<String, Object>> viasIngresos;
    
    /**
     * Carga los Tipos Pacientes
     */
    private ArrayList<HashMap<String, Object>> tiposPaciente;
    
	
	/*-------------------------------------------------
	 * 	FIN  ATRIBUTOS DE LOS PARAMETROS DE BUSQUEDA
	 --------------------------------------------------*/
	/*-----------------------------------------------------
	 * 			METODOS GETTERS AND SETTERS
	 -----------------------------------------------------*/
	
	public ArrayList<HashMap<String, Object>> getAsocios() {
		return asocios;
	}

	public void setAsocios(ArrayList<HashMap<String, Object>> asocios) {
		this.asocios = asocios;
	}

	public ArrayList<HashMap<String, Object>> getCentroCosto() {
		return centroCosto;
	}

	public void setCentroCosto(ArrayList<HashMap<String, Object>> centroCosto) {
		this.centroCosto = centroCosto;
	}

	public ArrayList<HashMap<String, Object>> getConvenios() {
		return convenios;
	}

	public void setConvenios(ArrayList<HashMap<String, Object>> convenios) {
		this.convenios = convenios;
	}

	public HashMap getCriteriosBusqueda() {
		return criteriosBusqueda;
	}

	public void setCriteriosBusqueda(HashMap criteriosBusqueda) {
		this.criteriosBusqueda = criteriosBusqueda;
	}

	public Object getCriteriosBusqueda(String key) {
		return criteriosBusqueda.get(key);
	}

	public void setCriteriosBusqueda(String key, Object value) {
		this.criteriosBusqueda.put(key, value);
	}
	
	public ArrayList<HashMap<String, Object>> getEspecialidades() {
		return especialidades;
	}

	public void setEspecialidades(ArrayList<HashMap<String, Object>> especialidades) {
		this.especialidades = especialidades;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public HashMap getExcepcionesQx() {
		return excepcionesQx;
	}

	public void setExcepcionesQx(HashMap excepcionesQx) {
		this.excepcionesQx = excepcionesQx;
	}

	public Object getExcepcionesQx(String key) {
		return excepcionesQx.get(key);
	}

	public void setExcepcionesQx(String key,Object value) {
		this.excepcionesQx.put(key,value);
	}
	
	public HashMap getExcepcionesQxClone() {
		return excepcionesQxClone;
	}

	public void setExcepcionesQxClone(HashMap excepcionesQxClone) {
		this.excepcionesQxClone = excepcionesQxClone;
	}
	
	public Object getExcepcionesQxClone(String key) {
		return excepcionesQxClone.get(key);
	}

	public void setExcepcionesQxClone(String key,Object value) {
		this.excepcionesQxClone.put(key, value);
	}

	public HashMap getExcepcionesQxEliminados() {
		return excepcionesQxEliminados;
	}

	public void setExcepcionesQxEliminados(HashMap excepcionesQxEliminados) {
		this.excepcionesQxEliminados = excepcionesQxEliminados;
	}
	

	public Object getExcepcionesQxEliminados(String key) {
		return excepcionesQxEliminados.get(key);
	}

	public void setExcepcionesQxEliminados(String key,Object value) {
		this.excepcionesQxEliminados.put(key, value);
	}

	public ArrayList<HashMap<String, Object>> getGruposServicio() {
		return gruposServicio;
	}

	public void setGruposServicio(ArrayList<HashMap<String, Object>> gruposServicio) {
		this.gruposServicio = gruposServicio;
	}

	public String getIndexEliminado() {
		return indexEliminado;
	}

	public void setIndexEliminado(String indexEliminado) {
		this.indexEliminado = indexEliminado;
	}

	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public ArrayList<HashMap<String, Object>> getTipoSala() {
		return tipoSala;
	}

	public void setTipoSala(ArrayList<HashMap<String, Object>> tipoSala) {
		this.tipoSala = tipoSala;
	}

	public ArrayList<HashMap<String, Object>> getTiposCirugia() {
		return tiposCirugia;
	}

	public void setTiposCirugia(ArrayList<HashMap<String, Object>> tiposCirugia) {
		this.tiposCirugia = tiposCirugia;
	}

	public ArrayList<HashMap<String, Object>> getTiposLiquidacion() {
		return tiposLiquidacion;
	}

	public void setTiposLiquidacion(
			ArrayList<HashMap<String, Object>> tiposLiquidacion) {
		this.tiposLiquidacion = tiposLiquidacion;
	}

	public ArrayList<HashMap<String, Object>> getTiposServicio() {
		return tiposServicio;
	}

	public void setTiposServicio(ArrayList<HashMap<String, Object>> tiposServicio) {
		this.tiposServicio = tiposServicio;
	}

	public String getUltimoPatron() {
		return ultimoPatron;
	}

	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	public HashMap getVigencias() {
		return vigencias;
	}

	public void setVigencias(HashMap vigencias) {
		this.vigencias = vigencias;
	}

	public Object getVigencias(String key) {
		return vigencias.get(key);
	}

	public void setVigencias(String key,Object value) {
		this.vigencias.put(key, value);
	}
	
	public HashMap getVigenciasClone() {
		return vigenciasClone;
	}

	public void setVigenciasClone(HashMap vigenciasClone) {
		this.vigenciasClone = vigenciasClone;
	}

	public HashMap getVigenciasEliminados() {
		return vigenciasEliminados;
	}

	public void setVigenciasEliminados(HashMap vigenciasEliminados) {
		this.vigenciasEliminados = vigenciasEliminados;
	}
	
	public Object getVigenciasEliminados(String key) {
		return vigenciasEliminados.get(key);
	}

	public void setVigenciasEliminados(String key, Object value) {
		this.vigenciasEliminados.put(key, value);
	}
	
	public ArrayList<HashMap<String, Object>> getContratos() {
		return contratos;
	}

	public void setContratos(ArrayList<HashMap<String, Object>> contratos) {
		this.contratos = contratos;
	}
	
	public String getIndexDestino() {
		return indexDestino;
	}

	public void setIndexDestino(String indexDestino) {
		this.indexDestino = indexDestino;
	}

	
	/*-----------------------------------------------------
	 * 			FIN METODOS GETTERS AND SETTERS
	 -----------------------------------------------------*/
	
	/*-----------------------------------------------------
	 * 			METODOS
	 -----------------------------------------------------*/
	
	public void reserEliminados()
	{
		this.excepcionesQxEliminados = new HashMap ();
		this.setExcepcionesQxEliminados("numRegistros",0);
	}
	
	public void reset ()
	{
		this.excepcionesQx = new HashMap ();
		this.setExcepcionesQx("numRegistros", 0);
		this.excepcionesQxEliminados = new HashMap ();
		this.setExcepcionesQxEliminados("numRegistros",0);
		this.excepcionesQxClone = new HashMap ();
		this.setExcepcionesQxClone("numRegistros", 0);
	}
	
	
	public void resetBusqueda ()
	{
		this.criteriosBusqueda = new HashMap ();
		this.setCriteriosBusqueda("numRegistros", 0);
		this.setCriteriosBusqueda("seccionBusquedaAvanzada", "N");
	}
	public void resetPager ()
	{
		this.linkSiguiente = "";
		this.ultimoPatron = "";
		this.patronOrdenar = "";
	}
	
	public void resetSelects ()
	{
		this.asocios = new ArrayList<HashMap<String,Object>>();
		this.centroCosto = new ArrayList<HashMap<String,Object>>();
		this.viasIngresos = new ArrayList<HashMap<String,Object>>();
		this.tiposPaciente = new ArrayList<HashMap<String,Object>>();
		this.convenios = new ArrayList<HashMap<String,Object>>();
		this.especialidades = new ArrayList<HashMap<String,Object>>();
		this.gruposServicio = new ArrayList<HashMap<String,Object>>();
		this.tipoSala = new ArrayList<HashMap<String,Object>>();
		this.tiposCirugia = new ArrayList<HashMap<String,Object>>();
		this.tiposLiquidacion = new ArrayList<HashMap<String,Object>>();
		this.tiposServicio = new ArrayList<HashMap<String,Object>>();
		this.contratos = new ArrayList<HashMap<String,Object>>();
		
	}

	public void resetVigencias ()
	{
		this.vigencias = new HashMap ();
		this.setVigencias("numRegistros",0);
		this.vigenciasEliminados = new HashMap ();
		this.setVigenciasEliminados("numRegistros", 0);
		this.vigenciasClone = new HashMap ();
	}
	
	
	 public void resetBusquedaAvanzada ()
	 {
				
		//1)se agrega el centro de costo 		
		
			this.setCriteriosBusqueda(indices[7], ConstantesBD.codigoNuncaValido);
		//2)se agrega el tipo de servicio 		
		
			this.setCriteriosBusqueda(indices[9],ConstantesBD.codigoNuncaValido);
		//3)se agrega el tipo de sala 		
		
			this.setCriteriosBusqueda(indices[21], ConstantesBD.codigoNuncaValido);
		//4)se agrega el servicio	
		
			this.setCriteriosBusqueda(indices[13], ConstantesBD.codigoNuncaValido);
		//5)se agrega el tipo de asocio	
		
			this.setCriteriosBusqueda(indices[12], ConstantesBD.codigoNuncaValido);
		//6)se agrega el tipo de cirugia
		
			this.setCriteriosBusqueda(indices[10], ConstantesBD.codigoNuncaValido);
		//7)se agrega continua medico 	
		
			this.setCriteriosBusqueda(indices[17], "");
		//8)se agrega continua via de acceso	
		
			this.setCriteriosBusqueda(indices[18], "");
		//9)se agrega el tipo de liquidacion		
		
			this.setCriteriosBusqueda(indices[11], ConstantesBD.codigoNuncaValido);
		//10)se agrega valor 		
		
			this.setCriteriosBusqueda(indices[19], "");
		//11)se agrega el codigo de excepciones_qx	
		
		//12)se agrega la especialidad	
		this.setCriteriosBusqueda(indices[15], ConstantesBD.codigoNuncaValido);
		
		//13)se agrega el grupo de servicio
		this.setCriteriosBusqueda(indices[16], ConstantesBD.codigoNuncaValido);
		
		//14)se agrega el campo por via de ingreso
		this.setCriteriosBusqueda(indices[26], ConstantesBD.codigoNuncaValido);
		
		//15)se agrega el campo por tipo paciente
		this.setCriteriosBusqueda(indices[27], ConstantesBD.codigoNuncaValido);
		
	 }
	
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		errores = super.validate(mapping, request);
	
		if(this.estado.equals("buscardet"))
		{
			logger.info("\n ::::::::::ENTRO A BUSCARDET EN FORM :::::: ");
			if(UtilidadCadena.noEsVacio(this.getCriteriosBusqueda(indices[26])+"") || (this.getCriteriosBusqueda(indices[26])+"").equals(ConstantesBD.codigoNuncaValido+""))
			{
				//logger.info("====>Via de Ingreso: "+this.getCriteriosBusqueda(indices[26]));
				//logger.info("====>Tipo Paciente: "+this.getCriteriosBusqueda(indices[27]));
				if(!UtilidadCadena.noEsVacio(this.getCriteriosBusqueda(indices[27])+"") && !(this.getCriteriosBusqueda(indices[27])+"").equals(ConstantesBD.codigoNuncaValido+""))
					errores.add("descripcion",new ActionMessage("errors.required","El Tipo Paciente "));
			}
		}
		
		if (this.estado.equals("guardar"))
		{
			for (int i= 0; i < Integer.parseInt(this.getExcepcionesQx("numRegistros")+"");i++)
			{
				logger.info("\n ::::::::::ENTRO A GUARDAR EN FORM :::::: ");
				/*---------------------------------------------------------
				 * Se modifico el requerido según correo enviado 20/10/2008
				 --------------------------------------------------------*/
				//1) se valida que el tipo de cirugia no este vacio
				//if ((this.getExcepcionesQx(indices[10]+i)+"").equals("") || (this.getExcepcionesQx(indices[10]+i)+"").equals(ConstantesBD.codigoNuncaValido+""))
					//errores.add("descripcion",new ActionMessage("errors.required","La Selección del Tipo de Cirugía del registro "+(i+1)));
				/*---------------------------------------------------------
				 *
				 --------------------------------------------------------*/
				//2) se valida que el tipo de liquidacion no este vacio
				if ((this.getExcepcionesQx(indices[11]+i)+"").equals("") || (this.getExcepcionesQx(indices[11]+i)+"").equals(ConstantesBD.codigoNuncaValido+""))
					errores.add("descripcion",new ActionMessage("errors.required","La Selección del Tipo de Liquidación del registro "+(i+1)));
				//3) se valida que el asocio no venga vacio
				if ((this.getExcepcionesQx(indices[12]+i)+"").equals("") || (this.getExcepcionesQx(indices[12]+i)+"").equals(ConstantesBD.codigoNuncaValido+""))
					errores.add("descripcion",new ActionMessage("errors.required","La Selección del Asocio del registro "+(i+1)));
				//4) se valida que el valor no venga vacio
				if ((this.getExcepcionesQx(indices[19]+i)+"").equals("") || (this.getExcepcionesQx(indices[12]+i)+"").equals(ConstantesBD.codigoNuncaValido+""))
					errores.add("descripcion",new ActionMessage("errors.required","El Valor del registro "+(i+1)));
			
				//5)se valida el servicio
				if ((this.getExcepcionesQx(indices[13]+i)+"").equals(ConstantesBD.codigoNuncaValido+"") && 
					(this.getExcepcionesQx(indices[9]+i)+"").equals(ConstantesBD.codigoNuncaValido+"") &&
					(this.getExcepcionesQx(indices[15]+i)+"").equals(ConstantesBD.codigoNuncaValido+"") &&
					(this.getExcepcionesQx(indices[16]+i)+"").equals(ConstantesBD.codigoNuncaValido+"")
				   )
				errores.add("descripcion",new ActionMessage("errors.required","Ingresar el Tipo de Servicio, la Especialidad, el Grupo de Servicio o el Servicio del registro "+(i+1)));
			
				//se debe de validar el Unique que consta de los siguientes campos:
				//-- Tipo de servicio
				//-- especialidad
				//-- servicio
				//-- tipo cirugia
				//-- asocio
				//-- medicos
				//-- vias
				//-- centro de costo
				
				String unique1=this.getExcepcionesQx(indices[9]+i)+""+this.getExcepcionesQx(indices[15]+i)+this.getExcepcionesQx(indices[13]+i)+
							   this.getExcepcionesQx(indices[10]+i)+this.getExcepcionesQx(indices[12]+i)+this.getExcepcionesQx(indices[17]+i)+
							   this.getExcepcionesQx(indices[18]+i)+this.criteriosBusqueda.get(indices[7]);
				
				for (int k= 0; k!=i && k < Integer.parseInt(this.getExcepcionesQx("numRegistros")+"");k++)
				{
					String unique2= this.getExcepcionesQx(indices[9]+k)+""+this.getExcepcionesQx(indices[15]+k)+this.getExcepcionesQx(indices[13]+k)+
									this.getExcepcionesQx(indices[10]+k)+this.getExcepcionesQx(indices[12]+k)+this.getExcepcionesQx(indices[17]+k)+
									this.getExcepcionesQx(indices[18]+k)+this.criteriosBusqueda.get(indices[7]);
					
					/*logger.info("\n :::::::::::Las cadenas a comparar son :::::::::::: k->"+k+" i->"+i);
					logger.info("\n::: unique1 ==>"+unique1);
					logger.info("\n::: unique2 ==>"+unique2);
					logger.info("\n ::::::::::::::::::::::::::::::::::::::::::::::::::::");
					*/
					if (unique1.equals(unique2))
					{
						logger.info("\n :::::::::::entre a son iguales :::::::::::: k->"+k+" i->"+i);
						errores.add("descripcion",new ActionMessage("error.noRegistroMismaInformacion","Tipo Servicio, Especialidad, Servicio, Tipo Cirugía, Asocio, tipo de especialista, Vias  y Centro de Costo en el registro "+(i+1)));		
					}
				
				}
			}
	
	
		}
		
		
		return errores; 
	}

	/**
	 * @return the viasIngresos
	 */
	public ArrayList<HashMap<String, Object>> getViasIngresos() {
		return viasIngresos;
	}

	/**
	 * @param viasIngresos the viasIngresos to set
	 */
	public void setViasIngresos(ArrayList<HashMap<String, Object>> viasIngresos) {
		this.viasIngresos = viasIngresos;
	}

	/**
	 * @return the tiposPaciente
	 */
	public ArrayList<HashMap<String, Object>> getTiposPaciente() {
		return tiposPaciente;
	}

	/**
	 * @param tiposPaciente the tiposPaciente to set
	 */
	public void setTiposPaciente(ArrayList<HashMap<String, Object>> tiposPaciente) {
		this.tiposPaciente = tiposPaciente;
	}
	
	/*-----------------------------------------------------
	 * 			FIN METODOS
	 -----------------------------------------------------*/
}
