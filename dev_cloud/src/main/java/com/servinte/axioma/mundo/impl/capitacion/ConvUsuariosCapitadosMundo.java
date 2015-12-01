package com.servinte.axioma.mundo.impl.capitacion;

import java.util.Date;
import java.util.List;

import com.princetonsa.dto.facturacion.DtoContrato;
import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IConvUsuariosCapitadosDAO;
import com.servinte.axioma.mundo.interfaz.capitacion.IConvUsuariosCapitadosMundo;
import com.servinte.axioma.orm.ConvUsuariosCapitados;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * para la entidad ConvUsuariosCapitados
 * @author Ricardo Ruiz
 *
 */
public class ConvUsuariosCapitadosMundo implements IConvUsuariosCapitadosMundo{

	IConvUsuariosCapitadosDAO dao;
	
	/**
	 * Constructor de la clase
	 */
	public ConvUsuariosCapitadosMundo(){
		dao= CapitacionFabricaDAO.crearConvUsuariosCapitadosDAO();
	} 
	
	/** (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IConvUsuariosCapitadosMundo#attachDirty(com.servinte.axioma.orm.ConvUsuariosCapitados)
	 */
	@Override
	public void attachDirty(ConvUsuariosCapitados instance) {
		dao.attachDirty(instance);
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IConvUsuariosCapitadosMundo#buscarConvUsuariosCapitadosRecientePorUsuarioCapitado(long)
	 */
	@Override
	public ConvUsuariosCapitados buscarConvUsuariosCapitadosRecientePorUsuarioCapitado(
			long codigoUsuarioCapitado) {
		return dao.buscarConvUsuariosCapitadosRecientePorUsuarioCapitado(codigoUsuarioCapitado);
	}


	/** (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IConvUsuariosCapitadosMundo#obtenerCarguesPreviosPorUsuarioCapitadoPorRangoFechas(long, java.util.Date, java.util.Date)
	 */
	@Override
	public List<DtoContrato> obtenerCarguesPreviosPorUsuarioCapitadoPorRangoFechas(
			long codigoUsuarioCapitado, Date fechaInicio, Date fechaFin) {
		return dao.obtenerCarguesPreviosPorUsuarioCapitadoPorRangoFechas(codigoUsuarioCapitado, fechaInicio, fechaFin);
	}

}
