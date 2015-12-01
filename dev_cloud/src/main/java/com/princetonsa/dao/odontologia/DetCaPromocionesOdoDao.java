package com.princetonsa.dao.odontologia;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoDetCaPromocionesOdo;
/**
 * 
 * @author axioma
 *
 */
public interface DetCaPromocionesOdoDao {
	 
	
		/**
		 * 
		 * @param dto
		 * @return
		 */
		public double  guardar(DtoDetCaPromocionesOdo dto);
		
		 /**
		  * 
		  * @param dto
		  * @return
		  */
		public ArrayList<DtoDetCaPromocionesOdo> cargar(DtoDetCaPromocionesOdo dto);
				
			/**
			 * 
			 * @param dto
			 * @return
			 */
		public boolean modificar(DtoDetCaPromocionesOdo dto);
				
			/**
			 * 
			 * @param dto
			 * @return
			 */
		public boolean eliminar(DtoDetCaPromocionesOdo dto);
		
		/**
		 * 
		 * @param dto
		 * @return
		 */
		public ArrayList<DtoDetCaPromocionesOdo> cargarCentroAtencionNUll(DtoDetCaPromocionesOdo dto);

}
