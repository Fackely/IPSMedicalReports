package com.servinte.axioma.servicio.impl.tesoreria;

import java.util.ArrayList;

import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IConceptosIngTesoreriaMundo;
import com.servinte.axioma.orm.ConceptosIngTesoreria;
import com.servinte.axioma.servicio.interfaz.tesoreria.IConceptosIngTesoreriaServicio;

public class ConceptosIngTesoreriaServicio implements IConceptosIngTesoreriaServicio {
	
	private IConceptosIngTesoreriaMundo conceptoMundo;
	
	
	public ConceptosIngTesoreriaServicio(){
		this.conceptoMundo=TesoreriaFabricaMundo.crearConceptosIngTesoreriaMundo();
	}

	@Override
	public ArrayList<ConceptosIngTesoreria> obtenerConceptosTipoIngAnticiposConvOdont() {
		return conceptoMundo.obtenerConceptosTipoIngAnticiposConvOdont();
	}

}
