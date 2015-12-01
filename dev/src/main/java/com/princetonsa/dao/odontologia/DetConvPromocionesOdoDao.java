package com.princetonsa.dao.odontologia;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoDetConvPromocionesOdo;

public  interface  DetConvPromocionesOdoDao {


	/**
	 * 
	 * @param dto
	 * @return
	 */
	public double  guardar(DtoDetConvPromocionesOdo dto);
	
	 /**
	  * 
	  * @param dto
	  * @return
	  */
	public ArrayList<DtoDetConvPromocionesOdo> cargar(DtoDetConvPromocionesOdo dto);
			
		/**
		 * 
		 * @param dto
		 * @return
		 */
	public boolean modificar(DtoDetConvPromocionesOdo dto);
			
		/**
		 * 
		 * @param dto
		 * @return
		 */
	public boolean eliminar(DtoDetConvPromocionesOdo dto);

	
}
