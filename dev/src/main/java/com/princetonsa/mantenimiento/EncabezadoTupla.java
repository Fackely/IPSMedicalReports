package com.princetonsa.mantenimiento;

/**
 * Esta clase encapsula los datos bï¿½icos de un atributo de tabla de
 * mantenimiento
 */
public class EncabezadoTupla {

	/**
	 * Representa el nombre de un campo de la tabla en la base de datos
	 */
	private String nombre;

	/**
	 * Representa el alias (nombre que verá el usuario)
	 * de un campo de la tabla en la base de datos
	 */
	private String alias;
	
	/**
	 * Representa el tipo de campo que es editable|no editable
	 */
	private boolean editable;
	
	/**
	 * Representa si el campo es requerido o no
	 */
	private boolean requerido;
	/**
		 * Representa si el campo es booleano o no
	 */
	private boolean booleano;

	/**
	 * Representa el tipo de datos de la tupla 
	 */
	private String tipo = null;

	/**
	 * Representa el nombre de la tabla de la cual depende el campo
	 */
	private String tablaDepende;
	
	/**
	 * Representa el nombre de la columna de la que depende el campo
	 */
	private String columnaDepende;

	/**
	 * Representa el nombre de la columna que se va a mostrar en la dependencia
	 */
	private String columnaDependeMostrar;

	/**
	 * Orden de los atributos a mostrar en el select
	 */
	private String orden;
	/**
	 * Crea un objeto de tipo EncabezadoTupla
	 * @see java.lang.Object#Object()
	 */
	public EncabezadoTupla(){
		this.nombre = new String();
		this.editable = false;
		this.booleano=false;
	}

	/**
	 * Crea un objeto de tipo EncabezadoTupla con el nombre especificado
	 * @param aNombre Nombre de la tupla
	 */
	public EncabezadoTupla(String aNombre){
		this.nombre = aNombre;
		this.editable = true;
		this.booleano=false;
	}

	/**
	 * Crea un objeto de tipo EncabezadoTupla
	 * @param aNombre Nombre de la tupla
	 * @param aEditable Si es editable o no
	 */
	public EncabezadoTupla(String aNombre, boolean aEditable){
		this.nombre = aNombre;
		this.editable = aEditable;
		this.booleano=false;
	}

	/**
	 * Permite obtener el nombre del atributo
	 * @return Un String con el nombre del atributo
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Asigna un nombre al atributo
	 * @param aNombre El nuevo nombre del atributo
	 */
	public void setNombre(String aNombre) {
		nombre = aNombre;
	}

	/**
	 * Permite establecer si este campo es editable o no
	 * @return Un boolean
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * Permite asignar un estado a un atributo de la tabla
	 * @param aEditable el tipo editable|no editable
	 */
	public void setEditable(boolean aEditable) {
		editable = aEditable;
	}

	/**
	 * Permite asignar si el atributo es requerido o no
	 * @param requerido 
	 */
	public void setRequerido(boolean requerido) {
		this.requerido = requerido;
	}

    /**
     * Permite establecer si el atributo es requerido o no
     * @return boolean
     */
	public boolean isRequerido() {
		return requerido;
	}

	/**
	 * Permite establecer el tipo de datos que maneja la tupla
	 * @param tipo Nombre de la clase que representa el tipo de datos
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * Permite obtener el tipo de datos que maneja la tupla
	 * @return String El nombre de la clase que representa el tipo de datos de
	 * la tupla
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * Permite asignar el nombre de la tabla de la cual depende el campo
	 * @param tablaDepende Nombre de la tabla de la que depende
	 */
	public void setTablaDepende(String tablaDepende) {
		this.tablaDepende = tablaDepende;
	}

	/**
	 * Permite obtener el nombre de la tabla de la cual depende el campo 
	 * @return String Nombre de la tabla
	 */
	public String getTablaDepende() {
		return tablaDepende;
	}

	/**
	 * Permite asignar el nombre de la columna de la cual depende el campo
	 * @param columnaDepende Nombre de la columna
	 */
	public void setColumnaDepende(String columnaDepende) {
		this.columnaDepende = columnaDepende;
	}

	/**
	 * Permite obtener el nombre de la columna de la cual depende el campo 
	 * @return String Nombre de la columna
	 */
	public String getColumnaDepende() {
		return columnaDepende;
	}

	/**
	 * Permite asignar el nombre de la columna a mostrar en la dependencia
	 * @param columnaDepedeMostrar Nombre de la columna a mostrar
	 */
	public void setColumnaDependeMostrar(String columnaDependeMostrar) {
		this.columnaDependeMostrar = columnaDependeMostrar;
	}

	/**
	 * Permite obtener el nombre de la columna que se va a mostrar en la
	 * dependencia
	 * @return String El nombre de la columna
	 */
	public String getColumnaDependeMostrar() {
		return columnaDependeMostrar;
	}

	

	/**
	 * @return
	 */
	public boolean isBooleano() {
		return booleano;
	}

	/**
	 * @param b
	 */
	public void setBooleano(boolean b) {
		booleano = b;
	}

	/**
	 * @return
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param string
	 */
	public void setAlias(String string) {
		alias = string;
	}

	/**
	 * @return Retorna orden.
	 */
	public String getOrden()
	{
		return orden;
	}
	/**
	 * @param orden Asigna orden.
	 */
	public void setOrden(String orden)
	{
		this.orden = orden;
	}
}
