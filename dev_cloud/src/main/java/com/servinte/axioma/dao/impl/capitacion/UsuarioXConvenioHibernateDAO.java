/**
 * 
 */
package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.princetonsa.dto.facturacion.DtoContrato;
import com.servinte.axioma.dao.interfaz.capitacion.IUsuarioXConvenioDAO;
import com.servinte.axioma.orm.UsuarioXConvenio;
import com.servinte.axioma.orm.delegate.capitacion.UsuarioXConvenioDelegate;

/**
 * Esta clase se encarga de definir los métodos de negocio
 * para la entidad  UsuarioXConvenio
 * @author Cristhian Murillo
 */
public class UsuarioXConvenioHibernateDAO implements IUsuarioXConvenioDAO
{

	
	UsuarioXConvenioDelegate delegate;
	
	
	/**
	 * Constructor de la clase
	 */
	public UsuarioXConvenioHibernateDAO() {
		delegate	= new UsuarioXConvenioDelegate();
	}

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.UsuarioXConvenioDAO#attachDirty(com.servinte.axioma.orm.UsuarioXConvenio)
	 */
	@Override
	public void attachDirty(UsuarioXConvenio instance) {
		delegate.attachDirty(instance);
	}

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.UsuarioXConvenioDAO#listarTodos()
	 */
	@Override
	public ArrayList<UsuarioXConvenio> listarTodos() {
		return delegate.listarTodos();
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar un registro de la tabla 
	 * usuario_x_convenio a partir del código del paciente
	 * @param int codigoPersona
	 * @return UsuarioXConvenio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public UsuarioXConvenio buscarUsuarioConvenioPorPaciente(int codigoPersona){
		return delegate.buscarUsuarioConvenioPorPaciente(codigoPersona);
	}


	/** (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IUsuarioXConvenioDAO#buscarUsuarioConvenioRecientePorPaciente(int)
	 */
	@Override
	public UsuarioXConvenio buscarUsuarioConvenioRecientePorPaciente(
			int codigoPersona) {
		return delegate.buscarUsuarioConvenioRecientePorPaciente(codigoPersona);
	}


	
	/** (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IUsuarioXConvenioDAO#obtenerCarguesPreviosPorPersonaPorRangoFechas(int, java.util.Date, java.util.Date)
	 */
	@Override
	public List<DtoContrato> obtenerCarguesPreviosPorPersonaPorRangoFechas(
			int codigoPersona, Date fechaInicio, Date fechaFin) {
		return delegate.obtenerCarguesPreviosPorPersonaPorRangoFechas(codigoPersona, fechaInicio, fechaFin);
	}


	/** (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IUsuarioXConvenioDAO#existeCargueVigentePorPersonaPorContrato(int, java.util.Date, int)
	 */
	@Override
	public boolean existeCargueVigentePorPersonaPorContrato(int codigoPersona,
			Date fechaActual, int codigoContrato) {
		return delegate.existeCargueVigentePorPersonaPorContrato(codigoPersona, fechaActual, codigoContrato);
	}
	
	
}
