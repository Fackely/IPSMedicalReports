/*
 * Abril 26, 2010
 */
package com.servinte.axioma.business.interfaz;

import org.axioma.util.log.Log4JManager;

import com.princetonsa.actionform.interfaz.CuentaGeneralInterfazForm;
import com.princetonsa.dto.interfaz.DtoCuentasInterfazEmpresasInsti;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.CuentaGeneralInterfaz;
import com.servinte.axioma.orm.CuentaInterfazEmpresInsti;
import com.servinte.axioma.orm.CuentaInterfazEmpresInstiHome;
import com.servinte.axioma.orm.CuentasContables;
import com.servinte.axioma.orm.delegate.administracion.InstitucionDelegate;
import com.servinte.axioma.orm.delegate.facturacion.EmpresasInstitucionDelegate;
import com.servinte.axioma.orm.delegate.interfaz.CuentaGeneralInterfazDelegate;
import com.servinte.axioma.orm.delegate.interfaz.CuentasContablesDelegate;
import com.servinte.axioma.persistencia.UtilidadTransaccion;


/**
 * @author Cristhian Murillo
 * Define la l&oacute;gica del negocio
 */
public class CuentaGeneralInterfazMundo {

	
	/**
	 * Carga el c&oacute;digo de cada una de las cuentas del registro para ser mostrado 
	 * en la forma, validando  que algunas de esas cuentas pueden ser nulas
	 * @param usuario
	 * @return {@link CuentaGeneralInterfaz} Objeto validado con cada una de sus cuentas 
	 */
	public static CuentaGeneralInterfaz mostrarCuentaGeneralInterfazXInsti(UsuarioBasico usuario){
		
		//HibernateUtil.beginTransaction();
		UtilidadTransaccion.getTransaccion().begin();
		
		CuentaGeneralInterfaz cuentaGralInterfazXInsti = new CuentaGeneralInterfazDelegate()
			.listarUnicoPorInstitucion(Integer.parseInt(usuario.getCodigoInstitucion()));
		
		if(cuentaGralInterfazXInsti==null)
		{
			cuentaGralInterfazXInsti=new CuentaGeneralInterfaz();
		}
		
		try{
			
			HibernateUtil.cerrarSession();	// Traslado abono paciente
			UtilidadTransaccion.getTransaccion().begin();
			if(cuentaGralInterfazXInsti.getCuentasContablesByCuentaTraslado() != null){
				cuentaGralInterfazXInsti.setCuentasContablesByCuentaTraslado(
					new CuentasContablesDelegate().findById(cuentaGralInterfazXInsti.getCuentasContablesByCuentaTraslado().getCodigo()));
				}else{ cuentaGralInterfazXInsti.setCuentasContablesByCuentaTraslado(new CuentasContables()); }
				
			UtilidadTransaccion.getTransaccion().commit();
			HibernateUtil.cerrarSession();	// Abono paciente
			UtilidadTransaccion.getTransaccion().begin();
			if(cuentaGralInterfazXInsti.getCuentasContablesByCuentaAbonoPaciente() != null){
				cuentaGralInterfazXInsti.setCuentasContablesByCuentaAbonoPaciente(
					new CuentasContablesDelegate().findById(cuentaGralInterfazXInsti.getCuentasContablesByCuentaAbonoPaciente().getCodigo()));
				}else{ cuentaGralInterfazXInsti.setCuentasContablesByCuentaAbonoPaciente(new CuentasContables()); }
			
			UtilidadTransaccion.getTransaccion().commit();
			HibernateUtil.cerrarSession();	// Sobrante Caja
			UtilidadTransaccion.getTransaccion().begin();
			if(cuentaGralInterfazXInsti.getCuentasContablesByCuentaSobrantes() != null){
				cuentaGralInterfazXInsti.setCuentasContablesByCuentaSobrantes(
					new CuentasContablesDelegate().findById(cuentaGralInterfazXInsti.getCuentasContablesByCuentaSobrantes().getCodigo()));
				}else{ cuentaGralInterfazXInsti.setCuentasContablesByCuentaSobrantes(new CuentasContables()); }
			
			UtilidadTransaccion.getTransaccion().commit();
			HibernateUtil.cerrarSession();	// Faltante caja
			UtilidadTransaccion.getTransaccion().begin();
			if(cuentaGralInterfazXInsti.getCuentasContablesByCuentaFaltantes() != null){
				cuentaGralInterfazXInsti.setCuentasContablesByCuentaFaltantes(
					new CuentasContablesDelegate().findById(cuentaGralInterfazXInsti.getCuentasContablesByCuentaFaltantes().getCodigo()));
				}else{ cuentaGralInterfazXInsti.setCuentasContablesByCuentaFaltantes(new CuentasContables()); }
				
			UtilidadTransaccion.getTransaccion().commit();
			HibernateUtil.cerrarSession();	// Sancion devolucion
			UtilidadTransaccion.getTransaccion().begin();
			if(cuentaGralInterfazXInsti.getCuentasContablesByCuentaDescDevolAbonosPacientes() != null){
				cuentaGralInterfazXInsti.setCuentasContablesByCuentaDescDevolAbonosPacientes(
					new CuentasContablesDelegate().findById(cuentaGralInterfazXInsti.getCuentasContablesByCuentaDescDevolAbonosPacientes().getCodigo()));
				}else{ cuentaGralInterfazXInsti.setCuentasContablesByCuentaDescDevolAbonosPacientes(new CuentasContables()); }
			
			UtilidadTransaccion.getTransaccion().commit();
			HibernateUtil.cerrarSession();	// Devolucion abono paciente
			UtilidadTransaccion.getTransaccion().begin();
			if(cuentaGralInterfazXInsti.getCuentasContablesByCuentaDevolAbonosPacientes() != null){
				cuentaGralInterfazXInsti.setCuentasContablesByCuentaDevolAbonosPacientes(
					new CuentasContablesDelegate().findById(cuentaGralInterfazXInsti.getCuentasContablesByCuentaDevolAbonosPacientes().getCodigo()));
				}else{ cuentaGralInterfazXInsti.setCuentasContablesByCuentaDevolAbonosPacientes(new CuentasContables()); }
			
			UtilidadTransaccion.getTransaccion().commit();
			HibernateUtil.cerrarSession();	// Por cobrar traslado abono paciente
			UtilidadTransaccion.getTransaccion().begin();
			if(cuentaGralInterfazXInsti.getCuentasContablesByCuentaPorCobrarTraslados() != null){
				cuentaGralInterfazXInsti.setCuentasContablesByCuentaPorCobrarTraslados(
					new CuentasContablesDelegate().findById(cuentaGralInterfazXInsti.getCuentasContablesByCuentaPorCobrarTraslados().getCodigo()));
				}else{ cuentaGralInterfazXInsti.setCuentasContablesByCuentaPorCobrarTraslados(new CuentasContables()); }
			
			UtilidadTransaccion.getTransaccion().commit();
			HibernateUtil.cerrarSession();	// Anticipo convenio
			UtilidadTransaccion.getTransaccion().begin();
			if(cuentaGralInterfazXInsti.getCuentasContablesByCuentaAnticipoConvenio() != null){
				cuentaGralInterfazXInsti.setCuentasContablesByCuentaAnticipoConvenio(
					new CuentasContablesDelegate().findById(cuentaGralInterfazXInsti.getCuentasContablesByCuentaAnticipoConvenio().getCodigo()));
				}else{ cuentaGralInterfazXInsti.setCuentasContablesByCuentaAnticipoConvenio(new CuentasContables()); }
			
			UtilidadTransaccion.getTransaccion().commit();
			HibernateUtil.cerrarSession();	// Descuento Bonos
			UtilidadTransaccion.getTransaccion().begin();
			if(cuentaGralInterfazXInsti.getCuentasContablesByCuentaDescuentoBonos() != null){
				cuentaGralInterfazXInsti.setCuentasContablesByCuentaDescuentoBonos(
					new CuentasContablesDelegate().findById(cuentaGralInterfazXInsti.getCuentasContablesByCuentaDescuentoBonos().getCodigo()));
				}else{ cuentaGralInterfazXInsti.setCuentasContablesByCuentaDescuentoBonos(new CuentasContables()); }
			
			UtilidadTransaccion.getTransaccion().commit();
			HibernateUtil.cerrarSession();	// Descuento promocion
			UtilidadTransaccion.getTransaccion().begin();
			if(cuentaGralInterfazXInsti.getCuentasContablesByCuentaDescuentoPromocion() != null){
				cuentaGralInterfazXInsti.setCuentasContablesByCuentaDescuentoPromocion(
					new CuentasContablesDelegate().findById(cuentaGralInterfazXInsti.getCuentasContablesByCuentaDescuentoPromocion().getCodigo()));
				}else{ cuentaGralInterfazXInsti.setCuentasContablesByCuentaDescuentoPromocion(new CuentasContables()); }
			
			UtilidadTransaccion.getTransaccion().commit();
			HibernateUtil.cerrarSession();	// Descuentos odontologicos
			UtilidadTransaccion.getTransaccion().begin();
			if(cuentaGralInterfazXInsti.getCuentasContablesByCuentaDescuentosOdontologicos() != null){
				cuentaGralInterfazXInsti.setCuentasContablesByCuentaDescuentosOdontologicos(
					new CuentasContablesDelegate().findById(cuentaGralInterfazXInsti.getCuentasContablesByCuentaDescuentosOdontologicos().getCodigo()));
				}else{ cuentaGralInterfazXInsti.setCuentasContablesByCuentaDescuentosOdontologicos(new CuentasContables()); }
			
			UtilidadTransaccion.getTransaccion().commit();
			HibernateUtil.cerrarSession();	// Por pagar honorarios
			UtilidadTransaccion.getTransaccion().begin();
			if(cuentaGralInterfazXInsti.getCuentasContablesByCuentaXPagarHonorarios() != null){
				cuentaGralInterfazXInsti.setCuentasContablesByCuentaXPagarHonorarios(
					new CuentasContablesDelegate().findById(cuentaGralInterfazXInsti.getCuentasContablesByCuentaXPagarHonorarios().getCodigo()));
				}else{ cuentaGralInterfazXInsti.setCuentasContablesByCuentaXPagarHonorarios(new CuentasContables()); }
			
			UtilidadTransaccion.getTransaccion().commit();
			HibernateUtil.cerrarSession();	// Valor Cargo paciente
			UtilidadTransaccion.getTransaccion().begin();
			if(cuentaGralInterfazXInsti.getCuentasContablesByCuentaValCargoPaci() != null){
				cuentaGralInterfazXInsti.setCuentasContablesByCuentaValCargoPaci(
					new CuentasContablesDelegate().findById(cuentaGralInterfazXInsti.getCuentasContablesByCuentaValCargoPaci().getCodigo()));
				}else{ cuentaGralInterfazXInsti.setCuentasContablesByCuentaValCargoPaci(new CuentasContables()); }
			
			UtilidadTransaccion.getTransaccion().commit();
			HibernateUtil.cerrarSession();	// Valor Cargo convenio
			UtilidadTransaccion.getTransaccion().begin();
			if(cuentaGralInterfazXInsti.getCuentasContablesByCuentaValCargoConv() != null){
				cuentaGralInterfazXInsti.setCuentasContablesByCuentaValCargoConv(
					new CuentasContablesDelegate().findById(cuentaGralInterfazXInsti.getCuentasContablesByCuentaValCargoConv().getCodigo()));
				}else{ cuentaGralInterfazXInsti.setCuentasContablesByCuentaValCargoConv(new CuentasContables()); }
			
			UtilidadTransaccion.getTransaccion().commit();
			HibernateUtil.cerrarSession();	// Descuento facturas varias
			UtilidadTransaccion.getTransaccion().begin();
			if(cuentaGralInterfazXInsti.getCuentasContablesByCuentaDescuentoFacturas() != null){
				cuentaGralInterfazXInsti.setCuentasContablesByCuentaDescuentoFacturas(
					new CuentasContablesDelegate().findById(cuentaGralInterfazXInsti.getCuentasContablesByCuentaDescuentoFacturas().getCodigo()));
				}else{ cuentaGralInterfazXInsti.setCuentasContablesByCuentaDescuentoFacturas(new CuentasContables()); }
			
			}catch (NullPointerException e) {
				Log4JManager.error("Error buscando cuenta general interfaz", e);
				
			}
		//HibernateUtil.endTransaction();
		UtilidadTransaccion.getTransaccion().commit();
		
		return cuentaGralInterfazXInsti;
		
	}
	


	/**
	 * Actualiza los datos modificados de la instancia
	 * Las cuentas no seleccuinadas pueden tener el valor de cero o null:
	 * @param instancia
	 */
	public static void actualizarRegistros(CuentaGeneralInterfaz instancia)
	{
		instancia = cambiarCerosPorNull(instancia); 
		//imprimirObj(instancia);	// Metodo para verificar los datos del dto
		
		UtilidadTransaccion.getTransaccion().begin();
		new CuentaGeneralInterfazDelegate().attachDirty(instancia);
		UtilidadTransaccion.getTransaccion().commit();
		
	}
	
	
	
	/**
	 * Actualiza los datos modificados de la instancia
	 * Las cuentas no seleccuinadas pueden tener el valor de cero o null:
	 * @param forma
	 */
	public static void actualizarRegistrosMultiempresa(CuentaGeneralInterfazForm forma, int institucion)
	{
		UtilidadTransaccion.getTransaccion().begin();
		
		if(forma.isIntitucionMultiEmpresa()){
			for (DtoCuentasInterfazEmpresasInsti dtoCuentasInterfazEmpresasInsti : forma.getListaCuentasInterfazEmpresasInsti()) 
			{
				CuentaInterfazEmpresInsti cuentaInterfazEmpresInsti;
				cuentaInterfazEmpresInsti = new CuentaInterfazEmpresInsti();
				
				if(dtoCuentasInterfazEmpresasInsti.getCodigoPk() > 0){
					cuentaInterfazEmpresInsti.setCodigoPk(dtoCuentasInterfazEmpresasInsti.getCodigoPk());
				}
				
				if(dtoCuentasInterfazEmpresasInsti.getCuentaContable() == 0){
					cuentaInterfazEmpresInsti.setCuentasContables(null); 
				}
				else{
					cuentaInterfazEmpresInsti.setCuentasContables(new CuentasContablesDelegate().findById(dtoCuentasInterfazEmpresasInsti.getCuentaContable()));
				}
				
				cuentaInterfazEmpresInsti.setInstituciones(new InstitucionDelegate().findById(institucion));
				cuentaInterfazEmpresInsti.setEmpresasInstitucion(new EmpresasInstitucionDelegate().findById(dtoCuentasInterfazEmpresasInsti.getEmpresasInstitucion()));
				
				new CuentaInterfazEmpresInstiHome().attachDirty(cuentaInterfazEmpresInsti);
			}
		}
		
		UtilidadTransaccion.getTransaccion().commit();
		
	}
	
	
	
	/**
	 * Coloca null si el c&oacute;digo de alguna cuenta es cero 
	 * @param instancia
	 */
	public static CuentaGeneralInterfaz cambiarCerosPorNull(CuentaGeneralInterfaz instancia)
	{
		try {
			if(instancia.getCuentasContablesByCuentaTraslado().getCodigo() == 0){
				instancia.setCuentasContablesByCuentaTraslado(null); 
			}
	
			if(instancia.getCuentasContablesByCuentaAbonoPaciente().getCodigo() == 0){
				instancia.setCuentasContablesByCuentaAbonoPaciente(null); 
			}
			
			if(instancia.getCuentasContablesByCuentaSobrantes().getCodigo() == 0){
				instancia.setCuentasContablesByCuentaSobrantes(null); 
			}
			
			if(instancia.getCuentasContablesByCuentaFaltantes().getCodigo() == 0){
				instancia.setCuentasContablesByCuentaFaltantes(null); 
			}
			
			if(instancia.getCuentasContablesByCuentaDescDevolAbonosPacientes().getCodigo() == 0){
				instancia.setCuentasContablesByCuentaDescDevolAbonosPacientes(null); 
			}
			
			if(instancia.getCuentasContablesByCuentaDevolAbonosPacientes().getCodigo() == 0){
				instancia.setCuentasContablesByCuentaDevolAbonosPacientes(null); 
			}
			
			if(instancia.getCuentasContablesByCuentaPorCobrarTraslados().getCodigo() == 0){
				instancia.setCuentasContablesByCuentaPorCobrarTraslados(null); 
			}
			
			if(instancia.getCuentasContablesByCuentaAnticipoConvenio().getCodigo() == 0){
				instancia.setCuentasContablesByCuentaAnticipoConvenio(null); 
			}
			
			if(instancia.getCuentasContablesByCuentaDescuentoBonos().getCodigo() == 0){
				instancia.setCuentasContablesByCuentaDescuentoBonos(null); 
			}
			
			if(instancia.getCuentasContablesByCuentaDescuentoPromocion().getCodigo() == 0){
				instancia.setCuentasContablesByCuentaDescuentoPromocion(null); 
			}
			
			if(instancia.getCuentasContablesByCuentaDescuentosOdontologicos().getCodigo() == 0){
				instancia.setCuentasContablesByCuentaDescuentosOdontologicos(null); 
			}
			
			if(instancia.getCuentasContablesByCuentaXPagarHonorarios().getCodigo() == 0){
				instancia.setCuentasContablesByCuentaXPagarHonorarios(null); 
			}
			
			if(instancia.getCuentasContablesByCuentaValCargoPaci().getCodigo() == 0){
				instancia.setCuentasContablesByCuentaValCargoPaci(null); 
			}
			
			if(instancia.getCuentasContablesByCuentaValCargoConv().getCodigo() == 0){
				instancia.setCuentasContablesByCuentaValCargoConv(null); 
			}
			
			if(instancia.getCuentasContablesByCuentaDescuentoFacturas().getCodigo() == 0){
				instancia.setCuentasContablesByCuentaDescuentoFacturas(null); 
			}
		}catch (Exception e) { 	}
		
		return instancia;
	}
	
	
	
	/**
	 * Para imprimir y monitorear el objeto que se va a guardar
	 * @param instancia
	 */
	@SuppressWarnings("unused")
	private static void imprimirObj(CuentaGeneralInterfaz instancia)
	{
		//ESte codigo documentado sirve para imprimir todo el objeto a guardar
		try{
			Log4JManager.info(
					"################################## Datos cuenta General Interfaz ###########################\n"
					+"\n----: "+
					instancia.getCuentasContablesByCuentaTraslado().getCodigo()
					+"\n----: "+
					instancia.getCuentasContablesByCuentaAbonoPaciente().getCodigo()
					+"\n----: "+
					instancia.getCuentasContablesByCuentaSobrantes().getCodigo()
					+"\n----: "+
					instancia.getCuentasContablesByCuentaFaltantes().getCodigo()
					+"\n----: "+
					instancia.getCuentasContablesByCuentaDescDevolAbonosPacientes().getCodigo()
					+"\n----: "+
					instancia.getCuentasContablesByCuentaDevolAbonosPacientes().getCodigo()
					+"\n----: "+
					instancia.getCuentasContablesByCuentaPorCobrarTraslados().getCodigo()
					+"\n----: "+
					instancia.getCuentasContablesByCuentaAnticipoConvenio().getCodigo()
					+"\n----: "+
					instancia.getCuentasContablesByCuentaDescuentoBonos().getCodigo()
					+"\n----: "+	
					instancia.getCuentasContablesByCuentaDescuentoPromocion().getCodigo()
					+"\n----: "+
					instancia.getCuentasContablesByCuentaDescuentosOdontologicos().getCodigo()
					+"\n----: "+
					instancia.getCuentasContablesByCuentaXPagarHonorarios().getCodigo()
					+"\n----: "+
					instancia.getCuentasContablesByCuentaValCargoPaci().getCodigo()
					+"\n----: "+
					instancia.getCuentasContablesByCuentaValCargoConv().getCodigo()
					+"\n----: "+
					instancia.getCuentasContablesByCuentaDescuentoFacturas().getCodigo()
					
					+"\n#############################################################################################################");
		}catch (Exception e) { 	
			Log4JManager.error("ERROR imprimiendo objeto completo: ",e);
		}
	}
	
	
}
