/*
 * @(#)CentrosCostoForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.actionform;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.Utilidades;


/**
 * Form que contiene todos los datos específicos para generar 
 * el Registro de los Centros de Costo
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. May 06, 2005
 * @author cperalta 
 */
public class CentrosCostoForm extends ValidatorForm 
{
	/**
	 * Mapa con los centros de costo
	 */
	private HashMap mapaCentrosCosto;
	
	/**
	 * Mapa con los centros de costo  que se obtuvo de la BD,
	 * el cual no ha tenido modificación alguna
	 */
	private HashMap mapaCentrosCostoNoModificado;
	
	/**
	 * Mapa con todos los centros de costo del sistema
	 */
	private HashMap mapaCentrosCostoGeneral;
	
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * Este campo contiene el pageUrl para controlar el pager,
	 *  y conservar los valores del hashMap mediante un submit de
	 * JavaScript. (Integra pager -Valor Captura)
	 */
	private String linkSiguiente="";

	/**
	 * Offset del HashMap
	 */
	private int offsetHash;
	
	/**
	 * Numero real de filas del mapa
	 */
	private int numeroRealFilasMapa;
	
	/**
	 * Elimina un elemento del Hash Map
	 * Dada su llave;
	 */
	private String keyDelete;
	
	/**
	 * Offset para el pager 
	 */
	private int offset=0;
	
	/**
	 * Codigo del centro de atencion
	 */
	private int codCentroAtencion;
	
	/**
	 * Posicion del mapa
	 */
	private int posicionMapa;
	
	/**
	 * String con mensajes opcionales
	 */
	private String mensaje="";
	
	
	private String patronOrdenar;
	
	
	private String ultimoPatron;
	
	private int institucion;
	
	private String tipoEntidad;
	/**
	 * resetea los valores de la forma
	 * @param i 
	 *
	 */
	public void reset(int i)
	{
	    this.linkSiguiente = "";
	    this.mapaCentrosCosto = new HashMap();
	    this.mapaCentrosCostoNoModificado = new HashMap();
	    this.mapaCentrosCostoGeneral = new HashMap();
	    this.offsetHash = 0;
	    this.numeroRealFilasMapa = 0;
	    this.keyDelete = "";
	    this.codCentroAtencion = 0;
	    this.posicionMapa = 0;
	    this.patronOrdenar ="";
	    this.ultimoPatron = "";
	    this.institucion=i;
	    this.tipoEntidad="";
	}
	
	public void resetMensaje()
	{
		this.mensaje = "";
	}
	
	public void resetMapa()
	{
	    this.mapaCentrosCosto = new HashMap();
	    this.mapaCentrosCostoNoModificado = new HashMap();
	    this.mapaCentrosCostoGeneral = new HashMap();
	} 

	
	
	public String getTipoEntidad() {
		return tipoEntidad;
	}

	public void setTipoEntidad(String tipoEntidad) {
		this.tipoEntidad = tipoEntidad;
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
	 * @return Returns the codCentroAtencion.
	 */
	public int getCodCentroAtencion()
	{
		return codCentroAtencion;
	}

	/**
	 * @param codCentroAtencion The codCentroAtencion to set.
	 */
	public void setCodCentroAtencion(int codCentroAtencion)
	{
		this.codCentroAtencion=codCentroAtencion;
	}

	/**
	 * @return Returns the mapaCentrosCosto.
	 */
	public HashMap getMapaCentrosCosto()
	{
		return mapaCentrosCosto;
	}
	
	/**
	 * @param mapaCentrosCosto The mapaCentrosCosto to set.
	 */
	public void setMapaCentrosCosto(HashMap mapaCentrosCosto)
	{
		this.mapaCentrosCosto= mapaCentrosCosto;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaCentrosCosto(String key, Object value) 
	{
		mapaCentrosCosto.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaCentrosCosto(String key) 
	{
		return mapaCentrosCosto.get(key);
	}
	
	/**
	 * @return Returns the mapaCentrosCosto.
	 */
	public HashMap getMapaCentrosCostoNoModificado()
	{
		return mapaCentrosCostoNoModificado;
	}
	
	/**
	 * @param mapaCentrosCosto The mapaCentrosCostoNoModificado to set.
	 */
	public void setMapaCentrosCostoNoModificado(HashMap mapaCentrosCostoNoModificado)
	{
		this.mapaCentrosCostoNoModificado= mapaCentrosCostoNoModificado;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaCentrosCostoNoModificado(String key, Object value) 
	{
		mapaCentrosCostoNoModificado.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaCentrosCostoNoModificado(String key) 
	{
		return mapaCentrosCostoNoModificado.get(key);
	}
	
	/**
	 * @return Returns the mapaCentrosCostoGeneral
	 */
	public HashMap getMapaCentrosCostoGeneral()
	{
		return mapaCentrosCostoGeneral;
	}
	
	/**
	 * @param mapaCentrosCostoGeneral The mapaCentrosCostoGeneral to set.
	 */
	public void setMapaCentrosCostoGeneral(HashMap mapaCentrosCostoGeneral)
	{
		this.mapaCentrosCostoGeneral = mapaCentrosCostoGeneral;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaCentrosCostoGeneral(String key, Object value) 
	{
		mapaCentrosCostoGeneral.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaCentrosCostoGeneral(String key) 
	{
		return mapaCentrosCostoGeneral.get(key);
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
	 * @return Returns the keyDelete.
	 */
	public String getKeyDelete()
	{
		return keyDelete;
	}

	/**
	 * @param keyDelete The keyDelete to set.
	 */
	public void setKeyDelete(String keyDelete)
	{
		this.keyDelete=keyDelete;
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
	 * @return Returns the numeroRealFilasMapa.
	 */
	public int getNumeroRealFilasMapa()
	{
		return numeroRealFilasMapa;
	}

	/**
	 * @param numeroRealFilasMapa The numeroRealFilasMapa to set.
	 */
	public void setNumeroRealFilasMapa(int numeroRealFilasMapa)
	{
		this.numeroRealFilasMapa=numeroRealFilasMapa;
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
	 * @return Returns the offsetHash.
	 */
	public int getOffsetHash()
	{
		return offsetHash;
	}

	/**
	 * @param offsetHash The offsetHash to set.
	 */
	public void setOffsetHash(int offsetHash)
	{
		this.offsetHash=offsetHash;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * 
	 * @param institucion
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
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
		    String tempoDesc = "";
		    String tempoCodigo = "" ;
		    for(int k = 0 ; k < Integer.parseInt(this.getMapaCentrosCosto("numRegistros").toString()) ; k++)
		    {
		    	tempoCodigo = this.getMapaCentrosCosto("identificador_"+k)+"";
		        if(tempoCodigo.trim().equals("") || tempoCodigo.equals("null") || tempoCodigo==null)
		        {
		            errores.add("Campo Codigo vacio", new ActionMessage("errors.required"," El Código del Centro de Costo "));
		        }
		    	//Validamos la descripcion del centro de costo
		        tempoDesc = this.getMapaCentrosCosto("descripcion_"+k)+"";
		        if(tempoDesc.trim().equals("") || tempoDesc.equals("null") || tempoDesc==null)
		        {
		            errores.add("Campo Descripción vacio", new ActionMessage("errors.required"," La Descripción del Centro de Costo "));
		        }
		        
		        //Validamos el tipo de area seleccionado para el centro de costo
		        if(Integer.parseInt(this.getMapaCentrosCosto("codigotipoarea_"+k).toString()) == -1)
		        {
		        	errores.add("Campo Descripción vacio", new ActionMessage("errors.required"," El Tipo de Area del Centro de Costo "));
		        }
		        
        		//Validamos el tipo de area seleccionado para el centro de costo
		        if(this.getMapaCentrosCosto("unidadfuncional_"+k).toString().equals("-1"))
		        {
		        	errores.add("Campo Descripción vacio", new ActionMessage("errors.required"," La Unidad Funcional del Centro de Costo "));
		        }
		    }
		    
		    //Comparamos si existe la el mismo Identificador del centro de costo dento
		    //de un mismo centro de atencion
			String auxS1 = "";
			String auxS0 = "";
			HashMap codigosComparados = new HashMap();
			String descripcion = "";
			int numregistros = Integer.parseInt(this.getMapaCentrosCosto("numRegistros").toString());
			for(int i = 0 ; i < numregistros ; i++)
			{
				auxS0 = this.getMapaCentrosCosto("identificador_"+i).toString().trim();
				for(int j = (numregistros-1) ; j > i ; j--)
				{
					auxS1 = this.getMapaCentrosCosto("identificador_"+j).toString().trim();
					//Se realiza la comparacion 
					if(auxS0.compareToIgnoreCase(auxS1)==0&&!auxS0.equals("")&&!auxS1.equals("")&&!codigosComparados.containsValue(auxS0))
					{
						if(descripcion.equals(""))
							descripcion = (i+1) + "";
						descripcion += "," + (j+1);
						
					}
				}
				
				if(!descripcion.equals(""))
				{
					
					errores.add("error.centrosCosto.centroCostoIgualIdentificador", new ActionMessage("error.centrosCosto.centroCostoIgualIdentificador",	descripcion));
				}
				descripcion = "";
				codigosComparados.put(i+"",auxS0.toUpperCase());
			}
			
			
			 //Comparamos si existe la el mismo Identificador del centro de costo para una institucion
			descripcion = "";
			int numRegistrosCA = Integer.parseInt(this.getMapaCentrosCosto("numRegistros").toString());
			int numRegistrosGeneral = Integer.parseInt(this.getMapaCentrosCostoGeneral("numRegistros").toString());
			for(int k = 0 ; k < numRegistrosCA ; k++)
			{
				descripcion = this.getMapaCentrosCosto("identificador_"+k)+"";
				if((this.getMapaCentrosCosto("existebd_"+k)+"").equals("no"))
				{
					for(int l = 0 ; l < numRegistrosGeneral ; l++)
					{
						if(descripcion.equals(this.getMapaCentrosCostoGeneral("identificador_"+l)+"".trim()))
						{
							
							errores.add("error.centrosCosto.identificadorExistenteParaInstitucion", new ActionMessage("error.centrosCosto.identificadorExistenteParaInstitucion", descripcion, this.getMapaCentrosCostoGeneral("descripcion_"+l)+""));
							//Rompemos con el cilo ed COmparacion si encontramos este error
							//para no repetirlo n veces
							l = numRegistrosGeneral;
							k = numRegistrosCA;
						}
					}
				}
			}
		    
		    
		}
		return errores;
	}
	
	
}