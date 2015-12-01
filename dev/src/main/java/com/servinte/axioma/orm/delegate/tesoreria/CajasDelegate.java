package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import util.ConstantesBD;
import util.UtilidadTexto;

import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.CajasHome;
import com.servinte.axioma.orm.TurnoDeCaja;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene logica del negocio sobre el modelo 
 */
public class CajasDelegate extends CajasHome{
	
	
	/**
	 * M&eacute;todo que lista de Cajas de un usuario pasando una instancia del objeto <code>UsuarioBasico</code>
	 * @param usuario
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Cajas> listarCajasPorUsuarioPorInstitucion(UsuarioBasico usuario)
	{
		
		return (ArrayList<Cajas>) sessionFactory.getCurrentSession()
				.createCriteria(Cajas.class)
				.add(Expression.eq("activo", true))
				.add(Expression.eq("instituciones.codigo", Integer.parseInt(usuario.getCodigoInstitucion())))
				.createCriteria("cajasCajeroses")
						.add(Expression.eq("usuarios.login", usuario.getLoginUsuario()))
				.list();
	}
	
	/**
	 * M&eacute;todo que lista de Cajas por la instituci&oacute;n y por tipo de caja (Caja Mayor - Caja Principal - Caja Recaudo)
	 *
	 * @param codigoInstitucion
	 * @param constanteBDTipoCaja
	 * @return lista de Cajas por la instituci&oacute;n y por tipo de caja (Caja Mayor - Caja Principal - Caja Recaudo)
	 */
	@SuppressWarnings("unchecked")
	public List<Cajas> listarCajasPorInstitucionPorTipoCaja(int codigoInstitucion, int constanteBDTipoCaja)
	{
		List<Cajas> listadoCajas =  (List<Cajas>) sessionFactory.getCurrentSession()
				.createCriteria(Cajas.class)
				.add(Restrictions.eq("activo", true))
				.add(Restrictions.eq("instituciones.codigo", codigoInstitucion))
				.add(Restrictions.eq("tiposCaja.codigo",constanteBDTipoCaja))
				.list();
	
		for (Cajas cajas : listadoCajas) {
			
			cajas.getCentroAtencion().getDescripcion();
		}
		
		return listadoCajas;
	}
	
	
	
	/**
	 * lista de Cajas activas por institucion para el cajero enviado y 
	 * que esten activas en el sistema y para ese cajero
	 * @param usuario
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Cajas> listarCajasPorCajeroActivasPorInstitucion(UsuarioBasico usuario, int constanteBDTipoCaja)
	{
		ArrayList<Cajas> listadoCajas = (ArrayList<Cajas>) sessionFactory.getCurrentSession()
			.createCriteria(Cajas.class)
			
			.add(Expression.eq("activo", true))
			.add(Expression.eq("instituciones.codigo", Integer.parseInt(usuario.getCodigoInstitucion())))
			.add(Expression.eq("tiposCaja.codigo",constanteBDTipoCaja))
			.createCriteria("cajasCajeroses")
				.add(Expression.eq("activo",true))
				.add(Expression.eq("usuarios.login", usuario.getLoginUsuario()))
				
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				
			.list() ;
		
		for (Cajas cajas : listadoCajas) {
			
			cajas.getCentroAtencion().getDescripcion();
		}
		
		return listadoCajas;
	}
	
	
	
	
	/**
	 * lista de Cajas activas por institucion y centro de atenci&oacute;n para el cajero enviado y 
	 * que esten activas en el sistema y para ese cajero
	 * @param usuario
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Cajas> listarCajasPorCajeroActivasXInstitucionXCentroAtencion(UsuarioBasico usuario, int constanteBDTipoCaja)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Cajas.class,"cajas");
		
		criteria.createAlias("cajas.instituciones"		, "instituciones");
		criteria.createAlias("cajas.tiposCaja"			, "tiposCaja");
		criteria.createAlias("cajas.centroAtencion"		, "centroAtencion");
		
		
		criteria.add(Expression.eq("cajas.activo", true));
		criteria.add(Expression.eq("instituciones.codigo"		, Integer.parseInt(usuario.getCodigoInstitucion())));
		criteria.add(Expression.eq("tiposCaja.codigo"			, constanteBDTipoCaja));
		
		if(usuario.getCodigoCentroAtencion() != ConstantesBD.codigoNuncaValido){
			criteria.add(Expression.eq("centroAtencion.consecutivo"	, usuario.getCodigoCentroAtencion()));
		}
		
		if(!UtilidadTexto.isEmpty(usuario.getLoginUsuario()))
		{
			criteria.createAlias("cajas.cajasCajeroses"		, "cajasCajeroses");
			criteria.createAlias("cajasCajeroses.usuarios"	, "usuarios");
			
			criteria.add(Expression.eq("usuarios.login"			, usuario.getLoginUsuario()));
			criteria.add(Expression.eq("cajasCajeroses.activo"	, true));
		}
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		ArrayList<Cajas> listadoCajas = (ArrayList<Cajas>) criteria.list();
		
		for (Cajas cajas : listadoCajas) 
		{
			cajas.getCentroAtencion().getDescripcion();
			cajas.getInstituciones().getRazonSocial();
		}
		
		return listadoCajas;
	}
	
	

	

	/**
	 * lista de Cajas activas por institucion y centro de atenci&oacute;n para el cajero enviado y 
	 * que esten activas en el sistema y para ese cajero
	 * @param usuario
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Cajas> obtenerCajasPorCajeroActivasXCentroAtencion(DtoUsuarioPersona usuario, int codigoCentroAtencion, int constanteBDTipoCaja)
	{
		List<Cajas> listadoCajas = (List<Cajas>) sessionFactory.getCurrentSession()
			.createCriteria(Cajas.class)
			.createAlias("centroAtencion", "cenAten")
			.add(Expression.eq("activo", true))
			.add(Expression.eq("tiposCaja.codigo",constanteBDTipoCaja))
			.add(Expression.eq("centroAtencion.consecutivo",codigoCentroAtencion))
			.addOrder(Property.forName("codigo").asc())
			.createCriteria("cajasCajeroses")
				.add(Expression.eq("activo",true))
				.add(Expression.eq("usuarios.login", usuario.getLogin()))
			.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				
			.list() ;
		
		for (Cajas cajas : listadoCajas) {
			
			cajas.getCentroAtencion().getDescripcion();
		}
		
		return listadoCajas;
	}
	
	/**
	 * M&eacute;todo que lista las Cajas por el centro de atenci&oacute;n y 
	 * por el tipo de caja (Caja Mayor - Caja Principal - Caja Recaudo)
	 *
	 * @param codigoCentroAtencion
	 * @param constanteBDTipoCaja
	 * @return lista de Cajas por el centro de institucion y por tipo de 
	 * caja (Caja Mayor - Caja Principal - Caja Recaudo)
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Cajas> listarCajasPorCentrosAtencionPorTipoCaja(int codigoCA, int constanteBDTipoCaja)
	{
		ArrayList<Cajas> listaCajas = new ArrayList<Cajas>();
		
		listaCajas = (ArrayList<Cajas>) sessionFactory.getCurrentSession()
				.createCriteria(Cajas.class)
				.add(Restrictions.eq("activo", true))				
				.add(Restrictions.eq("centroAtencion.consecutivo", codigoCA))
				.add(Restrictions.eq("tiposCaja.codigo",constanteBDTipoCaja))
				.addOrder(Property.forName("codigo").asc())
				.list();	
		 
		for (Cajas caja : listaCajas) {
			caja.getCentroAtencion().getDescripcion();
		}
		
		return listaCajas;
	}
	

	
	/**
	 * lista de Cajas activas por institucion y centro de atenci&oacute;n para el cajero enviado y 
	 * que esten activas en el sistema y para ese cajero.
	 * El campo integridadDominioEstadoTurnoExcluir se utiliza para EXCLUIR las cajas que tengan 
	 * ese estado.
	 * @param usuario
	 * @param constanteBDTipoCaja
	 * @param integridadDominioEstadoTurnoExcluir
	 * @return ArrayList<Cajas>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes"})
	public ArrayList<Cajas> listarCajasPorCajeroActivasXInstitucionXCentroAtencionTurno(
			UsuarioBasico usuario, int constanteBDTipoCaja, String integridadDominioEstadoTurnoExcluir)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Cajas.class);
		
		criteria.createAlias("instituciones"	, "instituciones");
		criteria.createAlias("tiposCaja"		, "tiposCaja");
		criteria.createAlias("centroAtencion"	, "centroAtencion");
		criteria.createAlias("cajasCajeroses"	, "cajasCajeroses");
		criteria.createAlias("turnoDeCajas"		, "turnoDeCajas", Criteria.LEFT_JOIN);
		
		criteria.add(Restrictions.eq("activo"						, true));
		criteria.add(Restrictions.eq("instituciones.codigo"			, Integer.parseInt(usuario.getCodigoInstitucion())));
		criteria.add(Restrictions.eq("tiposCaja.codigo"				,constanteBDTipoCaja));
		criteria.add(Restrictions.eq("centroAtencion.consecutivo"	,usuario.getCodigoCentroAtencion()));
		
		criteria.add(Restrictions.eq("cajasCajeroses.activo"			,true));
		criteria.add(Restrictions.eq("cajasCajeroses.usuarios.login"	, usuario.getLoginUsuario()));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		ArrayList<Cajas> listadoCajas = (ArrayList<Cajas>)criteria.list();
		
		
		ArrayList<Cajas> listadoCajasDefinitivas = new ArrayList<Cajas>();
		
		for (Cajas cajaResultado : listadoCajas) 
		{
			boolean registrarCaja = true;
			for (Cajas cajaCiclo : listadoCajas) 
			{
				if(cajaResultado.getConsecutivo() == cajaCiclo.getConsecutivo()){
					for (Iterator iteratorTurno =  cajaCiclo.getTurnoDeCajas().iterator(); iteratorTurno.hasNext();) 
					{
						TurnoDeCaja turnoCaja = (TurnoDeCaja) iteratorTurno.next();
						if(turnoCaja.getEstado().equalsIgnoreCase(integridadDominioEstadoTurnoExcluir)){
							registrarCaja = false;
						}
					}
				}
			}
			
			if(registrarCaja){ 	listadoCajasDefinitivas.add(cajaResultado);  }
		}
		
		for (Cajas cajas : listadoCajasDefinitivas) 
		{
			cajas.getCentroAtencion().getDescripcion();
			cajas.getInstituciones().getRazonSocial();
			cajas.getTiposCaja();
			cajas.getCajasCajeroses();
		}
		
		return listadoCajasDefinitivas;
	}
	
	
}
