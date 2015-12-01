/*
 * @(#)ElementoApResource.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_05
 *
 */

package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;


/**
 * Clase utilitaria que permite manejar errores
 * en la forma t�pica que lo hace un error de 
 * aplication Resources de Java, una llave
 * y una colecci�n de atributos
 * 
 * @version 1.0 Dec 9, 2004
 */
public class ElementoApResource 
{
	
    /**
     * String con la llave del error a manejar
     */
    private String llave="";
    
    /**
     * Colecci�n con los atributos de este 
     * mensaje de error. Por el momento se
     * maneja una implementaci�n "ArrayList",
     * pero puede ser cambiada en cualquier
     * momento
     */
    private Collection atributos;
    
    /**
     * Constructor para 
     *
     */
    public ElementoApResource(String llave)
    {
        this.clean();
        this.llave=llave;
    }
    
    /**
     * M�todo que limpia este objeto
     */
    public void clean ()
    {
        this.llave="";
        atributos=new ArrayList();
    }
    /**
     * @return Retorna el/la llave.
     */
    public String getLlave() {
        return llave;
    }
    /**
     * El/La llave a establecer.
     * @param llave 
     */
    public void setLlave(String llave) {
        this.llave = llave;
    }
    
    /**
     * M�todo que me devuelve un iterador
     * que permite recorrer el conjunto
     * de atributos presentes
     * 
     * @return
     */
    public Iterator getAtributosIterator ()
    {
        return this.atributos.iterator();
    }
    
    /**
     * M�todo que me permite obtener un
     * atributo de la colecci�n
     * 
     * @param indice
     * @return
     */
    public String getAtributo(int indice) 
    {
        if (this.atributos instanceof ArrayList)
        {
            return (String)((ArrayList)this.atributos).get(indice);
        }
        else
        {
            Iterator it=this.getAtributosIterator();
            int contador=0;
            while (it.hasNext())
            {
                if (indice==contador)
                {
                    return (String)it.next();
                }
                contador++;
            }
            //Si llega a este punto es que el elemento
            //No se encontr�
            return "";
        }
    }
    
    /**
     * M�todo que agrega un atributo a este elemento
     * 
     * @param nuevoAtributo
     */
    public void agregarAtributo (String nuevoAtributo)
    {
        this.atributos.add(nuevoAtributo);
    }

	/**
	 * @return the atributos
	 */
	public Collection getAtributos() {
		return atributos;
	}

	/**
	 * @param atributos the atributos to set
	 */
	public void setAtributos(Collection atributos) {
		this.atributos = atributos;
	}
	
	/**
	 * @return the atributos
	 */
	public ArrayList getAtributosArrayList() {
		return (ArrayList) atributos;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ElementoApResource [atributos=" + atributos + ", llave="
				+ llave + "]";
	}

}
