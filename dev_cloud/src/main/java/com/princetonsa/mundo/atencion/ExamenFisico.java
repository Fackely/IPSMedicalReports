/*
 * @(#)ExamenFisico.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo.atencion;

/**
 * Esta clase encapsula los atributos de un exámen físico.
 *
 * @version 1.0, May 15, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public class ExamenFisico {

	/**
	 * Indica el código de este examen físico
	 */
	private int codigo;

	/**
	 * Indica si los resultados de un examen pueden considerarse como normales/anormales
	 * (o adecuado/inadecuado, según el caso). Posibles valores son 't', 'f', o ' ' para
	 * indefinido.
	 */
	private char normal;

	/**
	 * Unidad de medida en la que se toma este exámen en particular.
	 */
	private String unidadMedida;

	/**
	 * Si el atributo 'normal' es <b>true</b>, indica el texto que le corresponde ( por ej.,
	 * "normal" , o "adecuado" ).
	 */
	private String valorVerdadero;

	/**
	 * Si el atributo 'normal' es <b>false</b>, indica el texto que le corresponde ( por ej.,
	 * "anormal" , o "inadecuado" ).
	 */
	private String valorFalso;

	/**
	 * Nombre del Examen Físico
	 */
	private String nombre;

	/**
	 * Valor del Examen Físico
	 */
	private double valor;
	
	/**
	 * Descripción del Examen Físico
	 */
	private String descripcion;
	
	/**
	 * Código de la especialidad a la que pertenece este examen físico 
	 */
	private int codigoEspecialidad=0;
	
	/**
	 * Crea un nuevo objeto <code>ExamenFisico</code>.
	 */
	public ExamenFisico() 
	{
		this.codigoEspecialidad=0;
		this.codigo=0;
		this.normal = ' ';
		this.unidadMedida = "";
		this.valorVerdadero = "";
		this.valorFalso = "";
		this.nombre="";
		this.valor=0.0;
		this.descripcion="";
	}

	/**
	 * Retorna el valor de 'normal'.
	 * @return <b>'t'</b> si el examen físico se puede considerar normal o adecuado,
	 * <b>'f'</b> si se debe considerar como anormal o inadecuado,
	 * <b>' '</b> si se debe considerar como anormal o inadecuado,
	 */
	public char isNormal() {
		return normal;
	}

	/**
	 * Retorna la unidad de medida de este exámen físico.
	 * @return la unidad de medida de este exámen físico
	 */
	public String getUnidadMedida() {
		return unidadMedida;
	}

	/**
	 * Retorna el texto que corresponda si 'normal' es <b>false</b>.
	 * @return el texto que corresponda si 'normal' es <b>false</b>
	 */
	public String getValorFalso() {
		return valorFalso;
	}

	/**
	 * Retorna el texto que corresponda si 'normal' es <b>true</b>.
	 * @return el texto que corresponda si 'normal' es <b>true</b>
	 */
	public String getValorVerdadero() {
		return valorVerdadero;
	}

	/**
	 * Establece el valor de 'normal'.
	 * @param <b>'t'</b> si el examen físico se puede considerar normal o adecuado,
	 * <b>'f'</b> si se debe considerar como anormal o inadecuado,
	 * <b>' '</b> si se debe considerar como anormal o inadecuado,
	 */
	public void setNormal(char normal) {
		this.normal = normal;
	}

	/**
	 * Establece la unidad de medida adecuada para este exámen.
	 * @param unidadMedida la unidad de medida a establecer
	 */
	public void setUnidadMedida(String unidadMedida) {
	    if (unidadMedida!=null)
	    {
			this.unidadMedida = unidadMedida;
	    }
	}

	/**
	 * Establece el texto a mostrar si 'normal' es <b>false</b>.
	 * @param valorFalso el texto a mostrar si 'normal' es <b>false</b>
	 */
	public void setValorFalso(String valorFalso) {
	    if (valorFalso!=null)
	    {
			this.valorFalso = valorFalso;
	    }
	}

	/**
	 * Establece el texto a mostrar si 'normal' es <b>true</b>.
	 * @param valorVerdadero el texto a mostrar si 'normal' es <b>true</b>
	 */
	public void setValorVerdadero(String valorVerdadero) {
	    if (valorVerdadero!=null)
	    {
			this.valorVerdadero = valorVerdadero;
	    }
	}

	/**
	 * @return
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param string
	 */
	public void setNombre(String string) {
	    if (nombre!=null)
	    {
			nombre = string;
	    }
	}

	/**
	 * @return
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param i
	 */
	public void setCodigo(int i) {
		codigo = i;
	}

	/**
	 * @return
	 */
	public double getValor() {
		return valor;
	}

	/**
	 * @param d
	 */
	public void setValor(double d) {
		valor = d;
	}

	/**
	 * @return
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param string
	 */
	public void setDescripcion(String string) {
	    if (string!=null)
	    {
			descripcion = string;
	    }
	}

	/**
	 * Returns the normal.
	 * @return char
	 */
	public char getNormal() {
		return normal;
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