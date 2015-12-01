package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.UtilidadBD;


/**
 * Clase que implementa el patrón singleton y un método sincronizado
 * para el manejo de concurrencia en la Reserva de Citas
 * 
 * @author Ricardo Ruiz Combita
 *
 */
public class SqlBaseCitaDaoSinglenton {
	
	/**
	 * Instancia única de la clase
	 */
	private static SqlBaseCitaDaoSinglenton instance=null;
	
	/**
	 * Atributo para controlar el acceso a la misma agenda
	 */
	private static int idAgenda;
	
	/**
	 * Para hacer logs debug / warn / error de esta Clase.
	 */
	private static Logger logger = Logger.getLogger(SqlBaseCitaDaoSinglenton.class);
	
	/**
	 * Constructor de la clase
	 */
	public SqlBaseCitaDaoSinglenton() {
		
	}

	/**
	 * Método encargado de evaluar si se crea la nueva instancia o se retorna la misma
	 * @return the instance
	 */
	public static SqlBaseCitaDaoSinglenton getInstance(int identAgenda) {
		if(instance==null){
			idAgenda=identAgenda;
			instance= new SqlBaseCitaDaoSinglenton();
			return instance;
		}
		else{
			if(idAgenda==identAgenda){
				return instance;
			}
			else{
				idAgenda=identAgenda;
				instance= new SqlBaseCitaDaoSinglenton();
				return instance;
			}
		}
	}

	/**
	* Reserva una cita en una fuente de datos, reutilizando una conexion 
	* existente en una BD Oracle y de forma sincronizada si ay concurrencia de usuarios accediendo a la misma cita
	* 
	 * @param ac_con
	 * @param i_paciente
	 * @param ai_agenda
	 * @param ai_unidadConsulta
	 * @param as_usuario
	 * @param mapaServicios
	 * @param fechaSolicitada
	 * @param prioridad
	 * @param motivoAutorizacionCita
	 * @param usuarioAutoriza
	 * @param requiereAuto
	 * @param verificarEstCitaPac
	 * @return Código asignado a la cita. Si es menor que 0 es un código inválido
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public synchronized int reservarCita(Connection	ac_con,	int	ai_paciente, int ai_agenda, int ai_unidadConsulta,
								String as_usuario, String is_validarReserva, String is_aumentarSeqCita,
								HashMap mapaServicios, String secuenciaServiciosCita, String fechaSolicitada,
								String prioridad, String motivoAutorizacionCita, String usuarioAutoriza, 
								String requiereAuto, String verificarEstCitaPac)throws Exception
	{
		int result = SqlBaseCitaDao.reservarCita(ac_con, ai_paciente, ai_agenda, ai_unidadConsulta, as_usuario, is_validarReserva, is_aumentarSeqCita, mapaServicios, secuenciaServiciosCita,fechaSolicitada,prioridad,
				motivoAutorizacionCita, usuarioAutoriza, requiereAuto, verificarEstCitaPac);
		UtilidadBD.finalizarTransaccion(ac_con);
		return result;
	}
}
