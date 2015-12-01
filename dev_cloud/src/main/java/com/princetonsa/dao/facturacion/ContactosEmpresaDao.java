package com.princetonsa.dao.facturacion;


import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DtoContactoEmpresa;

public interface ContactosEmpresaDao {
	
	public double  guardar( DtoContactoEmpresa dto);
	 
	/**
	 *  
	 * @param dto
	 * @return
	 */
	public ArrayList<DtoContactoEmpresa> cargar(DtoContactoEmpresa dto);
		
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public boolean modificar(DtoContactoEmpresa dto);
		
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public boolean eliminar(DtoContactoEmpresa dto);

}
