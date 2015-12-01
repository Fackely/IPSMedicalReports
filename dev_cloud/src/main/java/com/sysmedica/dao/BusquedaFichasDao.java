/*
 * Creado en 15-nov-2005
 * 
 * Autor Santiago Salamanca
 * SysMédica
 */ 
package com.sysmedica.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author santiago
 *
 */
public interface BusquedaFichasDao {

    /**
     * Metodo para consultar las fichas de vigilancia epidemiologica
     * @param con
     * @param fechaInicial
     * @param fechaFinal
     * @param diagnostico
     * @param estado
     * @param verPendientes
     * @return
     */
    public Collection consultaFichas(Connection con, 
										String fechaInicial, 
										String fechaFinal, 
										String diagnostico, 
										String estado, 
										boolean verPendientes,
										String loginUsuario,
										String loginUsuarioBusqueda);
    
         
    /**
     * Metodo para notificar las fichas que han sido diligenciadas
     * @param con
     * @param codigoFicha
     * @param codigoEnfermedadNotificable
     * @param codigoUsuario
     * @param tipo
     * @param secuencia
     * @return
     */
    public int notificarFicha(Connection con,
								HashMap mapaFichas,
								String codigoUsuario,
								int tipo);
    
    
    
    /**
     * Metodo para consultar todas las fichas pendientes que tiene el usuario
     * @param con
     * @return
     */
    public Collection consultaFichasPendientes(Connection con,String loginUsuario);
    
    
    
    public Collection consultaBrotesPendientes(Connection con,String loginUsuario);
    
    
    public Collection consultaBrote(Connection con, 
									int evento, 
									String loginUsuarioBusqueda, 
									String loginUsuario, 
									String fechaInicial, 
									String fechaFinal, 
									int estado);
    
    
    public ResultSet consultaFichasPorPaciente(Connection con, 
									    		int codigoPaciente, 
									    		String diagnostico,
									    		String codigoDx);
}
