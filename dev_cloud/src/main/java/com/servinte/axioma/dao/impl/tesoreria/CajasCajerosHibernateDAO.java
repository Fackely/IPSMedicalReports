package com.servinte.axioma.dao.impl.tesoreria;

import java.util.ArrayList;
import java.util.List;

import util.Utilidades;

import com.princetonsa.dto.tesoreria.DtoCajaCajeros;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dao.interfaz.tesoreria.ICajasCajerosDAO;
import com.servinte.axioma.orm.CajasCajeros;
import com.servinte.axioma.orm.delegate.tesoreria.CajasCajerosDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link ICajasCajerosDAO}.
 * 
 * @author Jorge Armando Agudelo Quintero - Luis Alejandro Echandia
 * @see CajasCajerosDelegate.
 */


public class CajasCajerosHibernateDAO implements ICajasCajerosDAO{

	private CajasCajerosDelegate cajasCajerosDaoImp;

	
	
	public CajasCajerosHibernateDAO() {
		cajasCajerosDaoImp  = new CajasCajerosDelegate();
	}

	@Override
	public List<DtoUsuarioPersona> obtenerListadoCajeros() {
		
		return cajasCajerosDaoImp.obtenerListadoCajeros();
	}
	
	

	
	@Override
	public boolean validarUsuarioEsCajero(UsuarioBasico usuario) 
	{
		boolean isCajero = true;
		ArrayList<CajasCajeros> listaRerotno = new ArrayList<CajasCajeros>();
		listaRerotno = cajasCajerosDaoImp.obtenerCajasCajero(usuario);
		
		boolean vacia = Utilidades.isEmpty(listaRerotno); 
		
		if(vacia){
			isCajero= false;
		}
		
		return isCajero;
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de consultar los usuarios cajeros pertenecientes
	 * a un centro de atenci&oacute;n espec&iacute;fico
	 * 
	 * @param consecutivoCentroAtencion
	 * @return ArrayList<DtoUsuarioPersona> 
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public ArrayList<DtoUsuarioPersona> obtenerCajerosCentrosAtencion(int consecutivoCentroAtencion){
		return cajasCajerosDaoImp.obtenerCajerosCentrosAtencion(consecutivoCentroAtencion);
		
	}	
	
	@Override
	public ArrayList<DtoUsuarioPersona> obtenerCajerosPorInstitucion(int codigoInstitucion){
		return cajasCajerosDaoImp.obtenerCajerosPorInstitucion(codigoInstitucion);
	}
	

	/**
	 * Este metodo permite consultar para todos los usuarios las cajas que tiene asociadas y que se encuentran activas
	 * @param usuario
	 * @return
	 * @author Diana Ruiz
	 */
	
	
	@Override
	public ArrayList<CajasCajeros> obtenerTodasCajasCajero(int codigo_caja) {
		return cajasCajerosDaoImp.obtenerTodasCajasCajero(codigo_caja);		
	}
	
	/**
	 * Este metodo permite realizar una merge a la clase
	 * @param instance
	 * @return
	 * @author Diana Ruiz
	 */

	@Override
	public CajasCajeros merge(CajasCajeros instance) {
		return cajasCajerosDaoImp.merge(instance);
	}

	/**
	 * Este metodo permite consultar por usuario las cajas que este tiene activas
	 * @param usuario
	 * @return
	 * @author Diana Ruiz
	 * @since 11/10/2011
	 */
	
	@Override
	public ArrayList<CajasCajeros> obtenerCajasCajero(UsuarioBasico usuario) {		
		return cajasCajerosDaoImp.obtenerCajasCajero(usuario);
	}


	/**
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.ICajasCajerosDAO#consultarCajaCajerosParametrizados(java.lang.String, java.lang.String)
	 */
	@Override
	public List<DtoCajaCajeros> consultarCajaCajerosParametrizados(
			String codigoInstitucion, String centroAtencion) {
		return cajasCajerosDaoImp.consultarCajaCajerosParametrizados(codigoInstitucion, centroAtencion);
	}

	@Override
	public List<DtoCajaCajeros> consultarDatosCajaParametrizados(Integer codigoCajero,
			String codigoInstitucion, String centroAtencion) {
		return cajasCajerosDaoImp.consultarDatosCajaParametrizados(codigoCajero,codigoInstitucion, centroAtencion);
	}
	
	/**
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.ICajasCajerosDAO#obtenerCajeroXCodigo(java.lang.Integer)
	 */
	public DtoUsuarioPersona obtenerCajeroXCodigo(Integer codigoCajero){
		return cajasCajerosDaoImp.obtenerCajeroXCodigo(codigoCajero);
	}
	
	
}
