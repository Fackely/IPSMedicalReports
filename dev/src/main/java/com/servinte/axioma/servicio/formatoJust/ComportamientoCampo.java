/**
 * 
 */
package com.servinte.axioma.servicio.formatoJust;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.UtilidadTexto;

import com.princetonsa.actionform.inventarios.FormatoJustArtNoposForm;
import com.princetonsa.actionform.inventarios.FormatoJustInsNoposForm;
import com.princetonsa.actionform.inventarios.FormatoJustServNoposForm;
import com.princetonsa.dto.historiaClinica.DtoParamCamposJusNoPos;
import com.princetonsa.dto.historiaClinica.DtoParamSeccionesJusNoPos;
import com.princetonsa.dto.inventario.DtoAccionCampo;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.FormatoJustArtNopos;

/**
 * @author jeilones
 *
 */
public class ComportamientoCampo {

	/**
	 * 
	 */
	public ComportamientoCampo() {
		// TODO Auto-generated constructor stub
	}

	
	/**
     * Carga los valores por defecto de los campos afectados por las acciones de otros campos
     * 
     * @param con
     * @param forma
     * @param mapping
     * @param request
     */
    @SuppressWarnings("unchecked")
	public static void cargarValoresDefectoAccionesCampo(Connection con,
			FormatoJustArtNoposForm forma, ActionMapping mapping,
			HttpServletRequest request){
    	int posicionRegistro=-1;
		int identificadorCampo=-1;
		int numeroSeccion=-1;
		
		FormatoJustArtNopos fjan = new FormatoJustArtNopos ();
		
		HashMap mapa=forma.getFormularioMap();
		HashMap mapaSecciones=forma.getMapaSecciones();
		int numRegSecciones=Integer.parseInt((String)(mapaSecciones.get("numRegistros")));
		
		for(int i=0;i<numRegSecciones;i++){
			int numRegistrosCampos=Integer.parseInt((String)mapa.get("numRegistros_"+mapaSecciones.get("codigo_"+i)));
			for(int j=0;j<numRegistrosCampos;j++){
				List<DtoAccionCampo>listaAcciones=fjan.consultarAccionesCampo(con,Integer.parseInt(mapa.get("campo_"+mapaSecciones.get("codigo_"+i)+"_"+j).toString()) , "N");
				for(DtoAccionCampo accionCampo:listaAcciones){
					int []valores=buscarIdentificadoresCampo(mapa,mapaSecciones,numRegSecciones,accionCampo.getCampoAfectado().toString());
					String valorInicial=mapa.get("valorcampo_"+mapaSecciones.get("codigo_"+i)+"_"+j)!=null?mapa.get("valorcampo_"+mapaSecciones.get("codigo_"+i)+"_"+j).toString():"";
					boolean cumpleValidacion=false;
					switch(accionCampo.getAccion()){
						case DtoAccionCampo.ACCION_CAMBIAR_VISIBILIDAD:
							
							
							if(valores!=null&&valores!=null&&mapa.get("campo_"+valores[2]+"_"+valores[0]).toString().equals(""+accionCampo.getCampoAfectado())){
								posicionRegistro=valores[0];
								identificadorCampo=valores[1];
								numeroSeccion=valores[2];
								
								if(valorInicial.trim().equals("")){
									mapa.put("mostrar_"+numeroSeccion+"_"+posicionRegistro,UtilidadTexto.getBoolean(""+accionCampo.getVisibilidadInicial())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
								}else{
									cumpleValidacion=false;
									if(accionCampo.getValorValidar()!=null){
										String[] valoresValidar=accionCampo.getValorValidar().split(",");
										for(String valorValidar:valoresValidar){
											if(valorValidar.equals(valorInicial)&&UtilidadTexto.getBoolean(mapa.get("mostrar_"+mapaSecciones.get("codigo_"+i)+"_"+j).toString())){
												mapa.put("mostrar_"+numeroSeccion+"_"+posicionRegistro,ConstantesBD.acronimoSi);
												cumpleValidacion=true;
												break;
											}
										}
										
									}
									if(!cumpleValidacion){
										mapa.put("mostrar_"+numeroSeccion+"_"+posicionRegistro,ConstantesBD.acronimoNo);
									}
								}
							}
							
							//cambiarVisibilidad(mapa,mapaSecciones,i,j,accionCampo,(String)mapa.get("valorcampo_"+mapaSecciones.get("codigo_"+i)+"_"+j));
							
							break;
						case DtoAccionCampo.ACCION_UTILIZAR_MEDICAMENTOS_POS:
							cumpleValidacion=false;
							if(accionCampo.getValorValidar()!=null){
								String[] valoresValidar=accionCampo.getValorValidar().split(",");
								for(String valorValidar:valoresValidar){
									if(valorValidar.equals(valorInicial)){
										forma.setSeHanUtilizadoMedicamentosPosEnElTratamientoDelPaciente(ConstantesBD.acronimoSi);
										cumpleValidacion=true;
										break;
									}
								}
								
							}
							if(!cumpleValidacion){
								forma.setSeHanUtilizadoMedicamentosPosEnElTratamientoDelPaciente(ConstantesBD.acronimoNo);
							}
							
							break;
						case DtoAccionCampo.ACCION_INFORMAR_RIESGO_TRATAMIENTO:
							
							cambiarBanderaSeInformaRiesgoTratamiento(forma, mapa, mapaSecciones, accionCampo, valorInicial);
							break;
					}
				}
			}
		}
    }
    
	/**
	 * Busca los identificadores para ubicar un campo en el mapa dado el identificador primario del campo
	 * 
	 * @param mapa
	 * @param mapaSecciones
	 * @param numRegSecciones
	 * @param idCampo
	 * @return [posicionRegistro,idCampo,numeroSeccion]
	 */
	public static int[] buscarIdentificadoresCampo(HashMap mapa, HashMap mapaSecciones,int numRegSecciones,String idCampo) {
		if(idCampo==null){
			return null;
		}
		boolean accionEjecutada=false;
		
		int [] valores=new int[]{-1,-1,-1};
		
		for(int i=0;i<numRegSecciones;i++){
			int numRegistrosCampos=Integer.parseInt((String)mapa.get("numRegistros_"+mapaSecciones.get("codigo_"+i)));
			for(int j=0;j<numRegistrosCampos;j++){
				if(mapa.get("campo_"+mapaSecciones.get("codigo_"+i).toString()+"_"+j).toString().equals(idCampo)){
					
					valores[0]=j;
					valores[1]=Integer.parseInt(mapa.get("campo_"+mapaSecciones.get("codigo_"+i).toString()+"_"+j).toString());
					valores[2]=Integer.parseInt(mapaSecciones.get("codigo_"+i).toString());
					accionEjecutada=true;
					break;
				}
			}
			if(accionEjecutada){
				break;
			}
		}
		
		if(!accionEjecutada){
			valores=null;
		}
		
		return valores;
	}

	/**
	 * Se ejecuta la accion correspondiente por cada campo afectado
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	public synchronized static ActionForward accionCampo(Connection con,
			FormatoJustArtNoposForm forma, ActionMapping mapping,
			HttpServletRequest request, UsuarioBasico usuario,
			PersonaBasica paciente) {
		// TODO Auto-generated method stub
		
		int posicionRegistro=Integer.parseInt(forma.getPosicionRegistro());
		int identificadorCampo=Integer.parseInt(forma.getIdentificadorCampo());
		int numeroSeccion=Integer.parseInt(forma.getNumeroSeccion());
		
		FormatoJustArtNopos fjan = new FormatoJustArtNopos ();
		
		List<DtoAccionCampo>listaAcciones=fjan.consultarAccionesCampo(con, identificadorCampo, "N");
		HashMap mapa=forma.getFormularioMap();
		HashMap mapaSecciones=forma.getMapaSecciones();
		int numRegSecciones=Integer.parseInt((String)(mapaSecciones.get("numRegistros")));
		
		boolean accionEjecutada=false;
		
		for(DtoAccionCampo accionCampo:listaAcciones){
			for(int i=0;i<numRegSecciones;i++){
				int numRegistrosCampos=Integer.parseInt((String)mapa.get("numRegistros_"+mapaSecciones.get("codigo_"+i)));
				for(int j=0;j<numRegistrosCampos;j++){
					switch(accionCampo.getAccion()){
						case DtoAccionCampo.ACCION_CAMBIAR_VISIBILIDAD:
							if(mapa.get("campo_"+mapaSecciones.get("codigo_"+i)+"_"+j).toString().equals(""+accionCampo.getCampoAfectado())){
								cambiarVisibilidad(con,forma,mapa,mapaSecciones,i,j,accionCampo,(String)mapa.get("valorcampo_"+numeroSeccion+"_"+posicionRegistro));
								accionEjecutada=true;
							}
							break;
						case DtoAccionCampo.ACCION_UTILIZAR_MEDICAMENTOS_POS:
							/*if(((String)mapa.get("valorcampo_"+numeroSeccion+"_"+posicionRegistro)).equals(accionCampo.getValorValidar())){
								forma.setSeHanUtilizadoMedicamentosPosEnElTratamientoDelPaciente(ConstantesBD.acronimoSi);
							}else{
								forma.setSeHanUtilizadoMedicamentosPosEnElTratamientoDelPaciente(ConstantesBD.acronimoNo);
							}*/
							
							cambiarBanderaSeUtilizaMedPos(forma,mapa, mapaSecciones, accionCampo, ((String)mapa.get("valorcampo_"+numeroSeccion+"_"+posicionRegistro)));
							accionEjecutada=true;							
							break;
						case DtoAccionCampo.ACCION_INFORMAR_RIESGO_TRATAMIENTO:
							cambiarBanderaSeInformaRiesgoTratamiento(forma, mapa, mapaSecciones, accionCampo, ((String)mapa.get("valorcampo_"+numeroSeccion+"_"+posicionRegistro)));
							accionEjecutada=true;
							break;
					}
				}
				if(accionEjecutada){
					break;
				}
			}
			accionEjecutada=false;
		}
		
		return mapping.findForward("principal");
	}

	/**
	 * Se cambia la visibilidad del campo afectado
	 * @param forma 
	 * 
	 * @param long1 
	 * @param mapaSecciones 
	 * @param mapa 
	 * @param valorReal 
	 * @param accionCampo 
	 */
	@SuppressWarnings("unchecked")
	public static void cambiarVisibilidad(Connection con,FormatoJustArtNoposForm forma, HashMap mapa, HashMap mapaSecciones,int numRegSeccion,int numRegCam, DtoAccionCampo accionCampo, String valorReal){
		
		mapa=forma.getFormularioMap();
		
		boolean cumpleValidacion=false;
		
		if(accionCampo.getValorValidar()!=null){
			String[] valoresValidar=accionCampo.getValorValidar().split(",");
			for(String valorValidar:valoresValidar){
				if(valorValidar.equals(valorReal)){
					mapa.put("mostrar_"+mapaSecciones.get("codigo_"+numRegSeccion).toString()+"_"+numRegCam,ConstantesBD.acronimoSi);
					cumpleValidacion=true;
					break;
				}
			}
			
		}
		if(!cumpleValidacion){
			mapa.put("mostrar_"+mapaSecciones.get("codigo_"+numRegSeccion).toString()+"_"+numRegCam,ConstantesBD.acronimoNo);
			cambiarVisibilidadHijos(con,forma,mapa,mapaSecciones,Integer.parseInt((String)(mapaSecciones.get("numRegistros"))),accionCampo.getCampoAccion().intValue(),"N",null);
		}else{
			int[] valores=buscarIdentificadoresCampo(mapa, mapaSecciones, Integer.parseInt((String)(mapaSecciones.get("numRegistros"))), accionCampo.getCampoAccion().toString());
			if(valores!=null){
				cambiarVisibilidadHijos(con,forma,mapa,mapaSecciones,Integer.parseInt((String)(mapaSecciones.get("numRegistros"))),accionCampo.getCampoAccion().intValue(),"N",(String)mapa.get("valorcampo_"+valores[2]+"_"+valores[0]));
			}
		}
	}
	/**
	 * Se cambia la visibilidad del campo hijo afectado
	 * @param forma 
	 * 
	 * @param long1 
	 * @param mapaSecciones 
	 * @param mapa 
	 * @param string 
	 * @param valorReal 
	 * @param accionCampo 
	 */
	@SuppressWarnings("unchecked")
	public static void cambiarVisibilidadHijos(Connection con,FormatoJustArtNoposForm forma, HashMap mapa, HashMap mapaSecciones,int numRegSecciones,int idCampo, String servicio, String valorReal){
		FormatoJustArtNopos fjan = new FormatoJustArtNopos ();
		
		mapa=forma.getFormularioMap();
		
		List<DtoAccionCampo>listaAcciones=fjan.consultarAccionesCampo(con, idCampo, servicio);
		for(DtoAccionCampo accionCampo:listaAcciones){
			if(accionCampo.getAccion()==DtoAccionCampo.ACCION_CAMBIAR_VISIBILIDAD){
				int []valores=buscarIdentificadoresCampo(mapa,mapaSecciones,numRegSecciones,accionCampo.getCampoAfectado().toString());
				if(valores!=null){
				
					if(valorReal==null){
						mapa.put("mostrar_"+valores[2]+"_"+valores[0],ConstantesBD.acronimoNo);
						cambiarVisibilidadHijos(con,forma, mapa, mapaSecciones, numRegSecciones,valores[1] , servicio, null);
					}else{
						String[] valoresValidar=accionCampo.getValorValidar().split(",");
						for(String valorValidar:valoresValidar){
							if(valorValidar.equals(valorReal)){
								mapa.put("mostrar_"+valores[2]+"_"+valores[0],ConstantesBD.acronimoSi);
								cambiarVisibilidadHijos(con,forma, mapa, mapaSecciones, numRegSecciones,valores[1] , servicio, (String)mapa.get("valorcampo_"+valores[2]+"_"+valores[0]));
								break;
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Se cambia la bandera que indica si se utilizan medicamentos POS
	 * 
	 * @param forma
	 * @param mapa
	 * @param mapaSecciones
	 * @param accionCampo
	 * @param valorReal
	 */
	@SuppressWarnings("unchecked")
	public static void cambiarBanderaSeUtilizaMedPos(FormatoJustArtNoposForm forma,HashMap mapa, HashMap mapaSecciones,DtoAccionCampo accionCampo, String valorReal){
		
		boolean cumpleValidacion=false;
		
		if(accionCampo.getValorValidar()!=null){
			String[] valoresValidar=accionCampo.getValorValidar().split(",");
			for(String valorValidar:valoresValidar){
				if(valorValidar.equals(valorReal)){
					forma.setSeHanUtilizadoMedicamentosPosEnElTratamientoDelPaciente(ConstantesBD.acronimoSi);
					cumpleValidacion=true;
					break;
				}
			}
		}
		if(!cumpleValidacion){
			forma.setSeHanUtilizadoMedicamentosPosEnElTratamientoDelPaciente(ConstantesBD.acronimoNo);
		}
	}
	
	/**
	 * Se cambia la bandera que indica si se utilizan medicamentos POS
	 * 
	 * @param forma
	 * @param mapa
	 * @param mapaSecciones
	 * @param accionCampo
	 * @param valorReal
	 */
	@SuppressWarnings("unchecked")
	public static void cambiarBanderaSeInformaRiesgoTratamiento(FormatoJustArtNoposForm forma,HashMap mapa, HashMap mapaSecciones,DtoAccionCampo accionCampo, String valorReal){
		boolean cumpleValidacion=false;
		if(accionCampo.getValorValidar()!=null){
			String[] valoresValidar=accionCampo.getValorValidar().split(",");
			for(String valorValidar:valoresValidar){
				if(valorValidar.equals(valorReal)){
					forma.setSeInformaAlPacienteRiesgosTratamiento(false);
					cumpleValidacion=true;
					break;
				}
			}
			
		}
		if(!cumpleValidacion){
			forma.setSeInformaAlPacienteRiesgosTratamiento(true);
		}
	}


	/**
	 * Se ejecuta la accion correspondiente por cada campo afectado (servicios)
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	public static ActionForward accionCampo(Connection con,
			FormatoJustServNoposForm forma, ActionMapping mapping,
			HttpServletRequest request, UsuarioBasico usuario,
			PersonaBasica paciente) {
		// TODO Auto-generated method stub
		
		int posicionRegistro=Integer.parseInt(forma.getPosicionRegistro());
		int posicionCampo=Integer.parseInt(forma.getPosicionCampo());
		int identificadorCampo=Integer.parseInt(forma.getIdentificadorCampo());
		int numeroSeccion=Integer.parseInt(forma.getNumeroSeccion());
		
		FormatoJustArtNopos fjan = new FormatoJustArtNopos ();
		
		List<DtoAccionCampo>listaAcciones=fjan.consultarAccionesCampo(con, identificadorCampo, "S");
		HashMap mapa=forma.getFormatoJustNoposMap();
		HashMap mapaSecciones=(HashMap)mapa.get("mapasecciones");
		int numRegSecciones=Integer.parseInt((String)(mapaSecciones.get("numRegistros")));
		
		
		for(DtoAccionCampo accionCampo:listaAcciones){
			switch(accionCampo.getAccion()){
				case DtoAccionCampo.ACCION_CAMBIAR_VISIBILIDAD:
					int[] valores=buscarIdentificadoresCampoServicio(mapa, mapaSecciones, numRegSecciones, accionCampo.getCampoAfectado()+"");
				if(buscarIdentificadoresCampoServicio(mapa, mapaSecciones, numRegSecciones, accionCampo.getCampoAfectado()+"")!=null){	
					if( mapa.get("codigocampo_"+valores[3]+"_"+valores[0]).toString()!=null &&    mapa.get("codigocampo_"+valores[3]+"_"+valores[0]).toString().equals(""+accionCampo.getCampoAfectado())){
						cambiarVisibilidad(con,forma,mapa,mapaSecciones,valores[2],valores[0],accionCampo,(String)mapa.get("valorcampo_"+numeroSeccion+"_"+posicionRegistro));
					}
				}
					break;
				case DtoAccionCampo.ACCION_CAMBIAR_OBLIGATORIEDAD:
					cambiarValorRequerido(mapa, accionCampo, numRegSecciones, mapaSecciones, numeroSeccion, posicionRegistro);
					break;
			}
		}
		
		return mapping.findForward("formato");
	}
	/**
	 * Cambiar el valor requerido del campo afectado 
	 * 
	 * @param mapa
	 * @param accionCampo
	 * @param numRegSecciones
	 * @param mapaSecciones
	 * @param numeroSeccion
	 * @param posicionRegistro
	 */
	public static void cambiarValorRequerido(HashMap mapa, DtoAccionCampo accionCampo, int numRegSecciones, HashMap mapaSecciones, int numeroSeccion, int posicionRegistro) {
		// TODO Auto-generated method stub
		int[] valores=buscarIdentificadoresCampoServicio(mapa, mapaSecciones, numRegSecciones, accionCampo.getCampoAfectado()+"");
		String valorReal=(String) mapa.get("valorcampo_"+numeroSeccion+"_"+posicionRegistro);
		
		boolean cumpleValidacion=false;
		
		if(valores!=null){
			if(accionCampo.getValorValidar()!=null){
				String[] valoresValidar=accionCampo.getValorValidar().split(",");
				for(String valorValidar:valoresValidar){
					if(valorValidar.equals(valorReal)){
						mapa.put("requerido_"+valores[3]+"_"+valores[0],ConstantesBD.acronimoSi);
						cumpleValidacion=true;
						break;
					}
				}
				
			}
		}
		if(!cumpleValidacion){
			if(valores!=null){
				mapa.put("requerido_"+valores[3]+"_"+valores[0],ConstantesBD.acronimoNo);
			}
		}
	}


	/**
	 * Busca los identificadores para ubicar un campo en el mapa dado el identificador primario del campo (servicios)
	 * 
	 * @param mapa
	 * @param mapaSecciones
	 * @param numRegSecciones
	 * @param idCampo
	 * @return [posicionRegistro,idCampo,posicionSeccion,numeroSeccion]
	 */
	public static int[] buscarIdentificadoresCampoServicio(HashMap mapa, HashMap mapaSecciones,int numRegSecciones,String idCampo) {
		if(idCampo==null){
			return null;
		}
		boolean accionEjecutada=false;
		
		int [] valores=new int[]{-1,-1,-1,-1};
		
		for(int i=0;i<numRegSecciones;i++){
			int numRegistrosCampos=Integer.parseInt((String)mapa.get("numRegistrosXSec_"+mapaSecciones.get("codigo_"+i).toString()));
			for(int j=0;j<numRegistrosCampos;j++){
				if(mapa.get("codigocampo_"+mapaSecciones.get("codigo_"+i).toString()+"_"+j).toString().equals(idCampo)){
					
					valores[0]=j;
					valores[1]=Integer.parseInt(mapa.get("codigocampo_"+mapaSecciones.get("codigo_"+i).toString()+"_"+j).toString());
					valores[2]=i;
					valores[3]=Integer.parseInt(mapaSecciones.get("codigo_"+i).toString());
					accionEjecutada=true;
					break;
				}
			}
			if(accionEjecutada){
				break;
			}
		}
		
		if(!accionEjecutada){
			valores=null;
		}
		
		return valores;
	}


	public static void cargarValoresDefectoAccionesCampoServicios(Connection con,
			FormatoJustServNoposForm forma, ActionMapping mapping,
			HttpServletRequest request) {
		int posicionRegistro=-1;
		int identificadorCampo=-1;
		int numeroSeccion=-1;
		
		FormatoJustArtNopos fjan = new FormatoJustArtNopos ();
		
		HashMap mapa=forma.getFormatoJustNoposMap();
		HashMap mapaSecciones=(HashMap)mapa.get("mapasecciones");
		int numRegSecciones=Integer.parseInt((String)(mapaSecciones.get("numRegistros")));
		
		for(int i=0;i<numRegSecciones;i++){
			int numRegistrosCampos=Integer.parseInt(mapa.get("numRegistrosXSec_"+mapaSecciones.get("codigo_"+i)).toString());
			for(int j=0;j<numRegistrosCampos;j++){
				List<DtoAccionCampo>listaAcciones=fjan.consultarAccionesCampo(con,Integer.parseInt(mapa.get("codigocampo_"+mapaSecciones.get("codigo_"+i)+"_"+j).toString()) , "S");
				for(DtoAccionCampo accionCampo:listaAcciones){
					int []valores=buscarIdentificadoresCampoServicio(mapa, mapaSecciones, numRegSecciones, accionCampo.getCampoAfectado().toString());
					String valorInicial=mapa.get("valorcampo_"+mapaSecciones.get("codigo_"+i)+"_"+j)!=null?mapa.get("valorcampo_"+mapaSecciones.get("codigo_"+i)+"_"+j).toString():"";
					boolean cumpleValidacion=false;
					switch(accionCampo.getAccion()){
						case DtoAccionCampo.ACCION_CAMBIAR_VISIBILIDAD:
							
							
							if(valores!=null&&valores!=null&&mapa.get("codigocampo_"+valores[3]+"_"+valores[0]).toString().equals(""+accionCampo.getCampoAfectado())){
								posicionRegistro=valores[0];
								identificadorCampo=valores[1];
								numeroSeccion=valores[3];
								
								if(valorInicial.trim().equals("")){
									mapa.put("mostrar_"+numeroSeccion+"_"+posicionRegistro,UtilidadTexto.getBoolean(""+accionCampo.getVisibilidadInicial())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
								}else{
									cumpleValidacion=false;
									if(accionCampo.getValorValidar()!=null){
										String[] valoresValidar=accionCampo.getValorValidar().split(",");
										for(String valorValidar:valoresValidar){
											if(valorValidar.equals(valorInicial)&&UtilidadTexto.getBoolean(mapa.get("mostrar_"+mapaSecciones.get("codigo_"+i)+"_"+j).toString())){
												mapa.put("mostrar_"+numeroSeccion+"_"+posicionRegistro,ConstantesBD.acronimoSi);
												cumpleValidacion=true;
												break;
											}
										}
										
									}
									if(!cumpleValidacion){
										mapa.put("mostrar_"+numeroSeccion+"_"+posicionRegistro,ConstantesBD.acronimoNo);
									}
								}
							}
							
							break;
						case DtoAccionCampo.ACCION_CAMBIAR_OBLIGATORIEDAD:
							
							cambiarValorRequerido(mapa, accionCampo, numRegSecciones, mapaSecciones, valores[3], valores[0]);
							break;
					}
				}
			}
		}
		
	}
	
	/**
	 * Se cambia la visibilidad del campo afectado
	 * @param forma 
	 * 
	 * @param long1 
	 * @param mapaSecciones 
	 * @param mapa 
	 * @param valorReal 
	 * @param accionCampo 
	 */
	@SuppressWarnings("unchecked")
	public static void cambiarVisibilidad(Connection con,FormatoJustServNoposForm forma, HashMap mapa, HashMap mapaSecciones,int numRegSeccion,int numRegCam, DtoAccionCampo accionCampo, String valorReal){
		
		mapa=forma.getFormatoJustNoposMap();
		mapaSecciones=(HashMap)mapa.get("mapasecciones");
		
		boolean cumpleValidacion=false;
		
		if(accionCampo.getValorValidar()!=null){
			String[] valoresValidar=accionCampo.getValorValidar().split(",");
			for(String valorValidar:valoresValidar){
				if(valorValidar.equals(valorReal)){
					mapa.put("mostrar_"+mapaSecciones.get("codigo_"+numRegSeccion).toString()+"_"+numRegCam,ConstantesBD.acronimoSi);
					cumpleValidacion=true;
					break;
				}
			}
			
		}
		if(!cumpleValidacion){
			mapa.put("mostrar_"+mapaSecciones.get("codigo_"+numRegSeccion).toString()+"_"+numRegCam,ConstantesBD.acronimoNo);
			cambiarVisibilidadHijos(con,forma,mapa,mapaSecciones,Integer.parseInt((String)(mapaSecciones.get("numRegistros"))),accionCampo.getCampoAccion().intValue(),"S",null);
		}else{
			int[] valores=buscarIdentificadoresCampoServicio(mapa, mapaSecciones, Integer.parseInt((String)(mapaSecciones.get("numRegistros"))), accionCampo.getCampoAccion().toString());
			if(valores!=null){
				cambiarVisibilidadHijos(con,forma,mapa,mapaSecciones,Integer.parseInt((String)(mapaSecciones.get("numRegistros"))),accionCampo.getCampoAccion().intValue(),"S",(String)mapa.get("valorcampo_"+valores[3]+"_"+valores[0]));
			}
		}
	}
	/**
	 * Se cambia la visibilidad del campo hijo afectado
	 * @param forma 
	 * 
	 * @param long1 
	 * @param mapaSecciones 
	 * @param mapa 
	 * @param string 
	 * @param valorReal 
	 * @param accionCampo 
	 */
	@SuppressWarnings("unchecked")
	public static void cambiarVisibilidadHijos(Connection con,FormatoJustServNoposForm forma, HashMap mapa, HashMap mapaSecciones,int numRegSecciones,int idCampo, String servicio, String valorReal){
		FormatoJustArtNopos fjan = new FormatoJustArtNopos ();
		
		mapa=forma.getFormatoJustNoposMap();
		mapaSecciones=(HashMap)mapa.get("mapasecciones");
		
		List<DtoAccionCampo>listaAcciones=fjan.consultarAccionesCampo(con, idCampo, servicio);
		for(DtoAccionCampo accionCampo:listaAcciones){
			if(accionCampo.getAccion()==DtoAccionCampo.ACCION_CAMBIAR_VISIBILIDAD){
				int []valores=buscarIdentificadoresCampoServicio(mapa,mapaSecciones,numRegSecciones,accionCampo.getCampoAfectado().toString());
				if(valores!=null){
				
					if(valorReal==null){
						mapa.put("mostrar_"+valores[3]+"_"+valores[0],ConstantesBD.acronimoNo);
						cambiarVisibilidadHijos(con,forma, mapa, mapaSecciones, numRegSecciones,valores[1] , servicio, null);
					}else{
						String[] valoresValidar=accionCampo.getValorValidar().split(",");
						for(String valorValidar:valoresValidar){
							if(valorValidar.equals(valorReal)){
								mapa.put("mostrar_"+valores[3]+"_"+valores[0],ConstantesBD.acronimoSi);
								cambiarVisibilidadHijos(con,forma, mapa, mapaSecciones, numRegSecciones,valores[1] , servicio, (String)mapa.get("valorcampo_"+valores[3]+"_"+valores[0]));
								break;
							}
						}
					}
				}
			}
		}
	}
	
	
	/*****************************INSUMOS****************************/
	/**
	 * 
	 * Se ejecuta la accion correspondiente por cada campo afectado
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @return
	 */
	public synchronized static ActionForward accionCampo(Connection con,
			FormatoJustInsNoposForm forma, ActionMapping mapping,
			HttpServletRequest request){
		
		List<DtoParamSeccionesJusNoPos>secciones=forma.getDtoParam().getSecciones();
		
		DtoParamSeccionesJusNoPos seccion=secciones.get(Integer.parseInt(forma.getPosicionSeccion()));
		DtoParamCamposJusNoPos campo=seccion.getCampos().get(Integer.parseInt(forma.getPosicionCampo()));
		
		FormatoJustArtNopos fjan = new FormatoJustArtNopos ();
		
		List<DtoAccionCampo>listaAcciones=fjan.consultarAccionesCampo(con,Integer.parseInt(campo.getCodigo()) , "N");
		for(DtoAccionCampo accionCampo:listaAcciones){
			int []valores=buscarIdentificadoresCampoInsumo(secciones,accionCampo.getCampoAfectado().toString());
			if(valores!=null){
				DtoParamCamposJusNoPos campoAfectado=secciones.get(valores[0]).getCampos().get(valores[1]);
				String valorInicial=campo.getValor();
				boolean cumpleValidacion=false;
				switch(accionCampo.getAccion()){
					case DtoAccionCampo.ACCION_CAMBIAR_VISIBILIDAD:
						
						cumpleValidacion=false;
						if(accionCampo.getValorValidar()!=null){
							String[] valoresValidar=accionCampo.getValorValidar().split(",");
							for(String valorValidar:valoresValidar){
								if(valorValidar.equals(valorInicial)&&UtilidadTexto.getBoolean(campo.getMostrar())){
									campoAfectado.setMostrar(ConstantesBD.acronimoSi);
									cumpleValidacion=true;
									break;
								}
							}
							
						}
						if(!cumpleValidacion){
							campoAfectado.setMostrar(ConstantesBD.acronimoNo);
						}
						
						break;
					case DtoAccionCampo.ACCION_CAMBIAR_OBLIGATORIEDAD:
						
						cumpleValidacion=false;
						if(accionCampo.getValorValidar()!=null){
							String[] valoresValidar=accionCampo.getValorValidar().split(",");
							for(String valorValidar:valoresValidar){
								if(valorValidar.equals(valorInicial)&&UtilidadTexto.getBoolean(campo.getMostrar())){
									campoAfectado.setRequerido(ConstantesBD.acronimoSi);
									cumpleValidacion=true;
									break;
								}
							}
							
						}
						if(!cumpleValidacion){
							campoAfectado.setRequerido(ConstantesBD.acronimoNo);
						}
						
						break;
				}
			}
		}
		
		forma.setEstado("empezar");
		
		return mapping.findForward("formato");
	}
	
	/**
	 * Carga los valores por defecto de los campos afectados por las acciones de otros campos
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 */
	public static void cargarValoresDefectoAccionesCampo(Connection con,
			FormatoJustInsNoposForm forma, ActionMapping mapping,
			HttpServletRequest request){
		List<DtoParamSeccionesJusNoPos>secciones=forma.getDtoParam().getSecciones();

		FormatoJustArtNopos fjan = new FormatoJustArtNopos ();
		
		for(int i=0;i<secciones.size();i++){
			List<DtoParamCamposJusNoPos>campos=secciones.get(i).getCampos();
			for(int j=0;j<campos.size();j++){
				DtoParamCamposJusNoPos campo=campos.get(j);
				List<DtoAccionCampo>listaAcciones=fjan.consultarAccionesCampo(con,Integer.parseInt(campo.getCodigo()) , "N");
				for(DtoAccionCampo accionCampo:listaAcciones){
					int []valores=buscarIdentificadoresCampoInsumo(secciones,accionCampo.getCampoAfectado().toString());
					if(valores!=null){
						DtoParamCamposJusNoPos campoAfectado=secciones.get(valores[0]).getCampos().get(valores[1]);
						String valorInicial=campo.getValor();
						boolean cumpleValidacion=false;
						switch(accionCampo.getAccion()){
							case DtoAccionCampo.ACCION_CAMBIAR_VISIBILIDAD:
								
								cumpleValidacion=false;
								if(accionCampo.getValorValidar()!=null){
									String[] valoresValidar=accionCampo.getValorValidar().split(",");
									for(String valorValidar:valoresValidar){
										if(valorValidar.equals(valorInicial)&&UtilidadTexto.getBoolean(campo.getMostrar())){
											campoAfectado.setMostrar(ConstantesBD.acronimoSi);
											cumpleValidacion=true;
											break;
										}
									}
									
								}
								if(!cumpleValidacion){
									campoAfectado.setMostrar(ConstantesBD.acronimoNo);
								}
								
								break;
							case DtoAccionCampo.ACCION_CAMBIAR_OBLIGATORIEDAD:
								
								cumpleValidacion=false;
								if(accionCampo.getValorValidar()!=null){
									String[] valoresValidar=accionCampo.getValorValidar().split(",");
									for(String valorValidar:valoresValidar){
										if(valorValidar.equals(valorInicial)&&UtilidadTexto.getBoolean(campo.getMostrar())){
											campoAfectado.setRequerido(ConstantesBD.acronimoSi);
											cumpleValidacion=true;
											break;
										}
									}
									
								}
								if(!cumpleValidacion){
									campoAfectado.setRequerido(ConstantesBD.acronimoNo);
								}
								
								break;
						}
					}
				}
			}
		}
		
	}


	/**
	 * @param string
	 * @return [posSeccion,posCampo]
	 */
	public static int[] buscarIdentificadoresCampoInsumo(List<DtoParamSeccionesJusNoPos> secciones,String codigoCampo) {
		// TODO Auto-generated method stub
		boolean encontrado=false;
		int retorno[]=null;
		for(int i=0;i<secciones.size();i++){
			List<DtoParamCamposJusNoPos>campos=secciones.get(i).getCampos();
			for(int j=0;j<campos.size();j++){
				DtoParamCamposJusNoPos campo=campos.get(j);
				if(campo.getCodigo().equals(codigoCampo)){
					encontrado=true;
					retorno=new int[]{i,j};
					break;
				}
			}
			if(encontrado){
				break;
			}
		}
		return retorno;
	}
}
