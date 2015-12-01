/*
 *@author Jorge Armando Osorio Velasquez. 
 */
package com.princetonsa.actionform.pyp;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadValidacion;
import util.Utilidades;


/**
 * 
 * @author Jorge Armando Osorio Velasquez.
 *
 */
public class ActividadesProgramasPYPForm extends ValidatorForm 
{
	
	/**
	 * Variable para manejar el estado en el que se encuentra la funcionalidad
	 */
	private String estado;
	
	/**
	 * Variable para manejar el programa seleccionado al ingreso del sistema.
	 */
	private String programa;
	
	/**
	 * 
	 */
	private String actividad;
	
	
	/**
	 * 
	 */
	private boolean embarazo;
	
	/**
	 * 
	 */
	private String semanasGestacion;
	
	/***
	 * 
	 */
	private boolean requerido;
	
	/**
	 * Indica si la actividad que se está asociando al programa 
	 * se puede ejecutar varias veces al día (RQF 431) 
	 */
	private boolean permitirEjecutar;
	
	/**
	 * 
	 */
	private boolean activo;
	
	
	/**
	 * 
	 */
	private String descArchivo;
	
	/**
	 * 
	 */
	private boolean esModificacion;
	
	/**
	 * 
	 */
	private FormFile archivo;
	
	/**
     * el numero de registros por pager
     */
    private int maxPageItems;
    
    /**
     * 
     */
    private int institucion;
	
    /**
     * 
     */
    private String codigo;
	/**
	 * 
	 */
	private HashMap actProgPYP;
	
	/**
	 * 
	 */
	private int regEliminar;
	
	
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
    private HashMap metas;
    
    
    /**
     * 
     */
    private HashMap metasEliminados;
    
    /**
     * 
     */
    private HashMap diagnosticos;
    
	
	/**
	 * 
	 */
	private int diagEliminar;
	
	/**
	 * 
	 */
	private String finalidadConsulta;
	
	/**
	 * 
	 */
	private String finalidadServicio;
	
	
	/**
	 * VARIABLES USADAS SOLO PARA MANTENTER LOS DATOS.
	 *
	 */
	private String naturalezaServicio;
	private String tipoServicio;
    
	private String procesoExitoso;
    
	public void reset()
	{
		this.naturalezaServicio="";
		this.tipoServicio="";
		this.programa="";
		this.actividad="";
		this.embarazo=false;
		this.semanasGestacion="";
		this.requerido=false;
		this.permitirEjecutar=false;
		this.procesoExitoso=ConstantesBD.acronimoNo;
		this.activo=true;
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.descArchivo="";
		this.actProgPYP=new HashMap();
		this.actProgPYP.put("numRegistros","0");
		this.maxPageItems=20;
		this.codigo="";
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
		this.metas=new HashMap();
		this.metas.put("numRegistros","0");
		this.metasEliminados=new HashMap();
		this.metasEliminados.put("numRegistros","0");
		this.diagnosticos=new HashMap();
		this.diagnosticos.put("numRegistros","0");
		this.diagEliminar=ConstantesBD.codigoNuncaValido;
		this.esModificacion=false;
		this.finalidadConsulta="";
		this.finalidadServicio="";
	}
	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(this.estado.equals("guardarActividad")||this.estado.equals("modificarActividad"))
		{
			if(this.actividad.trim().equals(""))
			{
				errores.add("ACTIVIDAD REQUERIDO", new ActionMessage("errors.required","La Actividad"));  
			}
			if((this.finalidadConsulta.trim().equals("")||this.finalidadConsulta.trim().equalsIgnoreCase("null"))&&(this.finalidadServicio.trim().equals("")||this.finalidadServicio.trim().equalsIgnoreCase("null")))
			{
				errores.add("FINALIDAD REQUERIDO", new ActionMessage("errors.required","La Finalidad"));
			}
			if(this.embarazo)
			{
				if(this.semanasGestacion.trim().equals(""))
				{
					errores.add("SEMANAS GESTACION REQUERIDO", new ActionMessage("errors.required","Semanas Gestacion"));  
				}
				else
				{
					try
					{
						if(Utilidades.convertirAEntero(this.semanasGestacion+"")<=0)
							errores.add("errors.integerMayorQue", new ActionMessage("errors.integerMayorQue","Semanas Gestacion","0"));  
					}
					catch(Exception e)
					{
						errores.add("errors.integerMayorQue", new ActionMessage("errors.integerMayorQue","Semanas Gestacion","0"));  
					}
				}
			}
		}
		if(estado.equals("guardarViaIngreso"))
		{
			for(int k=0;k<Utilidades.convertirAEntero(this.getViasIngreso("numRegistros")+"");k++)
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
			for(int k=0;k<Utilidades.convertirAEntero(this.getGruposEtareos("numRegistros")+"");k++)
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
					ri=(Utilidades.convertirAEntero(rangoGE[0])*12)*30;
					rf=(Utilidades.convertirAEntero(rangoGE[1])*12)*30;
				}
				else if(rangoGE[2].equals(ConstantesBD.codigoUnidadMedidaFechaMeses+""))
				{
					ri=Utilidades.convertirAEntero(rangoGE[0])*30;
					rf=Utilidades.convertirAEntero(rangoGE[1])*30;
				}
				else
				{
					ri=Utilidades.convertirAEntero(rangoGE[0]);
					rf=Utilidades.convertirAEntero(rangoGE[1]);
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
						riT=(Utilidades.convertirAEntero(rangoTemp[0])*12)*30;
						rfT=(Utilidades.convertirAEntero(rangoTemp[1])*12)*30;
					}
					else if(rangoTemp[2].equals(ConstantesBD.codigoUnidadMedidaFechaMeses+""))
					{
						riT=Utilidades.convertirAEntero(rangoTemp[0])*30;
						rfT=Utilidades.convertirAEntero(rangoTemp[1])*30;
					}
					else
					{
						riT=Utilidades.convertirAEntero(rangoTemp[0]);
						rfT=Utilidades.convertirAEntero(rangoTemp[1]);
					}
					String sexTemp=Utilidades.obtenerSexoGrupoEtareoPYP(this.getGruposEtareos("grupoetareo_"+l)+"");
					if(Utilidades.convertirAEntero(sexoGE)==ConstantesBD.codigoSexoAmbos||Utilidades.convertirAEntero(sexTemp)==ConstantesBD.codigoSexoAmbos||Utilidades.convertirAEntero(sexoGE)==Utilidades.convertirAEntero(sexTemp))
					{
						if((ri<riT&&riT<rf)||(ri<=rfT&&rfT<=rf)||(riT<ri&&rfT>rf)||(ri==riT)||(rf==rfT))
						{
							errores.add("CRUCE DE RANGOS", new ActionMessage("error.pyp.programasPYP.GrupoEtareoCruceRangoerror","En la Posicion "+(k+1),"En la Posicion "+(l+1)));
						}
					}
				}
		    }		
			
		}


		if(estado.equals("guardarMeta"))
		{
			for(int k=0;k<Utilidades.convertirAEntero(this.getMetas("numRegistros")+"");k++)
		    {
				if(((this.getMetas("regimen_"+k)+"").trim()).equals(""))  
			      {
			          errores.add("REGIME REQUERIDA", new ActionMessage("errors.required","El Regimen para el registro "+(k+1)));  
			      }
				if(!((this.getMetas("meta_"+k)+"").trim()).equals(""))
				{
					double me=Double.parseDouble(this.getMetas("meta_"+k)+"");
					if(me<0||me>100)
					{
						errores.add("META", new ActionMessage("errors.range","La meta de cumplimiento en el registro "+(k+1),"0","100"));
					}
				}
				String reg=this.getMetas("regimen_"+k)+"";
				for(int l=0;l<k;l++)
				{
					if(reg.equals(this.getMetas("regimen_"+l)+"") && !(this.getMetas("regimen_"+k)+"").equals(""))  
		             {		                  		                  
		                  errores.add("YA EXISTE EL REGISTRO.", new ActionMessage("errors.yaExiste","El Regimen "+reg));                 
		             }
				}
		    }		
			
		}
		return errores;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public HashMap getActProgPYP() {
		return actProgPYP;
	}

	public void setActProgPYP(HashMap actProgPYP) {
		this.actProgPYP = actProgPYP;
	}

	public String getPrograma() {
		return programa;
	}

	public void setPrograma(String programa) {
		this.programa = programa;
	}

	public int getMaxPageItems() {
		return maxPageItems;
	}

	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	public String getActividad() {
		return actividad;
	}

	public void setActividad(String actividad) {
		this.actividad = actividad;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public boolean isEmbarazo() {
		return embarazo;
	}

	public void setEmbarazo(boolean embarazo) {
		this.embarazo = embarazo;
	}

	public boolean isRequerido() {
		return requerido;
	}

	public void setRequerido(boolean requerido) {
		this.requerido = requerido;
	}

	public String getSemanasGestacion() {
		return semanasGestacion;
	}

	public void setSemanasGestacion(String semanasGestacion) {
		this.semanasGestacion = semanasGestacion;
	}

	public FormFile getArchivo() {
		return archivo;
	}

	public void setArchivo(FormFile archivo) {
		this.archivo = archivo;
	}

	public String getDescArchivo() {
		return descArchivo;
	}

	public void setDescArchivo(String descArchivo) {
		this.descArchivo = descArchivo;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
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
	
	public HashMap getMetas() {
		return metas;
	}

	public void setMetas(HashMap metas) {
		this.metas = metas;
	}

	public Object getMetas(String key) {
		return metas.get(key);
	}

	public void setMetas(String key,Object value) {
		this.metas.put(key,value);
	}
	public HashMap getMetasEliminados() {
		return metasEliminados;
	}

	public void setMetasEliminados(HashMap metasEliminados) {
		this.metasEliminados = metasEliminados;
	}	
	public Object getMetasEliminados(String key) {
		return metasEliminados.get(key);
	}

	public void setMetasEliminados(String key,Object value) {
		this.metasEliminados.put(key,value);
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
		this.metas=new HashMap();
		this.metas.put("numRegistros","0");
		this.metasEliminados=new HashMap();
		this.metasEliminados.put("numRegistros","0");
		this.diagnosticos=new HashMap();
		this.diagnosticos.put("numRegistros","0");
	}

	public HashMap getDiagnosticos() {
		return diagnosticos;
	}

	public void setDiagnosticos(HashMap diagnosticos) {
		this.diagnosticos = diagnosticos;
	}


	public Object getDiagnosticos(String key) {
		return diagnosticos.get(key);
	}

	public void setDiagnosticos(String key,Object value) {
		this.diagnosticos.put(key,value);
	}

	public int getDiagEliminar() {
		return diagEliminar;
	}

	public void setDiagEliminar(int diagEliminar) {
		this.diagEliminar = diagEliminar;
	}

	public boolean isEsModificacion() {
		return esModificacion;
	}

	public void setEsModificacion(boolean esModificacion) {
		this.esModificacion = esModificacion;
	}

	public String getFinalidadConsulta() {
		return finalidadConsulta;
	}

	public void setFinalidadConsulta(String finalidadConsulta) 
	{
		this.finalidadConsulta = finalidadConsulta;
	}

	public String getFinalidadServicio() {
		return finalidadServicio;
	}

	public void setFinalidadServicio(String finalidadServicio) {
		this.finalidadServicio = finalidadServicio;
	}

	public String getNaturalezaServicio() {
		return naturalezaServicio;
	}

	public void setNaturalezaServicio(String naturalezaServicio) {
		this.naturalezaServicio = naturalezaServicio;
	}

	public String getTipoServicio() {
		return tipoServicio;
	}

	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	public void setPermitirEjecutar(boolean permitirEjecutar) {
		this.permitirEjecutar = permitirEjecutar;
	}

	public boolean isPermitirEjecutar() {
		return permitirEjecutar;
	}

	public void setProcesoExitoso(String procesoExitoso) {
		this.procesoExitoso = procesoExitoso;
	}

	public String getProcesoExitoso() {
		return procesoExitoso;
	}
}
