/**
 * 
 */
package com.servinte.axioma.orm.delegate.manejoPaciente;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.servinte.axioma.orm.CajasCajeros;
import com.servinte.axioma.orm.SubCuentas;
import com.servinte.axioma.orm.SubCuentasHome;

/**
 * @author armando
 *
 */
@SuppressWarnings("unchecked")
public class SubCuentasDelegate extends SubCuentasHome 
{

	/**
	 * Lista todas las subcuentas del paciente dado
	 * @param paciente
	 * @return ArrayList<SubCuentas>
	 */
	public ArrayList<SubCuentas> listarCuentasPorPaciente (int paciente){
		
		return (ArrayList<SubCuentas>) sessionFactory.getCurrentSession()
				.createCriteria(SubCuentas.class)
				.add(Expression.eq("pacientes.codigoPaciente",paciente))
				.list();
	}
	
	
	/**
	 * Carga una subcuenta por su id y su detalle monto
	 * @param id de la subcuenta
	 * @return SubCuentas
	 */
	public SubCuentas cargarSubcuentaDetalleMonto(int codigoResponsable){
		
	SubCuentas detallesSubcuenta = null;
	Criteria criteria = sessionFactory.getCurrentSession().
	createCriteria(SubCuentas.class,"subCuentas")
	//.createAlias("subCuentas.detalleMonto", "detalleMonto")
	//.createAlias("detalleMonto.detalleMontoGeneral", "detalleMontoGeneral")	   
	.add(Restrictions.eq("subCuentas.subCuenta", codigoResponsable));
    detallesSubcuenta = (SubCuentas) criteria.list().get(0);
	
	//detallesSubcuenta.getDetalleMonto().getDetalleMontoGeneral().getDetalleCodigo();
	
	return detallesSubcuenta;
	
	}
	
    public static void main(String args[])
	{
		SubCuentasDelegate s= new SubCuentasDelegate();
		s.cargarSubcuentaDetalleMonto(170886);
	}
	
	/**
	 * Retorna Los cajeros relacionados en cajas_cajeros
	 * @param usuario
	 * @return List<{@link CajasCajeros}>
	 */
	/*public List<Usuarios> obtenerListadoCajeros()
	{
		ArrayList<Usuarios> listadoCajeros = (ArrayList<Usuarios>) sessionFactory.getCurrentSession()
			.createCriteria(CajasCajeros.class)
			.createAlias("usuarios", "usuarios")
			//.createAlias("usuarios.personas", "persona")
			//.addOrder(Property.forName("persona.primerApellido").asc())
			.setProjection(Projections.distinct(Projections.projectionList()
				.add((Projections.property("usuarios")))
				//.add((Projections.property("persona.primerApellido")))
				))
			//.setProjection(Projections.property("persona.primerApellido"))
			//.addOrder(Order.asc("persona.primerApellido"))
			.add(Restrictions.eq("activo", true))
		//.setResultTransformer( Transformers.aliasToBean(Usuarios.class))
	         .list();

//		String query = "Usuarios as u FROM CajasCajeros as c WHERE c.usuarios.login = u.login ORDER BY u.personas.primerApellido asc";
//		
//		ArrayList<Usuarios> listadoCajeros = (ArrayList<Usuarios>) sessionFactory.getCurrentSession()
//			.createQuery(query).list();
//		
		for(Usuarios usuario: listadoCajeros)
		{
			usuario.getPersonas().getPrimerNombre();
		}
		
		return listadoCajeros;
		
		//return ordernarUsuariosPorApellido(listadoLoginCajeros);
		
	}*/
}
