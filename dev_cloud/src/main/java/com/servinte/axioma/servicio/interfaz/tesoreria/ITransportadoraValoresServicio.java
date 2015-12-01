package com.servinte.axioma.servicio.interfaz.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.TransportadoraValores;


/**
 * Servicio que le delega al negocio las operaciones relacionados con 
 * las Transportadora de valores registradas en el sistema 
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see com.servinte.axioma.servicio.impl.tesoreria.TransportadoraValoresServicio
 */

public interface ITransportadoraValoresServicio {


	
	/**
	 * GUARDA LA TRANPORTADORA DE VALORES
	 * @return 
	 */
	public void guardar(TransportadoraValores dtoTransportadora, UsuarioBasico usuario, ArrayList<String> listaCodigo, ArrayList<CentroAtencion> listaCentrosAtencion);
	
	/**
	 * FILTRAR LISTA CENTRO DE ATENCION
	 * @param listaCentrosAtencion
	 */
	public ArrayList<CentroAtencion> filtrarListaCentros(ArrayList<CentroAtencion> listaCentrosAtencion,ArrayList<String> listaCodigoCentros );

	
	/**
	 * LISTA LAS TRANSPORTADORAS DE VALORES
	 * @param dtoTransportadora
	 */
	public  List<TransportadoraValores> consultar(TransportadoraValores dtoTransportadora, int institucion);
	
	/**
	 * MODIFICAR TRANSPORTADORA DE VALORES 
	 * @param dtoTransportadoraValores
	 * @param usuario
	 */
	public void modificar(TransportadoraValores dtoTransportadoraValores, 
								UsuarioBasico usuario , ArrayList<Integer> listaCodigoEliminar , 
								ArrayList<String> listaCodigoGuardar, ArrayList<CentroAtencion> listaCentroAtencion);
	
	/**
	 * ELIMINAR TRANSPORTADORA
	 * @param dtoTransportadora
	 */
	public void eliminar(TransportadoraValores dtoTransportadora);
	
	/**
	 * CARGA CODIGOS CENTROS DE ATENCION
	 * @param dto
	 * @return
	 */
	public  String[] cargarCodigoCentro(TransportadoraValores dto);
	

	/**
	 * CARGA CODIGOS CENTROS DE ATENCION
	 * @param dto
	 * @return
	 */
	public ArrayList<Integer> cargarCodigosPkCentroTransportadora(TransportadoraValores dto);
	
	
	/**
	 * ELIMINAR LOS CENTROS DE ATENCION 
	 * RECIBE UNA LISTA DE CODIGOS CENTRO ATENCION
	 * @param listaCodigoCentroAtencion
	 */
	public void eliminiarCentroAtencionTransportadora(ArrayList<Integer> listaCodigoCentroAtencion);
	
	
	/**
	 * RECIBE UN OBJTEO TRANPORTADORA Y RETORNA UN STRING BUILDER CON EL FORMATO DE ARCHIVO PLANO
	 * 
	 * @param dto
	 * @param titulo
	 * @param listaCentros
	 * @param listaCodigoCentro
	 * @return
	 */
	public StringBuilder armarArchivo(TransportadoraValores dto, String titulo, ArrayList<CentroAtencion> listaCentros, ArrayList<String> listaCodigoCentro);
	
	/**
	 *	GUARDA EL LOG EN ARCHIVO PLANO 
	 * @param estructuraArchivo
	 */
	public void guardarLog(StringBuilder estructuraArchivo, UsuarioBasico usuario, int tipoRegistro);
		
	/**
	 * Retorna una transportadora de valores por centro de atenci&oacute;n
	 * @author Diana Carolina G
	 * @param dtoTransportadora
	 * @param institucion
	 * @param consecutivoCentroAtencion
	 * @return
	 */
	public List<TransportadoraValores> listarTodos(TransportadoraValores dtoTransportadora, int institucion , int consecutivoCentroAtencion );
	
	
}
