package com.servinte.axioma.orm.delegate.historiaClinica;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;

import util.adjuntos.DTOArchivoAdjunto;

import com.servinte.axioma.dto.manejoPaciente.DtoNotaAclaratoria;
import com.servinte.axioma.orm.AdjuntoNotaAclaratoriaHome;

/**
 * Esta clase se encarga de definir los métodos de negocio
 * para la entidad  AdjuntoNotaAclaratoria
 * @author Ricardo Ruiz
 */
public class AdjuntoNotaAclaratoriaDelegate extends AdjuntoNotaAclaratoriaHome{
	
	/**
	 * Método que se encarga de obtener todas las notas aclaratorias asociadas a un ingreso
	 * @param codigoIngreso
	 * @return La lista de adjuntos de la nota aclaratoria
	 * @author Ricardo Ruiz
	 */
	@SuppressWarnings("unchecked")
	public List<DTOArchivoAdjunto> buscarAdjuntosNotaAclaratoria(long codigoNotaAclaratoria){
		String consulta="SELECT new util.adjuntos.DTOArchivoAdjunto(" +
											"adj.nombreOriginal, adj.nombreGenerado, " +
											"adj.fechaRegistro, adj.horaRegistro) " +
						"FROM NotaAclaratoria na " +
						"INNER JOIN na.adjuntoNotaAclaratorias adj " +
						"WHERE na.codigo=:codigoNotaAclaratoria";
		Query query = sessionFactory.getCurrentSession().createQuery(consulta);
		query.setParameter("codigoNotaAclaratoria", codigoNotaAclaratoria, Hibernate.LONG);
		List<DTOArchivoAdjunto> adjuntosNota=new ArrayList<DTOArchivoAdjunto>();
		List<DTOArchivoAdjunto> adjuntos=(List<DTOArchivoAdjunto>)query.list();
		if(adjuntos != null && !adjuntos.isEmpty()){
			adjuntosNota.addAll(adjuntos);
		}	
		return adjuntosNota;
	}

}
