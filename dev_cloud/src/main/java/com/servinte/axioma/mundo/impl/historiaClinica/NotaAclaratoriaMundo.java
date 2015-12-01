package com.servinte.axioma.mundo.impl.historiaClinica;

import java.util.List;

import com.servinte.axioma.dao.impl.historiaClinica.NotaAclaratoriaDAO;
import com.servinte.axioma.dto.manejoPaciente.DtoNotaAclaratoria;
import com.servinte.axioma.mundo.interfaz.historiaClinica.INotaAclaratoriaMundo;
import com.servinte.axioma.orm.NotaAclaratoria;

/**
 * Esta clase se encarga de definir los métodos de negocio
 * para la entidad  NotaAclaratoria
 * @author Ricardo Ruiz
 */
public class NotaAclaratoriaMundo implements INotaAclaratoriaMundo{

	private NotaAclaratoriaDAO dao;
	
	/**
	 * Constructor de la clase
	 */
	public NotaAclaratoriaMundo(){
		dao=new NotaAclaratoriaDAO();
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.historiaClinica.INotaAclaratoriaMundo#buscarNotasAclaratoriasPorIngreso(int)
	 */
	@Override
	public List<DtoNotaAclaratoria> buscarNotasAclaratoriasPorIngreso(
			int codigoIngreso) {
		return dao.buscarNotasAclaratoriasPorIngreso(codigoIngreso);
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.historiaClinica.INotaAclaratoriaMundo#merge(com.servinte.axioma.orm.NotaAclaratoria)
	 */
	@Override
	public NotaAclaratoria merge(NotaAclaratoria notaAclaratoria) {
		return dao.merge(notaAclaratoria);
	}
}
