/*
 * @(#)AsignacionPermisosBean.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Este Bean maneja la asignación de permisos de un conjunto de pacientes a otro
 * conjunto de instituciones.
 *
 * @version 1.0, Nov 27, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class AsignacionPermisosBean {

	/**
	 * Arreglo con los nombres de las instituciones.
	 */
	private String instituciones[];

	/**
	 * Arreglo con los códigos de las instituciones.
	 */
	private String codigoInstituciones[];

	/**
	 * Arreglo con los números de las identificaciones de los pacientes.
	 */
	private String identificacionPacientes[];
	
	/**
	 * Arreglo con los nombres de las identificaciones de los pacientes.
	 */
	private String nombresPaciente[];

	/**
	 * Retorna un arreglo con todas las instituciones a las cuales se va a dar
	 * permiso sobre los pacientes especificados en los arreglos
	 * identificacionPacientes y tiposIdentificacionPacientes.
	 * @return String[] con un arreglo de instituciones
	 */
	public String[] getInstituciones() {
		return instituciones;
	}

	/**
	 * Retorna el arreglo con los codigos de las instituciones.
	 * @return String[] con el arreglo que contiene los codigos de las
	 * instituciones
	 */
	public String[] getCodigoInstituciones() {
		return codigoInstituciones;
	}

	/**
	 * Retorna el arreglo con los números de identificacion de los pacientes.
	 * @return String[] con el arreglo que contiene los números de
	 * identificacion
	 */
	public String[] getIdentificacionPacientes() {
		return identificacionPacientes;
	}

	/**
	 * Establece el arreglo de instituciones.
	 * @param instituciones El arreglo de instituciones a establecer
	 */
	public void setInstituciones(String[] instituciones) {
		this.instituciones = instituciones;
	}

	/**
	 * Establece el arreglo donde se guardan los números
	 * de identificacion de los pacientes.
	 * @param identificacionPacientes El arreglo de identificacion de
	 * los pacientes a establecer
	 */
	public void setIdentificacionPacientes(String[] identificacionPacientes) {
		this.identificacionPacientes = identificacionPacientes;
	}

	/**
	 * El objetivo de este método es el de dejar todas las variables de este
	 * bean en nulo, de tal manera que la siguiente vez que pase el recolector
	 * de basura, se libere la memoria innecesaria
	 */
	public void clean()
	{
		instituciones=null;
		identificacionPacientes=null;
	}

	/**
	 * Inicializa las variables que vienen de la forma código-nombre
	 */
	public void inicializarVariables ()
	{
		int i=0;
		String [] resultados;

		//Primero vamos a tomar las instituciones

		if (instituciones!=null&&instituciones.length>0)
		{
			//Antes que nada asignamos memoria de acuerdo a la
			//al tamaño del arreglo de instituciones

			codigoInstituciones=new String[instituciones.length];
			
			//Parseamos todo el arreglo
			for (i=0;i<instituciones.length;i++)
			{
				resultados = UtilidadTexto.separarNombresDeCodigos(instituciones[i], 1);
				codigoInstituciones[i] = resultados[0];
				instituciones[i] = resultados[1];
			}
		}
		
	}

	/**
	 * Este método me permite insertar todos los permisos necesarios, que devuelve
	 * el texto que se debe imprimir en el método. Como el usuario puede seleccionar
	 * el texto informativo, el sistema no tiene en cuenta estos casos, luego el 
	 * correspondiente mensaje de error estará en null.
	 * @param con conexión abierta con la BD
	 * @param tipoBD tipo de BD usada
	 * @param codigoInstitucionActual código de la institución actual
	 * @return el texto que debe imprimir el método
	 */
	public String[] darPermisos (Connection con, String codigoInstitucionActual) throws SQLException
	{
		String mensajesAImprimir[];
		RespuestaValidacion resp=null;
		int i,j;
		//Antes de empezar tenemos que asegurarnos que tanto el arreglo 
		//de instituciones como el de pacientes tiene al menos un elemento,
		//si no devolvemos un mensaje de error
		
		if (instituciones==null||codigoInstituciones==null||identificacionPacientes==null||instituciones.length<1||codigoInstituciones.length<1||identificacionPacientes.length<1)
		{
			mensajesAImprimir= new String[1];
			mensajesAImprimir[0]="Por Favor seleccione alguna pareja";
			return mensajesAImprimir;
		}

		//Vamos a crear un arreglo de Strings, en donde guardaremos todos los
		//mensajes de error / exito que genera cada uno de los permisos especificados
		//Para esto necesitamos un arreglo de tamaño m x n (m=número de instituciones
		//y n=número de pacientes)
		
		mensajesAImprimir= new String[identificacionPacientes.length*codigoInstituciones.length];

		for (i=0;i<identificacionPacientes.length;i++)
		{
			//Como el usuario puede seleccionar el texto informativo
			//Tenemos que asegurarnos que el codigo no sea 0
			if (!identificacionPacientes[i].equals("0"))
			{
				int codigoEntero=0;
				for (j=0;j<codigoInstituciones.length;j++)
				{
					//De nuevo tenemos que verificar que no sea el codigo de mentiras (0)
					if (!codigoInstituciones[j].equals("0"))
					{
						//En este punto insertamos un permiso, primero llamamos a la utilidad
						//de validacion, despuès insertamos y por ùltimo guardamos el mensaje 
						//que vamos a mostrar por pantalla
						codigoEntero=Integer.parseInt(identificacionPacientes[i]);
						resp=UtilidadValidacion.validacionPermisosInstitucionPaciente2 (con, codigoEntero, codigoInstitucionActual, codigoInstituciones[j]);
						mensajesAImprimir[(i*codigoInstituciones.length)+j]= resp.textoRespuesta + " Institucion: " + instituciones[j]+ " Paciente : "  + UtilidadValidacion.obtenerNombrePersona(con,codigoEntero)  ;
					}
				}
			}
		}
		return mensajesAImprimir;
	}

}