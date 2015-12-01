package com.servinte.axioma.orm.delegate.administracion;

import java.util.ArrayList;

import com.servinte.axioma.dto.administracion.DtoPaises;
import com.servinte.axioma.orm.Paises;
import com.servinte.axioma.orm.PaisesHome;

/**
 * 
 * @author axioma
 */
public class PaisesDelegate extends PaisesHome {
	

	/**
	 * Lista todos los Paises
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Paises> listarPaises (){
		return (ArrayList<Paises>) sessionFactory.getCurrentSession()
			.createCriteria(Paises.class)
			.list();
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<DtoPaises> listarPaisesDto() 
	{
		ArrayList<Paises> paises=listarPaises();
		ArrayList<DtoPaises> resultado= new ArrayList<DtoPaises>();
		for(Paises pais:paises)
		{
			DtoPaises dto=new DtoPaises();
			dto.setCodigoPais(pais.getCodigoPais());
			dto.setDescripcionPais(pais.getDescripcion());
			resultado.add(dto);
		}
		return resultado;
	}
}
