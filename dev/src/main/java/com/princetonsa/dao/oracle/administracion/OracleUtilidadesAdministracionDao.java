package com.princetonsa.dao.oracle.administracion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatosStr;
import util.ResultadoBoolean;

import com.princetonsa.dao.administracion.UtilidadesAdministracionDao;
import com.princetonsa.dao.sqlbase.administracion.SqlBaseUtilidadesAdministracionDao;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.administracion.DtoProfesional;
import com.princetonsa.dto.administracion.DtoTiposMoneda;
import com.princetonsa.dto.administracion.DtoUsuario;
import com.princetonsa.mundo.Usuario;


/**
 * Métodos de Oracle para el acceso a la fuente de datos para las utilidades
 * del módulo de ADMINISTRACION 
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 *
 */
public class OracleUtilidadesAdministracionDao implements UtilidadesAdministracionDao
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
	public ArrayList<DtoTiposMoneda> obtenerTiposMoneda(Connection connection, DtoTiposMoneda dtoTiposMoneda)
	{
		return SqlBaseUtilidadesAdministracionDao.obtenerTiposMoneda(connection, dtoTiposMoneda);
	}
	
	/**
	 * Método que consulta los profesionales según ciertas validaciones
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerProfesionales(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesAdministracionDao.obtenerProfesionales(con, campos);
	}
	
	/**
	 * Método que consulta los profesionales según ciertas validaciones
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerProfesionales(Connection con,HashMap campos, ArrayList<HashMap<String,Object>> centrosCosto)
	{
		return SqlBaseUtilidadesAdministracionDao.obtenerProfesionales(con, campos, centrosCosto);
	}
	
	/**
	 * Método que consulta los cargos parametrizados por la institucion  
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerCargosInstitucion(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesAdministracionDao.obtenerCargosInstitucion(con, campos);
	}
	
	/**
	 * Método que consulta los Tipos de Vigencia parametrizados por la institucion  
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerTiposVigencia(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesAdministracionDao.obtenerTiposVigencia(con, campos);
	}
	
	/**
	 * Cadena que consulta los centros de costos de una unidad de agenda segun el tipo entida que ejecuta y el 
	 * codigo de la unidad de agenda
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerCentroCosto(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesAdministracionDao.obtenerCentroCosto(con, campos);
	}
	
	/**
	 * Cadena que consulta los codigos de los centros de costos asociados a una unidad de consulta
	 * @param con
	 * @param campos
	 * @return ArrayList<HashMap<String, Object>>
	 */
	public ArrayList<HashMap<String, Object>> obtenerCodCentroCostoXUnidadConsulta(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadesAdministracionDao.obtenerCodCentroCostoXUnidadConsulta(con, campos);
	}

	@Override
	public ArrayList<InfoDatosStr> obtenerUsariosCentroCosto(int centroCosto) {
		
		return SqlBaseUtilidadesAdministracionDao.obtenerUsariosCentroCosto(centroCosto);
	}

	@Override
	public ArrayList<Usuario> obtenerUsuarios(Usuario objUsuario,
			int codigoInstitucion, boolean ordenarPorNombre) {
		
		return SqlBaseUtilidadesAdministracionDao.obtenerUsuarios(objUsuario, codigoInstitucion, ordenarPorNombre);
	}

	@Override
	public String obtenerEspecialidadesMedicoPorLogin(String login) {
		
		return SqlBaseUtilidadesAdministracionDao.obtenerEspecialidadesMedicoPorLogin(login);
	}
	
	/**
	 * Método implementado para verificar si existe 
	 * @param con
	 * @param login
	 * @param password
	 * @return
	 */
	@Override
	public ResultadoBoolean existeUsuario(Connection con,String login,String password,boolean mismoProfesional)
	{
		return SqlBaseUtilidadesAdministracionDao.existeUsuario(con, login, password,mismoProfesional);
	}

	@Override
	public DtoPersonas cargarPersona(DtoUsuario dtoUsuario) 
	{
		return SqlBaseUtilidadesAdministracionDao.cargarPersonas(dtoUsuario);
	}

	/* (non-Javadoc)
	 * @see com.princetonsa.dao.administracion.UtilidadesAdministracionDao#obtenerProfesionalesAgendaOdontologica(java.sql.Connection, java.util.HashMap, java.util.ArrayList)
	 */
	@Override
	public ArrayList<HashMap<String, Object>> obtenerProfesionalesAgendaOdontologica(Connection con, HashMap campos,
			ArrayList<HashMap<String, Object>> centrosCosto) {
	
		return SqlBaseUtilidadesAdministracionDao.obtenerProfesionalesAgendaOdontologica(con, campos, centrosCosto);
	}
	
	/**
	 * 
	 * @param ocupaciones
	 * @param filtrarActivos
	 * @return
	 */
	@Override
	public ArrayList<DtoProfesional> listarProfesionales(ArrayList<Integer> ocupaciones, boolean filtrarActivos)
	{
		return SqlBaseUtilidadesAdministracionDao.listarProfesionales(ocupaciones,filtrarActivos);
	}
	
	@Override
	public ArrayList<DtoProfesional> obtenerProfesionalesXCentroAtencion(Connection con, int codigoCentroAtencion)
	{
		return SqlBaseUtilidadesAdministracionDao.obtenerProfesionalesXCentroAtencion(con, codigoCentroAtencion);
	}


	@Override
	public int procesoInactivacionUsuario() 
	{
		return SqlBaseUtilidadesAdministracionDao.procesoInactivacionUsuario();
	}

	@Override
	public int procesoCaducidadPassword()
	{
		return SqlBaseUtilidadesAdministracionDao.procesoCaducidadPassword();
	}

	@Override
	public String fechaUltimoLoginUsuario(String login) 
	{
		return SqlBaseUtilidadesAdministracionDao.fechaUltimoLoginUsuario(login);
	}

	@Override
	public boolean inactivarPasswordUsuario(Connection con, String login) throws Exception 
	{
		return SqlBaseUtilidadesAdministracionDao.inactivarPasswordUsuario(con, login);
	}

	@Override
	public boolean inactivarUsuario(Connection con, String login) throws Exception 
	{
		return SqlBaseUtilidadesAdministracionDao.inactivarUsuario(con, login);
	}

	/**
	 * 
	 * @param con
	 * @param codigoFuncionalidad
	 * @return
	 */
	public ArrayList<Integer> obtenerFuncionalidadHija(Connection con, int codigoFuncionalidad)
	{
		return SqlBaseUtilidadesAdministracionDao.obtenerFuncionalidadHija(con,codigoFuncionalidad);
	}
}