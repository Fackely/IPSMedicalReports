/*
 * Creado el Aug 8, 2006
 * por Julian Montoya
 */
package com.princetonsa.actionform.pyp;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.Utilidades;


public class ProgramaArticuloPypForm extends ActionForm  {
	
	
	private static final long serialVersionUID = 1L;

	/**
	 * variable para manejar el flujo de la funcionalidad.  
	 */	
	private String estado;

	/**
	 * Variable para establecer que programa se esta seleccionando
	 */	
	private String programa;
	
	/**
	 * 
	 */
	private String actividad;

	/**
	 * Variable para establecer el nombre del  programa se esta seleccionando
	 */	
	private String nombrePrograma;
	
	/**
	 * Variable para saber que articulo se va a eliminar 
	 */
	
	private int nroArticuloEliminado;

	/**
	 * Variable para saber el indice dentro del mapa del Articulo a eliminar 
	 */
	
	private int indiceEliminado;

	
	/**
	 * Mapa Para Manejar El Ingreso y la Consulta de la Funcionalidad.
	 */
	private HashMap mapa;


	/**
	 * Variable para almacenar el lik, cuando se redirecciona por el pàginador. 
	 */
	private String linkSiguiente;
	
	/**
	 * indice del registro seleccionado
	 */
	private String index;


	/**
	 * 
	 */
	private HashMap viasIngreso;
	

	/**
	 * 
	 */
	private HashMap viasIngresoEliminados;

    /**
     * 
     */
    private String regimenGrupoEtareo;
    
    
    /**
     * 
     */
    private HashMap gruposEtareos;
    
    
    /**
     * 
     */
    private HashMap gruposEtareosEliminados;
    
    /**
	 * 
	 */
	private int regEliminar;
	
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
		if(estado.equals("guardarViaIngreso"))
		{
			for(int k=0;k<Integer.parseInt(this.getViasIngreso("numRegistros")+"");k++)
		    {
				if(((this.getViasIngreso("viaingreso_"+k)+"").trim()).equals(""))  
			      {
			          errores.add("VIA INGRESO REQUERIDA", new ActionMessage("errors.required","La via de Ingreso para el registro "+(k+1)));  
			      }
				if(((this.getViasIngreso("ocupacion_"+k)+"").trim()).equals("-1")) 
			      {
			          errores.add("OCUPACION REQUERIDO", new ActionMessage("errors.required","La Ocupacion para el registro "+(k+1)));  
			      }
				String viaIng=this.getViasIngreso("viaingreso_"+k)+"";
				String ocup=this.getViasIngreso("ocupacion_"+k)+"";
				
				for(int l=0;l<k;l++)
				{
					if((viaIng.equals(this.getViasIngreso("viaingreso_"+l)+"") && !(this.getViasIngreso("viaingreso_"+k)+"").equals(""))&&(ocup.equals(this.getViasIngreso("ocupacion_"+l)+"") && !(this.getViasIngreso("ocupacion_"+k)+"").equals("")))  
		             {		                  		                  
		                  errores.add("YA EXISTE EL REGISTRO.", new ActionMessage("errors.yaExiste","La via Ingreso "+viaIng+" y la Ocupacion "+ocup));                 
		             }
				}
		    }		
			
		}
		
		if(estado.equals("guardarGrupoEtareo"))
		{
			for(int k=0;k<Integer.parseInt(this.getGruposEtareos("numRegistros")+"");k++)
		    {
				if(((this.getGruposEtareos("grupoetareo_"+k)+"").trim()).equals(""))  
			      {
			          errores.add("Grupo Etareo REQUERIDA", new ActionMessage("errors.required","El Grupo Etareo para el registro "+(k+1)));  
			      }
				if(!((this.getGruposEtareos("frecuencia_"+k)+"").trim()).equals("")&&((this.getGruposEtareos("tipofrecuencia_"+k)+"").trim()).equals(""))  
			      {
			          errores.add("TIPO REQUERIDA", new ActionMessage("errors.required","El tipo de Frecuencia para el registro "+(k+1)));  
			      }
				String grEtareo=this.getGruposEtareos("grupoetareo_"+k)+"";
				String[] rangoGE=Utilidades.obtenerRango_UnidadMedidaGrupoEtareoPYP(grEtareo);
				String sexoGE=Utilidades.obtenerSexoGrupoEtareoPYP(grEtareo);
				int ri=0,rf=0,riT=0,rfT=0;
				if(rangoGE[2].equals(ConstantesBD.codigoUnidadMedidaFechaAnios+""))
				{
					ri=(Integer.parseInt(rangoGE[0])*12)*30;
					rf=(Integer.parseInt(rangoGE[1])*12)*30;
				}
				else if(rangoGE[2].equals(ConstantesBD.codigoUnidadMedidaFechaMeses+""))
				{
					ri=Integer.parseInt(rangoGE[0])*30;
					rf=Integer.parseInt(rangoGE[1])*30;
				}
				else
				{
					ri=Integer.parseInt(rangoGE[0]);
					rf=Integer.parseInt(rangoGE[1]);
				}
				//en este punto ya tenemos el rango en dias.
				for(int l=0;l<k;l++)
				{
					if(grEtareo.equals(this.getGruposEtareos("grupoetareo_"+l)+"") && !(this.getGruposEtareos("grupoetareo_"+k)+"").equals(""))  
		             {		                  		                  
		                  errores.add("YA EXISTE EL REGISTRO.", new ActionMessage("errors.yaExiste","El Grupo Etareo "+grEtareo));                 
		             }
					String rangoTemp[]=Utilidades.obtenerRango_UnidadMedidaGrupoEtareoPYP(this.getGruposEtareos("grupoetareo_"+l)+"");
					if(rangoTemp[2].equals(ConstantesBD.codigoUnidadMedidaFechaAnios+""))
					{
						riT=(Integer.parseInt(rangoTemp[0])*12)*30;
						rfT=(Integer.parseInt(rangoTemp[1])*12)*30;
					}
					else if(rangoTemp[2].equals(ConstantesBD.codigoUnidadMedidaFechaMeses+""))
					{
						riT=Integer.parseInt(rangoTemp[0])*30;
						rfT=Integer.parseInt(rangoTemp[1])*30;
					}
					else
					{
						riT=Integer.parseInt(rangoTemp[0]);
						rfT=Integer.parseInt(rangoTemp[1]);
					}
					String sexTemp=Utilidades.obtenerSexoGrupoEtareoPYP(this.getGruposEtareos("grupoetareo_"+l)+"");
					if(Integer.parseInt(sexoGE)==ConstantesBD.codigoSexoAmbos||Integer.parseInt(sexTemp)==ConstantesBD.codigoSexoAmbos||Integer.parseInt(sexoGE)==Integer.parseInt(sexTemp))
					{
						if((ri<riT&&riT<rf)||(ri<=rfT&&rfT<=rf)||(riT<ri&&rfT>rf)||(ri==riT)||(rf==rfT))
						{
							errores.add("CRUCE DE RANGOS", new ActionMessage("error.pyp.programasPYP.GrupoEtareoCruceRangoerror","En la Posicion "+(k+1),"En la Posicion "+(l+1)));
						}
					}
				}
		    }		
			
		}
		
		if(!errores.isEmpty())
		{
			this.estado = "";
		
		}
		return errores;
	}
	
	/**
	 * Resetear la Forma.
	 */
	
	public void reset()
	{
		this.programa  = "";
		this.actividad="";
		this.nombrePrograma  = "";
		this.nroArticuloEliminado = 0;
		this.mapa = new HashMap();
		this.setMapa("nroRegistrosNv","0");
		this.index="";
		this.viasIngreso=new HashMap();
		this.viasIngreso.put("numRegistros","0");
		this.viasIngresoEliminados=new HashMap();
		this.viasIngresoEliminados.put("numRegistros","0");
		this.regEliminar=ConstantesBD.codigoNuncaValido;
		this.regimenGrupoEtareo="";
		this.gruposEtareos=new HashMap();
		this.gruposEtareos.put("numRegistros","0");
		this.gruposEtareosEliminados=new HashMap();
		this.gruposEtareosEliminados.put("numRegistros","0");

	}
	
	/**
	 * @return Retorna mapaCargue.
	 */
	public HashMap getMapa() {
		return mapa;
	}


	/**
	 * @param Asigna mapaCargue.
	 */
	public void setMapa(HashMap mapa) {
		this.mapa = mapa;
	}

	/**
	 * @return Returns the mapa.
	 */
	public Object getMapa(String key) {
		return this.mapa.get(key);
	}

	/**
	 * @param Colocar un elemento en el mapa con un key Especifico.
	 */
	public void setMapa(String key, String valor) 
	{
		this.mapa.put(key, valor); 
	}

	/**
	 * @return Retorna estado.
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param Asigna estado.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * @return Retorna programa.
	 */
	public String getPrograma() {
		return programa;
	}

	/**
	 * @param Asigna programa.
	 */
	public void setPrograma(String programa) {
		this.programa = programa;
	}

	/**
	 * @return Retorna nroArticuloEliminado.
	 */
	public int getNroArticuloEliminado() {
		return nroArticuloEliminado;
	}

	/**
	 * @param Asigna nroArticuloEliminado.
	 */
	public void setNroArticuloEliminado(int nroArticuloEliminado) {
		this.nroArticuloEliminado = nroArticuloEliminado;
	}

	/**
	 * @return Retorna nombrePrograma.
	 */
	public String getNombrePrograma() {
		return nombrePrograma;
	}

	/**
	 * @param Asigna nombrePrograma.
	 */
	public void setNombrePrograma(String nombrePrograma) {
		this.nombrePrograma = nombrePrograma;
	}

	/**
	 * @return Retorna indiceEliminado.
	 */
	public int getIndiceEliminado() {
		return indiceEliminado;
	}

	/**
	 * @param Asigna indiceEliminado.
	 */
	public void setIndiceEliminado(int indiceEliminado) {
		this.indiceEliminado = indiceEliminado;
	}

	/**
	 * @return Retorna linkSiguiente.
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * @param Asigna linkSiguiente.
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	public String getIndex()
	{
		return index;
	}

	public void setIndex(String index)
	{
		this.index = index;
	}
	public HashMap getViasIngreso() {
		return viasIngreso;
	}

	public void setViasIngreso(HashMap viasIngreso) {
		this.viasIngreso = viasIngreso;
	}
	public Object getViasIngreso(String key) {
		return viasIngreso.get(key);
	}

	public void setViasIngreso(String key,Object value) {
		this.viasIngreso.put(key,value);
	}

	public int getRegEliminar() {
		return regEliminar;
	}

	public void setRegEliminar(int regEliminar) {
		this.regEliminar = regEliminar;
	}

	public HashMap getViasIngresoEliminados() {
		return viasIngresoEliminados;
	}

	public void setViasIngresoEliminados(HashMap viasIngresoEliminados) {
		this.viasIngresoEliminados = viasIngresoEliminados;
	}
	

	public Object getViasIngresoEliminados(String key) {
		return viasIngresoEliminados.get(key);
	}

	public void setViasIngresoEliminados(String key,Object value) {
		this.viasIngresoEliminados.put(key,value);
	}

	public HashMap getGruposEtareos() {
		return gruposEtareos;
	}

	public void setGruposEtareos(HashMap gruposEtareos) {
		this.gruposEtareos = gruposEtareos;
	}


	public Object getGruposEtareos(String key) {
		return gruposEtareos.get(key);
	}

	public void setGruposEtareos(String key,Object value) {
		this.gruposEtareos.put(key,value);
	}
	
	public HashMap getGruposEtareosEliminados() {
		return gruposEtareosEliminados;
	}

	public void setGruposEtareosEliminados(HashMap gruposEtareosEliminados) {
		this.gruposEtareosEliminados = gruposEtareosEliminados;
	}
	public Object getGruposEtareosEliminados(String key) {
		return gruposEtareosEliminados.get(key);
	}

	public void setGruposEtareosEliminados(String key,Object value) {
		this.gruposEtareosEliminados.put(key,value);
	}

	public String getRegimenGrupoEtareo() {
		return regimenGrupoEtareo;
	}

	public void setRegimenGrupoEtareo(String regimenGrupoEtareo) {
		this.regimenGrupoEtareo = regimenGrupoEtareo;
	}

	public String getActividad()
	{
		return actividad;
	}

	public void setActividad(String actividad)
	{
		this.actividad = actividad;
	}

	public void resetMapas()
	{
		this.viasIngreso=new HashMap();
		this.viasIngreso.put("numRegistros","0");
		this.viasIngresoEliminados=new HashMap();
		this.viasIngresoEliminados.put("numRegistros","0");
		this.gruposEtareos=new HashMap();
		this.gruposEtareos.put("numRegistros","0");
		this.gruposEtareosEliminados=new HashMap();
		this.gruposEtareosEliminados.put("numRegistros","0");
		
	}
	
	
}
