package com.princetonsa.dao.carterapaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.sqlbase.carteraPaciente.SqlBaseDocumentosGarantiaDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseUtilidadesManejoPacienteDao;

public interface DocumentosGarantiaDao
{
	
	/**
	 * consulta lista de Garantia Generados
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public HashMap consultarListaDocumentosGarantia(Connection con,
											HashMap parametros);	
		
	
	/**
	 * Inserta un nuevo registro de tipo Documentos en Garantia
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public int insertarDocumentoGarantia(Connection con, 
													HashMap parametros);
	
	
	/**
	 * Actuliza Documentos en Garantia
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public boolean actualizarDocumentoGarantia(Connection con, 
												HashMap parametros);
	
	/**
	 * Devuelve el listado de los documentos cargados al paciente y su estado
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap infoDocsDependientes(Connection con,
											  HashMap parametros); 
	
	
	/**
	 * Consulta la dependencia que existe entre el Deudor y el Codeudor
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarDependenciaDeudorCodeudor(Connection con, HashMap parametros);
	
	
	/**
	 * Eliminacion de Informacion DeudorCo
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean eliminarDeudorCo(Connection con, HashMap parametros);

	
	/**
	 * Consulta los datos de la persona
	 * @param Connection con
	 * @param HashMap parametros
	 * @param String OpcionConsulta
	 * */
	public HashMap consultarDatosPaciente(Connection con, HashMap parametros);
	
	/**
	 * Consulta los datos de la persona
	 * @param Connection con
	 * @param HashMap parametros
	 * @param String OpcionConsulta
	 * */
	public HashMap consultarResponsablePaciente(Connection con, HashMap parametros);
	
	/**
	 * inserta un nuevo responsable de paciente
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public boolean insertarResponsablePaciente(Connection con, HashMap parametros);
	
	
	/**
	 * Actualiza la informacion del responsable del paciente mientras este exista y 
	 * los campos de ciudad y pais de residencia y expedicion esten vacios
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarResponsablePaciente(Connection con, HashMap parametros);
	
	
	
	/**
	 * inserta un registro de tipo deudorco
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public int insertarDeudorCo(Connection con, 
									HashMap parametros);
	
	
	/**
	 * Consulta Informacion DeudorCo
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarDeudorCo(Connection con,
									 HashMap parametros);

	/**
	 * Actualiza un registro de tipo deudorco
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public boolean actualizarDeudorCo(Connection con,
									  HashMap parametros);
	
	/**
	 * Actualiza el codigo del Responsable en la Tabla Cuentas
	 * Connection con
	 * HashMap parametros
	 * */
	public boolean actualizarCuenta(Connection con, 
									HashMap parametros);
	
	/**
	 * Actualiza los registros de la verificacion
	 * Connection con
	 * HashMap parametros
	 * */
	public boolean actualizarverficacion(Connection con,
										 HashMap parametros);
	
	/**
	 * consulta ingresos de pacientes
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public HashMap consultarIngresos(Connection con,
			  						 HashMap parametros);
	
	/**
	 * @autor Jhony Alexander Duque A. 
	 * Metodo encargado de de consultar los ingresos que tienen documentos de garantia
	 * @param Connection con
	 * @param HashMap parametros 
	 */
	public HashMap consultarListadoingresosDocumentosGarantia (Connection connection, HashMap parametros);
	
	/**
	 * Consulta por Rangos los documentos de garantia existentes
	 * @author Jhony Alexander Duque A.
	 * @param Connection con
	 * @param HashMap parametros
	 * en el hashmap parametros deben venir los criterios de la busqueda; estos datos pueden o no venir.
	 * institucion --> Requerido
	 * centroatencion --> Opcional
	 * fechainicial --> Opcional
	 * fechafinal --> Opcional
	 * onsecutivo --> Opcional
	 * tipodocumento --> Opcional 
	 * estadodoc --> Opcional
	 */
	public HashMap consultaXRangosDocumentosGarantia (Connection connection, HashMap parametros);
	
	
	/**
	 * Actualiza la informacion del DeudorCo de Documentos de Garantia cuando se ha 
	 * modificado la informacion del paciente o del Responsable
	 * @param Connection con
	 * @param HashMap campos  
	 * */
	public boolean actualizarDatosPersonaDocGarantia(Connection con, HashMap campos);
	
	/**
	 * Método que verifica  si existen documentos de garabtía x ingreso
	 */
	public boolean existenDocumentosGarantiaXIngreso(Connection con,HashMap campos);
}