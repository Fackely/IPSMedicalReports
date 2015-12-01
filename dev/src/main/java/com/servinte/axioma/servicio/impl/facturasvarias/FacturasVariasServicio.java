package com.servinte.axioma.servicio.impl.facturasvarias;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.consultaExterna.DtoConceptoFacturaVaria;
import com.princetonsa.dto.facturasVarias.DtoFacturaVariaGenerico;
import com.servinte.axioma.mundo.fabrica.facturasvarias.FacturasVariasMundoFabrica;
import com.servinte.axioma.orm.FacturasVarias;
import com.servinte.axioma.servicio.interfaz.facturasvarias.IFacturasVariasServicio;

public class FacturasVariasServicio implements IFacturasVariasServicio {

	@Override
	public ArrayList<DtoConceptoFacturaVaria> listarConceptosFacturasVarias() {
		return FacturasVariasMundoFabrica.crearFacturasVariasMundo()
				.listarConceptosFacturasVarias();
	}

	@Override
	public boolean generarReporte(Connection con, DtoFacturaVariaGenerico dto,
			HttpServletRequest request) {
		return FacturasVariasMundoFabrica.crearFacturasVariasMundo()
				.generarReporte(con, dto, request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.servinte.axioma.servicio.interfaz.facturasvarias.IFacturasVariasServicio
	 * #obtenerFacturaVaria(long)
	 */
	@Override
	public FacturasVarias obtenerFacturaVaria(long codigoFacVar) {

		return FacturasVariasMundoFabrica.crearFacturasVariasMundo()
				.obtenerFacturaVaria(codigoFacVar);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.facturasvarias.IFacturasVariasServicio#obtenerPrefijoConsecutivo(long)
	 */
	@Override
	public String obtenerPrefijoConsecutivo(long codigoFacVar) {

		return FacturasVariasMundoFabrica.crearFacturasVariasMundo().obtenerPrefijoConsecutivo(codigoFacVar);
	}

	@Override
	public ArrayList<DtoConceptoFacturaVaria> listarConceptosFacturasV() {
		
		return FacturasVariasMundoFabrica.crearFacturasVariasMundo().listarConceptosFacturasV();
	}
	
	@Override
	public ArrayList<DtoIntegridadDominio> listarEstadoFacturasVarias(){
	
		return FacturasVariasMundoFabrica.crearFacturasVariasMundo().listarEstadoFacturasVarias();
	}
}
