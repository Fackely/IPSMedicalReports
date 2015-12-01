/*
 * Sep 05/2005
 */
package com.princetonsa.actionform.parametrizacion;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadTexto;

/**
 * @author Sebastián Gómez
 *
 *Clase que almacena y carga la información utilizada para la funcionalidad
 *Parametrización de Salas
 */
public class SalasForm extends ValidatorForm {
	
	/**
	 * Variable usada para almacenar el estado del controlador
	 */
	private String estado;
	
	/**
	 * Objeto donde se almacena el listado de Salas
	 */
	private HashMap salas=new HashMap();
	
	/**
	 * Variable usada para almacenar el número de registros
	 * del mapa Salas
	 */
	private int numRegistros;
	
	/**
	 * Variable usada para almacenar el consecutivo del registro
	 * que se va a eliminar
	 */
	private int codigoRegistro;
	
	/**
	 * Cadena que almacena información de los registros utilizados en el
	 * sistema que no pueden ser modificados o eliminados
	 */
	private String registrosUsados;
	
	/**
	 * Variable usada para almacenar la posicion del mapa donde
	 * se encuentra ubicado el registro
	 */
	private int pos;
	
	/**
	 * Código de la institucion 
	 */
	private String institucion;
	
	//*********************************** Para la disponibilidad de salas ***************************************//
	/**
	 * Objeto donde se almacena el listado de la disponiblidad para la sala
	 */
	private HashMap disponibilidadSalas=new HashMap();
	
	/**
	 * Variable usada para almacenar el número de registros
	 * del mapa disponibilidadSalas
	 */
	private int numRegDisponibilidad;
	
	/**
	 * Código de la sala a la cuál se le va a asignar un rango de disponibilidad
	 */
	//private int codigoSala;
	private String codigoSala;
	
	/**
	 *  Descripción de la sala cuando se va asignar un horario de disponibilidad
	 */
	private String descripcionSala;
	
	/**
	 * Descripción del tipo de sala cuando se va a asignar un horario de disponibilidad
	 */
	private String descripcionTipoSala;
	
	/**
	 * Campo pàra saber el estado de la eliminacion de un rango de una disponibilidad de una sala
	 */
	private int estadoEliminacion;
	
	/**
	 * variable que almacena el centro de atencion.s
	 */
	private int centroAtencion;
	
	/**
	 * Mensaje de exito o fracaso en de la operacion
	 */
	private ResultadoBoolean mensaje = new ResultadoBoolean(false);
	
	/**
	 * Variable utilizada para mostrar el mensaje cuando se hace una eliminación
	 */
	private boolean mostrarMensajeEliminar = false;
	
	/**
	 * Variable utilizada para quitar el mensaje de proceso realizado con éxito
	 */
	private boolean empezar = true;
	
	/**
	 * Array en el que se almacenan todos los medicos de la institucion
	 */
	private ArrayList<HashMap<String, Object>> medicos;
	
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
			
			
			//*****VALIDAR CÓDIGOS********************************
			errores=this.validarCodigos(errores);
			
			//****VALIDAR DESCRIPCIONES**********
			errores=this.validarDescripciones(errores);
			
			//***VALIDAR TIPOS DE SALA**********
			errores=this.validarTipoSala(errores);
			
			if(!errores.isEmpty())
				this.estado="empezar";
		}
		return errores;
	}
	
	/**
	 * Método usado para validar los tipos de sala
	 * @param errores
	 * @return
	 */
	private ActionErrors validarTipoSala(ActionErrors errores) {
		
		int aux=0;
		//sección para validar que no hayan tipos de salas sin seleccionar
		for(int i=0;i<this.numRegistros;i++)
		{
			if(this.salas.get("tipo_"+i).toString().indexOf(ConstantesBD.separadorSplit)>ConstantesBD.codigoNuncaValido)
				aux=Integer.parseInt(this.salas.get("tipo_"+i).toString().split(ConstantesBD.separadorSplit)[0]);
			else
				aux=Integer.parseInt(this.salas.get("tipo_"+i)+"");
			if(aux<=0)
			{
				errores.add("faltan tipos de salas por definir", new ActionMessage("error.salasCirugia.sinDefinirGeneral","tipos de salas"));
				i=this.numRegistros;
			}
		}
		return errores;
	}

	/**
	 * Métdo usado para validar las descripciones del listado de Salas
	 * @param errores
	 * @return
	 */
	private ActionErrors validarDescripciones(ActionErrors errores) {
		
		String aux="";
		String aux2="";
		boolean bandera=false;
		//sección para validar que no hayan descripciones vacías
		for(int i=0;i<this.numRegistros;i++)
		{
			aux=this.salas.get("descripcion_"+i)+"";
			if(aux.equals(""))
			{
				errores.add("faltan descripciones por definir", new ActionMessage("error.salasCirugia.sinDefinirGeneral","descripciones"));
				i=this.numRegistros;
			}
		}
		// sección para validar que no hayan descripciones repetidas
		for(int i=0;i<this.numRegistros;i++)
		{
			//solo se revisarán registros activos
			if(!bandera&&UtilidadTexto.getBoolean(this.salas.get("activo_"+i)+""))
			{
				aux=this.salas.get("descripcion_"+i)+"";
				for(int j=this.numRegistros-1;j>i;j--)
				{
					//solo se revisarán registros activos
					if(!bandera&&UtilidadTexto.getBoolean(this.salas.get("activo_"+j)+""))
					{
						aux2=this.salas.get("descripcion_"+j)+"";
						//se formatean los valores (se les quita acentuación)
						aux=UtilidadTexto.removeAccents(aux);
						aux2=UtilidadTexto.removeAccents(aux2);
						//se comparan las descripciones
						if(aux.compareToIgnoreCase(aux2)==0&&!aux.equals("")&&!aux2.equals(""))
						{
							errores.add("descripciones iguales", new ActionMessage("error.salasCirugia.igualesGeneral","descripciones","de las salas ACTIVAS"));
							bandera=true;
						}
					}
				}
			}
		}
		return errores;
	}

	/**
	 * Método usado para validar los códigos de las Salas
	 * @param errores
	 * @return
	 */
	private ActionErrors validarCodigos(ActionErrors errores) {
		
		String aux="";
		String aux2="";
		boolean bandera=false;
		//sección para validar que no hayan CÓDIGOS vacíos
		for(int i=0;i<this.numRegistros;i++)
		{
			aux=this.salas.get("codigo_"+i)+"";
			if(aux.equals(""))
			{
				errores.add("faltan códigos por definir", new ActionMessage("error.salasCirugia.sinDefinirGeneral","códigos de salas"));
				i=this.numRegistros;
			}
		}
		//sección para validar que no hayan CÓDIGOS con caracteres especiales
		for(int i=0;i<this.numRegistros;i++)
		{
			try
			{
				Double.parseDouble(this.salas.get("codigo_"+i)+"");
				for(int j=0;j<i;j++)
				{
					if((this.salas.get("codigo_"+i)+"").equalsIgnoreCase(this.salas.get("codigo_"+j)+""))
					{
						errores.add("", new ActionMessage("errors.yaExiste","El Código "+this.salas.get("codigo_"+i)));
					}
				}
			}
			catch (Exception e) 
			{
				errores.add("", new ActionMessage("errors.integer","El Código "+this.salas.get("codigo_"+i)));
			}
		}
		//sección para validar que no hayan CÓDIGOS iguales
		for(int i=0;i<this.numRegistros;i++)
		{
			if(!bandera)
			{
				aux=this.salas.get("codigo_"+i)+"";
				for(int j=this.numRegistros-1;j>i;j--)
				{
					if(!bandera)
					{
						aux2=this.salas.get("codigo_"+j)+"";
						//se comparan los códigos
						if(aux.compareToIgnoreCase(aux2)==0&&!aux.equals("")&&!aux2.equals(""))
						{
							errores.add("codigos iguales", new ActionMessage("error.salasCirugia.igualesGeneral","códigos","en el listado de salas"));
							bandera=true;
						}
					}
				}
			}
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
		this.salas=new HashMap();
		this.numRegistros=0;
		this.codigoRegistro=0;
		this.registrosUsados="";
		this.pos=-1;
		this.institucion="-1";
		this.disponibilidadSalas=new HashMap();
		this.descripcionSala = "";
		this.descripcionTipoSala = "";
		this.centroAtencion = 0;
		this.medicos = new ArrayList<HashMap<String,Object>>();
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
	 * @return Returns the salas.
	 */
	public HashMap getSalas() {
		return salas;
	}
	/**
	 * @param salas The salas to set.
	 */
	public void setSalas(HashMap salas) {
		this.salas = salas;
	}
	
	/**
	 * @return Retorna un elemento del arreglo Salas
	 */
	public Object getSalas(String key) {
		return salas.get(key);
	}
	/**
	 * @param asigna un elemento al arreglo Salas
	 */
	public void setSalas(String key,Object obj) {
		this.salas.put(key,obj);
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
	 * @return Returns the codigoRegistro.
	 */
	public int getCodigoRegistro() {
		return codigoRegistro;
	}
	/**
	 * @param codigoRegistro The codigoRegistro to set.
	 */
	public void setCodigoRegistro(int codigoRegistro) {
		this.codigoRegistro = codigoRegistro;
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
	/**
	 * @return Returns the institucion.
	 */
	public String getInstitucion() {
		return institucion;
	}
	/**
	 * @param institucion The institucion to set.
	 */
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}
	/**
	 * @return Returns the disponibilidadSalas.
	 */
	public HashMap getDisponibilidadSalas()
	{
		return disponibilidadSalas;
	}
	/**
	 * @param disponibilidadSalas The disponibilidadSalas to set.
	 */
	public void setDisponibilidadSalas(HashMap disponibilidadSalas)
	{
		this.disponibilidadSalas = disponibilidadSalas;
	}
	
	/**
	 * @return Retorna un elemento del hashmap disponiblidadSalas
	 */
	public Object getDisponibilidadSalas(String key) {
		return disponibilidadSalas.get(key);
	}
	/**
	 * @param asigna un elemento al hashmap disponibilidadSalas
	 */
	public void setDisponibilidadSalas(String key,Object obj) {
		this.disponibilidadSalas.put(key,obj);
	}
	
	/**
	 * @return Returns the numRegDisponibilidad.
	 */
	public int getNumRegDisponibilidad()
	{
		return numRegDisponibilidad;
	}
	/**
	 * @param numRegDisponibilidad The numRegDisponibilidad to set.
	 */
	public void setNumRegDisponibilidad(int numRegDisponibilidad)
	{
		this.numRegDisponibilidad = numRegDisponibilidad;
	}
	/**
	 * @return Returns the codigoSala.
	 */
	public String getCodigoSala()
	{
		return codigoSala;
	}
	/**
	 * @param codigoSala The codigoSala to set.
	 */
	public void setCodigoSala(String codigoSala)
	{
		this.codigoSala = codigoSala;
	}
	/**
	 * @return Returns the descripcionSala.
	 */
	public String getDescripcionSala()
	{
		return descripcionSala;
	}
	/**
	 * @param descripcionSala The descripcionSala to set.
	 */
	public void setDescripcionSala(String descripcionSala)
	{
		this.descripcionSala = descripcionSala;
	}
	/**
	 * @return Returns the descripcionTipoSala.
	 */
	public String getDescripcionTipoSala()
	{
		return descripcionTipoSala;
	}
	/**
	 * @param descripcionTipoSala The descripcionTipoSala to set.
	 */
	public void setDescripcionTipoSala(String descripcionTipoSala)
	{
		this.descripcionTipoSala = descripcionTipoSala;
	}
	/**
	 * @return Retorna estadoEliminacion.
	 */
	public int getEstadoEliminacion() {
		return estadoEliminacion;
	}
	/**
	 * @param Asigna estadoEliminacion.
	 */
	public void setEstadoEliminacion(int estadoEliminacion) {
		this.estadoEliminacion = estadoEliminacion;
	}

	public int getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}
	
	/**
	 * Método getMensaje
	 * @return  ResultadoBoolean mensaje
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}
	
	/**
	 * Metodo setMensaje
	 * Utilizado para mostrar el mensaje de "PROCESO REALIZADO CON ÉXITO !!!"
	 * @param ResultadoBoolean mensaje
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * Método setMostrarMensajeEliminar
	 * @param mostrarMensajeEliminar
	 */
	public void setMostrarMensajeEliminar(boolean mostrarMensajeEliminar) {
		this.mostrarMensajeEliminar = mostrarMensajeEliminar;
	}

	/**
	 * Método getMostrarMensajeEliminar
	 * @return boolean mostrarMensajeEliminar
	 */
	public boolean getMostrarMensajeEliminar() {
		return mostrarMensajeEliminar;
	}

	public void setEmpezar(boolean empezar) {
		this.empezar = empezar;
	}

	public boolean getEmpezar() {
		return empezar;
	}

	/**
	 * @return the medicos
	 */
	public ArrayList<HashMap<String, Object>> getMedicos() {
		return medicos;
	}

	/**
	 * @param medicos the medicos to set
	 */
	public void setMedicos(ArrayList<HashMap<String, Object>> medicos) {
		this.medicos = medicos;
	}
	
}
