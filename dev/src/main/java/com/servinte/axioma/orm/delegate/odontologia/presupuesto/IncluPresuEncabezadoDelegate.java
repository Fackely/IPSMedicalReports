
package com.servinte.axioma.orm.delegate.odontologia.presupuesto;

import java.util.Iterator;
import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;

import com.servinte.axioma.orm.IncluPresuConvenio;
import com.servinte.axioma.orm.IncluPresuEncabezado;
import com.servinte.axioma.orm.IncluPresuEncabezadoHome;
import com.servinte.axioma.orm.IncluProgramaConvenio;
import com.servinte.axioma.orm.IncluServicioConvenio;
import com.servinte.axioma.orm.InclusionesPresupuesto;


/**
 * Clase que se encarga de manejar las transaccciones relacionadas
 * con el objeto {@link IncluPresuEncabezado}.
 * 
 * @author Jorge Armando Agudelo Quintero
 */
public class IncluPresuEncabezadoDelegate extends IncluPresuEncabezadoHome{

	
	/**
	 * Método que permite realizar un registro de la entidad {@link IncluPresuEncabezado}
	 * 
	 * Con la información del proceso de contratación y precontratación 
	 * de Inclusiones de programas al plan de tratamiento y al presupuesto del paciente.
	 * 
	 * @param excluPresuEncabezado
	 * @return
	 */
	public long registrarInclusionPresupuestoEncabezado (IncluPresuEncabezado incluPresuEncabezado){
		
		try {
			
			super.attachDirty(incluPresuEncabezado);
			
			return incluPresuEncabezado.getCodigoPk();
			
		} catch (Exception e) {
		
			return ConstantesBD.codigoNuncaValidoLong;
		}
	}
	
	/**
	 * Método que carga un encabezado de Inclusión específico
	 * 
	 * @param codigoPresupuesto
	 */
	@SuppressWarnings("unchecked")
	public IncluPresuEncabezado cargarEncabezadoInclusion(long codigoIncluPresuEncabezado){
		
		IncluPresuEncabezado incluPresuEncabezado = super.findById(codigoIncluPresuEncabezado);

		if(incluPresuEncabezado!=null){
			
			if(incluPresuEncabezado.getOtrosSi()!=null){
				
				incluPresuEncabezado.getOtrosSi().getConsecutivo();
			}
			
			incluPresuEncabezado.getUsuarios().getPersonas().getPrimerNombre();
			incluPresuEncabezado.getCentroAtencion().getDescripcion();
			
			for (Iterator<InclusionesPresupuesto> inclusionesPresupuesto = incluPresuEncabezado.getInclusionesPresupuestos().iterator(); inclusionesPresupuesto.hasNext();) {
				
				InclusionesPresupuesto inclusionPresupuesto = (InclusionesPresupuesto) inclusionesPresupuesto.next();
				
				inclusionPresupuesto.getValor();
			}
		}
		
		return incluPresuEncabezado;
	}
	
	
	/**
	 * Método que consulta las inclusiones asociadas al presupuesto activo del paciente
	 * 
	 * @param codigoPresupuesto
	 */
	@SuppressWarnings("unchecked")
	public List<IncluPresuEncabezado> cargarRegistrosInclusion(long codigoPresupuesto){
		
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(IncluPresuEncabezado.class, "encabezado")
		   .createAlias("presupuestoOdontologico", "presupuesto");

		if(codigoPresupuesto > ConstantesBD.codigoNuncaValidoLong)
		{
			criteria.add(Restrictions.eq("presupuesto.codigoPk", codigoPresupuesto));
		}

		criteria.addOrder(Order.desc("encabezado.consecutivo"));
		
		List<IncluPresuEncabezado> listadoInclusiones =criteria.list();
		
		
		for (IncluPresuEncabezado incluPresuEncabezado : listadoInclusiones) {
			
			if(incluPresuEncabezado.getOtrosSi()!=null){
				
				incluPresuEncabezado.getOtrosSi().getConsecutivo();
			}
			
			incluPresuEncabezado.getUsuarios().getPersonas().getPrimerNombre();
			incluPresuEncabezado.getCentroAtencion().getDescripcion();
			incluPresuEncabezado.getPresupuestoOdontologico().getPacientes().getCodigoPaciente();
			
			for (Iterator<InclusionesPresupuesto> inclusionesPresupuesto = incluPresuEncabezado.getInclusionesPresupuestos().iterator(); inclusionesPresupuesto.hasNext();) {
				
				InclusionesPresupuesto inclusionPresupuesto = (InclusionesPresupuesto) inclusionesPresupuesto.next();
				
				inclusionPresupuesto.getValor();
			}
		}
		
		return listadoInclusiones;
	}
	
	

	/**
	 * Método que consulta un registro del proceso de contratación de Inclusiones
	 * 
	 * @param codigoPresupuesto
	 */
	@SuppressWarnings("unchecked")
	public IncluPresuEncabezado cargarDetalleRegistroInclusion(long codigoIncluPresuEncabezado){
		
		IncluPresuEncabezado incluPresuEncabezado = super.findById(codigoIncluPresuEncabezado);
		
		if(incluPresuEncabezado!=null){
			
			if(incluPresuEncabezado.getOtrosSi()!=null){
				
				incluPresuEncabezado.getOtrosSi().getConsecutivo();
			}
			
			incluPresuEncabezado.getCentroAtencion().getDescripcion();
			
			for (Iterator<InclusionesPresupuesto> inclusionesPresupuesto = incluPresuEncabezado.getInclusionesPresupuestos().iterator(); inclusionesPresupuesto.hasNext();) {
				
				InclusionesPresupuesto inclusionPresupuesto = (InclusionesPresupuesto) inclusionesPresupuesto.next();
				inclusionPresupuesto.getValor();
			}
			
			for (Iterator<IncluPresuConvenio> convenios = incluPresuEncabezado.getIncluPresuConvenios().iterator(); convenios.hasNext();) {
				
				IncluPresuConvenio incluPresuConvenio = (IncluPresuConvenio) convenios.next();
				
				incluPresuConvenio.getContratos().getCodigo();
				
				for (Iterator<IncluProgramaConvenio> programasConvenio = incluPresuConvenio.getIncluProgramaConvenios().iterator(); programasConvenio.hasNext();) {
					
					IncluProgramaConvenio incluProgramaConvenio = (IncluProgramaConvenio) programasConvenio.next();
					
					incluProgramaConvenio.getCantidad();
					incluProgramaConvenio.getProgramas().getCodigoPrograma();
					incluProgramaConvenio.getProgramas().getEspecialidades().getNombre();
			
					for (Iterator<IncluServicioConvenio> serviciosConvenio = incluProgramaConvenio.getIncluServicioConvenios().iterator(); serviciosConvenio.hasNext();) {
						
						IncluServicioConvenio incluServicioConvenio = (IncluServicioConvenio) serviciosConvenio.next();
						
						incluServicioConvenio.getCantidad();
						incluServicioConvenio.getEsquemasTarifarios().getNombre();
					}
				}
			}
		}
		
		return incluPresuEncabezado;
	}
	
	/**
	 * Método que realiza el proceso de actualización de un registro de 
	 * Inclusión precontratada
	 * 
	 * @param incluPresuEncabezado
	 * @return
	 */
	public boolean actualizarIncluPresuEncabezado (IncluPresuEncabezado incluPresuEncabezado){
		
		boolean resultado = false;
		
		try {
			
			IncluPresuEncabezado incluEncabezadoModificar = super.findById(incluPresuEncabezado.getCodigoPk());
			
			if(incluEncabezadoModificar!=null){
				
				incluEncabezadoModificar.setEstado(incluPresuEncabezado.getEstado());
				incluEncabezadoModificar.setUsuarios(incluPresuEncabezado.getUsuarios());
				incluEncabezadoModificar.setFecha(incluPresuEncabezado.getFecha());
				incluEncabezadoModificar.setHora(incluPresuEncabezado.getHora());
				incluEncabezadoModificar.setCentroAtencion(incluPresuEncabezado.getCentroAtencion());
				
				if(incluPresuEncabezado.getOtrosSi()!=null){
					
					incluEncabezadoModificar.setOtrosSi(incluPresuEncabezado.getOtrosSi());
				}
				
				
				/*
				 * Cuando se va a contratar el encabezado de inclusión se deben
				 * eliminar los registros de InclusionesPresupuestos asociados
				 */
				if(incluPresuEncabezado.getEstado().equals(ConstantesIntegridadDominio.acronimoContratado)){
					
					incluEncabezadoModificar.getInclusionesPresupuestos().clear();

					InclusionesPresupuestoDelegate inclusionesDelegate = new InclusionesPresupuestoDelegate();
					
					inclusionesDelegate.eliminarInclusionesPorIncluPresuEncabezado(incluPresuEncabezado.getCodigoPk());
				}
		
				super.attachDirty(incluEncabezadoModificar);
				
				resultado = true;
			}
			
		} catch (Exception e) {
		
			Log4JManager.info(e);
		}
		
		return resultado;
	}

	/**
	 * Eliminar todo el detalle de la inclusión sin eliminar el encabezado
	 * ya que no se puede perder el consecutivo asignado
	 * @param encabezadoInclusionPresupuesto Llave primaria del encabezado (Tabla inclu_presu_encabezado, Columna codigo_pk)
	 * @return true en caso de guardar correctamente, false de lo contrario
	 */

	public boolean eliminarDetalleInclusiones(long encabezadoInclusionPresupuesto)
	{
		
		try {
			
			// se elimina la relación de programa_hallazgo_pieza con las inclusiones
			eliminarInclusionesPresupuesto(encabezadoInclusionPresupuesto);		
			
			// se elimina la relación con el detalle de las promociones
			eliminarIncluPresProgramaPromo(encabezadoInclusionPresupuesto);
			
		
			// Se eliminan los programas asociados a un convenio
			eliminarIncluProgramaConvenio(encabezadoInclusionPresupuesto);
			
			// Se eliminan los servicios asociados a un convenio
			eliminarIncluServicioConvenio(encabezadoInclusionPresupuesto);
			
			// Se eliminan los convenios de las inclusiones contratadas 
			eliminarIncluPresuConvenio(encabezadoInclusionPresupuesto);
			
			return true;
			
		} catch (Exception e) {
			
			Log4JManager.error(e);
			return false;
		}
	}

	private void eliminarInclusionesPresupuesto(
			long encabezadoInclusionPresupuesto)
	{
		String hql=
			"DELETE " +
			"FROM " +
			"InclusionesPresupuesto " +
			"WHERE " +
			"incluPresuEncabezado=:encabezadoInclusionPresupuesto";
		Query sentencia=sessionFactory.getCurrentSession().createQuery(hql);
		sentencia.setLong("encabezadoInclusionPresupuesto", encabezadoInclusionPresupuesto);
		sentencia.executeUpdate();
	}

	private void eliminarIncluPresProgramaPromo(long encabezadoInclusionPresupuesto)
	{
		String hql=
			"DELETE " +
			"FROM " +
			"IncluPresProgramaPromo " +
			"WHERE " +
				"incluProgramaConvenio " +
				"IN(" +
					"SELECT codigoPk " +
					"FROM " +
					"IncluProgramaConvenio " +
					"WHERE " +
						"incluPresuConvenio IN(" +
							"SELECT " +
							"codigoPk " +
							"FROM " +
							"IncluPresuConvenio " +
							"WHERE " +
							"incluPresuEncabezado=:encabezadoInclusionPresupuesto" +
						")"+
				")";
		Query sentencia=sessionFactory.getCurrentSession().createQuery(hql);
		sentencia.setLong("encabezadoInclusionPresupuesto", encabezadoInclusionPresupuesto);
		sentencia.executeUpdate();
	}

	/**
	 * Elimina los registros de le tabla inclu_programa_convenio
	 * para un encabezado
	 * @param encabezadoInclusionPresupuesto
	 */
	private void eliminarIncluServicioConvenio(long encabezadoInclusionPresupuesto)
	{
		String hql=
			"DELETE " +
			"FROM " +
			"IncluServicioConvenio " +
			"WHERE " +
				"incluPresuConvenio IN(" +
					"SELECT " +
					"codigoPk " +
					"FROM " +
					"IncluPresuConvenio " +
					"WHERE " +
					"incluPresuEncabezado=:encabezadoInclusionPresupuesto" +
				")";
		Query sentencia=sessionFactory.getCurrentSession().createQuery(hql);
		sentencia.setLong("encabezadoInclusionPresupuesto", encabezadoInclusionPresupuesto);
		sentencia.executeUpdate();
	}

	/**
	 * Elimina los registros de le tabla inclu_presu_convenio
	 * para un encabezado
	 * @param encabezadoInclusionPresupuesto
	 */
	private void eliminarIncluPresuConvenio(long encabezadoInclusionPresupuesto)
	{
		String hql=
				"DELETE " +
				"FROM " +
				"IncluPresuConvenio " +
				"WHERE " +
				"incluPresuEncabezado=:encabezadoInclusionPresupuesto";
		Query sentencia=sessionFactory.getCurrentSession().createQuery(hql);
		sentencia.setLong("encabezadoInclusionPresupuesto", encabezadoInclusionPresupuesto);
		sentencia.executeUpdate();
	}

	/**
	 * Elimina los registros de le tabla inclu_programa_convenio
	 * para un encabezado
	 * @param encabezadoInclusionPresupuesto
	 */
	@SuppressWarnings("unchecked")
	private void eliminarIncluProgramaConvenio(long encabezadoInclusionPresupuesto)
	{
		
		DetachedCriteria dc=DetachedCriteria.forClass(IncluPresuConvenio.class);
		dc.add(Restrictions.eq("incluPresuEncabezado.codigoPk", encabezadoInclusionPresupuesto)).setProjection(Property.forName("codigoPk"));
		
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(IncluProgramaConvenio.class).
		add(Property.forName("incluPresuConvenio.codigoPk").in(dc));
		
		
		List<IncluProgramaConvenio> listaProgramasConvenio=(List<IncluProgramaConvenio>)criteria.list();
		
		for (IncluProgramaConvenio incluProgramaConvenio : listaProgramasConvenio) {
			
			IncluProgramaConvenioDelegate delegado=new IncluProgramaConvenioDelegate();
			
			delegado.delete(incluProgramaConvenio);
		}
	}
}
