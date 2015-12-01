/*
 * @(#)GruposEtareosForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.actionform.capitacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.Utilidades;


/**
 * Forma para manejo presentación de la funcionalidad 
 * Ingresar Modificar Grupos Etareos
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 25 /May/ 2006
 */
public class GruposEtareosForm extends ValidatorForm
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
	 * Mapa con los datos originales
	 */
	private HashMap mapaGruposEtareosNoModificado;
	
	/**
 	 * Offset para el pager 
 	 */
 	private int offset=0;
 	
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
	 * Int para el codigo del convenio
	 */
	private int codigoConvenio;
	
	/**
	 * String de la Fecha inicial
	 */
	private String fechaInicial;
	
	/**
	 * String con la fecha final
	 */
	private String fechaFinal;
	
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
		this.mapaGruposEtareos = new HashMap ();
		this.mapaGruposEtareosNoModificado = new HashMap();
		this.estado = "";
		this.linkSiguiente = "";
	 	this.patronOrdenar = "";
	 	this.ultimoPatron = "";
	 	this.codigoConvenio = -1;
	 	this.fechaInicial = "";
	 	this.fechaFinal = "";
	 	this.posicionMapa = 0;
	 	this.mensaje = "";
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
	 * @return Returns the codigoConvenio.
	 */
	public int getCodigoConvenio()
	{
		return codigoConvenio;
	}

	/**
	 * @param codigoConvenio The codigoConvenio to set.
	 */
	public void setCodigoConvenio(int codigoConvenio)
	{
		this.codigoConvenio=codigoConvenio;
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
	 * @return Returns the fechaFinal.
	 */
	public String getFechaFinal()
	{
		return fechaFinal;
	}

	/**
	 * @param fechaFinal The fechaFinal to set.
	 */
	public void setFechaFinal(String fechaFinal)
	{
		this.fechaFinal=fechaFinal;
	}

	/**
	 * @return Returns the fechaInicial.
	 */
	public String getFechaInicial()
	{
		return fechaInicial;
	}

	/**
	 * @param fechaInicial The fechaInicial to set.
	 */
	public void setFechaInicial(String fechaInicial)
	{
		this.fechaInicial=fechaInicial;
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
			String temp = "";
			String temporal = "";
			String descripcion = "";
			int numeroRegistros = Integer.parseInt(this.getMapaGruposEtareos("numRegistros").toString());
			//Validamos que no hayan edades iniciales vacias
			for(int k = 0 ; k < numeroRegistros ; k++)
			{
				temp=this.getMapaGruposEtareos("edadinicial_"+k)+"";
				if(temp.equals(""))
				{
					if(descripcion.equals(""))
						descripcion = (k+1) + "";
					else	
						descripcion += "," + (k+1);
				}
			}
			if(!descripcion.trim().equals(""))
			{
				errores.add("edad inicial vacia", new ActionMessage("error.capitacion.gruposEtareos.atributoRequeridoVacio", "Edad Inicial", descripcion));
			}
			
			temp = "";
			descripcion = "";
			//Validamos que no hayan edades finales vacias
			for(int k = 0 ; k < numeroRegistros ; k++)
			{
				temp=this.getMapaGruposEtareos("edadfinal_"+k)+"";
				if(temp.equals(""))
				{
					if(descripcion.equals(""))
						descripcion = (k+1) + "";
					else	
						descripcion += "," + (k+1);
				}
			}
			if(!descripcion.trim().equals(""))
			{
				errores.add("edad final vacia", new ActionMessage("error.capitacion.gruposEtareos.atributoRequeridoVacio", "Edad Final", descripcion));
			}
			
			temp = "";
			temporal = "";
			descripcion = "";
			for(int k = 0 ; k < numeroRegistros ; k++)
			{
				temp = this.getMapaGruposEtareos("edadinicial_"+k)+"";
				temporal = this.getMapaGruposEtareos("edadfinal_"+k)+"";
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
			if(!descripcion.trim().equals(""))
			{
				errores.add("edad final menor que edad inicial", new ActionMessage("error.capitacion.gruposEtareos.edadFinalMayorQueEdadInicial", descripcion));
			}
			
			temp = "";
			descripcion = "";
			//Validamos que no hayan sexos no seleccionados
			for(int k = 0 ; k < numeroRegistros ; k++)
			{
				temp=this.getMapaGruposEtareos("codigosexo_"+k)+"";
				if(temp.equals("-1"))
				{
					if(descripcion.equals(""))
						descripcion = (k+1) + "";
					else	
						descripcion += "," + (k+1);
				}
			}
			if(!descripcion.trim().equals(""))
			{
				errores.add("sexo no seleccionado", new ActionMessage("error.capitacion.gruposEtareos.atributoRequeridoVacio", "Sexo", descripcion));
			}
			
			temp = "";
			descripcion = "";
			//Validamos que no hayan fechas iniciales vacias
			for(int k = 0 ; k < numeroRegistros ; k++)
			{
				temp=this.getMapaGruposEtareos("fechainicial_"+k)+"";
				if(temp.equals(""))
				{
					if(descripcion.equals(""))
						descripcion = (k+1) + "";
					else	
						descripcion += "," + (k+1);
				}
			}
			if(!descripcion.trim().equals(""))
			{
				errores.add("fecha inicial vacia", new ActionMessage("error.capitacion.gruposEtareos.atributoRequeridoVacio", "Fecha Inicial", descripcion));
			}
			
			
			temp = "";
			descripcion = "";
			//Validamos el formato de las fechas iniciales
			for(int k = 0 ; k < numeroRegistros ; k++)
			{
				temp=this.getMapaGruposEtareos("fechainicial_"+k)+"";
				if(!temp.equals(""))
				{
					if(!UtilidadFecha.validarFecha(temp))
					{
						if(descripcion.equals(""))
							descripcion = (k+1) + "";
						else	
							descripcion += "," + (k+1);
					}
				}
			}
			if(!descripcion.equals(""))
			{
				errores.add("fechaInicial", new ActionMessage("errors.formatoFechaInvalido", "Inicial de el/los Registro(s)("+descripcion+")"));
			}
			
			
			temp = "";
			descripcion = "";
			//Validamos que no hayan fechas finales vacias
			for(int k = 0 ; k < numeroRegistros ; k++)
			{
				temp=this.getMapaGruposEtareos("fechafinal_"+k)+"";
				if(temp.equals(""))
				{
					if(descripcion.equals(""))
						descripcion = (k+1) + "";
					else	
						descripcion += "," + (k+1);
				}
			}
			if(!descripcion.trim().equals(""))
			{
				errores.add("fecha final vacia", new ActionMessage("error.capitacion.gruposEtareos.atributoRequeridoVacio", "Fecha Final", descripcion));
			}
			
			
			temp = "";
			descripcion = "";
			//Validamos el formato de las fechas iniciales
			for(int k = 0 ; k < numeroRegistros ; k++)
			{
				temp=this.getMapaGruposEtareos("fechafinal_"+k)+"";
				if(!temp.equals(""))
				{
					if(!UtilidadFecha.validarFecha(temp))
					{
						if(descripcion.equals(""))
							descripcion = (k+1) + "";
						else	
							descripcion += "," + (k+1);
					}
				}
			}
			if(!descripcion.equals(""))
			{
				errores.add("fechaFinal", new ActionMessage("errors.formatoFechaInvalido", "Final de el/los Registro(s)("+descripcion+")"));
			}
			
			
			temp = "";
			descripcion = "";
			//Validamos que no hayan valores vacias
			for(int k = 0 ; k < numeroRegistros ; k++)
			{
				temp = this.getMapaGruposEtareos("valor_"+k)+"";
				if(temp.equals(""))
				{
					if(descripcion.equals(""))
						descripcion = (k+1) + "";
					else	
						descripcion += "," + (k+1);
				}
				else
				{
					double tempValor = Double.parseDouble(this.getMapaGruposEtareos("valor_"+k)+"");
					if(tempValor <= 0)
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
				errores.add("valor vacio o igual a cero", new ActionMessage("error.capitacion.gruposEtareos.atributoRequeridoVacio", "Valor", descripcion));
			}
			
			
			temp = "";
			descripcion = "";
			//Se valida si el campo esta Nulo o Vacio para los Convenios que tienen PYP en Si
			for(int k = 0 ; k < numeroRegistros ; k++)
			{
				temp=this.getMapaGruposEtareos("pyp_"+k)+"";
				if(Utilidades.convenioManejaPyp(this.codigoConvenio))
				{
					if(temp.equals("") || temp.equals("0"))
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
				errores.add("pyp vacio o igual a cero", new ActionMessage("error.capitacion.gruposEtareos.atributoRequeridoVacio", "%PyP", descripcion));
			}

			//se hacen las siguientes validaciones, solo si todos los campos existen. (es decir no hay errores.)
			if(errores.isEmpty())
			{
				temp = "";
				descripcion = "";
				//Validamos que  %pyp sea meor o igual a 100%
				for(int k = 0 ; k < numeroRegistros ; k++)
				{
					temp=this.getMapaGruposEtareos("pyp_"+k)+"";
					if(!temp.equals(""))
					{	
						double pyp = Double.parseDouble(temp);
						if(pyp > 100)
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
					errores.add("pyp mayor que 100%", new ActionMessage("error.capitacion.gruposEtareos.pypMayorQueCienPorciento", descripcion));
				}
				
				//Validamos que no haya registros con:
				//edadInicial-EdadFinal-Sexo-fechaInicial-fechaFinal repetidos
				String auxS1 = "";
				String auxS0 = "";
				HashMap datosComparados = new HashMap();
				descripcion = "";
				int numregistros = Integer.parseInt(this.getMapaGruposEtareos("numRegistros").toString());
				for(int i = 0 ; i < numregistros ; i++)
				{
					auxS0 = this.getMapaGruposEtareos("edadinicial_"+i).toString().trim()+"-"+this.getMapaGruposEtareos("edadfinal_"+i).toString().trim()+"-"+this.getMapaGruposEtareos("codigosexo_"+i).toString().trim()+"-"+this.getMapaGruposEtareos("fechainicial_"+i).toString().trim()+"-"+this.getMapaGruposEtareos("fechafinal_"+i).toString().trim();
					for(int j = (numregistros-1) ; j > i ; j--)
					{
						
						auxS1 = this.getMapaGruposEtareos("edadinicial_"+j).toString().trim()+"-"+this.getMapaGruposEtareos("edadfinal_"+j).toString().trim()+"-"+this.getMapaGruposEtareos("codigosexo_"+j).toString().trim()+"-"+this.getMapaGruposEtareos("fechainicial_"+j).toString().trim()+"-"+this.getMapaGruposEtareos("fechafinal_"+j).toString().trim();
						//Se realiza la comparacion compara
						if(auxS0.compareToIgnoreCase(auxS1)==0&&!auxS0.equals("")&&!auxS1.equals("")&&!datosComparados.containsValue(auxS0))
						{
							if(descripcion.equals(""))
								descripcion = (i+1) + "";
							descripcion += "," + (j+1);
							
						}
					}
					
					if(!descripcion.equals(""))
					{
						errores.add("atributos repetidos no validos", new ActionMessage("error.capitacion.gruposEtareos.atributosRepetidos",	descripcion));
					}
					descripcion = "";
					datosComparados.put(i+"",auxS0.toUpperCase());
				}
				
				for(int i = 0 ; i < numeroRegistros ; i++)
				{
					
					if(!UtilidadFecha.validarFechaRango(this.fechaInicial,this.fechaFinal,this.getMapaGruposEtareos("fechainicial_"+i)+"")||!UtilidadFecha.validarFechaRango(this.fechaInicial,this.fechaFinal,this.getMapaGruposEtareos("fechafinal_"+i)+""))
					{
						errores.add("CRUCE DE RANGOS", new ActionMessage("error.capitacion.fechaNoContenidaEnFiltro",(i+1),this.fechaInicial,this.fechaFinal));	
					}
					
					if((UtilidadFecha.conversionFormatoFechaABD(this.getMapaGruposEtareos("fechainicial_"+i)+"")).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.getMapaGruposEtareos("fechafinal_"+i)+""))>0)
					{
						errores.add("fecha de radicación", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Inicial ("+(i+1)+")", "Final ("+(i+1)+")"));
					}
					
					for(int k=0; k< i;k++)
					{
						int ri=Integer.parseInt(this.getMapaGruposEtareos("edadinicial_"+i)+"");
						int rf=Integer.parseInt(this.getMapaGruposEtareos("edadfinal_"+i)+"");
	
						//validacion de los sexos
						if(Integer.parseInt(this.getMapaGruposEtareos("codigosexo_"+i)+"")==ConstantesBD.codigoSexoAmbos||Integer.parseInt(this.getMapaGruposEtareos("codigosexo_"+i)+"")==Integer.parseInt(this.getMapaGruposEtareos("codigosexo_"+k)+""))
						{
							int riT=Integer.parseInt(this.getMapaGruposEtareos("edadinicial_"+k)+"");
							int rfT=Integer.parseInt(this.getMapaGruposEtareos("edadfinal_"+k)+"");
							if((ri<riT&&riT<rf)||(ri<=rfT&&rfT<=rf)||(riT<ri&&rfT>rf)||(ri==riT)||(rf==rfT))
							{
								if(UtilidadFecha.validarFechaRango(this.getMapaGruposEtareos("fechainicial_"+k)+"",this.getMapaGruposEtareos("fechafinal_"+k)+"",this.getMapaGruposEtareos("fechainicial_"+i)+"")||UtilidadFecha.validarFechaRango(this.getMapaGruposEtareos("fechainicial_"+k)+"",this.getMapaGruposEtareos("fechafinal_"+k)+"",this.getMapaGruposEtareos("fechafinal_"+i)+""))
								{
									errores.add("CRUCE DE RANGOS", new ActionMessage("error.pyp.programasPYP.GrupoEtareoCruceRangoerror","En la Posicion "+(k+1),"En la Posicion "+(i+1)));	
								}
							}
						}
					}
				}
			}
		}
		return errores;
	}
	
	
}