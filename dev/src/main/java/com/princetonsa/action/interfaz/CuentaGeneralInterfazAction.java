/*
 * Abril 26, 2010
 */
package com.princetonsa.action.interfaz;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.interfaz.CuentaGeneralInterfazForm;
import com.princetonsa.dto.interfaz.DtoCuentasInterfazEmpresasInsti;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.business.interfaz.CuentaGeneralInterfazMundo;
import com.servinte.axioma.orm.CuentaGeneralInterfaz;
import com.servinte.axioma.orm.CuentaInterfazEmpresInsti;
import com.servinte.axioma.orm.CuentasContables;
import com.servinte.axioma.orm.EmpresasInstitucion;
import com.servinte.axioma.orm.delegate.administracion.InstitucionDelegate;
import com.servinte.axioma.orm.delegate.interfaz.CuentaInterfazEmpresInstiDelegate;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;


/**
 * @author Cristhian Murillo
 *
 * Clase usada para controlar los procesos de la funcionalidad.
 * 
 */
public class CuentaGeneralInterfazAction extends Action 
{
	
	/**
	 * M&eacute;todo encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, 
	 * HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
								HttpServletResponse response )throws Exception
	{

		if(form instanceof CuentaGeneralInterfazForm)
		{
			CuentaGeneralInterfazForm forma = (CuentaGeneralInterfazForm)form;
			
			UsuarioBasico usuario 		= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			String estado = forma.getEstado(); 
			Log4JManager.info("Estado CuentaGeneralInterfazAction --> "+estado); 
			
			if(estado == null)
			{
				request.setAttribute("codigoDescripcionError","errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}
			else if( (estado.equals("empezar")) || (estado.equals("resumen")) )
			{
				return accionEmpezar(mapping, forma, usuario, request);
			}
			else if(estado.equals("guardar"))
			{
				return accionGuardar(mapping, forma, usuario, request);
			}
			else if(estado.equals("volver"))
			{
				return accionEmpezar(mapping,forma, usuario, request);
			}
		}
		return null;
	}
	
	

	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionEmpezar(ActionMapping mapping, CuentaGeneralInterfazForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{
		forma.reset();
		
		// Se valida si la institucuón es multiempresa
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt())))
		{
			forma.setIntitucionMultiEmpresa(true);
		}
		
		cargarDatosBD(usuario, forma);
		
		return mapping.findForward("principal");
	}
	
	
		
	
	/**
	 * Carga la informacion de las cuentas de sde la base de datos
	 * @param usuario
	 * @param forma
	 */
	private void cargarDatosBD(UsuarioBasico usuario, CuentaGeneralInterfazForm forma) 
	{
		CuentaGeneralInterfaz cuentaGeneralInterfaz = CuentaGeneralInterfazMundo.mostrarCuentaGeneralInterfazXInsti(usuario);
		forma.setDto(cuentaGeneralInterfaz);
		
		if( cuentaGeneralInterfaz == null)
		{
			cuentaGeneralInterfaz = new CuentaGeneralInterfaz();
			cuentaGeneralInterfaz.setCuentasContablesByCuentaTraslado(new CuentasContables());
			forma.setDto(cuentaGeneralInterfaz);
		}
		else 
		{
			cargarDatosMultiempresa(usuario, forma, cuentaGeneralInterfaz );
		}
		
		forma.setMostrarCuenta(true);
	}

	
	
	/**
	 * Carga los detalles de las cuentas de multiempresa
	 * @param usuario
	 * @param forma
	 * @param cuentaGeneralInterfaz
	 */
	private void cargarDatosMultiempresa(UsuarioBasico usuario, CuentaGeneralInterfazForm forma, CuentaGeneralInterfaz cuentaGeneralInterfaz )
	{
		UtilidadTransaccion.getTransaccion().begin();
		
		if(forma.isIntitucionMultiEmpresa())
		{
			ArrayList<CuentaInterfazEmpresInsti> listaCuentasContablesCPorCobrarTrasladoAbonos = new ArrayList<CuentaInterfazEmpresInsti>();
			listaCuentasContablesCPorCobrarTrasladoAbonos = new CuentaInterfazEmpresInstiDelegate().listarPorInstitucion(usuario.getCodigoInstitucionInt());
			
			for (CuentaInterfazEmpresInsti cuentasPorInsti : listaCuentasContablesCPorCobrarTrasladoAbonos)
			{
				DtoCuentasInterfazEmpresasInsti dtoCuentasInterfazEmpresasInsti;
				dtoCuentasInterfazEmpresasInsti = new DtoCuentasInterfazEmpresasInsti();
				
				dtoCuentasInterfazEmpresasInsti.setCodigoPk(cuentasPorInsti.getCodigoPk());
				dtoCuentasInterfazEmpresasInsti.setEmpresasInstitucion(cuentasPorInsti.getEmpresasInstitucion().getCodigo());
				dtoCuentasInterfazEmpresasInsti.setNombreEmpresasInstitucion(cuentasPorInsti.getEmpresasInstitucion().getRazonSocial());
				if(cuentasPorInsti.getCuentasContables() != null) {
					dtoCuentasInterfazEmpresasInsti.setCuentaContable(cuentasPorInsti.getCuentasContables().getCodigo());
				}
				
				forma.getListaCuentasInterfazEmpresasInsti().add(dtoCuentasInterfazEmpresasInsti);
			}
		}
		
		UtilidadTransaccion.getTransaccion().commit();
		
		
		/*
		 * Despues de cargar los datos de las cuentas por empresa institución se compara cuales empresas parametrizadas
		 *  no han sido cargadas en la lista y se agregan sin cuenta para que el usuario la seleccione.
		 */
		
		// Codigos de las Empresas-Instituciones ya cargadas
		ArrayList<Long> listaInstitucionesCargadas = new ArrayList<Long>();
		for (DtoCuentasInterfazEmpresasInsti cuentasEmpresasInstitucion : forma.getListaCuentasInterfazEmpresasInsti()) {
			listaInstitucionesCargadas.add(cuentasEmpresasInstitucion.getEmpresasInstitucion());
		}
		
		UtilidadTransaccion.getTransaccion().begin();
		// Todas las Empresas-Instituciones parametrizadas para la institución
		ArrayList<EmpresasInstitucion> listaEmpresasInstitucionParametrizadas = new ArrayList<EmpresasInstitucion>();
		listaEmpresasInstitucionParametrizadas = FacturacionServicioFabrica.crearEmpresasInstitucionServicio()
											.listarEmpresaInstitucionPorInstitucion(usuario.getCodigoInstitucionInt());
		
		// Se comparan los registros cargados con los parametrizados en el sistema para determinar 
		// si falta alguna Empresa-Institucion por cargar y asignarle cuenta
		for (EmpresasInstitucion empresaInstitucionParametrizada : listaEmpresasInstitucionParametrizadas) 
		{
			if(!listaInstitucionesCargadas.contains( empresaInstitucionParametrizada.getCodigo()) ){
				DtoCuentasInterfazEmpresasInsti dtoCuentasInterfazEmpresasInstiNoCargada;
				dtoCuentasInterfazEmpresasInstiNoCargada = new DtoCuentasInterfazEmpresasInsti();
				dtoCuentasInterfazEmpresasInstiNoCargada.setEmpresasInstitucion(empresaInstitucionParametrizada.getCodigo());
				dtoCuentasInterfazEmpresasInstiNoCargada.setNombreEmpresasInstitucion(empresaInstitucionParametrizada.getRazonSocial());
				
				forma.getListaCuentasInterfazEmpresasInsti().add(dtoCuentasInterfazEmpresasInstiNoCargada);
			}
		}
		
		UtilidadTransaccion.getTransaccion().commit();
	}


	
	/**
	 * Guarda o actualiza el registro
	 * @param mapping
	 * @param forma
	 * @param request 
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardar(ActionMapping mapping,	CuentaGeneralInterfazForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{
		if(forma.getDto().getInstituciones() == null){
			forma.getDto().setInstituciones(new InstitucionDelegate().findById(usuario.getCodigoInstitucionInt()));
		}
		
		CuentaGeneralInterfazMundo.actualizarRegistros(forma.getDto());
		CuentaGeneralInterfazMundo.actualizarRegistrosMultiempresa(forma, usuario.getCodigoInstitucionInt());
		
		forma.reset();
		forma.setEstado("resumen");
		forma.setDto(CuentaGeneralInterfazMundo.mostrarCuentaGeneralInterfazXInsti(usuario));

		cargarDatosBD(usuario, forma);
		return mapping.findForward("principal");
	}
	
	
}	
	
	