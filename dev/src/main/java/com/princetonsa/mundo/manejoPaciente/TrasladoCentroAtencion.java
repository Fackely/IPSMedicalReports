/*
 * Sep 28,06
 */
package com.princetonsa.mundo.manejoPaciente;


import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.TrasladoCentroAtencionDao;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Sebastián Gómez 
 *
 *Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * Traslado Centro de Atención
 */
public class TrasladoCentroAtencion 
{
	/**
	 * DAO para el manejo de TrasladoCentroAtencionDao
	 */
	TrasladoCentroAtencionDao trasladoDao = null;
	
	//**********ATRIBUTOS*****************************************
	
	/**
	 * parametros enviados a los métodos del DAO
	 */
	HashMap campos = new HashMap();
	//************************************************************
	
	//**********INICIALIZADORES & CONSTRUCTORES***********************
	/**
	 * Constructor
	 */
	public TrasladoCentroAtencion() {
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.campos = new HashMap();
	}
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (trasladoDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			trasladoDao = myFactory.getTrasladoCentroAtencionDao();
		}	
	}
	//****************************************************************************
	
	//********METODOS*************************************************************
	/**
	 * Método implementado para realizar las validaciones de ingreso
	 * a la funcionalidad Traslado Centro Atencion
	 * @param con
	 * @param campos
	 * @param paciente
	 * @param usuario
	 * @return
	 */
	public ActionErrors validaciones(Connection con,PersonaBasica paciente, UsuarioBasico usuario)
	{
		setCampos("pacienteActivo",paciente);
		setCampos("usuarioBasico",usuario);
		
		ActionErrors errores = new ActionErrors();
		Vector mensajes = trasladoDao.validaciones(con,campos);
		String aux = "";
		String[] vector;
	
		//se revisa los mensajes********************
		for(int i=0;i<mensajes.size();i++)
		{
			aux = mensajes.get(i).toString();
			
			vector = aux.split(ConstantesBD.separadorSplit);
			
			if(vector.length>1)
				errores.add("adicionar error con atributos",new ActionMessage(vector[0],vector[1]));
			else
				errores.add("adicionar error sin atributos",new ActionMessage(aux));
		}
		//****************************************************
		
		return errores;
	}
	
	/**
	 * Método que desasigna la cama de una admision de urgencias
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public int desasignarCamaUrgencias(Connection con,String idCuenta)
	{
		return trasladoDao.desasignarCamaUrgencias(con,idCuenta);
	}
	
	
	/**
	 * Método implementado para realizar el traslado por centro de atencion
	 * @param con
	 * @return
	 */
	public int realizarTraslado(Connection con)
	{
		return trasladoDao.realizarTraslado(con,campos);
	}
	//*******************************************************************************
	//*************GETTERS & SETTERS**************************************************
	/**
	 * @return Returns the campos.
	 */
	public HashMap getCampos() {
		return campos;
	}
	/**
	 * @param campos The campos to set.
	 */
	public void setCampos(HashMap campos) {
		this.campos = campos;
	}
	/**
	 * @return Retorna un elemento al mapa campos.
	 */
	public Object getCampos(String key) {
		return campos.get(key);
	}
	/**
	 * @param Asigna un elemento al mapa campos.
	 */
	public void setCampos(String key,Object obj) {
		this.campos.put(key,obj);
	}
}
