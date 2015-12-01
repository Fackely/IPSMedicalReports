package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.Utilidades;

import com.princetonsa.actionform.manejoPaciente.AperturaIngresosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.AperturaIngresosDao;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.fwk.exception.IPSException;







/**
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 */

public class AperturaIngresos
{
	/**
	 *Atributos aperuta ingresos 
	 */

	private static Logger logger = Logger.getLogger(AperturaIngresos.class);
	
	//---manejo de indices -----------------------------------------------------
	
	public final static String  [] indicesListado={"consecutivo0_","fechaIngreso1_","horaIngreso2_","fechaCierre3_","horaCierrre4_","nombreUsuario5_",
													"codigoIngreso6_","codigoPaciente7_","centroAtencion8_","codigoCentroAtencion9_","cuenta10_",
													"link11_","viaIngreso12_","nomViaIngreso13_","motivoApertura14","nomMotivoCierre15_","estadoCuenta16_",
													"codigoCierre17_","reingreso18_","cuentaReingreso19_","consecutivoReingreso20_","preingreso21_"};
	
	//--------------------------------------------------------------------------
	
	
	/*-----------------------------------------------------------
	 *         				METODOS APERTURA INGRESOS
	 ------------------------------------------------------------*/
	

	
	/**
	 * Se inicializa el Dao
	 */
	public static AperturaIngresosDao  aperturaIngresosDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAperturaIngresosDao();
	}
	
	/**
	 * Metodo encargado de listar los ingresos de un paciente
	 * @param connection
	 * @param codigoPaciente
	 * ------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -------------------------------
	 * -- consecutivo0_
	 * -- fechaIngreso1_
	 * -- horaIngreso2_
	 * -- fechaCierre3_
	 * -- horaCierrre4_
	 * -- nombreUsuario5_
	 * -- codigoIngreso6_
	 * -- codigoPaciente7_
	 * -- centroAtencion8_
	 * -- codigoCentroAtencion9_
	 * -- cuenta10_
	 */
	public static HashMap cargarListadoIngresos (Connection connection,String codigoPaciente,String ingreso)
	{
		return aperturaIngresosDao().cargarListadoIngresos(connection, codigoPaciente,ingreso);
	}
	
	
	/**
	 * Metodo encargado de actualizar el estado del cierre.
	 * @param connection
	 * @param datosCierre
	 * ----------------------------
	 * KEY'S DEL MAPA CIERRE
	 * ----------------------------
	 * -- activo
	 * -- codigo
	 */
	public  static boolean ActualizarEstadoCierre (Connection connection,HashMap datosCierre)
	{
		return aperturaIngresosDao().ActualizarEstadoCierre(connection, datosCierre);
	}
	
	
	/**
	 * Metodo encargado de hacer una apertera de un ingreso cerrado.
	 * @param connection
	 * @param datosIngreso
	 * ------------------------------
	 * KEY'S DEL MAPA DATOSINGRESO
	 * -------------------------------
	 * -- estado			requerido
	 * -- cierreManual
	 * -- ingreso			requerido
	 * -- aperturaAuto
	 * -- usuarioModifica
	 * 
	 */
	public  static boolean ActualizarEstadoIngreso (Connection connection,HashMap datosIngreso)
	{
		return aperturaIngresosDao().ActualizarEstadoIngreso(connection, datosIngreso);
	}
	
	
	/**
	 * Metodo encargado de verificar el centro de atencion de los
	 * ingresos y de la sesion para saber si se activa el link o no.
	 * @param datos
	 * @return
	 */
	public static HashMap colocarLink (Connection connection, HashMap datos, UsuarioBasico usuario)
	{
		int numReg=Utilidades.convertirAEntero(datos.get("numRegistros")+"");
		for (int i=0;i<numReg;i++)
		{	
			if ((datos.get(indicesListado[9]+i)+"").equals(usuario.getCodigoCentroAtencion()+""))
				datos.put(indicesListado[11]+i, ConstantesBD.acronimoSi);
			else
				datos.put(indicesListado[11]+i, ConstantesBD.acronimoNo);
			
			
			//datos.put(indicesListado[18]+i,Utilidades.esIngresoReingreso(connection, datos.get(indicesListado[6]+i)+""));
			
		}
		return datos;
	}
	
	
	public static ActionForward guardar (Connection connection , AperturaIngresosForm forma, ActionMapping mapping,UsuarioBasico usuario)
	{
		logger.info("\n entre a guardar -->"+forma.getIngreso(indicesListado[14]));
		//-----se varifica si realizo una apertura
		
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		logger.info("----->INICIANDO TRANSACCION ....");
		
		if (  UtilidadCadena.noEsVacio(forma.getIngreso(indicesListado[14])+"") && !(forma.getIngreso(indicesListado[14])+"").equals(ConstantesBD.codigoNuncaValido+""))
		{
			logger.info("\n entre a guardar ");
			
			HashMap datosIngreso = new HashMap ();
			datosIngreso.put("estado", ConstantesIntegridadDominio.acronimoEstadoAbierto);
			datosIngreso.put("cierreManual", ConstantesBD.acronimoNo);
			datosIngreso.put("ingreso", forma.getListadoIngresos(indicesListado[6]+forma.getIndex()));
			if (ActualizarEstadoIngreso(connection, datosIngreso))
			{
				HashMap datosCierre = new HashMap();
				datosCierre.put("activo", ConstantesBD.acronimoNo);
				datosCierre.put("codigo", forma.getListadoIngresos(indicesListado[17]+forma.getIndex()));
				datosCierre.put("motivoApertura", forma.getIngreso(indicesListado[14]));
				datosCierre.put("aperturaAuto", ConstantesBD.acronimoNo);
				datosCierre.put("usuario", usuario.getLoginUsuario());
				transacction=ActualizarEstadoCierre(connection, datosCierre);
			}
			else
				transacction=false;
				
		}
		
		
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(connection);			
			logger.info("----->TRANSACCION AL 100% ....");
			forma.setOperacionTrue(true);
		}
		else
		{
			UtilidadBD.abortarTransaccion(connection);
		}
		
			
		
		UtilidadBD.closeConnection(connection);
		
		return mapping.findForward("listado");
		
	}
	
	
	public static HashMap accionOrdenarMapa(HashMap mapaOrdenar,AperturaIngresosForm forma)
	{			
		
		
		
		int numReg = Utilidades.convertirAEntero(mapaOrdenar.get("numRegistros")+"");		
		mapaOrdenar = (Listado.ordenarMapa(indicesListado,forma.getPatronOrdenar(),forma.getUltimoPatron(),mapaOrdenar,numReg));		
		forma.setUltimoPatron(forma.getPatronOrdenar());
		mapaOrdenar.put("numRegistros",numReg+"");
	
		
		return mapaOrdenar;
	}	
		
	
	public static ActionForward abrirReingreso (Connection connection,AperturaIngresosForm forma,ActionMapping mapping,UsuarioBasico usuario) throws IPSException
	{

		logger.info("\n entre a abrirReingreso ");
		//-----se varifica si realizo una apertura
		
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		logger.info("----->INICIANDO TRANSACCION ....");
		
		transacction=Cuenta.trasladarCuentaAingreso(connection, forma.getListadoIngresos(indicesListado[18]+"0")+"", ConstantesBD.codigoEstadoCuentaAsociada+"",  forma.getListadoIngresos(indicesListado[6]+"0")+"");

		if (transacction)
			transacction=Cuenta.asociosCuentaTotal(connection, usuario.getLoginUsuario(), forma.getListadoIngresos(indicesListado[10]+"0")+"", forma.getListadoIngresos(indicesListado[19]+"0")+"", ConstantesBD.acronimoTrueCorto, forma.getListadoIngresos(indicesListado[18]+"0")+"");

		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(connection);			
			logger.info("----->TRANSACCION AL 100% ....");
			forma.setOperacionTrue(true);
		}
		else
		{
			UtilidadBD.abortarTransaccion(connection);
		}
		
		UtilidadBD.closeConnection(connection);
		
		return mapping.findForward("listado");
		
		
	}
	
}