package com.servinte.axioma.bl.manejoPaciente.interfaz;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.servinte.axioma.dto.manejoPaciente.ViaIngresoDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.orm.CentroAtencion;

/**
 * Interface que expone los servicios de Negocio correspondientes a la lógica asociada a los
 * catalogos o paramétricas del módulo de Manejo del Paciente
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:01 p.m.
 */
public interface ICatalogoManejoPacienteMundo {

	
	/**
	 * Servicio encargado de obtener las vias de ingreso parametrizadas
	 * en el sistema
	 * 
	 * @return
	 * @throws IPSException
	 */
	List<ViaIngresoDto> consultarViasIngreso() throws IPSException;

	
	/**
	 * Servicio encargado de obtener el detalle de la viaIngrso por id
	 * en el sistema 
	 * 
	 * @param codigo
	 * 
	 * @return
	 * @throws IPSException
	 */
	ViaIngresoDto consultarViaIngresoPorId(int codigo) throws IPSException;
	
	
	/**
	 * Servicio encargado de verificar si para un servicio existe parámetrización de 
	 * unidad de consulta por centro de costo
	 * @param codigoServicio
	 * @param codigoCentroCosto
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public boolean existeCentroCostoParametrizadoPorUnidadConsulta(int codigoServicio, int codigoCentroCosto) throws IPSException;
	
	/**
	 * Servicio encargado de verificar si para un grupo de servicio existe parámetrización de 
	 * grupo servicios por centro de costo
	 * @param codigoGrupoServicio
	 * @param codigoCentroCosto
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public boolean existeCentroCostoParametrizadoPorGrupoServicio(Integer codigoGrupoServicio, int codigoCentroCosto) throws IPSException;
	
	
	/**
	 * Retorna los centros de atencion dependiendo su estado que tengan parametrizada una ciudad.
	 * @param Boolean estado. Si estado true retorna los activos, si estado false retorna los inactivos, si es null retorna todos los centros de atencion.
	 * @param boolean requiereTransaccion 
	 * @param int codigoInstitucion
	 * @return
	 * @throws IPSException
	 */
	public List<DtoCentrosAtencion> listarTodosCentrosAtencion(boolean requiereTransaccion, Boolean estado, int codigoInstitucion) throws IPSException;
	
	
	
}