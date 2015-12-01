package com.princetonsa.dao.oracle.carteraPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.carterapaciente.DocumentosGarantiaDao;
import com.princetonsa.dao.sqlbase.carteraPaciente.SqlBaseDocumentosGarantiaDao;

/**
 * @author Jose Eduardo Arias Doncel
 * jearias@princetonsa.com
 * */
public class OracleDocumentosGarantiaDao implements DocumentosGarantiaDao
{	
	
	
	/**
	 * Cadena de Inserccion de un Responsable de Paciente
	 * */
	private static final String strCadenaInserccionNuevoResponsable ="INSERT INTO responsables_pacientes (" +
			"numero_identificacion," +
			"tipo_identificacion," +
			"direccion," +			
			"telefono," +
			"relacion_paciente," +
			"codigo," +
			"codigo_pais_doc," +
			"codigo_ciudad_doc," +
			"codigo_depto_doc," +
			"primer_apellido," +
			"segundo_apellido," +
			"primer_nombre," +
			"segundo_nombre," +
			"codigo_pais," +
			"codigo_ciudad," +
			"codigo_depto," +
			"codigo_barrio," +
			"fecha_nacimiento," +
			"fecha_modifica," +
			"hora_modifica," +
			"usuario_modifica) " +
			"VALUES (?,?,?,?,?,seq_responsables_pacientes.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
	
	
	/**
	 * consulta lista de Garantia Generados
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public HashMap consultarListaDocumentosGarantia(Connection con,
											HashMap parametros)
	{
		return SqlBaseDocumentosGarantiaDao.consultarListaDocumentosGarantia(con, parametros);
	}
	
	/**
	 * Actuliza  Documentos en Garantia
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public boolean actualizarDocumentoGarantia(Connection con, 
													HashMap parametros)
	{
		return SqlBaseDocumentosGarantiaDao.actualizarDocumentoGarantia(con, parametros);
	}
	
	/**
	 * Inserta un nuevo registro de tipo Documentos en Garantia
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	@SuppressWarnings("unchecked")
	public int insertarDocumentoGarantia(Connection con, 
													HashMap parametros)
	{
		String anioConsecutivo=(String)parametros.get("anioconsecutivo");
		if(anioConsecutivo==null || anioConsecutivo.equals(""))
		{
			// Oracle no permite ingresar "" cuando es un dato calificado como 'not null'
			anioConsecutivo=" ";
			parametros.put("anioconsecutivo", anioConsecutivo);
		}
		return SqlBaseDocumentosGarantiaDao.insertarDocumentoGarantia(con, parametros);
	}
	
	
	/**
	 * Devuelve el listado de los documentos cargados al paciente y su estado
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap infoDocsDependientes(Connection con,
											  HashMap parametros) 
	{		
		return SqlBaseDocumentosGarantiaDao.infoDocsDependientes(con, parametros);
	}
	
	/**
	 * Consulta la dependencia que existe entre el Deudor y el Codeudor
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarDependenciaDeudorCodeudor(Connection con, HashMap parametros)
	{
		return SqlBaseDocumentosGarantiaDao.consultarDependenciaDeudorCodeudor(con, parametros);
	}
	
	/**
	 * Consulta los datos de la persona
	 * @param Connection con
	 * @param HashMap parametros
	 * @param String OpcionConsulta
	 * */
	public HashMap consultarDatosPaciente(Connection con, HashMap parametros)
	{
		return SqlBaseDocumentosGarantiaDao.consultarDatosPaciente(con, parametros);
	}
	
	/**
	 * Consulta los datos de la persona
	 * @param Connection con
	 * @param HashMap parametros
	 * @param String OpcionConsulta
	 * */
	public HashMap consultarResponsablePaciente(Connection con, HashMap parametros)
	{
		return SqlBaseDocumentosGarantiaDao.consultarResponsablePaciente(con, parametros);
	}
	
	
	/**
	 * Eliminacion de Informacion DeudorCo
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean eliminarDeudorCo(Connection con, HashMap parametros)
	{	
		return SqlBaseDocumentosGarantiaDao.eliminarDeudorCo(con, parametros);
	}
	
		
	/**
	 * inserta un nuevo responsable de paciente
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public boolean insertarResponsablePaciente(Connection con, HashMap parametros)
	{
		return SqlBaseDocumentosGarantiaDao.insertarResponsablePaciente(con, parametros,strCadenaInserccionNuevoResponsable);
	}
	
	
	/**
	 * Actualiza la informacion del responsable del paciente mientras este exista y 
	 * los campos de ciudad y pais de residencia y expedicion esten vacios
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarResponsablePaciente(Connection con, HashMap parametros)
	{
		return SqlBaseDocumentosGarantiaDao.actualizarResponsablePaciente(con, parametros);		
	}
	
	/**
	 * inserta un registro de tipo deudorco
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public int insertarDeudorCo(Connection con, 
									HashMap parametros)
	{
		return SqlBaseDocumentosGarantiaDao.insertarDeudorCo(con, parametros);
	} 		
	
	/**
	 * Consulta Informacion DeudorCo
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarDeudorCo(Connection con, 
									 HashMap parametros)
	{
		return SqlBaseDocumentosGarantiaDao.consultarDeudorCo(con, parametros);
	}
	
	/**
	 * Actualiza un registro de tipo deudorco
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public boolean actualizarDeudorCo(Connection con,
									  HashMap parametros) 
	{
		return SqlBaseDocumentosGarantiaDao.actualizarDeudorCo(con, parametros);		
	}
	
	/**
	 * Actualiza los registros de la verificacion
	 * Connection con
	 * HashMap parametros
	 * */
	public boolean actualizarverficacion(Connection con,
										 HashMap parametros)
	{
		return SqlBaseDocumentosGarantiaDao.actualizarverficacion(con, parametros);
	}
	
	/**
	 * Actualiza el codigo del Responsable en la Tabla Cuentas
	 * Connection con
	 * HashMap parametros
	 * */
	public boolean actualizarCuenta(Connection con, 
									HashMap parametros)
	{
		return SqlBaseDocumentosGarantiaDao.actualizarCuenta(con, parametros);
	}
	
	/**
	 * consulta ingresos de pacientes
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public HashMap consultarIngresos(Connection con,
											HashMap parametros)
	{
		return SqlBaseDocumentosGarantiaDao.consultarIngresos(con, parametros);
	}
	
	/**
	 * @autor Jhony Alexander Duque A. 
	 * Metodo encargado de de consultar los ingresos que tienen documentos de garantia
	 * @param Connection con
	 * @param HashMap parametros 
	 */
	public HashMap consultarListadoingresosDocumentosGarantia (Connection connection, HashMap parametros)
	{
		return SqlBaseDocumentosGarantiaDao.consultarListadoingresosDocumentosGarantia(connection, parametros);
	}
	
	
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
	public HashMap consultaXRangosDocumentosGarantia (Connection connection, HashMap parametros)
	{
		return SqlBaseDocumentosGarantiaDao.consultaXRangosDocumentosGarantia(connection, parametros);
	}
	
	/**
	 * Actualiza la informacion del DeudorCo de Documentos de Garantia cuando se ha 
	 * modificado la informacion del paciente o del Responsable
	 * @param Connection con
	 * @param HashMap campos  
	 * */
	public boolean actualizarDatosPersonaDocGarantia(Connection con, HashMap campos)
	{
		return SqlBaseDocumentosGarantiaDao.actualizarDatosPersonaDocGarantia(con, campos);
	}	
	
	/**
	 * Método que verifica  si existen documentos de garabtía x ingreso
	 */
	public boolean existenDocumentosGarantiaXIngreso(Connection con,HashMap campos)
	{
		return SqlBaseDocumentosGarantiaDao.existenDocumentosGarantiaXIngreso(con, campos);
	}
}