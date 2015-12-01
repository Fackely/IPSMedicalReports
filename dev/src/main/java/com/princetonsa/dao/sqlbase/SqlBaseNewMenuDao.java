package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoFuncionalidad;
import com.princetonsa.dto.administracion.DtoModulo;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;

public class SqlBaseNewMenuDao{
	
	/**
	 * Objeto para manejar log de la clase  
	 * */
	private static Logger logger = Logger.getLogger(SqlBaseNewMenuDao.class);
	
	/**
	 * Cadena SQL para cargar el menu segun los roles del usuario
	 */
	private static String strCargar =	"SELECT " +
											"m.nombre as nombremod, " +
											"m.codigo as codigomod, " +
											"f.codigo_func AS codigofun, " +
											"f.etiqueta_func AS etiquetafun, " +
											"f.archivo_func AS archivofun " +
										"FROM " +
											"administracion.funcionalidades f " +
										"INNER JOIN " +
											"administracion.roles_funcionalidades rf ON (rf.codigo_func=f.codigo_func) " +
										"INNER JOIN " +
											"administracion.roles_usuarios ru ON (ru.nombre_rol=rf.nombre_rol) " +
										"INNER JOIN " +
											"administracion.dep_modulo_func dmf ON (dmf.funcionalidad=f.codigo_func) " +
										"INNER JOIN " +
											"administracion.modulos m ON (m.codigo=dmf.modulo) " +
										"WHERE " +
											"f.debo_imprimir="+util.ValoresPorDefecto.getValorTrueParaConsultas()+" and ru.login=? " +
										"ORDER BY " +
											"m.nombre, f.etiqueta_func";
	
	/**
	 * 
	 * @param con
	 * @param loginUsuario
	 * @return
	 */
	public static ArrayList<DtoModulo> cargar(Connection con, String loginUsuario){
		
		ArrayList<DtoModulo> modulos = new ArrayList<DtoModulo>();
		HashMap mapa = new HashMap();
		
		try
		{
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(strCargar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, loginUsuario);
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
		}
		catch (SQLException e)
		{
			logger.info("ERROR", e);
		}
		
		Utilidades.imprimirMapa(mapa);
		
		int numRegistros = Utilidades.convertirAEntero(mapa.get("numRegistros").toString());
		String modulo="";
		for(int i=0; i<numRegistros; i++){
			if(!modulo.equals(mapa.get("codigomod_"+i)+"")){
				
				modulo = mapa.get("codigomod_"+i)+"";
				
				// Cargar info modulo
				DtoModulo dtoModulo = new DtoModulo();
				dtoModulo.setCodigo(Utilidades.convertirAEntero(mapa.get("codigomod_"+i)+""));
				dtoModulo.setNombre(mapa.get("nombremod_"+i)+"");
				
				for(int j=0; j<numRegistros; j++){
					if(modulo.equals(mapa.get("codigomod_"+j)+"")){
						
						// Cargar info funcionalidad
						DtoFuncionalidad dtoFun = new DtoFuncionalidad();
						dtoFun.setCodigo(Utilidades.convertirAEntero(mapa.get("nombremod_"+j)+""));
						dtoFun.setEtiqueta(mapa.get("etiquetafun_"+j)+"");
						dtoFun.setArchivo(mapa.get("archivofun_"+j)+"");
						dtoModulo.getFuncionalidades().add(dtoFun);
						
					}
				}
				
				modulos.add(dtoModulo);
				
				logger.info("numFuncXModulo "+dtoModulo.getFuncionalidades().size());
			}
			modulo = mapa.get("codigomod_"+i)+"";
		}
		
		logger.info("numModulos "+modulos.size());
		
		return modulos;
	}

}