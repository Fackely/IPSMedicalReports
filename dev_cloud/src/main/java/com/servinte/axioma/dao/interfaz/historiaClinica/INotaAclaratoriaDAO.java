package com.servinte.axioma.dao.interfaz.historiaClinica;

import java.util.List;

import com.servinte.axioma.dto.manejoPaciente.DtoNotaAclaratoria;
import com.servinte.axioma.orm.NotaAclaratoria;

/**
 * Esta interfaz se encarga de definir los métodos de negocio
 * para la entidad  NotaAclaratoria
 * @author Ricardo Ruiz
 */
public interface INotaAclaratoriaDAO {
	
	/**
	 * Método que se encarga de obtener todas las notas aclaratorias asociadas a un ingreso
	 * @param codigoIngreso
	 * @return La lista de notas aclaratorias
	 * @author Ricardo Ruiz
	 */
	public List<DtoNotaAclaratoria> buscarNotasAclaratoriasPorIngreso(int codigoIngreso);
	
	/**
	 * Método que permite sincronizar con la BD la nota Aclaratoria
	 * @param notaAclaratoria
	 * @return
	 * @author Ricardo Ruiz
	 */
	public NotaAclaratoria merge(NotaAclaratoria notaAclaratoria);

}
