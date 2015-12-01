/*
 * Created on Sep 1, 2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.actionform.parametrizacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadTexto;

/**
 * @author Sebastián Gómez
 *
 *Clase que almacena y carga la información utilizada para la funcionalidad
 *Parametrización de Tipos Salas
 */
public class TipoSalasForm extends ValidatorForm {
	
	/**
	 * Variable usada para almacenar el estado del controlador
	 */
	private String estado;
	
	/**
	 * Objeto usado para almacenar los tipos de salas
	 */
	private HashMap tipoSalas=new HashMap();
	
	/**
	 * número de registros del Mapa tipoSalas
	 */
	private int numRegistros;
	
	/**
	 * variable para almacenar el código del registro que
	 * se va a borrar
	 */
	private String codigoRegistro;
	
	/**
	 * Cadena que almacena los códigos de los registros que no pudieron
	 * ser eliminados o modificados porque están siendo usados en el sistema
	 */
	private String registrosUsados;
	
	/**
	 * Variable usada para almacenar la posicion del mapa donde
	 * se encuentra ubicado el registro
	 */
	private int pos;
	
	
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
		if(estado.equals("guardar"))
		{
			String aux="";
			String aux2="";
			boolean bandera=false;
			
			//****VALIDAR DESCRIPCIONES**********
			//sección para validar que no hayan descripciones vacías
			for(int i=0;i<this.numRegistros;i++)
			{
				aux=this.tipoSalas.get("nombre_"+i)+"";
				if(aux.equals(""))
				{
					errores.add("faltan descripciones por definir", new ActionMessage("error.salasCirugia.sinDefinirGeneral","descripciones de tipo sala"));
					i=this.numRegistros;
				}
			}
			// sección para validar que no hayan descripciones repetidas
			for(int i=0;i<this.numRegistros;i++)
			{
				if(!bandera)
				{
					aux=this.tipoSalas.get("nombre_"+i)+"";
					for(int j=this.numRegistros-1;j>i;j--)
					{
						if(!bandera)
						{
							aux2=this.tipoSalas.get("nombre_"+j)+"";
							//se formatean los valores (se les quita acentuación)
							aux=UtilidadTexto.removeAccents(aux);
							aux2=UtilidadTexto.removeAccents(aux2);
							//se co
							if(aux.compareToIgnoreCase(aux2)==0&&!aux.equals("")&&!aux2.equals(""))
							{
								errores.add("tipos de salas iguales", new ActionMessage("error.salasCirugia.igualesGeneral","descripciones","del listado"));
								bandera=true;
							}
						}
					}
				}
			}
			
			if(!errores.isEmpty())
				this.estado="empezar";
		}
		return errores;
	}
	
	/**
	 * reset de los datos de la forma
	 *
	 */
	public void reset()
	{
	    this.estado="";
	    this.tipoSalas= new HashMap();
	    this.numRegistros=0;
	    this.codigoRegistro="";
	    this.registrosUsados="";
	    this.pos=-1;
	 
	}
	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * @return Returns the codigoRegistro.
	 */
	public String getCodigoRegistro() {
		return codigoRegistro;
	}
	/**
	 * @param codigoRegistro The codigoRegistro to set.
	 */
	public void setCodigoRegistro(String codigoRegistro) {
		this.codigoRegistro = codigoRegistro;
	}
	/**
	 * @return Returns the numRegistros.
	 */
	public int getNumRegistros() {
		return numRegistros;
	}
	/**
	 * @param numRegistros The numRegistros to set.
	 */
	public void setNumRegistros(int numRegistros) {
		this.numRegistros = numRegistros;
	}
	/**
	 * @return Returns the tipoSalas.
	 */
	public HashMap getTipoSalas() {
		return tipoSalas;
	}
	/**
	 * @param tipoSalas The tipoSalas to set.
	 */
	public void setTipoSalas(HashMap tipoSalas) {
		this.tipoSalas = tipoSalas;
	}
	/**
	 * @return Retorna un elemento del mapa.
	 */
	public Object getTipoSalas(String key) {
		return tipoSalas.get(key);
	}
	/**
	 * @param asigna un elemento al mapa
	 */
	public void setTipoSalas(String key, Object obj) {
		this.tipoSalas.put(key,obj);
	}
	/**
	 * @return Returns the registrosUsados.
	 */
	public String getRegistrosUsados() {
		return registrosUsados;
	}
	/**
	 * @param registrosUsados The registrosUsados to set.
	 */
	public void setRegistrosUsados(String registrosUsados) {
		this.registrosUsados = registrosUsados;
	}
	/**
	 * @return Returns the pos.
	 */
	public int getPos() {
		return pos;
	}
	/**
	 * @param pos The pos to set.
	 */
	public void setPos(int pos) {
		this.pos = pos;
	}
}
