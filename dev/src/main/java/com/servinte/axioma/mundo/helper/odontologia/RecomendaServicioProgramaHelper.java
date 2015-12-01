package com.servinte.axioma.mundo.helper.odontologia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.princetonsa.sort.odontologia.recomendaciones.SortRecomenServicioPrograma;
import com.servinte.axioma.orm.RecomSerproSerpro;


/**
 * 
 * @author Edgar Carvajal
 *
 */
public class RecomendaServicioProgramaHelper {

	
	
	
	
	/**
	 * ORDENAMIENTO DE RECOMENDACIONES SERVICIOS PROGRAMAS 
	 * @author Edgar Carvajal Ruiz
	 * @param listaServicioProgramas
	 */
	@SuppressWarnings("unchecked")
	public static Set  ordenamientoRecomendacionServicio(Set listaServicioProgramas ){
		
		
		Iterator iterator =listaServicioProgramas.iterator();
		List<RecomSerproSerpro> lista = new ArrayList<RecomSerproSerpro>();
		
		
		
		while(iterator.hasNext())
		{
			RecomSerproSerpro recomSerPro = (RecomSerproSerpro)iterator.next();
			lista.add(recomSerPro);
		}
		
		
		Collections.sort(lista,new  SortRecomenServicioPrograma());
		Set newLista = new HashSet();
		
		for(RecomSerproSerpro dto :lista )
		{
			newLista.add(dto);
		}
		
		return newLista;
	}
	
	
	
	
}
