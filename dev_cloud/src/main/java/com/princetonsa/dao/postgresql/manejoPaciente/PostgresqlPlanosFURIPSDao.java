package com.princetonsa.dao.postgresql.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.manejoPaciente.RutasArchivosFURIPS;

import com.princetonsa.dao.manejoPaciente.PlanosFURIPSDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBasePlanosFURIPSDao;
import com.princetonsa.dto.manejoPaciente.DtoFURIPS1;
import com.princetonsa.dto.manejoPaciente.DtoFURIPS2;
import com.princetonsa.dto.manejoPaciente.DtoFURPRO;
import com.princetonsa.dto.manejoPaciente.DtoFURTRAN;
import com.princetonsa.mundo.InstitucionBasica;

/**
 * 
 * @author wilson
 *
 */
public class PostgresqlPlanosFURIPSDao implements PlanosFURIPSDao 
{
	/**
	 * Metodo que carga toda la información pertinente para generar el archivo FURIPS1
	 * @param con
	 * @param mapaBusqueda
	 * @param institucionBasica
	 * @param institucion
	 * @return
	 */
	public ArrayList<DtoFURIPS1> consultaFURIPS1(Connection con, HashMap<Object, Object> mapaBusqueda, InstitucionBasica institucionBasica, int institucion,String reclamacion)
	{
		return SqlBasePlanosFURIPSDao.consultaFURIPS1(con, mapaBusqueda, institucionBasica, institucion,reclamacion);
	}
	
	/**
	 * Metodo que carga toda la información pertinente para generar el archivo FURIPS2
	 * @param con
	 * @param mapaBusqueda
	 * @param institucionBasica
	 * @param institucion
	 * @return
	 */
	public ArrayList<DtoFURIPS2> consultaFURIPS2(Connection con, HashMap<Object, Object> mapaBusqueda, InstitucionBasica institucionBasica, int institucion,String reclamacion)
	{
		return SqlBasePlanosFURIPSDao.consultaFURIPS2(con, mapaBusqueda, institucionBasica, institucion,reclamacion);
	}
	
	/**
	 * Metodo que carga toda la información pertinente para generar el archivo FURPRO
	 * @param con
	 * @param mapaBusqueda
	 * @param institucionBasica
	 * @param institucion
	 * @return
	 */
	public ArrayList<DtoFURPRO> consultaFURPRO(Connection con, HashMap<Object, Object> mapaBusqueda, InstitucionBasica institucionBasica, int institucion,String reclamacion)
	{
		return SqlBasePlanosFURIPSDao.consultaFURPRO(con, mapaBusqueda, institucionBasica, institucion,reclamacion) ;
	}
	
	/**
	 * Metodo que carga toda la información pertinente para generar el archivo FURTRAN
	 * @param con
	 * @param mapaBusqueda
	 * @param institucionBasica
	 * @param institucion
	 * @return
	 */
	public  ArrayList<DtoFURTRAN> consultaFURTRAN(Connection con, HashMap<Object, Object> mapaBusqueda, InstitucionBasica institucionBasica, int institucion)
	{
		return SqlBasePlanosFURIPSDao.consultaFURTRAN(con, mapaBusqueda, institucionBasica, institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param rutasArchivos
	 * @param mapaBusqueda
	 * @param institucion
	 * @param usuario
	 * @return
	 */
	public boolean insertarLogBD(Connection con, RutasArchivosFURIPS rutasArchivos, HashMap<Object, Object> mapaBusqueda, int institucion, String usuario)
	{
		return SqlBasePlanosFURIPSDao.insertarLogBD(con, rutasArchivos, mapaBusqueda, institucion, usuario);
	}
}
