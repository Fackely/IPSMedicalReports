/*
 * @(#)GrupoEtareoCrecimientoDesarrolloForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.actionform.pyp;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ResultadoBoolean;
import util.UtilidadTexto;

/**
 * Forma para manejo presentación de la funcionalidad 
 * Ingresar Modificar Grupo Etareo de Crecimiento y Desarrollo
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 28 /Jul/ 2006
 */
public class GrupoEtareoCrecimientoDesarrolloForm extends ValidatorForm
{

	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private  String estado = "";
	
	/**
	 * almacena los datos de la consulta
	 */
	private HashMap mapaGruposEtareos;
	
	/**
	 * almacena los datos de la consulta
	 */
	private HashMap mapaGruposEtareosAux;
	
	/**
	 * Mapa con los datos originales
	 */
	private HashMap mapaGruposEtareosNoModificado;
	
	/**
 	 * Offset para el pager 
 	 */
 	private int offset = 0;
 	
    /**
 	 * Este campo contiene el pageUrl para controlar el pager,
 	 *  y conservar los valores del hashMap mediante un submit de
 	 * JavaScript. (Integra pager -Valor Captura)
 	 */
     private String linkSiguiente;
     
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
	
	private ResultadoBoolean resultado=new ResultadoBoolean(false);
 	
 	/**
 	 * Reset generla de la Forma
 	 */
	public void reset ()
	{
		this.mapaGruposEtareos = new HashMap ();
		this.mapaGruposEtareos.put("numRegistros", "0");
		this.mapaGruposEtareosNoModificado = new HashMap();
		this.mapaGruposEtareosNoModificado.put("numRegistros", "0");
		this.mapaGruposEtareosAux =  new HashMap();
		this.mapaGruposEtareosAux.put("numRegistros", "0");
		this.estado = "";
		this.linkSiguiente = "";
	 	this.patronOrdenar = "";
	 	this.ultimoPatron = "";
	 	this.posicionMapa = 0;
	 	this.mensaje = "";
	 	this.resultado=new ResultadoBoolean(false);
	}
	
	/**
	 * Reset único para los mapas
	 */
	public void resetMapa()
	{
		this.mapaGruposEtareos = new HashMap ();
		this.mapaGruposEtareosNoModificado = new HashMap();
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
	 * @return Returns the linkSiguiente.
	 */
	public String getLinkSiguiente()
	{
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente)
	{
		this.linkSiguiente=linkSiguiente;
	}

	/**
	 * @return Returns the offset.
	 */
	public int getOffset()
	{
		return offset;
	}

	/**
	 * @param offset The offset to set.
	 */
	public void setOffset(int offset)
	{
		this.offset=offset;
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
	 * @return Returns the mapaGruposEtareos
	 */
	public HashMap getMapaGruposEtareos()
	{
		return mapaGruposEtareos;
	}
	
	/**
	 * @param mapaGruposEtareos The mapaGruposEtareos to set.
	 */
	public void setMapaGruposEtareos(HashMap mapaGruposEtareos)
	{
		this.mapaGruposEtareos = mapaGruposEtareos;
	}
	
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaGruposEtareos(String key) 
	{
		return mapaGruposEtareos.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaGruposEtareos(String key, Object value) 
	{
		mapaGruposEtareos.put(key, value);
	}
	
	/**
	 * @return Returns the mapaGruposEtareos
	 */
	public HashMap getMapaGruposEtareosNoModificado()
	{
		return mapaGruposEtareosNoModificado;
	}
	
	/**
	 * @param mapaGruposEtareosNoModificado The mapaGruposEtareosNoModificado to set.
	 */
	public void setMapaGruposEtareosNoModificado(HashMap mapaGruposEtareosNoModificado)
	{
		this.mapaGruposEtareosNoModificado = mapaGruposEtareosNoModificado;
	}
	
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaGruposEtareosNoModificado(String key) 
	{
		return mapaGruposEtareosNoModificado.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaGruposEtareosNoModificado(String key, Object value) 
	{
		mapaGruposEtareosNoModificado.put(key, value);
	}
	
	/**
	 * @return Returns the mapaGruposEtareos
	 */
	public HashMap getMapaGruposEtareosAux()
	{
		return mapaGruposEtareosAux;
	}
	
	/**
	 * @param mapaGruposEtareosAux The mapaGruposEtareosAux to set.
	 */
	public void setMapaGruposEtareosAux(HashMap mapaGruposEtareosAux)
	{
		this.mapaGruposEtareosAux = mapaGruposEtareosAux;
	}
	
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaGruposEtareosAux(String key) 
	{
		return mapaGruposEtareosAux.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaGruposEtareosAux(String key, Object value) 
	{
		mapaGruposEtareosAux.put(key, value);
	}
	
	
	public ResultadoBoolean getResultado() {
		return resultado;
	}

	public void setResultado(ResultadoBoolean resultado) {
		this.resultado = resultado;
	}

	/**
	 * Metodo para insertar los valores en el mapa aux que va ha contener los datos modificados
	 * @param k
	 * @param keyMapa
	 * @return
	 */
	private boolean modificarMapaAux(int k, String keyMapa)	
	{
	    boolean fueInsertadoEnMapa = false;
	    int temp = 0;
	    String campoMapaOriginalNoMod = this.getMapaGruposEtareosNoModificado(keyMapa+k)+"";
	    String campoMapaModificado = "";
	    if(campoMapaOriginalNoMod != null || !campoMapaOriginalNoMod.equals("") || !campoMapaOriginalNoMod.equals("null"))
        {
	        campoMapaModificado = this.getMapaGruposEtareos(keyMapa+k)+"";
	        if(campoMapaOriginalNoMod.compareTo(campoMapaModificado)!=0)
            {
	        	mapaGruposEtareosAux.put("codigo_"+ k, this.getMapaGruposEtareos("codigo_"+k));
	        	mapaGruposEtareosAux.put("codigointerno_"+ k, this.getMapaGruposEtareos("codigointerno_"+k));
	        	mapaGruposEtareosAux.put("descripcion_"+ k, this.getMapaGruposEtareos("descripcion_"+k));
	        	mapaGruposEtareosAux.put("codigounidadmedida_"+ k, this.getMapaGruposEtareos("codigounidadmedida_"+k));
	        	mapaGruposEtareosAux.put("rangoinicial_"+ k, this.getMapaGruposEtareos("rangoinicial_"+k));
	        	mapaGruposEtareosAux.put("rangofinal_"+ k, this.getMapaGruposEtareos("rangofinal_"+k));
	        	mapaGruposEtareosAux.put("codigosexo_"+ k, this.getMapaGruposEtareos("codigosexo_"+k));
	            mapaGruposEtareosAux.put("activo_"+ k, this.getMapaGruposEtareos("activo_"+k));
	            if(mapaGruposEtareosAux.containsKey("numRegistros"))
	            {
	            	temp = Integer.parseInt(mapaGruposEtareosAux.get("numRegistros")+"");
	            	temp =  temp +1;
	            	mapaGruposEtareosAux.put("numRegistros", temp);
	            }
	            else
	            {
	            	mapaGruposEtareosAux.put("numRegistros", k+1);
	            }
	            
			    fueInsertadoEnMapa=true;
            }    
        }    
	    return fueInsertadoEnMapa;
	}
	
	/**
	 * Metodo para comparar dos HashMap
	 * @return
	 */
	public boolean comparar2HashMap()
	{
	    this.mapaGruposEtareosAux = new HashMap();
	    this.mapaGruposEtareosAux.put("numRegistros", 0);
	    boolean fueInsertadoEnMapa = false;

	    for(int k = 0 ; k < Integer.parseInt(this.getMapaGruposEtareosNoModificado("numRegistros")+"") ; k++)
	    {
	        if((this.getMapaGruposEtareos("existebd_"+k)+"").equals("si"))
	        {
	           fueInsertadoEnMapa = this.modificarMapaAux(k, "codigo_");
	           { 
	               fueInsertadoEnMapa = this.modificarMapaAux(k, "descripcion_");
	               if(!fueInsertadoEnMapa)
			       {
			           fueInsertadoEnMapa =this.modificarMapaAux(k, "codigounidadmedida_");
			           if(!fueInsertadoEnMapa)
				       {
			               fueInsertadoEnMapa = this.modificarMapaAux(k, "rangoinicial_");
			               if(!fueInsertadoEnMapa)
			               {
			                   fueInsertadoEnMapa = this.modificarMapaAux(k, "rangofinal_");
			                   if(!fueInsertadoEnMapa)
			                   {
			                       fueInsertadoEnMapa = this.modificarMapaAux(k, "codigosexo_");
				                   if(!fueInsertadoEnMapa)
				                   {
				                       fueInsertadoEnMapa = this.modificarMapaAux(k, "activo_");
				                   }    
			                   }    
			               }
			           }
				   }
	           }    
	        }    
	    }
	    return fueInsertadoEnMapa;
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
		ActionErrors errores = new ActionErrors();					
		
		if(estado.equals("guardar"))
		{
			String temp = "";
			String temporal = "";
			String descripcion = "";
			int numeroRegistros = Integer.parseInt(this.getMapaGruposEtareos("numRegistros")+"");
			//Validamos que no hayan edades codigos vacios
			for(int k = 0 ; k < numeroRegistros ; k++)
			{
				if(!UtilidadTexto.getBoolean(this.getMapaGruposEtareos("eseliminado_"+k)+""))
				{
					temp = this.getMapaGruposEtareos("codigo_"+k)+"";
					if(temp.equals(""))
					{
						if(descripcion.equals(""))
							descripcion = (k+1) + "";
						else	
							descripcion += "," + (k+1);
					}
				}
			}
			if(!descripcion.trim().equals(""))
			{
				errores.add("codigo vacio", new ActionMessage("error.pyp.gruposEtareos.atributoRequeridoVacio", "Código", descripcion));
			}
			
			temp = "";
			descripcion = "";
			//Validamos que no hayan descripciones vacias
			for(int k = 0 ; k < numeroRegistros ; k++)
			{
				if(!UtilidadTexto.getBoolean(this.getMapaGruposEtareos("eseliminado_"+k)+""))
				{
					temp = this.getMapaGruposEtareos("descripcion_"+k)+"";
					if(temp.equals(""))
					{
						if(descripcion.equals(""))
							descripcion = (k+1) + "";
						else	
							descripcion += "," + (k+1);
					}
				}
			}
			if(!descripcion.trim().equals(""))
			{
				errores.add("descripcion vacia", new ActionMessage("error.pyp.gruposEtareos.atributoRequeridoVacio", "Descripción", descripcion));
			}
			
			temp = "";
			temporal = "";
			descripcion = "";
			for(int k = 0 ; k < numeroRegistros ; k++)
			{
				if(!UtilidadTexto.getBoolean(this.getMapaGruposEtareos("eseliminado_"+k)+""))
				{
					temp = this.getMapaGruposEtareos("rangoinicial_"+k)+"";
					temporal = this.getMapaGruposEtareos("rangofinal_"+k)+"";
					if(!temp.equals("") && !temporal.equals(""))
					{
						if(Integer.parseInt(temporal) < Integer.parseInt(temp))
						{
							if(descripcion.equals(""))
								descripcion = (k+1) + "";
							else	
								descripcion += "," + (k+1);
						}
					}
				}
			}
			if(!descripcion.trim().equals(""))
			{
				errores.add("rango final menor que rango inicial", new ActionMessage("error.pyp.gruposEtareos.rangoFinalMayorQueRangoInicial", descripcion));
			}
			
			temp = "";
			descripcion = "";
			//Validamos que no hayan sexos no seleccionados
			for(int k = 0 ; k < numeroRegistros ; k++)
			{
				if(!UtilidadTexto.getBoolean(this.getMapaGruposEtareos("eseliminado_"+k)+""))
				{
					temp = this.getMapaGruposEtareos("codigosexo_"+k)+"";
					if(temp.equals("-1"))
					{
						if(descripcion.equals(""))
							descripcion = (k+1) + "";
						else	
							descripcion += "," + (k+1);
					}
				}
			}
			if(!descripcion.trim().equals(""))
			{
				errores.add("sexo no seleccionado", new ActionMessage("error.pyp.gruposEtareos.atributoRequeridoVacio", "Sexo", descripcion));
			}
			
			temp = "";
			descripcion = "";
			//Validamos que no hayan unidades de medidas no seleccionadas
			for(int k = 0 ; k < numeroRegistros ; k++)
			{
				if(!UtilidadTexto.getBoolean(this.getMapaGruposEtareos("eseliminado_"+k)+""))
				{
					temp = this.getMapaGruposEtareos("codigounidadmedida_"+k)+"";
					if(temp.equals("-1"))
					{
						if(descripcion.equals(""))
							descripcion = (k+1) + "";
						else	
							descripcion += "," + (k+1);
					}
				}
			}
			if(!descripcion.trim().equals(""))
			{
				errores.add("unidad de medida no seleccionada", new ActionMessage("error.pyp.gruposEtareos.atributoRequeridoVacio", "Unidad de Medida", descripcion));
			}
			
			temp = "";
			descripcion = "";
			//Validamos que no hayan rangos iniciales vacios
			for(int k = 0 ; k < numeroRegistros ; k++)
			{
				if(!UtilidadTexto.getBoolean(this.getMapaGruposEtareos("eseliminado_"+k)+""))
				{
					temp = this.getMapaGruposEtareos("rangoinicial_"+k)+"";
					if(temp.equals(""))
					{
						if(descripcion.equals(""))
							descripcion = (k+1) + "";
						else	
							descripcion += "," + (k+1);
					}
				}
			}
			if(!descripcion.trim().equals(""))
			{
				errores.add("rango inicial vacio", new ActionMessage("error.pyp.gruposEtareos.atributoRequeridoVacio", "Rango Inicial", descripcion));
			}
			
			
			temp = "";
			descripcion = "";
			//Validamos que no hayan rangos finales vacios
			for(int k = 0 ; k < numeroRegistros ; k++)
			{
				if(!UtilidadTexto.getBoolean(this.getMapaGruposEtareos("eseliminado_"+k)+""))
				{
					temp = this.getMapaGruposEtareos("rangofinal_"+k)+"";
					if(temp.equals(""))
					{
						if(descripcion.equals(""))
							descripcion = (k+1) + "";
						else	
							descripcion += "," + (k+1);
					}
				}
			}
			if(!descripcion.trim().equals(""))
			{
				errores.add("rango final vacio", new ActionMessage("error.pyp.gruposEtareos.atributoRequeridoVacio", "Rango Final", descripcion));
			}
			
			
			temp = "";
			descripcion = "";
			//Validamos que  rango inicial sea mayor o igual a 0
			for(int k = 0 ; k < numeroRegistros ; k++)
			{
				if(!UtilidadTexto.getBoolean(this.getMapaGruposEtareos("eseliminado_"+k)+""))
				{
					temp = this.getMapaGruposEtareos("rangoinicial_"+k)+"";
					if(!temp.equals(""))
					{	
						int rangoRnicial = Integer.parseInt(temp);
						if(rangoRnicial < 0)
						{
							if(descripcion.equals(""))
								descripcion = (k+1) + "";
							else	
								descripcion += "," + (k+1);
						}
					}
				}
			}
			if(!descripcion.trim().equals(""))
			{
				errores.add("rango inicial menor que 0", new ActionMessage("error.pyp.gruposEtareos.rangoMenorQueCero", "Inicial", descripcion));
			}
			
			temp = "";
			descripcion = "";
			//Validamos que  rango final sea mayor o igual a 0
			for(int k = 0 ; k < numeroRegistros ; k++)
			{
				if(!UtilidadTexto.getBoolean(this.getMapaGruposEtareos("eseliminado_"+k)+""))
				{
					temp = this.getMapaGruposEtareos("rangoinicial_"+k)+"";
					if(!temp.equals(""))
					{	
						int rangoFinal = Integer.parseInt(temp);
						if(rangoFinal < 0)
						{
							if(descripcion.equals(""))
								descripcion = (k+1) + "";
							else	
								descripcion += "," + (k+1);
						}
					}
				}
			}
			if(!descripcion.trim().equals(""))
			{
				errores.add("rango final menor que 0", new ActionMessage("error.pyp.gruposEtareos.rangoMenorQueCero", "Final", descripcion));
			}
			
			//Validamos que no haya registros con:
			//rangoInicial-rangoFinal-Sexo-Unidad de Medida
			String auxS1 = "";
			String auxS0 = "";
			HashMap datosComparados = new HashMap();
			descripcion = "";
			int numregistros = Integer.parseInt(this.getMapaGruposEtareos("numRegistros").toString());
			for(int i = 0 ; i < numregistros ; i++)
			{
				if(!UtilidadTexto.getBoolean(this.getMapaGruposEtareos("eseliminado_"+i)+""))
				{
					auxS0 = this.getMapaGruposEtareos("rangoinicial_"+i)+"".trim()+"-"+this.getMapaGruposEtareos("rangofinal_"+i)+"".trim()+"-"+this.getMapaGruposEtareos("codigosexo_"+i)+"".trim()+"-"+this.getMapaGruposEtareos("codigounidadmedida_"+i)+"".trim();
					for(int j = (numregistros-1) ; j > i ; j--)
					{
						
						auxS1 = this.getMapaGruposEtareos("rangoinicial_"+j).toString().trim()+"-"+this.getMapaGruposEtareos("rangofinal_"+j).toString().trim()+"-"+this.getMapaGruposEtareos("codigosexo_"+j).toString().trim()+"-"+this.getMapaGruposEtareos("codigounidadmedida_"+j).toString().trim();
						//Se realiza la comparacion compara
						if(auxS0.compareToIgnoreCase(auxS1)==0&&!auxS0.equals("")&&!auxS1.equals("")&&!datosComparados.containsValue(auxS0))
						{
							if(descripcion.equals(""))
								descripcion = (i+1) + "";
							descripcion += "," + (j+1);
							
						}
					}
				}
				
				if(!descripcion.equals(""))
				{
					errores.add("atributos repetidos no validos", new ActionMessage("error.pyp.gruposEtareos.atributosRepetidos",	descripcion));
				}
				descripcion = "";
				datosComparados.put(i+"",auxS0.toUpperCase());
			}
			
			
			//Validamos que no haya registros con el mismo codigo:
			auxS1 = "";
			auxS0 = "";
			datosComparados = new HashMap();
			descripcion = "";
			numregistros = Integer.parseInt(this.getMapaGruposEtareos("numRegistros").toString());
			for(int i = 0 ; i < numregistros ; i++)
			{
				if(!UtilidadTexto.getBoolean(this.getMapaGruposEtareos("eseliminado_"+i)+""))
				{
					auxS0 = this.getMapaGruposEtareos("codigo_"+i)+"".trim();
					for(int j = (numregistros-1) ; j > i ; j--)
					{
						
						auxS1 = this.getMapaGruposEtareos("codigo_"+j)+"".trim();
						//Se realiza la comparacion compara
						if(auxS0.compareToIgnoreCase(auxS1) == 0 && !auxS0.equals("") && !auxS1.equals("") && !datosComparados.containsValue(auxS0))
						{
							if(descripcion.equals(""))
								descripcion = (i+1) + "";
							descripcion += "," + (j+1);
							
						}
					}
				}
				
				if(!descripcion.equals(""))
				{
					errores.add("codigos repetidos no validos", new ActionMessage("error.pyp.gruposEtareos.codigosRepetidos",	descripcion));
				}
				descripcion = "";
				datosComparados.put(i+"",auxS0.toUpperCase());
			}
		}
		return errores;
	}
	
	
}