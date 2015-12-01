package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.ArrayList;
import org.hibernate.Query;
import org.hibernate.Session;
import util.UtilidadTexto;
import com.servinte.axioma.orm.CentrosAtenTransportadoraHome;


/**
 * 
 * @author axioma
 *
 */
public class CentrosAtenTransportadoraDelegate extends CentrosAtenTransportadoraHome {
	
	
	
	
	/**
	 * ELIMINAR LOS CENTRO ATENCION TRANSPORTADORA
	 * RECIBE UNA LISTA DE CODIGOS  
	 * @param listaCodigoCentroAtencion
	 */
	public void eliminarTrasnportadoras(ArrayList<Integer> listaCodigoCentroAtencion)
	{
		String listaCodigos=UtilidadTexto.convertirArrayIntegerACodigosSeparadosXComas(listaCodigoCentroAtencion);
		
		String hql="delete from   CentrosAtenTransportadora  centroTranportadora where  codigoPk in ("+listaCodigos+")";
		Session sess=  sessionFactory.getCurrentSession();
		Query query= sess.createQuery(hql);
		query.executeUpdate();
		
	}

	
	
}
