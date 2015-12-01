/*
 * Creado el 27/04/2006
 * Juan David Ramírez López
 */
package com.princetonsa.action.interfaz;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadBD;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.interfaz.ParamInterfazForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.interfaz.ParamInterfaz;

public class ParamInterfazAction extends Action
{
	/**
	 * Manejador de logs de la clase
	 */
	Logger logger = Logger.getLogger(ParamInterfazAction.class);

	/**
	 * Método que ejecuta el flujo de la funcionalidad
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		Connection con =null;
		try{	


			if (form instanceof ParamInterfazForm)
			{
				UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");

				ParamInterfazForm forma = (ParamInterfazForm) form;
				con = UtilidadBD.abrirConexion();

				String estado = forma.getEstado();

				logger.warn("Estado ParamInterfazAction " + estado);

				if (estado.equals("empezar"))
				{
					return accionEmpezar(con, forma, mapping);
				}
				else if (estado.equals("parametrizar"))
				{
					return accionParametrizar(con, forma, mapping, usuario);
				}
				else if (estado.equals("guardar"))
				{
					return accionGuardar(con, forma, mapping, request, usuario, false);
				}
				else if (estado.equals("guardarModificacion"))
				{
					return accionGuardar(con, forma, mapping, request, usuario, true);
				}
				else if (estado.equals("consultar"))
				{
					return accionEmpezar(con, forma, mapping);
				}
				else if (estado.equals("consultarListado"))
				{
					return accionConsultar(con, forma, mapping, usuario);
				}
				else if (estado.equals("consultarDetalle"))
				{
					return accionDetalle(con, forma, mapping, request, usuario);
				}
				return ComunAction.accionSalirCasoError(mapping, request, null, logger, "errors.estadoInvalido", "errors.estadoInvalido", true);
			}
			return ComunAction.accionSalirCasoError(mapping, request, null, logger, "errors.formaTipoInvalido", "errors.formaTipoInvalido", true);

		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;

}

	/**
	 * Consultar el detalle de la parametrización de interfaz
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @return
	 */
	private ActionForward accionDetalle(Connection con, ParamInterfazForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario)
	{
		forma.reset();
		ParamInterfaz mundo=new ParamInterfaz();
		forma.setTiposInterfaz(mundo.consultarTipos(con, 5));
		forma.setTiposRegistro(mundo.consultarTipos(con, 6));
		consultarRegistroInterfaz(con, forma, forma.getCodigoRegistro(), usuario);
		forma.setEstado("modificar");
		cerrarConexion(con);
		return mapping.findForward("modificar");
	}

	/**
	 * Método que consulta la parametrización de interfaz
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @return
	 */
	private ActionForward accionConsultar(Connection con, ParamInterfazForm forma, ActionMapping mapping, UsuarioBasico usuario)
	{
		ParamInterfaz mundo=new ParamInterfaz();
		forma.setListadoRegistrosInterfaz(mundo.listadoRegistrosInterfaz(con, usuario.getCodigoInstitucionInt(), forma.getTipoInterfaz(),false));
		cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método para guardar la parametrización de la interfaz
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @param esModificar @todo
	 * @return
	 */
	private ActionForward accionGuardar(Connection con, ParamInterfazForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario, boolean esModificar)
	{
		ParamInterfaz mundo=new ParamInterfaz();
		UtilidadBD.iniciarTransaccion(con);
		int codigo=0;
		if(esModificar)
		{
			codigo=mundo.ingresarRegistroInterfaz(con, forma.getTipoInterfaz(), forma.getDescripcion(), usuario.getLoginUsuario(), forma.getTipoRegistro(), forma.getCodigoRegistro());
		}
		else
		{
			codigo=mundo.ingresarRegistroInterfaz(con, forma.getTipoInterfaz(), forma.getDescripcion(), usuario.getLoginUsuario(), forma.getTipoRegistro(), 0);
		}
		Iterator<HashMap> tiposLibresIt=forma.getTiposLibres().iterator();
		Vector<Integer> tiposLibres=new Vector<Integer>();
		for(int zz=0;zz<forma.getTiposLibres().size();zz++)
		{
			HashMap tipoLibre=tiposLibresIt.next();
			tiposLibres.add(Integer.parseInt(tipoLibre.get("codigo")+""));
		}
		HashMap camposInterfaz=forma.getCamposInterfaz();
		if(codigo>0)
		{
			int numeroRegistros=Integer.parseInt(camposInterfaz.get("numRegistros")+"");
			for(int i=0; i<numeroRegistros; i++)
			{
				InfoDatosInt valor=null;
				InfoDatosInt valorSecundario=null;
				int campo=Integer.parseInt(camposInterfaz.get("codigo_tipo_campo_"+i)+"");
				int campoInterfaz=Integer.parseInt(camposInterfaz.get("codigo_campo_"+i)+"");
				String selectorStr=(String)camposInterfaz.get("select_principal_"+i);
				int codigoSelector=-0;
				if(selectorStr!=null)
				{
					codigoSelector=Integer.parseInt(selectorStr);
					valor=new InfoDatosInt(codigoSelector);
				}
				String nombre=(String)camposInterfaz.get("nombre_campo_"+i);
				switch(campo)
				{
					case ConstantesBD.codigoTipoCampoSecuencia:
						//No se hace nada
					break;
					case ConstantesBD.codigoTipoCampoLibre:
						String libre=(String)camposInterfaz.get("libre");
						valor=new InfoDatosInt();
						valor.setNombre(libre);
						valor.setCodigo(-1);
					break;
					case ConstantesBD.codigoTipoCampoCuentasContables:
					case ConstantesBD.codigoTipoCampoTipoMovimiento:
					case ConstantesBD.codigoTipoCampoCentroCostoSistema:
						int codigoSelectorSec=Integer.parseInt(camposInterfaz.get("select_secundario_"+i)+"");
						valorSecundario=new InfoDatosInt(codigoSelectorSec);
						if(tiposLibres.contains(codigoSelectorSec))
						{
							valorSecundario.setNombre(camposInterfaz.get("text_secundario_"+i)+"");
						}
					break;
					case ConstantesBD.codigoTipoCampoCampoDetalleSistema:
						int codigoSelectorPri=Integer.parseInt(camposInterfaz.get("select_principal_"+i)+"");
						valor=new InfoDatosInt(codigoSelectorPri);
						if(codigoSelectorPri==ConstantesBD.tipoSelectorInterfazCampoDescripcionFija)
						{
							valorSecundario=new InfoDatosInt(codigoSelectorPri);
							valorSecundario.setNombre(camposInterfaz.get("text_principal_"+i)+"");
						}
					break;
				}
				InfoDatosInt indicativoExiste=new InfoDatosInt();
				InfoDatosInt tamanio=new InfoDatosInt();
				
				int tempo=Integer.parseInt(camposInterfaz.get("existe_"+i)+"");
				indicativoExiste.setCodigo(tempo);
				if(tempo==ConstantesBD.codIndicativoSiNoExisteDefault)
				{
					String tempoText=camposInterfaz.get("text_existe_"+i)+"";
					indicativoExiste.setNombre(tempoText);
				}
				tempo=Integer.parseInt(camposInterfaz.get("tamanio_"+i)+"");
				tamanio.setCodigo(tempo);
				if(tempo==ConstantesBD.codTamanioFijo || tempo==ConstantesBD.codTamanioMenorQue)
				{
					String tempoText=camposInterfaz.get("text_tamanio_"+i)+"";
					tamanio.setNombre(tempoText);
				}
				if(mundo.ingresarDetalleRegistroInterfaz(con, codigo, campoInterfaz, nombre, valor, valorSecundario, indicativoExiste, tamanio)<=0)
				{
					UtilidadBD.abortarTransaccion(con);
					return ComunAction.accionSalirCasoError(mapping, request, null, logger, "errors.problemasBd", "errors.problemasBd", true);
				}
			}
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			return ComunAction.accionSalirCasoError(mapping, request, null, logger, "errors.problemasBd", "errors.problemasBd", true);
		}
		UtilidadBD.finalizarTransaccion(con);

		forma.reset();
		forma.setTiposInterfaz(mundo.consultarTipos(con, 5));
		consultarRegistroInterfaz(con, forma, codigo, usuario);
		forma.setEstado("resumen");
		
		cerrarConexion(con);
		return mapping.findForward("resumen");
	}
	
	/**
	 * Método para llenar los datos del registro de interfaz en la forma 
	 * @param con
	 * @param forma
	 * @param codigo
	 */
	private boolean consultarRegistroInterfaz(Connection con, ParamInterfazForm forma, int codigo, UsuarioBasico usuario)
	{
		ParamInterfaz mundo=new ParamInterfaz();

		/*
		 * Retomar parametrización
		 */
		forma.setTiposIndicativoExiste(mundo.consultarTipos(con, 1));
		forma.setTiposTamanio(mundo.consultarTipos(con, 2));
		forma.setOpcionesSelector(mundo.consultarTipos(con, 3));
		forma.setSelectores(mundo.consultarTipos(con, 4));
		forma.setTiposLibres(mundo.consultarTipos(con, 7));
		HashMap camposInterfaz=mundo.consultarCamposInterfaz(con, usuario.getCodigoInstitucionInt());
		forma.setCamposInterfaz(camposInterfaz);

		/*
		 * Cargar Registro
		 */
		HashMap registro=mundo.consultarRegistroInterfaz(con, codigo);
		try
		{
			int numeroRegistros=Integer.parseInt(registro.get("numRegistros")+"");
			if(numeroRegistros==0)
			{
				logger.error("Error cargando la parametrización del registro de interfaz (No existe el registro en la BD)");
				return false;
			}
		}
		catch (Exception e)
		{
			logger.error("Error cargando la parametrización del registro de interfaz "+e);
			return false;
		}
		
		forma.setDescripcion((String)registro.get("descripcion_0"));
		forma.setConsecutivo(Integer.parseInt(registro.get("consecutivo_0")+""));
		forma.setTipoInterfaz(Integer.parseInt(registro.get("tipo_interfaz_0")+""));
		forma.setTipoRegistro(Integer.parseInt(registro.get("tipo_registro_0")+""));
		
		/*
		 * Cargar el detalle del registro
		 */
		int numeroRegistros=Integer.parseInt(camposInterfaz.get("numRegistros")+"");
		HashMap detalle=mundo.consultarDetalleRegistroInterfaz(con, codigo);
		
		for(int i=0; i<numeroRegistros; i++)
		{
			int codigoCampo=Integer.parseInt(camposInterfaz.get("codigo_campo_"+i)+"");
			int numeroRegistrosDetalle=Integer.parseInt(detalle.get("numRegistros")+"");
			for(int j=0; j<numeroRegistrosDetalle; j++)
			{
				int codigoCampoDetalle=Integer.parseInt(detalle.get("campo_regis_interfaz_"+j)+"");
				if(codigoCampoDetalle==codigoCampo)
				{
					camposInterfaz.put("nombre_campo_"+i, (String)detalle.get("nombre_"+j));
					camposInterfaz.put("select_principal_"+i, detalle.get("valor_tipo_campo_int_"+j)==null?null:detalle.get("valor_tipo_campo_int_"+j)+"");
					camposInterfaz.put("select_secundario_"+i, detalle.get("secun_tipo_campo_int_"+j)==null?null:detalle.get("secun_tipo_campo_int_"+j)+"");
					camposInterfaz.put("existe_"+i, detalle.get("indicativo_existe_"+j)+"");
					camposInterfaz.put("tamanio_"+i, detalle.get("tamanio_campo_"+j)+"");
					
				}					
			}
			if(detalle.get("codigo_"+i)!=null)
			{
				int codigoDetalle=(Integer)detalle.get("codigo_"+i);
				HashMap valores=mundo.consultarValoresRegistroInterfaz(con, codigoDetalle);
				int numRegistros=Integer.parseInt(valores.get("numRegistros")+"");
				for(int k=0; k<numRegistros; k++)
				{
					int tipoCampo=Integer.parseInt(valores.get("tipo_"+k)+"");
					if(tipoCampo==ConstantesBD.tipoValorInterfazLibre)
					{
						camposInterfaz.put("libre", valores.get("valor_"+k)+"");
					}
					else if(tipoCampo==ConstantesBD.tipoValorInterfazDefault)
					{
						camposInterfaz.put("text_existe_"+i, valores.get("valor_"+k)+"");
					}
					else if(tipoCampo==ConstantesBD.tipoValorInterfazDescripcionFija)
					{
						int codigoTipoCampo=Integer.parseInt(camposInterfaz.get("codigo_tipo_campo_"+i)+"");
						if(codigoTipoCampo==ConstantesBD.codigoTipoCampoCentroCostoSistema)
						{
							camposInterfaz.put("text_secundario_"+i, valores.get("valor_"+k)+"");
						}
						else if(codigoTipoCampo==ConstantesBD.codigoTipoCampoCampoDetalleSistema)
						{
							camposInterfaz.put("text_principal_"+i, valores.get("valor_"+k)+"");
						}
					}
					else if(tipoCampo==ConstantesBD.tipoValorInterfazTamanoFijo || tipoCampo==ConstantesBD.tipoValorInterfazTamanoMenorQue)
					{
						camposInterfaz.put("text_tamanio_"+i, valores.get("valor_"+k)+"");
					}
				}
			}
		}
		return true;
	}

	/**
	 * Método que inicializa la funcionalidad
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return Pagina principal
	 */
	private ActionForward accionEmpezar(Connection con,	ParamInterfazForm forma, ActionMapping mapping)
	{
		forma.reset();
		ParamInterfaz mundo = new ParamInterfaz();
		forma.setTiposInterfaz(mundo.consultarTipos(con, 5));
		forma.setTiposRegistro(mundo.consultarTipos(con, 6));
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método que permite el ingreso de la nueva parametrización
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return forward a la página principal
	 */
	private ActionForward accionParametrizar(Connection con, ParamInterfazForm forma, ActionMapping mapping, UsuarioBasico usuario)
	{
		ParamInterfaz mundo = new ParamInterfaz();
		forma.setTiposIndicativoExiste(mundo.consultarTipos(con, 1));
		forma.setTiposTamanio(mundo.consultarTipos(con, 2));
		forma.setOpcionesSelector(mundo.consultarTipos(con, 3));
		forma.setSelectores(mundo.consultarTipos(con, 4));
		forma.setTiposLibres(mundo.consultarTipos(con, 7));
		forma.setCamposInterfaz(mundo.consultarCamposInterfaz(con, usuario.getCodigoInstitucionInt()));
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método para cerrar la conexión
	 * 
	 * @param con Conexión abierta
	 */
	private void cerrarConexion(Connection con)
	{
		try
		{
			UtilidadBD.cerrarConexion(con);
		} catch (SQLException e)
		{
			logger.error("Error cerrando la conexión " + e);
		}
	}
}
