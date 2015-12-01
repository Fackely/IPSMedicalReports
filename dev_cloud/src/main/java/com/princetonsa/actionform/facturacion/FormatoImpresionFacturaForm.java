/*
 * @(#)FormatoImpresionFacturaForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.actionform.facturacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadTexto;

/**
 * Forma para manejo presentación de la funcionalidad parametrica de Formato Impresión Factura.
 * 
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 02 /Feb/ 2006
 */
public class FormatoImpresionFacturaForm extends ValidatorForm
{

	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private String estado="";

	/**
	 * Codigo del formato de impresión de presupuesto
	 */
	private int codigoFormato;

	/**
	 * String con el nombre del formato de impresión
	 */
	private String nombreFormato;

	/**
	 * String para la actividad economica de la institucion
	 */
	private String observacionesActividadEconomica;

	/**
	 * String para las observaciones iniciales del encabezado
	 */
	private String observacionesIniciales;

	/**
	 * String con la nota de pie de pagina parametrizada
	 */
	private String piePagina;

	/**
	 * Poscicion del mapa en la consulta de facturas
	 */
	private int posicionMapa;

	/**
	 * Alamancena los formatos existentes
	 */
	private HashMap mapaFormatosPrevios;

	/**
	 * HashMap para almeacenar los datos basicos del formato de impresion del factura
	 */
	private HashMap mapaFormatoBasico;

	/**
	 * HashMap para almacenar los datos de la seccion principal de formato de factura
	 */
	private HashMap mapaSeccionPpal;

	/**
	 * HashMap para almacenar los datos estaticos de la seccion encabezado
	 */
	private HashMap mapaDatosSecEncabezado;

	/**
	 * HashMap para almacenar todos los datos de la seccion encabezado y sus sub secciones
	 */
	private HashMap mapaSeccionEncabezado;

	/**
	 * Mapa para almacenar los datos de la seccion de servicios
	 */
	private HashMap mapaSecServicios;

	/**
	 * Mapa para almacenar los datos de la seccion de articulos
	 */
	private HashMap mapaSecArticulos;

	/**
	 * Mapa para almacenar los datos de la seccion de totales de la factura
	 */
	private HashMap mapaSecTotales;

	/**
	 * HashMap para almacenar los datos de la seccion de pie de pagina
	 */
	private HashMap mapaSecNotaPie;

	/**
	 * Mapa para almacenar los datos de la seccion de firmas de la factura
	 */
	private HashMap mapaFirmas;

	/**
	 * Mapa para los campos de la seccion de encabezado
	 */
	private HashMap mapaCamposSecEncabe;

	/**
	 * Mapa para el detalle de los niveles de la seccion de servicios
	 */
	private HashMap mapaDetNivelServ;

	/**
	 * Mapa para el detalle de los niveles de la seccion de articulos
	 */
	private HashMap mapaDetNivelArt;

	/**
	 * Mapara para los campos de la seccion de totales
	 */
	private HashMap mapaCamposTotales;

	/**
	 * Mapa para los valores de la seccion ppal del formato
	 */
	private HashMap mapaValores;

	/**
	 * String para almacenar los mejsaes de transacciones exitosas
	 */
	private String mensajeExitoso="";

	/**
	 * Entero para la codificacion de servicios
	 */
	private int codServicios;

	/**
	 * Entero para la codificacion de articulos
	 */
	private int codArticulos;

	/**
	 * Entero con el tipo de rompimiento de los servicios
	 */
	private int tipoRompimientoServ;

	/**
	 * Entero con el tipo de rompimiento de los articulos
	 */
	private int tipoRompimientoArt;

	/**
	 * Entero para el valor en letras
	 */
	private int valorEnLetras;

	/**
	 * Entero para el codigo de la firma seleccionada
	 */
	private int firma;

	/**
	 * Reset de toda la forma
	 */
	public void reset()
	{
		this.mapaFormatosPrevios=new HashMap();
		this.mapaFormatoBasico=new HashMap();
		this.mapaSeccionPpal=new HashMap();
		this.mapaSeccionEncabezado=new HashMap();
		this.mapaSecServicios=new HashMap();
		this.mapaSecArticulos=new HashMap();
		this.mapaDatosSecEncabezado=new HashMap();
		this.mapaSecTotales=new HashMap();
		this.mapaSecNotaPie=new HashMap();
		this.mapaFirmas=new HashMap();
		this.mapaCamposSecEncabe=new HashMap();
		this.mapaDetNivelServ=new HashMap();
		this.mapaDetNivelArt=new HashMap();
		this.mapaCamposTotales=new HashMap();
		this.mapaValores=new HashMap();
		this.estado="";
		this.codigoFormato=0;
		this.nombreFormato="";
		this.piePagina="";
		this.posicionMapa=0;
		this.mensajeExitoso="";
		this.observacionesActividadEconomica="";
		this.observacionesIniciales="";
		this.codArticulos=0;
		this.codServicios=0;
		this.firma=0;
		this.valorEnLetras=0;
		this.tipoRompimientoArt=0;
		this.tipoRompimientoServ=0;
	}

	/**
	 * Reset único para los mapas
	 */
	public void resetMapa()
	{
		this.mapaFormatosPrevios=new HashMap();
		this.mapaFormatoBasico=new HashMap();
		this.mapaSeccionPpal=new HashMap();
		this.mapaSeccionEncabezado=new HashMap();
		this.mapaDatosSecEncabezado=new HashMap();
		this.mapaSecServicios=new HashMap();
		this.mapaSecArticulos=new HashMap();
		this.mapaSecTotales=new HashMap();
		this.mapaSecNotaPie=new HashMap();
		this.mapaFirmas=new HashMap();
		this.mapaCamposSecEncabe=new HashMap();
		this.mapaDetNivelServ=new HashMap();
		this.mapaDetNivelArt=new HashMap();
		this.mapaCamposTotales=new HashMap();
		this.mapaValores=new HashMap();
	}
	public void resetFirmas()
	{
		this.mapaFirmas=new HashMap();
	}

	/**
	 * @return Returns the codArticulos.
	 */
	public int getCodArticulos()
	{
		return codArticulos;
	}

	/**
	 * @param codArticulos
	 *            The codArticulos to set.
	 */
	public void setCodArticulos(int codArticulos)
	{
		this.codArticulos=codArticulos;
	}

	/**
	 * @return Returns the codServicios.
	 */
	public int getCodServicios()
	{
		return codServicios;
	}

	/**
	 * @param codServicios
	 *            The codServicios to set.
	 */
	public void setCodServicios(int codServicios)
	{
		this.codServicios=codServicios;
	}

	/**
	 * @return Returns the firma.
	 */
	public int getFirma()
	{
		return firma;
	}

	/**
	 * @param firma
	 *            The firma to set.
	 */
	public void setFirma(int firma)
	{
		this.firma=firma;
	}

	/**
	 * @return Returns the tipoRompimientoArt.
	 */
	public int getTipoRompimientoArt()
	{
		return tipoRompimientoArt;
	}

	/**
	 * @param tipoRompimientoArt
	 *            The tipoRompimientoArt to set.
	 */
	public void setTipoRompimientoArt(int tipoRompimientoArt)
	{
		this.tipoRompimientoArt=tipoRompimientoArt;
	}

	/**
	 * @return Returns the tipoRompimientoServ.
	 */
	public int getTipoRompimientoServ()
	{
		return tipoRompimientoServ;
	}

	/**
	 * @param tipoRompimientoServ
	 *            The tipoRompimientoServ to set.
	 */
	public void setTipoRompimientoServ(int tipoRompimientoServ)
	{
		this.tipoRompimientoServ=tipoRompimientoServ;
	}

	/**
	 * @return Returns the valorEnLetras.
	 */
	public int getValorEnLetras()
	{
		return valorEnLetras;
	}

	/**
	 * @param valorEnLetras
	 *            The valorEnLetras to set.
	 */
	public void setValorEnLetras(int valorEnLetras)
	{
		this.valorEnLetras=valorEnLetras;
	}

	/**
	 * @return Returns the mensajeExitoso.
	 */
	public String getMensajeExitoso()
	{
		return mensajeExitoso;
	}

	/**
	 * @param mensajeExitoso
	 *            The mensajeExitoso to set.
	 */
	public void setMensajeExitoso(String mensajeExitoso)
	{
		this.mensajeExitoso=mensajeExitoso;
	}

	/**
	 * @return Returns the posicionMapa.
	 */
	public int getPosicionMapa()
	{
		return posicionMapa;
	}

	/**
	 * @param posicionMapa
	 *            The posicionMapa to set.
	 */
	public void setPosicionMapa(int posicionMapa)
	{
		this.posicionMapa=posicionMapa;
	}

	/**
	 * @return Retorna el estado.
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * @param estado
	 *            El estado a establecer.
	 */
	public void setEstado(String estado)
	{
		this.estado=estado;
	}

	/**
	 * @return Returns the observacionesActividadEconomica.
	 */
	public String getObservacionesActividadEconomica()
	{
		return observacionesActividadEconomica;
	}

	/**
	 * @param observacionesActividadEconomica
	 *            The observacionesActividadEconomica to set.
	 */
	public void setObservacionesActividadEconomica(String observacionesActividadEconomica)
	{
		this.observacionesActividadEconomica=observacionesActividadEconomica;
	}

	/**
	 * @return Returns the observacionesIniciales.
	 */
	public String getObservacionesIniciales()
	{
		return observacionesIniciales;
	}

	/**
	 * @param observacionesIniciales
	 *            The observacionesIniciales to set.
	 */
	public void setObservacionesIniciales(String observacionesIniciales)
	{
		this.observacionesIniciales=observacionesIniciales;
	}

	/**
	 * @return Returns the codigoFormato.
	 */
	public int getCodigoFormato()
	{
		return codigoFormato;
	}

	/**
	 * @param codigoFormato
	 *            The codigoFormato to set.
	 */
	public void setCodigoFormato(int codigoFormato)
	{
		this.codigoFormato=codigoFormato;
	}

	/**
	 * @return Returns the nombreFormato.
	 */
	public String getNombreFormato()
	{
		return nombreFormato;
	}

	/**
	 * @param nombreFormato
	 *            The nombreFormato to set.
	 */
	public void setNombreFormato(String nombreFormato)
	{
		this.nombreFormato=nombreFormato;
	}

	/**
	 * @return Returns the piePagina.
	 */
	public String getPiePagina()
	{
		return piePagina;
	}

	/**
	 * @param piePagina
	 *            The piePagina to set.
	 */
	public void setPiePagina(String piePagina)
	{
		this.piePagina=piePagina;
	}

	/**
	 * @return Returns the mapaFormatosPrevios.
	 */
	public HashMap getMapaFormatosPrevios()
	{
		return mapaFormatosPrevios;
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setMapaFormatosPrevios(String key, Object value)
	{
		mapaFormatosPrevios.put(key, value);
	}

	/**
	 * @param key
	 * @return
	 */
	public Object getMapaFormatosPrevios(String key)
	{
		return mapaFormatosPrevios.get(key);
	}

	/**
	 * @param mapaFormatosPrevios
	 *            The mapaFormatosPrevios to set.
	 */
	public void setMapaFormatosPrevios(HashMap mapaFormatosPrevios)
	{
		this.mapaFormatosPrevios=mapaFormatosPrevios;
	}

	/**
	 * @return Returns the mapaFormatoBasico.
	 */
	public HashMap getMapaFormatoBasico()
	{
		return mapaFormatoBasico;
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setMapaFormatoBasico(String key, Object value)
	{
		mapaFormatoBasico.put(key, value);
	}

	/**
	 * @param key
	 * @return
	 */
	public Object getMapaFormatoBasico(String key)
	{
		return mapaFormatoBasico.get(key);
	}

	/**
	 * @param mapaFormatoBasico
	 *            The mapaFormatoBasico to set.
	 */
	public void setMapaFormatoBasico(HashMap mapaFormatoBasico)
	{
		this.mapaFormatoBasico=mapaFormatoBasico;
	}

	/**
	 * @return Returns the mapaSeccionPpal.
	 */
	public HashMap getMapaSeccionPpal()
	{
		return mapaSeccionPpal;
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setMapaSeccionPpal(String key, Object value)
	{
		mapaSeccionPpal.put(key, value);
	}

	/**
	 * @param key
	 * @return
	 */
	public Object getMapaSeccionPpal(String key)
	{
		return mapaSeccionPpal.get(key);
	}

	/**
	 * @param mapaSeccionPpal
	 *            The mapaSeccionPpal to set.
	 */
	public void setMapaSeccionPpal(HashMap mapaSeccionPpal)
	{
		this.mapaSeccionPpal=mapaSeccionPpal;
	}

	/**
	 * @return Returns the mapaSeccionEncabezado.
	 */
	public HashMap getMapaSeccionEncabezado()
	{
		return mapaSeccionEncabezado;
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setMapaSeccionEncabezado(String key, Object value)
	{
		mapaSeccionEncabezado.put(key, value);
	}

	/**
	 * @param key
	 * @return
	 */
	public Object getMapaSeccionEncabezado(String key)
	{
		return mapaSeccionEncabezado.get(key);
	}

	/**
	 * @param mapaSeccionEncabezado
	 *            The mapaSeccionEncabezado to set.
	 */
	public void setMapaSeccionEncabezado(HashMap mapaSeccionEncabezado)
	{
		this.mapaSeccionEncabezado=mapaSeccionEncabezado;
	}

	/**
	 * @return Returns the mapaDatosSecEncabezado.
	 */
	public HashMap getMapaDatosSecEncabezado()
	{
		return mapaDatosSecEncabezado;
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setMapaDatosSecEncabezado(String key, Object value)
	{
		mapaDatosSecEncabezado.put(key, value);
	}

	/**
	 * @param key
	 * @return
	 */
	public Object getMapaDatosSecEncabezado(String key)
	{
		return mapaDatosSecEncabezado.get(key);
	}

	/**
	 * @param mapaDatosSecEncabezado
	 *            The mapaDatosSecEncabezado to set.
	 */
	public void setMapaDatosSecEncabezado(HashMap mapaDatosSecEncabezado)
	{
		this.mapaDatosSecEncabezado=mapaDatosSecEncabezado;
	}

	/**
	 * @return Returns the mapaSecServicios.
	 */
	public HashMap getMapaSecServicios()
	{
		return mapaSecServicios;
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setMapaSecServicios(String key, Object value)
	{
		mapaSecServicios.put(key, value);
	}

	/**
	 * @param key
	 * @return
	 */
	public Object getMapaSecServicios(String key)
	{
		return mapaSecServicios.get(key);
	}

	/**
	 * @param mapaSecServicios
	 *            The mapaSecServicios to set.
	 */
	public void setMapaSecServicios(HashMap mapaSecServicios)
	{
		this.mapaSecServicios=mapaSecServicios;
	}

	/**
	 * @return Returns the mapaSecArticulos.
	 */
	public HashMap getMapaSecArticulos()
	{
		return mapaSecArticulos;
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setMapaSecArticulos(String key, Object value)
	{
		mapaSecArticulos.put(key, value);
	}

	/**
	 * @param key
	 * @return
	 */
	public Object getMapaSecArticulos(String key)
	{
		return mapaSecArticulos.get(key);
	}

	/**
	 * @param mapaSecArticulos
	 *            The mapaSecArticulos to set.
	 */
	public void setMapaSecArticulos(HashMap mapaSecArticulos)
	{
		this.mapaSecArticulos=mapaSecArticulos;
	}

	/**
	 * @return Returns the mapaSecTotales.
	 */
	public HashMap getMapaSecTotales()
	{
		return mapaSecTotales;
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setMapaSecTotales(String key, Object value)
	{
		mapaSecTotales.put(key, value);
	}

	/**
	 * @param key
	 * @return
	 */
	public Object getMapaSecTotales(String key)
	{
		return mapaSecTotales.get(key);
	}

	/**
	 * @param mapaSecTotales
	 *            The mapaSecTotales to set.
	 */
	public void setMapaSecTotales(HashMap mapaSecTotales)
	{
		this.mapaSecTotales=mapaSecTotales;
	}

	/**
	 * @return Returns the mapaSecNotaPie.
	 */
	public HashMap getMapaSecNotaPie()
	{
		return mapaSecNotaPie;
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setMapaSecNotaPie(String key, Object value)
	{
		mapaSecNotaPie.put(key, value);
	}

	/**
	 * @param key
	 * @return
	 */
	public Object getMapaSecNotaPie(String key)
	{
		return mapaSecNotaPie.get(key);
	}

	/**
	 * @param mapaSecNotaPie
	 *            The mapaSecNotaPie to set.
	 */
	public void setMapaSecNotaPie(HashMap mapaSecNotaPie)
	{
		this.mapaSecNotaPie=mapaSecNotaPie;
	}

	/**
	 * @return Returns the mapaFirmas.
	 */
	public HashMap getMapaFirmas()
	{
		return mapaFirmas;
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setMapaFirmas(String key, Object value)
	{
		mapaFirmas.put(key, value);
	}

	/**
	 * @param key
	 * @return
	 */
	public Object getMapaFirmas(String key)
	{
		return mapaFirmas.get(key);
	}

	/**
	 * @param mapaFirmas
	 *            The mapaFirmas to set.
	 */
	public void setMapaFirmas(HashMap mapaFirmas)
	{
		this.mapaFirmas=mapaFirmas;
	}

	/**
	 * @return Returns the mapaFirmas.
	 */
	public HashMap getMapaCamposSecEncabe()
	{
		return mapaCamposSecEncabe;
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setMapaCamposSecEncabe(String key, Object value)
	{
		mapaCamposSecEncabe.put(key, value);
	}

	/**
	 * @param key
	 * @return
	 */
	public Object getMapaCamposSecEncabe(String key)
	{
		return mapaCamposSecEncabe.get(key);
	}

	/**
	 * @param mapaFirmas
	 *            The mapaCamposSecEncabe to set.
	 */
	public void setMapaCamposSecEncabe(HashMap mapaCamposSecEncabe)
	{
		this.mapaCamposSecEncabe=mapaCamposSecEncabe;
	}

	/**
	 * @return Returns the mapaDetNivelServ.
	 */
	public HashMap getMapaDetNivelServ()
	{
		return mapaDetNivelServ;
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setMapaDetNivelServ(String key, Object value)
	{
		mapaDetNivelServ.put(key, value);
	}

	/**
	 * @param key
	 * @return
	 */
	public Object getMapaDetNivelServ(String key)
	{
		return mapaDetNivelServ.get(key);
	}

	/**
	 * @param mapaFirmas
	 *            The mapaDetNivelServ to set.
	 */
	public void setMapaDetNivelServ(HashMap mapaDetNivelServ)
	{
		this.mapaDetNivelServ=mapaDetNivelServ;
	}

	/**
	 * @return Returns the mapaDetNivelArt.
	 */
	public HashMap getMapaDetNivelArt()
	{
		return mapaDetNivelArt;
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setMapaDetNivelArt(String key, Object value)
	{
		mapaDetNivelArt.put(key, value);
	}

	/**
	 * @param key
	 * @return
	 */
	public Object getMapaDetNivelArt(String key)
	{
		return mapaDetNivelArt.get(key);
	}

	/**
	 * @param mapaFirmas
	 *            The mapaDetNivelArt to set.
	 */
	public void setMapaDetNivelArt(HashMap mapaDetNivelArt)
	{
		this.mapaDetNivelArt=mapaDetNivelArt;
	}

	/**
	 * @return Returns the mapaCamposTotales.
	 */
	public HashMap getMapaCamposTotales()
	{
		return mapaCamposTotales;
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setMapaCamposTotales(String key, Object value)
	{
		mapaCamposTotales.put(key, value);
	}

	/**
	 * @param key
	 * @return
	 */
	public Object getMapaCamposTotales(String key)
	{
		return mapaCamposTotales.get(key);
	}

	/**
	 * @param mapaFirmas
	 *            The mapaCamposTotales to set.
	 */
	public void setMapaCamposTotales(HashMap mapaCamposTotales)
	{
		this.mapaCamposTotales=mapaCamposTotales;
	}

	/**
	 * @return Returns the mapaValores.
	 */
	public HashMap getMapaValores()
	{
		return mapaValores;
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setMapaValores(String key, Object value)
	{
		mapaValores.put(key, value);
	}

	/**
	 * @param key
	 * @return
	 */
	public Object getMapaValores(String key)
	{
		return mapaValores.get(key);
	}

	/**
	 * @param mapaFirmas
	 *            The mapaValores to set.
	 */
	public void setMapaValores(HashMap mapaValores)
	{
		this.mapaValores=mapaValores;
	}

	/**
	 * Metodo para revisar si hay prioridades duplicadas o no secuenciales
	 * 
	 * @param errores
	 * @param mapaTemporal
	 * @return
	 */
	private ActionErrors verificarPrioridadesDuplicadas(ActionErrors errores, HashMap mapaTemporal, String alias)
	{
		
		//String [] prioridades= new String[4];
		String prioridades[]={"-1","-1","-1"};
		String aux="";
		int tamano=0;
		//Se asignan las prioridades a un vector temporal
		for (int i=1; i < 4; i++)
		{
			aux=mapaTemporal.get(alias + "_" + i) + "";
			if(!aux.equals(""))
			{
				prioridades[tamano]=aux;
			}
			tamano++;
		}

		String cadena = "";
		boolean bandera = false;
		//Se recorre el vector en un ciclo de comparación
		for (int i=0; i < 3; i++)
		{
			if(!prioridades[i].equals("")||!prioridades[i].equals("-1")||!UtilidadTexto.getBoolean(prioridades[i]))
			{
				//int temp = Integer.parseInt(prioridades[i]);
				bandera = false;
				if(prioridades[i]=="2"||prioridades[i]=="3"||prioridades[i]=="4")
				{
					//validacion de la instancia de la prioridad
					if(!cadena.equals("")) 
					{
						String vector[]= cadena.split("-");
						for(int j=0;j<vector.length;j++)
						{
							if((vector[j])==prioridades[i])
								bandera = true;
								
						}
					}
					
					if(bandera)
					{
						errores.add("prioridades sin orden secuencial", new ActionMessage("error.manejoPaciente.formatoImpresionPresupuesto.prioridadesNoSecuenciales"));
					}
					else
					{
						if(!cadena.equals(""))
						{
							cadena+="-";
						}
						cadena+=prioridades[i];
					}
				}
				else
				{
					errores.add("prioridades sin orden secuencial", new ActionMessage("error.manejoPaciente.formatoImpresionPresupuesto.prioridadesNoSecuenciales"));
				}
				
			}
		}
		return errores;
	}

	/**
	 * Función de validación:
	 * 
	 * @param mapping
	 * @param request
	 * @return ActionError que especifica el error
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{

		ActionErrors errores=new ActionErrors();
		/** ****************************************************************************** */
		if(estado.equals("guardarNuevo") || estado.equals("guardarModificacion"))
		{
			if((this.getMapaFormatoBasico("nombreformato").toString()).equals("") || this.getMapaFormatoBasico("nombreformato") == null)
			{
				errores.add("errors.required", new ActionMessage("errors.required", "El Nombre del Formato "));
			}
			/**
			 * Validamos que para las sub secciones de la seccion encabezado la prioridad no sea vacia ni null
			 */
			HashMap mapaTemp=new HashMap();
			boolean existeSeleccion=false;
			for (int i=0; i < Integer.parseInt(this.getMapaSeccionEncabezado("numRegistros").toString()); i++)
			{
				if(i<1)
				{
					for (int j=0; j < Integer.parseInt(this.getMapaCamposSecEncabe("numRegistros_"+i).toString()); j++)
					{
						if(this.getMapaCamposSecEncabe("imprimir_"+i+"_"+j)!=null)
						{
							if(this.getMapaCamposSecEncabe("imprimir_"+i+"_"+j).toString().equals("true"))
							{
								existeSeleccion=true;
							}
						}
					}
				}
				if(i>0)
				{
					existeSeleccion=false;
					for (int j=0; j < Integer.parseInt(this.getMapaCamposSecEncabe("numRegistros_"+i).toString()); j++)
					{
						if(this.getMapaCamposSecEncabe("imprimir_"+i+"_"+j)!=null)
						{
							if(this.getMapaCamposSecEncabe("imprimir_"+i+"_"+j).toString().equals("true"))
							{
								existeSeleccion=true;
							}
						}
					}
				}
				if(i > 0 && existeSeleccion)
				{
					if(this.getMapaSeccionEncabezado("prioridad_" + i).toString().trim().equals("") || this.getMapaSeccionEncabezado("prioridad_" + i) == null)
					{
						errores.add("errors.required", new ActionMessage("errors.required", "La prioridad de la sección " + this.getMapaSeccionEncabezado("descripcion_" + i)));
					}
					else if(this.getMapaSeccionEncabezado("prioridad_" + i).toString().equals("0"))
					{   
						errores.add("errors.integerMayorQue", new ActionMessage("errors.integerMayorQue", "La prioridad de la sección " + this.getMapaSeccionEncabezado("descripcion_" + i), "0"));
					}
				}
			}
			boolean noHayNull=false;
			/** Validamos el orden y el imprmir de la seccion ppal del formato de impresion* */
			mapaTemp=(HashMap)((HashMap)this.getMapaSeccionPpal()).clone();
			for (int i=0; i < Integer.parseInt(mapaTemp.get("numRegistros").toString()); i++)
			{
				if((mapaTemp.get("imprimir_" + i).toString()).equals("true"))
				{
					if(mapaTemp.get("orden_" + i).toString().trim().equals("") || mapaTemp.get("orden_" + i) == null)
					{
						noHayNull=true;
						errores.add("errors.required", new ActionMessage("errors.required", "El orden para " + mapaTemp.get("campo_" + i)));
					}
				}
			}
			if(!noHayNull)
			{
				errores=this.verificarPrioridadesDuplicadas(errores, mapaTemp, "orden");
				noHayNull=false;
			}
			/**
			 * Validamos que no se hayan repetido los tipos de rompimiento de la seccion de articulos
			 */
			mapaTemp=(HashMap)((HashMap)this.getMapaSecArticulos("detallenivel")).clone();
			for (int i=0; i < Integer.parseInt(mapaTemp.get("numRegistros").toString()); i++)
			{
				if(mapaTemp.containsKey("codtiporompimiento_"+i))
				{
					int tiporomp=Integer.parseInt(mapaTemp.get("codtiporompimiento_" + i).toString());
					for (int k=0; k < i; k++)
					{
						if(tiporomp!=-1)
						{
							if(tiporomp == Integer.parseInt(mapaTemp.get("codtiporompimiento_" + k).toString()))
							{
								errores.add("tipoRompimientoRepetidos", new ActionMessage("error.manejoPaciente.formatoImpresionFactura.tipoRompimientoRepetidos", "Artículos"));
							}
						}
					}
				}
			}
			/**
			 * Validamos que el orden de seleccion este hecho por prioridad asi: Calse->Grupo->SubGrupo
			 * para el tipo de rompimiento de la seccion de articulos
			 */
			boolean existeClase=false, existeGrupo=false, existeSubGrupo=false;
			for (int i=0; i < Integer.parseInt(mapaTemp.get("numRegistros").toString()); i++)
			{
				if(mapaTemp.containsKey("codtiporompimiento_"+i))
				{
					int tiporomp=Integer.parseInt(mapaTemp.get("codtiporompimiento_" + i).toString());
					if(tiporomp == ConstantesBD.codigoTipoRompimientoArtClaseInventario)
					{
						existeClase = true;
					}
					if(tiporomp == ConstantesBD.codigoTipoRompimientoArtGrupo && existeClase)
					{
						existeGrupo = true;
					}
					else if(tiporomp == ConstantesBD.codigoTipoRompimientoArtGrupo && !existeClase)
					{
						errores.add("rompimientoArtClaseEsPrioritaria", new ActionMessage("error.manejoPaciente.formatoImpresionFactura.rompimientoArtClaseEsPrioritaria"));
					}
					if(tiporomp == ConstantesBD.codigoTipoRompimientoArtSubGrupo && existeClase && existeGrupo)
					{
						existeSubGrupo = true;
					}
					else if(tiporomp == ConstantesBD.codigoTipoRompimientoArtSubGrupo && !existeClase)
					{
						errores.add("rompimientoArtGrupoPrioritarioSobreSubGrupo", new ActionMessage("error.manejoPaciente.formatoImpresionFactura.rompimientoArtGrupoPrioritarioSobreSubGrupo"));
						
					}
					else if(tiporomp == ConstantesBD.codigoTipoRompimientoArtSubGrupo && !existeGrupo)
					{
						errores.add("rompimientoArtGrupoPrioritarioSobreSubGrupo", new ActionMessage("error.manejoPaciente.formatoImpresionFactura.rompimientoArtGrupoPrioritarioSobreSubGrupo"));
					}
				}
			}
			
			/**
			 * Validamos que no se hayan repetido los tipos de rompimiento de la seccion de servicios
			 */
			mapaTemp=(HashMap)((HashMap)this.getMapaSecServicios("detallenivel")).clone();
			for (int i=0; i < Integer.parseInt(mapaTemp.get("numRegistros").toString()); i++)
			{
				if(mapaTemp.containsKey("codtiporompimiento_"+i))
				{
					int tiporomp=Integer.parseInt(mapaTemp.get("codtiporompimiento_" + i).toString());
					for (int k = 0 ; k < i ; k++)
					{
						int tipoRompInterno=Integer.parseInt(mapaTemp.get("codtiporompimiento_" + k).toString());
						if(tiporomp!=-1)
						{
							if(tiporomp == Integer.parseInt(mapaTemp.get("codtiporompimiento_" + k).toString()))
							{
								errores.add("tipoRompimientoRepetidos", new ActionMessage("error.manejoPaciente.formatoImpresionFactura.tipoRompimientoRepetidos", "Servicios"));
							}
							if(tiporomp==4)
							{
								if(tipoRompInterno==5)
								{
									errores.add("tipoRompimientoExcluyente", new ActionMessage("error.manejoPaciente.formatoImpresionFactura.tipoRompimientoExcluyente"));
								}
							}
							if(tiporomp==5)
							{
								if(tipoRompInterno==4)
								{
									errores.add("tipoRompimientoExcluyente", new ActionMessage("error.manejoPaciente.formatoImpresionFactura.tipoRompimientoExcluyente"));
								}
							}
						}
					}
				}
			}
			int codigoFirma=0;
			if(this.getMapaSecNotaPie("impfirmas").toString().equals("true"))
			{
				
				for (int i = 0 ; i < Integer.parseInt(this.getMapaFirmas("numRegistros").toString()) ; i++)
				{
					if(Integer.parseInt(this.getMapaFirmas("codfirma_"+i).toString())==-1)
					{
						errores.add("errors.required", new ActionMessage("errors.required", "La opción Cuál Firma?"));
					}
					codigoFirma=Integer.parseInt(this.getMapaFirmas("codfirma_"+i).toString());
					for(int k = 0 ; k < i ; k++)
					{
						if(codigoFirma == Integer.parseInt(this.getMapaFirmas("codfirma_"+k).toString()))
						{
							errores.add("firmaRepetida", new ActionMessage("error.manejoPaciente.formatoImpresionFactura.firmaRepetida"));
						}
					}
				}
			}
			
			if(this.getMapaFormatoBasico("codigocodserv").toString().equals("-1"))
			{
				errores.add("errors.required", new ActionMessage("errors.required", "La codificación para imprimir los servicios"));
			}
		}
		
		
		if(estado.equals("guardarNuevo"))
		{
			for (int i=0; i < Integer.parseInt(this.getMapaFormatosPrevios("numRegistros")+""); i++)
			{
				if(this.getMapaFormatoBasico("nombreformato").toString().toLowerCase().equals(this.getMapaFormatosPrevios("nombreformato_"+i).toString().trim().toLowerCase()))
				{
					errores.add("error.manejoPaciente.formatoImpresionPresupuesto.nombreFormatoExistente", new ActionMessage("error.manejoPaciente.formatoImpresionPresupuesto.nombreFormatoExistente",this.getMapaFormatoBasico("nombreformato")));
				}
			}
		}
		
		return errores;
	}
}
