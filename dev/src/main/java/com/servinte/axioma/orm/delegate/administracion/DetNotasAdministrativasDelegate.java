package com.servinte.axioma.orm.delegate.administracion;

import java.util.ArrayList;

import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.dto.manejoPaciente.DtoIngresos;
import com.servinte.axioma.orm.DetNotasAdministrativas;
import com.servinte.axioma.orm.DetNotasAdministrativasHome;

public class DetNotasAdministrativasDelegate extends DetNotasAdministrativasHome {
	

	/**
	 * 	Retorna todos
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DetNotasAdministrativas> listarDetallesNotas(){
		
		return (ArrayList<DetNotasAdministrativas>) sessionFactory.getCurrentSession()
				.createCriteria(DetNotasAdministrativas.class)
				.list();
	}
	
	
	
	/**
	 * Lista los detalles de la nota administrativa enviada
	 * @param notaAdministrativa
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DetNotasAdministrativas> listarDetallesDeNotaAdministrativa(int notaAdministrativa){
		
		return (ArrayList<DetNotasAdministrativas>) sessionFactory.getCurrentSession()
			.createCriteria(DetNotasAdministrativas.class)
			.add(Expression.eq("notasAdministrativas.codigoPk", notaAdministrativa))
			.addOrder( Order.desc("fechaModifica") )
			.addOrder( Order.desc("horaModifica") )
			.list();
	}
	
	/**
	 * 
	 * @param codigoPersona
	 * @return
	 */
	public static ArrayList<DtoIngresos> consultarIngresosConNotasAdministrativas(
			int codigoPersona) {
		return UtilidadesManejoPaciente.consultarIngresosConNotasAdministrativas(codigoPersona);
	}
}