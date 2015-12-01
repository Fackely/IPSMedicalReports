package com.servinte.axioma.dao.impl.capitacion;

import java.util.Date;
import java.util.List;

import com.princetonsa.dto.facturacion.DtoContrato;
import com.servinte.axioma.dao.interfaz.capitacion.IConvUsuariosCapitadosDAO;
import com.servinte.axioma.orm.ConvUsuariosCapitados;
import com.servinte.axioma.orm.delegate.capitacion.ConvUsuariosCapitadosDelegate;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * para la entidad ConvUsuariosCapitados
 * @author Ricardo Ruiz
 *
 */
public class ConvUsuariosCapitadosHibernateDAO implements IConvUsuariosCapitadosDAO{

	ConvUsuariosCapitadosDelegate delegate;
	
	
	/**
	 * Constructor de la clase
	 */
	public ConvUsuariosCapitadosHibernateDAO() {
		delegate	= new ConvUsuariosCapitadosDelegate();
	}
	
	
	/** (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IConvUsuariosCapitadosDAO#attachDirty(com.servinte.axioma.orm.ConvUsuariosCapitados)
	 */
	@Override
	public void attachDirty(ConvUsuariosCapitados instance) {
		delegate.attachDirty(instance);
	}


	/** (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IConvUsuariosCapitadosDAO#buscarConvUsuariosCapitadosRecientePorUsuarioCapitado(long)
	 */
	@Override
	public ConvUsuariosCapitados buscarConvUsuariosCapitadosRecientePorUsuarioCapitado(
			long codigoUsuarioCapitado) {
		return delegate.buscarConvUsuariosCapitadosRecientePorUsuarioCapitado(codigoUsuarioCapitado);
	}


	
	/** (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IConvUsuariosCapitadosDAO#obtenerCarguesPreviosPorUsuarioCapitadoPorRangoFechas(long, java.util.Date, java.util.Date)
	 */
	@Override
	public List<DtoContrato> obtenerCarguesPreviosPorUsuarioCapitadoPorRangoFechas(
			long codigoUsuarioCapitado, Date fechaInicio, Date fechaFin) {
		return delegate.obtenerCarguesPreviosPorUsuarioCapitadoPorRangoFechas(codigoUsuarioCapitado, fechaInicio, fechaFin);
	}

}
