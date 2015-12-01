package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.UtilidadBD;
import util.Utilidades;

public class SqlBaseParametrizacionPlantillasDao 
{

	
	/**
	 * 
	 */
	public static Logger logger=Logger.getLogger(SqlBaseParametrizacionPlantillasDao.class);
	
	private static final int tipoElementoSeccion = 1;
	private static final int tipoElementoComponente = 2;
	private static final int tipoElementoEscala = 3;
	
	
	/**
	 * 
	 */
	public static String consultaEspecialidadesStr="SELECT e.codigo as codigo, e.nombre as descripcion, p.codigo_pk as codigoplantilla, 'BD' as tiporegistro FROM especialidades e inner join plantillas p on(p.especialidad=e.codigo) WHERE p.fun_param="+ConstantesCamposParametrizables.funcParametrizableValoracionConsulta+" ";
	
	
	public static String consultaPlantillasStr="SELECT * FROM "+ 
	"( "+
	"( "+ 
		"SELECT "+ 
		"ps.codigo_pk AS consecutivo, "+
		tipoElementoSeccion+" AS tipo_elemento, "+ //código para identificar una sección
		"sp.orden AS orden, "+
		"sp.codigo_pk AS codigo_pk, "+
		"coalesce(sp.codigo||'','') AS codigo, "+
		"coalesce(sp.descripcion,'') AS descripcion, "+
		"coalesce(sp.columnas_seccion,10) AS columnas_seccion, "+ //se consultan el N° de columnas de la seccion
		"'"+ConstantesBD.acronimoNo+"'  AS observaciones_requeridas, " +
		"sp.mostrar AS mostrar, " +
		"sp.mostrar_modificacion AS mostrar_modificacion "+
		"FROM plantillas_secciones ps "+ 
		"INNER JOIN secciones_parametrizables sp ON(sp.codigo_pk=ps.seccion) "+ 
		"WHERE ps.plantilla_sec_fija = ? AND sp.seccion_padre is null "+ //se consulta la seccion de más alto nivel
	") "+
	"UNION "+
	"( "+
		"SELECT "+ 
		"pc.codigo_pk AS consecutivo, "+
		tipoElementoComponente+" AS tipo_elemento, "+ //código para identificar un componente
		"pc.orden AS orden, "+
		"c.codigo AS codigo_pk, "+
		"tc.codigo ||'' AS codigo, "+ //el codigo es la constante del tipo de componente
		"tc.nombre AS descripcion, "+ //la descripcion es el nombre del tipo de componente
		"0 AS columnas_seccion, "+ 
		"'"+ConstantesBD.acronimoNo+"' AS observaciones_requeridas, " +
		"pc.mostrar AS mostrar, " +
		"c.activo AS mostrar_modificacion "+ 
		"FROM plantillas_componentes pc "+ 
		"INNER JOIN componentes c ON(c.codigo=pc.componente) "+ 
		"INNER JOIN tipos_componente tc ON(tc.codigo=c.tipo_componente) "+
		"WHERE pc.plantilla_sec_fija = ? "+ 
	") "+
	"UNION "+
	"( "+ 
		"SELECT "+ 
		"pe.codigo_pk as consecutivo, "+
		tipoElementoEscala+" as tipo_elemento, "+ //código para identificar una escala
		"pe.orden AS orden, "+ 
		"e.codigo_pk AS codigo_pk, "+
		"e.codigo AS codigo, "+
		"e.nombre AS descripcion, "+
		"0 AS columnas_seccion, "+
		"e.observaciones_requeridas AS observaciones_requeridas, " +
		"pe.mostrar AS mostrar, " +
		"e.mostrar_modificacion AS mostrar_modificacion "+ 
		"FROM plantillas_escalas pe "+ 
		"INNER JOIN escalas e ON(e.codigo_pk=pe.escala) "+
		"WHERE pe.plantilla_sec_fija = ? "+
	") "+ 
") t ORDER BY t.orden"; 
	
	
	private static final String strCargarSeccionesFijasPlantilla = "SELECT "+
																	"coalesce(ps.codigo_pk||'','') AS codigo_pk, "+
																	"sf.codigo AS codigoseccion, "+
																	"sf.nombre AS nombreseccion, "+
																	"coalesce(ps.orden,fp.orden) AS orden, " +
																	"coalesce(ps.mostrar,'') AS mostrar," +
																	"fp.activo AS mostrar_modificacion "+
																	"FROM fun_param_secciones_fijas fp "+ 
																	"INNER JOIN secciones_fijas sf ON(sf.codigo=fp.seccion_fija) "+ 
																	"LEFT OUTER JOIN plantillas_sec_fijas ps ON(ps.fun_param_secciones_fijas = fp.codigo_pk) "+
																	"WHERE fp.fun_param = ? AND fp.institucion = ? ORDER BY orden";
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap consultarEspecialidades(Connection con) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps= null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaEspecialidadesStr));
			
			
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseMotivosSircDao "+sqlException.toString() );
				}
			}		
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param plantilla
	 * @param centroCosto
	 * @param sexo
	 * @return
	 */
	public static HashMap consultarPlantillas(Connection con, String plantilla, String centroCosto, String sexo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		String cadena=consultaPlantillasStr;
		PreparedStatementDecorator busqueda=null;
		
		cadena+=" WHERE fun_param="+plantilla;
		
		if(!centroCosto.equals(""))
		{
			cadena+=" AND centro_costo="+centroCosto;
		}
		if(!sexo.equals(ConstantesBD.codigoSexoAmbos+""))
		{
			cadena+=" AND sexo="+sexo;
		}
		if(centroCosto.equals(""))
		{
			cadena+=" AND centro_costo is null" +"";
		}
		if(sexo.equals(ConstantesBD.codigoSexoAmbos+""))
		{
			cadena+=" AND sexo is null" +"";
		}
		
		
		try
		{
			busqueda= new PreparedStatementDecorator(con.prepareStatement(cadena));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(busqueda.executeQuery()));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(busqueda!=null){
				try{
					busqueda.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseMotivosSircDao "+sqlException.toString() );
				}
			}		
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param plantillaBase
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap consultaSeccionesFijas(Connection con, String plantillaBase, String codigoInstitucion) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps= null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(strCargarSeccionesFijasPlantilla));
			ps.setInt(1, Utilidades.convertirAEntero(plantillaBase));
			ps.setInt(2, Utilidades.convertirAEntero(codigoInstitucion));
			
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseMotivosSircDao "+sqlException.toString() );
				}
			}		
		}
		return mapa;
	}

}
