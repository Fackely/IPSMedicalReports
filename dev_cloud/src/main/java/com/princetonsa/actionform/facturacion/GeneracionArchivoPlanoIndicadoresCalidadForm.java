package com.princetonsa.actionform.facturacion;

import java.io.File;
import java.util.HashMap;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;

import javax.servlet.http.HttpServletRequest;

public class GeneracionArchivoPlanoIndicadoresCalidadForm extends ValidatorForm {
	
	private String estado="";
	
	private HashMap filtro=new HashMap();
	
	private HashMap instituciones=new HashMap();
	
	private String pathArchivoTxt;
	
	private String mensaje;
	
	private String archivo;
	
	private int alerta=0;
	
	private String numArch;
	
	private int incon=0;
	
	private String inconsis="";
	
	public void reset(){
		this.filtro=new HashMap();
		this.filtro.put("numRegistros", 0);
		this.archivo="";
		this.filtro.put("ano", "");
		this.filtro.put("ruta", "");
		this.filtro.put("periodo", "");
		this.filtro.put("institucion", "");
		this.instituciones=new HashMap();
		this.instituciones.put("numRegistros", 0);
		this.mensaje="";
		this.pathArchivoTxt="";
		this.alerta=0;
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if (this.estado.equals("generar") || this.estado.equals("sobrescribir"))
		{
		
			
			if (!this.filtro.containsKey("numArch") || this.filtro.get("numArch").toString().equals(""))
			{
				errores.add("El Numero de Archivo es Requerido ", new ActionMessage("errors.required"," Numero de Archivo "));
			}
			
			if (!this.filtro.containsKey("ano") || this.filtro.get("ano").toString().equals(""))
			{
				errores.add("El año es Requerido ", new ActionMessage("errors.required"," Año "));
			}
			if (!this.filtro.containsKey("periodo") || this.filtro.get("periodo").toString().equals(""))
			{
				errores.add("El periodo es Requerido ", new ActionMessage("errors.required"," Periodo "));
			}
			if (!this.filtro.containsKey("ruta") || this.filtro.get("ruta").toString().equals(""))
			{
				errores.add("La ruta es Requerida ", new ActionMessage("errors.required"," Ruta "));
			}
			else
			{
				/*
				File directorio = new File(this.filtro.get("ruta").toString().trim());
				
				if(!this.filtro.get("ruta").toString().trim().endsWith("/"))
				{
					errores.add("error ", new ActionMessage("error.facturacion.rutaInvalida","  "));
				}*/

			}
			if (!this.filtro.containsKey("institucion") || this.filtro.get("institucion").toString().equals("-1"))
			{
				errores.add("La institucion es Requerida ", new ActionMessage("errors.required"," Institucion "));
			}
			
			if(!this.filtro.get("ano").toString().equals(""))
			{
				if(Integer.parseInt(this.filtro.get("ano").toString())>UtilidadFecha.getMesAnioDiaActual("anio"))
				{
					errores.add("Fecha (año) mayor a la fecha (año) del sistema",new ActionMessage("errors.fechaPosteriorIgualActual"," Año "," Actual"));
				}
				
				if (!this.filtro.get("periodo").toString().equals(""))
				{
					if(Integer.parseInt(this.filtro.get("ano").toString())>=UtilidadFecha.getMesAnioDiaActual("anio")
							&& this.filtro.get("periodo").toString().equals("43"))
					{
						errores.add("Motivo ",new ActionMessage("error.facturacion.archivoplanocalidad",this.filtro.get("periodo").toString()+" (01 Julio - 31 Diciembre)",Integer.parseInt(this.filtro.get("ano").toString())));
					}
					
					if (this.filtro.get("periodo").toString().equals("41"))
					{
						if (Integer.parseInt(this.filtro.get("ano").toString())>UtilidadFecha.getMesAnioDiaActual("anio"))
						{
							errores.add("Motivo ",new ActionMessage("error.facturacion.archivoplanocalidad",this.filtro.get("periodo").toString()+" (01 enero - 30 Julio)",Integer.parseInt(this.filtro.get("ano").toString())));
						}
						else
							if(Integer.parseInt(this.filtro.get("ano").toString())==UtilidadFecha.getMesAnioDiaActual("anio")
									&& UtilidadFecha.getMesAnioDiaActual("mes")<=6)
							{
								errores.add("Motivo ",new ActionMessage("error.facturacion.archivoplanocalidad",this.filtro.get("periodo").toString()+" (01 enero - 30 Julio)",Integer.parseInt(this.filtro.get("ano").toString())));
							}
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

	/**
	 * @return the filtro
	 */
	public HashMap getFiltro() {
		return filtro;
	}

	/**
	 * @param filtro the filtro to set
	 */
	public void setFiltro(HashMap filtro) {
		this.filtro = filtro;
	}
	
	/**
	 * @param filtro the filtro to set
	 */
	public void setFiltro(String key, Object o) {
		this.filtro.put(key, o);
	}

	/**
	 * @return the instituciones
	 */
	public HashMap getInstituciones() {
		return instituciones;
	}

	/**
	 * @param instituciones the instituciones to set
	 */
	public void setInstituciones(HashMap instituciones) {
		this.instituciones = instituciones;
	}

	/**
	 * @return the archivo
	 */
	public String getArchivo() {
		return archivo;
	}

	/**
	 * @param archivo the archivo to set
	 */
	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}

	/**
	 * @return the mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return the pathArchivoTxt
	 */
	public String getPathArchivoTxt() {
		return pathArchivoTxt;
	}

	/**
	 * @param pathArchivoTxt the pathArchivoTxt to set
	 */
	public void setPathArchivoTxt(String pathArchivoTxt) {
		this.pathArchivoTxt = pathArchivoTxt;
	}

	/**
	 * @return the alerta
	 */
	public int getAlerta() {
		return alerta;
	}

	/**
	 * @param alerta the alerta to set
	 */
	public void setAlerta(int alerta) {
		this.alerta = alerta;
	}

	/**
	 * @return the numArch
	 */
	public String getNumArch() {
		return numArch;
	}

	/**
	 * @param numArch the numArch to set
	 */
	public void setNumArch(String numArch) {
		this.numArch = numArch;
	}

	public int getIncon() {
		return incon;
	}

	public void setIncon(int incon) {
		this.incon = incon;
	}

	public String getInconsis() {
		return inconsis;
	}

	public void setInconsis(String inconsis) {
		this.inconsis = inconsis;
	}

}
