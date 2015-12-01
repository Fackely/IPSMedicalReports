package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dao.UtilidadesDao;
import com.princetonsa.dto.odontologia.DtoMotivosAtencion;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.Utilidades;


public class MotivosAtencionOdontologicaForm extends ValidatorForm{
	
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(MotivosAtencionOdontologicaForm.class);
	
	/**
	 * Variable para manejo de Estado
	 */
	private String estado;
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 * Atributos para el ingreso de un nuevo registro de Motivo Atención Odontológica
	 */
	private String codigoNuevo;
	private String nombreNuevo;
	private int tipoNuevo;
	
	private HashMap tiposMotivo;
	
	private ArrayList<DtoMotivosAtencion> listaMotivosAtencion= new ArrayList<DtoMotivosAtencion>();
	
	private int indiceModificar;
	private int codigoPk;
	
	/***
	 * Variables para el ordenamiento del ArrayList
	 */
	private String patronOrdenar;
	private String esDescendente;
	
	/**
	 * Método encargado de borrar el ResultadoBoolean de mensaje
	 */
	public void resetMensaje()
	{
		this.mensaje = new ResultadoBoolean(false);
	}
	
	/**
	 * Resetea los valores de la forma cuando
	 * se ingresa por la funcionalidad por Area
	 * @param codigoInstitucion
	 */
	public void reset(int codigoInstitucion)
	{
		this.estado="";
		this.codigoNuevo="";
		this.nombreNuevo="";
		this.tipoNuevo=ConstantesBD.codigoNuncaValido;
		this.tiposMotivo= new HashMap();
		this.tiposMotivo.put("numRegistros", "0");
		this.listaMotivosAtencion= new ArrayList<DtoMotivosAtencion>();
		this.indiceModificar=0;
		this.codigoPk=0;
		this.patronOrdenar = "";
	}

	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public String getEsDescendente() {
		return esDescendente;
	}

	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}

	public int getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public int getIndiceModificar() {
		return indiceModificar;
	}

	public void setIndiceModificar(int indiceModificar) {
		this.indiceModificar = indiceModificar;
	}

	public ArrayList<DtoMotivosAtencion> getListaMotivosAtencion() {
		return listaMotivosAtencion;
	}

	public void setListaMotivosAtencion(
			ArrayList<DtoMotivosAtencion> listaMotivosAtencion) {
		this.listaMotivosAtencion = listaMotivosAtencion;
	}

	public HashMap getTiposMotivo() {
		return tiposMotivo;
	}

	public void setTiposMotivo(HashMap tiposMotivo) {
		this.tiposMotivo = tiposMotivo;
	}

	public String getCodigoNuevo() {
		return codigoNuevo;
	}

	public void setCodigoNuevo(String codigoNuevo) {
		this.codigoNuevo = codigoNuevo;
	}

	public String getNombreNuevo() {
		return nombreNuevo;
	}

	public void setNombreNuevo(String nombreNuevo) {
		this.nombreNuevo = nombreNuevo;
	}

	public int getTipoNuevo() {
		return tipoNuevo;
	}

	public void setTipoNuevo(int tipoNuevo) {
		this.tipoNuevo = tipoNuevo;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}
	
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
		ActionErrors errores= new ActionErrors();
        errores=super.validate(mapping,request);
        logger.info(">>> Codigo = "+this.codigoNuevo);
        
        if(this.estado.equals("guardarNuevoRegistro"))
        {
        	Iterator<DtoMotivosAtencion> iterador=listaMotivosAtencion.iterator();
        	
        	while(iterador.hasNext())
        	{
        		DtoMotivosAtencion dto=iterador.next();
        		if (dto.getCodigo().equals(this.codigoNuevo))
        		{
        			logger.info(">>> El código ya está registrado en la BD");
        			errores.add("descripcion",new ActionMessage("errors.notEspecific","El Código ya se encuentra registado, por favor verifique "));
        		}
        		if(dto.getNombre().equals(this.nombreNuevo))
        		{
        			logger.info(">>> El nombre ya está registrado en la BD");
        			errores.add("descripcion",new ActionMessage("errors.notEspecific","El Nombre ya se encuentra registado, por favor verifique "));
        		}
        	}
        }
        
        if(this.estado.equals("guardarNuevoRegistro") || this.estado.equals("guardarModificarRegistro"))
        {
        	if(this.codigoNuevo.equals(""))
        		errores.add("descripcion",new ActionMessage("errors.required","El Código Motivo Odontológico "));
        	if(this.nombreNuevo.equals(""))
        		errores.add("descripcion",new ActionMessage("errors.required","El Nombre Motivo Odontológico "));
        	if(this.tipoNuevo == ConstantesBD.codigoNuncaValido)
        		errores.add("descripcion",new ActionMessage("errors.required","El Tipo Motivo Odontológico "));      
        }
        
        if(!errores.isEmpty())
        {	
        	logger.info("\n\nestado: "+estado);
        	if(this.estado.equals("guardarNuevoRegistro"))
        		estado= "nuevoRegistro";
        	else if(this.estado.equals("guardarModificarRegistro"))
        		estado= "modificarRegistro";
        }
        	
        return errores;
    }
}