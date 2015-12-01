package util.Administracion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosStr;
import util.ResultadoBoolean;
import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.administracion.UtilidadesAdministracionDao;
import com.princetonsa.dao.sqlbase.administracion.SqlBaseUtilidadesAdministracionDao;
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
 */
public class UtilidadesAdministracion
{
	
	/**
	 * instancia del DAO
	 * @return
	 */
	public static UtilidadesAdministracionDao utilidadesAdministracionDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesAdministracionDao();
	}
	
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
	public static ArrayList<DtoTiposMoneda> obtenerTiposMoneda(Connection connection, DtoTiposMoneda dtoTiposMoneda)
	{
		return utilidadesAdministracionDao().obtenerTiposMoneda(connection, dtoTiposMoneda);
	}
	
	/**
	 * Método que consulta los profesionales según ciertas validaciones
	 * @param con
	 * @param codigoInstitucion
	 * @param codigoEspecialidad
	 * @param realizanCx
	 * @param activos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerProfesionales(Connection con,int codigoInstitucion,int codigoEspecialidad,boolean realizanCx, boolean activos, int codigoOcupacion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoInstitucion",codigoInstitucion);
		campos.put("codigoEspecialidad",codigoEspecialidad);
		campos.put("realizanCx", realizanCx?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		campos.put("activos",activos?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		campos.put("codigoOcupacion",codigoOcupacion);
		return utilidadesAdministracionDao().obtenerProfesionales(con, campos);
	}
	
	/**
	 * Método que consulta los profesionales según ciertas validaciones
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerProfesionales(Connection con,
			int codigoInstitucion,
			int codigoEspecialidad,
			boolean realizanCx, 
			boolean activos, 
			ArrayList<HashMap<String,Object>> centrosCosto)
	{
		HashMap campos = new HashMap();
		campos.put("codigoInstitucion",codigoInstitucion);
		campos.put("codigoEspecialidad",codigoEspecialidad);
		campos.put("realizanCx", realizanCx?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		campos.put("activos",activos?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		return utilidadesAdministracionDao().obtenerProfesionales(con, campos, centrosCosto);
	}
	
	
	
	/**
	 * 
	 * M&eacute;todo que consulta los profesionales seg&uacute;n ciertas validaciones para la Agenda Odontol&oacute;gica
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param codigoEspecialidad
	 * @param centrosCosto
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerProfesionalesAgendaOdontologica(Connection con,
			int codigoInstitucion,
			int codigoEspecialidad,
			ArrayList<HashMap<String,Object>> centrosCosto)
	{
		HashMap campos = new HashMap();
		campos.put("codigoInstitucion",codigoInstitucion);
		campos.put("codigoEspecialidad",codigoEspecialidad);
		return utilidadesAdministracionDao().obtenerProfesionalesAgendaOdontologica(con, campos, centrosCosto);
	}
	
	
	
	/**
	 * Método que consulta los cargos parametrizados por la institucion  
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerCargosInstitucion(Connection con,String codigoInstitucion, String activo)
	{
		HashMap campos = new HashMap();
		campos.put("institucion",codigoInstitucion);
		campos.put("activo",activo);
		return utilidadesAdministracionDao().obtenerCargosInstitucion(con, campos);
	}
	
	/**
	 * Método que consulta los cargos parametrizados por la institucion  
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposVigencia(Connection con,String codigoInstitucion, String activo, String tipo)
	{
		HashMap campos = new HashMap();
		campos.put("institucion",codigoInstitucion);
		campos.put("activo",activo);
		campos.put("tipo", tipo);
		return utilidadesAdministracionDao().obtenerTiposVigencia(con, campos);
	}
	
	
	/**
	 * Cadena que consulta los centros de costos de una unidad de agenda segun el tipo entida que ejecuta y el 
	 * codigo de la unidad de agenda
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerCentroCosto(Connection con,
			String codigo_institucion,
			String centro_atencion,
			String tipo_entidad_ejecuta)
	{
		HashMap campos = new HashMap();
		campos.put("institucion", codigo_institucion);
		campos.put("centro_atencion", centro_atencion);
		campos.put("tipo_entidad_ejecuta", tipo_entidad_ejecuta);
		return utilidadesAdministracionDao().obtenerCentroCosto(con, campos);
	}
	
	/**
	 * Cadena que consulta los codigos de los centros de costos asociados a una unidad de consulta
	 * @param con
	 * @param campos
	 * @return ArrayList<HashMap<String, Object>>
	 */
	public static ArrayList<HashMap<String, Object>> obtenerCodCentroCostoXUnidadConsulta(Connection con, String unidad_consulta)
	{
		HashMap campos = new HashMap();
		campos.put("unidad_consulta", unidad_consulta);
		return utilidadesAdministracionDao().obtenerCodCentroCostoXUnidadConsulta(con, campos);
	}
	
	/**
	 * 
	 * @param centroAtencion
	 * @return
	 */
	public static  ArrayList<InfoDatosStr> obtenerUsariosCentroCosto(int centroAtencion){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesAdministracionDao().obtenerUsariosCentroCosto(centroAtencion);
	}
	/**
	 * 
	 * @param objUsuario
	 * @param codigoInstitucion
	 * @return
	 */
	public static  ArrayList<Usuario>  obtenerUsuarios(Usuario objUsuario, int codigoInstitucion, boolean ordenarPorNombre){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesAdministracionDao().obtenerUsuarios(objUsuario, codigoInstitucion, ordenarPorNombre);
	}
	
	/**
	 * 
	 * 
	 */
	
	
	public static String obtenerEspecialidadesMedicoPorLogin(String login) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesAdministracionDao().obtenerEspecialidadesMedicoPorLogin(login);
	}
	
	/**
	 * Método implementado para verificar si existe 
	 * @param con
	 * @param login
	 * @param password
	 * @return
	 */
	public static ResultadoBoolean existeUsuario(Connection con,String login,String password,boolean mismoProfesional)
	{
		return utilidadesAdministracionDao().existeUsuario(con, login, password, mismoProfesional);
	}
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoUsuario
	 * @return
	 */
	public static  DtoPersonas cargarPersona(DtoUsuario dtoUsuario) 
	{
		return utilidadesAdministracionDao().cargarPersona(dtoUsuario);
	}

	/**
	 * 
	 * @param ocupaciones
	 * @param b
	 * @return
	 */
	public static ArrayList<DtoProfesional> listarProfesionales(ArrayList<Integer> ocupaciones, boolean filtrarActivos) 
	{
		return utilidadesAdministracionDao().listarProfesionales(ocupaciones,filtrarActivos);
	}

	/**
	 * Obtener los profesionales por centro de atención
	 * @param con Conexión con la BD
	 * @param codigoCentroAtencion Código del centro de atención a filtrar
	 * @return Listado de {@link DtoProfesional} con el resultado de los profesionales
	 */
	public static ArrayList<DtoProfesional> obtenerProfesionalesXCentroAtencion(Connection con, int codigoCentroAtencion)
	{
		return utilidadesAdministracionDao().obtenerProfesionalesXCentroAtencion(con, codigoCentroAtencion);
	}
	
	
		/**
	 * 
	 * @return
	 */
	public static int procesoInactivacionUsuario() 
	{
		return utilidadesAdministracionDao().procesoInactivacionUsuario();
	}

	/**
	 * 
	 * @return
	 */
	public static int procesoCaducidadPassword() 
	{
		return utilidadesAdministracionDao().procesoCaducidadPassword();
	}

	/**
	 * 
	 * @param login
	 */
	public static String fechaUltimoLoginUsuario(String login) 
	{
		return utilidadesAdministracionDao().fechaUltimoLoginUsuario(login);
	}

	/**
	 * 
	 * @param login
	 */
	public static boolean inactivarUsuario(String login) 
	{
		boolean resultado=false;
		Connection con=UtilidadBD.abrirConexion();
		try
		{
			resultado=utilidadesAdministracionDao().inactivarUsuario(con,login);
		}
		catch(Exception e)
		{
			Log4JManager.error("error",e);
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		return resultado;
	}

	/**
	 * 
	 * @param login
	 */
	public static boolean inactivarPasswordUsuario(String login) 
	{
		boolean resultado=false;
		Connection con=UtilidadBD.abrirConexion();
		try
		{
			resultado=utilidadesAdministracionDao().inactivarPasswordUsuario(con,login);
		}
		catch(Exception e)
		{
			Log4JManager.error("error",e);
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		return resultado;
		
	}

	public static ArrayList<Integer> obtenerFuncionalidadHija(int codigoFuncionalidad) 
	{
		ArrayList<Integer> resultado = null;
		Connection con=UtilidadBD.abrirConexion();
		try
		{
			resultado=utilidadesAdministracionDao().obtenerFuncionalidadHija(con,codigoFuncionalidad);
		}
		catch(Exception e)
		{
			Log4JManager.error("error",e);
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		return resultado;
	}
	
}