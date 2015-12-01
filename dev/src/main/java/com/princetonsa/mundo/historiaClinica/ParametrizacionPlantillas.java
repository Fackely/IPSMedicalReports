package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.ConstantesBD;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.ParametrizacionPlantillasDao;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantillaServDiag;

public class ParametrizacionPlantillas 
{
	
	
	/**
	 * 
	 */
	private ParametrizacionPlantillasDao objetoDao;
	
	/**
	 * 
	 *
	 */
	public ParametrizacionPlantillas()
	{
		init(System.getProperty("TIPOBD"));
	}
	
	
	/**
	 * Inicializa el acceso a la base de datos de este objeto, obteniendo su respectivo DAO.
	 * param tipoBD el tipo de bases de datos que va a usar este objeto.
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores validos para tipoBD.
	 * son los nombres y constantes definidos en <code>DaoFactory</code>
	 * @return <b>true</b> si la inicializacion fue exitosa, <code>false</code> si no.
	 */
	private boolean init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(tipoBD);
			objetoDao=myFactory.getParametrizacionPlantillasDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
			
	}
	
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public HashMap consultarEspecialidades(Connection con) 
	{
		return objetoDao.consultarEspecialidades(con);
	}

	/**
	 * 
	 * @param con
	 * @param plantilla
	 * @param centroCosto
	 * @param sexo
	 * @return
	 */
	public HashMap consultarPlantillas(Connection con, String plantilla, String centroCosto, String sexo) 
	{
		return objetoDao.consultarPlantillas(con, plantilla, centroCosto, sexo);
	}


	public HashMap consultaSeccionesFijas(Connection con, String plantillaBase, String codigoInstitucion) 
	{
		return objetoDao.consultaSeccionesFijas(con, plantillaBase, codigoInstitucion);
	}
	
	public static int getPosArrayServicio(String codigoServicio,ArrayList<DtoPlantillaServDiag> array)
	{	
		for(int i=0;i<array.size();i++)
		{
			if((array.get(i).getCodigoServicio()+"").equals(codigoServicio))
				return i;
		}
		
		Utilidades.imprimirArrayList(array);		
		return ConstantesBD.codigoNuncaValido;		
	}
}