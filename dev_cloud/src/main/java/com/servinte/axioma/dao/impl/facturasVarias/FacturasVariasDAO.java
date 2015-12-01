package com.servinte.axioma.dao.impl.facturasVarias;

import java.util.ArrayList;

import com.princetonsa.dto.consultaExterna.DtoConceptoFacturaVaria;
import com.servinte.axioma.dao.interfaz.facturaVarias.IFacturasVariasDAO;
import com.servinte.axioma.orm.FacturasVarias;
import com.servinte.axioma.orm.delegate.facturasVarias.FacturasVariasDelegate;


/**
 * 
 * @author Edgar Carvajal 
 *
 */
public class FacturasVariasDAO implements IFacturasVariasDAO{

	@Override
	public void generarFacturaVaria(FacturasVarias factura)
	{
		FacturasVariasDelegate delegate=new FacturasVariasDelegate();
		delegate.attachDirty(factura);
	}

	@Override
	public ArrayList<DtoConceptoFacturaVaria> listarConceptosFacturasVarias() {
		FacturasVariasDelegate delegate=new FacturasVariasDelegate();
		return delegate.listarConceptosFacturasVarias();
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.facturaVarias.IFacturasVariasDAO#obtenerFacturaVaria(int)
	 */
	@Override
	public FacturasVarias obtenerFacturaVaria(long codigoFacVar) {
		
		FacturasVariasDelegate delegate=new FacturasVariasDelegate();
		return delegate.findById(codigoFacVar);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.facturaVarias.IFacturasVariasDAO#obtenerPrefijoConsecutivo(long)
	 */
	@Override
	public String obtenerPrefijoConsecutivo(long codigoFacVar) {
		
		FacturasVariasDelegate delegate=new FacturasVariasDelegate();
		return delegate.obtenerPrefijoConsecutivo(codigoFacVar);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.facturaVarias.IFacturasVariasDAO#listarConceptosFacturasV()
	 */
	@Override
	public ArrayList<DtoConceptoFacturaVaria> listarConceptosFacturasV() {
		FacturasVariasDelegate delegate=new FacturasVariasDelegate();
		return delegate.listarConceptosFacturasV();
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.facturaVarias.IFacturasVariasDAO#obtenerCodigoFacturaVaria(long)
	 */
	@Override
	public long obtenerCodigoFacturaVaria(long consecutivo) {
		FacturasVariasDelegate delegate=new FacturasVariasDelegate();
		return delegate.obtenerCodigoFacturaVaria(consecutivo);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.facturaVarias.IFacturasVariasDAO#obtenerConsecutivoFacturaVaria(long)
	 */
	@Override
	public long obtenerConsecutivoFacturaVaria(long codigoFacVar) {
		FacturasVariasDelegate delegate=new FacturasVariasDelegate();
		return delegate.obtenerConsecutivoFacturaVaria(codigoFacVar);
	}
}
