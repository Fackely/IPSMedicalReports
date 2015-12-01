package com.sies.dao.sqlbase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

import util.UtilidadBD;

public class SqlBaseUtilidadSiEsDao
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseUtilidadSiEsDao.class);
	
	/**
	 * Sentencia para listar las personas activas del sistema pertenecientes a la una institución
	 * pasada por parámetros
	 */
    private final static String consultarPersonasActivasStr=
	    	"SELECT " +
	    		"getNombrePersonaSiEs(per.codigo) AS nombres, " +
	    		"med.codigo_medico AS codigo_medico " +
	    	"FROM personas per " +
	    	"INNER JOIN medicos med ON(per.codigo=med.codigo_medico) " +
	    	"INNER JOIN medicos_instituciones medins on(medins.codigo_medico=med.codigo_medico) " +
	    	"WHERE " +
	    		"medins.codigo_institucion=? " +
	    	"AND " +
	    		"med.codigo_medico NOT IN (SELECT codigo_medico FROM medicos_inactivos) " +
	    	"ORDER BY " +
	    		"nombres";

    /**
     * Método para listar las personas activas del sistema pertenecientes a la una institución
	 * pasada por parámetros
     * @param con
     * @param codigoInstitucion
     * @return
     */
    public Collection<HashMap<String, Object>> consultarPersonasActivas(Connection con, int codigoInstitucion)
    {
    	try
		{
			PreparedStatementDecorator stm=new PreparedStatementDecorator(con, consultarPersonasActivasStr);
			stm.setInt(1, codigoInstitucion);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(stm.executeQuery()));
		}
    	catch (SQLException e)
		{
    		logger.error("Error consultando las personas activas para la institución "+codigoInstitucion+" e: "+e);
    		return null;
		}
    }
    
	/**
	 * Método para consultar el nombre de la persona
	 * Adicionalmente agrega codigo y teléfono al listado
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public static Collection<HashMap<String, Object>> consultarNombrePersona(Connection con, int codigoPersona)
	{
		try
		{
			PreparedStatementDecorator stm=new PreparedStatementDecorator(con, "SELECT per.codigo AS codigo, getnombrepersonasies(per.codigo) AS nombre, telefono AS telefono FROM personas per WHERE codigo=?");
			stm.setInt(1, codigoPersona);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(stm.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando el nombre de la persona "+codigoPersona+": "+e);
			return null;
		}
	}

	/**
	 * Método que busca un listado de personas por nombre (criterio=false) o por código (criterio=true)
	 * @param textoBusqueda
	 * @param criterio
	 * @param institucion Institución
	 * @param centroCosto Centro de Costo del Usuario
	 * @param centroAtencion Centro de Atención del Usuario
	 * @param connection
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Collection<HashMap<String, Object>> busquedaAvanzadaPersonas(Connection con, String textoBusqueda, boolean criterio, int institucion, int centroCosto, int centroAtencion)
	{
		String consulta="";

		String[] palabras=textoBusqueda.split(" ");
		if(textoBusqueda!=null && !textoBusqueda.equals(""))
		{
			if(criterio)
			{
				consulta="SELECT " +
								"DISTINCT " +
								"usu.codigo_persona AS codigo, " +
								"getnombrepersonasies(usu.codigo_persona) AS nombre " +
							"FROM " +
								"usuarios usu " +
							"INNER JOIN " +
								"personas per " +
								"ON(per.codigo=usu.codigo_persona) " +
							"WHERE " +
								"usu.login NOT IN(SELECT ui.login FROM usuarios_inactivos ui) "+
							"AND " +
								"per.numero_identificacion=? ";
/*
				consulta="SELECT " +
								"usu.codigo_persona AS codigo, " +
								"getnombrepersonasies(usu.codigo_persona) AS nombre " +
							"FROM " +
								"usuarios usu " +
							"INNER JOIN " +
								"personas per " +
								"ON(per.codigo=usu.codigo_persona) " +
							"INNER JOIN " +
								"centros_costo_usuario ccu " +
								"ON(ccu.usuario=usu.login) " +
							"iNNER JOIN " +
								"centros_costo cc " +
								"ON(cc.codigo=ccu.centro_costo)" +
							"WHERE " +
								"usu.login NOT IN(SELECT ui.login FROM usuarios_inactivos ui) "+
							" AND " +
								"per.numero_identificacion=? ";*/
			}
			else
			{
/*
				consulta="SELECT " +
								"usu.codigo_persona AS codigo, " +
								"getnombrepersonasies(usu.codigo_persona) AS nombre " +
							"FROM " +
								"usuarios usu " +
							"WHERE " +
								"usu.login NOT IN(SELECT ui.login FROM usuarios_inactivos ui) "+
							" AND (";*/

				consulta="SELECT " +
								"DISTINCT " +
								"usu.codigo_persona AS codigo, " +
								"getnombrepersonasies(usu.codigo_persona) AS nombre " +
							"FROM " +
								"usuarios usu " +
							"INNER JOIN " +
								"centros_costo_usuario ccu " +
								"ON(ccu.usuario=usu.login) " +
							"iNNER JOIN " +
								"centros_costo cc " +
								"ON(cc.codigo=ccu.centro_costo)" +
							"WHERE " +
								"usu.login NOT IN(SELECT ui.login FROM usuarios_inactivos ui) "+
							" AND (";
				for(int i=0; i<palabras.length; i++)
				{
					if(i>0)
					{
						consulta+=" AND ";
					}
					consulta+="UPPER(getnombrepersonasies(usu.codigo_persona)) LIKE UPPER(?)";
				}
				consulta+=") ";
			}
			if(centroCosto!=0)
			{
				consulta+="AND ccu.centro_costo=? ";
			}
			if(centroAtencion!=0)
			{
				consulta+="AND cc.centro_atencion=? ";
			}
		}
		try
		{
			//System.out.println("Consulta "+consulta);
			PreparedStatementDecorator stm=new PreparedStatementDecorator(con, consulta);
			if(textoBusqueda!=null && !textoBusqueda.equals(""))
			{
				int contadorParametros=1;
				if(criterio)
				{
					stm.setInt(1, Integer.parseInt(textoBusqueda));
					contadorParametros++;
				}
				else
				{
					for(int i=0; i<palabras.length; i++)
					{
						stm.setString(contadorParametros, "%"+palabras[i]+"%");
						contadorParametros++;
					}
				}
				if(centroCosto!=0)
				{
					stm.setInt(contadorParametros, centroCosto);
					contadorParametros++;
				}
				
				if(centroAtencion!=0)
				{
					stm.setInt(contadorParametros, centroAtencion);
					contadorParametros++;
				}
			}
			//logger.info(stm);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(stm.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error obteniendo el listado de personas en la búsqueda ",e);
			return null;
		}
	}

	/**
	 * Método para consultar la categoría a la cual pertenece un turno 
	 * @param con
	 * @param codigoTurno
	 * @return Código de la categoría
	 */
	public static Integer codigoCategoriaTurno(Connection con, int codigoTurno)
	{
		String consulta="SELECT getCodigoCategoriaTurno(?) AS codigoCategoria";
		try
		{
			PreparedStatementDecorator stm=new PreparedStatementDecorator(con, consulta);
			stm.setInt(1, codigoTurno);
			ResultSet resultado=stm.executeQuery();
			if(resultado.next())
			{
				return resultado.getInt("codigoCategoria");
			}
		}
		catch (SQLException e)
		{
			logger.error("Error consultando la categoría del turno "+e);
		}
		return null;
	}

	/**
	 * Método que busca personas que no tienen turnos en un rango de fechas
	 * para in cuadro de turnos específico
	 * @param connection
	 * @param textoBusqueda
	 * @param criterio
	 * @param institucion
	 * @param centroCosto
	 * @param centroAtencion
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoCuadro
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Collection<HashMap<String, Object>> busquedaAvanzadaPersonas(Connection con, String textoBusqueda, boolean criterio, int institucion, int centroCosto, int centroAtencion, String fechaInicial, String fechaFinal, int codigoCuadro)
	{
		String consulta="";

		String[] palabras=textoBusqueda.split(" ");
		if(textoBusqueda!=null && !textoBusqueda.equals(""))
		{
			if(criterio)
			{
				/*
				consulta="SELECT " +
								"usu.codigo_persona AS codigo, " +
								"getnombrepersonasies(usu.codigo_persona) AS nombre " +
							"FROM " +
								"usuarios usu " +
							"INNER JOIN " +
								"personas per " +
								"ON(per.codigo=usu.codigo_persona) " +
							"WHERE " +
								"usu.login NOT IN(SELECT ui.login FROM usuarios_inactivos ui) "+
							" AND " +
								"per.numero_identificacion=? ";
*/
				consulta="SELECT " +
								"DISTINCT " +
								"usu.codigo_persona AS codigo, " +
								"getnombrepersonasies(usu.codigo_persona) AS nombre " +
							"FROM " +
								"usuarios usu " +
							"INNER JOIN " +
								"personas per " +
								"ON(per.codigo=usu.codigo_persona) " +
							"INNER JOIN " +
								"centros_costo_usuario ccu " +
								"ON(ccu.usuario=usu.login) " +
							"INNER JOIN " +
								"centros_costo cc " +
								"ON(cc.codigo=ccu.centro_costo)" +
							"WHERE " +
								"usu.login NOT IN(SELECT ui.login FROM usuarios_inactivos ui) "+
							" AND " +
								"per.numero_identificacion=? ";
			}
			else
			{
				consulta="SELECT " +
								"DISTINCT " +
								"usu.codigo_persona AS codigo, " +
								"getnombrepersonasies(usu.codigo_persona) AS nombre " +
							"FROM " +
								"usuarios usu " +
							"INNER JOIN " +
								"centros_costo_usuario ccu " +
								"ON(ccu.usuario=usu.login) " +
							"iNNER JOIN " +
								"centros_costo cc " +
								"ON(cc.codigo=ccu.centro_costo)" +
							"WHERE " +
								"usu.login NOT IN(SELECT ui.login FROM usuarios_inactivos ui) "+
							" AND (";
				for(int i=0; i<palabras.length; i++)
				{
					if(i>0)
					{
						consulta+=" AND ";
					}
					consulta+="UPPER(getnombrepersonasies(usu.codigo_persona)) LIKE UPPER(?)";
				}
				consulta+=") ";
			}
			if(centroCosto!=0)
			{
				consulta+="AND usu.centro_costo=? ";
			}
			if(centroAtencion!=0)
			{
				consulta+="AND cc.centro_atencion=? ";
			}
			consulta+=
				"AND " +
					"usu.codigo_persona " +
						"NOT IN" +
						"(" +
								"SELECT " +
									"codigomedico " +
								"FROM " +
									"ct_turno " +
								"WHERE " +
									"ctcodigocuadro=? " +
							"UNION " +
								"SELECT " +
									"codigomedico " +
								"FROM " +
									"ct_cubrir_turno " +
								"WHERE " +
									"categoria = " +
									"(" +
										"SELECT " +
										"codigocategoria " +
										"FROM " +
										"cuadro_turnos " +
										"WHERE " +
										"ctcodigocuadro=?" +
									") " +
								"AND " +
									"ct_fecha " +
								"BETWEEN " +
									"? AND ?" +
						")";
			try
			{
				//System.out.println("Consulta "+consulta);
				PreparedStatementDecorator stm=new PreparedStatementDecorator(con, consulta);
				if(textoBusqueda!=null && !textoBusqueda.equals(""))
				{
					int contadorParametros=1;
					if(criterio)
					{
						stm.setInt(1, Integer.parseInt(textoBusqueda));
						contadorParametros++;
					}
					else
					{
						for(int i=0; i<palabras.length; i++)
						{
							stm.setString(contadorParametros, "%"+palabras[i]+"%");
							contadorParametros++;
						}
					}
					if(centroCosto!=0)
					{
						stm.setInt(contadorParametros, centroCosto);
						contadorParametros++;
					}
					stm.setInt(contadorParametros, codigoCuadro);
					contadorParametros++;
					stm.setInt(contadorParametros, codigoCuadro);
					contadorParametros++;
					stm.setString(contadorParametros, fechaInicial);
					contadorParametros++;
					stm.setString(contadorParametros, fechaFinal);
					if(centroAtencion!=0)
					{
						stm.setInt(contadorParametros, centroAtencion);
						contadorParametros++;
					}
				}
				logger.info(stm);
				return UtilidadBD.resultSet2Collection(new ResultSetDecorator(stm.executeQuery()));
			}
			catch (SQLException e)
			{
				logger.error("Error obteniendo el listado de personas en la búsqueda "+e);
				return null;
			}
		}
		return null;
	}

	/**
	 * Método para listar los centros de costo
	 * @param con Conexión con la base de datos
	 * @return Listado de los centros de costo (Keys: "codigo", "nombre")
	 */
	public static Collection<HashMap<String, Object>> listarCentrosCosto(Connection con)
	{
		try
		{
			PreparedStatementDecorator stm=new PreparedStatementDecorator(con, "SELECT cc.codigo AS codigo, cc.nombre AS nombre FROM centros_costo cc WHERE cc.codigo NOT IN(-1, 0)");
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(stm.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("Error consultando los centros de costo "+e);
			return null;
		}
	}
	
}
