package com.servinte.axioma.dao.impl.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOEstanciaViaIngCentroCosto;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IViasIngresoDAO;
import com.servinte.axioma.orm.ViasIngreso;
import com.servinte.axioma.orm.delegate.manejoPaciente.ViasIngresoDelegate;

/**
 * Esta clase se encarga de ejecutar los métodos de 
 * negocio para la entidad Vias de Ingreso
 * 
 * @author Angela Maria Aguirre
 * @since 2/09/2010
 */
public class ViasIngresoHibernateDAO implements IViasIngresoDAO 
{
	
	/**
	 * Instancia de ViasIngresoDelegate
	 */
	ViasIngresoDelegate delegate;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public ViasIngresoHibernateDAO(){
		delegate = new ViasIngresoDelegate();
	}
	
	/**
	 * 
	 * Este Método se encarga de buscar todas las vias 
	 * de ingreso registradas en el sistema
	 * 
	 * @return ArrayList<ViasIngreso>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<ViasIngreso> buscarViasIngreso(){
		return delegate.buscarViasIngreso();
	}

	
	/**
	 * Este M&eacute;todo se encarga de buscar vias
	 * de ingreso por c&oacute;digo
	 * @param id
	 * @return ViasIngreso
	 * @author Diana Carolina G
	 */
	public ViasIngreso findbyId(int id){
		return  delegate.findById(id);
	}

	
	@Override
	public ArrayList<DTOEstanciaViaIngCentroCosto> buscarVias(DTOEstanciaViaIngCentroCosto parametros) 
	{
		return delegate.buscarVias(parametros);
	}
	

}
