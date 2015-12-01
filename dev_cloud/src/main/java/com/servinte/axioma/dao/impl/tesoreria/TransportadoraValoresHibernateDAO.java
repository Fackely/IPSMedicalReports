package com.servinte.axioma.dao.impl.tesoreria;

import java.util.List;

import com.servinte.axioma.dao.interfaz.tesoreria.ITransportadoraValoresDAO;
import com.servinte.axioma.orm.TransportadoraValores;
import com.servinte.axioma.orm.delegate.tesoreria.TransportadoraValoresDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link ITransportadoraValoresDAO}
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public class TransportadoraValoresHibernateDAO  implements ITransportadoraValoresDAO{
	
	
	
	private TransportadoraValoresDelegate transportadoraValoresDelegate ;
	
	
	
	
	
	public TransportadoraValoresHibernateDAO()
	{
		transportadoraValoresDelegate = new TransportadoraValoresDelegate();
	}

	
	

	
	@Override
	public List<TransportadoraValores> listarTodos(TransportadoraValores dtoTransportadora, int institucion) {
		
		return transportadoraValoresDelegate.listarTodos(dtoTransportadora, institucion);
	}


	@Override
	public TransportadoraValores consultarTransportadoraValores(int codigoTransportadora) {
		
		TransportadoraValores transportadoraValores = transportadoraValoresDelegate.findById(codigoTransportadora);
		
		if(transportadoraValores!=null){
			
			transportadoraValores.getTerceros().getDescripcion();
		}
		
		return transportadoraValores;
	}


	
	@Override
	public List<TransportadoraValores> listarTodos(TransportadoraValores dtoTransportadora, int institucion, int consecutivoCentroAtencion) 
	{
		return transportadoraValoresDelegate.listarTodos(dtoTransportadora, institucion, consecutivoCentroAtencion);
	}

}
