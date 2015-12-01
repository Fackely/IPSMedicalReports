package com.servinte.axioma.servicio.impl.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IEmpresasInstitucionMundo;
import com.servinte.axioma.orm.EmpresasInstitucion;
import com.servinte.axioma.servicio.interfaz.facturacion.IEmpresasInstitucionServicio;

/**
 * Implementaci&oacute;n de la interface {@link IEmpresasInstitucionServicio}.
 *
 * @author Yennifer Guerrero
 * @since  27/08/2010
 *
 */
public class EmpresasInstitucionServicio implements IEmpresasInstitucionServicio{
	
	private IEmpresasInstitucionMundo mundo;
	
	/**
	 * M&eacute;todo constructor de la clase EmpresasInstitucionServicio
	 * 
	 * @author Yennifer Guerrero
	 */
	public EmpresasInstitucionServicio() {
		
		mundo = FacturacionFabricaMundo.crearEmpresasInstitucionMundo();
	}
	
	
	@Override
	public ArrayList<EmpresasInstitucion> listarEmpresaInstitucion (){
		return mundo.listarEmpresaInstitucion();
	}


	@Override
	public ArrayList<EmpresasInstitucion> listarEmpresaInstitucionPorInstitucion(int institucion) {
		return mundo.listarEmpresaInstitucionPorInstitucion(institucion);
	}

}
