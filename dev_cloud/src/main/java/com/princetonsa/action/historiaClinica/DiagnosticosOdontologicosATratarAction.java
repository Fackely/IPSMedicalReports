package com.princetonsa.action.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.historiaClinica.DiagnosticosOdontologicosATratarForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.DiagnosticosOdontologicosATratar;


 
public class DiagnosticosOdontologicosATratarAction extends Action
{
	/**
	 * Para manjar los logger de la clase DiagnosticosOdontologicosATratarAction
	 */
	Logger logger = Logger.getLogger(DiagnosticosOdontologicosATratarAction.class);
	

	/**
	 * 
	 */
	private static String[] indices={"codigo_","nombre_","activo_","institucion_","numfotograma_","acronimo_","cartadental_","tiporegistro_","excluyente_","atratar_"};
	
	
	/**
	 * 
	 */
	public ActionForward execute (ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response) throws Exception
	{
		
		
		if (form instanceof DiagnosticosOdontologicosATratarForm) 
		{
			
			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			DiagnosticosOdontologicosATratarForm forma = (DiagnosticosOdontologicosATratarForm) form;
			
			String estado = forma.getEstado();

			logger.info("Estado: "+estado);
			
			DiagnosticosOdontologicosATratar mundo =new DiagnosticosOdontologicosATratar();
			
			if(estado == null)
			{
				logger.warn("Estado no Valido dentro del Flujo de Diagnosticos Odontologicos A Tratar (null)");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}
			else if(estado.equals("empezar"))
			{
				forma.reset();
				forma.resetMensaje();
				forma.setMapaDiagnosticos(mundo.consultarDiagnosticosATratar(usuario.getCodigoInstitucionInt()));
				return mapping.findForward("principal");
			}
			else if(estado.equals("nuevo"))
			{
				forma.resetMensaje(); 
				Utilidades.nuevoRegistroMapaGenerico(forma.getMapaDiagnosticos(),indices,"numRegistros","tiporegistro_","MEM");
				forma.getMapaDiagnosticos().put("activo_"+(Utilidades.convertirAEntero(forma.getMapaDiagnosticos().get("numRegistros")+"")-1), ValoresPorDefecto.getValorTrueCortoParaConsultas());
				forma.getMapaDiagnosticos().put("excluyente_"+(Utilidades.convertirAEntero(forma.getMapaDiagnosticos().get("numRegistros")+"")-1), ConstantesBD.acronimoNo);
				forma.getMapaDiagnosticos().put("atratar_"+(Utilidades.convertirAEntero(forma.getMapaDiagnosticos().get("numRegistros")+"")-1), ConstantesBD.acronimoNo);
				return mapping.findForward("principal");
			}
			else if(estado.equals("eliminarMotivo"))
			{
				Utilidades.eliminarRegistroMapaGenerico(forma.getMapaDiagnosticos(),forma.getMapaDiagnosticosEliminados(),forma.getIndiceDiagnosticoEliminar(),indices,"numRegistros","tiporegistro_","BD",false);
				forma.setIndiceDiagnosticoEliminar(ConstantesBD.codigoNuncaValido);
				return mapping.findForward("principal");
			}
			else if(estado.equals("guardar"))
			{
				
				this.accionGuardarRegistros(forma,mundo,usuario);
				forma.reset();
				forma.setMapaDiagnosticos(mundo.consultarDiagnosticosATratar(usuario.getCodigoInstitucionInt()));
				return mapping.findForward("principal");

			}
			else
			{
				forma.reset();
				logger.warn("Estado no valido dentro del flujo de Diagnosticos CANCELACION CITA ");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
			}
			
		}
		else
		{
			logger.error("El form no es compatible con el form de DiagnosticosOdontologicosATratarForm");
			request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
			return mapping.findForward("paginaError");
		}
	}



	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardarRegistros(DiagnosticosOdontologicosATratarForm forma,DiagnosticosOdontologicosATratar mundo, UsuarioBasico usuario) 
	{
		
		Connection con=UtilidadBD.abrirConexion();
		
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		
		/**
		 * Por ahora no se maneja la opcion de eliminacion, solo la de activar o inactivar.
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getMapaDiagnosticosEliminados("numRegistros")+"");i++)
		{
			if(mundo.eliminarRegistro(con, Utilidades.convertirAEntero(forma.getMapaDiagnosticosEliminados("codigo_"+i)+"")))
			{
				transaccion=true;
			}
		}
		*/
		
		for(int i=0;i<Integer.parseInt(forma.getMapaDiagnosticos("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getMapaDiagnosticos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma,mundo,Integer.parseInt(forma.getMapaDiagnosticos("codigo_"+i)+""),i,usuario))
			{
				HashMap vo=new HashMap();
				vo.put("codigo",forma.getMapaDiagnosticos("codigo_"+i));
				vo.put("nombre",forma.getMapaDiagnosticos("nombre_"+i));
				vo.put("activo",forma.getMapaDiagnosticos("activo_"+i));
				vo.put("atratar",forma.getMapaDiagnosticos("atratar_"+i));
				vo.put("excluyente",forma.getMapaDiagnosticos("excluyente_"+i));
				vo.put("institucion",usuario.getCodigoInstitucionInt()+"");
				vo.put("numfotograma",forma.getMapaDiagnosticos("numfotograma_"+i));
				vo.put("acronimo",forma.getMapaDiagnosticos("acronimo_"+i));
				//por ahora siempre se maneja carta dental
				//vo.put("cartadental",forma.getMapaDiagnosticos("cartadental_"+i));
				vo.put("cartadental",ConstantesBD.acronimoSi);
				vo.put("usuariomodifica",usuario.getLoginUsuario());
				vo.put("fechamodifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				vo.put("horamodifica",UtilidadFecha.getHoraActual());
				
				//logger.info("valor de atratar en modificar...>>>>>" +vo.get("atratar_"+i).toString());
				//logger.info("valor de excluyente en modificar...>>>>>" +vo.get("excluyente_"+i).toString());
				transaccion=mundo.modificar(con, vo);
			}
			
			else if((forma.getMapaDiagnosticos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("nombre",forma.getMapaDiagnosticos("nombre_"+i));
				vo.put("activo",forma.getMapaDiagnosticos("activo_"+i));
				vo.put("atratar",forma.getMapaDiagnosticos("atratar_"+i));
				vo.put("excluyente",forma.getMapaDiagnosticos("excluyente_"+i));
				vo.put("institucion",usuario.getCodigoInstitucionInt()+"");
				vo.put("numfotograma",forma.getMapaDiagnosticos("numfotograma_"+i));
				vo.put("acronimo",forma.getMapaDiagnosticos("acronimo_"+i));
				//vo.put("cartadental",forma.getMapaDiagnosticos("cartadental_"+i));
				vo.put("cartadental",ConstantesBD.acronimoSi);
				vo.put("usuariomodifica",usuario.getLoginUsuario());
				vo.put("fechamodifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				vo.put("horamodifica",UtilidadFecha.getHoraActual());
				//logger.info("valor de atratar en insertar...>>>>>" +vo.get("atratar_"+i).toString());
			    //logger.info("valor de excluyente en insertar...>>>>>" +vo.get("excluyente_"+i).toString());
				transaccion=mundo.insertar(con, vo);
			}
			
		}
		
		if(transaccion)
		{
			forma.setMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
		UtilidadBD.closeConnection(con);
	}



	private boolean existeModificacion(Connection con,DiagnosticosOdontologicosATratarForm forma,DiagnosticosOdontologicosATratar mundo, int codigo, int pos,UsuarioBasico usuario) 
	{
		HashMap temp=mundo.consultarDiagnosticosATratarEspecifico(con, codigo);
		for(int i=0;i<indices.length;i++)
		{
			if(temp.containsKey(indices[i]+"0")&&forma.getMapaDiagnosticos().containsKey(indices[i]+""+pos))
			{
				if(!((temp.get(indices[i]+"0")+"").trim().equals(forma.getMapaDiagnosticos(indices[i]+""+pos)+"")))
				{
					mundo.setMapaDiagnosticos(temp);
					return true;
				}
			}
		}
		return false;
	}
}