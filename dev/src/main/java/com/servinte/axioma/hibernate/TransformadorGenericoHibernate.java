/**
 * 
 */
package com.servinte.axioma.hibernate;

import java.util.List;

import org.hibernate.property.ChainedPropertyAccessor;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.PropertyAccessorFactory;
import org.hibernate.property.Setter;
import org.hibernate.transform.ResultTransformer;

import com.princetonsa.dto.tesoreria.DtoTarjetasFinancieras;

/**
 * Implementación genérica de result transformer de Hibernate
 * para convertir resultados a DTOs específicos.
 * @author Juan David Ramírez
 * @since 22 Noviembre 2010
 */
@SuppressWarnings("serial")
public abstract class TransformadorGenericoHibernate implements ResultTransformer
{
	protected final Class<DtoTarjetasFinancieras> claseResultado;
	protected Setter[] setters;
	protected PropertyAccessor propertyAccessor;
	
	@SuppressWarnings("unchecked")
	public TransformadorGenericoHibernate(Class claseResultado) {
		if(claseResultado==null) throw new IllegalArgumentException("Clase resultado no puede ser nula");
		this.claseResultado = claseResultado;
		propertyAccessor = new ChainedPropertyAccessor(new PropertyAccessor[] { PropertyAccessorFactory.getPropertyAccessor(claseResultado,null), PropertyAccessorFactory.getPropertyAccessor("field")}); 		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List transformList(List lista)
	{
		return lista;
	}
	
	/**
	 * Asigna propiedades de primer nivel a los DTO
	 * @param alias
	 * @param tarjetaFinanciera
	 * @param object
	 * @param setters 
	 */
	protected void asignarPropiedadGenerica(String alias,
			Object objeto, Object tupla)
	{
		Setter setter = propertyAccessor.getSetter(claseResultado, alias);
		setter.set(objeto, tupla, null);
	}

}
