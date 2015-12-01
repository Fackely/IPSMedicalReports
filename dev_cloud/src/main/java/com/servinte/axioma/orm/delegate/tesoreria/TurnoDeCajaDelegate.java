/*
 * Mayo 12, 2010
 */
package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;

import com.princetonsa.dto.tesoreria.DtoCaja;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.RegistroEgresosDeCaja;
import com.servinte.axioma.orm.TurnoDeCaja;
import com.servinte.axioma.orm.TurnoDeCajaHome;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene logica del negocio sobre el modelo 
 */
public class TurnoDeCajaDelegate extends TurnoDeCajaHome{
	
	
	
	/**
	 * Lista todos
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TurnoDeCaja> listarTodos()
	{
		return (ArrayList<TurnoDeCaja>) sessionFactory.getCurrentSession()
			.createCriteria(RegistroEgresosDeCaja.class)
			.list();
	}

	
	/**
	 * Retorna una el objeto por un campo en especifico
	 * @param campoTabla
	 * @param campoDato
	 */
	public TurnoDeCaja buscarPorCampo(String campoTabla, String campoDato) 
	{
		try {
			return (TurnoDeCaja) sessionFactory.getCurrentSession()
					.createCriteria(TurnoDeCaja.class)
					.add(Expression.eq(campoTabla, campoDato))
					.uniqueResult();
			
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	
	
	/**
	 * Retorna una lista si el usuario enviado tiene turnos de caja abiertos
	 * @param usuario
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TurnoDeCaja> obtenerTurnoCajaAbiertoPorCajero(UsuarioBasico usuario)
	{
		return (ArrayList<TurnoDeCaja>) sessionFactory.getCurrentSession()
			.createCriteria(TurnoDeCaja.class)
			.add(Expression.eq("estado", ConstantesIntegridadDominio.acronimoEstadoAperturaDeCajaAbierto))
			.add(Expression.eq("usuarios.login", usuario.getLoginUsuario()))
			.list();
	}
	
	
	
	/**
	 * Retorna una lista si la caja enviada tiene turnos de caja abiertos
	 * @param usuario
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TurnoDeCaja> obtenerTurnoCajaAbiertoPorCaja(Cajas caja)
	{
		
		return (ArrayList<TurnoDeCaja>) sessionFactory.getCurrentSession()
			.createCriteria(TurnoDeCaja.class)
			.createAlias("usuarios", "usuario")
			.add(Expression.eq("estado", ConstantesIntegridadDominio.acronimoEstadoAperturaDeCajaAbierto))
			.add(Expression.eq("cajas.consecutivo", caja.getConsecutivo()))
			.list();
		
		
		
	}
	
	
	
	/**
	 * 
	 * Retorna un {@link TurnoDeCaja} si el usuario enviado tiene uno en estado abierto y asociado a esa caja,
	 * a el centro de atenci&oacute;n espec&iacute;fico.
	 * 	
	 * @param usuario
	 * @param consecutivoCaja
	 * @param consecutivoCentroAtencion
	 * @return TurnoDeCaja para ese usuario/cajero en estado abierto.
	 */
	public TurnoDeCaja obtenerTurnoCajaAbiertoPorCajaCajero(DtoUsuarioPersona usuario, int consecutivoCaja, int consecutivoCentroAtencion)
	{
		
		TurnoDeCaja turnoDeCaja = (TurnoDeCaja) sessionFactory.getCurrentSession()
			.createCriteria(TurnoDeCaja.class)
			.add(Restrictions.eq("estado", ConstantesIntegridadDominio.acronimoEstadoAperturaDeCajaAbierto))
			.add(Restrictions.eq("usuarios.login", usuario.getLogin()))
			.add(Restrictions.eq("cajas.consecutivo", consecutivoCaja))
			.add(Restrictions.eq("centroAtencion.consecutivo", consecutivoCentroAtencion))
			.uniqueResult();
		
		if(turnoDeCaja!=null){
			
			turnoDeCaja.getUsuarios().getPersonas().getPrimerApellido();
			turnoDeCaja.getCajas().getDescripcion();
			turnoDeCaja.getCajas().getCentroAtencion().getDescripcion();
		}
	
		return turnoDeCaja;
		
	}
	
	
	
	/**
	 * Retorna el turno
	 * @param pk
	 */
	@Override
	public TurnoDeCaja findById(long id) {
		return super.findById(id);
	}
	
	
	
	/**
	 * Retorna el turno de caja del movimiento dado
	 * @param idMovimientoCaja
	 * @return TurnoDeCaja
	 */
	public TurnoDeCaja obtenerTurnoPorMovimiento(long idMovimientoCaja) 
	{
		return (TurnoDeCaja) sessionFactory.getCurrentSession()
					.createCriteria(TurnoDeCaja.class)
					.createAlias("movimientosCajas", "mc")
					.add(Expression.eq("mc.codigoPk", idMovimientoCaja))
					.uniqueResult();
	}

	/**
	 * Verifica si una caja tiene turno abierto
	 * @param loginUsuario Login del usuario de la caja
	 * @param tiposCaja Tipos de caja que se desea consultar (ConstantesBD.codigoTipoCajaRecaudado, ConstantesBD.codigoTipoCajaPrincipal, ConstantesBD.codigoTipoCajaMayor)
	 * @param consecutivoCA Consecutivo del centro de atención para validar si se tiene caja activa en éste
	 * @return {@link DtoCaja} Caja abierta, null en caso de no tener turno abierto
	 */
	public DtoCaja validarTurnoUsuario(String login, Integer[] tiposCaja, int consecutivoCA)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TurnoDeCaja.class, "turno_de_caja")
		.createAlias("cajas", "caja");
		for(Integer tipoCaja:tiposCaja)
		{
			criteria.add(Restrictions.eq("caja.tiposCaja.codigo", tipoCaja));
		}
		criteria.add(Restrictions.eq("caja.activo", true));
		criteria.add(Restrictions.eq("turno_de_caja.estado", ConstantesIntegridadDominio.acronimoEstadoAperturaDeCajaAbierto));
		criteria.add(Restrictions.eq("turno_de_caja.usuarios.login", login));
		criteria.add(Restrictions.eq("turno_de_caja.centroAtencion.consecutivo", consecutivoCA));
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("caja.consecutivo"), "consecutivo")
				.add(Projections.property("caja.codigo"), "codigo")
				.add(Projections.property("caja.descripcion"), "descripcion")
		);
		criteria.setResultTransformer(Transformers.aliasToBean(DtoCaja.class));

		DtoCaja caja=(DtoCaja)criteria.uniqueResult();
		
		return caja;
	}
	
	
	/**
	 * Metodo que se encarga de consultar si la caja esta abierta o no para el usuario 
	 * @param usuario
	 * @param consecutivoCaja
	 * @param consecutivoCentroAtencion
	 * @return boolean con el estado de la caja para el usuario 
	 */
	@SuppressWarnings("unchecked")
	public Boolean cajaAbierta(String loginUsuario) {

		List<TurnoDeCaja> turnoDeCaja = sessionFactory
				.getCurrentSession() 
				.createCriteria(TurnoDeCaja.class,"turnoDeCaja")
				.createAlias("turnoDeCaja.cajas", "cajas")
				.add(Restrictions.eq("turnoDeCaja.estado",  ConstantesIntegridadDominio.acronimoEstadoAperturaDeCajaAbierto))
				.add(Restrictions.eq("cajas.tiposCaja.codigo",  ConstantesBD.codigoCajaRecaudo))
				.add(Restrictions.eq("turnoDeCaja.usuarios.login", loginUsuario)).list();

		if (turnoDeCaja.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * @param login
	 * @return si tiene cajas asociadas el usuario que genero la devolucion
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TurnoDeCaja> obtenerTurnoCajaAbiertoPorCajeroDevolucion(String login)
	{
		return (ArrayList<TurnoDeCaja>) sessionFactory.getCurrentSession()
			.createCriteria(TurnoDeCaja.class)
			.add(Expression.eq("estado", ConstantesIntegridadDominio.acronimoEstadoAperturaDeCajaAbierto))
			.add(Expression.eq("usuarios.login", login))
			.list();
	}
	
}
