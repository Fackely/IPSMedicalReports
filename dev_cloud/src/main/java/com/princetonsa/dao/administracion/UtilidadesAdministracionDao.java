package com.princetonsa.dao.administracion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatosStr;
import util.ResultadoBoolean;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.administracion.DtoProfesional;
import com.princetonsa.dto.administracion.DtoTiposMoneda;
import com.princetonsa.dto.administracion.DtoUsuario;
import com.princetonsa.mundo.Usuario;


/**
 * 
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 *
 */public interface UtilidadesAdministracionDao
{
	
	 
		/**
		 * Metodo encargado de consultar los tipos
		 * de moneda; este puede ser filtrado por 
		 * dos criterios por el codigo yo por el 
		 * codigotipomoneda.
		 *-------------------------------------
		 *			PARAMETROS DTO
		 *-------------------------------------
		 *--institucion --> Requerido
		 *--codigo --> Opcional
		 *--codigoTipoMoneda --> Opcional
		 *
		 * @param connection
		 * @param dtoTiposMoneda
		 * @return
		 */
		public ArrayList<DtoTiposMoneda> obtenerTiposMoneda(Connection connection, DtoTiposMoneda dtoTiposMoneda);
		
		/**
		 * Método que consulta los profesionales según ciertas validaciones
		 * @param con
		 * @param campos
		 * @return
		 */
		public ArrayList<HashMap<String, Object>> obtenerProfesionales(Connection con,HashMap campos);
		
		/**
		 * Método que consulta los profesionales según ciertas validaciones
		 * @param con
		 * @param campos
		 * @return
		 */
		public ArrayList<HashMap<String, Object>> obtenerProfesionales(Connection con,HashMap campos, ArrayList<HashMap<String,Object>> centrosCosto);
		
		/**
		 * Método que consulta los cargos parametrizados por la institucion  
		 * @param con
		 * @param campos
		 * @return
		 */
		public ArrayList<HashMap<String, Object>> obtenerCargosInstitucion(Connection con,HashMap campos);
		
		/**
		 * Método que consulta los Tipos de Vigencia parametrizados por la institucion  
		 * @param con
		 * @param campos
		 * @return
		 */
		public ArrayList<HashMap<String, Object>> obtenerTiposVigencia(Connection con,HashMap campos);
		
		/**
		 * Cadena que consulta los centros de costos de una unidad de agenda segun el tipo entida que ejecuta y el 
		 * codigo de la unidad de agenda
		 * @param con
		 * @param campos
		 * @return
		 */
		public ArrayList<HashMap<String, Object>> obtenerCentroCosto(Connection con,HashMap campos);
		
		/**
		 * Cadena que consulta los codigos de los centros de costos asociados a una unidad de consulta
		 * @param con
		 * @param campos
		 * @return ArrayList<HashMap<String, Object>>
		 */
		public ArrayList<HashMap<String, Object>> obtenerCodCentroCostoXUnidadConsulta(Connection con,HashMap campos);
		/**
		 * 
		 * @param centroAtencion
		 * @return
		 */
		public  ArrayList<InfoDatosStr> obtenerUsariosCentroCosto(int centroAtencion);
		/**
		 * 
		 * @param objUsuario
		 * @param codigoInstitucion
		 * @return
		 */
		public ArrayList<Usuario> obtenerUsuarios(Usuario objUsuario, int codigoInstitucion,boolean ordenarPorNombre);
		
		/**
		 * 
		 * 
		 * 
		 */
		
		public  String  obtenerEspecialidadesMedicoPorLogin(String login );
		
		/**
		 * Método implementado para verificar si existe 
		 * @param con
		 * @param login
		 * @param password
		 * @param mismoProfesional
		 * @return
		 */
		public ResultadoBoolean existeUsuario(Connection con,String login,String password,boolean mismoProfesional);
		
		
		
		/**
		 * 
		 * @author Edgar Carvajal Ruiz
		 * @param dtoUsuario
		 * @return
		 */
		public DtoPersonas cargarPersona(DtoUsuario dtoUsuario );
		
		
		
		/**
		 * 
		 * M&eacute;todo que consulta los profesionales seg&uacute;n ciertas validaciones para la Agenda Odontol&oacute;gica
		 * 
		 * @param con
		 * @param campos
		 * @param centrosCosto
		 * @return
		 */
		public ArrayList<HashMap<String, Object>> obtenerProfesionalesAgendaOdontologica (Connection con, HashMap campos, ArrayList<HashMap<String,Object>> centrosCosto);

		/**
		 * 
		 * @param ocupaciones
		 * @param filtrarActivos
		 * @return
		 */
		public ArrayList<DtoProfesional> listarProfesionales(ArrayList<Integer> ocupaciones, boolean filtrarActivos);

	/**
	 * Obtener los profesionales por centro de atención
	 * @param con Conexión con la BD
	 * @param codigoCentroAtencion Código del centro de atención a filtrar
	 * @return Listado de {@link DtoProfesional} con el resultado de los profesionales
	 */
	public ArrayList<DtoProfesional> obtenerProfesionalesXCentroAtencion(Connection con, int codigoCentroAtencion);

		/**
		 * 
		 * @return
		 */
		public int procesoInactivacionUsuario();

		/**
		 * 
		 * @return
		 */
		public int procesoCaducidadPassword();

		/**
		 * 
		 * @param login
		 * @return
		 */
		public String fechaUltimoLoginUsuario(String login);

		
	/**
	 * 
	 * @param con
	 * @param login
	 * @return
	 */
	public boolean inactivarPasswordUsuario(Connection con, String login) throws Exception ;

	
	/**
	 * 
	 * @param con
	 * @param login
	 * @return
	 */
	public boolean inactivarUsuario(Connection con, String login) throws Exception ;

	/**
	 * 
	 * @param con
	 * @param codigoFuncionalidad
	 * @return
	 */
	public ArrayList<Integer> obtenerFuncionalidadHija(Connection con, int codigoFuncionalidad);
	
}
