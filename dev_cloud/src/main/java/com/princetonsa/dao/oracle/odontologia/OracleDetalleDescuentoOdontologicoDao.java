package com.princetonsa.dao.oracle.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.odontologia.DetalleDescuentoOdontologicoDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseDetalleDescuentoOdontologicoDao;
import com.princetonsa.dto.odontologia.DtoDetalleDescuentoOdontologico;

public class OracleDetalleDescuentoOdontologicoDao implements DetalleDescuentoOdontologicoDao {

	@Override
	public ArrayList<DtoDetalleDescuentoOdontologico> cargar(
			DtoDetalleDescuentoOdontologico dtoWhere) {
		
		return SqlBaseDetalleDescuentoOdontologicoDao.cargar(dtoWhere);
	}

	@Override
	public boolean eliminar(DtoDetalleDescuentoOdontologico dtoWhere) {
		
		return  SqlBaseDetalleDescuentoOdontologicoDao.eliminar(dtoWhere);
	}

	@Override
	public double guardar(DtoDetalleDescuentoOdontologico dto) {
		
		return SqlBaseDetalleDescuentoOdontologicoDao.guardar(dto);
	}

	@Override
	public boolean modificar(DtoDetalleDescuentoOdontologico dtoNuevo,
			DtoDetalleDescuentoOdontologico dtoWhere) {
		
		return SqlBaseDetalleDescuentoOdontologicoDao.modificar(dtoNuevo, dtoWhere);
	}
	
	@Override
	public boolean existeRangoPresupuesto(double consecutivo,
			double centroAtencion, double ValorMinimo, double ValorMaximo,double codigo) {
		
		return SqlBaseDetalleDescuentoOdontologicoDao.existeRangoPresupuesto(consecutivo, centroAtencion, ValorMinimo,ValorMaximo, codigo);
	}
	
}
