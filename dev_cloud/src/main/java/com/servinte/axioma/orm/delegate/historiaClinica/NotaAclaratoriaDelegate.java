package com.servinte.axioma.orm.delegate.historiaClinica;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.type.StandardBasicTypes;

import com.servinte.axioma.dto.manejoPaciente.DtoNotaAclaratoria;
import com.servinte.axioma.orm.NotaAclaratoriaHome;
import com.servinte.axioma.orm.delegate.administracion.MedicosDelegate;

/**
 * Esta clase se encarga de definir los métodos de negocio
 * para la entidad  NotaAclaratoria
 * @author Ricardo Ruiz
 */
public class NotaAclaratoriaDelegate extends NotaAclaratoriaHome{
	
	
	/**
	 * Método que se encarga de obtener todas las notas aclaratorias asociadas a un ingreso
	 * @param codigoIngreso
	 * @return La lista de notas aclaratorias
	 * @author Ricardo Ruiz
	 */
	@SuppressWarnings("unchecked")
	public List<DtoNotaAclaratoria> buscarNotasAclaratoriasPorIngreso(int codigoIngreso){
		String consulta="SELECT new com.servinte.axioma.dto.manejoPaciente.DtoNotaAclaratoria(" +
									"na.codigo, na.descripcion, na.fechaRegistro, na.horaRegistro, med.codigoMedico, " +
									"per.primerNombre, per.segundoNombre, per.primerApellido, " +
									"per.segundoApellido, med.numeroRegistro) " +
						"FROM NotaAclaratoria na " +
							"INNER JOIN na.ingresos ing " +
							"INNER JOIN na.medicos med " +
							"INNER JOIN med.personas per " +
						"WHERE ing.id=:codigoIngreso";
		Query query = sessionFactory.getCurrentSession().createQuery(consulta);
		query.setParameter("codigoIngreso", codigoIngreso, StandardBasicTypes.INTEGER);
		List<DtoNotaAclaratoria> notasAclaratorias=(List<DtoNotaAclaratoria>)query.list();
		if(notasAclaratorias != null && !notasAclaratorias.isEmpty()){
			MedicosDelegate medicoDelegate= new MedicosDelegate();
			AdjuntoNotaAclaratoriaDelegate adjuntoNotaDelegate= new AdjuntoNotaAclaratoriaDelegate();
			for(DtoNotaAclaratoria dtoNota:notasAclaratorias){
				dtoNota.setEspecialidadesProfesional(medicoDelegate.
						obtenerEspecialidadesMedicoSeparadoPorComa(dtoNota.getCodigoProfesional()));
				dtoNota.getArchivosAdjuntos().addAll(adjuntoNotaDelegate.buscarAdjuntosNotaAclaratoria(dtoNota.getCodigo()));
			}
		}
		return notasAclaratorias;
	}
	

}
