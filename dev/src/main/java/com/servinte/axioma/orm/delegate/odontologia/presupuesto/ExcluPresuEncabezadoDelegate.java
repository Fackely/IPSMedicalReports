
package com.servinte.axioma.orm.delegate.odontologia.presupuesto;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import util.ConstantesBD;

import com.servinte.axioma.orm.ExcluPresuEncabezado;
import com.servinte.axioma.orm.ExcluPresuEncabezadoHome;
import com.servinte.axioma.orm.ExclusionesPresupuesto;


/**
 * Clase que se encarga de manejar las transaccciones relacionadas
 * con el objeto {@link ExcluPresuEncabezado}.
 * 
 * @author Jorge Armando Agudelo Quintero
 */
public class ExcluPresuEncabezadoDelegate extends ExcluPresuEncabezadoHome{

	
	/**
	 * Método que permite realizar un registro de la entidad 
	 * 
	 * Con la información del proceso de aprobación de exclusiones de programas
	 * al plan de tratamiento y al presupuesto del paciente.
	 * 
	 * @param excluPresuEncabezado
	 * @return
	 */
	public long registrarExclusionPresupuestoEncabezado (ExcluPresuEncabezado excluPresuEncabezado){
		
		try {
			
			super.persist(excluPresuEncabezado);
			
			return excluPresuEncabezado.getCodigoPk();
			
		} catch (Exception e) {
		
			return ConstantesBD.codigoNuncaValidoLong;
		}
	}
	
	
	/**
	 * Método que consulta las exclusiones asociadas al presupuesto activo del paciente
	 * 
	 * @param codigoPresupuesto
	 */

	@SuppressWarnings("unchecked")
	public List<ExcluPresuEncabezado> cargarRegistrosExclusion(long codigoPresupuesto){
		
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ExcluPresuEncabezado.class, "encabezado")
		   .createAlias("presupuestoOdontologico", "presupuesto");

		if(codigoPresupuesto > ConstantesBD.codigoNuncaValidoLong)
		{
			criteria.add(Restrictions.eq("presupuesto.codigoPk", codigoPresupuesto));
		}

		List<ExcluPresuEncabezado> lista =criteria.list();

		for (ExcluPresuEncabezado excluPresuEncabezado : lista) {
			
			excluPresuEncabezado.getOtrosSi().getConsecutivo();
			excluPresuEncabezado.getUsuarios().getPersonas().getPrimerNombre();

			for (Iterator<ExclusionesPresupuesto> exclusionesPresupuesto = excluPresuEncabezado.getExclusionesPresupuestos().iterator(); exclusionesPresupuesto.hasNext();) {
				
				ExclusionesPresupuesto exclusionPresupuesto = (ExclusionesPresupuesto) exclusionesPresupuesto.next();
				
				if(exclusionPresupuesto.getProgramas()!=null){
					
					exclusionPresupuesto.getProgramas().getCodigoPrograma();
					exclusionPresupuesto.getProgramas().getEspecialidades().getNombre();
				}
				
				if(exclusionPresupuesto.getServicios()!=null){
					
					exclusionPresupuesto.getServicios().getCodigo();
					exclusionPresupuesto.getServicios().getEspecialidades().getNombre();
				}
			}
		}
		
		return lista;
	}
}
