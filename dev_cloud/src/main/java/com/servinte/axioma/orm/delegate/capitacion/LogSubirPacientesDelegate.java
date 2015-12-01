package com.servinte.axioma.orm.delegate.capitacion;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.capitacion.ConsultaLogSubirPacientesForm;
import com.princetonsa.dto.capitacion.DTOConsultaLogSubirPacientes;
import com.servinte.axioma.orm.LogSubirPacientes;
import com.servinte.axioma.orm.LogSubirPacientesHome;

public class LogSubirPacientesDelegate extends LogSubirPacientesHome{

	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de log de subir pacientes
	 * 
	 * @param logSubirPacientes 
	 * @return boolean
	 * @author Camilo Gómez
	 *
	 */
	public boolean guardarLogSubirPacientes(LogSubirPacientes logSubirPacientes){
		boolean save = true;					
		try{
 			super.attachDirty(logSubirPacientes);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el registro de " +
					"log subir pacientes: ",e);
		}				
		return save;				
	}
	
	
	
	/**
	 * 
	 * Este Método se encarga de consultar en la base de datos
	 * el log de subir pacientes
	 * 
	 * @param  ConsultaLogSubirPacientesForm
	 * @return ArrayList<DTOSubirPacientesInconsistencias>
	 * @author Ricardo Ruiz
	 *
	 */
	@SuppressWarnings({ "unchecked", "static-access" })
	public ArrayList<DTOConsultaLogSubirPacientes> consultarLog(ConsultaLogSubirPacientesForm paramteros)
	{
						
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LogSubirPacientes.class,"logSubirPacientes");
		
		criteria.createAlias("logSubirPacientes.inconsistenSubirPacientes"		,"inconsistenSubirPaciente",criteria.LEFT_JOIN);
		criteria.createAlias("logSubirPacientes.convenios"						,"convenios");
		criteria.createAlias("logSubirPacientes.contratos"						,"contratos");
		criteria.createAlias("logSubirPacientes.usuarios"						,"usuarios");		
						
		// Parametro convenioparamteros
		if(!UtilidadTexto.isEmpty(paramteros.getConvenio().getId()))
		{
			criteria.add(Restrictions.eq("convenios.codigo", Utilidades.convertirAEntero(paramteros.getConvenio().getId())));			
		}
		//Parametro Fecha Inicial
		if(!UtilidadTexto.isEmpty(paramteros.getFechaInicial()))
		{			
			criteria.add(Restrictions.ge("logSubirPacientes.fechaCargue", UtilidadFecha.conversionFormatoFechaStringDate(paramteros.getFechaInicial())));									
		}
		//Parametro Fecha Final
		if(!UtilidadTexto.isEmpty(paramteros.getFechaInicial()))
		{	
			criteria.add(Restrictions.le("logSubirPacientes.fechaCargue", UtilidadFecha.conversionFormatoFechaStringDate(paramteros.getFechaFinal())));			
		}
		//Parametro Usuario
		if(!UtilidadTexto.isEmpty(paramteros.getUsuario()))
		{	
			criteria.add(Restrictions.eq("usuarios.login", paramteros.getUsuario()));			
		}
		
		
		ProjectionList projectionList = Projections.projectionList();
		/**tabla logSubirPacientes*/
			projectionList.add(Projections.groupProperty("logSubirPacientes.codigoPk"),"codigoPkLogSubir");
			projectionList.add(Projections.groupProperty("convenios.codigo"),"convenio");
			projectionList.add(Projections.groupProperty("convenios.nombre"),"nombreConvenio");
			projectionList.add(Projections.groupProperty("contratos.codigo"),"contrato");
			projectionList.add(Projections.groupProperty("contratos.numeroContrato"),"numeroContrato");
			projectionList.add(Projections.groupProperty("contratos.fechaInicial"),"fechaInicialContrato");
			projectionList.add(Projections.groupProperty("contratos.fechaFinal"),"fechaFinalContrato");
			projectionList.add(Projections.groupProperty("logSubirPacientes.fechaCargue"),"fechaCargue");
			projectionList.add(Projections.groupProperty("logSubirPacientes.horaCargue"),"horaCargue");
			projectionList.add(Projections.groupProperty("usuarios.login"),"usuario");
			projectionList.add(Projections.groupProperty("logSubirPacientes.fechaInicio"),"fechaInicial");
			projectionList.add(Projections.groupProperty("logSubirPacientes.fechaFin"),"fechaFinal");
			projectionList.add(Projections.count("inconsistenSubirPaciente.codigoPk"), "numInconsistencias");
		criteria.setProjection(projectionList);
		criteria.addOrder(Order.desc("logSubirPacientes.codigoPk"));
		
		criteria.setResultTransformer( Transformers.aliasToBean(DTOConsultaLogSubirPacientes.class));
	
		ArrayList<DTOConsultaLogSubirPacientes> listaResultado = (ArrayList<DTOConsultaLogSubirPacientes>) criteria.list();
		
		return listaResultado;				
	}
	
}
