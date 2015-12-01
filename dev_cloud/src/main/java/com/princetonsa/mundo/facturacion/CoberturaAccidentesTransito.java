/*
 * Creado   19/11/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.InfoDatosInt;

import com.princetonsa.dao.CoberturaAccidentesTransitoDao;
import com.princetonsa.dao.DaoFactory;

/**
 * Clase para manejar la informaci&oacute;n topes cobertura para accidentes de
 * tr&aacute;nsito
 * 
 * @version 1.0, 17/08/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez </a>
 */
public class CoberturaAccidentesTransito
{
	/**
	 * Manejador de logs de la clase
	 */
	Logger logger=Logger.getLogger(CoberturaAccidentesTransito.class);
	
	/**
	 * codigo de la institucion
	 */
	private int institucion;

	/**
	 * n&uacute;mero de salarios diarios legales vigentes que cubre el
	 * responsable correspondiente.
	 */
	private double[] cobertura;

	/**
	 * objeto que permite las operaciones sobre la fuente de datos
	 */
	private CoberturaAccidentesTransitoDao coberturaAccidentesDao;
	
	/**
	 * Utilizado para almacenar InfoaDatos
	 * con todas las coberturas ordenadas
	 */
	private ArrayList coberturasCompletas;

	/**
	 * Metodo para resetear el mundo
	 */
	public void reset()
	{
		this.institucion = 0;
		coberturasCompletas=new ArrayList();
	}

	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su
	 * respectivo DAO.
	 * 
	 * @param tipoBD
	 *            el tipo de base de datos que va a usar este objeto (e.g.,
	 *            Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD son
	 *            los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD)
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

		if (myFactory != null)
		{
			coberturaAccidentesDao = myFactory.getCoberturaAccidentesTransitoDao();
		}
	}

	/**
	 * Constructor de la clase
	 *  
	 */
	public CoberturaAccidentesTransito()
	{
		this.reset();
		this.init(System.getProperty("TIPOBD"));
	}

	/**
	 * @return Returns the institucion.
	 */
	public int getInstitucion()
	{
		return institucion;
	}

	/**
	 * @param institucion
	 * Asigna institucion
	 */
	public void setInstitucion(int institucion)
	{
		this.institucion = institucion;
	}

	/**
	 * Método para insertar responsables en la BD
	 * @param con
	 * @param codInstitucion
	 * @param cobertura
	 * @return numero de registros insertados
	 */
	public int guardar(Connection con, int codInstitucion, double[] cobertura)
	{
		this.institucion=codInstitucion;
		this.cobertura=cobertura;
		return guardar(con);
	}

	/**
	 * Método para insertar responsables en la BD
	 * tomando los objetos propios de la clase
	 * @param con
	 * @return numero de registros insertados
	 */
	private int guardar(Connection con)
	{
		int resultado=0;
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try
		{
			myFactory.beginTransaction(con);
			coberturaAccidentesDao.eliminar(con, this.institucion);
			resultado=coberturaAccidentesDao.insertar(con, this.institucion, this.cobertura);
			myFactory.endTransaction(con);
		}
		catch (SQLException e)
		{
			logger.error("Error abriendo la transacción "+e);
			try
			{
				myFactory.abortTransaction(con);
			}
			catch (SQLException e1)
			{
				logger.error("Error abortando la transacción "+e1);
			}
			return -1;
		}
		return resultado;
	}

	/**
	 * Método para listar la cobertura de accidentes para una institución dada
	 * @param con
	 * @param institucion
	 * @return Mapa con los datos de la cobertura para la institución dada
	 */
	public HashMap listar(Connection con, int institucion)
	{
		return coberturaAccidentesDao.listar(con, institucion);
	}

	/**
	 * Método para listar la cobertura de accidentes para una institución dada
	 * @param con
	 * @param institucion
	 * @return numero de registros de cobertura
	 */
	public int cargarCobertura(Connection con, int institucion)
	{
		HashMap cob=coberturaAccidentesDao.listar(con, institucion);
		int numRegistros=((Integer)cob.get("numRegistros")).intValue();
		int i=0;
		for(i=0; i<numRegistros; i++)
		{
			int responsable=Integer.parseInt(cob.get("responsable_"+i)+"");
			String cober=cob.get("cobertura_"+i)+"";
			InfoDatosInt cobertura=new InfoDatosInt(responsable, cober);
			coberturasCompletas.add(cobertura);
		}
		return i;
	}

	
	/**
	 * @return Retorna cobertura.
	 */
	public double[] getCobertura()
	{
		return cobertura;
	}
	/**
	 * @param cobertura Asigna cobertura.
	 */
	public void setCobertura(double[] cobertura)
	{
		this.cobertura = cobertura;
	}
	/**
	 * @return Retorna coberturaAccidentesDao.
	 */
	public CoberturaAccidentesTransitoDao getCoberturaAccidentesDao()
	{
		return coberturaAccidentesDao;
	}
	/**
	 * @param coberturaAccidentesDao Asigna coberturaAccidentesDao.
	 */
	public void setCoberturaAccidentesDao(
			CoberturaAccidentesTransitoDao coberturaAccidentesDao)
	{
		this.coberturaAccidentesDao = coberturaAccidentesDao;
	}
	/**
	 * @return Retorna coberturasCompletas.
	 */
	public ArrayList getCoberturasCompletas()
	{
		return coberturasCompletas;
	}
	/**
	 * @param coberturasCompletas Asigna coberturasCompletas.
	 */
	public void setCoberturasCompletas(ArrayList coberturasCompletas)
	{
		this.coberturasCompletas = coberturasCompletas;
	}
}