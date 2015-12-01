/*
 * Creado el Jun 13, 2006
 * por Julian Montoya
 */
package com.princetonsa.actionform.capitacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.UtilidadCadena;

public class NivelAtencionForm extends ActionForm{

	/**
	 * Para Mantener el Flujo de La Funcionalidad
	 */
	private String estado; 

	/**
	 * Mapa para almacenar la información.
	 */
	private HashMap mapa; 
	
	
	/**
	 * Variables para ordenar El Listado
	 */
    private String patronOrdenar;
    private String ultimoPatronOrdenar;

    /**
     * Variable para contabilizar los registros nuevos ingresados
     */
    private int nroRegistrosNuevos;
    

    /**
     * Variable para saber si se entra a consultar o a ingresar.
     */
    private boolean  soloConsulta;

    /**Atributo que alamcena si se guardo exitosamente el nivel*/
    private boolean procesoExitoso;
    
	/**
	 * Limpiar e inicializar la informacion de la funcionalidad.
	 *
	 */
	public void reset()
	{
		this.nroRegistrosNuevos = 0;
		this.mapa = new HashMap();
	
		if(this.estado.equals("guardar"))
			this.procesoExitoso=true;
		else
			this.procesoExitoso=false;
	}
	/**
	 * Para validar la informacion del formulario. 
	 */
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		this.procesoExitoso=false;
		
		if(estado.equals("guardar"))
		{
			int nroReg = 0;
			if ( UtilidadCadena.noEsVacio(this.mapa.get("numRegistros")+"") )
			{
				nroReg = Integer.parseInt(this.mapa.get("numRegistros")+"");
			}
			
			ArrayList<String> nombresNivelesAnteriores= new ArrayList<String>();
			//-----Validando que se ingrese informacion en la descripcion de los anteriores registros.
			for (int i = 0; i < nroReg; i++)
			{
				String codigo = this.getMapa("codigo_" + i)+"";  
				String descripcion = this.getMapa("nivel_" + i)+""; 
				if ( !UtilidadCadena.noEsVacio(descripcion) )
				{
					errores.add("Descripcion vacia", new ActionMessage("errors.required","La descripción del nivel con codigo " + codigo));
				}else
				{
					nombresNivelesAnteriores.add(this.getMapa("h_nivel_" + i)+"".trim().toUpperCase());
				}
				
				
			}
			
			if(errores.isEmpty())
			{
				for (int i = 0; i < nroReg; i++)
				{
					for(int j=0; j< nroReg;j++)
					{
						if(i!=j)
						{
							String nombreAnterior=nombresNivelesAnteriores.get(i).trim().toUpperCase();
							String nombreNuevo=this.getMapa("nivel_" + j)+"".trim();
							nombreNuevo=nombreNuevo.toUpperCase();
							if(nombreAnterior.equals(nombreNuevo))
							{
								errores.add("Descripciones iguales", new ActionMessage("errores.descripcionNivelAtencion"));
								return errores;
							}
						}
					}
					for (int j = 0; j <= this.nroRegistrosNuevos; j++)
					{
						String nombreAnterior=nombresNivelesAnteriores.get(i).trim().toUpperCase();
						String nombreNuevo=this.getMapa("nivelN_" + j)+"".trim();
						nombreNuevo=nombreNuevo.toUpperCase();
						if(nombreAnterior.equals(nombreNuevo))
						{
							errores.add("Descripciones iguales", new ActionMessage("errores.descripcionNivelAtencion"));
							return errores;
						}
					}
				}
			}
			

			//-----Validando los registrados nuevos.
			for (int i = 0; i <= this.nroRegistrosNuevos; i++)
			{
				String codigo = this.getMapa("codigoN_" + i)+"";  
				String descripcion = this.getMapa("nivelN_" + i)+""; 
				if ( !UtilidadCadena.noEsVacio(codigo) && UtilidadCadena.noEsVacio(descripcion) )
				{
					errores.add("Codigo vacio", new ActionMessage("errors.required","El código del nuevo nivel " + descripcion));
				}
				if ( !UtilidadCadena.noEsVacio(descripcion) && UtilidadCadena.noEsVacio(codigo) )
				{
					errores.add("Descripcion vacia", new ActionMessage("errors.required","La descripción del nuevo nivel con codigo " + codigo));
				}
			}

			//-Validando los codigos repetidos de los nuevos.	
			Vector rep = new Vector();
			for (int i = 0; i <= this.nroRegistrosNuevos; i++)
			{
				String cod = this.getMapa("codigoN_" + i)+"";  
				for (int j = 0; j <= this.nroRegistrosNuevos; j++) 
				{
					String codAux = this.getMapa("codigoN_" + j)+"";
					if ( (j!=i) && UtilidadCadena.noEsVacio(cod) && UtilidadCadena.noEsVacio(codAux) && cod.trim().equals(codAux.trim()) && !rep.contains(cod) )
					{
						errores.add("Codigo Repetido", new ActionMessage("error.capitacion.yaExisteCodigo"," código del nivel " + cod));
						rep.add(codAux);
					}
				}
			}
			
			//----Validando los Codigos.
			for (int i = 0; i < nroReg; i++)  //--Barriendo los viejos.
			{
				String codigo = this.getMapa("codigo_" + i)+"";  
				for (int j = 0; j <= this.nroRegistrosNuevos; j++) 	//--Barriendo los nuevos.
				{
					String codAux = this.getMapa("codigoN_" + j)+"";
					if ( UtilidadCadena.noEsVacio(codigo) && UtilidadCadena.noEsVacio(codAux) && codigo.trim().equals(codAux.trim()) )
					{
						errores.add("Codigo Repetido", new ActionMessage("error.capitacion.yaExisteCodigo"," código del nivel " + codigo));
					}
				}
			}

			
			
		}
		return errores;
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
	 * @return Returns the mapaFuerzaMuscular.
	 */
	public HashMap getMapa() {
		return this.mapa;
	}

	/**
	 * @param Asignar el mapa
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
	 * @return Retorna patronOrdenar.
	 */
	public String getPatronOrdenar() {
		return this.patronOrdenar;
	}

	/**
	 * @param Asigna patronOrdenar.
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return Retorna ultimoPatronOrdenar.
	 */
	public String getUltimoPatronOrdenar() {
		return ultimoPatronOrdenar;
	}

	/**
	 * @param Asigna ultimoPatronOrdenar.
	 */
	public void setUltimoPatronOrdenar(String ultimoPatronOrdenar) {
		this.ultimoPatronOrdenar = ultimoPatronOrdenar;
	}

	/**
	 * @return Retorna nroRegistrosNuevos.
	 */
	public int getNroRegistrosNuevos() {
		return nroRegistrosNuevos;
	}

	/**
	 * @param Asigna nroRegistrosNuevos.
	 */
	public void setNroRegistrosNuevos(int nroRegistrosNuevos) {
		this.nroRegistrosNuevos = nroRegistrosNuevos;
	}
	/**
	 * @return Retorna soloConsulta.
	 */
	public boolean getSoloConsulta() {
		return soloConsulta;
	}
	/**
	 * @param Asigna soloConsulta.
	 */
	public void setSoloConsulta(boolean soloConsulta) {
		this.soloConsulta = soloConsulta;
	}
	public void setProcesoExitoso(boolean procesoExitoso) {
		this.procesoExitoso = procesoExitoso;
	}
	public boolean isProcesoExitoso() {
		return procesoExitoso;
	}

}
