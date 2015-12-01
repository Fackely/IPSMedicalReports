package com.princetonsa.dao.oracle.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DtoContactoEmpresa;
import com.princetonsa.dao.facturacion.ContactosEmpresaDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlContactosEmpresaDao;

public class OracleContactosEmpresaDao implements ContactosEmpresaDao {

	@Override
	public ArrayList<DtoContactoEmpresa> cargar(DtoContactoEmpresa dto) {
		
		return SqlContactosEmpresaDao.cargar(dto);
	}

	@Override
	public boolean eliminar(DtoContactoEmpresa dto) {
		
		return SqlContactosEmpresaDao.eliminar(dto);
	}

	@Override
	public double guardar(DtoContactoEmpresa dto) {
		
		return SqlContactosEmpresaDao.guardar(dto);
	}

	@Override
	public boolean modificar(DtoContactoEmpresa dto) {
		
		return SqlContactosEmpresaDao.modificar(dto);
	}

}
