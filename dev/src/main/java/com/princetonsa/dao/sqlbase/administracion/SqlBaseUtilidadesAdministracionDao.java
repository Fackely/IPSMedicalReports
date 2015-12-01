package com.princetonsa.dao.sqlbase.administracion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosStr;
import util.LogsAxioma;
import util.MD5Hash;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.administracion.DtoProcesoInactivacionUsuario;
import com.princetonsa.dto.administracion.DtoProfesional;
import com.princetonsa.dto.administracion.DtoTiposMoneda;
import com.princetonsa.dto.administracion.DtoUsuario;
import com.princetonsa.mundo.Usuario;
import com.servinte.axioma.dao.fabrica.AdministracionFabricaDAO;
import com.servinte.axioma.dao.interfaz.administracion.ILogProcesoInactivacionUsuDAO;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.LogActiinactiUsuarios;
import com.servinte.axioma.orm.LogActiinactiUsuariosHome;
import com.servinte.axioma.orm.LogCadupwdUsuarios;
import com.servinte.axioma.orm.LogCadupwdUsuariosHome;
import com.servinte.axioma.orm.LogProcesoInactivacionUsu;
import com.servinte.axioma.orm.Medicos;
import com.servinte.axioma.orm.MedicosInactivos;
import com.servinte.axioma.orm.MedicosInactivosHome;
import com.servinte.axioma.orm.MedicosInactivosId;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.UsuariosInactivos;
import com.servinte.axioma.orm.UsuariosInactivosHome;
import com.servinte.axioma.orm.UsuariosInactivosId;
import com.servinte.axioma.orm.delegate.UsuariosDelegate;
import com.servinte.axioma.persistencia.UtilidadTransaccion;




/**
 * 
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 *
 */
public class SqlBaseUtilidadesAdministracionDao
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseUtilidadesAdministracionDao.class);
	
	
	/**
	 * Cadena de Consulta tipos de moneda
	 */
	private static final String strCadenaConsultaTiposMoneda = "SELECT " +
																	"tm.codigo AS codigo," +
																	"tm.codigo_tipo_moneda AS codigotipomoneda," +
																	"tm.descripcion AS descripcion," +
																	"tm.simbolo AS simbolo," +
																	"tm.institucion AS institucion " +
																" FROM tipos_moneda tm ";
		
	
	/**
	 * Cadena de Consulta Cargos
	 */
	private static final String strCadenaConsultaCargosIns = "SELECT " +
			"cu.codigo AS codigo, " +
			"cu.nombre AS nombre, " +
			"cu.activo AS activo, " +
			"cu.institucion AS institucion " +
			"FROM cargos_usuarios cu ";
	
	
	/**
	 * Cadena que Consulta los Tipos de Vigencias 
	 */
	public static final String strCadenaConsultaTiposVigencia = "SELECT " +
			"tv.codigo AS codigo, " +
			"tv.nombre AS nombre, " +
			"tv.activo AS activo, " +
			"tv.institucion AS institucion, " +
			"tv.tipo AS tipo " +
			"FROM tipos_vigencia tv ";
	
	/**
	 * Cadena que consulta los centros de costos de una unidad de agenda segun el tipo entida que ejecuta y el 
	 * codigo de la unidad de agenda
	 */
	private static final String strCadenaConsultaCentroCostoTipo = "SELECT " +
			"cc.codigo AS codigo, " +
			"cc.nombre AS nombre, " +
			"cc.identificador AS identificador " +
			"FROM centros_costo cc " +
			"WHERE institucion = ? " +
			"AND cc.centro_atencion = ? " +
			"AND tipo_area="+ConstantesBD.codigoTipoAreaDirecto+" " +
			"AND cc.tipo_entidad_ejecuta = ? ";
	
	/**
	 * Cadena que consulta los codigos de los centros de costos asociados a una unidad de consulta
	 */
	private static final String strCadenaConsutlaCodigosCentrosCiosto = "SELECT centro_costo FROM cen_costo_x_un_consulta WHERE unidad_consulta = ? ";
	
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
		logger.info("entro al obtenerTiposMoneda "+dtoTiposMoneda.getInstitucion());
		
		ArrayList<DtoTiposMoneda> resultados = new ArrayList<DtoTiposMoneda> ();
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs=null;
		try
		{
			//se filtra por institucion
			String consulta = strCadenaConsultaTiposMoneda,
			where=" WHERE tm.institucion="+dtoTiposMoneda.getInstitucion()+"";
			//se filtra por codigo
			if(dtoTiposMoneda.getCodigo()>0)
				where+=" AND tm.codigo="+dtoTiposMoneda.getCodigo()+"";
			//se filtra por el codigo dijitado por el usuario
			if(!UtilidadTexto.isEmpty(dtoTiposMoneda.getCodigoTipoMoneda()))
				where+=" AND tm.codigo_tipo_moneda='"+dtoTiposMoneda.getCodigoTipoMoneda()+"'";
				
			consulta+=where;
			logger.info("cadena de consulta obtenerTiposMoneda"+consulta);
			
			pst = new PreparedStatementDecorator(connection.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
			rs =  new ResultSetDecorator(pst.executeQuery(consulta));
			
			while(rs.next())
			{
				//se crea y se carga el Dto con la informacion que arroja la BD
				DtoTiposMoneda elemento = new DtoTiposMoneda(rs.getInt("codigo"),rs.getString("codigotipomoneda"),rs.getString("descripcion"),
								rs.getInt("institucion")	,rs.getString("simbolo"));
				//se añade el elemento al arrayList.
				resultados.add(elemento);
				logger.info("valores del dto: "+elemento.getCodigo());
			}
		}
		catch(Exception e){
			Log4JManager.error("ERROR obtenerTiposMoneda", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return resultados;
	}
	
	/**
	 * Método que consulta los profesionales según ciertas validaciones
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static ArrayList<HashMap<String, Object>> obtenerProfesionales(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs=null;
		try
		{
			//**********SE TOMAN LOS PARÁMETROS DEL FILTRO*************************************
			boolean realizanCx = UtilidadTexto.getBoolean(campos.get("realizanCx").toString());
			boolean activos = UtilidadTexto.getBoolean(campos.get("activos").toString());
			int codigoEspecialidad = Utilidades.convertirAEntero(campos.get("codigoEspecialidad").toString(), true);
			String codigoInstitucion = campos.get("codigoInstitucion").toString();
			int codigoOcupacion = Utilidades.convertirAEntero(campos.get("codigoOcupacion").toString());
			//*********************************************************************************
			
			String seccionSELECT = "SELECT DISTINCT "+ 
				"m.codigo_medico AS codigo_profesional, "+
				"getnombrepersona(m.codigo_medico) As nombre_profesional "+ 
				"FROM medicos m ";
			String seccionWHERE = "";
			
			if(realizanCx)
			{
				seccionSELECT += " INNER JOIN ocupa_realizan_qx_inst o ON(o.ocupacion=m.ocupacion_medica) ";
				seccionWHERE += (seccionWHERE.equals("")?" WHERE":" AND") + " o.institucion = "+codigoInstitucion;
			}
			
			if(codigoEspecialidad>0)
			{
				
				seccionSELECT += " INNER JOIN especialidades_medicos em ON(em.codigo_medico=m.codigo_medico) ";
				seccionWHERE += (seccionWHERE.equals("")?" WHERE":" AND") + " em.codigo_especialidad = "+codigoEspecialidad;
			}
			
			if(activos)
				seccionWHERE += (seccionWHERE.equals("")?" WHERE":" AND") + " m.codigo_medico NOT IN (SELECT codigo_medico FROM medicos_inactivos WHERE codigo_institucion = "+codigoInstitucion+") ";
			
			if(codigoOcupacion>0)
			{
				seccionWHERE += (seccionWHERE.equals("")?" WHERE":" AND") + " m.ocupacion_medica = "+codigoOcupacion+" ";
			}
			
			
			String consulta = seccionSELECT + seccionWHERE + " ORDER BY nombre_profesional";
			
			logger.info("consulta profesionales=> "+consulta);
			
			pst = new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
			rs =  new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo", rs.getObject("codigo_profesional"));
				elemento.put("nombre", rs.getObject("nombre_profesional"));
				resultados.add(elemento);
			}
		}
		catch(Exception e){
			Log4JManager.error("ERROR estaAsignadaCamaObservacion", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return resultados;
	}

	
	public static ArrayList<InfoDatosStr> obtenerUsariosCentroCosto(int centroAtencion){
		
		ArrayList<InfoDatosStr> arrayStr = new ArrayList<InfoDatosStr>();
		
		String consulta = "Select distinct usu.login as login, getnombreusuario(usu.login)  as nombre   from administracion.usuarios usu  INNER JOIN administracion.centros_costo_usuario ccu ON(ccu.usuario=usu.login) INNER JOIN administracion.centros_costo cco ON(ccu.centro_costo=cco.codigo) where  cco.centro_atencion="+centroAtencion;   
		Connection  con =null;
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs=null;
		try 
		{
			logger.info("\n\n\n\n\n SQL cargar usuarios centro / " + consulta);
			con = UtilidadBD.abrirConexion();
			ps = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next()) 
			{
				InfoDatosStr tmpUsuario = new InfoDatosStr();
				
				tmpUsuario.setCodigo(rs.getString("login"));
				tmpUsuario.setNombre(rs.getString("nombre"));
				arrayStr.add(tmpUsuario);
				
			} 
		}
		catch (SQLException e) 
		{
			logger.error("error en carga det env==> " + e);
		}
		finally{
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		return arrayStr;
		
		
	}
	
	
	
	/**
	 * Método que consulta los profesionales según ciertas validaciones
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static ArrayList<HashMap<String, Object>> obtenerProfesionales(Connection con,HashMap campos, ArrayList<HashMap<String,Object>> centrosCosto)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs=null;
		try
		{
			//**********SE TOMAN LOS PARÁMETROS DEL FILTRO*************************************
			boolean realizanCx = UtilidadTexto.getBoolean(campos.get("realizanCx").toString());
			boolean activos = UtilidadTexto.getBoolean(campos.get("activos").toString());
			int codigoEspecialidad = Utilidades.convertirAEntero(campos.get("codigoEspecialidad").toString(), true);
			String codigoInstitucion = campos.get("codigoInstitucion").toString();
			//*********************************************************************************
			String codigoCentroCosto  = "";
			String seccionSELECT = "SELECT DISTINCT "+ 
				"m.codigo_medico AS codigo_profesional, "+
				"getnombrepersona(m.codigo_medico) As nombre_profesional "+ 
				"FROM medicos m ";
			String seccionWHERE = "";
			codigoCentroCosto = getCodCentrosCostos(centrosCosto);
			
			if(realizanCx)
			{
				seccionSELECT += " INNER JOIN ocupa_realizan_qx_inst o ON(o.ocupacion=m.ocupacion_medica) ";
				seccionWHERE += (seccionWHERE.equals("")?" WHERE":" AND") + " o.institucion = "+codigoInstitucion;
			}
			
			if(codigoEspecialidad>0 && centrosCosto!= null && centrosCosto.size()>0)
			{
				seccionSELECT += " INNER JOIN usuarios usu ON (usu.codigo_persona = m.codigo_medico) " +
								 " INNER JOIN centros_costo_usuario ccu ON (ccu.usuario = usu.login) " +
								 " LEFT OUTER JOIN especialidades_medicos em ON(em.codigo_medico=m.codigo_medico) ";
				seccionWHERE += (seccionWHERE.equals("")?" WHERE":" AND") + " (em.codigo_especialidad = "+codigoEspecialidad+" OR ccu.centro_costo IN ("+codigoCentroCosto+"))";
			
			}else{
				
				if(codigoEspecialidad>0)
				{
					seccionSELECT += " INNER JOIN especialidades_medicos em ON(em.codigo_medico=m.codigo_medico) ";
					seccionWHERE += (seccionWHERE.equals("")?" WHERE":" AND") + " em.codigo_especialidad = "+codigoEspecialidad;
				}
				
				if(centrosCosto!=null && centrosCosto.size()>0)
				{
					seccionSELECT += " INNER JOIN usuarios usu ON (usu.codigo_persona = m.codigo_medico) " + 
									 " INNER JOIN centros_costo_usuario ccu ON (ccu.usuario = usu.login) ";
					seccionWHERE += (seccionWHERE.equals("")?" WHERE":" AND") + " ccu.centro_costo IN ("+codigoCentroCosto+") ";
				}
			}
			
			if(activos)
				seccionWHERE += (seccionWHERE.equals("")?" WHERE":" AND") + " m.codigo_medico NOT IN (SELECT codigo_medico FROM medicos_inactivos WHERE codigo_institucion = "+codigoInstitucion+") ";
			
			
			String consulta = seccionSELECT + seccionWHERE + " ORDER BY nombre_profesional";
			
			logger.info("consulta profesionales=> "+consulta);
			
			pst = new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
			rs =  new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo", rs.getObject("codigo_profesional"));
				elemento.put("nombre", rs.getObject("nombre_profesional"));
				resultados.add(elemento);
			}
		}
		catch(Exception e){
			Log4JManager.error("ERROR obtenerProfesionales", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return resultados;
	}
	
	
	/**
	 * Método que consulta los profesionales según ciertas validaciones
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static ArrayList<HashMap<String, Object>> obtenerProfesionalesAgendaOdontologica (Connection con, HashMap campos, ArrayList<HashMap<String,Object>> centrosCosto)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs=null;
		try
		{
			
			int codigoEspecialidad = Utilidades.convertirAEntero(campos.get("codigoEspecialidad").toString(), true);
			String codigoInstitucion = campos.get("codigoInstitucion").toString();
			//*********************************************************************************
			String codigoCentroCosto  = "";
			String seccionSELECT = "SELECT DISTINCT "+ 
				"m.codigo_medico AS codigo_profesional, "+
				"getnombrepersona(m.codigo_medico) As nombre_profesional "+ 
				"FROM medicos m ";
			String seccionWHERE = "";
			if(centrosCosto!=null && centrosCosto.size()>0)
			{
				codigoCentroCosto = getCodCentrosCostos(centrosCosto);
			}
			
			String ocupacionOdontologo = ValoresPorDefecto.getOcupacionOdontologo(Integer.parseInt(codigoInstitucion));
			String ocupacionAuxOdontologia = ValoresPorDefecto.getOcupacionAuxiliarOdontologo(Integer.parseInt(codigoInstitucion));
			
			seccionSELECT += " INNER JOIN administracion.ocupaciones_medicas o ON(o.codigo = m.ocupacion_medica) ";
			seccionWHERE += (seccionWHERE.equals("")?" WHERE":" AND") + " o.codigo IN ( "+ ocupacionOdontologo + " , " + ocupacionAuxOdontologia + " ) ";
		
			if(codigoEspecialidad>0)
			{
				seccionSELECT += " INNER JOIN especialidades_medicos em ON(em.codigo_medico=m.codigo_medico) ";
				seccionWHERE += (seccionWHERE.equals("")?" WHERE":" AND") + " em.codigo_especialidad = "+codigoEspecialidad;
			}
			
			if(centrosCosto!=null && centrosCosto.size()>0)
			{
				seccionSELECT += " INNER JOIN usuarios usu ON (usu.codigo_persona = m.codigo_medico) " + 
								 " INNER JOIN centros_costo_usuario ccu ON (ccu.usuario = usu.login) ";
				seccionWHERE += (seccionWHERE.equals("")?" WHERE":" AND") + " ccu.centro_costo IN ("+codigoCentroCosto+") ";
			}
		
			seccionWHERE += (seccionWHERE.equals("")?" WHERE":" AND") + " m.codigo_medico NOT IN (SELECT codigo_medico FROM medicos_inactivos WHERE codigo_institucion = "+codigoInstitucion+") ";
			
			
			String consulta = seccionSELECT + seccionWHERE + " ORDER BY nombre_profesional";
			
			logger.info("consulta profesionales para unidad agenda => "+consulta);
			
			pst = new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
			rs =  new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo", rs.getObject("codigo_profesional"));
				elemento.put("nombre", rs.getObject("nombre_profesional"));
				resultados.add(elemento);
			}
			
		}catch(Exception e){
			Log4JManager.error("ERROR obtenerProfesionalesAgendaOdontologica", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return resultados;
	}
	
	
	@SuppressWarnings("rawtypes")
	public static ArrayList<HashMap<String, Object>> obtenerCargosInstitucion(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> arrayCargosIns = new ArrayList<HashMap<String, Object>>();
		String consulta = strCadenaConsultaCargosIns;
		String condicionwhere = "";
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs=null;
		try{
			if(campos.size()>0){
				if(!campos.get("institucion").equals("")){
					condicionwhere += (condicionwhere.equals("")?" WHERE":" AND") + " cu.institucion = "+campos.get("institucion").toString();
				}
				if(!campos.get("activo").equals("")){
					condicionwhere += (condicionwhere.equals("")?" WHERE":" AND") + " cu.activo = '"+campos.get("activo").toString()+"'";
				}
			}
			consulta += condicionwhere+" ORDER BY cu.nombre ";
			pst = new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
			rs =  new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				HashMap<String, Object> cargos = new HashMap<String, Object>();
				cargos.put("codigo", rs.getInt("codigo"));
				cargos.put("nombre", rs.getString("nombre"));
				cargos.put("activo", rs.getString("activo"));
				cargos.put("institucion", rs.getInt("institucion"));
				arrayCargosIns.add(cargos);
			}
			logger.info("consulta >>>>>>>>>>>>>>>>>>>> "+consulta);
		}catch(Exception e){
			Log4JManager.error("ERROR obtenerCargosInstitucion", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return arrayCargosIns;
	}
	
	
	@SuppressWarnings("rawtypes")
	public static ArrayList<HashMap<String, Object>> obtenerTiposVigencia(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> arrayTipoVig = new ArrayList<HashMap<String, Object>>();
		String consulta = strCadenaConsultaTiposVigencia;
		String condicionwhere = "";
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs=null;
		try{
			if(campos.size()>0){
				if(!campos.get("institucion").equals("")){
					condicionwhere += (condicionwhere.equals("")?" WHERE":" AND") + " tv.institucion = "+campos.get("institucion").toString();
				}
				if(!campos.get("activo").equals("")){
					condicionwhere += (condicionwhere.equals("")?" WHERE":" AND") + " tv.activo = '"+campos.get("activo").toString()+"'";
				}
				if(!campos.get("tipo").equals("")){
					condicionwhere += (condicionwhere.equals("")?" WHERE":" AND") + " tv.tipo = '"+campos.get("tipo").toString()+"'";
				}
			}
			consulta += condicionwhere+" ORDER BY tv.nombre ";
			pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
			rs =  new ResultSetDecorator(pst.executeQuery(consulta));
			while(rs.next())
			{
				HashMap<String, Object> cargos = new HashMap<String, Object>();
				cargos.put("codigo", rs.getInt("codigo"));
				cargos.put("nombre", rs.getString("nombre"));
				cargos.put("activo", rs.getString("activo"));
				cargos.put("institucion", rs.getInt("institucion"));
				cargos.put("tipo", rs.getString("tipo"));
				arrayTipoVig.add(cargos);
			}
			logger.info("consulta >>>>>>>>>>>>>>>>>>>> "+consulta);
		}catch(Exception e){
			Log4JManager.error("ERROR obtenerTiposVigencia", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return arrayTipoVig;
	}
	
	/**
	 * Cadena que consulta los centros de costos de una unidad de agenda segun el tipo entida que ejecuta y el 
	 * codigo de la unidad de agenda
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static ArrayList<HashMap<String, Object>> obtenerCentroCosto(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> arrayCaentrosCostos = new ArrayList<HashMap<String, Object>>();
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs=null;
		try
		{
			pst = new PreparedStatementDecorator(con.prepareStatement(strCadenaConsultaCentroCostoTipo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
			pst.setInt(1, Utilidades.convertirAEntero(campos.get("institucion").toString()));
			pst.setInt(2, Utilidades.convertirAEntero(campos.get("centro_atencion").toString()));
			pst.setString(3, campos.get("tipo_entidad_ejecuta").toString());
			rs =  new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				HashMap<String, Object> cargos = new HashMap<String, Object>();
				cargos.put("codigo", rs.getInt("codigo"));
				cargos.put("nombre", rs.getString("nombre"));
				cargos.put("id", rs.getString("identificador"));
				arrayCaentrosCostos.add(cargos);
				logger.info("codigo >>> "+rs.getInt("codigo")+"  identificador >>>"+rs.getString("identificador")+" nombre >>>>"+rs.getString("nombre"));
			}
		}catch(Exception e){
			Log4JManager.error("ERROR obtenerCentroCosto", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return arrayCaentrosCostos;
	}
	
	
	/**
	 * Cadena que consulta los codigos de los centros de costos asociados a una unidad de consulta
	 * @param con
	 * @param campos
	 * @return ArrayList<HashMap<String, Object>>
	 */
	@SuppressWarnings("rawtypes")
	public static ArrayList<HashMap<String, Object>> obtenerCodCentroCostoXUnidadConsulta(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> arrayCodCenCosUniCon = new ArrayList<HashMap<String, Object>>();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			pst = con.prepareStatement(strCadenaConsutlaCodigosCentrosCiosto,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			pst.setInt(1, Utilidades.convertirAEntero(campos.get("unidad_consulta").toString()));
			rs =  pst.executeQuery();
			while(rs.next())
			{
				HashMap<String, Object> cargos = new HashMap<String, Object>();
				cargos.put("codigo_centro_costo", rs.getInt("centro_costo"));
				arrayCodCenCosUniCon.add(cargos);
			}
		}catch(Exception e){
			Log4JManager.error("ERROR obtenerCodCentroCostoXUnidadConsulta", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return arrayCodCenCosUniCon;
	}
	
	/**
	 * 
	 * @param ArrayList<HashMap<String, Object>>
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	private static String getCodCentrosCostos(ArrayList<HashMap<String, Object>> array)
	{
		String codCentroCostos = "";
		if(array != null && array.size()>0)
		{
			for(HashMap elem: array){
				try {
					codCentroCostos += elem.get("codigo_centro_costo").toString()+",";
				} catch (NullPointerException e) {
					codCentroCostos += elem.get("codigo").toString()+",";
				}
				 
			}
			codCentroCostos = codCentroCostos.substring(0, codCentroCostos.length()-1);
		}
		return codCentroCostos;
	}
	
	
	
	/**
	 * 
	 * @param objUsuario
	 * @param codigoInstitucion
	 * @return
	 */
	
	public static ArrayList<Usuario> obtenerUsuarios(Usuario objUsuario, int codigoInstitucion, boolean ordenarPorNombre ) {
		 ArrayList<Usuario> arrayUsuario = new ArrayList<Usuario>();
		
		 String consultaStr= "select  codigo as codigo, " +
		 		" us.login as login, " +
		 		" numero_identificacion as numeroIdentificacion, "+
		 		" tipo_identificacion as tipoIdentificacion, "+
		 		" codigo_departamento_nacimiento as codigoDepartamentoNacimiento,"+
		 		" codigo_ciudad_nacimiento  as codigoCiudadNacimiento, "+
		 		" tipo_persona as tipoPersona," +
		 		" fecha_nacimiento as fechaNacimiento, "+
		 		" estado_civil as estadoCivil, "+
		 		" sexo as sexo, "+
		 		" libreta_militar  as libretaMilitar, " +
		 		"primer_nombre as primerNombre," +
		 		"segundo_nombre as segundoNombre, " +
		 		"primer_apellido as primerApellido, " +
		 		"segundo_apellido as segundoApellido, " +
		 		" direccion as direccion, " +
		 		" codigo_departamento_vivienda as codigoDepartamentoVivienda, " +
		 		" codigo_ciudad_vivienda as codigo_ciudad_vivienda, " +
		 		" codigo_barrio_vivienda as codigo_barrio_vivienda," +
		 		" telefono as  telefono, " +
		 		" email as email,  " +
		 		" codigo_depto_id as codigoDepto_id,  " +
		 		" codigo_ciudad_id as codigoCiudad_id, " +
		 		" codigo_pais_vivienda as codigoPaisVivienda,  " +
		 		"codigo_localidad_vivienda as codigoLocalidadVivienda ," +
		 		"codigo_pais_id as codigo_pais_id ," +
		 		" codigo_pais_nacimiento as codigoPaisNacimiento, " +
		 		"telefono_celular as telefonoCelular,  " +
		 		"telefono_fijo as telefonoFijo" +
		 		" from " +
		 		"administracion.personas pe INNER JOIN  administracion.usuarios us on (pe.codigo=us.codigo_persona)" +
		 		"where us.login not in (select login from usuarios_inactivos where login = us.login) and us.institucion ="+codigoInstitucion;
		 		
			 logger.info("\n\n\n\n\n SQL obtener Usuarios / "+consultaStr);
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs=null;
		Connection con=null;
		 try 
			 {
				con = UtilidadBD.abrirConexion();
				
				String orderBy = " order by pe.primer_apellido, pe.segundo_apellido, pe.primer_nombre, pe.segundo_nombre ";
				if(ordenarPorNombre)
				{
					orderBy = " order by pe.primer_nombre, pe.segundo_nombre,pe.primer_apellido, pe.segundo_apellido ";
				}
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr+orderBy,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				rs=new ResultSetDecorator(ps.executeQuery());
				while(rs.next())
				{
					
					
					 Usuario objUs = new  Usuario();
					 
					 objUs.setCodigoPersona(rs.getInt("codigo"));
					 objUs.setLoginUsuario(rs.getString("login"));
					 objUs.setPrimerNombrePersona(rs.getString("primerNombre"));
					 objUs.setSegundoNombrePersona(rs.getString("segundoNombre"));
					 objUs.setPrimerApellidoPersona(rs.getString("primerApellido"));
					 objUs.setSegundoApellidoPersona(rs.getString("segundoApellido"));
					 objUs.setEmail(rs.getString("email"));
					 arrayUsuario.add(objUs);
				}
			}
			catch (SQLException e) 
			{
				logger.error("error en carga==> "+e);
			}
			finally{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			}
			
			return arrayUsuario;
	}
		
	
	/**
	 * 
	 * 
	 * 
	 * 
	 */
	
	public static String  obtenerEspecialidadesMedicoPorLogin(String login ) {
		 String retorna  = "";
		
		 String consultaStr= "select  esp.nombre as nombre " +
		 		
		 		" from administracion.especialidades esp " +
		 		"INNER JOIN  administracion.especialidades_medicos espm  on (espm.codigo_especialidad=esp.codigo)" +
		 		"INNER JOIN  administracion.medicos me  on (me.codigo_medico=espm.codigo_medico)" +
		 		"INNER JOIN  administracion.personas pe  on (me.codigo_medico=pe.codigo)" +
		 		"INNER JOIN  administracion.usuarios usu  on (usu.codigo_persona=pe.codigo)" +
		 		
		 		"where usu.login ='"+login+"'";
		 		
		 Connection con=null;
		 PreparedStatementDecorator pst=null;
		 ResultSetDecorator rs=null;
		 try 
			 {
				con = UtilidadBD.abrirConexion();
				pst =  new PreparedStatementDecorator(con.prepareStatement(consultaStr+" order by primer_nombre ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				rs=new ResultSetDecorator(pst.executeQuery());
				boolean esPrimero=true;
				while(rs.next())
				{
					retorna+=(!esPrimero)?" - ":"";					
					retorna+=rs.getString("nombre"); 
					esPrimero=false;
				}
				
				
			}
			catch (SQLException e) 
			{
				logger.error("error en obtenerEspecialidadesMedicoPorLogin==> "+e);
			}
			finally{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(pst, rs, con);
			}
			
			return retorna;
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
		ResultadoBoolean resultado = new ResultadoBoolean(false);
		int cuenta = 0;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta = "SELECT count(1) as cuenta from administracion.usuarios WHERE login = ? and password = ?";
			pst = con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setString(1,login);
			if(mismoProfesional)
			{
				pst.setString(2,password);
			}
			else
			{
				pst.setString(2,MD5Hash.hashPassword(password));
			}
			
			rs = pst.executeQuery();
			if(rs.next())
			{
				cuenta = rs.getInt("cuenta");
			}
			
			if(cuenta>0)
			{
				resultado.setResultado(true);
			}
			else
			{
				resultado.setResultado(false);
				resultado.setDescripcion("No existe usuario para el login y password ingresado. Por favor verifique");
			}
			
		}
		catch(Exception e){
			Log4JManager.error("ERROR existeUsuario", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return resultado;
	}
	
	
	
	
	/**
	 * 
	 * METODO QUE CARGAR UN DTO PERSONA 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static DtoPersonas cargarPersonas(DtoUsuario dtoUsuario )
	{
		
		
		DtoPersonas dtoPersona= new DtoPersonas();
		
		String consultaStr 
				=
				" select " +
				" pe.primer_nombre as primerNombre," +
				" pe.segundo_nombre as segundoNombre," +
				" pe.primer_apellido as primerApellido," +
				" pe.segundo_apellido as segundoApellido ,"+
				" pe.tipo_identificacion as tipoIdentificacion," +
				" (select ti.nombre from  administracion.tipos_identificacion ti where ti.acronimo=pe.tipo_identificacion) as nombreTipoIdentificacion , "+
				" pe.tipo_persona as tipoPesona," +
				" pe.numero_identificacion as numeroIdentificacion " +
				
				" from  administracion.personas pe   " +
					" INNER JOIN " +
				" administracion.usuarios us ON (us.codigo_persona=pe.codigo )" +
				
				" where 1=1  ";
	
		
		if(!UtilidadTexto.isEmpty(dtoUsuario.getLogin()))
		{
			consultaStr+= " and us.login='"+dtoUsuario.getLogin()+"'";
		}
		
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs=null;
		Connection con=null;
		try 
		 {
			con = UtilidadBD.abrirConexion();
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				dtoPersona.setPrimerApellido(rs.getString("primerApellido"));
				dtoPersona.setSegundoApellido(rs.getString("segundoApellido"));
				dtoPersona.setPrimerNombre(rs.getString("primerNombre"));
				dtoPersona.setSegundoNombre(rs.getString("segundoNombre"));
				dtoPersona.setNombreTipoIdentificacion("nombreTipoIdentificacion");
			}
		}
		catch (SQLException e) 
		{
			logger.error("error en cargarPersonas==> "+e);
		}
		finally{
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}		
		return dtoPersona;
		
	}

	public static ArrayList<DtoProfesional> listarProfesionales(ArrayList<Integer> ocupaciones, boolean filtrarActivos) 
	{
		Connection con=null;
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs=null;
		ArrayList<DtoProfesional> resultado=new  ArrayList<DtoProfesional>();
		String consulta=" SELECT " +
								" med.codigo_medico as codigomedico," +
								" per.tipo_identificacion as tipoid," +
								" per.numero_identificacion as numeroid," +
								" per.primer_nombre as primernombre," +
								" per.segundo_nombre as segundonombre," +
								" per.primer_apellido as primerapellido," +
								" per.segundo_apellido as segundoapellido" +
						" from medicos med " +
						" inner join personas per on (med.codigo_medico=per.codigo) " +
						" where 1=1 "; 
		if(filtrarActivos)
			consulta=consulta+" and per.codigo not in (select codigo_medico from medicos_inactivos)";
		if(ocupaciones.size()>0)
		{
			consulta=consulta+" and med.ocupacion_medica in (";
			int temporal=0;
			for(Integer ocupacion:ocupaciones)
			{
				if(temporal>0)
					consulta=consulta+",";
				
				consulta=consulta+" "+ocupacion;
				temporal++;				
			}
			consulta=consulta+")";
		}
		consulta=consulta+" order by per.primer_apellido, per.segundo_apellido, per.primer_nombre,per.segundo_nombre";
		try
		{
			con=UtilidadBD.abrirConexion();
			pst=new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs=new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				DtoProfesional profesional=new DtoProfesional();
				profesional.setCodigo(rs.getInt("codigomedico"));
				profesional.setTipoIdentificacion(rs.getString("tipoid"));
				profesional.setNumeroIdentificacion(rs.getString("numeroid"));
				profesional.setPrimerNombre(rs.getString("primernombre"));
				profesional.setSegundoNombre(rs.getString("segundonombre"));
				profesional.setPrimerApellido(rs.getString("primerapellido"));
				profesional.setSegundoApellido(rs.getString("segundoapellido"));
				resultado.add(profesional);
			}
		}
		catch(SQLException e)
		{
			Log4JManager.error("ERROR listarProfesionales",e);
		}
		finally
		{
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(pst, rs, con);
			
		}
		
		return resultado;
	}

	/**
	 * Obtener los profesionales por centro de atención
	 * @param con Conexión con la BD
	 * @param codigoCentroAtencion Código del centro de atención a filtrar
	 * @return Listado de {@link DtoProfesional} con el resultado de los profesionales
	 */
	public static ArrayList<DtoProfesional> obtenerProfesionalesXCentroAtencion(Connection con, int codigoCentroAtencion)
	{
		String sentencia=
					"SELECT distinct " +
						"m.codigo_medico AS codigo_medico, " +
						"p.numero_identificacion AS numero_identificacion, " +	 			
						"p.tipo_identificacion AS tipo_identificacion, " +
						"p.codigo_departamento_nacimiento AS codigo_departamento_nacimiento, " +
						"p.codigo_ciudad_nacimiento AS codigo_ciudad_nacimiento, " +
						"p.tipo_persona AS tipo_persona, " +
						"p.fecha_nacimiento AS fecha_nacimiento, " +
						"p.estado_civil AS estado_civil, " +
						"p.sexo AS sexo, " +
						"p.libreta_militar AS libreta_militar, " +
						"p.primer_nombre AS primer_nombre, " +
						"p.segundo_nombre AS segundo_nombre, " +
						"p.primer_apellido AS primer_apellido, " +
						"p.segundo_apellido AS segundo_apellido " +
					"FROM " +
						"administracion.centros_costo_usuario ccu " +
					"INNER JOIN " +
						"administracion.usuarios u " +
						"ON (u.login=ccu.usuario)" +
					"INNER JOIN " +
						"administracion.medicos m " +
						"ON (m.codigo_medico=u.codigo_persona) " +
					"INNER JOIN " +
						"administracion.centros_costo cc " +
						"ON (cc.codigo=ccu.centro_costo) " +
					"INNER JOIN " +
						"administracion.personas p " +
						"ON (p.codigo=m.codigo_medico) " +
					"WHERE " +
						"cc.centro_atencion=? " +
						"order by primer_apellido, segundo_apellido, primer_nombre, segundo_nombre ";
		PreparedStatementDecorator psd=null;
		ResultSetDecorator resultado=null;
		try
		{
			psd=new PreparedStatementDecorator(con.prepareStatement(sentencia, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			psd.setInt(1, codigoCentroAtencion);
			resultado=new ResultSetDecorator(psd.executeQuery());
			ArrayList<DtoProfesional> listaProfesionales=new ArrayList<DtoProfesional>();
			while(resultado.next())
			{
				DtoProfesional profesional=new DtoProfesional();
				profesional.setCodigo(resultado.getInt("codigo_medico"));
				profesional.setPrimerNombre(resultado.getString("primer_nombre"));
				profesional.setTipoIdentificacion(resultado.getString("tipo_identificacion"));
				profesional.setNumeroIdentificacion(resultado.getString("numero_identificacion"));
				profesional.setSegundoNombre(resultado.getString("segundo_nombre"));
				profesional.setPrimerApellido(resultado.getString("primer_apellido"));
				profesional.setSegundoApellido(resultado.getString("segundo_apellido"));
				
				listaProfesionales.add(profesional);
			}
			return listaProfesionales;
		} catch(Exception e){
			Log4JManager.error("ERROR obtenerProfesionalesXCentroAtencion", e);
		}
		finally{
			try{
				if(resultado != null){
					resultado.close();
				}
				if(psd != null){
					psd.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public static int procesoInactivacionUsuario() 
	{
		Log4JManager.info("INACTIVANDO LOS USUARIOS");
		String consulta="SELECT login as login,dias_inactivar_usuario as dias,to_char(fecha_ultima_activa_usu,'dd/mm/yyyy') as fechaactivacion from usuarios where activo='"+ConstantesBD.acronimoSi+"' and dias_inactivar_usuario is not null order by login";
		Connection con = null;
		int usuarios=0;
		
		try
		{
			con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =new PreparedStatementDecorator(con,consulta);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			DtoProcesoInactivacionUsuario dtoUsuario = null;
			while(rs.next())
			{
				String fechaActivacion=rs.getString("fechaactivacion");
				Log4JManager.info("VERIFICANDO USUARIO -->"+rs.getString("login"));
				String subConsulta="SELECT to_char(fecha_login,'yyyy-mm-dd') as fecha from login_usuarios where login='"+rs.getString("login")+"' order by fecha_login desc";
				PreparedStatementDecorator psInterno=new PreparedStatementDecorator(con,subConsulta);
				ResultSetDecorator rsInterno=new ResultSetDecorator(psInterno.executeQuery());
				if(rsInterno.next())
				{
					String fechaActual=UtilidadFecha.getFechaActual(con);
					String fechaEvaluar=UtilidadFecha.conversionFormatoFechaAAp(rsInterno.getString("fecha"));

					if(!UtilidadTexto.isEmpty(fechaEvaluar)&&!UtilidadTexto.isEmpty(fechaActivacion))
					{
						if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaEvaluar, fechaActivacion))
						{
							fechaEvaluar = fechaActivacion;
						}
					}
					
					
					int numDias=rs.getInt("dias");
					String fechaIncrementada=UtilidadFecha.incrementarDiasAFecha(fechaEvaluar, numDias, false);
					Log4JManager.info("FECHA EN QUE SE DEBE INACTIVAR EL USUARIO -->"+fechaIncrementada);
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaIncrementada, fechaActual))
					{
						try {
							if(inactivarUsuario(con,rs.getString("login")))
							{
								usuarios++;
								dtoUsuario = new DtoProcesoInactivacionUsuario();

								dtoUsuario.setFechaEjecucion(UtilidadFecha.getFechaActualTipoBD());
								dtoUsuario.setHoraEjecucion(UtilidadFecha.getHoraActual());
								dtoUsuario.setExitoso(ConstantesBD.acronimoSi);
								dtoUsuario.setCantidad(1);

								//INSERTAMOS EL LOG PARA PROFESIONALES DE LA SALUD A LOS CUALES NO SE LES VA A GENERAR VALOR DE HONORARIOS
								insertarLogProcesoInactivacionUsuario(con,dtoUsuario);
							}
						} catch (Exception e) {
							Log4JManager.error("El usuario "+ rs.getString("login") +" ya se encuentra en la lista de usuarios Inactivos", e);
							LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, "El usuario "+ rs.getString("login") +" ya se encuentra en la lista de usuarios Inactivos", ConstantesBD.tipoRegistroLogInsercion, "Sistema");
						}
					}
				}
				rsInterno.close();
				psInterno.close();
			}
			rs.close();
			ps.close();
		}
		catch(Exception e)
		{
			Log4JManager.error("error",e);
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		return usuarios;
	}

	/**
	 * Método encargado de insertar el log para los usuarios que se van a inactivar.
	 * 
	 * @param con  Conexión a la base de datos
	 * @param dtoUsuario  Dto que contiene la información del log
	 * 
	 * @author Luis Fernando Hincapié Ospina
	 * @since 15/01/2011
	 */
	public static void insertarLogProcesoInactivacionUsuario(Connection con,
			DtoProcesoInactivacionUsuario dtoUsuario) {

		try{
			HibernateUtil.beginTransaction();
			LogProcesoInactivacionUsu logProcesoInactivacionUsu = new LogProcesoInactivacionUsu(
					dtoUsuario.getFechaEjecucion(), 
					dtoUsuario.getHoraEjecucion(), 
					dtoUsuario.getExitoso(), 
					dtoUsuario.getCantidad());
			ILogProcesoInactivacionUsuDAO logProcesoInactivacionUsuHibernateDAO = AdministracionFabricaDAO
					.crearLogProcesoInactivacionUsuarioDAO();
			logProcesoInactivacionUsuHibernateDAO.insertar(logProcesoInactivacionUsu);
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error("ERROR ", e);
			HibernateUtil.abortTransaction();
		}

	}
	
	
	
	
	/**
	 * 
	 * @param con
	 * @param loginUsuario 
	 * @return
	 */
	public static boolean inactivarUsuario(Connection con, String loginUsuario) throws Exception 
	{
		boolean resultado = false;
		PreparedStatementDecorator ps = null; 
		try {
			String consulta="UPDATE usuarios set activo='"+ConstantesBD.acronimoNo+"', fecha_ultima_inact_usu=current_date where login='"+loginUsuario+"'";
			ps = new PreparedStatementDecorator(con,consulta);
			resultado = ps.executeUpdate()>0;
			ps.close();
			HibernateUtil.beginTransaction();
			//insertar el log
			LogActiinactiUsuarios dto=new LogActiinactiUsuarios();
			LogActiinactiUsuariosHome dao=new LogActiinactiUsuariosHome();
			dto.setEstadoUsuario(ConstantesIntegridadDominio.acronimoEstadoInactivo);
			dto.setFechaProceso(UtilidadFecha.getFechaActualTipoBD());
			dto.setHoraProceso(UtilidadFecha.getHoraActual(con));
			dto.setTipoInactivacion(ConstantesIntegridadDominio.acronimoAutomatica);
			Usuarios usu=new Usuarios();
			UsuariosDelegate usuDao=new UsuariosDelegate();
			usu=usuDao.findById(loginUsuario);
			dto.setUsuariosByLoginUsuario(usu);
			dao.persist(dto);
		
			if(usu.getPersonas() !=null && usu.getInstituciones() != null)
			{
				Medicos med = new Medicos();
//				MedicosDelegate medDao=new MedicosDelegate();
//				med=medDao.findById(usu.getPersonas().getCodigo());
				med.setCodigoMedico(usu.getPersonas().getCodigo());
//				InstitucionDelegate instDelegate = new InstitucionDelegate();
//				inst = instDelegate.findById(usu.getInstituciones().getCodigo());
				if(med != null && med.getCodigoMedico() > 0) { 
					try {
						Instituciones inst = new Instituciones();
						inst.setCodigo(usu.getInstituciones().getCodigo());
						MedicosInactivos medIna=new MedicosInactivos();
						MedicosInactivosHome medInaDao=new MedicosInactivosHome();
						MedicosInactivosId medInaID=new MedicosInactivosId();
						medInaID.setCodigoInstitucion(inst.getCodigo());
						medInaID.setCodigoMedico(med.getCodigoMedico());
						medIna.setId(medInaID);
						medIna.setInstituciones(inst);
						medIna.setMedicos(med);
						Log4JManager.info("--------------- codMed: " + med.getCodigoMedico() + " inst: " + inst.getCodigo());
						medInaDao.merge(medIna);
						UtilidadTransaccion.getTransaccion().flush();
					} catch (Exception e) {
						Log4JManager.error("INACTIVANDO USUARIO : " + loginUsuario + " : " + e);
						LogsAxioma.enviarLog(ConstantesBD.logParametrosGeneralesCodigo, "El médico "+ loginUsuario +" ya se encuentra en la lista de Medicos Inactivos", ConstantesBD.tipoRegistroLogInsercion, "Sistema");
					}
				}
			}

			//hacer incser en inactivacion usuarios.
			UsuariosInactivos usuInac=new UsuariosInactivos();
			UsuariosInactivosHome usuInacDao=new UsuariosInactivosHome();
			UsuariosInactivosId usuID=new UsuariosInactivosId();
			usuID.setCodigoInstitucion(usu.getInstituciones().getCodigo());
			usuID.setLogin(usu.getLogin());
			usuInac.setId(usuID);
			usuInac.setInstituciones(usu.getInstituciones());
			usuInac.setUsuarios(usu);
			usuInac.setPassword(usu.getPassword());
			usuInacDao.persist(usuInac);
			HibernateUtil.endTransaction();
		} catch (Exception e) {
			Log4JManager.error("INACTIVANDO USUARIO : " + e);
			HibernateUtil.abortTransaction();
		} 
		return resultado;
	}
	
	/**
	 * 
	 * @param con
	 * @param loginUsuario
	 * @return
	 */
	public static boolean inactivarPasswordUsuario(Connection con,String loginUsuario) throws Exception 
	{
		boolean resultado = false;
		try {
			String consulta="UPDATE usuarios set password_activo='"+ConstantesBD.acronimoNo+"', fecha_ultima_caducidad_passwd=current_date where login='"+loginUsuario+"'";
			PreparedStatement ps=con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			resultado = ps.executeUpdate()>0;
			ps.close();

			if(loginUsuario != null)
			{
				try{
					HibernateUtil.beginTransaction();
					Usuarios usu = new Usuarios();
					usu.setLogin(loginUsuario);
					LogCadupwdUsuarios dto=new LogCadupwdUsuarios();
					LogCadupwdUsuariosHome dao=new LogCadupwdUsuariosHome();
					if(UtilidadTexto.getBoolean(usu.getActivo()+""))
						dto.setEstadoUsuario(ConstantesIntegridadDominio.acronimoEstadoActivo);
					else
						dto.setEstadoUsuario(ConstantesIntegridadDominio.acronimoEstadoInactivo);
					dto.setFechaProceso(UtilidadFecha.getFechaActualTipoBD());
					dto.setHoraProceso(UtilidadFecha.getHoraActual());
					dto.setEsactivacion(ConstantesBD.acronimoNo);
					dto.setTipoInactivacion(ConstantesIntegridadDominio.acronimoAutomatica);
					dto.setUsuariosByLoginUsuario(usu);
					dao.merge(dto);
					HibernateUtil.endTransaction();
				}
				catch (Exception e) {
					Log4JManager.error("ERROR inactivarPasswordUsuario", e);
					HibernateUtil.abortTransaction();
				}
			}
		} catch (Exception e) {
			Log4JManager.error("INACTIVANDO USUARIO : " + e);
		}
		return resultado;
	}

	/**
	 * 
	 * @return
	 */
	public static int procesoCaducidadPassword()  
	{
		Log4JManager.info("INICIA PROCESO DE CADUCIDAD DE PASSWORD");
		String consulta="SELECT login as login,dias_caducidad_password as dias,to_char(fecha_ultima_activa_passwd,'dd/mm/yyyy') as fechaactivacion from usuarios where password_activo='"+ConstantesBD.acronimoSi+"' and dias_caducidad_password is not null";
		Connection con = null;
		int usuarios=0;
		
		try
		{
			con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con,consulta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				String fechaActivacion=rs.getString("fechaactivacion");
				String subConsulta="SELECT to_char(fecha_login,'yyyy-mm-dd') as fecha from login_usuarios where login='"+rs.getString("login")+"' order by fecha_login desc";
				PreparedStatementDecorator psInterno=new PreparedStatementDecorator(con,subConsulta);
				ResultSetDecorator rsInterno=new ResultSetDecorator(psInterno.executeQuery());
				Log4JManager.info("ANALIZANDO CADUCIDAD DE PASSWORD PARA EL USUARIO "+rs.getString("login"));
				if(rsInterno.next())
				{
					String fechaActual=UtilidadFecha.getFechaActual(con);
					String fechaEvaluar=UtilidadFecha.conversionFormatoFechaAAp(rsInterno.getString("fecha"));
					int numDias=rs.getInt("dias");
					
					if(!UtilidadTexto.isEmpty(fechaEvaluar)&&!UtilidadTexto.isEmpty(fechaActivacion))
					{
						if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaEvaluar, fechaActivacion))
						{
							fechaEvaluar=fechaActivacion;
						}
					}
					
					String fechaIncrementada=UtilidadFecha.incrementarDiasAFecha(fechaEvaluar, numDias, false);
					Log4JManager.info("FECHA CADUCIDAD DE PASSWORD PARA EL USUARIO "+fechaIncrementada);
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaIncrementada, fechaActual))
					{
						if(inactivarPasswordUsuario(con,rs.getString("login")))
						{
							usuarios++;
						}
					}
				}
				rsInterno.close();
				psInterno.close();
			}
			rs.close();
			ps.close();
		} catch(Exception e) {
			Log4JManager.error("error",e);
		} finally {
			UtilidadBD.closeConnection(con);
		}
		return usuarios;
	}

	/**
	 * 
	 * @param login
	 * @return
	 */
	public static String fechaUltimoLoginUsuario(String login) 
	{
		String resultado="";
		String subConsulta="SELECT to_char(fecha_login,'yyyy-mm-dd') as fecha from login_usuarios where login='"+login+"' order by fecha_login desc";
		Connection con=UtilidadBD.abrirConexion();
		PreparedStatementDecorator psInterno=new PreparedStatementDecorator(con,subConsulta);
		try
		{
			ResultSetDecorator rsInterno=new ResultSetDecorator(psInterno.executeQuery());
			if(rsInterno.next())
			{
				resultado=rsInterno.getString("fecha");
			}
			rsInterno.close();
			psInterno.close();
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

	public static ArrayList<Integer> obtenerFuncionalidadHija(Connection con,
			int codigoFuncionalidad) {
		ArrayList<Integer> resultado = new ArrayList<Integer>();
		String subConsulta="select funcionalidad_hija from dependencias_func where funcionalidad_padre="+codigoFuncionalidad;
		PreparedStatementDecorator psInterno = null;
		ResultSetDecorator rsInterno = null;
		try	{
			psInterno = new PreparedStatementDecorator(con,subConsulta);
			rsInterno = new ResultSetDecorator(psInterno.executeQuery());
			while(rsInterno.next())	{
				resultado.add(rsInterno.getInt(1));
			}
		}
		catch(Exception e) {
			Log4JManager.error("obtenerFuncionalidadHija: ",e);
		} finally {
			try {
				if(rsInterno != null) {
					rsInterno.close();
				}
				if(psInterno != null) {
					psInterno.close();
				}
			} catch (SQLException e) {
				Log4JManager.error("Error cerrando PreparedStament " + e);
			}
		}
		
		return resultado;
	}

	
}