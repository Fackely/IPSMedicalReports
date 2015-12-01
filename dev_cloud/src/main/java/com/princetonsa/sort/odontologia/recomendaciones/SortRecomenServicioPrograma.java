package com.princetonsa.sort.odontologia.recomendaciones;

import java.util.Comparator;
import com.servinte.axioma.orm.RecomSerproSerpro;


/**
 * 
 * @author Edgar Carvajal 
 *
 */
public class SortRecomenServicioPrograma implements Comparator<RecomSerproSerpro> {

	
	
	@Override
	public int compare(RecomSerproSerpro o1, RecomSerproSerpro o2) 
	{
	
		if( (o1.getProgramas()!=null) &&  (o2.getProgramas()!=null) )
		{
			return	o1.getProgramas().getNombre().compareTo(o2.getProgramas().getNombre());
		}
		
		return 0;
	}

	
	
}
