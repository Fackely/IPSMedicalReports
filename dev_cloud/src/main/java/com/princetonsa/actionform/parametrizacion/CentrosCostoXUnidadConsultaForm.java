package com.princetonsa.actionform.parametrizacion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import util.ConstantesBD;
import util.UtilidadTexto;
import util.Utilidades;

public class CentrosCostoXUnidadConsultaForm extends ValidatorForm
{
	/**
	 * Serial vesrsion UID
	 */
	private static final long serialVersionUID = 1L;
	
	// Atributos de la clase
	
	/**
	 * Manejo de estados de la clase
	 */
	private String estado;
	
	/**
	 * Manejo de la unidad de atención
	 */
	private int centroAtencion;
	
	/**
	 * Manejo del código de los centros de atención
	 */
	private String codigoCentroAtencion;
	
	/**
	 * Mapa para el manejo de la relacion
	 * de centros de costo y unidades de consulta
	 */
	private HashMap mapa;
	
	/**
	 * Unidades de consulta
	 */
	private Collection listadoUnidadesConsulta;

	/**
	 * Centros de costo
	 */
	private Collection listadoCentrosCosto;
	
	/**
	 * Unidades de atención
	 */
	private Collection listadoCentrosAtencion;
	
	/**
	 * Indice para eliminar un registro
	 */
	private int indiceEliminado;
	
	/**
	 * Indice para modificar un registro
	 */
	private int indiceModificado;
	
	/**
	 * Manejo ordenamiento
	 */
	private String ultimaPropiedad;
	
	/**
	 * Manejo ordenamiento
	 */
	private String propiedad;
	
	/**
	 * Activar el Boton Guardar
	 */
	private String activoGuardar;
	
	
	//----------------------------------
	//Cambio Funcionalidades Consulta Ext --Anexo 810
	private ArrayList<HashMap<String,Object>> centrosCostosUniAgen;
	private String indexCodUniAgen;
	//----------------------------------

	// Validación de datos
	
	/**
	 * Metodo para validar los datos
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errores=new ActionErrors();
		if(estado.equalsIgnoreCase("nuevo"))
		{
			int numRegistros=Utilidades.convertirAEntero(mapa.get("numRegistros")+"");
			
				Vector elementos=new Vector();
				/*
				 * Reposicionamiento de los valores en el mapa
				 */
				HashMap mapaAux = new HashMap();
				for(int j=0;j<numRegistros;j++)
				{
					mapaAux.put("unidad_consulta_"+(j+1), mapa.get("unidad_consulta_"+j));
					mapaAux.put("centro_costo_"+(j+1), mapa.get("centro_costo_"+j));
				}	
				int unidadConsulta=Utilidades.convertirAEntero(mapa.get("unidad_consulta_"+numRegistros)+"");
				int centroCosto=Utilidades.convertirAEntero(mapa.get("centro_costo_"+numRegistros)+"");
				
				if(unidadConsulta==0)
				{
					errores.add("UC requerido", new ActionMessage("errors.required", "La unidad de Agenda"));
				}
				if(centroCosto==0)
				{
					errores.add("CC requerido", new ActionMessage("errors.required", "El centro de Costo"));
				}
				
				if (errores.isEmpty())
				{
				
					mapaAux.put("unidad_consulta_0", unidadConsulta);
					mapaAux.put("centro_costo_0", centroCosto);
					this.setMapa(mapaAux);
				}
				/*
				 * Fin Reposicionamiento de los valores en el mapa
				 */
				
				
				if(errores.isEmpty())
				{
					for(int i=0; i<=numRegistros; i++)
					{
						if(!UtilidadTexto.getBoolean(mapa.get("modificar_activado_"+i)+""))
						{
						
							unidadConsulta=Utilidades.convertirAEntero(mapa.get("unidad_consulta_"+i)+"");
							centroCosto=Utilidades.convertirAEntero(mapa.get("centro_costo_"+i)+"");
							Vector elemento=new Vector();
							elemento.add(unidadConsulta);
							elemento.add(centroCosto);
							if(elementos.contains(elemento))
							{
								errores.add("Elemento Repetido", new ActionMessage("errors.yaExiste", "El registro"));
								break;
							}
							else
							{
								elementos.add(elemento);
							}
						}
					}
					if(errores.isEmpty())
					{
						mapa.put("modificar_activado_"+numRegistros,ConstantesBD.acronimoNo);
						mapa.put("numRegistros", (numRegistros+1));
					}
					else
					{
						mapa.put("numRegistros", numRegistros);
					}
				}
				else
				{
					mapa.put("numRegistros", numRegistros);
				}
				Utilidades.imprimirMapa(mapaAux);
				Utilidades.imprimirMapa(mapa);
				
		}
		else if(estado.equalsIgnoreCase("guardar"))
		{
			int numRegistros=Utilidades.convertirAEntero(mapa.get("numRegistros")+"");
			ArrayList<Vector> elementos=new ArrayList<Vector>();
			ArrayList<Vector> elementosRepetidos = new ArrayList<Vector>();
			int unidadConsulta=0;
			int centroCosto=0;
			for(int i=0; i<=numRegistros; i++)
			{
				unidadConsulta=Utilidades.convertirAEntero(mapa.get("unidad_consulta_"+i)+"");
				centroCosto=Utilidades.convertirAEntero(mapa.get("centro_costo_"+i)+"");
				Vector elemento=new Vector();
				elemento.add(unidadConsulta);
				elemento.add(centroCosto);
				
				if(elementos.contains(elemento))
				{
					if(!elementosRepetidos.contains(elemento))
						elementosRepetidos.add(elemento);
				}
				else
				{
					elementos.add(elemento);
				}
			}
			
			for(Vector elemento:elementosRepetidos)
			{
				unidadConsulta = Utilidades.convertirAEntero(elemento.get(0)+"");
				centroCosto = Utilidades.convertirAEntero(elemento.get(1)+"");
				String nombreCentroCosto = "";
				String nombreUnidadConsulta = "";
				
				//************se busca nombre de la unidad de consulta***********************
				Iterator iterador = this.listadoUnidadesConsulta.iterator();
				while(iterador.hasNext())
				{
					HashMap fila = (HashMap)iterador.next();
					if(fila.get("codigo").toString().equals(unidadConsulta+""))
						nombreUnidadConsulta = fila.get("nombre").toString(); 
				}
				//********************************************************************
				//*************se busca nombre de centro de costo**************************
				iterador = this.listadoCentrosCosto.iterator();
				while(iterador.hasNext())
				{
					HashMap fila = (HashMap)iterador.next();
					if(fila.get("codigo").toString().equals(centroCosto+""))
						nombreCentroCosto = fila.get("codigo_nombre").toString(); 
				}
				//***************************************************************************
				
				errores.add("", new ActionMessage("error.capitacion.yaExisteCodigo","registro de unidad agenda "+nombreUnidadConsulta+" y centro de costo "+nombreCentroCosto));
			}
		}
		return errores;
	}
	
	// Limpiador de la clase
	
	public void reset()
	{
		this.centroAtencion=0;
		this.mapa=new HashMap();
		this.mapa.put("numRegistros", 0);
		this.listadoUnidadesConsulta=new ArrayList();
		this.listadoCentrosCosto=new ArrayList();
		this.listadoCentrosAtencion=new ArrayList();
		this.indiceEliminado=0;
		this.indiceModificado=0;
		this.propiedad="";
		this.ultimaPropiedad="";
		this.activoGuardar=ConstantesBD.acronimoNo;
		
		// Cambios Anexo 810
		this.centrosCostosUniAgen = new ArrayList<HashMap<String,Object>>();
		this.indexCodUniAgen = "";
	}
	
	// Getters y Setters
	
	/**
	 * @return the indiceModificado
	 */
	public int getIndiceModificado() {
		return indiceModificado;
	}

	/**
	 * @param indiceModificado the indiceModificado to set
	 */
	public void setIndiceModificado(int indiceModificado) {
		this.indiceModificado = indiceModificado;
	}

	/**
	 * @return Retorna estado.
	 */
	public String getEstado()
	{
		return estado;
	}
	/**
	 * @param estado Asigna estado.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}
	/**
	 * @return Retorna mapa.
	 */
	public HashMap getMapa()
	{
		return mapa;
	}
	/**
	 * @param mapa Asigna mapa.
	 */
	public void setMapa(HashMap mapa)
	{
		this.mapa = mapa;
	}
	
	/**
	 * @return Retorna mapa.
	 */
	public Object getMapa(String key)
	{
		return mapa.get(key);
	}
	/**
	 * @param mapa Asigna mapa.
	 */
	public void setMapa(String key,Object obj)
	{
		this.mapa.put(key,obj);
	}
	/**
	 * @return Retorna centroAtencion.
	 */
	public int getCentroAtencion()
	{
		return centroAtencion;
	}
	/**
	 * @param centroAtencion Asigna centroAtencion.
	 */
	public void setCentroAtencion(int unidadAtencion)
	{
		this.centroAtencion = unidadAtencion;
	}

	/**
	 * @return Retorna listadoCentrosCosto.
	 */
	public Collection getListadoCentrosCosto()
	{
		return listadoCentrosCosto;
	}

	/**
	 * @param listadoCentrosCosto Asigna listadoCentrosCosto.
	 */
	public void setListadoCentrosCosto(Collection listadoCentrosCosto)
	{
		this.listadoCentrosCosto = listadoCentrosCosto;
	}

	/**
	 * @return Retorna listadoCentrosAtencion.
	 */
	public Collection getListadoCentrosAtencion()
	{
		return listadoCentrosAtencion;
	}

	/**
	 * @param listadoCentrosAtencion Asigna listadoCentrosAtencion.
	 */
	public void setListadoCentrosAtencion(Collection listadoUnidadesAtencion)
	{
		this.listadoCentrosAtencion = listadoUnidadesAtencion;
	}

	/**
	 * @return Retorna listadoUnidadesConsulta.
	 */
	public Collection getListadoUnidadesConsulta()
	{
		return listadoUnidadesConsulta;
	}

	/**
	 * @param listadoUnidadesConsulta Asigna listadoUnidadesConsulta.
	 */
	public void setListadoUnidadesConsulta(Collection listadoUnidadesConsulta)
	{
		this.listadoUnidadesConsulta = listadoUnidadesConsulta;
	}

	/**
	 * @return Retorna codigoCentroAtencion.
	 */
	public String getCodigoCentroAtencion()
	{
		return codigoCentroAtencion;
	}

	/**
	 * @param codigoCentroAtencion Asigna codigoCentroAtencion.
	 */
	public void setCodigoCentroAtencion(String codigoCentroAtencion)
	{
		this.codigoCentroAtencion = codigoCentroAtencion;
	}

	/**
	 * @return Retorna indiceEliminado.
	 */
	public int getIndiceEliminado()
	{
		return indiceEliminado;
	}

	/**
	 * @param indiceEliminado Asigna indiceEliminado.
	 */
	public void setIndiceEliminado(int indiceEliminado)
	{
		this.indiceEliminado = indiceEliminado;
	}

	/**
	 * @return Retorna propiedad.
	 */
	public String getPropiedad()
	{
		return propiedad;
	}

	/**
	 * @param propiedad Asigna propiedad.
	 */
	public void setPropiedad(String propiedad)
	{
		this.propiedad = propiedad;
	}

	/**
	 * @return Retorna ultimaPropiedad.
	 */
	public String getUltimaPropiedad()
	{
		return ultimaPropiedad;
	}

	/**
	 * @param ultimaPropiedad Asigna ultimaPropiedad.
	 */
	public void setUltimaPropiedad(String ultimaPropiedad)
	{
		this.ultimaPropiedad = ultimaPropiedad;
	}

	/**
	 * @return the centrosCostosUniAgen
	 */
	public ArrayList<HashMap<String, Object>> getCentrosCostosUniAgen() {
		return centrosCostosUniAgen;
	}

	/**
	 * @param centrosCostosUniAgen the centrosCostosUniAgen to set
	 */
	public void setCentrosCostosUniAgen(
			ArrayList<HashMap<String, Object>> centrosCostosUniAgen) {
		this.centrosCostosUniAgen = centrosCostosUniAgen;
	}

	/**
	 * @return the indexCodUniAgen
	 */
	public String getIndexCodUniAgen() {
		return indexCodUniAgen;
	}

	/**
	 * @param indexCodUniAgen the indexCodUniAgen to set
	 */
	public void setIndexCodUniAgen(String indexCodUniAgen) {
		this.indexCodUniAgen = indexCodUniAgen;
	}

	/**
	 * @return the activoGuardar
	 */
	public String getActivoGuardar() {
		return activoGuardar;
	}

	/**
	 * @param activoGuardar the activoGuardar to set
	 */
	public void setActivoGuardar(String activoGuardar) {
		this.activoGuardar = activoGuardar;
	}
	
	

}
