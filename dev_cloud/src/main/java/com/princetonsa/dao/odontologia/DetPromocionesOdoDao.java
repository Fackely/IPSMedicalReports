package com.princetonsa.dao.odontologia;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoDetPromocionOdo;
import com.princetonsa.dto.odontologia.DtoPromocionesOdontologicas;

public interface DetPromocionesOdoDao {
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public int  guardar(DtoDetPromocionOdo dto);
	
	 /**
	  * 
	  * @param dto
	  * @return
	  */
	public ArrayList<DtoDetPromocionOdo> cargar(DtoDetPromocionOdo dto);
			
		/**
		 * 
		 * @param dto
		 * @return
		 */
	public boolean modificar(DtoDetPromocionOdo dto);
			
		/**
		 * 
		 * @param dto
		 * @return
		 */
	public boolean eliminar(DtoDetPromocionOdo dto);

	
	/**
	 * 
	 * @param dto
	 * @return
	 */
    public DtoDetPromocionOdo cargarObjeto( DtoDetPromocionOdo dto); 
    
    /**
     * 
     * @param dto
     * @return
     */
    public int   guardarLog( DtoDetPromocionOdo dto);
    
    

     
	
}
