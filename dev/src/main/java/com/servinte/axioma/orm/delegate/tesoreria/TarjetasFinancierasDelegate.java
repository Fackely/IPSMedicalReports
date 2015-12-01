/**
 * 
 */
package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.princetonsa.dto.tesoreria.DtoTarjetasFinancieras;
import com.princetonsa.dto.tesoreria.DtoTipoTarjetaFinanciera;
import com.servinte.axioma.hibernate.TransformadorGenericoHibernate;
import com.servinte.axioma.orm.TarjetaFinancieraComision;
import com.servinte.axioma.orm.TarjetaFinancieraReteica;
import com.servinte.axioma.orm.TarjetasFinancieras;
import com.servinte.axioma.orm.TarjetasFinancierasHome;

/**
 * @author armando
 *
 */
public class TarjetasFinancierasDelegate extends TarjetasFinancierasHome 
{

	@SuppressWarnings("unchecked")
	public ArrayList<TarjetasFinancieras> listarTarjetasFinancieras() 
	{
		ArrayList<TarjetasFinancieras> listarTarjetasFinancieras = (ArrayList<TarjetasFinancieras>) sessionFactory.getCurrentSession().createCriteria(TarjetasFinancieras.class).addOrder(Order.asc("codigo")).list();
		
		for (TarjetasFinancieras tarjetasFinancieras : listarTarjetasFinancieras) 
		{
			tarjetasFinancieras.getMovimientosTarjetases();
		}
		
		return listarTarjetasFinancieras;
	}

	/**
	 * Lista las tarjetas financieras activas en el sistema
	 * @author Juan David Ramírez
	 * @param tarjetasActivas
	 * @return Lista de {@link DtoTarjetasFinancieras} con las entidades financieras activas del sistema
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoTarjetasFinancieras> listarTarjetasFinancieras(boolean tarjetasActivas) {
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(TarjetasFinancieras.class)
		.add(Restrictions.eq("activo", true))
		.setProjection(Projections.projectionList()
				.add(Projections.property("consecutivo"), "consecutivo")
				.add(Projections.property("codigo"), "codigo")
				.add(Projections.property("descripcion"), "descripcion")
				.add(Projections.property("tiposTarjetaFinanciera.codigo"), "tipoTrjeta.descripcion")
		)
		//.setResultTransformer(Transformers.aliasToBean(DtoTarjetasFinancieras.class));
		.setResultTransformer(new TransformadorTarjetasFinancieras());
		ArrayList<DtoTarjetasFinancieras> listaTarjetas=(ArrayList<DtoTarjetasFinancieras>) criteria.list();
		return listaTarjetas;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public TarjetasFinancieras cargarTarjeta(int consecutivoTarjeta){
		
		TarjetasFinancieras tarjeta= findById(consecutivoTarjeta);
	
		if(tarjeta!=null){
			
			Set<TarjetaFinancieraComision> tarjetasFinancierasComision = tarjeta.getTarjetaFinancieraComisions();
			
			for (TarjetaFinancieraComision comision : tarjetasFinancierasComision) {
				
				if(comision!=null){
					
					comision.getEntidadesFinancieras().getTerceros().getDescripcion();
				}
			}
			
			Set<TarjetaFinancieraReteica> tarjetasFinancierasReteICA = tarjeta.getTarjetaFinancieraReteicas();
			
			for (TarjetaFinancieraReteica reteICA : tarjetasFinancierasReteICA) {
				
				if(reteICA!=null){
					
					reteICA.getCentroAtencion().getDescripcion();
				}
			}
			
			//tarjeta.setTarjetaFinancieraReteicas(tarjetasFinancierasReteICA);
			tarjeta.getTiposTarjetaFinanciera().getDescripcion();
		}
		
		return tarjeta;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<TarjetaFinancieraReteica> obtenerTarjetaFinancieraReteicaOrdenada (int consecutivoTarjeta){
		
		return (List<TarjetaFinancieraReteica>) sessionFactory.getCurrentSession().createCriteria(TarjetaFinancieraReteica.class)
		.createAlias("centroAtencion", "centroAtencion")
		.add(Restrictions.eq("tarjetasFinancieras.consecutivo", consecutivoTarjeta))
		.addOrder(Order.desc("centroAtencion.descripcion")).list() ;
	}

}


@SuppressWarnings("serial")
class TransformadorTarjetasFinancieras extends TransformadorGenericoHibernate
{
	/**
	 * Constructor para obtener las propiedades
	 * @param claseResultado clase a la cual se piensa convertir el objeto 
	 */
	public TransformadorTarjetasFinancieras()
	{
		super(DtoTarjetasFinancieras.class);
	}

	@Override
	public Object transformTuple(Object[] tuple, String[] aliases)
	{
		DtoTarjetasFinancieras tarjetaFinanciera=new DtoTarjetasFinancieras();
		
		for (int i = 0; i < aliases.length; i++) {
			String alias = aliases[i];
			if(alias != null) {
				if(alias.contains("tipoTrjeta.descripcion"))
				{
					tarjetaFinanciera.setTipoTrjeta(new DtoTipoTarjetaFinanciera());
					tarjetaFinanciera.getTipoTrjeta().setDescripcion((Character)tuple[i]+"");
				}
				else{
					
					asignarPropiedadGenerica(alias, tarjetaFinanciera, tuple[i]);
				}
			}
		}
		return tarjetaFinanciera;
	}
}

//
//@SuppressWarnings("serial")
//class TransformadorTarjetasFinancieras extends TransformadorGenericoHibernate
//{
//	/**
//	 * Constructor para obtener las propiedades
//	 * @param claseResultado clase a la cual se piensa convertir el objeto 
//	 */
//	public TransformadorTarjetasFinancieras()
//	{
//		super(DtoTarjetasFinancieras.class);
//	}
//
//	@Override
//	public Object transformTuple(Object[] tuple, String[] aliases)
//	{
//		DtoTarjetasFinancieras tarjetaFinanciera=new DtoTarjetasFinancieras();
//		
//		for (int i = 0; i < aliases.length; i++) {
//			String alias = aliases[i];
//			if(alias != null) {
//				if(alias.contains("."))
//				{
//					String[] propiedades=alias.split("\\.");
//					for(int j=0; j < propiedades.length; j++)
//					{
//						if(propiedades.length-1==j)
//						{
//							asignarPropiedadGenerica(propiedades[j], tarjetaFinanciera, tuple[i]);
//							continue;
//						}
//						Setter setter = propertyAccessor.getSetter(claseResultado, propiedades[j]);
//						Getter getter = propertyAccessor.getGetter(claseResultado, propiedades[j]);
//						
//						Class clase=getter.getClass();
//						try
//						{
//							setter.set(tarjetaFinanciera, clase.newInstance(), null);
//						} catch (HibernateException e)
//						{
//							Log4JManager.error("Error asignando propiedad", e);
//						} catch (InstantiationException e)
//						{
//							Log4JManager.error("Error creando instancia del objeto asignado", e);
//						} catch (IllegalAccessException e)
//						{
//							Log4JManager.error("Error de acceso a las propiedades, verifique visibilidad de los métodos gettesr y setters", e);
//						}
//					}
//				}
//				else{
//					
//					asignarPropiedadGenerica(alias, tarjetaFinanciera, tuple[i]);
//				}
//			}
//		}
//		return tarjetaFinanciera;
//	}
//}