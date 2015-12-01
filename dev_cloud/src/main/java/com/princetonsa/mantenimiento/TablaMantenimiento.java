package com.princetonsa.mantenimiento;

import java.util.Vector;

/**
 * Esta clase encapsula los datos básicos de un tabla de mantenimiento.
 */
public class TablaMantenimiento {

	/**
	 * indica que se pueden realizar operaciones de edicion sobre esta tabla
	 */
	private boolean editable;
	
	/**
	 * indica que se pueden realizar operaciones de eliminacion sobre esta tabla
	 */
	private boolean eliminable;
	/**
	 * Nombre de la tabla de mantenimiento
	 */
	private String nombre;
	
	/**
	 * Nombre para mostrar en la página más legible que el de la tabla en la BD
	 */
	private String nombreMostrar;
	
	/**
	 * Atributos de la tabla de mantenimiento 
	 * Este vector contiene objetos de tipo EncabezadoTupla
	 */
	private Vector atributos;

	
	//variable que almacena la maxima longitud en los caracteres de los cuadros de texto
	private int maxlongitud;
	
	
	/**
	 * Constructora por defecto de la clase
	 */
	public TablaMantenimiento() {
		this.nombre = new String();
		this.nombreMostrar = new String();
		this.atributos = new Vector();
		this.maxlongitud = 0;
	}
	
	/**
	 * Construtora con parámetros
	 * @param aNombre Nombre de la tabla
	 */
	public TablaMantenimiento(String aNombre){
		this.nombre = new String(aNombre);
		this.nombreMostrar = new String(aNombre);
		this.atributos = new Vector();
		this.maxlongitud = 0;
	}

	/**
	 * Constructora de la clase
	 * @param aNombre Nombre de la tabla 
	 * @param aNombreMostrar Nombre para visualizar en la interfaz
	 */
	public TablaMantenimiento(String aNombre, String aNombreMostrar){
		this.nombre = new String(aNombre);
		this.nombreMostrar = new String(aNombreMostrar);
		this.atributos = new Vector();
		this.maxlongitud = 0;
	}

	/**
	 * Permite obtener el nombre de la tabla de mantenimiento que este objeto
	 * representa
	 * @return Un String con el nombre de la tabla
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Permite modificar el nombre de esta tabla de mantenimiento 
	 * @param aNombre Nuevo nombre de la tabla
	 */
	public void setNombre(String aNombre) {
		this.nombre = aNombre;
	}

	/**
	 * Permite obtener los atributos de esta tabla
	 * @return un Vector con lo astributos de la clase  
	 */
	public Vector getAtributos() {
		return atributos;
	}

	/**
	 * Permite asignar los atributos de esta tabla
	 * @param aAtributos Vector que contiene los nuevos atributos de l a tabla
	 */
	public void setAtributos(Vector aAtributos) {
		this.atributos = aAtributos;
	}

	/**
	 * Permite adicionar un nuevo atributo a la tabla
	 * @param aAtributo  Atributo que se desea adicionar 
	 */
	public void addAtributo(EncabezadoTupla aAtributo) {
		if (!atributos.contains(aAtributo))
			atributos.add(aAtributo);
	}

	/**
	 * Permite eliminar un atributo de la tabla
	 * @param aAtributo  Elemento que se quiere eliminar
	 */
	public void removeAtributo(EncabezadoTupla aAtributo) {
		atributos.remove(aAtributo);
	}

	/**
	 * Permite modificar el nombre que se va a mostrar en la visualización
	 * @param nombreMostrar Nuevo nombre para "mostrar"
	 */
	public void setNombreMostrar(String nombreMostrar) {
		this.nombreMostrar = nombreMostrar;
	}

	/**
	 * Permite obtener el nombre de visualización de la tabla
	 * @return String Nombre para la visualización de la tabla 
	 */
	public String getNombreMostrar() {
		return nombreMostrar;
	}

	/**
	 * Returns the editable.
	 * @return boolean
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * Returns the eliminable.
	 * @return boolean
	 */
	public boolean isEliminable() {
		return eliminable;
	}

	/**
	 * Sets the editable.
	 * @param editable The editable to set
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	/**
	 * Sets the eliminable.
	 * @param eliminable The eliminable to set
	 */
	public void setEliminable(boolean eliminable) {
		this.eliminable = eliminable;
	}

	
	/** maxlongitud	 */
	public int getMaxlongitud() {	return maxlongitud;	}
	public void setMaxlongitud(int maxlongitud) {	this.maxlongitud = maxlongitud;	}

}
