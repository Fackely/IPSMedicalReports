/*
 * @(#)SistemasMotivoConsultaForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.actionform.historiaClinica;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;


/**
 * Forma para manejo presentación de la funcionalidad 
 * Ingresar Modificar Sistemas Motivo de Consultas de Urgencias
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 31 /May/ 2006
 */
public class SistemasMotivoConsultaForm extends ValidatorForm
{

	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private  String estado = "";
	
	/**
	 * almacena los datos de la consulta
	 */
	private HashMap mapaMotivosConsulta;
	
	/**
	 * Mapa con los datos originales
	 */
	private HashMap mapaMotivosConsultaNoModificado;
	
 	/**
	 * Patron de ordenamiento por columnas
	 */
	private String patronOrdenar;
	
	/**
	 * String ultimo patron de ordenamiento
	 */
	private String ultimoPatron;
	
	/**
	 * Posicion del mapa a eliminar
	 */
	private int posicionMapa;
	
	/**
	 * String para mensajes de exito o de error 
	 */
	private String mensaje;
 	
 	/**
 	 * Reset generla de la Forma
 	 */
	public void reset ()
	{
		this.mapaMotivosConsulta = new HashMap ();
		this.mapaMotivosConsultaNoModificado = new HashMap();
		this.estado = "";
	 	this.patronOrdenar = "";
	 	this.ultimoPatron = "";
	 	this.posicionMapa = 0;
	 	this.mensaje = "";
	}
	
	/**
	 * Reset único para los mapas
	 */
	public void resetMapa()
	{
		this.mapaMotivosConsulta = new HashMap ();
		this.mapaMotivosConsultaNoModificado = new HashMap();
	}

	
	public void resetMensaje()
	{
		this.mensaje = "";
	}
	
	/**
	 * @return Returns the mensaje.
	 */
	public String getMensaje()
	{
		return mensaje;
	}

	/**
	 * @param mensaje The mensaje to set.
	 */
	public void setMensaje(String mensaje)
	{
		this.mensaje=mensaje;
	}

	/**
	 * @return Returns the posicionMapa.
	 */
	public int getPosicionMapa()
	{
		return posicionMapa;
	}

	/**
	 * @param posicionMapa The posicionMapa to set.
	 */
	public void setPosicionMapa(int posicionMapa)
	{
		this.posicionMapa=posicionMapa;
	}

	/**
	 * @return Returns the estado.
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado)
	{
		this.estado=estado;
	}


	/**
	 * @return Returns the patronOrdenar.
	 */
	public String getPatronOrdenar()
	{
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar The patronOrdenar to set.
	 */
	public void setPatronOrdenar(String patronOrdenar)
	{
		this.patronOrdenar=patronOrdenar;
	}

	/**
	 * @return Returns the ultimoPatron.
	 */
	public String getUltimoPatron()
	{
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron The ultimoPatron to set.
	 */
	public void setUltimoPatron(String ultimoPatron)
	{
		this.ultimoPatron=ultimoPatron;
	}
	
	/**
	 * @return Returns the mapaMotivosConsulta
	 */
	public HashMap getMapaMotivosConsulta()
	{
		return mapaMotivosConsulta;
	}
	
	/**
	 * @param mapaMotivosConsulta The mapaMotivosConsulta to set.
	 */
	public void setMapaMotivosConsulta(HashMap mapaMotivosConsulta)
	{
		this.mapaMotivosConsulta = mapaMotivosConsulta;
	}
	
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaMotivosConsulta(String key) 
	{
		return mapaMotivosConsulta.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaMotivosConsulta(String key, Object value) 
	{
		mapaMotivosConsulta.put(key, value);
	}
	
	/**
	 * @return Returns the mapaMotivosConsulta
	 */
	public HashMap getMapaMotivosConsultaNoModificado()
	{
		return mapaMotivosConsultaNoModificado;
	}
	
	/**
	 * @param mapaMotivosConsultaNoModificado The mapaMotivosConsultaNoModificado to set.
	 */
	public void setMapaMotivosConsultaNoModificado(HashMap mapaMotivosConsultaNoModificado)
	{
		this.mapaMotivosConsultaNoModificado = mapaMotivosConsultaNoModificado;
	}
	
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaMotivosConsultaNoModificado(String key) 
	{
		return mapaMotivosConsultaNoModificado.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaMotivosConsultaNoModificado(String key, Object value) 
	{
		mapaMotivosConsultaNoModificado.put(key, value);
	}
	
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
			//Validamos que no hayan identificadores vacios
			String tempoDesc = "";
		    String descripcion = "";
		    int numregistros = Integer.parseInt(this.getMapaMotivosConsulta("numRegistros").toString());
			for(int k = 0 ; k < numregistros ; k++)
			{
				tempoDesc=this.getMapaMotivosConsulta("identificador_"+k)+"";
				if(tempoDesc.equals(""))
				{
					if(descripcion.equals(""))
						descripcion = (k+1) + "";
					else	
						descripcion += "," + (k+1);
				}
			}
			if(!descripcion.trim().equals(""))
			{
				errores.add("identificador vacia", new ActionMessage("error.historiaClinica.MotivosConsultaUrgencias.atributoRequeridoVacio", "Código", descripcion));
			}
			
			//Validamos que no haya una descripcion vacia
			tempoDesc = "";
		    descripcion = "";
			for(int k = 0 ; k < numregistros ; k++)
			{
				tempoDesc=this.getMapaMotivosConsulta("descripcion_"+k)+"";
				if(tempoDesc.equals(""))
				{
					if(descripcion.equals(""))
						descripcion = (k+1) + "";
					else	
						descripcion += "," + (k+1);
				}
			}
			if(!descripcion.trim().equals(""))
			{
				errores.add("descripcion vacia", new ActionMessage("error.historiaClinica.MotivosConsultaUrgencias.atributoRequeridoVacio", "Descripción", descripcion));
			}
			
			
			 //Comparamos si existe la el mismo Identificador del sistema motivo de consulta
			String auxS1 = "";
			String auxS0 = "";
			HashMap codigosComparados = new HashMap();
			descripcion = "";
			for(int i = 0 ; i < numregistros ; i++)
			{
				auxS0 = this.getMapaMotivosConsulta("identificador_"+i).toString().trim();
				for(int j = (numregistros-1) ; j > i ; j--)
				{
					auxS1 = this.getMapaMotivosConsulta("identificador_"+j).toString().trim();
					//Se realiza la comparacion 
					if(auxS0.compareToIgnoreCase(auxS1)==0&&!auxS0.equals("")&&!auxS1.equals("")&&!codigosComparados.containsValue(auxS0))
					{
						if(descripcion.equals(""))
							descripcion = (i+1) + "";
						else
							descripcion += "," + (j+1);
						
					}
				}
				
				if(!descripcion.equals(""))
				{
					errores.add("identificador Repetido", new ActionMessage("error.historiaClinica.MotivosConsultaUrgencias.igualIdentificador",	descripcion));
				}
				descripcion = "";
				codigosComparados.put(i+"",auxS0.toUpperCase());
			}
			
		}
		return errores;
	}
	
	
}