package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadFecha;

public class DtoDetCaPromocionesOdo implements Serializable {
	
	/**
	 * 
	 */
private int codigoPk;
private int detPromocionOdo;
private InfoDatosInt centroAtencion;
private String activo ;
private String fechaInactivacion ;
private String 	horaInactivacion ;
private String 	usuarioInactivacion ;
private String fechaModifica ;
private String horaModifica;
private String usuarioModifica;
private boolean estadoDb;
private boolean visible; 




	
		/**
		 * 
		 */
		public void reset(){
			codigoPk=0;
			detPromocionOdo=0;
			this.centroAtencion = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
			activo="";
			fechaInactivacion="";
			horaInactivacion="";
			usuarioInactivacion="";
			fechaModifica="";
			horaModifica="";	
			usuarioModifica="";
			estadoDb=false;
			this.visible=true;
		
		}

	
		
		/**
		 * 
		 */
		public  DtoDetCaPromocionesOdo (){
			this.reset();
		}



		/**
		 * 
		 * @param centroAtencion
		 */
		public void setCentroAtencion(InfoDatosInt centroAtencion) {
			this.centroAtencion = centroAtencion;
		}
		/**
		 * 
		 * @return
		 */
		public InfoDatosInt getCentroAtencion() {
			return centroAtencion;
		}



		/**
		 * @return the codigoPk
		 */
		public int getCodigoPk() {
			return codigoPk;
		}




		/**
		 * @param codigoPk the codigoPk to set
		 */
		public void setCodigoPk(int codigoPk) {
			this.codigoPk = codigoPk;
		}




		/**
		 * @return the detPromocionOdo
		 */
		public int getDetPromocionOdo() {
			return detPromocionOdo;
		}




		/**
		 * @param detPromocionOdo the detPromocionOdo to set
		 */
		public void setDetPromocionOdo(int detPromocionOdo) {
			this.detPromocionOdo = detPromocionOdo;
		}




		/**
		 * @return the activo
		 */
		public String getActivo() {
			return activo;
		}




		/**
		 * @param activo the activo to set
		 */
		public void setActivo(String activo) {
			this.activo = activo;
		}




		/**
		 * @return the fecha_inactivacion
		 */
		public String getFechaInactivacion() {
			return fechaInactivacion;
		}




		/**
		 * @param fecha_inactivacion the fecha_inactivacion to set
		 */
		public void setFechaInactivacion(String fechaInactivacion) {
			this.fechaInactivacion = fechaInactivacion;
		}
		
		/**
		 * 
		 * @return
		 */
		public String getFechaInactivacionDB()
		{
			return  UtilidadFecha.conversionFormatoFechaABD(this.fechaInactivacion);
		}





		/**
		 * @return the hora_inactivacion
		 */
		public String getHoraInactivacion() {
			return horaInactivacion;
		}




		/**
		 * @param hora_inactivacion the hora_inactivacion to set
		 */
		public void setHoraInactivacion(String horaInactivacion) {
			this.horaInactivacion = horaInactivacion;
		}




		/**
		 * @return the usuarioInactivacion
		 */
		public String getUsuarioInactivacion() {
			return usuarioInactivacion;
		}

		
		


		/**
		 * @param usuarioInactivacion the usuarioInactivacion to set
		 */
		public void setUsuarioInactivacion(String usuarioInactivacion) {
			this.usuarioInactivacion = usuarioInactivacion;
		}




		/**
		 * @return the fechaModifica
		 */
		public String getFechaModifica() {
			return fechaModifica;
		}
		
		/**
		 * 
		 * @return
		 */
		public String getFechaModificaBD()
		
		{
			return  UtilidadFecha.conversionFormatoFechaABD(this.fechaModifica);
		}

		
		
		



		/**
		 * @param fechaModifica the fechaModifica to set
		 */
		public void setFechaModifica(String fechaModifica) {
			this.fechaModifica = fechaModifica;
		}




		/**
		 * @return the horaModifica
		 */
		public String getHoraModifica() {
			return horaModifica;
		}




		/**
		 * @param horaModifica the horaModifica to set
		 */
		public void setHoraModifica(String horaModifica) {
			this.horaModifica = horaModifica;
		}




		/**
		 * @return the usuarioModifica
		 */
		public String getUsuarioModifica() {
			return usuarioModifica;
		}




		/**
		 * @param usuarioModifica the usuarioModifica to set
		 */
		public void setUsuarioModifica(String usuarioModifica) {
			this.usuarioModifica = usuarioModifica;
		}




		public void setEstadoDb(boolean estadoDb) {
			this.estadoDb = estadoDb;
		}




		public boolean isEstadoDb() {
			return estadoDb;
		}



		public void setVisible(boolean visible) {
			this.visible = visible;
		}



		public boolean isVisible() {
			return visible;
		}
			




}
