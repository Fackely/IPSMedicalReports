/**
 * Nov 05, 2009
 */
package com.princetonsa.dao.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.ResultadoBoolean;

import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.odontologia.DtoCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoOdo;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * 
 * nterface que maneja los procesos de acceso a la fuente de datos
 * de la atencion de citas de odontologia
 * @author Sebastián Gómez R.
 *
 */
public interface AtencionCitasOdontologiaDao 
{
	/**
	 * Método para obtener las citas odontologicas para atender
	 * @param parametros
	 * @return
	 */
	public ArrayList<DtoCitaOdontologica> consultarCitas(HashMap<String, Object> parametros);
	
	/**
	 * Método para cargar las plantillas de atención para la cita
	 * @param cita
	 * @param usuario
	 * @return
	 */
	public ArrayList<DtoPlantilla> cargarPlantillasAtencion(DtoCitaOdontologica cita,UsuarioBasico usuario);
	
	/**
	 * Método para cargar el historico de plantillas por ingreso del paciente
	 * @param codigoPaciente
	 * @param consecutivoIngreso
	 * @return
	 */
	public ArrayList<DtoPlantilla> consultarPlantillasPorIngresos(int codigoPaciente, String consecutivoIngreso);
	
	/**
	 * Método para cargar los datos de la cita para la atencion
	 * @param cita
	 * @param usuario
	 */
	public void cargarDatosCita(DtoCitaOdontologica cita,UsuarioBasico usuario, boolean leftOuterUnidadAgenda);
	
	/**
	 * Método implementado para realizar la confirmación de los datos de la cita
	 * @param con
	 * @param cita
	 * @param usuarioConfirmacion
	 * @return
	 */
	public ResultadoBoolean confirmarDatosCita(Connection con, DtoCitaOdontologica cita, UsuarioBasico usuarioConfirmacion);
	
	/**
	 * Método implementado para cargar los servicios de una cita
	 * @param con
	 * @param cita
	 * @param usuario
	 */
	public void cargarServiciosCita(Connection con,DtoCitaOdontologica cita,UsuarioBasico usuario);
	
	/**
	 * Método implementado 
	 * @param con
	 * @param codigoPkProgServ
	 * @return
	 */
	public BigDecimal consultarNumeroSolicitudServicioCitaXPlanTratamiento(Connection con,BigDecimal codigoPkProgServ);
	
	/**
	 * Método implementado para consultar el codigo_pk del servicio de la cita asociado
	 * al registro del programa/servicio plan de tratamiento
	 * @param con
	 * @param codigoPkProgServ
	 * @return
	 */
	public BigDecimal consultarCodigoServicioCitaOdoXPlanTratamiento(Connection con,BigDecimal codigoPkProgServ);
	
	/**
	 * Método implementado para cargar las plantillas registradas de la cita
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public ArrayList<DtoPlantilla> cargarPlantillasRegistradas(Connection con,BigDecimal codigoCita);
	
	/**
	 * Método implementado para cargar la plantilla seleccionada en el historico
	 * @param con
	 * @param codigoCita
	 * @param codigoPkPlantilla
	 * @return
	 */
	public DtoPlantilla cargarPlantillaSeleccionadaDeHistorico(Connection con,BigDecimal codigoCita,BigDecimal codigoPkPlantilla);
	
	/**
	 * Método implementado para cargar los datos del paciente
	 * @param con
	 * @param paciente
	 */
	public void cargarDatosPaciente(Connection con,DtoPaciente paciente);
	
	
	
}
