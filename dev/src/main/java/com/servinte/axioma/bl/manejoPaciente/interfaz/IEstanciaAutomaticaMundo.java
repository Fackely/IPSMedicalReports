package com.servinte.axioma.bl.manejoPaciente.interfaz;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.EstanciaAutomatica;

/**
 * Interface que presenta los servicios para al logica de negocio de la estancia automatica.
 * 
 * @author DiaRuiPe
 * @version 1.0
 * @created 03-jul-2012 03:34:35 p.m.
 */
public interface IEstanciaAutomaticaMundo {
	
	/**
	 * Método que permite generar la autorización de estancia
	 * @param con
	 * @param estanciaAutomatica
	 * @param pos
	 * @param mapaCuentas
	 */
	public int generarEstanciaAutomatica(Connection con, EstanciaAutomatica estanciaAutomatica, int pos, HashMap mapaCuentas,  UsuarioBasico usuario, int servicio) throws Exception;


}
