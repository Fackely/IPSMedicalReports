package com.mercury.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;

import org.apache.commons.beanutils.DynaBean;

import util.ConstantesBD;


public class UtilidadDynaCollection implements Comparator
{
    String columnaOrden;
    
    public UtilidadDynaCollection(String columnaOrden)
    {
        this.columnaOrden=columnaOrden;
    }
    
    public int compare(Object o1, Object o2)
    {
    	HashMap dyna1 = (HashMap)o1;
    	HashMap dyna2 = (HashMap)o2;
    	String campo1="";
        String campo2="";
    	
        //System.out.println("colu: "+columnaOrden);
        String vec[]= columnaOrden.split("_"); 
        //System.out.print("\n... Vector...."+vec+" tamanio: "+vec.length);
        
        //for(String elem: vec)
        	//System.out.println("elemto: "+elem);
        
        if(vec.length > 1 )
          {
           if( vec[1].equals("descendente"))
           {
            campo1 = dyna1.get(vec[0]).toString();
            campo2 = dyna2.get(vec[0]).toString();
            return campo2.compareTo(campo1);
           }           
        }
        else
        {
     	    campo1=dyna1.get(vec[0]).toString();
            campo2=dyna2.get(vec[0]).toString();
            return campo1.compareTo(campo2);
        }
        
        return campo1.compareTo(campo2);
        
    }
    
    public static Collection ordenar(Collection lista, String columna)
    {
    	//System.out.print("\n... entramos a ordenar por Columna...."+columna);
        Object[] listado = lista.toArray();
        UtilidadDynaCollection utilidad = new UtilidadDynaCollection(columna);
        //System.out.print("\n... vamos Bien antes del....SORT");
        Arrays.sort(listado, utilidad);
        //System.out.print("\n... vamos Bien despues del....SORT");
        ArrayList ordenada = new ArrayList(listado.length);
        
        for(int i=0; i<listado.length; i++)
            ordenada.add(listado[i]);
       
        return ordenada;
    }
    
}