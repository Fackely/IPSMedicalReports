package com.servinte.axioma.dao.impl.tesoreria;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.tesoreria.IConceptosIngTesoreriaDAO;
import com.servinte.axioma.orm.ConceptosIngTesoreria;
import com.servinte.axioma.orm.delegate.tesoreria.ConceptosIngTesoreriaDelegate;

/**
 * Clase encargada de ejecutar los m&eacute;todos de 
 * negocio de la entidad ConceptosIngTesoreria
 * @author Diana Carolina G
 *
 */

public class ConceptosIngTesoreriaDAO implements IConceptosIngTesoreriaDAO {

	private ConceptosIngTesoreriaDelegate delegate;
	
	public ConceptosIngTesoreriaDAO(){
		delegate=new ConceptosIngTesoreriaDelegate();
	} 
	
	@Override
	public ArrayList<ConceptosIngTesoreria> obtenerConceptosTipoIngAnticiposConvOdont() {
		return delegate.obtenerConceptosTipoIngAnticiposConvOdont();
	}

	@Override
	public ConceptosIngTesoreria buscarxId(Number id) {
		return null;
	}

	@Override
	public void eliminar(ConceptosIngTesoreria objeto) {
		delegate.delete(objeto);
		
	}

	@Override
	public void insertar(ConceptosIngTesoreria objeto) {
		delegate.attachDirty(objeto);
		
	}

	@Override
	public void modificar(ConceptosIngTesoreria objeto) {
		delegate.attachDirty(objeto);
		
	}

}
