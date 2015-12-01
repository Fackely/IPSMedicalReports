/**
 * 
 */
package com.princetonsa.mundo.odontologia;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.ListIterator;

import org.apache.log4j.Logger;

import util.ResultadoBoolean;
import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.odontologia.ReasignarProfesionalOdontoDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseReasignarProfesionalOdontoDao;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.odontologia.DtoReasignarProfesionalOdonto;

/**
 * @author Jairo Andrés Gómez
 * noviembre 9 / 2009
 *
 */
public class ReasignarProfesionalOdonto {
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger = Logger.getLogger(ReasignarProfesionalOdonto.class);
	
	private ArrayList<DtoReasignarProfesionalOdonto> list;
	
	/**
	 * Constructor de la Clase
	 */
	public ReasignarProfesionalOdonto() {
		
	}

	/**
	 * DAO de este objeto, para trabajar con la fuente de datos
	 */
	private static ReasignarProfesionalOdontoDao getReasignarProfesionalOdontoDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getReasignarProfesionalOdontoDao();
	}
	
	/**
	 * Método que realiza el proceso para reasignar profesional
	 */
	public ResultadoBoolean reasignarProfesionalSalud(Connection connection, DtoInfoFechaUsuario fechaUsuario){
		boolean transaction = UtilidadBD.iniciarTransaccion(connection);
		ResultadoBoolean rb = new ResultadoBoolean(false);
		if(transaction){
			ListIterator<DtoReasignarProfesionalOdonto> iterator = this.list.listIterator();
			while (iterator.hasNext()){
				DtoReasignarProfesionalOdonto dto = iterator.next();
				transaction = getReasignarProfesionalOdontoDao().confirmacionDisponibilidadAgenda(connection, dto.getCodigoMedicoAsignado()
						, dto.getFechaAgenda(), dto.getHoraAgendaInicial(), dto.getHoraAgendaFinal());
				if (!transaction){
					rb = new ResultadoBoolean(true, "EL PROFESIONAL DE LA SALUD YA TIENE AGENDA GENERADA EL DÍA "+dto.getFechaAgenda()
							+" ENTRE "+dto.getHoraAgendaInicial()+" Y "+dto.getHoraAgendaFinal());
					break;
				}
				else{
					transaction = getReasignarProfesionalOdontoDao().actualizarAgendaOdontologica(connection, fechaUsuario
							, dto.getCodigoMedicoAsignado(), dto.getCodigoPkAgendaOdonto());
					if (!transaction){
						rb = new ResultadoBoolean(true, "NO SE HA PODIDO LLEVAR A CABO LA ACTUALIZACIÓN DE LA AGENDA");
						break;
					}
					else{
						transaction = getReasignarProfesionalOdontoDao().insertarLogReasignarProfesionalOdonto(connection, dto);
						if(!transaction){
							rb = new ResultadoBoolean(true, "NO HA SIDO POSIBLE INSERTAR EL LOG, LUEGO DE REASIGNAR PROFESIONAL");
							break;
						}
						else{
							rb = new ResultadoBoolean(true, "PROCESO EXITOSO!!");
						}
					}
				}
			}
		}
		else{
			logger.info("Error abriendo la transacción");
		}
		if(!transaction)
			UtilidadBD.abortarTransaccion(connection);
		else
			UtilidadBD.finalizarTransaccion(connection);
		return rb;
	}
	
	/**
	 * Método que consulta la especialidad de una unidad de agenda(consulta).
	 * @param connection
	 * @param unidadAgenda
	 * @return
	 */
	public int consultarEspecialidadUnidadAgenda (Connection connection, int unidadAgenda){
		return getReasignarProfesionalOdontoDao().consultarEspecialidadUnidadAgenda(connection, unidadAgenda);
	}
	
	/**
	 * Método que retorna el conjunto de registros resultado de la busqueda de servicios para reasignar profesional
	 * @param connection
	 * @param dtoReasignarProfesionalOdonto
	 * @return
	 */
	public ArrayList<DtoReasignarProfesionalOdonto> consultarServiciosAReasignar (Connection connection, DtoReasignarProfesionalOdonto dtoReasignarProfesionalOdonto){
		return getReasignarProfesionalOdontoDao().consultarServiciosAReasignar(connection, dtoReasignarProfesionalOdonto);
	}
	
	/**
	 * Método que consulta la informacion almacenada en el log de reasignaciones de profesional
	 * @param connection
	 * @param dtoReasignarProfesionalOdonto
	 * @return
	 */
	public ArrayList<DtoReasignarProfesionalOdonto> consultarLogReasignacionProfesional (Connection connection, DtoReasignarProfesionalOdonto dtoReasignarProfesionalOdonto){
		return getReasignarProfesionalOdontoDao().consultarLogReasignacionProfesional(connection, dtoReasignarProfesionalOdonto);
	}

	/**
	 * @return the list
	 */
	public ArrayList<DtoReasignarProfesionalOdonto> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(ArrayList<DtoReasignarProfesionalOdonto> list) {
		this.list = list;
	}
}
