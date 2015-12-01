package com.servinte.axioma.vista.helper.tesoreria;

import javax.servlet.http.HttpSession;

import util.UtilidadFecha;

import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.TurnoDeCaja;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.vista.helper.autenticacion.AutenticacionHelper;


/**
 * 
 * @author axioma
 *
 */
public final class TurnoDeCajaHelper {
	
	
	private TurnoDeCajaHelper (){}
		
	
	
	/**
	 * M&eacute;todo que llena y retorna una instancia de TurnoDeCaja
	 * @param caja
	 * @param session
	 * @return {@link TurnoDeCaja}
	 */
	public static TurnoDeCaja iniciarTurno(Cajas caja, HttpSession session){
		TurnoDeCaja turno = new TurnoDeCaja();
		
		// El CentroAtencion solo se llena con la llave primaria (consecutivo)
		CentroAtencion centroAtencion = new CentroAtencion();
		centroAtencion.setConsecutivo(AutenticacionHelper.getCodigoCentroAtencion(session));
		turno.setCentroAtencion(centroAtencion);
		
		turno.setFechaApertura(UtilidadFecha.getFechaActualTipoBD());
		turno.setHoraApertura(UtilidadFecha.getHoraActual());
		turno.setCajas(caja);
		turno.setValorBase(caja.getValorBase());

		// El Usuario solo se llena con la llave primaria (login)
		Usuarios usuarioModifica = new Usuarios();
		usuarioModifica.setLogin(AutenticacionHelper.getLoginUsuarioBasico(session));
		turno.setUsuarios(usuarioModifica);

		//-forma.getDto().setCodigoPk(codigoPk);
		//-forma.getDto().setEstado(estado);
		
		return turno;
		
	}
	

}
