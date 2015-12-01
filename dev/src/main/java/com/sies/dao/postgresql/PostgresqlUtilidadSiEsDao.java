package com.sies.dao.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.sies.dao.UtilidadSiEsDao;
import com.sies.dao.sqlbase.SqlBaseUtilidadSiEsDao;

public class PostgresqlUtilidadSiEsDao implements UtilidadSiEsDao
{
	/**
	 * Manejador de losg de la clase
	 */
	private Logger logger=Logger.getLogger(PostgresqlUtilidadSiEsDao.class);
	
	/**
	 * Método para obtener el siguiente valor de las secuencias
	 */
	public int obtenerValorSecuencia(Connection con, String nombreSecuencia)
	{
		try
		{
			PreparedStatement stm=con.prepareStatement("SELECT nextval(?) AS valor_sec");
			stm.setString(1, nombreSecuencia);
			ResultSet resultado=stm.executeQuery();
			if(resultado.next())
			{
				return resultado.getInt("valor_sec");
			}
		}
		catch (SQLException e)
		{
			logger.error("Error incremenando la secuencia "+nombreSecuencia+": "+e);
		}
		return -1;
	}
	
	/**
	 * Método para consultar el nombre de la persona
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarNombrePersona(Connection con, int codigoPersona)
	{
		return SqlBaseUtilidadSiEsDao.consultarNombrePersona(con, codigoPersona);
	}

	/**
	 * Método que busca un listado de personas por nombre (criterio=false) o por código (criterio=true)
	 * @param textoBusqueda
	 * @param criterio
	 * @param connection
	 * @return
	 */
	public Collection<HashMap<String, Object>> busquedaAvanzadaPersonas(Connection con, String textoBusqueda, boolean criterio, int institucion, int centroCosto, int centroAtencion)
	{
		return SqlBaseUtilidadSiEsDao.busquedaAvanzadaPersonas(con, textoBusqueda, criterio, institucion, centroCosto, centroAtencion);
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
	public Collection<HashMap<String, Object>> busquedaAvanzadaPersonas(Connection con, String textoBusqueda, boolean criterio, int institucion, int centroCosto, int centroAtencion, String fechaInicial, String fechaFinal, int codigoCuadro)
	{
		return SqlBaseUtilidadSiEsDao.busquedaAvanzadaPersonas(con, textoBusqueda, criterio, institucion, centroCosto, centroAtencion, fechaInicial, fechaFinal, codigoCuadro);
	}

	/**
	 * Método para consultar la categoría a la cual pertenece un turno 
	 * @param con
	 * @param codigoTurno
	 * @return Código de la categoría
	 */
	public Integer codigoCategoriaTurno(Connection con, int codigoTurno)
	{
		return SqlBaseUtilidadSiEsDao.codigoCategoriaTurno(con, codigoTurno);
	}

	@Override
	public Collection<HashMap<String, Object>> listarCentrosCosto(Connection con)
	{
		return SqlBaseUtilidadSiEsDao.listarCentrosCosto(con);
	}
}
