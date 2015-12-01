package com.servinte.axioma.mundo.fabrica.odontologia.recomendacion;

import com.servinte.axioma.mundo.impl.odontologia.recomendacion.RecomendacionesContOdontoMundo;
import com.servinte.axioma.mundo.impl.odontologia.recomendacion.RecomendacionesServProSerProMundo;
import com.servinte.axioma.mundo.impl.odontologia.recomendacion.RecomendacionesServiciosProgramaMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.recomendacion.IRecomSerproSerproMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.recomendacion.IRecomendacionesContratoMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.recomendacion.IRecomendacionesServicioProgramasMundo;


/**
 * 
 * @author Edgar Carvajal
 *
 */
public abstract class RecomendacionesFabrica 
{
	
	
	
	
	/**
	 * CONTRUTOR PRIVADAO PARA QUE NO SE PUEDA HACER INSTANCIA DE LA FABRICA
	 */
	private RecomendacionesFabrica(){
		
	}
	
	
	

	
	/**
	 * CREAR INSTANCIA DE MUNDO RECOMENDACION CONTRATO
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static final  IRecomendacionesContratoMundo crearRecomentadacionMundo()
	{
		return new RecomendacionesContOdontoMundo();
	}
	
	
	
	
	/**
	 * CREAR INSTANCIA DE RECOMENDACIONES SERVICIO PROGRAMA MUNDO
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static final IRecomendacionesServicioProgramasMundo crearRecomendacionServioProgramaMundo()
	{
		return new RecomendacionesServiciosProgramaMundo();
	}

	
	
	
	/**
	 * CREAR INSTANTACION RECOMENDACIONES SERVICIO PROGRAMA. SERVICIO PROGRAMA 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static final IRecomSerproSerproMundo crearRecomendacionSerProSerProMundo()
	{
		return new RecomendacionesServProSerProMundo();
	}
	
	
	
	
	
	
	
	

}
