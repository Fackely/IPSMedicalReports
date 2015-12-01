package com.princetonsa.dao.postgresql.interfaz;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.interfaz.DesmarcarDocProcesadosDao;
import com.princetonsa.dao.sqlbase.interfaz.SqlBaseDesmarcarDocProcesadosDao;
import com.princetonsa.dto.interfaz.DtoDocumentoDesmarcar;
import com.princetonsa.dto.interfaz.DtoLogInterfaz1E;

public class PostgresqlDesmarcarDocProcesadosDao implements DesmarcarDocProcesadosDao {

	@Override
	public ArrayList<DtoDocumentoDesmarcar> consultarDocumentosXtipoMvto(String tipoMov) {
		
		return SqlBaseDesmarcarDocProcesadosDao.consultarDocumentosXtipoMvto(tipoMov);
	}

	@Override
	public HashMap desmarcarFacturasPacientes(Connection con, HashMap parametros) {
		
		return SqlBaseDesmarcarDocProcesadosDao.desmarcarFacturasPacientes(con, parametros);
	}

	@Override
	public HashMap desmarcarAnulacionFacturasPacientes(Connection con,
			HashMap parametrosBusqueda) {
		
		return SqlBaseDesmarcarDocProcesadosDao.desmarcarAnulacionFacturasPacientes(con, parametrosBusqueda);
	}

	@Override
	public HashMap desmarcarFacturasVarias(Connection con,HashMap parametrosBusqueda, boolean anulacion) {
		
		return SqlBaseDesmarcarDocProcesadosDao.desmarcarFacturasVarias(con, parametrosBusqueda, anulacion);
	}

	@Override
	public HashMap desmarcarAjustesFacturasVarias(Connection con,
			HashMap parametrosBusqueda) {
		
		return SqlBaseDesmarcarDocProcesadosDao.desmarcarAjustesFacturasVarias(con, parametrosBusqueda);
	}

	@Override
	public HashMap desmarcarAjustesCuentasCobroCapitacion(Connection con,HashMap parametrosBusqueda) {
		
		return SqlBaseDesmarcarDocProcesadosDao.desmarcarAjustesCuentasCobroCapitacion(con, parametrosBusqueda);
	}

	@Override
	public HashMap desmarcarCuentasCobroCapitacion(Connection con,HashMap parametrosBusqueda) {
		
		return SqlBaseDesmarcarDocProcesadosDao.desmarcarCuentasCobroCapitacion(con, parametrosBusqueda);
	}

	@Override
	public HashMap desmarcarAnulacionRecibosdeCaja(Connection con,HashMap parametrosBusqueda) {
		
		return SqlBaseDesmarcarDocProcesadosDao.desmarcarAnulacionRecibosdeCaja(con, parametrosBusqueda);
	}

	@Override
	public HashMap desmarcarRecibosdeCaja(Connection con,HashMap parametrosBusqueda) {
		
		return SqlBaseDesmarcarDocProcesadosDao.desmarcarRecibosdeCaja(con, parametrosBusqueda);
	}


	@Override
	public HashMap desmarcarDevolucionRecibosdeCaja(Connection con,	HashMap parametrosBusqueda) { 
		
		return SqlBaseDesmarcarDocProcesadosDao.desmarcarDevolucionRecibosdeCaja(con,parametrosBusqueda);
	}

	@Override
	public HashMap desmarcarAjustesFacuturasPacientes(Connection con,HashMap parametrosBusqueda) {
		
		return SqlBaseDesmarcarDocProcesadosDao.desmarcarAjustesFacuturasPacientes(con, parametrosBusqueda);
	}
	
	@Override
	public HashMap desmarcarRegistroGlosas(Connection con,HashMap parametrosBusqueda) {
		
		return SqlBaseDesmarcarDocProcesadosDao.desmarcarRegistroGlosas(con, parametrosBusqueda);
	}

	@Override
	public HashMap desmarcarAutorServEntidadesSub(Connection con,HashMap parametrosBusqueda, boolean anulacion) {
		
		return SqlBaseDesmarcarDocProcesadosDao.desmarcarAutorServEntidadesSub(con, parametrosBusqueda, anulacion);
	}

	@Override
	public HashMap desmarcarDespachoMedicamentos(Connection con,HashMap parametrosBusqueda) {
		
		return SqlBaseDesmarcarDocProcesadosDao.desmarcarDespachoMedicamentos(con, parametrosBusqueda);
	}

	@Override
	public HashMap desmarcarDevolucionMedicamentos(Connection con,HashMap parametrosBusqueda) {
		
		return SqlBaseDesmarcarDocProcesadosDao.desmarcarDevolucionMedicamentos(con, parametrosBusqueda);
	}

	@Override
	public HashMap desmarcarDespachoPedidos(Connection con,HashMap parametrosBusqueda, boolean esQuirurgico) {
		
		return SqlBaseDesmarcarDocProcesadosDao.desmarcarDespachoPedidos(con, parametrosBusqueda, esQuirurgico);
	}

	@Override
	public HashMap desmarcarDevolucionPedidos(Connection con,HashMap parametrosBusqueda, boolean esQuirurgico) {
		
		return SqlBaseDesmarcarDocProcesadosDao.desmarcarDevolucionPedidos(con, parametrosBusqueda, esQuirurgico);
	}

	@Override
	public HashMap desmarcarCargosDirectosArticulos(Connection con,	HashMap parametrosBusqueda) {
		
		return SqlBaseDesmarcarDocProcesadosDao.desmarcarCargosDirectosArticulos(con, parametrosBusqueda);
	}

	@Override
	public HashMap desmarcarAnulacionCargosArticulos(Connection con,HashMap parametrosBusqueda) {
		
		return SqlBaseDesmarcarDocProcesadosDao.desmarcarAnulacionCargosArticulos(con, parametrosBusqueda);
	}

	@Override
	public HashMap guardarLogInterfaz1E(Connection con, DtoLogInterfaz1E dto) {
		
		return SqlBaseDesmarcarDocProcesadosDao.guardarLogInterfaz1E(con, dto);
	}

	@Override
	public HashMap guardarLogInterfazTiposDoc1E(Connection con,int codLogInterfaz, String tipoDocumento) {
		
		return SqlBaseDesmarcarDocProcesadosDao.guardarLogInterfazTiposDoc1E(con, codLogInterfaz, tipoDocumento);
	}

	

	
}
