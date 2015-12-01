/*
 * Creado el Jun 1, 2006
 * por Julian Montoya
 */
package com.princetonsa.actionform.historiaClinica;

import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.UtilidadCadena;
import util.Utilidades;

public class SignosSintomasForm extends ActionForm {

	/**
	 * Variable para almacenar el estado del flujo por la funcionalidad. 
	 */
	private String estado;

	/**
	 * Mapa para guardar toda las descripciones de los signos y los sintomas.
	 */
	private HashMap mapa;
	
    /**
     * Variable para manejar el paginador.
     */
    private int index;
	private int pager;
    private int offset;
    private String linkSiguiente;
    private String patronOrdenar;
    private String ultimoPatronOrdenar;
    
    /**
     * Variable para contabilizar el numero de registros nuevos  
     */
    private int nroRegistros;
    
    /**
     * Variable para almacenar el registro a eliminar
     */
    private int nroRegEliminar;
    
    /**
     * Mostrar mensaje de exito
     */
    boolean operacionExitosa;
    
	/**
	 * Método para limpiar la clase
	 */
	public void reset()
	{
		this.mapa = new HashMap();
		this.ultimoPatronOrdenar = "";
		this.patronOrdenar = "";
		this.nroRegistros = 0;
	}
	
	/**
	 * Para validar la informacion del formulario. 
	 */
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if(estado.equals("guardar"))
		{
			int nroReg = 0;
			if ( UtilidadCadena.noEsVacio(this.mapa.get("numRegistros")+"") )
			{
				nroReg = Integer.parseInt(this.mapa.get("numRegistros")+"");
			}
			//-----Validando los anteriores registros.
			for (int i = 0; i <= nroReg; i++)
			{
				String codigo = this.getMapa("codigo_" + i)+"";  
				String descripcion = this.getMapa("signo_" + i)+""; 
				if ( !UtilidadCadena.noEsVacio(codigo) && UtilidadCadena.noEsVacio(descripcion) )
				{
					errores.add("Codigo vacio", new ActionMessage("errors.required","El código del signo/sintoma " + descripcion));
				}
				if ( !UtilidadCadena.noEsVacio(descripcion) && UtilidadCadena.noEsVacio(codigo) )
				{
					errores.add("Descripcion vacia", new ActionMessage("errors.required","La descripción del signo/sintoma con codigo " + codigo));
				}
			}

			//-----Validando los registrados anteriormente.
			for (int i = 0; i <= this.nroRegistros; i++)
			{
				String codigo = this.getMapa("codigoN_" + i)+"";  
				String descripcion = this.getMapa("signoN_" + i)+""; 
				if ( !UtilidadCadena.noEsVacio(codigo) && UtilidadCadena.noEsVacio(descripcion) )
				{
					errores.add("Codigo vacio", new ActionMessage("errors.required","El código del nuevo signo/sintoma " + descripcion));
				}
				if ( !UtilidadCadena.noEsVacio(descripcion) && UtilidadCadena.noEsVacio(codigo) )
				{
					errores.add("Descripcion vacia", new ActionMessage("errors.required","La descripción del nuevo signo/sintoma con codigo " + codigo));
				}
			}

			Vector rep = new Vector();
			
			//-----Validando que la los codigos de la informacion registrada anteriormente 
			//-----no se ingresen repetidos.
			for (int i = 0; i < nroReg; i++) // Recorro los codigos de la informacion ingresada en la bd
			{
				String cod = this.getMapa("codigo_" + i)+"";
				for (int j = 0; j < nroReg; j++) // Los comparo con los de la informacion ingresada en la bd
				{
					String codAux = this.getMapa("codigo_" + j)+"";
					//System.out.print(" (cod_"+j+"):"+codAux);
					if ( (j!=i) && UtilidadCadena.noEsVacio(cod) && UtilidadCadena.noEsVacio(codAux) )
					{
						if ( cod.trim().equals(codAux.trim()) )
						{
							if ( !rep.contains(cod) )
							{
							//error.signosSintomas.yaExisteCodigo=El codigo {0} del signo/sintoma se encuentra repetido. Por favor revisar. [cq-02]
								errores.add("Codigo Repetido", new ActionMessage("error.signosSintomas.yaExisteCodigo","" + cod));
								rep.add(codAux);
							}	
						}
					}
				}

				for (int j = 0; j <= this.nroRegistros; j++) // Los comparo con la de la informacion nueva, <= porque el nuevo tiene numero igual a nroRegistros
				{
					String codAux = this.getMapa("codigoN_" + j)+"";
					//System.out.print(" (codN_"+j+"):"+codAux);
					if ( UtilidadCadena.noEsVacio(cod) && UtilidadCadena.noEsVacio(codAux) )
					{
						if ( cod.trim().equals(codAux.trim()) )
						{
							if ( !rep.contains(cod) )
							{
							//error.signosSintomas.yaExisteCodigo=El codigo {0} del signo/sintoma se encuentra repetido. Por favor revisar. [cq-02]
								errores.add("Codigo Repetido", new ActionMessage("error.signosSintomas.yaExisteCodigo","" + cod));
								rep.add(codAux);
							}	
						}
					}
				}
			}

			//-------Validar los nuevos contra los nuevos
			//rep.clear();
			//-----Validando que la los codigos de la informacion registrada anteriormente 
			//-----no se ingresen repetidos.
			for (int i = 0; i <= this.nroRegistros; i++)
			{
				String cod = this.getMapa("codigoN_" + i)+"";  
				for (int j = 0; j <= this.nroRegistros; j++) // solo se comparan con los nuevos porque con los anteriores ya se hizo
				{
					String codAux = this.getMapa("codigoN_" + j)+"";
					//System.out.print(" (codN_"+j+"):"+codAux);
					if ( (j!=i) && UtilidadCadena.noEsVacio(cod) && UtilidadCadena.noEsVacio(codAux) )
					{
						if ( cod.trim().equals(codAux.trim()) )
						{
							if ( !rep.contains(cod) )
							{
							//error.signosSintomas.yaExisteCodigo=El codigo {0} del signo/sintoma se encuentra repetido. Por favor revisar. [cq-02]
								errores.add("Codigo Repetido", new ActionMessage("error.signosSintomas.yaExisteCodigo","" + cod));
								rep.add(codAux);
							}	
						}
					}
				}
			}
		}
		return errores;
	}
	
    
	/**
	 * @return Retorna index.
	 */
	public int getIndex() {
		return index;
	}


	/**
	 * @param Asigna index.
	 */
	public void setIndex(int index) {
		this.index = index;
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


	/**
	 * @return Retorna offset.
	 */
	public int getOffset() {
		return offset;
	}


	/**
	 * @param Asigna offset.
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}


	/**
	 * @return Retorna pager.
	 */
	public int getPager() {
		return pager;
	}


	/**
	 * @param Asigna pager.
	 */
	public void setPager(int pager) {
		this.pager = pager;
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
	 * @return Retorna mapa
	 */
	public HashMap getMapa() {
		return mapa;
	}
	/**
	 * @param Asigna mapa
	 */
	public void setMapa(HashMap mapaAux) {
		this.mapa = mapaAux;
	}
	/**
	 * @return Retorna mapa
	 */
	public Object getMapa(Object key) {
		return mapa.get(key);
	}
	/**
	 * @param Asigna dato.
	 */
	public void setMapa(Object key, Object dato) {
		this.mapa.put(key, dato);
	}


	/**
	 * @return Retorna patronOrdenar.
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
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
	 * @return Retorna nroRegistros.
	 */
	public int getNroRegistros() {
		return nroRegistros;
	}


	/**
	 * @param Asigna nroRegistros.
	 */
	public void setNroRegistros(int nroRegistros) {
		this.nroRegistros = nroRegistros;
	}

	/**
	 * @return Retorna nroRegEliminar.
	 */
	public int getNroRegEliminar() {
		return nroRegEliminar;
	}

	/**
	 * @param Asigna nroRegEliminar.
	 */
	public void setNroRegEliminar(int nroRegEliminar) {
		this.nroRegEliminar = nroRegEliminar;
	}

	public boolean isOperacionExitosa() {
		return operacionExitosa;
	}

	public void setOperacionExitosa(boolean operacionExitosa) {
		this.operacionExitosa = operacionExitosa;
	}

	
}
