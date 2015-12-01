package com.servinte.axioma.dao.impl.facturacion;

import java.util.List;

import com.servinte.axioma.dao.interfaz.facturacion.IEstanciaViaIngCentroCostoDAO;
import com.servinte.axioma.orm.EstanciaViaIngCentroCosto;
import com.servinte.axioma.orm.delegate.facturacion.EstanciaViaIngCentroCostoDelegate;



/**
 * 
 * @author axioma
 *
 */
public class EstanciaViaIngCentroCostoDAO  implements IEstanciaViaIngCentroCostoDAO {
	
	
	
	/*
	 * 
	 */
	private EstanciaViaIngCentroCostoDelegate delegate;
	
	
	
	public EstanciaViaIngCentroCostoDAO(){
		delegate= new EstanciaViaIngCentroCostoDelegate();
		
	}
	
	

	/**
	 * METODO QUE RECIBE UN OBJETO  Y LO INSERTA EN LA BASE DE DATOS
	 * @author 
	 * @param objecto
	 */
	public void insertar(EstanciaViaIngCentroCosto objeto){
		delegate.guardarEstancia(objeto);
		
	}

	
	/**
	 * METODO QUE RECIBE UN OBJETO Y LOS MODIFICA EN LA BASE DE DATOS 
	 * @author 
	 * @param objecto
	 */
	public void modificar(EstanciaViaIngCentroCosto objeto){
		delegate.attachDirty(objeto);
		
	}
	/**
	 * METODO QUE RECIBE UN OBJETO Y LOS ELIMINA DE LA BASE DE DATOS
	 * @author 
	 * @param objecto
	 */
	public void eliminar( EstanciaViaIngCentroCosto objeto){
		delegate.delete(objeto);
		
	}

	
	/**
	 * METOD QUE RECIBE UN ID Y RETORNA UN TIPO DE OBJETO DE LA BASE DE DATOS
	 * @author 
	 * @param objeto
	 * @param id
	 * @return
	 */
	public EstanciaViaIngCentroCosto buscarxId(Number id){
		
		return  delegate.findById(id.longValue());
		
	}
	
	
	
	/**
	 * 
	 * @param idEntidadSubContratada
	 * @return
	 */
	public List<EstanciaViaIngCentroCosto> listarEstanciasxEntidadesSubContratadas(Long idEntidadSubContratada){
		
		return delegate.listarEstanciasxEntidadesSubContratadas(idEntidadSubContratada);
	}



	@Override
	public boolean guardarEstancia(EstanciaViaIngCentroCosto estancia) {
		return delegate.guardarEstancia(estancia);
	}
	

}
