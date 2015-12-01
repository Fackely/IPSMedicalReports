package com.servinte.axioma.dao.impl.odontologia.cuotaOdontologica;



import com.servinte.axioma.dao.interfaz.odontologia.cuotaOdontologica.ICuotasOdontEspecialidadDao;
import com.servinte.axioma.orm.CuotasOdontEspecialidad;
import com.servinte.axioma.orm.delegate.odontologia.cuotaOdontologica.CuotasOdontEspecialidadDelegate;


/**
 * 
 * @author Edgar Carvajal
 *
 */
public class CuotasOdonEspecialidadDAO  implements ICuotasOdontEspecialidadDao{

	
	/**
	 * INTERFAZ
	 */
	private CuotasOdontEspecialidadDelegate delegeteI ;
	
	
	/**
	 * CONSTRUTOR 
	 */
	public CuotasOdonEspecialidadDAO()
	{
		delegeteI =new CuotasOdontEspecialidadDelegate();
	}
	
	
	
	@Override
	public CuotasOdontEspecialidad consultarAvanzadaCuotaEspecialidad(CuotasOdontEspecialidad dto)
	{
		return delegeteI.consultarAvanzadaCuotaEspecialidad(dto);
	}

	
	@Override
	public void insertar(CuotasOdontEspecialidad objeto) 
	{
		delegeteI.attachDirty(objeto);
		
	}

	
	@Override
	public void modificar(CuotasOdontEspecialidad objeto) 
	{
		delegeteI.attachDirty(objeto);
	}




	@Override
	public CuotasOdontEspecialidad buscarxId(Number id) 
	{
		return  delegeteI.findById(id.intValue());
	}



	@Override
	public void eliminar(CuotasOdontEspecialidad objeto) 
	{
		delegeteI.delete(objeto);
	}

}
