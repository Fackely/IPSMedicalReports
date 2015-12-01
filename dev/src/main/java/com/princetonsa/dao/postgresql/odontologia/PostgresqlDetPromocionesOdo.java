package com.princetonsa.dao.postgresql.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.odontologia.DetPromocionesOdoDao;
import com.princetonsa.dto.odontologia.DtoDetPromocionOdo;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseDetPromocionesOdoDao;

/**
 * 
 * @author axioma
 *
 */
public class PostgresqlDetPromocionesOdo implements DetPromocionesOdoDao{

	@Override
	public ArrayList<DtoDetPromocionOdo> cargar(DtoDetPromocionOdo dto) {
		return SqlBaseDetPromocionesOdoDao.cargar(dto);
	}

	@Override
	/**
	 * 
	 */
	
	public boolean eliminar(DtoDetPromocionOdo dto) {
		
		return SqlBaseDetPromocionesOdoDao.eliminar(dto);
		}

	@Override
	/**
	 * 
	 */
	public int guardar(DtoDetPromocionOdo dto) {
		
		return SqlBaseDetPromocionesOdoDao.guardar(dto);
	}

	@Override
	/**
	 * 
	 */
	public boolean modificar(DtoDetPromocionOdo dto) {
		
		return SqlBaseDetPromocionesOdoDao.modificar(dto);
	}
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
   public  DtoDetPromocionOdo cargarObjeto( DtoDetPromocionOdo dto){
	   return  SqlBaseDetPromocionesOdoDao.cargarObjeto(dto);
   }
   
   
   public int   guardarLog( DtoDetPromocionOdo dto){
	   return  SqlBaseDetPromocionesOdoDao.guardarLog(dto);
   }
   
	

}
