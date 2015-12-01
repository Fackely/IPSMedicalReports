/*
 * @author artotor
 */
package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dto.manejoPaciente.DtoFiltroBusquedaAvanzadaEgresoPorFacturar;


/**
 * 
 * @author artotor
 *
 */
public interface PacientesConEgresoPorFacturarDao 
{

	public abstract HashMap cargarPacientesConEgresoPorFacturar(Connection con, UsuarioBasico usuario);

	public abstract HashMap cargarPacientesConEgresoPorFacturarAvanzado(
			Connection con, UsuarioBasico usuario,
			DtoFiltroBusquedaAvanzadaEgresoPorFacturar dtoFiltro);

}
