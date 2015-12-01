package com.servinte.axioma.orm.delegate.manejoPaciente;

import com.servinte.axioma.orm.AutoEntsubOrdenambula;
import com.servinte.axioma.orm.AutoEntsubOrdenambulaHome;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.orm.OrdenesAmbulatorias;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene lógica del negocio sobre el modelo 
 */
public class AutoEntsubOrdenambulaDelegate extends AutoEntsubOrdenambulaHome
{
	public static void main(String[] args) 
	{
		AutorizacionesEntidadesSub autorizacionesEntidadesSub; autorizacionesEntidadesSub = new AutorizacionesEntidadesSub();
		autorizacionesEntidadesSub.setConsecutivo(1);
		
		OrdenesAmbulatorias ordenesAmbulatorias; ordenesAmbulatorias = new OrdenesAmbulatorias();
		ordenesAmbulatorias.setCodigo(1);
		
		AutoEntsubOrdenambula autoEntsubOrdenambulaGuardar; autoEntsubOrdenambulaGuardar = new AutoEntsubOrdenambula();
		autoEntsubOrdenambulaGuardar.setAutorizacionesEntidadesSub(autorizacionesEntidadesSub);
		autoEntsubOrdenambulaGuardar.setOrdenesAmbulatorias(ordenesAmbulatorias);
		
		AutoEntsubOrdenambulaDelegate d = new AutoEntsubOrdenambulaDelegate();
		d.persist(autoEntsubOrdenambulaGuardar);
	}
}
