package com.servinte.axioma.orm.delegate.facturacion.convenio;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.servinte.axioma.orm.UsuariosEntidadSub;
import com.servinte.axioma.orm.UsuariosEntidadSubHome;


/**
 * Esta clase se encarga de de manejar las transaccciones relacionadas
 * con la entidad Convenios
 * 
 * @author Cristhian Murillo
 *
 */
@SuppressWarnings("unchecked")
public class UsuariosEntidadSubDelegate extends UsuariosEntidadSubHome  {

	
	/**
	 * Busca registros por los parametros de busqueda.
	 * Este método puede ser utilizado para validar si el usuario tiene permisos sobre la entidad subcontratada.
	 * 
	 * @author Cristhian Murillo
	 * @param login
	 * @param entidadSub
	 * @return ArrayList<UsuariosEntidadSub>
	 */
	public ArrayList<UsuariosEntidadSub> buscarUsuariosEntidadSubPorUsuarioEntidad(String login, long entidadSub)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(UsuariosEntidadSub.class, "usuariosEntidadSub");
		
		criteria.createAlias("usuariosEntidadSub.usuariosByUsuario"			, "usuariosByUsuario");
		criteria.createAlias("usuariosEntidadSub.entidadesSubcontratadas"	, "entidadesSubcontratadas"); 
		
		criteria.add(Restrictions.eq("usuariosByUsuario.login"			, login));
		criteria.add(Restrictions.eq("entidadesSubcontratadas.codigoPk"	, entidadSub));
		
		return (ArrayList<UsuariosEntidadSub>)criteria.list();
	}
	
}
