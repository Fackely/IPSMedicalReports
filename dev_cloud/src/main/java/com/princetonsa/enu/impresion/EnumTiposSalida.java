package com.princetonsa.enu.impresion;

/**
 * Esta clase se encarga de listar los tipos de 
 * salida para la impresión de archivos.
 *
 * @author Yennifer Guerrero
 * @since  23/09/2010
 *
 */
public enum EnumTiposSalida 
{

	//mensaje.tipoSalida.Pdf
	PDF("mensaje.tipoSalida.Pdf", 1),
	
	//mensaje.tipoSalida.ArchivoPlano
	PLANO("mensaje.tipoSalida.ArchivoPlano", 2),
	
	//mensaje.tipoSalida.HojaCalculo
	HOJA_CALCULO("mensaje.tipoSalida.HojaCalculo", 3);
	
	
	
	private final String recurso;
	private final int codigo;
	
	EnumTiposSalida(String recurso, int codigo)
	{
		this.recurso=recurso;
		this.codigo=codigo;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo codigo
	 * 
	 * @return  Retorna la variable codigo
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo recurso
	 * 
	 * @return  Retorna la variable recurso
	 */
	public String getRecurso() {
		return recurso;
	}
	
}
