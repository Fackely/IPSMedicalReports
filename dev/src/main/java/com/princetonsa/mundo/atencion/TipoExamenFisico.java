/*
 * @(#)ExamenFisicoOpcion.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo.atencion;

import java.util.ArrayList;

import util.Encoder;
import util.InfoDatos;

/**
 * Esta clase encapsula los atributos de un tipo de ex�men f�sico, seleccionable
 * de entre una lista de opciones.
 *
 * @version 1.0, May 15, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public class TipoExamenFisico 
{

	/**
	 * C�digo del tipo de ex�men f�sico.
	 */
	private int codigo;

	/**
	 * Descripci�n del an�lisis de ex�men f�sico.
	 */
	private String descripcion="";
	
	/**
	 * Colecci�n con los ex�menes f�sicos que pueden venir como una opci�n seleccionable de una lista.
	 */
	private ArrayList examenesFisicosOpciones;

	/**
	 * Si el atributo 'normal' es' <b>f</b>', indica el texto que le corresponde ( por ej.,
	 * "anormal" , o "inadecuado" ).
	 */
	private String labelFalso;

	/**
	 * Si el atributo 'normal' es '<b>t</b>', indica el texto que le corresponde ( por ej.,
	 * "normal" , o "adecuado" ).
	 */
	private String labelVerdadero;

	/**
	 * Nombre del tipo de ex�men f�sico.
	 */
	private String nombre;

	/**
	 * Indica si los resultados de un examen pueden considerarse como normales/anormales
	 * (o adecuado/inadecuado, seg�n el caso). Posibles valores son 't', 'f', o ' ' para
	 * indefinido.
	 */
	private char normal;

	/**
	 * Indica el c�digo de la especialidad al que pertenece este tipo de examen f�sico
	 */
	private int codigoEspecialidad=0;

	/**
	 * Crea un nuevo objeto <code>TipoExamenFisico</code>.
	 */
	public TipoExamenFisico() 
	{
		codigoEspecialidad=0;
		this.codigo = -1;
		this.examenesFisicosOpciones = new ArrayList();
		this.labelFalso = "";
		this.labelVerdadero = "";
		this.nombre = "";
		this.normal = '\0';
	}

	/**
	 * Retorna el c�digo del tipo de ex�men.
	 * @return el c�digo del tipo de ex�men
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * Retorna una lista con los ex�menes f�sicos.
	 * @return una lista con los ex�menes f�sicos
	 */
	public ArrayList getExamenesFisicosOpciones() {
		return examenesFisicosOpciones;
	}
	public String getExamenesFisicosOpcionesStr(){
		String cadenaOpciones = "";
		for (int i=0; i<examenesFisicosOpciones.size(); i++){
			cadenaOpciones+=((InfoDatos)examenesFisicosOpciones.get(i)).getValue()+",";
		}
		cadenaOpciones = (cadenaOpciones.length()>0)? cadenaOpciones.substring(0, cadenaOpciones.length()-1):"";
		return cadenaOpciones;
	}
	/**
	 * Retorna el texto que corresponda si 'normal' es '<b>f</b>'.
	 * @return el texto que corresponda si 'normal' es '<b>f</b>'
	 */
	public String getLabelFalso() {
		return labelFalso;
	}

	/**
	 * Retorna el texto que corresponda si 'normal' es '<b>t</b>'.
	 * @return el texto que corresponda si 'normal' es '<b>t</b>'
	 */
	public String getLabelVerdadero() {
		return labelVerdadero;
	}

	/**
	 * Retorna el nombre del tipo de ex�men.
	 * @return el nombre del tipo de ex�men
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Retorna el valor de 'normal'.
	 * @return '<b>t</b>' si el examen f�sico se puede considerar normal o adecuado,
	 * '<b>f</b>' si se debe considerar como anormal o inadecuado, o ' ' si no
	 * se sabe.
	 */
	public char getNormal() {
		return normal;
	}

	/**
	 * Establece el c�digo del tipo de ex�men.
	 * @param codigo el c�digo del tipo de ex�men a establecer
	 */
	public void setCodigo(int codigoTipoExamen) {
		this.codigo = codigoTipoExamen;
	}

	/**
	 * Establece el valor de la lista de ex�menes f�sicos que sean seleccionables de entre una lista de opciones.
	 * @param examenesFisicosOpciones la lista de ex�menes f�sicos a establecer.
	 */
	public void setExamenesFisicosOpciones(ArrayList examenesFisicosOpciones) {
		this.examenesFisicosOpciones = examenesFisicosOpciones;
	}

	/**
	 * Establece el texto a mostrar si 'normal' es '<b>f</b>'.
	 * @param labelFalso el texto a mostrar si 'normal' es '<b>f</b>'
	 */
	public void setLabelFalso(String labelFalso) {
		this.labelFalso = labelFalso;
	}

	/**
	 * Establece el texto a mostrar si 'normal' es '<b>t</b>'.
	 * @param labelVerdadero el texto a mostrar si 'normal' es '<b>t</b>'
	 */
	public void setLabelVerdadero(String labelVerdadero) {
		this.labelVerdadero = labelVerdadero;
	}

	/**
	 * Establece el nombre del tipo de ex�men.
	 * @param nombre el nombre del tipo de ex�men a establecer
	 */
	public void setNombre(String nombreTipoExamen) {
		this.nombre = nombreTipoExamen;
	}

	/**
	 * Establece el valor de 'normal'.
	 * @param normal '<b>t</b>' si el examen f�sico se puede considerar normal o adecuado,
	 * '<b>f</b>' si se debe considerar como anormal o inadecuado, o ' ' si no se proporcion�
	 * este dato.
	 */
	public void setNormal(char normal) {
		this.normal = normal;
	}

	/**
	 * @return
	 */
	public String getDescripcion() {
		return descripcion;
	}

	public String getDescripcion(boolean encoded){

String hola = Encoder.encode(this.descripcion);

		if (encoded) return hola.replaceAll("\n", "<br>");
		return this.descripcion;
				
	}
	/**
	 * @param string
	 */
	public void setDescripcion(String string) {
		descripcion = string;
	}

	public void addExamenFisicoOpcion (InfoDatos examenFisicoOpcion)
	{
		examenesFisicosOpciones.add(examenFisicoOpcion);
	}
	
	public ExamenFisico getExamenFisico (int numero)
	{
		return (ExamenFisico) examenesFisicosOpciones.get(numero);
	}

	/**
	 * @return
	 */
	public int getCodigoEspecialidad() {
		return codigoEspecialidad;
	}

	/**
	 * @param i
	 */
	public void setCodigoEspecialidad(int i) {
		codigoEspecialidad = i;
	}

}